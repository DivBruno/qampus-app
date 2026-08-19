package com.project.qampus.dto;

import com.project.qampus.model.Answer;

import java.time.LocalDateTime;

public record AnswerResponseDTO(
        String id,
        String content,
        String userId,
        String postId,
        LocalDateTime createdAt
) {

    public static AnswerResponseDTO from(Answer answer) {
        return new AnswerResponseDTO(
                answer.getId(),
                answer.getContent(),
                answer.getUser().getId(),
                answer.getPost().getId(),
                answer.getCreatedAt()
        );
    }
}