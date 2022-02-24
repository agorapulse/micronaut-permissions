package com.agorapulse.permissions;

public interface TemporaryPermissionsHolder {

    boolean isPermissionGranted(String permissionDefinition, Object value);
    void grantPermission(String permissionDefinition, Object value);
    void revokePermission(String permissionDefinition, Object value);

}
