package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Home", description = "The Home API. Nothing more!!!.")
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response home(@RequestParam int offset, @RequestParam int limit) {
        return postService.showAllPosts(offset,limit);
    }

}
