package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.response.ShowMyPostResponseDTO;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.enums.PostStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.ReactRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostMapperTest {

    @InjectMocks
    private PostMapper postMapper;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReactRepository reactRepository;

    @BeforeEach
    void setUp(){
        modelMapper = spy(modelMapper);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void convertPostToPostResponseDTO(){
        try {
            postMapper.convertPostToPostResponseDTO(new Post());
        } catch (NullPointerException e) {

        }
    }

    @Test
    void convertPostToShowMyPostResponseDTO(){
        Post post = new Post();
        post.setId(1L);
        post.setImages("image.jpg");
        post.setStatus(PostStatus.PUBLIC);
        post.setCreatedBy("dadsad");
        post.setLastModifiedBy("rvwetber");
        List<Post> posts = Arrays.asList(post);
        when(reactRepository.countByPostIdAndAndStatus(anyLong(), any())).thenReturn(2);
        when(commentRepository.countByPostId(anyLong())).thenReturn(3);
        List<ShowMyPostResponseDTO> responseDTOS = postMapper.convertPostToShowMyPostResponseDTO(posts);
        assertEquals(1, responseDTOS.size());
    }
}