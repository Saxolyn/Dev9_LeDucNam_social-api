package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.PostService;
import com.social.socialserviceapp.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "6. Home", description = "The Home API. Nothing more!!!.")
@RequiredArgsConstructor
@ApiResponses(value = {@ApiResponse(responseCode = "200",
        content = {@Content(mediaType = "application/json",
                schema = @Schema(example = Constants.RESPONSE_SCHEMA.OK))}), @ApiResponse(responseCode = "400",
        content = {@Content(mediaType = "application/json",
                schema = @Schema(example = Constants.RESPONSE_SCHEMA.BAD_REQUEST))})})
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Retrieve all the latest posts of ur friends.")
    public Response home(@RequestParam int offset, @RequestParam(defaultValue = "5") int limit) {
        return postService.showAllPosts(offset, limit);
    }

}
