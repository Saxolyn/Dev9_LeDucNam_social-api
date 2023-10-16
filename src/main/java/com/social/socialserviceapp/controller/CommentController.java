package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.CommentService;
import com.social.socialserviceapp.service.ReactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comment", description = "The Comment API. Nothing more!!!.")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ReactService reactService;

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Delete a comment by comment id.")
    public Response deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Update a comment by comment id.")
    public Response editComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO requestDTO){
        return commentService.updateComment(commentId, requestDTO);
    }

    @PostMapping("/{commentId}/react")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Like or unlike a comment by comment id.")
    public Response likeOrUnlikeComment(@PathVariable Long commentId){
        return reactService.likeOrUnlikeAComment(commentId);
    }
}
