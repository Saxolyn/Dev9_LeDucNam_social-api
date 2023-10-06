package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.response.CommentResponseDTO;
import com.social.socialserviceapp.model.entities.Comment;
import com.social.socialserviceapp.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public List<CommentResponseDTO> convertCommentsLstToCommentResponseDTOLst(List<Comment> comments) {
        return ObjectMapperUtils.mapAll(comments, CommentResponseDTO.class);
    }
}
