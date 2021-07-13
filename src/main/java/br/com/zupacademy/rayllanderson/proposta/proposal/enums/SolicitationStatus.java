package br.com.zupacademy.rayllanderson.proposta.proposal.enums;

public enum SolicitationStatus {
    COM_RESTRICAO(ProposalStatus.NOT_ELIGIBLE),
    SEM_RESTRICAO(ProposalStatus.ELIGIBLE);

    private final ProposalStatus status;

    SolicitationStatus(ProposalStatus status) {
        this.status = status;
    }

    public ProposalStatus getProposalStatus(){
        return this.status;
    }
}
