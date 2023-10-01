package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.result.Response;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Home", description = "The Home API. Nothing more!!!.")
public class HomeController {

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> home(){
        return ResponseEntity.ok(new Response()
                .withMessage("Hello User"));
    }

    @GetMapping("/test-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> home2(){
        return ResponseEntity.ok(new Response()
                .withMessage("Hello Admin"));
    }

}
