package com.project.qampus.dto;

import jakarta.validation.constraints.NotBlank;

public record AnswerDTO(
        @NotBlank String content
) {
}