package com.project.qampus.dto;

import com.project.qampus.model.Tag;

public record TagResponseDTO(String id, String name) {
    public static TagResponseDTO from(Tag tag) {
        return new TagResponseDTO(tag.getId(), tag.getName());
    }
}
