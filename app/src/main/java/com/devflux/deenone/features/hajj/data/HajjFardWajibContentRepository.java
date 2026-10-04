package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjFardWajibContentRepository {

    public static List<HajjHistoryCardItem> getFardWajibCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. ইহরাম তথা হজের নিয়ত করা (ফরজ)
        list.add(new HajjHistoryCardItem(
            1,
            "ইহরাম তথা হজের নিয়ত করা (ফরজ)",
            "Assuming Ihram and Intention for Hajj (Fard)",
            "যে ব্যক্তি হজের নিয়ত করবে না তার হজ হবে না...",
            "Whoever does not make intention for Hajj, his Hajj is invalid...",
            "<b>বিধান ও গুরুত্ব:</b><br>"
            + "হজের প্রথম রুকন বা ফরজ হলো ইহরাম ও হজের সুনির্দিষ্ট নিয়ত করা। নিয়ত হলো অন্তরের দৃঢ় সংকল্প এবং মুখে তালবিয়াহ পাঠ করা আবশ্যক।<br><br>"
            + "<b>হাদিস শরিফ:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) ইরশাদ করেছেন:<br>"
            + "<font color='#10B981'><b>إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى</b></font><br>"
            + "<i>\"নিশ্চয়ই সমস্ত আমলের ফলাফল নিয়তের ওপর নির্ভরশীল, এবং প্রত্যেক ব্যক্তি যা নিয়ত করবে তাই পাবে।\"</i> (সহীহ বুখারী: ১)<br><br>"
            + "<b>আবশ্যকীয় কার্যাবলি:</b><br>"
            + "যে ব্যক্তি হজের নিয়ত করবে না তার হজ হবে না। নিয়তের সাথে সাথে পুরুষদের সেলাইবিহীন সাদা চাদর পরিধান করতে হবে এবং ইহরামের যাবতীয় নিষেধাজ্ঞা (সুগন্ধি বর্জন, চুল-নখ না কাটা ইত্যাদি) মেনে চলা ফরজ।",
            "<b>Obligation & Significance:</b><br>"
            + "Assuming Ihram with sincere intention is the primary pillar (Fard) of Hajj.<br><br>"
            + "<b>Hadith:</b><br>"
            + "The Prophet (ﷺ) said: 'Actions are judged by motives...' (Sahih al-Bukhari: 1)<br><br>"
            + "<b>Rules:</b><br>"
            + "Without valid intention and assuming the state of Ihram, the pilgrimage cannot commence."
        ));

        // 2. আরাফায় অবস্থান (ফরজ)
        list.add(new HajjHistoryCardItem(
            2,
            "আরাফায় অবস্থান (ফরজ)",
            "Standing at Arafah / Wuquf (Fard)",
            "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেন-",
            "The Messenger of Allah (ﷺ) said: 'Hajj is Arafah'...",
            "<b>হজের সর্বশ্রেষ্ঠ রুকন:</b><br>"
            + "আরাফাতের ময়দানে অবস্থান করাই হলো হজের মূল ভিত্তি। কোনো ব্যক্তি ৯ই জিলহজ আরাফাতে উপস্থিত না হলে তার হজ কোনো অবস্থাতেই শুদ্ধ হবে না।<br><br>"
            + "<b>হাদিস শরিফ:</b><br>"
            + "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেন:<br>"
            + "<font color='#10B981'><b>الْحَجُّ عَرَفَةُ، فَمَنْ جَاءَ قَبْلَ صَلَاةِ الْفَجْرِ مِنْ لَيْلَةِ جَمْعٍ فَقَدْ تَمَّ حَجُّهُ</b></font><br>"
            + "<i>\"আরাফাতই হলো হজ। যে ব্যক্তি মুজদালিফার রাতে ফজরের নামাজের পূর্বে আরাফাতে এসে উপস্থিত হলো, তার হজ পূর্ণ হলো।\"</i> (জামে তিরমিজি: ৮৮৯, সুনানে আবু দাউদ: ১৯৪৯)<br><br>"
            + "<b>সময়সীমা:</b><br>"
            + "৯ই জিলহজ দ্বিপ্রহর (জোহর) থেকে শুরু করে ১০ই জিলহজ সুবহে সাদিকের পূর্ব পর্যন্ত যেকোনো সময় আরাফাতের সীমানার ভেতরে অবস্থান করা ফরজ।",
            "<b>The Core Pillar:</b><br>"
            + "Wuquf (standing) in the plains of Arafah is the pinnacle foundation of Hajj.<br><br>"
            + "<b>Hadith:</b><br>"
            + "The Messenger of Allah (ﷺ) declared: 'Hajj is Arafah.' (Jami at-Tirmidhi: 889)<br><br>"
            + "<b>Timing:</b><br>"
            + "From midday of 9th Dhul Hijjah until dawn of 10th Dhul Hijjah."
        ));

        // 3. তাওয়াফে ইফাযা বা তাওয়াফে যিয়ারা (ফরজ)
        list.add(new HajjHistoryCardItem(
            3,
            "তাওয়াফে ইফাযা বা তাওয়াফে যিয়ারা (ফরজ)",
            "Tawaf al-Ifadah / Tawaf al-Ziyarah (Fard)",
            "আল্লাহ তা'আলা বলেন-",
            "Allah the Exalted says in the Holy Quran...",
            "<b>কুরআনের নির্দেশ:</b><br>"
            + "আল্লাহ তা'আলা বলেন:<br>"
            + "<font color='#10B981'><b>ثُمَّ لْيَقْضُوا تَفَثَهُمْ وَلْيُوفُوا نُذُورَهُمْ وَلْيَطَّوَّفُوا بِالْبَيْتِ الْعَتِيقِ</b></font><br>"
            + "<i>\"অতঃপর তারা যেন তাদের অপরিচ্ছন্নতা দূর করে, তাদের মানত পূর্ণ করে এবং প্রাচীন ঘরের (পবিত্র কাবার) তাওয়াফ করে।\"</i> (সূরা আল-হাজ্জ: ২৯)<br><br>"
            + "<b>বিধান ও গুরুত্ব:</b><br>"
            + "১০ই জিলহজ কঙ্কর নিক্ষেপ ও মাথা মুণ্ডনের পর থেকে কাবা শরীফের সাত চক্কর তাওয়াফ করা হজের অন্যতম প্রধান ফরজ। এই তাওয়াফ সম্পন্ন না করা পর্যন্ত স্বামী-স্ত্রীর সম্পর্ক হালাল হয় না।",
            "<b>Quranic Command:</b><br>"
            + "Allah Almighty says: 'Then let them complete their prescribed duties and perform Tawaf around the Ancient House.' (Surah Al-Hajj: 29)<br><br>"
            + "<b>Significance:</b><br>"
            + "Performing seven circuits around the Kaaba after stoning on the 10th of Dhul Hijjah is an obligatory pillar."
        ));

        // 4. সাফা ও মারওয়ায় সাঈ করা (ফরজ)
        list.add(new HajjHistoryCardItem(
            4,
            "সাফা ও মারওয়ায় সাঈ করা (ফরজ)",
            "Sa'i Between Safa and Marwah (Fard)",
            "অধিকাংশ সাহাবী, তাবিঈ ও ইমামের মতে এটা ফ...",
            "According to the majority of Sahabah and scholars, Sa'i is an essential pillar...",
            "<b>শরীয়তের বিধান:</b><br>"
            + "অধিকাংশ সাহাবী, তাবিঈ ও ইমামদের (ইমাম শাফেয়ী, ইমাম মালেক ও ইমাম আহমদ রহ.) মতে সাফা ও মারওয়ার মাঝে সাত চক্কর সাঈ করা হজের অবিচ্ছেদ্য ফরজ/রুকন (হানাফী মাযহাবে অন্যতম প্রধান ওয়াজিব)।<br><br>"
            + "<b>হাদিস শরিফ:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) ইরশাদ করেছেন:<br>"
            + "<font color='#10B981'><b>اسْعَوْا، فَإِنَّ اللَّهَ كَتَبَ عَلَيْكُمُ السَّعْيَ</b></font><br>"
            + "<i>\"তোমরা সাঈ করো, কেননা নিশ্চয়ই আল্লাহ তোমাদের ওপর সাঈ ফরজ করেছেন।\"</i> (মুসনাদে আহমদ: ২৭৪২৪, মুসান্নাফে আব্দুর রাযযাক: ৮৮৭২)<br><br>"
            + "<b>পদ্ধতি:</b><br>"
            + "সাফা পাহাড় থেকে শুরু করে মারওয়ায় গিয়ে ১ম চক্কর এবং মারওয়া থেকে সাফায় ২য় চক্কর—এভাবে ৭ম চক্করে মারওয়ায় শেষ করতে হয়।",
            "<b>Legal Ruling:</b><br>"
            + "The majority of jurists consider Sa'i between Safa and Marwah a foundational pillar (Fard/Rukn).<br><br>"
            + "<b>Hadith:</b><br>"
            + "The Prophet (ﷺ) said: 'Perform Sa'i, for Allah has ordained Sa'i upon you.' (Musnad Ahmad: 27424)<br><br>"
            + "<b>Manner:</b><br>"
            + "Begins at Safa and completes seven laps ending at Marwah."
        ));

        // 5. মীকাত থেকে ইহরাম (ওয়াজিব)
        list.add(new HajjHistoryCardItem(
            5,
            "মীকাত থেকে ইহরাম (ওয়াজিব)",
            "Assuming Ihram from the Miqat (Wajib)",
            "মীকাত থেকে ইহরাম বাঁধা। অর্থাৎ মীকাত অতিক্রম...",
            "Assuming Ihram at or before crossing the prescribed geographical Miqat...",
            "<b>ওয়াজিব বিধান:</b><br>"
            + "মীকাত থেকে ইহরাম বাঁধা। অর্থাৎ মীকাত অতিক্রম করার পূর্বেই হজের ইহরাম ও নিয়ত সম্পন্ন করা হজের অন্যতম প্রধান ওয়াজিব বিধান।<br><br>"
            + "<b>হাদিস শরিফ:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) মক্কার বহিরাগতদের জন্য ৫টি মীকাত নির্ধারণ করে ইরশাদ করেছেন:<br>"
            + "<font color='#10B981'><b>هُنَّ لَهُنَّ وَلِمَنْ أَتَى عَلَيْهِنَّ مِنْ غَيْرِهِنَّ مِمَّنْ أَرَادَ الحَجَّ وَالعُمْرَةَ</b></font><br>"
            + "<i>\"এগুলো সংশ্লিষ্ট অঞ্চলের মানুষদের জন্য এবং যারা এই পথ দিয়ে হজ ও ওমরাহর উদ্দেশ্যে আসবে তাদের সবার জন্য নির্ধারিত মীকাত।\"</i> (সহীহ বুখারী: ১৫২৪)<br><br>"
            + "<b>ভুলের কাফফারা:</b><br>"
            + "ইহরাম ছাড়া মীকাত অতিক্রম করে ফেললে পুনরায় মীকাতে ফিরে এসে ইহরাম বাঁধতে হবে, অন্যথায় কাফফারা হিসেবে একটি দম (কোরবানি) দিতে হবে।",
            "<b>The Obligation:</b><br>"
            + "Entering the state of Ihram before crossing the boundary of Miqat is strictly Wajib.<br><br>"
            + "<b>Hadith:</b><br>"
            + "The Prophet (ﷺ) set fixed geographical stations for pilgrims from every horizon (Sahih al-Bukhari: 1524).<br><br>"
            + "<b>Expiation:</b><br>"
            + "Crossing without Ihram requires returning to Miqat or sacrificing a Dam."
        ));

        // 6. সূর্যাস্ত পর্যন্ত আরাফায় অবস্থান করা (ওয়াজিব)
        list.add(new HajjHistoryCardItem(
            6,
            "সূর্যাস্ত পর্যন্ত আরাফায় অবস্থান করা (ওয়াজিব)",
            "Remaining at Arafah Until Sunset (Wajib)",
            "যে ব্যক্তি আরাফার ময়দানে দিনে উকুফ করবে, ...",
            "Whoever stands in Arafah during daylight must remain until sunset...",
            "<b>ওয়াজিবের বিবরণ:</b><br>"
            + "যে ব্যক্তি আরাফার ময়দানে দিনে উকুফ করবে, তার জন্য সূর্য সম্পূর্ণরূপে অস্ত যাওয়া পর্যন্ত আরাফাতের সীমানায় অবস্থান বজায় রাখা ওয়াজিব।<br><br>"
            + "<b>সুন্নাত আমল:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) সূর্যাস্ত পর্যন্ত নিরবচ্ছিন্নভাবে দোয়া ও মোনাজাতে রত ছিলেন এবং সূর্যাস্তের পরই মুজদালিফার উদ্দেশ্যে রওয়ানা হয়েছিলেন। (সহীহ মুসলিম: ১২১৮)<br><br>"
            + "<b>সতর্কতা:</b><br>"
            + "সূর্যাস্তের পূর্বে ইচ্ছাকৃতভাবে আরাফাতের সীমানা ত্যাগ করা মারাত্মক ভুল এবং এর জন্য কাফফারা (দম) ওয়াজিব হয়।",
            "<b>The Requirement:</b><br>"
            + "Pilgrims present in Arafah during the afternoon must remain until the disc of the sun fully sets.<br><br>"
            + "<b>Sunnah:</b><br>"
            + "The Prophet (ﷺ) remained standing in supplication until sunset before moving to Muzdalifah (Sahih Muslim: 1218)."
        ));

        // 7. মুযদালিফায় রাত যাপন (ওয়াজিব)
        list.add(new HajjHistoryCardItem(
            7,
            "মুযদালিফায় রাত যাপন (ওয়াজিব)",
            "Overnight Stay at Muzdalifah (Wajib)",
            "কেননা রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম ...",
            "Because the Messenger of Allah (ﷺ) stayed overnight at Muzdalifah...",
            "<b>ওয়াজিবের প্রেক্ষাপট:</b><br>"
            + "কেননা রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম ৯ই জিলহজ সূর্যাস্তের পর আরাফাত থেকে মুজদালিফায় এসে মাগরিব ও এশা একত্রে আদায় করে রাত যাপন করেছিলেন। (সহীহ বুখারী: ১৬৮২)<br><br>"
            + "<b>কুরআনের নির্দেশ:</b><br>"
            + "<font color='#10B981'><b>فَإِذَا أَفَضْتُم مِّنْ عَرَفَاتٍ فَاذْكُرُوا اللَّهَ عِندَ الْمَشْعَرِ الْحَرَامِ</b></font><br>"
            + "<i>\"অতঃপর যখন তোমরা আরাফাত থেকে প্রত্যাবর্তন করবে, তখন মাশআরে হারামের নিকট আল্লাহকে স্মরণ করো।\"</i> (সূরা আল-বাকারা: ১৯৮)<br><br>"
            + "<b>আমল:</b><br>"
            + "মুজদালিফায় রাত যাপন করা এবং সুবহে সাদিকের পর মাশআরুল হারামের নিকট কিবলামুখী হয়ে মোনাজাত করা এবং জামারার জন্য কঙ্কর সংগ্রহ করা ওয়াজিব/সুন্নাত।",
            "<b>Context:</b><br>"
            + "The Prophet (ﷺ) stayed overnight at Muzdalifah, combining Maghrib and Isha prayers (Sahih al-Bukhari: 1682).<br><br>"
            + "<b>Quran Reference:</b><br>"
            + "Surah Al-Baqarah: 198 ('Remember Allah at Al-Mash'ar Al-Haram.')"
        ));

        // 8. তাশরীকের রাতগুলো মিনায় যাপন (ওয়াজিব)
        list.add(new HajjHistoryCardItem(
            8,
            "তাশরীকের রাতগুলো মিনায় যাপন (ওয়াজিব)",
            "Staying Overnight in Mina During Tashreeq Nights (Wajib)",
            "তারিখ দিবাগত রাত ও ১১ তারিখ দিবাগত রাত মি...",
            "Staying in Mina on the nights following the 10th, 11th, and 12th Dhul Hijjah...",
            "<b>ওয়াজিবের বিবরণ:</b><br>"
            + "১০ই জিলহজ দিবাগত রাত ও ১১ তারিখ দিবাগত রাত মিনায় যাপন করা অধিকাংশ ফকীহের মতে ওয়াজিব (এবং যারা ১৩ই জিলহজ পাথর নিক্ষেপ করবেন তাদের জন্য ১২ই জিলহজ দিবাগত রাতও)।<br><br>"
            + "<b>হাদিস শরিফ:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) তাশরীকের দিনগুলোতে মিনাতেই অবস্থান করেছিলেন এবং সেখানে রাত কাটিয়েছিলেন। (সহীহ বুখারী: ১৭৪৫, সহীহ মুসলিম: ১৩১৩)<br><br>"
            + "<b>ব্যতিক্রম:</b><br>"
            + "কেবলমাত্র জমজমের পানি সরবরাহকারী এবং পশুপালকদের মতো বিশেষ দায়িত্বপ্রাপ্ত ব্যক্তিদের মিনায় রাত যাপনে ছাড় দেওয়া হয়েছিল। সাধারণ হাজীদের জন্য বিনা কারণে মিনার বাইরে রাত কাটানো নিষিদ্ধ।",
            "<b>The Obligation:</b><br>"
            + "Spending the nights of Tashreeq (11th, 12th, and 13th Dhul Hijjah) in Mina is Wajib.<br><br>"
            + "<b>Hadith:</b><br>"
            + "The Prophet (ﷺ) resided and spent these nights in Mina (Sahih al-Bukhari: 1745)."
        ));

        // 9. জামরায় কঙ্কর নিক্ষেপ করা (ওয়াজিব)
        list.add(new HajjHistoryCardItem(
            9,
            "জামরায় কঙ্কর নিক্ষেপ করা (ওয়াজিব)",
            "Stoning the Jamarat Pillars (Wajib)",
            "যিলহজ জামরাতুল আকাবায় (বড় জমরায়) কঙ্ক...",
            "Throwing pebbles at Jamarat al-Aqaba on 10th Dhul Hijjah, and all three on Tashreeq...",
            "<b>পাথর নিক্ষেপের শিডিউল:</b><br>"
            + "১০ই যিলহজ জামরাতুল আকাবায় (বড় জমরায়) ৭টি কঙ্কর নিক্ষেপ করা এবং ১১ ও ১২ই জিলহজ প্রতিদিন দুপুর (জাওয়াল)-এর পর থেকে ক্রমান্বয়ে জামরায়ে সুগরা (ছোট), উসতা (মধ্যম) এবং আকাবায় (বড়) ৭টি করে মোট ২১টি কঙ্কর নিক্ষেপ করা ওয়াজিব।<br><br>"
            + "<b>তাকবীর ও দোয়া:</b><br>"
            + "প্রতিটি কঙ্কর নিক্ষেপকালে 'আল্লাহু আকবার' বলা সুন্নাত। ১ম ও ২য় জামারায় পাথর নিক্ষেপের পর একপাশে সরে এসে কিবলামুখী হয়ে দীর্ঘ মোনাজাত করা সুন্নাত।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ বুখারী: ১৭৫১, সহীহ মুসলিম: ১২৯৯",
            "<b>Schedule:</b><br>"
            + "Stoning Jamarat al-Aqaba with 7 pebbles on the 10th of Dhul Hijjah, and stoning all 3 pillars (21 pebbles daily) on the 11th and 12th after Dhuhr.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih al-Bukhari: 1751, Sahih Muslim: 1299"
        ));

        // 10. মাথা মুন্ডান বা চুল ছোট করা (ওয়াজিব)
        list.add(new HajjHistoryCardItem(
            10,
            "মাথা মুন্ডান বা চুল ছোট করা (ওয়াজিব)",
            "Shaving (Halq) or Trimming Hair (Taqseer) (Wajib)",
            "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম মাথা মু...",
            "The Messenger of Allah (ﷺ) supplicated thrice for those who shave their heads...",
            "<b>ওয়াজিবের বিধান:</b><br>"
            + "হজের অন্যতম ওয়াজিব হলো ১০ই জিলহজ কুরবানির পর মাথা মুণ্ডন (হলক) বা চুল ছোট (কসর) করা। এর মাধ্যমে হাজী সাহেব ইহরামের প্রথম তাহাল্লুল (স্ত্রীসঙ্গ ব্যতীত অন্যান্য বিধিনিষেধ থেকে মুক্ত) অর্জন করেন।<br><br>"
            + "<b>হাদিস শরিফ:</b><br>"
            + "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম মাথা মুণ্ডনকারীদের জন্য ৩ বার এবং চুল ছোটকারীদের জন্য ১ বার দোয়া করে বলেন:<br>"
            + "<font color='#10B981'><b>اللَّهُمَّ اغْفِرْ لِلْمُحَلِّقِينَ، قَالُوا: وَلِلْمُقَصِّرِينَ؟ قَالَ: وَلِلْمُقَصِّرِينَ</b></font><br>"
            + "<i>\"হে আল্লাহ! মাথা মুণ্ডনকারীদের ক্ষমা করুন। সাহাবীগণ বললেন: হে আল্লাহর রাসুল, চুল ছোটকারীদের জন্য? তিনি ৪র্থ বারে বললেন: এবং চুল ছোটকারীদেরও।\"</i> (সহীহ বুখারী: ১৭২৭, সহীহ মুসলিম: ১৩০১)<br><br>"
            + "<b>মহিলাদের নিয়ম:</b><br>"
            + "মহিলাদের মাথা মুণ্ডন করা নিষিদ্ধ; তারা তাদের চুলের গোছা থেকে আঙুলের এক কর (প্রায় ১ ইঞ্চি) পরিমাণ কাটবেন।",
            "<b>The Obligation:</b><br>"
            + "Shaving the head or trimming hair equally is Wajib to release from the primary state of Ihram.<br><br>"
            + "<b>Hadith:</b><br>"
            + "The Prophet (ﷺ) prayed thrice for those who shave and once for those who trim (Sahih al-Bukhari: 1727).<br><br>"
            + "<b>Rule for Women:</b><br>"
            + "Women only trim a fingertip length (approx 1 inch) from the ends of their hair."
        ));

        // 11. বিদায়ী তাওয়াফ (ওয়াজিব)
        list.add(new HajjHistoryCardItem(
            11,
            "বিদায়ী তাওয়াফ (ওয়াজিব)",
            "Farewell Tawaf / Tawaf al-Wada (Wajib)",
            "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বিদায়ী ...",
            "The Messenger of Allah (ﷺ) instructed to make Tawaf the final rite in Makkah...",
            "<b>ওয়াজিবের বিধান:</b><br>"
            + "মক্কার বাইরের সমস্ত হাজীদের জন্য মক্কা ত্যাগ করার পূর্বে কা'বা শরীফে শেষ তাওয়াফ হিসেবে বিদায়ী তাওয়াফ আদায় করা ওয়াজিব।<br><br>"
            + "<b>হাদিস শরিফ:</b><br>"
            + "রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বিদায়ী তাওয়াফের নির্দেশ দিয়ে ইরশাদ করেছেন:<br>"
            + "<font color='#10B981'><b>لاَ يَنْفِرَنَّ أَحَدٌ حَتَّى يَكُونَ آخِرُ عَهْدِهِ بِالْبَيْتِ</b></font><br>"
            + "<i>\"বাইতুল্লাহর সাথে শেষ সাক্ষাৎ বা তাওয়াফ না করে তোমাদের কেউ যেন মক্কা ত্যাগ না করে।\"</i> (সহীহ মুসলিম: ১৩২৮, সহীহ বুখারী: ১৭৫৫)<br><br>"
            + "<b>ছাড় ও মাসআলা:</b><br>"
            + "ঋতুবতী নারীদের জন্য বিদায়ী তাওয়াফ মাফ করা হয়েছে; তারা তাওয়াফ ছাড়াই মক্কা ত্যাগ করতে পারবেন।",
            "<b>The Obligation:</b><br>"
            + "Tawaf al-Wada is Wajib for all pilgrims residing outside the Haram boundary prior to departure.<br><br>"
            + "<b>Hadith:</b><br>"
            + "The Prophet (ﷺ) stated: 'Let none of you depart until his last act is with the House.' (Sahih Muslim: 1328)<br><br>"
            + "<b>Exemption:</b><br>"
            + "Menstruating women are excused from the Farewell Tawaf."
        ));

        return list;
    }
}
