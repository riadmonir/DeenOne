package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: দুই সিজদার মাঝের দোয়া (Dua between Two Sujoods).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahJalsahDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. দুই সিজদার মাঝের দোয়া (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "দুই সিজদার মাঝের দোয়া",
            "Dua between Two Sujoods",
            "দুই সিজদার মাঝখানে গুরুত্বপূর্ণ দো'আ রয়েছে। যে দো'আটি এতটাই সারগর্ভপূর্ণ যে, যদি সেই একটি দো'আ কবুল হয় তবে মানুষের জীবন সব দিক দিয়ে ইহ-পরকালে পরিপূর্ণ হয়ে যাবে। আমরা সেই দো'আটি শিখব। তার আগে জেনে নেওয়া দরকার একটি সিজদার ...",
            "There are profound supplications between the two prostrations (Jalsah). These supplications are so comprehensive that if accepted, a person's life in this world and the Hereafter becomes completely fulfilled...",
            "দুই সিজদার মাঝখানে গুরুত্বপূর্ণ দো'আ রয়েছে। যে দো'আটি এতটাই সারগর্ভপূর্ণ যে, যদি সেই একটি দো'আ কবুল হয় তবে মানুষের জীবন সব দিক দিয়ে ইহ-পরকালে পরিপূর্ণ হয়ে যাবে। আমরা সেই দো'আটি শিখব। তার আগে জেনে নেওয়া দরকার একটি সিজদার পর আর একটি সিজদা দেওয়ার আগে অর্থ্যাৎ দুই সিজদার মাঝখানে কিভাবে বসতে হয়। সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম বলেন,\n\n" +
            "وَيَرْفَعُ رَأْسَهُ حَتَّى يَسْتَوِيَ قَاعِدًا\n\n" +
            "অর্থ:\n\n" +
            "(একটি সিজদার পরে উঠে এমনভাবে বসবে) যাতে শরীরের জোড়াসমূহ স্ব-স্ব স্থানে যথারীতি অবস্থান করে।\n\n" +
            "[আবু দাউদ হা/৮৫৭]\n\n" +
            "দুই সিজদার মাঝখানে পড়ার দুইটি দো'আ রয়েছে। যেকোন একটি পড়তে হয়। দো'আ দুটি হল\n\n" +
            "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي\n\n" +
            "উচ্চারণ:\n\n" +
            "রব্বিগফির লী, রব্বিগফির লী।\n\n" +
            "অর্থ:\n\n" +
            "হে আমার রব! আপনি আমাকে ক্ষমা করুন। হে আমার রব! আপনি আমাকে ক্ষমা করুন।\n\n" +
            "[আবূ দাউদ ১/২৩১]\n\n" +
            "اللَّهُمَّ اغْفِرْ لِي، وَارْحَمْنِي، وَاهْدِنِي، وَاجْبُرْنِي، وَعَافِنِي، وَارْزُقْنِي، وَارْفَعْنِي\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লা-হুম্মাগফির লী, ওয়ারহামনী, ওয়াহদিনী, ওয়াজবুরনী, ওয়া‘আফিনী, ওয়ারযুক্বনী, ওয়ারফা‘নী।\n\n" +
            "অর্থ:\n\n" +
            "হে আল্লাহ! আপনি আমাকে ক্ষমা করুন, আমার প্রতি দয়া করুন, আমাকে সঠিক পথে পরিচালিত করুন, আমার সমস্ত ক্ষয়ক্ষতি পূরণ করে দিন, আমাকে নিরাপত্তা দান করুন, আমাকে রিযিক দান করুন এবং আমার মর্যাদা বৃদ্ধি করুন”।\n\n" +
            "[আবূ দাউদ, ১/২৩১, নং ৮৫০; তিরমিযী, নং ২৮৪, ২৮৫; ইবন মাজাহ, নং ৮৯৮]",
            "There are profound supplications between the two prostrations (Jalsah). These supplications are so comprehensive that if accepted, a person's life in this world and the Hereafter becomes completely fulfilled. Before learning these supplications, it is important to know how to sit properly between the two prostrations. The Messenger of Allah (ﷺ) said:\n\n" +
            "وَيَرْفَعُ رَأْسَهُ حَتَّى يَسْتَوِيَ قَاعِدًا\n\n" +
            "Meaning:\n\n" +
            "\"(He would raise his head from prostration and sit) until every joint returned to its proper place.\"\n\n" +
            "[Abu Dawud H/857]\n\n" +
            "There are two supplications to recite between the two prostrations; either one may be recited:\n\n" +
            "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي\n\n" +
            "Transliteration:\n\n" +
            "Rabbighfir lee, Rabbighfir lee.\n\n" +
            "Meaning:\n\n" +
            "\"O my Lord, forgive me! O my Lord, forgive me!\"\n\n" +
            "[Abu Dawud 1/231]\n\n" +
            "اللَّهُمَّ اغْفِرْ لِي، وَارْحَمْنِي، وَاهْدِنِي، وَاجْبُرْنِي، وَعَافِنِي، وَارْزُقْنِي، وَارْفَعْنِي\n\n" +
            "Transliteration:\n\n" +
            "Allahummaghfir lee, warhamnee, wahdinee, wajburnee, wa'aafinee, warzuqnee, warfa'nee.\n\n" +
            "Meaning:\n\n" +
            "\"O Allah! Forgive me, have mercy on me, guide me, rectify all my shortcomings, grant me well-being, provide for me, and elevate my rank.\"\n\n" +
            "[Abu Dawud 1/231, No. 850; Tirmidhi, No. 284, 285; Ibn Majah, No. 898]"
        ));

        return list;
    }
}
