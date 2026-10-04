package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: ঈদের দিন জুম'আ (When Eid Falls on Friday).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and Classical Fiqh references.
 */
public class JumuahEidDayContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. ঈদের দিন জুম'আ (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "ঈদের দিন জুম'আ",
            "When Eid Falls on Friday",
            "ঈদের দিন জুমুআহ পড়লে ইমাম জুমুআহ পড়বেন। সাধারণ মুসলিমদের জন্য এখতিয়ার থাকবে; তারা জুমুআহ পড়তেও পারে, নচেৎ যোহ্র পড়াও বৈধ। আর পূর্বে এ কথা উল্লেখ করা হয়েছে যে, বৃষ্টি-বন্যার কারণে মসজিদে উপস্থিত হতে না পারলে ঘরে যোহ্র প...",
            "When Eid falls on Friday, the Imam will lead the Jumu'ah prayer. The general Muslims have an option; they may attend Jumu'ah, or otherwise offering Dhuhr is permissible. And as mentioned previously, if unable to attend due to rain or floods...",
            "ঈদের দিন জুমুআহ পড়লে ইমাম জুমুআহ পড়বেন। সাধারণ মুসলিমদের জন্য এখতিয়ার থাকবে; তারা জুমুআহ পড়তেও পারে, নচেৎ যোহ্র পড়াও বৈধ।\n\n" +
            "আর পূর্বে এ কথা উল্লেখ করা হয়েছে যে, বৃষ্টি-বন্যার কারণে মসজিদে উপস্থিত হতে না পারলে ঘরে যোহ্র পড়ে নিতে হবে।",
            "When Eid falls on Friday, the Imam will lead the Jumu'ah prayer. The general Muslims have an option (allowance); they may attend and offer the Jumu'ah prayer, or otherwise it is also permissible for them to offer Dhuhr prayer instead.\n\n" +
            "And as mentioned previously, if one is unable to attend the mosque due to heavy rain or floods, they should offer Dhuhr prayer at home."
        ));

        return list;
    }
}
