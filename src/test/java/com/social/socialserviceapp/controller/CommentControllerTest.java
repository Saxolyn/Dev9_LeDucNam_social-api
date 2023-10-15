package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.CommentService;
import com.social.socialserviceapp.service.ReactService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
    @InjectMocks
    private CommentController commentController;
    @Mock
    private CommentService commentService;
    @Mock
    private ReactService reactService;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void deleteComment(){
        when(commentService.deleteComment(anyLong())).thenReturn(new Response());
        Response response = commentController.deleteComment(99L);
        assertThat(response).isNotNull();
    }

    @Test
    void editComment(){
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("aduchat day");
        when(commentService.updateComment(anyLong(), any())).thenReturn(new Response());
        Response response = commentController.editComment(99L, requestDTO);
        assertThat(response).isNotNull();
    }

    @Test
    void likeOrUnlikeComment(){
        when(reactService.likeOrUnlikeAComment(anyLong())).thenReturn(new Response());
        Response response = commentController.likeOrUnlikeComment(99L);
        assertThat(response).isNotNull();
    }
}