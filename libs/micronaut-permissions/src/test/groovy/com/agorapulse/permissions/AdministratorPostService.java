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

@Singleton
public class AdministratorPostService {

    private final PostService postService;
    private final PostRepository postRepository;
    private final TemporaryPermissions temporaryPermissions;

    public AdministratorPostService(
        PostService postService,
        PostRepository postRepository,
        TemporaryPermissions temporaryPermissions
    ) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.temporaryPermissions = temporaryPermissions;
    }

    @GrantsPermission("edit")                                                           // <1>
    public Post archive(Post post) {
        return postRepository.save(postService.archive(post));
    }

    public Post publish(Post post) {
        Post publishedPost = temporaryPermissions.grantPermissions("edit", post, () -> {// <2>
            return postService.archive(post);
        });
        return postRepository.save(publishedPost);
    }

}
