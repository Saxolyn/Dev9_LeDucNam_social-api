package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.result.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface PostService {

    public Response createOrEditAPost(PostStatus status, Long id, String content, MultipartFile[] multipartFiles);

    public Response showMyPosts(int offset, int limit);

    public Response deleteAPost(Long postId);

    public Response showOtherPost(Long userId, int offset, int limit);

    public Response showAllPosts(int offset, int limit);

}
