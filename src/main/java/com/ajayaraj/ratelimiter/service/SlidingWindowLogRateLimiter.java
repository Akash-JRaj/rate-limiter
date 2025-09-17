package com.ajayaraj.ratelimiter.service;

import com.ajayaraj.ratelimiter.exception.RateLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class SlidingWindowLogRateLimiter implements RateLimiter{

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int LIMIT = 10;
    private static final int WINDOW_SIZE = 60;

    @Override
    public boolean isAllowed(String clientId) {

        long now = Instant.now().toEpochMilli(); // to avoid more request at same second

        String key = "rate_limit_log:" + clientId;

        redisTemplate.opsForZSet().removeRangeByScore(key, 0, now - (WINDOW_SIZE * 1000));

        Long count = redisTemplate.opsForZSet().zCard(key);

        if(count != null && count >= LIMIT) {
            throw new RateLimitExceededException("Too many requests!");
        }

        redisTemplate.opsForZSet().add(key, String.valueOf(now), now);

        redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SIZE));

        return true;
    }
}
