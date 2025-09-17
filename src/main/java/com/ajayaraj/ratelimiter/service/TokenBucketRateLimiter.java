package com.ajayaraj.ratelimiter.service;

import com.ajayaraj.ratelimiter.exception.RateLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenBucketRateLimiter implements RateLimiter{

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final int CAPACITY = 5;
    private final int REFILL_RATE = 10;
    private final int REFILL_INTERVAL = 10; // seconds

    @Override
    public boolean isAllowed(String clientId) {
        String keyToken = "bucket:token:" + clientId;
        String keyTimeStamp = "bucket:timestamp:" + clientId;

        long now = Instant.now().getEpochSecond();

        String tokenStr = redisTemplate.opsForValue().get(keyToken);
        String timeStampStr = redisTemplate.opsForValue().get(keyTimeStamp);

        int tokens = tokenStr == null ? CAPACITY : Integer.parseInt(tokenStr);
        long lastRefill = timeStampStr == null ? now : Long.parseLong(timeStampStr);

        long elapsed = now - lastRefill;
        long intervals = elapsed / REFILL_INTERVAL;

        if(intervals > 0) {
            int refill = (int) (intervals * REFILL_RATE);
            tokens = Math.min(CAPACITY, tokens + refill);
            lastRefill += (intervals * REFILL_INTERVAL);
        }

        redisTemplate.opsForValue().set(keyTimeStamp, String.valueOf(lastRefill));

        if(tokens > 0) {
            redisTemplate.opsForValue().set(keyToken, String.valueOf(tokens - 1));
            return true;
        }
        else {
            redisTemplate.opsForValue().set(keyToken, String.valueOf(tokens));
            throw new RateLimitExceededException("Too many requests!");
        }
    }
}
