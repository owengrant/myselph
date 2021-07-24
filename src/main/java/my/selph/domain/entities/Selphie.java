package my.selph.domain.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "selphie")
public class Selphie extends PanacheEntity {

    @Column
    private String question;

    @Column
    private String response;

    public String getQuestion() {
        return question;
    }

    public Selphie setQuestion(String question) {
        this.question = question;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public Selphie setResponse(String response) {
        this.response = response;
        return this;
    }
}
