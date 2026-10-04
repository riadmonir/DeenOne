package com.devflux.deenone.data.repository;

import com.devflux.deenone.features.salahguide.model.SalahStepItem;
import com.devflux.deenone.features.salahguide.model.SalahTopicItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalahGuideRepository {

    private static SalahGuideRepository instance;

    public static synchronized SalahGuideRepository getInstance() {
        if (instance == null) {
            instance = new SalahGuideRepository();
        }
        return instance;
    }

    public List<SalahTopicItem> getAllTopics() {
        return getAllTopics(true);
    }

    public List<SalahTopicItem> getAllTopics(boolean isBn) {
        return isBn ? getBengaliTopics() : getEnglishTopics();
    }

    private List<SalahTopicItem> getBengaliTopics() {
        List<SalahTopicItem> list = new ArrayList<>();

        // 1. Wudu Guide
        List<SalahStepItem> wuduSteps = new ArrayList<>();
        wuduSteps.add(new SalahStepItem("১", "নিয়ত ও বিসমিল্লাহ",
                "মনে মনে পবিত্রতা অর্জনের ইচ্ছা করে 'বিসমিল্লাহ' বলে ওজু শুরু করা।",
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "বিসমিল্লাহির রাহমানির রাহিম",
                "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।",
                "সুনান আবু দাউদ: ১০১"));
        wuduSteps.add(new SalahStepItem("২", "দুই হাত কবজি পর্যন্ত ধৌত করা",
                "উভয় হাত কবজি পর্যন্ত আঙ্গুলের ফাঁক দিয়ে খিলালসহ ৩ বার উত্তমরূপে ধোয়া।",
                "", "", "", "সহীহ বুখারী: ১৫৯, সহীহ মুসলিম: ২২৬"));
        wuduSteps.add(new SalahStepItem("৩", "কুলি ও মেসওয়াক করা",
                "ডান হাতে পানি নিয়ে ৩ বার মুখে দেওয়া ও মেসওয়াক বা আঙ্গুল দিয়ে দাঁত পরিষ্কার করা।",
                "", "", "", "সহীহ বুখারী: ১৯৩"));
        wuduSteps.add(new SalahStepItem("৪", "নাকে পানি দেওয়া ও ঝাড়া",
                "ডান হাতে পানি নাকে টেনে নিয়ে বাঁ হাত দিয়ে ৩ বার নাক পরিষ্কার করা।",
                "", "", "", "সহীহ বুখারী: ১৬১"));
        wuduSteps.add(new SalahStepItem("৫", "সম্পূর্ণ মুখমণ্ডল ধৌত করা",
                "কপালের চুলের গোড়া থেকে থুতনির নিচ এবং এক কানের লতি থেকে অন্য কানের লতি পর্যন্ত ৩ বার ধোয়া।",
                "", "", "", "সূরা আল-মায়িদাহ: ৬"));
        wuduSteps.add(new SalahStepItem("৬", "দুই হাত কনুইসহ ধৌত করা",
                "প্রথমে ডান হাত কনুইসহ ৩ বার, এরপর বাঁ হাত কনুইসহ ৩ বার ধোয়া।",
                "", "", "", "সহীহ বুখারী: ১৮৫"));
        wuduSteps.add(new SalahStepItem("৭", "মাথা ও কান মাসেহ করা",
                "নতুন পানি নিয়ে ভেজা হাত দিয়ে কপালের দিক থেকে ঘাড় পর্যন্ত নিয়ে গিয়ে আবার সামনে আনা এবং শাহাদাত ও বৃদ্ধাঙ্গুলি দিয়ে কান মাসেহ করা (১ বার)।",
                "", "", "", "সহীহ বুখারী: ১৮৫, সুনান আবু দাউদ: ১১৮"));
        wuduSteps.add(new SalahStepItem("৮", "দুই পা টাখনুসহ ধৌত করা",
                "ডান পা আঙ্গুল খিলালসহ গোড়ালি ও টাখনু পর্যন্ত ৩ বার ধোয়া, এরপর বাঁ পা ৩ বার ধোয়া।",
                "", "", "", "সহীহ মুসলিম: ২৪৬"));
        wuduSteps.add(new SalahStepItem("৯", "ওজু শেষের মাসনূন দোয়া",
                "ওজু শেষে কিবলার দিকে মুখ করে শাহাদাত ও এই দোয়া পড়া—এর ফলে জান্নাতের ৮টি দরজাই খুলে যায়।",
                "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ، اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ",
                "আশহাদু আল লা-ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসূলুহু। আল্লাহুম্মাজ আলনী মিনাত তাউয়্যাবীন, ওয়াজ আলনী মিনাল মুতাত্বহহিরীন।",
                "আমি সাক্ষ্য দিচ্ছি যে, এক আল্লাহ ছাড়া কোনো মাবুদ নেই, তাঁর কোনো অংশীদার নেই এবং মুহাম্মাদ (ﷺ) তাঁর বান্দা ও রাসূল। হে আল্লাহ! আমাকে তাওবাকারীদের এবং পবিত্রতা অর্জনকারীদের অন্তর্ভুক্ত করুন।",
                "সহীহ মুসলিম: ২৩৪, জামে আত-তিরমিযী: ৫৫"));

        list.add(new SalahTopicItem("wudu", "পরিপূর্ণ ওজু শিক্ষা",
                "ওজুর ধারাবাহিক ধাপ, সুন্নত ও সহীহ দোয়া", "purity",
                "ফরজ (সালাতের শর্ত)", "৮টি সুন্নাত ধাপ", "প্রতি সালাতের পূর্বে ওজু নষ্ট হলে",
                "ওজু হলো সালাতের চাবি। পবিত্রতা ব্যতীত কোনো সালাত কবুল হয় না। রাসূলুল্লাহ (ﷺ) যেভাবে ওজু করতেন তার ধারাবাহিক সহীহ নির্দেশিকা।",
                wuduSteps));

        // 2. Prerequisites of Salah
        List<SalahStepItem> prereqSteps = new ArrayList<>();
        prereqSteps.add(new SalahStepItem("১", "নামাজের বাইরের ৭ ফরজ (আহকাম)",
                "১. শরীর পাক হওয়া\n২. কাপড় পাক হওয়া\n৩. নামাজের জায়গা পাক হওয়া\n৪. সতর ঢাকা (পুরুষ: নাভি থেকে হাঁটু নিচ পর্যন্ত, নারী: মুখমণ্ডল, দুই হাতের কবজি ও পায়ের পাতা ছাড়া সমগ্র শরীর)\n৫. কিবলামুখী হওয়া\n৬. ওয়াক্তমতো নামাজ পড়া\n৭. নামাজের নিয়ত করা।",
                "", "", "", "ফিকহুস সুন্নাহ, সহীহ বুখারী ও মুসলিম"));
        prereqSteps.add(new SalahStepItem("২", "নামাজের ভেতরের ৬ ফরজ (আরকান)",
                "১. তাকবীরে তাহরীমা ('আল্লাহু আকবার' বলে নামাজ শুরু করা)\n২. কিয়াম (দাঁড়িয়ে নামাজ পড়া)\n৩. কিরাত (কুরআনের আয়াত তেলাওয়াত করা)\n৪. রুকূ করা\n৫. সিজদা করা (উভয় সিজদা)\n৬. শেষ বৈঠক (তাশাহহুদ পরিমাণ বসা)।",
                "", "", "", "সহীহ বুখারী: ৭৫৭, সহীহ মুসলিম: ৩৯৭"));

        list.add(new SalahTopicItem("prerequisites", "নামাজের পূর্বশর্ত ও আরকান-আহকাম",
                "নামাজের বাইরের ৭ ফরজ ও ভেতরের ৬ ফরজ", "purity",
                "ফরজ আইন", "১৩টি ফরজ", "নামাজ শুরুর পূর্বে ও চলাকালীন",
                "নামাজ শুদ্ধ হওয়ার জন্য এই ১৩টি ফরজ অবশ্যই পালন করতে হবে। এর কোনো একটি ছুটে গেলে নামাজ বাতিল বলে গণ্য হবে।",
                prereqSteps));

        // 3. Step-by-Step Salah Actions & Recitations
        List<SalahStepItem> stepRecitations = new ArrayList<>();
        stepRecitations.add(new SalahStepItem("১", "তাকবীরে তাহরীমা ও নিয়ত",
                "মনে মনে নির্দিষ্ট সালাতের সংকল্প করে দুই হাত কান পর্যন্ত (মহিলাগণ কাঁধ পর্যন্ত) উঠিয়ে 'আল্লাহু আকবার' বলে নাভির উপর (বা বুকের উপর) হাত বাঁধা।",
                "اللَّهُ أَكْبَرُ", "আল্লাহু আকবার", "আল্লাহ মহান।", "সহীহ বুখারী: ৭৩৮"));
        stepRecitations.add(new SalahStepItem("২", "ছানা (দোয়ায়ে ইস্তেফতাহ)",
                "হাত বাঁধার পর প্রথম রাকাতে কিরাতের পূর্বে পাঠ করা সুন্নাত।",
                "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَىٰ جَدُّكَ، وَلَا إِلٰهَ غَيْرُكَ",
                "সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়া লা ইলাহা গাইরুক।",
                "হে আল্লাহ! আপনার প্রশংসার সাথে পবিত্রতা ঘোষণা করছি, আপনার নাম বরকতময়, আপনার মর্যাদা অতি উচ্চ এবং আপনি ছাড়া সত্য কোনো উপাস্য নেই।",
                "সুনান আবু দাউদ: ৭৭৫, জামে আত-তিরমিযী: ২৪৩"));
        stepRecitations.add(new SalahStepItem("৩", "তাআউউয, তাসমিয়া ও সূরা ফাতিহা",
                "আউযু বিল্লাহ ও বিসমিল্লাহ পড়ে সূরা আল-ফাতিহা তিলাওয়াত করা প্রত্যেক রাকাতে ফরজ। এরপর 'আমীন' বলা।",
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ • الرَّحْمَٰنِ الرَّحِيمِ • مَالِكِ يَوْمِ الدِّينِ • إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ • اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ • صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                "আলহামদু লিল্লাহি রাব্বিল আলামীন। আর-রাহমানির রাহীম। মালিকি ইয়াউমিদ্দীন। ইয়্যাকা না'বুদু ওয়া ইয়্যাকা নাসতা'ঈন। ইহদিনাস সিরাতাল মুস্তাকীম। সিরাতাল্লাযীনা আন'আমতা আলাইহিম, গাইরিল মাগদূবি আলাইহিম ওয়ালাদ দোয়াল্লীন।",
                "সকল প্রশংসা বিশ্বজাহানের প্রতিপালক আল্লাহর। যিনি পরম করুণাময়, অতি দয়ালু। বিচার দিবসের মালিক। আমরা কেবল আপনারই ইবাদত করি এবং কেবল আপনারই সাহায্য চাই। আমাদের সরল পথ দেখান। তাদের পথ যাদের আপনি অনুগ্রহ করেছেন, তাদের পথ নয় যারা অভিশপ্ত ও পথভ্রষ্ট।",
                "সহীহ বুখারী: ৭৫৬"));
        stepRecitations.add(new SalahStepItem("৪", "রুকূ ও রুকূর তাসবীহ",
                "'আল্লাহু আকবার' বলে মাথা ও পিঠ সমান করে দুই হাত দিয়ে হাঁটু শক্ত করে ধরা এবং শান্তভাবে কমপক্ষে ৩ বার পাঠ করা।",
                "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
                "সুবহানা রাব্বিয়াল আজীম",
                "আমার মহান রবের পবিত্রতা বর্ণনা করছি।",
                "সহীহ মুসলিম: ৭৭২"));
        stepRecitations.add(new SalahStepItem("৫", "কওমা ও তাহমীদ (রুকূ থেকে সোজা হওয়া)",
                "রুকূ থেকে সোজা হয়ে দাঁড়ানোর সময় ইমাম ও একাকী পড়লে 'সামিআল্লাহু লিমান হামিদাহ' বলবে এবং মুক্তাদীসহ সবাই বলবে 'রব্বানা ওয়া লাকাল হামদ'।",
                "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ • رَبَّنَا وَلَكَ الْحَمْدُ حَمْدًا كَثِيرًا طَيِّبًا مُبَارَكًا فِيهِ",
                "সামি'আল্লাহু লিমান হামিদাহ। রাব্বানা ওয়া লাকাল হামদু, হামদান কাসীরান তাইয়িবান মুবারাকান ফীহ।",
                "আল্লাহ শুনলেন যে তাঁর প্রশংসা করল। হে আমাদের রব! আপনার জন্যই সমস্ত প্রশংসা—অগণিত, পবিত্র ও বরকতময় প্রশংসা।",
                "সহীহ বুখারী: ৭৯৯"));
        stepRecitations.add(new SalahStepItem("৬", "সিজদা ও সিজদার তাসবীহ",
                "'আল্লাহু আকবার' বলে সাতটি অঙ্গের উপর (কপাল ও নাক, দুই হাত, দুই হাঁটু, দুই পায়ের আঙ্গুল) সিজদা করা এবং কমপক্ষে ৩ বার তাসবীহ পড়া।",
                "سُبْحَانَ رَبِّيَ الْأَعْلَىٰ",
                "সুবহানা রাব্বিয়াল আ'লা",
                "আমার পরম উচ্চ মর্যাদাশীল রবের পবিত্রতা ঘোষণা করছি।",
                "সহীহ মুসলিম: ৭৭২"));
        stepRecitations.add(new SalahStepItem("৭", "দুই সিজদার মাঝের বৈঠক ও দোয়া",
                "প্রথম সিজদা থেকে উঠে স্থির হয়ে সোজা হয়ে বসা এবং দোয়া করা।",
                "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي، اللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي",
                "রাব্বিগফির লী, রাব্বিগফির লী। আল্লাহুম্মাগফির লী, ওয়ারহামনী, ওয়াহদিনী, ওয়া আফিনী, ওয়ারযুক্বনী।",
                "হে আমার রব! আমাকে ক্ষমা করুন, আমাকে ক্ষমা করুন। হে আল্লাহ! আমাকে ক্ষমা করুন, দয়া করুন, হিদায়াত দিন, সুস্থতা দান করুন এবং জীবিকা দান করুন।",
                "সুনান আবু দাউদ: ৮৫০, জামে আত-তিরমিযী: ২৮৪"));
        stepRecitations.add(new SalahStepItem("৮", "তাশাহহুদ (আত্তাহিয়্যাতু)",
                "২ রাকাত পর এবং শেষ বৈঠকে বসে শান্ত মনে তাশাহহুদ পাঠ করা ওয়াজিব। 'আশহাদু আল লা ইলাহা' বলার সময় শাহাদাত আঙ্গুল তোলা।",
                "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
                "আত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু ওয়াত-ত্বায়্যিবাতু, আস-সালামু আলাইকা আইয়্যুহান নাবিয়্যু ওয়া রাহমাতুল্লাহি ওয়া বারাকাতুহু, আস-সালামু আলাইনা ওয়া আলা ইবাদিল্লাহিস সালিহীন। আশহাদু আল লা-ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসূলুহু।",
                "সমস্ত মৌখিক, শারীরিক ও আর্থিক ইবাদত আল্লাহর জন্য। হে নবী! আপনার প্রতি শান্তি, আল্লাহর রহমত ও বরকত বর্ষিত হোক। আমাদের প্রতি এবং আল্লাহর নেক বান্দাদের প্রতিও শান্তি বর্ষিত হোক। আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ ছাড়া সত্য কোনো উপাস্য নেই এবং মুহাম্মাদ (ﷺ) তাঁর বান্দা ও রাসূল।",
                "সহীহ বুখারী: ৮৩১, সহীহ মুসলিম: ৪০২"));
        stepRecitations.add(new SalahStepItem("৯", "দরূদে ইব্রাহিম",
                "শেষ বৈঠকে তাশাহহুদের পর দরূদ পাঠ করা সুন্নাতে মুয়াক্কাদা/ফরজ।",
                "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
                "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিঁও ওয়া আলা আলি মুহাম্মাদ, কামা সাল্লাইতা আলা ইবরাহীমা ওয়া আলা আলি ইবরাহীম, ইন্নাকা হামীদুম মাজীদ। আল্লাহুম্মা বারিক আলা মুহাম্মাদিঁও ওয়া আলা আলি মুহাম্মাদ, কামা বারাকতা আলা ইবরাহীমা ওয়া আলা আলি ইবরাহীম, ইন্নাকা হামীদুম মাজীদ।",
                "হে আল্লাহ! মুহাম্মাদ (ﷺ) এবং তাঁর পরিবারের উপর রহমত বর্ষণ করুন, যেমন আপনি ইব্রাহিম (আঃ) ও তাঁর পরিবারের উপর করেছিলেন। নিশ্চয় আপনি প্রশংসিত ও মহিমান্বিত। হে আল্লাহ! মুহাম্মাদ (ﷺ) এবং তাঁর পরিবারের উপর বরকত দান করুন, যেমন আপনি ইব্রাহিম (আঃ) ও তাঁর পরিবারের উপর করেছিলেন। নিশ্চয় আপনি প্রশংসিত ও মহিমান্বিত।",
                "সহীহ বুখারী: ৩৩৭০, সহীহ মুসলিম: ৪০৫"));
        stepRecitations.add(new SalahStepItem("১০", "দোআয়ে মাসূরা",
                "দরূদের পর সালাম ফেরানোর পূর্বে জাহান্নাম, কবর ও ফিতনা থেকে মুক্তির মাসনূন দোয়া।",
                "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا، وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
                "আল্লাহুম্মা ইন্নী যালামতু নাফসী যুলমান কাসীরা, ওয়ালা ইয়াগফিরুয যুনূবা ইল্লা আনতা, ফাগফির লী মাগফিরাতাম মিন ইনদিকা ওয়ারহামনী, ইন্নাকা আনতাল গাফুরুর রাহীম।",
                "হে আল্লাহ! আমি আমার নিজের উপর অনেক জুলুম করেছি। আর আপনি ছাড়া গুনাহ মাফ করার কেউ নেই। অতএব আপনার পক্ষ থেকে আমাকে বিশেষ ক্ষমা দান করুন এবং আমার প্রতি দয়া করুন। নিশ্চয় আপনি ক্ষমাশীল ও পরম দয়ালু।",
                "সহীহ বুখারী: ৮৩৪, সহীহ মুসলিম: ২৭০৫"));
        stepRecitations.add(new SalahStepItem("১১", "সালাম ফেরানো",
                "প্রথমে ডান কাঁধে মুখ ফিরিয়ে 'আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ' বলা, এরপর বাঁ কাঁধে মুখ ফিরিয়ে বলা। এর মাধ্যমে সালাত সম্পন্ন হয়।",
                "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
                "আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ",
                "আপনার উপর আল্লাহর শান্তি ও তাঁর রহমত বর্ষিত হোক।",
                "সহীহ মুসলিম: ৫৮২"));

        list.add(new SalahTopicItem("steps", "সালাতের ধারাবাহিক রুকন ও তাসবীহ",
                "তাকবীর থেকে সালাম পর্যন্ত ১১টি রুকন, বিশুদ্ধ আরবি, উচ্চারণ ও অর্থ", "steps",
                "আরকান ও ওয়াজিবাত", "১১টি ধারাবাহিক রুকন", "সকল নামাজে প্রযোজ্য",
                "রাসূলুল্লাহ (ﷺ) বলেছেন: 'তোমরা সেভাবে সালাত আদায় করো যেভাবে আমাকে সালাত আদায় করতে দেখেছ।' (সহীহ বুখারী: ৬৩১)। প্রতিটি রুকনের সঠিক উচ্চারণ ও অর্থ নিচে দেওয়া হলো।",
                stepRecitations));

        // 4. Fajr Prayer
        List<SalahStepItem> fajrSteps = new ArrayList<>();
        fajrSteps.add(new SalahStepItem("১", "২ রাকাত সুন্নাত (সুন্নাতে মুয়াক্কাদা)",
                "ফজরের ফরজ নামাজের পূর্বে ২ রাকাত সুন্নাত অত্যন্ত গুরুত্বপূর্ণ। রাসূলুল্লাহ (ﷺ) কখনো এই ২ রাকাত ছাড়তেন না। প্রথম রাকাতে সূরা কাফিরুন ও দ্বিতীয় রাকাতে সূরা ইখলাস পড়া সুন্নাত।",
                "", "", "", "সহীহ মুসলিম: ৭২৪"));
        fajrSteps.add(new SalahStepItem("২", "২ রাকাত ফরজ",
                "জামাতে বা একাকী ইমামের পেছনে জাহরী কিরাতে (উচ্চস্বরে) সূরা ফাতিহা ও কিরাত তিলাওয়াতসহ ২ রাকাত সম্পন্ন করা।",
                "", "", "", "সহীহ বুখারী: ৭৭৬"));
        list.add(new SalahTopicItem("fajr", "ফজর নামাজ",
                "২ রাকাত সুন্নাত + ২ রাকাত ফরজ", "fardh",
                "ফরজ আইন", "মোট ৪ রাকাত (২ সুন্নাত + ২ ফরজ)", "সুবহে সাদিক থেকে সূর্যোদয়ের পূর্ব পর্যন্ত",
                "ফজরের সালাত দিনের শুরুতেই মুমিনের অন্তরে নূর ও বরকত প্রবেশ করায়। মুনাফিকের জন্য ফজর ও এশার সালাত সবচেয়ে ভারী।",
                fajrSteps));

        // 5. Dhuhr Prayer
        List<SalahStepItem> dhuhrSteps = new ArrayList<>();
        dhuhrSteps.add(new SalahStepItem("১", "৪ রাকাত সুন্নাতে মুয়াক্কাদা", "ফরজের পূর্বে ৪ রাকাত এক সালামে পড়া।", "", "", "", "সহীহ মুসলিম: ৭৩০"));
        dhuhrSteps.add(new SalahStepItem("২", "৪ রাকাত ফরজ", "সিররি কিরাতে (নিঃশব্দে) ৪ রাকাত ফরজ নামাজ আদায় করা।", "", "", "", "সহীহ বুখারী: ৭৫৯"));
        dhuhrSteps.add(new SalahStepItem("৩", "২ রাকাত সুন্নাতে মুয়াক্কাদা", "ফরজের পর ২ রাকাত সুন্নাত নামাজ আদায় করা।", "", "", "", "সহীহ বুখারী: ৯৩৭"));
        dhuhrSteps.add(new SalahStepItem("৪", "২ রাকাত নফল (ঐচ্ছিক)", "সুন্নাতের পর অতিরিক্ত ২ রাকাত নফল সালাত আদায় করা উত্তম।", "", "", "", "জামে আত-তিরমিযী: ৪২৮"));
        list.add(new SalahTopicItem("dhuhr", "যোহর নামাজ",
                "৪ সুন্নাত + ৪ ফরজ + ২ সুন্নাত + ২ নফল", "fardh",
                "ফরজ আইন", "মোট ১২ রাকাত (৪ সুন্নাত + ৪ ফরজ + ২ সুন্নাত + ২ নফল)", "দুপুরে সূর্য পশ্চিমাকাশে ঢলে পড়ার পর থেকে আসরের ওয়াক্তের পূর্ব পর্যন্ত",
                "যোহরের সালাত ব্যস্ত দিনের মাঝে প্রশান্তি ও তাকওয়া এনে দেয়।",
                dhuhrSteps));

        // 6. Asr Prayer
        List<SalahStepItem> asrSteps = new ArrayList<>();
        asrSteps.add(new SalahStepItem("১", "৪ রাকাত গায়রে মুয়াক্কাদা সুন্নাত (ঐচ্ছিক)", "ফরজের পূর্বে ৪ রাকাত পড়া মুস্তাহাব। রাসূলুল্লাহ (ﷺ) এর জন্য রহমতের দোয়া করেছেন।", "", "", "", "সুনান আবু দাউদ: ১২৭১"));
        asrSteps.add(new SalahStepItem("২", "৪ রাকাত ফরজ", "সিররি কিরাতে (নিঃশব্দে) ৪ রাকাত ফরজ নামাজ আদায় করা। আসরের পর সূর্যাস্ত পর্যন্ত কোনো নফল নামাজ নেই।", "", "", "", "সহীহ বুখারী: ৫৫৩"));
        list.add(new SalahTopicItem("asr", "আসর নামাজ",
                "৪ সুন্নাত (নফল) + ৪ রাকাত ফরজ", "fardh",
                "ফরজ আইন", "মোট ৮ রাকাত (৪ সুন্নাত + ৪ ফরজ)", "বস্তুর ছায়া দ্বিগুণের পর থেকে সূর্যাস্তের পূর্ব পর্যন্ত",
                "কুরআনে আসরের নামাজকে মধ্যবর্তী নামাজ হিসেবে বিশেষভাবে তাগিদ দেওয়া হয়েছে (সূরা আল-বাক্বারাহ: ২৩৮)।",
                asrSteps));

        // 7. Maghrib Prayer
        List<SalahStepItem> maghribSteps = new ArrayList<>();
        maghribSteps.add(new SalahStepItem("১", "৩ রাকাত ফরজ", "প্রথম ২ রাকাতে উচ্চস্বরে (জাহরী) এবং ৩য় রাকাতে নিঃশব্দে সূরা ফাতিহাসহ ৩ রাকাত আদায় করা।", "", "", "", "সহীহ বুখারী: ৭৬১"));
        maghribSteps.add(new SalahStepItem("২", "২ রাকাত সুন্নাতে মুয়াক্কাদা", "ফরজের পর ২ রাকাত সুন্নাত আদায় করা।", "", "", "", "সহীহ মুসলিম: ৭২৮"));
        maghribSteps.add(new SalahStepItem("৩", "২ বা ৬ রাকাত নফল (আউয়াবীন)", "মাগরিবের পর নফল সালাত অত্যন্ত সাওয়াবের কাজ।", "", "", "", "জামে আত-তিরমিযী: ৪৩৫"));
        list.add(new SalahTopicItem("maghrib", "মাগরিব নামাজ",
                "৩ রাকাত ফরজ + ২ রাকাত সুন্নাত + ২ নফল", "fardh",
                "ফরজ আইন", "মোট ৭ রাকাত (৩ ফরজ + ২ সুন্নাত + ২ নফল)", "সূর্যাস্তের পর থেকে পশ্চিমাকাশের লাল আভা বিলীন হওয়া পর্যন্ত",
                "মাগরিবের সালাত সূর্যাস্তের পরপরই দ্রুত আদায় করা সুন্নাত।",
                maghribSteps));

        // 8. Isha Prayer
        List<SalahStepItem> ishaSteps = new ArrayList<>();
        ishaSteps.add(new SalahStepItem("১", "৪ রাকাত সুন্নাত (গায়রে মুয়াক্কাদা)", "ফরজের পূর্বে ৪ রাকাত পড়া উত্তম।", "", "", "", "ফিকহুস সুন্নাহ"));
        ishaSteps.add(new SalahStepItem("২", "৪ রাকাত ফরজ", "প্রথম ২ রাকাতে উচ্চস্বরে কিরাতসহ ৪ রাকাত ফরজ আদায় করা।", "", "", "", "সহীহ বুখারী: ৭৬২"));
        ishaSteps.add(new SalahStepItem("৩", "২ রাকাত সুন্নাতে মুয়াক্কাদা", "ফরজের পর ২ রাকাত সুন্নাত।", "", "", "", "সহীহ বুখারী: ৯৩৭"));
        ishaSteps.add(new SalahStepItem("৪", "২ রাকাত নফল", "সুন্নাতের পর ২ রাকাত নফল।", "", "", "", "মুসান্নাফ ইবনে আবি শাইবাহ"));
        list.add(new SalahTopicItem("isha", "এশা নামাজ",
                "৪ সুন্নাত + ৪ ফরজ + ২ সুন্নাত + ২ নফল", "fardh",
                "ফরজ আইন", "মোট ১২ রাকাত (৪ সুন্নাত + ৪ ফরজ + ২ সুন্নাত + ২ নফল)", "মাগরিবের ওয়াক্ত শেষ থেকে মধ্যরাত বা সুবহে সাদিকের পূর্ব পর্যন্ত",
                "এশার সালাত জামাতে আদায় করা সারা রাত ইবাদত করার সমতুল্য (সহীহ মুসলিম: ৬৫৬)।",
                ishaSteps));

        // 9. Witr Prayer & Dua Qunut
        List<SalahStepItem> witrSteps = new ArrayList<>();
        witrSteps.add(new SalahStepItem("১", "বিতরের নিয়ম ও ৩ রাকাত আদায়",
                "এশার পর অথবা রাতের শেষভাগে ৩ রাকাত এক সালামে বা ২+১ রাকাত হিসেবে আদায় করা। ৩য় রাকাতে সূরা ফাতিহা ও অন্য সূরা মিলানোর পর রুকূর পূর্বে 'আল্লাহু আকবার' বলে হাত উঠিয়ে পুনরায় হাত বেঁধে দোয়া কুনূত পড়তে হয়।",
                "", "", "", "সহীহ বুখারী: ৯৯৪, সুনান আবু দাউদ: ১৪২৪"));
        witrSteps.add(new SalahStepItem("২", "দোয়ায়ে কুনূত",
                "বিতর সালাতের ৩য় রাকাতে হাত বেঁধে এই দোয়া পড়া ওয়াজিব বা সুন্নাত।",
                "اللَّهُمَّ إِنَّا نَسْتَعِينُكَ وَنَسْتَغْفِرُكَ، وَنُؤْمِنُ بِكَ وَنَتَوَكَّلُ عَلَيْكَ، وَنُثْنِي عَلَيْكَ الْخَيْرَ، وَنَشْكُرُكَ وَلَا نَكْفُرُكَ، وَنَخْلَعُ وَنَتْرُكُ مَنْ يَفْجُرُكَ، اللَّهُمَّ إِيَّاكَ نَعْبُدُ، وَلَكَ نُصَلِّي وَنَسْجُدُ، وَإِلَيْكَ نَسْعَىٰ وَنَحْفِدُ، نَرْجُو رَحْمَتَكَ وَنَخْشَىٰ عَذَابَكَ، إِنَّ عَذَابَكَ بِالْكُفَّارِ مُلْحَقٌ",
                "আল্লাহুম্মা ইন্না নাসতা'ঈনুকা ওয়া নাসতাগফিরুকা, ওয়া নু'মিনু বিকা ওয়া নাতাওয়াক্কালু আলাইকা, ওয়া নুছনী আলাইকাল খাইর, ওয়া নাশকুরুকা ওয়ালা নাকফুরুকা, ওয়া নাখলা'উ ওয়া নাতরুকু মাইঁ ইয়াফজুরুক। আল্লাহুম্মা ইয়্যাকা না'বুদু, ওয়া লাকা নুসল্লী ওয়া নাসজুদ, ওয়া ইলাইকা নাস'আ ওয়া নাহফিদ, নারজু রাহমাতাকা ওয়া নাখশা আযাবাক, ইন্না আযাবাকা বিল কুফফারি মুলহিক্ব।",
                "হে আল্লাহ! আমরা আপনারই সাহায্য প্রার্থনা করছি, আপনারই নিকট ক্ষমা চাচ্ছি, আপনার উপর ঈমান আনছি, আপনারই উপর ভরসা করছি, আপনার উত্তম প্রশংসা করছি। আমরা আপনার কৃতজ্ঞতা প্রকাশ করি, অকৃতজ্ঞ হই না। আর যে আপনার অবাধ্য হয় তাকে আমরা বর্জন ও পরিত্যাগ করি। হে আল্লাহ! আমরা কেবল আপনারই ইবাদত করি, আপনার জন্যই নামাজ পড়ি ও সিজদা করি। আপনার দিকেই ধাবিত হই এবং আনুগত্যে প্রস্তুত থাকি। আমরা আপনার রহমতের আশা করি এবং আপনার শাস্তিকে ভয় করি। নিশ্চয় আপনার শাস্তি কাফিরদের ওপর আপতিত হবে।",
                "সুনান বায়হাকী: ২/২১১, সুনান ইবনে মাজাহ: ১১৭৮"));

        list.add(new SalahTopicItem("witr", "বিতর নামাজ ও দোয়া কুনূত",
                "৩ রাকাত ওয়াজিব বিতর ও পূর্ণাঙ্গ দোয়া কুনূত", "sunnah_wajib",
                "ওয়াজিব", "৩ রাকাত", "এশার পর থেকে সুবহে সাদিকের পূর্ব পর্যন্ত",
                "রাসূলুল্লাহ (ﷺ) বলেছেন: 'নিশ্চয় আল্লাহ বিতর (বেজোড়), তিনি বিতরকে ভালোবাসেন। অতএব হে কুরআনের অনুসারীগণ! তোমরা বিতর নামাজ পড়।' (সুনান আবু দাউদ: ১৪১৬)।",
                witrSteps));

        // 10. Daily Sunnah Prayers
        List<SalahStepItem> sunnahSteps = new ArrayList<>();
        sunnahSteps.add(new SalahStepItem("১", "দৈনিক ১২ রাকাত সুন্নাত",
                "• ফজরের পূর্বে ২ রাকাত\n• যোহরের পূর্বে ৪ রাকাত ও পরে ২ রাকাত\n• মাগরিবের পরে ২ রাকাত\n• এশার পরে ২ রাকাত।",
                "مَنْ صَلَّىٰ فِي يَوْمٍ وَلَيْلَةٍ ثِنْتَيْ عَشْرَةَ رَكْعَةً بُنِيَ لَهُ بَيْتٌ فِي الْجَنَّةِ",
                "মান সাল্লা ফী ইয়াওমিঁও ওয়া লাইলাতিন সিনতাই আশরাতা রাক'আতান বুনিয়া লাহু বাইতুন ফিল জান্নাহ।",
                "যে ব্যক্তি দিনে-রাতে ১২ রাকাত সুন্নাত সালাত আদায় করবে, তার জন্য জান্নাতে একটি প্রাসাদ নির্মাণ করা হবে।",
                "সহীহ মুসলিম: ৭২৮"));
        list.add(new SalahTopicItem("sunnah", "সুন্নাতে মুয়াক্কাদা",
                "প্রতিদিনের ১২ রাকাত সুন্নাতের তালিকা ও ফজিলত", "sunnah_wajib",
                "সুন্নাতে মুয়াক্কাদা", "১২ রাকাত", "প্রতি ওয়াক্তের সাথে নির্ধারিত",
                "সুন্নাতে মুয়াক্কাদা সালাত রাসূলুল্লাহ (ﷺ) নিয়মিত আদায় করতেন এবং বিনা ওজরে ত্যাগ করা গুনাহের শামিল।",
                sunnahSteps));

        // 11. Nafl Prayers
        List<SalahStepItem> naflSteps = new ArrayList<>();
        naflSteps.add(new SalahStepItem("১", "সালাতুত তাহাজ্জুদ",
                "রাতের শেষ তৃতীয়াংশে ঘুম থেকে উঠে ২ থেকে ১২ রাকাত পর্যন্ত নফল সালাত। রাসূলুল্লাহ (ﷺ) সাধারণত ৮ রাকাত তাহাজ্জুদ ও ৩ রাকাত বিতর পড়তেন।",
                "", "", "", "সহীহ বুখারী: ১১২৯, সূরা আল-ইসরা: ৭৯"));
        naflSteps.add(new SalahStepItem("২", "সালাতুত দুহা (চাশত ও ইশরাক)",
                "সূর্যোদয়ের ১৫-২০ মিনিট পর থেকে দুপুরের পূর্ব পর্যন্ত ২ থেকে ৮ রাকাত পর্যন্ত। এটি প্রতিদিনের ৩৬০টি জোড়ের সদকা হিসেবে গণ্য হয়।",
                "", "", "", "সহীহ মুসলিম: ৭২০"));
        naflSteps.add(new SalahStepItem("৩", "সালাতুল ইস্তিখারা ও দোয়া",
                "কোনো গুরুত্বপূর্ণ সিদ্ধান্তের ক্ষেত্রে ২ রাকাত নফল নামাজ পড়ে সহীহ বুখারির মাসনূন দোয়া পাঠ করা।",
                "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ...",
                "আল্লাহুম্মা ইন্নী আসতাখীরুকা বিইলমিকা...",
                "হে আল্লাহ! আমি আপনার জ্ঞানের সাহায্যে শুভ পরিণতি প্রার্থনা করছি...",
                "সহীহ বুখারী: ১১৬২"));
        naflSteps.add(new SalahStepItem("৪", "সালাতুত তাসবীহ",
                "৪ রাকাতের বিশেষ নফল সালাত যেখানে প্রতি রাকাতে ৭৫ বার করে মোট ৩০০ বার তাসবীহ পাঠ করা হয়। এর দ্বারা পূর্বাপর গুনাহ মাফ হয়।",
                "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَلَا إِلٰهَ إِلَّا اللَّهُ وَاللَّهُ أَكْبَرُ",
                "সুবহানাল্লাহি ওয়াল হামদুলিল্লাহি ওয়া লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার।",
                "আল্লাহ পবিত্র, সমস্ত প্রশংসা আল্লাহর, আল্লাহ ছাড়া কোনো উপাস্য নেই এবং আল্লাহ সর্বশ্রেষ্ঠ।",
                "সুনান আবু দাউদ: ১২৯৭, জামে আত-তিরমিযী: ৪৮১"));

        list.add(new SalahTopicItem("nafl", "নফল ও বিশেষ সালাত",
                "তাহাজ্জুদ, ইশরাক, চাশত, ইস্তিখারা ও সালাতুত তাসবীহ", "nafl_janazah",
                "নফল বা মুস্তাহাব", "২ থেকে ১২ রাকাত", "বিভিন্ন বরকতময় সময়",
                "নফল সালাতের মাধ্যমে বান্দা আল্লাহর সবচেয়ে নিকটবর্তী হতে পারে এবং কেয়ামতের দিন ফরজ সালাতের ঘাটতি পূরণ করা হবে।",
                naflSteps));

        // 12. Janazah Prayer
        List<SalahStepItem> janazahSteps = new ArrayList<>();
        janazahSteps.add(new SalahStepItem("১", "১ম তাকবীর ও ছানা বা ফাতিহা",
                "ইমামের পেছনে দাঁড়িয়ে নিয়ত করে হাত উঠিয়ে ১ম তাকবীর ('আল্লাহু আকবার') বলে হাত বেঁধে ছানা বা সূরা ফাতিহা পাঠ করা।",
                "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ...",
                "সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা...",
                "হে আল্লাহ! আপনার প্রশংসার সাথে পবিত্রতা বর্ণনা করছি...",
                "সহীহ বুখারী: ১৩৩৫, সুনান নাসায়ী: ১৯৮৭"));
        janazahSteps.add(new SalahStepItem("২", "২য় তাকবীর ও দরূদে ইব্রাহিম",
                "হাত না উঠিয়ে ২য় তাকবীর বলে নামাজের মতো দরূদে ইব্রাহিম পাঠ করা।",
                "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ...",
                "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিঁও...",
                "হে আল্লাহ! মুহাম্মাদ (ﷺ) ও তাঁর পরিবারের উপর রহমত বর্ষণ করুন...",
                "সহীহ বুখারী: ৩৩৭০"));
        janazahSteps.add(new SalahStepItem("৩", "৩য় তাকবীর ও জানাযার দোয়া",
                "৩য় তাকবীর বলে মৃত ব্যক্তির মাগফিরাতের জন্য মাসনূন দোয়া পাঠ করা।",
                "اللَّهُمَّ اغْفِرْ لِحَيِّنَا وَمَيِّتِنَا، وَشَاهِدِنَا وَغَائِبِنَا، وَصَغِيرِنَا وَكَبِيرِنَا، وَذَكَرِنَا وَأُنْثَانَا، اللَّهُمَّ مَنْ أَحْيَيْتَهُ مِنَّا فَأَحْيِهِ عَلَى الْإِسْلَامِ، وَمَنْ تَوَفَّيْتَهُ مِنَّا فَتَوَفَّهُ عَلَى الْإِيمَانِ، اللَّهُمَّ لَا تَحْرِمْنَا أَجْرَهُ وَلَا تَفْتِنَّا بَعْدَهُ",
                "আল্লাহুম্মাগফির লি হাইয়্যিনা ওয়া মাইয়্যিতিনা, ওয়া শাহিদিনা ওয়া গায়িবিনা, ওয়া সগীরিনা ওয়া কাবীরিনা, ওয়া যাকারিনা ওয়া উনছানা। আল্লাহুম্মা মান আহইয়াইতাহু মিন্না ফাআহয়িহি আলাল ইসলাম, ওয়া মান তাওয়াফফায়তাহু মিন্না ফাতাওয়াফফাহু আলাল ঈমান। আল্লাহুম্মা লা তাহরিমনা আজরাহু ওয়ালা তাফতিন্না বা'দাহু।",
                "হে আল্লাহ! আমাদের জীবিত ও মৃতদের, উপস্থিত ও অনুপস্থিতদের, ছোট ও বড়দের, এবং আমাদের পুরুষ ও নারীদের ক্ষমা করুন। হে আল্লাহ! আমাদের মধ্যে যাকে আপনি জীবিত রাখবেন তাকে ইসলামের উপর জীবিত রাখুন, আর যাকে মৃত্যু দেবেন তাকে ঈমানের সাথে মৃত্যু দিন। হে আল্লাহ! এর সাওয়াব থেকে আমাদের বঞ্চিত করবেন না এবং এর পর আমাদের ফিতনায় ফেলবেন না।",
                "সুনান আবু দাউদ: ৩২০১, জামে আত-তিরমিযী: ১০২৪"));
        janazahSteps.add(new SalahStepItem("৪", "৪র্থ তাকবীর ও সালাম",
                "৪র্থ তাকবীর বলে ডান দিকে এবং বাঁ দিকে সালাম ফিরিয়ে জানাযা সম্পন্ন করা। জানাযার নামাজে কোনো রুকূ বা সিজদা নেই।",
                "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
                "আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ",
                "আপনার উপর আল্লাহর শান্তি ও রহমত বর্ষিত হোক।",
                "সুনান বায়হাকী: ৪/৪৩"));

        list.add(new SalahTopicItem("janazah", "জানাযার নামাজ",
                "রুকূ-সিজদাহীন ৪ তাকবীরে জানাযার পূর্ণাঙ্গ নিয়ম ও দোয়া", "nafl_janazah",
                "ফরজে কিফায়া", "৪ তাকবীর (দাঁড়িয়ে)", "মুসলিম ব্যক্তির ইন্তেকালের পর",
                "মৃত মুসলিমের জন্য মাগফিরাত কামনা করে জানাযার নামাজ আদায় করা জীবিত মুসলিমদের উপর ফরজে কিফায়া। জানাযায় অংশগ্রহণ করলে ১ কিরাত (উহুদ পাহাড় সমতুল্য) নেকি অর্জিত হয় (সহীহ বুখারী: ১৩২৫)।",
                janazahSteps));

        return Collections.unmodifiableList(list);
    }

    private List<SalahTopicItem> getEnglishTopics() {
        List<SalahTopicItem> list = new ArrayList<>();

        // 1. Complete Wudu Guide
        List<SalahStepItem> wuduSteps = new ArrayList<>();
        wuduSteps.add(new SalahStepItem("1", "Intention & Bismillah",
                "Form the intention of purification in the heart and begin by reciting 'Bismillah'.",
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "Bismillahir Rahmanir Raheem",
                "In the name of Allah, the Most Gracious, the Most Merciful.",
                "Sunan Abi Dawud: 101"));
        wuduSteps.add(new SalahStepItem("2", "Washing Both Hands up to the Wrists",
                "Wash both hands thoroughly up to the wrists 3 times, passing wet fingers between the fingers.",
                "", "", "", "Sahih al-Bukhari: 159, Sahih Muslim: 226"));
        wuduSteps.add(new SalahStepItem("3", "Rinsing the Mouth & Using Siwak",
                "Take water into the mouth with the right hand 3 times, rinse thoroughly, and clean teeth with siwak or fingers.",
                "", "", "", "Sahih al-Bukhari: 193"));
        wuduSteps.add(new SalahStepItem("4", "Sniffing Water into the Nostrils",
                "Sniff water into the nostrils with the right hand and blow it out using the left hand 3 times.",
                "", "", "", "Sahih al-Bukhari: 161"));
        wuduSteps.add(new SalahStepItem("5", "Washing the Entire Face",
                "Wash the face 3 times from the hairline of the forehead to below the chin, and from earlobe to earlobe.",
                "", "", "", "Surah Al-Ma'idah: 6"));
        wuduSteps.add(new SalahStepItem("6", "Washing Both Arms up to the Elbows",
                "Wash the right arm including the elbow 3 times, then wash the left arm including the elbow 3 times.",
                "", "", "", "Sahih al-Bukhari: 185"));
        wuduSteps.add(new SalahStepItem("7", "Wiping the Head & Ears",
                "With freshly wet hands, wipe over the head from front to back and back to front, and wipe the ears once.",
                "", "", "", "Sahih al-Bukhari: 185, Sunan Abi Dawud: 118"));
        wuduSteps.add(new SalahStepItem("8", "Washing Both Feet up to the Ankles",
                "Wash the right foot including heels and ankles 3 times passing fingers between toes, then the left foot 3 times.",
                "", "", "", "Sahih Muslim: 246"));
        wuduSteps.add(new SalahStepItem("9", "Supplication After Wudu",
                "Facing the Qiblah after wudu and reciting this supplication opens all eight gates of Paradise.",
                "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ، اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ",
                "Ash-hadu alla ilaha illallahu wahdahu la sharika lahu, wa ash-hadu anna Muhammadan abduhu wa rasuluhu. Allahummaj'alni minat-tawwabeena waj'alni minal-mutatahhireen.",
                "I bear witness that there is no god but Allah alone, having no partner, and I bear witness that Muhammad (PBUH) is His servant and Messenger. O Allah, make me among those who repent and make me among the purified.",
                "Sahih Muslim: 234, Jami' at-Tirmidhi: 55"));

        list.add(new SalahTopicItem("wudu", "Complete Wudu Guide",
                "Step-by-step ablution, Sunnahs and authentic supplications", "purity",
                "Prerequisite of Salah", "8 Sunnah Steps", "Before prayer or when purity is nullified",
                "Ablution (Wudu) is the key to prayer. No prayer is accepted without purity. Here is the authentic guide according to the Sunnah of the Prophet (PBUH).",
                wuduSteps));

        // 2. Prerequisites & Pillars
        List<SalahStepItem> prereqSteps = new ArrayList<>();
        prereqSteps.add(new SalahStepItem("1", "7 External Conditions of Salah (Ahkam)",
                "1. Purity of body\n2. Purity of garments\n3. Purity of place of prayer\n4. Covering the Awrah (Men: navel to knee; Women: entire body except face, hands, and feet)\n5. Facing the Qiblah\n6. Observing the prayer in its proper time\n7. Intention in the heart.",
                "", "", "", "Fiqh us-Sunnah, Sahih al-Bukhari & Muslim"));
        prereqSteps.add(new SalahStepItem("2", "6 Internal Pillars of Salah (Arkan)",
                "1. Takbirat al-Ihram (Saying 'Allahu Akbar' to begin)\n2. Qiyam (Standing posture for those able)\n3. Qira'at (Recitation of Quranic verses)\n4. Ruku' (Bowing down)\n5. Sujud (Two prostrations per rak'ah)\n6. Final Sitting (Sitting for the duration of Tashahhud).",
                "", "", "", "Sahih al-Bukhari: 757, Sahih Muslim: 397"));

        list.add(new SalahTopicItem("prerequisites", "Prerequisites & Pillars of Salah",
                "7 External Conditions and 6 Internal Pillars", "purity",
                "Fardh Ayn", "13 Obligations", "Before commencing and during prayer",
                "These 13 obligations must be observed for the prayer to be valid. Missing any of them invalidates the prayer.",
                prereqSteps));

        // 3. Steps of Salah & Recitations
        List<SalahStepItem> stepRecitations = new ArrayList<>();
        stepRecitations.add(new SalahStepItem("1", "Takbirat al-Ihram & Intention",
                "Form the intention of the specific prayer in the heart, raise both hands to earlobes (women to shoulder height) saying 'Allahu Akbar', then fold hands.",
                "اللَّهُ أَكْبَرُ", "Allahu Akbar", "Allah is the Greatest.", "Sahih al-Bukhari: 738"));
        stepRecitations.add(new SalahStepItem("2", "Opening Supplication (Dua al-Istiftah)",
                "Recited in the first rak'ah after folding hands before Surah Al-Fatihah.",
                "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَىٰ جَدُّكَ، وَلَا إِلٰهَ غَيْرُكَ",
                "Subhanaka Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruk.",
                "Glory be to You, O Allah, and all praise is Yours. Blessed is Your name, exalted is Your majesty, and there is no deity worthy of worship besides You.",
                "Sunan Abi Dawud: 775, Jami' at-Tirmidhi: 243"));
        stepRecitations.add(new SalahStepItem("3", "Ta'awwudh, Tasmiyah & Surah Al-Fatihah",
                "Seeking refuge from Satan, saying Bismillah, and reciting Surah Al-Fatihah in every rak'ah is obligatory. End with Ameen.",
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ • الرَّحْمَٰنِ الرَّحِيمِ • مَالِكِ يَوْمِ الدِّينِ • إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ • اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ • صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                "Alhamdu lillahi Rabbil 'Alameen. Ar-Rahmanir Raheem. Maliki Yawmid-Deen. Iyyaka na'budu wa iyyaka nasta'een. Ihdinas-Siratal Mustaqeem. Siratal-latheena an'amta 'alayhim ghayril maghdoobi 'alayhim walad-daalleen.",
                "All praise is due to Allah, Lord of the worlds. The Most Gracious, the Most Merciful. Master of the Day of Judgment. You alone we worship and You alone we ask for help. Guide us to the straight path. The path of those upon whom You have bestowed favor, not of those who have evoked anger or of those who are astray.",
                "Sahih al-Bukhari: 756"));
        stepRecitations.add(new SalahStepItem("4", "Bowing (Ruku') & Tasbeeh",
                "Say 'Allahu Akbar', bow keeping back straight and hands grasping knees firmly, and recite at least 3 times calmly.",
                "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
                "Subhana Rabbiyal 'Azeem",
                "Glory is to my Lord, the Magnificent.",
                "Sahih Muslim: 772"));
        stepRecitations.add(new SalahStepItem("5", "Rising from Ruku' (Qawmah) & Tahmeed",
                "Rise up straight saying 'Sami'Allahu liman hamidah', then recite 'Rabbana wa lakal hamd...'.",
                "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ • رَبَّنَا وَلَكَ الْحَمْدُ حَمْدًا كَثِيرًا طَيِّبًا مُبَارَكًا فِيهِ",
                "Sami'Allahu liman hamidah. Rabbana wa lakal hamd, hamdan katheeran tayyiban mubarakan feeh.",
                "Allah hears whoever praises Him. Our Lord, to You belongs all praise, abundant, good, and blessed praise.",
                "Sahih al-Bukhari: 799"));
        stepRecitations.add(new SalahStepItem("6", "Prostration (Sujud) & Tasbeeh",
                "Say 'Allahu Akbar' and prostrate on seven limbs (forehead and nose, both hands, both knees, and toes), reciting at least 3 times.",
                "سُبْحَانَ رَبِّيَ الْأَعْلَىٰ",
                "Subhana Rabbiyal A'la",
                "Glory is to my Lord, the Most High.",
                "Sahih Muslim: 772"));
        stepRecitations.add(new SalahStepItem("7", "Sitting Between Two Prostrations (Jalsah)",
                "Rise from first prostration, sit upright serenely, and make supplication before second prostration.",
                "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي، اللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي",
                "Rabbighfir lee, Rabbighfir lee. Allahummaghfir lee warhamnee wahdinee wa 'aafinee warzuqnee.",
                "O my Lord, forgive me, O my Lord, forgive me. O Allah, forgive me, have mercy on me, guide me, grant me health, and provide for me.",
                "Sunan Abi Dawud: 850, Jami' at-Tirmidhi: 284"));
        stepRecitations.add(new SalahStepItem("8", "Tashahhud (At-Tahiyyat)",
                "Sitting after 2 rak'ahs and in the final sitting, calmly reciting Tashahhud and raising index finger at the Shahadah.",
                "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
                "At-tahiyyatu lillahi was-salawatu wat-tayyibatu, as-salamu 'alayka ayyuhan-Nabiyyu wa rahmatullahi wa barakatuh, as-salamu 'alayna wa 'ala 'ibadillahis-saliheen. Ash-hadu alla ilaha illallahu wa ash-hadu anna Muhammadan 'abduhu wa rasuluh.",
                "All greetings, prayers, and pure things are for Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no deity worthy of worship except Allah, and I bear witness that Muhammad is His slave and Messenger.",
                "Sahih al-Bukhari: 831, Sahih Muslim: 402"));
        stepRecitations.add(new SalahStepItem("9", "Salawat upon the Prophet (Durood Ibrahim)",
                "Recited in the final sitting after Tashahhud.",
                "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
                "Allahumma salli 'ala Muhammadin wa 'ala aali Muhammad, kama sallayta 'ala Ibraheema wa 'ala aali Ibraheem, innaka Hameedum Majeed. Allahumma barik 'ala Muhammadin wa 'ala aali Muhammad, kama barakta 'ala Ibraheema wa 'ala aali Ibraheem, innaka Hameedum Majeed.",
                "O Allah, send prayers upon Muhammad and upon the family of Muhammad, as You sent prayers upon Ibrahim and the family of Ibrahim. Indeed You are Praiseworthy and Glorious. O Allah, send blessings upon Muhammad and upon the family of Muhammad, as You sent blessings upon Ibrahim and the family of Ibrahim. Indeed You are Praiseworthy and Glorious.",
                "Sahih al-Bukhari: 3370, Sahih Muslim: 405"));
        stepRecitations.add(new SalahStepItem("10", "Dua al-Ma'thur (Before Salaam)",
                "Supplication seeking protection and forgiveness before concluding prayer with Salaam.",
                "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا، وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
                "Allahumma innee zalamtu nafsee zulman katheeran, wa la yaghfirudh-dhunooba illa anta, faghfir lee maghfiratan min 'indika warhamnee, innaka antal-Ghafoorur-Raheem.",
                "O Allah, I have wronged myself greatly, and none forgives sins except You. So grant me forgiveness from Yourself and have mercy on me. Indeed You are the Forgiving, the Merciful.",
                "Sahih al-Bukhari: 834, Sahih Muslim: 2705"));
        stepRecitations.add(new SalahStepItem("11", "Tasleem (Concluding the Prayer)",
                "Turn face to the right saying 'Assalamu Alaykum wa Rahmatullah', then turn to the left saying the same.",
                "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
                "Assalamu alaykum wa rahmatullah",
                "May peace and mercy of Allah be upon you.",
                "Sahih Muslim: 582"));

        list.add(new SalahTopicItem("steps", "Steps of Salah & Recitations",
                "11 Essential Steps from Takbeer to Tasleem with Arabic, transliteration and translation", "steps",
                "Pillars & Obligations", "11 Sequential Steps", "Applicable to all prayers",
                "The Prophet (PBUH) said: 'Pray as you have seen me praying.' (Sahih al-Bukhari: 631). The proper steps, recitations, and meanings are detailed below.",
                stepRecitations));

        // 4. Fajr Prayer
        List<SalahStepItem> fajrSteps = new ArrayList<>();
        fajrSteps.add(new SalahStepItem("1", "2 Rak'ahs Sunnah Mu'akkadah",
                "The 2 rak'ahs before Fajr fardh are highly emphasized. The Prophet (PBUH) never abandoned them. Reciting Surah Al-Kafirun in the 1st rak'ah and Surah Al-Ikhlas in the 2nd is Sunnah.",
                "", "", "", "Sahih Muslim: 724"));
        fajrSteps.add(new SalahStepItem("2", "2 Rak'ahs Fardh",
                "Perform 2 rak'ahs in congregation or individually reciting Surah Al-Fatihah and additional Surah aloud (Jahri).",
                "", "", "", "Sahih al-Bukhari: 776"));
        list.add(new SalahTopicItem("fajr", "Fajr Prayer",
                "2 Rak'ahs Sunnah + 2 Rak'ahs Fardh", "fardh",
                "Fardh Ayn", "4 Rak'ahs (2 Sunnah + 2 Fardh)", "From true dawn until before sunrise",
                "Fajr prayer infuses light and blessings into a believer's heart at the start of the day. It is among the heaviest prayers for hypocrites.",
                fajrSteps));

        // 5. Dhuhr Prayer
        List<SalahStepItem> dhuhrSteps = new ArrayList<>();
        dhuhrSteps.add(new SalahStepItem("1", "4 Rak'ahs Sunnah Mu'akkadah", "Performed before Fardh with one Salaam.", "", "", "", "Sahih Muslim: 730"));
        dhuhrSteps.add(new SalahStepItem("2", "4 Rak'ahs Fardh", "Performed with silent recitation (Sirri).", "", "", "", "Sahih al-Bukhari: 759"));
        dhuhrSteps.add(new SalahStepItem("3", "2 Rak'ahs Sunnah Mu'akkadah", "Performed after Fardh.", "", "", "", "Sahih al-Bukhari: 937"));
        dhuhrSteps.add(new SalahStepItem("4", "2 Rak'ahs Nafl (Optional)", "Recommended after the Sunnah.", "", "", "", "Jami' at-Tirmidhi: 428"));
        list.add(new SalahTopicItem("dhuhr", "Dhuhr Prayer",
                "4 Sunnah + 4 Fardh + 2 Sunnah + 2 Nafl", "fardh",
                "Fardh Ayn", "12 Rak'ahs (4 Sunnah + 4 Fardh + 2 Sunnah + 2 Nafl)", "From the sun passing its meridian until the shadow reaches twice the length",
                "Dhuhr prayer provides serenity and mindfulness amidst the busy hours of midday.",
                dhuhrSteps));

        // 6. Asr Prayer
        List<SalahStepItem> asrSteps = new ArrayList<>();
        asrSteps.add(new SalahStepItem("1", "4 Rak'ahs Sunnah Ghayr Mu'akkadah", "Recommended before Fardh. The Prophet (PBUH) supplicated for mercy upon who prays it.", "", "", "", "Sunan Abi Dawud: 1271"));
        asrSteps.add(new SalahStepItem("2", "4 Rak'ahs Fardh", "Performed with silent recitation. No voluntary prayer is allowed after Asr until sunset.", "", "", "", "Sahih al-Bukhari: 553"));
        list.add(new SalahTopicItem("asr", "Asr Prayer",
                "4 Sunnah + 4 Rak'ahs Fardh", "fardh",
                "Fardh Ayn", "8 Rak'ahs (4 Sunnah + 4 Fardh)", "From mid-afternoon until shortly before sunset",
                "The Quran specifically emphasizes the middle prayer (Asr) for steadfast observance (Surah Al-Baqarah: 238).",
                asrSteps));

        // 7. Maghrib Prayer
        List<SalahStepItem> maghribSteps = new ArrayList<>();
        maghribSteps.add(new SalahStepItem("1", "3 Rak'ahs Fardh", "First 2 rak'ahs recited aloud and the 3rd rak'ah silently with Surah Al-Fatihah.", "", "", "", "Sahih al-Bukhari: 761"));
        maghribSteps.add(new SalahStepItem("2", "2 Rak'ahs Sunnah Mu'akkadah", "Performed immediately after Fardh.", "", "", "", "Sahih Muslim: 728"));
        maghribSteps.add(new SalahStepItem("3", "2 or 6 Rak'ahs Nafl (Awwabeen)", "Highly rewarded voluntary prayer following Maghrib.", "", "", "", "Jami' at-Tirmidhi: 435"));
        list.add(new SalahTopicItem("maghrib", "Maghrib Prayer",
                "3 Rak'ahs Fardh + 2 Rak'ahs Sunnah + 2 Nafl", "fardh",
                "Fardh Ayn", "7 Rak'ahs (3 Fardh + 2 Sunnah + 2 Nafl)", "From sunset until the red afterglow disappears from the western sky",
                "It is Sunnah to perform Maghrib promptly after sunset without delay.",
                maghribSteps));

        // 8. Isha Prayer
        List<SalahStepItem> ishaSteps = new ArrayList<>();
        ishaSteps.add(new SalahStepItem("1", "4 Rak'ahs Sunnah Ghayr Mu'akkadah", "Commendable before Fardh.", "", "", "", "Fiqh us-Sunnah"));
        ishaSteps.add(new SalahStepItem("2", "4 Rak'ahs Fardh", "First 2 rak'ahs recited aloud and last 2 silently.", "", "", "", "Sahih al-Bukhari: 762"));
        ishaSteps.add(new SalahStepItem("3", "2 Rak'ahs Sunnah Mu'akkadah", "Performed after Fardh.", "", "", "", "Sahih al-Bukhari: 937"));
        ishaSteps.add(new SalahStepItem("4", "2 Rak'ahs Nafl", "Voluntary prayer after Sunnah.", "", "", "", "Musannaf Ibn Abi Shaybah"));
        list.add(new SalahTopicItem("isha", "Isha Prayer",
                "4 Sunnah + 4 Fardh + 2 Sunnah + 2 Nafl", "fardh",
                "Fardh Ayn", "12 Rak'ahs (4 Sunnah + 4 Fardh + 2 Sunnah + 2 Nafl)", "From the fading of twilight until midnight or true dawn",
                "Praying Isha in congregation is equivalent to spending half the night in worship (Sahih Muslim: 656).",
                ishaSteps));

        // 9. Witr Prayer & Dua Qunut
        List<SalahStepItem> witrSteps = new ArrayList<>();
        witrSteps.add(new SalahStepItem("1", "Method & Observance of 3 Rak'ahs Witr",
                "Performed after Isha or in the last third of the night. In the 3rd rak'ah, after reciting Surah Al-Fatihah and an additional Surah, raise hands saying 'Allahu Akbar', re-fold hands, and recite Dua Qunut.",
                "", "", "", "Sahih al-Bukhari: 994, Sunan Abi Dawud: 1424"));
        witrSteps.add(new SalahStepItem("2", "Dua al-Qunut",
                "Obligatory or highly emphasized supplication recited standing in the 3rd rak'ah before bowing.",
                "اللَّهُمَّ إِنَّا نَسْتَعِينُكَ وَنَسْتَغْفِرُكَ، وَنُؤْمِنُ بِكَ وَنَتَوَكَّلُ عَلَيْكَ، وَنُثْنِي عَلَيْكَ الْخَيْرَ، وَنَشْكُرُكَ وَلَا نَكْفُرُكَ، وَنَخْلَعُ وَنَتْرُكُ مَنْ يَفْجُرُكَ، اللَّهُمَّ إِيَّاكَ نَعْبُدُ، وَلَكَ نُصَلِّي وَنَسْجُدُ، وَإِلَيْكَ نَسْعَىٰ وَنَحْفِدُ، نَرْجُو رَحْمَتَكَ وَنَخْشَىٰ عَذَابَكَ، إِنَّ عَذَابَكَ بِالْكُفَّارِ مُلْحَقٌ",
                "Allahumma inna nasta'eenuka wa nastaghfiruka, wa nu'minu bika wa natawakkalu 'alayka, wa nuthnee 'alaykal khayr, wa nashkuruka wa la nakfuruk, wa nakhla'u wa natruku may-yafjuruk. Allahumma iyyaka na'budu, wa laka nusallee wa nasjud, wa ilayka nas'a wa nahfid, narjoo rahmataka wa nakhsha 'adhabak, inna 'adhabaka bil-kuffari mulhiq.",
                "O Allah, we seek Your help and Your forgiveness, we believe in You, put our trust in You, and praise You in the best manner. We thank You and do not deny You, and we reject and forsake whoever disobeys You. O Allah, You alone we worship, to You we pray and prostrate, towards You we strive and rush. We hope for Your mercy and fear Your punishment. Indeed, Your punishment encompasses the disbelievers.",
                "Sunan al-Bayhaqi: 2/211, Sunan Ibn Majah: 1178"));

        list.add(new SalahTopicItem("witr", "Witr Prayer & Dua Qunut",
                "3 Rak'ahs Wajib Witr & Complete Dua Qunut", "sunnah_wajib",
                "Wajib", "3 Rak'ahs", "After Isha prayer until true dawn",
                "The Prophet (PBUH) said: 'Indeed, Allah is Witr (One/Odd), and He loves the odd number. So observe the Witr prayer, O people of the Quran.' (Sunan Abi Dawud: 1416).",
                witrSteps));

        // 10. Daily Sunnah Mu'akkadah
        List<SalahStepItem> sunnahSteps = new ArrayList<>();
        sunnahSteps.add(new SalahStepItem("1", "Daily 12 Rak'ahs of Sunnah",
                "• 2 Rak'ahs before Fajr\n• 4 Rak'ahs before Dhuhr & 2 Rak'ahs after\n• 2 Rak'ahs after Maghrib\n• 2 Rak'ahs after Isha.",
                "مَنْ صَلَّىٰ فِي يَوْمٍ وَلَيْلَةٍ ثِنْتَيْ عَشْرَةَ رَكْعَةً بُنِيَ لَهُ بَيْتٌ فِي الْجَنَّةِ",
                "Man salla fee yawmin wa laylatin thintay 'ashrata rak'atan buniya lahu baytun fil Jannah.",
                "Whoever prays twelve rak'ahs during the day and night, a house will be built for him in Paradise.",
                "Sahih Muslim: 728"));
        list.add(new SalahTopicItem("sunnah", "Sunnah Mu'akkadah",
                "List and virtues of the daily 12 Sunnah rak'ahs", "sunnah_wajib",
                "Sunnah Mu'akkadah", "12 Rak'ahs", "Designated alongside each fardh prayer",
                "The Prophet (PBUH) consistently observed Sunnah Mu'akkadah prayers, and neglecting them habitually without excuse is sinful.",
                sunnahSteps));

        // 11. Nafl & Special Prayers
        List<SalahStepItem> naflSteps = new ArrayList<>();
        naflSteps.add(new SalahStepItem("1", "Salat ut-Tahajjud (Qiyam al-Layl)",
                "Night vigil prayer of 2 to 12 rak'ahs after waking up during the last third of the night. The Prophet (PBUH) regularly prayed 8 rak'ahs plus 3 rak'ahs Witr.",
                "", "", "", "Sahih al-Bukhari: 1129, Surah Al-Isra: 79"));
        naflSteps.add(new SalahStepItem("2", "Salat ud-Duha (Ishraq & Chasht)",
                "2 to 8 rak'ahs observed 15-20 minutes after sunrise until shortly before solar noon. It serves as daily charity for all 360 bodily joints.",
                "", "", "", "Sahih Muslim: 720"));
        naflSteps.add(new SalahStepItem("3", "Salat ul-Istikharah & Supplication",
                "2 rak'ahs of voluntary prayer followed by the authentic Bukhari supplication when seeking divine guidance in crucial decisions.",
                "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ...",
                "Allahumma innee astakheeruka bi'ilmika...",
                "O Allah, I seek Your guidance through Your knowledge...",
                "Sahih al-Bukhari: 1162"));
        naflSteps.add(new SalahStepItem("4", "Salat ut-Tasbeeh",
                "A 4-rak'ah voluntary prayer reciting the Tasbeeh 75 times per rak'ah (300 times total), bringing forgiveness of past and future sins.",
                "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَلَا إِلٰهَ إِلَّا اللَّهُ وَاللَّهُ أَكْبَرُ",
                "Subhanallahi wal-hamdulillahi wa la ilaha illallahu wallahu akbar.",
                "Glory be to Allah, all praise is due to Allah, there is no deity worthy of worship except Allah, and Allah is the Greatest.",
                "Sunan Abi Dawud: 1297, Jami' at-Tirmidhi: 481"));

        list.add(new SalahTopicItem("nafl", "Nafl & Special Prayers",
                "Tahajjud, Ishraq, Duha, Istikharah, and Salat ut-Tasbeeh", "nafl_janazah",
                "Nafl or Mustahabb", "2 to 12 Rak'ahs", "Various blessed times",
                "Through voluntary prayers, a servant draws closest to Allah, and deficits in obligatory prayers will be compensated on the Day of Resurrection.",
                naflSteps));

        // 12. Janazah Prayer
        List<SalahStepItem> janazahSteps = new ArrayList<>();
        janazahSteps.add(new SalahStepItem("1", "1st Takbeer & Thana or Fatihah",
                "Standing behind the Imam, form intention, raise hands for the 1st Takbeer ('Allahu Akbar'), fold hands, and recite Thana or Surah Al-Fatihah.",
                "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ...",
                "Subhanaka Allahumma wa bihamdika...",
                "Glory be to You, O Allah, and all praise is Yours...",
                "Sahih al-Bukhari: 1335, Sunan an-Nasa'i: 1987"));
        janazahSteps.add(new SalahStepItem("2", "2nd Takbeer & Durood Ibrahim",
                "Say the 2nd Takbeer without raising hands and recite Durood Ibrahim upon the Prophet (PBUH).",
                "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ...",
                "Allahumma salli 'ala Muhammadin...",
                "O Allah, send blessings upon Muhammad and the family of Muhammad...",
                "Sahih al-Bukhari: 3370"));
        janazahSteps.add(new SalahStepItem("3", "3rd Takbeer & Funeral Supplication",
                "Say the 3rd Takbeer and recite the authentic supplication asking for forgiveness for the deceased.",
                "اللَّهُمَّ اغْفِرْ لِحَيِّنَا وَمَيِّتِنَا، وَشَاهِدِنَا وَغَائِبِنَا، وَصَغِيرِنَا وَكَبِيرِنَا، وَذَكَرِنَا وَأُنْثَانَا، اللَّهُمَّ مَنْ أَحْيَيْتَهُ مِنَّا فَأَحْيِهِ عَلَى الْإِسْلَامِ، وَمَنْ تَوَفَّيْتَهُ مِنَّا فَتَوَفَّهُ عَلَى الْإِيمَانِ، اللَّهُمَّ لَا تَحْرِمْنَا أَجْرَهُ وَلَا تَفْتِنَّا بَعْدَهُ",
                "Allahummaghfir li hayyina wa mayyitina, wa shahidina wa gha'ibina, wa sagheerina wa kabeerina, wa dhakarina wa unthana. Allahumma man ahyaytahu minna fa-ahyihi 'alal Islam, wa man tawaffaytahu minna fatawaffahu 'alal Iman. Allahumma la tahrimna ajrahu wa la taftinna ba'dah.",
                "O Allah, forgive our living and our deceased, those present and those absent, our young and our old, our males and our females. O Allah, whoever among us You keep alive, keep him alive upon Islam, and whoever among us You take away, cause him to die upon faith. O Allah, do not deprive us of his reward and do not put us to trial after him.",
                "Sunan Abi Dawud: 3201, Jami' at-Tirmidhi: 1024"));
        janazahSteps.add(new SalahStepItem("4", "4th Takbeer & Tasleem",
                "Say the 4th Takbeer and turn face to the right and left with Tasleem. The funeral prayer has neither bowing (Ruku') nor prostration (Sujud).",
                "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
                "Assalamu alaykum wa rahmatullah",
                "May peace and mercy of Allah be upon you.",
                "Sunan al-Bayhaqi: 4/43"));

        list.add(new SalahTopicItem("janazah", "Janazah Prayer",
                "Funeral prayer procedure with 4 Takbeers without Ruku' or Sujud", "nafl_janazah",
                "Fardh Kifayah", "4 Takbeers (Standing)", "Upon the demise of a Muslim",
                "Praying for forgiveness of a deceased Muslim is a communal obligation (Fardh Kifayah). Participating earns one Qirat (equivalent to Mount Uhud) of reward (Sahih al-Bukhari: 1325).",
                janazahSteps));

        return Collections.unmodifiableList(list);
    }
}
