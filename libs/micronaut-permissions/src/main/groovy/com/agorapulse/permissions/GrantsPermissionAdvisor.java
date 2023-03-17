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

import io.micronaut.core.type.Argument;

import javax.inject.Singleton;

@Singleton
public class GrantsPermissionAdvisor implements PermissionAdvisor<Object> {

    private final TemporaryPermissionsHolder temporaryPermissionsHolder;

    public GrantsPermissionAdvisor(TemporaryPermissionsHolder temporaryPermissionsHolder) {
        this.temporaryPermissionsHolder = temporaryPermissionsHolder;
    }

    @Override
    public Argument<Object> argumentType() {
        return Argument.OBJECT_ARGUMENT;
    }

    @Override
    public PermissionCheckResult checkPermissions(String permissionDefinition, Object value, Argument<Object> argument) {
        if (temporaryPermissionsHolder.isPermissionGranted(permissionDefinition, value)) {
            return PermissionCheckResult.ALLOW;
        }
        return PermissionCheckResult.UNKNOWN;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
