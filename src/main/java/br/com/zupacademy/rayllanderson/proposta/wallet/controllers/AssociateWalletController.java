package br.com.zupacademy.rayllanderson.proposta.wallet.controllers;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import br.com.zupacademy.rayllanderson.proposta.wallet.clients.CardAssociatorFeign;
import br.com.zupacademy.rayllanderson.proposta.wallet.models.PayPalWallet;
import br.com.zupacademy.rayllanderson.proposta.wallet.requests.WalletExternalRequest;
import br.com.zupacademy.rayllanderson.proposta.wallet.requests.WalletRequest;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
public class AssociateWalletController {

    private final EntityManager manager;
    private final CardAssociatorFeign associator;
    private final Logger logger = LoggerFactory.getLogger(AssociateWalletController.class);

    @Autowired
    public AssociateWalletController(EntityManager manager, CardAssociatorFeign associator) {
        this.manager = manager;
        this.associator = associator;
    }

    @Transactional
    @PostMapping("/{id}/associate/paypal")
    public ResponseEntity<?> associatePaypal(@PathVariable Long id, @RequestBody @Valid WalletRequest request,
                                             UriComponentsBuilder builder){

        Card card = Optional.ofNullable(manager.find(Card.class, id)).orElseThrow(() -> {
            logger.warn("Tentativa de associação do cartão de id {} com paypal falhou. Motivo: Cartão não existe", id);
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "Cartão não encontrado");
        });

        PayPalWallet wallet = new PayPalWallet(request.getEmail(), card);

        boolean hasAlreadyAssociated = !card.associatePaypal(wallet);
        if(hasAlreadyAssociated){
            logger.warn("Tentativa de associação do cartão de id {} com paypal falhou. " +
                    "Motivo: Cartão já está associado com Paypal", id);
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já está associado com Paypal");
        }

        tryAssociateCardInExternalService(card.getNumber(), wallet.getEmail(), "Paypal");

        manager.persist(wallet);
        logger.info("Cartão id {} foi associado com paypal com sucesso! id paypal -> {}", id, wallet.getId());

        URI uri = builder.path("/cards/{id}/wallets/paypal/{paypalId}").build(id, wallet.getId());
        return ResponseEntity.created(uri).build();
    }

    private void tryAssociateCardInExternalService(String cardNumber, String email, String walletName) throws ApiErrorException{
        WalletExternalRequest request = new WalletExternalRequest(email, walletName);
        try {
            associator.associate(cardNumber, request);
        } catch (FeignException e){
            logger.error("Não foi possível associar o cartão ao {}. Erro no sistema externo. Status {}", walletName, e.status());
            throw new ApiErrorException(HttpStatus.BAD_GATEWAY, "Não foi possível realizar a associação nesse momento. Tente mais tarde");
        }

    }
}
