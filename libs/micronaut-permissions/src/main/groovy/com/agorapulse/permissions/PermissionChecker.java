package com.agorapulse.permissions;

import io.micronaut.core.type.Argument;

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
