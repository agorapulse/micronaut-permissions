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

import javax.annotation.Nullable;

/**
 * The exception is throw when there are missing permissions for a particular value.
 */
public class PermissionException extends RuntimeException {

    private final String permission;
    private final Object value;

    public PermissionException(@Nullable String permission, @Nullable Object value) {
        this.permission = permission;
        this.value = value;
    }

    public PermissionException(@Nullable String permission, @Nullable Object value, String message) {
        super(message);
        this.permission = permission;
        this.value = value;
    }

    public PermissionException(@Nullable String permission, @Nullable Object value, String message, Throwable cause) {
        super(message, cause);
        this.permission = permission;
        this.value = value;
    }

    public PermissionException(@Nullable String permission, @Nullable Object value, Throwable cause) {
        super(cause);
        this.permission = permission;
        this.value = value;
    }

    public PermissionException(@Nullable String permission, @Nullable Object value, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.permission = permission;
        this.value = value;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    @Nullable
    public Object getValue() {
        return value;
    }
}
