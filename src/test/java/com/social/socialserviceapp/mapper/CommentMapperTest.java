package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.entities.Comment;
import com.social.socialserviceapp.repository.ReactRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper commentMapper;

    @Mock
    private ReactRepository reactRepository;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void convertCommentsLstToCommentResponseDTOLst(){
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setPostId(3L);
        comment.setContent("dasdsad");
        comment.setCreatedBy("dasds");
        comment.setLastModifiedBy("devrwevr");
        List<Comment> comments = Arrays.asList(comment);
        when(reactRepository.countByCommentIdAndStatus(anyLong(), any())).thenReturn(2);
        commentMapper.convertCommentsLstToCommentResponseDTOLst(comments);
    }
}