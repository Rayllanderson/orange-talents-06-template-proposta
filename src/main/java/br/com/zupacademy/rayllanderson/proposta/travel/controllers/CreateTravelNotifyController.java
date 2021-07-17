package br.com.zupacademy.rayllanderson.proposta.travel.controllers;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import br.com.zupacademy.rayllanderson.proposta.travel.model.TravelNotify;
import br.com.zupacademy.rayllanderson.proposta.travel.request.TravelNotifyRequest;
import br.com.zupacademy.rayllanderson.proposta.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
public class CreateTravelNotifyController {

    private final Logger logger = LoggerFactory.getLogger(CreateTravelNotifyController.class);

    private final EntityManager manager;

    public CreateTravelNotifyController(EntityManager manager) {
        this.manager = manager;
    }

    @Transactional
    @PostMapping("/{id}/travels/notify")
    public ResponseEntity<?> createNotify(@PathVariable Long id, HttpServletRequest servletRequest,
                                        @RequestBody @Valid TravelNotifyRequest request){
        String ip = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader(HttpHeaders.USER_AGENT);
        Assert.notNull(ip, userAgent);

        logger.info("Está tendo uma tentativa de aviso de viagem para o cartão de id {}", id);

        Card card = Optional.ofNullable(manager.find(Card.class, id)).orElseThrow(() -> {
            logger.warn("Tentativa de aviso de viagem falhou. Motivo: Cartão não encontrado");
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "Cartão não encontrado");
        });

        TravelNotify travelNotify = request.toModel(ip, userAgent, card);

        manager.persist(travelNotify);

        logger.info("Novo Aviso de viagem criado [id {}] para o cartão {}", travelNotify.getId(), card.getNumber());

        return ResponseEntity.ok().build();
    }

}
