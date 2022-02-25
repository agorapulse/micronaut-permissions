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

public class Post {

    public enum Status { DRAFT, PUBLISHED, ARCHIVED }

    public static Post createDraft(Long authorId, String message) {
        return new Post(null, authorId, message, Status.DRAFT);
    }

    private Long id;
    private final Status status;
    private final Long authorId;
    private final String message;

    private Post(Long id, Long authorId, String message, Status status) {
        this.id = id;
        this.authorId = authorId;
        this.message = message;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    Post publish() {
        return new Post(id, authorId, message, Status.PUBLISHED);
    }

    Post archive() {
        return new Post(id, authorId, message, Status.ARCHIVED);
    }

}
