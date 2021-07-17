package br.com.zupacademy.rayllanderson.proposta.travel.model;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TravelNotify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @PastOrPresent
    @Column(nullable = false)
    private LocalDateTime createdIn = LocalDateTime.now();

    @NotBlank
    @Column(nullable = false)
    private String userAgent;

    @NotBlank
    @Column(nullable = false)
    private String ip;

    @NotBlank
    @Column(nullable = false)
    private String destination;

    @NotNull @Future
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull
    @ManyToOne(optional = false)
    private Card card;

    @Deprecated
    private TravelNotify (){}

    public TravelNotify(String userAgent, String ip, String destination,
                        LocalDate endDate, Card card) {
        this.userAgent = userAgent;
        this.ip = ip;
        this.destination = destination;
        this.endDate = endDate;
        this.card = card;
    }

    public Long getId() {
        return id;
    }
}
