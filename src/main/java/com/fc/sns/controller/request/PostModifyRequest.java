package com.fc.sns.controller.request;

import com.fc.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostModifyRequest {
    private Integer id;
    private String title;
    private String body;
    private UserEntity user;

}
