package com.ajayaraj.ratelimiter.service;

import com.ajayaraj.ratelimiter.exception.RateLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class FixedWindowRateLimiter implements RateLimiter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final int LIMIT = 10;
    private final int WINDOW_SIZE = 60;

    @Override
    public boolean isAllowed(String clientId) {

        long windowStart = Instant.now().getEpochSecond() / WINDOW_SIZE;
        String key = "rate_limit:" + clientId + ":" + windowStart;

        System.out.println(key);

        Long current = stringRedisTemplate.opsForValue().increment(key);

        if(current == 1) {
            stringRedisTemplate.expire(key, Duration.ofSeconds(WINDOW_SIZE));
        }

        if(current > LIMIT) {
            throw new RateLimitExceededException("Too many requests!");
        }

        return current <= LIMIT;
    }
}
