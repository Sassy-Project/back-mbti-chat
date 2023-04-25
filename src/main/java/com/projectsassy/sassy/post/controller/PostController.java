package com.projectsassy.sassy.post.controller;

import com.projectsassy.sassy.post.dto.CreatePostResponse;
import com.projectsassy.sassy.post.dto.HomeResponse;
import com.projectsassy.sassy.post.dto.CreatePostRequest;
import com.projectsassy.sassy.post.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @ApiOperation("게시판 메인 페이지 조회순")
    @GetMapping("/home")
    public ResponseEntity<HomeResponse> postHome(
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page
    ) {
        List<HomeResponse> homeResponse = postService.getPostHome(limit, page);


        return new ResponseEntity(homeResponse, HttpStatus.OK);
    }

    @ApiOperation("게시글 등록")
    @PostMapping("/new")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest createPostRequest) {
        Long postId = postService.createPost(createPostRequest);
        CreatePostResponse createPostResponse = new CreatePostResponse(postId);

        return new ResponseEntity(createPostResponse, HttpStatus.OK);
    }
}