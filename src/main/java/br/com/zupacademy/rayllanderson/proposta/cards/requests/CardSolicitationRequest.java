package br.com.zupacademy.rayllanderson.proposta.cards.requests;

import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CardSolicitationRequest {

    @NotBlank
    @JsonProperty("documento")
    private final String document;

    @NotBlank
    @JsonProperty("nome")
    private final String name;

    @NotNull
    @JsonProperty("idProposta")
    private final Long proposalId;

    public CardSolicitationRequest(String document, String name, Long proposalId) {
        this.document = document;
        this.name = name;
        this.proposalId = proposalId;
    }

    public static CardSolicitationRequest fromProposal(Proposal proposal) {
        return new CardSolicitationRequest(proposal.getDocument(), proposal.getName(), proposal.getId());
    }
}
