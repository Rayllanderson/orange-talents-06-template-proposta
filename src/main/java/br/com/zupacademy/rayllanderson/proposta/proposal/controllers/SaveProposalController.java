package br.com.zupacademy.rayllanderson.proposta.proposal.controllers;

import br.com.zupacademy.rayllanderson.proposta.dao.TransactionExecutor;
import br.com.zupacademy.rayllanderson.proposta.proposal.enums.SolicitationStatus;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.proposal.requests.ProposalPostRequest;
import br.com.zupacademy.rayllanderson.proposta.proposal.services.ProposalEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/proposals")
public class SaveProposalController {

    private final TransactionExecutor executor;
    private final ProposalEvaluator evaluator;
    private final Logger logger = LoggerFactory.getLogger(SaveProposalController.class);

    public SaveProposalController(TransactionExecutor executor, ProposalEvaluator evaluator) {
        this.executor = executor;
        this.evaluator = evaluator;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ProposalPostRequest request, UriComponentsBuilder uriBuilder) {
        Proposal proposalToBeSaved = request.toModel();
        executor.save(proposalToBeSaved);

        SolicitationStatus solicitationStatus = evaluator.evaluate(proposalToBeSaved);
        proposalToBeSaved.setStatus(solicitationStatus.getProposalStatus());

        executor.update(proposalToBeSaved);

        logger.info("Proposta documento={} criada com sucesso!", proposalToBeSaved.getDocument());

        URI uri = uriBuilder.path("/proposals/{id}").build(proposalToBeSaved.getId());
        return ResponseEntity.created(uri).build();
    }
}
