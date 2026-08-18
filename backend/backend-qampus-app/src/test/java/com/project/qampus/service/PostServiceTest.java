package com.project.qampus.service;

import com.project.qampus.dto.PostDTO;
import com.project.qampus.model.Post;
import com.project.qampus.model.User;
import com.project.qampus.repositories.PostRepository;
import com.project.qampus.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository repository;

    @Mock
    private TagService tagService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostService postService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user-id");
        user.setName("Leonardo");
        user.setEmail("leonardo@qampus.com");
    }

    @Test
    void shouldCreatePostSuccessfully() {
        PostDTO dto = new PostDTO(
                "Título",
                "Conteúdo",
                Set.of()
        );

        when(authentication.getName())
                .thenReturn("leonardo@qampus.com");

        when(userRepository.findByEmail("leonardo@qampus.com"))
                .thenReturn(Optional.of(user));

        when(tagService.resolveTags(Set.of()))
                .thenReturn(Set.of());

        Post savedPost = new Post();
        savedPost.setTitle("Título");
        savedPost.setContent("Conteúdo");
        savedPost.setUser(user);
        savedPost.setTags(Set.of());

        when(repository.save(any(Post.class)))
                .thenReturn(savedPost);

        Post result = postService.create(dto, authentication);

        assertNotNull(result);
        assertEquals("Título", result.getTitle());
        assertEquals("Conteúdo", result.getContent());
        assertEquals(user, result.getUser());

        verify(authentication).getName();
        verify(userRepository).findByEmail("leonardo@qampus.com");
        verify(tagService).resolveTags(Set.of());
        verify(repository).save(any(Post.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        PostDTO dto = new PostDTO(
                "Título",
                "Conteúdo",
                Set.of()
        );

        when(authentication.getName())
                .thenReturn("naoexiste@qampus.com");

        when(userRepository.findByEmail("naoexiste@qampus.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postService.create(dto, authentication)
        );

        assertEquals(
                "Usuário não encontrado.",
                exception.getMessage()
        );

        verify(userRepository)
                .findByEmail("naoexiste@qampus.com");

        verify(repository, never())
                .save(any(Post.class));
    }

    @Test
    void shouldFindAllPosts() {
        Post post1 = new Post();
        post1.setTitle("Post 1");

        Post post2 = new Post();
        post2.setTitle("Post 2");

        when(repository.findAll())
                .thenReturn(java.util.List.of(post1, post2));

        var result = postService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Post 1", result.get(0).getTitle());
        assertEquals("Post 2", result.get(1).getTitle());

        verify(repository).findAll();
    }

    @Test
    void shouldFindPostById() {
        Post post = new Post();
        post.setTitle("Meu post");

        when(repository.findById("1"))
                .thenReturn(Optional.of(post));

        Post result = postService.findById("1");

        assertNotNull(result);
        assertEquals("Meu post", result.getTitle());

        verify(repository).findById("1");
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        when(repository.findById("999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postService.findById("999")
        );

        assertEquals(
                "Post not found... x.x",
                exception.getMessage()
        );

        verify(repository).findById("999");
    }
}