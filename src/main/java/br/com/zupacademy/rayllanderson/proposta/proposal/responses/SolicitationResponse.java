package br.com.zupacademy.rayllanderson.proposta.proposal.responses;


import br.com.zupacademy.rayllanderson.proposta.proposal.enums.SolicitationStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SolicitationResponse {

    @JsonProperty("resultadoSolicitacao")
    private final SolicitationStatus solicitationStatus;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SolicitationResponse(SolicitationStatus status) {
        this.solicitationStatus = status;
    }

    public SolicitationStatus getSolicitationStatus() {
        return solicitationStatus;
    }
}
