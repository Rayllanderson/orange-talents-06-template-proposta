package br.com.zupacademy.rayllanderson.proposta.biometrics.requets;

import br.com.zupacademy.rayllanderson.proposta.biometrics.model.Biometry;
import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.core.annotations.Base64;
import br.com.zupacademy.rayllanderson.proposta.core.annotations.Unique;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class BiometryRequest {

    @NotBlank @Base64
    @Unique(entity = Biometry.class, field = "fingerprint")
    private final String fingerprint;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BiometryRequest(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Biometry toModel(Card card){
        return new Biometry(this.fingerprint, card);
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
