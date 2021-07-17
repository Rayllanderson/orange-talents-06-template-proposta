package br.com.zupacademy.rayllanderson.proposta.travel.request;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.travel.model.TravelNotify;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TravelNotifyExternalRequest {

    @NotBlank
    @JsonProperty("destino")
    private final String destination;

    @NotNull @Future
    @JsonProperty("validoAte")
    private final String endDate;

    @JsonCreator
    public TravelNotifyExternalRequest(LocalDate endDate, String destination) {
        this.endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(endDate);
        this.destination = destination;
    }

    public static TravelNotifyExternalRequest fromTravelNotifyRequest(TravelNotifyRequest request){
        return new TravelNotifyExternalRequest(request.getEndDate(), request.getDestination());
    }
}
