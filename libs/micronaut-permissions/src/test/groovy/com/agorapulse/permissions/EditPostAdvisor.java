/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2022 Agorapulse.
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

import io.micronaut.core.type.Argument;

import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class EditPostAdvisor implements PermissionAdvisor<Post> {

    private final UserProvider provider;

    public EditPostAdvisor(UserProvider provider) {
        this.provider = provider;
    }

    @Override
    public Argument<Post> argumentType() {
        return Argument.of(Post.class);
    }

    @Override
    public PermissionCheckResult checkPermissions(
        String permissionDefinition,
        Post value,
        Argument<Post> argument
    ) {
        if (provider == null || value == null || !"edit".equals(permissionDefinition)) {
            return PermissionCheckResult.UNKNOWN;
        }

        return provider.getCurrentUser().map(u -> {
            if (Objects.equals(u.getId(), value.getAuthorId())) {
                return PermissionCheckResult.ALLOW;
            }
            return PermissionCheckResult.DENY;
        }).orElse(PermissionCheckResult.DENY);
    }
}
