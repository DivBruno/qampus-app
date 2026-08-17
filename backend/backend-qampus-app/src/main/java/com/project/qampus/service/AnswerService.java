package com.project.qampus.service;

import com.project.qampus.dto.AnswerDTO;
import com.project.qampus.model.Answer;
import com.project.qampus.model.Post;
import com.project.qampus.model.User;
import com.project.qampus.repositories.AnswerRepository;
import com.project.qampus.repositories.PostRepository;
import com.project.qampus.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Answer create(
            String postId,
            AnswerDTO body,
            Authentication authentication
    ) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found... x.x"));

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado."));

        Answer answer = new Answer();

        answer.setContent(body.content());
        answer.setPost(post);
        answer.setUser(user);

        return answerRepository.save(answer);
    }
}