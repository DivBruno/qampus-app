package com.project.qampus.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.qampus.dto.LoginRequestDTO;
import com.project.qampus.dto.RegisterRequestDTO;
import com.project.qampus.model.enums.Role;
import com.project.qampus.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewUserAndReturnJwtToken() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("Fernanda", "fernanda@qampus.com", "senha123", Role.STUDENT);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fernanda"))
                .andExpect(jsonPath("$.token").exists());

        assertTrue(userRepository.findByEmail("fernanda@qampus.com").isPresent());
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringDuplicateEmail() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("Fernanda", "fernanda@qampus.com", "senha123", Role.STUDENT);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginSuccessfullyAndReturnJwtToken() throws Exception {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO("Roberto", "roberto@qampus.com", "senha123", Role.PROFESSOR);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk());

        LoginRequestDTO loginDTO = new LoginRequestDTO("roberto@qampus.com", "senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Roberto"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldReturnBadRequestOnLoginWithWrongPassword() throws Exception {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO("Roberto", "roberto@qampus.com", "senha123", Role.PROFESSOR);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk());

        LoginRequestDTO loginDTO = new LoginRequestDTO("roberto@qampus.com", "wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLogoutUserSuccessfully() throws Exception {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO("Mariana", "mariana@qampus.com", "senha123", Role.STUDENT);

        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseContent).get("token").asText();

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
