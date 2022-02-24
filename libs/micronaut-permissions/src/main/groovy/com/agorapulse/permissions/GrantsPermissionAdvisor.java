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
