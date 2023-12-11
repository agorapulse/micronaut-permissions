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

import java.util.Collections;
import java.util.function.Supplier;

/**
 * This is a lowel access to the functionality provided by {@link GrantsPermission} annotation.
 */
public interface TemporaryPermissions {

    default <T> T grantPermissions(String permissionDefinition, Supplier<T> withPermissions) {
        return grantPermissions(Collections.singleton(permissionDefinition), withPermissions);
    }

    <T> T grantPermissions(Iterable<String> permissionStrings, Supplier<T> withPermissions);


    default <T> T grantPermissions(String permissionDefinition, Object value, Supplier<T> withPermissions) {
        return grantPermissions(Collections.singleton(permissionDefinition), Collections.singleton(value), withPermissions);
    }

    default <T> T grantPermissions(Iterable<String> permissionDefinition, Object value, Supplier<T> withPermissions) {
        return grantPermissions(permissionDefinition, Collections.singleton(value), withPermissions);
    }

    default <T> T grantPermissions(String permissionDefinition, Iterable<Object> values, Supplier<T> withPermissions){
        return grantPermissions(Collections.singleton(permissionDefinition), values, withPermissions);
    }

    <T> T grantPermissions(Iterable<String> permissionDefinition, Iterable<Object> values, Supplier<T> withPermissions);

}
