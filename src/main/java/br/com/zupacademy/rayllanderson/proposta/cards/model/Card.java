package br.com.zupacademy.rayllanderson.proposta.cards.model;

import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String number;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime emissionDate;

    @NotBlank
    @Column(nullable = false)
    private String ownerName;

    @NotNull
    @Column(nullable = false, name = "card_limit")
    private BigDecimal limit;

    @NotNull
    @Column(nullable = false)
    private Integer dueDate;

    @OneToOne(mappedBy = "card")
    private Proposal proposal;

    @Deprecated
    private Card() {}

    public Card(String number, LocalDateTime emissionDate, String ownerName,
                BigDecimal limit, Integer dueDate, Proposal proposal) {
        this.number = number;
        this.emissionDate = emissionDate;
        this.ownerName = ownerName;
        this.limit = limit;
        this.dueDate = dueDate;
        this.proposal = proposal;
    }

    public String getNumber() {
        return number;
    }
}
