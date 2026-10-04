package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: অজুর দোয়া (Supplications for Wudu).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahWuduDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. অজুর দোয়া (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "অজুর দোয়া",
            "Supplications for Wudu",
            "অযুর সময় এই দোয়াটি পড়া: اللَّهُمَّ اغْفِرْ لِي ذَنْبِي، وَوَسِّعْ لِي فِي دَارِي، وَبَارِكْ لِي فِي رِزْقِي উচ্চারণ: আল্লা-হুম্মাগফিরলি জামবি, ওয়া ওয়াস্ সিলি ফি দারি, ওয়া বারিক-লি ফি রিযক্বি।” (নাসাঈ) অর্থ: হে আল্লাহ! আমার গোনাহ ক্ষমা করে দাও। আমার জন্য আমার বাস...",
            "Reciting this supplication during Wudu: Allahummaghfir li dhanbi, wa wassi' li fi dari, wa barik li fi rizqi. Meaning: O Allah! Forgive my sins, expand my dwelling for me, and bless my sustenance. Reciting Kalima Shahadah after Wudu...",
            "অযুর সময় এই দোয়াটি পড়া:\n\n" +
            "اللَّهُمَّ اغْفِرْ لِي ذَنْبِي، وَوَسِّعْ لِي فِي دَارِي، وَبَارِكْ لِي فِي رِزْقِي\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লা-হুম্মাগফিরলি জামবি, ওয়া ওয়াস্ সিলি ফি দারি, ওয়া বারিক-লি ফি রিযক্বি।” (নাসাঈ)\n\n" +
            "অর্থ:\n\n" +
            "হে আল্লাহ! আমার গোনাহ ক্ষমা করে দাও। আমার জন্য আমার বাসস্থান প্রসারিত করে দাও এবং আমার রিযিক্বে বরকত দাও।”\n\n" +
            "অযুর পর কালেমা শাহাদাত পাঠ করা:\n\n" +
            "لَا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\n" +
            "উচ্চারণ:\n\n" +
            "আশ-হাদু আল্লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা-শারি কালাহু, ওয়া আশ-হাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসূলুহু।\n\n" +
            "হজরত ওমর (রাঃ) বর্ণনা করেন- রাসুলুল্লাহ (সাঃ) বলেছেন,\n\n" +
            "যে ব্যক্তি উত্তমরূপে অযু করে কালেমায়ে শাহাদাত পাঠ করবে, তার জন্য জান্নাতের আটটি দরজা খুলে দেয়া হবে। যে দরজা দিয়ে ইচ্ছা সে প্রবেশ করবে।\n\n" +
            "[মুসলিম ও মিশকাত]",
            "Reciting this supplication during Wudu:\n\n" +
            "اللَّهُمَّ اغْفِرْ لِي ذَنْبِي، وَوَسِّعْ لِي فِي دَارِي، وَبَارِكْ لِي فِي رِزْقِي\n\n" +
            "Transliteration:\n\n" +
            "Allahummaghfir li dhanbi, wa wassi' li fi dari, wa barik li fi rizqi. (An-Nasa'i)\n\n" +
            "Meaning:\n\n" +
            "\"O Allah! Forgive my sins, expand my dwelling for me, and bless my sustenance.\"\n\n" +
            "Reciting Kalima Shahadah after Wudu:\n\n" +
            "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\n" +
            "Transliteration:\n\n" +
            "Ash-hadu alla ilaha illallahu wahdahu la sharika lahu, wa ash-hadu anna Muhammadan 'abduhu wa Rasuluhu.\n\n" +
            "Narrated by Umar (RA), the Messenger of Allah (ﷺ) said:\n\n" +
            "\"Whoever performs ablution (Wudu) thoroughly and then recites the Kalima Shahadah, all eight gates of Paradise will be opened for him, and he may enter through whichever gate he wishes.\"\n\n" +
            "[Muslim and Mishkat]"
        ));

        return list;
    }
}
