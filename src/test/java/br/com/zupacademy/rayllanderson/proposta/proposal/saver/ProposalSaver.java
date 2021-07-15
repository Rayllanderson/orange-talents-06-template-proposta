package br.com.zupacademy.rayllanderson.proposta.proposal.saver;

import br.com.zupacademy.rayllanderson.proposta.proposal.creators.ProposalCreator;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class ProposalSaver {

    @Autowired
    private EntityManager manager;

    @Transactional
    public Proposal saveNewProposal(){
        Proposal proposalToBeSaved = ProposalCreator.createProposalToBeSaved();
        manager.persist(proposalToBeSaved);
        return proposalToBeSaved;
    }

}
