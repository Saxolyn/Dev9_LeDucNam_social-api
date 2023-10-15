package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.model.dto.response.FriendRequestsResponseDTO;
import com.social.socialserviceapp.model.dto.response.SentRequestsResponseDTO;
import com.social.socialserviceapp.model.dto.response.ShowMyFriendsResponseDTO;
import com.social.socialserviceapp.model.entities.Friend;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FriendMapper {

    @Autowired
    private UserRepository userRepository;

    public List<FriendRequestsResponseDTO> convertFriendLstToFriendRequestsDTOLst(List<Friend> friends){
        return friends.stream()
                .map(friend -> FriendRequestsResponseDTO.builder()
                        .userId(friend.getBaseUserId())
                        .username(friend.getCreatedBy())
                        .createdDate(friend.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }

    public List<SentRequestsResponseDTO> convertFriendLstToSendRequestsDTOLst(List<Friend> friends){
        return friends.stream()
                .map(friend -> SentRequestsResponseDTO.builder()
                        .userId(friend.getOtherUserId())
                        .username(userRepository.findById(friend.getOtherUserId())
                                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND))
                                .getUsername())
                        .sendOn(friend.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ShowMyFriendsResponseDTO> convertFriendLstToMyFriendsResponseDTOLst(List<Friend> friends, Long userId,
                                                                                    String username){
        return friends.stream()
                .map(friend -> {
                    Long modifiedUserId = friend.getBaseUserId()
                            .equals(userId) ? friend.getOtherUserId() : friend.getBaseUserId();
                    String modifiedUsername = friend.getCreatedBy()
                            .equals(username) ? friend.getLastModifiedBy() : friend.getCreatedBy();
                    return new ShowMyFriendsResponseDTO(modifiedUserId, modifiedUsername);
                })
                .collect(Collectors.toList());
    }

}
