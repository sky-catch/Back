package com.example.api.review;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReviewImageMapper {
    void createReviewImage(long reviewId,List<String> list);

    List<String> getReviewImages(long reviewId);
    void deleteReviewImages(long reviewId);
}
