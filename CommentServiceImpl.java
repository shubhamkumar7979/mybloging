package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.BlogAPIException;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class CommentServiceImpl implements CommentService {

    private ModelMapper mapper;

    private PostRepository postRepository;
    private CommentRepository commentRepository;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper mapper ) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository ;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );

        // set post to comment entity
        comment.setPost(post);

        // comment entity to DB
        Comment newComment = commentRepository.save(comment);

        CommentDto dto = mapToDto(newComment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId ) {
        postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id",postId)
        );

        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",commentId)
        );

        if (comment.getPost().getId() != post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");

        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {

        // retrieve post entity by id
       Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id", postId)

        );

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new ResourceNotFoundException("Comment", "id", commentId)
        );


        if(comment.getPost().getId() != post.getId()){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to this post");

        }


        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());

        Comment updateComment = commentRepository.save(comment);
        return mapToDto(updateComment);
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post =postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post", "id", postId)
        );
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Comment", "id", id)
        );

        if (comment.getPost().getId() != post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
        commentRepository.deleteById(id);
    }

    private CommentDto mapToDto(Comment newComment) {

        CommentDto commentDto = mapper.map(newComment, CommentDto.class);

//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(newComment.getId());
//        commentDto.setName(newComment.getName());
//        commentDto.setEmail(newComment.getEmail());
//        commentDto.setBody(newComment.getBody());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {

        Comment comment = mapper.map(commentDto, Comment.class);

//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setBody(commentDto.getBody());
//        comment.setEmail(commentDto.getEmail());
        return comment;
    }



}




