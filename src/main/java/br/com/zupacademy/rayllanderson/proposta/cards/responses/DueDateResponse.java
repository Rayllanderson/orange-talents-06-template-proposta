package br.com.zupacademy.rayllanderson.proposta.cards.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class DueDateResponse {

    private final String id;
    @JsonProperty("dia")
    private final Integer day;
    @JsonProperty("dataDeCriacao")
    private final LocalDateTime createdIn;

    public DueDateResponse(String id, Integer day, LocalDateTime createdIn) {
        this.id = id;
        this.day = day;
        this.createdIn = createdIn;
    }

    public Integer getDay() {
        return day;
    }
}
