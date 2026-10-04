package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.R;
import com.devflux.deenone.features.hajj.model.HajjTopicItem;

import java.util.ArrayList;
import java.util.List;

public class HajjContentRepository {

    public static HajjTopicItem getBannerItem() {
        return new HajjTopicItem(
            0,
            "ধর্ম মন্ত্রণালয় হজ ও উমরাহ সহায়িকা",
            "Ministry of Religious Affairs Hajj & Umrah Handbook",
            "সরকারি ও আন্তর্জাতিক নির্দেশনা",
            "Official Government & Saudi Ministry Guidelines",
            R.drawable.ic_hajj_banner_kaaba,
            "গণপ্রজাতন্ত্রী বাংলাদেশ সরকারের ধর্ম বিষয়ক মন্ত্রণালয় এবং সৌদি আরব সরকারের হজ ও ওমরাহ মন্ত্রণালয় কর্তৃক প্রকাশিত হজ ও ওমরাহ পালনকারীদের জন্য পূর্ণাঙ্গ দিকনির্দেশনা ও নীতিমালা। এখানে রয়েছে স্বাস্থ্যবিধি, বিমান ভ্রমণ, লাগেজ পলিসি, মক্কা-মদিনায় মোয়াল্লিম সহায়তা, আইনি সুরক্ষা ও জরুরি সেবার নিয়মাবলী।",
            "Official comprehensive handbook published by the Ministry of Religious Affairs (Bangladesh) and the Ministry of Hajj & Umrah (KSA). Features vital guidelines on pilgrim welfare, immigration, Moallim protocols, travel checklists, health certifications, and emergency helpline services.",
            "• হজের সরকারি পোর্টাল (hajj.gov.bd) ও নুসুক (Nusuk) অ্যাপ নিয়মিত চেক করা\n• ট্রাভেল ডকুমেন্টস, ই-ভিসা ও স্বাস্থ্য সনদ সবসময় ওয়াটারপ্রুফ ব্যাগে রাখা\n• মোয়াল্লিম কার্ড ও হোটেলের পরিচিতিপত্র সার্বক্ষণিক গলায় ঝুলিয়ে রাখা\n• সৌদি আরবে স্থানীয় আইনশৃঙ্খলা ও ট্রাফিক নিয়ম কঠোরভাবে মেনে চলা\n• অসুস্থ হলে বাংলাদেশ হজ মেডিক্যাল মিশন ও সৌদি স্বাস্থ্যকেন্দ্রে যোগাযোগ করা",
            "• Regularly monitor the official Hajj portal and Saudi Nusuk app\n• Keep original passport, electronic visa, and medical vaccination certificates securely\n• Always wear the official pilgrim wristband and Moallim ID card\n• Strictly adhere to group movement schedules announced for Jamarat and Haramain\n• Contact the Bangladesh Hajj Medical Mission in Makkah/Madinah for healthcare needs",
            "• অনুমোদনবিহীন কোনো দালালের আর্থিক প্রলোভনে পা দেওয়া বারণ\n• নির্ধারিত ফ্লাইটের সময়সূচি মিস করা ও দলচ্যুত হয়ে একা ভ্রমণ করা নিষেধ\n• স্থানীয় আইন অমান্য করে ভিক্ষাবৃত্তি বা অননুমোদিত ব্যবসা করা দণ্ডনীয় অপরাধ",
            "• Never deal with unauthorized brokers or fraudulent agencies\n• Do not stray away from your assigned group or miss flight schedules\n• Avoid carrying prohibited items or engaging in unauthorized business",
            "اللَّهُمَّ اجْعَلْهُ حَجًّا مَبْرُورًا وَذَنْبًا مَغْفُورًا وَسَعْيًا مَشْكُورًا",
            "উচ্চারণ: আল্লাহুম্মাজ'আলহু হাজ্জাম মাবরূরা, ওয়া জাম্বাম মাগফূরা, ওয়া সা'ইয়াম মাশকূরা।",
            "Pronunciation: Allahummaj'alhu hajjan mabroora, wa dham-ban maghfoora, wa sa'yan mashkoora.",
            "অর্থ: হে আল্লাহ! এই হজকে কবুল হজ (হজ্জে মাবরুর), সমস্ত পাপের ক্ষমা এবং প্রশংসনীয় প্রচেষ্টায় পরিণত করুন।",
            "Meaning: O Allah! Make this a pilgrimage that is accepted, sins forgiven, and efforts rewarded.",
            "ধর্ম বিষয়ক মন্ত্রণালয় (বাংলাদেশ) ও সহীহ বুখারী: ১৫২১"
        );
    }

    public static List<HajjTopicItem> getGridTopics() {
        List<HajjTopicItem> list = new ArrayList<>();

        // 1. হজ
        list.add(new HajjTopicItem(
            1,
            "হজ",
            "Hajj",
            "ইসলামের ৫ম স্তম্ভ",
            "5th Pillar of Islam",
            R.drawable.ic_hajj_1_hajj,
            "হজ ইসলামের অন্যতম গুরুত্বপূর্ণ মৌলিক স্তম্ভ। শারীরিক ও আর্থিকভাবে সক্ষম প্রত্যেক মুসলিম নর-নারীর ওপর জীবনে অন্তত একবার পবিত্র কা'বা শরীফে গিয়ে হজ পালন করা ফরজ (অবশ্য পালনীয়)। আল্লাহ তাআলা ঘোষণা করেছেন: 'মানুষের মধ্যে যার সেখানে পৌঁছার সামর্থ্য আছে, আল্লাহর উদ্দেশ্যে ওই ঘরের হজ করা তার ওপর ফরজ।' (সূরা আল-ইমরান: ৯৭)",
            "Hajj is the fifth pillar of Islam. It is an obligatory spiritual duty that must be carried out at least once in a lifetime by all adult Muslims who are physically and financially capable of undertaking the journey.",
            "• হজ করার সামর্থ্য হওয়ামাত্র বিলম্ব না করে প্রথম সুযোগেই হজ আদায় করা\n• খাঁটি নিয়তে শুধু আল্লাহর সন্তুষ্টির উদ্দেশ্যে হজের সংকল্প করা\n• হজের সমুদয় ব্যয় হালাল উপার্জন থেকে নির্বাহ করা\n• সফরের পূর্বে ঋণ, দায়দেনা ও বান্দার হক পরিশোধ করে ক্ষমা চেয়ে নেওয়া",
            "• Perform Hajj as soon as capability is reached without undue delay\n• Purify intentions solely for the pleasure of Almighty Allah\n• Ensure all travel funds are strictly halal and lawful\n• Settle all debts and seek forgiveness from family and peers before departure",
            "• হজে গিয়ে দুনিয়াবি নাম-ডাক, খ্যাতি বা 'হাজী সাহেব' উপাধির লোভ করা মারাত্মক পাপ\n• হারাম বা সুদের টাকায় হজ করা সর্বসম্মতভাবে নিষিদ্ধ",
            "• Do not perform Hajj for worldly vanity, show, or societal prestige\n• Never finance pilgrimage through interest or illicit means",
            "لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لاَ شَرِيكَ لَكَ لَبَّيْكَ",
            "উচ্চারণ: লাব্বাইক আল্লাহুম্মা লাব্বাইক, লাব্বাইকা লা শারীকা লাকা লাব্বাইক।",
            "Pronunciation: Labbayk Allahumma Labbayk, Labbayka la shareeka laka Labbayk.",
            "অর্থ: আমি হাজির, হে আল্লাহ আমি উপস্থিত! আমি আপনার দরবারে হাজির, আপনার কোনো অংশীদার নেই, আমি উপস্থিত!",
            "Meaning: Here I am, O Allah, here I am. Here I am, You have no partner, here I am.",
            "সূরা আল-ইমরান: ৯৭, সহীহ বুখারী: ১৫৪৯"
        ));

        // 2. ফজিলত ও তাৎপর্য
        list.add(new HajjTopicItem(
            2,
            "ফজিলত ও তাৎপর্য",
            "Virtues & Significance",
            "মর্যাদা ও সওয়াব",
            "Rewards & Blessings",
            R.drawable.ic_hajj_2_significance,
            "হজের ফজিলত ও তাৎপর্য অপরিসীম। রাসুলুল্লাহ (সা.) ইরশাদ করেছেন: 'এক ওমরাহর পর আরেক ওমরাহ উভয়ের মধ্যবর্তী সময়ের পাপসমূহের কাফফারা স্বরূপ। আর মাবরুর হজের (নিখুঁত ও কবুল হজের) একমাত্র প্রতিদান হলো জান্নাত।' অন্য হাদিসে এসেছে, যে ব্যক্তি কোনো গুনাহ ও অশ্লীলতায় লিপ্ত না হয়ে হজ আদায় করল, সে সদ্যভূমিষ্ঠ নিষ্পাপ শিশুর মতো নিষ্পাপ হয়ে বাড়ি ফিরবে।",
            "The virtues of Hajj are boundless. The Messenger of Allah (peace be upon him) said: 'The performance of Umrah is an expiation for the sins committed between them, and the reward of Hajj Mabrur is nothing except Paradise.' (Sahih al-Bukhari: 1773)",
            "• প্রতিটি রুকন আদায়ের সময় গভীর একাগ্রতা ও কান্নাভেজা হৃদয়ে দোয়া করা\n• সহযাত্রী হাজীদের সর্বতোভাবে সেবা করা ও নম্র ব্যবহার করা\n• দিনরাত হারামাইন শরিফাইনে নফল ইবাদত, কুরআন তিলাওয়াত ও তাওয়াফে মশগুল থাকা",
            "• Pour out your heart in continuous supplication and sincere tears\n• Help elderly and fellow pilgrims with humility, patience, and kindness\n• Maximize Quranic recitation, voluntary Tawaf, and prayers within Haramain",
            "• সফরসঙ্গী কারো সাথে ঝগড়া-বিবাদ বা অধৈর্য আচরণ করা যাবে না\n• কোনো অনর্থক ও অর্থহীন কথা বা কাজে সময় নষ্ট করা অনুচিত",
            "• Refrain from disputes, quarrels, anger, or disrespectful conduct\n• Do not waste precious moments in vain talk or idle amusement",
            "اللَّهُمَّ إِنِّي أَسْأَلُكَ حَجًّا مَبْرُورًا، وَسَعْيًا مَشْكُورًا، وَذَنْبًا مَغْفُورًا",
            "উচ্চারণ: আল্লাহুম্মা ইন্নি আসআলুকা হাজ্জাম মাবরূরা, ওয়া সা'ইয়াম মাশকূরা, ওয়া জাম্বাম মাগফূরা।",
            "Pronunciation: Allahumma inni as'aluka hajjan mabroora, wa sa'yan mashkoora, wa dham-ban maghfoora.",
            "অর্থ: হে আল্লাহ! আমি আপনার কাছে কবুল হজ, প্রশংসনীয় প্রচেষ্টা এবং মার্জিত ক্ষমা প্রার্থনা করছি।",
            "Meaning: O Allah! I ask of You an accepted Hajj, a grateful effort, and forgiven sins.",
            "সহীহ বুখারী: ১৫২১, সহীহ মুসলিম: ১৩৫০"
        ));

        // 3. ইতিহাস
        list.add(new HajjTopicItem(
            3,
            "ইতিহাস",
            "History of Hajj",
            "ঐতিহাসিক পটভূমি",
            "Historical Background",
            R.drawable.ic_hajj_3_history,
            "হজের ইতিহাসের সাথে জড়িয়ে আছে তাওহীদের পিতা হযরত ইবরাহীম (আ.), তাঁর মহীয়সী স্ত্রী বিবি হাজেরা (আ.) এবং পুত্র হযরত ইসমাঈল (আ.)-এর অবিস্মরণীয় ত্যাগ ও কোরবানির ইতিহাস। আল্লাহর নির্দেশে হযরত ইবরাহীম (আ.) ও ইসমাঈল (আ.) কা'বা ঘর পুনর্নির্মাণ করেন এবং বিশ্ববাসীকে হজের জন্য আহ্বান জানান। পরবর্তীতে প্রিয় নবী মুহাম্মদ (সা.) বিদায় হজের মাধ্যমে জাহেলিয়াতের বিকৃতি দূর করে ইবরাহিমী বিশুদ্ধ হজের ধারা পুনঃপ্রতিষ্ঠা করেন।",
            "The monumental history of Hajj revolves around the profound sacrifices of Prophet Ibrahim (AS), his devoted wife Hajar (AS), and his son Ismail (AS). Allah commanded Ibrahim (AS) to proclaim Hajj to all mankind, which was perfected by Prophet Muhammad (PBUH) in the Farewell Pilgrimage.",
            "• হজের প্রতিটি নিদর্শন ও ইতিহাসের আত্মিক শিক্ষাকে অন্তরে ধারণ করা\n• হযরত ইবরাহীম (আ.)-এর ন্যায় পূর্ণ আত্মসমর্পণের মানসিকতা তৈরি করা\n• মাকামে ইবরাহীম ও হিজরে ইসমাঈলে আদবের সাথে নামাজ আদায় করা",
            "• Reflect deeply on the timeless legacy of Prophet Ibrahim (AS)\n• Embody unconditional submission to Allah's divine commandments\n• Offer prayers with profound reverence at Maqam Ibrahim and Hijr Ismail",
            "• কোনো ঐতিহাসিক স্থানকে শরিয়ত বহির্ভূত বিশ্বাস নিয়ে স্পর্শ বা পূজা করা নিষেধ",
            "• Avoid superstitious practices or associating holiness outside Islamic tenets",
            "وَأَذِّن فِي النَّاسِ بِالْحَجِّ يَأْتُوكَ رِجَالًا وَعَلَىٰ كُلِّ ضَامِرٍ",
            "উচ্চারণ: ওয়া আজ্জিন ফিন-নাসি বিল-হাজ্জি ইয়া'তূকা রিজালাও ওয়া আলা কুল্লি দামির।",
            "Pronunciation: Wa adh-dhin fin-nasi bil-hajji ya'tooka rijalan wa 'ala kulli damir.",
            "অর্থ: এবং মানুষের মধ্যে হজের ঘোষণা দাও; তারা তোমার কাছে আসবে পায়ে হেঁটে এবং সর্বপ্রকার শীর্ণকায় উটের পিঠে চড়ে।",
            "Meaning: And proclaim to the people the Hajj; they will come to you on foot and on every lean camel.",
            "সূরা আল-হাজ্জ: ২৭"
        ));

        // 4. ৩ প্রকার হজ
        list.add(new HajjTopicItem(
            4,
            "৩ প্রকার হজ",
            "3 Types of Hajj",
            "হজের প্রকারভেদ",
            "Types of Pilgrimage",
            R.drawable.ic_hajj_4_types,
            "শরীয়তে হজ পালনের ৩টি নির্দিষ্ট পদ্ধতি রয়েছে:\n১. হজে তামাত্তু (Hajj al-Tamattu): হজের মাসে প্রথমে ওমরাহর ইহরাম বেঁধে ওমরাহ শেষ করে হালাল হওয়া, অতঃপর ৮ই জিলহজ পুনরায় হজের ইহরাম বেঁধে হজ সম্পন্ন করা। (বাংলাদেশীদের জন্য এটিই সর্বাধিক প্রচলিত ও সহজ)।\n২. হজে কিরান (Hajj al-Qiran): একই সাথে ওমরাহ ও হজের ইহরাম বেঁধে প্রথমে ওমরাহ করা এবং হালাল না হয়ে একই ইহরামে হজ শেষ করা।\n৩. হজে ইফরাদ (Hajj al-Ifrad): ওমরাহ ব্যতীত শুধুমাত্র হজের জন্য ইহরাম বাঁধা।",
            "There are three sanctioned forms of Hajj:\n1. Hajj al-Tamattu: Performing Umrah first during Hajj months, exiting Ihram, and entering a new Ihram for Hajj on 8th Dhul Hijjah (Recommended for most overseas pilgrims).\n2. Hajj al-Qiran: Entering Ihram for both Umrah and Hajj together without exiting Ihram until Hajj completes.\n3. Hajj al-Ifrad: Entering Ihram solely for Hajj without Umrah.",
            "• সফরের পূর্বে আপনি কোন প্রকারের হজ আদায় করছেন তা সুনির্দিষ্টভাবে নিয়ত করা\n• তামাত্তু ও কিরান হজকারীদের জন্য দমে শোকর (কুরবানি) আদায় করা ওয়াজিব\n• ইফরাদ হজকারীদের জন্য কুরবানি মুস্তাহাব",
            "• Explicitly determine your intended type of Hajj before assuming Ihram\n• Remember that animal sacrifice (Dam of gratitude) is obligatory for Tamattu & Qiran\n• Sacrifice is voluntary/recommended for pilgrims performing Ifrad",
            "• নির্ধারিত হজের প্রকার না জেনে বা দ্বিধাদ্বন্দ্বে নিয়ত করা অনুচিত",
            "• Do not enter Ihram with confused or ambiguous intentions",
            "فَمَن تَمَتَّعَ بِالْعُمْرَةِ إِلَى الْحَجِّ فَمَا اسْتَيْسَرَ مِنَ الْهَدْيِ",
            "উচ্চারণ: ফামান তামাত্তা'আ বিল-উমরাতি ইলাল-হাজ্জি ফামাস-তাইসারা মিনাল-হাদই।",
            "Pronunciation: Faman tamatta'a bil-'umrati ilal-hajji famas-taysara minal-hady.",
            "অর্থ: সুতরাং যে ব্যক্তি হজের সাথে ওমরাহ যুক্ত করে সুবিধা গ্রহণ করল, সে সহজলভ্য কুরবানি করবে।",
            "Meaning: Then whoever performs Umrah followed by Hajj shall offer what sacrifice can be easily obtained.",
            "সূরা আল-বাকারা: ১৯৬"
        ));

        // 5. ফরজ ও ওয়াজিব
        list.add(new HajjTopicItem(
            5,
            "ফরজ ও ওয়াজিব",
            "Fard & Wajib",
            "শরিয়তের বিধান",
            "Essential Pillars & Duties",
            R.drawable.ic_hajj_5_fard_wajib,
            "হজের রুকন বা ফরজ ৩টি (যা কোনো অবস্থাতেই বাদ দেওয়া যায় না, বাদ পড়লে হজ বাতিল হয়ে যাবে):\n১. ইহরাম বাঁধা (নিয়ত ও তালবিয়াহ সহ)\n২. ৯ই জিলহজ দ্বিপ্রহরের পর থেকে সূর্যাস্ত পর্যন্ত আরাফাতে অবস্থান\n৩. তাওয়াফে জিয়ারত (১০ থেকে ১২ই জিলহজের মধ্যে কাবা তাওয়াফ)।\n\nহজের প্রধান ওয়াজিবসমূহ (ভুলবশত বাদ পড়লে দমে কাফফারা দিয়ে শুদ্ধ করতে হয়):\n১. মীকাত থেকে ইহরাম বাঁধা\n২. মুজদালিফায় রাতযাপন\n৩. সাফা-মারওয়ায় সাঈ করা\n৪. জামারাতে পাথর নিক্ষেপ (রমী)\n৫. মাথা মুণ্ডন বা চুল ছোট করা (হলক/কসর)\n৬. বিদায়ী তাওয়াফ।",
            "The 3 Essential Pillars (Fard/Arkan) of Hajj:\n1. Entering Ihram with intention and Talbiyah\n2. Wuquf (standing) in the plains of Arafah on the 9th of Dhul Hijjah\n3. Tawaf al-Ziyarah (Tawaf al-Ifadah).\n\nThe Essential Duties (Wajibat) of Hajj:\n1. Crossing Miqat in Ihram\n2. Staying overnight at Muzdalifah\n3. Sa'i between Safa and Marwah\n4. Stoning the Jamarat\n5. Shaving or trimming hair\n6. Farewell Tawaf (Tawaf al-Wada).",
            "• হজের ৩টি ফরজ ও ওয়াজিবসমূহ মুখস্থ ও সম্পূর্ণ নির্ভুলভাবে পালন করা\n• কোনো ওয়াজিব অসাবধানতাবশত ছুটে গেলে দ্রুত অভিজ্ঞ আলেমের পরামর্শ নিয়ে কাফফারা আদায় করা",
            "• Memorize and meticulously safeguard every Fard and Wajib of Hajj\n• If a Wajib is unintentionally missed, consult scholars immediately to offer proper expiation (Dam)",
            "• কোনো ফরজ বাদ পড়লে তা দম বা অর্থ দিয়ে আদায় করা যায় না; হজ বাতিল হবে",
            "• Missing any essential pillar (Fard) invalidates Hajj completely; it cannot be compensated by sacrifice",
            "الْحَجُّ أَشْهُرٌ مَّعْلُومَاتٌ ۚ فَمَن فَرَضَ فِيهِنَّ الْحَجَّ فَلَا رَفَثَ وَلَا فُسُوقَ",
            "উচ্চারণ: আল-হাজ্জু আশহুরুম মা'লূমাত, ফামান ফারাদা ফীহিন্নাল হাজ্জা ফালা রাফাসা ওয়া লা ফুসূক্ব।",
            "Pronunciation: Al-hajju ash-hurum ma'loomat, faman farada feehinnal-hajja fala rafatha wa la fusooq.",
            "অর্থ: হজের মাসগুলো সুবিদিত। অতএব এ মাসগুলোতে যে হজের নিয়ত করবে, তার জন্য অশ্লীলতা, পাপাচার ও কলহ নিষিদ্ধ।",
            "Meaning: Hajj is during well-known months, so whoever has made Hajj obligatory upon himself therein, there is to be no sexual relations and no disobedience.",
            "সূরা আল-বাকারা: ১৯৭, সহীহ বুখারী: ১৭৭২"
        ));

        // 6. হজের প্রস্তুতি
        list.add(new HajjTopicItem(
            6,
            "হজের প্রস্তুতি",
            "Preparation for Hajj",
            "সফর ও মালামাল চেকলিস্ট",
            "Journey Preparation & Packing",
            R.drawable.ic_hajj_6_preparation,
            "হজে যাওয়ার পূর্বে তিনটি স্তরে পূর্ণ প্রস্তুতি গ্রহণ করা আবশ্যক: আধ্যাত্মিক প্রস্তুতি (আন্তরিক তাওবা, হক আদায়, সহীহ নিয়ত), জ্ঞানগত প্রস্তুতি (হজের কিতাব পড়া ও মাসায়েল জানা) এবং শারীরিক ও মালামাল সংক্রান্ত প্রস্তুতি। সাথে রাখুন: কমপক্ষে ২ সেট সুতি সেলাইবিহীন ইহরাম, বেল্ট, পাসপোর্ট সাইজের ব্যাগ, নিয়মিত ওষুধ, সুগন্ধিমুক্ত সাবান ও পেট্রোলিয়াম জেলি, নরম জুতা ও ছাতা।",
            "Comprehensive Hajj preparation spans three key dimensions: Spiritual (sincere repentance, clearing debts), Knowledge (mastering Hajj rites and Duas), and Physical Packing (Ihram sets, travel bag, prescribed medicines, fragrance-free soap, comfortable sandals, umbrella).",
            "• প্রয়োজনীয় সকল কাগজপত্র ও পাসপোর্ট ফটোকপিসহ গুছিয়ে রাখা\n• নিয়মিত হাঁটার অভ্যাস করা (প্রতিদিন ৩-৫ কি.মি.) যাতে হজের দীর্ঘ পথচলা সহজ হয়\n• টিকা (মেনিনজাইটিস, ফ্লু, কোভিড) যথাসময়ে গ্রহণ করা",
            "• Organize passports, visas, and health documents in a waterproof carrier\n• Build walking stamina by walking 3-5 kilometers daily before departure\n• Complete all mandatory immunizations and health checks well in advance",
            "• অতিরিক্ত ভারী লাগেজ নিয়ে যাওয়া এড়িয়ে চলুন\n• ধার-দেনা না মিটিয়ে বা পরিবারের ভরণপোষণের ব্যবস্থা না করে সফরে বের হওয়া নিষিদ্ধ",
            "• Avoid overpacking heavy and non-essential belongings\n• Never travel without settling debts and providing for dependents",
            "وَتَزَوَّدُوا فَإِنَّ خَيْرَ الزَّادِ التَّقْوَىٰ",
            "উচ্চারণ: ওয়া তাযাওয়্যাদূ ফা-ইন্না খাইরায যা-দিত তাক্বওয়া।",
            "Pronunciation: Wa tazawwadoo fa-inna khayraz-zadit-taqwa.",
            "অর্থ: এবং তোমরা পাথেয় সংগ্রহ কর; নিশ্চয়ই সর্বোত্তম পাথেয় হলো তাকওয়া (আল্লাহভীতি)।",
            "Meaning: And take provisions, but indeed, the best provision is righteousness (Taqwa).",
            "সূরা আল-বাকারা: ১৯৭"
        ));

        // 7. পবিত্র স্থান সমূহ
        list.add(new HajjTopicItem(
            7,
            "পবিত্র স্থান সমূহ",
            "Sacred Places",
            "মক্কা ও হারাম শরীফ",
            "Holy Sites & Sanctuaries",
            R.drawable.ic_hajj_7_holy_places,
            "আল্লাহর সর্বশ্রেষ্ঠ ও সম্মানিত ভৌগোলিক নিদর্শনসমূহ মক্কা মুকাররমা ও মদিনা মুনাওয়ারায় অবস্থিত। এর মধ্যে প্রধান: কা'বা শরীফ (বাইতুল্লাহ), হাতিম ও হিজরে ইসমাঈল, মাকামে ইবরাহীম, জমজম কূপ, সাফা ও মারওয়া পাহাড়, মিনা উপত্যকা, আরাফাতের ময়দান (জাবালে রহমত), মুজদালিফা এবং মদিনার মসজিদে নববী ও রওজাতুম মিন রিয়াদিল জান্নাহ।",
            "The holiest sanctuaries of Islam include the Holy Kaaba (Baytullah), Hijr Ismail (Hateem), Maqam Ibrahim, the Well of Zamzam, Safa and Marwah, Mina Valley, the Plain of Arafah, Muzdalifah, and the Prophet's Mosque in Madinah.",
            "• পবিত্র স্থানসমূহের প্রতিটি ধূলিকণা ও সীমানাকে সর্বোচ্চ সম্মান প্রদর্শন করা\n• হাতিমের ভেতর নামাজ পড়লে তা কা'বা শরীফের ভেতরে নামাজ পড়ার সমতুল্য সওয়াব\n• জমজমের পানি দাঁড়িয়ে কেবলামুখী হয়ে তৃপ্তিসহ পান করা ও দোয়া করা",
            "• Uphold the supreme sanctity of Haramain with utmost dignity and reverence\n• Praying inside Hateem carries the exact reward of praying inside the Holy Kaaba\n• Drink Zamzam water standing, facing the Qiblah, with sincere supplications",
            "• পবিত্র হরমে কোনো গাছ বা ঘাস কাটা এবং প্রাণী শিকার করা কঠোরভাবে হারাম",
            "• It is strictly forbidden to hunt game or cut vegetation within the sacred Haram boundary",
            "إِنَّ أَوَّلَ بَيْتٍ وُضِعَ لِلنَّاسِ لَلَّذِي بِبَكَّةَ مُبَارَكًا",
            "উচ্চারণ: ইন্না আউয়ালা বাইতিন উদি'আ লিন্নাসি লাল্লাযী বিবাক্কাতা মুবারাকাও।",
            "Pronunciation: Inna awwala baytin wudi'a lin-nasi lalladhee bibakkata mubarakan.",
            "অর্থ: নিশ্চয় মানবজাতির জন্য সর্বপ্রথম যে ঘরটি প্রতিষ্ঠিত হয়েছিল, তা তো বাক্কায় (মক্কায়), যা বরকতময় ও সমগ্র বিশ্ববাসীর জন্য দিশারি।",
            "Meaning: Indeed, the first House established for mankind was that at Makkah - blessed and a guidance for the worlds.",
            "সূরা আল-ইমরান: ৯৬"
        ));

        // 8. হজ দোয়া
        list.add(new HajjTopicItem(
            8,
            "হজ দোয়া",
            "Hajj Duas",
            "দোয়া ও মোনাজাত",
            "Essential Supplications",
            R.drawable.ic_hajj_8_dua,
            "হজের পুরো সফরটিই মহান রবের কাছে ক্ষমা ও রহমত কামনার পরম সুযোগ। তালবিয়াহ, তাওয়াফের সাত চক্করের বিভিন্ন মাসনূন দোয়া, রুকনে ইয়ামানী ও হাজরে আসওয়াদের মাঝে 'রাব্বানা আতিনা ফিদ দুনয়া...', সাফা-মারওয়ায় সাঈর দোয়া এবং বিশেষ করে আরাফাতের ময়দানের দোয়া সর্বোত্তম দোয়া হিসেবে বর্ণিত হয়েছে।",
            "Hajj is a continuous journey of intimate supplication and remembrance. Authentic prayers include the universal Talbiyah, Tawaf Duas, the famous verse between Rukn Yamani and Hajr Aswad, Sa'i invocations, and the pinnacle supplications on Arafah.",
            "• নিজের, পিতা-মাতা, পরিবার, সন্তান ও বিশ্ব মুসলিম উম্মাহর ক্ষমার জন্য দোয়া করা\n• হাজরে আসওয়াদ ও রুকনে ইয়ামানীর মাঝে কোরআনের বিখ্যাত দোয়া পাঠ করা\n• আরাফাতের ময়দানে জোহর থেকে মাগরিব পর্যন্ত কান্নাভেজা মোনাজাত অব্যাহত রাখা",
            "• Pray earnestly for your parents, family, righteous offspring, and the global Ummah\n• Recite 'Rabbana atina fid-dunya...' between Rukn Yamani and the Black Stone\n• Dedicate the afternoon of Arafah entirely to continuous crying supplications",
            "• দোয়ার সময় অন্যের সাথে ধাক্কাধাক্কি বা উচ্চস্বরে চিৎকার করে অন্যদের ব্যাঘাত ঘটানো নিষিদ্ধ",
            "• Never push, shove, or disturb fellow worshippers during supplication",
            "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            "উচ্চারণ: রাব্বানা আতিনা ফিদ-দুনয়া হাসানাতাও ওয়া ফিল-আখিরাতি হাসানাতাও ওয়া ক্বিনা আযাবান-নার।",
            "Pronunciation: Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.",
            "অর্থ: হে আমাদের প্রতিপালক! আমাদের ইহকালে কল্যাণ দান করুন এবং পরকালেও কল্যাণ দান করুন এবং আমাদেরকে জাহান্নামের আগুন থেকে রক্ষা করুন।",
            "Meaning: Our Lord, give us in this world that which is good and in the Hereafter that which is good and protect us from the punishment of the Fire.",
            "সূরা আল-বাকারা: ২০১, সহীহ মুসলিম: ১২১৮"
        ));

        // 9. কবুলের আলামত
        list.add(new HajjTopicItem(
            9,
            "কবুলের আলামত",
            "Signs of Acceptance",
            "মাবরুর হজের লক্ষণ",
            "Signs of an Accepted Hajj",
            R.drawable.ic_hajj_9_acceptance,
            "হজ কবুল হলো কি না তা বোঝার জন্য বুজুর্গ ও ওলামায়ে কেরাম কিছু মৌলিক আলামতের কথা বলেছেন। এর প্রধান লক্ষণ হলো: হজের পর হাজী সাহেবের জীবনে এক আমূল ইতিবাচক পরিবর্তন আসা। পূর্বে যে পাপাচারে লিপ্ত ছিল তা স্থায়ীভাবে বর্জন করা, নামাজের ব্যাপারে একনিষ্ঠ হওয়া, হালাল উপার্জনে চলা এবং দুনিয়ার মোহের চেয়ে আখিরাতের প্রতি আগ্রহ ও ভালোবাসা বহুগুণ বৃদ্ধি পাওয়া।",
            "Scholars emphasize that the greatest manifestation of an accepted Hajj (Hajj Mabrur) is a profound spiritual transformation upon returning home. The pilgrim abandons past sins, guards prayers faithfully, earns strictly halal, and seeks the Hereafter over transient worldly vanities.",
            "• হজ শেষে স্বদেশে ফিরে নম্রতা বজায় রাখা এবং অতীত পাপ থেকে দূরে থাকা\n• জামাতে ৫ ওয়াক্ত নামাজ আদায় ও কুরআন তিলাওয়াত নিয়মিত করা\n• মানুষের সাথে সদ্ব্যবহার ও দান-সদকার পরিমাণ বৃদ্ধি করা",
            "• Maintain humility, modesty, and righteous steadfastness upon returning home\n• Guard all five daily prayers in congregation and establish regular Quran reading\n• Increase charity, volunteerism, and compassionate interactions with society",
            "• হজ সম্পন্ন করে দাম্ভিকতা প্রকাশ করা বা মানুষকে তুচ্ছজ্ঞান করা বর্জনীয়",
            "• Avoid boasting about Hajj, vanity, arrogance, or looking down upon others",
            "الْحَجُّ الْمَبْرُورُ لَيْسَ لَهُ جَزَاءٌ إِلَّا الْجَنَّةُ",
            "উচ্চারণ: আল-হাজ্জুল মাবরূরু লাইসা লাহু জাযা-উন ইল্লাল জান্নাহ।",
            "Pronunciation: Al-hajjul mabrooru laysa lahoo jaza'un illal-jannah.",
            "অর্থ: মাবরুর হজের (কবুল হজের) একমাত্র প্রতিদান হলো জান্নাত।",
            "Meaning: An accepted Hajj has no reward other than Paradise.",
            "সহীহ বুখারী: ১৭৭৩, সহীহ মুসলিম: ১৩৪৯"
        ));

        // 10. ইহরাম
        list.add(new HajjTopicItem(
            10,
            "ইহরাম",
            "Ihram",
            "হজের ১ম ফরজ",
            "Sacred State of Ihram",
            R.drawable.ic_hajj_10_ihram,
            "ইহরাম হলো হজ বা ওমরাহর প্রথম ফরজ। এর আভিধানিক অর্থ কোনো কিছু নিজের ওপর হারাম বা নিষিদ্ধ করে নেওয়া। হজের নিয়ত ও তালবিয়াহ পাঠের মাধ্যমে বান্দা ইহরামে প্রবেশ করে। পুরুষদের জন্য সেলাইবিহীন দুটি সাদা সুতি চাদর (একটি তহবন্দ, অন্যটি গায়ে জড়ানো) এবং নারীদের জন্য সতর আবৃত সাধারণ শালীন পোশাক পরিধান করতে হয়।",
            "Ihram is the sacred state entered into for performing Hajj or Umrah. For men, it entails wearing two seamless white garments (an Izar around waist and Rida over shoulders) and sandals. For women, it is modest, loose-fitting attire that covers the entire body except face and hands.",
            "• মীকাত অতিক্রমের পূর্বে গোসল করে পরিচ্ছন্ন হওয়া\n• ইহরামের কাপড়ে কোনো সুগন্ধি না লাগিয়ে গায়ে সুগন্ধি লাগানো (কাপড় পরার পূর্বে)\n• নিয়ত করা এবং উচ্চৈঃস্বরে তালবিয়াহ পাঠ করা",
            "• Perform full ritual ghusl (bath) and personal grooming before crossing Miqat\n• Apply perfume to the body before wearing garments (not onto the cloth itself)\n• Sincerly make intention and recite the Talbiyah aloud (men) or softly (women)",
            "• ইহরাম অবস্থায় চুল, নখ কাটা সম্পূর্ণ নিষিদ্ধ\n• কোনো সুগন্ধি, আতর বা সুগন্ধিযুক্ত সাবান ব্যবহার নিষেধ\n• পুরুষদের মাথা বা কান ঢাকা এবং নারীদের জন্য নিকাব বা হাতমোজা পরা নিষেধ\n• শিকার করা, ঝগড়া বা যৌন মিলন কঠোরভাবে নিষিদ্ধ",
            "• Never cut, trim, or pluck hair or nails while in Ihram\n• Do not apply perfumes, scented oils, or scented soaps\n• Men must not cover heads or ears; women must not wear Niqab veil or gloves\n• Avoid hunting, arguing, cursing, and intimate physical relations",
            "لَبَّيْكَ اللَّهُمَّ حَجًّا",
            "উচ্চারণ: লাব্বাইক আল্লাহুম্মা হাজ্জা।",
            "Pronunciation: Labbayk Allahumma Hajjan.",
            "অর্থ: হে আল্লাহ! হজের জন্য আপনার দরবারে হাজির।",
            "Meaning: Here I am, O Allah, to perform Hajj.",
            "সহীহ মুসলিম: ১২১১, সহীহ বুখারী: ১৫৪৪"
        ));

        // 11. আরাফাত
        list.add(new HajjTopicItem(
            11,
            "আরাফাত",
            "Day of Arafah",
            "হজের প্রধান রুকন",
            "The Pinnacle Pillar of Hajj",
            R.drawable.ic_hajj_11_arafat,
            "৯ই জিলহজ আরাফাতের ময়দানে অবস্থান করাই হলো হজের মূল ভিত্তি। রাসুলুল্লাহ (সা.) দ্ব্যর্থহীন কণ্ঠে ঘোষণা করেছেন: 'আল-হাজ্জু আরাফাহ'—অর্থাৎ আরাফাতই হলো হজ। এ ময়দানে উপস্থিত না হলে কোনোভাবেই হজ হবে না। জোহরের ওয়াক্ত থেকে সূর্যাস্ত পর্যন্ত খুশু-খুজুর সাথে মোনাজাত, তাওবা ও ইবাদতে মশগুল থাকা অপরিহার্য।",
            "The Day of Arafah (9th Dhul Hijjah) is the supreme essence of Hajj. The Prophet (PBUH) declared: 'Hajj is Arafah.' Pilgrims gather on the plains from midday until sunset, dedicating hours to earnest supplications, weeping for forgiveness, and glorifying Allah.",
            "• ৯ই জিলহজ দ্বিপ্রহরের পূর্বেই আরাফাতের ময়দানে পৌঁছানো\n• মসজিদে নামিরায় বা নিজ নিজ তাঁবুতে জোহর ও আসর নামাজ একত্রে এক আজান ও দুই ইকামতে কসর করে আদায় করা\n• সূর্যাস্ত পর্যন্ত দাঁড়িয়ে বা বসে ক্বিবলামুখী হয়ে দোয়ায় রত থাকা",
            "• Arrive at Arafah before noon on the 9th of Dhul Hijjah\n• Combine and shorten Dhuhr and Asr prayers at Dhuhr time with one Adhan and two Iqamahs\n• Stand or sit facing Qiblah in continuous Duas, repentance, and Dhikr until sunset",
            "• কোনো অবস্থাতেই সূর্যাস্তের পূর্বে আরাফাতের সীমানা ত্যাগ করা যাবে না\n• জাবালে রহমতে ওঠার জন্য অতিরিক্ত ঝুঁকি বা হুড়োহুড়ি করা অনুচিত",
            "• Leaving Arafah before total sunset is strictly prohibited and warrants Dam\n• Do not exhaust yourself pushing to climb Mount Rahmah; the entire plain is blessed",
            "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            "উচ্চারণ: লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, লাহুল মুলকু ওয়া লাহুল হামদু ওয়া হুওয়া আলা কুল্লি শাইয়িন ক্বাদীর।",
            "Pronunciation: La ilaha illallahu wahdahu la shareeka lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli shay'in qadeer.",
            "অর্থ: একমাত্র আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই, তাঁর কোনো অংশীদার নেই; রাজত্ব একমাত্র তাঁরই, সমস্ত প্রশংসা তাঁরই এবং তিনি সবকিছুর ওপর ক্ষমতাবান।",
            "Meaning: There is no deity worthy of worship except Allah alone, with no partner; to Him belongs dominion and praise, and He is over all things capable.",
            "জামে তিরমিযী: ৩৫৮৫, সহীহ বুখারী: ১৬৬৩"
        ));

        // 12. মিনা
        list.add(new HajjTopicItem(
            12,
            "মিনা",
            "Mina",
            "মিনায় অবস্থান ও রমী",
            "Encampment & Stoning",
            R.drawable.ic_hajj_12_mina,
            "মিনা হলো পবিত্র তাঁবুর শহর। হজের মূল আনুষ্ঠানিকতা শুরু হয় ৮ই জিলহজ মিনায় গমনের মাধ্যমে। এখানে জোহর, আসর, মাগরিব, এশা এবং ৯ই জিলহজের ফজর—মোট ৫ ওয়াক্ত নামাজ আদায় করা সুন্নাত। পরবর্তীতে ১০, ১১ ও ১২ই জিলহজ মিনায় রাত্রিযাপন এবং শয়তানের প্রতীকী স্তম্ভে (জামারাত) পাথর নিক্ষেপ করা হজের অন্যতম ওয়াজিব দায়িত্ব।",
            "Mina is the historic valley of tents where pilgrims reside from the 8th of Dhul Hijjah, praying Dhuhr, Asr, Maghrib, Isha, and Fajr of the 9th. Pilgrims return to Mina on the 10th, 11th, and 12th for overnight stays and stoning of the Jamarat pillars.",
            "• ৮ই জিলহজ সকাল সকাল মিনায় পৌঁছে তাঁবুতে অবস্থান করা\n• চার রাকাত বিশিষ্ট ফরজ নামাজগুলো কসর (২ রাকাত) করে যথাসময়ে আদায় করা\n• ১০, ১১ ও ১২ই জিলহজ জামারাতে পাথর নিক্ষেপের নির্ধারিত শিডিউল ও শান্ত শৃঙ্খলা মেনে চলা",
            "• Depart for Mina early on the 8th of Dhul Hijjah and settle in your tent\n• Shorten four-rakah prayers to two rakahs at their respective times\n• Stone the Jamarat according to group assigned schedules with calm patience",
            "• পাথর নিক্ষেপের সময় জুতা, স্যান্ডেল বা বড় পাথর নিক্ষেপ করা চরম অজ্ঞতা ও নিষেধ\n• অতিরিক্ত ভিড়ে ধাক্কাধাক্কি করা থেকে বিরত থাকুন",
            "• Never throw shoes, umbrellas, or large rocks at the pillars\n• Avoid aggressive rushing or pushing in heavy crowds around Jamarat",
            "اللَّهُ أَكْبَرُ، رَغْمًا لِلشَّيْطَانِ وَرِضًا لِلرَّحْمَنِ",
            "উচ্চারণ: আল্লাহু আকবার, রাগমান লিশ-শাইতানি ওয়া রিদান লির-রাহমান।",
            "Pronunciation: Allahu Akbar, raghman lish-shaytani wa ridan lir-Rahman.",
            "অর্থ: আল্লাহ সর্বশ্রেষ্ঠ! শয়তানকে অপদস্থ করার জন্য এবং পরম করুণাময় আল্লাহর সন্তুষ্টির উদ্দেশ্যে।",
            "Meaning: Allah is the Greatest, in defiance of Satan and for the pleasure of the Most Merciful.",
            "সহীহ মুসলিম: ১২৯৯, সহীহ বুখারী: ১৭৫১"
        ));

        // 13. মুজদালিফা
        list.add(new HajjTopicItem(
            13,
            "মুজদালিফা",
            "Muzdalifah",
            "রাত্রিযাপন ও কঙ্কর সংগ্রহ",
            "Night Stay & Pebble Collection",
            R.drawable.ic_hajj_13_muzdalifah,
            "৯ই জিলহজ সূর্যাস্তের পর হাজীরা মাগরিবের নামাজ না পড়ে আরাফাত থেকে মুজদালিফার উদ্দেশ্যে রওনা হন। মুজদালিফায় পৌঁছে এক আজান ও দুই ইকামতে মাগরিব ও এশার নামাজ একত্রে আদায় করা ওয়াজিব। খোলা আকাশের নিচে পাথর বিছানো মাটিতে রাত যাপন করা সুন্নাতে মুয়াক্কাদা। এখান থেকেই জামারাতে নিক্ষেপের জন্য ছোট ছোট কঙ্কর (ছোলা বুটের আকৃতির) সংগ্রহ করতে হয়।",
            "On the evening of 9th Dhul Hijjah after sunset, pilgrims proceed to Muzdalifah without praying Maghrib at Arafah. At Muzdalifah, Maghrib and Isha are joined together. Sleeping under the open starry sky and collecting small pebbles for Jamarat is an inspiring Sunnah.",
            "• মুজদালিফায় পৌঁছেই মাগরিব ও এশা একসাথে জমা ও কসর করে আদায় করা\n• খোলা আকাশের নিচে বিশ্রাম নিয়ে ফজরের নামাজ পর্যন্ত অবস্থান করা\n• জামারাতের জন্য কমপক্ষে ৪৯টি (অথবা ৭০টি) ছোট ছোট কঙ্কর সংগ্রহ ও ধুয়ে রাখা",
            "• Combine Maghrib and shortened Isha immediately upon arrival at Muzdalifah\n• Rest under the open sky and spend the remaining night in peace and Dhikr\n• Collect 49 (or 70) small pea-sized pebbles for stoning the Jamarat",
            "• আরাফাত থেকে মুজদালিফায় আসার পথে বা আরাফাতে মাগরিব নামাজ পড়া নিষিদ্ধ",
            "• Do not offer Maghrib prayer while still at Arafah or on the road before Muzdalifah",
            "فَإِذَا أَفَضْتُم مِّنْ عَرَفَاتٍ فَاذْكُرُوا اللَّهَ عِندَ الْمَشْعَرِ الْحَرَامِ",
            "উচ্চারণ: ফাইজা আফাদতুম মিন আরাফাতিন ফাযকুরুল্লাহা ইনদাল মাশ'আরিল হারাম।",
            "Pronunciation: Fa-idha afadtum min 'Arafatin fadh-kurullaha 'indal-mash'aril-haram.",
            "অর্থ: অতএব যখন তোমরা আরাফাত থেকে প্রত্যাবর্তন করবে, তখন মাশআরে হারামের (মুজদালিফার) কাছে আল্লাহকে স্মরণ কর।",
            "Meaning: But when you depart from Arafat, remember Allah at the sacred landmark (al-Mash'ar al-Haram).",
            "সূরা আল-বাকারা: ১৯৮, সহীহ মুসলিম: ১২১৮"
        ));

        // 14. সাঈ
        list.add(new HajjTopicItem(
            14,
            "সাঈ",
            "Sa'i",
            "সাফা ও মারওয়ায় সাঈ",
            "Walking Between Safa & Marwah",
            R.drawable.ic_hajj_14_saee,
            "সাঈ হলো সাফা ও মারওয়া পাহাড়দ্বয়ের মাঝে ৭ চক্কর দৌড়ানো বা দ্রুত হাঁটা। এটি মা হাজেরা (আ.)-এর তৃষ্ণার্ত শিশু ইসমাঈল (আ.)-এর জন্য পানির খোঁজে ব্যাকুল দৌড়ানোর চিরস্মরণীয় স্মৃতি। সাফা থেকে শুরু করে মারওয়ায় গেলে ১ চক্কর এবং মারওয়া থেকে সাফায় এলে ২ চক্কর—এভাবে মোট ৭ চক্কর দিয়ে সাঈ সম্পন্ন করতে হয়। পুরুষরা দুই সবুজ বাতির মধ্যবর্তী স্থানে কিছুটা দ্রুতগতিতে দৌড়াবেন।",
            "Sa'i consists of walking seven laps back and forth between the hills of Safa and Marwah, commemorating Lady Hajar's desperate search for water for infant Ismail. Walking from Safa to Marwah is lap 1, and back to Safa is lap 2, concluding lap 7 at Marwah.",
            "• সাফা পাহাড়ে উঠে ক্বিবলামুখী হয়ে হাত তুলে তাকবীর, তাহলীল ও দোয়া করা\n• সবুজ বাতি চিহ্নিত অংশে পুরুষদের দ্রুত কদমে দৌড়ানো (হারওয়ালাহ)\n• পুরো সাঈতে অনর্গল তাসবীহ, তাহলীল ও কোরআনের আয়াত পাঠ করা",
            "• Ascend Safa hill, face the Kaaba, raise hands, and glorify Allah with Takbir & Tahlil\n• Men briskly jog between the two designated green light markers\n• Continually recite Duas and Quranic verses throughout all seven laps",
            "• মারওয়া থেকে শুরু করে সাফায় শেষ করা যাবে না (সাঈ অবশ্যই সাফা থেকে শুরু করতে হবে)",
            "• Never start Sa'i from Marwah; it must always strictly begin from Safa",
            "إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ",
            "উচ্চারণ: ইন্নাস সাফা ওয়াল মারওয়াতা মিন শা'আ-ইরি ল্লাহ।",
            "Pronunciation: Innas-Safa wal-Marwata min sha'a'irillah.",
            "অর্থ: নিশ্চয় সাফা ও মারওয়া আল্লাহর নিদর্শনসমূহের অন্তর্ভুক্ত।",
            "Meaning: Indeed, as-Safa and al-Marwah are among the symbols of Allah.",
            "সূরা আল-বাকারা: ১৫৮, সহীহ মুসলিম: ১২১৮"
        ));

        // 15. তাওয়াফ
        list.add(new HajjTopicItem(
            15,
            "তাওয়াফ",
            "Tawaf",
            "কাবা শরীফ প্রদক্ষিণ",
            "Circumambulation of the Kaaba",
            R.drawable.ic_hajj_15_tawaf,
            "কা'বা শরীফকে নিজের বাম পাশে রেখে হাজরে আসওয়াদের সমান্তরাল থেকে শুরু করে ঘড়ির কাঁটার বিপরীত দিকে ৭ বার প্রদক্ষিণ করাকে তাওয়াফ বলে। তাওয়াফ শুরু ও শেষ হয় হাজরে আসওয়াদে ইস্তিলাম (চুম্বন বা হাত দিয়ে ইশারা) করার মাধ্যমে। তাওয়াফের প্রথম ৩ চক্করে পুরুষদের জন্য 'রমল' (বুক ফুলিয়ে দ্রুত হাঁটা) এবং ওমরাহ বা কুদুম তাওয়াফে 'ইজতিবা' (ডান কাঁধ খোলা রাখা) সুন্নাত। তাওয়াফ শেষে মাকামে ইবরাহীমের পেছনে ২ রাকাত সালাত আদায় করতে হয়।",
            "Tawaf is the sacred circumambulation of the Holy Kaaba seven times counter-clockwise, keeping the Kaaba to the left, starting and ending at the Black Stone (Hajar al-Aswad). Following the seven circuits, pray two rakahs behind Maqam Ibrahim.",
            "• তাওয়াফের পূর্বে অবশ্যই পূর্ণ অজু অবস্থায় পবিত্র থাকা ফরজ\n• প্রতি চক্করের শুরুতে হাজরে আসওয়াদের দিকে ফিরে 'বিসমিল্লাহি আল্লাহু আকবার' বলে ইস্তিলাম করা\n• রুকনে ইয়ামানী ও হাজরে আসওয়াদের মাঝে 'রাব্বানা আতিনা...' পাঠ করা\n• তাওয়াফ শেষ করে তৃপ্তিসহ জমজমের পানি পান করা",
            "• Maintain complete ritual purification (Wudu) throughout Tawaf\n• Make Istilam (pointing hands saying Bismillahi Allahu Akbar) at each lap\n• Recite the Sunnah Dua between Rukn Yamani and the Black Stone\n• Drink blessed Zamzam water following the two rakahs of Tawaf prayer",
            "• তাওয়াফের সময় কাবার দিকে বুক ফিরিয়ে হাঁটা নিষেধ\n• হাতিম বা হিজরে ইসমাঈলের ভেতর দিয়ে তাওয়াফ করলে সেই চক্কর বাতিল হবে (হাতিমের বাইরে দিয়ে ঘুরতে হবে)",
            "• Never face the Kaaba directly while walking; keep it to your left side\n• Do not enter inside the Hateem wall during Tawaf, as it forms part of the Kaaba itself",
            "وَلْيَطَّوَّفُوا بِالْبَيْتِ الْعَتِيقِ",
            "উচ্চারণ: ওয়াল-ইয়াত-তাওয়াফূ বিল-বাইতিল আতীক্ব।",
            "Pronunciation: Wal-yattawwafoo bil-Baytil-'Ateeq.",
            "অর্থ: এবং তারা যেন এই প্রাচীন ঘরের (কা'বা শরীফের) তাওয়াফ সম্পন্ন করে।",
            "Meaning: And circumambulate the Ancient House.",
            "সূরা আল-হাজ্জ: ২৯, সহীহ বুখারী: ১৬০৩"
        ));

        // 16. মীকাত
        list.add(new HajjTopicItem(
            16,
            "মীকাত",
            "Miqat Points",
            "ইহরামের ভৌগোলিক সীমানা",
            "Geographical Boundaries",
            R.drawable.ic_hajj_16_miqat,
            "মীকাত হলো হজ বা ওমরাহ পালনের উদ্দেশ্যে মক্কায় প্রবেশের জন্য মহানবী (সা.) কর্তৃক নির্ধারিত ভৌগোলিক সীমানা। মীকাত অতিক্রমের পূর্বে ইহরাম বাঁধা ওয়াজিব। ৫টি প্রধান মীকাত হলো:\n১. জুল হুলাইফা (মদিনাবাসীদের জন্য)\n২. আল-জুহফাহ (সিরিয়া ও মিসরের জন্য)\n৩. কারনুল মানাযিল (নজদ ও তায়েফবাসীদের জন্য)\n৪. ইয়ালামলাম (ইয়েমেন ও বাংলাদেশ/ভারত/পাকিস্তানের বিমানযাত্রীদের জন্য)\n৫. জাতু ইরক (ইরাকবাসীদের জন্য)।",
            "Miqat denotes the designated boundary stations outside Makkah set by Prophet Muhammad (PBUH) which pilgrims cannot cross without entering the state of Ihram. The 5 primary Miqat points are Dhul Hulayfah, Al-Juhfah, Qarn al-Manazil, Yalamlam, and Dhat 'Irq.",
            "• বাংলাদেশ থেকে বিমানে জেদ্দা যাওয়ার সময় পাইলটের ঘোষণার আগেই ফ্লাইটে ইহরাম পরা ও প্রস্তুত থাকা\n• মীকাত বরাবর পৌঁছামাত্রই নিয়ত করে তালবিয়াহ পাঠ শুরু করা",
            "• Wear Ihram garments at home or departure airport before boarding flight\n• Make verbal intention and begin Talbiyah when the captain announces approaching Miqat",
            "• ইহরাম ছাড়া মীকাত অতিক্রম করা গুরুতর গুনাহ; এরূপ হলে কাফফারা স্বরূপ একটি বকরি কোরবানি (দম) দেওয়া ওয়াজিব হবে",
            "• Crossing the Miqat boundary without Ihram is a grave violation requiring a sacrificial Dam",
            "هُنَّ لَهُنَّ وَلِمَنْ أَتَى عَلَيْهِنَّ مِنْ غَيْرِهِنَّ مِمَّنْ أَرَادَ الْحَجَّ وَالْعُمْرَةَ",
            "উচ্চারণ: হুন্না লাহুননা ওয়া লিমান আতা আলাইহিন্না মিন গাইরিহিন্না মিম্মান আরাদাল হাজ্জা ওয়াল উমরাহ।",
            "Pronunciation: Hunna lahunna wa liman ata 'alayhinna min ghayrihinna mimman aradal-hajja wal-'umrah.",
            "অর্থ: এই স্থানগুলো সেখানকার অধিবাসীদের জন্য এবং অন্য অঞ্চলের যারা হজ ও ওমরাহর উদ্দেশ্যে এসব স্থান দিয়ে অতিক্রম করবে তাদের জন্য মীকাত।",
            "Meaning: These boundaries are for their inhabitants and for whoever comes across them with intention for Hajj or Umrah.",
            "সহীহ বুখারী: ১৫২৪, সহীহ মুসলিম: ১১৮১"
        ));

        // 17. সাফা মারওয়া
        list.add(new HajjTopicItem(
            17,
            "সাফা মারওয়া",
            "Safa & Marwah",
            "পবিত্র পাহাড়দ্বয়",
            "The Sacred Hills",
            R.drawable.ic_hajj_17_safa_marwa,
            "সাফা ও মারওয়া হলো মসজিদুল হারামের পূর্ব পাশে অবস্থিত দুটি ঐতিহাসিক পবিত্র পাহাড়। পবিত্র কুরআনে একে 'শাআইরিল্লাহ' বা আল্লাহর বিশেষ নিদর্শন বলে উল্লেখ করা হয়েছে। হযরত ইবরাহীম (আ.) আল্লাহর হুকুমে বিবি হাজেরা ও শিশু ইসমাঈলকে যখন জনমানবহীন মরুভূমিতে রেখে যান, তখন পানির তৃষ্ণায় কাতর সন্তানের জীবন বাঁচাতে মা হাজেরা এই দুই পাহাড়ের মাঝে সাতবার ব্যাকুল হয়ে ছুটেছিলেন।",
            "Safa and Marwah are two revered hills adjacent to the Holy Kaaba. Designated in the Quran as Divine Symbols of Allah, they preserve the profound legacy of Mother Hajar's maternal devotion and unwavering faith in Allah's providence.",
            "• সাফা পাহাড়ে উঠে কাবার দিকে ফিরে তিনবার তাকবীর বলা ও হাত তুলে দীর্ঘ দোয়া করা\n• মারওয়া পাহাড়ে পৌঁছেও একইভাবে ক্বিবলামুখী হয়ে দোয়া করা",
            "• Ascend Safa hill, look towards the Kaaba, recite Takbir thrice, and supplicate\n• Conclude every crossing at Marwah with sincere praise and personal prayers",
            "• সাঈর সময় অহেতুক আড্ডা বা সেলফি তুলে এবাদতের ভাবগাম্ভীর্য নষ্ট করা অনুচিত",
            "• Avoid idle chatter or casual photography during Sa'i",
            "إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ ۖ فَمَنْ حَجَّ الْبَيْتَ أَوِ اعْتَمَرَ فَلَا جُنَاحَ عَلَيْهِ أَن يَطَّوَّفَ بِهِمَا",
            "উচ্চারণ: ইন্নাস সাফা ওয়াল মারওয়াতা মিন শা'আ-ইরি ল্লাহ, ফামান হাজ্জাল বাইতা আউই'তামারা ফালা জুনা-হা আলাইহি আই-ইয়াত্তাওওয়াফা বিহিমা।",
            "Pronunciation: Innas-Safa wal-Marwata min sha'a'irillah, faman hajjal-bayta awi'tamara fala junaha 'alayhi an yattawwafa bihima.",
            "অর্থ: নিশ্চয় সাফা ও মারওয়া আল্লাহর নিদর্শনসমূহের অন্যতম। সুতরাং যে ব্যক্তি কাবা ঘরে হজ বা ওমরাহ পালন করে, এ দুটিতে সাঈ করায় তার কোনো দোষ নেই।",
            "Meaning: Indeed, as-Safa and al-Marwah are among the symbols of Allah. So whoever makes Hajj to the House or performs Umrah - there is no blame upon him for walking between them.",
            "সূরা আল-বাকারা: ১৫৮"
        ));

        // 18. জিলহজ
        list.add(new HajjTopicItem(
            18,
            "জিলহজ",
            "Dhul Hijjah",
            "হজের পবিত্র মাস",
            "Sacred Month of Pilgrimage",
            R.drawable.ic_hajj_18_dhul_hijjah,
            "জিলহজ হলো হিজরি বর্ষপঞ্জিকার ১২তম ও শেষ মাস এবং চারটি সম্মানিত ও নিষিদ্ধ মাসের (আশহুরে হুরুম) একটি। বিশেষ করে জিলহজ মাসের প্রথম দশ দিন গোটা বছরের মধ্যে সবচেয়ে বরকতময় ও মর্যাদাপূর্ণ। রাসুলুল্লাহ (সা.) ইরশাদ করেছেন: 'জিলহজের প্রথম দশ দিনের নেক আমল আল্লাহর কাছে অন্য যেকোনো দিনের আমলের চেয়ে বেশি প্রিয়।' হজের সমস্ত কার্যক্রম ৮ থেকে ১৩ই জিলহজের মধ্যে অনুষ্ঠিত হয়।",
            "Dhul Hijjah is the 12th and concluding month of the Islamic calendar, one of the four sacred months. Its first ten days are the most virtuous days of the entire year, wherein righteous deeds are beloved to Allah above all else.",
            "• চাঁদ দেখার পর থেকেই বেশি বেশি তাকবীর, তাহমীদ ও তাসবীহ পাঠ করা\n• সামর্থ্য অনুযায়ী ১ থেকে ৯ই জিলহজ নফল রোজা রাখা (বিশেষ করে ৯ই জিলহজ আরাফার রোজা)\n• ৮ থেকে ১৩ জিলহজ হজের প্রতিটি রুকন সঠিক সময়ে সম্পাদন করা",
            "• Increase glorification of Allah through Takbir, Tahmid, and Tahleel\n• Observe voluntary fasts during the first nine days, particularly on the Day of Arafah\n• Perform all rites of Hajj diligently from the 8th to the 13th of Dhul Hijjah",
            "• জিলহজের চাঁদ ওঠার পর থেকে কুরবানি সম্পন্ন হওয়া পর্যন্ত নখ ও চুল কাটা থেকে বিরত থাকা মুস্তাহাব",
            "• Recommended for those offering Qurbani to refrain from cutting hair and nails from 1st of month until sacrifice",
            "وَالْفَجْرِ ۝ وَلَيَالٍ عَشْرٍ",
            "উচ্চারণ: ওয়াল ফাজর, ওয়া লায়ালিন আশর।",
            "Pronunciation: Wal-fajr, wa layalin 'ashr.",
            "অর্থ: শপথ ফজরের, এবং শপথ দশ রাতের (জিলহজের প্রথম দশ রাতের)।",
            "Meaning: By the dawn, and by the ten nights (of Dhul Hijjah).",
            "সূরা আল-ফজর: ১-২, সহীহ বুখারী: ৯৬৯"
        ));

        // 19. জিলহজের আমল
        list.add(new HajjTopicItem(
            19,
            "জিলহজের আমল",
            "Deeds of Dhul Hijjah",
            "প্রথম ১০ দিনের বিশেষ আমল",
            "Virtuous Deeds of 1st 10 Days",
            R.drawable.ic_hajj_19_dhul_hijjah_amal,
            "জিলহজ মাসের প্রধান আমলসমূহ:\n১. নফল রোজা পালন (বিশেষ করে ৯ই জিলহজ আরাফার দিনে রোজা রাখা, যা বিগত ও আগামী এক বছরের গুনাহ মাফ করে দেয়—তবে আরাফাত ময়দানে উপস্থিত হাজীদের জন্য এ রোজা রাখা নিষেধ যাতে শারীরিক দুর্বলতা না আসে)।\n২. তাকবীরে তাশরীক পাঠ করা (৯ই জিলহজ ফজর থেকে ১৩ই জিলহজ আসর পর্যন্ত প্রত্যেক ফরজ নামাজের পর পাঠ করা ওয়াজিব)।\n৩. আল্লাহর উদ্দেশ্যে সাধ্যমতো পশু কুরবানি করা।\n৪. দান-সদকা ও তওবা-ইস্তিগফার বৃদ্ধি করা।",
            "Key virtuous deeds during Dhul Hijjah:\n1. Fasting on the Day of Arafah for non-pilgrims (expiates sins of previous and upcoming year)\n2. Reciting Takbeer al-Tashreeq after every obligatory prayer from Fajr of 9th till Asr of 13th\n3. Offering sacrificial Qurbani animal for Allah\n4. Generous charity, sincere repentance, and nightly Tahajjud prayers.",
            "• ৯ই জিলহজ ফজর থেকে ১৩ই জিলহজ আসর পর্যন্ত মোট ২৩ ওয়াক্ত ফরজ সালাতের পর তাকবীরে তাশরীক পাঠ করা ওয়াজিব\n• কোরবানির পশুর গোশত গরিব-মিসকিন ও আত্মীয়-স্বজনের মাঝে বণ্টন করা",
            "• Recite Takbeer al-Tashreeq audibly after all 23 prescribed obligatory prayers\n• Distribute Qurbani meat equitably to the poor, neighbors, and kin",
            "• আরাফাতে অবস্থানরত হাজীদের জন্য ৯ জিলহজ রোজা রাখা অনুচিত (কারণ রাসুলুল্লাহ সা. হজে এ রোজা রাখেননি)",
            "• Pilgrims performing Hajj at Arafah should NOT fast on that day to retain physical stamina",
            "اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ لَا إِلَهَ إِلَّا اللَّهُ وَاللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ وَلِلَّهِ الْحَمْدُ",
            "উচ্চারণ: আল্লাহু আকবার, আল্লাহু আকবার, লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার, আল্লাহু আকবার ওয়া লিল্লাহিল হামদ।",
            "Pronunciation: Allahu Akbar, Allahu Akbar, La ilaha illallahu wallahu Akbar, Allahu Akbar wa lillahil-hamd.",
            "অর্থ: আল্লাহ সর্বশ্রেষ্ঠ, আল্লাহ সর্বশ্রেষ্ঠ; আল্লাহ ব্যতীত কোনো উপাস্য নেই, আল্লাহ সর্বশ্রেষ্ঠ, আল্লাহ সর্বশ্রেষ্ঠ এবং সমস্ত প্রশংসা আল্লাহরই।",
            "Meaning: Allah is the Greatest, Allah is the Greatest; there is no god but Allah, and Allah is the Greatest, Allah is the Greatest, and to Allah belongs all praise.",
            "সহীহ মুসলিম: ১১৬২, মুসান্নাফে ইবনে আবি শায়বা: ৫৬৯৬"
        ));

        // 20. বিদায় হজ
        list.add(new HajjTopicItem(
            20,
            "বিদায় হজ",
            "Farewell Pilgrimage",
            "রাসূল (সা.)-এর ঐতিহাসিক ভাষণ",
            "Prophet's Final Sermon",
            R.drawable.ic_hajj_20_farewell,
            "দশম হিজরিতে প্রিয় নবী হযরত মুহাম্মদ (সা.) তাঁর জীবনের একমাত্র ফরজ হজ আদায় করেন, যা 'বিদায় হজ' (হাজ্জাতুল বিদা) নামে পরিচিত। এ হজে আরাফাতের ময়দানে জাবালে রহমতে দাঁড়িয়ে মহানবী (সা.) সোয়া লক্ষ সাহাবীর উপস্থিতিতে মানব ইতিহাসের সর্বশ্রেষ্ঠ ঐতিহাসিক ভাষণ প্রদান করেন। এ ভাষণে তিনি বর্ণবাদ দূরীকরণ, নারীর অধিকার, মানবাধিকার প্রতিষ্ঠা, রক্তপাত ও সুদের চির অবসান ঘোষণা করেন এবং কুরআন ও সুন্নাহ আঁকড়ে ধরার নির্দেশ দেন।",
            "In the 10th year of Hijrah, Prophet Muhammad (PBUH) performed his only Hajj, known as the Farewell Pilgrimage (Hajjat al-Wada). On the plains of Arafah, he delivered the historic Farewell Sermon to over 124,000 companions, cementing universal human rights, racial equality, sanctity of life and wealth, women's rights, and the finality of Divine revelation.",
            "• বিদায় হজের প্রতিটি ঐতিহাসিক নির্দেশনা ব্যক্তি, পরিবার ও সমাজে মেনে চলা\n• সকল প্রকার সুদ, বর্ণবৈষম্য ও কুসংস্কার থেকে নিজেকে মুক্ত রাখা\n• নারীদের প্রতি সদ্ব্যবহার ও মর্যাদাপূর্ণ আচরণ নিশ্চিত করা",
            "• Implement the core teachings of the Farewell Sermon in daily societal life\n• Completely eradicate usury, racism, bigotry, and tribal chauvinism\n• Treat women with dignity, honor, equity, and loving kindness",
            "• ধর্মের নামে কোনো নতুন বিদআত বা মনগড়া রীতি চালু করা কঠোরভাবে নিষিদ্ধ",
            "• Never introduce innovations (Bid'ah) into the pristine teachings of Islam",
            "الْيَوْمَ أَكْمَلْتُ لَكُمْ دِينَكُمْ وَأَتْمَمْتُ عَلَيْكُمْ نِعْمَتِي وَرَضِيتُ لَكُمُ الْإِسْلَامَ دِينًا",
            "উচ্চারণ: আল-ইয়াওমা আকমালতু লাকুম দীনাকুম ওয়া আতমামতু আলাইকুম নি'মাতী ওয়া রাদ্বীতু লাকুমুল ইসলামা দীনা।",
            "Pronunciation: Al-yawma akmaltu lakum deenakum wa atmamtu 'alaykum ni'matee wa radeetu lakumul-Islama deena.",
            "অর্থ: আজ আমি তোমাদের জন্য তোমাদের দ্বীনকে পূর্ণাঙ্গ করে দিলাম এবং তোমাদের ওপর আমার নেয়ামত সম্পূর্ণ করলাম এবং ইসলামকে তোমাদের দ্বীন হিসেবে মনোনীত করলাম।",
            "Meaning: This day I have perfected for you your religion and completed My favor upon you and have approved for you Islam as your religion.",
            "সূরা আল-মায়িদা: ৩, সহীহ মুসলিম: ১২১৮"
        ));

        // 21. ওমরাহ হজ
        list.add(new HajjTopicItem(
            21,
            "ওমরাহ হজ",
            "Umrah Guide",
            "ওমরাহর সম্পূর্ণ নিয়ম",
            "Complete Guide to Umrah",
            R.drawable.ic_hajj_21_umrah,
            "ওমরাহ একটি অত্যন্ত মর্যাদাপূর্ণ নফল বা সুন্নাতে মুয়াক্কাদা ইবাদত যা বছরের যেকোনো সময় পালন করা যায়। ওমরাহর মূল কাজ ৪টি:\n১. মীকাত থেকে ইহরাম বাঁধা (ফরজ)\n২. কা'বা শরীফ সাত চক্কর তাওয়াফ করা (ফরজ)\n৩. সাফা ও মারওয়া পাহাড়ে সাতবার সাঈ করা (ওয়াজিব)\n৪. মাথার চুল মুণ্ডন করা বা সমানভাবে ছোট করা (হলক/কসর) (ওয়াজিব)। এরপর ইহরামের সমাপ্তি ঘটে এবং স্বাভাবিক পোশাক পরা যায়।",
            "Umrah is a highly rewarded pilgrimage that can be performed at any time of the year. It comprises 4 fundamental steps:\n1. Entering Ihram from Miqat (Fard)\n2. Tawaf of the Holy Kaaba 7 circuits (Fard)\n3. Sa'i between Safa and Marwah 7 laps (Wajib)\n4. Shaving (Halq) or trimming (Taqseer) the hair to exit Ihram (Wajib).",
            "• ওমরাহ পালনের সময় ধীরস্থিরতা ও আন্তরিক একাগ্রতা বজায় রাখা\n• তাওয়াফ শেষে ২ রাকাত ওয়াজিবুল তাওয়াফ নামাজ আদায় করা\n• সাঈ শেষ করে পুরুষদের জন্য মাথা কামানো (হলক) অধিক সওয়াবের, অথবা পুরো মাথার চুল সমানভাবে ছোট করা",
            "• Perform rites with tranquil composure, patience, and humility\n• Offer 2 rakahs of prayer behind Maqam Ibrahim following Tawaf\n• For men, shaving head completely (Halq) is three times more rewarded than trimming",
            "• চুল না কেটে বা আংশিক একপাশের সামান্য চুল কেটে হালাল হয়ে যাওয়া নিষেধ; পুরো মাথার চুল কাটতে হবে",
            "• Do not exit Ihram without proper Halq or uniform Taqseer across the entire head",
            "وَأَتِمُّوا الْحَجَّ وَالْعُمْرَةَ لِلَّهِ",
            "উচ্চারণ: ওয়া আতিম্মুল হাজ্জা ওয়াল উমরাতা লিল্লাহ।",
            "Pronunciation: Wa atimmul-hajja wal-'umrata lillah.",
            "অর্থ: আর তোমরা আল্লাহর উদ্দেশ্যে হজ ও ওমরাহ পূর্ণ কর।",
            "Meaning: And complete the Hajj and Umrah for Allah.",
            "সূরা আল-বাকারা: ১৯৬, সহীহ বুখারী: ১৭৭৩"
        ));

        // 22. আইয়ামে তাশরীক
        list.add(new HajjTopicItem(
            22,
            "আইয়ামে তাশরীক",
            "Ayyam at-Tashreeq",
            "১১, ১২ ও ১৩ জিলহজ",
            "Days of Tashreeq (11th-13th)",
            R.drawable.ic_hajj_22_ayyame_tashreeq,
            "জিলহজ মাসের ১১, ১২ ও ১৩ তারিখ—এই ৩ দিনকে 'আইয়ামে তাশরীক' বলা হয়। রাসুলুল্লাহ (সা.) বলেছেন: 'আইয়ামে তাশরীক হলো পানাহার ও আল্লাহ তাআলার জিকিরের দিন।' এ দিনগুলোতে হাজীরা মিনায় অবস্থান করেন এবং প্রতিদিন দুপুর গড়ার পর ধারাবাহিকভাবে ৩টি জামারাতে (ছোট, মেজো ও বড় শয়তান) ৭টি করে মোট ২১টি পাথর নিক্ষেপ করেন। সাধারণ মুসলিমদের জন্য এ দিনগুলোতে রোজা রাখা সম্পূর্ণ হারাম।",
            "The 11th, 12th, and 13th of Dhul Hijjah are known as Ayyam at-Tashreeq. The Prophet (PBUH) designated them as days of eating, drinking, and remembering Allah. Pilgrims stay in Mina and stone all three Jamarat pillars each afternoon with seven pebbles each. Fasting is prohibited.",
            "• প্রতিদিন জোহরের ওয়াক্ত প্রবেশের পর কঙ্কর নিক্ষেপ করা\n• ছোট জামারাহ থেকে শুরু করে মেজো ও বড় জামারাতে ক্রমানুসারে পাথর মারা\n• প্রথম দুই জামারাহর পর একটু দূরে সরে ক্বিবলামুখী হয়ে দীর্ঘ সময় দোয়া করা",
            "• Cast pebbles after Zawal (midday sun decline) on each of these three days\n• Follow the sequential order: Small (Sughra), Medium (Wusta), and Large (Kubra)\n• Step aside after the first two Jamarat to offer heartfelt supplications facing Qiblah",
            "• সূর্যাস্তের পূর্বে কঙ্কর নিক্ষেপ করা উত্তম; তবে ভিড় থাকলে রাতেও নিক্ষেপ করা জায়েজ\n• এই দিনগুলোতে নফল রোজা রাখা নিষিদ্ধ",
            "• Avoid rushing in scorching noon heat; evening stoning is permissible to prevent stampedes\n• Fasting during these days is strictly forbidden",
            "وَاذْكُرُوا اللَّهَ فِي أَيَّامٍ مَّعْدُودَاتٍ",
            "উচ্চারণ: ওয়াযকুরুল্লাহা ফী আইয়্যামিম মা'দূদাত।",
            "Pronunciation: Wadh-kurullaha fee ayyamim ma'doodat.",
            "অর্থ: আর তোমরা নির্দিষ্ট দিনসমূহে (আইয়ামে তাশরীকে) আল্লাহকে স্মরণ কর।",
            "Meaning: And remember Allah during [specific] numbered days.",
            "সূরা আল-বাকারা: ২০৩, সহীহ মুসলিম: ১১৪১"
        ));

        // 23. জিয়ারতে মদিনা
        list.add(new HajjTopicItem(
            23,
            "জিয়ারতে মদিনা",
            "Visiting Madinah",
            "মসজিদে নববী ও রওজা শরীফ",
            "Prophet's Mosque & Rawdah",
            R.drawable.ic_hajj_23_ziyarat_madina,
            "যদিও মদিনা মুনাওয়ারা জিয়ারত হজের রুকন নয়, তবুও হজের আগে বা পরে প্রিয় নবীজির শহর মদিনা জিয়ারত করা প্রতিটি মুমিনের প্রাণের পরম আকাঙ্ক্ষা ও সুন্নাতে মুয়াক্কাদা। মসজিদে নববীতে এক রাকাত নামাজে অন্য মসজিদে ১০০০ রাকাত নামাজের সওয়াব পাওয়া যায়। এছাড়া রয়েছে রাওজাতুম মিন রিয়াদিল জান্নাহ এবং রাসুলুল্লাহ (সা.), হযরত আবু বকর (রা.) ও হযরত ওমর (রা.)-এর পবিত্র রওজায় আদবের সাথে সালাম পেশ করার বিরল সৌভাগ্য।",
            "Visiting Madinah al-Munawwarah is an immensely blessed Sunnah. A single prayer in the Prophet's Mosque yields 1,000 times the reward of other mosques. Pilgrims cherish praying in the Rawdah (a Garden of Paradise) and conveying Salam at the noble resting place of the Beloved Prophet (PBUH) and his two caliphs.",
            "• মসজিদে নববীতে সার্বক্ষণিক শালীনতা ও পরম আদব বজায় রাখা\n• নুসুক অ্যাপে পারমিট নিয়ে রিয়াজুল জান্নাতে সালাত ও তওবা করা\n• রওজা শরীফের সামনে দাঁড়িয়ে অত্যন্ত ধীরস্থির ও বিনম্র কণ্ঠে সালাম পেশ করা",
            "• Maintain supreme composure, reverence, and soft voices within the Prophet's Mosque\n• Reserve Rawdah appointment via Nusuk app and offer Tahiyyatul Masjid & Dua\n• Convey respectful greetings (Salam) at the Prophet's grave with deep humility",
            "• রওজা শরীফের গ্রিল বা দেয়াল চুম্বন, স্পর্শ বা সিজদা করা কঠোরভাবে শিরক ও নিষিদ্ধ\n• নববী চত্বরে চিৎকার বা চেঁচামেচি করা নিষেধ",
            "• Never kiss or rub walls/screens of the Rawdah; prostrating to graves is strictly forbidden\n• Avoid raising voices or loud arguments inside the sacred sanctuary",
            "الصَّلَاةُ فِي مَسْجِدِي هَذَا أَفْضَلُ مِنْ أَلْفِ صَلَاةٍ فِيمَا سِوَاهُ إِلَّا الْمَسْجِدَ الْحَرَامَ",
            "উচ্চারণ: আস-সালাতু ফী মাসজিদী হাযা আফদালু মিন আলফি সালাতিন ফীমা সিওয়াহু ইল্লাল মাসজিদাল হারাম।",
            "Pronunciation: As-salatu fee masjidee hadha afdalu min alfi salatin feema siwahu illal-Masjidil-Haram.",
            "অর্থ: আমার এই মসজিদে এক নামাজ অন্য মসজিদে এক হাজার নামাজের চেয়েও উত্তম, তবে মসজিদুল হারাম ছাড়া।",
            "Meaning: One prayer in my mosque is better than one thousand prayers elsewhere, except al-Masjid al-Haram.",
            "সহীহ বুখারী: ১১৯০, সহীহ মুসলিম: ১৩৯৪"
        ));

        // 24. ভুল ত্রুটি
        list.add(new HajjTopicItem(
            24,
            "ভুল ত্রুটি",
            "Mistakes & Expiations",
            "দম, কাফফারা ও সদকা",
            "Penalties, Dam & Expiations",
            R.drawable.ic_hajj_24_mistakes_kaffarah,
            "হজের সফরে মানবিক অসাবধানতাবশত কোনো ত্রুটি হলে শরীয়ত অনুযায়ী তার প্রতিকার ও কাফফারা রয়েছে:\n১. দম (Dam): কোনো ওয়াজিব ছুটে গেলে বা ইহরামের বড় কোনো নিষেধাজ্ঞা ভঙ্গ করলে কাফফারা স্বরূপ হারাম শরীফের সীমানার ভেতর একটি ছাগল/বকরি কোরবানি দেওয়া ওয়াজিব।\n২. সদকা (Sadaqah): অপেক্ষাকৃত ছোট ভুল (যেমন সামান্য চুল কাটা বা সামান্য সুগন্ধি ব্যবহার) হলে পৌনে দুই কেজি গম বা তার সমপরিমাণ সদকা ফিতর মিসকিনকে দান করতে হয়।\n৩. সিয়াম (Fasting): কোনো ক্ষেত্রে আর্থিক সামর্থ্য না থাকলে নির্ধারিত রোজা রাখার বিধান রয়েছে।",
            "Unintentional mistakes during Hajj are rectified according to Islamic jurisprudence:\n1. Dam (Sacrifice): Missing a Wajib or violating major Ihram prohibitions requires slaughtering a sheep within the Haram boundary.\n2. Sadaqah: Minor violations (e.g., trimming minor hair or slight perfume) require feeding the poor with Sadaqatul Fitr.\n3. Fasting: In specific cases where pilgrims cannot afford sacrifice, ordained fasting compensates.",
            "• কোনো ভুল হলে কালবিলম্ব না করে অভিজ্ঞ নির্ভরযোগ্য আলেমের সাথে পরামর্শ করা\n• দমের পশু অবশ্যই হারাম শরিফের সীমানার (হারামের) ভেতরে জবাই হতে হবে\n• ভুল সংশোধনের সাথে সাথে খাঁটি মনে আল্লাহর দরবারে ইস্তিগফার করা",
            "• Consult qualified Islamic scholars promptly to ascertain the exact required expiation\n• Sacrificial animals for Dam must be slaughtered strictly within the sacred Haram limits\n• Accompany every expiation with sincere repentance and seeking divine pardon",
            "• ভুলত্রুটি গোপন করা বা অবহেলাভরে কাফফারা এড়িয়ে চলা হজের পবিত্রতাকে ক্ষুণ্ণ করে",
            "• Never ignore or neglect required expiations; settling obligations purifies your Hajj",
            "فَمَن كَانَ مِنكُم مَّرِيضًا أَوْ بِهِ أَذًى مِّن رَّأْسِهِ فَفِدْيَةٌ مِّن صِيَامٍ أَوْ صَدَقَةٍ أَوْ نُسُكٍ",
            "উচ্চারণ: ফামান কানা মিনকুম মারীদান আউ বিহী আজাম মির রা'সিহী ফাফিদইয়াতুম মিন সিয়ামিন আউ সাদাক্বাতিন আউ নুসুক।",
            "Pronunciation: Faman kana minkum mareedan aw bihee adham mir-ra'sihee fafidyatum min siyamin aw sadaqatin aw nusuk.",
            "অর্থ: আর তোমাদের মধ্যে যে ব্যক্তি অসুস্থ হবে কিংবা মাথায় কোনো কষ্ট থাকবে, সে রোজা, সদকা অথবা পশু কোরবানির মাধ্যমে ফিদয়া আদায় করবে।",
            "Meaning: And whoever among you is ill or has an ailment of the head shall offer a ransom of fasting or charity or sacrifice.",
            "সূরা আল-বাকারা: ১৯৬, সহীহ বুখারী: ১৮১৪"
        ));

        // 25. প্রয়োজনীয় তথ্য
        list.add(new HajjTopicItem(
            25,
            "প্রয়োজনীয় তথ্য",
            "Essential Information",
            "জরুরি তথ্য ও দিকনির্দেশনা",
            "Vital Guidelines & Contacts",
            R.drawable.ic_hajj_25_necessary_info,
            "হজের সফরে প্রতিটি হাজীর জন্য অতি দরকারি কিছু বাস্তব তথ্য ও নিয়মাবলী:\n১. কারেন্সি ও লেনদেন: সৌদি রিয়াল (SAR) পর্যাপ্ত ক্যাশ সাথে রাখা এবং আন্তর্জাতিক ক্রেডিট/ডেবিট কার্ড কার্যকর রাখা।\n২. সিম ও ইন্টারনেট: সৌদি আরবের লোকাল সিম (STC, Mobily, Zain) অথবা আন্তর্জাতিক রোমিং ব্যবহার করা।\n৩. জরুরি নম্বরসমূহ: সৌদি পুলিশ (৯৯৯), অ্যাম্বুলেন্স (৯৯৭), ট্রাফিক পুলিশ (৯৯৩), হজ ও ওমরাহ বিষয়ক মন্ত্রণালয় হেল্পলাইন (১৯৬৬)।\n৪. হারিয়ে গেলে: নিজের হোটেলের কার্ড ও মোয়াল্লিম তাঁবুর নম্বর সবসময় সাথে রাখা।",
            "Key operational information for pilgrims:\n1. Currency: Carry sufficient Saudi Riyals (SAR) in cash alongside international debit/credit cards.\n2. Connectivity: Procure local SIM cards (STC, Mobily, Zain) at airport or active roaming.\n3. Emergency Numbers: Police (999), Ambulance (997), Ministry Helpline (1966).\n4. Lost Pilgrim Protocol: Always retain hotel cards and Moallim wristbands.",
            "• স্মার্টফোনে গুগল ম্যাপে হোটেল ও ক্যাম্পের লোকেশন পিন করে সেভ রাখা\n• সৌদি রোদ ও ডিহাইড্রেশন থেকে বাঁচতে প্রতিদিন পর্যাপ্ত পানি ও ওরাল স্যালাইন পান করা\n• দলনেতা ও সহযাত্রীদের ফোন নম্বর ডায়াল লিস্টে সেভ করে রাখা",
            "• Save hotel and tent GPS coordinates offline on Google Maps\n• Drink abundant fluids and hydration salts to guard against extreme desert heat\n• Keep your group guide and roommates' contact numbers easily accessible",
            "• অচেনা কাউকে নিজের লাগেজ বহন করতে দেওয়া বা পাসপোর্ট হস্তান্তর করা যাবে না",
            "• Never hand over your passport or baggage to unauthorized strangers",
            "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            "উচ্চারণ: হাসবুনাল্লাহু ওয়া নি'মাল ওয়াকিল।",
            "Pronunciation: Hasbunallahu wa ni'mal wakeel.",
            "অর্থ: আল্লাহই আমাদের জন্য যথেষ্ট এবং তিনি কতই না উত্তম কর্মবিধায়ক!",
            "Meaning: Sufficient for us is Allah, and He is the best Disposer of affairs.",
            "সূরা আল-ইমরান: ১৭৩"
        ));

        // 26. হজ গাইড
        list.add(new HajjTopicItem(
            26,
            "হজ গাইড",
            "Day-by-Day Guide",
            "৮ থেকে ১৩ জিলহজের টাইমলাইন",
            "Hajj Day-by-Day Timeline",
            R.drawable.ic_hajj_26_guide,
            "হজের ৫ দিনের মূল কর্মযজ্ঞের সংক্ষিপ্ত ধারাবাহিক টাইমলাইন:\n• ১ম দিন (৮ জিলহজ - ইয়াওমুত তারবিয়াহ): ইহরাম পরে মিনায় গমন এবং সেখানে ৫ ওয়াক্ত নামাজ আদায়।\n• ২য় দিন (৯ জিলহজ - ইয়াওমে আরাফাহ): সকালে আরাফাতে গমন, জোহর-আসর একত্র আদায় ও সূর্যাস্ত পর্যন্ত দোয়া; সূর্যাস্তের পর মুজদালিফায় রাতযাপন।\n• ৩য় দিন (১০ জিলহজ - ইয়াওমুন নহর): সকালে মিনায় বড় শয়তানকে কঙ্কর নিক্ষেপ, দমে শোকর (কুরবানি), মাথা মুণ্ডন করে ইহরাম ভঙ্গ, এবং মক্কায় গিয়ে তাওয়াফে জিয়ারত ও সাঈ।\n• ৪র্থ ও ৫ম দিন (১১ ও ১২ জিলহজ - আইয়ামে তাশরীক): মিনায় রাত্রিযাপন ও প্রতিদিন ৩ জামারাতে পাথর নিক্ষেপ।\n• মক্কা ত্যাগের পূর্বে: বিদায়ী তাওয়াফ সম্পন্ন করা।",
            "Concise Day-by-Day Roadmap of Hajj:\n• Day 1 (8th Dhul Hijjah): Enter Ihram, move to Mina, pray 5 daily prayers.\n• Day 2 (9th Dhul Hijjah): Plain of Arafah standing, combined Dhuhr/Asr, proceed to Muzdalifah after sunset.\n• Day 3 (10th Dhul Hijjah): Jamarat Kubra stoning, animal sacrifice, Halq/haircut, Tawaf al-Ziyarah.\n• Days 4 & 5 (11th & 12th Dhul Hijjah): Overnight stay in Mina, stone all 3 Jamarat each afternoon.\n• Departure: Conclude with Tawaf al-Wada (Farewell Tawaf).",
            "• প্রতিদিনের নির্ধারিত কাজের সময়সূচি দলের মোয়াল্লিম নির্দেশিকা অনুযায়ী প্রস্তুত রাখা\n• প্রতিটি ধাপ সতর্কতার সাথে সুন্নাত তরীকায় সম্পন্ন করা",
            "• Maintain synchronization with your group schedule for transport and Jamarat timings\n• Execute each rite in strict compliance with the authentic Sunnah method",
            "• কোনো ধাপ তাড়াহুড়ো করে সুন্নাত নিয়ম লঙ্ঘন করে আগে-পিছে করা অনুচিত",
            "• Do not alter prescribed sequence haphazardly without genuine Islamic justification",
            "خُذُوا عَنِّي مَنَاسِكَكُمْ",
            "উচ্চারণ: খুযূ 'আন্নী মানাসিকাকুম।",
            "Pronunciation: Khudhoo 'annee manasikakum.",
            "অর্থ: তোমরা আমার কাছ থেকে তোমাদের হজের নিয়ম-পদ্ধতি শিখে নাও।",
            "Meaning: Learn your pilgrimage rituals from me.",
            "সহীহ মুসলিম: ১২৯৭, সুনান আন-নাসায়ী: ৩০৬২"
        ));

        // 27. হজ ম্যাপ
        list.add(new HajjTopicItem(
            27,
            "হজ ম্যাপ",
            "Hajj Route Map",
            "পবিত্র স্থানের ভৌগোলিক ম্যাপ",
            "Geographical Map & Distances",
            R.drawable.ic_hajj_27_map,
            "হজের পবিত্র স্থানগুলোর পারস্পরিক দূরত্ব ও ভৌগোলিক অবস্থান:\n১. মক্কা হারামাইন থেকে মিনা উপত্যকা: প্রায় ৮ কিলোমিটার।\n২. মিনা থেকে মুজদালিফা ময়দান: প্রায় ৩-৪ কিলোমিটার।\n৩. মুজদালিফা থেকে আরাফাত প্রান্তর: প্রায় ৮-৯ কিলোমিটার।\n৪. মক্কা থেকে সরাসরি আরাফাত: প্রায় ২১ কিলোমিটার।\nহজের পুরো এলাকাটিতে সুপরিকল্পিত আল-মাশায়ের ট্রেন সার্ভিস, ডেডিকেটেড বাস রুট ও সুপ্রশস্ত পথচারী ফুটওভার ব্রিজ রয়েছে।",
            "Key distances and spatial landmarks of the holy pilgrimage corridor:\n1. Makkah to Mina: ~8 km\n2. Mina to Muzdalifah: ~4 km\n3. Muzdalifah to Arafah: ~9 km\n4. Makkah directly to Arafah: ~21 km\nThe area is fully connected via the Al-Mashaaer Holy Sites Metro, shuttle bus routes, and pedestrian bridges.",
            "• নিজের তাঁবু নম্বর, জোন নম্বর (যেমন: মিনা জোন ১/২) এবং নিকটস্থ ট্রেন স্টেশনের নম্বর নোট করে রাখা\n• হাঁটার সময় নির্ধারিত পদচারী শেড ও পানির কুলার ব্যবহার করা",
            "• Record your Mina tent number, street sign, pole number, and nearby Metro station\n• Follow shaded pedestrian corridors equipped with cooling misting fans",
            "• অননুমোদিত শর্টকাট বা পাহাড়ি খাঁদ দিয়ে ঝুঁকিপূর্ণ হাঁটাচলা করা বিপজ্জনক",
            "• Never attempt hazardous shortcuts across rocky hills or unauthorized tunnels",
            "وَهَدَيْنَاهُ النَّجْدَيْنِ",
            "উচ্চারণ: ওয়া হাদাইনাহুন নাজদাইন।",
            "Pronunciation: Wa hadaynahun-najdayn.",
            "অর্থ: আর আমি তাকে দুটি পথই প্রদর্শন করেছি।",
            "Meaning: And shown him the two ways.",
            "সূরা আল-বালাদ: ১০"
        ));

        // 28. হজ প্রি-রেজিস্ট্রেশন
        list.add(new HajjTopicItem(
            28,
            "হজ প্রি-রেজিস্ট্রেশন",
            "Pre-Registration",
            "সরকারি ও বেসরকারি নিবন্ধন",
            "Hajj Portal & Registration Process",
            R.drawable.ic_hajj_28_pre_registration,
            "বাংলাদেশ থেকে সরকারি বা বেসরকারি মাধ্যমে হজে যাওয়ার প্রাথমিক ধাপ হলো হজ প্রি-রেজিস্ট্রেশন। ধর্ম বিষয়ক মন্ত্রণালয়ের ই-হজ সিস্টেম (hajj.gov.bd)-এর মাধ্যমে জাতীয় পরিচয়পত্র (NID) দিয়ে নির্দিষ্ট ফি জমাদানপূর্বক প্রি-রেজিস্ট্রেশন করতে হয়। প্রি-রেজিস্ট্রেশনের পর একটি ইউনিক সিরিয়াল নম্বর দেওয়া হয়, যার ভিত্তিতে প্রতি বছর নির্ধারিত কোটা অনুযায়ী চূড়ান্ত নিবন্ধন ও ভিসা প্রক্রিয়াকরণ সম্পন্ন হয়।",
            "Hajj Pre-Registration is the initial mandatory milestone for pilgrims from Bangladesh through the official e-Hajj system (hajj.gov.bd). Applicants provide NID data, pay registration fees via banks, and receive a permanent tracking serial number governing official quota allocation.",
            "• সরকার অনুমোদিত বৈধ ব্যাংক ও এজেন্সির মাধ্যমে রেজিস্ট্রেশন সম্পন্ন করা\n• ট্র্যাকিং নম্বর ও পেমেন্ট ভাউচার যত্নসহকারে সংরক্ষণ করা\n• পাসপোর্ট নবায়ন ও মেয়াদ কমপক্ষে পরবর্তী বছরের ফেব্রুয়ারি পর্যন্ত রাখা",
            "• Complete registration only through authorized commercial banks or licensed agencies\n• Retain payment slips, tracking vouchers, and official acknowledgements\n• Ensure machine-readable or e-passport validity extends at least 6 months beyond travel",
            "• কোনো অনুমোদনহীন ব্যক্তি বা মধ্যস্বত্বভোগীর সাথে নগদ অর্থ লেনদেন করা থেকে বিরত থাকুন",
            "• Never hand over cash to unauthorized middlemen or unlicensed intermediaries",
            "وَأَوْفُوا بِالْعَهْدِ ۖ إِنَّ الْعَهْدَ كَانَ مَسْئُولًا",
            "উচ্চারণ: ওয়া আউফূ বিল-'আহদি, ইন্নাল-'আহদা কানা মাস'ঊলা।",
            "Pronunciation: Wa awfoo bil-'ahdi, innal-'ahda kana mas'oola.",
            "অর্থ: এবং তোমরা প্রতিশ্রুতি পূরণ কর; নিশ্চয় প্রতিশ্রুতি সম্পর্কে কৈফিয়ত তলব করা হবে।",
            "Meaning: And fulfill every commitment. Indeed, the commitment will be inquired about.",
            "সূরা আল-ইসরা: ৩৪"
        ));

        // 29. হজ যাত্রা
        list.add(new HajjTopicItem(
            29,
            "হজ যাত্রা",
            "Hajj Travel Journey",
            "ফ্লাইট ও এয়ারপোর্ট প্রস্তুতি",
            "Flight Journey & Airport Protocols",
            R.drawable.ic_hajj_29_journey,
            "হজ যাত্রা একজন মুমিনের জীবনের সবচেয়ে রোমাঞ্চকর ও আধ্যাত্মিক সফর। ঢাকা শাহজালাল আন্তর্জাতিক বিমানবন্দর থেকে সরাসরি জেদ্দা বাদশাহ আব্দুল আজিজ বিমানবন্দর অথবা মদিনা প্রিন্স মোহাম্মদ বিমানবন্দরে বিশেষ ডেডিকেটেড হজ ফ্লাইটের মাধ্যমে যাত্রা পরিচালিত হয়। বর্তমানে 'মক্কা রুট ইনিশিয়েটিভ' (Makkah Route Initiative)-এর অধীনে ঢাকাতেই সৌদি আরবের ইমিগ্রেশন সম্পন্ন হয়ে যায়, ফলে সৌদি পৌঁছে সরাসরি হোটেলে যাওয়া যায়।",
            "The pilgrim's flight journey departs from Dhaka to King Abdulaziz Airport (Jeddah) or Prince Mohammad Airport (Madinah). Under the 'Makkah Route Initiative', Saudi customs and immigration are completed at Dhaka Airport, enabling swift transit upon arrival.",
            "• ফ্লাইটের অন্তত ৬-৮ ঘণ্টা পূর্বে আশকোনা হজ ক্যাম্পে রিপোর্ট করা\n• হ্যান্ড ব্যাগেজে কোনো ধারালো বস্তু বা তরল না রাখা এবং প্রেসক্রিপশন ওষুধ সাথে রাখা\n• বিমানে উঠেই অযথা সময় অপচয় না করে জিকির ও তালবিয়াহ পাঠ করা",
            "• Report to the Hajj Camp/Airport 6-8 hours before scheduled flight departure\n• Pack daily medications in hand carry with original doctor prescriptions\n• Dedicate flight hours to quiet Dhikr, Talbiyah, and reading Hajj literature",
            "• ওজনসীমা (সাধারণত ২৩ কেজি করে ২টি ব্যাগ) অতিক্রম করা থেকে বিরত থাকুন",
            "• Adhere strictly to airline baggage weight limits (usually two bags of 23 kg each)",
            "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ",
            "উচ্চারণ: সুবহানাল্লাযী সাখখারা লানা হাযা ওয়া মা কুন্না লাহু মুক্বরিনীন, ওয়া ইন্না ইলা রাব্বিনা লামুনক্বালিবূন।",
            "Pronunciation: Subhanalladhee sakh-khara lana hadha wa ma kunna lahoo muqrineen, wa inna ila Rabbina lamunqaliboon.",
            "অর্থ: পবিত্র ও মহান তিনি, যিনি একে আমাদের জন্য বশীভূত করে দিয়েছেন, অথচ আমরা একে বশীভূত করতে সমর্থ ছিলাম না। আর নিশ্চয় আমরা আমাদের প্রতিপালকের দিকেই প্রত্যাবর্তনকারী।",
            "Meaning: Glory to Him who has subjected this to us, and we could not have otherwise subdued it. And indeed, to our Lord we will return.",
            "সূরা আয-যুখরুফ: ১৩-১৪, সহীহ মুসলিম: ১৩৪২"
        ));

        // 30. ধর্মীয় স্থান
        list.add(new HajjTopicItem(
            30,
            "ধর্মীয় স্থান",
            "Historic Sacred Sites",
            "ঐতিহাসিক স্থানসমূহ",
            "Historic Sanctuaries & Landmarks",
            R.drawable.ic_hajj_30_religious_places,
            "মক্কা ও মদিনার পবিত্র ঐতিহাসিক নিদর্শনসমূহ জিয়ারত ঈমানকে সতেজ করে। উল্লেখযোগ্য পবিত্র স্থানসমূহ:\n১. জাবালে নূর ও হেরা গুহা: যেখানে পবিত্র কুরআনের প্রথম ওহি অবতীর্ণ হয়েছিল।\n২. জাবালে সওর ও সওর গুহা: হিজরতের সময় রাসুলুল্লাহ (সা.) ও আবু বকর (রা.)-এর ঐতিহাসিক আশ্রয়স্থল।\n৩. মসজিদে কুবা: ইসলামের প্রথম মসজিদ, যেখানে ২ রাকাত নামাজে এক ওমরাহর সওয়াব মিলে।\n৪. মসজিদে কিবলাতাইন: যে মসজিদে নামাজ চলাকালীন কিবলা পরিবর্তনের নির্দেশ হয়েছিল।\n৫. উহুদের প্রান্তর ও শহীদ সাহাবীদের কবরস্থান।\n৬. জান্নাতুল মুয়াল্লা ও জান্নাতুল বাকি গোরস্থান।",
            "Historical ziyarat landmarks of Makkah and Madinah reinforce faith and gratitude:\n1. Jabal al-Noor & Cave of Hira: Cradle of the first Quranic revelation.\n2. Jabal Thawr & Cave: Sanctuary during the historic Hijrah.\n3. Masjid Quba: The first mosque built in Islam, yielding the reward of an Umrah for 2 rakahs.\n4. Masjid al-Qiblatayn: Where the change of Qiblah occurred.\n5. Mount Uhud and martyrs cemetery.\n6. Jannat al-Mu'alla (Makkah) and Jannat al-Baqi (Madinah).",
            "• এসব ঐতিহাসিক স্থানে গিয়ে সাহাবায়ে কেরামের আত্মত্যাগ স্মরণ করা এবং তাঁদের জন্য দোয়া করা\n• মসজিদে কুবায় ওজু অবস্থায় গিয়ে ২ রাকাত নফল নামাজ আদায় করা (সুন্নাত)\n• উহুদ ও বাকির কবর জিয়ারতের মাসনূন দোয়া পাঠ করা",
            "• Reflect on the heroic sacrifices of the Sahabah and supplicate for their elevated status\n• Purify oneself with Wudu and pray two voluntary rakahs at Masjid Quba\n• Offer authentic Sunnah cemetery greetings at Mount Uhud and Jannat al-Baqi",
            "• পাহাড় বা গুহার পাথর ঘষে বরকত নেওয়ার চেষ্টা করা অথবা সেখানে চিরকুট বা সুতা বাঁধা সম্পূর্ণ বিদআত ও নিষেধ",
            "• Never tie threads, write names, or collect dirt/stones from caves as lucky charms (strictly forbidden)",
            "السَّلَامُ عَلَيْكُمْ أَهْلَ الدِّيَارِ مِنَ الْمُؤْمِنِينَ وَالْمُسْلِمِينَ، وَإِنَّا إِنْ شَاءَ اللَّهُ بِكُمْ لَاحِقُونَ",
            "উচ্চারণ: আস-সালামু আলাইকুম আহলাদ দিয়ারি মিনাল মু'মিনীনা ওয়াল মুসলিমীন, ওয়া ইন্না ইনশাআল্লাহু বিকুম লা-হিক্বূন।",
            "Pronunciation: As-salamu 'alaykum ahlad-diyari minal-mu'mineena wal-muslimeen, wa inna in sha Allahu bikum lahiqoon.",
            "অর্থ: হে কবরবাসী মুমিন ও মুসলিমগণ! আপনাদের ওপর শান্তি বর্ষিত হোক। নিশ্চয় আমরাও আল্লাহর ইচ্ছায় আপনাদের সাথে মিলিত হব।",
            "Meaning: Peace be upon you, O inhabitants of these dwellings from among the believers and Muslims, and we will, if Allah wills, join you.",
            "সহীহ মুসলিম: ৯৭৪, সুনান তিরমিযী: ৩২৪"
        ));

        return list;
    }
}
