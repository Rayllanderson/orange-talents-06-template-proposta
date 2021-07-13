package br.com.zupacademy.rayllanderson.proposta.proposal.services;

import br.com.zupacademy.rayllanderson.proposta.proposal.clients.ProposalSolicitationFeignClient;
import br.com.zupacademy.rayllanderson.proposta.proposal.enums.SolicitationStatus;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.proposal.requests.SolicitationRequest;
import br.com.zupacademy.rayllanderson.proposta.proposal.responses.SolicitationResponse;
import feign.FeignException;
import org.springframework.stereotype.Service;

/**
 * Responsável por avaliar a proposta para checar se é elegível ou não.
 */
@Service
public class ProposalEvaluator {

    private final ProposalSolicitationFeignClient client;

    public ProposalEvaluator(ProposalSolicitationFeignClient client) {
        this.client = client;
    }

    public SolicitationStatus evaluate(Proposal proposal){
        try {
            SolicitationRequest request = new SolicitationRequest(proposal);
            SolicitationResponse response = client.sendSolicitation(request);
            return response.getSolicitationStatus();
        }catch (FeignException.UnprocessableEntity e){
            return SolicitationStatus.COM_RESTRICAO;
        }
    }
}
