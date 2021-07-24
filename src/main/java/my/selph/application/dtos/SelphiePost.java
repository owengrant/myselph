package my.selph.application.dtos;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;

public class SelphiePost {

    @Schema(format = "binary", type = SchemaType.STRING)
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @FormParam("response")
    @NotNull
    private File response;

    @FormParam("question")
    @NotBlank
    private String question;

    public File getResponse() {
        return response;
    }

    public SelphiePost setResponse(File video) {
        this.response = video;
        return this;
    }

    public String getQuestion() {
        return question;
    }

    public SelphiePost setQuestion(String question) {
        this.question = question;
        return this;
    }
}
