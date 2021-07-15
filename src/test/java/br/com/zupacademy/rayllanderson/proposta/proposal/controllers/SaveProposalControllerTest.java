package br.com.zupacademy.rayllanderson.proposta.proposal.controllers;

import br.com.zupacademy.rayllanderson.proposta.proposal.creators.ProposalPostRequestCreator;
import br.com.zupacademy.rayllanderson.proposta.proposal.enums.ProposalStatus;
import br.com.zupacademy.rayllanderson.proposta.proposal.model.Proposal;
import br.com.zupacademy.rayllanderson.proposta.proposal.requests.ProposalPostRequest;
import br.com.zupacademy.rayllanderson.proposta.proposal.saver.ProposalPostRequestSaver;
import br.com.zupacademy.rayllanderson.proposta.utils.HeaderUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser(authorities = {"SCOPE_proposal:read", "SCOPE_proposal:write"} )
class SaveProposalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager manager;

    private ProposalPostRequestSaver proposalSaver;

    private final Gson gson = new Gson();
    private final String uri = "/proposals";

    @BeforeEach
    void setUp(){
        this.proposalSaver = new ProposalPostRequestSaver(mockMvc);
    }

    @Test
    @DisplayName("Should save a new Proposal with CPF when successful")
    void shouldSaveAProposalWithCPF() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createAValidProposalToBeSavedWithCPF();

        String contextPath = "http://localhost";
        MvcResult mvcResult = mockMvc.perform(
                post(this.uri)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, contextPath + "/proposals/1"))
                .andReturn();

        String returnedHeader = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        assertThat(returnedHeader).isNotNull().isNotBlank();

        long returnedId = HeaderUtils.getReturnedId(returnedHeader);

        assertThat(returnedId).isNotNull();

        Proposal savedProposal = manager.find(Proposal.class, returnedId);

        assertThat(savedProposal).isNotNull();
        assertThat(savedProposal.getStatus()).isEqualTo(ProposalStatus.ELIGIBLE);
    }

    @Test
    @DisplayName("Should save a new Proposal with CNPJ when successful")
    void shouldSaveAProposalWithCNPJ() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createAValidProposalToBeSavedWithCNPJ();

        String contextPath = "http://localhost";
        MvcResult mvcResult = mockMvc.perform(
                post(this.uri)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, contextPath + "/proposals/1"))
                .andReturn();

        String returnedHeader = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        assertThat(returnedHeader).isNotNull().isNotBlank();

        long returnedId = HeaderUtils.getReturnedId(returnedHeader);

        assertThat(returnedId).isNotNull();

        Proposal savedProposal = manager.find(Proposal.class, returnedId);

        assertThat(savedProposal).isNotNull();
        assertThat(savedProposal.getStatus()).isEqualTo(ProposalStatus.ELIGIBLE);
    }

    @Test
    @DisplayName("Should save a new Proposal not eligible with CNPJ when successful")
    void shouldSaveANotEligibleProposalWithCNPJ() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createANotEligibleProposalToBeSavedWithCNPJ();

        String contextPath = "http://localhost";
        MvcResult mvcResult = mockMvc.perform(
                post(this.uri)
                        .content(this.gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, contextPath + "/proposals/1"))
                .andReturn();

        String returnedHeader = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        assertThat(returnedHeader).isNotNull().isNotBlank();

        long returnedId = HeaderUtils.getReturnedId(returnedHeader);

        assertThat(returnedId).isNotNull();

        Proposal savedProposal = manager.find(Proposal.class, returnedId);

        assertThat(savedProposal).isNotNull();
        assertThat(savedProposal.getStatus()).isEqualTo(ProposalStatus.NOT_ELIGIBLE);
    }

    @Test
    @DisplayName("Should return 400 (bad request) when Document is invalid")
    void shouldReturn400WhenDocumentIsInvalid() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createProposalWithDocumentInvalid();

        String expectedField = "document";

        mockMvc.perform(
                post(this.uri)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fieldErrors").isArray())
                .andExpect(jsonPath("fieldErrors.*", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].*", hasSize(2)))
                .andExpect(jsonPath("fieldErrors[0].field").value(expectedField))
                .andExpect(jsonPath("fieldErrors[0].message").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 400 (bad request) when Name is blank")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createProposalWithNameBlank();

        String expectedField = "name";

        mockMvc.perform(
                post(this.uri)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fieldErrors").isArray())
                .andExpect(jsonPath("fieldErrors.*", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].*", hasSize(2)))
                .andExpect(jsonPath("fieldErrors[0].field").value(expectedField))
                .andExpect(jsonPath("fieldErrors[0].message").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 400 (bad request) when Address is null")
    void shouldReturn400WhenAddressIsNull() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createProposalWithAddressNull();

        String expectedField = "address";

        mockMvc.perform(
                post(this.uri)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fieldErrors").isArray())
                .andExpect(jsonPath("fieldErrors.*", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].*", hasSize(2)))
                .andExpect(jsonPath("fieldErrors[0].field").value(expectedField))
                .andExpect(jsonPath("fieldErrors[0].message").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 400 (bad request) when Salary is null")
    void shouldReturn400WhenSalaryIsNull() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createProposalWithSalaryNull();

        String expectedField = "salary";

        mockMvc.perform(
                post(this.uri)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fieldErrors").isArray())
                .andExpect(jsonPath("fieldErrors.*", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].*", hasSize(2)))
                .andExpect(jsonPath("fieldErrors[0].field").value(expectedField))
                .andExpect(jsonPath("fieldErrors[0].message").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 400 (bad request) when Salary is negative")
    void shouldReturn400WhenSalaryIsNegative() throws Exception {
        ProposalPostRequest request = ProposalPostRequestCreator.createProposalWithSalaryNegative();

        String expectedField = "salary";

        mockMvc.perform(
                post(this.uri)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fieldErrors").isArray())
                .andExpect(jsonPath("fieldErrors.*", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].*", hasSize(2)))
                .andExpect(jsonPath("fieldErrors[0].field").value(expectedField))
                .andExpect(jsonPath("fieldErrors[0].message").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 422 (unprocessable entity) when CPF already exists")
    void shouldReturn422WhenCPFExists() throws Exception {

        //salvando uma proposta com cpf válido
        proposalSaver.saveProposalWithCPF();

        //tentando salvar uma proposta com o cpf da proposta salva acima (Já existente)
        ProposalPostRequest request = ProposalPostRequestCreator.createAValidProposalToBeSavedWithCPF();

        String expectedField = "document";

        mockMvc.perform(
                post(this.uri)
                        .content(this.gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("globalErrorMessages").isArray())
                .andExpect(jsonPath("globalErrorMessages.*", hasSize(1)))
                .andExpect(jsonPath("globalErrorMessages[0]").value(containsString(expectedField)));
    }

    @Test
    @DisplayName("Should return 422 (unprocessable entity) when CNPJ already exists")
    void shouldReturn422WhenCNPJExists() throws Exception {

        //salvando uma proposta com CNPJ válido
        proposalSaver.saveProposalWithCNPJ();

        //tentando salvar uma proposta com o CNPJ da proposta salva acima (Já existente)
        ProposalPostRequest request = ProposalPostRequestCreator.createAValidProposalToBeSavedWithCNPJ();

        String expectedField = "document";

        mockMvc.perform(
                post(this.uri)
                        .content(this.gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("globalErrorMessages").isArray())
                .andExpect(jsonPath("globalErrorMessages.*", hasSize(1)))
                .andExpect(jsonPath("globalErrorMessages[0]").value(containsString(expectedField)));
    }
}