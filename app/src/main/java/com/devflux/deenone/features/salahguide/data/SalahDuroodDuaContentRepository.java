package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: দুরুদ শরীফ (Durood Sharif / Salawat upon the Prophet).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahDuroodDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. দুরুদ শরীফ (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "দুরুদ শরীফ",
            "Durood Sharif (Salawat)",
            "১।اَللّهُمَّ صَلِّ عَلى مُحَمَّدٍ وَّعَلى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلى إِبْرَاهِيْمَ وَعَلى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَجِيْدُ، اَللّهُمَّ بَارِكْ عَلى مُحَمَّدٍ وَّعَلى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلى إِبْرَاهِيْمَ وَعَلى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَّجِيْدٌ। উচ্চারণ: আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিও ওয়া আলা আলি মুম্মাদিন কামা সাল্লাইতা ...",
            "1. اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ Transliteration: Allahumma salli 'ala Muhammadin wa 'ala aali Muhammadin kama sallayta...",
            "১।اَللّهُمَّ صَلِّ عَلى مُحَمَّدٍ وَّعَلى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلى إِبْرَاهِيْمَ وَعَلى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَجِيْدُ، اَللّهُمَّ بَارِكْ عَلى مُحَمَّدٍ وَّعَلى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلى إِبْرَاهِيْمَ وَعَلى آلِ إِبْرَاهِيْمَ إِنَّكَ حَمِيْدٌ مَّجِيْدٌ।\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিও ওয়া আলা আলি মুম্মাদিন কামা সাল্লাইতা আলা ইব্রাহিমা ওয়া আলা আলি ইব্রাহিমা ইন্নাকা হামিদুম্ মাজীদ। আল্লাহুম্মা বারিক আলা মুহাম্মাদিও ওয়া আলা আলি মুহাম্মাদিন কামা বারাকতা আলা ইব্রাহিমা ওয়া আলা আলি ইব্রাহিমা ইন্নাকা হামীদুম্ মাজীদ।\n\n" +
            "অর্থ:\n\n" +
            "হে আল্লাহ! আপনি হযরত মুহাম্মদ ও তাঁর বংশধরের উপর রহমত বর্ষণ করুন, যেমন আপনি হযরত ইব্রাহিম ও তাঁর বংশধরের উপর রহমত বর্ষণ করেছেন। নিশ্চয় আপনি প্রশংসিত, গৌরবান্বিত।",
            "1. اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\n" +
            "Transliteration:\n\n" +
            "Allahumma salli 'ala Muhammadin wa 'ala aali Muhammadin kama sallayta 'ala Ibrahima wa 'ala aali Ibrahima innaka Hamidum Majeed. Allahumma barik 'ala Muhammadin wa 'ala aali Muhammadin kama barakta 'ala Ibrahima wa 'ala aali Ibrahima innaka Hamidum Majeed.\n\n" +
            "Meaning:\n\n" +
            "\"O Allah! Send Your mercy upon Muhammad and the family of Muhammad, as You sent Your mercy upon Abraham and the family of Abraham. Truly, You are Praiseworthy, Glorious. O Allah! Bless Muhammad and the family of Muhammad, as You blessed Abraham and the family of Abraham. Truly, You are Praiseworthy, Glorious.\""
        ));

        return list;
    }
}
