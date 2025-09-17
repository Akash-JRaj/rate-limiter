package com.ajayaraj.ratelimiter.service;

import com.ajayaraj.ratelimiter.exception.RateLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LeakyBucketRateLimiter implements RateLimiter{

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int CAPACITY = 10;
    private static final int LEAK_RATE = 1; // req / sec

    @Override
    public boolean isAllowed(String clientId) {
        String keyWater = "leaky:water:" + clientId;
        String keyTimeStamp = "leaky:timestamp:" + clientId;

        long now = Instant.now().getEpochSecond();

        String waterStr = redisTemplate.opsForValue().get(keyWater);
        String tsStr = redisTemplate.opsForValue().get(keyTimeStamp);

        int waters = waterStr == null ? 0 : Integer.parseInt(waterStr);
        long lastLeak = tsStr == null ? now : Long.parseLong(tsStr);

        long elapsed = now - lastLeak;
        int leaked = (int) (elapsed * LEAK_RATE);
        waters = Math.max(0, waters - leaked);

        if(waters < CAPACITY) {
            redisTemplate.opsForValue().set(keyTimeStamp, String.valueOf(now));
            redisTemplate.opsForValue().set(keyWater, String.valueOf(waters + 1));

            return true;
        }
        else {
            redisTemplate.opsForValue().set(keyTimeStamp, String.valueOf(now));
            redisTemplate.opsForValue().set(keyWater, String.valueOf(waters));

            throw new RateLimitExceededException("Too many requests!");
        }
    }
}
