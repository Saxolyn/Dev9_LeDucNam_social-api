package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.FriendService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friend")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "5. Friend", description = "The Friend API. Nothing more!!!.")
@RequiredArgsConstructor
@ApiResponses(value = {@ApiResponse(responseCode = "200",
        content = {@Content(mediaType = "application/json",
                schema = @Schema(example = Constants.RESPONSE_SCHEMA.OK))}), @ApiResponse(responseCode = "400",
        content = {@Content(mediaType = "application/json",
                schema = @Schema(example = Constants.RESPONSE_SCHEMA.BAD_REQUEST))})})
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/send-request/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Send a friend request to another user.")
    public Response sendARequest(@PathVariable Long userId) {
        return friendService.sendARequest(userId);
    }

    @DeleteMapping("/{userId}/request")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Cancel send request or delete the friend request.")
    public Response cancelARequest(@PathVariable Long userId) {
        return friendService.cancelARequest(userId);
    }

    @PostMapping("/unfriend/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Unfriend who is already a friend to u.")
    public Response unFriend(@PathVariable Long userId) {
        return friendService.unFriend(userId);
    }

    @PostMapping("/accept-request/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Accept the friend request the user sent you.")
    public Response acceptARequest(@PathVariable Long userId) {
        return friendService.acceptARequest(userId);
    }

    @GetMapping("/friend-requests")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Retrieve all friend requests.")
    public Response friendRequests() {
        return friendService.friendRequests();
    }

    @GetMapping("/sent-requests")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Retrieve all send requests.")
    public Response sentRequests() {
        return friendService.sendRequests();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Search another user by username.")
    public Response searchFriend(@RequestParam String username) {
        return friendService.searchFriend(username);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Retrieve all friends are already friends.")
    public Response showMyFriends() {
        return friendService.showMyFriends();
    }

}
