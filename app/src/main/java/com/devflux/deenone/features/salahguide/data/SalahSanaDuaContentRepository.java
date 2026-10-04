package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: সানা দোয়া (Dua Sana).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahSanaDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. সানা দোয়া (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "সানা দোয়া",
            "Dua Sana (Opening Supplication)",
            "سُبْحَانَكَ ٱللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ ٱسْمُكَ، وَتَعَالَىٰ جَدُّكَ، وَلَا إِلَٰهَ غَيْرُكَ উচ্চারণ: সুবহানাকা আল্লা-হুম্মা ওয়া বি-হামদিকা, ওয়া তাবারা-কাসমুকা, ওয়া তা আলা জাদ্দুকা ওয়া-লা ইলাহা গাইরুকা। অর্থ: হে আল্লাহ! আমি তোমার পবিত্রতা বর্ণনা করছি। তুমি প্রশংসাময়, তোমার ...",
            "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَى جَدُّكَ، وَلَا إِلَهَ غَيْرُكَ Transliteration: Subhanaka Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruka. Meaning: Glory be to You, O Allah, and all praise is due to You...",
            "سُبْحَانَكَ ٱللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ ٱسْمُكَ، وَتَعَالَىٰ جَدُّكَ، وَلَا إِلَٰهَ غَيْرُكَ\n\n" +
            "উচ্চারণ:\n\n" +
            "সুবহানাকা আল্লা-হুম্মা ওয়া বি-হামদিকা, ওয়া তাবারা-কাসমুকা, ওয়া তা আলা জাদ্দুকা ওয়া-লা ইলাহা গাইরুকা।\n\n" +
            "অর্থ:\n\n" +
            "হে আল্লাহ! আমি তোমার পবিত্রতা বর্ণনা করছি। তুমি প্রশংসাময়, তোমার নাম বরকতময়, তোমার মর্যাদা অতি উচ্চে, আর তুমি ব্যতীত সত্যিকার কোনো মাবুদ নেই।”\n\n" +
            "[আবু দাউদ, তিরমিজি, মিশকাত]",
            "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَى جَدُّكَ، وَلَا إِلَهَ غَيْرُكَ\n\n" +
            "Transliteration:\n\n" +
            "Subhanaka Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruka.\n\n" +
            "Meaning:\n\n" +
            "\"Glory be to You, O Allah, and all praise is due to You, blessed is Your Name, exalted is Your Majesty, and there is no true deity worthy of worship besides You.\"\n\n" +
            "[Abu Dawud, Tirmidhi, Mishkat]"
        ));

        return list;
    }
}
