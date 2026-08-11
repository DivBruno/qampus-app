package com.project.qampus.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.qampus.dto.PostDTO;
import com.project.qampus.dto.RegisterRequestDTO;
import com.project.qampus.model.enums.Role;
import com.project.qampus.repositories.QuestionRepository;
import com.project.qampus.repositories.TagRepository;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagRepository tagRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    private String validToken;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        questionRepository.deleteAll();
        tagRepository.deleteAll();
        userRepository.deleteAll();

        RegisterRequestDTO registerDTO = new RegisterRequestDTO("Professor Teste", "prof@qampus.com", "senha123", Role.PROFESSOR);
        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        validToken = objectMapper.readTree(responseContent).get("token").asText();
    }

    @Test
    void shouldDenyCreatePostWithoutAuthentication() throws Exception {
        PostDTO postDTO = new PostDTO("Título sem auth", "Conteúdo sem auth", Set.of("tag1"));

        mockMvc.perform(post("/question/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldCreatePostSuccessfullyWithValidJwtToken() throws Exception {
        PostDTO postDTO = new PostDTO("Dúvida sobre Spring Boot", "Como configurar testes de integração?", Set.of("spring", "junit"));

        mockMvc.perform(post("/question/create")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Dúvida sobre Spring Boot"))
                .andExpect(jsonPath("$.content").value("Como configurar testes de integração?"));

        assertEquals(1, questionRepository.count());
    }

    @Test
    void shouldRejectCreatePostAfterUserLogsOut() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());

        PostDTO postDTO = new PostDTO("Título após logout", "Conteúdo após logout", Set.of("tag1"));

        mockMvc.perform(post("/question/create")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDTO)))
                .andExpect(status().isUnauthorized());
    }
}
