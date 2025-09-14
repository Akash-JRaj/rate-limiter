package com.ajayaraj.ratelimiter.service;

public interface RateLimiter {
    boolean isAllowed(String clientId);
}
