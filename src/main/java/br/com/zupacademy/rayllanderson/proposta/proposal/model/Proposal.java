package br.com.zupacademy.rayllanderson.proposta.proposal.model;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.cards.responses.CardSolicitationResponse;
import br.com.zupacademy.rayllanderson.proposta.core.annotations.CPFOrCNPJ;
import br.com.zupacademy.rayllanderson.proposta.core.security.ProposalDocumentEncryptor;
import br.com.zupacademy.rayllanderson.proposta.proposal.enums.ProposalStatus;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String document;

    @NotBlank @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotNull @Positive
    @Column(nullable = false)
    private Double salary;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    @JoinColumn(unique = true)
    @OneToOne(cascade = CascadeType.MERGE)
    private Card card;

    @Deprecated
    private Proposal() {
    }

    public Proposal(@NotBlank @CPFOrCNPJ String document, @NotBlank String email, @NotBlank String name,
                    @NotBlank String address, @NotNull @Positive Double salary) {
        this.document = ProposalDocumentEncryptor.encrypt(document);
        this.email = email;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getDocument() {
        return ProposalDocumentEncryptor.decrypt(document);
    }

    public String getName() {
        return name;
    }

    /**
     * Deve ser usado para setar o status da proposta
     * depois de recebe-la de outro serviço externo.
     */
    public void setStatus(@NotNull ProposalStatus status) {
        Assert.notNull(status, "Status não pode ser nulo");
        this.status = status;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    /**
     * Realiza a associação de cartão com a proposta.
     * @param response não deve ser nulo.
     */
    public void assignCard(@NotNull CardSolicitationResponse response){
        Assert.isTrue(this.status.equals(ProposalStatus.ELIGIBLE),
                "Não é possível criar um cartão para uma proposta não elegível");
        this.card = response.toModel(this);
    }
}
