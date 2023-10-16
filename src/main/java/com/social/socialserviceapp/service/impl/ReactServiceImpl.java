package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.model.entities.Comment;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.entities.React;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.model.enums.ReactStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.repository.ReactRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.ReactService;
import com.social.socialserviceapp.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReactServiceImpl implements ReactService {

    private final ReactRepository reactRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public Response likeOrUnlikeAPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.POST_NOT_FOUND));
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        React react = reactRepository.findByPostIdAndLastModifiedBy(postId, username);
        if (PostStatus.PUBLIC.equals(post.getStatus()) || username.equals(post.getCreatedBy())) {
            if (react == null) {
                react = new React();
                react.setPostId(post.getId());
                react.setStatus(ReactStatus.LIKE);
            } else {
                react.setStatus(react.getStatus()
                        .equals(ReactStatus.LIKE) ? ReactStatus.UNLIKE : ReactStatus.LIKE);
            }
        } else {
            throw new SocialAppException("This post is private.");
        }
        reactRepository.save(react);
        return Response.success(username + " has " + react.getStatus()
                .getReact() + " the post.");
    }

    @Override
    public Response likeOrUnlikeAComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found."));
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        React react = reactRepository.findByCommentIdAndLastModifiedBy(commentId, username);
//        if (username.equals(comment.getCreatedBy())) {
        if (react == null) {
            react = React.builder()
                    .commentId(comment.getId())
                    .status(ReactStatus.LIKE)
                    .build();
        } else {
            react.setStatus(react.getStatus()
                    .equals(ReactStatus.LIKE) ? ReactStatus.UNLIKE : ReactStatus.LIKE);
        }
//        } else {
//            throw new SocialAppException("This comment is private.");
//        }
        reactRepository.save(react);
        return Response.success(username + " has " + react.getStatus()
                .getReact() + " the comment.");
    }
}
