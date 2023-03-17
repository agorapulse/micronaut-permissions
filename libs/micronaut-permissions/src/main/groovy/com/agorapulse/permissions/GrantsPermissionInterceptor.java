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

    public static final int POSITION = -350;

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
