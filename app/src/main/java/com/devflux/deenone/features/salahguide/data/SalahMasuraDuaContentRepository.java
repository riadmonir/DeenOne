package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: দোয়া মাসুরা (Dua Masura).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahMasuraDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. দোয়া মাসুরা (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "দোয়া মাসুরা",
            "Dua Masura",
            "اَللَّهُمَّ إِنِّيْ ظَلَمْتُ نَفْسِيْ ظُلْمًا كَثِيْرًا وَّلاَ يَغْفِرُ الذُّنُوْبَ إِلاَّ أَنْتَ، فَاغْفِرْ لِيْ مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِيْ إِنَّكَ أَنْتَ الْغَفُوْرُ الرَّحِيْمُ উচ্চারণ: আল্লাহুম্মা ইন্নী যালামতু নাফ্সি যুলমান কাছীরাঁও ওয়ালা ইয়াগ্ফিরুয যুনূবা ইল্লা আন্ত, ফাগ্ফিরলি মাগফিরাতাম মিন ‘ইনদিক, ওয়ারহাম্নি ইন্নাকা আ...",
            "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ Transliteration: Allahumma innee zalamtu nafsee zulman katheeran wa la yaghfirudh-dhunooba illa Anta, faghfir lee maghfiratan min 'indika warhamnee...",
            "اَللَّهُمَّ إِنِّيْ ظَلَمْتُ نَفْسِيْ ظُلْمًا كَثِيْرًا وَّلاَ يَغْفِرُ الذُّنُوْبَ إِلاَّ أَنْتَ، فَاغْفِرْ لِيْ مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِيْ إِنَّكَ أَنْتَ الْغَفُوْرُ الرَّحِيْمُ\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লাহুম্মা ইন্নী যালামতু নাফ্সি যুলমান কাছীরাঁও ওয়ালা ইয়াগ্ফিরুয যুনূবা ইল্লা আন্ত, ফাগ্ফিরলি মাগফিরাতাম মিন ‘ইনদিক, ওয়ারহাম্নি ইন্নাকা আন্তাল গাফূরুর রহীম।\n\n" +
            "অর্থ:\n\n" +
            "হে আল্লাহ! আমি আমার নফসের উপরে অসংখ্য জুলুম করেছি। ঐসব গুনাহ মাফ করার কেউ নেই আপনি ব্যতীত। অতএব আপনি আমাকে আপনার পক্ষ হতে বিশেষভাবে ক্ষমা করুন এবং আমার উপরে অনুগ্রহ করুন। নিশ্চয়ই আপনি ক্ষমাশীল ও দয়াবান।\n\n" +
            "[সহিহ বুখারি, সহিহ মুসলিম]",
            "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\n" +
            "Transliteration:\n\n" +
            "Allahumma innee zalamtu nafsee zulman katheeran wa la yaghfirudh-dhunooba illa Anta, faghfir lee maghfiratan min 'indika warhamnee, innaka Antal-Ghafoorur-Raheem.\n\n" +
            "Meaning:\n\n" +
            "\"O Allah! I have greatly wronged my soul, and no one forgives sins except You. So grant me forgiveness from Yourself and have mercy upon me. Truly, You are the Most Forgiving, the Most Merciful.\"\n\n" +
            "[Sahih al-Bukhari, Sahih Muslim]"
        ));

        return list;
    }
}
