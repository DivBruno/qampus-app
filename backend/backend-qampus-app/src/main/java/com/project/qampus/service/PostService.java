package com.project.qampus.service;

import java.util.List;

import com.project.qampus.dto.PostDTO;
import com.project.qampus.model.Post;
import com.project.qampus.repositories.PostRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final TagService tagService;

    public Post create(PostDTO body){
        Post post = new Post();
        post.setTitle(body.title());
        post.setContent(body.content());
        post.setTags(tagService.resolveTags(body.tags()));

        return repository.save(post);
    }

    public List<Post> findAll(){
        return repository.findAll();
    }

    public Post findById(String id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Post not found... x.x"));
    }
}
