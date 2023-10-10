package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.PostService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Home", description = "The Home API. Nothing more!!!.")
public class HomeController {

    @Autowired
    private PostService postService;

//    @GetMapping("/test")
//    @PreAuthorize("hasRole('USER')")
//    @Hidden
//    public ResponseEntity<?> home(){
//        return ResponseEntity.ok(new Response()
//                .withMessage("Hello User"));
//    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response home(@RequestParam int offset, @RequestParam int limit) {
        return postService.showAllPosts(offset,limit);
    }

}
