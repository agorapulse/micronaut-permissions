package com.agorapulse.permissions;

import io.micronaut.core.order.OrderUtil;
import io.micronaut.core.type.Argument;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class DefaultPermissionChecker implements PermissionChecker {

    private final List<PermissionAdvisor<?>> advisors;

    public DefaultPermissionChecker(List<PermissionAdvisor<?>> advisors) {
        OrderUtil.sort(advisors);
        this.advisors = advisors;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> PermissionCheckResult checkPermission(String permissionDefinition, T value, Argument<T> valueType) {
        if (valueType.isContainerType() && valueType.hasTypeVariables()) {
            return checkPermissionOnContainer(permissionDefinition, (Iterable<Object>) value, (Argument<Iterable<Object>>) valueType);
        }

        boolean permissionDenied = false;
        for (PermissionAdvisor<?> advisor : advisors) {
            if (advisor.argumentType().getType().isAssignableFrom(valueType.getType())) {
                PermissionAdvisor<Object> typeSafeAdvisor = (PermissionAdvisor<Object>) advisor;
                PermissionCheckResult permissionCheckResult = typeSafeAdvisor.checkPermissions(permissionDefinition, value, (Argument<Object>) valueType);
                switch (permissionCheckResult) {
                    case ALLOW:
                        return PermissionCheckResult.ALLOW;
                    case DENY:
                        permissionDenied = true;
                    case UNKNOWN:
                    default:
                        // continue to the next advisor
                }
            }
        }

        return permissionDenied ? PermissionCheckResult.DENY : PermissionCheckResult.UNKNOWN;
    }

    @SuppressWarnings("unchecked")
    public <T> PermissionCheckResult checkPermissionOnContainer(String permissionDefinition, Iterable<T> value, Argument<Iterable<T>> valueType) {
        for (T item : value) {
            switch (checkPermission(permissionDefinition, item, valueType.getTypeParameters()[0])) {
                case DENY:
                    return PermissionCheckResult.DENY;
                case UNKNOWN:
                    return PermissionCheckResult.UNKNOWN;
                case ALLOW:
                default:
                    // proceed to next item
            }
        }

        return PermissionCheckResult.ALLOW;
    }
}
