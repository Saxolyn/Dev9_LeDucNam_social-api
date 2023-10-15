package com.social.socialserviceapp.service.impl;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.PostMapper;
import com.social.socialserviceapp.model.dto.response.PostResponseDTO;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.FriendStatus;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.PostService;
import com.social.socialserviceapp.util.CommonUtil;
import com.social.socialserviceapp.util.Constants;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Response createOrEditAPost(PostStatus status, Long id, String content, MultipartFile[] multipartFiles){
        Post post = null;
        if (id == null) {
            post = new Post();
            if (CommonUtil.isNullOrEmpty(content) && multipartFiles == null) {
                throw new SocialAppException(Constants.RESPONSE_MESSAGE.CONTENT_AND_FILE_NULL);
            }
        } else {
            post = postRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.POST_NOT_FOUND));
            String username = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
            if (!username.equals(post.getCreatedBy())) {
                throw new SocialAppException(Constants.RESPONSE_MESSAGE.NOT_MINE_POST);
            }
        }
        if (!CommonUtil.isNullOrEmpty(content)) {
            post.setContent(content);
        }
        if (multipartFiles != null) {
            List<String> fileNames = new LinkedList<>();
            Arrays.stream(multipartFiles)
                    .forEach(mf -> {
                        try {
                            fileNames.add(handleImageUpload(mf));
                        } catch (IOException e) {
                            throw new SocialAppException("Error saving uploaded file");
                        }
                    });
            try {
                post.setImages(CommonUtil.toString(fileNames, ","));
            } catch (Exception e) {
                throw new SocialAppException("!!!");
            }
        }
        post.setStatus(status);
        postRepository.save(post);
        PostResponseDTO responseDTO = postMapper.convertPostToPostResponseDTO(post);
        return Response.success("Updated post successfully.")
                .withData(responseDTO);
    }

    @Override
    public Response showMyPosts(int offset, int limit){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        if (limit == 0) {
            throw new IllegalArgumentException(Constants.RESPONSE_MESSAGE.INVALID_PAGE_LIMIT);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdDate")
                .descending());
        List<Post> posts = postRepository.findAllByCreatedBy(username, pageable)
                .getContent();
        if (!CommonUtil.isNullOrEmpty(posts)) {
            return Response.success("Show my posts.")
                    .withData(postMapper.convertPostToShowMyPostResponseDTO(posts));
        } else {
            return Response.success(Constants.RESPONSE_MESSAGE.NO_POST);
        }
    }

    @Override
    public Response deleteAPost(Long postId){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.POST_NOT_FOUND));
        if (username.equals(post.getCreatedBy())) {
            postRepository.delete(post);
            commentRepository.deleteByPostId(postId);
            return Response.success(Constants.RESPONSE_MESSAGE.DELETE_POST_SUCCESSFULLY);
        } else {
            throw new SocialAppException(Constants.RESPONSE_MESSAGE.NOT_MINE_POST);
        }
    }

    @Override
    public Response showOtherPost(Long userId, int offset, int limit){
        if (limit == 0) {
            throw new IllegalArgumentException(Constants.RESPONSE_MESSAGE.INVALID_PAGE_LIMIT);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdDate")
                .descending());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        List<Post> posts = postRepository.findAllByCreatedByAndStatus(user.getUsername(), PostStatus.PUBLIC, pageable)
                .getContent();
        if (!CommonUtil.isNullOrEmpty(posts)) {
            return Response.success("Show" + user.getUsername() + "posts.")
                    .withData(postMapper.convertPostToShowMyPostResponseDTO(posts));
        } else {
            return Response.success(Constants.RESPONSE_MESSAGE.NO_POST);
        }
    }

    @Override
    public Response showAllPosts(int offset, int limit){
        if (limit == 0) {
            throw new IllegalArgumentException(Constants.RESPONSE_MESSAGE.INVALID_PAGE_LIMIT);
        }
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.RESPONSE_MESSAGE.USER_NOT_FOUND));
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdDate")
                .descending());
        List<Post> posts = postRepository.getAllFriendPostsByUserIdAndFriendStatus(user.getId(), FriendStatus.ACCEPTED,
                        pageable)
                .getContent();
        if (!CommonUtil.isNullOrEmpty(posts)) {
            return Response.success("Show all friend posts.")
                    .withData(postMapper.convertPostToShowMyPostResponseDTO(posts));
        } else {
            return Response.success(Constants.RESPONSE_MESSAGE.NO_POST);
        }
    }


}
