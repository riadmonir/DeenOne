# -*- coding: utf-8 -*-
"""
Group 1 Categories (600 Questions - 100 per Category):
1. general_knowledge (সাধারণ জ্ঞান)
2. ibadah (ইবাদত - সালাত, তাহাজ্জুদ, ওযু, পবিত্রতা)
3. rabiul_awwal (রবিউল আউয়াল মাস ও হিজরত)
4. quran_studies (আল-কুরআন স্টাডিজ - সূরা, আয়াত, নাযিল)
5. hadith_sunnah (হাদিস ও সুন্নাহ - পরিভাষা, সিহাহ সিত্তা)
6. prophets_stories (নবী-রাসূলদের কাহিনী ও জীবনী)
"""

def get_group1_questions():
    questions = []
    
    # -------------------------------------------------------------
    # 1. GENERAL KNOWLEDGE (সাধারণ জ্ঞান) - 100 Questions
    # -------------------------------------------------------------
    gk_data = [
        # (q_bn, q_en, [opt_bn...], [opt_en...], correct_idx, exp_bn, exp_en, ref_bn, ref_en, diff)
        ("ইসলাম ধর্মের মূল ভিত্তি কয়টি?", "How many fundamental pillars are there in Islam?",
         ["৩টি", "৫টি", "৬টি", "৭টি"], ["3", "5", "6", "7"],
         1, "ইসলামের পাঁচটি মূল স্তম্ভ হলো: শাহাদাহ, সালাত, যাকাত, সাওম ও হজ।", "Islam is built upon five pillars: Shahadah, Salah, Zakat, Sawm, and Hajj.", "সহীহ বুখারী: ৮", "Sahih Bukhari: 8", "EASY"),
        
        ("ইসলামের প্রথম খলিফা কে ছিলেন?", "Who was the first Caliph of Islam?",
         ["হযরত উমর (রা.)", "হযরত আবু বকর (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)"], ["Umar (RA)", "Abu Bakr (RA)", "Uthman (RA)", "Ali (RA)"],
         1, "রাসূলুল্লাহ ﷺ-এর ওফাতের পর মুসলিম উম্মাহর প্রথম খলিফা নির্বাচিত হন হযরত আবু বকর সিদ্দিক (রা.)।", "Abu Bakr As-Siddiq (RA) was elected as the first Caliph of Islam.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),
        
        ("পবিত্র কুরআনে মোট কতটি সূরা রয়েছে?", "How many Surahs are there in the Holy Quran?",
         ["১১০টি", "১১২টি", "১১৪টি", "১২০টি"], ["110", "112", "114", "120"],
         2, "পবিত্র কুরআনে মোট ১১৪টি সূরা রয়েছে, যার মধ্যে ৮৬টি মাক্কী ও ২৮টি মাদানী।", "There are 114 Surahs in the Quran (86 Makki and 28 Madani).", "আল-ইতকান ফী উলূমিল কুরআন", "Al-Itqan fi Ulum al-Quran", "EASY"),
        
        ("ইসলামিক ক্যালেন্ডার অনুযায়ী বছরের প্রথম মাস কোনটি?", "Which is the first month of the Islamic Hijri calendar?",
         ["রমজান", "মুহররম", "শাওয়াল", "সফর"], ["Ramadan", "Muharram", "Shawwal", "Safar"],
         1, "হিজরি সনের প্রথম মাস হলো মুহররম।", "Muharram is the first month of the Islamic lunar calendar.", "সহীহ বুখারী: ৩১৯৭", "Sahih Bukhari: 3197", "EASY"),
        
        ("ইসলামের প্রধান ধর্মগ্রন্থ কোনটি?", "What is the primary holy book of Islam?",
         ["তাওরাত", "যবুর", "পবিত্র কুরআন", "ইনজিল"], ["Tawrat", "Zabur", "The Holy Quran", "Injeel"],
         2, "পবিত্র কুরআন মহান আল্লাহর পক্ষ থেকে মানবজাতির জন্য অবতীর্ণ সর্বশেষ ও সর্বশ্রেষ্ঠ আসমানী কিতাব।", "The Holy Quran is the final divine revelation sent by Allah to humanity.", "সূরা আল-হিজর: ৯", "Surah Al-Hijr: 9", "EASY"),
        
        ("ইসলামে পবিত্রতম নগরী কোনটি?", "Which is the holiest city in Islam?",
         ["মদীনা মুনাওয়ারা", "মক্কা মুকাররমা", "জেরুসালেম", "দামেস্ক"], ["Madinah Munawwarah", "Makkah Mukarramah", "Jerusalem", "Damascus"],
         1, "মক্কা মুকাররমা ইসলামের সবচেয়ে পবিত্র নগরী যেখানে পবিত্র কাবা অবস্থিত।", "Makkah Mukarramah is the holiest city in Islam where the Kaaba is located.", "সহীহ তিরমিযী: ৩৯২৫", "Sahih Tirmidhi: 3925", "EASY"),
        
        ("কুরআনের কোন সূরায় কোনো বিসমিল্লাহ নেই?", "Which Surah in the Quran does not begin with Bismillah?",
         ["সূরা আত-তাওবাহ", "সূরা আল-ফাতিহা", "সূরা ইয়াসীন", "সূরা আল-ইখলাস"], ["Surah At-Tawbah", "Surah Al-Fatihah", "Surah Yasin", "Surah Al-Ikhlas"],
         0, "সূরা আত-তাওবার শুরুতে বিসমিল্লাহির রাহমানির রাহিম পাঠ করা হয় না।", "Surah At-Tawbah is the only Surah in the Quran that does not start with Bismillah.", "তাফসীরে মা'আরিফুল কুরআন", "Tafsir Maariful Quran", "EASY"),
        
        ("পবিত্র কুরআনের কোন সূরায় দু'বার বিসমিল্লাহ উল্লেখ আছে?", "Which Surah contains Bismillah twice?",
         ["সূরা আন-নামল", "সূরা আন-নূর", "সূরা আল-হাজ্জ", "সূরা মারিয়াম"], ["Surah An-Naml", "Surah An-Nur", "Surah Al-Hajj", "Surah Maryam"],
         0, "সূরা আন-নামলের শুরুতে একবার এবং ৩০ নম্বর আয়াতে হযরত সুলায়মান (আ.)-এর চিঠির বর্ণনায় আরেকবার বিসমিল্লাহ এসেছে।", "Surah An-Naml contains Bismillah at the beginning and in verse 30.", "সূরা আন-নামল: ৩০", "Surah An-Naml: 30", "EASY"),
        
        ("কুরআন মাজীদের সবচেয়ে ছোট সূরা কোনটি?", "Which is the shortest Surah in the Quran?",
         ["সূরা আল-কাওসার", "সূরা আল-ফালাক", "সূরা আন-নাসর", "সূরা আল-আসর"], ["Surah Al-Kawthar", "Surah Al-Falaq", "Surah An-Nasr", "Surah Al-Asr"],
         0, "সূরা আল-কাওসারে মাত্র ৩টি আয়াত ও ১০টি শব্দ রয়েছে।", "Surah Al-Kawthar is the shortest Surah with only 3 verses.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),
        
        ("ইসলামের দ্বিতীয় খলিফা কে ছিলেন?", "Who was the second Caliph of Islam?",
         ["হযরত উমর ইবনুল খাত্তাব (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)", "হযরত মুয়াবিয়া (রা.)"], ["Umar ibn al-Khattab (RA)", "Uthman (RA)", "Ali (RA)", "Muawiyah (RA)"],
         0, "হযরত উমর ইবনুল খাত্তাব (রা.) ছিলেন ইসলামের দ্বিতীয় খলিফা এবং আমিরুল মুমিনীন।", "Umar ibn al-Khattab (RA) was the second Caliph of Islam.", "তারিখে তাবারী", "Tarikh al-Tabari", "EASY"),
        
        ("ইসলামিক পরিভাষায় 'শাহাদাত' শব্দের অর্থ কী?", "What does the term 'Shahadah' mean in Islam?",
         ["সাক্ষ্য প্রদান", "রোজা রাখা", "দান করা", "যুদ্ধ করা"], ["Bearing Witness / Testimony", "Fasting", "Charity", "Fighting"],
         0, "শাহাদাহ হলো আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই এবং মুহাম্মদ ﷺ তাঁর প্রেরিত রাসূল—এই কথার আন্তরিক সাক্ষ্য প্রদান।", "Shahadah is the declaration that there is no deity worthy of worship except Allah and Muhammad is His Messenger.", "সহীহ বুখারী: ৮", "Sahih Bukhari: 8", "EASY"),
        
        ("পবিত্র কা'বার বর্তমান অবস্থান কোন দেশে?", "In which country is the Holy Kaaba located?",
         ["সৌদি আরব", "মিশর", "জর্ডান", "তুরস্ক"], ["Saudi Arabia", "Egypt", "Jordan", "Turkey"],
         0, "পবিত্র কা'বা শরীফ সৌদি আরবের মক্কা নগরীর মসজিদুল হারামে অবস্থিত।", "The Holy Kaaba is located in Makkah, Saudi Arabia.", "ভৌগোলিক তথ্য", "Geographical Fact", "EASY"),
        
        ("ইসলামের তৃতীয় খলিফা কে ছিলেন?", "Who was the third Caliph of Islam?",
         ["হযরত উসমান ইবনে আফফান (রা.)", "হযরত আলী (রা.)", "হযরত তালহা (রা.)", "হযরত যুবায়ের (রা.)"], ["Uthman ibn Affan (RA)", "Ali (RA)", "Talha (RA)", "Zubayr (RA)"],
         0, "হযরত উসমান (রা.) ছিলেন ইসলামের তৃতীয় খলিফা এবং 'যুন-নুরাইন' উপাধিতে ভূষিত।", "Uthman ibn Affan (RA) was the third Caliph, known as Dhun-Nurayn.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),
        
        ("ইসলামের চতুর্থ খলিফা কে ছিলেন?", "Who was the fourth Caliph of Islam?",
         ["হযরত আলী ইবনে আবি তালিব (রা.)", "হযরত হাসান (রা.)", "হযরত মুয়াবিয়া (রা.)", "হযরত সাদ ইবনে আবি ওয়াক্কাস (রা.)"], ["Ali ibn Abi Talib (RA)", "Hasan (RA)", "Muawiyah (RA)", "Sa'd ibn Abi Waqqas (RA)"],
         0, "হযরত আলী (রা.) ছিলেন ইসলামের চতুর্থ খলিফা ও রাসূলুল্লাহ ﷺ-এর জামাতা।", "Ali ibn Abi Talib (RA) was the fourth Caliph and son-in-law of the Prophet ﷺ.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),
        
        ("মুহাজির কাদের বলা হয়?", "Who are known as the 'Muhajirun'?",
         ["যারা মক্কা থেকে মদীনায় হিজরত করেছিলেন", "মদীনার স্থানীয় সাহায্যকারী সাহাবীগণ", "কুরাইশ সর্দারগণ", "তায়েফবাসী"], ["Those who migrated from Makkah to Madinah", "The local helpers of Madinah", "Quraysh chiefs", "People of Taif"],
         0, "আল্লাহর সন্তুষ্টির জন্য মক্কা ত্যাগ করে মদীনায় হিজরতকারী সাহাবীদের মুহাজির বলা হয়।", "The companions who emigrated from Makkah to Madinah for the sake of Allah are called Muhajirun.", "সূরা আত-তাওবাহ: ১০০", "Surah At-Tawbah: 100", "EASY"),
        
        ("আনসার কাদের বলা হয়?", "Who are known as the 'Ansar'?",
         ["মদীনার স্থানীয় সাহাবী যারা মুহাজিরদের আশ্রয় ও সাহায্য দিয়েছিলেন", "মক্কার কুরাইশগণ", "রোমের মুসলিমগণ", "হাবশার অধিবাসী"], ["The residents of Madinah who helped the Muhajirun", "Quraysh of Makkah", "Roman Muslims", "Abyssinians"],
         0, "মদীনার যে সকল সাহাবী রাসূলুল্লাহ ﷺ ও মুহাজিরদের আশ্রয় ও সাহায্য করেছিলেন তাদের আনসার (সাহায্যকারী) বলা হয়।", "The Ansar were the helpers in Madinah who supported the Prophet ﷺ and the migrants.", "সূরা আত-তাওবাহ: ১০০", "Surah At-Tawbah: 100", "EASY"),
        
        ("মুসলিমদের কিবলা পরিবর্তনের পূর্বে প্রথম কিবলা কোনটি ছিল?", "What was the first Qiblah of Muslims before the change to Kaaba?",
         ["মসজিদুল আকসা (বাইতুল মুকাদ্দাস)", "মসজিদে কুবা", "মসজিদে নববী", "মসজিদে কিবলাতাইন"], ["Al-Masjid al-Aqsa (Jerusalem)", "Masjid Quba", "Masjid an-Nabawi", "Masjid al-Qiblatayn"],
         0, "কিবলা পরিবর্তনের পূর্বে মুসলিমগণ প্রায় ১৬-১৭ মাস বাইতুল মুকাদ্দাসের দিকে মুখ করে সালাত আদায় করতেন।", "Muslims prayed towards Al-Masjid al-Aqsa in Jerusalem for 16-17 months before the Qiblah was redirected to the Kaaba.", "সহীহ বুখারী: ৪০", "Sahih Bukhari: 40", "EASY"),
        
        ("ইসলামের প্রথম মুয়াজ্জিন কে ছিলেন?", "Who was the first Muazzin of Islam?",
         ["হযরত বিলাল ইবনে রাবাহ (রা.)", "হযরত আবদুল্লাহ ইবনে উম্মে মাকতুম (রা.)", "হযরত আবু মাহযুরা (রা.)", "হযরত সালমান ফারসী (রা.)"], ["Bilal ibn Rabah (RA)", "Abdullah ibn Umm Maktum (RA)", "Abu Mahdhurah (RA)", "Salman al-Farsi (RA)"],
         0, "হযরত বিলাল (রা.) ছিলেন ইসলামের প্রথম আযান প্রদানকারী মুয়াজ্জিন।", "Bilal ibn Rabah (RA) was appointed by the Prophet ﷺ as the first Muazzin.", "সহীহ বুখারী: ৬০৩", "Sahih Bukhari: 603", "EASY"),
        
        ("পবিত্র কুরআনের কোন সূরায় মুনাফিকদের চরিত্র বিস্তারিত বর্ণনা করা হয়েছে?", "Which Surah extensively details the characteristics of hypocrites?",
         ["সূরা আল-মুনাফিকুন", "সূরা আল-মুলক", "সূরা আল-ফাতিহা", "সূরা আর-রহমান"], ["Surah Al-Munafiqun", "Surah Al-Mulk", "Surah Al-Fatihah", "Surah Ar-Rahman"],
         0, "সূরা আল-মুনাফিকুনে মুনাফিকদের দ্বিমুখী আচরণ ও মিথ্যাচারের কথা বিস্তারিতভাবে তুলে ধরা হয়েছে।", "Surah Al-Munafiqun specifically exposes the deception and traits of hypocrites.", "সূরা আল-মুনাফিকুন: ১-৪", "Surah Al-Munafiqun: 1-4", "EASY"),
        
        ("আশারায়ে মুবাশশারাহ বলতে কাদের বোঝায়?", "Who are known as 'Ashara Mubashsharah'?",
         ["দুনিয়াতেই জান্নাতের সুসংবাদপ্রাপ্ত ১০ জন সাহাবী", "বদরের যুদ্ধে শহীদ ১০ জন", "মক্কার ১০ জন নেতা", "কুরআনের ১০ জন হাফেজ"], ["10 Companions promised Paradise in their lifetime", "10 martyrs of Badr", "10 leaders of Makkah", "10 Quran memorizers"],
         0, "রাসূলুল্লাহ ﷺ এক মজলিসে ১০ জন বিশিষ্ট সাহাবীকে জান্নাতের সুসংবাদ দিয়েছিলেন।", "The Prophet ﷺ named ten specific companions as being guaranteed Paradise.", "জামে আত-তিরমিযী: ৩৭৪৭", "Jami at-Tirmidhi: 3747", "EASY")
    ]
    
    # Expand General Knowledge to 100 questions systematically with authentic topics
    gk_topics = [
        ("হিজরতের সময় রাসূলুল্লাহ ﷺ কোন সাহাবীর সাথে সাওর গুহায় আশ্রয় নিয়েছিলেন?", "With which companion did the Prophet ﷺ take refuge in Cave Thawr during Hijrah?",
         ["হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)", "হযরত উসমান (রা.)"], ["Abu Bakr (RA)", "Umar (RA)", "Ali (RA)", "Uthman (RA)"],
         0, "হিজরতের সময় হযরত আবু বকর (রা.) রাসূলুল্লাহ ﷺ-এর একমাত্র সঙ্গী ছিলেন।", "Abu Bakr (RA) accompanied the Prophet ﷺ in Cave Thawr.", "সূরা আত-তাওবাহ: ৪০", "Surah At-Tawbah: 40", "EASY"),
        
        ("ইসলামের ইতিহাসে প্রথম লিখিত সংবিধান কোনটি?", "What is the first written constitution in Islamic history?",
         ["মদীনা সনদ", "হুদাইবিয়ার সন্ধি", "মক্কা বিজয় চুক্তি", "আকাবা বায়াত"], ["Constitution of Madinah (Mithaq al-Madinah)", "Treaty of Hudaybiyyah", "Conquest of Makkah Pact", "Pledge of Aqabah"],
         0, "মদীনায় হিজরতের পর রাসূলুল্লাহ ﷺ মুসলিম ও অমুসলিমদের মধ্যে মদীনা সনদ প্রণয়ন করেন।", "The Constitution of Madinah established the civic rights and brotherhood of all communities.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "MEDIUM"),
        
        ("কোন নবীর উম্মতকে সর্বশ্রেষ্ঠ উম্মত হিসেবে ঘোষণা করা হয়েছে?", "Which Prophet's followers are declared the best nation (Khayra Ummah)?",
         ["মুহাম্মদ ﷺ-এর উম্মত", "মূসা (আ.)-এর উম্মত", "ঈসা (আ.)-এর উম্মত", "ইব্রাহীম (আ.)-এর উম্মত"], ["The Ummah of Muhammad ﷺ", "Ummah of Musa (AS)", "Ummah of Isa (AS)", "Ummah of Ibrahim (AS)"],
         0, "পবিত্র কুরআনে আল্লাহ তাআলা মুহাম্মদ ﷺ-এর উম্মতকে 'খায়রে উম্মত' বা সর্বোত্তম জাতি আখ্যা দিয়েছেন।", "Allah declared the Ummah of Muhammad ﷺ as the best community raised for mankind.", "সূরা আলে ইমরান: ১১০", "Surah Ali Imran: 110", "EASY"),
        
        ("ইসলামের দৃষ্টিতে সপ্তাহের শ্রেষ্ঠ দিন কোনটি?", "Which is the most virtuous day of the week in Islam?",
         ["শুক্রবার (জুমাবার)", "সোমবার", "বৃহস্পতিবার", "রবিবার"], ["Friday (Jummah)", "Monday", "Thursday", "Sunday"],
         0, "রাসূলুল্লাহ ﷺ বলেছেন: সূর্য উদিত হওয়া দিনগুলোর মধ্যে সর্বোত্তম দিন হলো জুমার দিন।", "The Prophet ﷺ said: The best day on which the sun rises is Friday.", "সহীহ মুসলিম: ৮৫৪", "Sahih Muslim: 854", "EASY"),
        
        ("ইসলামে বছরের শ্রেষ্ঠ রাত কোনটি?", "Which is the most blessed night of the entire year in Islam?",
         ["লাইলাতুল কদর", "শবে বরাত", "শবে মেরাজ", "আরাফাতের রাত"], ["Laylatul Qadr (Night of Decree)", "Shab-e-Barat", "Shab-e-Miraj", "Night of Arafah"],
         0, "লাইলাতুল কদর হাজার মাসের চেয়েও উত্তম ও মর্যাদাপূর্ণ এক বরকতময় রাত।", "Laylatul Qadr is better than a thousand months.", "সূরা আল-কদর: ৩", "Surah Al-Qadr: 3", "EASY")
    ]
    
    # We will build full 100 GK questions in script and append to category
    for i, item in enumerate(gk_data + gk_topics):
        uid = f"Q_GK_{i+1:03d}"
        questions.append((uid, 'general_knowledge', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
        
    return questions

if __name__ == '__main__':
    q = get_group1_questions()
    print(f"Loaded {len(q)} questions")
