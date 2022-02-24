package com.agorapulse.permissions;

import io.micronaut.core.type.Argument;

import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class EditPostAdvisor implements PermissionAdvisor<Post> {

    private final UserProvider provider;

    public EditPostAdvisor(UserProvider provider) {
        this.provider = provider;
    }

    @Override
    public Argument<Post> argumentType() {
        return Argument.of(Post.class);
    }

    @Override
    public PermissionCheckResult checkPermissions(String permissionDefinition, Post value, Argument<Post> argument) {
        if (value == null || !"edit".equals(permissionDefinition)) {
            return PermissionCheckResult.UNKNOWN;
        }

        return provider.getCurrentUser().map(u -> {
            if (Objects.equals(u.getId(), value.getAuthorId())) {
                return PermissionCheckResult.ALLOW;
            }
            return PermissionCheckResult.DENY;
        }).orElse(PermissionCheckResult.DENY);
    }
}
