package com.fc.sns.controller;

import com.fc.sns.controller.request.PostCommentRequest;
import com.fc.sns.controller.request.PostCreateRequest;
import com.fc.sns.controller.request.PostModifyRequest;
import com.fc.sns.controller.response.CommentResponse;
import com.fc.sns.controller.response.PostResponse;
import com.fc.sns.controller.response.Response;
import com.fc.sns.model.Comment;
import com.fc.sns.model.Post;
import com.fc.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getBody(), authentication.getName());

        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);

        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);

        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));

    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication) {
        return Response.success(postService.myList(pageable, authentication.getName()).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> likes(@PathVariable Integer postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Long> likesCount(@PathVariable Integer postId) {
        return Response.success(postService.likesCount(postId));
    }

    @PostMapping("/{postId}/comments")
    public Response<Void> comments(@PathVariable Integer postId, @RequestBody PostCommentRequest postCommentRequest, Authentication authentication) {
        postService.comment(postId, postCommentRequest, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> comments(@PathVariable Integer postId, Pageable pageable, Authentication authentication) {
        return Response.success(postService.getComments(postId, pageable).map(CommentResponse::fromComment));
    }
}
