package br.com.zupacademy.rayllanderson.proposta.cards.model;

import br.com.zupacademy.rayllanderson.proposta.biometrics.model.Biometry;
import br.com.zupacademy.rayllanderson.proposta.cards.block.model.CardBlock;
import br.com.zupacademy.rayllanderson.proposta.cards.enums.CardStatus;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.wallet.models.Wallet;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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

    @OneToMany(mappedBy = "card")
    private Set<Biometry> biometrics = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private CardStatus status = CardStatus.UNBLOCKED;

    @OneToMany(mappedBy = "card", cascade = CascadeType.MERGE)
    private List<CardBlock> blocks = new ArrayList<>();

    @OneToMany(mappedBy = "card")
    private Set<Wallet> wallets;

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

    public Long getId() {
        return id;
    }

    public boolean isBlocked() {
        return this.status.equals(CardStatus.BLOCKED);
    }

    /**
     * Retorna a lista de bloqueios do cartão.
     * OBS: Read only
     */
    public List<CardBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    /**
     * Realiza o bloqueio do cartão.
     * @param ip não deve ser vazio ou nulo
     * @param userAgent não deve ser vazio ou nulo
     */
    public void block(@NotBlank String ip, @NotBlank String userAgent) {
        Assert.state(!isBlocked(), "Não pode bloquear um cartão já bloqueado");
        Assert.state(ip != null, "Não é possível bloquear um cartão sem um ip");
        Assert.state(userAgent != null, "Não é possível bloquear um cartão sem o User agent");
        Assert.state(!ip.isBlank(), "Não é possível bloquear um cartão sem um ip");
        Assert.state(!userAgent.isBlank(), "Não é possível bloquear um cartão sem o User agent");

        this.status = CardStatus.BLOCKED;
        this.blocks.add(new CardBlock(ip, userAgent, this));
    }

    private boolean hasWallet(Wallet wallet){
        return this.wallets.contains(wallet);
    }

    /**
     * @return True caso associe com sucesso. False caso já tenha associado.
     */
    public boolean associateWallet(@NotNull Wallet wallet){
        Assert.notNull(wallet, "Não é possível associar uma carteira vazia ao cartão.");
        if(hasWallet(wallet)) return false;
        this.wallets.add(wallet);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
