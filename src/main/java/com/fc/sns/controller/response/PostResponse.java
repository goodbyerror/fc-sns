package com.fc.sns.controller.response;

import com.fc.sns.model.Post;
import com.fc.sns.model.User;
import com.fc.sns.model.entity.PostEntity;
import com.fc.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class PostResponse {
    private Integer id;
    private String title;
    private String body;
    private User user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getTitle(),
                post.getUser(),
                post.getRegisteredAt(),
                post.getUpdatedAt(),
                post.getDeletedAt());
    }
}
