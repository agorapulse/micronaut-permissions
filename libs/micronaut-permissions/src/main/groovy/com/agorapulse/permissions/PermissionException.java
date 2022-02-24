package com.agorapulse.permissions;

import javax.annotation.Nullable;

public class PermissionException extends RuntimeException {

    private final String permission;
    private final Object value;

    public PermissionException(@Nullable String permission, @Nullable Object value) {
        this.permission = permission;
        this.value = value;
    }

    public PermissionException(@Nullable String permission, @Nullable Object value, String message) {
        super(message);
        this.permission = permission;
        this.value = value;
    }

    public PermissionException(@Nullable String permission, @Nullable Object value, String message, Throwable cause) {
        super(message, cause);
        this.permission = permission;
        this.value = value;
    }

    public PermissionException(@Nullable String permission, @Nullable Object value, Throwable cause) {
        super(cause);
        this.permission = permission;
        this.value = value;
    }

    public PermissionException(@Nullable String permission, @Nullable Object value, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.permission = permission;
        this.value = value;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    @Nullable
    public Object getValue() {
        return value;
    }
}
