package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আর নামাজের ইতিহাস.
 * 100% verbatim text matching screenshots and authentic Islamic sources.
 */
public class JumuahHistoryContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আর নামাজের ইতিহাস
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আর নামাজের ইতিহাস",
            "History of Jumu'ah Prayer",
            "জুমার ব্যাখ্যা ও শাব্দিক অর্থ: আরবি শব্দ ‘জুমআহ/জুমুআহ’র ব্যাপারে একাধিক মতামত পাওয়া যায়। জমহুর উলামায়ে কেরামের মতে শব্দটি হবে ‘জুমুআহ’। আর ইমাম আ’মাশ (রহ.)-এর মতে, শব্দটি হবে ‘জুমআহ’। [রুহুল মাআনি: ১৪/৯৯ দরসে তিরমিজি:২/১৯২- এর সূত্রে]...",
            "Explanation and Linguistic Meaning of Jumu'ah: Multiple opinions exist regarding the Arabic term 'Jum'ah / Jumu'ah'. According to the majority of scholars (Jamhur), the term is 'Jumu'ah', while according to Imam al-A'mash (RA), it is 'Jum'ah'. [Ruh al-Ma'ani: 14/99; via Dars-e-Tirmidhi: 2/192]...",
            "জুমার ব্যাখ্যা ও শাব্দিক অর্থ:\n\n" +
            "আরবি শব্দ ‘জুমআহ/জুমুআহ’র ব্যাপারে একাধিক মতামত পাওয়া যায়। জমহুর উলামায়ে কেরামের মতে শব্দটি হবে ‘জুমুআহ’। আর ইমাম আ’মাশ (রহ.)-এর মতে, শব্দটি হবে ‘জুমআহ’। \n\n" +
            "[রুহুল মাআনি: ১৪/৯৯ দরসে তিরমিজি:২/১৯২- এর সূত্রে]\n\n" +
            "আমাদের বাংলাদেশের রীতি অনুযায়ী একে ‘জুমা’ বলা হয়। জুমার অর্থ হলো একত্র হওয়া, সংঘবদ্ধ হওয়া। যেহেতু এই দিনে মানুষ একত্র হয়, তাই এ দিনকে ইয়াওমুল জুমা বা জুমার দিনও বলা হয়।\n\n" +
            "জুমার দিনের নামকরণ:\n\n" +
            "মূর্খতাযুগে শুক্রবারকে ‘ইয়াওমে আরুবা’ বলা হতো। আরবে কাআব ইবনে লুয়াই সর্বপ্রথম এর নাম ‘ইয়াওমুল জুমুআ’ বা জুমার দিন রাখেন। এই দিনে কোরাইশদের সমাবেশ হতো এবং কাআব ইবনে লুয়াই ভাষণ দিতেন। এটা রাসুলুল্লাহ (সা.)-এর আবির্ভাবের পাঁচশ ষাট বছর পূর্বের ঘটনা। কাআব ইবনে লুয়াই রাসুলুল্লাহ (সা.)-এর পূর্বপুরুষদের অন্যতম।\n\n" +
            "[তাফসিরে মারেফুল কোরআন বাংলা - ১৩৭১]\n\n" +
            "ইসলাম-পরবর্তী সময়ে এ দিনে মুসলিম উম্মাহ আল্লাহর নির্দেশ পালনের উদ্দেশ্যে মসজিদে একত্র হয় বলে দিনটিকে ইয়াওমুল জুমুআ বা জুমার দিন বলা হয়। কোরআনেও সে কথাটি ওঠে এসেছে।ইরশাদ হয়েছে,\n\n" +
            "হে মুমিনগণ, জুমার দিনে যখন নামাজের আজান দেওয়া হয়, তখন তোমরা আল্লাহর স্মরণে দ্রুত মসজিদের দিকে ধাবিত হও। আর বেচাকেনা বন্ধ করো। এটা তোমাদের জন্য উত্তম, যদি তোমরা বুঝতে পারো।\n\n" +
            "[সুরা জুমা - ৯]\n\n" +
            "যেভাবে জুমার সূচনা:\n\n" +
            "প্রথম হিজরি থেকে। প্রিয় নবী (সা.) পবিত্র নগরী মক্কা থেকে হিজরত করে মদিনার কুবায় গিয়ে অবস্থান করেন। সেখান থেকে জুমার দিন মদিনায় যাওয়ার পথে বনি সালেম গোত্রের উপত্যকায় পৌঁছলে জোহরের সময় হয়ে যায়। তিনি সেখানেই জুমা আদায় করেন। ইসলামের ইতিহাসে এটাই ছিল প্রথম জুমা। তবে হিজরতের পর জুমার নামাজ ফরজ হওয়ার আগে বিশ্বনবী মুহাম্মদ (সা.)-এর নবুয়তের ১২তম বর্ষে মদিনায় নাকিউল খাজিমাতে দুই রাকাত জুমা পড়ার প্রমাণ পাওয়া যায়। সেখানে হজরত আসআদ বিন জুরারাহ (রা.)-এর ইমামতিতে শুক্রবার সে নামাজ অনুষ্ঠিত হয়। আর সেটি ছিল নফল নামাজ। এ প্রসঙ্গে মুহাম্মদ ইবনে সিরিন থেকে বর্ণিত আছে, রাসুল (সা.) মদিনায় আগমনের পর এবং জুমার নামাজ ফরজ হওয়ার আগে একবার মদিনার আনসারগণ একত্র হয়ে আলোচনা করলেন যে ইহুদিদের জন্য সপ্তাহে একটা দিন নির্দিষ্ট আছে, যে দিন তারা সবাই একত্র হয়। আর নাসারাদেরও সপ্তাহে সবার একত্র হওয়ার জন্য এক দিন নির্ধারিত আছে। সুতরাং আমাদের জন্য সপ্তাহে একটা দিন নির্দিষ্ট হওয়া প্রয়োজন, যে দিন আমরা সবাই সমবেত হয়ে আল্লাহকে স্মরণ করব, নামাজ আদায় করব। অতঃপর তারা আলোচনাকালে বললেন, শনিবার ইহুদিদের আর রবিবার নাসারাদের জন্য নির্ধারিত। অবশেষে তাঁরা ‘ইয়াওমুল আরুবা’ (শুক্রবার)-কে গ্রহণ করলেন এবং তাঁরাই এদিনকে ‘জুমার দিন’ নামকরণ করলেন। \n\n" +
            "[দরসে তিরমিজি - ২/১৯২-এর সূত্রে মুসান্নাফে আবদুর রাজ্জাক - ৩/১৫৯, হাদিস - ৫১৪৪]\n\n" +
            "তবে বিশ্বনবী (সা.) কুবা নামক স্থান থেকে মদিনায় পৌঁছার পথে বনি সালেম গোত্রের উপত্যকায় সবাইকে নিয়ে যে জুমা আদায় করেন, এটিকে ইসলামের প্রথম জুমা ধরা হয়। আর সেদিন থেকেই মুমিন মুসলমানদের জুমা আদায় শুরু হয়। সুতরাং জুমা হলো, মুমিন মুসলমানের জন্য বিশেষ ফজিলতপূর্ণ ইবাদত। যা প্রিয় নবী (সা.) মদিনায় হিজরত করে আনুষ্ঠানিকভাবে বনি সালেম গোত্রের উপত্যকায় আদায়ের মাধ্যমে শুরু করেছিলেন। আজও মুমিন-মুসলমানের জন্য তা বিশেষ ইবাদত হিসেবে জারি আছে।",
            "Explanation and Linguistic Meaning of Jumu'ah:\n\n" +
            "Multiple opinions exist regarding the Arabic term 'Jum'ah / Jumu'ah'. According to the majority of scholars (Jamhur), the term is pronounced 'Jumu'ah', whereas according to Imam al-A'mash (RA), it is vocalized as 'Jum'ah'.\n\n" +
            "[Ruh al-Ma'ani: 14/99; via Dars-e-Tirmidhi: 2/192]\n\n" +
            "In customary convention, it is called Jumu'ah. The linguistic meaning of Jumu'ah is gathering, uniting, and assembling together. Since people congregate on this day, it is termed 'Yawm al-Jumu'ah' or the Day of Friday.\n\n" +
            "Naming of the Day of Jumu'ah:\n\n" +
            "During the Pre-Islamic Era of Ignorance (Jahiliyyah), Friday was called 'Yawm al-'Arubah'. In Arabia, Ka'b ibn Lu'ayy was the first to name it 'Yawm al-Jumu'ah' (the Day of Assembly). On this day, the Quraysh used to assemble and Ka'b ibn Lu'ayy would deliver an address to them. This took place five hundred and sixty years before the advent of the Prophet (ﷺ). Ka'b ibn Lu'ayy was one of the noble ancestors of the Prophet (ﷺ).\n\n" +
            "[Tafsir Ma'ariful Quran Bangla - 1371]\n\n" +
            "In the Islamic era, this day is called Yawm al-Jumu'ah because the Muslim Ummah gathers in mosques to fulfill the divine command of Allah. This is proclaimed in the Holy Quran, where Allah says:\n\n" +
            "\"O you who have believed, when the call is proclaimed for the prayer on the day of Jumu'ah, proceed swiftly to the remembrance of Allah and cease all trade. That is better for you, if you only knew.\"\n\n" +
            "[Surah Al-Jumu'ah: 9]\n\n" +
            "How Jumu'ah Began:\n\n" +
            "From the first year of Hijrah. The Holy Prophet (ﷺ) migrated from the sacred city of Makkah and stayed at Quba in Madinah. From there, while journeying to Madinah on Friday, upon reaching the valley of the Banu Salim tribe, the time of Dhuhr arrived. He offered the Jumu'ah prayer there. In Islamic history, this was the very first congregational Jumu'ah. However, prior to the obligation of Jumu'ah following Hijrah, historical evidence shows that in the 12th year of Prophethood in Madinah, two Rak'ahs of Jumu'ah were offered at Naqi' al-Khadhimat under the leadership of As'ad ibn Zurarah (RA) on a Friday, which was a voluntary prayer. Regarding this, it is narrated from Muhammad ibn Sirin that before the arrival of the Prophet (ﷺ) in Madinah and prior to Jumu'ah being made obligatory, the Ansar of Madinah gathered and discussed: \"The Jews have a specific day in the week on which they assemble, and the Christians likewise have a designated day in the week for gathering. Therefore, we should establish a day in the week on which we all gather to remember Allah and pray.\" They noted that Saturday belonged to the Jews and Sunday to the Christians. Consequently, they adopted 'Yawm al-'Arubah' (Friday) and named it the Day of Jumu'ah.\n\n" +
            "[Dars-e-Tirmidhi: 2/192; via Musannaf Abdur Razzaq: 3/159, Hadith #5144]\n\n" +
            "However, the Jumu'ah prayer that the Prophet (ﷺ) led for everyone in the valley of Banu Salim on his way from Quba to Madinah is recognized as the first official Jumu'ah in Islam. From that day, the continuous practice of Jumu'ah for Muslims commenced. Thus, Jumu'ah is an exceptionally virtuous act of worship for believers, officially established by the Prophet (ﷺ) upon his migration in the valley of Banu Salim, and it remains a cornerstone of Islamic devotion to this day."
        ));

        return list;
    }
}
