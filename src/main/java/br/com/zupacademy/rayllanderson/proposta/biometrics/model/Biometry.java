package br.com.zupacademy.rayllanderson.proposta.biometrics.model;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.core.annotations.Base64;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Biometry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String fingerprint;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdIn = LocalDateTime.now();

    @ManyToOne(optional = false)
    private Card card;

    @Deprecated
    private Biometry() {
    }

    public Biometry(@NotBlank @Base64 String fingerprint, @NotNull Card card) {
        this.fingerprint = fingerprint;
        this.card = card;
    }

    public Long getId() {
        return id;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Biometry biometry = (Biometry) o;
        return Objects.equals(fingerprint, biometry.fingerprint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fingerprint);
    }
}
