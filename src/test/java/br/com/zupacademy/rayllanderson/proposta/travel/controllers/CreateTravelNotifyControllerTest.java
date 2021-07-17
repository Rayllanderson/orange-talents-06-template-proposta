package br.com.zupacademy.rayllanderson.proposta.travel.controllers;


import br.com.zupacademy.rayllanderson.proposta.cards.savers.CardSaver;
import br.com.zupacademy.rayllanderson.proposta.travel.clients.TravelNotifyExternalFeign;
import br.com.zupacademy.rayllanderson.proposta.travel.creators.TravelNotifyRequestCreator;
import br.com.zupacademy.rayllanderson.proposta.travel.request.TravelNotifyExternalRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(authorities = "SCOPE_card:write")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CreateTravelNotifyControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager manager;
    @Autowired private CardSaver cardSaver;

    private final Gson gson = new Gson();

    @MockBean
    private TravelNotifyExternalFeign notificator;

    @Test
    @DisplayName("Should create a new travel notify when successful")
    void shouldCreateTravelNotifyWhenSuccessful() throws Exception {
        cardSaver.saveNewCard();

        BDDMockito.when(notificator.notify(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(TravelNotifyExternalRequest.class))).thenReturn("");

        var traveNotifyToBeSaved = TravelNotifyRequestCreator.createTraveNotifyRequestToBeSaved();

        long expectedCardId = 1;
        String url = "/cards/" + expectedCardId + "/travels/notify";
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).header("user-agent", "Chrome/5.0")
        .content(gson.toJson(traveNotifyToBeSaved))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 when destination is null")
    void shouldReturn400WhenDestinyIsEmpty() throws Exception {
        cardSaver.saveNewCard();

        var traveNotifyToBeSaved = TravelNotifyRequestCreator.createTraveNotifyRequestWithDestinationEmpty();

        long expectedCardId = 1;
        String url = "/cards/" + expectedCardId + "/travels/notify";
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).header("user-agent", "Chrome/5.0")
                .content(gson.toJson(traveNotifyToBeSaved))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when end date is in the past")
    void shouldReturn400WhenEndDateIsInPast() throws Exception {
        cardSaver.saveNewCard();

        var traveNotifyToBeSaved = TravelNotifyRequestCreator.createTraveNotifyRequestWithEndDateInPast();

        long expectedCardId = 1;
        String url = "/cards/" + expectedCardId + "/travels/notify";
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).header("user-agent", "Chrome/5.0")
                .content(gson.toJson(traveNotifyToBeSaved))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when card not exists")
    void shouldReturn404WhenCardNotExists() throws Exception {
        var traveNotifyToBeSaved = TravelNotifyRequestCreator.createTraveNotifyRequestToBeSaved();

        long expectedCardId = 16545;
        String url = "/cards/" + expectedCardId + "/travels/notify";
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).header("user-agent", "Chrome/5.0")
                .content(gson.toJson(traveNotifyToBeSaved))
        ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when end date is in the past")
    void shouldReturn400WhenUserAgentIsNull() throws Exception {
        cardSaver.saveNewCard();

        var traveNotifyToBeSaved = TravelNotifyRequestCreator.createTraveNotifyRequestToBeSaved();

        long expectedCardId = 1;
        String url = "/cards/" + expectedCardId + "/travels/notify";
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(traveNotifyToBeSaved))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when ip is null")
    void shouldReturn400WhenIpIsNull() throws Exception {
        cardSaver.saveNewCard();

        var traveNotifyToBeSaved = TravelNotifyRequestCreator.createTraveNotifyRequestToBeSaved();

        long expectedCardId = 1;
        String url = "/cards/" + expectedCardId + "/travels/notify";
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).header("user-agent", "Chrome/5.0")
                .with(request -> {
                    request.setRemoteAddr(null);
                    return request;
                })
                .content(gson.toJson(traveNotifyToBeSaved))
        ).andExpect(status().isBadRequest());
    }

}