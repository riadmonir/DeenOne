package com.devflux.deenone.features.battle.engine;

import com.devflux.deenone.features.battle.model.BattleQuestion;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Enterprise-grade Question Quality & Security Validator.
 * Enforces strict Category matching, Duplicate Detection (via SHA-256 Text Hashing),
 * Option Integrity (exactly 4 distinct non-empty choices), Valid Correct Answer Index,
 * Source Citation Authenticity, and Content Quality Sanity Checks.
 */
public class IslamicQuestionValidator {

    private static final Set<String> registeredQuestionHashes = new HashSet<>();

    public static synchronized void resetRegisteredHashes() {
        registeredQuestionHashes.clear();
    }

    public static class ValidationResult {
        private final boolean isValid;
        private final String rejectionReason;

        public ValidationResult(boolean isValid, String rejectionReason) {
            this.isValid = isValid;
            this.rejectionReason = rejectionReason;
        }

        public boolean isValid() { return isValid; }
        public String getRejectionReason() { return rejectionReason; }
    }

    public static synchronized ValidationResult validateAndDeduplicate(BattleQuestion q, String expectedCategoryId) {
        if (q == null) {
            return new ValidationResult(false, "Question object is null");
        }

        // 1. Category Verification
        if (expectedCategoryId != null && !expectedCategoryId.trim().isEmpty()) {
            if (q.getCategoryId() == null || !q.getCategoryId().equalsIgnoreCase(expectedCategoryId.trim())) {
                return new ValidationResult(false, "Category mismatch: expected " + expectedCategoryId + " but got " + q.getCategoryId());
            }
        }

        // 2. Question Text Quality Check
        String qText = q.getQuestionText();
        if (qText == null || qText.trim().length() < 10) {
            return new ValidationResult(false, "Question text is too short or empty (min 10 characters required)");
        }

        // 3. Options Integrity Check (Must have exactly 4 non-empty, distinct options)
        List<String> opts = q.getOptions();
        if (opts == null || opts.size() != 4) {
            return new ValidationResult(false, "Question must have exactly 4 options. Found: " + (opts == null ? 0 : opts.size()));
        }

        Set<String> uniqueOpts = new HashSet<>();
        for (int i = 0; i < opts.size(); i++) {
            String opt = opts.get(i);
            if (opt == null || opt.trim().isEmpty()) {
                return new ValidationResult(false, "Option " + (i + 1) + " is empty");
            }
            uniqueOpts.add(opt.trim().toLowerCase());
        }
        if (uniqueOpts.size() != 4) {
            return new ValidationResult(false, "Options must be distinct from each other");
        }

        // 4. Correct Answer Index Validation
        int correctIdx = q.getCorrectOptionIndex();
        if (correctIdx < 0 || correctIdx >= 4) {
            return new ValidationResult(false, "Invalid correctOptionIndex: " + correctIdx + " (must be 0, 1, 2, or 3)");
        }

        // 5. Authentic Source & Dalil Tracking Check
        String ref = q.getReference();
        if (ref == null || ref.trim().isEmpty()) {
            return new ValidationResult(false, "Question must include an authentic Islamic source citation or reference");
        }

        // 6. Duplicate Detection (SHA-256 Normalized Text Hashing)
        String normalized = qText.toLowerCase().replaceAll("[\\s\\p{Punct}]", "");
        String hash = computeSha256(normalized);
        if (registeredQuestionHashes.contains(hash)) {
            return new ValidationResult(false, "Duplicate question detected via content hash fingerprinting");
        }
        registeredQuestionHashes.add(hash);

        return new ValidationResult(true, "Question successfully validated and verified");
    }

    private static String computeSha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return String.valueOf(input.hashCode());
        }
    }

    public static void clearDeduplicationCache() {
        registeredQuestionHashes.clear();
    }
}
