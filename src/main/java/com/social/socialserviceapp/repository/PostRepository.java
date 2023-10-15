package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.enums.FriendStatus;
import com.social.socialserviceapp.model.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByCreatedBy(String createdBy, Pageable pageable);

    Page<Post> findAllByCreatedByAndStatus(String createdBy, PostStatus status, Pageable pageable);

    @Query(value = "SELECT DISTINCT " +
            "p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.username = p.createdBy " +
            "LEFT JOIN Friend f ON f.baseUserId = u.id " +
            "LEFT JOIN React r ON r.postId = p.id " +
            "LEFT JOIN Comment c ON c.postId = p.id " +
            "WHERE f.baseUserId = ?1 AND f.status = ?2")
    Page<Post> getAllFriendPostsByUserIdAndFriendStatus(Long baseUserId, FriendStatus status, Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM posts p " +
            "WHERE p.created_date >= NOW() - INTERVAL 1 WEEK " +
            "AND p.created_date < NOW() " +
            "AND p.created_by = ?1", nativeQuery = true)
    public int countPostsLastWeekByCreatedBy(String createdBy);

}
