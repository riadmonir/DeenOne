package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আর রাকআত ছুটে গেলে (What to Do When Missing a Rak'ah of Jumu'ah).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and Classical Fiqh references.
 */
public class JumuahMissedRakatsContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আর রাকআত ছুটে গেলে (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আর রাকআত ছুটে গেলে",
            "What to Do When Missing a Rak'ah of Jumu'ah",
            "কারো জুমআর এক রাকআত ছুটে গেলে বাকী আর এক রাকআত ইমামের সালাম ফিরার পর উঠে পড়ে নিলে তার জুমুআহ হয়ে যাবে। অনুরুপ কেউ দ্বিতীয় রাকআতের রুকূ পেলেও ঐ রাকআত এবং তার সাথে আর এক রাকআত পড়লে তারও জুমুআহ হয়ে যাবে। ...",
            "If a worshipper misses one Rak'ah of Jumu'ah, they can stand up after the Imam finishes with Salam and complete the remaining one Rak'ah, and their Jumu'ah prayer will be valid...",
            "কারো জুমআর এক রাকআত ছুটে গেলে বাকী আর এক রাকআত ইমামের সালাম ফিরার পর উঠে পড়ে নিলে তার জুমুআহ হয়ে যাবে। অনুরুপ কেউ দ্বিতীয় রাকআতের রুকূ পেলেও ঐ রাকআত এবং তার সাথে আর এক রাকআত পড়লে তারও জুমুআহ হয়ে যাবে। কিন্তু যদি কেউ দ্বিতীয় রাকআতের রুকূ থেকে ইমামের মাথা তোলার পর জামাআতে শামিল হয়, তাহলে সে জুমআর নামায পাবে না। এই অবস্থায় তাকে যোহরের ৪ রাকআত আদায়ের নিয়তে জামাআতে শামিল হয়ে ইমামের সালাম ফিরার পর ৪ রাকআত ফরয পড়তে হবে।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪১৮, ৪২১]\n\n" +
            "যেমন জামাআত ছুটে গেলে জুমআও ছুটে যাবে। এ ক্ষেত্রেও একাকী যোহ্র পড়তে হবে। কারণ জামাআত ছাড়া জুমুআহ হয় না।\n\n" +
            "মহানবী (ﷺ) বলেন, “যে ব্যক্তি জুমআর এক রাকআত নামায পায়, সে যেন অপর এক রাকআত পড়ে নেয়।”\n\n" +
            "[ইবনে মাজাহ্, সুনান,হাকেম, মুস্তাদরাক, ইরওয়াউল গালীল, আলবানী ৬২২, জামে ৫৯৯১নং]\n\n" +
            "মহানবী (ﷺ) বলেন, “যে ব্যক্তি এক রাকআত নামায পায়, সে নামায পেয়ে যায়।”\n\n" +
            "[বুখারী ৫৭৯, মুসলিম, সহীহ ৬০৭, তিরমিযী, সুনান ৫২৪নং]\n\n" +
            "এর বিপরীত অর্থ এই দাঁড়ায় যে, “যে ব্যক্তি এক রাকআত নামায পায় না, সে নামায পায় না।” এ জন্যই ইমাম তিরমিযী উক্ত হাদীসের টীকায় বলেছেন, ‘এ হাদীসটি হাসান সহীহ। নবী (ﷺ)-এর অধিকাংশ সাহাবা ও অন্যান্য আহলে ইলমগণ এহাদীসের উপর আমল করেছেন। তাঁরা বলেছেন, “যে ব্যক্তি জুমআর এক রাকআত পেয়ে যায়, সে ব্যক্তি যেন আর এক রাকআত পড়ে নেয়। কিন্তু যে (দ্বিতীয় রাকআতের তাশাহহুদের) বৈঠক অবস্থায় জামাআত পায়, সে যেন যোহরের ৪ রাকআত পড়ে নেয়।” এ মত গ্রহণ করেছেন সুফয়্যান সওরী, ইবনুল মুবারক, শাফেয়ী, আহমাদ এবং ইসহাক (রহঃ)।\n\n" +
            "ইবনে মাসঊদ (রাঃ) বলেন, “যে ব্যক্তি জুমআর এক রাকআত পেয়ে যায়, সে ব্যক্তি যেন আর এক রাকআত পড়ে নেয়। কিন্তু যে (দ্বিতীয় রাকআতের) রুকূ না পায়, সে যেন যোহরের ৪ রাকআত পড়ে নেয়।”\n\n" +
            "[ইবনে আবী শাইবা, ত্বাবারানীরানী, মু’জাম, বায়হাকী, ইরওয়াউল গালীল, আলবানী ৬২১নং]\n\n" +
            "ইবনে উমার (রাঃ) বলেন, “যে ব্যক্তি জুমআর এক রাকআত পেয়ে যায়, সে ব্যক্তি যেন আর এক রাকআত পড়ে নেয়। কিন্তু যে (দ্বিতীয় রাকআতের) তাশাহহুদ পায়, সে যেন যোহরের ৪ রাকআত পড়ে নেয়।”\n\n" +
            "[বায়হাকী, ইরওয়াউল গালীল, আলবানী ৬২১]\n\n" +
            "কোন কোন বর্ণনায় তাশাহহুদ পেলে নামায পেয়ে যাওয়ার কথা বলা হয়েছে। আর তার মানে হল জামাআতের সওয়াব পেয়ে যাওয়া।\n\n" +
            "[ইরওয়াউল গালীল, আলবানী ৩/৮২]\n\n" +
            "প্রকাশ থাকে যে, জুমআর খুতবা না শুনলেও; বরং ১ রাকআত নামায না পেলেও জুমুআহ হয়ে যাবে। যেমন, জুমআর খুতবা দিলে অথবা শুনলেও যদি নামাযের ১ রাকআতও না পায়, তাহলে তাকে যোহ্রই পড়তে হবে।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪১০]\n\n" +
            "উল্লেখ্য যে, জুমআর নামায পড়তে পড়তে যদি কারো ওযূ নষ্ট হয়ে যায় এবং ওযূ করে ফিরে এসে যদি দ্বিতীয় রাকআতের রুকূ পেয়ে যায়, তাহলে সে আর এক রাকআত পড়ে নেবে। নচেৎ সিজদা বা তাশাহহুদ পেলে যোহরের নিয়তে শামিল হয়ে ৪ রাকআত পড়বে। তদনুরুপ জামাআত ছুটে গেলেও যোহ্র পড়বে।\n\n" +
            "অনুরুপ কোন ইমাম সাহেব যদি বিনা ওযূতে জুমুআহ পড়িয়ে নামাযের শেষে মনে হয়, তাহলে মুক্তাদীদের নামায সহীহ হয়ে যাবে। আর ইমাম ঐ নামায কাযা করতে ৪ রাকআত যোহ্র পড়বেন।\n\n" +
            "[আল-মুন্তাকা মিন ফাতাওয়াল ফাওযান ৩/৬৮]",
            "If a worshipper misses one Rak'ah of Jumu'ah, they can stand up after the Imam finishes with Salam and complete the remaining one Rak'ah, and their Jumu'ah prayer will be valid. Similarly, if someone catches the Ruku of the second Rak'ah, they have caught that Rak'ah, and upon offering one more Rak'ah after the Imam's Salam, their Jumu'ah is valid. However, if one joins the congregation after the Imam has risen from the Ruku of the second Rak'ah, they have not caught the Jumu'ah prayer. In this case, they should join the congregation with the intention of offering four Rak'ahs of Dhuhr, and after the Imam's Salam, stand up and complete four Rak'ahs of obligatory Dhuhr prayer.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/418, 421]\n\n" +
            "Just as missing the congregation entirely means missing Jumu'ah, in such a case one must pray Dhuhr individually, because Jumu'ah is not valid without congregation.\n\n" +
            "The Holy Prophet (ﷺ) said: \"Whoever catches one Rak'ah of the Friday prayer, let him add another Rak'ah to it.\"\n\n" +
            "[Ibn Majah, Sunan; Al-Hakim, Mustadrak; Irwa al-Ghalil, Albani #622; Jami #5991]\n\n" +
            "The Holy Prophet (ﷺ) said: \"Whoever catches one Rak'ah of the prayer has indeed caught the prayer.\"\n\n" +
            "[Bukhari #579, Muslim, Sahih #607, Tirmidhi, Sunan #524]\n\n" +
            "The converse implication of this is: \"Whoever does not catch at least one Rak'ah has not caught the prayer.\" For this reason, Imam Tirmidhi noted in his commentary on this Hadith: 'This Hadith is Hasan Sahih. The majority of the Prophet's (ﷺ) Companions and other scholars of knowledge acted upon this Hadith. They held: \"Whoever catches one Rak'ah of Jumu'ah should pray one more Rak'ah. But whoever catches the congregation during the sitting (Tashahhud) of the second Rak'ah must pray four Rak'ahs of Dhuhr.\" This view was adopted by Sufyan ath-Thawri, Ibn al-Mubarak, ash-Shafi'i, Ahmad, and Ishaq (RA).'\n\n" +
            "Ibn Mas'ud (RA) stated: \"Whoever catches one Rak'ah of Jumu'ah should pray one more Rak'ah. But whoever does not catch the Ruku (of the second Rak'ah) must pray four Rak'ahs of Dhuhr.\"\n\n" +
            "[Ibn Abi Shaybah; Tabarani, Mu'jam; Bayhaqi; Irwa al-Ghalil, Albani #621]\n\n" +
            "Ibn Umar (RA) stated: \"Whoever catches one Rak'ah of Jumu'ah should pray one more Rak'ah. But whoever catches only the Tashahhud (of the second Rak'ah) must pray four Rak'ahs of Dhuhr.\"\n\n" +
            "[Bayhaqi; Irwa al-Ghalil, Albani #621]\n\n" +
            "In some narrations where catching the Tashahhud is mentioned as 'catching the prayer', it specifically refers to attaining the reward of the congregation.\n\n" +
            "[Irwa al-Ghalil, Albani 3/82]\n\n" +
            "It is worth noting that even if someone did not hear the Friday Khutbah, as long as they catch at least one Rak'ah of the prayer, their Jumu'ah is valid. Conversely, if someone delivered or listened to the Khutbah but failed to catch even a single Rak'ah of the congregational prayer, they must offer Dhuhr instead.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/410]\n\n" +
            "Additionally, if one's Wudu breaks during the Jumu'ah prayer and after renewing Wudu they return and catch the Ruku of the second Rak'ah, they should complete one more Rak'ah. Otherwise, if they catch the Sujud or Tashahhud, they must join with the intention of Dhuhr and complete four Rak'ahs. Similarly, if one misses the congregation entirely, they must offer Dhuhr.\n\n" +
            "Likewise, if an Imam leads the Jumu'ah prayer without Wudu and only remembers after finishing the prayer, the prayer of the followers (Muqtadis) remains valid, while the Imam must make up for it by offering four Rak'ahs of Dhuhr.\n\n" +
            "[Al-Muntaqa min Fatawa al-Fawzan 3/68]"
        ));

        return list;
    }
}
