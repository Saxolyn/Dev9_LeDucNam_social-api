package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.CommentService;
import com.social.socialserviceapp.service.PostService;
import com.social.socialserviceapp.service.ReactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Mock
    private ReactService reactService;

    @Mock
    private CommentService commentService;

    @Test
    void createAPost(){
        when(postService.createOrEditAPost(any(), eq(null), anyString(), any())).thenReturn(new Response());
        postController.createAPost("dsadasd", new MultipartFile[]{});
    }

    @Test
    void showMyPosts(){
        when(postService.showMyPosts(anyInt(), anyInt())).thenReturn(new Response());
        postController.showMyPosts(0, 1);
    }

    @Test
    void updateAPost(){
        when(postService.createOrEditAPost(any(), anyLong(), anyString(), any())).thenReturn(new Response());
        postController.updateAPost(22L, "dsadcadc", new MultipartFile[]{});
    }

    @Test
    void deleteAPost(){
        when(postService.deleteAPost(anyLong())).thenReturn(new Response());
        postController.deleteAPost(22L);
    }

    @Test
    void likeOrUnlikeAPost(){
        when(reactService.likeOrUnlikeAPost(anyLong())).thenReturn(new Response());
        postController.likeOrUnlikeAPost(22L);
    }

    @Test
    void commentAPosts(){
        when(commentService.createACommentForPosts(anyLong(), any())).thenReturn(new Response());
        postController.commentAPosts(22L, new CommentRequestDTO());
    }

    @Test
    void showComments(){
        when(commentService.showComments(anyLong())).thenReturn(new Response());
        postController.showComments(22L);
    }

    @Test
    void showOtherPosts(){
        when(postService.showOtherPost(anyLong(), anyInt(), anyInt())).thenReturn(new Response());
        postController.showOtherPosts(22L, 0, 5);
    }
}