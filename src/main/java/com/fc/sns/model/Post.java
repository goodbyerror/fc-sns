package com.fc.sns.model;

import com.fc.sns.model.entity.PostEntity;
import com.fc.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class Post {
    private Integer id;
    private String title;
    private String body;
    private User user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Post fromEntity(PostEntity postEntity) {
        return Post.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .body(postEntity.getBody())
                .user(User.fromEntity(postEntity.getUser()))
                .registeredAt(postEntity.getRegisteredAt())
                .updatedAt(postEntity.getUpdatedAt())
                .deletedAt(postEntity.getDeletedAt())
                .build();
    }
}
