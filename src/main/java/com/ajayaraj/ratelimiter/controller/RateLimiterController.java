package com.ajayaraj.ratelimiter.controller;

import com.ajayaraj.ratelimiter.models.Response;
import com.ajayaraj.ratelimiter.service.FixedWindowRateLimiter;
import com.ajayaraj.ratelimiter.service.RateLimiter;
import com.ajayaraj.ratelimiter.service.SlidingWindowCounterRateLimiter;
import com.ajayaraj.ratelimiter.service.SlidingWindowLogRateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {

    private RateLimiter rateLimiter;

    public RateLimiterController(SlidingWindowCounterRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @GetMapping("/check")
    public ResponseEntity<Response> check(@RequestParam(required = true) String clientId) {
        Response response = new Response();
        response.setAllowed(rateLimiter.isAllowed(clientId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
