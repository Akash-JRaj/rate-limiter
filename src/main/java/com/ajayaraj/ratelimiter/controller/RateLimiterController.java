package com.ajayaraj.ratelimiter.controller;

import com.ajayaraj.ratelimiter.factory.RateLimiterFactory;
import com.ajayaraj.ratelimiter.models.Response;
import com.ajayaraj.ratelimiter.service.FixedWindowRateLimiter;
import com.ajayaraj.ratelimiter.service.RateLimiter;
import com.ajayaraj.ratelimiter.service.SlidingWindowCounterRateLimiter;
import com.ajayaraj.ratelimiter.service.SlidingWindowLogRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {

    @Autowired
    private RateLimiterFactory rateLimiterFactory;

    @GetMapping("/check")
    public ResponseEntity<Response> check(@RequestParam(required = true) String clientId) {
        Response response = new Response();
        response.setAllowed(rateLimiterFactory.getRateLimiter().isAllowed(clientId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
