package br.com.zupacademy.rayllanderson.proposta.proposal.creators;

import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;

public class ProposalCreator {

    public static Proposal createProposalToBeSaved(){
        return new Proposal("26067995000100",
                "ray@email.com", "Ray",
                "avenida 10", 50.0);
    }
}
