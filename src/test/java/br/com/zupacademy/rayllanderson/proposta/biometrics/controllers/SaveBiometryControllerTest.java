package br.com.zupacademy.rayllanderson.proposta.biometrics.controllers;

import br.com.zupacademy.rayllanderson.proposta.biometrics.creators.BiometryRequestCreator;
import br.com.zupacademy.rayllanderson.proposta.biometrics.model.Biometry;
import br.com.zupacademy.rayllanderson.proposta.biometrics.requets.BiometryRequest;
import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.cards.savers.CardSaver;
import br.com.zupacademy.rayllanderson.proposta.utils.HeaderUtils;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(authorities = {"SCOPE_card:write"} )
class SaveBiometryControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CardSaver cardSaver;
    @PersistenceContext private EntityManager manager;

    private final String baseUrl = "/cards";
    private final Gson gson = new Gson();

    @Test
    @DisplayName("Should save a new biometry when card exists and fingerprint is valid")
    void shouldSaveBiometryWhenCardExists() throws Exception {
        Card savedCard = cardSaver.saveNewCard();
        Assertions.assertThat(savedCard).isNotNull();
        
        Long cardId = savedCard.getId();
        String url = baseUrl + "/" + cardId + "/biometrics";

        BiometryRequest request = BiometryRequestCreator.createAValidBiometryToBeSaved();

        String contextPath = "http://localhost";
        MvcResult mvcResult = mockMvc.perform(post(url)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, contextPath + url + "/1"))
                .andReturn();

        String returnedHeader = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        assertThat(returnedHeader).isNotNull().isNotBlank();

        long returnedId = HeaderUtils.getReturnedId(returnedHeader);

        assertThat(returnedId).isNotNull();

        Biometry savedBiometry = manager.find(Biometry.class, returnedId);

        assertThat(savedBiometry).isNotNull();

        String expectedFingerprint = request.getFingerprint();
        assertThat(savedBiometry.getFingerprint()).isEqualTo(expectedFingerprint);
    }

    @Test
    @DisplayName("Should return 400 when fingerprint is not formatted in base64")
    void shouldReturn400WhenFingerprintIsNotBase64() throws Exception {
        Card savedCard = cardSaver.saveNewCard();
        Assertions.assertThat(savedCard).isNotNull();

        Long cardId = savedCard.getId();
        String url = baseUrl + "/" + cardId + "/biometrics";

        BiometryRequest request = BiometryRequestCreator.createInvalidBiometry();

        mockMvc.perform(post(url)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when card not exists")
    void shouldReturn404WhenCardNotExists() throws Exception {
        String url = baseUrl + "/" + 5000 + "/biometrics";

        BiometryRequest request = BiometryRequestCreator.createAValidBiometryToBeSaved();

        mockMvc.perform(post(url)
                .content(this.gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}