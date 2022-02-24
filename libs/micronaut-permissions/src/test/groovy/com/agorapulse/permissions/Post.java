package com.agorapulse.permissions;

public class Post {

    public enum Status { DRAFT, PUBLISHED, ARCHIVED }

    public static Post createDraft(Long authorId, String message) {
        return new Post(null, authorId, message, Status.DRAFT);
    }

    private Long id;
    private final Status status;
    private final Long authorId;
    private final String message;

    private Post(Long id, Long authorId, String message, Status status) {
        this.id = id;
        this.authorId = authorId;
        this.message = message;
        this.status = status;
    }

    Post publish() {
        return new Post(id, authorId, message, Status.PUBLISHED);
    }

    Post archive() {
        return new Post(id, authorId, message, Status.ARCHIVED);
    }

    public Status getStatus() {
        return status;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getMessage() {
        return message;
    }

    void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
