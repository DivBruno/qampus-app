package com.project.qampus.service;

import com.project.qampus.model.Tag;
import com.project.qampus.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repository;

    public Set<Tag> resolveTags(Set<String> tagNames) {
        if (tagNames == null) {
            return Set.of();
        }

        return tagNames.stream()
                .map(this::findOrCreate)
                .collect(Collectors.toSet());
    }

    private Tag findOrCreate(String name) {
        return repository.findByName(name)
                .orElseGet(() -> repository.save(new Tag(null, name, null)));
    }
}
