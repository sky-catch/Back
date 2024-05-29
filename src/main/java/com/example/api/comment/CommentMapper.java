package com.example.api.comment;

import com.example.api.comment.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    boolean isPresentComment(long reviewId);
    
    void createComment(CommentDTO commentDTO);

    CommentDTO getComment(long ownerId);

    void updateComment(CommentDTO commentDTO);

    void deleteComment(long commentId);

    List<CommentDTO> findCommentByMember(long memberId);
}