package my.selph.application.dtos;

public class SelphieGet {

    private long id;

    private String response;

    private String question;

    public String getResponse() {
        return response;
    }

    public SelphieGet setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getQuestion() {
        return question;
    }

    public SelphieGet setQuestion(String question) {
        this.question = question;
        return this;
    }

    public long getId() {
        return id;
    }

    public SelphieGet setId(long id) {
        this.id = id;
        return this;
    }
}
