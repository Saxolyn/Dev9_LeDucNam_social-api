package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.CommentRequestDTO;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.CommentService;
import com.social.socialserviceapp.service.PostService;
import com.social.socialserviceapp.service.ReactService;
import com.social.socialserviceapp.validation.annotation.MultipleFileSize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/post")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Post", description = "The Post API. Nothing more!!!.")
@Validated
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ReactService reactService;

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response createAPost(@RequestPart(value = "content", required = false) String content,
                                @RequestPart(value = "image", required = false) MultipartFile[] multipartFiles){
        return postService.createOrEditAPost(PostStatus.PUBLIC, null, content, multipartFiles);
    }

    @GetMapping(value = "/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response showMyPosts(@RequestParam(defaultValue = "0") int offset,
                                @RequestParam(defaultValue = "5") int limit){
        return postService.showMyPosts(offset, limit);
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response updateAPost(@PathVariable(required = true) Long postId,
                                @RequestPart(value = "content", required = false) String content,
                                @RequestPart(value = "image", required = false) @Valid @MultipleFileSize
                                MultipartFile[] multipartFiles){
        return postService.createOrEditAPost(PostStatus.PUBLIC, postId, content, multipartFiles);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response deleteAPost(@PathVariable Long postId){
        return postService.deleteAPost(postId);
    }

    @PostMapping("{postId}/react")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response likeOrUnlikeAPost(@PathVariable Long postId){
        return reactService.likeOrUnlikeAPost(postId);
    }


    @PostMapping("/{postId}/comment")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response commentAPosts(@PathVariable Long postId, @RequestBody CommentRequestDTO requestDTO){
        return commentService.createACommentForPosts(postId, requestDTO);
    }

    @GetMapping("/{postId}/comment")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response showComments(@PathVariable Long postId){
        return commentService.showComments(postId);
    }


    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response showOtherPosts(@PathVariable Long userId, @RequestParam(defaultValue = "0") int offset,
                                   @RequestParam(defaultValue = "5") int limit){
        return postService.showOtherPost(userId, offset, limit);
    }
}
