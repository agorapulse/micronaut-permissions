package com.agorapulse.permissions;

import javax.inject.Singleton;
import javax.validation.constraints.NotEmpty;

@Singleton
public class PostService {

    public Post create(Long userId, String message) {
        if (userId == null || userId == 0) {
            throw new IllegalArgumentException("User not specified");
        }
        return Post.createDraft(userId, message);
    }

    @RequiresPermission("edit")
    public Post archive(Post post) {
        return post.archive();
    }

    @RequiresPermission("edit")
    public Post publish(Post post) {
        return post.publish();
    }

}
