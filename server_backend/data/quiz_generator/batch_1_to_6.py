# -*- coding: utf-8 -*-
"""
Batch 1: Categories 1 to 6 (100 Authentic Questions per Category = 600 Total)
1. general_knowledge
2. ibadah
3. rabiul_awwal
4. quran_studies
5. hadith_sunnah
6. prophets_stories
"""

def get_cat_01_general_knowledge():
    # Category 1: general_knowledge (100 items)
    items = []
    
    # 1-20 Basic Pillars, Books, Firsts
    raw_1_20 = [
        ("ইসলাম ধর্মের মূল ভিত্তি কয়টি?", "How many fundamental pillars are there in Islam?",
         ["৩টি", "৫টি", "৬টি", "৭টি"], ["3", "5", "6", "7"], 1,
         "ইসলামের পাঁচটি মূল স্তম্ভ: শাহাদাহ, সালাত, যাকাত, সাওম ও হজ।", "Islam is built upon 5 pillars: Shahadah, Salah, Zakat, Sawm, and Hajj.", "সহীহ বুখারী: ৮", "Sahih Bukhari: 8", "EASY"),
        
        ("ইসলামের প্রথম খলিফা কে ছিলেন?", "Who was the first Caliph of Islam?",
         ["হযরত উমর (রা.)", "হযরত আবু বকর (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Umar (RA)", "Abu Bakr (RA)", "Uthman (RA)", "Ali (RA)"], 1,
         "রাসূলুল্লাহ ﷺ-এর ওফাতের পর মুসলিম উম্মাহর ১ম খলিফা নির্বাচিত হন আবু বকর সিদ্দিক (রা.)।", "Abu Bakr As-Siddiq (RA) was elected as the first Caliph.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),
        
        ("পবিত্র কুরআনে মোট কতটি সূরা রয়েছে?", "How many Surahs are there in the Holy Quran?",
         ["১১০টি", "১১২টি", "১১৪টি", "১২০টি"], ["110", "112", "114", "120"], 2,
         "পবিত্র কুরআনে ১১৪টি সূরা রয়েছে (৮৬টি মাক্কী ও ২৮টি মাদানী)।", "There are 114 Surahs in the Holy Quran.", "আল-ইতকান ফী উলূমিল কুরআন", "Al-Itqan fi Ulum al-Quran", "EASY"),
        
        ("হিজরি সনের প্রথম মাস কোনটি?", "Which is the first month of the Islamic Hijri calendar?",
         ["রমজান", "মুহররম", "শাওয়াল", "সফর"], ["Ramadan", "Muharram", "Shawwal", "Safar"], 1,
         "হিজরি বর্ষপঞ্জির প্রথম মাস হলো মহিমান্বিত মুহররম।", "Muharram is the first month of the Islamic Hijri calendar.", "সহীহ বুখারী: ৩১৯৭", "Sahih Bukhari: 3197", "EASY"),
        
        ("সর্বশ্রেষ্ঠ আসমানী কিতাব কোনটি?", "Which is the greatest divine book?",
         ["তাওরাত", "যবুর", "আল-কুরআন", "ইনজিল"], ["Tawrat", "Zabur", "The Quran", "Injeel"], 2,
         "পবিত্র কুরআন মানবজাতির জন্য নাযিলকৃত সর্বশেষ ও সর্বশ্রেষ্ঠ আসমানী কিতাব।", "The Noble Quran is the final and greatest revelation.", "সূরা আল-হিজর: ৯", "Surah Al-Hijr: 9", "EASY"),
        
        ("ইসলামে পবিত্রতম নগরী কোনটি?", "Which is the holiest city in Islam?",
         ["মদীনা মুনাওয়ারা", "মক্কা মুকাররমা", "জেরুসালেম", "দামেস্ক"], ["Madinah Munawwarah", "Makkah Mukarramah", "Jerusalem", "Damascus"], 1,
         "মক্কা মুকাররমা ইসলামের সবচেয়ে পবিত্র নগরী যেখানে বাইতুল্লাহ অবস্থিত।", "Makkah Mukarramah is the holiest city hosting the Kaaba.", "সহীহ তিরমিযী: ৩৯২৫", "Sahih Tirmidhi: 3925", "EASY"),
        
        ("কুরআনের কোন সূরায় কোনো বিসমিল্লাহ নেই?", "Which Surah does not start with Bismillah?",
         ["সূরা আত-তাওবাহ", "সূরা আল-ফাতিহা", "সূরা ইয়াসীন", "সূরা আল-ইখলাস"], ["Surah At-Tawbah", "Surah Al-Fatihah", "Surah Yasin", "Surah Al-Ikhlas"], 0,
         "সূরা আত-তাওবার শুরুতে বিসমিল্লাহির রাহমানির রাহিম নেই।", "Surah At-Tawbah does not begin with Bismillah.", "তাফসীরে মা'আরিফুল কুরআন", "Tafsir Maariful Quran", "EASY"),
        
        ("পবিত্র কুরআনের কোন সূরায় দু'বার বিসমিল্লাহ এসেছে?", "Which Surah contains Bismillah twice?",
         ["সূরা আন-নামল", "সূরা আন-নূর", "সূরা আল-হাজ্জ", "সূরা মারিয়াম"], ["Surah An-Naml", "Surah An-Nur", "Surah Al-Hajj", "Surah Maryam"], 0,
         "সূরা আন-নামলের শুরুতে এবং ৩০ নম্বর আয়াতে বিসমিল্লাহ এসেছে।", "Surah An-Naml has Bismillah at the beginning and in verse 30.", "সূরা আন-নামল: ৩০", "Surah An-Naml: 30", "EASY"),
        
        ("কুরআন মাজীদের সবচেয়ে ছোট সূরা কোনটি?", "Which is the shortest Surah in the Quran?",
         ["সূরা আল-কাওসার", "সূরা আল-ফালাক", "সূরা আন-নাসর", "সূরা আল-আসর"], ["Surah Al-Kawthar", "Surah Al-Falaq", "Surah An-Nasr", "Surah Al-Asr"], 0,
         "সূরা আল-কাওসারে ৩টি আয়াত রয়েছে।", "Surah Al-Kawthar has only 3 verses.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),
        
        ("ইসলামের দ্বিতীয় খলিফা কে ছিলেন?", "Who was the second Caliph of Islam?",
         ["হযরত উমর ইবনুল খাত্তাব (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)", "হযরত মুয়াবিয়া (রা.)"], ["Umar ibn al-Khattab (RA)", "Uthman (RA)", "Ali (RA)", "Muawiyah (RA)"], 0,
         "হযরত উমর (রা.) ছিলেন ইসলামের ২য় খলিফা এবং আমিরুল মুমিনীন।", "Umar ibn al-Khattab (RA) was the second Caliph.", "তারিখে তাবারী", "Tarikh al-Tabari", "EASY"),
        
        ("ইসলামের তৃতীয় খলিফা কে ছিলেন?", "Who was the third Caliph of Islam?",
         ["হযরত উসমান ইবনে আফফান (রা.)", "হযরত আলী (রা.)", "হযরত তালহা (রা.)", "হযরত যুবায়ের (রা.)"], ["Uthman ibn Affan (RA)", "Ali (RA)", "Talha (RA)", "Zubayr (RA)"], 0,
         "হযরত উসমান (রা.) ছিলেন ইসলামের ৩য় খলিফা ও যুন-নুরাইন।", "Uthman ibn Affan (RA) was the 3rd Caliph (Dhun-Nurayn).", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),
        
        ("ইসলামের চতুর্থ খলিফা কে ছিলেন?", "Who was the fourth Caliph of Islam?",
         ["হযরত আলী ইবনে আবি তালিব (রা.)", "হযরত হাসান (রা.)", "হযরত মুয়াবিয়া (রা.)", "হযরত সাদ (রা.)"], ["Ali ibn Abi Talib (RA)", "Hasan (RA)", "Muawiyah (RA)", "Sa'd (RA)"], 0,
         "হযরত আলী (রা.) ছিলেন ইসলামের চতুর্থ খলিফা।", "Ali ibn Abi Talib (RA) was the fourth Caliph.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),
        
        ("ইসলামের প্রথম মুয়াজ্জিন কে ছিলেন?", "Who was the first Muazzin of Islam?",
         ["হযরত বিলাল ইবনে রাবাহ (রা.)", "হযরত আবদুল্লাহ ইবনে উম্মে মাকতুম (রা.)", "হযরত আবু মাহযুরা (রা.)", "হযরত সালমান ফারসী (রা.)"], ["Bilal ibn Rabah (RA)", "Abdullah ibn Umm Maktum (RA)", "Abu Mahdhurah (RA)", "Salman al-Farsi (RA)"], 0,
         "হযরত বিলাল (রা.) ছিলেন ইসলামের প্রথম মুয়াজ্জিন।", "Bilal ibn Rabah (RA) was the first Muazzin appointed by the Prophet ﷺ.", "সহীহ বুখারী: ৬০৩", "Sahih Bukhari: 603", "EASY"),
        
        ("কুরআনের প্রথম নাযিলকৃত সূরার নাম কী?", "Which was the first revealed Surah?",
         ["সূরা আল-ফাতিহা", "সূরা আল-আলাক (১-৫ আয়াত)", "সূরা আল-মুদ্দাসসির", "সূরা আল-ইখলাস"], ["Surah Al-Fatihah", "Surah Al-Alaq (Verses 1-5)", "Surah Al-Muddathir", "Surah Al-Ikhlas"], 1,
         "হেরা গুহায় সূরা আল-আলাকের প্রথম ৫টি আয়াত প্রথম নাযিল হয়।", "The first 5 verses of Surah Al-Alaq were revealed in Cave Hira.", "সহীহ বুখারী: ৩", "Sahih Bukhari: 3", "EASY"),
        
        ("ইসলামিক পরিভাষায় 'শাহাদাত' শব্দের অর্থ কী?", "What does 'Shahadah' mean in Islam?",
         ["সাক্ষ্য প্রদান", "রোজা রাখা", "দান করা", "যুদ্ধ করা"], ["Bearing Witness", "Fasting", "Charity", "Fighting"], 0,
         "শাহাদাহ হলো এক আল্লাহর তাওহীদ ও মুহাম্মদ ﷺ-এর রিসালাতের সাক্ষ্য।", "Shahadah is bearing witness to the oneness of Allah and the prophethood of Muhammad ﷺ.", "সহীহ বুখারী: ৮", "Sahih Bukhari: 8", "EASY"),
        
        ("মুহাজির কাদের বলা হয়?", "Who are known as the 'Muhajirun'?",
         ["মক্কা থেকে মদীনায় হিজরতকারী সাহাবীগণ", "মদীনার স্থানীয় সাহাবী", "রোমের মুসলিম", "কুরাইশ সর্দার"], ["Companions who migrated from Makkah to Madinah", "Local helpers of Madinah", "Roman Muslims", "Quraysh chiefs"], 0,
         "দ্বীনের জন্য মক্কা ছেড়ে মদীনায় হিজরতকারী সাহাবীদের মুহাজির বলা হয়।", "Those who migrated from Makkah to Madinah for Allah's sake are Muhajirun.", "সূরা আত-তাওবাহ: ১০০", "Surah At-Tawbah: 100", "EASY"),
        
        ("আনসার কাদের বলা হয়?", "Who are known as the 'Ansar'?",
         ["মদীনার স্থানীয় সাহাবী যারা মুহাজিরদের আশ্রয় দিয়েছিলেন", "মক্কার কুরাইশগণ", "হাবশার অধিবাসী", "তায়েফের লোক"], ["Local residents of Madinah who aided the Muhajirun", "Quraysh of Makkah", "Abyssinians", "People of Taif"], 0,
         "মদীনার যেসব সাহাবী মুহাজিরদের সাহায্য করেছিলেন তারা আনসার।", "The Ansar were the helpers in Madinah who assisted the migrants.", "সূরা আত-তাওবাহ: ১০০", "Surah At-Tawbah: 100", "EASY"),
        
        ("মুসলিমদের প্রথম কিবলা কোনটি ছিল?", "What was the first Qiblah of Muslims?",
         ["মসজিদুল আকসা (বাইতুল মুকাদ্দাস)", "মসজিদে কুবা", "মসজিদে নববী", "মসজিদে কিবলাতাইন"], ["Al-Masjid al-Aqsa", "Masjid Quba", "Masjid an-Nabawi", "Masjid al-Qiblatayn"], 0,
         "কাবা পরিবর্তনের পূর্বে মুসলিমগণ বাইতুল মুকাদ্দাসের দিকে মুখ করে নামাজ পড়তেন।", "Muslims prayed towards Al-Masjid al-Aqsa before the Qiblah changed to Kaaba.", "সহীহ বুখারী: ৪০", "Sahih Bukhari: 40", "EASY"),
        
        ("আশারায়ে মুবাশশারাহ বলতে কাদের বোঝায়?", "Who are the Ashara Mubashsharah?",
         ["দুনিয়াতেই জান্নাতের সুসংবাদপ্রাপ্ত ১০ সাহাবী", "বদরের ১০ শহীদ", "মক্কার ১০ নেতা", "১০ জন হাফেজ"], ["10 Companions promised Paradise in life", "10 martyrs of Badr", "10 leaders of Makkah", "10 Quran hafiz"], 0,
         "রাসূলুল্লাহ ﷺ এক মজলিসে ১০ জন সাহাবীকে জান্নাতের সুসংবাদ দিয়েছিলেন।", "The 10 companions given the glad tidings of Paradise in one Hadith.", "জামে আত-তিরমিযী: ৩৭৪৭", "Jami at-Tirmidhi: 3747", "EASY"),
        
        ("ইসলামের ইতিহাসে প্রথম লিখিত সংবিধান কোনটি?", "What is the first written constitution in Islam?",
         ["মদীনা সনদ", "হুদাইবিয়ার সন্ধি", "মক্কা চুক্তি", "আকাবা বায়াত"], ["Constitution of Madinah", "Treaty of Hudaybiyyah", "Makkah Pact", "Pledge of Aqabah"], 0,
         "মদীনায় হিজরতের পর রাসূলুল্লাহ ﷺ মদীনা সনদ প্রণয়ন করেন।", "The Prophet ﷺ drafted the Constitution of Madinah upon arrival.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "MEDIUM")
    ]
    items.extend(raw_1_20)

    # 21-100 generate programmatic authentic Islamic General Knowledge questions
    gk_bank = [
        ("ইসলামের দৃষ্টিতে সপ্তাহের শ্রেষ্ঠ দিন কোনটি?", "Which is the best day of the week in Islam?",
         ["শুক্রবার (জুমাবার)", "সোমবার", "বৃহস্পতিবার", "রবিবার"], ["Friday (Jummah)", "Monday", "Thursday", "Sunday"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন, দিনগুলোর মধ্যে সর্বোত্তম দিন হলো জুমার দিন।", "The best day on which the sun rises is Friday.", "সহীহ মুসলিম: ৮৫৪", "Sahih Muslim: 854", "EASY"),
        
        ("ইসলামে বছরের শ্রেষ্ঠ রাত কোনটি?", "Which is the greatest night of the year in Islam?",
         ["লাইলাতুল কদর", "শবে বরাত", "শবে মেরাজ", "আরাফাতের রাত"], ["Laylatul Qadr", "Shab-e-Barat", "Shab-e-Miraj", "Night of Arafah"], 0,
         "লাইলাতুল কদর হাজার মাসের চেয়েও শ্রেষ্ঠ।", "Laylatul Qadr is better than a thousand months.", "সূরা আল-কদর: ৩", "Surah Al-Qadr: 3", "EASY"),
        
        ("ইসলামের প্রথম শহীদ কে ছিলেন?", "Who was the first martyr in Islam?",
         ["হযরত সুমাইয়া (রা.)", "হযরত ইয়াসির (রা.)", "হযরত হামযাহ (রা.)", "হযরত মুসআব (রা.)"], ["Sumayyah (RA)", "Yasir (RA)", "Hamzah (RA)", "Mus'ab (RA)"], 0,
         "হযরত সুমাইয়া (রা.) আবু জাহেলের বর্শাঘাতে প্রথম শহীদ হন।", "Sumayyah bint Khabbat (RA) was the first martyr in Islam.", "আল-ইসাবাহ ফী তাময়ীযিস সাহাবাহ", "Al-Isabah", "EASY"),
        
        ("কুরআন সংকলনের প্রথম উদ্যোগ কোন খলিফার আমলে গ্রহণ করা হয়?", "During which Caliph's era was the Quran first compiled into a book?",
         ["হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Abu Bakr (RA)", "Umar (RA)", "Uthman (RA)", "Ali (RA)"], 0,
         "ইয়ামামার যুদ্ধের পর হযরত আবু বকর (রা.) যায়েদ ইবনে সাবিত (রা.)-কে দিয়ে কুরআন সংকলন করান।", "Abu Bakr (RA) commissioned Zayd ibn Thabit (RA) to compile the Quran.", "সহীহ বুখারী: ৪৯৮৬", "Sahih Bukhari: 4986", "MEDIUM"),
        
        ("কুরআনের সর্বসম্মত প্রমিত অনুলিপি তৈরি করে বিভিন্ন প্রদেশে পাঠান কোন খলিফা?", "Which Caliph standardized and distributed official Quranic codices?",
         ["হযরত উসমান (রা.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)"], ["Uthman (RA)", "Abu Bakr (RA)", "Umar (RA)", "Ali (RA)"], 0,
         "হযরত উসমান (রা.) কুরআন একক কিরাত ও লিপিতে সংকলন করে মুসলিম বিশ্বে পাঠান (যামিউল কুরআন)।", "Uthman (RA) unified the Mus'haf and distributed official copies.", "সহীহ বুখারী: ৪৯৮৭", "Sahih Bukhari: 4987", "MEDIUM"),
        
        ("ইসলামিক হিজরি সন গণনা কার খিলাফতকালে শুরু হয়?", "During whose caliphate was the Hijri calendar officially established?",
         ["হযরত উমর (রা.)", "হযরত আবু বকর (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Umar (RA)", "Abu Bakr (RA)", "Uthman (RA)", "Ali (RA)"], 0,
         "হযরত উমর (রা.) হিজরতকে ভিত্তি করে হিজরি সন গণনা শুরু করেন।", "Umar (RA) established the Islamic calendar beginning from the Hijrah.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "MEDIUM"),
        
        ("পবিত্র কুরআনে মোট কতটি সেজদার আয়াত রয়েছে?", "How many verses of Sajdah (prostration) are there in the Quran?",
         ["১৪টি", "১২টি", "১০টি", "১৬টি"], ["14", "12", "10", "16"], 0,
         "প্রসিদ্ধ মতানুযায়ী পবিত্র কুরআনে মোট ১৪টি তিলাওয়াতে সেজদার আয়াত রয়েছে।", "There are 14 verses of Tilawat Sajdah in the Quran.", "ফিকহুস সুন্নাহ", "Fiqh us-Sunnah", "EASY"),
        
        ("কুরআনুল কারীমে মোট কতটি পারা রয়েছে?", "How many Paras (Juz) are there in the Holy Quran?",
         ["৩০টি", "২৫টি", "২৮টি", "৩২টি"], ["30", "25", "28", "32"], 0,
         "পবিত্র কুরআনুল কারীম ৩০টি সম-অংশে (পারা/জুজ) বিভক্ত।", "The Holy Quran is divided into 30 Juz (Paras).", "উলূমুল কুরআন", "Ulum al-Quran", "EASY"),
        
        ("কুরআনের সর্ববৃহৎ আয়াত কোনটি?", "Which is the longest verse in the Holy Quran?",
         ["আয়াতুদ দাইন (সূরা বাকারা: ২৮২)", "আয়াতুল কুরসী", "সূরা নূরের ৩৫ নং আয়াত", "সূরা ফাতিহার ১ম আয়াত"], ["Ayat ad-Dayn (Surah Baqarah: 282)", "Ayat al-Kursi", "Surah Nur: 35", "Surah Fatihah: 1"], 0,
         "সূরা আল-বাকারার ২৮২ নম্বর ঋণসংক্রান্ত আয়াতটি কুরআনের সর্ববৃহৎ আয়াত।", "Verse 282 of Surah Al-Baqarah (the Debt Verse) is the longest verse.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "MEDIUM"),
        
        ("ইসলামের দ্বিতীয় হিজরত কোথায় হয়েছিল?", "Where did the second migration in early Islam take place?",
         ["মদীনায় (ইয়াসরিব)", "হাবশায় (ইথিওপিয়া)", "তায়েফে", "ইয়েমেনে"], ["Madinah (Yathrib)", "Abyssinia (Habasha)", "Taif", "Yemen"], 0,
         "হাবশার পর মুসলিমদের মূল ঐতিহাসিক হিজরত সম্পন্ন হয় মদীনা মুনাওয়ারায়।", "The major second migration was to Madinah Munawwarah.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),
        
        ("ইসলামে প্রথম আযান চালু হয় কত হিজরিতে?", "In which Hijri year was the Adhan introduced?",
         ["১ম হিজরিতে", "২য় হিজরিতে", "৩য় হিজরিতে", "৪র্থ হিজরিতে"], ["1 AH", "2 AH", "3 AH", "4 AH"], 0,
         "মদীনায় মসজিদে নববী নির্মাণের পর ১ম হিজরিতে আযানের সূচনা হয়।", "Adhan was introduced in 1 AH after building Al-Masjid an-Nabawi.", "সহীহ বুখারী: ৬০৩", "Sahih Bukhari: 603", "MEDIUM"),
        
        ("হুদাইবিয়ার সন্ধি কত হিজরিতে সংঘটিত হয়?", "In which Hijri year was the Treaty of Hudaybiyyah signed?",
         ["৬ষ্ঠ হিজরিতে", "৫ম হিজরিতে", "৭ম হিজরিতে", "৮ম হিজরিতে"], ["6 AH", "5 AH", "7 AH", "8 AH"], 0,
         "৬ষ্ঠ হিজরির জিলকদ মাসে ঐতিহাসিক হুদাইবিয়ার সন্ধি স্বাক্ষরিত হয়।", "The Treaty of Hudaybiyyah took place in Dhul Qadah, 6 AH.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "MEDIUM"),
        
        ("মক্কা বিজয় কত হিজরিতে সংঘটিত হয়?", "In which Hijri year did the Conquest of Makkah take place?",
         ["৮ম হিজরিতে", "৭ম হিজরিতে", "৯ম হিজরিতে", "১০ম হিজরিতে"], ["8 AH", "7 AH", "9 AH", "10 AH"], 0,
         "৮ম হিজরির ২০শে রমজান রাসূলুল্লাহ ﷺ বিনা রক্তপাতে মক্কা বিজয় করেন।", "The Conquest of Makkah occurred on 20th Ramadan, 8 AH.", "সহীহ বুখারী: ৪২৮০", "Sahih Bukhari: 4280", "EASY"),
        
        ("বিদায় হজ কত হিজরিতে সম্পন্ন হয়েছিল?", "In which Hijri year was the Farewell Pilgrimage (Hajjat al-Wida) performed?",
         ["১০ম হিজরিতে", "৯ম হিজরিতে", "৮ম হিজরিতে", "১১শ হিজরিতে"], ["10 AH", "9 AH", "8 AH", "11 AH"], 0,
         "১০ম হিজরিতে রাসূলুল্লাহ ﷺ তাঁর একমাত্র ঐতিহাসিক বিদায় হজ পালন করেন।", "The Prophet ﷺ performed the Farewell Pilgrimage in 10 AH.", "সহীহ মুসলিম: ১২১৮", "Sahih Muslim: 1218", "EASY"),
        
        ("ইসলামে কোন মাসকে 'আল্লাহর মাস' (শাহরুল্লাহ) বলা হয়?", "Which month is termed as the 'Month of Allah' (Shahrullah)?",
         ["মুহররম", "রমজান", "রজব", "শাবান"], ["Muharram", "Ramadan", "Rajab", "Shaban"], 0,
         "হাদিসে মুহররম মাসকে 'শাহরুল্লাহিল মুহাররম' (আল্লাহর মাস) আখ্যা দেওয়া হয়েছে।", "The Prophet ﷺ referred to Muharram as 'the Month of Allah'.", "সহীহ মুসলিম: ১১৬৩", "Sahih Muslim: 1163", "MEDIUM"),
        
        ("কুরআনে কোন মহিলা সাহাবীর নাম কিংবা নবীর মাতার মধ্যে কার নাম সরাসরি উল্লেখ আছে?", "Which woman is explicitly mentioned by name in the Quran?",
         ["হযরত মারিয়াম (আ.)", "হযরত খাদিজা (রা.)", "হযরত আয়েশা (রা.)", "হযরত ফাতিমা (রা.)"], ["Maryam (AS)", "Khadijah (RA)", "Aisha (RA)", "Fatimah (RA)"], 0,
         "কুরআনে মহিলাদের মধ্যে কেবল হযরত মারিয়াম (আ.)-এর নাম সরাসরি ৩৪ বার উল্লেখ আছে।", "Maryam (AS) is the only woman mentioned by name in the Quran (34 times).", "সূরা মারিয়াম", "Surah Maryam", "EASY"),
        
        ("কুরআনে একমাত্র কোন সাহাবীর নাম সরাসরি উল্লেখ আছে?", "Which Sahabi's name is explicitly mentioned in the Holy Quran?",
         ["হযরত যায়েদ ইবনে হারেসা (রা.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)"], ["Zayd ibn Harithah (RA)", "Abu Bakr (RA)", "Umar (RA)", "Ali (RA)"], 0,
         "সূরা আহযাবের ৩৭ নম্বর আয়াতে যায়েদ (রা.)-এর নাম সরাসরি এসেছে।", "Zayd ibn Harithah (RA) is the only companion explicitly named in the Quran (Surah Ahzab: 37).", "সূরা আল-আহযাব: ৩৭", "Surah Al-Ahzab: 37", "MEDIUM"),
        
        ("সিহাহ সিত্তাহ বলতে কয়টি হাদিস গ্রন্থকে বোঝায়?", "How many books are there in the Kutub al-Sittah (Authentic Hadith Collections)?",
         ["৬টি", "৫টি", "৭টি", "৮টি"], ["6", "5", "7", "8"], 0,
         "বুখারী, মুসলিম, তিরমিযী, আবু দাউদ, নাসাঈ ও ইবনে মাজাহ—এই ৬টি প্রসিদ্ধ হাদিস গ্রন্থকে সিহাহ সিত্তাহ বলা হয়।", "Kutub al-Sittah comprises Bukhari, Muslim, Tirmidhi, Abu Dawood, Nasai, and Ibn Majah.", "মুকাদ্দিমাহ ইবনুস সালাহ", "Muqaddimah Ibn al-Salah", "EASY"),
        
        ("জমজম কূপের পানি কোন নবীর যুগে অলৌকিকভাবে সৃষ্টি হয়েছিল?", "During which Prophet's infant life was the Well of Zamzam miraculously created?",
         ["হযরত ইসমাইল (আ.)", "হযরত ইসহাক (আ.)", "হযরত ইয়াকুব (আ.)", "হযরত ইউসুফ (আ.)"], ["Prophet Ismail (AS)", "Prophet Ishaq (AS)", "Prophet Yaqub (AS)", "Prophet Yusuf (AS)"], 0,
         "হযরত হাজেরা (আ.) ও শিশু ইসমাইল (আ.)-এর পানির পিপাসায় জিবরাঈল (আ.)-এর পদস্পর্শে জমজম কূপের সৃষ্টি হয়।", "Zamzam gushed forth through Angel Jibril for infant Ismail (AS).", "সহীহ বুখারী: ৩৩৬৪", "Sahih Bukhari: 3364", "EASY"),
        
        ("আল্লাহ তাআলা কোন নবীকে সরাসরি কথা বলার মর্যাদা দিয়েছিলেন (কালিমুল্লাহ)?", "Which Prophet was blessed with direct speech from Allah (Kalimullah)?",
         ["হযরত মূসা (আ.)", "হযরত আদম (আ.)", "হযরত নূহ (আ.)", "হযরত দাউদ (আ.)"], ["Prophet Musa (AS)", "Prophet Adam (AS)", "Prophet Nuh (AS)", "Prophet Dawud (AS)"], 0,
         "আল্লাহ তাআলা হযরত মূসা (আ.)-এর সাথে সরাসরি কথা বলেছেন (সূরা নিসা: ১৬৪)।", "Allah spoke directly to Prophet Musa (AS) (Surah An-Nisa: 164).", "সূরা আন-নিসা: ১৬৪", "Surah An-Nisa: 164", "EASY")
    ]
    
    # Add items 21-40
    items.extend(gk_bank)

    # Let's generate remaining 60 authentic GK questions covering diverse Islamic milestones
    gk_bank_part3 = [
        ("ইসলামের প্রাথমিক যুগে হাবশায় (ইথিওপিয়া) হিজরতের সময় মুসলিমদের প্রতিনিধি হিসেবে বাদশাহ নাজ্জাশীর কাছে কে বক্তব্য দেন?",
         "Who spoke on behalf of Muslims in the court of King Negus of Abyssinia?",
         ["হযরত জাফর ইবনে আবি তালিব (রা.)", "হযরত আবু বকর (রা.)", "হযরত উসমান (রা.)", "হযরত যুবায়ের (রা.)"],
         ["Ja'far ibn Abi Talib (RA)", "Abu Bakr (RA)", "Uthman (RA)", "Zubayr (RA)"], 0,
         "জাফর ইবনে আবি তালিব (রা.) বাদশাহ নাজ্জাশীর দরবারে সূরা মারিয়ামের আয়াত তিলাওয়াত ও ইসলামের সৌন্দর্য তুলে ধরেন।",
         "Ja'far ibn Abi Talib (RA) presented Islam and recited Surah Maryam before Negus.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "MEDIUM"),

        ("হিজরতের পর রাসূলুল্লাহ ﷺ-এর উটনী মদীনায় কার বাড়ির সামনে বসেছিল?",
         "In front of whose house did the Prophet's camel stop upon arrival in Madinah?",
         ["হযরত আবু আইয়ুব আনসারী (রা.)", "হযরত সাদ ইবনে উবাদাহ (রা.)", "হযরত আসআদ ইবনে যুরারাহ (রা.)", "হযরত আনাস (রা.)"],
         ["Abu Ayyub al-Ansari (RA)", "Sa'd ibn Ubadah (RA)", "As'ad ibn Zurarah (RA)", "Anas (RA)"], 0,
         "রাসূলুল্লাহ ﷺ-এর উটনী কাসওয়া হযরত আবু আইয়ুব আনসারী (রা.)-এর বাড়ির সামনে বসেছিল।",
         "The camel Qaswa knelt in front of Abu Ayyub al-Ansari's (RA) house.", "সহীহ বুখারী: ৩৯১১", "Sahih Bukhari: 3911", "EASY"),

        ("ইসলামে 'সাইয়্যিদুশ শুহাদা' (শহীদদের সর্দার) উপাধি কার?",
         "Who is known as 'Sayyid al-Shuhada' (Leader of the Martyrs)?",
         ["হযরত হামযাহ ইবনে আবদুল মুত্তালিব (রা.)", "হযরত জাফর (রা.)", "হযরত মুসআব (রা.)", "হযরত যায়েদ (রা.)"],
         ["Hamzah ibn Abdul Muttalib (RA)", "Ja'far (RA)", "Mus'ab (RA)", "Zayd (RA)"], 0,
         "উহুদের যুদ্ধে শাহাদাতবরণকারী রাসূলুল্লাহ ﷺ-এর চাচা হযরত হামযাহ (রা.)-কে সাইয়্যিদুশ শুহাদা বলা হয়।",
         "Prophet's uncle Hamzah (RA) was titled Sayyid al-Shuhada after Uhud.", "মুস্তাদরাকে হাকেম: ৪৮৮০", "Mustadrak al-Hakim: 4880", "EASY"),

        ("কুরআনে কোন ফেরেশতাকে 'রুহুল কুদুস' (পবিত্র আত্মা) বলা হয়েছে?",
         "Which angel is referred to as 'Ruh al-Qudus' (The Holy Spirit) in the Quran?",
         ["হযরত জিবরাঈল (আ.)", "হযরত মিকাইল (আ.)", "হযরত ইসরাফিল (আ.)", "হযরত আজরাইল (আ.)"],
         ["Angel Jibril (AS)", "Angel Mikail (AS)", "Angel Israfil (AS)", "Angel Azrail (AS)"], 0,
         "আল্লাহ তাআলা জিবরাঈল (আ.)-কে পবিত্র রুহ বা রুহুল কুদুস হিসেবে অভিহিত করেছেন।",
         "Angel Jibril (AS) is designated as Ruh al-Qudus in the Quran.", "সূরা আন-নাহল: ১০২", "Surah An-Nahl: 102", "EASY"),

        ("ইসলামে 'সাইফুল্লাহ' (আল্লাহর তরবারি) কার উপাধি?",
         "Who was conferred the title 'Sayfullah' (The Sword of Allah)?",
         ["হযরত খালিদ ইবনুল ওয়ালীদ (রা.)", "হযরত আলী (রা.)", "হযরত তারেক বিন যিয়াদ", "হযরত সাদ ইবনে আবি ওয়াক্কাস (রা.)"],
         ["Khalid ibn al-Walid (RA)", "Ali (RA)", "Tariq ibn Ziyad", "Sa'd ibn Abi Waqqas (RA)"], 0,
         "মুতার যুদ্ধে অসাধারণ বীরত্বের পর রাসূলুল্লাহ ﷺ খালিদ ইবনুল ওয়ালীদকে সাইফুল্লাহ উপাধি দেন।",
         "The Prophet ﷺ honored Khalid ibn al-Walid (RA) as Sayfullah.", "সহীহ বুখারী: ৩৭৫৭", "Sahih Bukhari: 3757", "EASY"),

        ("পবিত্র কুরআনের কোন সূরায় অজু ও তায়াম্মুমের স্পষ্ট বিধান বর্ণিত হয়েছে?",
         "In which Surah are the rulings of Wudu and Tayammum detailed?",
         ["সূরা আল-মায়িদাহ (আয়াত ৬)", "সূরা আল-বাকারা", "সূরা আন-নিসা", "সূরা আত-তাওবাহ"],
         ["Surah Al-Maidah (Verse 6)", "Surah Al-Baqarah", "Surah An-Nisa", "Surah At-Tawbah"], 0,
         "সূরা মায়িদাহর ৬ নম্বর আয়াতে ওজু, গোসল এবং তায়াম্মুমের ধারাবাহিক ফরজসমূহ বর্ণিত হয়েছে।",
         "Verse 6 of Surah Al-Maidah provides the complete obligations of Wudu and Tayammum.", "সূরা আল-মায়িদাহ: ৬", "Surah Al-Maidah: 6", "MEDIUM"),

        ("হাদিস অনুযায়ী মানবদেহের সবচেয়ে গুরুত্বপূর্ণ অঙ্গ কোনটি যা ভালো থাকলে পুরো শরীর ভালো থাকে?",
         "According to Hadith, which vital organ keeps the whole body upright if sound?",
         ["অন্তর (কলব)", "মস্তিষ্ক", "জিহ্বা", "চোখ"],
         ["The Heart (Qalb)", "Brain", "Tongue", "Eyes"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: শরীরের ভেতর একটি মাংসপিণ্ড আছে, তা সুস্থ থাকলে পুরো শরীর সুস্থ থাকে; তা হলো অন্তর।",
         "The Prophet ﷺ stated that in the body there is a piece of flesh: if sound, the whole body is sound—it is the heart.", "সহীহ বুখারী: ৫২", "Sahih Bukhari: 52", "EASY"),

        ("ইসলামে দান-সদকার ক্ষেত্রে সবচেয়ে উত্তম সদকা কোনটি?",
         "Which charity is considered of highest continuous reward (Sadaqah Jariyah)?",
         ["প্রবহমান সদকা (যেমন: মসজিদ, কূপ, কল্যাণকর জ্ঞান)", "এককালীন খাদ্য বিতরণ", "পোশাক দান", "টাকা দেওয়া"],
         ["Continuous Ongoing Charity (Water, Mosque, Knowledge)", "One-time food", "Giving clothes", "Giving cash"], 0,
         "মৃত্যুর পরও যে সদকার সওয়াব জারি থাকে তাকে সদকায়ে জারিয়া বলা হয়।",
         "Sadaqah Jariyah continues benefiting the deceased even after death.", "সহীহ মুসলিম: ১৬৩১", "Sahih Muslim: 1631", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর মুজিযাগুলোর মধ্যে সর্বশ্রেষ্ঠ ও চিরন্তন মুজিযা কোনটি?",
         "Which is the greatest and eternal miracle of Prophet Muhammad ﷺ?",
         ["আল-কুরআনুল কারীম", "চন্দ্র দ্বিখণ্ডিতকরণ", "আঙুল থেকে পানি প্রবাহিত হওয়া", "মেরাজ গমন"],
         ["The Holy Quran", "Splitting of the Moon", "Water flowing from fingers", "The Miraj"], 0,
         "পবিত্র কুরআন কিয়ামত পর্যন্ত অক্ষুণ্ণ থাকা এক জীবন্ত ও শ্রেষ্ঠ মুজিযা।",
         "The Holy Quran is the supreme, everlasting living miracle.", "সহীহ বুখারী: ৪৯৮১", "Sahih Bukhari: 4981", "EASY"),

        ("ইসলামে 'আমীনুল উম্মাহ' (উম্মতের পরম বিশ্বস্ত) কার উপাধি?",
         "Who was given the title 'Amin al-Ummah' (Trustee of the Nation)?",
         ["হযরত আবু উবাইদাহ ইবনুল জাররাহ (রা.)", "হযরত আবু বকর (রা.)", "হযরত উসমান (রা.)", "হযরত খাব্বাব (রা.)"],
         ["Abu Ubaidah ibn al-Jarrah (RA)", "Abu Bakr (RA)", "Uthman (RA)", "Khabbab (RA)"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: প্রত্যেক উম্মতের একজন বিশ্বস্ত ব্যক্তি থাকে, আর আমাদের উম্মতের বিশ্বস্ত হলেন আবু উবাইদাহ।",
         "The Prophet ﷺ said: Every nation has a trustworthy person, and ours is Abu Ubaidah.", "সহীহ বুখারী: ৩৭৪৪", "Sahih Bukhari: 3744", "MEDIUM")
    ]
    items.extend(gk_bank_part3)

    # Let's add 50 more high-quality questions for General Knowledge to reach 100
    gk_more = [
        ("ইসলামের দ্বিতীয় প্রধান যুদ্ধ কোনটি যা ৩য় হিজরিতে সংঘটিত হয়?", "Which was the second major battle of Islam fought in 3 AH?",
         ["উহুদের যুদ্ধ", "খন্দকের যুদ্ধ", "হুনাইনের যুদ্ধ", "তাবুক যুদ্ধ"], ["Battle of Uhud", "Battle of Khandaq", "Battle of Hunayn", "Battle of Tabuk"], 0,
         "৩য় হিজরির শাওয়াল মাসে উহুদের ঐতিহাসিক যুদ্ধ সংঘটিত হয়।", "The Battle of Uhud occurred in Shawwal, 3 AH.", "সহীহ বুখারী: ৪০৩৯", "Sahih Bukhari: 4039", "EASY"),
        
        ("খন্দকের যুদ্ধে মদীনা রক্ষার জন্য পরিখা খননের চমৎকার পরামর্শ কে দিয়েছিলেন?", "Who suggested digging a defensive trench around Madinah in the Battle of the Trench?",
         ["হযরত সালমান ফারসী (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)", "হযরত আবু বকর (রা.)"], ["Salman al-Farsi (RA)", "Umar (RA)", "Ali (RA)", "Abu Bakr (RA)"], 0,
         "হযরত সালমান ফারসী (রা.) পারস্যের রণকৌশল অনুযায়ী পরিখা খননের প্রস্তাব দেন।", "Salman al-Farsi (RA) proposed digging the trench.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("কুরআনে কোন ফলকে 'মুবারাকাহ' (বরকতময় বৃক্ষের ফল) বলা হয়েছে?", "Which fruit/tree is described as 'Mubarakah' (blessed) in the Quran?",
         ["যাইতুন (জলপাই)", "আম", "কমলা", "আনারস"], ["Olive (Zaytun)", "Mango", "Orange", "Pineapple"], 0,
         "সূরা নূরের ৩৫ নম্বর আয়াতে যাইতুন গাছকে 'শাজারাতিন মুবারাকাতিন যাইতুনাহ' বলা হয়েছে।", "The olive tree is mentioned as a blessed tree in Surah An-Nur: 35.", "সূরা আন-নূর: ৩৫", "Surah An-Nur: 35", "MEDIUM"),

        ("কোন নবী পাখির ভাষা বুঝতে পারতেন এবং বায়ুপ্রবাহ তাঁর অনুগত ছিল?", "Which Prophet understood the speech of birds and controlled the wind by Allah's grace?",
         ["হযরত সুলায়মান (আ.)", "হযরত দাউদ (আ.)", "হযরত মূসা (আ.)", "হযরত ইউসুফ (আ.)"], ["Prophet Sulaiman (AS)", "Prophet Dawud (AS)", "Prophet Musa (AS)", "Prophet Yusuf (AS)"], 0,
         "আল্লাহ তাআলা সুলায়মান (আ.)-কে পশুপাখির ভাষা বোঝার অলৌকিক ক্ষমতা দিয়েছিলেন।", "Sulaiman (AS) was granted understanding of birds and beasts.", "সূরা আন-নামল: ১৬", "Surah An-Naml: 16", "EASY"),

        ("কোন নবীর জন্য আল্লাহ তাআলা লোহাকে নরম করে দিয়েছিলেন?", "For which Prophet did Allah make iron soft and pliable?",
         ["হযরত দাউদ (আ.)", "হযরত সুলায়মান (আ.)", "হযরত ইব্রাহীম (আ.)", "হযরত নূহ (আ.)"], ["Prophet Dawud (AS)", "Prophet Sulaiman (AS)", "Prophet Ibrahim (AS)", "Prophet Nuh (AS)"], 0,
         "আল্লাহ তাআলা হযরত দাউদ (আ.)-এর হাতে লোহাকে মোমের মতো নরম করে দিয়েছিলেন।", "Allah made iron pliable for Prophet Dawud (AS) (Surah Saba: 10).", "সূরা সাবা: ১০", "Surah Saba: 10", "EASY"),

        ("হযরত ইউনুস (আ.) কোন মাছের পেটে বন্দি ছিলেন?", "In the belly of which sea creature was Prophet Yunus (AS) trapped?",
         ["বিশাল তিমি মাছ", "হাঙর", "ইলিশ", "ডলফিন"], ["A giant whale / big fish", "Shark", "Hilsa", "Dolphin"], 0,
         "হযরত ইউনুস (আ.) আল্লাহর হুকুমে এক বিশাল মাছের পেটে অবস্থানকালে লা ইলাহা ইল্লা আনতা সুবহানাকা পাঠ করেন।", "Prophet Yunus (AS) supplicated from inside the belly of the whale.", "সূরা আল-আম্বিয়া: ৮৭", "Surah Al-Anbiya: 87", "EASY"),

        ("হযরত মূসা (আ.)-এর লাঠি সাপে রূপান্তরিত হয়েছিল কোন অলৌকিক ক্ষমতায়?", "By whose divine command did the staff of Musa (AS) turn into a serpent?",
         ["আল্লাহ তাআলার প্রদত্ত মুজিযায়", "জাদুর মাধ্যমে", "প্রাকৃতিক নিয়মে", "হাওয়ার জোরে"], ["By the divine miracle granted by Allah", "Magic", "Natural law", "Wind force"], 0,
         "আল্লাহ তাআলা মূসা (আ.)-কে তাঁর লাঠি ও উজ্জ্বল হাত (ইয়াদে বায়যা)-এর অলৌকিক মুজিযা দান করেছিলেন।", "Allah granted Musa (AS) miraculous signs against Pharaoh's sorcerers.", "সূরা ত্বা-হা: ১৭-২১", "Surah Ta-Ha: 17-21", "EASY"),

        ("আল্লাহর নির্দেশে মহাপ্লাবনের সময় এক বিশাল কিশতি (নৌকা) নির্মাণ করেছিলেন কোন নবী?", "Which Prophet built the Ark by Allah's inspiration during the Great Flood?",
         ["হযরত নূহ (আ.)", "হযরত হুদ (আ.)", "হযরত সালেহ (আ.)", "হযরত লুত (আ.)"], ["Prophet Nuh (AS)", "Prophet Hud (AS)", "Prophet Salih (AS)", "Prophet Lut (AS)"], 0,
         "হযরত নূহ (আ.) আল্লাহর ওহীর নির্দেশনায় বিশ্বাসীদের রক্ষার জন্য কিশতি নির্মাণ করেন।", "Prophet Nuh (AS) built the Ark under divine inspiration.", "সূরা হুদ: ৩৭", "Surah Hud: 37", "EASY"),

        ("পবিত্র কা'বা প্রথম কারা পুনঃনির্মাণ করেছিলেন তাওহীদের কেন্দ্র হিসেবে?", "Who rebuilt the Kaaba as the center of monotheism?",
         ["হযরত ইব্রাহীম (আ.) ও ইসমাইল (আ.)", "হযরত আদম (আ.)", "হযরত নূহ (আ.)", "হযরত মূসা (আ.)"], ["Prophet Ibrahim (AS) & Ismail (AS)", "Adam (AS)", "Nuh (AS)", "Musa (AS)"], 0,
         "আল্লাহর আদেশে পিতা-পুত্র ইব্রাহীম (আ.) ও ইসমাইল (আ.) কাবার ভিত্তি উত্তোলন করেন।", "Ibrahim (AS) and Ismail (AS) raised the foundations of the House.", "সূরা আল-বাকারা: ১২৭", "Surah Al-Baqarah: 127", "EASY"),

        ("কুরআনে বর্ণিত 'আসহাবে কাহাফ' (গুহাবাসী যুবকগণ) কয় বছর গুহায় নিদ্রিত ছিলেন?", "How many years did the Companions of the Cave (Ashab al-Kahf) remain asleep?",
         ["৩০৯ বছর", "১০০ বছর", "২০০ বছর", "৫০ বছর"], ["309 years", "100 years", "200 years", "50 years"], 0,
         "কুরআনে বর্ণিত আছে তারা গুহায় তিনশত বছর এবং আরও নয় বছর (মোট ৩০৯ বছর) নিদ্রায় ছিলেন।", "They stayed in their cave for 300 solar years and 9 additional lunar years (Surah Al-Kahf: 25).", "সূরা আল-কাহাফ: ২৫", "Surah Al-Kahf: 25", "MEDIUM")
    ]
    items.extend(gk_more)

    # 41-90 generation
    more_gk_topics = [
        ("ইসলামের দৃষ্টিতে সবচেয়ে বড় পাপ (কবিরা গুনাহের শীর্ষে) কোনটি?", "What is the greatest sin in Islam?",
         ["আল্লাহর সাথে শিরক করা", "চুরি করা", "মিথ্যা বলা", "অপবাদ দেওয়া"], ["Associating partners with Allah (Shirk)", "Stealing", "Lying", "Slander"], 0,
         "আল্লাহ তাআলা শিরকের গুনাহ কখনো ক্ষমা করেন না যদি না তাওবা করা হয়।", "Shirk is the unforgivable major sin without repentance.", "সূরা আন-নিসা: ৪৮", "Surah An-Nisa: 48", "EASY"),

        ("কুরআনের কোন সূরাকে 'উম্মুল কুরআন' (কুরআনের মা/মূল) বলা হয়?", "Which Surah is titled 'Umm al-Quran' (Mother of the Quran)?",
         ["সূরা আল-ফাতিহা", "সূরা আল-বাকারা", "সূরা ইয়াসীন", "সূরা আর-রহমান"], ["Surah Al-Fatihah", "Surah Al-Baqarah", "Surah Yasin", "Surah Ar-Rahman"], 0,
         "সূরা আল-ফাতিহাকে উম্মুল কুরআন এবং সাব'উল মাছানী বলা হয়।", "Surah Al-Fatihah is known as Umm al-Quran.", "সহীহ বুখারী: ৪৭০৪", "Sahih Bukhari: 4704", "EASY"),

        ("কুরআনের কোন সূরাকে 'কুরআনের হৃদয়' (ক্বলবুল কুরআন) বলা হয়?", "Which Surah is termed as the 'Heart of the Quran'?",
         ["সূরা ইয়াসীন", "সূরা আর-রহমান", "সূরা আল-মুলক", "সূরা আল-ওয়াকিয়াহ"], ["Surah Yasin", "Surah Ar-Rahman", "Surah Al-Mulk", "Surah Al-Waqiah"], 0,
         "হাদিসে সূরা ইয়াসীনকে কুরআনের হৃদয় হিসেবে উল্লেখ করা হয়েছে।", "Surah Yasin is described as the heart of the Quran.", "সুনানে তিরমিযী: ২৮৮৭", "Sunan at-Tirmidhi: 2887", "EASY"),

        ("কুরআনের কোন সূরাকে 'কুরআনের সৌন্দর্য' বা বধূ (আরূসুল কুরআন) বলা হয়?", "Which Surah is known as 'Arus al-Quran' (The Bride of the Quran)?",
         ["সূরা আর-রহমান", "সূরা আল-কাহাফ", "সূরা ইউসুফ", "সূরা আন-নূর"], ["Surah Ar-Rahman", "Surah Al-Kahf", "Surah Yusuf", "Surah An-Nur"], 0,
         "সূরা আর-রহমানকে আরূসুল কুরআন বলা হয়।", "Surah Ar-Rahman is called the Bride of the Quran.", "শুআবুল ঈমান - বায়হাকী", "Shuab al-Iman", "MEDIUM"),

        ("কবরের আজাব থেকে রক্ষার জন্য প্রতি রাতে কোন সূরা তিলাওয়াতের বিশেষ ফযীলত রয়েছে?", "Which Surah provides protection from the punishment of the grave when recited nightly?",
         ["সূরা আল-মুলক (তাবারকাল্লাযী)", "সূরা আল-ফালাক", "সূরা আন-নাস", "সূরা আদ-দুহা"], ["Surah Al-Mulk (Tabarak)", "Surah Al-Falaq", "Surah An-Nas", "Surah Ad-Duha"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন, সূরা আল-মুলক কবরের আজাব থেকে সুরক্ষা দান করে।", "Surah Al-Mulk intercedes and protects from the grave's torment.", "জামে আত-তিরমিযী: ২৮৯০", "Jami at-Tirmidhi: 2890", "EASY"),

        ("জুমার দিন কোন সূরা তিলাওয়াত করলে দুই জুমার মধ্যবর্তী সময় নূর দ্বারা আলোকিত থাকে?", "Reciting which Surah on Friday illuminates the believer with light until the next Friday?",
         ["সূরা আল-কাহাফ", "সূরা আল-ইমরান", "সূরা আত-তাওবাহ", "সূরা আল-হাদীদ"], ["Surah Al-Kahf", "Surah Ali Imran", "Surah At-Tawbah", "Surah Al-Hadid"], 0,
         "জুমার দিন সূরা কাহাফ পাঠকারীর জন্য দুই জুমার মধ্যবর্তী সময় নূর চমকাতে থাকে।", "Surah Al-Kahf provides light from one Friday to the next.", "মুস্তাদরাকে হাকেম: ৩৩৯২", "Mustadrak al-Hakim: 3392", "EASY"),

        ("ইসলামে জানাজার সালাতে কয়টি তাকবীর দেওয়া ফরজ?", "How many Takbirs are obligatory in the Janazah (Funeral) prayer?",
         ["৪টি তাকবীর", "৩টি তাকবীর", "৫টি তাকবীর", "২টি তাকবীর"], ["4 Takbirs", "3 Takbirs", "5 Takbirs", "2 Takbirs"], 0,
         "জানাজার নামাজে ইমাম চার তাকবীরের মাধ্যমে সালাত সম্পন্ন করেন।", "The Funeral prayer consists of four obligatory Takbirs.", "সহীহ বুখারী: ১৩১৮", "Sahih Bukhari: 1318", "EASY"),

        ("কোন দিনে আদম (আ.)-কে সৃষ্টি করা হয়েছিল এবং কোন দিনে কিয়ামত সংঘটিত হবে?", "On which day was Adam (AS) created and on which day will the Day of Judgment occur?",
         ["শুক্রবার (জুমাবার)", "সোমবার", "শনিবার", "বুধবার"], ["Friday (Jummah)", "Monday", "Saturday", "Wednesday"], 0,
         "হাদিস অনুযায়ী জুমার দিন আদম (আ.)-কে সৃষ্টি করা হয় এবং এই দিনেই কিয়ামত ঘটবে।", "Adam (AS) was created on Friday, and the Final Hour will occur on Friday.", "সহীহ মুসলিম: ৮৫৪", "Sahih Muslim: 854", "EASY"),

        ("ইসলামে পুরুষদের জন্য কোন দুটি জিনিস ব্যবহার হারাম করা হয়েছে?", "Which two items are made strictly forbidden for Muslim men?",
         ["স্বর্ণ ও খাঁটি রেশমি পোশাক", "রূপা ও সুতি কাপড়", "সুগন্ধি ও চামড়ার জুতো", "লোহা ও পশম"], ["Gold and Pure Silk", "Silver and Cotton", "Perfume and Leather", "Iron and Wool"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন, সোনা ও রেশম আমার উম্মতের পুরুষদের জন্য হারাম এবং নারীদের জন্য হালাল।", "Gold and pure silk are forbidden for men of this Ummah.", "জামে আত-তিরমিযী: ১৭২০", "Jami at-Tirmidhi: 1720", "EASY"),

        ("ইসলামিক বিধান মতে একজন মুসলিমের ওপর অপর মুসলিমের কয়টি মৌলিক অধিকার (হক) রয়েছে?", "According to authentic Hadith, how many fundamental rights does a Muslim have over another?",
         ["৬টি", "৪টি", "৫টি", "৮টি"], ["6 Rights", "4 Rights", "5 Rights", "8 Rights"], 0,
         "সালামের জবাব দেওয়া, দাওয়াত গ্রহণ, উপদেশ চাওয়া, হাঁচির উত্তর, অসুস্থ হলে দেখতে যাওয়া এবং জানাজায় অংশ নেওয়া।", "Six rights: Salam, invitations, advice, sneezing, visiting the sick, and attending funeral.", "সহীহ মুসলিম: ২১৬২", "Sahih Muslim: 2162", "MEDIUM"),

        ("কুরআনে কোন সূরাকে 'সূরাতুশ শিফা' বা আরোগ্য দানকারী সূরা বলা হয়?", "Which Surah is also termed as 'Surah ash-Shifa' (The Healing Surah)?",
         ["সূরা আল-ফাতিহা", "সূরা আল-বাকারা", "সূরা আল-ইখলাস", "সূরা আন-নাস"], ["Surah Al-Fatihah", "Surah Al-Baqarah", "Surah Al-Ikhlas", "Surah An-Nas"], 0,
         "সূরা আল-ফাতিহা সকল আধ্যাত্মিক ও শারীরিক ব্যাধির জন্য মহৌষধ।", "Surah Al-Fatihah is known as the ultimate cure (Ash-Shifa).", "সহীহ বুখারী: ৫০০৭", "Sahih Bukhari: 5007", "EASY"),

        ("ইসলামের প্রথম লিখিত হাদিস সংকলন 'সহীফায়ে সাদিকা' কার দ্বারা সংকলিত হয়েছিল?", "Who compiled the early written Hadith collection 'Sahifah al-Sadiqah'?",
         ["হযরত আবদুল্লাহ ইবনে আমর ইবনুল আস (রা.)", "হযরত আবু হুরায়রা (রা.)", "হযরত আনাস (রা.)", "হযরত ইবনে উমর (রা.)"], ["Abdullah ibn Amr ibn al-Aas (RA)", "Abu Hurairah (RA)", "Anas (RA)", "Ibn Umar (RA)"], 0,
         "আবদুল্লাহ ইবনে আমর রাসূলুল্লাহ ﷺ-এর অনুমতি নিয়ে হাদিস লিখে রাখতেন যা সহীফায়ে সাদিকা নামে পরিচিত।", "Abdullah ibn Amr wrote down Hadiths during the Prophet's life in Sahifah al-Sadiqah.", "মুসনাদে আহমাদ: ৬৪৯৫", "Musnad Ahmad: 6495", "HARD"),

        ("কোন যুদ্ধের মাধ্যমে মক্কার কুরাইশদের অহংকার চূর্ণ হয়েছিল এবং একে 'ইয়াওমুল ফুরকান' বলা হয়েছে?", "Which battle is called 'Yawm al-Furqan' (Day of Distinction) in the Quran?",
         ["বদরের যুদ্ধ", "উহুদের যুদ্ধ", "হুনাইনের যুদ্ধ", "খাইবার যুদ্ধ"], ["Battle of Badr", "Battle of Uhud", "Battle of Hunayn", "Battle of Khaybar"], 0,
         "সূরা আনফালে বদরের যুদ্ধকে সত্য ও মিথ্যার পার্থক্যকারী 'ইয়াওমুল ফুরকান' বলা হয়েছে।", "The Battle of Badr is called Yawm al-Furqan in Surah Al-Anfal: 41.", "সূরা আল-আনফাল: ৪১", "Surah Al-Anfal: 41", "MEDIUM"),

        ("রাসূলুল্লাহ ﷺ-এর জীবদ্দশায় কতজন সাহাবী পবিত্র কুরআন সম্পূর্ণ মুখস্থ (হাফেজ) করেছিলেন বলে বিশেষভাবে খ্যাত?", "Which prominent group of companions memorized the full Quran during the Prophet's ﷺ lifetime?",
         ["উবাই ইবনে কাব, মুআজ ইবনে জাবাল, যায়েদ ইবনে সাবিত, আবু যায়েদ (রা.)", "আবু বকর, উমর, উসমান ও আলী (রা.)", "খালিদ, তারেক ও আম্মার (রা.)", "আবু জর ও মিকদাদ (রা.)"], ["Ubayy, Muadh, Zayd ibn Thabit, Abu Zayd (RA)", "Abu Bakr, Umar, Uthman, Ali", "Khalid, Tariq, Ammar", "Abu Dharr, Miqdad"], 0,
         "সহীহ বুখারীতে বর্ণিত বিশিষ্ট আনসারী চার সাহাবী রাসূলুল্লাহ ﷺ-এর যুগে কুরআন সংগ্রহ করেছিলেন।", "Prominent Huffaz gathered the Quran during the prophetic era.", "সহীহ বুখারী: ৫০৯৯", "Sahih Bukhari: 5099", "HARD"),

        ("ইসলামে কোন ধরণের আহারকে শয়তানের কাজ বলে সতর্ক করা হয়েছে?", "Which eating habit is discouraged as the way of Satan?",
         ["বাম হাতে আহার করা ও পান করা", "বসে খাওয়া", "বিসমিল্লাহ বলা", "তিন আঙুলে খাওয়া"], ["Eating and drinking with the left hand", "Sitting while eating", "Saying Bismillah", "Eating with three fingers"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: শয়তান বাম হাত দিয়ে খায় এবং বাম হাত দিয়ে পান করে।", "The Prophet ﷺ said: Satan eats with his left hand and drinks with his left hand.", "সহীহ মুসলিম: ২০১২", "Sahih Muslim: 2012", "EASY"),

        ("হিজরতের সময় মদীনার পূর্ব নাম কী ছিল?", "What was the ancient name of Madinah before Hijrah?",
         ["ইয়াসরিব", "বাথহা", "তায়েফ", "খায়বার"], ["Yathrib", "Batha", "Taif", "Khaybar"], 0,
         "হিজরতের পূর্বে মদীনা নগরী 'ইয়াসরিব' নামে পরিচিত ছিল। পরবর্তীতে মদিনাতুন্নবী নাম ধারণ করে।", "Madinah was called Yathrib prior to the arrival of the Prophet ﷺ.", "সহীহ বুখারী: ১৮৭১", "Sahih Bukhari: 1871", "EASY"),

        ("ইসলামে কোন মসজিদকে 'তাকওয়ার ওপর প্রতিষ্ঠিত মসজিদ' বলা হয়েছে?", "Which mosque is celebrated in the Quran as founded upon piety from the first day?",
         ["মসজিদে কুবা", "মসজিদে দিরার", "মসজিদে নামিরাহ", "মসজিদে গামামাহ"], ["Masjid Quba", "Masjid Dirar", "Masjid Namirah", "Masjid Ghamamah"], 0,
         "মদীনায় প্রবেশের মুখে হযরত মুহাম্মদ ﷺ কর্তৃক নির্মিত মসজিদে কুবা তাকওয়ার ভিত্তির ওপর প্রতিষ্ঠিত।", "Masjid Quba was established upon piety from its first day (Surah At-Tawbah: 108).", "সূরা আত-তাওবাহ: ১০৮", "Surah At-Tawbah: 108", "MEDIUM"),

        ("কুরআনের কোন সূরায় নারীদের উত্তরাধিকার ও পারিবারিক অধিকার সবচেয়ে বিস্তারিত বর্ণিত হয়েছে?", "Which Surah extensively details inheritance laws and women's rights?",
         ["সূরা আন-নিসা", "সূরা মারিয়াম", "সূরা আত-তালাক", "সূরা আল-মুজাদালাহ"], ["Surah An-Nisa", "Surah Maryam", "Surah At-Talaq", "Surah Al-Mujadilah"], 0,
         "সূরা আন-নিসায় নারীদের অধিকার, মোহরানা এবং মিরাস বণ্টন নিখুঁতভাবে বিধিবদ্ধ করা হয়েছে।", "Surah An-Nisa legislates comprehensive inheritance shares and women's rights.", "সূরা আন-নিসা: ১১-১২", "Surah An-Nisa: 11-12", "EASY"),

        ("ইসলামের পরিভাষায় 'ইসরা' ও 'মিরাজ' বলতে কী বোঝায়?", "What do 'Isra' and 'Miraj' refer to in Islamic terminology?",
         ["রাতের সফর (মক্কা থেকে বাইতুল মুকাদ্দাস) ও ঊর্ধ্বাকাশ ভ্রমণ", "হিজরত করা", "যুদ্ধ করা", "রোজা রাখা"], ["The Night Journey from Makkah to Jerusalem and Ascension to Heavens", "Migration", "Battle", "Fasting"], 0,
         "ইসরা হলো মক্কা থেকে জেরুসালেমের সফর এবং মিরাজ হলো সপ্ত আকাশ অতিক্রম করে আল্লাহর সান্নিধ্যে গমন।", "Isra is the journey from Makkah to Jerusalem, and Miraj is the heavenly ascension.", "সূরা আল-ইসরা: ১", "Surah Al-Isra: 1", "EASY"),

        ("আল্লাহর সুন্দরতম নাম (আসমাউল হুসনা) কয়টি বলে হাদিসে বিশেষভাবে উল্লেখ রয়েছে?", "How many blessed names (Asma ul-Husna) are highlighted in the famous Hadith?",
         ["৯৯টি নাম", "৭৭টি নাম", "১০০টি নাম", "১০১টি নাম"], ["99 Names", "77 Names", "100 Names", "101 Names"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: নিশ্চয়ই আল্লাহর নিরানব্বইটি (১০০-১) নাম রয়েছে, যে তা মুখস্থ ও অনুধাবন করবে সে জান্নাতে প্রবেশ করবে।", "The Prophet ﷺ said: Allah has ninety-nine names; whoever enumerates and lives by them enters Paradise.", "সহীহ বুখারী: ২৭৩৬", "Sahih Bukhari: 2736", "EASY"),

        ("ইসলামে সবচেয়ে বড় নেক আমল কোনটি যা সকল ইবাদতের মূল?", "Which act is the prerequisite and foundation for all good deeds to be accepted?",
         ["খাঁটি নিয়ত ও ইখলাস", "লোক দেখানো আমল", "সম্পদ ব্যয়", "সফর"], ["Sincere Intention & Ikhlas", "Ostentation (Riya)", "Spending wealth", "Travel"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সকল কাজের ফলাফল নিয়তের ওপর নির্ভরশীল।", "The Prophet ﷺ said: Actions are judged strictly by intentions.", "সহীহ বুখারী: ১", "Sahih Bukhari: 1", "EASY"),

        ("কুরআনে কোন নবীকে 'ধৈর্যের প্রতীক' হিসেবে স্মরণ করা হয়েছে যিনি কঠিন রোগে দীর্ঘ পরীক্ষা দিয়েছিলেন?", "Which Prophet is renowned in the Quran for exemplary patience (Sabr) during illness?",
         ["হযরত আইয়ুব (আ.)", "হযরত ইয়াকুব (আ.)", "হযরত ইউনুস (আ.)", "হযরত লুত (আ.)"], ["Prophet Ayyub (Job) (AS)", "Prophet Yaqub (AS)", "Prophet Yunus (AS)", "Prophet Lut (AS)"], 0,
         "হযরত আইয়ুব (আ.) কঠিন শারীরিক ও আর্থিক বিপদে চরম ধৈর্য ও শোকর প্রদর্শন করেছিলেন।", "Prophet Ayyub (AS) exemplified profound patience in prolonged trial.", "সূরা সাদ: ৪১-৪৪", "Surah Sad: 41-44", "EASY"),

        ("পবিত্র কুরআনের কোন আয়াতে মুসলিমদের ভ্রাতৃত্ববোধের কথা ঘোষণা করা হয়েছে?", "Which verse declares all believers as brothers unto one another?",
         ["ইন্নামাল মু'মিনূনা ইখওয়াহ (সূরা হুজুরাত: ১০)", "সূরা বাকারা: ২৫৫", "সূরা তাওবাহ: ১২৮", "সূরা ফাতহ: ২৯"], ["Innamal mu'minuna ikhwatun (Surah Al-Hujurat: 10)", "Surah Baqarah: 255", "Surah Tawbah: 128", "Surah Fath: 29"], 0,
         "পবিত্র কুরআনে আল্লাহ বলেন: নিশ্চয়ই মুমিনগণ পরস্পর ভাই ভাই।", "The Quran explicitly states: The believers are but brothers (Surah Al-Hujurat: 10).", "সূরা আল-হুজুরাত: ১০", "Surah Al-Hujurat: 10", "EASY"),

        ("কোন বিখ্যাত ইসলামী বিজয়ে স্পেন (আন্দালুসিয়া) মুসলিম শাসনের অন্তর্ভুক্ত হয়েছিল?", "Under whose leadership did the Islamic conquest of Spain (Andalusia) take place in 711 CE?",
         ["তারেক বিন যিয়াদ ও মুসা বিন নুসাইর", "সালাহউদ্দিন আইয়ুবী", "খালিদ বিন ওয়ালিদ", "মুহাম্মদ বিন কাসিম"], ["Tariq ibn Ziyad & Musa ibn Nusayr", "Salahuddin Ayyubi", "Khalid ibn Walid", "Muhammad ibn Qasim"], 0,
         "তারেক বিন যিয়াদ ৭১১ খ্রিস্টাব্দে জিব্রাল্টার অতিক্রম করে আন্দালুসিয়া জয় করেন।", "Tariq ibn Ziyad led the decisive Muslim conquest of the Iberian Peninsula.", "তারিখুল ইসলাম", "Tarikh al-Islam", "MEDIUM"),

        ("কোন মুসলিম সেনাপতি জেরুসালেমকে ক্রুসেডারদের হাত থেকে পুনরুদ্ধার করেছিলেন?", "Which heroic leader liberated Jerusalem from Crusader rule in 1187 CE?",
         ["সুলতান সালাহউদ্দিন আইয়ুবী", "নূরুদ্দীন জঙ্গী", "আলপ আরসলান", "সুলতান মাহমুদ গজনভী"], ["Sultan Salahuddin Ayyubi", "Nur ad-Din Zangi", "Alp Arslan", "Mahmud Ghaznavi"], 0,
         "সুলতান সালাহউদ্দিন আইয়ুবী ১১৮৭ সালে হিত্তিনের যুদ্ধের মাধ্যমে বায়তুল মুকাদ্দাস মুক্ত করেন।", "Salahuddin Ayyubi liberated Jerusalem in 1187 following the Battle of Hattin.", "আল-কামিল ফিত-তারিখ", "Al-Kamil fit-Tarikh", "EASY"),

        ("সিন্ধু বিজয় করে ভারতীয় উপমহাদেশে ইসলামের প্রবেশদ্বার উন্মোচন করেন কোন কিশোর সেনাপতি?", "Which young general opened the gateway to Islam in Sindh/Indian subcontinent in 712 CE?",
         ["মুহাম্মদ বিন কাসিম", "কুতাইবা বিন মুসলিম", "তারেক বিন জিয়াদ", "আমির খসরু"], ["Muhammad ibn Qasim", "Qutayba ibn Muslim", "Tariq ibn Ziyad", "Amir Khusrow"], 0,
         "মাত্র ১৭ বছর বয়সে মুহাম্মদ বিন কাসিম সিন্ধু বিজয় সম্পন্ন করেন।", "Muhammad ibn Qasim led the conquest of Sindh at age 17 in 712 CE.", "চাচনামা", "Chachnama", "MEDIUM"),

        ("ইসলামে প্রথম বিশ্ববিদ্যালয় কোনটি যা মরক্কোর ফেজ শহরে প্রতিষ্ঠিত বিশ্বের প্রাচীনতম সক্রিয় বিশ্ববিদ্যালয়?", "Which is the world's oldest continuously operating university founded by Fatima al-Fihri?",
         ["ইউনিভার্সিটি অব আল-কারাওইয়িন", "আল-আজহার বিশ্ববিদ্যালয়", "নিযামিয়া মাদ্রাসা", "কর্ডোভা বিশ্ববিদ্যালয়"], ["University of al-Qarawiyyin", "Al-Azhar University", "Nizamiyya", "Cordoba University"], 0,
         "ফাতিমা আল-ফিহরি ৮৫৯ খ্রিস্টাব্দে মরক্কোর ফেজে আল-কারাওইয়িন প্রতিষ্ঠান গড়ে তোলেন।", "Fatima al-Fihri founded University of al-Qarawiyyin in Fez, Morocco in 859 CE.", "ইউনেস্কো বিশ্ব ঐতিহ্য", "UNESCO World Records", "MEDIUM"),

        ("কুরআনে কোন পাহাড়ের ওপর হযরত মূসা (আ.) আল্লাহর নূর প্রত্যক্ষ করেছিলেন এবং কথা বলেছিলেন?", "On which blessed mountain did Prophet Musa (AS) speak to Allah?",
         ["তূর পাহাড় (জাবালে তূর)", "হেরা পাহাড়", "সাওর পাহাড়", "উহুদ পাহাড়"], ["Mount Tur (Sinai)", "Mount Hira", "Mount Thawr", "Mount Uhud"], 0,
         "সিনাই মরুভূমির তূর পাহাড়ে হযরত মূসা (আ.) আল্লাহর নির্দেশ ও তাওরাত লাভ করেন।", "Prophet Musa (AS) received revelations on Mount Tur (Sinai).", "সূরা আল-কাসাস: ২৯-৩০", "Surah Al-Qasas: 29-30", "EASY"),

        ("হযরত নূহ (আ.)-এর কিশতি প্লাবনের পর কোন পাহাড়ে এসে নোঙর করেছিল?", "On which mountain did Prophet Nuh's Ark rest after the Flood?",
         ["জুদী পাহাড়ে", "আরাফাত পাহাড়ে", "তূর পাহাড়ে", "হিমালয়ে"], ["Mount Judi", "Mount Arafat", "Mount Tur", "Himalayas"], 0,
         "পবিত্র কুরআনে সুস্পষ্টভাবে বলা হয়েছে: 'এবং নৌকাটি জুদী পর্বতে স্থির হলো' (সূরা হুদ: ৪৪)।", "The Quran explicitly states the Ark rested upon Mount Judi (Surah Hud: 44).", "সূরা হুদ: ৪৪", "Surah Hud: 44", "MEDIUM"),

        ("ইসলামের পরিভাষায় 'হিসনুল মুসলিম' গ্রন্থটির মূল বিষয়বস্তু কী?", "What is the primary subject matter of the renowned book 'Hisnul Muslim'?",
         ["কুরআন ও হাদিসের বিশুদ্ধ দোয়া ও যিকিরের সংকলন", "ইতিহাসের যুদ্ধ বিবরণী", "বিজ্ঞান ও গণিত", "স্বপ্নের ব্যাখ্যা"], ["Authentic Quranic and Prophetic Duas and Adhkar", "War History", "Science and Math", "Dream Interpretations"], 0,
         "শাইখ সাঈদ ইবনে আলী আল-কাহতানী সংকলিত হিসনুল মুসলিম দৈনন্দিন দোয়া ও জিকিরের আকর গ্রন্থ।", "Hisnul Muslim is the world's most famous compilation of daily prophetic supplications.", "হিসনুল মুসলিম পরিচিতি", "Hisnul Muslim", "EASY")
    ]
    items.extend(more_gk_topics)

    # Add remaining to make exactly 100 GK items
    gk_part4 = [
        ("ইসলামে কোন মাসে রোজা রাখা সকল প্রাপ্তবয়স্ক সুস্থ মুসলিমের জন্য ফরজ?", "In which month is fasting obligatory for all healthy adult Muslims?",
         ["রমজান মাস", "শাবান মাস", "রজব মাস", "মুহররম মাস"], ["Ramadan", "Shaban", "Rajab", "Muharram"], 0,
         "রমজান মাসেই রোজা ফরজ করা হয়েছে (সূরা বাকারা: ১৮৩)।", "Fasting is prescribed during Ramadan (Surah Al-Baqarah: 183).", "সূরা আল-বাকারা: ১৮৩", "Surah Al-Baqarah: 183", "EASY"),

        ("হজের প্রধান আনুষ্ঠানিকতা কোন হিজরি মাসে পালিত হয়?", "In which Islamic month are the core rituals of Hajj performed?",
         ["জিলহজ মাস", "জিলকদ মাস", "শাওয়াল মাস", "মুহররম মাস"], ["Dhul Hijjah", "Dhul Qadah", "Shawwal", "Muharram"], 0,
         "জিলহজ মাসের ৮ থেকে ১২ তারিখ হজের মূল রুকনসমূহ পালিত হয়।", "The rituals of Hajj occur between 8th and 12th of Dhul Hijjah.", "সহীহ বুখারী: ১৫১৩", "Sahih Bukhari: 1513", "EASY"),

        ("ইসলামিক পরিভাষায় 'তাকওয়া' শব্দের অর্থ কী?", "What does the core Islamic term 'Taqwa' signify?",
         ["আল্লাহভীতি ও পাপ থেকে বেঁচে থাকা", "যুদ্ধ করা", "সম্পদ অর্জন", "দেশভ্রমণ"], ["God-consciousness and piety", "Fighting", "Earning wealth", "Traveling"], 0,
         "তাকওয়া হলো আল্লাহর আদেশ পালন ও নিষেধ বর্জনের মাধ্যমে হৃদয়ে আল্লাহভীতি বজায় রাখা।", "Taqwa is living with God-consciousness and avoiding all prohibitions.", "মুফরাদাত আল-কুরআন", "Mufradat al-Quran", "EASY"),

        ("ইসলামে সালাম বিনিময়ের সর্বোত্তম রূপ কোনটি?", "What is the complete and most rewarding form of Islamic greeting?",
         ["আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহি ওয়া বারাকাতুহু", "শুভ সকাল", "কেমন আছেন", "নমস্কার"], ["Assalamu Alaikum wa Rahmatullahi wa Barakatuh", "Good morning", "How are you", "Hello"], 0,
         "রাসূলুল্লাহ ﷺ পূর্ণাঙ্গ সালামে ৩০টি নেকির সুসংবাদ দিয়েছেন।", "Offering the full Salam earns 30 rewards.", "সুনানে আবু দাউদ: ৫১৯৫", "Sunan Abi Dawud: 5195", "EASY"),

        ("হাঁচি দেওয়ার পর একজন মুসলিমের কী বলা সুন্নাত?", "What should a Muslim say upon sneezing?",
         ["আলহামদুলিল্লাহ", "সুবহানাল্লাহ", "আস্তাগফিরুল্লাহ", "আল্লাহু আকবার"], ["Alhamdulillah", "Subhanallah", "Astaghfirullah", "Allahu Akbar"], 0,
         "হাঁচি দেওয়ার পর 'আলহামদুলিল্লাহ' বলা সুন্নাত এবং শ্রবণকারী 'ইয়ারহামুকাল্লাহ' বলবে।", "One says Alhamdulillah after sneezing, and the listener responds with Yarhamukallah.", "সহীহ বুখারী: ৬২২৪", "Sahih Bukhari: 6224", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর কোন সাহাবীকে 'তরজুমানুল কুরআন' (কুরআনের শ্রেষ্ঠ ভাষ্যকার) বলা হয়?", "Which companion is celebrated as 'Tarjuman al-Quran' (The Interpreter of the Quran)?",
         ["হযরত আবদুল্লাহ ইবনে আব্বাস (রা.)", "হযরত আবদুল্লাহ ইবনে মাসউদ (রা.)", "হযরত যায়েদ বিন সাবিত (রা.)", "হযরত আবু হুরায়রা (রা.)"], ["Abdullah ibn Abbas (RA)", "Abdullah ibn Masud (RA)", "Zayd ibn Thabit (RA)", "Abu Hurairah (RA)"], 0,
         "রাসূলুল্লাহ ﷺ ইবনে আব্বাসের জন্য দীনের গভীর জ্ঞান ও তাফসীর বোঝার বিশেষ দোয়া করেছিলেন।", "The Prophet ﷺ supplicated for Ibn Abbas to be blessed with deep understanding of Quranic tafsir.", "সহীহ বুখারী: ১৪৩", "Sahih Bukhari: 143", "MEDIUM"),

        ("ইসলামে কোন চার মাসকে 'আশহুরে হুরুম' বা সম্মানিত নিষিদ্ধ মাস বলা হয়?", "Which four months are designated as the Sacred Months (Ashhur al-Hurum)?",
         ["জিলকদ, জিলহজ, মুহররম ও রজব", "রমজান, শাওয়াল, সফর ও শাবান", "রবিউল আউয়াল, রবিউস সানি, রজব ও রমজান", "মুহররম, সফর, রবিউল আউয়াল ও শাওয়াল"], ["Dhul Qadah, Dhul Hijjah, Muharram, and Rajab", "Ramadan, Shawwal, Safar, Shaban", "Rabi I, Rabi II, Rajab, Ramadan", "Muharram, Safar, Rabi I, Shawwal"], 0,
         "আল্লাহ তাআলা সৃষ্টির শুরু থেকেই এই চারটি মাসকে বিশেষ সম্মানিত ও যুদ্ধ নিষিদ্ধ ঘোষণা করেছেন।", "Four sacred months declared in Surah At-Tawbah: 36.", "সূরা আত-তাওবাহ: ৩৬", "Surah At-Tawbah: 36", "MEDIUM"),

        ("রাসূলুল্লাহ ﷺ-এর পিতা ও মাতার নাম কী ছিল?", "What were the names of Prophet Muhammad's ﷺ father and mother?",
         ["পিতা: আবদুল্লাহ, মাতা: আমিনা", "পিতা: আবু তালিব, মাতা: ফাতিমা", "পিতা: আবদুল মুত্তালিব, মাতা: হালিমা", "পিতা: যুবায়ের, মাতা: সাফিয়া"], ["Father: Abdullah, Mother: Aminah", "Father: Abu Talib, Mother: Fatimah", "Father: Abdul Muttalib, Mother: Halimah", "Father: Zubayr, Mother: Safiyyah"], 0,
         "রাসূলুল্লাহ ﷺ-এর পিতার নাম আবদুল্লাহ ইবনে আবদুল মুত্তালিব এবং মাতার নাম আমিনা বিনতে ওয়াহাব।", "The Prophet's father was Abdullah and mother was Aminah.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ কত বছর বয়সে নবুওয়াত লাভ করেন?", "At what age did Prophet Muhammad ﷺ receive the first revelation and Prophethood?",
         ["৪০ বছর বয়সে", "২৫ বছর বয়সে", "৩৩ বছর বয়সে", "৫০ বছর বয়সে"], ["At the age of 40", "At 25", "At 33", "At 50"], 0,
         "৪০ বছর বয়সে রমজান মাসে হেরা গুহায় ধ্যানমগ্ন অবস্থায় রাসূলুল্লাহ ﷺ নবুওয়াত লাভ করেন।", "The Prophet ﷺ received prophethood at age 40 in Cave Hira.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর নবুওয়াতী জীবনের মোট মেয়াদ কত বছর ছিল?", "How many years did the Prophetic mission of Muhammad ﷺ last?",
         ["২৩ বছর (১৩ বছর মক্কায় + ১০ বছর মদীনায়)", "২০ বছর", "২৫ বছর", "৩০ বছর"], ["23 years (13 in Makkah + 10 in Madinah)", "20 years", "25 years", "30 years"], 0,
         "রাসূলুল্লাহ ﷺ নবুওয়াত প্রাপ্তির পর মক্কায় ১৩ বছর ও মদীনায় ১০ বছর মোট ২৩ বছর দ্বীনের দাওয়াত দেন।", "Prophethood lasted 23 years: 13 years in Makkah and 10 years in Madinah.", "সহীহ বুখারী: ৩৫৩৬", "Sahih Bukhari: 3536", "EASY"),

        ("ইসলামে মিথ্যা সাক্ষ্য দেওয়া বা মিথ্যা কথা বলা কেমন অপরাধ?", "What is the severity of giving false testimony (Shahadat az-Zur) in Islam?",
         ["মারাত্মক কবিরা গুনাহ", "ছোট গুনাহ", "মাকরুহ তানজিহী", "সাধারণ ভুল"], ["Major sin (Kaba'ir) of highest degree", "Minor sin", "Mildly disliked", "Normal mistake"], 0,
         "রাসূলুল্লাহ ﷺ শিরকের পরেই মিথ্যা সাক্ষ্য দেওয়াকে অন্যতম ধ্বংসাত্মক কবিরা গুনাহ আখ্যা দিয়েছেন।", "False testimony is among the greatest of destructive major sins.", "সহীহ বুখারী: ২৬৫৪", "Sahih Bukhari: 2654", "EASY"),

        ("কোন সাহাবীকে জীবদ্দশায় ফেরেশতাগণ গোসল করিয়েছিলেন বলে 'হাসিলুল মালাইকা' বলা হয়?", "Which companion was washed by angels after martyrdom and known as 'Ghasil al-Mala'ikah'?",
         ["হযরত হানজালা ইবনে আবি আমির (রা.)", "হযরত মুসআব (রা.)", "হযরত খুবাইব (রা.)", "হযরত আনাস বিন নদর (রা.)"], ["Hanzalah ibn Abi Amir (RA)", "Mus'ab (RA)", "Khubayb (RA)", "Anas ibn an-Nadr (RA)"], 0,
         "উহুদের যুদ্ধে শাহাদাতের পর ফেরেশতাগণ আকাশ ও জমিনের মাঝে হযরত হানজালা (রা.)-কে গোসল করান।", "Hanzalah (RA) was washed by angels following his martyrdom at Uhud.", "আল-মুস্তাদরাক আলাস সহীহাইন: ৪৯১৩", "Al-Mustadrak: 4913", "HARD"),

        ("কুরআনের কোন সূরায় আল্লাহ তাআলা সময়ের কসম খেয়ে মানবজাতির ক্ষতির কথা বলেছেন?", "In which Surah does Allah swear by Time that mankind is in loss except those of faith and good deeds?",
         ["সূরা আল-আসর", "সূরা আল-হুমাযাহ", "সূরা আল-ফীল", "সূরা কুরাইশ"], ["Surah Al-Asr", "Surah Al-Humazah", "Surah Al-Fil", "Surah Quraysh"], 0,
         "ইমাম শাফেয়ী (রহ.) বলেছেন: মানুষ যদি কেবল সূরা আসর নিয়ে চিন্তা করত, তবে তাই হেদায়াতের জন্য যথেষ্ট হতো।", "Imam Shafi stated that if humanity contemplated only Surah Al-Asr, it would suffice them.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর দুধমাতা যিনি শৈশবে তাঁকে লালন-পালন করেছিলেন তাঁর নাম কী?", "What was the name of the foster mother who nursed Prophet Muhammad ﷺ in Banu Sa'd?",
         ["হালীমাতুস সাদিয়া (রা.)", "উম্মে আইমান (রা.)", "সুওয়াইবা", "ফাতিমা বিনতে আসাদ"], ["Halimah al-Sa'diyyah (RA)", "Umm Ayman (RA)", "Thuwaybah", "Fatimah bint Asad"], 0,
         "হযরত হালীমাতুস সাদিয়া (রা.) বানু সা'দ গোত্রে রাসূলুল্লাহ ﷺ-কে দুধপান ও লালন-পালন করেন।", "Halimah al-Sa'diyyah nurtured the young Prophet ﷺ in the desert of Banu Sa'd.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("কোন সাহাবী ইসলাম গ্রহণের কারণে তপ্ত বালির ওপর পাথর চাপা দিয়ে অমানুষিক নির্যাতন সহ্য করেও 'আহাদ আহাদ' বলতেন?", "Which companion endured extreme torture on burning desert sands while repeating 'Ahad, Ahad'?",
         ["হযরত বিলাল ইবনে রাবাহ (রা.)", "হযরত আম্মার ইবনে ইয়াসির (রা.)", "হযরত সুহাইব রুমি (রা.)", "হযরত খাব্বাব (রা.)"], ["Bilal ibn Rabah (RA)", "Ammar ibn Yasir (RA)", "Suhayb ar-Rumi (RA)", "Khabbab (RA)"], 0,
         "উমাইয়া ইবনে খালাফের শত অত্যাচারেও হযরত বিলাল (রা.) আল্লাহর তাওহীদের ঘোষণায় অবিচল ছিলেন।", "Bilal (RA) persevered through brutal torture declaring the oneness of Allah.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "EASY"),

        ("ইসলামে রোগীর সেবা ও খোঁজখবর নেওয়ার মর্যাদা কী?", "What is the spiritual reward of visiting and caring for a sick person in Islam?",
         ["জান্নাতের ফলবাগানে বিচরণের মতো বিশাল সওয়াব", "শুধুমাত্র সামাজিক শিষ্টাচার", "কোনো সওয়াব নেই", "বাধ্যতামূলক নয়"], ["Tremendous reward akin to roaming the gardens of Jannah", "Mere etiquette", "No reward", "Discouraged"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যখন কোনো মুসলিম তার অসুস্থ ভাইয়ের খোঁজ নেয়, সে ফিরে না আসা পর্যন্ত জান্নাতের ফলবাগানে থাকে।", "Visiting the sick keeps a person immersed in the harvest of Paradise.", "সহীহ মুসলিম: ২৫৬৮", "Sahih Muslim: 2568", "EASY"),

        ("ইসলামিক অর্থনীতির মূল বৈশিষ্ট্য কোনটি যা পুঁজিবাদ ও সমাজতন্ত্র থেকে পৃথক?", "What is the core distinction of Islamic economics from capitalism and socialism?",
         ["সুদবিহীন, ইনসাফভিত্তিক ও যাকাত-সদকা নির্ভর অর্থনীতি", "অবাধ মুনাফাখোরি", "ব্যক্তিগত মালিকানা হরণ", "সুদের উচ্চ হার"], ["Interest-free, equity-based with mandatory Zakat and welfare", "Unregulated profiteering", "Abolition of private property", "High interest rates"], 0,
         "ইসলামে সম্পদ কেন্দ্রীভূতকরণ ও সুদ নিষিদ্ধ করে সামাজিক নিরাপত্তা ও ইনসাফ কায়েম করা হয়েছে।", "Islamic economics prohibits usury and mandates Zakat for equitable distribution.", "সূরা আল-হাশর: ৭", "Surah Al-Hashr: 7", "MEDIUM"),

        ("পবিত্র কুরআনের কোন আয়াতে ঘোষণা করা হয়েছে যে ইসলাম আল্লাহর মনোনীত একমাত্র পূর্ণাঙ্গ দ্বীন?", "In which verse did Allah declare Islam as the perfected and chosen religion?",
         ["আল-ইয়াওমা আকমালতু লাকুম দীনাকুম (সূরা মায়েদাহ: ৩)", "সূরা বাকারা: ২৫৬", "সূরা আলে ইমরান: ১৯", "সূরা তাওবাহ: ৩৩"], ["Al-yawma akmaltu lakum deenakum (Surah Al-Maidah: 3)", "Surah Baqarah: 256", "Surah Ali Imran: 19", "Surah Tawbah: 33"], 0,
         "বিদায় হজের আরাফাতের দিনে এই ঐতিহাসিক আয়াত নাযিল হয়।", "Revealed on the Day of Arafah during the Farewell Hajj (Surah Al-Maidah: 3).", "সহীহ বুখারী: ৪৫", "Sahih Bukhari: 45", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর কোন সাহাবী তাঁর নির্দেশে পারস্য ও রোমের রাজাদের চিঠির জবাব দেওয়ার জন্য হিব্রু ও সুরিয়ানি ভাষা আয়ত্ত করেছিলেন?", "Which companion learned Hebrew and Syriac in days to handle correspondence for the Prophet ﷺ?",
         ["হযরত যায়েদ ইবনে সাবিত (রা.)", "হযরত উবাই ইবনে কাব (রা.)", "হযরত মুয়াবিয়া (রা.)", "হযরত আবদুল্লাহ ইবনে রওয়াহা (রা.)"], ["Zayd ibn Thabit (RA)", "Ubayy ibn Kab (RA)", "Muawiyah (RA)", "Abdullah ibn Rawahah (RA)"], 0,
         "যায়েদ বিন সাবিত মাত্র কয়েক সপ্তাহের মধ্যে হিব্রু ও সুরিয়ানি ভাষা শিখে রাসূলুল্লাহ ﷺ-এর ওহী ও কূটনৈতিক চিঠিপত্র লিখতেন।", "Zayd ibn Thabit (RA) mastered Hebrew and Syriac on the Prophet's instruction.", "জামে আত-তিরমিযী: ২৭১৫", "Jami at-Tirmidhi: 2715", "HARD"),

        ("ইসলামে মাতা-পিতার সন্তুষ্টির সাথে কার সন্তুষ্টি সরাসরি জড়িত?", "Whose divine pleasure is directly tied to the pleasure of one's parents?",
         ["আল্লাহ তাআলার সন্তুষ্টি", "সমাজের সন্তুষ্টি", "বন্ধুদের সন্তুষ্টি", "রাষ্ট্রের সন্তুষ্টি"], ["The Pleasure of Allah", "Society's pleasure", "Friends' pleasure", "State's pleasure"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: রবের সন্তুষ্টি পিতার সন্তুষ্টির মাঝে এবং রবের অসন্তুষ্টি পিতার অসন্তুষ্টির মাঝে নিহিত।", "The Prophet ﷺ said: The pleasure of the Lord lies in the pleasure of parents.", "জামে আত-তিরমিযী: ১৮৯৯", "Jami at-Tirmidhi: 1899", "EASY")
    ]
    items.extend(gk_part4)
    
    # Ensure exactly 100 questions
    final_items = []
    for i in range(100):
        item = items[i]
        uid = f"Q_GK_{i+1:03d}"
        final_items.append((uid, 'general_knowledge', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return final_items

if __name__ == '__main__':
    q = get_cat_01_general_knowledge()
    print(f"Cat 01 Generated: {len(q)} questions")
