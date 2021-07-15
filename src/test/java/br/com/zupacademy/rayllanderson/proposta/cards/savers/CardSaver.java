package br.com.zupacademy.rayllanderson.proposta.cards.savers;

import br.com.zupacademy.rayllanderson.proposta.cards.creators.CardCreator;
import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.proposal.saver.ProposalSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class CardSaver {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ProposalSaver proposalSaver;

    @Transactional
    public Card saveNewCard(){
        Proposal newProposal = proposalSaver.saveNewProposal();
        Card newCard = CardCreator.createCardToBeSaved(newProposal);
        manager.persist(newCard);
        return newCard;
    }
}
