package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আর স্থান (Location for Jumu'ah Prayer).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and Classical Fiqh references.
 */
public class JumuahPrayerLocationContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আর স্থান (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আর স্থান",
            "Location for Jumu'ah Prayer",
            "জুমুআহ যেমন শহরবাসীর জন্য ফরয, তেমনি ফরয গ্রামবাসীর জন্যও। এর জন্য খলীফা হওয়া, শহর হওয়া, জামে মসজিদ হওয়া বা ৪০ জন নামাযী হওয়া শর্ত নয়। বরং যেখানেই স্থানীয় স্থায়ী বসবাসকারী জামাআত পাওয়া যাবে, সেখানেই জুমুআহ ফরয। [মাজাল্লা...",
            "Friday prayer is obligatory upon villagers just as it is obligatory upon city dwellers. Having a Caliph, being a major city, having a grand Jame Mosque, or requiring 40 worshippers is not an essential prerequisite...",
            "জুমুআহ যেমন শহরবাসীর জন্য ফরয, তেমনি ফরয গ্রামবাসীর জন্যও। এর জন্য খলীফা হওয়া, শহর হওয়া, জামে মসজিদ হওয়া বা ৪০ জন নামাযী হওয়া শর্ত নয়। বরং যেখানেই স্থানীয় স্থায়ী বসবাসকারী জামাআত পাওয়া যাবে, সেখানেই জুমুআহ ফরয।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ২২/৭৫, ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪২৪]\n\n" +
            "হযরত ইবনে আব্বাস (রাঃ) বলেন, ‘নবী (ﷺ)-এর মসজিদে জুমুআহ প্রতিষ্ঠিত হওয়ার পর ইসলামে সর্বপ্রথম যে জুমুআহ প্রতিষ্ঠিত হয়, তা হল বাহ্রাইনের জুয়াষা নামক এক গ্রামে।’\n\n" +
            "[বুখারী ৮৯২, ৪৩৭১, আবূদাঊদ, সুনান ১০৬৮নং]\n\n" +
            "হযরত ইবনে উমার (রাঃ) মক্কা মুকার্রামা ও মদ্বীনা নববিয়ার মধ্যবর্তী পথে অবস্থিত ছোট ছোট জনপদে জুমুআহ প্রতিষ্ঠিত হতে লক্ষ্য করতেন। তিনি তাতে কোন আপত্তি জানাতেন না। (আব্দুর রাযযাক, মুসান্নাফ)\n\n" +
            "হযরত উমার (রাঃ) বলেন, ‘তোমরা যেখানেই থাক, সেখানেই জুমুআহ পড়।’\n\n" +
            "[আব্দুর রাযযাক, মুসান্নাফ, তামামুল মিন্নাহ্, আলবানী ৩৩২পৃ:]\n\n" +
            "পক্ষান্তরে পাড়া-গ্রামে জুমুআহ হবে না বলে হযরত আলী কর্তৃক যে হাদীস বর্ণনা করা হয়, তা সহীহ নয়।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৬/৩৫২-৩৫৪, ২২/৭৫]\n\n" +
            "কোন অমুসলিম দেশে পড়াশোনা বা চাকরী করতে গিয়ে সেখানে মসজিদ না থাকলে বা যথেষ্ট সংখ্যক মুসলিম না থাকলেও ৩ জনেই যে কোন রুমে জুমুআহ কায়েম হবে।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৫/৮৫]\n\n" +
            "একই বড় গ্রাম বা শহরে বিচ্ছন্নতা সৃষ্টির উদ্দেশ্যে নয়, বরং মসজিদ সংকীর্ণ হওয়ার কারণে, অথবা দূর হওয়ার কারণে, অথবা ফিতনা সৃষ্টি হওয়ার আশঙ্কায় প্রয়োজনে একাধিক মসজিদে জুমুআহ কায়েম করা যায়।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৮/১১১, ১৯/১৬৫-১৬৬]\n\n" +
            "কোন মসজিদে জুমুআহ পড়ার জন্য নির্মাণের সময় ঐ নিয়ত শর্ত নয়। অক্তিযারুপে নির্মাণের পর প্রয়োজনে সেখানে জুমুআহ পড়া যায়।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৮/১১০]",
            "Friday (Jumu'ah) prayer is obligatory upon villagers just as it is obligatory upon city dwellers. Having a Caliph, being a major city, having a grand Jame Mosque, or requiring 40 worshippers is not an essential prerequisite. Rather, wherever a permanent resident Muslim community is present, Jumu'ah is obligatory upon them.\n\n" +
            "[Majallatul Buhuthil Islamiyyah 22/75; Fatawa Islamiyyah, Saudi Scholars Committee 1/424]\n\n" +
            "Narrated by Ibn Abbas (RA): 'The first Friday prayer offered in Islam after the Friday prayer in the Prophet's Mosque (in Madinah) was held at the village of Juwatha in Bahrain.'\n\n" +
            "[Bukhari #892, 4371; Abu Dawud, Sunan #1068]\n\n" +
            "Ibn Umar (RA) used to observe Friday prayers established in small habitations situated along the route between Makkah and Madinah, and he did not raise any objection against it. (Abdur Razzaq, Musannaf)\n\n" +
            "Umar (RA) stated: 'Wherever you are, establish the Friday prayer.'\n\n" +
            "[Abdur Razzaq, Musannaf; Tamam al-Minnah, Albani p. 332]\n\n" +
            "Conversely, the narration attributed to Ali (RA) claiming that Friday prayer cannot be held in villages and rural outskirts is not authentic (Da'if / Ghayr Sahih).\n\n" +
            "[Majallatul Buhuthil Islamiyyah 16/352-354, 22/75]\n\n" +
            "If one travels to a non-Muslim country for study or employment and there is no mosque or large Muslim gathering available, holding Jumu'ah with even three individuals in any room is valid and established.\n\n" +
            "[Majallatul Buhuthil Islamiyyah 15/85]\n\n" +
            "In a large village or city, establishing Friday prayer in multiple mosques is permissible out of necessity—not for causing division, but due to mosque congestion, excessive distance, or fear of civil discord (Fitnah).\n\n" +
            "[Majallatul Buhuthil Islamiyyah 18/111, 19/165-166]\n\n" +
            "Having the intention to establish Jumu'ah at the initial construction of a mosque is not a condition. A regular prayer area built for daily prayers may subsequently be used for holding Jumu'ah whenever the need arises.\n\n" +
            "[Majallatul Buhuthil Islamiyyah 18/110]"
        ));

        return list;
    }
}
