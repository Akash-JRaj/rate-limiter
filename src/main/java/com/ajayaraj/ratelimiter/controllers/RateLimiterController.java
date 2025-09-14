package com.ajayaraj.ratelimiter.controllers;

import com.ajayaraj.ratelimiter.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {

    @GetMapping("/check")
    public ResponseEntity<Response> check(@RequestParam(required = true) String clientId) {
        Response response = new Response();
        response.setAllowed(true);
        System.out.println(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
