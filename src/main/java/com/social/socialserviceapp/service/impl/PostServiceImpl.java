package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.PostMapper;
import com.social.socialserviceapp.model.dto.request.ShowMyPostRequestDTO;
import com.social.socialserviceapp.model.dto.response.PostResponseDTO;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.PostService;
import com.social.socialserviceapp.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.social.socialserviceapp.util.FileUploadUtil.handleImageUpload;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostRepository postRepository;

    @Override
    public Response createOrEditAPost(PostStatus status, Long id, String content,
                                      MultipartFile[] multipartFiles) throws Exception {
        Post post = null;
        if (id == null) {
            post = new Post();
            if (CommonUtil.isNullOrEmpty(content) && multipartFiles == null) {
                throw new SocialAppException("Plz have at least 1 post or 1 image.");
            }
            post.setContent(content);
        } else {
            post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found."));
            if (!CommonUtil.isNullOrEmpty(content)) {
                post.setContent(content);
            }
        }
        if (multipartFiles != null) {
            List<String> fileNames = new LinkedList<>();
            Arrays.stream(multipartFiles).forEach(mf -> {
                try {
                    fileNames.add(handleImageUpload(mf));
                } catch (IOException e) {
                    throw new SocialAppException("Error saving uploaded file");
                }
            });
            post.setImages(CommonUtil.toString(fileNames, ","));
        }
        post.setStatus(status);
        postRepository.save(post);
        PostResponseDTO responseDTO = postMapper.convertPostToPostResponseDTO(post);
        return Response.success("Created post successfully.").withData(responseDTO);
    }

    @Override
    public Response showMyPosts(ShowMyPostRequestDTO showMyPostRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (showMyPostRequestDTO.getLimit() == 0) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
        Pageable pageable = PageRequest.of(showMyPostRequestDTO.getOffSet(), showMyPostRequestDTO.getLimit(), Sort.by("lastModifiedDate")
                .descending());
        List<Post> posts = postRepository.findAllByCreatedBy(username, pageable).getContent();
        if (!CommonUtil.isNullOrEmpty(posts)) {
            return Response.success().withData(postMapper.convertPostToShowMyPostResponseDTO(posts));
        } else {
            return Response.success("No posts.");
        }
    }

}
