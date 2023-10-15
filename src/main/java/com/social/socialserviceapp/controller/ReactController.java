package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.service.ReactService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/react")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "React", description = "The React API. Nothing more!!!.")
public class ReactController {

    @Autowired
    private ReactService reactService;

//    @PostMapping("{postId}/post")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    @Hidden
//    public Response likeOrUnlikeAPost(@PathVariable Long postId){
//        return reactService.likeOrUnlikeAPost(postId);
//    }
}
