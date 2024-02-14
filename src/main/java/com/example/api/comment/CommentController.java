package com.example.api.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public void createComment(@RequestBody CommentDTO commentDTO){
        commentService.createComment(commentDTO);
    }

    @PatchMapping("")
    public void updateComment(@RequestBody CommentDTO commentDTO){
        commentService.updateComment(commentDTO);
    }

    @DeleteMapping("{id}")
    public void deleteComment(@PathVariable(name = "id") long commentId){
        commentService.deleteComment(commentId);
    }

}