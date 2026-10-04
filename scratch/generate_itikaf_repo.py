# -*- coding: utf-8 -*-
import os

topics = [
    {
        "id": 1,
        "slug": "what_is_itikaf",
        "order": 1,
        "title_bn": "ইতিকাফ কি?",
        "title_en": "What is Itikaf?",
        "card_title_bn": "ইতিকাফ কি?",
        "card_title_en": "What is Itikaf?",
        "preview_bn": "ইতিকাফ (اعتكاف) একটি আরবি শব্দ, যার আভিধানিক অর্থ কোনো স্থানে অবস্থান করা, আবদ্ধ হওয়া বা নিজেকে নিবিষ্ট রাখা। ইসলামী পরিভাষায় ইতিকাফ হলো—আল্লাহর সন্তুষ্টি ও নৈকট্য লাভের উদ্দেশ্যে দুনিয়াবি ব্যস্ততা থেকে সম্পূর্ণ বিচ্ছিন্ন হয়ে নির্দিষ্ট নিয়তে মসজিদে অবস্থান করা।",
        "preview_en": "Itikaf (اعتكاف) is an Arabic word meaning to adhere, stay, or confine oneself to something. In Islamic terminology, it means secluding oneself in a mosque with the intention of worshiping Allah and seeking His closeness.",
        "details_bn": """ইতিকাফের শাব্দিক ও পারিভাষিক অর্থ:
ইতিকাফ (اعتكاف) আরবি শব্দ। এর আভিধানিক অর্থ কোনো স্থানে অবস্থান করা, নিজেকে আবদ্ধ রাখা বা কোনো কাজে অবিচলভাবে নিবিষ্ট থাকা।
শরীয়তের পরিভাষায়: ইবাদতের নিয়তে ও আল্লাহর নৈকট্য লাভের প্রত্যাশায় সাংসারিক ও দুনিয়াবি যাবতীয় কর্মব্যস্ততা ও সংস্রব ত্যাগ করে একটি নির্দিষ্ট সময়সীমার জন্য মসজিদে অবস্থান করাকে ইতিকাফ বলা হয়।

কুরআন মাজীদে ইতিকাফের উল্লেখ:
পবিত্র কুরআনে আল্লাহ তাআলা ঘোষণা করেন:
﴿ وَأَنْتُمْ عَاكِفُونَ فِي الْمَسَاجِدِ ﴾
"আর তোমরা মসজিদে ইতিকাফরত অবস্থায় তাদের (স্ত্রীগণের) সাথে সংগম করো না।" [সূরা আল-বাকারা: ১৮৭]
অন্য আয়াতে ইব্রাহীম ও ইসমাইল (আলাইহিমাস সালাম)-কে নির্দেশ দিয়ে আল্লাহ বলেন:
﴿ وَعَهِدْنَا إِلَىٰ إِبْرَاهِيمَ وَإِسْمَاعِيلَ أَنْ طَهِّرَا بَيْتِيَ لِلطَّائِفِينَ وَالْعَاكِفِينَ وَالرُّكَّعِ السُّجُودِ ﴾
"এবং আমি ইব্রাহীম ও ইসমাইলকে নির্দেশ দিয়েছিলাম যে, তোমরা আমার ঘরকে তাওয়াফকারী, ইতিকাফকারী এবং রুকু ও সিজদাকারীদের জন্য পবিত্র রাখো।" [সূরা আল-বাকারা: ১২৫]

ইতিকাফের মূল লক্ষ্য ও আধ্যাত্মিক দর্শন:
১. আল্লাহর সাথে আত্মিক সম্পর্ক সুদৃঢ় করা: মানুষের দৈনন্দিন জীবন পার্থিব চিন্তা, পরিবার, ব্যবসা-বাণিজ্য ও সামাজিক ব্যস্ততায় আচ্ছন্ন থাকে। ইতিকাফ মানুষের মন ও হৃদয়কে ক্ষণিকের জন্য সবকিছু থেকে বিচ্ছিন্ন করে একমাত্র আল্লাহর দরবারে সঁপে দেয়।
২. লাইলাতুল কদর অনুসন্ধান: রমাদ্বানের শেষ দশকে ইতিকাফের অন্যতম প্রধান লক্ষ্য হলো বরকতময় লাইলাতুল কদর লাভ করা, যা হাজার মাসের চেয়েও উত্তম।
৩. গুনাহ মোচন ও আত্মশুদ্ধি: একাকীত্বের নীরবতায় আত্মসমালোচনা (মুহাযাবা), গভীর কান্নাকাটি ও তওবা-ইস্তিগফারের মাধ্যমে অন্তরকে পরিশুদ্ধ করা।
৪. সার্বক্ষণিক ইবাদতে থাকা: ইতিকাফকারী মসজিদে অবস্থান করার কারণে ঘুমন্ত অবস্থাতেও সার্বক্ষণিক ইবাদতের সওয়াব অর্জন করতে থাকে।""",
        "details_en": """Linguistic and Shari'ah Meaning:
The word Itikaf (اعتكاف) is derived from the Arabic root meaning to confine, adhere, or remain firmly in a place. In Islamic jurisprudence, it refers to secluding oneself inside a mosque with a specific intention to worship Allah, detaching from worldly affairs.

Mention in the Glorious Quran:
Allah the Exalted says in the Quran:
"And do not have relations with them as long as you are staying for seclusion in the mosques." [Surah Al-Baqarah: 187]
And He commanded Prophets Ibrahim and Ismail (peace be upon them):
"And We commanded Abraham and Ishmael that they should purify My House for those who perform Tawaf and those who stay for seclusion and those who bow down and prostrate." [Surah Al-Baqarah: 125]

Core Objectives of Itikaf:
1. Reconnecting Solely with Allah: Freeing the heart and mind from worldly distractions, business, and social routines to devote oneself entirely to the Creator.
2. Seeking Laylatul Qadr: The primary goal of seclusion during the last ten nights of Ramadan is to attain the immense rewards of the Night of Decree.
3. Spiritual Purification and Repentance: Engaging in deep introspection (Muhasabah), heartfelt repentance (Tawbah), and seeking forgiveness in tranquil seclusion.
4. Constant Worship Status: By remaining within the confines of the mosque for Allah's sake, an individual is continually rewarded as a worshiper, even while resting.""",
        "reference_bn": "সূরা আল-বাকারা: ১২৫, ১৮৭; সহীহ বুখারী: ২০২৬; সহীহ মুসলিম: ১১৭২",
        "reference_en": "Surah Al-Baqarah: 125, 187; Sahih al-Bukhari: 2026; Sahih Muslim: 1172"
    },
    {
        "id": 2,
        "slug": "importance_virtues_itikaf",
        "order": 2,
        "title_bn": "ইতিকাফের গুরুত্ব ও ফজিলত",
        "title_en": "Importance and Virtues of Itikaf",
        "card_title_bn": "ইতিকাফের গুরুত্ব ও ফজিলত",
        "card_title_en": "Importance and Virtues of Itikaf",
        "preview_bn": "রমাদ্বানের শেষ দশকের ইতিকাফ সুন্নাতে মুয়াক্কাদাহ আলাল কিফায়াহ। রাসুলুল্লাহ ﷺ মদীনায় হিজরতের পর থেকে ওফাত পর্যন্ত প্রতি বছর রমাদ্বানের শেষ দশ দিন নিয়মিতভাবে ইতিকাফ পালন করেছেন। এটি লাইলাতুল কদর অর্জন এবং জাহান্নাম থেকে মুক্তির এক অনন্য মাধ্যম।",
        "preview_en": "Itikaf in the last ten days of Ramadan is Sunnah Mu'akkadah al-Kifayah. From the time the Prophet ﷺ migrated to Madinah until his demise, he observed Itikaf every year without fail during the last ten days.",
        "details_bn": """ইতিকাফের ধর্মীয় গুরুত্ব ও বিধান:
রমাদ্বানের শেষ দশ দিনের ইতিকাফ হলো সুন্নাতে মুয়াক্কাদাহ আলাল কিফায়াহ (سنة مؤكدة على الكفاية)। অর্থাৎ কোনো মহল্লা বা জনপদের জামে মসজিদে অন্তত একজন ব্যক্তিও যদি ইতিকাফ পালন করেন, তবে পুরো মহল্লাবাসী দায়িত্বমুক্ত হবে। কিন্তু যদি কেউ পালন না করে, তবে মহল্লার সবাই সুন্নাত ত্যাগের অপরাধে দায়ী থাকবে।

নবীজী ﷺ-এর অবিচ্ছিন্ন আমল:
উম্মুল মুমিনীন আয়েশা (রাদ্বিয়াল্লাহু আনহা) বর্ণনা করেন:
«أَنَّ النَّبِيَّ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ كَانَ يَعْتَكِفُ العَشْرَ الأَوَاخِرَ مِنْ رَمَضَانَ حَتَّى تَوَفَّاهُ اللَّهُ، ثُمَّ اعْتَكَفَ أَزْوَاجُهُ مِنْ بَعْدِهِ»
"নবী করীম ﷺ মৃত্যুর পূর্ব পর্যন্ত প্রতি বছর রমাদ্বানের শেষ দশকে ইতিকাফ করতেন। তাঁর ওফাতের পর তাঁর পবিত্র স্ত্রীগণও ইতিকাফ পালন করেছেন।" [সহীহ বুখারী: ২০২৬, সহীহ মুসলিম: ১১৭২]

ইতিকাফের অতুলনীয় ফজিলতসমূহ:
১. নিশ্চিতভাবে লাইলাতুল কদর লাভ: রমাদ্বানের শেষ দশকের বেজোড় রাতগুলোতে লাইলাতুল কদর অবতীর্ণ হয়। যে ব্যক্তি এই দশ দিন মসজিদে অবস্থান করে, সে কোনোভাবেই এই মহান বরকতময় রাত থেকে বঞ্চিত হয় না।
২. গুনাহ হতে সার্বক্ষণিক নিরাপত্তা: ইতিকাফকারী দুনিয়াবি পাপাচার, অনর্থক পরনিন্দা (গীবত), চোখ ও কানের পাপ এবং পার্থিব ফেতনা থেকে নিজেকে সম্পূর্ণ হেফাজত রাখতে পারে।
৩. সমস্ত ভালো কাজের সওয়াব অর্জন:
ইবনে আব্বাস (রাদ্বিয়াল্লাহু আনহুমা) হতে বর্ণিত, রাসুলুল্লাহ ﷺ ইতিকাফকারী সম্পর্কে বলেছেন:
«هُوَ يَعْكِفُ الذُّنُوبَ، وَيُجْرَى لَهُ مِنَ الحَسَنَاتِ كَعَامِلِ الحَسَنَاتِ كُلِّهَا»
"ইতিকাফকারী গুনাহ থেকে মুক্ত থাকে এবং বাহিরে স্বাভাবিক অবস্থায় যেসকল নেক আমল সে করত (যেমন: জানাজায় অংশগ্রহণ, অসুস্থ ব্যক্তির সেবা ইত্যাদি), মসজিদে আবদ্ধ থাকার পরেও সেই সমস্ত আমলের পূর্ণ সওয়াব তার আমলনামায় লিপিবদ্ধ করা হয়।" [সুনানে ইবনে মাজাহ: ১৭৮১]
৪. জাহান্নাম থেকে বহু দূরবর্তী হওয়া:
রাসুলুল্লাহ ﷺ ইরশাদ করেছেন:
"যে ব্যক্তি আল্লাহর সন্তুষ্টির উদ্দেশ্যে একদিনও ইতিকাফ করবে, আল্লাহ তাআলা তার ও জাহান্নামের মাঝে তিন খন্দক (দূরত্ব) পরিমাণ ব্যবধান তৈরি করে দেবেন, যা পূর্ব ও পশ্চিম দিগন্তের মধ্যকার দূরত্বের চেয়েও বেশি।" [মুসতাদরাক আলাস সাহীহাইন: ১৬২৮]""",
        "details_en": """Legal Status and Religious Weight:
Observing Itikaf during the last ten days of Ramadan is considered Sunnah Mu'akkadah al-Kifayah (a communal emphasized Sunnah). If at least one person in a locality or congregation observes it, the obligation is lifted from the community; if no one observes it, all bear the shortfall of neglecting this Sunnah.

Continuous Practice of the Prophet ﷺ:
Mother of the Believers Aisha (may Allah be pleased with her) narrated:
"The Prophet ﷺ used to practice Itikaf during the last ten days of Ramadan until he passed away, and his wives used to practice Itikaf after him." [Sahih al-Bukhari: 2026, Sahih Muslim: 1172]

Tremendous Virtues of Itikaf:
1. Guaranteeing Laylatul Qadr: Because the Night of Decree occurs during the odd nights of the last ten days, the person staying in the mosque is practically guaranteed to spend that blessed night in worship.
2. Protection from Sins: In seclusion, a worshiper is shielded from backbiting, frivolous talk, visual fitnah, and worldly transgressions.
3. Continuous Rewarding for Missed Good Deeds:
Ibn Abbas (may Allah be pleased with them) narrated that the Messenger of Allah ﷺ said regarding the one who observes Itikaf:
"He is restrained from sins and he is given a reward like the one who does all good deeds." [Sunan Ibn Majah: 1781]
4. Distance from Hellfire:
The Prophet ﷺ said:
"Whoever observes Itikaf for one day seeking Allah's pleasure, Allah will place between him and the Fire three trenches, each trench wider than the distance between East and West." [Al-Mustadrak: 1628]""",
        "reference_bn": "সহীহ বুখারী: ২০২৬; সহীহ মুসলিম: ১১৭২; সুনানে ইবনে মাজাহ: ১৭৮১; মুসতাদরাক হাকিম: ১৬২৮",
        "reference_en": "Sahih al-Bukhari: 2026; Sahih Muslim: 1172; Sunan Ibn Majah: 1781; Mustadrak al-Hakim: 1628"
    },
    {
        "id": 3,
        "slug": "types_of_itikaf",
        "order": 3,
        "title_bn": "ইতিকাফের ধরন",
        "title_en": "Types of Itikaf",
        "card_title_bn": "ইতিকাফের ধরন",
        "card_title_en": "Types of Itikaf",
        "preview_bn": "ইসলামী শরীয়তে ইতিকাফ মূলত তিন প্রকার: ওয়াজিব ইতিকাফ (মানতের ইতিকাফ), সুন্নাতে মুয়াক্কাদাহ (রমাদ্বানের শেষ দশ দিনের ইতিকাফ) এবং মুস্তাহাব বা নফল ইতিকাফ (বছরের যে কোনো সময় যেকোনো মেয়াদের জন্য মসজিদে অবস্থানের ইতিকাফ)।",
        "preview_en": "Islamic jurisprudence categorizes Itikaf into three main types: Wajib (obligatory due to a vow/nadhr), Sunnah Mu'akkadah (the last ten days of Ramadan), and Nafl / Mustahabb (voluntary seclusion at any time of the year).",
        "details_bn": """ইতিকাফের তিন প্রকারের বিস্তারিত বিবরণ:

১. ওয়াজিব ইতিকাফ (المُعْتَكَفُ الوَاجِبُ):
এটি হলো মানতের ইতিকাফ। কোনো ব্যক্তি যদি মানত করে—"আমার এই কাজ পূর্ণ হলে আমি আল্লাহর জন্য এত দিন ইতিকাফ করব" অথবা কোনো শর্তহীনভাবেই মানত করল যে সে ইতিকাফ করবে, তবে তা পূর্ণ করা শরীয়তের দৃষ্টিতে ওয়াজিব বা আবশ্যকীয় হয়ে যায়।
- বিধান: মানতকৃত নির্দিষ্ট সময়সীমা পূরণ করা আবশ্যক।
- রোজা রাখার শর্ত: ওয়াজিব ইতিকাফ সহীহ হওয়ার জন্য ইতিকাফকারীকে রোজা অবস্থায় থাকতে হবে। রোজা ভঙ্গ হলে বা না রাখলে সেই দিনের ইতিকাফ কাজা করতে হবে।

২. সুন্নাতে মুয়াক্কাদাহ আলাল কিফায়াহ (السُّنَّةُ المُؤَكَّدَةُ):
রমাদ্বানের শেষ দশকে (২০ রমাদ্বান সূর্যাস্তের পূর্ব থেকে ঈদের চাঁদ দেখা পর্যন্ত) মসজিদে অবস্থান করা।
- সময়সীমা: ২০ রমাদ্বানের সূর্য অস্ত যাওয়ার আগ মুহূর্তে মসজিদে প্রবেশ করতে হয় এবং ২৯ বা ৩০ রমাদ্বানের সূর্যাস্তের পর ঈদের চাঁদ দেখা প্রমাণিত হলে মসজিদ থেকে বের হওয়া যায়।
- বিধান: মহল্লায় অন্তত একজন পালন করলে বাকিরা দায়িত্বমুক্ত হবেন।

৩. নফল বা মুস্তাহাব ইতিকাফ (النَّفْلُ أَوِ المُسْتَحَبُّ):
ওয়াজিব ও রমাদ্বানের শেষ দশক ব্যতীত অন্য যেকোনো সময় ইতিকাফের নিয়তে মসজিদে অবস্থান করাকে নফল ইতিকাফ বলা হয়।
- সময়সীমা: এর জন্য কোনো নির্দিষ্ট ন্যূনতম দিনক্ষণ নির্ধারিত নেই। যেকোনো সময়—কয়েক মিনিট, কয়েক ঘণ্টা বা কয়েক দিনের জন্য মসজিদে প্রবেশকালে নফল ইতিকাফের নিয়ত করা যায়।
- রোজা রাখার শর্ত: নফল ইতিকাফের জন্য রোজা থাকা বাধ্যতামূলক নয়। মসজিদে থাকা অবস্থায় যতক্ষণ সে ইতিকাফের নিয়তে থাকবে, ততক্ষণ সে নামাজ ও ইবাদতের সওয়াব পেতে থাকবে।""",
        "details_en": """The Three Categories of Itikaf Explained:

1. Wajib (Obligatory Itikaf):
This occurs when a person makes a solemn vow (Nadhr) to Allah, such as: "If Allah fulfills my need, I will observe Itikaf for X days," or makes an unconditional pledge to seclude themselves.
- Ruling: Fulfilling the vowed period is legally mandatory.
- Fasting Requirement: Fasting is an essential condition for Wajib Itikaf. If broken, it must be made up (Qadha).

2. Sunnah Mu'akkadah al-Kifayah:
This takes place exclusively during the last ten nights of Ramadan, commencing just before sunset on the 20th of Ramadan and concluding upon the sighting of the Eid crescent.
- Ruling: Communal responsibility. If fulfilled by one in a community, the Sunnah requirement is discharged for the entire area.

3. Nafl / Mustahabb (Voluntary Itikaf):
Entering the mosque with the intention of seclusion at any time outside the two aforementioned categories.
- Duration: No minimum duration is strictly mandated; one may make the intention for a few hours or several days.
- Fasting Requirement: Fasting is not a mandatory prerequisite for voluntary Itikaf.""",
        "reference_bn": "আল-ফিকহুল ইসলামি ওয়া আদিল্লাতুহু; ফতোয়ায়ে হিন্দিয়া: খণ্ড ১, পৃষ্ঠা ২১১; রদ্দুল মুহতার: খণ্ড ২, পৃষ্ঠা ৪৪০",
        "reference_en": "Al-Fiqh al-Islami wa Adillatuhu; Fatawa al-Hindiyyah: Vol 1, p. 211; Radd al-Muhtar: Vol 2, p. 440"
    },
    {
        "id": 4,
        "slug": "rules_conditions_itikaf",
        "order": 4,
        "title_bn": "ইতিকাফের নিয়ম ও শর্তাবলি",
        "title_en": "Rules and Conditions of Itikaf",
        "card_title_bn": "ইতিকাফের নিয়ম ও শর্তাবলি",
        "card_title_en": "Rules and Conditions of Itikaf",
        "preview_bn": "ইতিকাফ সহীহ হওয়ার জন্য কিছু মৌলিক শর্ত ও নিয়মাবলী পালন অপরিহার্য। এর মধ্যে রয়েছে: মুসলিম হওয়া, বালেগ বা সুস্থ মস্তিষ্ক হওয়া, নিয়ত করা, পবিত্র জামে মসজিদে অবস্থান করা এবং পবিত্র অবস্থায় থাকা। বিনা প্রয়োজনে মসজিদ থেকে বের হলে ইতিকাফ ভেঙে যায়।",
        "preview_en": "Validating Itikaf requires fundamental conditions: being Muslim, of sound mind, forming sincere intention, staying in a mosque where congregational prayers are held, and maintaining physical purity. Leaving without necessity invalidates it.",
        "details_bn": """ইতিকাফ সহীহ হওয়ার মৌলিক শর্তসমূহ:
১. ইসলাম ও ঈমান: ইতিকাফকারীকে অবশ্যই মুসলিম হতে হবে।
২. বিবেকবান হওয়া: পাগল বা অপ্রকৃতিস্থ ব্যক্তির ইতিকাফ সহীহ নয়।
৩. নিয়ত করা (الإخلاص والنية): মনে মনে আল্লাহর সন্তুষ্টির জন্য ইতিকাফের সুনির্দিষ্ট নিয়ত করতে হবে।
৪. পবিত্রতা: জানাবাত (ফরজ গোসল আবশ্যককারী অপবিত্রতা), হায়েয ও নেফাস থেকে পবিত্র হওয়া।
৫. সঠিক স্থান (মসজিদ): এমন মসজিদে ইতিকাফ হতে হবে যেখানে নিয়মিত আজান ও পাঁচ ওয়াক্তের জামাত অনুষ্ঠিত হয়। যে মসজিদে জুমার নামাজ হয়, সেখানে শেষ দশকের ইতিকাফ করা সর্বোত্তম।

ইতিকাফে মসজিদ থেকে বের হওয়ার শরয়ী বিধান:
ইতিকাফকালীন বিনা ওজরে মসজিদের সীমানা থেকে বের হওয়া সম্পূর্ণ নিষিদ্ধ। তবে নির্দিষ্ট কিছু জরুরি প্রয়োজনে বের হওয়া বৈধ:
- প্রাকৃতিক প্রয়োজন (হালাতে তাবিয়্যাহ): প্রস্রাব, পায়খানা ও নাপাকির কারণে ফরজ গোসলের জন্য বের হওয়া বৈধ।
- শরয়ী প্রয়োজন (হালাতে শরইয়্যাহ): যে মসজিদে ইতিকাফ করছেন সেখানে জুমার জামাত না হলে জুমার সালাত আদায় করতে নিকটবর্তী জামে মসজিদে যাওয়া বৈধ।
- জরুরী প্রয়োজন: মসজিদ ভেঙে পড়ার উপক্রম হলে বা জানমালের চরম হুমকি দেখা দিলে অন্য মসজিদে স্থানান্তরিত হওয়া।

ইতিকাফ ভেঙে যাওয়ার কারণসমূহ:
১. বিনা প্রয়োজনে ইচ্ছাকৃতভাবে মসজিদের সীমানা অতিক্রম করা (এমনকি কয়েক মুহূর্তের জন্য হলেও)।
২. সহবাস বা যৌন সম্ভোগে লিপ্ত হওয়া (সূরা বাকারা: ১৮৭)।
৩. বেহুশ, পাগল বা জ্ঞানশূন্যতা দীর্ঘস্থায়ী হওয়া।
৪. ধর্মত্যাগ (আল্লাহ হেফাজত করুন) বা হায়েয-নেফাস শুরু হওয়া।
৫. রমাদ্বানের রোজা ভেঙে ফেলা।""",
        "details_en": """Prerequisites for a Valid Itikaf:
1. Islam: The person must be a Muslim.
2. Sanity: The individual must possess sound judgment and intellect.
3. Intention (Niyyah): Sincere dedication to observe seclusion for Allah's sake alone.
4. Ritual Purity: Freedom from major ritual impurity (Janabah) as well as menses (Hayd) and postnatal bleeding (Nifas).
5. Legitimate Mosque Venue: Seclusion must be within a mosque where regular congregational prayers are established, ideally where Jumu'ah is held.

Permissible Reasons to Exit the Mosque:
Exiting the mosque borders without legitimate excuse is strictly prohibited and invalidates the Itikaf. Legitimate exceptions include:
- Natural Necessities: Using the lavatory, relieving oneself, or performing obligatory Ghusl.
- Religious Necessities: Traveling to another mosque to perform the Jumu'ah prayer if it is not held in the current mosque.
- Emergency Necessities: Immediate danger to life, fire, or structural collapse of the mosque.

Actions that Invalidate Itikaf:
1. Stepping outside the mosque boundary without legitimate necessity, even momentarily.
2. Sexual intimacy or relations (Surah Al-Baqarah: 187).
3. Long-term unconsciousness or insanity.
4. Onset of menstruation or postpartum bleeding.
5. Intentionally breaking the fast during Ramadan.""",
        "reference_bn": "সূরা আল-বাকারা: ১৮৭; সহীহ বুখারী: ২০৩৮, ২০৩৯; সহীহ মুসলিম: ২৯৭",
        "reference_en": "Surah Al-Baqarah: 187; Sahih al-Bukhari: 2038, 2039; Sahih Muslim: 297"
    },
    {
        "id": 5,
        "slug": "recommended_actions_itikaf",
        "order": 5,
        "title_bn": "ইতিকাফের সময়ে করণীয় কাজ",
        "title_en": "Recommended Actions During Itikaf",
        "card_title_bn": "ইতিকাফের সময়ে করণীয় কাজ",
        "card_title_en": "Recommended Actions During Itikaf",
        "preview_bn": "ইতিকাফের মূল লক্ষ্য হলো দুনিয়ার ঝামেলা থেকে মুক্ত হয়ে আল্লাহর দরবারে সার্বক্ষণিক ইবাদতে নিয়োজিত থাকা। এ সময় কুরআন তিলাওয়াত, নফল নামাজ, তাহাজ্জুদ, জিকির-আসকার, সালাতুত তাসবীহ, দোয়া ও ইলম অর্জনে আত্মনিয়োগ করা সর্বোচ্চ সওয়াবের কাজ।",
        "preview_en": "The core objective of Itikaf is total devotion to Allah away from worldly turmoil. Spending time in Quran recitation, voluntary prayers, Tahajjud, Dhikr, Salatul Tasbeeh, Duas, and seeking beneficial Islamic knowledge yields the highest reward.",
        "details_bn": """ইতিকাফের দিনগুলোতে সর্বোত্তম ইবাদত ও আমলসমূহ:

১. কুরআন তিলাওয়াত ও তাদাব্বুর:
ইতিকাফে কুরআনের সাথে গভীর বন্ধন গড়ে তোলার শ্রেষ্ঠ সুযোগ। প্রতিদিন অর্থ ও তাফসীরসহ কুরআন তিলাওয়াত করা এবং আয়াতসমূহের মর্ম অনুধাবন করা।

২. কিয়ামুল লাইল ও তাহাজ্জুদের নামাজ:
রাতের গভীর নীরবতায় কিয়ামুল লাইলে দীর্ঘ সিজদা এবং রুকু আদায় করা। নবীজী ﷺ শেষ দশকে সারা রাত জাগ্রত থাকতেন এবং ইবাদতের জন্য কোমর বেঁধে নামতেন।

৩. সার্বক্ষণিক জিকির ও তাসবীহ:
সুবহানাল্লাহ, আলহামদুলিল্লাহ, লা ইলাহা ইল্লাল্লাহ, আল্লাহু আকবার, লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ অধিক পরিমাণে পাঠ করা।

৪. লাইলাতুল কদরের বিশেষ দোয়া:
রাসুলুল্লাহ ﷺ আয়েশা (রাদ্বিয়াল্লাহু আনহা)-কে শিখিয়েছিলেন:
«اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي»
(হে আল্লাহ! নিশ্চয়ই আপনি ক্ষমাশীল, ক্ষমা পছন্দ করেন; অতএব আমাকে ক্ষমা করে দিন।) [জামে আত-তিরমিযী: ৩৫১৩]

৫. তাওবা, ইস্তিগফার ও কান্নাকাটি:
নিভৃতে অতীত জীবনের গুনাহের জন্য লজ্জিত হয়ে আল্লাহর কাছে কায়মনোবাক্যে মাগফিরাত প্রার্থনা করা।

৬. দ্বীনি ইলম অর্জন ও জ্ঞানচর্চা:
সহীহ হাদিসের কিতাব, নবী-রাসুলগণের জীবনী (সীরাত), এবং মাসআলা-মাসায়েল অধ্যয়ন করা।

বর্জনীয় ও নিরুৎসাহিত বিষয়:
- মোবাইল ফোন ও সোশ্যাল মিডিয়ার অপ্রয়োজনীয় ব্যবহার থেকে সম্পূর্ণ বিরত থাকা।
- মসজিদে অনর্থক গল্পগুজব, হাসিঠাট্টা ও রাজনৈতিক তর্কবিতর্ক পরিহার করা।
- কেনাবেচা ও ব্যবসা-বাণিজ্যের আলোচনা মসজিদে না করা।""",
        "details_en": """Virtuous Actions and Daily Routine During Itikaf:

1. Recitation and Contemplation of the Quran:
Deeply engaging with the Word of Allah, reflecting on its meanings (Tadabbur), and studying reliable Tafsir.

2. Qiyam al-Layl and Tahajjud Prayers:
Spending the quiet hours of the night in prolonged bowing and prostration. The Prophet ﷺ spent the entire nights of the last ten days in vigorous devotion.

3. Constant Remembrance and Dhikr:
Frequently glorifying Allah with SubhanAllah, Alhamdulillah, La ilaha illallah, Allahu Akbar, and sending Salawat upon the Prophet ﷺ.

4. The Special Supplication of Laylatul Qadr:
The Prophet ﷺ instructed Aisha (may Allah be pleased with her) to supplicate:
"Allahumma innaka 'afuwwun tuhibbul-'afwa fa'fu 'anni"
(O Allah, You are Forgiving and love forgiveness, so forgive me.) [Jami' at-Tirmidhi: 3513]

5. Sincere Repentance (Tawbah and Istighfar):
Weeping in seclusion, confessing faults, and asking Allah for salvation and mercy.

6. Seeking Sacred Islamic Knowledge:
Studying Sahih Hadith, the Prophetic biography (Seerah), and essential jurisprudence.

Detrimental Habits to Strictly Avoid:
- Unnecessary smartphone scrolling, social media distractions, and chatting.
- Engaging in idle gossip, humorous chatter, or political debates inside the sanctuary.
- Commercial discussions or business dealings within the mosque.""",
        "reference_bn": "জামে আত-তিরমিযী: ৩৫১৩; সহীহ বুখারী: ২০২৪; সহীহ মুসলিম: ১১৭৪",
        "reference_en": "Jami' at-Tirmidhi: 3513; Sahih al-Bukhari: 2024; Sahih Muslim: 1174"
    },
    {
        "id": 6,
        "slug": "women_and_itikaf",
        "order": 6,
        "title_bn": "ইতিকাফ এবং মহিলারা",
        "title_en": "Women and Itikaf",
        "card_title_bn": "ইতিকাফ এবং মহিলারা",
        "card_title_en": "Women and Itikaf",
        "preview_bn": "মহিলাদের জন্যও ইতিকাফ অত্যন্ত সওয়াব ও বরকতময় একটি আমল। ফিকহে হানাফী অনুযায়ী মহিলারা নিজেদের ঘরের নির্দিষ্ট নামাজের স্থানে (মাকামুস সালাত) ইতিকাফ করবেন। অন্যান্য ফিকহ মতে মসজিদের নিরাপদ ও পর্দাবিশিষ্ট স্থানেও অনুমতিসাপেক্ষে ইতিকাফ করা যায়।",
        "preview_en": "Itikaf is also a highly rewarded worship for women. According to the Hanafi school, women observe Itikaf within a dedicated prayer space in their homes. Other schools allow it in mosques provided complete privacy, safety, and permission exist.",
        "details_bn": """মহিলাদের ইতিকাফের বিধান ও শরয়ী প্রেক্ষাপট:
উম্মুল মুমিনীন আয়েশা (রাদ্বিয়াল্লাহু আনহা) বর্ণনা করেন যে, রাসুলুল্লাহ ﷺ-এর জীবদ্দশায় এবং তাঁর ওফাতের পর তাঁর পবিত্র স্ত্রীগণও ইতিকাফ পালন করেছেন। [সহীহ বুখারী: ২০২৬]

মহিলাদের ইতিকাফের স্থান সংক্রান্ত মতভেদ:
১. হানাফী ফিকহের অভিমত:
মহিলাদের জন্য নিজ ঘরের নামাজের স্থান বা ঘরের একটি নির্দিষ্ট পরিচ্ছন্ন কক্ষ হলো ইতিকাফের সর্বশ্রেষ্ঠ স্থান।
- কারণ: পর্দা রক্ষা, ঘরের সার্বিক নিরাপত্তা এবং গৃহস্থালির ফিতনা থেকে নিরাপদ থাকার জন্য ঘরই নারীর জন্য উত্তম।
- ঘরের বাইরে যাওয়া: ঘরের নির্ধারিত সীমানা থেকে বিনা প্রয়োজনে (যেমন শৌচাগার ছাড়া) বের হলে ইতিকাফ ভেঙে যাবে।

২. অন্যান্য ইমামগণের (শাফেঈ, মালেকী, হাম্বলী) অভিমত:
তাদের মতে ইতিকাফ কেবল মসজিদেই হতে হবে। তবে মহিলাদের মসজিদে ইতিকাফের জন্য কঠোর শর্ত প্রযোজ্য:
- স্বামীর বা অভিভাবকের সুস্পষ্ট অনুমতি থাকতে হবে।
- মসজিদের অভ্যন্তরে নারীদের সম্পূর্ণ পৃথক, নিরাপদ ও পর্দানশীন ব্যবস্থা থাকতে হবে।
- কোনো প্রকার ফিতনা বা নিরাপত্তার সংশয় থাকা চলবে না।

মহিলাদের জন্য বিশেষ সতর্কতামূলক নিয়মাবলী:
১. হায়েয ও নেফাস: ইতিকাফ অবস্থায় ঋতুস্রাব (হায়েয) শুরু হলে ইতিকাফ তাৎক্ষণিকভাবে ভেঙে যাবে। পরবর্তীতে পবিত্র হওয়ার পর কেবল সেই দিনের রোজা ও ইতিকাফ কাজা করতে হবে।
২. পারিবারিক দায়িত্ব: ছোট দুধের শিশু বা পরিবারে বিশেষ অসুস্থ ব্যক্তির সেবা থাকলে তাদের হক নষ্ট করে ইতিকাফ করা বাঞ্ছনীয় নয়।""",
        "details_en": """Islamic Perspective on Women's Itikaf:
Aisha (may Allah be pleased with her) reported that the wives of the Prophet ﷺ used to observe Itikaf during the Prophet's lifetime and continued doing so after his demise. [Sahih al-Bukhari: 2026]

Jurisprudential Views on Venue:
1. The Hanafi View:
The most recommended and valid place for a woman's Itikaf is a designated prayer area (Musalla) within her own home.
- Wisdom: Ensures privacy, preserves modest boundaries, avoids disruption of household duties, and guards against trials.
- Boundary: She remains within her designated room and only steps out for natural necessities like the bathroom.

2. Majority Juristic View (Shafi'i, Maliki, Hanbali):
They hold that Itikaf is fundamentally observed in a mosque, subject to strict conditions:
- Explicit permission from her husband or guardian.
- Completely secluded, private, and secure quarters designated exclusively for women.
- Complete absence of temptation, compromise, or safety risks.

Crucial Guidelines for Female Worshippers:
1. Menses and Postpartum Bleeding: The onset of menstruation invalidates the Itikaf immediately. Only the remaining incomplete day needs to be made up after Ramadan.
2. Family Responsibilities: If caring for infants or nursing sick dependents is neglected, taking care of one's obligations takes spiritual precedence.""",
        "reference_bn": "সহীহ বুখারী: ২০২৬, ২০৩৩; সহীহ মুসলিম: ১১৭২; ফতোয়ায়ে হিন্দিয়া: খণ্ড ১, পৃষ্ঠা ২১১",
        "reference_en": "Sahih al-Bukhari: 2026, 2033; Sahih Muslim: 1172; Fatawa al-Hindiyyah: Vol 1, p. 211"
    },
    {
        "id": 7,
        "slug": "itikaf_and_modern_age",
        "order": 7,
        "title_bn": "ইতিকাফ এবং আধুনিক যুগ",
        "title_en": "Itikaf and the Modern Age",
        "card_title_bn": "ইতিকাফ এবং আধুনিক যুগ",
        "card_title_en": "Itikaf and the Modern Age",
        "preview_bn": "আধুনিক প্রযুক্তিময় ও ব্যস্ত যুগে ইতিকাফ মানসিক শান্তি এবং আত্মিক প্রশান্তির এক অনন্য আশ্রয়স্থল। ডিজিটাল ডিভাইস ও সোশ্যাল মিডিয়ার আসক্তি থেকে দূরে সরে এসে কয়েক দিন আল্লাহর সান্নিধ্যে নিভৃতে কাটানো বর্তমান সময়ের মানুষের জন্য পরম প্রয়োজনীয় এক আধ্যাত্মিক ডিটক্স।",
        "preview_en": "In our fast-paced technological era, Itikaf provides a sublime spiritual detox. Disconnecting from screens, endless notifications, and social pressures to spend uninterrupted days in the Divine presence rejuvenates the exhausted modern soul.",
        "details_bn": """আধুনিক যুগে ইতিকাফের অনন্য প্রাসঙ্গিকতা:

১. ডিজিটাল ডিটক্স (Digital Detoxification):
বর্তমান মানুষ প্রতিনিয়ত সোশ্যাল মিডিয়া, মেসেজিং অ্যাপ, নিউজ পোর্টাল ও স্মার্টফোনের নোটিফিকেশন ঝড়ে মানসিকভাবে ক্লান্ত। ইতিকাফ মানুষের মনকে এই ক্রমাগত বিক্ষিপ্ততা থেকে মুক্ত করে অন্তরে গভীর প্রশান্তি (সাকীনাহ) এনে দেয়।

২. মানসিক চাপ ও ডিপ্রেশন থেকে মুক্তি:
আধুনিক জীবনের অন্যতম ব্যাধি হলো মানসিক অস্থিরতা ও একাকীত্ব। মসজিদে সমমনা মুমিনদের সাথে আল্লাহর ইবাদতে রত থাকলে মনের হতাশা দূর হয় এবং পরম স্রষ্টার ওপর অবিচল তাওয়াক্কুল তৈরি হয়।

আধুনিক সময়ে ইতিকাফকারীদের জন্য বিশেষ সতর্কতা:
১. মোবাইল ফোনের নিয়ন্ত্রণ:
ইতিকাফে স্মার্টফোন সাথে থাকলে সবচেয়ে বড় ক্ষতি হয়। ফোন সম্পূর্ণ বন্ধ রাখা অথবা খুব জরুরি কাজের জন্য কেবল বাটন ফোন ব্যবহার করা উচিত।
২. কাজের ছুটি ও পরিকল্পনা:
কর্মজীবী ও চাকরিজীবীদের উচিত আগে থেকেই রমাদ্বানের শেষ দশকে বার্ষিক ছুটি বা ছুটির ব্যবস্থা করে নেওয়া, যাতে কাজের চাপ ইতিকাফের একাগ্রতা নষ্ট না করে।
৩. সময়ের সর্বোচ্চ সদ্ব্যবহার:
মসজিদে এসি বা সুযোগ-সুবিধা থাকলেও অলসতা করে শুয়ে-বসে সময় নষ্ট না করে প্রতিদিনের জন্য একটি সুনির্দিষ্ট রুটিন (তিলাওয়াত, নামাজ, জিকির, দোয়া ও বিশ্রামের সময়সূচি) মেনে চলা।""",
        "details_en": """Significance of Itikaf in Contemporary Society:

1. Digital and Sensory Detoxification:
Modern individuals suffer from cognitive overload due to constant notifications, short-form media, and social hyper-connectivity. Itikaf offers an unparalleled retreat to reset cognitive focus and rediscover inner tranquility (Sakinah).

2. Relieving Chronic Anxiety and Existential Stress:
The relentless rat race often leads to spiritual emptiness and burnout. Communal seclusion alongside fellow sincere believers nurtures deep contentment and reliance upon Allah (Tawakkul).

Practical Guidelines for Contemporary Worshippers:
1. Smartphone Management:
A smartphone is the single greatest hazard to Itikaf. Keep it switched off or delegate communication to an emergency-only keypad device.
2. Advance Leave Planning:
Professionals and employees should reserve annual leaves for the last ten days of Ramadan to safeguard uninterrupted spiritual focus.
3. Structured Routine:
Do not squander seclusion in endless sleeping or idle discussions; design a strict daily schedule balancing Quranic recitation, night prayers, contemplation, and restorative rest.""",
        "reference_bn": "সূরা আল-আলা: ১৪-১৫; জামে আত-তিরমিযী: ২৪৫৮",
        "reference_en": "Surah Al-A'la: 14-15; Jami' at-Tirmidhi: 2458"
    },
    {
        "id": 8,
        "slug": "mistakes_precautions_itikaf",
        "order": 8,
        "title_bn": "ইতিকাফের ভুল ও সতর্কতা",
        "title_en": "Mistakes and Precautions in Itikaf",
        "card_title_bn": "ইতিকাফের ভুল ও সতর্কতা",
        "card_title_en": "Mistakes and Precautions in Itikaf",
        "preview_bn": "ইতিকাফ চলাকালীন অনেক সময় অজ্ঞতাবশত এমন কিছু কাজ হয়ে যায় যার কারণে মূল্যবান ইবাদতের সওয়াব নষ্ট হয় বা ইতিকাফ বাতিল হয়ে যায়। বিনা প্রয়োজনে মসজিদের বাইরে যাওয়া, অনর্থক আড্ডা দেওয়া, ফোনে সময় নষ্ট করা এবং পরিচ্ছন্নতা রক্ষা না করা সাধারণ ভুলের অন্তর্ভুক্ত।",
        "preview_en": "During Itikaf, lack of awareness sometimes leads to errors that diminish spiritual rewards or even nullify the seclusion. Stepping outside without legitimate necessity, excessive socialization, and neglecting hygiene are common pitfalls.",
        "details_bn": """ইতিকাফে সচরাচর সংঘটিত ভুলসমূহ:

১. বিনা প্রয়োজনে মসজিদের বাইরে কদম রাখা:
অনেকে অজু বা শৌচাগারে যাওয়ার অজুহাতে বাইরে এসে রাস্তায় দাঁড়িয়ে আত্মীয়স্বজনের সাথে কথা বলেন বা দোকানে যান। মনে রাখতে হবে, জরুরি কাজের পর এক মুহূর্তও মসজিদের সীমানার বাইরে বিলম্ব করলে ইতিকাফ নষ্ট হয়ে যায়।

২. মসজিদে খোশগল্প ও আড্ডায় মত্ত হওয়া:
ইতিকাফে দলবদ্ধ হয়ে বসে দেশ-দুনিয়ার রাজনীতি, ব্যবসা বা পরিবারের ব্যক্তিগত বিষয়ে গল্প করা মারাত্মক ভুল। রাসুলুল্লাহ ﷺ বলেছেন, মসজিদে অনর্থক কথাবার্তা নেক আমলকে এমনভাবে ধ্বংস করে যেমন আগুন কাঠকে ভস্মীভূত করে।

৩. অতিরিক্ত মোবাইল ব্যবহার:
ফেসবুক, ইউটিউব বা বার্তা আদান-প্রদান করতে করতে তাহাজ্জুদ ও তিলাওয়াতের সময় নষ্ট করা ইতিকাফের মূল উদ্দেশ্যের পরিপন্থী।

৪. পরিচ্ছন্নতার প্রতি অবহেলা:
মসজিদে অবস্থানকালে নিজের শরীর, বিছানা ও কাপড় পরিচ্ছন্ন না রাখা, দুর্গন্ধযুক্ত খাবার খেয়ে মসজিদে অন্যদের কষ্ট দেওয়া বা অজুখানায় বিশৃঙ্খলা সৃষ্টি করা গুনাহের কাজ।

৫. মাত্রাতিরিক্ত খাদ্য গ্রহণ ও ঘুমানো:
সেহরি ও ইফতারে অতিভোজনের কারণে অলসতা ভর করে, ফলে রাতে দীর্ঘ সময় দাঁড়িয়ে তাহাজ্জুদ আদায় করা অসম্ভব হয়ে পড়ে।""",
        "details_en": """Common Mistakes and Misconceptions:

1. Loitering Outside Mosque Premises:
Some participants exit for the restroom and linger outside chatting with acquaintances or visiting shops. Lingering beyond the exact necessity even for a brief moment nullifies the Itikaf.

2. Idle Chitchat and Group Socializing:
Turning the mosque into a social lounge to discuss politics, business, or sports undermines the entire sanctity of seclusion.

3. Unchecked Device and Internet Usage:
Browsing social media or texting under the blanket robs the soul of valuable nocturnal worship and Quranic contemplation.

4. Neglect of Personal and Environmental Hygiene:
Failing to maintain bodily purity, emitting unpleasant odors, or cluttering the mosque premises creates discomfort for other worshippers.

5. Overeating and Excessive Slumber:
Overindulgence during Iftar and Suhoor induces lethargy, crippling one's stamina for extensive Night Prayers (Qiyam al-Layl).""",
        "reference_bn": "সহীহ বুখারী: ২০৩৮; সুনানে আবু দাউদ: ২৪৭৩; ফতোয়ায়ে উসমানী: খণ্ড ২, পৃষ্ঠা ১৮০",
        "reference_en": "Sahih al-Bukhari: 2038; Sunan Abi Dawud: 2473; Fatawa Uthmani: Vol 2, p. 180"
    },
    {
        "id": 9,
        "slug": "history_examples_itikaf",
        "order": 9,
        "title_bn": "ইতিকাফের ইতিহাস ও উদাহরণ",
        "title_en": "History and Examples of Itikaf",
        "card_title_bn": "ইতিকাফের ইতিহাস ও উদাহরণ",
        "card_title_en": "History and Examples of Itikaf",
        "preview_bn": "ইতিকাফ কেবল উম্মতে মুহাম্মাদীর মধ্যেই সীমাবদ্ধ ছিল না, বরং অতীত নবীদের যুগেও এর প্রচলন ছিল। হযরত ইব্রাহীম ও ইসমাইল (আলাইহিমাস সালাম)-কে পবিত্র কাবাঘর ইতিকাফকারীদের জন্য প্রস্তুত রাখার নির্দেশ দেওয়া হয়েছিল। সালাফে সালেহীন কীভাবে ইতিকাফ পালন করতেন তার স্বর্ণালী ইতিহাস রয়েছে।",
        "preview_en": "Itikaf was not confined solely to the Ummah of Prophet Muhammad ﷺ; it dates back to previous Messengers. Allah commanded Ibrahim and Ismail (peace be upon them) to purify the Ka'bah for those performing Itikaf, demonstrating its timeless heritage.",
        "details_bn": """প্রাচীন নবীগণের যুগে ইতিকাফ:
পবিত্র কুরআনে আল্লাহ তাআলা হযরত ইব্রাহীম ও হযরত ইসমাইল (আলাইহিমাস সালাম)-কে লক্ষ্য করে ইরশাদ করেন:
﴿ وَطَهِّرَا بَيْتِيَ لِلطَّائِفِينَ وَالْعَاكِفِينَ وَالرُّكَّعِ السُّجُودِ ﴾
"এবং তোমরা আমার গৃহকে তাওয়াফকারী, ইতিকাফকারী এবং রুকু ও সিজদাকারীদের জন্য পবিত্র রাখো।" [সূরা আল-বাকারা: ১২৫]
এর দ্বারা প্রমাণিত হয় যে, হাজার হাজার বছর পূর্বেও খাঁটি তাওহীদপন্থী বান্দাদের জন্য ইতিকাফের পবিত্র বিধান বিদ্যমান ছিল।

রাসুলুল্লাহ ﷺ-এর যুগে ইতিকাফের স্বর্ণালী ইতিহাস:
মদীনায় হিজরতের পর রাসুলুল্লাহ ﷺ প্রথম রমাদ্বানে প্রথম দশ দিন ইতিকাফ করেন, অতপর মধ্যম দশ দিন এবং পরবর্তীতে জিবরীল (আলাইহিস সালাম)-এর মারফতে জানতে পারেন যে লাইলাতুল কদর শেষ দশকে নিহিত। অতঃপর তিনি আমৃত্যু শেষ দশ দিনেই ইতিকাফ পালন করেন।
জীবনের শেষ রমাদ্বানে রাসুলুল্লাহ ﷺ ২০ দিন ইতিকাফ পালন করেছিলেন। [সহীহ বুখারী: ২০৪৪]

সালাফে সালেহীন ও আইম্মায়ে কেরামের ইতিকাফ:
১. ইমাম জুহরী (রহিমাহুল্লাহ) আফসোস করে বলতেন:
"মানুষের ব্যাপারে বড়ই বিস্ময় জাগে যে তারা কেন ইতিকাফ ছেড়ে দেয়! অথচ রাসুলুল্লাহ ﷺ অনেক নফল আমল মাঝে মাঝে করতেন আবার কখনো ছেড়ে দিতেন, কিন্তু মদীনায় আগমনের পর ওফাত পর্যন্ত তিনি কখনো ইতিকাফ ত্যাগ করেননি।"
২. হাসান বসরী ও ইমাম মালিক (রহিমাহুল্লাহ)-এর মতো মহান মনীষীগণ রমাদ্বানের শেষ দশকে জ্ঞানচর্চা ও পাঠদান স্থগিত রেখে সম্পূর্ণভাবে আল্লাহর ধ্যানে মসজিদে ইতিকাফে নিমগ্ন হতেন।""",
        "details_en": """Itikaf Across Past Prophetic Dispensations:
In the Glorious Quran, Allah commands Prophets Ibrahim and Ismail (peace be upon them):
"And purify My House for those who perform Tawaf and those who stay for seclusion and those who bow down and prostrate." [Surah Al-Baqarah: 125]
This confirms that spiritual seclusion in dedicated sanctuaries is a timeless sunnah rooted in ancient monotheistic heritage.

The Prophetic Model in Madinah:
Initially in Madinah, the Messenger of Allah ﷺ secluded himself for the first ten days, then the middle ten, seeking Laylatul Qadr. When Jibril (peace be upon him) informed him that the Night was in the final ten, he committed to the last ten nights for the rest of his life.
In the final Ramadan before his demise, the Prophet ﷺ observed Itikaf for twenty days. [Sahih al-Bukhari: 2044]

Inspiring Precedents from the Righteous Predecessors:
1. Imam Ibn Shihab az-Zuhri remarked in amazement:
"How strange are the people that they abandon Itikaf! The Messenger of Allah ﷺ sometimes performed a voluntary act and sometimes left it, yet he never once abandoned Itikaf from the time he entered Madinah until his death."
2. Luminaries such as Hasan al-Basri and Imam Malik would suspend public lectures and legal consultations during the final ten days to dedicate every heartbeat to communion with Allah.""",
        "reference_bn": "সূরা আল-বাকারা: ১২৫; সহীহ বুখারী: ২০৪৪; ফাতহুল বারী: খণ্ড ৪, পৃষ্ঠা ২৮৫",
        "reference_en": "Surah Al-Baqarah: 125; Sahih al-Bukhari: 2044; Fath al-Bari: Vol 4, p. 285"
    },
    {
        "id": 10,
        "slug": "preparation_for_itikaf",
        "order": 10,
        "title_bn": "ইতিকাফের প্রস্তুতি",
        "title_en": "Preparation for Itikaf",
        "card_title_bn": "ইতিকাফের প্রস্তুতি",
        "card_title_en": "Preparation for Itikaf",
        "preview_bn": "সফল ও আধ্যাত্মিকভাবে অর্থপূর্ণ ইতিকাফের জন্য পূর্বপ্রস্তুতি অত্যন্ত গুরুত্বপূর্ণ। মানসিক প্রস্তুতি, প্রয়োজনীয় ব্যবহার্য জিনিসপত্র গুছিয়ে নেওয়া, জরুরি যোগাযোগ সম্পন্ন করা এবং প্রতিদিনের আমল ও ইবাদতের স্পষ্ট সময়সূচি নির্ধারণ করা ইতিকাফকে ত্রুটিমুক্ত রাখে।",
        "preview_en": "Thorough preparation is indispensable for a deeply transformative Itikaf. Mental readiness, packing essential necessities, settling worldly affairs beforehand, and preparing a structured schedule ensure a tranquil seclusion.",
        "details_bn": """ইতিকাফের পূর্বে করণীয় গুরুত্বপূর্ণ প্রস্তুতিসমূহ:

১. মানসিক ও নিয়তগত প্রস্তুতি:
ইতিকাফে প্রবেশের পূর্বেই নিজের মনকে প্রস্তুত করতে হবে যে আগামী ১০ দিন আমি সম্পূর্ণভাবে দুনিয়াবি আসক্তি, ব্যবসা ও পরিবার থেকে বিচ্ছিন্ন হয়ে কেবল আল্লাহর সন্তুষ্টির সাধনায় নিমগ্ন থাকব।

২. সাংসারিক ও দাপ্তরিক দায়িত্ব সমাপন:
কর্মক্ষেত্রের ছুটি নিশ্চিত করা, পরিবারের জন্য প্রয়োজনীয় খরচ ও খাদ্যসামগ্রীর বন্দোবস্ত করে দেওয়া এবং জরুরি আত্মীয়দের জানিয়ে রাখা যাতে ইতিকাফকালীন অযথা দুশ্চিন্তায় পড়তে না হয়।

৩. প্রয়োজনীয় সামগ্রীর তালিকা প্রস্তুত করা:
- হালকা বিছানা, চাদর ও ছোট বালিশ।
- পরিষ্কার-পরিচ্ছন্ন ২/৩ সেট আরামদায়ক সুতি পোশাক।
- মেসওয়াক, টুথব্রাশ, সাবান, তোয়ালে ও প্রয়োজনীয় প্রেসক্রিপশনের ওষুধপত্র।
- নিজস্ব কুরআন শরীফ, সহীহ হাদিস ও দোয়ার কিতাব।

৪. প্রতিদিনের আমলের চার্ট বা রুটিন তৈরি:
২৪ ঘণ্টার দিন-রাতকে সুবিন্যস্ত ভাগে ভাগ করে নেওয়া:
- সাহরী ও ফজরের পূর্ববর্তী তাহাজ্জুদ ও কন্দনরত দোয়া।
- ফজর পরবর্তী ইশরাক পর্যন্ত কুরআন তিলাওয়াত ও সকালের মাসনুন আজকার।
- সকালের বিশ্রাম ও কায়লুলাহ (দুপুরের সামান্য ঘুম)।
- জোহর ও আসরের মধ্যবর্তী নফল নামাজ ও হাদিস অধ্যয়ন।
- আসর থেকে মাগরিব পর্যন্ত ইফতারের অপেক্ষায় একাকী অশ্রুসজল দোয়া।
- মাগরিব ও এশার পর তারাবীহ ও দীর্ঘ কিয়ামুল লাইল।""",
        "details_en": """Essential Steps in Preparing for Itikaf:

1. Mental and Spiritual Preparedness:
Condition your heart in advance to embrace ten days of voluntary simplicity, quiet contemplation, and complete emotional detachment from business and entertainment.

2. Settling Domestic and Professional Obligations:
Securing official leave from employment, providing sufficient household provisions for your family, and settling urgent commitments beforehand to prevent restlessness.

3. Packing Practical Necessities:
- Compact sleeping mat, light blanket, and pillow.
- Two to three sets of clean, comfortable, and modest clothing.
- Miswak/toothbrush, fragrance-free soap, towel, and prescribed personal medicines.
- A personal copy of the Holy Quran, reliable Hadith compilations, and books of authentic supplications.

4. Crafting a Disciplined Daily Schedule:
Distribute the 24 hours into productive spiritual blocks:
- Suhoor, intimate Tahajjud, and tearful pre-dawn Istighfar.
- Post-Fajr until Ishraq: Morning Adhkar and Quranic recitation.
- Mid-morning restorative rest and brief Sunnah nap (Qaylulah).
- Between Zuhr and Asr: Voluntary prayers and beneficial reading.
- Asr until Maghrib: Intense Dua and repentance before breaking the fast.
- Night: Taraweeh and extended midnight standing in Qiyam.""",
        "reference_bn": "সহীহ বুখারী: ২০২৬; যাদুল মা'আদ: খণ্ড ২, পৃষ্ঠা ৮৭",
        "reference_en": "Sahih al-Bukhari: 2026; Zad al-Ma'ad: Vol 2, p. 87"
    },
    {
        "id": 11,
        "slug": "teaching_itikaf_to_children",
        "order": 11,
        "title_bn": "বাচ্চাদের জন্য ইতিকাফ শেখানো",
        "title_en": "Teaching Itikaf to Children",
        "card_title_bn": "বাচ্চাদের জন্য ইতিকাফ শেখানো",
        "card_title_en": "Teaching Itikaf to Children",
        "preview_bn": "সন্তানদের অন্তরে মসজিদের ভালোবাসা এবং নিভৃত ইবাদতের অভ্যাস গড়ে তুলতে ছোটবেলা থেকেই বয়সোপযোগী করে ইতিকাফের শিক্ষা দেওয়া অত্যন্ত কার্যকরী। তাদের মসজিদে নিয়ে অল্প সময় অবস্থানের মাধ্যমে অভ্যাস করানো এবং ইতিকাফের তাৎপর্য বুঝিয়ে দ্বীনি আদর্শে গড়ে তোলা সম্ভব।",
        "preview_en": "Nurturing an enduring love for the mosque and secluded devotion begins in childhood. Introducing children to short, age-appropriate periods of seclusion and explaining the beauty of Itikaf instills deep spiritual connection from an early age.",
        "details_bn": """সন্তানদের ইতিকাফের চেতনায় অনুপ্রাণিত করার কৌশল:

১. পর্যায়ক্রমিক ও স্বল্পমেয়াদী অনুশীলন:
বাচ্চাদের ওপর শুরুতেই পুরো দশ দিনের কঠোর ইতিকাফ চাপিয়ে দেওয়া যাবে না। তাদেরকে জুমার দিনে কয়েক ঘণ্টার জন্য অথবা রমাদ্বানের কোনো একটি বিজোড় রাতে ইতিকাফের নিয়তে মসজিদে নিয়ে যাওয়া উচিত।

২. মসজিদের আদব ও শান্ত থাকার শিক্ষা:
মসজিদের পবিত্রতা রক্ষা, অন্যের নামাজে বিঘ্ন না ঘটানো, দৌড়াদৌড়ি না করা এবং নিচু স্বরে কথা বলার শিক্ষা আগে থেকেই দেওয়া।

৩. আগ্রহোদ্দীপক ইসলামিক গল্প ও পাঠদান:
ইতিকাফকালীন তাদেরকে নবীজী ﷺ ও সাহাবায়ে কেরামের বীরত্ব ও আত্মত্যাগের কাহিনী শোনানো, কুরআনের ছোট ছোট সূরা মুখস্থ করানো এবং সুন্দর দোয়াসমূহ অর্থসহ শেখানো।

৪. পুরস্কার ও ইতিবাচক উৎসাহ প্রদান:
বাচ্চারা যদি মসজিদে কিছু সময় ধৈর্য সহকারে তিলাওয়াত বা জিকিরে মনোনিবেশ করে, তবে তাদেরকে ভালোবাসা, প্রশংসা ও পছন্দসই উপহার দিয়ে উৎসাহিত করা।

৫. ঘরে মেয়ে সন্তানদের প্রশিক্ষণ:
মা বা পরিবারের বয়োজ্যেষ্ঠ নারী যখন ঘরে ইতিকাফ পালন করেন, তখন কন্যা সন্তানকে পাশে বসিয়ে অল্প সময়ের জন্য নফল ইতিকাফের অনুকরণ করানো যেতে পারে, যা তাদের মনে আজীবন দ্বীনি প্রভাব ফেলে।""",
        "details_en": """Methods for Imparting Itikaf Values to Youth:

1. Incremental and Age-Appropriate Seclusion:
Do not burden young children with ten consecutive days. Introduce them gradually—a few hours on Friday, or overnight during one odd night of Ramadan.

2. Instilling Mosque Etiquette (Adab):
Educating children on preserving the peace of the sanctuary, avoiding running or shouting, and respecting the concentration of fellow worshippers.

3. Engaging with Inspiring Prophetic Stories:
Narrating uplifting stories of the Prophets and Companions, memorizing short Surahs, and practicing everyday Duas together in a warm, encouraging manner.

4. Positive Reinforcement and Rewards:
Applauding their perseverance, praising their patience, and presenting small gifts when they complete their scheduled period of quiet reflection.

5. Modeling in the Household for Young Girls:
When mothers or grandmothers observe seclusion in the home prayer space, young daughters can join for brief intervals, forming cherished spiritual memories that endure for a lifetime.""",
        "reference_bn": "সুনানে আবু দাউদ: ৪৯৫; মুসান্নাফ ইবনে আবি শাইবাহ: খণ্ড ২, পৃষ্ঠা ৩৪৮",
        "reference_en": "Sunan Abi Dawud: 495; Musannaf Ibn Abi Shaybah: Vol 2, p. 348"
    }
]

# Generate Java File
java_content = '''package com.devlabs.deanone.features.ramadan.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devlabs.deanone.core.backend.BackendConfigManager;
import com.devlabs.deanone.core.network.NetworkConnectivityHelper;
import com.devlabs.deanone.features.ramadan.model.RozaItikafTopicItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 3-Tier Production Repository for: section: ইতিকাফ (Itikaf).
 * 0ms instant memory cache with background async REST API sync (Rule 11).
 * 100% matches screenshot topics:
 * 1. ইতিকাফ কি?
 * 2. ইতিকাফের গুরুত্ব ও ফজিলত
 * 3. ইতিকাফের ধরন
 * 4. ইতিকাফের নিয়ম ও শর্তাবলি
 * 5. ইতিকাফের সময়ে করণীয় কাজ
 * 6. ইতিকাফ এবং মহিলারা
 * 7. ইতিকাফ এবং আধুনিক যুগ
 * 8. ইতিকাফের ভুল ও সতর্কতা
 * 9. ইতিকাফের ইতিহাস ও উদাহরণ
 * 10. ইতিকাফের প্রস্তুতি
 * 11. বাচ্চাদের জন্য ইতিকাফ শেখানো
 */
public final class RozaItikafRepository {

    private static final List<RozaItikafTopicItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultTopics());
    }

    private RozaItikafRepository() {}

    public interface DataCallback {
        void onDataLoaded(List<RozaItikafTopicItem> items);
    }

    public static List<RozaItikafTopicItem> getAllTopics(Context context, DataCallback callback) {
        if (context != null && NetworkConnectivityHelper.isOnline(context)) {
            final Context appContext = context.getApplicationContext();
            executor.execute(() -> fetchRemoteTopics(appContext, callback));
        }
        return new ArrayList<>(cachedList);
    }

    public static List<RozaItikafTopicItem> getAllTopics() {
        return new ArrayList<>(cachedList);
    }

    public static List<RozaItikafTopicItem> getTopics(Context context, DataCallback callback) {
        return getAllTopics(context, callback);
    }

    public static RozaItikafTopicItem getTopicBySlug(String slug) {
        if (slug == null) return null;
        synchronized (cachedList) {
            for (RozaItikafTopicItem item : cachedList) {
                if (slug.equalsIgnoreCase(item.getSlug())) {
                    return item;
                }
            }
        }
        return null;
    }

    public static List<RozaItikafTopicItem> getDefaultTopics() {
        List<RozaItikafTopicItem> list = new ArrayList<>();
'''

def escape_java(s):
    return s.replace('\\', '\\\\').replace('"', '\\"').replace('\r\n', '\\n').replace('\n', '\\n')

for t in topics:
    java_content += f'''
        // {t["order"]}. {t["title_bn"]}
        list.add(new RozaItikafTopicItem(
                {t["id"]},
                "{t["slug"]}",
                {t["order"]},
                "{escape_java(t["title_bn"])}",
                "{escape_java(t["title_en"])}",
                "{escape_java(t["card_title_bn"])}",
                "{escape_java(t["card_title_en"])}",
                "{escape_java(t["preview_bn"])}",
                "{escape_java(t["preview_en"])}",
                "{escape_java(t["details_bn"])}",
                "{escape_java(t["details_en"])}",
                "{escape_java(t["reference_bn"])}",
                "{escape_java(t["reference_en"])}"
        ));
'''

java_content += '''
        return list;
    }

    private static void fetchRemoteTopics(Context context, DataCallback callback) {
        HttpURLConnection conn = null;
        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_itikaf_topics.php");
            URL url = new URL(endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    JsonObject root = JsonParser.parseString(sb.toString()).getAsJsonObject();
                    if (root.has("success") && root.get("success").getAsBoolean() && root.has("topics")) {
                        JsonArray arr = root.getAsJsonArray("topics");
                        List<RozaItikafTopicItem> remoteList = new ArrayList<>();
                        for (JsonElement el : arr) {
                            JsonObject obj = el.getAsJsonObject();
                            int id = obj.get("id").getAsInt();
                            String slug = obj.get("slug").getAsString();
                            int orderIndex = obj.has("sort_order") ? obj.get("sort_order").getAsInt() : id;
                            String titleBn = obj.get("title_bn").getAsString();
                            String titleEn = obj.get("title_en").getAsString();
                            String cardTitleBn = obj.has("card_title_bn") ? obj.get("card_title_bn").getAsString() : titleBn;
                            String cardTitleEn = obj.has("card_title_en") ? obj.get("card_title_en").getAsString() : titleEn;
                            String previewBn = obj.get("preview_bn").getAsString();
                            String previewEn = obj.get("preview_en").getAsString();
                            String detailsBn = obj.get("details_bn").getAsString();
                            String detailsEn = obj.get("details_en").getAsString();
                            String referenceBn = obj.has("reference_bn") && !obj.get("reference_bn").isJsonNull() ? obj.get("reference_bn").getAsString() : "";
                            String referenceEn = obj.has("reference_en") && !obj.get("reference_en").isJsonNull() ? obj.get("reference_en").getAsString() : "";

                            remoteList.add(new RozaItikafTopicItem(
                                    id, slug, orderIndex, titleBn, titleEn, cardTitleBn, cardTitleEn,
                                    previewBn, previewEn, detailsBn, detailsEn, referenceBn, referenceEn
                            ));
                        }

                        if (!remoteList.isEmpty()) {
                            synchronized (cachedList) {
                                cachedList.clear();
                                cachedList.addAll(remoteList);
                            }
                            if (callback != null) {
                                mainHandler.post(() -> callback.onDataLoaded(new ArrayList<>(cachedList)));
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
'''

with open(r"f:\Deanone\app\src\main\java\com\devlabs\deanone\features\ramadan\data\RozaItikafRepository.java", "w", encoding="utf-8") as f:
    f.write(java_content)

print("RozaItikafRepository.java generated successfully!")
