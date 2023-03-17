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

import io.micronaut.http.context.ServerRequestContext;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton                                                                              // <1>
public class RequestUserProvider implements UserProvider {

    private static final String KEY = "com.agorapulse.user";

    private final UserRepository userRepository;

    public RequestUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentUser() {
        return ServerRequestContext.currentRequest().flatMap(request -> {               // <2>
            Optional<User> existingUser = request.getAttribute(KEY, User.class);        // <3>

            if (existingUser.isPresent()) {
                return existingUser;
            }

            Optional<Long> maybeUserId = request.getHeaders().get("X-User-Id", Long.class);

            if (!maybeUserId.isPresent()) {
                return Optional.empty();
            }

            User user = userRepository.get(maybeUserId.get());                          // <4>

            request.setAttribute(KEY, user);                                            // <5>

            return Optional.of(user);
        });
    }

}
