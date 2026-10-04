package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Hajj & Umrah Tawaf Content.
 * Contains 100% verbatim text matching screenshots for the Tawaf section.
 */
public class HajjTawafContentRepository {

    public static List<HajjHistoryCardItem> getTawafCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. তাওয়াফ এর বিবরণ
        list.add(new HajjHistoryCardItem(
            1,
            "তাওয়াফ এর বিবরণ",
            "Description of Tawaf",
            "তাওয়াফ (আরবি: طواف) একটি ইসলামি ধর্মীয় রীতি। হজ্জ ও উমরার সময় মুসলিমরা কাবার চারপাশে ঘড়ির কাটার বিপরীতদিকে সাতবার ঘোরে যা তাওয়াফ নামে পরিচিত।",
            "Tawaf (Arabic: طواف) is an Islamic religious rite. During Hajj and Umrah, Muslims circumambulate the Holy Kaaba seven times counter-clockwise, which is known as Tawaf.",
            "তাওয়াফ (আরবি: طواف) একটি ইসলামি ধর্মীয় রীতি। হজ্জ ও উমরার সময় মুসলিমরা কাবার চারপাশে ঘড়ির কাটার বিপরীতদিকে সাতবার ঘোরে যা তাওয়াফ নামে পরিচিত।",
            "Tawaf (Arabic: طواف) is an Islamic religious rite. During Hajj and Umrah, Muslims circumambulate the Holy Kaaba seven times counter-clockwise, which is known as Tawaf."
        ));

        // 2. তাওয়াফের সংজ্ঞা
        list.add(new HajjHistoryCardItem(
            2,
            "তাওয়াফের সংজ্ঞা",
            "Definition of Tawaf",
            "কোনো কিছুর চারদিকে প্রদক্ষিণ করাকে শাব্দিক অর্থে তাওয়াফ বলে। হজ্জের ক্ষেত্রে কাবা শরীফের চতুর্দিকে প্রদক্ষিণ করাকে তাওয়াফ বলে। পবিত্র কাবা ব্যতীত অন্য কোনো জায়গায় কোনো জিনিসকে কেন্দ্র করে তাওয়াফ করা হারাম।",
            "Literally, Tawaf means circumambulating or walking around something. In the context of pilgrimage, circumambulating around the Holy Kaaba is called Tawaf. It is strictly forbidden to perform Tawaf around any object or place other than the Holy Kaaba.",
            "কোনো কিছুর চারদিকে প্রদক্ষিণ করাকে শাব্দিক অর্থে তাওয়াফ বলে। হজ্জের ক্ষেত্রে কাবা শরীফের চতুর্দিকে প্রদক্ষিণ করাকে তাওয়াফ বলে। পবিত্র কাবা ব্যতীত অন্য কোনো জায়গায় কোনো জিনিসকে কেন্দ্র করে তাওয়াফ করা হারাম।",
            "Literally, Tawaf means circumambulating or walking around something. In the context of pilgrimage, circumambulating around the Holy Kaaba is called Tawaf. It is strictly forbidden (Haram) to perform Tawaf around any object or place other than the Holy Kaaba."
        ));

        // 3. তাওয়াফের ফজিলত
        list.add(new HajjHistoryCardItem(
            3,
            "তাওয়াফের ফজিলত",
            "Virtues of Tawaf",
            "হাদিসে এসেছে, ‘যে ব্যক্তি বায়তুল্লাহর তাওয়াফ করল, ও দু’রাকাত সালাত আদায় করল, তার এ কাজ একটি গোলাম আযাদের সমতুল্য হল।\n\n[ফাতহুল বারী - ৩/৩০৩ হাদিস নং ১৬৪১]\n\nহাদিসে আরো এসেছে, ‘তুমি যখন বায়তুল্লাহর তাওয়াফ করলে, পাপ থেকে এমনভাবে বের হয়ে গেলে যেমন নাকি আজই তোমার মাতা তোমাকে জন্ম দিলেন।\n\n[মুসান্নাফু আব্দিুররাজ্জাক - ৮৮৩০]",
            "It is narrated in the Hadith: 'Whoever circumambulates the House of Allah (Baytullah) and prays two rak'ahs of prayer, it is equivalent to freeing a slave.'\n\n[Fath al-Bari - 3/303, Hadith No. 1641]\n\nIt is also narrated in the Hadith: 'When you perform Tawaf around Baytullah, you emerge from sins as pure as the day your mother gave birth to you.'\n\n[Musannaf Abdur Razzaq - 8830]",
            "হাদিসে এসেছে, ‘যে ব্যক্তি বায়তুল্লাহর তাওয়াফ করল, ও দু’রাকাত সালাত আদায় করল, তার এ কাজ একটি গোলাম আযাদের সমতুল্য হল।\n\n<b>[ফাতহুল বারী - ৩/৩০৩ হাদিস নং ১৬৪১]</b>\n\nহাদিসে আরো এসেছে, ‘তুমি যখন বায়তুল্লাহর তাওয়াফ করলে, পাপ থেকে এমনভাবে বের হয়ে গেলে যেমন নাকি আজই তোমার মাতা তোমাকে জন্ম দিলেন।\n\n<b>[মুসান্নাফু আব্দিুররাজ্জাক - ৮৮৩০]</b>",
            "It is narrated in the Hadith: 'Whoever circumambulates the House of Allah (Baytullah) and prays two rak'ahs of prayer, it is equivalent to freeing a slave.'\n\n<b>[Fath al-Bari - 3/303, Hadith No. 1641]</b>\n\nIt is also narrated in the Hadith: 'When you perform Tawaf around Baytullah, you emerge from sins as pure as the day your mother gave birth to you.'\n\n<b>[Musannaf Abdur Razzaq - 8830]</b>"
        ));

        // 4. তাওয়াফের প্রকারভেদ
        list.add(new HajjHistoryCardItem(
            4,
            "তাওয়াফের প্রকারভেদ",
            "Types of Tawaf",
            "এফরাদ হজ্জকারী মক্কায় এসে প্রথম যে তাওয়াফ আদায় করে তাকে তাওয়াফে কুদুম বলে। কেরান হজ্জকারী ও তামাত্তু হজ্জকারী উমরার উদ্দেশ্যে যে তাওয়াফ করে থাকেন তা তাওয়াফে কুদুমেরও স্থলাভিষিক্ত হয়ে যায়। ...\n" +
            "সকল হজ্জকারীকেই এ তাওয়াফটি আদায় করতে হয়। এটা হল হজ্জের ফরজ তাওয়াফ যা বাদ পড়লে হজ্জ সম্পন্ন হবে না। তাওয়াফে যিয়ারত আদায়ের আওয়াল ওয়াক্ত শুরু হয় ১০ তারিখ সুবহে সাদেক উদয়ের পর থেকে। জমহুর ফুকাহাদের মতে এর শেষ কোনো ওয়াক্ত নির্ধারিত নেই। ...\n" +
            "বায়তুল্লাহ শরীফ হতে প্রত্যাবর্তনের সময় যে তাওয়াফ করা হয় তাকে তাওয়াফে বিদা বলে। এ তাওয়াফ কেবল বহিরাগতদের ক্ষেত্রে প্রযোজ্য। মক্কায় বসবাসকারীদের জন্য প্রযোজ্য নয়। ...\n" +
            "উমরা আদায়ের ক্ষেত্রে এ তাওয়াফ ফরজ ও রুকন। এ তাওয়াফে রামল ও ইযতিবা উভয়টাই রয়েছে।\n" +
            "ইহা মান্নত হজ্জকারীদের ওপর ওয়াজিব।\n" +
            "ইহা মসজিদুল হারামে প্রবেশকারীদের জন্য মুস্তাহাব। তবে যদি কেউ অন্য কোনো তাওয়াফ করে থাকে তাহলে সেটিই এ তাওয়াফের স্থলাভিষিক্ত হবে।\n" +
            "যখন ইচ্ছা তখনই এ তাওয়াফ সম্পন্ন করা যায়.",
            "The first Tawaf performed by an Ifrad pilgrim upon arriving in Makkah is called Tawaf al-Qudum. For Qiran and Tamattu pilgrims, the Umrah Tawaf substitutes for Tawaf al-Qudum...\n" +
            "All pilgrims must perform this Tawaf. This is the obligatory (Fard) Tawaf of Hajj without which Hajj is incomplete. Tawaf al-Ziyarah begins from dawn on the 10th of Dhul Hijjah...\n" +
            "The Tawaf performed upon departure is called Tawaf al-Wada. It is obligatory for all out-of-town pilgrims...\n" +
            "For Umrah, this Tawaf is Fard and Rukn. Both Ramal and Idtiba are observed.\n" +
            "This is Wajib for pilgrims who made a vow (Nadhr).\n" +
            "This is Mustahabb for anyone entering Masjid al-Haram.\n" +
            "This voluntary (Nafl) Tawaf can be performed at any time.",
            "<b>১. তাওয়াফে কুদুম:</b>\n" +
            "এফরাদ হজ্জকারী মক্কায় এসে প্রথম যে তাওয়াফ আদায় করে তাকে তাওয়াফে কুদুম বলে। কেরান হজ্জকারী ও তামাত্তু হজ্জকারী উমরার উদ্দেশ্যে যে তাওয়াফ করে থাকেন তা তাওয়াফে কুদুমেরও স্থলাভিষিক্ত হয়ে যায়。\n\n" +
            "তবে হানাফি মাজহাব অনুযায়ী কেরান হজ্জকারীকে উমরার তাওয়াফের পর ভিন্নভাবে তাওয়াফে কুদুম আদায় করতে হয়। হানাফি মাজহাবে তামাত্তু ও শুধু উমরা পালনকারীর জন্য কোনো তাওয়াফে কুদুম নেই。\n\n" +
            "কুদুম শব্দের অর্থ আগমণ। সে হিসেবে তাওয়াফে কুদুম কেবল বহিরাগত হাজিদের ক্ষেত্রেই প্রযোজ্য। মক্কায় বসবাসকারীরা যেহেতু অন্য কোথাও থেকে আগমন করে না, তাই তাদের জন্য তাওয়াফে কুদুম সুন্নত নয়。\n\n" +
            "<b>২. তাওয়াফে এফাদা বা যিয়ারত:</b>\n" +
            "সকল হজ্জকারীকেই এ তাওয়াফটি আদায় করতে হয়। এটা হল হজ্জের ফরজ তাওয়াফ যা বাদ পড়লে হজ্জ সম্পন্ন হবে না। তাওয়াফে যিয়ারত আদায়ের আওয়াল ওয়াক্ত শুরু হয় ১০ তারিখ সুবহে সাদেক উদয়ের পর থেকে। জমহুর ফুকাহার নিকট ১৩ তারিখ সূর্যাস্তের পূর্বে সম্পন্ন করা ভাল। এর পরে করলেও কোনো সমস্যা নেই। সাহেবাইন (ইমাম আবু ইউসুফ ও মুহাম্মদ) এর নিকট তাওয়াফে এফেদা আদায়ের সময়সীমা উন্মুক্ত। ইমাম আবু হানিফা (র) এর নিকট তাওয়াফে যিয়ারত আদায়ের ওয়াজিব সময় হল ১২ তারিখ সূর্যাস্ত পর্যন্ত। এ সময়ের পরে তাওয়াফে যিয়ারত আদায় করলে ফরজ আদায় হয়ে যাবে তবে ওয়াজিব তরক হওয়ার কারণে দম দিয়ে ক্ষতিপূরণ করতে হবে। তাওয়াফে যিয়ারত আদায়ের পূর্বে স্বামী-স্ত্রী একে অন্যের জন্য হালাল হয় না。\n\n" +
            "<b>৩. তাওয়াফে বিদা বা বিদায়ি তাওয়াফ:</b>\n" +
            "বায়তুল্লাহ শরীফ হতে প্রত্যাবর্তনের সময় যে তাওয়াফ করা হয় তাকে তাওয়াফে বিদা বলে। এ তাওয়াফ কেবল বহিরাগতদের ক্ষেত্রে প্রযোজ্য। মক্কায় বসবাসকারীদের জন্য প্রযোজ্য নয়। যেহেতু মক্কায় বসবাসকারী হাজিদের জন্য প্রযোজ্য নয়, তাই এ তাওয়াফ হজ্জের অংশ কি-না তা নিয়ে বিতর্ক রয়েছে। কেননা হজ্জের অংশ হলে মক্কাবাসী এ থেকে অব্যাহতি পেত না। মুসলিম শরীফের একটি হাদিস থেকেও বুঝা যায় যে বিদায়ি তাওয়াফ হজ্জের অংশ নয়। হাদিসটিতে রাসূলুল্লাহ (ﷺ)বলেছেন, يقيم المهاجربمكة بعد قضاء نسكه ثلاثا মুহাজির ব্যক্তি হজ্জের কার্যক্রম সম্পন্ন করার পর মক্কায় তিন দিন অবস্থান করবে。\n\n" +
            "<b>[মুসলিম - ২৪০৯]</b>\n\n" +
            "<b>৪. তাওয়াফে উমরা:</b>\n" +
            "উমরা আদায়ের ক্ষেত্রে এ তাওয়াফ ফরজ ও রুকন। এ তাওয়াফে রামল ও ইযতিবা উভয়টাই রয়েছে。\n\n" +
            "<b>৫. তাওয়াফে নযর:</b>\n" +
            "ইহা মান্নত হজ্জকারীদের ওপর ওয়াজিব。\n\n" +
            "<b>৬. তাওয়াফে তাহিয়্যা:</b>\n" +
            "ইহা মসজিদুল হারামে প্রবেশকারীদের জন্য মুস্তাহাব। তবে যদি কেউ অন্য কোনো তাওয়াফ করে থাকে তাহলে সেটিই এ তাওয়াফের স্থলাভিষিক্ত হবে。\n\n" +
            "<b>৭. নফল তাওয়াফ:</b>\n" +
            "যখন ইচ্ছা তখনই এ তাওয়াফ সম্পন্ন করা যায়।",
            "<b>1. Tawaf al-Qudum (Arrival Tawaf):</b>\n" +
            "The first Tawaf performed by an Ifrad pilgrim upon arriving in Makkah is called Tawaf al-Qudum. For pilgrims performing Qiran or Tamattu, the Umrah Tawaf substitutes for Tawaf al-Qudum.\n\n" +
            "However, according to the Hanafi Madhhab, a Qiran pilgrim must perform Tawaf al-Qudum separately after the Umrah Tawaf. In the Hanafi school, there is no Tawaf al-Qudum for Tamattu or Umrah-only pilgrims.\n\n" +
            "Qudum means arrival. Accordingly, Tawaf al-Qudum applies strictly to visiting pilgrims from outside Makkah. Since Makkah residents do not arrive from outside, Tawaf al-Qudum is not Sunnah for them.\n\n" +
            "<b>2. Tawaf al-Ifadah or Ziyarah (Obligatory Pillar):</b>\n" +
            "All pilgrims must perform this circumambulation. This is the obligatory (Fard) Tawaf of Hajj without which Hajj is incomplete. The earliest time for Tawaf al-Ziyarah begins after dawn on the 10th of Dhul Hijjah. According to the majority of jurists, completing it before sunset on the 13th is best, though performing it later is valid without penalty. According to Sahibayn (Imam Abu Yusuf & Imam Muhammad), the time for Tawaf al-Ifadah remains open. According to Imam Abu Hanifah (RA), the Wajib timeframe for Tawaf al-Ziyarah extends until sunset on the 12th; performing it after this fulfills the Fard but requires a sacrificial Dam to compensate for missing the Wajib deadline. Marital relations remain prohibited until Tawaf al-Ziyarah is completed.\n\n" +
            "<b>3. Tawaf al-Wada or Farewell Tawaf):</b>\n" +
            "The circumambulation performed upon departing from Baytullah is called Tawaf al-Wada. This Tawaf applies solely to out-of-town pilgrims and not to Makkah residents. Since it does not apply to residents, there is scholarly discussion regarding whether it forms an intrinsic part of Hajj, for if it were a core part, residents would not be exempt. A narration in Sahih Muslim also indicates it is distinct: the Prophet (ﷺ) said, 'A Muhajir may stay in Makkah for three days after completing his rites.'\n\n" +
            "<b>[Sahih Muslim - 2409]</b>\n\n" +
            "<b>4. Tawaf al-Umrah:</b>\n" +
            "For performing Umrah, this Tawaf is an essential pillar (Fard & Rukn). In this Tawaf, both Ramal and Idtiba are observed.\n\n" +
            "<b>5. Tawaf an-Nadhr (Vowed Tawaf):</b>\n" +
            "This is obligatory (Wajib) for pilgrims who made a vow (Nadhr).\n\n" +
            "<b>6. Tawaf at-Tahiyyah:</b>\n" +
            "This is recommended (Mustahabb) for anyone entering Masjid al-Haram. However, if any other Tawaf is performed, it substitutes for this.\n\n" +
            "<b>7. Nafl Tawaf (Voluntary Circumambulation):</b>\n" +
            "This voluntary circumambulation can be performed at any time whenever desired."
        ));

        // 5. তাওয়াফ বিষয়ক কিছু জরুরি মাসায়েল
        list.add(new HajjHistoryCardItem(
            5,
            "তাওয়াফ বিষয়ক কিছু জরুরি মাসায়েল",
            "Essential Rulings Regarding Tawaf",
            "তাওয়াফের পূর্বে পবিত্রতা জরুরি। কেননা আপনি আল্লাহর ঘর তাওয়াফ করতে যাচ্ছেন যা পৃথিবীর বুকে পবিত্রতম জায়গা। বিদায় হজ্জের সময় রাসূলুল্লাহ (ﷺ)প্রথমে ওজু করেছেন, তারপর তাওয়াফ শুরু করেছেন।\n" +
            "আর রাসূলুল্লাহ (ﷺ)যেভাবে হজ্জ করেছেন আমাদেরকেও তিনি সেভাবেই হজ্জ করতে বলেছেন। তিনি বলেছেন, ‘خذوا عنى مناسككم - আমার কাছ থেকে তোমাদের হজ্জকর্মসমূহ জেনে নাও।’\n" +
            "ইবনে আব্বাস থেকে বর্ণিত এক হাদিসে তাওয়াফকে সালাতের তুল্য বলা হয়েছে। পার্থক্য শুধু এতটুকু যে, আল্লাহ তা’আলা এতে কথা বলা বৈধ করে দিয়েছেন, তবে যে কথা বলতে চায় সে যেন উত্তম কথা বলে।\n" +
            "এহরাম অবস্থায় আয়েশা (রাঃ) এর ঋতুস্রাব শুরু হলে রাসূলুল্লাহ (ﷺ)তাঁকে তাওয়াফ করতে নিষেধ করে দেন।\n" +
            "এ হাদিসও তাওয়াফের সময় পবিত্রতার গুরুত্বের প্রতিই ইঙ্গিত দিচ্ছে। সে কারণেই ইমাম মোহাম্মদ ও ইমাম আবু ইউসুফ ওজু অবস্থায় তাওয়াফ করাকে ওয়াজিব বলেছেন।\n" +
            "তাওয়াফের সময় সতর ঢাকাও জরুরি,...\n" +
            "ইবনে আব্বাস (রাঃ) সৌন্দর্য অর্থ পোশাক বলেছেন। এক হাদিস অনুযায়ী তাওয়াফও একপ্রকার সালাত তা পূর্বেই উল্লেখ হয়েছে। তাছাড়া ৯ হিজরীতে, হজ্জের সময় পবিত্র কাবা তাওয়াফের সময় যেন কেউ উলঙ্গ হয়ে তাওয়াফ না করে সে মর্মে ফরমান জারি করা হয়।\n" +
            "তাওয়াফের শুরুতে নিয়ত করা বাঞ্ছনীয়- তবে সুনির্ধারিতভাবে নিয়ত করতে হবে না। বরং মনে মনে এরূপ প্রতিজ্ঞা করলেই চলবে যে আমি আল্লাহর ঘর তাওয়াফ করতে যাচ্ছি। অনেক বই-পুস্তকে তাওয়াফের যে নিয়ত লেখা আছে-আল্লাহ...\n" +
            "হানাফি মাজহাব অনুসারে যে তাওয়াফের পর সাফা-মারওয়ার সাঈ আছে সে তাওয়াফের প্রথম তিন চক্করে রমল ও পুরা তাওয়াফে ইযতিবা আছে। ...\n" +
            "ঋতুস্রাব অবস্থায় নারীরা তাওয়াফ করবে না। প্রয়োজন হলে হজ্জের সময়ে ঋতুস্রাব ঠেকানোর জন্য ওষুধ ব্যবহার করা যেতে পারে, ব্যবহার করার বৈধতা রয়েছে। তাওয়াফের সময় নারীর জন্য কোনো রামল বা ইযতিবা নেই। কেননা রাসূলুল্লাহ (ﷺ)...",
            "Ritual purity is essential prior to Tawaf. You are about to circumambulate the House of Allah, the holiest place on earth. During the Farewell Pilgrimage, the Messenger of Allah (ﷺ) performed Wudu first, and then commenced Tawaf...\n" +
            "The Prophet (ﷺ) said: 'Learn your rituals of Hajj from me.' In a Hadith narrated by Ibn Abbas, Tawaf is compared to Salah...\n" +
            "Covering the Awrah during Tawaf is also obligatory...\n" +
            "Making intention at the start of Tawaf is essential...\n" +
            "Ramal in the first 3 circuits and Idtiba are Sunnah if Sa'i follows...\n" +
            "Directives for women during Tawaf...",
            "তাওয়াফের পূর্বে পবিত্রতা জরুরি। কেননা আপনি আল্লাহর ঘর তাওয়াফ করতে যাচ্ছেন যা পৃথিবীর বুকে পবিত্রতম জায়গা। বিদায় হজ্জের সময় রাসূলুল্লাহ (ﷺ)প্রথমে ওজু করেছেন, তারপর তাওয়াফ শুরু করেছেন।\n\n" +
            "<b>[ফাতহুল বারী - ৩/৩০৩ , হাদিস নং ১৬৪১]</b>\n\n" +
            "আর রাসূলুল্লাহ (ﷺ)যেভাবে হজ্জ করেছেন আমাদেরকেও তিনি সেভাবেই হজ্জ করতে বলেছেন। তিনি বলেছেন, ‘خذوا عنى مناسككم - আমার কাছ থেকে তোমাদের হজ্জকর্মসমূহ জেনে নাও।’\n\n" +
            "<b>[শারহুননববী আলা মুসলিম - খন্ড ৮ / ২২০]</b>\n\n" +
            "ইবনে আব্বাস থেকে বর্ণিত এক হাদিসে তাওয়াফকে সালাতের তুল্য বলা হয়েছে। পার্থক্য শুধু এতটুকু যে, আল্লাহ তা’আলা এতে কথা বলা বৈধ করে দিয়েছেন, তবে যে কথা বলতে চায় সে যেন উত্তম কথা বলে।\n\n" +
            "<b>[এরওয়া - ২১]</b>\n\n" +
            "এহরাম অবস্থায় আয়েশা (রাঃ) এর ঋতুস্রাব শুরু হলে রাসূলুল্লাহ (ﷺ)তাঁকে তাওয়াফ করতে নিষেধ করে দেন।\n\n" +
            "<b>[মুসলিম]</b>\n\n" +
            "এ হাদিসও তাওয়াফের সময় পবিত্রতার গুরুত্বের প্রতিই ইঙ্গিত দিচ্ছে। সে কারণেই ইমাম মোহাম্মদ ও ইমাম আবু ইউসুফ ওজু অবস্থায় তাওয়াফ করাকে ওয়াজিব বলেছেন।\n\n" +
            "<b>[খালিসূল জুমান- ১৮২]</b>\n\n" +
            "<b>তাওয়াফের সময় সতর ঢাকাও জরুরি:</b>\n" +
            "কেননা জাহেলি-যুগে উলঙ্গ হয়ে তাওয়াফ করার প্রথাকে বন্ধ করার জন্য পবিত্র কুরআনে এরশাদ হয়েছে-\n\n" +
            "يَا بَنِي آَدَمَ خُذُوا زِينَتَكُمْ عِنْدَ كُلِّ مَسْجِدٍ.\n\n" +
            "'হে বনী আদম, প্রত্যেক সালাতের সময় তোমরা সৌন্দর্য অবলম্বন করো।'\n\n" +
            "<b>[সূরা আরাফ - ৩১]</b>\n\n" +
            "ইবনে আব্বাস (রাঃ) সৌন্দর্য অর্থ পোশাক বলেছেন। এক হাদিস অনুযায়ী তাওয়াফও একপ্রকার সালাত তা পূর্বেই উল্লেখ হয়েছে। তাছাড়া ৯ হিজরীতে, হজ্জের সময় পবিত্র কাবা তাওয়াফের সময় যেন কেউ উলঙ্গ হয়ে তাওয়াফ না করে সে মর্মে ফরমান জারি করা হয়।\n\n" +
            "<b>[ইবনে কাছীর - খন্ড১, পৃ: ১৫৭]</b>\n\n" +
            "<b>তাওয়াফের শুরুতে নিয়ত করা বাঞ্ছনীয়:</b>\n" +
            "তবে সুনির্ধারিতভাবে নিয়ত করতে হবে না। বরং মনে মনে এরূপ প্রতিজ্ঞা করলেই চলবে যে আমি আল্লাহর ঘর তাওয়াফ করতে যাচ্ছি। অনেক বই-পুস্তকে তাওয়াফের যে নিয়ত লেখা আছে-আল্লাহুম্মা ইন্নি উরিদু তাওয়াফা বায়তিকাল হারাম ফা য়াস্সিরহু লি ওয়া তাকাববালহু মিন্নি—হাদিসে এর কোনো ভিত্তি নেই।\n\n" +
            "<b>সাত চক্করে তাওয়াফ শেষ করা উচিৎ:</b>\n" +
            "চার চক্করে তাওয়াফ শেষ করা কখনো উচিৎ নয়। কেননা রাসূলুল্লাহ (ﷺ)সাহাবায়ে কেরাম, তাবেইন, তাবে-তাবেইনদের মধ্যে কেউ চার চক্করে তাওয়াফ শেষ করেছেন বলে হাদিস ও ইতিহাসে নেই।\n\n" +
            "<b>তাওয়াফ শুরুর ও শেষের স্থান:</b>\n" +
            "তাওয়াফ হজ্জরে আসওয়াদ থেকে শুরু করে হাজরে আসওয়াদ বরাবর এসে শেষ করতে হবে- কেউ যদি হজ্জরে আসওয়াদের বরাবর আসার একটু পূর্বেও তাওয়াফ ছেড়ে দেয় তাহলে তার তাওয়াফ শুদ্ধ বলে গণ্য হবে না।\n\n" +
            "<b>তাওয়াফ করার সময় রামল ও ইযতিবা:</b>\n" +
            "কোন কোন তাওয়াফে রামল ও ইযতিবা আছে তা নিয়ে ফেকাহবিদদের মধ্যে বিতর্ক রয়েছে। উমরার তাওয়াফ ও কুদুমের তাওয়াফেই কেবল ইযতিবা আছে, এটাই হল বিশুদ্ধ অভিমত। কেননা রাসূলুল্লাহ (ﷺ)এ দু’ধরনের তাের তাওয়াফে রমল ও ইযতিবা করেছেন।\n\n" +
            "<b>[ফাতহুল বারী - ৩/২৬৯]</b>\n\n" +
            "হানাফি মাজহাব অনুসারে যে তাওয়াফের পর সাফা-মারওয়ার সাঈ আছে সে তাওয়াফের প্রথম তিন চক্করে রমল ও পুরা তাওয়াফে ইযতিবা আছে।\n\n" +
            "<b>নারীর তাওয়াফ:</b>\n" +
            "নারী অবশ্যই তাওয়াফ করবে। তবে পুরুষদের সাথে মিশ্রিত হয়ে নয়। যখন ভিড় কম থাকে তখন নারীদের তাওয়াফ করা বাঞ্ছনীয়। অথবা, একটু সময় বেশি লাগলেও দূর দিয়ে নারীরা তাওয়াফ করবে। পুরুষের ভিড়ে নারীরা হাজরে আসওয়াদ চুম্বন করতে যাবে না। আয়েশা (রাঃ) এর তাওয়াফের ব্যাপারে হাদিসে এসেছে-\n\n" +
            "كانت عائشة رضى الله عنها تطوف حجرة من الرجال ، لا تخالطهم ، فقالت امرأة: انطلقى نستلم يا أم المؤمنين . قالت: انطلقي -- عنك ، وأبت\n\n" +
            "'আয়েশা (রাঃ) পুরুষদের একপাশ হয়ে একাকী তাওয়াফ করতেন। পুরুষদের সাথে মিশতেন না। এক মহিলা বললেন: চলুন, হাজরে আসওয়াদ চুম্বন-স্পর্শ করি। তিনি বললেন, তুমি যাও—আমাকে ছাড়। তিনি যেতে অস্বীকার করলেন।'\n\n" +
            "<b>[বুখারি - ১৫১৩]</b>\n\n" +
            "ঋতুস্রাব অবস্থায় নারীরা তাওয়াফ করবে না। প্রয়োজন হলে হজ্জের সময়ে ঋতুস্রাব ঠেকানোর জন্য ওষুধ ব্যবহার করা যেতে পারে, ব্যবহার করার বৈধতা রয়েছে। তাওয়াফের সময় নারীর জন্য কোনো রামল বা ইযতিবা নেই। কেননা রাসূলুল্লাহ (ﷺ)নারীকে রামল ইযতিবা করতে বলেননি।\n\n" +
            "হজ্জের ফরজ তাওয়াফের সময় যদি কারও ঋতুস্রাব চলে আসে এবং ঋতুস্রাব বন্ধ হওয়া পর্যন্ত মক্কায় অবস্থান করা কোনো ক্রমেই সম্ভব না হয়, পরবর্তীতে এসে ফরজ তাওয়াফ আদায় করারও কোনো সুযোগ না থাকে, এমন পরিস্থিতিতে বিজ্ঞ ওলামাগণ ফতোয়া দিয়েছেন যে ন্যাপকিন দিয়ে ভালো করে বেঁধে তাওয়াফ আদায় করে নিতে পারে।",
            "Ritual purity is essential prior to Tawaf. You are about to circumambulate the House of Allah, the holiest place on earth. During the Farewell Pilgrimage, the Messenger of Allah (ﷺ) performed Wudu first, and then commenced Tawaf.\n\n" +
            "<b>[Fath al-Bari - 3/303, Hadith No. 1641]</b>\n\n" +
            "The Messenger of Allah (ﷺ) instructed us to perform Hajj in the exact manner he did. He said: 'Learn your rituals of Hajj from me.'\n\n" +
            "<b>[Sharh an-Nawawi 'ala Muslim - Vol 8 / 220]</b>\n\n" +
            "In a Hadith narrated by Ibn Abbas, Tawaf is compared to Salah, with the only difference being that Allah has permitted talking during Tawaf, but whoever speaks should utter only good words.\n\n" +
            "<b>[Irwa al-Ghalil - 21]</b>\n\n" +
            "When Mother of the Believers Aisha (RA) began menstruating during Ihram, the Messenger of Allah (ﷺ) prohibited her from performing Tawaf.\n\n" +
            "<b>[Muslim]</b>\n\n" +
            "This Hadith underscores the vital importance of purity during Tawaf. Therefore, Imam Muhammad and Imam Abu Yusuf declared performing Tawaf in a state of Wudu to be Wajib.\n\n" +
            "<b>[Khalisul Juman - 182]</b>\n\n" +
            "<b>Covering the Awrah is Obligatory:</b>\n" +
            "To eradicate the Jahiliyyah custom of circumambulating unclothed, Almighty Allah revealed:\n\n" +
            "يَا بَنِي آَدَمَ خُذُوا زِينَتَكُمْ عِنْدَ كُلِّ مَسْجِدٍ.\n\n" +
            "'O Children of Adam! Take your adornment (proper clothing) at every place of prayer.'\n\n" +
            "<b>[Surah Al-A'raf - 31]</b>\n\n" +
            "Ibn Abbas (RA) explained that adornment refers to proper clothing. Furthermore, in the 9th year of Hijrah during Hajj, an official decree was proclaimed forbidding anyone from circumambulating the Holy Kaaba naked.\n\n" +
            "<b>[Ibn Kathir - Vol 1, p. 157]</b>\n\n" +
            "<b>Intention (Niyyah) for Tawaf:</b>\n" +
            "Making an intention at the commencement of Tawaf is recommended, though not in rigid formulated wording. Resolving in one's heart that one is circumambulating the House of Allah is sufficient. The formulated verbal phrases found in various books have no foundation in the authentic Sunnah.\n\n" +
            "<b>Completing Seven Circuits:</b>\n" +
            "Tawaf must strictly consist of seven full circuits; ending at four circuits is never permissible, as no narration from the Prophet (ﷺ), the Companions, or the Tabi'un supports completing Tawaf in four rounds.\n\n" +
            "<b>Starting and Ending Point:</b>\n" +
            "Tawaf must begin aligned with the Black Stone (Hajar al-Aswad) and conclude aligned with it. If a person terminates even slightly before the alignment, the Tawaf is invalid.\n\n" +
            "<b>Ramal and Idtiba:</b>\n" +
            "Jurists discuss which Tawafs incorporate Ramal and Idtiba. The most sound position is that Idtiba applies strictly to the Umrah Tawaf and Tawaf al-Qudum, as the Prophet (ﷺ) observed Ramal and Idtiba in these two.\n\n" +
            "<b>[Fath al-Bari - 3/269]</b>\n\n" +
            "According to the Hanafi Madhhab, any Tawaf followed by Sa'i entails Ramal in the first three circuits and Idtiba throughout the entire Tawaf.\n\n" +
            "<b>Tawaf Directives for Women:</b>\n" +
            "Women must perform Tawaf, but without mixing with men. It is recommended for women to perform Tawaf when crowds are minimal or circumambulate along the outer perimeter. Women should not push through male crowds to kiss the Black Stone. Regarding Mother of the Believers Aisha (RA):\n\n" +
            "كانت عائشة رضى الله عنها تطوف حجرة من الرجال ، لا تخالطهم ، فقالت امرأة: انطلقى نستلم يا أم المؤمنين . قالت: انطلقي -- عنك ، وأبت\n\n" +
            "'Aisha (RA) circumambulated along the outer edge apart from men without mingling. A woman said to her: \"Let us go and touch the Black Stone, O Mother of the Believers.\" She replied: \"You go ahead—leave me,\" and declined to push into the crowd.'\n\n" +
            "<b>[Sahih al-Bukhari - 1513]</b>\n\n" +
            "Women experiencing menstruation must not perform Tawaf. If necessary, approved medication may be used to regulate cycles during Hajj. There is no Ramal or Idtiba for women.\n\n" +
            "If a woman experiences menstruation during the obligatory Tawaf al-Ifadah and cannot remain in Makkah until becoming pure nor return later, prominent scholars have issued rulings that she may securely dress with proper sanitary protection and perform the Tawaf out of unavoidable necessity."
        ));

        return list;
    }
}
