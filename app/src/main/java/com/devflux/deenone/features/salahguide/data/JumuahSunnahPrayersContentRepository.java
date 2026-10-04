package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আর আগে ও পরে সুন্নত (Sunnah Before & After Jumu'ah).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and Fiqh references.
 */
public class JumuahSunnahPrayersContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আর আগে ও পরে সুন্নত (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আর আগে ও পরে সুন্নত",
            "Sunnah Before & After Jumu'ah",
            "জুমআর খুতবার পূর্বে ‘কাবলাল জুমুআহ’ বলে কোন নির্দিষ্ট রাকআত সুন্নত নেই। অতএব নামাযী মসজিদে এলে ‘তাহিয়্যাতুল মাসজিদ’ ২ রাকআত সুন্নত পড়ে বসে যেতে পারে এবং দুআ, দরুদ তাসবীহ- যিকর বা তেলাওয়াত করতে পারে। আবার ইচ্ছা হলে নামাযও পড়তে পারে। তবে এ নামায হবে নফল এবং অনির্দিষ্ট সংখ্যায়...",
            "There is no fixed, designated number of Rak'ahs prescribed as 'Qablal Jumu'ah' Sunnah prior to the Friday sermon. Therefore, upon entering the mosque, the worshipper may offer 2 Rak'ahs of Tahiyyatul Masjid, sit down, and engage in supplication, sending Durood, Dhikr, or reciting the Holy Qur'an...",
            "জুমআর খুতবার পূর্বে ‘কাবলাল জুমুআহ’ বলে কোন নির্দিষ্ট রাকআত সুন্নত নেই। অতএব নামাযী মসজিদে এলে ‘তাহিয়্যাতুল মাসজিদ’ ২ রাকআত সুন্নত পড়ে বসে যেতে পারে এবং দুআ, দরুদ তাসবীহ- যিকর বা তেলাওয়াত করতে পারে। আবার ইচ্ছা হলে নামাযও পড়তে পারে। তবে এ নামায হবে নফল এবং অনির্দিষ্ট সংখ্যায়।\n\n" +
            "মহানবী (ﷺ) বলেন,\n" +
            "যে ব্যক্তি জুমআর দিন যথা নিয়মে গোসল করে, দাঁত পরিষ্কার করে, খোশবূ থাকলে তা ব্যবহার করে, তার সবচেয়ে সুন্দর পোশাক পরে, অতঃপর (মসজিদে) যায়, নামাযীদের ঘাড় ডিঙিয়ে (কাতার চিরে) আগে যায় না, অতঃপর আল্লাহর ইচ্ছানুযায়ী নামায পড়ে। তারপর ইমাম উপস্থিত হলে নীরব ও নিশ্চুপ থাকে এবং নামায শেষ না হওয়া পর্যন্ত কোন কথা বলে না, সে ব্যক্তির এ কাজ এই জুমুআহ থেকে অপর জুমআর মধ্যবর্তীকালে কৃত পাপের কাফফারা হয়ে যায়।\n\n" +
            "[আহমাদ, মুসনাদ, ইবনে মাজাহ্, সুনান,হাকেম, মুস্তাদরাক, জামে ৬০৬৬নং]\n\n" +
            "প্রকাশ থাকে যে, প্রত্যেক আযান ও ইকামতের মাঝে নামায আছে।\n\n" +
            "[বুখারী, মুসলিম, মিশকাত ৬৬২নং]\n\n" +
            "এই হাদীস দ্বারা কাবলাল জুমআর সুন্নত প্রমাণ হয় না। কারণ, বিদিত যে, জুমআর আযান ও ইকামতের মাঝে থাকে খুতবা। আর মহানবী (ﷺ)-এর যুগে পূর্বের আর একটি আযান ছিল না। আর সুন্নত প্রমাণ হলেও মুআক্কাদাহ ও নির্দিষ্ট সংখ্যক নয়। তদনুরুপ “এমন কোন ফরয নামায নেই, যার পূর্বে ২ রাকআত নামায নেই।\n\n" +
            "[ইবনে হিব্বান, সহীহ, ত্বাবারানীরানী, মু’জাম, সিলসিলাহ সহীহাহ, আলবানী ২৩২, জামে ৫৭৩০নং]\n\n" +
            "এ হাদীস দ্বারাও জুমআর পূর্বে ২ রাকআত সুন্নত প্রমাণ হয় না। কারণ, জুমআর ফরয নামাযের পূর্বে খুতবা হয়। আর খুতবার পূর্বে ২ রাকআত নামায এ দ্বারা প্রমাণিত হয় না।\n\n" +
            "[সিলসিলাহ সহীহাহ, আলবানী ২৩২নং]",
            "There is no fixed, designated number of Rak'ahs prescribed as 'Qablal Jumu'ah' Sunnah prior to the Friday sermon. Therefore, upon entering the mosque, the worshipper may offer 2 Rak'ahs of Tahiyyatul Masjid, sit down, and engage in supplication, sending Durood, Dhikr, or reciting the Holy Qur'an. Alternatively, one may offer voluntary (Nafl) prayers as desired, though these voluntary prayers are unrestricted in number.\n\n" +
            "The Holy Prophet (ﷺ) said:\n" +
            "\"Whoever takes a bath on Friday according to the prescribed rules, cleans their teeth, applies perfume if available, wears their best clothes, then proceeds to the mosque without stepping over the shoulders of the worshippers (or breaking rows), then prays whatever Allah wills for them to pray; and when the Imam appears, remains completely silent and listens attentively until the prayer finishes—this act will serve as an expiation for the sins committed between that Friday and the next.\"\n\n" +
            "[Ahmad, Musnad; Ibn Majah, Sunan; Al-Hakim, Mustadrak; Jami #6066]\n\n" +
            "It is worth noting that: \"Between every two calls (Adhan and Iqamah) there is a prayer.\"\n\n" +
            "[Bukhari, Muslim, Mishkat #662]\n\n" +
            "This Hadith does not establish a specific Sunnah prayer before Jumu'ah (Qablal Jumu'ah). Because, as is well known, the Khutbah takes place between the Friday Adhan and Iqamah. Furthermore, during the era of the Holy Prophet (ﷺ), there was no earlier additional Adhan. Even if any Sunnah were inferred, it is not an emphasized Sunnah (Mu'akkadah) nor is it a fixed number of Rak'ahs. Similarly, the narration: \"There is no obligatory prayer except that before it there are two Rak'ahs of prayer.\"\n\n" +
            "[Ibn Hibban, Sahih; Tabarani, Mu'jam; Silsilah Sahihah, Albani #232; Jami #5730]\n\n" +
            "This Hadith also does not establish a two-Rak'ah Sunnah before Jumu'ah. Because the Khutbah precedes the obligatory Jumu'ah prayer, and offering a two-Rak'ah prayer prior to the Khutbah is not proven by this narration.\n\n" +
            "[Silsilah Sahihah, Albani #232]"
        ));

        // 2. জুম'আর পর সুন্নাতে রাতেবাহ
        list.add(new HajjHistoryCardItem(
            2,
            "জুম'আর পর সুন্নাতে রাতেবাহ",
            "Sunnah Ratibah After Jumu'ah",
            "জুমআর পর সুন্নাতে রাতেবাহ রয়েছে। আর তা হলো মসজিদে ৪ রাকআত পড়া অথবা ঘরে ২ রাকআত পড়া। মহানবী (ﷺ) বলেন, 'তোমাদের কেউ যখন জুমআহ পড়বে, তখন সে যেন তার পর ৪ রাকআত (সুন্নত) পড়ে।' [সহীহ মুসলিম ৮৮১]",
            "After the Friday obligatory prayer, there is an established Sunnah: offering 4 Rak'ahs in the mosque or 2 Rak'ahs at home...",
            "জুমআর পর সুন্নাতে রাতেবাহ রয়েছে। আর তা হলো মসজিদে ৪ রাকআত পড়া অথবা ঘরে ২ রাকআত পড়া। মহানবী (ﷺ) বলেন, 'তোমাদের কেউ যখন জুমআহ পড়বে, তখন সে যেন তার পর ৪ রাকআত (সুন্নত) পড়ে।' [সহীহ মুসলিম ৮৮১]\n\n" +
            "ইবনে উমর (রাঃ) থেকে বর্ণিত, 'নবী (ﷺ) জুমআর পর (মসজিদে কোনো নামায পড়তেন না, বরং) তাঁর ঘরে ফিরে গিয়ে ২ রাকআত নামায পড়তেন।' [সহীহ বুখারী ৯৩৭, সহীহ মুসলিম ৮৮২]\n\n" +
            "সুতরাং উত্তম হলো—মসজিদে পড়লে ৪ রাকআত পড়া এবং ঘরে গিয়ে পড়লে ২ রাকআত পড়া।\n\n" +
            "[যাদুল মাআদ ১/৪২৫, মাজমূউ ফাতাওয়া ইবনে তাইমিয়্যাহ ২৪/১৮৭]",
            "After the Friday obligatory prayer, there is an established Sunnah (Sunnah Ratibah): either offering 4 Rak'ahs in the mosque or 2 Rak'ahs at home. The Prophet (ﷺ) said: 'When any of you prays Jumu'ah, let him pray 4 Rak'ahs after it.' [Sahih Muslim 881]\n\n" +
            "Narrated by Ibn Umar (RA): 'The Prophet (ﷺ) would not pray after Jumu'ah in the mosque, but would return to his house and pray 2 Rak'ahs.' [Sahih Bukhari 937, Sahih Muslim 882]\n\n" +
            "Thus, the superior practice is: if prayed in the mosque, offer 4 Rak'ahs; and if prayed at home, offer 2 Rak'ahs.\n\n" +
            "[Zad al-Ma'ad 1/425, Majmu' al-Fatawa 24/187]"
        ));

        // 3. ফরয ও সুন্নতের মাঝে ব্যবধান করা
        list.add(new HajjHistoryCardItem(
            3,
            "ফরয ও সুন্নতের মাঝে ব্যবধান করা",
            "Separating Obligatory and Sunnah Prayers",
            "জুম'আর ফরজ নামায সমাপ্ত হওয়ার সাথে সাথে কথা না বলে বা স্থান পরিবর্তন না করে সুন্নতে দাঁড়িয়ে যাওয়া নিষেধ।",
            "Standing immediately for Sunnah prayers after Jumu'ah Fardh without speaking or changing spot is prohibited.",
            "জুমআর ফরয নামায পড়ার পর কোনো কথা না বলে অথবা স্থান পরিবর্তন না করে সাথে সাথে সুন্নতের জন্য দাঁড়িয়ে যাওয়া অনুচিত।\n\n" +
            "সায়েব বিন ইয়াযীদ (রাঃ) বলেন, হযরত মুআবিয়া (রাঃ) আমাকে বললেন: 'তুমি যখন জুমআহ পড়বে, তখন যতক্ষণ না কথা বলবে অথবা (স্থান ত্যাগ করে) বের হবে ততক্ষণ তার সাথে অপর কোনো নামায মিলাবে না। কেননা আল্লাহর রসূল (ﷺ) আমাদেরকে এ নির্দেশ দিয়েছেন যে, আমরা যেন কোনো নামাযের সাথে অপর নামায না মিলাই যে পর্যন্ত না কথা বলি অথবা স্থান পরিবর্তন করি।'\n\n" +
            "[সহীহ মুসলিম ৭১২]",
            "It is improper to stand up immediately for Sunnah prayers following the obligatory Jumu'ah prayer without speaking or shifting one's place.\n\n" +
            "As-Sa'ib ibn Yazid (RA) narrated that Mu'awiyah (RA) said to him: 'When you have offered the Friday prayer, do not connect it with another prayer until you have spoken or left the place, for the Messenger of Allah (ﷺ) commanded us not to join one prayer with another unless we speak or leave.'\n\n" +
            "[Sahih Muslim 712]"
        ));

        return list;
    }
}
