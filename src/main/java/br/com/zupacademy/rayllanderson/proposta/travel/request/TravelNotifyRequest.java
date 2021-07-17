package br.com.zupacademy.rayllanderson.proposta.travel.request;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.travel.model.TravelNotify;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TravelNotifyRequest {

    @NotBlank
    private final String destination;

    @NotNull @Future
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private final LocalDate endDate;

    @JsonCreator
    public TravelNotifyRequest(LocalDate endDate, String destination) {
        this.endDate = endDate;
        this.destination = destination;
    }

    public TravelNotify toModel(String ip, String userAgent, Card card) {
        return new TravelNotify(userAgent, ip, destination, endDate, card);
    }
}
