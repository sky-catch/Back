package com.example.api.review;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewImageMapper {
    void createReviewImage(long reviewId, List<String> fileNames);
}
