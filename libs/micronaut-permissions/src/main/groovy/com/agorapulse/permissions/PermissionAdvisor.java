package com.agorapulse.permissions;

import io.micronaut.core.order.Ordered;
import io.micronaut.core.type.Argument;

public interface PermissionAdvisor<T> extends Ordered {

    Argument<T> argumentType();
    PermissionCheckResult checkPermissions(String permissionDefinition, T value, Argument<T> argument);

}
