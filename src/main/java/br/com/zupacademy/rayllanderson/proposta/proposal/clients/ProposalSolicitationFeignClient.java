package br.com.zupacademy.rayllanderson.proposta.proposal.clients;

import br.com.zupacademy.rayllanderson.proposta.proposal.requests.SolicitationRequest;
import br.com.zupacademy.rayllanderson.proposta.proposal.responses.SolicitationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "solicitacao", url = "#{'${solicitacao.url}'}")
public interface ProposalSolicitationFeignClient {

    @PostMapping
    SolicitationResponse sendSolicitation(SolicitationRequest request);
}
