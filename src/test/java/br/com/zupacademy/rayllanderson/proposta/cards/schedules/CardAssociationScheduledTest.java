package br.com.zupacademy.rayllanderson.proposta.cards.schedules;

import br.com.zupacademy.rayllanderson.proposta.proposal.repository.ProposalRepository;
import br.com.zupacademy.rayllanderson.proposta.proposal.saver.ProposalPostRequestSaver;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser(authorities = {"SCOPE_card:write", "SCOPE_proposal:write", "SCOPE_proposal:read"} )
class CardAssociationScheduledTest {

    @Value("${associate.card.fixed.delay}")
    private String scheduledFixedDelay;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private MockMvc mockMvc;

    private ProposalPostRequestSaver proposalSaver;

    private final Gson gson = new Gson();

    @BeforeEach
    void setUp(){
        this.proposalSaver = new ProposalPostRequestSaver(mockMvc);
    }

    @Test
    @DisplayName("Should create a card to a proposal when proposal is eligible and haven't card")
    void shouldCreateCardToEligibleProposal() throws Exception {

        //garantindo que não existem propostas elegíveis
       assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().hasSize(0).isEmpty();

       //criando uma proposta elegível
        proposalSaver.saveEligibleProposalWithoutCard();

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
        proposalSaver.saveNotEligibleProposal();

        //garantindo novamente que não existem propostas elegíveis, mesmo depois de salvar uma nova proposta (não elegível)
        assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().hasSize(0).isEmpty();

        //simulando o tempo de espera do scheduled
        Thread.sleep(Long.parseLong(scheduledFixedDelay));

        //confirmando que não existem propostas elegíveis mesmo depois que o scheduled foi executado
        assertThat(proposalRepository.findAllEligibleWithoutCard()).isNotNull().hasSize(0).isEmpty();
    }
}