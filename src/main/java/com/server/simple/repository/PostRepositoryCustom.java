package com.server.simple.repository;

import com.server.simple.domain.Post;
import com.server.simple.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom  {

    List<Post> getList(PostSearch postSearch);
}
