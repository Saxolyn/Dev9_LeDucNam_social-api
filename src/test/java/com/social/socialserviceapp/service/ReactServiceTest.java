package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.entities.Comment;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.entities.React;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.model.enums.ReactStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.repository.ReactRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.impl.ReactServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactServiceTest {

    @InjectMocks
    private ReactServiceImpl reactService;
    @Mock
    private ReactRepository reactRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp(){
        User user = User.builder()
                .username("kyrios")
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
    }

    @Test
    void likeOrUnlikeAPost(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder()
                .status(PostStatus.PUBLIC)
                .build()));
        when(reactRepository.findByPostIdAndLastModifiedBy(anyLong(), anyString())).thenReturn(React.builder()
                .status(ReactStatus.LIKE)
                .build());
        Response response = reactService.likeOrUnlikeAPost(99L);
        assertEquals(200, response.getStatus());
    }

    @Test
    void likeOrUnlikeAPost_ifItFirstTimeReactAPost(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder()
                .status(PostStatus.PUBLIC)
                .build()));
        when(reactRepository.findByPostIdAndLastModifiedBy(anyLong(), anyString())).thenReturn(null);
        Response response = reactService.likeOrUnlikeAPost(99L);
        assertEquals(200, response.getStatus());
    }

    @Test
    void likeOrUnlikeAComment(){
        Comment comment = new Comment();
        comment.setCreatedBy("kyrios");
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(reactRepository.findByCommentIdAndLastModifiedBy(anyLong(), anyString())).thenReturn(React.builder()
                .status(ReactStatus.LIKE)
                .build());
        Response response = reactService.likeOrUnlikeAComment(99L);
        assertEquals(200, response.getStatus());
    }

    @Test
    void likeOrUnlikeAComment_ifItFirstTimeReactAComment(){
        Comment comment = new Comment();
        comment.setCreatedBy("kyrios");
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(reactRepository.findByCommentIdAndLastModifiedBy(anyLong(), anyString())).thenReturn(null);
        Response response = reactService.likeOrUnlikeAComment(99L);
        assertEquals(200, response.getStatus());
    }
}