package com.example.api.comment;

import com.example.api.alarm.Alarm;
import com.example.api.alarm.AlarmMapper;
import com.example.api.comment.dto.CommentDTO;
import com.example.api.comment.dto.CreateCommentReq;
import com.example.api.comment.dto.UpdateCommentReq;
import com.example.api.review.ReviewMapper;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final ReviewMapper reviewMapper;
    private final AlarmMapper alarmMapper;

    @Transactional
    public void createComment(CreateCommentReq dto) {
        CommentDTO commentDTO = new CommentDTO(dto);

        if (commentMapper.isPresentComment(commentDTO.getReviewId())) {
            throw new SystemException("이미 답글을 달았습니다.");
        }
        commentMapper.createComment(commentDTO);

        alarmMapper.creatReviewAlarm(new Alarm(commentDTO.getReviewId()));
    }

    @Transactional
    public void updateComment(UpdateCommentReq dto) {
        checkExistComment(dto.getCommentId());
        CommentDTO commentDTO = new CommentDTO(dto);

        commentMapper.updateComment(commentDTO);
    }

    @Transactional
    public void deleteComment(long commentId) {
        checkExistComment(commentId);
        commentMapper.deleteComment(commentId);
    }

    public void checkExistComment(long commentId) {
        if (commentMapper.getComment(commentId) == null) {
            throw new SystemException("해당하는 답글이 없습니다.");
        }
    }
}