package com.devflux.deenone.core.security;

import java.security.MessageDigest;

public class AdminAuthGuard {

    // Default hashed PIN verification (e.g. SHA-256 for admin actions)
    public static boolean verifyAdminPin(String enteredPin, String salt, String expectedHash) {
        if (enteredPin == null || expectedHash == null) return false;
        try {
            String combined = enteredPin + (salt != null ? salt : "");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combined.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().equalsIgnoreCase(expectedHash);
        } catch (Exception e) {
            return false;
        }
    }
}
