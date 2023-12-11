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
package com.agorapulse.permissions

import groovy.transform.CompileStatic
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
class TemporaryPermissionsSpec extends Specification {

    public static final Object TESTER_1 = 'this is a tester one'
    public static final Object TESTER_2 = 'this is a tester two'

    public static final String PERMISSION_1 = 'one'
    public static final String PERMISSION_2 = 'two'

    @Inject GrantPermissionsTester tester
    @Inject TemporaryPermissions temporaryPermissions

    void 'call without grants'() {
        expect:
            tester.withoutGranted(TESTER_1) == PermissionCheckResult.UNKNOWN
    }

    void 'with permission one granted'() {
        expect:
            tester.withoutGranted(TESTER_1) == PermissionCheckResult.UNKNOWN
            tester.withPermissionOneGranted(TESTER_1) == PermissionCheckResult.ALLOW
            tester.withoutGranted(TESTER_1) == PermissionCheckResult.UNKNOWN
    }

    void 'with permission one temporary granted'() {
        expect:
            tester.withoutGranted(TESTER_1) == PermissionCheckResult.UNKNOWN
            temporaryPermissions.grantPermissions(PERMISSION_1, TESTER_1) {
                tester.withoutGranted(TESTER_1) == PermissionCheckResult.ALLOW
            }
            tester.withoutGranted(TESTER_1) == PermissionCheckResult.UNKNOWN
    }

    void 'with permission two granted'() {
        expect:
            tester.withPermissionTwoGranted(TESTER_1) == PermissionCheckResult.UNKNOWN
    }

    void 'with permission one on the first argument granted'() {
        expect:
            tester.withoutGranted(TESTER_1) == PermissionCheckResult.UNKNOWN
            tester.withPermissionOneFirstArgumentGranted(TESTER_1, TESTER_2) == PermissionCheckResult.ALLOW
            tester.withoutGranted(TESTER_1) == PermissionCheckResult.UNKNOWN
    }

    void 'with permission one on the second argument granted'() {
        expect:
            tester.withPermissionOnSecondArgumentGranted(TESTER_1, TESTER_2) == PermissionCheckResult.UNKNOWN
    }

    void 'with permission one result temporarily granted'() {
        when:
            temporaryPermissions.grantPermissions(PERMISSION_1) {
                tester.withResultRequiresPermission(TESTER_1) == PermissionCheckResult.UNKNOWN
            }
        then:
            noExceptionThrown()
    }

}

@Singleton
@CompileStatic
@SuppressWarnings('UnusedMethodParameter')
class GrantPermissionsTester {

    private final PermissionChecker permissionChecker

    GrantPermissionsTester(PermissionChecker permissionChecker) {
        this.permissionChecker = permissionChecker
    }

    @GrantsPermission(TemporaryPermissionsSpec.PERMISSION_1)
    PermissionCheckResult withPermissionOneGranted(Object tested) {
        return withoutGranted(tested)
    }

    @SuppressWarnings('unused')
    @GrantsPermission(value = TemporaryPermissionsSpec.PERMISSION_1, target = 'one')
    PermissionCheckResult withPermissionOneFirstArgumentGranted(Object one, Object two) {
        return withoutGranted(one)
    }

    @SuppressWarnings('unused')
    @GrantsPermission(value = TemporaryPermissionsSpec.PERMISSION_1, target = 'two')
    PermissionCheckResult withPermissionOnSecondArgumentGranted(Object one, Object two) {
        return withoutGranted(one)
    }

    @GrantsPermission(TemporaryPermissionsSpec.PERMISSION_2)
    PermissionCheckResult withPermissionTwoGranted(Object tested) {
        return withoutGranted(tested)
    }

    @ResultRequiresPermission(TemporaryPermissionsSpec.PERMISSION_1)
    Object withResultRequiresPermission(Object tested) {
        withoutGranted(tested)
        return tested
    }

    PermissionCheckResult withoutGranted(Object tested) {
        return permissionChecker.checkPermission(TemporaryPermissionsSpec.PERMISSION_1, tested)
    }

}
