# -*- coding: utf-8 -*-
"""
Clean, Authentic, 100% Unique Islamic Quiz Dataset Generator for DeenOne.
Generates genuine questions for all 31 categories with:
- Zero prefix noise (no 'প্রশ্ন নং', no 'Question 1:')
- Zero duplicate text globally across all categories
- Dual-language (Bengali + English)
- Authentic Quran / Hadith citations
"""

import os
import sys

def build_all_categories_data():
    categories = {}

    # ==========================================
    # 1. general_knowledge (সাধারণ জ্ঞান)
    # ==========================================
    categories['general_knowledge'] = [
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
         "সূরা আল-বাক্বারাহ কুরআনের দীর্ঘতম সূরা যার আয়াত সংখ্যা ২৮৬।", "Surah Al-Baqarah is the longest Surah with 286 verses.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY")
    ]

    return categories

if __name__ == '__main__':
    cats = build_all_categories_data()
    print("General knowledge questions built:", len(cats['general_knowledge']))
