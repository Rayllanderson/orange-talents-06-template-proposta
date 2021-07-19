package br.com.zupacademy.rayllanderson.proposta.proposal.controllers;

import br.com.zupacademy.rayllanderson.proposta.proposal.enums.SolicitationStatus;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.proposal.requests.ProposalPostRequest;
import br.com.zupacademy.rayllanderson.proposta.proposal.services.ProposalEvaluator;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ProposalEvaluator evaluator;
    private final Tracer tracer;
    private final Logger logger = LoggerFactory.getLogger(SaveProposalController.class);

    public SaveProposalController(EntityManager manager, ProposalEvaluator evaluator, Tracer tracer) {
        this.manager = manager;
        this.evaluator = evaluator;
        this.tracer = tracer;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ProposalPostRequest request, UriComponentsBuilder uriBuilder) {

        Span span = tracer.buildSpan("criando-proposta").start();
        span.setTag("criando-proposta-para", request.getDocument());

        Proposal proposalToBeSaved = request.toModel();
        manager.persist(proposalToBeSaved);

        SolicitationStatus solicitationStatus = evaluator.evaluate(proposalToBeSaved);
        proposalToBeSaved.setStatus(solicitationStatus.getProposalStatus());

        manager.merge(proposalToBeSaved);

        logger.info("Proposta documento={} criada com sucesso!", proposalToBeSaved.getDocument());

        URI uri = uriBuilder.path("/proposals/{id}").build(proposalToBeSaved.getId());
        return ResponseEntity.created(uri).build();
    }
}
