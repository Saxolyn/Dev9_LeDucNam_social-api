package com.social.socialserviceapp.service;

import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.FriendMapper;
import com.social.socialserviceapp.model.custom.ProfileCustom;
import com.social.socialserviceapp.model.dto.response.FriendRequestsResponseDTO;
import com.social.socialserviceapp.model.dto.response.SentRequestsResponseDTO;
import com.social.socialserviceapp.model.dto.response.ShowMyFriendsResponseDTO;
import com.social.socialserviceapp.model.entities.Friend;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.FriendStatus;
import com.social.socialserviceapp.repository.FriendRepository;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.impl.FriendServiceImpl;
import com.social.socialserviceapp.util.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

    @InjectMocks
    private FriendServiceImpl friendService;

    @Spy
    private FriendRepository friendRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private FriendMapper friendMapper;

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
    void sendARequest(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Friend friend = Friend.builder()
                .baseUserId(1L)
                .otherUserId(2L)
                .status(FriendStatus.PENDING)
                .build();
        when(userRepository.existsUserById(anyLong())).thenReturn(true);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(friend);
        when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(null);
        Response response = friendService.sendARequest(2L);
        assertEquals("U have successfully sent a friend request.", response.getMessage());
    }

    @Test
    void sendARequestForYourSelf(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            Friend friend = Friend.builder()
                    .baseUserId(1L)
                    .otherUserId(2L)
                    .status(FriendStatus.PENDING)
                    .build();
            when(userRepository.existsUserById(anyLong())).thenReturn(true);
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(friend);
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(null);
            Response response = friendService.sendARequest(1L);
        } catch (SocialAppException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.SEND_REQUEST_TO_YOURSELF, e.getMessage());
        }
    }

    @Test
    void sendARequest_ifAlreadySendRequest(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            Friend friend = Friend.builder()
                    .baseUserId(1L)
                    .otherUserId(2L)
                    .status(FriendStatus.PENDING)
                    .build();
            when(userRepository.existsUserById(anyLong())).thenReturn(true);
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(friend);
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(friend);
            Response response = friendService.sendARequest(2L);
            assertEquals("U have successfully sent a friend request.", response.getMessage());
        } catch (SocialAppException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.ALREADY_SEND_REQUEST, e.getMessage());
        }
    }

    @Test
    void sendARequest_ifAlreadyFriend(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            Friend friend = Friend.builder()
                    .baseUserId(1L)
                    .otherUserId(2L)
                    .status(FriendStatus.ACCEPTED)
                    .build();
            when(userRepository.existsUserById(anyLong())).thenReturn(true);
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(friend);
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(friend);
            Response response = friendService.sendARequest(2L);
        } catch (SocialAppException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.ALREADY_FRIEND_SEND_REQUEST, e.getMessage());
        }
    }

    @Test
    void sendARequest_ifSendARequestToFriendButAlreadyHaveFriendRequest(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            Friend friend = Friend.builder()
                    .baseUserId(1L)
                    .otherUserId(2L)
                    .status(FriendStatus.PENDING)
                    .build();
            friend.setCreatedBy("testUser");
            when(userRepository.existsUserById(anyLong())).thenReturn(true);
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(any(), any())).thenReturn(null, friend);
            Response response = friendService.sendARequest(2L);
        } catch (SocialAppException e) {
            assertEquals("testUser has sent a friend request, plz accept it.", e.getMessage());
        }
    }

    @Test
    void friendRequests(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Friend friend = Friend.builder()
                .baseUserId(1L)
                .otherUserId(2L)
                .status(FriendStatus.PENDING)
                .build();
        friend.setCreatedBy("testUser");
        List<Friend> friends = Arrays.asList(friend);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.getFriendsByOtherUserIdAndStatusOrderByCreatedDate(anyLong(), any())).thenReturn(friends);
        List<FriendRequestsResponseDTO> responseDTOS = Arrays.asList(new FriendRequestsResponseDTO());
        when(friendMapper.convertFriendLstToFriendRequestsDTOLst(anyList())).thenReturn(responseDTOS);
        friendService.friendRequests();
    }

    @Test
    void friendRequests_ifFriendRequestsIsEmpty(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.getFriendsByOtherUserIdAndStatusOrderByCreatedDate(anyLong(), any())).thenReturn(
                new ArrayList<>());
        friendService.friendRequests();
    }

    @Test
    void searchFriend(){
        when(profileRepository.findAllUsersByUsernameContain(anyString(), anyString())).thenReturn(
                Arrays.asList(new ProfileCustom()));
        friendService.searchFriend("aduchat");
    }

    @Test
    void searchFriend_whenNoResult(){
        when(profileRepository.findAllUsersByUsernameContain(anyString(), anyString())).thenReturn(new ArrayList<>());
        friendService.searchFriend("aduchat");
    }

    @Test
    void cancelARequest(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Friend friend = Friend.builder()
                .baseUserId(1L)
                .otherUserId(2L)
                .status(FriendStatus.PENDING)
                .build();
        friend.setCreatedBy("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.findFriendByBaseUserIdAndAndOtherUserIdAndStatus(anyLong(), anyLong(), any())).thenReturn(
                friend, null);
        Response response = friendService.cancelARequest(2L);
        assertEquals(200, response.getStatus());
    }

    @Test
    void cancelARequest_ifFriendNull(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Friend friend = Friend.builder()
                .baseUserId(1L)
                .otherUserId(2L)
                .status(FriendStatus.PENDING)
                .build();
        friend.setCreatedBy("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.findFriendByBaseUserIdAndAndOtherUserIdAndStatus(anyLong(), anyLong(), any())).thenReturn(
                null, friend);
        Response response = friendService.cancelARequest(2L);
        assertEquals(200, response.getStatus());
    }

    @Test
    void cancelARequest_ifFriendAndFriendCheckNull(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndAndOtherUserIdAndStatus(anyLong(), anyLong(),
                    any())).thenReturn(null, null);
            Response response = friendService.cancelARequest(2L);
        } catch (SocialAppException e) {
            assertEquals("Failed to execute a request.", e.getMessage());
        }

    }

    @Test
    void sendRequests(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Friend friend = Friend.builder()
                .baseUserId(1L)
                .otherUserId(2L)
                .status(FriendStatus.PENDING)
                .build();
        friend.setCreatedBy("testUser");
        List<Friend> friends = Arrays.asList(friend);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.getFriendsByBaseUserIdAndStatus(anyLong(), any())).thenReturn(friends);
        List<SentRequestsResponseDTO> responseDTOS = Arrays.asList(new SentRequestsResponseDTO());
        when(friendMapper.convertFriendLstToSendRequestsDTOLst(anyList())).thenReturn(responseDTOS);
        friendService.sendRequests();
    }

    @Test
    void sendRequests_ifSendRequestsIsEmpty(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.getFriendsByBaseUserIdAndStatus(anyLong(), any())).thenReturn(new ArrayList<>());
        friendService.sendRequests();
    }

    @Test
    void acceptARequest(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Friend friend = Friend.builder()
                .baseUserId(1L)
                .otherUserId(2L)
                .status(FriendStatus.PENDING)
                .build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(friend);
        Response response = friendService.acceptARequest(1L);
        assertEquals(200, response.getStatus());
    }

    @Test
    void acceptARequest_ifAlreadyFriend(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            Friend friend = Friend.builder()
                    .baseUserId(1L)
                    .otherUserId(2L)
                    .status(FriendStatus.ACCEPTED)
                    .build();
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(friend);
            friendService.acceptARequest(1L);
        } catch (SocialAppException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.ALREADY_FRIEND_ACCEPT_REQUEST, e.getMessage());
        }
    }

    @Test
    void acceptARequest_whenNoFriendRequestToAccept(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndOtherUserId(anyLong(), anyLong())).thenReturn(null);
            friendService.acceptARequest(1L);
        } catch (SocialAppException e) {
            assertEquals("Accepting ur friend request failed.", e.getMessage());
        }
    }

    @Test
    void showMyFriends(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Friend friend = Friend.builder()
                .baseUserId(1L)
                .otherUserId(2L)
                .status(FriendStatus.ACCEPTED)
                .build();
        List<Friend> friends = Arrays.asList(friend);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.getFriendsByBaseUserIdAndStatusCustom(anyLong(), anyInt())).thenReturn(friends);
        List<ShowMyFriendsResponseDTO> responseDTOS = Arrays.asList(new ShowMyFriendsResponseDTO());
        when(friendMapper.convertFriendLstToMyFriendsResponseDTOLst(any(), anyLong(), anyString())).thenReturn(
                responseDTOS);
        friendService.showMyFriends();
    }

    @Test
    void showMyFriends_whenNoFriend(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.getFriendsByBaseUserIdAndStatusCustom(anyLong(), anyInt())).thenReturn(null);
        friendService.showMyFriends();
    }

    @Test
    void unFriend(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        Friend friend = Friend.builder()
                .baseUserId(1L)
                .otherUserId(2L)
                .status(FriendStatus.ACCEPTED)
                .build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(friendRepository.findFriendByBaseUserIdAndOtherUserIdCustom(anyLong(), anyLong())).thenReturn(friend);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
                .username("kyrios")
                .build()));
        friendService.unFriend(1L);
    }

    @Test
    void unFriend_ifTheyNotYourFriend(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            Friend friend = Friend.builder()
                    .baseUserId(1L)
                    .otherUserId(2L)
                    .status(FriendStatus.PENDING)
                    .build();
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndOtherUserIdCustom(anyLong(), anyLong())).thenReturn(friend);
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
                    .username("kyrios")
                    .build()));
            friendService.unFriend(1L);
        } catch (SocialAppException e) {
            assertEquals("U can't unfriend, kyrios isn't ur friend.", e.getMessage());
        }
    }

    @Test
    void unFriend_ifFriendIsNull(){
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(friendRepository.findFriendByBaseUserIdAndOtherUserIdCustom(anyLong(), anyLong())).thenReturn(null);
            friendService.unFriend(1L);
        } catch (SocialAppException e) {
            assertEquals("U failed to unfriend.", e.getMessage());
        }
    }
}