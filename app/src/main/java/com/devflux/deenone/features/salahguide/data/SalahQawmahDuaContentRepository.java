package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: রুকু থেকে উঠার পর দোয়া (Dua after Rising from Ruku).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahQawmahDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. রুকু থেকে উঠার পর দোয়া (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "রুকু থেকে উঠার পর দোয়া",
            "Dua after Rising from Ruku",
            "اَللّهُمَّ رَبَّنَا لَكَ الْحَمْد উচ্চারণ: আল্লা-হুম্মা রাব্বানা লাকাল হামদ। অর্থ: হে আল্লাহ! যাবতীয় সকল প্রশংসা তোমারই। [বুখারি, মিশকাত, মুসলিম]",
            "اللَّهُمَّ رَبَّنَا لَكَ الْحَمْد Transliteration: Allahumma Rabbana lakal-hamd. Meaning: O Allah, our Lord! All praise is due to You. [Bukhari, Mishkat, Muslim]",
            "اَللّهُمَّ رَبَّنَا لَكَ الْحَمْد\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লা-হুম্মা রাব্বানা লাকাল হামদ।\n\n" +
            "অর্থ:\n\n" +
            "হে আল্লাহ! যাবতীয় সকল প্রশংসা তোমারই।\n\n" +
            "[বুখারি, মিশকাত, মুসলিম]",
            "اللَّهُمَّ رَبَّنَا لَكَ الْحَمْد\n\n" +
            "Transliteration:\n\n" +
            "Allahumma Rabbana lakal-hamd.\n\n" +
            "Meaning:\n\n" +
            "\"O Allah, our Lord! All praise is due to You.\"\n\n" +
            "[Bukhari, Mishkat, Muslim]"
        ));

        return list;
    }
}
