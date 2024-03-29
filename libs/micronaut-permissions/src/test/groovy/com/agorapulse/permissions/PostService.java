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

import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Map;

@Singleton
public class PostService implements IPostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post create(Long userId, String message) {
        if (userId == null || userId == 0) {
            throw new IllegalArgumentException("User not specified");
        }
        return Post.createDraft(userId, message);
    }

    @Override                                                  // <1>
    public Post archive(Post post) {
        return post.archive();
    }

    @Override
    public void handleIterableContainer(Collection<Post> posts) {
    }

    @Override
    public void handleContainerNonIterable(Post post, Map<String, String> couldBeIterableContainer) {
    }

    @Override
    public Post publish(Post post) {
        return post.publish();
    }

    @Override
    public Post get(Long id) {
        return postRepository.get(id);
    }

    @Override
    public Post getOrEmpty(Long id) {
        return postRepository.get(id);
    }

    @Override
    public Post merge(Long userId, Post post1, Post post2) {
        return Post.createDraft(userId, post1.getMessage() + post2.getMessage());
    }

}
