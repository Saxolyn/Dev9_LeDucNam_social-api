package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.FriendRepository;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.repository.ReactRepository;
import com.social.socialserviceapp.service.impl.ReportServiceImpl;
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

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReactRepository reactRepository;

    @BeforeEach
    void setUp(){
        User user = new User();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void exportExcel() throws IOException{
        when(postRepository.countPostsLastWeekByCreatedBy(anyString())).thenReturn(2);
        when(friendRepository.countFriendsLastWeekByCreatedBy(anyString())).thenReturn(2);
        when(reactRepository.countReactsLastWeekByCreatedByAndStatus(anyString(), anyInt())).thenReturn(2);
        when(commentRepository.countCommentsLastWeekByCreatedBy(anyString())).thenReturn(2);
        reportService.exportExcel();
    }
}