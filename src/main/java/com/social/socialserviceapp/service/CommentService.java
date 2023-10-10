package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.result.Response;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    public Response createACommentForPosts(Long postId, CommentRequestDTO requestDTO);
    public Response showComments(Long postId);
    public Response deleteComment(Long commentId);
    public Response updateComment(Long commentId, CommentRequestDTO requestDTO);



}
