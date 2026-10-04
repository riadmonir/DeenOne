package com.devflux.deenone.data.repository;

import com.devflux.deenone.data.model.SixKalimaItem;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SixKalimaRepository {

    private static final List<SixKalimaItem> KALIMAS = new ArrayList<>();

    private static String generateArabicTtsUrl(String arabicText) {
        try {
            return "https://translate.google.com/translate_tts?ie=UTF-8&tl=ar&client=tw-ob&q=" +
                    URLEncoder.encode(arabicText, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return "";
        }
    }

    static {
        // 1. Kalima Tayyibah
        String ar1 = "لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ";
        KALIMAS.add(new SixKalimaItem(
                1,
                "১ম কালিমা: তাইয়্যেবা",
                "1. Kalima Tayyibah (Word of Purity)",
                "الكَلِمَةُ الطَّيِّبَة",
                ar1,
                "লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ।",
                "আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই, মুহাম্মদ ﷺ আল্লাহর প্রেরিত রাসূল।",
                "La ilaha illallahu Muhammadur Rasulullah.",
                "There is no god worthy of worship except Allah, and Muhammad (pbuh) is the Messenger of Allah.",
                "ইসলামের মূল ভিত্তি ও তাওহীদের চূড়ান্ত সাক্ষ্য। এই কালিমা অন্তরে বিশ্বাস ও মুখে স্বীকারের মাধ্যমে একজন মানুষ ইসলামে প্রবেশ করে।",
                generateArabicTtsUrl(ar1)
        ));

        // 2. Kalima Shahadat
        String ar2 = "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ";
        KALIMAS.add(new SixKalimaItem(
                2,
                "২য় কালিমা: শাহাদাত",
                "2. Kalima Shahadat (Word of Testimony)",
                "كَلِمَةُ الشَّهَادَة",
                ar2,
                "আশহাদু আল্লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসুলুহু।",
                "আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই। আমি আরও সাক্ষ্য দিচ্ছি যে, মুহাম্মদ ﷺ তাঁরই প্রিয় বান্দা ও প্রেরিত রাসূল।",
                "Ash-hadu alla ilaha illallahu wahdahu la shareeka lahu, wa ash-hadu anna Muhammadan 'abduhu wa rasooluhu.",
                "I bear witness that there is no god but Allah, the One, having no partner with Him, and I bear witness that Muhammad (pbuh) is His servant and Messenger.",
                "প্রতিটি ওজু ও আযানের পর এই কালিমা পাঠ করলে জান্নাতের আটটি দরজার যেকোনোটি দিয়ে প্রবেশের সুসংবাদ রয়েছে।",
                generateArabicTtsUrl(ar2)
        ));

        // 3. Kalima Tamjeed
        String ar3 = "سُبْحَانَ اللهِ وَالْحَمْدُ لِلّٰهِ وَلَا إِلٰهَ إِلَّا اللهُ وَاللهُ أَكْبَرُ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ";
        KALIMAS.add(new SixKalimaItem(
                3,
                "৩য় কালিমা: তামজীদ",
                "3. Kalima Tamjeed (Word of Glorification)",
                "كَلِمَةُ التَّمْجِيد",
                ar3,
                "সুবহানাল্লাহি ওয়াল হামদুলিল্লাহি ওয়া লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার, ওয়া লা হাওলা ওয়া লা কুওয়্যাতা ইল্লা বিল্লাহিল আলিয়্যিল আযীম।",
                "আল্লাহ অতি পবিত্র, সকল প্রশংসা আল্লাহর জন্য, আল্লাহ ব্যতীত কোনো উপাস্য নেই এবং আল্লাহ সর্বশ্রেষ্ঠ। মহান ও পরম পরাক্রমশালী আল্লাহর সাহায্য ব্যতীত পাপ থেকে বাঁচার কোনো উপায় এবং সৎকাজ করার কোনো শক্তি নেই।",
                "Subhanallahi walhamdulillahi wa la ilaha illallahu wallahu akbar, wa la hawla wa la quwwata illa billahil 'aliyyil 'azeem.",
                "Glory be to Allah, all praise is for Allah, there is no god but Allah, Allah is the Greatest, and there is no power nor strength except with Allah, the Most High, the Supreme.",
                "এটি জান্নাতের অন্যতম গুপ্তধন (কানযুম মিন কুনূযিল জান্নাহ)। নামাজের তাসবীহ ও শ্রেষ্ঠ জিকিরসমূহের অন্যতম।",
                generateArabicTtsUrl(ar3)
        ));

        // 4. Kalima Tawheed
        String ar4 = "لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ يُحْيِي وَيُمِيتُ وَهُوَ حَيٌّ لَا يَمُوتُ أَبَدًا أَبَدًا، ذُو الْجَلَالِ وَالْإِكْرَامِ، بِيَدِهِ الْخَيْرُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ";
        KALIMAS.add(new SixKalimaItem(
                4,
                "৪র্থ কালিমা: তাওহীদ",
                "4. Kalima Tawheed (Word of Oneness)",
                "كَلِمَةُ التَّوْحِيد",
                ar4,
                "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, লাহুল মুলকু ওয়া লাহুল হামদু য়্যুহয়ী ওয়া য়্যুমীতু ওয়া হুয়া হাইয়্যুল লা ইয়ামূতু আবাদান আবাদা, যুল জালালি ওয়াল ইকরাম, বিয়াদিহিল খাইরু ওয়া হুয়া আলা কুল্লি শাইয়িন ক্বদীর।",
                "আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই। রাজত্ব একমাত্র তাঁরই এবং সমস্ত প্রশংসাও তাঁর। তিনিই জীবন দেন এবং তিনিই মৃত্যু ঘটান। তিনি চিরঞ্জীব, কখনোই মৃত্যুবরণ করবেন না। তিনি অনন্ত মহিমাময় ও পরম সম্মানিত। সকল কল্যাণ একমাত্র তাঁরই হাতে এবং তিনি সর্ববিষয়ে পূর্ণ ক্ষমতাবান।",
                "La ilaha illallahu wahdahu la shareeka lahu, lahul-mulku wa lahul-hamdu yuhyee wa yumeetu wa huwa hayyun la yamootu abadan abada, dhul-jalali wal-ikram, biyadihil-khayru wa huwa 'ala kulli shay'in qadeer.",
                "There is no god but Allah alone, without partner. His is the sovereignty and His is the praise. He gives life and causes death, and He is Ever-Living, never dies. Possessor of Majesty and Honor. In His Hand is all goodness, and He is over all things capable.",
                "সকাল-সন্ধ্যায় ও বাজারে প্রবেশের সময় এই দোয়া পাঠে দশ লক্ষ নেকি লাভ ও দশ লক্ষ গুনাহ মাফ হওয়ার বিশেষ ফজিলত বর্ণিত আছে।",
                generateArabicTtsUrl(ar4)
        ));

        // 5. Kalima Radd-e-Kufr
        String ar5 = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا وَأَنَا أَعْلَمُ بِهِ، وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ بِهِ، تُبْتُ عَنْهُ وَتَبَرَّأْتُ مِنَ الْكُفْرِ وَالشِّرْكِ وَالْكِذْبِ وَالْغِيبَةِ وَالْبِدْعَةِ وَالنَّمِيمَةِ وَالْفَوَاحِشِ وَالْبُهْتَانِ وَالْمَعَاصِي كُلِّهَا، وَأَسْلَمْتُ وَأَقُولُ: لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ";
        KALIMAS.add(new SixKalimaItem(
                5,
                "৫ম কালিমা: রদ্দে কুফর",
                "5. Kalima Radd-e-Kufr (Rejection of Disbelief)",
                "كَلِمَةُ رَدِّ الْكُفْر",
                ar5,
                "আল্লাহুম্মা ইন্নী আ'ঊযু বিকা মিন আন উশরিকা বিকা শাইআন ওয়া আনা আ'লামু বিহী, ওয়া আসতাগফিরুকা লিমা লা আ'লামু বিহী, তুবতু 'আনহু ওয়া তাবাররা'তু মিনাল কুফরি ওয়াশ শিরকি ওয়াল কিযবি ওয়াল গীবাতী ওয়াল বিদ'আতী ওয়ান নামীমাতি ওয়াল ফাওয়াহিশি ওয়াল বুহতানি ওয়াল মা'আসী কুল্লিহা, ওয়া আসলামতু ওয়া আকূলু: লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ।",
                "হে আল্লাহ! নিশ্চয়ই আমি আপনার নিকট আশ্রয় প্রার্থনা করছি জানা অবস্থায় আপনার সাথে কাউকে শরিক করা হতে, আর অজানা অবস্থায় কোনো শিরক হয়ে থাকলে তার জন্য ক্ষমা চাচ্ছি। আমি তা হতে তওবা করলাম এবং কুফর, শিরক, মিথ্যা, পরনিন্দা (গীবত), বেদআত, চোগলখোরি, অশ্লীলতা, অপবাদ ও যাবতীয় পাপ থেকে নিজেকে মুক্ত ঘোষণা করলাম। আমি আনুগত্য স্বীকার করলাম এবং ঘোষণা করছি: আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই, মুহাম্মদ ﷺ আল্লাহর প্রেরিত রাসূল।",
                "Allahumma innee a'oodhu bika min an ushrika bika shay'an wa ana a'lamu bihi, wa astaghfiruka lima la a'lamu bih, tubtu 'anhu wa tabarra'tu minal-kufri wash-shirki wal-kidhbi wal-gheebati wal-bid'ati wan-nameemati wal-fawahishi wal-buhtani wal-ma'asi kulliha, wa aslamtu wa aqoolu: La ilaha illallahu Muhammadur Rasulullah.",
                "O Allah! I seek protection in You from that I should join any partner with You knowingly, and I seek Your forgiveness from that which I do not know. I repent from it and free myself from disbelief, polytheism, falsehood, backbiting, innovation, gossip, immoralities, slander and all sins. I submit to Your Will and declare: There is no god but Allah, Muhammad (pbuh) is the Messenger of Allah.",
                "শিরক ও কুফরির মারাত্মক গুনাহ থেকে আত্মরক্ষা ও অন্তরের ইখলাস বজায় রাখতে এটি অত্যন্ত প্রভাবশালী মাসনুন দোয়া।",
                generateArabicTtsUrl(ar5)
        ));

        // 6. Kalima Astaghfar
        String ar6 = "أَسْتَغْفِرُ اللهَ رَبِّي مِنْ كُلِّ ذَنْبٍ أَذْنَبْتُهُ عَمْدًا أَوْ خَطَأً، سِرًّا أَوْ عَلَانِيَةً، وَأَتُوبُ إِلَيْهِ مِنَ الذَّنْبِ الَّذِي أَعْلَمُ وَمِنَ الذَّنْبِ الَّذِي لَا أَعْلَمُ، إِنَّكَ أَنْتَ عَلَّامُ الْغُيُوبِ وَسَتَّارُ الْعُيُوبِ وَغَفَّارُ الذُّنُوبِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ";
        KALIMAS.add(new SixKalimaItem(
                6,
                "৬ষ্ঠ কালিমা: এস্তাগফার",
                "6. Kalima Astaghfar (Word of Forgiveness)",
                "كَلِمَةُ الاسْتِغْفَار",
                ar6,
                "আসতাগফিরুল্লাহা রাব্বী মিন কুল্লি যাম্বিন আযনাবতুহু 'আমাদান আও খাতাআন, সিররান আও 'আলানিয়াতান, ওয়া আতূবু ইলাইহি মিনায যামবিল্লাযী আ'লামু ওয়া মিনায যামবিল্লাযী লা আ'লামু, ইন্নাকা আনতা আল্লামুল গুয়ূবি ওয়া সাত্তারুল 'উয়ূবি ওয়া গাফফারুয যুনূবি, ওয়া লা হাওলা ওয়া লা কুওয়্যাতা ইল্লা বিল্লাহিল আলিয়্যিল আযীম।",
                "আমি আমার পালনকর্তা আল্লাহর নিকট ক্ষমা প্রার্থনা করছি আমার কৃত সকল পাপের জন্য—যা আমি ইচ্ছাকৃতভাবে করেছি কিংবা অনিচ্ছাকৃতভাবে, গোপনে করেছি কিংবা প্রকাশ্যে। আমি তাঁরই সমীপে তওবা করছি সেই পাপ হতে যা আমি জানি এবং সেই পাপ হতেও যা আমি জানি না। নিশ্চয়ই আপনি সকল অদৃশ্য বিষয় সম্পর্কে সম্যক অবগত, সকল ত্রুটি-বিচ্যুতি গোপনকারী এবং মহাপাপ ক্ষমাকারী। আর মহান আল্লাহর সাহায্য ছাড়া পাপ হতে বাঁচার কোনো উপায় এবং নেক কাজ করার কোনো শক্তি নেই।",
                "Astaghfirullaha Rabbee min kulli dhambin adhnabtuhu 'amadan aw khata'an, sirran aw 'alaniyatan, wa atoobu ilayhi minadh-dhambilladhee a'lamu wa minadh-dhambilladhee la a'lam, innaka Anta 'Allamul-ghuyoobi wa Sattarul-'uyoobi wa Ghaffarudh-dhunoob, wa la hawla wa la quwwata illa billahil 'aliyyil 'azeem.",
                "I seek forgiveness of Allah, my Lord, from every sin I committed knowingly or unknowingly, secretly or openly, and I turn towards Him in repentance from the sins that I know and from the sins that I do not know. Indeed, You are the Knower of the unseen, the Concealer of faults, and the Forgiver of sins; and there is no power and no strength except with Allah, the Most High, the Supreme.",
                "গুনাহ মাফের সর্বোত্তম মাধ্যম, অন্তরের কালিমা দূর করতে ও সর্বাবস্থায় আল্লাহর রহমতের ছায়ায় থাকতে প্রতিদিন নিয়মিত ইস্তেগফার পাঠ সুন্নাত।",
                generateArabicTtsUrl(ar6)
        ));
    }

    public static List<SixKalimaItem> getAllKalimas() {
        return Collections.unmodifiableList(KALIMAS);
    }

    public static SixKalimaItem getKalimaByNumber(int number) {
        if (number >= 1 && number <= KALIMAS.size()) {
            return KALIMAS.get(number - 1);
        }
        return KALIMAS.get(0);
    }
}
