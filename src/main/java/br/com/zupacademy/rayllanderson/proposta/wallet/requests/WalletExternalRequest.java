package br.com.zupacademy.rayllanderson.proposta.wallet.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class WalletExternalRequest {
    @NotBlank @Email
    private final String email;

    @NotBlank
    @JsonProperty("carteira")
    private final String wallet;

    public WalletExternalRequest(String email, String wallet) {
        this.email = email;
        this.wallet = wallet;
    }
}
