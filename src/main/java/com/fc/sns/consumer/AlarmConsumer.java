package com.fc.sns.consumer;

import com.fc.sns.model.event.AlarmEvent;
import com.fc.sns.service.AlarmService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.swing.plaf.PanelUI;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmConsumer {

    private final AlarmService alarmService;

    @KafkaListener(topics = "${spring.kafka.topic.alarm}")
    public void consumeAlarm(AlarmEvent event, Acknowledgment acknowledgment) {
        log.info("Consume the event {}", event);
        alarmService.send(event.getAlarmType(), event.getAlarmArgs(), event.getReceiveUserId());
        acknowledgment.acknowledge();
    }
}
