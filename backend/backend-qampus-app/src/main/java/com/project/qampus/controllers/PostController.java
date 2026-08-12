package com.project.qampus.controllers;

import com.project.qampus.dto.PostDTO;
import com.project.qampus.dto.PostResponseDTO;
import com.project.qampus.model.Post;
import com.project.qampus.repositories.PostRepository;
import com.project.qampus.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class PostController {

    private final PostRepository repository;
    private final PostService postService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostDTO body) {
        Post saved = postService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponseDTO.from(saved));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        List<PostResponseDTO> posts = postService.findAll().stream().map(PostResponseDTO::from).toList();

        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPosts(@PathVariable String id){
        Post post = postService.findById(id);
        
        return ResponseEntity.ok(PostResponseDTO.from(post));
    }

}
