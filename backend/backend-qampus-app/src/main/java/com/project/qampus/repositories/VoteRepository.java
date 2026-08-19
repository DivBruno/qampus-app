package com.project.qampus.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.qampus.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, String> {
    boolean existsByUserIdAndPostId(String userId, String postId);
    Optional<Vote> findByUserIdAndPostId(String userId, String postId);
    Optional<Vote> findByUserIdAndAnswerId(String userId, String answerId);
}
