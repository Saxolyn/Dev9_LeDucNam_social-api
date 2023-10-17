package com.social.socialserviceapp.service;

import com.social.socialserviceapp.exception.NotFoundException;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.mapper.PostMapper;
import com.social.socialserviceapp.model.dto.response.PostResponseDTO;
import com.social.socialserviceapp.model.dto.response.ShowMyPostResponseDTO;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.entities.User;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.PostRepository;
import com.social.socialserviceapp.repository.UserRepository;
import com.social.socialserviceapp.result.Response;
import com.social.socialserviceapp.service.impl.PostServiceImpl;
import com.social.socialserviceapp.util.Constants;
import com.social.socialserviceapp.util.FileUploadUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostServiceImpl postService;
    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("aduchat");
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null);
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createOrEditAPost() {
        Long id = null;
        String content = "Sample content";
        MultipartFile[] multipartFiles = null;
        PostStatus status = PostStatus.PUBLIC;
        Post post = new Post();
        post.setContent(content);
        post.setStatus(status);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        Response response = postService.createOrEditAPost(status, id, content, multipartFiles);
        assertEquals("Updated post successfully.", response.getMessage());
    }

    @Test
    void createOrEditAPost_ifContentAndFileNull() {
        try {
            Long id = null;
            MultipartFile[] multipartFiles = null;
            PostStatus status = PostStatus.PUBLIC;
            Post post = new Post();
            post.setStatus(status);
            Response response = postService.createOrEditAPost(status, id, null, multipartFiles);
        } catch (SocialAppException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.CONTENT_AND_FILE_NULL, e.getMessage());

        }
    }

    @Test
    void createOrEditAPost_ifIdPostNotNullAndPostNotFound() {
        try {
            Long id = 22L;
            MultipartFile[] multipartFiles = null;
            String content = "Sample content";
            PostStatus status = PostStatus.PUBLIC;
            when(postRepository.findById(anyLong())).thenThrow(NotFoundException.class);
            Response response = postService.createOrEditAPost(status, id, content, multipartFiles);
        } catch (NotFoundException e) {
            assertThrows(NotFoundException.class, () -> postRepository.findById(anyLong()));
        }
    }

    @Test
    void createOrEditAPost_editOtherPostNotMine() {
        try {
            Long id = 22L;
            MultipartFile[] multipartFiles = null;
            String content = "Sample content";
            PostStatus status = PostStatus.PUBLIC;
            Post post = new Post();
            post.setContent(content);
            post.setStatus(status);
            when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
            Response response = postService.createOrEditAPost(status, id, content, multipartFiles);
        } catch (SocialAppException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.NOT_MINE_POST, e.getMessage());
        }
    }

    @Test
    void createOrEditAPost_ifFileNotNull() throws IOException {
        Long id = 22L;
        String content = "Sample content";
        MultipartFile[] multipartFiles = {
                new MockMultipartFile("image1", "image1.jpg", "image/jpeg", "image1 data".getBytes()),};
        try (MockedStatic<FileUploadUtil> utilities = Mockito.mockStatic(FileUploadUtil.class)) {
            utilities.when(() -> FileUploadUtil.handleImageUpload(any()))
                    .thenReturn("dssd");
        }
        PostStatus status = PostStatus.PUBLIC;
        Post post = new Post();
        post.setContent(content);
        post.setCreatedBy("aduchat");
        post.setStatus(status);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenReturn(post);
        Response response = postService.createOrEditAPost(status, id, content, multipartFiles);
        assertEquals("Updated post successfully.", response.getMessage());
    }

    @Test
    void showMyPosts() {
        int offset = 0;
        int limit = 5;
        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(postRepository.findAllByCreatedBy(anyString(), any())).thenReturn(new PageImpl<>(posts));
        List<ShowMyPostResponseDTO> showMyPostResponseDTOS = Arrays.asList(new ShowMyPostResponseDTO(),
                new ShowMyPostResponseDTO());
        when(postMapper.convertPostToShowMyPostResponseDTO(any())).thenReturn(showMyPostResponseDTOS);
        Response response = postService.showMyPosts(offset, limit);
        assertEquals("Show my posts.", response.getMessage());
        assertEquals(2, ((List<PostResponseDTO>) response.getData()).size());
    }

    @Test
    void showMyPosts_ifLimitEqual0() {
        try {
            int offset = 0;
            int limit = 0;
            Response response = postService.showMyPosts(offset, limit);
        } catch (IllegalArgumentException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.INVALID_PAGE_LIMIT, e.getMessage());
        }
    }

    @Test
    void showMyPosts_whenUserHasNoPost() {
        int offset = 0;
        int limit = 5;
        when(postRepository.findAllByCreatedBy(anyString(), any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        Response response = postService.showMyPosts(offset, limit);
        assertEquals(Constants.RESPONSE_MESSAGE.NO_POST, response.getMessage());
    }

    @Test
    void deleteAPost_ifPostNotFound() {
        try {
            when(postRepository.findById(anyLong())).thenThrow(NotFoundException.class);
            postService.deleteAPost(22L);
        } catch (NotFoundException e) {
            assertThrows(NotFoundException.class, () -> postRepository.findById(anyLong()));
        }
    }

    @Test
    void deleteAPost() {
        Post post = new Post();
        post.setCreatedBy("aduchat");
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        Response response = postService.deleteAPost(22L);
        assertEquals(Constants.RESPONSE_MESSAGE.DELETE_POST_SUCCESSFULLY, response.getMessage());
    }

    @Test
    void deleteAPost_ifNotMinePost() {
        Post post = new Post();
        post.setCreatedBy("vcl");
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        SocialAppException exception = assertThrows(SocialAppException.class, () -> postService.deleteAPost(22L));
        assertTrue(exception.getMessage()
                .contains("This is not ur post."));
    }

    @Test
    void showOtherPost() {
        int offset = 0;
        int limit = 5;
        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
                .username("aduchat")
                .build()));
        when(postRepository.findAllByCreatedByAndStatus(anyString(), any(), any())).thenReturn(new PageImpl<>(posts));
        List<ShowMyPostResponseDTO> showMyPostResponseDTOS = Arrays.asList(new ShowMyPostResponseDTO(),
                new ShowMyPostResponseDTO());
        when(postMapper.convertPostToShowMyPostResponseDTO(any())).thenReturn(showMyPostResponseDTOS);
        Response response = postService.showOtherPost(22L, offset, limit);
        assertEquals("Show" + "aduchat" + "posts.", response.getMessage());
        assertEquals(2, ((List<PostResponseDTO>) response.getData()).size());
    }

    @Test
    void showOtherPost_ifLimitEqual0() {
        try {
            int offset = 0;
            int limit = 0;
            Response response = postService.showOtherPost(22L, offset, limit);
        } catch (IllegalArgumentException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.INVALID_PAGE_LIMIT, e.getMessage());
        }
    }

    @Test
    void showOtherPost_whenUserHasNoPost() {
        int offset = 0;
        int limit = 5;
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder()
                .username("aduchat")
                .build()));
        when(postRepository.findAllByCreatedByAndStatus(anyString(), any(), any())).thenReturn(
                new PageImpl<>(new ArrayList<>()));
        Response response = postService.showOtherPost(22L, offset, limit);
        assertEquals(Constants.RESPONSE_MESSAGE.NO_POST, response.getMessage());
    }

    @Test
    void showAllPosts() {
        int offset = 0;
        int limit = 5;
        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(User.builder()
                .id(99L)
                .username("aduchat")
                .build()));
        when(postRepository.getAllFriendPostsByUserIdAndFriendStatus(anyLong(), any(), any())).thenReturn(
                new PageImpl<>(posts));
        List<ShowMyPostResponseDTO> showMyPostResponseDTOS = Arrays.asList(new ShowMyPostResponseDTO(),
                new ShowMyPostResponseDTO());
        when(postMapper.convertPostToShowMyPostResponseDTO(any())).thenReturn(showMyPostResponseDTOS);
        Response response = postService.showAllPosts(offset, limit);
        assertEquals("Show all friend posts.", response.getMessage());
        assertEquals(2, ((List<PostResponseDTO>) response.getData()).size());
    }

    @Test
    void showAllPosts_ifLimitEqual0() {
        try {

            int offset = 0;
            int limit = 0;
            Response response = postService.showAllPosts(offset, limit);
        } catch (IllegalArgumentException e) {
            assertEquals(Constants.RESPONSE_MESSAGE.INVALID_PAGE_LIMIT, e.getMessage());
        }

    }

    @Test
    void showAllPosts_whenUserHasNoPost() {
        int offset = 0;
        int limit = 5;
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(User.builder()
                .id(99L)
                .username("aduchat")
                .build()));
        when(postRepository.getAllFriendPostsByUserIdAndFriendStatus(anyLong(), any(), any())).thenReturn(
                new PageImpl<>(new ArrayList<>()));
        Response response = postService.showAllPosts(offset, limit);
        assertEquals(Constants.RESPONSE_MESSAGE.NO_POST, response.getMessage());
    }
}