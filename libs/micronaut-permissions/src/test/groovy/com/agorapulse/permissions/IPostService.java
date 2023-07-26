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
