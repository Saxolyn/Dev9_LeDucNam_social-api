package com.social.socialserviceapp.controller;

import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.FriendService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friend")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Friend", description = "The Friend API. Nothing more!!!.")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/send-request/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response sendARequest(@PathVariable Long userId){
        return friendService.sendARequest(userId);
    }

    @DeleteMapping("/{userId}/request")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response cancelARequest(@PathVariable Long userId){
        return friendService.cancelARequest(userId);
    }

    @PostMapping("/unfriend/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response unFriend(@PathVariable Long userId){
        return friendService.unFriend(userId);
    }

    @PostMapping("/accept-request/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response acceptARequest(@PathVariable Long userId){
        return friendService.acceptARequest(userId);
    }

    @GetMapping("/friend-requests")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response friendRequests(){
        return friendService.friendRequests();
    }

    @GetMapping("/sent-requests")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response sentRequests(){
        return friendService.sendRequests();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response searchFriend(@RequestParam String username){
        return friendService.searchFriend(username);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response showMyFriends(){
        return friendService.showMyFriends();
    }

}
