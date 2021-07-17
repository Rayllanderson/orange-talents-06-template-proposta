package br.com.zupacademy.rayllanderson.proposta.cards.block.controllers;

import br.com.zupacademy.rayllanderson.proposta.cards.block.clients.BlockNotificatorFeign;
import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import br.com.zupacademy.rayllanderson.proposta.utils.Assert;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
public class CardBlockerController {

    private final EntityManager manager;
    private final BlockNotificatorFeign notificator;
    private final Logger logger = LoggerFactory.getLogger(CardBlockerController.class);


    @Autowired
    public CardBlockerController(EntityManager manager, BlockNotificatorFeign notificator) {
        this.manager = manager;
        this.notificator = notificator;
    }

    @Transactional
    @PostMapping("/{id}/block")
    public ResponseEntity<?> block(HttpServletRequest request, @PathVariable Long id){
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        Assert.notNull(ip, userAgent);

        Card card = Optional.ofNullable(manager.find(Card.class, id)).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "O cartão não existe")
        );

        logger.info("Está tendo uma tentativa de bloqueio para o cartão {} vindo do user agent {}", card.getNumber(), userAgent);

        if (card.isBlocked()){
            logger.info("A tentativa de bloqueio para o cartão {} falhou. O cartão já está bloqueado", card.getNumber());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já está bloqueado");
        }

        notifyClients(card.getNumber());

        card.block(ip, userAgent);
        manager.merge(card);

        logger.info("A tentativa de bloqueio para o cartão {} foi efetivada com sucesso pelo ip {}", card.getNumber(), ip);
        return ResponseEntity.ok().build();
    }

    private void notifyClients(String number) throws ApiErrorException{
        try{
            notificator.notify(number, Map.of("sistemaResponsavel", "Proposal-Microservice-Ray"));
        } catch (FeignException e){
            logger.error("Não foi possível notificar o bloqueio do cartão para o sistema externo.");
            throw new ApiErrorException(HttpStatus.BAD_GATEWAY, "Não foi possível realizar o bloqueio do cartão nesse momento");
        }
    }
}
