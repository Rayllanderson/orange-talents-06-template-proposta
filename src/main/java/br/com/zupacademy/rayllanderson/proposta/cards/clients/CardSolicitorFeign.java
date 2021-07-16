package br.com.zupacademy.rayllanderson.proposta.cards.clients;

import br.com.zupacademy.rayllanderson.proposta.cards.requests.CardSolicitationRequest;
import br.com.zupacademy.rayllanderson.proposta.cards.responses.CardSolicitationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "card-number-solicitation", url = "#{'${api.cards.url}'}")
public interface CardSolicitorFeign {

    @PostMapping
    CardSolicitationResponse solicit(CardSolicitationRequest request);
}
