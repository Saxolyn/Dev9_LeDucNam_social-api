package com.social.socialserviceapp.service;

import com.social.socialserviceapp.result.Response;
import org.springframework.stereotype.Service;

@Service
public interface ReactService {

    public Response likeOrUnlikeAPost(Long postId);

    public Response likeOrUnlikeAComment(Long commentId);

}
