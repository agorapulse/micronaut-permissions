/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2023 Agorapulse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agorapulse.permissions;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;

@Singleton
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post create(Long userId, String message) {
        if (userId == null || userId == 0) {
            throw new IllegalArgumentException("User not specified");
        }
        return Post.createDraft(userId, message);
    }

    @RequiresPermission("edit")                                                         // <1>
    public Post archive(Post post) {
        return post.archive();
    }

    @RequiresPermission("edit")
    public void handleIterableContainer(Collection<Post> posts) {
    }

    @RequiresPermission("edit")
    public void handleContainerNonIterable(Post post, Map<String, String> couldBeIterableContainer) {
    }


    @RequiresPermission("edit")
    public Post publish(Post post) {
        return post.publish();
    }

    @ResultRequiresPermission(value = "view")                                           // <2>
    public Post get(Long id) {
        return postRepository.get(id);
    }

    @ResultRequiresPermission(value = "view", returnNull = true)                        // <3>
    public Post getOrEmpty(Long id) {
        return postRepository.get(id);
    }

    @RequiresPermission("read")
    public Post merge(Long userId, Post post1, Post post2) {
        return Post.createDraft(userId, post1.getMessage() + post2.getMessage());
    }

}
