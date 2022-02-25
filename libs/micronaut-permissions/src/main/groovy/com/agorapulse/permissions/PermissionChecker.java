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

/**
 * {@link PermissionChecker} is the service backing the {@link RequiresPermission} annotation and can be used
 * for low-level access. The default implementation uses existin beans of type {@link PermissionAdvisor}
 * to evaluate the permissions' conditions.
 */
public interface PermissionChecker {

    <T> PermissionCheckResult checkPermission(String permissionDefinition, T value, Argument<T> valueType);

    default <T, E extends Enum<E>> PermissionCheckResult checkPermission(E permission, T value, Argument<T> valueType) {
        return checkPermission(permission.name(), value, valueType);
    }

    @SuppressWarnings("unchecked")
    default PermissionCheckResult checkPermission(String permissionDefinition, Object value) {
        return checkPermission(permissionDefinition, value, Argument.of((Class<Object>) value.getClass()));
    }

    default <E extends Enum<E>> PermissionCheckResult checkPermission(E permission, Object value) {
        return checkPermission(permission.name(), value);
    }

    default <T> void requirePermission(String permissionDefinition, T value, Argument<T> valueType) {
        PermissionCheckResult permissionCheckResult = checkPermission(permissionDefinition, value, valueType);
        switch (permissionCheckResult) {
            case ALLOW:
                return;
            case DENY:
                throw new PermissionException(permissionDefinition, value, "The user does not have a permissions to perform the operation");
            case UNKNOWN:
            default:
                throw new PermissionException(permissionDefinition, value, "Cannot determine permissions check to perform operation");
        }
    }

    default <T, E extends Enum<E>> void requirePermission(E permission, T value, Argument<T> valueType) {
        requirePermission(permission.name(), value, valueType);
    }

    @SuppressWarnings("unchecked")
    default void requirePermission(String permissionDefinition, Object value) {
        requirePermission(permissionDefinition, value, Argument.of((Class<Object>) value.getClass()));
    }

    default <E extends Enum<E>> void requirePermission(E permission, Object value) {
        requirePermission(permission.name(), value);
    }

}
