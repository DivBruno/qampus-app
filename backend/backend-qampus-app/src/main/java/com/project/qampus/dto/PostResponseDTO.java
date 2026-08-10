package com.project.qampus.dto;

import com.project.qampus.model.Post;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record PostResponseDTO(String id, String title, String content, Long upVotes, Long downVotes,
                              Set<TagResponseDTO> tags, LocalDateTime createdAt){
    public static PostResponseDTO from(Post post) {
        Set<TagResponseDTO> tagDTOs = post.getTags().stream()
                .map(TagResponseDTO::from)
                .collect(Collectors.toSet());

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUpVotes(),
                post.getDownVotes(),
                tagDTOs,
                post.getCreated_at()
        );
    }

}