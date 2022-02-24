package com.agorapulse.permissions;

import java.util.Collections;
import java.util.function.Supplier;

public interface TemporaryPermissions {

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
