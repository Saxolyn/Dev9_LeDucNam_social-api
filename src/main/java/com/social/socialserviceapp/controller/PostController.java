package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.model.dto.request.ShowMyPostRequestDTO;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/post")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Post", description = "The Post API. Nothing more!!!.")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response createAPost(@RequestPart(value = "content", required = false) String content,
                                @RequestPart(value = "image", required = false) MultipartFile[] multipartFiles) throws Exception {
        return postService.createOrEditAPost(PostStatus.PUBLIC, null, content, multipartFiles);
    }

    @GetMapping(value = "/my-posts")
    public Response showMyPosts(ShowMyPostRequestDTO showMyPostRequestDTO) {
        return postService.showMyPosts(showMyPostRequestDTO);
    }

    @PutMapping(value = "/{status}/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response updateAPost(@PathVariable(required = true) PostStatus status,
                                @PathVariable(required = true) Long id,
                                @RequestPart(value = "content", required = false) String content,
                                @RequestPart(value = "image", required = false) MultipartFile[] multipartFiles) throws Exception {
        return postService.createOrEditAPost(status, id, content, multipartFiles);
    }
}
