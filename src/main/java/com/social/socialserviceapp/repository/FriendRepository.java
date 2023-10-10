package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Friend;
import com.social.socialserviceapp.model.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    public Friend findFriendByBaseUserIdAndOtherUserId(Long baseUserId, Long otherUserId);

    public List<Friend> getFriendsByOtherUserIdAndStatusOrderBySentOn(Long userId, FriendStatus status);

    public List<Friend> getFriendsByBaseUserIdAndStatus(Long userId, FriendStatus status);

    public Friend findFriendByOtherUserIdAndAndBaseUserId(Long otherUserId, Long baseUserId);


    @Query(value = "SELECT * FROM friends f " +
            "WHERE f.base_user_id = :userId AND f.status = :status " +
            "UNION ALL " +
            "SELECT * FROM friends f WHERE f.other_user_id = :userId AND f.status = :status",
            nativeQuery = true)
    public List<Friend> getFriendsByBaseUserIdAndStatusCustom(@Param("userId") Long userId,
                                                              @Param("status") int status);

    public Friend findFriendByBaseUserIdAndAndOtherUserIdAndStatus(Long baseUserId, Long otherUserId,
                                                                   FriendStatus status);

    @Query(value = "SELECT * FROM friends f " +
            "WHERE f.base_user_id = :baseUserId AND f.other_user_id = :otherUserId " +
            "UNION ALL " +
            "SELECT * FROM friends f WHERE f.base_user_id = :otherUserId AND f.other_user_id = :baseUserId",
            nativeQuery = true)
    public Friend findFriendByBaseUserIdAndOtherUserIdCustom(Long baseUserId, Long otherUserId);

}
