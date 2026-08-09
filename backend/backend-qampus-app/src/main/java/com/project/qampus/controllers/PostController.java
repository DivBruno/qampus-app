package com.project.qampus.controllers;

import com.project.qampus.dto.PostDTO;
import com.project.qampus.dto.PostResponseDTO;
import com.project.qampus.model.Post;
import com.project.qampus.repositories.QuestionRepository;
import com.project.qampus.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class PostController {

    private final QuestionRepository repository;
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostDTO body) {
        Post saved = postService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponseDTO.from(saved));
    }
}
