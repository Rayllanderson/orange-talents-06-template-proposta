package br.com.zupacademy.rayllanderson.proposta.wallet.models;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.wallet.enums.WalletName;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private WalletName name;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotNull
    @JoinColumn(unique = true)
    @ManyToOne(cascade = CascadeType.MERGE)
    private Card card;

    private Wallet(){}

    public Wallet(WalletName name, String email, Card card) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return name == wallet.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
