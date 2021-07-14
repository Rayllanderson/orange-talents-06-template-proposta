package br.com.zupacademy.rayllanderson.proposta.cards.schedules;

import br.com.zupacademy.rayllanderson.proposta.proposal.creators.ProposalPostRequestCreator;
import br.com.zupacademy.rayllanderson.proposta.proposal.repository.ProposalRepository;
import br.com.zupacademy.rayllanderson.proposta.proposal.requests.ProposalPostRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class CardAssociationScheduledTest {

    @Value("${associate.card.fixed.delay}")
    private String scheduledFixedDelay;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private MockMvc mockMvc;

    private final Gson gson = new Gson();

    @Test
    @DisplayName("Should create a card to a proposal when proposal is eligible and haven't card")
    void shouldCreateCardToEligibleProposal() throws Exception {

        //garantindo que não existem propostas elegíveis
       assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().hasSize(0).isEmpty();

       //criando uma proposta elegível
        saveEligibleProposalWithoutCard();

        //garantindo que agora tem uma proposta elegível
        assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().isNotEmpty().hasSize(1);

        //simulando o tempo de espera do scheduled
        Thread.sleep(Long.parseLong(scheduledFixedDelay));

        //garantindo que o scheduled foi executado e não existem mais propostas elegíveis
        assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().hasSize(0).isEmpty();
    }

    @Test
    @DisplayName("Shouldn't create a card to a proposal when proposal is not eligible")
    void shouldNotCreateCardToNotEligibleProposal() throws Exception {

        //garantindo que não existem propostas elegíveis
        assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().hasSize(0).isEmpty();

        //criando uma proposta não elegível
        saveNotEligibleProposal();

        //garantindo novamente que não existem propostas elegíveis, mesmo depois de salvar uma nova proposta (não elegível)
        assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().hasSize(0).isEmpty();

        //simulando o tempo de espera do scheduled
        Thread.sleep(Long.parseLong(scheduledFixedDelay));

        //confirmando que não existem propostas elegíveis mesmo depois que o scheduled foi executado
        assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().hasSize(0).isEmpty();
    }


    private void saveProposal(Object request) throws Exception {
        mockMvc.perform(post("/proposals").content(this.gson.toJson(request)).contentType(MediaType.APPLICATION_JSON));
    }

    private void saveEligibleProposalWithoutCard() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createAValidProposalToBeSavedWithCPF();
        saveProposal(request);
    }

    private void saveNotEligibleProposal() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createANotEligibleProposalToBeSavedWithCNPJ();
        saveProposal(request);
    }
}