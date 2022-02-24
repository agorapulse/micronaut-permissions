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

}

@Singleton
@CompileStatic
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

    PermissionCheckResult withoutGranted(Object tested) {
        return permissionChecker.checkPermission(TemporaryPermissionsSpec.PERMISSION_1, tested)
    }

}
