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

import io.micronaut.runtime.context.scope.ThreadLocal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ThreadLocal
class ThreadLocalTemporaryPermissionsHolder implements TemporaryPermissionsHolder {

    private final Map<String, Set<Object>> grantedPermissions = new HashMap<>();
    private final Set<String> grantedForAll = new HashSet<>();

    public boolean isPermissionGranted(String permissionDefinition, Object value) {
        if (grantedForAll.contains(permissionDefinition)) {
            return true;
        }
        if (!grantedPermissions.containsKey(permissionDefinition)) {
            return false;
        }
        return grantedPermissions.computeIfAbsent(permissionDefinition, d -> new HashSet<>()).contains(value);
    }

    public void grantPermission(String permissionDefinition, Object value) {
        grantedPermissions.computeIfAbsent(permissionDefinition, d -> new HashSet<>()).add(value);
    }

    @Override
    public void revokePermission(String permissionDefinition, Object value) {
        if (!grantedPermissions.containsKey(permissionDefinition)) {
            return;
        }

        grantedPermissions.computeIfAbsent(permissionDefinition, d -> new HashSet<>()).remove(value);
    }

    @Override
    public void grantPermission(String permissionDefinition) {
        grantedForAll.add(permissionDefinition);
    }

    @Override
    public void revokePermission(String permissionDefinition) {
        grantedForAll.remove(permissionDefinition);
    }

}
