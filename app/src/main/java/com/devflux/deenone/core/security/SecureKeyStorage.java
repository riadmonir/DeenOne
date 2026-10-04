package com.devflux.deenone.core.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SecureKeyStorage {

    private static final String PREF_SECURE = "deanone_encrypted_vault";
    private static final String KEY_OPENAI_API_KEY = "sec_openai_key";
    private static final String KEY_ADMIN_TOKEN = "sec_admin_token";
    private static final String KEY_APP_SALT = "DeenOne-Islamic-App-Secure-Salt-v2.1";

    public static void saveOpenAiApiKey(Context context, String apiKey) {
        if (context == null || apiKey == null) return;
        String obfuscated = obfuscate(apiKey);
        context.getSharedPreferences(PREF_SECURE, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_OPENAI_API_KEY, obfuscated)
                .apply();
    }

    public static String getOpenAiApiKey(Context context) {
        if (context == null) return "";
        String stored = context.getSharedPreferences(PREF_SECURE, Context.MODE_PRIVATE)
                .getString(KEY_OPENAI_API_KEY, "");
        if (stored.isEmpty()) return "";
        return deobfuscate(stored);
    }

    public static void saveAdminToken(Context context, String token) {
        if (context == null || token == null) return;
        String obfuscated = obfuscate(token);
        context.getSharedPreferences(PREF_SECURE, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_ADMIN_TOKEN, obfuscated)
                .apply();
    }

    public static String getAdminToken(Context context) {
        if (context == null) return "";
        String stored = context.getSharedPreferences(PREF_SECURE, Context.MODE_PRIVATE)
                .getString(KEY_ADMIN_TOKEN, "");
        if (stored.isEmpty()) return "";
        return deobfuscate(stored);
    }

    private static String obfuscate(String plainText) {
        try {
            byte[] saltBytes = KEY_APP_SALT.getBytes(StandardCharsets.UTF_8);
            byte[] textBytes = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] result = new byte[textBytes.length];
            for (int i = 0; i < textBytes.length; i++) {
                result[i] = (byte) (textBytes[i] ^ saltBytes[i % saltBytes.length]);
            }
            return Base64.encodeToString(result, Base64.NO_WRAP);
        } catch (Exception e) {
            return Base64.encodeToString(plainText.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        }
    }

    private static String deobfuscate(String encoded) {
        try {
            byte[] decoded = Base64.decode(encoded, Base64.NO_WRAP);
            byte[] saltBytes = KEY_APP_SALT.getBytes(StandardCharsets.UTF_8);
            byte[] result = new byte[decoded.length];
            for (int i = 0; i < decoded.length; i++) {
                result[i] = (byte) (decoded[i] ^ saltBytes[i % saltBytes.length]);
            }
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return new String(Base64.decode(encoded, Base64.NO_WRAP), StandardCharsets.UTF_8);
        }
    }
}
