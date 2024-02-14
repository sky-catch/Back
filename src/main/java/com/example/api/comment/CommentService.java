package com.example.api.comment;

import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    @Transactional
    public void createComment(CommentDTO commentDTO) {
        if (commentMapper.isPresentComment(commentDTO.getReviewId())) {
            throw new SystemException("이미 댓글을 달았습니다.");
        }
        commentMapper.createComment(commentDTO);
    }

    @Transactional
    public void updateComment(CommentDTO commentDTO) {
        commentMapper.updateComment(commentDTO);
    }

    @Transactional
    public void deleteComment(long commentId) {
        commentMapper.deleteComment(commentId);
    }
}