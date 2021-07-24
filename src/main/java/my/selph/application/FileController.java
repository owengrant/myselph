package my.selph.application;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Tag(name = "FileController")
@Path("/files")
public class FileController {

    @ConfigProperty(name = "app.folder.selphies")
    String selphiesFolder;

    @GET
    @Path("/{filename}")
    public Response getResponse(@PathParam("filename") String filename) {
        var file = new File(selphiesFolder+File.separator+filename);
        return Response.ok(file).header("content-type", "audio/mpeg").build();
    }

}
