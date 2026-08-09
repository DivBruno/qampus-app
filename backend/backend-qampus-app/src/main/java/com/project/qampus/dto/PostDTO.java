package com.project.qampus.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record PostDTO(@NotBlank String title, @NotBlank String content, Set<String> tags) {
}
