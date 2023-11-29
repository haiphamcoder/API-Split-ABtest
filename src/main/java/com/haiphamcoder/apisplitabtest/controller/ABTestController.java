package com.haiphamcoder.apisplitabtest.controller;

import com.haiphamcoder.apisplitabtest.service.ABTestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/ab_test")
public class ABTestController {
    private final ABTestService abTestService;

    private final SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(200,
            Duration.ofSeconds(1));

    public ABTestController(ABTestService abTestService) {
        this.abTestService = abTestService;
    }

    @GetMapping("/get_variant/{userId}")
    public ResponseEntity<String> getVariant(@PathVariable("userId") String userId) {
        if (!rateLimiter.allowRequest()) {
            return ResponseEntity.status(429).body("Too Many Requests");
        }
        return ResponseEntity.ok(abTestService.assignVariant(userId));
    }
}
