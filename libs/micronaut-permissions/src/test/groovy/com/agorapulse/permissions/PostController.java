package com.agorapulse.permissions;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;

import javax.annotation.Nullable;

@Controller("/post")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    public PostController(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @Status(HttpStatus.CREATED)
    @io.micronaut.http.annotation.Post("/")
    public Post create(@Nullable @Header("X-User-Id") Long userId, String message) {
        return postRepository.save(postService.create(userId, message));
    }

    @Delete("/{id}")
    public Post archive(Long id) {
        return postService.archive(postRepository.get(id));
    }

    @Put("/{id}")
    public Post publish(Long id) {
        return postService.publish(postRepository.get(id));
    }

    @Error(PermissionException.class)
    public HttpResponse<JsonError> permissionException(PermissionException ex) {
        return HttpResponse.<JsonError>unauthorized().body(new JsonError(ex.getMessage()));
    }

    @Error(IllegalArgumentException.class)
    public HttpResponse<JsonError> illegalArgumentException(IllegalArgumentException ex) {
        return HttpResponse.badRequest(new JsonError(ex.getMessage()));
    }

}
