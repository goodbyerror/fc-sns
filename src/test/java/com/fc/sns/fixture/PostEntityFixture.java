package com.fc.sns.fixture;

import com.fc.sns.model.entity.PostEntity;
import com.fc.sns.model.entity.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(String userName, Integer postId, Integer userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUserName(userName);

        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);
        postEntity.setUser(userEntity);
        return postEntity;
    }
}
