package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.CommentService;
import com.social.socialserviceapp.service.ReactService;
import com.social.socialserviceapp.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "4. Comment", description = "The Comment API. Nothing more!!!.")
@RequiredArgsConstructor
@ApiResponses(value = {@ApiResponse(responseCode = "200",
        content = {@Content(mediaType = "application/json",
                schema = @Schema(example = Constants.RESPONSE_SCHEMA.OK))}), @ApiResponse(responseCode = "400",
        content = {@Content(mediaType = "application/json",
                schema = @Schema(example = Constants.RESPONSE_SCHEMA.BAD_REQUEST))})})
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
