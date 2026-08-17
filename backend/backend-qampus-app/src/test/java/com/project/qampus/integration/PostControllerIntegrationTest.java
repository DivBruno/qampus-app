package com.project.qampus.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.qampus.dto.PostDTO;
import com.project.qampus.dto.RegisterRequestDTO;
import com.project.qampus.model.User;
import com.project.qampus.model.enums.Role;
import com.project.qampus.repositories.PostRepository;
import com.project.qampus.repositories.TagRepository;
import com.project.qampus.repositories.UserRepository;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private User testUser;

    private Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        postRepository.deleteAll();
        tagRepository.deleteAll();
        userRepository.deleteAll();

        RegisterRequestDTO registerDTO = new RegisterRequestDTO(
                "Aluno Teste",
                "aluno@qampus.com",
                "senha123",
                Role.STUDENT
        );

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(registerDTO)
                        )
        )
                .andExpect(status().isOk());

        testUser = userRepository
                .findByEmail("aluno@qampus.com")
                .orElseThrow();

        authentication = new UsernamePasswordAuthenticationToken(
                testUser.getEmail(),
                null,
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + testUser.getRole().name())
                )
        );
    }

    @Test
    void shouldCreatePostSuccessfullyWithValidAuthentication()
            throws Exception {

        PostDTO postDTO = new PostDTO(
                "Dúvida sobre Spring Boot",
                "Como configurar testes de integração?",
                Set.of("spring", "junit")
        );

        mockMvc.perform(
                post("/post/create")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(postDTO)
                        )
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(
                        jsonPath("$.title")
                                .value("Dúvida sobre Spring Boot")
                )
                .andExpect(
                        jsonPath("$.content")
                                .value(
                                        "Como configurar testes de integração?"
                                )
                );

        assertEquals(1, postRepository.count());
    }

    @Test
    void shouldDenyCreatePostWithoutAuthentication()
            throws Exception {

        PostDTO postDTO = new PostDTO(
                "Título sem auth",
                "Conteúdo sem auth",
                Set.of("tag1")
        );

        mockMvc.perform(
                post("/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(postDTO)
                        )
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldUpdateOwnPostSuccessfully()
            throws Exception {

        PostDTO createDTO = new PostDTO(
                "Título original",
                "Conteúdo original",
                Set.of("java")
        );

        MvcResult result = mockMvc.perform(
                post("/post/create")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(createDTO)
                        )
        )
                .andExpect(status().isCreated())
                .andReturn();

        String postId = objectMapper
                .readTree(
                        result.getResponse().getContentAsString()
                )
                .get("id")
                .asText();

        PostDTO updateDTO = new PostDTO(
                "Título atualizado",
                "Conteúdo atualizado",
                Set.of("java", "spring")
        );

        mockMvc.perform(
                put("/post/" + postId)
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(updateDTO)
                        )
        )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(postId)
                )
                .andExpect(
                        jsonPath("$.title")
                                .value("Título atualizado")
                )
                .andExpect(
                        jsonPath("$.content")
                                .value("Conteúdo atualizado")
                );
    }

    @Test
    void shouldRejectUpdatePostWithoutAuthentication()
            throws Exception {

        PostDTO createDTO = new PostDTO(
                "Título original",
                "Conteúdo original",
                Set.of("java")
        );

        MvcResult result = mockMvc.perform(
                post("/post/create")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(createDTO)
                        )
        )
                .andExpect(status().isCreated())
                .andReturn();

        String postId = objectMapper
                .readTree(
                        result.getResponse().getContentAsString()
                )
                .get("id")
                .asText();

        PostDTO updateDTO = new PostDTO(
                "Título atualizado",
                "Conteúdo atualizado",
                Set.of("spring")
        );

        mockMvc.perform(
                put("/post/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(updateDTO)
                        )
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectUpdatePostWithInvalidData()
            throws Exception {

        PostDTO createDTO = new PostDTO(
                "Título original",
                "Conteúdo original",
                Set.of("java")
        );

        MvcResult result = mockMvc.perform(
                post("/post/create")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(createDTO)
                        )
        )
                .andExpect(status().isCreated())
                .andReturn();

        String postId = objectMapper
                .readTree(
                        result.getResponse().getContentAsString()
                )
                .get("id")
                .asText();

        PostDTO invalidDTO = new PostDTO(
                "",
                "",
                Set.of("java")
        );

        mockMvc.perform(
                put("/post/" + postId)
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(invalidDTO)
                        )
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentPost()
            throws Exception {

        PostDTO updateDTO = new PostDTO(
                "Título atualizado",
                "Conteúdo atualizado",
                Set.of("java")
        );

        ServletException exception = assertThrows(
                ServletException.class,
                () -> mockMvc.perform(
                        put("/post/id-inexistente")
                                .with(authentication(authentication))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(updateDTO)
                                )
                )
        );

        assertEquals(
                "Request processing failed: java.lang.RuntimeException: Post not found... x.x",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectUpdatePostOwnedByAnotherUser()
            throws Exception {

        PostDTO createDTO = new PostDTO(
                "Dúvida do primeiro aluno",
                "Conteúdo da dúvida",
                Set.of("java")
        );

        MvcResult result = mockMvc.perform(
                post("/post/create")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(createDTO)
                        )
        )
                .andExpect(status().isCreated())
                .andReturn();

        String postId = objectMapper
                .readTree(
                        result.getResponse().getContentAsString()
                )
                .get("id")
                .asText();

        RegisterRequestDTO secondUser = new RegisterRequestDTO(
                "Segundo Aluno",
                "aluno2@qampus.com",
                "senha123",
                Role.STUDENT
        );

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(secondUser)
                        )
        )
                .andExpect(status().isOk());

        User secondUserEntity = userRepository
                .findByEmail("aluno2@qampus.com")
                .orElseThrow();

        Authentication secondAuthentication =
                new UsernamePasswordAuthenticationToken(
                        secondUserEntity.getEmail(),
                        null,
                        Collections.singletonList(
                                new SimpleGrantedAuthority(
                                        "ROLE_" +
                                                secondUserEntity
                                                        .getRole()
                                                        .name()
                                )
                        )
                );

        PostDTO updateDTO = new PostDTO(
                "Tentativa de alteração",
                "Outro conteúdo",
                Set.of("spring")
        );

        mockMvc.perform(
                put("/post/" + postId)
                        .with(authentication(secondAuthentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(updateDTO)
                        )
        )
                .andExpect(status().isForbidden());
    }
}