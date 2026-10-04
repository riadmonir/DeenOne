package com.devflux.deenone.features.janaza;

import com.devflux.deenone.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Production-ready authentic data repository for the Janaza Guide module.
 * Provides complete A-to-Z Islamic content from basic to advanced levels
 * in pure Bengali and pure English (zero mixed bracket text).
 */
public class JanazaDataProvider {

    public static class JanazaContentItem {
        public final String heading;
        public final String arabic;
        public final String pronunciation;
        public final String meaning;
        public final String explanation;
        public final String reference;

        public JanazaContentItem(String heading, String arabic, String pronunciation, String meaning, String explanation, String reference) {
            this.heading = heading;
            this.arabic = arabic;
            this.pronunciation = pronunciation;
            this.meaning = meaning;
            this.explanation = explanation;
            this.reference = reference;
        }
    }

    public static class JanazaTopic {
        public final String id;
        public final String title;
        public final String subtitle;
        public final int iconRes;
        public final String intro;
        public final List<JanazaContentItem> contentItems;

        public JanazaTopic(String id, String title, String subtitle, int iconRes, String intro, List<JanazaContentItem> contentItems) {
            this.id = id;
            this.title = title;
            this.subtitle = subtitle;
            this.iconRes = iconRes;
            this.intro = intro;
            this.contentItems = contentItems;
        }
    }

    public static List<JanazaTopic> getTopics(boolean isBn) {
        List<JanazaTopic> list = new ArrayList<>();

        // 1. জানাযা নামাযের নিয়ত ও প্রাথমিক প্রস্তুতি
        list.add(getTopicNiyyah(isBn));

        // 2. নিয়তের পরে ছানা ও কিরাতের নিয়ম
        list.add(getTopicSana(isBn));

        // 3. দুরূদ শরীফ
        list.add(getTopicDurood(isBn));

        // 4. জানাযার দোয়া
        list.add(getTopicDuas(isBn));

        // 5. জানাযার পূর্ণাঙ্গ পদ্ধতি ও কাতার বিন্যাস
        list.add(getTopicMethod(isBn));

        // 6. গায়েবী জানাযা
        list.add(getTopicGayebee(isBn));

        // 7. মৃত ব্যক্তির গোসল ও কাফন
        list.add(getTopicGhusl(isBn));

        // 8. জানাযার নামাযের পদ্ধতি ও করণীয়
        list.add(getTopicBurialAndRules(isBn));

        return list;
    }

    public static JanazaTopic getTopicById(String topicId, boolean isBn) {
        for (JanazaTopic topic : getTopics(isBn)) {
            if (topic.id.equalsIgnoreCase(topicId)) {
                return topic;
            }
        }
        return getTopics(isBn).get(0);
    }

    // ==========================================
    // 1. জানাযা নামাযের নিয়ত ও প্রাথমিক প্রস্তুতি
    // ==========================================
    private static JanazaTopic getTopicNiyyah(boolean isBn) {
        List<JanazaContentItem> items = new ArrayList<>();
        if (isBn) {
            items.add(new JanazaContentItem(
                    "১. মৃত্যুর পর তাৎক্ষণিক করণীয় ও প্রাথমিক প্রস্তুতি",
                    null,
                    null,
                    null,
                    "• চোখ বন্ধ করা: মৃত্যুর পরপরই মাইয়্যিতের চোখ বন্ধ করে দেওয়া এবং মুখের চোয়াল নরম কাপড় দিয়ে মাথার ওপর বেঁধে রাখা যাতে হাঁ হয়ে না থাকে।\n" +
                    "• হাত-পা সোজা করা: হাত ও পা সোজা করে রাখা এবং শরীর ঢেকে দেওয়া।\n" +
                    "• পেটের ওপর হালকা বস্তু রাখা: পেট ফুলে যাওয়া রোধ করতে পেটের ওপর সামান্য ওজনের কোনো বস্তু রাখা মুস্তাহাব।\n" +
                    "• মৃত্যু সংবাদ জানানো: আত্মীয়-স্বজন ও নেককার মুসলিমদের দ্রুত মৃত্যু সংবাদ জানানো যাতে জানাজায় অধিক সংখ্যক মুমিন শরিক হতে পারেন।\n" +
                    "• জানাজার স্থান নির্বাচন: মসজিদ সংলগ্ন জানাজার মাঠ, ঈদগাহ বা উন্মুক্ত প্রশস্ত স্থান নির্বাচন করা উত্তম।",
                    "সহীহ মুসলিম: ৯২০, সুনানে আবু দাউদ: ৩১২০"
            ));

            items.add(new JanazaContentItem(
                    "২. নিয়তের প্রকৃত অর্থ ও শরিয়তসম্মত বিধান",
                    null,
                    null,
                    null,
                    "• নিয়ত অর্থ অন্তরের দৃঢ় সংকল্প। জানাযার নামাযে অন্তরে এই সংকল্প থাকাই যথেষ্ট যে, 'আমি আল্লাহর সন্তুষ্টির উদ্দেশ্যে এই উপস্থিত মৃত ব্যক্তির জন্য ফরযে কেফায়া জানাযা সালাত চার তাকবিরের সাথে ইমামের পেছনে আদায় করছি।'\n" +
                    "• মুখে আরবি বা বাংলায় বাক্য উচ্চারণ করা জরুরি নয়; বরং অন্তরের ইচ্ছাই মূল নিয়ত। তবে মনকে একাগ্র ও স্থির রাখার সুবিধার্থে কেউ মুখে উচ্চারণ করলে তা জায়েয।\n" +
                    "• জানাযা সালাতের হুকুম হলো 'ফরযে কেফায়া' (কিছু মুসলিম আদায় করলে সবার পক্ষ থেকে আদায় হয়ে যায়, কিন্তু কেউ না পড়লে সবাই গুনাহগার হবে)।",
                    "সহীহ বুখারী: ১, সহীহ মুসলিম: ১৯০৭"
            ));

            items.add(new JanazaContentItem(
                    "৩. জানাযা নামাযের আরবি নিয়ত",
                    "نَوَيْتُ أَنْ أُؤَدِّيَ لِلَّهِ تَعَالَى أَرْبَعَ تَكْبِيرَاتِ صَلَاةِ الْجَنَازَةِ، فَرْضَ الْكِفَايَةِ، وَالثَّنَاءُ لِلَّهِ تَعَالَى، وَالصَّلَاةُ عَلَى النَّبِيِّ ﷺ، وَالدُّعَاءُ لِهَذَا الْمَيِّتِ، مُقْتَدِيًا بِهَذَا الْإِمَامِ، مُتَوَجِّهًا إِلَى جِهَةِ الْكَعْبَةِ الشَّرِيفَةِ، اللَّهُ أَكْبَرُ.",
                    "নাওয়াইতু আন উয়াদ্দিয়া লিল্লাহি তাআলা আরবাআ তাকবিরাতি সালাতিল জানাযাতি ফারযাল কিফায়াতি, ওয়াছ ছানা-উ লিল্লাহি তাআলা, ওয়াস সালাতু আলান নাবিয়্যি ﷺ, ওয়াদ দুআউ লিহাযাল মাইয়্যিতি, মুক্তাদিয়ান বিহাযাল ইমামি, মুতাওয়াজ্জিহান ইলা জিহাতিল কা'বাতিশ শারিফাতি, আল্লাহু আকবার।",
                    "আমি আল্লাহর সন্তুষ্টির উদ্দেশ্যে এই মৃত ব্যক্তির জন্য চার তাকবিরের সাথে ফরযে কেফায়া জানাযার নামায, আল্লাহর প্রশংসা, নবীর ওপর দরূদ এবং এই মাইয়্যিতের জন্য দোয়া করার উদ্দেশ্যে এই ইমামের পেছনে কেবলামুখী হয়ে আদায় করার নিয়ত করছি। আল্লাহু আকবার।",
                    "• যদি মাইয়্যিত পুরুষ হন তবে 'লিহাযাল মায়্যিত' (لِهَذَا الْمَيِّتِ) বলবেন।\n" +
                    "• যদি মাইয়্যিত নারী হন তবে 'লিহাযিহিল মায়্যিতাহ' (لِهَذِهِ الْمَيِّتَةِ) বলবেন।\n" +
                    "• যদি মাইয়্যিত নাবালক ছেলে হয় তবে 'লিহাযাস সাবিয়্যি' (لِهَذَا الصَّبِيِّ) বলবেন।\n" +
                    "• যদি মাইয়্যিত নাবালিকা মেয়ে হয় তবে 'লিহাযিহিস সাবিয়্যাহ' (لِهَذِهِ الصَّبِيَّةِ) বলবেন।",
                    "ফাতাওয়া শামী ২/২০৯, আল-বাহরুর রায়েক ২/১৮০"
            ));

            items.add(new JanazaContentItem(
                    "৪. বাংলায় নিয়ত করার সহজ পদ্ধতি",
                    null,
                    null,
                    "“আমি আল্লাহর সন্তুষ্টির উদ্দেশ্যে এই মৃত ব্যক্তির জানাযা নামায ফরযে কেফায়া হিসেবে চার তাকবিরের সাথে এই ইমামের পেছনে কেবলামুখী হয়ে আদায় করার নিয়ত করছি। আল্লাহু আকবার।”",
                    "• ইমামের নিয়ত: ইমাম নিয়ত করবেন উপস্থিত মাইয়্যিতের জানাযা নামাযের ইমামতি করার।\n" +
                    "• মুক্তাদির নিয়ত: মুক্তাদি নিয়ত করবেন উপস্থিত ইমামের পেছনে উক্ত মাইয়্যিতের জানাযা সালাত আদায় করার।\n" +
                    "• যদি একাধিক মাইয়্যিতের জানাযা একসাথে হয়, তবে এক নিয়তেই সবার জন্য সালাত আদায় হয়ে যাবে।",
                    "ফাতাওয়ায়ে আলমগীরী ১/১৬৪"
            ));
        } else {
            items.add(new JanazaContentItem(
                    "1. Immediate Actions After Death and Preparation",
                    null,
                    null,
                    null,
                    "• Closing Eyes: Gently close the eyes of the deceased and tie the chin with a soft strip of cloth over the head so the mouth remains closed.\n" +
                    "• Straightening Limbs: Straighten arms and legs and cover the entire body with a clean sheet.\n" +
                    "• Placing light weight on abdomen: Placing a mild weight on the stomach is recommended to prevent bloating.\n" +
                    "• Announcing Death: Inform relatives and righteous Muslims promptly so many can participate in the funeral prayer.\n" +
                    "• Prayer Location: Selecting an open prayer ground, Eidgah, or courtyard adjacent to the mosque is preferred.",
                    "Sahih Muslim: 920, Sunan Abi Dawud: 3120"
            ));

            items.add(new JanazaContentItem(
                    "2. True Meaning and Rulings of Niyyah",
                    null,
                    null,
                    null,
                    "• Niyyah literally means the intention and resolve of the heart. Having the clear intention in your heart that 'I am offering the Janaza prayer for this deceased person with 4 Takbeers as Fardh Kifayah behind this Imam' is completely sufficient.\n" +
                    "• Verbal uttering in Arabic or English is not obligatory; the intention in the heart is the core pillar. However, saying it verbally to concentrate the mind is permissible.\n" +
                    "• Janaza prayer is 'Fardh al-Kifayah' (a communal obligation — if some Muslims perform it, the entire community is absolved; if none perform it, the entire community is sinful).",
                    "Sahih Bukhari: 1, Sahih Muslim: 1907"
            ));

            items.add(new JanazaContentItem(
                    "3. Traditional Arabic Niyyah for Janaza",
                    "نَوَيْتُ أَنْ أُؤَدِّيَ لِلَّهِ تَعَالَى أَرْبَعَ تَكْبِيرَاتِ صَلَاةِ الْجَنَازَةِ، فَرْضَ الْكِفَايَةِ، وَالثَّنَاءُ لِلَّهِ تَعَالَى، وَالصَّلَاةُ عَلَى النَّبِيِّ ﷺ، وَالدُّعَاءُ لِهَذَا الْمَيِّتِ، مُقْتَدِيًا بِهَذَا الْإِمَامِ، مُتَوَجِّهًا إِلَى جِهَةِ الْكَعْبَةِ الشَّرِيفَةِ، اللَّهُ أَكْبَرُ.",
                    "Nawaytu an u'addiya lillahi ta'ala arba'a takbirati salatil janazati fardal kifayati, wath-thana'u lillahi ta'ala, was-salatu 'alan-nabiyyi ﷺ, wad-du'a'u lihathal mayyiti, muqtadiyan bihathal imami, mutawajjihan ila jihatil ka'batish-sharifati, Allahu Akbar.",
                    "I intend to perform for the sake of Allah the four Takbeers of Janaza prayer as Fardh al-Kifayah, praising Allah, sending peace upon the Prophet ﷺ, and supplicating for this deceased person, following this Imam, facing the Holy Kaaba. Allahu Akbar.",
                    "• For a deceased male, use 'lihathal mayyit' (لِهَذَا الْمَيِّتِ).\n" +
                    "• For a deceased female, use 'lihathihil mayyitah' (لِهَذِهِ الْمَيِّتَةِ).\n" +
                    "• For a young boy, use 'lihathas-sabiyyi' (لِهَذَا الصَّبِيِّ).\n" +
                    "• For a young girl, use 'lihathihis-sabiyyah' (لِهَذِهِ الصَّبِيَّةِ).",
                    "Fatawa Shami 2/209, Al-Bahrur Ra'iq 2/180"
            ));

            items.add(new JanazaContentItem(
                    "4. Simple English Intention",
                    null,
                    null,
                    "“I intend to perform the funeral prayer for the sake of Allah for this deceased person with four Takbeers as a communal obligation behind this Imam facing the Qiblah. Allahu Akbar.”",
                    "• Imam's Intention: Intends to lead the funeral prayer for the deceased present.\n" +
                    "• Follower's (Muqtadi) Intention: Intends to follow the Imam in praying for the deceased.\n" +
                    "• Combined Funerals: If multiple bodies are present, a single combined intention covers all.",
                    "Fatawa Alamgiri 1/164"
            ));
        }

        return new JanazaTopic(
                "topic_niyyah",
                isBn ? "জানাযা নামাযের নিয়ত" : "Intention for Janaza Prayer",
                isBn ? "অন্তরের সংকল্প, প্রাথমিক প্রস্তুতি, আরবি ও বাংলায় নিয়তের পূর্ণ বিবরণ" : "Heart intention, immediate preparation, Arabic & English wording",
                R.drawable.ic_janaza_niyyah,
                isBn ? "জানাযা সালাত শুরুর পূর্বে নিয়ত ও প্রস্তুতির সুন্নাহসম্মত বিধান।" : "Authentic Sunnah guidelines regarding preparation and intention before Janaza.",
                items
        );
    }

    // ==========================================
    // 2. নিয়তের পরে ছানা ও কিরাতের নিয়ম
    // ==========================================
    private static JanazaTopic getTopicSana(boolean isBn) {
        List<JanazaContentItem> items = new ArrayList<>();
        if (isBn) {
            items.add(new JanazaContentItem(
                    "১. প্রথম তাকবির ও ছানা পাঠের বিধান",
                    "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَى جَدُّكَ، وَجَلَّ ثَنَاؤُكَ، وَلَا إِلَهَ غَيْرُكَ",
                    "সুবহা-নাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবা-রাকাসমুকা, ওয়া তাআ-লা জাদ্দুকা, ওয়া জাল্লা সানা-উকা, ওয়া লা ইলা-হা গাইরুক।",
                    "হে আল্লাহ! আপনার পবিত্রতা ঘোষণা করছি এবং আপনার প্রশংসা বর্ণনা করছি। আপনার নাম পরম বরকতময়, আপনার মাহাত্ম্য অতি সমুচ্চ, আপনার প্রশংসা অতি মহান এবং আপনি ব্যতীত কোনো সত্য মাবুদ নেই।",
                    "• প্রথম তাকবিরে (তাকবিরে তাহরিমা) উভয় হাত কান পর্যন্ত উঠিয়ে নাভির নিচে বা বুকের ওপর হাত বাঁধতে হবে।\n" +
                    "• এরপর নিঃশব্দে এই বিশেষ ছানাটি পাঠ করতে হবে।\n" +
                    "• সাধারণ সালাতের ছানার চেয়ে জানাযার ছানায় অতিরিক্ত শব্দ হলো 'وَجَلَّ ثَنَاؤُكَ' (ওয়া জাল্লা সানা-উকা — 'আপনার প্রশংসা অতি মহান')।",
                    "সুনানে আবু দাউদ: ৭৭৫, সুনানে তিরমিযী: ২৪২, সুনানে নাসাঈ: ৮৯৯"
            ));

            items.add(new JanazaContentItem(
                    "২. জানাযা নামাযে সূরা ফাতিহা পাঠের হুকুম ও চার মাযহাবের তাহকিক",
                    "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمَٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                    "বিসমিল্লাহির রাহমানির রাহিম। আলহামদু লিল্লাহি রাব্বিল আলামিন। আর-রাহমানির রাহিম। মালিকি ইয়াওমিদ্দীন। ইয়্যাকা না'বুদু ওয়া ইয়্যাকা নাস্তাঈন। ইহদিনাস সিরাতাল মুস্তাকীম। সিরাতাল্লাযীনা আনআমতা আলাইহিম, গাইরিল মাগদূবি আলাইহিম ওয়ালাদ-দ্বোয়াল্লীন।",
                    "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু। সমস্ত প্রশংসা আল্লাহর যিনি সমস্ত সৃষ্টির পালনকর্তা। যিনি পরম করুণাময়, অতি দয়ালু। যিনি বিচার দিনের মালিক। আমরা একমাত্র আপনারই ইবাদত করি এবং আপনারই কাছে সাহায্য চাই। আমাদের সরল পথ দেখান। তাঁদের পথ যাঁদের আপনি নিয়ামত দিয়েছেন, তাঁদের নয় যাঁদের ওপর আপনার ক্রোধ বর্ষিত হয়েছে এবং যারা পথভ্রষ্ট হয়েছে।",
                    "• শাফেয়ী ও হাম্বলী মাযহাবে: প্রথম তাকবিরের পর সূরা ফাতিহা পাঠ করা ওয়াজিব/রুকন। হযরত ইবনে আব্বাস (রা.) জানাযায় সূরা ফাতিহা পড়ে বলেছিলেন, 'যাতে তোমরা জানতে পারো যে এটি সুন্নাত।' (সহীহ বুখারী: ১৩৩৫)\n" +
                    "• হানাফী মাযহাবে: প্রথম তাকবিরের পর ছানা পড়া সুন্নত এবং দোয়ার নিয়তে সূরা ফাতিহা পড়া জায়েয।\n" +
                    "• কিরাতের স্বর: জানাযা সালাতে দিনে বা রাতে সবসময়েই কিরাত ও দোয়াসমূহ নিঃশব্দে (সিররি) পাঠ করতে হয়।",
                    "সহীহ বুখারী: ১৩৩৫, সুনানে নাসাঈ: ১৯৮৭"
            ));
        } else {
            items.add(new JanazaContentItem(
                    "1. First Takbeer and Sana (Thana) Supplication",
                    "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَى جَدُّكَ، وَجَلَّ ثَنَاؤُكَ، وَلَا إِلَهَ غَيْرُكَ",
                    "Subhanakallahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa jalla thana'uka, wa la ilaha ghayruk.",
                    "Glory be to You, O Allah, and all praise is for You. Blessed is Your Name, exalted is Your Majesty, magnificent is Your Praise, and there is no deity worthy of worship except You.",
                    "• In the 1st Takbeer (Takbirat al-Tahrimah), raise both hands up to the ears/shoulders, then clasp them below the navel or over the chest.\n" +
                    "• Recite this special Sana silently.\n" +
                    "• The phrase 'wa jalla thana'uka' (and magnificent is Your Praise) is distinctively recited in Janaza Sana.",
                    "Sunan Abi Dawud: 775, Sunan at-Tirmidhi: 242, Sunan an-Nasa'i: 899"
            ));

            items.add(new JanazaContentItem(
                    "2. Recitation of Surah Al-Fatihah across Madhhabs",
                    "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمَٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                    "Bismillahir Rahmanir Rahim. Al-hamdu lillahi Rabbil 'alamin. Ar-Rahmanir Rahim. Maliki yawmid-din. Iyyaka na'budu wa iyyaka nasta'in. Ihdinas-siratal mustaqim. Siratal-ladhina an'amta 'alayhim, ghayril maghdubi 'alayhim wa lad-dallin.",
                    "In the Name of Allah, the Most Gracious, the Most Merciful. All praise is for Allah, Lord of all the worlds. The Most Gracious, the Most Merciful. Master of the Day of Judgment. You alone we worship, and You alone we ask for help. Guide us to the straight path. The path of those upon whom You have bestowed favor, not of those who have evoked anger or of those who are astray.",
                    "• In Shafi'i and Hanbali Madhhab: Reciting Surah Al-Fatihah after the 1st Takbeer is a mandatory pillar (Rukn). Ibn Abbas (RA) recited it aloud in Janaza and stated, 'So that you may know it is the Sunnah.' (Sahih Bukhari: 1335)\n" +
                    "• In Hanafi Madhhab: Sana is recited as Sunnah, and Surah Al-Fatihah may be recited with the intention of Dua.\n" +
                    "• Tone of Recitation: All recitations and supplications in Janaza are performed silently (Sirri) both during the day and night.",
                    "Sahih Bukhari: 1335, Sunan an-Nasa'i: 1987"
            ));
        }

        return new JanazaTopic(
                "topic_sana",
                isBn ? "নিয়তের পরে ছানা" : "Sana after First Takbeer",
                isBn ? "প্রথম তাকবিরের পর বিশেষ ছানা, সূরা ফাতিহা ও কিরাতের ফিকহী বিধান" : "Special Sana, Surah Fatihah recitation and silent prayer rulings",
                R.drawable.ic_janaza_sana,
                isBn ? "প্রথম তাকবিরের পর আল্লাহর বড়ত্ব ও প্রশংসাসূচক ছানা পাঠের বিধান।" : "Rulings on praising Allah after the first Takbeer.",
                items
        );
    }

    // ==========================================
    // 3. দুরূদ শরীফ
    // ==========================================
    private static JanazaTopic getTopicDurood(boolean isBn) {
        List<JanazaContentItem> items = new ArrayList<>();
        if (isBn) {
            items.add(new JanazaContentItem(
                    "১. পূর্ণাঙ্গ দুরূদে ইবরাহীম (দ্বিতীয় তাকবিরের পর)",
                    "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ، كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ. اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ، كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ",
                    "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিঁও ওয়া আলা আলি মুহাম্মাদ, কামা সাল্লাইতা আলা ইবরাহিমা ওয়া আলা আলি ইবরাহিম, ইন্নাকা হামিদুম মাজিদ। আল্লাহুম্মা বারিক আলা মুহাম্মাদিঁও ওয়া আলা আলি মুহাম্মাদ, কামা বারাকতা আলা ইবরাহিমা ওয়া আলা আলি ইবরাহিম, ইন্নাকা হামিদুম মাজিদ।",
                    "হে আল্লাহ! আপনি মুহাম্মদ ﷺ এবং তাঁর পরিবারের ওপর রহমত বর্ষণ করুন, যেভাবে আপনি ইবরাহীম (আ.) এবং তাঁর পরিবারের ওপর রহমত বর্ষণ করেছিলেন। নিশ্চয় আপনি প্রশংসিত ও মহিমান্বিত। হে আল্লাহ! আপনি মুহাম্মদ ﷺ এবং তাঁর পরিবারের ওপর বরকত নাযিল করুন, যেভাবে আপনি ইবরাহীম (আ.) এবং তাঁর পরিবারের ওপর বরকত নাযিল করেছিলেন। নিশ্চয় আপনি প্রশংসিত ও মহিমান্বিত।",
                    "• দ্বিতীয় তাকবিরের পর হাত না উঠিয়ে হাত বাঁধা অবস্থায় এই দুরূদটি পাঠ করতে হবে।\n" +
                    "• জানাযা সালাতে দুরূদে ইবরাহীম পাঠ করা সর্বশ্রেষ্ঠ ও সর্বাধিক সওয়াবপূর্ণ সুন্নাত আমল।\n" +
                    "• ইমাম ও মুক্তাদি সবাই মনে মনে নিঃশব্দে দুরূদ পাঠ করবেন।",
                    "সহীহ বুখারী: ৩৩৭০, সহীহ মুসলিম: ৪০৬"
            ));

            items.add(new JanazaContentItem(
                    "২. সংক্ষিপ্ত দরূদ (জরুরি ক্ষেত্রে)",
                    "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ النَّبِيِّ الْأُمِّيِّ وَعَلَى آلِهِ وَسَلِّمْ",
                    "আল্লাহুম্মা সাল্লি আলা সাইয়্যিদিনা মুহাম্মাদিনিন নাবিয়্যিল উম্মিয়্যি ওয়া আলা আলিহি ওয়া সাল্লিম।",
                    "হে আল্লাহ! উম্মী নবী মুহাম্মদ ﷺ এবং তাঁর পরিবারের ওপর রহমত ও শান্তি বর্ষণ করুন।",
                    "• যদি ইমাম দ্রুত তৃতীয় তাকবির দিয়ে ফেলেন এবং পুরো দুরূদে ইবরাহীম শেষ করার সময় না থাকে, তবে এই সংক্ষিপ্ত দরূদ পড়ে পরবর্তী তাকবিরে শরিক হওয়া যাবে।",
                    "সুনানে আবু দাউদ: ৯৮১"
            ));
        } else {
            items.add(new JanazaContentItem(
                    "1. Complete Durood-e-Ibrahim (After Second Takbeer)",
                    "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ، كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ. اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ، كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ",
                    "Allahumma salli 'ala Muhammadin wa 'ala ali Muhammad, kama sallayta 'ala Ibrahima wa 'ala ali Ibrahim, innaka Hamidum Majid. Allahumma barik 'ala Muhammadin wa 'ala ali Muhammad, kama barakta 'ala Ibrahima wa 'ala ali Ibrahim, innaka Hamidum Majid.",
                    "O Allah, send peace upon Muhammad and upon the family of Muhammad, as You sent peace upon Ibrahim and upon the family of Ibrahim. Indeed, You are Praiseworthy and Glorious. O Allah, send blessings upon Muhammad and upon the family of Muhammad, as You sent blessings upon Ibrahim and upon the family of Ibrahim. Indeed, You are Praiseworthy and Glorious.",
                    "• After the second Takbeer, keep hands folded and recite this Durood silently.\n" +
                    "• Reciting Durood-e-Ibrahim is the most virtuous and authentic Sunnah in funeral prayer.\n" +
                    "• Both Imam and followers recite it silently.",
                    "Sahih Bukhari: 3370, Sahih Muslim: 406"
            ));

            items.add(new JanazaContentItem(
                    "2. Short Salawat (In case of haste)",
                    "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ النَّبِيِّ الْأُمِّيِّ وَعَلَى آلِهِ وَسَلِّمْ",
                    "Allahumma salli 'ala sayyidina Muhammadinin-nabiyyil-ummiyyi wa 'ala alihi wa sallim.",
                    "O Allah, bestow mercy and peace upon our Master Muhammad, the unlettered Prophet, and upon his family.",
                    "• If the Imam proceeds quickly to the 3rd Takbeer before you finish Durood-e-Ibrahim, this short Salawat is sufficient.",
                    "Sunan Abi Dawud: 981"
            ));
        }

        return new JanazaTopic(
                "topic_durood",
                isBn ? "দুরূদ শরীফ" : "Durood Sharif after Second Takbeer",
                isBn ? "দ্বিতীয় তাকবিরের পর রাসুলুল্লাহ ﷺ এর ওপর দরূদ পাঠের নিয়ম" : "Sending blessings upon Prophet Muhammad ﷺ after 2nd Takbeer",
                R.drawable.ic_janaza_durood,
                isBn ? "দ্বিতীয় তাকবিরের পর দরূদে ইবরাহীম পাঠের হুকুম ও নিয়ম।" : "Guidelines on reciting Durood-e-Ibrahim after second Takbeer.",
                items
        );
    }

    // ==========================================
    // 4. জানাযার দোয়া
    // ==========================================
    private static JanazaTopic getTopicDuas(boolean isBn) {
        List<JanazaContentItem> items = new ArrayList<>();
        if (isBn) {
            items.add(new JanazaContentItem(
                    "১. প্রাপ্তবয়স্ক পুরুষ ও নারীর জন্য জানাযার দোয়া",
                    "اللَّهُمَّ اغْفِرْ لِحَيِّنَا وَمَيِّتِنَا، وَشَاهِدِنَا وَغَائِبِنَا، وَصَغِيرِنَا وَكَبِيرِنَا، وَذَكَرِنَا وَأُنْثَانَا، اللَّهُمَّ مَنْ أَحْيَيْتَهُ مِنَّا فَأَحْيِهِ عَلَى الْإِسْلَامِ، وَمَنْ تَوَفَّيْتَهُ مِنَّا فَتَوَفَّهُ عَلَى الْإِيمَانِ، اللَّهُمَّ لَا تَحْرِمْنَا أَجْرَهُ، وَلَا تَفْتِنَّا بَعْدَهُ",
                    "আল্লাহুম্মাগফির লিহাইয়্যিনা ওয়া মাইয়্যিতিনা, ওয়া শাহিদিনা ওয়া গা-ইবিনা, ওয়া সাগীরিনা ওয়া কাবীরিনা, ওয়া জাকারিনা ওয়া উনছা-না। আল্লাহুম্মা মান আহ্ইয়াইতাহু মিন্না ফাআহয়িহি আলাল ইসলাম, ওয়া মান তাওয়াফ্ফাইতাহু মিন্না ফাতাওয়াফ্ফাহু আলাল ঈমান। আল্লাহুম্মা লা তাহরিমনা আজরাহু, ওয়া লা তাফতিন্না বা’দাহু।",
                    "হে আল্লাহ! আমাদের জীবিত ও মৃত, উপস্থিত ও অনুপস্থিত, ছোট ও বড়, এবং পুরুষ ও নারীদের ক্ষমা করে দিন। হে আল্লাহ! আমাদের মধ্য থেকে আপনি যাকে জীবিত রাখবেন তাকে ইসলামের ওপর জীবিত রাখুন এবং যাকে মৃত্যু দান করবেন তাকে ঈমানের ওপর মৃত্যু দান করুন। হে আল্লাহ! এর সওয়াব থেকে আমাদের বঞ্চিত করবেন না এবং এর পর আমাদের কোনো ফিতনায় ফেলবেন না।",
                    "• তৃতীয় তাকবিরের পর হাত বাঁধা অবস্থায় মাইয়্যিত প্রাপ্তবয়স্ক পুরুষ বা নারী হলে এই দোয়াটি পড়া সুন্নাত।\n" +
                    "• এটি জানাযার সর্বাধিক প্রসিদ্ধ ও সহীহ হাদিস দ্বারা প্রমাণিত সর্বজনীন দোয়া।",
                    "সুনানে আবু দাউদ: ৩২০১, সুনানে তিরমিযী: ১০২৪, সুনানে ইবনে মাজাহ: ১৪৯৮ (সহীহ)"
            ));

            items.add(new JanazaContentItem(
                    "২. অপ্রাপ্তবয়স্ক নাবালক ছেলের জানাযার দোয়া",
                    "اللَّهُمَّ اجْعَلْهُ لَنَا فَرَطًا، وَاجْعَلْهُ لَنَا أَجْرًا وَذُخْرًا، وَاجْعَلْهُ لَنَا شَافِعًا وَمُشَفَّعًا",
                    "আল্লাহুম্মাজ‘আলহু লানা ফারাঁত্বাও, ওয়াজ‘আলহু লানা আজরাঁও ওয়া জুখরাঁও, ওয়াজ‘আলহু লানা শা-ফি‘আঁও ওয়া মুশাফ্ফা‘আ।",
                    "হে আল্লাহ! এই শিশুকে আমাদের জন্য অগ্রগামী সাহায্যকারী বানিয়ে দিন, তাকে আমাদের জন্য প্রতিদান ও সঞ্চয় বানিয়ে দিন এবং তাকে আমাদের জন্য সুপারিশকারী ও গৃহীত সুপারিশকারী করুন।",
                    "• মাইয়্যিত যদি নাবালক শিশু (ছেলে) হয়, তবে তৃতীয় তাকবিরের পর এই দোয়া পড়তে হয়। কারণ নাবালক শিশু নিষ্পাপ, তাদের মাগফিরাতের দোয়ার প্রয়োজন হয় না; বরং তাদের পিতা-মাতার জন্য সুপারিশকারী হওয়ার দোয়া করা হয়।",
                    "কানযুল উম্মাল: ১৫/৭৩১, মিশকাতুল মাসাবীহ"
            ));

            items.add(new JanazaContentItem(
                    "৩. অপ্রাপ্তবয়স্ক নাবালিকা মেয়ের জানাযার দোয়া",
                    "اللَّهُمَّ اجْعَلْهَا لَنَا فَرَطًا، وَاجْعَلْهَا لَنَا أَجْرًا وَذُخْرًا، وَاجْعَلْهَا لَنَا شَافِعَةً وَمُشَفَّعَةً",
                    "আল্লাহুম্মাজ‘আলহা লানা ফারাঁত্বাও, ওয়াজ‘আলহা লানা আজরাঁও ওয়া জুখরাঁও, ওয়াজ‘আলহা লানা শা-ফি‘আতাঁও ওয়া মুশাফ্ফা‘আহ।",
                    "হে আল্লাহ! এই শিশুকন্যাকে আমাদের জন্য অগ্রগামী সাহায্যকারী বানিয়ে দিন, তাকে আমাদের জন্য প্রতিদান ও সঞ্চয় বানিয়ে দিন এবং তাকে আমাদের জন্য সুপারিশকারী ও গৃহীত সুপারিশকারী করুন।",
                    "• মাইয়্যিত যদি নাবালিকা কন্যাশিশু হয়, তবে তৃতীয় তাকবিরের পর স্ত্রীলিঙ্গ শব্দ প্রয়োগ করে এই দোয়া পড়তে হয়।",
                    "ফাতাওয়া শামী: ২/২১৩"
            ));

            items.add(new JanazaContentItem(
                    "৪. রাসূলুল্লাহ ﷺ এর বিশেষ দীর্ঘ জানাযার দোয়া",
                    "اللَّهُمَّ اغْفِرْ لَهُ وَارْحَمْهُ، وَعَافِهِ وَاعْفُ عَنْهُ، وَأَكْرِمْ نُزُلَهُ، وَوَسِّعْ مُدْخَلَهُ، وَاغْسِلْهُ بِالْمَاءِ وَالثَّلْجِ وَالْبَرَدِ، وَنَقِّهِ مِنَ الْخَطَايَا كَمَا نَقَّيْتَ الثَّوْبَ الْأَبْيَضَ مِنَ الدَّنَسِ، وَأَبْدِلْهُ دَارًا خَيْرًا مِنْ دَارِهِ، وَأَهْلًا خَيْرًا مِنْ أَهْلِهِ، وَزَوْجًا خَيْرًا مِنْ زَوْجِهِ، وَأَدْخِلْهُ الْجَنَّةَ، وَأَعِذْهُ مِنْ عَذَابِ الْقَبْرِ، وَمِنْ عَذَابِ النَّارِ",
                    "আল্লাহুম্মাগফির লাহু ওয়ারহামহু, ওয়া আ-ফিহি ওয়া’ফু আনহু, ওয়া আকরিম নুযুলাহু, ওয়া ওয়াদ্দি’ মুদখালাহু, ওয়াগসিলহু বিল মা-য়ি ওয়াছ ছালজি ওয়াল বারাদ, ওয়া নাক্কিহি মিনাল খাতা-য়া কামা নাক্কাইতাছ ছাওবাল আবইয়াদা মিনাদ্দানাস, ওয়া আবদিলহু দা-রান খাইরাম মিন দা-রিহি, ওয়া আহলান খাইরাম মিন আহলিহি, ওয়া যাওজান খাইরাম মিন যাওজিহি, ওয়া আদখিলহুল জান্নাহ, ওয়া আইযহু মিন আযা-বিল কাবরি ওয়া মিন আযা-বিন নার।",
                    "হে আল্লাহ! আপনি তাকে ক্ষমা করে দিন এবং তার ওপর রহম করুন। তাকে পূর্ণ নিরাপত্তা দিন এবং তার ত্রুটি মার্জনা করুন। তার আপ্যায়ন সম্মানিত করুন এবং তার কবর প্রশস্ত করে দিন। তাকে পানি, বরফ ও শিশির দিয়ে ধৌত করে দিন। তাকে গুনাহ থেকে এমনভাবে পরিচ্ছন্ন করুন যেমন সাদা কাপড় ময়লা থেকে পরিষ্কার করা হয়। তাকে তার পার্থিব ঘরের চেয়ে উত্তম ঘর, পরিবারের চেয়ে উত্তম পরিবার এবং জোড়ার চেয়ে উত্তম জোড়া দান করুন। তাকে জান্নাতে প্রবেশ করান এবং কবরের আজাব ও জাহান্নামের শাস্তি থেকে মুক্তি দিন।",
                    "• হযরত আওফ ইবনে মালিক (রা.) বলেন, 'রাসূলুল্লাহ ﷺ জানাযায় এমন সুন্দর করে এই দোয়া পাঠ করলেন যে আমার মনে ইচ্ছা হলো, হায়! যদি আমি এই মাইয়্যিত হতাম!'",
                    "সহীহ মুসলিম: ৯৬৩, সুনানে নাসাঈ: ১৯৮১"
            ));

            items.add(new JanazaContentItem(
                    "৫. মাসবুকের দোয়া ও করণীয় (দেরিতে শরিক হলে)",
                    null,
                    null,
                    null,
                    "• যদি কেউ জানাযার কিছু তাকবির হয়ে যাওয়ার পর জামাতে পৌঁছায়, তবে ইমামের পরবর্তী তাকবিরের সাথে সাথে 'আল্লাহু আকবার' বলে জামাতে শরিক হবে।\n" +
                    "• ইমাম যখন সালাম ফেরাবেন, তখন মাসবুক ব্যক্তি সালাম না ফিরিয়ে দ্রুত ছুটে যাওয়া তাকবিরগুলো ক্রমান্বয়ে পাঠ করবে (যেমন দোয়া না পড়ে শুধু তাকবিরগুলো বলবে যদি খাটিয়া উঠিয়ে ফেলার আশঙ্কা থাকে) এবং সালাম ফিরিয়ে সালাত শেষ করবে।",
                    "ফাতাওয়ায়ে আলমগীরী ১/১৬৫, রদ্দুল মুহতার ২/২১৭"
            ));
        } else {
            items.add(new JanazaContentItem(
                    "1. Janaza Dua for Adult Male & Female",
                    "اللَّهُمَّ اغْفِرْ لِحَيِّنَا وَمَيِّتِنَا، وَشَاهِدِنَا وَغَائِبِنَا، وَصَغِيرِنَا وَكَبِيرِنَا، وَذَكَرِنَا وَأُنْثَانَا، اللَّهُمَّ مَنْ أَحْيَيْتَهُ مِنَّا فَأَحْيِهِ عَلَى الْإِسْلَامِ، وَمَنْ تَوَفَّيْتَهُ مِنَّا فَتَوَفَّهُ عَلَى الْإِيمَانِ، اللَّهُمَّ لَا تَحْرِمْنَا أَجْرَهُ، وَلَا تَفْتِنَّا بَعْدَهُ",
                    "Allahummaghfir lihayyina wa mayyitina, wa shahidina wa gha'ibina, wa saghirina wa kabirina, wa dhakarina wa unthana. Allahumma man ahyaytahu minna fa-ahyihi 'alal-Islam, wa man tawaffaytahu minna fa-tawaffahu 'alal-Iman. Allahumma la tahrimna ajrahu, wa la taftinna ba'dah.",
                    "O Allah, forgive our living and our deceased, those present and those absent, our young and our old, our males and our females. O Allah, whomsoever You keep alive among us, let him live upon Islam, and whomsoever You take away in death, let him die upon Iman. O Allah, do not deprive us of his reward and do not put us to trial after him.",
                    "• Recited silently after the third Takbeer for an adult male or female deceased.\n" +
                    "• This is the most authentic, universal, and widely practiced prophetic supplication.",
                    "Sunan Abi Dawud: 3201, Sunan at-Tirmidhi: 1024, Sunan Ibn Majah: 1498"
            ));

            items.add(new JanazaContentItem(
                    "2. Janaza Dua for a Minor Boy (Child)",
                    "اللَّهُمَّ اجْعَلْهُ لَنَا فَرَطًا، وَاجْعَلْهُ لَنَا أَجْرًا وَذُخْرًا، وَاجْعَلْهُ لَنَا شَافِعًا وَمُشَفَّعًا",
                    "Allahummaj'alhu lana faratan, waj'alhu lana ajran wa dhukhran, waj'alhu lana shafi'an wa mushaffa'a.",
                    "O Allah, make him for us a forerunner, a source of reward and treasure, and make him for us an intercessor whose intercession is accepted.",
                    "• If the deceased is a minor boy, recite this Dua. Since young children are sinless, we pray for them to be an intercessor and eternal treasure for their parents.",
                    "Kanzul Ummal: 15/731, Mishkat al-Masabih"
            ));

            items.add(new JanazaContentItem(
                    "3. Janaza Dua for a Minor Girl (Child)",
                    "اللَّهُمَّ اجْعَلْهَا لَنَا فَرَطًا، وَاجْعَلْهَا لَنَا أَجْرًا وَذُخْرًا، وَاجْعَلْهَا لَنَا شَافِعَةً وَمُشَفَّعَةً",
                    "Allahummaj'alha lana faratan, waj'alha lana ajran wa dhukhran, waj'alha lana shafi'atan wa mushaffa'ah.",
                    "O Allah, make her for us a forerunner, a source of reward and treasure, and make her for us an intercessor whose intercession is accepted.",
                    "• If the deceased is a young girl, recite this Dua using the feminine Arabic grammatical form.",
                    "Fatawa Shami: 2/213"
            ));

            items.add(new JanazaContentItem(
                    "4. Comprehensive Prophetic Dua (Awf ibn Malik)",
                    "اللَّهُمَّ اغْفِرْ لَهُ وَارْحَمْهُ، وَعَافِهِ وَاعْفُ عَنْهُ، وَأَكْرِمْ نُزُلَهُ، وَوَسِّعْ مُدْخَلَهُ، وَاغْسِلْهُ بِالْمَاءِ وَالثَّلْجِ وَالْبَرَدِ، وَنَقِّهِ مِنَ الْخَطَايَا كَمَا نَقَّيْتَ الثَّوْبَ الْأَبْيَضَ مِنَ الدَّنَسِ، وَأَبْدِلْهُ دَارًا خَيْرًا مِنْ دَارِهِ، وَأَهْلًا خَيْرًا مِنْ أَهْلِهِ، وَزَوْجًا خَيْرًا مِنْ زَوْجِهِ، وَأَدْخِلْهُ الْجَنَّةَ، وَأَعِذْهُ مِنْ عَذَابِ الْقَبْرِ، وَمِنْ عَذَابِ النَّارِ",
                    "Allahummaghfir lahu warhamhu, wa 'afihi wa'fu 'anhu, wa akrim nuzulahu, wa wassi' mudkhalahu, waghsilhu bil-ma'i wath-thalji wal-barad, wa naqqihi minal-khataya kama naqqaytath-thawbal-abyada minad-danas, wa abdilhu daran khayram min darihi, wa ahlan khayram min ahlihi, wa zawjan khayram min zawjihi, wa adkhilhul-jannah, wa a'idh-hu min 'adhabil-qabri wa min 'adhabin-nar.",
                    "O Allah, forgive him and have mercy on him, grant him peace and pardon him, make his welcome honorable and expand his entrance. Wash him with water, snow, and hail, and cleanse him of sins as a white garment is cleansed of filth. Give him a home better than his home, a family better than his family, and a spouse better than his spouse. Admit him into Paradise and protect him from the torment of the grave and the punishment of the Fire.",
                    "• Awf ibn Malik (RA) narrated: 'The Prophet ﷺ recited this prayer so beautifully that I wished I had been that deceased person!'",
                    "Sahih Muslim: 963, Sunan an-Nasa'i: 1981"
            ));

            items.add(new JanazaContentItem(
                    "5. Latecomer (Masbuq) Rules in Janaza",
                    null,
                    null,
                    null,
                    "• If a worshipper arrives after the Imam has already completed some Takbeers, enter immediately with the Imam upon his next Takbeer.\n" +
                    "• When the Imam says the final Salam, do not turn Salam; quickly make up the missed Takbeers in sequence (even by saying just the Takbeers without full Duas if there is fear of lifting the bier) and finish with Salam.",
                    "Fatawa Alamgiri 1/165, Radd al-Muhtar 2/217"
            ));
        }

        return new JanazaTopic(
                "topic_duas",
                isBn ? "জানাযার দোয়া" : "Janaza Duas",
                isBn ? "প্রাপ্তবয়স্ক পুরুষ, নারী, অপ্রাপ্তবয়স্ক শিশু ও মাসবুকের নির্দিষ্ট মাসনূন দোয়াসমূহ" : "Masnoon supplications for adult men, women, minor children, and latecomers",
                R.drawable.ic_janaza_dua,
                isBn ? "তৃতীয় তাকবিরের পর পাঠের জন্য বিশুদ্ধ ও প্রামাণিক জানাযার দোয়াসমূহ।" : "Authentic prophetic supplications after the third Takbeer.",
                items
        );
    }

    // ==========================================
    // 5. জানাযার পূর্ণাঙ্গ পদ্ধতি ও কাতার বিন্যাস
    // ==========================================
    private static JanazaTopic getTopicMethod(boolean isBn) {
        List<JanazaContentItem> items = new ArrayList<>();
        if (isBn) {
            items.add(new JanazaContentItem(
                    "১. জানাযা নামাযের স্বরূপ ও মৌলিক শর্ত",
                    null,
                    null,
                    null,
                    "• জানাযা নামাযে কোনো রুকু, সিজদা বা তাশাহহুদ নেই। এটি দাঁড়িয়ে ৪টি তাকবির ও সালামের মাধ্যমে সম্পন্ন হয়।\n" +
                    "• শর্তাবলি: অজু থাকা, শরীর ও কাপড়ের পবিত্রতা, কেবলামুখী হওয়া এবং মাইয়্যিত মুসলিম ও সামনে উপস্থিত থাকা।\n" +
                    "• কাতার বিন্যাস: মুক্তাদিদের ৩, ৫ বা বিজোড় কাতারে দাঁড় করানো মুস্তাহাব। ইমাম মাইয়্যিতের বক্ষ বরাবর (পুরুষের মাথা/বুক এবং নারীর কোমর/মধ্যভাগ) দাঁড়াবেন।",
                    "সহীহ বুখারী: ১৩২৬, সুনানে আবু দাউদ: ৩১৬৮"
            ));

            items.add(new JanazaContentItem(
                    "২. ৪ তাকবিরের ধারাবাহিক আমল",
                    "১ম তাকবির: আল্লাহু আকবার ➔ ছানা পাঠ\n২য় তাকবির: আল্লাহু আকবার ➔ দুরূদে ইবরাহীম\n৩য় তাকবির: আল্লাহু আকবার ➔ জানাযার দোয়া\n৪র্থ তাকবির: আল্লাহু আকবার ➔ সালাম ফিরানো",
                    null,
                    null,
                    "• ১ম তাকবির: মনে মনে নিয়ত করে হাত কান পর্যন্ত উঠিয়ে 'আল্লাহু আকবার' বলে নাভির নিচে/বুকের ওপর হাত বাঁধবে। এরপর ছানা পড়বে।\n" +
                    "• ২য় তাকবির: হাত না উঠিয়ে 'আল্লাহু আকবার' বলবে এবং হাত বাঁধা অবস্থায় দুরূদে ইবরাহীম পাঠ করবে।\n" +
                    "• ৩য় তাকবির: হাত না উঠিয়ে 'আল্লাহু আকবার' বলবে এবং মাইয়্যিতের জন্য নির্দিষ্ট জানাযার দোয়া পাঠ করবে।\n" +
                    "• ৪র্থ তাকবির: হাত না উঠিয়ে 'আল্লাহু আকবার' বলবে। এরপর উভয় হাত বাঁধা অবস্থায় ডানে 'আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ' এবং বামে 'আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ' বলে সালাম ফিরিয়ে হাত ছেড়ে সালাত সম্পন্ন করবে।",
                    "আল-হিদায়া ১/১৮০, ফিকহুস সুন্নাহ ১/৫৩২"
            ));

            items.add(new JanazaContentItem(
                    "৩. তাকবিরে হাত উঠানো ও হাত ছাড়ার ফিকহী বিধান",
                    null,
                    null,
                    null,
                    "• হানাফী মাযহাব মতে: শুধুমাত্র ১ম তাকবিরে উভয় হাত কান পর্যন্ত উঠাতে হবে। বাকি তাকবিরসমূহে হাত উঠাতে হবে না। ৪র্থ তাকবিরের পর উভয় দিকে সালাম শেষ করে হাত ছাড়তে হবে।\n" +
                    "• শাফেয়ী ও হাম্বলী মাযহাব মতে: প্রতি তাকবিরেই দুই হাত কাঁধ/কান পর্যন্ত উঠানো মুস্তাহাব।\n" +
                    "• উভয় পদ্ধতিই নির্ভরযোগ্য ও সুন্নাহসম্মত। ঝগড়া-বিবাদ না করে যার যার মাযহাব অনুযায়ী আমল করাই শ্রেয়।",
                    "সহীহ বুখারী: ১৩৩৫, মুসান্নাফে ইবনে আবি শাইবা: ৩/২৯৫"
            ));

            items.add(new JanazaContentItem(
                    "৪. ইমামের অবস্থান ও কাতার বিন্যাসের নিয়ম",
                    null,
                    null,
                    null,
                    "• ইমামের অবস্থান: পুরুষ মাইয়্যিত হলে ইমাম তার মাথা বা বক্ষ বরাবর দাঁড়াবেন। আর মহিলা মাইয়্যিত হলে তার কোমর বা মাঝ বরাবর দাঁড়াবেন (সহীহ বুখারী: ১৩৩১, সুনানে আবু দাউদ: ৩১৯৪)।\n" +
                    "• কাতার বিন্যাস: জানাযায় অন্তত ৩টি কাতার করা মুস্তাহাব। লোক কম হলেও ৩ কাতারে ভাগ করে দাঁড় করানো উত্তম। হাদিসে এসেছে: 'যার জানাযায় ৩ কাতার মুসলিম দাঁড়ায়, তার জন্য মাগফিরাত অবধারিত হয়ে যায়।' (আবু দাউদ: ৩১৬৬)",
                    "সুনানে আবু দাউদ: ৩১৬৬, সহীহ বুখারী: ১৩৩১"
            ));
        } else {
            items.add(new JanazaContentItem(
                    "1. Nature and Core Conditions of Janaza",
                    null,
                    null,
                    null,
                    "• The funeral prayer has no Ruku (bowing), Sujud (prostration), or Tashahhud (sitting). It is performed entirely standing with 4 Takbeers and concluding with Salam.\n" +
                    "• Prerequisites: Wudu/purity of body and clothes, facing the Qiblah, and the deceased being a Muslim placed in front.\n" +
                    "• Rows: It is recommended to make an odd number of rows (3, 5, etc.). The Imam stands level with the chest of a male deceased, and level with the middle/waist of a female deceased.",
                    "Sahih Bukhari: 1326, Sunan Abi Dawud: 3168"
            ));

            items.add(new JanazaContentItem(
                    "2. Step-by-Step Procedure of 4 Takbeers",
                    "1st Takbeer: Allahu Akbar ➔ Recite Sana / Fatihah\n2nd Takbeer: Allahu Akbar ➔ Recite Durood-e-Ibrahim\n3rd Takbeer: Allahu Akbar ➔ Recite Janaza Dua\n4th Takbeer: Allahu Akbar ➔ Turn Salam to End",
                    null,
                    null,
                    "• 1st Takbeer: Make intention in heart, raise hands and say 'Allahu Akbar', fold hands over chest or below navel, and recite Sana.\n" +
                    "• 2nd Takbeer: Say 'Allahu Akbar' without raising hands, keep hands folded, and recite Durood-e-Ibrahim.\n" +
                    "• 3rd Takbeer: Say 'Allahu Akbar' without raising hands, and recite the specific Janaza Dua for the deceased.\n" +
                    "• 4th Takbeer: Say 'Allahu Akbar' without raising hands, pause briefly in silence, then turn head right saying 'Assalamu Alaykum wa Rahmatullah' and left saying 'Assalamu Alaykum wa Rahmatullah', then lower hands.",
                    "Al-Hidayah 1/180, Fiqh us-Sunnah 1/532"
            ));

            items.add(new JanazaContentItem(
                    "3. Jurisprudence of Raising and Lowering Hands",
                    null,
                    null,
                    null,
                    "• In Hanafi Madhhab: Hands are raised only during the 1st Takbeer. In the remaining three Takbeers, hands remain folded and are released only after turning Salam.\n" +
                    "• In Shafi'i & Hanbali Madhhabs: Hands are raised up to shoulders/ears at every Takbeer.\n" +
                    "• Both practices are firmly grounded in authentic Sunnah.",
                    "Sahih Bukhari: 1335, Musannaf Ibn Abi Shaybah 3/295"
            ));

            items.add(new JanazaContentItem(
                    "4. Position of Imam and Formation of Rows",
                    null,
                    null,
                    null,
                    "• Position of Imam: For a deceased male, the Imam stands aligned with his head or chest. For a deceased female, the Imam stands aligned with her waist/midsection (Sahih Bukhari: 1331).\n" +
                    "• Formation of Rows: It is recommended to make at least 3 rows, even if the congregation is small. The Prophet ﷺ said: 'Whosoever has three rows of Muslims pray over him, forgiveness becomes binding for him.' (Abu Dawud: 3166)",
                    "Sunan Abi Dawud: 3166, Sahih Bukhari: 1331"
            ));
        }

        return new JanazaTopic(
                "topic_method",
                isBn ? "জানাযার পদ্ধতি" : "Method of Janaza Salah",
                isBn ? "৪ তাকবিরের ধারাবাহিক আমল, হাত বাঁধা-ছাড়ার নিয়ম, কাতার বিন্যাস ও সালাম" : "Step-by-step 4 Takbeers procedure, hand positions, rows and Salam",
                R.drawable.ic_janaza_method,
                isBn ? "জানাযা নামাযের ধারাবাহিক ও নির্ভুল পদ্ধতি।" : "Step-by-step guide to offering Janaza prayer.",
                items
        );
    }

    // ==========================================
    // 6. গায়েবী জানাযা
    // ==========================================
    private static JanazaTopic getTopicGayebee(boolean isBn) {
        List<JanazaContentItem> items = new ArrayList<>();
        if (isBn) {
            items.add(new JanazaContentItem(
                    "১. গায়েবী জানাযার পরিচিতি ও প্রেক্ষাপট",
                    null,
                    null,
                    null,
                    "• গায়েবী জানাযা (صلاة الغائب) বলতে বোঝায়: মৃত ব্যক্তি সামনে উপস্থিত না থাকা সত্ত্বেও দূরবর্তী স্থান থেকে তার জন্য জানাযা নামায আদায় করা।\n" +
                    "• এর মূল ভিত্তি হলো হাবশার (বর্তমান ইথিওপিয়া) শাসক বাদশাহ আসহামা নাজ্জাশী (রহ.)-এর ওফাত। তিনি গোপনে ইসলাম গ্রহণ করেছিলেন। তাঁর মৃত্যুর দিন রাসূলুল্লাহ ﷺ মদিনায় সাহাবিদের নিয়ে গায়েবানা জানাযা আদায় করেছিলেন।",
                    "সহীহ বুখারী: ১৩২৭, সহীহ মুসলিম: ৯৫১"
            ));

            items.add(new JanazaContentItem(
                    "২. বাদশাহ নাজ্জাশীর জানাযার সহীহ হাদিস",
                    "أَنَّ رَسُولَ اللَّهِ ﷺ نَعَى النَّجَاشِيَ فِي الْيَوْمِ الَّذِي مَاتَ فِيهِ، وَخَرَجَ بِهِمْ إِلَى الْمُصَلَّى، فَصَفَّ بِهِمْ وَكَبَّرَ أَرْبَعَ تَكْبِيرَاتٍ",
                    "আন্না রাসূলাল্লাহি ﷺ না'আন নাজাশিয়া ফিল ইয়াওমিল লাযী মাতা ফিহি, ওয়া খরাজা বিহিম ইলাল মুসল্লা, ফাসাফ্ফা বিহিম ওয়া কাব্বারা আরবা'আ তাকবিরাত।",
                    "রাসূলুল্লাহ ﷺ নাজ্জাশীর মৃত্যুর দিনই তাঁর মৃত্যুর সংবাদ ঘোষণা করলেন এবং সাহাবিদের নিয়ে ঈদগাহের ময়দানে বের হলেন। এরপর তিনি সাহাবিদের কাতারবদ্ধ করলেন এবং চার তাকবিরে তাঁর জানাযা সালাত আদায় করলেন।",
                    "• এই হাদিসটি গায়েবী জানাযা সংক্রান্ত প্রধান ঐতিহাসিক দলিল।",
                    "সহীহ বুখারী: ১৩২৭, সহীহ মুসলিম: ৯৫১"
            ));

            items.add(new JanazaContentItem(
                    "৩. চার মাযহাবের ফিকহী পর্যালোচনা ও আমল",
                    null,
                    null,
                    null,
                    "• হানাফী ও মালেকী মাযহাব মতে: সাধারণ ক্ষেত্রে গায়েবী জানাযা বৈধ নয়। নাজ্জাশীর জানাযা ছিল রাসূলুল্লাহ ﷺ এর একটি বিশেষ মুজিযা ও খাস বিধান, কারণ নাজ্জাশী কাফের ভূমিতে ইন্তেকাল করায় সেখানে তাঁর কোনো জানাযা অনুষ্ঠিত হয়নি। যেখানে জানাযা হয়ে গেছে সেখানে দ্বিতীয়বার গায়েবী জানাযা সুন্নত নয়।\n" +
                    "• শাফেয়ী ও হাম্বলী মাযহাব মতে: কোনো মুসলিম যদি দূরবর্তী স্থানে মারা যায় এবং সেখানে তাঁর কোনো জানাযা অনুষ্ঠিত না হয় (যেমন সমুদ্রে ডুবে বা নিখোঁজ হয়ে মৃত্যুবরণ), তবে তাঁর জন্য গায়েবী জানাযা পড়া জায়েয ও মুস্তাহাব।\n" +
                    "• ইমাম ইবনে তাইমিয়্যাহ (রহ.)-এর তাহকিক: যদি কোনো মুসলিমের ওপর জানাযা না হয়ে থাকে তবে গায়েবী জানাযা পড়বে। আর যদি ইতোমধ্যে তাঁর জানাযা পড়া হয়ে থাকে তবে দূর থেকে তাঁর জন্য দোয়া ও ইস্তিগফার করাই যথেষ্ট।",
                    "আল-মাজমু' ৫/২৫৩, মাজমুউল ফাতাওয়া ২৪/৩৮৩"
            ));
        } else {
            items.add(new JanazaContentItem(
                    "1. Introduction to Absentee Funeral Prayer",
                    null,
                    null,
                    null,
                    "• Salat al-Gha'ib (صلاة الغائب) refers to performing the funeral prayer for a deceased Muslim when the body is not physically present in front of the congregation.\n" +
                    "• The prime historical evidence is the funeral prayer performed by Prophet Muhammad ﷺ for the Christian King of Abyssinia (Ethiopia), Ashamah an-Najashi, who embraced Islam secretly.",
                    "Sahih Bukhari: 1327, Sahih Muslim: 951"
            ));

            items.add(new JanazaContentItem(
                    "2. Authentic Hadith on King Najashi's Funeral",
                    "أَنَّ رَسُولَ اللَّهِ ﷺ نَعَى النَّجَاشِيَ فِي الْيَوْمِ الَّذِي مَاتَ فِيهِ، وَخَرَجَ بِهِمْ إِلَى الْمُصَلَّى، فَصَفَّ بِهِمْ وَكَبَّرَ أَرْبَعَ تَكْبِيرَاتٍ",
                    "Anna Rasulallahi ﷺ na'an-Najashiya fil-yawmil-ladhi mata fih, wa kharaja bihim ilal-musalla, fasaffa bihim wa kabbara arba'a takbirat.",
                    "The Messenger of Allah ﷺ announced the death of Najashi on the day he died, took the Companions to the prayer ground, lined them up in rows, and recited four Takbeers in funeral prayer for him.",
                    "• This narration is the primary source regarding absentee funeral prayer.",
                    "Sahih Bukhari: 1327, Sahih Muslim: 951"
            ));

            items.add(new JanazaContentItem(
                    "3. Four Madhhab Perspectives and Practical Rulings",
                    null,
                    null,
                    null,
                    "• Hanafi & Maliki View: Absentee funeral prayer is generally not permissible. The prayer for Najashi was a unique miraculous exception specific to the Prophet ﷺ because no Muslim was present in Abyssinia to offer his funeral.\n" +
                    "• Shafi'i & Hanbali View: Permissible and recommended if a Muslim dies in a distant land where no funeral prayer was held (e.g., lost at sea, plane crash, or in non-Muslim territory).\n" +
                    "• Imam Ibn Taymiyyah's synthesis: If funeral prayer was not offered for the deceased, pray Salat al-Gha'ib. If a prayer was already held, praying for his forgiveness (Dua & Istighfar) from afar is the Sunnah practice.",
                    "Al-Majmu' 5/253, Majmu' al-Fatawa 24/383"
            ));
        }

        return new JanazaTopic(
                "topic_gayebee",
                isBn ? "গায়েবী জানাযা" : "Absentee Funeral Prayer (Salat al-Gha'ib)",
                isBn ? "শরিয়তের বিধান, বাদশা নাজ্জাশীর হাদিস ও চার মাযহাবের তাহকিক" : "Shariah rulings, Hadith of King Najashi, and Madhhab viewpoints",
                R.drawable.ic_janaza_gayebee,
                isBn ? "গায়েবী জানাযা সংক্রান্ত সহীহ হাদিস ও ফিকহী পর্যালোচনা।" : "Islamic rulings regarding funeral prayer in absentia.",
                items
        );
    }

    // ==========================================
    // 7. মৃত ব্যক্তির গোসল ও কাফন
    // ==========================================
    private static JanazaTopic getTopicGhusl(boolean isBn) {
        List<JanazaContentItem> items = new ArrayList<>();
        if (isBn) {
            items.add(new JanazaContentItem(
                    "১. মাইয়্যিতকে গোসল করানোর হুকুম ও গুরুত্ব",
                    null,
                    null,
                    null,
                    "• মৃত মুসলিমকে গোসল করানো ফরযে কেফায়া।\n" +
                    "• গোসলদাতার যোগ্যতা: গোসলদাতাকে বিশ্বস্ত, পরহেযগার ও গোসলের নিয়ম সম্পর্কে অভিজ্ঞ হতে হবে। মাইয়্যিতের কোনো ত্রুটি দেখলে তা গোপন রাখা আবশ্যক।\n" +
                    "• পুরুষকে পুরুষ এবং নারীকে নারী গোসল করাবেন। স্বামী-স্ত্রীর ক্ষেত্রে স্ত্রী স্বামীকে গোসল করাতে পারবেন।",
                    "সুনানে ইবনে মাজাহ: ১৪৬২, সুনানে আবু দাউদ: ৩১৪১"
            ));

            items.add(new JanazaContentItem(
                    "২. ধাপে ধাপে গোসল দেওয়ার মাসনূন পদ্ধতি",
                    null,
                    null,
                    null,
                    "১. গোসলের চৌকি বা তখতায় মাইয়্যিতকে রেখে নাভি থেকে হাঁটু পর্যন্ত কাপড় দিয়ে ঢেকে রাখবে।\n" +
                    "২. পেটে আলতো চাপ দিয়ে কোনো নাপাকি থাকলে তা বের করে ধুয়ে দেবে। গোসলদাতার হাতে কাপড় পেঁচিয়ে ইস্তিঞ্জা করাবে (সতরে দৃষ্টি না দিয়ে)।\n" +
                    "৩. নামাজের অজুর মতো অজু করাবে। তবে নাকে ও মুখে পানি প্রবেশ করাবে না; বরং ভেজা কাপড়/তুলা দিয়ে ঠোঁট, দাঁত ও নাক পরিষ্কার করবে।\n" +
                    "৪. কুসুম গরম পানিতে কুল পাতা বা সাবান মিশিয়ে মাথা ও দাড়ি পরিষ্কার করবে।\n" +
                    "৫. প্রথমে ডান কাতে ফিরিয়ে পানি ঢেলে ডান পাশ এবং পরে বাম কাতে ফিরিয়ে বাম পাশ ধৌত করবে।\n" +
                    "৬. শেষে কর্পূর বা সুগন্ধি মিশ্রিত পানি দিয়ে মাথা থেকে পা পর্যন্ত ঢেলে দেবে (৩, ৫ বা ৭ বার বেজোড় সংখ্যায় ধৌত করা সুন্নত)।\n" +
                    "৭. নরম শুকনো তোয়ালে দিয়ে শরীর মুছে কাফনের কাপড়ে স্থানান্তর করবে।",
                    "সহীহ বুখারী: ১২৫৩, সহীহ মুসলিম: ৯৩৯ (উম্মে আতিয়্যাহ রা. বর্ণিত হাদিস)"
            ));

            items.add(new JanazaContentItem(
                    "৩. পুরুষের কাফনের ৩ কাপড় ও পরিমাপের পুঙ্খানুপুঙ্খ বিবরণ",
                    null,
                    null,
                    null,
                    "• পুরুষের কাফনের কাপড় মোট ৩টি:\n" +
                    "  ১. লেফাফা (বড় চাদর): মাথা থেকে পা পর্যন্ত মাইয়্যিতের দৈর্ঘ্যের চেয়ে দুই হাত বেশি দীর্ঘ যাতে দুই মাথায় গিট দেওয়া যায়।\n" +
                    "  ২. ইযার (লুঙ্গি বা তেহবন্দ): মাথা থেকে পা পর্যন্ত মাইয়্যিতের সম্পূর্ণ দেহের সমান মাপের।\n" +
                    "  ৩. কামিস (জামা বা কুর্তা): কাঁধ বা গলা থেকে হাঁটু পর্যন্ত বা পায়ের নলা পর্যন্ত বিস্তৃত, কিন্তু এতে কোনো হাতা বা বোতাম থাকে না।\n" +
                    "• কাপড়ের রঙ: সাদা সুতি কাপড় হওয়া মুস্তাহাব। কাফনে লোবান, আতর ও কর্পূর লাগানো সুন্নত।",
                    "সহীহ বুখারী: ১২৬৪, সুনানে আবু দাউদ: ৩৮৭৮"
            ));

            items.add(new JanazaContentItem(
                    "৪. নারীর কাফনের ৫ কাপড় ও পর্দার বিশেষ পরিমাপ",
                    null,
                    null,
                    null,
                    "• নারীর কাফনের কাপড় মোট ৫টি:\n" +
                    "  ১. লেফাফা (বড় চাদর): মাথা থেকে পায়ের চেয়ে দুই হাত বড় (সবচেয়ে নিচের বড় চাদর)।\n" +
                    "  ২. ইযার (লুঙ্গি): মাথা থেকে পা পর্যন্ত।\n" +
                    "  ৩. কামিস (জামা): কাঁধ থেকে পায়ের গোড়ালি পর্যন্ত।\n" +
                    "  ৪. ওড়না / খিমার / সিরবান্দ (মাথার কাপড়): এক থেকে দেড় গজ লম্বা, যা দিয়ে মাথার চুল দুই ভাগে ভাগ করে বুকের ওপর রেখে ওড়না দিয়ে মাথা ও চুল ঢেকে দেওয়া হয়।\n" +
                    "  ৫. সিনাবান্দ (বুকের চাদর বা বন্ধনী): বগল থেকে হাঁটু বা কোমর পর্যন্ত প্রশস্ত, যা কামিসের ওপর দিয়ে বুকের অংশ শক্তভাবে পেঁচিয়ে বাঁধা হয় যাতে সতর ও বক্ষদেশ সম্পূর্ণ সংরক্ষিত থাকে।",
                    "সুনানে আবু দাউদ: ৩১৪৪, আল-হিদায়া ১/১৮৫"
            ));
        } else {
            items.add(new JanazaContentItem(
                    "1. Ruling and Importance of Ghusl for Deceased",
                    null,
                    null,
                    null,
                    "• Washing the deceased Muslim is Fardh al-Kifayah (communal obligation).\n" +
                    "• Qualifications of the Washer: Must be trustworthy, pious, and knowledgeable in the Sunnah procedure. They must strictly conceal any defects observed on the body.\n" +
                    "• Males wash males, and females wash females. A wife is unanimously permitted to wash her deceased husband.",
                    "Sunan Ibn Majah: 1462, Sunan Abi Dawud: 3141"
            ));

            items.add(new JanazaContentItem(
                    "2. Step-by-Step Sunnah Washing Procedure",
                    null,
                    null,
                    null,
                    "1. Place the body on a washing table and cover the 'Awrah (navel to knees) with an opaque sheet.\n" +
                    "2. Gently press the abdomen to expel impurities and cleanse the private parts using a cloth-wrapped hand without looking at the 'Awrah.\n" +
                    "3. Perform complete Wudu for the deceased (without pouring water into nose and mouth; clean teeth and nostrils with damp cotton).\n" +
                    "4. Wash head and beard with lukewarm water infused with berry leaves (Sidr) or mild soap.\n" +
                    "5. Turn the body on its left side and wash the right side; then turn on its right side and wash the left side.\n" +
                    "6. Pour camphor-infused water from head to toe for the final wash (wash 3, 5, or 7 odd times as prescribed by Sunnah).\n" +
                    "7. Gently dry the body with a clean towel and transfer to the shrouding garments.",
                    "Sahih Bukhari: 1253, Sahih Muslim: 939 (Hadith of Umm Atiyyah RA)"
            ));

            items.add(new JanazaContentItem(
                    "3. Men's Shroud (3 Garments and Exact Measurements)",
                    null,
                    null,
                    null,
                    "• Men's Kafan consists of 3 white garments:\n" +
                    "  1. Lifafah (Outer Sheet): Longer than the body by about two cubits (one foot at each end) to tie knots at head and feet.\n" +
                    "  2. Izar (Inner Wrap): Covers from head to toe.\n" +
                    "  3. Qamis (Sleeveless Tunic): Extends from shoulders down to the shins or knees without seams or buttons.\n" +
                    "• Color & Fragrance: Pure white cotton is Sunnah, perfumed with Attar and camphor.",
                    "Sahih Bukhari: 1264, Sunan Abi Dawud: 3878"
            ));

            items.add(new JanazaContentItem(
                    "4. Women's Shroud (5 Garments and Modesty Coverings)",
                    null,
                    null,
                    null,
                    "• Women's Kafan consists of 5 garments for complete modesty:\n" +
                    "  1. Lifafah (Outer Sheet): Outermost wrap longer than the body.\n" +
                    "  2. Izar (Inner Wrap): Extends from head to feet.\n" +
                    "  3. Qamis (Tunic): Extends from shoulders to ankles.\n" +
                    "  4. Khimar / Sirband (Head Veil): Covers hair (braided in two parts and placed over chest) and head.\n" +
                    "  5. Sinaband (Chest Wrap): Wraps firmly from underarms down to thighs/waist over the Qamis to ensure absolute privacy and modesty.",
                    "Sunan Abi Dawud: 3144, Al-Hidayah 1/185"
            ));
        }

        return new JanazaTopic(
                "topic_ghusl",
                isBn ? "মৃত ব্যক্তির গোসল" : "Ghusl and Shrouding (Kafan)",
                isBn ? "গোসল করানোর নিয়ম, বরই পাতা-কর্পূর এবং পুরুষের ৩ ও নারীর ৫ কাপড়ের মাপ" : "Washing method, Sidr leaves, camphor, and shroud measurements for men & women",
                R.drawable.ic_janaza_ghusl,
                isBn ? "মৃত ব্যক্তির গোসল ও কাফন সংক্রান্ত সুন্নাহ নির্দেশিকা।" : "Complete Sunnah guidelines for washing and shrouding the deceased.",
                items
        );
    }

    // ==========================================
    // 8. জানাযার নামাযের পদ্ধতি ও করণীয়
    // ==========================================
    private static JanazaTopic getTopicBurialAndRules(boolean isBn) {
        List<JanazaContentItem> items = new ArrayList<>();
        if (isBn) {
            items.add(new JanazaContentItem(
                    "১. কারা খাঁটিয়া বহন করবে ও এর নিয়ম",
                    null,
                    null,
                    null,
                    "• কারা খাঁটিয়া ধরবে: মাইয়্যিতের পুরুষ আত্মীয়-স্বজন এবং উপস্থিত নেককার মুমিন ভাইয়েরা খাঁটিয়া বহন করবেন। এটি মুসলিমের ওপর অপর মুসলিমের অন্যতম শ্রেষ্ঠ মানবিক ও দ্বীনি হক।\n" +
                    "• কারা খাঁটিয়া বহন করবে না: মহিলাদের জানাজার খাটিয়া বহন করা এবং জানাজার পেছনে চলা শরিয়তে নিষেধ (উম্মে আতিয়্যাহ রা. বলেন: 'আমাদের জানাজার পেছনে চলতে নিষেধ করা হয়েছিল' — সহীহ বুখারী: ১২৭৮)।\n" +
                    "• বহনের সুন্নাত নিয়ম: খাটিয়ার চার পায়া কাঁধে নিয়ে অন্তত ১০ কদম করে মোট ৪০ কদম বহন করা মুস্তাহাব।\n" +
                    "• চলার আদব: খাটিয়ার সাথে নিঃশব্দে দ্রুত পায়ে চলা। খাটিয়ার সাথে উচ্চৈঃস্বরে জিকির, কান্নাকাটি বা শোরগোল করা অনুচিত। খাটিয়া মাটিতে না রাখা পর্যন্ত বসা মাকরূহ।",
                    "সহীহ বুখারী: ১২৭৮, সহীহ বুখারী: ১৩০৮, সুনানে আবু দাউদ: ৩১৭৮"
            ));

            items.add(new JanazaContentItem(
                    "২. কবর খনন ও গভীরতার পরিমাপ (পুরুষ বনাম মহিলা)",
                    null,
                    null,
                    null,
                    "• পুরুষের কবরের গভীরতা: সাধারণত বুক সমান বা প্রায় ৪ থেকে ৪.৫ ফুট গভীর এবং লম্বায় মাইয়্যিতের দেহের চেয়ে সামান্য বড়।\n" +
                    "• মহিলার কবরের গভীরতা: মহিলার কবর পুরুষের চেয়ে অধিক গভীর অর্থাৎ গলা সমান বা বুক ছাড়িয়ে (প্রায় ৫ থেকে ৫.৫ ফুট) খনন করা মুস্তাহাব, যাতে সতরের পূর্ণ পর্দা বজায় থাকে।\n" +
                    "• লাহদ (বগলী কবর): মাটি শক্ত হলে পশ্চিম পাশের দেয়ালে তাক বা কুলুঙ্গির মতো বগলী কবর খনন করা সর্বোত্তম সুন্নাত (সহীহ মুসলিম: ৯৬৬)।\n" +
                    "• শাক্ক (সিন্দুকী কবর): মাটি নরম বা বালুকাময় হলে মাঝ বরাবর সিন্দুকী গর্ত করে পাটাতন বা বাঁশ দেওয়া জায়েয।",
                    "সহীহ মুসলিম: ৯৬৬, ফাতাওয়ায়ে আলমগীরী ১/১৬৬, রদ্দুল মুহতার ২/২৩৪"
            ));

            items.add(new JanazaContentItem(
                    "৩. কবরে কারা নামবে ও নামানোর পদ্ধতি",
                    "بِسْمِ اللَّهِ وَعَلَى مِلَّةِ رَسُولِ اللَّهِ",
                    "বিসমিল্লাহি ওয়া আলা মিল্লাতি রাসুলিল্লাহ।",
                    "আল্লাহর নামে এবং রাসূলুল্লাহ ﷺ এর আদর্শের ওপর রাখছি।",
                    "• কবরে কারা নামবে: মাইয়্যিতকে কবরে নামানোর জন্য তার নিকটাত্মীয় মাহরাম পুরুষগণ নামবেন।\n" +
                    "• মহিলার ক্ষেত্রে: মহিলার নিকটাত্মীয় মাহরাম পুরুষগণ কবরে নামবেন। যদি মাহরাম না থাকে, তবে এমন পরহেযগার নেককার পুরুষ নামবেন যিনি বিগত রাতে স্ত্রী সহবাস করেননি (সহীহ বুখারী: ১২৮৫, আনাস রা. বর্ণিত হাদিস)।\n" +
                    "• মহিলার কবরে পর্দার চাদর: মহিলার লাশ কবরে নামানোর সময় পুরো কবরের ওপর চাদর ধরে পর্দা করে রাখা মুস্তাহাব।\n" +
                    "• কবরে শোয়ানো: মাইয়্যিতকে ডান কাতে কিবলামুখী করে শোয়াবে এবং কাফনের মাথার ও পায়ের বাঁধন খুলে দেবে।",
                    "সহীহ বুখারী: ১২৮৫, সুনানে তিরমিযী: ১০৪৬, সুনানে আবু দাউদ: ৩২১৩"
            ));

            items.add(new JanazaContentItem(
                    "৪. কবরে তিন অঞ্জলি মাটি দেওয়ার কুরআনভিত্তিক দোয়া",
                    "১ম অঞ্জলিতে: مِنْهَا خَلَقْنَاكُمْ\n২য় অঞ্জলিতে: وَفِيهَا نُعِيدُكُمْ\n৩য় অঞ্জলিতে: وَمِنْهَا نُخْرِجُكُمْ تَارَةً أُخْرَى",
                    "১ম অঞ্জলিতে: মিনহা খালাকনাকুম।\n২য় অঞ্জলিতে: ওয়া ফিহা নুঈদুকুম।\n৩য় অঞ্জলিতে: ওয়া মিনহা নুখরিজুকুম তারাতান উখরা।",
                    "১ম অঞ্জলিতে: 'এ মাটি থেকেই তোমাদের সৃষ্টি করেছি।'\n২য় অঞ্জলিতে: 'এ মাটিতেই তোমাদের ফিরিয়ে নেব।'\n৩য় অঞ্জলিতে: 'এবং এ মাটি থেকেই তোমাদের পুনরায় বের করব।'",
                    "• কবরে মাথার দিক থেকে দুই হাতে তিন অঞ্জলি মাটি দেওয়া মুস্তাহাব। কবর ভরাট শেষে উটের পিঠের কুঁজের মতো এক বিঘত পরিমাণ উঁচু করে পানি ছিটানো সুন্নত।",
                    "সূরা ত্বা-হা: ৫৫, মুসনাদে আহমাদ: ৫/২৫৪, সুনানে বায়হাকী: ৩/৪১২"
            ));

            items.add(new JanazaContentItem(
                    "৫. মৃত ব্যক্তির জন্য সন্তান ও ওয়ারিশদের সর্বপ্রধান করণীয়",
                    null,
                    null,
                    null,
                    "১. ঋণ (কর্জ) পরিশোধ: মৃত্যুর পর সর্বাগ্রে মাইয়্যিতের রেখে যাওয়া সম্পদ থেকে তার সমস্ত ঋণ পরিশোধ করা ফরজ। ঋণ পরিশোধ না হওয়া পর্যন্ত মুমিনের আত্মা ঝুলন্ত থাকে (সুনানে তিরমিযী: ১০৭৮)।\n" +
                    "২. জায়েয ওসিয়ত পূরণ: অনধিক এক-তৃতীয়াংশ (১/৩) সম্পদের ভেতর মাইয়্যিতের বৈধ ওসিয়ত বাস্তবায়ন করা।\n" +
                    "৩. সদকায়ে জারিয়া: পিতা-মাতার নামে কুরআন তিলাওয়াত, মসজিদ-মাদ্রাসায় দান, পানির নলকূপ বা সদকায়ে জারিয়ার ব্যবস্থা করা।\n" +
                    "৪. অবিরাম দোয়া করা: সন্তান কর্তৃক দোয়া—'রাব্বির হামহুমা কামা রাব্বায়ানী সাগীরা' (হে আমাদের রব! তাদের ওপর দয়া করুন যেভাবে শৈশবে তারা আমাকে লালন-পালন করেছিলেন — সূরা বনি ইসরাঈল: ২৪)।\n" +
                    "৫. পিতা-মাতার বন্ধুদের সম্মান করা: পিতা-মাতার মৃত্যুর পর তাদের বন্ধুদের সাথে সুসম্পর্ক রক্ষা করা শ্রেষ্ঠ সদ্ব্যবহার (সহীহ মুসলিম: ২৫৫২)।",
                    "সুনানে তিরমিযী: ১০৭৮, সহীহ মুসলিম: ২৫৫২, সূরা বনি ইসরাঈল: ২৪"
            ));

            items.add(new JanazaContentItem(
                    "৬. শোক পালন, তাযিয়াত (সমবেদনা) ও বর্জনীয় বিদআত",
                    null,
                    null,
                    null,
                    "• তাযিয়াত (সমবেদনা): শোকগ্রস্ত পরিবারকে ৩ দিনের মধ্যে সান্ত্বনা দেওয়া সুন্নত। সান্ত্বনার বাক্য: 'ইন্না লিল্লাহি মা আখাযা ওয়া লাহু মা আ’তা...' (নিশ্চয় যা নিয়েছেন তা আল্লাহরই এবং যা দিয়েছেন তাও তাঁরই — সহীহ বুখারী: ১২৮৪)।\n" +
                    "• প্রতিবেশীদের খাবার পাঠানো: শোকগ্রস্ত পরিবারের জন্য প্রতিবেশীদের খাবার রান্না করে পাঠানো সুন্নত ('জাফর পরিবারের জন্য খাবার প্রস্তুত করো' — তিরমিযী: ৯৯৮)।\n" +
                    "• বর্জনীয় বিদআত: উচ্চৈঃস্বরে মাতম, বুক চাপড়ানো, ৩য়, ৭ম বা ৪০তম দিনে আনুষ্ঠানিক কুলখানি/চেহলামের ভোজ আয়োজন করা এবং কবরে পাকা সৌধ বা মাজার নির্মাণ কঠোরভাবে হারাম ও নিষিদ্ধ।",
                    "সহীহ বুখারী: ১২৮৪, সুনানে তিরমিযী: ৯৯৮, সহীহ মুসলিম: ৯৬৯"
            ));
        } else {
            items.add(new JanazaContentItem(
                    "1. Who Should Carry the Bier and Etiquettes",
                    null,
                    null,
                    null,
                    "• Who Should Carry: Male relatives and present righteous Muslim brothers should carry the bier. It is one of the greatest rights of a Muslim upon his brother.\n" +
                    "• Who Should Not: Women are prohibited from carrying the funeral bier and following funeral processions (Umm Atiyyah RA narrated: 'We were forbidden from following funeral processions' — Sahih Bukhari: 1278).\n" +
                    "• Sunnah Method: Carrying all four corner posts for at least 10 steps each (40 steps in total) is highly recommended.\n" +
                    "• Walking Etiquettes: Walk silently and swiftly. Wailing, loud chanting, or commotion is prohibited. Sitting is disliked until the bier is placed onto the ground.",
                    "Sahih Bukhari: 1278, Sahih Bukhari: 1308, Sunan Abi Dawud: 3178"
            ));

            items.add(new JanazaContentItem(
                    "2. Grave Digging Dimensions (Men vs. Women)",
                    null,
                    null,
                    null,
                    "• Grave Depth for Men: Typically chest-deep (about 4 to 4.5 feet) and slightly longer than the deceased's height.\n" +
                    "• Grave Depth for Women: Recommended to be deeper — up to the neck or above chest-level (about 5 to 5.5 feet) to preserve complete modesty and privacy.\n" +
                    "• Lahd (Side Niche): If soil is firm, digging a side niche along the Qiblah wall is the best Sunnah (Sahih Muslim: 966).\n" +
                    "• Shakk (Central Trench): If soil is loose or sandy, digging a trench in the middle covered with planks or bamboo is permissible.",
                    "Sahih Muslim: 966, Fatawa Alamgiri 1/166, Radd al-Muhtar 2/234"
            ));

            items.add(new JanazaContentItem(
                    "3. Lowering into Grave and Who Enters",
                    "بِسْمِ اللَّهِ وَعَلَى مِلَّةِ رَسُولِ اللَّهِ",
                    "Bismillahi wa 'ala millati Rasulillah.",
                    "In the name of Allah and upon the religion/creed of the Messenger of Allah.",
                    "• Who Enters Grave: Close male Mahram relatives lower the body.\n" +
                    "• For a Deceased Female: Mahram male relatives lower her. If none are available, pious righteous men who did not have marital relations the previous night enter (Sahih Bukhari: 1285, Hadith of Anas RA).\n" +
                    "• Modesty Sheet for Female Grave: Holding a large cloth sheet over the entire grave opening while lowering a woman is Sunnah to guard privacy.\n" +
                    "• Placement: Place on right side facing the Qiblah and untie the shroud knots at head and feet.",
                    "Sahih Bukhari: 1285, Sunan at-Tirmidhi: 1046, Sunan Abi Dawud: 3213"
            ));

            items.add(new JanazaContentItem(
                    "4. Three Handfuls of Soil (Quranic Supplications)",
                    "1st Handful: مِنْهَا خَلَقْنَاكُمْ\n2nd Handful: وَفِيهَا نُعِيدُكُمْ\n3rd Handful: وَمِنْهَا نُخْرِجُكُمْ تَارَةً أُخْرَى",
                    "1st: Minha khalaqnakum.\n2nd: Wa feeha nu'eedukum.\n3rd: Wa minha nukhrijukum taratan ukhra.",
                    "1st: 'From it (the earth) We created you.'\n2nd: 'And into it We shall return you.'\n3rd: 'And from it We shall bring you out once again.'",
                    "• It is Sunnah to throw three handfuls of soil starting from the head side. Raise the grave about one hand-span in a hump shape and sprinkle water over it.",
                    "Surah Taha: 55, Musnad Ahmad 5/254, Sunan Bayhaqi 3/412"
            ));

            items.add(new JanazaContentItem(
                    "5. Foremost Duties of Children and Heirs for the Deceased",
                    null,
                    null,
                    null,
                    "1. Settling Debts (Fardh): Immediately pay off all debts from the deceased's estate. The soul remains suspended until debts are cleared (Sunan at-Tirmidhi: 1078).\n" +
                    "2. Fulfilling Valid Bequests (Wasiyyah): Executing valid bequests up to a maximum of one-third (1/3) of the estate.\n" +
                    "3. Ongoing Charity (Sadaqah Jariyah): Donating on their behalf for wells, mosques, Quran distribution, or Islamic education.\n" +
                    "4. Continuous Supplication by Children: Praying 'My Lord, have mercy upon them as they brought me up when I was small' (Surah Al-Isra: 24).\n" +
                    "5. Honoring Parents' Friends: Maintaining warm ties with the friends of deceased parents is among the highest forms of filial devotion (Sahih Muslim: 2552).",
                    "Sunan at-Tirmidhi: 1078, Sahih Muslim: 2552, Surah Al-Isra: 24"
            ));

            items.add(new JanazaContentItem(
                    "6. Condolences (Ta'ziyah) and Prohibited Innovations",
                    null,
                    null,
                    null,
                    "• Ta'ziyah (Comforting): Console the grieving family within 3 days saying: 'Inna lillahi ma akhadha wa lahu ma a'ta...' (To Allah belongs what He took and gave — Sahih Bukhari: 1284).\n" +
                    "• Food from Neighbors: Neighbors preparing food for the grieving family is an authentic Sunnah ('Prepare food for the family of Ja'far' — Tirmidhi: 998).\n" +
                    "• Prohibited Bid'ah: Wailing loudly, tearing clothes, hosting ritual 3rd/7th/40th-day feasts (Kholkhani), or constructing raised shrines and monuments over graves is strictly forbidden.",
                    "Sahih Bukhari: 1284, Sunan at-Tirmidhi: 998, Sahih Muslim: 969"
            ));
        }

        return new JanazaTopic(
                "topic_burial_and_rules",
                isBn ? "জানাযার নামাযের পদ্ধতি ও করণীয়" : "Janaza Method, Burial & Etiquettes",
                isBn ? "খাটিয়া বহন, পুরুষ ও মহিলার কবরের মাপ, দাফন, ওয়ারিশদের দায়িত্ব ও তাযিয়াত" : "Carrying bier, grave depths for men & women, burial, heirs' duties & condolences",
                R.drawable.ic_janaza_burial,
                isBn ? "জানাযা, দাফন, ওয়ারিশদের করণীয় ও শোক পালন সংক্রান্ত পূর্ণাঙ্গ সুন্নাহ দিকনির্দেশনা।" : "Comprehensive Sunnah guidance on Janaza, burial, heirs' duties, and condolences.",
                items
        );
    }
}
