package my.selph.application;

import my.selph.application.dtos.SelphieGet;
import my.selph.application.dtos.SelphiePost;
import my.selph.application.mappers.SelphieMapper;
import my.selph.domain.ai.POSProcessor;
import my.selph.domain.ai.TokenPOS;
import my.selph.domain.ai.TokenPOSMatcher;
import my.selph.domain.entities.Selphie;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "SelphieController")
@Path("/api/v1/selphies")
@Produces(MediaType.APPLICATION_JSON)
public class SelphieController {

    @ConfigProperty(name = "app.folder.selphies")
    String selphiesFolder;

    @Inject
    SelphieMapper selphieMapper;

    @Inject
    POSProcessor posProcessor;

    @Inject
    TokenPOSMatcher tokenPOSMatcher;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RequestBody(required = true, content = @Content(
            mediaType = MediaType.MULTIPART_FORM_DATA,
            schema = @Schema(implementation = SelphiePost.class)
        )
    )
    @Transactional
    public SelphieGet createSelphie(@MultipartForm SelphiePost body) throws IOException {
        var newSelphie = selphieMapper.fromPost(body);
        var fullFilename = Paths.get(selphiesFolder+ File.separator+newSelphie.getResponse());
        Files.copy(body.getResponse().toPath(), fullFilename);
        Selphie.persist(newSelphie);
        return selphieMapper.fromEntity(newSelphie);
    }

    @GET
    @Transactional
    public List<SelphieGet> findAllSelphies() {
        var selphies = Selphie.<Selphie>listAll();
        return selphies.stream().map(i -> selphieMapper.fromEntity(i)).toList();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteSelphie(@PathParam("id") long id) {
        if(Selphie.deleteById(id))
            return Response.noContent().build();
        throw new InternalServerErrorException();
    }

    @GET
    @Path("/{ask}")
    @Transactional
    public SelphieGet askSelph(@QueryParam("q") String question) throws IOException {
        var selphies = Selphie.<Selphie>listAll();
        var knowledge = posProcessor.extractPOS(selphies.stream().map(Selphie::getQuestion).toList());
        var questionTokenPOS = posProcessor.extractPOS(question);
        var bestMatchIndex = tokenPOSMatcher.match(questionTokenPOS, knowledge);
        Selphie selphie = selphies.get(bestMatchIndex);
        System.out.println("Question POST - "+questionTokenPOS);
        knowledge.forEach(k -> System.out.println("Knowledge - "+k));
        return selphieMapper.fromEntity(selphie);
    }

}
