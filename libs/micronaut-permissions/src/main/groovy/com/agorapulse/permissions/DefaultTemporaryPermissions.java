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
import java.util.function.Supplier;

@Singleton
public class DefaultTemporaryPermissions implements TemporaryPermissions {

    private final TemporaryPermissionsHolder temporaryPermissionsHolder;

    public DefaultTemporaryPermissions(TemporaryPermissionsHolder temporaryPermissionsHolder) {
        this.temporaryPermissionsHolder = temporaryPermissionsHolder;
    }

    @Override
    public <T> T grantPermissions(Iterable<String> permissionStrings, Iterable<Object> values, Supplier<T> withPermissions) {
        for (Object value : values) {
                for (String permission : permissionStrings) {
                    temporaryPermissionsHolder.grantPermission(permission, value);
                }
        }

        try {
            return withPermissions.get();
        } finally {
            for (Object value : values) {
                for (String permission : permissionStrings) {
                    temporaryPermissionsHolder.revokePermission(permission, value);
                }
            }
        }
    }

    @Override
    public <T> T grantPermissions(Iterable<String> permissionStrings, Supplier<T> withPermissions) {
        for (String permission : permissionStrings) {
            temporaryPermissionsHolder.grantPermission(permission);
        }

        try {
            return withPermissions.get();
        } finally {
            for (String permission : permissionStrings) {
                temporaryPermissionsHolder.revokePermission(permission);
            }
        }
    }
}
