package com.fc.sns.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class EmitterRepository {

    private Map<String, SseEmitter> sseEmitterMap = new HashMap<>();

    public SseEmitter save(Integer userId, SseEmitter sseEmitter) {
        final String key = getKey(userId);
        sseEmitterMap.put(key, sseEmitter);

        return sseEmitter;
    }

    public Optional<SseEmitter> get(Integer userId) {
        final String key = getKey(userId);
        return Optional.ofNullable(sseEmitterMap.get(key));
    }

    public void delete(Integer userId) {
        sseEmitterMap.remove(getKey(userId));
    }

    private String getKey(Integer userId) {
        return "Emitter:UID:" + userId;
    }
}
