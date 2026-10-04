package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjArafatContentRepository {

    public static List<HajjHistoryCardItem> getArafatCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. আরাফাতের দিনের বিবরণ
        list.add(new HajjHistoryCardItem(
            1,
            "আরাফাতের দিনের বিবরণ",
            "Description of the Day of Arafah",
            "আরাফাতের দিন বা আরাফার দিন (আরবি: يوم عرفة) হলো হজের সর্বপ্রধান দিন...",
            "The Day of Arafah (Arabic: Yawm 'Arafah) is the pinnacle and most sacred day of Hajj...",
            "আরাফাতের দিন বা আরাফার দিন (আরবি: يوم عرفة) হলো হজের সর্বপ্রধান ও সবচেয়ে মর্যাদাপূর্ণ দিন, যা জিলহজ মাসের ৯ তারিখে অনুষ্ঠিত হয়। রাসূলুল্লাহ (ﷺ) ইরশাদ করেছেন: 'আল-হাজ্জু আরাফাহ'—অর্থাৎ আরাফাতই হলো মূল হজ। এই দিনে আরাফাতের ময়দানে উপস্থিত হওয়া হজের প্রধানতম ফরজ রুকন, যা ব্যতীত কারো হজ সম্পন্ন হবে না।",
            "The Day of Arafah (Arabic: Yawm 'Arafah) is the most virtuous and pivotal day of Hajj, taking place on the 9th of Dhul Hijjah. The Messenger of Allah (ﷺ) declared: 'Hajj is Arafah'—meaning standing on the plains of Arafah is the quintessential pillar of Hajj without which the pilgrimage is invalid."
        ));

        // 2. আরাফার ময়দানে হাজীদের করণীয় কাজগুলো
        list.add(new HajjHistoryCardItem(
            2,
            "আরাফার ময়দানে হাজীদের করণীয় কাজগুলো",
            "Duties of Pilgrims on the Plains of Arafah",
            "(১) আরাফায় পৌঁছে মসজিদে 'নামিরা'র কাছে অবস্থান করা বা তাঁবুতে থাকা...",
            "(1) Arriving at Arafah, staying near Masjid Nimrah or in tents...",
            "(১) আরাফায় পৌঁছে মসজিদে 'নামিরা'র কাছে অথবা ময়দানের যেকোনো সুবিধাজনক স্থানে তাঁবুতে অবস্থান করা।\n(২) সূর্য পশ্চিমাকাশে ঢলার পর জোহর ও আসরের নামাজ এক আজান ও দুই ইকামতে একত্রে জামাতের সাথে কসর করে আদায় করা।\n(৩) জোহরের পর থেকে সূর্যাস্ত পর্যন্ত দাঁড়িয়ে বা বসে ক্বিবলামুখী হয়ে খুশু-খুজুর সাথে অনর্গল তওবা, ইস্তিগফার, তালবিয়াহ ও মোনাজাত করা।\n(৪) সূর্যাস্তের পূর্বে কোনো অবস্থাতেই আরাফাতের সীমানা ত্যাগ না করা।",
            "(1) Upon arriving in Arafah, settling near Masjid Nimrah or in assigned tents within the boundaries of Arafah.\n(2) Combining and shortening Dhuhr and Asr prayers at Dhuhr time with one Adhan and two Iqamahs.\n(3) Dedicated supplication, Dhikr, Talbiyah, and sincere repentance facing the Qiblah from afternoon until sunset.\n(4) Not leaving the boundary of Arafah before complete sunset."
        ));

        // 3. আরাফার দিনে হাজীদের জন্য আল্লাহ কী কী মর্যাদা ও ফযীলত রেখেছেন
        list.add(new HajjHistoryCardItem(
            3,
            "আরাফার দিনে হাজীদের জন্য আল্লাহ কী কী মর্যাদা ও ফযীলত রেখেছেন",
            "Virtues and Status Bestowed on Pilgrims on Arafah",
            "(১) এ তারিখে দিনের বেলায়ই আল্লাহ তা'আলা প্রচুর বান্দাকে জাহান্নাম থেকে মুক্তি দেন...",
            "(1) On this day, Allah grants freedom from Hellfire to countless servants...",
            "(১) এ তারিখে দিনের বেলায়ই আল্লাহ তা'আলা সর্বাধিক সংখ্যক বান্দাকে জাহান্নাম থেকে মুক্তি দেন যা বছরের অন্য কোনো দিনে দেন না।\n(২) আল্লাহ তাআলা নিকটবর্তী আসমানে নেমে আসেন এবং আরাফায় অবস্থানকারী বান্দাদের নিয়ে ফেরেশতাদের কাছে গর্ব করে বলেন: 'এরা আমার কাছে কী চায়?'\n(৩) এটি দোয়া কবুলের শ্রেষ্ঠতম দিন। রাসূলুল্লাহ (ﷺ) বলেছেন: 'সর্বোত্তম দোয়া হলো আরাফার দিনের দোয়া।'\n(৪) শয়তান এই দিনে আল্লাহর অসীম রহমত ও ক্ষমা দেখে চরমভাবে অপদস্থ ও লাঞ্ছিত হয়।",
            "(1) On this day Allah frees more servants from the Fire of Hell than on any other day of the year.\n(2) Allah descends to the lowest heaven and boasts of the pilgrims before the angels, saying: 'What do My servants seek?'\n(3) It is the greatest day for accepted supplication. The Prophet (ﷺ) said: 'The best supplication is the supplication on the Day of Arafah.'\n(4) Satan is never seen more humiliated, disgraced, and infuriated than on the Day of Arafah due to witnessing Allah's immense mercy."
        ));

        // 4. আরাফায় অবস্থান ও দোয়ার ইসলামী আদব
        list.add(new HajjHistoryCardItem(
            4,
            "আরাফায় অবস্থান ও দোয়ার ইসলামী আদব",
            "Islamic Manners of Supplication & Staying at Arafah",
            "আদবগুলো নিম্নরূপঃ (১) গোসল করে নেয়া, (২) পবিত্র অবস্থায় থাকা...",
            "The manners are: (1) Performing Ghusl, (2) Remaining in ritual purity...",
            "আদবগুলো নিম্নরূপঃ\n(১) সূর্য ঢলার পূর্বে গোসল করে নেয়া মুস্তাহাব।\n(২) অজুর সাথে পবিত্র অবস্থায় থাকা ও ক্বিবলামুখী হয়ে অবস্থান করা।\n(৩) বিনয় ও একাগ্রতার সাথে দুই হাত তুলে দোয়া করা।\n(৪) রাসুলুল্লাহ (ﷺ)-এর ওপর বেশি বেশি দরুদ পাঠ করা এবং 'লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু...' দোয়াটি পাঠ করা।\n(৫) পার্থিব অপ্রয়োজনীয় আলাপচারিতা ও মোবাইল ফোনে সময় নষ্ট করা সম্পূর্ণ পরিহার করা।",
            "The Islamic manners include:\n(1) Taking a bath (Ghusl) before midday (Sunnah/Mustahabb).\n(2) Maintaining ritual purity (Wudu) and facing the Qiblah.\n(3) Raising hands humbly in tearful supplication.\n(4) Abundant recitation of Salawat upon the Prophet (ﷺ) and the best Dua: 'La ilaha illallahu wahdahu la shareeka lah...'\n(5) Strictly avoiding idle worldly talk, arguments, and unnecessary phone distractions."
        ));

        // 5. আরাফায় অবস্থানের সময় কখন শুরু হয় এবং এর শেষ সময়
        list.add(new HajjHistoryCardItem(
            5,
            "আরাফায় অবস্থানের সময় কখন শুরু হয় এবং এর শেষ সময়",
            "Start and End Time of Standing at Arafah",
            "দুপুরে সূর্য পশ্চিমে ঢলার পর থেকে আরাফার প্রধান সময় শুরু হয়...",
            "The primary time for standing begins after the sun passes the meridian...",
            "দুপুরে সূর্য পশ্চিমে ঢলার পর (জোহরের ওয়াক্ত শুরু হওয়ার পর) থেকে আরাফায় অবস্থানের প্রধান ও মূল সময় শুরু হয়। এর শেষ সময় হলো ১০ই জিলহজ সুবহে সাদিক (ফজরের ওয়াক্ত) উদিত হওয়া পর্যন্ত। এই সময়ের মধ্যে যে কোনো ব্যক্তি আরাফাতের ময়দানে ক্ষণিকের জন্যও উপস্থিত হলে তার উকুফে আরাফার ফরজ রুকন আদায় হয়ে যাবে। তবে সূর্যাস্ত পর্যন্ত অবস্থান করা ওয়াজিব।",
            "The primary time for Wuquf (standing) begins after the sun passes the meridian (Zawal/midday) on the 9th of Dhul Hijjah and extends until the dawn (Subh Sadiq) of the 10th of Dhul Hijjah. Whoever spends even a brief moment within Arafah during this window fulfills the essential pillar of Hajj. However, remaining until complete sunset is obligatory (Wajib)."
        ));

        // 6. কমপক্ষে কী পরিমাণ সময় আরাফাতে থাকতে হয়
        list.add(new HajjHistoryCardItem(
            6,
            "কমপক্ষে কী পরিমাণ সময় আরাফাতে থাকতে হয়",
            "Minimum Duration Required to Stay at Arafah",
            "দিনে অবস্থানকারীর সূর্যাস্ত পর্যন্ত অবস্থান করা।",
            "Staying until sunset for whoever arrives during the daytime.",
            "দিনে অবস্থানকারীর জন্য সূর্যাস্ত পর্যন্ত অবস্থান করা ওয়াজিব। তবে হজের ফরজ রুকন আদায়ের জন্য ৯ই জিলহজ দুপুর থেকে ১০ই জিলহজ সুবহে সাদিকের মধ্যবর্তী যেকোনো মুহূর্তে সামান্য সময়ের উপস্থিতিই যথেষ্ট। কেউ যদি দিনে পৌঁছে সূর্যাস্তের পূর্বে আরাফাত থেকে চলে যায় এবং সূর্যাস্তের পর আর ফিরে না আসে, তবে তার ওপর দম (কুরবানি) ওয়াজিব হবে।",
            "For whoever arrives during daylight hours, remaining until sunset is obligatory (Wajib). However, for the essential pillar (Fard) to be valid, being present in Arafah for even a fleeting moment between the afternoon of the 9th and dawn of the 10th suffices. If one departs before sunset without returning, a penalty sacrifice (Dam) becomes mandatory."
        ));

        // 7. আরাফার দিনে কিছু প্রচলিত ভুল
        list.add(new HajjHistoryCardItem(
            7,
            "আরাফার দিনে কিছু প্রচলিত ভুল",
            "Common Mistakes Made on the Day of Arafah",
            "অনেক হাজী আরাফার দিন কিছু ভুল করে থাকেন যেমন সীমানার বাইরে থাকা...",
            "Many pilgrims commit certain mistakes such as staying outside boundaries...",
            "অনেক হাজী আরাফার দিন কিছু ভুল করে থাকেন। যেমন:\n(১) আরাফাতের মূল সীমানার বাইরে যেমন উরানাহ উপত্যকায় অবস্থান করা—ফলে তাদের হজ আদায় হয় না।\n(২) জাবালে রহমতে (রহমতের পাহাড়ে) ওঠার জন্য ঝুঁকি নেওয়া ও কষ্ট করা—অথচ পুরো আরাফাত ময়দানই বরকতময়।\n(৩) জাবালে রহমত বা কোনো পাথরের দিকে মুখ করে দোয়া করা—অথচ ক্বিবলামুখী (কাবার দিকে) মুখ করে দোয়া করা সুন্নাত।\n(৪) সূর্যাস্তের পূর্বেই তাড়াাহুড়ো করে আরাফাতের সীমানা ত্যাগ করা।\n(৫) মূল্যবান সময় ঘুমিয়ে বা অনর্থক কথাবার্তা ও সেলফি তুলে নষ্ট করা।",
            "Common mistakes include:\n(1) Staying outside the designated boundaries of Arafah (such as the valley of Uranah), which invalidates Hajj.\n(2) Struggling and exhausting oneself to climb Mount Rahmah, whereas the entire plain of Arafah is a place of standing.\n(3) Facing Mount Rahmah or stones during Dua instead of facing the Qiblah (Kaaba).\n(4) Rushing to exit the boundaries of Arafah before complete sunset.\n(5) Wasting precious moments in sleeping, idle socializing, or photography."
        ));

        return list;
    }
}
