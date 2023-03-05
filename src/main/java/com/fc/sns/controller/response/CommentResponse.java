package com.fc.sns.controller.response;

import com.fc.sns.model.Comment;
import com.fc.sns.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUserName(),
                comment.getPostId(),
                comment.getRegisteredAt(),
                comment.getUpdatedAt(),
                comment.getDeletedAt()
        );
    }
}
