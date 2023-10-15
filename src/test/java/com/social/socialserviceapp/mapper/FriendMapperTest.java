package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.response.FriendRequestsResponseDTO;
import com.social.socialserviceapp.model.dto.response.SentRequestsResponseDTO;
import com.social.socialserviceapp.model.entities.Friend;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ConfigurationException;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendMapperTest {

    @InjectMocks
    private FriendMapper friendMapper;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void convertFriendLstToFriendRequestsDTOLst(){
        try {
            Friend friend = new Friend();
            friend.setCreatedBy("sampleUser");
            friend.setBaseUserId(1L);
            List<Friend> friends = Arrays.asList(friend);
            FriendRequestsResponseDTO expectedDto = new FriendRequestsResponseDTO();
            expectedDto.setUsername("sampleUser");
            expectedDto.setUserId(1L);
            List<FriendRequestsResponseDTO> result = friendMapper.convertFriendLstToFriendRequestsDTOLst(friends);
            assertEquals(1, result.size());
            assertEquals(expectedDto, result.get(0));
        } catch (ConfigurationException e) {

        }
    }

    @Test
    void convertFriendLstToSendRequestsDTOLst(){
        Friend friend = new Friend();
        friend.setCreatedBy("sampleUser");
        friend.setOtherUserId(2L);
        friend.setBaseUserId(1L);
        List<Friend> friends = Arrays.asList(friend);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
                .username("aduchat")
                .build()));
        List<SentRequestsResponseDTO> result = friendMapper.convertFriendLstToSendRequestsDTOLst(friends);
        assertEquals(1, result.size());
    }

    @Test
    void convertFriendLstToMyFriendsResponseDTOLst(){
        Friend friend = new Friend();
        friend.setCreatedBy("kyrios");
        friend.setLastModifiedBy("kyrios");
        friend.setBaseUserId(1L);
        friend.setOtherUserId(2L);
        List<Friend> friends = Arrays.asList(friend);
        friendMapper.convertFriendLstToMyFriendsResponseDTOLst(friends, 22L, "kyrios");
    }
}