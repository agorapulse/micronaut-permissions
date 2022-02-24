package com.agorapulse.permissions;

import io.micronaut.runtime.context.scope.ThreadLocal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ThreadLocal
public class ThreadLocalTemporaryPermissionsHolder implements TemporaryPermissionsHolder {

    private final Map<String, Set<Object>> grantedPermissions = new HashMap<>();

    public boolean isPermissionGranted(String permissionDefinition, Object value) {
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
}
