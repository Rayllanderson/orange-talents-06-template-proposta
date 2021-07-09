package br.com.zupacademy.rayllanderson.proposta.proposal.controllers;

import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.proposal.requests.ProposalPostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/proposals")
public class SaveProposalController {

    private final EntityManager manager;

    public SaveProposalController(EntityManager manager) {
        this.manager = manager;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> save(@RequestBody @Valid ProposalPostRequest request, UriComponentsBuilder uriBuilder) {
        Proposal proposalToBeSaved = request.toModel();
        manager.persist(proposalToBeSaved);
        URI uri = uriBuilder.path("/proposals/{id}").build(proposalToBeSaved.getId());
        return ResponseEntity.created(uri).build();
    }
}
