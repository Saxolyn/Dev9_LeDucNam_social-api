package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.FriendService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest {

    @InjectMocks
    private FriendController friendController;

    @Mock
    private FriendService friendService;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void sendARequest(){
        when(friendService.sendARequest(anyLong())).thenReturn(new Response());
        Response response = friendController.sendARequest(99L);
        assertThat(response).isNotNull();
    }

    @Test
    void cancelARequest(){
        when(friendService.cancelARequest(anyLong())).thenReturn(new Response());
        Response response = friendController.cancelARequest(99L);
        assertThat(response).isNotNull();
    }

    @Test
    void unFriend(){
        when(friendService.unFriend(anyLong())).thenReturn(new Response());
        Response response = friendController.unFriend(99L);
        assertThat(response).isNotNull();
    }

    @Test
    void acceptARequest(){
        when(friendService.acceptARequest(anyLong())).thenReturn(new Response());
        Response response = friendController.acceptARequest(99L);
        assertThat(response).isNotNull();
    }

    @Test
    void friendRequests(){
        when(friendService.friendRequests()).thenReturn(new Response());
        Response response = friendController.friendRequests();
        assertThat(response).isNotNull();
    }

    @Test
    void sentRequests(){
        when(friendService.sendRequests()).thenReturn(new Response());
        Response response = friendController.sentRequests();
        assertThat(response).isNotNull();
    }

    @Test
    void searchFriend(){
        when(friendService.searchFriend(anyString())).thenReturn(new Response());
        Response response = friendController.searchFriend("aduchat");
        assertThat(response).isNotNull();
    }

    @Test
    void showMyFriends(){
        when(friendService.showMyFriends()).thenReturn(new Response());
        Response response = friendController.showMyFriends();
        assertThat(response).isNotNull();
    }
}