package br.com.zupacademy.rayllanderson.proposta.proposal.controllers;

import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.proposal.repository.ProposalRepository;
import br.com.zupacademy.rayllanderson.proposta.proposal.responses.ProposalStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/proposals")
public class CheckProposalStatusController {

    private final ProposalRepository proposalRepository;

    private final Logger logger = LoggerFactory.getLogger(CheckProposalStatusController.class);

    @Autowired
    public CheckProposalStatusController(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        logger.info("Proposta de id {} foi consultada", id);

        Proposal proposal = proposalRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(new ProposalStatusResponse(proposal.getStatus()));
    }
}
