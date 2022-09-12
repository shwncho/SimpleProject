package com.server.simple.service;

import com.server.simple.domain.Post;
import com.server.simple.repository.PostRepository;
import com.server.simple.request.PostCreate;
import com.server.simple.request.PostSearch;
import com.server.simple.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        Assertions.assertEquals(1L,postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 단건조회")
    void test2(){
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);
        Long postId = 1L;

        //when
        PostResponse post = postService.get(requestPost.getId());

        //then
        Assertions.assertNotNull(post);
        assertEquals("foo",post.getTitle());
        assertEquals("bar",post.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3(){
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                        .mapToObj(i -> Post.builder()
                                    .title("심플 제목 " + i)
                                    .content("반포자이 " + i)
                                    .build())
                        .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        //when
        List<PostResponse> posts= postService.getList(postSearch);

        //then
        assertEquals(10L,posts.size());
        assertEquals("심플 제목 30",posts.get(0).getTitle());
    }



}