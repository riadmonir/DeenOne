package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Masail: মসজিদ (Masjid Masail).
 * 100% verbatim text matching screenshots and authentic islamic sources.
 */
public class MasailMasjidContentRepository {

    public static List<HajjHistoryCardItem> getMasjidCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. কোন প্রকার জুলুম ও অন্যায়ভাবে দখলকৃত জায়গার উপর মসজিদ নির্মাণ
        list.add(new HajjHistoryCardItem(
            1,
            "কোন প্রকার জুলুম ও অন্যায়ভাবে দখলকৃত জায়গার উপর মসজিদ নির্মাণ",
            "Building a Mosque on Oppressively or Illegitimately Usurped Land",
            "কোন প্রকার জুলুম ও অন্যায়ভাবে দখলকৃত জায়গার উপর মসজিদ নির্মাণ এবং জেনে-শুনে তাতে নামায পড়া বৈধ নয়। [মাজাল্লাতুল বুহুসিল ইসলামিয়্যাহ ১৭/৫৩]",
            "Building a mosque upon illegitimately usurped or seized land through injustice, and deliberately offering prayer upon it while aware of its usurpation, is unlawful...",
            "কোন প্রকার জুলুম ও অন্যায়ভাবে দখলকৃত জায়গার উপর মসজিদ নির্মাণ এবং জেনে-শুনে তাতে নামায পড়া বৈধ নয়। [মাজাল্লাতুল বুহুসিল ইসলামিয়্যাহ ১৭/৫৩]",
            "Building a mosque upon illegitimately usurped or seized land through injustice, and deliberately offering prayer upon it while aware of its usurpation, is unlawful (Haram).\n\n" +
            "[Majallatul Buhuthil Islamiyyah 17/53]"
        ));

        // 2. কোনও বিষয় নিয়ে কারো সাথে বিরোধ ঘটলে
        list.add(new HajjHistoryCardItem(
            2,
            "কোনও বিষয় নিয়ে কারো সাথে বিরোধ ঘটলে",
            "Preventing Someone from Entering Mosque Due to Personal Dispute",
            "কোনও বিষয় নিয়ে কারো সাথে বিরোধ ঘটলে তাকে মসজিদে আসতে বাধা দেওয়া উচিৎ নয়। কারণ, মহান আল্লাহ বলেন, “যে ব্যক্তি আল্লাহর মসজিদে তাঁর নাম স্মরণ (যিক্র) করতে বাধা দেয় ও তার ধ্বংস-সাধনে প্রয়াসী হয়, তার চেয়ে বড় জালেম আর কে হত পারে?” [কুরআন মাজীদ ২/১১৪]",
            "If a personal disagreement occurs with someone, it is not permissible to prevent them from coming to the mosque of Allah...",
            "কোনও বিষয় নিয়ে কারো সাথে বিরোধ ঘটলে তাকে মসজিদে আসতে বাধা দেওয়া উচিৎ নয়। কারণ, মহান আল্লাহ বলেন, “যে ব্যক্তি আল্লাহর মসজিদে তাঁর নাম স্মরণ (যিক্র) করতে বাধা দেয় ও তার ধ্বংস-সাধনে প্রয়াসী হয়, তার চেয়ে বড় জালেম আর কে হত পারে?” [কুরআন মাজীদ ২/১১৪]",
            "If a personal disagreement occurs with someone, it is not permissible to prevent them from coming to the mosque of Allah. Allah Almighty says: \"And who are more unjust than those who prevent the name of Allah from being mentioned in His mosques and strive for their ruin?\"\n\n" +
            "[Quran Majeed 2:114]"
        ));

        // 3. কোন অমুসলিম যদি বাহ্যিক পবিত্র অবস্থায় আদবের সাথে মসজিদ প্রবেশ
        list.add(new HajjHistoryCardItem(
            3,
            "কোন অমুসলিম যদি বাহ্যিক পবিত্র অবস্থায় আদবের সাথে মসজিদ প্রবেশ",
            "Entry of Non-Muslims into Mosque with Physical Cleanliness and Respect",
            "কোন অমুসলিম যদি বাহ্যিক পবিত্র অবস্থায় আদবের সাথে মসজিদ প্রবেশ করতে চায়, তবে তাতে কোন ক্ষতি হয় না। অবশ্য মক্কা (ও মদ্বীনার) হারাম ও মসজিদে তারা প্রবেশ করতে পারে না। [কুরআন মাজীদ ৯/২৮, মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ২১/২০, ৩২/৯৪, ১০৫]",
            "If a non-Muslim wishes to enter a mosque in a physically clean and respectful state for understanding or legitimate needs, there is no harm in it...",
            "কোন অমুসলিম যদি বাহ্যিক পবিত্র অবস্থায় আদবের সাথে মসজিদ প্রবেশ করতে চায়, তবে তাতে কোন ক্ষতি হয় না। অবশ্য মক্কা (ও মদ্বীনার) হারাম ও মসজিদে তারা প্রবেশ করতে পারে না। [কুরআন মাজীদ ৯/২৮, মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ২১/২০, ৩২/৯৪, ১০৫]",
            "If a non-Muslim wishes to enter a mosque in a physically clean and respectful manner for observing or seeking knowledge, there is no prohibition, provided it maintains sacred decorum. However, they are restricted from entering the Sacred Sanctuary (Haram) of Makkah.\n\n" +
            "[Quran Majeed 9:28, Majallatul Buhuthil Islamiyyah 21/20, 32/94, 105]"
        ));

        // 4. মসজিদের উপর দিয়ে রাস্তা
        list.add(new HajjHistoryCardItem(
            4,
            "মসজিদের উপর দিয়ে রাস্তা",
            "Using the Mosque as a Thoroughfare or Passage",
            "মসজিদের উপর দিয়ে রাস্তা করায় মসজিদের সম্মানহানি হয়। মহানবী (ﷺ) বলেন, “যিক্র ও নামায ছাড়া অন্য কিছুর জন্য মসজিদসমূহকে রাস্তা করে নিও না।” [ত্বাবারানী, মু’জাম, জামে ৭২১৫ নং] মসজিদকে রাস্তায় পরিণত করে তার সম্মান নষ্ট করা কিয়ামতের অন্যতম পূর্বলক্ষণ। [ত্বাবারানী, মু’জাম আউসাত্ব, জামে ৫৮৯৯ নং]",
            "Using a mosque merely as a pathway or thoroughfare degrades the sanctity and honor of the mosque...",
            "মসজিদের উপর দিয়ে রাস্তা করায় মসজিদের সম্মানহানি হয়। মহানবী (ﷺ) বলেন, “যিক্র ও নামায ছাড়া অন্য কিছুর জন্য মসজিদসমূহকে রাস্তা করে নিও না।” [ত্বাবারানী, মু’জাম, জামে ৭২১৫ নং]\n\nমসজিদকে রাস্তায় পরিণত করে তার সম্মান নষ্ট করা কিয়ামতের অন্যতম পূর্বলক্ষণ। [ত্বাবারানী, মু’জাম আউসাত্ব, জামে ৫৮৯৯ নং]",
            "Using a mosque merely as a pathway or thoroughfare degrades the sanctity and honor of the mosque. The Holy Prophet (ﷺ) said: \"Do not make mosques into thoroughfares except for remembrance (Dhikr) and prayer.\" [Tabarani, Mu'jam, Jami #7215]\n\nDegrading mosques by turning them into mere roads and shortcuts is among the prominent signs of the Last Day. [Tabarani, Mu'jam Awsat, Jami #5899]"
        ));

        // 5. মসজিদে কোন প্রকার খেলা
        list.add(new HajjHistoryCardItem(
            5,
            "মসজিদে কোন প্রকার খেলা",
            "Games and Sports Inside the Mosque",
            "মসজিদে কোন প্রকার খেলাও বৈধ নয়। অবশ্য যে খেলা জিহাদ বিষয়ক অথবা জিহাদের সহায়ক (অস্ত্রচালনার খেলা) তা বৈধ। হযরত আয়েশা (রাঃ) বলেন, ‘একদা হাবশী দল মসজিদে তাদের যুদ্ধাস্ত্র নিয়ে খেলা করছিল। আর আমি আল্লাহর রসূল (ﷺ) এর পশ্চাতে আড়ালে হুজরার দরজায় দাঁড়িয়ে থেকে তাদের খেলা দেখছিলাম।’ [বুখারী - ৪৫৪, ৪৫৫নং, প্রমুখ]",
            "General games and recreational sports are not permissible inside mosques. However, training exercises aiding defensive preparation or martial skills are permissible...",
            "মসজিদে কোন প্রকার খেলাও বৈধ নয়। অবশ্য যে খেলা জিহাদ বিষয়ক অথবা জিহাদের সহায়ক (অস্ত্রচালনার খেলা) তা বৈধ। হযরত আয়েশা (রাঃ) বলেন, ‘একদা হাবশী দল মসজিদে তাদের যুদ্ধাস্ত্র নিয়ে খেলা করছিল। আর আমি আল্লাহর রসূল (ﷺ) এর পশ্চাতে আড়ালে হুজরার দরজায় দাঁড়িয়ে থেকে তাদের খেলা দেখছিলাম।’ [বুখারী - ৪৫৪, ৪৫৫নং, প্রমুখ]",
            "General games and sports are not permissible inside the mosque. However, displays aiding martial training or defense skills are permissible. Aisha (RA) narrated: 'Once, an Abyssinian delegation was demonstrating their weapons play inside the mosque, and I was watching their display while standing behind the Messenger of Allah (ﷺ) concealed at the apartment entrance.'\n\n" +
            "[Bukhari - #454, 455 et al.]"
        ));

        // 6. বাড়ির কোন একটা কামরা বা নির্দিষ্ট জায়গাকে মসজিদ বানানো
        list.add(new HajjHistoryCardItem(
            6,
            "বাড়ির কোন একটা কামরা বা নির্দিষ্ট জায়গাকে মসজিদ বানানো",
            "Designating a Dedicated Room or Space at Home for Prayer",
            "বাড়ির কোন একটা কামরা বা নির্দিষ্ট জায়গাকে মসজিদ বানানো চলে। যাতে নফল নামায এবং মসজিদে যেতে না পারলে ফরয নামাযও পড়া যাবে। ইতবান বিন মালেক (রাঃ) এই রকমই একটি আবেদন আল্লাহর রসূল (ﷺ)কে জানালেন। তিনি তাঁর আবেদন মঞ্জুর করে তাঁর ঘরে গিয়ে তাঁর পছন্দমত এক স্থানে ২ রাকআত নামায পড়লেন। অনুরুপ বারা’ বিন আযেব (রাঃ) নিজ বাড়িতে (বাড়ির লোকদের নিয়ে) জামাআত করে নামায পড়তেন। [বুখারী ৪২৫নং]",
            "It is permissible to designate a specific room or area in one's home as a dedicated place of prayer (Musalla)...",
            "বাড়ির কোন একটা কামরা বা নির্দিষ্ট জায়গাকে মসজিদ বানানো চলে। যাতে নফল নামায এবং মসজিদে যেতে না পারলে ফরয নামাযও পড়া যাবে। ইতবান বিন মালেক (রাঃ) এই রকমই একটি আবেদন আল্লাহর রসূল (ﷺ)কে জানালেন। তিনি তাঁর আবেদন মঞ্জুর করে তাঁর ঘরে গিয়ে তাঁর পছন্দমত এক স্থানে ২ রাকআত নামায পড়লেন। অনুরুপ বারা’ বিন আযেব (রাঃ) নিজ বাড়িতে (বাড়ির লোকদের নিয়ে) জামাআত করে নামায পড়তেন। [বুখারী ৪২৫নং]",
            "It is permissible to designate a specific room or area in one's home as a dedicated place of prayer (Musalla) for offering voluntary prayers and obligatory prayers when unable to attend the mosque. Itban ibn Malik (RA) made such a request to the Messenger of Allah (ﷺ), who accepted his invitation, came to his home, and offered 2 Rak'ahs of prayer at a place of his choice. Similarly, Bara' ibn Azib (RA) used to lead congregational prayers in his house with his household members.\n\n" +
            "[Bukhari #425]"
        ));

        // 7. নূতন মসজিদ অপেক্ষা পুরাতন মসজিদের অধিক কোন ফযীলত ও বৈশিষ্ট্য আছে কি
        list.add(new HajjHistoryCardItem(
            7,
            "নূতন মসজিদ অপেক্ষা পুরাতন মসজিদের অধিক কোন ফযীলত ও বৈশিষ্ট্য আছে কি",
            "Is There Any Extra Virtue in an Older Mosque Over a Newer Mosque?",
            "নূতন মসজিদ অপেক্ষা পুরাতন মসজিদের অধিক কোন ফযীলত ও বৈশিষ্ট্য বা অধিক সওয়াব আছে এর কোন দলীল নেই। [আলমুমতে’, শারহে ফিক্হ, ইবনে উষাইমীন ৪/২১৬, ফাতাওয়া নাযীরিয়্যাহ্ ১/৩৫৯] তবে অপ্রয়োজনে যেহেতু একই মহ্ল্লায়...",
            "There is no authentic scriptural evidence that an older mosque inherently possesses greater virtue or superior reward over a newer mosque...",
            "নূতন মসজিদ অপেক্ষা পুরাতন মসজিদের অধিক কোন ফযীলত ও বৈশিষ্ট্য বা অধিক সওয়াব আছে এর কোন দলীল নেই।\n\n" +
            "[আলমুমতে’, শারহে ফিক্হ, ইবনে উষাইমীন ৪/২১৬, ফাতাওয়া নাযীরিয়্যাহ্ ১/৩৫৯]\n\n" +
            "তবে অপ্রয়োজনে যেহেতু একই মহ্ল্লায় একাধিক মসজিদ বিদআত, সেহেতু এর ফলে যেখানে ‘যিরার’ হওয়ার আশঙ্কা থাকে সেখানে নূতন ছেড়ে পুরাতন মসজিদে নামায পড়া উত্তম। কিছু সাহাবা ও সলফ এই আশঙ্কাতেই কোন কোন স্থানে পুরাতন মসজিদে নামায পড়েছেন। অবশ্য সেই মসজিদে নামায পড়া উত্তম, যে মসজিদ বিদআতশূন্য, যার জামাআত সংখ্যা অধিক\n\n" +
            "[আলমুমতে’, শারহে ফিক্হ, ইবনে উষাইমীন ৪/২১৩]\n\n" +
            "এবং যার ইমাম ফাসেক বা বিদআতী বলে আশঙ্কা নেই।",
            "There is no authentic scriptural evidence that an older mosque inherently possesses greater virtue, unique status, or superior reward merely due to its antiquity over a newly constructed mosque.\n\n" +
            "[Al-Mumti', Sharh al-Fiqh, Ibn Uthaymeen 4/216; Fatawa Naziriyyah 1/359]\n\n" +
            "However, since establishing multiple mosques without necessity in the same neighborhood is an innovation that can cause division or harm (Dirar), it is preferable in such circumstances to pray in the established older mosque rather than leaving it. Certain Companions and righteous predecessors prayed in original mosques out of this consideration. Furthermore, it is superior to pray in a mosque that is free from innovations, has a larger congregation [Al-Mumti', Sharh al-Fiqh, Ibn Uthaymeen 4/213], and whose Imam carries no suspicion of wrongdoing or un-Islamic innovation."
        ));

        return list;
    }
}
