package com.social.socialserviceapp.repository;

import com.social.socialserviceapp.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public void deleteByPostId(Long postId);
    public List<Comment> getAllByPostId(Long postId);
}
