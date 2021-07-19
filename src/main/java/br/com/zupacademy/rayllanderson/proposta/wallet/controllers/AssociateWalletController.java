package br.com.zupacademy.rayllanderson.proposta.wallet.controllers;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import br.com.zupacademy.rayllanderson.proposta.wallet.clients.CardAssociatorFeign;
import br.com.zupacademy.rayllanderson.proposta.wallet.enums.WalletName;
import br.com.zupacademy.rayllanderson.proposta.wallet.models.Wallet;
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
        return process(id, request, WalletName.PAYPAL, builder);
    }

    @Transactional
    @PostMapping("/{id}/associate/samsung-pay")
    public ResponseEntity<?> associateSamsungPay(@PathVariable Long id, @RequestBody @Valid WalletRequest request,
                                             UriComponentsBuilder builder){
        return process(id, request, WalletName.SAMSUNG_PAY, builder);
    }

    private ResponseEntity<?> process(Long id, WalletRequest request, WalletName walletName, UriComponentsBuilder builder) {

        Card card = Optional.ofNullable(manager.find(Card.class, id)).orElseThrow(() -> {
            logger.warn("Tentativa de associação do cartão de id {} com {} falhou. Motivo: Cartão não existe", id, walletName);
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "Cartão não encontrado");
        });

        Wallet wallet = new Wallet(walletName, request.getEmail(), card);

        boolean hasAlreadyAssociated = !card.associateWallet(wallet);
        if(hasAlreadyAssociated){
            logger.warn("Tentativa de associação do cartão de id {} com {} falhou. " +
                    "Motivo: Cartão já está associado com {}", id, walletName, walletName);
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão já está associado com "
                    + walletName.getEndpointValue());
        }

        tryAssociateCardInExternalService(card.getNumber(), wallet.getEmail(), walletName.toString());

        manager.persist(wallet);
        logger.info("Cartão id {} foi associado com {} com sucesso! id -> {}", id, walletName, wallet.getId());

        URI uri = builder.path("/cards/{id}/wallets/{walletName}/{walletId}")
                .build(id, walletName.getEndpointValue(), wallet.getId());
        return ResponseEntity.created(uri).build();
    }

    private void tryAssociateCardInExternalService(String cardNumber, String email, String walletName) throws ApiErrorException{
        WalletExternalRequest request = new WalletExternalRequest(email, walletName);
        try {
            associator.associate(cardNumber, request);
        } catch (FeignException e){
            logger.error("Não foi possível associar o cartão ao {}. Erro no sistema externo. Status {}", walletName, e.status());
            throw new ApiErrorException(HttpStatus.BAD_GATEWAY, "Não foi possível realizar a associação nesse momento. " +
                    "Tente mais tarde");
        }

    }
}
