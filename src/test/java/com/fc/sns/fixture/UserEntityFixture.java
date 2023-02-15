package com.fc.sns.fixture;

import com.fc.sns.model.entity.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(String userName, String password, Integer id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        return userEntity;
    }
}
