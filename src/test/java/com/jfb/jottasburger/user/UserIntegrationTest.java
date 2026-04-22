package com.jfb.jottasburger.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfb.jottasburger.auth.dto.LoginRequest;
import com.jfb.jottasburger.auth.dto.RegisterRequest;
import com.jfb.jottasburger.support.PostgresIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserIntegrationTest extends PostgresIntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAuthenticatedUserProfile() throws Exception {
        String email = "user_" + System.currentTimeMillis() + "@jottasburger.com";
        String password = "123456";

        register(email, password);
        String token = login(email, password);

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    void shouldReturnUnauthorizedWhenAccessingMeWithoutToken() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUpdateUserName() throws Exception {
        String email = "user_" + System.currentTimeMillis() + "@jottasburger.com";
        String password = "123456";

        register(email, password);
        String token = login(email, password);

        mockMvc.perform(put("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Name"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void shouldChangePasswordSuccessfully() throws Exception {
        String email = "user_" + System.currentTimeMillis() + "@jottasburger.com";
        String password = "123456";
        String newPassword = "1234567";

        register(email, password);
        String token = login(email, password);

        mockMvc.perform(patch("/api/users/me/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "currentPassword": "%s",
                                  "newPassword": "%s",
                                  "confirmNewPassword": "%s"
                                }
                                """.formatted(password, newPassword, newPassword)))
                .andExpect(status().isNoContent());

        // senha antiga não funciona
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequest(email, password)
                        )))
                .andExpect(status().isUnauthorized());

        // senha nova funciona
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequest(email, newPassword)
                        )))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailWhenCurrentPasswordIsWrong() throws Exception {
        String email = "user_" + System.currentTimeMillis() + "@jottasburger.com";
        String password = "123456";

        register(email, password);
        String token = login(email, password);

        mockMvc.perform(patch("/api/users/me/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "currentPassword": "wrong",
                                  "newPassword": "1234567",
                                  "confirmNewPassword": "1234567"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWhenPasswordsDoNotMatch() throws Exception {
        String email = "user_" + System.currentTimeMillis() + "@jottasburger.com";
        String password = "123456";

        register(email, password);
        String token = login(email, password);

        mockMvc.perform(patch("/api/users/me/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "currentPassword": "%s",
                                  "newPassword": "1234567",
                                  "confirmNewPassword": "999999"
                                }
                                """.formatted(password)))
                .andExpect(status().isBadRequest());
    }

    private void register(String email, String password) throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new RegisterRequest("Test User", email, password)
                        )))
                .andExpect(status().isCreated());
    }

    private String login(String email, String password) throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequest(email, password)
                        )))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("accessToken").asText();
    }
}