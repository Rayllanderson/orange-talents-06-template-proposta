package br.com.zupacademy.rayllanderson.proposta.cards.schedules;

import br.com.zupacademy.rayllanderson.proposta.cards.clients.CardSolicitorFeign;
import br.com.zupacademy.rayllanderson.proposta.cards.requests.CardSolicitationRequest;
import br.com.zupacademy.rayllanderson.proposta.cards.responses.CardSolicitationResponse;
import br.com.zupacademy.rayllanderson.proposta.dao.TransactionExecutor;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.proposal.repository.ProposalRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CardAssociationScheduled {

    private final ProposalRepository proposalRepository;
    private final CardSolicitorFeign cardNumberSolicitor;
    private final TransactionExecutor executor;

    private final Logger log = LoggerFactory.getLogger(CardAssociationScheduled.class);

    public CardAssociationScheduled(ProposalRepository proposalRepository, CardSolicitorFeign cardNumberSolicitor,
                                    TransactionExecutor executor) {
        this.proposalRepository = proposalRepository;
        this.cardNumberSolicitor = cardNumberSolicitor;
        this.executor = executor;
    }

    @Scheduled(fixedDelayString = "${associate.card.fixed.delay}")
    public void searchProposalsWithoutCardAndAssign() {
        List<Proposal> eligibleProposals = proposalRepository.findAllEligibleWithoutCard();

        log.info("Foram encontradas {} propostas elegíveis", eligibleProposals.size());

        eligibleProposals.forEach(proposal -> {
           try {
               CardSolicitationResponse response = cardNumberSolicitor.solicit(CardSolicitationRequest.fromProposal(proposal));

               proposal.assignCard(response);
               executor.update(proposal);

               log.info("Foi criado um novo cartão de número {} para a proposta de id {}", response.getNumber(), proposal.getId());
           }catch (FeignException e){
               log.error("Não foi possível criar um cartão nesse momento. Motivo: {}. Resposta: {}", e.getMessage(), e.contentUTF8());
           }
        });
    }
}
