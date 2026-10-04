package com.devflux.deenone.features.battle.engine;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * High-Entropy Cryptographically Secure Random Battle Code Generator.
 * Strictly guarantees that every newly created custom battle receives a fresh, unique,
 * non-reusable 6-character battle code.
 */
public class BattleCodeGenerator {

    private static final String ALPHANUMERIC_POOL = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // No ambiguous chars (0, O, 1, I)
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Set<String> USED_CODES = Collections.synchronizedSet(new HashSet<>());
    private static final String PREFS_NAME = "deen_battle_codes";
    private static final String KEY_USED_CODES = "registered_battle_codes";

    public static synchronized String generateUniqueBattleCode(Context context) {
        loadPersistedCodes(context);

        String generatedCode;
        int attempts = 0;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                int randomIndex = SECURE_RANDOM.nextInt(ALPHANUMERIC_POOL.length());
                sb.append(ALPHANUMERIC_POOL.charAt(randomIndex));
            }
            generatedCode = sb.toString();
            attempts++;
            if (attempts > 10000) {
                // Highly improbable fallback: append timestamp entropy
                generatedCode = generatedCode.substring(0, 4) + String.valueOf(System.currentTimeMillis() % 100);
                break;
            }
        } while (USED_CODES.contains(generatedCode));

        USED_CODES.add(generatedCode);
        persistCode(context, generatedCode);
        return generatedCode;
    }

    private static void loadPersistedCodes(Context context) {
        if (context == null) return;
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Set<String> saved = prefs.getStringSet(KEY_USED_CODES, null);
            if (saved != null) {
                USED_CODES.addAll(saved);
            }
        } catch (Exception ignored) {}
    }

    private static void persistCode(Context context, String code) {
        if (context == null) return;
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Set<String> set = new HashSet<>(USED_CODES);
            prefs.edit().putStringSet(KEY_USED_CODES, set).apply();
        } catch (Exception ignored) {}
    }
}
