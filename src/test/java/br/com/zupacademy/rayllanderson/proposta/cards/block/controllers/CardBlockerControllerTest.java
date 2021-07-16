package br.com.zupacademy.rayllanderson.proposta.cards.block.controllers;


import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.cards.savers.CardSaver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(authorities = "SCOPE_card:write")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CardBlockerControllerTest {

    @Autowired
    private CardSaver cardSaver;

    @Autowired
    private EntityManager manager;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @DisplayName("Should block the card when successful")
    void shouldBlockCardWhenSuccessful() throws Exception {
        cardSaver.saveNewCard();

        long expectedCardId = 1L;
        String url = "/cards/"+ expectedCardId +"/block";

        mockMvc.perform(post(url).header("user-agent", "Chrome/5.0"))
                .andExpect(status().isOk());

        Card card = manager.find(Card.class, expectedCardId);

        assertThat(card).isNotNull();
        assertThat(card.isBlocked()).isTrue();
        assertThat(card.getBlocks()).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("Should return 422 when card has already been blocked")
    void shouldReturn422WhenCardHasAlreadyBlocked() throws Exception {
        //salvando um cartão já bloqueado
        cardSaver.saveNewCardBlocked();

        long expectedCardId = 1L;
        String url = "/cards/"+ expectedCardId +"/block";

        mockMvc.perform(post(url)
                .header("user-agent", "Chrome/5.0"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    @DisplayName("Should return 400 when ip is null")
    void shouldReturn400WhenIpIsNull() throws Exception {
        cardSaver.saveNewCard();

        long expectedCardId = 1L;
        String url = "/cards/"+ expectedCardId +"/block";

        mockMvc.perform(post(url)
                .with(request -> {
                    request.setRemoteAddr(null);
                    return request;
                })
                .header("user-agent", "Chrome/5.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DisplayName("Should return 400 when user agent is null")
    void shouldReturn400WhenUserAgentIsNull() throws Exception {
        cardSaver.saveNewCard();

        long expectedCardId = 1L;
        String url = "/cards/"+ expectedCardId +"/block";

        mockMvc.perform(post(url))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when card not exists")
    void shouldReturn404WhenCardNotExists() throws Exception {
        long nonexistentCardId = 54151L;

        String url = "/cards/"+ nonexistentCardId +"/block";

        mockMvc.perform(post(url)
                .header("user-agent", "Chrome/5.0"))
                .andExpect(status().isNotFound());
    }

}