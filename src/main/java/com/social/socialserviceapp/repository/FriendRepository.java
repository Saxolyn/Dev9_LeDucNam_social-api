package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Friend;
import com.social.socialserviceapp.model.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    public Friend findFriendByBaseUserIdAndAndOtherUserId(Long baseUserId, Long otherUserId);

    public List<Friend> getFriendsByOtherUserIdAndStatusOrderBySentOn(Long userId, FriendStatus status);

    public List<Friend> getFriendsByBaseUserIdAndStatus(Long userId, FriendStatus status);
}
