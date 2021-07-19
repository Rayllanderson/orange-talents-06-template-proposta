package br.com.zupacademy.rayllanderson.proposta.wallet.models;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class PayPalWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email @NotBlank
    @Column(nullable = false)
    private String email;

    @NotNull
    @JoinColumn(unique = true)
    @OneToOne(optional = false, cascade = CascadeType.MERGE)
    private Card card;

    @Deprecated
    private PayPalWallet() {}

    public PayPalWallet(String email, Card card) {
        this.email = email;
        this.card = card;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Card getCard() {
        return card;
    }
}
