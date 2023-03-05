package com.fc.sns.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    NEW_COMMENT_ON_POST("NEW COMMENT!"),
    NEW_LIKE_ON_POST("NEW LIKE!")
    ;

    private final String alarmText;
}
