package com.agorapulse.permissions;

import javax.inject.Singleton;
import java.util.function.Supplier;

@Singleton
public class DefaultTemporaryPermissions implements TemporaryPermissions {

    private final TemporaryPermissionsHolder temporaryPermissionsHolder;

    public DefaultTemporaryPermissions(TemporaryPermissionsHolder temporaryPermissionsHolder) {
        this.temporaryPermissionsHolder = temporaryPermissionsHolder;
    }

    @Override
    public <T> T grantPermissions(Iterable<String> permissionStrings, Iterable<Object> values, Supplier<T> withPermissions) {
        for (Object value : values) {
                for (String permission : permissionStrings) {
                    temporaryPermissionsHolder.grantPermission(permission, value);
                }
        }

        try {
            return withPermissions.get();
        } finally {
            for (Object value : values) {
                for (String permission : permissionStrings) {
                    temporaryPermissionsHolder.revokePermission(permission, value);
                }
            }
        }
    }

}
