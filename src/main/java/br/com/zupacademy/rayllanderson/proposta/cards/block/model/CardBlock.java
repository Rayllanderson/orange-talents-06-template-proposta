package br.com.zupacademy.rayllanderson.proposta.cards.block.model;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class CardBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String ip;

    @NotBlank
    @Column(nullable = false)
    private String userAgent;

    @NotNull
    private LocalDateTime instant = LocalDateTime.now();

    @NotNull
    @ManyToOne(optional = false)
    private Card card;

    @Deprecated
    private CardBlock(){}

    public CardBlock(@NotBlank String ip, @NotBlank String userAgent, @NotNull Card card) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.card = card;
    }
}
