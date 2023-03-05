package com.fc.sns.service;

import com.fc.sns.exception.ErrorCode;
import com.fc.sns.exception.SnsApplicationException;
import com.fc.sns.model.AlarmArgs;
import com.fc.sns.model.AlarmType;
import com.fc.sns.model.entity.AlarmEntity;
import com.fc.sns.model.entity.UserEntity;
import com.fc.sns.repository.AlarmEntityRepository;
import com.fc.sns.repository.EmitterRepository;
import com.fc.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";

    private final EmitterRepository emitterRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public void send(AlarmType alarmType, AlarmArgs alarmArgs, Integer receiveUserId) {
        UserEntity user = userEntityRepository.findById(receiveUserId).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND));
        AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(user, alarmType, alarmArgs));

        emitterRepository.get(receiveUserId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmEntity.getId().toString()).name(ALARM_NAME).data("new alarm"));
            } catch (IOException e) {
                emitterRepository.delete(receiveUserId);
                throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("no emitter founded"));
    }

    public SseEmitter connectAlarm(Integer userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            sseEmitter.send(SseEmitter.event().id("id").name(ALARM_NAME).data("connect completed"));
        } catch (IOException e) {
            throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
        }
        return sseEmitter;
    }
}
