package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.response.MyInfoResponseDTO;
import com.social.socialserviceapp.model.dto.response.PostResponseDTO;
import com.social.socialserviceapp.model.dto.response.ShowMyPostResponseDTO;
import com.social.socialserviceapp.model.entities.Post;
import com.social.socialserviceapp.util.CommonUtil;
import com.social.socialserviceapp.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    @Autowired
    private ModelMapper modelMapper;

    public PostResponseDTO convertPostToPostResponseDTO(Post post) {
        return modelMapper.createTypeMap(post, PostResponseDTO.class).setPostConverter(converter -> {
            converter.getDestination().setImages(CommonUtil.toList(converter.getSource().getImages(), ","));
            return converter.getDestination();
        }).map(post);
    }

    public List<ShowMyPostResponseDTO> convertPostToShowMyPostResponseDTO(List<Post> posts) {
        List<ShowMyPostResponseDTO> responseDTOS = new ArrayList<>();
        for (Post post : posts) {
            ShowMyPostResponseDTO responseDTO = new ShowMyPostResponseDTO();
            responseDTO.setId(post.getId());
            responseDTO.setContent(post.getContent());
            responseDTO.setCreatedBy(post.getCreatedBy());
            responseDTO.setCreatedDate(String.valueOf(post.getCreatedDate()));
            responseDTO.setLastModifiedBy(post.getLastModifiedBy());
            responseDTO.setLastModifiedDate(String.valueOf(post.getLastModifiedDate()));
            responseDTO.setImages(CommonUtil.toList(post.getImages(), ","));
            responseDTO.setStatus(post.getStatus());
            responseDTOS.add(responseDTO);
        }
        return responseDTOS;
    }
}


