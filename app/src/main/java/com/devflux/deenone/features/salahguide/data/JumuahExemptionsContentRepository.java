package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আ যাদের উপর ফরয নয় (Those Exempt from Obligatory Jumu'ah).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and Classical Fiqh references.
 */
public class JumuahExemptionsContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আ যাদের উপর ফরয নয় (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আ যাদের উপর ফরয নয়",
            "Those Exempt from Obligatory Jumu'ah",
            "মহিলা, শিশু ও অসুস্থ ব্যক্তি: নবী মুবাশ্শির (ﷺ) বলেন, প্রত্যেক মুসলিমের জন্য জামাআত সহকারে জুমুআহ ফরয। অবশ্য ৪ ব্যক্তির জন্য ফরয নয়; ক্রীতদাস, মহিলা, শিশু ও অসুস্থ। [আবূদাঊদ, সুনান ১০৬৭নং] যে ব্যক্তি (শত্রু, সম্পদ বিনষ্ট, সফরের সঙ্গী ছু...",
            "Women, Children, and the Sick: The Prophet (ﷺ) said: The Friday prayer in congregation is an obligatory duty upon every Muslim, with the exception of four individuals: an enslaved person, a woman, a child, and a sick person...",
            "মহিলা, শিশু ও অসুস্থ ব্যক্তি:\n\n" +
            "নবী মুবাশ্শির (ﷺ) বলেন,\n" +
            "প্রত্যেক মুসলিমের জন্য জামাআত সহকারে জুমুআহ ফরয। অবশ্য ৪ ব্যক্তির জন্য ফরয নয়; ক্রীতদাস, মহিলা, শিশু ও অসুস্থ।\n\n" +
            "[আবূদাঊদ, সুনান ১০৬৭নং]\n\n" +
            "যে ব্যক্তি (শত্রু, সম্পদ বিনষ্ট, সফরের সঙ্গী ছুটে যাওয়ার) ভয়ে, অথবা বৃষ্টি, কাদা বা অত্যন্ত শীত বা গ্রীষ্মের কারণে মসজিদে উপস্থিত হতে অক্ষম। মহানবী (ﷺ) বলেন,\n" +
            "যে ব্যক্তি মুআযযিনের (আযান) শোনে এবং কোন ওজর (ভয় অথবা অসুখ) তাকে জামাআতে উপস্থিত হতে বাধা না দেয়, তাহলে যে নামায সে পড়ে, তার সে নামায কবুল হয় না।\n\n" +
            "[আবূদাঊদ, সুনান ৫৫১নং]\n\n" +
            "একদা ইবনে আব্বাস (রাঃ) এক বৃষ্টিময় জুমআর দিনে তাঁর মুআযযিনকে বললেন, ‘তুমি যখন আশহাদু আন্না মুহাম্মাদুর রাসূলুল্লাহ্’ বলবে তখন বল, ‘তোমরা তোমাদের ঘরে নামায পড়ে নাও।’ এ কথা শুনে লোকেরা যেন আপত্তিকর মনোভাব ব্যক্ত করল। কিন্তু তিনি বললেন, ‘এরুপ তিনি করেছেন যিনি আমার থেকে শ্রেষ্ঠ। অর্থাৎ, আল্লাহর রসূল (ﷺ) এরুপ করেছেন। আর আমিও তোমাদেরকে এই কাদা ও পিছল জায়গার মাঝে বের হওয়াকে অপছন্দ করলাম।’\n\n" +
            "[বুখারী ৯০১, মুসলিম, সহীহ ৬৯৯নং]\n\n" +
            "প্রকাশ থাকে যে, যারা দূরের মাঠে অথবা জঙ্গলে অথবা সমুদ্রে কাজ করে এবং আযান শুনতে পায় না, তাছাড়া কাজ ছেড়ে শহর বা গ্রামে আসাও সম্ভব নয়, তাদের জন্য জুমুআহ ফরয নয়।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪১৪, ফাতাওয়া ইবনে উষাইমীন ১/৩৯৯]\n\n" +
            "মুসাফির:\n\n" +
            "মহানবী (ﷺ) জুমআর দিন সফরে থাকলে, জুমআর নামায না পড়ে যোহরের নামায পড়তেন। বিদায়ী হজ্জের সময় তিনি আরাফাতে অবস্থানকালে জুমআর নামায পড়েননি। বরং যোহ্র ও আসরের নামাযকে অগ্রিম জমা করে পড়েছিলেন। অনুরুপ আমল ছিল খুলাফায়ে রাশেদ্বীন (রাঃ) দেরও।উপর্যুক্ত ব্যক্তিবর্গের জন্য জুমআর নামায ফরয নয়। কিন্তু যোহরের নামায অবশ্যই ফরয। পরন্তু যদি তারাও জুমআর মসজিদে উপস্থিত হয়ে জামাআতে জুমুআহ পড়ে নেয়, তাহলে তা বৈধ। এ ক্ষেত্রে তাদের জুমুআহ হয়ে যাবে এবং যোহরের নামায মাফ হয়ে যাবে। একাধিক হাদীস এ কথা প্রমাণ করে যে, মহানবী (ﷺ) এবং খুলাফায়ে রাশেদ্বীন (রাঃ) দের যুগে মহিলারা জুমুআহ ও জামাআতে উপস্থিত হয়ে নামায আদায় করত। উল্লেখ্য যে, কোন মুসাফির জুমুআহ খুতবা দিলে ও ইমামতি করলে তা শুদ্ধ হয়ে যাবে।\n\n" +
            "[আলমুমতে শারহে ফিক্হ, ইবনে উষাইমীন ৫/২৩]\n\n" +
            "জুমআর জামাআতে কোন মুসাফির যোহরের কসর আদায় করার নিয়ত করতে পারে না। কারণ, যোহ্র অপেক্ষা জুমআর ফযীলত অনেক বেশী।\n\n" +
            "[আলমুমতে শারহে ফিক্হ, ইবনে উষাইমীন ৪/৫৭৪]",
            "Women, Children, and the Sick:\n\n" +
            "The Prophet of glad tidings (ﷺ) said:\n" +
            "\"The Friday (Jumu'ah) prayer in congregation is an obligatory duty upon every Muslim, with the exception of four individuals: an enslaved person, a woman, a child, and a sick person.\"\n\n" +
            "[Abu Dawud, Sunan #1067]\n\n" +
            "A person who is unable to attend the mosque due to fear (of enemies, loss of property, or missing their travel companions), or due to heavy rain, mud, or extreme cold or heat. The Holy Prophet (ﷺ) said:\n" +
            "\"Whoever hears the caller to prayer (Adhan) and has no valid excuse (fear or illness) that prevents him from attending the congregation, then the prayer he offers alone is not accepted.\"\n\n" +
            "[Abu Dawud, Sunan #551]\n\n" +
            "Once, on a rainy Friday, Ibn Abbas (RA) told his Mu'adhdhin: \"When you pronounce 'Ashhadu anna Muhammadan Rasulullah', then announce: 'Pray in your dwellings (homes).'\" Upon hearing this, the people expressed surprise and hesitation. Thereupon he said: \"This was done by one who was far superior to me—meaning the Messenger of Allah (ﷺ). And I disliked making you walk out in this slippery mud and dirt.\"\n\n" +
            "[Bukhari #901, Muslim, Sahih #699]\n\n" +
            "It is worth noting that for those who work in distant fields, forests, or at sea, where they cannot hear the Adhan, and it is unfeasible for them to leave their work and travel to a town or village, Jumu'ah is not obligatory upon them.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/414; Fatawa Ibn Uthaymeen 1/399]\n\n" +
            "Travelers (Musafir):\n\n" +
            "When the Holy Prophet (ﷺ) was traveling on a Friday, he would not offer the Jumu'ah prayer, but rather prayed Dhuhr. During the Farewell Pilgrimage (Hajjat al-Wada'), while staying at Arafah on a Friday, he did not offer Jumu'ah prayer; instead, he combined Dhuhr and Asr in advance. The Rightly Guided Caliphs (RA) followed the same practice. For the aforementioned categories of people, Jumu'ah prayer is not obligatory, but Dhuhr prayer remains mandatory. However, if they attend the mosque and pray Jumu'ah in congregation, it is fully valid for them; their Jumu'ah is accomplished, and the obligation of Dhuhr is lifted. Numerous Hadiths prove that during the era of the Prophet (ﷺ) and the Rightly Guided Caliphs, women used to attend the mosque and participate in Jumu'ah and congregational prayers. Furthermore, if a traveler delivers the Khutbah and leads the Jumu'ah prayer, it is entirely valid and sound.\n\n" +
            "[Al-Mumti' Sharh al-Fiqh, Ibn Uthaymeen 5/23]\n\n" +
            "A traveler joining the Jumu'ah congregation cannot make the intention for shortening (Qasr) Dhuhr, because the virtue and rank of Jumu'ah are far greater than Dhuhr.\n\n" +
            "[Al-Mumti' Sharh al-Fiqh, Ibn Uthaymeen 4/574]"
        ));

        return list;
    }
}
