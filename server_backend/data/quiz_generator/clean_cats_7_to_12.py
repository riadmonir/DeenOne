# -*- coding: utf-8 -*-
"""
Batch 2: Categories 7 to 12 (35 Genuine Authentic Unique Questions Each = 210 Questions)
7. seerat_un_nabi
8. sahaba_life
9. jannah_paradise
10. jahannam_hell
11. jinn_unseen
12. islamic_history
"""

def get_cat_07_seerat():
    items = [
        ("রাসূলুল্লাহ ﷺ কোন্ বছর পবিত্র মক্কা নগরীতে কুরাইশ বংশের বনু হাশিম শাখায় শুভ জন্মগ্রহণ করেন?", "In which year was Prophet Muhammad ﷺ born in Makkah?",
         ["৫৭১ খ্রিস্টাব্দে (আমুল ফিল)", "৬১০ খ্রিস্টাব্দে", "৬২২ খ্রিস্টাব্দে", "৬৩২ খ্রিস্টাব্দে"], ["571 CE (Year of Elephant)", "610 CE", "622 CE", "632 CE"], 0,
         "রাসূলুল্লাহ ﷺ ঐতিহাসিক হস্তী বছর বা ৫৭১ খ্রিস্টাব্দের রবিউল আউয়াল মাসে শুভ জন্ম নেন।", "The Prophet ﷺ was born in 571 CE in Makkah.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ নবুওয়াত লাভের পর মক্কায় কত বছর গোপনে ও প্রকাশ্যে ইসলামের দাওয়াত দেন?", "How many years did Prophet Muhammad ﷺ preach in Makkah after Prophethood?",
         ["১৩ বছর", "১০ বছর", "২৩ বছর", "৪০ বছর"], ["13 years", "10 years", "23 years", "40 years"], 0,
         "মক্কায় ১৩ বছর ইসলাম প্রচারের পর আল্লাহর আদেশে তিনি মদীনায় হিজরত করেন।", "The Prophet ﷺ preached in Makkah for 13 years before Hijrah.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("মক্কায় প্রাথমিক ৩ বছর গোপনে দাওয়াত দেওয়ার পর আল্লাহ কোন পাহাড়ে দাঁড়িয়ে প্রকাশ্যে দাওয়াতের নির্দেশ দেন?", "From which hill in Makkah did the Prophet ﷺ first openly preach to Quraysh?",
         ["সাফা পাহাড়", "মারওয়া পাহাড়", "নূর পাহাড়", "সওর পাহাড়"], ["Mount Safa", "Mount Marwah", "Mount Noor", "Mount Thawr"], 0,
         "সাফা পাহাড়ে দাঁড়িয়ে রাসূল ﷺ কুরাইশদের ডাক দিয়ে একত্ববাদের প্রকাশ্যে ঘোষণা দেন।", "The Prophet ﷺ stood atop Mount Safa to declare the open message of Islam.", "সহীহ বুখারী: ৪৭৭০", "Sahih Bukhari: 4770", "EASY"),

        ("ইসলামের ইতিহাসে সাহাবীদের ওপর চরম নির্যাতনের মুখে প্রথম হিজরত কোথায় সম্পন্ন হয়েছিল?", "Where did the first migration of early Muslims take place to escape torture?",
         ["আবিসিনিয়া (হাবশা)", "মদীনা", "তায়েফ", "ইয়েমেন"], ["Abyssinia (Habashah)", "Madinah", "Taif", "Yemen"], 0,
         "ন্যায়পরায়ণ খ্রিষ্টান বাদশাহ নাজ্জাশীর রাজ্যে মুসলিমগণ প্রথম হাবশায় হিজরত করেন।", "Early Muslims migrated to Abyssinia ruled by the just King Negus.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("কুরাইশরা বনু হাশিম ও মুসলমানদের কোন গিরিসঙ্কটে দীর্ঘ ৩ বছর নির্মম অর্থনৈতিক ও সামাজিক বয়কট করে রেখেছিল?", "In which valley did Quraysh boycott Banu Hashim and Muslims for 3 harsh years?",
         ["শে'আবে আবু তালিব", "বদর উপত্যকা", "হুনাইন উপত্যকা", "মিনার উপত্যকা"], ["Shi'b Abi Talib", "Valley of Badr", "Valley of Hunayn", "Mina Valley"], 0,
         "শে'আবে আবু তালিবে মুসলিমরা গাছের পাতা ও শুকনো চামড়া খেয়ে অবর্ণনীয় কষ্টে ৩ বছর কাটান।", "Muslims endured 3 years of agonizing boycott inside Shi'b Abi Talib.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("মিরাজের পবিত্র রাতে আল্লাহ তাআলা উম্মতে মুহাম্মাদীর ওপর উপহার হিসেবে কী ফরজ করেছিলেন?", "What was gifted as an obligation to the Ummah on the Night of Mi'raj?",
         ["দৈনিক ৫ ওয়াক্ত সালাত (নামাজ)", "রমজানের রোজা", "হজ", "যাকাত"], ["Five daily prayers (Salah)", "Ramadan fasting", "Hajj", "Zakat"], 0,
         "মিরাজে প্রথমে ৫০ ওয়াক্তের বিধান হলেও কমিয়ে ৫ ওয়াক্ত করা হয় যা ৫০ ওয়াক্তের সওয়াব দেয়।", "Allah ordained the five daily prayers during the ascension (Mi'raj).", "সহীহ বুখারী: ৩৪৯", "Sahih Bukhari: 349", "EASY"),

        ("মদীনার আনসারগণ মক্কায় এসে রাসূলুল্লাহ ﷺ-এর হাতে যে ঐতিহাসিক আনুগত্যের শপথ নিয়েছিলেন তাকে কী বলে?", "What were the historic pledges made by the people of Madinah at Makkah called?",
         ["বায়আতে আকাবা (প্রথম ও দ্বিতীয়)", "বায়আতে রিদওয়ান", "বায়আতে নিসা", "বায়আতে হারব"], ["Pledges of Aqabah (1st & 2nd)", "Bay'at ar-Ridwan", "Bay'at an-Nisa", "Bay'at al-Harb"], 0,
         "আকাবার গিরিপথে মদীনার নেতৃবৃন্দ নবীজিকে মদীনায় আগমন ও পূর্ণ সহযোগিতার শপথ দেন।", "The Pledges of Aqabah laid the foundation for the migration to Madinah.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("মক্কা থেকে মদীনায় হিজরতের সময় রাসূলুল্লাহ ﷺ ও হযরত আবু বকর (রা.) কোন গুহায় ৩ দিন আত্মগোপন করেছিলেন?", "In which cave did Prophet Muhammad ﷺ and Abu Bakr (RA) shelter for three nights during Hijrah?",
         ["সওর গুহা (গায়ে সওর)", "হেরা গুহা", "কাহফ গুহা", "সিনাই গুহা"], ["Cave Thawr (Ghar Thawr)", "Cave Hira", "Cave of Kahf", "Sinai Cave"], 0,
         "কুরাইশদের চোখ এড়িয়ে রাসূল ﷺ ও আবু বকর (রা.) সওর পর্বতের গুহায় অবস্থান নেন।", "They took refuge in Cave Thawr where spider web and pigeons protected them.", "সূরা আত-তাওবাহ: ৪০", "Surah At-Tawbah: 40", "EASY"),

        ("হিজরতের পথে রাসূল ﷺ-এর পিছু ধাওয়া করে আসা কোন অশ্বারোহীর ঘোড়ার পা অলৌকিকভাবে মাটিতে দেবে গিয়েছিল?", "Which tracker pursued the Prophet ﷺ during Hijrah but his horse's legs sank into the sand?",
         ["সুরাকা ইবনে মালিক", "আবু সুফিয়ান", "ইকরিমা", "খালিদ বিন ওয়ালিদ"], ["Suraqah ibn Malik", "Abu Sufyan", "Ikrimah", "Khalid bin Walid"], 0,
         "সুরাকা তিনবার আক্রমণের চেষ্টা করে ব্যর্থ হয়ে ক্ষমা চায় এবং নবীজি তাকে কিসরার স্বর্ণের বালা পরার সুসংবাদ দেন।", "Suraqah's horse sank into earth and the Prophet ﷺ promised him the bangles of Chosroes.", "সহীহ বুখারী: ৩৯০৬", "Sahih Bukhari: 3906", "EASY"),

        ("মদীনায় হিজরতের পর মুহাজির ও আনসারদের মাঝে স্থাপিত ঐতিহাসিক ভ্রাতৃত্বের বন্ধনকে কী বলা হয়?", "What was the institutional brotherhood established between Muhajirun and Ansar called?",
         ["মুওয়াখাত (Muwakhat)", "মুদারাবা", "মুশারাকা", "মুআমালাত"], ["Muwakhat (Brotherhood Pact)", "Mudarabah", "Musharakah", "Muamalat"], 0,
         "রাসূল ﷺ প্রত্যেক মুহাজিরের সাথে একজন আনসারের রক্তের সম্পর্কের মতো গভীর ভ্রাতৃত্ব প্রতিষ্ঠা করেন।", "The Prophet ﷺ established the bond of Muwakhat uniting Muhajirun and Ansar.", "সহীহ বুখারী: ৩৯৩৭", "Sahih Bukhari: 3937", "EASY"),

        ("ইসলামের ইতিহাসে মুসলমানদের প্রথম আনুষ্ঠানিক সশস্ত্র যুদ্ধ কোনটি ছিল?", "Which was the first major military battle fought by Muslims in Islamic history?",
         ["বদরের যুদ্ধ (২য় হিজরি)", "উহুদের যুদ্ধ", "খন্দকের যুদ্ধ", "মুতার যুদ্ধ"], ["Battle of Badr (2 AH)", "Battle of Uhud", "Battle of Khandaq", "Battle of Mu'tah"], 0,
         "১৭ই রমজান ২য় হিজরিতে ৩১৩ জন নিরস্ত্র মুসলিম ১,০০০ সুসজ্জিত কুরাইশ বাহিনীকে শোচনীয়ভাবে পরাজিত করে।", "Battle of Badr on 17 Ramadan 2 AH was the glorious first victory.", "সূরা আল-আনফাল", "Surah Al-Anfal", "EASY"),

        ("বদর যুদ্ধে কুরাইশ বাহিনীর সর্বপ্রধান ইসলামবিদ্বেষী নেতা যে নিহত হয়েছিল তার নাম কী?", "Which archenemy of Islam and chief of Quraysh was killed at the Battle of Badr?",
         ["আবু জাহেল (আমর ইবনে হিশাম)", "আবু সুফিয়ান", "উতবা", "শাইবা"], ["Abu Jahl (Amr ibn Hisham)", "Abu Sufyan", "Utbah", "Shaybah"], 0,
         "দুই আনসার কিশোর মুয়ায ও মুআউওয়ায আবু জাহেলকে ধরাশায়ী করে এবং আব্দুল্লাহ ইবনে মাসউদ তার শিরশ্ছেদ করেন।", "Abu Jahl, the Pharaoh of this Ummah, was slain at Badr.", "সহীহ বুখারী: ৩৯৬২", "Sahih Bukhari: 3962", "EASY"),

        ("উহুদের যুদ্ধে তীরন্দাজদের গিরিপথ ত্যাগের ভুলের সুযোগে কোন দূরদর্শী সেনাপতি মুসলিমদের পিছন থেকে আক্রমণ করেছিলেন?", "Who led the cavalry flanking maneuver against Muslims at Mount Uhud before accepting Islam?",
         ["হযরত খালিদ বিন ওয়ালিদ (তৎকালীন অমুসলিম অবস্থায়)", "আবু সুফিয়ান", "ইকরিমা", "আমর ইবনুল আস"], ["Khalid bin Walid (prior to Islam)", "Abu Sufyan", "Ikrimah", "Amr ibn al-Aas"], 0,
         "খালিদ বিন ওয়ালিদ তীরন্দাজদের শূন্য গিরিপথ দিয়ে অশ্বারোহী বাহিনী নিয়ে আকস্মিক আক্রমণ করেন।", "Khalid bin Walid exploited the abandoned archers' pass at Uhud.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("উহুদের ময়দানে বীরত্বের সাথে যুদ্ধ করে শাহাদাত বরণ করেন রাসূল ﷺ-এর প্রিয় চাচা সাইয়্যিদুশ শুহাদা কে?", "Which beloved uncle of the Prophet ﷺ was martyred at Uhud as the Leader of Martyrs?",
         ["হযরত হামযাহ ইবনে আব্দুল মুত্তালিব (রা.)", "হযরত আব্বাস (রা.)", "হযরত আবু তালিব", "হযরত হারেস"], ["Hamzah ibn Abdul Muttalib (RA)", "Abbas (RA)", "Abu Talib", "Harith"], 0,
         "ওয়াহশী নামক হাবশী ক্রীতদাস জুবায়ের ইবনে মুতইমের নির্দেশে দূর থেকে বর্শা ছুড়ে হামযাহ (রা.)-কে শহীদ করে।", "Hamzah (RA) was martyred at Uhud and named Sayyidush Shuhada.", "সহীহ বুখারী: ৪০৭২", "Sahih Bukhari: 4072", "EASY"),

        ("খন্দক (পরিখা) যুদ্ধের সময় মদীনা প্রতিরক্ষায় পরিখা খননের চমৎকার সামরিক পরামর্শ কে দিয়েছিলেন?", "Who suggested digging a defensive trench (Khandaq) around Madinah?",
         ["হযরত সালমান ফারসী (রা.)", "হযরত আলী (রা.)", "হযরত আম্মার (রা.)", "হযরত মিকদাদ (রা.)"], ["Salman al-Farsi (RA)", "Ali (RA)", "Ammar (RA)", "Miqdad (RA)"], 0,
         "পারস্যের কৌশল অনুযায়ী সালমান ফারসী (রা.) মদীনার উন্মুক্ত অংশে পরিখা খননের অভিনব পরামর্শ দেন।", "Salman al-Farsi (RA) advised digging the trench which repelled the allied confederacy.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("হিজরি ৬ষ্ঠ সনে মক্কার কুরাইশদের সাথে মুসলমানদের যে ঐতিহাসিক ১০ বছরের শান্তিচুক্তি স্বাক্ষরিত হয় তার নাম কী?", "What was the historic 10-year peace treaty signed in 6 AH between Muslims and Quraysh?",
         ["হুদাইবিয়ার সন্ধি (Sulh al-Hudaybiyyah)", "মদীনা সনদ", "আকাবা চুক্তি", "ইয়ামান চুক্তি"], ["Treaty of Hudaybiyyah", "Constitution of Madinah", "Pledge of Aqabah", "Yemen Treaty"], 0,
         "হুদাইবিয়ার সন্ধির ফলে মক্কার কুরাইশদের সাথে যুদ্ধবিরতি ঘটে এবং ইসলাম দ্রুত আরবজুড়ে ছড়িয়ে পড়ে।", "The Treaty of Hudaybiyyah paved the way for massive peaceful propagation.", "সহীহ বুখারী: ২৭৩১", "Sahih Bukhari: 2731", "EASY"),

        ("হুদাইবিয়ার সন্ধি স্বাক্ষরের সময় রাসূল ﷺ-এর নির্দেশে সন্ধিপত্রটি লেখক হিসেবে কে লিপিবদ্ধ করেছিলেন?", "Who wrote down the clauses of the Treaty of Hudaybiyyah as scribe for the Prophet ﷺ?",
         ["হযরত আলী ইবনে আবি তালিব (রা.)", "হযরত উসমান (রা.)", "হযরত যায়েদ বিন সাবিত (রা.)", "হযরত মুয়াবিয়া (রা.)"], ["Ali ibn Abi Talib (RA)", "Uthman (RA)", "Zayd bin Thabit (RA)", "Muawiyah (RA)"], 0,
         "হযরত আলী (রা.) হুদাইবিয়ার সন্ধিপত্রটি অত্যন্ত সুন্দর হস্তাক্ষরে লিপিবদ্ধ করেন।", "Ali (RA) acted as the official scribe for the Hudaybiyyah treaty.", "সহীহ মুসলিম: ১৭৮৩", "Sahih Muslim: 1783", "EASY"),

        ("হিজরি ৮ম সনের রমজান মাসে রক্তপাতহীনভাবে কোন ঐতিহাসিক মহান বিজয় সম্পন্ন হয়েছিল?", "Which glorious bloodless victory occurred in Ramadan of 8 AH?",
         ["মক্কা বিজয় (Fath Makkah)", "খায়বার বিজয়", "তায়েফ বিজয়", "তাবুক বিজয়"], ["Conquest of Makkah (Fath Makkah)", "Conquest of Khaybar", "Siege of Taif", "Expedition of Tabuk"], 0,
         "দশ হাজার সাহাবীর বাহিনী নিয়ে রাসূলুল্লাহ ﷺ বিনা রক্তপাতে মক্কা বিজয় করেন এবং কাবাঘরের ৩৬০টি মূর্তি অপসারণ করেন।", "The Prophet ﷺ entered Makkah victorious and purified the Kaaba of all 360 idols.", "সহীহ বুখারী: ৪২৮০", "Sahih Bukhari: 4280", "EASY"),

        ("মক্কা বিজয়ের পর কাবাঘরের ছাদে উঠে ঐতিহাসিক বিজয়ের আজান দিয়েছিলেন কোন সাহাবী?", "Which companion climbed the roof of the Kaaba to call the victory Adhan upon the Conquest of Makkah?",
         ["হযরত বিলাল ইবনে রাবাহ (রা.)", "হযরত আবু মাহযুরা (রা.)", "হযরত আব্দুল্লাহ ইবনে উম্মে মাকতুম (রা.)", "হযরত সা'দ (রা.)"], ["Bilal ibn Rabah (RA)", "Abu Mahdhurah (RA)", "Abdullah ibn Umm Maktum (RA)", "Sa'd (RA)"], 0,
         "রাসূল ﷺ-এর আদেশে হযরত বিলাল (রা.) কাবার ছাদে উঠে তাওহীদের আযান ধ্বনিত করেন।", "Bilal (RA) climbed the Kaaba's roof and called the historic Adhan.", "সহীহ বুখারী: ৪২৯০", "Sahih Bukhari: 4290", "EASY"),

        ("খায়বারের শক্তিশালী কামুস দুর্গ জয় করতে রাসূল ﷺ কার হাতে ইসলামের বিজয়ী পতাকা অর্পণ করেছিলেন?", "To whom did the Prophet ﷺ hand the victorious banner to conquer the fortress of Khaybar?",
         ["হযরত আলী ইবনে আবি তালিব (রা.)", "হযরত উমর (রা.)", "হযরত আবু বকর (রা.)", "হযরত যুবায়ের (রা.)"], ["Ali ibn Abi Talib (RA)", "Umar (RA)", "Abu Bakr (RA)", "Zubayr (RA)"], 0,
         "রাসূল ﷺ বলেন: কাল আমি এমন এক ব্যক্তির হাতে ঝাণ্ডা দেব যাকে আল্লাহ ও তাঁর রাসূল ভালোবাসেন এবং সেও আল্লাহ ও রাসূলকে ভালোবাসে।", "Ali (RA) broke the gates of Khaybar and achieved decisive victory.", "সহীহ বুখারী: ৩৭০১", "Sahih Bukhari: 3701", "EASY"),

        ("মুতার যুদ্ধে তিনজন মহান সেনাপতি একে একে শহীদ হওয়ার পর মুসলিম সেনাদলকে দক্ষতার সাথে নিরাপদে ফিরিয়ে আনেন কে?", "Who took command at the Battle of Mu'tah after 3 commanders were martyred and led Muslims to safety?",
         ["হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত আবু উবাইদাহ (রা.)", "হযরত আমর ইবনুল আস (রা.)", "হযরত সাদ বিন আবি ওয়াক্কাস (রা.)"], ["Khalid bin Walid (RA)", "Abu Ubaidah (RA)", "Amr ibn al-Aas (RA)", "Sa'd bin Abi Waqqas (RA)"], 0,
         "খালিদ বিন ওয়ালিদ (রা.) সেনাপতিত্ব গ্রহণ করে রণকৌশলে রোমান বাহিনীকে পরাস্ত করে মুসলিমদের ফিরিয়ে আনেন।", "Khalid (RA) broke 9 swords in battle and secured safe withdrawal.", "সহীহ বুখারী: ৪২৬০", "Sahih Bukhari: 4260", "EASY"),

        ("তীব্র খরতাপ ও দুর্ভিক্ষের মৌসুমে রোমানদের বিরুদ্ধে পরিচালিত রাসূল ﷺ-এর সর্বশেষ সামরিক অভিযান কোনটি ছিল?", "Which was the last military expedition personally led by Prophet Muhammad ﷺ?",
         ["তাবুক অভিযান (৯ম হিজরি)", "হুনাইন যুদ্ধ", "মুতার যুদ্ধ", "ইয়ামামার যুদ্ধ"], ["Expedition of Tabuk (9 AH)", "Battle of Hunayn", "Battle of Mu'tah", "Battle of Yamama"], 0,
         "তাবুক অভিযানে হযরত উসমান (রা.) বিপুল পরিমাণ স্বর্ণ ও সাজসরঞ্জাম দান করে বাহিনীর দায়িত্ব নেন।", "Tabuk was the Prophet's ﷺ final expedition against the Byzantine empire.", "সহীহ বুখারী: ৪৪১৫", "Sahih Bukhari: 4415", "EASY"),

        ("তাবুক অভিযানের চরম মুহূর্তে হযরত আবু বকর সিদ্দিক (রা.) ইসলামের তহবিলে কী পরিমাণ সম্পদ দান করেছিলেন?", "How much of his wealth did Abu Bakr (RA) donate for the Expedition of Tabuk?",
         ["তাঁর ঘরের সমুদয় সম্পদ (শতভাগ)", "অর্ধেক সম্পদ", "এক তৃতীয়াংশ", "এক চতুর্থাংশ"], ["All of his household possessions (100%)", "Half his wealth", "One third", "One fourth"], 0,
         "নবীজি জিজ্ঞেস করলেন: ঘরে কী রেখে এসেছ? আবু বকর (রা.) উত্তর দিলেন: 'আল্লাহ ও তাঁর রাসূলকে রেখে এসেছি।'", "Abu Bakr (RA) brought everything he owned, leaving Allah and His Messenger for his family.", "জামে আত-তিরমিযী: ৩৬৭৫", "Jami at-Tirmidhi: 3675", "EASY"),

        ("তাবুক অভিযানের সময় হযরত উমর ফারুক (রা.) তাঁর সমুদয় সম্পদের কত অংশ দান করেছিলেন?", "How much of his wealth did Umar ibn al-Khattab (RA) donate for Tabuk?",
         ["তাঁর মোট সম্পদের অর্ধেক অংশ (৫০%)", "সম্পূর্ণ সম্পদ", "এক দশমাংশ", "দুই তৃতীয়াংশ"], ["Half of his total wealth (50%)", "All wealth", "One tenth", "Two thirds"], 0,
         "উমর (রা.) ভাবলেন আজ আবু বকরকে অতিক্রম করবেন এবং অর্ধেক সম্পদ নিয়ে আসেন, কিন্তু আবু বকর সম্পূর্ণটাই নিয়ে আসেন।", "Umar (RA) donated half his wealth for the army.", "সুনানে আবু দাউদ: ১৬৭৮", "Sunan Abi Dawud: 1678", "EASY"),

        ("১০ম হিজরিতে অনুষ্ঠিত ঐতিহাসিক বিদায় হজে আরাফাতের ময়দানে রাসূল ﷺ-এর ভাষণ কতজন সাহাবী শুনেছিলেন?", "Approximately how many companions were present during the Prophet's ﷺ Farewell Pilgrimage?",
         ["প্রায় ১ লক্ষেরও বেশি সাহাবী (১,২৪,০০০)", "১০ হাজার", "৫০ হাজার", "৫ লক্ষ"], ["Over 100,000 companions (~124,000)", "10,000", "50,000", "500,000"], 0,
         "আরাফাতের জাবালে রহমতে লক্ষাধিক সাহাবীর বিশাল সমাবেশে রাসূল ﷺ বিদায় হজের অমর ঐতিহাসিক ভাষণ প্রদান করেন।", "Over 124,000 companions gathered at Arafat during the Farewell Hajj.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("বিদায় হজের দিন আরাফাতের ময়দানে দ্বীন পূর্ণতার ঘোষণায় কুরআনের কোন বিখ্যাত আয়াত নাযিল হয়েছিল?", "Which momentous verse completing the religion of Islam was revealed at Arafat during Farewell Hajj?",
         ["'আজ আমি তোমাদের জন্য তোমাদের দ্বীনকে পূর্ণাঙ্গ করলাম' (সূরা মায়িদাহ: ৩)", "সূরা ফাতিহা", "সূরা নাসর", "সূরা ইখলাস"], ["'This day I have perfected for you your religion' (Surah Al-Ma'idah: 3)", "Surah Fatihah", "Surah Nasr", "Surah Ikhlas"], 0,
         "আল্লাহ তাআলা ইসলামকে বিশ্বমানবতার জন্য পূর্ণাঙ্গ ও একমাত্র মনোনীত দ্বীন হিসেবে চূড়ান্ত ঘোষণা করেন।", "This day I have perfected your religion for you and completed My favor upon you.", "সূরা আল-মায়িদাহ: ৩", "Surah Al-Ma'idah: 3", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর নবুওয়াতের সিলমোহর (মোহরে নবুওয়াত) তাঁর শরীরের কোথায় অবস্থিত ছিল?", "Where was the physical Seal of Prophethood (Khatam an-Nubuwwah) located on the Prophet's ﷺ body?",
         ["দুই কাঁধের মাঝখানে পিঠের ওপর কবুতরের ডিমের মতো উঁচু মাংসপিণ্ড", "বুকে", "হাতের তালুতে", "কপালে"], ["Between his shoulder blades on his back like a pigeon's egg", "On chest", "On palm", "On forehead"], 0,
         "রাসূল ﷺ-এর দুই স্কন্ধের মাঝে মোহরে নবুওয়াত ছিল যা আসমানী কিতাবসমূহের পূর্বাভাসের হুবহু অনুরূপ।", "The Seal of Prophethood was situated between his shoulder blades.", "সহীহ মুসলিম: ২৩৪৪", "Sahih Muslim: 2344", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর প্রধান মুয়াজ্জিন হযরত বিলাল (রা.) কোন দেশের বংশোদ্ভূত ছিলেন?", "From which region was the Prophet's beloved Muazzin Bilal ibn Rabah (RA) originally from?",
         ["আবিসিনিয়া (হাবশা / ইথিওপিয়া)", "রোম", "পারস্য", "ইয়েমেন"], ["Abyssinia (Habashah / Ethiopia)", "Rome", "Persia", "Yemen"], 0,
         "হযরত বিলাল (রা.) হাবশী বংশোদ্ভূত ছিলেন এবং উমাইয়া ইবনে খালাফের চরম নির্যাতন সত্ত্বেও 'আহাদ আহাদ' উচ্চারণ করতেন।", "Bilal (RA) was of Abyssinian descent who endured scorching torture for Tawheed.", "আল-ইসাবাহ", "Al-Isabah", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর বিশ্বস্ত দেহরক্ষী ও সিক্রেট কিপার (গোপনীয় বিষয়ের সংরক্ষক) বলা হতো কোন সাহাবীকে?", "Which companion was trusted as the Secret-Keeper (Sahib as-Sirr) of the Prophet ﷺ regarding hypocrites?",
         ["হযরত হুজাইফা ইবনুল ইয়ামান (রা.)", "হযরত আবু বকর (রা.)", "হযরত আলী (রা.)", "হযরত মুয়াবিয়া (রা.)"], ["Hudhayfah ibn al-Yaman (RA)", "Abu Bakr (RA)", "Ali (RA)", "Muawiyah (RA)"], 0,
         "রাসূল ﷺ হুজাইফা (রা.)-কে মদীনার মুনাফিকদের গোপন নামের তালিকা জানিয়েছিলেন।", "Hudhayfah (RA) was entrusted with the confidential names of all hypocrites.", "সহীহ বুখারী: ৩৭৪৩", "Sahih Bukhari: 3743", "MEDIUM"),

        ("রাসূলুল্লাহ ﷺ-এর জীবদ্দশায় শেষ অসুস্থতার সময় তিনি কার ইমামতিতে সাহাবীদের সালাত আদায়ের নির্দেশ দেন?", "Whom did Prophet Muhammad ﷺ command to lead the prayers during his final illness?",
         ["হযরত আবু বকর সিদ্দিক (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)", "হযরত উসমান (রা.)"], ["Abu Bakr As-Siddiq (RA)", "Umar (RA)", "Ali (RA)", "Uthman (RA)"], 0,
         "রাসূল ﷺ স্পষ্টভাবে নির্দেশ দেন: 'আবু বকরকে বলো লোকদের নিয়ে সালাত আদায় করতে।' যা তাঁর খিলাফতের সুস্পষ্ট ইঙ্গিত।", "The Prophet ﷺ ordered Abu Bakr (RA) to lead all public prayers in his absence.", "সহীহ বুখারী: ৬৮৩", "Sahih Bukhari: 683", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর ওফাতের সংবাদে যখন সাহাবীগণ শোকে বিহ্বল তখন কে অটল ঈমানদীপ্ত ঐতিহাসিক বক্তব্য দিয়েছিলেন?", "Who restored calm among grief-stricken companions upon the Prophet's passing with iconic words?",
         ["হযরত আবু বকর সিদ্দিক (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)", "হযরত উসমান (রা.)"], ["Abu Bakr As-Siddiq (RA)", "Umar (RA)", "Ali (RA)", "Uthman (RA)"], 0,
         "আবু বকর (রা.) ঘোষণা করেন: 'যে মুহাম্মদের ইবাদত করত সে জানুক মুহাম্মদ ﷺ ইন্তেকাল করেছেন, আর যে আল্লাহর ইবাদত করে নিশ্চয়ই আল্লাহ চিরঞ্জীব।' ", "Abu Bakr (RA) proclaimed: Whoever worships Muhammad, Muhammad has died; but whoever worships Allah, Allah is alive and never dies.", "সহীহ বুখারী: ১২৪১", "Sahih Bukhari: 1241", "EASY"),

        ("রাসূলুল্লাহ ﷺ কোন্ উম্মুল মুমিনীনের হুজরায় ওফাত লাভ করেন এবং সেখানেই সমাহিত হন?", "In whose room did Prophet Muhammad ﷺ pass away and get buried?",
         ["হযরত আয়েশা সিদ্দিকা (রা.)", "হযরত খাদিজা (রা.)", "হযরত হাফসা (রা.)", "হযরত উম্মে সালামা (রা.)"], ["Aisha as-Siddiqah (RA)", "Khadijah (RA)", "Hafsah (RA)", "Umm Salamah (RA)"], 0,
         "রাসূল ﷺ প্রিয়তমা স্ত্রী আয়েশা (রা.)-এর কোলে মাথা রেখে রফিকে আলার সান্নিধ্যে গমন করেন।", "The Prophet ﷺ passed away in the arms of Aisha (RA) and was buried in her room.", "সহীহ বুখারী: ৪৪৩৭", "Sahih Bukhari: 4437", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর জানাজা ও গোসল সম্পন্ন করতে মূল দায়িত্বে কারা ছিলেন?", "Who performed the ritual washing (Ghusl) of Prophet Muhammad ﷺ?",
         ["হযরত আলী, চাচা আব্বাস, ফজল ও কুসাম ইবনে আব্বাস (রা.)", "রোমান প্রতিনিধি", "কুরাইশ সর্দার", "আনসার প্রধান"], ["Ali, Abbas, Fadl, and Qutham ibn Abbas (RA)", "Romans", "Quraysh chiefs", "Ansar chiefs"], 0,
         "রাসূল ﷺ-এর নিকটাত্মীয় আহলে বাইতের সদস্যগণ সম্মান ও গোপনীয়তার সাথে গোসল সম্পন্ন করেন।", "The immediate Ahl al-Bayt washed the blessed body of the Prophet ﷺ.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "MEDIUM"),

        ("রাসূলুল্লাহ ﷺ-এর মৃত্যুর পর তাঁর কোনো পার্থিব উত্তরাধিকার সম্পত্তি ভাগ হয়নি কেন?", "Why was the Prophet's ﷺ estate not distributed among heirs as inheritance?",
         ["নবী-রাসূলদের কোনো সম্পত্তি উত্তরাধিকারীদের মাঝে বণ্টিত হয় না, যা থাকে তা সদকা হিসেবে গণ্য হয়", "সম্পদ ছিল না", "আইন ছিল না", "ভুলে যাওয়া হয়েছিল"], ["Prophets leave no inheritance; whatever they leave behind is Sadaqah", "No wealth", "No law", "Forgotten"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: 'আমরা নবীর দল কোনো ওয়ারিশ রেখে যাই না, আমরা যা রেখে যাই তা সদকা।' ", "Prophets do not leave monetary inheritance; knowledge and Sadaqah remain.", "সহীহ বুখারী: ৩০৯৩", "Sahih Bukhari: 3093", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর প্রতি ভালোবাসাকে নিজের জান, মাল ও সন্তানের চেয়েও বেশি করার ব্যাপারে হাদিসের হুকুম কী?", "What is the degree of love for the Prophet ﷺ required for complete faith?",
         ["নিজের প্রাণ, সন্তান ও সমগ্র মানবজাতির চেয়ে বেশি ভালোবাসা ঈমানের অপরিহার্য শর্ত", "ঐচ্ছিক পছন্দ", "সাধারণ শ্রদ্ধা", "অপ্রয়োজনীয়"], ["Loving him more than one's self, family, and all mankind is mandatory for faith", "Optional", "General respect", "Unnecessary"], 0,
         "রাসূল ﷺ বলেছেন: তোমাদের কেউ ততক্ষণ মুমিন হবে না যতক্ষণ না আমি তার নিকট তার পিতা, সন্তান ও সব মানুষের চেয়ে প্রিয়তম হই।", "None of you believes until I am dearer to him than his parent, child, and all of mankind.", "সহীহ বুখারী: ১৫", "Sahih Bukhari: 15", "EASY")
    ]
    return [('Q_STR_' + str(i+1).zfill(3), 'seerat_un_nabi', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_08_sahaba():
    items = [
        ("খুলাফায়ে রাশিদীনের প্রথম খলিফা হযরত আবু বকর (রা.)-এর আসল নাম কী ছিল?", "What was the real name of Caliph Abu Bakr As-Siddiq (RA)?",
         ["আব্দুল্লাহ ইবনে আবি কুহাফা", "আমর ইবনে উসমান", "আব্দুর রহমান", "সা'দ ইবনে আবি ওয়াক্কাস"], ["Abdullah ibn Abi Quhafah", "Amr ibn Uthman", "Abdur Rahman", "Sa'd ibn Abi Waqqas"], 0,
         "আবু বকর (রা.)-এর আসল নাম আব্দুল্লাহ এবং উপাধি ছিল 'আতিক' ও 'সিদ্দিক'।", "Abu Bakr's name was Abdullah ibn Abi Quhafah, titled As-Siddiq.", "আল-ইসাবাহ", "Al-Isabah", "EASY"),

        ("হযরত আবু বকর (রা.)-কে রাসূলুল্লাহ ﷺ কোন্ ঘটনার পর 'আস-সিদ্দিক' (পরম সত্যবাদী) উপাধিতে ভূষিত করেন?", "Following which miraculous event did the Prophet ﷺ title Abu Bakr as 'As-Siddiq'?",
         ["ইসরা ও মিরাজের ঘটনা", "মক্কা বিজয়", "বদর যুদ্ধ", "হুদাইবিয়ার সন্ধি"], ["The Miracle of Isra and Mi'raj", "Conquest of Makkah", "Battle of Badr", "Treaty of Hudaybiyyah"], 0,
         "মিরাজের ঘটনা শোনার সাথে সাথে কোনো দ্বিধা ছাড়া বিশ্বাস করার কারণে তিনি সিদ্দিক উপাধি পান।", "Abu Bakr believed the Mi'raj without hesitation and was named As-Siddiq.", "আল-মুসতাদরাক", "Al-Mustadrak", "EASY"),

        ("ইসলাম গ্রহণের পূর্বে কুরাইশদের মধ্যে তীব্র বিরোধী থাকা সত্ত্বেও কোন মহান সাহাবী খলিফা হয়ে 'আল-ফারুক' উপাধি পান?", "Which companion, once a fierce opponent, embraced Islam and became known as 'Al-Farooq'?",
         ["হযরত উমর ইবনুল খাত্তাব (রা.)", "হযরত আবু বকর (রা.)", "হযরত উসমান (রা.)", "হযরত খালিদ (রা.)"], ["Umar ibn al-Khattab (RA)", "Abu Bakr (RA)", "Uthman (RA)", "Khalid (RA)"], 0,
         "সত্য ও মিথ্যার মাঝে সুস্পষ্ট পার্থক্যকারী হিসেবে রাসূল ﷺ উমর (রা.)-কে ফারুক উপাধি দেন।", "The Prophet ﷺ titled Umar (RA) as Al-Farooq (The Distinguisher between truth and falsehood).", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),

        ("হযরত উমর (রা.)-এর খেলাফতকালে জেরুজালেমের (বাইতুল মুকাদ্দাস) চাবি কার নিকট সরাসরি হস্তান্তর করা হয়?", "To whom were the keys of Jerusalem handed over during the Muslim conquest?",
         ["হযরত উমর ইবনুল খাত্তাব (রা.)-এর নিকট ব্যক্তিগতভাবে", "খালিদ বিন ওয়ালিদ", "আবু উবাইদাহ", "আমর ইবনুল আস"], ["Umar ibn al-Khattab (RA) personally", "Khalid bin Walid", "Abu Ubaidah", "Amr ibn al-Aas"], 0,
         "খ্রিষ্টান প্যাট্রিয়ার্ক সোফ্রোনিয়াস খলিফা উমর (রা.)-এর অনন্য বিনয় ও সততা দেখে তাঁর হাতে চাবি তুলে দেন।", "Patriarch Sophronius personally surrendered the keys of Jerusalem to Caliph Umar (RA).", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর দুই কন্যাকে (রুকাইয়্যাহ ও উম্মে কুলসুম) বিয়ে করার কারণে হযরত উসমান (রা.) কী উপাধি পান?", "Which title was granted to Uthman ibn Affan (RA) for marrying two daughters of the Prophet ﷺ?",
         ["যুন-নুরাইন (দুই জ্যোতির অধিকারী)", "ফারুক", "সাইফুল্লাহ", "আসাদুল্লাহ"], ["Dhun-Nurayn (Possessor of Two Lights)", "Al-Farooq", "Saifullah", "Asadullah"], 0,
         "রাসূল ﷺ-এর দুই কন্যাকে একের পর এক বিবাহ করার অনন্য সম্মানে তিনি যুন-নুরাইন হন।", "Uthman (RA) was titled Dhun-Nurayn for marrying Ruqayyah and Umm Kulthum.", "আল-ইসাবাহ", "Al-Isabah", "EASY"),

        ("মদীনায় চরম পানির সংকটের সময় 'বীরে রুমা' (রুমা কূপ) নিজের অর্থে কিনে মুসলমানদের জন্য ওয়াকফ করেছিলেন কে?", "Who purchased the well of Rumah (Bi'r Rumah) in Madinah and dedicated it free for public use?",
         ["হযরত উসমান ইবনে আফফান (রা.)", "হযরত আব্দুর রহমান বিন আউফ (রা.)", "হযরত তালহা (রা.)", "হযরত আবু বকর (রা.)"], ["Uthman ibn Affan (RA)", "Abdur Rahman bin Awf (RA)", "Talha (RA)", "Abu Bakr (RA)"], 0,
         "উসমান (রা.) এক ইহুদির কাছ থেকে কূপটি কিনে জান্নাতের ঝর্ণার বিনিময়ে জনগণের জন্য দান করেন।", "Uthman (RA) bought the well of Rumah for public charity seeking Jannah.", "সহীহ বুখারী: ২৭৭৮", "Sahih Bukhari: 2778", "EASY"),

        ("রাসূলুল্লাহ ﷺ বালকদের মধ্যে সর্বপ্রথম কাকে ইসলামের দাওয়াত দিলে তিনি তৎক্ষণাৎ ইসলাম গ্রহণ করেন?", "Who was the first youth/child to embrace Islam upon the Prophet's call?",
         ["হযরত আলী ইবনে আবি তালিব (রা.)", "হযরত যায়েদ বিন হারেসা (রা.)", "হযরত উসামা বিন যায়েদ (রা.)", "হযরত আব্দুল্লাহ ইবনে আব্বাস (রা.)"], ["Ali ibn Abi Talib (RA)", "Zayd ibn Harithah (RA)", "Usama ibn Zayd (RA)", "Abdullah ibn Abbas (RA)"], 0,
         "দশ বছর বয়সে হযরত আলী (রা.) শিশুদের মধ্যে সর্বপ্রথম ইসলাম গ্রহণ করেন।", "Ali (RA) was the first child/youth to accept Islam at around age 10.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("হিজরতের রাতে নিজের জীবনের ঝুঁকি নিয়ে রাসূল ﷺ-এর বিছানায় চাদর মুড়ি দিয়ে শুয়েছিলেন কোন নির্ভীক সাহাবী?", "Which fearless companion risked his life sleeping in the Prophet's bed on the night of Hijrah?",
         ["হযরত আলী ইবনে আবি তালিব (রা.)", "হযরত আবু বকর (রা.)", "হযরত তালহা (রা.)", "হযরত যুবায়ের (রা.)"], ["Ali ibn Abi Talib (RA)", "Abu Bakr (RA)", "Talha (RA)", "Zubayr (RA)"], 0,
         "কুরাইশদের গচ্ছিত আমানত ফেরত দেওয়ার জন্য আলী (রা.) নবীজির বিছানায় মৃত্যুকে তুচ্ছ করে শয়ন করেন।", "Ali (RA) slept in the Prophet's bed to return deposits and confuse assassins.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("উম্মতের সবচেয়ে বিশ্বস্ত ব্যক্তি বা 'আমিনুল উম্মাহ' (Trustee of the Ummah) কাকে বলা হয়েছে?", "Who was titled 'Ameen al-Ummah' (The Trustee of this Nation) by the Prophet ﷺ?",
         ["হযরত আবু উবাইদাহ ইবনুল জাররাহ (রা.)", "হযরত সা'দ বিন আবি ওয়াক্কাস (রা.)", "হযরত সাঈদ বিন যায়েদ (রা.)", "হযরত মুয়ায বিন জাবাল (রা.)"], ["Abu Ubaidah ibn al-Jarrah (RA)", "Sa'd ibn Abi Waqqas (RA)", "Sa'eed ibn Zayd (RA)", "Mu'adh ibn Jabal (RA)"], 0,
         "রাসূল ﷺ বলেন: প্রত্যেক উম্মতের একজন বিশ্বস্ত আমানতদার থাকে, আমাদের উম্মতের আমানতদার আবু উবাইদাহ।", "Every nation has a trustworthy guardian, and ours is Abu Ubaidah ibn al-Jarrah.", "সহীহ বুখারী: ৩৭৪৪", "Sahih Bukhari: 3744", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর হাওয়ারী বা বিশেষ অন্তরঙ্গ সহচর (Disciple) উপাধিতে কাকে ভূষিত করা হয়েছিল?", "Who was designated as the Hawari (Close Disciple) of the Prophet ﷺ?",
         ["হযরত যুবায়ের ইবনুল আওয়াম (রা.)", "হযরত তালহা ইবনে উবাইদুল্লাহ (রা.)", "হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত হামযাহ (রা.)"], ["Zubayr ibn al-Awwam (RA)", "Talha ibn Ubaydullah (RA)", "Khalid bin Walid (RA)", "Hamzah (RA)"], 0,
         "রাসূল ﷺ বলেন: প্রত্যেক নবীর একজন হাওয়ারী থাকে, আর আমার হাওয়ারী হলো যুবায়ের।", "Every Prophet has a disciple, and my disciple is Az-Zubayr.", "সহীহ বুখারী: ২৯৯৭", "Sahih Bukhari: 2997", "EASY"),

        ("উহুদের যুদ্ধে রাসূল ﷺ-কে রক্ষা করতে গিয়ে নিজের শরীরকে ঢাল বানিয়ে ৮০টিরও বেশি আঘাত পেয়েছিলেন কোন বীর সাহাবী?", "Which companion acted as a human shield for the Prophet ﷺ at Uhud, sustaining 80+ wounds?",
         ["হযরত তালহা ইবনে উবাইদুল্লাহ (রা.)", "হযরত সা'দ বিন আবি ওয়াক্কাস (রা.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)"], ["Talha ibn Ubaydullah (RA)", "Sa'd ibn Abi Waqqas (RA)", "Abu Bakr (RA)", "Umar (RA)"], 0,
         "তালহা (রা.) নিজের হাত দিয়ে তীর ঠেকিয়ে হাত অবশ করে ফেলেন; তাঁকে 'জীবন্ত শহীদ' বলা হতো।", "Talha (RA) shielded the Prophet ﷺ with his own body and was called a living martyr.", "জামে আত-তিরমিযী: ৩৭৩৮", "Jami at-Tirmidhi: 3738", "EASY"),

        ("ইসলামে প্রথম তীর নিক্ষেপকারী এবং যার দোয়ার ব্যাপারে রাসূল ﷺ বিশেষ শুভকামনা করেছিলেন তিনি কে?", "Who was the first companion to shoot an arrow for Islam and whose supplications were always granted?",
         ["হযরত সা'দ বিন আবি ওয়াক্কাস (রা.)", "হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত আবু উবাইদাহ (রা.)", "হযরত আমর ইবনুল আস (রা.)"], ["Sa'd ibn Abi Waqqas (RA)", "Khalid bin Walid (RA)", "Abu Ubaidah (RA)", "Amr ibn al-Aas (RA)"], 0,
         "রাসূল ﷺ সা'দ (রা.)-এর জন্য দোয়া করেন: 'হে আল্লাহ! সা'দের তীর লক্ষ্যভেদী করুন এবং তার দোয়া কবুল করুন।' ", "Sa'd (RA) was the first archer in Islam whose prayers were granted.", "জামে আত-তিরমিযী: ৩৭৫১", "Jami at-Tirmidhi: 3751", "EASY"),

        ("কুরআনের শ্রেষ্ঠ তাফসীরকারক ও অগাধ জ্ঞানের অধিকারী হিসেবে 'রাইসুল মুফাসসিরীন' বলা হয় কাকে?", "Who was titled Rais al-Mufassireen (Master of Quranic Commentators) among the Sahaba?",
         ["হযরত আব্দুল্লাহ ইবনে আব্বাস (রা.)", "হযরত আব্দুল্লাহ ইবনে মাসউদ (রা.)", "হযরত যায়েদ বিন সাবিত (রা.)", "হযরত উবাই বিন কাব (রা.)"], ["Abdullah ibn Abbas (RA)", "Abdullah ibn Mas'ud (RA)", "Zayd ibn Thabit (RA)", "Ubayy bin Ka'b (RA)"], 0,
         "রাসূল ﷺ ইবনে আব্বাসের জন্য দোয়া করেছিলেন: 'হে আল্লাহ! তাকে দ্বীনের গভীর প্রজ্ঞা ও কুরআনের তাফসীর শিখিয়ে দিন।' ", "The Prophet ﷺ prayed for Ibn Abbas to be granted deep wisdom in Quranic interpretation.", "সহীহ বুখারী: ১৪৩", "Sahih Bukhari: 143", "EASY"),

        ("কুরআন তিলাওয়াত এতো মিষ্টি ও সুরেলা ছিল যে রাসূল ﷺ কার তিলাওয়াত শুনে মুগ্ধ হয়ে অশ্রুপাত করতেন?", "Whose melodious recitation of the Quran moved the Prophet ﷺ to tears?",
         ["হযরত আব্দুল্লাহ ইবনে মাসউদ (রা.)", "হযরত বিলাল (রা.)", "হযরত যায়েদ (রা.)", "হযরত সালমান (রা.)"], ["Abdullah ibn Mas'ud (RA)", "Bilal (RA)", "Zayd (RA)", "Salman (RA)"], 0,
         "ইবনে মাসউদ (রা.) যখন সূরা নিসা তিলাওয়াত করতেন তখন নবীজির দু'চোখ বেয়ে অশ্রু ঝরতো।", "The Prophet ﷺ loved listening to Ibn Mas'ud's heart-melting recitation.", "সহীহ বুখারী: ৫০৫০", "Sahih Bukhari: 5050", "EASY"),

        ("হালাল ও হারামের বিধানে সাহাবীদের মধ্যে সবচেয়ে বেশি প্রাজ্ঞ আলেম কাকে ঘোষণা করা হয়েছিল?", "Who was described by the Prophet ﷺ as the most knowledgeable in Halal and Haram?",
         ["হযরত মুয়ায ইবনে জাবাল (রা.)", "হযরত আবু হুরায়রা (রা.)", "হযরত আনাস বিন মালিক (রা.)", "হযরত খুবাইব (রা.)"], ["Mu'adh ibn Jabal (RA)", "Abu Hurairah (RA)", "Anas ibn Malik (RA)", "Khubayb (RA)"], 0,
         "নবীজি ﷺ মুয়ায (রা.)-কে ইয়েমেনের বিচারক ও গভর্নর হিসেবে পাঠিয়েছিলেন।", "Mu'adh ibn Jabal (RA) was appointed governor to Yemen for his legal acumen.", "জামে আত-তিরমিযী: ৩৭৯০", "Jami at-Tirmidhi: 3790", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর দশ বছর বয়সে খাদেম হিসেবে নিয়োজিত হয়ে আজীবন সেবা করেছিলেন কোন আনসারী সাহাবী?", "Which companion served the Prophet ﷺ devotedly for ten continuous years from childhood?",
         ["হযরত আনাস ইবনে মালিক (রা.)", "হযরত সা'দ বিন উবাদাহ (রা.)", "হযরত আবু দারদা (রা.)", "হযরত জাবির বিন আব্দুল্লাহ (রা.)"], ["Anas ibn Malik (RA)", "Sa'd bin Ubadah (RA)", "Abu Darda (RA)", "Jabir ibn Abdullah (RA)"], 0,
         "আনাস (রা.) বলেন: দশ বছরে রাসূল ﷺ আমাকে কোনো কাজে কখনো 'উফ' বা 'কেন এটা করলে' বলে ধমক দেননি।", "Anas (RA) served the Prophet ﷺ for 10 years without ever hearing a harsh word.", "সহীহ বুখারী: ২৭৬০", "Sahih Bukhari: 2760", "EASY"),

        ("মদীনায় ইসলামের প্রথম রাষ্ট্রদূত হিসেবে যাকে দাওয়াত নিয়ে পাঠানো হয়েছিল সেই সুদর্শন তরুণ সাহাবী কে?", "Who was the young handsome companion appointed as the first ambassador to Madinah?",
         ["হযরত মুসআব ইবনে উমাইর (রা.)", "হযরত জাফর ইবনে আবি তালিব (রা.)", "হযরত উসামা বিন যায়েদ (রা.)", "হযরত আব্দুল্লাহ বিন রাওয়াহা (রা.)"], ["Mus'ab ibn Umayr (RA)", "Ja'far ibn Abi Talib (RA)", "Usama ibn Zayd (RA)", "Abdullah ibn Rawahah (RA)"], 0,
         "মুসআব (রা.) মক্কায় প্রাচুর্য ছেড়ে ইসলামের জন্য জীবন উৎসর্গ করেন এবং মদীনার ঘরে ঘরে ইসলাম পৌঁছান।", "Mus'ab (RA) successfully brought Islam into almost every household in Madinah.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("হাবশার খ্রিষ্টান বাদশাহ নাজ্জাশীর দরবারে সূরা মারিয়ামের হৃদয়গ্রাহী আয়াতসমূহ তিলাওয়াত করে বুঝিয়েছিলেন কে?", "Who recited Surah Maryam in the court of King Negus of Abyssinia, moving him to tears?",
         ["হযরত জাফর ইবনে আবি তালিব (রা.)", "হযরত উসমান (রা.)", "হযরত আব্দুর রহমান বিন আউফ (রা.)", "হযরত যুবায়ের (রা.)"], ["Ja'far ibn Abi Talib (RA)", "Uthman (RA)", "Abdur Rahman bin Awf (RA)", "Zubayr (RA)"], 0,
         "জাফর (রা.)-এর বক্তব্যে বাদশাহ নাজ্জাশী কেঁদে ফেলেন এবং মুসলিমদের পূর্ণ নিরাপত্তা দান করেন।", "Ja'far (RA) articulated the beauty of Islam and Jesus's true status in the royal court.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("মুতার যুদ্ধে দুই হাত বিচ্ছিন্ন হয়ে শাহাদাত বরণের পর জান্নাতে ডানা লাভের সুসংবাদ পান কোন সাহাবী?", "Which companion had both arms severed in battle and was promised wings in Jannah (At-Tayyar)?",
         ["হযরত জাফর ইবনে আবি তালিব (রা.)", "হযরত যায়েদ বিন হারেসা (রা.)", "হযরত আব্দুল্লাহ বিন রাওয়াহা (রা.)", "হযরত হামযাহ (রা.)"], ["Ja'far ibn Abi Talib (RA) (Ja'far at-Tayyar)", "Zayd ibn Harithah (RA)", "Abdullah ibn Rawahah (RA)", "Hamzah (RA)"], 0,
         "রাসূল ﷺ বলেন: আমি জাফরকে জান্নাতে ফেরেশতাদের সাথে দুই ডানা মেলে উড়তে দেখেছি।", "The Prophet ﷺ saw Ja'far flying in Paradise with two emerald wings.", "সহীহ বুখারী: ৩৭০৯", "Sahih Bukhari: 3709", "EASY"),

        ("রাসূলুল্লাহ ﷺ মাত্র ১৮ বছর বয়সে রোমানদের বিরুদ্ধে বিশাল মুসলিম বাহিনীর প্রধান সেনাপতি নিযুক্ত করেছিলেন কাকে?", "Whom did the Prophet ﷺ appoint as commander-in-chief of the army against Byzantines at age 18?",
         ["হযরত উসামা বিন যায়েদ (রা.)", "হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত আলী (রা.)", "হযরত সা'দ (রা.)"], ["Usama ibn Zayd (RA)", "Khalid bin Walid (RA)", "Ali (RA)", "Sa'd (RA)"], 0,
         "নবীজি ﷺ ওফাতের পূর্বে উসামা (রা.)-এর নেতৃত্বে সেনাবাহিনী প্রস্তুত করেন যা আবু বকর (রা.) প্রেরণ করেন।", "Usama ibn Zayd (RA) was entrusted with supreme military command at age 18.", "সহীহ বুখারী: ৪৪৬৯", "Sahih Bukhari: 4469", "EASY"),

        ("ইসলামের প্রথম কবি বা 'শায়েরুর রাসূল' (রাসূলের কবি) হিসেবে কাফেরদের অপপ্রচারের কাব্যিক জবাব দিতেন কে?", "Which companion was the official poet of the Prophet ﷺ who defended Islam with poetry?",
         ["হযরত হাসসান ইবনে সাবিত (রা.)", "হযরত কাব ইবনে মালিক (রা.)", "হযরত আব্দুল্লাহ বিন রাওয়াহা (রা.)", "হযরত লাবীদ (রা.)"], ["Hassan ibn Thabit (RA)", "Ka'b ibn Malik (RA)", "Abdullah ibn Rawahah (RA)", "Labid (RA)"], 0,
         "রাসূল ﷺ হাসসান (রা.)-এর জন্য মসজিদে নববীতে মিম্বর রাখতেন এবং জিবরাইল (আ.) তাঁকে সাহায্য করতেন।", "Hassan ibn Thabit was aided by Jibreel (AS) in defending the Prophet ﷺ.", "সহীহ বুখারী: ৩২১৩", "Sahih Bukhari: 3213", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর জীবদ্দশায় তাঁর সকল যুদ্ধ ও অভিযানে উপস্থিত থাকা বিশিষ্ট সাহাবী কে ছিলেন?", "Who participated in every single major campaign alongside the Prophet ﷺ?",
         ["হযরত আবু বকর সিদ্দিক ও হযরত আলী (রা.)", "হযরত মুয়াবিয়া (রা.)", "হযরত আমর ইবনুল আস (রা.)", "হযরত ওয়াহশী (রা.)"], ["Abu Bakr As-Siddiq and Ali (RA)", "Muawiyah (RA)", "Amr ibn al-Aas (RA)", "Wahshi (RA)"], 0,
         "আবু বকর ও আলী (রা.) সর্বদা ছায়ার মতো রাসূল ﷺ-এর পাশে প্রতিটি অভিযানে বীরত্বের সাথে লড়েছেন।", "Abu Bakr and Ali (RA) stood steadfast beside the Prophet ﷺ in all battles.", "আল-ইসাবাহ", "Al-Isabah", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর দৌহিত্র হযরত হাসান ও হুসাইন (রা.)-কে হাদিসে কী ঘোষণা করা হয়েছে?", "What did the Prophet ﷺ proclaim regarding his grandsons Al-Hasan and Al-Husain (RA)?",
         ["জান্নাতের যুবকদের সর্দার (সাইয়্যিদা শাবাবি আহলিল জান্নাহ)", "সাধারণ ব্যক্তি", "আরবের ধনী", "শাসক"], ["Leaders of the youth in Paradise (Sayyida Shababi Ahl al-Jannah)", "Ordinary men", "Rich men", "Rulers"], 0,
         "রাসূল ﷺ বলেন: হাসান ও হুসাইন হলো জান্নাতের সকল যুবকদের সর্দার এবং আমার নয়নের মণি।", "Al-Hasan and Al-Husain are the leaders of the youth of Paradise.", "জামে আত-তিরমিযী: ৩৭৬৮", "Jami at-Tirmidhi: 3768", "EASY"),

        ("হযরত আব্দুল্লাহ ইবনে উমর (রা.) সাহাবীদের মধ্যে কোন গুণের জন্য সর্বাধিক প্রসিদ্ধ ছিলেন?", "For what exceptional trait was Abdullah ibn Umar (RA) renowned among companions?",
         ["রাসূলুল্লাহ ﷺ-এর সুন্নাহর অক্ষরে অক্ষরে হুবহু অনুসরণ ও নিখুঁত অনুকরণ", "ব্যবসায়িক দক্ষতা", "যুদ্ধযাত্রা", "কবিতা রচনা"], ["Meticulous verbatim emulation of every Sunnah of the Prophet ﷺ", "Trade", "War", "Poetry"], 0,
         "ইবনে উমর (রা.) নবীজি যেখানে যেখানে দাঁড়িয়েছেন বা সালাত পড়েছেন অবিকল সেখানে সেভাবে আমল করতেন।", "Abdullah ibn Umar (RA) strictly replicated the Prophet's ﷺ exact actions.", "সিয়ারু আ'লামিন নুবালা", "Siyar A'lam an-Nubala", "EASY"),

        ("হিজরতের সময় রাসূল ﷺ ও আবু বকর (রা.)-কে খাবার ও খবরাখবর পৌঁছে দিয়ে 'যাতুন নিতাক্বাইন' উপাধি পান কে?", "Who earned the title 'Dhat an-Nitaqayn' (Possessor of Two Belts) during Hijrah?",
         ["হযরত আসমা বিনতে আবু বকর (রা.)", "হযরত আয়েশা (রা.)", "হযরত ফাতিমা (রা.)", "হযরত উম্মে সালমা (রা.)"], ["Asma bint Abi Bakr (RA)", "Aisha (RA)", "Fatimah (RA)", "Umm Salamah (RA)"], 0,
         "আসমা (রা.) নিজের কোমরবন্ধ ছিঁড়ে দু'টুকরো করে পাথেয় ও মশকের মুখ বেঁধেছিলেন।", "Asma (RA) split her waistband to tie provisions for the Prophet ﷺ and her father.", "সহীহ বুখারী: ২৯৭৯", "Sahih Bukhari: 2979", "EASY"),

        ("কুরআনের অন্যতম শ্রেষ্ঠ ক্বারী ও শিক্ষক হিসেবে রাসূল ﷺ কার থেকে কুরআন শিখতে নির্দেশ দিয়েছিলেন?", "From which esteemed master reciter did the Prophet ﷺ encourage learning the Quran?",
         ["হযরত উবাই ইবনে কাব (রা.)", "হযরত আবু হুরায়রা (রা.)", "হযরত খালিদ (রা.)", "হযরত সুহাইল (রা.)"], ["Ubayy ibn Ka'b (RA)", "Abu Hurairah (RA)", "Khalid (RA)", "Suhayl (RA)"], 0,
         "উবাই বিন কাব (রা.) ছিলেন আনসারদের শ্রেষ্ঠ হাফেজ ও ক্বারী।", "Ubayy ibn Ka'b (RA) was acclaimed as the master reciter of the Quran.", "সহীহ বুখারী: ৩৭৫৮", "Sahih Bukhari: 3758", "EASY"),

        ("হযরত আবু বকর (রা.)-এর খেলাফতকালে ভণ্ড নবী মুসাইলামা কাজ্জাবের বিরুদ্ধে পরিচালিত রক্তক্ষয়ী যুদ্ধের নাম কী?", "What was the decisive battle fought against the false prophet Musaylimah al-Kadhdhab?",
         ["ইয়ামামার যুদ্ধ (Battle of Yamama)", "মুতার যুদ্ধ", "ইয়ারমুকের যুদ্ধ", "কাদেসিয়ার যুদ্ধ"], ["Battle of Yamama", "Battle of Mu'tah", "Battle of Yarmouk", "Battle of Qadisiyyah"], 0,
         "ইয়ামামার যুদ্ধে বহু হাফেজে কুরআন শাহাদাত বরণ করেন এবং মুসাইলামা নিহত হয়।", "The Battle of Yamama defeated the false prophet and led to Quran compilation.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("পারস্য সাম্রাজ্যের (সাসানীয়) বিরুদ্ধে ঐতিহাসিক কাদেসিয়ার যুদ্ধে মুসলিম বাহিনীর প্রধান সেনাপতি কে ছিলেন?", "Who was the supreme Muslim commander at the historic Battle of Qadisiyyah against Persia?",
         ["হযরত সা'দ বিন আবি ওয়াক্কাস (রা.)", "হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত আবু উবাইদাহ (রা.)", "হযরত মুসান্না (রা.)"], ["Sa'd ibn Abi Waqqas (RA)", "Khalid bin Walid (RA)", "Abu Ubaidah (RA)", "Muthanna (RA)"], 0,
         "সা'দ (রা.)-এর নেতৃত্বে মুসলিম বাহিনী পারস্যের মহাশক্তিমান রুস্তম বাহিনীকে চূর্ণ করে দেয়।", "Sa'd ibn Abi Waqqas (RA) led Muslims to epochal victory at Qadisiyyah.", "তারিখে তাবারী", "Tarikh al-Tabari", "EASY"),

        ("রোমান বাইজেন্টাইন সাম্রাজ্যের বিরুদ্ধে সিরিয়ায় ঐতিহাসিক ইয়ারমুকের যুদ্ধে প্রধান সেনাপতি কে ছিলেন?", "Who brilliantly commanded the Muslim army at the pivotal Battle of Yarmouk against Rome?",
         ["হযরত খালিদ বিন ওয়ালিদ ও হযরত আবু উবাইদাহ (রা.)", "হযরত সা'দ (রা.)", "হযরত আলী (রা.)", "হযরত উসমান (রা.)"], ["Khalid bin Walid & Abu Ubaidah (RA)", "Sa'd (RA)", "Ali (RA)", "Uthman (RA)"], 0,
         "ইয়ারমুকের যুদ্ধে খালিদ (রা.)-এর সমরকুশলতায় রোমান সম্রাটের বিশাল বাহিনী পর্যুদস্ত হয়।", "Yarmouk permanently dismantled Byzantine rule in the Levant.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("মিসর বিজয় করে সেখানে ইসলামী শাসন ও বিখ্যাত ফুসতাত নগরী প্রতিষ্ঠা করেন কোন সাহাবী?", "Which companion conquered Egypt and founded the historic city of Fustat (Cairo)?",
         ["হযরত আমর ইবনুল আস (রা.)", "হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত তারিক বিন যিয়াদ", "হযরত মুসা বিন নুসাইর"], ["Amr ibn al-Aas (RA)", "Khalid bin Walid (RA)", "Tariq ibn Ziyad", "Musa ibn Nusayr"], 0,
         "খলিফা উমর (রা.)-এর শাসনামলে আমর ইবনুল আস (রা.) মিসর জয় করেন।", "Amr ibn al-Aas (RA) liberated Egypt and built the first mosque in Africa.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),

        ("সাহাবায়ে কেরামের সমালোচনা বা গালি দেওয়ার ব্যাপারে রাসূলুল্লাহ ﷺ-এর কঠোর সতর্কবার্তা কী?", "What did the Prophet ﷺ strictly warn regarding insulting his companions?",
         ["'আমার সাহাবীদের গালি দিও না, ওহুদ পাহাড় সমান স্বর্ণ দান করলেও তাদের সমকক্ষ হতে পারবে না'", "ক্ষমাযোগ্য", "সাধারণ ভুল", "কোনো ক্ষতি নেই"], ["'Do not insult my companions; even donating Mount Uhud in gold cannot match their deeds'", "Pardonable", "Minor mistake", "No harm"], 0,
         "সাহাবাদের মর্যাদা রক্ষা করা ঈমানের দাবি; তাঁদের অসম্মান করা স্পষ্ট বিভ্রান্তি।", "Do not revile my companions; none can attain even a fraction of their status.", "সহীহ বুখারী: ৩৬৭৩", "Sahih Bukhari: 3673", "EASY"),

        ("উম্মুল মুমিনীন হযরত খাদিজা (রা.)-এর নিকট জিবরাইল (আ.) মারফত কার পক্ষ থেকে সালাম পৌঁছানো হয়েছিল?", "From Whom did Jibreel (AS) bring personal greetings (Salam) to Khadijah (RA)?",
         ["আল্লাহ রব্বুল আলামীনের পক্ষ থেকে", "ফেরেশতাদের পক্ষ থেকে", "মক্কার পক্ষ থেকে", "নবীগণের পক্ষ থেকে"], ["From Allah, Lord of the Worlds", "From angels", "From Makkah", "From Prophets"], 0,
         "জিবরাইল (আ.) নবীজিকে বলেন: খাদিজাকে তাঁর রবের পক্ষ থেকে এবং আমার পক্ষ থেকে সালাম দিন।", "Jibreel brought greetings to Khadijah from Allah and glad tidings of a palace in Jannah.", "সহীহ বুখারী: ৩৮২০", "Sahih Bukhari: 3820", "EASY"),

        ("কুরআনে বর্ণিত 'আসহাবুস সুফফাহ' (দরিদ্র আশ্রয়হীন জ্ঞানপিপাসু সাহাবী দল) কোথায় অবস্থান করতেন?", "Where did the scholarly, ascetic companions known as Ashab as-Suffah reside?",
         ["মসজিদে নববীর পেছনের উন্মুক্ত ছায়াযুক্ত চত্বরে", "কাবার পাশে", "মক্কা উপত্যকায়", "জেরুসালেমে"], ["In the rear sheltered platform of Masjid an-Nabawi", "Beside Kaaba", "Makkah", "Jerusalem"], 0,
         "আবু হুরায়রা (রা.)-সহ নিঃস্ব সাহাবীগণ সেখানে দিনরাত কুরআন ও হাদিস অধ্যয়নে মগ্ন থাকতেন।", "Ashab as-Suffah dedicated their lives to studying Hadith and Quran under the Prophet ﷺ.", "সহীহ বুখারী: ৪৪৮", "Sahih Bukhari: 448", "EASY"),

        ("ইসলামের ইতিহাসে সর্বপ্রথম নৌবাহিনী (নৌবহর) গঠন করেছিলেন কোন খলিফা?", "Under which Caliph's reign was the first Muslim naval fleet established?",
         ["হযরত উসমান ইবনে আফফান (রা.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)"], ["Uthman ibn Affan (RA)", "Abu Bakr (RA)", "Umar (RA)", "Ali (RA)"], 0,
         "মুয়াবিয়া (রা.)-এর প্রস্তাবে খলিফা উসমান (রা.) সাইপ্রাস বিজয়ে প্রথম মুসলিম নৌবাহিনী অনুমতি দেন।", "Caliph Uthman (RA) authorized the first Islamic naval expedition to Cyprus.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "MEDIUM"),

        ("সাহাবায়ে কেরামের সামগ্রিক মর্যাদা সম্পর্কে কুরআনে আল্লাহ তাআলা কী চিরন্তন সন্তুষ্টি ঘোষণা করেছেন?", "What eternal pleasure did Allah declare for the Sahaba in Surah At-Tawbah: 100?",
         ["'রাদিয়াল্লাহু আনহুম ওয়া রাদূ আনহু' (আল্লাহ তাদের প্রতি সন্তুষ্ট এবং তারাও আল্লাহর প্রতি সন্তুষ্ট)", "তারা পাপী", "তারা সাধারণ মানুষ", "তাদের বিচার হবে না"], ["'Radhiyallahu Anhum wa Radhu Anhu' (Allah is pleased with them and they are pleased with Him)", "Sinners", "Average", "No trial"], 0,
         "মুহাজির ও আনসার সাহাবীদের জন্য আল্লাহ জান্নাতের সুসংবাদ ও নিজের সন্তুষ্টি অবতীর্ণ করেছেন।", "Allah declared His eternal pleasure with the early Muhajirun and Ansar.", "সূরা আত-তাওবাহ: ১০০", "Surah At-Tawbah: 100", "EASY")
    ]
    return [('Q_SHB_' + str(i+1).zfill(3), 'sahaba_life', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_09_jannah():
    items = [
        ("জান্নাতের মোট প্রধান দরজার সংখ্যা কতটি?", "How many main gates does Jannah have?",
         ["৮টি দরজা", "৭টি দরজা", "১২টি দরজা", "৪টি দরজা"], ["8 Gates", "7 Gates", "12 Gates", "4 Gates"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: জান্নাতের আটটি দরজা রয়েছে যার একটির নাম রাইয়্যান।", "Jannah has eight gates, one of which is Ar-Rayyan for fasting people.", "সহীহ বুখারী: ৩২৫৭", "Sahih Bukhari: 3257", "EASY"),

        ("জান্নাতের কোন্ বিশেষ দরজা দিয়ে কেবলমাত্র রোযাদারগণই প্রবেশ করবেন?", "Through which specific gate of Jannah will only the fasting persons enter?",
         ["বাবুল রাইয়্যান (Ar-Rayyan)", "বাবুস সালাত", "বাবুল জিহাদ", "বাবুস সাদাকাহ"], ["Bab ar-Rayyan", "Bab as-Salah", "Bab al-Jihad", "Bab as-Sadaqah"], 0,
         "কিয়ামতের দিন আহ্বান করা হবে: রোযাদাররা কোথায়? তারা রাইয়্যান দিয়ে প্রবেশের পর দরজা বন্ধ হবে।", "Ar-Rayyan is exclusively reserved for those who observed fasts sincerely.", "সহীহ বুখারী: ১৮৯৬", "Sahih Bukhari: 1896", "EASY"),

        ("জান্নাতের সর্বোচ্চ এবং সবচেয়ে সম্মানিত স্তরের নাম কী যা আরশের ঠিক নিচে অবস্থিত?", "What is the highest and most supreme level of Jannah located directly beneath the Throne?",
         ["জান্নাতুল ফিরদাউস", "জান্নাতুল আদন", "জান্নাতুল মা'ওয়া", "জান্নাতুন নাঈম"], ["Jannat al-Firdaws", "Jannat al-Adn", "Jannat al-Ma'wa", "Jannat an-Na'eem"], 0,
         "রাসূল ﷺ বলেছেন: যখন তোমরা আল্লাহর কাছে জান্নাত চাইবে, তখন জান্নাতুল ফিরদাউস চাইবে।", "When you ask Allah, ask for Al-Firdaws, the highest peak of Paradise.", "সহীহ বুখারী: ২৭৯০", "Sahih Bukhari: 2790", "EASY"),

        ("জান্নাতে মুমিনদের জন্য সবচেয়ে সেরা, পরম ও অতুলনীয় নেয়ামত কোনটি হবে?", "What will be the greatest, most sublime reward for the believers in Jannah?",
         ["আল্লাহ তাআলার পবিত্র সত্তার সরাসরি দীদার বা দর্শন লাভ", "সুস্বাদু ফলমূল", "সোনার প্রাসাদ", "হূরদের সাহচর্য"], ["Beholding the Divine Countenance (Looking at Allah)", "Delicious fruits", "Golden palaces", "Companionship of Hoor"], 0,
         "পর্দা উন্মোচিত হলে জান্নাতীরা সরাসরি রবের নূর প্রত্যক্ষ করবে, যা সকল নেয়ামতের চেয়ে শ্রেষ্ঠ।", "Looking upon the Face of Allah will be the greatest reward in Jannah.", "সহীহ মুসলিম: ১৮১", "Sahih Muslim: 181", "EASY"),

        ("জান্নাতের প্রধান প্রহরী বা দায়িত্বশীল সম্মানিত ফেরেশতার নাম কী?", "What is the name of the angel who is the Chief Keeper and Gatekeeper of Jannah?",
         ["হযরত রিদওয়ান (আ.)", "হযরত মালিক (আ.)", "হযরত জিবরাইল (আ.)", "হযরত মিকাইল (আ.)"], ["Angel Ridwan (AS)", "Angel Malik", "Angel Jibreel", "Angel Mikail"], 0,
         "জান্নাতের তোরণের দায়িত্বশীল ফেরেশতার নাম রেদওয়ান এবং জাহান্নামের দায়িত্বশীল মালিক।", "Angel Ridwan is the gatekeeper of Paradise.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),

        ("জান্নাতে প্রবাহিত চারটি প্রধান নহরের মধ্যে মধুর নহর ছাড়া বাকি তিনটি কিসের?", "Besides pure honey, what do the other 3 eternal rivers of Jannah flow with?",
         ["স্বচ্ছ সুমিষ্ট পানি, বিশুদ্ধ দুধ ও সুস্বাদু পবিত্র শরাব", "তেল ও রস", "পারফিউম ও সুগন্ধি", "সোনার পানি"], ["Crystal pure water, pristine milk, and delicious un-intoxicating wine", "Oil & juice", "Perfume", "Gold water"], 0,
         "সূরা মুহাম্মাদের ১৫ নম্বর আয়াতে পানি, দুধ, নির্ভেজাল শরাব ও মধুর চারটি নহরের বর্ণনা এসেছে।", "Four rivers of water, milk, pure wine, and honey flow in Paradise (Surah Muhammad: 15).", "সূরা মুহাম্মাদ: ১৫", "Surah Muhammad: 15", "EASY"),

        ("কুরআনে বর্ণিত 'কাউসার' নামক নহর আল্লাহ বিশেষভাবে কাকে দান করেছেন?", "To whom did Allah grant the exclusive river and basin of Al-Kawthar?",
         ["বিশ্বনবী হযরত মুহাম্মদ ﷺ", "হযরত ইব্রাহিম (আ.)", "হযরত মূসা (আ.)", "হযরত ঈসা (আ.)"], ["Prophet Muhammad ﷺ", "Prophet Ibrahim (AS)", "Prophet Musa (AS)", "Prophet Isa (AS)"], 0,
         "সূরা আল-কাওসারে আল্লাহ বলেন: 'নিশ্চয়ই আমি আপনাকে কাউসার দান করেছি।' ", "Indeed, We have granted you, [O Muhammad], al-Kawthar.", "সূরা আল-কাওসার: ১", "Surah Al-Kawthar: 1", "EASY"),

        ("হাওযে কাউসারের পানির বৈশিষ্ট্য কেমন যা একবার পান করলে আর কখনো পিপাসা লাগবে না?", "What are the characteristics of the water of Hawd al-Kawthar?",
         ["দুধের চেয়েও সাদা, মধুর চেয়েও মিষ্টি এবং মিশকের চেয়েও সুবাসিত", "স্বাভাবিক পানি", "লবণাক্ত", "রঙিন"], ["Whiter than milk, sweeter than honey, and fragrant than musk", "Normal water", "Salty", "Colored"], 0,
         "হাওযে কাউসারের এক ঢোক পানি পানকারী হাশরের তীব্র উত্তাপেও চিরতরে তৃষ্ণা থেকে মুক্তি পাবে।", "Whoever drinks from Kawthar will never experience thirst again.", "সহীহ বুখারী: ৬৫৭৯", "Sahih Bukhari: 6579", "EASY"),

        ("জান্নাতে মুমিনদের বয়স কত থাকবে এবং তারা কি কখনো বার্ধক্য বা অসুস্থতায় আক্রান্ত হবে?", "What will be the eternal age of believers in Jannah, and will they ever age or fall sick?",
         ["৩৩ বছর চিরযৌবন থাকবে, কখনো রোগ, বার্ধক্য বা মৃত্যু স্পর্শ করবে না", "৪০ বছর", "৫০ বছর", "১০০ বছর"], ["33 years of eternal youth, never aging, falling ill, or dying", "40 years", "50 years", "100 years"], 0,
         "জান্নাতবাসীরা হযরত আদম (আ.)-এর সুরত ও হযরত ঈসা (আ.)-এর ৩৩ বছরের চিরন্তন যৌবনে বাস করবে।", "People of Jannah will enter at age 33 without sickness, decay, or death.", "জামে আত-তিরমিযী: ২৫৪৫", "Jami at-Tirmidhi: 2545", "EASY"),

        ("জান্নাতে মানুষের শারীরিক আকৃতি বা উচ্চতা কার আকৃতির অনুরূপ (৬০ হাত দীর্ঘ) হবে?", "Whose physical stature (60 cubits tall) will believers inherit in Jannah?",
         ["হযরত আদম (আ.)", "হযরত নূহ (আ.)", "হযরত মূসা (আ.)", "হযরত সুলাইমান (আ.)"], ["Prophet Adam (AS)", "Prophet Nuh (AS)", "Prophet Musa (AS)", "Prophet Sulaiman (AS)"], 0,
         "জান্নাতে প্রবেশকারী প্রতিটি মানুষ আদি পিতা আদম (আ.)-এর মতো ৬০ হাত দীর্ঘ ও জ্যোতির্ময় হবে।", "Believers will enter Paradise in the majestic 60-cubit form of Adam (AS).", "সহীহ বুখারী: ৩৩২৬", "Sahih Bukhari: 3326", "EASY"),

        ("জান্নাতে প্রবেশকারী প্রথম মানবদল বা ভিআইপিদের চেহারা কিসের মতো উজ্জ্বল হবে?", "What will the first group entering Jannah resemble in radiant beauty?",
         ["পূর্ণিমার চাঁদের আলোর মতো জ্যোতির্ময়", "সূর্যের মতো", "নক্ষত্রের মতো", "স্বর্ণের মতো"], ["The radiant full moon of the 14th night", "The sun", "Stars", "Gold"], 0,
         "প্রথম দলের চেহারা পূর্ণিমার চাঁদের মতো এবং দ্বিতীয় দলের চেহারা উজ্জ্বল নক্ষত্রের মতো জ্বলজ্বল করবে।", "The first group entering Jannah will shine like the full moon.", "সহীহ বুখারী: ৩২৪৫", "Sahih Bukhari: 3245", "EASY"),

        ("জান্নাতে কি কোনো ধরনের মলমূত্র, থুতু বা ময়লা-আবর্জনা নির্গমনের প্রয়োজন হবে?", "Will there be any need for bodily excretion, spitting, or waste in Jannah?",
         ["না, কোনো মলমূত্র নেই; সুগন্ধি ঢেকুর ও মিশকযুক্ত ঘামের মাধ্যমে খাবার হজম হবে", "হ্যাঁ, সামান্য হবে", "পানির মতো হবে", "স্বাভাবিক থাকবে"], ["No waste; digestion occurs via aromatic belching and musk-scented perspiration", "Yes, slight", "Water-like", "Normal"], 0,
         "জান্নাতের খাবার অত্যন্ত পুষ্টিকর ও নির্ভেজাল, সামান্য সুবাসিত ঘামের মাধ্যমে তা সম্পূর্ণ নিঃশেষ হবে।", "Food in Jannah is digested effortlessly through fragrant musk-scented perspiration.", "সহীহ মুসলিম: ২৮৩৫", "Sahih Muslim: 2835", "EASY"),

        ("জান্নাতের গাছপালার ছায়া কতটা সুবিশাল যার ছায়ায় একজন দ্রুতগামী ঘোড়সওয়ার শত বছর চললেও শেষ করতে পারবে না?", "How vast is the shade of trees in Jannah described in Hadith?",
         ["দ্রুতগামী ঘোড়সওয়ার ১০০ বছর দৌড়ালেও ছায়া শেষ করতে পারবে না", "১০ বছর", "১ বছর", "৫০ বছর"], ["A fast rider would travel 100 years beneath its shade without finishing it", "10 years", "1 year", "50 years"], 0,
         "জান্নাতের সুবিশাল ছায়া ও ডালপালা চিরসবুজ ও অসীম বিস্তৃত।", "A rider under the shade of a tree in Paradise would not cross it in 100 years.", "সহীহ বুখারী: ৩২৫১", "Sahih Bukhari: 3251", "EASY"),

        ("জান্নাতের কোন বিখ্যাত ঝর্ণার পানির আলোচনা সূরা আদ-দাহরে 'কাফুর' ও 'সালসাবিল' নামে এসেছে?", "Which blessed springs of Jannah are named in Surah Al-Insan (Ad-Dahr)?",
         ["সালসাবিল ও কাফুর", "যমযম", "কাউসার", "তাসনীম"], ["Salsabeel and Kafur", "Zamzam", "Kawthar", "Tasneem"], 0,
         "জান্নাতীদের সুবাসিত পানীয়ের জন্য কাফুর ও সালসাবিল ঝর্ণার সুমিষ্ট ধারা প্রবাহিত হবে।", "Salsabeel and Kafur are exquisite flowing springs of Jannah.", "সূরা আল-ইনসান: ৫-১৮", "Surah Al-Insan: 5-18", "EASY"),

        ("জান্নাতের সর্বোচ্চ মর্যাদাবান ও মুকাররাবীনদের পানীয়ের জন্য নির্ধারিত বিশেষ ঝর্ণার নাম কী?", "What is the name of the exclusive spring reserved for the foremost in closeness (Muqarrabun)?",
         ["তাসনীম (Tasneem)", "সালসাবিল", "কাফুর", "কাউসার"], ["Tasneem", "Salsabeel", "Kafur", "Kawthar"], 0,
         "তাসনীম হলো জান্নাতের সর্বোৎকৃষ্ট পানীয় যা সাধারণদের পানীয়তে মেশানো হবে এবং নৈকট্যপ্রাপ্তরা সরাসরি পান করবে।", "Tasneem is the purest celestial drink for the highest ranks.", "সূরা আল-মুতাফফিফীন: ২৭-২৮", "Surah Al-Mutaffifin: 27-28", "MEDIUM"),

        ("যে ব্যক্তি দিনে ও রাতে ১২ রাকাত সুন্নাতে মুয়াক্কাদাহ সালাত নিয়মিত আদায় করে তার জন্য আল্লাহর ওয়াদা কী?", "What is promised to whoever prays 12 Sunnah Mu'akkadah Rak'ahs daily?",
         ["জান্নাতে তার জন্য একটি আলিশান প্রাসাদ নির্মাণ করা হবে", "ধনী হবে", "ক্ষমতা পাবে", "দীর্ঘায়ু হবে"], ["A palace will be built for him in Jannah", "Become rich", "Gain power", "Long life"], 0,
         "রাসূল ﷺ বলেন: যে দিন-রাতে ১২ রাকাত সুন্নাত পড়ে আল্লাহ তার জন্য জান্নাতে একটি ঘর বানান।", "A house in Paradise is built for whoever observes 12 voluntary Sunnah prayers daily.", "সহীহ মুসলিম: ৭২৮", "Sahih Muslim: 728", "EASY"),

        ("কুরআনে কারীমে জান্নাতুল আদনে প্রবেশের জন্য মুমিনদের অভ্যর্থনায় ফেরেশতারা কী বলবেন?", "With which greeting will angels welcome the believers at the gates of Jannah?",
         ["'সালামুন আলাইকুম বিমা সাবারতুম' (তোমাদের সবরের কারণে তোমাদের ওপর শান্তি বর্ষিত হোক)", "স্বাগতম", "তোমরা সফল", "ভিতরে এসো"], ["'Salamun Alaykum bima Sabartum' (Peace be upon you for what you patiently endured)", "Welcome", "You won", "Come in"], 0,
         "ফেরেশতাগণ সকল দরজা দিয়ে অভিবাদন জানাতে এসে দুনিয়ার সবরের বিনিময়ে চিরশান্তির জান্নাত উপহার দেবেন।", "Angels will enter from every gate saying: Peace be upon you for your patience.", "সূরা আর-রাদ: ২৪", "Surah Ar-Ra'd: 24", "EASY"),

        ("জান্নাতীদের মাথার মুকুটের একটি ক্ষুদ্র ইয়াকুত পাথরের ঔজ্জ্বল্য দুনিয়ার কিসের চেয়ে শ্রেষ্ঠ?", "How does a single ruby on a believer's crown in Jannah compare to this worldly life?",
         ["সমগ্র পৃথিবী এবং পৃথিবীর মধ্যে যা কিছু আছে তার চেয়েও উত্তম ও উজ্জ্বল", "সূর্যের সমান", "১০০টি হিরার সমান", "চাঁদের সমান"], ["Better than the entire world and everything contained in it", "Equal to sun", "100 diamonds", "Equal to moon"], 0,
         "জান্নাতীদের পোশাক রেশমের এবং গহনা হবে খাঁটি সোনা, রূপা ও মহামূল্যবান মোতির।", "A single gem on a crown in Jannah outshines the entire worldly realm.", "জামে আত-তিরমিযী: ২৫৬২", "Jami at-Tirmidhi: 2562", "EASY"),

        ("জান্নাতে মুমিন যা কিছু মনে মনে কামনা করবে তৎক্ষণাৎ তার সামনে হাজির হওয়ার ব্যাপারে আল্লাহর ঘোষণা কী?", "What did Allah promise regarding whatever believers desire in Jannah?",
         ["'লাহুম মা ইয়াশাউনা ফীহা ওয়া লাদাইনা মাযীদ' (সেখানে যা চাইবে তাই পাবে এবং আমার কাছে আছে আরও অধিক)", "কিছু পাবে", "পরিশ্রম করতে হবে", "অপেক্ষা করতে হবে"], ["'They will have whatever they wish therein, and with Us is more' (Surah Qaf: 35)", "Some things", "Must work", "Must wait"], 0,
         "জান্নাতে কল্পনার সাথে সাথেই যেকোনো ফলমূল বা পাখির গোশত নিমিষে পরিবেশন করা হবে।", "In Paradise, every wish and longing is fulfilled instantly and magnified.", "সূরা কাফ: ৩৫", "Surah Qaf: 35", "EASY"),

        ("দুনিয়াতে যে ব্যক্তি আল্লাহর ভয়ে রাগ সংবরণ করে, কিয়ামতের দিন আল্লাহ তাকে জান্নাতের কী অধিকার দেবেন?", "What privilege will Allah grant on Judgment Day to one who restrains his anger?",
         ["সমগ্র সৃষ্টির সামনে ডেকে জান্নাতের হূরদের মধ্য থেকে পছন্দমতো বেছে নেওয়ার অধিকার", "টাকা দেওয়া", "বাদশাহ বানানো", "যুদ্ধজয়ী করা"], ["He will be called before all creation to choose whichever Hoor he desires", "Given money", "Made king", "War glory"], 0,
         "ক্ষমতা থাকা সত্ত্বেও রাগ দমনকারী মুমিনকে আল্লাহ কিয়ামতে বিশাল সম্মান দান করবেন।", "Whoever controls anger while able to act will choose his rewards openly in Jannah.", "সুনানে আবু দাউদ: ৪৭৭৭", "Sunan Abi Dawud: 4777", "EASY"),

        ("জান্নাতকে আল্লাহ তাআলা দুনিয়াতে মানুষের চোখের সামনে কী দিয়ে আবৃত করে রেখেছেন?", "With what has Paradise been enveloped/surrounded in this worldly test?",
         ["নফসের অপছন্দনীয় ও কষ্টসাধ্য নেক আমল দিয়ে (মাকারেহ)", "বিলাসিতা দিয়ে", "অর্থ দিয়ে", "ঘুম দিয়ে"], ["Hardships and things disliked by the lower self (Makarih)", "Luxuries", "Money", "Sleep"], 0,
         "রাসূল ﷺ বলেছেন: জান্নাতকে কষ্টসাধ্য দায়িত্ব দিয়ে এবং জাহান্নামকে কুপ্রবৃত্তির আকর্ষণ দিয়ে ঘিরে রাখা হয়েছে।", "Paradise is surrounded by hardships, and Hell is surrounded by worldly desires.", "সহীহ মুসলিম: ২৮২২", "Sahih Muslim: 2822", "EASY"),

        ("জান্নাতে সর্বপ্রথম প্রবেশের জন্য দরজায় কড়া নাড়বেন এবং কার সম্মানে তোরণ উন্মুক্ত হবে?", "Who will be the very first person to knock on the gates of Jannah, opening them for humanity?",
         ["বিশ্বনবী হযরত মুহাম্মদ ﷺ", "হযরত ইব্রাহিম (আ.)", "হযরত মূসা (আ.)", "হযরত আদম (আ.)"], ["Prophet Muhammad ﷺ", "Prophet Ibrahim (AS)", "Prophet Musa (AS)", "Prophet Adam (AS)"], 0,
         "প্রহরী ফেরেশতা রিদওয়ান বলবেন: আপনার জন্য দ্বার খোলার আদেশ ছিল, আপনার পূর্বে কারও জন্য খুলিনি।", "The Prophet ﷺ will knock and the gatekeeper will state: I was commanded to open only for you.", "সহীহ মুসলিম: ১৯৭", "Sahih Muslim: 197", "EASY"),

        ("উম্মতে মুহাম্মাদীর মধ্যে কত হাজার ব্যক্তি বিনা হিসাবে সরাসরি জান্নাতে প্রবেশ করবে?", "How many from the Ummah of Muhammad ﷺ will enter Jannah without reckoning?",
         ["৭০,০০০ ব্যক্তি (এবং প্রতি হাজারের সাথে আরও সত্তর হাজার)", "১০,০০০", "৫০,০০০", "১,০০,০০০"], ["70,000 (and with every 1,000 another seventy thousand by Allah's grace)", "10,000", "50,000", "100,000"], 0,
         "যারা ঝাড়ফুঁক বা কুসংস্কারে লিপ্ত হয়নি এবং একমাত্র রবের ওপর অবিচল তাওয়াক্কুল রেখেছে।", "Those who rely purely on Allah without superstition will enter without reckoning.", "সহীহ বুখারী: ৫৭০৫", "Sahih Bukhari: 5705", "EASY"),

        ("জান্নাতবাসীদের জন্য আল্লাহ তাআলার সর্বোচ্চ চিরন্তন সন্তুষ্টির সুসংবাদটি কী হবে?", "What will be Allah's eternal declaration of contentment to the people of Jannah?",
         ["'আজ আমি তোমাদের ওপর চিরস্থায়ী সন্তুষ্টি দান করলাম, এরপর কখনো তোমাদের ওপর অসন্তুষ্ট হবো না'", "তোমরা কাজ করো", "তোমরা পৃথিবীতে ফিরে যাও", "তোমাদের পরীক্ষা শেষ"], ["'I bestow My eternal Pleasure upon you, and I shall never be displeased with you thereafter'", "Go work", "Return to earth", "Test finished"], 0,
         "আল্লাহর চিরন্তন রেজামন্দি বা সন্তুষ্টি হলো জান্নাতের সবচেয়ে মহান ও অতুলনীয় পরমানন্দ।", "Allah will proclaim His eternal contentment never to be angry with them again.", "সহীহ বুখারী: ৬৫৪৯", "Sahih Bukhari: 6549", "EASY"),

        ("জান্নাতের ঘরগুলোর ইট ও সিমেন্টের গাঁথুনি কী দিয়ে তৈরি?", "What are the bricks and mortar of the palaces in Jannah made of?",
         ["সোনার ইট, রূপার ইট এবং খাঁটি মিশকের সুগন্ধি কাদা", "সাধারণ সিমেন্ট", "লোহা ও তামা", "কাঠ ও পাথর"], ["Bricks of gold and silver, and mortar of fragrant pure musk", "Cement", "Iron and copper", "Wood and stone"], 0,
         "জান্নাতের ভূমি জাফরানের এবং পাথরগুলো খাঁটি মুক্তা ও ইয়াকুতের।", "Bricks of gold and silver with mortar of fragrant musk and pebbles of pearl.", "জামে আত-তিরমিযী: ২৫২৬", "Jami at-Tirmidhi: 2526", "EASY"),

        ("জান্নাতে মুমিনের তাঁবু (খাইমাহ) কত উচ্চতা বিশিষ্ট বিশাল ফাঁপা একক মুক্তা দিয়ে নির্মিত?", "How high is the pavilion (tent) of a single hollow pearl in Jannah for a believer?",
         ["৬০ মাইল প্রশস্ত ও আকাশচুম্বী উঁচু", "১০ মাইল", "৫ মাইল", "১ মাইল"], ["60 miles wide and lofty in the heavens", "10 miles", "5 miles", "1 mile"], 0,
         "একটিমাত্র ফাঁপা মুক্তার তৈরি তাঁবু ৬০ মাইল বিস্তৃত যেখানে মুমিনের পরিবার শান্তিতে থাকবে।", "The tent is a single hollow pearl 60 miles wide in Paradise.", "সহীহ বুখারী: ৪৮৭৯", "Sahih Bukhari: 4879", "EASY"),

        ("জান্নাতবাসীদের প্রতিটি আনন্দের পর তাদের রূপ-লাবণ্য ও সৌন্দর্য কীভাবে বৃদ্ধি পাবে?", "How will the beauty and radiance of believers increase every Friday in Jannah?",
         ["প্রতি শুক্রবার বিশেষ বাজারে সমবেত হলে উত্তর দিকের মৃদুমন্দ বাতাসে সৌন্দর্য বহুগুণ বেড়ে যাবে", "একই থাকবে", "কমে যাবে", "পোশাক বদলাবে"], ["A northern breeze will blow upon them enhancing their beauty exponentially", "Remain same", "Decrease", "Change clothes"], 0,
         "শুক্রবার জান্নাতীরা বাড়ি ফিরলে তাদের পরিবারের সদস্যরা বলবে: আল্লাহর কসম! তোমাদের সৌন্দর্য শতগুণ বেড়ে গেছে।", "The market of Paradise and northern breeze will perpetually enhance their radiance.", "সহীহ মুসলিম: ২৮৩৩", "Sahih Muslim: 2833", "EASY"),

        ("দুনিয়াতে যে নারী একাধিক বিয়ে করেছিলেন, জান্নাতে তিনি কার সঙ্গী হবেন?", "In Jannah, which husband will a righteous woman accompany if she had multiple marriages?",
         ["দুনিয়াতে তাঁর শেষ স্বামীর অথবা যার চরিত্র সবচেয়ে উত্তম ছিল", "প্রথম স্বামীর", "কোনো স্বামী থাকবে না", "ইচ্ছাধীন নয়"], ["Her last husband in this life or the one with the best moral character", "First husband", "No husband", "Not chosen"], 0,
         "উম্মুল মুমিনীনগণের জন্য এবং মুমিন নারীদের জন্য শেষ স্বামী বা সর্বোত্তম চরিত্রের স্বামী নির্ধারিত হবে।", "A woman will accompany her last husband or the one of noblest conduct.", "সিলসিলাতুস সাহীহাহ: ১২৮১", "Silsilah as-Sahihah", "MEDIUM"),

        ("জান্নাতে মানুষের পারস্পরিক সালাম বিনিময় ও সম্ভাষণ কী হবে?", "What will be the greeting of the dwellers of Paradise to one another?",
         ["'সালাম' (শান্তি)", "নমস্কার", "হ্যালো", "গুড মর্নিং"], ["'Salam' (Peace)", "Namaste", "Hello", "Good morning"], 0,
         "কুরআনে বলা হয়েছে: 'তাহিয়্যাতুহুম ফীহা সালাম' (সেখানে তাদের সম্ভাষণ হবে সালাম)।", "Their greeting therein will be: Peace (Salam).", "সূরা ইউনুস: ১০", "Surah Yunus: 10", "EASY"),

        ("দুনিয়ার জীবনে যে মুমিন এতিমের লালন-পালনের দায়িত্ব গ্রহণ করে জান্নাতে রাসূল ﷺ-এর সাথে তার অবস্থান কেমন হবে?", "What is the status in Jannah of one who sponsors and cares for an orphan?",
         ["রাসূল ﷺ-এর পাশে তর্জনী ও মধ্যমা আঙুলের মতো অতি নিকটবর্তী", "বহু দূরে", "অন্য স্তরে", "দরজার কাছে"], ["Side by side with the Prophet ﷺ like the index and middle fingers", "Far away", "Different level", "Near gate"], 0,
         "রাসূল ﷺ দুই আঙুল মিলিয়ে বলেন: আমি ও এতিমের অভিভাবক জান্নাতে এভাবে পাশাপাশি থাকব।", "I and the sponsor of an orphan will be in Paradise like these two fingers.", "সহীহ বুখারী: ৫৩০৪", "Sahih Bukhari: 5304", "EASY"),

        ("জান্নাতে কোনো মৃত্যু, রোগ, শোক বা কোনো ধরনের অপ্রীতিকর দুঃখ-কষ্ট থাকবে কি?", "Will there be any death, sickness, grief, or fatigue in Jannah?",
         ["না, জান্নাত সম্পূর্ণ রোগহীন, শোকহীন ও চিরস্থায়ী পরম আনন্দের আবাস", "সামান্য থাকবে", "মাঝে মাঝে হবে", "ঘুমের সময় হবে"], ["No, Jannah is an eternal sanctuary of absolute perfection and joy", "Slight", "Occasional", "In sleep"], 0,
         "কিয়ামতের দিন মৃত্যুকে একটি মেষের আকৃতিতে এনে জবাই করে ঘোষণা করা হবে: 'হে জান্নাতবাসী! আর কোনো মৃত্যু নেই।' ", "Death will be slaughtered on Judgment Day, inaugurating eternal life.", "সহীহ বুখারী: ৪৭৩০", "Sahih Bukhari: 4730", "EASY"),

        ("পবিত্র কুরআনে বর্ণিত 'সিদরাতুল মুনতাহা' (সীমান্তবর্তী বরই গাছ) কোথায় অবস্থিত?", "Where is Sidrat al-Muntaha (The Lote Tree of the Utmost Boundary) located?",
         ["সপ্তম আসমানে জান্নাতুল মা'ওয়ার সন্নিকটে", "প্রথম আকাশে", "জমিনে", "কবরের ওপর"], ["In the Seventh Heaven near Jannat al-Ma'wa", "First heaven", "Earth", "Above grave"], 0,
         "সিদরাতুল মুনতাহা হলো সৃষ্টির জ্ঞানের সর্বশেষ সীমানা যেখানে মিরাজের রাতে রাসূল ﷺ গমন করেন।", "Sidrat al-Muntaha marks the boundary where revelation descends.", "সূরা আন-নাজম: ১৪-১৫", "Surah An-Najm: 14-15", "EASY"),

        ("জান্নাতের সর্বশেষ প্রবেশকারী ব্যক্তি যে দুনিয়ার সমান কতগুণ বিশাল রাজত্ব লাভ করবে?", "How large a kingdom will the very last person admitted into Jannah receive?",
         ["সমগ্র পৃথিবীর আয়তনের অন্তত ১০ গুণ বিশাল জান্নাত", "পৃথিবীর সমান", "২ গুণ", "৫ গুণ"], ["At least ten times the size of the entire worldly earth", "Equal to earth", "2 times", "5 times"], 0,
         "সর্বনিম্ন স্তরের জান্নাতী ব্যক্তিকেও আল্লাহ এই বিশাল নেয়ামত ও রাজত্ব দান করবেন।", "The lowest person in Paradise will be granted 10 times the size of this world.", "সহীহ বুখারী: ৬৫৭১", "Sahih Bukhari: 6571", "EASY"),

        ("জান্নাতে কোনো ঘুম থাকবে কি?", "Will people sleep in Jannah?",
         ["না, জান্নাতে কোনো ঘুম থাকবে না, কারণ ঘুম হলো মৃত্যুর ভাই", "হ্যাঁ, রাতে ঘুমাবে", "মাঝে মাঝে ঘুমাবে", "ইচ্ছাধীন"], ["No, sleep does not exist in Jannah as sleep is the brother of death", "Yes, at night", "Occasional", "Optional"], 0,
         "জান্নাতবাসীরা সার্বক্ষণিক পরম আনন্দে নিমগ্ন থাকবে, তাই ঘুমের কোনো প্রয়োজন বা ক্লান্তি থাকবে না।", "Sleep is the sister of death, and the people of Paradise will never sleep.", "সিলসিলাতুস সাহীহাহ: ১০৮৭", "Silsilah as-Sahihah", "MEDIUM"),

        ("জান্নাত লাভের জন্য বান্দার আমল ছাড়াও সর্বপ্রধান কোন্ বিষয়টি অপরিহার্য?", "What is the ultimate deciding factor in entering Jannah besides good deeds?",
         ["একমাত্র আল্লাহ রব্বুল আলামীনের অসীম রহমত ও দয়া", "পার্থিব ধনদৌলত", "শারীরিক শক্তি", "বংশের কৌলীন্য"], ["The boundless Mercy and Grace of Allah alone", "Wealth", "Physical power", "Lineage"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: কারও আমল তাকে জান্নাতে প্রবেশ করাতে পারবে না যতক্ষণ না আল্লাহর রহমত তাকে ঢেকে নেয়।", "None will enter Jannah purely through deeds without Allah's encompassing mercy.", "সহীহ বুখারী: ৫৬৭৩", "Sahih Bukhari: 5673", "EASY")
    ]
    return [('Q_JAN_' + str(i+1).zfill(3), 'jannah_paradise', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_10_jahannam():
    items = [
        ("জাহান্নামের মোট কতটি প্রধান দরজা বা স্তর রয়েছে যা কুরআনে ঘোষিত হয়েছে?", "How many main gates (levels) does Hellfire (Jahannam) have as declared in the Quran?",
         ["৭টি স্তর/দরজা", "৮টি দরজা", "১২টি দরজা", "৫টি দরজা"], ["7 Gates/Levels", "8 Gates", "12 Gates", "5 Gates"], 0,
         "সূরা আল-হিজরে বলা হয়েছে: 'জাহান্নামের সাতটি দরজা রয়েছে, প্রত্যেক দরজার জন্য নির্দিষ্ট দল রয়েছে।' ", "Hell has seven gates, for each gate is an assigned group (Surah Al-Hijr: 44).", "সূরা আল-হিজর: ৪৪", "Surah Al-Hijr: 44", "EASY"),

        ("জাহান্নামের প্রধান ও কঠোরতম পাহারাদার ফেরেশতার নাম কী?", "What is the name of the Chief Keeper and Guardian of Hellfire?",
         ["হযরত মালিক (আ.)", "হযরত রিদওয়ান (আ.)", "হযরত জিবরাইল (আ.)", "হযরত ইসরাফিল (আ.)"], ["Angel Malik (AS)", "Angel Ridwan", "Angel Jibreel", "Angel Israfil"], 0,
         "কুরআনে জাহান্নামীরা চিৎকার করে বলবে: 'হে মালিক! আপনার রব যেন আমাদের মৃত্যু দেন।' ", "The dwellers of Hell will call out: 'O Malik, let your Lord put an end to us!'", "সূরা আয-যুখরুফ: ৭৭", "Surah Az-Zukhruf: 77", "EASY"),

        ("জাহান্নামের কঠোর শাস্তি প্রয়োগকারী ১৯ জন শক্তিশালী ফেরেশতার দলকে কুরআনে কী বলা হয়েছে?", "What are the 19 stern guardian angels of Hellfire called in the Quran?",
         ["যাবানিয়াহ (Zabaniyah)", "মালাইকাতুর রহমত", "আসহাবুল মাশআমাহ", "হুর"], ["Zabaniyah", "Angels of Mercy", "Ashab al-Mash'amah", "Hoor"], 0,
         "সূরা আলাকে আল্লাহ বলেন: 'সানাদ্উয যাবানিয়াহ' (আমি অচিরেই যাবানিয়াহ ফেরেশতাদের ডাকব)।", "Over it are nineteen stern angels called the Zabaniyah (Surah Al-Muddathir: 30).", "সূরা আল-আলাক: ১৮", "Surah Al-Alaq: 18", "EASY"),

        ("জাহান্নামের তলদেশে উৎপন্ন হওয়া বিষাক্ত, কাঁটাযুক্ত ও দুর্গন্ধময় বৃক্ষের নাম কী যা জাহান্নামীদের খেতে দেওয়া হবে?", "What is the thorny, bitter tree growing at the bottom of Hell that dwellers are forced to eat?",
         ["যাক্কুম গাছ (Zaqqum)", "বাবুল গাছ", "শাল গাছ", "নিম গাছ"], ["Zaqqum Tree", "Acacia", "Sal", "Neem"], 0,
         "যাক্কুমের ফল দেখতে শয়তানের মাথার মতো ভয়ংকর এবং তা পেটে গলিত তামার মতো ফুটতে থাকবে।", "The Tree of Zaqqum boils in bellies like molten lead.", "সূরা আস-সাফফাত: ৬২-৬৬", "Surah As-Saffat: 62-66", "EASY"),

        ("জাহান্নামীদের পান করার জন্য যে ফুটন্ত পুঁজ ও রক্তের মিশ্রণ দেওয়া হবে তাকে কী বলে?", "What is the scalding purulent fluid of pus and blood forced upon dwellers of Hell called?",
         ["গিসলীন ও হামিম (Ghisleen & Hameem)", "সালসাবিল", "কাউসার", "তাসনীম"], ["Ghisleen & Hameem", "Salsabeel", "Kawthar", "Tasneem"], 0,
         "হামিম পান করার সাথে সাথে তা পেটের নাড়িভুঁড়ি ছিন্নভিন্ন করে গলিয়ে দেবে।", "Scalding water (Hameem) and pus (Ghisleen) will sever their intestines.", "সূরা আল-হাক্কাহ: ৩৬", "Surah Al-Haqqah: 36", "EASY"),

        ("দুনিয়ার সাধারণ আগুনের উত্তাপের তুলনায় জাহান্নামের আগুনের উত্তাপ কতগুণ বেশি তীব্র ও শক্তিশালী?", "How many times hotter is the Hellfire compared to the earthly fire of this world?",
         ["৭০ গুণ বেশি উত্তপ্ত ও কালো", "১০ গুণ", "২০ গুণ", "১০০ গুণ"], ["70 times hotter and pitch-black", "10 times", "20 times", "100 times"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তোমাদের পার্থিব আগুন জাহান্নামের আগুনের সত্তর ভাগের এক ভাগ মাত্র।", "Your worldly fire is one-seventieth part of the heat of Hellfire.", "সহীহ বুখারী: ৩২৬০", "Sahih Bukhari: 3260", "EASY"),

        ("জাহান্নামের সর্বনিম্ন ও সর্বাপেক্ষা কঠিন শাস্তির স্তরে কারা অবস্থান করবে?", "Who will occupy the absolute lowest, most excruciating depth of Hell (Asfal as-Safileen)?",
         ["মুনাফিকরা (কপট কপটচারী)", "সাধারণ পাপী", "মুসাফির", "কৃপণ"], ["The Hypocrites (Munafiqun)", "Ordinary sinners", "Travelers", "Miserly"], 0,
         "কুরআনে স্পষ্ট ঘোষণা: 'নিশ্চয়ই মুনাফিকরা জাহান্নামের সর্বনিম্ন স্তরে (দারকুল আসফাল) থাকবে।' ", "Indeed, the hypocrites will be in the lowest depths of the Fire (Surah An-Nisa: 145).", "সূরা আন-নিসা: ১৪৫", "Surah An-Nisa: 145", "EASY"),

        ("জাহান্নামের সবচেয়ে হালকা শাস্তিপ্রাপ্ত ব্যক্তি (আবু তালিব)-এর শাস্তির বিবরণ হাদিসে কী এসেছে?", "What is the lightest punishment in Hellfire described in authentic Hadith?",
         ["আগুনের দুটি জুতো পরানো হবে যার তাপে মাথার মগজ হাঁড়ির পানির মতো টগবগ করে ফুটবে", "হাতে আগুনের শিকল", "গায়ে আগুনের জামা", "মুখে আগুন"], ["Wearing sandals of fire causing the brain to boil like a cauldron", "Chains on hands", "Fiery shirt", "Fire on face"], 0,
         "সে মনে করবে সে-ই সবচেয়ে কঠিন শাস্তি পাচ্ছে, অথচ সেটিই হবে জাহান্নামের সবচেয়ে হালকা শাস্তি।", "The lightest punished person will wear shoes of fire causing his brain to boil.", "সহীহ বুখারী: ৬৫৬২", "Sahih Bukhari: 6562", "EASY"),

        ("জাহান্নামের আগুনের জ্বালানি হিসেবে কুরআনে কোন দুটি জিনিসের কথা উল্লেখ করা হয়েছে?", "What two things are explicitly mentioned in the Quran as fuel for the Hellfire?",
         ["মানুষ এবং পাথর (মূর্তি)", "কাঠ ও তেল", "কয়লা ও গ্যাস", "কাগজ ও প্লাস্টিক"], ["Humans and Stones (Idols)", "Wood and oil", "Coal and gas", "Paper & plastic"], 0,
         "সূরা আল-বাকারায় বলা হয়েছে: 'সেই আগুনকে ভয় করো যার জ্বালানি হবে মানুষ ও পাথর।' ", "Fear the Fire whose fuel is men and stones prepared for disbelievers.", "সূরা আল-বাক্বারাহ: ২৪", "Surah Al-Baqarah: 24", "EASY"),

        ("জাহান্নামের আগুনে পুড়ে যখন চামড়া নষ্ট হয়ে যাবে তখন আল্লাহ তাদের কী শাস্তি দেবেন?", "What will happen when the skin of dwellers of Hell is thoroughly burnt and destroyed?",
         ["নতুন চামড়া পরিয়ে দেওয়া হবে যাতে তারা অবিরাম শাস্তির স্বাদ আস্বাদন করতে পারে", "ছেড়ে দেওয়া হবে", "পানি দেওয়া হবে", "ঘুম পাড়ানো হবে"], ["Replaced with fresh skin continuously so they taste the punishment without end", "Released", "Given water", "Put to sleep"], 0,
         "আল্লাহ বলেন: যতবার তাদের চামড়া পুড়ে যাবে ততবার নতুন চামড়া সৃষ্টি করব যাতে শাস্তি ভোগ অব্যাহত থাকে।", "Every time their skins are roasted through, We replace them with other skins (Surah An-Nisa: 56).", "সূরা আন-নিসা: ৫৬", "Surah An-Nisa: 56", "EASY"),

        ("জাহান্নামের একটি অন্ধকারময় বরফশীতল চরম যন্ত্রণাদায়ক শাস্তির স্থানের নাম কী?", "What is the freezing, painfully cold sector of punishment in Hell called?",
         ["যামহারীর (Zamhareer)", "হাবিয়া", "লযা", "হুতামাহ"], ["Zamhareer", "Hawiyah", "Ladhaa", "Hutamah"], 0,
         "জাহান্নামে কেবল আগুনের উত্তাপই নয়, চরম হিমাঙ্কের বরফশীতল যন্ত্রণাদায়ক যামহারীরের শাস্তিও রয়েছে।", "Zamhareer is the agonizing extreme freezing punishment within Hell.", "সহীহ বুখারী: ৩২৬০", "Sahih Bukhari: 3260", "MEDIUM"),

        ("জাহান্নামীদের মৃত্যুর কোনো আশা থাকবে কি?", "Will there be any escape through death for dwellers of Hell?",
         ["না, মৃত্যু আসবে না; তারা মরবেও না আবার শান্তিতে বাঁচবেও না", "হ্যাঁ, ১ বছর পর মরবে", "১০০ বছর পর মরবে", "ইচ্ছাধীন"], ["No death; they will neither die nor live in comfort (Perpetual Torment)", "Die in 1 year", "Die in 100 years", "Optional"], 0,
         "কুরআনে বর্ণিত: 'লা ইয়ামূতু ফীহা ওয়ালা ইয়াহইয়া' (সেখানে সে মরবেও না, বাঁচবেও না)।", "Death will come from every side, yet they will not die (Surah Ibrahim: 17).", "সূরা ত্বা-হা: ৭৪", "Surah Ta-Ha: 74", "EASY"),

        ("জাহান্নামে অহংকারী দাম্ভিক ব্যক্তিদের হাশরের ময়দানে কোন আকৃতিতে পদদলিত করা হবে?", "In what humiliating form will arrogant tyrants be resurrected and trampled towards Hell?",
         ["পিপীলিকার মতো ক্ষুদ্র আকৃতিতে পায়ের নিচে পিষ্ট হবে", "হাতির মতো", "পাখির মতো", "বিশাল দানব"], ["As tiny ants trampled beneath the feet of all people", "Like elephants", "Like birds", "Monsters"], 0,
         "অহংকারীদের দুনিয়ার দম্ভের কারণে কিয়ামতের দিন মানুষের পায়ের নিচে পিপড়ার মতো লাঞ্ছিত করা হবে।", "The arrogant will be resurrected like small ants trampled underfoot.", "জামে আত-তিরমিযী: ২৪৯২", "Jami at-Tirmidhi: 2492", "EASY"),

        ("যে ব্যক্তি আল্লাহর সাথে প্রকাশ্য শিরক বা অংশীদার স্থাপন করে মৃত্যুবরণ করবে তার পরিণতি কী?", "What is the eternal fate of one who dies committing Shirk (associating partners with Allah)?",
         ["জাহান্নামে চিরস্থায়ী শাস্তি এবং তার জন্য জান্নাত চিরতরে হারাম", "কিছুদিন পর মুক্তি", "মাফ পাবে", "শাফাআত পাবে"], ["Eternal condemnation in Hellfire; Jannah is permanently forbidden to him", "Release later", "Pardoned", "Intercession"], 0,
         "আল্লাহ শিরকের গুনাহ কখনো ক্ষমা করেন না; এটি ক্ষমার অযোগ্য বৃহত্তম জুলুম।", "Indeed, whoever associates others with Allah, Allah has forbidden him Paradise.", "সূরা আল-মায়িদাহ: ৭২", "Surah Al-Ma'idah: 72", "EASY"),

        ("রাসূলুল্লাহ ﷺ দৈনিক কতবার বা কীভাবে জাহান্নামের আগুন থেকে আশ্রয় প্রার্থনা করার শিক্ষা দিয়েছেন?", "How frequently did Prophet Muhammad ﷺ teach us to seek refuge from Hellfire?",
         ["প্রতি সালাতের শেষ বৈঠকে এবং সকাল-সন্ধ্যায় 'আল্লাহুম্মা আজিরনি মিনান নার' ৩ বা ৭ বার পাঠ করে", "মাসে একবার", "বছরে একবার", "প্রয়োজন নেই"], ["In every prayer's Tashahhud and repeating 'Allahumma ajirni minan-naar' morning and evening", "Monthly", "Yearly", "Not needed"], 0,
         "যে ব্যক্তি দৈনিক সাতবার জাহান্নাম থেকে পানাহ চায়, জাহান্নাম নিজে আল্লাহর কাছে বলে: হে আল্লাহ! তাকে মুক্তি দিন।", "Whoever seeks refuge from Hell 7 times, the Fire says: O Allah, save him from me.", "জামে আত-তিরমিযী: ২৫৭২", "Jami at-Tirmidhi: 2572", "EASY"),

        ("জাহান্নামের গভীরতা কতটা সুবিশাল যা হাদিসে একটি পাথর ফেলার মাধ্যমে বর্ণনা করা হয়েছে?", "How immense is the depth of Hell described by the falling stone Hadith?",
         ["একটি বিশাল পাথর উপর থেকে ফেললে তলায় পৌঁছাতে টানা ৭০ বছর সময় লাগে", "১ দিন", "১ বছর", "১০ দিন"], ["A stone thrown from its rim takes 70 years of falling to hit the bottom", "1 day", "1 year", "10 days"], 0,
         "রাসূল ﷺ সাহাবীদের সাথে বসা অবস্থায় বিকট শব্দ শুনে জানান: ৭০ বছর পূর্বে নিক্ষিপ্ত পাথর এইমাত্র জাহান্নামের তলদেশে পৌঁছাল।", "A boulder thrown into Hell falls continuously for 70 years before reaching the base.", "সহীহ মুসলিম: ২৮৪৪", "Sahih Muslim: 2844", "EASY"),

        ("কুরআনে বর্ণিত 'ওয়াইল' (Wail) কী যা মাপে কম দেওয়া ও পরনিন্দাকারীদের জন্য নির্ধারিত?", "What is 'Wail' mentioned in the Quran for cheats and backbiters?",
         ["জাহান্নামের একটি ভয়াবহ গভীর উপত্যকা ও ধ্বংসের স্থান", "একটি নদী", "একটি তোরণ", "একটি পাহাড়"], ["A terrible deep valley and destruction in Hell", "A river", "A gate", "A hill"], 0,
         "সূরা মুতাফফিফীন ও সূরা হুমাযাহতে মাপে কম দেওয়া ও গীবতকারীদের জন্য 'ওয়াইল'-এর কঠিন সতর্কবার্তা এসেছে।", "Woe (Wail) to those who give less in measure and defame others.", "সূরা আল-মুতাফফিফীন: ১", "Surah Al-Mutaffifin: 1", "EASY"),

        ("জাহান্নামীদের পোশাক কিসের তৈরি হবে যা তাদের গায়ে চাপিয়ে দেওয়া হবে?", "What will the garments of the dwellers of Hell be made of?",
         ["গলিত আলকাতরা ও আগুনের পোশাক (ক্বাত্বিরান)", "রেশম", "সুতি কাপড়", "পশমি কাপড়"], ["Molten pitch/tar and garments of liquid fire (Qatiran)", "Silk", "Cotton", "Wool"], 0,
         "তাদের পোশাক হবে আলকাতরার এবং তাদের মুখমণ্ডল ঢেকে ফেলা হবে দাউদাউ আগুনে।", "Their garments of pitch (liquid tar) and their faces covered by fire.", "সূরা ইব্রাহিম: ৫০", "Surah Ibrahim: 50", "EASY"),

        ("কিয়ামতের দিন জাহান্নামকে কতজন শক্তিশালী ফেরেশতা কয়টি লাগাম টেনে উপস্থিত করবেন?", "How many angels will drag the tied reins of Hell on the Day of Judgment?",
         ["৭০,০০০ লাগাম, প্রতিটি লাগামে ৭০,০০০ ফেরেশতা (মোট ৪৯০ কোটি ফেরেশতা)", "১০ জন", "১০০ জন", "১,০০০ জন"], ["70,000 reins, pulled by 70,000 angels on each rein (4.9 Billion angels)", "10", "100", "1,000"], 0,
         "কিয়ামতের দিন জাহান্নামকে গর্জনরত অবস্থায় হাশরের ময়দানে টেনে আনা হবে।", "Hell will be brought forth dragged with 70,000 reins by 4.9 billion angels.", "সহীহ মুসলিম: ২৮৪২", "Sahih Muslim: 2842", "MEDIUM"),

        ("জাহান্নামের কোন স্তরের নাম 'হুতামাহ' (যা চূর্ণ-বিচূর্ণকারী আগুন)?", "Which level of Hell is termed Al-Hutamah (The Crusher)?",
         ["যে আগুন সরাসরি হৃদয়কে স্পর্শ করে পুড়িয়ে ফেলে", "বরফের জায়গা", "পানির জায়গা", "বাতাসের জায়গা"], ["The crushing fire of Allah that mounts up over the hearts", "Ice sector", "Water sector", "Wind sector"], 0,
         "সূরা হুমাযাহতে বলা হয়েছে: 'নারুল্লাহিল মুকাদাহ, আল্লাতি তাত্তালিউ আলাল আফইদাহ' (আল্লাহর প্রজ্জ্বলিত আগুন যা হৃদয় পর্যন্ত পৌঁছায়)।", "It is the fire of Allah fueled, which mounts directed at the hearts.", "সূরা আল-হুমাযাহ: ৬-৭", "Surah Al-Humazah: 6-7", "EASY"),

        ("জাহান্নামের রক্ষীরা যখন তাদের তিরস্কার করে জিজ্ঞেস করবে: তোমাদের কাছে কি কোনো সতর্ককারী নবী আসেনি? তখন তারা কী বলবে?", "When keepers of Hell ask: Did there not come to you a warner? What will they reply?",
         ["'হ্যাঁ, সতর্ককারী এসেছিল কিন্তু আমরা মিথ্যা বলেছিলাম এবং জ্ঞান খাটাইনি'", "'না, কেউ আসেনি'", "'আমরা জানতাম না'", "'আমাদের ভুল হয়েছিল'"], ["'Yes, a warner came to us, but we denied and used no intellect'", "'No one came'", "'We didn't know'", "'We forgot'"], 0,
         "তারা আক্ষেপ করে বলবে: হায়! আমরা যদি শুনতাম বা বুদ্ধি খাটাতাম তবে আজ জাহান্নামী হতাম না।", "They will say: If only we had listened or reasoned, we would not be among the companions of the Blaze.", "সূরা আল-মুলক: ৯-১০", "Surah Al-Mulk: 9-10", "EASY"),

        ("দুনিয়াতে অহংকারবশত নিজের কাপড় টাখনুর নিচে ঝুলিয়ে পরা ব্যক্তিদের জন্য হাদিসে কী শাস্তি ঘোষিত হয়েছে?", "What warning is issued for men who drag their lower garments below ankles out of arrogance?",
         ["টাখনুর নিচের অংশ জাহান্নামের আগুনে পুড়বে", "মাফ পাবে", "সওয়াব পাবে", "কিছু হবে না"], ["The part below the ankles is condemned to the Fire", "Pardoned", "Rewarded", "Nothing"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: পুরুষের লুঙ্গি বা কাপড়ের যে অংশ টাখনুর নিচে যাবে তা জাহান্নামে যাবে।", "Whatever of the lower garment is below the ankles is in the Fire.", "সহীহ বুখারী: ৫৭৮৭", "Sahih Bukhari: 5787", "EASY"),

        ("জাহান্নাম থেকে মুক্তির জন্য অত্যন্ত শক্তিশালী দোয়া কোনটি যা নবীজি ﷺ শিখিয়েছেন?", "Which powerful supplication seeking protection from the torment of Hell did the Prophet ﷺ teach?",
         ["'রাব্বানা আসরিফ আন্না আযাবা জাহান্নাম, ইন্না আযাবাহা কানা গারামা'", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "লা ইলাহা ইল্লাল্লাহ"], ["'Rabbana-srif 'anna 'adhaba jahannama inna 'adhabaha kana gharama'", "Subhanallah", "Alhamdulillah", "La ilaha"], 0,
         "মুমিনের গুণ হলো তারা সর্বদা প্রার্থনা করে: হে আমাদের রব! আমাদের থেকে জাহান্নামের আযাব হটিয়ে দিন।", "Our Lord, avert from us the punishment of Hell; indeed, its punishment is unending affliction.", "সূরা আল-ফুরকান: ৬৫", "Surah Al-Furqan: 65", "EASY"),

        ("জাহান্নামে নারীদের সংখ্যাধিক্য দেখার কারণ হিসেবে রাসূল ﷺ নারীদের কোন্ প্রধান দুটি অভ্যাসের কথা উল্লেখ করেছেন?", "Which two traits did the Prophet ﷺ counsel women about regarding salvation from Hell?",
         ["স্বামীর অকৃতজ্ঞতা প্রকাশ এবং কথায় কথায় অভিশাপ দেওয়া", "সালাত পড়া", "রোজা রাখা", "দান করা"], ["Ungratefulness to spouses and frequent cursing/complaining", "Praying", "Fasting", "Charity"], 0,
         "রাসূল ﷺ নারীদের বেশি বেশি দান-সদকা ও ইসতিগফার করার উপদেশ দিয়েছিলেন।", "The Prophet ﷺ urged women to give Sadaqah and avoid ingratitude.", "সহীহ বুখারী: ৩০৪", "Sahih Bukhari: 304", "EASY"),

        ("দুনিয়াতে জুলুমকারী ও অত্যাচারী শাসকদের জন্য পরকালে জাহান্নামে কী অপেক্ষা করছে?", "What awaits oppressive tyrants and unjust oppressors in Hellfire?",
         ["চরম লাঞ্ছনা ও চতুর্দিক থেকে অন্ধকারময় কঠিন আজাব", "সম্মান", "মুক্তি", "ক্ষমা"], ["Utter humiliation and severe encompassing darkness and agony", "Honor", "Pardon", "Forgiveness"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: নিশ্চয়ই জুলুম কিয়ামতের দিন চরম অন্ধকারের রূপ ধারণ করবে।", "Oppression will be absolute darkness on the Day of Judgment.", "সহীহ বুখারী: ২৪৪৭", "Sahih Bukhari: 2447", "EASY"),

        ("কিয়ামতের দিন জাহান্নামের ওপর স্থাপিত সবচেয়ে বিপজ্জনক সেতুটির নাম কী যার ওপর দিয়ে সবাইকে পার হতে হবে?", "What is the razor-sharp bridge spanning over the abyss of Hell that all must cross?",
         ["পুলসিরাত (As-Sirat)", "মীযান", "কাউসার", "আ'রাফ"], ["Pul Sirat (The Bridge)", "Mizan", "Kawthar", "A'raf"], 0,
         "পুলসিরাত চুলের চেয়েও সূক্ষ্ম ও তরবারির চেয়েও ধারালো; আমল অনুযায়ী মুমিনগণ বিদ্যুৎ গতিতে পার হবেন।", "The Sirat bridge is sharper than a sword and thinner than a hair over Hell.", "সহীহ মুসলিম: ১৯৫", "Sahih Muslim: 195", "EASY"),

        ("পুলসিরাতের দুই পাশে ঝুলন্ত লোহার বিশেষ আঁকড়া (ক্বালাতীব)-এর কাজ কী?", "What is the function of the iron hooks (Kalateeb) along the sides of the Sirat bridge?",
         ["আল্লাহর আদেশে পাপিষ্ঠদের খামচে টেনে জাহান্নামে নিক্ষেপ করা", "সাহায্য করা", "আলো দেওয়া", "পানি দেওয়া"], ["Snatching and dragging condemned transgressors down into the depths of Hell", "Helping", "Lighting", "Watering"], 0,
         "হুকগুলো নির্দেশমতো নির্দিষ্ট অপরাধীদের আঁকড়ে ধরে অতল জাহান্নামে ফেলে দেবে।", "Hooks like thorny brambles will snatch people according to their evil deeds.", "সহীহ বুখারী: ৬৫৭৩", "Sahih Bukhari: 6573", "EASY"),

        ("জাহান্নাম থেকে সর্বশেষ যে ব্যক্তিকে বের করে আনা হবে তার অন্তরে কতটুকু ঈমান থাকবে?", "How much faith will the very last person liberated from Hell have in his heart?",
         ["একটি সরিষার দানা পরিমাণ বা অনু পরিমাণ খাঁটি ঈমান", "পাহাড় পরিমাণ", "সম্পূর্ণ ঈমান", "কোনো ঈমান নেই"], ["An atom's weight (mustard seed) of authentic Tawheed/Faith", "Mountain", "Complete", "No faith"], 0,
         "আল্লাহ তাআলা বলবেন: যার অন্তরে একটি সরিষার দানা পরিমাণও ঈমান আছে তাকে জাহান্নাম থেকে বের করো।", "Whoever had even an atom's weight of faith will eventually be taken out of Hell.", "সহীহ বুখারী: ৪৪", "Sahih Bukhari: 44", "EASY"),

        ("জাহান্নামে যারা যাবে তারা দুনিয়ার অর্থবিত্ত বা সন্তানসন্ততি মুক্তিপণ হিসেবে দিয়ে বাঁচতে পারবে কি?", "Can anyone in Hell ransom themselves with the earth's gold or their children?",
         ["না, সমগ্র পৃথিবীর সমান স্বর্ণ দিলেও বিন্দুমাত্র মুক্তিপণ গ্রহণ করা হবে না", "হ্যাঁ, টাকা দিলে হবে", "সন্তান দিলে হবে", "ইচ্ছাধীন"], ["No, even a whole earth full of gold would never be accepted as ransom", "Yes with money", "Yes with sons", "Optional"], 0,
         "কুরআনে স্পষ্ট ঘোষণা: যদি তাদের কাছে জমিনের সবকিছু ও তার সাথে আরও অনুরূপ থাকত, তবুও তা গ্রহণ করা হতো না।", "Even if they had all that is on earth and twice as much to ransom, it would not be accepted.", "সূরা আল-মায়িদাহ: ৩৬", "Surah Al-Ma'idah: 36", "EASY"),

        ("জাহান্নামীদের শরীরের আয়তন কেমন বিশাল করে দেওয়া হবে যাতে তারা বেশি শাস্তি অনুভব করে?", "How vast will the physical size of disbelievers be made in Hell to feel intense punishment?",
         ["একটি দাঁত ওহুদ পাহাড়ের সমান এবং কাঁধের দূরত্ব তিন দিনের সফরের সমান হবে", "স্বাভাবিক থাকবে", "ছোট হবে", "এক হাত"], ["A single molar tooth will be like Mount Uhud and shoulders days of travel apart", "Normal", "Tiny", "One cubit"], 0,
         "শাস্তির তীব্র অনুভূতি বৃদ্ধির জন্য তাদের শরীর ও চামড়ার ঘনত্ব অস্বাভাবিকভাবে বৃদ্ধি করা হবে।", "Their physical bodies will be enlarged immensely to endure maximum torment.", "সহীহ মুসলিম: ২৮৫১", "Sahih Muslim: 2851", "MEDIUM"),

        ("দুনিয়াতে যারা মানুষের ওপর অন্যায়ভাবে চাবুক বা লাঠি চালিয়ে জুলুম করত তাদের পরিণতি জাহান্নামে কী?", "What did the Prophet ﷺ warn regarding oppressors who beat innocent people unjustly?",
         ["তারা জাহান্নামের অধিবাসী হবে যাদের হাতে গরুর লেজের মতো চাবুক থাকবে", "মাফ পাবে", "শাসক হবে", "সুখী হবে"], ["They will be in Hell carrying whips like the tails of oxen striking people", "Pardoned", "Rulers", "Happy"], 0,
         "রাসূল ﷺ এমন জালেম পুলিশ ও অত্যাচারী শাসকদের জাহান্নামী বলে সতর্ক করেছেন।", "Oppressive strikers of innocent people will enter Hell with their instruments of torture.", "সহীহ মুসলিম: ২১২৮", "Sahih Muslim: 2128", "MEDIUM"),

        ("জাহান্নামীদের পারস্পরিক কথোপকথনে তারা তাদের অনুসারী ও নেতাদের কী করবে?", "What will occur between followers and misguided leaders in Hellfire?",
         ["তারা একে অপরকে চরম অভিশাপ দেবে এবং নেতাদের দ্বিগুণ শাস্তির দাবি জানাবে", "বন্ধুত্ব করবে", "সহানুভূতি জানাবে", "একসাথে থাকবে"], ["They will bitterly curse each other and demand double punishment for misleading chiefs", "Befriend", "Sympathize", "Stay together"], 0,
         "অনুসারীরা বলবে: হে আমাদের রব! এরাই আমাদের পথভ্রষ্ট করেছিল, তাই এদের দ্বিগুণ আগুনের শাস্তি দিন।", "They will curse one another saying: Our Lord, give them double the punishment.", "সূরা আল-আ'রাফ: ৩৮", "Surah Al-A'raf: 38", "EASY"),

        ("জাহান্নামের আগুনের গায়ে আল্লাহ তাআলা কি কোনো অনুভূতি বা ক্রোধ দিয়েছেন?", "Does Hellfire possess consciousness and rage against the disbelievers?",
         ["হ্যাঁ, জাহান্নাম ক্রোধে ফেটে পড়ার উপক্রম হয় এবং আরও পাপিষ্ঠদের জন্য গর্জন করে", "না, কোনো অনুভূতি নেই", "সাধারণ কাঠ", "ঠান্ডা"], ["Yes, it almost bursts with fury and roars for more transgressors", "No feelings", "Normal wood", "Cold"], 0,
         "কিয়ামতের দিন আল্লাহ জিজ্ঞেস করবেন: 'তুমি কি পূর্ণ হয়েছ?' জাহান্নাম বলবে: 'আরও অতিরিক্ত কেউ আছে কি?' ", "On that Day We will say to Hell, 'Are you full?' and it will say, 'Are there any more?'", "সূরা কাফ: ৩০", "Surah Qaf: 30", "EASY"),

        ("আত্মহত্যাকারীর জাহান্নামের শাস্তি সম্পর্কে রাসূলুল্লাহ ﷺ-এর বিধান কী?", "What is the punishment in Hell for a person who commits suicide?",
         ["যে বস্তুটি দিয়ে সে আত্মহত্যা করেছিল অনন্তকাল জাহান্নামে সে নিজেকে সেই বস্তু দিয়েই আঘাত করতে থাকবে", "মাফ পাবে", "স্বাভাবিক থাকবে", "জাহান্নামে যাবে না"], ["Perpetually tormenting himself with the exact weapon/method used to end his life", "Pardoned", "Normal", "Won't go to Hell"], 0,
         "বিষপায়ী বিষ পান করতে থাকবে, পাহাড় থেকে লাফ দেওয়া ব্যক্তি অবিরাম আছড়ে পড়তে থাকবে।", "Whoever kills himself with an instrument will be tortured with it in Hell perpetually.", "সহীহ বুখারী: ৫৭৭৮", "Sahih Bukhari: 5778", "EASY"),

        ("জাহান্নামের আগুন থেকে বাঁচার জন্য সর্বনিম্ন দান-সদকার গুরুত্ব বোঝাতে রাসূল ﷺ কী বলেছেন?", "What did the Prophet ﷺ urge to shield oneself from the Fire, even with minimal charity?",
         ["'একটি খেজুরের টুকরো (অর্ধেক) দান করে হলেও তোমরা জাহান্নামের আগুন থেকে আত্মরক্ষা করো'", "হাজার টাকা", "সোনার ঘর", "উটের কাফেলা"], ["'Protect yourselves from the Fire, even with half a date given in charity'", "1,000 taka", "Gold house", "Camel caravan"], 0,
         "সামান্যতম নেক আমল ও দানও নিষ্ঠার সাথে করা হলে জাহান্নামের আগুন থেকে মুক্তির কারণ হতে পারে।", "Shield yourselves from the Fire even by giving half a date in charity.", "সহীহ বুখারী: ১৪১৭", "Sahih Bukhari: 1417", "EASY")
    ]
    return [('Q_JHN_' + str(i+1).zfill(3), 'jahannam_hell', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_11_jinn():
    items = [
        ("আল্লাহ তাআলা পবিত্র কুরআনুল কারীমে জিন জাতিকে কী উপাদান দিয়ে সৃষ্টি করার কথা ঘোষণা করেছেন?", "From what substance did Allah create the race of Jinn as declared in the Quran?",
         ["ধোঁয়াহীন প্রখর অগ্নির শিখা (মারিজিন মিন নার)", "কাদামাটি", "নূর (আলো)", "পানি"], ["Smokeless scorching flame of fire (Marij min Nar)", "Clay", "Noor (Light)", "Water"], 0,
         "সূরা আর-রহমানের ১৫ নম্বর আয়াতে আল্লাহ বলেন: 'তিনি জিন জাতিকে ধোঁয়াহীন নির্ভেজাল আগুন থেকে সৃষ্টি করেছেন।' ", "And He created the Jinn from a smokeless flame of fire (Surah Ar-Rahman: 15).", "সূরা আর-রহমান: ১৫", "Surah Ar-Rahman: 15", "EASY"),

        ("ফেরেশতাদের আল্লাহ তাআলা কোন্ উপাদান দিয়ে সৃষ্টি করেছেন যা হাদিসে বর্ণিত হয়েছে?", "From what substance were the noble Angels (Mala'ikah) created according to Hadith?",
         ["পবিত্র নূর (আলো) থেকে", "আগুন থেকে", "মাটি থেকে", "বাতাস থেকে"], ["Pure Noor (Divine Light)", "Fire", "Clay", "Wind"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: ফেরেশতাদের সৃষ্টি করা হয়েছে নূর থেকে, জিনকে আগুন থেকে এবং মানুষকে মাটি থেকে।", "Angels were created from light, Jinn from smokeless fire, and Adam from clay.", "সহীহ মুসলিম: ২৯৯৬", "Sahih Muslim: 2996", "EASY"),

        ("ইবলিস (শয়তান) মূলত কোন্ জাতির অন্তর্ভুক্ত ছিল যা সূরা আল-কাহফে স্পষ্টভাবে ঘোষিত হয়েছে?", "To which creation did Iblees (Satan) originally belong as clarified in Surah Al-Kahf?",
         ["জিন জাতির অন্তর্ভুক্ত (কানা মিনাল জিন্নি)", "ফেরেশতা ছিল", "মানুষ ছিল", "প্রাণী ছিল"], ["He was from the Jinn (Kana minal Jinni)", "An angel", "A human", "An animal"], 0,
         "সূরা কাহফে আল্লাহ বলেন: 'সে ছিল জিনদের একজন, অতঃপর সে তার রবের নির্দেশের অবাধ্যতা করল।' ", "He was of the Jinn and departed from the command of his Lord (Surah Al-Kahf: 50).", "সূরা আল-কাহফ: ৫০", "Surah Al-Kahf: 50", "EASY"),

        ("চারজন প্রধান শীর্ষস্থানীয় ফেরেশতার মধ্যে আল্লাহর ওহী নবী-রাসূলগণের কাছে পৌঁছানোর দায়িত্বে কে ছিলেন?", "Among the 4 archangels, who was entrusted with delivering divine Revelation to Prophets?",
         ["হযরত জিবরাইল (আ.) (রুহুল কুদুস)", "হযরত মিকাইল (আ.)", "হযরত ইসরাফিল (আ.)", "হযরত আজরাইল (আ.)"], ["Angel Jibreel (Gabriel) (AS)", "Angel Mikail", "Angel Israfil", "Angel Azrael"], 0,
         "জিবরাইল (আ.) হলেন ফেরেশতাদের সর্দার এবং ওহীর বার্তা বহনকারী মহাসম্মানিত দূত।", "Jibreel (AS) is the Trustee of Revelation (Rooh al-Qudus).", "সূরা আশ-শু'আরা: ১৯৩", "Surah Ash-Shu'ara: 193", "EASY"),

        ("বৃষ্টিবর্ষণ, মেঘমালা পরিচালনা এবং জীবিকার বণ্টন ব্যবস্থাপনায় নিয়োজিত সম্মানিত প্রধান ফেরেশতা কে?", "Which archangel is in charge of rainfall, vegetation, and provisions by Allah's decree?",
         ["হযরত মিকাইল (আ.)", "হযরত জিবরাইল (আ.)", "হযরত ইসরাফিল (আ.)", "হযরত মালিক (আ.)"], ["Angel Mikail (Michael) (AS)", "Angel Jibreel", "Angel Israfil", "Angel Malik"], 0,
         "মিকাইল (আ.) মেঘমালা চালনা ও জীবজগতের খাদ্য-জীবিকা সরবরাহের দায়িত্বে নিয়োজিত।", "Angel Mikail is responsible for clouds, rain, and sustaining provisions.", "তাফসীরে তাবারী", "Tafsir al-Tabari", "EASY"),

        ("কিয়ামতের দিন আল্লাহর আদেশে শিঙ্গায় (তূরী) প্রথম ও দ্বিতীয় ফুৎকার দেবেন কোন্ সম্মানিত ফেরেশতা?", "Which archangel will blow the Trumpet (Soor) announcing the Day of Judgment and Resurrection?",
         ["হযরত ইসরাফিল (আ.)", "হযরত জিবরাইল (আ.)", "হযরত মিকাইল (আ.)", "হযরত আজরাইল (আ.)"], ["Angel Israfil (AS)", "Angel Jibreel", "Angel Mikail", "Angel Azrael"], 0,
         "ইসরাফিল (আ.) আল্লাহর নির্দেশমাত্র শিঙ্গায় ফুঁক দেওয়ার জন্য প্রস্তুত হয়ে তাকিয়ে আছেন।", "Angel Israfil stands ready with the Trumpet to execute Allah's command.", "সূরা আয-যুমার: ৬৮", "Surah Az-Zumar: 68", "EASY"),

        ("প্রাণী ও মানুষের রুহ বা আত্মা কবজ করার দায়িত্বে নিয়োজিত প্রধান ফেরেশতাকে কুরআনে কী নামে ডাকা হয়েছে?", "What is the Angel of Death in charge of taking souls called in the Quran?",
         ["মালাকুল মাউত (Malak al-Mawt)", "মালিক", "রিদওয়ান", "রাকীব"], ["Malak al-Mawt (Angel of Death)", "Malik", "Ridwan", "Raqeeb"], 0,
         "সূরা আস-সাজদাহে আল্লাহ বলেন: 'বলুন, তোমাদের প্রাণ হরণ করবেন মালাকুল মাউত যিনি তোমাদের জন্য নিযুক্ত।' ", "Say: The Angel of Death entrusted with you will take your soul (Surah As-Sajdah: 11).", "সূরা আস-সাজদাহ: ১১", "Surah As-Sajdah: 11", "EASY"),

        ("প্রত্যেক মানুষের কাঁধে সর্বদা ভালো ও মন্দ আমল নিখুঁতভাবে লিপিবদ্ধকারী দুই সম্মানিত ফেরেশতাকে কী বলে?", "What are the two recording angels on every person's shoulders called in the Quran?",
         ["কিরামান কাতিবীন (রাকীব ও আতিদ)", "মুনকার ও নাকীর", "হারুত ও মারুত", "যাবানিয়াহ"], ["Kiraman Katibeen (Raqeeb & Ateed)", "Munkar & Nakeer", "Harut & Marut", "Zabaniyah"], 0,
         "ডান কাঁধের ফেরেশতা নেক আমল এবং বাম কাঁধের ফেরেশতা বদ আমল লিপিবদ্ধ করেন।", "Noble recorders (Kiraman Katibeen) write every word uttered (Surah Qaf: 17-18).", "সূরা ক্বাফ: ১৭-১৮", "Surah Qaf: 17-18", "EASY"),

        ("কবরে মৃত ব্যক্তিকে তিনটি মৌলিক প্রশ্ন (রব, দ্বীন ও নবী) করার জন্য আগত দুই ফেরেশতার নাম কী?", "What are the two questioning angels who examine the deceased in the grave called?",
         ["মুনকার ও নাকীর", "রাকীব ও আতিদ", "হারুত ও মারুত", "মালিক ও রিদওয়ান"], ["Munkar and Nakeer", "Raqeeb & Ateed", "Harut & Marut", "Malik & Ridwan"], 0,
         "কালো-নীল চক্ষুবিশিষ্ট মুনকার ও নাকীর কবরে এসে বান্দার ঈমান ও আকীদার পরীক্ষা নেবেন।", "Munkar and Nakeer question every soul in the grave regarding his Lord, Deen, and Prophet.", "জামে আত-তিরমিযী: ১০৭১", "Jami at-Tirmidhi: 1071", "EASY"),

        ("পবিত্র কুরআনে একটি পূর্ণাঙ্গ সূরার নাম জিন জাতির নামে রাখা হয়েছে, সেই সূরার নাম কী?", "Which complete Surah in the Holy Quran is named after the Jinn?",
         ["সূরা আল-জিন (Surah Al-Jinn)", "সূরা আল-ইনসান", "সূরা আন-নাস", "সূরা আল-ফালাক"], ["Surah Al-Jinn", "Surah Al-Insan", "Surah An-Nas", "Surah Al-Falaq"], 0,
         "সূরা জিনে জিনদের কুরআন শ্রবণ, মুগ্ধ হয়ে ঈমান আনয়ন এবং তাওহীদের সাক্ষ্যের ঘটনা বর্ণিত হয়েছে।", "Surah Al-Jinn recounts how Jinn listened to the Quran and embraced true faith.", "সূরা আল-জিন", "Surah Al-Jinn", "EASY"),

        ("জিনদের মধ্যে কি মানুষের মতো মুসলিম মুমিন ও কাফের অবাধ্য দল রয়েছে?", "Do Jinn possess free will with believers (Muslims) and disbelievers among them like humans?",
         ["হ্যাঁ, জিনদের মাঝেও সৎকর্মশীল মুমিন এবং কাফের-অবাধ্য দল রয়েছে", "না, সবাই কাফের", "না, সবাই মুসলিম", "তারা পশু"], ["Yes, there are righteous Muslims and disbelieving rebels among them", "All are disbelievers", "All are Muslims", "They are beasts"], 0,
         "সূরা জিনে জিনেরা বলে: 'আমাদের মধ্যে কেউ কেউ সৎকর্মশীল আর কেউ কেউ অন্যরকম; আমরা বিভিন্ন পথে বিভক্ত।' ", "And among us are the righteous, and among us are otherwise; we are divided ways (Surah Al-Jinn: 11).", "সূরা আল-জিন: ১১", "Surah Al-Jinn: 11", "EASY"),

        ("জিন জাতি মানুষের চেয়ে কতকাল পূর্বে পৃথিবীতে সৃষ্টি হয়েছিল?", "Were Jinn created on earth before the creation of mankind (Adam)?",
         ["হ্যাঁ, মানুষের সৃষ্টির পূর্বেই জিন জাতিকে পৃথিবীতে সৃষ্টি করা হয়েছিল", "মানুষের পরে", "একই সাথে", "ফেরেশতাদের আগে"], ["Yes, Jinn were created and inhabited earth long before Adam (AS)", "After humans", "Simultaneously", "Before angels"], 0,
         "সূরা হিজরে আল্লাহ বলেন: 'আর জিন জাতিকে আমি ইতিপূর্বে অতি প্রখর অগ্নিশিখা থেকে সৃষ্টি করেছি।' ", "And the Jinn We created before from scorching fire (Surah Al-Hijr: 27).", "সূরা আল-হিজর: ২৭", "Surah Al-Hijr: 27", "EASY"),

        ("আল্লাহ তাআলা কোন নবীকে জিন জাতির ওপর পূর্ণ নিয়ন্ত্রণ ও তাদের দিয়ে স্থাপত্য ও ডুবুরির কাজ করানোর মুজিযা দেন?", "Which Prophet was granted absolute control over armies of Jinn by Allah?",
         ["হযরত সুলাইমান (আ.)", "হযরত দাউদ (আ.)", "হযরত মূসা (আ.)", "হযরত ইব্রাহিম (আ.)"], ["Prophet Sulaiman (Solomon) (AS)", "Prophet Dawud (AS)", "Prophet Musa (AS)", "Prophet Ibrahim (AS)"], 0,
         "সুলাইমান (আ.)-এর আদেশে জিনেরা বাইতুল মুকাদ্দাস নির্মাণ, দুর্গ ও গভীর সমুদ্র থেকে মুক্তা আহরণের কাজ করত।", "Jinn worked under Sulaiman (AS) constructing sanctuaries and diving for pearls.", "সূরা সাবা: ১২-১৩", "Surah Saba: 12-13", "EASY"),

        ("হযরত সুলাইমান (আ.)-এর ওফাত যে ঘটেছিল তা জিনেরা কতদিন বুঝতে পারেনি যতক্ষণ না লাঠিটি কীসে খেয়েছিল?", "How did the Jinn realize Prophet Sulaiman (AS) had passed away while leaning on his staff?",
         ["ঘুণপোকা (উইপোকা) তাঁর কাঠের লাঠি খেয়ে ফেললে তিনি মাটিতে পড়ে যান", "পাখি ডাক দিলে", "বায়ু থেমে গেলে", "ফেরেশতা বললে"], ["A woodworm ate his leaning staff, causing him to fall to the ground", "Bird cried", "Wind stopped", "Angel spoke"], 0,
         "কুরআনে বর্ণিত এই ঘটনা প্রমাণ করে যে জিনেরা অদৃশ্যের খবর (গায়েব) কিছুই জানে না।", "This proved that Jinn have zero knowledge of the unseen (Ghayb).", "সূরা সাবা: ১৪", "Surah Saba: 14", "EASY"),

        ("জিন বা শয়তানের কি মানুষের ভবিষ্যৎ বা গায়েবের গোপন রহস্য জানার কোনো ক্ষমতা আছে?", "Do Jinn or devils possess any knowledge of the unseen (Ghayb) or the future?",
         ["না, গায়েবের জ্ঞান একমাত্র আল্লাহ ছাড়া আকাশ বা জমিনের কারও নেই", "হ্যাঁ, সম্পূর্ণ জানে", "অর্ধেক জানে", "জ্যোতিষীরা জানে"], ["No, absolute knowledge of the Unseen belongs exclusively to Allah", "Yes completely", "Half", "Astrologers know"], 0,
         "আল্লাহ স্পষ্ট ঘোষণা করেন: 'বলুন, আসমান ও জমিনে আল্লাহ ব্যতীত কেউই অদৃশ্যের জ্ঞান রাখে না।' ", "None in the heavens and earth knows the unseen except Allah (Surah An-Naml: 65).", "সূরা আন-নামল: ৬৫", "Surah An-Naml: 65", "EASY"),

        ("জিন ও শয়তানের আক্রমণ, কুদৃষ্টি ও অনিষ্টতা থেকে বাঁচার জন্য সবচেয়ে শক্তিশালী আমল কোনটি?", "Which authentic practice provides the strongest divine protection against Jinn and Satan?",
         ["আয়াতুল কুরসী, সূরা বাকারা ও শেষ তিন কুল (ইখলাস, ফালাক, নাস) তিলাওয়াত", "তাবিজ ঝুলানো", "গাছে সুতা বাঁধা", "ভয়ে ঘরে থাকা"], ["Reciting Ayat al-Kursi, Surah Al-Baqarah, and the Mu'awwidhatayn", "Wearing amulets", "Tying threads", "Staying indoors"], 0,
         "রাসূল ﷺ বলেছেন: যে ঘরে সূরা বাকারা তিলাওয়াত করা হয় সেখান থেকে শয়তান পলায়ন করে।", "Satan flees from the house in which Surah Al-Baqarah is recited.", "সহীহ মুসলিম: ৭৮০", "Sahih Muslim: 780", "EASY"),

        ("শয়তান ও দুষ্ট জিনদের প্রধান খাদ্য কী যা অপবিত্র অবস্থায় গ্রহণ করা হয়?", "What is the primary food source of Jinn as specified in Hadith?",
         ["হাড়গোড় এবং গোবরের অবশিষ্টাংশ যার ওপর বিসমিল্লাহ বলা হয়নি", "ভাত ও রুটি", "ফলমূল", "মাংস"], ["Bones upon which Allah's name was recited and animal dung", "Rice & bread", "Fruits", "Meat"], 0,
         "নবীজি ﷺ ইস্তেনজার সময় হাড় ও গোবর দিয়ে শৌচ করতে নিষেধ করেছেন কারণ তা জিনদের খাবার।", "Do not clean yourselves with bones or dung, for they are food for your Jinn brothers.", "সহীহ মুসলিম: ৪৫০", "Sahih Muslim: 450", "EASY"),

        ("প্রত্যেক মানুষের সাথে জন্মগতভাবে সংযুক্ত যে শয়তান সঙ্গী থাকে তাকে হাদিসের পরিভাষায় কী বলে?", "What is the designated personal companion Jinn attached to every human called?",
         ["ক্বারীন (Qareen)", "ইফরিত", "মারিদ", "খিনযাব"], ["Qareen", "Ifrit", "Marid", "Khinzab"], 0,
         "রাসূল ﷺ বলেন: তোমাদের প্রত্যেকের সাথে একটি করে জিন সঙ্গী (ক্বারীন) নির্ধারিত রয়েছে যে খারাপের প্ররোচনা দেয়।", "There is none among you except that he has a partner assigned to him from the Jinn.", "সহীহ মুসলিম: ২৮১৪", "Sahih Muslim: 2814", "EASY"),

        ("সালাতের মধ্যে ওয়াসওয়াসা ও বিভ্রান্তি সৃষ্টি করতে যে বিশেষ শয়তান আসে তার নাম হাদিসে কী এসেছে?", "What is the name of the specific devil that whispers doubts during Salah?",
         ["খিনযাব (Khinzab)", "ইবলিস", "হামাসাত", "দাশিম"], ["Khinzab", "Iblees", "Hamasat", "Dashim"], 0,
         "সালাতে ভুল বা অমনোযোগ সৃষ্টি হলে বাম দিকে তিনবার হালকা থুথু ফেলে আউযুবিল্লাহ পাঠ করতে হয়।", "When Khinzab distracts you, spit dryly to your left three times and seek refuge in Allah.", "সহীহ মুসলিম: ২২০৩", "Sahih Muslim: 2203", "EASY"),

        ("বাড়িতে প্রবেশের সময় এবং খাবার গ্রহণের শুরুতে 'বিসমিল্লাহ' বললে শয়তানের কী দশা হয়?", "What happens to Satan when one mentions Allah's name upon entering home and eating?",
         ["শয়তান তার সঙ্গীদের বলে: আজ তোমাদের এখানে থাকারও জায়গা নেই, খাওয়ারও সুযোগ নেই", "সে খুশি হয়", "সে সাথে খায়", "সে ঘুমায়"], ["Satan tells his minions: You have no lodging tonight and no meal here", "He rejoices", "He eats together", "He sleeps"], 0,
         "বিসমিল্লাহ না বললে শয়তান সেই ঘরে রাত কাটায় এবং খাবারে অংশীদার হয়।", "When Allah's name is mentioned, Satan finds neither shelter nor dinner in that house.", "সহীহ মুসলিম: ২০১০", "Sahih Muslim: 2010", "EASY"),

        ("আল্লাহর আরশ (Throne) বহনকারী ফেরেশতাদের কুরআনে কী বলা হয়েছে?", "What are the angels who carry the Mighty Throne of Allah called?",
         ["হামালাতুল আরশ (Bearers of the Throne)", "যাবানিয়াহ", "সাফির", "মুকাররাবীন"], ["Hamalat al-Arsh (Bearers of the Throne)", "Zabaniyah", "Safeer", "Muqarrabun"], 0,
         "কিয়ামতের দিন আটজন সম্মানিত ফেরেশতা তোমার রবের আরশকে তাদের মাথার ওপর বহন করবেন।", "And the angels will be on its sides, and eight will bear the Throne of your Lord (Surah Al-Haqqah: 17).", "সূরা আল-হাক্কাহ: ১৭", "Surah Al-Haqqah: 17", "EASY"),

        ("মানুষের দেহের ভেতরে রুহ বা আত্মার আসল স্বরূপ সম্পর্কে জানতে চাইলে আল্লাহ কুরআনে কী উত্তর দেন?", "What divine answer was revealed when people questioned the Prophet ﷺ regarding the Soul (Rooh)?",
         ["'রুহ হলো আমার রবের এক আদেশমাত্র এবং তোমাদের সামান্য জ্ঞানই দেওয়া হয়েছে'", "রুহ মাটি", "রুহ আগুন", "রুহ বাতাস"], ["'The soul is of the affair of my Lord, and of knowledge you have been given but a little'", "Soul is clay", "Soul is fire", "Soul is wind"], 0,
         "সূরা বনী ইসরাঈলে আল্লাহ রুহের রহস্যকে একমাত্র তাঁর ইলমের অধীনে বলে ঘোষণা করেছেন।", "Say: The soul is of the affair of my Lord, and you have been given but little knowledge.", "সূরা আল-ইসরা: ৮৫", "Surah Al-Isra: 85", "EASY"),

        ("বাইতুল মা'মুর (Bayt al-Ma'mur) কোথায় অবস্থিত যেখানে দৈনিক ৭০,০০০ ফেরেশতা সালাত আদায় করেন?", "Where is Bayt al-Ma'mur located where 70,000 angels pray daily never to return again?",
         ["সপ্তম আকাশে কাবার ঠিক সোজাসুজি উপরে", "প্রথম আকাশে", "মদীনায়", "আরাফাতে"], ["Directly above the Kaaba in the Seventh Heaven", "First heaven", "Madinah", "Arafat"], 0,
         "বাইতুল মা'মুরে প্রতিদিন সত্তর হাজার নতুন ফেরেশতা প্রবেশ করেন এবং কিয়ামত পর্যন্ত আর তাদের পুনরায় আসার পালা আসবে না।", "Bayt al-Ma'mur is the heavenly counterpart of the Kaaba visited by 70,000 angels daily.", "সহীহ বুখারী: ৩২০৭", "Sahih Bukhari: 3207", "EASY"),

        ("জিবরাইল (আ.)-এর আসল রূপের বিশালতা কতটি ডানা বিশিষ্ট ছিল যা রাসূল ﷺ সচক্ষে দেখেছিলেন?", "How many wings did Angel Jibreel (AS) have in his original celestial form seen by the Prophet ﷺ?",
         ["৬০০টি সুবিশাল ডানা যা দিগন্ত বিস্তৃত করে রেখেছিল", "১০০টি ডানা", "৫০টি ডানা", "১০টি ডানা"], ["600 immense wings blocking the entire horizon", "100 wings", "50 wings", "10 wings"], 0,
         "রাসূলুল্লাহ ﷺ জিবরাইল (আ.)-কে তাঁর আসল আকৃতিতে দু'বার দেখেছেন যা আসমান ও জমিনের মধ্যবর্তী স্থান পূর্ণ করে রেখেছিল।", "The Prophet ﷺ saw Jibreel in his true angelic form having 600 wings.", "সহীহ বুখারী: ৩২৩২", "Sahih Bukhari: 3232", "EASY"),

        ("হারুত ও মারুত নামক দুই ফেরেশতার ঘটনা কুরআনের কোন সূরায় যাদুর পরীক্ষা হিসেবে বর্ণিত হয়েছে?", "In which Surah is the historical test of the two angels Harut and Marut at Babylon narrated?",
         ["সূরা আল-বাক্বারাহ (১০২ নম্বর আয়াত)", "সূরা আল-ইমরান", "সূরা আন-নিসা", "সূরা আল-মায়িদাহ"], ["Surah Al-Baqarah (Verse 102)", "Surah Ali Imran", "Surah An-Nisa", "Surah Al-Ma'idah"], 0,
         "ব্যাবিলনে মানুষকে যাদুর কুফরির ক্ষতি সম্পর্কে সতর্ক করার পরীক্ষা হিসেবে তাদের প্রেরণ করা হয়।", "Harut and Marut warned: 'We are only a trial, so do not disbelieve' (Surah Al-Baqarah: 102).", "সূরা আল-বাক্বারাহ: ১০২", "Surah Al-Baqarah: 102", "MEDIUM"),

        ("মানুষের মৃত্যুর সময় সৎ মুমিনদের রুহ কবজ করতে ফেরেশতাগণ কোন্ বেশে উপস্থিত হন?", "In what form do angels descend to extract the soul of a righteous believer?",
         ["উজ্জ্বল শ্বেতশুভ্র মুখমণ্ডল ও জান্নাতের সুবাসিত রেশমী কাফন নিয়ে", "কালো কাপড়ে", "ভয়ানক শব্দে", "অস্ত্র নিয়ে"], ["Radiant faces like the sun carrying fragrant silk shrouds from Jannah", "Black clothes", "Terrifying noise", "Weapons"], 0,
         "মুমিনের রুহ সুরমা পাত্রের মুখ থেকে মিষ্টি পানির ফোঁটার মতো অতি সহজে প্রশান্তির সাথে বের হয়ে আসে।", "The soul of a believer emerges gently like a drop flowing from a waterskin.", "মুসনাদে আহমাদ: ১৮৫৩৪", "Musnad Ahmad: 18534", "EASY"),

        ("পাপী কাফের ও অবাধ্যদের রুহ বের করার সময় ফেরেশতাগণ কেমন কঠোরতা অবলম্বন করেন?", "How do the angels of punishment seize the soul of an unrepentant wicked person?",
         ["আগুনের দুর্গন্ধযুক্ত চট নিয়ে কর্কশ চেহারায় চরম কষ্টে শরীর থেকে টেনে ছেঁড়েন", "শান্তিতে নেন", "গান গেয়ে নেন", "ঘুম পাড়িয়ে নেন"], ["With foul scorching sackcloth ripping the soul out like thorny skewers through wet wool", "Peacefully", "Singing", "Sleeping"], 0,
         "ভিজা পশমের ভেতর থেকে কাঁটাযুক্ত শাখা টানলে যেমন সব ছিঁড়ে আসে, কাফেরের রুহ সেভাবে কষ্টদায়কভাবে বের করা হয়।", "The wicked soul is violently ripped out like thorny branches through wet wool.", "মুসনাদে আহমাদ: ১৮৫৩৪", "Musnad Ahmad: 18534", "MEDIUM"),

        ("বারযাখ (Barzakh) বলতে কোন পর্যায়কালীন জীবনকে বোঝানো হয়?", "What does the realm of 'Al-Barzakh' signify in Islamic eschatology?",
         ["মৃত্যুর পর থেকে কিয়ামতের পুনরুত্থান পর্যন্ত মধ্যবর্তী অন্তর্বর্তীকালীন জগত", "দুনিয়ার জীবন", "হাশরের দিন", "জান্নাতের বাগান"], ["The intermediate realm between physical death and bodily resurrection", "Worldly life", "Day of Gathering", "Jannah garden"], 0,
         "কুরআনে বলা হয়েছে: 'আর তাদের পেছনে বারযাখ থাকবে পুনরুত্থান দিবস পর্যন্ত।' ", "And behind them is a barrier (Barzakh) until the Day they are resurrected (Surah Al-Mu'minun: 100).", "সূরা আল-মুমিনূন: ১০০", "Surah Al-Mu'minun: 100", "EASY"),

        ("ঘুমের মধ্যে ভয়ংকর দুঃস্বপ্ন বা ভীতিপ্রদ স্বপ্ন দেখা কার পক্ষ থেকে ঘটে থাকে?", "From whom do terrifying nightmares or disturbing dreams originate?",
         ["শয়তানের পক্ষ থেকে প্ররোচনা ও আতঙ্ক সৃষ্টি", "আল্লাহর পক্ষ থেকে", "ফেরেশতার পক্ষ থেকে", "বাতাস থেকে"], ["From Satan trying to distress and frighten the believer", "From Allah", "From angels", "From wind"], 0,
         "রাসূল ﷺ বলেছেন: ভালো স্বপ্ন আল্লাহর পক্ষ থেকে এবং দুঃস্বপ্ন শয়তানের পক্ষ থেকে।", "Good dreams are from Allah, and bad nightmares are from Satan.", "সহীহ বুখারী: ৬৯৮৪", "Sahih Bukhari: 6984", "EASY"),

        ("দুঃস্বপ্ন দেখলে রাসূলুল্লাহ ﷺ কোন্ ৩টি জরুরি আমল করার নির্দেশ দিয়েছেন?", "What three actions did the Prophet ﷺ prescribe upon waking from a terrifying nightmare?",
         ["বাম দিকে তিনবার থুতু ফেলা, আউযুবিল্লাহ পাঠ করা এবং পার্শ্ব পরিবর্তন করা", "পানি খাওয়া", "অন্যকে বলা", "কান্নাকাটি করা"], ["Spit dryly to the left thrice, seek refuge in Allah, and switch sleeping sides", "Drink water", "Tell others", "Cry"], 0,
         "দুঃস্বপ্নের কথা কারও কাছে প্রকাশ না করলে তা বিন্দুমাত্র কোনো ক্ষতি করতে পারে না।", "Spit dryly to the left 3 times, seek refuge from Satan, and never narrate it to anyone.", "সহীহ মুসলিম: ২২৬১", "Sahih Muslim: 2261", "EASY"),

        ("জিনদের শক্তির প্রকারভেদে সবচেয়ে শক্তিশালী ও উগ্র দৈত্যাকার জিনদের কী বলা হয়?", "What are the most powerful, rebellious, and formidable giants among Jinn termed in the Quran?",
         ["ইফরিত (Ifrit) ও মারিদ (Marid)", "ক্বারীন", "হুর", "শাইতান"], ["Ifrit and Marid", "Qareen", "Hoor", "Shaitan"], 0,
         "সুলাইমান (আ.)-এর রাজসভায় এক ইফরিত জিন রানি বিলকিসের সিংহাসন চোখের পলকে আনার দাবি করেছিল।", "An Ifrit from the Jinn said: 'I will bring it to you before you rise from your place' (Surah An-Naml: 39).", "সূরা আন-নামল: ৩৯", "Surah An-Naml: 39", "MEDIUM"),

        ("জাদু (Sihr / Black Magic) করা ও জাদু শেখার ব্যাপারে ইসলামের অকাট্য বিধান কী?", "What is the unequivocal Islamic ruling on practicing and learning black magic (Sihr)?",
         ["কুফরি ও কবিরা গুনাহ যা মানুষকে দ্বীন ইসলাম থেকে বহিষ্কার করে দেয়", "জায়েজ", "মাকরুহ", "সাধারণ কৌশল"], ["Major Kufr (Disbelief) and a fatal destructive sin nullifying Islam", "Permissible", "Makruh", "Normal trick"], 0,
         "সূরা বাকারার ১০২ নম্বর আয়াতে যাদুর চর্চাকে সরাসরি শয়তানের কুফরি ঘোষণা করা হয়েছে।", "They followed what devils recited; Sulaiman did not disbelieve, but devils disbelieved teaching magic.", "সূরা আল-বাক্বারাহ: ১০২", "Surah Al-Baqarah: 102", "EASY"),

        ("গণক বা জ্যোতিষীর (সোত্থসেয়ার) কাছে গিয়ে ভাগ্য জানতে চাওয়া বা বিশ্বাস করার পরিণাম কী?", "What is the severe consequence of visiting a fortune-teller or astrologer?",
         ["৪০ দিনের সালাত কবুল হয় না এবং বিশ্বাস করলে কুরআনের সাথে কুফরি হয়", "সওয়াব হয়", "ক্ষতি নেই", "উপকার হয়"], ["His prayer is not accepted for 40 days, and believing him is disbelief in revelation", "Rewarding", "No harm", "Beneficial"], 0,
         "রাসূল ﷺ বলেন: যে জ্যোতিষীর কাছে যায় ও বিশ্বাস করে সে মুহাম্মদ ﷺ-এর ওপর অবতীর্ণ দ্বীনকে অস্বীকার করল।", "Whoever visits a diviner and asks him, his prayer will not be accepted for forty nights.", "সহীহ মুসলিম: ২২২৮", "Sahih Muslim: 2228", "EASY"),

        ("কুরআনের কোন আয়াতটি রাতে শয়নের পূর্বে পাঠ করলে সারারাত আল্লাহর পক্ষ থেকে একজন রক্ষক ফেরেশতা পাহারা দেয়?", "Which verse protects a person throughout the night with a guardian angel until morning?",
         ["আয়াতুল কুরসী (সূরা বাকারা: ২৫৫)", "সূরা কাফিরুন", "সূরা কুরাইশ", "সূরা ফীল"], ["Ayat al-Kursi (Surah Al-Baqarah: 255)", "Surah Kafirun", "Surah Quraysh", "Surah Fil"], 0,
         "আবু হুরায়রা (রা.)-কে শয়তান নিজে স্বীকার করেছিল যে আয়াতুল কুরসী পড়লে সকাল পর্যন্ত শয়তান কাছে ঘেঁষতে পারে না।", "Reciting Ayat al-Kursi before sleep keeps a guardian angel over you until dawn.", "সহীহ বুখারী: ২৩১১", "Sahih Bukhari: 2311", "EASY"),

        ("জিন ও মানুষের সৃষ্টিজগতের মূল সৃষ্টির উদ্দেশ্য হিসেবে আল্লাহ সূরা যারিয়াতে কী ঘোষণা করেছেন?", "What is the sole ultimate purpose of creating Jinn and Mankind declared in Surah Adh-Dhariyat?",
         ["'আমি জিন ও মানবজাতিকে কেবল আমার খাঁটি ইবাদত করার জন্যই সৃষ্টি করেছি'", "পার্থিব ভোগ", "যুদ্ধ করা", "বাণিজ্য করা"], ["'And I did not create the Jinn and Mankind except to worship Me alone' (Surah Adh-Dhariyat: 56)", "Worldly enjoyment", "Fighting", "Trading"], 0,
         "আল্লাহর তাওহীদের স্বীকৃতি ও একনিষ্ঠ আনুগত্যের ইবাদতই জিন ও মানব সৃষ্টির একমাত্র মূল উদ্দেশ্য।", "The singular divine purpose of existence is the sincere worship of Allah alone.", "সূরা আয-যারিয়াত: ৫৬", "Surah Adh-Dhariyat: 56", "EASY")
    ]
    return [('Q_JIN_' + str(i+1).zfill(3), 'jinn_unseen', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_12_history():
    items = [
        ("খুলাফায়ে রাশিদীনের যুগ কয় বছর স্থায়ী হয়েছিল যা রাসূল ﷺ-এর ভবিষ্যদ্বাণীতে বর্ণিত হয়েছিল?", "How many years did the Golden Era of the Rightly Guided Caliphate (Khilafah Rashidah) last?",
         ["৩০ বছর", "৪০ বছর", "২০ বছর", "৫০ বছর"], ["30 Years", "40 Years", "20 Years", "50 Years"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আমার পর খিলাফতে রাশিদাহ ৩০ বছর স্থায়ী হবে, এরপর রাজতন্ত্র আসবে।", "The Caliphate upon the prophetic model will last thirty years.", "সুনানে আবু দাউদ: ৪৬৪৬", "Sunan Abi Dawud: 4646", "EASY"),

        ("উমাইয়া খেলাফতের (Umayyad Caliphate) প্রতিষ্ঠাতা ও প্রথম খলিফা কে ছিলেন?", "Who was the founder and first Caliph of the Umayyad Dynasty?",
         ["হযরত মুয়াবিয়া ইবনে আবি সুফিয়ান (রা.)", "মারওয়ান ইবনুল হাকাম", "আব্দুল মালিক বিন মারওয়ান", "ইয়াজিদ"], ["Muawiyah ibn Abi Sufyan (RA)", "Marwan ibn al-Hakam", "Abdul Malik", "Yazid"], 0,
         "হযরত মুয়াবিয়া (রা.) ৪১ হিজরিতে দামেস্কে রাজধানী স্থাপন করে উমাইয়া খেলাফতের সূচনা করেন।", "Muawiyah (RA) founded the Umayyad Caliphate in 41 AH with Damascus as capital.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("উমাইয়া খলিফাদের মধ্যে তাঁর অতুলনীয় সততা ও ইনসাফের জন্য কাকে 'পঞ্চম খলিফায়ে রাশিদ' বলা হয়?", "Which Umayyad Caliph is universally revered as the 'Fifth Rightly Guided Caliph' for his justice?",
         ["হযরত উমর ইবনে আব্দুল আজিজ (রহ.)", "ওয়ালিদ ইবনে আব্দুল মালিক", "হিশাম ইবনে আব্দুল মালিক", "সুলাইমান"], ["Umar ibn Abdul Aziz (RA)", "Walid ibn Abdul Malik", "Hisham", "Sulaiman"], 0,
         "উমর ইবনে আব্দুল আজিজ (রহ.)-এর শাসনামলে সুবিচার এতো উচ্চে পৌঁছেছিল যে যাকাত নেওয়ার মতো গরিব খুঁজে পাওয়া যেত না।", "Umar ibn Abdul Aziz restored strict justice and prophetic simplicity across the Caliphate.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),

        ("স্পেন (আন্দালুসিয়া) জয় করে ইউরোপে ইসলামের সোনালী প্রবেশদ্বার উন্মোচন করেছিলেন কোন মুসলিম বীর সেনাপতি?", "Which legendary Muslim general conquered Spain (Andalusia) in 711 CE?",
         ["তারিক বিন যিয়াদ (Tariq ibn Ziyad)", "মুসা বিন নুসাইর", "মুহাম্মদ বিন কাসিম", "কুতাইবা বিন মুসলিম"], ["Tariq ibn Ziyad", "Musa ibn Nusayr", "Muhammad ibn Qasim", "Qutaybah ibn Muslim"], 0,
         "তারিক বিন যিয়াদ জিব্রাল্টার (জাবালে তারিক) প্রণালী অতিক্রম করে আন্দালুসিয়া বিজয় করেন।", "Tariq ibn Ziyad crossed the straits (Gibraltar / Jabal Tariq) opening 800 years of Islamic Spain.", "তারিখে তাবারী", "Tarikh al-Tabari", "EASY"),

        ("মাত্র ১৭ বছর বয়সে ভারতীয় উপমহাদেশে (সিন্ধু ও মুলতান) প্রথম ইসলামের বিজয় পতাকা উড্ডয়ন করেছিলেন কে?", "Which 17-year-old Muslim general liberated Sindh and Multan, introducing Islam to India?",
         ["মুহাম্মদ বিন কাসিম (Muhammad ibn Qasim)", "মাহমুদ গজনভী", "মুহাম্মদ ঘোরী", "কুতুবউদ্দিন আইবেক"], ["Muhammad ibn Qasim", "Mahmud of Ghazni", "Muhammad Ghori", "Qutb ud-Din Aibak"], 0,
         "৭১২ খ্রিস্টাব্দে রাজা দাহিরের অত্যাচার থেকে নারী ও বন্দিদের উদ্ধারে সিন্ধু বিজয় করেন মুহাম্মদ বিন কাসিম।", "Muhammad ibn Qasim brought Islamic justice to the Indian subcontinent in 712 CE.", "আল-কামিল ফিত তারিখ", "Al-Kamil fit-Tarikh", "EASY"),

        ("আব্বাসীয় খেলাফতের (Abbasid Caliphate) রাজধানী হিসেবে কোন বিখ্যাত ঐতিহাসিক নগরী প্রতিষ্ঠিত হয়েছিল?", "Which famous world capital was founded as the heart of the Abbasid Caliphate?",
         ["বাগদাদ (মদিনাতুস সালাম)", "দামেস্ক", "কায়রো", "ইস্তাম্বুল"], ["Baghdad (Madinat as-Salam)", "Damascus", "Cairo", "Istanbul"], 0,
         "খলিফা আবু জাফর আল-মনসুর ৭৬২ খ্রিস্টাব্দে টাইগ্রিস নদীর তীরে জ্ঞান-বিজ্ঞানের প্রাণকেন্দ্র বাগদাদ প্রতিষ্ঠা করেন।", "Caliph Al-Mansur founded the glorious capital Baghdad in 762 CE.", "তারিখে বাগদাদ", "Tarikh Baghdad", "EASY"),

        ("আব্বাসীয় যুগে জ্ঞান-বিজ্ঞান, দর্শন ও চিকিৎসাশাস্ত্র অনুবাদের বিশ্ববিখ্যাত গবেষণা প্রতিষ্ঠানের নাম কী ছিল?", "What was the world-famous Grand Library and Academy of Baghdad called?",
         ["বায়তুল হিকমাহ (House of Wisdom)", "দারুল উলূম", "আল-আজহার", "কুরতুব বিশ্ববিদ্যালয়"], ["Bayt al-Hikmah (House of Wisdom)", "Darul Ulum", "Al-Azhar", "Cordoba Academy"], 0,
         "খলিফা হারুনুর রশীদ ও আল-মামুনের পৃষ্ঠপোষকতায় বায়তুল হিকমাহ বৈশ্বিক বিজ্ঞানের শীর্ষে পৌঁছায়।", "Bayt al-Hikmah served as the global intellectual catalyst during the Islamic Golden Age.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),

        ("হিটটিনের ঐতিহাসিক যুদ্ধে (১১৮৭ খ্রি.) ক্রুসেডারদের পরাজিত করে জেরুজালেম পুনরুদ্ধার করেছিলেন কোন মহান সুলতান?", "Which noble Sultan liberated Jerusalem from the Crusaders at the Battle of Hattin (1187 CE)?",
         ["সুলতান সালাহুদ্দীন আইয়ুবী (Saladin)", "নুরুদ্দীন জঙ্গি", "সুলতান বাইবার্স", "সুলতান মাহমুদ"], ["Sultan Salahuddin Ayyubi (Saladin)", "Nur ad-Din Zengi", "Sultan Baybars", "Sultan Mahmud"], 0,
         "সালাহুদ্দীন আইয়ুবী পরম ক্ষমা ও মহানুভবতার সাথে রক্তপাতহীনভাবে বাইতুল মুকাদ্দাস মুক্ত করেন।", "Salahuddin Ayyubi liberated Jerusalem exhibiting unparalleled chivalry and mercy.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("১৪৫৩ খ্রিস্টাব্দে মাত্র ২১ বছর বয়সে ঐতিহাসিক কনস্টান্টিনোপল (ইস্তাম্বুল) বিজয় করেছিলেন কোন উসমানীয় সুলতান?", "Which 21-year-old Ottoman Sultan conquered Constantinople (Istanbul) in 1453 CE?",
         ["সুলতান মুহাম্মদ আল-ফাতিহ (Mehmed the Conqueror)", "সুলতান সুলাইমান কানুনী", "সুলতান সেলিম", "সুলতান বায়েজিদ"], ["Sultan Mehmed II (Al-Fatih)", "Suleiman the Magnificent", "Sultan Selim", "Sultan Bayezid"], 0,
         "রাসূল ﷺ সুসংবাদ দিয়েছিলেন: 'তোমরা অবশ্যই কনস্টান্টিনোপল জয় করবে; কতই না উত্তম সেই সেনাপতি ও সেনাবাহিনী!' ", "The Prophet ﷺ prophesied the glorious conquest of Constantinople by a noble commander.", "মুসনাদে আহমাদ: ১৮৯৫৭", "Musnad Ahmad: 18957", "EASY"),

        ("বিশ্বের প্রাচীনতম নিরবচ্ছিন্নভাবে চলমান বিশ্ববিদ্যালয় হিসেবে স্বীকৃত কোনটি যা একজন মুসলিম নারী প্রতিষ্ঠা করেছিলেন?", "Which is recognized as the world's oldest continually operating university, founded by a Muslim woman?",
         ["মরক্কোর আল-কারাওইন বিশ্ববিদ্যালয় (ফাতিমা আল-ফিহরি কর্তৃক ৮৫৯ খ্রি.)", "আল-আজহার বিশ্ববিদ্যালয়", "অক্সফোর্ড বিশ্ববিদ্যালয়", "কেমব্রিজ বিশ্ববিদ্যালয়"], ["University of al-Qarawiyyin (Morocco by Fatima al-Fihri 859 CE)", "Al-Azhar", "Oxford", "Cambridge"], 0,
         "ফাতিমা আল-ফিহরি মরক্কোর ফেজে এই অমর বিদ্যাপীঠ প্রতিষ্ঠা করেন যা ইউনেস্কো স্বীকৃত প্রাচীনতম বিশ্ববিদ্যালয়।", "Fatima al-Fihri established Al-Qarawiyyin, verified by UNESCO as the oldest university.", "আল-ইসতিবসার", "Al-Istibsar", "EASY"),

        ("চিকিৎসাশাস্ত্রের অবিসংবাদিত জনক এবং 'আল-কানুন ফিত-তিব্ব' (Canon of Medicine) গ্রন্থের রচয়িতা কে?", "Who is the Father of Modern Medicine and author of the legendary 'The Canon of Medicine'?",
         ["ইবনে সিনা (Avicenna)", "আল-রাযী (Rhazes)", "ইবনে রুশদ", "আল-ফারাবি"], ["Ibn Sina (Avicenna)", "Al-Razi", "Ibn Rushd", "Al-Farabi"], 0,
         "ইবনে সিনার রচিত চিকিৎসা বিশ্বকোষ ইউরোপের মেডিকেল কলেজসমূহে দীর্ঘ ৫০০ বছর প্রধান পাঠ্যবই ছিল।", "Ibn Sina's Canon of Medicine remained the gold standard medical textbook in Europe for centuries.", "সিয়ারু আ'লামিন নুবালা", "Siyar A'lam an-Nubala", "EASY"),

        ("বীজগণিত (Algebra) ও অ্যালগরিদমের (Algorithm) আবিষ্কারক এবং 'আল-জাবর' গ্রন্থের প্রণেতা কে?", "Who is the founder of Algebra and the pioneer of Algorithm science?",
         ["মুহাম্মদ ইবনে মূসা আল-খাওয়ারিজমি (Al-Khwarizmi)", "আল-বাত্তানী", "আল-কিন্দি", "উমর খৈয়াম"], ["Muhammad ibn Musa al-Khwarizmi", "Al-Battani", "Al-Kindi", "Omar Khayyam"], 0,
         "আল-খাওয়ারিজমির নাম থেকেই 'অ্যালগরিদম' এবং তাঁর গ্রন্থ 'আল-জাবর' থেকেই 'বীজগণিত/Algebra' নামের উৎপত্তি।", "Al-Khwarizmi formulated Algebra and the systemic numbering concept algorithm.", "কাশফুয যুনূন", "Kashf az-Zunun", "EASY"),

        ("আধুনিক আলোকবিজ্ঞান (Optics)-এর জনক এবং ক্যামেরা অবস্কিউরার মূল উদ্ভাবক কে ছিলেন?", "Who is the Father of Modern Optics and the discoverer of the camera obscura principles?",
         ["ইবনুল হাইসাম (Alhazen)", "আল-বিরুনি", "জাবির ইবনে হাইয়ান", "ইবনে বতুতা"], ["Ibn al-Haytham (Alhazen)", "Al-Biruni", "Jabir ibn Hayyan", "Ibn Battuta"], 0,
         "ইবনুল হাইসাম 'কিতাবুল মানাযির' গ্রন্থে প্রমাণের সাহায্যে প্রতিষ্ঠা করেন যে আলো বস্তু থেকে প্রতিফলিত হয়ে চোখে প্রবেশ করে।", "Ibn al-Haytham revolutionized experimental physics and optical sciences.", "মু'জামুল উদাবা", "Mu'jam al-Udaba", "EASY"),

        ("রসায়নশাস্ত্রের জনক (Father of Chemistry) হিসেবে বিশ্বখ্যাত মুসলিম বিজ্ঞানী কে ছিলেন?", "Who is celebrated as the Father of Chemistry for introducing experimental laboratory methods?",
         ["জাবির ইবনে হাইয়ান (Geber)", "আল-রাযী", "ইবনে সিনা", "আল-মাজরিতী"], ["Jabir ibn Hayyan (Geber)", "Al-Razi", "Ibn Sina", "Al-Majriti"], 0,
         "জাবির ইবনে হাইয়ান পরিশ্রুতকরণ, বাষ্পীভবন, সালফিউরিক এসিড ও নাইট্রিক এসিডের প্রস্তুতি আবিষ্কার করেন।", "Jabir ibn Hayyan pioneered chemical distillation, crystallization, and key mineral acids.", "তারিখুল হুকামা", "Tarikh al-Hukama", "EASY"),

        ("বিশ্বের সর্বশ্রেষ্ঠ পর্যটক হিসেবে যিনি ৩০ বছরে ১,২০,০০০ কিলোমিটার পথ ভ্রমণ করেছিলেন তাঁর নাম কী?", "Who is the greatest traveler in pre-modern history, journeying over 120,000 km across the globe?",
         ["ইবনে বতুতা (Ibn Battuta)", "ইবনে জুবায়ের", "আল-ইদ্রিসি", "আল-মাসউদি"], ["Ibn Battuta", "Ibn Jubayr", "Al-Idrisi", "Al-Mas'udi"], 0,
         "মরক্কোর তানজিয়ারে জন্ম নেওয়া ইবনে বতুতা আফ্রিকা, আরব, পারস্য, ভারত, মালদ্বীপ ও চীন ভ্রমণ করেন।", "Ibn Battuta explored Africa, the Middle East, India, Southeast Asia, and China.", "রিহলাতু ইবনে বতুতা", "Rihlah Ibn Battuta", "EASY"),

        ("সমাজবিজ্ঞান ও ইতিহাস দর্শনের জনক এবং বিখ্যাত 'মুকাদ্দিমাহ' (Muqaddimah)-এর প্রণেতা কে?", "Who is the Father of Sociology and Historiography, author of the profound 'Muqaddimah'?",
         ["ইবনে খালদুন (Ibn Khaldun)", "ইবনে রুশদ", "আল-মাওয়ার্দী", "ইবনে কাসীর"], ["Ibn Khaldun", "Ibn Rushd", "Al-Mawardi", "Ibn Kathir"], 0,
         "ইবনে খালদুন আসাবিয়্যাহ (সামাজিক সংহতি) ও সভ্যতার উত্থান-পতনের কালজয়ী বৈজ্ঞানিক দর্শন প্রণয়ন করেন।", "Ibn Khaldun established sociology, political theory, and critical historical analysis.", "আল-মুকাদ্দিমাহ", "Al-Muqaddimah", "EASY"),

        ("ইউরোপে মুসলিম শাসিত আন্দালুসিয়ার কর্ডোভা (কুরতুব) নগরী মধ্যযুগে কিসের কেন্দ্র ছিল?", "What was Cordoba (Qurtubah) in Islamic Spain celebrated for during the European Dark Ages?",
         ["আলোর নগরী, ৭০টি পাবলিক লাইব্রেরি, চিকিৎসালয় ও সুবিশাল জ্ঞানচর্চার কেন্দ্র", "অন্ধকারাচ্ছন্ন শহর", "কৃষিকেন্দ্র", "কেবল সামরিক দুর্গ"], ["City of Light with 70 public libraries, paved illuminated streets, and universities", "Dark town", "Farming", "Fort only"], 0,
         "ইউরোপের অন্যান্য স্থান যখন অন্ধকারে নিমজ্জিত, কর্ডোভায় তখন রাজকীয় লাইব্রেরিতে ৪ লক্ষাধিক বই সংরক্ষিত ছিল।", "Islamic Cordoba was the jewel of Europe, leading in medicine, astronomy, and philosophy.", "নাফহুত তীব", "Nafh at-Teeb", "EASY"),

        ("১২৫৮ খ্রিস্টাব্দে হালাকু খানের নেতৃত্বে মোঙ্গলদের নারকীয় আক্রমণে কোন মুসলিম জ্ঞানপীঠের পতন ঘটেছিল?", "Which glorious Islamic capital fell to the catastrophic Mongol invasion under Hulagu Khan in 1258 CE?",
         ["বাগদাদ নগরী", "দামেস্ক", "মক্কা", "কায়রো"], ["Baghdad", "Damascus", "Makkah", "Cairo"], 0,
         "মোঙ্গলরা বাগদাদের লাইব্রেরির লক্ষ লক্ষ অমূল্য পাণ্ডুলিপি টাইগ্রিস নদীতে ফেলে নদী কালো করে দিয়েছিল।", "The sack of Baghdad in 1258 CE devastated centuries of accumulated human knowledge.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("আইন জালুতের ঐতিহাসিক যুদ্ধে (১২৬০ খ্রি.) মোঙ্গলদের অপরাজেয় অগ্রযাত্রাকে চিরতরে স্তব্ধ করেছিল কোন মুসলিম বাহিনী?", "Which Muslim force decisively shattered the invincible Mongol army at the Battle of Ain Jalut (1260 CE)?",
         ["মামলুক বাহিনী (সুলতান সাইফুদ্দিন কুতুজ ও বাইবার্স)", "উমাইয়া বাহিনী", "উসমানীয় বাহিনী", "মুঘল বাহিনী"], ["Mamluk Army (Sultan Qutuz and Baybars)", "Umayyad", "Ottoman", "Mughal"], 0,
         "মামলুক বীর সাইফুদ্দিন কুতুজ 'ওয়া ইসলামাহ' ধ্বনি দিয়ে মোঙ্গল বাহিনীকে শোচনীয়ভাবে পরাস্ত করেন।", "The Mamluks under Qutuz and Baybars saved Islamic civilization from complete destruction.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("উসমানীয় সাম্রাজ্যের (Ottoman Empire) প্রতিষ্ঠাতা ও প্রথম শাসক কে ছিলেন?", "Who was the founder and first Sultan of the Ottoman Dynasty?",
         ["উসমান গাজী (Osman I Gazi)", "এরতুগ্রুল গাজী", "ওরহান গাজী", "মুরাদ ১ম"], ["Osman I Gazi", "Ertugrul Gazi", "Orhan Gazi", "Murad I"], 0,
         "এরতুগ্রুল গাজীর পুত্র উসমান গাজী ১২৯৯ খ্রিস্টাব্দে উসমানীয় খিলাফতের ভিত্তি স্থাপন করেন।", "Osman I established the Ottoman Beylik in 1299 CE which lasted over 600 years.", "উসমানীয় ইতিহাস", "Ottoman History", "EASY"),

        ("উসমানীয় সাম্রাজ্যের সুবর্ণ যুগের মহান খলিফা যাঁকে ইউরোপীয়রা 'ম্যাগনিফিসেন্ট' বলত তাঁর নাম কী?", "Which Ottoman Caliph was known in Europe as 'Suleiman the Magnificent' and in Islam as 'Al-Qanuni'?",
         ["সুলতান সুলাইমান কানুনী (Suleiman I)", "সুলতান সেলিম ১ম", "সুলতান আব্দুল হামিদ ২য়", "সুলতান মাহমুদ ২য়"], ["Sultan Suleiman the Magnificent (Al-Qanuni)", "Selim I", "Abdul Hamid II", "Mahmud II"], 0,
         "সুলতান সুলাইমান সুবিচার ও সুসংহত আইনি সংস্কারের মাধ্যমে উসমানীয় সাম্রাজ্যকে বিশ্বসেরা শক্তিতে পরিণত করেন।", "Suleiman al-Qanuni expanded the empire and codified standardized legal administrative codes.", "উসমানীয় ইতিহাস", "Ottoman History", "EASY"),

        ("ভারতবর্ষে মুঘল সাম্রাজ্যের প্রতিষ্ঠাতা কে ছিলেন যিনি ১৫২৬ সালে প্রথম পানিপথের যুদ্ধে জয়লাভ করেন?", "Who founded the Mughal Empire in India by winning the First Battle of Panipat in 1526 CE?",
         ["জহিরুদ্দিন মুহাম্মদ বাবর", "হুমায়ুন", "আকবর", "শাহজাহান"], ["Zahir-ud-din Muhammad Babur", "Humayun", "Akbar", "Shah Jahan"], 0,
         "সমরকন্দের রাজপুত্র বাবর পানিপথের প্রথম যুদ্ধে ইব্রাহিম লোদিকে পরাজিত করে মুঘল সাম্রাজ্য কায়েম করেন।", "Babur established Mughal rule across northern India in 1526 CE.", "তুজকে বাবরি", "Tuzk-e-Babri", "EASY"),

        ("মুঘল বাদশাহদের মধ্যে যিনি হাফেজে কুরআন ছিলেন এবং ফতোয়ায়ে আলমগীরী সংকলন করান তাঁর নাম কী?", "Which pious Mughal Emperor was a Hafiz of the Quran and commissioned Fatawa-e-Alamgiri?",
         ["মুহিউদ্দীন আওরঙ্গজেব আলমগীর", "শাহজাহান", "জাহাঙ্গীর", "বাবর"], ["Muhi-ud-Din Aurangzeb Alamgir", "Shah Jahan", "Jahangir", "Babur"], 0,
         "আওরঙ্গজেব আলমগীর রাষ্ট্রীয় কোষাগার থেকে এক পয়সাও নিজের খরচে নিতেন না; টুপি সেলাই ও কুরআন লিখে জীবিকা নির্বাহ করতেন।", "Aurangzeb ruled with strict personal piety and codified Islamic jurisprudence in Fatawa-e-Alamgiri.", "তারিখে হিন্দ", "Tarikh-e-Hind", "EASY"),

        ("মিশরের রাজধানী কায়রোতে অবস্থিত আল-আজহার বিশ্ববিদ্যালয় কোন শতাব্দীতে প্রতিষ্ঠিত হয়েছিল?", "In which century was Al-Azhar Mosque and University founded in Cairo?",
         ["১০ম শতাব্দীতে (৯৭০-৯৭২ খ্রি.)", "১২শ শতাব্দীতে", "৮ম শতাব্দীতে", "১৪শ শতাব্দীতে"], ["10th Century (970-972 CE)", "12th Century", "8th Century", "14th Century"], 0,
         "ফাতেমীয় আমলে প্রতিষ্ঠিত আল-আজহার পরবর্তীতে সুন্নি বিশ্বের প্রধানতম ইসলামী জ্ঞানকেন্দ্রে রূপ নেয়।", "Al-Azhar was founded in 970 CE and remains the foremost center of Islamic theology.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),

        ("ইসলামী অর্থনীতিতে রৌপ্যমুদ্রা ও স্বর্ণমুদ্রার আন্তর্জাতিক ঐতিহাসিক নাম কী ছিল?", "What were the historical Islamic standard currencies of gold and silver called?",
         ["স্বর্ণমুদ্রা: দিনার (Dinar) এবং রৌপ্যমুদ্রা: দিরহাম (Dirham)", "টাকা ও পয়সা", "রুপিয়া ও আনা", "রিয়াল ও পাউন্ড"], ["Gold: Dinar and Silver: Dirham", "Taka and Paisa", "Rupee and Anna", "Riyal and Pound"], 0,
         "খলিফা আব্দুল মালিক বিন মারওয়ান প্রথম আরবি খোদাইকৃত প্রমিত দিনার ও দিরহাম চালু করেন।", "Caliph Abdul Malik standardized pure Islamic gold Dinars and silver Dirhams.", "আল-আমওয়াল", "Al-Amwal", "EASY"),

        ("ইসলামের স্বর্ণযুগে বিশ্বের অন্যতম শ্রেষ্ঠ ভূগোলে মানচিত্রকার যিনি রাজা দ্বিতীয় রজারের জন্য মানচিত্র আঁকেন কে?", "Who was the legendary Muslim cartographer who created the detailed 'Tabula Rogeriana' world map?",
         ["আল-ইদ্রিসি (Muhammad al-Idrisi)", "আল-মাসউদি", "ইয়াকুত আল-হামাবী", "ইবনে হাওকাল"], ["Muhammad al-Idrisi", "Al-Mas'udi", "Yaqut al-Hamawi", "Ibn Hawqal"], 0,
         "আল-ইদ্রিসি ১১৪৪ খ্রিস্টাব্দে সিসিলিতে রৌপ্য গোলকের ওপর নিখুঁত গোলাকার পৃথিবীর মানচিত্র নির্মাণ করেন।", "Al-Idrisi constructed the most accurate world map of the medieval era in Sicily.", "নুযহাতুল মুশতাক", "Nuzhat al-Mushtaq", "MEDIUM"),

        ("চিকিৎসাবিজ্ঞানে রক্তের ফুসফুসীয় সঞ্চালন প্রক্রিয়া (Pulmonary Circulation of Blood) সর্বপ্রথম আবিষ্কার করেন কে?", "Who first correctly described the pulmonary circulation of blood centuries before William Harvey?",
         ["ইবনুন নাফিস (Ibn al-Nafis)", "ইবনে সিনা", "আল-রাযী", "ইবনে জুহর"], ["Ibn al-Nafis", "Ibn Sina", "Al-Razi", "Ibn Zuhr"], 0,
         "ইবনুন নাফিস প্রমাণ করেন যে রক্ত হৃৎপিণ্ডের ডান অংশ থেকে ফুসফুসে গিয়ে পরিশোধিত হয়ে বাম অংশে আসে।", "Ibn al-Nafis discovered pulmonary blood transit 300 years before European scientists.", "শারহু তাশরীহিল কানুন", "Sharh Tashrih al-Qanun", "MEDIUM"),

        ("সুলতান সালাহুদ্দীন আইয়ুবীর মহৎ চরিত্রের কারণে ক্রুসেডার শত্রু বাদশাহ কে তাঁর প্রতি গভীর শ্রদ্ধা পোষণ করতেন?", "Which English Crusader King held profound respect for the chivalry of Sultan Saladin?",
         ["রিচার্ড দ্য লায়নহার্ট (King Richard the Lionheart)", "ফিলিপ অগাস্টাস", "ফ্রেডেরিক বারবারোসা", "লুইস ৯ম"], ["King Richard the Lionheart", "Philip Augustus", "Frederick Barbarossa", "Louis IX"], 0,
         "যুদ্ধক্ষেত্রে রিচার্ড অসুস্থ হলে সালাহুদ্দীন নিজের ব্যক্তিগত চিকিৎসক ও বরফশীতল ফল পাঠিয়ে মানবিকতার পরাকাষ্ঠা দেখান।", "Saladin famously sent snow and his personal physician to treat King Richard.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("বায়তুল মুকাদ্দাসে ক্রুসেডাররা ১০৯৯ সালে দখলের সময় যে নির্মম গণহত্যা চালিয়েছিল তার তুলনায় সালাহুদ্দীনের নীতি কী ছিল?", "How did Saladin's capture of Jerusalem in 1187 contrast with the Crusader massacre of 1099?",
         ["সম্পূর্ণ অহিংসা, সর্বজনীন ক্ষমা ও বন্দিদের বিনাশুল্কে মুক্তি প্রদান", "সকলকে হত্যা", "সম্পদ ধ্বংস", "শহর পোড়ানো"], ["Complete safety, universal amnesty, and paying ransoms for the poor", "Killing all", "Seizing wealth", "Burning city"], 0,
         "সালাহুদ্দীন আইয়ুবী খ্রিস্টান নাগরিকদের নিরাপদে প্রস্থান ও ধর্মপালনের পূর্ণ স্বাধীনতা দেন।", "Salahuddin prohibited looting and unharmed the civilian population.", "সীরাতে সালাহুদ্দীন", "Sirah Salahuddin", "EASY"),

        ("ইসলামী উসমানীয় খিলাফতের সর্বশেষ ক্ষমতাধর ন্যায়পরায়ণ সুলতান যিনি ফিলিস্তিন রক্ষায় অবিচল ছিলেন কে?", "Which last powerful Ottoman Sultan steadfastly refused to sell Palestinian land to Zionists?",
         ["সুলতান দ্বিতীয় আব্দুল হামিদ (Abdul Hamid II)", "সুলতান মুরাদ ৫ম", "সুলতান ওয়াহিদউদ্দীন", "সুলতান আব্দুল মজিদ"], ["Sultan Abdul Hamid II", "Sultan Murad V", "Sultan Vahidettin", "Sultan Abdulmejid"], 0,
         "সুলতান আব্দুল হামিদ বলেছিলেন: 'ফিলিস্তিন আমার ব্যক্তিগত সম্পত্তি নয়, এটা মুসলিম উম্মাহর আমানত।' ", "Abdul Hamid II declared: I cannot sell even an inch of Palestine as it belongs to the Ummah.", "স্মৃতিকথা আব্দুল হামিদ", "Memoirs of Abdul Hamid II", "EASY"),

        ("প্রথম বিশ্বযুদ্ধের পর কত খ্রিস্টাব্দে আনুষ্ঠানিকভাবে উসমানীয় খেলাফত বিলুপ্ত ঘোষণা করা হয়?", "In which year was the Ottoman Caliphate officially abolished in modern Turkey?",
         ["১৯২৪ খ্রিস্টাব্দে (৩রা মার্চ)", "১৯১৮ খ্রিস্টাব্দে", "১৯২৩ খ্রিস্টাব্দে", "১৯৩০ খ্রিস্টাব্দে"], ["1924 CE (March 3)", "1918 CE", "1923 CE", "1930 CE"], 0,
         "মুস্তফা কামাল আতাতুর্কের নেতৃত্বে ৩রা মার্চ ১৯২৪ সালে খেলাফত ব্যবস্থা বিলুপ্ত করা হয়।", "The Caliphate was officially dissolved on March 3, 1924.", "আধুনিক ইসলামী ইতিহাস", "Modern Islamic History", "EASY"),

        ("ইসলামী স্বর্ণযুগে রোবটিক্স ও অটোমেশনের জনক (Father of Robotics) বলা হয় কোন প্রখ্যাত মুসলিম প্রকৌশলীকে?", "Who is celebrated as the Father of Robotics and automation for inventing programmable automata?",
         ["বদিউযযামান ইসমাইল আল-জাজারী (Al-Jazari)", "আল-খাযিনী", "বানু মুসা ভ্রাতৃদ্বয়", "আল-ফারাবি"], ["Ismail al-Jazari", "Al-Khazini", "Banu Musa brothers", "Al-Farabi"], 0,
         "আল-জাজারী তাঁর 'কিতাব ফী মারিফাতিল হিয়াল' গ্রন্থে হস্তী-ঘড়ি, ক্র্যাঙ্কশ্যাফট ও স্বয়ংক্রিয় পানির পাম্প উদ্ভাবন করেন।", "Al-Jazari invented the crankshaft, programmable automata, and the Elephant Clock.", "আল-হিয়াল আল-হেন্দাসিয়্যাহ", "Al-Hiyal al-Handasiyyah", "MEDIUM"),

        ("বিশ্বের প্রথম গম্বুজযুক্ত এবং কুরআনিক ক্যালিগ্রাফিতে সজ্জিত বিখ্যাত 'কুব্বাতুস সাখরা' (Dome of the Rock) কে নির্মাণ করেন?", "Which Umayyad Caliph built the iconic Dome of the Rock (Qubbat as-Sakhrah) in Jerusalem?",
         ["খলিফা আব্দুল মালিক ইবনে মারওয়ান (৬৮৫-৬৯১ খ্রি.)", "ওয়ালিদ ১ম", "উমর ২য়", "মুয়াবিয়া ১ম"], ["Caliph Abdul Malik ibn Marwan (691 CE)", "Walid I", "Umar II", "Muawiyah I"], 0,
         "আব্দুল মালিক বাইতুল মুকাদ্দাস প্রাঙ্গণে পবিত্র পাথরের ওপর এই স্বর্ণালী গম্বুজ নির্মাণ করেন।", "The Dome of the Rock was constructed by Caliph Abdul Malik in 691 CE.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("সিরিয়ার দামেস্কের বিশ্বখ্যাত ঐতিহাসিক 'উমাইয়া জামে মসজিদ' কোন খলিফার আমলে নির্মিত হয়েছিল?", "Under which Caliph's reign was the magnificent Umayyad Grand Mosque in Damascus constructed?",
         ["খলিফা প্রথম আল-ওয়ালিদ (Al-Walid I)", "সুলাইমান", "হিশাম", "ইয়াজিদ ২য়"], ["Caliph Al-Walid I", "Sulaiman", "Hisham", "Yazid II"], 0,
         "খলিফা ওয়ালিদ ৭০৫-৭১৫ খ্রিস্টাব্দে ইসলামী স্থাপত্যের এই অনন্য নিদর্শন মসজিদটি নির্মাণ করেন।", "Caliph Al-Walid I built the grand Umayyad Mosque of Damascus.", "তারিখে দামেস্ক", "Tarikh Dimashq", "EASY"),

        ("ইসলামী ইতিহাসে বিজ্ঞান, চিকিৎসাবিদ্যা ও সংস্কৃতির চরম উন্নতির এই যুগকে বৈশ্বিকভাবে কী নামে অভিহিত করা হয়?", "What is the remarkable multi-century era of Islamic intellectual ascendancy globally titled?",
         ["ইসলামের স্বর্ণযুগ (The Islamic Golden Age)", "অন্ধকার যুগ", "ব্রোঞ্জ যুগ", "লৌহ যুগ"], ["The Islamic Golden Age (8th - 14th Century CE)", "Dark Ages", "Bronze Age", "Iron Age"], 0,
         "৮ম থেকে ১৪শ শতাব্দী পর্যন্ত মুসলিম বিশ্ব দর্শন, বিজ্ঞান, চিকিৎসা, জ্যোতির্বিদ্যা ও গণিতে মানবজাতিকে নেতৃত্ব দিয়েছে।", "The Islamic Golden Age brought groundbreaking scientific advances that sparked the European Renaissance.", "ইউনেস্কো বিজ্ঞান ইতিহাস", "UNESCO Science History", "EASY")
    ]
    return [('Q_HIS_' + str(i+1).zfill(3), 'islamic_history', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_batch_7_to_12():
    q = []
    q.extend(get_cat_07_seerat())
    q.extend(get_cat_08_sahaba())
    q.extend(get_cat_09_jannah())
    q.extend(get_cat_10_jahannam())
    q.extend(get_cat_11_jinn())
    q.extend(get_cat_12_history())
    return q
