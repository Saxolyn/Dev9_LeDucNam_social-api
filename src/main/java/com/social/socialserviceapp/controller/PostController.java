package com.social.socialserviceapp.controller;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.CommentService;
import com.social.socialserviceapp.service.PostService;
import com.social.socialserviceapp.service.ReactService;
import com.social.socialserviceapp.util.Constants;
import com.social.socialserviceapp.validation.annotation.MultipleFileSize;
import com.social.socialserviceapp.validation.annotation.MultipleFileType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/post")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "3. Post", description = "The Post API. Nothing more!!!.")
@Validated
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = {
                @Content(mediaType = "application/json", schema = @Schema(example = Constants.RESPONSE_SCHEMA.OK))}),
        @ApiResponse(responseCode = "400", content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(example = Constants.RESPONSE_SCHEMA.BAD_REQUEST))})})
@JsonPropertyOrder({
        "GET",
        "POST",
        "PUT",
        "DELETE"})
public class PostController {

    private final PostService postService;
    private final ReactService reactService;
    private final CommentService commentService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Create a new post.")
    public Response createAPost(@RequestPart(value = "content", required = false) String content,
                                @RequestPart(value = "image", required = false) @Valid @MultipleFileSize
                                @MultipleFileType MultipartFile[] multipartFiles){
        return postService.createOrEditAPost(PostStatus.PUBLIC, null, content, multipartFiles);
    }

    @GetMapping(value = "/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Retrieve all posts of logged user.")
    public Response showMyPosts(@RequestParam(defaultValue = "0") int offset,
                                @RequestParam(defaultValue = "5") int limit){
        return postService.showMyPosts(offset, limit);
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Update a post has been created by post id.")
    public Response updateAPost(@PathVariable(required = true) Long postId,
                                @RequestPart(value = "content", required = false) String content,
                                @RequestPart(value = "image", required = false) @Valid @MultipleFileSize
                                @MultipleFileType MultipartFile[] multipartFiles){
        return postService.createOrEditAPost(PostStatus.PUBLIC, postId, content, multipartFiles);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Delete a post has been created by post id.")
    public Response deleteAPost(@PathVariable Long postId){
        return postService.deleteAPost(postId);
    }

    @PostMapping("{postId}/react")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Like or unlike a post has been created by post id.")
    public Response likeOrUnlikeAPost(@PathVariable Long postId){
        return reactService.likeOrUnlikeAPost(postId);
    }


    @PostMapping("/{postId}/comment")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Create a new comment for the post by post id.")
    public Response commentAPosts(@PathVariable Long postId, @RequestBody CommentRequestDTO requestDTO){
        return commentService.createACommentForPosts(postId, requestDTO);
    }

    @GetMapping("/{postId}/comments")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Retrieve all comments by post id.")
    public Response showComments(@PathVariable Long postId){
        return commentService.showComments(postId);
    }


    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Retrieve all posts by user id.")
    public Response showOtherPosts(@PathVariable Long userId, @RequestParam(defaultValue = "0") int offset,
                                   @RequestParam(defaultValue = "5") int limit){
        return postService.showOtherPost(userId, offset, limit);
    }
}
