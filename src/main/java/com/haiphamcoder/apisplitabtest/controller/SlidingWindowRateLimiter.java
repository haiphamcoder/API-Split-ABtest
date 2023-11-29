package com.haiphamcoder.apisplitabtest.controller;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowRateLimiter {
    private final int maxRequests;
    private final AtomicInteger[] slots;
    private volatile int indexTheNextOfExpiredSlot;
    private volatile int index;

    public SlidingWindowRateLimiter(int maxRequests, Duration duration) {
        this.maxRequests = maxRequests;
        int sizeOfSlots = (int) (duration.toMillis() / 100);
        this.slots = new AtomicInteger[sizeOfSlots];
        for (int i = 0; i < sizeOfSlots; i++) {
            slots[i] = new AtomicInteger(0);
        }
        this.indexTheNextOfExpiredSlot = 0;
        this.index = 0;
    }

    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        int currentSlot = (int) ((currentTime / 100) % slots.length);

        // Remove expired slots
        if (currentSlot != indexTheNextOfExpiredSlot) {
            resetSlot(currentSlot);
            indexTheNextOfExpiredSlot = currentSlot;
        }

        int count = 0;
        for (AtomicInteger slot : slots) {
            count += slot.get();
        }

        if (count < maxRequests) {
            slots[index].incrementAndGet();
            index = (index + 1) % slots.length;
            return true;
        }
        return false;
    }

    public void resetSlot(int index) {
        slots[index].set(0);
    }
}
