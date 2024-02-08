package com.example.api.comment;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    boolean isPresentComment(long reviewId);
}