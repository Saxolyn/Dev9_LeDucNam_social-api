package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.enums.FriendStatus;
import com.social.socialserviceapp.model.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByCreatedBy(String createdBy, Pageable pageable);
    List<Post> findAllByCreatedByAndStatus(String createdBy, PostStatus status);

    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN User u ON u.username = p.createdBy " +
            "LEFT JOIN Friend f ON f.baseUserId = u.id " +
            "WHERE f.baseUserId = ?1 AND f.status = ?2")
    Page<Post> getAllFriendPostsByUserIdAndFriendStatus(Long baseUserId, FriendStatus status, Pageable pageable);

}
