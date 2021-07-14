package br.com.zupacademy.rayllanderson.proposta.proposal.repository;

import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    @Query("FROM Proposal AS p WHERE p.status = 'ELIGIBLE' AND p.card = null")
    List<Proposal> findAllEligibleWithoutCard();
}
