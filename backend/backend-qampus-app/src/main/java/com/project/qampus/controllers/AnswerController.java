package com.project.qampus.controllers;

import com.project.qampus.dto.AnswerDTO;
import com.project.qampus.dto.AnswerResponseDTO;
import com.project.qampus.model.Answer;
import com.project.qampus.service.AnswerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}