package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
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
import com.social.socialserviceapp.repository.NotificationRepository;
import com.social.socialserviceapp.repository.ProfileRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.FriendService;
import com.social.socialserviceapp.util.CommonUtil;
import com.social.socialserviceapp.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FriendMapper friendMapper;

    @Override
    public Response sendARequest(Long userId){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        Friend friend = friendRepository.findFriendByBaseUserIdAndAndOtherUserId(user.getId(), userId);
        if (userId.equals(user.getId())) {
            throw new SocialAppException("What is wrong with u? U can't send a friend request for urself.");
        } else {
            Friend friendCheck = friendRepository.findFriendByBaseUserIdAndAndOtherUserId(userId, user.getId());
            if (friendCheck != null) {
                if (FriendStatus.ACCEPTED.equals(friendCheck.getStatus())) {
                    throw new SocialAppException(Constants.RESPONSE_MESSAGE.ALREADY_FRIEND);
                } else {
                    throw new SocialAppException(
                            friendCheck.getCreatedBy() + " has sent a friend request, plz accept it.");
                }
            } else {
                if (friend == null) {
                    friend = Friend.builder()
                            .baseUserId(user.getId())
                            .otherUserId(userId)
                            .status(FriendStatus.PENDING)
                            .sentOn(LocalDateTime.now())
                            .build();
                } else {
                    if (FriendStatus.PENDING.equals(friend.getStatus())) {
                        throw new SocialAppException("U have already sent a friend request.");
                    } else {
                        throw new SocialAppException(Constants.RESPONSE_MESSAGE.ALREADY_FRIEND);
                    }
                }
            }
        }
        friendRepository.save(friend);
        return Response.success("U have successfully sent a friend request.");
    }

    @Override
    public Response friendRequests(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        List<Friend> friends = friendRepository.getFriendsByOtherUserIdAndStatusOrderBySentOn(user.getId(),
                FriendStatus.PENDING);
        if (!CommonUtil.isNullOrEmpty(friends)) {
            List<FriendRequestsResponseDTO> responseDTOS = friendMapper.convertFriendLstToFriendRequestsDTOLst(friends);
            return Response.success("Friend Requests.")
                    .withData(responseDTOS);
        }
        return Response.success("U don't have any friend requests");
    }

    @Override
    public Response searchFriend(String realName){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        List<ProfileCustom> profiles = profileRepository.findAllUsersByUsernameContain(realName, username);
        if (!CommonUtil.isNullOrEmpty(profiles)) {
            return Response.success("Found result.")
                    .withData(profiles);
        }
        return Response.success("Didn't find any results");
    }

    @Override
    public Response cancelARequest(Long userId){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        Friend friend = friendRepository.findFriendByBaseUserIdAndAndOtherUserId(user.getId(), userId);
        if (friend != null) {
            friendRepository.delete(friend);
            return Response.success("Canceled a friend request successfully.");
        } else {
            throw new SocialAppException("Canceling ur friend request failed.");
        }
    }

    @Override
    public Response sendRequests(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        List<Friend> friends = friendRepository.getFriendsByBaseUserIdAndStatus(user.getId(), FriendStatus.PENDING);
        if (!CommonUtil.isNullOrEmpty(friends)) {
            List<SentRequestsResponseDTO> responseDTOS = friendMapper.convertFriendLstToSendRequestsDTOLst(friends);
            return Response.success("Sent Requests.")
                    .withData(responseDTOS);
        }
        return Response.success("U don't have any sent requests");
    }

    @Override
    public Response acceptARequest(Long userId){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        Friend friend = friendRepository.findFriendByBaseUserIdAndAndOtherUserId(userId, user.getId());
        if (friend != null) {
            friend.setStatus(FriendStatus.ACCEPTED);
            friendRepository.save(friend);
            return Response.success("Accepted a friend request successfully.");
        } else {
            throw new SocialAppException("Accepting ur friend request failed.");
        }
    }

    @Override
    public Response showMyFriends(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        List<Friend> friends = friendRepository.getFriendsByBaseUserIdAndStatusCustom(user.getId(),
                FriendStatus.ACCEPTED.ordinal());
        if (!CommonUtil.isNullOrEmpty(friends)) {
            List<ShowMyFriendsResponseDTO> responseDTOS = friendMapper.convertFriendLstToMyFriendsResponseDTOLst(
                    friends, user.getId(), username);
            return Response.success("Show list friends.")
                    .withData(responseDTOS);
        }
        return Response.success("U have no friends.");
    }

    @Override
    public Response unFriend(Long userId){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        Friend friend = friendRepository.findFriendByBaseUserIdAndAndOtherUserId(user.getId(), userId);
        if (friend != null) {
            User findOtherUser = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
            if (FriendStatus.ACCEPTED.equals(friend.getStatus())) {
                friend.setStatus(FriendStatus.UN_FRIEND);
                friendRepository.save(friend);
                return Response.success(findOtherUser.getUsername() + " is no longer your friend.");
            } else {
                throw new SocialAppException("U can't unfriend, " + findOtherUser.getUsername() + " isn't ur friend.");
            }
        } else {
            throw new SocialAppException("U failed to unfriend.");
        }
    }
}
