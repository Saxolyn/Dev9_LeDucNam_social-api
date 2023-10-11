package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.React;
import com.social.socialserviceapp.model.enums.ReactStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactRepository extends JpaRepository<React, Long> {
    public React findByPostIdAndLastModifiedBy(Long postId, String username);

    public int countByPostIdAndAndStatus(Long postId, ReactStatus status);

    public React findByCommentIdAndLastModifiedBy(Long postId, String username);

    public int countByCommentIdAndStatus(Long commentId, ReactStatus status);

    @Query(value = "SELECT COUNT(*) " +
            "FROM reacts r " +
            "WHERE r.last_modified_date >= NOW() - INTERVAL 1 WEEK " +
            "AND r.last_modified_date < NOW() " +
            "AND r.created_by = ?1 AND r.status = ?2",nativeQuery = true)
    public int countReactsLastWeekByCreatedByAndStatus(String createdBy, int status);

}