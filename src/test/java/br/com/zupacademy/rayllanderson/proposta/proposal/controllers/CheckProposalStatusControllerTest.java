package br.com.zupacademy.rayllanderson.proposta.proposal.controllers;


import br.com.zupacademy.rayllanderson.proposta.proposal.saver.ProposalPostRequestSaver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CheckProposalStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ProposalPostRequestSaver proposalSaver;

    private final String url = "/proposals";

    @BeforeEach
    void setUp(){
        this.proposalSaver = new ProposalPostRequestSaver(mockMvc);
    }

    @Test
    @DisplayName("Should find an eligible proposal when exists and is eligible")
    void shouldFindAnEligibleProposal() throws Exception {
        proposalSaver.saveEligibleProposalWithoutCard();

        String expectedStatus = "ELIGIBLE";

        mockMvc.perform(get(this.url + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("status").value(expectedStatus));
    }

    @Test
    @DisplayName("Should find a not eligible proposal when exists and is not eligible")
    void shouldFindANotEligibleProposal() throws Exception {
        proposalSaver.saveNotEligibleProposal();

        String expectedStatus = "NOT_ELIGIBLE";

        mockMvc.perform(get(this.url + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("status").value(expectedStatus));
    }

    @Test
    @DisplayName("Should return 404 when proposal not exists")
    void shouldReturn404WhenProposalNotExists() throws Exception {
        mockMvc.perform(get(this.url + "/154548748")).andExpect(status().isNotFound());
    }
}