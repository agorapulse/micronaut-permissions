package com.agorapulse.permissions;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PostRepository {

    private final Map<Long, Post> posts = new HashMap<>();
    private long counter;

    public Post save(Post post) {
        post.setId(++counter);
        posts.put(post.getId(), post);
        return post;
    }

    public Post get(Long id) {
        return posts.get(id);
    }

}
