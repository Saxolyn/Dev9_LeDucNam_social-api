package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.response.PostResponseDTO;
import com.social.socialserviceapp.model.dto.response.ShowMyPostResponseDTO;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.model.enums.ReactStatus;
import com.social.socialserviceapp.repository.CommentRepository;
import com.social.socialserviceapp.repository.ReactRepository;
import com.social.socialserviceapp.util.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostMapper {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReactRepository reactRepository;

    public PostResponseDTO convertPostToPostResponseDTO(Post post){
        if (modelMapper.getTypeMap(Post.class, PostResponseDTO.class) == null) {
            modelMapper.createTypeMap(post, PostResponseDTO.class)
                    .setPostConverter(converter -> {
                        converter.getDestination()
                                .setImages(CommonUtil.toList(converter.getSource()
                                        .getImages(), ","));
                        return converter.getDestination();
                    });
        }
        return modelMapper.map(post, PostResponseDTO.class);
    }

    public List<ShowMyPostResponseDTO> convertPostToShowMyPostResponseDTO(List<Post> posts){
        List<ShowMyPostResponseDTO> responseDTOS = new ArrayList<>();
        posts.stream()
                .forEach(post -> responseDTOS.add(ShowMyPostResponseDTO.builder()
                        .id(post.getId())
                        .content(post.getContent())
                        .images(CommonUtil.toList(post.getImages(), ","))
                        .likes(reactRepository.countByPostIdAndAndStatus(post.getId(), ReactStatus.LIKE))
                        .comments(commentRepository.countByPostId(post.getId()))
                        .createdBy(post.getCreatedBy())
                        .createdDate(String.valueOf(post.getCreatedDate()))
                        .lastModifiedBy(post.getLastModifiedBy())
                        .lastModifiedDate(String.valueOf(post.getLastModifiedDate()))
                        .build()));
        return responseDTOS;
    }
}


