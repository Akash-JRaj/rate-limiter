package com.ajayaraj.ratelimiter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class SlidingWindowCounterRateLimiter implements RateLimiter{

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int LIMIT = 10;
    private static final int WINDOW_SIZE = 60;

    @Override
    public boolean isAllowed(String clientId) {
        long now = Instant.now().getEpochSecond();

        long currWindow = now / WINDOW_SIZE;
        long prevWindow = currWindow - 1;

        String currKey = "rate_limit_counter:" + clientId + currWindow;
        String prevKey = "rate_limit_counter:" + clientId + prevWindow;

        int currentCount = getCount(currKey);
        int prevCount = getCount(prevKey);

        double weight = (WINDOW_SIZE - (now % WINDOW_SIZE)) / (double) WINDOW_SIZE;
        double effectiveCount = currentCount + prevCount * weight;

        if(effectiveCount < LIMIT) {
            redisTemplate.opsForValue().increment(currKey);

            redisTemplate.expire(currKey, Duration.ofSeconds(WINDOW_SIZE * 2));

            return true;
        }

        return false;
    }

    public int getCount(String key) {
        String count = redisTemplate.opsForValue().get(key);

        return count == null ? 0 : Integer.parseInt(count);
    }
}
