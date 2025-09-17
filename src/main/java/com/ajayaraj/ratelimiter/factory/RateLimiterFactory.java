package com.ajayaraj.ratelimiter.factory;

import com.ajayaraj.ratelimiter.config.RateLimiterStrategy;
import com.ajayaraj.ratelimiter.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterFactory {

    private final FixedWindowRateLimiter fixedWindowRateLimiter;
    private final SlidingWindowLogRateLimiter slidingWindowLogRateLimiter;
    private final SlidingWindowCounterRateLimiter slidingWindowCounterRateLimiter;
    private final TokenBucketRateLimiter tokenBucketRateLimiter;
    private final LeakyBucketRateLimiter leakyBucketRateLimiter;
    private final RateLimiterStrategy rateLimiterStrategy;

    public RateLimiterFactory(FixedWindowRateLimiter fixedWindowRateLimiter,
                              SlidingWindowCounterRateLimiter slidingWindowCounterRateLimiter,
                              SlidingWindowLogRateLimiter slidingWindowLogRateLimiter,
                              TokenBucketRateLimiter tokenBucketRateLimiter,
                              LeakyBucketRateLimiter leakyBucketRateLimiter,
                              @Value("${ratelimiter.strategy}") String strategy) {
        this.fixedWindowRateLimiter = fixedWindowRateLimiter;
        this.slidingWindowLogRateLimiter = slidingWindowLogRateLimiter;
        this.slidingWindowCounterRateLimiter = slidingWindowCounterRateLimiter;
        this.tokenBucketRateLimiter = tokenBucketRateLimiter;
        this.leakyBucketRateLimiter = leakyBucketRateLimiter;
        this.rateLimiterStrategy = RateLimiterStrategy.valueOf(strategy.toUpperCase());
    }

    public RateLimiter getRateLimiter() {
        return switch (rateLimiterStrategy) {
            case FIXED -> fixedWindowRateLimiter;
            case SLIDING_COUNTER -> slidingWindowCounterRateLimiter;
            case SLIDING_LOG -> slidingWindowLogRateLimiter;
            case TOKEN_BUCKET -> tokenBucketRateLimiter;
            case LEAKY_BUCKET -> leakyBucketRateLimiter;
        };
    }

}
