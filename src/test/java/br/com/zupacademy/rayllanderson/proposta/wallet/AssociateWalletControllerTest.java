package br.com.zupacademy.rayllanderson.proposta.wallet;

import br.com.zupacademy.rayllanderson.proposta.cards.model.Card;
import br.com.zupacademy.rayllanderson.proposta.cards.savers.CardSaver;
import br.com.zupacademy.rayllanderson.proposta.core.exceptions.ApiErrorException;
import br.com.zupacademy.rayllanderson.proposta.utils.HeaderUtils;
import br.com.zupacademy.rayllanderson.proposta.wallet.clients.CardAssociatorFeign;
import br.com.zupacademy.rayllanderson.proposta.wallet.models.PayPalWallet;
import br.com.zupacademy.rayllanderson.proposta.wallet.requests.WalletExternalRequest;
import br.com.zupacademy.rayllanderson.proposta.wallet.requests.WalletRequest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(authorities = "SCOPE_card:write")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AssociateWalletControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager manager;
    @Autowired private CardSaver cardSaver;

    @MockBean
    private CardAssociatorFeign associator;

    private final Gson gson = new Gson();
    private final long expectedCardId = 1;
    private final String url = "/cards/" + expectedCardId + "/associate/paypal";

    @Test
    @DisplayName("Should associate card with paypal when Successful")
    void shouldAssociateCardWithPayPal() throws Exception {
        Card expectedCard = cardSaver.saveNewCard();

        BDDMockito.when(associator.associate(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(WalletExternalRequest.class)))
                .thenReturn(ResponseEntity.ok().build());

        String expectedEmail = "paypal@email.com";
        var request = new WalletRequest(expectedEmail);

        long expectedPaypalId = 1;
        String contextPath = "http://localhost";
        String expectedHeaderLocation = contextPath + "/cards/" + expectedCardId + "/wallets/paypal/" +expectedPaypalId;

        MvcResult mvcResult = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request))
        ).andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, expectedHeaderLocation))
                .andReturn();

        String returnedHeader = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);

        assertThat(returnedHeader).isNotNull().isNotBlank();

        long returnedId = HeaderUtils.getReturnedId(returnedHeader);

        assertThat(returnedId).isNotNull();

        PayPalWallet savedWallet = manager.find(PayPalWallet.class, returnedId);

        assertThat(savedWallet).isNotNull();
        assertThat(savedWallet.getEmail()).isEqualTo(expectedEmail);
        assertThat(savedWallet.getCard()).isEqualTo(expectedCard);
    }

    @Test
    @DisplayName("Should return 422 when card is already associated with paypal")
    void shouldReturn422WhenCardAlreadyAssociatedWithPayPal() throws Exception {
        cardSaver.saveNewCard();
        associateCardToPaypal();

        String expectedEmail = "paypal@email.com";
        var request = new WalletRequest(expectedEmail);

       mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request))
        ).andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return 502 when external system fail")
    void shouldReturn502WhenExternalSystemFail() throws Exception {
        cardSaver.saveNewCard();

        BDDMockito.when(associator.associate(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(WalletExternalRequest.class)))
                .thenThrow(new ApiErrorException(HttpStatus.BAD_GATEWAY, ""));

        String expectedEmail = "paypal@email.com";
        var request = new WalletRequest(expectedEmail);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request))
        ).andExpect(status().isBadGateway());
    }

    @Test
    @DisplayName("Should return 400 when request email is not valid")
    void shouldReturn400WhenRequestEmailIsInvalid() throws Exception {
        cardSaver.saveNewCard();

        String expectedEmail = "notValidEmail";
        var request = new WalletRequest(expectedEmail);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when request email is empty")
    void shouldReturn400WhenRequestEmailIsEmpty() throws Exception {
        cardSaver.saveNewCard();

        String expectedEmail = "";
        var request = new WalletRequest(expectedEmail);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when card not exists")
    void shouldReturn404WhenCardNotExists() throws Exception {
        String expectedEmail = "paypal@email.com";
        var request = new WalletRequest(expectedEmail);

        long nonexistentCardId = 10000;
        String url = "/cards/" + nonexistentCardId + "/associate/paypal";

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request))
        ).andExpect(status().isNotFound());
    }

    private void associateCardToPaypal() throws Exception {
        var request = new WalletRequest("paypal@email.com");
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)));
    }


}