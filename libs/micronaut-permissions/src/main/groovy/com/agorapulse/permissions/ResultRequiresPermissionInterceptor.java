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

import jakarta.inject.Singleton;

@Singleton
public class ResultRequiresPermissionInterceptor implements MethodInterceptor<Object, Object> {

    public static final int POSITION = -299;

    private final PermissionChecker permissionChecker;

    public ResultRequiresPermissionInterceptor(PermissionChecker permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        AnnotationValue<ResultRequiresPermission> annotation = context.findAnnotation(ResultRequiresPermission.class)
            .orElseThrow(() -> new PermissionException(null, context.getTargetMethod(), "Method without @ResultRequiresPermission annotation!"));

        String permissionString = annotation.getRequiredValue(String.class);
        Object value = context.proceed();

        if (value == null) {
            return null;
        }

        Argument<Object> argument = context.getReturnType().asArgument();
        PermissionCheckResult result = permissionChecker.checkPermission(permissionString, value, argument);

        boolean returnNull = annotation.get("returnNull", Boolean.class, Boolean.FALSE);

        if (returnNull) {
            switch (result) {
                case DENY:
                    return null;
                case ALLOW:
                    return value;
                default:
                    throw new PermissionException(permissionString, context.getTargetMethod(), "Cannot determine if the user has the permissions to perform operation");
            }
        }

        switch (result) {
            case DENY:
                throw new PermissionException(permissionString, value, "The user does not have a permissions to perform operation");
            case ALLOW:
                return value;
            default:
                throw new PermissionException(permissionString, context.getTargetMethod(), "Cannot determine if the user has the permissions to perform operation");
        }

    }

    @Override
    public int getOrder() {
        return POSITION;
    }
}
