package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for: গোসল (Ghusl - Full Ritual Bath).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahGhuslContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // Card 1: গোসলের দোয়া (Verbatim)
        list.add(new HajjHistoryCardItem(
            1,
            "গোসলের দোয়া",
            "Supplication for Ghusl",
            "গোসলের নিয়ত বলতে বোঝায় নিজেকে অপবিত্রতা (যেমন জানাবাত) থেকে পবিত্র করার উদ্দেশ্যে গোসল করা। এটি মূলত একটি মনের ইচ্ছা মুখে বলা বাধ্যতামূলক নয়। ইসলামে কোনো ইবাদতই নিয়ত ছাড়া গ্রহণযোগ্য হয় না, তাই গোসল করার সময় মনে ম...",
            "The intention for Ghusl means bathing with the purpose of purifying oneself from ritual impurity (such as Janabah). It is essentially an intention in the heart and not mandatory to utter verbally. In Islam, no worship is accepted without sincere intention...",
            "গোসলের নিয়ত বলতে বোঝায় নিজেকে অপবিত্রতা (যেমন জানাবাত) থেকে পবিত্র করার উদ্দেশ্যে গোসল করা। এটি মূলত একটি মনের ইচ্ছা মুখে বলা বাধ্যতামূলক নয়। ইসলামে কোনো ইবাদতই নিয়ত ছাড়া গ্রহণযোগ্য হয় না, তাই গোসল করার সময় মনে মনে পবিত্রতার উদ্দেশ্য স্থির করাই যথেষ্ট। আলাদা করে নির্দিষ্ট কোনো দোয়া পড়া ফরজ নয়, তবে বুঝার সুবিধার জন্য অনেকেই আরবিতে একটি বাক্য ব্যবহার করে।\n\n" +
            "أَنْوِي الغُسْلَ لِرَفْعِ الجَنَابَةِ\n\n" +
            "উচ্চারণ:\n" +
            "আনওয়ি আল-গুসলা লিরাফ‘ইল জানাবাহ\n\n" +
            "অর্থ:\n" +
            "আমি জানাবাত দূর করার জন্য গোসলের নিয়ত করছি",
            "The intention for Ghusl means bathing with the purpose of purifying oneself from ritual impurity (such as Janabah). It is essentially an intention in the heart and not mandatory to utter verbally. In Islam, no worship is accepted without intention; therefore, intending the purpose of purification in the heart while bathing is sufficient. Reciting a specific supplication is not obligatory, but for clarity and understanding, many use the following Arabic phrasing:\n\n" +
            "أَنْوِي الغُسْلَ لِرَفْعِ الجَنَابَةِ\n\n" +
            "Pronunciation:\n" +
            "Anwi al-ghusla li-raf'il janabah\n\n" +
            "Meaning:\n" +
            "\"I intend to perform Ghusl to remove Janabah (ritual impurity).\""
        ));

        // Card 2: গোসলের পূর্বে উযূ করা (Verbatim)
        list.add(new HajjHistoryCardItem(
            2,
            "গোসলের পূর্বে উযূ করা",
            "Performing Wudu Before Ghusl",
            "حَدَّثَنَا مُحَمَّدُ بْنُ يُوسُفَ، قَالَ حَدَّثَنَا سُفْيَانُ، عَنِ الأَعْمَشِ، عَنْ سَالِمِ بْنِ أَبِي الْجَعْدِ، عَنْ كُرَيْبٍ، عَنِ ابْنِ عَبَّاسٍ، عَنْ مَيْمُونَةَ، زَوْجِ النَّبِيِّ صلى الله عليه وسلم قَالَتْ تَوَضَّأَ رَسُولُ اللَّهِ صلى الله عليه وسلم ...",
            "Narrated Maimunah: Allah's Messenger performed ablution like that for prayer except his feet, and washed his private parts and whatever dirt was on him. Then he poured water over himself. Then he stepped aside from that place and washed his feet...",
            "حَدَّثَنَا مُحَمَّدُ بْنُ يُوسُفَ، قَالَ حَدَّثَنَا سُفْيَانُ، عَنِ الأَعْمَشِ، عَنْ سَالِمِ بْنِ أَبِي الْجَعْدِ، عَنْ كُرَيْبٍ، عَنِ ابْنِ عَبَّاسٍ، عَنْ مَيْمُونَةَ، زَوْجِ النَّبِيِّ صلى الله عليه وسلم قَالَتْ تَوَضَّأَ رَسُولُ اللَّهِ صلى الله عليه وسلم وُضُوءَهُ لِلصَّلاَةِ غَيْرَ رِجْلَيْهِ، وَغَسَلَ فَرْجَهُ، وَمَا أَصَابَهُ مِنَ الأَذَى، ثُمَّ أَفَاضَ عَلَيْهِ الْمَاءَ، ثُمَّ نَحَّى رِجْلَيْهِ فَغَسَلَهُمَا، هَذِهِ غُسْلُهُ مِنَ الْجَنَابَةِ.\n\n" +
            "মাইমূনাহ্ (রাযি.) হতে বর্ণিত। তিনি বলেন,\n\n" +
            "আল্লাহর রাসুল সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম সালাতের উযূর ন্যায় উযূ করলেন, পা দুটো ব্যতীত এবং তাঁর লজ্জাস্থান ও যে যে স্থানে নোংরা লেগেছে তা ধুয়ে নিলেন। অতঃপর নিজের উপর পানি ঢেলে দেন। অতঃপর সেখান হতে সরে গিয়ে পা দু’টো ধুয়ে নেন। এই ছিল তাঁর জানাবাতের গোসল।\n\n" +
            "[বুখারী -২৫৭, ২৫৯, ২৬০, ২৬৬, ২৭৪, ২৭৬, ২৮১; মুসলিম ৩/৯, হাঃ ৩১৭, আহমাদ ২৬৮৬১]",
            "حَدَّثَنَا مُحَمَّدُ بْنُ يُوسُفَ، قَالَ حَدَّثَنَا سُفْيَانُ، عَنِ الأَعْمَشِ، عَنْ سَالِمِ بْنِ أَبِي الْجَعْدِ، عَنْ كُرَيْبٍ، عَنِ ابْنِ عَبَّاسٍ، عَنْ مَيْمُونَةَ، زَوْجِ النَّبِيِّ صلى الله عليه وسلم قَالَتْ تَوَضَّأَ رَسُولُ اللَّهِ صلى الله عليه وسلم وُضُوءَهُ لِلصَّلاَةِ غَيْرَ رِجْلَيْهِ، وَغَسَلَ فَرْجَهُ، وَمَا أَصَابَهُ مِنَ الأَذَى، ثُمَّ أَفَاضَ عَلَيْهِ الْمَاءَ، ثُمَّ نَحَّى رِجْلَيْهِ فَغَسَلَهُمَا، هَذِهِ غُسْلُهُ مِنَ الْجَنَابَةِ.\n\n" +
            "Narrated Maimunah (may Allah be pleased with her):\n\n" +
            "\"Allah's Messenger (peace be upon him) performed ablution like that for prayer except his feet, and washed his private parts and whatever dirt was on him. Then he poured water over himself. Then he stepped aside from that place and washed his feet. This was his Ghusl from Janabah.\"\n\n" +
            "[Bukhari: 257, 259, 260, 266, 274, 276, 281; Muslim: 3/9, Hadith 317; Ahmad: 26861]"
        ));

        // Card 3: গোসল করার নিয়ম (Verbatim)
        list.add(new HajjHistoryCardItem(
            3,
            "গোসল করার নিয়ম",
            "Step-by-Step Method of Ghusl",
            "নাপাকীর গোসল করতে হলে গোসলের নিয়ত করে মুসলিম প্রথমে ৩ বার দুইহাত কব্জি পর্যন্ত ধুবে। অতঃপর বাম হাতের উপর পানি ঢেলে দেহের নাপাকী ধুয়ে ফেলবে। তারপর বাম হাতকে মাটি অথবা সাবান দ্বারা ধুয়ে নামাযের জন্য ওযু করার মত পূর্ণ ওযু করবে...",
            "When performing Ghusl for purification from ritual impurity, a Muslim begins with the intention and first washes both hands up to the wrists 3 times. Then pours water over the left hand to wash away the impurity from the body...",
            "নাপাকীর গোসল করতে হলে গোসলের নিয়ত করে মুসলিম প্রথমে ৩ বার দুইহাত কব্জি পর্যন্ত ধুবে। অতঃপর বাম হাতের উপর পানি ঢেলে দেহের নাপাকী ধুয়ে ফেলবে। তারপর বাম হাতকে মাটি অথবা সাবান দ্বারা ধুয়ে নামাযের জন্য ওযু করার মত পূর্ণ ওযু করবে। অবশ্য গোসলের জায়গা পরিষ্কার না হলে পা দুটি গোসল শেষে ধুয়ে নেবে। ওযুর পর ৩ বার মাথায় পানি ঢেলে ভাল করে চুলগুলো ধোবে, যাতে সমস্ত চুলের গোড়ায় গোড়ায় পানি পৌঁছে যায়। তারপর সারা দেহে ৩ বার পানি ঢেলে ভালরুপে ধুয়ে নেবে।\n\n" +
            "[বুখারী, মুসলিম, মিশকাত ৪৩৫-৪৩৬ নং]\n\n" +
            "মহিলাদের গোসলও পুরুষদের অনুরুপ। অবশ্য মহিলার মাথার চুলে বেণী বাঁধা (চুটি গাঁথা) থাকলে তা খোলা জরুরী নয়। তবে ৩ বার পানি নিয়ে চুলের গোড়া অবশ্যই ধুয়ে নিতে হবে।\n\n" +
            "[বুখারী, মিশকাত ৪৩৮]\n\n" +
            "নখে নখপালিশ বা কোন প্রকার পুরু পেন্ট্ থাকলে তা তুলে না ফেলা পর্যন্ত গোসল হবে না। পক্ষান্তরে মেহেদী বা আলতা লেগে থাকা অবস্থায় গোসল হয়ে যাবে। কপালে টিপ থাকলে ছাড়িয়ে ফেলে (কপাল) ধুতে হবে। নচেৎ গোসল হবে না।\n\n" +
            "বীর্যপাত বা সঙ্গম-জনিত নাপাকী ও মাসিকের গোসল, অথবা মাসিক ও ঈদ, অথবা বীর্যপাত বা সঙ্গম-জনিত নাপাকী ও জুমআ বা ঈদের গোসল নিয়ত হলে একবারই যথেষ্ট। পৃথক পৃথক গোসলের দরকার নেই।\n\n" +
            "[ফিকহুস সুন্নাহ্ উর্দু ৬০পৃ: দ্র:]\n\n" +
            "গোসলের পর নামাযের জন্য আর পৃথক ওযুর প্রয়োজন নেই। গোসলের পর ওযু ভাঙ্গার কোন কাজ না করলে গোসলের ওযুতেই নামায হয়ে যাবে।\n\n" +
            "[আবূদাঊদ, সুনান, তিরমিযী, সুনান, নাসাঈ, সুনান, ইবনে মাজাহ্, সুনান, মিশকাত ৪৪৫নং]\n\n" +
            "রোগ-জনিত কারণে যদি কারো লাগাতার বীর্য, মযী, স্রাব বা ইস্তিহাযার খুন ঝরে তবে তার জন্য গোসল ফরয নয়; প্রত্যেক নামাযের জন্য ওযুই যথেষ্ট। এই সকল অবস্থায় নামায মাফ নয়।\n\n" +
            "[আবূদাঊদ, সুনান, তিরমিযী, সুনান, মিশকাত ৫৬০-৫৬১ নং]\n\n" +
            "প্রকাশ যে, গোসল, ওযু বা অন্যান্য কর্মের সময় নিয়ত আরবীতে বা নিজ ভাষায় মুখে উচ্চারণ করা বিদআত।\n\n" +
            "সতর্কতার বিষয় যে, নাপাকী দূর করার জন্য কেবল গা-ধোয়া বা গা ডুবিয়ে নেওয়া যথেষ্ট নয়। পূর্বে ওযু করে যথানিয়মে গোসল করলে তবেই পূর্ণ গোসল হয়। নচেৎ অনেকের মতে কুল্লি না করলে এবং নাকে পানি না নিলে গোসলই শুদ্ধ হবে না।\n\n" +
            "[আলমুমতে’, শারহে ফিক্হ, ইবনে উষাইমীন ১/৩০৪]",
            "When performing Ghusl for purification from ritual impurity, a Muslim begins with the intention and first washes both hands up to the wrists 3 times. Then pours water over the left hand to wash away the impurity from the body. Next, washes the left hand with earth or soap and performs a complete ablution (Wudu) just like for prayer. If the bathing place is not clean, the feet should be washed at the end of the bath. After Wudu, pour water over the head 3 times and wash the hair thoroughly so that water reaches the roots of all hair. Then pour water over the entire body 3 times and wash thoroughly.\n\n" +
            "[Bukhari, Muslim, Mishkat Hadith 435-436]\n\n" +
            "Women's Ghusl is similar to men's. However, if a woman has braided hair, it is not mandatory to untie the braids. But water must be poured 3 times ensuring the roots of the hair are thoroughly washed.\n\n" +
            "[Bukhari, Mishkat Hadith 438]\n\n" +
            "If there is nail polish or any thick impermeable coating on nails, Ghusl will not be valid until it is removed. On the other hand, Ghusl is valid with henna or altah. If there is a decorative bindi/sticker on the forehead, it must be removed and the forehead washed; otherwise Ghusl is invalid.\n\n" +
            "A single Ghusl is sufficient with combined intentions for: Janabah and menses, or menses and Eid, or Janabah and Jumu'ah/Eid. There is no need for separate baths.\n\n" +
            "[Fiqhus Sunnah Urdu, p. 60]\n\n" +
            "There is no need for a separate Wudu for prayer after Ghusl. As long as nothing that nullifies Wudu occurs after the bath, one can pray with the Wudu performed during Ghusl.\n\n" +
            "[Abu Dawud, Tirmidhi, Nasa'i, Ibn Majah, Mishkat Hadith 445]\n\n" +
            "If someone experiences continuous involuntary discharge (semen, madhi, discharge, or Istihadah bleeding) due to illness, Ghusl is not obligatory upon them; fresh Wudu for each prayer time is sufficient. Prayer is not excused under these conditions.\n\n" +
            "[Abu Dawud, Tirmidhi, Mishkat Hadith 560-561]\n\n" +
            "Note that verbally uttering the intention in Arabic or one's own native tongue at the time of Ghusl, Wudu, or other deeds is an innovation (Bid'ah).\n\n" +
            "Caution: Merely washing or dipping the body into water is not sufficient to remove Janabah. A full valid Ghusl requires performing Wudu first and following the prescribed sequence. Otherwise, according to many scholars, Ghusl is invalid without rinsing the mouth and inhaling water into the nose.\n\n" +
            "[Al-Mumti', Sharh al-Fiqh, Ibn Uthaymeen 1/304]"
        ));

        return list;
    }
}
