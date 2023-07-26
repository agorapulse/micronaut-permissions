package com.agorapulse.permissions;

import java.util.Collection;
import java.util.Map;

public interface IPostService {

    Post create(Long userId, String message);

    @ResultRequiresPermission(value = "view")                                           // <2>
    Post get(Long id);

    @RequiresPermission("edit")
    Post archive(Post post);

    @RequiresPermission("edit")
    void handleIterableContainer(Collection<Post> posts);

    @RequiresPermission("edit")
    void handleContainerNonIterable(Post post, Map<String, String> couldBeIterableContainer);

    @RequiresPermission("edit")
    Post publish(Post post);

    @ResultRequiresPermission(value = "view", returnNull = true)                        // <3>
    Post getOrEmpty(Long id);

    @RequiresPermission("read")
    Post merge(Long userId, Post post1, Post post2);

}
