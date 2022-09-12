package com.server.simple.controller;

import com.server.simple.domain.Post;
import com.server.simple.request.PostCreate;
import com.server.simple.response.PostResponse;
import com.server.simple.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable long postId){
        // Request 클래스
        // Response 클래스

        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(){
        return postService.getList();
    }
}
