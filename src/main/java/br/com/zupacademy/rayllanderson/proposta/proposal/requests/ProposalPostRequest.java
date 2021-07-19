package br.com.zupacademy.rayllanderson.proposta.proposal.requests;

import br.com.zupacademy.rayllanderson.proposta.core.annotations.CPFOrCNPJ;
import br.com.zupacademy.rayllanderson.proposta.core.annotations.Unique;
import br.com.zupacademy.rayllanderson.proposta.core.annotations.UniqueDocument;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProposalPostRequest {

    @NotBlank
    private final String name;
    @NotBlank
    private final String address;
    @NotBlank @Email
    private final String email;
    @NotBlank @CPFOrCNPJ
    @UniqueDocument
    private final String document;
    @NotNull @Positive
    private final Double salary;

    public ProposalPostRequest(@NotBlank @CPFOrCNPJ String document, @NotBlank String email, @NotBlank String name,
                               @NotBlank String address, @NotNull @Positive Double salary) {
        this.document = document;
        this.email = email;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    public Proposal toModel(){
        return new Proposal(document, email, name, address, salary);
    }

    public String getDocument() {
        return document;
    }
}
