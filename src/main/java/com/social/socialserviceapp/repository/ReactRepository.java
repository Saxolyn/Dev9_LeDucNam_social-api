package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.React;
import com.social.socialserviceapp.model.enums.ReactStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactRepository extends JpaRepository<React, Long> {
    public React findByPostIdAndLastModifiedBy(Long postId, String username);

    public int countByPostIdAndAndStatus(Long postId, ReactStatus status);
}
