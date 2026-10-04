package com.devflux.deenone.features.faraid.engine;

import java.io.Serializable;

/**
 * Authentic Qur'an and Sunnah Dalil Repository for classical Islamic Faraid calculation.
 * Contains verified Arabic matn, Bangla and English pronunciation, translation, scholarly reference sources,
 * four Sunni madhab consensus notes, and 1961 BD law comparison.
 */
public class FaraidDalilRepository implements Serializable {

    public static class DalilItem implements Serializable {
        public final String arabicText;
        public final String banglaPronunciation;
        public final String englishPronunciation;
        public final String banglaTranslation;
        public final String englishTranslation;
        public final String referenceSource;
        public final String referenceSourceEn;
        public final String madhabConsensusBn;
        public final String madhabConsensusEn;
        public final String bangladeshLawNoteBn;
        public final String bangladeshLawNoteEn;

        public DalilItem(String arabicText, String banglaPronunciation, String englishPronunciation,
                         String banglaTranslation, String englishTranslation,
                         String referenceSource, String referenceSourceEn,
                         String madhabConsensusBn, String madhabConsensusEn,
                         String bangladeshLawNoteBn, String bangladeshLawNoteEn) {
            this.arabicText = arabicText;
            this.banglaPronunciation = banglaPronunciation;
            this.englishPronunciation = englishPronunciation;
            this.banglaTranslation = banglaTranslation;
            this.englishTranslation = englishTranslation;
            this.referenceSource = referenceSource;
            this.referenceSourceEn = referenceSourceEn;
            this.madhabConsensusBn = madhabConsensusBn;
            this.madhabConsensusEn = madhabConsensusEn;
            this.bangladeshLawNoteBn = bangladeshLawNoteBn;
            this.bangladeshLawNoteEn = bangladeshLawNoteEn;
        }

        public DalilItem(String arabicText, String banglaPronunciation, String banglaTranslation,
                         String referenceSource, String madhabConsensusBn, String bangladeshLawNoteBn) {
            this(arabicText, banglaPronunciation, banglaPronunciation, banglaTranslation, banglaTranslation,
                 referenceSource, referenceSource, madhabConsensusBn, madhabConsensusBn,
                 bangladeshLawNoteBn, bangladeshLawNoteBn);
        }

        public String getTranslation(boolean isBn) {
            if (isBn) return banglaTranslation;
            return (englishTranslation != null && !englishTranslation.isEmpty()) ? englishTranslation : banglaTranslation;
        }

        public String getPronunciation(boolean isBn) {
            if (isBn) return banglaPronunciation;
            return (englishPronunciation != null && !englishPronunciation.isEmpty()) ? englishPronunciation : banglaPronunciation;
        }

        public String getReference(boolean isBn) {
            if (isBn) return referenceSource;
            return (referenceSourceEn != null && !referenceSourceEn.isEmpty()) ? referenceSourceEn : referenceSource;
        }

        public String getMadhabConsensus(boolean isBn) {
            if (isBn) return madhabConsensusBn;
            return (madhabConsensusEn != null && !madhabConsensusEn.isEmpty()) ? madhabConsensusEn : madhabConsensusBn;
        }

        public String getBangladeshLawNote(boolean isBn) {
            if (isBn) return bangladeshLawNoteBn;
            return (bangladeshLawNoteEn != null && !bangladeshLawNoteEn.isEmpty()) ? bangladeshLawNoteEn : bangladeshLawNoteBn;
        }

        public String getSourceUrl() {
            if (referenceSource == null) return "https://quran.com";
            if (referenceSource.contains("৪:১১") || referenceSource.contains("4:11")) return "https://quran.com/4/11";
            if (referenceSource.contains("৪:১২") || referenceSource.contains("4:12")) return "https://quran.com/4/12";
            if (referenceSource.contains("৪:১৭৬") || referenceSource.contains("4:176")) return "https://quran.com/4/176";
            if (referenceSource.contains("৬৭৩২") || referenceSource.contains("6732")) return "https://sunnah.com/bukhari:6732";
            if (referenceSource.contains("৬৭৩৭") || referenceSource.contains("6737")) return "https://sunnah.com/bukhari:6737";
            if (referenceSource.contains("১৬১৫") || referenceSource.contains("1615")) return "https://sunnah.com/muslim:1615a";
            return "https://quran.com";
        }
    }

    public static DalilItem getHusbandDalil(boolean hasChildren) {
        if (!hasChildren) {
            return new DalilItem(
                    "وَلَكُمْ نِصْفُ مَا تَرَكَ أَزْوَاجُكُمْ إِن لَّمْ يَكُن لَّهُنَّ وَلَدٌ",
                    "ওয়া লাকুম নিসফু মা তারা কা আজওয়াজুকুম ইল্লাম ইয়াকুল লাহুন্না ওয়ালাদ",
                    "Wa lakum nisfu ma taraka azwajukum illam yakul lahunna walad",
                    "আর তোমাদের স্ত্রীদের ত্যাজ্য সম্পত্তির অর্ধাংশ (১/২) তোমাদের জন্য, যদি তাদের কোনো সন্তান না থাকে।",
                    "And for you is half (1/2) of what your wives leave if they have no child.",
                    "সূরা আন-নিসা: ৪:১২",
                    "Surah An-Nisa: 4:12",
                    "চার মাযহাবের সর্বসম্মত ইজমা অনুযায়ী নিঃসন্তান স্ত্রীর সম্পত্তিতে স্বামীর নির্ধারিত হিস্যা ১/২ (অর্ধেক)।",
                    "Consensus of the four Sunni schools that the husband receives a fixed 1/2 share when the wife leaves no child.",
                    "বাংলাদেশ মুসলিম পারিবারিক আইন ও সাধারণ শরীয়াহ উভয় অনুসারেই স্বামী ১/২ অংশ পাবেন।",
                    "Under both classical Shariah and Bangladesh Muslim Family Laws Ordinance 1961, husband receives 1/2 share."
            );
        } else {
            return new DalilItem(
                    "فَإِن كَانَ لَهُنَّ وَلَدٌ فَلَكُمُ الرُّبُعُ مِمَّا تَرَكْنَ مِن بَعْدِ وَصِيَّةٍ يُوصِينَ بِهَا أَوْ دَيْنٍ",
                    "ফাইং কানা লাহুন্না ওয়ালাদুন ফালাকুমুর রুবু'উ মিম্মা তারাকনা মিম বা'দি ওয়াসিয়্যাতিল ইউসীনা বিহা আও দাইন",
                    "Fa in kana lahunna waladun falakumur-rubu'u mimma tarakna mim ba'di wasiyyatin yooseena biha aw dayn",
                    "আর যদি তাদের সন্তান থাকে, তবে তাদের ত্যাজ্য সম্পত্তির এক-চতুর্থাংশ (১/৪) তোমাদের জন্য, তাদের কৃত অসিয়ত ও ঋণ পরিশোধের পর।",
                    "But if they have a child, for you is one fourth (1/4) of what they leave, after any bequest they made or debt.",
                    "সূরা আন-নিসা: ৪:১২",
                    "Surah An-Nisa: 4:12",
                    "সন্তান (ছেলে, মেয়ে, নাতি, নাতনি) বিদ্যমান থাকলে স্বামীর অংশ ১/২ থেকে হ্রাস পেয়ে ১/৪ (হজব নু কসান) হয়।",
                    "When qualifying descendants exist, the husband's share is reduced from 1/2 to 1/4 (Hajb Nuqsan).",
                    "বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১ অনুযায়ী সন্তান বা মৃত সন্তানের সন্তানের উপস্থিতিতে স্বামী ১/৪ অংশ পাবেন।",
                    "Under Bangladesh Muslim Family Laws Ordinance 1961, husband receives 1/4 in the presence of children or grandchildren."
            );
        }
    }

    public static DalilItem getWifeDalil(boolean hasChildren) {
        if (!hasChildren) {
            return new DalilItem(
                    "وَلَهُنَّ الرُّبُعُ مِمَّا تَرَكْتُمْ إِن لَّمْ يَكُن لَّكُمْ وَلَدٌ",
                    "ওয়া লাহুন্নার রুবু'উ মিম্মা তারাকতুম ইল্লাম ইয়াকুল লাকুম ওয়ালাদ",
                    "Wa lahunnar-rubu'u mimma taraktum illam yakul lakum walad",
                    "আর স্ত্রীদের জন্য তোমাদের ত্যাজ্য সম্পত্তির এক-চতুর্থাংশ (১/৪), যদি তোমাদের কোনো সন্তান না থাকে।",
                    "And for the wives is one fourth (1/4) if you leave no child.",
                    "সূরা আন-নিসা: ৪:১২",
                    "Surah An-Nisa: 4:12",
                    "মৃত স্বামীর কোনো সন্তান না থাকলে স্ত্রী (এক বা একাধিক হলে সকলে মিলে সমান ভাগে) ১/৪ অংশ পাবেন।",
                    "If the deceased husband leaves no child, the wife (or wives collectively divided equally) receives 1/4.",
                    "বাংলাদেশ পারিবারিক আইন ও শরীয়াহ মোতাবেক সন্তানহীন স্বামীর সম্পত্তিতে স্ত্রী/স্ত্রীগণের সম্মিলিত হিস্যা ১/৪।",
                    "Under classical Shariah and Bangladesh law, wife/wives collectively receive 1/4 without children."
            );
        } else {
            return new DalilItem(
                    "فَإِن كَانَ لَكُمْ وَلَدٌ فَلَهُنَّ الثُّمُنُ مِمَّا تَرَكْتُم مِّن بَعْدِ وَصِيَّةٍ تُوصُونَ بِهَا أَوْ دَيْنٍ",
                    "ফাইং কানা লাকুম ওয়ালাদুন ফালাহুন্নাছ ছুমুনু মিম্মা তারাকতুম মিম বা'দি ওয়াসিয়্যাতিন তূসূনা বিহা আও দাইন",
                    "Fa in kana lakum waladun falahunnath-thumunu mimma taraktum mim ba'di wasiyyatin toosoona biha aw dayn",
                    "আর যদি তোমাদের সন্তান থাকে, তবে তাদের জন্য তোমাদের ত্যাজ্য সম্পত্তির এক-অষ্টমাংশ (১/৮), তোমাদের কৃত অসিয়ত ও ঋণ পরিশোধের পর।",
                    "But if you leave a child, then for them is an eighth (1/8) of what you leave, after any bequest made or debt.",
                    "সূরা আন-নিসা: ৪:১২",
                    "Surah An-Nisa: 4:12",
                    "সন্তানের উপস্থিতিতে স্ত্রী/স্ত্রীগণ সম্মিলিতভাবে ১/৮ অংশ পাবেন। একাধিক স্ত্রী থাকলে এই ১/৮ অংশ তাদের মাঝে সমানভাবে বণ্টিত হবে।",
                    "In the presence of children, wife/wives collectively receive 1/8, divided equally among multiple wives.",
                    "বাংলাদেশ আইন ১৯৬১ এর ৪ ধারা মতে নাতি-নাতনি থাকলেও স্ত্রীর অংশ ১/৮ হবে।",
                    "Under Section 4 of Bangladesh Law 1961, presence of grandchildren also reduces wives' share to 1/8."
            );
        }
    }

    public static DalilItem getSpouseDalil(boolean isWife, boolean hasChildren) {
        if (isWife) {
            return getWifeDalil(hasChildren);
        } else {
            return getHusbandDalil(hasChildren);
        }
    }

    public static DalilItem getMotherDalil(boolean hasChildren, boolean hasMultipleSiblings) {
        if (hasChildren || hasMultipleSiblings) {
            return new DalilItem(
                    "وَلِأَبَوَيْهِ لِكُلِّ وَاحِدٍ مِّنْهُمَا السُّدُسُ مِمَّا تَرَكَ إِن كَانَ لَهُ وَلَدٌ ۚ فَإِن كَانَ لَهُ إِخْوَةٌ فَلِأُمِّهِ السُّدُسُ",
                    "ওয়া লি-আবাওয়াইহি লিকুল্লি ওয়াহিদিম মিনহুমাস সুদুসু মিম্মা তারা কা ইং কানা লাহু ওয়ালাদ, ফাইং কানা লাহু ইখওয়াতুন ফা-লি-উম্মিহিস সুদুসু",
                    "Wa li-abawayhi likulli wahidim-minhuma as-sudusu mimma taraka in kana lahu walad, fa in kana lahu ikhwatun fali-ummihi as-sudus",
                    "আর মৃতের পিতা-মাতা উভয়ের প্রত্যেকের জন্য ত্যাজ্য সম্পত্তির এক-ষষ্ঠাংশ (১/৬), যদি তার সন্তান থাকে। আর যদি তার একাধিক ভাই-বোন থাকে, তবে তার মাতার জন্য এক-ষষ্ঠাংশ (১/৬)।",
                    "And for one's parents, to each one of them is a sixth (1/6) of the estate if he left children. But if he had siblings, then for his mother is a sixth (1/6).",
                    "সূরা আন-নিসা: ৪:১১",
                    "Surah An-Nisa: 4:11",
                    "সন্তান অথবা ২ বা ততোধিক ভাই-বোন (সহোদর, বৈমাত্রেয় বা বৈপিত্রীয়) থাকলে মা ১/৬ অংশ পাবেন।",
                    "Mother receives 1/6 when children or 2 or more siblings exist across all four Sunni madhabs.",
                    "বাংলাদেশ মুসলিম পারিবারিক আইন এবং চার মাযহাবের সর্বসম্মত বিধান।",
                    "Unanimous across Bangladesh Muslim law and four Sunni madhabs."
            );
        } else {
            return new DalilItem(
                    "فَإِن لَّمْ يَكُن لَّهُ وَلَدٌ وَوَرِثَهُ أَبَوَاهُ فَلِأُمِّهِ الثُّلُثُ",
                    "ফাইং লাম ইয়াকুল লাহু ওয়ালাদুওঁ ওয়া ওয়ারিছাহু আবাওয়াহু ফা-লি-উম্মিহিছ ছুলুছ",
                    "Fa illam yakul lahu waladun wa warithahu abawahu fali-ummihith-thuluth",
                    "আর যদি তার কোনো সন্তান না থাকে এবং কেবল পিতা-মাতাই তার ওয়ারিশ হয়, তবে তার মাতার জন্য এক-তৃতীয়াংশ (১/৩)।",
                    "And if he had no children and the parents [alone] inherit from him, then for his mother is one third (1/3).",
                    "সূরা আন-নিসা: ৪:১১",
                    "Surah An-Nisa: 4:11",
                    "সন্তান ও একাধিক ভাই-বোনের অবর্তমানে মাতা সম্পূর্ণ সম্পত্তির ১/৩ অংশ পাবেন। (উমারিয়্যাতান মাসআলায় অবশিষ্টের ১/৩)।",
                    "In the absence of children and multiple siblings, mother receives 1/3 of the estate (1/3 of residue in Umariyyatan).",
                    "বাংলাদেশ মুসলিম উত্তরাধিকার আইনে মাতা ১/৩ অংশ প্রাপ্ত হন।",
                    "Under Bangladesh inheritance laws, mother receives 1/3 share."
            );
        }
    }

    public static DalilItem getFatherDalil(boolean hasMaleChild, boolean hasFemaleChildOnly) {
        if (hasMaleChild) {
            return new DalilItem(
                    "وَلِأَبَوَيْهِ لِكُلِّ وَاحِدٍ مِّنْهُمَا السُّدُسُ مِمَّا تَرَكَ إِن كَانَ لَهُ وَلَدٌ",
                    "ওয়া লি-আবাওয়াইহি লিকুল্লি ওয়াহিদিম মিনহুমাস সুদুসু মিম্মা তারা কা ইং কানা লাহু ওয়ালাদ",
                    "Wa li-abawayhi likulli wahidim-minhuma as-sudusu mimma taraka in kana lahu walad",
                    "আর মৃতের পিতা-মাতা উভয়ের প্রত্যেকের জন্য ত্যাজ্য সম্পত্তির এক-ষষ্ঠাংশ (১/৬), যদি তার সন্তান থাকে।",
                    "And for one's parents, to each one of them is a sixth (1/6) of his estate if he left children.",
                    "সূরা আন-নিসা: ৪:১১",
                    "Surah An-Nisa: 4:11",
                    "মৃতের ছেলে বা নাতি জীবিত থাকলে পিতা কেবল নির্ধারিত ১/৬ অংশ পাবেন, আসাবা হিসেবে উদ্বৃত্ত পাবেন না।",
                    "Father receives a fixed 1/6 share when sons or grandsons are present (not Asabah residue).",
                    "বাংলাদেশ ও শরীয়াহ আইনে পুত্রের উপস্থিতিতে পিতা ১/৬ অংশ পান।",
                    "Father receives 1/6 in presence of male descendants under BD and Shariah law."
            );
        } else if (hasFemaleChildOnly) {
            return new DalilItem(
                    "وَلِأَبَوَيْهِ لِكُلِّ وَاحِدٍ مِّنْهُمَا السُّدُسُ ... أَلْحِقُوا الْفَرَائِضَ بِأَهْلِهَا فَمَا بَقِيَ فَلِأَوْلَى رَجُلٍ ذَكَرٍ",
                    "সুদুসু বিল ফারদ ওয়া মা বাকিয়া বিল আসাবাহ",
                    "Sudusu bil-fard wa ma baqiya bil-asabah",
                    "পিতা নির্ধারিত ১/৬ অংশ পাবেন এবং কন্যাদের নির্ধারিত অংশ দেওয়ার পর অবশিষ্ট সম্পদ আসাবা হিসেবে পাবেন।",
                    "Father receives 1/6 as fixed share plus the remaining residue as Asabah after daughters' shares.",
                    "সূরা আন-নিসা: ৪:১১ ও সহীহ বুখারী: ৬৭৩২",
                    "Surah An-Nisa: 4:11 & Sahih Bukhari: 6732",
                    "কেবল কন্যা/নাতনি থাকলে পিতা একই সাথে নির্ধারিত অংশীদার (১/৬) এবং আসাবা (অবশিষ্টভোগী) হিসেবে দ্বৈত অধিকার লাভ করেন।",
                    "With daughters only, father inherits in dual capacity: fixed 1/6 + residuary (Asabah).",
                    "বাংলাদেশ মুসলিম আইনে পিতা ১/৬ + আসাবা অবশিষ্ট পান।",
                    "Father receives 1/6 + residue under BD Muslim law."
            );
        } else {
            return new DalilItem(
                    "أَلْحِقُوا الْفَرَائِضَ بِأَهْلِهَا فَمَا بَقِيَ فَلِأَوْلَى رَجُلٍ ذَكَرٍ",
                    "আলহিকুল ফারায়িদা বি-আহলিহা ফামা বাকিয়া ফাহুওয়া লি-আওলা রাজুলিন জাকার",
                    "Alhiqul-fara'ida bi-ahliha fama baqiya fahuwa li-awla rajulin dhakar",
                    "নির্ধারিত অংশীদারদের তাদের হিস্যা প্রদান করো; অতঃপর যা অবশিষ্ট থাকে তা নিকটতম পুরুষ আত্মীয়ের প্রাপ্য।",
                    "Give the shares to those entitled to them, and what remains belongs to the nearest male relative.",
                    "সহীহ বুখারী: ৬৭৩২ ও সহীহ মুসলিম: ১৬১৫ক",
                    "Sahih Bukhari: 6732 & Sahih Muslim: 1615a",
                    "সন্তানহীন অবস্থায় পিতা সর্বপ্রধান আসাবা (bi-nafsihi) হিসেবে অন্য অংশীদারদের দেওয়ার পর অবশিষ্ট সর্বস্ব লাভ করবেন।",
                    "Without children, father inherits the entire residue as the primary Asabah bi-nafsihi.",
                    "বাংলাদেশ ও বিশ্বব্যাপী সুন্নি চার মাযহাবের সর্বসম্মত বিধান।",
                    "Unanimous standard across all four Sunni madhabs and Bangladesh law."
            );
        }
    }

    public static DalilItem getSingleDaughterDalil() {
        return new DalilItem(
                "وَإِن كَانَتْ وَاحِدَةً فَلَهَا النِّصْفُ",
                "ওয়া ইং কানাত ওয়াহিদাতান ফালাহান নিসফ",
                "Wa in kanat wahidatan falahan-nisf",
                "আর যদি কেবল একজন কন্যা থাকে, তবে তার জন্য অর্ধাংশ (১/২)।",
                "And if there is only one, for her is half (1/2).",
                "সূরা আন-নিসা: ৪:১১",
                "Surah An-Nisa: 4:11",
                "একক কন্যা পুত্রহীন অবস্থায় সম্পত্তির অর্ধেক (১/২) লাভ করেন। আসাবা না থাকলে রাদ্দ এর মাধ্যমে অবশিষ্টাংশও পান।",
                "A single daughter inherits 1/2 of the estate in the absence of sons, receiving residue via Radd if no Asabah.",
                "বাংলাদেশ মুসলিম আইন ও শরীয়াহ মোতাবেক একক কন্যার নির্ধারিত অংশ ১/২।",
                "Single daughter receives 1/2 fixed Quranic share under BD and Shariah law."
        );
    }

    public static DalilItem getMultipleDaughtersDalil() {
        return new DalilItem(
                "فَإِن كُنَّ نِسَاءً فَوْقَ اثْنَتَيْنِ فَلَهُنَّ ثُلُثَا مَا تَرَكَ",
                "ফাইং কুন্না নিসা-আন ফাওকাছনাতাইনি ফালাহুন্না ছুলুছা মা তারা কা",
                "Fa in kunna nisa'an fawqath-natayni falahunna thulutha ma tarak",
                "আর যদি কেবল দুইয়ের অধিক (বা দুই বা ততোধিক) কন্যা থাকে, তবে তাদের জন্য ত্যাজ্য সম্পত্তির দুই-তৃতীয়াংশ (২/৩)।",
                "But if there are daughters, two or more, for them is two thirds (2/3) of what he left.",
                "সূরা আন-নিসা: ৪:১১",
                "Surah An-Nisa: 4:11",
                "দুই বা ততোধিক কন্যা পুত্রহীন অবস্থায় সম্মিলিতভাবে সমান ভাগে ২/৩ অংশ লাভ করবেন।",
                "Two or more daughters collectively share 2/3 equally in the absence of sons.",
                "ইজমা ও বাংলাদেশ মুসলিম পারিবারিক আইন অনুযায়ী একাধিক কন্যার যৌথ অংশ ২/৩।",
                "Multiple daughters share 2/3 under Ijma and BD Muslim Family Laws."
        );
    }

    public static DalilItem getSonsAndDaughtersDalil() {
        return new DalilItem(
                "يُوصِيكُمُ اللَّهُ فِي أَوْلَادِكُمْ ۖ لِلذَّكَرِ مِثْلُ حَظِّ الْأُنثَيَيْنِ",
                "ইউসীকুমুল্লাহু ফী আওলাদিকুম লিজ-জাকারি মিছলু হাদ্ধিল উনছায়াইন",
                "Yooseekumullahu fee awladikum lidh-dhakari mithlu hazzil-unthayayn",
                "আল্লাহ তোমাদের সন্তানদের ব্যাপারে নির্দেশ দিচ্ছেন: এক পুরুষের অংশ দুই নারীর অংশের সমান (২:১ অনুপাতে)।",
                "Allah instructs you concerning your children: for the male, what is equal to the share of two females (2:1).",
                "সূরা আন-নিসা: ৪:১১",
                "Surah An-Nisa: 4:11",
                "পুত্র ও কন্যা একত্রে উপস্থিত থাকলে কন্যা আসাবা বিল-গাইর হয় এবং প্রত্যেকে ২:১ অনুপাতে অবশিষ্ট সম্পদ ভাগ করে নেয়।",
                "When sons and daughters exist together, daughters become Asabah bil-ghayr and share the residue 2:1.",
                "বাংলাদেশ আইন ও চার মাযহাবের সর্বসম্মত শ্বাশত কোরআনিক বিধান।",
                "Universal 2:1 ratio for sons and daughters under Quranic mandate and BD law."
        );
    }

    public static final DalilItem GRANDDAUGHTER_DALIL = new DalilItem(
            "قَضَى النَّبِيُّ ﷺ لِلابْنَةِ النِّصْفَ، وَلابْنَةِ الابْنِ السُّدُسَ تَكْمِلَةَ الثُّلُثَيْنِ",
            "কাদান নাবিয়্যু সাল্লাল্লাহু আলাইহি ওয়াসাল্লামা লিল ইবনাতি নিসফা ওয়া লিবনাতিল ইবনিস সুদুসা তাকমিলাতাছ ছুলুছাইন",
            "Qadan-Nabiyyu sallallahu alayhi wa sallam lil-ibnati an-nisfa wa libnatil-ibnis-sudusa takmilatath-thuluthayn",
            "নবী করীম ﷺ ফয়সালা দেন: একক কন্যার জন্য ১/২ এবং নাতনির জন্য দুই-তৃতীয়াংশ (২/৩) পূর্ণ করতে ১/৬ অংশ।",
            "The Prophet ﷺ decreed: for the daughter half, and for the granddaughter (son's daughter) one-sixth to complete two-thirds.",
            "সহীহ বুখারী: ৬৭৩৭",
            "Sahih Bukhari: 6737",
            "মৃতের কেবল ১ কন্যা থাকলে নাতনি (পুত্রের মেয়ে) ২/৩ পূর্ণ করার জন্য ১/৬ (তাকমিলাতুস সুলুসাইন) লাভ করে।",
            "Granddaughter receives 1/6 (Takmilat al-Thuluthayn) to complete 2/3 when only 1 daughter exists.",
            "বাংলাদেশ মুসলিম আইন ১৯৬১ এর পূর্বে ও শরীয়াহর সুপ্রতিষ্ঠিত হাদিস ভিত্তিক বিধান।",
            "Established Prophetic ruling for granddaughter's completion share."
    );

    public static DalilItem getGranddaughterTakmilatDalil() {
        return GRANDDAUGHTER_DALIL;
    }

    public static DalilItem getFullSisterSingleDalil() {
        return new DalilItem(
                "إِنِ امْرُؤٌ هَلَكَ لَيْسَ لَهُ وَلَدٌ وَلَهُ أُخْتٌ فَلَهَا نِصْفُ مَا تَرَكَ",
                "ইনিম রুউন হালাকা লাইসা লাহু ওয়ালাদুওঁ ওয়া লাহু উখতুন ফালাহা নিসফু মা তারা কা",
                "Inimru'un halaka laysa lahu waladun wa lahu ukhtun falaha nisfu ma tarak",
                "কোনো ব্যক্তি সন্তানহীন অবস্থায় মারা গেলে এবং তার এক বোন থাকলে, সে ত্যাজ্য সম্পত্তির অর্ধাংশ (১/২) পাবে।",
                "If a person dies leaving no child but has a sister, for her is half (1/2) of what he left.",
                "সূরা আন-নিসা: ৪:১৭৬",
                "Surah An-Nisa: 4:176",
                "কালালাহ (পিতা ও সন্তানহীন) অবস্থায় একক সহোদর বোন ১/২ অংশ লাভ করেন।",
                "Single full sister receives 1/2 in Kalalah (no parents or descendants).",
                "কোরআনুল কারীমের অকাট্য কালালাহ বিধান।",
                "Direct Quranic Kalalah rule."
        );
    }

    public static DalilItem getFullSistersMultipleDalil() {
        return new DalilItem(
                "فَإِن كَانَتَا اثْنَتَيْنِ فَلَهُمَا الثُّلُثَانِ مِمَّا تَرَكَ",
                "ফাইং কানাতাস নাতাইনি ফালাহুমাছ ছুলুছানি মিম্মা তারা কা",
                "Fa in kanatath-natayni falahumath-thuluthani mimma tarak",
                "আর যদি তারা দুই বোন হয় (বা ততোধিক), তবে তাদের জন্য ত্যাজ্য সম্পত্তির দুই-তৃতীয়াংশ (২/৩)।",
                "But if there are two [or more] sisters, they shall have two thirds (2/3) of what he left.",
                "সূরা আন-নিসা: ৪:১৭৬",
                "Surah An-Nisa: 4:176",
                "কালালাহ অবস্থায় একাধিক সহোদর বোন সম্মিলিতভাবে সমান ভাগে ২/৩ অংশ লাভ করবেন।",
                "Multiple full sisters share 2/3 equally in Kalalah.",
                "চার মাযহাবের ইজমা ও কালালাহ নীতি।",
                "Ijma of four madhabs for multiple sisters in Kalalah."
        );
    }

    public static DalilItem getSiblingsDalil(boolean isFullSister, boolean isMultiple) {
        if (isFullSister) {
            return isMultiple ? getFullSistersMultipleDalil() : getFullSisterSingleDalil();
        } else {
            return getUterineBrotherDalil(isMultiple);
        }
    }

    public static DalilItem getFullSiblingsResidueDalil() {
        return new DalilItem(
                "وَإِن كَانُوا إِخْوَةً رِّجَالًا وَنِسَاءً فَلِلذَّكَرِ مِثْلُ حَظِّ الْأُنثَيَيْنِ",
                "ওয়া ইং কানু ইখওয়াতার রিজালাওঁ ওয়া নিসা-আন ফালিজ-জাকারি মিছলু হাদ্ধিল উনছায়াইন",
                "Wa in kanoo ikhwatan rijalan wa nisa'an falidh-dhakari mithlu hazzil-unthayayn",
                "আর যদি ভাই ও বোন উভয়ই থাকে, তবে এক পুরুষের অংশ দুই নারীর অংশের সমান হবে (২:১)।",
                "If there are both brothers and sisters, the male will have the share of two females (2:1).",
                "সূরা আন-নিসা: ৪:১৭৬",
                "Surah An-Nisa: 4:176",
                "কালালাহ অবস্থায় ভাই-বোন একত্রে থাকলে তারা আসাবা বিল-গাইর হয়ে ২:১ অনুপাতে অবশিষ্ট অংশ পাবে।",
                "Full brothers and sisters inherit residue as Asabah bil-ghayr with 2:1 ratio.",
                "সূরা নিসার শেষ আয়াত ও সুন্নি ফিকহ শাস্ত্রের মূল ভিত্তি।",
                "Foundational rule from final verse of Surah An-Nisa."
        );
    }

    public static DalilItem getUterineBrotherDalil(boolean multiple) {
        return new DalilItem(
                "وَإِن كَانَ رَجُلٌ يُورَثُ كَلَالَةً أَوِ امْرَأَةٌ وَلَهُ أَخٌ أَوْ أُخْتٌ فَلِكُلِّ وَاحِدٍ مِّنْهُمَا السُّدُسُ ۚ فَإِن كَانُوا أَكْثَرَ مِن ذَٰلِكَ فَهُمْ شُرَكَاءُ فِي الثُّلُثِ",
                "ওয়া ইং কানা রাজুলুই ইউরাছু কালালাতান... ফাহুম শুরাকা-উ ফিছ ছুলুছ",
                "Wa in kana rajulun yoorathu kalalatan awimra'atun wa lahoo akhun aw ukhtun falikulli wahidim-minhuma as-sudus, fa in kanoo akthara min dhalika fahum shuraka'u fith-thuluth",
                "যদি কোনো পুরুষ বা নারীর কালালাহ সূত্রে উত্তরাধিকার হয় এবং তার বৈপিত্রীয় এক ভাই বা এক বোন থাকে, তবে প্রত্যেকের জন্য ১/৬; আর যদি তারা ততোধিক হয়, তবে তারা সকলে মিলে ১/৩ অংশে অংশীদার হবে (নারী-পুরুষ সমান ১:১)।",
                "If a man or woman leaves neither ascendants nor descendants, but has a brother or sister (from mother's side), for each is 1/6; if more, they share 1/3 (1:1 equal ratio).",
                "সূরা আন-নিসা: ৪:১২",
                "Surah An-Nisa: 4:12",
                "বৈপিত্রীয় (মায়ের দিক) ভাই-বোনদের ক্ষেত্রে নারী ও পুরুষের হিস্যা সম্পূর্ণ সমান (১:১) এবং একত্রে ১/৩ অংশ পায়।",
                "Uterine siblings (mother's side) inherit equal 1:1 shares, sharing 1/3 collectively.",
                "কোরআন মাজিদের অনন্য বিধান যেখানে নারী ও পুরুষের হিস্যা সমান।",
                "Direct Quranic injunction of 1:1 equal share among uterine siblings."
        );
    }

    public static DalilItem getUterineSisterDalil(boolean multiple) {
        return getUterineBrotherDalil(multiple);
    }
}