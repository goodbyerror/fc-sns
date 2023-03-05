package com.fc.sns.model;

import com.fc.sns.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Comment {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Comment fromEntity(CommentEntity commentEntity) {
        return new Comment(commentEntity.getId(),
                commentEntity.getComment(),
                commentEntity.getUser().getUserName(),
                commentEntity.getId(),
                commentEntity.getRegisteredAt(),
                commentEntity.getUpdatedAt(),
                commentEntity.getDeletedAt()
        );
    }

}
