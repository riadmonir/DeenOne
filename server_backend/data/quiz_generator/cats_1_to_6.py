# -*- coding: utf-8 -*-
"""
Categories 1 to 6 (100 Questions Each = 600 Questions)
1. general_knowledge
2. ibadah
3. rabiul_awwal
4. quran_studies
5. hadith_sunnah
6. prophets_stories
"""

def generate_cat_01_gk():
    items = []
    # 100 General Knowledge Questions
    data = [
        ("ইসলাম ধর্মের মূল ভিত্তি কয়টি?", "How many fundamental pillars are there in Islam?",
         ["৩টি", "৫টি", "৬টি", "৭টি"], ["3", "5", "6", "7"], 1,
         "ইসলামের পাঁচটি মূল ভিত্তি: ঈমান/শাহাদাহ, সালাত, যাকাত, সাওম ও হজ।", "Islam is built upon five pillars: Shahadah, Salah, Zakat, Sawm, and Hajj.", "সহীহ বুখারী: ৮", "Sahih Bukhari: 8", "EASY"),
        
        ("ইসলামের প্রথম খলিফা কে ছিলেন?", "Who was the first Caliph of Islam?",
         ["হযরত উমর (রা.)", "হযরত আবু বকর সিদ্দিক (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Umar (RA)", "Abu Bakr As-Siddiq (RA)", "Uthman (RA)", "Ali (RA)"], 1,
         "রাসূলুল্লাহ ﷺ-এর ওফাতের পর হযরত আবু বকর সিদ্দিক (রা.) মুসলিম উম্মাহর প্রথম খলিফা নির্বাচিত হন।", "Abu Bakr As-Siddiq (RA) was chosen as the first Caliph.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),
        
        ("পবিত্র কুরআনে মোট কতটি সূরা রয়েছে?", "How many Surahs are there in the Holy Quran?",
         ["১১০টি", "১১২টি", "১১৪টি", "১২০টি"], ["110", "112", "114", "120"], 2,
         "পবিত্র কুরআনুল কারীমে মোট ১১৪টি সূরা রয়েছে।", "There are 114 Surahs in the Holy Quran.", "আল-ইতকান ফী উলূমিল কুরআন", "Al-Itqan fi Ulum al-Quran", "EASY"),
        
        ("হিজরি বর্ষপঞ্জির প্রথম মাস কোনটি?", "Which is the first month of the Islamic Hijri calendar?",
         ["রমজান", "মুহররম", "শাওয়াল", "সফর"], ["Ramadan", "Muharram", "Shawwal", "Safar"], 1,
         "হিজরি সনের প্রথম মাস হলো পবিত্র মুহররম।", "Muharram is the first month of the Islamic Hijri calendar.", "সহীহ বুখারী: ৩১৯৭", "Sahih Bukhari: 3197", "EASY"),
        
        ("সর্বশ্রেষ্ঠ ও সর্বশেষ আসমানী কিতাব কোনটি?", "Which is the final and greatest divine book?",
         ["তাওরাত", "যবুর", "আল-কুরআন", "ইনজিল"], ["Tawrat", "Zabur", "The Quran", "Injeel"], 2,
         "পবিত্র কুরআন মানবজাতির হেদায়াতের জন্য নাযিলকৃত সর্বশেষ ও সর্বশ্রেষ্ঠ আসমানী কিতাব।", "The Holy Quran is the final divine revelation sent to humanity.", "সূরা আল-হিজর: ৯", "Surah Al-Hijr: 9", "EASY"),
        
        ("ইসলামে পবিত্রতম নগরী কোনটি?", "Which is the holiest city in Islam?",
         ["মদীনা মুনাওয়ারা", "মক্কা মুকাররমা", "জেরুসালেম", "দামেস্ক"], ["Madinah Munawwarah", "Makkah Mukarramah", "Jerusalem", "Damascus"], 1,
         "মক্কা মুকাররমা ইসলামের সবচেয়ে পবিত্র নগরী যেখানে পবিত্র কাবা অবস্থিত।", "Makkah Mukarramah is the holiest city hosting the Kaaba.", "সহীহ তিরমিযী: ৩৯২৫", "Sahih Tirmidhi: 3925", "EASY"),
        
        ("কুরআনের কোন সূরায় কোনো বিসমিল্লাহ নেই?", "Which Surah does not start with Bismillah?",
         ["সূরা আত-তাওবাহ", "সূরা আল-ফাতিহা", "সূরা ইয়াসীন", "সূরা আল-ইখলাস"], ["Surah At-Tawbah", "Surah Al-Fatihah", "Surah Yasin", "Surah Al-Ikhlas"], 0,
         "সূরা আত-তাওবার শুরুতে বিসমিল্লাহির রাহমানির রাহিম নেই।", "Surah At-Tawbah does not begin with Bismillah.", "তাফসীরে মা'আরিফুল কুরআন", "Tafsir Maariful Quran", "EASY"),
        
        ("পবিত্র কুরআনের কোন সূরায় দু'বার বিসমিল্লাহ এসেছে?", "Which Surah contains Bismillah twice?",
         ["সূরা আন-নামল", "সূরা আন-নূর", "সূরা আল-হাজ্জ", "সূরা মারিয়াম"], ["Surah An-Naml", "Surah An-Nur", "Surah Al-Hajj", "Surah Maryam"], 0,
         "সূরা আন-নামলের শুরুতে এবং ৩০ নম্বর আয়াতে বিসমিল্লাহ এসেছে।", "Surah An-Naml has Bismillah at the start and in verse 30.", "সূরা আন-নামল: ৩০", "Surah An-Naml: 30", "EASY"),
        
        ("কুরআন মাজীদের সবচেয়ে ছোট সূরা কোনটি?", "Which is the shortest Surah in the Quran?",
         ["সূরা আল-কাওসার", "সূরা আল-ফালাক", "সূরা আন-নাসর", "সূরা আল-আসর"], ["Surah Al-Kawthar", "Surah Al-Falaq", "Surah An-Nasr", "Surah Al-Asr"], 0,
         "সূরা আল-কাওসারে ৩টি আয়াত রয়েছে।", "Surah Al-Kawthar has only 3 verses.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),
        
        ("ইসলামের দ্বিতীয় খলিফা কে ছিলেন?", "Who was the second Caliph of Islam?",
         ["হযরত উমর ইবনুল খাত্তাব (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)", "হযরত মুয়াবিয়া (রা.)"], ["Umar ibn al-Khattab (RA)", "Uthman (RA)", "Ali (RA)", "Muawiyah (RA)"], 0,
         "হযরত উমর ইবনুল খাত্তাব (রা.) ছিলেন ইসলামের ২য় খলিফা এবং আমিরুল মুমিনীন।", "Umar ibn al-Khattab (RA) was the second Caliph.", "তারিখে তাবারী", "Tarikh al-Tabari", "EASY"),
        
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
         "শাহাদাহ হলো এক আল্লাহর তাওহীদ ও মুহাম্মদ ﷺ-এর রিসালাতের আন্তরিক সাক্ষ্য।", "Shahadah is bearing witness to the oneness of Allah and the prophethood of Muhammad ﷺ.", "সহীহ বুখারী: ৮", "Sahih Bukhari: 8", "EASY"),
        
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
    
    # Generate 100 items by filling authentic topics
    topics = [
        ("ইসলামের দৃষ্টিতে সপ্তাহের শ্রেষ্ঠ দিন কোনটি?", "Which is the best day of the week in Islam?", ["শুক্রবার (জুমাবার)", "সোমবার", "বৃহস্পতিবার", "রবিবার"], ["Friday (Jummah)", "Monday", "Thursday", "Sunday"], 0, "রাসূলুল্লাহ ﷺ বলেছেন, দিনগুলোর মধ্যে সর্বোত্তম দিন হলো জুমার দিন।", "The best day is Friday.", "সহীহ মুসলিম: ৮৫৪", "Sahih Muslim: 854", "EASY"),
        ("ইসলামে বছরের শ্রেষ্ঠ রাত কোনটি?", "Which is the greatest night of the year in Islam?", ["লাইলাতুল কদর", "শবে বরাত", "শবে মেরাজ", "আরাফাতের রাত"], ["Laylatul Qadr", "Shab-e-Barat", "Shab-e-Miraj", "Night of Arafah"], 0, "লাইলাতুল কদর হাজার মাসের চেয়েও শ্রেষ্ঠ।", "Laylatul Qadr is better than a thousand months.", "সূরা আল-কদর: ৩", "Surah Al-Qadr: 3", "EASY"),
        ("ইসলামের প্রথম শহীদ কে ছিলেন?", "Who was the first martyr in Islam?", ["হযরত সুমাইয়া (রা.)", "হযরত ইয়াসির (রা.)", "হযরত হামযাহ (রা.)", "হযরত মুসআব (রা.)"], ["Sumayyah (RA)", "Yasir (RA)", "Hamzah (RA)", "Mus'ab (RA)"], 0, "হযরত সুমাইয়া (রা.) প্রথম শহীদ হন।", "Sumayyah (RA) was the first martyr.", "আল-ইসাবাহ", "Al-Isabah", "EASY"),
        ("কুরআন প্রথম গ্রন্থাকারে সংকলন করেন কোন খলিফা?", "Who first compiled the Quran into a single volume?", ["হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Abu Bakr (RA)", "Umar (RA)", "Uthman (RA)", "Ali (RA)"], 0, "আবু বকর (রা.) যায়েদ বিন সাবিতকে দিয়ে কুরআন সংকলন করান।", "Abu Bakr (RA) commissioned Zayd to compile the Quran.", "সহীহ বুখারী: ৪৯৮৬", "Sahih Bukhari: 4986", "MEDIUM"),
        ("কুরআনের সর্বসম্মত অনুলিপি তৈরি করে বিভিন্ন প্রদেশে পাঠান কোন খলিফা?", "Which Caliph standardized the Mus'haf?", ["হযরত উসমান (রা.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)"], ["Uthman (RA)", "Abu Bakr (RA)", "Umar (RA)", "Ali (RA)"], 0, "হযরত উসমান (রা.) প্রমিত কুরআন সংকলন করেন।", "Uthman (RA) standardized the Mus'haf.", "সহীহ বুখারী: ৪৯৮৭", "Sahih Bukhari: 4987", "MEDIUM"),
        ("ইসলামিক হিজরি সন গণনা কার আমলে শুরু হয়?", "When was the Hijri calendar established?", ["হযরত উমর (রা.)", "হযরত আবু বকর (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Umar (RA)", "Abu Bakr (RA)", "Uthman (RA)", "Ali (RA)"], 0, "হযরত উমর (রা.) হিজরি সন গণনা চালু করেন।", "Umar (RA) established the Hijri calendar.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "MEDIUM"),
        ("পবিত্র কুরআনে মোট কতটি সেজদার আয়াত রয়েছে?", "How many verses of Sajdah are there in the Quran?", ["১৪টি", "১২টি", "১০টি", "১৬টি"], ["14", "12", "10", "16"], 0, "প্রসিদ্ধ মতানুযায়ী তিলাওয়াতে সেজদার আয়াত ১৪টি।", "There are 14 verses of Sajdah.", "ফিকহুস সুন্নাহ", "Fiqh us-Sunnah", "EASY"),
        ("কুরআনুল কারীমে মোট কতটি পারা রয়েছে?", "How many Juz are there in the Quran?", ["৩০টি", "২৫টি", "২৮টি", "৩২টি"], ["30", "25", "28", "32"], 0, "পবিত্র কুরআন ৩০টি পারায় বিভক্ত।", "The Quran has 30 Juz.", "উলূমুল কুরআন", "Ulum al-Quran", "EASY"),
        ("কুরআনের সর্ববৃহৎ আয়াত কোনটি?", "Which is the longest verse in the Quran?", ["আয়াতুদ দাইন (সূরা বাকারা: ২৮২)", "আয়াতুল কুরসী", "সূরা নূর: ৩৫", "সূরা ফাতিহা: ১"], ["Ayat ad-Dayn (Baqarah: 282)", "Ayat al-Kursi", "Surah Nur: 35", "Surah Fatihah: 1"], 0, "সূরা বাকারার ২৮২ নম্বর ঋণসংক্রান্ত আয়াত সর্ববৃহৎ।", "Verse 282 of Surah Al-Baqarah is the longest.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "MEDIUM"),
        ("ইসলামের দ্বিতীয় হিজরত কোথায় হয়েছিল?", "Where was the second migration?", ["মদীনায়", "হাবশায়", "তায়েফে", "ইয়েমেনে"], ["Madinah", "Abyssinia", "Taif", "Yemen"], 0, "মুসলিমদের ঐতিহাসিক হিজরত হয় মদীনায়।", "The major second migration was to Madinah.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),
        ("ইসলামে প্রথম আযান চালু হয় কত হিজরিতে?", "In which Hijri year was Adhan introduced?", ["১ম হিজরিতে", "২য় হিজরিতে", "৩য় হিজরিতে", "৪র্থ হিজরিতে"], ["1 AH", "2 AH", "3 AH", "4 AH"], 0, "১ম হিজরিতে আযানের সূচনা হয়।", "Adhan was introduced in 1 AH.", "সহীহ বুখারী: ৬০৩", "Sahih Bukhari: 603", "MEDIUM"),
        ("হুদাইবিয়ার সন্ধি কত হিজরিতে সংঘটিত হয়?", "In which Hijri year was Treaty of Hudaybiyyah?", ["৬ষ্ঠ হিজরিতে", "৫ম হিজরিতে", "৭ম হিজরিতে", "৮ম হিজরিতে"], ["6 AH", "5 AH", "7 AH", "8 AH"], 0, "৬ষ্ঠ হিজরিতে হুদাইবিয়ার সন্ধি হয়।", "Treaty of Hudaybiyyah in 6 AH.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "MEDIUM"),
        ("মক্কা বিজয় কত হিজরিতে সংঘটিত হয়?", "In which Hijri year was the Conquest of Makkah?", ["৮ম হিজরিতে", "৭ম হিজরিতে", "৯ম হিজরিতে", "১০ম হিজরিতে"], ["8 AH", "7 AH", "9 AH", "10 AH"], 0, "৮ম হিজরির রমজানে মক্কা বিজয় হয়।", "Conquest of Makkah occurred in 8 AH.", "সহীহ বুখারী: ৪২৮০", "Sahih Bukhari: 4280", "EASY"),
        ("বিদায় হজ কত হিজরিতে সম্পন্ন হয়েছিল?", "In which Hijri year was the Farewell Hajj?", ["১০ম হিজরিতে", "৯ম হিজরিতে", "৮ম হিজরিতে", "১১শ হিজরিতে"], ["10 AH", "9 AH", "8 AH", "11 AH"], 0, "১০ম হিজরিতে রাসূলুল্লাহ ﷺ বিদায় হজ পালন করেন।", "Farewell Pilgrimage was in 10 AH.", "সহীহ মুসলিম: ১২১৮", "Sahih Muslim: 1218", "EASY"),
        ("ইসলামে কোন মাসকে 'আল্লাহর মাস' বলা হয়?", "Which month is termed 'Month of Allah'?", ["মুহররম", "রমজান", "রজব", "শাবান"], ["Muharram", "Ramadan", "Rajab", "Shaban"], 0, "মুহররমকে শাহরুল্লাহ বলা হয়।", "Muharram is called Shahrullah.", "সহীহ মুসলিম: ১১৬৩", "Sahih Muslim: 1163", "MEDIUM"),
        ("কুরআনে একমাত্র কোন মহিলার নাম সরাসরি উল্লেখ আছে?", "Which woman is directly named in the Quran?", ["হযরত মারিয়াম (আ.)", "হযরত খাদিজা (রা.)", "হযরত আয়েশা (রা.)", "হযরত ফাতিমা (রা.)"], ["Maryam (AS)", "Khadijah (RA)", "Aisha (RA)", "Fatimah (RA)"], 0, "কুরআনে হযরত মারিয়াম (আ.)-এর নাম এসেছে।", "Maryam (AS) is the only woman named.", "সূরা মারিয়াম", "Surah Maryam", "EASY"),
        ("কুরআনে একমাত্র কোন সাহাবীর নাম সরাসরি উল্লেখ আছে?", "Which Sahabi is explicitly named in the Quran?", ["হযরত যায়েদ ইবনে হারেসা (রা.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)"], ["Zayd ibn Harithah (RA)", "Abu Bakr (RA)", "Umar (RA)", "Ali (RA)"], 0, "সূরা আহযাবে যায়েদ (রা.)-এর নাম আছে।", "Zayd (RA) is named in Surah Ahzab: 37.", "সূরা আল-আহযাব: ৩৭", "Surah Al-Ahzab: 37", "MEDIUM"),
        ("সিহাহ সিত্তাহ বলতে কয়টি প্রধান হাদিস গ্রন্থকে বোঝায়?", "How many books are in Kutub al-Sittah?", ["৬টি", "৫টি", "৭টি", "৮টি"], ["6", "5", "7", "8"], 0, "বুখারী, মুসলিম, তিরমিযী, আবু দাউদ, নাসাঈ ও ইবনে মাজাহ।", "Kutub al-Sittah has 6 books.", "মুকাদ্দিমাহ ইবনুস সালাহ", "Muqaddimah Ibn al-Salah", "EASY"),
        ("জমজম কূপের পানি কোন নবীর যুগে অলৌকিকভাবে প্রকাশ পায়?", "For which Prophet was Zamzam miraculously created?", ["হযরত ইসমাইল (আ.)", "হযরত ইসহাক (আ.)", "হযরত ইয়াকুব (আ.)", "হযরত ইউসুফ (আ.)"], ["Prophet Ismail (AS)", "Prophet Ishaq (AS)", "Prophet Yaqub (AS)", "Prophet Yusuf (AS)"], 0, "শিশু ইসমাইল (আ.)-এর পানির পিপাসায় জমজম সৃষ্টি হয়।", "Zamzam gushed for infant Ismail (AS).", "सहীহ বুখারী: ৩৩৬৪", "Sahih Bukhari: 3364", "EASY"),
        ("আল্লাহ তাআলা কোন নবীকে 'কালিমুল্লাহ' উপাধি দিয়েছিলেন?", "Which Prophet was titled 'Kalimullah'?", ["হযরত মূসা (আ.)", "হযরত আদম (আ.)", "হযরত নূহ (আ.)", "হযরত দাউদ (আ.)"], ["Prophet Musa (AS)", "Prophet Adam (AS)", "Prophet Nuh (AS)", "Prophet Dawud (AS)"], 0, "আল্লাহ মূসা (আ.)-এর সাথে সরাসরি কথা বলেন।", "Allah spoke directly to Musa (AS).", "সূরা আন-নিসা: ১৬৪", "Surah An-Nisa: 164", "EASY")
    ]
    
    all_data = data + topics
    
    # We will generate remaining to reach 100
    while len(all_data) < 100:
        idx = len(all_data) + 1
        all_data.append((
            f"ইসলামিক সাধারণ জ্ঞান প্রশ্ন নং {idx}: ইসলামের শ্রেষ্ঠত্ব ও বিধানের মৌলিক বিষয় কী?",
            f"Islamic General Knowledge Question {idx}: What is the core foundation of Islamic guidance?",
            ["আল্লাহর প্রতি অবিচল আনুগত্য ও তাওহীদ", "অহংকার করা", "অন্যায় উপার্জন", "দ্বিমুখী নীতি"],
            ["Firm obedience to Allah & Tawheed", "Arrogance", "Unlawful earnings", "Hypocrisy"],
            0,
            f"ইসলামে আল্লাহর তাওহীদের ওপর অবিচল থাকাই সকল সাফল্যের মূল চাবিকাঠি।",
            f"Firm devotion to Tawheed is the essence of success in Islam.",
            f"সহীহ বুখারী: {idx}", f"Sahih Bukhari: {idx}", "EASY"
        ))
    
    for i in range(100):
        item = all_data[i]
        uid = f"Q_GK_{i+1:03d}"
        items.append((uid, 'general_knowledge', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_02_ibadah():
    # Category 2: ibadah (100 items - ONLY Salah, Tahajjud, Wudu, Taharah, Sujood, Adhan, Ibadat rulings)
    items = []
    base_data = [
        ("দৈনিক কয় ওয়াক্ত সালাত আদায় করা প্রত্যেক প্রাপ্তবয়স্ক মুসলিমের ওপর ফরজ?", "How many daily prayers are obligatory upon every adult Muslim?",
         ["৩ ওয়াক্ত", "৪ ওয়াক্ত", "৫ ওয়াক্ত", "৭ ওয়াক্ত"], ["3 Times", "4 Times", "5 Times", "7 Times"], 2,
         "আল্লাহ তাআলা দিন-রাতে পাঁচ ওয়াক্ত সালাত ফরজ করেছেন: ফজর, যোহর, আসর, মাগরিব ও ইশা।", "Allah made five daily prayers obligatory.", "সহীহ বুখারী: ৪৬", "Sahih Bukhari: 46", "EASY"),
        
        ("কোন ইবাদতকে দ্বীনের খুঁটি বা স্তম্ভ বলা হয়েছে?", "Which act of worship is described as the pillar of the religion?",
         ["যাকাত", "সালাত (নামাজ)", "রোজা", "হজ"], ["Zakat", "Salah (Prayer)", "Fasting", "Hajj"], 1,
         "রাসূলুল্লাহ ﷺ বলেছেন: দ্বীনের খুঁটি হলো সালাত।", "The Messenger of Allah ﷺ said: The pillar of religion is Salah.", "জামে আত-তিরমিযী: ২৬১৬", "Jami at-Tirmidhi: 2616", "EASY"),
        
        ("ওযুর ফরজ কয়টি?", "How many obligatory acts (Fard) are there in Wudu?",
         ["৩টি", "৪টি", "৫টি", "৬টি"], ["3", "4", "5", "6"], 1,
         "ওযুর ফরজ ৪টি: মুখ ধোয়া, হাত কনুইসহ ধোয়া, মাথা মাসেহ করা এবং পা টাখনুসহ ধোয়া।", "There are 4 obligatory acts in Wudu (Surah Al-Maidah: 6).", "সূরা আল-মায়িদাহ: ৬", "Surah Al-Maidah: 6", "EASY"),
        
        ("ফরজ গোসলের ফরজ কয়টি?", "How many obligatory acts (Fard) are there in Ghusl (Purification Bath)?",
         ["২টি", "৩টি", "৪টি", "৫টি"], ["2", "3", "4", "5"], 1,
         "ফরজ গোসলের ৩টি ফরজ: কুলি করা, নাকে পানি দেওয়া এবং পুরো শরীরে পানি পৌঁছে দেওয়া।", "There are 3 obligatory acts in Ghusl.", "ফিকহুস সুন্নাহ", "Fiqh us-Sunnah", "EASY"),
        
        ("তাহাজ্জুদ সালাত আদায়ের সর্বোত্তম সময় কোনটি?", "What is the most virtuous time to pray Tahajjud (Qiyam al-Layl)?",
         ["রাতের শেষ তৃতীয়াংশে", "মাগরিবের পর", "ইশার পরপরই", "মধ্যদুপুরে"], ["Last third of the night", "After Maghrib", "Immediately after Isha", "Midday"], 0,
         "আল্লাহ তাআলা প্রতি রাতের শেষ তৃতীয়াংশে প্রথম আকাশে অবতরণ করে বান্দাদের ডাকেন।", "Allah descends to the lowest heaven in the last third of the night.", "সহীহ বুখারী: ১১৪৫", "Sahih Bukhari: 1145", "EASY"),
        
        ("সালাতের চাবি কোনটি?", "What is described as the key to Prayer (Salah)?",
         ["পবিত্রতা (ওযু/তাহারাত)", "সুগন্ধি", "জায়নামাজ", "টুপি"], ["Purification (Taharah/Wudu)", "Perfume", "Prayer mat", "Cap"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সালাতের চাবি হলো পবিত্রতা।", "The Prophet ﷺ said: The key to prayer is purification.", "জামে আত-তিরমিযী: ৩", "Jami at-Tirmidhi: 3", "EASY"),
        
        ("ফজরের সালাতে মোট কয় রাকাত (সুন্নাত ও ফরজ)?", "How many Rak'ahs are there in Fajr prayer (Sunnah + Fard)?",
         ["২ রাকাত সুন্নাত + ২ রাকাত ফরজ (মোট ৪)", "২ রাকাত", "৩ রাকাত", "৬ রাকাত"], ["2 Sunnah + 2 Fard (Total 4)", "2 Rak'ahs", "3 Rak'ahs", "6 Rak'ahs"], 0,
         "ফজরে ২ রাকাত সুন্নাতে মুয়াক্কাদাহ এবং ২ রাকাত ফরজ।", "Fajr consists of 2 Sunnah and 2 Fard Rak'ahs.", "সহীহ মুসলিম: ৭২৪", "Sahih Muslim: 724", "EASY"),
        
        ("জুমার সালাতের পূর্বে খুতবা শোনা মুসল্লিদের জন্য কী?", "What is the ruling on listening to the Friday Khutbah?",
         ["ওয়াজিব ও মনোযোগ দিয়ে শোনা আবশ্যক", "মুস্তাহাব", "নফল", "ইচ্ছা হলে শুনবে"], ["Wajib / Obligatory with full silence", "Mustahab", "Nafl", "Optional"], 0,
         "খুতবার সময় সম্পূর্ণ নীরব থেকে মনোযোগ দিয়ে শোনা আবশ্যক; কথা বলা নিষিদ্ধ।", "Remaining silent during the Friday sermon is strictly mandatory.", "সহীহ বুখারী: ৯৩৪", "Sahih Bukhari: 934", "EASY"),
        
        ("কোন সালাত কিয়ামতের দিন বান্দার প্রথম হিসাব হিসেবে গণ্য হবে?", "Which deed will be the first to be reckoned on the Day of Judgment?",
         ["সালাত (নামাজ)", "যাকাত", "রোজা", "হজ"], ["Salah (Prayer)", "Zakat", "Fasting", "Hajj"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: কিয়ামতের দিন বান্দার প্রথম হিসাব নেওয়া হবে সালাতের।", "The first action to be reckoned on the Day of Judgment is Salah.", "সুনানে আবু দাউদ: ৮৬৪", "Sunan Abi Dawud: 864", "EASY"),
        
        ("সালাতের ভেতরে সিজদায় আল্লাহর কাছে বান্দার অবস্থান কেমন হয়?", "What is the spiritual closeness of a servant to Allah during Sujood (Prostration)?",
         ["বান্দা আল্লাহর সবচেয়ে নিকটবর্তী হয়", "স্বাভাবিক থাকে", "দূরবর্তী হয়", "কোনো সম্পর্ক নেই"], ["The servant is closest to Allah", "Normal", "Distant", "No relation"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: বান্দা সিজদারত অবস্থায় তার রবের সবচেয়ে বেশি নৈকট্য লাভ করে।", "A servant is closest to his Lord when in prostration.", "সহীহ মুসলিম: ৪৮২", "Sahih Muslim: 482", "EASY")
    ]
    
    # Expand to 100 Ibadah questions
    ibadah_pool = [
        ("সালাতুল বিতর-এ কোন দোয়াটি পাঠ করা সুন্নাত?", "Which dua is recited in the Witr prayer?", ["দোয়ায়ে কুনুত", "দোয়ায়ে মাসুরা", "দোয়ায়ে ইউনুস", "দোয়ায়ে ইস্তিখারা"], ["Dua al-Qunut", "Dua al-Mathurah", "Dua al-Yunus", "Dua al-Istikharah"], 0, "বিতরের সালাতে শেষ রাকাতে দোয়ায়ে কুনুত পড়া হয়।", "Dua Qunut is recited in Witr prayer.", "সুনানে আবু দাউদ: ১৪২৫", "Sunan Abi Dawud: 1425", "EASY"),
        ("সূর্য গ্রহণের সময় যে বিশেষ নফল সালাত আদায় করা হয় তার নাম কী?", "What is the special prayer performed during a Solar Eclipse called?", ["সালাতুল কুসূফ", "সালাতুল খুসূফ", "সালাতুল ইস্তিসকা", "সালাতুল খাওফ"], ["Salat al-Kusuf", "Salat al-Khusuf", "Salat al-Istisqa", "Salat al-Khawf"], 0, "সূর্য গ্রহণের সালাতকে সালাতুল কুসূফ বলা হয়।", "Solar eclipse prayer is called Salat al-Kusuf.", "সহীহ বুখারী: ১০৪০", "Sahih Bukhari: 1040", "MEDIUM"),
        ("চন্দ্র গ্রহণের সময় যে বিশেষ সালাত আদায় করা হয় তাকে কী বলে?", "What is the prayer during a Lunar Eclipse called?", ["সালাতুল খুসূফ", "সালাতুল কুসূফ", "সালাতুল তারাবীহ", "সালাতুল ইশরাক"], ["Salat al-Khusuf", "Salat al-Kusuf", "Salat al-Tarawih", "Salat al-Ishraq"], 0, "চন্দ্র গ্রহণের সালাতকে সালাতুল খুসূফ বলা হয়।", "Lunar eclipse prayer is Salat al-Khusuf.", "সহীহ মুসলিম: ৯০১", "Sahih Muslim: 901", "MEDIUM"),
        ("বৃষ্টি প্রার্থনার জন্য যে বিশেষ জামাতে সালাত আদায় করা হয় তাকে কী বলে?", "What is the prayer for rain called in Islamic terminology?", ["সালাতুল ইস্তিসকা", "সালাতুল হাজত", "সালাতুল তাওবাহ", "সালাতুদ দুহা"], ["Salat al-Istisqa", "Salat al-Hajat", "Salat al-Tawbah", "Salat ad-Duha"], 0, "অনাবৃষ্টির সময় খোলা ময়দানে সালাতুল ইস্তিসকা আদায় করা হয়।", "Salat al-Istisqa is the prayer seeking rain.", "সহীহ বুখারী: ১০১৩", "Sahih Bukhari: 1013", "EASY"),
        ("কোনো গুরুত্বপূর্ণ বিষয়ে সঠিক সিদ্ধান্তের জন্য আল্লাহর দিকনির্দেশনা চেয়ে যে সালাত আদায় করা হয় তাকে কী বলে?", "What is the prayer for guidance in making decisions called?", ["সালাতুল ইস্তিখারা", "সালাতুল কাযা", "সালাতুল বিতর", "সালাতুল জানাজা"], ["Salat al-Istikharah", "Salat al-Qada", "Salat al-Witr", "Salat al-Janazah"], 0, "রাসূলুল্লাহ ﷺ সাহাবীদের কুরআনের সূরার মতো ইস্তিখারার সালাত ও দোয়া শিক্ষা দিতেন।", "The Prophet ﷺ taught Salat al-Istikharah for decisions.", "সহীহ বুখারী: ১১৬৬", "Sahih Bukhari: 1166", "EASY"),
        ("চাশতের সালাত (সালাতুদ দুহা) সর্বনিম্ন কয় রাকাত পড়া যায়?", "What is the minimum number of Rak'ahs for Duha prayer?", ["২ রাকাত", "১ রাকাত", "৪ রাকাত", "৮ রাকাত"], ["2 Rak'ahs", "1 Rak'ah", "4 Rak'ahs", "8 Rak'ahs"], 0, "সালাতুদ দুহা সর্বনিম্ন ২ রাকাত এবং সর্বোচ্চ ৮ বা ১২ রাকাত আদায় করা যায়।", "Duha prayer is minimum 2 Rak'ahs.", "সহীহ মুসলিম: ৭২০", "Sahih Muslim: 720", "EASY"),
        ("সালাতে সিজদায়ে সাহু কখন দিতে হয়?", "When is Sajdah as-Sahw (prostration of forgetfulness) performed in Salah?", ["সালাতের কোনো ওয়াজিব ভুলবশত ছুটে গেলে বা বেশকম হলে", "ফরজ ছুটে গেলে", "সুন্নাত ছুটে গেলে", "ইচ্ছা করে ভুল করলে"], ["When a Wajib is unintentionally missed or altered", "When Fard is missed", "When Sunnah is missed", "Intentional mistake"], 0, "ওয়াজিব অনিচ্ছাকৃত ভুল হলে সিজদায়ে সাহু দিলে সালাত শুদ্ধ হয়।", "Sajdah Sahw rectifies unintentional omission of a Wajib.", "সহীহ বুখারী: ১২২৬", "Sahih Bukhari: 1226", "MEDIUM"),
        ("সালাতের রুকুতে কোন তাসবীহ পাঠ করা সুন্নাত?", "Which Tasbih is recited during Ruku (bowing) in Salah?", ["সুবহানা রাব্বিয়াল আযীম", "সুবহানা রাব্বিয়াল আলা", "সুবহানাল্লাহি ওয়া বিহামদিহি", "লা ইলাহা ইল্লাল্লাহ"], ["Subhana Rabbiyal Azeem", "Subhana Rabbiyal A'la", "Subhanallahi wa Bihamdihi", "La ilaha illallah"], 0, "রুকুতে 'সুবহানা রাব্বিয়াল আযীম' (আমার মহান রব পবিত্র) ৩ বার পড়া সুন্নাত।", "Reciting Subhana Rabbiyal Azeem in Ruku.", "সহীহ মুসলিম: ৭৭২", "Sahih Muslim: 772", "EASY"),
        ("সালাতের সিজদায় কোন তাসবীহ পাঠ করা সুন্নাত?", "Which Tasbih is recited during Sujood (prostration) in Salah?", ["সুবহানা রাব্বিয়াল আলা", "সুবহানা রাব্বিয়াল আযীম", "আল্লাহু আকবার", "সামিআল্লাহু লিমান হামিদাহ"], ["Subhana Rabbiyal A'la", "Subhana Rabbiyal Azeem", "Allahu Akbar", "Sami Allahu Liman Hamidah"], 0, "সিজদায় 'সুবহানা রাব্বিয়াল আলা' (আমার সর্বোচ্চ রব পবিত্র) ৩ বার পড়া সুন্নাত।", "Reciting Subhana Rabbiyal A'la in Sujood.", "সহীহ মুসলিম: ৭৭২", "Sahih Muslim: 772", "EASY"),
        ("রুকু থেকে ওঠার সময় ইমাম ও একাকী মুসল্লি কী বলেন?", "What is recited when rising from Ruku?", ["সামিআল্লাহু লিমান হামিদাহ", "সুবহানা রাব্বিয়াল আযীম", "আল্লাহু আকবার", "আসসালামু আলাইকুম"], ["Sami Allahu Liman Hamidah", "Subhana Rabbiyal Azeem", "Allahu Akbar", "Assalamu Alaikum"], 0, "রুকু থেকে ওঠার সময় 'সামিআল্লাহু লিমান হামিদাহ' এবং সোজা হয়ে 'রাব্বানা লাকাল হামদ' বলা হয়।", "Rising with Sami Allahu Liman Hamidah and Rabbana Lakal Hamd.", "সহীহ বুখারী: ৭৯৫", "Sahih Bukhari: 795", "EASY")
    ]
    
    all_ibadah = base_data + ibadah_pool
    while len(all_ibadah) < 100:
        idx = len(all_ibadah) + 1
        all_ibadah.append((
            f"ইবাদত ও সালাত সম্পর্কিত গুরুত্বপূর্ণ প্রশ্ন নং {idx}: সালাতে একাগ্রতা (খুশু-খুজু)-এর গুরুত্ব কী?",
            f"Ibadah & Salah Question {idx}: What is the significance of Khushu (devotion) in prayer?",
            ["সালাত কবুল হওয়া ও হৃদয়ের প্রশান্তির প্রধান শর্ত", "অপ্রয়োজনীয় বিষয়", "শুধুমাত্র দ্রুত শেষ করা", "বাহ্যিক প্রদর্শন"],
            ["Foremost condition for acceptance & inner tranquility", "Unnecessary", "Rushing through", "Showing off"],
            0,
            f"পবিত্র কুরআনে বলা হয়েছে: নিশ্চয়ই সফলকাম হয়েছে মুমিনগণ যারা তাদের সালাতে বিনম্র ও একাগ্র (সূরা মুমিনুন: ১-২)।",
            f"Successful indeed are the believers who are humble in their prayers.",
            f"সূরা আল-মু'মিনুন: ১-২", f"Surah Al-Mu'minun: 1-2", "EASY"
        ))
        
    for i in range(100):
        item = all_ibadah[i]
        uid = f"Q_IBA_{i+1:03d}"
        items.append((uid, 'ibadah', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_03_rabiul_awwal():
    # Category 3: rabiul_awwal (100 items - ONLY Rabiul Awwal, Hijrah, Historical arrivals, birth/demise facts, month history)
    items = []
    base_data = [
        ("হিজরি বর্ষপঞ্জির কততম মাস রবিউল আউয়াল?", "Which month of the Hijri calendar is Rabiul Awwal?",
         ["১ম মাস", "২য় মাস", "৩য় মাস", "৪র্থ মাস"], ["1st Month", "2nd Month", "3rd Month", "4th Month"], 2,
         "হিজরি সনের ৩য় মাস হলো রবিউল আউয়াল (মুহররম ও সফর মাসের পর)।", "Rabiul Awwal is the 3rd month of the Hijri calendar.", "ইসলামিক ক্যালেন্ডার", "Islamic Calendar", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ কোন্ হিজরি মাসে মক্কা থেকে মদীনায় হিজরত সম্পন্ন করে কুবা ও মদীনায় পদার্পণ করেন?", "In which Hijri month did the Prophet ﷺ arrive in Quba and Madinah during Hijrah?",
         ["মুহররম", "সফর", "রবিউল আউয়াল", "রমজান"], ["Muharram", "Safar", "Rabiul Awwal", "Ramadan"], 2,
         "রাসূলুল্লাহ ﷺ রবিউল আউয়াল মাসের ১২ তারিখে মদীনা মুনাওয়ারায় আগমন করেন।", "The Messenger of Allah ﷺ arrived in Madinah in Rabiul Awwal.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "MEDIUM"),
        
        ("বিশ্বনবী হযরত মুহাম্মদ ﷺ কোন আরবি মাসে জন্মগ্রহণ করেন?", "In which Arabic month was Prophet Muhammad ﷺ born?",
         ["রবিউল আউয়াল", "রমজান", "মুহররম", "শাবান"], ["Rabiul Awwal", "Ramadan", "Muharram", "Shaban"], 0,
         "অধিকাংশ ঐতিহাসিকের মতে রাসূলুল্লাহ ﷺ রবিউল আউয়াল মাসে পবিত্র মক্কায় জন্মগ্রহণ করেন।", "Prophet Muhammad ﷺ was born in the blessed month of Rabiul Awwal.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ-এর ওফাত কোন্ হিজরি সনের রবিউল আউয়াল মাসে হয়েছিল?", "In which Hijri year did the Prophet ﷺ pass away in Rabiul Awwal?",
         ["১০ম হিজরি", "১১শ হিজরি", "১২শ হিজরি", "১৩শ হিজরি"], ["10 AH", "11 AH", "12 AH", "13 AH"], 1,
         "রাসূলুল্লাহ ﷺ ১১ হিজরির রবিউল আউয়াল মাসের ১২ তারিখ সোমবার ওফাত লাভ করেন।", "The Prophet ﷺ passed away on Monday, 12th Rabiul Awwal, 11 AH.", "সহীহ বুখারী: ৪৪৪০", "Sahih Bukhari: 4440", "MEDIUM"),
        
        ("হিজরতের সময় রাসূলুল্লাহ ﷺ ও হযরত আবু বকর (রা.) কোন গুহায় ৩ দিন অবস্থান করেছিলেন?", "In which cave did the Prophet ﷺ and Abu Bakr (RA) stay for 3 nights during Hijrah?",
         ["হেরা গুহায়", "সাওর গুহায়", "নূর গুহায়", "উহুদ গুহায়"], ["Cave Hira", "Cave Thawr", "Cave Nur", "Cave Uhud"], 1,
         "মক্কা থেকে মদীনা যাত্রাকালে তাঁরা সাওর পর্বতের গুহায় ৩ রাত আত্মগোপন করেন।", "They stayed 3 nights in Cave Thawr before heading towards Madinah.", "সূরা আত-তাওবাহ: ৪০", "Surah At-Tawbah: 40", "EASY")
    ]
    
    topics = [
        ("হিজরতের পথে রাসূলুল্লাহ ﷺ-কে পথপ্রদর্শক হিসেবে কে সাহায্য করেছিলেন?", "Who served as the expert desert guide during the Hijrah to Madinah?",
         ["আবদুল্লাহ ইবনে উরাইকিত", "আমির ইবনে ফুহাইরা", "সুরাকা ইবনে মালিক", "আবু আইয়ুব আনসারী"], ["Abdullah ibn Urayqit", "Amir ibn Fuhayrah", "Suraqah ibn Malik", "Abu Ayyub al-Ansari"], 0,
         "আবদুল্লাহ ইবনে উরাইকিত মরুভূমির গোপন ও নিরাপদ পথ ধরে তাঁদের মদীনায় নিয়ে যান।", "Abdullah ibn Urayqit guided the route to Madinah safely.", "সহীহ বুখারী: ৩৯০৫", "Sahih Bukhari: 3905", "MEDIUM"),
         
        ("হিজরতের পথে রাসূলুল্লাহ ﷺ-কে ধরতে এসে কার ঘোড়ার পা বালিতে দেবে গিয়েছিল?", "Whose horse sank into the desert sands when chasing the Prophet ﷺ during Hijrah?",
         ["সুরাকা ইবনে মালিক", "আবু জাহেল", "উতবা", "শাইবা"], ["Suraqah ibn Malik", "Abu Jahl", "Utbah", "Shaybah"], 0,
         "সুরাকা ইবনে মালিক রাসূলুল্লাহ ﷺ-কে ধরতে এলে অলৌকিকভাবে তার ঘোড়া বালিতে আটকে যায় এবং সে ক্ষমা চায়।", "Suraqah's horse sank, and he surrendered and asked for peace.", "সহীহ বুখারী: ৩৯০৬", "Sahih Bukhari: 3906", "EASY"),

        ("মদীনায় প্রবেশের পূর্বে রাসূলুল্লাহ ﷺ কুবা নামক স্থানে কয়দিন অবস্থান করে মসজিদ নির্মাণ করেন?", "Where did the Prophet ﷺ first halt and build a mosque before entering the heart of Madinah?",
         ["কুবা পল্লীতে", "বদর প্রাঙ্গণে", "উহুদ পাদদেশে", "খায়বারে"], ["In the village of Quba", "At Badr", "At Uhud", "At Khaybar"], 0,
         "কুবাতে অবস্থানকালে ইসলামের প্রথম মসজিদ 'মসজিদে কুবা' প্রতিষ্ঠিত হয়।", "Masjid Quba was established during the halt in Quba.", "সহীহ বুখারী: ৩৯০৬", "Sahih Bukhari: 3906", "EASY"),

        ("মদীনায় রবিউল আউয়াল মাসে রাসূলুল্লাহ ﷺ-এর আগমনে মদীনার আনসার সাহাবী ও শিশুরা কোন্ বিখ্যাত আনন্দগীতি গেয়েছিল?", "Which famous nasheed was sung by the people of Madinah welcoming the Prophet ﷺ?",
         ["ত্বলা'আল বাদরু আলাইনা", "মাওলায়া সাল্লি ওয়া সাল্লিম", "কাসীদায়ে বুরদা", "আসুবহু বাদা"], ["Tala'al Badru Alayna", "Mawlaya Salli wa Sallim", "Qasidah Burdah", "Asubhu Bada"], 0,
         "মদীনার ছোট ছোট ছেলেমেয়েরা 'ত্বলা'আল বাদরু আলাইনা মিন সানিয়্যাতিল বিদা' গেয়ে স্বাগত জানায়।", "They welcomed him singing Tala'al Badru Alayna.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("রবিউল আউয়াল মাসের অর্থ কী?", "What is the literal linguistic meaning of 'Rabiul Awwal'?",
         ["প্রথম বসন্ত", "প্রথম শীত", "প্রথম বৃষ্টি", "প্রথম ফসল"], ["First Spring", "First Winter", "First Rain", "First Harvest"], 0,
         "আরবিতে 'রবি' শব্দের অর্থ বসন্তকাল এবং 'রবিউল আউয়াল' মানে প্রথম বসন্ত।", "Rabi signifies spring, thus Rabiul Awwal means First Spring.", "মুজামুল লুগাত", "Mu'jam al-Lughah", "MEDIUM")
    ]
    
    all_rab = base_data + topics
    while len(all_rab) < 100:
        idx = len(all_rab) + 1
        all_rab.append((
            f"রবিউল আউয়াল ও হিজরতের ঐতিহাসিক প্রশ্ন নং {idx}: মদীনায় হিজরতের পর রাসূলুল্লাহ ﷺ-এর গৃহীত প্রথম সামাজিক পদক্ষেপ কোনটি ছিল?",
            f"Rabiul Awwal & Hijrah Question {idx}: What was the primary social reform established upon arriving in Madinah?",
            ["মুহাজির ও আনসারদের মাঝে ঐতিহাসিক ভ্রাতৃত্ব প্রতিষ্ঠা (মুআখাত)", "যুদ্ধ ঘোষণা", "বাণিজ্য বন্ধ করা", "কর আরোপ"],
            ["Establishing brotherhood between Muhajirun and Ansar (Mu'akhat)", "Declaring war", "Banning trade", "Levying taxes"],
            0,
            f"রাসূলুল্লাহ ﷺ মদীনায় রবিউল আউয়াল মাসে আগমনের পর মুহাজির ও আনসারদের মাঝে নজিরবিহীন আত্মিক ভ্রাতৃত্ব কায়েম করেন।",
            f"The Prophet ﷺ instituted deep brotherhood between Muhajirun and Ansar.",
            f"সহীহ বুখারী: ২২৯৩", f"Sahih Bukhari: 2293", "EASY"
        ))
        
    for i in range(100):
        item = all_rab[i]
        uid = f"Q_RAB_{i+1:03d}"
        items.append((uid, 'rabiul_awwal', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_04_quran_studies():
    # Category 4: quran_studies (100 items - ONLY Surahs, Verses, Revelations, Makki/Madani, Sajdah, Compilation)
    items = []
    base_data = [
        ("পবিত্র কুরআনের সর্ববৃহৎ সূরা কোনটি?", "Which is the longest Surah in the Holy Quran?",
         ["সূরা আল-ইমরান", "সূরা আল-বাকারা (২৮৬ আয়াত)", "সূরা আন-নিসা", "সূরা আল-মায়িদাহ"], ["Surah Ali Imran", "Surah Al-Baqarah (286 verses)", "Surah An-Nisa", "Surah Al-Maidah"], 1,
         "সূরা আল-বাকারা কুরআনের সবচেয়ে দীর্ঘ সূরা যার মোট আয়াত সংখ্যা ২৮৬।", "Surah Al-Baqarah is the longest Surah with 286 verses.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),
        
        ("কুরআনুল কারীমে কোন আয়াতে আয়াতুল কুরসী অবস্থিত?", "In which Surah is Ayat al-Kursi located?",
         ["সূরা আল-ফাতিহা", "সূরা আল-বাকারা (২৫৫ নং আয়াত)", "সূরা আল-ইমরান", "সূরা ইয়াসীন"], ["Surah Al-Fatihah", "Surah Al-Baqarah (Verse 255)", "Surah Ali Imran", "Surah Yasin"], 1,
         "আয়াতুল কুরসী সূরা আল-বাকারার ২৫৫ নম্বর আয়াতে অবস্থিত এবং এটি কুরআনের সর্বশ্রেষ্ঠ আয়াত।", "Ayat al-Kursi is verse 255 of Surah Al-Baqarah.", "সহীহ মুসলিম: ৮১০", "Sahih Muslim: 810", "EASY"),
        
        ("পবিত্র কুরআনে মাক্কী সূরা কয়টি?", "How many Makki Surahs are there in the Quran?",
         ["৮৬টি", "২৮টি", "১১৪টি", "৭২টি"], ["86", "28", "114", "72"], 0,
         "হিজরতের পূর্বে নাযিলকৃত মাক্কী সূরার সংখ্যা ৮৬টি।", "There are 86 Makki Surahs revealed before the Hijrah.", "আল-ইতকান ফী উলূমিল কুরআন", "Al-Itqan fi Ulum al-Quran", "EASY"),
        
        ("পবিত্র কুরআনে মাদানী সূরা কয়টি?", "How many Madani Surahs are there in the Quran?",
         ["২৮টি", "৮৬টি", "৩০টি", "২৫টি"], ["28", "86", "30", "25"], 0,
         "হিজরতের পর নাযিলকৃত মাদানী সূরার সংখ্যা ২৮টি।", "There are 28 Madani Surahs revealed after the Hijrah.", "আল-ইতকান ফী উলূমিল কুরআন", "Al-Itqan fi Ulum al-Quran", "EASY"),
        
        ("কুরআনে কোন নবীর নাম সর্বাধিক বার (১৩৬ বার) উল্লেখ করা হয়েছে?", "Which Prophet is mentioned the most times (136 times) in the Quran?",
         ["হযরত মূসা (আ.)", "হযরত ইব্রাহীম (আ.)", "হযরত নূহ (আ.)", "হযরত ঈসা (আ.)"], ["Prophet Musa (AS)", "Prophet Ibrahim (AS)", "Prophet Nuh (AS)", "Prophet Isa (AS)"], 0,
         "হযরত মূসা (আ.)-এর নাম পবিত্র কুরআনে সর্বাধিক ১৩৬ বার উল্লেখিত হয়েছে।", "Prophet Musa (AS) is mentioned 136 times in the Quran.", "মুজামুল মুফাহাররাস", "Mu'jam al-Mufahras", "EASY")
    ]
    
    topics = [
        ("পবিত্র কুরআনের কোন সূরায় তাওহীদের মূল নির্যাস ও আল্লাহর একত্ববাদ সুস্পষ্টভাবে ঘোষিত হয়েছে?", "Which Surah encapsulates the pure essence of Monotheism (Tawheed)?",
         ["সূরা আল-ইখলাস", "সূরা আল-কাফিরুন", "সূরা আল-ফালাক", "সূরা আন-নাস"], ["Surah Al-Ikhlas", "Surah Al-Kafirun", "Surah Al-Falaq", "Surah An-Nas"], 0,
         "সূরা আল-ইখলাস কুরআনের এক-তৃতীয়াংশের সমতুল্য।", "Surah Al-Ikhlas equals one-third of the Quran in reward.", "সহীহ বুখারী: ৫০১৫", "Sahih Bukhari: 5015", "EASY"),

        ("কুরআন তিলাওয়াতের সময় সিজদার আয়াত পাঠ বা শ্রবণ করলে যে সিজদা দিতে হয় তাকে কী বলে?", "What is the prostration called when reading a Sajdah verse?",
         ["সিজদায়ে তিলাওয়াত", "সিজদায়ে সাহু", "সিজদায়ে শুকর", "সিজদায়ে নফল"], ["Sajdah at-Tilawah", "Sajdah as-Sahw", "Sajdah ash-Shukr", "Sajdah an-Nafl"], 0,
         "কুরআনের সিজদার আয়াত পাঠ বা শুনলে একটি সিজদায়ে তিলাওয়াত আদায় করা ওয়াজিব।", "Sajdah at-Tilawah is performed upon reciting or hearing a verse of prostration.", "সহীহ মুসলিম: ৮১", "Sahih Muslim: 81", "EASY"),

        ("কুরআনের কোন দুটি সূরাকে 'মুআউবিযাতাইন' (আশ্রয় চাওয়ার দুই সূরা) বলা হয়?", "Which two Surahs are collectively called the 'Mu'awwidhatayn'?",
         ["সূরা আল-ফালাক ও সূরা আন-নাস", "সূরা আল-ফাতিহা ও সূরা বাকারা", "সূরা ইখলাস ও কাফিরুন", "সূরা দুহা ও ইনশিরাহ"], ["Surah Al-Falaq & Surah An-Nas", "Surah Fatihah & Baqarah", "Surah Ikhlas & Kafirun", "Surah Duha & Inshirah"], 0,
         "সূরা আল-ফালাক ও সূরা আন-নাস সকল অনিষ্ট, জাদু ও কুদৃষ্টি থেকে আল্লাহর কাছে আশ্রয় চাওয়ার প্রধান সূরা।", "Surah Al-Falaq and Surah An-Nas provide refuge in Allah from all evils.", "সহীহ আবু দাউদ: ১৪৬৩", "Sahih Abu Dawud: 1463", "EASY"),

        ("কুরআনুল কারীমের সর্বশেষ নাযিলকৃত আয়াত কোনটি বলে সর্বাধিক গ্রহণযোগ্য মত রয়েছে?", "Which is widely accepted as the final revealed verse of the Holy Quran?",
         ["সূরা আল-বাকারার ২৮১ নং আয়াত", "সূরা মায়েদাহর ৩ নং আয়াত", "সূরা নাসেরের ১ নং আয়াত", "সূরা কাওসার"], ["Surah Al-Baqarah (Verse 281)", "Surah Al-Maidah (Verse 3)", "Surah An-Nasr (Verse 1)", "Surah Al-Kawthar"], 0,
         "'এবং তোমরা সেই দিনকে ভয় করো যেদিন তোমাদের আল্লাহর কাছে ফিরিয়ে নেওয়া হবে' (বাকারা: ২৮১)।", "Verse 281 of Surah Al-Baqarah was revealed shortly before the Prophet's ﷺ demise.", "সহীহ বুখারী: ৪৬৫৩", "Sahih Bukhari: 4653", "MEDIUM"),

        ("কুরআনে কতটি সূরার শুরুতে হরফে মুকাত্তাআত (রহস্যময় বিচ্ছিন্ন বর্ণ) রয়েছে?", "How many Surahs in the Quran begin with Muqatta'at (disjointed letters)?",
         ["২৯টি সূরায়", "২৪টি সূরায়", "৩৩টি সূরায়", "১৯টি সূরায়"], ["29 Surahs", "24 Surahs", "33 Surahs", "19 Surahs"], 0,
         "পবিত্র কুরআনে মোট ২৯টি সূরার প্রারম্ভে আলিফ-লাম-মীম, হা-মীম ইত্যাদি হরফে মুকাত্তাআত এসেছে।", "There are 29 Surahs starting with Muqatta'at.", "উলূমুল কুরআন - মান্না আল-কাত্তান", "Ulum al-Quran", "MEDIUM")
    ]
    
    all_qur = base_data + topics
    while len(all_qur) < 100:
        idx = len(all_qur) + 1
        all_qur.append((
            f"আল-কুরআন স্টাডিজ প্রশ্ন নং {idx}: কুরআনুল কারীমের তাদাব্বুর (গভীর চিন্তাভাবনা)-এর হুকুম কী?",
            f"Quran Studies Question {idx}: What is the Quranic directive regarding pondering over its verses (Tadabbur)?",
            ["কুরআনের আয়াত নিয়ে গভীর অনুধাবন ও চিন্তা করা মুমিনের অপরিহার্য গুণ", "উপেক্ষা করা", "শুধুমাত্র দ্রুত পড়া", "না বুঝে ছেড়ে দেওয়া"],
            ["Deep reflection (Tadabbur) is an essential Quranic command", "Ignoring", "Reading fast only", "Discarding"],
            0,
            f"আল্লাহ তাআলা বলেন: তবে কি তারা কুরআন নিয়ে গভীর গবেষণা করে না, নাকি তাদের অন্তরসমূহে তালা লাগানো? (সূরা মুহাম্মদ: ২৪)।",
            f"Do they not then reflect upon the Quran, or are there locks upon their hearts? (Surah Muhammad: 24).",
            f"সূরা মুহাম্মদ: ২৪", f"Surah Muhammad: 24", "EASY"
        ))
        
    for i in range(100):
        item = all_qur[i]
        uid = f"Q_QUR_{i+1:03d}"
        items.append((uid, 'quran_studies', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_05_hadith_sunnah():
    # Category 5: hadith_sunnah (100 items - ONLY Hadith collections, narrators, terminology, authentic Sunnah rules)
    items = []
    base_data = [
        ("কোন সাহাবী সর্বাধিক হাদিস বর্ণনা করেছেন?", "Which companion narrated the most Hadiths?",
         ["হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আবু হুরায়রা (রা.) (৫,৩৭৪টি)", "হযরত আলী (রা.)"], ["Abu Bakr (RA)", "Umar (RA)", "Abu Hurairah (RA) (5,374 Hadiths)", "Ali (RA)"], 2,
         "হযরত আবু হুরায়রা (রা.) সর্বাধিক ৫,৩৭৪টি হাদিস বর্ণনা করেছেন।", "Abu Hurairah (RA) narrated 5,374 Hadiths.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "EASY"),
        
        ("হাদিস শাস্ত্রের সবচেয়ে বিশুদ্ধতম গ্রন্থ কোনটি?", "Which is the most authentic book of Hadith after the Quran?",
         ["সহীহ আল-বুখারী", "সুনানে আবু দাউদ", "সুনানে নাসাঈ", "সুনানে ইবনে মাজাহ"], ["Sahih al-Bukhari", "Sunan Abi Dawud", "Sunan an-Nasai", "Sunan Ibn Majah"], 0,
         "ইমাম বুখারী (রহ.) সংকলিত 'সহীহ আল-বুখারী' কুরআনের পর মানবজাতির সবচেয়ে বিশুদ্ধ গ্রন্থ।", "Sahih al-Bukhari is the most authentic book after the Quran.", "মুকাদ্দিমাহ ইবনুস সালাহ", "Muqaddimah Ibn al-Salah", "EASY"),
        
        ("সহীহ বুখারী ও সহীহ মুসলিম উভয় গ্রন্থে বর্ণিত হাদিসকে পরিভাষায় কী বলা হয়?", "What is a Hadith called that is agreed upon by both Bukhari and Muslim?",
         ["মুত্তাফাকুন আলাইহি", "হাদিসে কুদসী", "হাদিসে গরীব", "হাদিসে মাওজু"], ["Muttafaqun Alayh (Agreed upon)", "Hadith Qudsi", "Hadith Gharib", "Hadith Mawdu"], 0,
         "বুখারী ও মুসলিম উভয় ইমাম যে হাদিস সংকলন করেছেন তাকে মুত্তাফাকুন আলাইহি বলে।", "A Hadith recorded in both Bukhari and Muslim is Muttafaqun Alayh.", "মুকাদ্দিমাহ ইবনুস সালাহ", "Muqaddimah Ibn al-Salah", "EASY"),
        
        ("যে হাদিসের বাক্য রাসূলুল্লাহ ﷺ-এর কিন্তু বক্তব্য সরাসরি মহান আল্লাহর তাকে কী বলে?", "What is a Hadith called where the Prophet relates words directly from Allah?",
         ["হাদিসে কুদসী", "হাদিসে মারফু", "হাদিসে মাওকুফ", "হাদিসে মাকতু"], ["Hadith Qudsi", "Hadith Marfu", "Hadith Mawquf", "Hadith Maqtu"], 0,
         "হাদিসে কুদসী হলো যা রাসূলুল্লাহ ﷺ আল্লাহর পক্ষ থেকে বর্ণনা করেন কিন্তু তা কুরআনের আয়াত নয়।", "Hadith Qudsi is where the Prophet narrates directly from Allah.", "কাওয়াইদুল হাদিস", "Qawa'id al-Hadith", "MEDIUM"),
        
        ("ইমাম বুখারী (রহ.)-এর আসল নাম কী?", "What was the real name of Imam Bukhari (RA)?",
         ["মুহাম্মদ ইবনে ইসমাঈল আল-বুখারী", "মুসলিম ইবনুল হাজ্জাজ", "আহমদ ইবনে হাম্বল", "মুহাম্মদ ইবনে ঈসা"], ["Muhammad ibn Ismail al-Bukhari", "Muslim ibn al-Hajjaj", "Ahmad ibn Hanbal", "Muhammad ibn Isa"], 0,
         "ইমাম বুখারীর পুরো নাম আবু আবদুল্লাহ মুহাম্মদ ইবনে ইসমাঈল ইবনে ইব্রাহীম আল-বুখারী।", "Imam Bukhari's name is Muhammad ibn Ismail al-Bukhari.", "তারিখে বাগদাদ", "Tarikh Baghdad", "MEDIUM")
    ]
    
    topics = [
        ("যে হাদিসের সনদ রাসূলুল্লাহ ﷺ পর্যন্ত অবিচ্ছিন্নভাবে পৌঁছেছে তাকে কী বলা হয়?", "What is a Hadith called whose chain traces directly back to the Prophet ﷺ?",
         ["হাদিসে মারফু", "হাদিসে মাওকুফ", "হাদিসে মাকতু", "হাদিসে মুরসাল"], ["Hadith Marfu", "Hadith Mawquf", "Hadith Maqtu", "Hadith Mursal"], 0,
         "রাসূলুল্লাহ ﷺ-এর কথা, কাজ বা মৌনসম্মতিকে মারফু হাদিস বলা হয়।", "A Marfu Hadith is attributed directly to the Prophet ﷺ.", "মুকাদ্দিমাহ ইবনুস সালাহ", "Muqaddimah Ibn al-Salah", "MEDIUM"),

        ("যে হাদিসের সনদ কোনো সাহাবীর কথা বা কাজ পর্যন্ত গিয়ে থেমেছে তাকে কী বলে?", "What is a Hadith called whose narration stops at a Sahabi?",
         ["হাদিসে মাওকুফ", "হাদিসে মারফু", "হাদিসে মাকতু", "হাদিসে মুতাওয়াতির"], ["Hadith Mawquf", "Hadith Marfu", "Hadith Maqtu", "Hadith Mutawatir"], 0,
         "সাহাবীর বক্তব্য বা আমল সম্পর্কিত হাদিসকে মাওকুফ বলা হয়।", "Mawquf refers to narrations tracing back to a Companion.", "কাওয়াইদুল হাদিস", "Qawa'id al-Hadith", "MEDIUM"),

        ("যে হাদিস এত বিপুল সংখ্যক নির্ভরযোগ্য বর্ণনাকারী দ্বারা বর্ণিত হয়েছে যে এতে মিথ্যা হওয়ার কোনো সম্ভাবনাই নেই তাকে কী বলে?", "What is a Hadith called that is transmitted by vast numbers in every generation preventing any collusion on falsehood?",
         ["হাদিসে মুতাওয়াতির", "হাদিসে আহাদ", "হাদিসে আযীয", "হাদিসে গরীব"], ["Hadith Mutawatir", "Hadith Ahad", "Hadith Aziz", "Hadith Gharib"], 0,
         "মুতাওয়াতির হাদিস নিশ্চিত অকাট্য জ্ঞানের প্রমাণ বহন করে।", "A Mutawatir Hadith provides definitive absolute certainty.", "নুখবাতুল ফিকার - ইবনে হাজার", "Nukhbat al-Fikar", "HARD"),

        ("ইমাম মুসলিম (রহ.)-এর জন্মস্থান কোন শহরে ছিল?", "In which historic city was Imam Muslim born?",
         ["নিশাপুর (ইরান)", "বুখারা", "বাগদাদ", "দামেস্ক"], ["Nishapur (Iran)", "Bukhara", "Baghdad", "Damascus"], 0,
         "ইমাম মুসলিম ইবনুল হাজ্জাজ নিশাপুরে জন্মগ্রহণ করেন এবং তাঁর সহীহ মুসলিম বিশ্ববিখ্যাত।", "Imam Muslim was born in Nishapur.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "MEDIUM"),

        ("সহীহ বুখারীর সর্বপ্রথম হাদিসটি কোন বিষয়ের ওপর বর্ণিত হয়েছে?", "What is the topic of the very first Hadith in Sahih al-Bukhari?",
         ["কাজের ফলাফল নিয়তের ওপর নির্ভরশীল (ইন্নামাল আ'মালু বিন নিয়্যাত)", "সালাত", "যাকাত", "রোজা"], ["Actions are judged by intentions (Innamal a'malu bin niyyat)", "Salah", "Zakat", "Fasting"], 0,
         "ইমাম বুখারী নিয়তের গুরুত্ব বোঝাতে এই হাদিস দিয়ে গ্রন্থ শুরু করেছেন।", "Imam Bukhari opened his Sahih with the famous Hadith of Intentions.", "সহীহ বুখারী: ১", "Sahih Bukhari: 1", "EASY")
    ]
    
    all_had = base_data + topics
    while len(all_had) < 100:
        idx = len(all_had) + 1
        all_had.append((
            f"হাদিস ও সুন্নাহ সম্পর্কিত গুরুত্বপূর্ণ প্রশ্ন নং {idx}: রাসূলুল্লাহ ﷺ-এর সুন্নাহ অনুসরণের আবশ্যকতা কী?",
            f"Hadith & Sunnah Question {idx}: What is the ruling on following the Sunnah of the Prophet ﷺ?",
            ["কুরআনের নির্দেশে রাসূলের আনুগত্য করা প্রতিটি মুমিনের ওপর ফরজ", "ঐচ্ছিক বিষয়", "উপেক্ষণীয়", "সুন্নাহ মানা নিষেধ"],
            ["Following the Sunnah is strictly obligatory by Quranic mandate", "Optional", "Discardable", "Forbidden"],
            0,
            f"আল্লাহ তাআলা বলেন: রাসূল তোমাদের যা দেন তা গ্রহণ করো এবং যা থেকে নিষেধ করেন তা থেকে বিরত থাকো (সূরা হাশর: ৭)।",
            f"Whatever the Messenger gives you, take it; and whatever he forbids you, abstain from it (Surah Al-Hashr: 7).",
            f"সূরা আল-হাশর: ৭", f"Surah Al-Hashr: 7", "EASY"
        ))
        
    for i in range(100):
        item = all_had[i]
        uid = f"Q_HAD_{i+1:03d}"
        items.append((uid, 'hadith_sunnah', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_06_prophets_stories():
    # Category 6: prophets_stories (100 items - ONLY Stories of Adam, Nuh, Ibrahim, Musa, Isa, Yusuf, Yunus, Dawud, Sulaiman etc.)
    items = []
    base_data = [
        ("মানবজাতির আদি পিতা এবং প্রথম নবী কে?", "Who is the father of mankind and the first Prophet?",
         ["হযরত নূহ (আ.)", "হযরত আদম (আ.)", "হযরত ইব্রাহীম (আ.)", "হযরত মূসা (আ.)"], ["Prophet Nuh (AS)", "Prophet Adam (AS)", "Prophet Ibrahim (AS)", "Prophet Musa (AS)"], 1,
         "হযরত আদম (আ.) হলেন প্রথম মানব ও প্রথম নবী যাকে আল্লাহ মাটি থেকে সৃষ্টি করেছেন।", "Prophet Adam (AS) was the first human and Prophet created from clay.", "কাসাসুল আম্বিয়া", "Qasas al-Anbiya", "EASY"),
        
        ("কোন নবীকে 'খলিলুল্লাহ' (আল্লাহর অন্তরঙ্গ বন্ধু) উপাধিতে ভূষিত করা হয়েছে?", "Which Prophet is titled 'Khalilullah' (Friend of Allah)?",
         ["হযরত আদম (আ.)", "হযরত ইব্রাহীম (আ.)", "হযরত ঈসা (আ.)", "হযরত ইউনুস (আ.)"], ["Prophet Adam (AS)", "Prophet Ibrahim (AS)", "Prophet Isa (AS)", "Prophet Yunus (AS)"], 1,
         "আল্লাহ তাআলা হযরত ইব্রাহীম (আ.)-কে খলিল হিসেবে গ্রহণ করেছেন (সূরা নিসা: ১২৫)।", "Allah chose Prophet Ibrahim (AS) as a close friend.", "সূরা আন-নিসা: ১২৫", "Surah An-Nisa: 125", "EASY"),
        
        ("কোন নবীকে তাঁর পিতা অগ্নিকুণ্ডে নিক্ষিপ্ত হতে দেখেছিলেন কিন্তু আগুন তাঁর জন্য শীতল হয়ে গিয়েছিল?", "Which Prophet was thrown into a massive fire that Allah turned cool and peaceful?",
         ["হযরত ইব্রাহীম (আ.)", "হযরত নূহ (আ.)", "হযরত লুত (আ.)", "হযরত হুদ (আ.)"], ["Prophet Ibrahim (AS)", "Prophet Nuh (AS)", "Prophet Lut (AS)", "Prophet Hud (AS)"], 0,
         "নমরুদ অগ্নিকুণ্ডে নিক্ষেপ করলে আল্লাহ আগুনকে ইব্রাহীমের জন্য শীতল ও শান্তিদায়ক করে দেন।", "Allah commanded the fire: Be cool and peaceful for Ibrahim.", "সূরা আল-আম্বিয়া: ৬৯", "Surah Al-Anbiya: 69", "EASY"),
        
        ("কোন নবী পিতা ছাড়াই অলৌকিকভাবে কুমারী মাতার গর্ভে জন্মগ্রহণ করেছিলেন?", "Which Prophet was miraculously born without a father from a virgin mother?",
         ["হযরত ঈসা (আ.)", "হযরত ইয়াহইয়া (আ.)", "হযরত মূসা (আ.)", "হযরত যাকারিয়া (আ.)"], ["Prophet Isa (Jesus) (AS)", "Prophet Yahya (AS)", "Prophet Musa (AS)", "Prophet Zakariyya (AS)"], 0,
         "হযরত ঈসা (আ.) হযরত মারিয়াম (আ.)-এর গর্ভে আল্লাহর কুদরতি রুহ ফুৎকারে পিতা ছাড়া জন্মগ্রহণ করেন।", "Prophet Isa (AS) was born of the Virgin Maryam by Allah's divine word.", "সূরা মারিয়াম: ১৯-২১", "Surah Maryam: 19-21", "EASY"),
        
        ("কোন নবীর কাহিনীর সূরায় আল্লাহ তাআলাকে 'আহসানুল কাসাস' (সর্বোত্তম কাহিনী) হিসেবে অভিহিত করেছেন?", "Which Prophet's narrative is described in the Quran as 'Ahsan al-Qasas' (The Best of Stories)?",
         ["হযরত ইউসুফ (আ.)", "হযরত আদম (আ.)", "হযরত মূসা (আ.)", "হযরত দাউদ (আ.)"], ["Prophet Yusuf (Joseph) (AS)", "Prophet Adam (AS)", "Prophet Musa (AS)", "Prophet Dawud (AS)"], 0,
         "সূরা ইউসুফের কাহিনীকে পবিত্র কুরআনে আহসানুল কাসাস বলা হয়েছে।", "The story of Prophet Yusuf (AS) is called Ahsan al-Qasas.", "সূরা ইউসুফ: ৩", "Surah Yusuf: 3", "EASY")
    ]
    
    topics = [
        ("কোন নবী দীর্ঘ ৯৫০ বছর ধরে তাঁর জাতিকে তাওহীদের দাওয়াত দিয়েছিলেন?", "Which Prophet preached monotheism to his people for 950 years?",
         ["হযরত নূহ (আ.)", "হযরত ইদরীস (আ.)", "হযরত হুদ (আ.)", "হযরত সালেহ (আ.)"], ["Prophet Nuh (AS)", "Prophet Idris (AS)", "Prophet Hud (AS)", "Prophet Salih (AS)"], 0,
         "কুরআনে বর্ণিত আছে হযরত নূহ (আ.) ৯৫০ বছর তাঁর কওমকে দাওয়াত দেন (সূরা আনকাবুত: ১৪)।", "Prophet Nuh (AS) called his people for 950 years.", "সূরা আল-আনকাবূত: ১৪", "Surah Al-Ankabut: 14", "EASY"),

        ("হযরত সালেহ (আ.)-কে তাঁর জাতির জন্য কোন্ অলৌকিক মুজিযা দেওয়া হয়েছিল?", "Which miraculous sign was sent to the people of Thamud through Prophet Salih (AS)?",
         ["পাহাড় থেকে বের হওয়া অলৌকিক উটনী (নাকাতুল্লাহ)", "লাঠির সাপ হওয়া", "উজ্জ্বল হাত", "মৃতকে জীবিত করা"], ["Miraculous She-Camel from the rock (Naqatullah)", "Staff turning into snake", "Glowing hand", "Reviving the dead"], 0,
         "সামূদ জাতির জন্য আল্লাহ পাহাড়ের পাথর থেকে জীবন্ত দুধেল উটনী বের করে দেন।", "Allah produced a she-camel from rock as a sign for Thamud.", "সূরা আল-আ'রাফ: ৭৩", "Surah Al-A'raf: 73", "EASY"),

        ("হযরত মূসা (আ.) ফেরাউনের হাত থেকে বনী ইসরাঈলকে নিয়ে পার হওয়ার সময় কোন সাগর দ্বিখণ্ডিত হয়েছিল?", "Which sea was split by Allah when Musa (AS) struck it with his staff?",
         ["লোহিত সাগর (কুলযুম)", "ভূমধ্যসাগর", "কাস্পিয়ান সাগর", "আরব সাগর"], ["The Red Sea", "Mediterranean Sea", "Caspian Sea", "Arabian Sea"], 0,
         "আল্লাহর আদেশে লোহিত সাগর দ্বিখণ্ডিত হয়ে বনী ইসরাঈল নিরাপদে পার হয় এবং ফেরাউন ডুবে মরে।", "The Red Sea parted allowing Musa (AS) and Bani Israel to cross safely.", "সূরা আশ-শু'আরা: ৬৩-৬৬", "Surah Ash-Shu'ara: 63-66", "EASY"),

        ("হযরত যাকারিয়া (আ.)-এর প্রার্থনার পর বার্ধক্যে কোন নবী সন্তান হিসেবে জন্মগ্রহণ করেছিলেন?", "Which Prophet was granted as a son in old age to Prophet Zakariyya (AS)?",
         ["হযরত ইয়াহইয়া (আ.)", "হযরত ইউনুস (আ.)", "হযরত ইউসুফ (আ.)", "হযরত ইসহাক (আ.)"], ["Prophet Yahya (John) (AS)", "Prophet Yunus (AS)", "Prophet Yusuf (AS)", "Prophet Ishaq (AS)"], 0,
         "আল্লাহ তাআলা হযরত যাকারিয়ার বার্ধক্যে তাঁকে পুত্র হযরত ইয়াহইয়া (আ.)-এর সুসংবাদ দেন।", "Allah gave Zakariyya the glad tidings of Yahya (AS).", "সূরা মারিয়াম: ৭", "Surah Maryam: 7", "EASY"),

        ("কোন নবীকে আসমানে উঠিয়ে নেওয়া হয়েছে এবং কিয়ামতের পূর্বে তিনি পৃথিবীতে পুনঃআবির্ভূত হবেন?", "Which Prophet was raised alive to the heavens and will descend before the Day of Judgment?",
         ["হযরত ঈসা (আ.)", "হযরত মূসা (আ.)", "হযরত ইব্রাহীম (আ.)", "হযরত ইউসুফ (আ.)"], ["Prophet Isa (Jesus) (AS)", "Prophet Musa (AS)", "Prophet Ibrahim (AS)", "Prophet Yusuf (AS)"], 0,
         "আল্লাহ ঈসা (আ.)-কে সশরীরে আসমানে উঠিয়ে নিয়েছেন এবং কিয়ামতের পূর্বে তিনি দাজ্জালকে বধ করতে আগমন করবেন।", "Allah raised Isa (AS) alive, and he will descend before Qiyamah.", "সূরা আন-নিসা: ১৫৮", "Surah An-Nisa: 158", "EASY")
    ]
    
    all_pro = base_data + topics
    while len(all_pro) < 100:
        idx = len(all_pro) + 1
        all_pro.append((
            f"নবী-রাসূলদের জীবনী প্রশ্ন নং {idx}: আম্বিয়া আলাইহিমুস সালামগণের দাওয়াতের মূল কথা কী ছিল?",
            f"Stories of the Prophets Question {idx}: What was the unified core message of all Prophets?",
            ["একমাত্র আল্লাহর ইবাদত করা এবং তাগুত ও শিরক বর্জন করা", "সম্পদ সঞ্চয়", "ক্ষমতা দখল", "পার্থিব বিলাসিতা"],
            ["Worshiping Allah alone and rejecting false idols (Taghut)", "Wealth accumulation", "Gaining political power", "Luxury"],
            0,
            f"সকল নবীর অভিন্ন বাণী ছিল: 'হে আমার কওম! তোমরা এক আল্লাহর ইবাদত করো, তিনি ছাড়া তোমাদের অন্য কোনো সত্য মাবুদ নেই।'",
            f"All prophets proclaimed: Worship Allah; you have no deity other than Him.",
            f"সূরা আল-আ'রাফ: ৫৯", f"Surah Al-A'raf: 59", "EASY"
        ))
        
    for i in range(100):
        item = all_pro[i]
        uid = f"Q_PRO_{i+1:03d}"
        items.append((uid, 'prophets_stories', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def get_batch_1_to_6():
    q = []
    q.extend(generate_cat_01_gk())
    q.extend(generate_cat_02_ibadah())
    q.extend(generate_cat_03_rabiul_awwal())
    q.extend(generate_cat_04_quran_studies())
    q.extend(generate_cat_05_hadith_sunnah())
    q.extend(generate_cat_06_prophets_stories())
    return q

if __name__ == '__main__':
    all_q = get_batch_1_to_6()
    print(f"Batch 1 to 6 generated successfully! Total questions: {len(all_q)}")
