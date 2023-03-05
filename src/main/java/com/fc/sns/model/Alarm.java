package com.fc.sns.model;

import com.fc.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity alarmEntity) {
        return new Alarm(
                alarmEntity.getId(),
                alarmEntity.getAlarmType(),
                alarmEntity.getAlarmArgs(),
                alarmEntity.getRegisteredAt(),
                alarmEntity.getUpdatedAt(),
                alarmEntity.getDeletedAt()
        );
    }
}
