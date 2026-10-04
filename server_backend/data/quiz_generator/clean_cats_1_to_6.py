# -*- coding: utf-8 -*-
"""
Batch 1: Categories 1 to 6 (35 Genuine Authentic Unique Questions Each = 210 Questions)
1. general_knowledge
2. ibadah
3. rabiul_awwal
4. quran_studies
5. hadith_sunnah
6. prophets_stories
"""

def get_cat_01_gk():
    items = [
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
         "মদীনায় হিজরতের পর রাসূলুল্লাহ ﷺ মদীনা সনদ প্রণয়ন করেন।", "The Prophet ﷺ drafted the Constitution of Madinah upon arrival.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "MEDIUM"),

        ("ইসলামের দৃষ্টিতে সপ্তাহের শ্রেষ্ঠ দিন কোনটি?", "Which is the best day of the week in Islam?",
         ["শুক্রবার (জুমাবার)", "সোমবার", "বৃহস্পতিবার", "রবিবার"], ["Friday (Jummah)", "Monday", "Thursday", "Sunday"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন, দিনগুলোর মধ্যে সর্বোত্তম দিন হলো জুমার দিন।", "The best day is Friday.", "সহীহ মুসলিম: ৮৫৪", "Sahih Muslim: 854", "EASY"),

        ("ইসলামে বছরের শ্রেষ্ঠ রাত কোনটি?", "Which is the greatest night of the year in Islam?",
         ["লাইলাতুল কদর", "শবে বরাত", "শবে মেরাজ", "আরাফাতের রাত"], ["Laylatul Qadr", "Shab-e-Barat", "Shab-e-Miraj", "Night of Arafah"], 0,
         "লাইলাতুল কদর হাজার মাসের চেয়েও শ্রেষ্ঠ।", "Laylatul Qadr is better than a thousand months.", "সূরা আল-কদর: ৩", "Surah Al-Qadr: 3", "EASY"),

        ("কুরআনে কোন নবীর নাম সর্বাধিক বার উল্লেখ করা হয়েছে?", "Which Prophet's name is mentioned most frequently in the Quran?",
         ["হযরত মূসা (আ.)", "হযরত ইব্রাহিম (আ.)", "হযরত নূহ (আ.)", "হযরত ঈসা (আ.)"], ["Prophet Musa (AS)", "Prophet Ibrahim (AS)", "Prophet Nuh (AS)", "Prophet Isa (AS)"], 0,
         "পবিত্র কুরআনে হযরত মূসা (আ.)-এর নাম ১৩৬ বার উল্লেখ করা হয়েছে।", "Prophet Musa (AS) is mentioned 136 times in the Quran.", "আল-ইতকান", "Al-Itqan", "EASY"),

        ("ইসলামের প্রথম মসজিদ কোনটি?", "Which is the first mosque built in Islam?",
         ["মসজিদে কুবা", "মসজিদে নববী", "মসজিদুল হারাম", "মসজিদুল আকসা"], ["Masjid Quba", "Masjid an-Nabawi", "Masjid al-Haram", "Masjid al-Aqsa"], 0,
         "হিজরতের সময় মদীনার উপকণ্ঠে কুবায় ইসলামের প্রথম মসজিদ স্থাপিত হয়।", "Masjid Quba was the first mosque built by the Prophet ﷺ during Hijrah.", "সূরা আত-তাওবাহ: ১০৮", "Surah At-Tawbah: 108", "EASY"),

        ("কুরআনের সর্বশ্রেষ্ঠ আয়াত কোনটি?", "Which is the greatest verse in the Holy Quran?",
         ["আয়াতুল কুরসী", "সূরা আল-ইখলাসের প্রথম আয়াত", "সূরা আল-ফাতিহার প্রথম আয়াত", "আমনার রাসূল"], ["Ayat al-Kursi", "First verse of Surah Al-Ikhlas", "First verse of Surah Al-Fatihah", "Amanar Rasul"], 0,
         "সূরা আল-বাকারার ২৫৫ নম্বর আয়াত (আয়াতুল কুরসী) কুরআনের সর্বশ্রেষ্ঠ আয়াত।", "Ayat al-Kursi (2:255) is confirmed by the Prophet ﷺ as the greatest ayah.", "সহীহ মুসলিম: ৮০৪", "Sahih Muslim: 804", "EASY"),

        ("কুরআনের হৃদয় (Heart of Quran) কাকে বলা হয়?", "Which Surah is referred to as the Heart of the Quran?",
         ["সূরা ইয়াসীন", "সূরা আর-রহমান", "সূরা আল-মুলক", "সূরা আল-ওয়াকিয়া"], ["Surah Yasin", "Surah Ar-Rahman", "Surah Al-Mulk", "Surah Al-Waqiah"], 0,
         "হাদিসে সূরা ইয়াসীনকে কুরআনের হৃদয় বলা হয়েছে।", "Surah Yasin is described in narrations as the heart of the Quran.", "তিরমিযী: ২৮৮৭", "Tirmidhi: 2887", "EASY"),

        ("কুরআনের সৌন্দর্য বা বধূ (Bride of Quran) বলা হয় কোন সূরাকে?", "Which Surah is known as the Bride of the Quran (Uroos al-Quran)?",
         ["সূরা আর-রহমান", "সূরা আল-ফাতিহা", "সূরা আল-কাহফ", "সূরা ইউসুফ"], ["Surah Ar-Rahman", "Surah Al-Fatihah", "Surah Al-Kahf", "Surah Yusuf"], 0,
         "সূরা আর-রহমানকে হাদিসে 'উরূসুল কুরআন' বা কুরআনের বধূ বলা হয়েছে।", "Surah Ar-Rahman is titled Uroos al-Quran in famous narrations.", "বায়হাকী: শুআবুল ঈমান", "Bayhaqi", "EASY"),

        ("কোন সাহাবীকে 'সাইফুল্লাহ' (আল্লাহর তলোয়ার) উপাধিতে ভূষিত করা হয়?", "Which companion was titled 'Saifullah' (Sword of Allah)?",
         ["হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত হামযা (রা.)", "হযরত আলী (রা.)", "হযরত সাদ বিন আবি ওয়াক্কাস (রা.)"], ["Khalid bin Walid (RA)", "Hamzah (RA)", "Ali (RA)", "Sa'd bin Abi Waqqas (RA)"], 0,
         "মুতার যুদ্ধে অসীম বীরত্বের কারণে রাসূলুল্লাহ ﷺ খালিদ বিন ওয়ালিদকে সাইফুল্লাহ উপাধি দেন।", "The Prophet ﷺ titled Khalid bin Walid as the Sword of Allah.", "সহীহ বুখারী: ৩৭৫৭", "Sahih Bukhari: 3757", "EASY"),

        ("কোন সাহাবীকে 'আসাদুল্লাহ' (আল্লাহর সিংহ) বলা হতো?", "Which companion was known as 'Asadullah' (Lion of Allah)?",
         ["হযরত হামযা ইবনে আব্দুল মুত্তালিব (রা.)", "হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত উমর (রা.)", "হযরত আবু উবাইদাহ (রা.)"], ["Hamzah ibn Abdul Muttalib (RA)", "Khalid bin Walid (RA)", "Umar (RA)", "Abu Ubaidah (RA)"], 0,
         "হযরত হামযা (রা.) ছিলেন ইসলামের সিংহ ও সাইয়্যিদুশ শুহাদা।", "Hamzah (RA) was called the Lion of Allah and Leader of Martyrs.", "আল-মুসতাদরাক", "Al-Mustadrak", "EASY"),

        ("কুরআনের সবচেয়ে বড় সূরা কোনটি?", "Which is the longest Surah in the Holy Quran?",
         ["সূরা আল-বাক্বারাহ", "সূরা আলে ইমরান", "সূরা আন-নিসা", "সূরা আল-মায়িদাহ"], ["Surah Al-Baqarah", "Surah Ali Imran", "Surah An-Nisa", "Surah Al-Ma'idah"], 0,
         "সূরা আল-বাক্বারাহ কুরআনের দীর্ঘতম সূরা যার আয়াত সংখ্যা ২৮৬।", "Surah Al-Baqarah is the longest Surah with 286 verses.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),

        ("ইসলামের প্রথম শহীদ কে ছিলেন?", "Who was the first martyr in Islam?",
         ["হযরত সুমাইয়া (রা.)", "হযরত ইয়াসির (রা.)", "হযরত হামযাহ (রা.)", "হযরত মুসআব (রা.)"], ["Sumayyah (RA)", "Yasir (RA)", "Hamzah (RA)", "Mus'ab (RA)"], 0,
         "হযরত সুমাইয়া (রা.) ইসলামের প্রথম শহীদ হন।", "Sumayyah (RA) was the first martyr in Islam.", "আল-ইসাবাহ", "Al-Isabah", "EASY"),

        ("কুরআন প্রথম গ্রন্থাকারে সংকলন করেন কোন খলিফা?", "Who first compiled the Quran into a single volume?",
         ["হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Abu Bakr (RA)", "Umar (RA)", "Uthman (RA)", "Ali (RA)"], 0,
         "আবু বকর (রা.) যায়েদ বিন সাবিতকে দিয়ে কুরআন সংকলন করান।", "Abu Bakr (RA) commissioned Zayd to compile the Quran.", "সহীহ বুখারী: ৪৯৮৬", "Sahih Bukhari: 4986", "MEDIUM"),

        ("কুরআনের সর্বসম্মত অনুলিপি তৈরি করে বিভিন্ন প্রদেশে পাঠান কোন খলিফা?", "Which Caliph standardized the Mus'haf?",
         ["হযরত উসমান (রা.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)"], ["Uthman (RA)", "Abu Bakr (RA)", "Umar (RA)", "Ali (RA)"], 0,
         "হযরত উসমান (রা.) প্রমিত কুরআন সংকলন করে বিভিন্ন দেশে পাঠান।", "Uthman (RA) standardized the Mus'haf.", "সহীহ বুখারী: ৪৯৮৭", "Sahih Bukhari: 4987", "MEDIUM"),

        ("ইসলামিক হিজরি সন গণনা কার আমলে শুরু হয়?", "When was the Hijri calendar established?",
         ["হযরত উমর (রা.)", "হযরত আবু বকর (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Umar (RA)", "Abu Bakr (RA)", "Uthman (RA)", "Ali (RA)"], 0,
         "হযরত উমর (রা.) হিজরি সন গণনা চালু করেন।", "Umar (RA) established the Hijri calendar.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "MEDIUM"),

        ("পবিত্র কুরআনে মোট কতটি সেজদার আয়াত রয়েছে?", "How many verses of Sajdah are there in the Quran?",
         ["১৪টি", "১২টি", "১০টি", "১৬টি"], ["14", "12", "10", "16"], 0,
         "প্রসিদ্ধ মতানুযায়ী তিলাওয়াতে সেজদার আয়াত ১৪টি।", "There are 14 verses of Sajdah in the Quran.", "ফিকহুস সুন্নাহ", "Fiqh us-Sunnah", "EASY")
    ]
    return [('Q_GK_' + str(i+1).zfill(3), 'general_knowledge', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_02_ibadah():
    items = [
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
         "রাসূলুল্লাহ ﷺ বলেছেন: বান্দা সিজদারত অবস্থায় তার রবের সবচেয়ে বেশি নৈকট্য লাভ করে।", "A servant is closest to his Lord when in prostration.", "সহীহ মুসলিম: ৪৮২", "Sahih Muslim: 482", "EASY"),

        ("সালাতুল বিতর-এ কোন দোয়াটি পাঠ করা সুন্নাত?", "Which dua is recited in the Witr prayer?",
         ["দোয়ায়ে কুনুত", "দোয়ায়ে মাসুরা", "দোয়ায়ে ইউনুস", "দোয়ায়ে ইস্তিখারা"], ["Dua al-Qunut", "Dua al-Mathurah", "Dua al-Yunus", "Dua al-Istikharah"], 0,
         "বিতরের সালাতে শেষ রাকাতে দোয়ায়ে কুনুত পড়া হয়।", "Dua Qunut is recited in Witr prayer.", "সুনানে আবু দাউদ: ১৪২৫", "Sunan Abi Dawud: 1425", "EASY"),

        ("সূর্য গ্রহণের সময় যে বিশেষ নফল সালাত আদায় করা হয় তার নাম কী?", "What is the special prayer performed during a Solar Eclipse called?",
         ["সালাতুল কুসূফ", "সালাতুল খুসূফ", "সালাতুল ইস্তিসকা", "সালাতুল খাওফ"], ["Salat al-Kusuf", "Salat al-Khusuf", "Salat al-Istisqa", "Salat al-Khawf"], 0,
         "সূর্য গ্রহণের সালাতকে সালাতুল কুসূফ বলা হয়।", "Solar eclipse prayer is called Salat al-Kusuf.", "সহীহ বুখারী: ১০৪০", "Sahih Bukhari: 1040", "MEDIUM"),

        ("চন্দ্র গ্রহণের সময় যে বিশেষ সালাত আদায় করা হয় তাকে কী বলে?", "What is the prayer during a Lunar Eclipse called?",
         ["সালাতুল খুসূফ", "সালাতুল কুসূফ", "সালাতুল তারাবীহ", "সালাতুল ইশরাক"], ["Salat al-Khusuf", "Salat al-Kusuf", "Salat al-Tarawih", "Salat al-Ishraq"], 0,
         "চন্দ্র গ্রহণের সালাতকে সালাতুল খুসূফ বলা হয়।", "Lunar eclipse prayer is Salat al-Khusuf.", "সহীহ মুসলিম: ৯০১", "Sahih Muslim: 901", "MEDIUM"),

        ("বৃষ্টি প্রার্থনার জন্য যে বিশেষ জামাতে সালাত আদায় করা হয় তাকে কী বলে?", "What is the prayer for rain called in Islamic terminology?",
         ["সালাতুল ইস্তিসকা", "সালাতুল হাজত", "সালাতুল তাওবাহ", "সালাতুদ দুহা"], ["Salat al-Istisqa", "Salat al-Hajat", "Salat al-Tawbah", "Salat ad-Duha"], 0,
         "অনাবৃষ্টির সময় খোলা ময়দানে সালাতুল ইস্তিসকা আদায় করা হয়।", "Salat al-Istisqa is the prayer seeking rain.", "সহীহ বুখারী: ১০১৩", "Sahih Bukhari: 1013", "EASY"),

        ("কোনো গুরুত্বপূর্ণ বিষয়ে সঠিক সিদ্ধান্তের জন্য আল্লাহর দিকনির্দেশনা চেয়ে যে সালাত আদায় করা হয় তাকে কী বলে?", "What is the prayer for guidance in making decisions called?",
         ["সালাতুল ইস্তিখারা", "সালাতুল কাযা", "সালাতুল বিতর", "সালাতুল জানাজা"], ["Salat al-Istikharah", "Salat al-Qada", "Salat al-Witr", "Salat al-Janazah"], 0,
         "রাসূলুল্লাহ ﷺ সাহাবীদের কুরআনের সূরার মতো ইস্তিখারার সালাত ও দোয়া শিক্ষা দিতেন।", "The Prophet ﷺ taught Salat al-Istikharah for decisions.", "সহীহ বুখারী: ১১৬৬", "Sahih Bukhari: 1166", "EASY"),

        ("চাশতের সালাত (সালাতুদ দুহা) সর্বনিম্ন কয় রাকাত পড়া যায়?", "What is the minimum number of Rak'ahs for Duha prayer?",
         ["২ রাকাত", "১ রাকাত", "৪ রাকাত", "৮ রাকাত"], ["2 Rak'ahs", "1 Rak'ah", "4 Rak'ahs", "8 Rak'ahs"], 0,
         "সালাতুদ দুহা সর্বনিম্ন ২ রাকাত এবং সর্বোচ্চ ৮ বা ১২ রাকাত আদায় করা যায়।", "Duha prayer is minimum 2 Rak'ahs.", "সহীহ মুসলিম: ৭২০", "Sahih Muslim: 720", "EASY"),

        ("সালাতে সিজদায়ে সাহু কখন দিতে হয়?", "When is Sajdah as-Sahw (prostration of forgetfulness) performed in Salah?",
         ["সালাতের কোনো ওয়াজিব ভুলবশত ছুটে গেলে বা বেশকম হলে", "ফরজ ছুটে গেলে", "সুন্নাত ছুটে গেলে", "ইচ্ছা করে ভুল করলে"], ["When a Wajib is unintentionally missed or altered", "When Fard is missed", "When Sunnah is missed", "Intentional mistake"], 0,
         "ওয়াজিব অনিচ্ছাকৃত ভুল হলে সিজদায়ে সাহু দিলে সালাত শুদ্ধ হয়।", "Sajdah Sahw rectifies unintentional omission of a Wajib.", "সহীহ বুখারী: ১২২৬", "Sahih Bukhari: 1226", "MEDIUM"),

        ("সালাতের রুকুতে কোন তাসবীহ পাঠ করা সুন্নাত?", "Which Tasbih is recited during Ruku (bowing) in Salah?",
         ["সুবহানা রাব্বিয়াল আযীম", "সুবহানা রাব্বিয়াল আলা", "সুবহানাল্লাহি ওয়া বিহামদিহি", "লা ইলাহা ইল্লাল্লাহ"], ["Subhana Rabbiyal Azeem", "Subhana Rabbiyal A'la", "Subhanallahi wa Bihamdihi", "La ilaha illallah"], 0,
         "রুকুতে 'সুবহানা রাব্বিয়াল আযীম' (আমার মহান রব পবিত্র) ৩ বার পড়া সুন্নাত।", "Reciting Subhana Rabbiyal Azeem in Ruku.", "সহীহ মুসলিম: ৭৭২", "Sahih Muslim: 772", "EASY"),

        ("সালাতের সিজদায় কোন তাসবীহ পাঠ করা সুন্নাত?", "Which Tasbih is recited during Sujood (prostration) in Salah?",
         ["সুবহানা রাব্বিয়াল আলা", "সুবহানা রাব্বিয়াল আযীম", "আল্লাহু আকবার", "সামিআল্লাহু লিমান হামিদাহ"], ["Subhana Rabbiyal A'la", "Subhana Rabbiyal Azeem", "Allahu Akbar", "Sami Allahu Liman Hamidah"], 0,
         "সিজদায় 'সুবহানা রাব্বিয়াল আলা' (আমার সর্বোচ্চ রব পবিত্র) ৩ বার পড়া সুন্নাত।", "Reciting Subhana Rabbiyal A'la in Sujood.", "সহীহ মুসলিম: ৭৭২", "Sahih Muslim: 772", "EASY"),

        ("রুকু থেকে ওঠার সময় ইমাম ও একাকী মুসল্লি কী বলেন?", "What is recited when rising from Ruku?",
         ["সামিআল্লাহু লিমান হামিদাহ", "সুবহানা রাব্বিয়াল আযীম", "আল্লাহু আকবার", "আসসালামু আলাইকুম"], ["Sami Allahu Liman Hamidah", "Subhana Rabbiyal Azeem", "Allahu Akbar", "Assalamu Alaikum"], 0,
         "রুকু থেকে ওঠার সময় 'সামিআল্লাহু লিমান হামিদাহ' এবং সোজা হয়ে 'রাব্বানা লাকাল হামদ' বলা হয়।", "Rising with Sami Allahu Liman Hamidah and Rabbana Lakal Hamd.", "সহীহ বুখারী: ৭৯৫", "Sahih Bukhari: 795", "EASY"),

        ("সালাতে প্রথম বৈঠকের সময় কোন দোয়াটি পাঠ করা ওয়াজিব?", "What is recited during the first Tashahhud in Salah?",
         ["আত্তাহিয়্যাতু", "দুরুদে ইব্রাহিম", "দোয়ায়ে মাসুরা", "দোয়ায়ে কুনুত"], ["At-Tahiyyat", "Durood Ibrahim", "Dua Mathurah", "Dua Qunut"], 0,
         "তাশাহহুদে আত্তাহিয়্যাতু পাঠ করা সালাতের অন্যতম ওয়াজিব।", "Reciting Tashahhud (At-Tahiyyat) is Wajib.", "সহীহ বুখারী: ৮৩১", "Sahih Bukhari: 831", "EASY"),

        ("সালাতের শেষ বৈঠকে তাশাহহুদের পর কোন দুরুদ পাঠ করা সুন্নাতে মুয়াক্কাদাহ?", "Which Durood is recited in the final sitting of Salah?",
         ["দুরুদে ইব্রাহিম", "দুরুদে তাজ", "দুরুদে শিফা", "দুরুদে মুকাদ্দাস"], ["Durood Ibrahim", "Durood Taj", "Durood Shifa", "Durood Muqaddas"], 0,
         "সালাতের শেষ বৈঠকে দুরুদে ইব্রাহিম পড়া সুন্নাতে মুয়াক্কাদাহ।", "Durood Ibrahim is recited after Tashahhud.", "সহীহ বুখারী: ৩৩৭০", "Sahih Bukhari: 3370", "EASY"),

        ("মসজিদে প্রবেশের সময় কোন পা প্রথমে দিতে হয়?", "Which foot is stepped forward when entering a mosque?",
         ["ডান পা", "বাম পা", "যেকোনো পা", "উভয় পা একসাথে"], ["Right foot", "Left foot", "Either foot", "Both together"], 0,
         "মসজিদে প্রবেশের সময় ডান পা দিয়ে প্রবেশ করা এবং দোয়া পড়া সুন্নাত।", "Entering the mosque with the right foot is Sunnah.", "সহীহ বুখারী: ৪২৬", "Sahih Bukhari: 426", "EASY"),

        ("মসজিদ থেকে বের হওয়ার সময় কোন পা প্রথমে দিতে হয়?", "Which foot is stepped forward when exiting a mosque?",
         ["বাম পা", "ডান পা", "যেকোনো পা", "উভয় পা একসাথে"], ["Left foot", "Right foot", "Either foot", "Both together"], 0,
         "মসজিদ থেকে বের হওয়ার সময় বাম পা দিয়ে বের হওয়া সুন্নাত।", "Exiting the mosque with the left foot is Sunnah.", "সুনানে আবু দাউদ: ৪৬৫", "Sunan Abi Dawud: 465", "EASY"),

        ("জায়নামাজে দাঁড়িয়ে সালাত শুরু করার তাকবীরকে কী বলে?", "What is the opening Takbir of Salah called?",
         ["তাকবীরে তাহরীমা", "তাকবীরে তাশরিক", "তাকবীরে ইনতিকাল", "তাকবীরে কুনুত"], ["Takbir Tahrimah", "Takbir Tashreeq", "Takbir Intiqal", "Takbir Qunut"], 0,
         "সালাত শুরু করার তাকবীরকে তাকবীরে তাহরীমা বলে, যা সালাতের ফরজ রুকন।", "The opening Takbir is called Takbir Tahrimah, a pillar of Salah.", "সহীহ বুখারী: ৭৩৮", "Sahih Bukhari: 738", "EASY"),

        ("এক ওয়াক্ত সালাত ইচ্ছাকৃতভাবে ত্যাগ করার বিধান কী?", "What is the ruling on deliberately abandoning a prayer?",
         ["কবিরা গুনাহ ও কুফরির সমতুল্য মারাত্মক অপরাধ", "ছোট গুনাহ", "মাকরুহ", "কোনো পাপ নেই"], ["Major sin bordering on Kufr", "Minor sin", "Makruh", "No sin"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: ব্যক্তি ও কুফরের মাঝে পার্থক্য হলো সালাত ত্যাগ করা।", "The pact between us and them is prayer; whoever abandons it has disbelieved.", "সহীহ মুসলিম: ৮২", "Sahih Muslim: 82", "EASY"),

        ("আযানের জবাবে মুয়াজ্জিনের সাথে সাথে কী করা সুন্নাত?", "What is Sunnah to do while listening to the Adhan?",
         ["মুয়াজ্জিন যা বলে হুবহু তাই পুনরাবৃত্তি করা", "চুপ থাকা", "অন্য কথা বলা", "তালি দেওয়া"], ["Repeat verbatim what the Muazzin says", "Stay silent", "Talk", "Clap"], 0,
         "আযানের বাক্যগুলো পুনরাবৃত্তি করা এবং হাইয়া আলাস সালাহ-এ 'লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ' বলা সুন্নাত।", "Repeating after the Muazzin is an authentic Sunnah.", "সহীহ বুখারী: ৬১৩", "Sahih Bukhari: 613", "EASY"),

        ("আযান ও ইকামতের মধ্যবর্তী সময়ের দোয়ার মর্যাদা কী?", "What is the status of supplication between Adhan and Iqamah?",
         ["দোয়া নিশ্চিত কবুল হওয়ার অন্যতম বরকতময় সময়", "দোয়া করা নিষেধ", "সাধারণ সময়", "মাকরুহ সময়"], ["A blessed time when Dua is not rejected", "Forbidden", "Ordinary time", "Disliked time"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আযান ও ইকামতের মধ্যবর্তী দোয়া ফিরিয়ে দেওয়া হয় না।", "Supplication between Adhan and Iqamah is not rejected.", "জামে আত-তিরমিযী: ২১২", "Jami at-Tirmidhi: 212", "EASY"),

        ("সালাতের মধ্যে দৃষ্টি কোথায় রাখা মুস্তাহাব ও আদব?", "Where should a worshiper focus their gaze during Salah?",
         ["সিজদার স্থানে", "আকাশের দিকে", "ডানে-বামে", "চোখ বন্ধ করে"], ["Place of prostration (Sajdah spot)", "Towards the sky", "Left and right", "Eyes closed"], 0,
         "দাঁড়ানো অবস্থায় সিজদার স্থানে দৃষ্টি রাখা সালাতের আদব ও খুশু অর্জনে সহায়ক।", "Looking at the spot of prostration fosters Khushu.", "ফিকহুস সুন্নাহ", "Fiqh us-Sunnah", "EASY"),

        ("সালাতে সূরা আল-ফাতিহা পাঠ করার বিধান কী?", "What is the ruling on reciting Surah Al-Fatihah in Salah?",
         ["রুকন ও ফরজ, যা ছাড়া সালাত হয় না", "নফল", "মুস্তাহাব", "ইচ্ছাধীন"], ["A mandatory pillar without which prayer is invalid", "Nafl", "Mustahab", "Optional"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি সূরা ফাতিহা পড়ল না তার সালাতই হলো না।", "No prayer is valid without reciting the Opening of the Book (Al-Fatihah).", "সহীহ বুখারী: ৭৫৬", "Sahih Bukhari: 756", "EASY"),

        ("মুসাফির ব্যক্তির চার রাকাত বিশিষ্ট ফরজ সালাতের কসরের বিধান কী?", "How many Rak'ahs does a traveler pray for a 4-Rak'ah Fard prayer (Qasr)?",
         ["২ রাকাত", "১ রাকাত", "৩ রাকাত", "৪ রাকাতই পড়বে"], ["2 Rak'ahs", "1 Rak'ah", "3 Rak'ahs", "4 Rak'ahs"], 0,
         "সফর অবস্থায় চার রাকাত বিশিষ্ট ফরজ সালাত ২ রাকাত আদায় করা আল্লাহর পক্ষ থেকে বিশেষ সুবিধা।", "Travelers shorten 4-Rak'ah prayers to 2 Rak'ahs.", "সহীহ বুখারী: ১০৯০", "Sahih Bukhari: 1090", "EASY"),

        ("সালাতুল জানাজার ফরজ কয়টি?", "How many Fard acts are there in Janazah (Funeral) prayer?",
         ["২টি (চার তাকবীর ও দাঁড়িয়ে সালাত আদায়)", "৩টি", "৪টি", "৫টি"], ["2 (4 Takbirs & Standing)", "3", "4", "5"], 0,
         "জানাজার সালাতে চার তাকবীর এবং দাঁড়িয়ে সালাত আদায় করা ফরজ।", "Janazah prayer has two obligatory pillars: 4 Takbirs and Qiyam.", "ফিকহুস সুন্নাহ", "Fiqh us-Sunnah", "EASY"),

        ("সালাতুল জানাজায় রুকু বা সিজদা আছে কি?", "Is there any Ruku or Sajdah in the Funeral (Janazah) Prayer?",
         ["না, কোনো রুকু বা সিজদা নেই", "হ্যাঁ, ১টি রুকু আছে", "হ্যাঁ, ২টি সিজদা আছে", "ইচ্ছাধীন"], ["No, there is no Ruku or Sajdah", "Yes, 1 Ruku", "Yes, 2 Sajdahs", "Optional"], 0,
         "জানাজার সালাতে কোনো রুকু ও সিজদা নেই; এটি কেবল দাঁড়িয়ে চার তাকবীরে দোয়ার মাধ্যমে সম্পন্ন হয়।", "Janazah prayer contains no bowing or prostrations.", "সহীহ বুখারী: ১৩২৯", "Sahih Bukhari: 1329", "EASY"),

        ("জুমার দুই রাকাত ফরজ সালাতের জামাতে অংশগ্রহণ করা কাদের ওপর ফরজ?", "Upon whom is attending the Friday congregational prayer (Jummah) obligatory?",
         ["প্রাপ্তবয়স্ক সুস্থ স্থায়ী পুরুষ মুসলিমের ওপর", "সকল শিশুর ওপর", "মুসাফিরের ওপর", "অসুস্থ ব্যক্তির ওপর"], ["Adult, sane, resident Muslim males", "All children", "Travelers", "Sick persons"], 0,
         "জুমার সালাত প্রত্যেক স্বাধীন, প্রাপ্তবয়স্ক ও সুস্থ পুরুষের ওপর ফরজ।", "Jummah is Fard Ayn upon adult resident Muslim men.", "সুনানে আবু দাউদ: ১০৬৭", "Sunan Abi Dawud: 1067", "EASY"),

        ("সালাতুল ইশরাক কখন আদায় করতে হয়?", "When is Ishraq prayer performed?",
         ["সূর্যোদয়ের প্রায় ১৫-২০ মিনিট পর", "মধ্যরাতে", "মাগরিবের পর", "যোহরের পর"], ["15-20 minutes after sunrise", "Midnight", "After Maghrib", "After Zuhr"], 0,
         "সূর্য পূর্ণরূপে উদিত হয়ে কিছুটা ওপরে ওঠার পর ইশরাকের সালাত পড়া সুন্নাত।", "Ishraq prayer is offered once the sun has completely risen.", "জামে আত-তিরমিযী: ৫৮৬", "Jami at-Tirmidhi: 586", "EASY")
    ]
    return [('Q_IBD_' + str(i+1).zfill(3), 'ibadah', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_03_rabiul_awwal():
    items = [
        ("বিশ্বনবী হযরত মুহাম্মদ ﷺ কোন মাসে এই পৃথিবীতে জন্মগ্রহণ করেছিলেন?", "In which Islamic month was Prophet Muhammad ﷺ born?",
         ["রবিউল আউয়াল", "রমজান", "মুহররম", "শাবান"], ["Rabiul Awwal", "Ramadan", "Muharram", "Shaban"], 0,
         "বিশ্বনবী হযরত মুহাম্মদ ﷺ ১২ই রবিউল আউয়াল সোমবার জন্মগ্রহণ করেন।", "The Prophet ﷺ was born in the blessed month of Rabiul Awwal.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ কোন্ বারে জন্মগ্রহণ করেছিলেন?", "On which day of the week was Prophet Muhammad ﷺ born?",
         ["সোমবার", "শুক্রবার", "বুধবার", "রবিবার"], ["Monday", "Friday", "Wednesday", "Sunday"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সোমবার দিনে আমি জন্মগ্রহণ করেছি এবং এ দিনেই আমার ওপর ওহী নাযিল হয়েছে।", "The Prophet ﷺ said: Monday is the day on which I was born and received revelation.", "সহীহ মুসলিম: ১১৬২", "Sahih Muslim: 1162", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর জন্মসালকে ঐতিহাসিক প্রেক্ষাপটে কী বলা হয়?", "What is the historical year of the Prophet's ﷺ birth known as?",
         ["আমুল ফিল (হস্তী বছর)", "আমুল হুযন (শোকের বছর)", "আমুল ওফুদ (প্রতিনিধিদের বছর)", "আমুল ফাতহ"], ["Am al-Fil (Year of the Elephant)", "Am al-Huzn", "Am al-Wufud", "Am al-Fath"], 0,
         "আবরাহা কর্তৃক কাবা আক্রমণের বছর আবাবিল পাখি দ্বারা হস্তীবাহিনী ধ্বংসের বছরে রাসূল ﷺ জন্ম নেন।", "The Prophet ﷺ was born in the Year of the Elephant (Am al-Fil).", "সূরা আল-ফীল", "Surah Al-Fil", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর পিতার নাম কী ছিল?", "What was the name of the father of Prophet Muhammad ﷺ?",
         ["আব্দুল্লাহ", "আবু তালিব", "হামযাহ", "আব্বাস"], ["Abdullah", "Abu Talib", "Hamzah", "Abbas"], 0,
         "রাসূল ﷺ-এর পিতার নাম ছিল আব্দুল্লাহ ইবনে আব্দুল মুত্তালিব।", "The father of the Prophet ﷺ was Abdullah ibn Abdul Muttalib.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর মাতার নাম কী ছিল?", "What was the name of the mother of Prophet Muhammad ﷺ?",
         ["আমিনা বিনতে ওয়াহাব", "হালিমা সাদিয়া", "ফাতিমা বিনতে আসাদ", "উম্মে আইমান"], ["Aminah bint Wahb", "Halimah Sa'diyyah", "Fatimah bint Asad", "Umm Ayman"], 0,
         "রাসূলুল্লাহ ﷺ-এর সম্মানিত মাতার নাম আমিনা।", "The mother of the Prophet ﷺ was Aminah bint Wahb.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর দুধমাতা কে ছিলেন যিনি তাঁকে শৈশবে লালন-পালন করেছিলেন?", "Who was the wet nurse that fostered Prophet Muhammad ﷺ in childhood?",
         ["হালিমা সাদিয়া (রা.)", "উম্মে জামিল", "আসমা বিনতে আবু বকর", "খাওলা বিনতে হাকিম"], ["Halimah as-Sa'diyyah", "Umm Jamil", "Asma bint Abi Bakr", "Khawlah bint Hakim"], 0,
         "বনু সাদ গোত্রের হালিমা সাদিয়া (রা.) শৈশবে নবীজি ﷺ-কে দুধপান ও লালন-পালন করান।", "Halimah as-Sa'diyyah was the foster mother of the Prophet ﷺ.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর দাদা যিনি জন্মের পর তাঁর নাম 'মুহাম্মদ' রেখেছিলেন তাঁর নাম কী?", "What was the name of the grandfather who named the Prophet ﷺ 'Muhammad'?",
         ["আব্দুল মুত্তালিব", "হাশিম", "আব্দে মানাফ", "কুসাই"], ["Abdul Muttalib", "Hashim", "Abd Manaf", "Qusayy"], 0,
         "দাদা আব্দুল মুত্তালিব কাবা প্রাঙ্গণে পৌত্রের নাম রাখেন মুহাম্মদ ﷺ।", "His grandfather Abdul Muttalib named him Muhammad ﷺ.", "যাদুল মা'আদ", "Zad al-Ma'ad", "EASY"),

        ("রাসূলুল্লাহ ﷺ কত বছর বয়সে নবুওয়াত লাভ করেন?", "At what age did Prophet Muhammad ﷺ receive prophethood?",
         ["৪০ বছর বয়সে", "২৫ বছর বয়সে", "৩০ বছর বয়সে", "৫০ বছর বয়সে"], ["40 years old", "25 years old", "30 years old", "50 years old"], 0,
         "চল্লিশ বছর বয়সে হেরা গুহায় ধ্যানমগ্ন অবস্থায় জিবরাইল (আ.)-এর মাধ্যমে প্রথম ওহী নাযিল হয়।", "The Prophet ﷺ received the first revelation in Cave Hira at age 40.", "সহীহ বুখারী: ৩", "Sahih Bukhari: 3", "EASY"),

        ("নবুওয়াত লাভের পূর্বে সততা ও বিশ্বস্ততার জন্য মক্কাবাসীরা রাসূল ﷺ-কে কী উপাধি দিয়েছিল?", "What title was given to Prophet Muhammad ﷺ by Makkans for his honesty?",
         ["আল-আমিন ও আস-সাদিক", "ফারুক", "সাইফুল্লাহ", "যুন-নুরাইন"], ["Al-Amin and As-Sadiq", "Al-Farooq", "Saifullah", "Dhun-Nurayn"], 0,
         "মক্কার কাফের-মুসলিম সকলেই নবীজিকে 'আল-আমিন' (বিশ্বস্ত) ও 'আস-সাদিক' (সত্যবাদী) বলে ডাকত।", "He was universally known as Al-Amin (The Trustworthy) and As-Sadiq (The Truthful).", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ প্রথম কোন ব্যবসায়ী মহীয়সী নারীর বাণিজ্য কাফেলা সফলভাবে পরিচালনা করেছিলেন?", "Whose trade caravan did Prophet Muhammad ﷺ manage successfully in Syria?",
         ["হযরত খাদিজাতুল কুবরা (রা.)", "হযরত সওদা (রা.)", "হযরত মায়মুনা (রা.)", "হযরত হাফসা (রা.)"], ["Khadijah tul-Kubra (RA)", "Sawdah (RA)", "Maymunah (RA)", "Hafsah (RA)"], 0,
         "হযরত খাদিজা (রা.)-এর ব্যবসায়িক দায়িত্ব নিয়ে তিনি সিরিয়ায় সততার সাথে বাণিজ্য পরিচালনা করেন।", "The Prophet ﷺ managed Khadijah's (RA) trade expedition to Syria.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("হযরত খাদিজা (রা.)-এর সাথে বিবাহের সময় রাসূলুল্লাহ ﷺ-এর বয়স কত ছিল?", "How old was Prophet Muhammad ﷺ when he married Khadijah (RA)?",
         ["২৫ বছর", "২০ বছর", "৩০ বছর", "৪০ বছর"], ["25 years old", "20 years old", "30 years old", "40 years old"], 0,
         "বিবাহের সময় নবীজি ﷺ-এর বয়স ছিল ২৫ বছর এবং খাদিজা (রা.)-এর বয়স ছিল ৪০ বছর।", "The Prophet ﷺ was 25 years old when he married Khadijah (RA).", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর জীবনে মিরাজের অলৌকিক ঘটনা কোন মাসে সংঘটিত হয়েছিল?", "In which month did the miraculous Night Journey (Isra and Mi'raj) take place?",
         ["রজব মাসে", "রমজান মাসে", "শাওয়াল মাসে", "মুহররম মাসে"], ["Rajab", "Ramadan", "Shawwal", "Muharram"], 0,
         "প্রসিদ্ধ মতানুযায়ী রজব মাসের ২৭তম রজনীতে মিরাজের অবিস্মরণীয় ঘটনা ঘটে।", "The Isra and Mi'raj occurred on the 27th night of Rajab.", "সূরা আল-ইসরা: ১", "Surah Al-Isra: 1", "MEDIUM"),

        ("রাসূলুল্লাহ ﷺ মক্কা থেকে মদীনায় যে ঐতিহাসিক হিজরত সম্পন্ন করেন তা কোন হিজরি সনের সূচনা করে?", "Which milestone event marks the starting point of the Islamic calendar?",
         ["মদীনায় ঐতিহাসিক হিজরত", "মক্কা বিজয়", "বদর যুদ্ধ", "নবুওয়াত লাভ"], ["The Hijrah to Madinah", "Conquest of Makkah", "Battle of Badr", "Beginning of Prophethood"], 0,
         "রাসূলুল্লাহ ﷺ-এর মক্কা থেকে মদীনায় হিজরতের ঐতিহাসিক ঘটনা থেকেই হিজরি সন গণনা শুরু হয়।", "The migration to Madinah (Hijrah) marks the foundation of the Hijri calendar.", "সহীহ বুখারী: ৩৯০৫", "Sahih Bukhari: 3905", "EASY"),

        ("মদীনায় হিজরতের পর রাসূলুল্লাহ ﷺ সর্বপ্রথম কার বাড়িতে মেহমান হিসেবে অবস্থান করেছিলেন?", "In whose house did the Prophet ﷺ stay upon arriving in Madinah?",
         ["হযরত আবু আইয়ুব আনসারী (রা.)", "হযরত সা'দ বিন উবাদাহ (রা.)", "হযরত উবাই বিন কাব (রা.)", "হযরত আনাস বিন মালিক (রা.)"], ["Abu Ayyub al-Ansari (RA)", "Sa'd bin Ubadah (RA)", "Ubayy bin Ka'b (RA)", "Anas bin Malik (RA)"], 0,
         "রাসূলুল্লাহ ﷺ-এর উটনী আবু আইয়ুব আনসারী (রা.)-এর ঘরের সামনে থামে এবং তিনি তাঁর মেহমান হন।", "The Prophet ﷺ stayed at Abu Ayyub al-Ansari's (RA) residence.", "সহীহ বুখারী: ৩৯১১", "Sahih Bukhari: 3911", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর পবিত্র রওজা মুবারক কোথায় অবস্থিত?", "Where is the blessed resting place (Rawdah) of Prophet Muhammad ﷺ located?",
         ["মদীনা মুনাওয়ারায় মসজিদে নববীর অভ্যন্তরে", "মক্কা মুকাররমায়", "জেরুসালেমে", "তায়েফে"], ["Inside Masjid an-Nabawi in Madinah", "Makkah", "Jerusalem", "Taif"], 0,
         "রাসূলুল্লাহ ﷺ যে হুজরায় (হযরত আয়েশা রা.-এর কামরা) ইন্তেকাল করেন সেখানেই সমাহিত হন।", "The Prophet ﷺ is buried in the chamber of Aisha (RA) inside Masjid an-Nabawi.", "সহীহ বুখারী: ১৩৯০", "Sahih Bukhari: 1390", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর সীরাত অধ্যয়নের প্রধান ধর্মীয় উদ্দেশ্য কী?", "What is the primary spiritual objective of studying the Prophet's ﷺ Seerah?",
         ["নবীজি ﷺ-এর সুন্নাহকে জীবনের প্রতিটি ক্ষেত্রে অনুসরণ ও ভালোবাসা বৃদ্ধি করা", "শুধুমাত্র ঐতিহাসিক গল্প জানা", "পার্থিব প্রশংসা অর্জন", "কবিতা রচনা করা"], ["To emulate his Sunnah in all aspects of life and deepen love for him", "Historical pastime", "Worldly praise", "Poetry composition"], 0,
         "আল্লাহ তাআলা বলেন: নিশ্চয়ই তোমাদের জন্য আল্লাহর রাসূলের জীবনে রয়েছে সর্বোত্তম আদর্শ।", "Indeed, in the Messenger of Allah you have an excellent example.", "সূরা আল-আহযাব: ২১", "Surah Al-Ahzab: 21", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর পিতা আব্দুল্লাহ তাঁর জন্মের কতদিন পূর্বে ইন্তেকাল করেন?", "When did the Prophet's ﷺ father Abdullah pass away?",
         ["রাসূল ﷺ-এর জন্মের পূর্বেই গর্ভকালীন অবস্থায়", "জন্মের ৫ বছর পর", "জন্মের ১০ বছর পর", "নবুওয়াতের পর"], ["Before his birth while in his mother's womb", "5 years after birth", "10 years after birth", "After Prophethood"], 0,
         "রাসূলুল্লাহ ﷺ ইয়াতীম অবস্থায় জন্মগ্রহণ করেন, জন্মের পূর্বেই পিতা আব্দুল্লাহ ইন্তেকাল করেন।", "The Prophet ﷺ was born an orphan, his father having passed away before his birth.", "সূরা আদ-দুহা: ৬", "Surah Ad-Duha: 6", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর মাতা আমিনা যখন ইন্তেকাল করেন তখন নবীজি ﷺ-এর বয়স কত ছিল?", "How old was Prophet Muhammad ﷺ when his mother Aminah passed away?",
         ["৬ বছর", "২ বছর", "৮ বছর", "১২ বছর"], ["6 years old", "2 years old", "8 years old", "12 years old"], 0,
         "ছয় বছর বয়সে আবওয়া নামক স্থানে ফেরার পথে মাতা আমিনা ইন্তেকাল করেন।", "His mother Aminah passed away at Abwa when he was 6 years old.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("মাতার ইন্তেকালের পর আট বছর বয়স পর্যন্ত নবীজি ﷺ-কে কে লালন-পালন করেছিলেন?", "Who took care of Prophet Muhammad ﷺ until age 8 after his mother's death?",
         ["দাদা আব্দুল মুত্তালিব", "চাচা আবু তালিব", "চাচা হামযাহ", "চাচা আব্বাস"], ["His grandfather Abdul Muttalib", "Uncle Abu Talib", "Uncle Hamzah", "Uncle Abbas"], 0,
         "দাদা আব্দুল মুত্তালিব অত্যন্ত স্নেহে নবীজিকে আট বছর বয়স পর্যন্ত লালন-পালন করেন।", "Grandfather Abdul Muttalib took devoted care of him until age eight.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("দাদার ইন্তেকালের পর আজীবন ভাতিজা মুহাম্মদ ﷺ-কে বিপদ-আপদ থেকে রক্ষা করেন কোন চাচা?", "Which uncle protected Prophet Muhammad ﷺ throughout his life in Makkah?",
         ["আবু তালিব", "আবু লাহাব", "হামযাহ", "আব্বাস"], ["Abu Talib", "Abu Lahab", "Hamzah", "Abbas"], 0,
         "চাচা আবু তালিব নবুওয়াতের পরেও কুরাইশদের অত্যাচার থেকে ভাতিজাকে পূর্ণ নিরাপত্তা দেন।", "Uncle Abu Talib protected him with utmost loyalty in Makkah.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ শৈশবে কিশোর বয়সে শান্তির জন্য কোন ঐতিহাসিক শান্তিচুক্তিতে অংশ নেন?", "Which historical peace alliance did teenage Muhammad ﷺ participate in?",
         ["হিলফুল ফুযুল", "হুদাইবিয়ার সন্ধি", "মদীনা সনদ", "আকাবা চুক্তি"], ["Hilf al-Fudul (Alliance of the Virtuous)", "Treaty of Hudaybiyyah", "Madinah Charter", "Pledge of Aqabah"], 0,
         "মক্কার দুর্বল ও নিপীড়িতদের সাহায্য করার জন্য হিলফুল ফুযুল শান্তিচুক্তি গঠিত হয়।", "He took part in Hilf al-Fudul to protect the oppressed and travelers.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর পবিত্র বংশের নাম কী?", "Which noble clan did Prophet Muhammad ﷺ belong to?",
         ["কুরাইশ বংশের বনু হাশিম গোত্র", "বনু উমাইয়া", "বনু মাখযূম", "বনু নাজ্জার"], ["Banu Hashim of the Quraysh tribe", "Banu Umayyah", "Banu Makhzum", "Banu Najjar"], 0,
         "রাসূলুল্লাহ ﷺ আরবের সর্বাধিক সম্মানিত কুরাইশ বংশের বনু হাশিম শাখায় জন্মগ্রহণ করেন।", "The Prophet ﷺ belonged to the noble Banu Hashim clan of Quraysh.", "সহীহ মুসলিম: ২২৭৬", "Sahih Muslim: 2276", "EASY"),

        ("কুরআনে কারীমে রাসূলুল্লাহ ﷺ-কে সমগ্র সৃষ্টিজগতের জন্য কী হিসেবে প্রেরণের ঘোষণা দেওয়া হয়েছে?", "What is Prophet Muhammad ﷺ described as for all creation in the Quran?",
         ["সমগ্র বিশ্বজগতের জন্য রহমত (রাহমাতুল্লিল আলামীন)", "কঠোর শাসক", "যুদ্ধবাজ নেতা", "কেবল আরবের পথপ্রদর্শক"], ["A Mercy to all the worlds (Rahmatan lil-Alameen)", "A harsh ruler", "A warrior", "Only an Arab guide"], 0,
         "আল্লাহ তাআলা বলেন: আমি আপনাকে সমগ্র বিশ্বজগতের জন্য কেবল রহমত হিসেবেই প্রেরণ করেছি।", "We have not sent you except as a mercy to the worlds.", "সূরা আল-আম্বিয়া: ১০৭", "Surah Al-Anbiya: 107", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর সীরাতে 'আমুল হুযন' (শোকের বছর) কোন দুটি প্রিয় ব্যক্তির ওফাতকে নির্দেশ করে?", "Which two beloved figures' passing marked the Year of Grief (Am al-Huzn)?",
         ["স্ত্রী হযরত খাদিজা (রা.) ও চাচা আবু তালিব", "পিতা আব্দুল্লাহ ও মাতা আমিনা", "দাদা আব্দুল মুত্তালিব ও হামযাহ", "পুত্র ইব্রাহিম ও কাসিম"], ["Wife Khadijah (RA) and Uncle Abu Talib", "Parents Abdullah & Aminah", "Abdul Muttalib & Hamzah", "Ibrahim & Qasim"], 0,
         "একই বছরে স্নেহময়ী স্ত্রী খাদিজা (রা.) ও পরম অভিভাবক চাচা আবু তালিবের ইন্তেকাল হয়।", "The passing of Khadijah (RA) and Abu Talib in the same year was Am al-Huzn.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ তায়েফে ইসলাম প্রচার করতে গেলে তায়েফবাসীরা তাঁর সাথে কেমন আচরণ করেছিল?", "How did the people of Taif treat Prophet Muhammad ﷺ during his visit?",
         ["পাথর ছুড়ে তাঁকে মারাত্মকভাবে রক্তাক্ত ও ক্ষতবিক্ষত করেছিল", "সম্মান প্রদর্শন করেছিল", "স্বর্ণ উপহার দিয়েছিল", "ইসলাম গ্রহণ করেছিল"], ["Stoned him severely causing blood to flow into his shoes", "Honored him", "Gave gold gifts", "Accepted Islam immediately"], 0,
         "তায়েফের দুষ্ট যুবকেরা পাথর মেরে নবীজির শরীর রক্তাক্ত করে দেয়, তবুও তিনি তাদের ধ্বংসের জন্য বদদোয়া করেননি।", "The Prophet ﷺ was stoned by Taif's mobs but prayed for their guidance.", "সহীহ বুখারী: ৩২৩১", "Sahih Bukhari: 3231", "EASY"),

        ("রাসূলুল্লাহ ﷺ মোট কত বছর জীবিত ছিলেন?", "How long was the blessed lifespan of Prophet Muhammad ﷺ?",
         ["৬৩ বছর", "৬০ বছর", "৬৫ বছর", "৭০ বছর"], ["63 years", "60 years", "65 years", "70 years"], 0,
         "রাসূলুল্লাহ ﷺ হিজরি ৬৩ বছর বয়সে মদীনায় ওফাত লাভ করেন।", "The Prophet ﷺ passed away at the age of 63 lunar years.", "সহীহ বুখারী: ৩৫৩৬", "Sahih Bukhari: 3536", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর কয়জন সন্তান ছিলেন?", "How many children did Prophet Muhammad ﷺ have?",
         ["৭ জন (৩ পুত্র ও ৪ কন্যা)", "৫ জন", "৬ জন", "৮ জন"], ["7 children (3 sons and 4 daughters)", "5 children", "6 children", "8 children"], 0,
         "পুত্রগণ: কাসিম, আব্দুল্লাহ, ইব্রাহিম; কন্যাগণ: জয়নব, রুকাইয়্যাহ, উম্মে কুলসুম ও ফাতিমা।", "He had seven children: Qasim, Abdullah, Ibrahim, Zaynab, Ruqayyah, Umm Kulthum, Fatimah.", "যাদুল মা'আদ", "Zad al-Ma'ad", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর কোন সন্তান ব্যতিরেকে বাকি সকল সন্তান তাঁর জীবদ্দশাতেই ইন্তেকাল করেন?", "Which child of the Prophet ﷺ survived him and passed away shortly after him?",
         ["হযরত ফাতিমাতুজ জাহরা (রা.)", "হযরত জয়নব (রা.)", "হযরত রুকাইয়্যাহ (রা.)", "হযরত উম্মে কুলসুম (রা.)"], ["Fatimah az-Zahra (RA)", "Zaynab (RA)", "Ruqayyah (RA)", "Umm Kulthum (RA)"], 0,
         "রাসূল ﷺ-এর ওফাতের ছয় মাস পর হযরত ফাতিমা (রা.) ইন্তেকাল করেন।", "Fatimah (RA) was the only child who survived him, passing away 6 months later.", "সহীহ বুখারী: ৪২৪০", "Sahih Bukhari: 4240", "EASY"),

        ("হুদাইবিয়ার সন্ধির ঐতিহাসিক শর্তাবলী মুসলিমদের কাছে সাময়িক কঠিন মনে হলেও আল্লাহ এটিকে কী ঘোষণা করেছিলেন?", "What did Allah declare the Treaty of Hudaybiyyah to be in Surah Al-Fath?",
         ["ফাতহুম মুবীন (সুস্পষ্ট বিজয়)", "পরাজয়", "সাধারণ চুক্তি", "শোকগাথা"], ["Fathan Mubeena (A Manifest Victory)", "Defeat", "Ordinary truce", "Grief"], 0,
         "আল্লাহ তাআলা সূরা আল-ফাতহে হুদাইবিয়ার সন্ধিকে প্রকাশ্য বিজয় বলে ঘোষণা করেন।", "Indeed, We have given you a manifest victory (Surah Al-Fath: 1).", "সূরা আল-ফাতহ: ১", "Surah Al-Fath: 1", "EASY"),

        ("রাসূলুল্লাহ ﷺ মক্কা বিজয়ের দিন মক্কাবাসীদের উদ্দেশ্যে কী সাধারণ ঘোষণা দিয়েছিলেন?", "What general declaration did the Prophet ﷺ make to the Makkans on the day of Conquest?",
         ["আজ তোমাদের ওপর কোনো প্রতিশোধ নেই, তোমরা সবাই মুক্ত", "সবাইকে বন্দী করা হলো", "সম্পদ বাজেয়াপ্ত করা হলো", "দেশত্যাগ করতে হবে"], ["No blame upon you today; you are all free (Unconditional Amnesty)", "All imprisoned", "Wealth seized", "Must leave"], 0,
         "রাসূলুল্লাহ ﷺ পরম ক্ষমা প্রদর্শন করে সমগ্র মক্কাবাসীকে নিঃশর্ত ক্ষমা ঘোষণা করেন।", "The Prophet ﷺ granted a general amnesty to all former persecutors.", "সীরাতে ইবনে হিশাম", "Sirah Ibn Hisham", "EASY"),

        ("বিদায় হজের ঐতিহাসিক ভাষণে রাসূলুল্লাহ ﷺ সমগ্র মানবজাতির সমতা প্রকাশে কী বলেছিলেন?", "What did the Prophet ﷺ state regarding racial equality in his Farewell Sermon?",
         ["তাকওয়া ব্যতীত কোনো আরবের ওপর অনারবের এবং অনারবের ওপর আরবের কোনো শ্রেষ্ঠত্ব নেই", "আরবরা সবার শ্রেষ্ঠ", "ধনীরা শ্রেষ্ঠ", "শক্তিমান শ্রেষ্ঠ"], ["No Arab is superior to a non-Arab nor white over black except by Taqwa", "Arabs superior", "Wealthy superior", "Mighty superior"], 0,
         "ইসলামে বর্ণ, গোত্র ও জাতির কোনো ভেদাভেদ নেই; শ্রেষ্ঠত্বের একমাত্র মাপকাঠি তাকওয়া।", "All mankind is from Adam and Eve; superiority is solely determined by piety.", "মুসনাদে আহমাদ: ২২৯৭৮", "Musnad Ahmad: 22978", "EASY"),

        ("বিদায় হজের ভাষণে রাসূলুল্লাহ ﷺ মুসলিমদের জন্য পথভ্রষ্ট না হওয়ার কোন্ দুটি প্রধান উৎস আঁকড়ে ধরার নির্দেশ দেন?", "Which two primary sources did the Prophet ﷺ leave behind for our guidance?",
         ["আল্লাহর কিতাব (কুরআন) ও রাসূলের সুন্নাহ (হাদিস)", "কেবল দর্শনশাস্ত্র", "পার্থিব আইন", "ঐতিহ্য ও প্রথা"], ["The Book of Allah (Quran) and the Sunnah of His Prophet", "Philosophy only", "Secular law", "Customs"], 0,
         "রাসূল ﷺ বলেন: আমি তোমাদের মাঝে দুটি জিনিস রেখে যাচ্ছি, যা শক্তভাবে আঁকড়ে রাখলে কখনও পথভ্রষ্ট হবে না।", "I leave behind two things: the Book of Allah and my Sunnah.", "মুয়াত্তা মালিক: ৩৩৩", "Muwatta Malik: 333", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর ওফাত হিজরি সনের কোন মাসে সংঘটিত হয়েছিল?", "In which month did the passing (Wafat) of Prophet Muhammad ﷺ occur?",
         ["রবিউল আউয়াল মাসে", "রমজান মাসে", "যিলহজ মাসে", "মুহররম মাসে"], ["Rabiul Awwal", "Ramadan", "Dhu al-Hijjah", "Muharram"], 0,
         "রাসূলুল্লাহ ﷺ ১১ হিজরির ১২ই রবিউল আউয়াল সোমবার রফিকে আলার সান্নিধ্যে গমন করেন।", "The Prophet ﷺ passed away on 12 Rabiul Awwal, 11 AH.", "সহীহ বুখারী: ৪৪৪০", "Sahih Bukhari: 4440", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর পবিত্র নামের প্রতি সম্মান জানিয়ে দরুদ পাঠ করার কুরআনিক নির্দেশ কোন সূরায় এসেছে?", "In which Surah does Allah command believers to send blessings (Salawat) upon the Prophet ﷺ?",
         ["সূরা আল-আহযাব (৫৬ নম্বর আয়াত)", "সূরা আল-বাক্বারাহ", "সূরা আলে ইমরান", "সূরা আন-নূর"], ["Surah Al-Ahzab (Verse 56)", "Surah Al-Baqarah", "Surah Ali Imran", "Surah An-Nur"], 0,
         "আল্লাহ বলেন: নিশ্চয়ই আল্লাহ ও তাঁর ফেরেশতাগণ নবীর ওপর দরুদ পাঠান; হে মুমিনগণ! তোমরাও তাঁর ওপর দরুদ ও সালাম পাঠাও।", "Allah and His angels send blessings upon the Prophet; O you who believe, send blessings upon him.", "সূরা আল-আহযাব: ৫৬", "Surah Al-Ahzab: 56", "EASY"),

        ("যে ব্যক্তি একবার রাসূলুল্লাহ ﷺ-এর ওপর দরুদ পাঠ করে, আল্লাহ তার ওপর কতটি রহমত নাযিল করেন?", "How many mercies does Allah bestow upon someone who sends one Salawat upon the Prophet ﷺ?",
         ["১০টি রহমত", "১টি রহমত", "৫টি রহমত", "৭০টি রহমত"], ["10 Mercies", "1 Mercy", "5 Mercies", "70 Mercies"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি আমার ওপর একবার দরুদ পাঠ করে, আল্লাহ তার ওপর দশটি রহমত নাযিল করেন।", "Whoever sends blessings upon me once, Allah sends ten blessings upon him.", "সহীহ মুসলিম: ৪০৮", "Sahih Muslim: 408", "EASY")
    ]
    return [('Q_RAW_' + str(i+1).zfill(3), 'rabiul_awwal', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_04_quran_studies():
    items = [
        ("পবিত্র কুরআনের প্রথম অবতীর্ণ পাঁচটি আয়াত কোন সূরার অন্তর্গত?", "The first five revealed verses belong to which Surah?",
         ["সূরা আল-আলাক", "সূরা আল-ফাতিহা", "সূরা আল-মুদ্দাসসির", "সূরা আল-বাক্বারাহ"], ["Surah Al-Alaq", "Surah Al-Fatihah", "Surah Al-Muddathir", "Surah Al-Baqarah"], 0,
         "হেরা গুহায় জিবরাইল (আ.)-এর মাধ্যমে সূরা আলাকের ১-৫ নম্বর আয়াত ('পড় তোমার রবের নামে') নাযিল হয়।", "The first five verses revealed were from Surah Al-Alaq.", "সহীহ বুখারী: ৩", "Sahih Bukhari: 3", "EASY"),

        ("কুরআন মাজীদের সূচিপত্র ও উম্মুল কিতাব (কুরআনের মূল) বলা হয় কোন সূরাকে?", "Which Surah is titled Umm al-Kitab (Mother of the Book)?",
         ["সূরা আল-ফাতিহা", "সূরা আল-বাক্বারাহ", "সূরা আল-ইখলাস", "সূরা ইয়াসীন"], ["Surah Al-Fatihah", "Surah Al-Baqarah", "Surah Al-Ikhlas", "Surah Yasin"], 0,
         "সূরা আল-ফাতিহা হলো কুরআনের সারনির্যাস ও উম্মুল কুরআন।", "Surah Al-Fatihah is called Umm al-Kitab.", "সহীহ বুখারী: ৪৭০৪", "Sahih Bukhari: 4704", "EASY"),

        ("পবিত্র কুরআনের কোন সূরা পাঠ করলে এক-তৃতীয়াংশ (১/৩) কুরআন খতমের সওয়াব পাওয়া যায়?", "Reciting which Surah is equivalent to one-third of the Quran?",
         ["সূরা আল-ইখলাস", "সূরা আল-ফালাক", "সূরা আন-নাস", "সূরা আল-কাওসার"], ["Surah Al-Ikhlas", "Surah Al-Falaq", "Surah An-Nas", "Surah Al-Kawthar"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সূরা ইখলাস মর্যাদায় কুরআনের এক-তৃতীয়াংশের সমান।", "Surah Al-Ikhlas equals one third of the Holy Quran.", "সহীহ বুখারী: ৫০১৫", "Sahih Bukhari: 5015", "EASY"),

        ("পবিত্র কুরআনে কতটি মাক্কী ও কতটি মাদানী সূরা রয়েছে?", "How many Makki and Madani Surahs are there in the Quran?",
         ["৮৬টি মাক্কী ও ২৮টি মাদানী", "৭৫টি মাক্কী ও ৩৯টি মাদানী", "১০০টি মাক্কী ও ১৪টি মাদানী", "৯০টি মাক্কী ও ২৪টি মাদানী"], ["86 Makki and 28 Madani", "75 Makki and 39 Madani", "100 Makki and 14 Madani", "90 Makki and 24 Madani"], 0,
         "হিজরতের পূর্বে অবতীর্ণ ৮৬টি মাক্কী এবং হিজরতের পরে অবতীর্ণ ২৮টি মাদানী সূরা।", "86 Makki Surahs and 28 Madani Surahs constitute the 114 Surahs.", "আল-ইতকান", "Al-Itqan", "EASY"),

        ("পবিত্র কুরআনের দীর্ঘতম আয়াত 'আয়াতুদ দাইন' (ঋণ সংক্রান্ত আয়াত) কোন সূরায় অবস্থিত?", "The longest verse of the Quran (Ayat ad-Dayn) is in which Surah?",
         ["সূরা আল-বাক্বারাহ (২৮২ নম্বর আয়াত)", "সূরা আলে ইমরান", "সূরা আন-নিসা", "সূরা আল-মায়িদাহ"], ["Surah Al-Baqarah (Verse 282)", "Surah Ali Imran", "Surah An-Nisa", "Surah Al-Ma'idah"], 0,
         "সূরা বাকারার ২৮২ নম্বর আয়াতটি কুরআনের দীর্ঘতম একক আয়াত যা আর্থিক লেনদেন সংক্রান্ত।", "Verse 282 of Surah Al-Baqarah is the longest single verse in the Quran.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),

        ("পবিত্র কুরআন দীর্ঘ কত বছর ধরে রাসূলুল্লাহ ﷺ-এর ওপর পর্যায়ক্রমে নাযিল হয়েছিল?", "Over how many years was the Quran gradually revealed to the Prophet ﷺ?",
         ["প্রায় ২৩ বছর", "১০ বছর", "১৫ বছর", "৩০ বছর"], ["Approximately 23 years", "10 years", "15 years", "30 years"], 0,
         "মক্কায় ১৩ বছর এবং মদীনায় ১০ বছর মিলিয়ে প্রায় ২৩ বছর ধরে কুরআন নাযিল সম্পন্ন হয়।", "The Quran was revealed gradually over a period of 23 years.", "আল-বুরহান ফী উলূমিল কুরআন", "Al-Burhan", "EASY"),

        ("কুরআনের কোন সূরাটি পাঠ করলে কবরের আযাব থেকে সুরক্ষার সুসংবাদ রয়েছে?", "Which Surah provides protection from the punishment of the grave?",
         ["সূরা আল-মুলক", "সূরা আস-সাজদাহ", "সূরা আল-কাহফ", "সূরা আদ-দুখান"], ["Surah Al-Mulk", "Surah As-Sajdah", "Surah Al-Kahf", "Surah Ad-Dukhan"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সূরা আল-মুলক তিলাওয়াতকারীকে কবরের আযাব থেকে রক্ষা করে।", "Surah Al-Mulk defends and protects its reciter from the punishment of the grave.", "জামে আত-তিরমিযী: ২৮৯০", "Jami at-Tirmidhi: 2890", "EASY"),

        ("জুমার দিনে কোন সূরা তিলাওয়াত করলে দুই জুমার মধ্যবর্তী সময় নূর দ্বারা আলোকিত থাকে?", "Reciting which Surah on Friday illuminates the believer with light until next Friday?",
         ["সূরা আল-কাহফ", "সূরা ইয়াসীন", "সূরা আল-ওয়াকিয়া", "সূরা আর-রহমান"], ["Surah Al-Kahf", "Surah Yasin", "Surah Al-Waqiah", "Surah Ar-Rahman"], 0,
         "হাদিসে জুমার দিনে সূরা কাহফ তিলাওয়াতকারীর জন্য বিশেষ নূরের ঘোষণা রয়েছে।", "Whoever reads Surah Al-Kahf on Friday will have light between the two Fridays.", "সুনানে বায়হাকী: ৫৯৯৬", "Sunan al-Bayhaqi: 5996", "EASY"),

        ("কুরআন মাজীদের কোন সূরায় মুসাফির ও হকের ক্ষেত্রে 'মুতাশাবিহাত' ও 'মুহকামাত' আয়াতের ব্যাখ্যা দেওয়া হয়েছে?", "Which Surah mentions Muhkamat (Clear) and Mutashabihat (Allegorical) verses?",
         ["সূরা আলে ইমরান (৭ নম্বর আয়াত)", "সূরা আল-বাক্বারাহ", "সূরা আন-নিসা", "সূরা আল-আ'রাফ"], ["Surah Ali Imran (Verse 7)", "Surah Al-Baqarah", "Surah An-Nisa", "Surah Al-A'raf"], 0,
         "সূরা আলে ইমরানের ৭ নম্বর আয়াতে সুস্পষ্ট ও রূপক আয়াতের শ্রেণীবিভাগ বর্ণিত হয়েছে।", "Surah Ali Imran verse 7 explains the categorization of Muhkam and Mutashabih verses.", "সূরা আলে ইমরান: ৭", "Surah Ali Imran: 7", "MEDIUM"),

        ("কুরআনে কোন ফলের নাম সরাসরি উল্লেখ রয়েছে যা জান্নাতের নিয়ামত?", "Which fruit mentioned directly in the Quran is among the bounties of Jannah?",
         ["ডালিম (আনার), খেজুর ও আঙুর", "আপেল", "কমলা", "আম"], ["Pomegranate, Dates, and Grapes", "Apple", "Orange", "Mango"], 0,
         "কুরআনে খেজুর, আঙুর, ডালিম ও জলপাইয়ের কথা বিশেষ নিয়ামত হিসেবে উল্লেখ রয়েছে।", "Dates, grapes, pomegranates, and olives are directly praised in the Quran.", "সূরা আর-রহমান: ৬৮", "Surah Ar-Rahman: 68", "EASY"),

        ("পবিত্র কুরআনের কোন সূরায় 'লাইলাতুল কদর' হাজার মাসের চেয়ে শ্রেষ্ঠ ঘোষণা করা হয়েছে?", "In which Surah is Laylatul Qadr declared better than a thousand months?",
         ["সূরা আল-কদর", "সূরা আদ-দুখান", "সূরা আল-মুযযাম্মিল", "সূরা আল-ইনশিরাহ"], ["Surah Al-Qadr", "Surah Ad-Dukhan", "Surah Al-Muzzammil", "Surah Al-Inshirah"], 0,
         "সূরা আল-কদরে বলা হয়েছে: 'লাইলাতুল কদরি খাইরুম মিন আলফি শাহর' (কদরের রাত হাজার মাসের চেয়ে উত্তম)।", "Surah Al-Qadr states that the Night of Decree is better than 1,000 months.", "সূরা আল-কদর: ৩", "Surah Al-Qadr: 3", "EASY"),

        ("কুরআনের প্রথম হাফেজ ও প্রধান সংকলক কমিটির সভাপতি কে ছিলেন?", "Who served as the chief scribe of the Quran and head of the compilation committee?",
         ["হযরত যায়েদ ইবনে সাবিত (রা.)", "হযরত উবাই বিন কাব (রা.)", "হযরত আলী (রা.)", "হযরত আব্দুল্লাহ ইবনে মাসউদ (রা.)"], ["Zayd ibn Thabit (RA)", "Ubayy bin Ka'b (RA)", "Ali (RA)", "Abdullah ibn Mas'ud (RA)"], 0,
         "আবু বকর ও উসমান (রা.) উভয়ের খেলাফতকালেই যায়েদ বিন সাবিত প্রধান সংকলকের দায়িত্ব পালন করেন।", "Zayd ibn Thabit (RA) was appointed chief compiler of the Quran.", "সহীহ বুখারী: ৪৯৮৬", "Sahih Bukhari: 4986", "MEDIUM"),

        ("কুরআনুল কারীমে মোট কতটি রুকু রয়েছে?", "How many Ruku (sections) are there in the Holy Quran?",
         ["৫৫৮টি", "৫৪০টি", "৬০০টি", "৫২০টি"], ["558", "540", "600", "520"], 0,
         "কুরআন মাজীদের ১১৪টি সূরায় সর্বমোট ৫৫৮টি রুকু রয়েছে।", "There are 558 Ruku sections in the standardized Quran.", "উলূমুল কুরআন", "Ulum al-Quran", "EASY"),

        ("পবিত্র কুরআনে সর্বমোট কতটি আসমানী কিতাবের নাম সরাসরি উল্লেখ করা হয়েছে?", "How many divine scriptures are directly named in the Holy Quran?",
         ["৪টি (তাওরাত, যাবুর, ইনজিল ও কুরআন)", "৩টি", "৫টি", "৬টি"], ["4 (Tawrat, Zabur, Injeel, Quran)", "3", "5", "6"], 0,
         "প্রধান চার আসমানী কিতাবের পাশাপাশি ইব্রাহিম ও মুসা (আ.)-এর সহীফার কথাও এসেছে।", "Four major divine scriptures are named alongside the Scrolls of Ibrahim and Musa.", "সূরা আল-আ'লা: ১৯", "Surah Al-A'la: 19", "EASY"),

        ("পবিত্র কুরআনের কোন সূরাটি সম্পূর্ণ নাযিল হওয়ার সময় হাজার হাজার ফেরেশতা পাহারা দিয়েছিলেন?", "Which Surah was revealed in its entirety accompanied by 70,000 angels?",
         ["সূরা আল-আন'আম", "সূরা আল-বাক্বারাহ", "সূরা আল-মায়িদাহ", "সূরা ইউনুস"], ["Surah Al-An'am", "Surah Al-Baqarah", "Surah Al-Ma'idah", "Surah Yunus"], 0,
         "সূরা আল-আন'আম একবারে নাযিল হয় এবং বর্ণনামতে সত্তর হাজার ফেরেশতা এর সাথে অবতরণ করেন।", "Surah Al-An'am was revealed all at once accompanied by angels.", "তাফসীরে তাবারী", "Tafsir al-Tabari", "MEDIUM"),

        ("কুরআনে কোন প্রাণীর নামে সম্পূর্ণ সূরার নামকরণ করা হয়েছে?", "Which creatures have entire Surahs named after them in the Quran?",
         ["মৌমাছি (নাহল), পিপীলিকা (নামল), মাকড়সা (আনকাবুত), গরু (বাকারা) ও হাতি (ফিল)", "সিংহ ও বাঘ", "মাছ ও তিমি", "ঈগল ও চিল"], ["Bee (Nahl), Ant (Naml), Spider (Ankabut), Cow (Baqarah), Elephant (Fil)", "Lion & Tiger", "Fish & Whale", "Eagle & Hawk"], 0,
         "কুরআনের বেশ কয়েকটি সূরার নাম আল্লাহ তাআলার সৃষ্টির বিস্ময়কর প্রাণীদের নামে রাখা হয়েছে।", "Several Surahs are named after animals reflecting Allah's creation.", "উলূমুল কুরআন", "Ulum al-Quran", "EASY"),

        ("পবিত্র কুরআনে 'বিসমিল্লাহির রাহমানির রাহিম' মোট কতবার উল্লেখ করা হয়েছে?", "How many times does 'Bismillahir Rahmanir Rahim' appear in the Quran?",
         ["১১৪ বার", "১১৩ বার", "১১৫ বার", "১১২ বার"], ["114 times", "113 times", "115 times", "112 times"], 0,
         "সূরা তাওবায় বিসমিল্লাহ নেই, কিন্তু সূরা নামলে দু'বার আসায় মোট সংখ্যা ১১৪-ই পূর্ণ হয়েছে।", "It appears 114 times (Surah At-Tawbah lacks it, but Surah An-Naml has it twice).", "আল-ইতকান", "Al-Itqan", "EASY"),

        ("কুরআনুল কারীমের কোন সূরায় মুমিনদের পারস্পরিক ভ্রাতৃত্ব ও গীবত বর্জনের কঠোর নির্দেশ দেওয়া হয়েছে?", "Which Surah establishes Islamic brotherhood and prohibits backbiting (Gheebah)?",
         ["সূরা আল-হুজুরাত", "সূরা আন-নূর", "সূরা আল-মুনাফিকুন", "সূরা আল-মুজাদালাহ"], ["Surah Al-Hujurat", "Surah An-Nur", "Surah Al-Munafiqun", "Surah Al-Mujadilah"], 0,
         "সূরা হুজুরাতে মুমিনদের একে অপরের ভাই ঘোষণা করে গীবতকে মৃত ভাইয়ের গোশত খাওয়ার সাথে তুলনা করা হয়েছে।", "Surah Al-Hujurat strictly forbids Gheebah and commands brotherhood.", "সূরা আল-হুজুরাত: ১২", "Surah Al-Hujurat: 12", "EASY"),

        ("কুরআনের কোন সূরায় আল্লাহ তাআলা পিতা-মাতার সামনে 'উফ' শব্দটিও না করার নির্দেশ দিয়েছেন?", "In which Surah does Allah command not to say even 'Uff' to parents?",
         ["সূরা আল-ইসরা (বনী ইসরাঈল: ২৩)", "সূরা লুকমান", "সূরা আল-আহকাফ", "সূরা আন-নিসা"], ["Surah Al-Isra (Verse 23)", "Surah Luqman", "Surah Al-Ahqaf", "Surah An-Nisa"], 0,
         "আল্লাহ নির্দেশ দেন: পিতা-মাতার একজন বা উভয় বৃদ্ধ বয়সে পৌঁছলে তাদের সাথে কর্কশ আচরণ করো না এবং 'উফ' পর্যন্ত বোলো না।", "Do not say to them so much as 'uff' and do not repel them (Surah Al-Isra: 23).", "সূরা আল-ইসরা: ২৩", "Surah Al-Isra: 23", "EASY"),

        ("কুরআনের কোন সূরায় হযরত লুকমান (আ.) কর্তৃক তাঁর পুত্রকে প্রদত্ত অমূল্য তাওহীদ ও শিষ্টাচারের উপদেশ বর্ণিত হয়েছে?", "Which Surah contains Luqman's timeless wisdom to his son?",
         ["সূরা লুকমান", "সূরা ইউনুস", "সূরা হূদ", "সূরা ইব্রাহিম"], ["Surah Luqman", "Surah Yunus", "Surah Hud", "Surah Ibrahim"], 0,
         "সূরা লুকমানে শিরক বর্জন, সালাত কায়েম, সৎকাজের আদেশ, অসৎকাজে নিষেধ ও অহংকার বর্জনের উপদেশ রয়েছে।", "Surah Luqman details essential monotheistic and moral advice.", "সূরা লুকমান: ১৩-১৯", "Surah Luqman: 13-19", "EASY"),

        ("কুরআনের কোন সূরায় যুলকারনাইন, খিজির ও আসহাবে কাহাফের শিক্ষণীয় ঘটনা বর্ণিত হয়েছে?", "Which Surah narrates the stories of Dhul-Qarnayn, Khidr, and People of the Cave?",
         ["সূরা আল-কাহফ", "সূরা আল-আম্বিয়া", "সূরা আল-কাসাস", "সূরা ত্বা-হা"], ["Surah Al-Kahf", "Surah Al-Anbiya", "Surah Al-Qasas", "Surah Ta-Ha"], 0,
         "সূরা আল-কাহফে ঈমান ও ফিতনা থেকে রক্ষার এই চারটি প্রধান ঐতিহাসিক ঘটনা বর্ণিত হয়েছে।", "Surah Al-Kahf highlights four trials and their divine solutions.", "সূরা আল-কাহফ", "Surah Al-Kahf", "EASY"),

        ("পবিত্র কুরআনের কোন সূরায় জান্নাতীদের 'আবরার' ও জাহান্নামীদের 'ফুজ্জার' শ্রেণীবিভাগ বিশদভাবে এসেছে?", "Which Surah describes the righteous (Abrar) and the wicked (Fujjar) in detail?",
         ["সূরা আল-ইনফিতার ও সূরা আল-মুতাফফিফীন", "সূরা আল-ফালাক", "সূরা আন-নাসর", "সূরা আল-লাহাব"], ["Surah Al-Infitar & Al-Mutaffifin", "Surah Al-Falaq", "Surah An-Nasr", "Surah Al-Lahab"], 0,
         "আমলনামার সংরক্ষণ এবং জান্নাত ও জাহান্নামের স্থায়ী পরিণতির চিত্র এ সূরাসমূহে বর্ণিত হয়েছে।", "Surahs Al-Infitar and Al-Mutaffifin describe the eternal destinies of Abrar and Fujjar.", "সূরা আল-ইনফিতার: ১৩-১৪", "Surah Al-Infitar: 13-14", "MEDIUM"),

        ("কুরআনের কোন সূরায় হযরত মরিয়ম (আ.) ও তাঁর অলৌকিক পুত্র হযরত ঈসা (আ.)-এর জন্মকাহিনী বর্ণিত হয়েছে?", "Which Surah narrates the miraculous birth of Prophet Isa (AS) to Maryam (AS)?",
         ["সূরা মারিয়াম", "সূরা আত-তাহরীম", "সূরা আন-নূর", "সূরা আল-হাদীদ"], ["Surah Maryam", "Surah At-Tahrim", "Surah An-Nur", "Surah Al-Hadid"], 0,
         "সূরা মারিয়ামে আল্লাহর কুদরতে পিতা ছাড়া হযরত ঈসা (আ.)-এর শুভ জন্মের বিশদ বিবরণ এসেছে।", "Surah Maryam details the miraculous conception and birth of Isa (AS).", "সূরা মারিয়াম: ১৬-৩৪", "Surah Maryam: 16-34", "EASY"),

        ("কুরআনুল কারীমের কোন সূরায় যাকাত বণ্টনের আটটি সুনির্দিষ্ট খাতের বর্ণনা দেওয়া হয়েছে?", "Which Surah specifies the eight eligible categories for Zakat distribution?",
         ["সূরা আত-তাওবাহ (৬০ নম্বর আয়াত)", "সূরা আল-বাক্বারাহ", "সূরা আন-নিসা", "সূরা আল-হাশর"], ["Surah At-Tawbah (Verse 60)", "Surah Al-Baqarah", "Surah An-Nisa", "Surah Al-Hashr"], 0,
         "সূরা আত-তাওবার ৬০ নম্বর আয়াতে যাকাত বণ্টনের আটটি খাত আল্লাহ সুনির্দিষ্ট করেছেন।", "Surah At-Tawbah verse 60 designates the 8 categories of Zakat recipients.", "সূরা আত-তাওবাহ: ৬০", "Surah At-Tawbah: 60", "EASY"),

        ("পবিত্র কুরআনের সবচেয়ে ফযীলতপূর্ণ আয়াত 'আয়াতুল কুরসী' সূরা আল-বাক্বারার কত নম্বর আয়াত?", "What is the verse number of Ayat al-Kursi in Surah Al-Baqarah?",
         ["২৫৫ নম্বর আয়াত", "২৫০ নম্বর আয়াত", "২৮৫ নম্বর আয়াত", "১ নম্বর আয়াত"], ["Verse 255", "Verse 250", "Verse 285", "Verse 1"], 0,
         "সূরা আল-বাক্বারার ২৫৫ নম্বর আয়াত হলো মহাসম্মানিত আয়াতুল কুরসী।", "Ayat al-Kursi is verse 255 of Surah Al-Baqarah.", "সহীহ মুসলিম: ৮০৪", "Sahih Muslim: 804", "EASY"),

        ("পবিত্র কুরআনে 'মাক্বামে মাহমুদ' বা প্রশংসিত উচ্চ মর্যাদার ওয়াদা কোন সূরায় এসেছে?", "In which Surah is the promise of Maqam al-Mahmud (Praised Station) mentioned?",
         ["সূরা আল-ইসরা (৭৯ নম্বর আয়াত)", "সূরা আদ-দুহা", "সূরা আল-কাওসার", "সূরা আল-ইনশিরাহ"], ["Surah Al-Isra (Verse 79)", "Surah Ad-Duha", "Surah Al-Kawthar", "Surah Al-Inshirah"], 0,
         "তাহাজ্জুদের সালাতের আলোচনা প্রসঙ্গে রাসূলুল্লাহ ﷺ-কে মাক্বামে মাহমুদে অধিষ্ঠিত করার ওয়াদা দেওয়া হয়েছে।", "Surah Al-Isra verse 79 promises the Prophet ﷺ the Praised Station (Intercession).", "সূরা আল-ইসরা: ৭৯", "Surah Al-Isra: 79", "MEDIUM"),

        ("কুরআনের কোন সূরায় বদর যুদ্ধের বিজয়কে 'ইয়াওমুল ফুরকান' (সত্য-মিথ্যার মীমাংসার দিন) বলা হয়েছে?", "In which Surah is the Battle of Badr called Yawm al-Furqan?",
         ["সূরা আল-আনফাল", "সূরা আত-তাওবাহ", "সূরা আল-আহযাব", "সূরা আল-ফাতহ"], ["Surah Al-Anfal", "Surah At-Tawbah", "Surah Al-Ahzab", "Surah Al-Fath"], 0,
         "সূরা আল-আনফালে বদর যুদ্ধের গনিমত, ফেরেশতাদের সাহায্য ও সত্যের বিজয় বর্ণিত হয়েছে।", "Surah Al-Anfal details the Battle of Badr and terms it Yawm al-Furqan.", "সূরা আল-আনফাল: ৪১", "Surah Al-Anfal: 41", "EASY"),

        ("কুরআনে কারীমে মানব ভ্রূণের পর্যায়ক্রমিক নিখুঁত বিকাশের বৈজ্ঞানিক বিবরণ কোন সূরায় এসেছে?", "Which Surah describes the stages of human embryonic development?",
         ["সূরা আল-মুমিনূন (১২-১৪ আয়াত)", "সূরা আর-রহমান", "সূরা আল-হাক্কাহ", "সূরা আল-ক্বিয়ামাহ"], ["Surah Al-Mu'minun (Verses 12-14)", "Surah Ar-Rahman", "Surah Al-Haqqah", "Surah Al-Qiyamah"], 0,
         "নুতফাহ, আলাকাহ, মুদগাহ, হাড় গঠন ও গোশত পরানোর নিখুঁত পর্যায় কুরআনে বর্ণিত হয়েছে।", "Surah Al-Mu'minun verses 12-14 accurately detail embryonic development.", "সূরা আল-মুমিনূন: ১২-১৪", "Surah Al-Mu'minun: 12-14", "EASY"),

        ("কুরআনের কোন সূরায় মুনাফিকদের চরিত্র ও অপকৌশল স্পষ্টভাবে উন্মোচন করা হয়েছে?", "Which Surah explicitly exposes the traits and schemes of hypocrites?",
         ["সূরা আল-মুনাফিকুন ও সূরা আত-তাওবাহ", "সূরা আল-ফাতিহা", "সূরা আল-ইখলাস", "সূরা আন-নাসর"], ["Surah Al-Munafiqun & Surah At-Tawbah", "Surah Al-Fatihah", "Surah Al-Ikhlas", "Surah An-Nasr"], 0,
         "সূরা মুনাফিকুনে আব্দুল্লাহ ইবনে উবাই ও কপট মুনাফিকদের ষড়যন্ত্রের পর্দা ফাঁস করা হয়েছে।", "Surah Al-Munafiqun exposes the reality of the hypocrites.", "সূরা আল-মুনাফিকুন", "Surah Al-Munafiqun", "EASY"),

        ("পবিত্র কুরআনের কোন দুটি সূরাকে একত্রে 'মু'আউবিযাতাইন' (আশ্রয় চাওয়ার দুই সূরা) বলা হয়?", "Which two Surahs are collectively known as Al-Mu'awwidhatayn?",
         ["সূরা আল-ফালাক ও সূরা আন-নাস", "সূরা আল-ইখলাস ও সূরা আল-ফাতিহা", "সূরা আল-কাওসার ও সূরা আল-আসর", "সূরা আল-মুলক ও সূরা আল-কাহফ"], ["Surah Al-Falaq and Surah An-Nas", "Surah Al-Ikhlas & Al-Fatihah", "Surah Al-Kawthar & Al-Asr", "Surah Al-Mulk & Al-Kahf"], 0,
         "হিংসুক, জাদু ও শয়তানের অনিষ্ট থেকে আল্লাহর আশ্রয় প্রার্থনার জন্য এ দুটি সূরা নাযিল হয়।", "Surah Al-Falaq and Surah An-Nas are recited for divine protection from evil.", "সহীহ মুসলিম: ৮১৪", "Sahih Muslim: 814", "EASY"),

        ("পবিত্র কুরআনে আল্লাহ তাআলা কার জন্য 'ইন্নাকা লাআলা খুলুক্বিন আযীম' (নিশ্চয়ই আপনি মহান চরিত্রের ওপর অধিষ্ঠিত) বলেছেন?", "For whom did Allah declare: 'Indeed, you are of a great moral character'?",
         ["রাসূলুল্লাহ হযরত মুহাম্মদ ﷺ", "হযরত ইব্রাহিম (আ.)", "হযরত মূসা (আ.)", "হযরত সুলাইমান (আ.)"], ["Prophet Muhammad ﷺ", "Prophet Ibrahim (AS)", "Prophet Musa (AS)", "Prophet Sulaiman (AS)"], 0,
         "সূরা আল-কলমের ৪ নম্বর আয়াতে আল্লাহ বিশ্বনবী ﷺ-এর মহান চরিত্রের সর্বোচ্চ প্রশংসা করেছেন।", "Allah praises the exemplary character of the Prophet ﷺ in Surah Al-Qalam: 4.", "সূরা আল-কলম: ৪", "Surah Al-Qalam: 4", "EASY"),

        ("পবিত্র কুরআনে কতটি পারায় সম্পূর্ণ কুরআন বিভক্ত?", "Into how many equal Juz (parts) is the Quran divided?",
         ["৩০টি পারা", "২৫টি পারা", "২৮টি পারা", "৩২টি পারা"], ["30 Juz", "25 Juz", "28 Juz", "32 Juz"], 0,
         "তিলাওয়াত ও হিফজের সুবিধার্থে কুরআনুল কারীম ৩০টি সমবিভক্ত পারায় বিন্যস্ত।", "The Holy Quran is divided into 30 Juz for recitation and memorization.", "উলূমুল কুরআন", "Ulum al-Quran", "EASY"),

        ("কুরআনের কোন সূরায় মহাবিশ্বের সম্প্রসারণশীলতা (Expanding Universe) সম্পর্কে ইঙ্গিত দেওয়া হয়েছে?", "Which Surah refers to the expansion of the universe?",
         ["সূরা আয-যারিয়াত (৪৭ নম্বর আয়াত)", "সূরা আর-রাদ", "সূরা আল-মুলক", "সূরা ইয়াসীন"], ["Surah Adh-Dhariyat (Verse 47)", "Surah Ar-Ra'd", "Surah Al-Mulk", "Surah Yasin"], 0,
         "আল্লাহ বলেন: 'আমি কুদরতে আসমান সৃষ্টি করেছি এবং নিশ্চয়ই আমি একে সম্প্রসারণকারী।' (সূরা যারিয়াত: ৪৭)।", "And the heaven We constructed with strength, and indeed, We are expanding it.", "সূরা আয-যারিয়াত: ৪৭", "Surah Adh-Dhariyat: 47", "MEDIUM"),

        ("কুরআনের কোন সূরায় 'আসহাবে ফিল' বা হস্তীবাহিনীর ধ্বংসযজ্ঞের ঘটনা বর্ণিত হয়েছে?", "Which Surah recounts the destruction of the Army of the Elephant?",
         ["সূরা আল-ফীল", "সূরা কুরাইশ", "সূরা আল-মাউন", "সূরা আল-হুমাযাহ"], ["Surah Al-Fil", "Surah Quraysh", "Surah Al-Ma'un", "Surah Al-Humazah"], 0,
         "আবরাহার হস্তীবাহিনীকে ঝাঁকে ঝাঁকে আবাবিল পাখি কঙ্কর নিক্ষেপ করে ধ্বংস করে দিয়েছিল।", "Surah Al-Fil details how Allah destroyed Abrahah's army with flocks of birds.", "সূরা আল-ফীল", "Surah Al-Fil", "EASY"),

        ("কুরআনের কোন সূরায় সময়ের শপথ করে মানুষের ক্ষতি ও ঈমানদারদের সফলতার কথা বর্ণিত হয়েছে?", "Which short Surah swears by time to highlight mankind's ultimate success or loss?",
         ["সূরা আল-আসর", "সূরা আত-তাকাসুর", "সূরা আল-হুমাহ", "সূরা আল-কারিয়াহ"], ["Surah Al-Asr", "Surah At-Takathur", "Surah Al-Humazah", "Surah Al-Qari'ah"], 0,
         "ইমাম শাফেয়ী বলেন: মানুষ যদি শুধু এই সূরা আল-আসর নিয়ে গভীরভাবে চিন্তা করত, তবে এটাই তাদের জন্য যথেষ্ট হতো।", "Surah Al-Asr outlines the four prerequisites of salvation: Faith, Good Deeds, Truth, and Patience.", "সূরা আল-আসর: ১-৩", "Surah Al-Asr: 1-3", "EASY")
    ]
    return [('Q_QRN_' + str(i+1).zfill(3), 'quran_studies', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_05_hadith_sunnah():
    items = [
        ("ইসলামের সবচেয়ে বিশুদ্ধ ও নির্ভরযোগ্য হাদিস গ্রন্থ কোনটি?", "Which is the most authentic book of Hadith in Islam?",
         ["সহীহ আল-বুখারী", "সুনানে আবু দাউদ", "সুনানে তিরমিযী", "সুনানে ইবনে মাজাহ"], ["Sahih al-Bukhari", "Sunan Abi Dawud", "Sunan at-Tirmidhi", "Sunan Ibn Majah"], 0,
         "আল-কুরআনের পর মানবজাতির নিকট সর্বাধিক বিশুদ্ধ গ্রন্থ হলো সহীহ আল-বুখারী।", "Sahih al-Bukhari is universally recognized as the most authentic Hadith compilation.", "মুকাদ্দিমাহ ইবনুস সালাহ", "Muqaddimah Ibn al-Salah", "EASY"),

        ("সহীহ আল-বুখারীর সংকলক মহান ইমামের পুরো নাম কী?", "What is the full name of Imam Bukhari, compiler of Sahih al-Bukhari?",
         ["ইমাম মুহাম্মদ ইবনে ইসমাইল আল-বুখারী", "ইমাম মুসলিম ইবনুল হাজ্জাজ", "ইমাম আবু ঈসা তিরমিযী", "ইমাম আহমদ ইবনে হাম্বল"], ["Muhammad ibn Ismail al-Bukhari", "Muslim ibn al-Hajjaj", "Abu Isa at-Tirmidhi", "Ahmad ibn Hanbal"], 0,
         "ইমাম বুখারী (রহ.) গভীর নিষ্ঠা ও কঠোর যাচাই-বাছাইয়ের মাধ্যমে সহীহ বুখারী সংকলন করেন।", "Imam Muhammad ibn Ismail al-Bukhari compiled the Sahih.", "সিয়ারু আ'লামিন নুবালা", "Siyar A'lam an-Nubala", "EASY"),

        ("হাদিস শাস্ত্রের পরিভাষায় যে হাদিসের বর্ণনাকারী সূত্র রাসূলুল্লাহ ﷺ পর্যন্ত নিরবচ্ছিন্নভাবে পৌঁছেছে তাকে কী বলে?", "What is a Hadith called whose chain of narration links directly to the Prophet ﷺ?",
         ["হাদিসে মারফূ", "হাদিসে মাওকূফ", "হাদিসে মাকতূ", "হাদিসে মুনকাতি"], ["Hadith Marfu", "Hadith Mawquf", "Hadith Maqtu", "Hadith Munqati"], 0,
         "যে হাদিসের বক্তব্য সরাসরি রাসূলুল্লাহ ﷺ-এর কথা, কাজ বা মৌনসম্মতি তাকে মারফূ হাদিস বলে।", "A Hadith attributed directly to the Prophet ﷺ is termed Marfu.", "উলূমুল হাদিস", "Ulum al-Hadith", "MEDIUM"),

        ("যে হাদিসের বক্তব্য সাহাবীর কথা বা কাজ হিসেবে বর্ণিত তাকে কী বলে?", "What is a narration attributed to a Companion (Sahabi) called?",
         ["হাদিসে মাওকূফ", "হাদিসে মারফূ", "হাদিসে মাকতূ", "হাদিসে কুদসী"], ["Hadith Mawquf", "Hadith Marfu", "Hadith Maqtu", "Hadith Qudsi"], 0,
         "সাহাবায়ে কেরামের বক্তব্য ও আমলকে হাদিসে মাওকূফ বলা হয়।", "Narrations originating from Companions are termed Mawquf.", "নুখবাতুল ফিকার", "Nukhbat al-Fikar", "MEDIUM"),

        ("যে হাদিসের মূল কথা ও অর্থ আল্লাহ তাআলার পক্ষ থেকে এবং ভাষা রাসূলুল্লাহ ﷺ-এর তাকে কী বলে?", "What is a Hadith where Allah speaks through the Prophet's words called?",
         ["হাদিসে কুদসী", "হাদিসে মারফূ", "হাদিসে মুতাওয়াতির", "হাদিসে হাসান"], ["Hadith Qudsi", "Hadith Marfu", "Hadith Mutawatir", "Hadith Hasan"], 0,
         "হাদিসে কুদসীতে রাসূলুল্লাহ ﷺ বলেন: আল্লাহ তাআলা ইরশাদ করেছেন...।", "Hadith Qudsi contains divine meanings conveyed in the Prophet's ﷺ wording.", "আত-তারিফাত", "At-Ta'rifat", "EASY"),

        ("হাদিস শাস্ত্রে সর্বাধিক হাদিস বর্ণনাকারী সাহাবী কে ছিলেন?", "Which Sahabi narrated the highest number of Hadiths?",
         ["হযরত আবু হুরায়রা (রা.)", "হযরত আব্দুল্লাহ ইবনে উমর (রা.)", "হযরত আনাস ইবনে মালিক (রা.)", "হযরত আয়েশা (রা.)"], ["Abu Hurairah (RA)", "Abdullah ibn Umar (RA)", "Anas ibn Malik (RA)", "Aisha (RA)"], 0,
         "হযরত আবু হুরায়রা (রা.) সর্বমোট ৫,৩৭৪টি হাদিস বর্ণনা করে শীর্ষে অবস্থান করছেন।", "Abu Hurairah (RA) narrated 5,374 Hadiths.", "আল-ইসাবাহ", "Al-Isabah", "EASY"),

        ("মহিলাদের মধ্যে সর্বাধিক হাদিস বর্ণনাকারী উম্মুল মুমিনীন কে?", "Which Mother of the Believers narrated the highest number of Hadiths?",
         ["হযরত আয়েশা সিদ্দিকা (রা.)", "হযরত উম্মে সালামা (রা.)", "হযরত হাফসা (রা.)", "হযরত মায়মুনা (রা.)"], ["Aisha as-Siddiqah (RA)", "Umm Salamah (RA)", "Hafsah (RA)", "Maymunah (RA)"], 0,
         "উম্মুল মুমিনীন হযরত আয়েশা (রা.) ২,২১০টি হাদিস বর্ণনা করেছেন।", "Aisha (RA) narrated 2,210 authentic Hadiths.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("হাদিসের মূল বাণী বা টেক্সট অংশকে কী বলা হয়?", "What is the actual text/content of a Hadith called in Hadith terminology?",
         ["মতন (Matn)", "সনদ (Sanad)", "রাবী (Narrator)", "তারীখ"], ["Matn", "Sanad", "Rawi", "Tarikh"], 0,
         "হাদিসের বর্ণনাকারীদের ধারাবাহিক শিকলকে সনদ এবং মূল বক্তব্যকে মতন বলা হয়।", "The text of the Hadith is called Matn, while the chain of transmitters is Sanad.", "মুকাদ্দিমাহ ইবনুস সালাহ", "Muqaddimah", "EASY"),

        ("হাদিসের বর্ণনাকারীগণের ধারাক্রম বা সূত্রের শৃঙ্খলকে কী বলা হয়?", "What is the chain of narrators of a Hadith called?",
         ["সনদ বা ইসনাদ (Sanad)", "মতন", "রিওয়ায়াত", "দিরবায়াত"], ["Sanad / Isnad", "Matn", "Riwayah", "Dirayah"], 0,
         "সনদ হলো সেই মাধ্যম যার মাধ্যমে হাদিসের মূল বক্তব্যে পৌঁছানো হয়।", "Sanad is the chain of transmitters reaching the text.", "উলূমুল হাদিস", "Ulum al-Hadith", "EASY"),

        ("যে হাদিস এত বিপুল সংখ্যক নির্ভরযোগ্য বর্ণনাকারী বর্ণনা করেছেন যাতে মিথ্যার ওপর একমত হওয়া অসম্ভব তাকে কী বলে?", "What is a Hadith narrated by so many independent chains that conspiracy on falsehood is impossible?",
         ["হাদিসে মুতাওয়াতির", "হাদিসে খবরে ওয়াহেদ", "হাদিসে গরীব", "হাদিসে আযীয"], ["Hadith Mutawatir", "Hadith Ahad", "Hadith Gharib", "Hadith Aziz"], 0,
         "মুতাওয়াতির হাদিস قطعي الثبوت বা নিশ্চিতরূপে প্রমাণিত ও অকাট্য প্রামাণ্য।", "A Mutawatir Hadith imparts definitive knowledge through multiple unbroken chains.", "নুখবাতুল ফিকার", "Nukhbat al-Fikar", "MEDIUM"),

        ("সহীহ আল-বুখারী গ্রন্থে সর্বপ্রথম কোন বিখ্যাত হাদিসটি সন্নিবেশিত হয়েছে?", "Which famous Hadith opens Sahih al-Bukhari as the very first narration?",
         ["সকল কাজের ফলাফল নিয়তের ওপর নির্ভরশীল (ইন্নামাল আ'মালু বিন নিয়্যাত)", "ইসলামের পাঁচ স্তম্ভ", "মিরাজের হাদিস", "ওহীর আগমন"], ["Actions are judged by intentions (Innamal A'malu bin-Niyyat)", "Five pillars", "Mi'raj Hadith", "Revelation"], 0,
         "ইমাম বুখারী ইখলাস ও নিয়তের গুরুত্ব তুলে ধরতে এই বিখ্যাত হাদিস দিয়ে গ্রন্থ শুরু করেছেন।", "Actions are according to intentions (Sahih Bukhari: 1).", "সহীহ বুখারী: ১", "Sahih Bukhari: 1", "EASY"),

        ("সহীহ মুসলিম গ্রন্থের মহান সংকলক ইমাম মুসলিম (রহ.)-এর জন্মস্থান কোথায় ছিল?", "Where was Imam Muslim (author of Sahih Muslim) born?",
         ["ইরানের নিশাপুর", "উজবেকিস্তানের বুখারা", "বাগদাদ", "দামেস্ক"], ["Nishapur (Iran)", "Bukhara", "Baghdad", "Damascus"], 0,
         "ইমাম আবুল হুসাইন মুসলিম ইবনুল হাজ্জাজ আন-নিশাপুরী নিশাপুরে জন্মগ্রহণ করেন।", "Imam Muslim was born in Nishapur.", "সিয়ারু আ'লামিন নুবালা", "Siyar A'lam an-Nubala", "EASY"),

        ("মুয়াত্তা (Al-Muwatta) নামক প্রসিদ্ধ প্রাচীনতম হাদিস ও ফিকহ সংকলনটি কার রচনা?", "Who is the author of the renowned early Hadith compilation Al-Muwatta?",
         ["ইমাম মালিক ইবনে আনাস (রহ.)", "ইমাম আবু হানিফা (রহ.)", "ইমাম শাফেয়ী (রহ.)", "ইমাম আহমদ (রহ.)"], ["Imam Malik ibn Anas", "Imam Abu Hanifa", "Imam Shafi'i", "Imam Ahmad"], 0,
         "মদীনার ইমাম হযরত ইমাম মালিক (রহ.) বিখ্যাত মুয়াত্তা গ্রন্থ সংকলন করেন।", "Imam Malik compiled Al-Muwatta in Madinah.", "তারিখে বাগদাদ", "Tarikh Baghdad", "EASY"),

        ("মুসনাদে আহমদ (Musnad Ahmad) নামক বিশাল হাদিস সংকলনের রচয়িতা কে?", "Who authored the monumental Hadith encyclopedia Musnad Ahmad?",
         ["ইমাম আহমদ ইবনে হাম্বল (রহ.)", "ইমাম তিরমিযী", "ইমাম বায়হাকী", "ইমাম তাবারানী"], ["Imam Ahmad ibn Hanbal", "Imam Tirmidhi", "Imam Bayhaqi", "Imam Tabarani"], 0,
         "ইমাম আহমদ ইবনে হাম্বল (রহ.) প্রায় ত্রিশ হাজার হাদিসের সংকলন মুসনাদ রচনা করেন।", "Imam Ahmad ibn Hanbal compiled over 30,000 Hadiths in his Musnad.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("সিহাহ সিত্তাহর অন্তর্ভুক্ত 'সুনানে আত-তিরমিযী' গ্রন্থের সংকলক কে?", "Who compiled the famous Sunan at-Tirmidhi (Jami at-Tirmidhi)?",
         ["ইমাম আবু ঈসা মুহাম্মদ আত-তিরমিযী", "ইমাম বুখারী", "ইমাম নাসাঈ", "ইমাম ইবনে মাজাহ"], ["Imam Abu Isa Muhammad at-Tirmidhi", "Imam Bukhari", "Imam Nasa'i", "Imam Ibn Majah"], 0,
         "ইমাম আবু ঈসা আত-তিরমিযী (রহ.) জামে আত-তিরমিযী সংকলন করেন।", "Imam Abu Isa at-Tirmidhi compiled Jami at-Tirmidhi.", "মুকাদ্দিমাহ তিরমিযী", "Muqaddimah Tirmidhi", "EASY"),

        ("সিহাহ সিত্তাহর অন্তর্ভুক্ত 'সুনানে আবু দাউদ' গ্রন্থের রচয়িতা কে ছিলেন?", "Who authored Sunan Abi Dawud, specialized in legal rulings (Ahadith al-Ahkam)?",
         ["ইমাম আবু দাউদ সুলাইমান ইবনুল আশ'আস আস-সিজিস্তানী", "ইমাম মুসলিম", "ইমাম দারেমী", "ইমাম হাকিম"], ["Imam Abu Dawud as-Sijistani", "Imam Muslim", "Imam Darimi", "Imam Hakim"], 0,
         "ইমাম আবু দাউদ ফিকহ ও বিধানভিত্তিক হাদিসসমূহ চমৎকার বিন্যাসে সংকলন করেন।", "Imam Abu Dawud compiled Sunan Abi Dawud focusing on Ahkam.", "সিয়ারু আ'লামিন নুবালা", "Siyar A'lam an-Nubala", "EASY"),

        ("হাদিসের বিশুদ্ধতা যাচাইয়ের ক্ষেত্রে বর্ণনাকারীদের জীবনী ও বিশ্বস্ততা পর্যালোচনার বিদ্যাকে কী বলে?", "What is the Islamic science of analyzing narrators' reliability and integrity called?",
         ["ইলমুল জারহ ওয়াত তা'দীল (Ilm al-Jarh wa't-Ta'dil)", "ইলমুল তাফসীর", "ইলমুল কিরাত", "ইলমুল বালাগাত"], ["Ilm al-Jarh wa't-Ta'dil", "Ilm at-Tafsir", "Ilm al-Qira'at", "Ilm al-Balaghah"], 0,
         "বর্ণনাকারীদের দোষ-গুণ, স্মৃতিশক্তি ও সততা যাচাইয়ের গভীর বিজ্ঞান হলো জারহ ও তা'দীল।", "The critical science of verifying narrators' credibility is Jarh wa Ta'dil.", "মুকাদ্দিমাহ ইবনুস সালাহ", "Muqaddimah", "MEDIUM"),

        ("হাদিসের সনদে কোনো বর্ণনাকারী যদি বিশ্বস্ত কিন্তু স্মৃতিশক্তি সামান্য দুর্বল হন তবে সেই হাদিসের মান কী হয়?", "If a narrator is upright but has slightly lesser memory retention, what is the grade of the Hadith?",
         ["হাদিসে হাসান (Hasan / Good)", "হাদিসে সহীহ", "হাদিসে মওজু", "হাদিসে বাতেল"], ["Hadith Hasan", "Hadith Sahih", "Hadith Mawdu", "Hadith Batil"], 0,
         "হাসান হাদিস নির্ভরযোগ্য এবং শরীয়তের হুকুম প্রমাণের জন্য গ্রহণযোগ্য দলিল।", "A Hasan Hadith meets high authenticity standards and serves as legal proof.", "নুখবাতুল ফিকার", "Nukhbat al-Fikar", "MEDIUM"),

        ("হাদিসের নামে মনগড়া বা বানোয়াট কোনো বক্তব্য প্রচার করাকে হাদিস শাস্ত্রে কী বলা হয়?", "What is a fabricated or falsely attributed narration called in Hadith terminology?",
         ["হাদিসে মাওদূ (Mawdu' / Fabricated)", "হাদিসে হাসান", "হাদিসে সহীহ", "হাদিসে যয়ীফ"], ["Hadith Mawdu (Fabricated)", "Hadith Hasan", "Hadith Sahih", "Hadith Da'eef"], 0,
         "ইচ্ছাকৃত মিথ্যা হাদিস রচনা করা কবিরা গুনাহ এবং জাহান্নামে নিজের ঠিকানা বানানোর শামিল।", "Fabricating Hadiths is a major sin explicitly condemned by the Prophet ﷺ.", "সহীহ বুখারী: ১১০", "Sahih Bukhari: 110", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর সুস্পষ্ট বাণী: 'যে ব্যক্তি ইচ্ছাকৃতভাবে আমার নামে মিথ্যা রটনা করে, সে যেন...'", "Prophet's ﷺ explicit warning: 'Whoever intentionally tells a lie against me...'",
         ["জাহান্নামে তার ঠিকানা বানিয়ে নেয়", "তওবা করে", "দান করে", "হজ করে"], ["Let him take his seat in the Hellfire", "Make Tawbah", "Give charity", "Make Hajj"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে আমার ওপর মিথ্যা আরোপ করে সে যেন জাহান্নামে নিজের বাসস্থান নির্ধারণ করে নেয়।", "Whoever lies upon me deliberately, let him take his place in Hell.", "সহীহ বুখারী: ১১০", "Sahih Bukhari: 110", "EASY"),

        ("হাদিস সংকলন সরকারি তত্ত্বাবধানে আনুষ্ঠানিকভাবে সর্বপ্রথম কার নির্দেশে শুরু হয়?", "Under whose official caliphal decree was Hadith compilation officially commissioned?",
         ["হযরত উমর ইবনে আব্দুল আজিজ (রহ.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত মুয়াবিয়া (রা.)"], ["Caliph Umar ibn Abdul Aziz", "Abu Bakr (RA)", "Umar (RA)", "Muawiyah (RA)"], 0,
         "ন্যায়পরায়ণ খলিফা উমর ইবনে আব্দুল আজিজ (রহ.) আবু বকর ইবনে হাযম ও ইবনে শিহাব যুহরীকে হাদিস সংকলনের ফরমান জারি করেন।", "Umar ibn Abdul Aziz officially commissioned formal Hadith recording.", "সহীহ বুখারী: কিতাবুল ইলম", "Sahih Bukhari", "MEDIUM"),

        ("রিয়াদুস সালেহীন (Riyad us-Saliheen) নামক জনপ্রিয় হাদিস সংকলনটির রচয়িতা কে?", "Who compiled the world-renowned Hadith collection Riyad us-Saliheen?",
         ["ইমাম মুহিউদ্দীন আবু যাকারিয়া ইয়াহইয়া আন-নববী (রহ.)", "ইমাম ইবনে তাইমিয়্যাহ", "ইমাম ইবনুল কাইয়্যিম", "ইমাম গাজ্জালী"], ["Imam Yahya an-Nawawi", "Ibn Taymiyyah", "Ibn al-Qayyim", "Imam Ghazali"], 0,
         "ইমাম নববী (রহ.) কুরআন ও সহীহ হাদিসের আলোকে দৈনন্দিন জীবনের অমূল্য এই গ্রন্থ রচনা করেন।", "Imam an-Nawawi compiled the masterpiece Riyad us-Saliheen.", "মুকাদ্দিমাহ রিয়াদুস সালেহীন", "Riyad us-Saliheen", "EASY"),

        ("চল্লিশ হাদিসের সংকলন হিসেবে সর্বাধিক পঠিত 'আল-আরবাঈন আন-নববীয়্যাহ' কার রচনা?", "Who authored the famous 'Forty Hadith' (Al-Arba'in an-Nawawiyyah)?",
         ["ইমাম নববী (রহ.)", "ইমাম বুখারী", "ইমাম মুসলিম", "ইমাম তিরমিযী"], ["Imam an-Nawawi", "Imam Bukhari", "Imam Muslim", "Imam Tirmidhi"], 0,
         "দ্বীন ইসলামের মৌলিক আকাইদ ও শিষ্টাচারের ওপর ইমাম নববীর চল্লিশ হাদিস অত্যন্ত প্রামাণ্য।", "Imam an-Nawawi compiled the essential 42 fundamental Hadiths.", "আল-আরবাঈন", "Al-Arba'in", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'সকল মুমিন পরস্পরের প্রতি একটি সুদৃঢ় ইমারতের মতো, যার এক অংশ...'", "The Prophet ﷺ said: 'Believers are like a single structure, each part...'",
         ["অপর অংশকে শক্তি ও সমর্থন যোগায়", "আলাদা থাকে", "ধ্বংস করে", "উপভোগ করে"], ["Strengthens and supports the other", "Stands alone", "Destroys", "Enjoys"], 0,
         "মুমিনদের পারস্পরিক ঐক্য ও সহমর্মিতার উপমা একটি সুসংহত ইমারত।", "A faithful believer to another is like a brick wall, reinforcing each other.", "সহীহ বুখারী: ৪৮১", "Sahih Bukhari: 481", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সে, যে নিজে কুরআন শিখে এবং...'", "The Prophet ﷺ said: 'The best among you is the one who learns the Quran and...'",
         ["অন্যকে তা শিক্ষা দেয়", "সম্পদ অর্জন করে", "ক্ষমতা পায়", "বিদেশে যায়"], ["Teaches it to others", "Earns wealth", "Gains power", "Travels abroad"], 0,
         "কুরআন শেখা এবং অন্যকে শেখানোই মানবজীবনের সর্বোত্তম কল্যাণকর কাজ।", "The best of you are those who learn the Quran and teach it.", "সহীহ বুখারী: ৫০২৭", "Sahih Bukhari: 5027", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'পরিষ্কার-পরিচ্ছন্নতা ও পবিত্রতা হলো...'", "The Prophet ﷺ said: 'Cleanliness and purity is...'",
         ["ঈমানের অর্ধেক", "ঈমানের এক চতুর্থাংশ", "সাধারণ অভ্যাস", "ঐচ্ছিক কাজ"], ["Half of faith (Shatr al-Iman)", "One quarter of faith", "A habit", "Optional"], 0,
         "ইসলামে আত্মিক ও শারীরিক উভয় পবিত্রতা ঈমানের অপরিহার্য অর্ধাংশ।", "Purity is half of faith.", "সহীহ মুসলিম: ২২৩", "Sahih Muslim: 223", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'প্রকৃত বীর যোদ্ধা সে নয় যে কুস্তিতে প্রতিপক্ষকে আছড়ে ফেলে, বরং প্রকৃত বীর সে যে...'", "The Prophet ﷺ said: 'The strong man is not good at wrestling, but the one who...'",
         ["রাগের সময় নিজেকে নিয়ন্ত্রণে রাখতে পারে", "যুদ্ধ জয় করে", "পাহাড় তোলে", "ধনশালী হয়"], ["Controls himself in a fit of anger", "Wins battles", "Lifts mountains", "Becomes rich"], 0,
         "ক্রোধ সংবরণ করা এবং আত্মনিয়ন্ত্রণ বজায় রাখাই প্রকৃত শক্তির পরিচয়।", "The truly strong person controls himself when angry.", "সহীহ বুখারী: ৬১১৪", "Sahih Bukhari: 6114", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'তোমাদের মধ্যে কেউই প্রকৃত মুমিন হতে পারবে না যতক্ষণ না সে তার ভাইয়ের জন্য তাই পছন্দ করে...'", "The Prophet ﷺ said: 'None of you truly believes until he loves for his brother...'",
         ["যা সে নিজের জন্য পছন্দ করে", "যা অন্যের কাছে নেই", "যা সস্তা", "যা পুরনো"], ["What he loves for himself", "What is unique", "What is cheap", "What is old"], 0,
         "পরের জন্য আত্মত্যাগ ও সমানুভূতি খাঁটি ঈমানের অবিচ্ছেদ্য শর্ত।", "None of you believes until he loves for his brother what he loves for himself.", "সহীহ বুখারী: ১৩", "Sahih Bukhari: 13", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'যে ব্যক্তি মানুষের কৃতজ্ঞতা প্রকাশ করে না, সে মূলত...'", "The Prophet ﷺ said: 'He who does not thank people...'",
         ["আল্লাহরও শোকর আদায় করে না", "ধনী হতে পারে না", "জ্ঞানী নয়", "বীর নয়"], ["Does not show gratitude to Allah", "Cannot be rich", "Is not wise", "Is not brave"], 0,
         "মানুষের উপকারের প্রতি কৃতজ্ঞ হওয়া আল্লাহর প্রতি কৃতজ্ঞতারই অপরিহার্য অংশ।", "He who does not thank people does not thank Allah.", "সুনানে আবু দাউদ: ৪৮১১", "Sunan Abi Dawud: 4811", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'দুনিয়া মুমিনের জন্য কারাগার সদৃশ এবং কাফেরের জন্য...'", "The Prophet ﷺ said: 'The world is a prison for the believer and...'",
         ["জান্নাত সদৃশ", "জাহান্নাম", "মহাসাগর", "মরুভূমি"], ["A paradise for the disbeliever", "Hellfire", "An ocean", "A desert"], 0,
         "দুনিয়া ক্ষণস্থায়ী পরীক্ষার স্থান, মুমিন এখানে আল্লাহর বিধান মেনে সংযত জীবনযাপন করে।", "The world is a prison for the believer and a paradise for the disbeliever.", "সহীহ মুসলিম: ২৯৫৬", "Sahih Muslim: 2956", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'আল্লাহ তাআলা তোমাদের চেহারা ও সম্পদের দিকে তাকান না, বরং তিনি তাকান...'", "The Prophet ﷺ said: 'Allah does not look at your forms and wealth, but looks at...'",
         ["তোমাদের অন্তর এবং তোমাদের আমলের দিকে", "তোমাদের পোশাকের দিকে", "তোমাদের বংশের দিকে", "তোমাদের ক্ষমতার দিকে"], ["Your hearts and your deeds", "Your clothes", "Your lineage", "Your status"], 0,
         "আল্লাহর দরবারে বাহ্যিক আড়ম্বর নয়, বরং অন্তরের তাকওয়া ও নেক আমলই মূল্যায়িত হয়।", "Allah looks at your hearts and deeds, not outward appearances.", "সহীহ মুসলিম: ২৫৬৪", "Sahih Muslim: 2564", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'অন্যায়ভাবে প্রতিবেশীকে কষ্ট দেওয়া ব্যক্তি...'", "The Prophet ﷺ said regarding one whose neighbor is not safe from his harm:",
         ["জান্নাতে প্রবেশ করবে না", "ধনী হবে", "মরবে না", "সুখী হবে"], ["Shall not enter Paradise", "Will be rich", "Will not die", "Will be happy"], 0,
         "প্রতিবেশীর নিরাপত্তা ও অধিকার রক্ষা করা ঈমানের অন্যতম দাবি।", "He whose neighbor is not safe from his harm will not enter Paradise.", "সহীহ মুসলিম: ৪৬", "Sahih Muslim: 46", "EASY"),

        ("হাদিস শাস্ত্রে 'মারাসীলে আবু দাউদ' বা মুরসাল হাদিস বলতে কী বোঝায়?", "What is a Mursal Hadith in Hadith terminology?",
         ["যে হাদিসে তাবিঈ সরাসরি সাহাবীর নাম বাদ দিয়ে রাসূল ﷺ থেকে বর্ণনা করেন", "যে হাদিসে দুইজন রাবী আছেন", "যে হাদিস সম্পূর্ণ মিথ্যা", "যে হাদিস সহীহ বুখারীতে আছে"], ["A Hadith where a Tabi'i attributes directly to the Prophet skipping the Sahabi", "Two narrators", "Fabricated", "In Bukhari"], 0,
         "তাবিঈ যখন সাহাবীর নাম উল্লেখ না করে সরাসরি বলেন 'রাসূলুল্লাহ ﷺ বলেছেন' তখন তা মুরসাল।", "Mursal is when a successor quotes the Prophet ﷺ without naming the companion.", "উলূমুল হাদিস", "Ulum al-Hadith", "MEDIUM"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'লজ্জাশীলতা ও শালীনতা (হায়া)...'", "The Prophet ﷺ said: 'Modesty (Haya)...'",
         ["কল্যাণ ছাড়া আর কিছুই বয়ে আনে না", "ক্ষতিকর", "দুর্বলতা", "অপ্রয়োজনীয়"], ["Brings nothing but good", "Is harmful", "Is weakness", "Is unnecessary"], 0,
         "হায়া হলো ইসলামের অন্যতম প্রধান ভূষণ যা মুমিনকে পাপ থেকে রক্ষা করে।", "Modesty brings only good and is a branch of faith.", "সহীহ বুখারী: ৬১১৭", "Sahih Bukhari: 6117", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'যে ব্যক্তি শেষ রাতে কিংবা একাকী চোখ থেকে আল্লাহর ভয়ে পানি ফেলে...'", "The Prophet ﷺ said regarding a person who weeps out of fear of Allah in seclusion:",
         ["কিয়ামতের দিন সে আরশের ছায়াতলে আশ্রয় পাবে", "সে অপমানিত হবে", "তার ধন হারাবে", "তার রোগ হবে"], ["Will be under the shade of Allah's Throne on Judgment Day", "Humiliated", "Loses wealth", "Falls sick"], 0,
         "আল্লাহর ভয়ে নির্জনে অশ্রুপাতকারী ব্যক্তি কিয়ামতের ময়দানে বিশেষ ছায়াপ্রাপ্ত সাত শ্রেণীর অন্যতম।", "One who weeps out of fear of Allah in privacy will be shaded under the Throne.", "সহীহ বুখারী: ৬৬০", "Sahih Bukhari: 660", "EASY")
    ]
    return [('Q_HAD_' + str(i+1).zfill(3), 'hadith_sunnah', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_06_prophets_stories():
    items = [
        ("মানবজাতির আদি পিতা এবং প্রথম নবী কে?", "Who is the father of mankind and the first Prophet?",
         ["হযরত আদম (আ.)", "হযরত নূহ (আ.)", "হযরত ইব্রাহীম (আ.)", "হযরত মূসা (আ.)"], ["Prophet Adam (AS)", "Prophet Nuh (AS)", "Prophet Ibrahim (AS)", "Prophet Musa (AS)"], 0,
         "হযরত আদম (আ.) হলেন প্রথম মানব ও প্রথম নবী যাকে আল্লাহ মাটি থেকে সৃষ্টি করেছেন।", "Prophet Adam (AS) was the first human and Prophet created from clay.", "কাসাসুল আম্বিয়া", "Qasas al-Anbiya", "EASY"),

        ("কোন নবীকে 'খলিলুল্লাহ' (আল্লাহর অন্তরঙ্গ বন্ধু) উপাধিতে ভূষিত করা হয়েছে?", "Which Prophet is titled 'Khalilullah' (Friend of Allah)?",
         ["হযরত ইব্রাহীম (আ.)", "হযরত আদম (আ.)", "হযরত ঈসা (আ.)", "হযরত ইউনুস (আ.)"], ["Prophet Ibrahim (AS)", "Prophet Adam (AS)", "Prophet Isa (AS)", "Prophet Yunus (AS)"], 0,
         "আল্লাহ তাআলা হযরত ইব্রাহীম (আ.)-কে খলিল হিসেবে গ্রহণ করেছেন (সূরা নিসা: ১২৫)।", "Allah chose Prophet Ibrahim (AS) as a close friend.", "সূরা আন-নিসা: ১২৫", "Surah An-Nisa: 125", "EASY"),

        ("কোন নবীকে নমরুদ বিশাল অগ্নিকুণ্ডে নিক্ষেপ করেছিল এবং আল্লাহ আগুনকে শীতল ও শান্তিময় করেছিলেন?", "Which Prophet was thrown into a massive fire that Allah turned cool and peaceful?",
         ["হযরত ইব্রাহীম (আ.)", "হযরত নূহ (আ.)", "হযরত লুত (আ.)", "হযরত হুদ (আ.)"], ["Prophet Ibrahim (AS)", "Prophet Nuh (AS)", "Prophet Lut (AS)", "Prophet Hud (AS)"], 0,
         "নমরুদ অগ্নিকুণ্ডে নিক্ষেপ করলে আল্লাহ আগুনকে ইব্রাহীমের জন্য শীতল ও শান্তিদায়ক করে দেন।", "Allah commanded the fire: Be cool and peaceful for Ibrahim.", "সূরা আল-আম্বিয়া: ৬৯", "Surah Al-Anbiya: 69", "EASY"),

        ("কোন নবী পিতা ছাড়াই অলৌকিকভাবে কুমারী মাতার গর্ভে জন্মগ্রহণ করেছিলেন?", "Which Prophet was miraculously born without a father from a virgin mother?",
         ["হযরত ঈসা (আ.)", "হযরত ইয়াহইয়া (আ.)", "হযরত মূসা (আ.)", "হযরত যাকারিয়া (আ.)"], ["Prophet Isa (Jesus) (AS)", "Prophet Yahya (AS)", "Prophet Musa (AS)", "Prophet Zakariyya (AS)"], 0,
         "হযরত ঈসা (আ.) হযরত মারিয়াম (আ.)-এর গর্ভে আল্লাহর কুদরতি রুহ ফুৎকারে পিতা ছাড়া জন্মগ্রহণ করেন।", "Prophet Isa (AS) was born of the Virgin Maryam by Allah's divine word.", "সূরা মারিয়াম: ১৯-২১", "Surah Maryam: 19-21", "EASY"),

        ("পবিত্র কুরআনে কোন নবীর কাহিনীর সূরায় আল্লাহ তাআলাকে 'আহসানুল কাসাস' (সর্বোত্তম কাহিনী) হিসেবে অভিহিত করেছেন?", "Which Prophet's narrative is described in the Quran as 'Ahsan al-Qasas' (The Best of Stories)?",
         ["হযরত ইউসুফ (আ.)", "হযরত আদম (আ.)", "হযরত মূসা (আ.)", "হযরত দাউদ (আ.)"], ["Prophet Yusuf (Joseph) (AS)", "Prophet Adam (AS)", "Prophet Musa (AS)", "Prophet Dawud (AS)"], 0,
         "সূরা ইউসুফের কাহিনীকে পবিত্র কুরআনে আহসানুল কাসাস বলা হয়েছে।", "The story of Prophet Yusuf (AS) is called Ahsan al-Qasas.", "সূরা ইউসুফ: ৩", "Surah Yusuf: 3", "EASY"),

        ("কোন নবী দীর্ঘ ৯৫০ বছর ধরে তাঁর জাতিকে এক আল্লাহর ইবাদতের দাওয়াত দিয়েছিলেন?", "Which Prophet preached monotheism to his people for 950 years?",
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
         "আল্লাহ ঈসা (আ.)-কে সশরীরে আসমানে উঠিয়ে নিয়েছেন এবং কিয়ামতের পূর্বে তিনি দাজ্জালকে বধ করতে আগমন করবেন।", "Allah raised Isa (AS) alive, and he will descend before Qiyamah.", "সূরা আন-নিসা: ১৫৮", "Surah An-Nisa: 158", "EASY"),

        ("কোন নবীকে আল্লাহ তাআলা বায়ু নিয়ন্ত্রণ এবং পশুপাখি ও জিনদের ভাষা বোঝার অলৌকিক ক্ষমতা দিয়েছিলেন?", "Which Prophet was granted control over wind, and understood the language of birds, animals, and Jinn?",
         ["হযরত সুলাইমান (আ.)", "হযরত দাউদ (আ.)", "হযরত ইউসুফ (আ.)", "হযরত মূসা (আ.)"], ["Prophet Sulaiman (Solomon) (AS)", "Prophet Dawud (AS)", "Prophet Yusuf (AS)", "Prophet Musa (AS)"], 0,
         "সুলাইমান (আ.) বাতাস, জিন ও জীবজন্তুর ওপর শাসন ও তাদের ভাষা বোঝার বিশেষ মুজিযা পেয়েছিলেন।", "Prophet Sulaiman (AS) was given dominion over wind and understood animal speech.", "সূরা আন-নামল: ১৬-১৭", "Surah An-Naml: 16-17", "EASY"),

        ("কোন নবীর জন্য আল্লাহ তাআলা শক্ত লোহাকে মোমের মতো নরম করে দিয়েছিলেন?", "For which Prophet did Allah soften iron like wax to make armor?",
         ["হযরত দাউদ (আ.)", "হযরত সুলাইমান (আ.)", "হযরত মূসা (আ.)", "হযরত হারুন (আ.)"], ["Prophet Dawud (David) (AS)", "Prophet Sulaiman (AS)", "Prophet Musa (AS)", "Prophet Harun (AS)"], 0,
         "আল্লাহ তাআলা হযরত দাউদ (আ.)-এর জন্য লোহাকে কোমল করেছিলেন যাতে তিনি বর্ম তৈরি করতে পারেন।", "Allah made iron pliable for Prophet Dawud (AS) to craft chainmail armor.", "সূরা সাবা: ১০", "Surah Saba: 10", "EASY"),

        ("বিশাল তিমির পেটে বন্দি অবস্থায় কোন নবী আল্লাহর কাছে 'লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নি কুনতু মিনাজ জোয়ালিমীন' দোয়া করেছিলেন?", "Which Prophet recited the famous Dua in the belly of the whale?",
         ["হযরত ইউনুস (আ.) (যুন-নূন)", "হযরত আইয়ুব (আ.)", "হযরত ইয়াকুব (আ.)", "হযরত হুদ (আ.)"], ["Prophet Yunus (Jonah) (AS)", "Prophet Ayyub (AS)", "Prophet Yaqub (AS)", "Prophet Hud (AS)"], 0,
         "হযরত ইউনুস (আ.) তিমির পেটে অন্ধকারে এই দোয়া করে আল্লাহর ক্ষমা ও মুক্তি লাভ করেন।", "Prophet Yunus (AS) prayed from the belly of the whale and was delivered.", "সূরা আল-আম্বিয়া: ৮৭", "Surah Al-Anbiya: 87", "EASY"),

        ("মারাত্মক রোগ ও চরম বিপদে অতুলনীয় সবর (ধৈর্য)-এর উজ্জ্বল দৃষ্টান্ত স্থাপন করেছিলেন কোন নবী?", "Which Prophet became the legendary paragon of patience (Sabr) during severe illness?",
         ["হযরত আইয়ুব (আ.)", "হযরত ইয়াকুব (আ.)", "হযরত ইউসুফ (আ.)", "হযরত যাকারিয়া (আ.)"], ["Prophet Ayyub (Job) (AS)", "Prophet Yaqub (AS)", "Prophet Yusuf (AS)", "Prophet Zakariyya (AS)"], 0,
         "হযরত আইয়ুব (আ.) দীর্ঘ বছর কঠিন অসুস্থতা ও সম্পদ হারানোর পরও আল্লাহর প্রতি পরম কৃতজ্ঞ ও ধৈর্যশীল ছিলেন।", "Prophet Ayyub (AS) endured prolonged affliction with steadfast faith and patience.", "সূরা আল-আম্বিয়া: ৮৩-৮৪", "Surah Al-Anbiya: 83-84", "EASY"),

        ("হযরত ইউসুফ (আ.)-এর বিরহে কেঁদে কেঁদে দৃষ্টিশক্তি হারিয়ে ফেলেছিলেন কোন নবী পিতা?", "Which Prophet wept for his separated son Yusuf until he lost his eyesight?",
         ["হযরত ইয়াকুব (আ.) (ইসরাঈল)", "হযরত ইসহাক (আ.)", "হযরত ইব্রাহিম (আ.)", "হযরত লুত (আ.)"], ["Prophet Yaqub (Jacob) (AS)", "Prophet Ishaq (AS)", "Prophet Ibrahim (AS)", "Prophet Lut (AS)"], 0,
         "হযরত ইয়াকুব (আ.) ইউসুফের শোকে কেঁদে অন্ধ হয়ে যান এবং পরে ইউসুফের জামার স্পর্শে দৃষ্টি ফিরে পান।", "Prophet Yaqub (AS) wept for Yusuf (AS) until his eyesight returned via Yusuf's shirt.", "সূরা ইউসুফ: ৮৪-৯৩", "Surah Yusuf: 84-93", "EASY"),

        ("কুরআনে বর্ণিত আদ (Aad) জাতির নিকট কোন নবীকে পথপ্রদর্শক হিসেবে পাঠানো হয়েছিল?", "Which Prophet was sent to the ancient civilization of Aad?",
         ["হযরত হুদ (আ.)", "হযরত সালেহ (আ.)", "হযরত শুআইব (আ.)", "হযরত লুত (আ.)"], ["Prophet Hud (AS)", "Prophet Salih (AS)", "Prophet Shu'ayb (AS)", "Prophet Lut (AS)"], 0,
         "শক্তিশালী অহংকারী আদ জাতির নিকট হযরত হুদ (আ.) তাওহীদের দাওয়াত নিয়ে এসেছিলেন।", "Prophet Hud (AS) was sent to the powerful people of Aad.", "সূরা হূদ: ৫০", "Surah Hud: 50", "EASY"),

        ("মাদইয়ানবাসীদের কাছে ওজনে কম দেওয়া ও প্রতারণা বন্ধের দাওয়াত নিয়ে এসেছিলেন কোন নবী?", "Which Prophet warned the people of Madyan against cheating in weights and measures?",
         ["হযরত শুআইব (আ.) (খতিবুল আম্বিয়া)", "হযরত লুত (আ.)", "হযরত হুদ (আ.)", "হযরত সালেহ (আ.)"], ["Prophet Shu'ayb (AS)", "Prophet Lut (AS)", "Prophet Hud (AS)", "Prophet Salih (AS)"], 0,
         "হযরত শুআইব (আ.) মাপে কম দেওয়ার সামাজিক অবিচারের বিরুদ্ধে তাওহীদের বাণী প্রচার করেন।", "Prophet Shu'ayb (AS) called Madyan to fair trade and monotheism.", "সূরা আল-আ'রাফ: ৮৫", "Surah Al-A'raf: 85", "EASY"),

        ("কোন নবী ও তাঁর পুত্র মিলে মক্কায় পবিত্র কাবার পুনর্নির্মাণ সম্পন্ন করেছিলেন?", "Which Prophet and his son rebuilt the foundations of the Holy Kaaba in Makkah?",
         ["হযরত ইব্রাহিম (আ.) ও হযরত ইসমাইল (আ.)", "হযরত আদম (আ.) ও শীষ (আ.)", "হযরত নূহ (আ.) ও সাম", "হযরত দাউদ ও সুলাইমান"], ["Prophet Ibrahim (AS) and Prophet Ismail (AS)", "Adam & Seth", "Nuh & Shem", "Dawud & Sulaiman"], 0,
         "পিতা ইব্রাহিম ও পুত্র ইসমাইল (আ.) কাবার প্রাচীর তুলে আল্লাহর দরবারে কবুলিয়তের দোয়া করেন।", "Ibrahim (AS) and Ismail (AS) raised the foundations of the House of Allah.", "সূরা আল-বাক্বারাহ: ১২৭", "Surah Al-Baqarah: 127", "EASY"),

        ("হযরত ইব্রাহিম (আ.) আল্লাহর আদেশে প্রিয় পুত্র হযরত ইসমাইল (আ.)-কে কোরবানি করতে গিয়েছিলেন কোথায়?", "Where did Prophet Ibrahim (AS) take Ismail (AS) to fulfill the sacrifice commanded by Allah?",
         ["মিনায়", "আরাফাতে", "মুযদালিফায়", "মদীনায়"], ["Mina", "Arafat", "Muzdalifah", "Madinah"], 0,
         "মিনায় ইব্রাহিম (আ.) পুত্রের গলায় ছুরি চালাতে উদ্যত হলে আল্লাহ জান্নাত থেকে দুম্বা পাঠিয়ে তা কোরবানি করান।", "In Mina, Allah replaced Ismail (AS) with a magnificent ram from Jannah.", "সূরা আস-সাফফাত: ১০২-১০৭", "Surah As-Saffat: 102-107", "EASY"),

        ("হযরত মূসা (আ.)-এর সহোদর ভাই যিনি একজন নবী ছিলেন তাঁর নাম কী?", "What was the name of Prophet Musa's (AS) brother who was also appointed as a Prophet?",
         ["হযরত হারুন (আ.)", "হযরত ইউশা (আ.)", "হযরত শামউয়েল (আ.)", "হযরত দাউদ (আ.)"], ["Prophet Harun (Aaron) (AS)", "Prophet Yusha (AS)", "Prophet Shamweel (AS)", "Prophet Dawud (AS)"], 0,
         "মূসা (আ.)-এর দোয়ার ফলে আল্লাহ তাঁর ভাই হারুন (আ.)-কে নবুওয়াত ও সাহায্যকারী দান করেন।", "Allah appointed Harun (AS) as a Prophet and helper upon Musa's supplication.", "সূরা মারিয়াম: ৫৩", "Surah Maryam: 53", "EASY"),

        ("ফেরাউনের দরবারে লালিত-পালিত হয়েছিলেন কোন সম্মানিত নবী?", "Which Prophet was raised in the royal palace of Pharaoh (Fir'awn)?",
         ["হযরত মূসা (আ.)", "হযরত ইউসুফ (আ.)", "হযরত হারুন (আ.)", "হযরত দাউদ (আ.)"], ["Prophet Musa (AS)", "Prophet Yusuf (AS)", "Prophet Harun (AS)", "Prophet Dawud (AS)"], 0,
         "আল্লাহর কুদরতে শিশু মূসা (আ.)-এর সিন্ধুক নদীর পানিতে ভেসে আসিয়া (রা.)-এর কোলে পৌঁছায় এবং ফেরাউনের প্রাসাদে প্রতিপালিত হন।", "By divine plan, Musa (AS) was protected and raised inside Pharaoh's palace.", "সূরা আল-কাসাস: ৭-৯", "Surah Al-Qasas: 7-9", "EASY"),

        ("হযরত ইউসুফ (আ.)-কে হিংসুক ভাইয়েরা অন্ধকূপে ফেলে দিলে কোন কাফেলা তাঁকে উদ্ধার করে কোথায় বিক্রি করেছিল?", "Where was Prophet Yusuf (AS) sold after being rescued from the well?",
         ["মিসরের রাজদরবারে (আযীযে মিসর)", "রোম সাম্রাজ্যে", "পারস্যে", "ইয়েমেনে"], ["In Egypt to the Aziz of Egypt", "Rome", "Persia", "Yemen"], 0,
         "মিসরের অর্থমন্ত্রী (আযীযে মিসর) ইউসুফ (আ.)-কে কিনে নেন এবং পরবর্তীতে তিনি মিসরের শাসনকর্তা হন।", "Yusuf (AS) was taken to Egypt and later rose to govern its treasuries.", "সূরা ইউসুফ: ২১-৫৫", "Surah Yusuf: 21-55", "EASY"),

        ("স্বপ্নের নির্ভুল তাফসীর ও ব্যাখ্যা প্রদানের অলৌকিক জ্ঞান আল্লাহ কোন নবীকে দিয়েছিলেন?", "Which Prophet was blessed with the divine miracle of accurate dream interpretation?",
         ["হযরত ইউসুফ (আ.)", "হযরত মূসা (আ.)", "হযরত সুলাইমান (আ.)", "হযরত দাউদ (আ.)"], ["Prophet Yusuf (Joseph) (AS)", "Prophet Musa (AS)", "Prophet Sulaiman (AS)", "Prophet Dawud (AS)"], 0,
         "আল্লাহ হযরত ইউসুফ (আ.)-কে স্বপ্নের রহস্য উন্মোচন ও ভবিষ্যৎ দুর্যোগ মোকাবিলার বিশেষ প্রজ্ঞা দান করেন।", "Allah bestowed upon Yusuf (AS) the knowledge of dream interpretation.", "সূরা ইউসুফ: ৬", "Surah Yusuf: 6", "EASY"),

        ("কোন নবীর দোয়ায় আল্লাহ আকাশ থেকে খাবারভর্তি দস্তরখান (আল-মায়িদাহ) অবতীর্ণ করেছিলেন?", "For which Prophet's disciples did Allah send down a heavenly table spread with food (Al-Ma'idah)?",
         ["হযরত ঈসা (আ.)", "হযরত মূসা (আ.)", "হযরত দাউদ (আ.)", "হযরত ইব্রাহিম (আ.)"], ["Prophet Isa (Jesus) (AS)", "Prophet Musa (AS)", "Prophet Dawud (AS)", "Prophet Ibrahim (AS)"], 0,
         "হাওয়ারীদের অনুরোধে হযরত ঈসা (আ.)-এর দোয়ায় জান্নাতী খাদ্যের টেবিল (আল-মায়িদাহ) অবতীর্ণ হয়।", "Allah sent down the table spread from heaven as requested by Isa's disciples.", "সূরা আল-মায়িদাহ: ১১৪-১১৫", "Surah Al-Ma'idah: 114-115", "EASY"),

        ("হযরত নূহ (আ.)-এর কিশতী মহাপ্লাবনের পর কোন পাহাড়ের চূড়ায় নোঙর করেছিল?", "On which mountain did the Ark of Prophet Nuh (AS) come to rest after the Great Flood?",
         ["জুদী পাহাড়", "তুর পাহাড়", "আরাফাত পাহাড়", "হেরা পাহাড়"], ["Mount Judi", "Mount Sinai", "Mount Arafat", "Mount Hira"], 0,
         "কুরআনে স্পষ্ট বর্ণিত আছে যে মহাপ্লাবনের শেষে নূহ (আ.)-এর কিশতী জুদী পাহাড়ের ওপর স্থির হয়।", "The Ark rested on Mount Judi after the waters subsided (Surah Hud: 44).", "সূরা হূদ: ৪৪", "Surah Hud: 44", "EASY"),

        ("হযরত নূহ (আ.)-এর অবাধ্য পুত্র যে ঈমান না এনে প্লাবনে ডুবে মারা গিয়েছিল তার নাম কী ছিল?", "What was the name of the disbelieving son of Prophet Nuh (AS) who drowned in the Flood?",
         ["ইয়াম (কেনান)", "সাম", "হাম", "ইয়াফিস"], ["Yam (Canaan)", "Shem", "Ham", "Japheth"], 0,
         "পিতার ব্যাকুল আহ্বান সত্ত্বেও কেনান পাহাড়ে আশ্রয়ের দম্ভ করে এবং মহাপ্লাবনে নিমজ্জিত হয়।", "Nuh's disbelieving son refused the Ark and drowned in the waves.", "সূরা হূদ: ৪২-৪৩", "Surah Hud: 42-43", "EASY"),

        ("হযরত ইব্রাহিম (আ.)-এর বড় পুত্রের নাম কী ছিল যাঁর বংশে বিশ্বনবী মুহাম্মদ ﷺ আগমন করেন?", "What was the name of Prophet Ibrahim's (AS) eldest son from whom Prophet Muhammad ﷺ descended?",
         ["হযরত ইসমাইল (আ.)", "হযরত ইসহাক (আ.)", "হযরত ইয়াকুব (আ.)", "হযরত ইউসুফ (আ.)"], ["Prophet Ismail (Ishmael) (AS)", "Prophet Ishaq (AS)", "Prophet Yaqub (AS)", "Prophet Yusuf (AS)"], 0,
         "হযরত হাজেরা (আ.)-এর গর্ভে জন্ম নেওয়া ইসমাইল (আ.)-এর বংশেই সর্বশেষ নবী মুহাম্মদ ﷺ প্রেরিত হন।", "Prophet Muhammad ﷺ descended from Prophet Ismail (AS).", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("হযরত ইব্রাহিম (আ.)-এর দ্বিতীয় পুত্র হযরত ইসহাক (আ.)-এর বংশে কোন বিখ্যাত জাতি উদ্ভূত হয়েছিল?", "Which nation descended from Prophet Ibrahim's second son Prophet Ishaq (AS)?",
         ["বনী ইসরাঈল", "কুরাইশ", "রোমান", "পারসিক"], ["Bani Israel (Children of Israel)", "Quraysh", "Romans", "Persians"], 0,
         "ইসহাক (আ.)-এর পুত্র ইয়াকুব (আ.)-এর অপর নাম ছিল ইসরাঈল, যাঁর বংশধররাই বনী ইসরাঈল।", "Bani Israel descended from Prophet Yaqub (Israel), son of Ishaq (AS).", "কাসাসুল আম্বিয়া", "Qasas al-Anbiya", "EASY"),

        ("অহংকারী অত্যাচারী জালুতকে পাথর ছুড়ে বধ করেছিলেন কোন তরুণ নবী?", "Which young future Prophet slew the tyrant Goliath (Jalut) with a sling?",
         ["হযরত দাউদ (আ.)", "হযরত মূসা (আ.)", "হযরত ইউশা (আ.)", "হযরত শামউয়েল (আ.)"], ["Prophet Dawud (David) (AS)", "Prophet Musa (AS)", "Prophet Yusha (AS)", "Prophet Shamweel (AS)"], 0,
         "তালুতের নেতৃত্বে যুদ্ধে দাউদ (আ.) জালুতকে হত্যা করেন এবং পরবর্তীতে বাদশাহ ও নবী হন।", "Dawud (AS) slew Jalut (Goliath) and was granted kingdom and wisdom.", "সূরা আল-বাক্বারাহ: ২৫১", "Surah Al-Baqarah: 251", "EASY"),

        ("হযরত ঈসা (আ.) দোলনায় থাকা অবস্থায় মাতৃক্রোড়ে কী অলৌকিক কথা বলেছিলেন?", "What did infant Prophet Isa (AS) miraculously speak from the cradle?",
         ["'আমি তো আল্লাহর বান্দা, তিনি আমাকে কিতাব দিয়েছেন এবং নবী করেছেন'", "'আমি বাদশাহ'", "'আমি ধনী'", "'আমি শক্তিশালী'"], ["'Indeed, I am the servant of Allah; He has given me the Scripture and made me a Prophet'", "'I am a king'", "'I am rich'", "'I am mighty'"], 0,
         "শিশু ঈসা (আ.) দোলনায় কথা বলে মাতা মারিয়ামের পবিত্রতা ও নিজের নবুওয়াতের সাক্ষ্য দেন।", "Infant Isa (AS) spoke from the cradle affirming his servitude to Allah and Prophethood.", "সূরা মারিয়াম: ৩০", "Surah Maryam: 30", "EASY"),

        ("হযরত আদম (আ.) ও হাওয়া (আ.)-এর ভুলের পর আল্লাহ তাঁদের কোন তওবার বাক্য শিখিয়ে দিয়েছিলেন?", "Which repentance prayer did Allah teach Adam and Eve (Hawwa)?",
         ["'রাব্বানা যালামনা আনফুসানা ওয়া ইল্লাম তাগফিরলানা ওয়া তারহামনা লানাকূনান্না মিনাল খাসিরীন'", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "লা হাওলা ওয়ালা কুওয়াতা"], ["'Rabbana Zalamna Anfusana wa il-lam taghfir lana wa tarhamna lana koonanna minal khasireen'", "Subhanallah", "Alhamdulillah", "La hawla"], 0,
         "আদম ও হাওয়া (আ.) এই ব্যাকুল দোয়ার মাধ্যমে আল্লাহর কাছে ক্ষমা প্রার্থনা করেন এবং আল্লাহ তওবা কবুল করেন।", "Adam and Hawwa prayed: Our Lord, we have wronged ourselves (Surah Al-A'raf: 23).", "সূরা আল-আ'রাফ: ২৩", "Surah Al-A'raf: 23", "EASY"),

        ("আল্লাহ তাআলা কোন নবীকে সর্বপ্রথম কলম দিয়ে লেখা ও কাপড় সেলাইয়ের বিদ্যা শিখিয়েছিলেন?", "Which early Prophet was taught writing with the pen and tailoring garments?",
         ["হযরত ইদরীস (আ.)", "হযরত শীষ (আ.)", "হযরত হুদ (আ.)", "হযরত সালেহ (আ.)"], ["Prophet Idris (Enoch) (AS)", "Prophet Seth (AS)", "Prophet Hud (AS)", "Prophet Salih (AS)"], 0,
         "হযরত ইদরীস (আ.) ছিলেন প্রথম ব্যক্তি যিনি কলম দিয়ে লিখেছিলেন এবং বিজ্ঞান ও কাপড়ের সূক্ষ্ম কাজ জানতেন।", "Prophet Idris (AS) was the first to write with the pen and practice tailoring.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "MEDIUM"),

        ("কুরআনে বর্ণিত কোন নবীকে 'যুল কিফল' (প্রতিশ্রুতি পালনকারী দায়িত্বশীল) নামে স্মরণ করা হয়েছে?", "Which Prophet praised in the Quran is known as Dhul-Kifl (Possessor of the Fold)?",
         ["হযরত যুল কিফল (আ.)", "হযরত দাউদ (আ.)", "হযরত ইয়াহইয়া (আ.)", "হযরত ইউনুস (আ.)"], ["Prophet Dhul-Kifl (AS)", "Prophet Dawud (AS)", "Prophet Yahya (AS)", "Prophet Yunus (AS)"], 0,
         "হযরত যুল কিফল (আ.) কঠিন দায়িত্ব ও প্রতিশ্রুতি পালনে অটল ধৈর্য ও সততার প্রতীক ছিলেন।", "Prophet Dhul-Kifl (AS) is praised in Surah Al-Anbiya for righteous patience.", "সূরা আল-আম্বিয়া: ৮৫", "Surah Al-Anbiya: 85", "EASY"),

        ("সদুম (Sodom) নগরের পথভ্রষ্ট ও চরিত্রহীন জাতির কাছে কোন নবীকে হেদায়াতের জন্য পাঠানো হয়েছিল?", "Which Prophet was sent to the corrupt people of Sodom?",
         ["হযরত লুত (আ.)", "হযরত হুদ (আ.)", "হযরত সালেহ (আ.)", "হযরত শুআইব (আ.)"], ["Prophet Lut (Lot) (AS)", "Prophet Hud (AS)", "Prophet Salih (AS)", "Prophet Shu'ayb (AS)"], 0,
         "হযরত লুত (আ.) অশ্লীলতা ও সমকামিতা বর্জনের ডাক দেন, অবাধ্য জাতি পাথরের বৃষ্টিতে ধ্বংস হয়।", "Prophet Lut (AS) called the people of Sodom to abandon lewdness and fear Allah.", "সূরা হূদ: ৭৮-৮২", "Surah Hud: 78-82", "EASY"),

        ("হযরত মূসা (আ.) আল্লাহর নির্দেশমতো জ্ঞান অন্বেষণের সফরে কোন পুণ্যবান বান্দার সাথে যাত্রা করেছিলেন?", "With which wise servant of Allah did Prophet Musa (AS) travel to seek deeper knowledge?",
         ["হযরত খিজির (আ.)", "হযরত হারুন (আ.)", "হযরত ইউশা (আ.)", "হযরত লুকমান (আ.)"], ["Al-Khidr (AS)", "Harun (AS)", "Yusha (AS)", "Luqman (AS)"], 0,
         "সূরা কাহফে মূসা (আ.) ও খিজির (আ.)-এর রহস্যময় ও হেকমতপূর্ণ ঐতিহাসিক সফরের বিবরণ রয়েছে।", "Prophet Musa (AS) traveled with Al-Khidr to witness divine wisdom behind destiny.", "সূরা আল-কাহফ: ৬৫-৮২", "Surah Al-Kahf: 65-82", "EASY")
    ]
    return [('Q_PRO_' + str(i+1).zfill(3), 'prophets_stories', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_batch_1_to_6():
    q = []
    q.extend(get_cat_01_gk())
    q.extend(get_cat_02_ibadah())
    q.extend(get_cat_03_rabiul_awwal())
    q.extend(get_cat_04_quran_studies())
    q.extend(get_cat_05_hadith_sunnah())
    q.extend(get_cat_06_prophets_stories())
    return q
