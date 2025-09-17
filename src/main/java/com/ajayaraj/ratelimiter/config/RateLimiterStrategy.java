package com.ajayaraj.ratelimiter.config;

public enum RateLimiterStrategy {
    FIXED,
    SLIDING_LOG,
    SLIDING_COUNTER,
    TOKEN_BUCKET,
    LEAKY_BUCKET
}
