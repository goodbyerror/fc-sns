package com.fc.sns.controller.response;

import com.fc.sns.model.Alarm;
import com.fc.sns.model.AlarmArgs;
import com.fc.sns.model.AlarmType;
import com.fc.sns.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;
    private AlarmArgs alarmArgs;
    private AlarmType alarmType;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmArgs(),
                alarm.getAlarmType(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }

}
