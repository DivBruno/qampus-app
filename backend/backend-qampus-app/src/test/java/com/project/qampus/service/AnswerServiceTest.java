package com.project.qampus.service;

import com.project.qampus.dto.AnswerDTO;
import com.project.qampus.model.Answer;
import com.project.qampus.model.Post;
import com.project.qampus.model.User;
import com.project.qampus.repositories.AnswerRepository;
import com.project.qampus.repositories.PostRepository;
import com.project.qampus.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private Post post;

    @Mock
    private User user;

    @InjectMocks
    private AnswerService answerService;

    @Test
    void shouldCreateAnswerSuccessfully() {

        AnswerDTO dto = new AnswerDTO(
                "Esta é uma resposta válida."
        );

        when(postRepository.findById("post-1"))
                .thenReturn(Optional.of(post));

        when(authentication.getName())
                .thenReturn("ana@qampus.com");

        when(userRepository.findByEmail("ana@qampus.com"))
                .thenReturn(Optional.of(user));

        when(answerRepository.save(any(Answer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Answer result = answerService.create(
                "post-1",
                dto,
                (User) authentication.getPrincipal()
        );

        assertNotNull(result);

        assertEquals(
                "Esta é uma resposta válida.",
                result.getContent()
        );

        assertSame(
                user,
                result.getUser()
        );

        assertSame(
                post,
                result.getPost()
        );

        verify(postRepository)
                .findById("post-1");

        verify(authentication)
                .getName();

        verify(userRepository)
                .findByEmail("ana@qampus.com");

        verify(answerRepository)
                .save(any(Answer.class));
    }

    @Test
    void shouldThrowExceptionWhenPostDoesNotExist() {

        AnswerDTO dto = new AnswerDTO(
                "Minha resposta."
        );

        when(postRepository.findById("post-1"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> answerService.create(
                        "post-1",
                        dto,
                        (User) authentication.getPrincipal()
                )
        );

        assertEquals(
                "Post not found... x.x",
                exception.getMessage()
        );

        verify(postRepository)
                .findById("post-1");

        verifyNoInteractions(userRepository);
        verifyNoInteractions(answerRepository);
        verifyNoInteractions(authentication);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        AnswerDTO dto = new AnswerDTO(
                "Minha resposta."
        );

        when(postRepository.findById("post-1"))
                .thenReturn(Optional.of(post));

        when(authentication.getName())
                .thenReturn("inexistente@qampus.com");

        when(userRepository.findByEmail("inexistente@qampus.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> answerService.create(
                        "post-1",
                        dto,
                        (User) authentication.getPrincipal()
                )
        );

        assertEquals(
                "Usuário não encontrado.",
                exception.getMessage()
        );

        verify(postRepository)
                .findById("post-1");

        verify(authentication)
                .getName();

        verify(userRepository)
                .findByEmail("inexistente@qampus.com");

        verify(answerRepository, never())
                .save(any(Answer.class));
    }

    @Test
    void shouldUseAuthenticatedUserWhenCreatingAnswer() {

        AnswerDTO dto = new AnswerDTO(
                "Resposta do aluno."
        );

        when(postRepository.findById("post-1"))
                .thenReturn(Optional.of(post));

        when(authentication.getName())
                .thenReturn("ana@qampus.com");

        when(userRepository.findByEmail("ana@qampus.com"))
                .thenReturn(Optional.of(user));

        when(answerRepository.save(any(Answer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Answer result = answerService.create(
                "post-1",
                dto,
                (User) authentication.getPrincipal()
        );

        assertNotNull(result);

        assertSame(
                user,
                result.getUser()
        );

        verify(authentication)
                .getName();

        verify(userRepository)
                .findByEmail("ana@qampus.com");
    }

    @Test
    void shouldAssociateAnswerWithCorrectPost() {

        AnswerDTO dto = new AnswerDTO(
                "Resposta relacionada ao post."
        );

        when(postRepository.findById("post-1"))
                .thenReturn(Optional.of(post));

        when(authentication.getName())
                .thenReturn("ana@qampus.com");

        when(userRepository.findByEmail("ana@qampus.com"))
                .thenReturn(Optional.of(user));

        when(answerRepository.save(any(Answer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Answer result = answerService.create(
                "post-1",
                dto,
                (User) authentication.getPrincipal()
        );

        assertNotNull(result);

        assertSame(
                post,
                result.getPost()
        );

        verify(postRepository)
                .findById("post-1");
    }

    @Test
    void shouldNotSaveAnswerWhenPostDoesNotExist() {

        AnswerDTO dto = new AnswerDTO(
                "Resposta."
        );

        when(postRepository.findById("post-404"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> answerService.create(
                        "post-404",
                        dto,
                        (User) authentication.getPrincipal()
                )
        );

        verify(answerRepository, never())
                .save(any(Answer.class));

        verifyNoInteractions(
                userRepository,
                authentication
        );
    }

    @Test
    void shouldNotSaveAnswerWhenUserDoesNotExist() {

        AnswerDTO dto = new AnswerDTO(
                "Resposta."
        );

        when(postRepository.findById("post-1"))
                .thenReturn(Optional.of(post));

        when(authentication.getName())
                .thenReturn("unknown@qampus.com");

        when(userRepository.findByEmail("unknown@qampus.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> answerService.create(
                        "post-1",
                        dto,
                        (User) authentication.getPrincipal()
                )
        );

        verify(answerRepository, never())
                .save(any(Answer.class));

        verify(userRepository)
                .findByEmail("unknown@qampus.com");
    }
}