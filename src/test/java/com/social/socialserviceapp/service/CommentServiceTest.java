package com.social.socialserviceapp.service;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.CommentMapper;
import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.model.entities.Comment;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.impl.CommentServiceImpl;
import com.social.socialserviceapp.util.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentMapper commentMapper;

    @BeforeEach
    void setUp(){
        User user = new User();
        user.setUsername("aduchat");
        user.setPassword("aduchat");
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void createACommentForPosts(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder()
                .content("323123rewcrewr")
                .images("images.jpg")
                .status(PostStatus.PUBLIC)
                .build()));
        Response response = commentService.createACommentForPosts(99L, CommentRequestDTO.builder()
                .content("dsdsds")
                .build());
        assertEquals(Constants.RESPONSE_MESSAGE.COMMENT_SUCCESSFULLY, response.getMessage());
    }

    @Test
    void showComments(){
        List<Comment> comments = Arrays.asList(new Comment());
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder()
                .content("323123rewcrewr")
                .images("images.jpg")
                .status(PostStatus.PUBLIC)
                .build()));
        when(commentRepository.getAllByPostIdOrderByLastModifiedDate(anyLong())).thenReturn(comments);
        when(commentMapper.convertCommentsLstToCommentResponseDTOLst(anyList())).thenReturn(new ArrayList<>());
        commentService.showComments(99L);
    }

    @Test
    void showComments_whenNoCommentsOnPost(){
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder()
                .content("323123rewcrewr")
                .images("images.jpg")
                .status(PostStatus.PUBLIC)
                .build()));
        when(commentRepository.getAllByPostIdOrderByLastModifiedDate(anyLong())).thenReturn(new ArrayList<>());
        Response response = commentService.showComments(99L);
        assertEquals(200, response.getStatus());
    }

    @Test
    void deleteComment(){
        Comment comment = new Comment();
        comment.setCreatedBy("aduchat");
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        Response response = commentService.deleteComment(99L);
        assertEquals(200, response.getStatus());
        assertEquals("Deleted comment successfully.", response.getMessage());
    }

    @Test
    void deleteComment_ifNotMineComment(){
        Comment comment = new Comment();
        comment.setCreatedBy("vcl");
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        SocialAppException exception = assertThrows(SocialAppException.class, () -> commentService.deleteComment(99L));
        assertTrue(exception.getMessage()
                .contains("This is not ur comment, so u can't delete."));
    }

    @Test
    void updateComment(){
        Comment comment = new Comment();
        comment.setCreatedBy("aduchat");
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        Response response = commentService.updateComment(99L, CommentRequestDTO.builder()
                .content("aduchat")
                .build());
        assertEquals(200, response.getStatus());
        assertEquals("Updated comment successfully.", response.getMessage());
    }

    @Test
    void updateComment_ifNotMineComment(){
        Comment comment = new Comment();
        comment.setCreatedBy("vcl");
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        SocialAppException exception = assertThrows(SocialAppException.class, () -> commentService.updateComment(99L,
                CommentRequestDTO.builder()
                        .content("aduchat")
                        .build()));
        assertTrue(exception.getMessage()
                .contains("This is not ur comment, so u can't edit."));
    }
}