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

import io.micronaut.core.order.Ordered;
import io.micronaut.core.type.Argument;

/**
 * The permission advisor checks the permissions for the given object and the current execution
 * context.
 *
 * @param <T> the type of the objects which permissions are being checked
 */
public interface PermissionAdvisor<T> extends Ordered {

    /**
     * @return the argument type of the objects being checked
     */
    Argument<T> argumentType();

    /**
     * Checks whether the operation described by the <code>permissionDefinition</code> can be
     * executed within current execution context on the <code>value</code> object.
     *
     * @param permissionDefinition the string definition of the permission which are being
     *                            evaluated
     * @param value the current value being evaluated
     * @param argument the argument with can be source of additional metadata such as annotations
     *                being present
     * @return <code>ALLOW</code> if the operation is allowed,
     *      <code>DENY</code> if the operation is forbidden or
     *      <code>UNKNOWN</code> if the result cannot be decided
     */
    PermissionCheckResult checkPermissions(
        String permissionDefinition,
        T value,
        Argument<T> argument
    );

}
