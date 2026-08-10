package com.project.qampus.service;

import com.project.qampus.model.Tag;
import com.project.qampus.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private Tag existingTag;

    @BeforeEach
    void setUp() {
        existingTag = new Tag("tag-1", "backend", Set.of());
    }

    @Test
    void shouldReturnEmptySetWhenTagNamesIsNull() {
        Set<Tag> result = tagService.resolveTags(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tagRepository, never()).findByName(anyString());
    }

    @Test
    void shouldReuseExistingTagWhenFoundInDatabase() {
        when(tagRepository.findByName("backend")).thenReturn(Optional.of(existingTag));

        Set<Tag> result = tagService.resolveTags(Set.of("backend"));

        assertEquals(1, result.size());
        assertTrue(result.contains(existingTag));
        verify(tagRepository, times(1)).findByName("backend");
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void shouldCreateNewTagWhenNotFoundInDatabase() {
        when(tagRepository.findByName("newtag")).thenReturn(Optional.empty());
        Tag newlySavedTag = new Tag("tag-2", "newtag", Set.of());
        when(tagRepository.save(any(Tag.class))).thenReturn(newlySavedTag);

        Set<Tag> result = tagService.resolveTags(Set.of("newtag"));

        assertEquals(1, result.size());
        assertTrue(result.contains(newlySavedTag));
        verify(tagRepository, times(1)).findByName("newtag");
        verify(tagRepository, times(1)).save(any(Tag.class));
    }
}
