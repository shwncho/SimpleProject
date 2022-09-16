package com.server.simple.service;

import com.server.simple.domain.Post;
import com.server.simple.exception.PostNotFound;
import com.server.simple.repository.PostRepository;
import com.server.simple.request.PostCreate;
import com.server.simple.request.PostEdit;
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
        assertEquals(1L,postRepository.count());

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
        assertNotNull(post);
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

    @Test
    @DisplayName("글 제목 조회")
    void test4(){
        //given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다 id=" + post.getId()));
        assertEquals("호돌걸", changedPost.getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test5(){
        //given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다 id=" + post.getId()));
        assertEquals("호돌걸", changedPost.getTitle());
        assertEquals("반포자이", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test6(){
        //given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("초가집")
                .build();
        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다 id=" + post.getId()));
        assertEquals("초가집", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test7(){
        //given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertEquals(0,postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test8(){
        //given
        Post post = Post.builder()
                .title("심플")
                .content("반포자이")
                .build();
        postRepository.save(post);

        //expected
        assertThrows(PostNotFound.class, ()-> {
            postService.get(post.getId() + 1L);
        });



    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test9(){
        //given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        //expected
        assertThrows(PostNotFound.class, ()-> {
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test10(){
        //given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("초가집")
                .build();

        //expected
        assertThrows(PostNotFound.class, ()-> {
            postService.edit(post.getId() + 1L, postEdit);
        });
    }




}