package com.project.qampus.service;

import java.util.List;

import com.project.qampus.dto.PostDTO;
import com.project.qampus.model.Post;
import com.project.qampus.model.User;
import com.project.qampus.repositories.PostRepository;
import com.project.qampus.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final TagService tagService;
    private final UserRepository userRepository;

    public Post create(PostDTO body, Authentication authentication) {

        Post post = new Post();

        post.setTitle(body.title());
        post.setContent(body.content());
        post.setTags(tagService.resolveTags(body.tags()));

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado."));

        post.setUser(user);

        return repository.save(post);
    }

    public List<Post> findAll() {
        return repository.findAll();
    }

    public Post findById(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Post not found... x.x"));
    }

    public Post update(
            String id,
            PostDTO body,
            Authentication authentication
    ) {

        Post post = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Post not found... x.x"));

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(
                    "Você não pode editar esta dúvida."
            );
        }

        post.setTitle(body.title());
        post.setContent(body.content());
        post.setTags(tagService.resolveTags(body.tags()));

        return repository.save(post);
    }
}