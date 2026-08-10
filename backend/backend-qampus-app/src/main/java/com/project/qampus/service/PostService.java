package com.project.qampus.service;

import com.project.qampus.dto.PostDTO;
import com.project.qampus.model.Post;
import com.project.qampus.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final QuestionRepository questionRepository;
    private final TagService tagService;

    public Post create(PostDTO body){
        Post post = new Post();
        post.setTitle(body.title());
        post.setContent(body.content());
        post.setTags(tagService.resolveTags(body.tags()));

        return questionRepository.save(post);
    }
}
