package com.project.qampus.service;

import com.project.qampus.dto.PostDTO;
import com.project.qampus.model.Post;
import com.project.qampus.model.User;
import com.project.qampus.model.Vote;
import com.project.qampus.model.enums.VoteType;
import com.project.qampus.repositories.PostRepository;
import com.project.qampus.repositories.UserRepository;
import com.project.qampus.repositories.VoteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

        @Mock
        private PostRepository repository;

        @Mock
        private TagService tagService;

        @Mock
        private UserRepository userRepository;

        @Mock
        private Authentication authentication;

        @Mock
        private VoteRepository voteRepository;

        @InjectMocks
        private PostService postService;

        @Mock
        private Post post;

        private User user;

        @BeforeEach
        void setUp() {
                user = new User();
                user.setId("user-id");
                user.setName("Leonardo");
                user.setEmail("leonardo@qampus.com");
        }

        @Test
        void shouldCreatePostSuccessfully() {
                PostDTO dto = new PostDTO(
                                "Título",
                                "Conteúdo",
                                Set.of());

                when(authentication.getPrincipal())
                                .thenReturn(user);

                when(tagService.resolveTags(Set.of()))
                                .thenReturn(Set.of());

                Post savedPost = new Post();
                savedPost.setTitle("Título");
                savedPost.setContent("Conteúdo");
                savedPost.setUser(user);
                savedPost.setTags(Set.of());

                when(repository.save(any(Post.class)))
                                .thenReturn(savedPost);

                Post result = postService.create(dto, authentication);

                assertNotNull(result);
                assertEquals("Título", result.getTitle());
                assertEquals("Conteúdo", result.getContent());
                assertEquals(user, result.getUser());

                verify(authentication).getPrincipal();
                verify(tagService).resolveTags(Set.of());
                verify(repository).save(any(Post.class));
                verifyNoInteractions(userRepository);
        }

        @Test
        void shouldFindAllPosts() {
                Post post1 = new Post();
                post1.setTitle("Post 1");

                Post post2 = new Post();
                post2.setTitle("Post 2");

                when(repository.findAll())
                                .thenReturn(java.util.List.of(post1, post2));

                var result = postService.findAll();

                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals("Post 1", result.get(0).getTitle());
                assertEquals("Post 2", result.get(1).getTitle());

                verify(repository).findAll();
        }

        @Test
        void shouldFindPostById() {
                post = new Post();
                post.setTitle("Meu post");

                when(repository.findById("1"))
                                .thenReturn(Optional.of(post));

                Post result = postService.findById("1");

                assertNotNull(result);
                assertEquals("Meu post", result.getTitle());

                verify(repository).findById("1");
        }

        @Test
        void shouldThrowExceptionWhenPostNotFound() {
                when(repository.findById("999"))
                                .thenReturn(Optional.empty());

                RuntimeException exception = assertThrows(
                                RuntimeException.class,
                                () -> postService.findById("999"));

                assertEquals(
                                "Post not found... x.x",
                                exception.getMessage());

                verify(repository).findById("999");
        }

        @Test
        void shouldUpdatePostSuccessfully() {

                Set<String> tags = Set.of("Java", "Spring");

                PostDTO dto = new PostDTO(
                                "Novo título",
                                "Novo conteúdo",
                                tags);

                when(repository.findById("post-1"))
                                .thenReturn(Optional.of(post));

                when(authentication.getPrincipal())
                                .thenReturn(user);

                when(post.getUser())
                                .thenReturn(user);

                when(tagService.resolveTags(tags))
                                .thenReturn(Set.of());

                when(repository.save(post))
                                .thenReturn(post);

                Post result = postService.update(
                                "post-1",
                                dto,
                                authentication);

                assertSame(post, result);

                verify(post)
                                .setTitle("Novo título");

                verify(post)
                                .setContent("Novo conteúdo");

                verify(post)
                                .setTags(any());

                verify(tagService)
                                .resolveTags(tags);

                verify(repository)
                                .save(post);
        }

        @Test
        void shouldThrowExceptionWhenVotingOnNonExistingPost() {

                when(repository.findById("post-404"))
                                .thenReturn(Optional.empty());

                RuntimeException exception = assertThrows(
                                RuntimeException.class,
                                () -> postService.vote(
                                                "post-404",
                                                VoteType.LIKE,
                                                user));

                assertEquals(
                                "post não encontrado",
                                exception.getMessage());

                verify(repository)
                                .findById("post-404");

                verifyNoInteractions(voteRepository);
        }

        @Test
        void shouldCreateLikeWhenUserHasNotVoted() {

                when(repository.findById("post-1"))
                                .thenReturn(Optional.of(post));

                when(post.getId())
                                .thenReturn("post-1");

                when(voteRepository.findByUserIdAndPostId(
                                "user-id",
                                "post-1")).thenReturn(Optional.empty());

                when(post.getUpVotes())
                                .thenReturn(5L);

                when(repository.save(post))
                                .thenReturn(post);

                Post result = postService.vote(
                                "post-1",
                                VoteType.LIKE,
                                user);

                assertSame(post, result);

                verify(post)
                                .setUpVotes(6L);

                verify(voteRepository)
                                .save(any(Vote.class));

                verify(repository)
                                .save(post);
        }

        @Test
        void shouldRemoveLikeWhenUserVotesAgainWithSameType() {

                when(repository.findById("post-1"))
                                .thenReturn(Optional.of(post));

                when(post.getId())
                                .thenReturn("post-1");

                Vote vote = new Vote();
                vote.setType(VoteType.LIKE);

                when(voteRepository.findByUserIdAndPostId(
                                "user-id",
                                "post-1")).thenReturn(Optional.of(vote));

                when(post.getUpVotes())
                                .thenReturn(5L);

                when(repository.save(post))
                                .thenReturn(post);

                Post result = postService.vote(
                                "post-1",
                                VoteType.LIKE,
                                user);

                assertSame(post, result);

                verify(post)
                                .setUpVotes(4L);

                verify(voteRepository)
                                .delete(vote);

                verify(repository)
                                .save(post);
        }

        @Test
        void shouldRemoveDislikeWhenUserVotesAgainWithSameType() {

                when(repository.findById("post-1"))
                                .thenReturn(Optional.of(post));

                when(post.getId())
                                .thenReturn("post-1");

                Vote vote = new Vote();
                vote.setType(VoteType.DISLIKE);

                when(voteRepository.findByUserIdAndPostId(
                                "user-id",
                                "post-1")).thenReturn(Optional.of(vote));

                when(post.getDownVotes())
                                .thenReturn(3L);

                when(repository.save(post))
                                .thenReturn(post);

                Post result = postService.vote(
                                "post-1",
                                VoteType.DISLIKE,
                                user);

                assertSame(post, result);

                verify(post)
                                .setDownVotes(2L);

                verify(voteRepository)
                                .delete(vote);

                verify(repository)
                                .save(post);
        }

        @Test
        void shouldChangeLikeToDislike() {

                when(repository.findById("post-1"))
                                .thenReturn(Optional.of(post));
                when(post.getId())
                                .thenReturn("post-1");

                Vote vote = new Vote();
                vote.setType(VoteType.LIKE);

                when(voteRepository.findByUserIdAndPostId(
                                "user-id",
                                "post-1")).thenReturn(Optional.of(vote));

                when(post.getUpVotes())
                                .thenReturn(5L);

                when(post.getDownVotes())
                                .thenReturn(2L);

                when(repository.save(post))
                                .thenReturn(post);

                Post result = postService.vote(
                                "post-1",
                                VoteType.DISLIKE,
                                user);

                assertSame(post, result);

                verify(post)
                                .setUpVotes(4L);

                verify(post)
                                .setDownVotes(3L);

                assertEquals(
                                VoteType.DISLIKE,
                                vote.getType());

                verify(voteRepository)
                                .save(vote);

                verify(repository)
                                .save(post);
        }

        @Test
        void shouldChangeDislikeToLike() {

                when(repository.findById("post-1"))
                                .thenReturn(Optional.of(post));

                when(post.getId())
                                .thenReturn("post-1");

                Vote vote = new Vote();
                vote.setType(VoteType.DISLIKE);

                when(voteRepository.findByUserIdAndPostId(
                                "user-id",
                                "post-1")).thenReturn(Optional.of(vote));

                when(post.getUpVotes())
                                .thenReturn(5L);

                when(post.getDownVotes())
                                .thenReturn(2L);

                when(repository.save(post))
                                .thenReturn(post);

                Post result = postService.vote(
                                "post-1",
                                VoteType.LIKE,
                                user);

                assertSame(post, result);

                verify(post)
                                .setUpVotes(6L);

                verify(post)
                                .setDownVotes(1L);

                assertEquals(
                                VoteType.LIKE,
                                vote.getType());

                verify(voteRepository)
                                .save(vote);

                verify(repository)
                                .save(post);
        }

        @Test
        void shouldCreateDislikeWhenUserHasNotVoted() {

                when(repository.findById("post-1"))
                                .thenReturn(Optional.of(post));

                when(post.getId())
                                .thenReturn("post-1");

                when(voteRepository.findByUserIdAndPostId(
                                "user-id",
                                "post-1"))
                                .thenReturn(Optional.empty());

                when(post.getDownVotes())
                                .thenReturn(3L);

                when(repository.save(post))
                                .thenReturn(post);

                Post result = postService.vote(
                                "post-1",
                                VoteType.DISLIKE,
                                user);

                assertSame(post, result);

                verify(post)
                                .setDownVotes(4L);

                verify(voteRepository)
                                .save(any(Vote.class));

                verify(repository)
                                .save(post);
        }
}