package br.com.zupacademy.rayllanderson.proposta.api.metrics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ActuatorTest {

    @Autowired
    private MockMvc mockMvc;

    private final String HEALTH_URL = "/actuator/health";

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should validate if the application is UP when authenticated user is admin")
    void shouldValidateIfTheApplicationIsUp() throws Exception {
        mockMvc.perform(get(HEALTH_URL))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when user is not an admin")
    void shouldReturn403WhenUserIsNotAuthorized() throws Exception {
        mockMvc.perform(get(HEALTH_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 401 when user is not authenticated")
    void shouldReturn401WhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get(HEALTH_URL))
                .andExpect(status().isUnauthorized());
    }
}
