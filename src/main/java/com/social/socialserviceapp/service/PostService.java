package com.social.socialserviceapp.service;

import com.social.socialserviceapp.model.dto.request.ShowMyPostRequestDTO;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.result.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface PostService {

    public Response createOrEditAPost(PostStatus status, Long id, String content,
                                      MultipartFile[] multipartFiles);
    public Response showMyPosts(ShowMyPostRequestDTO showMyPostRequestDTO);
    public Response deleteAPost(Long postId);

}
