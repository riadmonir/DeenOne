package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আর দিনের ফযীলত ও বৈশিষ্ট্য (Virtues and Distinctions of Friday).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and Classical Fiqh references.
 */
public class JumuahVirtuesContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আর দিনের ফযীলত ও বৈশিষ্ট্য (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আর দিনের ফযীলত ও বৈশিষ্ট্য",
            "Virtues and Distinctions of Friday",
            "জুমুআহ অর্থাৎ জমায়েত বা সমাবেশ ও সম্মেলনের দিন। এটি মুসলিমদের সাপ্তাহিক ঈদ ও বিশেষ ইবাদতের দিন। মহানবী (ﷺ) বলেন, “এই দিন হল ঈদের দিন। আল্লাহ মুসলিমদের জন্য তা নির্বাচিত করেছেন। অতএব যে জুমআয় আসে, সে যেন গোসল করে এ...",
            "Jumu'ah means the day of gathering, congregation, and assembly. It is the weekly Eid and a special day of worship for Muslims. The Holy Prophet (ﷺ) said: \"This day is a day of Eid that Allah has designated for Muslims...",
            "জুমুআহ অর্থাৎ জমায়েত বা সমাবেশ ও সম্মেলনের দিন। এটি মুসলিমদের সাপ্তাহিক ঈদ ও বিশেষ ইবাদতের দিন। মহানবী (ﷺ) বলেন, “এই দিন হল ঈদের দিন। আল্লাহ মুসলিমদের জন্য তা নির্বাচিত করেছেন। অতএব যে জুমআয় আসে, সে যেন গোসল করে এবং খোশবূ থাকলে তা ব্যবহার করে। আর তোমরা দাঁতন করায় অভ্যাসী হও।\n\n" +
            "[ইবনে মাজাহ্, সুনান ১০৯৮নং]\n\n" +
            "জুমআর দিন সবার চাইতে শ্রেষ্ঠ দিন। এমনকি ঈদুল ফিতর ও আযহা থেকেও শ্রেষ্ঠ। এই দিনে আদমকে সৃষ্টি করা হয়েছে এবং বেহেশ্ত দান করা হয়েছে। মহানবী (ﷺ) বলেন,\n" +
            "যার উপর সূর্য উদিত হয়েছে তার মধ্যে সর্বশ্রেষ্ঠ দিন হল জুমআর দিন। এই দিনে আদমকে সৃষ্টি করা হয়েছে, এই দিনে তাঁকে বেহেশ্তে স্থান দেওয়া হয়েছে এবং এই দিনেই তাঁকে বের করে দেওয়া হয়েছে বেহেশ্ত থেকে। (এই দিনেই তাঁর তওবা কবুল করা হয়েছে, তাঁর মৃত্যু হয়েছে এই দিনেই। আর কিয়ামত সংঘটিত হবে এই দিনেই।”\n\n" +
            "[মুসলিম, মালেক, মুঅত্তা, আহমাদ, সহীহ, জামে ৩৩৩৪নং]\n\n" +
            "তিনি বলেন, “জুমআর দিন সকল দিনের সর্দার এবং আল্লাহর নিকট সবার চেয়ে মহান দিন। এমনকি এ দিনটি আল্লাহর নিকট আযহা ও ফিতরের দিন থেকেও শ্রেষ্ঠ। এই দিনে রয়েছে ৫টি বিশেষ বৈশিষ্ট্য; এই দিনে আল্লাহ আদমকে সৃষ্টি করেছেন, এই দিনে তাঁকে পৃথিবীতে অবতারণ করেছেন, এই দিনে তাঁর ইন্তিকাল হয়েছে, এই দিনে এমন একটি মুহূর্ত আছে, যদি কোন মুসলিম বান্দা সে মুহূর্তে আল্লাহর নিকট কোন কিছু বৈধ জিনিস প্রার্থনা করে, তাহলে আল্লাহ তাকে তা দিয়ে থাকেন। এই দিনে কিয়ামত সংঘটিত হবে। আর প্রত্যেক নৈকট্যপ্রাপ্ত ফিরিশ্তা, আকাশ, পৃথিবী, বাতাস, পর্বত, সমুদ্র এই দিনকে ভয় করে।\n\n" +
            "[ইবনে মাজাহ্, সুনান ১০৮৪নং]\n\n" +
            "এই দিনে মহান আল্লাহ জান্নাতে প্রত্যেক সপ্তাহে বেহেশতী বান্দাগণকে দর্শন দেবেন। হযরত আনাস (রাঃ) (ولدينا مزيد) এর ব্যাখ্যায় বলেন, ‘মহান আল্লাহ বেহেশতীদের জন্য প্রত্যেক জুমআর দিন জ্যোতিষ্মান হবেন। এই দিনের আসমানী ফিরিশ্তাবর্গের নিকট নাম হল, ‘য়্যাউমুল মাযীদ। এই দিনে গুনাহ মাফ হয়। হযরত আবু হুরাইরা (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল (ﷺ) বলেছেন, যে ব্যক্তি সুন্দরভাবে ওযু করে জুমআর উদ্দেশ্যে (মসজিদে) উপস্থিত হয়। অতঃপর মনোযোগ সহকারে (খুতবাহ্) শ্রবণ করে ও নীরব থাকে সেই ব্যক্তির ঐ জুমুআহ থেকে দ্বিতীয় জুমুআহর মধ্যবর্তীকালে সংঘটিত এবং অতিরিক্ত তিন দিনের পাপ মাফ করে দেওয়া হয়। আর যে ব্যক্তি (খুতবা চলাকালে) কাঁকর স্পর্শ করে সে অসার (ভুল) কাজ করে।\n\n" +
            "[মুসলিম, সহীহ ৮৫৭ নং আবূদাঊদ, সুনান, তিরমিযী, সুনান, ইবনে মাজাহ্, সুনান]\n\n" +
            "এই দিনে এমন একটি সময় আছে, যাতে দুআ কবুল হয়। মহানবী (ﷺ) বলেন,\n" +
            "জুমআর দিনে এমন একটি (সামান্য) মুহূর্ত আছে, যদি কোন মুসলিম বান্দা নামায পড়া অবস্থায় তা পায় এবং আল্লাহর নিকট কোন মঙ্গল প্রার্থনা করে, তাহলে আল্লাহ তাকে তা দিয়ে থাকেন।\n\n" +
            "[বুখারী ৯৩৫নং, মুসলিম, মিশকাত ১৩৫৭নং]\n\n" +
            "এই মুহূর্তের ব্যাপারে বলা হয়েছে যে, তা হল ইমামের মিম্বরে বসা থেকে নিয়ে নামায শেষ হওয়া পর্যন্ত সময়।\n\n" +
            "[মুসলিম, মিশকাত ১৩৫৮নং]\n\n" +
            "অথবা তা হল আসরের পর যে কোন একটি সময়। অবশ্য এ প্রসঙ্গে আরো অন্য সময়ের কথাও অনেকে বলেছেন।\n\n" +
            "[যাদুল মাআদ, ইবনুল কাইয়েম ১/৩৮৯-৩৯০]\n\n" +
            "এই দিনে দান-খয়রাত করার সওয়াব বেশী। হযরত কা’ব (রাঃ) বলেন, ‘অন্যান্য সকল দিন অপেক্ষা এই দিনে সদকাহ্ করার সওয়াব অধিক।’\n\n" +
            "[যাদুল মাআদ, ইবনুল কাইয়েম]\n\n" +
            "জুমআর ফজরের নামায জামাআত সহকারে পড়ার পৃথক বৈশিষ্ট্য আছে। মহানবী (ﷺ) বলেন, আল্লাহর নিকটে সব চাইতে শ্রেষ্ঠ নামায হল, জুমআর দিন জামাআত সহকারে ফজরের নামায।\n\n" +
            "[সিলসিলাহ সহীহাহ, আলবানী ১৫৬৬নং]\n\n" +
            "এই দিনে বা তার রাতে কেউ মারা গেলে কবরের আযাব থেকে রেহাই পাবে। মহানবী (ﷺ) বলেন, “যে মুসলিম জুমআর দিন অথবা রাতে মারা যায়, আল্লাহ তাকে কবরের ফিতনা থেকে বাচান।\n\n" +
            "[আহমাদ, মুসনাদ, তিরমিযী, সুনান, জামে ৫৭৭৩]",
            "Jumu'ah means the day of gathering, congregation, and assembly. It is the weekly Eid and a special day of worship for Muslims. The Holy Prophet (ﷺ) said: \"This day is a day of Eid that Allah has designated for Muslims. Therefore, whoever comes for Jumu'ah should take a bath and apply perfume if available. And make a regular habit of using the tooth-stick (Miswak).\"\n\n" +
            "[Ibn Majah, Sunan #1098]\n\n" +
            "The day of Friday is the best of all days—superior even to Eid al-Fitr and Eid al-Adha. On this day Adam was created and granted Paradise. The Holy Prophet (ﷺ) said:\n" +
            "\"The best day upon which the sun has risen is Friday; on it Adam was created, on it he was admitted into Paradise, and on it he was expelled from it. (On it his repentance was accepted, on it he passed away, and on it the Hour will be established).\"\n\n" +
            "[Muslim; Malik, Muwatta; Ahmad, Sahih; Jami #3334]\n\n" +
            "He (ﷺ) said: \"Friday is the master of all days and the greatest day in the sight of Allah. It is even greater before Allah than the days of Eid al-Adha and Eid al-Fitr. It has five distinct virtues: on this day Allah created Adam, on this day He sent him down to the earth, on this day Adam passed away, on this day there is a special hour during which no Muslim servant asks Allah for anything permissible except that He grants it to him, and on this day the Hour will be established. Every proximate angel, heaven, earth, wind, mountain, and sea stands in awe of this day.\"\n\n" +
            "[Ibn Majah, Sunan #1084]\n\n" +
            "On this day, Allah Almighty will grant His blessed vision to the dwellers of Paradise each week. Narrated by Anas (RA) in the commentary of the verse (وَلَدَيْنَا مَزِيدٌ): 'Allah Almighty will manifest His glorious divine light to the people of Paradise every Friday. The angels in the heavens name this day 'Yawm al-Majeed' (the Day of Abundance).' Sins are forgiven on this day. Narrated by Abu Hurairah (RA), the Messenger of Allah (ﷺ) said: 'Whoever performs Wudu thoroughly and proceeds to the mosque for Jumu'ah, listens attentively to the Khutbah and remains silent, his sins between that Friday and the following Friday—plus an additional three days—will be forgiven. And whoever touches pebbles (distracts himself) during the Khutbah has engaged in an idle, invalidating act.'\n\n" +
            "[Muslim, Sahih #857; Abu Dawud; Tirmidhi; Ibn Majah]\n\n" +
            "There is an hour on this day in which supplications are answered. The Holy Prophet (ﷺ) said:\n" +
            "\"On Friday there is a particular hour in which no Muslim servant asks Allah for good while standing in prayer, except that Allah grants it to him.\"\n\n" +
            "[Bukhari #935, Muslim, Mishkat #1357]\n\n" +
            "Regarding this hour, it is said that it is the duration from when the Imam sits on the pulpit until the prayer concludes.\n\n" +
            "[Muslim, Mishkat #1358]\n\n" +
            "Alternatively, it is an hour after Asr prayer until sunset. Other times have also been discussed by scholars.\n\n" +
            "[Zad al-Ma'ad, Ibn al-Qayyim 1/389-390]\n\n" +
            "Giving charity on this day carries immense reward. Ka'b (RA) stated: 'Giving charity on Friday is greater in virtue than on all other days.'\n\n" +
            "[Zad al-Ma'ad, Ibn al-Qayyim]\n\n" +
            "Offering the Fajr prayer in congregation on Friday has a unique distinction. The Holy Prophet (ﷺ) said: \"The best prayer in the sight of Allah is the Fajr prayer in congregation on Friday.\"\n\n" +
            "[Silsilah Sahihah, Albani #1566]\n\n" +
            "Whoever passes away on this day or its preceding night will be saved from the trial of the grave. The Prophet (ﷺ) said: \"No Muslim dies on the day or night of Friday except that Allah protects him from the trial and torment of the grave.\"\n\n" +
            "[Ahmad, Musnad; Tirmidhi, Sunan; Jami #5773]"
        ));

        return list;
    }
}
