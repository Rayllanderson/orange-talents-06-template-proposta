package br.com.zupacademy.rayllanderson.proposta.wallet.requests;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class WalletRequest {
    @Email @NotBlank
    private final String email;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public WalletRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
