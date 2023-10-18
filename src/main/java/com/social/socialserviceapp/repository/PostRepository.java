package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Post;
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

    Page<Post> findAllByCreatedByAndStatus(String createdBy, PostStatus status, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM " +
            "(SELECT * " +
            "FROM posts p " +
            "WHERE p.created_by = ?1 " +
            "UNION " +
            "SELECT p.id,p.content,p.images,p.status,p.created_by,p.created_date,p.last_modified_by,p.last_modified_date FROM friends f " +
            "LEFT JOIN posts p ON p.created_by = f.last_modified_by " +
            "WHERE f.created_by = ?1 and f.status = ?2 " +
            "UNION " +
            "SELECT p.id,p.content,p.images,p.status,p.created_by,p.created_date,p.last_modified_by,p.last_modified_date FROM friends f " +
            "LEFT JOIN posts p ON p.created_by = p.created_by " +
            "WHERE f.last_modified_by = ?1 and f.status = ?2 " +
            " ) results ORDER BY created_date DESC LIMIT ?3 OFFSET ?4", nativeQuery = true)
    List<Post> getAllFriendPostsByUsernameAndFriendStatus(String username, int status, int limit, int offset);

    @Query(value = "SELECT COUNT(*) " +
            "FROM posts p " +
            "WHERE p.created_date >= NOW() - INTERVAL 1 WEEK " +
            "AND p.created_date < NOW() " +
            "AND p.created_by = ?1", nativeQuery = true)
    public int countPostsLastWeekByCreatedBy(String createdBy);

}
