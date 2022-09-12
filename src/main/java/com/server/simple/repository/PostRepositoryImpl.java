package com.server.simple.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.simple.domain.Post;
import com.server.simple.domain.QPost;
import com.server.simple.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.server.simple.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset((long)(postSearch.getOffset()))
                .orderBy(post.id.desc())
                .fetch();
    }
}
