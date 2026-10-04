package com.devflux.deenone.data.repository;

import com.devflux.deenone.data.model.AllahNameItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllahNamesRepository {

    private static final List<AllahNameItem> ALLAH_NAMES = new ArrayList<>();

    private static String generateArabicTtsUrl(String arabicText) {
        try {
            return "https://translate.google.com/translate_tts?ie=UTF-8&tl=ar&client=tw-ob&q=" +
                    java.net.URLEncoder.encode(arabicText, java.nio.charset.StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return "";
        }
    }

    static {
        // High quality Islamic audio CDN pattern (e.g. Al-Afasy / Islamic Audio CDN 1..99)


        ALLAH_NAMES.add(new AllahNameItem(1, "الرَّحْمٰنُ", "আর-রহমান", "Ar-Rahman", "পরম দয়ালু, অসীম দাতা", "The Most Gracious", "সৃষ্টিজগতের সমস্ত সৃষ্টির প্রতি যিনি অনন্ত ও ব্যাপক অনুগ্রহশীল।", "প্রতিদিন জিকির করলে অন্তরে প্রশান্তি ও আল্লাহর রহমত লাভ হয়।", generateArabicTtsUrl("الرَّحْمٰنُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(2, "الرَّحِيمُ", "আর-রাহীম", "Ar-Raheem", "পরম করুণাময়", "The Most Merciful", "যিনি মুমিন বান্দাদের প্রতি আখেরাতে বিশেষ দয়া ও অনুগ্রহ প্রদর্শন করবেন।", "যেকোনো বিপদ থেকে হেফাজত ও ঈমানের দৃঢ়তা অর্জিত হয়।", generateArabicTtsUrl("الرَّحِيمُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(3, "الْمَلِكُ", "আল-মালিক", "Al-Malik", "সার্বভৌম ক্ষমতার অধিকারী ও বাদশাহ", "The King / Sovereign", "সমগ্র আসমান ও জমিনের একমাত্র একচ্ছত্র অধিপতি ও প্রকৃত শাসক।", "অভাব দূর হয় এবং আত্মমর্যাদাবোধ বৃদ্ধি পায়।", generateArabicTtsUrl("الْمَلِكُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(4, "الْقُدُّوسُ", "আল-কুদ্দুস", "Al-Quddus", "সর্বপ্রকার দোষ-ত্রুটিমুক্ত ও পরম পবিত্র", "The Most Holy / Pure", "যিনি সব ধরনের অপূর্ণতা ও সৃষ্টির সদৃশতা থেকে চির পবিত্র।", "অন্তরের রোগ ও কুমন্ত্রণা থেকে মুক্তি পাওয়া যায়।", generateArabicTtsUrl("الْقُدُّوسُ"), "PURITY"));
        ALLAH_NAMES.add(new AllahNameItem(5, "السَّلَامُ", "আস-সালাম", "As-Salam", "শান্তি ও নিরাপত্তাদাতা", "The Source of Peace", "যিনি বান্দাকে যাবতীয় বালা-মুসিবত থেকে শান্তি ও নিরাপত্তা দান করেন।", "শারীরিক ও মানসিক শান্তি ও রোগমুক্তি অর্জিত হয়।", generateArabicTtsUrl("السَّلَامُ"), "PEACE"));
        ALLAH_NAMES.add(new AllahNameItem(6, "الْمُؤْمِنُ", "আল-মু'মিন", "Al-Mu'min", "নিরাপত্তা ও ঈমানদানকারী", "The Granter of Security", "যিনি বান্দাকে সত্যের নিরাপত্তা দেন এবং তার প্রতিশ্রুত পুরস্কার নিশ্চিত করেন।", "ভয়ভীতি ও আতঙ্ক দূর হয়ে মনে সাহস সঞ্চার হয়।", generateArabicTtsUrl("الْمُؤْمِنُ"), "PEACE"));
        ALLAH_NAMES.add(new AllahNameItem(7, "الْمُهَيْمِنُ", "আল-মুহাইমিন", "Al-Muhaymin", "রক্ষক ও চির পর্যবেক্ষণকারী", "The Guardian / Preserver", "যিনি প্রতিটি বস্তুর স্থায়িত্ব রক্ষা করেন এবং সবার আমল প্রত্যক্ষ করেন।", "গুপ্ত পাপ থেকে দূরে থাকতে অন্তরে আল্লাহর ভয় সৃষ্টি হয়।", generateArabicTtsUrl("الْمُهَيْمِنُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(8, "الْعَزِيزُ", "আল-আযীয", "Al-Azeez", "পরাক্রমশালী ও অপরাজেয়", "The All Mighty", "যার ক্ষমতার ওপর কারো আধিপত্য নেই, যিনি সর্বজয়ী।", "সম্মান ও মর্যাদা বৃদ্ধি পায় এবং শত্রুর ভয় দূরীভূত হয়।", generateArabicTtsUrl("الْعَزِيزُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(9, "الْجَبَّارُ", "আল-জাব্বার", "Al-Jabbar", "দুর্দণ্ড প্রতাপশালী ও সংশোধক", "The Compeller / Restorer", "যিনি ভাঙা অবস্থাকে জোড়া লাগান এবং নিজের ইচ্ছায় সবকিছু পরিচালনা করেন।", "অত্যাচারীর জুলুম থেকে হেফাজত থাকা যায়।", generateArabicTtsUrl("الْجَبَّارُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(10, "الْمُتَكَبِّرُ", "আল-মুতাকাব্বির", "Al-Mutakabbir", "অহংকারের একমাত্র প্রকৃত অধিকারী ও মহান", "The Supreme / Majestic", "যিনি সকল সৃষ্টি হতে শ্রেষ্ঠ এবং সব ধরনের অহমিকা একমাত্র তাঁরই শোভা পায়।", "উচ্চ মর্যাদা ও সমাজে নেক প্রভাব বিস্তার লাভ হয়।", generateArabicTtsUrl("الْمُتَكَبِّرُ"), "MAJESTY"));

        ALLAH_NAMES.add(new AllahNameItem(11, "الْخَالِقُ", "আল-খালিক্ব", "Al-Khaliq", "একমাত্র সৃষ্টিকর্তা", "The Creator", "যিনি শূন্য হতে কোনো পূর্ব নমুনা ছাড়াই সব সৃষ্টি করেছেন।", "কাজে সফলতা ও সৃষ্টির মাঝে বরকত লাভ হয়।", generateArabicTtsUrl("الْخَالِقُ"), "CREATION"));
        ALLAH_NAMES.add(new AllahNameItem(12, "الْبَارِئُ", "আল-বারি", "Al-Bari", "সঠিক রূপ ও প্রাণদানকারী", "The Evolver / Maker", "যিনি সুনির্দিষ্ট পরিমাপে নিখুঁতভাবে সৃষ্টিকে অস্তিত্ব দেন।", "রোগব্যাধি ও জটিল কষ্ট থেকে আরোগ্য অর্জিত হয়।", generateArabicTtsUrl("الْبَارِئُ"), "CREATION"));
        ALLAH_NAMES.add(new AllahNameItem(13, "الْمُصَوِّرُ", "আল-মুসাওয়ির", "Al-Musawwir", "রূপদানকারী ও সৌন্দর্যবিধায়ক", "The Fashioner / Shaper", "যিনি প্রতিটি বস্তুকে অনন্য রূপ ও বৈচিত্র্য দান করেছেন।", "নেককার সন্তান ও সুন্দর চরিত্র লাভের উসিলা হয়।", generateArabicTtsUrl("الْمُصَوِّرُ"), "CREATION"));
        ALLAH_NAMES.add(new AllahNameItem(14, "الْغَفَّارُ", "আল-গাফফার", "Al-Ghaffar", "অসীম ক্ষমাশীল ও পাপ গোপনকারী", "The Great Forgiver", "যিনি বারবার গুনাহ গোপন করেন ও অনুতপ্ত বান্দাকে ক্ষমা করেন।", "গুনাহ মাফ হয় এবং অন্তরের অন্ধকার দূর হয়।", generateArabicTtsUrl("الْغَفَّارُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(15, "الْقَهَّارُ", "আল-ক্বাহহার", "Al-Qahhar", "কঠোর দমনকারী ও বিজয়ী", "The Subduer / All-Dominant", "যার সামনে সমগ্র সৃষ্টি মস্তক অবনত করতে বাধ্য।", "নফসের প্রবৃত্তি ও শয়তানের প্ররোচনা নিস্তেজ হয়।", generateArabicTtsUrl("الْقَهَّارُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(16, "الْوَهَّابُ", "আল-ওয়াহহাব", "Al-Wahhab", "প্রতিদানহীন মহাদানকারী", "The Supreme Bestower", "যিনি কোনো প্রতিদান বা শর্ত ছাড়াই অকাতরে দান করেন।", "দোয়া কবুল হয় এবং রিজিকের অভাব মোচন হয়।", generateArabicTtsUrl("الْوَهَّابُ"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(17, "الرَّزَّاقُ", "আর-রাযযাক্ব", "Ar-Razzaq", "পরম রিজিকদাতা", "The Total Provider", "যিনি প্রতিটি জীব ও প্রাণীর রিজিকের ব্যবস্থা নিশ্চিত করেছেন।", "হালাল রিজিক বৃদ্ধি পায় ও বরকত আসে।", generateArabicTtsUrl("الرَّزَّاقُ"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(18, "الْفَتَّاحُ", "আল-ফাত্তাহ", "Al-Fattah", "বিজয়দানকারী ও দ্বার উন্মোচনকারী", "The Supreme Opener", "যিনি বন্ধ দরজা খুলে দেন এবং হক ও বাতিলের মাঝে ফয়সালা করেন।", "কঠিন কাজের জট খুলে যায় ও অন্তর্দৃষ্টি লাভ হয়।", generateArabicTtsUrl("الْفَتَّاحُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(19, "الْعَلِيمُ", "আল-আলীম", "Al-Aleem", "সর্বজ্ঞাতা ও অন্তরযামী", "The All-Knowing", "যার জ্ঞান অতীত, বর্তমান, ভবিষ্যৎ ও মনের গোপন ইচ্ছায় পরিব্যাপ্ত।", "ইলম ও দ্বীনি প্রজ্ঞা বৃদ্ধি পায়।", generateArabicTtsUrl("الْعَلِيمُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(20, "الْقَابِضُ", "আল-ক্বাবিদ", "Al-Qabid", "সংকোচনকারী ও নিয়ন্ত্রণকারী", "The Withholder / Constrictor", "যিনি পরীক্ষা স্বরূপ রিজিক ও জীবনকে সংকুচিত করতে পারেন।", "অহংকার চূর্ণ হয় ও আল্লাহর ওপর তাওয়াক্কুল বাড়ে।", generateArabicTtsUrl("الْقَابِضُ"), "MAJESTY"));

        ALLAH_NAMES.add(new AllahNameItem(21, "الْبَاسِطُ", "আল-বাসিত", "Al-Basit", "প্রসারণকারী ও প্রাচুর্যদাতা", "The Extender / Expander", "যিনি বান্দার রিজিক, জ্ঞান ও আয়ু উদারভাবে প্রশস্ত করেন।", "অভাব দূর হয়ে আর্থিক ও মানসিক স্বস্তি আসে।", generateArabicTtsUrl("الْبَاسِطُ"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(22, "الْخَافِضُ", "আল-খাফিদ", "Al-Khafid", "অবনমিতকারী ও হীনতাকারী", "The Reducer / Abaser", "যিনি অহংকারী ও কাফিরদের লাঞ্ছিত ও অপদস্থ করেন।", "শত্রুর ষড়যন্ত্র থেকে সুরক্ষা লাভ হয়।", generateArabicTtsUrl("الْخَافِضُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(23, "الرَّافِعُ", "আর-রাফি'", "Ar-Rafi'", "উন্নতকারী ও মর্যাদা দানকারী", "The Exalter / Elevator", "যিনি মুমিন ও বিনম্র বান্দাদের সম্মান ও মর্যাদা সমুন্নত করেন।", "সম্মান ও ধর্মীয় উচ্চ মর্যাদা লাভ হয়।", generateArabicTtsUrl("الرَّافِعُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(24, "الْمُعِزُّ", "আল-মু'ইয্য", "Al-Mu'izz", "সম্মানদানকারী", "The Bestower of Honor", "যিনি যাকে চান দুনিয়া ও আখেরাতে মর্যাদা ও ইজ্জত দান করেন।", "পরনির্ভরশীলতা দূর হয় ও ব্যক্তিত্ব উজ্জ্বল হয়।", generateArabicTtsUrl("الْمُعِزُّ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(25, "الْمُذِلُّ", "আল-মুযিল্ল", "Al-Muzill", "অপমানকারী ও হেয়কারী", "The Humiliator / Dishonorer", "যিনি অবাধ্য ও অহংকারীদের মর্যাদাহীন ও লাঞ্ছিত করেন।", "কুচক্রীদের অনিষ্ট থেকে মুক্তি পাওয়া যায়।", generateArabicTtsUrl("الْمُذِلُّ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(26, "السَّمِيعُ", "আস-সামী'", "As-Samee'", "সর্বশ্রোতা", "The All-Hearing", "যিনি নিখিল বিশ্বের প্রকাশ্য ও ফিসফিস করা প্রতিটি ধ্বনি শোনেন।", "দোয়া অতি দ্রুত আল্লাহর দরবারে পৌঁছায়।", generateArabicTtsUrl("السَّمِيعُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(27, "الْبَصِيرُ", "আল-বাসীর", "Al-Baseer", "সর্বদ্রষ্টা", "The All-Seeing", "ঘোর অন্ধকারে কালো পাথরে কালো পিপীলিকার পদচারণাও যিনি দেখেন।", "দৃষ্টিশক্তি ও অন্তর্দৃষ্টির জ্যোতি বৃদ্ধি পায়।", generateArabicTtsUrl("الْبَصِيرُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(28, "الْحَكَمُ", "আল-হাকাম", "Al-Hakam", "পরম বিচারক ও ফয়সালাকারী", "The Impartial Judge", "যিনি ন্যায়পরায়ণতার সাথে নিখুঁতভাবে শেষ বিচার করবেন।", "ন্যায়বিচার লাভ ও জটিল বিরোধের সমাধান হয়।", generateArabicTtsUrl("الْحَكَمُ"), "JUSTICE"));
        ALLAH_NAMES.add(new AllahNameItem(29, "الْعَدْلُ", "আল-আদল", "Al-Adl", "পরম ন্যায়পরায়ণ ও সুবিচারক", "The Utterly Just", "যার কোনো ফয়সালাতেই সামান্যতম জুলুমের অবকাশ নেই।", "হৃদয় পরিশুদ্ধ হয় ও সঠিক সিদ্ধান্তের তৌফিক মেলে।", generateArabicTtsUrl("الْعَدْلُ"), "JUSTICE"));
        ALLAH_NAMES.add(new AllahNameItem(30, "اللَّطِيفُ", "আল-লাতীফ", "Al-Lateef", "পরম স্নেহশীল ও সূক্ষ্মদর্শী", "The Subtle / Most Kind", "যিনি বান্দার প্রয়োজন গোপনে সূক্ষ্মতম উপায়ে পূরণ করেন।", "অপ্রত্যাশিত উৎস থেকে রিজিক ও বিপদমুক্তি আসে।", generateArabicTtsUrl("اللَّطِيفُ"), "MERCY"));

        ALLAH_NAMES.add(new AllahNameItem(31, "الْخَبِيرُ", "আল-খাবীর", "Al-Khabeer", "সর্ববিষয়ে সম্যক অবগত", "The All-Aware", "যিনি প্রতিটি ঘটনার মূল রহস্য ও অন্তঃসার জানেন।", "গোপন ফেতনা ও অসৎ সঙ্গ থেকে বেঁচে থাকা যায়।", generateArabicTtsUrl("الْخَبِيرُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(32, "الْحَلِيمُ", "আল-হালীম", "Al-Haleem", "পরম ধৈর্যশীল ও সহনশীল", "The Most Forbearing", "যিনি বান্দার পাপ দেখেও তাৎক্ষণিক শাস্তি না দিয়ে তওবার সুযোগ দেন।", "ক্রোধ নিয়ন্ত্রণ হয় ও হৃদয়ে প্রশান্তি আসে।", generateArabicTtsUrl("الْحَلِيمُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(33, "الْعَظِيمُ", "আল-আযীম", "Al-Azeem", "সর্বোচ্চ মহান ও মহিমান্বিত", "The Magnificent / Supreme", "যার সমকক্ষ বা তুলনীয় মহত্ত্ব আর কারো নেই।", "আল্লাহর ভয় অন্তরে জাগে এবং মর্যাদা বৃদ্ধি পায়।", generateArabicTtsUrl("الْعَظِيمُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(34, "الْغَفُورُ", "আল-গাফূর", "Al-Ghafoor", "মহাক্ষমাশীল", "The Great Forgiver", "যিনি বান্দার পাহাড়সম গুনাহও এক নিমিষে ক্ষমা করে দেন।", "আত্মার ক্লান্তি ও গুনাহের গ্লানি মুছে যায়।", generateArabicTtsUrl("الْغَفُورُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(35, "الشَّكُورُ", "আশ-শাকূর", "Ash-Shakoor", "কৃতজ্ঞতা গ্রহণকারী ও পুরস্কারদাতা", "The Most Appreciative", "যিনি বান্দার সামান্য নেক আমলের বিনিময়ে অসীম সওয়াব দান করেন।", "আমলে তৃপ্তি ও অফুরন্ত বরকত পাওয়া যায়।", generateArabicTtsUrl("الشَّكُورُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(36, "الْعَلِيُّ", "আল-আলী", "Al-Aliyy", "সর্বোচ্চ ও সুমহান", "The Most High", "যিনি স্থান, ক্ষমতা ও মর্যাদায় সর্বোচ্চ শিখরে অবস্থানরত।", "হীনমন্যতা দূর হয় ও ঈমানি বল বাড়ে।", generateArabicTtsUrl("الْعَلِيُّ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(37, "الْكَبِيرُ", "আল-কাবীর", "Al-Kabeer", "সর্বশ্রেষ্ঠ ও সুবিশাল", "The Greatest", "যার তুলনায় নিখিল বিশ্বব্রহ্মাণ্ড সামান্য বালুকণার মতো।", "জ্ঞান ও আত্মসম্মান উন্নত হয়।", generateArabicTtsUrl("الْكَبِيرُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(38, "الْحَفِيظُ", "আল-হাফীয", "Al-Hafeez", "পরম সংরক্ষক", "The Preserver", "যিনি নিখিল বিশ্বকে ধ্বংস ও বিপর্যয় হতে রক্ষা করে চলেছেন।", "দুর্ঘটনা ও জাদু-টোনা হতে অটুট নিরাপত্তা লাভ হয়।", generateArabicTtsUrl("الْحَفِيظُ"), "PROTECTION"));
        ALLAH_NAMES.add(new AllahNameItem(39, "الْمُقِيتُ", "আল-মুক্বীত", "Al-Muqeet", "সকলের শক্তির যোগানদাতা ও রক্ষক", "The Sustainer", "যিনি সকল সৃষ্টির আত্মিক ও শারীরিক খাদ্যের যোগান দেন।", "শারীরিক দুর্বলতা দূর হয় ও শক্তি বাড়ে।", generateArabicTtsUrl("الْمُقِيتُ"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(40, "الْحَسِيبُ", "আল-হাসীব", "Al-Haseeb", "পর্যাপ্ত সহায় ও হিসাব গ্রহণকারী", "The Reckoner / Sufficient", "যিনি বান্দার যাবতীয় প্রয়োজনে একাই যথেষ্ট।", "চিন্তামুক্ত জীবন ও আল্লাহর ওপর অবিচল আস্থা অর্জিত হয়।", generateArabicTtsUrl("الْحَسِيبُ"), "JUSTICE"));

        ALLAH_NAMES.add(new AllahNameItem(41, "الْجَلِيلُ", "আল-জালীল", "Al-Jaleel", "পরম মর্যাদাবান ও প্রতাপশালী", "The Majestic", "যার ব্যক্তিত্বে মহিমা ও সম্ভ্রমের পূর্ণ প্রকাশ বিদ্যমান।", "সমাজে প্রভাব ও গ্রহণযোগ্যতা বৃদ্ধি পায়।", generateArabicTtsUrl("الْجَلِيلُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(42, "الْكَرِيمُ", "আল-কারীম", "Al-Kareem", "পরম দাতা ও মহিমান্বিত", "The Most Generous", "যিনি অসীম বদান্যতায় না চাইতেই অগণিত নিয়ামত বর্ষণ করেন।", "দুনিয়াবী ও পরকালীন অভাব দূরীভূত হয়।", generateArabicTtsUrl("الْكَرِيمُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(43, "الرَّقِيبُ", "আর-রাক্বীব", "Ar-Raqeeb", "চির জাগ্রত ও সদা সতর্ক প্রহরী", "The Watchful", "যার নজর থেকে কোনো কিছুই এক মুহূর্তের জন্যও আড়াল হয় না।", "পরিবার ও ধন-সম্পদ নিরাপদে থাকে।", generateArabicTtsUrl("الرَّقِيبُ"), "PROTECTION"));
        ALLAH_NAMES.add(new AllahNameItem(44, "الْمُجِيبُ", "আল-মুজীব", "Al-Mujeeb", "দোয়া কবুলকারী ও সাড়া দানকারী", "The Responsive", "যিনি ব্যাকুল অন্তরের আর্তনাদ শোনেন ও প্রার্থনায় সাড়া দেন।", "প্রার্থনা দ্রুত মঞ্জুর হয় ও হতাশা কাটে।", generateArabicTtsUrl("الْمُجِيبُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(45, "الْوَاسِعُ", "আল-ওয়াসি'", "Al-Wasi'", "সীমাহীন প্রশস্ত ও প্রাচুর্যময়", "The All-Encompassing", "যার জ্ঞান, করুণা ও রাজত্ব সীমাহীন বিস্তৃত।", "মনের সংকীর্ণতা ও আর্থিক টানাপোড়েন দূর হয়।", generateArabicTtsUrl("الْوَاسِعُ"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(46, "الْحَكِيمُ", "আল-হাকীম", "Al-Hakeem", "পরম প্রজ্ঞাময়", "The All-Wise", "যার প্রতিটি আদেশ ও সৃষ্টিতে পরম হিকমত নিহিত রয়েছে।", "কাজে সঠিক বুদ্ধিমত্তা ও প্রজ্ঞা বৃদ্ধি পায়।", generateArabicTtsUrl("الْحَكِيمُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(47, "الْوَدُودُ", "আল-ওয়াদূদ", "Al-Wadood", "বান্দার প্রতি সর্বাধিক প্রেমময়", "The Most Loving", "যিনি নিজের নেককার বান্দাদের গভীর ভালোবাসায় সিক্ত করেন।", "পারিবারিক ভালোবাসা বৃদ্ধি পায় ও তিক্ততা দূর হয়।", generateArabicTtsUrl("الْوَدُودُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(48, "الْمَجِيدُ", "আল-মাজীদ", "Al-Majeed", "সুমহান ও মহিমান্বিত", "The Most Glorious", "যার সম্মান ও অনুগ্রহের কোনো শেষ নেই।", "অসুস্থতা দূর হয় ও অন্তরের অন্ধকার কেটে যায়।", generateArabicTtsUrl("الْمَجِيدُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(49, "الْبَاعِثُ", "আল-বা'ইস", "Al-Ba'ith", "পুনরুত্থানকারী ও জাগ্রতকারী", "The Resurrector", "যিনি মৃত্যুর পর কবর হতে সমস্ত সৃষ্টিকে পুনরায় জীবিত করবেন।", "ঈমান তাজা হয় ও অলসতা দূর হয়।", generateArabicTtsUrl("الْبَاعِثُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(50, "الشَّهِيدُ", "আশ-শাহীদ", "Ash-Shaheed", "প্রত্যক্ষদর্শী ও চির উপস্থিত", "The Universal Witness", "যিনি প্রকাশ্যে ও গোপনে যা ঘটে তার প্রত্যক্ষ সাক্ষী।", "সন্তানের অবাধ্যতা দূর হয় ও সঠিক পথে আসে।", generateArabicTtsUrl("الشَّهِيدُ"), "WISDOM"));

        ALLAH_NAMES.add(new AllahNameItem(51, "الْحَقُّ", "আল-হাক্বক্ব", "Al-Haqq", "একমাত্র চিরন্তন সত্য", "The Absolute Truth", "যার অস্তিত্ব অবিসংবাদিত ও যার বাণী চিরন্তন সত্য।", "হারানো বস্তু ফিরে পাওয়া যায় ও সত্যের জয় হয়।", generateArabicTtsUrl("الْحَقُّ"), "TRUTH"));
        ALLAH_NAMES.add(new AllahNameItem(52, "الْوَكِيلُ", "আল-ওয়াকীল", "Al-Wakeel", "উত্তম অভিভাবক ও সহায়তাকারী", "The Trustee", "যিনি তাঁর ওপর ভরসাকারীদের সকল দায়িত্ব সুন্দরভাবে সম্পন্ন করেন।", "আগুনের ভয়, ঝড় ও সকল বিপর্যয় হতে রক্ষা পাওয়া যায়।", generateArabicTtsUrl("الْوَكِيلُ"), "PROTECTION"));
        ALLAH_NAMES.add(new AllahNameItem(53, "الْقَوِيُّ", "আল-ক্বাবীয়", "Al-Qawiyy", "মহাশক্তিশালী", "The All-Strong", "যার শক্তিতে কখনো কোনো ক্লান্তি বা দুর্বলতা আসে না।", "শত্রুর ওপর বিজয় লাভ ও শারীরিক বল অর্জিত হয়।", generateArabicTtsUrl("الْقَوِيُّ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(54, "الْمَتِينُ", "আল-মাতীন", "Al-Mateen", "সুদৃঢ় ও অটল ক্ষমতার অধিকারী", "The Firm / Steadfast", "যার ক্ষমতা কখনো হ্রাস পায় না বা স্থানচ্যুত হয় না।", "সংকটময় পরিস্থিতিতে অটল ধৈর্য লাভ হয়।", generateArabicTtsUrl("الْمَتِينُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(55, "الْوَلِيُّ", "আল-ওয়ালী", "Al-Waliyy", "পরম বন্ধু ও পৃষ্ঠপোষক", "The Protecting Friend", "যিনি মুমিন বান্দাদের প্রকৃত সাহায্যকারী ও রক্ষাকর্তা।", "আল্লাহর বিশেষ নৈকট্য ও অভিভাবকত্ব অর্জিত হয়।", generateArabicTtsUrl("الْوَلِيُّ"), "PROTECTION"));
        ALLAH_NAMES.add(new AllahNameItem(56, "الْحَمِيدُ", "আল-হামীদ", "Al-Hameed", "সকল প্রশংসার একমাত্র যোগ্য", "The Praiseworthy", "যিনি স্বীয় সত্তায় প্রশংসিত, সমগ্র সৃষ্টি যার প্রশংসা গায়।", "মুখে মিষ্টভাষিতা আসে ও মানুষ সম্মান করে।", generateArabicTtsUrl("الْحَمِيدُ"), "PRAISE"));
        ALLAH_NAMES.add(new AllahNameItem(57, "الْمُحْصِي", "আল-মুহসী", "Al-Muhsee", "সকল কিছুর পুঙ্খানুপুঙ্খ গণনাকারী", "The Accounter", "যিনি সমগ্র সৃষ্টির প্রতিটি ধূলিকণার হিসাব রাখেন।", "কেয়ামতের কঠিন দিনে হিসাব সহজ হবে।", generateArabicTtsUrl("الْمُحْصِي"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(58, "الْمُبْدِئُ", "আল-মুবদি'", "Al-Mubdi'", "প্রথমবার অস্তিত্ব দানকারী", "The Originator", "যিনি প্রথম সৃষ্টিকে অস্তিত্বে এনেছেন কোনো পূর্বরূপ ছাড়া।", "গর্ভবতী নারীর সন্তান নিরাপদে থাকে ও সফল সূচনা হয়।", generateArabicTtsUrl("الْمُبْدِئُ"), "CREATION"));
        ALLAH_NAMES.add(new AllahNameItem(59, "الْمُعِيدُ", "আল-মু'ঈদ", "Al-Mu'eed", "পুনরায় সৃষ্টি ও ফিরিয়ে আনয়নকারী", "The Restorer", "যিনি ধ্বংসের পর সৃষ্টিকে আবার ফিরিয়ে আনতে সক্ষম।", "হারানো ব্যক্তি বা সম্পদ ফিরে পাওয়া যায়।", generateArabicTtsUrl("الْمُعِيدُ"), "CREATION"));
        ALLAH_NAMES.add(new AllahNameItem(60, "الْمُحْيِي", "আল-মুহয়ী", "Al-Muhyee", "জীবনদানকারী", "The Giver of Life", "যিনি প্রাণহীন বস্তুতে প্রাণের সঞ্চার করেন।", "রোগীর স্বাস্থ্য দ্রুত ভালো হয় ও জীবনীশক্তি বাড়ে।", generateArabicTtsUrl("الْمُحْيِي"), "LIFE"));

        ALLAH_NAMES.add(new AllahNameItem(61, "الْمُمِيتُ", "আল-মুমীত", "Al-Mumeet", "মৃত্যুদানকারী", "The Creator of Death", "যিনি নির্ধারিত সময়ে সকল জীবের মৃত্যুর ফয়সালা করেন।", "নফসের কুপ্রবৃত্তি ও কামভাব দমন হয়।", generateArabicTtsUrl("الْمُمِيتُ"), "LIFE"));
        ALLAH_NAMES.add(new AllahNameItem(62, "الْحَيُّ", "আল-হাইয়্যু", "Al-Hayy", "চিরঞ্জীব ও চিরজীবন্ত", "The Ever-Living", "যার জীবনের কোনো শুরু বা শেষ নেই, যিনি চিরকাল আছেন ও থাকবেন।", "দীর্ঘায়ু ও সুস্থতা অর্জিত হয়।", generateArabicTtsUrl("الْحَيُّ"), "LIFE"));
        ALLAH_NAMES.add(new AllahNameItem(63, "الْقَيُّومُ", "আল-ক্বাইয়্যুম", "Al-Qayyoom", "সকল কিছুর ধারক ও নিয়ন্ত্রক", "The Self-Subsisting", "যিনি স্বয়ংসম্পূর্ণ এবং যার সাহায্যে নিখিল বিশ্ব টিকে আছে।", "ক্লান্তি দূর হয় ও মনে স্থায়ী স্থিতি আসে।", generateArabicTtsUrl("الْقَيُّومُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(64, "الْوَاجِدُ", "আল-ওয়াজিদ", "Al-Wajid", "সমৃদ্ধ ও অমুখাপেক্ষী", "The Perceiver / Resourceful", "যিনি যা চান অনায়াসে পেয়ে যান, কোনো অভাব যার নেই।", "মনের উদারতা ও আর্থিক সচ্ছলতা লাভ হয়।", generateArabicTtsUrl("الْوَاجِدُ"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(65, "الْمَاجِدُ", "আল-মাজিদ", "Al-Majid", "উদার ও মহিমান্বিত", "The Illustrious", "যার দান ও কৃপা অগণিত ও অনুপম।", "অন্তরে নূর সৃষ্টি হয় ও চরিত্র সুন্দর হয়।", generateArabicTtsUrl("الْمَاجِدُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(66, "الْوَاحِدُ", "আল-ওয়াহিদ", "Al-Wahid", "এক ও একক", "The Unique One", "সত্তা ও গুণাবলীতে যার কোনো দ্বিতীয় নেই।", "একাকিত্ব ও ভয় কেটে যায় এবং তাওহীদ দৃঢ় হয়।", generateArabicTtsUrl("الْوَاحِدُ"), "UNITY"));
        ALLAH_NAMES.add(new AllahNameItem(67, "الْأَحَدُ", "আল-আহাদ", "Al-Ahad", "অদ্বিতীয় ও অংশীদারহীন", "The Indivisible", "যিনি এক, যার কোনো পিতা, সন্তান বা সমকক্ষ নেই।", "শিরক থেকে মন রক্ষা পায় ও ঈমান খাঁটি হয়।", generateArabicTtsUrl("الْأَحَدُ"), "UNITY"));
        ALLAH_NAMES.add(new AllahNameItem(68, "الصَّمَدُ", "আস-সামাদ", "As-Samad", "অমুখাপেক্ষী ও পরম আশ্রয়", "The Eternal / Self-Sufficient", "যার কাছে সমগ্র সৃষ্টি মুখাপেক্ষী, কিন্তু তিনি কারো মুখাপেক্ষী নন।", "অভাব-অনটন ও পরমুখাপেক্ষিতা দূর হয়।", generateArabicTtsUrl("الصَّمَدُ"), "UNITY"));
        ALLAH_NAMES.add(new AllahNameItem(69, "الْقَادِرُ", "আল-ক্বাদির", "Al-Qadir", "সর্বশক্তিমান ও সক্ষম", "The Capable / Omnipotent", "যিনি যা ইচ্ছা তা বাস্তবায়নে পূর্ণ সক্ষম।", "অসাধ্য কাজ সহজ হয় ও মনোবল বৃদ্ধি পায়।", generateArabicTtsUrl("الْقَادِرُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(70, "الْمُقْتَدِرُ", "আল-মুক্বতাদির", "Al-Muqtadir", "মহাপ্রভাবশালী ও চূড়ান্ত ক্ষমতার অধিকারী", "The Omnipotent Determiner", "যার নিখুঁত নিয়ন্ত্রণে সমগ্র সৃষ্টি আবর্তিত হচ্ছে।", "অসচেতনতা দূর হয় ও সত্য উদভাসিত হয়।", generateArabicTtsUrl("الْمُقْتَدِرُ"), "MAJESTY"));

        ALLAH_NAMES.add(new AllahNameItem(71, "الْمُقَدِّمُ", "আল-মুকাদ্দিম", "Al-Muqaddim", "অগ্রবর্তীকারী", "The Expediter", "যিনি যাকে ইচ্ছা উচ্চ মর্যাদায় এগিয়ে দেন।", "যুদ্ধে ও প্রতিযোগিতায় বিজয় এবং এগিয়ে থাকার তৌফিক হয়।", generateArabicTtsUrl("الْمُقَدِّمُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(72, "الْمُؤَخِّرُ", "আল-মুআখ্খির", "Al-Mu'akhkhir", "পশ্চাদবর্তীকারী ও বিলম্বকারী", "The Delayer", "যিনি হিকমতের কারণে যাকে ইচ্ছা পেছনে রাখেন।", "তওবা কবুল হয় ও পাপের শাস্তি স্থগিত থাকে।", generateArabicTtsUrl("الْمُؤَخِّرُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(73, "الْأَوَّلُ", "আল-আউয়াল", "Al-Awwal", "অনাদি ও সর্বপ্রথম", "The Very First", "যার অস্তিত্বের কোনো সূচনা নেই, যিনি সব সৃষ্টির পূর্বে ছিলেন।", "সন্তানহীনতা দূর হয় ও শুভ সূচনা লাভ হয়।", generateArabicTtsUrl("الْأَوَّلُ"), "ETERNITY"));
        ALLAH_NAMES.add(new AllahNameItem(74, "الْآخِرُ", "আল-আখির", "Al-Akhir", "অনন্ত ও সর্বশেষ", "The Infinite Last", "সমগ্র সৃষ্টি ধ্বংস হওয়ার পরও যিনি চিরকাল বিরাজমান থাকবেন।", "মৃত্যুর সময় ঈমানের সাথে শেষ নিঃশ্বাস ত্যাগ সহজ হয়।", generateArabicTtsUrl("الْآخِرُ"), "ETERNITY"));
        ALLAH_NAMES.add(new AllahNameItem(75, "الظَّاهِرُ", "আয-যাহির", "Az-Zahir", "প্রকাশ্য ও সুস্পষ্ট", "The Manifest", "যার অস্তিত্ব সমস্ত সৃষ্টির নিদর্শন দ্বারা সুস্পষ্ট প্রতীয়মান।", "চোখের জ্যোতি ও অন্তর্দৃষ্টি বৃদ্ধি পায়।", generateArabicTtsUrl("الظَّاهِرُ"), "TRUTH"));
        ALLAH_NAMES.add(new AllahNameItem(76, "الْبَاطِنُ", "আল-বাতিন", "Al-Batin", "গুপ্ত ও অদৃশ্য", "The Hidden", "যিনি চর্মচক্ষুর অন্তরালে থেকেও সমগ্র সত্তায় জড়িয়ে আছেন।", "অন্তরের রহস্যময় দ্বীনি প্রজ্ঞা অর্জিত হয়।", generateArabicTtsUrl("الْبَاطِنُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(77, "الْوَالِي", "আল-ওয়ালী", "Al-Wali", "একমাত্র অভিভাবক ও শাসক", "The Sole Governor", "যিনি মহাবিশ্বের প্রতিটি অনু-পরমাণু একক কর্তৃত্বে পরিচালনা করেন।", "ঘরবাড়ি ও ধন-সম্পদ প্রাকৃতিক দুর্যোগ থেকে নিরাপদ থাকে।", generateArabicTtsUrl("الْوَالِي"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(78, "الْمُتَعَالِي", "আল-মুতা'আলী", "Al-Muta'ali", "সর্বোচ্চ উন্নত ও মহান", "The Supreme Exalted", "যিনি সমস্ত সৃষ্টির কল্পনারও ঊর্ধ্বে অবস্থানরত।", "কঠিন কাজে সহজ সমাধান মেলে।", generateArabicTtsUrl("الْمُتَعَالِي"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(79, "الْبَرُّ", "আল-বার্র", "Al-Barr", "পরম দয়াময় ও কল্যাণদাতা", "The Source of All Goodness", "যিনি সৃষ্টিকে অবারিত কল্যাণ ও সৎপথের দিকনির্দেশনা দেন।", "নেক কাজের স্পৃহা বাড়ে ও বদভ্যাস দূর হয়।", generateArabicTtsUrl("الْبَرُّ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(80, "التَّوَّابُ", "আত-তাওয়াব", "At-Tawwab", "তওবা কবুলকারী", "The Acceptor of Repentance", "যিনি বারবার বান্দার ক্ষমাপ্রার্থনা কবুল করেন।", "পাপের অনুভূতি দূর হয়ে খাঁটি তওবার নসিব হয়।", generateArabicTtsUrl("التَّوَّابُ"), "MERCY"));

        ALLAH_NAMES.add(new AllahNameItem(81, "الْمُنْتَقِمُ", "আল-মুনতাক্বিম", "Al-Muntaqim", "উচিত প্রতিশোধ গ্রহণকারী", "The Avenger", "যিনি অবাধ্য জালিমদের কর্মের সমুচিত শাস্তি প্রদান করেন।", "জালিমের অত্যাচার থেকে নিষ্কৃতি পাওয়া যায়।", generateArabicTtsUrl("الْمُنْتَقِمُ"), "JUSTICE"));
        ALLAH_NAMES.add(new AllahNameItem(82, "الْعَفُوُّ", "আল-আফুউ", "Al-Afuww", "পরম মার্জনাশীল", "The Supreme Pardoner", "যিনি পাপের শাস্তি শুধু মওকুফই করেন না, বরং পাপের চিহ্নও মুছে দেন।", "গুনাহের দাগ সম্পূর্ণ সাফ হয়ে যায়।", generateArabicTtsUrl("الْعَفُوُّ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(83, "الرَّءُوفُ", "আর-রাউফ", "Ar-Ra'oof", "পরম স্নেহশীল ও সহানুভূতিশীল", "The Most Clement", "যিনি বান্দার কষ্টে গভীর মমতা ও কোমলতা প্রদর্শন করেন।", "সকলের সাথে সদ্ভাব ও হৃদ্যতা সৃষ্টি হয়।", generateArabicTtsUrl("الرَّءُوفُ"), "MERCY"));
        ALLAH_NAMES.add(new AllahNameItem(84, "مَالِكُ الْمُلْكِ", "মালিকুল মুলক", "Malik-ul-Mulk", "সমগ্র রাজত্বের একমাত্র অধিপতি", "Master of Dominion", "যিনি সৃষ্টিজগতের সমস্ত ক্ষমতার একচ্ছত্র মালিক।", "দরিদ্রতা দূর হয় ও সামাজিক প্রতিষ্ঠা লাভ হয়।", generateArabicTtsUrl("مَالِكُ الْمُلْكِ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(85, "ذُو الْجَلَالِ وَالْإِكْرَامِ", "যুল জালালি ওয়াল ইকরাম", "Dhul-Jalali wal-Ikram", "মহিমান্বিত ও পরম সম্মানকারী", "Lord of Glory and Honor", "যার ব্যক্তিত্ব মর্যাদায় পূর্ণ এবং যিনি বান্দাকে সম্মান দেন।", "ইজ্জত, সম্মান ও ধন-সম্পদে বরকত আসে।", generateArabicTtsUrl("ذُو الْجَلَالِ وَالْإِكْرَامِ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(86, "الْمُقْسِطُ", "আল-মুক্বসিত", "Al-Muqsit", "সুবিচারক ও হক্কের রক্ষক", "The Equitable / Just", "যিনি মজলুমের অধিকার ফিরিয়ে দেন ও সুবিচার প্রতিষ্ঠা করেন।", "শয়তানের ধোঁকা ও কুমন্ত্রণা থেকে মুক্তি মেলে।", generateArabicTtsUrl("الْمُقْسِطُ"), "JUSTICE"));
        ALLAH_NAMES.add(new AllahNameItem(87, "الْجَامِعُ", "আল-জামি'", "Al-Jami'", "একত্রকারী ও সমাবেশকারী", "The Gatherer", "যিনি হাশরের ময়দানে সমগ্র মানবজাতিকে একত্রিত করবেন।", "বিচ্ছিন্ন পরিবার ও হারিয়ে যাওয়া আপনজন একত্রিত হয়।", generateArabicTtsUrl("الْجَامِعُ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(88, "الْغَنِيُّ", "আল-গানীয়্যু", "Al-Ghaniyy", "স্বয়ংসম্পূর্ণ ও ধনী", "The Self-Sufficient", "যার কোনো কিছুর প্রয়োজন নেই, যিনি কারো ওপর নির্ভরশীল নন।", "মন বড় হয় এবং অমুখাপেক্ষিতা লাভ হয়।", generateArabicTtsUrl("الْغَنِيُّ"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(89, "الْمُغْنِي", "আল-মুগনী", "Al-Mughni", "অভাবমোচনকারী ও সমৃদ্ধিদাতা", "The Enricher", "যিনি বান্দাকে সম্পদ ও মানসিক তৃপ্তি দান করে অভাবমুক্ত করেন।", "দারিদ্র্য দূর হয়ে জীবনে সচ্ছলতা আসে।", generateArabicTtsUrl("الْمُغْنِي"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(90, "الْمَانِعُ", "আল-মানি'", "Al-Mani'", "প্রতিরোধকারী ও বারণকারী", "The Preventer", "যিনি স্বীয় হিকমতের কারণে ক্ষতিকর বিষয় থেকে বান্দাকে আটকে রাখেন।", "পারিবারিক কলহ ও অমঙ্গল দূর হয়।", generateArabicTtsUrl("الْمَانِعُ"), "PROTECTION"));

        ALLAH_NAMES.add(new AllahNameItem(91, "الضَّارُّ", "আদ-দার্রু", "Ad-Darr", "ক্ষতিসাধনকারী ও পরীক্ষক", "The Creator of Harm", "যিনি হিকমত অনুযায়ী শিক্ষা বা পরীক্ষার জন্য ক্ষতি পৌঁছাতে পারেন।", "বিপদাপদ ও বালা-মুসিবত হতে নিরাপত্তা মেলে।", generateArabicTtsUrl("الضَّارُّ"), "MAJESTY"));
        ALLAH_NAMES.add(new AllahNameItem(92, "النَّافِعُ", "আন-নাফি'", "An-Nafi'", "উপকারকারী ও কল্যাণদাতা", "The Creator of Good", "যার অনুমতি ছাড়া কেউ কারো সামান্যতম উপকার করতে পারে না।", "ব্যবসা-বাণিজ্যে লাভ ও সুস্থতা অর্জিত হয়।", generateArabicTtsUrl("النَّافِعُ"), "PROVISION"));
        ALLAH_NAMES.add(new AllahNameItem(93, "النُّورُ", "আন-নূর", "An-Noor", "পরম জ্যোতি ও আলোকবর্তিকা", "The Light", "যিনি আকাশমন্ডলী ও পৃথিবীর জ্যোতি এবং অন্ধকার দূরকারী।", "চেহারায় ও অন্তরে ঈমানের জ্যোতি ফুটে ওঠে।", generateArabicTtsUrl("النُّورُ"), "PURITY"));
        ALLAH_NAMES.add(new AllahNameItem(94, "الْهَادِي", "আল-হাদী", "Al-Hadi", "সঠিক পথপ্রদর্শক", "The Guide", "যিনি বান্দাকে সিরাতুল মুস্তাকিমের সরল পথ দেখান।", "সঠিক দ্বীনি পথে চলা ও হেদায়েত লাভ সহজ হয়।", generateArabicTtsUrl("الْهَادِي"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(95, "الْبَدِيعُ", "আল-বাদী'", "Al-Badee'", "অনুপম ও অপূর্ব স্রষ্টা", "The Incomparable Originator", "যিনি অপূর্ব শৈল্পিক কারুকার্যে মহাবিশ্ব নির্মাণ করেছেন।", "দুশ্চিন্তা দূর হয় ও কাজে অপূর্ব সাফল্য আসে।", generateArabicTtsUrl("الْبَدِيعُ"), "CREATION"));
        ALLAH_NAMES.add(new AllahNameItem(96, "الْبَاقِي", "আল-বাক্বী", "Al-Baqi", "চিরস্থায়ী ও অবিনশ্বর", "The Everlasting", "সব সৃষ্টি বিলীন হওয়ার পরও যিনি চিরকাল থাকবেন।", "আমলে নিষ্ঠা আসে ও অমঙ্গল থেকে হেফাজত থাকে।", generateArabicTtsUrl("الْبَاقِي"), "ETERNITY"));
        ALLAH_NAMES.add(new AllahNameItem(97, "الْوَارِثُ", "আল-ওয়ারিস", "Al-Warith", "চূড়ান্ত মালিক ও উত্তরাধিকারী", "The Ultimate Inheritor", "সৃষ্টির পর সকল কিছুর চূড়ান্ত মালিকানা যার কাছে ফিরে যাবে।", "সম্পদে বরকত আসে ও পরকালের সম্বল প্রস্তুত হয়।", generateArabicTtsUrl("الْوَارِثُ"), "ETERNITY"));
        ALLAH_NAMES.add(new AllahNameItem(98, "الرَّشِيدُ", "আর-রাশীদ", "Ar-Rasheed", "সত্যের নির্দেশক ও নির্ভুল পথপ্রদর্শক", "The Righteous Teacher", "যিনি নিখুঁত প্রজ্ঞায় সঠিক পরিণতির দিকে পরিচালিত করেন।", "কাজের জটিলতা কেটে যায় ও শুভ ফল মেলে।", generateArabicTtsUrl("الرَّشِيدُ"), "WISDOM"));
        ALLAH_NAMES.add(new AllahNameItem(99, "الصَّبُورُ", "আস-সাবূর", "As-Saboor", "অসীম ধৈর্যশীল", "The Most Patient", "যিনি অবাধ্যতার পরও তৎক্ষণাৎ পাকড়াও না করে সময় দেন।", "ধৈর্য ও সহনশীলতা অর্জন করা যায়।", generateArabicTtsUrl("الصَّبُورُ"), "MERCY"));
    }

    public static List<AllahNameItem> getAllNames() {
        return Collections.unmodifiableList(ALLAH_NAMES);
    }

    public static List<AllahNameItem> searchNames(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllNames();
        }
        String clean = query.trim().toLowerCase();
        List<AllahNameItem> results = new ArrayList<>();
        for (AllahNameItem item : ALLAH_NAMES) {
            if (item.getNameBengali().toLowerCase().contains(clean) ||
                item.getNameArabic().contains(clean) ||
                item.getNameEnglish().toLowerCase().contains(clean) ||
                item.getMeaningBengali().toLowerCase().contains(clean) ||
                item.getMeaningEnglish().toLowerCase().contains(clean) ||
                String.valueOf(item.getNumber()).equals(clean)) {
                results.add(item);
            }
        }
        return results;
    }
}
