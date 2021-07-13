package br.com.zupacademy.rayllanderson.proposta.proposal.requests;

import br.com.zupacademy.rayllanderson.proposta.core.annotations.CPFOrCNPJ;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SolicitationRequest {

    @JsonProperty("documento")
    @CPFOrCNPJ @NotBlank
    private final String document;

    @NotBlank
    @JsonProperty("nome")
    private final String requesterName;

    @JsonProperty("idProposta")
    @NotNull
    private final long proposalId;

    public SolicitationRequest(Proposal proposal) {
        this.document = proposal.getDocument();
        this.requesterName = proposal.getName();
        this.proposalId = proposal.getId();
    }
}
