package br.com.zupacademy.rayllanderson.proposta.biometrics.controllers;

import br.com.zupacademy.rayllanderson.proposta.biometrics.model.Biometry;
import br.com.zupacademy.rayllanderson.proposta.biometrics.requets.BiometryRequest;
import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/cards")
public class SaveBiometryController {

    private final EntityManager manager;
    private final Logger logger = LoggerFactory.getLogger(SaveBiometryController.class);

    @Autowired
    public SaveBiometryController(EntityManager manager) {
        this.manager = manager;
    }

    @Transactional
    @PostMapping("/{id}/biometrics")
    public ResponseEntity<?> save(@PathVariable Long id, @RequestBody @Valid BiometryRequest request,
                                  UriComponentsBuilder builder){
        Card card = manager.find(Card.class, id);
        if (card == null) return ResponseEntity.notFound().build();

        Biometry biometryToBeSaved = request.toModel(card);
        manager.persist(biometryToBeSaved);

        logger.info("Nova biometria criada com id {}", biometryToBeSaved.getId());

        URI uri = builder.path("/cards/{id}/biometrics/{biometricId}").build(id, biometryToBeSaved.getId());
        return ResponseEntity.created(uri).build();
    }
}
