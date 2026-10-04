package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: সিজদার তাসবিহ (Tasbih and Supplications of Sujood).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahSajdahDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. সিজদার তাসবিহ (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "সিজদার তাসবিহ",
            "Tasbih and Supplications of Sujood",
            "সিজদা হল দো‘আ কবুলের সর্বোত্তম সময়। আবু হুরায়রা (রাঃ) বর্ণিত রাসূলূল্লাহ (সাঃ) এরশাদ করেন, أَقْرَبُ مَا يَكُوْنُ الْعَبْدُ مِنْ رَّبِّهِ وَهُوَ سَاجِدٌ فَأَكْثِرُوا الدُّعَاءَ অর্থ: বান্দা স্বীয় প্রভুর সর্বাধিক নিকটে পৌঁছে যায়, যখন সে সিজদায় রত হয়। অতএব তোমরা ঐ সময় বেশী ...",
            "Sujood (prostration) is the best time for acceptance of supplications. Abu Hurairah (RA) narrated that the Messenger of Allah (ﷺ) said: أَقْرَبُ مَا يَكُوْنُ الْعَبْدُ مِنْ رَّبِّهِ وَهُوَ سَاجِدٌ فَأَكْثِرُوا الدُّعَاءَ Meaning: The nearest a servant comes to his Lord is when he is prostrating...",
            "সিজদা হল দো‘আ কবুলের সর্বোত্তম সময়। আবু হুরায়রা (রাঃ) বর্ণিত রাসূলূল্লাহ (সাঃ) এরশাদ করেন,\n\n" +
            "أَقْرَبُ مَا يَكُوْنُ الْعَبْدُ مِنْ رَّبِّهِ وَهُوَ سَاجِدٌ فَأَكْثِرُوا الدُّعَاءَ\n\n" +
            "অর্থ:\n\n" +
            "বান্দা স্বীয় প্রভুর সর্বাধিক নিকটে পৌঁছে যায়, যখন সে সিজদায় রত হয়। অতএব তোমরা ঐ সময় বেশী বেশী প্রার্থনা কর।\n\n" +
            "অন্য বর্ণনায় এসেছে, ‘তোমরা প্রার্থনায় সাধ্যমত চেষ্টা কর। আশা করা যায়, তোমাদের দো‘আ কবুল করা হবে’।\n\n" +
            "[মুসলিম, মিশকাত হা/৮৯৪, অনুচ্ছেদ-১৪]\n\n" +
            "তিনি আরও বলেন, রুকূ ও সিজদাতে কমপক্ষে তিনবার তাসবীহ পাঠ করবে।\n\n" +
            "[ইবনু মাজাহ হা/৮৮৮]\n\n" +
            "সিজদার জন্য হাদীসে অনেকগুলি দো‘আ এসেছে। তার মধ্যে বহুল প্রচলিত দো'আ হল,\n\n" +
            "سُبْحَانَ رَبِّىَ الْأَعْلَى\n\n" +
            "উচ্চারণ:\n\n" +
            "সুবহা-না রব্বিয়াল আ‘লা।\n\n" +
            "অর্থ:\n\n" +
            "মহা পবিত্র আমার প্রতিপালক যিনি সর্বোচ্চ।\n\n" +
            "এই দো'আটি তিনবার বলা। তবে তিনবারের বেশীও বলা যেতে পারে।\n\n" +
            "সিজদার অন্যান্য দো'আ হল -\n\n" +
            "اَللَّهُمَّ اغْفِرْ لِيْ ذَنْبِيْ كُلَّهُ دِقَّهُ وَجِلَّهُ وَ أَوَّلَهُ وَآخِرَهُ وَعَلاَنِيَتَهُ وَسِرَّهُ\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লাহুম্মাগফিরলি জম্বি কুল্লাহু, দিক্কাহু ওয়া জিল্লাহু, ওয়া আওয়ালাহু ওয়া আখিরাহু, ওয়া আলানিয়াতাহু ওয়া সিররাহু।\n\n" +
            "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ لآ إِلَهَ إِلاَّ أَنْتَ\n\n" +
            "উচ্চারণ:\n\n" +
            "সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, লা ইলাহা ইল্লা আনতা।\n\n" +
            "اَللَّهُمَّ اغْفِرْ لِيْ مَا أَسْرَرْتُ وَمَا أَعْلَنْتُ\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লাহুম্মাগ ফীরলী মা-আসররতু ওয়া মা-আ'লানতু।\n\n" +
            "اَللَّهُمَّ إنِّيْ أَعُوْذُ بِرِضَاكَ مِنْ سَخَطِكَ، وَأَعُوْذُ بِمُعَافَاتِكَ مِنْ عُقُوْبَتِكَ، وَأَعُوْذُ بِكَ مِنْكَ، لاَ أُحْصِيْ ثَنَاءً عَلَيْكَ، أَنْتَ كَمَا أَثْنَيْتَ عَلَى نَفْسِكَ\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লাহুম্মা ইন্নী আ'উজুবিরি-য-কা মিন সাখাতিকা, ওয়া আউজুবিমুআ-ফা-তিকা মিন উকুবাতিকা, ওয়া আউজুবিকা মিনকা, লা- উহসী ছানা-আন আলাইকা, আ'নতা কামা- আছনায়তা আলা নাফসিকা।\n\n" +
            "اَللَّهُمَّ لَكَ سَجَدْتُ، وَبِكَ آمَنْتُ، وَلَكَ أَسْلَمْتُ وَ أَنْتَ رَبِّىْ، سَجَدَ وَجْهِيَ لِلَّذِيْ خَلَقَهُ وَصَوَّرَهُ فَأَحْسَنَ صُوَرَهُ وَشَقَّ سَمْعَهُ وَبَصَرَهُ، فَتَبَارَكَ اللهُ أَحْسَنُ الْخَالِقِيْنَ\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লাহুম্মা লাকা সাজাদতু, ওয়া বিকা আমানতু, ওয়া লাকা আসলামতু, ওয়া আনতা রাব্বি। সাজাদা ওয়াজহিয়া লিল্লাযি খালাকাহু ওয়া সাওয়ারাহু ফা আহসানা সাওয়ারাহু, ওয়া শাক্কা সামআহু ওয়া বাসারাহু। ফা তাবারাকাল্লাহু আহসানুল খালিকীন।",
            "Sujood (prostration) is the best time for acceptance of supplications. Abu Hurairah (RA) narrated that the Messenger of Allah (ﷺ) said:\n\n" +
            "أَقْرَبُ مَا يَكُوْنُ الْعَبْدُ مِنْ رَّبِّهِ وَهُوَ سَاجِدٌ فَأَكْثِرُوا الدُّعَاءَ\n\n" +
            "Meaning:\n\n" +
            "\"The nearest a servant comes to his Lord is when he is prostrating, so increase your supplications.\"\n\n" +
            "In another narration: \"Exert your utmost in supplication, for it is most likely that you will be answered.\"\n\n" +
            "[Muslim, Mishkat H/894, Section 14]\n\n" +
            "He also said: \"Recite the Tasbih at least three times in Ruku and Sujood.\"\n\n" +
            "[Ibn Majah H/888]\n\n" +
            "Many supplications have been narrated in authentic Hadiths for Sujood. Among them, the most widely recited Dua is:\n\n" +
            "سُبْحَانَ رَبِّىَ الْأَعْلَى\n\n" +
            "Transliteration:\n\n" +
            "Subhana Rabbiyal A'la.\n\n" +
            "Meaning:\n\n" +
            "\"Glory be to my Lord, the Most High.\"\n\n" +
            "Recite this supplication three times; reciting more than three times is also permissible.\n\n" +
            "Other supplications of Sujood include:\n\n" +
            "اَللَّهُمَّ اغْفِرْ لِيْ ذَنْبِيْ كُلَّهُ دِقَّهُ وَجِلَّهُ وَ أَوَّلَهُ وَآخِرَهُ وَعَلاَنِيَتَهُ وَسِرَّهُ\n\n" +
            "Transliteration:\n\n" +
            "Allahummaghfirli dhanbee kullahu, diqqahu wa jillahu, wa awwalahu wa aakhirahu, wa 'alaniyatahu wa sirrahu.\n\n" +
            "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ لآ إِلَهَ إِلاَّ أَنْتَ\n\n" +
            "Transliteration:\n\n" +
            "Subhanaka Allahumma wa bihamdika, la ilaha illa Anta.\n\n" +
            "اَللَّهُمَّ اغْفِرْ لِيْ مَا أَسْرَرْتُ وَمَا أَعْلَنْتُ\n\n" +
            "Transliteration:\n\n" +
            "Allahummaghfirlee ma asrartu wa ma a'lantu.\n\n" +
            "اَللَّهُمَّ إنِّيْ أَعُوْذُ بِرِضَاكَ مِنْ سَخَطِكَ، وَأَعُوْذُ بِمُعَافَاتِكَ مِنْ عُقُوْبَتِكَ، وَأَعُوْذُ بِكَ مِنْكَ، لاَ أُحْصِيْ ثَنَاءً عَلَيْكَ، أَنْتَ كَمَا أَثْنَيْتَ عَلَى نَفْسِكَ\n\n" +
            "Transliteration:\n\n" +
            "Allahumma innee a'oodhu bi-ridhaaka min sakhatika, wa a'oodhu bi-mu'aafaatika min 'uqoobatika, wa a'oodhu bika minka, laa uhsee thanaa'an 'alayka, Anta kama athnayta 'ala nafsika.\n\n" +
            "اَللَّهُمَّ لَكَ سَجَدْتُ، وَبِكَ آمَنْتُ، وَلَكَ أَسْلَمْتُ وَ أَنْتَ رَبِّىْ، سَجَدَ وَجْهِيَ লِلَّذِيْ خَلَقَهُ وَصَوَّرَهُ فَأَحْسَنَ صُوَرَهُ وَشَقَّ سَمْعَهُ وَبَصَرَهُ، فَتَبَارَكَ اللهُ أَحْسَنُ الْخَالِقِيْنَ\n\n" +
            "Transliteration:\n\n" +
            "Allahumma laka sajadtu, wa bika aamantu, wa laka aslamtu, wa Anta Rabbee. Sajada wajhiya lilladhee khalaqahu wa sawwarahu fa-ahsana suwarahu, wa shaqqa sam'ahu wa basarahu. Fa-tabaarakallahu ahsanul-khaaliqeen."
        ));

        return list;
    }
}
