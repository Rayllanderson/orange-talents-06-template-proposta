package br.com.zupacademy.rayllanderson.proposta.cards.responses;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardSolicitationResponse {

    @JsonProperty("id")
    private final String number;

    @JsonProperty("emitidoEm")
    private final LocalDateTime emissionDate;

    @JsonProperty("titular")
    private final String ownerName;

    @JsonProperty("limite")
    private final BigDecimal limit;

    @JsonProperty("vencimento")
    private final DueDateResponse dueDate;

    public CardSolicitationResponse(String number, LocalDateTime emissionDate, String ownerName, BigDecimal limit,
                                    DueDateResponse dueDate) {
        this.number = number;
        this.emissionDate = emissionDate;
        this.ownerName = ownerName;
        this.limit = limit;
        this.dueDate = dueDate;
    }

    public Card toModel(Proposal proposal){
        return new Card(this.number, this.emissionDate, this.ownerName, this.limit, this.dueDate.getDay(), proposal);
    }

    public String getNumber() {
        return number;
    }
}
