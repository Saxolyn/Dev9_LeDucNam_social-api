package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comment", description = "The Comment API. Nothing more!!!.")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}/post")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response commentAPosts(@PathVariable Long postId, @RequestBody CommentRequestDTO requestDTO) {
        return commentService.createACommentForPosts(postId, requestDTO);
    }

    @GetMapping("/{postId}/post")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response showComments(@PathVariable Long postId) {
        return commentService.showComments(postId);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response editComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO requestDTO) {
        return commentService.updateComment(commentId,requestDTO);
    }
}
