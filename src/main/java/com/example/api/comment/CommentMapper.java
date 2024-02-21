package com.example.api.comment;

import com.example.api.comment.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    boolean isPresentComment(long reviewId);
    
    void createComment(CommentDTO commentDTO);

    //List<CommentDTO> getComment(long ownerId);

    int updateComment(CommentDTO commentDTO);

    int deleteComment(long commentId);

}