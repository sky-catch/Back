package com.example.api.comment;

import com.example.api.comment.dto.CreateCommentReq;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Tag(name = "답글")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public void createComment(@RequestBody CreateCommentReq dto){
        commentService.createComment(dto);
    }

    @PatchMapping("")
    public void updateComment(@RequestBody CreateCommentReq dto){
        commentService.updateComment(dto);
    }

    @DeleteMapping("{id}")
    public void deleteComment(@PathVariable(name = "id") long commentId){
        commentService.deleteComment(commentId);
    }

}