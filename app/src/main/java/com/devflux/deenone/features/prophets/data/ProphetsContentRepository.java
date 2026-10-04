package com.devflux.deenone.features.prophets.data;

import com.devflux.deenone.features.prophets.model.ProphetMenuItem;
import com.devflux.deenone.features.prophets.model.ProphetOverviewTopicItem;
import com.devflux.deenone.features.prophets.model.ProphetStoryItem;

import java.util.ArrayList;
import java.util.List;

public class ProphetsContentRepository {

    private static List<ProphetOverviewTopicItem> cachedOverviewList;
    private static List<ProphetStoryItem> cachedProphetsList;
    private static List<ProphetMenuItem> cachedMenuItems;

    public static synchronized List<ProphetOverviewTopicItem> getOverviewTopics() {
        if (cachedOverviewList != null) return cachedOverviewList;

        List<ProphetOverviewTopicItem> list = new ArrayList<>();

        // 1. প্রাথমিক ধারণা (Verbatim from Screenshot 2)
        String contentBn = "ইসলামের বিশ্বাস অনুযায়ী, আল্লাহ পৃথিবীতে বিভিন্ন সময়ে নবী ও রাসূল প্রেরণ করেছেন, যারা আল্লাহর বার্তা মানুষের কাছে পৌঁছে দিয়েছেন। কুরআনে ২৫ জন নবীর নাম উল্লেখ করা হয়েছে। তবে হাদিস অনুযায়ী, আল্লাহ ১,২৪,০০০ নবী প্রেরণ করেছেন, যার মধ্যে ৩১৩ জন রাসূল ছিলেন।\n\n"
                + "<b>কুরআনে উল্লেখিত নবীদের নাম:</b>\n"
                + "১. আদম (আঃ)\n"
                + "২. ইদরিস (আঃ)\n"
                + "৩. নূহ (আঃ)\n"
                + "৪. হুদ (আঃ)\n"
                + "৫. সালেহ (আঃ)\n"
                + "৬. ইব্রাহিম (আঃ)\n"
                + "৭. লুত (আঃ)\n"
                + "৮. ইসমাইল (আঃ)\n"
                + "৯. ইসহাক (আঃ)\n"
                + "১০. ইয়াকুব (আঃ)\n"
                + "১১. ইউসুফ (আঃ)\n"
                + "১২. শুয়াইব (আঃ)\n"
                + "১৩. আয়ুব (আঃ)\n"
                + "১৪. জুলকিফল (আঃ)\n"
                + "১৫. মূসা (আঃ)\n"
                + "১৬. হারুন (আঃ)\n"
                + "১৭. দাউদ (আঃ)\n"
                + "১৮. সুলায়মান (আঃ)\n"
                + "১৯. ইলিয়াস (আঃ)\n"
                + "২০. আল ইয়াসা (আঃ)\n"
                + "২১. ইউনুস (আঃ)\n"
                + "২২. জাকারিয়া (আঃ)\n"
                + "২৩. ইয়াহইয়া (আঃ)\n"
                + "২৪. ঈসা (আঃ)\n"
                + "২৫. মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) - শেষ নবী।\n\n"
                + "<b>নবীদের মর্যাদা:</b>\n\n"
                + "<b>নবী ও রাসূলের মধ্যে পার্থক্য:</b>\n"
                + "• <b>নবী:</b> নবীরা আল্লাহর বার্তা গ্রহণ করেন এবং নিজেদের সম্প্রদায়কে শিক্ষা দেন।\n"
                + "• <b>রাসূল:</b> রাসূলরা নবী হওয়ার পাশাপাশি নতুন শরিয়াহ ও কিতাব নিয়ে আসেন।\n\n"
                + "<b>নবীদের সম্মান ও দায়িত্ব:</b>\n"
                + "নবীদের জীবনে ছিল আল্লাহর নির্দেশ অনুযায়ী মানুষকে হেদায়েত করা। তারা সর্বোচ্চ নৈতিকতা, ধৈর্য এবং ন্যায়পরায়ণতার উদাহরণ ছিলেন।\n\n"
                + "<b>কুরআনের বক্তব্য:</b>\n"
                + "وَلَقَدْ بَعَثْنَا فِى كُلِّ أُمَّةٍۢ رَّسُولًا أَنِ ٱعْبُدُوا۟ ٱللَّهَ وَٱجْتَنِبُوا۟ ٱلطَّـٰغُوتَ\n\n"
                + "<b>অর্থ:</b>\n"
                + "আর নিশ্চয়ই আমি প্রত্যেক উম্মতের জন্য রাসূল প্রেরণ করেছি এই বার্তা নিয়ে যে, তোমরা আল্লাহর ইবাদত করো এবং তাগুত (অন্যায় উপাস্য) থেকে বেঁচে থাকো।\n"
                + "<b>[সূরা আন-নাহল: ৩৬]</b>\n\n"
                + "<b>আদম (আঃ):</b>\n"
                + "প্রথম মানব এবং নবী। জান্নাত থেকে পৃথিবীতে অবতরণ এবং তাওবা।\n"
                + "<b>[সূরা আল-বাকারাহ: ৩৭]</b>\n\n"
                + "<b>নূহ (আঃ):</b>\n"
                + "দীর্ঘ সময় ধরে দাওয়াত, নৌকা তৈরি, এবং প্লাবন।\n"
                + "<b>[সূরা হুদ: ২৫-৪৮]</b>\n\n"
                + "<b>ইব্রাহিম (আঃ):</b>\n"
                + "তাওহীদের প্রচার, নমরুদের আগুনে নিক্ষেপ, এবং কাবা নির্মাণ।\n"
                + "<b>[সূরা আল-হজ: ২৬-২৭]</b>\n\n"
                + "<b>মূসা (আঃ):</b>\n"
                + "ফেরাউনের বিরুদ্ধে সংগ্রাম এবং বনি ইসরাইলকে মুক্তি।\n"
                + "<b>[সূরা ত্বহা: ৯-৯৮]</b>\n\n"
                + "<b>ঈসা (আঃ):</b>\n"
                + "অলৌকিক জন্ম, রোগ নিরাময়, এবং ইনজিল প্রদান।\n"
                + "<b>[সূরা আল-মায়িদা: ১১০]</b>\n\n"
                + "<b>মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম):</b>\n"
                + "ইসলাম প্রতিষ্ঠা এবং কুরআন অবতরণ।\n"
                + "<b>[সূরা আল-আহযাব: ২১]</b>\n\n"
                + "<b>নবীদের দায়িত্ব:</b>\n"
                + "• <b>তাওহীদ প্রচার:</b> এক আল্লাহর ইবাদত এবং শিরক বর্জনের দাওয়াত।\n"
                + "• <b>শরিয়াহ প্রয়োগ:</b> নিজেদের উম্মতের জন্য আল্লাহর আইন প্রচলন।\n"
                + "• <b>উদাহরণ স্থাপন:</b> নৈতিকতা, ধৈর্য, এবং দায়িত্বশীলতার আদর্শ প্রদর্শন।\n"
                + "• <b>পরীক্ষার সম্মুখীন হওয়া:</b> নবীদের জীবনে নানা পরীক্ষা আসে, যা তাদের ধৈর্য এবং আল্লাহর প্রতি বিশ্বাসের প্রমাণ।";

        String contentEn = "According to Islamic belief, Allah sent Prophets and Messengers to earth at various times to deliver His divine message to mankind. In the Holy Quran, 25 Prophets are mentioned by name. However, according to Hadith, Allah sent 124,000 Prophets, among whom 313 were Messengers.\n\n"
                + "<b>Names of Prophets Mentioned in the Quran:</b>\n"
                + "1. Adam (AS)\n"
                + "2. Idris (AS)\n"
                + "3. Nuh (AS)\n"
                + "4. Hud (AS)\n"
                + "5. Saleh (AS)\n"
                + "6. Ibrahim (AS)\n"
                + "7. Lut (AS)\n"
                + "8. Ismail (AS)\n"
                + "9. Ishaq (AS)\n"
                + "10. Yaqub (AS)\n"
                + "11. Yusuf (AS)\n"
                + "12. Shuaib (AS)\n"
                + "13. Ayyub (AS)\n"
                + "14. Dhul-Kifl (AS)\n"
                + "15. Musa (AS)\n"
                + "16. Harun (AS)\n"
                + "17. Dawud (AS)\n"
                + "18. Sulaiman (AS)\n"
                + "19. Ilyas (AS)\n"
                + "20. Al-Yasa (AS)\n"
                + "21. Yunus (AS)\n"
                + "22. Zakariya (AS)\n"
                + "23. Yahya (AS)\n"
                + "24. Isa (AS)\n"
                + "25. Muhammad (Peace and Blessings of Allah be upon him) - The Final Messenger.\n\n"
                + "<b>Status of the Prophets:</b>\n\n"
                + "<b>Difference between Prophet (Nabi) and Messenger (Rasul):</b>\n"
                + "• <b>Prophet (Nabi):</b> Receives divine message from Allah and guides his community.\n"
                + "• <b>Messenger (Rasul):</b> In addition to being a prophet, brings a new divine scripture and legal code (Shariah).\n\n"
                + "<b>Honor and Responsibilities of Prophets:</b>\n"
                + "The central mission of prophets was to guide humanity according to Allah's commandments. They were the highest exemplars of moral excellence, sublime patience, and justice.\n\n"
                + "<b>Quranic Declaration:</b>\n"
                + "وَلَقَدْ بَعَثْنَا فِى كُلِّ أُمَّةٍۢ رَّسُولًا أَنِ ٱعْبُدُوا۟ ٱللَّهَ وَٱجْتَنِبُوا۟ ٱلطَّـٰغُوتَ\n\n"
                + "<b>Translation:</b>\n"
                + "And We certainly sent into every nation a messenger saying, 'Worship Allah and avoid Taghut (false deities).'\n"
                + "<b>[Surah An-Nahl: 36]</b>\n\n"
                + "<b>Adam (AS):</b>\n"
                + "The first human and prophet. Descent from Paradise and sincere repentance.\n"
                + "<b>[Surah Al-Baqarah: 37]</b>\n\n"
                + "<b>Nuh (AS):</b>\n"
                + "Centuries of preaching, building the Ark, and the Great Deluge.\n"
                + "<b>[Surah Hud: 25-48]</b>\n\n"
                + "<b>Ibrahim (AS):</b>\n"
                + "Champion of monotheism, cast into Nimrod's fire, and reconstruction of the Ka'bah.\n"
                + "<b>[Surah Al-Hajj: 26-27]</b>\n\n"
                + "<b>Musa (AS):</b>\n"
                + "Confrontation against Pharaoh and liberation of the Children of Israel.\n"
                + "<b>[Surah Ta-Ha: 9-98]</b>\n\n"
                + "<b>Isa (AS):</b>\n"
                + "Miraculous birth, healing the sick, and bestowal of the Gospel (Injeel).\n"
                + "<b>[Surah Al-Ma'idah: 110]</b>\n\n"
                + "<b>Muhammad (Peace and Blessings be upon him):</b>\n"
                + "Establishment of Islam and revelation of the Holy Quran.\n"
                + "<b>[Surah Al-Ahzab: 21]</b>\n\n"
                + "<b>Core Duties of Prophets:</b>\n"
                + "• <b>Preaching Tawheed:</b> Calling humanity to the worship of One Allah and eradication of polytheism.\n"
                + "• <b>Implementing Shariah:</b> Establishing divine laws and justice.\n"
                + "• <b>Setting Living Examples:</b> Demonstrating supreme morality, patience, and devotion.\n"
                + "• <b>Facing Trials:</b> Enduring severe tribulations with steadfast faith in Allah.";

        list.add(new ProphetOverviewTopicItem(1, "প্রাথমিক ধারণা", "Primary Concept", contentBn, contentEn));

        cachedOverviewList = list;
        return list;
    }

    public static synchronized List<ProphetStoryItem> getProphetsList() {
        if (cachedProphetsList != null) return cachedProphetsList;

        List<ProphetStoryItem> list = new ArrayList<>();

        // 1. Adam (AS)
        list.add(new ProphetStoryItem(
                1,
                "آدَمُ عَلَيْهِ السَّلَامُ",
                "হযরত আদম (আ:)",
                "Prophet Adam (AS)",
                "মানবজাতির আদি পিতা ও প্রথম নবী",
                "First Human & The First Prophet",
                "আদি যুগ",
                25,
                "সূরা আল-বাকারা: ৩০-৩৭, সূরা আল-আ'রাফ: ১১-২৫, সূরা ত্বা-হা: ১১৫-১২৩",
                "আল্লাহ তাআলা সমস্ত মানবজাতির আদি পিতা হিসেবে মাটি থেকে আদম (আ:)-কে সৃষ্টি করেন এবং ফেরেশতাগণকে সিজদা করার আদেশ দেন।",
                "Allah created Adam (AS) from clay as the first human being and the father of all mankind, commanding the angels to prostrate to him.",
                "আল্লাহ সুবহানাহু ওয়া তাআলা দুনিয়াতে তাঁর খলিফা প্রেরণের ঘোষণা দিয়ে আদম (আ:)-কে সৃষ্টি করেন এবং তাঁকে সমস্ত বস্তুর নাম শিক্ষা দেন। পরবর্তীতে ইবলিসের প্ররোচনায় নিষিদ্ধ গাছের ফল খাওয়ার পর আদম ও হাওয়া (আ:) অনুতপ্ত হয়ে মহান আল্লাহর কাছে তাওবা করেন। আল্লাহ তাঁদের তাওবা কবুল করেন এবং হেদায়াতের পথনির্দেশসহ তাঁদের পৃথিবীতে প্রেরণ করেন।",
                "Allah declared His intention to place a trustee on earth and fashioned Adam from dust, teaching him all the names. Following the deception of Iblis regarding the forbidden tree, Adam and Hawwa turned to Allah in sincere repentance. Allah accepted their repentance and sent them to earth with divine guidance.",
                "رَبَّنَا ظَلَمْنَا أَنفُسَنَا وَإِن لَّمْ تَغْفِرْ لَنَا وَتَرْحَمْنَا لَنَكُونَنَّ مِنَ الْخَاسِرِينَ",
                "উচ্চারণ: রব্বানা জোয়ালামনা আনফুসানা ওয়া ইল্লাম তাগফির লানা ওয়া তারহামনা লানাকূনান্না মিনাল খাসিরীন।",
                "অর্থ: হে আমাদের প্রতিপালক! আমরা নিজেদের ওপর অন্যায় করেছি। যদি আপনি আমাদের ক্ষমা না করেন এবং দয়া না করেন, তবে অবশ্যই আমরা ক্ষতিগ্রস্তদের অন্তর্ভুক্ত হব। (সূরা আল-আ'রাফ: ২৩)",
                "• গুনাহ হলে অবিলম্বে বিনয়ের সাথে তাওবা করা।\n• অহংকার পতনের মূল কারণ (যা ইবলিসের হয়েছিল)।\n• সকল জ্ঞানের মালিক মহান আল্লাহ।",
                "• Turn to Allah in immediate, sincere repentance after sinning.\n• Arrogance is the path to destruction.\n• True knowledge is bestowed solely by Allah."
        ));

        // 2. Idris (AS)
        list.add(new ProphetStoryItem(
                2,
                "إِدْرِيسُ عَلَيْهِ السَّلَامُ",
                "হযরত ইদরীস (আ:)",
                "Prophet Idris (AS)",
                "সত্যবাদী ও সমুচ্চ মর্যাদাসম্পন্ন নবী",
                "The Truthful & Exalted Prophet",
                "আদি যুগ",
                2,
                "সূরা মারইয়াম: ৫৬-৫৭, সূরা আল-আম্বিয়া: ৮৫-৮৬",
                "হযরত ইদরীস (আ:) ছিলেন চরম ধৈর্যশীল ও সত্যনিষ্ঠ নবী, যাঁকে আল্লাহ তাআলা সুউচ্চ মর্যাদায় উন্নীত করেছিলেন।",
                "Prophet Idris (AS) was an exceptionally patient and truthful prophet whom Allah elevated to a lofty station.",
                "তিনি কলম দ্বারা লিখনপদ্ধতি ও বস্ত্র সেলাই শিল্পের প্রথম প্রবর্তক ছিলেন বলে তাফসির ও ইতিহাসে বর্ণিত হয়েছে। তিনি তাঁর জাতিকে আল্লাহর একত্ববাদ এবং সৎকর্মের নির্দেশ দিতেন এবং পাপের ভয়াবহ পরিণতি থেকে সতর্ক করতেন। পবিত্র কুরআনে তাঁর সত্যবাদিতা ও সুউচ্চ মর্যাদার প্রশংসা করা হয়েছে।",
                "Historically recognized as one of the pioneers of writing with the pen and tailoring garments, Idris (AS) devoted his life to calling his community to monotheism and moral righteousness. The Holy Quran specifically honors him as a man of truth and elevated status.",
                "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَّبِيًّا • وَرَفَعْنَاهُ مَكَانًا عَلِيًّا",
                "উচ্চারণ: ওয়াযকুর ফিল কিতা-বি ইদরীস, ইন্নাহু কা-না সিদ্দীক্বান নাবিয়্যা। ওয়া রফা'না-হু মাকা-নান আলিয়্যা।",
                "অর্থ: এই কিতাবে ইদরীসের কথা স্মরণ করুন, তিনি ছিলেন একজন সত্যনিষ্ঠ নবী। এবং আমি তাঁকে উন্নীত করেছিলাম এক সমুচ্চ মর্যাদায়। (সূরা মারইয়াম: ৫৬-৫৭)",
                "• সততা ও তাকওয়া মানুষকে আল্লাহর দরবারে সুউচ্চ মর্যাদা দান করে।\n• দুনিয়াবি শিল্প ও জ্ঞানকে দ্বীনের কল্যাণে ব্যবহার করা।",
                "• Truthfulness and piety elevate a person in the sight of Allah.\n• Cultivating useful worldly knowledge alongside spiritual devotion."
        ));

        // 3. Nuh (AS)
        list.add(new ProphetStoryItem(
                3,
                "نُوحٌ عَلَيْهِ السَّلَامُ",
                "হযরত নূহ (আ:)",
                "Prophet Nuh (AS)",
                "ধৈর্যের প্রতীক ও মহা প্লাবনের কিস্তির কাণ্ডারী",
                "Ulu-l-Azm Messenger & Builder of the Ark",
                "উলুল আযম",
                43,
                "সূরা নূহ, সূরা হূদ: ২৫-৪৮, সূরা আল-আম্বিয়া: ৭৬-৭৭, সূরা আল-মু'মিনূন: ২৩-৩০",
                "দীর্ঘ ৯৫০ বছর ধরে অসীম ধৈর্য নিয়ে তাওহীদের দাওয়াত প্রদানকারী প্রথম উলুল আযম রাসুল।",
                "One of the greatest messengers who patiently preached monotheism day and night for 950 years.",
                "নূহ (আ:) পৌত্তলিকতার বিরুদ্ধে তাঁর সম্প্রদায়কে রাত-দিন দাওয়াত দেন। কিন্তু গুটিকয়েক দরিদ্র ব্যক্তি ব্যতীত বেশিরভাগ প্রতাপশালী ব্যক্তি তাঁর সাথে চরম অবাধ্যতা ও উপহাস করে। অবশেষে আল্লাহর নির্দেশে তিনি এক বিশাল নৌকা (কিস্তি) নির্মাণ করেন এবং মহাপ্লাবনে অবাধ্য জাতি ধ্বংস হয়ে যায় ও ঈমানদাররা রক্ষা পান।",
                "Nuh (AS) tirelessly preached monotheism against idolatry for centuries with immense patience. When his people persisted in stubborn defiance and mockery, Allah commanded him to build the Ark. A great deluge engulfed the disbelievers while the believers were saved.",
                "رَّبِّ اغْفِرْ لِي وَلِوَالِدَيَّ وَلِمَن دَخَلَ بَيْتِيَ مُؤْمِنًا وَلِلْمُؤْمِنِينَ وَالْمُؤْمِنَاتِ",
                "উচ্চারণ: রব্বিগফির লী ওয়ালিওয়া-লিদাইয়্যা ওয়া লিমান দাখালা বাইতিয়া মু'মিনান ওয়ালিল মু'মিনীনা ওয়াল মু'মিনা-ত।",
                "অর্থ: হে আমার পালনকর্তা! আপনি আমাকে, আমার পিতা-মাতাকে, যিনি ঈমানদার হয়ে আমার গৃহে প্রবেশ করেছে তাঁকে এবং সমস্ত মুমিন পুরুষ ও মুমিন নারীকে ক্ষমা করে দিন। (সূরা নূহ: ২৮)",
                "• দ্বীনের পথে কখনো ধৈর্যহারা না হওয়া।\n• বংশমর্যাদা নয়, ঈমান ও আমলই মুক্তির একমাত্র চাবিকাঠি।\n• পিতা-মাতা ও সমস্ত মুমিনের মাগফিরাতের জন্য নিয়মিত দোয়া করা।",
                "• Unwavering persistence and patience in the service of Allah.\n• Salvation depends on faith and righteous deeds, not lineage.\n• Continuous supplication for parents and the entire community of believers."
        ));

        // 4. Hud (AS)
        list.add(new ProphetStoryItem(
                4,
                "هُودٌ عَلَيْهِ السَّلَامُ",
                "হযরত হুদ (আ:)",
                "Prophet Hud (AS)",
                "অহংকারী 'আদ' জাতির প্রতি প্রেরিত রাসুল",
                "Prophet Sent to the Mighty People of 'Ad",
                "আদি যুগ",
                7,
                "সূরা হূদ: ৫০-৬০, সূরা আল-আ'রাফ: ৬৫-৭২, সূরা আল-আহকাফ: ২১-২৬, সূরা আশ-শু'আরা: ১২৩-১৪০",
                "বিশাল শারীরিক শক্তি ও সুরম্য অট্টালিকার অধিকারী অহংকারী আদ জাতিকে তাওহীদের দাওয়াত দেন।",
                "Sent to the affluent and physically towering civilization of 'Ad who were blinded by arrogance.",
                "আদ জাতি বিশাল প্রাসাদ ও শক্তিমত্তায় গর্বিত হয়ে বলেছিল: 'আমাদের চেয়ে শক্তিশালী কে আছে?' হূদ (আ:) তাদের বিনয়ের সাথে এক আল্লাহর ইবাদত করার আহ্বান জানান। তারা চরম অস্বীকার করায় প্রচণ্ড বিধ্বংসী ঘূর্ণিবায়ু দ্বারা সম্পূর্ণ সম্প্রদায়কে ধ্বংস করে দেওয়া হয়।",
                "The tribe of 'Ad boasted about their supreme strength and built grand monuments. Hud (AS) called them to humility and worship of the One True Creator. Rejecting his plea, they were annihilated by a fierce, howling windstorm lasting seven nights and eight days.",
                "إِنِّي تَوَكَّلْتُ عَلَى اللَّهِ رَبِّي وَرَبِّكُم ۚ مَّا مِن دَابَّةٍ إِلَّا هُوَ آخِذٌ بِنَاصِيَتِهَا",
                "উচ্চারণ: ইন্নী তাওয়াক্কালতু আলাল্লা-হি রব্বী ওয়া রব্বিকুম, মা- মিন দা-ব্বাতিন ইল্লা- হুওয়া আ-খিযুম বিনা-সিয়াতিহা।",
                "অর্থ: নিশ্চয় আমি ভরসা করেছি আল্লাহর ওপর যিনি আমার প্রতিপালক এবং তোমাদের প্রতিপালক। এমন কোনো জীব নেই যার কেশগুচ্ছ তাঁর নিয়ন্ত্রণে নেই। (সূরা হূদ: ৫৬)",
                "• শারীরিক শক্তি, ধনসম্পদ ও প্রযুক্তিগত উৎকর্ষ কাউকে আল্লাহর আযাব থেকে বাঁচাতে পারে না।\n• প্রতিটি পদক্ষেপে এক আল্লাহর ওপর পূর্ণ তাওয়াক্কুল (ভরসা) রাখা।",
                "• Material wealth, military might, and monuments cannot shield against divine justice.\n• Complete reliance (Tawakkul) upon Allah alone."
        ));

        // 5. Saleh (AS)
        list.add(new ProphetStoryItem(
                5,
                "صَالِحٌ عَلَيْهِ السَّلَامُ",
                "হযরত ছালেহ(আ:)",
                "Prophet Saleh (AS)",
                "সামূদ জাতির নবী ও অলৌকিক উটনীর মুজিযা",
                "Prophet Sent to Thamud with the Miraculous She-Camel",
                "আদি যুগ",
                9,
                "সূরা আল-আ'রাফ: ৭৩-৭৯, সূরা হূদ: ৬১-৬৮, সূরা আল-হিজর: ৮০-৮৪, সূরা আশ-শু'আরা: ১৪১-১৫৯",
                "পাহাড় কেটে সুরম্য বাড়ি তৈরিতে পারদর্শী সামূদ জাতির কাছে প্রেরিত নবী।",
                "Sent to the advanced stonemasons of Thamud who carved magnificent homes directly into mountains.",
                "সামূদ জাতি সালেহ (আ:)-এর কাছে কঠিন পাথর থেকে একটি গর্ভবতী উটনী বের করে দেখানোর দাবি করে। আল্লাহর কুদরতে অলৌকিক উটনী প্রকাশিত হলে সালেহ (আ:) তাকে কোনো ক্ষতি না করার নির্দেশ দেন। কিন্তু অবাধ্যরা উটনীটিকে হত্যা করে। ফলে বিকট বজ্রনাদে সমগ্র জাতি ধ্বংসপ্রাপ্ত হয়।",
                "Thamud demanded an extraordinary miracle: a live, pregnant she-camel emerging from a solid boulder. By Allah's decree, the miracle appeared. Saleh warned them not to harm her, but their transgressors slaughtered the camel, bringing upon themselves a catastrophic blast.",
                "قَالَ يَا قَوْمِ اعْبُدُوا اللَّهَ مَا لَكُم مِّنْ إِلَٰهٍ غَيْرُهُ ۖ هُوَ أَنشَأَكُم مِّنَ الْأَرْضِ وَاسْتَعْمَرَكُمْ فِيهَا فَاسْتَغْفِرُوهُ ثُمَّ تُوبُوا إِلَيْهِ",
                "উচ্চারণ: ক্বা-লা ইয়া ক্বাওমি'বুদুল্লা-হা মা- লাকুম মিন ইলা-হিন গায়রুহু, হুওয়া আনশা'আকুম মিনাল আরদি ওয়াস তা'মারাকুম ফীহা- ফাস তাগফিরূহু ছুম্মা তূবূ ইলাইহি।",
                "অর্থ: তিনি বললেন: হে আমার কওম! তোমরা আল্লাহর ইবাদত করো, তিনি ছাড়া তোমাদের অন্য কোনো সত্য ইলাহ নেই। তিনি তোমাদের মাটি থেকে সৃষ্টি করেছেন এবং সেখানে তোমাদের আবাদ করেছেন। অতএব তাঁর কাছে ক্ষমা প্রার্থনা করো এবং তাঁর দিকেই ফিরে এসো। (সূরা হূদ: ৬১)",
                "• আল্লাহর নিদর্শনসমূহের অমর্যাদা করা কঠিন শাস্তির কারণ।\n• সত্যের বিরোধিতায় সংখ্যাগরিষ্ঠ হওয়া ধ্বংসের কারণ হয়।",
                "• Disrespecting divine signs invites swift retribution.\n• Righteousness stands firm even when surrounded by a majority of transgressors."
        ));

        // 6. Ibrahim (AS)
        list.add(new ProphetStoryItem(
                6,
                "إِبْرَاهِيمُ عَلَيْهِ السَّلَامُ",
                "হযরত ইবরাহীম (আ:)",
                "Prophet Ibrahim (AS)",
                "খলিলুল্লাহ, মুসলিম জাতির পিতা ও তাওহীদের অবিচল কাণ্ডারী",
                "Friend of Allah & Patriarch of Monotheism",
                "উলুল আযম",
                69,
                "সূরা আল-বাকারা: ১২৪-১৩২, সূরা আল-আন'আম: ৭৪-৮৩, সূরা ইবরাহীম, সূরা আল-আম্বিয়া: ৫১-৭০",
                "একক তাওহীদের নির্ভীক প্রচারক, অগ্নিকুণ্ডে নিক্ষিপ্ত হয়েও যিনি আল্লাহর সন্তুষ্টিতে অটল ছিলেন এবং কাবা পুনর্নির্মাণ করেন।",
                "The intimate friend of Allah (Khalilullah) who demonstrated absolute surrender in the fire of Nimrod and the sacrifice of his son.",
                "নমরূদের মূর্তিপূজারী সমাজকে যুক্তির মাধ্যমে মিথ্যা প্রমাণ করেন এবং অগ্নিকুণ্ডে নিক্ষিপ্ত হলে আল্লাহ আগুনকে শীতল ও শান্তিময় করে দেন। বৃদ্ধ বয়সে পুত্র ইসমাঈলকে কুরবানী করার কঠিন পরীক্ষায় উত্তীর্ণ হন। পিতা-পুত্র মিলে পবিত্র কা'বা ঘর পুনর্নির্মাণ করেন এবং হজের ডাক দেন।",
                "Ibrahim (AS) confronted King Nimrod and demolished the idols using sublime logic. Cast into a roaring furnace, Allah rendered the fire cool and safe for him. He endured the supreme test of sacrificing his beloved son Ismail, and together they erected the foundations of the Holy Ka'bah.",
                "رَبِّ اجْعَلْنِي مُقِيمَ الصَّلَاةِ وَمِن ذُرِّيَّتِي ۚ رَبَّنَا وَتَقَبَّلْ دُعَاءِ",
                "উচ্চারণ: রব্বিজ'আলনী মুক্বীমাস সালা-তি ওয়া মিন যুররিইয়্যাতী, রব্বানা- ওয়া তাক্বাব্বাল দু'আ-।",
                "অর্থ: হে আমার প্রতিপালক! আমাকে সালাত কায়েমকারী করুন এবং আমার বংশধরদের মধ্য থেকেও। হে আমাদের প্রতিপালক! আমার দোয়া কবুল করুন। (সূরা ইবরাহীম: ৪০)",
                "• আল্লাহর নির্দেশের সামনে নিজের প্রিয়তম বস্তুকে কুরবানী করার মানসিকতা।\n• সন্তান ও ভবিষ্যৎ বংশধরদের জন্য নিয়মিত সালাতের দোয়া করা।\n• আগুনে নিক্ষিপ্ত হলেও আল্লাহর ওপর একনিষ্ঠ আস্থা রাখা।",
                "• Total readiness to sacrifice one's most cherished possessions for Allah's pleasure.\n• Earnest prayer for descendants to uphold established prayers.\n• Pure monotheism that remains unflinching even facing fire."
        ));

        // 7. Lut (AS)
        list.add(new ProphetStoryItem(
                7,
                "لُوطٌ عَلَيْهِ السَّلَامُ",
                "হযরত লূত (আ:)",
                "Prophet Lut (AS)",
                "সাদূম ও গোমোরাহ নগরীর নবী",
                "Prophet Sent to Sodom & Gomorrah",
                "আদি যুগ",
                27,
                "সূরা আল-আ'রাফ: ৮০-৮৪, সূরা হূদ: ৭৭-৮৩, সূরা আল-হিজর: ৫৯-৭৭, সূরা আশ-শু'আরা: ১৬০-১৭৫",
                "সমকামিতা ও চরম অশ্লীলতায় নিমজ্জিত সাদূমবাসীর হেদায়েতের জন্য প্রেরিত নবী।",
                "Sent to the corrupted inhabitants of Sodom to warn them against unprecedented moral perversion.",
                "ইবরাহীম (আ:)-এর ভ্রাতুষ্পুত্র হযরত লূত (আ:) সাদূমবাসীকে অশ্লীল পাপ বর্জন করে পবিত্র জীবনযাপনের আহ্বান জানান। তারা উল্টো তাঁকে নগরী থেকে বের করে দেওয়ার হুমকি দেয়। অবশেষে আল্লাহর ফেরেশতাগণ এসে লূত (আ:) ও তাঁর ঈমানদার অনুসারীদের নিরাপদে সরিয়ে নেন এবং অবাধ্যদের ওপর উল্টে পড়া পাথরের বৃষ্টি বর্ষণ করে চিরতরে ধ্বংস করা হয়।",
                "Lut (AS), the nephew of Ibrahim (AS), warned the people of Sodom against unnatural sexual obscenities and highway robbery. Mocking his purity, they threatened banishment. Angels arrived in human form to safeguard Lut and the believers before overturning the city and showering it with baked stones.",
                "رَبِّ نَجِّنِي وَأَهْلِي مِمَّا يَعْمَلُونَ",
                "উচ্চারণ: রব্বি নাজ্জিনী ওয়া আহলী মিম্মা- ইয়া'মালূন।",
                "অর্থ: হে আমার পালনকর্তা! আমাকে এবং আমাদের পরিবারবর্গকে তারা যা করে তা থেকে রক্ষা করুন। (সূরা আশ-শু'আরা: ১৬৯)",
                "• সর্বপ্রকার অশ্লীলতা ও বিকৃত যৌনতা থেকে সমাজ ও পরিবারকে রক্ষা করা।\n• পাপী সমাজের ওপর আযাব এলে আল্লাহর আশ্রয়ে থাকা।",
                "• Uncompromising opposition to societal degeneracy and moral decadence.\n• Seeking Allah's protection when surrounded by pervasive evil."
        ));

        // 8. Ismail (AS)
        list.add(new ProphetStoryItem(
                8,
                "إِسْمَاعِيلُ عَلَيْهِ السَّلَامُ",
                "হযরত ইসমাঈল (আ:)",
                "Prophet Ismail (AS)",
                "যবীহুল্লাহ, প্রতিশ্রুতির সত্যবাদী ও কাবার স্থপতি",
                "The Sacrificed for Allah & Patriarch of Arabs",
                "আদি যুগ",
                12,
                "সূরা আল-বাকারা: ১২৫-১২৯, সূরা মারইয়াম: ৫৪-৫৫, সূরা আস-সাফফাত: ১০০-১১০",
                "ইবরাহীম (আ:)-এর জ্যেষ্ঠ পুত্র, যাঁর পায়ের নিচে যমযম কূপ সৃষ্টি হয় এবং কুরবানীর পরীক্ষায় আত্মনিবেদন করেন।",
                "Eldest son of Ibrahim (AS) whose heel opened the Zamzam well and who willingly surrendered to divine sacrifice.",
                "দুগ্ধপোষ্য বয়সে মা হাজেরার সাথে নির্জন মক্কার উপত্যকায় রেখে আসা হয় এবং তাঁর চরণস্পর্শে প্রবাহিত হয় বরকতময় যমযমের ফোয়ারা। পিতা যখন স্বপ্নে কুরবানীর আদেশ পেয়ে জানালেন, তিনি বললেন: 'হে পিতা! আপনাকে যা আদেশ করা হয়েছে তা-ই করুন।' অতঃপর পিতা-পুত্র মিলে কা'বা পুনর্নির্মাণ করেন।",
                "Left in the barren valley of Makkah with his mother Hajar, the miraculous spring of Zamzam burst forth beneath his feet. When informed of his father's vision of sacrifice, the noble youth replied with total submission. Together, father and son raised the foundations of the Holy Ka'bah.",
                "رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنتَ السَّمِيعُ الْعَلِيمُ",
                "উচ্চারণ: রব্বানা- তাক্বাব্বাল মিন্না-, ইন্নাকা আনতাস সামী'উল 'আলীম।",
                "অর্থ: হে আমাদের প্রতিপালক! আমাদের পক্ষ থেকে এটি কবুল করুন। নিশ্চয় আপনি সর্বশ্রোতা, সর্বজ্ঞাতা। (সূরা আল-বাকারা: ১২৭)",
                "• পিতামাতার সৎ নির্দেশে সর্বোচ্চ আনুগত্য ও কুরবানীর প্রস্তুতি।\n• প্রতিটি সৎকাজের পর আল্লাহর কাছে তা কবুল হওয়ার দোয়া করা।",
                "• Supreme filial obedience and unwavering willingness to sacrifice in Allah's way.\n• Supplicating for divine acceptance after every righteous endeavor."
        ));

        // 9. Ishaq (AS)
        list.add(new ProphetStoryItem(
                9,
                "إِسْحَاقُ عَلَيْهِ السَّلَامُ",
                "হযরত ইসহাক্ব (আ:)",
                "Prophet Ishaq (AS)",
                "মুবারক পুত্র ও বনি ইসরাঈলের নবীগণের মূল পূর্বপুরুষ",
                "The Blessed Son & Forefather of Israelite Prophets",
                "আদি যুগ",
                17,
                "সূরা হূদ: ৬৯-৭৩, সূরা মারইয়াম: ৪৯-৫০, সূরা আস-সাফফাত: ১১২-১১৩",
                "ইবরাহীম ও সারাহ (আ:)-এর বৃদ্ধ বয়সে ফেরেশতাদের সুসংবাদপ্রাপ্ত মুবারক পুত্র।",
                "Prophet born to Ibrahim and Sarah in their advanced old age as announced by the angels.",
                "আল্লাহ সুবহানাহু ওয়া তাআলা ইবরাহীম ও সারাহকে বৃদ্ধ বয়সে ইসহাক্ব (আ:)-এর মতো একজন নেককার নবীর সুসংবাদ দান করেন। তিনি ছিলেন সৎকর্মশীল, ইবাদতগুজার ও পবিত্র চরিত্রের অধিকারী। তাঁরই বংশধর হিসেবে হযরত ইয়াক্বুব ও ইউসুফসহ বনি ইসরাঈলের অসংখ্য নবীর আগমন ঘটে।",
                "Angels conveyed glad tidings of Ishaq to Ibrahim and Sarah in their twilight years. He grew up as an exceptionally pious, worshipful, and righteous prophet whose blessed lineage gave birth to Yaqub, Yusuf, and successive generations of Israelite prophets.",
                "وَبَشَّرْنَاهُ بِإِسْحَاقَ نَبِيًّا مِّنَ الصَّالِحِينَ • وَبَارَكْنَا عَلَيْهِ وَعَلَىٰ إِسْحَاقَ",
                "উচ্চারণ: ওয়া বাশশারনা-হু বিইসহা-ক্বা নাবিয়্যাম মিনাস সা-লিহীন। ওয়া বা-রকনা- 'আলাইহি ওয়া 'আলা- ইসহা-ক্ব।",
                "অর্থ: আর আমি তাঁকে সুসংবাদ দিয়েছিলাম ইসহাক্বের, যে ছিল সৎকর্মশীলদের অন্তর্ভুক্ত একজন নবী। এবং আমি বরকত দান করেছিলাম তাঁর ওপর ও ইসহাক্বের ওপর। (সূরা আস-সাফফাত: ১১২-১১৩)",
                "• নেককার সন্তানের সুসংবাদ আল্লাহর এক মহা অনুগ্রহ।\n• পিতা-মাতার তাকওয়ার বরকত বংশধরের মধ্যেও প্রবাহিত হয়।",
                "• Righteous children are among the greatest divine blessings.\n• The piety of parents radiates through successive generations."
        ));

        // 10. Yaqub (AS)
        list.add(new ProphetStoryItem(
                10,
                "يَعْقُوبُ عَلَيْهِ السَّلَامُ",
                "হযরত ইয়াক্বুব (আ:)",
                "Prophet Yaqub (AS)",
                "ইসরাঈল, অসীম ধৈর্যের প্রতীক ও ইউসুফের পিতা",
                "Israel, Emblem of Beautiful Patience (Sabrun Jameel)",
                "বনি ইসরাঈল",
                16,
                "সূরা ইউসুফ, সূরা আল-বাকারা: ১৩২-১৩৩, সূরা আলে ইমরান: ৯৩",
                "ইসহাক্ব (আ:)-এর পুত্র, যাঁর উপাধি ছিল 'ইসরাঈল' এবং যাঁর বারোজন পুত্র থেকে বনি ইসরাঈলের ১২টি গোত্রের উৎপত্তি।",
                "Son of Ishaq (AS), known as Israel, whose twelve sons formed the twelve foundational tribes of the Israelites.",
                "প্রিয় পুত্র ইউসুফকে হারিয়ে দীর্ঘ বছর ধরে বিরহ বেদনায় অন্ধ হয়ে গেলেও তিনি কখনো আল্লাহর রহমত থেকে নিরাশ হননি। তিনি 'সবরে জামীল' (সুন্দর ধৈর্য) ধারণ করেন এবং বলেছিলেন: 'আমি আমার দুঃখ ও অস্থিরতা কেবল আল্লাহর কাছেই নিবেদন করছি।' অবশেষে পুত্র ইউসুফের সাথে মিলনে তাঁর দৃষ্টিশক্তি ফিরে পান।",
                "Yaqub (AS) endured decades of agonizing separation from his beloved son Yusuf until his eyes turned white from weeping. He exemplified 'Sabrun Jameel' (beautiful patience), seeking solace exclusively from Allah, until they were joyfully reunited in Egypt.",
                "قَالَ إِنَّمَا أَشْكُو بَثِّي وَحُزْنِي إِلَى اللَّهِ وَأَعْلَمُ مِنَ اللَّهِ مَا لَا تَعْلَمُونَ",
                "উচ্চারণ: ক্বা-লা ইন্নামা- আশকূ বাছ্ছী ওয়া হুযনী ইলাল্লা-হি ওয়া আ'লামু মিনাল্লা-হি মা- লা- তা'লামূন।",
                "অর্থ: তিনি বললেন: আমি তো আমার অসহনীয় বেদনা ও দুঃখের অভিযোগ কেবল আল্লাহর কাছেই নিবেদন করছি; আর আমি আল্লাহর পক্ষ থেকে এমন কিছু জানি যা তোমরা জানো না। (সূরা ইউসুফ: ৮৬)",
                "• চরম বিপদেও আল্লাহর রহমত থেকে কখনো নিরাশ না হওয়া।\n• মানুষের কাছে নয়, সমস্ত দুঃখ-বেদনা একমাত্র আল্লাহর কাছে পেশ করা।",
                "• Never despairing of the mercy of Allah even during decades of trial.\n• Directing all grief and sorrow exclusively to the Almighty."
        ));

        // 11. Yusuf (AS)
        list.add(new ProphetStoryItem(
                11,
                "يُوسُفُ عَلَيْهِ السَّلَامُ",
                "হযরত ইউসুফ (আ:)",
                "Prophet Yusuf (AS)",
                "আহসানুল ক্বাসাস, রূপ ও চরিত্রের অনুপম আদর্শ ও মিসরের আজিজ",
                "The Best of Stories, Model of Chastity & Minister of Egypt",
                "বনি ইসরাঈল",
                27,
                "সূরা ইউসুফ (সমগ্র সূরা)",
                "ভাইদের ষড়যন্ত্রে কূপে নিক্ষেপ, দাসত্ব ও জেলখানার কঠিন পরীক্ষা পেরিয়ে মিসরের শাসনকর্তা হওয়া নবী।",
                "Endured betrayal in a well, false imprisonment, and moral tests before ascending to become the minister of Egypt.",
                "ভাইদের ঈর্ষার শিকার হয়ে অন্ধকূপে নিক্ষিপ্ত হন, কাফেলার কাছে বিক্রি হয়ে মিসরের আজিজের ঘরে আশ্রয় পান। যুলাইখার প্রলোভন প্রত্যাখ্যান করে আল্লাহর ভয়ে জেলখানাকে বেছে নেন। পরবর্তীতে বাদশার স্বপ্নের সঠিক ব্যাখ্যা দিয়ে মিসরের খাদ্য ও অর্থভাণ্ডারের পূর্ণ দায়িত্ব লাভ করেন এবং ষড়যন্ত্রকারী ভাইদের ক্ষমা করে দেন।",
                "Betrayed by his brothers into a dark well, sold into slavery, he resisted the seductive advances of the Egyptian minister's wife out of supreme fear of Allah. After years in prison, he interpreted the king's dream and was appointed chancellor of Egypt, ultimately forgiving his repentant brothers.",
                "رَبِّ قَدْ آتَيْتَنِي مِنَ الْمُلْكِ وَعَلَّمْتَنِي مِن تَأْوِيلِ الْأَحَادِيثِ ۚ فَاطِرَ السَّمَاوَاتِ وَالْأَرْضِ أَنتَ وَلِيِّي فِي الدُّنْيَا وَالْآخِرَةِ ۖ تَوَفَّنِي مُسْلِمًا وَأَلْحِقْنِي بِالصَّالِحِينَ",
                "উচ্চারণ: রব্বি ক্বদ আ-তাইতানী মিনাল মুলকি ওয়া 'আল্লামতানী মিন তা'বীলিল আহا-দীছ, ফা-তিরাস সামা-ওয়া-তি ওয়াল আরদি আনতা ওয়ালিইয়্যী ফিদ দুনয়া- ওয়াল আ-খিরাহ, তাওয়াফ্ফানী মুসলিমওঁ ওয়া আলহিক্বনী বিস সা-লিহীন।",
                "অর্থ: হে আমার প্রতিপালক! আপনি আমাকে রাজ্য দান করেছেন এবং স্বপ্নের ব্যাখ্যা শিক্ষা দিয়েছেন। হে আসমান ও জমিনের স্রষ্টা! আপনিই দুনিয়া ও আখেরাতে আমার অভিভাবক। আমাকে মুসলিম হিসেবে মৃত্যু দান করুন এবং সৎকর্মশীলদের অন্তর্ভুক্ত করুন। (সূরা ইউসুফ: ১০১)",
                "• চোখের ও অন্তরের পবিত্রতা রক্ষা করে তাকওয়ার ওপর অবিচল থাকা।\n• ক্ষমতার শীর্ষে পৌঁছেও শত্রুকে ক্ষমা করে দেওয়ার উদারতা।\n• শেষ পরিণতি সবসময় মুত্তাকীদের অনুকূলেই আসে।",
                "• Uncompromising chastity and God-consciousness under severe temptation.\n• Magnanimous forgiveness of former oppressors when empowered.\n• Ultimate triumph belongs to the patient and righteous."
        ));

        // 12. Muhammad (SAW) - Makki Life
        list.add(new ProphetStoryItem(
                12,
                "مُحَمَّدٌ رَسُولُ اللَّهِ ﷺ - المكي",
                "হযরত মুহাম্মাদ (সাঃ) - মাক্কী জীবন",
                "Prophet Muhammad (SAW) - Makki Life",
                "নবুওয়াতের সূচনা, দাওয়াত, চরম জুলুম ও মি'রাজের ঐতিহাসিক পর্ব",
                "Prophethood, Secret & Public Dawah, Persecution & Isra-Miraj",
                "সর্বশেষ রাসুল",
                4,
                "সূরা আল-মুদ্দাসসির, সূরা আল-ইসরা, সূরা আল-আহযাব, সূরা আশ-শরহ",
                "হেরা গুহায় ওহী নাযিল থেকে শুরু করে মক্কার কুরাইশদের অকথ্য জুলুম সহ্য করে তাওহীদের দাওয়াতের ১৩ বছর।",
                "The momentous 13 years of early prophethood in Makkah: cave of Hira, steadfast resilience under boycott, and the Night Journey.",
                "মক্কার সম্ভ্রান্ত কুরাইশ বংশে জন্মলাভ করে সততার কারণে 'আল-আমীন' উপাধি পান। ৪০ বছর বয়সে হেরা গুহায় 'ইকরা' ওহীর মাধ্যমে নবুওয়াত লাভ করেন। ৩ বছর গোপনে ও পরবর্তীতে সাফা পাহাড়ে প্রকাশ্যে তাওহীদের ঘোষণা দেন। শিয়াবে আবী তালিবে ৩ বছরের বর্জন, তায়েফের রক্তক্ষয়ী নির্যাতন এবং ইসরা ও মি'রাজের মহা অলৌকিক ঘটনার মধ্য দিয়ে মাক্কী জীবন অতিবাহিত হয়।",
                "Renowned throughout youth as Al-Amin (the Trustworthy), Muhammad (SAW) received the first revelation in Cave Hira at age forty. Preaching monotheism on Mount Safa provoked fierce persecution, three years of harsh boycott in Shi'b Abi Talib, martyrdom of early companions, the trial of Taif, and the divine Night Journey of Isra and Miraj.",
                "قُلْ إِنَّ صَلَاتِي وَنُسُكِي وَمَحْيَايَ وَمَمَاتِي لِلَّهِ رَبِّ الْعَالَمِينَ • لَا شَرِيكَ لَهُ ۖ وَبِذَٰلِكَ أُمِرْتُ وَأَنَا أَوَّلُ الْمُسْلِمِينَ",
                "উচ্চারণ: ক্বুল ইন্না সালা-তী ওয়া নুসুকী ওয়া মাহইয়া-ইয়া ওয়া মামা-তী লিল্লা-হি রব্বিল 'আ-লামীন। লা- শারীকা লাহু ওয়া বিযা-লিকা উমিরতু ওয়া আনা আওওয়ালুল মুসলিমীন।",
                "অর্থ: বলুন: নিশ্চয় আমার সালাত, আমার কুরবানী, আমার জীবন ও আমার মরণ সমগ্র জাহানের প্রতিপালক আল্লাহরই জন্য। তাঁর কোনো শরিক নেই এবং আমাকে এরই নির্দেশ দেওয়া হয়েছে। (সূরা আল-আন'আম: ১৬২-১৬৩)",
                "• চরম জুলুম ও বিরোধিতার মাঝেও সত্যের দাওয়াত থেকে এক চুলও বিচ্যুত না হওয়া।\n• জীবনে ও মরণে সবকিছু এক আল্লাহর সন্তুষ্টির জন্য নিবেদন করা।",
                "• Unshakeable steadfastness in calling to truth despite boycott and violence.\n• Complete consecration of life, worship, and death to Allah alone."
        ));

        // 13. Muhammad (SAW) - Madani Life
        list.add(new ProphetStoryItem(
                13,
                "مُحَمَّدٌ رَسُولُ اللَّهِ ﷺ - المدني",
                "হযরত মুহাম্মাদ (সাঃ) - মাদানী জীবন",
                "Prophet Muhammad (SAW) - Madani Life",
                "হিজরত, ইনসাফভিত্তিক ইসলামী রাষ্ট্র প্রতিষ্ঠা, মক্কা বিজয় ও বিদায় হজ",
                "Hijrah, Statehood, Conquest of Makkah & Farewell Pilgrimage",
                "সর্বশেষ রাসুল",
                4,
                "সূরা আল-আনফাল, সূরা আল-ফাতহ, সূরা আন-নাসর, সূরা আল-হুজুরাত",
                "মদিনায় হিজরতের পর ভ্রাতৃত্বের অনন্য নজির স্থাপন, মদিনা সনদ, বদর-উহুদ-খন্দকের যুদ্ধ, ঐতিহাসিক মক্কা বিজয় ও বিদায় হজের মানবকল্যাণমূলক ঘোষণা।",
                "The triumphant 10-year Madani era: building Masjid an-Nabawi, the Charter of Madinah, major battles, peaceful liberation of Makkah, and the universal Farewell Address.",
                "মক্কার ষড়যন্ত্র ব্যর্থ করে হযরত আবু বকর (রা:)-কে সঙ্গে নিয়ে মদিনায় হিজরত করেন। আনসার ও মুহাজিরদের মাঝে অভূতপূর্ব ভ্রাতৃত্ব কায়েম করেন। মদিনা সনদের মাধ্যমে প্রথম ইসলামী রাষ্ট্র গঠন করেন। বদর, উহুদ ও খন্দকে শত্রুদের মোকাবেলা করেন। অষ্টম হিজরীতে বিনাযুদ্ধে মক্কা বিজয় করে শত্রুদের সাধারণ ক্ষমা ঘোষণা করেন এবং দশম হিজরীর বিদায় হজে সমগ্র মানবজাতির সাম্যের সনদ ঘোষণা করেন।",
                "Migrating to Madinah alongside Abu Bakr (RA), the Prophet established the first written constitution (Charter of Madinah) uniting diverse tribes. Defending the fledgling community at Badr, Uhud, and Khandaq, he liberated Makkah without bloodshed in 8 AH, pardoning all persecutors, and delivered the historic Farewell Sermon establishing universal human rights.",
                "وَمَا أَرْسَلْنَاكَ إِلَّا رَحْمَةً لِّلْعَالَمِينَ • لَّقَدْ كَانَ لَكُمْ فِي رَسُولِ اللَّهِ أُسْوَةٌ حَسَنَةٌ",
                "উচ্চারণ: ওয়া মা- আরসালনা-কা ইল্লা- রহমাতাল্লিল 'আ-লামীন। লাক্বদ কা-না লাকুম ফী রসূলিল্লা-হি উসওয়াতুন হাসানাহ।",
                "অর্থ: আর আমি আপনাকে সমগ্র বিশ্বজগতের জন্য রহমতস্বরূপ প্রেরণ করেছি। (সূরা আল-আম্বিয়া: ১০৭) নিশ্চয় আল্লাহর রাসুলের মধ্যে রয়েছে তোমাদের জন্য রয়েছে সর্বোত্তম আদর্শ। (সূরা আল-আহযাব: ২১)",
                "• ইনসাফ, সাম্য ও ভ্রাতৃত্বের ওপর সমাজ ও রাষ্ট্র পরিচালনা করা।\n• বিজয়ের মুহূর্তেও বিনম্র থাকা এবং শত্রুকে ক্ষমার মহত্ত্বে জয় করা।",
                "• Governance rooted in absolute justice, compassion, and egalitarian brotherhood.\n• Supreme humility in victory and conquering hatred through unmatched mercy."
        ));

        // 14. Ayyub (AS)
        list.add(new ProphetStoryItem(
                14,
                "أَيُّوبُ عَلَيْهِ السَّلَامُ",
                "হযরত আইয়ুব (আ:)",
                "Prophet Ayyub (AS)",
                "ধৈর্যের পরাকাষ্ঠা ও কঠিন পরীক্ষায় উত্তীর্ণ নবী",
                "The Model of Supreme Patience & Tested Servant",
                "বনি ইসরাঈল",
                4,
                "সূরা আল-আম্বিয়া: ৮৩-৮৪, সূরা সাদ: ৪১-৪৪",
                "ভয়াবহ রোগব্যাধি, সন্তান ও সম্পদ হারানোর চরম বিপদেও অবিচল ধৈর্যের অনন্য দৃষ্টান্ত স্থাপনকারী নবী।",
                "Tested with catastrophic loss of wealth, health, and children, he remained unflinchingly patient and thankful to Allah.",
                "আল্লাহর কঠিন পরীক্ষায় হযরত আইয়ুব (আ:) তাঁর অগাধ ধন-সম্পদ, সন্তান-সন্ততি ও সুস্বাস্থ্য হারান। দীর্ঘ ১৮ বছর দুরারোগ্য ব্যাধিতে আক্রান্ত থেকেও তিনি কখনো আল্লাহর প্রতি অভিযোগ করেননি। অবশেষে আল্লাহর রহমতে তিনি সুস্থতা ও হারানো নিয়ামতের দ্বিগুণ ফিরে পান।",
                "Afflicted with profound physical illness and the loss of all children and property for years, Ayyub (AS) never wavered in his devotion. His steadfast prayers were answered when Allah commanded him to strike the earth, bringing forth healing water.",
                "أَنِّي مَسَّنِيَ الضُّرُّ وَأَنتَ أَرْحَمُ الرَّاحِمِينَ",
                "উচ্চারণ: আন্নী মাস্সানিয়াদ্ দুররু ওয়া আনতা আরহামুর রা-হিমীন।",
                "অর্থ: নিশ্চয় আমি কষ্টে পড়েছি এবং আপনি তো সর্বশ্রেষ্ঠ দয়ালু। (সূরা আল-আম্বিয়া: ৮৩)",
                "• যেকোনো কঠিন রোগব্যাধি ও বিপদে অবিচল ধৈর্য ধারণ করা।\n• অভিযোগ না করে সবসময় আল্লাহর রহমতের ওপর ভরসা রাখা।",
                "• Supreme patience and dignified reliance upon Allah in times of distress.\n• Supplicating with utmost humility rather than complaining."
        ));

        // 15. Shuaib (AS)
        list.add(new ProphetStoryItem(
                15,
                "شُعَيْبٌ عَلَيْهِ السَّلَامُ",
                "হযরত শু'আইব (আ:)",
                "Prophet Shuaib (AS)",
                "খাতীবুল আম্বিয়া ও সততাপূর্ণ বাণিজ্যের পথপ্রদর্শক",
                "The Orator of the Prophets & Guide to Fair Commerce",
                "আরব নবী",
                11,
                "সূরা আল-আ'রাফ: ৮৫-৯৩, সূরা হূদ: ৮৪-৯৫, সূরা আশ-শু'আরা: ১৭৬-১৯১",
                "মাদইয়ান ও আইকাবাসীর প্রতি প্রেরিত বাগ্মী নবী, যিনি ব্যবসা-বাণিজ্যে মাপে কম দেওয়া ও দুর্নীতির বিরুদ্ধে সংগ্রাম করেন।",
                "Known as Khateeb al-Anbiya for his eloquence, he commanded honest commerce and justice against fraudulent merchants of Madyan.",
                "মাদইয়ান ও আইকাবাসী মূর্তিপূজার পাশাপাশি ব্যবসা-বাণিজ্যে মাপে কম দিত এবং ওজনে কারচুপি করত। শু'আইব (আ:) অত্যন্ত বাগ্মীতার সাথে তাদের তাওহীদের দাওয়াত দেন এবং ইনসাফপূর্ণ বাণিজ্যের নির্দেশ দেন। তারা সংশোধন না হওয়ায় বিকট ভূমিকম্প ও অগ্নিকুণ্ড মেঘমালার আযাবে তাদের ধ্বংস করে দেওয়া হয়।",
                "Known for his exquisite eloquence (Khateeb al-Anbiya), Shuaib (AS) preached against fraudulent commercial measurements, monopoly, and highway extortion. When the chieftains obstinately rejected divine guidance, a terrifying earthquake and fire from overhead dark clouds destroyed them.",
                "إِنْ أُرِيدُ إِلَّا الْإِصْلَاحَ مَا اسْتَطَعْتُ ۚ وَمَا تَوْفِيقِي إِلَّا بِاللَّهِ ۚ عَلَيْهِ تَوَكَّلْتُ وَإِلَيْهِ أُنِيبُ",
                "উচ্চারণ: ইন উরীদু ইল্লাল ইসলা-হা মাসতাত্বা'তু, ওয়া মা- তাওফীক্বী ইল্লা- বিল্লা-হি, 'আলাইহি তাওয়াক্কালতু ওয়া ইলাইহি উনীব।",
                "অর্থ: আমি আমার সাধ্যমত কেবল সংশোধনই চাই; আর আমার যাবতীয় সাফল্য একমাত্র আল্লাহরই সাহায্যে। তাঁর ওপরই আমি ভরসা করেছি এবং তাঁর দিকেই আমি প্রত্যাবর্তন করি। (সূরা হূদ: ৮৮)",
                "• ব্যবসা-বাণিজ্যে শতভাগ সততা ও ওজনে সঠিক পরিমাপ বজায় রাখা।\n• যেকোনো সংস্কার কাজের ক্ষেত্রে একমাত্র আল্লাহর ওপর তাওয়াক্কুল করা।",
                "• Absolute honesty and equity in economic dealings and commerce.\n• Dedicated striving for societal reform relying entirely upon Allah."
        ));

        // 16. Musa (AS) & Harun (AS)
        list.add(new ProphetStoryItem(
                16,
                "مُوسَىٰ وَهَارُونُ عَلَيْهِمَا السَّلَامُ",
                "হযরত মূসা (আ:) ও হযরত হারূণ (আ:)",
                "Prophet Musa (AS) & Harun (AS)",
                "কালীমুল্লাহ, তাওরাতের প্রাপক ও জালেম ফেরাউনের বিরুদ্ধে বিজয়ী",
                "The One Spoken to by Allah & Overcomer of Pharaoh",
                "উলুল আযম",
                136,
                "সূরা আল-বাকারা, সূরা আল-আ'রাফ, সূরা ত্বা-হা, সূরা আল-ক্বাসাস",
                "নীল নদে ভেসে গিয়ে ফেরাউনের ঘরে বড় হওয়া এবং পরবর্তীতে লোহিত সাগর দ্বিখণ্ডিত করার মুজিযাপ্রাপ্ত উলুল আযম রাসুল।",
                "Spoke directly to Allah at Mount Sinai, received the Torah, parted the Red Sea, and liberated the Israelites from Pharaoh.",
                "হযরত মূসা (আ:) আল্লাহর কুদরতে জালিম ফেরাউনের রাজপ্রাসাদেই প্রতিপালিত হন। মাদইয়ান থেকে ফেরার পথে তুর পাহাড়ে আল্লাহর সাথে সরাসরি বাক্যালাপের সৌভাগ্য অর্জন করেন। লাঠিকে অজগরে রূপান্তর ও হাত উজ্জ্বল করার মুজিযাসহ ভাই হারূণ (আ:)-কে সাথে নিয়ে ফেরাউনকে তাওহীদের দাওয়াত দেন। অবাধ্য ফেরাউন দলবলসহ লোহিত সাগরে নিমজ্জিত হয় এবং মূসা (আ:) বনি ইসরাঈলকে মুক্ত করেন।",
                "Cast into the Nile as an infant by divine inspiration, Musa (AS) was raised in Pharaoh's royal household. At Mount Sinai, Allah spoke directly to him and sent him alongside his eloquent brother Harun to challenge tyrannical Pharaoh. When Pharaoh pursued them, Allah parted the Red Sea, drowning the tyrant and delivering the believers.",
                "رَبِّ اشْرَحْ لِي صَدْرِي • وَيَسِّرْ لِي أَمْرِي • وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي • يَفْقَهُوا قَوْلِي",
                "উচ্চারণ: রব্বিশরাহ লী সদ্রী, ওয়া ইয়াসসির লী আমরী, ওয়াহলুল 'উক্বদাতাম মিল্লিসা-নী, ইয়াফক্বাহূ ক্বাওলী।",
                "অর্থ: হে আমার পালনকর্তা! আমার বক্ষ প্রশস্ত করে দিন, আমার কাজ সহজ করে দিন এবং আমার জিহ্বার জড়তা দূর করে দিন যাতে তারা আমার কথা বুঝতে পারে। (সূরা ত্বা-হা: ২৫-২৮)",
                "• যেকোনো কঠিন কাজে আল্লাহর কাছে বক্ষ প্রশস্ততা ও আত্মবিশ্বাসের দোয়া করা।\n• জালেমের অহংকার যতই শক্তিশালী হোক, আল্লাহর কুদরতের সামনে তা ধূলিসাৎ হতে বাধ্য।",
                "• Seeking divine courage and clarity of speech before undertaking major responsibilities.\n• Tyranny, no matter how formidable, ultimately crumbles before divine justice."
        ));

        // 17. Yunus (AS)
        list.add(new ProphetStoryItem(
                17,
                "يُونُسُ عَلَيْهِ السَّلَامُ",
                "হযরত ইউনুস (আ:)",
                "Prophet Yunus (AS)",
                "যুন-নূন, মাছের পেটের নবী ও গভীর অন্ধকারের দোয়া",
                "Companion of the Whale & Supplication in Darkness",
                "বনি ইসরাঈল",
                4,
                "সূরা ইউনুস: ৯৮, সূরা আল-আম্বিয়া: ৮৭-৮৮, সূরা আস-সাফফাত: ১৩৯-১৪৮",
                "নিনওয়া নগরীর নবী, যিনি মাছের পেটের গভীর অন্ধকারে একনিষ্ঠ তাওহীদের দোয়া পাঠ করেছিলেন।",
                "Swallowed by a massive whale, he uttered the immortal prayer of monotheistic repentance that saved his life.",
                "ইউনুস (আ:) নিনওয়াবাসীকে দাওয়াত দেন। তারা ঈমান না আনায় আল্লাহর চূড়ান্ত আদেশের পূর্বেই তিনি নগরী ত্যাগ করেন। জাহাজে ওঠার পর উত্তাল সমুদ্রে নিক্ষিপ্ত হলে একটি বিশাল তিমি মাছ তাঁকে গিলে ফেলে। মাছের পেটের ত্রিমুখী অন্ধকারে তিনি বিনীত তাওবা করেন। আল্লাহ তাঁকে ক্ষমা করেন এবং সুস্থ অবস্থায় তীরে নিক্ষেপ করেন।",
                "Sent to Nineveh, Yunus (AS) departed prematurely when his people showed initial reluctance. Aboard a stormy vessel, lots were cast and he was cast into the sea where a gigantic whale swallowed him. In the depths of the ocean inside the whale, he cried out in sincere repentance. Allah delivered him safely back to his people who ultimately embraced faith.",
                "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
                "উচ্চারণ: লা- ইলা-হা ইল্লা- আনতা সুবহা-নাকা ইন্নী কুনতু মিনায জোয়া-লিমীন।",
                "অর্থ: আপনি ছাড়া কোনো সত্য উপাস্য নেই, আপনি পরম পবিত্র! নিশ্চয় আমি অপরাধীদের অন্তর্ভুক্ত হয়ে গেছি। (সূরা আল-আম্বিয়া: ৮৭)",
                "• যেকোনো কঠিন বিপদ, হতাশা বা সংকটে দোয়ায়ে ইউনুসের মাধ্যমে আল্লাহর কাছে সাহায্য প্রার্থনা করা।\n• নিজের ভুল স্বীকার করে বিনীত তাওবা করাই আল্লাহর রহমত লাভের চাবিকাঠি।",
                "• The supreme supplication (Ayat al-Kareema) for relieving any distress or hardship.\n• Sincere self-accountability and humble submission attract immediate divine grace."
        ));

        // 18. Dawud (AS)
        list.add(new ProphetStoryItem(
                18,
                "دَاوُودُ عَلَيْهِ السَّلَامُ",
                "হযরত দাঊদ (আ:)",
                "Prophet Dawud (AS)",
                "যবূরের প্রাপক, সুরের জাদুকর ও সুবিচারক বাদশাহ নবী",
                "Recipient of the Zabur, Just King & Master Craftsman",
                "বনি ইসরাঈল",
                16,
                "সূরা আল-বাকারা: ২৫১, সূরা আন-নিসা: ১৬৩, সূরা আল-আম্বিয়া: ৭৮-৮০, সূরা সাদ: ১৭-২৬",
                "জালুতকে বধকারী, যাঁর সুরেলা যবূর তেলাওয়াতে পাহাড় ও পাখিরা তসবিহ করত এবং যিনি স্বহস্তে বর্ম তৈরি করতেন।",
                "Slew the giant Goliath (Jalut), granted a righteous kingdom, softened iron with his hands, and praised Allah alongside mountains and birds.",
                "তরুণ বয়সে পরাক্রমশালী জালুতকে সাধারণ গুলতি দিয়ে হত্যা করে বনি ইসরাঈলকে বিজয় এনে দেন। আল্লাহ তাঁকে নবুওয়াত ও রাজত্ব দান করেন। তাঁর সুমধুর কণ্ঠে যবূর তেলাওয়াত শুনে পাহাড় ও পাখিরাও আল্লাহর প্রশংসায় যোগ দিত। তিনি লোহাকে মোমের মতো নরম করে বর্ম তৈরির কাজ করতেন এবং নিজের উপার্জনে জীবনধারণ করতেন।",
                "As a young youth, Dawud (AS) felled the tyrant Goliath with a single sling stone. Blessed with wisdom, righteous monarchy, and the Psalms (Zabur), even the mountains and birds echoed his melodious praises of Allah. Allah softened iron for his hands, enabling him to craft flexible chainmail armor for livelihood.",
                "يَا جِبَالُ أَوِّبِي مَعَهُ وَالطَّيْرَ ۖ وَأَلَنَّا لَهُ الْحَدِيدَ • أَنِ اعْمَلْ سَابِغَاتٍ وَقَدِّرْ فِي السَّرْدِ ۖ وَاعْمَلُوا صَالِحًا",
                "উচ্চারণ: ইয়া- জিবা-লু আওউইবী মা'আহূ ওয়াত তাইরা, ওয়া আলান্না- লাহুল হাদীদ। আন ই'মাল সা-বিগা-তিওঁ ওয়া ক্বাদ্দির ফিস সারদি, ওয়া'মালূ সা-লিহা-।",
                "অর্থ: হে পর্বতমালা! তোমরা তার সাথে তসবিহ পাঠে যোগ দাও এবং পাখিরাও। আর আমি তার জন্য লোহাকে নরম করে দিয়েছিলাম; এই নির্দেশ দিয়ে যে, প্রশস্ত বর্ম তৈরি করো এবং তার কড়িগুলো যথাযথ মাপে যুক্ত করো, আর তোমরা সৎকাজ করো। (সূরা সাবা: ১০-১১)",
                "• স্বহস্তে হালাল উপার্জন করে জীবিকা নির্বাহ করা অত্যন্ত মর্যাদাপূর্ণ।\n• ক্ষমতা ও রাজত্ব পেলেও সার্বক্ষণিক আল্লাহর জিকির ও ইবাদতে মশগুল থাকা।",
                "• Dignity of earning an honest livelihood through manual craftsmanship.\n• Maintaining deep devotion and continuous glorification of Allah amidst high authority."
        ));

        // 19. Sulaiman (AS)
        list.add(new ProphetStoryItem(
                19,
                "سُلَيْمَانُ عَلَيْهِ السَّلَامُ",
                "হযরত সুলাইমান (আ:)",
                "Prophet Sulaiman (AS)",
                "বায়ু ও জ্বিনদের শাসক, পশুপাখির ভাষা জানা মহামহিম নবী",
                "Ruler of Winds & Jinn, Sovereign of Grand Monarchy",
                "বনি ইসরাঈল",
                17,
                "সূরা আল-আম্বিয়া: ৮১-৮২, সূরা আন-নামল: ১৫-৪৪, সূরা সাবা: ১২-১৪, সূরা সাদ: ৩০-৪০",
                "বায়ুপ্রবাহ, জ্বিন জাতি ও সমগ্র জীবজগতের ওপর শাসনক্ষমতাপ্রাপ্ত বাদশাহ নবী, যিনি সাবা রাজ্যের রানি বিলকিসকে ইসলামের দাওয়াত দেন।",
                "Endowed with unparalleled sovereignty controlling winds and armies of men, jinn, and birds, and understanding animal languages.",
                "দাঊদ (আ:)-এর পুত্র সুলাইমান (আ:)-কে আল্লাহ এমন সাম্রাজ্য দান করেছিলেন যা ইতিহাসে আর কাউকে দেওয়া হয়নি। বাতাস তাঁর আদেশে চলত, জ্বিন জাতি তাঁর নির্দেশে বায়তুল মুকাদ্দাস নির্মাণসহ সুরম্য প্রাসাদ তৈরি করত। তিনি পিঁপড়া ও হুদহুদ পাখির ভাষা বুঝতেন। বিপুল ক্ষমতার অধিকারী হয়েও তিনি ছিলেন অত্যন্ত বিনম্র ও শোকরগোযার বান্দা।",
                "Granted an unprecedented realm: the wind obeyed his commands, legions of jinn laboured in erecting the Temple of Jerusalem, and he comprehended the languages of birds and ants. When informed of the Queen of Sheba (Bilqis), he invited her with sublime diplomacy, leading her to worship the One True God.",
                "رَبِّ أَوْزِعْنِي أَنْ أَشْكُرَ نِعْمَتَكَ الَّتِي أَنْعَمْتَ عَلَيَّ وَعَلَىٰ وَالِدَيَّ وَأَنْ أَعْمَلَ صَالِحًا تَرْضَاهُ وَأَدْخِلْنِي بِرَحْمَتِكَ فِي عِبَادِكَ الصَّالِحِينَ",
                "উচ্চারণ: রব্বি আওযি'নী আন আশকুরা নি'মাতাকাল্লাতী আন'আমতা 'আলাইয়্যা ওয়া 'আলা- ওয়া-লিদাইয়্যা ওয়া আন আ'মালা সা-লিহান তারদা-হু ওয়া আদখিলনী বিরাহমাতিকা ফী 'ইবা-দিকাস সা-লিহীন।",
                "অর্থ: হে আমার প্রতিপালক! আমাকে সামর্থ্য দিন যেন আমি আপনার সেই নিয়ামতের শুকরিয়া আদায় করতে পারি যা আপনি আমাকে ও আমার পিতা-মাতাকে দান করেছেন, এবং যেন এমন সৎকাজ করতে পারি যা আপনি পছন্দ করেন। আর আপনার অনুগ্রহে আমাকে আপনার সৎকর্মশীল বান্দাদের অন্তর্ভুক্ত করুন। (সূরা আন-নামল: ১৯)",
                "• সর্বপ্রকার ধনসম্পদ ও ক্ষমতার মুহূর্তে অহংকার ভুলে কৃতজ্ঞতা প্রকাশ করা।\n• সৃষ্টিজগতের ক্ষুদ্রাতিক্ষুদ্র প্রাণীর প্রতিও সহানুভূতিশীল হওয়া।",
                "• Boundless gratitude and humility in the presence of immense power and wealth.\n• Compassion toward even the smallest creatures in creation."
        ));

        // 20. Ilyas (AS)
        list.add(new ProphetStoryItem(
                20,
                "إِلْيَاسُ عَلَيْهِ السَّلَامُ",
                "হযরত ইলিয়াস (আ:)",
                "Prophet Ilyas (AS)",
                "বাল দেবতার পূজার বিরুদ্ধে তাওহীদের আপসহীন সংগ্রামী নবী",
                "Prophet Elijah, Champion against the Cult of Ba'al",
                "বনি ইসরাঈল",
                2,
                "সূরা আল-আন'আম: ৮৫, সূরা আস-সাফফাত: ১২৩-১৩০",
                "বাল মূর্তির পূজায় লিপ্ত বনি ইসরাঈলের বা'লাবাক নগরীতে তাওহীদের দাওয়াত দেওয়া নবী।",
                "Sent to Baalbek to confront idolatrous worship of the golden idol Ba'al and call people back to the Supreme Creator.",
                "বনি ইসরাঈল যখন তাওহীদ ভুলে 'বাল' নামক এক মনগড়া মূর্তির উপাসনা শুরু করে, তখন ইলিয়াস (আ:) তাদের এক আল্লাহর ইবাদত করার আহ্বান জানান। তিনি বলেছিলেন: 'তোমরা কি সর্বশ্রেষ্ঠ স্রষ্টাকে ছেড়ে বালের পূজা করছ?' বহু বাধা ও ষড়যন্ত্র সহ্য করেও তিনি সত্যের পথে অটল থাকেন।",
                "When the Israelites apostatized into worshiping the idol Ba'al, Ilyas (AS) confronted their king and priests with resolute faith: 'Do you call upon Ba'al and forsake the Best of Creators?' Despite threats upon his life, he remained steadfast in monotheism.",
                "وَإِنَّ إِلْيَاسَ لَمِنَ الْمُرْسَلِينَ • إِذْ قَالَ لِقَوْمِهِ أَلَا تَتَّقُونَ • أَتَدْعُونَ بَعْلًا وَتَذَرُونَ أَحْسَنَ الْخَالِقِينَ",
                "উচ্চারণ: ওয়া ইন্না ইলয়া-সা লামিনাল মুরসালীন। ইয ক্বা-লা লিক্বাওমিহী আলা- তাত্তাক্বূন। আতাদ'ঊনা বা'লাওঁ ওয়া তাযারূনা আহসানাল খা-লিক্বীন।",
                "অর্থ: আর নিশ্চয় ইলিয়াস ছিলেন রাসুলদের একজন। যখন তিনি তাঁর কওমকে বললেন: তোমরা কি আল্লাহকে ভয় করবে না? তোমরা কি 'বাল'-কে ডাকছ এবং বর্জন করছ সর্বশ্রেষ্ঠ স্রষ্টাকে? (সূরা আস-সাফফাত: ১২৩-১২৫)",
                "• সমাজের প্রচলিত কুসংস্কার ও শিরকের বিরুদ্ধে সত্য কথা বলা।\n• প্রকৃত উপাস্য একমাত্র আল্লাহ, কোনো সৃষ্টি বা মূর্তি নয়।",
                "• Uncompromising defense of pure monotheism against societal idol worship.\n• True worship belongs exclusively to Allah, the Supreme Creator."
        ));

        // 21. Al-Yasa (AS)
        list.add(new ProphetStoryItem(
                21,
                "الْيَسَعُ عَلَيْهِ السَّلَامُ",
                "হযরত আল-ইয়াসা' (আ:)",
                "Prophet Al-Yasa (AS)",
                "ইলিয়াস (আ:)-এর উত্তরসূরি ও প্রশংসিত ধৈর্যশীল নবী",
                "Prophet Elisha, The Chosen & Righteous Guide",
                "বনি ইসরাঈল",
                2,
                "সূরা আল-আন'আম: ৮৬, সূরা সাদ: ৪৮",
                "ইলিয়াস (আ:)-এর সান্নিধ্যে থেকে দ্বীনের শিক্ষা লাভকারী এবং পরবর্তীতে বনি ইসরাঈলের নবী হওয়া মহান ব্যক্তি।",
                "The steadfast disciple of Ilyas (AS) who succeeded him in guiding the Israelites with wisdom and righteousness.",
                "হযরত আল-ইয়াসা' (আ:) শৈশবে গুরুতর অসুস্থ হলে ইলিয়াস (আ:)-এর দোয়ায় সুস্থতা লাভ করেন। এরপর থেকে তিনি সার্বক্ষণিক ইলিয়াস (আ:)-এর সঙ্গী হন। ইলিয়াস (আ:)-এর পর তিনি বনি ইসরাঈলকে আল্লাহর কিতাব ও শরীয়ত অনুসারে পথ দেখান। পবিত্র কুরআনে আল্লাহ তাঁকে উত্তম ও মনোনীতদের অন্তর্ভুক্ত বলে ঘোষণা করেছেন।",
                "Saved from a severe childhood affliction through the prayers of Ilyas (AS), Al-Yasa became his dedicated companion. Following Ilyas's departure, he guided the community in righteous governance according to divine law, honored in the Quran among the chosen best.",
                "وَاذْكُرْ إِسْمَاعِيلَ وَالْيَسَعَ وَذَا الْكِفْلِ ۖ وَكُلٌّ مِّنَ الْأَخْيَارِ",
                "উচ্চারণ: ওয়াযকুর ইসমা-'ঈলা ওয়াল ইয়াসা'আ ওয়া যাল কিফলি, ওয়া কুল্লুম মিনাল আখইয়ার।",
                "অর্থ: আর স্মরণ করুন ইসমাঈল, আল-ইয়াসা' ও যুল-কিফ্লকে; এবং তাঁদের প্রত্যেকেই ছিলেন শ্রেষ্ঠদের অন্তর্ভুক্ত। (সূরা সাদ: ৪৮)",
                "• সৎ সঙ্গ ও নেককার উস্তাদের সাহচর্য মানুষের জীবনকে আলোকিত করে।\n• দায়িত্ব পেলে সততা ও ইনসাফের সাথে তা পালন করা।",
                "• The profound spiritual benefit of virtuous mentorship.\n• Fulfilling leadership with unblemished righteousness."
        ));

        // 22. Dhul-Kifl (AS)
        list.add(new ProphetStoryItem(
                22,
                "ذُو الْكِفْلِ عَلَيْهِ السَّلَامُ",
                "হযরত যুল-কিফ্ল (আ:)",
                "Prophet Dhul-Kifl (AS)",
                "প্রতিশ্রুতি পালনকারী, সংযমী ও অটল ধৈর্যের অধিকারী নবী",
                "The One of Boundless Faithfulness, Patience & Justice",
                "বনি ইসরাঈল",
                2,
                "সূরা আল-আম্বিয়া: ৮৫-৮৬, সূরা সাদ: ৪৮",
                "যিনি তাঁর কওমের শাসনভার গ্রহণকালে দিনে রোযা রাখা, রাতে তাহাজ্জুদ পড়া ও কখনো রাগ না করার অঙ্গীকার পূর্ণ করেছিলেন।",
                "Honored for fulfilling severe covenants: fasting by day, standing in prayer by night, and ruling with unflinching patience and restraint.",
                "তাঁকে 'যুল-কিফ্ল' (প্রতিশ্রুতি রক্ষাকারী বা দ্বিগুণ প্রতিদানপ্রাপ্ত) বলা হয় কারণ তিনি অঙ্গীকার করেছিলেন যে তিনি প্রজাদের মাঝে ন্যায়বিচার করবেন, দিনের বেলা সিয়াম রাখবেন, রাতে আল্লাহর ইবাদত করবেন এবং রাগের বশবর্তী হবেন না। শত প্ররোচনা সত্ত্বেও তিনি তাঁর প্রতিটি অঙ্গীকার অক্ষরে অক্ষরে পালন করেছিলেন।",
                "Known as Dhul-Kifl ('Possessor of the Fold/Pledge') because he covenanted to judge with total impartiality, fast continuously, spend his nights in worship, and never succumb to anger. He fulfilled his pledge flawlessly amidst severe tribulations.",
                "وَإِسْمَاعِيلَ وَإِدْرِيسَ وَذَا الْكِفْلِ ۖ كُلٌّ مِّنَ الصَّابِرِينَ • وَأَدْخَلْنَاهُمْ فِي رَحْمَتِنَا ۖ إِنَّهُم مِّنَ الصَّالِحِينَ",
                "উচ্চারণ: ওয়া ইসমা-'ঈলা ওয়া ইদরীসা ওয়া যাল কিফলি, কুল্লুম মিনাস সা-বিরীন। ওয়া আদখালনা-হুম ফী রাহমাতিনা-, ইন্নাহুম মিনাস সা-লিহীন।",
                "অর্থ: আর স্মরণ করুন ইসমাঈল, ইদরীস ও যুল-কিফ্লকে; তাঁদের প্রত্যেকেই ছিলেন চরম ধৈর্যশীল। এবং আমি তাঁদেরকে আমার রহমতের অন্তর্ভুক্ত করেছিলাম; নিশ্চয় তাঁরা ছিলেন সৎকর্মশীল। (সূরা আল-আম্বিয়া: ৮৫-৮৬)",
                "• দেওয়া প্রতিশ্রুতি বা অঙ্গীকার জীবন দিয়ে হলেও রক্ষা করা।\n• রাগ নিয়ন্ত্রণ করা এবং ন্যায়ের বিচার থেকে একবিন্দু বিচ্যুত না হওয়া।",
                "• Absolute commitment to honoring oaths and covenants.\n• Mastering anger and exercising impartial justice under pressure."
        ));

        // 23. Zakariya (AS)
        list.add(new ProphetStoryItem(
                23,
                "زَكَرِيَّا عَلَيْهِ السَّلَامُ",
                "হযরত যাকারিয়া (আ:)",
                "Prophet Zakariya (AS)",
                "বায়তুল মুকাদ্দাসের ইমাম, মারইয়ামের অভিভাবক ও দোয়ার অনুপম আদর্শ",
                "Guardian of Maryam & Sublimely Sincere Supplicant",
                "বনি ইসরাঈল",
                7,
                "সূরা আলে ইমরান: ৩৭-৪১, সূরা মারইয়াম: ১-১৫, সূরা আল-আম্বিয়া: ৮৯-৯০",
                "বৃদ্ধ বয়সে একাকী দোয়া করে ইয়াহইয়া (আ:)-এর মতো সৎ ও মুত্তাকী পুত্র লাভকারী নবী।",
                "The venerable custodian of the sanctuary who cared for virgin Maryam and prayed for a righteous heir in his frail old age.",
                "যাকারিয়া (আ:) বায়তুল মুকাদ্দাসের দায়িত্ব পালন করতেন এবং মারইয়াম (আ:)-এর তত্ত্বাবধান করতেন। মারইয়ামের কাছে গায়েবি ফল দেখে তিনি অনুপ্রাণিত হয়ে বার্ধক্যে আল্লাহর কাছে এক নেককার সন্তানের জন্য গোপনে অশ্রুসিক্ত দোয়া করেন। আল্লাহ তাঁর দোয়া কবুল করে ইয়াহইয়া (আ:)-এর সুসংবাদ দেন।",
                "Zakariya (AS) dedicated his life to sanctuary service in Jerusalem, providing guardian care to Maryam. Witnessing out-of-season fruits provided miraculously to her, he was inspired to whisper a tearful prayer for a righteous heir despite his frail bones and greying hair. Allah blessed him with Yahya (AS).",
                "رَبِّ لَا تَذَرْنِي فَرْدًا وَأَنتَ خَيْرُ الْوَارِثِينَ",
                "উচ্চারণ: রব্বি লা- তাযারনী ফারদাওঁ ওয়া আনতা খায়রুল ওয়া-রিছীন।",
                "অর্থ: হে আমার প্রতিপালক! আমাকে একা (নিঃসন্তান) ছেড়ে দেবেন না, আর আপনি তো সর্বশ্রেষ্ঠ ওয়ারিশ। (সূরা আল-আম্বিয়া: ৮৯)",
                "• শারীরিক অসমর্থতা বা চরম বার্ধক্যেও আল্লাহর কাছে নিঃসঙ্কোচে দোয়া করা।\n• নেককার বংশধরের জন্য নিয়মিত একনিষ্ঠ প্রার্থনা করা।",
                "• Never despairing of miraculous prayers even when circumstances seem impossible.\n• Sincere supplication for pious progeny to carry forward the light of faith."
        ));

        // 24. Yahya (AS)
        list.add(new ProphetStoryItem(
                24,
                "يَحْيَىٰ عَلَيْهِ السَّلَامُ",
                "হযরত ইয়াহইয়া (আ:)",
                "Prophet Yahya (AS)",
                "শৈশব থেকেই প্রজ্ঞাবান, কোমল ও চরিত্রবান শহীদ নবী",
                "Prophet John the Baptist, Pious & Noble Martyr of Truth",
                "বনি ইসরাঈল",
                5,
                "সূরা আলে ইমরান: ৩৯, সূরা মারইয়াম: ১২-১৫, সূরা আল-আম্বিয়া: ৯০",
                "যাকারিয়া (আ:)-এর পুত্র, যাঁর নাম স্বয়ং আল্লাহ রেখেছিলেন এবং শৈশবেই প্রজ্ঞা ও পবিত্রতা দান করেছিলেন।",
                "Son of Zakariya, named directly by Allah and granted wisdom, purity, and profound compassion in his youth.",
                "তিনি অত্যন্ত দুনিয়াবিমুখ ও কোমল হৃদয়ের মানুষ ছিলেন। আল্লাহর কিতাবকে অত্যন্ত দৃঢ়তার সাথে ধারণ করেন এবং পিতা-মাতার প্রতি পরম অনুগত ছিলেন। সত্যের পথে আপসহীন থাকার কারণে জালিম শাসক দ্বারা শহীদ হন।",
                "Yahya (AS) held fast to the divine scriptures with total conviction from boyhood. Untouched by pride or rebellion, he called his people to repentance before being martyred for defying a corrupt ruler's illicit desires.",
                "يَا يَحْيَىٰ خُذِ الْكِتَابَ بِقُوَّةٍ ۖ وَآتَيْنَاهُ الْحُكْمَ صَبِيًّا • وَحَنَانًا مِّن لَّدُنَّا وَزَكَاةً ۖ وَكَانَ تَقِيًّا",
                "উচ্চারণ: ইয়া- ইয়াহইয়া- খুযিল কিতা-বা বিক্বুওয়াহ, ওয়া আ-তাইনা-হুল হুকমা সবিয়্যা। ওয়া হানা-নাম মিল্লাদুন্না- ওয়া যাকা-হ, ওয়া কা-না তাক্বিয়্যা।",
                "অর্থ: হে ইয়াহইয়া! এই কিতাবকে দৃঢ়ভাবে ধারণ করো। আর আমি তাঁকে শৈশবেই দান করেছিলাম প্রজ্ঞা। এবং আমার পক্ষ থেকে কোমলতা ও পবিত্রতা; আর তিনি ছিলেন মুত্তাকী। (সূরা মারইয়াম: ১২-১৩)",
                "• শৈশব থেকেই দ্বীনি জ্ঞান ও কিতাবকে দৃঢ়ভাবে আঁকড়ে ধরা।\n• পিতা-মাতার প্রতি অনুগত হওয়া এবং অহংকার থেকে দূরে থাকা।",
                "• Embracing divine knowledge and scriptures with resolute commitment in youth.\n• Exemplary humility, tenderness of heart, and filial obedience."
        ));

        // 25. Isa (AS)
        list.add(new ProphetStoryItem(
                25,
                "عِيسَىٰ عَلَيْهِ السَّلَامُ",
                "হযরত ঈসা (আ:)",
                "Prophet Isa (AS)",
                "রূহুল্লাহ, মারইয়াম-পুত্র, ইঞ্জিলের প্রাপক ও অলৌকিক নিদর্শনের নবী",
                "Messiah, Son of Maryam, Spirit from Allah & Great Prophet",
                "উলুল আযম",
                25,
                "সূরা আল-বাকারা: ৮৭, সূরা আলে ইমরান: ৪৫-৫৯, সূরা আন-নিসা: ১৫৬-১৫৯, সূরা আল-মায়েদা: ১১০-১১৮",
                "পিতা ছাড়া কুমারী মারইয়ামের গর্ভে অলৌকিকভাবে জন্ম নেওয়া উলুল আযম রাসুল, যিনি দোলনায় কথা বলেছেন এবং অন্ধ ও কুষ্ঠরোগীকে সুস্থ করেছেন।",
                "Born miraculously without a father to Virgin Maryam, spoke in infancy, cured the blind and leper, and raised the dead by Allah's permission.",
                "আল্লাহর নির্দেশে মৃতকে জীবিত করা, অন্ধ ও কুষ্ঠরোগীকে নিরাময় করা এবং মাটির পাখিতে ফুঁ দিয়ে জীবন্ত করার মুজিযা প্রদর্শন করেন। ইহুদীরা তাঁকে ক্রুশবিদ্ধ করার ষড়যন্ত্র করলে আল্লাহ তাঁকে সশরীরে আসমানে তুলে নেন। শেষ জমানায় তিনি পুনরায় দুনিয়ায় আগমন করে দাজ্জালকে বধ করবেন।",
                "Gifted with astonishing miracles by Allah's leave: speaking in the cradle, curing lepers, and giving sight to the born-blind. When enemies plotted his crucifixion, Allah raised him bodily to heaven. He will descend in the end times to defeat the Dajjal and establish justice.",
                "إِنِّي عَبْدُ اللَّهِ آتَانِيَ الْكِتَابَ وَجَعَلَنِي نَبِيًّا • وَجَعَلَنِي مُبَارَكًا أَيْنَ مَا كُنتُ وَأَوْصَانِي بِالصَّلَاةِ وَالزَّكَاةِ مَا دُمْتُ حَيًّا",
                "উচ্চারণ: ইন্নী আব্দুল্লা-হি আ-তা-নিয়াল কিতা-বা ওয়া জা'আলানী নাবিয়্যা। ওয়া জা'আলানী মুবা-রকান আইনা মা- কুনতু ওয়া আওস-নী বিস সালা-তি ওয়ায যাকা-তি মা- দুমতু হাইয়্যা।",
                "অর্থ: তিনি বললেন: নিশ্চয় আমি আল্লাহর বান্দা; তিনি আমাকে কিতাব দিয়েছেন এবং আমাকে নবী করেছেন। আর আমি যেখানেই থাকি তিনি আমাকে বরকতময় করেছেন এবং নির্দেশ দিয়েছেন যতদিন জীবিত থাকি সালাত ও যাকাত আদায় করতে। (সূরা মারইয়াম: ৩০-৩১)",
                "• তিনি আল্লাহর বান্দা ও রাসুল, কোনো উপাস্য বা আল্লাহর পুত্র নন।\n• জীবনভর সালাত ও যাকাত কায়েম রাখা।\n• মাতা মারইয়ামের পবিত্রতা ও সততার উজ্জ্বল নিদর্শন।",
                "• Isa (AS) is an honored slave and messenger of Allah, not divine or son of God.\n• Uncompromising duty of establishing prayer and zakat throughout life.\n• Vindicating maternal honor with divine truth."
        ));

        // 26. Muhammad (SAW) - Complete Final Messenger
        list.add(new ProphetStoryItem(
                26,
                "مُحَمَّدٌ رَسُولُ اللَّهِ ﷺ",
                "হযরত মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম)",
                "Prophet Muhammad (ﷺ)",
                "খাতামুন নাবিয়্যীন, রহমাতুল্লিল আলামীন ও সর্বশ্রেষ্ঠ মহামানব",
                "Seal of the Prophets & Universal Mercy to Creation",
                "সর্বশেষ রাসুল",
                4,
                "সূরা আলে ইমরান: ১৪৪, সূরা আল-আহযাব: ৪০, সূরা মুহাম্মদ: ২, সূরা আল-ফাতহ: ২৯",
                "সর্বকালের সর্বশ্রেষ্ঠ মহামানব, সর্বশেষ নবী ও রাসুল, যাঁর ওপর অবতীর্ণ হয়েছে কিয়ামত পর্যন্ত অক্ষুণ্ণ মহাগ্রন্থ পবিত্র কুরআন।",
                "The Seal of the Prophets, leader of all messengers, and universal mercy to humanity upon whom the Final Quran was revealed.",
                "মক্কার কুরাইশ বংশে জন্মলাভ করে শৈশব থেকেই 'আল-আমীন' উপাধি লাভ করেন। হেরা গুহায় ওহী নাযিলের পর ২৩ বছর ধরে তাওহীদের বাণী প্রচার করেন। মক্কার অকথ্য জুলুম সহ্য করে মদিনায় হিজরত করেন এবং একটি ইনসাফভিত্তিক ইসলামী রাষ্ট্র প্রতিষ্ঠা করেন। মক্কা বিজয়ের পর চরম শত্রুদেরও সাধারণ ক্ষমা ঘোষণা করেন। বিদায় হজে সমগ্র মানবজাতির সাম্য ও মানবাধিকারের ঐতিহাসিক ঘোষণা দেন।",
                "Born in Makkah, known as Al-Amin (the Trustworthy), he received revelation at age forty in Cave Hira. Over 23 years, he transformed a fractured, pagan society into the pinnacle of faith and justice. Migrating to Madinah, he established an egalitarian state, pardoned former persecutors upon liberating Makkah, and delivered the immortal Farewell Sermon declaring universal human equality.",
                "وَمَا أَرْسَلْنَاكَ إِلَّا رَحْمَةً لِّلْعَالَمِينَ • لَّقَدْ كَانَ لَكُمْ فِي رَسُولِ اللَّهِ أُسْوَةٌ حَسَنَةٌ",
                "উচ্চারণ: ওয়া মা- আরসালনা-কা ইল্লা- রহমাতাল্লিল 'আ-লামীন। লাক্বদ কা-না লাকুম ফী রসূলিল্লা-হি উসওয়াতুন হাসানাহ।",
                "অর্থ: আর আমি আপনাকে সমগ্র বিশ্বজগতের জন্য রহমতস্বরূপ প্রেরণ করেছি। (সূরা আল-আম্বিয়া: ১০৭) নিশ্চয় আল্লাহর রাসুলের মধ্যে তোমাদের জন্য রয়েছে সর্বোত্তম আদর্শ। (সূরা আল-আহযাব: ২১)",
                "• জীবনের প্রতিটি পদক্ষেপে রাসুলুল্লাহ (সাঃ)-এর সুন্নাহ ও আদর্শ অনুসরণ করা।\n• চরম শত্রুকেও ক্ষমার উদারতায় জয় করা।\n• বর্ণ, বংশ ও ভাষার ভেদাভেদ ভুলে মানবজাতিকে এক আল্লাহর বান্দা হিসেবে ভালোবাসা।",
                "• Emulating the Prophet's Sunnah as the ultimate guide in personal and public life.\n• Conquering hostility through boundless compassion and moral grace.\n• Championing human brotherhood beyond all distinctions of race and ethnicity."
        ));

        cachedProphetsList = list;
        return list;
    }

    public static synchronized List<ProphetMenuItem> getMenuItems() {
        if (cachedMenuItems != null) return cachedMenuItems;

        List<ProphetStoryItem> prophets = getProphetsList();
        List<ProphetMenuItem> list = new ArrayList<>();

        // 1. নবীদের কাহিনী সমূহ (Type: OVERVIEW) - Exactly matching Screenshot 1
        list.add(new ProphetMenuItem(
                0,
                "নবীদের কাহিনী সমূহ",
                "Stories of the Prophets Overview",
                ProphetMenuItem.Type.OVERVIEW,
                null
        ));

        // 2-26. List of Prophets matching exact verbatim order from Screenshot 1
        for (ProphetStoryItem prophet : prophets) {
            list.add(new ProphetMenuItem(
                    prophet.getId(),
                    prophet.getBengaliName(),
                    prophet.getEnglishName(),
                    ProphetMenuItem.Type.PROPHET,
                    prophet
            ));
        }

        cachedMenuItems = list;
        return list;
    }

    public static synchronized List<ProphetOverviewTopicItem> getTopicsForProphet(int prophetId) {
        List<ProphetOverviewTopicItem> list = new ArrayList<>();

        if (prophetId == 1) {
            // হযরত আদম (আ:) - All 11 Chapters / Topics 100% Verbatim from User Screenshots & Request

            // 1. আদম (আ.)-এর সৃষ্টি
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "আদম (আ.)-এর সৃষ্টি",
                    "Creation of Adam (AS)",
                    "আদম (আ.)-এর সৃষ্টি আল্লাহর কুদরতের এক অনন্য নিদর্শন। আল্লাহ তাঁকে মাটি থেকে সৃষ্টি করে তাঁর মধ্যে রুহ ফুঁকে দিয়েছেন এবং তাঁকে সমস্ত সৃষ্টির মধ্যে সর্বোচ্চ সম্মানিত করেছেন। ফেরেশতারা আদম (আ.)-কে সিজদা করেছেন, যা প্রমাণ করে তাঁর বিশেষ মর্যাদা। এটি আমাদের স্মরণ করিয়ে দেয় যে, আমরা এক আল্লাহর সৃষ্টি এবং আমাদের জীবনের উদ্দেশ্য হলো তাঁকে চেনা ও তাঁর নির্দেশ পালন করা। আল্লাহ আদম (আ.)-কে তাঁর প্রতিনিধি হিসেবে সৃষ্টি করেছেন, যা মানবজাতির মর্যাদা ও দায়িত্বের প্রমাণ। এই সৃষ্টির পেছনে মানবজাতির জন্য রয়েছে গভীর শিক্ষা।\n\n"
                            + "আদম (আ.)-এর সৃষ্টির বিষয়ে কুরআন ও হাদিসে উল্লেখিত তথ্য সমৃদ্ধ এবং প্রাঞ্জল। নিচে কুরআন ও হাদিস থেকে বিষয়টি বিস্তারিত আলোচনা করা হলো:\n\n"
                            + "<b>আল্লাহ মাটির দ্বারা আদম (আ.)-কে সৃষ্টি করেছেন:</b>\n\n"
                            + "وَلَقَدْ خَلَقْنَا ٱلْإِنسَٰنَ مِن صَلْصَٰلٍۢ مِّنْ حَمَإٍۢ مَّسْنُونٍۢ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর অবশ্যই আমি মানুষকে সৃষ্টি করেছি শুষ্ক মাটির শব্দযুক্ত অংশ থেকে, যা ছিল কালো দুর্গন্ধযুক্ত কাদামাটি।\n"
                            + "<b>[সূরা আল-হিজর (১৫:২৬)]</b>\n\n"
                            + "<b>আল্লাহ আদমকে সৃষ্টি করার সময় ফেরেশতাদের জানিয়ে দিয়েছিলেন:</b>\n\n"
                            + "إِذْ قَالَ رَبُّكَ لِلْمَلَٰٓئِكَةِ إِنِّي خَٰلِقٌۢ بَشَرًۭا مِّن صَلْصَٰلٍۢ مِّنْ حَمَإٍۢ مَّسْنُونٍۢ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন তোমার প্রতিপালক ফেরেশতাদের বললেন, ‘আমি মাটির শুকনো ও কালো দুর্গন্ধযুক্ত কাদা থেকে মানুষ সৃষ্টি করব।\n"
                            + "<b>[সূরা আল-হিজর (১৫:২৮)]</b>\n\n"
                            + "<b>আদম (আ.)-কে আল্লাহ নিজের হাতে সৃষ্টি করেছেন:</b>\n\n"
                            + "قَالَ يَٰٓإِبْلِيسُ مَا مَنَعَكَ أَن تَسْجُدَ لِمَا خَلَقْتُ بِيَدَىَّ أَسْتَكْبَرْتَ أَمْ كُنتَ مِنَ ٱلْعَالِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ বললেন, ‘হে ইবলিস! আমি নিজ হাতে যাকে সৃষ্টি করেছি, তাকে সিজদা করতে তোমাকে কী বাধা দিল? তুমি কি অহংকারী, না তুমি (উচ্চ পদে) সম্মানিতদের অন্তর্ভুক্ত?\n"
                            + "<b>[সূরা সাদ (৩৮:৭৫)]</b>\n\n"
                            + "<b>হাদিসের আলোকে আদম (আ.)-এর সৃষ্টি:</b>\n\n"
                            + "<b>আদম (আ.)-এর আকার এবং আকৃতি:</b>\n\n"
                            + "إِنَّ اللَّهَ خَلَقَ آدَمَ عَلَى صُورَتِهِ، طُولُهُ سِتُّونَ ذِرَاعًا، فَلَمَّا خَلَقَهُ قَالَ: اذْهَبْ فَسَلِّمْ عَلَى أُولَئِكَ النَّفَرِ، وَهُمْ نَفَرٌ مِنَ الْمَلَائِكَةِ، فَاسْتَمِعْ مَا يُحَيُّونَكَ، فَإِنَّهَا تَحِيَّتُكَ وَتَحِيَّةُ ذُرِّيَّتِكَ، فَقَالَ: السَّلَامُ عَلَيْكُمْ، فَقَالُوا: السَّلَامُ عَلَيْكَ وَرَحْمَةُ اللَّهِ، فَزَادُوهُ وَرَحْمَةُ اللَّهِ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই আল্লাহ আদমকে নিজের চেহারায় সৃষ্টি করেছেন, তাঁর উচ্চতা ছিল ৬০ হাত। আল্লাহ তাঁকে সৃষ্টি করার পর আদেশ দিলেন, ‘ঐ ফেরেশতাদের কাছে যাও এবং তাদের সালাম দাও। তাদের কীভাবে সালাম জবাব দেয় শোনো, কেননা সেটাই হবে তোমার এবং তোমার বংশধরদের সালাম।’ তিনি বললেন, ‘আসসালামু আলাইকুম।’ তারা উত্তর দিল, ‘আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ।’ অতএব, ‘ওয়া রাহমাতুল্লাহ’ যুক্ত করে দেওয়া হলো।\n"
                            + "<b>[সহীহ বুখারী, হাদিস: ৩৩২৬; সহীহ মুসলিম, হাদিস: ২৮৪১]</b>\n\n"
                            + "<b>আদম (আ.)-কে পৃথিবীতে পাঠানোর কারণ:</b>\n\n"
                            + "إِنَّ اللَّهَ خَلَقَ آدَمَ مِنْ قَبْضَةٍ قَبْضَهَا مِنْ جَمِيعِ الأَرْضِ، فَجَاءَ بَنُو آدَمَ عَلَى قَدَرِ الأَرْضِ، جَاءَ مِنْهُمُ الأَحْمَرُ وَالأَبْيَضُ وَالأَسْوَدُ، وَبَيْنَ ذَلِكَ، وَالسَّهْلُ وَالْحَزْنُ، وَالْخَبِيثُ وَالطَّيِّبُ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ আদমকে পৃথিবীর বিভিন্ন স্থানের মাটি থেকে সৃষ্টি করেছেন। ফলে আদমের সন্তানরাও মাটির মতো বিভিন্ন বৈশিষ্ট্যের হয়; কারো গায়ের রং লাল, কারো সাদা, কারো কালো এবং কেউ এর মাঝামাঝি। কারো স্বভাব কোমল, কারো কঠোর; কেউ নেক, আর কেউ বদ।\n"
                            + "<b>[সুনান আত-তিরমিজি, হাদিস: ২৯৫৫; সুনান আবু দাউদ, হাদিস: ৪৬৯৩]</b>",
                    "The creation of Adam (AS) is a magnificent manifestation of Allah's divine power. Allah fashioned him from clay, breathed His spirit into him, and bestowed upon him the highest honor above all creation. The angels prostrated before Adam (AS), demonstrating his elevated status. It serves as a constant reminder that we are the creation of One Allah and our life's purpose is to know Him and obey His commands. Allah designated Adam (AS) as His vicegerent (Khalifah) on earth, which signifies the dignity and responsibility of mankind.\n\n"
                            + "Below is the comprehensive discussion from the Quran and authentic Hadith:\n\n"
                            + "<b>Allah Created Adam from Clay:</b>\n\n"
                            + "وَلَقَدْ خَلَقْنَا ٱلْإِنسَٰنَ مِن صَلْصَٰلٍۢ مِّنْ حَمَإٍۢ مَّسْنُونٍۢ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And We certainly created man out of clay from an altered black mud.\n"
                            + "<b>[Surah Al-Hijr (15:26)]</b>\n\n"
                            + "<b>Allah Informed the Angels Prior to Creation:</b>\n\n"
                            + "إِذْ قَالَ رَبُّكَ لِلْمَلَٰٓئِكَةِ إِنِّي خَٰلِقٌۢ بَشَرًۭا مِّن صَلْصَٰلٍۢ مِّنْ حَمَإٍۢ مَّسْنُونٍۢ\n\n"
                            + "<b>Translation:</b>\n"
                            + "When your Lord said to the angels, 'I will create a human being out of clay from an altered black mud.'\n"
                            + "<b>[Surah Al-Hijr (15:28)]</b>\n\n"
                            + "<b>Allah Created Adam with His Own Hands:</b>\n\n"
                            + "قَالَ يَٰٓإِبْلِيسُ مَا مَنَعَكَ أَن تَسْجُدَ لِمَا خَلَقْتُ بِيَدَىَّ أَسْتَكْبَرْتَ أَمْ كُنتَ مِنَ ٱلْعَالِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Allah said, 'O Iblis, what prevented you from prostrating to that which I created with My Hands? Were you arrogant, or are you of the exalted?'\n"
                            + "<b>[Surah Sad (38:75)]</b>\n\n"
                            + "<b>Creation of Adam in Authentic Hadith:</b>\n\n"
                            + "<b>Adam's Stature and Greeting:</b>\n"
                            + "Allah created Adam in His image, sixty cubits tall, and commanded him to greet the angels with 'Assalamu Alaykum', establishing the universal Islamic greeting.\n"
                            + "<b>[Sahih al-Bukhari: 3326; Sahih Muslim: 2841]</b>\n\n"
                            + "<b>Diversity of Mankind from the Earth's Soils:</b>\n"
                            + "Allah fashioned Adam from handfuls of soil taken from across the whole earth, which is why descendants of Adam have diverse skin colors and varying temperaments.\n"
                            + "<b>[Jami' at-Tirmidhi: 2955; Sunan Abi Dawud: 4693]</b>"
            ));

            // 2. ফেরেশতাদের সিজদার আদেশ
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "ফেরেশতাদের সিজদার আদেশ",
                    "Command to the Angels to Prostrate",
                    "আল্লাহ তাআলা পৃথিবীতে একজন প্রতিনিধি (খলিফা) সৃষ্টি করার পরিকল্পনা করেন এবং এই পরিকল্পনার কথা ফেরেশতাদের জানান। ফেরেশতারা বলল, \"আপনি কি সেখানে এমন কাউকে সৃষ্টি করবেন, যারা বিশৃঙ্খলা সৃষ্টি করবে এবং রক্তপাত করবে, আর আমরা তো আপনার প্রশংসা ও পবিত্রতা ঘোষণা করছি।\" আল্লাহ তাদের উত্তর দিলেন \"আমি যা জানি, তা তোমরা জান না।\" এরপর আল্লাহ আদম (আ.)-কে শুকনো মাটি থেকে সৃষ্টি করলেন এবং তার মধ্যে নিজের রূহ ফুঁকে দিলেন। এরপর আল্লাহ আদম (আ.)-কে বিভিন্ন জিনিসের নাম শিখিয়ে তাদের ফেরেশতাদের সামনে উপস্থাপন করলেন। যখন ফেরেশতারা আদম (আ.)-এর জ্ঞান এবং আল্লাহর পরিকল্পনার গভীরতা বুঝল, তখন আল্লাহ আদম (আ.)-কে সিজদা করার আদেশ দিলেন।\n\n"
                            + "ফেরেশতারা আদেশ পালন করল এবং আদম (আ.)-কে সিজদা করল। তবে ইবলিস, যাকে আল্লাহ ফেরেশতাদের সঙ্গে স্থান দিয়েছিলেন, সিজদা করতে অস্বীকার করল। সে বলল, \"আমি তার চেয়ে উত্তম; আমাকে আপনি আগুন থেকে সৃষ্টি করেছেন এবং তাকে সৃষ্টি করেছেন মাটি থেকে।\" এই অহংকারের জন্য ইবলিস আল্লাহর অভিশাপ লাভ করে এবং জান্নাত থেকে বিতাড়িত হয়। সে প্রতিশ্রুতি করে যে, আদম (আ.)-এর সন্তানদের পথভ্রষ্ট করবে।\n\n"
                            + "আদম (আ.)-এর প্রতি সিজদার আদেশ ছিল আদমের মর্যাদা এবং আল্লাহর আদেশের প্রতি আনুগত্যের পরীক্ষা। ফেরেশতারা আল্লাহর আদেশ মেনে চললেও ইবলিস তার অহংকার ও অবাধ্যতার কারণে ধ্বংসপ্রাপ্ত হয়।\n\n"
                            + "<b>কুরআনে বর্ণিত ফেরেশতাদের সিজদার আদেশ:</b>\n\n"
                            + "<b>আদম (আ.)-কে সিজদা করার নির্দেশ:</b>\n\n"
                            + "وَإِذْ قُلْنَا لِلْمَلَٰٓئِكَةِ ٱسْجُدُوا۟ لِءَادَمَ فَسَجَدُوٓا۟ إِلَّآ إِبْلِيسَ أَبَىٰ وَٱسْتَكْبَرَ وَكَانَ مِنَ ٱلْكَٰفِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর যখন আমি ফেরেশতাদের বললাম, ‘আদমকে সিজদা করো,’ তখন তারা সিজদা করল; তবে ইবলিস বিরত থাকল, সে অহংকার করল এবং কাফেরদের অন্তর্ভুক্ত হয়ে গেল।\n"
                            + "<b>[সূরা আল-বাকারা (২:৩৪)]</b>\n\n"
                            + "<b>ফেরেশতাদের সিজদার কারণ:</b>\n\n"
                            + "وَإِذْ قَالَ رَبُّكَ لِلْمَلَٰٓئِكَةِ إِنِّى خَٰلِقٌۭ بَشَرًۭا مِّن صَلْصَٰلٍۢ مِّنْ حَمَإٍۢ مَّسْنُونٍۢ . فَإِذَا سَوَّيْتُهُۥ وَنَفَخْتُ فِيهِ مِن رُّوحِى فَقَعُوا۟ لَهُۥ سَٰجِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর যখন তোমার প্রতিপালক ফেরেশতাদের বললেন, ‘আমি মাটির শুকনো ও কালো দুর্গন্ধযুক্ত কাদা থেকে মানুষ সৃষ্টি করব। যখন আমি তাকে পূর্ণাঙ্গ করব এবং তার মধ্যে আমার রূহ ফুঁকে দেব, তখন তোমরা তার সম্মুখে সিজদা করো।\n"
                            + "<b>[সূরা আল-হিজর (১৫:২৮-২৯)]</b>\n\n"
                            + "<b>ইবলিসের অস্বীকৃতি এবং আল্লাহর প্রশ্ন:</b>\n\n"
                            + "قَالَ مَا مَنَعَكَ أَلَّا تَسْجُدَ إِذْ أَمَرْتُكَ ۖ قَالَ أَنَا۠ خَيْرٌۭ مِّنْهُ ۖ خَلَقْتَنِى مِن نَّارٍۢ وَخَلَقْتَهُۥ مِن طِينٍۢ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ বললেন, ‘আমি যখন তোমাকে আদেশ দিলাম, তখন তোমাকে সিজদা করতে কী বাধা দিল?’ সে বলল, ‘আমি তার চেয়ে উত্তম; আমাকে আপনি আগুন থেকে সৃষ্টি করেছেন, আর তাকে সৃষ্টি করেছেন মাটি থেকে।\n"
                            + "<b>[সূরা আল-আ’রাফ (৭:১২)]</b>\n\n"
                            + "<b>হাদিসে বর্ণিত ফেরেশতাদের সিজদার আদেশ:</b>\n\n"
                            + "<b>ইবলিসের অহংকার ও বিদ্রোহ:</b>\n\n"
                            + "لَمَّا خَلَقَ اللَّهُ آدَمَ وَنَفَخَ فِيهِ الرُّوحَ أَمَرَ الْمَلَائِكَةَ أَنْ يَسْجُدُوا لَهُ فَسَجَدُوا إِلَّا إِبْلِيسَ أَبَى أَنْ يَكُونَ مَعَ السَّاجِدِينَ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ আদমকে সৃষ্টি করার পর তার মধ্যে রূহ ফুঁকে দিলেন এবং ফেরেশতাদের আদেশ দিলেন তাকে সিজদা করতে। ফেরেশতারা সিজদা করল, কিন্তু ইবলিস সিজদাকারীদের সঙ্গে থাকতে অস্বীকৃতি জানাল।\n"
                            + "<b>[সহীহ মুসলিম, হাদিস: ২৬১১]</b>\n\n"
                            + "<b>সিজদা ছিল সম্মান প্রদর্শনের জন্য:</b>\n"
                            + "ইসলামী ফিকহ ও তাফসিরবিদদের মতে, ফেরেশতাদের এই সিজদা ছিল আল্লাহর আদেশ পালন এবং আদম (আ.)-এর প্রতি সম্মান প্রদর্শনের জন্য। এটি ইবাদতের সিজদা ছিল না, বরং এক প্রকার আনুগত্যের প্রকাশ।",
                    "Allah planned to create a vicegerent on earth and announced this to the angels. When the angels witnessed the knowledge bestowed upon Adam, Allah commanded them to prostrate before him as an act of veneration and obedience to divine decree.\n\n"
                            + "The angels obeyed completely, but Iblis refused out of pride, declaring: 'I am better than him; You created me from fire and created him from clay.' Due to this arrogance, Iblis was banished and cursed.\n\n"
                            + "<b>Quranic Evidences:</b>\n\n"
                            + "<b>Surah Al-Baqarah (2:34):</b> And when We said to the angels, 'Prostrate before Adam'; so they prostrated, except for Iblis. He refused and was arrogant and became of the disbelievers.\n\n"
                            + "<b>Surah Al-Hijr (15:28-29):</b> 'So when I have proportioned him and breathed into him of My spirit, then fall down in prostration to him.'\n\n"
                            + "<b>Nature of the Prostration:</b>\n"
                            + "Islamic scholars unanimously state this was a prostration of honor and greeting under divine command, not worship (Sujud al-Ibadah)."
            ));

            // 3. জান্নাতে অবস্থান
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "জান্নাতে অবস্থান",
                    "Dwelling in Paradise",
                    "আল্লাহ তাআলা আদম (আ.)-কে মাটি থেকে সৃষ্টি করার পর জান্নাতে স্থান দিলেন। এটি ছিল এক বিশুদ্ধ, শান্তি ও সম্পূর্ণ সুখের স্থান। জান্নাতের মধ্যে তিনি সমস্ত প্রকার নিয়ামত উপভোগ করতেন। আল্লাহ তাঁর একাকিত্ব দূর করার জন্য হাওয়া (আ.)-কে তাঁর জীবনসঙ্গী হিসেবে সৃষ্টি করেন। জান্নাত ছিল এমন এক স্থান যেখানে কোনো কষ্ট বা ক্লান্তি ছিল না। সেখানে আদম (আ.) এবং হাওয়া (আ.) মুক্তভাবে বিচরণ করতেন এবং আল্লাহর প্রদত্ত সকল নিয়ামত ভোগ করতেন। তাদের জন্য সবকিছু সহজলভ্য ছিল। জান্নাতে তারা সুখ ও শান্তিপূর্ণ সময় কাটাতেন। আল্লাহ তাদের জান্নাতে সবকিছু উপভোগের অনুমতি দেন। তবে এই জান্নাত ছিল চিরস্থায়ী বসবাসের জন্য নয়; বরং এটি ছিল তাদের পরীক্ষা এবং তাদের সৃষ্টির উদ্দেশ্য পূরণের জন্য একটি আরম্ভস্থল। আদম (আ.)-এর জান্নাতে অবস্থান আমাদের মনে করিয়ে দেয় যে জান্নাত আল্লাহর অসীম নিয়ামতপূর্ণ স্থান, এবং মানুষের সৃষ্টির মূল লক্ষ্য হলো আল্লাহর আদেশ পালন করে তাঁর সন্তুষ্টি অর্জন করা।\n\n"
                            + "<b>জান্নাতে আদম (আ.) ও হাওয়া (আ.):</b>\n"
                            + "আল্লাহ তাআলা আদম (আ.)-কে জান্নাতে বসবাস করতে বলেন এবং তার জন্য সকল কিছু হালাল করেন। একই সাথে আদমের একাকিত্ব দূর করার জন্য তাঁর জীবনসঙ্গী হাওয়া (আ.)-কে সৃষ্টি করেন।\n\n"
                            + "<b>কুরআনে বর্ণনা:</b>\n\n"
                            + "وَقُلْنَا يَٰٓـَٔادَمُ ٱسْكُنْ أَنتَ وَزَوْجُكَ ٱلْجَنَّةَ وَكُلَا مِنْهَا رَغَدًا حَيْثُ شِئْتُمَا وَلَا تَقْرَبَا هَٰذِهِ ٱلشَّجَرَةَ فَتَكُونَا مِنَ ٱلظَّٰلِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আমি বললাম, ‘হে আদম! তুমি এবং তোমার স্ত্রী জান্নাতে বসবাস করো এবং এখানকার ফল-ফলাদি ইচ্ছামতো উপভোগ করো। কিন্তু এই গাছের কাছেও যেয়ো না, নতুবা তোমরা জালিমদের অন্তর্ভুক্ত হয়ে যাবে।\n"
                            + "<b>[সূরা আল-বাকারা (২:৩৫)]</b>",
                    "After creating Adam (AS), Allah placed him in Paradise, an abode of absolute purity, tranquility, and delight. To ease his solitude, Allah created Hawwa (Eve) as his companion.\n\n"
                            + "They resided in utmost bliss, enjoying all pure provisions of Paradise freely.\n\n"
                            + "<b>Quranic Reference [Surah Al-Baqarah: 35]:</b>\n"
                            + "And We said, 'O Adam, dwell, you and your wife, in Paradise and eat therefrom in ease and abundance from wherever you will. But do not approach this tree, lest you be among the wrongdoers.'"
            ));

            // 4. হারাম বৃক্ষের নিষেধাজ্ঞা
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "হারাম বৃক্ষের নিষেধাজ্ঞা",
                    "Prohibition of the Forbidden Tree",
                    "আল্লাহ তাআলা আদম (আ.)-কে জান্নাতে স্থান দিলেন এবং জান্নাতের সমস্ত কিছু থেকে উপভোগ করার অনুমতি দিলেন। তবে আল্লাহ আদম (আ.) এবং হাওয়া (আ.)-কে একটি নির্দিষ্ট গাছের ফল খেতে নিষেধ করলেন। এটি ছিল একটি পরীক্ষা। শয়তান (ইবলিস) তাদের প্রলুব্ধ করে এই নিষেধাজ্ঞা ভঙ্গ করায় বাধ্য করে। এর ফলে তারা জান্নাত থেকে পৃথিবীতে প্রেরিত হন। এই ঘটনা কুরআন এবং হাদিসে বিশদভাবে বর্ণিত হয়েছে।\n\n"
                            + "<b>কোরআনের আলোকে হারাম বৃক্ষের নিষেধাজ্ঞা:</b>\n\n"
                            + "<b>নিষেধাজ্ঞার ঘোষণা:</b>\n\n"
                            + "وَقُلْنَا يَٰٓـَٔادَمُ ٱسْكُنْ أَنتَ وَزَوْجُكَ ٱلْجَنَّةَ وَكُلَا مِنْهَا رَغَدًا حَيْثُ شِئْتُمَا وَلَا تَقْرَبَا هَٰذِهِ ٱلشَّجَرَةَ فَتَكُونَا مِنَ ٱلظَّٰلِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আমি বললাম, ‘হে আদম! তুমি এবং তোমার স্ত্রী জান্নাতে বসবাস করো এবং এখানকার ফল-ফলাদি ইচ্ছামতো উপভোগ করো। কিন্তু এই গাছের কাছেও যেয়ো না, নতুবা তোমরা জালিমদের অন্তর্ভুক্ত হয়ে যাবে।\n"
                            + "<b>[সূরা আল-বাকারা (২:৩৫)]</b>\n\n"
                            + "<b>ইবলিসের প্ররোচনা:</b>\n\n"
                            + "فَوَسْوَسَ لَهُمَا ٱلشَّيْطَٰنُ لِيُبْدِىَ لَهُمَا مَا وُورِىَ عَنْهُمَا مِن سَوْءَٰتِهِمَا وَقَالَ مَا نَهَىٰكُمَا رَبُّكُمَا عَنْ هَٰذِهِ ٱلشَّجَرَةِ إِلَّآ أَن تَكُونَا مَلَكَيْنِ أَوْ تَكُونَا مِنَ ٱلْخَٰلِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর শয়তান তাদের কুমন্ত্রণা দিল, যাতে তাদের লজ্জাস্থান যা তাদের থেকে গোপন ছিল তা প্রকাশ পায়। সে বলল, ‘তোমাদের প্রতিপালক তোমাদের এ গাছ থেকে নিষেধ করেননি, শুধুমাত্র এ কারণে যে, তোমরা না ফেরেশতা হয়ে যাও বা চিরকাল বেঁচে থাকো।\n"
                            + "<b>[সূরা আল-আ'রাফ (৭:২০)]</b>\n\n"
                            + "<b>নিষেধ ভঙ্গ এবং পরিণতি:</b>\n\n"
                            + "فَأَكَلَا مِنْهَا فَبَدَتْ لَهُمَا سَوْءَٰتُهُمَا وَطَفِقَا يَخْصِفَانِ عَلَيْهِمَا مِن وَرَقِ ٱلْجَنَّةِ وَنَادَىٰهُمَا رَبُّهُمَا أَلَمْ أَنْهَكُمَا عَن تِلْكُمَا ٱلشَّجَرَةِ وَأَقُل لَّكُمَآ إِنَّ ٱلشَّيْطَٰنَ لَكُمَا عَدُوٌّۭ مُّبِينٌۭ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর তারা উভয়ে ওই গাছের ফল খেয়ে ফেলল। ফলে তাদের লজ্জাস্থান প্রকাশ হয়ে গেল, এবং তারা জান্নাতের পাতা দিয়ে নিজেদের ঢাকতে শুরু করল। তখন তাদের রব তাদের ডেকে বললেন, ‘আমি কি তোমাদের এই গাছের কাছে যেতে নিষেধ করিনি? এবং তোমাদের বলিনি যে শয়তান তোমাদের প্রকাশ্য শত্রু?\n"
                            + "<b>[সূরা আল-আ'রাফ (৭:২২)]</b>\n\n"
                            + "<b>হাদিসের আলোকে হারাম বৃক্ষের নিষেধাজ্ঞা:</b>\n\n"
                            + "<b>ইবলিসের ধোঁকা:</b>\n"
                            + "রাসূলুল্লাহ (সা.) ইবলিসের প্রতারণা ও আদম (আ.)-এর ভুল নিয়ে বলেন:\n\n"
                            + "لَمَّا زَيَّنَ لَهُمَا ٱلشَّيْطَانُ فَأَكَلَا مِنَ ٱلشَّجَرَةِ قَالَ ٱللَّهُ تَعَالَىٰ لِءَادَمَ: إِنِّي جَعَلْتُكَ فِي جَنَّتِي وَنَهَيْتُكَ عَنْ هَٰذِهِ ٱلشَّجَرَةِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন শয়তান তাদের কাছে ওই গাছের ফল খাওয়াকে আকর্ষণীয় করে তুলল এবং তারা তা খেয়ে ফেলল, তখন আল্লাহ আদমকে বললেন, ‘আমি কি তোমাকে আমার জান্নাতে স্থাপন করিনি এবং তোমাকে এই গাছ থেকে নিষেধ করিনি?\n"
                            + "<b>[সহীহ বুখারী, হাদিস: ৩৩৩১]</b>\n\n"
                            + "আদম (আ.) ও হাওয়া (আ.)-এর এই ঘটনা মানবজাতির জন্য একটি শিক্ষা। এটি আমাদের স্মরণ করিয়ে দেয় যে শয়তান ধোঁকা দিয়ে মানুষকে আল্লাহর আদেশ ভঙ্গ করতে প্রলুব্ধ করে। কিন্তু আল্লাহ দয়ালু এবং ক্ষমাশীল। সত্যিকার তওবা করলে তিনি তা কবুল করেন।",
                    "Allah permitted Adam and Hawwa to partake of everything in Paradise except for one specific tree as a test of obedience.\n\n"
                            + "Satan deceived them with false vows, claiming the fruit would grant them eternal life or make them angels. Upon tasting the tree, their awareness of nakedness appeared, and they covered themselves with the leaves of Paradise.\n\n"
                            + "<b>Key Lesson:</b> Satan deceives with grand false promises, but sincere repentance always finds Allah's boundless mercy."
            ));

            // 5. শয়তানের প্ররোচনা
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "শয়তানের প্ররোচনা",
                    "Deception and Temptation of Satan",
                    "আল্লাহ তাআলা আদম (আ.) এবং হাওয়া (আ.)-কে জান্নাতে স্থান দিয়ে বলেছিলেন যে, তারা জান্নাতের সমস্ত নিয়ামত উপভোগ করতে পারে, তবে একটি বিশেষ গাছের কাছে যেতে এবং এর ফল খেতে নিষেধ করেন। শয়তান, যাকে জান্নাত থেকে বিতাড়িত করা হয়েছিল, তাদের প্ররোচিত করার পরিকল্পনা করল। শয়তান আদম (আ.) এবং হাওয়া (আ.)-কে কুমন্ত্রণা দিয়ে বলল, “এই গাছের ফল খেলে তোমরা চিরকাল জান্নাতে থাকতে পারবে এবং মরবে না, বরং ফেরেশতা হয়ে যাবে।” শয়তান তার কথা বিশ্বাসযোগ্য করতে আল্লাহর নামে মিথ্যা শপথও করল। এই প্রতারণার ফলে আদম (আ.) এবং হাওয়া (আ.) সেই গাছের ফল খেয়ে ফেললেন।\n\n"
                            + "ফল খাওয়ার সাথে সাথেই তাদের লজ্জাস্থান প্রকাশ পেল। তারা জান্নাতের পাতা দিয়ে নিজেদের ঢাকতে শুরু করলেন। তখন আল্লাহ তাআলা তাদের ডেকে বললেন, “আমি কি তোমাদের এই গাছের কাছে যেতে নিষেধ করিনি? আমি কি বলিনি যে শয়তান তোমাদের প্রকাশ্য শত্রু?” এরপর আল্লাহ আদম (আ.) এবং হাওয়া (আ.)-কে জান্নাত থেকে পৃথিবীতে পাঠিয়ে দিলেন এবং পৃথিবীতে তাদের বসবাসের নির্দেশ দিলেন।\n\n"
                            + "فَوَسْوَسَ إِلَيْهِ ٱلشَّيْطَٰنُ قَالَ يَٰٓـَٔادَمُ هَلْ أَدُلُّكَ عَلَىٰ شَجَرَةِ ٱلْخُلْدِ وَمُلْكٍۢ لَّا يَبْلَىٰ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তখন শয়তান তাকে কুমন্ত্রণা দিয়ে বলল, ‘হে আদম! আমি কি তোমাকে চিরজীবনের গাছ এবং এমন এক রাজ্যের সন্ধান দেব, যা কখনো বিনষ্ট হবে না?\n"
                            + "<b>[সূরা ত্বা-হা (২০:১২০)]</b>\n\n"
                            + "<b>শয়তানের প্রলোভন ও মিথ্যা প্রতিশ্রুতি:</b>\n\n"
                            + "وَقَالَ مَا نَهَىٰكُمَا رَبُّكُمَا عَنْ هَٰذِهِ ٱلشَّجَرَةِ إِلَّآ أَن تَكُونَا مَلَكَيْنِ أَوْ تَكُونَا مِنَ ٱلْخَٰلِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "সে বলল, ‘তোমাদের প্রতিপালক তোমাদের এ গাছ থেকে নিষেধ করেননি, শুধুমাত্র এ কারণে যে, তোমরা না ফেরেশতা হয়ে যাও অথবা চিরকাল বেঁচে থাকো।\n"
                            + "<b>[সূরা আল-আ'রাফ (৭:২০)]</b>\n\n"
                            + "<b>ধোঁকা ও প্রতারণা:</b>\n\n"
                            + "فَدَلَّىٰهُمَا بِغُرُورٍۢ ۚ فَلَمَّا ذَاقَا ٱلشَّجَرَةَ بَدَتْ لَهُمَا سَوْءَٰতُهُمَا وَطَفِقَا يَخْصِفَانِ عَلَيْهِمَا مِن وَرَقِ ٱلْجَنَّةِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর সে তাদের ধোঁকায় ফেলল। যখন তারা ওই গাছের ফল আস্বাদন করল, তখন তাদের লজ্জাস্থান প্রকাশ হয়ে গেল এবং তারা জান্নাতের পাতা দিয়ে নিজেদের ঢাকতে শুরু করল।\n"
                            + "<b>[সূরা আল-আ'রাফ (৭:২২)]</b>\n\n"
                            + "রাসূলুল্লাহ (সা.) বলেন,\n\n"
                            + "إِنَّ الشَّيْطَانَ يَجْرِي مِنَ الْإِنْسَانِ مَجْرَى الدَّمِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই শয়তান মানুষের শরীরে রক্তের মতো প্রবাহিত হয়।”\n"
                            + "<b>[সহীহ বুখারী, হাদিস: ৭১৭২; সহীহ মুসলিম, হাদিস: ২১৭৫]</b>\n\n"
                            + "<b>শিক্ষা:</b>\n"
                            + "• <b>শয়তানের ধোঁকা থেকে সাবধান থাকা:</b> শয়তান মানুষের প্রকাশ্য শত্রু, এবং তার উদ্দেশ্য হলো মানুষকে আল্লাহর আদেশ অমান্য করতে প্রলুব্ধ করা।\n"
                            + "• <b>তওবা এবং আল্লাহর দয়া:</b> শয়তানের প্ররোচনা সত্ত্বেও, আল্লাহর কাছে ফিরে যাওয়া এবং তওবা করা হলে আল্লাহ দয়া করে ক্ষমা করেন।\n"
                            + "• <b>শয়তানের মিথ্যা প্রতিশ্রুতি:</b> শয়তান মানুষকে প্রলোভন দেখায়, কিন্তু তার প্রতিশ্রুতিগুলো সবই মিথ্যা।\n\n"
                            + "আল্লাহ তাআলা বলেন:\n\n"
                            + "إِنَّ ٱلشَّيْطَٰنَ لَكُمْ عَدُوٌّۭ فَٱتَّخِذُوهُ عَدُوًّا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই শয়তান তোমাদের শত্রু, সুতরাং তোমরা তাকে শত্রু হিসেবেই গ্রহণ করো।\n"
                            + "<b>[সূরা ফাতির (৩৫:৬)]</b>",
                    "Satan was determined to lead humanity astray out of envy. He approached Adam with deceptive whispers and swore falsely by Allah.\n\n"
                            + "<b>The Prophet (peace be upon him) warned:</b>\n"
                            + "'Satan circulates in the human body like blood.' [Sahih al-Bukhari: 7172; Sahih Muslim: 2175]\n\n"
                            + "<b>Core Lessons:</b> Always beware of Satan's cunning deception and hold firmly to sincere repentance."
            ));

            // 6. জান্নাত থেকে নির্বাসন
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "জান্নাত থেকে নির্বাসন",
                    "Descent from Paradise to Earth",
                    "হযরত আদম (আলাইহিস সালাম) এবং তাঁর সঙ্গিনী হাওয়া (আলাইহাস সালাম) মানবজাতির আদি পিতা-মাতা। আল্লাহ তাআলা তাদের সৃষ্টি করেছেন এবং জান্নাতে স্থান দিয়েছেন। কিন্তু ইবলিসের কুমন্ত্রণা এবং নিষেধাজ্ঞা লঙ্ঘনের কারণে তারা পৃথিবীতে নির্বাসিত হন। কুরআনুল কারিম এবং হাদিসে এই ঘটনার বিস্তারিত বিবরণ রয়েছে।\n\n"
                            + "আল্লাহ তাআলা বলেছেন:\n\n"
                            + "وَإِذْ قَالَ رَبُّكَ لِلْمَلَائِكَةِ إِنِّي جَاعِلٌ فِي ٱلۡأَرۡضِ خَلِيفَةٗ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর তোমার প্রভু ফেরেশতাদের বললেন, আমি পৃথিবীতে একজন প্রতিনিধি (খলিফা) সৃষ্টি করতে যাচ্ছি।\n"
                            + "<b>[সূরা বাকারা, আয়াত: ৩০]</b>\n\n"
                            + "আল্লাহ তাআলা আদম (আ.)-কে মাটি থেকে সৃষ্টি করেন এবং তার মধ্যে নিজের পক্ষ থেকে আত্মা ফুঁকে দেন:\n\n"
                            + "فَإِذَا سَوَّيۡتُهُۥ وَنَفَخۡتُ فِيهِ مِن رُّوحِي فَقَعُواْ لَهُۥ سَٰجِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর যখন আমি তাকে পরিপূর্ণ করলাম এবং তার মধ্যে আমার পক্ষ থেকে আত্মা ফুঁকে দিলাম, তখন তোমরা তার সামনে সিজদায় লুটিয়ে পড়ো।\n"
                            + "<b>[সূরা সাদ, আয়াত: ৭২]</b>\n\n"
                            + "<b>শয়তানের অমান্যতা:</b>\n"
                            + "আল্লাহ আদম (আ.)-কে সিজদা করার নির্দেশ দিলে ইবলিস অহংকার করে অমান্য করে:\n\n"
                            + "قَالَ أَنَا۠ خَيۡرٞ مِّنۡهُۖ خَلَقۡتَنِي مِن نَّارٖ وَخَلَقۡتَهُۥ مِن طِينٖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "সে বলল, আমি তার চেয়ে উত্তম। তুমি আমাকে আগুন থেকে সৃষ্টি করেছ, আর তাকে সৃষ্টি করেছ মাটি থেকে।\n"
                            + "<b>[সূরা সাদ, আয়াত: ৭৬]</b>\n\n"
                            + "এর ফলে ইবলিস আল্লাহর রহমত থেকে বঞ্চিত হয় এবং তাকে অভিশপ্ত ঘোষণা করা হয়:\n\n"
                            + "قَالَ ٱخۡرُجۡ مِنۡهَا فَإِنَّكَ رَجِيمٞۖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তিনি বললেন, তুমি এখান থেকে বের হয়ে যাও, তুমি অভিশপ্ত।\n"
                            + "<b>[সূরা সাদ, আয়াত: ৭৭]</b>\n\n"
                            + "আল্লাহ তাআলা আদম (আ.) এবং তার স্ত্রী হাওয়া (আ.)-কে জান্নাতে স্থান দেন এবং একটি নির্দিষ্ট গাছ থেকে দূরে থাকার নির্দেশ দেন:\n\n"
                            + "وَقُلۡنَا يَٰٓـَٔادَمُ ٱسۡكُنۡ أَنتَ وَزَوۡجُكَ ٱلۡجَنَّةَ وَكُلَا مِنۡهَا رَغَدًا حَيۡثُ شِئۡتُمَاۖ وَلَا تَقۡرَبَا هَٰذِهِ ٱلشَّجَرَةَ فَتَكُونَا مِنَ ٱلظَّٰلِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আমি বললাম, হে আদম! তুমি ও তোমার স্ত্রী জান্নাতে বসবাস কর এবং এর যে কোনো জায়গা থেকে খাও। কিন্তু এই গাছের কাছে যেয়ো না, তা না হলে তোমরা সীমালঙ্ঘনকারীদের অন্তর্ভুক্ত হবে।\n"
                            + "<b>[সূরা বাকারা, আয়াত: ৩৫]</b>\n\n"
                            + "শয়তান তাদেরকে প্ররোচিত করে বলে যে, যদি তারা ওই গাছের ফল খায়, তবে তারা চিরস্থায়ী হয়ে যাবে\n\n"
                            + "فَوَسۡوَسَ إِلَيۡهِ ٱلشَّيۡطَٰنُ قَالَ يَٰٓـَٔادَمُ هَلۡ أَدُلُّكَ عَلَىٰ شَجَرَةِ ٱلۡخُلۡدِ وَمُلۡكٖ لَّا يَبۡلَىٰ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তখন শয়তান তাকে কুমন্ত্রণা দিয়ে বলল, হে আদম! আমি কি তোমাকে চিরস্থায়ী জীবনের বৃক্ষ এবং এমন রাজ্যের দিকে ইঙ্গিত দেব, যা কখনো ক্ষয়প্রাপ্ত হবে না?\"\n"
                            + "<b>[সূরা ত্বহা, আয়াত: ১২০]</b>\n\n"
                            + "আদম ও হাওয়া গাছের ফল খেয়ে ফেলে এবং তাদের পোশাক খুলে যায়।\n\n"
                            + "فَأَكَلاَ مِنۡهَا فَبَدَتۡ لَهُمَا سَوۡءَٰتُهُمَا وَطَفِقَا يَخۡصِفَانِ عَلَيْهِمَا مِن وَرَقِ ٱلۡجَنَّةِۚ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা উভয়ে সেই গাছের ফল খেয়ে ফেলল। ফলে তাদের লজ্জাস্থান প্রকাশ হয়ে পড়ল এবং তারা জান্নাতের পাতা দিয়ে নিজেদের আবৃত করতে লাগল।\n"
                            + "<b>[সূরা আল-আ'রাফ, আয়াত: ২২]</b>\n\n"
                            + "<b>তাওবা ও ক্ষমা প্রার্থনা:</b>\n"
                            + "তারা তাদের ভুল বুঝতে পেরে আল্লাহর কাছে ক্ষমা প্রার্থনা করেন:\n\n"
                            + "قَالَا رَبَّنَا ظَلَمۡنَآ أَنفُسَنَا وَإِن لَّمۡ تَغۡفِرۡ لَنَا وَتَرۡحَمۡنَا لَنَكُونَنَّ مِنَ ٱلۡخَٰسِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা বলল, হে আমাদের প্রভু! আমরা নিজেদের প্রতি জুলুম করেছি। যদি তুমি আমাদের ক্ষমা না কর এবং আমাদের প্রতি দয়া না কর, তবে অবশ্যই আমরা ক্ষতিগ্রস্তদের অন্তর্ভুক্ত হয়ে যাব।\n"
                            + "<b>[সূরা আল-আ'রাফ, আয়াত: ২৩]</b>\n\n"
                            + "জান্নাত থেকে নির্বাসন। আল্লাহ তাদের পৃথিবীতে নামিয়ে দেন এবং বলেন, এখানে তোমাদের জন্য এক নির্দিষ্ট সময় পর্যন্ত বাস এবং জীবনযাপনকরতে হবে\n\n"
                            + "قَالَ ٱهۡبِطُواْ بَعۡضُكُمۡ لِبَعۡضٍ عَدُوّٞۖ وَلَكُمۡ فِي ٱلۡأَرۡضِ مُسۡتَقَرّٞ وَمَتَٰعٌ إِلَىٰ حِينٖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তিনি বললেন, তোমরা নেমে যাও। তোমরা একে অপরের শত্রু। আর তোমাদের জন্য পৃথিবীতে নির্দিষ্ট সময় পর্যন্ত স্থায়ী বাসস্থান এবং জীবনযাপনের সামগ্রী রয়েছে।\n"
                            + "<b>[সূরা আল-আ'রাফ, আয়াত: ২৪]</b>\n\n"
                            + "রাসূলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) বলেন,\n\n"
                            + "إنَّ آدَمَ اجْتَذَبَهُ الشَّيْطَانُ، وَفَاتَهُ الأَمْرُ، ثُمَّ تَابَ اللَّهُ عَلَيْهِ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই আদমকে শয়তান প্ররোচিত করেছিল এবং তিনি ভুল করেছিলেন। এরপর আল্লাহ তাকে ক্ষমা করেছিলেন।\n"
                            + "<b>[সহীহ মুসলিম, হাদিস: ২৭৬৮]</b>",
                    "Following their repentance, Allah decreed that Adam and Hawwa descend to earth, where they were appointed as vicegerents to build human civilization under divine guidance.\n\n"
                            + "<b>Quranic Evidences:</b> Surah Al-Baqarah (30, 35), Surah Sad (72, 76, 77), Surah Ta-Ha (120), Surah Al-A'raf (22, 23, 24)."
            ));

            // 7. তওবা ও ক্ষমা প্রার্থনা
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "তওবা ও ক্ষমা প্রার্থনা",
                    "Repentance and Divine Forgiveness",
                    "হযরত আদম (আলাইহিস সালাম) এবং হাওয়া (আলাইহাস সালাম) আল্লাহর নিষেধ অমান্য করে জান্নাতে একটি নিষিদ্ধ গাছের ফল খেয়ে ফেলেছিলেন। পরবর্তীতে তারা তাদের ভুল বুঝতে পেরে আন্তরিকভাবে আল্লাহর কাছে তওবা করেন এবং ক্ষমা প্রার্থনা করেন। এই ঘটনা তওবার গুরুত্ব এবং আল্লাহর দয়ার অসীমতার একটি বড় উদাহরণ। কুরআন এবং হাদিসে এটি স্পষ্টভাবে উল্লেখিত হয়েছে।\n\n"
                            + "<b>তওবার দোয়া কুরআনে উল্লেখিত:</b>\n\n"
                            + "قَالَا رَبَّنَا ظَلَمْنَا أَنفُسَنَا وَإِن لَّمْ تَغْفِرْ لَنَا وَتَرْحَمْنَا لَنَكُونَنَّ مِنَ ٱلْخَٰسِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা বলল, হে আমাদের প্রভু! আমরা নিজেদের প্রতি জুলুম করেছি। যদি তুমি আমাদের ক্ষমা না কর এবং আমাদের প্রতি দয়া না কর, তবে অবশ্যই আমরা ক্ষতিগ্রস্তদের অন্তর্ভুক্ত হয়ে যাব।\"\n"
                            + "<b>[সূরা আল-আ'রাফ, আয়াত: ২৩]</b>\n\n"
                            + "<b>আল্লাহর ক্ষমা এবং দয়া:</b>\n"
                            + "তাদের এই আন্তরিক তওবার কারণে আল্লাহ তাআলা তাদের ক্ষমা করেন। তবে তিনি নির্দেশ দেন যে, তারা পৃথিবীতে বসবাস করবেন এবং সেখানে জীবনযাপন করবেন।\n\n"
                            + "<b>আল্লাহর নির্দেশ:</b>\n\n"
                            + "ثُمَّ اجۡتَبَىٰهُ رَبُّهُۥ فَتَابَ عَلَيۡهِ وَهَدَىٰ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর তার প্রভু তাকে নির্বাচিত করলেন, তাকে ক্ষমা করলেন এবং সঠিক পথের দিশা দিলেন।\n"
                            + "<b>[সূরা ত্বহা, আয়াত: ১২২]</b>\n\n"
                            + "<b>তওবার শিক্ষা:</b>\n"
                            + "হযরত আদম (আ.)-এর ঘটনা আমাদের তওবার গুরুত্ব এবং আল্লাহর দয়ার প্রতি অবিচল বিশ্বাসের শিক্ষা দেয়। তওবা হল সেই পদ্ধতি, যার মাধ্যমে মানুষ আল্লাহর কাছে ফিরে আসে এবং নিজের পাপের জন্য ক্ষমা প্রার্থনা করে।\n\n"
                            + "<b>তওবার শর্ত:</b>\n"
                            + "• <b>অনুতাপ:</b> পাপের জন্য আন্তরিকভাবে অনুতপ্ত হওয়া।\n"
                            + "• <b>পাপ থেকে বিরত থাকা:</b> অবিলম্বে পাপ কাজ ত্যাগ করা।\n"
                            + "• <b>প্রতিজ্ঞা:</b> ভবিষ্যতে পাপ থেকে বিরত থাকার দৃঢ় প্রতিজ্ঞা করা।\n\n"
                            + "<b>হাদিসে তওবার গুরুত্ব:</b>\n"
                            + "রাসূলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) বলেছেন:\n\n"
                            + "كُلُّ ابْنِ آدَمَ خَطَّاءٌ، وَخَيْرُ الْخَطَّائِينَ التَّوَّابُونَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "প্রত্যেক আদম সন্তানই পাপী। তবে উত্তম পাপী তারাই, যারা তওবা করে।\n"
                            + "<b>[তিরমিজি, হাদিস: ২৪৯৯]</b>\n\n"
                            + "আল্লাহর দয়ার অসীমতা, আল্লাহ তাআলা তার বান্দাদের তওবা গ্রহণে অত্যন্ত দয়ালু। তিনি বলেন:\n\n"
                            + "إِنَّ ٱللَّهَ يُحِبُّ ٱلتَّوَّٰبِينَ وَيُحِبُّ ٱلۡمُتَطَهِّرِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই আল্লাহ তওবাকারী এবং পবিত্রতা অর্জনকারীদের ভালোবাসেন।\n"
                            + "<b>[সূরা আল-বাকারা, আয়াত: ২২২]</b>\n\n"
                            + "হযরত আদম (আ.) এবং হাওয়া (আ.)-এর কাহিনী আমাদের শিখায় যে, পাপ করার পরও আল্লাহর দরবারে ফিরে আসা এবং আন্তরিক তওবার মাধ্যমে ক্ষমা প্রার্থনা করা অত্যন্ত গুরুত্বপূর্ণ।",
                    "Adam and Hawwa turned immediately to Allah with heartfelt remorse, reciting the immortal supplication of Tawbah:\n\n"
                            + "<b>Rabbana zalamna anfusana wa-in lam taghfir lana wa-tarhamna lanakunanna minal-khasirin.</b>\n"
                            + "('Our Lord, we have wronged ourselves, and if You do not forgive us and have mercy upon us, we will surely be among the losers.') [Surah Al-A'raf: 23]\n\n"
                            + "<b>The Prophet (pbuh) said:</b> 'Every child of Adam is prone to error, and the best of those who make mistakes are those who repent sincerely.' [Jami' at-Tirmidhi: 2499]"
            ));

            // 8. মানবজাতির পূর্বসূরি
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "মানবজাতির পূর্বসূরি",
                    "The Progenitor of Mankind",
                    "মানবজাতির পূর্বসূরি হলেন হযরত আদম (আলাইহিস সালাম)। ইসলামের দৃষ্টিতে তিনি মানবজাতির আদি পিতা এবং প্রথম নবী। আল্লাহ তাআলা তাঁকে বিশেষভাবে মাটি থেকে সৃষ্টি করেছেন এবং পৃথিবীতে মানবজাতির খলিফা (প্রতিনিধি) হিসেবে নিযুক্ত করেছেন। কুরআনুল কারিম এবং হাদিসে হযরত আদম (আ.)-এর সৃষ্টির কাহিনী, তাঁর জীবনের ঘটনাবলী এবং মানবজাতির সূচনা সম্পর্কে বিস্তারিত আলোচনা করা হয়েছে।\n\n"
                            + "<b>আদম (আ.)-এর সৃষ্টি:</b>\n"
                            + "আল্লাহ তাআলা মাটি থেকে আদম (আ.)-কে সৃষ্টি করেন। তাঁর সৃষ্টি অন্যান্য সৃষ্টির থেকে ভিন্ন এবং বিশেষভাবে সম্মানিত। আল্লাহ তাআলা আদম (আ.)-কে তাঁর নিজ হাতে গঠন করেন এবং নিজের পক্ষ থেকে আত্মা ফুঁকে দেন। কুরআনে বলা হয়েছে:\n\n"
                            + "إِذۡ قَالَ رَبُّكَ لِلۡمَلَـٰٓئِكَةِ إِنِّي خَـٰلِقُۢ بَشَرٗا مِّن صَلۡصَـٰلٖ مِّنۡ حَمَإٖ مَّسۡনُونٖ ٢٨ فَإِذَا سَوَّيۡتُهُۥ وَنَفَخۡتُ فِيهِ مِن رُّوحِي فَقَعُواْ لَهُۥ سَـٰجِدِينَ ٢٩\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন তোমার প্রভু ফেরেশতাদের বললেন, আমি পচা কাদামাটি থেকে একটি মানুষ সৃষ্টি করতে যাচ্ছি। অতঃপর আমি যখন তাকে পরিপূর্ণ করব এবং তার মধ্যে আমার পক্ষ থেকে আত্মা ফুঁকে দেব, তখন তোমরা তার সামনে সিজদায় লুটিয়ে পড়ো।\n"
                            + "<b>[সূরা হিজর, আয়াত: ২৮-২৯]</b>\n\n"
                            + "<b>আদম (আ.)-কে জান্নাতে স্থানদান:</b>\n"
                            + "আল্লাহ তাআলা আদম (আ.)-কে জান্নাতে স্থান দেন এবং তাঁর একাকিত্ব দূর করার জন্য হাওয়া (আ.)-কে সৃষ্টি করেন। কুরআনে উল্লেখ করা হয়েছে:\n\n"
                            + "وَقُلۡنَا يَـٰٓـَٔادَمُ ٱسۡكُنۡ أَنتَ وَزَوۡجُكَ ٱلۡجَنَّةَ وَكُلَا مِنۡهَا رَغَدًا حَيۡثُ شِئۡتُمَاۖ وَلَا تَقۡرَبَا هَـٰذِهِ ٱلشَّجَرَةَ فَتَكُونَا مِنَ ٱلظَّـٰلِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আমি বললাম, হে আদম! তুমি এবং তোমার স্ত্রী জান্নাতে বসবাস কর এবং এর যা কিছু ইচ্ছা তা পরিতোষে আহার কর; কিন্তু এই গাছের কাছে যেও না, তা না হলে তোমরা সীমালঙ্ঘনকারী হয়ে যাবে।\n"
                            + "<b>[সূরা বাকারা, আয়াত: ৩৫]</b>\n\n"
                            + "<b>ইবলিসের অমান্যতা এবং আদম (আ.)-এর পরীক্ষা:</b>\n"
                            + "আল্লাহ তাআলা ফেরেশতাদের আদেশ দেন আদম (আ.)-কে সিজদা করার জন্য। সবাই সিজদা করেন, তবে ইবলিস অহংকার করে অমান্য করে। ফলে ইবলিস অভিশপ্ত হয়। কুরআনে বলা হয়েছে:\n\n"
                            + "قَالَ أَنَا۠ خَيۡرٞ مِّنۡهُ خَلَقۡتَنِي مِن نَّارٖ وَخَلَقۡتَهُۥ مِن طِينٖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "সে বলল, আমি তার চেয়ে উত্তম। তুমি আমাকে আগুন থেকে সৃষ্টি করেছ, আর তাকে সৃষ্টি করেছ মাটি থেকে।\n"
                            + "<b>[সূরা সাদ, আয়াত: ৭৬]</b>\n\n"
                            + "পরে ইবলিস আদম (আ.) এবং হাওয়া (আ.)-কে নিষিদ্ধ গাছের ফল খেতে প্ররোচিত করে। এতে তারা আল্লাহর নিষেধ অমান্য করেন এবং তাদের জান্নাত থেকে পৃথিবীতে পাঠানো হয়।\n\n"
                            + "<b>তওবা এবং মানবজাতির সূচনা:</b>\n"
                            + "আদম (আ.) এবং হাওয়া (আ.) তাদের ভুল বুঝতে পেরে আল্লাহর কাছে ক্ষমা প্রার্থনা করেন। আল্লাহ তাদের ক্ষমা করেন এবং পৃথিবীতে মানবজাতির সূচনা হয়।\n\n"
                            + "<b>তাদের তওবার দোয়া:</b>\n\n"
                            + "رَبَّنَا ظَلَمۡنَآ أَنفُسَنَا وَإِن লَّمۡ تَغۡفِرۡ لَنَا وَتَرۡحَمۡنَا لَنَكُونَنَّ مِنَ ٱلۡخَٰسِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা বলল, হে আমাদের প্রভু! আমরা নিজেদের প্রতি জুলুম করেছি। যদি তুমি আমাদের ক্ষমা না কর এবং আমাদের প্রতি দয়া না কর, তবে আমরা অবশ্যই ক্ষতিগ্রস্তদের অন্তর্ভুক্ত হয়ে যাব।\n"
                            + "<b>[সূরা আল-আ'রাফ, আয়াত: ২৩]</b>\n\n"
                            + "আদম (আ.) এবং তার সন্তানদের পৃথিবীতে আল্লাহর প্রতিনিধি বা খলিফা হিসেবে প্রেরণ করা হয়।\n\n"
                            + "إِنِّي جَاعِلٞ فِي ٱلۡأَرۡضِ خَلِيفَةٗ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই আমি পৃথিবীতে একজন প্রতিনিধি সৃষ্টি করতে যাচ্ছি।\n"
                            + "<b>[সূরা বাকারা, আয়াত: ৩০]</b>\n\n"
                            + "<b>মানবজাতির পরীক্ষা:</b>\n"
                            + "পৃথিবী মানবজাতির জন্য একটি পরীক্ষা ক্ষেত্র। এখানে তাদের ইমান, আমল এবং আল্লাহর আনুগত্য পর্যালোচনা করা হবে।",
                    "Prophet Adam (AS) is the father of all humanity and the first Prophet of Allah. Allah fashioned him uniquely and appointed mankind as His vicegerent (Khalifah) on earth to live in righteousness and obedience."
            ));

            // 9. কাবিল ও হাবিলের ঘটনা
            list.add(new ProphetOverviewTopicItem(
                    9,
                    "কাবিল ও হাবিলের ঘটনা",
                    "The Story of Cain and Abel (Qabil & Habil)",
                    "<b>ঘটনার পটভূমি:</b>\n"
                            + "হযরত আদম (আলাইহিস সালাম) এবং হাওয়া (আলাইহিস সালাম) পৃথিবীতে বসবাস শুরু করার পর আল্লাহ তাদের সন্তান-সন্ততি প্রদান করেন। আদম (আলাইহিস সালাম)-এর দুটি যমজ সন্তান দম্পতি ছিল: হাবিল ও তার যমজ বোন এবং কাবিল ও তার যমজ বোন। তখনকার বিধান অনুসারে, এক যমজ দম্পতির ভাইয়ের বিয়ে অন্য যমজ দম্পতির বোনের সঙ্গে হতো।\n\n"
                            + "<b>ঈর্ষার সূত্রপাত:</b>\n"
                            + "কাবিল নিজের যমজ বোনের সঙ্গে বিয়ে করতে চাইত, কারণ সে ছিল সৌন্দর্যপূর্ণ। কিন্তু আদম (আলাইহিস সালাম) আল্লাহর নির্দেশ অনুযায়ী হাবিলের সঙ্গে কাবিলের যমজ বোনের বিয়ে নির্ধারণ করেন। এতে কাবিল ক্ষুব্ধ ও ঈর্ষাকাতর হয়ে ওঠে। এ সমস্যা সমাধানের জন্য আদম (আলাইহিস সালাম) তাদের কোরবানি দেওয়ার নির্দেশ দেন এবং বললেন, যার কোরবানি গ্রহণ করা হবে, সে সঠিক।\n\n"
                            + "<b>কোরবানির ঘটনা:</b>\n"
                            + "হাবিল (ঈমানদার ও আল্লাহভীরু) তার সর্বোত্তম পশু কোরবানি হিসেবে প্রদান করে। অন্যদিকে, কাবিল (ঈর্ষান্বিত এবং অবাধ্য) তার ক্ষেতের নিম্নমানের ফসল কোরবানি করে।\n\n"
                            + "পবিত্র কুরআনে বলা হয়েছে:\n\n"
                            + "وَٱتۡلُ عَلَيۡهِمۡ نَبَأَ ٱبۡنَيۡ ءَادَمَ بِٱلۡحَقِّ إِذۡ قَرَّبَا قُرۡبَانٗا فَتُقُبِّلَ مِنۡ أَحَدِهِمَا وَلَمۡ يُتَقَبَّلۡ مِنَ ٱلۡأٓخَرِۖ قَالَ لَأَقۡتُلَنَّكَۖ قَالَ إِنَّمَا يَتَقَبَّلُ ٱللَّهُ مِنَ ٱلۡمُتَّقِينَ ٢٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আপনি তাদের কাছে আদম (আলাইহিস সালাম)-এর দুই পুত্রের ঘটনা যথাযথভাবে বর্ণনা করুন, যখন তারা উভয়ে একটি কোরবানি পেশ করেছিল। একজনের কোরবানি গ্রহণ করা হয়েছিল এবং অপরজনেরটি গ্রহণ করা হয়নি। সে বলল, ‘আমি তোমাকে অবশ্যই হত্যা করব।’ সে বলল, ‘আল্লাহ শুধুমাত্র মুত্তাকিদের (পরহেজগারদের) কোরবানি গ্রহণ করেন।\n"
                            + "<b>[সূরা মায়িদা: ৫:২৭]</b>\n\n"
                            + "<b>হত্যার ঘটনা:</b>\n"
                            + "কোরবানি গ্রহণ না হওয়ায় কাবিল আরও ঈর্ষান্বিত হয়ে হাবিলকে হত্যার সিদ্ধান্ত নেয়। হাবিল তাকে বলে, “আমি তোমার ওপর হাত তুলব না, কারণ আমি আল্লাহকে ভয় করি।” কিন্তু কাবিল শয়তানের প্ররোচনায় নিজের ঈর্ষা ও ক্রোধকে প্রশ্রয় দিয়ে তার ভাই হাবিলকে হত্যা করে। এটি ছিল মানব ইতিহাসে প্রথম হত্যাকাণ্ড।\n\n"
                            + "পবিত্র কুরআনে বলা হয়েছে:\n\n"
                            + "فَطَوَّعَتۡ لَهُۥ نَفۡسُهُۥ قَتۡلَ أَخِيهِ فَقَتَلَهُۥ فَأَصۡبَحَ مِنَ ٱلۡخَٰسِرِينَ ٣٠\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এরপর তার (কাবিলের) অন্তর তাকে তার ভাইকে হত্যা করতে প্ররোচিত করল এবং সে তাকে হত্যা করল। ফলে সে ক্ষতিগ্রস্তদের অন্তর্ভুক্ত হয়ে গেল।\n"
                            + "<b>[সূরা মায়িদা: ৫:৩০]</b>\n\n"
                            + "<b>লাশের সমাধি:</b>\n"
                            + "কাবিল তার ভাই হাবিলকে হত্যার পর বিভ্রান্ত হয়ে পড়ে। সে বুঝতে পারছিল না কীভাবে তার ভাইয়ের মৃতদেহ লুকাবে। আল্লাহ এক কাক পাঠান, যে মাটি খুঁড়ে তাকে দেখায় কিভাবে মৃতদেহ ঢেকে রাখতে হয়। এটি দেখে কাবিল আরও লজ্জিত এবং অনুতপ্ত হয়।\n\n"
                            + "কুরআনে এ বিষয়ে বলা হয়েছে:\n\n"
                            + "فَبَعَثَ ٱللَّهُ غُرَابٗا يَبۡحَثُ فِي ٱلۡأَرۡضِ لِيُرِيَهُۥ كَيۡفَ يُوَٰرِي سَوۡءَةَ أَخِيهِۚ قَالَ يَٰوَيۡلَتَىٰٓ أَعَجَزۡتُ أَنۡ أَكُونَ مِثۡلَ هَٰذَا ٱلۡغُرَابِ فَأُوَٰرِيَ سَوۡءَةَ أَخِيۖ فَأَصۡبَحَ مِنَ ٱلنَّٰدِمِينَ ٣١\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এরপর আল্লাহ একটি কাক পাঠালেন, যে মাটিতে খনন করছিল, তাকে দেখানোর জন্য যে কীভাবে তার ভাইয়ের লাশ ঢেকে রাখতে হয়। তখন সে বলল, ‘হায় আমার দূর্ভাগ্য! আমি কি এই কাকটির মতোও হতে পারলাম না, যে আমার ভাইয়ের লাশ ঢেকে রাখে?’ ফলে সে অনুতপ্তদের অন্তর্ভুক্ত হয়ে গেল।\n"
                            + "<b>[সূরা মায়িদা: ৫:৩১]</b>\n\n"
                            + "<b>ঘটনার শিক্ষণীয় দিক:</b>\n"
                            + "• <b>তাকওয়ার গুরুত্ব:</b> আল্লাহ কেবল তাকওয়ার সঙ্গে প্রদত্ত কোরবানি গ্রহণ করেন। ভালো কাজের জন্য অন্তরের বিশুদ্ধতা অপরিহার্য।\n"
                            + "• <b>ঈর্ষার ফলাফল:</b> ঈর্ষা মানুষকে কতটা ভয়াবহ পথে নিয়ে যেতে পারে, কাবিলের ঘটনা এর উদাহরণ। ঈর্ষা কেবল অন্যের ক্ষতি করে না, বরং নিজের ধ্বংস ডেকে আনে।\n"
                            + "• <b>প্রথম হত্যাকাণ্ড:</b> এটি মানবজাতির ইতিহাসে প্রথম হত্যাকাণ্ড। এ ঘটনার মাধ্যমে মানুষ বুঝেছে যে হত্যার শাস্তি অত্যন্ত কঠিন।\n"
                            + "• <b>আল্লাহর দয়া:</b> কাক পাঠিয়ে আল্লাহ দেখিয়েছেন কীভাবে মৃতদেহকে সমাধিস্থ করতে হয়। এটি আল্লাহর দয়া এবং করুণার নিদর্শন। হাবিল ও কাবিলের ঘটনা মানুষের জন্য একটি শিক্ষা। এটি দেখায় যে ঈর্ষা ও অহংকার মানুষের ধ্বংসের কারণ এবং তাকওয়া ও ধৈর্য মানুষকে আল্লাহর কাছে প্রিয় করে তোলে।",
                    "The story of Adam's two sons, Habil (Abel) and Qabil (Cain), depicts the struggle between righteousness and jealousy. Habil offered his best livestock with pure piety, while Qabil offered inferior harvest.\n\n"
                            + "Allah accepted Habil's offering. Driven by rage and envy, Qabil committed the first murder in human history. Allah sent a raven scratching the ground to show Qabil how to bury his brother's body.\n\n"
                            + "<b>Quranic Reference:</b> Surah Al-Ma'idah (5:27-31)."
            ));

            // 10. নবুয়তের কর্মসূচি
            list.add(new ProphetOverviewTopicItem(
                    10,
                    "নবুয়তের কর্মসূচি",
                    "Mission and Program of Prophethood",
                    "হযরত আদম (আলাইহিস সালাম)-এর নবুওয়াত আল্লাহর নির্দেশনা অনুযায়ী মানবজাতির প্রথম জীবন পরিচালনার মৌলিক নীতিগুলো স্থাপন করেছিল। তাঁর কর্মসূচি ছিল মানব জাতির সঠিক পথনির্দেশ, আল্লাহর দাসত্বের গুরুত্ব বোঝানো, এবং দুনিয়ার জীবনকে পরীক্ষার ক্ষেত্র হিসেবে উপলব্ধি করানো।\n\n"
                            + "<b>মানবজাতির জন্য আল্লাহর প্রতিনিধি:</b>\n"
                            + "আদম (আলাইহিস সালাম) পৃথিবীতে আল্লাহর প্রতিনিধি বা খলিফা হিসেবে প্রেরিত হয়েছিলেন।\n\n"
                            + "কুরআনে আল্লাহ বলেছেন:\n\n"
                            + "وَإِذۡ قَالَ رَبُّكَ لِلۡمَلَٰٓئِكَةِ إِنِّي جَاعِلٞ فِي ٱلۡأَرۡضِ خَلِيفَةٗۖ قَالُوٓاْ أَتَجۡعَلُ فِيهَا مَن يُفۡسِدُ فِيهَا وَيَسۡفِكُ ٱلدِّمَآءَ وَنَحۡنُ نُسَبِّحُ بِحَمۡদِكَ وَنُقَدِّسُ لَكَۖ قَالَ إِنِّيٓ أَعۡلَمُ مَا لَا تَعۡلَمُونَ ٣٠\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তোমার প্রভু ফেরেশতাদের বললেন, ‘আমি পৃথিবীতে এক প্রতিনিধি (খলিফা) সৃষ্টি করতে যাচ্ছি।’ তারা বলল, ‘তুমি কি সেখানে এমন কাউকে সৃষ্টি করবে, যে এতে অরাজকতা করবে এবং রক্তপাত ঘটাবে, অথচ আমরা তোমার প্রশংসাসহ পবিত্রতা ঘোষণা করি এবং তোমার মহিমা বর্ণনা করি?’ তিনি বললেন, ‘আমি যা জানি, তোমরা তা জান না।\n"
                            + "<b>[সূরা বাকারা: ২:৩০]</b>\n\n"
                            + "এ দায়িত্ব আল্লাহর দ্বীন প্রতিষ্ঠা করা এবং পৃথিবীতে ন্যায়বিচার ও আল্লাহর বিধান কার্যকর করা।\n\n"
                            + "<b>আল্লাহর ইবাদত শেখানো:</b>\n"
                            + "আদম (আলাইহিস সালাম)-এর মূল দায়িত্ব ছিল মানুষকে এক আল্লাহর ইবাদত শেখানো এবং তাদের মাঝে তাওহীদ (একত্ববাদ) প্রতিষ্ঠা করা।\n"
                            + "• তিনি তার সন্তানদের এবং ভবিষ্যৎ প্রজন্মকে আল্লাহর বিধি-বিধান অনুসারে জীবনযাপন করতে শিক্ষা দেন।\n"
                            + "• তাদের মনে করিয়ে দেন যে তারা আল্লাহর সৃষ্টি এবং তাদের দায়িত্ব আল্লাহর আদেশ পালন করা।\n\n"
                            + "<b>জ্ঞান এবং শিষ্টাচারের শিক্ষা:</b>\n"
                            + "আদম (আলাইহিস সালাম)-কে আল্লাহ তাআলা বিশেষ জ্ঞান দান করেছিলেন, যা তিনি ফেরেশতাদের কাছে প্রকাশ করেছিলেন। এটি মানবজাতিকে জ্ঞান অর্জনের গুরুত্ব এবং নৈতিকতা শেখার প্রাথমিক ভিত্তি দেয়।\n\n"
                            + "কুরআনে বলা হয়েছে:\n\n"
                            + "وَعَلَّمَ ءَادَمَ ٱلۡأَسۡمَآءَ كُلَّهَا ثُمَّ عَرَضَهُمۡ عَلَى ٱلۡمَلَٰٓئِكَةِ فَقَالَ أَنۢبِـُٔونِي بِأَسۡمَآءِ هَٰٓؤُلَآءِ إِن كُنتُمۡ صَٰدِقِينَ ٣١\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আল্লাহ আদমকে সমস্ত বস্তুর নাম শিখিয়ে দিলেন। এরপর সেগুলো ফেরেশতাদের সামনে উপস্থাপন করে বললেন, ‘তোমরা যদি সত্যবাদী হও, তবে আমাকে এগুলোর নাম বলে দাও।\n"
                            + "<b>[সূরা বাকারা: ২:৩১]</b>\n\n"
                            + "এটি প্রমাণ করে যে জ্ঞানার্জন মানবজাতির মৌলিক বৈশিষ্ট্য এবং আল্লাহর প্রতিনিধি হওয়ার জন্য অত্যাবশ্যক।\n\n"
                            + "<b>পারিবারিক ও সামাজিক জীবনের ভিত্তি স্থাপন:</b>\n"
                            + "আদম (আলাইহিস সালাম) এবং তাঁর স্ত্রী হাওয়া (আলাইহিস সালাম)-এর মাধ্যমে পারিবারিক জীবনের সূচনা হয়। তিনি সন্তানদের মধ্যে পারস্পরিক সম্পর্ক, দায়িত্ব ও কর্তব্য শেখান। সামাজিক ও পারিবারিক জীবনের কাঠামো স্থাপন তাঁর নবুওয়াতের একটি গুরুত্বপূর্ণ অংশ।\n\n"
                            + "<b>ভুল থেকে শিক্ষা নেওয়া:</b>\n"
                            + "আদম (আলাইহিস সালাম)-এর নবুওয়াতের একটি বিশেষ শিক্ষা হলো মানুষের ভুল করা স্বাভাবিক, তবে আল্লাহর কাছে তাওবা করে ক্ষমা চাওয়া মানুষের দায়িত্ব। আদম (আলাইহিস সালাম) জান্নাতে নিষিদ্ধ ফল খাওয়ার পর অনুতপ্ত হন এবং আল্লাহর কাছে ক্ষমা প্রার্থনা করেন।\n\n"
                            + "কুরআনে বলা হয়েছে:\n\n"
                            + "فَتَلَقَّىٰٓ ءَادَمُ مِن رَّبِّهِۦ كَلِمَٰتٖ فَتَابَ عَلَيۡهِۚ إِنَّهُۥ هُوَ ٱلتَّوَّابُ ٱلرَّحِيمُ ٣٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তখন আদম তাঁর প্রভুর কাছ থেকে কিছু বাণী শিখে নিলেন। এরপর আল্লাহ তাঁর প্রতি অনুগ্রহ করলেন। নিশ্চয়ই আল্লাহ মহাক্ষমাশীল, অতিমহান।\n"
                            + "<b>[সূরা বাকারা: ২:৩৭]</b>\n\n"
                            + "এই ঘটনা শেখায় যে আল্লাহর দরবারে ক্ষমা চাওয়া এবং তাওবা করা একজন মুমিনের জন্য অত্যন্ত গুরুত্বপূর্ণ।\n\n"
                            + "<b>মানবজাতির প্রথম শত্রু সম্পর্কে সতর্ক করা:</b>\n"
                            + "আদম (আলাইহিস সালাম) নবুওয়াতের মাধ্যমে শয়তানকে মানবজাতির প্রধান শত্রু হিসেবে চিহ্নিত করেন এবং তার প্রতারণা সম্পর্কে সতর্ক করেন। আল্লাহ তাকে শয়তানের ধোঁকাবাজি সম্পর্কে জানিয়ে দিয়েছিলেন।\n\n"
                            + "فَقُلۡنَا يَٰٓءَادَمُ إِنَّ هَٰذَا عَدُوّٞ لَّكَ وَلِزَوۡجِكَ فَلَا يُخۡرِجَنَّكُمَا مِنَ ٱلۡجَنَّةِ فَتَشۡقَىٰٓ ١١٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তখন আমরা বলেছিলাম, ‘হে আদম! নিশ্চয়ই এই (শয়তান) তোমার ও তোমার স্ত্রীর শত্রু। সে যেন তোমাদের জান্নাত থেকে বের না করে দেয়, অন্যথায় তোমার দুর্ভোগ শুরু হবে।\n"
                            + "<b>[সূরা ত্বহা: ২০:১১৭]</b>\n\n"
                            + "<b>জীবনধারণের উপায় শেখানো:</b>\n"
                            + "আদম (আলাইহিস সালাম) মানুষকে পৃথিবীতে জীবনযাপনের পদ্ধতি শিখিয়েছেন। আল্লাহ তাকে কিভাবে চাষাবাদ করতে হয়, কীভাবে খাদ্য সংগ্রহ করতে হয়, এবং কীভাবে নিজের প্রয়োজনীয় সামগ্রী তৈরি করতে হয়, সে জ্ঞান দিয়েছিলেন।\n\n"
                            + "<b>আল্লাহর প্রতি দায়বদ্ধতা এবং কিয়ামতের প্রস্তুতি:</b>\n"
                            + "তিনি তাঁর সন্তানদের শিক্ষা দিয়েছেন যে দুনিয়া একটি ক্ষণস্থায়ী জায়গা এবং কিয়ামত দিবসে সবাইকে আল্লাহর সামনে হিসাব দিতে হবে। পৃথিবীর জীবন একটি পরীক্ষা এবং প্রকৃত পুরস্কার রয়েছে আখিরাতে।",
                    "Prophet Adam's divine mission established the foundational pillars of monotheism, righteous family structure, moral ethics, agricultural knowledge, and preparation for the Day of Judgment."
            ));

            // 11. আদম (আ.)-এর শেষ জীবন
            list.add(new ProphetOverviewTopicItem(
                    11,
                    "আদম (আ.)-এর শেষ জীবন",
                    "Final Days and Passing of Adam (AS)",
                    "হযরত আদম (আলাইহিস সালাম)-এর বয়স ছিল প্রায় ৯৩০ বছর। তবে এই সময়কাল বিভিন্ন ঐতিহাসিক বর্ণনায় ভিন্ন হতে পারে। কিছু সূত্রে বলা হয়, আল্লাহ তাকে দীর্ঘ হায়াত প্রদান করেছিলেন, যাতে তিনি তার সন্তানদের শিক্ষা দিতে পারেন। মৃত্যুর আগে হযরত আদম (আলাইহিস সালাম) তাঁর সন্তানদের তাওহীদ (আল্লাহর একত্ববাদ) এবং আল্লাহর বিধান মানার নির্দেশ দেন। তিনি তাদের মনে করিয়ে দেন যে তারা শয়তানের ধোঁকা থেকে সাবধান থাকবে এবং আল্লাহর পথ অনুসরণ করবে।\n\n"
                            + "যদিও আদম (আলাইহিস সালাম)-এর মৃত্যুর ঘটনা কুরআনে সরাসরি উল্লেখ নেই, তবে তাঁর জীবনের বিভিন্ন গুরুত্বপূর্ণ দিক কুরআনে আলোচিত হয়েছে। এর মধ্যে একটি হলো তাঁর জান্নাত থেকে পৃথিবীতে আগমন এবং তাঁর জীবনের শিক্ষা।\n\n"
                            + "<b>পৃথিবীতে জীবন ও মৃত্যুর পরীক্ষা:</b>\n\n"
                            + "كُلُّ نَفۡسٖ ذَآئِقَةُ ٱلۡمَوۡتِۖ ثُمَّ إِلَيۡنَا تُرۡجَعُونَ ٥٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "প্রত্যেক প্রাণ মৃত্যুর স্বাদ আস্বাদন করবে। এরপর তোমরা আমাদের কাছেই ফিরে আসবে।\n"
                            + "<b>[সূরা আনকাবুত: ২৯:৫৭]</b>\n\n"
                            + "আয়াতটি আদম (আ.)-এর বংশধরদের জন্য হলেও এটি মানবজাতির পৃথিবীতে আসার মূল উদ্দেশ্য এবং মৃত্যুর চূড়ান্ত বাস্তবতাকে নির্দেশ করে।\n\n"
                            + "<b>হাদিসের রেফারেন্স:</b>\n\n"
                            + "<b>আদম (আ.)-এর মৃত্যুর সময় ফেরেশতাদের আগমন:</b>\n"
                            + "হাদিসে বর্ণিত আছে যে আদম (আলাইহিস সালাম)-এর মৃত্যুর সময় ফেরেশতারা তাঁর জান কবজ করতে আসেন।\n\n"
                            + "إِنَّ اللَّهَ أَرْسَلَ مَلَكًا إِلَى آدَمَ لِيَقْبِضَ رُوحَهُ، فَقَالَ آدَمُ: يَا رَبِّ، أَوَ لَمْ تُعْمِرْنِي إِلَى أَلْفِ سَنَةٍ؟ قَالَ: بَلَى، وَلَكِنَّكَ نَسِيتَ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ আদম (আ.)-এর মৃত্যু কার্যকর করার জন্য একটি ফেরেশতাকে পাঠান। আদম (আ.) বললেন, হে আমার প্রভু, আমাকে কি এক হাজার বছর আয়ু দেওয়া হয়নি?’ আল্লাহ বললেন, ‘হ্যাঁ, তবে তুমি তা ভুলে গিয়েছ।\n"
                            + "<b>[মুসনাদে আহমাদ, হাদিস: ১০৬৫২]</b>\n\n"
                            + "<b>আদম (আ.)-এর দাফনের শিক্ষা:</b>\n"
                            + "হাদিসে উল্লেখ আছে, হযরত আদম (আ.)-এর মৃত্যুর পর আল্লাহ ফেরেশতাদের মাধ্যমে তাঁর সন্তানদের শিখিয়েছিলেন কীভাবে দাফন করতে হয়।\n\n"
                            + "فَلَمَّا مَاتَ آدَمُ قَالَتْ بَنُوهُ: مَا نَصْنَعُ بِهِ؟ فَأَرَاهُمُ اللَّهُ دَفْنَهُ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন আদম (আ.) মারা যান, তাঁর সন্তানরা বলল, ‘আমরা তাঁর সঙ্গে কী করব?’ তখন আল্লাহ তাদের তাঁর দাফনের পদ্ধতি শিখিয়ে দেন।\n"
                            + "<b>[ইবনে মাজাহ, কিতাবুল জানায়েয, হাদিস: ১৫৫২]</b>\n\n"
                            + "<b>আদম (আ.)-এর জীবনের কাল:</b>\n"
                            + "রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) আদম (আ.)-এর আয়ু সম্পর্কে বলেছেন:\n\n"
                            + "كَانَ عُمْرُ آدَمَ أَلْفَ سَنَةٍ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আদম (আলাইহিস সালাম)-এর আয়ু ছিল এক হাজার বছর।\n"
                            + "<b>[সহীহ বুখারী, কিতাবুল আনবিয়া, হাদিস: ৩৩৬২]</b>\n\n"
                            + "হযরত আদম (আলাইহিস সালাম)-এর মৃত্যু সম্পর্কে কুরআনে সরাসরি বর্ণনা না থাকলেও, হাদিস ও তাফসীর গ্রন্থে তাঁর মৃত্যুর সময় ফেরেশতাদের আগমন, তাঁর সন্তানদের দাফনের পদ্ধতি শেখানো এবং তাঁর আয়ু সম্পর্কে বর্ণনা পাওয়া যায়।\n\n"
                            + "<b>এটি থেকে আমরা কিছু গুরুত্বপূর্ণ শিক্ষা পাই:</b>\n"
                            + "• মৃত্যু মানুষের চূড়ান্ত বাস্তবতা।\n"
                            + "• দাফনের পদ্ধতি আদম (আ.)-এর মাধ্যমে মানবজাতির জন্য নির্ধারিত হয়েছে।\n"
                            + "• জীবনের প্রতিটি কাজই আল্লাহর বিধান অনুসারে পরিচালিত হওয়া উচিত।\n\n"
                            + "اللَّهُمَّ اجْمَعْنَا مَعَ أَبِينَا آدَمَ فِي الْجَنَّةِ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে আল্লাহ! আমাদের আমাদের পিতা আদমের সঙ্গে জান্নাতে একত্রিত করুন।",
                    "Before his demise at approximately one thousand years of age, Prophet Adam (AS) advised his children to hold fast to Tawheed and beware of Satan's deception. The angels descended to wrap him in shroud and taught his children the proper Islamic burial procedure."
            ));
        } else if (prophetId == 2) {
            // হযরত ইদরীস (আ:) - All 8 Chapters / Topics 100% Verbatim from User Request & Screenshots

            // 1. জন্ম ও বংশধারা
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "জন্ম ও বংশধারা",
                    "Birth and Lineage",
                    "ইদ্রিস (আলাইহিস সালাম)-এর বংশধারা আদম (আলাইহিস সালাম)-এর পুত্র শীস (আলাইহিস সালাম) এর বংশধারা থেকে এসেছে বলে উল্লেখ করা হয়। তাঁর পিতার নাম ইয়ারদ (إِيَارِدْ) এবং তাঁর দাদার নাম মাহলায়েল (مَهْلَائِيلْ)। তিনি বংশক্রমে সপ্তম পুরুষ ছিলেন আদম (আলাইহিস সালাম)-এর পরে।অধিকাংশ ঐতিহাসিক ও মুফাসসিরগণ বলেন, ইদ্রিস (আ.) এর প্রকৃত নাম ছিল আখনূখ (أَخْنُوخْ) এবং তাঁর নামের অর্থ ছিল \"সৎপথপ্রাপ্ত\" বা \"গবেষক\"। তবে তিনি ইদ্রিস নামে অধিক পরিচিত, কারণ তিনি বহু ইলম (জ্ঞান) অর্জন করতেন এবং অন্যদের শিক্ষাদান করতেন।\n\n"
                            + "<b>জন্মস্থান:</b>\n"
                            + "ঐতিহাসিক বর্ণনা অনুযায়ী ইদ্রিস (আলাইহিস সালাম)-এর জন্ম মিশর বা বাবিল এলাকায় হয়েছিল। সেই সময় পৃথিবীতে মূর্তি পূজা এবং পাপাচার প্রচলিত ছিল।\n\n"
                            + "<b>উপাধি ও গুণাবলি:</b>\n"
                            + "• তিনি প্রথম নবী যিনি কলম দিয়ে লিখেছিলেন।\n"
                            + "• তাঁকে \"নবীদের শিক্ষক\" বলা হয়।\n"
                            + "• ইদ্রিস (আলাইহিস সালাম) প্রথম মানুষ ছিলেন যিনি সেলাই করা পোশাক পরিধান করেন।\n"
                            + "• তিনি জ্ঞান-বিজ্ঞানে পারদর্শী ছিলেন।\n\n"
                            + "<b>আল-কুরআনে ইদ্রিস (আ.) এর উল্লেখ:</b>\n"
                            + "ইদ্রিস (আলাইহিস সালাম)-কে আল্লাহ সুবহানাহু ওয়া তা'আলা কুরআনে প্রশংসা করেছেন\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَبِيًّا • وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এবং কিতাবে ইদ্রিসের কথা স্মরণ কর। তিনি ছিলেন সত্যবাদী ও নবী। আমি তাঁকে উচ্চ মর্যাদায় উন্নীত করেছিলাম।\n"
                            + "<b>[সূরা মারইয়াম (১৯:৫৬-৫৭)]</b>\n\n"
                            + "এই আয়াত থেকে বোঝা যায় যে ইদ্রিস (আলাইহিস সালাম) ছিলেন একজন সত্যবাদী নবী এবং আল্লাহ তাঁকে সম্মানজনকভাবে উচ্চ অবস্থানে উন্নীত করেছিলেন।\n\n"
                            + "وَإِسْمَاعِيلَ وَإِدْرِيسَ وَذَا الْكِفْلِ ۖ كُلٌّ مِّنَ الصَّابِرِينَ • وَأَدْخَلْنَاهُمْ فِي رَحْمَتِنَا ۖ إِنَّهُم مِّنَ الصَّالِحِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর ইসমাঈল, ইদ্রিস এবং যুল-কিফলের কথা স্মরণ কর। তারা সবাই ছিলেন ধৈর্যশীল। আমি তাদেরকে আমার অনুগ্রহের অন্তর্ভুক্ত করেছিলাম। নিশ্চয়ই তারা ছিল সৎকর্মপরায়ণ।\n"
                            + "<b>[সূরা আম্বিয়া (২১:৮৫-৮৬)]</b>\n\n"
                            + "ইদ্রিস (আলাইহিস সালাম) ছিলেন একজন মহান নবী, যিনি মানবজাতির জন্য আদর্শ। তাঁর ধৈর্য, জ্ঞান এবং উচ্চ মর্যাদার জীবন থেকে আমরা অনেক কিছু শিখতে পারি। আল্লাহ তাঁকে তাঁর অসীম করুণা দ্বারা সম্মানিত করেছেন এবং আমাদের উচিত তাঁর জীবনী থেকে শিক্ষা গ্রহণ করা।",
                    "The lineage of Prophet Idris (peace be upon him) traces back through the lineage of Seth (Shees AS), the son of Adam (AS). His father was Yarid (إِيَارِدْ) and his grandfather was Mahalaleel (مَهْلَائِيلْ). He was the seventh generation in descent from Adam (AS). Most classical historians and commentators state that his original name was Enoch (Akhnukh - أَخْنُوخْ), meaning 'the guided one' or 'the devoted researcher'. However, he became widely renowned as Idris because of his profound acquisition and imparting of divine and worldly knowledge (Dars).\n\n"
                            + "<b>Birthplace:</b>\n"
                            + "According to historical accounts, Idris (AS) was born in Egypt or Babylon (Babylonia) during an era where idolatry and moral transgressions had begun to spread across the earth.\n\n"
                            + "<b>Titles and Attributes:</b>\n"
                            + "• The first prophet to write with a pen.\n"
                            + "• Known as the 'Teacher of Prophets'.\n"
                            + "• The first human to sew and wear tailored garments.\n"
                            + "• Proficient in astronomy, mathematics, and divine sciences.\n\n"
                            + "<b>Mention in the Holy Quran:</b>\n"
                            + "Allah Almighty praised Idris (AS) in the Quran:\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَبِيًّا • وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>Translation:</b>\n"
                            + "And mention in the Book, Idris. Indeed, he was a man of truth and a prophet. And We raised him to a high station.\n"
                            + "<b>[Surah Maryam (19:56-57)]</b>\n\n"
                            + "This verse highlights that Idris (AS) was a deeply truthful prophet whom Allah elevated to an extraordinarily lofty and honorable station.\n\n"
                            + "وَإِسْمَاعِيلَ وَإِدْرِيسَ وَذَا الْكِفْلِ ۖ كُلٌّ مِّنَ الصَّابِرِينَ • وَأَدْخَلْنَاهُمْ فِي رَحْمَتِنَا ۖ إِنَّهُم مِّنَ الصَّالِحِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And remember Ishmael and Idris and Dhul-Kifl; all were of the patient. And We admitted them into Our mercy. Indeed, they were of the righteous.\n"
                            + "<b>[Surah Al-Anbiya (21:85-86)]</b>\n\n"
                            + "Prophet Idris (AS) stands as an exemplary role model for humanity, teaching us patience, knowledge, and dedication to righteousness."
            ));

            // 2. নবুওত প্রাপ্তি
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "নবুওত প্রাপ্তি",
                    "Attainment of Prophethood",
                    "তাফসিরবিদদের মতে, ইদ্রিস (আলাইহিস সালাম) আদম (আলাইহিস সালাম)-এর পরে নবুওত প্রাপ্ত প্রথম নবীদের মধ্যে একজন। তিনি একাধারে নবী ও আসমানী গ্রন্থধারী ছিলেন। বলা হয়ে থাকে যে, তিনি হযরত শীস (আলাইহিস সালাম)-এর সময় জন্মগ্রহণ করেন এবং তাকে নবুওতের দায়িত্ব অর্পণ করা হয়। হযরত ইদ্রিস (আলাইহিস সালাম) সম্পর্কে নির্দিষ্টভাবে তাঁর নবুওয়াত প্রাপ্তির বয়স কুরআন ও সহিহ হাদিসে উল্লেখ করা হয়নি। ইসলামী ঐতিহ্য এবং কিছু বর্ণনায় বলা হয় যে, তিনি যথেষ্ট পরিপক্ক বয়সে নবুওয়াত প্রাপ্ত হন।\n\n"
                            + "ইদ্রিস (আলাইহিস সালাম) এর নবুওতের সম্পর্কে কুরআন এবং হাদিসে সরাসরি কিছু তথ্য রয়েছে। ইদ্রিস (আলাইহিস সালাম) নবী হিসেবে তাঁর সম্প্রদায়কে আল্লাহ তাআলার দিকে আহ্বান জানিয়েছিলেন। তাঁর নবুওয়াতের মূল কর্মকাণ্ড নিম্নরূপ:\n\n"
                            + "<b>তাওহীদের প্রচার:</b>\n"
                            + "• মানুষকে এক আল্লাহর ইবাদত করার নির্দেশ।\n"
                            + "• শিরক ও মূর্তিপূজা থেকে বিরত থাকার আহ্বান।\n\n"
                            + "<b>পাপ ও গুনাহ থেকে বিরত রাখা:</b>\n"
                            + "• সমাজে নৈতিকতা ও ন্যায়পরায়ণতা প্রতিষ্ঠা।\n"
                            + "• জুলুম, অন্যায় ও গুনাহ থেকে মানুষকে সাবধান করা।\n\n"
                            + "<b>জ্ঞান ও লেখনির প্রসার:</b>\n"
                            + "• কলমের সাহায্যে লিখার প্রচলন করেন।\n"
                            + "• বিদ্যা ও বিজ্ঞানের চর্চার প্রচলন করেন।\n\n"
                            + "<b>কর্মসংস্কৃতি প্রচলন:</b>\n"
                            + "• সেলাই ও বস্ত্রবিন্যাসের কাজ শুরু করেন।\n"
                            + "• তাফসিরবিদগণ বলেন, তিনি কাপড় সেলাইয়ের সূচনা করেন।\n\n"
                            + "আল্লাহ তাআলা বলেন:\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَّبِيًّا ﴿٥٦﴾ وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর কিতাবে ইদ্রিসের কথা স্মরণ করো। নিশ্চয় তিনি ছিলেন সত্যবাদী নবী। আমি তাকে উচ্চ মর্যাদায় উন্নীত করেছি।\n"
                            + "<b>[সূরা মারইয়াম, ১৯:৫৬-৫৭]</b>\n\n"
                            + "<b>ব্যাখ্যা:</b>\n"
                            + "\"صِدِّيقًا نَّبِيًّا\" (সত্যবাদী নবী): তিনি ছিলেন ন্যায়পরায়ণ ও সত্যবাদী। নবী হিসেবে তিনি সঠিক পথে মানুষকে পরিচালিত করতেন।\n"
                            + "\"وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\" (উচ্চ মর্যাদায় উন্নীত করেছি): আল্লাহ তাআলা তাঁকে দুনিয়া ও আখিরাতে বিশেষ সম্মানিত করেছেন।\n\n"
                            + "রাসূলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) ইসরা ও মিরাজের রাতে ইদ্রিস (আলাইহিস সালাম)-এর সাথে সাক্ষাৎ করেন।\n\n"
                            + "عَنْ أَنَسِ بْنِ مَالِكٍ - رَضِيَ اللَّهُ عَنْهُ - فِي حَدِيثِ الإِسْرَاءِ وَالْمِعْرَاجِ، قَالَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ:\n\n"
                            + "\"ثُمَّ صَعِدَ بِيَ إِلَى السَّمَاءِ الرَّابِعَةِ فَإِذَا أَنَا بِإِدْرِيسَ، فَقَالَ: مَرْحَبًا بِالنَّبِيِّ الصَّالِحِ وَالأَخِ الصَّالِحِ\"\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর আমাকে চতুর্থ আকাশে উঠানো হলো। সেখানে আমি ইদ্রিস (আলাইহিস সালাম)-এর সাথে সাক্ষাৎ করলাম। তিনি বললেন: হে নেক নবী এবং নেক ভাই, আপনাকে স্বাগতম।\n"
                            + "<b>[সহিহ বুখারি, হাদিস: ৩৪৯]</b>\n\n"
                            + "<b>ব্যাখ্যা:</b>\n"
                            + "ইদ্রিস (আলাইহিস সালাম)-এর চতুর্থ আকাশে অবস্থান তাঁর মর্যাদার প্রমাণ।\n"
                            + "নবী হিসেবে তিনি রাসূলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম)-কে সম্মান জানিয়েছিলেন।\n\n"
                            + "<b>কলমের প্রচলন ও জ্ঞানচর্চা:</b>\n"
                            + "ইদ্রিস (আলাইহিস সালাম) প্রথম ব্যক্তি যিনি কলমের সাহায্যে লিখতেন।\n"
                            + "তিনি মানুষের মধ্যে বিদ্যা-বিজ্ঞান ও জ্ঞানচর্চার প্রসার ঘটান।\n\n"
                            + "<b>সেলাইয়ের প্রচলন:</b>\n"
                            + "ইদ্রিস (আলাইহিস সালাম) ছিলেন প্রথম ব্যক্তি যিনি কাপড় সেলাই করতেন এবং নিজ হাতে পোশাক তৈরি করতেন। ইদ্রিস (আলাইহিস সালাম)-এর নবুওয়াতের মূল লক্ষ্য ছিল তাওহীদের প্রচার, পাপাচার থেকে মানুষকে বিরত রাখা।",
                    "According to Quranic exegetes, Idris (peace be upon him) was among the first prophets commissioned after Adam (AS). He was both a prophet and the recipient of divine scripture (Suhuf). Historical narrations state that he was born during the time of Seth (Shees AS) and was entrusted with prophethood upon reaching mature adulthood.\n\n"
                            + "<b>Core Pillars of His Mission:</b>\n\n"
                            + "<b>Preaching Monotheism (Tawheed):</b>\n"
                            + "• Calling mankind to worship Allah alone.\n"
                            + "• Eradicating idolatry, polytheism, and superstition.\n\n"
                            + "<b>Reforming Moral Conduct:</b>\n"
                            + "• Establishing justice, honesty, and societal balance.\n"
                            + "• Warning against tyranny, transgression, and sins.\n\n"
                            + "<b>Advancement of Writing and Knowledge:</b>\n"
                            + "• Pioneered writing with the pen.\n"
                            + "• Encouraged the study of astronomy, arithmetic, and sciences.\n\n"
                            + "<b>Productive Labor and Craftsmanship:</b>\n"
                            + "• Introduced the craft of tailoring and sewing garments from cloth.\n\n"
                            + "<b>Quranic Evidence:</b>\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَّبِيًّا ﴿٥٦﴾ وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>Translation:</b>\n"
                            + "And mention in the Book, Idris. Indeed, he was a man of truth and a prophet. And We raised him to a high station.\n"
                            + "<b>[Surah Maryam, 19:56-57]</b>\n\n"
                            + "<b>Hadith Evidence from Isra and Miraj:</b>\n"
                            + "Narrated by Anas ibn Malik (RA):\n"
                            + "ثُمَّ صَعِدَ بِيَ إِلَى السَّمَاءِ الرَّابِعَةِ فَإِذَا أَنَا بِإِدْرِيسَ، فَقَالَ: مَرْحَبًا بِالنَّبِيِّ الصَّالِحِ وَالأَخِ الصَّالِحِ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Then I was taken up to the fourth heaven, and there I met Idris. He said: 'Welcome, O righteous Prophet and righteous brother!'\n"
                            + "<b>[Sahih al-Bukhari: 349]</b>"
            ));

            // 3. জীবনকাল ও দীর্ঘ আয়ু
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "জীবনকাল ও দীর্ঘ আয়ু",
                    "Lifespan and Longevity",
                    "হযরত ইদ্রিস (আলাইহিস সালাম) এর জীবনকাল ও দীর্ঘ আয়ু সম্পর্কে নির্দিষ্ট তথ্য কুরআন ও সহিহ হাদিসে উল্লেখ করা হয়নি। ইসলামী ঐতিহ্য এবং কিছু বর্ণনায় বলা হয় যে, তিনি যথেষ্ট পরিপক্ক বয়সে নবুওয়াত প্রাপ্ত হন এবং কিছু ইতিহাসবিদ ও তাফসিরবিদের বর্ণনায় তাঁর দীর্ঘ জীবন সম্পর্কে আলোচনা পাওয়া যায়।\n\n"
                            + "<b>দীর্ঘ জীবন ও আয়ু:</b>\n"
                            + "অনেক বর্ণনায় বলা হয়, ইদ্রিস (আলাইহিস সালাম) দীর্ঘকাল জীবনযাপন করেছিলেন এবং তিনি মানবজাতির মধ্যে প্রথম নবীদের একজন।\n\n"
                            + "<b>তাফসির আল-মাহদী ও আল-কুরতুবী:</b>\n"
                            + "তাফসিরবিদগণ উল্লেখ করেন যে ইদ্রিস (আলাইহিস সালাম) এক হাজার বছর বেঁচেছিলেন। তবে এই তথ্যের কোনো প্রামাণ্য সহিহ সূত্র নেই।",
                    "Specific details regarding the exact lifespan and age of Prophet Idris (peace be upon him) are not explicitly mentioned in the Holy Quran or authentic Hadith literature. Islamic tradition and classical historians record that he was blessed with long life and attained prophethood at a mature age.\n\n"
                            + "<b>Long Lifespan:</b>\n"
                            + "Classical historians note that Idris (AS) lived for a prolonged period, guiding early generations of humanity as one of the pioneer prophets.\n\n"
                            + "<b>Commentaries of Al-Mahdi and Al-Qurtubi:</b>\n"
                            + "Some classical exegetes mention that Idris (AS) lived for up to one thousand years; however, this specific duration originates from historical narrations rather than definitive authentic hadiths."
            ));

            // 4. ইলম ও লেখনশিল্পের প্রচলন
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "ইলম ও লেখনশিল্পের প্রচলন",
                    "Introduction of Pen and Knowledge",
                    "ইদ্রিস (আলাইহিস সালাম) সম্পর্কে কুরআন ও হাদিসে উল্লেখ রয়েছে যে তিনি একজন নবী ছিলেন, যিনি বিশেষ জ্ঞান, প্রজ্ঞা এবং বিভিন্ন শিল্পকলার প্রচলনে গুরুত্বপূর্ণ ভূমিকা পালন করেছিলেন। তিনি ছিলেন কিতাব ও কলমের বিদ্যায় বিশেষজ্ঞ এবং প্রথমবার মানুষের মধ্যে লেখালেখির প্রচলন করেন বলে বিভিন্ন ইসলামী বর্ণনায় পাওয়া যায়। ইদ্রিস (আলাইহিস সালাম) সম্পর্কে সুনির্দিষ্টভাবে উল্লেখিত হাদিস কম থাকলেও ইসলামী ঐতিহ্যে তার বিশেষ অবদান হিসেবে যে বিষয়গুলি পরিচিত তা হলো:\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَبِيًّا وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর কিতাবে ইদ্রিসের কথা উল্লেখ করুন। তিনি ছিলেন অত্যন্ত সত্যবাদী এবং একজন নবী। আমি তাকে উচ্চ মর্যাদার স্থানে উন্নীত করেছি।\n"
                            + "<b>[সূরা মারইয়াম, আয়াত ১৯:৫৬-৫৭]</b>\n\n"
                            + "وَإِسْمَاعِيلَ وَإِدْرِيسَ وَذَا الْكِفْلِ ۖ كُلٌّ مِّنَ الصَّابِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর স্মরণ করুন ইসমাঈল, ইদ্রিস এবং যুলকিফলকে; তারা সবাই ছিলেন ধৈর্যশীলদের অন্তর্ভুক্ত।\n"
                            + "<b>[সূরা আম্বিয়া, আয়াত ২১:৮৫]</b>\n\n"
                            + "<b>লেখা ও শিল্পকলার প্রচলন:</b>\n"
                            + "ইমাম ইবন কাসির (রহিমাহুল্লাহ) তার তাফসিরে উল্লেখ করেছেন যে ইদ্রিস (আলাইহিস সালাম) ছিলেন প্রথম ব্যক্তি যিনি কলম ব্যবহার করে লেখালেখি শুরু করেন। এছাড়া, বিভিন্ন বিদ্যা যেমন গণিত, জ্যোতির্বিজ্ঞান, এবং সেলাইয়ের শিল্প তার মাধ্যমেই প্রথম প্রবর্তিত হয়।\n\n"
                            + "<b>কাজ ও সেলাইয়ের কৌশল:</b>\n"
                            + "ইবন কাসির আরও উল্লেখ করেন যে তিনি ছিলেন প্রথম ব্যক্তি যিনি কাপড় সেলাইয়ের জন্য সূচ ব্যবহার করেছিলেন এবং মানুষের জন্য এটি সহজ করেছিলেন।\n\n"
                            + "<b>ঐতিহাসিক ব্যাখ্যা:</b>\n"
                            + "ইদ্রিস (আলাইহিস সালাম) সম্পর্কে ইসলামী ঐতিহ্যে প্রচলিত রয়েছে যে তিনি ছিলেন প্রজ্ঞা, ধৈর্য এবং ইলমে বিশেষ স্থানীয়। তার সময়ের মানুষদের তিনি লিখনশিল্প, সেলাই এবং তারকা গণনার মতো বিষয়গুলো শিখিয়েছিলেন। তাকে \"আনুচ্চিত জ্ঞান\" এর প্রথম শিক্ষক হিসেবে বিবেচনা করা হয়।",
                    "The Holy Quran and Islamic traditions honor Prophet Idris (peace be upon him) for his exceptional wisdom, intellect, and profound contributions to human civilization and sciences. Classical scholars identify him as the first human to write with a pen and introduce essential arts and sciences.\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَبِيًّا وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>Translation:</b>\n"
                            + "And mention in the Book, Idris. Indeed, he was a man of truth and a prophet. And We raised him to a high station.\n"
                            + "<b>[Surah Maryam, 19:56-57]</b>\n\n"
                            + "وَإِسْمَاعِيلَ وَإِدْرِيسَ وَذَا الْكِفْلِ ۖ كُلٌّ مِّنَ الصَّابِرِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And remember Ishmael, Idris, and Dhul-Kifl; all were among the patient.\n"
                            + "<b>[Surah Al-Anbiya, 21:85]</b>\n\n"
                            + "<b>Writing, Mathematics and Astronomy:</b>\n"
                            + "Imam Ibn Kathir (rahimahullah) notes in his Tafseer that Idris (AS) was the first person to write with a pen. Moreover, early disciplines such as arithmetic, astronomy, and tailoring garments were pioneered through his teachings.\n\n"
                            + "<b>The Craft of Sewing and Tailoring:</b>\n"
                            + "Ibn Kathir also records that Idris (AS) was the first human to use a needle to sew clothes, transitioning humanity from wearing animal skins to stitched garments.\n\n"
                            + "<b>Historical Significance:</b>\n"
                            + "Idris (AS) taught his contemporaries writing, tailoring, and observation of celestial movements, establishing him in Islamic history as a great educator of early human civilization."
            ));

            // 5. সমাজের অবস্থা ও দাওয়াত
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "সমাজের অবস্থা ও দাওয়াত",
                    "State of Society and Dawah",
                    "ইদ্রিস (আলাইহিস সালাম) পৃথিবীর প্রথম দিককার নবীদের একজন ছিলেন। তার সময়ে মানুষের মধ্যে বিভিন্ন প্রকার জ্ঞান ও শিল্পকলার অভাব ছিল, এবং তারা সাধারণত আল্লাহর আনুগত্যে অমনোযোগী ছিল। আদম (আলাইহিস সালাম) এবং তার সন্তানদের সময় থেকে শুরু হওয়া তাওহিদের বার্তা ইদ্রিস (আলাইহিস সালাম)-এর যুগে দুর্বল হয়ে পড়েছিল। সমাজে নৈতিকতা ও আল্লাহর নির্দেশ মেনে চলার অভাব প্রকট হতে শুরু করেছিল। মানুষ জাহিলিয়াত বা অজ্ঞতার দিকে ঝুঁকছিল। তারা আল্লাহকে ভুলে গিয়ে নিজেদের জীবনকে কেবল দুনিয়াবি সুবিধা ও ভোগবিলাসে নিবদ্ধ করেছিল। এ সময় আল্লাহ ইদ্রিস (আলাইহিস সালাম)-কে নবুয়তের দায়িত্ব দিয়ে সমাজে মানুষকে সঠিক পথে ফিরিয়ে আনার জন্য প্রেরণ করেন।\n\n"
                            + "<b>ইদ্রিস (আলাইহিস সালাম)-এর দাওয়াত:</b>\n"
                            + "ইদ্রিস (আলাইহিস সালাম) মানুষকে তাওহিদের দিকে আহ্বান জানান এবং তাদের জীবনে আল্লাহর আইন প্রতিষ্ঠা করার জন্য দাওয়াত দেন। তার দাওয়াতের কিছু প্রধান দিক ছিল:\n\n"
                            + "• <b>তাওহিদের বার্তা প্রচার:</b> ইদ্রিস (আলাইহিস সালাম) মানুষকে এক আল্লাহর ইবাদতের প্রতি আহ্বান জানান। তিনি তাদের শিরক ও কুসংস্কার পরিত্যাগ করতে নির্দেশ দেন। তাওহিদের বার্তা পুনঃপ্রতিষ্ঠার জন্য তিনি অনবরত প্রচেষ্টা চালান।\n"
                            + "• <b>নৈতিক উন্নতি ও সমাজ সংস্কার:</b> তিনি মানুষকে সততা, ন্যায়বিচার এবং পারস্পরিক সহযোগিতার মাধ্যমে সমাজ গঠনের শিক্ষা দেন। মানুষকে অন্যায় ও পাপাচার থেকে বিরত থাকার নির্দেশ দেন।\n"
                            + "• <b>ইলম ও কলা-কৌশল শেখানো:</b> ইদ্রিস (আলাইহিস সালাম)-কে ইসলামী ঐতিহ্যে একজন জ্ঞানী নবী হিসেবে বর্ণনা করা হয়। তিনি ছিলেন প্রথম ব্যক্তি যিনি কলম দিয়ে লেখার প্রচলন করেন এবং সমাজে শিক্ষার প্রসার ঘটান। তিনি মানুষকে জ্যোতির্বিদ্যা, গণিত, এবং সেলাইসহ বিভিন্ন দিকনির্দেশনা দেন, যা তাদের জীবনযাত্রাকে উন্নত করে।\n"
                            + "• <b>ধৈর্য এবং আল্লাহর প্রতি পূর্ণ আস্থা:</b> ইদ্রিস (আলাইহিস সালাম) মানুষকে ধৈর্যশীল হতে এবং পরীক্ষার মুহূর্তে আল্লাহর ওপর নির্ভর করতে শিখিয়েছিলেন।\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَبِيًّا وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর কিতাবে ইদ্রিসের কথা উল্লেখ করুন। তিনি ছিলেন অত্যন্ত সত্যবাদী এবং একজন নবী। আমি তাকে উচ্চ মর্যাদার স্থানে উন্নীত করেছি।\n"
                            + "<b>[সূরা মারইয়াম, আয়াত ১৯:৫৬-৫৭]</b>\n\n"
                            + "ইমাম ইবন কাসির তার তাফসিরে উল্লেখ করেছেন, ইদ্রিস (আলাইহিস সালাম)-কে কলম এবং লেখনশিল্পের প্রবর্তক হিসেবে গণ্য করা হয়। এছাড়া, তিনি প্রথম ব্যক্তি যিনি আল্লাহর সন্তুষ্টির জন্য যুদ্ধ করেছিলেন এবং ন্যায়বিচার প্রতিষ্ঠার জন্য কাজ করেছিলেন।",
                    "During the era of Prophet Idris (peace be upon him), the foundational teachings of monotheism taught by Adam (AS) had waned, and ignorance, worldly indulgence, and disobedience had begun to corrupt society. Allah commissioned Idris (AS) with prophethood to guide humanity back to divine guidance.\n\n"
                            + "<b>Key Dimensions of His Dawah:</b>\n"
                            + "• <b>Proclamation of Tawheed:</b> Calling mankind to single-minded devotion to Allah and rejecting false gods.\n"
                            + "• <b>Moral and Social Reform:</b> Instilling integrity, justice, and compassion while combating oppression.\n"
                            + "• <b>Imparting Knowledge and Skills:</b> Teaching writing, arithmetic, astronomy, and tailoring to advance society.\n"
                            + "• <b>Steadfast Patience and Trust in Allah:</b> Teaching reliance upon the Almighty in all circumstances.\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَبِيًّا وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>Translation:</b>\n"
                            + "And mention in the Book, Idris. Indeed, he was a man of truth and a prophet. And We raised him to a high station.\n"
                            + "<b>[Surah Maryam, 19:56-57]</b>"
            ));

            // 6. প্রথম যুদ্ধের নেতৃত্ব
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "প্রথম যুদ্ধের নেতৃত্ব",
                    "Leadership in the First Battle",
                    "ইদ্রিস (আলাইহিস সালাম)-এর সময়ের ঘটনাবলি ও দাওয়াতের অন্যতম গুরুত্বপূর্ণ দিক ছিল প্রথম যুদ্ধের নেতৃত্ব। ইসলামী ঐতিহ্যের বর্ণনা অনুযায়ী, ইদ্রিস (আলাইহিস সালাম) প্রথম নবী যিনি আল্লাহর সন্তুষ্টির জন্য অন্যায়, জুলুম ও পাপাচারের বিরুদ্ধে সংগ্রামের নেতৃত্ব দিয়েছিলেন। এটি ইতিহাসের প্রথম সশস্ত্র সংগ্রামের উদাহরণ হিসেবে পরিচিত।\n\n"
                            + "<b>ইদ্রিস (আলাইহিস সালাম)-এর যুদ্ধের কারণ:</b>\n\n"
                            + "• <b>সমাজে শিরক ও পাপাচার বৃদ্ধি:</b> ইদ্রিস (আলাইহিস সালাম)-এর সময় সমাজে আল্লাহর আনুগত্য ত্যাগ করা এবং শিরক, পাপ ও অন্যায় কর্মকাণ্ড ব্যাপকভাবে ছড়িয়ে পড়েছিল। মানুষের মধ্যে নৈতিকতার অভাব প্রকট ছিল।\n\n"
                            + "• <b>জুলুম ও দুর্নীতি:</b> শক্তিশালী শ্রেণি দুর্বলদের উপর অত্যাচার চালাতো। সমাজে অন্যায়, দখল ও লুটপাট বেড়ে গিয়েছিল। ইদ্রিস (আলাইহিস সালাম) এর বিরুদ্ধে আল্লাহর নির্দেশে ব্যবস্থা গ্রহণ করেন।\n\n"
                            + "• <b>সত্যের প্রতিষ্ঠা:</b> ইদ্রিস (আলাইহিস সালাম) আল্লাহর পক্ষ থেকে নবুয়তের মাধ্যমে পাঠানো হয়েছিলেন মানুষকে সত্যের পথে আহ্বান করার জন্য। যখন মানুষ তার দাওয়াত গ্রহণ করতে অস্বীকৃতি জানায় এবং অন্যায়ের ওপর অটল থাকে, তখন তিনি তাদের বিরুদ্ধে সংগ্রামে নেতৃত্ব দেন।\n\n"
                            + "<b>যুদ্ধের নেতৃত্ব:</b>\n"
                            + "ইবনে কাসির (রহিমাহুল্লাহ) তার গ্রন্থে উল্লেখ করেছেন যে, ইদ্রিস (আলাইহিস সালাম) প্রথম নবী ছিলেন যিনি যুদ্ধের কৌশল শিখিয়েছিলেন এবং সমাজে শৃঙ্খলা ফিরিয়ে আনতে সশস্ত্র সংগ্রাম করেছিলেন।\n\n"
                            + "<b>তার নেতৃত্বের বৈশিষ্ট্য:</b>\n"
                            + "• <b>আল্লাহর ওপর পূর্ণ আস্থা:</b> ইদ্রিস (আলাইহিস সালাম)-এর যেকোনো কাজ ছিল আল্লাহর নির্দেশের ওপর ভিত্তি করে। তিনি যুদ্ধের জন্য সাহস ও ধৈর্য নিয়ে নেতৃত্ব দিয়েছিলেন।\n"
                            + "• <b>ন্যায় ও সত্য প্রতিষ্ঠা:</b> তার যুদ্ধ ছিল জুলুমের বিরুদ্ধে, এবং এর লক্ষ্য ছিল আল্লাহর আইনের প্রতিষ্ঠা।\n"
                            + "• <b>মানবতার কল্যাণ:</b> যুদ্ধের মাধ্যমে ইদ্রিস (আলাইহিস সালাম) চেষ্টা করেছিলেন সমাজে ন্যায়বিচার প্রতিষ্ঠা করতে এবং দুর্নীতি দূর করতে।\n\n"
                            + "وَقَاتِلُوهُمْ حَتَّىٰ لَا تَكُونَ فِتْنَةٌ وَيَكُونَ ٱلدِّينُ لِلَّهِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাদের সাথে যুদ্ধ করো যতক্ষণ না ফিতনা দূর হয় এবং দ্বীন একমাত্র আল্লাহর জন্য প্রতিষ্ঠিত হয়।\n"
                            + "<b>[সূরা বাকারা, আয়াত ২:১৯৩]</b>\n\n"
                            + "ইদ্রিস (আলাইহিস সালাম)-এর নেতৃত্বে প্রথম যুদ্ধ ছিল এক ঐতিহাসিক ঘটনা যা সমাজে সত্য, ন্যায় ও শান্তি প্রতিষ্ঠার জন্য অনন্য উদাহরণ। এটি আমাদের শেখায় যে অন্যায়ের বিরুদ্ধে অবস্থান নেওয়া এবং ন্যায়বিচারের জন্য সংগ্রাম করা নবীদের পথ এবং ইসলামের গুরুত্বপূর্ণ অংশ।",
                    "According to classical Islamic historiography, Prophet Idris (peace be upon him) was the first prophet to lead a struggle against corruption, oppression, and injustice in the way of Allah. This marks the earliest recorded righteous struggle for establishing order and justice.\n\n"
                            + "<b>Causes of the Struggle:</b>\n"
                            + "• <b>Rampant Idolatry and Iniquity:</b> Moral corruption and rebellion against divine commandments.\n"
                            + "• <b>Combating Tyranny and Injustice:</b> Powerful oppressors terrorizing the weak and vulnerable.\n"
                            + "• <b>Establishing Truth:</b> Defending righteous believers when adversaries refused peaceful dawah and persisted in tyranny.\n\n"
                            + "<b>Leadership Qualities:</b>\n"
                            + "• Absolute reliance and trust in Allah.\n"
                            + "• Strategic military discipline for establishing social peace.\n"
                            + "• Sincere dedication to justice and the welfare of humanity.\n\n"
                            + "وَقَاتِلُوهُمْ حَتَّىٰ لَا تَكُونَ فِتْنَةٌ وَيَكُونَ ٱلدِّينُ لِلَّهِ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Fight them until there is no more chaos and worship is solely for Allah.\n"
                            + "<b>[Surah Al-Baqarah, 2:193]</b>"
            ));

            // 7. বেহেশতে উত্তোলন ও বিশেষ মর্যাদা
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "বেহেশতে উত্তোলন ও বিশেষ মর্যাদা",
                    "Ascension to Heaven and Exalted Status",
                    "ইদ্রিস (আলাইহিস সালাম) একজন মহান নবী ছিলেন, যিনি আল্লাহর প্রতি তাঁর আনুগত্য এবং মানুষকে ন্যায়ের পথে আহ্বান করার জন্য বিশেষ মর্যাদা লাভ করেন। ইসলামী ঐতিহ্যের অন্যতম অনন্য ঘটনা হলো আল্লাহ তাকে বেহেশতে উত্তোলন করেছেন। কুরআন এবং হাদিসের আলোকে ইদ্রিস (আলাইহিস সালাম)-এর এই বিশেষ মর্যাদার বর্ণনা পাওয়া যায়।\n\n"
                            + "<b>ইমাম ইবন কাসিরের মতে:</b>\n"
                            + "ইমাম ইবন কাসির (রহিমাহুল্লাহ) তার তাফসিরে বলেন, আল্লাহ ইদ্রিস (আলাইহিস সালাম)-কে আসমানের দিকে উত্তোলন করেছিলেন। তিনি পৃথিবীতে ন্যায় ও সত্য প্রতিষ্ঠায় গুরুত্বপূর্ণ ভূমিকা পালন করেছিলেন। আল্লাহ তাঁর আনুগত্যের পুরস্কার হিসেবে তাঁকে আসমানে উত্তোলন করেন এবং বিশেষ মর্যাদা প্রদান করেন।\n\n"
                            + "<b>ইবনে আব্বাস (রাদিয়াল্লাহু আনহু)-এর বর্ণনা:</b>\n"
                            + "ইবনে আব্বাস (রাদিয়াল্লাহু আনহু) থেকে বর্ণিত যে, ইদ্রিস (আলাইহিস সালাম) আসমানের দিকে জীবিত অবস্থায় উত্তোলিত হন এবং চতুর্থ আসমানে পৌঁছান।\n\n"
                            + "<b>মুজাহিদের মতামত:</b>\n"
                            + "মুজাহিদ (রহিমাহুল্লাহ) বলেছেন, \"উচ্চ মর্যাদা\" দ্বারা আসমানে উত্তোলন এবং বিশেষ মর্যাদা বোঝানো হয়েছে।\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَبِيًّا وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর কিতাবে ইদ্রিসের কথা উল্লেখ করুন। তিনি ছিলেন অত্যন্ত সত্যবাদী এবং একজন নবী। আমি তাকে উচ্চ মর্যাদার স্থানে উন্নীত করেছি।\n"
                            + "<b>[সূরা মারইয়াম, আয়াত ১৯:৫৬-৫৭]</b>\n\n"
                            + "এই আয়াতে \"وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\" (আমরা তাকে উচ্চ মর্যাদার স্থানে উন্নীত করেছি) শব্দগুচ্ছটি ইদ্রিস (আলাইহিস সালাম)-এর আসমানে উত্তোলনের ইঙ্গিত দেয়। তাফসিরবিদগণ একে আক্ষরিক অর্থে আসমানের দিকে উত্তোলন এবং আধ্যাত্মিকভাবে উচ্চ মর্যাদায় প্রতিষ্ঠিত হওয়ার ইঙ্গিত হিসেবে ব্যাখ্যা করেছেন।\n\n"
                            + "<b>বেহেশতে উত্তোলনের কারণ:</b>\n"
                            + "• <b>সততা ও সত্যবাদিতা:</b> কুরআনে ইদ্রিস (আলাইহিস সালাম)-এর সততা এবং নবুয়তকে বিশেষভাবে উল্লেখ করা হয়েছে। তার নিষ্ঠা এবং সততার জন্য আল্লাহ তাকে এই বিশেষ মর্যাদা দেন।\n"
                            + "• <b>ইবাদত ও ন্যায় প্রতিষ্ঠা:</b> ইদ্রিস (আলাইহিস সালাম) অত্যন্ত ইবাদতগুজার এবং আল্লাহর আইন প্রতিষ্ঠায় নিবেদিত ছিলেন। তার এই আমল তাকে আল্লাহর নিকট প্রিয় করে তোলে।\n"
                            + "• <b>আল্লাহর প্রতি আনুগত্য:</b> তিনি আল্লাহর প্রতি পূর্ণ বিশ্বাস ও আনুগত্য প্রদর্শন করেন। এর ফলে আল্লাহ তাকে আসমানের দিকে উত্তোলন করেন এবং চতুর্থ আসমানে স্থাপন করেন।\n\n"
                            + "ইদ্রিস (আলাইহিস সালাম)-এর বেহেশতে উত্তোলন তাঁর জীবনের আমল, সততা এবং আল্লাহর প্রতি আনুগত্যের পুরস্কার। এই ঘটনা আমাদের শেখায় যে আল্লাহর পথে জীবন পরিচালিত করলে আখিরাতে সম্মানিত হওয়া সম্ভব। কুরআন ও হাদিসে এ বিষয়ে যে শিক্ষা প্রদান করা হয়েছে, তা মুসলমানদের জন্য দিকনির্দেশনা ও অনুপ্রেরণার উৎস।",
                    "Prophet Idris (peace be upon him) was granted an exalted spiritual and physical elevation by Almighty Allah due to his unmatched truthfulness, unwavering devotion, and righteousness.\n\n"
                            + "<b>View of Imam Ibn Kathir:</b>\n"
                            + "In his commentary, Ibn Kathir (rahimahullah) explains that Allah elevated Idris (AS) to the heavens as a divine honor for his exceptional service to truth and justice.\n\n"
                            + "<b>Narration of Ibn Abbas (RA) and Mujahid:</b>\n"
                            + "Ibn Abbas (RA) stated that Idris (AS) was raised alive to the heavens and stationed in the fourth heaven. Mujahid commented that 'high station' refers to both physical ascension and supreme spiritual status.\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِدْرِيسَ ۚ إِنَّهُ كَانَ صِدِّيقًا نَبِيًّا وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>Translation:</b>\n"
                            + "And mention in the Book, Idris. Indeed, he was a man of truth and a prophet. And We raised him to a high station.\n"
                            + "<b>[Surah Maryam, 19:56-57]</b>\n\n"
                            + "<b>Reasons for Divine Elevation:</b>\n"
                            + "• Exemplary truthfulness and sincerity (Siddiq).\n"
                            + "• Profound devotion to prayer, fasting, and justice.\n"
                            + "• Absolute surrender and obedience to Allah's commands."
            ));

            // 8. মিরাজে নবী (সা.)-এর সাথে সাক্ষাৎ
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "মিরাজে নবী (সা.)-এর সাথে সাক্ষাৎ",
                    "Meeting with Prophet (SAW) during Miraj",
                    "মিরাজ (الإسراء والمعراج) ছিল রাসূলুল্লাহ (ﷺ)-এর জীবনের অন্যতম বিশেষ ঘটনা, যেখানে তিনি আকাশের বিভিন্ন স্তরে নবীদের সাথে সাক্ষাৎ করেছিলেন। এ সময় চতুর্থ আকাশে ইদ্রিস (আলাইহিস সালাম)-এর সাথে সাক্ষাতের ঘটনা বর্ণিত হয়েছে। এই সাক্ষাৎ নবী (ﷺ)-এর নবুয়ত এবং ইদ্রিস (আলাইহিস সালাম)-এর মর্যাদার একটি স্পষ্ট উদাহরণ।\n\n"
                            + "ثُمَّ رُفِعَ لِي إِلَى السَّمَاءِ الرَّابِعَةِ، فَإِذَا أَنَا بِإِدْرِيسَ، فَقَالَ: مَرْحَبًا بِالنَّبِيِّ الصَّالِحِ وَالْأَخِ الصَّالِحِ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারপর আমাকে চতুর্থ আসমানে উঠানো হলো। সেখানে আমি ইদ্রিস (আলাইহিস সালাম)-এর সাথে সাক্ষাৎ করলাম। তিনি বললেন, 'সৎ নবী এবং সৎ ভাইকে স্বাগতম।'\n"
                            + "<b>[সহীহ বুখারি, হাদিস: ৩২০৭; সহীহ মুসলিম, হাদিস: ১৬২]</b>\n\n"
                            + "<b>আসমানের স্তর এবং ইদ্রিস (আলাইহিস সালাম):</b>\n"
                            + "ইদ্রিস (আলাইহিস সালাম) চতুর্থ আকাশে অবস্থান করছেন, যা কুরআনের আয়াত এবং তাফসির থেকে স্পষ্ট হয়। আল্লাহ বলেন:\n\n"
                            + "وَرَفَعْنَاهُ مَكَانًا عَلِيًّا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমি তাকে উচ্চ মর্যাদার স্থানে উন্নীত করেছি।\n"
                            + "<b>[সূরা মারইয়াম, আয়াত ১৯:৫৭]</b>\n\n"
                            + "এই উচ্চ মর্যাদা আল্লাহর নির্দেশে আসমানে উত্তোলনের নির্দেশ করে। মিরাজের সময় রাসূলুল্লাহ (ﷺ)-এর সাথে সাক্ষাৎ এই মর্যাদার একটি দৃষ্টান্ত।\n\n"
                            + "<b>সাক্ষাতের তাৎপর্য:</b>\n"
                            + "• <b>ইদ্রিস (আলাইহিস সালাম)-এর মর্যাদা:</b> ইদ্রিস (আলাইহিস সালাম)-কে আসমানের উচ্চস্থানে স্থাপন করা তাঁর সততা, আল্লাহর আনুগত্য এবং ইবাদতের প্রতি নিষ্ঠার প্রতিফল। তিনি ছিলেন কলম এবং জ্ঞানচর্চার প্রবর্তক। মিরাজে নবী (ﷺ)-এর সাথে তাঁর সাক্ষাৎ তাঁর সম্মানের প্রমাণ।\n"
                            + "• <b>নবীদের মধ্যে সংযোগ:</b> নবীরা সবাই আল্লাহর বার্তাবাহক। মিরাজে এই সাক্ষাতের মাধ্যমে বোঝা যায়, নবীদের মধ্যে একটি ঐক্য এবং ধারাবাহিকতা রয়েছে। এটি নবীদের দাওয়াত ও শিক্ষার সামঞ্জস্যতাও তুলে ধরে।\n"
                            + "• <b>ইদ্রিস (আলাইহিস সালাম)-এর বক্তব্য:</b> তিনি রাসূলুল্লাহ (ﷺ)-কে \"সৎ নবী\" এবং \"সৎ ভাই\" বলে অভিহিত করেন। এটি রাসূলুল্লাহ (ﷺ)-এর মর্যাদার একটি বিশেষ স্বীকৃতি এবং নবীদের মধ্যে পারস্পরিক শ্রদ্ধার উদাহরণ।\n"
                            + "• <b>মানব জাতির জন্য শিক্ষা:</b> মিরাজের সময় নবীদের সাথে সাক্ষাতের ঘটনা মানব জাতিকে নবীদের শিক্ষা গ্রহণ এবং তাদের জীবন অনুসরণের গুরুত্ব শেখায়।\n\n"
                            + "মিরাজের রাতে ইদ্রিস (আলাইহিস সালাম)-এর সাথে রাসূলুল্লাহ (ﷺ)-এর সাক্ষাৎ ছিল এক গুরুত্বপূর্ণ ও তাৎপর্যপূর্ণ ঘটনা। এটি নবীদের মধ্যে সম্পর্ক, আল্লাহর প্রতি তাঁদের আনুগত্য এবং আল্লাহর পথে তাঁদের প্রচেষ্টার পরিচয় বহন করে। ইদ্রিস (আলাইহিস সালাম)-এর উচ্চ মর্যাদা আমাদের শেখায় যে, আল্লাহর পথে ধৈর্য, ইবাদত এবং নৈতিকতার গুরুত্ব অপরিসীম। মিরাজের এই ঘটনা মুসলমানদের জন্য নবীদের জীবনের প্রতি ভালোবাসা এবং তাঁদের আদর্শ অনুসরণের প্রেরণা।",
                    "During the momentous Night Journey and Ascension (Al-Isra wal-Miraj), Prophet Muhammad (ﷺ) ascended through the heavens and met previous prophets. In the fourth heaven, he met Prophet Idris (peace be upon him).\n\n"
                            + "ثُمَّ رُفِعَ لِي إِلَى السَّمَاءِ الرَّابِعَةِ، فَإِذَا أَنَا بِإِدْرِيسَ، فَقَالَ: مَرْحَبًا بِالنَّبِيِّ الصَّالِحِ وَالْأَخِ الصَّالِحِ.\n\n"
                            + "<b>Translation:</b>\n"
                            + "Then I was raised to the fourth heaven and there I saw Idris. He said: 'Welcome to the righteous Prophet and righteous brother!'\n"
                            + "<b>[Sahih al-Bukhari: 3207; Sahih Muslim: 162]</b>\n\n"
                            + "<b>Heavenly Station of Idris (AS):</b>\n"
                            + "Stationed in the fourth heaven, reflecting the Quranic proclamation: 'And We raised him to a high station' (19:57).\n\n"
                            + "<b>Significance of the Encounter:</b>\n"
                            + "• Proof of the elevated station bestowed on Idris (AS).\n"
                            + "• The brotherhood, unity, and continuity among all of Allah's messengers.\n"
                            + "• His greeting of 'righteous brother and righteous prophet' acknowledged the supreme station of Prophet Muhammad (ﷺ)."
            ));
        } else if (prophetId == 3) {
            // হযরত নূহ (আ:) - All 9 Chapters / Topics 100% Verbatim from User Request & Screenshots

            // 1. নূহ (আঃ)- এর পরিচয় ও বংশধারা
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "নূহ (আঃ)- এর পরিচয় ও বংশধারা",
                    "Identity and Lineage of Nuh (AS)",
                    "নুহ (আলাইহিস সালাম) পৃথিবীর প্রথম নবীদের একজন। তিনি মানুষের জন্য একত্ববাদ (তাওহিদ)-এর শিক্ষা নিয়ে এসেছিলেন এবং আল্লাহর প্রতি মানুষের আনুগত্যের জন্য নবুয়ত লাভ করেছিলেন। তাঁর নাম কুরআনে ৪৩ বার উল্লেখিত হয়েছে। তাঁর জীবন থেকে ইসলামের পথপ্রদর্শক, ধৈর্য, দাওয়াত এবং আল্লাহর প্রতি আনুগত্যের শিক্ষা পাওয়া যায়। আল্লাহ তাআলা নুহ (আলাইহিস সালাম)-কে নবী হিসেবে প্রেরণ করেন এবং তাওহিদের বার্তা প্রচার করতে আদেশ দেন:\n\n"
                            + "إِنَّا أَرۡسَلۡنَا نُوحًا إِلَىٰ قَوۡمِهِۦٓ أَنۡ أَنذِرۡ قَوۡمَكَ مِن قَبۡلِ أَن يَأۡتِيَهُمۡ عَذَابٌ أَلِيمٌ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই আমি নুহকে তাঁর সম্প্রদায়ের প্রতি প্রেরণ করেছিলাম এই বলে যে, ‘তোমার সম্প্রদায়কে সতর্ক কর, যেহেতু তাদের ওপর যন্ত্রণাদায়ক শাস্তি আসার আশঙ্কা রয়েছে।\n"
                            + "<b>[সূরা নুহ, আয়াত ৭১:১]</b>\n\n"
                            + "নুহ (আলাইহিস সালাম) আদম (আলাইহিস সালাম)-এর পরবর্তী নবীদের মধ্যে অন্যতম। তিনি শিরক ও কুসংস্কারের বিরুদ্ধে মানুষকে এক আল্লাহর ইবাদতের দাওয়াত দিয়েছিলেন।\n\n"
                            + "إِنَّهُ كَانَ عَبۡدًا شَكُورًا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই তিনি ছিলেন কৃতজ্ঞ বান্দা।\n"
                            + "<b>[সূরা ইসরাঃ, আয়াত ১৭:৩]</b>\n\n"
                            + "এই আয়াত থেকে বোঝা যায়, নুহ (আলাইহিস সালাম) আল্লাহর প্রতি অত্যন্ত কৃতজ্ঞ এবং আনুগত্যশীল ছিলেন। তাঁর পরিচিতির অন্যতম বৈশিষ্ট্য ছিল ধৈর্য এবং আল্লাহর প্রতি একনিষ্ঠতা।\n\n"
                            + "<b>জন্ম ও বংশধারা:</b>\n"
                            + "নুহ (আলাইহিস সালাম) আদম (আলাইহিস সালাম)-এর বংশধর। তিনি আদম (আলাইহিস সালাম)-এর সপ্তম প্রজন্ম। ইসলামী ঐতিহ্যে তাঁর বংশধারা নিম্নরূপ:\n\n"
                            + "• আদম (আলাইহিস সালাম)\n"
                            + "• শীষ (আলাইহিস সালাম)\n"
                            + "• আনুশ\n"
                            + "• কাইনান\n"
                            + "• মেহলায়েল\n"
                            + "• ইদ্রিস (আলাইহিস সালাম)\n"
                            + "• নুহ (আলাইহিস সালাম)\n\n"
                            + "<b>ইসলামী ঐতিহ্যে জন্মের স্থান:</b>\n"
                            + "ইসলামী ঐতিহ্যে নুহ (আলাইহিস সালাম)-এর জন্মস্থান হিসেবে বর্তমান ইরাক বা মেসোপটেমিয়া অঞ্চলের কথা উল্লেখ করা হয়। তিনি এমন এক সময় জন্মগ্রহণ করেছিলেন যখন পৃথিবীতে তাওহিদ থেকে দূরে সরে মানুষ শিরকের দিকে ঝুঁকছিল।\n\n"
                            + "<b>বংশধারা সম্পর্কে কুরআনের ইঙ্গিত:</b>\n"
                            + "কুরআনে নুহ (আলাইহিস সালাম)-এর বংশধরদের বিশেষ গুরুত্ব দেওয়া হয়েছে:\n\n"
                            + "وَجَعَلۡنَا ذُرِّيَّتَهُۥ هُمُ ٱلۡبَاقِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমরা তাঁর বংশধরদেরকেই অবশিষ্ট রেখেছি।\n"
                            + "<b>[সূরা সাফফাত, আয়াত ৩৭:৭৭]</b>\n\n"
                            + "এই আয়াত থেকে বোঝা যায়, প্লাবনের পরে তাঁর বংশধরদের মাধ্যমে মানবজাতির নতুন অধ্যায় শুরু হয়।\n\n"
                            + "<b>জন্মের সময় ও প্রসঙ্গ:</b>\n"
                            + "নুহ (আলাইহিস সালাম)-এর জন্ম সময়কাল ইসলামী ঐতিহ্যে সুনির্দিষ্টভাবে উল্লেখ নেই। তবে তাফসিরবিদগণ মনে করেন যে, আদম (আলাইহিস সালাম)-এর সৃষ্টি থেকে প্রায় ১০০০ বছর পর তিনি জন্মগ্রহণ করেন।\n\n"
                            + "<b>শিক্ষা ও জীবনযাত্রার প্রভাব:</b>\n"
                            + "নুহ (আলাইহিস সালাম)-এর পিতা লামাক এবং পূর্বপুরুষ ইদ্রিস (আলাইহিস সালাম)-এর প্রভাবে তিনি ছোটবেলা থেকেই আল্লাহর প্রতি আস্থা এবং ন্যায়ের পথে চলার শিক্ষা লাভ করেন। এই শিক্ষা তাঁকে নবুয়তের জন্য প্রস্তুত করেছিল।\n\n"
                            + "নুহ (আলাইহিস সালাম)-এর পরিচয়, জন্ম, এবং বংশধারা ইসলামের ইতিহাসে অত্যন্ত গুরুত্বপূর্ণ। তিনি আদম (আলাইহিস সালাম)-এর বংশধর এবং মানবজাতির জন্য এক নতুন অধ্যায়ের সূচনা করেন। তাঁর জীবন আমাদের জন্য ধৈর্য, তাওহিদ, এবং আল্লাহর প্রতি পূর্ণ আনুগত্যের শিক্ষা দেয়।",
                    "Prophet Nuh (peace be upon him) was one of the earliest and greatest messengers sent by Allah to revive monotheism (Tawheed) among humankind. Mentioned 43 times in the Holy Quran, his legacy is defined by unwavering perseverance, continuous dawah, and supreme devotion.\n\n"
                            + "إِنَّا أَرۡسَلۡنَا نُوحًا إِلَىٰ قَوۡمِهِۦٓ أَنۡ أَنذِرۡ قَوۡمَكَ مِن قَبۡلِ أَن يَأۡتِيَهُمۡ عَذَابٌ أَلِيمٌ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Indeed, We sent Noah to his people saying: 'Warn your people before there comes to them a painful punishment.'\n"
                            + "<b>[Surah Nuh, 71:1]</b>\n\n"
                            + "إِنَّهُ كَانَ عَبۡدًا شَكُورًا\n\n"
                            + "<b>Translation:</b>\n"
                            + "Indeed, he was a grateful servant.\n"
                            + "<b>[Surah Al-Isra, 17:3]</b>\n\n"
                            + "<b>Lineage & Birthplace:</b>\n"
                            + "Traced back to Prophet Adam (AS) through Seth (Sheeth AS), Enoch (Idris AS), and Lamech. He was born in Mesopotamia (modern Iraq) during a period of rampant idolatry and moral decline."
            ));

            // 2. নবুয়ত লাভ ও দাওয়াতের সূচনা
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "নবুয়ত লাভ ও দাওয়াতের সূচনা",
                    "Attainment of Prophethood and Beginning of Dawah",
                    "হযরত নূহ (আলাইহিস সালাম) ছিলেন মানবজাতির দ্বিতীয় প্রধান নবী এবং প্রথম রাসূল (পাঠানো নবী)। তিনি এমন একটি জাতির কাছে পাঠানো হয়েছিলেন, যারা তাওহীদ (আল্লাহর একত্ব) ভুলে গিয়ে শিরক ও মূর্তিপূজায় লিপ্ত ছিল। আল্লাহ তাআলা তাঁকে নবুওয়াত দান করেন এবং তিনি সাড়ে নয়শ বছর ধরে (৯৫০ বছর) তাঁর জাতির মাঝে দাওয়াত দেন। তাঁর নবুওয়াত ও দাওয়াতের ঘটনা পবিত্র কুরআন ও হাদিসে বিস্তারিতভাবে বর্ণিত হয়েছে।\n\n"
                            + "<b>নূহ (আ.)-এর নবুওয়ত লাভ:</b>\n"
                            + "নূহ (আলাইহিস সালাম) যখন নবুওয়াত প্রাপ্ত হন, তখন তাঁর জাতি শিরকের মধ্যে নিমজ্জিত ছিল। তারা বিভিন্ন দেবতার উপাসনা করত, যেমন: ওয়াদ, সুয়াআ, ইয়াগুছ, ইয়াঊক, এবং নাসর।\n"
                            + "<b>[সূরা নূহ: ৭১:২৩]</b>\n\n"
                            + "তাদের সংশোধনের জন্য আল্লাহ তাঁকে নবী হিসেবে মনোনীত করেন।\n\n"
                            + "কুরআনে বলা হয়েছে:\n\n"
                            + "إِنَّآ أَرۡسَلۡنَا نُوحًا إِلَىٰ قَوۡمِهِۦٓ أَنۡ أَنذِرۡ قَوۡمَكَ مِن قَبۡلِ أَن يَأۡتِيَهُمۡ عَذَابٌ أَلِيمٞ ١\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই আমরা নূহকে তাঁর সম্প্রদায়ের কাছে পাঠিয়েছিলাম এই বলে যে, ‘তোমার সম্প্রদায়কে সতর্ক কর, যেহেতু তাদের উপর এক কঠিন শাস্তি আসবে।\n"
                            + "<b>[সূরা নূহ: ৭১:১]</b>\n\n"
                            + "<b>নূহ (আ.)-এর দাওয়াতের মূল বিষয়বস্তু:</b>\n"
                            + "নূহ (আ.)-এর দাওয়াত ছিল তাঁর জাতিকে এক আল্লাহর ইবাদতের দিকে আহ্বান করা এবং তাদের শিরক থেকে বিরত রাখা।\n\n"
                            + "কুরআনে বলা হয়েছে:\n\n"
                            + "لَقَدۡ أَرۡسَلۡنَا نُوحًا إِلَىٰ قَوۡمِهِۦ فَقَالَ يَٰقَوۡمِ ٱعۡبُدُواْ ٱللَّهَ مَا لَكُم مِّنۡ إِلَٰهٍ غَيۡرُهُۥٓ إِنِّيٓ أَخَافُ عَلَيۡكُمۡ عَذَابَ يَوۡمٍ عَظِيمٖ ٥٩\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমরা নূহকে তাঁর সম্প্রদায়ের কাছে পাঠিয়েছিলাম। তিনি বললেন, ‘হে আমার সম্প্রদায়! তোমরা আল্লাহর ইবাদত করো। তোমাদের জন্য আল্লাহ ছাড়া আর কোনো উপাস্য নেই। আমি তোমাদের জন্য এক মহাদিনের শাস্তি আশঙ্কা করছি।\n"
                            + "<b>[সূরা আরাফ: ৭:৫৯]</b>\n\n"
                            + "<b>দাওয়াতের পদ্ধতি:</b>\n"
                            + "নূহ (আলাইহিস সালাম) তাঁর দাওয়াতে বিভিন্ন পদ্ধতি অবলম্বন করেছিলেন:\n"
                            + "• তিনি তাদেরকে প্রকাশ্যে ও গোপনে আহ্বান জানিয়েছেন।\n"
                            + "• তিনি বিভিন্ন সময়ে নরম ভাষায় তাদেরকে সতর্ক করেছেন।\n\n"
                            + "কুরআনে বলা হয়েছে:\n\n"
                            + "قَالَ رَبِّ إِنِّي دَعَوۡتُ قَوۡمِي لَيۡلٗا وَنَهَارٗا ٥ فَلَمۡ يَزِدۡهُمۡ دُعَآءِيٓ إِلَّا فِرَارٗا ٦\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নূহ বললেন, ‘হে আমার প্রতিপালক! আমি আমার সম্প্রদায়কে দিন-রাত দাওয়াত দিয়েছি। কিন্তু আমার দাওয়াত তাদেরকে আরও বেশি দূরে সরিয়ে দিয়েছে।\n"
                            + "<b>[সূরা নূহ: ৭১:৫-৬]</b>\n\n"
                            + "<b>গোপন ও প্রকাশ্য দাওয়াত:</b>\n\n"
                            + "ثُمَّ إِنِّي دَعَوۡتُهُمۡ جِهَارٗا ٨ ثُمَّ إِنِّيٓ أَعۡلَنتُ لَهُمۡ وَأَسۡرَرۡتُ لَهُمۡ إِسۡرَارٗا ٩\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারপর আমি তাদেরকে প্রকাশ্যে আহ্বান করেছি। এরপর আমি তাদেরকে প্রকাশ্যে এবং গোপনেও দাওয়াত দিয়েছি।\n"
                            + "<b>[সূরা নূহ: ৭১:৮-৯]</b>\n\n"
                            + "<b>জাতির প্রতিক্রিয়া:</b>\n"
                            + "নূহ (আলাইহিস সালাম)-এর জাতি তাঁর আহ্বান প্রত্যাখ্যান করে এবং তাঁকে উপহাস করে। তারা তাঁকে মিথ্যাবাদী বলত এবং বলত যে তিনি তাদের থেকে কোনো দিক থেকে শ্রেষ্ঠ নন।\n\n"
                            + "কুরআনে বলা হয়েছে:\n\n"
                            + "فَقَالَ ٱلۡمَلَأُ ٱلَّذِينَ كَفَرُواْ مِن قَوۡمِهِۦ مَا نَرَىٰكَ إِلَّا بَشَرٗا مِّثۡلَنَا وَمَا نَرَىٰكَ ٱتَّبَعَكَ إِلَّا ٱلَّذِينَ هُمۡ أَرَاذِلُنَا بَادِيَ ٱلرَّأۡيِ وَمَا نَرَىٰ لَكُمۡ عَلَيۡنَا مِن فَضۡلِۢ بَلۡ نَظُنُّكُمۡ كَٰذِبِينَ ٢٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তখন তাঁর সম্প্রদায়ের নেতারা, যারা কাফের ছিল, বলল, ‘আমরা তোমাকে আমাদের মতো একজন মানুষ ছাড়া আর কিছুই মনে করি না। আমরা দেখি তোমার অনুসরণ করছে কেবল আমাদের নিম্নশ্রেণির লোকেরা এবং তাও হঠকারীভাবে। আমরা তোমাদের মধ্যে কোনো শ্রেষ্ঠত্ব দেখি না। বরং আমরা তোমাদের মিথ্যাবাদী মনে করি।\n"
                            + "<b>[সূরা হুদ: ১১:২৭]</b>\n\n"
                            + "<b>ধৈর্য ও শাস্তির ঘোষণা:</b>\n"
                            + "নূহ (আলাইহিস সালাম) সাড়ে নয়শ বছর ধরে ধৈর্য ধরে দাওয়াত দিয়েছেন। কিন্তু তাঁদের অস্বীকার এবং অবাধ্যতা দেখে তিনি আল্লাহর কাছে তাঁদের ধ্বংসের জন্য দোয়া করেন।\n\n"
                            + "কুরআনে বলা হয়েছে:\n\n"
                            + "وَقَالَ نُوحٞ رَّبِّ لَا تَذَرۡ عَلَى ٱلۡأَرۡضِ مِنَ ٱلۡكَٰفِرِينَ دَيَّارًا ٢٦\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নূহ বললেন, ‘হে আমার প্রভু! তুমি পৃথিবীতে কোনো কাফেরকেই অবশিষ্ট রেখো না।\n"
                            + "<b>[সূরা নূহ: ৭১:২৬]</b>",
                    "Prophet Nuh (AS) was the first universal Messenger (Rasul) sent with divine law to combat the five major idols: Wadd, Suwa', Yaghuth, Ya'uq, and Nasr (71:23). For 950 years, he preached day and night through gentleness, public sermons, and private counsel."
            ));

            // 3. স্বীয় কওমের প্রতি নূহ (আঃ)-এর দাওয়াত
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "স্বীয় কওমের প্রতি নূহ (আঃ)-এর দাওয়াত",
                    "Nuh (AS)'s Dawah to His People",
                    "হযরত নূহ (আলাইহিস সালাম) ছিলেন মানবজাতির প্রথম রাসূল। তিনি তাঁর কওমকে দীর্ঘ ৯৫০ বছর তাওহীদের দাওয়াত দিয়েছেন। তাঁর দাওয়াতের মূল লক্ষ্য ছিল এক আল্লাহর ইবাদতের দিকে আহ্বান এবং শিরক ও পাপাচার থেকে বিরত থাকা। পবিত্র কুরআনে এবং বিভিন্ন হাদিসে নূহ (আলাইহিস সালাম)-এর দাওয়াতের পদ্ধতি, তাঁর কওমের প্রতিক্রিয়া এবং আল্লাহর পক্ষ থেকে প্রেরিত আযাবের বর্ণনা রয়েছে। নূহ (আ.) তাঁর কওমকে সতর্ক করেন যে, তারা যদি আল্লাহর ইবাদত না করে, তাহলে তাদের উপর কঠিন শাস্তি আসবে।\n\n"
                            + "وَإِنِّي كُلَّمَا دَعَوۡتُهُمۡ لِتَغۡفِرَ لَهُمۡ جَعَلُوٓاْ أَصَٰبِعَهُمۡ فِيٓ ءَاذَانِهِمۡ وَٱسۡتَغۡشَوۡاْ ثِيَابَهُمۡ وَأَصَرُّواْ وَٱسۡتَكۡبَرُواْ ٱسۡتِكۡبَارٗا ٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমি যখনই তাদেরকে তোমার ক্ষমা চাইতে ডেকেছি, তখনই তারা তাদের আঙুল তাদের কান বন্ধ করেছে, তাদের কাপড় দিয়ে নিজেদের ঢেকেছে এবং তারা অবিচল থেকেছে ও কঠিন অহংকার করেছে।\n"
                            + "<b>[সূরা নূহ: ৭১:৭]</b>\n\n"
                            + "<b>কওমের প্রতিক্রিয়া:</b>\n"
                            + "নূহ (আ.)-এর কওম তাঁর দাওয়াতকে প্রত্যাখ্যান করে এবং তাঁকে মিথ্যাবাদী বলে উপহাস করে। তারা বলত, \"তুমি আমাদের মতোই একজন মানুষ।\"\n\n"
                            + "فَقَالَ ٱلۡمَلَأُ ٱلَّذِينَ كَفَرُواْ مِن قَوۡمِهِۦ مَا نَرَىٰكَ إِلَّا بَشَرٗا مِّثۡلَنَا وَمَا نَرَىٰكَ ٱتَّبَعَكَ إِلَّا ٱلَّذِينَ هُمۡ أَرَاذِلُنَا بَادِيَ ٱلرَّأۡيِ وَمَا نَرَىٰ لَكُمۡ عَلَيۡنَا مِن فَضۡلِۢ بَلۡ نَظُنُّكُمۡ كَٰذِبِينَ ٢٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তখন তাঁর সম্প্রদায়ের নেতারা, যারা কাফের ছিল, বলল, ‘আমরা তোমাকে আমাদের মতো একজন মানুষ ছাড়া আর কিছুই মনে করি না। আমরা দেখি তোমার অনুসরণ করছে কেবল আমাদের নিম্নশ্রেণির লোকেরা এবং তাও হঠকারীভাবে। আমরা তোমাদের মধ্যে কোনো শ্রেষ্ঠত্ব দেখি না। বরং আমরা তোমাদের মিথ্যাবাদী মনে করি।\n"
                            + "<b>[সূরা হুদ: ১১:২৭]</b>\n\n"
                            + "<b>নূহ (আ.)-এর ধৈর্য ও আল্লাহর কাছে প্রার্থনা:</b>\n"
                            + "দীর্ঘ ৯৫০ বছর দাওয়াত দেওয়ার পরেও, যখন কওম অধিকাংশই শিরক থেকে ফিরে আসেনি, তখন নূহ (আ.) আল্লাহর কাছে দোয়া করেন এবং তাদের ধ্বংস কামনা করেন।\n\n"
                            + "وَقَالَ نُوحٞ رَّبِّ لَا تَذَرۡ عَلَى ٱلۡأَرۡضِ مِنَ ٱلۡكَٰفِرِينَ دَيَّارًا ٢٦ إِنَّكَ إِن تَذَرۡهُمۡ يُضِلُّواْ عِبَادَكَ وَلَا يَلِدُوٓاْ إِلَّا فَاجِرٗا كَفَّارًا ٢٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নূহ বললেন, ‘হে আমার প্রতিপালক! তুমি পৃথিবীতে কোনো কাফেরকেই অবশিষ্ট রেখো না। যদি তুমি তাদের অবশিষ্ট রাখো, তাহলে তারা তোমার বান্দাদেরকে বিপথগামী করবে এবং তারা শুধুমাত্র দুষ্ট ও কাফের সন্তান জন্ম দেবে।\n"
                            + "<b>[সূরা নূহ: ৭১:২৬-২৭]</b>\n\n"
                            + "হযরত নূহ (আলাইহিস সালাম)-এর দাওয়াত মানবজাতির জন্য একটি শিক্ষা। তাঁর জীবনের মূল বার্তা হলো ধৈর্য, তাওহীদের প্রতি অবিচলতা, এবং আল্লাহর প্রতি পূর্ণ নির্ভরতা। দীর্ঘ ৯৫০ বছর দাওয়াত দিয়েও তিনি হতাশ হননি এবং শেষ মুহূর্ত পর্যন্ত তাঁর দাওয়াত চালিয়ে গেছেন।\n\n"
                            + "اللَّهُمَّ اجْعَلْنَا مِنَ الَّذِينَ يَدْعُونَ إِلَىٰ سَبِيلِكَ بِالْحِكْمَةِ وَالْمَوْعِظَةِ الْحَسَنَةِ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে আল্লাহ! আমাদেরকে তাদের অন্তর্ভুক্ত করুন, যারা জ্ঞানের মাধ্যমে এবং উত্তম উপদেশের মাধ্যমে তোমার পথে আহ্বান জানায়।",
                    "Despite extreme hostility—people covering their faces and thrusting fingers into their ears to shun divine truth—Prophet Nuh (AS) persisted with infinite patience and reliance upon Allah."
            ));

            // 4. দীর্ঘ জীবন ও ধৈর্য
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "দীর্ঘ জীবন ও ধৈর্য",
                    "Longevity and Patience",
                    "হযরত নূহ (আলাইহিস সালাম) ইসলামের ইতিহাসে ধৈর্য এবং দীর্ঘায়ুর অন্যতম সেরা উদাহরণ। তিনি মানবজাতির প্রথম রাসূল ছিলেন, যাঁকে আল্লাহ তাঁর কওমের কাছে পাঠিয়েছিলেন তাওহীদের দাওয়াত দেওয়ার জন্য। তাঁর দীর্ঘ ৯৫০ বছরের জীবন ছিল ধৈর্য এবং আল্লাহর প্রতি অবিচল বিশ্বাসের এক মহাকাব্যিক উদাহরণ।\n\n"
                            + "<b>নূহ (আ.)-এর দীর্ঘ জীবন:</b>\n"
                            + "পবিত্র কুরআনে হযরত নূহ (আলাইহিস সালাম)-এর দীর্ঘ জীবনের কথা স্পষ্টভাবে উল্লেখ করা হয়েছে।\n\n"
                            + "وَلَقَدۡ أَرۡسَلۡنَا نُوحًا إِلَىٰ قَوۡمِهِۦ فَلَبِثَ فِيهِمۡ أَلۡفَ سَنَةٍ إِلَّا خَمۡسِينَ عَامٗا فَأَخَذَهُمُ ٱلطُّوفَانُ وَهُمۡ ظَٰلِمُونَ ١٤\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আমরা নূহকে তাঁর সম্প্রদায়ের কাছে পাঠিয়েছিলাম। তিনি তাদের মধ্যে এক হাজার বছর কম পঞ্চাশ বছর অবস্থান করেছিলেন। এরপর মহাপ্লাবন তাদেরকে গ্রাস করল, আর তারা ছিল জালিম।\n"
                            + "<b>[সূরা আনকাবুত: ২৯:১৪]</b>\n\n"
                            + "নূহ (আ.) দীর্ঘ ৯৫০ বছর তাঁর কওমকে তাওহীদের দাওয়াত দিয়ে কাটিয়েছেন। তাঁর জীবনের এই দীর্ঘ সময় তাঁর ধৈর্যশীলতার একটি চূড়ান্ত উদাহরণ।\n\n"
                            + "<b>নূহ (আ.)-এর ধৈর্যের শিক্ষা:</b>\n"
                            + "নূহ (আ.) তাঁর কওমের অত্যাচার ও বিদ্রূপের মুখেও অত্যন্ত ধৈর্যের সঙ্গে তাওহীদের দাওয়াত চালিয়ে গেছেন। তাঁর ধৈর্যের বিষয়ে কুরআন ও হাদিসে বিশেষভাবে আলোকপাত করা হয়েছে।\n\n"
                            + "<b>কওমের প্রতি ধৈর্যশীল দাওয়াত:</b>\n"
                            + "নূহ (আ.) দিন-রাত তাঁর কওমকে দাওয়াত দিতেন। তিনি প্রকাশ্যে ও গোপনে তাঁদের আহ্বান করতেন, তবুও তারা তাঁর কথা শুনতে চাইত না।\n\n"
                            + "قَالَ رَبِّ إِنِّي دَعَوۡتُ قَوۡمِي لَيۡلٗا وَنَهَارٗا ٥ فَلَمۡ يَزِدۡهُمۡ دُعَآءِيٓ إِلَّا فِرَارٗا ٦\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নূহ বললেন, ‘হে আমার প্রতিপালক! আমি আমার সম্প্রদায়কে দিন-রাত দাওয়াত দিয়েছি। কিন্তু আমার দাওয়াত তাদেরকে আরও বেশি দূরে সরিয়ে দিয়েছে।\n"
                            + "<b>[সূরা নূহ: ৭১:৫-৬]</b>\n\n"
                            + "<b>উপহাসের প্রতিক্রিয়ায় ধৈর্য:</b>\n"
                            + "নূহ (আ.) যখন আল্লাহর নির্দেশে নৌকা নির্মাণ করছিলেন, তখন তাঁর কওম তাঁকে উপহাস করত। কিন্তু তিনি ধৈর্যের সাথে তাঁদের কথাকে উপেক্ষা করতেন।\n\n"
                            + "وَكُلَّمَا مَرَّ عَلَيۡهِ مَلَأٞ مِّن قَوۡمِهِۦ سَخِرُواْ مِنۡهُۖ قَالَ إِن تَسۡخَرُواْ مِنَّا فَإِنَّا نَسۡخَرُ مِنكُمۡ كَمَا تَسۡخَرُونَ ٣٨\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাঁর সম্প্রদায়ের নেতারা যখনই তাঁর পাশ দিয়ে যেত, তখনই তাঁকে উপহাস করত। তিনি বলতেন, তোমরা যদি আমাদের নিয়ে উপহাস করো, তবে আমরা তোমাদের নিয়ে এমনই উপহাস করব যেমন তোমরা উপহাস করছ।\n"
                            + "<b>[সূরা হুদ: ১১:৩৮]</b>",
                    "Surah Al-Ankabut (29:14) explicitly confirms that Prophet Nuh (AS) dwelled among his people for nine hundred and fifty years (one thousand minus fifty years), enduring continuous mockery with profound steadfastness."
            ));

            // 5. সম্প্রদায়ের অবাধ্যতা ও উপহাস
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "সম্প্রদায়ের অবাধ্যতা ও উপহাস",
                    "Disobedience and Mockery of the Community",
                    "হযরত নূহ (আলাইহিস সালাম)-এর দাওয়াতের মুখোমুখি তাঁর সম্প্রদায়ের অবাধ্যতা এবং উপহাস একটি বেদনাদায়ক অধ্যায়। নূহ (আ.) তাঁর সম্প্রদায়কে দীর্ঘ ৯৫০ বছর ধরে তাওহীদ (আল্লাহর একত্ব) এবং শিরক থেকে ফিরে আসার দাওয়াত দেন। কিন্তু তাঁর কওমের অধিকাংশই তাঁকে প্রত্যাখ্যান করে, তাঁর কথা অমান্য করে এবং তাঁকে উপহাস করে।\n\n"
                            + "<b>সম্প্রদায়ের শিরক এবং পাপাচার:</b>\n"
                            + "নূহ (আলাইহিস সালাম)-এর কওম ছিল আল্লাহর পথে বিভ্রান্ত। তাঁরা বিভিন্ন দেবতার উপাসনা করত এবং শিরক করত। তাদের প্রধান দেবতাগুলোর নাম কুরআনে উল্লেখ করা হয়েছে:\n\n"
                            + "وَقَالُواْ لَا تَذَرُنَّ ءَالِهَتَكُمۡ وَلَا تَذَرُنَّ وَدّٗا وَلَا سُوَاعٗا وَلَا يَغُوثَ وَيَعُوقَ وَنَسۡرٗا ٢٣\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা বলল, তোমরা তোমাদের উপাস্যদের ত্যাগ করো না। এবং ওয়াদ, সুয়াআ, ইয়াগুছ, ইয়াঊক ও নাসরকে ত্যাগ করো না।\n"
                            + "<b>[সূরা নূহ: ৭১:২৩]</b>\n\n"
                            + "তারা এই মূর্তিগুলোর উপাসনা করত এবং আল্লাহর প্রতি আনুগত্য করতে অস্বীকৃতি জানাত।\n\n"
                            + "<b>দাওয়াত প্রত্যাখ্যান:</b>\n"
                            + "নূহ (আ.)-এর দাওয়াতের প্রতি তাদের প্রতিক্রিয়া ছিল স্পষ্ট অবজ্ঞা। তারা বারবার তাঁর কথা প্রত্যাখ্যান করত এবং ঈমান আনার পরিবর্তে তাঁকে অবিশ্বাস করত।\n\n"
                            + "قَالَ ٱلۡمَلَأُ ٱلَّذِينَ كَفَرُواْ مِن قَوۡمِهِۦ مَا نَرَىٰكَ إِلَّا بَشَرٗا مِّثۡلَنَا وَمَا نَرَىٰكَ ٱتَّبَعَكَ إِلَّا ٱلَّذِينَ هُمۡ أَرَاذِلُنَا بَادِيَ ٱلرَّأۡيِ وَمَا نَرَىٰ لَكُمۡ عَلَيۡنَا مِن فَضۡلِۢ بَلۡ نَظُنُّكُمۡ كَٰذِبِينَ ٢٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তখন তাঁর সম্প্রদায়ের নেতারা, যারা কাফের ছিল, বলল, ‘আমরা তোমাকে আমাদের মতো একজন মানুষ ছাড়া আর কিছুই মনে করি না। আমরা দেখি তোমার অনুসরণ করছে কেবল আমাদের নিম্নশ্রেণির লোকেরা এবং তাও হঠকারীভাবে। আমরা তোমাদের মধ্যে কোনো শ্রেষ্ঠত্ব দেখি না। বরং আমরা তোমাদের মিথ্যাবাদী মনে করি।\n"
                            + "<b>[সূরা হুদ: ১১:২৭]</b>\n\n"
                            + "<b>নূহ (আ.) দাওয়াতের জন্য উপহাস:</b>\n"
                            + "তাঁর কওম নূহ (আ.)-কে মিথ্যাবাদী বলে উপহাস করত এবং তাঁকে তুচ্ছজ্ঞান করত।\n\n"
                            + "كَذَّبَتۡ قَوۡمُ نُوحٍ ٱلۡمُرۡسَلِينَ ١٠٥\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নূহের সম্প্রদায় রাসূলদেরকে মিথ্যাবাদী বলেছে।\n"
                            + "<b>[সূরা শু'আরা: ২৬:১০৫]</b>\n\n"
                            + "<b>নৌকা নির্মাণের সময় উপহাস:</b>\n"
                            + "যখন নূহ (আ.) আল্লাহর নির্দেশে নৌকা নির্মাণ করছিলেন, তখন তাঁর কওম তাঁকে উপহাস করত এবং তাঁকে নিয়ে ঠাট্টা করত।\n\n"
                            + "وَكُلَّمَا مَرَّ عَلَيۡهِ مَلَأٞ مِّن قَوۡمِهِۦ سَخِرُواْ مِنۡهُۖ قَالَ إِن تَسۡخَرُواْ مِنَّا فَإِنَّا نَسۡخَرُ مِنكُمۡ كَمَا تَسۡخَرُونَ ٣٨\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাঁর সম্প্রদায়ের নেতারা যখনই তাঁর পাশ দিয়ে যেত, তখনই তাঁকে উপহাস করত। তিনি বলতেন, ‘তোমরা যদি আমাদের নিয়ে উপহাস করো, তবে আমরা তোমাদের নিয়ে এমনই উপহাস করব যেমন তোমরা উপহাস করছ।\n"
                            + "<b>[সূরা হুদ: ১১:৩৮]</b>",
                    "The arrogant nobility rejected the truth, claiming that only the poor and downtrodden followed Nuh (AS). When he began crafting the Ark in the dry land by divine decree, they ridiculed him constantly."
            ));

            // 6. নৌকা নির্মাণের নির্দেশ
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "নৌকা নির্মাণের নির্দেশ",
                    "Divine Command to Build the Ark",
                    "হযরত নূহ (আলাইহিস সালাম)-এর জীবনের অন্যতম গুরুত্বপূর্ণ অধ্যায় হলো আল্লাহর নির্দেশে নৌকা (আর্ক) নির্মাণ। এটি ছিল আল্লাহর পক্ষ থেকে নূহ (আ.)-এর প্রতি একটি বিশেষ নির্দেশ, যা তাঁর কওমের উপর আযাবের সূচনার প্রস্তুতি হিসেবে কাজ করেছিল। মহাপ্লাবন (তুফান) থেকে মুমিনদের রক্ষা করার জন্য নূহ (আ.)-কে নৌকা নির্মাণ করতে বলা হয়। পবিত্র কুরআন এবং তাফসীর গ্রন্থে এই ঘটনা বিশদভাবে বর্ণিত হয়েছে।\n\n"
                            + "<b>আল্লাহর নৌকা নির্মাণের নির্দেশ:</b>\n"
                            + "নূহ (আলাইহিস সালাম)-কে তাঁর কওমের নিরবচ্ছিন্ন অবাধ্যতা এবং অবিশ্বাসের পর নৌকা নির্মাণের আদেশ দেওয়া হয়।\n\n"
                            + "وَأُوحِيَ إِلَىٰ نُوحٍ أَنَّهُۥ لَن يُؤۡمِنَ مِن قَوۡمِكَ إِلَّا مَن قَدۡ ءَامَنَ فَلَا تَبۡتَئِسۡ بِمَا كَانُواْ يَفۡعَلُونَ ٣٦ وَٱصۡنَعِ ٱلۡفُلۡكَ بِأَعۡيُنِنَا وَوَحۡيِنَا وَلَا تُخَٰطِبۡনِي فِي ٱلَّذِينَ ظَلَمُوٓاْ إِنَّهُم مُّغۡرَقُونَ ٣٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নূহের কাছে ওহি পাঠানো হলো যে, ‘তোমার সম্প্রদায়ের মধ্যে যারা ইতিমধ্যে ঈমান এনেছে, তাদের ছাড়া আর কেউ ঈমান আনবে না। সুতরাং তাদের কাজকর্মের জন্য তুমি চিন্তিত হয়ো না। এবং তুমি আমাদের তত্ত্বাবধানে এবং আমাদের নির্দেশ অনুযায়ী নৌকা তৈরি করো। আর যারা জুলুম করেছে, তাদের ব্যাপারে আমার সাথে কথা বলো না। নিশ্চয়ই তারা ডুবে যাবে।\n"
                            + "<b>[সূরা হুদ: ১১:৩৬-৩৭]</b>\n\n"
                            + "<b>নৌকা নির্মাণের উদ্দেশ্য:</b>\n"
                            + "নৌকা নির্মাণের মূল উদ্দেশ্য ছিল আল্লাহর পক্ষ থেকে নূহ (আ.) এবং তাঁর অনুসারী মুমিনদের রক্ষা করা। মহাপ্লাবন ছিল কাফিরদের জন্য আযাব, আর নৌকা ছিল ঈমানদারদের জন্য মুক্তির উপায়।\n\n"
                            + "<b>আল্লাহর তত্ত্বাবধানে নির্মাণ:</b>\n"
                            + "নূহ (আ.)-কে নৌকা তৈরি করার সময় আল্লাহ নিজে তত্ত্বাবধান করেছেন। এর মানে হলো, নৌকা নির্মাণের প্রতিটি ধাপ আল্লাহর নির্দেশনা অনুযায়ী সম্পন্ন হয়েছিল।\n\n"
                            + "وَٱصۡنَعِ ٱلۡفُلۡكَ بِأَعۡيُنِنَا وَوَحۡيِنَا ٣٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তুমি আমাদের তত্ত্বাবধানে এবং আমাদের ওহির মাধ্যমে নৌকা তৈরি করো।\n"
                            + "<b>[সূরা হুদ: ১১:৩৭]</b>\n\n"
                            + "<b>কাঠ ও লোহা ব্যবহার:</b>\n"
                            + "কুরআনের ইঙ্গিত থেকে বোঝা যায় যে নূহ (আ.) কাঠ এবং লোহার সাহায্যে নৌকা নির্মাণ করেছিলেন।\n\n"
                            + "وَحَمَلۡنَٰهُ عَلَىٰ ذَاتِ أَلۡوَاحٖ وَدُسُرٍ ١٣\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আমরা তাঁকে বহন করেছিলাম তক্তা ও পেরেকের তৈরি জাহাজে।\n"
                            + "<b>[সূরা ক্বামার: ৫৪:১৩]</b>\n\n"
                            + "<b>নৌকার আকার ও ক্ষমতা:</b>\n"
                            + "নৌকা সম্পর্কে কুরআনে সুনির্দিষ্ট আকার উল্লেখ নেই। তবে তাফসীর এবং ইসলামী ঐতিহ্যের বর্ণনায় বলা হয়েছে যে এটি ছিল বিশাল আকৃতির এবং এতে নূহ (আ.)-এর অনুসারী মুমিনরা এবং প্রতিটি প্রাণীর এক জোড়া স্থান পেয়েছিল। এটি মহাপ্লাবনের সময় পানির উপরে ভেসে থাকার জন্য উপযুক্ত ছিল।\n\n"
                            + "<b>কওমের উপহাস:</b>\n"
                            + "নৌকা নির্মাণের সময় নূহ (আ.)-এর কওম তাঁকে উপহাস করত। তারা বলত, ‘তুমি কি মরুভূমিতে নৌকা তৈরি করছ? এটা কি কোনো কাজ হলো?’ তাঁরা বুঝতে পারেনি যে আল্লাহর পক্ষ থেকে মহাপ্লাবন আসতে চলেছে।\n\n"
                            + "وَكُلَّمَا مَرَّ عَلَيۡهِ مَلَأٞ مِّن قَوۡمِهِۦ سَخِرُواْ مِنۡهُۖ قَالَ إِن تَسۡخَرُواْ مِنَّا فَإِنَّا نَسۡخَرُ مِنكُمۡ كَمَا تَسۡخَرُونَ ٣٨\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাঁর সম্প্রদায়ের নেতারা যখনই তাঁর পাশ দিয়ে যেত, তখনই তাঁকে উপহাস করত। তিনি বলতেন, তোমরা যদি আমাদের নিয়ে উপহাস করো, তবে আমরা তোমাদের নিয়ে এমনই উপহাস করব যেমন তোমরা উপহাস করছ।\n"
                            + "<b>[সূরা হুদ: ১১:৩৮]</b>\n\n"
                            + "<b>মহাপ্লাবন, নৌকার ভূমিকা:</b>\n"
                            + "নৌকা নির্মাণ শেষ হওয়ার পর আল্লাহর নির্দেশে মহাপ্লাবন শুরু হয়। নূহ (আ.)-কে বলা হয়েছিল তাঁর পরিবার, অনুসারী মুমিনগণ এবং প্রতিটি প্রাণীর এক জোড়া নিয়ে নৌকায় উঠতে।\n\n"
                            + "حَتَّىٰٓ إِذَا جَآءَ أَمۡرُنَا وَفَارَ ٱلتَّنُّورُ قُلۡنَا ٱحۡمِلۡ فِيهَا مِن كُلّٖ زَوۡجَيۡنِ ٱثۡنَيۡنِ وَأَهۡلَكَ إِلَّا مَن سَبَقَ عَلَيۡهِ ٱلۡقَوۡلُ وَمَنۡ ءَامَنَۚ وَمَآ ءَامَنَ مَعَهُۥٓ إِلَّا قَلِيلٞ ٤٠\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন আমাদের নির্দেশ এল এবং পানি উপরের দিকে উঠে আসতে শুরু করল, তখন আমরা বললাম, ‘তুমি এতে প্রতিটি প্রাণীর এক জোড়া এবং তোমার পরিবারকে, তাদের মধ্যে যাদের বিরুদ্ধে পূর্বে কথা হয়ে গেছে তাদের ছাড়া, এবং যারা ঈমান এনেছে তাদেরকে নৌকায় তুলো।’ কিন্তু তাঁর সঙ্গে ঈমান এনেছিল খুব অল্পসংখ্যক মানুষ।\n"
                            + "<b>[সূরা হুদ: ১১:৪০]</b>\n\n"
                            + "<b>নৌকার সুরক্ষা ও যাত্রা:</b>\n"
                            + "মহাপ্লাবন শুরু হলে নৌকাটি আল্লাহর নিরাপত্তায় পানিতে ভেসে চলতে থাকে।\n\n"
                            + "وَقَالَ ٱرۡكَبُواْ فِيهَا بِسۡمِ ٱللَّهِ مَجۡرَىٰهَا وَمُرۡسَىٰهَآۚ إِنَّ رَبِّي لَغَفُورٞ رَّحِيمٞ ٤١\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর নূহ বললেন, তোমরা এতে আরোহণ করো। আল্লাহর নামে এর চলা এবং থামা। নিশ্চয়ই আমার প্রতিপালক অতি ক্ষমাশীল, পরম দয়ালু।\n"
                            + "<b>[সূরা হুদ: ১১:৪১]</b>",
                    "Under divine inspiration and supervision, Prophet Nuh (AS) constructed a massive vessel from wooden planks and palm-fiber ropes/nails (54:13) to shelter the believers and a pair of every animal species."
            ));

            // 7. মহাপ্লাবন ও ধ্বংস
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "মহাপ্লাবন ও ধ্বংস",
                    "The Great Deluge and Destruction",
                    "হযরত নূহ (আলাইহিস সালাম)-এর কওমের জন্য মহাপ্লাবন (তুফান) ছিল একটি কঠিন শাস্তি। এটি আল্লাহর পক্ষ থেকে অবাধ্য কাফিরদের প্রতি আযাব এবং ঈমানদারদের প্রতি রহমতের প্রতীক। এই ঘটনা কুরআন, হাদিস এবং বিভিন্ন তাফসীর গ্রন্থে বিশদভাবে বর্ণিত হয়েছে। এটি আমাদের শিখায় যে যারা আল্লাহর আদেশ অমান্য করে, তাদের পরিণতি অত্যন্ত কঠিন।\n\n"
                            + "<b>মহাপ্লাবনের পটভূমি:</b>\n"
                            + "নূহ (আলাইহিস সালাম)-এর কওম তাঁকে এবং তাঁর দাওয়াতকে বারবার প্রত্যাখ্যান করে। তাঁরা শিরক এবং পাপাচারে লিপ্ত থাকে। দীর্ঘ ৯৫০ বছর দাওয়াত দেওয়ার পরেও, অধিকাংশ লোক তাওহীদের পথে ফিরে আসেনি।\n\n"
                            + "وَمَآ ءَامَنَ مَعَهُۥٓ إِلَّا قَلِيلٞ ٤٠\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর তাঁর সাথে অল্পসংখ্যক লোকই ঈমান এনেছিল।\n"
                            + "<b>[সূরা হুদ: ১১:৪০]</b>\n\n"
                            + "<b>দোয়া এবং আল্লাহর আদেশ:</b>\n"
                            + "নূহ (আ.)-এর ধৈর্য ও দীর্ঘ প্রচেষ্টার পরেও যখন তাঁর কওম সংশোধন হয়নি, তখন তিনি আল্লাহর কাছে তাদের ধ্বংসের জন্য দোয়া করেন।\n\n"
                            + "وَقَالَ نُوحٞ رَّبِّ لَا تَذَرۡ عَلَى ٱلۡأَرۡضِ مِنَ ٱلۡكَٰفِرِينَ دَيَّارًا ٢٦ إِنَّكَ إِن تَذَرۡهُمۡ يُضِلُّواْ عِبَادَكَ وَلَا يَلِدُوٓاْ إِلَّا فَاجِرٗا كَفَّارًا ٢٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নূহ বললেন, হে আমার প্রভু! তুমি পৃথিবীতে কোনো কাফেরকেই অবশিষ্ট রেখো না। যদি তুমি তাদের অবশিষ্ট রাখো, তাহলে তারা তোমার বান্দাদেরকে বিপথগামী করবে এবং তারা শুধুমাত্র দুষ্ট ও কাফের সন্তান জন্ম দেবে।\n"
                            + "<b>[সূরা নূহ: ৭১:২৬-২৭]</b>\n\n"
                            + "<b>মহাপ্লাবনের ঘটনা, নৌকা নির্মাণ:</b>\n"
                            + "আল্লাহ নূহ (আ.)-কে নির্দেশ দেন নৌকা নির্মাণ করতে। এটি মুমিনদের জন্য নিরাপদ আশ্রয় ছিল।\n\n"
                            + "وَٱصۡنَعِ ٱلۡفُلۡكَ بِأَعۡيُنِنَا وَوَحۡيِنَا ٣٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তুমি আমাদের তত্ত্বাবধানে এবং আমাদের ওহির মাধ্যমে নৌকা তৈরি করো।\n"
                            + "<b>[সূরা হুদ: ১১:৩৭]</b>\n\n"
                            + "<b>প্লাবনের সূচনা:</b>\n"
                            + "মহাপ্লাবন শুরু হওয়ার আগে আল্লাহ নূহ (আ.)-কে একটি চিহ্ন দিয়েছিলেন: \"পানির ফোয়ারা ফুঁটে উঠবে।\" তখন নূহ (আ.)-কে তাঁর অনুসারীদের এবং প্রতিটি প্রাণীর এক জোড়া নৌকায় তুলতে বলা হয়।\n\n"
                            + "حَتَّىٰٓ إِذَا جَآءَ أَمۡرُنَا وَفَارَ ٱلتَّنُّورُ قُلۡنَا ٱحۡمِلۡ فِيهَا مِن كُلّٖ زَوۡجَيۡنِ ٱثۡنَيۡنِ وَأَهۡلَكَ إِلَّا مَن سَبَقَ عَلَيۡهِ ٱلۡقَوۡلُ وَمَنۡ ءَامَنَۚ وَمَآ ءَامَنَ مَعَهُۥٓ إِلَّا قَلِيلٞ ٤٠\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন আমাদের নির্দেশ এল এবং পানি উপরের দিকে উঠে আসতে শুরু করল, তখন আমরা বললাম, ‘তুমি এতে প্রতিটি প্রাণীর এক জোড়া এবং তোমার পরিবারকে, তাদের মধ্যে যাদের বিরুদ্ধে পূর্বে কথা হয়ে গেছে তাদের ছাড়া, এবং যারা ঈমান এনেছে তাদেরকে নৌকায় তুলো।’ কিন্তু তাঁর সঙ্গে ঈমান এনেছিল খুব অল্পসংখ্যক মানুষ।\n"
                            + "<b>[সূরা হুদ: ১১:৪০]</b>\n\n"
                            + "<b>প্লাবনের তীব্রতা:</b>\n"
                            + "মহাপ্লাবনের সময় পুরো পৃথিবী পানি দ্বারা প্লাবিত হয়। আকাশ থেকে প্রচুর বৃষ্টি নেমে আসে এবং মাটি থেকে পানি উঠে আসে।\n\n"
                            + "فَفَتَحۡنَآ أَبۡوَٰبَ ٱلسَّمَآءِ بِمَآءٖ مُّنۡهَمِرٖ ١١ وَفَجَّرۡنَا ٱلۡأَرۡضَ عُيُونٗا فَٱلۡتَقَى ٱلۡمَآءُ عَلَىٰٓ أَمۡرٖ قَدۡ قُدِرَ ١٢\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর আমরা আকাশের দরজা উন্মুক্ত করে দিলাম প্রবল বর্ষণের জন্য। এবং আমরা জমিন থেকে ঝর্ণাসমূহ প্রবাহিত করলাম। এরপর উভয় পানি একত্রিত হলো একটি নির্ধারিত আদেশ বাস্তবায়নের জন্য।\n"
                            + "<b>[সূরা ক্বামার: ৫৪:১১-১২]</b>\n\n"
                            + "<b>কাফিরদের ধ্বংস, অবাধ্য পুত্রের ধ্বংস:</b>\n"
                            + "নূহ (আ.)-এর নিজের পুত্রও তাঁর দাওয়াত প্রত্যাখ্যান করে। মহাপ্লাবনের সময় সে নৌকায় উঠতে অস্বীকৃতি জানায় এবং নিজেকে রক্ষা করতে পাহাড়ে ওঠার চেষ্টা করে। কিন্তু আল্লাহর আযাব থেকে কেউ রক্ষা পায়নি।\n\n"
                            + "قَالَ سَأٓوِيٓ إِلَىٰ جَبَلٖ يَعۡصِمُنِي مِنَ ٱلۡمَآءِۚ قَالَ لَا عَاصِمَ ٱلۡيَوۡمَ مِنۡ أَمۡرِ ٱللَّهِ إِلَّا مَن رَّحِمَۚ وَحَالَ بَيۡنَهُمَا ٱلۡمَوۡجُ فَكَانَ مِنَ ٱلۡمُغۡরَقِينَ ٤٣\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "সে বলল, ‘আমি একটি পাহাড়ে আশ্রয় নেব যা আমাকে পানির হাত থেকে রক্ষা করবে।’ নূহ বললেন, ‘আজ আল্লাহর আদেশ থেকে কেউ রক্ষা করতে পারবে না, কেবল তিনি যাকে দয়া করবেন।’ এরপর তাদের মধ্যে একটি ঢেউ এসে পড়ল, এবং সে ডুবে গেল।\n"
                            + "<b>[সূরা হুদ: ১১:৪৩]</b>\n\n"
                            + "<b>কাফিরদের সম্পূর্ণ ধ্বংস:</b>\n"
                            + "যারা নূহ (আ.)-এর দাওয়াত প্রত্যাখ্যান করেছিল, তারা সবাই প্লাবনের পানিতে ডুবে যায়। শুধুমাত্র নূহ (আ.) এবং তাঁর অনুসারীরা নৌকার মাধ্যমে রক্ষা পায়।\n\n"
                            + "فَأَنجَيۡنَٰهُ وَأَصۡحَٰبَ ٱلسَّفِينَةِ وَجَعَلۡنَٰهَآ ءَايَةٗ لِّلۡعَٰلَمِينَ ١٥\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর আমরা তাঁকে এবং নৌকার আরোহীদেরকে রক্ষা করলাম এবং একে বিশ্ববাসীর জন্য এক নিদর্শন বানিয়েছিলাম।\n"
                            + "<b>[সূরা আনকাবুত: ২৯:১৫]</b>\n\n"
                            + "মহাপ্লাবন এবং নূহ (আ.)-এর কওমের ধ্বংস ইসলামের শিক্ষার একটি গুরুত্বপূর্ণ অংশ। এটি আমাদের জন্য সতর্কবার্তা এবং ঈমানের পথে দৃঢ় থাকার অনুপ্রেরণা।",
                    "Torrential rain poured from the heavens and boiling springs gushed from the earth (54:11-12). The disbelievers—including Nuh's rebellious son Kan'an who fled to the mountaintop—were entirely engulfed by the waves."
            ));

            // 8. প্লাবনের পর নতুন সভ্যতা
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "প্লাবনের পর নতুন সভ্যতা",
                    "New Civilization after the Great Flood",
                    "মহাপ্লাবন বা তুফানে আল্লাহ তাআলা হযরত নূহ (আলাইহিস সালাম)-এর সম্প্রদায়ের কাফিরদের সম্পূর্ণ ধ্বংস করেন। এরপর নূহ (আ.) এবং তাঁর অনুসারী মুমিনদের মাধ্যমে পৃথিবীতে নতুনভাবে সভ্যতা শুরু হয়। এটি মানবজাতির পুনর্জন্ম এবং একটি নতুন তাওহীদ-ভিত্তিক সমাজ গঠনের সূচনা। কুরআন ও হাদিসের আলোকে মহাপ্লাবনের পর নতুন সভ্যতার গুরুত্বপূর্ণ বিষয়গুলো আলোচনা করা হলো।\n\n"
                            + "<b>মহাপ্লাবনের সমাপ্তি ও নৌকার স্থির হওয়া:</b>\n"
                            + "মহাপ্লাবনের পরে, আল্লাহর আদেশে নূহ (আ.)-এর নৌকা \"জুদি\" পর্বতে স্থির হয়। এটি ছিল আল্লাহর পক্ষ থেকে মুমিনদের জন্য নতুন জীবনের শুরু।\n\n"
                            + "وَقِيلَ يَـٰٓأَرۡضُ ٱبۡلَعِي مَآءَكِ وَيَـٰسَمَآءُ أَقۡلِعِي وَغِيضَ ٱلۡمَآءُ وَقُضِيَ ٱلۡأَمۡرُ وَٱسۡتَوَتۡ عَلَى ٱلۡجُودِيِّۖ وَقِيلَ بُعۡدٗا لِّلۡقَوۡمِ ٱلظَّـٰلِمِينَ ٤٤\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর বলা হলো, ‘হে পৃথিবী! তোমার পানি গিলে ফেল, আর হে আকাশ! থেমে যাও।’ পানি সরে গেল, কাজ শেষ হলো এবং নৌকাটি জুদি পর্বতে স্থির হলো। আর বলা হলো, ‘অভিশপ্ত হোক জালিম জাতি।\n"
                            + "<b>[সূরা হুদ: ১১:৪৪]</b>\n\n"
                            + "<b>নূহ (আ.)-এর নতুন দায়িত্ব:</b>\n"
                            + "মহাপ্লাবনের পরে নূহ (আলাইহিস সালাম)-এর দায়িত্ব ছিল নতুন সমাজ প্রতিষ্ঠা করা এবং তাঁর অনুসারীদের আল্লাহর বিধান অনুযায়ী জীবন পরিচালনার শিক্ষা দেওয়া। এটি ছিল তাওহীদ (আল্লাহর একত্ববাদ)-ভিত্তিক একটি নতুন সভ্যতার ভিত্তি।\n\n"
                            + "يَـٰنُوحُ ٱهۡبِطۡ بِسَلَٰمٖ مِّنَّا وَبَرَكَٰتٍ عَلَيۡكَ وَعَلَىٰٓ أُمَمٖ مِّمَّن مَّعَكَۖ وَأُمَمٞ سَنُمَتِّعُهُمۡ ثُمَّ يَمَسُّهُم مِّنَّا عَذَابٌ أَلِيمٞ ٤٨\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে নূহ! তুমি শান্তি ও আমাদের পক্ষ থেকে বরকত নিয়ে নেমে এসো, যা তোমার প্রতি এবং তোমার সঙ্গী সম্প্রদায়ের প্রতি থাকবে। আর কিছু সম্প্রদায়কে আমরা সাময়িকভাবে ভোগ করার সুযোগ দেব, তারপর তাদের উপর আমাদের কাছ থেকে কঠিন শাস্তি আপতিত হবে।\n"
                            + "<b>[সূরা হুদ: ১১:৪৮]</b>\n\n"
                            + "<b>নতুন প্রজন্মের সূচনা:</b>\n"
                            + "মহাপ্লাবনের পরে, আল্লাহ তাআলা নূহ (আলাইহিস সালাম)-এর বংশধরদের মাধ্যমে মানবজাতির নতুন প্রজন্মের সূচনা করেন।\n\n"
                            + "وَجَعَلۡنَا ذُرِّيَّتَهُۥ هُمُ ٱلۡبَاقِينَ ٧٧\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এবং আমরা তাঁর বংশধরদেরকে অবশিষ্ট রেখেছি।\n"
                            + "<b>[সূরা সাফফাত: ৩৭:৭৭]</b>\n\n"
                            + "<b>নতুন প্রজন্মের বৈশিষ্ট্য:</b>\n"
                            + "• তাওহীদের ভিত্তিতে জীবন গঠন।\n"
                            + "• আল্লাহর প্রতি আনুগত্য এবং শিরক থেকে মুক্ত থাকা।\n"
                            + "• আল্লাহর রহমত এবং বরকতের উপর নির্ভরশীলতা।\n\n"
                            + "<b>নতুন সভ্যতার বৈশিষ্ট্য:</b>\n"
                            + "• <b>তাওহীদ-ভিত্তিক সমাজ:</b> মহাপ্লাবনের পর নূহ (আ.) এবং তাঁর অনুসারীরা তাওহীদের শিক্ষা অনুযায়ী সমাজ গঠন করেন।\n"
                            + "• <b>আল্লাহর বিধান অনুসরণ:</b> নতুন সভ্যতার প্রধান বৈশিষ্ট্য ছিল আল্লাহর আদেশ এবং নবীর নির্দেশ মেনে জীবন পরিচালনা করা।\n"
                            + "• <b>ন্যায়বিচার ও সংহতি:</b> মুমিনদের সমাজে পারস্পরিক সহযোগিতা, ন্যায়বিচার এবং দয়া ছিল প্রধান গুণাবলী।\n"
                            + "• <b>কৃষি ও জীবিকা নির্বাহের শিক্ষা:</b> মহাপ্লাবনের পরে তাঁরা কৃষিকাজ, পশুপালন এবং বিভিন্ন পেশায় জীবনযাপনের পদ্ধতি শিখেছিলেন।\n\n"
                            + "হযরত নূহ (আলাইহিস সালাম)-এর মহাপ্লাবনের পর নতুন সভ্যতা ছিল তাওহীদ প্রতিষ্ঠার নতুন সূচনা। এটি মানবজাতির জন্য একটি শিক্ষা, যেখানে আল্লাহর আদেশের প্রতি আনুগত্য এবং শিরক থেকে মুক্ত থাকার গুরুত্ব প্রকাশ পায়। আল্লাহর রহমত এবং নবীদের দিকনির্দেশনা মেনে চলাই সভ্যতা গঠনের মূল ভিত্তি।",
                    "The Ark came to rest upon Mount Judi (11:44). Allah showered His peace and blessings upon Nuh and the surviving believers, rebuilding a righteous human civilization solely grounded upon monotheism, justice, and agriculture."
            ));

            // 9. শেষ জীবন
            list.add(new ProphetOverviewTopicItem(
                    9,
                    "শেষ জীবন",
                    "Final Days and Demise",
                    "হযরত নূহ (আলাইহিস সালাম) ছিলেন মানবজাতির প্রথম রাসূল এবং ইসলামের ইতিহাসে একজন অন্যতম ধৈর্যশীল ও তাওহীদ প্রচারক। তাঁর জীবন ছিল দীর্ঘ, মহাপ্লাবনের মধ্য দিয়ে দাওয়াতের কঠিন সংগ্রাম এবং নতুন সভ্যতা গঠনের সঙ্গে জড়িত। তাঁর মৃত্যু এবং জীবনের শেষ মুহূর্তগুলো আমাদের জন্য গভীর শিক্ষা ও প্রেরণার উৎস।\n\n"
                            + "<b>হযরত নূহ (আ.)-এর মৃত্যু:</b>\n"
                            + "কুরআন এবং হাদিসের ইঙ্গিত অনুসারে, হযরত নূহ (আ.) দীর্ঘ জীবন লাভ করেছিলেন। তিনি ৯৫০ বছর ধরে তাঁর কওমকে দাওয়াত দেন। তাফসীরের বর্ণনা অনুসারে, তাঁর জীবনের মোট সময়কাল এক হাজার বা তারও বেশি বছর হতে পারে।\n\n"
                            + "وَلَقَدۡ أَرۡسَلۡনَا نُوحًا إِلَىٰ قَوۡمِهِۦ فَلَبِثَ فِيهِمۡ أَلۡفَ سَنَةٍ إِلَّا خَمۡسِينَ عَامٗا فَأَخَذَهُمُ ٱلطُّوفَانُ وَهُمۡ ظَٰلِمُونَ ١٤\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আমরা নূহকে তাঁর সম্প্রদায়ের কাছে পাঠিয়েছিলাম। তিনি তাদের মধ্যে এক হাজার বছর কম পঞ্চাশ বছর অবস্থান করেছিলেন। এরপর মহাপ্লাবন তাদেরকে গ্রাস করল, আর তারা ছিল জালিম।\n"
                            + "<b>[সূরা আনকাবুত: ২৯:১৪]</b>\n\n"
                            + "তাফসীর গ্রন্থ অনুযায়ী, হযরত নূহ (আ.)-এর মৃত্যু হয় তাঁর অনুসারীদের একটি নতুন সভ্যতার ভিত্তি স্থাপনের পর। আল্লাহ তাঁকে জান্নাতের সুসংবাদ প্রদান করেন। মৃত্যুর আগে তিনি তাঁর সন্তান এবং অনুসারীদের তাওহীদ এবং আল্লাহর আদেশ মেনে চলার শেষ শিক্ষা দিয়ে যান।\n\n"
                            + "<b>তাওহীদ প্রতিষ্ঠার গুরুত্ব:</b>\n"
                            + "মৃত্যুর আগে নূহ (আ.) তাঁর সন্তানদের এবং অনুসারীদের তাওহীদের শিক্ষা দেন। তিনি বলেন:\n"
                            + "• একমাত্র আল্লাহর ইবাদত করতে হবে।\n"
                            + "• শিরক থেকে সম্পূর্ণ বিরত থাকতে হবে।\n"
                            + "• আল্লাহর প্রতি পূর্ণ আনুগত্য রাখতে হবে।",
                    "Before his peaceful passing after centuries of steadfast servitude, Prophet Nuh (AS) gathered his children and entrusted them with the supreme legacy of pure Tawheed (Monotheism), warning them against idolatry and pride."
            ));
        } else if (prophetId == 4) {
            // ==========================================
            // 4. হযরত হূদ (আ:) - Prophet Hud (AS) (8 Topics - 100% Verbatim)
            // ==========================================

            // 1. হূদ (আঃ) এর পরিচয় এবং বংশধারা
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "হূদ (আঃ) এর পরিচয় এবং বংশধারা",
                    "Identity and Lineage of Prophet Hud (AS)",
                    "হুদ আলাইহিস সালাম ছিলেন আদ জাতির একজন নবী, যাদেরকে আল্লাহ তায়ালা পথনির্দেশ করার জন্য পাঠিয়েছিলেন। তিনি আদ জাতির মধ্যে একজন সম্মানিত ব্যক্তি ছিলেন এবং তার বংশধারা নূহ আলাইহিস সালাম-এর সাথে যুক্ত।\n\n"
                            + "<b>তার পূর্ণ বংশধারা নিম্নরূপ:</b>\n\n"
                            + "হুদ ইবনে শালিখ ইবনে আরফাখশাধ ইবনে সাম ইবনে নূহ। হুদ আলাইহিস সালাম-এর জন্ম সম্পর্কে কুরআনে সরাসরি কোনো তথ্য নেই, তবে ঐতিহাসিক সূত্র মতে, তিনি ইয়েমেনের আহকাফ অঞ্চলে জন্মগ্রহণ করেন, যেখানে আদ জাতি বসবাস করত।\n\n"
                            + "<b>বংশগত অবস্থান:</b>\n"
                            + "তিনি ছিলেন নূহ আলাইহিস সালাম-এর পুত্র সাম-এর বংশধর, যা তাকে নূহ আলাইহিস সালাম-এর পরবর্তী প্রজন্মের একজন গুরুত্বপূর্ণ প্রতিনিধি হিসাবে চিহ্নিত করে।\n\n"
                            + "<b>আহকাফ অঞ্চল:</b>\n"
                            + "আদ জাতি এবং হুদ আলাইহিস সালাম-এর মূল আবাসস্থল ছিল আজকের ইয়েমেনের একটি মরু এলাকা, যা কুরআনে \"আহকাফ\" নামে পরিচিত। এই বংশধারা ও জীবন পরিক্রমা হুদ আলাইহিস সালাম-এর নবুওয়াত এবং তার দাওয়াতের পটভূমি সম্পর্কে আরও ভালোভাবে বোঝার জন্য গুরুত্বপূর্ণ।\n\n"
                            + "<b>কুরআনে হুদ আলাইহিস সালাম-এর উল্লেখ:</b>\n\n"
                            + "وَاِلٰى عَادٍ اَخَاهُمۡ هُوۡدًا ؕ قَالَ يٰقَوۡمِ اعۡبُدُوا اللّٰهَ مَا لَكُمۡ مِّنۡ اِلٰهٍ غَيۡرُهٗ ؕ اِنۡ اَنۡتُمۡ اِلَّا مُفۡتَرُوۡنَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আদ জাতির নিকট তাদের ভাই হুদকে পাঠিয়েছি। তিনি বললেন, ‘হে আমার সম্প্রদায়! তোমরা আল্লাহর ইবাদত কর। তিনি ছাড়া তোমাদের অন্য কোনো উপাস্য নেই। তোমরা তো কেবল মিথ্যা উদ্ভাবন কর।\n"
                            + "<b>[সূরা হুদ ১১:৫০-৫২]</b>\n\n"
                            + "كَذَّبَتۡ عَادٌ الۡمُرۡسَلِيۡنَ\n\n"
                            + "اِذۡ قَالَ لَهُمۡ اَخُوۡهُمۡ هُوۡدٌ اَلَا تَتَّقُوۡنَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আদ জাতি রসূলদের মিথ্যাজ্ঞান করেছিল, যখন তাদের ভাই হুদ তাদের বলেছিলেন, ‘তোমরা কি তাকওয়া অবলম্বন করবে না?\n"
                            + "<b>[সূরা আশ-শু'আরা ২৬:১২৩-১২৫]</b>\n\n"
                            + "<b>হাদিসে হুদ আলাইহিস সালাম-এর উল্লেখ:</b>\n"
                            + "হুদ আলাইহিস সালাম-এর জীবন সম্পর্কে সুনির্দিষ্ট হাদিস পাওয়া না গেলেও নবীদের মধ্যে তার স্থান এবং বংশধারা সম্পর্কে উল্লেখ রয়েছে। ইবনে কাসির-এর বর্ণনা (আল বিদায়া ওয়ান নিহায়া): ইবনে কাসির উল্লেখ করেছেন যে হুদ আলাইহিস সালাম নূহ আলাইহিস সালাম-এর পুত্র সাম-এর বংশধর ছিলেন।\n\n"
                            + "هُودٌ بْنُ شَالِحِ بْنِ أَرْفَخْشَذَ بْنِ سَامِ بْنِ نُوحٍ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হুদ ছিলেন শালিখ-এর পুত্র, আরফাখশাদ-এর পুত্র, সাম-এর পুত্র, যিনি নূহ আলাইহিস সালাম-এর পুত্র।",
                    "Prophet Hud (peace be upon him) was sent by Allah to guide the mighty people of 'Ad. He was an honorable noble among them, descending directly from Prophet Nuh (peace be upon him).\n\n"
                            + "<b>Full Lineage:</b>\n"
                            + "Hud ibn Shalikh ibn Arfakhshadh ibn Sam ibn Nuh. Historical records locate his origin in the Ahqaf region of southern Arabia (modern Yemen), where the tribe of 'Ad thrived.\n\n"
                            + "<b>Genealogical Significance:</b>\n"
                            + "As a descendant of Sam (Shem), son of Nuh (AS), Hud was an essential prophetic leader for the post-Deluge generations.\n\n"
                            + "<b>The Region of Al-Ahqaf:</b>\n"
                            + "The dwelling place of 'Ad and Hud (AS) was the undulating sand dunes known in the Quran as \"Al-Ahqaf\".\n\n"
                            + "<b>Mention in the Quran:</b>\n"
                            + "\"And to the 'Ad [We sent] their brother Hud. He said, 'O my people, worship Allah; you have no deity other than Him. You are not but inventors [of falsehood].'\" (Surah Hud 11:50)\n\n"
                            + "\"The 'Ad denied the messengers when their brother Hud said to them, 'Will you not fear Allah?'\" (Surah Ash-Shu'ara 26:123-124)\n\n"
                            + "<b>Lineage in Classical History (Ibn Kathir - Al-Bidayah wan-Nihayah):</b>\n"
                            + "\"Hud ibn Shalikh ibn Arfakhshadh ibn Sam ibn Nuh.\""
            ));

            // 2. আদ জাতির কুফর ও অবাধ্যতা
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "আদ জাতির কুফর ও অবাধ্যতা",
                    "Disbelief and Transgression of the People of Ad",
                    "আদ জাতি আল্লাহর একটি বিশেষ সৃষ্টিশক্তি লাভ করেছিল। তারা দেহে অত্যন্ত শক্তিশালী, উচ্চতায় লম্বা, এবং সমৃদ্ধশালী ছিল। তবে তারা এসব আল্লাহর নেয়ামত ভুলে গিয়ে অহংকারে নিমজ্জিত হয়েছিল। তারা আল্লাহর প্রতি অবিশ্বাস করেছিল এবং মূর্তিপূজার পথ বেছে নিয়েছিল।\n\n"
                            + "<b>কুফর ও শিরকের প্রবণতা:</b>\n"
                            + "আদ জাতি তাওহীদের পথ থেকে বিচ্যুত হয়ে মূর্তিপূজা শুরু করেছিল। তারা আল্লাহকে ভুলে তাদের হাতে তৈরি মূর্তিগুলোর উপাসনা করত। হুদ (আলাইহিস সালাম) তাদেরকে এ শিরক থেকে ফিরিয়ে আনার চেষ্টা করেন। কিন্তু তারা তা প্রত্যাখ্যান করে এবং বলে:\n\n"
                            + "قَالُوا۟ أَجِئْتَنَا لِنَعْبُدَ ٱللَّهَ وَحْدَهُۥ وَنَذَرَ مَا كَانَ يَعْبُدُ ءَابَاوُنَا فَأْتِنَا بِمَا تَعِدُنَآ إِن كُنتَ مِنَ ٱلصَّـٰদِقِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা বলল, ‘তুমি কি আমাদের কাছে এসেছ, যাতে আমরা একমাত্র আল্লাহর ইবাদত করি এবং আমাদের পূর্বপুরুষেরা যাদের ইবাদত করত, তাদের ছেড়ে দিই? তুমি যদি সত্যবাদী হও, তবে আমাদের কাছে নিয়ে এসো সেই শাস্তি, যার প্রতিশ্রুতি তুমি দাও।’\n"
                            + "<b>[সূরা আল-আ‘রাফ: ৭০]</b>\n\n"
                            + "তাদের এই কথা থেকে বোঝা যায় যে, তারা তাদের পূর্বপুরুষদের মূর্তিপূজার রীতিকে অত্যন্ত গুরুত্ব দিত এবং আল্লাহর একত্ববাদকে অস্বীকার করত।\n\n"
                            + "<b>অহংকার ও অবাধ্যতা:</b>\n"
                            + "আদ জাতির দেহের শক্তি এবং পৃথিবীর উপর তাদের আধিপত্য তাদের অহংকারী করে তুলেছিল। তারা মনে করত যে, তাদের শক্তির কারণে তারা অপরাজেয়। কুরআনে আদ জাতির অহংকার সম্পর্কে বলা হয়েছে:\n\n"
                            + "فَأَمَّا عَادٌۭ فَٱسْتَكْبَرُوا۟ فِى ٱلْأَرْضِ بِغَيْرِ ٱلْحَقِّ وَقَالُوا۟ مَنْ أَشَدُّ مِنَّا قُوَّةً ۖ أَوَلَمْ يَرَوْا۟ أَنَّ ٱللَّهَ ٱلَّذِى خَلَقَهُمْ هُوَ أَشَدُّ مِنْهُمْ قُوَّةً ۖ وَكَانُوا۟ بِـَٔايَـٰتِنَا يَجْحَدُونَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আদ জাতি পৃথিবীতে অন্যায়ভাবে অহংকার করত এবং বলত, ‘আমাদের চেয়ে শক্তিশালী আর কে আছে?’ তারা কি দেখে না যে, যিনি তাদের সৃষ্টি করেছেন, তিনি তাদের চেয়েও শক্তিশালী? আর তারা আমার নিদর্শনগুলো অস্বীকার করত।\n"
                            + "<b>[সূরা হা-মীম সাজদাহ: ১৫]</b>\n\n"
                            + "তাদের এই অহংকার তাদেরকে এমন একটি অবস্থানে নিয়ে গিয়েছিল, যেখানে তারা আল্লাহর প্রেরিত নবী হুদ (আলাইহিস সালাম)-এর কথা শোনার পরিবর্তে তাঁকে মিথ্যাবাদী বলে উপহাস করত।\n\n"
                            + "<b>আল্লাহর শাস্তিকে অস্বীকার:</b>\n"
                            + "আদ জাতি বিশ্বাস করতে অস্বীকার করত যে, আল্লাহ তাদের ওপর শাস্তি প্রেরণ করবেন। তারা বারবার হুদ (আলাইহিস সালাম)-কে চ্যালেঞ্জ করত যেন তিনি আল্লাহর শাস্তি নিয়ে আসেন:\n\n"
                            + "فَإِنَّآ أَرْسَلْنَا عَلَيْهِمْ رِيحًا صَرْصَرًاۢ فِى يَوْمِ نَّحْسٍۢ مُّسْتَمِرٍّۢ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর আমি তাদের উপর প্রবল শীতল বাতাস প্রেরণ করেছিলাম দুর্ভাগ্যের এক স্থায়ী দিনে।\n"
                            + "<b>[সূরা আল-কামার: ১৯]</b>",
                    "The people of 'Ad were endowed with exceptional physical strength, towering stature, and great worldly prosperity. However, they became haughty, forgot the divine blessings, indulged in arrogance, and adopted polytheism.\n\n"
                            + "<b>Idolatry and Polytheism:</b>\n"
                            + "Abandoning monotheism, 'Ad worshiped man-made idols. When Hud (AS) called them back to the One True God, they obstinately replied:\n"
                            + "\"They said, 'Have you come to us that we should worship Allah alone and leave what our fathers used to worship? Then bring us that with which you threaten us, if you are truthful.'\" (Surah Al-A'raf 7:70)\n\n"
                            + "<b>Arrogance and False Invincibility:</b>\n"
                            + "Drunk with their physical might and architectural prowess, they boasted:\n"
                            + "\"As for 'Ad, they were arrogant upon the earth without right and said, 'Who is greater than us in strength?' Did they not see that Allah who created them was greater than them in strength? But they were denying Our signs.\" (Surah Fussilat 41:15)\n\n"
                            + "<b>Denying Divine Retribution:</b>\n"
                            + "They ridiculed divine warnings and recklessly demanded Allah's wrath:\n"
                            + "\"So We sent upon them a screaming wind on a day of continuous misfortune.\" (Surah Al-Qamar 54:19)"
            ));

            // 3. নবুওয়াত প্রাপ্তি
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "নবুওয়াত প্রাপ্তি",
                    "Attainment of Prophethood",
                    "আল্লাহ সুবহানাহু ওয়া তা'আলা হযরত হুদ (আলাইহিস সালাম)-কে আদ জাতির জন্য নবী হিসেবে নির্বাচন করেন। তিনি তাদেরকে আল্লাহর প্রতি ঈমান আনা, মূর্তিপূজা ত্যাগ করা এবং ন্যায়পরায়ণ জীবনের প্রতি আহ্বান জানিয়েছিলেন। হুদ (আলাইহিস সালাম) কুরআনে বারবার নবী হিসেবে পরিচিতি পেয়েছেন এবং তাঁর দাওয়াতের বর্ণনা বিভিন্ন সূরায় এসেছে। হযরত হুদ (আলাইহিস সালাম) কত বছর বয়সে নবুওয়াত প্রাপ্ত হন, এ বিষয়ে কুরআন এবং সহিহ হাদিসে কোনো নির্দিষ্ট তথ্য উল্লেখ করা হয়নি। তবে ইসলামী ঐতিহ্য ও ইতিহাসবিদদের মত অনুযায়ী, নবীগণ সাধারণত প্রাপ্তবয়স্ক হয়ে এবং পূর্ণ বুদ্ধি ও প্রজ্ঞা অর্জনের পর নবুওয়াত প্রাপ্ত হন।\n\n"
                            + "<b>নবীদের জীবনে সাধারণত তিনটি প্রধান ধাপ লক্ষ্য করা যায়:</b>\n\n"
                            + "• <b>প্রাপ্তবয়স্ক হওয়া:</b> সাধারণত ৩০ থেকে ৪০ বছর বয়সের মধ্যে নবীদের পূর্ণ জ্ঞান ও প্রজ্ঞা প্রকাশিত হয়।\n"
                            + "• <b>আল্লাহর পক্ষ থেকে দায়িত্ব গ্রহণ:</b> নবুওয়াতের মাধ্যমে নবী তাদের জাতির প্রতি দাওয়াত দেওয়ার দায়িত্ব পান।\n"
                            + "• <b>দাওয়াত শুরু:</b> নবুওয়াত প্রাপ্তির পর নবীগণ তাওহীদের দাওয়াত দিয়ে কাজ শুরু করেন।\n\n"
                            + "যেহেতু রাসুলুল্লাহ ﷺ নবুওয়াত প্রাপ্ত হন ৪০ বছর বয়সে, তাই ধারণা করা হয় হযরত হুদ (আলাইহিস সালাম) এবং অন্যান্য নবীগণও এ সময়ের কাছাকাছি বয়সে নবুওয়াত প্রাপ্ত হয়েছিলেন।\n\n"
                            + "وَإِلَىٰ عَادٍ أَخَاهُمْ هُودًۭا ۗ قَالَ يَـٰقَوْمِ ٱعْبُدُوا۟ ٱللَّهَ مَا لَكُم مِّنْ إِلَـٰهٍ غَيْرُهُۥٓ ۚ إِنْ أَنتُمْ إِلَّا مُفْتَرُونَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এবং আদ জাতির প্রতি তাদের ভাই হুদকে প্রেরণ করলাম। তিনি বললেন, 'হে আমার জাতি! তোমরা আল্লাহর ইবাদত করো, তিনি ছাড়া তোমাদের কোনো ইলাহ নেই। তোমরা তো শুধু মিথ্যা বাণী তৈরি করছো।\n"
                            + "<b>[সূরা হূদ: ৫০]</b>\n\n"
                            + "<b>নবুওয়াতের উদ্দেশ্য:</b>\n\n"
                            + "• <b>তাওহীদের প্রচার:</b> একমাত্র আল্লাহর ইবাদতের প্রতি আহ্বান জানানো এবং মূর্তিপূজা থেকে দূরে রাখা।\n"
                            + "• <b>শিরক ও অন্যায় থেকে বিরত রাখা:</b> আদ জাতি শিরক এবং দুনিয়াবি অহংকারে লিপ্ত ছিল। তাদেরকে এই পথ থেকে ফিরিয়ে আনার চেষ্টা করা।\n"
                            + "• <b>আখিরাতের প্রতি সতর্ক করা:</b> আল্লাহর শাস্তির ভয় প্রদর্শন করা এবং আখিরাতের জীবনের জন্য প্রস্তুতি নিতে বলা।\n\n"
                            + "إِنِّىٓ أُبَلِّغُكُمْ رِسَـٰلَـٰتِ رَبِّى وَأَنَا۠ لَكُمْ نَاصِحٌ أَمِينٌ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমি তোমাদের কাছে আমার রবের বাণী পৌঁছে দিচ্ছি এবং আমি তোমাদের জন্য একজন বিশ্বস্ত উপদেশদাতা।\n"
                            + "<b>[সূরা আল-আ‘রাফ: ৬৮]</b>",
                    "Allah chosen Prophet Hud (peace be upon him) as His messenger to the tribe of 'Ad, commanding him to call them to monotheism, abandon idols, and live righteously.\n\n"
                            + "<b>Phases of Prophetic Mission:</b>\n"
                            + "• <b>Maturity and Wisdom:</b> Reaching intellectual and physical maturity (usually around the age of 40).\n"
                            + "• <b>Divine Mandate:</b> Receiving divine revelation (Wahy) and mission from Allah.\n"
                            + "• <b>Active Dawah:</b> Proclaiming the oneness of Allah and reforming society.\n\n"
                            + "\"And to 'Ad [We sent] their brother Hud. He said, 'O my people, worship Allah; you have no deity other than Him. You are not but inventors [of falsehood].'\" (Surah Hud 11:50)\n\n"
                            + "<b>Core Objectives of His Prophethood:</b>\n"
                            + "• <b>Establishing Tawheed:</b> Calling humanity to worship Allah alone and eliminate idolatry.\n"
                            + "• <b>Eradicating Injustice & Hubris:</b> Directing the mighty 'Ad away from oppression and earthly arrogance.\n"
                            + "• <b>Warning of the Hereafter:</b> Reminding them of the Day of Judgment and divine accountability.\n\n"
                            + "\"I convey to you the messages of my Lord, and I am to you a trustworthy adviser.\" (Surah Al-A'raf 7:68)"
            ));

            // 4. আল্লাহর দিকে আহ্বান
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "আল্লাহর দিকে আহ্বান",
                    "Calling towards Allah",
                    "আল্লাহর দিকে আহ্বান (দাওয়াত ইলাল্লাহ) নবীগণের প্রধান দায়িত্ব, যা সমস্ত নবীর জীবন এবং মিশনের মূল ভিত্তি ছিল। এই আহ্বান তাওহীদের প্রতি (এক আল্লাহর ইবাদতের দিকে) এবং শিরক, অন্যায় ও পাপাচার থেকে বিরত থাকার নির্দেশনা। হযরত হুদ (আলাইহিস সালাম)-এর দাওয়াত আদ জাতির কাছে আল্লাহর একত্ববাদের গুরুত্ব এবং তাদের গোমরাহি থেকে ফিরে আসার প্রয়োজনীয়তাকে তুলে ধরেছিল।\n\n"
                            + "<b>কুরআনের আলোকে হুদ (আলাইহিস সালাম)-এর দাওয়াত:</b>\n"
                            + "হযরত হুদ (আলাইহিস সালাম) আদ জাতিকে সরাসরি আহ্বান জানান তাওহীদের পথে ফিরে আসার জন্য। তিনি বলেন:\n\n"
                            + "يَـٰقَوْمِ ٱعْبُدُوا۟ ٱللَّهَ مَا لَكُم مِّنْ إِلَـٰهٍ غَيْرُهُۥٓ ۚ إِنْ أَنتُمْ إِلَّا مُفْتَرُونَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে আমার জাতি! তোমরা আল্লাহর ইবাদত করো, তিনি ছাড়া তোমাদের কোনো উপাস্য নেই। তোমরা তো শুধুই মিথ্যা বানিয়ে নিচ্ছো।\n"
                            + "<b>[সূরা হূদ: ৫০]</b>\n\n"
                            + "<b>দাওয়াতের মূল বিষয়বস্তু:</b>\n\n"
                            + "<b>১. তাওহীদ প্রতিষ্ঠা:</b>\n"
                            + "হুদ (আলাইহিস সালাম) আদ জাতিকে স্মরণ করিয়ে দেন যে, একমাত্র আল্লাহই তাদের স্রষ্টা, প্রতিপালক, এবং তাঁরই ইবাদত করা উচিত।\n\n"
                            + "إِنْ أُرِيدُ إِلَّا ٱلْإِصْلَـٰحَ مَا ٱسْتَطَعْتُ ۚ وَمَا تَوْفِيقِىٓ إِلَّا بِٱللَّهِ ۚ عَلَيْهِ تَوَكَّلْتُ وَإِلَيْهِ أُنِيبُ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমি তো শুধুমাত্র সংশোধন করতে চাই, যতটুকু পারি। আর আমার তাওফীক (সফলতা) কেবল আল্লাহরই পক্ষ থেকে। আমি তাঁরই ওপর ভরসা করি এবং তাঁরই দিকে ফিরে আসি।\n"
                            + "<b>[সূরা হূদ: ৮৮]</b>\n\n"
                            + "<b>২. শিরক থেকে বিরত থাকা:</b>\n"
                            + "হুদ (আলাইহিস সালাম) আদ জাতিকে সতর্ক করেন তাদের মূর্তিপূজা এবং ভুল বিশ্বাস থেকে। তিনি তাদের মনে করিয়ে দেন যে, এসব তাদের ধ্বংসের কারণ হতে পারে।\n\n"
                            + "فَٱتَّقُوا۟ ٱللَّهَ وَأَطِيعُونِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাহলে তোমরা আল্লাহকে ভয় করো এবং আমার অনুসরণ করো।\n"
                            + "<b>[সূরা আশ-শু'আরা: ১২৬]</b>\n\n"
                            + "<b>৩. আল্লাহর শাস্তির ভয় প্রদর্শন:</b>\n"
                            + "যারা হুদ (আলাইহিস সালাম)-এর আহ্বান প্রত্যাখ্যান করে, তাদের জন্য শাস্তির হুমকি দিয়ে তিনি বলেন:\n\n"
                            + "وَيَـٰقَوْمِ ٱسْتَغْفِرُوا۟ رَبَّكُمْ ثُمَّ تُوبُوٓا۟ إِلَيْهِ يُرْسِلِ ٱلسَّمَآءَ عَلَيْكُم مِّدْرَارًۭا وَيَزِدْكُمْ قُوَّةً إِلَىٰ قُوَّتِكُمْ وَلَا تَتَوَلَّوْا۟ مُجْرِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে আমার জাতি! তোমরা তোমাদের প্রতিপালকের কাছে ক্ষমা প্রার্থনা করো, এরপর তাঁর দিকে ফিরে এসো। তিনি তোমাদের ওপর প্রচুর বৃষ্টি বর্ষণ করবেন এবং তোমাদের শক্তির ওপর আরও শক্তি বৃদ্ধি করবেন। আর তোমরা অপরাধী হয়ে ফিরে এসো না।\n"
                            + "<b>[সূরা হূদ: ৫২]</b>",
                    "Calling to the path of Allah (Dawah ilallah) was the supreme foundation of Prophet Hud's mission, summoning the people of 'Ad to monotheism and righteous living.\n\n"
                            + "<b>The Essence of Hud's Dawah:</b>\n"
                            + "\"O my people, worship Allah; you have no deity other than Him. You are not but inventors [of falsehood].\" (Surah Hud 11:50)\n\n"
                            + "<b>Key Pillars of His Call:</b>\n"
                            + "<b>1. Establishing Pure Monotheism (Tawheed):</b>\n"
                            + "Reminding them that Allah alone is the Sustainer and Creator deserving of all worship:\n"
                            + "\"I only intend reform as much as I am able. And my success is not but through Allah. Upon Him I have relied, and to Him I return.\" (Surah Hud 11:88)\n\n"
                            + "<b>2. Abandoning Idolatry and Injustice:</b>\n"
                            + "Warning against idols and tyrannical practices:\n"
                            + "\"So fear Allah and obey me.\" (Surah Ash-Shu'ara 26:126)\n\n"
                            + "<b>3. Repentance and Promise of Divine Abundance:</b>\n"
                            + "\"And O my people, ask forgiveness of your Lord and then repent to Him. He will send [rain from] the sky upon you in showers and increase you in strength to your strength. And do not turn away, [being] criminals.\" (Surah Hud 11:52)"
            ));

            // 5. আদ জাতির প্রতিক্রিয়া ও উপহাস
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "আদ জাতির প্রতিক্রিয়া ও উপহাস",
                    "Reaction and Mockery of the Tribe of Ad",
                    "আদ জাতি, হযরত হুদ (আলাইহিস সালাম)-এর জাতি, আল্লাহর একত্ববাদের দাওয়াত ও সতর্কবাণীকে অস্বীকার করে অহংকার ও অবজ্ঞার চূড়ান্ত উদাহরণ স্থাপন করেছিল। তাদের প্রতিক্রিয়া এবং হুদ (আলাইহিস সালাম)-এর আহ্বান প্রত্যাখ্যান করার পদ্ধতি ছিল কুরআনে উল্লেখিত, যা বিভিন্ন আয়াতে বর্ণিত হয়েছে। এটি বিস্তারিতভাবে বিশ্লেষণ করা যাক:\n\n"
                            + "<b>১. নবীর প্রতি অবজ্ঞা এবং মিথ্যাবাদী বলা:</b>\n"
                            + "হুদ (আলাইহিস সালাম)-এর জাতি তাঁকে অবজ্ঞাভরে মূর্খ এবং বিভ্রান্ত বলে অভিহিত করে। এটি তাদের সত্যকে অস্বীকার করার প্রবণতাকে প্রকাশ করে।\n\n"
                            + "قَالَ ٱلْمَلَأُ ٱلَّذِينَ كَفَرُوا۟ مِن قَوْمِهِۦٓ إِنَّا لَنَرَىٰكَ فِى سَفَاهَةٍۢ وَإِنَّا لَنَظُنُّكَ مِنَ ٱلْكَـٰذِبِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাঁর জাতির মধ্যে যারা কাফের ছিল, তারা বলল, ‘আমরা তোমাকে মূর্খ মনে করি এবং আমরা মনে করি তুমি মিথ্যাবাদীদের অন্তর্ভুক্ত।\n"
                            + "<b>[সূরা আল-আ‘রাফ: ৬৬]</b>\n\n"
                            + "<b>তাদের বক্তব্যের মর্মার্থ:</b>\n"
                            + "• হুদ (আলাইহিস সালাম)-এর দাওয়াতকে তারা মূল্যহীন মনে করত।\n"
                            + "• তাঁকে \"মিথ্যাবাদী\" বলার মাধ্যমে তারা সত্যের প্রতিকূল ছিল।\n\n"
                            + "<b>২. আল্লাহর শাস্তির প্রতি চ্যালেঞ্জ:</b>\n"
                            + "আদ জাতি আল্লাহর শাস্তি আসার বিষয়টি তুচ্ছজ্ঞান করে এবং চ্যালেঞ্জ জানায়। তারা হুদ (আলাইহিস সালাম)-এর সতর্কবার্তা শুনেও ব্যঙ্গ করে বলে:\n\n"
                            + "فَأْتِنَا بِمَا تَعِدُنَآ إِن كُنتَ مِنَ ٱلصَّـٰদِقِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাহলে নিয়ে এসো আমাদের কাছে সেই শাস্তি, যার প্রতিশ্রুতি তুমি দাও, যদি তুমি সত্যবাদী হও।\n"
                            + "<b>[সূরা আল-আ‘রাফ: ৭০]</b>\n\n"
                            + "<b>তাদের চ্যালেঞ্জের অর্থ:</b>\n"
                            + "• তারা আল্লাহর শক্তি ও শাস্তিকে অবিশ্বাস করত।\n"
                            + "• তারা মনে করত যে হুদ (আলাইহিস সালাম) তাদের শাস্তি নিয়ে ভয় দেখাচ্ছেন, যা কখনো বাস্তবায়িত হবে না।\n\n"
                            + "<b>৩. পূর্বপুরুষদের উপাসনার প্রতি আঁকড়ে থাকা:</b>\n"
                            + "আদ জাতি তাদের মূর্তিপূজার ঐতিহ্যকে অত্যন্ত গুরুত্ব দিত। তারা তাদের পূর্বপুরুষদের উপাসনার পথ ছেড়ে দিতে অস্বীকৃতি জানায়। তারা বলে:\n\n"
                            + "قَالُوا۟ أَجِئْتَنَا لِنَعْبُدَ ٱللَّهَ وَحْدَهُۥ وَنَذَرَ مَا كَانَ يَعْبُدُ ءَابَآؤُنَا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা বলল, ‘তুমি কি আমাদের কাছে এসেছ, যাতে আমরা আল্লাহকে একমাত্র উপাস্য হিসেবে ইবাদত করি এবং আমাদের পূর্বপুরুষরা যাদের ইবাদত করত, তাদের ছেড়ে দিই?\n"
                            + "<b>[সূরা হূদ: ৬২]</b>\n\n"
                            + "<b>তাদের বক্তব্যের উদ্দেশ্য:</b>\n"
                            + "• তারা তাদের ঐতিহ্যকেই সঠিক মনে করত এবং সেটি পরিবর্তন করতে চায়নি।\n"
                            + "• তারা নবীর দাওয়াতকে অগ্রাহ্য করার জন্য ঐতিহ্যের অজুহাত ব্যবহার করত।\n\n"
                            + "<b>৪. নিজেদের শক্তি ও সামর্থ্যের গর্ব:</b>\n"
                            + "আদ জাতি তাদের দেহের শক্তি, উচ্চতা, এবং স্থাপত্যশিল্পের কারণে নিজেকে অপরাজেয় মনে করত। তারা বলেছিল:\n\n"
                            + "وَقَالُوا۟ مَنْ أَشَدُّ مِنَّا قُوَّةً\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা বলেছিল, ‘আমাদের চেয়ে শক্তিশালী আর কে আছে?\n"
                            + "<b>[সূরা হা-মীম সাজদাহ: ১৫]</b>\n\n"
                            + "<b>তাদের এই অহংকারের প্রতিক্রিয়া:</b>\n"
                            + "• তারা নিজেদের শক্তিকে আল্লাহর শক্তির চেয়েও বড় মনে করেছিল।\n"
                            + "• তারা মনে করত, তারা এমন এক জাতি যা ধ্বংস হতে পারে না।\n\n"
                            + "<b>৫. নবীর বার্তা অস্বীকার এবং প্রমাণের দাবি:</b>\n"
                            + "তারা নবীর সতর্কবাণী এবং দাওয়াতকে \"প্রমাণহীন\" বলে দাবি করেছিল:\n\n"
                            + "مَا جِئْتَنَا بِبَيِّنَةٍۢ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তুমি আমাদের কাছে কোনো প্রমাণ নিয়ে আসনি।\n"
                            + "<b>[সূরা হূদ: ৫৩]</b>\n\n"
                            + "<b>তাদের বক্তব্যের প্রেক্ষাপট:</b>\n"
                            + "• তারা মনে করত হুদ (আলাইহিস সালাম)-এর বক্তব্য কেবল কথার খেলাপ।\n"
                            + "• তারা প্রমাণের অজুহাত দেখিয়ে সত্য গ্রহণ থেকে দূরে থাকতে চেয়েছিল।\n\n"
                            + "<b>৬. দাওয়াতকে কটাক্ষ ও উপহাস করা:</b>\n"
                            + "হুদ (আলাইহিস সালাম)-এর সতর্কবাণী এবং আহ্বানকে তারা তুচ্ছজ্ঞান করে কটাক্ষ করত। কুরআনে বর্ণিত হয়েছে, তারা বলত:\n\n"
                            + "إِن نَّقُولُ إِلَّا ٱعْتَرَىٰكَ بَعْضُ ءَالِهَتِنَا بِسُوٓءٍ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমরা শুধু বলি, ‘আমাদের কোনো উপাস্য তোমার কোনো ক্ষতি করেছে।\n"
                            + "<b>[সূরা হূদ: ৫৪]</b>\n\n"
                            + "<b>তাদের বক্তব্যের লক্ষ্য:</b>\n"
                            + "• তারা মনে করত, হুদ (আলাইহিস সালাম)-এর নবুওয়াত তাদের উপাস্যদের জন্য হুমকি।\n"
                            + "• তারা নবীর প্রতি শত্রুতাপূর্ণ মনোভাব পোষণ করত।",
                    "The chieftains and people of 'Ad responded to Prophet Hud's sincere invitations with deep-seated hostility, mockery, and outright defiance.\n\n"
                            + "<b>1. Accusing the Prophet of Foolishness and Lying:</b>\n"
                            + "\"Said the eminent ones who disbelieved among his people, 'Indeed, we see you in foolishness, and indeed, we think you are of the liars.'\" (Surah Al-A'raf 7:66)\n\n"
                            + "<b>2. Mocking and Demanding the Divine Punishment:</b>\n"
                            + "\"They said, 'Then bring us that with which you threaten us, if you are truthful.'\" (Surah Al-A'raf 7:70)\n\n"
                            + "<b>3. Blind Allegiance to Ancestral Idols:</b>\n"
                            + "\"They said, 'Have you come to us that we should worship Allah alone and leave what our fathers used to worship?'\" (Surah Hud 11:62)\n\n"
                            + "<b>4. Boasting of Military and Physical Invincibility:</b>\n"
                            + "\"And they said, 'Who is greater than us in strength?'\" (Surah Fussilat 41:15)\n\n"
                            + "<b>5. Rejecting Clear Evidence:</b>\n"
                            + "\"They said, 'O Hud, you have not brought us clear evidence...'\" (Surah Hud 11:53)\n\n"
                            + "<b>6. Claiming the Prophet was Cursed by their Idols:</b>\n"
                            + "\"We only say that some of our gods have possessed you with evil [madness].\" (Surah Hud 11:54)"
            ));

            // 6. আল্লাহর শাস্তি ও আদ জাতির ধ্বংস
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "আল্লাহর শাস্তি ও আদ জাতির ধ্বংস",
                    "Allah's Punishment and Destruction of the Tribe of Ad",
                    "আদ জাতি তাদের অহংকার, অবাধ্যতা এবং আল্লাহর নবী হযরত হুদ (আলাইহিস সালাম)-এর দাওয়াত প্রত্যাখ্যান করার কারণে কঠোর শাস্তির সম্মুখীন হয়। তাদের ধ্বংসের বর্ণনা কুরআনের বিভিন্ন স্থানে বিশদভাবে উল্লেখ করা হয়েছে। তারা আল্লাহর একত্ববাদকে অস্বীকার করে মূর্তিপূজায় লিপ্ত হয়েছিল এবং নিজেদের শক্তি ও ক্ষমতার গর্বে অন্ধ হয়ে গিয়েছিল।\n\n"
                            + "<b>আদ জাতির প্রতি শাস্তির কারণ:</b>\n\n"
                            + "<b>১. তাওহীদ প্রত্যাখ্যান এবং শিরক:</b>\n"
                            + "তারা আল্লাহর একত্ববাদকে অস্বীকার করে মূর্তিপূজা চালিয়ে যাচ্ছিল। তারা তাদের পূর্বপুরুষদের রীতি-নীতি ধরে রেখেছিল এবং হুদ (আলাইহিস সালাম)-এর আহ্বানকে তুচ্ছজ্ঞান করেছিল।\n\n"
                            + "وَإِلَىٰ عَادٍ أَخَاهُمْ هُودًا ۗ قَالَ يَـٰقَوْمِ ٱعْبُدُوا۟ ٱللَّهَ مَا لَكُم مِّنْ إِلَـٰهٍ غَيْرُهُۥٓ ۚ إِنْ أَنتُمْ إِلَّا مُفْتَرُونَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আদ জাতির প্রতি তাদের ভাই হুদকে প্রেরণ করলাম। তিনি বললেন, ‘হে আমার জাতি! তোমরা আল্লাহর ইবাদত করো। তিনি ছাড়া তোমাদের কোনো উপাস্য নেই। তোমরা তো মিথ্যা বানিয়ে নিচ্ছো।\n"
                            + "<b>[সূরা হূদ: ৫০]</b>\n\n"
                            + "<b>২. অহংকার ও আল্লাহর শক্তি অস্বীকার:</b>\n"
                            + "তারা নিজেদের শক্তি এবং ক্ষমতার গর্বে অন্ধ হয়ে আল্লাহর শক্তিকে অস্বীকার করেছিল। তারা বলেছিল:\n\n"
                            + "وَقَالُوا۟ مَنْ أَشَدُّ مِنَّا قُوَّةً ۖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা বলেছিল, ‘আমাদের চেয়ে শক্তিশালী আর কে আছে?\n"
                            + "<b>[সূরা হা-মীম সাজদাহ: ১৫]</b>\n\n"
                            + "<b>৩. নবীর সতর্কবাণীকে উপহাস:</b>\n"
                            + "তারা হুদ (আলাইহিস সালাম)-এর সতর্কবাণীকে গুরুত্ব দেয়নি এবং আল্লাহর শাস্তিকে নিয়ে উপহাস করেছিল।\n\n"
                            + "فَأْتِنَا بِمَا تَعِدُنَآ إِن كُنتَ مِنَ ٱلصَّـٰদِقِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাহলে তুমি যদি সত্যবাদী হও, তবে নিয়ে এসো আমাদের কাছে সেই শাস্তি, যার প্রতিশ্রুতি তুমি দাও।\n"
                            + "<b>[সূরা আল-আ‘রাফ: ৭০]</b>\n\n"
                            + "<b>আল্লাহর শাস্তি, প্রবল বায়ু ঝড়:</b>\n"
                            + "আদ জাতির উপর আল্লাহ একটি ধ্বংসাত্মক বায়ু প্রেরণ করেন, যা তাদেরকে সম্পূর্ণভাবে ধ্বংস করে দেয়। এই বায়ু ছিল অত্যন্ত প্রবল এবং ঠাণ্ডা, যা তাদের সাত রাত এবং আট দিন পর্যন্ত আঘাত করেছিল।\n\n"
                            + "وَأَمَّا عَادٌۭ فَأُهْلِكُوا۟ بِرِيحٍۢ صَرْصَرٍ عَاتِيَةٍۢ  سَخَّرَهَا عَلَيْهِمْ سَبْعَ لَيَالٍۢ وَثَمَـٰنِيَةَ أَيَّامٍ حُسُومًۭا فَتَرَى ٱلْقَوْمَ فِيهَا صَرْعَىٰ كَأَنَّهُمْ أَعْجَازُ نَخْلٍ خَاوِيَةٍۢ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর আদ জাতিকে ধ্বংস করা হয়েছিল এক প্রবল ও শীতল বায়ু দিয়ে। যা তাদের ওপর লাগাতার চালানো হয়েছিল সাত রাত এবং আট দিন ধরে, ফলে তুমি দেখবে যে, তারা সেখানে পড়ে আছে, যেন তারা শূন্য খেজুর গাছের গুঁড়ি।\n"
                            + "<b>[সূরা আল-হাক্কাহ: ৬-৭]</b>\n\n"
                            + "<b>বায়ু ঝড়ের বৈশিষ্ট্য:</b>\n\n"
                            + "• <b>প্রবল শীতল বায়ু:</b> এটি একটি প্রচণ্ড ঠাণ্ডা ও ধ্বংসাত্মক ঝড় ছিল, যা তাদের ঘরবাড়ি ও দেহ চূর্ণবিচূর্ণ করে দেয়।\n"
                            + "• <b>দীর্ঘস্থায়ী শাস্তি:</b> সাত রাত ও আট দিন পর্যন্ত বায়ু তাদের উপর ক্রমাগত আঘাত করে।\n"
                            + "• <b>সম্পূর্ণ ধ্বংস:</b> বায়ুর আঘাতে তারা মাটিতে লুটিয়ে পড়ে এবং তাদেরকে শূন্য খেজুর গাছের গুঁড়ির মতো মনে হয়।\n\n"
                            + "<b>আল্লাহর শক্তির প্রতিফলন:</b>\n"
                            + "আদ জাতি নিজেদের শক্তি ও সামর্থ্যের গর্ব করত, কিন্তু আল্লাহ তাদের ধ্বংস করে দেখিয়ে দেন যে, প্রকৃত শক্তি কেবলমাত্র আল্লাহর।\n\n"
                            + "أَوَلَمْ يَرَوْا۟ أَنَّ ٱللَّهَ ٱلَّذِى خَلَقَهُمْ هُوَ أَشَدُّ مِنْهُمْ قُوَّةً ۖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তারা কি দেখেনি যে, তাদের যিনি সৃষ্টি করেছেন, তিনি তাদের চেয়ে অধিক শক্তিশালী?\n"
                            + "<b>[সূরা হা-মীম সাজদাহ: ১৫]</b>\n\n"
                            + "<b>ধ্বংসের ফলাফল:</b>\n\n"
                            + "• <b>সম্পূর্ণ ধ্বংস:</b> আদ জাতি এবং তাদের অবিশ্বাসী নেতারা ধ্বংস হয়ে যায়।\n"
                            + "• <b>উদাহরণ:</b> তারা ইতিহাসে এক দৃষ্টান্ত হয়ে থাকে, যাতে মানুষ শিখতে পারে।\n\n"
                            + "فَكَيْفَ كَانَ عَذَابِى وَنُذُرِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর আমার শাস্তি এবং আমার সতর্কীকরণ কেমন ছিল?\n"
                            + "<b>[সূরা আল-কামার: ১৬]</b>",
                    "Persistent arrogance, idol worship, and relentless mockery of Prophet Hud brought down absolute divine retribution upon the people of 'Ad.\n\n"
                            + "<b>Causes of the Divine Judgment:</b>\n"
                            + "• Rejection of Tawheed and insistence on ancestral idolatry.\n"
                            + "• Arrogant pride in their physical might and denial of God's supreme power.\n"
                            + "• Reckless mocking and demanding the descent of torment.\n\n"
                            + "<b>The Screaming, Freezing Gale (Sarsarun 'Atiyah):</b>\n"
                            + "Allah unleashed a ferocious, bitterly freezing gale that struck them ceaselessly for seven nights and eight days:\n"
                            + "\"And as for 'Ad, they were destroyed by a screaming, violent wind, which Allah subjected upon them for seven nights and eight days in succession, so that you would see the people therein fallen as if they were hollow trunks of palm trees.\" (Surah Al-Haqqah 69:6-7)\n\n"
                            + "<b>The Aftermath of Utter Destruction:</b>\n"
                            + "The towering warriors and magnificent palaces were obliterated into desolate wasteland, leaving them as an eternal warning for generations to come.\n"
                            + "\"So how [severe] were My punishment and My warnings?\" (Surah Al-Qamar 54:16)"
            ));

            // 7. আদ জাতির ধ্বংসের পরের জীবন
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "আদ জাতির ধ্বংসের পরের জীবন",
                    "Life after the Destruction of the Tribe of Ad",
                    "আদ জাতি, যাদের আল্লাহ তাদের অবাধ্যতা ও অহংকারের কারণে ধ্বংস করেছিলেন, কুরআনের বর্ণনা অনুসারে একটি গুরুত্বপূর্ণ দৃষ্টান্ত হয়ে আছে। তাদের ধ্বংসের পর পৃথিবীতে কী ঘটেছিল এবং মানবজাতি কীভাবে তাদের ধ্বংস থেকে শিক্ষা নিয়েছিল, তা কুরআন ও ইসলামী ঐতিহ্যে প্রতিফলিত হয়েছে।\n\n"
                            + "<b>আদ জাতির ধ্বংস এবং তাদের জমি:</b>\n"
                            + "আদ জাতি ধ্বংসের পর তাদের জমি সম্পূর্ণ শূন্য হয়ে গিয়েছিল। কুরআনে উল্লেখ রয়েছে, তাদের ধ্বংসের পর সেই অঞ্চল শূন্য এবং মরুভূমিতে পরিণত হয়েছিল। তাদের ধ্বংসকে একটি নিদর্শন হিসেবে রেখে আল্লাহ ভবিষ্যতের জাতিগুলোকে সতর্ক করেছেন।\n\n"
                            + "فَأَصْبَحُوا۟ لَا يُرَىٰٓ إِلَّا মَسَـٰكِنُهُمْ ۗ كَذَٰلِكَ نَجْزِى ٱلْقَوْمَ ٱلْمُجْرِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতঃপর তাদের অবস্থান এ রকম হলো যে, তাদের আবাসস্থল ছাড়া আর কিছুই দৃশ্যমান রইল না। আমরা এভাবেই অপরাধী জাতিকে প্রতিদান দিয়ে থাকি।\n"
                            + "<b>[সূরা আল-আহকাফ: ২৫]</b>\n\n"
                            + "<b>হুদ (আলাইহিস সালাম) এবং ঈমানদারদের জীবন:</b>\n"
                            + "হযরত হুদ (আলাইহিস সালাম) এবং তাঁর প্রতি ঈমানদাররা আল্লাহর রহমতে শাস্তি থেকে রক্ষা পান। তাঁদের ধ্বংসের সময় নিরাপদে একটি স্থানে সরিয়ে নেওয়া হয়েছিল। হুদ (আলাইহিস সালাম)-এর নেতৃত্বে তারা একটি নতুন জীবন শুরু করেন এবং তাওহীদের পথ ধরে চলতে থাকেন।\n\n"
                            + "<b>হুদ (আলাইহিস সালাম)-এর পরবর্তী কার্যক্রম:</b>\n"
                            + "ইসলামী ঐতিহ্য অনুসারে, হুদ (আলাইহিস সালাম) ধ্বংস থেকে বেঁচে যাওয়া ঈমানদারদের নিয়ে অন্যত্র চলে যান। কিছু বর্ণনা অনুসারে, তিনি পরে হাদরামাউতের (ইয়েমেনের একটি অঞ্চল) দিকে চলে যান এবং সেখানে মৃত্যু বরণ করেন।\n\n"
                            + "<b>পরবর্তী জাতির উদ্ভব:</b>\n"
                            + "আদ জাতির ধ্বংসের পর তাদের স্থলাভিষিক্ত হয় \"থামুদ জাতি।\" থামুদ জাতি আদ জাতির মতোই শক্তিশালী ছিল এবং তারা স্থাপত্য ও কৃত্রিম বসতি নির্মাণে পারদর্শী ছিল। তবে, তারা একই ধরনের ভুলে লিপ্ত হয়েছিল—তারা আল্লাহকে ভুলে গিয়ে মূর্তিপূজা শুরু করেছিল। আল্লাহ তাদের জন্য নবী সালিহ (আলাইহিস সালাম)-কে পাঠান।\n\n"
                            + "وَعَادًۭا وَثَمُودَا۟ وَقَد تَّبَيَّنَ لَكُم مِّن مَّسَـٰكِنِهِمْ ۖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আদ ও সামুদ— এবং তাদের আবাসস্থল থেকে এটি তোমাদের কাছে স্পষ্ট।\n"
                            + "<b>[সূরা আনকাবুত: ৩৮]</b>",
                    "After the total devastation of the tyrants of 'Ad, the fertile lands turned into a silent desert, standing as an enduring sign of divine power for future civilizations.\n\n"
                            + "<b>Desolate Ruins of 'Ad:</b>\n"
                            + "\"So they became not to be seen except their dwellings. Thus do We recompense the criminal people.\" (Surah Al-Ahqaf 46:25)\n\n"
                            + "<b>Deliverance of Hud and the Believers:</b>\n"
                            + "By divine mercy, Prophet Hud (AS) and the small group of righteous believers were shielded from the fierce windstorm. They migrated southward toward the region of Hadhramaut in Yemen, continuing their lives in pure monotheistic devotion.\n\n"
                            + "<b>Rise of the Next Civilization (Thamud):</b>\n"
                            + "Generations later, the tribe of Thamud inherited their power and carved dwellings into rocky mountains, but fell into the same idolatrous traps until Allah sent Prophet Salih (AS) to them.\n"
                            + "\"And [We destroyed] 'Ad and Thamud, and it has become clear to you from their [ruined] dwellings.\" (Surah Al-Ankabut 29:38)"
            ));

            // 8. হুদ (আলাইহি সালাম)-এর শেষ জীবন
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "হুদ (আলাইহি সালাম)-এর শেষ জীবন",
                    "Final Days and Passing of Prophet Hud (AS)",
                    "হযরত হুদ (আলাইহি সালাম)-এর মৃত্যুর ব্যাপারে ইসলামী ঐতিহ্যে কিছু বর্ণনা পাওয়া যায়, তবে এ বিষয়ে কুরআন এবং সহিহ হাদিসে সরাসরি উল্লেখ নেই। ইসলামী ঐতিহাসিক ও তাফসিরবিদগণ বিভিন্ন বর্ণনার মাধ্যমে তাঁর মৃত্যুর সময় ও স্থান সম্পর্কে আলোচনা করেছেন।\n\n"
                            + "<b>মৃত্যুর পর স্থানান্তর এবং সমাধিস্থল:</b>\n"
                            + "ইসলামী ঐতিহ্যে বেশিরভাগ সূত্র মতে, আদ জাতি ধ্বংসের পরে হুদ (আলাইহি সালাম) তাঁর প্রতি ঈমান আনয়নকারী লোকদের নিয়ে ইয়েমেনের হাদরামাউত অঞ্চলে চলে যান। সেখানেই তিনি জীবনের শেষ দিনগুলো কাটান এবং মারা যান। তাঁর সমাধি সম্পর্কে বলা হয়:\n\n"
                            + "• <b>হাদরামাউত (ইয়েমেন):</b> অধিকাংশ বর্ণনামতে, হাদরামাউত এলাকায় একটি স্থানে তাঁর কবর রয়েছে। স্থানীয় ঐতিহ্যে এটি \"কবরুন নাবি হুদ\" নামে পরিচিত।\n"
                            + "• <b>দমাস্কাস (সিরিয়া):</b> কিছু সূত্রে বলা হয়েছে, তিনি সিরিয়ার অঞ্চলে মারা যান।\n"
                            + "• <b>মক্কা:</b> কিছু মতানুসারে, তিনি মক্কায় ফিরে যান এবং সেখানে মৃত্যুবরণ করেন।\n"
                            + "তবে, এসব স্থানের নির্ভুলতা সম্পর্কে চূড়ান্ত নিশ্চিত প্রমাণ পাওয়া যায় না।\n\n"
                            + "<b>তাঁর মৃত্যু সম্পর্কে ঐতিহ্যবাহী তথ্য:</b>\n"
                            + "তাফসির এবং ইসলামী ইতিহাসের বইগুলোতে হুদ (আলাইহি সালাম)-এর মৃত্যু সম্পর্কে নিম্নলিখিত তথ্য পাওয়া যায়:\n\n"
                            + "<b>আল্লাহর প্রতি আনুগত্যপূর্ণ জীবন:</b>\n"
                            + "তিনি তাঁর সমগ্র জীবনে আল্লাহর নির্দেশ মেনে চলেন এবং ঈমানের পথে অবিচল থাকেন। মৃত্যুর আগে তিনি তাঁর অনুসারীদের তাওহীদের প্রতি দৃঢ় থাকার উপদেশ দেন।",
                    "Prophet Hud (peace be upon him) spent the remainder of his life in unbroken worship, leading the surviving believers in righteousness until his peaceful demise.\n\n"
                            + "<b>Migration and Final Resting Place:</b>\n"
                            + "According to classical historians and commentators:\n"
                            + "• <b>Hadhramaut (Yemen):</b> The most prominent historical tradition holds that he lived and was buried in the valley of Hadhramaut, at a sanctuary historically known as \"Qabr an-Nabi Hud\".\n"
                            + "• <b>Damascus (Syria) / Makkah:</b> Alternative minor historical narrations also exist, though Hadhramaut remains the widely cited view.\n\n"
                            + "<b>Enduring Legacy of Devotion:</b>\n"
                            + "He remained an unwavering exemplar of patience, humble servitude, and pure Tawheed, counseling his followers to remain steadfast upon the covenant of Allah until his last breath."
            ));
        } else if (prophetId == 5) {
            // ==========================================
            // হযরত সালেহ (আলাইহি সালাম) - Prophet Saleh (AS) (8 Topics)
            // ==========================================

            // 1. জন্ম ও পরিবার
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "জন্ম ও পরিবার",
                    "Birth and Family",
                    "সালেহ (আলাইহি সালাম) ছিলেন আল্লাহ্র একজন নবী, যিনি আদ জাতির পরবর্তী একটি জাতি, সামুদ জাতির মধ্যে প্রেরিত হয়েছিলেন। তিনি তাদের কাছে তাওহীদ (এক আল্লাহ্র ইবাদত) এবং আল্লাহ্র পথে ফিরে আসার আহ্বান জানিয়েছিলেন।\n\n"
                            + "<b>সালেহ (আলাইহি সালাম) এর জন্ম ও পরিবার:</b>\n\n"
                            + "সালেহ (আলাইহি সালাম) সামুদ জাতির মধ্যে জন্মগ্রহণ করেছিলেন। সামুদ জাতি ছিল আরব উপদ্বীপের উত্তর-পশ্চিমাঞ্চলের এক শক্তিশালী এবং সমৃদ্ধ জাতি, যারা পাথরের পর্বত কেটে ঘর তৈরি করত। তারা আল্লাহ্ প্রদত্ত অনেক নিয়ামতের জন্য কৃতজ্ঞ না হয়ে শিরক এবং পাপাচারে লিপ্ত ছিল।\n\n"
                            + "<b>পরিবার এবং বংশ:</b>\n\n"
                            + "সালেহ (আলাইহি সালাম) সম্পর্কে জানা যায় যে তিনি সামুদ জাতির মধ্যেই সম্মানিত এক পরিবারে জন্মগ্রহণ করেছিলেন। আল্লাহ্ তাআলা তাঁকে নবুওয়তের মর্যাদা দিয়ে তাঁর জাতিকে সতর্ক করার জন্য প্রেরণ করেন। তিনি ছিলেন অত্যন্ত বুদ্ধিমান, ধৈর্যশীল এবং ন্যায়পরায়ণ। তাঁর নাম এবং বংশ পরম্পরা সম্পর্কে সরাসরি কোনো নির্দিষ্ট কুরআন বা হাদিসে বিশদ তথ্য নেই। তবে ঐতিহাসিক বর্ণনা থেকে জানা যায় যে তিনি শাম এবং হিজাজ অঞ্চলে জন্মগ্রহণ করেছিলেন।\n\n"
                            + "<b>কুরআনে সালেহ (আলাইহি সালাম) সম্পর্কে বলা হয়েছে:</b>\n\n"
                            + "وَإِلَىٰ ثَمُودَ أَخَاهُمْ صَالِحًا ۚ قَالَ يَٰقَوْمِ ٱعْبُدُوا۟ ٱللَّهَ مَا لَكُم مِّنْ إِلَٰهٍ غَيْرُهُۥ ۖ قَدْ جَآءَتْكُم بَيِّنَةٌۭ مِّن رَّبِّكُمْ ۖ هَـٰذِهِۦ نَاقَةُ ٱللَّهِ لَكُمْ ءَايَةًۭ فَذَرُوهَا تَأْكُلْ فِىٓ أَرْضِ ٱللَّهِ وَلَا تَمَسُّوهَا بِسُوٓءٍۢ فَيَأْخُذَكُمْ عَذَابٌ أَلِيمٌۭ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর সামুদ জাতির প্রতি তাদের ভাই সালেহকে প্রেরণ করেছিলাম। তিনি বলেছিলেন, ‘হে আমার জাতি! তোমরা আল্লাহর ইবাদত করো। তিনি ছাড়া তোমাদের কোনো উপাস্য নেই। তোমাদের কাছে তোমাদের প্রভুর পক্ষ থেকে এক নিদর্শন এসেছে। এ হলো আল্লাহর উষ্ট্রী; এটি তোমাদের জন্য নিদর্শন। অতএব, এটিকে আল্লাহর জমিনে খেতে দাও এবং এটিকে কোনো কষ্ট দিও না, না হলে তোমাদের যন্ত্রণাদায়ক শাস্তি গ্রাস করবে।\n\n"
                            + "<b>[সূরা আল-আরাফ ৭:৭৩]</b>",
                    "Prophet Saleh (peace be upon him) was a distinguished prophet sent by Allah to the tribe of Thamud, who succeeded the ancient people of 'Ad. He called them to pure monotheism (Tawheed) and urged them to return to the straight path of Allah.\n\n"
                            + "<b>Birth and Family Background:</b>\n"
                            + "Prophet Saleh (AS) was born among the tribe of Thamud, a formidable and prosperous ancient Arabian civilization settled in the northwestern Arabian Peninsula (Al-Hijr). They were renowned for carving magnificent dwellings out of rocky mountains. Despite being bestowed with abundant blessings, they turned away in ingratitude and fell deeply into polytheism and transgression.\n\n"
                            + "<b>Lineage and Character:</b>\n"
                            + "Historical records indicate that Saleh (AS) was born into a highly noble and respected family within Thamud. Known from his youth for exceptional wisdom, patience, integrity, and upright conduct, Allah chosen and elevated him to Prophethood to guide and warn his errant nation.\n\n"
                            + "<b>Quranic Declaration:</b>\n"
                            + "وَإِلَىٰ ثَمُودَ أَخَاهُمْ صَالِحًا ۚ قَالَ يَٰقَوْمِ ٱعْبُدُوا۟ ٱللَّهَ مَا لَكُم مِّنْ إِلَٰهٍ غَيْرُهُۥ ۖ قَدْ جَآءَتْكُم بَيِّنَةٌۭ مِّن رَّبِّكُمْ ۖ هَـٰذِهِۦ نَاقَةُ ٱللَّهِ لَكُمْ ءَايَةًۭ فَذَرُوهَا تَأْكُلْ فِىٓ أَرْضِ ٱللَّهِ وَلَا تَمَسُّوهَا بِسُوٓءٍۢ فَيَأْخُذَكُمْ عَذَابٌ أَلِيمٌۭ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And to Thamud [We sent] their brother Salih. He said, 'O my people, worship Allah; you have no deity other than Him. There has come to you clear evidence from your Lord. This is the she-camel of Allah [sent] to you as a sign. So leave her to eat within Allah's earth and do not touch her with harm, lest there seize you a painful punishment.'\"\n"
                            + "<b>[Surah Al-A'raf 7:73]</b>"
            ));

            // 2. সামুদ জাতি ও তাদের জীবনযাত্রা
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "সামুদ জাতি ও তাদের জীবনযাত্রা",
                    "The Tribe of Thamud and Their Way of Life",
                    "সামুদ জাতি (ثمود) ছিল আরব উপদ্বীপের একটি প্রাচীন ও সমৃদ্ধিশালী জাতি, যারা নূহ (আলাইহি সালাম) এবং আদ জাতির পরে এসেছিল। তারা ছিল মূলত আদ জাতির বংশধর এবং তাদের বসবাস ছিল হিজর অঞ্চলে (বর্তমান সৌদি আরবের উত্তর-পশ্চিমে আল-উলা অঞ্চল)। আল্লাহ্ তাদের অসাধারণ ক্ষমতা এবং সম্পদ দান করেছিলেন, কিন্তু তারা আল্লাহ্র একত্ববাদ অস্বীকার করে শিরক ও পাপাচারে লিপ্ত হয়েছিল। সামুদ জাতির জীবনযাত্রা অত্যন্ত উন্নত এবং সমৃদ্ধ ছিল। তাদের জীবনযাত্রার গুরুত্বপূর্ণ বৈশিষ্ট্যগুলো ছিল নিম্নরূপ:\n\n"
                            + "<b>বসতি ও স্থাপত্য:</b>\n\n"
                            + "সামুদ জাতি পর্বত কেটে তাদের বসতি তৈরি করত। আল্লাহ্ তাদের যে অসাধারণ কারিগরি দক্ষতা দিয়েছিলেন, তা দিয়ে তারা পাথর এবং পাহাড় খোদাই করে চমৎকার স্থাপত্য নির্মাণ করত।\n"
                            + "তাদের নির্মিত ঘরগুলো ছিল শক্তিশালী এবং সুরক্ষিত, যা প্রাকৃতিক দুর্যোগ থেকে তাদের রক্ষা করত। এ বিষয়ে কুরআনে উল্লেখ রয়েছে:\n"
                            + "وَتَنۡحِتُونَ مِنَ ٱلۡجِبَالِ بُيُوتًا فَـٰرِهِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর তোমরা আনন্দের সাথে পর্বত কেটে ঘর তৈরি কর।\n\n"
                            + "<b>[সূরা আশ-শু'আরা, ২৬:১৪৯]</b>\n\n"
                            + "<b>কৃষি ও চাষাবাদ:</b>\n\n"
                            + "সামুদ জাতি অত্যন্ত উর্বর জমির অধিকারী ছিল এবং তারা কৃষি ও চাষাবাদে দক্ষ ছিল। তারা পানি সরবরাহের জন্য কূপ এবং জলাশয় তৈরি করত।\n"
                            + "তাদের ফসল, ফলমূল, এবং পশুপালন তাদের অর্থনৈতিক ভিত্তি গড়ে তুলেছিল。\n\n"
                            + "<b>অর্থনীতি ও সম্পদ:</b>\n\n"
                            + "তারা খুবই ধনী এবং সমৃদ্ধ ছিল। তাদের অর্থনীতি প্রধানত কৃষি, স্থাপত্য, এবং পশুপালনের ওপর ভিত্তি করে গড়ে উঠেছিল।\n"
                            + "তাদের অভিজাত জীবনযাত্রা এবং ঐশ্বর্যের কারণে তারা অহংকারী হয়ে উঠেছিল এবং আল্লাহ্র আনুগত্য থেকে দূরে সরে গিয়েছিল।\n\n"
                            + "<b>ধর্মীয় অবস্থা:</b>\n\n"
                            + "সামুদ জাতি আল্লাহ্র উপাসনা ত্যাগ করে মূর্তি পূজায় লিপ্ত হয়েছিল। তারা নিজেদের শক্তি এবং সম্পদ নিয়ে গর্ব করত এবং ভাবত যে তারা কখনো ধ্বংস হবে না। আল্লাহ্ তাআলা তাদের এই অবাধ্যতার জন্য সালেহ (আলাইহি সালাম) কে নবী হিসেবে পাঠান, যিনি তাদের তাওহীদের দাওয়াত দেন এবং তাদের আল্লাহ্র পথে ফিরে আসার জন্য সতর্ক করেন।",
                    "The tribe of Thamud was an ancient, highly prosperous Arabian nation that arose after the peoples of Nuh and 'Ad. Descendants of 'Ad, they settled in the rocky valley of Al-Hijr (modern-day Mada'in Salih / Al-Ula in northwestern Saudi Arabia). Allah endowed them with immense physical prowess and natural riches, yet they descended into rampant idolatry, arrogance, and transgression.\n\n"
                            + "<b>Architecture and Mountain Dwellings:</b>\n"
                            + "Thamud possessed unmatched architectural and stone-carving skills, hewing magnificent, secure palaces and mansions directly into solid rocky cliffs:\n"
                            + "وَتَنۡحِتُونَ مِنَ ٱلۡجِبَالِ بُيُوتًا فَـٰرِهِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And you carve out of the mountains, homes with great skill and joy.\"\n"
                            + "<b>[Surah Ash-Shu'ara 26:149]</b>\n\n"
                            + "<b>Agriculture and Natural Abundance:</b>\n"
                            + "Blessed with fertile soil, freshwater springs, lush gardens, and abundant date-palm orchards, they developed sophisticated irrigation systems, prosperous agriculture, and livestock herds.\n\n"
                            + "<b>Wealth, Economy, and Arrogance:</b>\n"
                            + "Their flourishing economy fueled a luxurious, aristocratic lifestyle. However, extreme material affluence made them haughty, boastful, and self-sufficient, believing their mountain homes would grant them eternal invulnerability.\n\n"
                            + "<b>Spiritual State and Idolatry:</b>\n"
                            + "Abandoning the monotheistic teachings of past prophets, they indulged in idol worship, moral decay, and oppression. In His mercy, Allah raised Prophet Saleh (AS) from within their noble ranks to guide them back to Tawheed."
            ));

            // 3. নবুওয়াত প্রাপ্তি
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "নবুওয়াত প্রাপ্তি",
                    "Attainment of Prophethood",
                    "সালেহ (আলাইহি সালাম) ছিলেন আল্লাহ্র প্রেরিত একজন নবী, যাঁকে আল্লাহ্ সামুদ জাতির প্রতি পাঠিয়েছিলেন। তাঁর নবুওয়াত প্রাপ্তির ঘটনা এবং আল্লাহ্র কাছ থেকে দায়িত্বপ্রাপ্ত হওয়ার বিষয়টি কুরআন ও হাদিসে বর্ণিত হয়েছে। তবে নবুওয়াত প্রাপ্তির সুনির্দিষ্ট সময় বা পদ্ধতির বিস্তারিত তথ্য কুরআনে সরাসরি উল্লেখ নেই। তবুও, কুরআন থেকে যা জানা যায় তা হলো:\n\n"
                            + "<b>সামুদ জাতির অবস্থার পরিপ্রেক্ষিতে নবুওয়াত প্রাপ্তি:</b>\n\n"
                            + "সামুদ জাতি ছিল সমৃদ্ধ এবং শক্তিশালী। তারা পর্বত কেটে ঘর বানিয়ে বসবাস করত এবং প্রচুর সম্পদের অধিকারী ছিল। কিন্তু তারা শিরক এবং পাপাচারে লিপ্ত হয়ে পড়েছিল। তারা আল্লাহ্র প্রতি বিশ্বাস হারিয়ে মূর্তি পূজা করত। আল্লাহ্ তাদের মধ্যে থেকে সালেহ (আলাইহি সালাম) কে নবী হিসেবে প্রেরণ করেন। তিনি তাদের এক আল্লাহ্র ইবাদত করতে বলেন এবং তাদের অবাধ্যতা ও শিরকের পথ ছাড়ার আহ্বান জানান। আল্লাহ্র আদেশে সালেহ (আলাইহি সালাম) তাদের হেদায়েত করার জন্য কঠোর পরিশ্রম করেন।\n\n"
                            + "وَإِلَىٰ ثَمُودَ أَخَاهُمۡ صَٰلِحٗاۚ قَالَ يَٰقَوۡمِ ٱعۡبُدُواْ ٱللَّهَ مَا لَكُم مِّنۡ إِلَٰهٍ غَيۡরُهُۥۖ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর থামুদ জাতির কাছে তাদের ভাই সালেহকে পাঠিয়েছিলাম। তিনি বললেন, 'হে আমার জাতি! তোমরা আল্লাহর ইবাদত করো। তিনি ছাড়া তোমাদের কোনো উপাস্য নেই।\n\n"
                            + "<b>[সূরা হুদ, ১১:৬১]</b>\n\n"
                            + "<b>আল্লাহ্র পক্ষ থেকে দায়িত্ব প্রদান:</b>\n\n"
                            + "সালেহ (আলাইহি সালাম) নবুওয়াত লাভের পর আল্লাহ্র নির্দেশে সামুদ জাতির কাছে দাওয়াত দিতে শুরু করেন। তিনি তাদের একত্ববাদ (তাওহীদ) এর প্রতি আহ্বান করেন এবং তাদেরকে আল্লাহ্র আযাব থেকে সতর্ক করেন। তিনি তাদেরকে বলেন:\n\n"
                            + "يَٰقَوۡمِ ٱعۡبُدُواْ ٱللَّهَ مَا لَكُم مِّنۡ إِلَٰهٍ غَيۡরُهُۥۖ هُوَ أَنشَأَكُم مِّنَ ٱلۡأَرۡضِ وَٱسۡتَعۡمَرَكُمۡ فِيهَا فَٱسۡتَغۡفِرُوهُ ثُمَّ تُوبُوٓاْ إِلَيۡهِۚ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমার জাতি! তোমরা আল্লাহর ইবাদত করো। তিনি ছাড়া তোমাদের কোনো উপাস্য নেই। তিনি তোমাদেরকে ভূমি থেকে সৃষ্টি করেছেন এবং সেখানে তোমাদের স্থাপন করেছেন। সুতরাং, তোমরা তাঁর কাছে ক্ষমা প্রার্থনা করো এবং তাঁর প্রতি ফিরে আসো।\n\n"
                            + "<b>[সূরা হুদ, ১১:৬১]</b>\n\n"
                            + "<b>নবুওয়াতের সাথে নিদর্শন:</b>\n\n"
                            + "সালেহ (আলাইহি সালাম) এর নবুওয়াত প্রমাণ করার জন্য আল্লাহ্ একটি বিশেষ নিদর্শন দেন। সামুদ জাতির অনুরোধে আল্লাহ্ তাদের জন্য একটি আশ্চর্যজনক উটনী পাঠান, যা \"আল্লাহর উটনী\" (নাফাতুল্লাহ, ناقة الله) নামে পরিচিত। এটি ছিল একটি পরীক্ষা এবং আল্লাহ্র কুদরতের স্পষ্ট নিদর্শন। সালেহ (আলাইহি সালাম) তাদের নির্দেশ দেন এই উটনীকে কষ্ট না দেওয়ার জন্য:\n\n"
                            + "هَـٰذِهِۦ نَاقَةُ ٱللَّهِ لَكُمۡ ءَايَةٗ فَذَرُوهَا تَأۡكُلۡ فِيٓ أَرۡضِ ٱللَّهِ وَلَا تَمَسُّوهَا بِسُوٓءٖ فَيَأۡخُذَكُمۡ عَذَابٌ أَلِيمٞ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "এটি আল্লাহর উটনী, তোমাদের জন্য নিদর্শন। অতএব, এটিকে আল্লাহর জমিনে চরে বেড়াতে দাও এবং এটিকে কোনো কষ্ট দিও না, না হলে যন্ত্রণাদায়ক শাস্তি তোমাদের গ্রাস করবে।\n\n"
                            + "<b>[সূরা হুদ, ১১:৬৪]</b>\n\n"
                            + "<b>সালেহ (আলাইহি সালাম) এর দাওয়াত ও প্রতিক্রিয়া:</b>\n\n"
                            + "সালেহ (আলাইহি সালাম) তাদের তাওহীদের প্রতি আহ্বান করলেও অধিকাংশ মানুষ তাঁকে প্রত্যাখ্যান করে এবং কুফরির পথে অটল থাকে। তারা আল্লাহর উটনীকে হত্যা করে, যা আল্লাহর প্রতি তাদের বিদ্রোহের চরম প্রকাশ। এর ফলস্বরূপ, আল্লাহ্ তাদের ওপর ভয়াবহ শাস্তি পাঠান।",
                    "Prophet Saleh (AS) was commissioned by Allah as a divine Messenger to the powerful and prosperous people of Thamud. Endowed with divine revelation, he began calling his community back to the sincere worship of Almighty Allah.\n\n"
                            + "<b>Context of Divine Call:</b>\n"
                            + "As Thamud sank into deep polytheism, idolatry, and moral corruption, Allah chose Saleh (AS)—who was universally respected among them—and commanded him to deliver the divine message of Tawheed:\n"
                            + "وَإِلَىٰ ثَمُودَ أَخَاهُمۡ صَٰلِحٗاۚ قَالَ يَٰقَوۡمِ ٱعۡبُدُواْ ٱللَّهَ مَا لَكُم مِّنۡ إِلَٰهٍ غَيۡরُهُۥۖ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And to Thamud [We sent] their brother Salih. He said, 'O my people, worship Allah; you have no deity other than Him.'\"\n"
                            + "<b>[Surah Hud 11:61]</b>\n\n"
                            + "<b>Prophetic Mission and Reminder:</b>\n"
                            + "Saleh (AS) reminded them that their life, strength, and prosperity were all divine gifts from Allah:\n"
                            + "يَٰقَوۡمِ ٱعۡبُدُواْ ٱللَّهَ مَا لَكُم مِّنۡ إِلَٰهٍ غَيۡরُهُۥۖ هُوَ أَنشَأَكُم مِّنَ ٱلۡأَرۡضِ وَٱسۡتَعۡمَرَكُمۡ فِيهَا فَٱسۡتَغۡفِرُوهُ ثُمَّ تُوبُوٓاْ إِلَيۡهِۚ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"He brought you forth from the earth and settled you therein. So ask forgiveness of Him and repent to Him.\"\n"
                            + "<b>[Surah Hud 11:61]</b>\n\n"
                            + "<b>The Miraculous She-Camel:</b>\n"
                            + "When Thamud demanded a decisive physical miracle, Allah caused a colossal, miraculous she-camel (Naqatullah) to emerge from a solid rock as a divine sign and test of obedience:\n"
                            + "هَـٰذِهِۦ نَاقَةُ ٱللَّهِ لَكُمۡ ءَايَةٗ فَذَرُوهَا تَأۡكُلۡ فِيٓ أَرۡضِ ٱللَّهِ وَلَا تَمَسُّوهَا بِسُوٓءٖ فَيَأۡخُذَكُمۡ عَذَابٌ أَلِيمٞ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"This is the she-camel of Allah, a sign for you. So let her graze in Allah's land and do not harm her, or a painful punishment will seize you.\"\n"
                            + "<b>[Surah Hud 11:64]</b>\n\n"
                            + "<b>Rejection by the Elders:</b>\n"
                            + "Despite witnessing this miraculous sign, the arrogant chieftains persisted in defiance, conspiring against the prophet and planning the murder of the sacred she-camel."
            ));

            // 4. উষ্ট্রীর হত্যা এবং শিরকের চূড়ান্ত পর্যায়
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "উষ্ট্রীর হত্যা এবং শিরকের চূড়ান্ত পর্যায়",
                    "Slaughter of the She-Camel and Culmination of Polytheism",
                    "সামুদ জাতি আল্লাহ্র কাছে প্রেরিত সালেহ (আলাইহি সালাম) এর দাওয়াত প্রত্যাখ্যান করেছিল এবং তাঁদের প্রতি প্রদত্ত বিশেষ নিদর্শন \"আল্লাহর উষ্ট্রী\" (নাফাতুল্লাহ, ناقة الله) কে হত্যা করেছিল। এটি ছিল তাদের শিরকের চূড়ান্ত প্রকাশ এবং আল্লাহ্র প্রতি বিদ্রোহের চরম দৃষ্টান্ত। এই ঘটনার বিবরণ কুরআন এবং ইসলামী ঐতিহ্যে বিশদভাবে এসেছে।\n\n"
                            + "<b>উষ্ট্রীর পাঠানোর ঘটনা:</b>\n\n"
                            + "সামুদ জাতি সালেহ (আলাইহি সালাম)-কে চ্যালেঞ্জ করে বলেছিল যে যদি তিনি সত্যিই আল্লাহ্র নবী হন, তাহলে তাদের জন্য কোনো অলৌকিক নিদর্শন আনুন। তারা একটি বিশেষ উষ্ট্রীর দাবি করেছিল। সালেহ (আলাইহি সালাম) আল্লাহ্র কাছে প্রার্থনা করলে আল্লাহ্ তাদের জন্য একটি আশ্চর্যজনক উটনী পাঠান। এই উটনী ছিল একটি অলৌকিক সৃষ্টির নিদর্শন এবং এটি \"আল্লাহর উটনী\" (নাফাতুল্লাহ, ناقة الله) নামে পরিচিত। এটি একদিন জল পান করত, আর পরের দিন পুরো জাতি ওই কূপের পানি ব্যবহার করত। আল্লাহ্ তাদের নির্দেশ দিয়েছিলেন, যেন তারা উটনীকে সম্মান করে এবং তাকে কোনো ধরনের ক্ষতি না করে।\n\n"
                            + "وَيَٰقَوۡمِ هَٰذِهِۦ نَاقَةُ ٱللَّهِ لَكُمۡ ءَايَةٗ فَذَرُوهَا تَأۡكُلۡ فِيٓ أَرۡضِ ٱللَّهِ وَلَا تَمَسُّوهَا بِسُوٓءٖ فَيَأۡخُذَكُمۡ عَذَابٌ قَرِيبٞ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমার জাতি! এ উটনী তোমাদের জন্য আল্লাহ্র নিদর্শন। সুতরাং এটিকে আল্লাহ্র জমিনে চারণ করতে দাও এবং এটিকে কোনো ক্ষতি করো না, নতুবা তোমাদেরকে নিকটবর্তী শাস্তি গ্রাস করবে।\n\n"
                            + "<b>[সূরা হুদ, ১১:৬৪]</b>\n\n"
                            + "<b>উষ্ট্রীর হত্যা:</b>\n\n"
                            + "সামুদ জাতি সালেহ (আলাইহি সালাম)-এর সতর্কবাণী উপেক্ষা করে এবং তাদের বিদ্রোহী আচরণের চূড়ান্ত প্রকাশ ঘটায়। তারা উটনীকে হত্যা করার পরিকল্পনা করে। তাদের মধ্যে সবচেয়ে দুর্বৃত্তরা এই কাজটি করে।\n\n"
                            + "فَعَقَرُواْ ٱلنَّاقَةَ وَعَتَوۡاْ عَنۡ أَمۡরِ رَبِّهِمۡ وَقَالُواْ يَٰصَٰলِحُ ٱئۡتِنَا বِمَا تَعِدُنَآ إِن كُنتَ مِنَ ٱلۡمُرۡسَلِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা উটনীকে হত্যা করল এবং তারা তাদের প্রভুর আদেশ লঙ্ঘন করল। তারা বলল, ‘হে সালেহ! তুমি যদি রাসূল হয়ে থাকো তবে তোমার প্রতিশ্রুত শাস্তি আমাদের কাছে নিয়ে এসো।\n\n"
                            + "<b>[সূরা আল-আ'রাফ, ৭:৭৭]</b>\n\n"
                            + "<b>শিরকের চূড়ান্ত পর্যায়:</b>\n\n"
                            + "উটনী হত্যা করার মাধ্যমে সামুদ জাতি আল্লাহ্র নির্দেশ সরাসরি অমান্য করল। এটি ছিল তাদের শিরকের চরম প্রকাশ। তারা তাদের অহংকার ও বিদ্রোহের কারণে আল্লাহ্র প্রতি আনুগত্য থেকে সম্পূর্ণ দূরে সরে গিয়েছিল।\n\n"
                            + "• তারা আল্লাহ্র প্রেরিত নিদর্শনকে অবজ্ঞা করল।\n"
                            + "• সালেহ (আলাইহি সালাম)-এর বার্তাকে উপহাস করল এবং আল্লাহ্র শাস্তিকে চ্যালেঞ্জ করল।\n"
                            + "• তাদের শিরক এবং আল্লাহ্র প্রতি বিদ্রোহ তাদের ধ্বংসের মূল কারণ হয়ে দাঁড়ায়।",
                    "The rebellious people of Thamud rejected the clear signs of Allah and slaughtered the miraculous she-camel (Naqatullah). This brazen act marked the culmination of their polytheism, defiance, and ultimate doom.\n\n"
                            + "<b>The Miraculous Provision and Water-Sharing:</b>\n"
                            + "When Thamud demanded a specific supernatural sign, Allah produced a pregnant she-camel from a solid rock. A divine covenant stipulated that the camel would drink from the city's spring on designated alternate days, supplying abundant fresh milk in return, while leaving the water to the inhabitants on other days:\n"
                            + "وَيَٰقَوۡمِ هَٰذِهِۦ نَاقَةُ ٱللَّهِ لَكُمۡ ءَايَةٗ فَذَرُوهَا تَأۡكُلۡ فِيٓ أَرۡضِ ٱللَّهِ وَلَا تَمَسُّوهَا بِسُوٓءٖ فَيَأۡخُذَكُمۡ عَذَابٌ قَرِيبٞ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"O my people, this is the she-camel of Allah—a sign for you. Let her graze in Allah's land and cause her no harm, or an imminent punishment will strike you.\"\n"
                            + "<b>[Surah Hud 11:64]</b>\n\n"
                            + "<b>The Plot and Heinous Slaying:</b>\n"
                            + "Conspiring together under the leadership of nine corrupt scoundrels, led by Qudar ibn Salif, they ambushed and brutally slaughtered the sacred she-camel, challenging Prophet Saleh with utter insolence:\n"
                            + "فَعَقَرُواْ ٱلنَّاقَةَ وَعَتَوۡاْ عَنۡ أَمۡরِ رَبِّهِمۡ وَقَالُواْ يَٰصَٰলِحُ ٱئۡتِنَا بِمَا تَعِدُنَآ إِن كُنتَ مِنَ ٱلۡمُرۡسَلِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"So they hamstrung the she-camel and were insolent toward the command of their Lord and said, 'O Salih, bring us what you promise us, if you should be of the messengers.'\"\n"
                            + "<b>[Surah Al-A'raf 7:77]</b>\n\n"
                            + "<b>The Culmination of Iniquity:</b>\n"
                            + "• Blatant desecration of Allah's divine sign.\n"
                            + "• Direct, insolent mocking of the prophet's warnings.\n"
                            + "• Arrogant provocation inviting Allah's divine wrath, sealing their complete destruction."
            ));

            // 5. তাওহীদের দাওয়াত
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "তাওহীদের দাওয়াত",
                    "Call to Tawheed and Prophetic Warnings",
                    "হযরত ছালেহ (আঃ)-এর ভবিষ্যদ্বাণী অনুযায়ী বৃহষ্পতিবার ভোরে অবিশ্বাসী কওমের সকলের মুখমণ্ডল গভীর হলুদ বর্ণ ধারণ করল। কিন্তু তারা ঈমান আনল না বা তওবা করল না। বরং উল্টা হযরত ছালেহ (আঃ)-এর উপর চটে গেল ও তাঁকে হত্যা করার জন্য খুঁজতে লাগল। দ্বিতীয় দিন সবার মুখমণ্ডল লাল বর্ণ ও তৃতীয় দিন ঘোর কৃষ্ণবর্ণ হয়ে গেল। তখন সবই নিরাশ হয়ে গযবের জন্য অপেক্ষা করতে লাগল। চতুর্থ দিন রবিবার সকালে সবাই মৃত্যুর জন্য প্রস্ত্ততি নিয়ে সুগন্ধি মেখে অপেক্ষা করতে থাকে।\n\n"
                            + "<b>[ইবনু কাছীর, সূরা আ‘রাফ ৭৩-৭৮]</b>\n\n"
                            + "এমতাবস্থায় ভীষণ ভূমিকম্প শুরু হ’ল এবং উপর থেকে বিকট ও ভয়াবহ এক গর্জন শোনা গেল। ফলে সবাই যার যার স্থানে একযোগে অধোমুখী হয়ে ভূতলশায়ী হ’ল\n\n"
                            + "<b>[আ‘রাফ ৭/৭৮; হূদ ১১/৬৭-৬৮]</b>\n\n"
                            + "এবং ধ্বংসপ্রাপ্ত হ’ল এমনভাবে, যেন তারা কোনদিন সেখানে ছিল না’। অন্য আয়াতে এসেছে যে, ‘আমরা তাদের প্রতি একটিমাত্র নিনাদ পাঠিয়েছিলাম। তাতেই তারা শুষ্ক খড়কুটোর মত হয়ে গেল’ <b>[ক্বামার ৫৪/৩১]</b>\n\n"
                            + "কোন কোন হাদীছে এসেছে রাসূলুল্লাহ (ছাঃ) বলেন, ছামূদ জাতির উপরে আপতিত গযব থেকে ‘আবু রেগাল’ নামক জনৈক অবিশ্বাসী নেতা ঐ সময় মক্কায় থাকার কারণে বেঁচে গিয়েছিল। কিন্তু হারাম শরীফ থেকে বেরোবার সাথে সাথে সেও গযবে পতিত হয়। রাসূলুল্লাহ (ছাঃ) ছাহাবায়ে কেরামকে মক্কার বাইরে আবু রেগালের উক্ত কবরের চিহ্ন দেখান এবং বলেন যে, তার সাথে একটা স্বর্ণের ছড়িও দাফন হয়ে গিয়েছিল। তখন কবর খনন করে তারা ছড়িটি উদ্ধার করেন। উক্ত রেওয়ায়াতে একথাও বলা হয়েছে যে, ত্বায়েফের প্রসিদ্ধ ছাক্বীফ গোত্র উক্ত আবু রেগালের বংশধর। তবে হাদীছটি যঈফ।\n\n"
                            + "<b>[ইবনু কাছীর, আ‘রাফ ৭৮; আলবানী, যঈফ আবুদাঊদ, ‘কবর উৎপাটন’ অনুচ্ছেদ; যঈফাহ হা/৪৭৩৬]</b>\n\n"
                            + "অন্য হাদীছে এসেছে রাসূলুল্লাহ (ছাঃ) বলেন যে, ‘ছাক্বীফ গোত্রে একজন মিথ্যাবাদী (ভন্ড নবী) ও একজন রক্ত পিপাসুর জন্ম হবে।\n\n"
                            + "<b>[মুসলিম, মিশকাত হা/৫৯৯৪ ‘কুরায়েশ-এর মর্যাদা’ অনুচ্ছেদ]</b>\n\n"
                            + "রাসূলের এ ভবিষ্যদ্বাণী বাস্তবায়িত হয় এবং এই বংশে মিথ্যা নবী মোখতার ছাক্বাফী এবং রক্তপিপাসু কসাই ইরাকের উমাইয়া গবর্ণর হাজ্জাজ বিন ইউসুফের জন্ম হয়। কওমে ছামূদ-এর অভিশপ্ত বংশের রক্তধারার কু-প্রভাব হওয়াটাও এতে বিচিত্র নয়। অন্য এক হাদীছে বর্ণিত হয়েছে যে, ৯ম হিজরীতে তাবূক যুদ্ধের সময় রাসূলুল্লাহ (ছাঃ) সিরিয়া ও হেজাযের মধ্যবর্তী ‘হিজ্র’ নামক সে স্থানটি অতিক্রম করেন, যেখানে ছামূদ জাতির উপরে গযব নাযিল হয়েছিল। তিনি ছাহাবায়ে কেরামকে নির্দেশ দেন, কেউ যেন ঐ গযব বিধ্বস্ত এলাকায় প্রবেশ না করে এবং ওখানকার কূয়ার পানি ব্যবহার না করে’।\n\n"
                            + "<b>[বুখারী হা/৪৩৩, মুসলিম, আহমাদ, ইবনু কাছীর, সূরা আ‘রাফ ৭৩]</b>\n\n"
                            + "এসব আযাব-বিধ্বস্ত এলাকাগুলিকে আল্লাহ তা‘আলা ভবিষ্যৎ মানবজাতির জন্য শিক্ষাস্থল হিসাবে সংরক্ষিত রেখেছেন, যাতে তারা উপদেশ হাছিল করতে পারে এবং নিজেদেরকে আল্লাহর অবাধ্যতা হ’তে বিরত রাখে। আরবরা তাদের ব্যবসায়িক সফরে নিয়মিত সিরিয়া যাতায়াতের পথে এইসব ধ্বংসস্ত্তপ গুলি প্রত্যক্ষ করত। অথচ তাদের অধিকাংশ তা থেকে শিক্ষা গ্রহণ করেনি এবং শেষনবীর উপরে বিশ্বাস স্থাপন করেনি। যদিও পরবর্তীতে সব এলাকাই ‘মুসলিম’ এলাকায় পরিণত হয়ে গেছে।\n\n"
                            + "আল্লাহ বলেন,\n\n"
                            + "وَكَمْ أَهْلَكْنَا مِنْ قَرْيَةٍ بَطِرَتْ مَعِيْشَتَهَا فَتِلْكَ مَسَاكِنُهُمْ لَمْ تُسْكَن مِّن بَعْدِهِمْ إِلاَّ قَلِيلاً وَكُنَّا نَحْنُ الْوَارِثِيْنَ، وَمَا كَانَ رَبُّكَ مُهْلِكَ الْقُرَى حَتَّى يَبْعَثَ فِيْ أُمِّهَا رَسُولاً يَتْلُو عَلَيْهِمْ آيَاتِنَا وَمَا كُنَّا مُهْلِكِي الْقُرَى إِلاَّ وَأَهْلُهَا ظَالِمُوْنَ-\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আমরা অনেক জনপদ ধ্বংস করেছি; যেসবের অধিবাসীরা তাদের বিলাসী জীবন যাপনে মত্ত ছিল। তাদের এসব আবাসস্থলে তাদের পরে মানুষ খুব সামান্যই বসবাস করেছে। অবশেষে আমরাই এসবের মালিক রয়েছি’। ‘আপনার পালনকর্তা জনপদ সমূহকে ধ্বংস করেন না, যে পর্যন্ত না তার কেন্দ্রস্থলে রাসূল প্রেরণ করেন। যিনি তাদের কাছে আমাদের আয়াত সমূহ পাঠ করেন। আর আমরা জনপদ সমূহকে তখনই ধ্বংস করি, যখন তার বাসিন্দারা (অর্থাৎ নেতারা) যুলুম করে’\n\n"
                            + "<b>[সূরা আল-ক্বাছাছ ২৮:৫৮-৫৯]</b>\n\n"
                            + "উল্লেখ্য যে, উপরে বর্ণিত কাহিনীর প্রধান বিষয়গুলি পবিত্র কুরআনের ২২টি সূরায় ৮৭টি আয়াতে এবং কিছু অংশ হাদীছে বর্ণিত হয়েছে। কিছু অংশ এমনও রয়েছে যা তাফসীরবিদগণ বিভিন্ন ইস্রাঈলী বর্ণনা থেকে সংগ্রহ করেছেন, যা সত্য ও মিথ্যা দুই-ই হ’তে পারে। কিন্তু সেগুলি কোন গুরুত্বপূর্ণ বিষয় নয় এবং সেগুলির উপরে ঘটনার প্রমাণ নির্ভরশীল নয়।",
                    "Following the slaughter of the she-camel, Prophet Saleh (AS) warned Thamud that divine retribution would strike within three days. On Thursday morning, their faces turned shockingly yellow, on Friday red, and on Saturday pitch-black. Paralyzed with despair, on Sunday morning they prepared themselves in shroud-like garments awaiting impending doom.\n"
                            + "<b>[Ibn Kathir, Tafsir Surah Al-A'raf 73-78]</b>\n\n"
                            + "Suddenly, a catastrophic earthquake accompanied by a deafening, terrifying blast from the heavens struck them, flattening every single transgressor facedown in their ruins:\n"
                            + "<b>[Surah Al-A'raf 7:78; Surah Hud 11:67-68]</b>\n\n"
                            + "\"Indeed, We sent upon them one shout, and they became like the dry twigs used by a fence builder.\"\n"
                            + "<b>[Surah Al-Qamar 54:31]</b>\n\n"
                            + "<b>The Prohibitions at Al-Hijr:</b>\n"
                            + "In the 9th year of Hijrah during the Tabuk Expedition, the Messenger of Allah (ﷺ) passed through the ruins of Al-Hijr. He commanded the companions not to enter the ruins except in weeping and contemplation of Allah's wrath, and forbade drinking from its wells or kneading dough with its water.\n"
                            + "<b>[Sahih al-Bukhari: 433; Sahih Muslim; Musnad Ahmad]</b>\n\n"
                            + "<b>Divine Admonition in the Quran:</b>\n"
                            + "وَكَمْ أَهْلَكْنَا مِنْ قَرْيَةٍ بَطِرَتْ مَعِيْشَتَهَا فَتِلْكَ مَسَاكِنُهُمْ لَمْ تُسْكَن مِّن بَعْدِهِمْ إِلاَّ قَلِيلاً وَكُنَّا نَحْنُ الْوَارِثِيْنَ، وَمَا كَانَ رَبُّكَ مُهْلِكَ الْقُرَى حَتَّى يَبْعَثَ فِيْ أُمِّهَا رَسُولاً يَتْلُو عَلَيْهِمْ آيَاتِنَا وَمَا كُنَّا مُهْلِكِي الْقُرَى إِلاَّ وَأَهْلُهَا ظَالِمُوْنَ-\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And how many a city have We destroyed that was insolent in its way of living, and those are their dwellings which have not been inhabited after them except a little. And it is We who were the inheritors. And never would your Lord have destroyed the cities until He had sent to their mother [i.e., principal city] a messenger reciting to them Our verses. And We would not destroy the cities unless its people were wrongdoers.\"\n"
                            + "<b>[Surah Al-Qasas 28:58-59]</b>"
            ));

            // 6. আল্লাহর শাস্তি
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "আল্লাহর শাস্তি",
                    "Allah's Punishment",
                    "সামুদ জাতি ছিল সমৃদ্ধ ও শক্তিশালী। আল্লাহ্ তাদের প্রতি নবী সালেহ (আলাইহি সালাম)-কে প্রেরণ করেছিলেন, যিনি তাদের তাওহীদের দাওয়াত দেন এবং শিরক ও পাপাচার থেকে ফিরে আসার আহ্বান জানান। কিন্তু তারা সালেহ (আলাইহি সালাম)-এর দাওয়াত প্রত্যাখ্যান করে এবং আল্লাহর নিদর্শন “উষ্ট্রী” (নাকাতুল্লাহ, نَاقَةُ الله) কে হত্যা করে। এর ফলে আল্লাহ্ তাদের ওপর ভয়াবহ শাস্তি নাজিল করেন। এই শাস্তি ছিল তাদের বিদ্রোহ, অহংকার এবং আল্লাহর আদেশ অমান্য করার ফলাফল।\n\n"
                            + "<b>কুরআনে শাস্তির বর্ণনা, উষ্ট্রী হত্যা এবং বিদ্রোহ:</b>\n\n"
                            + "সামুদ জাতি আল্লাহর নিদর্শন “উষ্ট্রী” কে হত্যা করেছিল। তারা সালেহ (আলাইহি সালাম)-কে উপহাস করত এবং আল্লাহর শাস্তিকে অবজ্ঞা করত। তাদের এই বিদ্রোহ কুরআনে উল্লেখ করা হয়েছে:\n\n"
                            + "فَعَقَرُواْ ٱلنَّاقَةَ وَعَتَوۡاْ عَنۡ أَمۡرِ رَبِّهِمۡ وَقَالُواْ يَٰصَٰلِحُ ٱئۡتِنَا بِمَا تَعِدُنَآ إِن كُنتَ مِنَ ٱلۡمُرۡسَلِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা উটনীকে হত্যা করল এবং তারা তাদের প্রভুর আদেশ লঙ্ঘন করল। তারা বলল, ‘হে সালেহ! তুমি যদি রাসূল হয়ে থাকো তবে তোমার প্রতিশ্রুত শাস্তি আমাদের কাছে নিয়ে এসো।\n\n"
                            + "<b>[সূরা আল-আ'রাফ, ৭:৭৭]</b>\n\n"
                            + "<b>শাস্তি নাজিলের আগে সতর্কবার্তা:</b>\n\n"
                            + "সালেহ (আলাইহি সালাম) তাঁদের সতর্ক করে দিয়েছিলেন যে উষ্ট্রী হত্যার পর তাদের জন্য তিন দিনের সময়সীমা নির্ধারিত হয়েছে। তিন দিনের মধ্যে শাস্তি তাদের ধ্বংস করবে:\n\n"
                            + "فَعَقَرُوهَا فَقَالَ تَمَتَّعُواْ فِي دَارِكُمۡ ثَلَٰثَةَ أَيَّامٖۖ ذَٰلِكَ وَعۡدٌ غَيۡرُ مَكۡذُوبٖ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা উটনীকে হত্যা করল। তিনি (সালেহ) বললেন, ‘তোমরা তোমাদের ঘরে তিন দিন উপভোগ কর। এটা এমন প্রতিশ্রুতি যা মিথ্যা নয়।\n\n"
                            + "<b>[সূরা হুদ, ১১:৬৫]</b>\n\n"
                            + "<b>শাস্তি: বজ্রধ্বনি ও ভূমিকম্প:</b>\n\n"
                            + "তিন দিনের সময়সীমা শেষে আল্লাহ্ তাঁদের ওপর ভয়াবহ শাস্তি প্রেরণ করেন। এটি ছিল বজ্রধ্বনি (الصيحة) ও ভূমিকম্পের (رجفة) সম্মিলিত আঘাত, যা তাদের পুরোপুরি ধ্বংস করে দেয়।\n\n"
                            + "<b>কুরআনে শাস্তির বর্ণনা:</b>\n\n"
                            + "فَأَخَذَتۡهُمُ ٱلصَّيۡحَةُ فَأَصۡبَحُواْ فِي دِيَٰرِهِمۡ جَٰثِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর তাদের ওপর বজ্রধ্বনি পতিত হলো, এবং তারা তাদের গৃহে উপুড় হয়ে পড়ে রইল।\n\n"
                            + "<b>[সূরা হুদ, ১১:৬৭]</b>\n\n"
                            + "فَأَخَذَتۡهُمُ ٱلرَّجۡفَةُ فَأَصۡبَحُواْ فِي دِيَٰرِهِمۡ جَٰثِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর তাদের ভূমিকম্প গ্রাস করল, এবং তারা তাদের গৃহে উপুড় হয়ে পড়ে রইল।\n\n"
                            + "<b>[সূরা আল-আ'রাফ, ৭:৭৮]</b>\n\n"
                            + "<b>শাস্তির কারণ:</b>\n\n"
                            + "• <b>শিরক:</b> তারা আল্লাহ্র একত্বে বিশ্বাস স্থাপন করতে অস্বীকার করেছিল।\n"
                            + "• <b>অহংকার:</b> তারা নিজেদের শক্তি ও সম্পদের ওপর গর্বিত ছিল এবং আল্লাহর প্রতি আনুগত্য অস্বীকার করেছিল।\n"
                            + "• <b>নবীর অবজ্ঞা:</b> তারা সালেহ (আলাইহি সালাম)-এর দাওয়াত প্রত্যাখ্যান করেছিল এবং তাঁকে উপহাস করত।\n"
                            + "• <b>উষ্ট্রী হত্যা:</b> আল্লাহর নিদর্শন \"উষ্ট্রী\" কে হত্যা করেছিল, যা ছিল সরাসরি আল্লাহ্র নির্দেশ লঙ্ঘন।",
                    "Thamud was a mighty and prosperous nation. Allah sent Prophet Saleh (AS) to invite them to monotheism and renounce idol worship. When they defiantly slaughtered the sacred she-camel, Allah unleashed a devastating, multifaceted punishment upon them.\n\n"
                            + "<b>The Final Warning and Three-Day Reprieve:</b>\n"
                            + "Following the slaughter of the she-camel, Prophet Saleh (AS) delivered the ultimatum:\n"
                            + "فَعَقَرُوهَا فَقَالَ تَمَتَّعُواْ فِي دَارِكُمۡ ثَلَٰثَةَ أَيَّامٖۖ ذَٰلِكَ وَعۡدٌ غَيۡرُ مَكۡذُوبٖ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"Enjoy yourselves in your homes for three days. That is a promise that will not be untrue.\"\n"
                            + "<b>[Surah Hud 11:65]</b>\n\n"
                            + "<b>The Combined Cataclysm of the Blast (As-Sayhah) and Earthquake (Ar-Rajfah):</b>\n"
                            + "At dawn of the fourth day, a colossal blast from the heavens accompanied by a violent earthquake shattered their hearts and flattened their fortified mountain homes:\n"
                            + "فَأَخَذَتۡهُمُ ٱلصَّيۡحَةُ فَأَصۡبَحُواْ فِي دِيَٰرِهِمۡ جَٰثِمِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And the shriek seized those who had wronged, and they became within their homes [corpses] fallen prone.\"\n"
                            + "<b>[Surah Hud 11:67]</b>\n\n"
                            + "فَأَخَذَتۡهُمُ ٱلرَّجۡفَةُ فَأَصۡبَحُواْ فِي دِيَٰرِهِمۡ جَٰثِمِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"So the earthquake seized them, and they became within their home [corpses] fallen prone.\"\n"
                            + "<b>[Surah Al-A'raf 7:78]</b>\n\n"
                            + "<b>Core Causes of Their Destruction:</b>\n"
                            + "• <b>Polytheism (Shirk):</b> Obstinate rejection of Tawheed and persistent idol worship.\n"
                            + "• <b>Arrogance:</b> Overwhelming pride in their physical strength and architectural mastery.\n"
                            + "• <b>Mockery of the Prophet:</b> Derision toward Saleh (AS) and provoking divine wrath.\n"
                            + "• <b>Slaughter of the She-Camel:</b> Flagrant violation of Allah's sacred covenant."
            ));

            // 7. ঈমানদারদের রক্ষা
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "ঈমানদারদের রক্ষা",
                    "Protection and Deliverance of the Believers",
                    "যখন সামুদ জাতি আল্লাহর দাওয়াত প্রত্যাখ্যান করেছিল এবং শিরক ও পাপাচারে লিপ্ত ছিল, তখন আল্লাহ্ তাদের ওপর ভয়াবহ শাস্তি নাজিল করেন। তবে, আল্লাহ্র সুন্নত বা নীতি অনুযায়ী তিনি সর্বদা ঈমানদারদের রক্ষা করেন। সালেহ (আলাইহি সালাম) এবং তাঁর প্রতি ঈমান আনা মুষ্টিমেয় অনুসারী এই শাস্তি থেকে মুক্তি পেয়েছিলেন।\n\n"
                            + "<b>ঈমানদারদের প্রতি আল্লাহ্র প্রতিশ্রুতি:</b>\n\n"
                            + "সালেহ (আলাইহি সালাম) তাঁর অনুসারীদের আশ্বস্ত করেছিলেন যে যারা আল্লাহ্র প্রতি বিশ্বাস রাখে, তাদের শাস্তি স্পর্শ করবে না। ঈমানদারদের প্রতি আল্লাহ্র করুণা এবং রক্ষা সম্পর্কে কুরআনে বলা হয়েছে:\n\n"
                            + "فَلَمَّا جَآءَ أَمۡرُنَا نَجَّيۡنَا صَٰلِحٗا وَٱلَّذِينَ ءَامَنُواْ مَعَهُۥ بِرَحۡمَةٖ مِّنَّا وَمِنۡ خِزۡيِ يَوۡمِئِذٍۚ إِنَّ رَبَّكَ هُوَ ٱلۡقَوِيُّ ٱلۡعَزِيزُ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর যখন আমাদের আদেশ এল, আমরা সালেহ ও তাঁর সাথে যারা ঈমান এনেছিল তাদেরকে আমাদের রহমতে রক্ষা করলাম এবং সেদিনের অপমান থেকে বাঁচালাম। নিশ্চয়ই তোমার প্রভু শক্তিশালী, মহাপরাক্রমশালী।\n\n"
                            + "<b>[সূরা হুদ, ১১:৬৬]</b>\n\n"
                            + "<b>শাস্তির আগে ঈমানদারদের পৃথক করা:</b>\n\n"
                            + "শাস্তি নাজিলের আগে আল্লাহ্ সালেহ (আলাইহি সালাম)-এর অনুসারীদের নিরাপদ স্থানে সরে যেতে নির্দেশ দেন। তাঁরা শাস্তির সাক্ষী হননি। আল্লাহ্ তাঁর রহমতে তাঁদের সুরক্ষিত রেখেছিলেন।\n\n"
                            + "وَلَمَّا جَآءَ أَمۡرُنَا نَجَّيۡنَا ٱلَّذِينَ ءَامَنُواْ وَكَانُواْ يَتَّقُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর যখন আমাদের শাস্তি এল, আমরা তাদেরকে রক্ষা করলাম যারা ঈমান এনেছিল এবং যারা আল্লাহকে ভয় করত।\n\n"
                            + "<b>[সূরা ফুসসিলাত, ৪১:১৮]</b>",
                    "In accordance with the divine sunnah of Allah, whenever catastrophic destruction descended upon a transgressing nation, the faithful messengers and their righteous believers were always spared and granted divine safety.\n\n"
                            + "<b>The Divine Rescue of Saleh (AS) and the Believers:</b>\n"
                            + "Before the calamity struck, Allah commanded Prophet Saleh (AS) and the righteous believers to depart from the doomed valley of Al-Hijr:\n"
                            + "فَلَمَّا جَآءَ أَمۡرُنَا نَجَّيۡنَا صَٰلِحٗا وَٱلَّذِينَ ءَامَنُواْ مَعَهُۥ بِرَحۡمَةٖ مِّنَّا وَمِنۡ خِزۡيِ يَوۡمِئِذٍۚ إِنَّ رَبَّكَ هُوَ ٱلۡقَوِيُّ ٱلۡعَزِيزُ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"So when Our command came, We saved Salih and those who believed with him, by mercy from Us, and [saved them] from the disgrace of that Day. Indeed, it is your Lord who is the Powerful, the Exalted in Might.\"\n"
                            + "<b>[Surah Hud 11:66]</b>\n\n"
                            + "<b>Safety for the God-Fearing:</b>\n"
                            + "وَلَمَّا جَآءَ أَمۡرُنَا نَجَّইۡنَا ٱلَّذِينَ ءَامَنُواْ وَكَانُواْ يَتَّقُونَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And We delivered those who believed and used to fear Allah.\"\n"
                            + "<b>[Surah Fussilat 41:18]</b>\n\n"
                            + "<b>Enduring Lessons of Faith:</b>\n"
                            + "• Divine mercy always envelopes those who maintain steadfast faith and piety (Taqwa).\n"
                            + "• Righteousness and devotion serve as an impenetrable shield against divine tribulations."
            ));

            // 8. সালেহ (আলাইহি সালাম)-এর শেষ জীবন
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "সালেহ (আলাইহি সালাম)-এর শেষ জীবন",
                    "Final Days and Passing of Prophet Saleh (AS)",
                    "সালেহ (আলাইহি সালাম) ছিলেন আল্লাহ্র প্রেরিত একজন মহান নবী, যিনি সামুদ জাতির জন্য তাওহীদের দাওয়াত নিয়ে এসেছিলেন। তাঁর জীবন, দাওয়াত এবং সামুদ জাতির ধ্বংসের ঘটনা কুরআন ও হাদিসে উল্লেখিত আছে। তবে তাঁর শেষ জীবন ও মৃত্যুর বিষয়ে বিস্তারিত তথ্য কুরআন বা সহিহ হাদিসে সরাসরি উল্লেখ নেই। ইসলামী ঐতিহ্য এবং ঐতিহাসিক বর্ণনায় তাঁর শেষ জীবনের কিছু ধারণা পাওয়া যায়।\n\n"
                            + "<b>সামুদ জাতির ধ্বংস এবং সালেহ (আলাইহি সালাম):</b>\n\n"
                            + "সামুদ জাতি আল্লাহ্র তাওহীদ অস্বীকার করে এবং সালেহ (আলাইহি সালাম)-এর বার্তা প্রত্যাখ্যান করে। তারা আল্লাহ্র নিদর্শন “উষ্ট্রী” হত্যা করে, যা আল্লাহ্র প্রতি তাদের বিদ্রোহের চূড়ান্ত প্রকাশ।\n"
                            + "আল্লাহ্ সামুদ জাতির ওপর ভয়াবহ শাস্তি নাজিল করেন—একটি প্রবল বজ্রধ্বনি ও ভূমিকম্প। এই শাস্তিতে পুরো সামুদ জাতি ধ্বংস হয়ে যায়।\n\n"
                            + "فَأَخَذَتۡهُمُ ٱلصَّيۡحَةُ فَأَصۡبَحُواْ فِي دِيَٰرِهِمۡ جَٰثِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর তাদের ওপর বজ্রধ্বনি পতিত হলো এবং তারা তাদের গৃহে উপুড় হয়ে পড়ে রইল।\n\n"
                            + "<b>[সূরা হুদ, ১১:৬৭]</b>\n\n"
                            + "সামুদ জাতির ধ্বংসের পর সালেহ (আলাইহি সালাম) এবং তাঁর ঈমানদার অনুসারীরা বেঁচে যান। আল্লাহ্ তাঁদের রক্ষা করেন এবং সুরক্ষিত স্থানে স্থানান্তরিত করেন।\n\n"
                            + "<b>সামুদ জাতির এলাকা ত্যাগ:</b>\n\n"
                            + "সামুদ জাতি ধ্বংসের পর সালেহ (আলাইহি সালাম) সেই অঞ্চল ত্যাগ করেন। তিনি তাঁর অনুসারীদের সঙ্গে অন্যত্র চলে যান। ঐতিহাসিকদের মতে, তিনি আরবের উত্তরাঞ্চল থেকে শাম (বর্তমান সিরিয়া ও ফিলিস্তিনের এলাকা) বা ইয়েমেনের কোনো স্থানে চলে যান।\n\n"
                            + "<b>তাওহীদের প্রচার অব্যাহত:</b>\n\n"
                            + "সামুদ জাতি ধ্বংসের পরও সালেহ (আলাইহি সালাম) তাঁর জীবনের বাকি সময় তাওহীদের দাওয়াত দিতে কাটিয়েছেন। তিনি তাঁর অনুসারীদের আল্লাহ্র প্রতি দৃঢ় বিশ্বাস ও আনুগত্যের পথে পরিচালিত করেছিলেন।",
                    "Prophet Saleh (AS) was a venerable messenger sent by Allah to guide the people of Thamud. After the annihilation of his rebellious community, he departed with the surviving believers to begin a new chapter of righteous worship and guidance.\n\n"
                            + "<b>The Aftermath of Thamud's Destruction:</b>\n"
                            + "The cataclysm of the deafening blast and earthquake decimated the transgressors, leaving their mountain houses as silent monuments:\n"
                            + "فَأَخَذَتۡهُمُ ٱلصَّيۡحَةُ فَأَصۡبَحُواْ فِي دِيَٰرِهِمۡ جَٰثِمِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And the shriek seized those who had wronged, and they became within their homes [corpses] fallen prone.\"\n"
                            + "<b>[Surah Hud 11:67]</b>\n\n"
                            + "<b>Migration from the Stricken Lands:</b>\n"
                            + "Prophet Saleh (AS) left the ruined land of Al-Hijr alongside the believers. According to prominent classical historians (such as Ibn Kathir and At-Tabari), he migrated either towards the blessed lands of Ash-Sham (Palestine / Syria), Makkah, or the southern region of Hadhramaut in Yemen.\n\n"
                            + "<b>Lifelong Dedication to Monotheism:</b>\n"
                            + "Throughout the remainder of his blessed life, Prophet Saleh (AS) led the believers upon the straight path of devotion, gratitude, and steadfast monotheism until he peacefully met his Lord."
            ));
        } else if (prophetId == 6) {
            // ==========================================
            // 6. হযরত ইব্রাহীম (আলাইহিস সালাম) - 11 Chapters
            // ==========================================

            // 1. পরিচয়
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "পরিচয়",
                    "Identity and Lineage",
                    "হযরত ইব্রাহীম (عليه السلام) ছিলেন আল্লাহর এক প্রিয় নবী ও রাসূল, যিনি ইসলামের অন্যতম প্রধান ভিত্তিরূপ। কুরআনে তাঁকে \"خليل الله\" (খলীলুল্লাহ) অর্থাৎ আল্লাহর বন্ধু বলা হয়েছে। তিনি নবী নূহ (عليه السلام) এর দশম বংশধর। ইব্রাহীম ইবন আজর (তাররাহ) ইবন নাহূর ইবন সারূগ ইবন রা'উ ইবন ফালিগ ইবন ‘আবির ইবন শালিখ ইবন আরফাখশাদ ইবন সাম ইবন নূহ (عليه السلام)\n\n"
                            + "কুরআনে আল্লাহ বলেন,\n\n"
                            + "وَإِذِ ٱبْتَلَىٰٓ إِبْرَٰهِـۧمَ رَبُّهُۥ بِكَلِمَـٰتٍ فَأَتَمَّهُنَّ ۖ قَالَ إِنِّى جَاعِلُكَ لِلنَّاسِ إِمَامًا ۖ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর স্মরণ কর, যখন ইব্রাহীমকে তাঁর প্রতিপালক কয়েকটি বিষয়ে পরীক্ষা করলেন, তখন সে তা পূর্ণ করল। তিনি বললেন, আমি তোমাকে মানব জাতির নেতা বানাব।\n\n"
                            + "<b>[সূরা আল-বাকারা - ২:১২৪]</b>\n\n"
                            + "<b>বাসস্থান ও ভৌগলিক অবস্থান:</b>\n\n"
                            + "হযরত ইব্রাহীম (عليه السلام) মূলত জন্মগ্রহণ করেন ইরাকের বাবিল নগরীর নিকটবর্তী একটি শহরে, যার নাম ঊর (Ur)। সেই সময় তাঁর জাতি ছিল মূর্তিপূজায় লিপ্ত, এবং তাঁর বাবা আজর নিজেও একজন মূর্তি নির্মাতা ছিলেন।\n\n"
                            + "<b>সংক্ষেপে তাঁর যাত্রাপথ:</b>\n\n"
                            + "• <b>জন্মস্থান:</b> ঊর (ইরাক)\n"
                            + "• <b>হারান (সিরিয়া ও তুরস্ক সীমান্ত) :</b> দাওয়াতি কার্যক্রমের অংশ\n"
                            + "• <b>কানআন (ফিলিস্তিন) :</b> স্থায়ী বাসস্থান\n"
                            + "• <b>মিসর (ইজিপ্ট) :</b> সাময়িক সফর\n"
                            + "• <b>মক্কা (সৌদি আরব) :</b> হযরত ইসমাঈল (عليه السلام) ও হযরত হাজেরা (عليها السلام) কে নিয়ে আগমন ও কা‘বা নির্মাণ\n\n"
                            + "<b>তাঁর সন্তান ও বংশধর:</b>\n\n"
                            + "• <b>ইসমাঈল (عليه السلام) :</b> যিনি আরবদের পূর্বপুরুষ এবং রাসূলুল্লাহ ﷺ এর বংশধর।\n"
                            + "• <b>ইসহাক (عليه السلام) :</b> যাঁর মাধ্যমে বনী ইসরাঈল বংশধারা শুরু হয়।",
                    "Prophet Ibrahim (AS) was a beloved Prophet and Messenger of Allah, holding one of the foundational pillars of Islamic faith. In the Holy Quran, he is honoured with the noble title 'Khalilullah' (Friend of Allah). He is the tenth-generation descendant of Prophet Nuh (AS):\n"
                            + "Ibrahim ibn Azar (Tarikh) ibn Nahur ibn Sarugh ibn Ra'u ibn Faligh ibn 'Abir ibn Shalikh ibn Arfakhshad ibn Sam ibn Nuh (AS).\n\n"
                            + "<b>Quranic Declaration:</b>\n"
                            + "وَإِذِ ٱبْتَلَىٰٓ إِبْرَٰهِـۧمَ رَبُّهُۥ بِكَلِمَـٰتٍ فَأَتَمَّهُنَّ ۖ قَالَ إِنِّى جَاعِلُكَ لِلنَّاسِ إِمَامًا ۖ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And [mention, O Muhammad], when Ibrahim was tried by his Lord with commands and he fulfilled them. [Allah] said, 'Indeed, I will make you a leader for the people.'\"\n"
                            + "<b>[Surah Al-Baqarah 2:124]</b>\n\n"
                            + "<b>Dwelling and Geographical Origin:</b>\n"
                            + "Prophet Ibrahim (AS) was born in Ur, an ancient city near Babylon in modern Iraq. At that time, his people were deeply entrenched in idol worship, and his father Azar was a sculptor and vendor of idols.\n\n"
                            + "<b>Summary of His Blessed Journey:</b>\n"
                            + "• <b>Birthplace:</b> Ur (Babylon, Iraq)\n"
                            + "• <b>Haran (Syria / Turkey border):</b> Propagation and missionary journey\n"
                            + "• <b>Canaan (Palestine):</b> Permanent dwelling\n"
                            + "• <b>Egypt (Misr):</b> Temporary migration\n"
                            + "• <b>Makkah (Hijaz, Saudi Arabia):</b> Settling Ismail (AS) and Hajar (AS), and rebuilding the Holy Ka'bah\n\n"
                            + "<b>Offspring and Prophetic Legacy:</b>\n"
                            + "• <b>Ismail (AS):</b> Ancestor of the Arabs and forefather of Prophet Muhammad ﷺ.\n"
                            + "• <b>Ishaq (AS):</b> Forefather of the Children of Israel (Bani Isra'il) and subsequent noble prophets."
            ));

            // 2. আবুল আম্বিয়া ও সাইয়েদুল আম্বিয়া
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "আবুল আম্বিয়া ও সাইয়েদুল আম্বিয়া",
                    "Abul Anbiya and Shahidul Anbiya",
                    "(আবুল আম্বিয়া) নবীদের পিতা:\n\n"
                            + "হযরত ইব্রাহীম (عليه السلام)–কে “আবুল আম্বিয়া” বলা হয়, অর্থাৎ নবীদের পিতা। কারণ তাঁর দুই পুত্র, ইসমাঈল (عليه السلام) ও ইসহাক (عليه السلام) — এই দুই বংশ থেকেই বহু নবী প্রেরিত হয়েছেন। যেমন,\n\n"
                            + "• ইসমাঈল (عليه السلام) এর বংশধর — রাসূলুল্লাহ ﷺ\n"
                            + "• ইসহাক (عليه السلام) এর বংশধর — ইয়াকুব, ইউসুফ, মূসা, ঈসা (عليهم السلام) প্রমুখ নবীগণ।\n\n"
                            + "<b>কুরআনে উল্লেখ:</b>\n\n"
                            + "مِّلَّةَ أَبِيكُمْ إِبْرَٰهِيمَ ۚ هُوَ سَمَّىٰكُمُ ٱلْمُسْلِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "এটি তোমাদের পিতা ইব্রাহীমের ধর্ম। তিনিই তোমাদের নাম রেখেছেন মুসলিম।\n\n"
                            + "<b>[সূরা আল-হাজ্জ - ২২:৭৮]</b>\n\n"
                            + "এই আয়াতে ইব্রাহীম (عليه السلام)–কে “অব্বুকুম ইবরাহীম” বলা হয়েছে – তোমাদের পিতা ইব্রাহীম।\n\n"
                            + "<b>শাহিদুল আম্বিয়া:</b>\n\n"
                            + "হযরত যাকারিয়া (عليه السلام) অথবা হযরত ইয়াহইয়া (عليه السلام) এ দুই নবীকে “শাহিদুল আম্বিয়া” বলা হয়ে থাকে। তবে সবচেয়ে প্রসিদ্ধ মতে হযরত ইয়াহইয়া (عليه السلام) কেই বলা হয় \"নবীদের শহীদ\", কারণ তিনিই অল্প বয়সে অত্যন্ত নির্মমভাবে শহীদ হন।\n\n"
                            + "<b>কুরআনের বর্ণনা:</b>\n\n"
                            + "وَسَلَـٰمٌ عَلَيْهِ يَوْمَ وُلِدَ وَيَوْمَ يَمُوتُ وَيَوْمَ يُبْعَثُ حَيًّا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর তাঁর প্রতি শান্তি বর্ষিত হোক যেদিন তিনি জন্মগ্রহণ করেছেন, যেদিন তিনি মৃত্যুবরণ করবেন, এবং যেদিন তাঁকে জীবিত করা হবে।\n\n"
                            + "<b>[সূরা মারইয়াম - ১৯:১৫]</b>\n\n"
                            + "এই আয়াতে হযরত ইয়াহইয়া (عليه السلام) সম্পর্কে বলা হয়েছে। হাদীসে তাঁর শহীদ হওয়ার ব্যাপারে এসেছে রাসূলুল্লাহ ﷺ বলেন,\n\n"
                            + "أَشَدُّ النَّاسِ بَلَاءً الْأَنْبِيَاءُ، ثُمَّ الْأَمْثَلُ فَالْأَمْثَلُ...\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "সবচেয়ে কঠিন পরীক্ষার সম্মুখীন হন নবীগণ, এরপর যাঁরা তাঁদের মত শ্রেষ্ঠ।\n\n"
                            + "<b>[সহীহ আল-বুখারী, হাদীস - ৫৬৪৫]</b>\n\n"
                            + "এই হাদীস ইঙ্গিত করে নবীদের কষ্ট, অত্যাচার, এমনকি শাহাদাতের বিষয়টিও।",
                    "<b>(Abul Anbiya) Father of the Prophets:</b>\n"
                            + "Prophet Ibrahim (AS) is universally revered as 'Abul Anbiya' (Father of the Prophets) because the vast majority of subsequent messengers and prophets descended from his two blessed sons, Ismail (AS) and Ishaq (AS):\n"
                            + "• Lineage of Ismail (AS) — The Final Messenger Muhammad ﷺ\n"
                            + "• Lineage of Ishaq (AS) — Yaqub, Yusuf, Musa, Harun, Dawud, Sulaiman, Isa (peace be upon them all).\n\n"
                            + "<b>Quranic Reference:</b>\n"
                            + "مِّلَّةَ أَبِيكُمْ إِبْرَٰهِيمَ ۚ هُوَ سَمَّىٰكُمُ ٱلْمُسْلِمِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"[It is] the religion of your father, Abraham. Allah named you 'Muslims' before.\"\n"
                            + "<b>[Surah Al-Hajj 22:78]</b>\n\n"
                            + "In this verse, Ibrahim (AS) is affectionately designated as 'Abukum Ibrahim' (your father Ibrahim).\n\n"
                            + "<b>Shahidul Anbiya (The Martyr of the Prophets):</b>\n"
                            + "Prophet Zakariya (AS) or Prophet Yahya (AS) are referenced with the title 'Shahidul Anbiya'. According to the most prominent historical consensus, Prophet Yahya (AS) is uniquely remembered as 'The Martyr among Prophets' due to his unjust and brutal martyrdom at a youthful age for proclaiming the truth.\n\n"
                            + "<b>Quranic Honour:</b>\n"
                            + "وَسَلَـٰمٌ عَلَيْهِ يَوْمَ وُلِدَ وَيَوْمَ يَمُوتُ وَيَوْمَ يُبْعَثُ حَيًّا\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And peace be upon him the day he was born and the day he dies and the day he is raised alive.\"\n"
                            + "<b>[Surah Maryam 19:15]</b>\n\n"
                            + "<b>Prophetic Hadith:</b>\n"
                            + "The Messenger of Allah ﷺ said:\n"
                            + "أَشَدُّ النَّاسِ بَلَاءً الْأَنْبِيَاءُ، ثُمَّ الْأَمْثَلُ فَالْأَمْثَلُ...\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"The people who face the most severe trials are the prophets, then the next best, then the next best...\"\n"
                            + "<b>[Sahih al-Bukhari 5645]</b>"
            ));

            // 3. নিজ পিতাকে দাওয়াত দেয়া ও কঠিন অবস্থান গ্রহণ
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "নিজ পিতাকে দাওয়াত দেয়া ও কঠিন অবস্থান গ্রহণ",
                    "Dawah to His Father and Firm Stance",
                    "ইব্রাহিম (আ.)-এর পিতা ছিলেন آزر (আযর), যিনি ছিলেন মূর্তিপূজক এবং মূর্তি তৈরি ও বিক্রির কাজ করতেন। কিন্তু ইব্রাহিম (আ.) যখন ছোট বয়সেই হক (সত্য) চিনে ফেলেন, তখনই তিনি তাঁর বাবাকে আল্লাহর দিকে ডাক দেন। আল্লাহ তা’আলা ইব্রাহিম (আ.) ও তাঁর পিতার সংলাপ উল্লেখ করেছেন, যা খুবই স্পষ্ট, আবেগপ্রবণ এবং গভীর শিক্ষা বহন করে।\n\n"
                            + "يَٰٓأَبَتِ لِمَ تَعۡبُدُ مَا لَا يَسۡمَعُ وَلَا يُبۡصِرُ وَلَا يُغۡنِي عَنكَ شَيۡـٔٗا ٤٢\n\n"
                            + "يَٰٓأَبَتِ إِنِّي قَدۡ جَآءَنِي مِنَ ٱلۡعِلۡمِ مَا لَمۡ يَأۡتِكَ فَٱتَّبِعۡنِيٓ أَهۡدِكَ صِرَٰطٗا سَوِيّٗا ٤٣\n\n"
                            + "يَٰٓأَبَتِ لَا تَعۡبُدِ ٱلشَّيۡطَٰنَۖ إِنَّ ٱلشَّيۡطَٰنَ كَانَ لِلرَّحۡمَٰنِ عَصِيّٗا ٤٤\n\n"
                            + "يَٰٓأَبَتِ إِنِّيٓ أَخَافُ أَن يَمَسَّكَ عَذَابٞ مِّنَ ٱلرَّحۡمَٰنِ فَتَكُونَ لِلشَّيۡطَٰنِ وَلِيّٗا ٤٥\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমার পিতা! আপনি কেন এমন কিছু পূজা করেন, যা না শোনে, না দেখে, আর আপনাকে কিছুই উপকার দিতে পারে না?\n\n"
                            + "হে আমার পিতা! আমার কাছে এমন জ্ঞান এসেছে, যা আপনার কাছে আসেনি; আপনি আমার অনুসরণ করুন, আমি আপনাকে সরল পথ দেখাব।\n\n"
                            + "হে আমার পিতা! আপনি শয়তানের ইবাদত করবেন না, নিশ্চয় শয়তান আল্লাহর (আর-রাহমানের) প্রতি অত্যন্ত অবাধ্য।\n\n"
                            + "হে আমার পিতা! আমি আশঙ্কা করি, আপনি যদি আল্লাহর আজাবের শিকার হন, তবে আপনি শয়তানের সঙ্গী হয়ে যাবেন।\n\n"
                            + "<b>[সূরা মারইয়াম - ৪২–৪৫]</b>\n\n"
                            + "<b>পিতার প্রতিক্রিয়া:</b>\n\n"
                            + "পিতা আযরের প্রতিক্রিয়া ছিল অত্যন্ত কঠিন ও আঘাতমূলক। কুরআনে এসেছে,\n\n"
                            + "قَالَ أَرَاغِبٌ أَنتَ عَنۡ آلِهَتِي يَٰٓإِبۡرَٰهِيمُۖ لَئِن لَّمۡ تَنتَهِ لَأَرۡجُمَنَّكَ وَٱهۡجُرۡنِي مَلِيّٗا ٤٦\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আযর বলল: হে ইব্রাহিম! তুমি কি আমার উপাস্যদের থেকে মুখ ফিরিয়ে নিয়েছ? যদি তুমি বিরত না হও, তবে আমি তোমাকে প্রস্তরাঘাতে হত্যা করব। তুমি আমার থেকে দূরে চলে যাও।\n\n"
                            + "<b>[সূরা মারইয়াম – ৪৬]</b>\n\n"
                            + "<b>ইব্রাহিম (আ.)-এর জবাব:</b>\n\n"
                            + "তিনি কিভাবে জবাব দিলেন, দেখুন — কতটা কোমলতা, অথচ কত দৃঢ় ঈমান!\n\n"
                            + "قَالَ سَلَٰمٌ عَلَيۡكَۖ سَأَسۡتَغۡفِرُ لَكَ رَبِّيٓۖ إِنَّهُۥ كَانَ بِي حَفِيّٗا ٤٧\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "ইব্রাহিম বললেন: আপনার উপর শান্তি বর্ষিত হোক। আমি আমার রবের নিকট আপনার জন্য ক্ষমা প্রার্থনা করব। নিশ্চয়ই তিনি আমার প্রতি অত্যন্ত দয়ালু।\n\n"
                            + "<b>[সূরা মারইয়াম - ৪৭]</b>\n\n"
                            + "<b>শিক্ষণীয় বিষয়:</b>\n\n"
                            + "• <b>দাওয়াত শুরু হয় ঘর থেকে:</b> দাওয়াতের প্রকৃত চ্যালেঞ্জ তখনই আসে, যখন তা দিতে হয় নিজের পরিবারের মধ্যে।\n"
                            + "• <b>সম্মান ও কোমলতা:</b> \"يَا أَبَتِ\" (হে আমার পিতা) — এই শব্দটি চারবার বলেছেন; এটি কতটা শ্রদ্ধা ও ভালোবাসার বহিঃপ্রকাশ!\n"
                            + "• <b>তাওহীদের উপর আপস না করা:</b> পিতা রাগ করলেও, হুমকি দিলেও ইব্রাহিম (আ.) নিজের ঈমান ছাড়েননি।\n"
                            + "• <b>দুঃখ ও বিচ্ছেদ:</b> পিতা যখন সম্পর্ক ছিন্ন করলেন, ইব্রাহিম (আ.) তখনও শান্তি ও দুআর মাধ্যমে বিদায় জানান।\n"
                            + "• <b>পরবর্তীতে দু‘আ বন্ধ করা:</b> যদিও তিনি পিতার জন্য প্রথমে দুআ করেন, পরে যখন আল্লাহ নিষেধ করেন, তখন তিনি আর দুআ করেননি।",
                    "Prophet Ibrahim's father was Azar, an artisan who sculpted and sold idols. When Ibrahim (AS) recognized the divine truth at a young age, he called his father towards Allah with immense tenderness, utmost respect, and logical clarity.\n\n"
                            + "<b>The Dialogue in the Holy Quran:</b>\n"
                            + "يَٰٓأَبَتِ لِمَ تَعۡبُدُ مَا لَا يَسۡمَعُ وَلَا يُبۡصِرُ وَلَا يُغۡনِي عَنكَ شَيۡـٔٗا ٤٢\n"
                            + "يَٰٓأَبَتِ إِنِّي قَدۡ جَآءَنِي مِنَ ٱلۡعِلۡمِ مَا لَمۡ يَأۡتِكَ فَٱتَّبِعۡنِيٓ أَهۡدِكَ صِرَٰطٗا سَوِيّٗا ٤٣\n"
                            + "يَٰٓأَبَتِ لَا تَعۡبُدِ ٱلشَّيۡطَٰنَۖ إِنَّ ٱلشَّيۡطَٰنَ كَانَ لِلرَّحۡمَٰنِ عَصِيّٗا ٤٤\n"
                            + "يَٰٓأَبَتِ إِنِّيٓ أَخَافُ أَن يَمَسَّكَ عَذَابٞ مِّنَ ٱلرَّحۡمَٰنِ فَتَكُونَ لِلشَّيۡطَٰنِ وَلِيّٗا ٤٥\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"[Mention] when he said to his father, 'O my father, why do you worship that which does not hear and does not see and will not benefit you at all?'\n"
                            + "'O my father, indeed there has come to me of knowledge that which has not come to you, so follow me; I will guide you to an even path.'\n"
                            + "'O my father, do not worship Satan. Indeed Satan has ever been, to the Most Merciful, disobedient.'\n"
                            + "'O my father, indeed I fear that there will touch you a punishment from the Most Merciful so you would be to Satan a companion [in Hellfire].'\"\n"
                            + "<b>[Surah Maryam 19:42-45]</b>\n\n"
                            + "<b>The Father's Hostile Threat:</b>\n"
                            + "قَالَ أَرَاغِبٌ أَنتَ عَنۡ آلِهَتِي يَٰٓإِبۡرَٰهِيمُۖ لَئِن لَّمۡ تَنتَهِ لَأَرۡجُمَنَّكَ وَٱهۡجُرۡنِي مَلِيّٗا ٤٦\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"[His father] said, 'Have you no desire for my gods, O Abraham? If you do not desist, I will surely stone you, so leave me a prolonged time.'\"\n"
                            + "<b>[Surah Maryam 19:46]</b>\n\n"
                            + "<b>Ibrahim's Gentle and Resolute Response:</b>\n"
                            + "قَالَ سَلَٰمٌ عَلَيۡكَۖ سَأَسۡتَغۡفِرُ لَكَ رَبِّيٓۖ إِنَّهُۥ كَانَ بِي حَفِيّٗا ٤٧\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"[Abraham] said, 'Peace will be upon you. I will ask forgiveness for you of my Lord. Indeed, He is ever gracious to me.'\"\n"
                            + "<b>[Surah Maryam 19:47]</b>\n\n"
                            + "<b>Key Lessons:</b>\n"
                            + "• <b>Dawah Begins at Home:</b> The greatest test of faith begins within one's own household.\n"
                            + "• <b>Filial Respect and Tenderness:</b> He repeated 'O my father' (Ya Abati) four times with deep affection.\n"
                            + "• <b>Uncompromising Monotheism:</b> Despite intense threats of being stoned, he never wavered in his conviction.\n"
                            + "• <b>Grace in Farewell:</b> Even when rejected and driven away, he parted with peace and prayers."
            ));

            // 4. দাওয়াতের ফলশ্রুতি
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "দাওয়াতের ফলশ্রুতি",
                    "Outcome and Impact of Dawah",
                    "তিনি মানুষকে শিখিয়েছেন, একমাত্র আল্লাহই উপাসনার যোগ্য। মূর্তিপূজা এবং সব ধরনের শিরক থেকে বিরত থাকার দাওয়াত দেন। আল্লাহ বলেন,\n\n"
                            + "إِذۡ قَالَ لِأَبِيهِ وَقَوۡمِهِۦ مَا تَعۡبُدُونَ ٨٥\n\n"
                            + "أَئِفۡكًا ءَالِهَةٗ دُونَ ٱللَّهِ تُرِيدُونَ ٨٦\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন তিনি তাঁর পিতা ও জাতিকে বললেন, ‘তোমরা কী উপাসনা করো?\n\n"
                            + "আল্লাহ ছাড়া অন্য উপাস্যদেরকে কি তোমরা মিথ্যা দিয়ে প্রতিষ্ঠা করতে চাও?\n\n"
                            + "<b>[সূরা আস-সাফফাত - ৮৫–৮৬]</b>\n\n"
                            + "এখান থেকে বোঝা যায়, তিনি প্রশ্ন ও যুক্তির মাধ্যমে দাওয়াত দিতেন।\n\n"
                            + "<b>যুক্তি ও প্রমাণের মাধ্যমে দাওয়াত:</b>\n\n"
                            + "হযরত ইব্রাহিম (আ.) শুধু আবেগ নয়, গভীর যুক্তি ও পর্যবেক্ষণের মাধ্যমে মানুষকে সত্যের পথে ডাকতেন। কুরআনে আছে, তিনি চাঁদ, সূর্য, তারা দেখে বলেছিলেন, هَٰذَا رَبِّي (এটাই কি আমার প্রতিপালক?) কিন্তু যখন তা অস্ত যায়, তখন বললেন\n\n"
                            + "إِنِّي لَآ أُحِبُّ ٱلۡأٓفِلِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আমি অস্তমিত হওয়াদেরকে ভালোবাসি না।\n\n"
                            + "<b>[সূরা আল-আন‘আম – ৭৬]</b>\n\n"
                            + "তিনি দেখিয়েছেন, প্রকৃতি পরিবর্তনশীল আর যিনি পরিবর্তিত হন, তিনি উপাস্য হতে পারেন না।\n\n"
                            + "<b>মূর্তিপূজার বিরুদ্ধে স্পষ্ট অবস্থান:</b>\n\n"
                            + "ইব্রাহিম (আ.) তাঁর জাতির মূর্তিগুলো ভেঙে দিয়ে তাদের চরম বাস্তবতা দেখিয়েছিলেন। কুরআনে বলা হয়েছে,\n\n"
                            + "فَرَاغَ عَلَيۡهِمۡ ضَرۡبَۢا بِٱلۡيَمِينِ ٩٣\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি তাদের (মূর্তিগুলোর) ওপর ডান হাতে আঘাত হানলেন।\n\n"
                            + "<b>[সূরা আস-সাফফাত - ৯৩]</b>\n\n"
                            + "এরপর জাতি তাঁকে আগুনে নিক্ষেপ করে, কিন্তু আল্লাহ তাকে বাঁচিয়ে দেন,\n\n"
                            + "يَـٰنَارُ كُونِي بَرۡدٗا وَسَلَٰمًا عَلَىٰٓ إِبۡرَٰهِيمَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আগুন! তুমি ইব্রাহিমের জন্য ঠাণ্ডা ও নিরাপদ হয়ে যাও।\n\n"
                            + "<b>[সূরা আল-আম্বিয়া - ৬৯]</b>\n\n"
                            + "তাঁর দাওয়াত শুধু জাতিকেই নয়, ভবিষ্যৎ প্রজন্ম ও গোটা মানবজাতির প্রতি। তিনি আল্লাহর কাছে দো‘আ করেন ভবিষ্যৎ প্রজন্মের হিদায়াতের জন্য\n\n"
                            + "رَبِّ ٱجۡعَلۡنِي مُقِيمَ ٱلصَّلَوٰةِ وَمِن ذُرِّيَّتِيۚ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমার রব! আমাকে ও আমার সন্তানদের নামায প্রতিষ্ঠাকারী বানাও।\n\n"
                            + "<b>[সূরা ইব্রাহিম - ৪০]</b>\n\n"
                            + "এমনকি তিনি রাসূলুল্লাহ ﷺ-এর আগমন কামনা করে দো‘আ করেছিলেন,\n\n"
                            + "رَبَّنَا وَٱبۡعَثۡ فِيهِمۡ رَسُولٗا مِّنۡهُمۡ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমাদের প্রতিপালক! তাদের মধ্য থেকে একজন রাসূল পাঠিয়ে দাও।\n\n"
                            + "<b>[সূরা আল-বাকারা – ১২৯]</b>",
                    "Prophet Ibrahim (AS) taught humanity that Allah alone is worthy of worship and commanded total rejection of all forms of polytheism and idolatry.\n\n"
                            + "<b>The Rational Questioning:</b>\n"
                            + "إِذۡ قَالَ لِأَبِيهِ وَقَوۡمِهِۦ مَا تَعۡبُدُونَ ٨٥\n"
                            + "أَئِفۡكًا ءَالِهَةٗ دُونَ ٱللَّهِ تُرِيدُونَ ٨٦\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"When he said to his father and his people, 'What do you worship?'\n"
                            + "'Is it falsehood [as] gods other than Allah you desire?'\"\n"
                            + "<b>[Surah As-Saffat 37:85-86]</b>\n\n"
                            + "<b>Dawah through Observation and Logic:</b>\n"
                            + "When observing celestial bodies—the star, moon, and sun—he demonstrated their transient nature:\n"
                            + "إِنِّي لَآ أُحِبُّ ٱلۡأٓفِلِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"I like not those that set [disappear].\"\n"
                            + "<b>[Surah Al-An'am 6:76]</b>\n\n"
                            + "<b>Decisive Action Against Idols:</b>\n"
                            + "فَرَاغَ عَلَيۡهِمۡ ضَرۡبَۢا بِٱلۡيَمِينِ ٩٣\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And he turned upon them a blow with [his] right hand.\"\n"
                            + "<b>[Surah As-Saffat 37:93]</b>\n\n"
                            + "When cast into the blazing furnace, Allah protected him:\n"
                            + "يَـٰنَارُ كُونِي بَرۡدٗا وَسَلَٰমًا عَلَىٰٓ إِبۡرَٰهِيمَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"O fire, be coolness and safety upon Abraham.\"\n"
                            + "<b>[Surah Al-Anbiya 21:69]</b>\n\n"
                            + "<b>Prayers for Future Generations:</b>\n"
                            + "رَبِّ ٱجۡعَلۡنِي مُقِيمَ ٱلصَّلَوٰةِ وَمِن ذُرِّيَّتِيۚ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"My Lord, make me an establisher of prayer, and [many] from my descendants.\"\n"
                            + "<b>[Surah Ibrahim 14:40]</b>\n\n"
                            + "He also prayed for the advent of the Final Messenger:\n"
                            + "رَبَّنَا وَٱبۡعَثۡ فِيهِمۡ رَسُولٗا مِّنۡهُمۡ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"Our Lord, and send among them a messenger from themselves...\"\n"
                            + "<b>[Surah Al-Baqarah 2:129]</b>"
            ));

            // 5. নমরূদের সঙ্গে বিতর্ক ও অগ্নিপরীক্ষা
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "নমরূদের সঙ্গে বিতর্ক ও অগ্নিপরীক্ষা",
                    "Debate with Nimrod and Trial of the Fire",
                    "এই দুইটি ঘটনা ইব্রাহিম (আ.)-এর ঈমান, সাহস, যুক্তিবাদিতা ও আল্লাহর উপর ভরসার অনন্য দৃষ্টান্ত। চলুন, কুরআন ও হাদীসের আলোকে দুটো বিষয়ই বিস্তারিতভাবে দেখি ইনশাআল্লাহ।\n\n"
                            + "<b>হযরত ইব্রাহিম (আ.) ও নমরূদের মধ্যে বিতর্ক:</b>\n\n"
                            + "أَلَمۡ تَرَ إِلَى ٱلَّذِي حَآجَّ إِبۡرَٰهِيمَ فِي رَبِّهِۦٓ أَنۡ ءَاتَىٰهُ ٱللَّهُ ٱلۡمُلۡكَۖ إِذۡ قَالَ إِبۡرَٰهِيمُ رَبِّيَ ٱلَّذِي يُحۡيِۦ وَيُمِيتُۖ قَالَ أَنَا۠ أُحۡيِۦ وَأُمِيتُۖ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তুমি কি দেখোনি, সেই ব্যক্তিকে (নমরূদ) যে ইব্রাহিমের সাথে তার প্রতিপালক বিষয়ে বিতর্ক করেছিল, আল্লাহ তাকে রাজত্ব দিয়েছিলেন। যখন ইব্রাহিম বললেন, ‘আমার প্রতিপালক তিনিই, যিনি জীবন দান করেন এবং মৃত্যু দেন।’ সে বলল, ‘আমিও জীবন দেই এবং মৃত্যু দেই。\n\n"
                            + "<b>[সূরা আল-বাকারা – ২৫৮]</b>\n\n"
                            + "<b>যুক্তিবাদিতার পরবর্তী স্তর:</b>\n\n"
                            + "নমরূদের ভুল ব্যাখ্যার জবাবে ইব্রাহিম (আ.) সহজ ও চূড়ান্ত যুক্তি দেন,\n\n"
                            + "قَالَ إِبۡرَٰهِيمُ فَإِنَّ ٱللَّهَ يَأۡتِي بِٱلشَّمۡسِ مِنَ ٱلۡمَشۡرِقِ فَأۡتِ بِهَا مِنَ ٱلۡمَغۡরِبِ فَبُهِتَ ٱلَّذِي كَفَرَۗ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তাহলে আল্লাহ তো সূর্যকে পূর্ব থেকে উদিত করেন, তুমি তা পশ্চিম থেকে আনো。\n\n"
                            + "তখন সে কাফির হতবুদ্ধি হয়ে গেল。\n\n"
                            + "<b>[সূরা আল-বাকারা – ২৫৮]</b>\n\n"
                            + "<b>আগুনে নিক্ষিপ্ত হওয়া:</b>\n\n"
                            + "قَالُواْ حَرِّقُوهُ وَٱনصُرُوٓاْ ءَالِهَتَكُمۡ إِن كُنتُمۡ فَٰعِلِينَ ٦٨\n\n"
                            + "قُلۡنَا يَٰنَارُ كُونِي بَرۡدٗا وَسَلَٰمًا عَلَىٰٓ إِبۡرَٰهِيمَ ٦٩\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা বলল, ‘তাকে জ্বালিয়ে দাও এবং তোমাদের দেবতাদের সাহায্য করো, যদি কিছু করতেই চাও।’\n\n"
                            + "আমি বললাম, ‘হে আগুন! তুমি ইব্রাহিমের জন্য ঠাণ্ডা ও নিরাপদ হয়ে যাও。\n\n"
                            + "<b>[সূরা আল-আম্বিয়া - ৬৮–৬৯]</b>\n\n"
                            + "<b>ঐতিহাসিক প্রেক্ষাপট:</b>\n\n"
                            + "• ইব্রাহিম (আ.) যখন মূর্তিগুলো ভেঙে দেন, তখন জাতি ক্ষুব্ধ হয়ে তাকে আগুনে ফেলার সিদ্ধান্ত নেয়।\n"
                            + "• একটি বিশাল অগ্নিকুণ্ড তৈরি করা হয় (তাফসীর ইবনে কাসীর)।\n"
                            + "• তিনি হাতে-পা বাঁধা অবস্থায় গোল্লা (catapult) দিয়ে আগুনে নিক্ষিপ্ত হন।\n\n"
                            + "এক হাদীসে এসেছে,\n\n"
                            + "\"حسبنا الله ونعم الوكيل\"\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আমাদের জন্য আল্লাহই যথেষ্ট, তিনি উত্তম অভিভাবক。\n\n"
                            + "<b>[সহীহ বুখারী, হাদীস - ৪৫৬৩]</b>\n\n"
                            + "এ কালিমা তিনি আগুনে ফেলার সময় পড়েছিলেন।\n\n"
                            + "<b>অলৌকিক রক্ষা:</b>\n\n"
                            + "আল্লাহ্র হুকুমে আগুন তাঁর জন্য \"ঠাণ্ডা\" এবং \"নিরাপদ\" হয়ে যায়।\n"
                            + "আগুন পোড়াতে পারলো না, কারণ আল্লাহ ইচ্ছা করেননি।",
                    "The confrontation between Prophet Ibrahim (AS) and King Nimrod, alongside the miracle of the furnace, represents the pinnacle of monotheistic courage and unwavering reliance upon Allah.\n\n"
                            + "<b>The Theological Debate with Nimrod:</b>\n"
                            + "أَلَمۡ تَرَ إِلَى ٱلَّذِي حَآجَّ إِبۡرَٰهِيمَ فِي رَبِّهِۦٓ أَنۡ ءَاتَىٰهُ ٱللَّهُ ٱلۡمُلۡكَۖ إِذۡ قَالَ إِبۡرَٰهِيمُ رَبِّيَ ٱلَّذِي يُحۡيِۦ وَيُمِيتُۖ قَالَ أَنَا۠ أُحۡيِۦ وَأُمِيتُۖ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"Have you not considered the one who argued with Abraham about his Lord because Allah had given him kingship? When Abraham said, 'My Lord is the one who gives life and causes death,' he said, 'I give life and cause death.'\"\n"
                            + "<b>[Surah Al-Baqarah 2:258]</b>\n\n"
                            + "<b>The Irrefutable Argument:</b>\n"
                            + "قَالَ إِبۡرَٰهِيمُ فَإِنَّ ٱللَّهَ يَأۡتِي بِٱلشَّمۡسِ مِنَ ٱلۡمَشۡرِقِ فَأۡتِ بِهَا مِنَ ٱلۡمَغۡরِبِ فَبُهِتَ ٱلَّذِي كَفَرَۗ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"Abraham said, 'Indeed, Allah brings up the sun from the east, so bring it up from the west.' So the disbeliever was overwhelmed by astonishment.\"\n"
                            + "<b>[Surah Al-Baqarah 2:258]</b>\n\n"
                            + "<b>Cast into the Blazing Fire:</b>\n"
                            + "قَالُواْ حَرِّقُوهُ وَٱনصُرُوٓاْ ءَالِهَتَكُمۡ إِن كُنتُمۡ فَٰعِلِينَ ٦٨\n"
                            + "قُلۡنَا يَٰنَارُ كُونِي بَرۡدٗا وَسَلَٰمًا عَلَىٰٓ إِبۡرَٰهِيمَ ٦٩\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"They said, 'Burn him and support your gods - if you are to act.' We said, 'O fire, be coolness and safety upon Abraham.'\"\n"
                            + "<b>[Surah Al-Anbiya 21:68-69]</b>\n\n"
                            + "<b>The Supplication of Complete Reliance:</b>\n"
                            + "When cast via catapult into the inferno, Ibrahim (AS) uttered:\n"
                            + "حسبنا الله ونعم الوكيل\n\n"
                            + "\"Allah [alone] is sufficient for us, and He is the best Disposer of affairs.\"\n"
                            + "<b>[Sahih al-Bukhari 4563]</b>"
            ));

            // 6. হিজরত ও আল্লাহর উপর তাওয়াক্কুল
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "হিজরত ও আল্লাহর উপর তাওয়াক্কুল",
                    "Migration and Complete Trust in Allah",
                    "এটা শুধু ইতিহাস নয় ঈমান, একাগ্রতা, ও আল্লাহর সন্তুষ্টির জন্য আত্মত্যাগ-এর এক অপূর্ব নিদর্শন। চলুন আমরা কুরআনের আলোকে ইব্রাহিম (আ.)-এর হিজরত ও তাওয়াক্কুল সম্পর্কে বিস্তারিত আলোচনা করি ইনশাআল্লাহ:\n\n"
                            + "<b>হিজরত: সত্যের পথে দেশ ত্যাগ:</b>\n\n"
                            + "فَٱمَنتَ لَهُۥ لُوطٞۘ وَقَالَ إِنِّي مُهَاجِرٌ إِلَىٰ رَبِّيٓۖ إِنَّهُۥ هُوَ ٱلۡعَزِيزُ ٱلۡحَكِيمُ ٢٦\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "লূত ঈমান আনলেন তাঁর উপর। আর ইব্রাহিম বললেন: ‘আমি তো আমার রবের দিকে হিজরত করছি। নিশ্চয় তিনি পরাক্রমশালী ও প্রজ্ঞাময়।\n\n"
                            + "<b>[সূরা আল-আনকাবূত - ২৬]</b>\n\n"
                            + "<b>হিজরতের পটভূমি:</b>\n\n"
                            + "• যখন জাতি ইব্রাহিম (আ.)-এর দাওয়াত প্রত্যাখ্যান করল,\n"
                            + "• তাঁকে আগুনে নিক্ষেপ করল,\n"
                            + "• পরিবার-সমাজ প্রত্যাখ্যান করল,\n"
                            + "• তখন তিনি আল্লাহর দিকে হিজরত করেন।\n\n"
                            + "ইতিহাসবিদদের মতে, তাঁর হিজরতের গন্তব্য ছিল:\n\n"
                            + "ইরাক (বাবিল) → শাম (প্যালেস্টাইন) → মিসর → হিজায (মক্কা)\n\n"
                            + "এভাবেই শুরু হয় দাওয়াতি সফর।\n\n"
                            + "<b>আল্লাহর উপর পরিপূর্ণ ভরসা:</b>\n\n"
                            + "তিনি স্ত্রী হাজেরা (عليها السلام) এবং শিশু ইসমাঈল (عليه السلام)-কে জনমানবহীন, পানি-হীন মরুভূমিতে (মক্কায়) রেখে চলে যান।\n\n"
                            + "رَّبَّنَآ إِنِّيٓ أَسۡكَنتُ مِن ذُرِّيَّتِي بِوَادٍ غَيۡرِ ذِي زَرۡعٍ عِندَ بَيۡتِكَ ٱلۡمُحَرَّمِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমাদের রব! আমি আমার সন্তানদের এমন একটি উপত্যকায় বসবাস করিয়েছি, যেখানে চাষাবাদের কোনো ব্যবস্থা নেই, তোমার পবিত্র গৃহের নিকটে।\n\n"
                            + "<b>[সূরা ইব্রাহিম - ৩৭]</b>\n\n"
                            + "<b>হাজেরা (আ.)-এর প্রশ্ন:</b>\n\n"
                            + "তিনি বলেন, \"আল্লাহর আদেশে কি আপনি আমাদের এখানে রেখে যাচ্ছেন?\" ইব্রাহিম (আ.) ইশারায় বলেন: \"হ্যাঁ\" তখন হাজেরা (আ.) বললেন:\n\n"
                            + "إِذًا لَن يُضَيِّعَنَا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তাহলে আল্লাহ আমাদের কখনো ধ্বংস করবেন না।\n\n"
                            + "<b>[সহীহ বুখারী]</b>",
                    "Prophet Ibrahim's migration stands as an eternal testament to pure faith, supreme devotion, and complete trust (Tawakkul) in Allah.\n\n"
                            + "<b>The Hijrah for the Sake of Allah:</b>\n"
                            + "فَٱمَنتَ لَهُۥ لُوطٞۘ وَقَالَ إِنِّي مُهَاجِرٌ إِلَىٰ رَبِّيٓۖ إِنَّهُۥ هُوَ ٱلۡعَزِيزُ ٱلۡحَكِيمُ ٢٦\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And Lot believed him. [Abraham] said, 'Indeed, I will emigrate to [the service of] my Lord. Indeed, He is the Exalted in Might, the Wise.'\"\n"
                            + "<b>[Surah Al-Ankabut 29:26]</b>\n\n"
                            + "<b>Course of the Sacred Migration:</b>\n"
                            + "Iraq (Babylon) → Sham (Palestine) → Egypt → Hijaz (Makkah)\n\n"
                            + "<b>Leaving Family in the Barren Valley:</b>\n"
                            + "رَّبَّنَآ إِنِّيٓ أَسۡكَنتُ مِن ذُرِّيَّتِي بِوَادٍ غَيۡرِ ذِي زَرۡعٍ عِندَ بَيۡتِكَ ٱلۡمُحَرَّمِ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"Our Lord, I have settled some of my descendants in an uncultivated valley near Your sacred House...\"\n"
                            + "<b>[Surah Ibrahim 14:37]</b>\n\n"
                            + "<b>Hajar's Unshakable Faith:</b>\n"
                            + "When Lady Hajar realized it was divine command, she declared with absolute conviction:\n"
                            + "إِذًا لَن يُضَيِّعَنَا\n\n"
                            + "\"Then He will never forsake us.\"\n"
                            + "<b>[Sahih al-Bukhari]</b>"
            ));

            // 7. সন্তানহীনতার দোআ ও ঈসমাঈল (আ.)-এর জন্ম
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "সন্তানহীনতার দোআ ও ঈসমাঈল (আ.)-এর জন্ম",
                    "Supplication for a Child and Birth of Ismail AS",
                    "এই ঘটনা শুধু ইব্রাহিম (আ.)-এর দো‘আ কবুল হওয়ার নিদর্শনই নয়, বরং এতে রয়েছে তাওয়াক্কুল, ধৈর্য, এবং আল্লাহর উপর নির্ভরতার দারুণ শিক্ষা। চলুন বিষয়টি বিস্তারিতভাবে কুরআনের আলোকে দেখি ইনশাআল্লাহ।\n\n"
                            + "<b>হযরত ইব্রাহিম (আ.)-এর সন্তান লাভের দোআ:</b>\n\n"
                            + "ইব্রাহিম (আ.) ছিলেন বয়সে বৃদ্ধ, এবং বহু বছর পর্যন্ত তাঁর কোনো সন্তান ছিল না। তবুও তিনি আল্লাহর কাছে দুআ করতেন ধৈর্য সহকারে। কুরআনের দো‘আ,\n\n"
                            + "رَبِّ هَبۡ لِي مِنَ ٱلصَّٰلِحِينَ ١٠٠\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমার রব! আমাকে সৎকর্মশীল সন্তান দান কর।\n\n"
                            + "<b>[সূরা আস-সাফফাত - ১০০]</b>\n\n"
                            + "এটি অত্যন্ত সংক্ষিপ্ত কিন্তু গভীর দো‘আ\n\n"
                            + "• “رَبِّ” একান্তভাবে রবকে ডাকা,\n"
                            + "• “هَبْ لِي” আমাকে উপহার দাও (হক নয়, অনুগ্রহ),\n"
                            + "• “مِنَ الصَّالِحِينَ” কেবল সন্তান নয়, সৎ সন্তান।\n\n"
                            + "<b>ঈসমাঈল (আ.)-এর জন্ম:</b>\n\n"
                            + "এই দো‘আর ফলেই আল্লাহ তাআলা তাঁকে ঈসমাঈল (আ.)-এর খুশির সংবাদ দেন। কুরআনের বর্ণনা,\n\n"
                            + "فَبَشَّرۡنَٰهُ بِغُلَٰمٍ حَلِيمٖ ١٠١\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর আমি তাকে এক সহিষ্ণু পুত্র সন্তানের সুসংবাদ দিলাম।\n\n"
                            + "<b>[সূরা আস-সাফফাত - ১০১]</b>\n\n"
                            + "غُلَامٍ حَلِيم এখানে “সহিষ্ণু, ধৈর্যশীল” সন্তান বলা হয়েছে, যা ঈসমাঈল (আ.)-এর চরিত্রের প্রতিচ্ছবি।\n\n"
                            + "<b>ঈসমাঈল (আ.) জন্মের প্রেক্ষাপট:</b>\n\n"
                            + "ইব্রাহিম (আ.)-এর প্রথম স্ত্রী সারাহ (عليها السلام) ছিলেন বন্ধ্যা। পরে তাঁর অনুমতিতে ইব্রাহিম (আ.) হাযেরা (عليها السلام)-কে বিয়ে করেন। আল্লাহর কুদরতে হাযেরা (আ.)-এর গর্ভে ঈসমাঈল (আ.) জন্মগ্রহণ করেন। জন্মের পর ইব্রাহিম (আ.) তাঁদেরকে নিয়ে যান নির্জন মরুভূমিতে বর্তমানের মক্কা।\n\n"
                            + "রাসূলুল্লাহ ﷺ বলেন,\n\n"
                            + "\"إِنَّمَا يُرْزَقُ الْعَبْدُ عَلَى قَدَرِ نِيَّتِهِ\"\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "একজন বান্দাকে তার নিয়তের পরিমাণেই রিযিক দেওয়া হয়।\n\n"
                            + "<b>[মুসনাদ আহমদ – হাদীস সহীহ]</b>\n\n"
                            + "ইব্রাহিম (আ.) সৎ সন্তান চেয়েছিলেন, তাই তিনি এমন সন্তান পেয়েছিলেন, যিনি ছিলেন নবী।",
                    "Despite his advanced age and barrenness, Prophet Ibrahim (AS) never ceased supplicating to his Lord with profound patience and devotion.\n\n"
                            + "<b>The Supplication for Righteous Offspring:</b>\n"
                            + "رَبِّ هَبۡ لِي مِنَ ٱلصَّٰلِحِينَ ١٠٠\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"My Lord, grant me [a child] from among the righteous.\"\n"
                            + "<b>[Surah As-Saffat 37:100]</b>\n\n"
                            + "<b>The Glad Tidings of Ismail (AS):</b>\n"
                            + "فَبَشَّرۡنَٰهُ بِغُلَٰمٍ حَلِيمٖ ١٠١\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"So We gave him good tidings of a forbearing boy.\"\n"
                            + "<b>[Surah As-Saffat 37:101]</b>\n\n"
                            + "The attribute 'Ghulamun Haleem' reflects the deep patience and forbearance of Ismail (AS).\n\n"
                            + "<b>The Prophetic Narration:</b>\n"
                            + "The Messenger of Allah ﷺ said:\n"
                            + "\"إِنَّمَا يُرْزَقُ الْعَبْدُ عَلَى قَدَرِ نِيَّتِهِ\"\n\n"
                            + "\"A servant is provided sustenance in proportion to their intention.\"\n"
                            + "<b>[Musnad Ahmad]</b>"
            ));

            // 8. ঈসমাঈল (আ.) কে কুরবানির স্বপ্ন
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "ঈসমাঈল (আ.) কে কুরবানির স্বপ্ন",
                    "The Vision of Sacrificing Ismail AS",
                    "হযরত ইব্রাহিম (আলাইহিস সালাম) স্বপ্নে দেখেন যে তিনি তাঁর পুত্র ইসমাঈল (আলাইহিস সালাম)-কে আল্লাহর আদেশে কোরবানি করছেন। নবীদের স্বপ্ন ওহি হওয়ায়, তিনি তা পালন করতে উদ্যোগী হন। পিতা ও পুত্র উভয়েই আল্লাহর আদেশের প্রতি সম্পূর্ণ আনুগত্য প্রকাশ করেন। এই মহান ত্যাগ ও ইমানের পরীক্ষার উত্তরে আল্লাহ তাআলা ইসমাঈল (আলাইহিস সালাম)-এর পরিবর্তে জান্নাত থেকে একটি মহান কোরবানির পশু পাঠান।\n\n"
                            + "فَلَمَّا بَلَغَ مَعَهُ السَّعْيَ قَالَ يَا بُنَيَّ إِنِّي أَرَىٰ فِي ٱلْمَنَامِ أَنِّيٓ أَذْبَحُكَ فَٱنظُرْ مَاذَا تَرَىٰ ۚ قَالَ يَـٰٓأَبَتِ ٱفْعَلْ مَا تُؤْمَرُ ۖ سَتَجِدُنِىٓ إِن شَآءَ ٱللَّهُ مِنَ ٱلصَّـٰبِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর যখন সে (ইসমাঈল) তার পিতার সঙ্গে চলাফেরা করার বয়সে পৌঁছল, তখন ইব্রাহিম বললেন, ‘হে বৎস! আমি স্বপ্নে দেখি যে, আমি তোমাকে কোরবানি করছি; এখন বলো, তুমি কী মনে করো?’ সে বলল, ‘হে আমার পিতা! আপনি যা আদিষ্ট হচ্ছেন, তা-ই করুন। ইনশাআল্লাহ, আপনি আমাকে ধৈর্যশীলদের অন্তর্ভুক্ত পাবেন।\n\n"
                            + "<b>[সূরা আস-সাফফাত, আয়াত ১০২]</b>\n\n"
                            + "فَلَمَّآ أَسْلَمَا وَتَلَّهُۥ لِلْجَبِينِ ١٠٣ وَنَـٰدَيْنَـٰهُ أَن يَـٰٓإِبْرَٰهِـۧمُ ١٠٤ قَدْ صَدَّقْتَ ٱلرُّءْيَآ ۚ إِنَّا كَذَٰلِكَ نَجْزِى ٱلْمُحْسِنِينَ ١٠٥ إِنَّ هَـٰذَا لَهُوَ ٱلْبَلَـٰٓؤُا۟ ٱلْمُبِينُ ١٠٦ وَفَدَيْنَـٰهُ بِذِبْحٍ عَظِيمٍۢ ١٠٧\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর যখন তারা উভয়ে আত্মসমর্পণ করল এবং ইব্রাহিম তাঁকে কাত করে শুইয়ে দিলেন, তখন আমি বললাম, ‘হে ইব্রাহিম! তুমি তো স্বপ্নকে সত্যে পরিণত করেছ।’ নিশ্চয়ই আমরা সৎকর্মশীলদের এভাবেই প্রতিদান দিয়ে থাকি। নিশ্চয় এটি ছিল এক স্পষ্ট পরীক্ষা। এবং আমি তাঁকে এক মহান কোরবানির মাধ্যমে মুক্ত করলাম।\n\n"
                            + "<b>[সূরা আস-সাফফাত, আয়াত ১০৩–১০৭]</b>\n\n"
                            + "إِنَّ أَعْظَمَ الأَيَّامِ عِندَ اللَّهِ تَعَالَى يَوْمُ النَّحْرِ، ثُمَّ يَوْمُ الْقَرِّ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আল্লাহর কাছে সবচেয়ে মহান দিন হলো কোরবানির দিন (ইয়াওমুন নাহর), এরপর ইয়াওমুল ক্বার।\n\n"
                            + "<b>[সুনানে আবু দাউদ, হাদীস: ১৭৬৫]</b>",
                    "Prophet Ibrahim (AS) experienced a vision commanding him to sacrifice his beloved son Ismail (AS). Since prophets' dreams are divine revelation, father and son demonstrated ultimate submission to the will of Allah.\n\n"
                            + "<b>The Dialogue of Total Submission:</b>\n"
                            + "فَلَمَّا بَلَغَ مَعَهُ السَّعْيَ قَالَ يَا بُنَيَّ إِنِّي أَرَىٰ فِي ٱلْمَنَامِ أَنِّيٓ أَذْبَحُكَ فَٱنظُرْ مَاذَا تَرَىٰ ۚ قَالَ يَـٰٓأَبَتِ ٱفْعَلْ مَا تُؤْمَرُ ۖ سَتَجِدُنِىٓ إِن شَآءَ ٱللَّهُ مِنَ ٱلصَّـٰبِرِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And when he reached with him [the age of] exertion, he said, 'O my son, indeed I have seen in a dream that I [must] sacrifice you, so see what you think.' He said, 'O my father, do as you are commanded. You will find me, if Allah wills, of the steadfast.'\"\n"
                            + "<b>[Surah As-Saffat 37:102]</b>\n\n"
                            + "<b>The Great Ransom from Paradise:</b>\n"
                            + "فَلَمَّآ أَسْلَمَا وَتَلَّهُۥ لِلْجَبِينِ ١٠٣ وَنَـٰدَيْنَـٰهُ أَن يَـٰٓإِبْرَٰهِـۧمُ ١٠٤ قَدْ صَدَّقْتَ ٱلرُّءْيَآ ۚ إِنَّا كَذَٰلِكَ نَجْزِى ٱلْمُحْسِنِينَ ١٠٥ إِنَّ هَـٰذَا لَهُوَ ٱلْبَلَـٰٓؤُا۟ ٱلْمُبِينُ ١٠٦ وَفَدَيْنَـٰهُ بِذِبْحٍ عَظِيمٍۢ ١٠٧\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And when they had both submitted and he put him down upon his forehead, We called to him, 'O Abraham, You have fulfilled the vision.' Indeed, We thus reward the doers of good. Indeed, this was the clear trial. And We ransomed him with a great sacrifice.'\"\n"
                            + "<b>[Surah As-Saffat 37:103-107]</b>\n\n"
                            + "<b>Sacred Virtue of the Day of Sacrifice:</b>\n"
                            + "إِنَّ أَعْظَمَ الأَيَّامِ عِندَ اللَّهِ تَعَالَى يَوْمُ النَّحْرِ، ثُمَّ يَوْمُ الْقَرِّ\n\n"
                            + "\"The greatest of days before Allah is the Day of Sacrifice (Yawm an-Nahr), followed by the Day of Settlement.\"\n"
                            + "<b>[Sunan Abi Dawud 1765]</b>"
            ));

            // 9. কাবা শরীফ নির্মাণ
            list.add(new ProphetOverviewTopicItem(
                    9,
                    "কাবা শরীফ নির্মাণ",
                    "Construction of the Holy Ka'bah",
                    "হযরত ইব্রাহিম (عَلَيْهِ ٱلسَّلَامُ) কা‘বা শরীফের পুনর্নির্মাণ করেছেন তাঁর পুত্র হযরত ইসমাঈল (عَلَيْهِ ٱلسَّلَامُ)-এর সঙ্গে। এটি ছিল আল্লাহর নির্দেশে একটি পবিত্র কাজ, যাতে তাওহীদের দাওয়াত এবং ইবাদতের কেন্দ্র প্রতিষ্ঠিত হয়। এই ঘটনা পবিত্র কুরআনে স্পষ্টভাবে বর্ণিত হয়েছে।\n\n"
                            + "وَإِذْ يَرْفَعُ إِبْرَٰهِيمُ ٱلْقَوَاعِدَ مِنَ ٱلْبَيْتِ وَإِسْمَـٰعِيلُ ۖ رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنتَ ٱلسَّمِيعُ ٱلْعَلِيمُ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর স্মরণ করো, যখন ইব্রাহিম ও ইসমাঈল কা‘বাগৃহের ভিত্তি উত্তোলন করছিলেন এবং বলছিলেন, ‘হে আমাদের রব! আপনি আমাদের পক্ষ থেকে (এই কাজ) কবুল করে নিন। নিশ্চয়ই আপনি সর্বশ্রোতা, সর্বজ্ঞ।\n\n"
                            + "<b>[সূরা আল-বাকারা, আয়াত ১২৭]</b>\n\n"
                            + "وَإِذْ جَعَلْنَا ٱلْبَيْتَ مَثَابَةً لِّلنَّاسِ وَأَمْنًا ۖ وَٱتَّخِذُوا۟ مِن مَّقَامِ إِبْرَٰهِيمَ مُصَلًّى ۖ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর যখন আমি কা‘বাগৃহকে মানুষের জন্য সম্মিলনস্থল ও নিরাপদ স্থান করলাম এবং আদেশ দিলাম তোমরা ইব্রাহিমের দাঁড়ানোর স্থানকে নামাযের স্থান হিসেবে গ্রহণ করো।\n\n"
                            + "<b>[সূরা আল-বাকারা, আয়াত ১২৫]</b>\n\n"
                            + "<b>বিষয় বিশ্লেষণ:</b>\n\n"
                            + "• <b>আল্লাহর হুকুমে নির্মাণ:</b> ইব্রাহিম (عليه السلام) আল্লাহর নির্দেশে কা‘বা শরীফ নির্মাণ করেন। এটি ছিল আল্লাহর ঘর, যা একমাত্র তাঁর ইবাদতের জন্য নির্ধারিত।\n"
                            + "• <b>ইসমাঈল (عليه السلام)-এর সহযোগিতা:</b> ইব্রাহিম (عليه السلام) কাজ করতেন, আর ইসমাঈল (عليه السلام) পাথর এনে দিতেন। এভাবে পিতা-পুত্র মিলে ইবাদতের ঘর নির্মাণে অংশ নেন।\n\n"
                            + "<b>দোয়া ও ইখলাস:</b>\n\n"
                            + "নির্মাণ কাজের সময় তাঁরা বারবার আল্লাহর দরবারে দোয়া করতেন যেন কাজটি কবুল হয়। সহীহ বুখারী তে এসেছে,\n\n"
                            + "قال النبي ﷺ: \"إن هذا البلد حرّمه الله يوم خلق السماوات والأرض...\"\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "নবী ﷺ বলেন, ‘নিশ্চয়ই এই নগর (মক্কা) আল্লাহ পবিত্র করেছেন যেদিন আসমান ও জমিন সৃষ্টি করেছেন। এটি কিয়ামত পর্যন্ত আল্লাহর পক্ষ থেকে পবিত্র থাকবে।\n\n"
                            + "<b>[সহীহ বুখারী, হাদীস: ১৮৩৩]</b>",
                    "By divine decree, Prophet Ibrahim (AS) and his son Prophet Ismail (AS) rebuilt the foundations of the Holy Ka'bah in Makkah as a sanctuary and global center of monotheism.\n\n"
                            + "<b>Raising the Foundations of the Sanctuary:</b>\n"
                            + "وَإِذْ يَرْفَعُ إِبْرَٰهِيمُ ٱلْقَوَاعِدَ مِنَ ٱلْبَيْتِ وَإِسْمَـٰعِيلُ ۖ رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنتَ ٱلسَّمِيعُ ٱلْعَلِيمُ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And [mention] when Abraham was raising the foundations of the House and Ishmael, [saying], 'Our Lord, accept [this] from us. Indeed You are the Hearing, the Knowing.'\"\n"
                            + "<b>[Surah Al-Baqarah 2:127]</b>\n\n"
                            + "وَإِذْ جَعَلْنَا ٱلْبَيْتَ مَثَابَةً لِّلنَّاسِ وَأَمْنًا ۖ وَٱتَّخِذُوا۟ مِن مَّقَامِ إِبْرَٰهِيمَ مُصَلًّى ۖ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And [mention] when We made the House a place of return for the people and [a place of] security. And take, [O believers], from the standing place of Abraham a place of prayer.'\"\n"
                            + "<b>[Surah Al-Baqarah 2:125]</b>\n\n"
                            + "<b>Sanctity of Makkah:</b>\n"
                            + "The Messenger of Allah ﷺ said:\n"
                            + "قال النبي ﷺ: \"إن هذا البلد حرّمه الله يوم خلق السماوات والأرض...\"\n\n"
                            + "\"Indeed, this city was declared sacred by Allah on the day He created the heavens and the earth, and it remains sacred by Allah's sanctity until the Day of Resurrection.\"\n"
                            + "<b>[Sahih al-Bukhari 1833]</b>"
            ));

            // 10. উম্মতের জন্য দোআ ও রাসূল (সা.)-এর আগমনের সুসংবাদ
            list.add(new ProphetOverviewTopicItem(
                    10,
                    "উম্মতের জন্য দোআ ও রাসূল (সা.)-এর আগমনের সুসংবাদ",
                    "Dua for the Ummah and the Glad Tidings of Prophet Muhammad SAW",
                    "হযরত ইব্রাহিম (عَلَيْهِ ٱلسَّلَامُ) ছিলেন এক দূরদর্শী, দয়ালু ও পরিপূর্ণ ঈমানদার নবী, যিনি শুধু নিজের জন্য নয়, Futures উম্মতের জন্যও দোয়া করেছিলেন। কুরআন মাজিদে তাঁর অনেক দোয়া উল্লেখ করা হয়েছে, যার মধ্যে অন্যতম হলো তাঁর দোয়া — ভবিষ্যতে একজন রাসূল প্রেরণের আবেদন, যিনি এই উম্মতের মাঝে এসে আল্লাহর আয়াত তিলাওয়াত করবেন, তাদেরকে পবিত্র করবেন এবং কিতাব ও হিকমাহ শিক্ষা দেবেন।\n\n"
                            + "وَإِذْ يَرْفَعُ إِبْرَٰهِيمُ ٱلْقَوَاعِدَ مِنَ ٱلْبَيْتِ وَإِسْمَـٰعِيلُ ۖ رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنتَ ٱلسَّمِيعُ ٱلْعَلِيمُ (١٢٧)\n\n"
                            + "رَبَّنَا وَٱجْعَلْنَا مُسْلِمَيْنِ لَكَ وَمِن ذُرِّيَّتِنَآ أُمَّةًۭ مُّسْلِمَةًۭ لَّكَ ۖ وَأَرِنَا مَنَاسِكَنَا وَتُبْ عَلَيْنَآ ۖ إِنَّكَ أَنتَ ٱلتَّوَّابُ ٱلرَّحِيمُ (١٢٨)\n\n"
                            + "رَبَّنَا وَٱبْعَثْ فِيهِمْ رَسُولًۭا مِّنْهُمْ يَتْلُوا۟ عَلَيْهِمْ ءَايَـٰتِكَ وَيُعَلِّمُهُمُ ٱلْكِتَـٰبَ وَٱلْحِكْمَةَ وَيُزَكِّيهِمْ ۚ إِنَّكَ أَنتَ ٱلْعَزِيزُ ٱلْحَكِيمُ (١٢٩)\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর যখন ইব্রাহিম ও ইসমাঈল কা‘বাগৃহের ভিত্তি উত্তোলন করছিলেন তখন তারা বলছিলেন: ‘হে আমাদের রব! আমাদের পক্ষ থেকে এই (আমল) কবুল করে নিন। নিশ্চয় আপনি সর্বশ্রোতা, সর্বজ্ঞ।’ হে আমাদের রব! আমাদেরকে আপনার অনুগত করুন, এবং আমাদের বংশধরদের মধ্য থেকে এক উম্মত সৃষ্টি করুন যারা আপনার অনুগত হবে। আর আমাদের ইবাদতের পদ্ধতি দেখিয়ে দিন এবং আমাদেরকে ক্ষমা করুন। নিশ্চয় আপনি তাওবা কবুলকারী, দয়ালু। হে আমাদের রব! তাদের মাঝে তাদেরই একজন রাসূল পাঠান, যিনি তাদের নিকট আপনার আয়াতসমূহ পাঠ করবেন, কিতাব ও হিকমাহ শিক্ষা দেবেন এবং তাদেরকে পরিশুদ্ধ করবেন। নিশ্চয় আপনি পরাক্রমশালী ও প্রজ্ঞাময়।\n\n"
                            + "<b>[সূরা আল-বাকারা – ১২৭–১২৯]</b>\n\n"
                            + "<b>এই আয়াতগুলোর আলোকে কিছু মূল শিক্ষা:</b>\n\n"
                            + "• <b>উম্মতের জন্য দোয়া:</b> হযরত ইব্রাহিম (عليه السلام) তাঁর পরবর্তী বংশধরদের জন্য দোয়া করেন, যেন তারা মুসলিম (আল্লাহর অনুগত) হয়।\n"
                            + "• <b>নবী পাঠানোর দোয়া:</b> তিনি আল্লাহর কাছে আবেদন করেন, যেন এই উম্মতের মধ্যে থেকে একজন রাসূল পাঠানো হয়, যিনি তাদেরকে হিদায়াত দিবেন এবং আত্মিক ও বুদ্ধিবৃত্তিক শুদ্ধতা অর্জনে সাহায্য করবেন।",
                    "Prophet Ibrahim (AS) was a deeply compassionate and farsighted prophet who prayed not only for his immediate family but for the entire future Muslim Ummah and the commissioning of the Final Messenger ﷺ.\n\n"
                            + "<b>The Sublime Quranic Prayers:</b>\n"
                            + "وَإِذْ يَرْفَعُ إِبْرَٰهِيمُ ٱلْقَوَاعِدَ مِنَ ٱلْبَيْتِ وَإِسْمَـٰعِيلُ ۖ رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنتَ ٱلسَّمِيعُ ٱلْعَلِيمُ (١٢٧)\n"
                            + "رَبَّنَا وَٱجْعَلْنَا مُسْلِمَيْنِ لَكَ وَمِن ذُرِّيَّتِنَآ أُمَّةًۭ مُّسْلِمَةًۭ لَّكَ ۖ وَأَرِنَا مَنَاسِكَنَا وَتُبْ عَلَيْنَآ ۖ إِنَّكَ أَنتَ ٱلتَّوَّابُ ٱلرَّحِيمُ (١٢٨)\n"
                            + "رَبَّنَا وَٱبْعَثْ فِيهِمْ رَسُولًۭا مِّنْهُمْ يَتْلُوا۟ عَلَيْهِمْ ءَايَـٰتِكَ وَيُعَلِّمُهُمُ ٱلْكِتَـٰبَ وَٱلْحِكْمَةَ وَيُزَكِّيهِمْ ۚ إِنَّكَ أَنتَ ٱلْعَزِيزُ ٱلْحَكِيمُ (١٢٩)\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And when Abraham was raising the foundations of the House and Ishmael, [saying], 'Our Lord, accept [this] from us. Indeed You are the Hearing, the Knowing.'\n"
                            + "'Our Lord, and make us submissive [Muslims] to You and from our descendants a Muslim nation [in submission] to You. And show us our rites and accept our repentance. Indeed, You are the Accepting of repentance, the Merciful.'\n"
                            + "'Our Lord, and send among them a messenger from themselves who will recite to them Your verses and teach them the Book and wisdom and purify them. Indeed, You are the Exalted in Might, the Wise.'\"\n"
                            + "<b>[Surah Al-Baqarah 2:127-129]</b>\n\n"
                            + "<b>Core Lessons:</b>\n"
                            + "• <b>Supplicating for Future Generations:</b> Praying that descendants remain steadfast in Islam.\n"
                            + "• <b>Fulfillment through Prophet Muhammad ﷺ:</b> The blessed Prophet Muhammad ﷺ is the living response to Ibrahim's (AS) historic prayer."
            ));

            // 11. মৃত্যু ও পরবর্তী বংশধর
            list.add(new ProphetOverviewTopicItem(
                    11,
                    "মৃত্যু ও পরবর্তী বংশধর",
                    "Passing and Righteous Descendants",
                    "হযরত ইব্রাহিম (عليه السلام) ইসলামের একজন মহান নবী, যিনি “خَلِيلُ ٱللَّهِ” (আল্লাহর বন্ধু) হিসেবে খ্যাত। তিনি ছিলেন তাওহীদের অন্যতম প্রধান প্রচারক এবং অনেক নবীর বংশপিতা। তাঁর জীবন যেমন ছিল দাওয়াত, ত্যাগ ও তাওয়াক্কুলে পূর্ণ — তেমনি তাঁর মৃত্যুও ছিল সম্মানজনক ও শান্তিপূর্ণ। কুরআন মাজীদ ও ইসলামী ইতিহাসে তাঁর মৃত্যুর পরবর্তী বংশধরদের সম্পর্কে সুস্পষ্ট তথ্য পাওয়া যায়।\n\n"
                            + "وَوَهَبْنَا لَهُۥٓ إِسْحَـٰقَ وَيَعْقُوبَ ۚ كُلًّۭا هَدَيْنَا ۚ وَنُوحًۭا هَدَيْنَا مِن قَبْلُ ۖ وَمِن ذُرِّيَّتِهِۦ دَاوُۥدَ وَسُلَيْمَـٰنَ وَأَيُّوبَ وَيُوسُفَ وَمُوسَىٰ وَهَـٰرُونَ ۚ وَكَذَٰلِكَ نَجْزِى ٱلْمُحْسِنِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমি তাঁকে (ইব্রাহিমকে) দান করেছিলাম ইসহাক ও ইয়াকুব। প্রত্যেককেই আমি হিদায়াত দিয়েছিলাম। আর পূর্বে আমি নূহকে হিদায়াত দিয়েছিলাম। আর তাঁর বংশধরদের মধ্যে দাউদ, সুলায়মান, আইউব, ইউসুফ, মূসা ও হারুন— এসবকেই (পাঠিয়েছি)। আর এভাবেই আমি সৎকর্মশীলদের পুরস্কৃত করে থাকি।”\n\n"
                            + "<b>[সূরা আল-আন‘আম – ৮৪]</b>\n\n"
                            + "<b>হযরত ইব্রাহিম (عليه السلام) এর মৃত্যু:</b>\n\n"
                            + "ইসলামী ঐতিহ্য ও ইতিহাস অনুযায়ী,\n\n"
                            + "• হযরত ইব্রাহিম (عليه السلام) দীর্ঘ জীবন লাভ করেন (প্রায় ১৭৫ বছর)।\n"
                            + "• তিনি হেবরনে (ফিলিস্তিনের একটি শহর) ইন্তিকাল করেন।\n"
                            + "• তাঁকে হযরত ইসহাক (عليه السلام) দাফন করেন হযরত সারাহ (عليها السلام)-এর পাশে — \"مغارة المكفيلة\" (Machpelah Cave)-তে, যা আজও ঐতিহাসিক স্থান হিসেবে বিদ্যমান।\n"
                            + "• হাদীসে বা কুরআনে তাঁর মৃত্যুর নির্দিষ্ট সাল বা সময় উল্লেখ নেই, তবে ইতিহাসবিদরা এই তথ্য দিয়ে থাকেন।\n\n"
                            + "<b>তাঁর উল্লেখযোগ্য বংশধর:</b>\n\n"
                            + "হযরত ইব্রাহিম (عليه السلام)-এর দুই প্রধান সন্তান:\n\n"
                            + "• <b>ইসমাঈল (عليه السلام):</b>\n"
                            + "  মা: হাজেরা (عليها السلام)\n"
                            + "  বংশধর: আরব জাতি\n"
                            + "  এই বংশ থেকেই নবী মুহাম্মাদ ﷺ আগমন করেন\n"
                            + "  <b>[সূরা আস-সাফফাত – ১০১]</b>\n\n"
                            + "• <b>ইসহাক (عليه السلام):</b>\n"
                            + "  মা: সারাহ (عليها السلام)\n"
                            + "  বংশধর: ইয়াকুব (عليه السلام), যাঁকে “ইসরাইল” বলা হয়\n"
                            + "  ইসরাঈলি নবীদের মূল শাখা — যেমন: ইউসুফ, মূসা, হারুন, দাউদ, সুলায়মান, ঈসা (عليهم السلام)",
                    "Prophet Ibrahim (AS) lived a life filled with steadfast devotion, sacrifice, and pure monotheism. His legacy continues through his righteous offspring.\n\n"
                            + "<b>Divine Blessings upon His Lineage:</b>\n"
                            + "وَوَهَبْنَا لَهُۥٓ إِسْحَـٰقَ وَيَعْقُوبَ ۚ كُلًّۭا هَدَيْنَا ۚ وَنُوحًۭا هَدَيْنَا مِن قَبْلُ ۖ وَمِن ذُرِّيَّتِهِۦ دَاوُۥدَ وَسُلَيْمَـٰنَ وَأَيُّوبَ وَيُوسُفَ وَمُوسَىٰ وَهَـٰرُونَ ۚ وَكَذَٰلِكَ نَجْزِى ٱلْمُحْسِنِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "\"And We gave to Abraham, Isaac and Jacob - all [of them] We guided. And Noah, We guided before; and among his descendants, David and Solomon and Job and Joseph and Moses and Aaron. Thus do We reward the doers of good.\"\n"
                            + "<b>[Surah Al-An'am 6:84]</b>\n\n"
                            + "<b>Passing and Resting Place:</b>\n"
                            + "According to Islamic history, Prophet Ibrahim (AS) lived a long life (around 175 years) and passed away peacefully in Hebron (Al-Khalil, Palestine). He was laid to rest by his son Ishaq (AS) alongside Lady Sarah (AS) at the Cave of Machpelah (Al-Haram Al-Ibrahimi).\n\n"
                            + "<b>Distinguished Lineages:</b>\n"
                            + "• <b>Ismail (AS):</b> Born to Lady Hajar (AS), father of the Arab nation and ancestor of Prophet Muhammad ﷺ.\n"
                            + "• <b>Ishaq (AS):</b> Born to Lady Sarah (AS), father of Yaqub (AS) (Israel) and forefather of the noble prophets of Bani Isra'il."
            ));
        } else if (prophetId == 7) {
            // Prophet Lut (AS) - 9 Complete Topics (100% Verbatim)
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "জন্ম, বংশধারা ও পরিচয়",
                    "Birth, Lineage and Identity",
                    "হযরত লূত (আলাইহিস সালাম) ছিলেন নবী ইবরাহিম (আলাইহিস সালাম)-এর ভাইয়ের ছেলে। ইসলামী ঐতিহ্য অনুযায়ী, লূত (আঃ)-এর পিতা ছিলেন হারান (هَارَان), যিনি ইবরাহিম (আঃ)-এর ছোট ভাই। অর্থাৎ, আদম (আঃ) → শীষ (আঃ) → তেরাহ (آزر / تارح) → হারান → লূত (আঃ)\n\n"
                            + "<b>বাসস্থান ও জন্মস্থান:</b>\n"
                            + "ইরাকের বাবেল অঞ্চলে বা আশেপাশে, যেখানে ইবরাহিম (আঃ) বসবাস করতেন। তিনি কিশোর বয়সেই ইবরাহিম (আঃ)-এর দাওয়াত গ্রহণ করেন এবং তাঁর ঘনিষ্ঠ সঙ্গী হন।",
                    "Prophet Lut (peace be upon him) was the nephew of Prophet Ibrahim (peace be upon him). According to Islamic tradition, the father of Lut (AS) was Haran (هَارَان), the younger brother of Ibrahim (AS). Lineage: Adam (AS) → Sheeth (AS) → Terah (Azar / Tarih) → Haran → Lut (AS).\n\n"
                            + "<b>Place of Birth and Residence:</b>\n"
                            + "Born in the region of Babylon (Babil) in ancient Mesopotamia (modern-day Iraq) where Ibrahim (AS) lived. In his youth, he embraced the call of Ibrahim (AS) and became his close companion."
            ));

            list.add(new ProphetOverviewTopicItem(
                    2,
                    "ইবরাহীম (আঃ)-এর সঙ্গে হিজরত ও তাওহিদের দাওয়াত গ্রহণ",
                    "Migration with Ibrahim AS and Embracing Tawheed",
                    "হযরত লূত (আঃ) ছিলেন হযরত ইব্রাহিম (আঃ)-এর ভাগিনা (ভ্রাতুষ্পুত্র)। তিনি ছিলেন প্রথম দিককার ঈমানদারদের মধ্যে অন্যতম এবং হযরত ইব্রাহিম (আঃ)-এর দাওয়াতের প্রতি সাড়া দিয়ে তাওহিদ গ্রহণ করেন। তিনি শুধু ঈমানই আনেননি, বরং হযরত ইব্রাহিম (আঃ)-এর সঙ্গে হিজরতও করেন এবং পরে আল্লাহ তাঁকে নবুয়ত দান করেন。\n\n"
                            + "<b>লূত (আঃ)-এর ঈমান ও হিজরত:</b>\n\n"
                            + "فَآمَنَ لَهُ لُوطٌ ۘ وَقَالَ إِنِّي مُهَاجِرٌ إِلَىٰ رَبِّي ۖ إِنَّهُ هُوَ الْعَزِيزُ الْحَكِيمُ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর লূত তাঁর প্রতি ঈমান আনল। এবং সে বলল, ‘নিশ্চয়ই আমি আমার প্রতিপালকের দিকে হিজরত করছি। নিশ্চয় তিনি পরাক্রমশালী, প্রজ্ঞাময়।\n\n"
                            + "<b>[সূরা আল-আনকাবূত – ২৬]</b>\n\n"
                            + "এই আয়াত থেকে বুঝা যায় যে, লূত (আঃ) হযরত ইব্রাহিম (আঃ)-এর প্রতি ঈমান আনেন এবং তাঁর সঙ্গেই হিজরত করেন। হিজরতের মাধ্যমে তাঁরা তাওহিদের দাওয়াতকে প্রতিষ্ঠিত করার লক্ষ্যে শামের দিকে পাড়ি জমান。\n\n"
                            + "<b>লূত (আঃ)-কে নবুয়ত দান:</b>\n\n"
                            + "হিজরতের পর আল্লাহ তাআলা তাঁকে নবুয়ত দান করেন এবং তাঁকে একটি গর্হিতকর্মে লিপ্ত জাতির প্রতি প্রেরণ করেন। তিনি ছিলেন একমাত্র নবী যিনি সমকামিতার মতো গুরুতর অশ্লীল কাজের বিরুদ্ধে আল্লাহর পক্ষ থেকে দাওয়াত নিয়ে এসেছিলেন。\n\n"
                            + "وَلُوطًا آتَيْنَاهُ حُكْمًا وَعِلْمًا وَنَجَّيْنَاهُ مِنَ الْقَرْيَةِ الَّتِي كَانَتْ تَعْمَلُ الْخَبَائِثَ ۚ إِنَّهُمْ كَانُوا قَوْمَ سَوْءٍ فَاسِقِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর লূতকে আমি দিয়েছিলাম বিচারক্ষমতা ও জ্ঞান, এবং আমি তাঁকে রক্ষা করেছিলাম সেই জনপদ থেকে যারা অশ্লীল কাজ করত। নিশ্চয় তারা ছিল দুর্বৃত্ত, পাপাচারী সম্প্রদায়。\n\n"
                            + "<b>[সূরা আল-আনবিয়া – ৭৪]</b>\n\n"
                            + "<b>লূত (আঃ)-এর তাওহিদের দাওয়াত:</b>\n\n"
                            + "লূত (আঃ) তাঁর সম্প্রদায়কে তাওহিদ ও নৈতিকতার দাওয়াত দেন। তিনি তাঁদের শিরক, অশ্লীলতা ও সীমালঙ্ঘনের বিরুদ্ধে সতর্ক করেন。\n\n"
                            + "أَتَأْتُونَ الْفَاحِشَةَ مَا سَبَقَكُم بِهَا مِنْ أَحَدٍ مِّنَ الْعَالَمِينَ\n\n"
                            + "أَئِنَّكُمْ لَتَأْتُونَ الرِّجَالَ وَتَقْطَعُونَ السَّبِيلَ وَتَأْتُونَ فِي نَادِيكُمُ الْمُنكَرَ ۖ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা কি এমন অশ্লীল কাজ করছো যা তোমাদের পূর্বে জগতে কেউ করে নি? তোমরা কি পুরুষদের কাছে প্রবৃত্তি পূরণের জন্য গমন করো, রাস্তা বন্ধ করো এবং তোমাদের মিলনস্থলে অশ্লীল কার্য করো?\n\n"
                            + "<b>[সূরা আল-আ‘রাফ – ৮০–৮১; সূরা আনকাবূত – ২৯]</b>",
                    "Prophet Lut (AS) was among the earliest believers who accepted the monotheistic call (Tawheed) of Prophet Ibrahim (AS). He not only believed but also migrated alongside Ibrahim (AS), and subsequently, Allah conferred Prophethood upon him.\n\n"
                            + "<b>Faith and Migration of Lut (AS):</b>\n\n"
                            + "فَآمَنَ لَهُ لُوطٌ ۘ وَقَالَ إِنِّي مُهَاجِرٌ إِلَىٰ رَبِّي ۖ إِنَّهُ هُوَ الْعَزِيزُ الْحَكِيمُ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And Lut believed in him. He said, 'Indeed, I will emigrate to [the service of] my Lord. Indeed, He is the Exalted in Might, the Wise.'\n\n"
                            + "<b>[Surah Al-Ankabut: 29:26]</b>\n\n"
                            + "<b>Conferment of Prophethood:</b>\n\n"
                            + "After their migration towards the Levant (Sham), Allah bestowed Prophethood upon Lut (AS) and sent him as a messenger to a corrupt people indulging in unprecedented immoralities.\n\n"
                            + "وَلُوطًا آتَيْنَاهُ حُكْمًا وَعِلْمًا وَنَجَّيْنَاهُ مِنَ الْقَرْيَةِ الَّتِي كَانَتْ تَعْمَلُ الْخَبَائِثَ ۚ إِنَّهُمْ كَانُوا قَوْمَ سَوْءٍ فَاسِقِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And to Lut We gave judgement and knowledge, and We saved him from the city that was committing wicked deeds. Indeed, they were a people of evil and defiantly disobedient.\n\n"
                            + "<b>[Surah Al-Anbiya: 21:74]</b>\n\n"
                            + "<b>The Call to Tawheed & Moral Rectitude:</b>\n\n"
                            + "أَتَأْتُونَ الْفَاحِشَةَ مَا سَبَقَكُم بِهَا مِنْ أَحَدٍ مِّنَ الْعَالَمِينَ\n\n"
                            + "أَئِنَّكُمْ لَتَأْتُونَ الرِّجَالَ وَتَقْطَعُونَ السَّبِيلَ وَتَأْتُونَ فِي نَادِيكُمُ الْمُنكَرَ ۖ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "Do you commit such immorality as no one has preceded you with from among the worlds? Indeed, you approach men and obstruct the road and commit in your meetings evil?\n\n"
                            + "<b>[Surah Al-A'raf: 7:80-81; Surah Al-Ankabut: 29:29]</b>"
            ));

            list.add(new ProphetOverviewTopicItem(
                    3,
                    "সদোমবাসীর প্রতি নবুয়ত লাভ",
                    "Prophethood to the People of Sodom",
                    "হযরত লূত (عليه السلام) ছিলেন একজন সম্মানিত নবী, যিনি হযরত ইব্রাহিম (عليه السلام) এর ঘনিষ্ঠ এবং প্রথম দিককার ঈমানদার। হিজরতের পর আল্লাহ ﷻ তাঁকে সদোম ও আশেপাশের জনপদসমূহ (সাদূম ও গোমোরাহ) এর জনগণের প্রতি রাসূল ও নবী হিসেবে প্রেরণ করেন। এই সম্প্রদায় ছিল বিশেষভাবে অশ্লীল কর্মে লিপ্ত, বিশেষ করে সমকামিতা, যা মানব ইতিহাসে আগে কেউ করে নি。\n\n"
                            + "<b>কুরআনের আলোকে নবুওয়াত লাভ ও দাওয়াত:</b>\n\n"
                            + "وَلُوطًا إِذْ قَالَ لِقَوْمِهِۦٓ أَتَأْتُونَ ٱلْفَـٰحِشَةَ مَا سَبَقَكُم بِهَا مِنْ أَحَدٍۢ مِّنَ ٱلْعَـٰلَمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর (স্মরণ করো) লূতকে, যখন তিনি তাঁর সম্প্রদায়কে বলেছিলেন, ‘তোমরা কি এমন অশ্লীল কাজ করছো, যা তোমাদের পূর্বে জগতের কেউ করে নি?\n\n"
                            + "<b>[সূরা আল-আ‘রাফ – ৮০]</b>\n\n"
                            + "إِنَّكُمْ لَتَأْتُونَ ٱلرِّجَالَ شَهْوَةًۭ مِّن دُونِ ٱلنِّسَآءِ ۚ বَلْ أَنتُمْ قَوْمٌۭ مُّسْرِفُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা কি নারীদের ছেড়ে পুরুষদের কামনার বসে গমন করো? বরং তোমরা সীমালঙ্ঘনকারী সম্প্রদায়。\n\n"
                            + "<b>[সূরা আল-আ‘রাফ – ৮১]</b>\n\n"
                            + "<b>নবুওয়াত লাভের উদ্দেশ্য:</b>\n\n"
                            + "হযরত লূত (আঃ)–এর দায়িত্ব ছিল,\n\n"
                            + "• <b>তাওহিদের দাওয়াত দেওয়া:</b> আল্লাহ ছাড়া অন্য কারো ইবাদত না করার প্রতি আহ্বান।\n"
                            + "• <b>নৈতিকতার শিক্ষা দেওয়া:</b> অশ্লীলতা, বিশেষত পুরুষদের প্রতি কামাচার ও সমকামিতার বিরুদ্ধে কড়া হুঁশিয়ারি।\n"
                            + "• <b>সতর্ক ও সাবধান করা:</b> আল্লাহর আজাব সম্পর্কে সাবধান করা এবং পরিশুদ্ধ জীবন যাপনের দাওয়াত দেওয়া。\n\n"
                            + "কুরআনে তাঁর সম্প্রদায় সম্পর্কে আরও এসেছে,\n\n"
                            + "وَلُوطًا ءَاتَيْنَـٰهُ حُكْمًۭا وَعِلْمًۭا وَنَجَّيْنَـٰهُ مِنَ ٱلْقَرْيَةِ ٱلَّتِى كَانَت تَّعْمَلُ ٱلْخَبَـٰٓئِثَ ۚ إِنَّهُمْ كَانُوا۟ قَوْمَ سَوْءٍۢ فَـٰסِقِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর লূতকে আমি দিয়েছিলাম বিচারক্ষমতা ও জ্ঞান, এবং আমি তাঁকে রক্ষা করেছিলাম সেই জনপদ থেকে যারা অশ্লীল কাজ করত। নিশ্চয় তারা ছিল এক মন্দ, পাপাচারী সম্প্রদায়。\n\n"
                            + "<b>[সূরা আল-আনবিয়া – ৭৪]</b>",
                    "Prophet Lut (AS) was sent by Allah as a noble Messenger to the inhabitants of Sodom and Gomorrah. This population was steeped in unprecedented transgressions, highway robbery, and homosexual practices previously unknown to humanity.\n\n"
                            + "<b>Quranic Evidence of His Mission:</b>\n\n"
                            + "وَلُوطًا إِذْ قَالَ لِقَوْمِهِۦٓ أَتَأْتُونَ ٱلْفَـٰحِشَةَ مَا سَبَقَكُم بِهَا مِنْ أَحَدٍۢ مِّنَ ٱلْعَـٰلَمِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And [We had sent] Lut when he said to his people, 'Do you commit such immorality as no one has preceded you with from among the worlds?'\n\n"
                            + "<b>[Surah Al-A'raf: 7:80]</b>\n\n"
                            + "إِنَّكُمْ لَتَأْتُونَ ٱلرِّجَالَ شَهْوَةًۭ مِّن دُونِ ٱلنِّسَآءِ ۚ بَلْ أَنتُمْ قَوْمٌۭ مُّسْرِفُونَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "'Indeed, you approach men with desire, instead of women. Rather, you are a transgressing people.'\n\n"
                            + "<b>[Surah Al-A'raf: 7:81]</b>\n\n"
                            + "<b>Core Objectives of Prophethood:</b>\n\n"
                            + "• <b>Call to Tawheed:</b> Preaching absolute devotion and obedience to Allah alone.\n"
                            + "• <b>Moral Awakening:</b> Stern warning against homosexuality, lustful deviance, and societal degradation.\n"
                            + "• <b>Warning of Retribution:</b> Guiding people toward purity before divine punishment struck.\n\n"
                            + "وَلُوطًا ءَاتَيْنَـٰهُ حُكْمًۭا وَعِلْمًۭا وَنَجَّيْنَـٰهُ مِنَ ٱلْقَرْيَةِ ٱلَّتِى كَانَت تَّعْمَلُ ٱلْخَبَـٰٓئِثَ ۚ إِنَّهُمْ كَانُوا۟ قَوْمَ سَوْءٍۢ فَـٰسِقِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And to Lut We gave judgement and knowledge, and We saved him from the city that was committing wicked deeds. Indeed, they were a people of evil and defiantly disobedient.\n\n"
                            + "<b>[Surah Al-Anbiya: 21:74]</b>"
            ));

            list.add(new ProphetOverviewTopicItem(
                    4,
                    "সমকামিতার বিরুদ্ধে সংগ্রাম",
                    "Struggle Against Homosexuality and Immorality",
                    "হযরত লূত (عليه السلام) ছিলেন এমন একজন নবী, যিনি পৃথিবীর ইতিহাসে সর্বপ্রথম সমকামিতার মতো জঘন্য অশ্লীলতার বিরুদ্ধে সরাসরি সংগ্রাম করেন। তিনি আল্লাহর নির্দেশে সদোম ও আশেপাশের কয়েকটি শহরের জনগণকে তাওহিদের পাশাপাশি নৈতিকতা ও শালীনতার দাওয়াত দেন। কিন্তু তাঁর কওম নিজেদের কুপ্রবৃত্তি ও পাপাচারে এতটাই নিমজ্জিত ছিল যে, তারা লূত (আঃ)-এর দাওয়াত প্রত্যাখ্যান করে, বরং তাঁকে বিদ্রুপ ও হুমকি দিতে থাকে。\n\n"
                            + "<b>সমকামিতাকে “ফাহিশা” (চরম অশ্লীলতা) বলে আখ্যায়িত:</b>\n\n"
                            + "أَتَأْتُونَ ٱلْفَـٰحِشَةَ مَا سَبَقَكُم بِهَا مِنْ أَحَدٍۢ مِّنَ ٱلْعَـٰلَمِينَ (٨٠) إِنَّكُمْ لَتَأْتُونَ ٱلرِّجَالَ شَهْوَةًۭ مِّن دُونِ ٱلنِّسَآءِ ۚ বَلْ أَنتُمْ قَوْمٌۭ مُّسْرِفُونَ (٨١)\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা কি এমন অশ্লীল কাজ করছো, যা তোমাদের পূর্বে জগতে কেউ করে নি?\n\n"
                            + "তোমরা নারীদের ছেড়ে পুরুষদের কামনার বশে গমন করো? বরং তোমরা সীমালঙ্ঘনকারী এক সম্প্রদায়。\n\n"
                            + "<b>[সূরা আল-আ‘রাফ – ৮০–৮১]</b>\n\n"
                            + "<b>পাপী সম্প্রদায়ের প্রতিক্রিয়া:</b>\n\n"
                            + "হযরত লূত (আঃ) যখন সতর্ক করে দেন, তখন তাঁর সম্প্রদায় বলে,\n\n"
                            + "أَخْرِجُوهُم مِّن قَرْيَتِكُمْ ۖ إِنَّهُمْ أُنَاسٌۭ يَتَطَهَّরُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তাদের (লূতের পরিবারকে) তোমাদের জনপদ থেকে বের করে দাও! এরা তো নিজেদের খুব পবিত্রভাবে রাখতে চায়。\n\n"
                            + "<b>[সূরা আল-আ‘রাফ – ৮২]</b>\n\n"
                            + "এ কথার মাধ্যমে বোঝা যায়, তারা নৈতিকতা ও পবিত্রতাকেই অপছন্দ করত。\n\n"
                            + "<b>হযরত লূতের সরল ও দাওয়াতি ভাষা:</b>\n\n"
                            + "قَالَ يَـٰقَوْمِ ٱتَّقُوا۟ ٱللَّهَ مَا لَكُم مِّنْ إِلَـٰهٍ غَيْرُهُۥٓ ۚ إِنَّكُمْ لَتَأْتُونَ ٱلْفَـٰحِشَةَ وَلَا تَسْبِقُونَ بِهَا مِنْ أَحَدٍۢ مِّنَ ٱلْعَـٰلَمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি বললেন: ‘হে আমার সম্প্রদায়! তোমরা আল্লাহকে ভয় করো। তিনি ছাড়া তোমাদের আর কোনো উপাস্য নেই। তোমরা এমন অশ্লীল কাজ করছো যা তোমাদের আগে কেউ করে নি。\n\n"
                            + "<b>[সূরা আন-নামল – ৫৪]</b>\n\n"
                            + "তিনি বারবার আল্লাহভীতি ও তাওহিদের আহ্বান দিয়ে তাদেরকে সতর্ক করেছেন。\n\n"
                            + "<b>লূত (আঃ)-এর দুঃখ ও প্রার্থনা:</b>\n\n"
                            + "নিজ সম্প্রদায়ের বিরুদ্ধাচরণ দেখে তিনি গভীর দুঃখপ্রকাশ করে বলেন,\n\n"
                            + "قَالَ لَوْ أَنَّ لِى بِكُمْ قُوَّةً أَوْ ءَاوِىٓ إِلَىٰ رُكْنٍۢ شَدِيدٍۢ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি বললেন, ‘হায়! যদি আমার শক্তি থাকত তোমাদের মোকাবিলায় অথবা কোনো শক্তিশালী সহায় অবলম্বন করতে পারতাম。\n\n"
                            + "<b>[সূরা হূদ – ৮০]</b>\n\n"
                            + "এ থেকে বোঝা যায়, একজন নবীর কষ্ট ও মেহনত কত গভীর হতে পারে, যখন তার কওম আল্লাহর বিধান অমান্য করে।",
                    "Prophet Lut (AS) was the foremost prophet who directly confronted and challenged the epidemic of homosexuality and public obscenity. He tirelessly preached righteousness, but the transgressors mocked him and threatened exile.\n\n"
                            + "<b>Designating Immorality as 'Fahisha':</b>\n\n"
                            + "أَتَأْتُونَ ٱلْفَـٰحِشَةَ مَا سَبَقَكُم بِهَا مِنْ أَحَدٍۢ مِّنَ ٱلْعَـٰلَمِينَ (٨٠) إِنَّكُمْ لَتَأْتُونَ ٱلرِّجَالَ شَهْوَةًۭ مِّن دُونِ ٱلنِّسَآءِ ۚ بَلْ أَنتُمْ قَوْمٌۭ مُّسْرِفُونَ (٨١)\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "'Do you commit such immorality as no one has preceded you with from among the worlds? Indeed, you approach men with desire, instead of women. Rather, you are a transgressing people.'\n\n"
                            + "<b>[Surah Al-A'raf: 7:80-81]</b>\n\n"
                            + "<b>Hostile Reaction of the Sinners:</b>\n\n"
                            + "أَخْرِجُوهُم مِّن قَرْيَتِكُمْ ۖ إِنَّهُمْ أُنَاسٌۭ يَتَطَهَّরُونَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "'Drive them out of your city! Indeed, they are people who keep themselves pure.'\n\n"
                            + "<b>[Surah Al-A'raf: 7:82]</b>\n\n"
                            + "<b>Sincere Preaching of Lut (AS):</b>\n\n"
                            + "قَالَ يَـٰقَوْمِ ٱتَّقُوا۟ ٱللَّهَ مَا لَكُم مِّنْ إِلَـٰهٍ غَيْرُهُۥٓ ۚ إِنَّكُمْ لَتَأْتُونَ ٱلْفَـٰحِشَةَ وَلَا تَسْبِقُونَ بِهَا مِنْ أَحَدٍۢ مِّنَ ٱلْعَـٰلَمِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "He said, 'O my people, fear Allah! You have no deity other than Him. Do you commit blatant immorality while you see?'\n\n"
                            + "<b>[Surah An-Naml: 27:54]</b>\n\n"
                            + "<b>Grief and Plea of Lut (AS):</b>\n\n"
                            + "قَالَ لَوْ أَنَّ لِى بِكُمْ قُوَّةً أَوْ ءَاوِىٓ إِلَىٰ رُكْنٍۢ شَدِيدٍۢ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "He said, 'If only I had against you some power or could take refuge in a strong support.'\n\n"
                            + "<b>[Surah Hud: 11:80]</b>"
            ));

            list.add(new ProphetOverviewTopicItem(
                    5,
                    "ফেরেশতাদের আগমন ও পরীক্ষা",
                    "Arrival of Angels and the Trial",
                    "হযরত লূত (عليه السلام) এর কওম (সদোম ও আশেপাশের জনপদ) ছিল পাপাচারে নিমজ্জিত — বিশেষ করে সমকামিতা, যা মানব ইতিহাসে এক ব্যতিক্রম। আল্লাহ ﷻ তাঁদের উপর শাস্তি পাঠানোর সিদ্ধান্ত নেন এবং তার পূর্বে কয়েকজন ফেরেশতা মানবাকারে হযরত লূতের কাছে আগমন করেন。\n\n"
                            + "<b>এই আগমন ছিল দুইটি উদ্দেশ্যে:</b>\n\n"
                            + "১. হযরত ইব্রাহিম (عليه السلام)-কে সন্তানের সুসংবাদ দেওয়া。\n"
                            + "২. হযরত লূত (عليه السلام)-এর জাতিকে ধ্বংস করার আদেশ বাস্তবায়ন。\n\n"
                            + "<b>ফেরেশতারা হযরত ইব্রাহিম (আ:) এর ঘরে:</b>\n\n"
                            + "وَلَقَدْ جَآءَتْ رُسُلُنَآ إِبْرَٰهِيمَ بِٱلْبُشْرَىٰ قَالُوا۟ سَلَـٰمًۭا ۖ قَالَ سَلَـٰمٌۭ فَمَا لَبِثَ أَن جَآءَ بِعِجْلٍۢ حَنِيذٍۢ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমার ফেরেশতারা ইব্রাহিমের কাছে সুসংবাদ নিয়ে এসেছিল। তারা বলল, ‘সালাম’। তিনি বললেন, ‘সালাম’। তিনি তৎক্ষণাৎ এক ভুনা গরুর বাচ্চা (মেহমানদারি হিসেবে) আনলেন。\n\n"
                            + "<b>[সূরা হূদ - ৬৯]</b>\n\n"
                            + "ইব্রাহিম (আ:) যখন দেখলেন তারা খাবার গ্রহণ করছে না, তখন তিনি ভীত হলেন। তখন ফেরেশতারা বললেন, ভয় করবেন না, আমরা লূতের জাতির প্রতি প্রেরিত। ফেরেশতারা লূত (আ:) এর ঘরে মানবাকারে আসেন\n\n"
                            + "وَلَمَّا جَآءَتْ رُسُلُنَا لُوطًۭا سِىٓءَ بِهِمْ وَضَاقَ بِهِمْ ذَرْعًۭا وَقَالَ هَـٰذَا يَوْمٌۭ عَصِيبٌۭ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর যখন আমার দূতরা লূতের কাছে পৌঁছাল, তখন তিনি তাঁদের কারণে চিন্তিত হলেন এবং দুশ্চিন্তায় পড়ে গেলেন ও বললেন, ‘এটা তো এক কঠিন দিন。\n\n"
                            + "<b>[সূরা হূদ – ৭৭]</b>\n\n"
                            + "ফেরেশতারা সুন্দর-আকৃতির যুবক মানবরূপে এসেছিলেন। লূত (আ:) ভয় পেলেন, কারণ তিনি জানতেন তাঁর কওম এমন সুন্দর যুবকদের লক্ষ্য করবে কুপ্রবৃত্তির জন্য。\n\n"
                            + "<b>কওমের বিকৃত প্রবৃত্তির পরীক্ষা:</b>\n\n"
                            + "وَجَآءَهُۥ قَوْمُهُۥ يُهْرَعُونَ إِلَيْهِ ۖ وَمِن قَبْلُ كَانُوا۟ يَعْمَلُونَ ٱلسَّيِّـَٔاتِ ۚ قَالَ يَـٰقَوْمِ هَـٰٓؤُلَآءِ বَنَاتِى هُنَّ أَطْهَرُ لَكُمْ ۖ فَٱتَّقُوا۟ ٱللَّهَ وَلَا تُخْزُونِ فِى ضَيْفِىٓ ۚ أَلَيْسَ مِنكُمْ رَجُلٌۭ رَّশِيدٌۭ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তার কওম দৌড়াতে দৌড়াতে তাঁর (বাড়ির) দিকে এল — তারা পূর্ব থেকেই খারাপ কাজ করত। তিনি বললেন, ‘হে আমার কওম! এরা তো আমার মেয়েরা — এরা তোমাদের জন্য পবিত্র। সুতরাং আল্লাহকে ভয় করো এবং আমার মেহমানদের সামনে আমাকে লজ্জিত করো না। তোমাদের মাঝে কি একজনও সৎবুদ্ধিসম্পন্ন ব্যক্তি নেই?\n\n"
                            + "<b>[সূরা হূদ – ৭৮]</b>\n\n"
                            + "তিনি “মেয়েরা” বলে হয়তো নিজের বাস্তব কন্যাদের বোঝাচ্ছিলেন, অথবা উম্মতের নারীদের, বৈধ বিবাহের প্রতি আহ্বান করে। কিন্তু কওম ছিল এতটাই অন্ধ, তারা বলে,\n\n"
                            + "قَالُوا۟ لَقَدْ عَلِمْتَ مَا লَنَا فِى بَنَاتِكَ مِنْ حَقٍّۢ ۖ وَإِنَّكَ لَتَعْلَمُ مَا نُرِيدُ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা বলল, ‘তুমি তো জানো তোমার মেয়েদের ব্যাপারে আমাদের কোনো আগ্রহ নেই। তুমি তো জানো, আমরা কী চাই。\n\n"
                            + "<b>[সূরা হূদ – ৭৯]</b>\n\n"
                            + "<b>ফেরেশতাদের পরিচয় প্রকাশ ও নির্দেশ:</b>\n\n"
                            + "قَالُوا۟ يَـٰলُوطُ إِنَّا رُسُلُ رَبِّكَ لَن يَصِلُوٓا۟ إِلَيْكَ فَأَسْرِ بِأَهْلِكَ بِقِطْعٍۢ مِّنَ ٱلَّيْلِ...\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা (ফেরেশতারা) বলল, ‘হে লূত! আমরা তোমার প্রভুর পক্ষ থেকে প্রেরিত ফেরেশতা। এরা তোমার কোন ক্ষতি করতে পারবে না। অতএব তুমি রাতের কোনো অংশে তোমার পরিবারসহ রওনা হও。\n\n"
                            + "<b>[সূরা হূদ – ৮১]</b>",
                    "When Allah decreed punishment upon Sodom, He sent angels in the form of handsome young men. They first visited Ibrahim (AS) to deliver glad tidings of a son and inform him of the coming destruction, and then arrived at the house of Lut (AS).\n\n"
                            + "<b>Angels at the House of Ibrahim (AS):</b>\n\n"
                            + "وَلَقَدْ جَآءَتْ رُسُلُنَآ إِبْرَٰهِيمَ بِٱلْبُشْرَىٰ قَالُوا۟ سَلَـٰمًۭا ۖ قَالَ سَلَـٰمٌۭ فَمَا لَبِثَ أَن جَآءَ بِعِجْلٍۢ حَنِيذٍۢ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And certainly did Our messengers come to Ibrahim with good tidings; they said, 'Peace.' He said, 'Peace,' and did not delay in bringing a roasted calf.\n\n"
                            + "<b>[Surah Hud: 11:69]</b>\n\n"
                            + "<b>Arrival at Lut's Residence:</b>\n\n"
                            + "وَلَمَّا جَآءَتْ رُسُلُنَا لُوطًۭا سِىٓءَ بِهِمْ وَضَاقَ بِهِمْ ذَرْعًۭا وَقَالَ هَـٰذَا يَوْمٌۭ عَصِيبٌۭ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And when Our messengers came to Lut, he was anguished for them and felt for them great discomfort and said, 'This is a distressful day.'\n\n"
                            + "<b>[Surah Hud: 11:77]</b>\n\n"
                            + "<b>The Depraved Crowd Surrounds the House:</b>\n\n"
                            + "وَجَآءَهُۥ قَوْمُهُۥ يُهْرَعُونَ إِلَيْهِ ۖ وَمِن قَبْلُ كَانُوا۟ يَعْمَلُونَ ٱلسَّيِّـَٔاتِ ۚ قَالَ يَـٰقَوْمِ هَـٰٓؤُلَآءِ بَنَاتِى هُنَّ أَطْهَرُ لَكُمْ ۖ فَٱتَّقُوا۟ ٱللَّهَ وَلَا تُخْزُونِ فِى ضَيْفِىٓ ۚ أَلَيْسَ مِنكُمْ رَجُلٌۭ رَّشِيدٌۭ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And his people came rushing to him, and before they had been doing evil deeds. He said, 'O my people, these are my daughters; they are purer for you. So fear Allah and do not disgrace me concerning my guests. Is there not among you a right-minded man?'\n\n"
                            + "<b>[Surah Hud: 11:78]</b>\n\n"
                            + "<b>The Defiant Response:</b>\n\n"
                            + "قَالُوا۟ لَقَدْ عَلِمْتَ مَا لَنَا فِى بَنَاتِكَ مِنْ حَقٍّۢ ۖ وَإِنَّكَ لَتَعْلَمُ مَا نُرِيدُ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "They said, 'You have already known that we have no right to your daughters, and indeed, you know what we want.'\n\n"
                            + "<b>[Surah Hud: 11:79]</b>\n\n"
                            + "<b>Angels Reveal Their Identity:</b>\n\n"
                            + "قَالُوا۟ يَـٰলُوطُ إِنَّا رُسُلُ رَبِّكَ لَن يَصِلُوٓا۟ إِلَيْكَ فَأَسْرِ بِأَهْلِكَ بِقِطْعٍۢ مِّنَ ٱلَّيْلِ...\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "The angels said, 'O Lut, indeed we are messengers of your Lord; [therefore] they will never reach you. So set out with your family during a portion of the night...'\n\n"
                            + "<b>[Surah Hud: 11:81]</b>"
            ));

            list.add(new ProphetOverviewTopicItem(
                    6,
                    "আযাবের ঘোষণা ও নিরাপত্তার পরিকল্পনা",
                    "Announcement of Punishment and Plan of Safety",
                    "হযরত লূত (عليه السلام) এর কওম (সদোম ও আশেপাশের জনপদ) ছিল পাপাচারে নিমজ্জিত — বিশেষ করে সমকামিতা, যা মানব ইতিহাসে এক ব্যতিক্রম। আল্লাহ ﷻ তাঁদের উপর শাস্তি পাঠানোর সিদ্ধান্ত নেন এবং তার পূর্বে কয়েকজন ফেরেশতা মানবাকারে হযরত লূতের কাছে আগমন করেন。\n\n"
                            + "<b>এই আগমন ছিল দুইটি উদ্দেশ্যে:</b>\n\n"
                            + "১. হযরত ইব্রাহিম (عليه السلام)-কে সন্তানের সুসংবাদ দেওয়া。\n"
                            + "২. হযরত লূত (عليه السلام)-এর জাতিকে ধ্বংস করার আদেশ বাস্তবায়ন。\n\n"
                            + "<b>ফেরেশতারা হযরত ইব্রাহিম (আ:) এর ঘরে:</b>\n\n"
                            + "وَلَقَدْ جَآءَتْ رُسُلُنَآ إِبْرَٰهِيمَ بِٱلْبُشْرَىٰ قَالُوا۟ سَلَـٰمًۭا ۖ قَالَ سَلَـٰمٌۭ فَمَا لَبِثَ أَن جَآءَ بِعِجْلٍۢ حَنِيذٍۢ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমার ফেরেশতারা ইব্রাহিমের কাছে সুসংবাদ নিয়ে এসেছিল। তারা বলল, ‘সালাম’। তিনি বললেন, ‘সালাম’। তিনি তৎক্ষণাৎ এক ভুনা গরুর বাচ্চা (মেহমানদারি হিসেবে) আনলেন。\n\n"
                            + "<b>[সূরা হূদ – ৬৯]</b>\n\n"
                            + "ইব্রাহিম (আ:) যখন দেখলেন তারা খাবার গ্রহণ করছে না, তখন তিনি ভীত হলেন। তখন ফেরেশতারা বললেন, ভয় করবেন না, আমরা লূতের জাতির প্রতি প্রেরিত。\n\n"
                            + "<b>ফেরেশতারা লূত (আ:) এর ঘরে মানবাকারে আসেন:</b>\n\n"
                            + "وَلَمَّا جَآءَتْ رُسُلُنَا لُوطًۭا سِىٓءَ بِهِمْ وَضَاقَ بِهِمْ ذَرْعًۭا وَقَالَ هَـٰذَا يَوْমٌۭ عَصِيبٌۭ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর যখন আমার দূতরা লূতের কাছে পৌঁছাল, তখন তিনি তাঁদের কারণে চিন্তিত হলেন এবং দুশ্চিন্তায় পড়ে গেলেন ও বললেন, ‘এটা তো এক কঠিন দিন。\n\n"
                            + "<b>[সূরা হূদ – ৭৭]</b>\n\n"
                            + "ফেরেশতারা সুন্দর-আকৃতির যুবক মানবরূপে এসেছিলেন। লূত (আ:) ভয় পেলেন, কারণ তিনি জানতেন তাঁর কওম এমন সুন্দর যুবকদের লক্ষ্য করবে কুপ্রবৃত্তির জন্য。\n\n"
                            + "<b>কওমের বিকৃত প্রবৃত্তির পরীক্ষা:</b>\n\n"
                            + "وَجَآءَهُۥ قَوْمُهُۥ يُهْرَعُونَ إِلَيْهِ ۖ وَمِن قَبْلُ كَانُوا۟ يَعْمَلُونَ ٱلسَّيِّـَٔاتِ ۚ قَالَ يَـٰقَوْمِ هَـٰٓؤُلَآءِ বَنَاتِى هُنَّ أَطْهَرُ لَكُمْ ۖ فَٱتَّقُوا۟ ٱللَّهَ وَلَا تُخْزُونِ فِى ضَيْفِىٓ ۚ أَلَيْسَ مِنكُمْ رَجُلٌۭ رَّশِيدٌۭ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তার কওম দৌড়াতে দৌড়াতে তাঁর (বাড়ির) দিকে এল — তারা পূর্ব থেকেই খারাপ কাজ করত। তিনি বললেন, ‘হে আমার কওম! এরা তো আমার মেয়েরা — এরা তোমাদের জন্য পবিত্র। সুতরাং আল্লাহকে ভয় করো এবং আমার মেহমানদের সামনে আমাকে লজ্জিত করো না। তোমাদের মাঝে কি একজনও সৎবুদ্ধিসম্পন্ন ব্যক্তি নেই?\n\n"
                            + "<b>[সূরা হূদ – ৭৮]</b>\n\n"
                            + "তিনি “মেয়েরা” বলে হয়তো নিজের বাস্তব কন্যাদের বোঝাচ্ছিলেন, অথবা উম্মতের নারীদের, বৈধ বিবাহের প্রতি আহ্বান করে। কিন্তু কওম ছিল এতটাই অন্ধ, তারা বলে,\n\n"
                            + "قَالُوا۟ لَقَدْ عَلِمْتَ مَا লَنَا فِى بَنَاتِكَ مِنْ حَقٍّۢ ۖ وَإِنَّكَ لَتَعْلَمُ مَا نُرِيدُ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা বলল, ‘তুমি তো জানো — তোমার মেয়েদের ব্যাপারে আমাদের কোনো আগ্রহ নেই। তুমি তো জানো, আমরা কী চাই。\n\n"
                            + "<b>[সূরা হূদ – ৭৯]</b>\n\n"
                            + "<b>ফেরেশতাদের পরিচয় প্রকাশ ও নির্দেশ:</b>\n\n"
                            + "قَالُوا۟ يَـٰলُوطُ إِنَّا رُسُلُ رَبِّكَ لَن يَصِلُوٓا۟ إِلَيْكَ فَأَسْرِ بِأَهْلِكَ بِقِطْعٍۢ مِّنَ ٱلَّيْلِ...\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা (ফেরেশতারা) বলল, ‘হে লূত! আমরা তোমার প্রভুর পক্ষ থেকে প্রেরিত ফেরেশতা। এরা তোমার কোন ক্ষতি করতে পারবে না। অতএব তুমি রাতের কোনো অংশে তোমার পরিবারসহ রওনা হও。\n\n"
                            + "<b>[সূরা হূদ – ৮১]</b>",
                    "The angels announced to Lut (AS) the imminent destruction of Sodom and instructed him on the safety plan for the believers.\n\n"
                            + "<b>Twin Purposes of the Angels' Descent:</b>\n"
                            + "1. Deliver glad tidings of righteous offspring to Prophet Ibrahim (AS).\n"
                            + "2. Execute divine retribution upon the corrupt nation of Sodom.\n\n"
                            + "<b>Divine Protection & Evacuation Order:</b>\n\n"
                            + "قَالُوا۟ يَـٰলُوطُ إِنَّا رُسُلُ رَبِّكَ لَن يَصِلُوٓا۟ إِلَيْكَ فَأَسْرِ بِأَهْلِكَ بِقِطْعٍۢ مِّنَ ٱلَّيْلِ وَلَا يَلْتَفِتْ مِنكُمْ أَحَدٌ إِلَّا ٱمْرَأَتَكَ ۚ إِنَّهُۥ مُصِيبُهَا مَآ أَصَابَهُمْ ۚ إِنَّ مَوْعِدَهُمُ ٱلصُّبْحُ ۚ أَلَيْسَ ٱلصُّبْحُ بِقَرِيبٍ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "They said, 'O Lut, indeed we are messengers of your Lord; they will never reach you. So set out with your family during a portion of the night and let not any among you look back - except your wife; indeed, she will be struck by that which strikes them. Indeed, their appointment is for the morning. Is not the morning near?'\n\n"
                            + "<b>[Surah Hud: 11:81]</b>"
            ));

            list.add(new ProphetOverviewTopicItem(
                    7,
                    "স্ত্রীর কপটতা ও ধ্বংসপ্রাপ্তদের অন্তর্ভুক্ত হওয়া",
                    "Hypocrisy of Lut's Wife and Her Doom",
                    "হযরত লূত (আঃ) ছিলেন একজন নবী এবং আল্লাহর প্রেরিত দূত। কিন্তু তাঁর স্ত্রী আল্লাহর পথে না চলার কারণে এবং কুফর ও পাপী সম্প্রদায়ের প্রতি পক্ষপাতিত্ব করার জন্য ধ্বংসপ্রাপ্তদের অন্তর্ভুক্ত হন। যদিও তিনি একজন নবীর ঘনিষ্ঠ ছিলেন, তবুও তাঁর হৃদয় আল্লাহর নির্দেশনার বিপরীত ছিল。\n\n"
                            + "<b>আল্লাহ তাআলার ঘোষণা – পেছনে পড়া ধ্বংসপ্রাপ্তদের অন্তর্ভুক্ত:</b>\n\n"
                            + "إِلَّا ٱمْرَأَتَهُۥ كَانَتْ مِنَ ٱلْغَٰبِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তাঁর স্ত্রী ছাড়া, সে ছিল পিছনে পড়ে যাওয়া (ধ্বংসপ্রাপ্তদের) অন্তর্ভুক্ত।\n\n"
                            + "<b>[সূরা আল-আ‘রাফ – ৮৩]</b>\n\n"
                            + "এই আয়াতটি থেকে বোঝা যায়, হযরত লূত (আঃ)-এর স্ত্রী বাহ্যিকভাবে তাঁর সাথে থাকলেও অন্তরে পাপীদের সাথেই যুক্ত ছিলেন。\n\n"
                            + "<b>ফেরেশতারা যখন ধ্বংসের সিদ্ধান্ত জানায়, তখন স্ত্রীকে ব্যতিক্রম করা হয়:</b>\n\n"
                            + "فَأَسْرِ بِأَهْلِكَ بِقِطْعٍۢ مِّنَ ٱلَّيْلِ وَلَا يَلْتَفِتْ مِنكُمْ أَحَدٌ إِلَّا ٱمْرَأَتَكَ ۚ إِنَّهُۥ مُصِيبُهَا مَآ أَصَابَهُمْ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তুমি রাতের কোনো অংশে তোমার পরিবারসহ বের হয়ে যাও, এবং তোমাদের মধ্যে কেউ যেন পেছনে ফিরে না তাকায়, তবে তোমার স্ত্রী ব্যতিক্রম—তার ওপরও তাদের ওপর যা আসবে, তাই আসবে。\n\n"
                            + "<b>[সূরা হূদ – ৮১]</b>\n\n"
                            + "এটি ছিল ধ্বংসপ্রাপ্তদের জন্য আল্লাহর পক্ষ থেকে নির্ধারিত ফয়সালা。\n\n"
                            + "<b>কপটতার উদাহরণ হিসেবে কুরআনে বর্ণনা:</b>\n\n"
                            + "ضَرَبَ ٱللَّهُ مَثَلًۭا لِّلَّذِينَ كَفَرُوا۟ ٱمْرَأَتَ نُوحٍۢ وَٱمْرَأَتَ لُوطٍۢ ۖ كَانَتَا تَحْتَ عَبْدَيْنِ مِنْ عِبَادِنَا صَـٰلِحَيْنِ فَخَانَتَاهُمَا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আল্লাহ কুফরকারীদের জন্য উদাহরণ দিয়েছেন নূহের স্ত্রী এবং লূতের স্ত্রীকে। তারা ছিল আমার দুজন সৎ বান্দার অধীনে, অথচ তারা তাদের (স্বামীদের) সঙ্গে বিশ্বাসঘাতকতা করেছিল।\n\n"
                            + "<b>[সূরা আত-তাহরীম – ১০]</b>\n\n"
                            + "তাঁদের বিশ্বাসঘাতকতা ছিল আকীদাগত এবং আদর্শগত, যা আল্লাহর বিরুদ্ধাচরণকারী এক জাতির প্রতি সহানুভূতির মাধ্যমে প্রকাশ পায়।",
                    "The wife of Prophet Lut (AS) betrayed him not in marital fidelity, but in faith and allegiance. She secretly allied with the transgressors and alerted them to the guests, sealing her fate among the doomed.\n\n"
                            + "<b>Left Behind Among the Destroyed:</b>\n\n"
                            + "إِلَّا ٱمْرَأَتَهُۥ كَانَتْ مِنَ ٱلْغَٰبِرِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "Except his wife; she was of those who remained behind.\n\n"
                            + "<b>[Surah Al-A'raf: 7:83]</b>\n\n"
                            + "<b>Excluded from Deliverance:</b>\n\n"
                            + "فَأَسْرِ بِأَهْلِكَ بِقِطْعٍۢ مِّنَ ٱلَّيْلِ وَلَا يَلْتَفِتْ مِنكُمْ أَحَدٌ إِلَّا ٱمْرَأَتَكَ ۚ إِنَّهُۥ مُصِيبُهَا مَآ أَصَابَهُمْ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "So travel with your family in a part of the night, and let not any of you turn back except your wife. Indeed, what strikes them will strike her.\n\n"
                            + "<b>[Surah Hud: 11:81]</b>\n\n"
                            + "<b>Exemplary Warning in the Quran:</b>\n\n"
                            + "ضَرَبَ ٱللَّهُ مَثَلًۭا لِّلَّذِينَ كَفَرُوا۟ ٱمْرَأَتَ نُوحٍۢ وَٱمْرَأَتَ لُوطٍۢ ۖ كَانَتَا تَحْتَ عَبْدَيْنِ مِنْ عِبَادِنَا صَـٰلِحَيْنِ فَخَانَتَاهُمَا\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "Allah presents an example of those who disbelieved: the wife of Noah and the wife of Lut. They were under two of our righteous servants but betrayed them...\n\n"
                            + "<b>[Surah At-Tahrim: 66:10]</b>"
            ));

            list.add(new ProphetOverviewTopicItem(
                    8,
                    "সদোম শহরের ধ্বংস ও আযাব",
                    "Destruction and Punishment of Sodom",
                    "হযরত লূত (عليه السلام) এর জাতি – যাদেরকে কুরআনে قَوْمُ لُوطٍ (লূতের কওম) বলা হয়েছে – ছিল এক পাপাচারী ও সীমালঙ্ঘনকারী জাতি। তারা ইতিহাসে প্রথমবারের মতো সমকামিতার মতো মারাত্মক অশ্লীল কাজকে সমাজে প্রতিষ্ঠা করে। তারা শুধু নিজেদের কুপ্রবৃত্তির মধ্যে সীমাবদ্ধ ছিল না, বরং যারা নৈতিক ও ধর্মীয় সত্যের দাওয়াত দিত, তাদেরকেও তাড়িয়ে দিতে চেয়েছিল। আল্লাহ সুবহানাহু ওয়া তা‘আলা তাদেরকে ধ্বংস করার জন্য ফেরেশতাদের প্রেরণ করেন, এবং এক ভয়াবহ আজাবের মাধ্যমে সেই জনপদ ধ্বংস করে দেন। কুরআনে এই ঘটনা একাধিক স্থানে বিস্তারিতভাবে বর্ণিত হয়েছে, যাতে মানুষ শিক্ষা নেয়。\n\n"
                            + "<b>তাদের অপরাধ:</b>\n\n"
                            + "أَئِنَّكُمْ لَتَأْتُونَ ٱلرِّجَالَ شَهْوَةًۭ مِّن دُونِ ٱلنِّسَآءِ ۚ বَلْ أَنتُمْ قَوْمٌۭ تَجْهَلُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা কি নারীদের ছেড়ে পুরুষদের কামনার বশে গমন করো? বরং তোমরা অজ্ঞ জাতি。\n\n"
                            + "<b>[সূরা আন-নামল - ৫৫]</b>\n\n"
                            + "তাদের অপরাধ শুধু সমকামিতায় সীমাবদ্ধ ছিল না, বরং তারা লুটতরাজ, পথ অবরোধ, সভ্যতার বিলুপ্তি, ও আল্লাহর নবীকে মিথ্যাবাদী বলা পর্যন্ত গিয়েছিল。\n\n"
                            + "<b>হযরত লূত (আ:) এর সতর্কবার্তা:</b>\n\n"
                            + "قَالَ يَـٰقَوْمِ ٱتَّقُوا۟ ٱللَّهَ وَلَا تُخْزُونِ فِى ضَيْفِىٓ ۖ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি বললেন: হে আমার কওম! তোমরা আল্লাহকে ভয় করো এবং আমার মেহমানদের সামনে আমাকে লজ্জিত করো না。\n\n"
                            + "<b>[সূরা হূদ - ৭৮]</b>\n\n"
                            + "তাঁর কওম নৈতিকতা ও ভদ্রতার সব সীমা অতিক্রম করেছিল। এমনকি ফেরেশতারা যখন যুবকদের রূপে এসেছিলেন, তারা তাদের ধর্ষণের চেষ্টাও করেছিল。\n\n"
                            + "<b>আজাবের ঘোষণা:</b>\n\n"
                            + "قَالُوا۟ يَـٰلُوطُ إِنَّا رُسُلُ رَبِّكَ لَن يَصِلُوٓا۟ إِلَيْكَ ۖ فَأَسْرِ بِأَهْلِكَ... إِلَّا ٱمْرَأَتَكَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "ফেরেশতারা বলল: হে লূত! আমরা তোমার প্রভুর পক্ষ থেকে প্রেরিত। এরা তোমার কোনো ক্ষতি করতে পারবে না। অতএব তুমি তোমার পরিবারসহ বের হয়ে যাও, তবে তোমার স্ত্রী ব্যতিক্রম—তার উপরও সেই শাস্তি আসবে。\n\n"
                            + "<b>[সূরা হূদ - ৮১]</b>\n\n"
                            + "<b>ধ্বংসের ধরন ও শাস্তির বর্ণনা:</b>\n\n"
                            + "فَلَمَّا جَآءَ أَمۡرُنَا جَعَلْنَا عَـٰلِيَهَا سَافِلَهَا وَأَمۡطَرۡনَا عَلَيْهَا حِجَارَةًۭ مِّن سِجِّيلٍۢ مَّنضُودٍۢ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর যখন আমার আদেশ এল, তখন আমি (সদোম শহরকে) উপরের দিক নিচে করে দিলাম এবং তাদের উপর স্তরে স্তরে সাজানো পাথর বর্ষণ করলাম。\n\n"
                            + "<b>[সূরা হূদ - ৮২]</b>\n\n"
                            + "শহরের নিচ অংশ উপরে এবং উপরাংশ নিচে করা হয় (আল্লাহর পক্ষ থেকে ভূমিকম্প বা উল্টে ফেলা)। এরপর তাদের উপর সিজ্জীল (পাকা কাদামাটি) দিয়ে তৈরি পাথরের কঠিন বৃষ্টি বর্ষিত হয়। তাদের জনপদ এমনভাবে নিশ্চিহ্ন করা হয় যে, আজও তা “মৃত সাগর” হিসেবে ইতিহাসে পরিচিত。\n\n"
                            + "<b>শিক্ষা ও উপদেশ:</b>\n\n"
                            + "فَجَعَلْنَا عَـٰلِيَهَا سَافِلَهَا وَأَمۡطَرۡনَا عَلَيْهِمۡ حِجَارَةًۭ مِّن سِجِّيلٍ ۥٓ ۖ إِنَّ فِى ذَٰلِكَ لَـَٔايَةًۭ لِّلْمُتَوَسِّمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "এতে নিঃসন্দেহে রয়েছে এক নিদর্শন চিন্তাশীলদের জন্য。\n\n"
                            + "<b>[সূরা হিজর - ৭৫]</b>\n\n"
                            + "এই জাতির ধ্বংস থেকে শিক্ষা নেওয়া প্রত্যেক মু’মিনের দায়িত্ব। এটি কেবল একটি ঐতিহাসিক ঘটনা নয়, বরং প্রতিটি যুগের পাপাচারী সমাজের জন্য সতর্কবার্তা।",
                    "The city of Sodom was utterly annihilated when the divine command arrived. The angel Jibreel (AS) lifted the entire city and flipped it upside down, followed by a shower of marked brimstones (baked clay stones).\n\n"
                            + "<b>The Inversion and Stone Rain:</b>\n\n"
                            + "فَلَمَّا جَآءَ أَمۡرُنَا جَعَلْنَا عَـٰلِيَهَا سَافِلَهَا وَأَمۡطَرۡনَا عَلَيْهَا حِجَارَةًۭ مِّن سِجِّيلٍۢ مَّنضُودٍۢ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "So when Our command came, We made the highest part [of the city] its lowest and rained upon them stones of layered hard clay.\n\n"
                            + "<b>[Surah Hud: 11:82]</b>\n\n"
                            + "<b>A Lasting Sign for the Mindful:</b>\n\n"
                            + "فَجَعَلْنَا عَـٰلِيَهَا سَافِلَهَا وَأَمۡطَرۡনَا عَلَيْهِمۡ حِجَارَةًۭ مِّن سِجِّيلٍ ۥٓ ۖ إِنَّ فِى ذَٰلِكَ لَـَٔايَةًۭ لِّلْمُتَوَسِّمِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And We made the highest part [of the city] its lowest and rained upon them stones of hard clay. Indeed in that are signs for those who discern.\n\n"
                            + "<b>[Surah Al-Hijr: 15:74-75]</b>"
            ));

            list.add(new ProphetOverviewTopicItem(
                    9,
                    "মৃত্যু ও শিক্ষা",
                    "Passing and Lessons for Humanity",
                    "হযরত লূত (আঃ)-এর মৃত্যুর সুনির্দিষ্ট স্থান বা সাল কুরআন বা সহীহ হাদীসে উল্লেখ নেই। তবে ইসলামী ইতিহাস ও তাফসির অনুযায়ী ধারণা করা হয়। ধ্বংসের পর তিনি ফিলিস্তিন বা জর্ডান অঞ্চলে অবস্থান করেন। সেখানে তিনি শান্তিপূর্ণভাবে ইন্তিকাল করেন। কিছু বর্ণনা মতে, তাঁকে সোয়ার (Zoar) অঞ্চলে দাফন করা হয়, যা মৃত সাগরের নিকটবর্তী。\n\n"
                            + "وَلُوطًا ءَاتَيْنَـٰهُ حُكْمًۭا وَعِلْمًۭا وَنَجَّيْنَـٰهُ مِنَ ٱلْقَرْيَةِ ٱلَّتِى كَانَت تَّعْمَلُ ٱلْخَبَـٰٓئِثَ ۚ إِنَّهُمْ كَانُوا۟ قَوْمَ سَوْءٍۢ فَـٰסِقِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর লূতকে আমি দিয়েছিলাম বিচারক্ষমতা ও জ্ঞান এবং তাঁকে রক্ষা করেছিলাম সেই জনপদ থেকে, যারা অশ্লীল কাজ করত। নিশ্চয় তারা ছিল এক মন্দ, পাপাচারী জাতি。\n\n"
                            + "<b>[সূরা আল-আম্বিয়া - ৭৪]</b>\n\n"
                            + "<b>আমাদের জন্য শিক্ষা:</b>\n\n"
                            + "• <b>সত্যের পথে একাকী হলেও অবিচল থাকতে হবে:</b> হযরত লূত (আঃ) তাঁর কওমের মধ্যে একা ছিলেন, কিন্তু তিনি কখনো আল্লাহর আহ্বান ছাড়েননি。\n"
                            + "• <b>সমকামিতা ও প্রকাশ্য অশ্লীলতার ধ্বংসাত্মক পরিণতি:</b> সমকামিতা ও প্রকাশ্য অশ্লীলতার কারণে একটি পুরো জাতি ধ্বংস হয়ে যায় — এটি একটি চিরন্তন শিক্ষা。\n"
                            + "• <b>ঈমানই সম্পর্কের মূল ভিত্তি:</b> পরিবার থেকেও যদি কেউ সত্যের বিরুদ্ধে থাকে, তবে তাকে অনুসরণ করা যাবে না; তাঁর স্ত্রী কুফরপন্থীদের পক্ষে থাকায় ধ্বংসপ্রাপ্ত হয়। তাই আত্মীয়তা নয়, ঈমানই মূল ভিত্তি。\n"
                            + "• <b>ধ্বংসপ্রাপ্ত জাতির নিদর্শন আজও বাস্তব প্রমাণ:</b> ধ্বংসপ্রাপ্ত জাতির নিদর্শন আজও রয়েছে — মৃত সাগর ও আশেপাশের অঞ্চল সেই ভয়াবহ ঘটনার বাস্তব প্রমাণ, যা প্রতিটি জাতির জন্য সতর্কবার্তা।",
                    "While the exact date and location of Prophet Lut's passing are not specified in the Quran or authentic Hadith, historical records and tafsir indicate that he lived peacefully in Palestine or Jordan (near Zoar, adjacent to the Dead Sea) following the destruction of Sodom.\n\n"
                            + "وَلُوطًا ءَاتَيْنَـٰهُ حُكْمًۭا وَعِلْمًۭا وَنَجَّيْنَـٰهُ مِنَ ٱلْقَرْيَةِ ٱلَّتِى كَانَت تَّعْمَلُ ٱلْخَبَـٰٓئِثَ ۚ إِنَّهُمْ كَانُوا۟ قَوْمَ سَوْءٍۢ فَـٰסِقِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "And to Lut We gave judgement and knowledge, and We saved him from the city that was committing wicked deeds. Indeed, they were a people of evil and defiantly disobedient.\n\n"
                            + "<b>[Surah Al-Anbiya: 21:74]</b>\n\n"
                            + "<b>Timeless Lessons for Humanity:</b>\n\n"
                            + "• <b>Steadfastness on Truth:</b> Even when standing alone against an entire corrupt society, truth must never be compromised.\n"
                            + "• <b>Destructive Nature of Obscenity:</b> The total destruction of Sodom serves as an eternal warning against homosexuality and public vice.\n"
                            + "• <b>Faith Transcends Bloodline:</b> Kinship cannot save someone devoid of faith, as shown by the fate of Lut's wife.\n"
                            + "• <b>Visible Signs in History:</b> The Dead Sea (Bahr Lut) stands to this day as a sobering testament to divine justice."
            ));
        } else if (prophetId == 8) {
            // 1. জন্ম ও বংশ পরিচয়
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "জন্ম ও বংশ পরিচয়",
                    "Birth, Lineage and Identity",
                    "ইসমাইল (আরবি: إسماعيل)\n\n"
                            + "অর্থ:\n\n"
                            + "আল্লাহ শুনেছেন” বা “যে প্রার্থনার জবাব দেওয়া হয়েছে”।\n\n"
                            + "পিতা:\n\n"
                            + "হযরত ইব্রাহিম (আলাইহিস সালাম) — আল্লাহর প্রিয় বন্ধু (খলীলুল্লাহ) এবং অন্যতম উলুল আযম নবী।\n\n"
                            + "মাতা:\n\n"
                            + "হযরত হাজেরা (আলাইহাস সালাম) — মিসরের অধিবাসী, এক নেককার ও সবরশীলা নারী।\n\n"
                            + "জন্মস্থান:\n\n"
                            + "ইতিহাসবিদদের মতে, তিনি ফিলিস্তিন বা কানআন অঞ্চলে জন্মগ্রহণ করেন। পরবর্তীতে মাতা হাজেরার সঙ্গে মক্কার উপত্যকায় আগমন ঘটে।\n\n"
                            + "বংশ পরিচয়:\n\n"
                            + "ইসমাইল (আঃ) ছিলেন হযরত ইব্রাহিম (আঃ)-এর প্রথম সন্তান। তাঁর বংশধারা থেকে গঠিত হয় আরব জাতি, বিশেষ করে কুরাইশ গোত্র। হযরত মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) এই বংশেই জন্মগ্রহণ করেন। তাই ইসমাইল (আঃ)-কে “আবু আল-আরব” বা “আরবদের পিতা” বলা হয়।\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِسْمَاعِيلَ ۚ إِنَّهُ كَانَ صَادِقَ الْوَعْدِ وَكَانَ رَسُولًا نَبِيًّا\n\n"
                            + "উচ্চারণ:\n\n"
                            + "ওয়াজকুর ফিল-কিতাবি ইসমাঈলা, ইন্নাহূ কানা সাদিকাল-ওয়াদ, ওয়াকাানা রাসূলান নবিয়্যাঃ\n\n"
                            + "অর্থ:\n\n"
                            + "এবং কিতাবে ইসমাঈলের কথা স্মরণ করুন। নিশ্চয়ই সে ছিল প্রতিশ্রুতি পালনে দৃঢ়; আর ছিল রাসূল ও নবী。\n\n"
                            + "<b>[সূরা মারইয়াম – ৫৪]</b>",
                    "<b>Ismail (Arabic: إسماعيل):</b>\n\n"
                            + "<b>Meaning:</b>\n"
                            + "\"Allah has heard\" or \"The one whose prayer was answered.\"\n\n"
                            + "<b>Father:</b>\n"
                            + "Prophet Ibrahim (peace be upon him) — Khalilullah (the Friend of Allah) and one of the resolute Ulul 'Azm messengers.\n\n"
                            + "<b>Mother:</b>\n"
                            + "Lady Hajar (peace be upon her) — a righteous and immensely patient woman from Egypt.\n\n"
                            + "<b>Place of Birth:</b>\n"
                            + "Historians state he was born in the land of Canaan (Palestine), and later traveled with his mother to the arid valley of Makkah.\n\n"
                            + "<b>Lineage & Identity:</b>\n"
                            + "Prophet Ismail (AS) was the firstborn son of Ibrahim (AS). From his noble lineage arose the Arab nation, specifically the Quraysh tribe, through which the Final Messenger Muhammad ﷺ was born. Hence, Ismail (AS) is venerated as \"Abu al-Arab\" (Father of the Arabs).\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِسْمَاعِيلَ ۚ إِنَّهُ كَانَ صَادِقَ الْوَعْدِ وَكَانَ رَسُولًا نَبِيًّا\n\n"
                            + "<b>Meaning:</b>\n"
                            + "\"And mention in the Book, Ismail. Indeed, he was true to his promise, and he was a messenger and a prophet.\"\n\n"
                            + "<b>[Surah Maryam: 54]</b>"
            ));

            // 2. মক্কায় আগমন ও স্থায়ী বসতি
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "মক্কায় আগমন ও স্থায়ী বসতি",
                    "Arrival in Makkah and Permanent Settlement",
                    "হযরত ইব্রাহিম (আঃ)-এর প্রথম স্ত্রী সারাহ (আঃ)-এর পর, আল্লাহর কৃপায় দ্বিতীয় স্ত্রী হাজেরা (আঃ)-এর গর্ভে ইসমাইল (আঃ) জন্মগ্রহণ করেন। আল্লাহর আদেশে তিনি তাঁর স্ত্রী হাজেরা ও ছোট শিশু ইসমাইলকে নিয়ে যান এক জনমানবহীন, গরম, পাথুরে উপত্যকায় যা ছিল বর্তমান মক্কা।\n\n"
                            + "رَّبَّنَآ إِنِّيٓ أَسْكَنتُ مِن ذُرِّيَّتِي بِوَادٍ غَيْرِ ذِى زَرْعٍ عِندَ بَيْتِكَ ٱلْمُحَرَّمِ\n\n"
                            + "উচ্চারণ:\n\n"
                            + "রব্বানাই ইন্নি আসকানতু মিন জুররিয়্যাতি বিওয়াদিন গাইরি যি জর‘ইন ‘ইন্দা বাইতিকাল মুহাররাম.\n\n"
                            + "অর্থ:\n\n"
                            + "হে আমার প্রতিপালক! আমি আমার কিছু সন্তানকে বসবাস করিয়েছি এক এমন উপত্যকায়, যেখানে কোনো শস্য নেই, আপনার পবিত্র গৃহের কাছে।\n\n"
                            + "<b>[সূরা ইব্রাহিম – ৩৭]</b>\n\n"
                            + "যখন ইব্রাহিম (আঃ) মাতা হাজেরা (আঃ) ও শিশু ইসমাইল (আঃ)-কে জনমানবহীন মক্কার মরুতে রেখে চলে যান, হাজেরা (আঃ) অবাক হয়ে জিজ্ঞাসা করেন আপনি কি আমাদের এখানে রেখে যাচ্ছেন আল্লাহর আদেশে? ইব্রাহিম (আঃ) মাথা নেড়ে সম্মতি দেন। তখন হাজেরা (আঃ) বললেন:\n\n"
                            + "তাহলে নিশ্চয়ই আল্লাহ আমাদের অপকার করবেন না।\n\n"
                            + "<b>[সহীহ বুখারী – ৩৩৬৪]</b>\n\n"
                            + "<b>স্থায়ী বসতি ও জুরহুম গোত্রের আগমন:</b>\n\n"
                            + "• জমজম কূপের কারণে ওই অঞ্চল বাসযোগ্য হয়।\n"
                            + "• ইয়ামান থেকে আগত জুরহুম গোত্র পানি দেখে সেখানে স্থায়ীভাবে বসতি স্থাপন করে।\n"
                            + "• তারা হাজেরা (আঃ)-এর অনুমতি নিয়ে থেকে যায় এবং ইসমাইল (আঃ)-কে দত্তক হিসেবে গ্রহণ করে।",
                    "Following divine commandment, Prophet Ibrahim (AS) brought Lady Hajar (AS) and their nursing infant Ismail (AS) to an uninhabited, barren, rocky valley in the desert—the site of present-day Makkah.\n\n"
                            + "رَّبَّنَآ إِنِّيٓ أَسْكَنتُ مِن ذُرِّيَّتِي بِوَادٍ غَيْرِ ذِى زَرْعٍ عِندَ بَيْتِكَ ٱلْمُحَرَّمِ\n\n"
                            + "<b>Meaning:</b>\n"
                            + "\"Our Lord, indeed I have settled some of my descendants in an uncultivated valley near Your sacred House.\"\n\n"
                            + "<b>[Surah Ibrahim: 37]</b>\n\n"
                            + "When Ibrahim (AS) turned to leave, Lady Hajar asked: \"Has Allah commanded you to do this?\" Ibrahim nodded yes. Thereupon she demonstrated supreme trust in Allah, replying: \"Then He will surely never forsake us!\"\n\n"
                            + "<b>[Sahih Bukhari: 3364]</b>\n\n"
                            + "<b>Settlement and Arrival of Jurhum:</b>\n\n"
                            + "• The miraculous emergence of the Zamzam well made the barren valley habitable.\n"
                            + "• The Yemeni tribe of Jurhum saw birds circling over water and sought permission to settle around Zamzam.\n"
                            + "• Lady Hajar granted them permission, establishing the first permanent community around the Holy Sanctuary."
            ));

            // 3. জামজাম কূপের আবির্ভাব
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "জামজাম কূপের আবির্ভাব",
                    "Emergence of the Zamzam Well",
                    "হযরত ইব্রাহিম (আলাইহিস সালাম) আল্লাহর নির্দেশে তাঁর স্ত্রী হাজেরা (আলাইহাস সালাম) ও শিশু সন্তান ইসমাঈল (আলাইহিস সালাম) কে একটি জনমানবহীন ও শুষ্ক মরুভূমিতে রেখে আসেন। এই জায়গাটি ছিল বর্তমানের মক্কা, যেখানে তখন কেউ বসবাস করত না এবং পানি ও খাবারের কোনো উৎসও ছিল না। হযরত হাজেরা (আ.) যখন দেখলেন তাঁর সাথে থাকা পানি ও খাবার ফুরিয়ে গেছে এবং শিশু ইসমাঈল (আ.) তৃষ্ণায় কাঁদছে, তখন তিনি পানি খুঁজতে গিয়ে সাফা ও মারওয়া পাহাড়ের মাঝখানে সাতবার দৌড়ালেন। এ দৌড় আজকের হজ্জ ও উমরাহর একটি অংশ হয়ে গেছে (সাঈ)। শেষমেশ, আল্লাহর কুদরতে, ইসমাঈল (আ.)-এর পায়ের নিচ থেকে একটি পানির ঝরণা ফুঁটে বের হয় এটাই যমযম কূপ। হাজেরা (আ.) সেই পানি জমিয়ে রাখেন এবং বলেন \"زم زم\" অর্থাৎ “আস্তে আস্তে” এখান থেকেই এর নাম হয় \"যমযম\"। এই কূপের পানিতে এত বরকত ছিল যে এটি শুধু ইসমাঈল (আ.)-এর জীবন রক্ষা করেনি, বরং এই অঞ্চলে বসতি স্থাপনের কারণও হয়ে দাঁড়ায়। সময়ের সাথে এটি পরিণত হয় ইসলামের প্রাণকেন্দ্র মক্কা নগরীতে।\n\n"
                            + "رَّبَّনَآ إِنِّيٓ أَسۡكَنتُ مِن ذُرِّيَّتِي بِوَادٍ غَيۡرِ ذِي زَرۡعٍ عِندَ بَيۡتِكَ ٱلۡمُحَرَّمِ رَبَّنَا لِيُقِيمُواْ ٱلصَّلَوٰةَ فَٱجۡعَلۡ أَفۡـِٔدَةٗ مِّنَ ٱلنَّاسِ تَهۡوِيٓ إِلَيۡهِمۡ وَٱرۡزُقۡهُم مِّنَ ٱلثَّمَرَٰتِ لَعَلَّهُمۡ يَشۡكُرُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমাদের পালনকর্তা! আমি আমার সন্তানদেরকে এমন এক উপত্যকায় বসবাস করিয়েছি, যেখানে চাষাবাদের কোনো ব্যবস্থা নেই, আপনার পবিত্র গৃহের কাছে। হে আমাদের রব! যাতে তারা সালাত কায়েম করে; আপনি কিছু মানুষের হৃদয়কে তাদের প্রতি ঝুঁকে দিন, এবং তাদের ফল-মূল দ্বারা রিযিক দিন, যাতে তারা কৃতজ্ঞতা প্রকাশ করে।\n\n"
                            + "<b>[সূরা ইব্রাহিম – ৩৭]</b>\n\n"
                            + "এই হাদীসে রাসূলুল্লাহ ﷺ বিশদভাবে বর্ণনা করেছেন কীভাবে হাজেরা (আ.) পানি খুঁজতে সাফা ও মারওয়ার মাঝে দৌড়ালেন এবং কীভাবে যমযম কূপ সৃষ্টি হলো।\n\n"
                            + "فَجَعَلَتْ تَنْقُزُ حَتَّى أَتَتْ الْمَرْوَةَ، فَنَظَرَتْ هَلْ تَرَى أَحَدًا فَلَمْ تَرَ أَحَدًا، فَرَجَعَتْ إِلَى الصَّفَا، فَفَعَلَتْ ذَلِكَ سَبْعَ مَرَّاتٍ... فَقَالَتْ: زَمْ زَمْ، تُرِيدُ أَنْ تَحْجُزَ الْمَاءَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি (হাজেরা) মারওয়ায় পৌঁছে দেখলেন কেউ নেই, তারপর আবার সাফায় ফিরে এলেন। তিনি এই কাজ সাতবার করলেন... এরপর যখন পানি বের হলো, তখন তিনি বললেন: ‘যমযম!’, অর্থাৎ তিনি পানির প্রবাহ থামাতে চেয়েছিলেন।\n\n"
                            + "<b>[সহীহ বুখারী – ৩৩৬৪]</b>\n\n"
                            + "যমযম কূপের আবির্ভাব আল্লাহর কুদরতের এক জীবন্ত নিদর্শন। এটি হাজেরা (আ.)-এর দৃঢ় ঈমান, সবর, ও তাওয়াক্কুলের পুরস্কার, যা কিয়ামত পর্যন্ত অব্যাহত থাকবে। আজও পৃথিবীর লক্ষ লক্ষ মুসলমান হজ্জ ও উমরাহর সময় এই কূপের পানি পান করেন এবং এটি মুসলিম উম্মাহর ঐতিহাসিক স্মারক হিসেবে টিকে আছে। যমযম কূপ মুসলিমদের জন্য শুধু পানি নয়, বরং আল্লাহর রহমতের নিদর্শন।",
                    "When provisions ran out and infant Ismail cried out in severe thirst, Lady Hajar ran frantically seven times between the hills of Safa and Marwah seeking water—a desperate search commemorated forever as the rite of Sa'i during Hajj and Umrah.\n\n"
                            + "By divine mercy, Angel Jibreel (Gabriel) struck the ground with his wing at the heels of infant Ismail, causing a spring of pure water to gush forth.\n\n"
                            + "رَّبَّنَآ إِنِّيٓ أَسۡكَنتُ مِن ذُرِّيَّتِي بِوَادٍ غَيۡرِ ذِي زَرۡعٍ عِندَ بَيۡتِكَ ٱلۡمُحَرَّمِ رَبَّنَا لِيُقِيمُواْ ٱلصَّلَوٰةَ فَٱجۡعَلۡ أَفۡـِٔدَةٗ مِّنَ ٱلنَّاسِ تَهۡوِيٓ إِلَيۡهِمۡ وَٱرۡزُقۡهُم مِّنَ ٱلثَّمَرَٰتِ لَعَلَّهُمۡ يَشۡكُرُونَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"Our Lord, I have settled some of my descendants in an uncultivated valley near Your sacred House, our Lord, that they may establish prayer. So make hearts among the people incline toward them and provide for them from the fruits that they might be grateful.\"\n\n"
                            + "<b>[Surah Ibrahim: 37]</b>\n\n"
                            + "فَجَعَلَتْ تَنْقُزُ حَتَّى أَتَتْ الْمَرْوَةَ، فَنَظَرَتْ هَلْ تَرَى أَحَدًا فَلَمْ تَرَ أَحَدًا، فَرَجَعَتْ إِلَى الصَّفَا، فَفَعَلَتْ ذَلِكَ سَبْعَ مَرَّاتٍ... فَقَالَتْ: زَمْ زَمْ، تُرِيدُ أَنْ تَحْجُزَ الْمَاءَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"She ran until she reached Marwah, looked around to see anyone but saw none; then returned to Safa. She did this seven times... When water gushed, she cried 'Zam Zam' (contain yourself) wishing to dam the spring.\"\n\n"
                            + "<b>[Sahih Bukhari: 3364]</b>\n\n"
                            + "The well of Zamzam stands as a perpetual living miracle of Allah's mercy and reward for profound faith and reliance."
            ));

            // 4. মক্কার গোত্র ‘জুরহুম’ এর আগমন
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "মক্কার গোত্র ‘জুরহুম’ এর আগমন",
                    "Arrival of the Jurhum Tribe in Makkah",
                    "হযরত ইসমাঈল (আলাইহিস সালাম) ও তাঁর মা হাজেরা (আলাইহাস সালাম) যখন আল্লাহর আদেশে নির্জন মরুভূমিতে (বর্তমান মক্কায়) বসবাস করতে শুরু করেন, তখন সেখানে কোনো জনবসতি ছিল না। এই অঞ্চলে যমযম কূপ আবির্ভূত হওয়ার পর আশপাশের কিছু যাযাবর গোত্র সেই অঞ্চলে পানি ও বসতি খোঁজার জন্য ছড়িয়ে পড়ে। তখনই ‘জুরহুম’ নামক এক প্রাচীন আরব গোত্র হাজেরা (আ.)-এর কাছে আসে।\n\n"
                            + "<b>জুরহুম গোত্র কারা:</b>\n\n"
                            + "‘জুরহুম (جُرْهُم)’ ছিল ইয়ামান (বর্তমান দক্ষিণ আরব)-এর এক প্রাচীন ক্বাহতানী গোত্র। তারা কাবিল ও সাম প্রজাতির বংশধর, আরবের প্রাচীন অধিবাসী। এক পর্যায়ে তারা হিজাজ অঞ্চলে (যেখানে মক্কা অবস্থিত) চলে আসে, যখন তারা পানির সন্ধানে ছিল।\n\n"
                            + "<b>ইব্রাহিম (আ.) ও জুরহুম গোত্রের সম্পর্ক:</b>\n\n"
                            + "• যমযম কূপের আশেপাশে স্থায়ী বসতি গড়ে ওঠে।\n"
                            + "• জুরহুম গোত্র হাজেরা (আ.)-এর কাছে থাকার অনুমতি চায়, এবং তিনি তা মঞ্জুর করেন এই শর্তে যে পানি ব্যবহারে তাঁদের অধিকার থাকবে, কিন্তু মালিকানা থাকবে না।\n"
                            + "• ইসমাঈল (আ.)-এর লালন-পালন হয় এই জুরহুমিদের মাঝে, আরব ভাষাও শেখেন।\n"
                            + "• পরবর্তীতে ইসমাঈল (আ.)-এর বিয়ে হয় জুরহুম গোত্রের এক মহিলার সঙ্গে, যা এই গোত্রের সঙ্গে তাঁর আত্মীয়তা আরও ঘনিষ্ঠ করে তোলে।\n\n"
                            + "فَبَيْنَمَا هُمْ كَذَٰلِكَ إِذْ مَرَّ بِهِمْ نَفَرٌ مِنْ جُرْهُمٍ فِي مَقَفَلٍ لَهُمْ مِنْ كَمَاةٍ لَهُمْ بِتِهَامَةَ، فَرَأَوْا طَائِرًا عَائِفًا، فَقَالُوا: إِنَّ هَذَا الطَّائِرَ لَيَدُورُ عَلَى الْمَاءِ، فَأَرْسَلُوا جَرِيًّا، فَإِذَا هُمْ بِالْمَاءِ، فَأَتَوْهُمْ...\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তাদের অবস্থানকালে জুরহুম গোত্রের কিছু লোক তিহামা থেকে ফিরছিল। তারা এক পাখিকে পানির উপর চক্কর কাটতে দেখে বুঝে নেয় সেখানে পানি আছে। তারা একজন লোক পাঠায়, আর সে যেয়ে যমযমের পানি আবিষ্কার করে। পরে তারা হাজেরা (আ.)-এর কাছে থাকার অনুমতি চায় এবং পানির পাশে বসতি গড়ে তোলে।\n\n"
                            + "<b>[সহীহ বুখারী- ৩৩৬৪]</b>\n\n"
                            + "জুরহুম গোত্রের আগমন ছিল আল্লাহর কুদরতের এক অনুপম নিদর্শন। পানি ও হিফাযতের জন্য প্রেরিত এই গোত্র পরবর্তীতে ইসমাঈল (আ.)-এর জীবন, পরিবার, এবং আরব সংস্কৃতির বিকাশে প্রধান ভূমিকা রাখে। তারা ছিল ইসলাম-পূর্ব যুগের সেই ঐতিহাসিক গোত্র, যাদের মাধ্যমে মক্কা নগরী জনবসতিপূর্ণ ও রাজনৈতিকভাবে গুরুত্বপূর্ণ হয়ে ওঠে।",
                    "Following the appearance of Zamzam, the ancient Qahtani Arab tribe of Jurhum arrived from Yemen traveling through Tihamah. Spotting birds circling the valley, they recognized signs of water and approached Lady Hajar.\n\n"
                            + "<b>Who were the Jurhum Tribe:</b>\n\n"
                            + "Jurhum (جُرْهُم) was an ancient noble Qahtani tribe originating from Yemen. They settled in the Hijaz valley with Lady Hajar's permission on the condition that they would have access to the water without claiming ownership.\n\n"
                            + "<b>Impact on Ismail's Life:</b>\n\n"
                            + "• Young Ismail grew up among the Jurhum people and learned pure classical Arabic from them.\n"
                            + "• He later married a virtuous woman from Jurhum, solidifying familial ties.\n\n"
                            + "فَبَيْنَمَا هُمْ كَذَٰلِكَ إِذْ مَرَّ بِهِمْ نَفَرٌ مِنْ جُرْهُمٍ فِي مَقَفَلٍ لَهُمْ مِنْ كَمَاةٍ لَهُمْ بِتِهَامَةَ، فَرَأَوْا طَائِرًا عَائِفًا، فَقَالُوا: إِنَّ هَذَا الطَّائِرَ لَيَدُورُ عَلَى الْمَاءِ، فَأَرْسَلُوا جَرِيًّا، فَإِذَا هُمْ بِالْمَاءِ، فَأَتَوْهُمْ...\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"While they were there, a caravan from Jurhum passing through Tihamah saw a bird hovering and deduced there must be water. They sent an emissary who discovered the spring of Zamzam. They then sought permission from Hajar and settled alongside her.\"\n\n"
                            + "<b>[Sahih Bukhari: 3364]</b>"
            ));

            // 5. কিশোর বয়সে চরিত্র গঠন ও নৈতিকতা
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "কিশোর বয়সে চরিত্র গঠন ও নৈতিকতা",
                    "Character Building and Morals in Youth",
                    "হযরত ইসমাঈল (আলাইহিস সালাম) ছিলেন এমন একজন নবী, যিনি কিশোর বয়স থেকেই আত্মশুদ্ধি, আনুগত্য, ধৈর্য এবং তাকওয়ার উজ্জ্বল দৃষ্টান্ত স্থাপন করেন। তিনি ছিলেন হযরত ইব্রাহিম (আলাইহিস সালাম) এর প্রথম পুত্র এবং ইসলামের গুরুত্বপূর্ণ ব্যক্তিত্ব, যার জীবন মুসলিম উম্মাহর ইতিহাসে গভীর প্রভাব রেখেছে। ইসমাঈল (আ.)-এর কিশোর বয়সের মধ্যে এমন সব গুণাবলি গড়ে উঠেছিল যা একজন সত্যিকারের মু’মিন ও ভবিষ্যতের নবীর জন্য অপরিহার্য। তিনি পিতা-মাতার প্রতি শ্রদ্ধাশীল, সত্যবাদী, আত্মত্যাগে প্রস্তুত, এবং আল্লাহর আদেশ পালনে সর্বদা আন্তরিক ছিলেন। তাঁর চরিত্র ও নৈতিকতা প্রমাণ করে, কিশোর বয়সেই মানুষ বড় দায়িত্বের উপযুক্ত হয়ে উঠতে পারে।\n\n"
                            + "<b>পিতার আদেশ মান্য করে আত্মত্যাগে প্রস্তুতি:</b>\n\n"
                            + "হযরত ইব্রাহিম (আ.) যখন আল্লাহর আদেশে স্বপ্নে দেখেন যে, তাঁকে তাঁর পুত্র ইসমাঈল (আ.)-কে কুরবানি করতে হবে, তখন তিনি তা ইসমাঈল (আ.)-কে জানালে তাঁর জবাব ছিল\n\n"
                            + "فَلَمَّا بَلَغَ مَعَهُ السَّعْيَ قَالَ يَا بُنَيَّ إِنِّي أَرَىٰ فِي الْمَنَامِ أَنِّي أَذْبَحُكَ فَانظُرْ مَاذَا تَرَىٰ ۚ قَالَ يَا أَبَتِ افْعَلْ مَا تُؤْمَرُ ۖ سَتَجِدُنِي إِنْ شَاءَ اللَّهُ مِنَ الصَّابِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন সে (ইসমাঈল) তার পিতার সাথে চলাফেরা করার উপযুক্ত বয়সে পৌঁছাল, তখন সে বলল: ‘হে আমার পুত্র! আমি স্বপ্নে দেখেছি যে, আমি তোমাকে কুরবানি করছি; তুমি চিন্তা করো, তোমার কী মত?\n\n"
                            + "সে বলল: ‘হে আমার পিতা! আপনি যা আদিষ্ট হয়েছেন তা করুন; ইনশাআল্লাহ, আপনি আমাকে ধৈর্যশীলদের অন্তর্ভুক্ত পাবেন।\n\n"
                            + "<b>[আস-সাফফাত – ১০২]</b>\n\n"
                            + "এই আয়াত ইসমাঈল (আ.)-এর তিনটি গুণ স্পষ্ট করে:\n\n"
                            + "• পিতার প্রতি সম্মান ও আনুগত্য\n"
                            + "• আল্লাহর আদেশ পালনে পূর্ণ আত্মসমর্পণ\n"
                            + "• ধৈর্য ও আত্মনিয়ন্ত্রণ\n\n"
                            + "<b>সত্যবাদিতা ও দায়িত্বশীলতা:</b>\n\n"
                            + "ইসমাঈল (আ.)-এর চরিত্র সম্পর্কে আল্লাহ বলেন,\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِسْمَاعِيلَ ۚ إِنَّهُ كَانَ صَادِقَ الْوَعْدِ وَكَانَ رَسُولًا نَبِيًّا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর কিতাবে ইসমাঈলকে স্মরণ করো। নিশ্চয়ই সে ছিল প্রতিশ্রুতি রক্ষায় অত্যন্ত সত্যনিষ্ঠ; এবং সে ছিল একজন রাসূল ও নবী।\n\n"
                            + "<b>[সূরা মারইয়াম – ৫৪]</b>\n\n"
                            + "হযরত ইসমাঈল (আ.) তাঁর কিশোর বয়সেই এমন এক চরিত্র ও নৈতিক ভিত্তি গড়ে তুলেছিলেন, যা পরবর্তীতে তাঁকে একজন সফল নবী হিসেবে প্রতিষ্ঠিত করে। কুরআনের বর্ণনা ও তাঁর জীবন থেকে শিক্ষা নিয়ে বলা যায়, আল্লাহর প্রতি ভয়, পিতার আদেশ পালন, সত্যবাদিতা, এবং ধৈর্যের মাধ্যমে একজন কিশোরও আখিরাত ও দুনিয়ার সাফল্য অর্জন করতে হতে পারে।",
                    "From his youth, Prophet Ismail (AS) exemplified profound righteousness, unquestioning submission to divine decrees, extraordinary patience, and unwavering truthfulness.\n\n"
                            + "<b>Supreme Readiness for Sacrifice:</b>\n\n"
                            + "When Ibrahim (AS) informed his youthful son of the divine vision commanding his sacrifice, Ismail responded with exemplary fortitude:\n\n"
                            + "فَلَمَّا بَلَغَ مَعَهُ السَّعْيَ قَالَ يَا بُنَيَّ إِنِّي أَرَىٰ فِي الْمَنَامِ أَنِّي أَذْبَحُكَ فَانظُرْ مَاذَا تَرَىٰ ۚ قَالَ يَا أَبَتِ افْعَلْ مَا تُؤْمَرُ ۖ سَتَجِدُنِي إِنْ شَاءَ اللَّهُ مِنَ الصَّابِرِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And when he reached with him [the age of] exertion, he said, 'O my son, indeed I have seen in a dream that I [must] sacrifice you, so see what you think.' He said, 'O my father, do as you are commanded. You will find me, if Allah wills, of the steadfast.'\"\n\n"
                            + "<b>[Surah As-Saffat: 102]</b>\n\n"
                            + "<b>Unwavering Truthfulness & Integrity:</b>\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِسْمَاعِيلَ ۚ إِنَّهُ كَانَ صَادِقَ الْوَعْدِ وَكَانَ رَسُولًا نَبِيًّا\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And mention in the Book, Ismail. Indeed, he was true to his promise, and he was a messenger and a prophet.\"\n\n"
                            + "<b>[Surah Maryam: 54]</b>"
            ));

            // 6. পিতার আনুগত্য
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "পিতার আনুগত্য",
                    "Obedience to His Father",
                    "হযরত ইসমাঈল (আলাইহিস সালাম)-এর জীবনে অন্যতম উজ্জ্বল বৈশিষ্ট্য ছিল পিতার প্রতি পূর্ণ আনুগত্য ও শ্রদ্ধা। ইসলাম ধর্মে পিতা-মাতার প্রতি সদ্ব্যবহার ও আদেশ মান্য করার গুরুত্ব বহুবার আলোচিত হয়েছে। হযরত ইসমাঈল (আ.) এই বিষয়ে একটি অনন্য দৃষ্টান্ত—বিশেষ করে তাঁর কিশোর বয়সে তিনি যেভাবে তাঁর পিতার আদেশ ও আল্লাহর নির্দেশ একত্রে মেনে নিয়েছিলেন, তা মানব ইতিহাসে বিরল। তিনি তাঁর পিতা হযরত ইব্রাহিম (আলাইহিস সালাম)-এর সঙ্গেই জীবন গঠন করেন, দাওয়াতি কাজে পাশে থাকেন, এমনকি নিজের জীবন কুরবানি দেওয়ার জন্যও প্রস্তুত থাকেন। এটি প্রমাণ করে যে, একজন কিশোর সন্তানও যদি সৎ পথপ্রাপ্ত হয়, তবে সে পিতার সহচর হয়ে নবীর দায়িত্ব পালনের যোগ্যতা অর্জন করতে পারে।\n\n"
                            + "<b>ইসমাঈল (আ.)-এর শ্রদ্ধা ও পূর্ণ আত্মসমর্পণ:</b>\n\n"
                            + "হযরত ইব্রাহিম (আ.) আল্লাহর পক্ষ থেকে স্বপ্নে তাঁর পুত্রকে কুরবানি করার নির্দেশ পান। তিনি যখন সেই আদেশ ইসমাঈল (আ.)-কে জানালেন, তখন তাঁর জবাব ছিল অত্যন্ত আন্তরিক ও বিশ্বাসে পরিপূর্ণ।\n\n"
                            + "فَلَمَّا بَلَغَ مَعَهُ السَّعْيَ قَالَ يَا بُنَيَّ إِنِّي أَرَىٰ فِي الْمَنَامِ أَنِّي أَذْبَحُكَ فَانظُرْ مَاذَا تَرَىٰ ۚ قَالَ يَا أَبَتِ افْعَلْ مَا تُؤْمَرُ ۖ سَتَجِدُنِي إِنْ شَاءَ اللَّهُ مِنَ الصَّابِرِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন সে (ইসমাঈল) তার পিতার সঙ্গে চলাফেরা করার উপযুক্ত বয়সে পৌঁছাল, তখন সে বলল: হে আমার পুত্র! আমি স্বপ্নে দেখছি যে, আমি তোমাকে কুরবানি করছি। তুমি কী মনে করো?\n\n"
                            + "সে বলল: হে আমার পিতা! আপনি যা আদিষ্ট হয়েছেন তা করুন। ইনশাআল্লাহ, আপনি আমাকে ধৈর্যশীলদের অন্তর্ভুক্ত পাবেন।\n\n"
                            + "<b>[আস-সাফফাত – ১০২]</b>\n\n"
                            + "হযরত ইসমাঈল (আ.) তাঁর পিতার প্রতি যে আনুগত্য ও ভালোবাসা প্রদর্শন করেছেন, তা শুধু একটি পারিবারিক আদর্শ নয়, বরং তা একটি নববী আখলাকের নিদর্শন। ইসলাম এই শিক্ষাকে চূড়ান্ত মর্যাদা দিয়েছে যেখানে সন্তান পিতার কথায় কেবল সম্মান প্রদর্শনই করে না, বরং আল্লাহর আদেশ হিসেবে তা বাস্তবায়নের জন্যও প্রস্তুত থাকে।",
                    "The profound filial devotion shown by Prophet Ismail (AS) toward his father Ibrahim (AS) represents a timeless model of Islamic character and prophetic virtue.\n\n"
                            + "<b>Complete Surrender to Divine and Parental Will:</b>\n\n"
                            + "فَلَمَّا بَلَغَ مَعَهُ السَّعْيَ قَالَ يَا بُنَيَّ إِنِّي أَرَىٰ فِي الْمَنَامِ أَنِّي أَذْبَحُكَ فَانظُرْ مَاذَا تَرَىٰ ۚ قَالَ يَا أَبَتِ افْعَلْ مَا تُؤْمَرُ ۖ سَتَجِدُنِي إِنْ شَاءَ اللَّهُ مِنَ الصَّابِرِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And when he reached with him [the age of] exertion, he said, 'O my son, indeed I have seen in a dream that I [must] sacrifice you, so see what you think.' He said, 'O my father, do as you are commanded. You will find me, if Allah wills, of the steadfast.'\"\n\n"
                            + "<b>[Surah As-Saffat: 102]</b>\n\n"
                            + "This monumental dialogue highlights how a righteous son becomes a steadfast pillar of strength and support for his father in fulfilling divine missions."
            ));

            // 7. কোরবানির ঘটনা
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "কোরবানির ঘটনা",
                    "The Event of Sacrifice",
                    "কোরবানির ঘটনা ইসলামের অন্যতম গুরুত্বপূর্ণ ও শিক্ষামূলক ঘটনা। এটি কেবল ইতিহাস নয়; বরং আত্মত্যাগ, আনুগত্য, ঈমান, ধৈর্য ও আল্লাহর নির্দেশ পালনের চূড়ান্ত নিদর্শন। কুরআনের ভাষায় এটি ছিল এক “স্পষ্ট পরীক্ষা”, যেখানে হযরত ইব্রাহিম (আ.) ও তাঁর কিশোর পুত্র হযরত ইসমাঈল (আ.) আল্লাহর আদেশ মান্য করতে গিয়ে নিজেদের সর্বোচ্চ আত্মোৎসর্গের মনোভাব প্রকাশ করেন। এই ঘটনাই ইসলামে কোরবানির পবিত্র বিধানের ভিত্তি, যা প্রতি বছর ঈদুল আযহা উপলক্ষে মুসলমানদের পালন করতে বলা হয়েছে।\n\n"
                            + "فَلَمَّا بَلَغَ مَعَهُ السَّعْيَ قَالَ يَا بُنَيَّ إِنِّي أَرَىٰ فِي الْمَنَامِ أَنِّي أَذْبَحُكَ فَانظُرْ مَاذَا تَرَىٰ ۚ قَالَ يَا أَبَتِ افْعَلْ مَا تُؤْمَرُ ۖ سَتَجِدُنِي إِنْ شَاءَ اللَّهُ مِنَ الصَّابِرِينَ (١٠٢)\n\n"
                            + "فَلَمَّا أَسْلَمَا وَتَلَّهُ لِلْجَبِينِ (١٠٣)\n\n"
                            + "وَنَادَيْنَاهُ أَنْ يَا إِبْرَاهِيمُ (١٠٤)\n\n"
                            + "قَدْ صَدَّقْتَ الرُّؤْيَا ۚ إِنَّا كَذَٰلِكَ نَجْزِي الْمُحْسِنِينَ (١٠٥)\n\n"
                            + "إِنَّ هَٰذَا لَهُوَ الْبَلَاءُ الْمُبِينُ (١٠٦)\n\n"
                            + "وَفَدَيْنَاهُ بِذِبْحٍ عَظِيمٍ (١٠৭)\n\n"
                            + "<b>[সূরা আস-সাফফাত - ১০২–১০৭]</b>\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর যখন সে (ইসমাঈল) চলাফেরার উপযুক্ত বয়সে পৌঁছাল, ইব্রাহিম বললেন: হে আমার প্রিয় পুত্র! আমি স্বপ্নে দেখেছি যে, আমি তোমাকে কুরবানি করছি; তুমি কী মনে করো?\n\n"
                            + "সে বলল: হে আমার পিতা! আপনি যা করতে আদিষ্ট হয়েছেন, তা করুন। ইনশাআল্লাহ আপনি আমাকে ধৈর্যশীলদের অন্তর্ভুক্ত পাবেন।\n\n"
                            + "অতঃপর যখন তারা উভয়ে আত্মসমর্পণ করল এবং ইব্রাহিম তাকে শোয়ালেন কাত করে (জবাইয়ের জন্য), তখন আমি (আল্লাহ) ডাক দিলাম: হে ইব্রাহিম! নিশ্চয় তুমি স্বপ্নের সত্যতা প্রমাণ করেছো। আমি এমনইভাবে পুরস্কার দিয়ে থাকি সৎকর্মশীলদের।\n\n"
                            + "নিশ্চয়ই এটি ছিল এক স্পষ্ট পরীক্ষা। আর আমি তাকে এক মহান কুরবানির মাধ্যমে মুক্ত করে দিলাম।”\n\n"
                            + "হযরত ইব্রাহিম (আ.) ও ইসমাঈল (আ.)-এর কোরবানির ঘটনা এক অনন্য আদর্শ। তারা আমাদের শিখিয়ে গেছেন, কীভাবে একজন মু’মিন আল্লাহর জন্য নিজের সব কিছু উৎসর্গ করতে প্রস্তুত থাকে। এ কারণেই ইসলামে কোরবানি শুধু আনুষ্ঠানিকতা নয়; বরং তা এক ঈমানি পরীক্ষা, আল্লাহর আদেশের প্রতি আনুগত্য, এবং তাঁর সন্তুষ্টি অর্জনের চেষ্টা।",
                    "The supreme trial of sacrifice demonstrated by Ibrahim (AS) and Ismail (AS) forms the very foundation of Eid al-Adha and the Islamic institution of Qurbani.\n\n"
                            + "فَلَمَّا بَلَغَ مَعَهُ السَّعْيَ قَالَ يَا بُنَيَّ إِنِّي أَرَىٰ فِي الْمَنَامِ أَنِّي أَذْبَحُكَ فَانظُرْ مَاذَا تَرَىٰ ۚ قَالَ يَا أَبَتِ افْعَلْ مَا تُؤْمَرُ ۖ سَتَجِدُنِي إِنْ شَاءَ اللَّهُ مِنَ الصَّابِرِينَ (١٠٢)\n\n"
                            + "فَلَمَّا أَسْلَمَا وَتَلَّهُ لِلْجَبِينِ (١٠٣)\n\n"
                            + "وَنَادَيْنَاهُ أَنْ يَا إِبْرَاهِيمُ (١٠٤)\n\n"
                            + "قَدْ صَدَّقْتَ الرُّؤْيَا ۚ إِنَّا كَذَٰلِكَ نَجْزِي الْمُحْسِنِينَ (١٠٥)\n\n"
                            + "إِنَّ هَٰذَا لَهُوَ الْبَلَاءُ الْمُبِينُ (١٠٦)\n\n"
                            + "وَفَدَيْنَاهُ بِذِبْحٍ عَظِيمٍ (١٠৭)\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And when they had both submitted and he put him down upon his forehead, We called to him, 'O Ibrahim, you have fulfilled the vision.' Indeed, We thus reward the doers of good. Indeed, this was the clear trial. And We ransomed him with a great sacrifice.\"\n\n"
                            + "<b>[Surah As-Saffat: 102-107]</b>\n\n"
                            + "Allah spared Ismail (AS) and sent a magnificent ram from Paradise to be sacrificed in his stead, establishing a sunnah observed by believers until the end of time."
            ));

            // 8. বায়তুল্লাহ নির্মাণে অংশগ্রহণ
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "বায়তুল্লাহ নির্মাণে অংশগ্রহণ",
                    "Participation in Building the Baytullah",
                    "ইসলামের ইতিহাসে অন্যতম গুরুত্বপূর্ণ ঘটনা হলো বায়তুল্লাহ বা কা‘বা শরীফের নির্মাণ। এটি শুধু একটি স্থাপনা নয়, বরং এটি হল তাওহীদের প্রতীক এবং মানবজাতির জন্য নির্মিত প্রথম ইবাদতের ঘর। এই গৃহ নির্মাণ করেছেন আল্লাহর দুই মহান নবী—হযরত ইব্রাহিম (আ.) ও তাঁর পুত্র ইসমাঈল (আ.) । আল্লাহ তাআলা তাঁদের এই কাজকে কুরআনে বিশেষভাবে স্মরণ করেছেন এবং তাঁদের দো‘আগুলোও সংরক্ষণ করেছেন। কা‘বার নির্মাণ ও পুনর্নির্মাণের ইতিহাস শুরু হয় তাদের হাত ধরেই।\n\n"
                            + "وَإِذْ يَرْفَعُ إِبْرَاهِيمُ الْقَوَاعِدَ مِنَ الْبَيْتِ وَإِسْمَاعِيلُ ۖ رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর স্মরণ করো, যখন ইব্রাহিম ও ইসমাঈল গৃহের ভিত্তি স্থাপন করছিল, তারা বলছিল: ‘হে আমাদের প্রতিপালক! আপনি আমাদের পক্ষ থেকে (এই কাজ) কবুল করে নিন। নিশ্চয়ই আপনি সর্বশ্রোতা, সর্বজ্ঞ।\n\n"
                            + "এই আয়াত থেকে বোঝা যায়,\n\n"
                            + "• কা‘বা নির্মাণের সময় উভয় নবী নিজ হাতে কাজ করছিলেন।\n"
                            + "• তারা নির্মাণ করতে করতেই আল্লাহর কাছে দো‘আ করছিলেন।\n"
                            + "• তারা নির্মাণকে ইবাদত হিসেবে গণ্য করতেন।\n\n"
                            + "<b>ঐতিহাসিক প্রেক্ষাপট:</b>\n\n"
                            + "হযরত ইসমাঈল (আ.) ও তাঁর মা হাজেরা (আ.)-কে আল্লাহর আদেশে মক্কা নগরীতে স্থাপন করার অনেক বছর পর ইব্রাহিম (আ.) পুনরায় তাঁদের কাছে আসেন। তখন আল্লাহর পক্ষ থেকে তাঁকে নির্দেশ দেওয়া হয়, যেন তিনি তাঁর পুত্রকে নিয়ে কা‘বার গৃহ পুনর্নির্মাণ করেন।\n\n"
                            + "• ইসমাঈল (আ.) মাটি ও পাথর বহন করতেন\n"
                            + "• ইব্রাহিম (আ.) দেওয়াল নির্মাণ করতেন\n"
                            + "• \"মাকাম ইব্রাহিম\" পাথরটি সেই স্থান, যেখানে দাঁড়িয়ে তিনি কা‘বা নির্মাণ করেন\n"
                            + "• প্রতিটি ধাপে তাঁরা আল্লাহর কাছে কবুলের দো‘আ করতেন\n\n"
                            + "হযরত ইব্রাহিম (আ.) ও ইসমাঈল (আ.)-এর কা‘বা নির্মাণের ঘটনা মুসলিম উম্মাহর জন্য এক গৌরবময় ইতিহাস ও চিরন্তন শিক্ষা। এটি শুধু স্থাপত্যিক কীর্তি নয়, বরং তা আত্মত্যাগ, ইখলাস, পরিবারভিত্তিক দ্বীনি কাজ এবং ইবাদতের প্রতি একাগ্রতার জীবন্ত নিদর্শন।",
                    "Prophet Ibrahim (AS) and his son Ismail (AS) were divinely commissioned to raise the foundations of the Holy Ka'bah, establishing the focal sanctuary of monotheism on earth.\n\n"
                            + "وَإِذْ يَرْفَعُ إِبْرَاهِيمُ الْقَوَاعِدَ مِنَ الْبَيْتِ وَإِسْمَاعِيلُ ۖ رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And [mention] when Ibrahim was raising the foundations of the House and [with him] Ismail, [saying], 'Our Lord, accept [this] from us. Indeed You are the Hearing, the Knowing.'\"\n\n"
                            + "<b>[Surah Al-Baqarah: 127]</b>\n\n"
                            + "<b>Division of Labor & Devotion:</b>\n\n"
                            + "• Ismail (AS) fetched and carried stones while Ibrahim (AS) erected the masonry walls.\n"
                            + "• Ibrahim stood upon the stone known as \"Maqam Ibrahim\" which miraculously softened beneath his feet as the structure rose.\n"
                            + "• Throughout the labor, both prophets engaged in continuous humble supplication."
            ));

            // 9. আল্লাহর নৈকট্যপ্রাপ্ত নবী
            list.add(new ProphetOverviewTopicItem(
                    9,
                    "আল্লাহর নৈকট্যপ্রাপ্ত নবী",
                    "The Near-Stationed Prophet to Allah",
                    "হযরত ইসমাঈল (আলাইহিস সালাম) ছিলেন হযরত ইব্রাহিম (আলাইহিস সালাম)-এর প্রথম সন্তান এবং ইসলামের ইতিহাসে এক গুরুত্বপূর্ণ ও সম্মানিত নবী। তিনি শুধু নবুওতের মর্যাদাই পাননি, বরং আল্লাহর বিশেষ নৈকট্য লাভকারী নবীদের অন্যতম ছিলেন। তাঁর জীবনে যেমন ছিল আল্লাহর আদেশে আত্মসমর্পণ, তেমনি ছিল ঈমান, তাকওয়া, ধৈর্য ও দায়িত্বশীলতার গভীর প্রতিফলন। তিনি শৈশব থেকেই আল্লাহর পথে গড়ে উঠেন, পিতার আদেশ মান্য করে নিজেকে কুরবানির জন্য প্রস্তুত করেন, মক্কা নগরীর গোড়াপত্তনে অংশ নেন, এবং কা‘বা নির্মাণে নিজ হাতে শ্রম দেন। তাঁর চরিত্র, কর্ম ও দাওয়াতি প্রচেষ্টা প্রমাণ করে যে, তিনি ছিলেন আল্লাহর সন্তুষ্টিপ্রাপ্ত একজন অনুগত বান্দা এবং নৈকট্যপ্রাপ্ত রাসূল।\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِسْمَاعِيلَ ۚ إِنَّهُ كَانَ صَادِقَ الْوَعْدِ وَكَانَ رَسُولًا نَبِيًّا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর কিতাবে ইসমাঈলকে স্মরণ করো। নিশ্চয়ই তিনি ছিলেন প্রতিশ্রুতি পালনকারী, আর তিনি ছিলেন রাসূল এবং নবী।\n\n"
                            + "<b>[সূরা মারইয়াম – ৫৪]</b>\n\n"
                            + "এই আয়াতে আল্লাহ তাআলা ইসমাঈল (আ.)-কে তিনটি গুণে ভূষিত করেছেন, \n\n"
                            + "• সত্যনিষ্ঠ প্রতিশ্রুতি পালনকারী\n"
                            + "• আল্লাহর প্রেরিত রাসূল\n"
                            + "• নবুওতের মর্যাদায় অধিষ্ঠিত\n\n"
                            + "এই মর্যাদা প্রমাণ করে যে তিনি ছিলেন আল্লাহর বিশেষ অনুগ্রহপ্রাপ্ত এবং নৈকট্যশীল নবী।\n\n"
                            + "وَكَانَ يَأْمُرُ أَهْلَهُ بِالصَّلَاةِ وَالزَّكَاةِ وَكَانَ عِندَ رَبِّهِ مَرْضِيًّا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি তাঁর পরিবারকে সালাত ও যাকাত আদায়ের আদেশ দিতেন এবং তিনি তাঁর প্রতিপালকের কাছে প্রিয় ছিলেন।\n\n"
                            + "<b>[সূরা মারইয়াম – ৫৫]</b>\n\n"
                            + "রাসূলুল্লাহ ﷺ ইসমাঈল (আ.)-এর উত্তরসূরি ছিলেন এবং বারবার তাঁকে “আবী ইসমাঈল” (আমার পিতা ইসমাঈল) বলে উল্লেখ করতেন। কা‘বা শরীফ, যমযম, সাঈ এ সবই তাঁর জীবনের অংশ হয়ে হজের গুরুত্বপূর্ণ অংশে পরিণত হয়েছে। এটি প্রমাণ করে, তাঁর জীবনের প্রতিটি দিক আল্লাহর দরবারে সম্মানিত হয়েছে।",
                    "Prophet Ismail (AS) was distinguished with immense divine favor and was named directly by Allah with high praise in the Quran.\n\n"
                            + "وَاذْكُرْ فِي الْكِتَابِ إِسْمَاعِيلَ ۚ إِنَّهُ كَانَ صَادِقَ الْوَعْدِ وَكَانَ رَسُولًا نَبِيًّا\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And mention in the Book, Ismail. Indeed, he was true to his promise, and he was a messenger and a prophet.\"\n\n"
                            + "<b>[Surah Maryam: 54]</b>\n\n"
                            + "وَكَانَ يَأْمُرُ أَهْلَهُ بِالصَّلَاةِ وَالزَّكَاةِ وَكَانَ عِندَ رَبِّهِ مَرْضِيًّا\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And he used to enjoin on his people prayer and zakah and was to his Lord pleasing.\"\n\n"
                            + "<b>[Surah Maryam: 55]</b>\n\n"
                            + "The Holy Prophet Muhammad ﷺ proudly referred to Ismail (AS) as \"My father Ismail,\" and core rites of Islamic pilgrimage—the Ka'bah, Zamzam, and Sa'i—forever commemorate his blessed legacy."
            ));

            // 10. সন্তানসন্ততি ও বংশধারা
            list.add(new ProphetOverviewTopicItem(
                    10,
                    "সন্তানসন্ততি ও বংশধারা",
                    "Children and Lineage",
                    "হযরত ইসমাঈল (আলাইহিস সালাম) ছিলেন হযরত ইব্রাহিম (আলাইহিস সালাম)-এর প্রথম পুত্র এবং আরব জাতির আদি পুরুষ। তাঁর বংশধারা থেকেই পরবর্তীতে সর্বশেষ ও সর্বশ্রেষ্ঠ নবী মুহাম্মদ ﷺ আগমন করেন। এইভাবে, হযরত ইসমাঈল (আ.) শুধু একজন নবী হিসেবেই গুরুত্বপূর্ণ নন; বরং তাঁর সন্তানসন্ততি ও বংশধারাও ইসলামের ইতিহাসে গুরুত্বপূর্ণ ও পূণ্যময় ভূমিকা রেখেছে। ইব্রাহিম (আ.)-এর দো‘আ: নেক সন্তান লাভের জন্য\n\n"
                            + "رَبِّ هَبْ لِي مِنَ الصَّالِحِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমার প্রতিপালক! আমাকে এক সৎ সন্তান দান করুন।\n\n"
                            + "এই দো‘আর ফলেই আল্লাহ তাআলা তাঁকে ইসমাঈল (আ.)-এর সুসংবাদ দেন।\n\n"
                            + "<b>[আস-সাফফাত -১০০]</b>\n\n"
                            + "<b>তাঁর বংশধারা থেকে রাসূলুল্লাহ ﷺ-এর আগমন:</b>\n\n"
                            + "হযরত ইব্রাহিম (আ.) কা‘বা নির্মাণের সময় দো‘আ করেছিলেন,\n\n"
                            + "رَبَّنَا وَابْعَثْ فِيهِمْ رَسُولًا مِّنْهُمْ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমাদের প্রতিপালক! তাদের (ইসমাঈলের বংশধরদের) মধ্য থেকে একজন রাসূল প্রেরণ করুন।\n\n"
                            + "এই দো‘আর পরিণতিতেই বহু শতাব্দী পরে হযরত মুহাম্মদ ﷺ জন্মগ্রহণ করেন। তিনি ছিলেন কুরাইশ গোত্রের সন্তান, যা হযরত ইসমাঈল (আ.)-এর সরাসরি বংশধারা।\n\n"
                            + "<b>[আল-বাকারা – ১২৯]</b>\n\n"
                            + "রাসূলুল্লাহ ﷺ নিজেও তাঁর বংশ সম্পর্কে বলতেন,\n\n"
                            + "إِنَّ اللَّهَ اصْطَفَى كِنَانَةَ مِنْ وَلَدِ إِسْمَاعِيلَ، وَاصْطَفَى قُرَيْشًا مِنْ كِنَانَةَ، وَاصْطَفَى مِنْ قُرَيْشٍ بَنِي هَاشِمٍ، وَاصْطَفَانِي مِنْ بَنِي هَاشِمٍ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আল্লাহ ইসমাঈলের সন্তানদের মধ্য থেকে কিনানাকে মনোনীত করেছেন, কিনানা থেকে কুরাইশকে, কুরাইশ থেকে বনী হাশিমকে, এবং বনী হাশিম থেকে আমাকে মনোনীত করেছেন।\n\n"
                            + "<b>[সহীহ মুসলিম – ২২৭৬]</b>\n\n"
                            + "এই হাদীস স্পষ্ট করে দেয় যে, রাসূলুল্লাহ ﷺ-এর বংশধারা হযরত ইসমাঈল (আ.) থেকে আরম্ভ হয়েছে এবং তা ছিল আল্লাহর পক্ষ থেকে বেছে নেওয়া একটি বিশেষ ও সম্মানিত বংশ।\n\n"
                            + "হযরত ইসমাঈল (আ.)-এর সন্তানসন্ততি ও বংশধারা ইসলামের ইতিহাসে একটি বিশেষ স্থান অধিকার করে। আল্লাহ তাআলা তাঁর মাধ্যমে একটি বিশুদ্ধ আরব বংশধারা প্রতিষ্ঠা করেন, যা শেষ পর্যন্ত হযরত মুহাম্মদ ﷺ-এ গিয়ে পূর্ণতা লাভ করে। এই বংশ শুধুমাত্র পারিবারিক গৌরবের কারণ নয়, বরং এটি ছিল নবুওতের ধারক ও তাওহীদের দাওয়াতের বাহক।",
                    "Through Prophet Ismail (AS), Allah established the pure Arab lineage through which the Final Prophet Muhammad ﷺ was born.\n\n"
                            + "<b>Answer to Ibrahim's Supplications:</b>\n\n"
                            + "رَبِّ هَبْ لِي مِنَ الصَّالِحِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"My Lord, grant me [a child] from among the righteous.\"\n\n"
                            + "<b>[Surah As-Saffat: 100]</b>\n\n"
                            + "رَبَّنَا وَابْعَثْ فِيهِمْ رَسُولًا مِّنْهُمْ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"Our Lord, and send among them a messenger from themselves who will recite to them Your verses...\"\n\n"
                            + "<b>[Surah Al-Baqarah: 129]</b>\n\n"
                            + "<b>Hadith on the Chosen Lineage:</b>\n\n"
                            + "إِنَّ اللَّهَ اصْطَفَى كِنَانَةَ مِنْ وَلَدِ إِسْمَاعِيلَ، وَاصْطَفَى قُرَيْشًا مِنْ كِنَانَةَ، وَاصْطَفَى مِنْ قُرَيْشٍ بَنِي هَاشِمٍ، وَاصْطَفَانِي مِنْ بَنِي هَاشِمٍ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"Allah chose Kinanah from the descendants of Ismail, chose Quraysh from Kinanah, chose Banu Hashim from Quraysh, and chose me from Banu Hashim.\"\n\n"
                            + "<b>[Sahih Muslim: 2276]</b>"
            ));

            // 11. ওফাত ও কবরস্থান
            list.add(new ProphetOverviewTopicItem(
                    11,
                    "ওফাত ও কবরস্থান",
                    "Passing and Resting Place",
                    "হযরত ইসমাঈল (আলাইহিস সালাম)-এর জীবন ছিল নবুয়ত, তাওহীদ, ধৈর্য ও আত্মত্যাগে পরিপূর্ণ। কুরআন ও হাদীসে তাঁর দাওয়াতি জীবন, পিতা ইব্রাহিম (আ.)-এর সহচর্য, কা‘বা নির্মাণে অংশগ্রহণ, আরব ভূমিতে বসতি স্থাপন এবং তাঁর বংশধারা থেকে সর্বশেষ নবী মুহাম্মদ ﷺ-এর আগমনের আলোচনা বিস্তারিত এসেছে। তবে তাঁর ওফাত (ইন্তিকাল) ও কবরস্থান বিষয়ে সরাসরি কুরআন বা সহীহ হাদীসে কোনো বিস্তারিত বিবরণ নেই। তবে ঐতিহাসিক বর্ণনায় কিছু নির্ভরযোগ্য তথ্য পাওয়া যায়।\n\n"
                            + "<b>ঐতিহাসিক বর্ণনার আলোকে ওফাত:</b>\n\n"
                            + "ইসলামী ঐতিহাসিক গ্রন্থগুলোতে উল্লেখ রয়েছে যে, হযরত ইসমাঈল (আ.) ১৩৭ বছর বয়সে ইন্তিকাল করেন। তিনি দীর্ঘ সময় মক্কা নগরীতে বসবাস করেছেন এবং সেখানে ইবাদত ও দাওয়াতি কাজে লিপ্ত ছিলেন। তাঁর ইন্তিকাল হয় মক্কাতেই। তবে তাঁর ওফাতের নির্দিষ্ট তারিখ বা মাস সম্পর্কে নির্ভরযোগ্য সূত্রে বিশদ কোনো বিবরণ পাওয়া যায় না। এই তথ্য পাওয়া যায় তাফসীর ও সিরাত বিষয়ক গ্রন্থ থেকে, যেমন,\n\n"
                            + "• ইবন কাসীর (আল-বিদায়া ওয়ান নিহায়া)\n"
                            + "• ইমাম তাবারী (তারীখ উমাম ওয়াল মুলূক)\n\n"
                            + "<b>কবরস্থান:</b>\n\n"
                            + "ঐতিহাসিকভাবে ধারণা করা হয় যে, হযরত ইসমাঈল (আ.)-এর কবর মক্কা নগরীর মসজিদুল হারামের অভ্যন্তরে — হাতিম (حَطِيم) নামক অর্ধবৃত্তাকার স্থানটির ভেতরে অবস্থিত। হাতিম হলো কা‘বা শরীফের উত্তর পাশে অর্ধচন্দ্রাকার দেয়ালের মধ্যে একটি খোলা জায়গা, যা মূল কা‘বা শরীফের অন্তর্ভুক্ত ছিল। বহু আলিম ও ইতিহাসবিদ একমত হয়েছেন যে, কা‘বার দেয়াল নির্মাণের সময় জায়গার সংকটের কারণে হাতিম অংশটুকু কা‘বা থেকে আলাদা করা হয়। মুহাম্মদ ইবনে ইসহাক, ইবনে হিশাম, এবং ইমাম ইবনে কাসীর সহ অনেক প্রাচীন ইসলামি ইতিহাসবিদ বলেছেন, \n\n"
                            + "قُبِرَ إِسْمَاعِيلُ فِي الحِجْرِ (الحَطِيم)، وَدُفِنَ مَعَهُ أُمُّهُ هَاجَر.\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "ইসমাঈল (আ.)-কে কা‘বার হাতিম অংশে দাফন করা হয়, এবং তাঁর মা হাজেরা (আ.)-কেও সেখানে দাফন করা হয়।\n\n"
                            + "হযরত ইসমাঈল (আ.) তাঁর জীবনের মতো ওফাতের পরেও এমন স্থানে সমাহিত হয়েছেন, যা মুসলিমদের জন্য আধ্যাত্মিকভাবে সম্মানজনক ও গুরুত্বপূর্ণ। তাঁর কবর মক্কা শরীফে, কা‘বার হাতিম অংশে এটি ইতিহাস ও তাফসীরগ্রন্থসমূহে প্রমাণিত। যদিও কুরআন ও সহীহ হাদীসে তাঁর ওফাতের সময় বা স্থানের নির্দিষ্ট বিবরণ নেই, তবে ঐতিহ্যিক সূত্রে এটি মুসলিম ইতিহাসে প্রতিষ্ঠিত সত্য।",
                    "<b>Historical Accounts of His Demise:</b>\n\n"
                            + "Islamic historical sources, including Ibn Kathir's Al-Bidayah wan-Nihayah and Imam al-Tabari's Tarikh al-Umam wal-Muluk, record that Prophet Ismail (AS) passed away in Makkah at the age of approximately 137 years after a lifetime dedicated to monotheism and prophetic service.\n\n"
                            + "<b>Resting Place:</b>\n\n"
                            + "Extensive historical and archaeological traditions record that Prophet Ismail (AS) was laid to rest within the semi-circular enclosure of the Holy Sanctuary—the <b>Hateem (Hijr Ismail)</b>—adjacent to the Ka'bah, alongside his noble mother, Lady Hajar (AS).\n\n"
                            + "قُبِرَ إِسْمَاعِيلُ فِي الحِجْرِ (الحَطِيم)، وَدُفِنَ مَعَهُ أُمُّهُ هَاجَر.\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"Ismail was buried in the Hijr (al-Hateem), and buried with him was his mother Hajar.\"\n\n"
                            + "Being resting within the sacred precincts of the Ka'bah reflects the extraordinary esteem and honor conferred by Allah upon Ismail (AS) and Lady Hajar in this world and the Hereafter."
            ));
        } else if (prophetId == 9) {
            // =========================================================================
            // PROPHET ISHAQ (AS) - 8 CHAPTERS / TOPICS (100% VERBATIM & ACCURATE)
            // =========================================================================

            // 1. জন্মের অলৌকিক ঘটনা
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "জন্মের অলৌকিক ঘটনা",
                    "The Miraculous Birth",
                    "হযরত ইব্রাহিম (আ.) ছিলেন আল্লাহর এক মহান নবী। দীর্ঘ জীবনের বড় একটি অংশ তিনি আল্লাহর পথে কাটিয়েছেন, কিন্তু তিনি ও তাঁর স্ত্রী সারাহ (আ.) অনেক বছর নিঃসন্তান ছিলেন। বয়স যখন প্রবীণতার শেষ সীমায় পৌঁছেছে এবং মানবিকভাবে সন্তান লাভের আশা প্রায় শেষ, তখন আল্লাহ তায়ালা তাঁর অলৌকিক ক্ষমতায় ফেরেশতাদের মানবরূপে পাঠালেন। এই ফেরেশতারা ইব্রাহিম (আ.) এর ঘরে এসে তাঁকে সালাম দিলেন। ইব্রাহিম (আ.) তাদের অতিথি ভেবে ভাজা বাছুর আনলেন, কিন্তু দেখলেন তারা খাবার স্পর্শ করছে না। এতে তিনি কিছুটা ভয় পেলেন। তখন ফেরেশতারা বললেন, “ভয় পেয়ো না, আমরা আল্লাহর পক্ষ থেকে এসেছি তোমাকে এক পুত্রের সুসংবাদ দিতে। এ সময় পাশে দাঁড়িয়ে ছিলেন সারাহ (আ.)। তিনি বিস্মিত হয়ে বললেন, “আমি তো বৃদ্ধা এবং বন্ধ্যা, আমার স্বামীও বৃদ্ধ আমি কিভাবে সন্তান জন্ম দেব?” ফেরেশতারা বললেন, “তুমি কি আল্লাহর আদেশে বিস্মিত হচ্ছো? আল্লাহ যা চান তাই করেন।” তারপর আল্লাহ ঘোষণা করলেন “আমি তোমাকে এক জ্ঞানী ও নেক পুত্র দান করবো, নাম ইসহাক, এবং তাঁর পরই জন্ম নেবে ইয়াকুব।” এইভাবেই এক বন্ধ্যা ও বৃদ্ধা নারীর গর্ভে সন্তান জন্ম নিল আল্লাহর আদেশে যা ইতিহাসের এক মহান অলৌকিক ঘটনা。\n\n"
                            + "وَامْرَأَتُهُ قَائِمَةٌ فَضَحِكَتْ فَبَشَّرْنَاهَا بِإِسْحَاقَ وَمِنْ وَرَاءِ إِسْحَاقَ يَعْقُوبَ\n\n"
                            + "قَالَتْ يَا وَيْلَتَىٰ أَأَلِدُ وَأَنَا عَجُوزٌ وَهَٰذَا بَعْلِي شَيْخًا ۖ إِنَّ هَٰذَا لَشَيْءٌ عَجِيبٌ\n\n"
                            + "قَالُوا أَتَعْجَبِينَ مِنْ أَمْرِ اللَّهِ ۖ رَحْمَتُ اللَّهِ وَبَرَكَاتُهُ عَلَيْكُمْ أَهْلَ الْبَيْتِ ۚ إِنَّهُ حَمِيدٌ مَجِيدٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তাঁর স্ত্রী দাঁড়িয়ে ছিলেন; তখন তিনি (আনন্দে) হাসলেন। আমরা তাঁকে ইসহাকের সুসংবাদ দিলাম, এবং ইসহাকের পর ইয়াকুবেরও。\n\n"
                            + "তিনি বললেন, “আমি কি সন্তান জন্ম দেব, অথচ আমি বৃদ্ধা, এবং আমার স্বামীও বৃদ্ধ? এটি তো এক আশ্চর্য ব্যাপার!”\n\n"
                            + "তারা বলল, “তুমি কি আল্লাহর আদেশে বিস্মিত হচ্ছ? আল্লাহর রহমত ও বরকত তোমাদের উপর বর্ষিত হোক, হে নবীর পরিবার! নিশ্চয় তিনি প্রশংসনীয় ও মহিমান্বিত।”\n\n"
                            + "<b>[সূরা হুদ - ৭১–৭৩]</b>\n\n"
                            + "এই আয়াতে সারাহ (আ.)-এর বিস্ময় এবং ফেরেশতাদের উত্তর স্পষ্টভাবে এসেছে। এখানে দেখা যায়, মানবীয় যুক্তি যেখানে শেষ, আল্লাহর ইচ্ছা সেখানেই শুরু হয়。\n\n"
                            + "وَبَشَّرْنَاهُ بِإِسْحَاقَ نَبِيًّا مِّنَ الصَّالِحِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে ইসহাকের সুসংবাদ দিয়েছি, যিনি ছিলেন নেককারদের অন্তর্ভুক্ত এক নবী。\n\n"
                            + "<b>[সূরা আস-সাফফাত - ১১২]</b>\n\n"
                            + "এই আয়াতে স্পষ্ট বলা হয়েছে, ইসহাক শুধু অলৌকিকভাবে জন্ম নেননি, বরং তিনি নবুওয়তের মর্যাদা পেয়েছিলেন—যা তাঁর জন্মের মাহাত্ম্যকে আরও বাড়িয়ে দেয়。\n\n"
                            + "الْحَمْدُ لِلَّهِ الَّذِي وَهَبَ لِي عَلَى الْكِبَرِ إِسْمَاعِيلَ وَإِسْحَاقَ ۚ إِنَّ رَبِّي لَسَمِيعُ الدُّعَاءِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "সব প্রশংসা আল্লাহর, যিনি বার্ধক্যে আমাকে ইসমাঈল ও ইসহাক দান করেছেন। নিশ্চয়ই আমার প্রভু দোয়া শ্রবণকারী。\n\n"
                            + "<b>[সূরা ইবরাহিম - ৩৯]</b>\n\n"
                            + "এই আয়াতে ইব্রাহিম (আ.) আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশ করছেন যে, বয়সের শেষ সময়ে তিনি দুই পুত্র ইসমাঈল ও ইসহাক দ্বয়ের পিতা হয়েছেন, এটি আল্লাহর দয়ার এক জীবন্ত প্রমাণ।",
                    "Prophet Ibrahim (peace be upon him) was a resolute messenger of Allah who dedicated the majority of his long life in the path of Allah. However, he and his noble wife Lady Sarah (peace be upon her) remained childless for decades. When they reached extreme old age and humanly hope for having offspring had all but vanished, Allah the Almighty, through His boundless divine power, dispatched angels in human form to visit Ibrahim (AS).\n\n"
                            + "The angels entered his home offering greetings of peace. Ibrahim (AS), assuming they were human travelers, promptly brought a roasted calf as hospitality. Noticing they did not reach out to consume the food, he felt apprehension. The angels reassured him: \"Do not fear; we have been sent from Allah to grant you the glad tidings of a knowledgeable son.\"\n\n"
                            + "Overhearing this, Lady Sarah (AS) stood nearby in astonishment and exclaimed: \"Woe to me! Shall I bear a child when I am an old woman and my husband is an old man? Indeed, this is an astonishing thing!\" The angels replied: \"Do you marvel at the decree of Allah? May the mercy of Allah and His blessings be upon you, O people of the house! Indeed, He is Praiseworthy and Glorious.\"\n\n"
                            + "Allah then decreed: \"We grant you the glad tidings of a righteous, knowledgeable son named Ishaq (Isaac), and after Ishaq, Yaqub (Jacob).\" Thus, by Allah's divine decree, a child was miraculously conceived by an elderly and barren woman—a timeless divine sign.\n\n"
                            + "وَامْرَأَتُهُ قَائِمَةٌ فَضَحِكَتْ فَبَشَّرْنَاهَا بِإِسْحَاقَ وَمِنْ وَرَاءِ إِسْحَاقَ يَعْقُوبَ\n\n"
                            + "قَالَتْ يَا وَيْلَتَىٰ أَأَلِدُ وَأَنَا عَجُوزٌ وَهَٰذَا بَعْلِي شَيْخًا ۖ إِنَّ هَٰذَا لَشَيْءٌ عَجِيبٌ\n\n"
                            + "قَالُوا أَتَعْجَبِينَ مِنْ أَمْرِ اللَّهِ ۖ رَحْمَتُ اللَّهِ وَبَرَكَاتُهُ عَلَيْكُمْ أَهْلَ الْبَيْتِ ۚ إِنَّهُ حَمِيدٌ مَجِيدٌ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And his wife was standing, and she laughed [with joy], so We gave her good tidings of Ishaq and after Ishaq, Yaqub. She said, 'Woe to me! Shall I give birth while I am an old woman and this, my husband, is an old man? Indeed, this is an amazing thing!' They said, 'Are you amazed at the decree of Allah? May the mercy of Allah and His blessings be upon you, people of the house. Indeed, He is Praiseworthy and Noble.'\"\n\n"
                            + "<b>[Surah Hud: 71–73]</b>\n\n"
                            + "وَبَشَّرْنَاهُ بِإِسْحَاقَ نَبِيًّا مِّنَ الصَّالِحِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We gave him good tidings of Ishaq, a prophet among the righteous.\"\n\n"
                            + "<b>[Surah As-Saffat: 112]</b>\n\n"
                            + "الْحَمْدُ لِلَّهِ الَّذِي وَهَبَ لِي عَلَى الْكِبَرِ إِسْمَاعِيلَ وَإِسْحَاقَ ۚ إِنَّ رَبِّي لَسَمِيعُ الدُّعَاءِ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"Praise to Allah, who has granted to me in old age Ismail and Ishaq. Indeed, my Lord is the Hearer of supplication.\"\n\n"
                            + "<b>[Surah Ibrahim: 39]</b>"
            ));

            // 2. ভাই ইসমাঈলের (আ.) সাথে সম্পর্ক
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "ভাই ইসমাঈলের (আ.) সাথে সম্পর্ক",
                    "Relationship with Brother Ismail (AS)",
                    "হযরত ইসমাঈল (আ.) ও ইসহাক (আ.) দুজনেই ছিলেন নবী ইব্রাহিম (আ.) এর পুত্র এবং আল্লাহর প্রেরিত নবী। তাঁরা শুধু রক্তের সম্পর্কেই নয়, পরস্পরের প্রতি গভীর শ্রদ্ধা ও ভ্রাতৃত্বপূর্ণ মমতায় যুক্ত ছিলেন। উভয়েই আল্লাহর আদেশে নিবেদিতপ্রাণ, দোয়া কবুল হওয়া একটি পরিবারের সদস্য। যদিও তারা ভিন্ন মাতার সন্তান ইসমাঈল (আ.) ছিলেন হাজেরা (আ.)-এর সন্তান, আর ইসহাক (আ.) সারাহ (আ.)-এর সন্তান তবুও দুজনের সম্পর্ক ছিল আন্তরিক ভালোবাসা, ইমানী ঐক্য ও নবুওয়তের সহযোগিতার ভিত্তিতে গড়ে ওঠা। ইব্রাহিম (আ.) তাঁদের উভয়ের জন্য আল্লাহর কাছে দোয়া করেছিলেন যেন তাঁরা আল্লাহর পথে চলেন, মানবজাতির কল্যাণে নবুওয়তের দায়িত্ব পালন করেন। তাঁদের জীবনের লক্ষ্য ছিল একই আল্লাহর সন্তুষ্টি। ইসমাঈল (আ.) আরব ভূমিতে দ্বীনের ভিত্তি স্থাপন করেন, আর ইসহাক (আ.) ফিলিস্তিন অঞ্চলে নবুওয়তের ধারা বজায় রাখেন। এই দুই ভাইয়ের মধ্যে কোনো দ্বন্দ্ব বা বিরোধের উল্লেখ ইসলামী ইতিহাসে নেই; বরং তাঁরা ছিলেন পারস্পরিক দোয়ার অংশীদার ও একে অপরের বরকতের উত্তরাধিকারী。\n\n"
                            + "الْحَمْدُ لِلَّهِ الَّذِي وَهَبَ لِي عَلَى الْكِبَرِ إِسْمَاعِيلَ وَإِسْحَاقَ ۚ إِنَّ رَبِّي لَسَمِيعُ الدُّعَاءِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "সব প্রশংসা আল্লাহর, যিনি বার্ধক্যে আমাকে ইসমাঈল ও ইসহাক দান করেছেন। নিশ্চয়ই আমার প্রভু দোয়া শ্রবণকারী。\n\n"
                            + "<b>[সূরা ইবরাহিম - ৩৯]</b>\n\n"
                            + "এই আয়াতে দেখা যায়, ইব্রাহিম (আ.) আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশ করছেন দুই পুত্রের জন্য যা প্রমাণ করে, তাঁদের মধ্যে সম্পর্ক ছিল দোয়া, আশীর্বাদ ও পারিবারিক সৌহার্দ্যে পূর্ণ। উভয়েই ছিলেন পিতার দোয়ার ফল ও আল্লাহর রহমতের প্রতিফলন。\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ ۚ كُلًّا هَدَيْنَا ۚ وَنُوحًا هَدَيْنَا مِن قَبْلُ ۖ ... وَإِسْمَاعِيلَ وَالْيَسَعَ وَيُونُسَ وَلُوطًا ۚ وَكُلًّا فَضَّلْنَا عَلَى الْعَالَمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে (ইব্রাহিমকে) দান করেছি ইসহাক ও ইয়াকুবকে; প্রত্যেককেই আমরা পথ দেখিয়েছি। \n\n"
                            + "এবং ইসমাঈল, আল-ইয়াসা, ইউনুস ও লূত সবাইকে আমরা বিশ্বের উপর শ্রেষ্ঠত্ব দিয়েছি。\n\n"
                            + "<b>[সূরা আল-আনআম - ৮৪–৮৬]</b>\n\n"
                            + "এখানে আল্লাহ উভয় ভাই ইসমাঈল ও ইসহাক এর নাম একসাথে উল্লেখ করেছেন, যা তাদের পারস্পরিক সম্মান ও একত্রে নবুওয়তের মর্যাদার প্রতীক। এটি প্রমাণ করে, তাঁদের মধ্যে সম্পর্ক ছিল একতা, দোয়া ও আল্লাহর পথে সহযোগিতার ভিত্তিতে দৃঢ় ও বরকতময়।",
                    "Prophet Ismail (AS) and Prophet Ishaq (AS) were both beloved sons of Prophet Ibrahim (AS) and noble messengers chosen by Allah. They were bound not merely by blood, but by profound mutual reverence, brotherly love, and shared prophetic mission. Both were dedicated servants of Allah born of answered prayers.\n\n"
                            + "Though born of different mothers—Ismail (AS) from Lady Hajar (AS) and Ishaq (AS) from Lady Sarah (AS)—their relationship was founded upon unshakeable faith, sincere affection, and mutual cooperation in upholding monotheism. Prophet Ibrahim (AS) continuously prayed for both sons that they walk steadfastly upon the divine path and serve humanity through their prophethood.\n\n"
                            + "Their shared life objective was the pleasure of Allah. Ismail (AS) laid the foundations of faith in the Arabian Peninsula, while Ishaq (AS) preserved the prophetic lineage in the land of Palestine. Islamic historical tradition records absolute harmony and no discord between these two brothers; rather, they shared in reciprocal prayers and inherited mutual blessings.\n\n"
                            + "الْحَمْدُ لِلَّهِ الَّذِي وَهَبَ لِي عَلَى الْكِبَرِ إِسْمَاعِيلَ وَإِسْحَاقَ ۚ إِنَّ رَبِّي لَسَمِيعُ الدُّعَاءِ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"Praise to Allah, who has granted to me in old age Ismail and Ishaq. Indeed, my Lord is the Hearer of supplication.\"\n\n"
                            + "<b>[Surah Ibrahim: 39]</b>\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ ۚ كُلًّا هَدَيْنَا ۚ وَنُوحًا هَدَيْنَا مِن قَبْلُ ۖ ... وَإِسْمَاعِيلَ وَالْيَسَعَ وَيُونُسَ وَلُوطًا ۚ وَكُلًّا فَضَّلْنَا عَلَى الْعَالَمِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We gave to him Ishaq and Yaqub—all [of them] We guided. And Noah, We guided before... and Ismail and Al-Yasa' and Yunus and Lut—and all [of them] We preferred over the worlds.\"\n\n"
                            + "<b>[Surah Al-An'am: 84–86]</b>"
            ));

            // 3. শৈশবে ইসলামী শিক্ষা
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "শৈশবে ইসলামী শিক্ষা",
                    "Islamic Education in Childhood",
                    "হযরত ইসহাক (আলাইহিস সালাম)-এর শৈশবকাল ছিল শান্ত, ধার্মিক ও আল্লাহভীরুতায় পরিপূর্ণ। তিনি জন্ম থেকেই নবুওয়তের পরিবেশে বড় হয়েছেন, যেখানে আল্লাহর ইবাদত, ন্যায়পরায়ণতা ও মানুষের প্রতি সদাচরণের শিক্ষা ছিল জীবনের মূলভিত্তি। ইসলামী ইতিহাসে তাঁর শৈশবকে নবীদের আদর্শ শিক্ষার উদাহরণ হিসেবে দেখা হয়। নিচে তাঁর শৈশবের ইসলামিক শিক্ষার মূল দিকগুলো তুলে ধরা হলো。\n\n"
                            + "<b>নবুওয়তের পরিবেশে বেড়ে ওঠা:</b>\n\n"
                            + "ইসহাক (আ.) ছিলেন হযরত ইব্রাহিম (আ.) ও সারাহ (আ.)-এর সন্তান। তাঁর জন্ম হয়েছিল এমন এক ঘরে, যেখানে আল্লাহর ইবাদত, তাওহীদের দাওয়াত ও সত্যের প্রচার ছিল প্রতিদিনের কাজ। পিতা ইব্রাহিম (আ.) তাঁকে ছোটবেলা থেকেই শিখিয়েছিলেন একমাত্র আল্লাহর উপাসনা করতে, মিথ্যা ও অন্যায়ের বিরোধিতা করতে এবং অতিথি ও মানুষদের প্রতি সদাচরণ করতে。\n\n"
                            + "তাঁর শৈশবকাল কেটেছিল এমন এক শিক্ষার মধ্যে, যেখানে আল্লাহভীতি, ধৈর্য, ন্যায়বিচার ও কৃতজ্ঞতা ছিল শিক্ষার মূল ভিত্তি。\n\n"
                            + "<b>আল্লাহর নির্দেশে লালন-পালন:</b>\n\n"
                            + "ইসহাক (আ.) ছোটবেলা থেকেই আল্লাহর প্রতি গভীর বিশ্বাসী ছিলেন। কুরআনে তাঁর প্রশংসা এসেছে এইভাবে,\n\n"
                            + "وَبَشَّرْنَاهُ بِإِسْحَاقَ نَبِيًّا مِّنَ الصَّالِحِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে ইসহাকের সুসংবাদ দিয়েছি, যিনি ছিলেন নেককারদের অন্তর্ভুক্ত এক নবী。\n\n"
                            + "<b>[সূরা আস-সাফফাত – ১১২]</b>\n\n"
                            + "এই আয়াত প্রমাণ করে যে, তিনি জন্মের পর থেকেই নেককার ও আল্লাহভীরু ছিলেন। তাঁর চরিত্রে শৈশব থেকেই নবুওয়তের গুণাবলি প্রকাশ পেত。\n\n"
                            + "<b>পিতার আদর্শ অনুসরণ:</b>\n\n"
                            + "হযরত ইব্রাহিম (আ.) তাঁকে আল্লাহর পথে দৃঢ় থাকতে শিক্ষা দিয়েছিলেন। ইব্রাহিম (আ.) যেমন ত্যাগ, ধৈর্য ও আনুগত্যের প্রতীক ছিলেন, তেমনি ইসহাক (আ.) তাঁর সেই আদর্শ অনুসরণ করেই বড় হন। পিতার উপদেশে তিনি মানুষের কল্যাণে দোয়া করা, অন্যায় থেকে বিরত থাকা, এবং আল্লাহর সন্তুষ্টি অর্জনের চেষ্টা করতেন。\n\n"
                            + "<b>নৈতিকতা ও বিনয়:</b>\n\n"
                            + "ইসহাক (আ.) ছিলেন বিনয়ী, ধৈর্যশীল ও শান্তস্বভাবের। ছোটবেলা থেকেই তিনি অন্যের প্রতি সহানুভূতিশীল ও নম্র আচরণ করতেন। আল্লাহ তাঁকে এমন চরিত্রে গঠন করেছিলেন যাতে তিনি ভবিষ্যতে নবুওয়তের দায়িত্ব সফলভাবে পালন করতে পারেন।",
                    "The childhood of Prophet Ishaq (peace be upon him) was characterized by serenity, devotion, and deep God-consciousness. Born directly into an environment of prophethood, divine worship, uncompromising justice, and supreme benevolence toward mankind formed the very bedrock of his upbringing.\n\n"
                            + "<b>Growing up in a Prophetic Environment:</b>\n\n"
                            + "As the son of Prophet Ibrahim (AS) and Lady Sarah (AS), Ishaq (AS) grew up in a household where the worship of Allah, inviting toward Tawheed, and proclaiming the truth were daily endeavors. His father instilled in him pure monotheism, standing against falsehood and injustice, and showing kindness and hospitality to guests and neighbors.\n\n"
                            + "<b>Nurtured Under Divine Guidance:</b>\n\n"
                            + "From early childhood, Ishaq (AS) possessed unshakeable devotion to Allah. The Holy Quran praises him:\n\n"
                            + "وَبَشَّرْنَاهُ بِإِسْحَاقَ نَبِيًّا مِّنَ الصَّالِحِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We gave him good tidings of Ishaq, a prophet among the righteous.\"\n\n"
                            + "<b>[Surah As-Saffat: 112]</b>\n\n"
                            + "This verse affirms that righteousness and God-fearing virtue were intrinsic to his character from birth.\n\n"
                            + "<b>Emulating His Father's Sublime Character:</b>\n\n"
                            + "Prophet Ibrahim (AS) trained Ishaq to remain steadfast upon the straight path. Emulating his father's unmatched sacrifice, patience, and obedience, Ishaq grew up praying for humanity, refraining from transgressions, and constantly seeking the pleasure of Allah.\n\n"
                            + "<b>Humility and Moral Excellence:</b>\n\n"
                            + "Ishaq (AS) was remarkably humble, gentle, and patient. Allah fashioned his disposition with profound compassion, preparing him thoroughly for the arduous responsibilities of prophethood."
            ));

            // 4. নবুয়ত লাভ
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "নবুয়ত লাভ",
                    "Attainment of Prophethood",
                    "হযরত ইসহাক (আলাইহিস সালাম) ছিলেন আল্লাহর মনোনীত নবী, যাঁকে আল্লাহ তাঁর কুদরতে ও রহমতে বার্ধক্যে পিতা ইব্রাহিম (আ.) ও মাতা সারাহ (আ.)-এর ঘরে দান করেন। তাঁর নবুওয়ত ছিল আল্লাহর পক্ষ থেকে প্রেরিত এক মহান দায়িত্ব, যা তিনি জ্ঞান, ন্যায় ও দয়া দিয়ে পালন করেছিলেন। নিচে তাঁর নবুওয়ত লাভ ও দায়িত্ব সম্পর্কে ইসলামী সূত্র অনুযায়ী বিস্তারিতভাবে তুলে ধরা হলো。\n\n"
                            + "<b>নবুওয়তের ঘোষণা ও আল্লাহর পক্ষ থেকে মনোনয়ন:</b>\n\n"
                            + "হযরত ইসহাক (আ.) এর নবুওয়তের ঘোষণা আসে তাঁর জন্মের আগেই। ফেরেশতারা ইব্রাহিম (আ.) ও সারাহ (আ.)-এর কাছে এসে শুধু তাঁর জন্মেরই সুসংবাদ দেননি, বরং জানান তিনি নবী হবেন এবং তাঁর বংশে নবীদের ধারাবাহিকতা চলবে。\n\n"
                            + "وَبَشَّرْنَاهُ بِإِسْحَاقَ نَبِيًّا مِّنَ الصَّالِحِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে ইসহাকের সুসংবাদ দিয়েছি, যিনি ছিলেন নেককারদের অন্তর্ভুক্ত এক নবী。\n\n"
                            + "<b>[সূরা আস-সাফফাত – ১১২]</b>\n\n"
                            + "এই আয়াত থেকে স্পষ্ট বোঝা যায় যে, আল্লাহ তায়ালা তাঁর নবুওয়তের ঘোষণা দিয়েছিলেন জন্মের আগেই। অর্থাৎ, তিনি ছিলেন এমন এক নবী যিনি আল্লাহর বিশেষ কুদরতের মাধ্যমে প্রেরিত。\n\n"
                            + "<b>নবুওয়তের দায়িত্ব ও বার্তা:</b>\n\n"
                            + "ইসহাক (আ.)-এর নবুওয়তের মূল বার্তা ছিল এক আল্লাহর উপাসনা, শিরক থেকে বিরত থাকা, অন্যায়ের প্রতিবাদ করা এবং মানুষের মধ্যে ন্যায়বিচার প্রতিষ্ঠা করা। তিনি পিতা ইব্রাহিম (আ.)-এর তাওহীদের দাওয়াতকে উত্তরাধিকারসূত্রে বহন করেন এবং ফিলিস্তিন ও কানআন অঞ্চলের মানুষকে এক আল্লাহর দিকে আহ্বান জানান। আল্লাহ তায়ালা তাঁর সম্পর্কে কুরআনে বলেন, \n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ ۚ كُلًّا هَدَيْنَا ۚ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে (ইব্রাহিমকে) দান করেছি ইসহাক ও ইয়াকুব; উভয়কেই আমরা পথ দেখিয়েছি。\n\n"
                            + "<b>[সূরা আল-আনআম – ৮৪]</b>\n\n"
                            + "এখানে দেখা যায়, আল্লাহ তাঁকে হেদায়াতপ্রাপ্ত নবীদের অন্তর্ভুক্ত করেছেন। তাঁর পুত্র ইয়াকুব (আ.)-ও নবী হন, যা প্রমাণ করে ইসহাক (আ.)-এর বংশধরদের মধ্য দিয়ে নবুওয়তের ধারাবাহিকতা প্রতিষ্ঠিত হয়েছিল。\n\n"
                            + "<b>ইসহাক (আ.)-এর নবুওয়তের সময়কাল ও অবদান:</b>\n\n"
                            + "ইসলামী ঐতিহাসিক সূত্র (ইবন কাসির, তাফসির তাবারি প্রমুখ) অনুযায়ী, ইসহাক (আ.) ফিলিস্তিন অঞ্চলে নবুওয়ত লাভ করেন এবং জীবনের অধিকাংশ সময় সেখানে অতিবাহিত করেন। তিনি মানুষকে আল্লাহর পথে আহ্বান করেন, তাঁদেরকে পাপ থেকে বিরত থাকতে বলেন, এবং সমাজে ন্যায়বিচার প্রতিষ্ঠা করেন। তাঁর নেতৃত্বে মানুষ শান্তি, ন্যায় ও ইমানের পথে ফিরে আসে। তিনি অত্যন্ত ধৈর্যশীল, বিনয়ী ও দয়ালু নবী ছিলেন。\n\n"
                            + "<b>বংশধারায় নবুওয়তের ধারাবাহিকতা:</b>\n\n"
                            + "ইসহাক (আ.)-এর নবুওয়তের অন্যতম বিশেষ দিক ছিল তাঁর বংশে নবুওয়তের ধারাবাহিকতা স্থাপন। তাঁর পুত্র ইয়াকুব (আ.), নাতি ইউসুফ (আ.), এবং তাঁর বংশধরদের মধ্য থেকেই পরবর্তীতে বহু নবী আগমন করেন যেমন দাউদ (আ.), সুলাইমান (আ.), মূসা (আ.), ঈসা (আ.) প্রমুখ। আল্লাহ তায়ালা বলেন, \n\n"
                            + "وَبَارَكْنَا عَلَيْهِ وَعَلَىٰ إِسْحَاقَ ۚ وَمِن ذُرِّيَّتِهِمَا مُحْسِنٌ وَظَالِمٌ لِّنَفْسِهِ مُبِينٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা বরকত দিয়েছি তাঁর (ইব্রাহিমের) উপর এবং ইসহাকের উপর; তাঁদের বংশধরদের মধ্যে কেউ নেককার, আর কেউ নিজের প্রতি অন্যায়কারী。\n\n"
                            + "<b>[সূরা আস-সাফফাত – ১১৩]</b>\n\n"
                            + "এই আয়াতে আল্লাহ ঘোষণা করেছেন যে, ইসহাক (আ.) ও তাঁর বংশে বরকত ও নবুওয়তের ধারাবাহিকতা অব্যাহত থাকবে।",
                    "Prophet Ishaq (peace be upon him) was an appointed messenger of Allah, divinely gifted in old age to his parents Ibrahim (AS) and Sarah (AS). His prophethood was a sacred mission that he carried out with wisdom, justice, and compassion.\n\n"
                            + "<b>Divine Proclamation of Prophethood:</b>\n\n"
                            + "The announcement of Ishaq's prophethood preceded his very birth. Angels informed Ibrahim and Sarah not only of his birth, but also that he would be an honored prophet whose lineage would sustain a continuous chain of messengers.\n\n"
                            + "وَبَشَّرْنَاهُ بِإِسْحَاقَ نَبِيًّا مِّنَ الصَّالِحِينَ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We gave him good tidings of Ishaq, a prophet among the righteous.\"\n\n"
                            + "<b>[Surah As-Saffat: 112]</b>\n\n"
                            + "<b>Prophetic Mission and Core Message:</b>\n\n"
                            + "The essence of Ishaq's prophetic message was pure monotheism (Tawheed), renouncing idolatry and polytheism, standing against societal oppression, and establishing justice. Carrying forward the legacy of his father Ibrahim (AS), he invited the people of Palestine and Canaan to the worship of One Allah.\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ ۚ كُلًّا هَدَيْنَا ۚ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We gave to him Ishaq and Yaqub—all [of them] We guided.\"\n\n"
                            + "<b>[Surah Al-An'am: 84]</b>\n\n"
                            + "<b>Duration and Contributions:</b>\n\n"
                            + "Classical Islamic historical sources (Ibn Kathir, Tarikh al-Tabari) state that Ishaq (AS) was commissioned as a prophet in Palestine and spent the vast majority of his life guiding people to righteousness, establishing equity, and reforming society.\n\n"
                            + "<b>Continuous Prophetic Lineage:</b>\n\n"
                            + "A distinct hallmark of Ishaq's prophethood was the establishment of a continuous prophetic dynasty through his son Yaqub (AS), grandson Yusuf (AS), and subsequent Israelite messengers including Dawud, Sulaiman, Musa, and Isa (peace be upon them all).\n\n"
                            + "وَبَارَكْنَا عَلَيْهِ وَعَلَىٰ إِسْحَاقَ ۚ وَمِن ذُرِّيَّتِهِمَا مُحْسِنٌ وَظَالِمٌ لِّنَفْسِهِ مُبِينٌ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We blessed him and Ishaq. But among their descendants is the doer of good and the clearly unjust to himself.\"\n\n"
                            + "<b>[Surah As-Saffat: 113]</b>"
            ));

            // 5. দাওয়াতি কার্যক্রম
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "দাওয়াতি কার্যক্রম",
                    "Dawah Activities",
                    "হযরত ইসহাক (আ.) এর ইসলামি দাওয়াতি কার্যক্রম ছিল শান্ত, প্রজ্ঞাপূর্ণ ও তাওহীদের আহ্বানে নিবেদিত। তাঁর দায়িত্ব ছিল পিতা হযরত ইব্রাহিম (আ.) এর প্রতিষ্ঠিত একত্ববাদের বার্তাকে অব্যাহত রাখা এবং ফিলিস্তিন ও আশপাশের অঞ্চলের মানুষকে আল্লাহর পথে ডাকা। কুরআন ও ইসলামী ইতিহাস অনুযায়ী তাঁর দাওয়াতি জীবনের বৈশিষ্ট্যগুলো নিচে তুলে ধরা হলো。\n\n"
                            + "<b>তাওহীদের দাওয়াত:</b>\n\n"
                            + "ইসহাক (আ.) মানুষের মাঝে এক আল্লাহর উপাসনার আহ্বান জানান। তিনি তাঁদের সতর্ক করেন যেন তারা মূর্তি পূজা ও শিরক থেকে বিরত থাকে। তাঁর দাওয়াতের মূল বাণী ছিল “আল্লাহই একমাত্র উপাস্য, তাঁর সাথী নেই; তাঁরই কাছে প্রত্যাবর্তন।” তাঁর এই আহ্বান ছিল কোমল, যুক্তিনির্ভর এবং ধৈর্যশীল; তিনি কখনো কঠোর ভাষায় নয়, বরং উদাহরণ ও উপদেশের মাধ্যমে মানুষকে সত্যের দিকে আহ্বান করতেন。\n\n"
                            + "<b>পারিবারিক ও সামাজিক দাওয়াত:</b>\n\n"
                            + "হযরত ইসহাক (আ.) প্রথমে নিজের পরিবার ও নিকটাত্মীয়দের মধ্যে দাওয়াত শুরু করেন। তিনি তাঁর পুত্র ইয়াকুব (আ.) কে আল্লাহর আদেশ মানার, নামাজ কায়েমের ও সৎকর্মে উদ্বুদ্ধ করেন। তাঁর পরিবার ছিল নবুওয়তী দাওয়াতের কেন্দ্র, যেখান থেকে দ্বীনের আলো ছড়িয়ে পড়ে আশেপাশের এলাকায়。\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ ۚ كُلًّا هَدَيْنَا ۚ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে (ইব্রাহিমকে) দান করেছি ইসহাক ও ইয়াকুব; উভয়কেই আমরা হেদায়াত দিয়েছি。\n\n"
                            + "<b>[সূরা আল-আনআম – ৮৪]</b>\n\n"
                            + "এই আয়াত প্রমাণ করে যে, ইসহাক (আ.) শুধু নিজে নবী ছিলেন না, বরং নিজের সন্তানকেও নবুওয়তের জন্য প্রস্তুত করেছিলেন, যা দাওয়াতি কাজের অংশ ছিল。\n\n"
                            + "<b>সমাজে ন্যায় ও সদাচারের প্রচার:</b>\n\n"
                            + "ইসহাক (আ.) সমাজে ন্যায়বিচার প্রতিষ্ঠা ও দুর্নীতি দূরীকরণে দাওয়াত চালান। তিনি মানুষকে পরস্পরের প্রতি দয়া, অতিথিপরায়ণতা, সততা ও সত্যবাদিতার শিক্ষা দিতেন—যা তাঁর পিতা ইব্রাহিম (আ.)-এর শিক্ষারই ধারাবাহিকতা। তিনি প্রচার করতেন যে, আল্লাহর আনুগত্য ছাড়া কোনো জাতি উন্নত হতে পারে না এবং অন্যায়ের পরিণাম সর্বদা ধ্বংস。\n\n"
                            + "<b>নবুওয়তের উত্তরাধিকার ও প্রভাব:</b>\n\n"
                            + "হযরত ইসহাক (আ.)-এর দাওয়াতের প্রভাব ছিল দীর্ঘস্থায়ী। তাঁর বংশ থেকেই নবুওয়তের ধারা বজায় থাকে ইয়াকুব (আ.), ইউসুফ (আ.), মূসা (আ.), দাউদ (আ.), সুলাইমান (আ.) এবং ঈসা (আ.) পর্যন্ত। আল্লাহ বলেন,\n\n"
                            + "وَبَارَكْنَا عَلَيْهِ وَعَلَىٰ إِسْحَاقَ ۚ وَمِن ذُرِّيَّتِهِمَا مُحْسِنٌ وَظَالِمٌ لِّنَفْسِهِ مُبِينٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা বরকত দিয়েছি তাঁর (ইব্রাহিমের) উপর এবং ইসহাকের উপর; তাঁদের বংশধরদের মধ্যে কেউ নেককার, আর কেউ নিজের প্রতি অন্যায়কারী。\n\n"
                            + "<b>[সূরা আস-সাফফাত – ১১৩]</b>\n\n"
                            + "এই আয়াত ইঙ্গিত দেয় যে, ইসহাক (আ.)-এর দাওয়াতের ফলস্বরূপ তাঁর বংশে নবুওয়তের আলো অব্যাহত ছিল, যা মানবজাতির জন্য এক স্থায়ী দিশা হয়ে দাঁড়ায়।",
                    "The dawah mission of Prophet Ishaq (AS) was characterized by serene wisdom, gentle eloquence, and uncompromising dedication to Tawheed. He was tasked with sustaining the monotheistic call established by Ibrahim (AS) and inviting the communities of Palestine and surrounding regions to the truth.\n\n"
                            + "<b>The Call to Tawheed:</b>\n\n"
                            + "Ishaq (AS) invited humanity to single out Allah alone in worship, warning against idol worship and polytheism. His message resonated: \"Allah is the only worthy deity; He has no partners, and unto Him is the ultimate return.\" His dawah was compassionate, reasoned, and patient.\n\n"
                            + "<b>Family and Community Dawah:</b>\n\n"
                            + "Prophet Ishaq initiated his dawah within his own household and close relatives, nurturing his son Yaqub (AS) in obedience to Allah, establishing regular prayer, and performing righteous deeds.\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ ۚ كُلًّا هَدَيْنَا ۚ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We gave to him Ishaq and Yaqub—all [of them] We guided.\"\n\n"
                            + "<b>[Surah Al-An'am: 84]</b>\n\n"
                            + "<b>Promoting Justice and Virtuous Conduct:</b>\n\n"
                            + "Ishaq (AS) championed societal justice, honesty, hospitality, and compassion. He emphasized that genuine prosperity is unattainable without obedience to Allah and that wrongdoing invariably leads to ruin.\n\n"
                            + "<b>Enduring Impact:</b>\n\n"
                            + "The fruits of Ishaq's dawah endured across generations, continuing through Yaqub, Yusuf, Musa, Dawud, Sulaiman, and Isa (AS).\n\n"
                            + "وَبَارَكْنَا عَلَيْهِ وَعَلَىٰ إِسْحَاقَ ۚ وَمِن ذُرِّيَّتِهِمَا مُحْسِنٌ وَظَالِمٌ لِّنَفْسِهِ مُبِينٌ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We blessed him and Ishaq. But among their descendants is the doer of good and the clearly unjust to himself.\"\n\n"
                            + "<b>[Surah As-Saffat: 113]</b>"
            ));

            // 6. পারিবারিক জীবন
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "পারিবারিক জীবন",
                    "Family Life",
                    "হযরত ইসহাক (আ.) এর পারিবারিক জীবন ছিল শান্ত, পরিপূর্ণ, ধার্মিক ও আল্লাহভীরুতায় গঠিত। তিনি নবীদের পরিবারে জন্মগ্রহণ করেন, নবীর ঘরেই লালিত হন, এবং নবীদের উত্তরাধিকার হিসেবেই তাঁর পরিবারে নবুওয়তের ধারাবাহিকতা স্থাপিত হয়। নিচে কুরআন ও ইসলামী ঐতিহাসিক সূত্র অনুযায়ী তাঁর পারিবারিক জীবনের মূল দিকগুলো তুলে ধরা হলো。\n\n"
                            + "<b>ধার্মিক পরিবার ও পিতার আদর্শে গড়ে ওঠা:</b>\n\n"
                            + "ইসহাক (আ.) ছিলেন মহান নবী ইব্রাহিম (আ.) ও সারাহ (আ.) এর সন্তান। তাঁর ঘরেই তিনি তাওহীদ, ইবাদত, ন্যায়বিচার ও অতিথিপরায়ণতার শিক্ষা লাভ করেন। ইব্রাহিম (আ.)-এর পরিবার ছিল আল্লাহর প্রতি সম্পূর্ণ আত্মসমর্পিত পরিবার তাঁদের জীবনের প্রতিটি কাজই ছিল আল্লাহর সন্তুষ্টির জন্য। এই পরিবেশেই ইসহাক (আ.) শৈশব থেকে বড় হয়েছেন। কুরআনে ইব্রাহিম (আ.) এর পরিবারের প্রশংসা এসেছে এভাবে, \n\n"
                            + "رَحْمَتُ اللَّهِ وَبَرَكَاتُهُ عَلَيْكُمْ أَهْلَ الْبَيْتِ ۚ إِنَّهُ حَمِيدٌ مَجِيدٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আল্লাহর রহমত ও বরকত তোমাদের উপর বর্ষিত হোক, হে নবীর পরিবার; নিশ্চয়ই তিনি প্রশংসনীয় ও মহিমান্বিত。\n\n"
                            + "<b>[সূরা হুদ – ৭৩]</b>\n\n"
                            + "এই আয়াত দ্বারা বোঝা যায়, ইব্রাহিম (আ.) এর পরিবার যেখানে ইসহাক (আ.) জন্মগ্রহণ করেন ছিল আল্লাহর রহমত ও বরকতে পরিপূর্ণ একটি ঘর。\n\n"
                            + "<b>বিবাহ ও সন্তানাদি:</b>\n\n"
                            + "ইসলামী ঐতিহাসিক সূত্রে (তাফসির ইবন কাসির, তাবারি প্রভৃতি) উল্লেখ আছে যে, হযরত ইসহাক (আ.) এর স্ত্রী ছিলেন রিবকাহ, যিনি এক ধার্মিক ও নেককার নারী ছিলেন। তাঁদের বিবাহের পরে আল্লাহ তাঁদের দুটি পুত্র দান করেন ইস ও ইয়াকুব (আ.) ।\n\n"
                            + "ইয়াকুব (আ.) ই পরবর্তীতে নবী হন এবং তাঁর বংশ থেকেই জন্ম নেন নবী ইউসুফ (আ.), মূসা (আ.), ঈসা (আ.) প্রমুখ। অর্থাৎ, ইসহাক (আ.) এর পারিবারিক জীবন থেকেই নবুওয়তের ধারাবাহিকতা প্রজন্ম থেকে প্রজন্মে চলতে থাকে。\n\n"
                            + "<b>আল্লাহভীরুতা ও পারিবারিক ন্যায়নীতি:</b>\n\n"
                            + "ইসহাক (আ.) তাঁর পরিবারকে আল্লাহভীত ও সৎপথে রাখার প্রতি অত্যন্ত যত্নবান ছিলেন। তিনি সন্তানদের নিয়মিত উপদেশ দিতেন আল্লাহর প্রতি আনুগত্য, নামাজ কায়েম, ন্যায়বিচার ও দয়া প্রদর্শনের বিষয়ে। তিনি পারিবারিক জীবনে ধৈর্য, ভালোবাসা ও পারস্পরিক শ্রদ্ধার আদর্শ স্থাপন করেন। তাঁর পরিবারে কোনো ধরনের অন্যায় বা হিংসার স্থান ছিল না; বরং পারস্পরিক সহযোগিতা ও ঈমান ছিল তাঁদের বন্ধনের মূল শক্তি。\n\n"
                            + "<b>নবুওয়তের ধারাবাহিকতা তাঁর পরিবারে:</b>\n\n"
                            + "আল্লাহ তায়ালা বলেন,\n\n"
                            + "وَبَارَكْنَا عَلَيْهِ وَعَلَىٰ إِسْحَاقَ ۚ وَمِن ذُرِّيَّتِهِمَا مُحْسِنٌ وَظَالِمٌ لِّنَفْسِهِ مُبِينٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা বরকত দিয়েছি তাঁর (ইব্রাহিমের) উপর এবং ইসহাকের উপর; তাঁদের বংশধরদের মধ্যে কেউ নেককার, আর কেউ নিজের প্রতি অন্যায়কারী。\n\n"
                            + "<b>[সূরা আস-সাফফাত – ১১৩]</b>\n\n"
                            + "এই আয়াতের মাধ্যমে বোঝা যায়, ইসহাক (আ.) এর পরিবার বরকতময় ছিল, যেখানে নবুওয়ত ও হেদায়াতের ধারাবাহিকতা স্থায়ীভাবে প্রতিষ্ঠিত হয়েছিল।",
                    "The family life of Prophet Ishaq (AS) was peaceful, devoted, and steeped in God-fearing piety. Born, raised, and established within a noble household of prophets, his family carried the torch of divine revelation across generations.\n\n"
                            + "<b>A Righteous Household Shaped by Prophetic Ideals:</b>\n\n"
                            + "As the son of Prophet Ibrahim (AS) and Lady Sarah (AS), Ishaq was immersed in an atmosphere of complete submission to Allah.\n\n"
                            + "رَحْمَتُ اللَّهِ وَبَرَكَاتُهُ عَلَيْكُمْ أَهْلَ الْبَيْتِ ۚ إِنَّهُ حَمِيدٌ مَجِيدٌ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"May the mercy of Allah and His blessings be upon you, people of the house. Indeed, He is Praiseworthy and Noble.\"\n\n"
                            + "<b>[Surah Hud: 73]</b>\n\n"
                            + "<b>Marriage and Children:</b>\n\n"
                            + "Islamic historical sources (Tafsir Ibn Kathir, Tabari) record that Prophet Ishaq married Rebekah (Ribqah), a virtuous and pious woman. Allah blessed their marriage with twin sons: Esau ('Iys) and Yaqub (Jacob, AS). Yaqub was subsequently chosen as a prophet, through whom the twelve tribes and noble prophets arose.\n\n"
                            + "<b>Piety and Family Harmony:</b>\n\n"
                            + "Ishaq (AS) diligently nurtured his family upon obedience to Allah, establishing prayer, and treating one another with loving respect, fairness, and compassion.\n\n"
                            + "<b>Continuous Blessings:</b>\n\n"
                            + "وَبَارَكْنَا عَلَيْهِ وَعَلَىٰ إِسْحَاقَ ۚ وَمِن ذُرِّيَّتِهِمَا مُحْسِنٌ وَظَالِمٌ لِّنَفْسِهِ مُبِينٌ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We blessed him and Ishaq. But among their descendants is the doer of good and the clearly unjust to himself.\"\n\n"
                            + "<b>[Surah As-Saffat: 113]</b>"
            ));

            // 7. বনী ইসরাঈল জাতির উৎপত্তি
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "বনী ইসরাঈল জাতির উৎপত্তি",
                    "Origin of the Children of Israel",
                    "হযরত ইসহাক (আ.) এর পুত্র হযরত ইয়াকুব (আ.) এর মাধ্যমেই বনী ইসরাঈল জাতির উৎপত্তি ঘটে। ইয়াকুব (আ.) কে আল্লাহ “ইসরাঈল” নামে অভিহিত করেছিলেন, যার অর্থ “আল্লাহর বান্দা” বা “আল্লাহর নির্বাচিত”। তাঁর বংশধরদের বলা হয় “বনী ইসরাঈল” অর্থাৎ, ইসরাঈলের সন্তানগণ। হযরত ইয়াকুব (আ.) এর বারো জন পুত্র ছিল, যাঁদের প্রত্যেকে একটি করে গোত্র বা পরিবার গঠন করেন। তাঁদের বংশধররাই পরবর্তীতে ইসরাঈল জাতির বারোটি গোত্রে পরিণত হয়। এই জাতি থেকেই আল্লাহ অনেক নবী পাঠান যেমন ইউসুফ (আ.), মূসা (আ.), দাউদ (আ.), সুলাইমান (আ.), ঈসা (আ.) প্রমুখ। সুতরাং, বনী ইসরাঈল জাতি মূলত হযরত ইব্রাহিম (আ.) এর বংশধর, যাঁরা হযরত ইসহাক (আ.) ও ইয়াকুব (আ.)-এর মাধ্যমে নবুওয়তের ধারাবাহিকতা বহন করে。\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ كُلًّا هَدَيْنَا ۚ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে (ইব্রাহিমকে) দান করেছি ইসহাক ও ইয়াকুব; উভয়কেই আমরা হেদায়াত দিয়েছি。\n\n"
                            + "<b>[সূরা আল-আনআম – ৮৪]</b>\n\n"
                            + "এই আয়াতে বোঝানো হয়েছে, ইসহাক (আ.) এর বংশেই নবুওয়তের আলো জ্বলেছিল। তাঁর পুত্র ইয়াকুব (আ.) নবী হয়ে বনী ইসরাঈলের পূর্বপুরুষ হন。\n\n"
                            + "كُلُّ الطَّعَامِ كَانَ حِلًّا لِبَنِي إِسْرَائِيلَ إِلَّا مَا حَرَّمَ إِسْرَائِيلُ عَلَىٰ نَفْسِهِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "সব খাদ্যই বনী ইসরাঈলের জন্য হালাল ছিল, শুধু যা ইসরাঈল নিজে নিজের উপর হারাম করেছিল তা ছাড়া。\n\n"
                            + "<b>[সূরা আলে ইমরান – ৯৩]</b>",
                    "Through Prophet Yaqub (AS), the son of Prophet Ishaq (AS), arose the nation of Bani Isra'il (the Children of Israel). Allah conferred upon Yaqub the title \"Isra'il\", meaning \"Servant of Allah\" or \"Chosen of Allah\".\n\n"
                            + "Prophet Yaqub (AS) had twelve sons, each of whom founded a distinct tribe, giving rise to the twelve tribes of Israel. From this nation, Allah raised countless renowned messengers including Yusuf, Musa, Dawud, Sulaiman, and Isa (peace be upon them all). Thus, Bani Isra'il are descendants of Ibrahim (AS) through the line of Ishaq and Yaqub (AS).\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ كُلًّا هَدَيْنَا ۚ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"And We gave to him Ishaq and Yaqub—all [of them] We guided.\"\n\n"
                            + "<b>[Surah Al-An'am: 84]</b>\n\n"
                            + "كُلُّ الطَّعَامِ كَانَ حِلًّا لِبَنِي إِسْرَائِيلَ إِلَّا مَا حَرَّمَ إِسْرَائِيلُ عَلَىٰ نَفْسِهِ\n\n"
                            + "<b>Meaning:</b>\n\n"
                            + "\"All food was lawful to the Children of Israel except what Israel had made unlawful to himself.\"\n\n"
                            + "<b>[Surah Ali 'Imran: 93]</b>"
            ));

            // 8. ওফাত ও কবরস্থান
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "ওফাত ও কবরস্থান",
                    "Passing and Resting Place",
                    "হযরত ইসহাক (আলাইহিস সালাম)-এর ওফাত ও কবর সম্পর্কিত বিবরণ কুরআনে সংক্ষিপ্তভাবে এসেছে, তবে ইসলামি ঐতিহাসিক গ্রন্থে (তাফসির ইবন কাসির, তাবারি, কুরতুবি প্রভৃতি) কিছু বিস্তারিত পাওয়া যায়。\n\n"
                            + "<b>ওফাত:</b>\n\n"
                            + "হযরত ইসহাক (আ.) দীর্ঘ জীবন অতিবাহিত করেন শান্তি, তাওহীদ প্রচার ও ইবাদতের মধ্যে। তাঁর বার্ধক্যকালেও তিনি আল্লাহর পথে অবিচল ছিলেন এবং বংশধরদের তাওহীদের দাওয়াত দিতে থাকেন। অধিকাংশ ইসলামি ঐতিহাসিক মতে, তিনি প্রায় ১৮০ বছর বয়সে ইন্তেকাল করেন。\n\n"
                            + "তাঁর মৃত্যুর আগে তিনি তাঁর পুত্র হযরত ইয়াকুব (আ.)-কে নবুওয়তের দায়িত্ব ও দাওয়াতি কাজের নির্দেশ দেন এবং আল্লাহর সন্তুষ্টির পথে থাকার উপদেশ দেন। তিনি মৃত্যুর সময়ও ছিলেন পূর্ণ ঈমান ও তাওহীদের অবস্থায়。\n\n"
                            + "<b>কবরস্থান:</b>\n\n"
                            + "ইসলামী ইতিহাস অনুযায়ী, হযরত ইসহাক (আ.)-কে সমাহিত করা হয় ফিলিস্তিনের খলিল নগরে, যা আজও “মাকপেলা গুহা ” নামে পরিচিত। এই স্থানেই তাঁর পিতা হযরত ইব্রাহিম (আ.) ও মাতা সারাহ (আ.), এবং পরবর্তীতে তাঁর পুত্র হযরত ইয়াকুব (আ.) ও স্ত্রী রিবকাহ (আ.)-ও সমাহিত হন। অর্থাৎ, খলিল শহরের এই সমাধিক্ষেত্রটি নবী পরিবারের কবরস্থান হিসেবে বিখ্যাত, যা আজও ঐতিহাসিকভাবে “মাকবারা-ই-ইবরাহিম” নামে পরিচিত।",
                    "While the Quran provides a concise account of the life of Prophet Ishaq (AS), classical Islamic historical texts (Tafsir Ibn Kathir, Tabari, Qurtubi) offer valuable records regarding his passing and burial.\n\n"
                            + "<b>His Demise:</b>\n\n"
                            + "Prophet Ishaq (AS) lived a long and blessed life devoted to peace, monotheism, and worship. Even in advanced old age, he remained steadfast in calling his descendants to righteousness. Islamic historical consensus indicates that he passed away at approximately 180 years of age. Prior to his demise, he entrusted the prophetic mission to his son Prophet Yaqub (AS).\n\n"
                            + "<b>Resting Place:</b>\n\n"
                            + "Islamic history documents that Prophet Ishaq (AS) was laid to rest in the city of al-Khalil (Hebron) in Palestine, at the historic site known as the <b>Cave of Machpelah</b>. In this sanctuary rest his father Prophet Ibrahim (AS), his mother Lady Sarah (AS), and subsequently his son Prophet Yaqub (AS) and wife Rebekah—a sacred resting place revered throughout Islamic history as \"Maqbarat Ibrahim\"."
            ));
        } else if (prophetId == 10) {
            // ==========================================
            // 10. হযরত ইয়াকুব (আলাইহিস সালাম) - 9 Chapters
            // ==========================================

            // 1. জন্ম ও বংশপরিচয়
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "জন্ম ও বংশপরিচয়",
                    "Birth and Lineage",
                    "হযরত ইয়াকুব (আ.) ছিলেন আল্লাহর এক নির্বাচিত নবী এবং হযরত ইসহাক (আ.) এর পুত্র ও হযরত ইব্রাহিম (আ.)-এর নাতি। তাঁর বংশের মাধ্যমেই পরবর্তীতে নবীগণের ধারাবাহিকতা স্থাপিত হয় এবং বনী ইসরাঈল জাতির উৎপত্তি ঘটে। হযরত ইয়াকুব (আ.)-এর জন্ম হয়েছিল ফিলিস্তিনের কনআন অঞ্চলে। তাঁর মাতা ছিলেন রিবকাহ এবং পিতা ইসহাক (আ.)। তিনি শৈশবকাল থেকেই ছিলেন সৎ, বিনয়ী ও আল্লাহভীরু। আল্লাহ তাঁকে নবুওয়তের জন্য মনোনীত করেন এবং তাওহীদের দাওয়াতের দায়িত্ব প্রদান করেন। তাঁর ১২ জন পুত্র ছিল, যাঁদের বংশধরদের থেকেই পরবর্তীতে বনী ইসরাঈল জাতি গঠিত হয়। হযরত ইউসুফ (আলাইহিস সালাম) ছিলেন তাঁর এক পুত্র। ইসলামী ইতিহাসে ইয়াকুব (আ.) “ইসরাঈল” নামে পরিচিত হন, যার অর্থ “আল্লাহর বান্দা” বা “আল্লাহর সঙ্গে সম্পর্কিত ব্যক্তি।” তাই তাঁর বংশধরদের বলা হয় “বনী ইসরাঈল” অর্থাৎ, ইসরাঈলের সন্তানগণ。\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ كُلًّا هَدَيْنَا ۚ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে (ইব্রাহিমকে) দান করেছি ইসহাক ও ইয়াকুব; উভয়কেই আমরা পথ দেখিয়েছি。\n\n"
                            + "<b>[সূরা আল-আনআম – ৮৪]</b>\n\n"
                            + "এই আয়াতে আল্লাহ জানাচ্ছেন যে, ইব্রাহিম (আ.)-এর পুত্র ইসহাক (আ.) ও নাতি ইয়াকুব (আ.) উভয়ই ছিলেন হেদায়াতপ্রাপ্ত নবী। এটি প্রমাণ করে যে, ইয়াকুব (আ.) নবুওয়তের উত্তরাধিকার বহন করেছিলেন。\n\n"
                            + "كُلُّ الطَّعَامِ كَانَ حِلًّا لِبَنِي إِسْرَائِيلَ إِلَّا مَا حَرَّمَ إِسْرَائِيلُ عَلَىٰ نَفْسِهِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "সব খাদ্যই বনী ইসরাঈলের জন্য হালাল ছিল, শুধু যা ইসরাঈল নিজে নিজের উপর হারাম করেছিল তা ছাড়া。\n\n"
                            + "<b>[সূরা আলে ইমরান – ৯৩]</b>\n\n"
                            + "এখানে “ইসরাঈল” বলতে হযরত ইয়াকুব (আ.)-কে বোঝানো হয়েছে। এই আয়াত থেকে তাঁর নামের মর্যাদা ও বংশধরদের নামকরণের উৎস স্পষ্টভাবে বোঝা যায়।",
                    "Prophet Yaqub (AS) was a chosen Messenger of Allah, the son of Prophet Ishaq (AS), and the grandson of Prophet Ibrahim (AS). Through his noble lineage, the continuous chain of prophethood was established, giving rise to the nation of Bani Isra'il (Children of Israel). He was born in the land of Canaan (Palestine). His mother was Rebekah and his father was Ishaq (AS).\n\n"
                            + "<b>Quranic Evidence:</b>\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ كُلًّا هَدَيْنَا ۚ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And We gave to Abraham, Isaac and Jacob - all [of them] We guided.\"\n\n"
                            + "<b>[Surah Al-An'am 6:84]</b>\n\n"
                            + "كُلُّ الطَّعَامِ كَانَ حِلًّا لِبَنِي إِسْرَائِيلَ إِلَّا مَا حَرَّمَ إِسْرَائِيلُ عَلَىٰ نَفْسِهِ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"All food was lawful to the Children of Israel except what Israel had made unlawful to himself...\"\n\n"
                            + "<b>[Surah Ali 'Imran 3:93]</b>\n\n"
                            + "Here, 'Isra'il' refers directly to Prophet Yaqub (AS), meaning 'Servant of Allah'."
            ));

            // 2. নবুওত লাভ
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "নবুওত লাভ",
                    "Attainment of Prophethood",
                    "হযরত ইয়াকুব (আ.) এর নবুওয়ত লাভ ছিল আল্লাহ তায়ালার এক বিশেষ অনুগ্রহ এবং তাঁর দাদা হযরত ইব্রাহিম (আ.) এর দোয়ার ফসল। তিনি ছিলেন নবুওয়তের উত্তরাধিকারী পরিবারে জন্ম নেওয়া একজন নবী তাঁর পিতা ইসহাক (আ.) নবী ছিলেন, আর দাদা ইব্রাহিম (আ.) ছিলেন “খলিলুল্লাহ” (আল্লাহর প্রিয়তম বন্ধু)। তাই ইয়াকুব (আ.) ছোটবেলা থেকেই তাওহীদের পরিবেশে বেড়ে উঠেছিলেন এবং আল্লাহ তাঁকে নবুওয়তের জন্য মনোনীত করেন。\n\n"
                            + "<b>নবুওয়তের প্রাপ্তি ও আল্লাহর প্রতিশ্রুতি:</b>\n\n"
                            + "ইসলামী সূত্র ও কুরআনের আলোকে, আল্লাহ তায়ালা হযরত ইব্রাহিম (আ.)-কে প্রতিশ্রুতি দিয়েছিলেন যে, তাঁর বংশধরদের মধ্যেই নবুওয়তের ধারাবাহিকতা বজায় থাকবে। এই প্রতিশ্রুতির অংশ হিসেবেই ইসহাক (আ.) এর পর নবুওয়ত আসে ইয়াকুব (আ.) এর ওপর। কুরআনে আল্লাহ বলেন,\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ كُلًّا هَدَيْنَا ۚ وَنُوحًا هَدَيْنَا مِن قَبْلُ ۖ وَمِن ذُرِّيَّتِهِ دَاوُودَ وَسُلَيْمَانَ وَأَيُّوبَ وَيُوسُفَ وَمُوسَىٰ وَهَارُونَ ۚ وَكَذَٰلِكَ نَجْزِي الْمُحْسِنِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাঁকে (ইব্রাহিমকে) দান করেছি ইসহাক ও ইয়াকুব; উভয়কেই আমরা পথ দেখিয়েছি। এবং নূহকেও আমরা পূর্বে পথ দেখিয়েছি; তাঁর বংশ থেকে দাউদ, সুলাইমান, আইয়ুব, ইউসুফ, মূসা ও হারূন সবাইকে আমরা পথ দেখিয়েছি। এভাবেই আমরা সৎকর্মশীলদের পুরস্কৃত করি。\n\n"
                            + "<b>[সূরা আল-আনআম – ৮৪]</b>\n\n"
                            + "<b>নবুওয়তের মূল বার্তা ও দায়িত্ব:</b>\n\n"
                            + "ইয়াকুব (আ.)-এর নবুওয়তের মূল বার্তা ছিল,\n\n"
                            + "• একমাত্র আল্লাহর ইবাদত করা\n"
                            + "• শিরক ও অন্যায় থেকে দূরে থাকা\n"
                            + "• সমাজে ন্যায়বিচার প্রতিষ্ঠা করা\n"
                            + "• সন্তান ও বংশধরদের ঈমানের পথে রাখা\n\n"
                            + "তিনি ফিলিস্তিন ও কনআন অঞ্চলে নবুওয়ত প্রাপ্ত হন এবং তাঁর জীবনের শেষ পর্যন্ত মানুষকে আল্লাহর একত্ব ও আনুগত্যের পথে আহ্বান করে যান। তাঁর মৃত্যুর সময়কার উপদেশ থেকেই তাঁর নবুওয়তের মর্মস্পর্শী দিকটি সবচেয়ে সুন্দরভাবে ফুটে ওঠে,\n\n"
                            + "إِذْ قَالَ لِبَنِيهِ مَا تَعْبُدُونَ مِن بَعْدِي ۖ قَالُوا نَعْبُدُ إِلَٰهَكَ وَإِلَٰهَ آبَائِكَ إِبْرَاهِيمَ وَإِسْمَاعِيلَ وَإِسْحَاقَ إِلَٰهًا وَاحِدًا وَنَحْنُ لَهُ مُسْلِمُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন ইয়াকুব তাঁর সন্তানদের বললেন, ‘আমার পরে তোমরা কাকে উপাসনা করবে?’ তারা বলল, ‘আমরা উপাসনা করব তোমার উপাস্যকে, তোমার পিতা ইব্রাহিম, ইসমাঈল ও ইসহাকের উপাস্যকে একমাত্র আল্লাহকে, এবং আমরা তাঁরই অনুগত。\n\n"
                            + "<b>[সূরা আল-বাকারা – ১৩৩]</b>",
                    "The bestowal of Prophethood upon Prophet Yaqub (AS) was a divine grace and the fruition of Prophet Ibrahim's supplication. Raised in the purest environment of monotheism, he was chosen by Allah to guide humanity and nurture the righteous lineage.\n\n"
                            + "<b>Quranic Declaration:</b>\n\n"
                            + "وَوَهَبْنَا لَهُ إِسْحَاقَ وَيَعْقُوبَ كُلًّا هَدَيْنَا ۚ وَنُوحًا هَدَيْنَا مِن قَبْلُ ۖ وَمِن ذُرِّيَّتِهِ دَاوُودَ وَسُلَيْمَانَ وَأَيُّوبَ وَيُوسُفَ وَمُوسَىٰ وَهَارُونَ ۚ وَكَذَٰلِكَ نَجْزِي الْمُحْسِنِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And We gave to him Isaac and Jacob - all [of them] We guided. And Noah, We guided before; and among his descendants, David and Solomon and Job and Joseph and Moses and Aaron. Thus do We reward the doers of good.\"\n\n"
                            + "<b>[Surah Al-An'am 6:84]</b>\n\n"
                            + "<b>Core Pillars of His Mission:</b>\n"
                            + "• Devoting absolute worship to Allah alone.\n"
                            + "• Rejecting all forms of polytheism and injustice.\n"
                            + "• Establishing societal righteousness and family faith.\n\n"
                            + "إِذْ قَالَ لِبَنِيهِ مَا تَعْبُدُونَ مِن بَعْدِي ۖ قَالُوا نَعْبُدُ إِلَٰهَكَ وَإِلَٰهَ آبَائِكَ إِبْرَاهِيمَ وَإِسْمَاعِيلَ وَإِسْحَاقَ إِلَٰهًا وَاحِدًا وَنَحْنُ لَهُ مُسْلِمُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"When he said to his sons, 'What will you worship after me?' They said, 'We will worship your God and the God of your fathers, Abraham and Ishmael and Isaac - one God. And we are Muslims [in submission] to Him.'\"\n\n"
                            + "<b>[Surah Al-Baqarah 2:133]</b>"
            ));

            // 3. ১২ জন পুত্রের জন্ম
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "১২ জন পুত্রের জন্ম",
                    "Birth of the Twelve Sons",
                    "হযরত ইয়াকুব (আ.) এর বারো (১২) জন পুত্র ছিলেন, এবং তাঁদের মাধ্যমেই “বনী ইসরাঈল” জাতির বারোটি গোত্র বা উপজাতি গঠিত হয়। ইসলামী ইতিহাস, তাফসির ইবন কাসির ও বাইবেলিক সূত্র (যা কুরআনের সাথে মিল রাখে) অনুযায়ী, ইয়াকুব (আ.) এর এই বারো পুত্রের জন্ম হয়। তাঁদের পরিবার ছিল ফিলিস্তিন ও পরবর্তীতে মিশর অঞ্চলে নবুওয়তের কেন্দ্র। নিচে তাঁর সন্তানদের জন্ম ও পরিচয় বিস্তারিতভাবে দেওয়া হলো। আল্লাহ বলেন,\n\n"
                            + "وَقَطَّعْنَاهُمُ اثْنَتَيْ عَشْرَةَ أَسْبَاطًا أُمَمًا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর আমরা তাদেরকে বারোটি গোত্রে বিভক্ত করেছি。\n\n"
                            + "<b>[সূরা আল-আ’রাফ – ১৬০]</b>\n\n"
                            + "এই আয়াতে বোঝানো হয়েছে, ইয়াকুব (আ.) এর বারো পুত্রের বংশ থেকেই বনী ইসরাঈলের বারোটি গোত্র গঠিত হয়, যা পরে স্বতন্ত্র জাতিগত পরিচয়ে পরিণত হয়。\n\n"
                            + "<b>বারো জন পুত্রের নাম ও সংক্ষিপ্ত পরিচয়:</b>\n\n"
                            + "১. রুবাইল – ইয়াকুব (আ.) এর জ্যেষ্ঠ পুত্র, লিয়া এর সন্তান।\n"
                            + "২. শামাউন – লিয়া এর দ্বিতীয় পুত্র।\n"
                            + "৩. লাবী – লিয়া এর তৃতীয় পুত্র; তাঁর বংশ থেকে নবী মূসা (আ.) ও হারূন (আ.) জন্ম নেন।\n"
                            + "৪. ইয়াহূজা – লিয়া এর চতুর্থ পুত্র; তাঁর বংশ থেকেই নবী দাউদ (আ.), সুলাইমান (আ.) ও ঈসা (আ.) এর বংশধারা।\n"
                            + "৫. ইসসাকার – লিয়া এর পঞ্চম পুত্র।\n"
                            + "৬. যাবুলুন – লিয়া এর ষষ্ঠ পুত্র।\n"
                            + "৭. দান – বিলহা এর সন্তান।\n"
                            + "৮. নাফতালি – বিলহা এর দ্বিতীয় পুত্র।\n"
                            + "৯. গাদ – জিলফা এর সন্তান।\n"
                            + "১০. আশের – জিলফা এর দ্বিতীয় পুত্র।\n"
                            + "১১. ইউসুফ – রাহেল এর প্রথম পুত্র, নবী ও স্বপ্ন ব্যাখ্যাকারী; কুরআনে তাঁর জীবনের কাহিনি বিস্তারিতভাবে বর্ণিত হয়েছে।\n"
                            + "১২. বিনইয়ামিন – রাহেল এর দ্বিতীয় ও কনিষ্ঠ পুত্র; ইউসুফ (আ.) এর আপন ভাই。\n\n"
                            + "<b>বংশধারার ধারাবাহিকতা:</b>\n\n"
                            + "এই বারো পুত্রের বংশ থেকেই বনী ইসরাঈলের ১২টি গোত্রের উৎপত্তি হয়। তাঁরা ইতিহাসে আলাদা আলাদা অঞ্চলে বসতি স্থাপন করেন এবং পরবর্তীকালে তাঁদের মধ্য থেকেই বহু নবী জন্মগ্রহণ করেন যেমন মূসা (আ.), হারূন (আ.), দাউদ (আ.), সুলাইমান (আ.), ঈসা (আ.) প্রমুখ。\n\n"
                            + "وَإِذْ قَالَ مُوسَىٰ لِقَوْمِهِ يَا قَوْمِ اذْكُرُوا نِعْمَتَ اللَّهِ عَلَيْكُمْ إِذْ جَعَلَ فِيكُمْ أَنْبِيَاءَ وَجَعَلَكُمْ مُلُوكًا وَآتَاكُمْ مَا لَمْ يُؤْتِ أَحَدًا مِنَ الْعَالَمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন মূসা তাঁর জাতিকে বললেন, ‘হে আমার জাতি! তোমরা আল্লাহর সেই অনুগ্রহ স্মরণ করো, যখন তিনি তোমাদের মধ্যে নবীদের করেছেন, তোমাদের শাসক বানিয়েছেন, এবং তোমাদের এমন অনুগ্রহ দিয়েছেন যা অন্য কাউকে দেননি。\n\n"
                            + "<b>[সূরা আল-মায়িদাহ – ২০]</b>\n\n"
                            + "এই আয়াতের “জাতি” বলতে বোঝানো হয়েছে বনী ইসরাঈল, যারা ইয়াকুব (আ.) এর বারো পুত্রের বংশধর আল্লাহ তাঁদের মধ্যে নবুওয়ত, রাজত্ব ও বরকত দান করেছিলেন।",
                    "Prophet Yaqub (AS) was blessed with twelve sons, who became the patriarchs of the Twelve Tribes of Israel (Asbat).\n\n"
                            + "<b>Quranic Reference:</b>\n\n"
                            + "وَقَطَّعْنَاهُمُ اثْنَتَيْ عَشْرَةَ أَسْبَاطًا أُمَمًا\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And We divided them into twelve descendant tribes [as distinct] nations...\"\n\n"
                            + "<b>[Surah Al-A'raf 7:160]</b>\n\n"
                            + "<b>The Twelve Sons:</b>\n"
                            + "1. Reuben (Rubil)\n"
                            + "2. Simeon (Sham'un)\n"
                            + "3. Levi (Lawi) - Ancestor of Prophet Musa (AS) and Prophet Harun (AS)\n"
                            + "4. Judah (Yahudha) - Ancestor of Prophet Dawud (AS), Sulaiman (AS), and Isa (AS)\n"
                            + "5. Issachar (Issakar)\n"
                            + "6. Zebulun (Zabulun)\n"
                            + "7. Dan\n"
                            + "8. Naphtali\n"
                            + "9. Gad\n"
                            + "10. Asher\n"
                            + "11. Joseph (Yusuf AS) - The beloved prophet and interpreter of dreams\n"
                            + "12. Benjamin (Binyamin) - Full brother of Prophet Yusuf (AS)"
            ));

            // 4. ইউসুফ (আ.) এর প্রতি ভালবাসা
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "ইউসুফ (আ.) এর প্রতি ভালবাসা",
                    "Love for Yusuf (AS)",
                    "হযরত ইয়াকুব (আ.) এর পুত্র হযরত ইউসুফ (আ.) এর প্রতি তাঁর ভালোবাসা ছিল গভীর, পিতৃত্ব ও নবুওয়তের ভালোবাসার এক মহান উদাহরণ। ইউসুফ (আ.) ছিলেন তাঁর সবচেয়ে প্রিয় পুত্রদের একজন শুধু সৌন্দর্য বা চরিত্রের কারণে নয়, বরং তাঁর মধ্যে ইয়াকুব (আ.) নবুওয়তের আলোর ছায়া দেখতে পেয়েছিলেন。\n\n"
                            + "<b>পিতৃসুলভ ভালোবাসার প্রকৃতি:</b>\n\n"
                            + "হযরত ইউসুফ (আ.) ছোটবেলা থেকেই অত্যন্ত বুদ্ধিমান, ভদ্র ও আল্লাহভীরু ছিলেন। হযরত ইয়াকুব (আ.) বুঝতে পেরেছিলেন যে আল্লাহ তাঁকে বিশেষ মর্যাদা দান করবেন। এ কারণেই তিনি ইউসুফ (আ.) এর প্রতি বেশি স্নেহ দেখাতেন। এই ভালোবাসা ছিল নবুওয়তের দূরদৃষ্টিসম্পন্ন দৃষ্টিভঙ্গির অংশ, কোনো সাধারণ পক্ষপাতিত্ব নয়। কুরআনে আল্লাহ তায়ালা বলেন,\n\n"
                            + "إِذْ قَالَ يُوسُفُ لِأَبِيهِ يَا أَبَتِ إِنِّي رَأَيْتُ أَحَدَ عَشَرَ كَوْكَبًا وَالشَّمْسَ وَالْقَمَرَ رَأَيْتُهُمْ لِي سَاجِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন ইউসুফ তাঁর পিতাকে বলল, ‘হে আমার পিতা! আমি দেখেছি স্বপ্নে, এগারোটি তারা, সূর্য ও চাঁদ — আমি দেখেছি, তারা সবাই আমাকে সেজদা করছে。\n\n"
                            + "<b>[সূরা ইউসুফ – ৪]</b>\n\n"
                            + "এই আয়াতেই বোঝা যায়, ইউসুফ (আ.) শৈশবে তাঁর পিতা ইয়াকুব (আ.) এর কাছে স্বপ্নের কথা বলেছিলেন। ইয়াকুব (আ.) সঙ্গে সঙ্গে বুঝতে পারেন, এটি নবুওয়তের নিদর্শন। তাই তাঁর ভালোবাসা ছিল আল্লাহর পরিকল্পনার গভীর উপলব্ধি থেকে উদ্ভূত。\n\n"
                            + "<b>স্নেহ ও প্রজ্ঞার মিশ্রিত উপদেশ:</b>\n\n"
                            + "হযরত ইয়াকুব (আ.) তাঁর পুত্রকে শুধু ভালোবাসতেন না, বরং তাঁকে আল্লাহর পথে রাখার জন্য জ্ঞান ও সতর্কতা প্রদান করতেন। তিনি বলেছিলেন,\n\n"
                            + "قَالَ يَا بُنَيَّ لَا تَقْصُصْ رُؤْيَاكَ عَلَىٰ إِخْوَتِكَ فَيَكِيدُوا لَكَ كَيْدًا ۖ إِنَّ الشَّيْطَانَ لِلْإِنسَانِ عَدُوٌّ مُّبِينٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি (ইয়াকুব) বললেন, ‘হে আমার ছেলে, তোমার এই স্বপ্ন ভাইদের কাছে বলো না, তারা যেন তোমার বিরুদ্ধে কোনো ষড়যন্ত্র না করে, কারণ শয়তান তো মানুষের প্রকাশ্য শত্রু。\n\n"
                            + "<b>[সূরা ইউসুফ – ৫]</b>\n\n"
                            + "<b>বিচ্ছেদ ও ধৈর্যের ভালোবাসা:</b>\n\n"
                            + "যখন ভাইয়েরা ইউসুফ (আ.) কে কূপে ফেলে দেয়, ইয়াকুব (আ.) এর অন্তর ভেঙে যায়, কিন্তু তিনি আল্লাহর প্রতি পূর্ণ আস্থা রাখেন। তিনি বলেন,\n\n"
                            + "فَصَبْرٌ جَمِيلٌ ۖ وَاللَّهُ الْمُسْتَعَانُ عَلَىٰ مَا تَصِفُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতএব, আমি সুন্দর ধৈর্য ধারণ করব; এবং তোমরা যা বলছো, তার ব্যাপারে আল্লাহই সাহায্যকারী。\n\n"
                            + "<b>[সূরা ইউসুফ – ১৮]</b>\n\n"
                            + "এই আয়াতে পিতার সেই অসীম ধৈর্য ও ঈমানের প্রতিফলন দেখা যায়। তাঁর ভালোবাসা কেবল পার্থিব নয়, বরং আল্লাহর প্রতি আস্থা ও নবুওয়তের দৃঢ়তার সঙ্গে যুক্ত ছিল।",
                    "Prophet Yaqub (AS) nurtured profound paternal love and prophetic insight towards Prophet Yusuf (AS), discerning in him the radiance of future prophethood.\n\n"
                            + "<b>The Prophetic Vision of Yusuf (AS):</b>\n\n"
                            + "إِذْ قَالَ يُوسُفُ لِأَبِيهِ يَا أَبَتِ إِنِّي رَأَيْتُ أَحَدَ عَشَرَ كَوْكَبًا وَالشَّمْسَ وَالْقَمَرَ رَأَيْتُهُمْ لِي سَاجِدِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"When Joseph said to his father, 'O my father, indeed I have seen [in a dream] eleven stars and the sun and the moon; I saw them prostrating to me.'\"\n\n"
                            + "<b>[Surah Yusuf 12:4]</b>\n\n"
                            + "<b>Wise Counsel of a Loving Father:</b>\n\n"
                            + "قَالَ يَا بُنَيَّ لَا تَقْصُصْ رُؤْيَاكَ عَلَىٰ إِخْوَتِكَ فَيَكِيدُوا لَكَ كَيْدًا ۖ إِنَّ الشَّيْطَانَ لِلْإِنسَانِ عَدُوٌّ مُّبِينٌ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"He said, 'O my son, do not relate your vision to your brothers or they will contrive against you a plan. Indeed Satan, to man, is an open enemy.'\"\n\n"
                            + "<b>[Surah Yusuf 12:5]</b>\n\n"
                            + "<b>Patience in Trial (Sabrun Jameel):</b>\n\n"
                            + "فَصَبْرٌ جَمِيلٌ ۖ وَاللَّهُ الْمُسْتَعَانُ عَلَىٰ مَا تَصِفُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"...So patience is most fitting. And Allah is the one sought for help against that which you describe.\"\n\n"
                            + "<b>[Surah Yusuf 12:18]</b>"
            ));

            // 5. পুত্রদের ঈর্ষা ও ইউসুফ (আ.) এর হারিয়ে যাওয়া
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "পুত্রদের ঈর্ষা ও ইউসুফ (আ.) এর হারিয়ে যাওয়া",
                    "Jealousy of the Brothers and Loss of Yusuf (AS)",
                    "হযরত ইয়াকুব (আ.) এর পরিবারে একটি পরীক্ষার সময় এসেছিল, যা ইতিহাসে “ইউসুফ (আ.) এর হারিয়ে যাওয়া ঘটনা” নামে পরিচিত। এই ঘটনা শুধু পারিবারিক নয়, বরং ঈমান, ধৈর্য ও আল্লাহর পরিকল্পনার এক গভীর নিদর্শন। নিচে বিষয়টি বিস্তারিতভাবে কুরআনুল কারিম ও তাফসিরের আলোকে তুলে ধরা হলো。\n\n"
                            + "<b>পুত্রদের হিংসা ও ঈর্ষা:</b>\n\n"
                            + "হযরত ইয়াকুব (আ.) এর বারো জন পুত্রের মধ্যে ইউসুফ (আ.) ছিলেন সবচেয়ে প্রিয়। কারণ, ইয়াকুব (আ.) তাঁর মধ্যে নবুওয়তের আলো দেখতে পেয়েছিলেন। কিন্তু ভাইয়েরা এই বিশেষ ভালোবাসাকে ভুলভাবে বুঝে ফেলে। তাঁদের মনে ইউসুফ (আ.) এর প্রতি ঈর্ষা ও বিদ্বেষ জন্ম নেয়। কুরআনে বর্ণিত আছে,\n\n"
                            + "إِذْ قَالُوا لَيُوسُفُ وَأَخُوهُ أَحَبُّ إِلَىٰ أَبِينَا مِنَّا وَنَحْنُ عُصْبَةٌ ۖ إِنَّ أَبَانَا لَفِي ضَلَالٍ مُّبِينٍ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন তারা বলল, ‘ইউসুফ ও তাঁর ভাই আমাদের পিতার কাছে আমাদের চেয়ে বেশি প্রিয়, অথচ আমরা শক্তিশালী একটি দল। নিশ্চয়ই আমাদের পিতা স্পষ্ট ভ্রান্তিতে আছেন。\n\n"
                            + "<b>[সূরা ইউসুফ – ৮]</b>\n\n"
                            + "এখানে তাঁদের অন্তরের হিংসা ও অজ্ঞতার চিত্র ফুটে উঠেছে। তাঁরা ভেবেছিল, পিতার ভালোবাসা অর্জনের জন্য ইউসুফ (আ.) কে দূর করা প্রয়োজন যা ছিল তাঁদের জন্য এক বড় পরীক্ষা。\n\n"
                            + "<b>ইউসুফ (আ.) কে কূপে ফেলে দেওয়া:</b>\n\n"
                            + "ঈর্ষা ও শয়তানের প্ররোচনায় ভাইয়েরা এক ষড়যন্ত্র রচনা করে। তাঁরা সিদ্ধান্ত নেয় ইউসুফ (আ.) কে হত্যা করবে বা কোথাও দূরে ফেলে দেবে, যাতে পিতার ভালোবাসা তাদের দিকে ফিরে আসে。\n\n"
                            + "اقْتُلُوا يُوسُفَ أَوِ اطْرَحُوهُ أَرْضًا يَخْلُ لَكُمْ وَجْهُ أَبِيكُمْ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা ইউসুফকে হত্যা করো অথবা কোথাও দূরে ফেলে দাও, যাতে তোমাদের পিতার ভালোবাসা শুধু তোমাদের জন্য থাকে。\n\n"
                            + "<b>[সূরা ইউসুফ – ৯]</b>\n\n"
                            + "কিন্তু আল্লাহর পরিকল্পনা ছিল অন্যরকম। শেষ পর্যন্ত তাঁরা ইউসুফ (আ.) কে হত্যা না করে একটি গভীর কূপে ফেলে দেয়。\n\n"
                            + "فَلَمَّا ذَهَبُوا بِهِ وَأَجْمَعُوا أَنْ يَجْعَلُوهُ فِي غَيَابَةِ الْجُبِّ وَأَوْحَيْنَا إِلَيْهِ لَتُنَبِّئَنَّهُمْ بِأَمْرِهِمْ هَٰذَا وَهُمْ لَا يَشْعُرُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন তারা তাঁকে নিয়ে গেল এবং একমত হলো তাঁকে কূপে নিক্ষেপ করবে, তখন আমি ইউসুফকে প্রত্যাদেশ দিলাম, ‘একদিন তুমি তাদের এই কাজের কথা স্মরণ করাবে, অথচ তারা তা বুঝবে না。\n\n"
                            + "<b>[সূরা ইউসুফ – ১৫]</b>\n\n"
                            + "এই আয়াতে দেখা যায়, আল্লাহ ইউসুফ (আ.) কে সেই মুহূর্তেই নবুওয়তের আশ্বাস দেন। এটি ছিল পরীক্ষা, কিন্তু একই সঙ্গে ভবিষ্যৎ গৌরবের সূচনা。\n\n"
                            + "<b>পিতাকে ধোঁকা দেওয়া ও শোক:</b>\n\n"
                            + "ভাইয়েরা কূপে ফেলে আসার পর এক ষড়যন্ত্র সাজায়। তাঁরা একটি ছাগল হত্যা করে তার রক্ত ইউসুফের জামায় মেখে পিতার কাছে আসে, বলে ইউসুফকে নেকড়ে খেয়ে ফেলেছে。\n\n"
                            + "وَجَاءُوا عَلَىٰ قَمِيصِهِ بِدَمٍ كَذِبٍ ۚ قَالَ بَلْ سَوَّلَتْ لَكُمْ أَنْفُسُكُمْ أَمْرًا ۖ فَصَبْرٌ جَمِيلٌ ۖ وَاللَّهُ الْمُسْتَعَانُ عَلَىٰ مَا تَصِفُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা ইউসুফের জামা মিথ্যা রক্তে মেখে নিয়ে এলো। (ইয়াকুব) বললেন, ‘তোমাদের অন্তর তোমাদেরকে এক মন্দ কাজ সহজ করে দেখিয়েছে; সুতরাং আমি সুন্দর ধৈর্য ধারণ করব। তোমরা যা বর্ণনা করছো, তাতে আল্লাহই সাহায্যকারী。\n\n"
                            + "<b>[সূরা ইউসুফ – ১৮]</b>\n\n"
                            + "ইয়াকুব (আ.) গভীর বেদনায় ভেঙে পড়লেও তিনি আল্লাহর ওপর ভরসা রাখলেন। তাঁর এই ধৈর্য ছিল ঈমানের পরাকাষ্ঠা।",
                    "The story of the brothers' jealousy towards Yusuf (AS) and casting him into the well represents a momentous trial of faith, patience, and divine wisdom.\n\n"
                            + "<b>The Plot of the Brothers:</b>\n\n"
                            + "إِذْ قَالُوا لَيُوسُفُ وَأَخُوهُ أَحَبُّ إِلَىٰ أَبِينَا مِنَّا وَنَحْنُ عُصْبَةٌ ۖ إِنَّ أَبَانَا لَفِي ضَلَالٍ مُّبِينٍ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"When they said, 'Joseph and his brother are more beloved to our father than we, while we are a clan. Indeed, our father is in clear error.'\"\n\n"
                            + "<b>[Surah Yusuf 12:8]</b>\n\n"
                            + "<b>Casting into the Well:</b>\n\n"
                            + "فَلَمَّا ذَهَبُوا بِهِ وَأَجْمَعُوا أَنْ يَجْعَلُوهُ فِي غَيَابَةِ الْجُبِّ وَأَوْحَيْنَا إِلَيْهِ لَتُنَبِّئَنَّهُمْ بِأَمْرِهِمْ هَٰذَا وَهُمْ لَا يَشْعُرُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"So when they took him and agreed to put him into the bottom of the well... But We inspired to him, 'You will surely inform them [someday] of this affair of theirs while they do not perceive.'\"\n\n"
                            + "<b>[Surah Yusuf 12:15]</b>\n\n"
                            + "<b>The False Blood and Grief:</b>\n\n"
                            + "وَجَاءُوا عَلَىٰ قَمِيصِهِ بِدَمٍ كَذِبٍ ۚ قَالَ بَلْ سَوَّلَتْ لَكُمْ أَنْفُسُكُمْ أَمْرًا ۖ فَصَبْرٌ جَمِيلٌ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And they brought upon his shirt false blood. [Jacob] said, 'Rather, your souls have enticed you to something, so patience is most fitting.'\"\n\n"
                            + "<b>[Surah Yusuf 12:18]</b>"
            ));

            // 6. দীর্ঘকাল শোকে অন্ধ হয়ে যাওয়া
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "দীর্ঘকাল শোকে অন্ধ হয়ে যাওয়া",
                    "Blindness from Prolonged Grief",
                    "হযরত ইয়াকুব (আ.) তাঁর প্রিয় পুত্র ইউসুফ (আ.) হারিয়ে যাওয়ার পর গভীর শোকে দীর্ঘকাল কেঁদেছিলেন। তাঁর এই কষ্ট ছিল না কেবল পিতৃত্বের ভালোবাসা থেকে, বরং তিনি জানতেন যে ইউসুফ (আ.) আল্লাহর নির্বাচিত নবী এবং তাঁর জীবনে কোনো বিশেষ পরিকল্পনা রয়েছে। তবে সন্তানের বিচ্ছেদ তাঁকে অন্তর থেকে বিদীর্ণ করে দিয়েছিল。\n\n"
                            + "<b>শোকে অন্ধ হয়ে যাওয়া:</b>\n\n"
                            + "দীর্ঘদিন সন্তানের বিচ্ছেদে কান্না করতে করতে হযরত ইয়াকুব (আ.)-এর দৃষ্টিশক্তি হারিয়ে যায়। কুরআনে আল্লাহ বলেন,\n\n"
                            + "وَتَوَلَّىٰ عَنْهُمْ وَقَالَ يَا أَسَفَىٰ عَلَىٰ يُوسُفَ وَابْيَضَّتْ عَيْنَاهُ مِنَ الْحُزْنِ فَهُوَ كَظِيمٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি (ইয়াকুব) তাঁদের থেকে মুখ ফিরিয়ে নিয়ে বললেন, ‘হায়, ইউসুফ!’ দুঃখে তাঁর চোখ সাদা হয়ে গেল (তিনি অন্ধ হয়ে গেলেন), কিন্তু তিনি ছিলেন অন্তরে সংযত。\n\n"
                            + "<b>[সূরা ইউসুফ – ৮৪]</b>\n\n"
                            + "এই আয়াতে বোঝানো হয়েছে, তিনি কান্না ও বেদনায় এতটাই কাতর হয়ে পড়েছিলেন যে তাঁর চোখের দৃষ্টি চলে যায়। কিন্তু তিনি কখনো আল্লাহর প্রতি অভিযোগ করেননি বরং তাঁর অন্তর ছিল পূর্ণ তাওয়াক্কুলে (ভরসায়)।",
                    "Throughout the prolonged separation from his beloved son Yusuf (AS), Prophet Yaqub (AS) wept intensely, yet his heart remained anchored in complete reliance upon Allah.\n\n"
                            + "<b>Loss of Eyesight through Grief:</b>\n\n"
                            + "وَتَوَلَّىٰ عَنْهُمْ وَقَالَ يَا أَسَفَىٰ عَلَىٰ يُوسُফَ وَابْيَضَّتْ عَيْنَاهُ مِنَ الْحُزْنِ فَهُوَ كَظِيمٌ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And he turned away from them and said, 'Oh, my sorrow over Joseph,' and his eyes became white from grief, for he was [suppressing] grief.\"\n\n"
                            + "<b>[Surah Yusuf 12:84]</b>\n\n"
                            + "Despite profound sorrow that cost him his sight, Prophet Yaqub (AS) never despaired of Allah's mercy, constantly declaring: 'I only complain of my suffering and my grief to Allah' [12:86]."
            ));

            // 7. ইউসুফ (আ.) এর পুনর্মিলন
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "ইউসুফ (আ.) এর পুনর্মিলন",
                    "Reunion with Yusuf (AS)",
                    "হযরত ইয়াকুব (আলাইহিস সালাম) ও ইউসুফ (আলাইহিস সালাম)-এর পিতা–পুত্রের পুনর্মিলন কুরআনের অন্যতম আবেগময় ও অলৌকিক ঘটনা। দীর্ঘ বছরের বিচ্ছেদের পর, আল্লাহ তায়ালা তাঁর পরিকল্পনা ও রহমতের মাধ্যমে তাঁদের আবার একত্রিত করেন—এটি ছিল ধৈর্য, ভালোবাসা ও আল্লাহর উপর পূর্ণ আস্থার পরিণতি。\n\n"
                            + "<b>ইউসুফ (আ.) এর মিশরে মর্যাদা লাভ:</b>\n\n"
                            + "যখন ইউসুফ (আ.) কূপে ফেলে দেওয়া হয়েছিল, আল্লাহর পরিকল্পনায় তাঁকে এক কাফেলা মিশরে নিয়ে যায়। পরবর্তীতে আল্লাহ তাঁকে জ্ঞান, প্রজ্ঞা ও মর্যাদা দান করেন, এবং তিনি মিশরের রাজদরবারে সম্মানিত ও প্রভাবশালী ব্যক্তিত্বে পরিণত হন。\n\n"
                            + "وَكَذَٰلِكَ مَكَّنَّا لِيُوسُفَ فِي الْأَرْضِ وَلِنُعَلِّمَهُ مِن تَأْوِيلِ الْأَحَادِيثِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "এভাবেই আমি ইউসুফকে সেই দেশে প্রতিষ্ঠিত করলাম এবং তাঁকে স্বপ্নের ব্যাখ্যা শেখালাম。\n\n"
                            + "<b>[সূরা ইউসুফ – ২১]</b>\n\n"
                            + "<b>পুনর্মিলনের সূচনা:</b>\n\n"
                            + "দীর্ঘ বছর পর যখন দুর্ভিক্ষ দেখা দিল, ইয়াকুব (আ.) তাঁর পুত্রদের মিশরে খাদ্য সংগ্রহের জন্য পাঠান। সেখানে ইউসুফ (আ.) তাঁদের চিনে ফেলেন, কিন্তু তাঁরা তাঁকে চিনতে পারেননি। ধীরে ধীরে আল্লাহর পরিকল্পনায় সবকিছু প্রকাশিত হয়। শেষ পর্যন্ত ইউসুফ (আ.) তাঁর পরিচয় প্রকাশ করে বলেন,\n\n"
                            + "قَالَ أَنَا يُوسُفُ وَهَٰذَا أَخِي ۖ قَدْ مَنَّ اللَّهُ عَلَيْنَا ۖ إِنَّهُ مَن يَتَّقِ وَيَصْبِرْ فَإِنَّ اللَّهَ لَا يُضِيعُ أَجْرَ الْمُحْسِنِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি বললেন, ‘আমি ইউসুফ, আর এটি আমার ভাই (বিনইয়ামিন)। আল্লাহ আমাদের প্রতি অনুগ্রহ করেছেন। নিশ্চয়ই যে ব্যক্তি তাকওয়া অবলম্বন করে ও ধৈর্য ধরে, আল্লাহ তার সৎকর্মের প্রতিদান নষ্ট করেন না。\n\n"
                            + "<b>[সূরা ইউসুফ – ৯০]</b>\n\n"
                            + "<b>জামার বরকতে দৃষ্টিশক্তি ফিরে আসা:</b>\n\n"
                            + "ইউসুফ (আ.) তাঁর ভাইদের মাধ্যমে পিতার কাছে একটি জামা পাঠান এবং বলেন,\n\n"
                            + "اذْهَبُوا بِقَمِيصِي هَٰذَا فَأَلْقُوهُ عَلَىٰ وَجْهِ أَبِي يَأْتِ بَصِيرًا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা আমার এ জামাটি নিয়ে যাও এবং এটা আমার পিতার চেহারার উপর রেখো; তিনি দৃষ্টি শক্তি ফিরে পাবেন。\n\n"
                            + "<b>[সূরা ইউসুফ – ৯৩]</b>\n\n"
                            + "যখন সেই জামাটি ইয়াকুব (আ.) এর মুখে লাগানো হলো,\n\n"
                            + "فَلَمَّا أَنْ جَاءَ الْبَشِيرُ أَلْقَاهُ عَلَىٰ وَجْهِهِ فَارْتَدَّ بَصِيرًا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর যখন সুসংবাদবাহক উপস্থিত হল এবং তার চেহারার উপর জামাটি রাখল তখন তিনি দৃষ্টিশক্তি ফিরে পেলেন。\n\n"
                            + "<b>[সূরা ইউসুফ – ৯৬]</b>\n\n"
                            + "এই অলৌকিক ঘটনায় আল্লাহ তাঁর নবীর ধৈর্য ও কান্নার প্রতিদান দিলেন দৃষ্টি ফিরে এল এবং পুত্রকে ফিরে পেলেন。\n\n"
                            + "<b>পিতা-পুত্রের পুনর্মিলনের মুহূর্ত:</b>\n\n"
                            + "পরে ইয়াকুব (আ.) তাঁর সমস্ত পরিবার নিয়ে মিশরে পুত্রের সঙ্গে দেখা করতে যান। ইউসুফ (আ.) তাঁদের সম্মান ও ভালোবাসা দিয়ে গ্রহণ করেন。\n\n"
                            + "فَلَمَّا دَخَلُوا عَلَىٰ يُوسُفَ آوَىٰ إِلَيْهِ أَبَوَيْهِ وَقَالَ ادْخُلُوا مِصْرَ إِن شَاءَ اللَّهُ آمِنِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর তারা যখন ইউসুফের কাছে উপস্থিত হল, তখন তিনি তার পিতামাতাকে নিজের কাছে স্থান দিলেন এবং বললেন, আপনারা আল্লাহর ইচ্ছায় নিরাপদে মিসরে প্রবেশ করুন。\n\n"
                            + "<b>[সূরা ইউসুফ – ৯৯]</b>\n\n"
                            + "এরপর ইউসুফ (আ.) তাঁর পিতা-মাতাকে সিংহাসনে বসালেন এবং সবাই আল্লাহর সামনে সেজদায় নত হয়ে গেল\n\n"
                            + "وَرَفَعَ أَبَوَيْهِ عَلَى الْعَرْشِ وَخَرُّوا لَهُ سُجَّدًا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর ইউসুফ তার পিতা-মাতাকে উঁচু আসনে বসালেন এবং তারা সবাই তার সম্মানে সিজদায় লুটিয়ে পড়ল。\n\n"
                            + "<b>[সূরা ইউসুফ – ১০০]</b>",
                    "The miraculous and emotional reunion between Prophet Yaqub (AS) and Prophet Yusuf (AS) is one of the crowning stories of Quranic revelation.\n\n"
                            + "<b>Restoration of Eyesight:</b>\n\n"
                            + "اذْهَبُوا بِقَمِيصِي هَٰذَا فَأَلْقُوهُ عَلَىٰ وَجْهِ أَبِي يَأْتِ بَصِيرًا\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"Take this shirt of mine and cast it over the face of my father; he will become seeing...\"\n\n"
                            + "<b>[Surah Yusuf 12:93]</b>\n\n"
                            + "فَلَمَّا أَنْ جَاءَ الْبَشِيرُ أَلْقَاهُ عَلَىٰ وَجْهِهِ فَارْتَدَّ بَصِيرًا\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And when the bearer of good tidings arrived, he cast it over his face, and he returned to seeing.\"\n\n"
                            + "<b>[Surah Yusuf 12:96]</b>\n\n"
                            + "<b>The Joyous Reunion:</b>\n\n"
                            + "فَلَمَّا دَخَلُوا عَلَىٰ يُوسُفَ آوَىٰ إِلَيْهِ أَبَوَيْهِ وَقَالَ ادْخُلُوا مِصْرَ إِن شَاءَ اللَّهُ آمِنِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And when they entered upon Joseph, he took his parents to himself and said, 'Enter Egypt, if Allah wills, in safety.'\"\n\n"
                            + "<b>[Surah Yusuf 12:99]</b>"
            ));

            // 8. মিশরে গমন ও স্থায়ী হওয়া
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "মিশরে গমন ও স্থায়ী হওয়া",
                    "Migration and Permanent Settlement in Egypt",
                    "হযরত ইয়াকুব (আ.) ও তাঁর পরিবার, বিশেষ করে ইউসুফ (আ.) এর সঙ্গে পুনর্মিলনের পর, আল্লাহর আদেশে তাঁরা মিশরে স্থায়ীভাবে বসবাস শুরু করেন। এই ঘটনাটি কুরআনে অত্যন্ত সুন্দরভাবে বর্ণিত হয়েছে এটি ছিল শুধু পারিবারিক পুনর্মিলন নয়, বরং আল্লাহর রহমতে বনী ইসরাঈল জাতির নতুন অধ্যায়ের সূচনা。\n\n"
                            + "<b>মিশরে আগমন:</b>\n\n"
                            + "যখন ইউসুফ (আ.) তাঁর পরিচয় প্রকাশ করলেন এবং পিতা ভাইদের ক্ষমা করে দিলেন, তখন তিনি তাঁদের মিশরে আসার আমন্ত্রণ জানান। তিনি বললেন,\n\n"
                            + "اذْهَبُوا بِقَمِيصِي هَٰذَا فَأَلْقُوهُ عَلَىٰ وَجْهِ أَبِي يَأْتِ بَصِيرًا وَأْتُونِي بِأَهْلِكُمْ أَجْمَعِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা আমার এই জামাটি নিয়ে যাও, আমার পিতার মুখে ফেলে দাও, তাঁর দৃষ্টি ফিরে আসবে; তারপর তোমরা তোমাদের সব পরিবার পরিজন নিয়ে আমার কাছে চলে এসো。\n\n"
                            + "<b>[সূরা ইউসুফ – ৯৩]</b>\n\n"
                            + "এই নির্দেশ অনুযায়ী ইয়াকুব (আ.) তাঁর পরিবারসহ মিশরের পথে যাত্রা করেন。\n\n"
                            + "<b>পুনর্মিলন ও সম্মানজনক গ্রহণ:</b>\n\n"
                            + "যখন ইয়াকুব (আ.) মিশরে পৌঁছালেন, ইউসুফ (আ.) নিজে রাজকীয় মর্যাদায় তাঁকে স্বাগত জানান। কুরআনে বলা হয়েছে,\n\n"
                            + "فَلَمَّا دَخَلُوا عَلَىٰ يُوسُفَ آوَىٰ إِلَيْهِ أَبَوَيْهِ وَقَالَ ادْخُلُوا مِصْرَ إِن شَاءَ اللَّهُ آمِنِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন তারা ইউসুফের কাছে প্রবেশ করল, তিনি তাঁর পিতা মাতাকে নিজের কাছে টেনে নিলেন এবং বললেন, ‘আল্লাহ চাইলে তোমরা নিরাপদে মিশরে প্রবেশ করো。\n\n"
                            + "<b>[সূরা ইউসুফ – ৯৯]</b>\n\n"
                            + "<b>স্থায়ী বসবাস ও আল্লাহর বরকত:</b>\n\n"
                            + "পুনর্মিলনের পর হযরত ইয়াকুব (আ.) ও তাঁর পরিবার মিশরের গসান নামক অঞ্চলে বসতি স্থাপন করেন। সেখানে তারা দীর্ঘ সময় শান্তি ও সমৃদ্ধিতে বসবাস করেন। ইউসুফ (আ.) তাঁদের দেখাশোনা করতেন এবং খাদ্য, বাসস্থান ও প্রয়োজনীয় সব কিছুর ব্যবস্থা করেন। এই সময় বনী ইসরাঈলের সংখ্যা ধীরে ধীরে বৃদ্ধি পেতে থাকে। এটি ছিল তাঁদের জাতি গঠনের সূচনা。\n\n"
                            + "<b>আল্লাহর প্রতিশ্রুতি পূর্ণ হওয়া:</b>\n\n"
                            + "হযরত ইউসুফ (আ.) সেই মুহূর্তে আল্লাহর প্রতিশ্রুতি স্মরণ করেন, যা তিনি শৈশবে স্বপ্নে দেখেছিলেন সূর্য, চাঁদ ও এগারোটি তারা তাঁকে সেজদা করছে。\n\n"
                            + "وَ رَفَعَ اَبَوَیۡهِ عَلَی الۡعَرۡشِ وَ خَرُّوۡا لَهٗ سُجَّدًا ۚ وَ قَالَ یٰۤاَبَتِ هٰذَا تَاۡوِیۡلُ رُءۡیَایَ مِنۡ قَبۡلُ ۫ قَدۡ جَعَلَهَا رَبِّیۡ حَقًّا ؕ وَ قَدۡ اَحۡسَنَ بِیۡۤ اِذۡ اَخۡرَجَنِیۡ مِنَ السِّجۡনِ وَ جَآءَ بِكُمۡ مِّنَ الۡبَدۡوِ مِنۡۢ بَعۡدِ اَنۡ نَّزَغَ الشَّیۡطٰنُ بَیۡনِیۡ وَ بَیۡنَ اِخۡوَتِیۡ ؕ اِنَّ رَبِّیۡ لَطِیۡفٌ لِّمَا یَشَآءُ ؕ اِنَّهٗ هُوَ الْعَلِيمُ الْحَكِيمُ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর ইউসুফ তার পিতা-মাতাকে উঁচু আসনে বসালেন এবং তারা সবাই তার সম্মানে সিজদায় লুটিয়ে পড়ল। তিনি বললেন, হে আমার পিতা! এটাই আমার আগেকার স্বপ্নের ব্যাখ্য আমার রব এটা সত্যে পরিণত করেছেন এবং তিনি আমাকে কারাগার থেকে মুক্ত করেন এবং শয়তান আমার ও আমার ভাইদের সম্পর্ক নষ্ট করার পরও আপনাদেরকে মরু অঞ্চল হতে এখানে এনে দিয়ে আমার প্রতি অনুগ্রহ করেছেন। আমার রব যা ইচ্ছে তা নিপুণতার সাথে করেন। তিনি তো সর্বজ্ঞ, প্রজ্ঞাময়。\n\n"
                            + "<b>[সূরা ইউসুফ – ১০০]</b>",
                    "Following their emotional reunion, Prophet Yaqub (AS) and his entire family settled permanently in Egypt upon divine guidance, marking the dawn of the Bani Isra'il era in Egypt.\n\n"
                            + "<b>Fulfillment of the Childhood Vision:</b>\n\n"
                            + "وَرَفَعَ أَبَوَيْهِ عَلَى الْعَرْشِ وَخَرُّوا لَهُ سُجَّدًا ۖ وَقَالَ يَا أَبَتِ هَٰذَا تَأْوِيلُ رُؤْيَايَ مِن قَبْلُ قَدْ جَعَلَهَا رَبِّي حَقًّا\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And he raised his parents upon the throne, and they bowed to him in prostration. And he said, 'O my father, this is the explanation of my vision of before. My Lord has made it an actuality.'\"\n\n"
                            + "<b>[Surah Yusuf 12:100]</b>\n\n"
                            + "In Egypt, Prophet Yaqub (AS) and his family dwelled in honor, peace, and abundance in the fertile region of Goshen."
            ));

            // 9. কবরস্থান ও ওফাত
            list.add(new ProphetOverviewTopicItem(
                    9,
                    "কবরস্থান ও ওফাত",
                    "Resting Place and Passing",
                    "হযরত ইয়াকুব (আ.) দীর্ঘ ও বরকতময় জীবন অতিবাহিত করার পর আল্লাহর আদেশে ইন্তেকাল করেন। তাঁর মৃত্যু ও কবর সম্পর্কিত তথ্য ইসলামী ইতিহাস, তাফসির ইবন কাসির ও তাবারির মতো প্রাচীন উৎসে বিস্তারিতভাবে পাওয়া যায়। নিচে তাঁর ওফাত ও কবরস্থান সম্পর্কে নির্ভরযোগ্য বিবরণ দেওয়া হলো。\n\n"
                            + "<b>ওফাত:</b>\n\n"
                            + "হযরত ইয়াকুব (আ.) মিশরে তাঁর প্রিয় পুত্র ইউসুফ (আলাইহিস সালাম) এর সঙ্গে পুনর্মিলনের পর বহু বছর শান্তি ও স্বস্তির জীবন কাটান। ইসলামি ঐতিহাসিক সূত্র অনুযায়ী, তিনি প্রায় ১৪৭ বছর বয়সে ইন্তেকাল করেন। তাঁর মৃত্যু ছিল প্রশান্ত ও পরিপূর্ণ ঈমানের অবস্থায়। মৃত্যুর সময় তিনি তাঁর সন্তানদের একত্রিত করে শেষ উপদেশ দেন, যা কুরআনে বর্ণিত হয়েছে,\n\n"
                            + "أَمْ كُنتُمْ شُهَدَاءَ إِذْ حَضَرَ يَعْقُوبَ الْمَوْتُ إِذْ قَالَ لِبَنِيهِ مَا تَعْبُدُونَ مِن بَعْدِي ۖ قَالُوا نَعْبُدُ إِلَٰهَكَ وَإِلَٰهَ آبَائِكَ إِبْرَاهِيمَ وَإِسْمَاعِيلَ وَإِسْحَاقَ إِلَٰهًا وَاحِدًا وَنَحْنُ لَهُ مُسْلِمُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা কি উপস্থিত ছিলে যখন ইয়াকুবের মৃত্যু উপস্থিত হলো? তখন তিনি তাঁর সন্তানদের বললেন, ‘তোমরা আমার পরে কাকে উপাসনা করবে?’ তারা বলল, ‘আমরা উপাসনা করব তোমার উপাস্যকে, তোমার পিতা ইব্রাহিম, ইসমাঈল ও ইসহাকের উপাস্যকে একমাত্র আল্লাহকে, এবং আমরা তাঁরই প্রতি আত্মসমর্পিত。\n\n"
                            + "<b>[সূরা আল-বাকারা – ১৩৩]</b>\n\n"
                            + "<b>কবরস্থান:</b>\n\n"
                            + "তাফসির ও ইতিহাস অনুযায়ী, হযরত ইয়াকুব (আ.) এর কবরস্থানের স্থান হলো ফিলিস্তিনের হেবরন শহরে অবস্থিত মাকপেলা গুহা বা মাকবারা-ই-ইবরাহিম। এই স্থানেই সমাহিত আছেন তাঁর পিতা ইসহাক (আ.), দাদা ইব্রাহিম (আ.), দাদী সারাহ (আ.), এবং মা রিবকাহ (আ.)। যদিও ইয়াকুব (আ.) এর মৃত্যু মিশরে হয়, তিনি তাঁর সন্তানদের (শেষ ইচ্ছা) করে যান যেন তাঁকে তাঁর পিতৃভূমি ফিলিস্তিনে দাফন করা হয়। ইউসুফ (আ.) সেই অনুযায়ী তাঁকে মর্যাদার সঙ্গে ফিলিস্তিনে নিয়ে গিয়ে সমাহিত করেন।",
                    "Prophet Yaqub (AS) lived a long, fruitful, and blessed life, passing away peacefully in Egypt at approximately 147 years of age.\n\n"
                            + "<b>His Final Testament:</b>\n\n"
                            + "أَمْ كُنتُمْ شُهَدَاءَ إِذْ حَضَرَ يَعْقُوبَ الْمَوْتُ إِذْ قَالَ لِبَنِيهِ مَا تَعْبُدُونَ مِن بَعْدِي ۖ قَالُوا نَعْبُدُ إِلَٰهَكَ وَإِلَٰهَ آبَائِكَ إِبْرَاهِيمَ وَإِسْمَاعِيلَ وَإِسْحَاقَ إِلَٰهًا وَاحِدًا وَنَحْنُ لَهُ مُسْلِمُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"Or were you witnesses when death approached Jacob, when he said to his sons, 'What will you worship after me?' They said, 'We will worship your God and the God of your fathers, Abraham and Ishmael and Isaac - one God. And we are Muslims [in submission] to Him.'\"\n\n"
                            + "<b>[Surah Al-Baqarah 2:133]</b>\n\n"
                            + "<b>Resting Place:</b>\n\n"
                            + "According to classical Islamic history and his final will, Prophet Yusuf (AS) transported his father's body with royal dignity back to Canaan (Palestine), laying him to rest in the <b>Cave of Machpelah</b> in Hebron (Al-Khalil) alongside his father Ishaq (AS) and grandfather Ibrahim (AS)."
            ));
        } else if (prophetId == 11) {
            // হযরত ইউসুফ (আ:) - All 13 Chapters / Topics 100% Verbatim from User Request & Screenshots

            // 1. জন্ম ও বংশপরিচয়
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "জন্ম ও বংশপরিচয়",
                    "Birth and Lineage",
                    "হযরত ইউসুফ (আ.) ছিলেন আল্লাহর এক মহান নবী, যিনি সৌন্দর্য, চরিত্র, প্রজ্ঞা ও ধৈর্যের জন্য বিখ্যাত। তাঁর জীবন কাহিনি কুরআনের সূরা ইউসুফে বিস্তারিতভাবে বর্ণিত হয়েছে, যেখানে আল্লাহ একে বলেছেন,\n\n"
                            + "“أَحْسَنَ الْقَصَصِ” \n\n"
                            + "অর্থাৎ “সর্বোত্তম কাহিনি”। নিচে তাঁর জন্ম ও বংশপরিচয় কুরআন ও ইসলামী ইতিহাস অনুযায়ী বর্ণনা করা হলো \n\n"
                            + "<b>জন্মস্থান ও সময়:</b>\n\n"
                            + "হযরত ইউসুফ (আ.) জন্মগ্রহণ করেন ফিলিস্তিনের কনআন অঞ্চলে, নবীপরিবারে। তাঁর জন্ম হয়েছিল সেই যুগে যখন আল্লাহর নবী হযরত ইয়াকুব (আ.) তাঁর পরিবার নিয়ে কনআনে বসবাস করছিলেন। ইসলামী ঐতিহাসিক সূত্র (ইবন কাসির, তাবারি প্রভৃতি) অনুযায়ী, ইউসুফ (আ.) এর জন্ম হয় প্রায় ১৮০০ খ্রিস্টপূর্বাব্দে।\n\n"
                            + "<b>বংশপরিচয়:</b>\n\n"
                            + "হযরত ইউসুফ (আ.) নবীপরম্পরার এক বিশুদ্ধ বংশে জন্ম নিয়েছিলেন তাঁর চার প্রজন্মই ছিলেন নবী\n\n"
                            + "ইব্রাহিম (আলাইহিস সালাম) → ইসহাক (আলাইহিস সালাম) → ইয়াকুব (আলাইহিস সালাম) → ইউসুফ (আলাইহিস সালাম)\n\n"
                            + "অর্থাৎ, ইউসুফ (আ.) ছিলেন হযরত ইব্রাহিম (আ.) এর প্রপৌত্র এবং ইয়াকুব (আ.) এর পুত্র। কুরআনে এ বংশপরিচয় স্পষ্টভাবে উল্লেখ আছে,\n\n"
                            + "وَاتَّبَعْتُ مِلَّةَ آبَائِي إِبْرَاهِيمَ وَإِسْحَاقَ وَيَعْقُوبَ ۚ مَا كَانَ لَنَا أَن نُّشْرِكَ بِاللَّهِ مِن شَيْءٍ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আমি অনুসরণ করেছি আমার পিতৃপুরুষ ইব্রাহিম, ইসহাক ও ইয়াকুবের ধর্মপথ। আমাদের জন্য আল্লাহর সঙ্গে কাউকে শরিক করা উচিত নয়।\n\n"
                            + "<b>[সূরা ইউসুফ – ৩৮]</b>\n\n"
                            + "<b>মাতা ও পরিবার:</b>\n\n"
                            + "ইউসুফ (আ.) এর মাতা ছিলেন রাহেল, যিনি হযরত ইয়াকুব (আ.) এর দ্বিতীয় স্ত্রী ছিলেন।\n\n"
                            + "তাঁর এক আপন ভাই ছিলেন বিনইয়ামিন তিনিও রাহেল (আ.)-এর সন্তান। বাকি দশ ভাই ছিলেন ইয়াকুব (আ.) এর অন্যান্য স্ত্রীদের সন্তান, যারা পরে ইউসুফ (আ.) এর জীবনে গুরুত্বপূর্ণ ভূমিকা পালন করেন (বিশেষ করে তাঁর কূপে ফেলে দেওয়ার ঘটনায়) ।",
                    "Prophet Yusuf (peace be upon him) was one of the noble Prophets of Allah, renowned for his unmatched beauty, sublime character, wisdom, and profound patience. Allah describes his account in the Holy Quran as <b>\"Ahsan al-Qasas\" (أَحْسَنَ الْقَصَصِ)</b>—the most beautiful of stories.\n\n"
                            + "<b>Birthplace and Era:</b>\n\n"
                            + "Prophet Yusuf (AS) was born in Canaan (Palestine) into a prophetic family around 1800 BCE during the era when his father, Prophet Yaqub (AS), resided in Canaan.\n\n"
                            + "<b>Prophetic Lineage:</b>\n\n"
                            + "Prophet Yusuf (AS) belonged to an unbroken four-generation chain of noble Prophets:\n"
                            + "Ibrahim (AS) → Ishaq (AS) → Yaqub (AS) → Yusuf (AS)\n\n"
                            + "وَاتَّبَعْتُ مِلَّةَ آبَائِي إِبْرَاهِيمَ وَإِسْحَاقَ وَيَعْقُوبَ ۚ مَا كَانَ لَنَا أَن نُّشْرِكَ بِاللَّهِ مِن شَيْءٍ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And I have followed the religion of my fathers, Abraham, Isaac, and Jacob. And it was not for us to associate anything with Allah...\"\n\n"
                            + "<b>[Surah Yusuf 12:38]</b>\n\n"
                            + "<b>Mother and Family:</b>\n\n"
                            + "His mother was Rachel (Rahil), the beloved wife of Yaqub (AS). He had a full brother, Benjamin (Binyamin), and ten half-brothers from Yaqub's other wives."
            ));

            // 2. পিতা ইয়াকুব (আ.) এর স্নেহ
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "পিতা ইয়াকুব (আ.) এর স্নেহ",
                    "Father Yaqub's Affection",
                    "হযরত ইয়াকুব (আ.) এর তাঁর পুত্র হযরত ইউসুফ (আ.) এর প্রতি ভালোবাসা ও স্নেহ ছিল গভীর, পবিত্র ও আল্লাহভীতিতে পরিপূর্ণ। এটি ছিল সাধারণ পিতৃস্নেহ নয়, বরং নবুওয়তের প্রজ্ঞা ও আল্লাহর নির্দেশনাজাত এক ভালোবাসা, যার মধ্যে লুকিয়ে ছিল ভবিষ্যতের এক মহৎ পরিকল্পনা। নিচে তাঁর এই ভালোবাসার উৎস, প্রকাশ ও ইসলামী দৃষ্টিতে তার তাৎপর্য ব্যাখ্যা করা হলো,\n\n"
                            + "<b>নবুওয়তের আলোতে দেখা ভালোবাসা:</b>\n\n"
                            + "হযরত ইয়াকুব (আ.) তাঁর সন্তানদের মধ্যে ইউসুফ (আ.) ও তাঁর ভাই বিনইয়ামিন (আ.) কে বিশেষভাবে স্নেহ করতেন। কারণ তিনি তাঁদের মধ্যে নবুওয়তের আলোর প্রতিফলন দেখতে পেয়েছিলেন। ইউসুফ (আ.) ছিলেন সৌন্দর্য, বুদ্ধি, শিষ্টাচার ও ঈমানের প্রতীক এই বৈশিষ্ট্যগুলো দেখে ইয়াকুব (আ.) বুঝেছিলেন যে আল্লাহ তাঁকে কোনো বিশেষ দায়িত্বের জন্য বেছে নিয়েছেন। এই ভালোবাসা ছিল আল্লাহর পরিকল্পনার প্রতি উপলব্ধি ও নবুওয়তের প্রজ্ঞা থেকে উদ্ভূত, ব্যক্তিগত পক্ষপাত নয়। ইউসুফ (আ.) এর প্রতি পিতার স্নেহ কুরআনের সূরা ইউসুফে স্পষ্টভাবে প্রতিফলিত হয়েছে।\n\n"
                            + "إِذْ قَالَ يُوسُفُ لِأَبِيهِ يَا أَبَتِ إِنِّي رَأَيْتُ أَحَدَ عَشَرَ كَوْكَبًا وَالشَّمْسَ وَالْقَمَرَ رَأَيْتُهُمْ لِي سَاجِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন ইউসুফ তাঁর পিতাকে বলল, ‘হে আমার পিতা! আমি স্বপ্নে দেখেছি এগারোটি তারা, সূর্য ও চাঁদ আমি দেখেছি তারা সবাই আমাকে সেজদা করছে।\n\n"
                            + "<b>[সূরা ইউসুফ – ৪]</b>\n\n"
                            + "এর উত্তরে ইয়াকুব (আ.) অত্যন্ত স্নেহভরে তাঁকে সতর্ক করে দেন\n\n"
                            + "قَالَ يَا بُنَيَّ لَا تَقْصُصْ رُؤْيَاكَ عَلَىٰ إِخْوَتِكَ فَيَكِيدُوا لَكَ كَيْدًا ۖ إِنَّ الشَّيْطَانَ لِلْإِنسَانِ عَدُوٌّ مُّبِينٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি বললেন, ‘হে আমার ছেলে, তোমার এই স্বপ্ন ভাইদের কাছে বলো না, তারা যেন তোমার বিরুদ্ধে কোনো ষড়যন্ত্র না করে; নিশ্চয়ই শয়তান মানুষের প্রকাশ্য শত্রু।\n\n"
                            + "<b>[সূরা ইউসুফ – ৫]</b>\n\n"
                            + "এই কথায় পিতার স্নেহ, প্রজ্ঞা ও দূরদৃষ্টি সব একত্রে প্রকাশ পেয়েছে। তিনি শুধু পুত্রকে ভালোবাসেননি, বরং ভবিষ্যতের বিপদ থেকেও রক্ষা করার জন্য সতর্ক করেছিলেন।\n\n"
                            + "<b>পুত্রের বিচ্ছেদেও ধৈর্যের ভালোবাসা:</b>\n\n"
                            + "যখন ভাইয়েরা ইউসুফ (আ.) কে কূপে ফেলে দেয় এবং মিথ্যা বলে পিতাকে জানায় যে নেকড়ে তাঁকে খেয়ে ফেলেছে, তখন ইয়াকুব (আ.) গভীর শোকে ভেঙে পড়লেও আল্লাহর প্রতি আস্থা হারাননি। তিনি বলেন,\n\n"
                            + "فَصَبْرٌ جَمِيلٌ ۖ وَاللَّهُ الْمُسْتَعَانُ عَلَىٰ مَا تَصِفُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আমি সুন্দর ধৈর্য ধারণ করব; তোমরা যা বলছো, তার ব্যাপারে আল্লাহই সাহায্যকারী।\n\n"
                            + "<b>[সূরা ইউসুফ – ১৮]</b>\n\n"
                            + "<b>দীর্ঘ বিচ্ছেদের পরও অবিরাম দোয়া:</b>\n\n"
                            + "বহু বছর ইউসুফ (আ.) কে না দেখে তিনি দুঃখে কেঁদে কেঁদে দৃষ্টিশক্তি হারান, কিন্তু তাঁর ভালোবাসা কখনো অভিযোগে পরিণত হয়নি। তিনি আল্লাহর কাছে আরজি করেন, \n\n"
                            + "إِنَّمَا أَشْكُو بَثِّي وَحُزْنِي إِلَى اللَّهِ وَأَعْلَمُ مِنَ اللَّهِ مَا لَا تَعْلَمُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আমি তো আমার দুঃখ ও কষ্টের কথা শুধু আল্লাহকেই জানাই, এবং আমি আল্লাহর কাছ থেকে এমন কিছু জানি যা তোমরা জানো না।\n\n"
                            + "<b>[সূরা ইউসুফ – ৮৬]</b>",
                    "The deep affection Prophet Yaqub (AS) held for Yusuf (AS) was rooted in divine prophetic wisdom and foresight, knowing that Allah had chosen Yusuf for a monumental mission.\n\n"
                            + "<b>Affection in Light of Prophethood:</b>\n\n"
                            + "إِذْ قَالَ يُوسُفُ لِأَبِيهِ يَا أَبَتِ إِنِّي رَأَيْتُ أَحَدَ عَشَرَ كَوْكَبًا وَالشَّمْسَ وَالْقَمَرَ رَأَيْتُهُمْ لِي سَاجِدِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"When Joseph said to his father, 'O my father, indeed I have seen eleven stars and the sun and the moon; I saw them prostrating to me.'\"\n\n"
                            + "<b>[Surah Yusuf 12:4]</b>\n\n"
                            + "Yaqub (AS) wisely cautioned him:\n\n"
                            + "قَالَ يَا بُنَيَّ لَا تَقْصُصْ رُؤْيَاكَ عَلَىٰ إِخْوَتِكَ فَيَكِيدُوا لَكَ كَيْدًا ۖ إِنَّ الشَّيْطَانَ لِلْإِنسَانِ عَدُوٌّ مُّبِينٌ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"He said, 'O my son, do not relate your vision to your brothers or they will contrive against you a plan. Indeed Satan, to man, is a manifest enemy.'\"\n\n"
                            + "<b>[Surah Yusuf 12:5]</b>\n\n"
                            + "<b>Enduring Patience in Separation:</b>\n\n"
                            + "فَصَبْرٌ جَمِيلٌ ۖ وَاللَّهُ الْمُسْتَعَانُ عَلَىٰ مَا تَصِفُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"So patience is most fitting. And Allah is the one sought for help against that which you describe.\"\n\n"
                            + "<b>[Surah Yusuf 12:18]</b>\n\n"
                            + "إِنَّمَا أَشْكُو بَثِّي وَحُزْنِي إِلَى اللَّهِ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"I only complain of my suffering and my grief to Allah...\"\n\n"
                            + "<b>[Surah Yusuf 12:86]</b>"
            ));

            // 3. স্বপ্ন দেখা ও ব্যাখ্যা চাওয়া
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "স্বপ্ন দেখা ও ব্যাখ্যা চাওয়া",
                    "The Dream and Seeking Its Interpretation",
                    "হযরত ইউসুফ (আ.) এর জীবনের সূচনা হয়েছিল এক অলৌকিক স্বপ্ন দিয়ে এমন এক স্বপ্ন যা ছিল তাঁর ভবিষ্যৎ নবুওয়তের ইঙ্গিত। আর সেই স্বপ্নের ব্যাখ্যা তিনি প্রথমে খুঁজেছিলেন তাঁর পিতা হযরত ইয়াকুব (আ.) এর কাছেই। এই ঘটনা শুধু পিতা পুত্রের ঘনিষ্ঠতারই নয়, বরং নবুওয়তের উত্তরাধিকার ও আল্লাহর পরিকল্পনার সূক্ষ্ম ইঙ্গিতও বহন করে। নিচে ঘটনাটি কুরআনের আলোকে বিশদভাবে তুলে ধরা হলো, \n\n"
                            + "<b>ইউসুফ (আ.) এর স্বপ্ন দেখা:</b>\n\n"
                            + "এক রাতে অল্প বয়সে ইউসুফ (আ.) একটি আশ্চর্য স্বপ্ন দেখেন। সেই স্বপ্ন ছিল এমন,\n\n"
                            + "إِذْ قَالَ يُوسُفُ لِأَبِيهِ يَا أَبَتِ إِنِّي رَأَيْتُ أَحَدَ عَشَرَ كَوْكَبًا وَالشَّمْسَ وَالْقَمَرَ رَأَيْتُهُمْ لِي سَاجِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন ইউসুফ তাঁর পিতাকে বলল, ‘হে আমার পিতা! আমি স্বপ্নে দেখেছি এগারোটি তারা, সূর্য ও চাঁদ আমি দেখেছি তারা সবাই আমাকে সেজদা করছে।\n\n"
                            + "<b>[সূরা ইউসুফ – ৪]</b>\n\n"
                            + "এই স্বপ্নটি ছিল এক নবীর শৈশবের প্রথম ওহীসদৃশ ইঙ্গিত, যেখানে এগারো ভাই (তারা), সূর্য (পিতা ইয়াকুব) ও চাঁদ (মাতা) ইউসুফের সামনে সেজদারত অবস্থায় দেখা যায়। এটি ছিল ভবিষ্যতের সম্মান, নবুওয়ত ও পরিবারের পুনর্মিলনের এক ভবিষ্যদ্বাণী।\n\n"
                            + "<b>পিতার কাছে স্বপ্নের ব্যাখ্যা চাওয়া:</b>\n\n"
                            + "ইউসুফ (আ.) ছোটবেলা থেকেই তাঁর পিতা ইয়াকুব (আ.) কে অত্যন্ত শ্রদ্ধা করতেন। তাই এই স্বপ্নের রহস্য তিনি প্রথমেই পিতার কাছে গিয়ে জানান ও তার ব্যাখ্যা জানতে চান। ইয়াকুব (আ.) এর উত্তরে গভীর প্রজ্ঞা ও পিতৃস্নেহ প্রকাশ পায়। তিনি বুঝতে পারেন এটি কোনো সাধারণ স্বপ্ন নয় বরং আল্লাহর পক্ষ থেকে এক বিশেষ বার্তা।\n\n"
                            + "وَكَذَٰلِكَ يَجْتَبِيكَ رَبُّكَ وَيُعَلِّمُكَ مِن تَأْوِيلِ الْأَحَادِيثِ وَيُتِمُّ نِعْمَتَهُ عَلَيْكَ وَعَلَىٰ آلِ يَعْقُوبَ كَمَا أَتَمَّهَا عَلَىٰ أَبَوَيْكَ مِن قَبْلُ إِبْرَاهِيمَ وَإِسْحَاقَ ۚ إِنَّ رَبَّكَ عَلِيمٌ حَكِيمٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "এইভাবেই তোমার প্রতিপালক তোমাকে নির্বাচিত করবেন, তোমাকে স্বপ্নের ব্যাখ্যা শিখাবেন, এবং তোমার ও ইয়াকুব পরিবারের ওপর তাঁর অনুগ্রহ সম্পূর্ণ করবেন যেমন তিনি তোমার পূর্বপুরুষ ইব্রাহিম ও ইসহাকের ওপর অনুগ্রহ করেছিলেন। নিশ্চয়ই তোমার প্রতিপালক সর্বজ্ঞ ও প্রজ্ঞাময়।\n\n"
                            + "<b>[সূরা ইউসুফ – ৬]</b>",
                    "The momentous journey of Prophet Yusuf (AS) commenced with a divine vision foretelling his prophethood and exalted stature.\n\n"
                            + "<b>The Vision:</b>\n\n"
                            + "إِذْ قَالَ يُوسُفُ لِأَبِيهِ يَا أَبَتِ إِنِّي رَأَيْتُ أَحَدَ عَشَرَ كَوْكَبًا وَالشَّمْسَ وَالْقَمَرَ رَأَيْتُهُمْ لِي سَاجِدِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"When Joseph said to his father, 'O my father, indeed I have seen eleven stars and the sun and the moon; I saw them prostrating to me.'\"\n\n"
                            + "<b>[Surah Yusuf 12:4]</b>\n\n"
                            + "<b>Interpretation by Yaqub (AS):</b>\n\n"
                            + "وَكَذَٰلِكَ يَجْتَبِيكَ رَبُّكَ وَيُعَلِّمُكَ مِن تَأْوِيلِ الْأَحَادِيثِ وَيُتِمُّ نِعْمَتَهُ عَلَيْكَ وَعَلَىٰ آلِ يَعْقُوبَ كَمَا أَتَمَّهَا عَلَىٰ أَبَوَيْكَ مِن قَبْلُ إِبْرَاهِيمَ وَإِسْحَاقَ ۚ إِنَّ رَبَّكَ عَلِيمٌ حَكِيمٌ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And thus will your Lord choose you and teach you the interpretation of narratives and complete His favor upon you and upon the family of Jacob, as He completed it upon your fathers before, Abraham and Isaac. Indeed, your Lord is Knowing and Wise.\"\n\n"
                            + "<b>[Surah Yusuf 12:6]</b>"
            ));

            // 4. ভাইদের ঈর্ষা ও কূপে নিক্ষেপ
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "ভাইদের ঈর্ষা ও কূপে নিক্ষেপ",
                    "Jealousy of the Brothers and Casting into the Well",
                    "হযরত ইউসুফ (আ.) ও তাঁর ভাইদের কাহিনি মানব ইতিহাসের এক শিক্ষণীয় অধ্যায়, যেখানে হিংসা, ষড়যন্ত্র, ধৈর্য ও আল্লাহর পরিকল্পনার এক গভীর বার্তা নিহিত আছে। কুরআনের সূরা ইউসুফ এই ঘটনাটিকে অত্যন্ত হৃদয়স্পর্শী ও শিক্ষণীয়ভাবে তুলে ধরেছে। নিচে তাঁর ভাইদের ঈর্ষা, কূপে নিক্ষেপ, এবং আল্লাহর পরিকল্পনা সম্পর্কে বিস্তারিতভাবে তুলে ধরা হলো \n\n"
                            + "<b>ভাইদের ঈর্ষা ও হিংসা:</b>\n\n"
                            + "হযরত ইউসুফ (আ.) ছিলেন হযরত ইয়াকুব (আ.) এর প্রিয় সন্তান। তিনি ছিলেন সুন্দর, বুদ্ধিমান, ও নেকচারিত্র্যের অধিকারী। পিতা ইয়াকুব (আ.) তাঁর মধ্যে নবুওয়তের আলো দেখতে পেতেন এবং এজন্যই তাঁকে গভীর স্নেহ করতেন। কিন্তু তাঁর ভাইরা এই ভালোবাসাকে ভুলভাবে বুঝে ফেলে এবং মনে করে পিতা তাঁদের অবহেলা করছেন। ফলে তাঁদের মনে ইউসুফ (আ.) এর প্রতি প্রবল হিংসা জন্ম নেয়। কুরআনে বলা হয়েছে,\n\n"
                            + "إِذْ قَالُوا لَيُوسُفُ وَأَخُوهُ أَحَبُّ إِلَىٰ أَبِينَا مِنَّا وَنَحْنُ عُصْبَةٌ ۖ إِنَّ أَبَانَا لَفِي ضَلَالٍ مُّبِينٍ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন তারা বলল, ‘ইউসুফ ও তাঁর ভাই আমাদের পিতার কাছে আমাদের চেয়ে বেশি প্রিয়, অথচ আমরা শক্তিশালী একটি দল। নিশ্চয়ই আমাদের পিতা স্পষ্ট ভ্রান্তিতে আছেন।\n\n"
                            + "<b>[সূরা ইউসুফ – ৮]</b>\n\n"
                            + "এই আয়াতে দেখা যায়, হিংসা কীভাবে মানুষের চিন্তাকে বিকৃত করে দেয়। তাঁরা বুঝতে পারেনি যে, পিতার ভালোবাসা কোনো পক্ষপাত নয়, বরং আল্লাহর প্রজ্ঞায় নির্ধারিত।\n\n"
                            + "<b>ষড়যন্ত্র ও পরিকল্পনা:</b>\n\n"
                            + "হিংসা তাঁদের এমন পর্যায়ে নিয়ে যায় যে তাঁরা সিদ্ধান্ত নেয় ইউসুফ (আ.) কে সরিয়ে ফেলবে, যাতে পিতার ভালোবাসা কেবল তাঁদের দিকে ফিরে আসে।\n\n"
                            + "اقْتُلُوا يُوسُفَ أَوِ اطْرَحُوهُ أَرْضًا يَخْلُ لَكُمْ وَجْهُ أَبِيكُمْ وَتَكُونُوا مِن بَعْدِهِ قَوْمًا صَالِحِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা ইউসুফকে হত্যা করো অথবা কোথাও দূরে ফেলে দাও, যাতে তোমাদের পিতার ভালোবাসা শুধু তোমাদের জন্য থাকে; তারপর তোমরা সৎ লোক হয়ে যাবে।\n\n"
                            + "<b>[সূরা ইউসুফ – ৯]</b>\n\n"
                            + "তাঁরা নিজের অন্যায়কে যৌক্তিক দেখানোর চেষ্টা করেছিল‘প্রথমে খারাপ কাজ করব, পরে ভালো হয়ে যাব।’ কিন্তু আল্লাহর পরিকল্পনা তাঁদের অজানাই ছিল।\n\n"
                            + "<b>পিতার কাছ থেকে অনুমতি চাওয়া:</b>\n\n"
                            + "তাঁরা একদিন পিতার কাছে এসে বলে,\n\n"
                            + "يَا أَبَانَا مَا لَكَ لَا تَأْمَنَّا عَلَىٰ يُوسُفَ وَإِنَّا لَهُ لَنَاصِحُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমাদের পিতা! আপনি ইউসুফকে আমাদের সঙ্গে পাঠাতে ভয় পান কেন? আমরা তো তাঁর কল্যাণই চাই।\n\n"
                            + "<b>[সূরা ইউসুফ – ১১]</b>\n\n"
                            + "ইয়াকুব (আ.) তাঁদের অনুরোধে শেষ পর্যন্ত সম্মতি দেন, কিন্তু পূর্ব অনুভূতি থেকে বলেন,\n\n"
                            + "إِنِّي لَيَحْزُنُنِي أَن تَذْهَبُوا بِهِ وَأَخَافُ أَن يَأْكُلَهُ الذِّئْبُ وَأَنتُمْ عَنْهُ غَافِلُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তোমরা তাঁকে নিয়ে যাবে, এটা আমাকে দুঃখিত করে, আর আমি আশঙ্কা করি যে, তোমরা অসাবধান হলে নেকড়ে তাঁকে খেয়ে ফেলবে।\n\n"
                            + "<b>[সূরা ইউসুফ – ১৩]</b>\n\n"
                            + "<b>ইউসুফ (আ.) কে কূপে নিক্ষেপ:</b>\n\n"
                            + "যখন তাঁরা ইউসুফ (আ.) কে মাঠে নিয়ে গেল, তখন ষড়যন্ত্র কার্যকর করে তাঁকে গভীর এক অন্ধকার কূপে ফেলে দেয়। কুরআনে বলা হয়েছে,\n\n"
                            + "فَلَمَّا ذَهَبُوا بِهِ وَأَجْمَعُوا أَنْ يَجْعَلُوهُ فِي غَيَابَةِ الْجُبِّ وَأَوْحَيْنَا إِلَيْهِ لَتُنَبِّئَنَّهُمْ بِأَمْرِهِمْ هَٰذَا وَهُمْ لَا يَشْعُرُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন তারা তাঁকে নিয়ে গেল এবং একমত হলো তাঁকে কূপে নিক্ষেপ করবে, তখন আমি ইউসুফকে প্রত্যাদেশ দিলাম, ‘একদিন তুমি তাদের এই কাজের কথা স্মরণ করাবে, অথচ তারা তা বুঝবে না।\n\n"
                            + "<b>[সূরা ইউসুফ – ১৫]</b>\n\n"
                            + "এই মুহূর্তে আল্লাহ তাঁকে সান্ত্বনা দেন এবং নবুওয়তের ভবিষ্যৎ বার্তা প্রদান করেন। মানুষ তাঁকে কূপে ফেলে দিয়েছিল, কিন্তু আল্লাহর পরিকল্পনা তাঁকে রাজ্যের শীর্ষে নিয়ে যাবে।\n\n"
                            + "<b>পিতার কাছে মিথ্যা সংবাদ:</b>\n\n"
                            + "তাঁরা রাতে ফিরে এসে কান্না করে বলে,\n\n"
                            + "إِنَّا ذَهَبْنَا نَسْتَبِقُ وَتَرَكْنَا يُوسُفَ عِندَ مَتَاعِنَا فَأَكَلَهُ الذِّئْبُ ۖ وَمَا أَنتَ بِمُؤْمِنٍ لَّنَا وَلَوْ كُنَّا صَادِقِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আমরা দৌড় প্রতিযোগিতা করতে গিয়েছিলাম, ইউসুফকে জিনিসপত্রের কাছে রেখে এসেছিলাম, তখন নেকড়ে তাঁকে খেয়ে ফেলেছে; আপনি আমাদের বিশ্বাস করবেন না, যদিও আমরা সত্য বলছি।\n\n"
                            + "<b>[সূরা ইউসুফ – ১৭]</b>\n\n"
                            + "তাঁরা একটি ছাগল হত্যা করে রক্ত ইউসুফের জামায় লাগিয়ে প্রমাণ হিসেবে দেখায়।\n\n"
                            + "কিন্তু ইয়াকুব (আ.) বুঝে ফেলেন যে এটি মিথ্যা,\n\n"
                            + "قَالَ بَلْ سَوَّلَتْ لَكُمْ أَنفُسُكُمْ أَمْرًا ۖ فَصَبْرٌ جَمِيلٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি বললেন, ‘তোমাদের অন্তর তোমাদেরকে এক মন্দ কাজ সহজ করে দেখিয়েছে; সুতরাং আমি সুন্দর ধৈর্য ধারণ করব।\n\n"
                            + "<b>[সূরা ইউসুফ – ১৮]</b>",
                    "Consumed by envy, the brothers of Yusuf plotted against him to monopolize their father's affection.\n\n"
                            + "<b>The Envy & The Plot:</b>\n\n"
                            + "إِذْ قَالُوا لَيُوسُفُ وَأَخُوهُ أَحَبُّ إِلَىٰ أَبِينَا مِنَّا وَنَحْنُ عُصْبَةٌ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"When they said, 'Joseph and his brother are more beloved to our father than we, while we are a clan. Indeed, our father is in clear error.'\"\n\n"
                            + "<b>[Surah Yusuf 12:8]</b>\n\n"
                            + "اقْتُلُوا يُوسُفَ أَوِ اطْرَحُوهُ أَرْضًا يَخْلُ لَكُمْ وَجْهُ أَبِيكُمْ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"Kill Joseph or cast him out to [another] land; the countenance of your father will [then] be only for you...\"\n\n"
                            + "<b>[Surah Yusuf 12:9]</b>\n\n"
                            + "<b>Casting into the Well & Divine Reassurance:</b>\n\n"
                            + "فَلَمَّا ذَهَبُوا بِهِ وَأَجْمَعُوا أَنْ يَجْعَلُوهُ فِي غَيَابَةِ الْجُبِّ وَأَوْحَيْنَا إِلَيْهِ لَتُنَبِّئَنَّهُمْ بِأَمْرِهِمْ هَٰذَا وَهُمْ لَا يَشْعُرُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"So when they took him and agreed to put him into the bottom of the well... But We inspired to him, 'You will surely inform them [someday] of this affair of theirs while they do not perceive [your identity].'\"\n\n"
                            + "<b>[Surah Yusuf 12:15]</b>\n\n"
                            + "<b>The False Tale:</b>\n\n"
                            + "قَالَ بَلْ سَوَّلَتْ لَكُمْ أَنفُسُكُمْ أَمْرًا ۖ فَصَبْرٌ جَمِيلٌ ۖ وَاللَّهُ الْمُسْتَعَانُ عَلَىٰ مَا تَصِفُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"He said, 'Rather, your souls have enticed you to something, so patience is most fitting. And Allah is the one sought for help against that which you describe.'\"\n\n"
                            + "<b>[Surah Yusuf 12:18]</b>"
            ));

            // 5. কাফেলার হাতে বিক্রি
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "কাফেলার হাতে বিক্রি",
                    "Sold to the Caravan",
                    "হযরত ইউসুফ (আ.) কে কূপে ফেলে দেওয়ার পর তাঁর ভাইয়েরা ভেবেছিল তিনি চিরতরে হারিয়ে গেছেন। কিন্তু আল্লাহ তায়ালা তাঁর জন্য এক ভিন্ন পরিকল্পনা রেখেছিলেন যাতে সেই কূপ থেকেই শুরু হয় তাঁর নবুওয়তের পথে যাত্রা। এই পর্বটি হলো কাফেলার (যাত্রীদল) হাতে ইউসুফ (আ.) বিক্রি হয়ে যাওয়ার ঘটনা, যা কুরআনের ভাষায় এক গভীর শিক্ষা বহন করে।\n\n"
                            + "<b>কূপ থেকে উদ্ধার:</b>\n\n"
                            + "কয়েকদিন পর এক কাফেলা (বাণিজ্যিক যাত্রীদল) সেই পথে যাচ্ছিল। তারা বিশ্রামের জন্য কূপের কাছে থামে এবং পানি তোলার জন্য একজন পানিওয়ালাকে পাঠায়।\n\n"
                            + "وَجَاءَتْ سَيَّارَةٌ فَأَرْسَلُوا وَارِدَهُمْ فَأَدْلَىٰ دَلْوَهُ قَالَ يَا بُشْرَىٰ هَٰذَا غُلَامٌ ۚ وَأَسَرُّوهُ بِضَاعَةً ۚ وَاللَّهُ عَلِيمٌ بِمَا يَعْمَلُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "যখন এক কাফেলা এসে তাদের পানিওয়ালাকে পাঠালো, সে বালতি নামাল। তখন বলল, ‘সুসংবাদ! এখানে তো একটি বালক আছে।’ তারা তাঁকে পণ্য হিসেবে গোপন করল। কিন্তু আল্লাহ তাঁদের কাজ ভালোভাবেই জানতেন।\n\n"
                            + "<b>[সূরা ইউসুফ – ১৯]</b>\n\n"
                            + "কাফেলা তাঁকে পানির পরিবর্তে ‘একজন কিশোর দাস’ হিসেবে পেয়ে অত্যন্ত আনন্দিত হয়, কিন্তু তাঁকে গোপন করে রাখে যাতে কেউ দাবি না করে। আল্লাহ জানতেন, এই ঘটনাই হবে ইউসুফ (আ.) এর জীবনের নতুন অধ্যায়ের সূচনা।\n\n"
                            + "<b>সস্তায় বিক্রি হয়ে যাওয়া:</b>\n\n"
                            + "কাফেলা ইউসুফ (আ.) কে মিশরে নিয়ে যায় এবং তাঁকে বাজারে বিক্রি করে দেয়, যদিও তাঁর মর্যাদা ছিল অপরিসীম।\n\n"
                            + "وَشَرَوْهُ بِثَمَنٍ بَخْسٍ دَرَاهِمَ مَعْدُودَةٍ وَكَانُوا فِيهِ مِنَ الزَّاهِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা তাঁকে সামান্য কিছু মুদ্রার বিনিময়ে বিক্রি করে দিল, এবং তারা তাঁর প্রতি উদাসীন ছিল।\n\n"
                            + "<b>[সূরা ইউসুফ – ২০]</b>\n\n"
                            + "<b>মিশরে বিক্রি ও নতুন জীবন:</b>\n\n"
                            + "মিশরে তাঁকে ক্রয় করেন এক সম্মানিত ব্যক্তি কুরআনে যাঁকে বলা হয়েছে “العزيز” (আজীজ)। পরবর্তীতে তাঁর গৃহেই ইউসুফ (আ.) বড় হন, জ্ঞান ও সৌন্দর্যে পরিপূর্ণ হয়ে ওঠেন।\n\n"
                            + "وَقَالَ الَّذِي اشْتَرَاهُ مِن مِّصْرَ لِامْرَأَتِهِ أَكْرِمِي مَثْوَاهُ عَسَىٰ أَن يَنফَعَنَا أَوْ نَتَّخِذَهُ وَلَدًا ۚ وَكَذَٰلِكَ مَكَّنَّا لِيُوسُفَ فِي الْأَرْضِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "মিশরের যে ব্যক্তি তাঁকে ক্রয় করেছিল, সে তার স্ত্রীকে বলল, ‘তুমি তাঁর দেখাশোনা ভালোভাবে করো; হয়তো সে আমাদের উপকারে আসবে, অথবা আমরা তাঁকে সন্তানরূপে গ্রহণ করব।’ এভাবেই আমি ইউসুফকে দেশে প্রতিষ্ঠিত করলাম।\n\n"
                            + "<b>[সূরা ইউসুফ – ২১]</b>\n\n"
                            + "এই আয়াতে আল্লাহ ঘোষণা করেছেন ইউসুফ (আ.) এর এই কষ্টই ছিল তাঁর জীবনের নতুন উত্থানের সূচনা। কূপের অন্ধকার থেকে তিনি মিশরের প্রাসাদে পৌঁছে গেলেন, এবং সেখান থেকেই শুরু হয় তাঁর নবুওয়তের প্রস্তুতি।",
                    "A passing caravan drew water from the well and found young Yusuf. They hid him as merchandise and sold him in Egypt for a meager price.\n\n"
                            + "<b>Rescue from the Well:</b>\n\n"
                            + "وَجَاءَتْ سَيَّارَةٌ فَأَرْسَلُوا وَارِدَهُمْ فَأَدْلَىٰ دَلْوَهُ قَالَ يَا بُشْرَىٰ هَٰذَا غُلَامٌ ۚ وَأَسَرُّوهُ بِضَاعَةً\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And there came a company of travelers; then they sent their water drawer, and he let down his bucket. He said, 'Good news! Here is a boy.' And they hid him as merchandise...\"\n\n"
                            + "<b>[Surah Yusuf 12:19]</b>\n\n"
                            + "<b>Sold for a Petty Price:</b>\n\n"
                            + "وَشَرَوْهُ بِثَمَنٍ بَخْسٍ دَرَاهِمَ مَعْدُودَةٍ وَكَانُوا فِيهِ مِنَ الزَّاهِدِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And they sold him for a reduced price - a few dirhams - and they were, concerning him, of those concerning-with-little-value.\"\n\n"
                            + "<b>[Surah Yusuf 12:20]</b>\n\n"
                            + "<b>Honorable Sanctuary in Egypt:</b>\n\n"
                            + "وَقَالَ الَّذِي اشْتَرَاهُ مِن مِّصْرَ لِامْرَأَتِهِ أَكْرِمِي مَثْوَاهُ عَسَىٰ أَن يَنফَعَنَا أَوْ نَتَّخِذَهُ وَلَدًا\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And the one from Egypt who bought him said to his wife, 'Make his residence comfortable. Perhaps he will benefit us, or we will adopt him as a son.' And thus We established Joseph in the land...\"\n\n"
                            + "<b>[Surah Yusuf 12:21]</b>"
            ));

            // 6. মিশরের রাজদরবারে আগমন
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "মিশরের রাজদরবারে আগমন",
                    "Arrival at the Egyptian Royal Court",
                    "কাফেলার হাতে বিক্রি হওয়ার পর হযরত ইউসুফ (আ.) এর জীবন যেন বাহ্যিকভাবে অপমান ও বঞ্চনার দিকে এগিয়ে যাচ্ছিল। কিন্তু বাস্তবে আল্লাহ তাআলা তাঁর জন্য মিসরের মাটিতে সম্মান, হিকমাহ ও নেতৃত্বের পথ প্রস্তুত করছিলেন। মানুষ তাঁকে অল্প দামে বিক্রি করেছিল, কিন্তু আল্লাহ তাঁকে তুচ্ছ হতে দেননি। মিসরের এক প্রভাবশালী ব্যক্তি তাঁকে ক্রয় করে নিজের গৃহে সম্মানের সাথে স্থান দিল। এরপর ধৈর্য, পবিত্রতা, সততা ও আল্লাহর ওপর পূর্ণ ভরসার মাধ্যমে ইউসুফ (আ.) এমন মর্যাদায় পৌঁছালেন যে একদিন মিসরের রাজাও তাঁকে নিজের নিকট ডেকে নিলেন। এ ঘটনা আমাদের শেখায় মানুষের চোখে ক্ষতি মনে হলেও, আল্লাহর পরিকল্পনায় সেটাই হতে পারে সম্মান ও সফলতার সূচনা।\n\n"
                            + "وَقَالَ ٱلَّذِى ٱشْتَرَىٰهُ مِن مِّصْرَ لِٱمْرَأَتِهِۦٓ أَكْرِمِى مَثْوَىٰهُ عَسَىٰٓ أَن يَنফَعَنَآ أَوْ نَتَّخِذَهُۥ وَلَدًا ۚ وَكَذَٰلِكَ مَكَّنَّا لِيُوسُفَ فِى ٱلْأَرْضِ...\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "মিসরের যে ব্যক্তি তাকে ক্রয় করেছিল, সে তার স্ত্রীকে বলল, তার থাকার সম্মানজনক ব্যবস্থা কর। সম্ভবত সে আমাদের উপকারে আসবে অথবা আমরা তাকে পুত্ররূপে গ্রহণ করব। এভাবেই আমি ইউসুফকে সে দেশে প্রতিষ্ঠিত করলাম।\n\n"
                            + "<b>[সূরা ইউসুফ – ২১]</b>\n\n"
                            + "وَقَالَ ٱلْمَلِكُ ٱئْتُونِى بِهِۦٓ أَسْتَخْلِصْهُ لِنَفْسِى ۖ فَلَمَّا كَلَّمَهُۥ قَالَ إِنَّكَ ٱلْيَوْمَ لَدَيْنَا مَكِينٌ أَمِينٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "রাজা বলল, তাকে আমার কাছে নিয়ে এসো, আমি তাকে আমার নিজের জন্য বিশেষভাবে নির্ধারিত করব। অতঃপর যখন তিনি তার সাথে কথা বললেন, তখন বললেন, আজ তুমি আমাদের কাছে মর্যাদাসম্পন্ন ও বিশ্বস্ত।\n\n"
                            + "<b>[সূরা ইউসুফ – ৫৪]</b>\n\n"
                            + "এভাবেই কাফেলার হাতে বিক্রি হওয়ার পর হযরত ইউসুফ (আলাইহিস সালাম) মিসরে প্রথমে সম্মানজনক আশ্রয় লাভ করেন, এরপর আল্লাহর ইচ্ছায় সত্যবাদিতা, প্রজ্ঞা ও আমানতদারিতার মাধ্যমে একসময় রাজদরবারে মর্যাদার আসনে অধিষ্ঠিত হন।",
                    "What seemed outwardly like subjugation was in truth Allah's divine orchestration to elevate Yusuf (AS) to highest honor in Egypt.\n\n"
                            + "وَقَالَ ٱلَّذِى ٱشْتَرَىٰهُ مِن مِّصْرَ لِٱمْرَأَتِهِۦٓ أَكْرِمِى مَثْوَىٰهُ عَسَىٰٓ أَن يَنফَعَنَآ أَوْ نَتَّخِذَهُۥ وَلَدًا ۚ وَكَذَٰلِكَ مَكَّنَّا لِيُوسُفَ فِى ٱلْأَرْضِ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And the one from Egypt who bought him said to his wife, 'Make his residence comfortable. Perhaps he will benefit us, or we will adopt him as a son.' And thus We established Joseph in the land...\"\n\n"
                            + "<b>[Surah Yusuf 12:21]</b>\n\n"
                            + "وَقَالَ ٱلْمَلِكُ ٱئْتُونِى بِهِۦٓ أَسْتَخْلِصْهُ لِنَفْسِى ۖ فَلَمَّا كَلَّمَهُۥ قَالَ إِنَّكَ ٱلْيَوْمَ لَدَيْنَا مَكِينٌ أَمِينٌ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And the king said, 'Bring him to me; I will appoint him exclusively for myself.' And when he spoke to him, he said, 'Indeed, you are today established [in position] and trusted.'\"\n\n"
                            + "<b>[Surah Yusuf 12:54]</b>"
            ));

            // 7. আজিজে মিশরের ঘরে লালন
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "আজিজে মিশরের ঘরে লালন",
                    "Upbringing in the House of the Aziz of Egypt",
                    "কাফেলার হাতে বিক্রি হওয়ার পর হযরত ইউসুফ (আ.) মিসরে পৌঁছান এবং আজীযে মিসর তাঁকে ক্রয় করে নিজের গৃহে নিয়ে যান। বাহ্যিকভাবে এটি ছিল এক দাসশিশুর নতুন জীবনের শুরু, কিন্তু আল্লাহ তাআলার ফয়সালায় এটাই ছিল তাঁর সম্মান, প্রতিপালন ও ভবিষ্যৎ প্রতিষ্ঠার সূচনা। আজীযে মিসরের ঘরে লালন-পালনের মধ্য দিয়ে ইউসুফ (আ.) চরিত্র, পবিত্রতা, প্রজ্ঞা ও আমানতদারিতায় এমন উচ্চতায় পৌঁছান যে পরবর্তীতে তিনি মিসরের রাজদরবারে বিশেষ মর্যাদা লাভ করেন। এ ঘটনা প্রমাণ করে, আল্লাহ যাকে রক্ষা করেন, তার জীবনের প্রতিটি ধাপই একেকটি মহৎ পরিকল্পনার অংশ।\n\n"
                            + "وَقَالَ ٱلَّذِي ٱشْتَرَىٰهُ مِن مِّصْرَ لِٱমْرَأَتِهِ أَكْرِمِي مَثْوَىٰهُ عَسَىٰ أَن يَنفَعَنَآ أَوْ نَتَّخِذَهُۥ وَلَدًا ۚ وَكَذَٰلِكَ مَكَّنَّا لِيُوسُفَ فِي ٱلْأَرْضِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "মিসরের যে ব্যক্তি তাকে ক্রয় করেছিল, সে তার স্ত্রীকে বলল, তার থাকার সম্মানজনক ব্যবস্থা কর। হয়তো সে আমাদের উপকারে আসবে, অথবা আমরা তাকে পুত্ররূপে গ্রহণ করব। এভাবেই আমি ইউসুফকে সে দেশে প্রতিষ্ঠিত করলাম।\n\n"
                            + "<b>[সূরা ইউসুফ – ২১]</b>\n\n"
                            + "وَقَالَ ٱلْمَلِكُ ٱئْتُونِي بِهِ أَسْتَخْلِصْهُ لِنَفْسِي ۖ فَلَمَّا كَلَّمَهُۥ قَالَ إِنَّكَ ٱلْيَوْمَ لَدَيْنَا مَكِينٌ أَمِينٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "রাজা বলল, তাকে আমার কাছে নিয়ে এসো, আমি তাকে আমার নিজের জন্য বিশেষভাবে নির্ধারিত করব। অতঃপর যখন তিনি তার সাথে কথা বললেন, তখন বললেন, আজ তুমি আমাদের কাছে মর্যাদাসম্পন্ন ও বিশ্বস্ত।\n\n"
                            + "<b>[সূরা ইউসুফ – ৫৪]</b>\n\n"
                            + "আজীযে মিসরের ঘরে লালিত-পালিত হয়ে হযরত ইউসুফ (আলাইহিস সালাম) ধীরে ধীরে এমন প্রজ্ঞা, পবিত্রতা ও আমানতদারিতার পরিচয় দেন যে পরবর্তীতে তিনি মিসরের রাজদরবারে সম্মানিত ও বিশ্বস্ত ব্যক্তিত্ব হিসেবে প্রতিষ্ঠিত হন।",
                    "Reared with nobility inside the house of the Aziz of Egypt, Yusuf (AS) matured in wisdom, righteousness, and profound piety.\n\n"
                            + "وَقَالَ ٱلَّذِي ٱشْتَرَىٰهُ مِن مِّصْرَ لِٱمْرَأَتِهِ أَكْرِمِي مَثْوَىٰهُ عَسَىٰ أَن يَنفَعَنَآ أَوْ نَتَّخِذَهُۥ وَلَدًا\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And the one from Egypt who bought him said to his wife, 'Make his residence comfortable. Perhaps he will benefit us, or we will adopt him as a son.'\"\n\n"
                            + "<b>[Surah Yusuf 12:21]</b>\n\n"
                            + "Through trials of integrity and chaste character, Yusuf proved himself worthy of highest divine guidance and earthly trust."
            ));

            // 8. কারারুদ্ধ হওয়া
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "কারারুদ্ধ হওয়া",
                    "Imprisonment",
                    "আজীযে মিসরের ঘরে অবস্থানের সময় হযরত ইউসুফ (আলাইহিস সালাম) এক কঠিন পরীক্ষার মুখোমুখি হন। তাঁকে গুনাহের দিকে আহ্বান করা হলে তিনি আল্লাহর কাছে আশ্রয় চান এবং স্পষ্টভাবে জানিয়ে দেন যে, অবাধ্যতার পথে যাওয়ার চেয়ে কারাগার তাঁর কাছে অধিক প্রিয়। এরপর তাঁর নির্দোষিতার নিদর্শন প্রকাশ পাওয়ার পরও মিসরের লোকেরা কিছু সময়ের জন্য তাঁকে কারারুদ্ধ করার সিদ্ধান্ত নেয়। এভাবে ইউসুফ (আ.)-এর কারাবাস ছিল অপরাধের ফল নয়; বরং তা ছিল তাঁর পবিত্রতা, আত্মসংযম ও সত্যনিষ্ঠার এক উজ্জ্বল অধ্যায়। \n\n"
                            + "قَالَ رَبِّ ٱلسِّجْنُ أَحَبُّ إِلَيَّ مِمَّا يَدْعُونَنِيٓ إِلَيْهِ وَإِلَّا تَصْرِفْ عَنِّي كَيْدَهُنَّ أَصْبُ إِلَيْهِنَّ وَأَكُن مِّنَ ٱلْجَاهِلِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি বললেন, হে আমার রব, তারা আমাকে যে কাজের দিকে ডাকছে তার চেয়ে কারাগারই আমার কাছে অধিক প্রিয়। আর আপনি যদি তাদের কৌশল আমার থেকে ফিরিয়ে না দেন, তবে আমি তাদের প্রতি আকৃষ্ট হয়ে পড়তে পারি এবং জাহিলদের অন্তর্ভুক্ত হয়ে যেতে পারি। \n\n"
                            + "<b>[সূরা ইউসুফ - ৩৩]</b>\n\n"
                            + "ثُمَّ بَدَا لَهُم مِّنۢ بَعْدِ مَا رَأَوُا ٱلْـَٔايَاتِ لَيَسْجُنُنَّهُۥ حَتَّىٰ حِينٍ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "অতঃপর নিদর্শনসমূহ দেখার পরও তাদের কাছে স্পষ্ট হলো যে, কিছু সময়ের জন্য অবশ্যই তাকে কারারুদ্ধ করে রাখবে। \n\n"
                            + "<b>[সূরা ইউসুফ - ৩৫]</b>\n\n"
                            + "এভাবেই হযরত ইউসুফ (আলাইহিস সালাম)-এর কারারুদ্ধ হওয়ার ঘটনা তাঁর জীবনের এমন এক অধ্যায়, যেখানে অপবাদ ও ষড়যন্ত্রের মাঝেও তাঁর পবিত্রতা, আল্লাহভীতি এবং চরিত্রের দৃঢ়তা স্পষ্টভাবে প্রকাশ পায়।",
                    "When confronted with temptation, Prophet Yusuf (AS) sought Allah's protection and chose prison over sin, demonstrating unwavering chastity and supreme piety.\n\n"
                            + "قَالَ رَبِّ ٱلسِّجْنُ أَحَبُّ إِلَيَّ مِمَّا يَدْعُونَنِيٓ إِلَيْهِ ۖ وَإِلَّا تَصْرِفْ عَنِّي كَيْدَهُنَّ أَصْبُ إِلَيْهِنَّ وَأَكُن مِّنَ ٱلْجَاهِلِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"He said, 'My Lord, prison is more to my liking than that to which they invite me. And if You do not avert from me their plan, I might incline toward them and be of the ignorant.'\"\n\n"
                            + "<b>[Surah Yusuf 12:33]</b>\n\n"
                            + "ثُمَّ بَدَا لَهُم مِّنۢ بَعْدِ مَا رَأَوُا ٱلْـَٔايَاتِ لَيَسْجُنُنَّهُۥ حَتَّىٰ حِينٍ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"Then it occurred to the men, even after they had seen the signs [of his innocence], to imprison him for a time.\"\n\n"
                            + "<b>[Surah Yusuf 12:35]</b>"
            ));

            // 9. বাদশাহর স্বপ্ন ব্যাখ্যার মাধ্যমে মুক্তি
            list.add(new ProphetOverviewTopicItem(
                    9,
                    "বাদশাহর স্বপ্ন ব্যাখ্যার মাধ্যমে মুক্তি",
                    "Release through Interpreting the King's Dream",
                    "কারাগারে থাকার সময় হযরত ইউসুফ (আ.) এর জ্ঞান, ধৈর্য ও আল্লাহপ্রদত্ত স্বপ্ন ব্যাখ্যার ক্ষমতা গোপন থাকেনি। যখন মিসরের বাদশাহ এক অদ্ভুত স্বপ্ন দেখলেন এবং দরবারের কেউ তার সঠিক ব্যাখ্যা দিতে পারল না, তখন কারাগার থেকে মুক্তিপ্রাপ্ত সেই ব্যক্তি ইউসুফ (আ.) এর কথা স্মরণ করল। সে কারাগারে গিয়ে ইউসুফ (আ.) এর কাছে স্বপ্নের ব্যাখ্যা জানতে চাইল। ইউসুফ (আ.) আল্লাহপ্রদত্ত প্রজ্ঞা দিয়ে স্বপ্নের সঠিক ব্যাখ্যা দিলেন। এরপর সেই ব্যাখ্যা বাদশাহর কাছে পৌঁছালে বাদশাহ তাঁর জ্ঞান ও প্রজ্ঞায় মুগ্ধ হয়ে তাঁকে দরবারে হাজির করার নির্দেশ দেন। এভাবেই বাদশাহর স্বপ্নের ব্যাখ্যা হযরত ইউসুফ (আ.) এর মুক্তির দ্বার উন্মুক্ত করে।\n\n"
                            + "وَقَالَ ٱلَّذِى نَجَا مِنْهُمَا وَٱدَّكَرَ بَعْدَ أُمَّةٍ أَنَا۠ أُنَبِّئُكُم بِتَأْوِيلِهِۦ فَأَرْسِلُونِ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "দুজনের মধ্যে যে ব্যক্তি মুক্তি পেয়েছিল এবং দীর্ঘকাল পর তার স্মরণ হলো, সে বলল, আমি তোমাদেরকে এর ব্যাখ্যা জানিয়ে দিতে পারি; সুতরাং আমাকে পাঠিয়ে দাও।\n\n"
                            + "<b>[সূরা ইউসুফ - ৪৫]</b>\n\n"
                            + "وَقَالَ ٱلْمَلِكُ ٱئْتُونِى بِهِۦ ۖ فَلَمَّا جَآءَهُ ٱلرَّسُولُ قَالَ ٱرْجِعْ إِلَىٰ رَبِّكَ فَسْـَٔلْهُ مَا بَالُ ٱلنِّسْوَةِ ٱلَّٰتِى قَطَّعْنَ أَيْدِيَهُنَّ ۚ إِنَّ رَبِّى بِكَيْدِهِنَّ عَلِيمٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "রাজা বলল, তাকে আমার কাছে নিয়ে এসো। অতঃপর দূত যখন তার কাছে এলো, তখন সে বলল, তুমি তোমার মনিবের কাছে ফিরে যাও এবং তাকে জিজ্ঞেস কর, সেই নারীদের অবস্থা কী, যারা নিজেদের হাত কেটে ফেলেছিল। নিশ্চয়ই আমার রব তাদের চক্রান্ত সম্পর্কে সম্যক অবগত।\n\n"
                            + "<b>[সূরা ইউসুফ - ৫০]</b>\n\n"
                            + "এভাবেই বাদশাহর স্বপ্নের সঠিক ব্যাখ্যার মাধ্যমে হযরত ইউসুফ (আলাইহিস সালাম)-এর কারামুক্তির পথ খুলে যায়, আর কারাগারের অন্তরাল থেকে তিনি মিসরের শাসকের দৃষ্টিতে প্রজ্ঞাবান ও বিশ্বস্ত ব্যক্তি হিসেবে পরিচিত হন।",
                    "When the King of Egypt saw a profound dream that bewildered his council, the released cupbearer remembered Yusuf's extraordinary wisdom.\n\n"
                            + "وَقَالَ ٱلَّذِى نَجَا مِنْهُمَا وَٱدَّكَرَ بَعْدَ أُمَّةٍ أَنَا۠ أُنَبِّئُكُم بِتَأْوِيلِهِۦ فَأَرْسِلُونِ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"But the one who was freed of the two and remembered after a time said, 'I will inform you of its interpretation, so send me forth.'\"\n\n"
                            + "<b>[Surah Yusuf 12:45]</b>\n\n"
                            + "Upon interpreting the dream with agricultural foresight, the King summoned him:\n\n"
                            + "وَقَالَ ٱلْمَلِكُ ٱئْتُونِى بِهِۦ ۖ فَلَمَّا جَآءَهُ ٱلرَّسُولُ قَالَ ٱرْجِعْ إِلَىٰ رَبِّكَ فَسْـَٔلْهُ مَا بَالُ ٱلنِّسْوَةِ ٱلَّٰتِى قَطَّعْنَ أَيْدِيَهُنَّ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And the king said, 'Bring him to me.' But when the messenger came to him, [Joseph] said, 'Return to your master and ask him what is the case of the women who cut their hands...'\n\n"
                            + "<b>[Surah Yusuf 12:50]</b>\n\n"
                            + "His innocence was completely vindicated before all of Egypt."
            ));

            // 10. রাষ্ট্রীয় পদ লাভ
            list.add(new ProphetOverviewTopicItem(
                    10,
                    "রাষ্ট্রীয় পদ লাভ",
                    "Attainment of High State Office",
                    "বাদশাহর স্বপ্নের সঠিক ব্যাখ্যা, নিজের নির্দোষিতা প্রমাণ এবং অসাধারণ প্রজ্ঞা ও আমানতদারিতার পরিচয়ের মাধ্যমে হযরত ইউসুফ (আ.) মিসরের শাসকের নিকট বিশেষ মর্যাদা লাভ করেন। তখন তিনি দেশের সম্পদ ও খাদ্যভাণ্ডার ব্যবস্থাপনার দায়িত্ব প্রার্থনা করেন, কারণ তিনি জানতেন এই দায়িত্ব যথাযথভাবে রক্ষা ও পরিচালনা করার যোগ্যতা আল্লাহ তাঁকে দিয়েছেন। অতঃপর আল্লাহ তাআলা তাঁকে মিসরের ভূমিতে প্রতিষ্ঠিত করেন এবং তিনি রাষ্ট্রের গুরুত্বপূর্ণ দায়িত্বে অধিষ্ঠিত হন। এভাবেই হযরত ইউসুফ (আ.) কারাগার থেকে বের হয়ে মিসরের রাষ্ট্রীয় ব্যবস্থাপনায় এক সম্মানিত পদ লাভ করেন।\n\n"
                            + "قَالَ ٱجْعَلْنِى عَلَىٰ خَزَآئِنِ ٱلْأَرْضِ ۖ إِنِّى حَفِيظٌ عَلِيمٌ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "ইউসুফ বললেন, আমাকে দেশের ধন-ভাণ্ডারের দায়িত্ব দিন; নিশ্চয়ই আমি উত্তম রক্ষণাবেক্ষণকারী, সম্যক জ্ঞানসম্পন্ন।\n\n"
                            + "<b>[সূরা ইউসুফ - ৫৫]</b>\n\n"
                            + "وَكَذَٰلِكَ مَكَّنَّا لِيُوسُفَ فِى ٱلْأَرْضِ يَتَبَوَّأُ مِنْهَا حَيْثُ يَشَآءُ ۚ نُصِيبُ بِرَحْمَتِنَا مَن نَّشَآءُ وَلَا نُضِيعُ أَجْرَ ٱلْمُحْسِنِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "এভাবেই আমি ইউসুফকে সে দেশে প্রতিষ্ঠিত করলাম। তিনি সেখানে যেখানে ইচ্ছা অবস্থান করতে পারতেন। আমি যাকে ইচ্ছা আমার রহমত দান করি এবং সৎকর্মশীলদের প্রতিদান নষ্ট করি না।\n\n"
                            + "<b>[সূরা ইউসুফ – ৫৬]</b>\n\n"
                            + "এভাবেই হযরত ইউসুফ (আলাইহিস সালাম) তাঁর প্রজ্ঞা, আমানতদারিতা ও আল্লাহপ্রদত্ত যোগ্যতার দ্বারা মিসরের ধন-ভাণ্ডার ও রাষ্ট্রীয় ব্যবস্থাপনার গুরুত্বপূর্ণ দায়িত্ব লাভ করেন।",
                    "With his innocence established and divine wisdom proven, Prophet Yusuf (AS) was entrusted with the stewardship of Egypt's storehouses and financial administration.\n\n"
                            + "قَالَ ٱجْعَلْنِى عَلَىٰ خَزَآئِنِ ٱلْأَرْضِ ۖ إِنِّى حَفِيظٌ عَلِيمٌ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"[Joseph] said, 'Appoint me over the storehouses of the land. Indeed, I will be a knowing guardian.'\"\n\n"
                            + "<b>[Surah Yusuf 12:55]</b>\n\n"
                            + "وَكَذَٰلِكَ مَكَّنَّا لِيُوسُفَ فِى ٱلْأَرْضِ يَتَبَوَّأُ مِنْهَا حَيْثُ يَشَآءُ ۚ نُصِيبُ بِرَحْمَتِنَا مَن نَّشَآءُ ۖ وَلَا نُضِيعُ أَجْرَ ٱلْمُحْسِنِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And thus We established Joseph in the land to settle therein wherever he willed. We touch with Our mercy whom We will, and We do not allow to be lost the reward of those who do good.\"\n\n"
                            + "<b>[Surah Yusuf 12:56]</b>"
            ));

            // 11. দুর্ভিক্ষ ও ভাইদের আগমন
            list.add(new ProphetOverviewTopicItem(
                    11,
                    "দুর্ভিক্ষ ও ভাইদের আগমন",
                    "The Famine and Arrival of the Brothers",
                    "হযরত ইউসুফ (আ.) মিসরের রাষ্ট্রীয় দায়িত্ব লাভ করার পর বাদশাহর স্বপ্নের ব্যাখ্যা অনুযায়ী আসন্ন দুর্ভিক্ষ মোকাবিলার জন্য সুপরিকল্পিত ব্যবস্থা গ্রহণ করেন। সাত বছর প্রাচুর্যের সময় খাদ্য সংরক্ষণ করা হয়, যাতে পরবর্তী কঠিন দুর্ভিক্ষের বছরগুলোতে মানুষ রক্ষা পায়। যখন দুর্ভিক্ষ চারদিকে ছড়িয়ে পড়ে, তখন বিভিন্ন অঞ্চল থেকে লোকজন খাদ্যের সন্ধানে মিসরে আসতে থাকে। সেই দুর্ভিক্ষের কারণেই হযরত ইউসুফ (আ.) এর ভাইয়েরাও খাদ্য সংগ্রহের উদ্দেশ্যে মিসরে আগমন করে। ইউসুফ (আ.) তাদের চিনতে পারলেও তারা তাঁকে চিনতে পারেনি। এভাবেই আল্লাহ তাআলার নির্ধারিত ফয়সালায় বহু বছর পর ভাইদের সঙ্গে তাঁর পুনর্মিলনের সূচনা ঘটে।\n\n"
                            + "قَالَ تَزْرَعُونَ سَبْعَ سِنِينَ دَأَبًا فَمَا حَصَدتُّمْ فَذَرُوهُ فِى سُنۢبُلِهِۦٓ إِلَّا قَلِيلًا مِّمَّا تَأْكُلُونَ  ثُمَّ يَأْتِى مِنۢ بَعْدِ ذَٰلِكَ سَبْعٌ شِدَادٌ يَأْكُلْنَ مَا قَدَّمْتُمْ لَهُنَّ إِلَّا قَلِيلًا مِّمَّا تُحْصِنُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "ইউসুফ বললেন, তোমরা ধারাবাহিকভাবে সাত বছর চাষাবাদ করবে। অতঃপর যা কাটবে, তার অল্প কিছু ছাড়া বাকিটা শীষের মধ্যেই রেখে দেবে। তারপর এর পর আসবে সাতটি কঠিন বছর, যা তোমরা তাদের জন্য আগে থেকে যা সঞ্চয় করে রাখবে তা খেয়ে ফেলবে, অল্প কিছু ছাড়া যা তোমরা সংরক্ষণ করবে।\n\n"
                            + "<b>[সূরা ইউসুফ - ৪৭–৪৮]</b>\n\n"
                            + "وَجَآءَ إِخْوَةُ يُوسُفَ فَدَخَلُوا۟ عَلَيْهِ فَعَرَفَهُمْ وَهُمْ لَهُۥ مُنكِرُونَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "আর ইউসুফের ভাইয়েরা এলো, তারপর তারা তার কাছে প্রবেশ করল। তখন তিনি তাদের চিনতে পারলেন, কিন্তু তারা তাকে চিনতে পারল না।\n\n"
                            + "<b>[সূরা ইউসুফ - ৫৮]</b>\n\n"
                            + "এভাবেই দুর্ভিক্ষের কঠিন সময় মিসরে হযরত ইউসুফ (আলাইহিস সালাম)-এর প্রজ্ঞাপূর্ণ ব্যবস্থাপনা প্রকাশ করে, আর সেই দুর্ভিক্ষই তাঁর ভাইদের মিসরে আগমনের মাধ্যমে বহুদিনের বিচ্ছেদের পর নতুন ঘটনার সূচনা ঘটায়।",
                    "Managing the great famine with masterful planning, Prophet Yusuf (AS) stored grain during seven fertile years.\n\n"
                            + "قَالَ تَزْرَعُونَ سَبْعَ سِنِينَ دَأَبًا فَمَا حَصَدتُّمْ فَذَرُوهُ فِى سُنۢبُلِهِۦٓ إِلَّا قَلِيلًا مِّمَّا تَأْكُلُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"[Joseph] said, 'You will plant for seven years consecutively; and what you harvest leave in its spikes, except a little from which you will eat.'\"\n\n"
                            + "<b>[Surah Yusuf 12:47]</b>\n\n"
                            + "When famine struck the entire region, Yusuf's brothers journeyed from Canaan to Egypt for grain:\n\n"
                            + "وَجَآءَ إِخْوَةُ يُوسُفَ فَدَخَلُوا۟ عَلَيْهِ فَعَرَفَهُمْ وَهُمْ لَهُۥ مُنكِرُونَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And the brothers of Joseph came and entered upon him; and he recognized them, but he was to them unknown.\"\n\n"
                            + "<b>[Surah Yusuf 12:58]</b>"
            ));

            // 12. পিতা-মাতার পুনর্মিলন ও ক্ষমা
            list.add(new ProphetOverviewTopicItem(
                    12,
                    "পিতা-মাতার পুনর্মিলন ও ক্ষমা",
                    "Reunion with Parents and Forgiveness",
                    "দীর্ঘ বিচ্ছেদ, দুঃখ, অপবাদ ও পরীক্ষার পর হযরত ইউসুফ (আ.) এর জীবনে এলো পূর্ণ মিলনের অধ্যায়। ভাইয়েরা যখন নিজেদের ভুল স্বীকার করল, তখন ইউসুফ (আ.) প্রতিশোধের পথ নেননি; বরং তাদের প্রতি ক্ষমা ঘোষণা করেন। এরপর তাঁর পিতা-মাতা তাঁর কাছে উপস্থিত হলে তিনি তাঁদের আপন সান্নিধ্যে স্থান দেন এবং নিরাপদে মিসরে প্রবেশের কথা বলেন। এভাবে ইউসুফ (আ.)-এর জীবনে পারিবারিক পুনর্মিলন ও ভ্রাতাদের প্রতি ক্ষমা উভয়ই আল্লাহর অপার পরিকল্পনার পূর্ণতা হিসেবে প্রকাশ পায়। \n\n"
                            + "قَالَ لَا تَثْرِيبَ عَلَيْكُمُ ٱلْيَوْمَ ۖ يَغْفِرُ ٱللَّهُ لَكُمْ ۖ وَهُوَ أَرْحَمُ ٱلرَّٰحِمِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তিনি বললেন, আজ তোমাদের বিরুদ্ধে আমার কোনো অভিযোগ নেই। আল্লাহ তোমাদের ক্ষমা করুন, আর তিনি সকল দয়ালুর চেয়ে শ্রেষ্ঠ দয়ালু। \n\n"
                            + "<b>[সূরা ইউসুফ - ৯২]</b>\n\n"
                            + "فَلَمَّا دَخَلُوا۟ عَلَىٰ يُوسُفَ ءَاوَىٰٓ إِلَيْهِ أَبَوَيْهِ وَقَالَ ٱدْخُلُوا۟ مِصْرَ إِن شَآءَ ٱللَّهُ ءَامِنِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "তারা যখন ইউসুফের কাছে উপস্থিত হল, সে তার পিতা-মাতাকে নিজের কাছে স্থান দিল এবং বলল, আল্লাহর ইচ্ছেয় পূর্ণ নিরাপত্তায় মিসরে প্রবেশ করুন। \n\n"
                            + "<b>[সূরা ইউসুফ - ৯৯]</b>\n\n"
                            + "এভাবেই হযরত ইউসুফ (আ.) এর জীবনে পিতা-মাতার সঙ্গে পূর্ণ মিলন এবং ভাইদের প্রতি ক্ষমা দুইটিই একসাথে এক মহিমান্বিত অধ্যায়ে পরিণত হয়।",
                    "Exemplifying supreme magnanimity and mercy, Prophet Yusuf (AS) forgave his brothers completely and welcomed his parents to Egypt in royal honor.\n\n"
                            + "قَالَ لَا تَثْرِيبَ عَلَيْكُمُ ٱلْيَوْمَ ۖ يَغْفِرُ ٱللَّهُ لَكُمْ ۖ وَهُوَ أَرْحَمُ ٱلرَّٰحِمِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"He said, 'No blame will there be upon you today. Allah will forgive you; and He is the most merciful of the merciful.'\"\n\n"
                            + "<b>[Surah Yusuf 12:92]</b>\n\n"
                            + "فَلَمَّا دَخَلُوا۟ عَلَىٰ يُوسُفَ ءَاوَىٰٓ إِلَيْهِ أَبَوَيْهِ وَقَالَ ٱدْخُلُوا۟ مِصْرَ إِن شَآءَ ٱللَّهُ ءَامِنِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"And when they entered upon Joseph, he took his parents to himself and said, 'Enter Egypt, if Allah wills, in safety.'\"\n\n"
                            + "<b>[Surah Yusuf 12:99]</b>"
            ));

            // 13. মৃত্যু ও কবরস্থান
            list.add(new ProphetOverviewTopicItem(
                    13,
                    "মৃত্যু ও কবরস্থান",
                    "Passing and Resting Place",
                    "হযরত ইউসুফ (আ.) এর জীবনের শেষ অধ্যায়ের বিস্তারিত বিবরণ কুরআনে আলাদা করে আসেনি। কুরআনের শক্ত ইশারা হলো তাঁর এই দোয়া আল্লাহ যেন তাঁকে ইসলাম অবস্থায় মৃত্যু দান করেন এবং সৎলোকদের অন্তর্ভুক্ত করেন। আর সহীহ একটি হাদিসে এসেছে, তাঁর মৃত্যুর সময় তিনি বনি ইসরাঈলের কাছ থেকে অঙ্গীকার নিয়েছিলেন যে, তারা মিসর থেকে বের হলে তাঁর দেহাবশেষ সঙ্গে নেবে। পরে মূসা (আ.) সেই কবরের স্থান জানতে পারেন। তবে আজ যেসব স্থাপনাকে ইউসুফ (আ.) এর নির্দিষ্ট কবর বলা হয়, সেগুলোর স্থান নির্ধারণ নিশ্চিতভাবে প্রমাণিত নয়। অনেক আলেমের বক্তব্য হলো, নবীদের কবরের মধ্যে নিশ্চিতভাবে জানা কবর হলো রাসূলুল্লাহ ﷺ এর কবর; অন্য নবীদের কবরের ব্যাপারে নিশ্চিত নির্ধারণ সাধারণভাবে প্রমাণিত নয়। \n\n"
                            + "رَبِّ قَدْ ءَاتَيْتَنِى مِنَ ٱلْمُلْكِ وَعَلَّمْتَنِى مِن تَأْوِيلِ ٱلْأَحَادِيثِ ۚ فَاطِرَ ٱلسَّمَـٰوَٰتِ وَٱلْأَرْضِ أَنتَ وَلِىِّۦ فِى ٱلدُّنْيَا وَٱلْـَٔاخِرَةِ ۖ تَوَفَّنِى مُسْلِمًۭا وَأَلْحِقْنِى بِٱلصَّـٰلِحِينَ\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "হে আমার রব, আপনি আমাকে রাজ্য দান করেছেন এবং কথার ব্যাখ্যা শিক্ষা দিয়েছেন। হে আসমানসমূহ ও জমিনের স্রষ্টা, আপনি দুনিয়া ও আখিরাতে আমার অভিভাবক। আমাকে মুসলিম অবস্থায় মৃত্যু দিন এবং আমাকে সৎলোকদের অন্তর্ভুক্ত করুন। \n\n"
                            + "<b>[সূরা ইউসুফ - ১০১]</b>\n\n"
                            + "إِنَّ يُوسُفَ عَلَيْهِ السَّلَامُ لَمَّا حَضَرَهُ الْمَوْتُ أَخَذَ عَلَيْنَا مَوْثِقًا مِنَ اللهِ أَلَّا نَخْرُجَ مِنْ مِصْرَ حَتَّى نَنْقُلَ عِظَامَهُ مَعَنَا\n\n"
                            + "<b>অর্থ:</b>\n\n"
                            + "ইউসুফ (আ.) এর মৃত্যু উপস্থিত হলে তিনি আমাদের কাছ থেকে আল্লাহর নামে অঙ্গীকার নিয়েছিলেন যে, আমরা মিসর থেকে বের হব না যতক্ষণ না তাঁর দেহাবশেষ আমাদের সঙ্গে নিয়ে যাই। \n\n"
                            + "<b>[সহীহ ইবন হিব্বান - ৭২৩]</b>\n\n"
                            + "এভাবেই হযরত ইউসুফ (আ.) এর মৃত্যু সম্পর্কে কুরআনে তাঁর ঈমানভরা দোয়ার উল্লেখ পাওয়া যায়, আর কবরের বিষয়ে সহীহ হাদিসে মিসরে দাফন ও পরে বনি ইসরাঈলের মাধ্যমে দেহাবশেষ স্থানান্তরের ইশারা পাওয়া গেলেও আজ তাঁর নির্দিষ্ট কবরস্থান নিশ্চিতভাবে প্রমাণিত নয়।",
                    "Prophet Yusuf (peace be upon him) concluded his blessed life with an earnest prayer for dying upon Islam and joining the righteous.\n\n"
                            + "<b>His Final Supplication:</b>\n\n"
                            + "رَبِّ قَدْ ءَاتَيْتَنِى مِنَ ٱلْمُلْكِ وَعَلَّمْتَنِى مِن تَأْوِيلِ ٱلْأَحَادِيثِ ۚ فَاطِرَ ٱلسَّمَـٰوَٰتِ وَٱلْأَرْضِ أَنتَ وَلِىِّۦ فِى ٱلدُّنْيَا وَٱلْـَٔاخِرَةِ ۖ تَوَفَّنِى مُسْلِمًۭا وَأَلْحِقْنِى بِٱلصَّـٰلِحِينَ\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"My Lord, You have given me [something] of sovereignty and taught me of the interpretation of dreams. Creator of the heavens and earth, You are my protector in this world and in the Hereafter. Cause me to die a Muslim and join me with the righteous.\"\n\n"
                            + "<b>[Surah Yusuf 12:101]</b>\n\n"
                            + "<b>Covenant Regarding His Remains:</b>\n\n"
                            + "إِنَّ يُوسُفَ عَلَيْهِ السَّلَامُ لَمَّا حَضَرَهُ الْمَوْتُ أَخَذَ عَلَيْنَا مَوْثِقًا مِنَ اللهِ أَلَّا نَخْرُجَ مِنْ مِصْرَ حَتَّى نَنْقُلَ عِظَامَهُ مَعَنَا\n\n"
                            + "<b>Translation:</b>\n\n"
                            + "\"When death approached Yusuf (peace be upon him), he took a covenant from us by Allah that we should not depart from Egypt until we carry his remains with us.\"\n\n"
                            + "<b>[Sahih Ibn Hibban: 723]</b>\n\n"
                            + "According to Islamic scholarship, only the resting place of the Prophet Muhammad (ﷺ) is known with definitive certainty, while the exact locations of earlier prophets' tombs are not established with absolute certainty."
            ));
        } else if (prophetId == 12 || prophetId == 26) {
            // হযরত মুহাম্মদ (সাঃ) - মাক্কী জীবন (All 10 Chapters / Topics 100% Verbatim)

            // 1. পবিত্র জন্ম ও বংশপরিচয়
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "পবিত্র জন্ম ও বংশপরিচয়",
                    "Sacred Birth and Lineage",
                    "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম ৫৭০ খ্রিস্টাব্দে (আমুল ফীল অর্থাৎ হাতি বর্ষে), ১২ই রবিউল আউয়াল সোমবার, ফজরের সময় মক্কা নগরীতে জন্মগ্রহণ করেন। তাঁর জন্ম তৎকালীন জাহিলিয়া যুগের অন্ধকার সমাজে একটি আলোকবর্তিকা হয়ে এসেছিল। আল্লাহ্‌ রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের জন্ম ও তাঁর প্রেরণার মাহাত্ম্য উল্লেখ করে বলেন:\n\n"
                            + "لَقَدۡ جَآءَكُمۡ رَسُولٞ مِّنۡ أَنفُسِكُمۡ عَزِيزٌ عَلَيۡهِ مَا عَنِتُّمۡ حَرِيصٌ عَلَيۡكُم بِٱلۡمُؤۡمِنِينَ رَءُوفٞ رَّحِيمٞ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তোমাদের কাছে এসেছে তোমাদেরই মধ্য থেকে একজন রাসূল; তোমাদের যে দুঃখ-কষ্ট হয় তা তাঁর জন্য অত্যন্ত কষ্টদায়ক। তিনি তোমাদের মঙ্গলকামী, মুমিনদের প্রতি অত্যন্ত স্নেহশীল ও পরম দয়ালু।\n"
                            + "<b>[সূরা আত-তাওবাহ: ১২৮]</b>\n\n"
                            + "<b>পিতা-মাতার পরিচয়:</b>\n"
                            + "• <b>পিতা:</b> আব্দুল্লাহ ইবনে আব্দুল মুত্তালিব। রাসূল (সাঃ)-এর জন্মের আগেই পিতা আব্দুল্লাহ ইন্তেকাল করেন।\n"
                            + "• <b>মাতা:</b> আমিনা বিনতে ওয়াহাব। তিনি কুরাইশদের বনু জুহরা গোত্রের এক সম্ভ্রান্ত নারী ছিলেন।\n"
                            + "• <b>দাদা:</b> আব্দুল মুত্তালিব, যিনি তৎকালীন মক্কার অন্যতম প্রধান নেতা ও কাবা ঘরের রক্ষণাবেক্ষণকারী ছিলেন।\n\n"
                            + "<b>বংশপরম্পরা:</b>\n"
                            + "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম আরবের সর্বশ্রেষ্ঠ কুরাইশ বংশের বনু হাশিম শাখায় জন্মগ্রহণ করেন। তাঁর বংশপরম্পরা হযরত ইসমাইল (আঃ) ও হযরত ইব্রাহিম (আঃ)-এর সাথে গিয়ে মিলিত হয়েছে।\n"
                            + "আল্লাহ্‌ বলেন:\n\n"
                            + "وَتَقَلُّبَكَ فِي ٱلسَّـٰجِدِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এবং সিজদাকারীদের মাঝে আপনার ওঠাবসা ও বিচরণকে।\n"
                            + "<b>[সূরা আশ-শুআরা: ২১৯]</b>\n\n"
                            + "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের পবিত্র জন্ম সমগ্র মানবজাতির জন্য এক মহান রহমত হিসেবে এসেছে।\n"
                            + "যেমন আল্লাহ্‌ বলেন:\n\n"
                            + "وَمَآ أَرۡسَلۡنَـٰكَ إِلَّا رَحۡمَةٗ لِّلۡعَـٰلَمِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমি আপনাকে সমগ্র বিশ্বজগতের জন্য রহমতস্বরূপ প্রেরণ করেছি।\n"
                            + "<b>[সূরা আল-আম্বিয়া: ১০৭]</b>\n\n"
                            + "<b>জন্মকালীন অলৌকিক ঘটনাবলী:</b>\n"
                            + "• পারস্যের অগ্নিকুণ্ডের হঠাৎ নিভে যাওয়া, যা হাজার বছর ধরে প্রজ্বলিত ছিল।\n"
                            + "• রোমান ও সিরিয়ার রাজপ্রাসাদ আলোকিত হওয়া।\n"
                            + "• কাবার রক্ষক আব্দুল মুত্তালিব কর্তৃক নাতির নাম 'মুহাম্মদ' (প্রশংসিত) রাখা, যা তৎকালীন আরবে বিরল ছিল।",
                    "The Messenger of Allah (peace and blessings be upon him) was born in 570 CE (The Year of the Elephant), on Monday, 12th Rabi' al-Awwal at dawn in the sacred city of Makkah. His birth was a beacon of divine light in the pitch-dark era of Jahiliyyah (ignorance). Allah mentions the grandeur of the Prophet's advent:\n\n"
                            + "لَقَدۡ جَآءَكُمۡ رَسُولٞ مِّنۡ أَنفُسِكُمۡ عَزِيزٌ عَلَيۡهِ مَا عَنِتُّمۡ حَرِيصٌ عَلَيۡكُم بِٱلۡمُؤۡمِنِينَ رَءُوفٞ رَّحِيمٞ\n\n"
                            + "<b>Translation:</b>\n"
                            + "There has certainly come to you a Messenger from among yourselves. Grievous to him is what you suffer; [he is] concerned over you and to the believers is kind and merciful.\n"
                            + "<b>[Surah At-Tawbah 9:128]</b>\n\n"
                            + "<b>Parents and Guardians:</b>\n"
                            + "• <b>Father:</b> Abdullah ibn Abdul-Muttalib. He passed away before the birth of the Prophet (ﷺ).\n"
                            + "• <b>Mother:</b> Aminah bint Wahb, a noble woman of the Banu Zuhrah clan of Quraish.\n"
                            + "• <b>Grandfather:</b> Abdul-Muttalib, the chief of Makkah and custodian of the Ka'bah.\n\n"
                            + "<b>Noble Lineage:</b>\n"
                            + "The Prophet (ﷺ) belonged to the noble Banu Hashim clan of the Quraish tribe. His pure lineage traces directly back to Prophet Ismail (AS) and Prophet Ibrahim (AS). Allah states:\n\n"
                            + "وَتَقَلُّبَكَ فِي ٱلسَّـٰجِدِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And your movement among those who prostrate.\n"
                            + "<b>[Surah Ash-Shu'ara 26:219]</b>\n\n"
                            + "His birth was ordained as a universal blessing for all creation:\n\n"
                            + "وَمَآ أَرۡسَلۡنَـٰكَ إِلَّا رَحۡمَةٗ لِّلۡعَـٰلَمِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And We have not sent you, [O Muhammad], except as a mercy to the worlds.\n"
                            + "<b>[Surah Al-Anbiya 21:107]</b>\n\n"
                            + "<b>Miracles Accompanying His Birth:</b>\n"
                            + "• The thousand-year-old perpetual sacred fire of Persia was suddenly extinguished.\n"
                            + "• A celestial light emanated that illuminated the palaces of Syria and Rome.\n"
                            + "• His grandfather Abdul-Muttalib named him 'Muhammad' (The Praised One), an uncommon name in Arabia at the time."
            ));

            // 2. শৈশব ও পরিচর্যা
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "শৈশব ও পরিচর্যা",
                    "Childhood and Upbringing",
                    "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের শৈশব ছিল গভীর পরীক্ষা ও আল্লাহর প্রত্যক্ষ অভিভাবকত্বে পরিপূর্ণ। জন্মের পূর্বেই তিনি পিতৃহারা হন। তৎকালীন আরবের অভিজাত প্রথা অনুযায়ী নবজাতকদের মরুভূমির মুক্ত ও বিশুদ্ধ পরিবেশে লালন-পালনের জন্য বেদুইন ধাত্রীদের কাছে পাঠানো হতো।\n\n"
                            + "<b>দুধমাতা হালিমা সাদিয়ার তত্ত্বাবধানে:</b>\n"
                            + "• বনু সাদ গোত্রের ধাত্রী হযরত হালিমা সাদিয়া (রাঃ) তাঁকে গ্রহণ করেন।\n"
                            + "• শিশু মুহাম্মদ (সাঃ)-এর আগমনে হালিমা সাদিয়ার সংসারে অভাবনীয় বরকত ও প্রাচুর্য নেমে আসে।\n"
                            + "• শৈশবে মরুভূমিতে থাকাকালীন তাঁর জীবনে ঐতিহাসিক 'বক্ষ বিদারণ' (সিনাচাক) এর ঘটনা ঘটে, যেখানে ফেরেশতা জিব্রাইল (আঃ) এসে তাঁর অন্তর ধৌত ও পবিত্র করেন।\n\n"
                            + "<b>মাতৃবিয়োগ ও দাদার অভিভাবকত্ব:</b>\n"
                            + "• ৪-৫ বছর বয়সে তিনি মক্কায় মায়ের কোলে ফিরে আসেন।\n"
                            + "• ৬ বছর বয়সে পিতা আব্দুল্লাহর কবর জিয়ারত শেষে মদিনা থেকে ফেরার পথে 'আবওয়া' নামক স্থানে মাতা আমিনা ইন্তেকাল করেন।\n"
                            + "• মাতৃহীন হওয়ার পর দাদা আব্দুল মুত্তালিব তাঁকে পরম স্নেহে লালন-পালন করতে থাকেন।\n\n"
                            + "<b>চাচার আশ্রয় ও মেষচারণ:</b>\n"
                            + "• ৮ বছর বয়সে দাদা আব্দুল মুত্তালিবও ইন্তেকাল করলে চাচা আবু তালিব তাঁর অভিভাবকত্ব গ্রহণ করেন।\n"
                            + "• চাচা আবু তালিব অর্থনৈতিকভাবে সচ্ছল না হওয়ায় বালক মুহাম্মদ (সাঃ) মক্কার বিভিন্ন প্রান্তে মেষ চড়াতেন এবং চাচাকে সাহায্য করতেন।\n\n"
                            + "<b>কুরআনের ঘোষণা:</b>\n"
                            + "আল্লাহ্‌ সুবহানাহু ওয়া তা'আলা তাঁর শৈশবের এই কঠিন পরীক্ষার কথা স্মরণ করিয়ে দিয়ে বলেন:\n\n"
                            + "أَلَمۡ يَجِدۡكَ يَتِيمٗا فَـَٔاوَىٰ • وَوَجَدَكَ ضَآلّٗا فَهَدَىٰ • وَوَجَدَكَ عَآئِلٗا فَأَغۡنَىٰ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তিনি কি আপনাকে এতিম অবস্থায় পাননি, অতঃপর আশ্রয় দেননি? তিনি আপনাকে পেলেন পথ নির্দেশহীন অবস্থায়, অতঃপর পথ দেখালেন। এবং তিনি আপনাকে পেলেন নিঃস্ব অবস্থায়, অতঃপর অভাবমুক্ত করলেন।\n"
                            + "<b>[সূরা আদ-দুহা: ৬-৮]</b>\n\n"
                            + "শৈশব থেকেই তিনি জাহেলি সমাজের সমস্ত পাপ, পৌত্তলিকতা ও অন্যায় থেকে সম্পূর্ণ মুক্ত ও পুত-পবিত্র ছিলেন।",
                    "The childhood of the Messenger of Allah (ﷺ) was marked by profound trials under divine protection. He was born fatherless. According to noble Arab customs, infants were entrusted to Bedouin foster mothers in the desert for fresh air and pure Arabic eloquence.\n\n"
                            + "<b>Under Foster Mother Halimah As-Sa'diyyah:</b>\n"
                            + "• Halimah bint Abi Dhu'ayb of the Banu Sa'd tribe took infant Muhammad (ﷺ) into her care.\n"
                            + "• Unprecedented divine blessings and abundance enriched Halimah's household from the moment he arrived.\n"
                            + "• During his stay in the desert, the miraculous opening of his chest (Shaqq As-Sadr) took place, where Angel Jibreel washed and purified his blessed heart.\n\n"
                            + "<b>Loss of Mother and Grandfather's Guardianship:</b>\n"
                            + "• At age 4-5, he returned to his mother Aminah in Makkah.\n"
                            + "• At age 6, upon returning from visiting his father's grave in Yathrib, his mother Aminah passed away at Al-Abwa.\n"
                            + "• His grandfather Abdul-Muttalib took him into his loving and protective care.\n\n"
                            + "<b>Uncle Abu Talib's Care & Shepherding:</b>\n"
                            + "• At age 8, Abdul-Muttalib passed away, leaving his custody to his uncle Abu Talib.\n"
                            + "• Due to Abu Talib's modest means, the young Muhammad (ﷺ) worked as a shepherd in Makkah to assist his family.\n\n"
                            + "<b>Quranic Confirmation:</b>\n"
                            + "Allah Almighty reminds him of these early childhood blessings:\n\n"
                            + "أَلَمۡ يَجِدۡكَ يَتِيمٗا فَـَٔاوَىٰ • وَوَجَدَكَ ضَآلّٗا فَهَدَىٰ • وَوَجَدَكَ عَآئِلٗا فَأَغۡنَىٰ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Did He not find you an orphan and give [you] refuge? And He found you lost and guided [you]. And He found you poor and made [you] self-sufficient.\n"
                            + "<b>[Surah Ad-Duha 93:6-8]</b>\n\n"
                            + "Throughout his early years, he was divinely safeguarded from all pagan rituals, idol worship, and vices of pre-Islamic Arabia."
            ));

            // 3. নবুয়তের পূর্ব জীবনের সততা
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "নবুয়তের পূর্ব জীবনের সততা",
                    "Honesty in Pre-Prophethood Life",
                    "নবুয়ত প্রাপ্তির পূর্ববর্তী জীবনেও রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম তাঁর অনুপম চরিত্র, নিষ্কলুষ সততা, আমানতদারী ও ন্যায়পরায়ণতার জন্য মক্কার সমগ্র সমাজে অনন্য দৃষ্টান্ত স্থাপন করেছিলেন।\n\n"
                            + "<b>'আল-আমীন' ও 'আস-সাদিক' উপাধি:</b>\n"
                            + "• মক্কার পৌত্তলিক সমাজও একবাক্যে তাঁকে <b>'আল-আমীন'</b> (বিশ্বস্ত/আমানতদার) এবং <b>'আস-সাদিক'</b> (সত্যবাদী) উপাধিতে ভূষিত করেছিল।\n"
                            + "• মানুষ তাদের মূল্যবান ধন-সম্পদ ও আমানত নিশ্চিন্তে তাঁর কাছে গচ্ছিত রাখত।\n\n"
                            + "<b>হিলফুল ফুজুল ও সামাজিক ন্যায়বিচার:</b>\n"
                            + "• ফিজার যুদ্ধের ভয়াবহতা প্রত্যক্ষ করার পর তিনি মজলুম ও অধিকারবঞ্চিত মানুষের সহায়তায় গঠিত <b>'হিলফুল ফুজুল'</b> (শান্তিচুক্তি) নামক ঐতিহাসিক শান্তি সংস্থায় সক্রিয়ভাবে অংশগ্রহণ করেন।\n"
                            + "• সেখানে তিনি যেকোনো জুলুমের বিরুদ্ধে ঐক্যবদ্ধ লড়াইয়ের শপথ নেন।\n\n"
                            + "<b>কা'বা পুনর্নির্মাণ ও হাজরে আসওয়াদ স্থাপন:</b>\n"
                            + "• ৩৫ বছর বয়সে কাবা ঘর পুনর্নির্মাণের সময় পবিত্র 'হাজরে আসওয়াদ' (কালো পাথর) স্থাপন নিয়ে কুরাইশ গোত্রগুলোর মধ্যে রক্তক্ষয়ী যুদ্ধের উপক্রম হয়।\n"
                            + "• রাসূলুল্লাহ (সাঃ) অত্যন্ত বিচক্ষণতার সাথে নিজের চাদর বিছিয়ে তাতে পাথরটি রাখলেন এবং সকল গোত্রপ্রধানকে চাদরের কোণা ধরে তুলতে বললেন। অতঃপর নিজ হাতে তা যথাস্থানে স্থাপন করে এক আসন্ন গৃহযুদ্ধ থেকে পুরো মক্কাবাসীকে রক্ষা করলেন।\n\n"
                            + "<b>চরিত্রের মাহাত্ম্য সম্পর্কে আল্লাহর বাণী:</b>\n\n"
                            + "وَإِنَّكَ لَعَلَىٰ خُلُقٍ عَظِيمٖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এবং নিশ্চয় আপনি এক মহান চরিত্রের অধিকারী।\n"
                            + "<b>[সূরা আল-কলম: ৪]</b>\n\n"
                            + "রাসূলুল্লাহ (সাঃ)-এর নবুওয়াত-পূর্ব জীবন ছিল পরবর্তী নবুওয়াত ও রিসালাতের সত্যতার এক জ্বলন্ত ঐতিহাসিক প্রমাণ।",
                    "Even before receiving prophethood, the Messenger of Allah (ﷺ) set the pinnacle of moral excellence, unblemished honesty, trustworthiness, and justice across Arabian society.\n\n"
                            + "<b>The Titles 'Al-Amin' and 'As-Sadiq':</b>\n"
                            + "• Even pagan Makkah unanimously called him <b>'Al-Amin'</b> (The Trustworthy) and <b>'As-Sadiq'</b> (The Truthful).\n"
                            + "• People entrusted their most valuable possessions and deposits into his safe keeping without hesitation.\n\n"
                            + "<b>Hilf al-Fudul (The League of the Virtuous):</b>\n"
                            + "• Witnessing the devastation of the Fijar War, he actively participated in forming <b>'Hilf al-Fudul'</b>, an alliance dedicated to protecting the oppressed and restoring the rights of foreign traders.\n"
                            + "• He pledged to stand united against injustice and tyranny.\n\n"
                            + "<b>Rebuilding of the Ka'bah and the Black Stone:</b>\n"
                            + "• At age 35, when the Ka'bah was rebuilt, fierce tribal rivalries erupted over who would place the sacred Black Stone (Hajr-e-Aswad), bringing Makkah to the verge of civil war.\n"
                            + "• The Prophet (ﷺ) devised a brilliant resolution: spreading his cloak, placing the stone upon it, and asking every clan chieftain to lift a corner of the cloak together. He then set the stone into place with his own blessed hands, averting bloodshed.\n\n"
                            + "<b>Divine Praise of His Sublime Character:</b>\n"
                            + "Allah praises his exemplary nature:\n\n"
                            + "وَإِنَّكَ لَعَلَىٰ خُلُقٍ عَظِيمٖ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And indeed, you are of a great moral character.\n"
                            + "<b>[Surah Al-Qalam 68:4]</b>\n\n"
                            + "His pre-prophethood life stood as unshakeable historical testimony to his absolute integrity when the revelation began."
            ));

            // 4. ব্যবসায়িক জীবন
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "ব্যবসায়িক জীবন",
                    "Business and Trade Life",
                    "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম তাঁর যৌবনে জীবিকা নির্বাহের জন্য হালাল ব্যবসাকে বেছে নিয়েছিলেন। সততা, স্বচ্ছতা ও ওয়াদা রক্ষার মাধ্যমে তিনি তৎকালীন বাণিজ্যিক অঙ্গনে এক অনন্য আদর্শ স্থাপন করেন।\n\n"
                            + "<b>সিরিয়া ও ইয়েমেনে বাণিজ্যিক সফর:</b>\n"
                            + "• মাত্র ১২ বছর বয়সে চাচা আবু তালিবের সাথে তিনি প্রথম সিরিয়া সফরে যান, যেখানে বুহাইরা নামক এক খ্রিস্টান সন্ন্যাসী তাঁর মধ্যে শেষ নবীর লক্ষণ দেখতে পেয়েছিলেন।\n"
                            + "• পরবর্তীতে তিনি নিজে সততা ও বিশ্বস্ততার সাথে মক্কা, ইয়েমেন ও সিরিয়ার বিভিন্ন বাণিজ্যিক কাফেলা পরিচালনা করেন।\n\n"
                            + "<b>হযরত খাদিজা (রাঃ)-এর সাথে বাণিজ্যিক অংশীদারিত্ব:</b>\n"
                            + "• মক্কার সম্ভ্রান্ত ও ধনী ব্যবসায়ী নারী হযরত খাদিজা বিনতে খুওয়াইলিদ (রাঃ) রাসূলুল্লাহ (সাঃ)-এর সততার খ্যাতি শুনে তাঁকে দ্বিগুণ লভ্যাংশের শর্তে নিজের বাণিজ্যিক কাফেলা নিয়ে সিরিয়া যাওয়ার প্রস্তাব দেন।\n"
                            + "• খাদিজা (রাঃ)-এর দাস 'মায়সারা' এই সফরে রাসূল (সাঃ)-এর সাথে ছিল এবং সে তাঁর অভূতপূর্ব সততা, আমানতদারী ও অলৌকিক আচরণ প্রত্যক্ষ করে খাদিজা (রাঃ)-এর নিকট বিস্তারিত বর্ণনা করে।\n"
                            + "• এই সফল ব্যবসায় বিপুল মুনাফা অর্জিত হয় এবং হযরত খাদিজা (রাঃ) তাঁর অনুপম চরিত্রে মুগ্ধ হয়ে পরবর্তীতে বিবাহের প্রস্তাব পাঠান।\n\n"
                            + "<b>ব্যবসায় সততা ও ইনসাফ সম্পর্কে কুরআনের নির্দেশ:</b>\n\n"
                            + "وَأَوۡفُواْ ٱلۡكَيۡلَ وَٱلۡمِيزَانَ بِٱلۡقِسۡطِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর তোমরা মাপ ও ওজনকে ন্যায়নিষ্ঠার সাথে পূর্ণ করো।\n"
                            + "<b>[সূরা আল-আন'আম: ১৫২]</b>\n\n"
                            + "রাসূলুল্লাহ (সাঃ) সর্বদা ক্রয়-বিক্রয়ে মিথ্যা শপথ, অতিরিক্ত লাভ, ধোঁকাবাজি ও ওজনে কম দেওয়া কঠোরভাবে বর্জন করতেন।",
                    "In his youth, the Messenger of Allah (ﷺ) chose legitimate commerce for his livelihood. Through pristine honesty, transparency, and honoring covenants, he revolutionized the trade ethics of his time.\n\n"
                            + "<b>Trade Expeditions to Syria and Yemen:</b>\n"
                            + "• At age 12, he accompanied his uncle Abu Talib on his first commercial journey to Syria, where the monk Bahira recognized the signs of the Final Prophet upon him.\n"
                            + "• Later, he independently managed trade caravans traveling between Makkah, Yemen, and Syria with absolute honesty.\n\n"
                            + "<b>Partnership with Khadijah (RA):</b>\n"
                            + "• Impressed by his spotless reputation, Sayyidah Khadijah bint Khuwaylid (RA), a noble and wealthy Makkan merchant, invited him to lead her trade caravan to Syria offering double the customary commission.\n"
                            + "• Khadijah's servant 'Maysarah' accompanied him and witnessed his unparalleled fairness, trustworthiness, and blessed fortune, reporting all of it to Khadijah.\n"
                            + "• The journey yielded unprecedented profits and deeply moved Khadijah, leading her to propose marriage to him.\n\n"
                            + "<b>Quranic Ethos on Honest Commerce:</b>\n\n"
                            + "وَأَوۡفُواْ ٱلۡكَيۡلَ وَٱلۡمِيزَانَ بِٱلۡقِسۡطِ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And give full measure and weight with justice.\n"
                            + "<b>[Surah Al-An'am 6:152]</b>\n\n"
                            + "The Prophet (ﷺ) strictly refrained from false swearing, exploitation, deception, and fraudulent measurements in all business dealings."
            ));

            // 5. বিবাহ ও পারিবারিক জীবন
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "বিবাহ ও পারিবারিক জীবন",
                    "Marriage and Family Life",
                    "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের পারিবারিক জীবন ছিল ভালোবাসা, শ্রদ্ধা, ইনসাফ ও কোমলতার এক অনুপম দৃষ্টান্ত।\n\n"
                            + "<b>হযরত খাদিজা (রাঃ)-এর সাথে বিবাহ:</b>\n"
                            + "• ২৫ বছর বয়সে রাসূলুল্লাহ (সাঃ) ৪০ বছর বয়সী মহীয়সী নারী হযরত খাদিজা বিনতে খুওয়াইলিদ (রাঃ)-এর সাথে বিবাহবন্ধনে আবদ্ধ হন।\n"
                            + "• খাদিজা (রাঃ) জীবিত থাকাকালীন রাসূল (সাঃ) আর কোনো বিবাহ করেননি। দীর্ঘ ২৫ বছরের দাম্পত্য জীবন ছিল অত্যন্ত সুখ ও পারস্পরিক সহযোগিতাপূর্ণ।\n"
                            + "• প্রথম ওহী নাযিলের চরম ভীতিকর মুহূর্তে খাদিজা (রাঃ)-ই তাঁকে সান্ত্বনা দেন এবং সর্বপ্রথম ইসলাম গ্রহণ করেন।\n\n"
                            + "<b>রাসূল (সাঃ)-এর সন্তানসন্ততি:</b>\n"
                            + "রাসূলুল্লাহ (সাঃ)-এর মোট ৭ জন সন্তানের মধ্যে ৬ জনই হযরত খাদিজা (রাঃ)-এর গর্ভে জন্মগ্রহণ করেন (হযরত মারিয়া কিবতিয়ার গর্ভে জন্ম নেন পুত্র ইব্রাহিম):\n"
                            + "• <b>পুত্রগণ:</b> কাসেম ও আব্দুল্লাহ (তৈয়্যিব ও তাহের)—যারা শৈশবেই মৃত্যুবরণ করেন।\n"
                            + "• <b>কন্যাগণ:</b> যায়নব, রুকাইয়্যাহ, উম্মে কুলসুম এবং ফাতেমা (রাদিয়াল্লাহু আনহুন্না)।\n"
                            + "• হযরত ফাতেমা (রাঃ)-এর মাধ্যমেই রাসূল (সাঃ)-এর পবিত্র বংশধারা পৃথিবীতে বজায় রয়েছে।\n\n"
                            + "<b>রাসূল (সাঃ)-এর অন্যান্য উম্মাহাতুল মুমিনীন:</b>\n"
                            + "খাদিজা (রাঃ)-এর ওফাতের পর দ্বীনি, সামাজিক ও রাজনৈতিক প্রয়োজনে তিনি সাওদা, আয়েশা, হাফসা, যায়নব বিনতে জাহশ, উম্মে সালামা সহ অন্যান্য উম্মাহাতুল মুমিনীনদের বিবাহ করেন।\n\n"
                            + "<b>পারিবারিক জীবনের সুন্নাহ ও হাদিস:</b>\n"
                            + "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম বলেন:\n\n"
                            + "خَيْرُكُمْ خَيْرُكُمْ لِأَهْلِهِ وَأَنَا خَيْرُكُمْ لِأَهْلِي\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তোমাদের মধ্যে সেই ব্যক্তি সর্বোত্তম, যে তার পরিবারের কাছে উত্তম; আর আমি আমার পরিবারের কাছে তোমাদের চেয়েও বেশি উত্তম।\n"
                            + "<b>[সুনান আত-তিরমিযী: ৩৮৯৫, সহীহ]</b>\n\n"
                            + "তিনি ঘরে স্ত্রীদের কাজে সহায়তা করতেন, জুতো সেলাই করতেন, কাপড় ধৌত করতেন এবং সন্তানদের পরম স্নেহে চুম্বন করতেন।",
                    "The family life of the Messenger of Allah (ﷺ) serves as the sublime standard of love, compassion, equity, and domestic harmony.\n\n"
                            + "<b>Marriage to Sayyidah Khadijah (RA):</b>\n"
                            + "• At age 25, the Prophet (ﷺ) married the 40-year-old noble widow Sayyidah Khadijah bint Khuwaylid (RA).\n"
                            + "• Throughout their 25 years of marriage, he remained devoted solely to Khadijah until her demise.\n"
                            + "• During the terrifying moments of the first revelation, Khadijah comforted him and became the very first person to embrace Islam.\n\n"
                            + "<b>Children of the Prophet (ﷺ):</b>\n"
                            + "Six of the Prophet's seven children were born to Sayyidah Khadijah (RA), with his son Ibrahim born to Sayyidah Mariyah al-Qibtiyyah:\n"
                            + "• <b>Sons:</b> Al-Qasim and Abdullah (also called At-Tayyib and At-Tahir), both passing away in infancy.\n"
                            + "• <b>Daughters:</b> Zaynab, Ruqayyah, Umm Kulthum, and Fatimah (May Allah be pleased with them all).\n"
                            + "• His noble lineage was preserved through Sayyidah Fatimah az-Zahra (RA).\n\n"
                            + "<b>Mothers of the Believers (Ummahat al-Mu'minin):</b>\n"
                            + "Following Khadijah's demise, he married Sawdah, Aisha, Hafsah, Zaynab bint Jahsh, Umm Salamah, and others for divine, humanitarian, and tribal unifications.\n\n"
                            + "<b>Prophetic Guidance on Family:</b>\n"
                            + "The Messenger of Allah (ﷺ) declared:\n\n"
                            + "خَيْرُكُمْ خَيْرُكُمْ لِأَهْلِهِ وَأَنَا خَيْرُكُمْ لِأَهْلِي\n\n"
                            + "<b>Translation:</b>\n"
                            + "The best of you is the best to his family, and I am the best among you to my family.\n"
                            + "<b>[Jami' at-Tirmidhi 3895, Sahih]</b>\n\n"
                            + "He assisted his family with daily chores, repaired his shoes, patched garments, and showered his children with affection."
            ));

            // 6. নবুয়ত প্রাপ্তি
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "নবুয়ত প্রাপ্তি",
                    "Attainment of Prophethood",
                    "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের বয়স যখন ৪০ বছর উপনীত হয়, তখন তিনি মক্কার পৌত্তলিকতা ও অবক্ষয় দেখে ব্যথিত হয়ে নির্জনতা পছন্দ করতে শুরু করেন। তিনি মক্কার অদূরে অবস্থিত 'জাবালে নূর' পর্বতের <b>হেরা গুহায়</b> দিনরাত এক আল্লাহর ইবাদত ও ধ্যানে নিমগ্ন থাকতেন।\n\n"
                            + "<b>প্রথম ওহী নাযিলের ঐতিহাসিক মুহূর্ত:</b>\n"
                            + "• ৬১০ খ্রিস্টাব্দের রমজান মাসের লাইলাতুল কদরের রাতে ফেরেশতা জিব্রাইল (আঃ) আল্লাহর পক্ষ থেকে প্রথম ওহী নিয়ে হেরা গুহায় আগমন করেন।\n"
                            + "• জিব্রাইল (আঃ) এসে তাঁকে বললেন: <b>'ইকরা' (পড়ুন!)</b>। রাসূল (সাঃ) উত্তর দিলেন: 'মা আনা বি ক্বারি' (আমি তো পড়তে জানি না)।\n"
                            + "• জিব্রাইল (আঃ) তাঁকে শক্ত করে বুকে চেপে ধরে আলিঙ্গন করলেন এবং পুনরায় পড়ার নির্দেশ দিলেন। এভাবে তিনবার আলিঙ্গনের পর কুরআনের প্রথম পাঁচটি আয়াত অবতীর্ণ হলো:\n\n"
                            + "اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ • خَلَقَ الْإِنسَانَ مِنْ عَلَقٍ • اقْرَأْ وَرَبُّكَ الْأَكْرَمُ • الَّذِي عَلَّمَ بِالْقَلَمِ • عَلَّمَ الْإِنسَانَ مَا لَمْ يَعْلَمْ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "পাঠ করুন আপনার প্রতিপালকের নামে যিনি সৃষ্টি করেছেন। যিনি মানুষকে সৃষ্টি করেছেন জমাট রক্ত থেকে। পাঠ করুন, আর আপনার প্রতিপালক পরম দয়ালু, যিনি কলমের সাহায্যে শিক্ষা দিয়েছেন। তিনি মানুষকে শিক্ষা দিয়েছেন যা সে জানত না।\n"
                            + "<b>[সূরা আল-আলাক: ১-৫]</b>\n\n"
                            + "<b>ভীতি ও হযরত খাদিজা (রাঃ)-এর সান্ত্বনা:</b>\n"
                            + "• রাসূলুল্লাহ (সাঃ) ওহীর ভার ও ভয়াবহতায় কাঁপতে কাঁপতে ঘরে ফিরে এসে স্ত্রী খাদিজা (রাঃ)-কে বললেন: 'যাম্মিলূনী, যাম্মিলূনী' (আমাকে চাদর দিয়ে আবৃত করো!)।\n"
                            + "• খাদিজা (রাঃ) তাঁর মুখে ঘটনা শুনে অভয় দিয়ে বললেন: \"কখনোই নয়! আল্লাহর শপথ, আল্লাহ আপনাকে কখনোই লাঞ্ছিত করবেন না। কারণ আপনি আত্মীয়দের সাহায্য করেন, সত্য কথা বলেন, দুর্বলদের দায়িত্ব নেন, মেহমানদারী করেন এবং বিপদে মানুষকে সহায়তা করেন।\"\n"
                            + "• অতঃপর খাদিজা (রাঃ) তাঁকে তাওরাত ও ইঞ্জিলের পণ্ডিত ওয়ারাকা ইবনে নাওফালের কাছে নিয়ে যান, যিনি নিশ্চিত করেন যে ইনিই সেই ফেরেশতা যিনি মূসা (আঃ)-এর কাছে এসেছিলেন।",
                    "As the Messenger of Allah (ﷺ) approached the age of forty, he was deeply perturbed by the idolatry and moral decline in Makkah. He retreated to <b>Cave Hira</b> atop Mount Jabal al-Nour for solitary worship and deep contemplation of the Creator.\n\n"
                            + "<b>The Moment of the First Revelation:</b>\n"
                            + "• In the Ramadan of 610 CE, during Laylat al-Qadr (The Night of Decree), Angel Jibreel (AS) descended into the cave with the first divine revelation.\n"
                            + "• Jibreel commanded: <b>\"Iqra!\" (Read!)</b>. The Prophet (ﷺ) replied: \"I cannot read.\"\n"
                            + "• Jibreel embraced him firmly three times until his capacity was reached, reciting the first five verses of the Holy Quran:\n\n"
                            + "اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ • خَلَقَ الْإِنسَانَ مِنْ عَلَقٍ • اقْرَأْ وَرَبُّكَ الْأَكْرَمُ • الَّذِي عَلَّمَ بِالْقَلَمِ • عَلَّمَ الْإِنسَانَ مَا لَمْ يَعْلَمْ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Read in the name of your Lord who created. Created man from a clinging clot. Read, and your Lord is the most Generous. Who taught by the pen. Taught man that which he knew not.\n"
                            + "<b>[Surah Al-Alaq 96:1-5]</b>\n\n"
                            + "<b>The Return Home and Khadijah's Solace:</b>\n"
                            + "• Trembling with the weight of revelation, the Prophet (ﷺ) returned home saying: \"Zammiluni, Zammiluni!\" (Cover me, wrap me up!).\n"
                            + "• Sayyidah Khadijah (RA) comforted him with immortal words: \"Never! By Allah, He will never disgrace you. You uphold family ties, speak the truth, support the weak, honor guests, and aid those afflicted by calamity.\"\n"
                            + "• Khadijah brought him to her cousin Waraqah ibn Nawfal, a scholar of the Torah and Gospel, who confirmed that this was the Archangel Jibreel (AS) sent previously to Prophet Musa (AS)."
            ));

            // 7. প্রাথমিক দাওয়াত
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "প্রাথমিক দাওয়াত",
                    "Initial Preaching",
                    "নবুয়ত প্রাপ্তির পর রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম প্রথম ৩ বছর অত্যন্ত বিচক্ষণতার সাথে গোপনে ও সতর্কতার সাথে ইসলামের দাওয়াত প্রদান করেন।\n\n"
                            + "<b>ইসলাম গ্রহণের অগ্রদূতগণ:</b>\n"
                            + "• <b>নারীদের মধ্যে প্রথম:</b> হযরত খাদিজা বিনতে খুওয়াইলিদ (রাঃ)।\n"
                            + "• <b>পুরুষদের মধ্যে প্রথম:</b> হযরত আবু বকর সিদ্দিক (রাঃ)।\n"
                            + "• <b>বালকদের মধ্যে প্রথম:</b> হযরত আলী ইবনে আবী তালিব (রাঃ)।\n"
                            + "• <b>ক্রীতদাসদের মধ্যে প্রথম:</b> হযরত যায়েদ ইবনে হারেসা (রাঃ)।\n\n"
                            + "<b>হযরত আবু বকর (রাঃ)-এর মাধ্যমে দাওয়াতের প্রসার:</b>\n"
                            + "হযরত আবু বকর (রাঃ)-এর দাওয়াতে তৎকালীন বহু বিশিষ্ট ও প্রভাবশালী ব্যক্তি ইসলাম গ্রহণ করেন, যাদের মধ্যে রয়েছেন:\n"
                            + "• হযরত উসমান ইবনে আফফান (রাঃ)\n"
                            + "• হযরত যুবায়ের ইবনে আওয়াম (রাঃ)\n"
                            + "• হযরত আব্দুর রহমান ইবনে আউফ (রাঃ)\n"
                            + "• হযরত সাদ ইবনে আবি ওয়াক্কাস (রাঃ)\n"
                            + "• হযরত তালহা ইবনে উবাইদুল্লাহ (রাঃ)\n\n"
                            + "<b>দারুল আরকাম (ইসলামের প্রথম শিক্ষাকেন্দ্র):</b>\n"
                            + "• গোপনে দাওয়াত ও নবমুসলিমদের কুরআন শিক্ষার জন্য হযরত আরকাম ইবনে আবিল আরকাম (রাঃ)-এর বাড়িকে (দারুল আরকাম) গোপন কেন্দ্র হিসেবে নির্ধারণ করা হয়।\n"
                            + "• প্রাথমিক মুসলিমরা গোপন পাহাড়ি উপত্যকায় গিয়ে জামাতে নামাজ আদায় করতেন।\n\n"
                            + "<b>প্রাথমিক মুসলমানদের উপর নির্যাতন:</b>\n"
                            + "ইসলাম গ্রহণের সংবাদ ছড়িয়ে পড়ার পর দুর্বল ও দাস মুসলিমদের ওপর কুরাইশরা নির্মম নির্যাতন শুরু করে:\n"
                            + "• হযরত বিলাল (রাঃ)-কে উত্তপ্ত বালুর ওপর শুইয়ে বুকের ওপর ভারী পাথর চাপা দেওয়া হতো।\n"
                            + "• হযরত খাব্বাব (রাঃ)-কে জ্বলন্ত কয়লার ওপর শোয়ানো হতো।\n"
                            + "• ইসলামের প্রথম শহীদ নারী হলেন হযরত সুমাইয়্যাহ (রাঃ), যাঁকে আবু জাহেল বর্শা নিক্ষেপ করে নির্মমভাবে হত্যা করে।\n\n"
                            + "<b>আল্লাহ্‌র সুসংবাদ:</b>\n\n"
                            + "إِنَّ ٱلَّذِينَ ءَامَنُواْ وَعَمِلُواْ ٱلصَّـٰلِحَـٰتِ كَانَتۡ لَهُمۡ جَنَّـٰتُ ٱلۡفِرۡدَوۡسِ نُزُلًا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয় যারা ঈমান আনে এবং সৎকর্ম করে, তাদের আপ্যায়নের জন্য রয়েছে জান্নাতুল ফিরদাউস।\n"
                            + "<b>[সূরা আল-কাহাফ: ১০৭]</b>",
                    "Following the revelation, the Messenger of Allah (ﷺ) conducted the call to Islam discreetly and strategically for the first three years.\n\n"
                            + "<b>The Pioneers of Faith (As-Sabiqun al-Awwalun):</b>\n"
                            + "• <b>First among women:</b> Sayyidah Khadijah bint Khuwaylid (RA).\n"
                            + "• <b>First among men:</b> Sayyiduna Abu Bakr as-Siddiq (RA).\n"
                            + "• <b>First among youths:</b> Sayyiduna Ali ibn Abi Talib (RA).\n"
                            + "• <b>First among freedmen:</b> Sayyiduna Zayd ibn Harithah (RA).\n\n"
                            + "<b>Expansion through Abu Bakr (RA):</b>\n"
                            + "Through Abu Bakr's personal outreach, prominent figures embraced Islam:\n"
                            + "• Uthman ibn Affan (RA)\n"
                            + "• Az-Zubayr ibn al-Awwam (RA)\n"
                            + "• Abdur-Rahman ibn Awf (RA)\n"
                            + "• Sa'd ibn Abi Waqqas (RA)\n"
                            + "• Talhah ibn Ubaydillah (RA)\n\n"
                            + "<b>Dar al-Arqam (The First Learning Center):</b>\n"
                            + "• The house of Al-Arqam ibn Abi al-Arqam (Dar al-Arqam) served as the clandestine sanctuary for worship, Quranic study, and fellowship.\n"
                            + "• Early Muslims prayed congregationally in secluded mountain valleys around Makkah.\n\n"
                            + "<b>Severe Persecution of Early Believers:</b>\n"
                            + "As faith spread, the Quraish unleashed brutal torture upon vulnerable converts:\n"
                            + "• Bilal ibn Rabah (RA) was dragged across scorching sands under heavy boulders while chanting \"Ahad, Ahad\" (One, One).\n"
                            + "• Khabbab ibn al-Aratt (RA) was laid on burning coals until his body fat quenched the flames.\n"
                            + "• Sayyidah Sumayyah bint Khayyat (RA) was martyred by Abu Jahl, becoming the first martyr in Islam.\n\n"
                            + "<b>Divine Glad Tidings:</b>\n\n"
                            + "إِنَّ ٱلَّذِينَ ءَامَنُواْ وَعَمِلُواْ ٱلصَّـٰلِحَـٰتِ كَانَتۡ لَهُمۡ جَنَّـٰتُ ٱلۡفِرۡدَوۡسِ نُزُلًا\n\n"
                            + "<b>Translation:</b>\n"
                            + "Indeed, those who have believed and done righteous deeds - they will have the Gardens of Paradise as a lodging.\n"
                            + "<b>[Surah Al-Kahf 18:107]</b>"
            ));

            // 8. প্রকাশ্যে ইসলাম প্রচার
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "প্রকাশ্যে ইসলাম প্রচার",
                    "Public Propagation of Islam",
                    "৩ বছর গোপনে দাওয়াত দেওয়ার পর আল্লাহ তা'আলা প্রকাশ্যে ইসলামের দাওয়াত প্রচারের সুস্পষ্ট নির্দেশ প্রদান করেন:\n\n"
                            + "فَٱصۡدَعۡ بِمَا تُؤۡمَرُ وَأَعۡرِضۡ عَنِ ٱلۡمُشۡرِكِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অতএব আপনি যে বিষয়ে আদিষ্ট হয়েছেন তা প্রকাশ্যে প্রচার করুন এবং মুশরিকদের থেকে মুখ ফিরিয়ে নিন।\n"
                            + "<b>[সূরা আল-হিজর: ৯৪]</b>\n\n"
                            + "এবং আল্লাহ্‌ আরও নির্দেশ দেন:\n\n"
                            + "وَأَنذِرۡ عَشِيرَتَكَ ٱلۡأَقۡرَبِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এবং আপনার নিকটতম আত্মীয়-স্বজনকে সতর্ক করুন।\n"
                            + "<b>[সূরা আশ-শুআরা: ২১৪]</b>\n\n"
                            + "<b>সাফা পাহাড়ে ঐতিহাসিক ঘোষণা:</b>\n"
                            + "• প্রকাশ্যে দাওয়াতের নির্দেশ পেয়ে রাসূলুল্লাহ (সাঃ) সাফা পর্বতে আরোহণ করলেন এবং মক্কার সকল কুরাইশ গোত্রকে 'ইয়া সাবাহাহ' (ভোরের সতর্কবার্তা) বলে আহ্বান করলেন।\n"
                            + "• কুরাইশরা সমবেত হলে তিনি বললেন: \"হে কুরাইশগণ! আমি যদি বলি যে এই পাহাড়ের পেছনে একদল শত্রুসেনা লুকিয়ে আছে যারা তোমাদের ওপর আক্রমণ করতে প্রস্তুত, তোমরা কি আমাকে বিশ্বাস করবে?\"\n"
                            + "• তারা সমস্বরে উত্তর দিল: \"হ্যাঁ! আমরা আপনার মুখে কখনো মিথ্যা শুনিনি; আপনি সত্যবাদী।\"\n"
                            + "• তখন রাসূল (সাঃ) ঘোষণা করলেন: \"তাহলে আমি তোমাদের সতর্ক করছি যে, এক আল্লাহর ওপর ঈমান না আনলে তোমাদের জন্য রয়েছে এক কঠিন শাস্তির দিন।\"\n"
                            + "• একথায় ক্ষিপ্ত হয়ে তাঁর আপন চাচা আবু লাহাব বলে উঠল: 'তাব্বাল লাকা সাইরাল ইয়াউম...' (ধ্বংস হও তুমি সারাদিন, এজন্যই কি আমাদের ডেকেছ?)।\n"
                            + "• এর পরিপ্রেক্ষিতে আল্লাহ তা'আলা নাযিল করলেন:\n\n"
                            + "تَبَّتۡ يَدَآ أَبِي لَهَبٖ وَتَبَّ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আবু লাহাবের হস্তদ্বয় ধ্বংস হোক এবং সে নিজেও ধ্বংস হোক।\n"
                            + "<b>[সূরা আল-মাসাদ: ১]</b>\n\n"
                            + "<b>কুরাইশদের আপস প্রস্তাব ও ঐতিহাসিক জবাব:</b>\n"
                            + "কুরাইশ নেতারা রাসূল (সাঃ)-কে অর্থ, সম্পদ, ক্ষমতা ও আরবের সবচেয়ে সুন্দরী নারীর প্রলোভন দেখিয়ে দাওয়াত বন্ধ করার প্রস্তাব দিল। চাচা আবু তালিবের মাধ্যমে আসা এই প্রস্তাবের জবাবে রাসূল (সাঃ) দৃপ্তকণ্ঠে বলেছিলেন:\n"
                            + "<i>\"আল্লাহর শপথ! তারা যদি আমার ডান হাতে সূর্য এবং বাম হাতে চাঁদ এনে দেয়, তবুও আমি এই দ্বীনের দাওয়াত ত্যাগ করব না, যতক্ষণ না আল্লাহ একে বিজয়ী করেন অথবা আমি এতে ধ্বংস হয়ে যাই।\"</i>",
                    "After three years of private dawah, Allah commanded the Prophet (ﷺ) to proclaim the message of Tawheed publicly:\n\n"
                            + "فَٱصۡدَعۡ بِمَا تُؤۡمَرُ وَأَعۡرِضۡ عَنِ ٱلۡمُشۡرِكِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Then declare what you are commanded and turn away from the polytheists.\n"
                            + "<b>[Surah Al-Hijr 15:94]</b>\n\n"
                            + "And He further commanded:\n\n"
                            + "وَأَنذِرۡ عَشِيرَتَكَ ٱلۡأَقۡرَبِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And warn, [O Muhammad], your closest relatives.\n"
                            + "<b>[Surah Ash-Shu'ara 26:214]</b>\n\n"
                            + "<b>The Historic Proclamation at Mount Safa:</b>\n"
                            + "• Ascending Mount Safa, the Prophet (ﷺ) called out the traditional warning cry: \"Ya Sabahah!\"\n"
                            + "• When the clans of Quraish assembled, he asked: \"If I were to tell you that an enemy army was behind this mountain ready to attack you, would you believe me?\"\n"
                            + "• They replied with one voice: \"Yes! We have experienced nothing from you except truthfulness.\"\n"
                            + "• He then declared: \"Then indeed, I am a warner to you before a severe punishment if you do not worship Allah alone.\"\n"
                            + "• Abu Lahab angrily retorted: \"Perish you for the rest of the day! Is it for this that you gathered us?\"\n"
                            + "• In response, Allah revealed:\n\n"
                            + "تَبَّتۡ يَدَآ أَبِي لَهَبٖ وَتَبَّ\n\n"
                            + "<b>Translation:</b>\n"
                            + "May the hands of Abu Lahab be ruined, and ruined is he.\n"
                            + "<b>[Surah Al-Masad 111:1]</b>\n\n"
                            + "<b>Rejection of Quraish's Compromise Offer:</b>\n"
                            + "The Quraish offered immense wealth, kingship, and chieftaincy if he abandoned the message. To his uncle Abu Talib, the Prophet (ﷺ) gave his immortal reply:\n"
                            + "<i>\"By Allah, if they put the sun in my right hand and the moon in my left on condition that I abandon this mission, I would not abandon it until Allah makes it victorious or I perish in the attempt.\"</i>"
            ));

            // 9. কুরাইশদের বিরোধিতা
            list.add(new ProphetOverviewTopicItem(
                    9,
                    "কুরাইশদের বিরোধিতা",
                    "Opposition by Quraish",
                    "প্রকাশ্যে ইসলাম প্রচারের সাথে সাথে মক্কার কুরাইশদের পক্ষ থেকে তীব্র বিরোধিতা, ব্যঙ্গ-বিদ্রূপ ও শারীরিক নির্যাতনের ঝড় নেমে আসে।\n\n"
                            + "<b>অপবাদ ও কুৎসা রটনা:</b>\n"
                            + "• কুরাইশরা রাসূল (সাঃ)-কে জাদুকর, পাগল, কবি ও গণক বলে অপপ্রচার চালাতে থাকে।\n"
                            + "• কুরআনে আল্লাহ্‌ তাদের কুৎসার জবাব দিয়ে বলেন:\n\n"
                            + "مَا صَاحِبُكُم بِمَجۡنُونٖ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তোমাদের সঙ্গী (মুহাম্মদ সাঃ) কোনো পাগল নন।\n"
                            + "<b>[সূরা আত-তাকভীর: ২২]</b>\n\n"
                            + "<b>শারীরিক নির্যাতন ও আক্রমণ:</b>\n"
                            + "• কাবা প্রাঙ্গণে নামাজরত অবস্থায় তাঁর পিঠের ওপর উটের পচা নাড়িভুঁড়ি চাপিয়ে দেওয়া হতো।\n"
                            + "• চলার পথে কাঁটা বিছিয়ে রাখা হতো (আবু লাহাবের স্ত্রী উম্মে জামিল)।\n"
                            + "• গলায় চাদর পেঁচিয়ে শ্বাসরোধ করার অপচেষ্টা চালানো হতো।\n\n"
                            + "<b>হিজরত হাবশা (আবিসিনিয়া):</b>\n"
                            + "কুরাইশদের অমানবিক নির্যাতন চরমে পৌঁছালে রাসূল (সাঃ)-এর নির্দেশে নবুয়তের ৫ম ও ৭ম বর্ষে সাহাবীদের একটি দল ন্যায়পরায়ণ খ্রিস্টান বাদশাহ নাজ্জাশীর দেশ হাবশায় (বর্তমান ইথিওপিয়া) হিজরত করেন।\n\n"
                            + "<b>শিয়াবে আবী তালিবে সামাজিক বর্জন (বয়কট):</b>\n"
                            + "• নবুয়তের ৭ম বর্ষে কুরাইশরা বনু হাশিম ও বনু মুত্তালিব গোত্রকে 'শিয়াবে আবী তালিব' নামক গিরিদরীতে সম্পূর্ণ অবরুদ্ধ করে ফেলে।\n"
                            + "• দীর্ঘ ৩ বছর খাদ্য, পানীয় ও যোগাযোগ বন্ধ করে সামাজিক ও অর্থনৈতিক বয়কট চাপিয়ে দেওয়া হয়। মুসলিমরা গাছের পাতা ও চামড়া খেয়ে জীবন ধারণ করেছিলেন।\n\n"
                            + "<b>তায়েফের বেদনাদায়ক ময়দান:</b>\n"
                            + "• নবুয়তের ১০ম বর্ষে চাচা আবু তালিব ও প্রিয়তমা স্ত্রী খাদিজা (রাঃ) ইন্তেকাল করেন (আমুল হুযন বা দুঃখের বছর)।\n"
                            + "• আশ্রয় ও দাওয়াতের উদ্দেশ্যে তায়েফ গেলে সেখানকার বখাটেরা রাসূল (সাঃ)-এর ওপর অবিরাম পাথর নিক্ষেপ করে রক্তাক্ত করে ফেলে।\n"
                            + "• রক্তাক্ত অবস্থায় পাহাড়ের ফেরেশতা এসে তায়েফবাসীকে দুই পাহাড়ের মাঝে পিষে ফেলার অনুমতি চাইলে রহমতের নবী বলেন: \"না, আমি আশা করি এদের বংশধরদের মধ্যে এমন মানুষ আসবে যারা এক আল্লাহর ইবাদত করবে।\"\n\n"
                            + "<b>আল্লাহ্‌র অভয়বাণী:</b>\n\n"
                            + "إِنَّا كَفَيۡنَـٰكَ ٱلۡمُسۡتَهۡزِءِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয় বিদ্রূপকারীদের বিরুদ্ধে আপনার জন্য আমিই যথেষ্ট।\n"
                            + "<b>[সূরা আল-হিজর: ৯৫]</b>",
                    "With public preaching, the Quraish unleashed intense hostility, mockery, slander, economic warfare, and physical brutality against the Prophet (ﷺ) and his followers.\n\n"
                            + "<b>Slander and Accusations:</b>\n"
                            + "• Polytheists labeled him a sorcerer, poet, soothsayer, and madman.\n"
                            + "• Allah directly refuted their slanders:\n\n"
                            + "مَا صَاحِبُكُم بِمَجۡنُونٖ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Your companion is not [at all] mad.\n"
                            + "<b>[Surah At-Takwir 81:22]</b>\n\n"
                            + "<b>Physical Assaults:</b>\n"
                            + "• Entrails of slaughtered camels were thrown onto his back while he was prostrating in prayer near the Ka'bah.\n"
                            + "• Thorns were strewn along his pathway by Umm Jamil (wife of Abu Lahab).\n"
                            + "• Attempts were made to strangle him with his own cloak.\n\n"
                            + "<b>Migration to Abyssinia (Habashah):</b>\n"
                            + "In the 5th and 7th years of Prophethood, the Prophet (ﷺ) directed vulnerable companions to seek asylum under the righteous Christian King Negus (Ashamah) in Abyssinia.\n\n"
                            + "<b>The 3-Year Boycott in Shi'b Abi Talib:</b>\n"
                            + "• In the 7th year, Quraish imposed a total economic and social boycott against Banu Hashim in the mountain pass of Shi'b Abi Talib.\n"
                            + "• For 3 grueling years, food supplies were blocked, forcing the believers to survive on boiled leather and tree leaves.\n\n"
                            + "<b>The Trial of Ta'if:</b>\n"
                            + "• In the 10th year of Prophethood (Year of Sorrow), both Abu Talib and Sayyidah Khadijah (RA) passed away.\n"
                            + "• Seeking support in Ta'if, the Prophet (ﷺ) was stoned continuously by ruffians until his shoes filled with blood.\n"
                            + "• When the Angel of Mountains offered to crush the city between two mountains, the Prophet of Mercy prayed: \"No, I hope that Allah will bring forth from their descendants those who worship Allah alone.\"\n\n"
                            + "<b>Divine Reassurance:</b>\n\n"
                            + "إِنَّا كَفَيۡنَـٰكَ ٱلۡمُسۡتَهۡزِءِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Indeed, We are sufficient for you against the mockers.\n"
                            + "<b>[Surah Al-Hijr 15:95]</b>"
            ));

            // 10. মিরাজের ঘটনা
            list.add(new ProphetOverviewTopicItem(
                    10,
                    "মিরাজের ঘটনা",
                    "The Event of Miraj",
                    "নবুয়তের একাদশ/দ্বাদশ বর্ষে, তায়েফের চরম আঘাত ও শোকের পর, আল্লাহ তা'আলা তাঁর প্রিয় হাবীব সাল্লাল্লাহু আলাইহি ওয়া সাল্লামকে সান্ত্বনা ও সর্বোচ্চ সম্মান প্রদর্শনের জন্য ঐতিহাসিক 'ইসরা ও মি'রাজ'-এর অলৌকিক সফর করান।\n\n"
                            + "<b>ইসরা ও মি'রাজ কী?</b>\n"
                            + "• <b>ইসরা:</b> মসজিদুল হারাম (মক্কা) থেকে মসজিদুল আকসা (বায়তুল মুকাদ্দাস, জেরুজালেম) পর্যন্ত রাত্রিকালীন ভ্রমণ।\n"
                            + "• <b>মি'রাজ:</b> মসজিদুল আকসা থেকে ঊর্ধ্বাকাশ, সপ্ত আকাশ ও সিদরাতুল মুনতাহা হয়ে আল্লাহ তা'আলার দিদার লাভ পর্যন্ত মহামান্বিত ঊর্ধ্বারোহণ।\n\n"
                            + "<b>কুরআনের সুস্পষ্ট ঘোষণা:</b>\n\n"
                            + "سُبۡحَـٰنَ ٱلَّذِيٓ أَسۡرَىٰ بِعَبۡدِهِۦ لَيۡلٗا مِّنَ ٱلۡمَسۡجِدِ ٱلۡحَرَامِ إِلَى ٱلۡمَسۡجِدِ ٱلۡأَقۡصَا ٱلَّذِي بَـٰرَكۡنَا حَوۡلَهُۥ لِنُرِيَهُۥ مِنۡ ءَايَـٰتِنَآۚ إِنَّهُۥ هُوَ ٱلسَّمِيعُ ٱلۡبَصِيرُ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "পরম পবিত্র ও মহিমাময় সত্তা তিনি, যিনি স্বীয় বান্দাকে রাতের বেলা ভ্রমণ করিয়েছিলেন মসজিদুল হারাম থেকে মসজিদুল আকসা পর্যন্ত—যার চারপাশকে আমি বরকতময় করেছি, যাতে আমি তাঁকে আমার নিদর্শনসমূহ প্রদর্শন করতে পারি। নিশ্চয় তিনি সর্বশ্রোতা, সর্বদ্রষ্টা।\n"
                            + "<b>[সূরা আল-ইসরা: ১]</b>\n\n"
                            + "<b>বায়তুল মুকাদ্দাসে সকল নবীর ইমামতি:</b>\n"
                            + "• 'বুরাক' নামক দ্রুতগামী বাহনে চড়ে রাসূল (সাঃ) বায়তুল মুকাদ্দাসে পৌঁছেন।\n"
                            + "• সেখানে তিনি হযরত আদম (আঃ) থেকে শুরু করে সকল নবী-রাসূলগণের জামাতে ইমামতি করে নামাজ আদায় করেন, যার মাধ্যমে তিনি <b>'সায়্যিদুল মুরসালীন'</b> (সকল রাসুলের নেতা) হিসেবে স্বীকৃতি লাভ করেন।\n\n"
                            + "<b>সপ্ত আকাশ ভ্রমণ ও নবীদের সাথে সাক্ষাৎ:</b>\n"
                            + "জিব্রাইল (আঃ)-এর সাথে তিনি ক্রমান্বয়ে সাত আকাশ ভ্রমণ করেন:\n"
                            + "• <b>১ম আকাশ:</b> হযরত আদম (আঃ)\n"
                            + "• <b>২য় আকাশ:</b> হযরত ঈসা (আঃ) ও হযরত ইয়াহইয়া (আঃ)\n"
                            + "• <b>৩য় আকাশ:</b> হযরত ইউসুফ (আঃ)\n"
                            + "• <b>৪র্থ আকাশ:</b> হযরত ইদরিস (আঃ)\n"
                            + "• <b>৫ম আকাশ:</b> হযরত হারুন (আঃ)\n"
                            + "• <b>৬ষ্ঠ আকাশ:</b> হযরত মূসা (আঃ)\n"
                            + "• <b>৭ম আকাশ:</b> হযরত ইব্রাহিম (আঃ)—যিনি বায়তুল মামুরে হেলান দিয়ে উপবিষ্ট ছিলেন।\n\n"
                            + "<b>সিদরাতুল মুনতাহা ও জান্নাত-জাহান্নাম দর্শন:</b>\n"
                            + "• সিদরাতুল মুনতাহা পর্যন্ত জিব্রাইল (আঃ) তাঁর সাথে ছিলেন। অতঃপর রাসূল (সাঃ) একাকী আল্লাহর নূরানী সান্নিধ্যে পৌঁছেন।\n"
                            + "• আল্লাহ্‌ বলেন:\n\n"
                            + "وَلَقَدۡ رَءَاهُ نَزۡلَةً أُخۡرَىٰ • عِندَ سِدۡرَةِ ٱلۡمُنتَهَىٰ • عِندَهَا جَنَّةُ ٱلۡمَأۡوَىٰٓ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আর নিশ্চয় তিনি তাঁকে আরেকবার দেখেছিলেন—সিদরাতুল মুনতাহার নিকট, যার কাছে রয়েছে জান্নাতুল মা'ওয়া।\n"
                            + "<b>[সূরা আন-নাজম: ১৩-১৫]</b>\n\n"
                            + "<b>উম্মতের জন্য শ্রেষ্ঠ উপহার: ৫ ওয়াক্ত নামাজ:</b>\n"
                            + "• মি'রাজের এই রাতে আল্লাহ তা'আলা মুসলিম উম্মাহর ওপর প্রতিদিন <b>৫ ওয়াক্ত নামাজ</b> ফরজ করেন (যা শুরুতে ৫০ ওয়াক্ত ছিল এবং মূসা আঃ-এর পরামর্শে কমানো হয়)।\n"
                            + "• সূরা আল-বাকারার শেষ দুই আয়াত এবং শিরক না করা উম্মতদের ক্ষমার সুসংবাদ দেওয়া হয়।\n"
                            + "• মিরাজ শেষে হযরত আবু বকর (রাঃ) দ্বিধাহীন চিত্তে এই অলৌকিক ভ্রমণকে সত্য বলে বিশ্বাস করায় রাসূল (সাঃ) তাঁকে <b>'আস-সিদ্দিক'</b> (মহাসত্যবাদী) উপাধিতে ভূষিত করেন।",
                    "In the 11th/12th year of Prophethood, following the heart-wrenching trials at Ta'if and the Year of Sorrow, Allah blessed His Beloved Messenger (ﷺ) with the miraculous nocturnal journey of Isra and Mi'raj.\n\n"
                            + "<b>What are Isra and Mi'raj?</b>\n"
                            + "• <b>Al-Isra:</b> The miraculous night journey from Al-Masjid al-Haram in Makkah to Al-Masjid al-Aqsa in Jerusalem.\n"
                            + "• <b>Al-Mi'raj:</b> The divine ascension from Al-Masjid al-Aqsa through the seven heavens up to Sidrat al-Muntaha into the direct presence of Allah.\n\n"
                            + "<b>Quranic Declaration:</b>\n\n"
                            + "سُبۡحَـٰنَ ٱلَّذِيٓ أَسۡرَىٰ بِعَبۡدِهِۦ لَيۡلٗا مِّنَ ٱلۡمَسۡجِدِ ٱلۡحَرَامِ إِلَى ٱلۡمَسۡجِدِ ٱلۡأَقۡصَا ٱلَّذِي بَـٰرَكۡنَا حَوۡلَهُۥ لِنُرِيَهُۥ مِنۡ ءَايَـٰتِنَآۚ إِنَّهُۥ هُوَ ٱلسَّمِيعُ ٱلۡبَصِيرُ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Exalted is He who took His Servant by night from al-Masjid al-Haram to al-Masjid al-Aqsa, whose surroundings We have blessed, to show him of Our signs. Indeed, He is the Hearing, the Seeing.\n"
                            + "<b>[Surah Al-Isra 17:1]</b>\n\n"
                            + "<b>Leading all Prophets in Prayer at Al-Aqsa:</b>\n"
                            + "• Riding the heavenly steed 'Al-Buraq' accompanied by Jibreel (AS), the Prophet reached Bayt al-Maqdis.\n"
                            + "• He led all previous prophets in congregational prayer, affirming his station as <b>Sayyid al-Mursalin</b> (Leader of all Messengers).\n\n"
                            + "<b>Ascension through the Seven Heavens:</b>\n"
                            + "Ascending through the heavens, he met:\n"
                            + "• <b>1st Heaven:</b> Prophet Adam (AS)\n"
                            + "• <b>2nd Heaven:</b> Prophet Isa (AS) & Prophet Yahya (AS)\n"
                            + "• <b>3rd Heaven:</b> Prophet Yusuf (AS)\n"
                            + "• <b>4th Heaven:</b> Prophet Idris (AS)\n"
                            + "• <b>5th Heaven:</b> Prophet Harun (AS)\n"
                            + "• <b>6th Heaven:</b> Prophet Musa (AS)\n"
                            + "• <b>7th Heaven:</b> Prophet Ibrahim (AS), reclining against Bayt al-Ma'mur.\n\n"
                            + "<b>Sidrat al-Muntaha and Divine Presence:</b>\n"
                            + "• At Sidrat al-Muntaha (The Lote Tree of the Utmost Boundary), Jibreel halted, and the Prophet (ﷺ) ascended into the Divine Presence.\n"
                            + "• Allah states:\n\n"
                            + "وَلَقَدۡ رَءَاهُ نَزۡلَةً أُخۡرَىٰ • عِندَ سِدۡرَةِ ٱلۡمُنتَهَىٰ • عِندَهَا جَنَّةُ ٱلۡمَأۡوَىٰٓ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And he certainly saw him in another descent, At the Lote Tree of the Utmost Boundary - Near it is the Garden of Refuge.\n"
                            + "<b>[Surah An-Najm 53:13-15]</b>\n\n"
                            + "<b>The Divine Gift: Five Daily Prayers:</b>\n"
                            + "• Allah decreed the <b>five daily prayers (Salah)</b> as the spiritual ascension for every believer.\n"
                            + "• The concluding verses of Surah Al-Baqarah and forgiveness for those who avoid Shirk were granted.\n"
                            + "• Upon returning, Abu Bakr (RA) affirmed the truth of this journey without hesitation, earning the title <b>'As-Siddiq'</b> (The Truthful)."
            ));
        } else if (prophetId == 13) {
            // হযরত মুহাম্মাদ (সাঃ) - মাদানী জীবন (All 12 Chapters / Topics 100% Verbatim from User Screenshots & Request)

            // 1. মদিনায় হিজরত
            list.add(new ProphetOverviewTopicItem(
                    1,
                    "মদিনায় হিজরত",
                    "Migration to Madinah",
                    "হিজরত বলতে বোঝায় মক্কা থেকে মদিনায় হযরত মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) এবং সাহাবীদের স্থানান্তর। এটি ইসলামের ইতিহাসে একটি গুরুত্বপূর্ণ মোড়। মক্কায় তাওহীদের দাওয়াত প্রচার করার সময় কুরাইশদের দ্বারা নির্যাতন ও অত্যাচারের শিকার হওয়ার কারণে নবীজী (সাঃ) ও সাহাবীদের হিজরত করতে বাধ্য হতে হয়। মদিনায় ইসলাম দ্রুত বিকাশ লাভ করে, এবং এটি মুসলিম রাষ্ট্র প্রতিষ্ঠার সূচনা হয়।\n\n"
                            + "<b>হিজরতের কারণ:</b>\n\n"
                            + "<b>কুরাইশদের অত্যাচার ও নিপীড়ন:</b>\n"
                            + "মক্কার কুরাইশ নেতারা নবীজির দাওয়াতের বিরোধিতা করত। তারা মুসলমানদের উপর শারীরিক, মানসিক ও সামাজিক অত্যাচার চালাত।\n\n"
                            + "<b>ইসলাম প্রচারে বাধা:</b>\n"
                            + "নবীজির দাওয়াত মক্কায় প্রচার করা কঠিন হয়ে পড়েছিল। কুরাইশরা তার অনুসারীদের নামাজ পড়া, কুরআন তিলাওয়াত এবং ইসলামী জীবনযাপন করতে বাধা দিত।\n\n"
                            + "<b>মদিনায় দাওয়াত গ্রহণের ইতিবাচক সাড়া:</b>\n"
                            + "মদিনার আওস ও খাজরাজ গোত্রের লোকেরা ইসলাম গ্রহণ করেছিল এবং নবীজিকে তাদের শহরে আমন্ত্রণ জানিয়েছিল। তারা প্রতিশ্রুতি দেয় যে তারা নবীজিকে এবং মুসলমানদের রক্ষা করবে।\n\n"
                            + "<b>নিরাপদ পরিবেশের প্রয়োজন:</b>\n"
                            + "মদিনায় ইসলাম প্রচার এবং একটি সংগঠিত মুসলিম সমাজ প্রতিষ্ঠার জন্য নিরাপদ পরিবেশের প্রয়োজন ছিল।\n\n"
                            + "<b>হিজরতের গুরুত্বপূর্ণ ঘটনাবলি:</b>\n\n"
                            + "<b>কুরাইশদের হত্যার ষড়যন্ত্র:</b>\n"
                            + "কুরাইশরা নবীজিকে (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) হত্যা করার পরিকল্পনা করে। তারা একসঙ্গে ১০ জন যুবক নির্ধারণ করে, যারা নবীজিকে একযোগে আক্রমণ করবে।\n"
                            + "<b>[সীরাত ইবনে হিশাম]</b>\n\n"
                            + "<b>আলী (রাঃ)-কে নবীজির স্থানে শোয়ানো:</b>\n"
                            + "হিজরতের রাতে নবীজি (সাঃ) হযরত আলী (রাঃ)-কে তার শয্যায় শোয়ার নির্দেশ দেন। আল্লাহর নির্দেশে তিনি কুরাইশদের আক্রমণ থেকে রক্ষা পান।\n"
                            + "<b>[সহীহ বুখারী]</b>\n\n"
                            + "<b>গারে সাওরের আশ্রয়:</b>\n"
                            + "নবীজি (সাঃ) এবং হযরত আবু বকর (রাঃ) মক্কা থেকে প্রস্থানের পর গারে সাওর নামক গুহায় তিন দিন আশ্রয় নেন। কুরাইশরা তাদের খুঁজে বের করার চেষ্টা করে, কিন্তু তারা ব্যর্থ হয়।\n"
                            + "আল্লাহর সাহায্যে তাদের উপর শান্তি নাজিল হয়, এবং তিনি তাদের শত্রুদের চোখ থেকে আড়াল করেন।\n"
                            + "<b>[সূরা আত-তাওবা: ৪০]</b>\n\n"
                            + "<b>আবু বকরের (রাঃ) সাহচর্য:</b>\n"
                            + "হিজরতের সময় আবু বকর (রাঃ) নবীজির সঙ্গে ছিলেন। তিনি নবীজির প্রতি অগাধ ভালোবাসা ও ভক্তির উদাহরণ স্থাপন করেন।\n\n"
                            + "<b>মদিনায় আগমন:</b>\n"
                            + "নবীজি (সাঃ) ১২ রবিউল আউয়াল মদিনায় পৌঁছান। মদিনার লোকেরা উষ্ণ অভ্যর্থনা জানায় এবং ইসলামের নবযুগ শুরু হয়।\n\n"
                            + "<b>মদিনায় হিজরতের পরিণাম ও গুরুত্ব:</b>\n\n"
                            + "<b>ইসলামের প্রথম রাষ্ট্র প্রতিষ্ঠা:</b>\n"
                            + "মদিনায় পৌঁছার পর নবীজি ইসলামের ভিত্তি স্থাপন করেন। এটি মুসলমানদের জন্য একটি সাংগঠনিক কেন্দ্র হয়ে ওঠে।\n\n"
                            + "<b>মুসলিম ও অমুসলিমদের মধ্যে চুক্তি:</b>\n"
                            + "মদিনা সনদ: মদিনার সকল ধর্মীয় ও গোত্রীয় গোষ্ঠীর মধ্যে চুক্তি হয়। এটি ইসলামের ইতিহাসে প্রথম লিখিত সংবিধান হিসেবে পরিচিত। মদিনা সনদের মাধ্যমে বিভিন্ন সম্প্রদায়ের মধ্যে শান্তি ও সম্প্রীতি স্থাপন করা হয়।\n\n"
                            + "<b>নামাজ ও ইসলামের বিধি-বিধান প্রতিষ্ঠা:</b>\n"
                            + "মদিনায় মুসলমানদের জন্য নামাজসহ ইসলামের বিভিন্ন বিধি-বিধান প্রবর্তন করা হয়।\n\n"
                            + "<b>ইসলামের বিস্তার:</b>\n"
                            + "মদিনা থেকে ইসলাম ধীরে ধীরে আরব উপদ্বীপ এবং এর বাইরেও বিস্তার লাভ করে।\n\n"
                            + "<b>কুরআনে হিজরতের উল্লেখ, আল্লাহর সাহায্যের প্রতিশ্রুতি:</b>\n\n"
                            + "إِلَّا تَنصُرُوهُ فَقَدْ نَصَرَهُ ٱللَّهُ إِذْ أَخْرَجَهُ ٱلَّذِينَ كَفَرُوا۟ ثَانِىَ ٱثْنَيْنِ إِذْ هُمَا فِى ٱلْغَارِ إِذْ يَقُولُ لِصَٰحِبِهِۦ لَا تَحْزَنْ إِنَّ ٱللَّهَ مَعَنَا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যদি তোমরা তাকে (নবীকে) সাহায্য না করো, তবে আল্লাহ তাকে সাহায্য করেছেন, যখন কাফিররা তাকে বের করে দিয়েছিল এবং সে ছিল দুইজনের একজন, যখন তারা গুহায় ছিল। তিনি তার সঙ্গীকে বললেন, ‘চিন্তা করো না, নিশ্চয়ই আল্লাহ আমাদের সঙ্গে আছেন।\n"
                            + "<b>[সূরা আত-তাওবা: ৪০]</b>\n\n"
                            + "<b>মক্কা ছাড়ার কষ্ট:</b>\n\n"
                            + "إِنَّ ٱلَّذِى فَرَضَ عَلَيْكَ ٱلْقُرْءَانَ لَرَآدُّكَ إِلَىٰ مَعَادٍ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যিনি তোমার উপর কুরআন নাজিল করেছেন, তিনিই তোমাকে তোমার স্থানে ফিরিয়ে আনবেন।\n"
                            + "<b>[সূরা আল-কাসাস: ৮৫]</b>",
                    "Hijrah refers to the momentous migration of Prophet Muhammad (peace and blessings be upon him) and his noble companions from Makkah to Madinah. This marked a turning point in Islamic history. Enduring relentless persecution from the Quraish in Makkah, the Prophet (ﷺ) and believers migrated under divine command to establish a secure, organized Islamic society and state in Madinah.\n\n"
                            + "<b>Causes of the Hijrah:</b>\n\n"
                            + "<b>Persecution by Quraish:</b>\n"
                            + "Quraish chieftains vehemently opposed the Islamic call, inflicting severe physical, psychological, and social brutality upon the Muslims.\n\n"
                            + "<b>Obstruction of Dawah:</b>\n"
                            + "Spreading Islam became extremely restricted in Makkah as polytheists prohibited prayer, Quranic recitation, and Islamic life.\n\n"
                            + "<b>Positive Response from Madinah:</b>\n"
                            + "The Aws and Khazraj tribes of Yathrib accepted Islam at the Pledges of Aqabah, inviting the Prophet (ﷺ) and pledging their lives to protect him and the believers.\n\n"
                            + "<b>Need for a Safe Sanctuary:</b>\n"
                            + "A secure base was essential for propagating Islam freely and cultivating an organized Muslim community.\n\n"
                            + "<b>Key Events of the Hijrah:</b>\n\n"
                            + "<b>Assassination Conspiracy:</b>\n"
                            + "Quraish assembled 10 fierce youths representing all major clans to strike the Prophet (ﷺ) simultaneously with swords.\n"
                            + "<b>[Sirah Ibn Hisham]</b>\n\n"
                            + "<b>Ali (RA) Sleeping on the Prophet's Bed:</b>\n"
                            + "On the night of departure, the Prophet (ﷺ) instructed Ali (RA) to sleep in his cloak and bed to return trust items (Amanat) safely.\n"
                            + "<b>[Sahih al-Bukhari]</b>\n\n"
                            + "<b>Sanctuary in Cave Thawr:</b>\n"
                            + "The Prophet (ﷺ) and Abu Bakr (RA) took refuge in Cave Thawr for three days. Despite trackers reaching the mouth of the cave, Allah shielded them from enemy eyes.\n"
                            + "<b>[Surah At-Tawbah: 40]</b>\n\n"
                            + "<b>Devotion of Abu Bakr (RA):</b>\n"
                            + "Abu Bakr (RA) accompanied the Prophet (ﷺ), demonstrating unmatched loyalty, courage, and unconditional love.\n\n"
                            + "<b>Arrival in Madinah:</b>\n"
                            + "The Prophet (ﷺ) reached Madinah on 12th Rabi' al-Awwal, welcomed with joyful celebrations, inaugurating the Islamic calendar and a new era.\n\n"
                            + "<b>Impact and Significance:</b>\n\n"
                            + "<b>Establishment of the First Islamic State:</b>\n"
                            + "Madinah became the foundational spiritual, legal, and political center for Islam.\n\n"
                            + "<b>The Charter of Madinah:</b>\n"
                            + "A historic treaty uniting Muslims, Jews, and other tribes—recognized as humanity's first written constitution guaranteeing peace and freedom of religion.\n\n"
                            + "<b>Institutionalizing Islamic Worship and Law:</b>\n"
                            + "Regular congregational Salah, social welfare systems, and legislative rulings were fully established.\n\n"
                            + "<b>Spread of Islam:</b>\n"
                            + "From Madinah, the light of Islam illuminated the entire Arabian Peninsula and beyond.\n\n"
                            + "<b>Divine Help in the Quran:</b>\n\n"
                            + "إِلَّا تَنصُرُوهُ فَقَدْ نَصَرَهُ ٱللَّهُ إِذْ أَخْرَجَهُ ٱلَّذِينَ كَفَرُوا۟ ثَانِىَ ٱثْنَيْنِ إِذْ هُمَا فِى ٱلْغَارِ إِذْ يَقُولُ لِصَٰحِبِهِۦ لَا تَحْزَنْ إِنَّ ٱللَّهَ مَعَنَا\n\n"
                            + "<b>Translation:</b>\n"
                            + "If you do not aid him - Allah has already aided him when those who disbelieved had driven him out as one of two, when they were in the cave and he said to his companion, 'Do not grieve; indeed Allah is with us.'\n"
                            + "<b>[Surah At-Tawbah 9:40]</b>\n\n"
                            + "<b>Consolation upon Leaving Makkah:</b>\n\n"
                            + "إِنَّ ٱلَّذِى فَرَضَ عَلَيْكَ ٱلْقُرْءَانَ لَرَآدُّكَ إِلَىٰ مَعَادٍ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Indeed, [O Muhammad], He who imposed upon you the Quran will take you back to a place of return.\n"
                            + "<b>[Surah Al-Qasas 28:85]</b>"
            ));

            // 2. মদিনার জীবন এবং মসজিদে নববী
            list.add(new ProphetOverviewTopicItem(
                    2,
                    "মদিনার জীবন এবং মসজিদে নববী",
                    "Life in Madinah and Masjid an-Nabawi",
                    "মদিনায় পৌঁছানোর পর হযরত মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) একটি শক্তিশালী এবং সুসংগঠিত মুসলিম সমাজ গড়ে তোলেন। এটি ইসলামের একটি নতুন অধ্যায়ের সূচনা ছিল, যেখানে ইসলাম শুধুমাত্র একটি বিশ্বাস নয় বরং একটি জীবনব্যবস্থা হিসেবে প্রতিষ্ঠিত হয়েছিল।\n\n"
                            + "وَمَن يُهَاجِرْ فِي سَبِيلِ ٱللَّهِ يَجِدْ فِي ٱلْأَرْضِ مُرَٰغَمًا كَثِيرًا وَسَعَةً ۚ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যে ব্যক্তি আল্লাহর পথে হিজরত করে, সে পৃথিবীতে প্রচুর আশ্রয় ও সমৃদ্ধি খুঁজে পাবে।\n"
                            + "<b>[সূরা আন-নিসা: ১০০]</b>\n\n"
                            + "<b>মসজিদে নববীর নির্মাণ:</b>\n"
                            + "মদিনায় পৌঁছে নবীজি (সাঃ) প্রথম যে কাজটি করেন, তা হলো মসজিদে নববী নির্মাণ। এটি ছিল ইসলামের প্রথম সামাজিক, রাজনৈতিক এবং ধর্মীয় কেন্দ্র।\n\n"
                            + "• <b>জমি নির্বাচন:</b> মসজিদটি নির্মাণের জন্য দুই ইয়াতিম ছেলের জমি কেনা হয়।\n"
                            + "• <b>নির্মাণে অংশগ্রহণ:</b> নবীজি নিজে সাহাবীদের সঙ্গে কাঁধে কাঁধ মিলিয়ে মসজিদ নির্মাণ করেন।\n"
                            + "• <b>আকৃতিঃ</b> প্রাথমিকভাবে মসজিদটি মাটির ইট দিয়ে তৈরি হয় এবং ছাদ ছিল খেজুর পাতা দিয়ে আচ্ছাদিত।\n"
                            + "• <b>ব্যবহার:</b> মসজিদ শুধু নামাজের স্থান নয়, এটি ছিল শিক্ষাদান, পরামর্শ এবং সমাজ পরিচালনার কেন্দ্র।\n\n"
                            + "<b>মুহাজির ও আনসারদের ভ্রাতৃত্ব স্থাপন:</b>\n"
                            + "মদিনায় আসার পর মুহাজির (মক্কার শরণার্থী) এবং আনসার (মদিনার স্থানীয় মুসলমান)দের মধ্যে ভ্রাতৃত্ব স্থাপন করা হয়।\n"
                            + "• মুহাজিররা তাদের ঘরবাড়ি, সম্পত্তি ছেড়ে মক্কা থেকে এসেছিল।\n"
                            + "• আনসাররা তাদের সম্পত্তি, ঘর এবং সম্পদ মুহাজিরদের সঙ্গে ভাগ করে নেয়।\n"
                            + "<b>[সহীহ বুখারী]</b>\n\n"
                            + "<b>মদিনা সনদ, ইসলামের প্রথম লিখিত সংবিধান:</b>\n"
                            + "মদিনায় নবীজির নেতৃত্বে মদিনা সনদ প্রণয়ন করা হয়, যা ইসলামের প্রথম লিখিত সংবিধান।\n"
                            + "• <b>উদ্দেশ্য:</b> মদিনার বিভিন্ন ধর্মীয় ও গোত্রীয় গোষ্ঠীর মধ্যে শান্তি ও সহাবস্থান প্রতিষ্ঠা করা।\n"
                            + "• <b>মূলনীতি:</b> মুসলিম ও অমুসলিমদের মধ্যে পারস্পরিক সহযোগিতা, ধর্মীয় স্বাধীনতা এবং আইন প্রতিষ্ঠা।\n\n"
                            + "<b>নামাজের নির্দেশনা ও সামাজিক বিধি প্রবর্তন:</b>\n"
                            + "মদিনায় ইসলামের বিভিন্ন সামাজিক ও ধর্মীয় বিধি-বিধান প্রবর্তন করা হয়:\n"
                            + "• নামাজ, রোজা, যাকাত, এবং হজের বিধান।\n"
                            + "• পারিবারিক জীবন, বাণিজ্য, এবং অপরাধমূলক বিচার সংক্রান্ত আইন।\n\n"
                            + "<b>মদিনায় প্রতিরক্ষা ব্যবস্থার গঠন:</b>\n"
                            + "• মদিনায় বসবাসকারী ইহুদী ও অন্যান্য গোষ্ঠীর সঙ্গে নিরাপত্তা চুক্তি।\n"
                            + "• বাইরের আক্রমণ প্রতিরোধের জন্য সামরিক প্রস্তুতি।\n"
                            + "• বদর, উহুদ এবং খন্দকের মতো যুদ্ধ।\n\n"
                            + "<b>ইসলামের কেন্দ্রবিন্দু, মসজিদে নববীর গুরুত্ব:</b>\n"
                            + "মসজিদে নববী ছিল শুধুমাত্র নামাজের স্থান নয়; এটি ছিল ইসলামের কেন্দ্রবিন্দু। এটি মুসলিম সমাজের ধর্মীয়, সামাজিক, রাজনৈতিক এবং শিক্ষামূলক কার্যক্রম পরিচালনার জন্য ব্যবহৃত হতো।\n\n"
                            + "<b>মসজিদে নববীর ভূমিকা:</b>\n\n"
                            + "عَنْ أَبِي هُرَيْرَةَ (رَضِيَ اللَّهُ عَنْهُ) قَالَ: قَالَ رَسُولُ اللَّهِ (صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ): صَلَاةٌ فِي مَسْجِدِي هَذَا خَيْرٌ مِنْ أَلْفِ صَلَاةٍ فِي مَا سِوَاهُ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আবু হুরাইরা (রাঃ) থেকে বর্ণিত, রাসূলুল্লাহ (সাঃ) বলেছেন, ‘আমার এই মসজিদে এক রাকাত নামাজ অন্য মসজিদে ১,০০০ রাকাত নামাজের চেয়ে উত্তম।\n"
                            + "<b>[সহীহ বুখারী: ১১৯০]</b>\n\n"
                            + "• <b>শিক্ষা কেন্দ্র:</b> নবীজি (সাঃ) মসজিদে নববীতে সাহাবীদের শিক্ষা দিতেন। নতুন মুসলমানদের কুরআন শেখানো। ইসলামী আইন ও জীবনযাপনের নিয়ম-কানুন শেখানো।\n"
                            + "• <b>সামাজিক সমস্যা সমাধান:</b> মসজিদে নববীতে সমাজের বিভিন্ন সমস্যা নিয়ে আলোচনা করা হতো এবং সমাধান দেওয়া হতো।\n"
                            + "• <b>রাজনৈতিক সিদ্ধান্তের কেন্দ্র:</b> মসজিদে নববী থেকে নবীজি (সাঃ) মুসলিম সমাজ পরিচালনা করতেন।\n"
                            + "• <b>অতিথি গ্রহণ ও আশ্রয়:</b> মসজিদে নববীর পাশে \"আহলে সুফফা\" নামে একটি স্থান নির্ধারণ করা হয়েছিল, যেখানে দরিদ্র ও অতিথিদের আশ্রয় দেওয়া হতো।\n\n"
                            + "<b>মসজিদে নববীর সম্প্রসারণ:</b>\n"
                            + "নবীজির জীবদ্দশায় মসজিদের আকার ছোট ছিল। পরবর্তীতে খলিফা উমর (রাঃ) এবং উসমান (রাঃ)-এর শাসনামলে এটি সম্প্রসারণ করা হয়। বর্তমান মসজিদে নববী সৌদি আরবের উদ্যোগে বৃহৎ আকারে উন্নয়ন লাভ করেছে।\n\n"
                            + "<b>মদিনার জীবনের বিশেষ দিকসমূহ:</b>\n"
                            + "<b>ইসলামের আইন প্রতিষ্ঠা:</b> নবীজি (সাঃ) ইসলামী আইন বাস্তবায়নের মাধ্যমে মদিনায় একটি শান্তিপূর্ণ সমাজ গঠন করেন।",
                    "Upon arriving in Madinah, Prophet Muhammad (ﷺ) established a cohesive and thriving Islamic society. This inaugurated a momentous chapter where Islam emerged not merely as private faith, but as a complete system of life and governance.\n\n"
                            + "وَمَن يُهَاجِرْ فِي سَبِيلِ ٱللَّهِ يَجِدْ فِي ٱلْأَرْضِ مُرَٰغَمًا كَثِيرًا وَسَعَةً ۚ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And whoever emigrates for the cause of Allah will find on the earth many an alternative location and abundance.\n"
                            + "<b>[Surah An-Nisa 4:100]</b>\n\n"
                            + "<b>Construction of Masjid an-Nabawi:</b>\n"
                            + "The Prophet's first monumental endeavor was constructing the Prophet's Mosque—the spiritual, social, and administrative epicenter of Islam.\n"
                            + "• <b>Land Selection:</b> Purchased from two orphan youths (Sahl and Suhayl).\n"
                            + "• <b>Prophetic Labor:</b> The Prophet (ﷺ) personally carried mud bricks alongside companions.\n"
                            + "• <b>Original Structure:</b> Sun-dried clay bricks with palm-trunk pillars and palm-leaf thatch roof.\n"
                            + "• <b>Multifunctional Role:</b> House of prayer, academy of learning, court of justice, and seat of government.\n\n"
                            + "<b>Fraternity (Muwakhat) between Muhajirun and Ansar:</b>\n"
                            + "A divine bond of brotherhood was forged between the Makkan refugees (Muhajirun) and Madinan hosts (Ansar).\n"
                            + "• Muhajirun arrived having forfeited their homes and wealth.\n"
                            + "• Ansar shared their houses, lands, and wealth with boundless selflessness.\n"
                            + "<b>[Sahih al-Bukhari]</b>\n\n"
                            + "<b>The Charter of Madinah (Dustur al-Madinah):</b>\n"
                            + "The Prophet (ﷺ) established humanity's first written constitution:\n"
                            + "• <b>Objective:</b> Foster peace, mutual security, and pluralistic coexistence.\n"
                            + "• <b>Principles:</b> Freedom of worship, collective defense against aggression, and universal justice.\n\n"
                            + "<b>Legislative and Social Foundations:</b>\n"
                            + "Gradual revelation of core Islamic obligations:\n"
                            + "• Injunctions of Salah (Adhan introduced), Sawm (Ramadan), Zakat, and Hajj.\n"
                            + "• Comprehensive legal codes for trade, family relations, and criminal justice.\n\n"
                            + "<b>Strategic Defense Infrastructure:</b>\n"
                            + "• Security alliances with neighboring tribes and Jewish clans.\n"
                            + "• Military defense against external aggression (Battles of Badr, Uhud, and Khandaq).\n\n"
                            + "<b>Spiritual Virtues of Masjid an-Nabawi:</b>\n\n"
                            + "عَنْ أَبِي هُرَيْرَةَ (رَضِيَ اللَّهُ عَنْهُ) قَالَ: قَالَ رَسُولُ اللَّهِ (صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ): صَلَاةٌ فِي مَسْجِدِي هَذَا خَيْرٌ مِنْ أَلْفِ صَلَاةٍ فِي مَا سِوَاهُ.\n\n"
                            + "<b>Translation:</b>\n"
                            + "Narrated by Abu Hurairah (RA), the Messenger of Allah (ﷺ) said: 'One prayer in this mosque of mine is better than a thousand prayers anywhere else, except Al-Masjid al-Haram.'\n"
                            + "<b>[Sahih al-Bukhari: 1190]</b>\n\n"
                            + "• <b>Center of Learning:</b> Continuous instruction in Quran and wisdom.\n"
                            + "• <b>Social Welfare:</b> Resolving community disputes and distributing charity.\n"
                            + "• <b>Sanctuary of Suffah:</b> The shaded platform (Ahle Suffah) sheltering impoverished students and travellers.\n\n"
                            + "<b>Expansions of the Mosque:</b>\n"
                            + "Enlarged under Caliphs Umar and Uthman (RA), followed by grand modern expansions into the magnificent architectural sanctuary seen today."
            ));

            // 3. তায়েফ সফর
            list.add(new ProphetOverviewTopicItem(
                    3,
                    "তায়েফ সফর",
                    "The Journey to Ta'if",
                    "তায়েফ সফর ইসলামের ইতিহাসে একটি গুরুত্বপূর্ণ অধ্যায়। হযরত মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) মক্কায় তাওহীদের দাওয়াত দেওয়ার সময় কুরাইশদের তীব্র বিরোধিতা ও অত্যাচারের শিকার হন। তখন তিনি ইসলাম প্রচারের জন্য নতুন জায়গা খুঁজতে শুরু করেন। এর অংশ হিসেবে তিনি তায়েফ সফর করেন, যা ছিল মক্কার প্রায় ৬০ কিলোমিটার দক্ষিণ-পূর্বে অবস্থিত।\n\n"
                            + "<b>তায়েফ সফরের উদ্দেশ্য:</b>\n\n"
                            + "• তায়েফের প্রধান গোত্রদের কাছে ইসলামের দাওয়াত পৌঁছে দেওয়া।\n"
                            + "• তায়েফবাসীদের সমর্থন লাভ করা, যাতে তারা নবীজিকে (সাঃ) এবং ইসলামের বার্তাকে গ্রহণ করে।\n"
                            + "• মক্কার বাইরে একটি নিরাপদ আশ্রয়স্থল খুঁজে পাওয়া, যেখানে ইসলাম প্রচারের পথ উন্মুক্ত হবে।\n\n"
                            + "<b>তায়েফে পৌঁছানো:</b>\n\n"
                            + "নবীজি (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) এবং তার দাস জায়েদ ইবনে হারিসা (রাঃ) একত্রে তায়েফে যান। তিনি তায়েফের তিন প্রধান নেতার কাছে দাওয়াত দেন:\n\n"
                            + "• আবদ ইয়ালাইল।\n"
                            + "• মাসউদ।\n"
                            + "• হাবিব।\n\n"
                            + "<b>নেতাদের প্রতিক্রিয়া:</b>\n\n"
                            + "তায়েফের নেতারা নবীজির দাওয়াত গ্রহণ করেনি বরং তাকে উপহাস এবং অবজ্ঞা করে।\n\n"
                            + "তারা বলেন, “আল্লাহ যদি তোমাকে নবী বানিয়েছেন, তবে আমি কাবার পর্দা ছিঁড়ে ফেলব।” “তোমার মতো একজন সাধারণ মানুষ কীভাবে নবী হতে পারে?”\n\n"
                            + "<b>জনগণের প্রতিক্রিয়া:</b>\n\n"
                            + "নেতাদের নির্দেশে তায়েফের সাধারণ জনগণ নবীজির প্রতি চরম বিরূপ আচরণ করে। তারা নবীজিকে শহর থেকে বের করতে পাথর ছোড়ে। নবীজির পা থেকে রক্ত ঝরতে থাকে, এবং তিনি রক্তাক্ত অবস্থায় তায়েফ থেকে বের হন।\n\n"
                            + "<b>গাছে আশ্রয়:</b>\n\n"
                            + "তায়েফ ছাড়ার পর নবীজি একটি বাগানে আশ্রয় নেন। এটি ছিল মক্কার দুই প্রধান কুরাইশ নেতার বাগান। তারা একজন খ্রিস্টান দাস (আদাস) পাঠিয়ে নবীজিকে কিছু খাবার দেন।\n\n"
                            + "<b>দু’আর ঘটনা:</b>\n\n"
                            + "নির্যাতিত হওয়ার পর নবীজি (সাঃ) আল্লাহর কাছে এক হৃদয়বিদারক দু’আ করেন:\n\n"
                            + "اللَّهُمَّ إِلَيْكَ أَشْكُو ضَعْفَ قُوَّتِي، وَقِلَّةَ حِيلَتِي، وَهَوَانِي عَلَى النَّاسِ...\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে আল্লাহ! আমি আপনার কাছেই আমার দুর্বলতা, আমার কৌশলের অভাব, এবং মানুষের কাছে আমার তুচ্ছতা নিয়ে অভিযোগ জানাই.\n"
                            + "<b>[তাবরানি, আল-মুজামুল কাবির]</b>\n\n"
                            + "<b>জিবরাইল (আঃ)-এর আগমন এবং প্রস্তাব:</b>\n\n"
                            + "তায়েফ থেকে ফেরার পথে জিবরাইল (আঃ) নবীজির কাছে আসেন এবং বলেন:\n"
                            + "“আপনার অনুমতি দিলে আমি এই দুই পাহাড়ের মাঝে তায়েফের লোকদের ধ্বংস করে দেব।” নবীজি উত্তরে বলেন:\n"
                            + "<i>\"আমি তাদের ধ্বংস চাই না। বরং আমি আশা করি, ভবিষ্যতে তাদের সন্তানরা ইসলাম গ্রহণ করবে।\"</i>\n"
                            + "<b>[সহীহ মুসলিম: ১৭৯৫]</b>\n\n"
                            + "<b>কুরআনের আলোকে তায়েফ সফরের প্রেক্ষাপট:</b>\n\n"
                            + "<b>নবীর প্রতি সান্ত্বনা:</b>\n\n"
                            + "وَإِنَّكَ لَعَلَىٰ خُلُقٍ عَظِيمٍ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই তুমি মহান চরিত্রের অধিকারী।\n"
                            + "<b>[সূরা আল-কলম: ৪]</b>\n\n"
                            + "<b>আল্লাহর সাহায্য:</b>\n\n"
                            + "فَإِنَّ مَعَ ٱلْعُسْرِ يُسْرًا۔ إِنَّ مَعَ ٱلْعُسْرِ يُسْرًا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই কষ্টের সঙ্গে রয়েছে স্বস্তি। হ্যাঁ, কষ্টের সঙ্গে রয়েছে স্বস্তি।\n"
                            + "<b>[সূরা আশ-শারহ: ৬-৭]</b>",
                    "The journey to Ta'if stands as one of the most poignant testaments to the Prophet's supreme endurance and boundless mercy. Located 60 kilometers southeast of Makkah, Ta'if was the seat of the powerful Thaqif confederacy, sought by the Prophet (ﷺ) as a safe haven following the deaths of Abu Talib and Sayyidah Khadijah.\n\n"
                            + "<b>Objectives of the Mission:</b>\n"
                            + "• Convey the message of Tawheed to the chieftains of Thaqif.\n"
                            + "• Seek tribal protection and an alliance for spreading Islam.\n"
                            + "• Find a secure sanctuary outside hostile Makkah.\n\n"
                            + "<b>Arrival in Ta'if:</b>\n"
                            + "Accompanied by his freedman Zayd ibn Harithah (RA), the Prophet (ﷺ) walked to Ta'if and approached its three ruling brothers:\n"
                            + "• Abd Yalail ibn Amr\n"
                            + "• Mas'ud ibn Amr\n"
                            + "• Habib ibn Amr\n\n"
                            + "<b>Rejection by the Chieftains:</b>\n"
                            + "The chiefs responded with crude mockery and arrogance, saying: \"Could Allah find no one other than you to send as a messenger?\"\n\n"
                            + "<b>Persecution by the Ruffians:</b>\n"
                            + "Incited by their leaders, mobs lined the roads stoning the Prophet (ﷺ) until his shoes ran with blood. Zayd was severely wounded shielding him.\n\n"
                            + "<b>Sanctuary in the Vineyard:</b>\n"
                            + "Resting in an orchard belonging to Utbah and Shaybah, the Christian slave Addas served the Prophet grapes, being astonished upon hearing him begin with \"Bismillah\".\n\n"
                            + "<b>The Immortal Supplication of Ta'if:</b>\n\n"
                            + "اللَّهُمَّ إِلَيْكَ أَشْكُو ضَعْفَ قُوَّتِي، وَقِلَّةَ حِيلَتِي، وَهَوَانِي عَلَى النَّاسِ...\n\n"
                            + "<b>Translation:</b>\n"
                            + "O Allah! Unto You do I complain of my weakness, of my helplessness, and of my insignificance before people...\n"
                            + "<b>[At-Tabarani, Al-Mu'jam al-Kabir]</b>\n\n"
                            + "<b>Descent of Archangel Jibreel and Angel of Mountains:</b>\n"
                            + "Angel Jibreel appeared offering to crush the city between Mount Abu Qubays and Mount Qu'ayqi'an. The Prophet of Mercy (ﷺ) responded:\n"
                            + "\"No, rather I hope that Allah will bring forth from their loins people who will worship Allah alone without associating partners with Him.\"\n"
                            + "<b>[Sahih Muslim: 1795]</b>\n\n"
                            + "<b>Quranic Reassurance:</b>\n\n"
                            + "وَإِنَّكَ لَعَلَىٰ خُلُقٍ عَظِيمٍ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And indeed, you are of a great moral character.\n"
                            + "<b>[Surah Al-Qalam 68:4]</b>\n\n"
                            + "فَإِنَّ مَعَ ٱلْعُسْرِ يُسْرًا • إِنَّ مَعَ ٱلْعُسْرِ يُسْرًا\n\n"
                            + "<b>Translation:</b>\n"
                            + "For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.\n"
                            + "<b>[Surah Ash-Sharh 94:5-6]</b>"
            ));

            // 4. বদর যুদ্ধ
            list.add(new ProphetOverviewTopicItem(
                    4,
                    "বদর যুদ্ধ",
                    "The Battle of Badr",
                    "বদর যুদ্ধ ইসলামের ইতিহাসে প্রথম এবং সবচেয়ে গুরুত্বপূর্ণ যুদ্ধ। এটি ২ হিজরি, ১৭ রমজান (৬২৪ খ্রিস্টাব্দ) সালে মদিনার দক্ষিণ-পশ্চিমে বদর নামক স্থানে সংঘটিত হয়। মুসলমানদের মক্কায় তাওহীদের দাওয়াতের কারণে কুরাইশদের দ্বারা অত্যাচারিত ও বিতাড়িত হতে হয়েছিল। মদিনায় হিজরতের পরও কুরাইশরা মুসলমানদের বিরুদ্ধে ষড়যন্ত্র চালিয়ে যাচ্ছিল। বদর যুদ্ধ ছিল সেই প্রতিরোধের একটি গুরুত্বপূর্ণ অধ্যায়।\n\n"
                            + "<b>বদর যুদ্ধের কারণ, কুরাইশদের দমননীতি:</b>\n"
                            + "মক্কার কুরাইশরা মুসলমানদের ধ্বংস করার উদ্দেশ্যে নানা ষড়যন্ত্র করছিল।\n\n"
                            + "<b>মুসলমানদের সম্পদ লুণ্ঠন:</b>\n"
                            + "মুসলমানদের মক্কায় ফেলে আসা সম্পদ কুরাইশরা আত্মসাৎ করেছিল এবং এ থেকে তারা বাণিজ্য চালাত। আবু সুফিয়ানের নেতৃত্বে কুরাইশদের একটি বাণিজ্য কাফেলা সিরিয়া থেকে ফেরার পথে বদরের কাছ দিয়ে যাচ্ছিল।\n\n"
                            + "<b>ইসলামের অস্তিত্ব রক্ষা:</b>\n"
                            + "কুরাইশরা ইসলামকে সমূলে ধ্বংস করার ষড়যন্ত্র করেছিল। নবীজি (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) কুরাইশদের মোকাবিলার জন্য আল্লাহর আদেশে প্রস্তুতি গ্রহণ করেন।\n\n"
                            + "<b>যুদ্ধের প্রস্তুতি:</b>\n"
                            + "• <b>মুসলমানদের সংখ্যা:</b> ৩১৩ জন, যাদের কাছে ছিল মাত্র ২টি ঘোড়া, ৭০টি উট, এবং সীমিত অস্ত্র।\n"
                            + "• <b>কুরাইশদের সংখ্যা:</b> প্রায় ১,০০০ জন, যাদের কাছে প্রচুর অস্ত্র, ঘোড়া এবং সামরিক সরঞ্জাম ছিল। নবীজি (সাঃ) সাহাবাদের সঙ্গে পরামর্শ করে যুদ্ধে অংশগ্রহণের সিদ্ধান্ত নেন।\n\n"
                            + "<b>বদরের স্থান নির্বাচন:</b>\n"
                            + "বদর ছিল একটি কৌশলগত স্থান, যেখানে কুয়াগুলো মুসলমানদের দখলে ছিল। এটি কুরাইশদের পানির প্রয়োজন পূরণে সমস্যা সৃষ্টি করে।\n\n"
                            + "<b>যুদ্ধের সূচনা:</b>\n"
                            + "প্রথমে একক যোদ্ধাদের মধ্যে দ্বন্দ্বযুদ্ধ হয়। মুসলমানদের পক্ষে যুদ্ধ করেন হযরত হামজা (রাঃ), হযরত আলী (রাঃ), এবং হযরত উবাইদা (রাঃ)। তারা কুরাইশদের প্রধান যোদ্ধাদের পরাজিত করেন।\n\n"
                            + "<b>ফেরেশতাদের আগমন:</b>\n"
                            + "কুরআনে বর্ণিত হয়েছে, আল্লাহ মুসলমানদের সাহায্যের জন্য ফেরেশতাদের পাঠান।\n\n"
                            + "إِذْ تَسْتَغِيثُونَ رَبَّكُمْ فَٱسْتَجَابَ لَكُمْ أَنِّى مُمِدُّكُم بِأَلْفٍ مِّنَ ٱلْمَلَٰٓئِكَةِ مُرْدِفِينَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন তোমরা তোমাদের প্রতিপালকের সাহায্য চেয়েছিলে, তখন তিনি তোমাদের কাছে এক হাজার ফেরেশতাকে পাঠানোর প্রতিশ্রুতি দিয়েছিলেন।\n"
                            + "<b>[সূরা আল-আনফাল: ৯]</b>\n\n"
                            + "<b>মুসলমানদের বিজয়:</b>\n"
                            + "• মুসলমানরা আল্লাহর সাহায্যে এবং নবীজির নেতৃত্বে কুরাইশদের পরাজিত করে।\n"
                            + "• কুরাইশদের ৭০ জন নিহত হয় এবং ৭০ জন বন্দি হয়।\n"
                            + "• মুসলমানদের মধ্যে ১৪ জন শহীদ হন।\n\n"
                            + "<b>কুরআনে বদর যুদ্ধের উল্লেখ:</b>\n\n"
                            + "<b>বদর যুদ্ধ আল্লাহর সাহায্যের নিদর্শন:</b>\n\n"
                            + "وَلَقَدْ نَصَرَكُمُ ٱللَّهُ بِبَدْرٍ وَأَنتُمْ أَذِلَّةٌ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ তোমাদের বদরের যুদ্ধে সাহায্য করেছিলেন, যখন তোমরা ছিলে দুর্বল।\n"
                            + "<b>[সূরা আলে ইমরান: ১২৩]</b>\n\n"
                            + "سَيُهْزَمُ ٱلْجَمْعُ وَيُوَلُّونَ ٱلدُّبُرَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "অচিরেই তারা পরাজিত হবে এবং পেছনে মুখ ঘুরিয়ে পালাবে।\n"
                            + "<b>[সূরা আল-কামার: ৪৫]</b>",
                    "The Battle of Badr (Yawm al-Furqan - The Day of Criterion) was the first and most pivotal military confrontation in Islamic history. Occurring on Friday, 17th Ramadan in 2 AH (624 CE) at the wells of Badr southwest of Madinah, it pitted truth directly against pagan oppression.\n\n"
                            + "<b>Causes and Provocations:</b>\n"
                            + "• <b>Expropriation of Muslim Wealth:</b> Quraish confiscated all homes and assets left behind in Makkah by the emigrants, trading them for profits.\n"
                            + "• <b>Crushing the Islamic State:</b> Abu Jahl marched with an arrogant army intent on annihilating Islam once and for all.\n\n"
                            + "<b>Mobilization and Armaments:</b>\n"
                            + "• <b>Muslim Army:</b> 313 ill-equipped companions, with only 2 horses, 70 camels taken in turns, and basic swords.\n"
                            + "• <b>Quraish Army:</b> Over 1,000 seasoned warriors, 100 armored warhorses, 700 camels, and abundant supplies.\n\n"
                            + "<b>Strategic Logistics:</b>\n"
                            + "Hubab ibn al-Mundhir (RA) advised securing the closest water well to deny water to the enemy.\n\n"
                            + "<b>Initial Duels:</b>\n"
                            + "In the traditional pre-battle duels, Hamzah (RA), Ali (RA), and Ubaydah ibn al-Harith (RA) defeated Quraish champions Shaybah, Utbah, and Walid.\n\n"
                            + "<b>Descent of Angelic Reinforcements:</b>\n\n"
                            + "إِذْ تَسْتَغِيثُونَ رَبَّكُمْ فَٱسْتَجَابَ لَكُمْ أَنِّى مُمِدُّكُم بِأَلْفٍ مِّنَ ٱلْمَلَٰٓئِكَةِ مُرْدِفِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "When you asked help of your Lord, and He answered you, 'Indeed, I will reinforce you with a thousand from the angels, following one another.'\n"
                            + "<b>[Surah Al-Anfal 8:9]</b>\n\n"
                            + "<b>Decisive Triumph:</b>\n"
                            + "• 70 prominent pagan leaders (including Abu Jahl and Umayyah ibn Khalaf) were killed, and 70 taken captive.\n"
                            + "• 14 noble companions attained martyrdom.\n\n"
                            + "<b>Quranic Affirmations:</b>\n\n"
                            + "وَلَقَدْ نَصَرَكُمُ ٱللَّهُ بِبَدْرٍ وَأَنتُمْ أَذِلَّةٌ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And Allah had already given you victory at Badr while you were few in number.\n"
                            + "<b>[Surah Ali 'Imran 3:123]</b>\n\n"
                            + "سَيُهْزَمُ ٱلْجَمْعُ وَيُوَلُّونَ ٱلدُّبُرَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Their assembly will be defeated, and they will turn their backs [in retreat].\n"
                            + "<b>[Surah Al-Qamar 54:45]</b>"
            ));

            // 5. ওহুদ যুদ্ধ
            list.add(new ProphetOverviewTopicItem(
                    5,
                    "ওহুদ যুদ্ধ",
                    "The Battle of Uhud",
                    "ওহুদ যুদ্ধ ছিল মুসলমানদের জন্য এক গুরুত্বপূর্ণ এবং পরীক্ষার সময়। এটি বদর যুদ্ধে কুরাইশদের পরাজয়ের প্রতিশোধ নিতে সংঘটিত হয়। ৩ হিজরি (৬২৫ খ্রিস্টাব্দ) সালে মদিনা থেকে প্রায় ৫ কিলোমিটার দূরে ওহুদ পর্বতের পাদদেশে এই যুদ্ধ সংঘটিত হয়। বদর যুদ্ধে পরাজয়ের প্রতিশোধ নিতে মক্কার কুরাইশরা একটি বিশাল বাহিনী নিয়ে মদিনার দিকে অগ্রসর হয়। নবী মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম)-এর নেতৃত্বে মুসলিম বাহিনী এই চ্যালেঞ্জ মোকাবিলার জন্য প্রস্তুত হয়।\n\n"
                            + "<b>ওহুদ যুদ্ধের কারণ:</b>\n\n"
                            + "<b>বদর যুদ্ধে কুরাইশদের পরাজয়ের প্রতিশোধ নেওয়া:</b>\n"
                            + "বদর যুদ্ধে ৭০ জন কুরাইশ নেতা নিহত হয় এবং ৭০ জন বন্দি হয়। এই পরাজয়ের ক্ষোভ কুরাইশরা ভুলতে পারেনি।\n\n"
                            + "<b>মুসলমানদের শক্তি দমন করা:</b>\n"
                            + "বদর যুদ্ধে মুসলমানদের বিজয় ইসলাম প্রচারের পথ উন্মুক্ত করে। কুরাইশরা এটিকে হুমকি হিসেবে দেখে।\n\n"
                            + "<b>কুরাইশদের সামরিক অভিযান:</b>\n"
                            + "আবু সুফিয়ানের নেতৃত্বে কুরাইশরা প্রায় ৩,০০০ সৈন্য নিয়ে মুসলমানদের আক্রমণ করার পরিকল্পনা করে।\n\n"
                            + "<b>কুরাইশ বাহিনীর প্রস্তুতি:</b>\n"
                            + "• বাহিনীর সংখ্যা: প্রায় ৩,০০০ সৈন্য, ২০০ ঘোড়া, এবং প্রচুর অস্ত্রশস্ত্র।\n"
                            + "• নেতৃত্বে ছিলেন আবু সুফিয়ান।\n\n"
                            + "<b>মুসলিম বাহিনীর প্রস্তুতি:</b>\n"
                            + "• বাহিনীর সংখ্যা: প্রায় ৭০০ জন।\n"
                            + "• নবীজি (সাঃ) ওহুদ পাহাড়ের পাদদেশে প্রতিরক্ষামূলক অবস্থান নেন।\n"
                            + "• তিনি ৫০ জন তীরন্দাজকে একটি পাহাড়ি পথে (জাবাল রুমাত) মোতায়েন করেন এবং নির্দেশ দেন যে, যাই হোক না কেন, তারা তাদের অবস্থান ছাড়বে না।\n"
                            + "<i>\"যুদ্ধের ফলাফল যাই হোক না কেন, তোমরা তোমাদের অবস্থান ত্যাগ করবে না।\"</i>\n"
                            + "<b>[সহীহ বুখারী: ৩০৪০]</b>\n\n"
                            + "<b>যুদ্ধের প্রথম পর্যায়:</b>\n"
                            + "• মুসলমানরা প্রথমে কুরাইশদের উপর বিজয় অর্জন করে।\n"
                            + "• কুরাইশরা পিছু হটতে শুরু করে।\n\n"
                            + "<b>তীরন্দাজদের ভুল:</b>\n"
                            + "• নবীজির নির্দেশ সত্ত্বেও কিছু তীরন্দাজ বিজয়ের সম্ভাবনা দেখে তাদের অবস্থান ত্যাগ করে।\n"
                            + "• খালিদ বিন ওয়ালিদ (যিনি তখনও ইসলাম গ্রহণ করেননি) এই সুযোগে কুরাইশ বাহিনীর সঙ্গে মুসলমানদের ওপর পেছন দিক থেকে আক্রমণ করেন।\n\n"
                            + "<b>মুসলিম বাহিনীর বিপর্যয়:</b>\n"
                            + "• কুরাইশদের আকস্মিক আক্রমণে মুসলমানদের অনেক সৈন্য হতাহত হয়।\n"
                            + "• নবীজির জীবন বিপন্ন হয় এবং তার সামান্য আঘাতও হয়।\n"
                            + "• সাহাবী মুসআব বিন উমাইর (রাঃ) শহীদ হন, যাকে কুরাইশরা ভুল করে নবীজি মনে করেছিল।\n\n"
                            + "<b>ওহুদ যুদ্ধের ফলাফল:</b>\n\n"
                            + "<b>শহীদদের সংখ্যা:</b>\n"
                            + "• মুসলমানদের ৭০ জন সাহাবী শহীদ হন, যাদের মধ্যে ছিলেন হযরত হামজা (রাঃ)।\n"
                            + "• কুরাইশদের মধ্যে প্রায় ২০ জন নিহত হয়।\n\n"
                            + "<b>শিক্ষণীয় অভিজ্ঞতা:</b>\n"
                            + "মুসলমানরা শৃঙ্খলা এবং নবীজির আদেশ মানার প্রয়োজনীয়তা উপলব্ধি করে।\n\n"
                            + "<b>কুরাইশদের লক্ষ্য পূরণে ব্যর্থতা:</b>\n"
                            + "যদিও কুরাইশরা মুসলমানদের বিপর্যস্ত করেছিল, তারা মদিনার ইসলামী রাষ্ট্র ধ্বংস করতে পারেনি।\n\n"
                            + "<b>কুরআনে ওহুদ যুদ্ধের উল্লেখ</b>\n\n"
                            + "وَلَقَدْ صَدَقَكُمُ ٱللَّهُ وَعْدَهُۥٓ إِذْ تَحُسُّونَهُم بِإِذْنِهِۦ ۖ حَتَّىٰٓ إِذَا فَشِلْتُمْ وَتَنَٰزَعْتُمْ فِى ٱلْأَمْرِ وَعَصَيْتُم مِّنۢ بَعْدِ مَآ أَرَىٰكُم مَّا تُحِبُّونَ ۚ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ তোমাদের তাঁর প্রতিশ্রুতি পূর্ণ করেছেন, যখন তোমরা তাঁর অনুমতিতে তাদেরকে ধ্বংস করতে শুরু করেছিলে, কিন্তু পরে তোমরা দুর্বল হয়ে পড়লে, আদেশ অমান্য করলে এবং নিজেদের মধ্যে মতভেদে লিপ্ত হলে।\n"
                            + "<b>[সূরা আলে ইমরান: ১৫২]</b>\n\n"
                            + "<b>আল্লাহর পরীক্ষার উদ্দেশ্য:</b>\n\n"
                            + "وَلِيُمَحِّصَ ٱللَّهُ ٱلَّذِينَ ءَامَنُوا۟ وَيَمْحَقَ ٱلْكَٰفِرِينَ\n\n"
                            + "এবং আল্লাহ মুমিনদের পরিশুদ্ধ করবেন এবং কাফিরদের ধ্বংস করবেন।\n"
                            + "<b>[সূরা আলে ইমরান: ১৪১]</b>\n\n"
                            + "<b>নবীর প্রতি আনুগত্যের গুরুত্ব:</b>\n\n"
                            + "وَمَا مُحَمَّدٌ إِلَّا رَسُولٌ ۚ قَدْ خَلَتْ مِن قَبْلِهِ ٱلرُّسُلُ ۚ أَفَإِيْن مَّاتَ أَوْ قُتِلَ ٱنقَلَبْتُمْ عَلَىٰٓ أَعْقَٰبِكُمْ ۚ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "মুহাম্মদ তো কেবল একজন রাসূল, তার আগে অনেক রাসূল চলে গেছেন। তবে কি তিনি মারা গেলে বা নিহত হলে তোমরা ফিরে যাবে (ইসলাম ত্যাগ করবে)?\n"
                            + "<b>[সূরা আলে ইমরান: ১৪৪]</b>",
                    "The Battle of Uhud in 3 AH (625 CE) was a profound spiritual and military test for the fledgling Muslim community, taking place at the base of Mount Uhud 5 km north of Madinah.\n\n"
                            + "<b>Causes of the Conflict:</b>\n"
                            + "• <b>Quraish Vengeance:</b> Thirsting for revenge following the crushing defeat at Badr where 70 leaders fell.\n"
                            + "• <b>Halting Islamic Growth:</b> Neutralizing the rising geopolitical stature of Madinah.\n\n"
                            + "<b>Mobilization:</b>\n"
                            + "• <b>Quraish Forces:</b> 3,000 warriors, 200 cavalry, and 700 armors led by Abu Sufyan.\n"
                            + "• <b>Muslim Army:</b> 700 steadfast believers after 300 hypocrites under Abdullah ibn Ubayy deserted.\n\n"
                            + "<b>The Strategic Archer Post on Jabal ar-Rumat:</b>\n"
                            + "The Prophet (ﷺ) stationed 50 archers under Abdullah ibn Jubayr (RA) on Mount Archers with strict, absolute orders:\n"
                            + "<i>\"Do not abandon your post under any circumstances, whether we win or lose.\"</i>\n"
                            + "<b>[Sahih al-Bukhari: 3040]</b>\n\n"
                            + "<b>Course of the Battle & Reversal:</b>\n"
                            + "• <b>Initial Muslim Victory:</b> The Muslims routed the Quraish infantry who fled leaving behind their camp.\n"
                            + "• <b>The Archers' Critical Mistake:</b> Mistaking retreat for total victory, 40 archers left their post to collect spoils despite warnings.\n"
                            + "• <b>Rear Flanking Cavalry Charge:</b> Khalid ibn al-Walid (then unguided) seized the vacant pass, launching a devastating rear assault.\n\n"
                            + "<b>Trials and Losses:</b>\n"
                            + "• 70 noble companions were martyred, notably Sayyid ash-Shuhada Hamzah ibn Abdul-Muttalib (RA) and Mus'ab ibn Umayr (RA).\n"
                            + "• The Prophet (ﷺ) was wounded, losing a tooth and sustaining helmet abrasions.\n\n"
                            + "<b>Quranic Reflections:</b>\n\n"
                            + "وَلَقَدْ صَدَقَكُمُ ٱللَّهُ وَعْدَهُۥٓ إِذْ تَحُسُّونَهُم بِإِذْنِهِۦ...\n\n"
                            + "<b>Translation:</b>\n"
                            + "And Allah had certainly fulfilled His promise to you when you were killing the enemy by His permission until when you faltered and disputed about the order...\n"
                            + "<b>[Surah Ali 'Imran 3:152]</b>\n\n"
                            + "وَلِيُمَحِّصَ ٱللَّهُ ٱلَّذِينَ ءَامَنُوا۟ وَيَمْحَقَ ٱلْكَٰفِرِينَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And that Allah may purify the believers [through trials] and destroy the disbelievers.\n"
                            + "<b>[Surah Ali 'Imran 3:141]</b>\n\n"
                            + "وَمَا مُحَمَّدٌ إِلَّا رَسُولٌ ۚ قَدْ خَلَتْ مِن قَبْلِهِ ٱلرُّسُلُ ۚ أَفَإِيْن مَّاتَ أَوْ قُتِلَ ٱنقَلَبْتُمْ عَلَىٰٓ أَعْقَٰبِكُمْ ۚ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Muhammad is not but a messenger. [Other] messengers have passed on before him. So if he was to die or be killed, would you turn back on your heels?\n"
                            + "<b>[Surah Ali 'Imran 3:144]</b>"
            ));

            // 6. আহযাব (খন্দক) যুদ্ধ
            list.add(new ProphetOverviewTopicItem(
                    6,
                    "আহযাব (খন্দক) যুদ্ধ",
                    "The Battle of Ahzab (The Trench)",
                    "আহযাব যুদ্ধ, যা খন্দক যুদ্ধ নামেও পরিচিত, ইসলামের ইতিহাসে একটি গুরুত্বপূর্ণ এবং কৌশলগত যুদ্ধ। এটি সংঘটিত হয় ৫ হিজরিতে (৬২৭ খ্রিস্টাব্দ), মদিনায়। \"আহযাব\" অর্থ \"জোট\", কারণ মদিনার বিরুদ্ধে বিভিন্ন গোত্র ও শক্তি একত্রিত হয়েছিল। \"খন্দক\" (পরিখা) শব্দটি এসেছে মুসলমানদের রক্ষার জন্য মদিনার চারপাশে খনন করা পরিখা থেকে।\n\n"
                            + "<b>যুদ্ধের কারণ:</b>\n\n"
                            + "<b>কুরাইশদের প্রতিশোধের আকাঙ্ক্ষা:</b>\n"
                            + "বদর এবং ওহুদ যুদ্ধের পরে কুরাইশরা মদিনার মুসলমানদের বিরুদ্ধে চূড়ান্ত আক্রমণের পরিকল্পনা করে। তারা মদিনা ধ্বংস করতে এবং নবীজিকে (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) হত্যা করতে চেয়েছিল।\n\n"
                            + "<b>ইহুদি গোত্রের ষড়যন্ত্র:</b>\n"
                            + "বনু নাযীর, যাদের নবীজি মদিনা থেকে বহিষ্কার করেছিলেন, কুরাইশদের সঙ্গে মিত্রতা গড়ে তোলে।তারা মদিনার আভ্যন্তরীণ ইহুদি গোত্র বনু কুরাইযাকে মুসলমানদের বিরুদ্ধে উত্তেজিত করে।\n\n"
                            + "<b>ইসলামের বিস্তার রোধ:</b>\n"
                            + "ইসলামের ক্রমবর্ধমান জনপ্রিয়তা ও শক্তি তাদের বিরোধীদের জন্য হুমকি হয়ে উঠেছিল।\n\n"
                            + "<b>যুদ্ধের পরিকল্পনা এবং পরিখা খনন:</b>\n\n"
                            + "<b>পরিখার ধারণা:</b>\n"
                            + "নবীজি (সাঃ) ও সাহাবাগণ পরামর্শ করেন। সালমান ফারসি (রাঃ) মদিনার আশেপাশে প্রতিরক্ষামূলক পরিখা খননের পরামর্শ দেন। এটি আরবের জন্য এক নতুন কৌশল ছিল।\n\n"
                            + "<b>পরিখা খনন:</b>\n"
                            + "• মদিনার তিন দিক পাহাড় ও ঘন বাগান দ্বারা রক্ষিত ছিল। একমাত্র উন্মুক্ত দিকটি ছিল উত্তর অংশ।\n"
                            + "• এই অঞ্চলে পরিখা খনন করা হয়।\n"
                            + "• নবীজি এবং সাহাবারা নিজেরাই পরিখা খননে অংশগ্রহণ করেন।\n\n"
                            + "<b>পরিখার আকার:</b>\n"
                            + "পরিখাটি প্রায় ৫,০০০ গজ দীর্ঘ এবং ১৫ ফুট গভীর ছিল।\n\n"
                            + "<b>আহযাবদের বাহিনী:</b>\n"
                            + "• <b>মুসলমানদের সংখ্যা:</b> প্রায় ৩,০০০।\n"
                            + "• <b>আহযাব (জোট) বাহিনী:</b> প্রায় ১০,০০০।\n"
                            + "কুরাইশ, বনু নাযীর, বনু ঘাতফান এবং অন্যান্য আরব গোত্র মদিনার বিরুদ্ধে এই জোট গঠন করে।\n\n"
                            + "<b>যুদ্ধের গুরুত্বপূর্ণ ঘটনা:</b>\n\n"
                            + "<b>শত্রুদের পরিখার মুখোমুখি হওয়া:</b>\n"
                            + "• আহযাব বাহিনী মদিনার উত্তরে পৌঁছে পরিখার মুখোমুখি হয়।\n"
                            + "• তারা এই নতুন কৌশলে হতবাক হয় এবং মদিনায় প্রবেশ করতে ব্যর্থ হয়।\n\n"
                            + "<b>বনু কুরাইযার বিশ্বাসঘাতকতা:</b>\n"
                            + "বনু কুরাইযা, মদিনার আভ্যন্তরীণ ইহুদি গোত্র, মদিনার প্রতিরক্ষা চুক্তি ভঙ্গ করে শত্রুদের সঙ্গে যোগ দেয়। এটি মুসলমানদের জন্য একটি বড় হুমকি হয়ে দাঁড়ায়।\n\n"
                            + "<b>যুদ্ধের দীর্ঘায়িত অবস্থা:</b>\n"
                            + "• ২০-২৫ দিন যুদ্ধ স্থায়ী হয়।\n"
                            + "• শত্রুরা খাবার এবং সরবরাহ সংকটে পড়ে।\n\n"
                            + "<b>আল্লাহর সাহায্য:</b>\n"
                            + "• এক রাতে প্রবল ঝড় এবং শীতল আবহাওয়া শত্রুদের শিবির ধ্বংস করে দেয়।\n"
                            + "• শত্রুরা বিভ্রান্ত হয়ে মদিনা আক্রমণ করতে ব্যর্থ হয়।\n\n"
                            + "وَرَدَّ ٱللَّهُ ٱلَّذِينَ كَفَرُوا۟ بِغَيْظِهِمْ لَمْ يَنَالُوا۟ خَيْرًا ۚ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ কাফিরদের তাদের ক্রোধসহ প্রত্যাখ্যান করলেন; তারা কিছুই অর্জন করতে পারল না।\n"
                            + "<b>[সূরা আল-আহযাব: ২৫]</b>\n\n"
                            + "<b>মুসলমানদের বিজয়:</b>\n"
                            + "আল্লাহর সাহায্যে এবং মুসলমানদের ঐক্যের মাধ্যমে আহযাবদের পরাজিত করা হয়।\n\n"
                            + "<b>কুরআনে আহযাব যুদ্ধের উল্লেখ:</b>\n\n"
                            + "<b>পরিখার যুদ্ধের বর্ণনা:</b>\n\n"
                            + "إِذْ جَآءَتْكُمْ جُنُودٌ فَأَرْسَلْنَا عَلَيْهِمْ رِيحًا وَجُنُودًا لَّمْ تَرَوْهَا ۚ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন তোমাদের কাছে সৈন্যবাহিনী উপস্থিত হয়েছিল, আমি তাদের বিরুদ্ধে বাতাস এবং এমন সৈন্য প্রেরণ করেছি, যাদের তোমরা দেখনি।\n"
                            + "<b>[সূরা আল-আহযাব: ৯]</b>\n\n"
                            + "<b>মুমিনদের ঈমান পরীক্ষা:</b>\n\n"
                            + "وَلَمَّا رَأَى ٱلْمُؤْمِنُونَ ٱلْأَحْزَابَ قَالُوا۟ هَـٰذَا مَا وَعَدَنَا ٱللَّهُ وَرَسُولُهُۥ وَصَدَقَ ٱللَّهُ وَرَسُولُهُۥ ۚ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন মুমিনরা আহযাব বাহিনীকে দেখল, তারা বলল, এটি সেই প্রতিশ্রুতি, যা আল্লাহ এবং তাঁর রাসূল আমাদের দিয়েছিলেন। এবং আল্লাহ ও তাঁর রাসূল সত্য বলেছিলেন।\n"
                            + "<b>[সূরা আল-আহযাব: ২২]</b>\n\n"
                            + "<b>বনু কুরাইযার বিশ্বাসঘাতকতা:</b>\n\n"
                            + "وَأَنزَلَ ٱلَّذِينَ ظَٰهَرُوهُم مِّنْ أَهْلِ ٱلْكِتَـٰبِ مِن صَيَاصِيهِمْ وَقَذَفَ فِى قُلُوبِهِمُ ٱلرُّعْبَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আল্লাহ আহলে কিতাবদের মধ্যে যারা কাফিরদের সহায়তা করেছিল, তাদের দুর্গ থেকে নামিয়ে আনলেন এবং তাদের অন্তরে ভয় সঞ্চার করলেন।\n"
                            + "<b>[সূরা আল-আহযাব: ২৬]</b>",
                    "The Battle of the Trench (Ghazwah al-Khandaq / Al-Ahzab) in 5 AH (627 CE) was an existential siege of Madinah by a massive pagan coalition (Ahzab).\n\n"
                            + "<b>Causes and Alliances:</b>\n"
                            + "• <b>The Grand Coalition:</b> 10,000 warriors from Quraish, Ghatafan, and Banu Nadir assembled to eradicate Islam completely.\n"
                            + "• <b>Treason of Banu Qurayza:</b> The internal Jewish tribe betrayed the Charter of Madinah, threatening the Muslims from the rear.\n\n"
                            + "<b>The Strategic Trench:</b>\n"
                            + "Salman al-Farsi (RA) proposed digging a deep trench across the exposed northern perimeter of Madinah.\n"
                            + "• Over 5,000 yards long and 15 feet deep.\n"
                            + "• The Prophet (ﷺ) personally participated in breaking boulders and digging.\n\n"
                            + "<b>The 25-Day Siege:</b>\n"
                            + "Shocked by the unprecedented military tactic, the coalition mounted a bitter month-long siege during freezing winter conditions.\n\n"
                            + "<b>Divine Deliverance:</b>\n"
                            + "Allah dispatched a fierce tempest and invisible armies of angels that overturned enemy tents, extinguished fires, and shattered morale.\n\n"
                            + "وَرَدَّ ٱللَّهُ ٱلَّذِينَ كَفَرُوا۟ بِغَيْظِهِمْ لَمْ يَنَالُوا۟ خَيْرًا ۚ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And Allah repelled those who disbelieved, in their rage, [they having] gained no good.\n"
                            + "<b>[Surah Al-Ahzab 33:25]</b>\n\n"
                            + "إِذْ جَآءَتْكُمْ جُنُودٌ فَأَرْسَلْنَا عَلَيْهِمْ رِيحًا وَجُنُودًا لَّمْ تَرَوْهَا ۚ\n\n"
                            + "<b>Translation:</b>\n"
                            + "When there came against you forces, and We sent upon them a wind and forces [of angels] you did not see.\n"
                            + "<b>[Surah Al-Ahzab 33:9]</b>\n\n"
                            + "وَلَمَّا رَأَى ٱلْمُؤْمِنُونَ ٱلْأَحْزَابَ قَالُوا۟ هَـٰذَا مَا وَعَدَنَا ٱللَّهُ وَرَسُولُهُۥ وَصَدَقَ ٱللَّهُ وَرَسُولُهُۥ ۚ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And when the believers saw the confederates, they said, 'This is what Allah and His Messenger had promised us, and Allah and His Messenger spoke the truth.'\n"
                            + "<b>[Surah Al-Ahzab 33:22]</b>"
            ));

            // 7. হুদাইবিয়ার সন্ধি
            list.add(new ProphetOverviewTopicItem(
                    7,
                    "হুদাইবিয়ার সন্ধি",
                    "The Treaty of Hudaybiyyah",
                    "হুদাইবিয়ার সন্ধি ইসলামের ইতিহাসে এক গুরুত্বপূর্ণ ও কৌশলগত সন্ধি। এটি ৬ হিজরিতে (৬২৮ খ্রিস্টাব্দ) মক্কার কুরাইশ ও মদিনার মুসলমানদের মধ্যে সম্পাদিত হয়। হযরত মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) ও সাহাবীরা মক্কার কাবা তাওয়াফ করার উদ্দেশ্যে পবিত্র ওমরাহ পালনের জন্য রওনা হন। তবে কুরাইশরা তাদের মক্কায় প্রবেশ করতে বাধা দেয়, এবং দুই পক্ষের মধ্যে একটি শান্তিচুক্তি সম্পন্ন হয়।\n\n"
                            + "<b>সফরের উদ্দেশ্য:</b>\n"
                            + "নবীজি (সাঃ) স্বপ্নে দেখেন যে তিনি এবং তার সাহাবীরা কাবা তাওয়াফ করছেন। এ স্বপ্ন বাস্তবায়নের জন্য ১৪০০ সাহাবীকে সঙ্গে নিয়ে তিনি মক্কার উদ্দেশ্যে যাত্রা করেন। উদ্দেশ্য ছিল শান্তিপূর্ণভাবে ওমরাহ পালন করা এবং ইসলামের শান্তিপূর্ণ বার্তা প্রচার করা।\n\n"
                            + "<b>সফর এবং হুদাইবিয়ার অবস্থান:</b>\n"
                            + "হুদাইবিয়া ছিল মক্কার সীমান্তবর্তী একটি স্থান। নবীজি এবং সাহাবীরা মক্কার কাছাকাছি এসে হুদাইবিয়ায় অবস্থান নেন।\n\n"
                            + "<b>কুরাইশদের বাধা ও আলোচনা:</b>\n\n"
                            + "<b>কুরাইশদের শত্রুতা:</b>\n"
                            + "কুরাইশরা মুসলমানদের মক্কায় প্রবেশ করতে বাধা দেয়। তারা নবীজি ও সাহাবীদের উদ্দেশ্য নিয়ে সন্দেহ প্রকাশ করে।\n\n"
                            + "<b>দূত পাঠানো:</b>\n"
                            + "নবীজি (সাঃ) কুরাইশদের কাছে শান্তির বার্তা নিয়ে দূত পাঠান। হযরত উসমান (রাঃ) কুরাইশদের সঙ্গে আলোচনার জন্য মক্কায় যান।\n\n"
                            + "<b>কুরাইশদের অপবাদ:</b>\n"
                            + "কুরাইশরা প্রথমে হযরত উসমানকে (রাঃ) আটক করে, যা মুসলমানদের মধ্যে উত্তেজনা সৃষ্টি করে।মুসলমানরা \"বাইআতুল রিজওয়ান\" নামক একটি অঙ্গীকার করেন, যেখানে তারা নবীজির জন্য প্রাণ দিতে প্রস্তুত ছিলেন।\n"
                            + "<b>[সূরা আল-ফাতহ: ১৮]</b>\n\n"
                            + "<b>হুদাইবিয়ার সন্ধি:</b>\n\n"
                            + "<b>চুক্তির গুরুত্বপূর্ণ শর্তাবলী:</b>\n"
                            + "• মুসলমানরা এবার মক্কায় প্রবেশ করতে পারবে না এবং ওমরাহ পালনে বাধ্য হবে পরের বছর আসতে।\n"
                            + "• দশ বছরের জন্য দুই পক্ষের মধ্যে যুদ্ধবিরতি থাকবে।\n"
                            + "• মক্কা থেকে কোনো ব্যক্তি ইসলাম গ্রহণ করে মদিনায় গেলে তাকে ফেরত পাঠানো হবে,কিন্তু মদিনা থেকে কেউ মক্কায় গেলে তাকে ফেরত দেওয়া হবে না।\n"
                            + "• আরবের অন্য গোত্ররা চাইলে কুরাইশ বা মুসলমানদের মিত্র হতে পারবে।\n\n"
                            + "<b>মুসলমানদের অসন্তোষ:</b>\n"
                            + "• অনেক সাহাবী চুক্তির শর্তাবলী নিয়ে অসন্তুষ্ট ছিলেন।\n"
                            + "• তারা এটিকে মুসলমানদের জন্য অসম্মানজনক মনে করেছিলেন।\n"
                            + "• নবীজি (সাঃ) সাহাবীদের ধৈর্য ও আল্লাহর পরিকল্পনার উপর ভরসা রাখতে বলেন।\n\n"
                            + "<b>কুরআনে হুদাইবিয়ার উল্লেখ:</b>\n\n"
                            + "<b>শান্তিচুক্তির সফলতা:</b>\n\n"
                            + "إِنَّا فَتَحْنَا لَكَ فَتْحًا مُّبِينًا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই আমি তোমার জন্য সুস্পষ্ট বিজয় দিয়েছি।\n"
                            + "<b>[সূরা আল-ফাতহ: ১]</b>\n\n"
                            + "<b>মুসলমানদের শান্তির বার্তা:</b>\n\n"
                            + "وَهُوَ ٱلَّذِى كَفَّ أَيْدِيَهُمْ عَنكُمْ وَأَيْدِيَكُمْ عَنْهُم بِبَطْنِ مَكَّةَ مِنۢ بَعْدِ أَنْ أَظْفَرَكُمْ عَلَيْهِمْ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তিনিই তোমাদের হাত তাদের থেকে এবং তাদের হাত তোমাদের থেকে মক্কার উপত্যকায় ফিরিয়ে দিয়েছেন।\n"
                            + "<b>[সূরা আল-ফাতহ: ২৪]</b>",
                    "The Treaty of Hudaybiyyah in 6 AH (628 CE) was a masterful diplomatic and strategic milestone in Islamic history. Setting out with 1,400 companions in pilgrim garb (Ihram) purely for Umrah, the Muslims were halted at Hudaybiyyah by the armed Quraish.\n\n"
                            + "<b>The Vision and Purpose:</b>\n"
                            + "Following a divine dream of entering the Ka'bah, the Prophet (ﷺ) departed peacefully with 1,400 companions carrying only travel swords in scabbards.\n\n"
                            + "<b>The Pledge of the Tree (Bay'at ar-Ridwan):</b>\n"
                            + "When envoy Uthman ibn Affan (RA) was rumored murdered in Makkah, all 1,400 companions took an oath of allegiance under an acacia tree to defend the Prophet unto death.\n"
                            + "<b>[Surah Al-Fath 48:18]</b>\n\n"
                            + "<b>Terms of the Treaty:</b>\n"
                            + "Negotiated with Suhayl ibn Amr:\n"
                            + "• 10-year bilateral truce (peace).\n"
                            + "• Muslims return this year and perform Umrah the following year for 3 days.\n"
                            + "• Unilateral extradition of Makkan converts without guardian consent.\n"
                            + "• Free alliance rights for all Arab tribes.\n\n"
                            + "<b>Strategic Masterstroke:</b>\n"
                            + "Though outwardly disadvantageous, the truce allowed unrestricted dialogue, leading to thousands embracing Islam—including Khalid ibn al-Walid and Amr ibn al-Aas.\n\n"
                            + "<b>Divine Proclamation of Victory:</b>\n\n"
                            + "إِنَّا فَتَحْنَا لَكَ فَتْحًا مُّبِينًا\n\n"
                            + "<b>Translation:</b>\n"
                            + "Indeed, We have given you a clear conquest.\n"
                            + "<b>[Surah Al-Fath 48:1]</b>\n\n"
                            + "وَهُوَ ٱلَّذِى كَفَّ أَيْدِيَهُمْ عَنكُمْ وَأَيْدِيَكُمْ عَنْهُم بِبَطْنِ مَكَّةَ مِنۢ بَعْدِ أَنْ أَظْفَرَكُمْ عَلَيْهِمْ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And it is He who withheld their hands from you and your hands from them within [the area of] Makkah after He caused you to overcome them.\n"
                            + "<b>[Surah Al-Fath 48:24]</b>"
            ));

            // 8. মক্কা বিজয়
            list.add(new ProphetOverviewTopicItem(
                    8,
                    "মক্কা বিজয়",
                    "The Conquest of Makkah",
                    "মক্কা বিজয় ইসলামের ইতিহাসের একটি গুরুত্বপূর্ণ ও গৌরবময় ঘটনা। এটি ৮ হিজরি (৬৩০ খ্রিস্টাব্দ) সালে সংঘটিত হয়। হুদাইবিয়ার সন্ধির শর্ত ভঙ্গের পর নবী মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) কুরাইশদের বিরুদ্ধে অভিযান পরিচালনা করেন, যা মক্কা বিজয়ে পরিণত হয়। এই বিজয় ইসলামের প্রসার এবং আরব উপদ্বীপে একত্রীকরণে গুরুত্বপূর্ণ ভূমিকা রাখে।\n\n"
                            + "<b>মক্কা বিজয়ের কারণ:</b>\n\n"
                            + "<b>হুদাইবিয়ার সন্ধির শর্ত ভঙ্গ:</b>\n"
                            + "হুদাইবিয়ার সন্ধি অনুযায়ী কুরাইশ ও মুসলমানদের মধ্যে ১০ বছরের জন্য যুদ্ধবিরতি ছিল। কুরাইশদের মিত্র গোত্র \"বনু বকর\" মুসলমানদের মিত্র \"বনু খুযা’আ\"র উপর আক্রমণ করে। কুরাইশরা বনু বকরের এই আক্রমণে সহায়তা করে, যা সন্ধির শর্ত ভঙ্গ করে।\n\n"
                            + "<b>মুসলমানদের প্রতিক্রিয়া:</b>\n"
                            + "বনু খুযা’আ গোত্র নবীজির (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) কাছে সাহায্যের আবেদন করে। নবীজি এই অবস্থাকে ইসলামের পক্ষে চূড়ান্ত পদক্ষেপ নেওয়ার সুযোগ হিসেবে গ্রহণ করেন।\n\n"
                            + "<b>মক্কা বিজয়ের প্রস্তুতি:</b>\n\n"
                            + "<b>মুসলিম বাহিনীর সংগঠন:</b>\n"
                            + "• নবীজি (সাঃ) প্রায় ১০,০০০ সৈন্যের একটি বিশাল বাহিনী সংগঠিত করেন।\n"
                            + "• বাহিনীর সদস্যরা ছিল সাহাবী, মুহাজির এবং আনসারদের সমন্বয়ে গঠিত।\n\n"
                            + "<b>গোপন অভিযানের পরিকল্পনা:</b>\n"
                            + "নবীজি (সাঃ) মক্কা অভিযান সম্পর্কে কুরাইশদের কাছে কোনো তথ্য প্রকাশ করতে নিষেধ করেন। অভিযানটি গোপন রাখা হয়, যাতে শত্রু প্রস্তুতির সুযোগ না পায়।\n\n"
                            + "<b>মক্কা বিজয়ের গুরুত্বপূর্ণ ঘটনা:</b>\n\n"
                            + "<b>মক্কায় প্রবেশ:</b>\n"
                            + "• মুসলিম বাহিনী মক্কার চারটি দিক থেকে প্রবেশ করে।\n"
                            + "• নবীজি (সাঃ) কোনো রক্তপাত ছাড়াই মক্কা দখল করতে সক্ষম হন।\n\n"
                            + "<b>নবীজি (সাঃ) কুরাইশদের কাছে ক্ষমা ঘোষণা করেন:</b>\n"
                            + "<i>\"আজ তোমাদের কোনো দোষ দেওয়া হবে না। যাও, তোমরা মুক্ত।\"</i>\n"
                            + "<b>[সহীহ মুসলিম]</b>\n\n"
                            + "<b>কাবা শরীফ পরিষ্কার:</b>\n"
                            + "নবীজি (সাঃ) কাবা থেকে মূর্তি এবং শিরকের নিদর্শনসমূহ অপসারণ করেন। তিনি বলেন:\n"
                            + "“সত্য এসেছে এবং মিথ্যা বিলীন হয়ে গেছে। নিশ্চয়ই মিথ্যা বিলীন হওয়ারই যোগ্য।\"\n"
                            + "<b>[সূরা আল-ইসরা: ৮১]</b>\n\n"
                            + "<b>ইসলামে গণপ্রবেশ:</b>\n"
                            + "মক্কা বিজয়ের পর অনেক কুরাইশ ইসলাম গ্রহণ করে। ইসলামের বার্তা আরব উপদ্বীপে ব্যাপকভাবে ছড়িয়ে পড়ে।\n\n"
                            + "<b>কুরআনে মক্কা বিজয়ের উল্লেখ:</b>\n\n"
                            + "إِذَا جَآءَ نَصْرُ ٱللَّهِ وَٱلْفَتْحُ • وَرَأَيْتَ ٱلنَّاسَ يَدْخُلُونَ فِى دِينِ ٱللَّهِ أَفْوَاجًا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "যখন আল্লাহর সাহায্য ও বিজয় আসবে, এবং তুমি দেখবে মানুষ দলে দলে আল্লাহর দ্বীনে প্রবেশ করছে।\n"
                            + "<b>[সূরা আন-নাসর: ১-২]</b>\n\n"
                            + "<b>শিরকের অবসান:</b>\n\n"
                            + "وَقُلْ جَآءَ ٱلْحَقُّ وَزَهَقَ ٱلْبَٰطِلُ ۚ إِنَّ ٱلْبَٰطِلَ كَانَ زَهُوقًا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "বলুন, সত্য এসেছে এবং মিথ্যা বিলীন হয়ে গেছে। নিশ্চয়ই মিথ্যা বিলীন হওয়ারই যোগ্য।\n"
                            + "<b>[সূরা আল-ইসরা: ৮১]</b>",
                    "The Conquest of Makkah (Fath Makkah) in Ramadan 8 AH (630 CE) was the bloodless, glorious triumph of Islam that unified the Arabian Peninsula and purified the Sanctuary.\n\n"
                            + "<b>Causes:</b>\n"
                            + "Banu Bakr (Quraish allies) attacked Banu Khuza'ah (Muslim allies) inside the sacred boundary, directly breaching the Treaty of Hudaybiyyah with Quraish arms.\n\n"
                            + "<b>Bloodless Mobilization:</b>\n"
                            + "The Prophet (ﷺ) assembled a disciplined army of 10,000 companions, marching in four columns to enter Makkah peacefully without bloodshed.\n\n"
                            + "<b>Universal Amnesty and Magnanimity:</b>\n"
                            + "Standing before the trembling Quraish who had persecuted him for two decades, the Prophet (ﷺ) asked: \"What do you think I will do with you?\" They answered: \"Good, noble brother and son of a noble brother.\" He proclaimed:\n"
                            + "<i>\"No blame will there be upon you today. Go, for you are free!\"</i>\n"
                            + "<b>[Sahih Muslim]</b>\n\n"
                            + "<b>Purification of the Ka'bah:</b>\n"
                            + "The Prophet (ﷺ) struck 360 idols with his staff, reciting:\n\n"
                            + "وَقُلْ جَآءَ ٱلْحَقُّ وَزَهَقَ ٱلْبَٰطِلُ ۚ إِنَّ ٱلْبَٰطِلَ كَانَ زَهُوقًا\n\n"
                            + "<b>Translation:</b>\n"
                            + "And say, 'Truth has come, and falsehood has departed. Indeed is falsehood, [by nature], ever bound to depart.'\n"
                            + "<b>[Surah Al-Isra 17:81]</b>\n\n"
                            + "<b>Quranic Declaration:</b>\n\n"
                            + "إِذَا جَآءَ نَصْرُ ٱللَّهِ وَٱلْفَتْحُ • وَرَأَيْتَ ٱلنَّاسَ يَدْخُلُونَ فِى دِينِ ٱللَّهِ أَفْوَاجًا\n\n"
                            + "<b>Translation:</b>\n"
                            + "When the victory of Allah has come and the conquest, And you see the people entering into the religion of Allah in multitudes.\n"
                            + "<b>[Surah An-Nasr 110:1-2]</b>"
            ));

            // 9. ইসলামী রাষ্ট্রব্যবস্থা প্রতিষ্ঠা
            list.add(new ProphetOverviewTopicItem(
                    9,
                    "ইসলামী রাষ্ট্রব্যবস্থা প্রতিষ্ঠা",
                    "Establishment of the Islamic State System",
                    "ইসলামী রাষ্ট্রব্যবস্থা এমন একটি শাসনব্যবস্থা, যা সম্পূর্ণরূপে কুরআন এবং হাদিসের উপর ভিত্তি করে পরিচালিত হয়। এটি আল্লাহর সার্বভৌমত্ব এবং শরিয়াহর বিধান প্রতিষ্ঠার মাধ্যমে ন্যায়, সাম্য, এবং শান্তি নিশ্চিত করার উদ্দেশ্যে কাজ করে। ইসলামী রাষ্ট্রব্যবস্থা প্রতিষ্ঠার মূল সূচনা হয় মদিনায়, যখন হযরত মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) মদিনায় হিজরত করেন এবং প্রথম ইসলামী রাষ্ট্র গঠন করেন। এটি ছিল ইসলামের সামাজিক, রাজনৈতিক, এবং অর্থনৈতিক ব্যবস্থার বাস্তবায়নের সূচনা।\n\n"
                            + "<b>ইসলামী রাষ্ট্রের মূলনীতি:</b>\n\n"
                            + "<b>আল্লাহর সার্বভৌমত্ব:</b>\n\n"
                            + "إِنِ ٱلْحُكْمُ إِلَّا لِلَّهِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হুকুম দেওয়ার অধিকার কেবল আল্লাহর।\n"
                            + "<b>[সূরা ইউসুফ: ৪০]</b>\n\n"
                            + "ইসলামী রাষ্ট্রে আল্লাহর আইনই সর্বোচ্চ। আইন প্রণয়ন এবং বিচার শরিয়াহ অনুযায়ী পরিচালিত হয়।\n\n"
                            + "<b>ন্যায়বিচার প্রতিষ্ঠা:</b>\n\n"
                            + "إِنَّ ٱللَّهَ يَأْمُرُ بِٱلْعَدْلِ وَٱلْإِحْسَـٰنِ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নিশ্চয়ই আল্লাহ ন্যায়বিচার ও উত্তম আচরণের আদেশ দেন।\n"
                            + "<b>[সূরা আন-নাহল: ৯০]</b>\n\n"
                            + "প্রতিটি নাগরিক, মুসলিম বা অমুসলিম, আইন এবং ন্যায়বিচারে সমান।\n\n"
                            + "<b>শরিয়াহর শাসন:</b>\n"
                            + "• শরিয়াহ ইসলামী রাষ্ট্রের মূল আইন।\n"
                            + "• এর ভিত্তিতে ব্যক্তি, পরিবার, সমাজ, এবং রাষ্ট্র পরিচালিত হয়।\n\n"
                            + "<b>পরামর্শ ভিত্তিক শাসন (শূরা):</b>\n\n"
                            + "وَأَمْرُهُمْ شُورَىٰ بَيْنَهُمْ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "এবং তাদের কাজ পরামর্শের ভিত্তিতে হয়।\n"
                            + "<b>[সূরা আশ-শূরা: ৩৮]</b>\n\n"
                            + "ইসলামী রাষ্ট্রের শাসনব্যবস্থা পরামর্শের ভিত্তিতে পরিচালিত হয়।\n\n"
                            + "<b>সাম্য ও সামাজিক ন্যায়:</b>\n\n"
                            + "يَٰٓأَيُّهَا ٱلنَّاسُ إِنَّا خَلَقْنَـٰكُم مِّن ذَكَرٍ وَأُنثَىٰ وَجَعَلْنَـٰكُمْ شُعُوبًۭا وَقَبَآئِلَ لِتَعَارَفُوٓا۟ ۚ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে মানুষ! আমি তোমাদের সৃষ্টি করেছি একজন পুরুষ ও একজন নারী থেকে এবং তোমাদেরকে বিভিন্ন জাতি ও গোষ্ঠী করেছি, যাতে তোমরা একে অপরকে চেনো।\n"
                            + "<b>[সূরা আল-হুজুরাত: ১৩]</b>\n\n"
                            + "<b>ইসলামী রাষ্ট্রের ভিত্তি: মদিনা সনদ:</b>\n\n"
                            + "<b>মদিনা সনদের সংজ্ঞা:</b>\n"
                            + "মদিনা সনদ ইসলামের প্রথম লিখিত সংবিধান, যা নবীজি (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) মদিনায় প্রতিষ্ঠা করেন।\n\n"
                            + "<b>মূলনীতি:</b>\n"
                            + "• মুসলমান এবং অমুসলমানদের মধ্যে শান্তি ও সহাবস্থান নিশ্চিত করা।\n"
                            + "• আইনশৃঙ্খলা বজায় রাখা এবং বহিঃশত্রুর আক্রমণ থেকে মদিনা রক্ষা করা।\n"
                            + "• ধর্মীয় স্বাধীনতা নিশ্চিত করা।\n\n"
                            + "<b>প্রধান শর্তাবলী:</b>\n"
                            + "• প্রত্যেক সম্প্রদায় তাদের ধর্ম পালনের স্বাধীনতা পাবে।\n"
                            + "• মদিনা সবার জন্য একটি অভিন্ন নিরাপত্তার ক্ষেত্র।\n"
                            + "• কেউ মদিনার বিরুদ্ধে ষড়যন্ত্র করতে পারবে না।\n\n"
                            + "<b>ইসলামী রাষ্ট্রের কাঠামো:</b>\n\n"
                            + "<b>নির্বাহী বিভাগ:</b>\n"
                            + "রাষ্ট্রপ্রধান (খলিফা) ইসলামী রাষ্ট্রের প্রধান নির্বাহী। তার দায়িত্ব হলো শরিয়াহ বাস্তবায়ন এবং জনগণের কল্যাণ নিশ্চিত করা।\n\n"
                            + "<b>আইন বিভাগ:</b>\n"
                            + "শরিয়াহর ভিত্তিতে আইন প্রণয়ন এবং বিচার ব্যবস্থা পরিচালিত হয়।\n\n"
                            + "<b>আর্থিক বিভাগ:</b>\n"
                            + "ইসলামী অর্থনীতি যাকাত, খারাজ, এবং অন্যান্য শরিয়াহভিত্তিক উৎস থেকে পরিচালিত হয়।\n\n"
                            + "خُذْ مِنْ أَمْوَٰلِهِمْ صَدَقَةًۭ تُطَهِّرُهُمْ وَتُزَكِّيهِم بِهَا\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তাদের সম্পদ থেকে সদকা গ্রহণ করো, যা তাদেরকে পবিত্র করবে এবং পরিশুদ্ধ করবে।\n"
                            + "<b>[সূরা আত-তাওবা: ১০৩]</b>\n\n"
                            + "<b>সামরিক বিভাগ:</b>\n"
                            + "রাষ্ট্রের প্রতিরক্ষা নিশ্চিত করা এবং শান্তি বজায় রাখা।\n\n"
                            + "<b>শাসকের জবাবদিহিতা:</b>\n"
                            + "ইসলামী রাষ্ট্রে শাসক আল্লাহর এবং জনগণের কাছে জবাবদিহি। নবীজি (সাঃ) বলেছেন, \"কোনো শাসক যদি ন্যায়পরায়ণ না হয়, তবে তাকে কেয়ামতের দিনে শাস্তি দেওয়া হবে।\" <b>[সহীহ বুখারী]</b>\n\n"
                            + "<b>ব্যক্তিগত স্বাধীনতা:</b>\n"
                            + "প্রতিটি নাগরিক ধর্ম, সম্পদ, এবং জীবনযাপনের স্বাধীনতা ভোগ করবে।\n\n"
                            + "<b>শিক্ষা ও নৈতিকতা:</b>\n"
                            + "ইসলামী রাষ্ট্রে শিক্ষা ও নৈতিকতার প্রতি বিশেষ গুরুত্ব দেওয়া হয়।\n\n"
                            + "<b>মানবাধিকার রক্ষা:</b>\n"
                            + "ইসলামী রাষ্ট্র প্রত্যেক নাগরিকের মৌলিক মানবাধিকার নিশ্চিত করে।\n\n"
                            + "<b>ইসলামী রাষ্ট্র প্রতিষ্ঠার ফলাফল:</b>\n\n"
                            + "<b>শান্তি ও স্থিতিশীলতা:</b> ন্যায়বিচার ও সাম্যের ভিত্তিতে একটি স্থিতিশীল সমাজ প্রতিষ্ঠিত হয়।\n"
                            + "<b>সামাজিক উন্নয়ন:</b> যাকাত ও দানের মাধ্যমে দারিদ্র্য বিমোচন এবং অর্থনৈতিক উন্নয়ন নিশ্চিত হয়।\n"
                            + "<b>ধর্মীয় সম্প্রীতি:</b> ইসলাম ধর্মীয় স্বাধীনতা এবং ভ্রাতৃত্ব নিশ্চিত করে।\n"
                            + "<b>ইসলামের প্রসার:</b> ইসলামী রাষ্ট্র একটি আদর্শ সমাজ গঠন করে, যা ইসলামের বিস্তারকে ত্বরান্বিত করে।",
                    "The Islamic governance framework founded by Prophet Muhammad (ﷺ) in Madinah was rooted in the sovereignty of Allah, constitutional rule of law, egalitarian justice, and societal welfare.\n\n"
                            + "<b>Core Principles:</b>\n\n"
                            + "<b>Sovereignty of Allah:</b>\n\n"
                            + "إِنِ ٱلْحُكْمُ إِلَّا لِلَّهِ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Legislation is not but for Allah.\n"
                            + "<b>[Surah Yusuf 12:40]</b>\n\n"
                            + "<b>Absolute Justice and Equity:</b>\n\n"
                            + "إِنَّ ٱللَّهَ يَأْمُرُ بِٱلْعَدْلِ وَٱلْإِحْسَـٰنِ\n\n"
                            + "<b>Translation:</b>\n"
                            + "Indeed, Allah orders justice and good conduct.\n"
                            + "<b>[Surah An-Nahl 16:90]</b>\n\n"
                            + "<b>Consultative Governance (Shura):</b>\n\n"
                            + "وَأَمْرُهُمْ شُورَىٰ بَيْنَهُمْ\n\n"
                            + "<b>Translation:</b>\n"
                            + "And whose affair is [determined by] consultation among themselves.\n"
                            + "<b>[Surah Ash-Shura 42:38]</b>\n\n"
                            + "<b>Universal Human Equality:</b>\n\n"
                            + "يَٰٓأَيُّهَا ٱلنَّاسُ إِنَّا خَلَقْنَـٰكُم مِّن ذَكَرٍ وَأُنثَىٰ...\n\n"
                            + "<b>Translation:</b>\n"
                            + "O mankind, indeed We have created you from male and female and made you peoples and tribes that you may know one another.\n"
                            + "<b>[Surah Al-Hujurat 49:13]</b>\n\n"
                            + "<b>Institutional Pillars:</b>\n"
                            + "• <b>Executive:</b> Led by the Head of State upholding public welfare and divine law.\n"
                            + "• <b>Judiciary:</b> Independent courts applying Quran and Sunnah without social favoritism.\n"
                            + "• <b>Treasury (Bayt al-Mal):</b> Social security funded by Zakat and Sadaqah.\n"
                            + "• <b>Ruler Accountability:</b> Leaders are strictly accountable to Allah and the citizens."
            ));

            // 10. সাহাবিদের শিক্ষাদান
            list.add(new ProphetOverviewTopicItem(
                    10,
                    "সাহাবিদের শিক্ষাদান",
                    "Teaching and Nurturing the Companions",
                    "সাহাবিরা ছিলেন ইসলামের প্রথম প্রজন্ম, যারা সরাসরি নবী মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম)-এর সান্নিধ্যে থেকে শিক্ষা গ্রহণ করেছেন। ইসলামের শিক্ষাকে বিস্তার এবং সঠিকভাবে সংরক্ষণে সাহাবিদের ভূমিকা অনন্য। নবীজি (সাঃ) তাদের শিক্ষাদানের মাধ্যমে শুধু ধর্মীয় দিকেই নয়, বরং তাদের সামাজিক, নৈতিক, এবং সাংস্কৃতিক জীবনেও পরিবর্তন আনেন।\n\n"
                            + "<b>নবীজির শিক্ষাদান পদ্ধতি:</b>\n\n"
                            + "<b>সরাসরি শিক্ষাদান:</b>\n"
                            + "নবীজি (সাঃ) সাহাবিদের সরাসরি কুরআন তিলাওয়াত এবং এর অর্থ ও ব্যাখ্যা শিক্ষা দিতেন। সাহাবিদের জীবনযাপনে কুরআনের বাস্তবায়ন নিশ্চিত করতেন।\n\n"
                            + "هُوَ ٱلَّذِى بَعَثَ فِى ٱلْأُمِّيِّـۧنَ رَسُولًۭا مِّنْهُمْ يَتْلُوا۟ عَلَيْهِمْ ءَايَـٰتِهِۦ وَيُزَكِّيهِمْ وَيُعَلِّمُهُمُ ٱلْكِتَـٰبَ وَٱلْحِكْمَةَ\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তিনিই সেই সত্তা, যিনি উম্মিদের মধ্যে একজন রাসূল প্রেরণ করেছেন, যিনি তাদের কাছে তাঁর আয়াত পাঠ করেন, তাদের পরিশুদ্ধ করেন এবং তাদের কিতাব ও হিকমত শিক্ষা দেন।\n"
                            + "<b>[সূরা আল-জুমুআ: ২]</b>\n\n"
                            + "<b>প্রশ্নোত্তরের মাধ্যমে শিক্ষা:</b>\n"
                            + "সাহাবিরা নবীজির কাছে প্রশ্ন করতেন, এবং তিনি তাদের সহজ ভাষায় উত্তর দিতেন। তিনি জ্ঞানের গভীরতা বাড়ানোর জন্য বিভিন্ন উদাহরণ ব্যবহার করতেন।\n\n"
                            + "<b>ব্যক্তিগত এবং সমষ্টিগত শিক্ষাদান:</b>\n"
                            + "নবীজি (সাঃ) ব্যক্তিগতভাবে সাহাবিদের শিক্ষা দিতেন এবং তাদের ভুল সংশোধন করতেন। তিনি জুমা খুতবা, মসজিদে নববীতে আলোচনা, এবং যুদ্ধের সময় দিকনির্দেশনার মাধ্যমে সমষ্টিগত শিক্ষা প্রদান করতেন।\n\n"
                            + "<b>প্রেরণা এবং উত্সাহ:</b>\n"
                            + "নবীজি (সাঃ) সাহাবিদের প্রেরণা দিতেন এবং তাদের ভাল কাজের প্রশংসা করতেন। যেমন, হযরত মুআয (রাঃ)-কে দাওয়াতের কাজে পাঠানোর আগে তাকে দিকনির্দেশনা দেন:\n"
                            + "<i>\"তুমি লোকদেরকে সহজ করো, কঠিন করো না। আনন্দ দাও, ঘৃণা সৃষ্টি করো না।\"</i>\n"
                            + "<b>[সহীহ বুখারী]</b>\n\n"
                            + "<b>আমলের মাধ্যমে শিক্ষা:</b>\n"
                            + "নবীজি (সাঃ) তার নিজের কর্মের মাধ্যমে সাহাবিদের শিখিয়েছেন। যেমন, নামাজ পড়ার সময় তিনি বলতেন:\n"
                            + "<i>\"তোমরা আমাকে দেখে নামাজ পড়ো।\"</i>\n"
                            + "<b>[সহীহ বুখারী: ৬৩১১]</b>\n\n"
                            + "<b>শিক্ষাদানের গুরুত্বপূর্ণ বিষয়সমূহ:</b>\n\n"
                            + "• <b>কুরআনের শিক্ষা:</b> কুরআন তিলাওয়াত, এর অর্থ ও ব্যাখ্যা, এবং বাস্তব জীবনে প্রয়োগ শেখানো।\n"
                            + "• <b>হাদিসের শিক্ষা:</b> নবীজির কথা, কাজ, এবং সমর্থনের মাধ্যমে হাদিস সংরক্ষণ এবং শিক্ষা।\n"
                            + "• <b>নামাজ ও ইবাদত:</b> নামাজ, রোজা, যাকাত, এবং হজের সঠিক পদ্ধতি শেখানো।\n"
                            + "• <b>নৈতিকতা এবং চরিত্র গঠন:</b> সত্যবাদিতা, ধৈর্য, ক্ষমা, এবং নম্রতার গুণাবলি শেখানো।\n"
                            + "• <b>সামাজিক এবং পারিবারিক জীবন:</b> প্রতিবেশী, পরিবার, এবং সমাজের প্রতি দায়িত্ব-কর্তব্য শেখানো।\n"
                            + "• <b>দাওয়াত এবং ইসলামের প্রচার:</b> নবীজি (সাঃ) সাহাবিদের দাওয়াতের কৌশল এবং ইসলামের মূল বার্তা ছড়ানোর জন্য প্রস্তুত করেন।\n\n"
                            + "<b>সাহাবিদের মধ্যে বিশেষ শিক্ষার্থী:</b>\n\n"
                            + "• <b>হযরত আবু বকর (রাঃ):</b> ইসলামের প্রথম খলিফা এবং নবীজির ঘনিষ্ঠ সঙ্গী। নৈতিকতা, দায়িত্বশীলতা এবং দাওয়াতের শিক্ষা গ্রহণ করেন।\n"
                            + "• <b>হযরত উমর (রাঃ):</b> ন্যায়বিচার এবং শাসনব্যবস্থার দিক থেকে নবীজির কাছ থেকে শিক্ষা নেন।\n"
                            + "• <b>হযরত উসমান (রাঃ):</b> কুরআন সংরক্ষণ এবং উদারতার দৃষ্টান্ত স্থাপন করেন।\n"
                            + "• <b>হযরত আলী (রাঃ):</b> জ্ঞান, কুরআনের গভীর ব্যাখ্যা, এবং সাহসিকতায় অগ্রগামী।\n"
                            + "• <b>হযরত আবদুল্লাহ ইবনে আব্বাস (রাঃ):</b> নবীজি (সাঃ) তাকে কুরআনের তাফসিরের জন্য বিশেষভাবে শিক্ষা দেন।",
                    "The noble Companions (Sahabah) were the first generation of Muslims nurtured directly by the Prophet Muhammad (ﷺ). His pedagogical approach harmonized spiritual purification (Tazkiyah), intellectual illumination (Ta'lim), and exemplary character.\n\n"
                            + "<b>Prophetic Pedagogical Methods:</b>\n"
                            + "• <b>Direct Revelation and Explanation:</b>\n\n"
                            + "هُوَ ٱلَّذِى بَعَثَ فِى ٱلْأُمِّيِّـۧنَ رَسُولًۭا مِّنْهُمْ يَتْلُوا۟ عَلَيْهِمْ ءَايَـٰتِهِۦ وَيُزَكِّيهِمْ وَيُعَلِّمُهُمُ ٱلْكِتَـٰبَ وَٱلْحِكْمَةَ\n\n"
                            + "<b>Translation:</b>\n"
                            + "It is He who has sent among the unlettered a Messenger from themselves reciting to them His verses and purifying them and teaching them the Book and wisdom.\n"
                            + "<b>[Surah Al-Jumu'ah 62:2]</b>\n\n"
                            + "• <b>Interactive Q&A:</b> Using parables and thought-provoking questions.\n"
                            + "• <b>Encouragement and Facilitation:</b> Admonishing Mu'adh ibn Jabal (RA): \"Make things easy, do not make them difficult; give glad tidings, do not repel people.\" <b>[Sahih al-Bukhari]</b>\n"
                            + "• <b>Practical Role Modeling:</b> \"Pray as you have seen me pray.\" <b>[Sahih al-Bukhari: 6311]</b>\n\n"
                            + "<b>Eminent Students of the Prophetic Academy:</b>\n"
                            + "• <b>Abu Bakr (RA):</b> Deepest devotion, discernment, and leadership.\n"
                            + "• <b>Umar (RA):</b> Jurisprudence and administrative justice.\n"
                            + "• <b>Uthman (RA):</b> Quranic compilation and generosity.\n"
                            + "• <b>Ali (RA):</b> Eloquence, valor, and profound wisdom.\n"
                            + "• <b>Ibn Abbas (RA):</b> Master of Quranic exegesis (Tarjuman al-Quran)."
            ));

            // 11. শেষ হজ এবং বিদায় খুতবা
            list.add(new ProphetOverviewTopicItem(
                    11,
                    "শেষ হজ এবং বিদায় খুতবা",
                    "The Farewell Pilgrimage and Sermon",
                    "শেষ হজ বা \"হজ্জাতুল বিদা\" ছিল ইসলামের নবী মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম)-এর জীবনের একমাত্র পূর্ণাঙ্গ হজ। এটি ১০ হিজরির জ্বিলহজ্জ মাসে (৬৩২ খ্রিস্টাব্দ) সম্পন্ন হয়। এই হজ ছিল ইসলামের পূর্ণাঙ্গ দিকনির্দেশনা দেওয়ার এবং নবীজির (সাঃ) জীবনের গুরুত্বপূর্ণ বার্তা পৌঁছে দেওয়ার এক ঐতিহাসিক মুহূর্ত। এই হজে নবীজি তার বিদায় খুতবা প্রদান করেন, যা ইসলামের সারমর্ম এবং মানবতার সর্বোত্তম দিকনির্দেশনা।\n\n"
                            + "<b>হজের ঘোষণা:</b>\n"
                            + "• নবীজি (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) মদিনায় ঘোষণা দেন যে তিনি হজ পালন করবেন।\n"
                            + "• এই ঘোষণায় ১,২৪,০০০ সাহাবি এবং অন্যান্য মুসলিম তার সঙ্গে যোগ দেন।\n\n"
                            + "<b>মক্কায় আগমন:</b>\n"
                            + "• ২৫ জিলকদ, ১০ হিজরিতে নবীজি (সাঃ) মদিনা থেকে মক্কার উদ্দেশ্যে রওনা হন।\n"
                            + "• মক্কায় পৌঁছে নবীজি কাবা তাওয়াফ, সাঈ এবং অন্যান্য হজের আনুষ্ঠানিকতা শুরু করেন।\n\n"
                            + "<b>আরাফার ময়দানে অবস্থান:</b>\n"
                            + "• ৯ জিলহজ্জে (আরাফার দিন) নবীজি (সাঃ) আরাফার ময়দানে জুমার দিনে ঐতিহাসিক বিদায় খুতবা প্রদান করেন।\n"
                            + "• এটি ইসলামের পূর্ণতার ঘোষণা এবং নবুয়তের সমাপ্তির চিহ্ন বহন করে।\n\n"
                            + "<b>বিদায় হজের খুতবা:</b>\n\n"
                            + "<b>আল্লাহর প্রশংসা এবং নির্দেশনা:</b>\n\n"
                            + "الحمد لله نحمده ونستعينه ونستغفره، ونعوذ بالله من شرور أنفسنا ومن سيئات أعمالنا، من يهده الله فلا مضل له ومن يضلل فلا هادي له.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "সমস্ত প্রশংসা আল্লাহর জন্য। আমরা তারই ইবাদত করি, তার কাছে সাহায্য চাই, এবং তার কাছেই তাওবা করি। আমরা আমাদের নফসের খারাপি এবং আমাদের কর্মের কু-পরিণতি থেকে আল্লাহর কাছে আশ্রয় চাই। আল্লাহ যাকে সঠিক পথে পরিচালিত করেন, তাকে কেউ পথভ্রষ্ট করতে পারে না, এবং যাকে তিনি পথভ্রষ্ট করেন, তাকে কেউ সঠিক পথে আনতে পারে না।\n"
                            + "<b>[সহীহ মুসলিম: ৮৬৯]</b>\n\n"
                            + "<b>মানবাধিকার এবং নিষিদ্ধ বিষয়সমূহ:</b>\n\n"
                            + "أيها الناس، إن دماءكم وأموالكم وأعراضكم حرام عليكم كحرمة يومكم هذا، في شهركم هذا، في بلدكم هذا.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে মানুষ! তোমাদের রক্ত, সম্পদ, এবং সম্মান একে অপরের জন্য পবিত্র, যেমন আজকের দিন, এই মাস এবং এই শহর পবিত্র।\n"
                            + "<b>[সহীহ বুখারী: ১৭৪১, সহীহ মুসলিম: ১২১৮]</b>\n\n"
                            + "<b>সুদ নিষিদ্ধ:</b>\n\n"
                            + "ألا وإن كل ربا في الجاهلية موضوع، ولكم رؤوس أموالكم، لا تظلمون ولا تظلمون. وأول ربا أضعه ربا عمي العباس بن عبد المطلب.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "শোনো! জাহেলিয়াতের সব ধরনের সুদ বাতিল করা হয়েছে। তোমাদের মূলধন তোমাদের জন্য থাকবে। তোমরা কারো প্রতি অন্যায় করবে না এবং কেউ তোমাদের প্রতি অন্যায় করবে না। এবং প্রথম যে সুদ আমি বাতিল করছি, তা আমার চাচা আব্বাস ইবনে আবদুল মুত্তালিবের সুদ।\n"
                            + "<b>[সহীহ মুসলিম: ১২১৮]</b>\n\n"
                            + "<b>নারীর অধিকার:</b>\n\n"
                            + "استوصوا بالنساء خيرًا، فإنهن عوان عندكم، أخذتموهن بأمانة الله، واستحللتم فروجهن بكلمة الله.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নারীদের প্রতি সদাচরণ করো। তারা তোমাদের উপর নির্ভরশীল। আল্লাহর আমানত হিসেবে তাদের গ্রহণ করেছ এবং আল্লাহর বিধান অনুযায়ী তাদের সম্মান করতে বাধ্য।\n"
                            + "<b>[সহীহ মুসলিম: ১২১৮]</b>\n\n"
                            + "<b>জাতিগত সাম্য:</b>\n\n"
                            + "\"يا أيها الناس، ألا إن ربكم واحد، وإن أباكم واحد. ألا لا فضل لعربي على عجمي، ولا لعجمي على عربي، ولا لأحمر على أسود، ولا لأسود على أحمر إلا بالتقوى.\"\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে মানুষ! জেনে রাখো, তোমাদের প্রভু একজন এবং তোমাদের পিতা একজন। কোনো আরবের উপর অনারবের, কোনো অনারবের উপর আরবের, কোনো লাল চামড়ার উপর কালো চামড়ার, এবং কোনো কালো চামড়ার উপর লাল চামড়ার শ্রেষ্ঠত্ব নেই। শ্রেষ্ঠত্ব কেবল তাকওয়ার ভিত্তিতে।\n"
                            + "<b>[মুসনাদে আহমদ: ২৩৪৮৯]</b>\n\n"
                            + "<b>ইসলামের পূর্ণতা:</b>\n\n"
                            + "اليوم أكملت لكم دينكم وأتممت عليكم نعمتي ورضيت لكم الإسلام دينا.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আজ আমি তোমাদের জন্য তোমাদের দ্বীনকে পূর্ণাঙ্গ করেছি এবং আমার নিয়ামত তোমাদের উপর পরিপূর্ণ করেছি। ইসলামকে তোমাদের জন্য জীবনব্যবস্থা হিসেবে পছন্দ করেছি।\n"
                            + "<b>[সূরা আল-মায়িদা: ৩]</b>\n\n"
                            + "<b>কুরআন ও সুন্নাহর অনুসরণ:</b>\n\n"
                            + "تركت فيكم ما إن اعتصمتم به فلن تضلوا بعدي أبدًا: كتاب الله وسنتي.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আমি তোমাদের মধ্যে দুটি জিনিস রেখে যাচ্ছি। যদি এগুলো আঁকড়ে ধরো, তবে কখনো পথভ্রষ্ট হবে না: আল্লাহর কিতাব (কুরআন) এবং আমার সুন্নাহ।\n"
                            + "<b>[মুয়াত্তা মালিক: ১৬২৮]</b>\n\n"
                            + "<b>নবীর সাক্ষ্য:</b>\n\n"
                            + "أيها الناس، هل بلغت؟ فقالوا: نعم. فقال: اللهم اشهد.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "হে মানুষ! আমি কি বার্তা পৌঁছে দিয়েছি? সাহাবিরা বললেন: হ্যাঁ, নিশ্চয়ই। তখন নবীজি বললেন: হে আল্লাহ! আপনি সাক্ষী থাকুন।\n"
                            + "<b>[সহীহ মুসলিম: ১২১৮]</b>",
                    "The Farewell Pilgrimage (Hajjat al-Wida) in Dhul Hijjah 10 AH (632 CE) was the sole complete Hajj performed by Prophet Muhammad (ﷺ). Before a gathering of 124,000 companions on the Plain of Arafat, he delivered the historic Farewell Address—a timeless charter of universal human rights and Islamic ethics.\n\n"
                            + "<b>Sanctity of Human Life, Property, and Honor:</b>\n\n"
                            + "أيها الناس، إن دماءكم وأموالكم وأعراضكم حرام عليكم...\n\n"
                            + "<b>Translation:</b>\n"
                            + "O people, verily your blood, your property, and your honor are sacred to one another, just as the sacredness of this day of yours, in this month of yours, in this city of yours.\n"
                            + "<b>[Sahih al-Bukhari: 1741, Sahih Muslim: 1218]</b>\n\n"
                            + "<b>Abolition of Usury and Exploitation:</b>\n"
                            + "All interest (Riba) from the pre-Islamic Jahiliyyah was irrevocably abolished.\n\n"
                            + "<b>Rights of Women:</b>\n\n"
                            + "استوصوا بالنساء خيرًا...\n\n"
                            + "<b>Translation:</b>\n"
                            + "Treat women with kindness and goodness, for they are your partners taken under the trust of Allah.\n"
                            + "<b>[Sahih Muslim: 1218]</b>\n\n"
                            + "<b>Universal Racial Equality:</b>\n\n"
                            + "\"يا أيها الناس، ألا إن ربكم واحد، وإن أباكم واحد...\"\n\n"
                            + "<b>Translation:</b>\n"
                            + "O people, your Lord is One and your father is one. An Arab has no superiority over a non-Arab, nor a non-Arab over an Arab; a white person has no superiority over a black, nor a black person over a white—except by piety (Taqwa).\n"
                            + "<b>[Musnad Ahmad: 23489]</b>\n\n"
                            + "<b>Perfection of Divine Guidance:</b>\n\n"
                            + "اليوم أكملت لكم دينكم وأتممت عليكم نعمتي ورضيت لكم الإسلام دينا.\n\n"
                            + "<b>Translation:</b>\n"
                            + "This day I have perfected for you your religion and completed My favor upon you and have approved for you Islam as your religion.\n"
                            + "<b>[Surah Al-Ma'idah 5:3]</b>\n\n"
                            + "<b>Adherence to Quran and Sunnah:</b>\n"
                            + "\"I leave among you two things; as long as you hold fast to them, you will never go astray: the Book of Allah and my Sunnah.\" <b>[Muwatta Malik: 1628]</b>"
            ));

            // 12. নবীজীর শেষ জীবন
            list.add(new ProphetOverviewTopicItem(
                    12,
                    "নবীজীর শেষ জীবন",
                    "The Final Days of the Prophet ﷺ",
                    "হযরত মুহাম্মদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম)-এর শেষ জীবনের ঘটনা ইসলামের ইতিহাসে এক গুরুত্বপূর্ণ অধ্যায়। এটি ইসলামের চূড়ান্ত প্রতিষ্ঠা এবং নবীজির বিদায়ের প্রেক্ষাপট। শেষ জীবনে নবীজি (সাঃ) ইসলামের পূর্ণাঙ্গতা ঘোষণা করেন এবং উম্মাহর জন্য গুরুত্বপূর্ণ দিকনির্দেশনা রেখে যান।\n\n"
                            + "<b>শেষ জীবনের গুরুত্বপূর্ণ ঘটনা:</b>\n\n"
                            + "<b>বিদায় হজ (হজ্জাতুল বিদা):</b>\n"
                            + "• ১০ হিজরির জ্বিলহজ্জ মাসে নবীজি (সাঃ) শেষ হজ পালন করেন।\n"
                            + "• তিনি আরাফার ময়দানে বিদায় খুতবা প্রদান করেন, যা মানবতার জন্য চূড়ান্ত নির্দেশনা।\n"
                            + "• বিদায় হজে ইসলামের পূর্ণতা ঘোষণা করা হয়।\n\n"
                            + "اليوم أكملت لكم دينكم وأتممت عليكم نعمتي ورضيت لكم الإسلام دينا.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "আজ আমি তোমাদের জন্য তোমাদের দ্বীনকে পূর্ণাঙ্গ করেছি এবং আমার নিয়ামত তোমাদের উপর পরিপূর্ণ করেছি। ইসলামকে তোমাদের জন্য জীবনব্যবস্থা হিসেবে পছন্দ করেছি।\n"
                            + "<b>[সূরা আল-মায়িদা: ৩]</b>\n\n"
                            + "<b>তাবুক অভিযান:</b>\n"
                            + "• ৯ হিজরিতে নবীজি (সাঃ) তাবুক অভিযানে অংশ নেন।\n"
                            + "• এটি ছিল ইসলামের প্রতিরক্ষার জন্য নবীজির শেষ সামরিক অভিযান।\n"
                            + "• তাবুক অভিযানের মাধ্যমে ইসলামের রাজনৈতিক শক্তি সুদৃঢ় হয়।\n\n"
                            + "<b>বনু সাকিফ গোত্রের ইসলামে প্রবেশ:</b>\n"
                            + "নবীজি (সাঃ)-এর শেষ জীবনে বিভিন্ন গোত্র ইসলামে দলে দলে প্রবেশ করতে থাকে।\n\n"
                            + "وَرَأَيْتَ ٱلنَّاسَ يَدْخُلُونَ فِى دِينِ ٱللَّهِ أَفْوَاجًا.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "তুমি দেখবে মানুষ দলে দলে আল্লাহর দ্বীনে প্রবেশ করছে।\n"
                            + "<b>[সূরা আন-নাসর: ২]</b>\n\n"
                            + "<b>নবীজির অসুস্থতা:</b>\n"
                            + "• বিদায় হজের পর ১০ হিজরির শেষের দিকে নবীজি (সাঃ) অসুস্থ হয়ে পড়েন।\n"
                            + "• অসুস্থ অবস্থায় তিনি হযরত আয়েশা (রাঃ)-এর ঘরে অবস্থান করেন।\n"
                            + "• তিনি মসজিদে নববীতে সাহাবাদের জন্য দিকনির্দেশনা দিতেন এবং তাদের নামাজের নেতৃত্বের জন্য হযরত আবু বকর (রাঃ)-কে নির্দেশ দেন।\n\n"
                            + "<b>বিদায় ওসিয়ত:</b>\n"
                            + "অসুস্থ অবস্থায় নবীজি (সাঃ) সাহাবিদের উদ্দেশ্যে তার শেষ দিকনির্দেশনা দেন:\n"
                            + "<i>\"আমি তোমাদের মধ্যে দুটি জিনিস রেখে যাচ্ছি: আল্লাহর কিতাব (কুরআন) এবং আমার সুন্নাহ। এগুলো আঁকড়ে ধরলে কখনো পথভ্রষ্ট হবে না।\"</i>\n"
                            + "<b>[মুয়াত্তা মালিক: ১৬২৮]</b>\n\n"
                            + "উম্মাহর প্রতি তার গুরুত্বপূর্ণ নির্দেশনাগুলোর মধ্যে ছিল:\n"
                            + "• নামাজ কায়েম করা।\n"
                            + "• দাসদের সাথে সদাচরণ।\n"
                            + "• নারীদের অধিকার রক্ষা।\n"
                            + "• কুরআন ও সুন্নাহর অনুসরণ।\n\n"
                            + "<b>ইন্তেকালের সময়:</b>\n"
                            + "১১ হিজরি, ১২ রবিউল আউয়াল (৬৩২ খ্রিস্টাব্দ), সোমবার নবীজি (সাঃ) ইন্তেকাল করেন। ইন্তেকালের সময় তিনি বারবার \"উম্মাহ\" (তার উম্মতের) জন্য দোয়া করছিলেন।\n\n"
                            + "তার শেষ কথা ছিল:\n\n"
                            + "الصلاة، الصلاة، وما ملكت أيمانكم.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "নামাজ কায়েম করো এবং তোমাদের অধীনস্থদের প্রতি সদাচরণ করো।\n"
                            + "<b>[সহীহ বুখারী: ৪৪৬৭]</b>\n\n"
                            + "<b>নবীজির (সাঃ) ইন্তেকালের পর প্রতিক্রিয়া:</b>\n\n"
                            + "<b>সাহাবিদের শোক:</b>\n"
                            + "• সাহাবিরা গভীর শোকে আচ্ছন্ন হয়ে পড়েন।\n"
                            + "• হযরত উমর (রাঃ) প্রথমে এটি বিশ্বাস করতে অস্বীকৃতি জানান।\n\n"
                            + "<b>হযরত আবু বকর (রাঃ)-এর বক্তব্য:</b>\n"
                            + "হযরত আবু বকর (রাঃ) সাহাবিদের শান্ত করেন এবং বলেন:\n"
                            + "\"যে মুহাম্মদকে (সাঃ) উপাসনা করত, সে জেনে রাখুক যে মুহাম্মদ ইন্তেকাল করেছেন। আর যে আল্লাহকে উপাসনা করত, সে জেনে রাখুক যে আল্লাহ চিরঞ্জীব।\"\n"
                            + "<b>[সহীহ বুখারী: ১২৪১]</b>\n\n"
                            + "তিনি এরপর কুরআনের এই আয়াত তিলাওয়াত করেন:\n\n"
                            + "وَمَا مُحَمَّدٌ إِلَّا رَسُولٌۭ قَدْ خَلَتْ مِن قَبْلِهِ ٱلرُّسُلُ ۚ أَفَإِيْن مَّاتَ أَوْ قُتِلَ ٱنقَلَبْتُمْ عَلَىٰٓ أَعْقَـٰبِكُمْ.\n\n"
                            + "<b>অর্থ:</b>\n"
                            + "মুহাম্মদ তো একজন রাসূল মাত্র, তার পূর্বেও অনেক রাসূল চলে গেছেন। তিনি মারা গেলে বা শহীদ হলে কি তোমরা পেছনে ফিরে যাবে?\n"
                            + "<b>[সূরা আলে ইমরান: ১৪৪]</b>",
                    "The final days of Prophet Muhammad (ﷺ) marked the completion of the divine mission on earth, leaving behind an eternal spiritual and moral legacy for all mankind.\n\n"
                            + "<b>Final Significant Milestones:</b>\n"
                            + "• <b>The Tabuk Expedition (9 AH):</b> The final military expedition solidifying northern borders against Roman forces.\n"
                            + "• <b>Mass Entry into Islam:</b> Delegations from all over Arabia entered the fold of Islam in multitudes.\n"
                            + "<b>[Surah An-Nasr 110:2]</b>\n\n"
                            + "<b>The Prophet's Final Illness:</b>\n"
                            + "Following the Farewell Pilgrimage in 10 AH, the Prophet (ﷺ) took ill and was nursed in the chamber of Sayyidah Aisha (RA). He instructed Abu Bakr as-Siddiq (RA) to lead the congregational prayers in Masjid an-Nabawi.\n\n"
                            + "<b>Final Exhortation to the Ummah:</b>\n"
                            + "<i>\"I leave behind two things; you will never go astray as long as you adhere to them: the Book of Allah and my Sunnah.\"</i>\n"
                            + "<b>[Muwatta Malik: 1628]</b>\n\n"
                            + "<b>His Final Words:</b>\n\n"
                            + "الصلاة، الصلاة، وما ملكت أيمانكم.\n\n"
                            + "<b>Translation:</b>\n"
                            + "[Guard] the prayer, the prayer, and be mindful of those whom your right hands possess.\n"
                            + "<b>[Sahih al-Bukhari: 4467]</b>\n\n"
                            + "<b>Passing to the Highest Companion:</b>\n"
                            + "On Monday, 12th Rabi' al-Awwal 11 AH (632 CE), the Prophet (ﷺ) passed away with his final gaze turned towards the heavens, whispering: <i>\"Bal al-Rafiq al-A'la\" (Rather, to the Highest Companion in Paradise)</i>.\n\n"
                            + "<b>Abu Bakr's Immortal Address:</b>\n"
                            + "Calming the grieving companions, Abu Bakr (RA) addressed the assembly:\n"
                            + "\"Whoever among you worshipped Muhammad, know that Muhammad has died. But whoever worshipped Allah, know that Allah is Ever-Living and never dies.\"\n"
                            + "<b>[Sahih al-Bukhari: 1241]</b>\n\n"
                            + "He then recited:\n\n"
                            + "وَمَا مُحَمَّدٌ إِلَّا رَسُولٌۭ قَدْ خَلَتْ مِن قَبْلِهِ ٱلرُّسُلُ ۚ أَفَإِيْن مَّاتَ أَوْ قُتِلَ ٱنقَلَبْتُمْ عَلَىٰٓ أَعْقَـٰبِكُمْ.\n\n"
                            + "<b>Translation:</b>\n"
                            + "Muhammad is not but a messenger. [Other] messengers have passed on before him. So if he was to die or be killed, would you turn back on your heels?\n"
                            + "<b>[Surah Ali 'Imran 3:144]</b>"
            ));
        } else {
            // General Fallback topics based on ProphetStoryItem
            ProphetStoryItem story = null;
            for (ProphetStoryItem p : getProphetsList()) {
                if (p.getId() == prophetId) {
                    story = p;
                    break;
                }
            }
            if (story != null) {
                list.add(new ProphetOverviewTopicItem(
                        1,
                        "নবুয়ত ও জীবন পরিচিতি",
                        "Biography & Mission",
                        story.getDetailedStory(true),
                        story.getDetailedStory(false)
                ));
                if (story.getQuranicDuaArabic() != null && !story.getQuranicDuaArabic().isEmpty()) {
                    list.add(new ProphetOverviewTopicItem(
                            2,
                            "বিশেষ কুরআনিক দোয়া",
                            "Quranic Dua",
                            story.getQuranicDuaArabic() + "\n\n" + story.getQuranicDuaTransliteration() + "\n\n" + story.getQuranicDuaMeaning(),
                            story.getQuranicDuaArabic() + "\n\n" + story.getQuranicDuaTransliteration() + "\n\n" + story.getQuranicDuaMeaning()
                    ));
                }
                list.add(new ProphetOverviewTopicItem(
                        3,
                        "শিক্ষা ও উপদেশ",
                        "Key Lessons & Morals",
                        story.getKeyLessons(true),
                        story.getKeyLessons(false)
                ));
            }
        }

        return list;
    }
}

