package com.devflux.deenone.core.halal;

/**
 * Legacy translation helper. 
 * Note: All Halal Food items and translations are now dynamically managed via PHP Admin & MySQL backend.
 */
public class HalalFoodTranslationRegistry {

    public static String cleanTitle(String rawTitle, boolean isBn) {
        return HalalFoodItem.cleanTitle(rawTitle, isBn);
    }
}
