package com.ajayaraj.ratelimiter.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlidingWindowCounterRateLimiterTest {

    private SlidingWindowCounterRateLimiter rateLimiter;
    private StringRedisTemplate redisTemplate;
    private ValueOperations valueOperations;

    @BeforeEach
    void setup() {
        redisTemplate = Mockito.mock(StringRedisTemplate.class);
        valueOperations = Mockito.mock(ValueOperations.class);

        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        rateLimiter = new SlidingWindowCounterRateLimiter();
    }

    @Test
    void testConcurrentRequests() {
        String client = "ags";

        Mockito.when(valueOperations.get(Mockito.anyString())).thenReturn(null);

        int totalReq = 20;
        int allowed = 0;
        for(int i = 0; i < totalReq; i++) {
            if(rateLimiter.isAllowed(client)) {
                allowed++;
            }
        }

        assertTrue(allowed <= 10, "More request were allowed");
    }

}
