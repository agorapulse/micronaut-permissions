package com.agorapulse.permissions;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.type.Argument;
import io.micronaut.core.type.MutableArgumentValue;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class RequiresPermissionInterceptor implements MethodInterceptor<Object, Object> {

    public static final int POSITION = -300;

    private final PermissionChecker permissionChecker;

    public RequiresPermissionInterceptor(PermissionChecker permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        AnnotationValue<RequiresPermission> annotation = context.findAnnotation(RequiresPermission.class)
                .orElseThrow(() -> new PermissionException(null, context.getTargetMethod(), "Method without @RequiresPermission annotation!"));

        String permissionString = annotation.getRequiredValue(String.class);
        for (Map.Entry<String, MutableArgumentValue<?>> e : context.getParameters().entrySet()) {
            Object value = e.getValue().getValue();
            Argument<Object> argument = (Argument<Object>) e.getValue();
            PermissionCheckResult result = permissionChecker.checkPermission(permissionString, value, argument);
            switch (result) {
                case DENY:
                    throw new PermissionException(permissionString, value, "The user does not have a permissions to perform operation");
                case ALLOW:
                    return context.proceed();
                case UNKNOWN:
                default:
                    // continue to the next argument
            }
        }

        throw new PermissionException(permissionString, context.getTargetMethod(), "Cannot determine if the user has the permissions to perform operation");
    }

    @Override
    public int getOrder() {
        return POSITION;
    }
}
