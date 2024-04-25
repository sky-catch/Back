package com.example.core.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    /**
     * set과 동시에 만료 시간 설정
     */
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public Optional<String> getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        return Optional.ofNullable((String) values.get(key));
    }

    /**
     * key에 해당하는 데이터 삭제
     */
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    /**
     * timeoutSecond 지나고 데이터 삭제
     */
    public void expireValues(String key, int timeoutSecond) {
        redisTemplate.expire(key, timeoutSecond, TimeUnit.SECONDS);
    }

}
