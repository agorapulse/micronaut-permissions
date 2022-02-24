package com.agorapulse.permissions;

import io.micronaut.http.HttpRequest;
import io.micronaut.runtime.http.scope.RequestAware;
import io.micronaut.runtime.http.scope.RequestScope;

import java.util.Optional;

@RequestScope
public class UserProvider implements RequestAware  {

    private HttpRequest<?> request;

    public static Optional<User> getCurrentUser(HttpRequest<?> request) {
        if (request == null) {
            return Optional.empty();
        }

        Optional<Long> userId = request.getHeaders().get("X-User-Id", Long.class);
        return userId.map(User::new);
    }

    @Override
    public void setRequest(HttpRequest<?> request) {
        this.request = request;
    }

    public Optional<User> getCurrentUser() {
        return getCurrentUser(request);
    }

}
