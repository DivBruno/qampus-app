package com.project.qampus.controllers;

import com.project.qampus.dto.AnswerDTO;
import com.project.qampus.dto.AnswerResponseDTO;
import com.project.qampus.dto.PostResponseDTO;
import com.project.qampus.model.Answer;
import com.project.qampus.model.Post;
import com.project.qampus.model.enums.VoteType;
import com.project.qampus.service.AnswerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.project.qampus.model.User;

@RestController
@RequestMapping("/post/{postId}/answer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AnswerResponseDTO> createAnswer(
            @PathVariable String postId,
            @Valid @RequestBody AnswerDTO body,
            Authentication authentication) {

        Answer answer = answerService.create(
                postId,
                body,
                authentication
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AnswerResponseDTO.from(answer));
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<AnswerResponseDTO> upvote(@PathVariable String id, @AuthenticationPrincipal User user){
        Answer answer = answerService.vote(id, VoteType.LIKE, user);
        return ResponseEntity.ok(AnswerResponseDTO.from(answer));
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<AnswerResponseDTO> downvote(@PathVariable String id, @AuthenticationPrincipal User user){
        Answer answer = answerService.vote(id, VoteType.DISLIKE, user);
        return ResponseEntity.ok(AnswerResponseDTO.from(answer));
    }

}