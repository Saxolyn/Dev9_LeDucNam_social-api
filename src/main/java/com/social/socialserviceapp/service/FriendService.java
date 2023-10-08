package com.social.socialserviceapp.service;

import com.social.socialserviceapp.result.Response;
import org.springframework.stereotype.Service;

@Service
public interface FriendService {
    public Response sendARequest(Long userId);

    public Response friendRequests();

    public Response searchFriend(String realName);

    public Response cancelARequest(Long userId);

    public Response sendRequests();
}
