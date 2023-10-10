package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.model.dto.response.FriendRequestsResponseDTO;
import com.social.socialserviceapp.model.dto.response.SentRequestsResponseDTO;
import com.social.socialserviceapp.model.dto.response.ShowMyFriendsResponseDTO;
import com.social.socialserviceapp.model.entities.Friend;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.util.Constants;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FriendMapper {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

    public List<FriendRequestsResponseDTO> convertFriendLstToFriendRequestsDTOLst(List<Friend> friends) {
        try {
            return friends.stream()
                    .map(friend -> {
                        if (modelMapper.getTypeMap(Friend.class, FriendRequestsResponseDTO.class) == null) {
                            modelMapper.createTypeMap(friend, FriendRequestsResponseDTO.class)
                                    .setPostConverter(converter -> {
                                        converter.getDestination()
                                                .setUsername(converter.getSource()
                                                        .getCreatedBy());
                                        converter.getDestination()
                                                .setUserId(converter.getSource()
                                                        .getBaseUserId());
                                        converter.getDestination()
                                                .setSendOn(converter.getSource()
                                                        .getSentOn());
                                        return converter.getDestination();
                                    });
                        }
                        return modelMapper.map(friend, FriendRequestsResponseDTO.class);
                    })
                    .collect(Collectors.toList());
        } catch (MappingException me) {
            throw new com.social.socialserviceapp.exception.MappingException(
                    Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR);
        }
    }

    public List<SentRequestsResponseDTO> convertFriendLstToSendRequestsDTOLst(List<Friend> friends) {
        try {
            return friends.stream()
                    .map(friend -> {
                        if (modelMapper.getTypeMap(Friend.class, SentRequestsResponseDTO.class) == null) {
                            modelMapper.createTypeMap(friend, SentRequestsResponseDTO.class)
                                    .setPostConverter(converter -> {
                                        User user = userRepository.findById(converter.getSource()
                                                        .getOtherUserId())
                                                .orElseThrow(() -> new NotFoundException("User not found."));
                                        converter.getDestination()
                                                .setUsername(user.getUsername());
                                        converter.getDestination()
                                                .setUserId(converter.getSource()
                                                        .getOtherUserId());
                                        converter.getDestination()
                                                .setSendOn(converter.getSource()
                                                        .getSentOn());
                                        return converter.getDestination();
                                    });
                        }
                        return modelMapper.map(friend, SentRequestsResponseDTO.class);
                    })
                    .collect(Collectors.toList());
        } catch (MappingException me) {
            throw new com.social.socialserviceapp.exception.MappingException(
                    Constants.RESPONSE_MESSAGE.MODEL_MAPPER_ERROR);
        }
    }

    public List<ShowMyFriendsResponseDTO> convertFriendLstToMyFriendsResponseDTOLst(List<Friend> friends, Long userId,
                                                                                    String username) {
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
