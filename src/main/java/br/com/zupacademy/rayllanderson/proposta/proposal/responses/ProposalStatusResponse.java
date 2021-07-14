package br.com.zupacademy.rayllanderson.proposta.proposal.responses;

import br.com.zupacademy.rayllanderson.proposta.proposal.enums.ProposalStatus;

public class ProposalStatusResponse {

    private final String status;

    public ProposalStatusResponse(ProposalStatus status) {
        this.status = status.toString();
    }

    public String getStatus() {
        return status;
    }
}
