package com.myblog.controller;

import com.myblog.payload.CommentDto;
import com.myblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments
    @PreAuthorize("hasRole('ADMIN,USER')")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Object> createComment
            (
             @PathVariable ("postId") long postId,
             @Valid @RequestBody CommentDto commentDto,
            BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommentDto dto = commentService.createComment(postId,commentDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(
            @PathVariable(value = "postId") long postId){

        return commentService.getCommentsByPostId(postId);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @GetMapping("/posts/{postId}/comments/{id}")
    public  ResponseEntity<CommentDto> getCommentById(
            @PathVariable ("postId") long postId,
            @PathVariable("id") long commentId) {
        CommentDto Dto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(Dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @PreAuthorize("hasRole('ADMIN,USER')")
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("postId") long postId,
            @PathVariable("id") long id,
            @RequestBody CommentDto commentDto) {

        CommentDto dto = commentService.updateComment(postId, id, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @PreAuthorize("hasRole('ADMIN,USER')")
    @DeleteMapping("posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable("postId") long postId,
            @PathVariable("id") long id ) {
        commentService.deleteComment(postId,id);
        return new ResponseEntity<>( "Comment is delete", HttpStatus.OK);

    }
}
