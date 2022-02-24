package com.agorapulse.permissions;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.type.MutableArgumentValue;
import io.micronaut.core.util.StringUtils;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Singleton
public class GrantsPermissionInterceptor implements MethodInterceptor<Object, Object> {

    public static final int POSITION = -250;

    private final TemporaryPermissions temporaryPermissions;

    public GrantsPermissionInterceptor(TemporaryPermissions temporaryPermissions) {
        this.temporaryPermissions = temporaryPermissions;
    }

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        AnnotationValue<GrantsPermission> annotation = context.findAnnotation(GrantsPermission.class)
                .orElseThrow(() -> new PermissionException(null, context.getTargetMethod(), "Method without @GrantsPermission annotation!"));

        String[] permissionStrings = annotation.stringValues();
        List<String> targets = Arrays.stream(annotation.stringValues("target"))
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());

        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, MutableArgumentValue<?>> e : context.getParameters().entrySet()) {
            if (targets.isEmpty() || targets.contains(e.getKey())) {
                values.add(e.getValue().getValue());
            }
        }

        return temporaryPermissions.grantPermissions(Arrays.asList(permissionStrings), values, (Supplier<Object>) context::proceed);
    }

    @Override
    public int getOrder() {
        return POSITION;
    }
}
