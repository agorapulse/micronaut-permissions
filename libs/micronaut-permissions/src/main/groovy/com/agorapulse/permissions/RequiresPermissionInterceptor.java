/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2023 Agorapulse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        boolean atLeastOneAllowed = false;
        for (Map.Entry<String, MutableArgumentValue<?>> e : context.getParameters().entrySet()) {
            Object value = e.getValue().getValue();
            Argument<Object> argument = (Argument<Object>) e.getValue();
            PermissionCheckResult result = permissionChecker.checkPermission(permissionString, value, argument);
            switch (result) {
                case DENY:
                    throw new PermissionException(permissionString, value, "The user does not have a permissions to perform operation");
                case ALLOW:
                    atLeastOneAllowed = true;
                case UNKNOWN:
                default:
                    // continue to the next argument
            }
        }

        if (atLeastOneAllowed) {
            return context.proceed();
        }

        throw new PermissionException(permissionString, context.getTargetMethod(), "Cannot determine if the user has the permissions to perform operation");
    }

    @Override
    public int getOrder() {
        return POSITION;
    }
}
