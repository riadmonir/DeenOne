package com.devflux.deenone.core.security;

import java.util.concurrent.ConcurrentHashMap;

public class ApiRateLimiter {

    private static final ConcurrentHashMap<String, Long> lastRequestTimestamps = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Integer> requestCounts = new ConcurrentHashMap<>();

    // Limit to max 10 requests per minute per endpoint key
    private static final int MAX_REQUESTS_PER_MINUTE = 15;
    private static final long WINDOW_MILLIS = 60000;

    public static synchronized boolean canProceed(String apiKeyOrEndpoint) {
        if (apiKeyOrEndpoint == null) return true;
        long now = System.currentTimeMillis();

        Long lastTime = lastRequestTimestamps.get(apiKeyOrEndpoint);
        Integer count = requestCounts.get(apiKeyOrEndpoint);

        if (lastTime == null || (now - lastTime > WINDOW_MILLIS)) {
            lastRequestTimestamps.put(apiKeyOrEndpoint, now);
            requestCounts.put(apiKeyOrEndpoint, 1);
            return true;
        }

        if (count != null && count < MAX_REQUESTS_PER_MINUTE) {
            requestCounts.put(apiKeyOrEndpoint, count + 1);
            return true;
        }

        return false;
    }
}
