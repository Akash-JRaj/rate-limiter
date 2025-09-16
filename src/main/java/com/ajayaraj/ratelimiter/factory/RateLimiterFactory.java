package com.ajayaraj.ratelimiter.factory;

import com.ajayaraj.ratelimiter.config.RateLimiterStrategy;
import com.ajayaraj.ratelimiter.service.FixedWindowRateLimiter;
import com.ajayaraj.ratelimiter.service.RateLimiter;
import com.ajayaraj.ratelimiter.service.SlidingWindowCounterRateLimiter;
import com.ajayaraj.ratelimiter.service.SlidingWindowLogRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterFactory {

    private final FixedWindowRateLimiter fixedWindowRateLimiter;
    private final SlidingWindowLogRateLimiter slidingWindowLogRateLimiter;
    private final SlidingWindowCounterRateLimiter slidingWindowCounterRateLimiter;
    private final RateLimiterStrategy rateLimiterStrategy;

    public RateLimiterFactory(FixedWindowRateLimiter fixedWindowRateLimiter,
                              SlidingWindowCounterRateLimiter slidingWindowCounterRateLimiter,
                              SlidingWindowLogRateLimiter slidingWindowLogRateLimiter,
                              @Value("${ratelimiter.strategy}") String strategy) {
        this.fixedWindowRateLimiter = fixedWindowRateLimiter;
        this.slidingWindowLogRateLimiter = slidingWindowLogRateLimiter;
        this.slidingWindowCounterRateLimiter = slidingWindowCounterRateLimiter;
        this.rateLimiterStrategy = RateLimiterStrategy.valueOf(strategy.toUpperCase());
    }

    public RateLimiter getRateLimiter() {
        return switch (rateLimiterStrategy) {
            case FIXED -> fixedWindowRateLimiter;
            case SLIDING_COUNTER -> slidingWindowCounterRateLimiter;
            case SLIDING_LOG -> slidingWindowLogRateLimiter;
            case TOKEN_BUCKET -> null;
        };
    }

}
