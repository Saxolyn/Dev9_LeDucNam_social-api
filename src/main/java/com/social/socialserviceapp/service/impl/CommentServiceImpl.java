package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.CommentMapper;
import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.model.dto.response.CommentResponseDTO;
import com.social.socialserviceapp.model.entities.Comment;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.CommentService;
import com.social.socialserviceapp.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Response createACommentForPosts(Long postId, CommentRequestDTO requestDTO){
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException("Posts not found."));
            Comment comment = getComment(requestDTO, post);
            commentRepository.save(comment);
            return Response.success("Comment on a post successfully.");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private Comment getComment(CommentRequestDTO requestDTO, Post post){
        Comment comment = new Comment();
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        if (username.equals(post.getLastModifiedBy())) {
            comment.setContent(requestDTO.getContent());
        } else {
            if (post.getStatus()
                    .equals(PostStatus.PUBLIC)) {
                comment.setContent(requestDTO.getContent());
            } else {
                throw new SocialAppException("This posts is private.");
            }
        }
        comment.setPostId(post.getId());
        return comment;
    }

    @Override
    public Response showComments(Long postId){
        List<Comment> comments = commentRepository.getAllByPostId(postId);
        if (!CommonUtil.isNullOrEmpty(comments)) {
            List<CommentResponseDTO> responseDTOList = commentMapper.convertCommentsLstToCommentResponseDTOLst(
                    comments);
            return Response.success("Show comments.")
                    .withData(responseDTOList);
        } else {
            return Response.success("No comments.");
        }
    }

    @Override
    public Response deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found."));
        commentRepository.delete(comment);
        return Response.success("Deleted comment successfully.");
    }

    @Override
    public Response updateComment(Long commentId, CommentRequestDTO requestDTO){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found."));
        commentRepository.save(comment);
        return Response.success("Updated comment successfully.");
    }
}
