package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public void deleteByPostId(Long postId);

    public List<Comment> getAllByPostIdOrderByLastModifiedDate(Long postId);

    public int countByPostId(Long postId);

    @Query(value = "SELECT COUNT(*) " +
            "FROM comments c " +
            "WHERE c.created_date >= NOW() - INTERVAL 1 WEEK " +
            "AND c.created_date < NOW() " +
            "AND c.created_by = ?1",nativeQuery = true)
    public int countCommentsLastWeekByCreatedBy(String createdBy);
}
