package com.project.qampus.service;

import com.project.qampus.dto.PostDTO;
import com.project.qampus.model.Post;
import com.project.qampus.model.Tag;
import com.project.qampus.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostService postService;

    private PostDTO postDTO;
    private Tag tagJava;

    @BeforeEach
    void setUp() {
        postDTO = new PostDTO("Como usar Spring Security?", "Gostaria de exemplos de testes com JWT.", Set.of("java", "spring"));
        tagJava = new Tag("tag-1", "java", Set.of());
    }

    @Test
    void shouldCreatePostSuccessfully() {
        Set<Tag> resolvedTags = Set.of(tagJava);
        when(tagService.resolveTags(postDTO.tags())).thenReturn(resolvedTags);

        Post savedPost = new Post("post-1", postDTO.title(), postDTO.content(), 0L, 0L, resolvedTags, null);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        Post result = postService.create(postDTO);

        assertNotNull(result);
        assertEquals("Como usar Spring Security?", result.getTitle());
        assertEquals("Gostaria de exemplos de testes com JWT.", result.getContent());
        assertEquals(resolvedTags, result.getTags());

        verify(tagService, times(1)).resolveTags(postDTO.tags());
        verify(postRepository, times(1)).save(any(Post.class));
    }
}
