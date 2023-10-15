package com.social.socialserviceapp.mapper;

import com.social.socialserviceapp.model.dto.response.CommentResponseDTO;
import com.social.socialserviceapp.model.entities.Comment;
import com.social.socialserviceapp.model.enums.ReactStatus;
import com.social.socialserviceapp.repository.ReactRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReactRepository reactRepository;

    public List<CommentResponseDTO> convertCommentsLstToCommentResponseDTOLst(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponseDTO.builder()
                        .id(comment.getId())
                        .postId(comment.getPostId())
                        .content(comment.getContent())
                        .createdBy(comment.getCreatedBy())
                        .createdDate(comment.getCreatedDate())
                        .lastModifiedBy(comment.getLastModifiedBy())
                        .lastModifiedDate(comment.getLastModifiedDate())
                        .likes(reactRepository.countByCommentIdAndStatus(comment.getId(), ReactStatus.LIKE))
                        .build())
                .collect(Collectors.toList());
    }
}
