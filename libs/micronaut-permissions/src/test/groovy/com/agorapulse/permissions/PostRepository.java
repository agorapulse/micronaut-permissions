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
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PostRepository {

    private Map<Long, Post> posts = new HashMap<>();
    private long counter;

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setId(++counter);
        }
        posts.put(post.getId(), post);
        return post;
    }

    public Post get(Long id) {
        return posts.get(id);
    }

    public void clean() {
        posts = new HashMap<>();
        counter = 0;
    }

}
