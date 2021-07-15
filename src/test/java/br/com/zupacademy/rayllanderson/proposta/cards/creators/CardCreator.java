package br.com.zupacademy.rayllanderson.proposta.cards.creators;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardCreator {

    public static Card createCardToBeSaved(Proposal proposal){
        return new Card("44444", LocalDateTime.now(), "Ray", BigDecimal.TEN, 31, proposal);
    }
}
