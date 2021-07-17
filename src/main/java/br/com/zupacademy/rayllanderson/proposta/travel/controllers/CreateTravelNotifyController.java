package br.com.zupacademy.rayllanderson.proposta.travel.controllers;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import br.com.zupacademy.rayllanderson.proposta.travel.clients.TravelNotifyExternalFeign;
import br.com.zupacademy.rayllanderson.proposta.travel.model.TravelNotify;
import br.com.zupacademy.rayllanderson.proposta.travel.request.TravelNotifyExternalRequest;
import br.com.zupacademy.rayllanderson.proposta.travel.request.TravelNotifyRequest;
import br.com.zupacademy.rayllanderson.proposta.utils.Assert;
import feign.FeignException;
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
    private final TravelNotifyExternalFeign notifyExternal;

    public CreateTravelNotifyController(EntityManager manager, TravelNotifyExternalFeign notifyExternal) {
        this.manager = manager;
        this.notifyExternal = notifyExternal;
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

        notifyToExternalServices(card.getNumber(), request);

        manager.persist(travelNotify);

        logger.info("Novo Aviso de viagem criado [id {}] para o cartão {}", travelNotify.getId(), card.getNumber());

        return ResponseEntity.ok().build();
    }

    private void notifyToExternalServices(String cardNumber, @Valid TravelNotifyRequest request) throws ApiErrorException{
        try{
            notifyExternal.notify(cardNumber, TravelNotifyExternalRequest.fromTravelNotifyRequest(request));
        } catch (FeignException e){
            logger.error("Não foi possível notificar o aviso de viagem para o sistema externo. status {}", e.status());
            throw new ApiErrorException(HttpStatus.BAD_GATEWAY, "Não foi possível criar um novo aviso de viagem nesse momento");
        }
    }

}
