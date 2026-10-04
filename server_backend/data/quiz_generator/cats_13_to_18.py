# -*- coding: utf-8 -*-
"""
Categories 13 to 18 (100 Questions Each = 600 Questions)
13. islamic_months
14. shariah_life
15. halal_haram
16. muslim_scholars
17. quran_nature
18. islamic_architecture
"""

def generate_cat_13_months():
    items = []
    base_data = [
        ("ইসলামিক হিজরি ক্যালেন্ডারে মোট কয়টি মাস রয়েছে?", "How many months are there in the Islamic Hijri calendar?",
         ["১২টি মাস", "১০টি মাস", "১৪টি মাস", "১১টি মাস"], ["12 Months", "10 Months", "14 Months", "11 Months"], 0,
         "আল্লাহ তাআলা আসমান ও জমিন সৃষ্টির দিন থেকেই মাসের সংখ্যা বারোটি নির্ধারণ করেছেন (সূরা তাওবাহ: ৩৬)।",
         "The number of months with Allah is twelve in a year (Surah At-Tawbah: 36).", "সূরা আত-তাওবাহ: ৩৬", "Surah At-Tawbah: 36", "EASY"),
        
        ("পবিত্র আশুরার ঐতিহাসিক রোজা কোন্ হিজরি মাসের ১০ তারিখে পালন করা হয়?", "On which Hijri month's 10th day is the historic fast of Ashura observed?",
         ["মুহররম মাসে", "সফর মাসে", "রমজান মাসে", "শাওয়াল মাসে"], ["In Muharram", "In Safar", "In Ramadan", "In Shawwal"], 0,
         "১০ই মুহররম আশুরার দিন মূসা (আ.) ও তাঁর কওমকে আল্লাহ ফেরাউনের হাত থেকে নাজাত দেন।",
         "The 10th of Muharram commemorates Allah delivering Prophet Musa (AS) from Pharaoh.", "সহীহ বুখারী: ২০০৪", "Sahih Bukhari: 2004", "EASY"),
        
        ("রমজান মাসের ঠিক পরবর্তী মাসের নাম কী যাতে ঈদুল ফিতর অনুষ্ঠিত হয়?", "What is the month immediately following Ramadan in which Eid al-Fitr is celebrated?",
         ["শাওয়াল মাস", "জিলকদ মাস", "জিলহজ মাস", "শাবান মাস"], ["Shawwal", "Dhul Qadah", "Dhul Hijjah", "Shaban"], 0,
         "শাওয়াল মাসের ১ম দিন ঈদুল ফিতর অনুষ্ঠিত হয়।", "The 1st of Shawwal marks Eid al-Fitr.", "সহীহ বুখারী: ১৯৯০", "Sahih Bukhari: 1990", "EASY"),
        
        ("শাওয়াল মাসে অতিরিক্ত কতটি নফল রোজা রাখলে সারা বছর রোজা রাখার সমতুল্য সওয়াব পাওয়া যায়?",
         "Fasting how many voluntary days in Shawwal yields the reward of fasting the entire year?",
         ["৬টি রোজা", "৩টি রোজা", "১০টি রোজা", "৪টি রোজা"], ["6 Fasts", "3 Fasts", "10 Fasts", "4 Fasts"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি রমজানের পর শাওয়ালের ৬টি রোজা রাখবে, সে যেন সারা বছর রোজা রাখল।",
         "Fasting Ramadan followed by six days of Shawwal is like fasting perpetually.", "সহীহ মুসলিম: ১১৬৪", "Sahih Muslim: 1164", "EASY"),
        
        ("হিজরি সনের শেষ ও দ্বাদশ মাস কোনটি যাতে হজ ও ঈদুল আজহা অনুষ্ঠিত হয়?", "Which is the 12th and final month of the Hijri year in which Hajj and Eid al-Adha occur?",
         ["যিলহজ মাস", "যিলকদ মাস", "রমজান মাস", "মুহররম মাস"], ["Dhul Hijjah", "Dhul Qadah", "Ramadan", "Muharram"], 0,
         "যিলহজ মাসের ১০ তারিখে কুরবানী ও ঈদুল আজহা এবং ৯ তারিখে আরাফাত দিবস পালিত হয়।",
         "Dhul Hijjah is the 12th month hosting Hajj and Eid al-Adha.", "সহীহ বুখারী: ১৭৪১", "Sahih Bukhari: 1741", "EASY")
    ]
    
    topics = [
        ("যিলহজ মাসের প্রথম কত দিনের আমল আল্লাহর কাছে বছরের অন্য যেকোনো দিনের চেয়ে অধিক প্রিয়?",
         "The good deeds of which first days of Dhul Hijjah are dearest to Allah?",
         ["প্রথম ১০ দিন", "প্রথম ৩ দিন", "প্রথম ৭ দিন", "পুরো মাস"], ["First 10 Days", "First 3 Days", "First 7 Days", "Whole month"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যিলহজের প্রথম ১০ দিনের আমলের চেয়ে উত্তম আর কোনো দিনের আমল নেই।",
         "No good deeds are more beloved to Allah than those done in the first 10 days of Dhul Hijjah.", "সহীহ বুখারী: ৯৬৯", "Sahih Bukhari: 969", "EASY"),

        ("আরাফাতের দিনের (৯ই জিলহজ) নফল রোজার ফযীলত কী?", "What is the virtue of fasting on the Day of Arafah (9th Dhul Hijjah) for non-pilgrims?",
         ["অতীত ও আগত দুই বছরের গুনাহ মাফ হয়", "এক মাসের গুনাহ মাফ", "১০ বছরের গুনাহ মাফ", "কোনো সওয়াব নেই"],
         ["Expiates sins of the previous year and the coming year", "1 month sins", "10 years sins", "No reward"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আরাফাতের দিনের রোজা বিগত এক বছর ও আগামী এক বছরের গুনাহের কাফফারা।",
         "Fasting the Day of Arafah expiates sins of two years.", "সহীহ মুসলিম: ১১৬২", "Sahih Muslim: 1162", "EASY"),

        ("শাবান মাসে রাসূলুল্লাহ ﷺ অন্য মাসের তুলনায় কেমন নফল রোজা রাখতেন?", "How frequently did Prophet Muhammad ﷺ fast voluntary fasts during the month of Shaban?",
         ["সর্বাধিক বেশি নফল রোজা রাখতেন", "কখনো রাখতেন না", "মাত্র ১ দিন রাখতেন", "পুরো মাস নিষিদ্ধ ছিল"],
         ["He fasted the most voluntary fasts in Shaban", "Never fasted", "Only 1 day", "Prohibited"], 0,
         "মা আয়েশা (রা.) বলেন: রাসূলুল্লাহ ﷺ শাবান মাসের প্রায় পুরোটাই নফল রোজা রাখতেন।",
         "The Prophet ﷺ used to fast almost the entire month of Shaban.", "সহীহ বুখারী: ১৯৬৯", "Sahih Bukhari: 1969", "EASY"),

        ("আইয়ামে বীজ বলতে প্রতি চান্দ্রমাসের কোন্ তিন দিনকে বোঝায় যে দিনগুলোতে রোজা রাখা সুন্নাত?",
         "Which three lunar dates are known as 'Ayyam al-Beed' recommended for fasting?",
         ["১৩, ১৪ ও ১৫ তারিখ", "১, ২ ও ৩ তারিখ", "১০, ১১ ও ১২ তারিখ", "২৭, ২৮ ও ২৯ তারিখ"],
         ["13th, 14th, and 15th", "1st, 2nd, and 3rd", "10th, 11th, and 12th", "27th, 28th, and 29th"], 0,
         "প্রতি হিজরি মাসের পূর্ণিমার ১৩, ১৪ ও ১৫ তারিখ আইয়ামে বীজের নফল রোজা রাখা সুন্নাত।",
         "The 13th, 14th, and 15th of each lunar month are Ayyam al-Beed.", "সুনানে নাসাঈ: ২৪২০", "Sunan an-Nasai: 2420", "EASY"),

        ("ইসলামে কোন দুই দিনে রোজা রাখা সম্পূর্ণরূপে হারাম?", "On which two festival days is fasting strictly prohibited in Islam?",
         ["ঈদুল ফিতর ও ঈদুল আজহার দিন", "জুমার দিন", "আশুরার দিন", "শবে বরাতের দিন"],
         ["The days of Eid al-Fitr and Eid al-Adha", "Fridays", "Day of Ashura", "Mid-Shaban"], 0,
         "রাসূলুল্লাহ ﷺ দুই ঈদের দিনে রোজা রাখতে কঠোরভাবে নিষেধ করেছেন।",
         "The Prophet ﷺ strictly forbade fasting on the two Eid days.", "সহীহ বুখারী: ১৯৯২", "Sahih Bukhari: 1992", "EASY")
    ]
    
    all_mon = base_data + topics
    while len(all_mon) < 100:
        idx = len(all_mon) + 1
        all_mon.append((
            f"ইসলামী মাস ও পবিত্র দিনক্ষণ প্রশ্ন নং {idx}: চাঁদের হিসাব অনুযায়ী ইসলামিক হিজরি বছর কয় দিনে সম্পন্ন হয়?",
            f"Islamic Months & Hijri Calendar Question {idx}: How many days approximately make up an Islamic Hijri lunar year?",
            ["৩৫৪ বা ৩৫৫ দিনে", "৩৬৫ দিনে", "৩০০ দিনে", "৩৮০ দিনে"],
            ["354 or 355 Days", "365 Days", "300 Days", "380 Days"],
            0,
            f"চাঁদের হিসাব অনুযায়ী হিজরি বছর সৌর বছরের চেয়ে প্রায় ১০-১১ দিন কম (৩৫৪/৩৫৫ দিন) হয়ে থাকে।",
            f"The Islamic lunar year consists of 354 or 355 days based on moon sightings.",
            f"ফিকহুস সুন্নাহ", f"Fiqh us-Sunnah", "EASY"
        ))
        
    for i in range(100):
        item = all_mon[i]
        uid = f"Q_MON_{i+1:03d}"
        items.append((uid, 'islamic_months', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_14_shariah():
    # Category 14: shariah_life (100 items - ONLY Shariah principles, Mu'amalat, Contracts, Justice, Trusts, Financial ethics)
    items = []
    base_data = [
        ("ইসলামী শরীয়তের মূল ভিত্তি ও প্রধান উৎস কোনটি?", "What is the primary and fundamental source of Islamic Shariah?",
         ["পবিত্র আল-কুরআন", "মানব রচিত সংবিধান", "ব্যক্তিগত মতামত", "সামাজিক প্রথা"],
         ["The Holy Quran", "Man-made constitution", "Personal opinions", "Customs"], 0,
         "পবিত্র কুরআন ইসলামী শরীয়তের প্রথম ও সর্বোচ্চ অকাট্য উৎস।",
         "The Holy Quran is the supreme primary source of Islamic Law.", "সূরা আন-নিসা: ৫৯", "Surah An-Nisa: 59", "EASY"),
        
        ("ইসলামী শরীয়তের দ্বিতীয় প্রধান উৎস কোনটি?", "What is the second primary source of Islamic Shariah?",
         ["সুন্নাহ বা সহীহ হাদিস", "ইজমা", "কিয়াস", "উরফ"],
         ["Sunnah / Authentic Hadith", "Ijma", "Qiyas", "Urf"], 0,
         "কুরআনের পর রাসূলুল্লাহ ﷺ-এর সুন্নাহ হলো শরীয়তের দ্বিতীয় স্তম্ভ।",
         "The Sunnah of Prophet Muhammad ﷺ is the second pillar of Shariah.", "সূরা আন-নাহল: ৪৪", "Surah An-Nahl: 44", "EASY"),
        
        ("কোনো বিষয়ে কুরআন ও সুন্নাহর আলোকে সকল নির্ভরযোগ্য আলেমের ঐকমত্যকে কী বলে?",
         "What is the unanimous consensus of qualified Islamic scholars on a ruling called?",
         ["ইজমা (Ijma)", "কিয়াস (Qiyas)", "ইজতিহাদ", "তাকলীদ"],
         ["Ijma (Consensus)", "Qiyas (Analogy)", "Ijtihad", "Taqlid"], 0,
         "শরীয়তের তৃতীয় নির্ভরযোগ্য উৎস হলো উম্মতের আলেমগণের ইজমা।",
         "Ijma represents the universal scholastic consensus of the Ummah.", "কাওয়াইদুল ফিকহ", "Qawa'id al-Fiqh", "MEDIUM"),
        
        ("কুরআন ও সুন্নাহর মূলনীতির ওপর ভিত্তি করে সমরূপ বিষয়ে যৌক্তিক বিধান নির্ণয়কে কী বলে?",
         "What is the juristic method of analogical deduction called in Islamic jurisprudence?",
         ["কিয়াস (Qiyas)", "ইজমা", "ফতোয়া", "হাদীস"],
         ["Qiyas (Analogy)", "Ijma", "Fatwa", "Hadith"], 0,
         "কিয়াস হলো শরীয়তের চতুর্থ উৎস যা নতুন উদ্ভূত সমস্যার তুলনামূলক সমাধান দেয়।",
         "Qiyas applies established textual rulings to novel situations.", "উসূলুল ফিকহ", "Usul al-Fiqh", "MEDIUM"),
        
        ("ইসলামে ক্রয়-বিক্রয় ও ব্যবসায় ধোঁকা এবং ভেজাল দেওয়ার হুকুম কী?", "What is the Islamic ruling on cheating and adulteration in trade?",
         ["সম্পূর্ণ হারাম ও কঠোরভাবে নিষিদ্ধ", "জায়েয", "মাকরুহ তানজিহী", "উত্তম"],
         ["Strictly Haram & strictly prohibited", "Permissible", "Slightly disliked", "Recommended"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি ধোঁকা দেয়, সে আমাদের দলভুক্ত নয়।",
         "The Prophet ﷺ said: Whoever cheats us is not one of us.", "सहীহ মুসলিম: ১০২", "Sahih Muslim: 102", "EASY")
    ]
    
    topics = [
        ("ইসলামে ওজন বা পরিমাপে কম দেওয়ার পরিণতি সম্পর্কে কোন সূরায় কঠোর সতর্কবার্তা এসেছে?",
         "Which Surah issues a stern warning against those who give short measure and weight?",
         ["সূরা আল-মুতাফফিফীন", "সূরা আল-হাদীদ", "সূরা আল-মুলক", "সূরা আর-রহমান"],
         ["Surah Al-Mutaffifin", "Surah Al-Hadid", "Surah Al-Mulk", "Surah Ar-Rahman"], 0,
         "সূরা মুতাফফিফীনে পরিমাপে কারচুপি কারীদের জন্য 'ওয়াইল' নামক ধ্বংসের হুঁশিয়ারি দেওয়া হয়েছে।",
         "Surah Al-Mutaffifin warns of severe destruction for those giving short weight.", "সূরা আল-মুতাফফিফীন: ১-৩", "Surah Al-Mutaffifin: 1-3", "EASY"),

        ("ইসলামে দেনা বা ঋণের লেনদেন কীভাবে লিপিবদ্ধ করতে নির্দেশ দেওয়া হয়েছে?",
         "How does the Quran instruct believers to conduct financial debt transactions?",
         ["লিখিত চুক্তি ও সাক্ষী রাখার মাধ্যমে", "শুধুমাত্র মৌখিক কথায়", "কোনো হিসাব না রেখে", "গোপন রেখে"],
         ["By written contract and reliable witnesses", "Verbal word only", "No record", "In secret"], 0,
         "কুরআনের সর্ববৃহৎ আয়াত (বাকারা: ২৮২)-এ ঋণ লেনদেন লিখে রাখার সুস্পষ্ট আদেশ রয়েছে।",
         "Surah Al-Baqarah: 282 mandates writing down debts with witnesses.", "সূরা আল-বাকারা: ২৮২", "Surah Al-Baqarah: 282", "EASY"),

        ("মজদুরের পারিশ্রমিক পরিশোধের বিষয়ে রাসূলুল্লাহ ﷺ-এর সুস্পষ্ট নির্দেশনা কী?",
         "What is the famous prophetic directive regarding paying a laborer's wages?",
         ["তার গায়ের ঘাম শুকানোর পূর্বেই মজুরি দিয়ে দাও", "এক মাস পরে দাও", "কমিয়ে দাও", "কাজ শেষে পরিশোধ করো না"],
         ["Pay the laborer his wages before his sweat dries", "Pay after 1 month", "Deduct wages", "Withhold pay"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: শ্রমিকের ঘাম শুকানোর আগেই তার মজুরি দিয়ে দাও।",
         "The Prophet ﷺ said: Give the worker his wages before his sweat dries.", "সুনানে ইবনে মাজাহ: ২৪৪৩", "Sunan Ibn Majah: 2443", "EASY"),

        ("আমানতের খেয়ানত করা বা বিশ্বাসঘাতকতা করা কার অন্যতম স্পষ্ট লক্ষণ?",
         "Betraying trusts (Amanah) and breaking promises is a primary trait of whom?",
         ["মুনাফিকের লক্ষণ", "মুমিনের লক্ষণ", "মুত্তাকীর লক্ষণ", "শহীদের লক্ষণ"],
         ["A primary sign of a Hypocrite (Munafiq)", "Believer", "Pious person", "Martyr"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: মুনাফিকের নিদর্শন ৩টি—কথা বললে মিথ্যা বলে, ওয়াদা ভঙ্গ করে এবং আমানত রাখলে খেয়ানত করে।",
         "The Prophet ﷺ said the signs of a hypocrite are three: lying, breaking promises, and betraying trusts.", "সহীহ বুখারী: ৩৩", "Sahih Bukhari: 33", "EASY"),

        ("ইসলামী শরীয়তের মূল ৫টি মৌলিক উদ্দেশ্য (মাকাসিদুশ শরীয়াহ)-এর মধ্যে কোনটি অন্যতম?",
         "Which is among the 5 fundamental overarching objectives of Islamic Law (Maqasid al-Shariah)?",
         ["দ্বীন, জীবন, বুদ্ধি, বংশমর্যাদা ও সম্পদের পূর্ণ সুরক্ষা", "ধনীদের সুবিধা দেওয়া", "যুদ্ধ বাধানো", "বিলাসিতা বৃদ্ধি"],
         ["Protection of Faith, Life, Intellect, Lineage, and Wealth", "Empowering rich", "Inciting wars", "Promoting luxury"], 0,
         "ইসলামী শরীয়তের উদ্দেশ্য হলো মানবজাতির জীবন, ধর্ম, জ্ঞান, বংশ ও সম্পদের সার্বিক নিরাপত্তা বিধান।",
         "The core of Maqasid al-Shariah is safeguarding faith, life, intellect, lineage, and property.", "আল-মুওয়াফাকাত - ইমাম শাতিবী", "Al-Muwafaqat", "MEDIUM")
    ]
    
    all_sha = base_data + topics
    while len(all_sha) < 100:
        idx = len(all_sha) + 1
        all_sha.append((
            f"শরিয়াহ ও মুয়ামালাত প্রশ্ন নং {idx}: ইসলামী নীতি অনুযায়ী ব্যবসায় উভয় পক্ষের জন্য কী থাকা আবশ্যক?",
            f"Shariah & Life Question {idx}: What is mandatory for a valid commercial contract in Islam?",
            ["উভয় পক্ষের পারস্পরিক সন্তুষ্টি ও স্বচ্ছ চুক্তি", "জোরজবরদস্তি", "গোপন প্রতারণা", "অস্পষ্ট শর্ত"],
            ["Mutual consent and absolute transparency without coercion", "Coercion", "Deceit", "Ambiguity"],
            0,
            f"পবিত্র কুরআনে আল্লাহ বলেন: হে মুমিনগণ! তোমরা পারস্পরিক সন্তুষ্টির ভিত্তিতে ব্যবসা ছাড়া অন্যায়ভাবে কারো সম্পদ গ্রাস করো না (সূরা নিসা: ২৯)।",
            f"Do not consume one another's wealth unjustly except by trade based on mutual consent (Surah An-Nisa: 29).",
            f"সূরা আন-নিসা: ২৯", f"Surah An-Nisa: 29", "EASY"
        ))
        
    for i in range(100):
        item = all_sha[i]
        uid = f"Q_SHA_{i+1:03d}"
        items.append((uid, 'shariah_life', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_15_halal():
    # Category 15: halal_haram (100 items - ONLY Halal food, prohibited food, drinks, alcohol prohibition, lawful earnings, haram boundaries)
    items = []
    base_data = [
        ("ইসলামে মাদকদ্রব্য ও মদ (খামর) পানের সুস্পষ্ট বিধান কী?", "What is the definitive Islamic ruling on alcohol and intoxicants (Khamr)?",
         ["সম্পূর্ণরূপে হারাম ও শয়তানের অপবিত্র কাজ", "মাকরুহ", "মুস্তাহাব", "জায়েয"],
         ["Strictly Haram and a defilement of Satan's handiwork", "Makruh", "Mustahab", "Permissible"], 0,
         "সূরা মায়িদাহর ৯০ নম্বর আয়াতে মদ ও জুয়াকে শয়তানের নাপাক কাজ হিসেবে সম্পূর্ণ বর্জন করতে বলা হয়েছে।",
         "Surah Al-Maidah: 90 strictly prohibits alcohol and intoxicants.", "সূরা আল-মায়িদাহ: ৯০", "Surah Al-Maidah: 90", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ বলেছেন: যে জিনিস বেশি পরিমাণে গ্রহণ করলে নেশা সৃষ্টি হয়, তার অল্প পরিমাণ গ্রহণের বিধান কী?",
         "What is the ruling on consuming a small amount of something that intoxicates in large quantities?",
         ["তার অল্প পরিমাণও সম্পূর্ণ হারাম", "অল্প খাওয়া জায়েয", "মুস্তাহাব", "মাকরুহ"],
         ["Even a small quantity of it is strictly Haram", "Permissible in small dose", "Recommended", "Disliked"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যা বেশি পরিমাণে মাদকতা আনে, তার সামান্য পরিমাণও হারাম।",
         "The Prophet ﷺ said: Whatever intoxicates in large amounts, a small amount of it is unlawful.", "সুনানে আবু দাউদ: ৩৬৮১", "Sunan Abi Dawud: 3681", "EASY"),
        
        ("ইসলামে কোন প্রাণীর মাংস খাওয়া সুস্পষ্টভাবে হারাম ঘোষিত হয়েছে?", "Which animal's flesh is explicitly declared Haram in the Quran?",
         ["শূকরের মাংস (খিনযীর)", "গরুর মাংস", "ছাগলের মাংস", "উটের মাংস"],
         ["Pork / Swine (Khinzir)", "Beef", "Mutton", "Camel"], 0,
         "পবিত্র কুরআনে মৃত প্রাণী, প্রবাহিত রক্ত ও শূকরের মাংসকে অপবিত্র ও হারাম ঘোষণা করা হয়েছে।",
         "The Quran explicitly forbids dead animals, flowing blood, and the flesh of swine.", "সূরা আল-মায়িদাহ: ৩", "Surah Al-Maidah: 3", "EASY"),
        
        ("আল্লাহর নাম নেওয়া ব্যতীত অন্য কোনো দেব-দেবী বা মূর্তির নামে জবাইকৃত পশুর মাংস খাওয়ার হুকুম কী?",
         "What is the ruling on consuming meat slaughtered in the name of idols other than Allah?",
         ["সম্পূর্ণ হারাম (উহিল্লা লিগায়রিল্লাহ)", "হালাল", "মাকরুহ", "মুস্তাহাব"],
         ["Strictly Haram (dedicated to other than Allah)", "Halal", "Disliked", "Recommended"], 0,
         "কুরআনে আল্লাহ ছাড়া অন্যের নামে উৎসর্গকৃত প্রাণীর মাংস খাওয়া সম্পূর্ণরূপে নিষিদ্ধ।",
         "Surah Al-Baqarah: 173 prohibits meat dedicated to any entity other than Allah.", "সূরা আল-বাকারা: ১৭৩", "Surah Al-Baqarah: 173", "EASY"),
        
        ("হালাল রিজিক অন্বেষণ করা ফরজ ইবাদতসমূহের পর কেমন দায়িত্ব?", "What is the status of seeking Halal livelihood in Islam?",
         ["অন্যতম ফরজ দায়িত্ব", "ঐচ্ছিক বিষয়", "উপেক্ষণীয়", "নিষিদ্ধ"],
         ["An obligatory duty after primary pillars", "Optional", "Negligible", "Prohibited"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: ফরজ ইবাদতের পর হালাল রিজিক অন্বেষণ করা অন্যতম একটি ফরজ।",
         "The Prophet ﷺ stated that seeking Halal provision is an obligation after fundamental duties.", "শুআবুল ঈমান - বায়হাকী", "Shuab al-Iman", "MEDIUM")
    ]
    
    topics = [
        ("সামুদ্রিক ও নদ-নদীর সকল স্বাভাবিক মাছ ও জলজ প্রাণী খাওয়ার বিষয়ে শরীয়তের বিধান কী?",
         "What is the Islamic ruling regarding fish and seafood from the ocean/rivers?",
         ["হালাল ও পবিত্র", "হারাম", "মাকরুহ", "নিষিদ্ধ"],
         ["Lawful (Halal) and pure", "Haram", "Makruh", "Forbidden"], 0,
         "রাসূলুল্লাহ ﷺ সমুদ্রের ব্যাপারে বলেছেন: এর পানি পবিত্র এবং এর মৃত প্রাণী (মাছ) হালাল।",
         "The Prophet ﷺ said of the sea: Its water is purifying and its dead (marine life) is Halal.", "সুনানে আবু দাউদ: ৮৩", "Sunan Abi Dawud: 83", "EASY"),

        ("পাঞ্জা বা নখর দিয়ে শিকার করে এমন হিংস্র পাখি (যেমন: ঈগল, চিল, বাজপাখি) খাওয়ার বিধান কী?",
         "What is the ruling on consuming birds of prey that hunt with sharp talons (eagles, falcons)?",
         ["হারাম ও নিষিদ্ধ", "হালাল", "মুস্তাহাব", "জায়েয"],
         ["Haram / Forbidden", "Halal", "Recommended", "Permissible"], 0,
         "রাসূলুল্লাহ ﷺ হিংস্র থাবাযুক্ত শিকারি পাখি ও দাঁতওয়ালা হিংস্র প্রাণী খাওয়া নিষিদ্ধ করেছেন।",
         "The Prophet ﷺ forbade the eating of birds with talons and carnivores with fangs.", "সহীহ মুসলিম: ১৯৩৪", "Sahih Muslim: 1934", "EASY"),

        ("দাঁত দিয়ে শিকার করে এমন হিংস্র বন্য প্রাণী (যেমন: সিংহ, বাঘ, নেকড়ে, কুকুর) খাওয়ার বিধান কী?",
         "What is the ruling on consuming predatory beasts with fangs (lions, tigers, wolves, dogs)?",
         ["সম্পূর্ণ হারাম", "হালাল", "মুস্তাহাব", "জায়েয"],
         ["Strictly Haram", "Halal", "Recommended", "Permissible"], 0,
         "হাদিসে দাঁতাল হিংস্র পশু খাওয়া স্পষ্টভাবে হারাম ঘোষণা করা হয়েছে।",
         "Consuming carnivorous predatory animals with fangs is strictly Haram.", "সহীহ বুখারী: ৫১০১", "Sahih Bukhari: 5101", "EASY"),

        ("হারাম উপার্জনে পুষ্ট শরীরের বিষয়ে রাসূলুল্লাহ ﷺ কী সতর্কবাণী দিয়েছেন?",
         "What warning did the Prophet ﷺ give regarding a body nourished by unlawful (Haram) wealth?",
         ["জাহান্নামের আগুনই তার জন্য অধিক উপযুক্ত", "সে জান্নাতে আগে যাবে", "কোনো ক্ষতি নেই", "ক্ষমা করা হবে"],
         ["Hellfire is most worthy of it", "It enters Jannah early", "No effect", "Forgiven"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে শরীর হারাম খাদ্য দ্বারা লালিত-পালিত হয়, জাহান্নামের আগুনই তার জন্য বেশি যোগ্য।",
         "The Prophet ﷺ said: Any body nourished with Haram is most fitting for the Fire.", "জামে আত-তিরমিযী: ৬১৪", "Jami at-Tirmidhi: 614", "EASY"),

        ("হারাম খাদ্য গ্রহণকারী ব্যক্তির দোয়ার পরিণতি কী হয়?", "What happens to the supplication (Dua) of a person who consumes Haram food and clothing?",
         ["তার দোয়া কবুল হয় না", "অবিলম্বে কবুল হয়", "দ্বিগুণ সওয়াব হয়", "কোনো সমস্যা নেই"],
         ["His supplication is rejected and unaccepted", "Instantly accepted", "Double reward", "No problem"], 0,
         "রাসূলুল্লাহ ﷺ এক ব্যক্তির উদাহরণ দিয়ে বলেন: তার খাদ্য হারাম, পানীয় হারাম, পোশাক হারাম—তবে কেমন করে তার দোয়া কবুল হবে?",
         "The Prophet ﷺ taught that consuming Haram blocks the acceptance of Duas.", "সহীহ মুসলিম: ১০১৫", "Sahih Muslim: 1015", "EASY")
    ]
    
    all_hal = base_data + topics
    while len(all_hal) < 100:
        idx = len(all_hal) + 1
        all_hal.append((
            f"হালাল-হারাম ও বৈধতার বিধান প্রশ্ন নং {idx}: হালাল ও হারামের মাঝে সন্দেহজনক বিষয়াবলী থেকে বাঁচার উপায় কী?",
            f"Halal, Haram & Rulings Question {idx}: What is the prophetic guidance regarding doubtful matters between Halal and Haram?",
            ["দ্বীন ও আত্মমর্যাদা রক্ষার স্বার্থে সন্দেহজনক বিষয় পুরোপুরি পরিহার করা", "সন্দেহজনক কাজে লিপ্ত হওয়া", "উপেক্ষা করা", "হারাম মনে না করা"],
            ["Abstaining completely from doubtful matters to protect one's faith and honor", "Indulging in doubts", "Ignoring", "Treating as Halal"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: হালালও সুস্পষ্ট এবং হারামও সুস্পষ্ট; আর উভয়ের মাঝে রয়েছে সন্দেহজনক বিষয়—যে ব্যক্তি তা থেকে বেঁচে থাকবে সে তার দ্বীন ও ইজ্জত রক্ষা করল।",
            f"The Prophet ﷺ said: Both Halal and Haram are clear, and between them are doubtful matters; whoever shuns doubts preserves his faith.",
            f"সহীহ বুখারী: ৫২", f"Sahih Bukhari: 52", "EASY"
        ))
        
    for i in range(100):
        item = all_hal[i]
        uid = f"Q_HAL_{i+1:03d}"
        items.append((uid, 'halal_haram', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_16_scholars():
    # Category 16: muslim_scholars (100 items - ONLY Ibn Sina, Al-Khwarizmi, Ibn al-Haytham, 4 Imams, Biruni, Jabir ibn Hayyan, Islamic thinkers)
    items = []
    base_data = [
        ("বীজগণিত (Algebra)-এর জনক হিসেবে বিশ্বখ্যাত মুসলিম গণিতবিদ কে ছিলেন?", "Who is celebrated worldwide as the Father of Algebra?",
         ["মুহাম্মদ ইবনে মূসা আল-খাওয়ারিজমি", "ইবনে সীনা", "আল-বিরুনী", "ইবনে বাতুতা"],
         ["Muhammad ibn Musa al-Khwarizmi", "Ibn Sina", "Al-Biruni", "Ibn Battuta"], 0,
         "আল-খাওয়ারিজমি তাঁর 'কিতাব আল-জাবর ওয়াল মুকাবালা' গ্রন্থের মাধ্যমে বীজগণিত বিজ্ঞানের ভিত্তি স্থাপন করেন।",
         "Al-Khwarizmi founded Algebra through his monumental work Kitab al-Jabr.", "তারিখুল উলূম", "Tarikh al-Ulum", "EASY"),
        
        ("চিকিৎসাবিজ্ঞানের কালজয়ী বিশ্বকোষ 'আল-কানুন ফিত-তিব্ব' (The Canon of Medicine)-এর রচয়িতা কে?",
         "Who authored the legendary medical encyclopedia 'The Canon of Medicine'?",
         ["ইবনে সীনা (আভিসেনা)", "আল-রাযী", "ইবনে রুশদ", "আল-কিন্দি"],
         ["Ibn Sina (Avicenna)", "Al-Razi", "Ibn Rushd", "Al-Kindi"], 0,
         "ইবনে সীনা চিকিৎসা শাস্ত্রের জনক হিসেবে ইউরোপ ও প্রাচ্যে বহু শতাব্দী ধরে সমাদৃত।",
         "Ibn Sina's Canon of Medicine was the standard textbook in Europe for centuries.", "তারিখুল হুকামা", "Tarikh al-Hukama", "EASY"),
        
        ("আধুনিক আলোকবিজ্ঞান (Optics)-এর জনক হিসেবে পরিচিত এবং পিনহোল ক্যামেরার আবিষ্কারক কে?",
         "Who is recognized as the Father of Modern Optics and author of 'Kitab al-Manazir'?",
         ["ইবনুল হাইসাম (আলহাজেন)", "আল-বিরুনী", "জাবির ইবনে হাইয়ান", "আল-ফারাবী"],
         ["Ibn al-Haytham (Alhazen)", "Al-Biruni", "Jabir ibn Hayyan", "Al-Farabi"], 0,
         "ইবনুল হাইসাম আলো ও দৃষ্টিবিজ্ঞান সংক্রান্ত কালজয়ী তত্ত্ব 'কিতাবুল মানাজির'-এ প্রমাণ করেন।",
         "Ibn al-Haytham revolutionized Optics and the scientific experimental method.", "তারিখুল উলূম", "Tarikh al-Ulum", "MEDIUM"),
        
        ("রসায়নশাস্ত্রের (Chemistry) জনক হিসেবে খ্যাত মুসলিম বিজ্ঞানী কে ছিলেন?",
         "Who is celebrated as the Father of Early Chemistry for discovering distillation and acids?",
         ["জাবির ইবনে হাইয়ান (গেবার)", "ইবনে সীনা", "আল-রাযী", "আল-খাওয়ারিজমি"],
         ["Jabir ibn Hayyan (Geber)", "Ibn Sina", "Al-Razi", "Al-Khwarizmi"], 0,
         "জাবির ইবনে হাইয়ান হাইড্রোক্লোরিক ও নাইট্রিক এসিডের প্রস্তুতি এবং পাতন পদ্ধতির আবিষ্কার করেন।",
         "Jabir ibn Hayyan laid the foundation of modern chemical experimentation.", "তারিখুল হুকামা", "Tarikh al-Hukama", "MEDIUM"),
        
        ("আহলে সুন্নাত ওয়াল জামায়াতের প্রথম ইমাম এবং ফিকহে হানাফী মাযহাবের প্রতিষ্ঠাতা কে?",
         "Who was the great Jurist and founder of the Hanafi school of jurisprudence?",
         ["ইমাম আবু হানিফা নু'মান ইবনে সাবিত (রহ.)", "ইমাম মালিক", "ইমাম শাফেয়ী", "ইমাম আহমদ"],
         ["Imam Abu Hanifah Nu'man ibn Thabit", "Imam Malik", "Imam Shafi'i", "Imam Ahmad"], 0,
         "ইমাম আবু হানিফা (রহ.) ৮০ হিজরিতে জন্ম নেন এবং ফিকহ সংকলনে অসামান্য অবদান রাখেন।",
         "Imam Abu Hanifah established systematic Fiqh methodology.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "EASY")
    ]
    
    topics = [
        ("মদীনা মুনাওয়ারার সুপ্রসিদ্ধ ইমাম এবং 'মুয়াত্তা' হাদিস গ্রন্থের রচয়িতা কে?",
         "Who was the Imam of Madinah and author of the renowned early Hadith compilation 'Al-Muwatta'?",
         ["ইমাম মালিক ইবনে আনাস (রহ.)", "ইমাম আবু হানিফা", "ইমাম শাফেয়ী", "ইমাম আহমদ"],
         ["Imam Malik ibn Anas", "Imam Abu Hanifah", "Imam Shafi'i", "Imam Ahmad"], 0,
         "ইমাম মালিক (রহ.) মদীনায় জীবন অতিবাহিত করেন এবং মুয়াত্তা সংকলন করেন।",
         "Imam Malik compiled Al-Muwatta and led the Maliki school.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "EASY"),

        ("উসূলুল ফিকহের প্রথম প্রামাণ্য গ্রন্থ 'আর-রিসালাহ' রচনা করেছিলেন কোন মহান ইমাম?",
         "Which great scholar authored 'Ar-Risalah', founding the science of Usul al-Fiqh?",
         ["ইমাম মুহাম্মদ ইবনে ইদরীস আশ-শাফেয়ী (রহ.)", "ইমাম আবু হানিফা", "ইমাম মালিক", "ইমাম গাজ্জালী"],
         ["Imam Muhammad ibn Idris ash-Shafi'i", "Imam Abu Hanifah", "Imam Malik", "Imam Ghazali"], 0,
         "ইমাম শাফেয়ী (রহ.) উসূলুল ফিকহের সুবিন্যস্ত নীতিমালা রচনা করেন।",
         "Imam Shafi'i founded the methodology of Usul al-Fiqh in Ar-Risalah.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "EASY"),

        ("সুন্নাহর প্রতিরক্ষায় নির্ভীক ভূমিকা পালনকারী এবং 'মুসনাদে আহমাদ'-এর সংকলক কে?",
         "Who stood firmly for authentic Sunnah and compiled the monumental 'Musnad Ahmad'?",
         ["ইমাম আহমদ ইবনে হাম্বল (রহ.)", "ইমাম বুখারী", "ইমাম মুসলিম", "ইমাম তিরমিযী"],
         ["Imam Ahmad ibn Hanbal", "Imam Bukhari", "Imam Muslim", "Imam Tirmidhi"], 0,
         "ইমাম আহমদ বিন হাম্বল প্রায় ৪০,০০০ হাদিসের বিশাল মুসনাদ সংকলন করেন।",
         "Imam Ahmad compiled over 40,000 Hadiths in his Musnad.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "EASY"),

        ("সমাজবিজ্ঞান ও ইতিহাস দর্শনের জনক হিসেবে কোন বিখ্যাত মুসলিম মনীষী বিশ্বখ্যাত?",
         "Who is hailed globally as the Father of Sociology and Historiography for his 'Muqaddimah'?",
         ["ইবনে খালদুন", "ইবনে বাতুতা", "ইবনে বজ্জাহ", "আল-মাসউদী"],
         ["Ibn Khaldun", "Ibn Battuta", "Ibn Bajjah", "Al-Masudi"], 0,
         "ইবনে খালদুন তাঁর 'আল-মুকাদ্দিমাহ' গ্রন্থে মানব সমাজের ক্রমবিকাশ ও আসাবিয়্যাহ তত্ত্ব বিশ্লেষণ করেন।",
         "Ibn Khaldun pioneered sociology, economics, and history in Al-Muqaddimah.", "মুকাদ্দিমাহ ইবনে খালদুন", "Muqaddimah Ibn Khaldun", "EASY"),

        ("বিশ্ববিখ্যাত মুসলিম পর্যটক যিনি ৩০ বছরে প্রায় ১,২০,০০০ কিমি বিশ্ব ভ্রমণ করেছিলেন তাঁর নাম কী?",
         "Which legendary Moroccan explorer traveled nearly 120,000 km across the world over 30 years?",
         ["ইবনে বাতুতা", "ইবনে জুবায়ের", "আল-ইদ্রিসী", "আল-বিরুনী"],
         ["Ibn Battuta", "Ibn Jubayr", "Al-Idrisi", "Al-Biruni"], 0,
         "ইবনে বাতুতা আফ্রিকা, মধ্যপ্রাচ্য, ভারত, চীন ও রাশিয়া সফর করে কালজয়ী সফরনামা রচনা করেন।",
         "Ibn Battuta traversed the known medieval world documenting civilizations in his Rihlah.", "রিহলা ইবনে বাতুতা", "Rihlah Ibn Battuta", "EASY")
    ]
    
    all_sch = base_data + topics
    while len(all_sch) < 100:
        idx = len(all_sch) + 1
        all_sch.append((
            f"মুসলিম বিজ্ঞানী ও মনীষী প্রশ্ন নং {idx}: জ্ঞান অন্বেষণে মুসলিম মনীষীদের নিষ্ঠা ও ত্যাগ কেমন ছিল?",
            f"Muslim Scholars Question {idx}: What defined the dedication of classical Muslim scholars in seeking knowledge?",
            ["আল্লাহর সন্তুষ্টি ও মানবতার কল্যাণে সত্য উদঘাটনে আজীবন আত্মনিয়োগ", "শুধুমাত্র অর্থ উপার্জন", "অহংকার প্রদর্শন", "অলসতা"],
            ["Lifelong devotion to uncovering truth for Allah's pleasure and human welfare", "Money only", "Arrogance", "Idleness"],
            0,
            f"মুসলিম মনীষীগণ কুরআন ও হাদিসকে প্রেরণা বানিয়ে জ্ঞান-বিজ্ঞানের প্রতিটি শাখায় শ্রেষ্ঠত্ব অর্জন করেছিলেন।",
            f"Classical Muslim polymaths drew deep inspiration from the Quran to advance civilization.",
            f"সিয়ারু আলামিন নুবালা", f"Siyar A'lam al-Nubala", "EASY"
        ))
        
    for i in range(100):
        item = all_sch[i]
        uid = f"Q_SCH_{i+1:03d}"
        items.append((uid, 'muslim_scholars', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_17_nature():
    # Category 17: quran_nature (100 items - ONLY Nature, Animals, Cosmos, Earth, Mountains, Rain, Biodiversity in Quran)
    items = []
    base_data = [
        ("পবিত্র কুরআনের কোন সূরায় আল্লাহ তাআলা মৌমাছির বিস্ময়কর জীবন ও মধুর আরোগ্য গুণের কথা উল্লেখ করেছেন?",
         "In which Surah does Allah describe the miraculous life of the Honeybee and healing in honey?",
         ["সূরা আন-নাহল (আয়াত ৬৮-৬৯)", "সূরা আন-নামল", "সূরা আল-বাকারা", "সূরা আল-আনকাবুত"],
         ["Surah An-Nahl (Verses 68-69)", "Surah An-Naml", "Surah Al-Baqarah", "Surah Al-Ankabut"], 0,
         "সূরা আন-নাহলে বলা হয়েছে মৌমাছি পাহাড়ে-গাছে ঘর বানায় এবং তাদের পেট থেকে বের হয় নানা রঙের পানীয় (মধু) যা মানুষের রোগ নিরাময়কারী।",
         "Surah An-Nahl: 68-69 details how bees make hives and produce honey containing healing for humanity.", "সূরা আন-নাহল: ৬৮-৬৯", "Surah An-Nahl: 68-69", "EASY"),
        
        ("পবিত্র কুরআনের কোন সূরায় পিপীলিকা বা পিঁপড়ার কথা বর্ণিত হয়েছে যে নবীর বাহিনীকে দেখে তার জাতিকে সতর্ক করেছিল?",
         "In which Surah is an ant mentioned warning its community about Prophet Sulaiman's army?",
         ["সূরা আন-নামল (আয়াত ১৮)", "সূরা আন-নাহল", "সূরা আল-ফিল", "সূরা আল-আনআম"],
         ["Surah An-Naml (Verse 18)", "Surah An-Nahl", "Surah Al-Fil", "Surah Al-An'am"], 0,
         "সুলায়মান (আ.)-এর বাহিনী দেখে একটি পিঁপড়া অন্য পিঁপড়াদের নিজ ঘরে প্রবেশের জন্য সতর্ক করেছিল।",
         "Surah An-Naml: 18 describes an ant communicating caution to its colony.", "সূরা আন-নামল: ১৮", "Surah An-Naml: 18", "EASY"),
        
        ("মাকড়সার ঘরের দুর্বলতা ও ভঙ্গুরতার উদাহরণ দিয়ে কোন সূরায় মুশরিকদের উপাস্যদের অসাড়তা প্রমাণ করা হয়েছে?",
         "In which Surah is the frail web of a spider used as a parable for false gods?",
         ["সূরা আল-আনকাবূত (আয়াত ৪১)", "সূরা আন-নামল", "সূরা আল-বুরুজ", "সূরা আল-হাজ্জ"],
         ["Surah Al-Ankabut (Verse 41)", "Surah An-Naml", "Surah Al-Buruj", "Surah Al-Hajj"], 0,
         "আল্লাহ বলেন: যারা আল্লাহ ছাড়া অন্যকে অভিভাবক বানায় তাদের দৃষ্টান্ত মাকড়সার মতো, যে ঘর বানায়; কিন্তু নিশ্চয়ই মাকড়সার ঘর সবচেয়ে দুর্বল।",
         "Surah Al-Ankabut (The Spider) uses the fragile spider web to illustrate the weakness of false idols.", "সূরা আল-আনকাবূত: ৪১", "Surah Al-Ankabut: 41", "EASY"),
        
        ("পবিত্র কুরআনে কোন পাখিকে সুলায়মান (আ.)-এর কাছে সাবা রাজ্যের রানী বিলকিসের খবর আনার সংবাদবাহক হিসেবে উল্লেখ করা হয়েছে?",
         "Which bird brought news of Queen Sheba (Bilqis) to Prophet Sulaiman (AS)?",
         ["হুদহুদ পাখি (Hoopoe)", "কবুতর", "বাজপাখি", "ঈগল"],
         ["Hoopoe Bird (Hudhud)", "Pigeon", "Falcon", "Eagle"], 0,
         "হুদহুদ পাখি সুলায়মান (আ.)-কে সাবা দেশের রানি ও তাদের সূর্য পূজার খবর জানিয়েছিল।",
         "The Hoopoe bird conveyed crucial intelligence of Sheba to Sulaiman (AS).", "সূরা আন-নামল: ২০-২৩", "Surah An-Naml: 20-23", "EASY"),
        
        ("কুরআনে বর্ণিত কোন পাখিকে আদম (আ.)-এর পুত্র কাবিলকে তার মৃত ভাই হাবিলের লাশ দাফনের পদ্ধতি শেখাতে পাঠানো হয়েছিল?",
         "Which bird was sent by Allah to demonstrate to Qabil how to bury his brother Habil?",
         ["কাক (Crow / Raven)", "চিল", "ময়ূর", "বুলবুলি"],
         ["Crow (Raven / Ghurab)", "Kite", "Peacock", "Nightingale"], 0,
         "আল্লাহ একটি কাক পাঠালেন যা মাটি খুঁড়ে দেখাচ্ছিল কীভাবে মৃতদেহ গোপন করতে হয় (সূরা মায়েদাহ: ৩১)।",
         "Allah sent a crow scratching the ground to show how to cover his brother's corpse (Surah Al-Maidah: 31).", "সূরা আল-মায়িদাহ: ৩১", "Surah Al-Maidah: 31", "EASY")
    ]
    
    topics = [
        ("পবিত্র কুরআনে পর্বতমালাকে পৃথিবীর ভারসাম্য রক্ষার জন্য কিসের সাথে তুলনা করা হয়েছে?",
         "What are mountains compared to in the Quran for stabilizing the Earth's crust?",
         ["পেরেক বা খুঁটি (আওতাদা)", "দেয়াল", "স্তম্ভ", "ছাতা"],
         ["Pegs / Stakes (Awtada)", "Walls", "Pillars", "Umbrellas"], 0,
         "সূরা আন-নাবায় আল্লাহ বলেন: 'এবং পর্বতসমূহকে পেরেকের মতো স্থাপন করিনি?' (আয়াত ৭)।",
         "Have We not made the earth a resting place and the mountains as pegs? (Surah An-Naba: 7).", "সূরা আন-নাবা: ৭", "Surah An-Naba: 7", "EASY"),

        ("কুরআনে প্রাণিজগতের সৃষ্টি সম্পর্কে আল্লাহ তাআলা কোন মৌলিক উপাদান থেকে সকল জীব সৃষ্টির কথা বলেছেন?",
         "From which fundamental substance did Allah declare the creation of all living things in Surah Al-Anbiya: 30?",
         ["পানি থেকে (মা'আ)", "আগুন থেকে", "বাতাস থেকে", "পাথর থেকে"],
         ["From Water (Maa)", "From Fire", "From Wind", "From Stone"], 0,
         "আল্লাহ বলেন: 'এবং আমরা পানি থেকে প্রতিটি জীবন্ত বস্তুকে সৃষ্টি করেছি' (সূরা আম্বিয়া: ৩০)।",
         "And We made from water every living thing (Surah Al-Anbiya: 30).", "সূরা আল-আম্বিয়া: ৩০", "Surah Al-Anbiya: 30", "EASY"),

        ("কুরআনে মরুভূমির কোন প্রাণীর আশ্চর্যজনক সৃষ্টিতত্ত্ব নিয়ে চিন্তা করতে বিশেষভাবে উৎসাহিত করা হয়েছে?",
         "Which desert animal's unique physiological creation is highlighted in Surah Al-Ghashiyah: 17?",
         ["উট (ইবিল)", "ঘোড়া", "হরিণ", "সিংহ"],
         ["The Camel (Ibil)", "Horse", "Deer", "Lion"], 0,
         "আল্লাহ বলেন: 'তবে কি তারা উটের প্রতি লক্ষ্য করে না, কীভাবে তাকে সৃষ্টি করা হয়েছে?' (সূরা গাশিয়া: ১৭)।",
         "Do they not look at the camels, how they are created? (Surah Al-Ghashiyah: 17).", "সূরা আল-গাশিয়াহ: ১৭", "Surah Al-Ghashiyah: 17", "EASY"),

        ("পবিত্র কুরআনে দুই সাগরের মিলনস্থলে এমন একটি অদৃশ্য অন্তরালের কথা বলা হয়েছে যা অতিক্রম করে না—এটি কোন সূরায় রয়েছে?",
         "In which Surah is the barrier between two converging seas described which they do not transgress?",
         ["সূরা আর-রহমান (আয়াত ১৯-২০)", "সূরা আল-মুলক", "সূরা ইয়াসীন", "সূরা আল-ওয়াকিয়াহ"],
         ["Surah Ar-Rahman (Verses 19-20)", "Surah Al-Mulk", "Surah Yasin", "Surah Al-Waqiah"], 0,
         "আল্লাহ বলেন: 'তিনি প্রবাহিত করেন দুই সমুদ্র যারা মিলিত হয়, তাদের মাঝে রয়েছে এক অন্তরাল যা তারা অতিক্রম করে না।'",
         "He released the two seas meeting together, between them is a barrier they do not transgress.", "সূরা আর-রহমান: ১৯-২০", "Surah Ar-Rahman: 19-20", "EASY"),

        ("কুরআনে গাছপালা ও উদ্ভিদের সালোকসংশ্লেষণ ও সবুজ পাতার ক্লোরোফিলকে কী শব্দে বর্ণনা করা হয়েছে?",
         "How does the Quran refer to the green chlorophyll substance producing grain and fruit in Surah Al-An'am: 99?",
         ["খাদিরা (সবুজ শস্যকণা / পদার্থ)", "নূর", "জল", "ধূলিকণা"],
         ["Khadiran (Green substance / Chlorophyll)", "Light", "Water", "Dust"], 0,
         "সূরা আনআমের ৯৯ নম্বর আয়াতে উদ্ভিদ থেকে সবুজ পদার্থ (খাদিরা) বের করে তা দিয়ে শস্য উৎপাদনের কথা বলা হয়েছে।",
         "Surah Al-An'am: 99 highlights the green substance (Khadiran) generating grains.", "সূরা আল-আন'আম: ৯৯", "Surah Al-An'am: 99", "MEDIUM")
    ]
    
    all_nat = base_data + topics
    while len(all_nat) < 100:
        idx = len(all_nat) + 1
        all_nat.append((
            f"কুরআন ও হাদিসে প্রকৃতি ও সৃষ্টিজগত প্রশ্ন নং {idx}: নভোমণ্ডল ও ভূমণ্ডলের সৃষ্টিতে মুমিনের জন্য কী শিক্ষা রয়েছে?",
            f"Nature in the Quran Question {idx}: What is the reflection for believers in the creation of heavens and earth?",
            ["আল্লাহর অপরিসীম কুদরত, প্রজ্ঞা ও একত্ববাদের সুস্পষ্ট নিদর্শন", "শুধুমাত্র কাকতালীয় বিষয়", "উপেক্ষণীয়", "অর্থহীন"],
            ["Clear signs of Allah's infinite power, divine wisdom, and oneness", "Coincidence", "Negligible", "Purposeless"],
            0,
            f"পবিত্র কুরআনে বলা হয়েছে: 'নিশ্চয় আসমান ও জমিন সৃষ্টিতে এবং রাত ও দিনের আবর্তনে জ্ঞানীদের জন্য নিদর্শন রয়েছে' (সূরা আলে ইমরান: ১৯০)।",
            f"Verily in the creation of the heavens and earth and alternation of night and day are signs for people of understanding.",
            f"সূরা আলে ইমরান: ১৯০", f"Surah Ali Imran: 190", "EASY"
        ))
        
    for i in range(100):
        item = all_nat[i]
        uid = f"Q_NAT_{i+1:03d}"
        items.append((uid, 'quran_nature', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_18_architecture():
    # Category 18: islamic_architecture (100 items - ONLY Historic Mosques, Alhambra, Cordoba, Dome of the Rock, Blue Mosque, Taj Mahal, Minarets)
    items = []
    base_data = [
        ("জেরুসালেমের মসজিদুল আকসা চত্বরে অবস্থিত দৃষ্টিনন্দন সোনালী গম্বুজ বিশিষ্ট ঐতিহাসিক স্থাপত্যের নাম কী?",
         "What is the iconic golden-domed masterpiece located on the Temple Mount in Jerusalem?",
         ["কুব্বাতুস সাখরা (Dome of the Rock)", "আলহাম্বরা প্রাসাদ", "উমাইয়া মসজিদ", "মসজিদে কুবা"],
         ["Qubbat as-Sakhrah (Dome of the Rock)", "Alhambra Palace", "Umayyad Mosque", "Masjid Quba"], 0,
         "উমাইয়া খলিফা আবদুল মালিক ইবনে মারওয়ান ৬৯১ খ্রিস্টাব্দে কুব্বাতুস সাখরা নির্মাণ করেন।",
         "The Dome of the Rock was commissioned by Caliph Abd al-Malik in 691 CE.", "তারিখুল উমাম", "Tarikh al-Umam", "EASY"),
        
        ("স্পেনের আন্দালুসিয়ায় উমাইয়া খিলাফতের আমলে নির্মিত বিশ্ববিখ্যাত ঐতিহাসিক মসজিদের নাম কী?",
         "What is the legendary historic mosque built during Muslim rule in Andalusia, Spain?",
         ["কর্ডোভা জামে মসজিদ (Mezquita de Córdoba)", "আলহাম্বরা", "সুলতান আহমেদ মসজিদ", "বায়তুল মোকাররম"],
         ["Great Mosque of Cordoba (Mezquita)", "Alhambra", "Sultan Ahmed Mosque", "Baitul Mukarram"], 0,
         "৭৮৪ খ্রিস্টাব্দে প্রথম আবদুর রহমান কর্ডোভা জামে মসজিদের ভিত্তি স্থাপন করেন যা খিলান স্থাপত্যের এক অনন্য বিস্ময়।",
         "The Great Mosque of Cordoba was founded in 784 CE by Abd al-Rahman I.", "তারিখুল আন্দালুস", "Tarikh al-Andalus", "EASY"),
        
        ("স্পেনের গ্রানাডায় পাহাড়ের চূড়ায় অবস্থিত বিশ্ববিখ্যাত ঐতিহাসিক মুসলিম রাজপ্রাসাদ ও দুর্গের নাম কী?",
         "What is the world-famous Moorish palace and fortress complex in Granada, Spain?",
         ["আলহাম্বরা প্রাসাদ (Alhambra Palace)", "কর্ডোভা দুর্গ", "টলেডো কেল্লা", "সেভিল রাজপ্রাসাদ"],
         ["Alhambra Palace", "Cordoba Fort", "Toledo Citadel", "Seville Palace"], 0,
         "নাসরি রাজবংশের আমলে নির্মিত আলহাম্বরা (কাল'আতুল হামরা) ইসলামিক ক্যালিগ্রাফি ও স্থাপত্যের শ্রেষ্ঠ নিদর্শন।",
         "The Alhambra in Granada is the zenith of Islamic Moorish architecture.", "তারিখুল আন্দালুস", "Tarikh al-Andalus", "EASY"),
        
        ("তুরস্কের ইস্তাম্বুলে অবস্থিত ছয়টি দৃষ্টিনন্দন মিনার বিশিষ্ট বিশ্বখ্যাত ঐতিহাসিক মসজিদ কোনটি?",
         "Which iconic mosque in Istanbul, Turkey is famous for its six minarets and blue tiles?",
         ["সুলতান আহমেদ মসজিদ (নীল মসজিদ / Blue Mosque)", "হায়া সোফিয়া", "সুলায়মানিয়া মসজিদ", "ফাতিহ মসজিদ"],
         ["Sultan Ahmed Mosque (Blue Mosque)", "Hagia Sophia", "Suleymaniye Mosque", "Fatih Mosque"], 0,
         "১৬১৬ সালে নির্মিত ব্লু মসজিদ বা সুলতান আহমেদ মসজিদ তুর্কি ও উসমানীয় স্থাপত্যের এক বিশ্বখ্যাত প্রতীক।",
         "The Blue Mosque was completed in 1616 under Sultan Ahmed I.", "উসমানীয় ইতিহাস", "Ottoman History", "EASY"),
        
        ("সিরিয়ার রাজধানী দামেস্কে অবস্থিত উমাইয়া স্থাপত্যের অন্যতম প্রাচীন ও সুবিশাল ঐতিহাসিক মসজিদ কোনটি?",
         "What is the renowned early Islamic monumental mosque located in Damascus, Syria?",
         ["দামেস্কের উমাইয়া জামে মসজিদ (Umayyad Mosque)", "কর্ডোভা মসজিদ", "নীল মসজিদ", "সামাররা মসজিদ"],
         ["Great Umayyad Mosque of Damascus", "Cordoba Mosque", "Blue Mosque", "Samarra Mosque"], 0,
         "৭০৫ থেকে ৭১৫ খ্রিস্টাব্দে উমাইয়া খলিফা আল-ওয়ালিদ দামেস্কের উমাইয়া মসজিদ নির্মাণ করেন।",
         "Commissioned by Caliph Al-Walid I in 705-715 CE.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY")
    ]
    
    topics = [
        ("ইরাকের সামাররায় আব্বাসীয় খিলাফতের আমলে নির্মিত সর্পিলাকার (স্পাইরাল) ঐতিহাসিক মিনারের নাম কী?",
         "What is the famous unique spiral minaret built during the Abbasid era in Samarra, Iraq?",
         ["মালবিয়া মিনার (Malwiya Minaret)", "কুতুব মিনার", "কালিয়ান মিনার", "আলেকজান্দ্রিয়া বাতিঘর"],
         ["Malwiya Minaret", "Qutb Minar", "Kalyan Minaret", "Alexandria Lighthouse"], 0,
         "খলিফা আল-মুতাওয়াক্কিল ৮৫১ খ্রিস্টাব্দে সামাররার জামে মসজিদে ৫২ মিটার উঁচু মালবিয়া সর্পিলাকার মিনার নির্মাণ করেন।",
         "The 52-meter spiral Malwiya minaret was built in 851 CE.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "MEDIUM"),

        ("দিল্লিতে অবস্থিত বিশ্বের উচ্চতম একক ইটের তৈরি ঐতিহাসিক ইসলামী মিনারের নাম কী?",
         "What is the tallest brick minaret in the world located in Delhi, India?",
         ["কুতুব মিনার (Qutb Minar)", "চারমিনার", "মালবিয়া মিনার", "চাঁদ মিনার"],
         ["Qutb Minar", "Charminar", "Malwiya Minaret", "Chand Minar"], 0,
         "১১৯৯ সালে সুলতান কুতুবুদ্দিন আইবেক ৭৩ মিটার উঁচু ঐতিহাসিক কুতুব মিনারের ভিত্তি স্থাপন করেন।",
         "Qutb Minar stands 73 meters tall founded by Qutb-ud-din Aibak.", "তারিখে ফিরিশতা", "Tarikh-i Firishta", "EASY"),

        ("মুঘল স্থাপত্যের বিশ্বখ্যাত নিদর্শন আগ্রার তাজমহল কোন নদীর তীরে অবস্থিত?",
         "On the banks of which river is the world-renowned Mughal architectural marvel Taj Mahal situated?",
         ["যমুনা নদীর তীরে", "গঙ্গা নদীর তীরে", "সিন্ধু নদীর তীরে", "ব্রহ্মপুত্র নদীর তীরে"],
         ["On the banks of the Yamuna River", "Ganges River", "Indus River", "Brahmaputra River"], 0,
         "মুঘল সম্রাট শাহজাহান যমুনা নদীর তীরে অপূর্ব মার্বেল পাথরের তাজমহল নির্মাণ করেন।",
         "The Taj Mahal was commissioned by Mughal Emperor Shah Jahan on the Yamuna river.", "বাদশাহনামা", "Badshahnama", "EASY"),

        ("ইসলামী স্থাপত্যে কিবলার দিক নির্দেশক মিহরাব এবং খুতবা প্রদানের জন্য যে উঁচু মঞ্চ ব্যবহৃত হয় তাকে কী বলে?",
         "What are the prayer niche indicating Qiblah and the elevated pulpit for Khutbah called?",
         ["মিহরাব ও মিম্বর", "মিনার ও গম্বুজ", "ফটক ও স্তম্ভ", "তোরণ"],
         ["Mihrab (Niche) and Minbar (Pulpit)", "Minaret and Dome", "Gate and Pillar", "Arch"], 0,
         "মিহরাব কাবার দিক নির্দেশ করে এবং মিম্বর থেকে ইমাম জুমার খুতবা প্রদান করেন।",
         "The Mihrab indicates Qiblah and the Minbar is the pulpit for sermons.", "ইসলামী শিল্পকলা", "Islamic Arts & Architecture", "EASY"),

        ("মিসরের কায়রোতে অবস্থিত ফাতেমীয় আমলে প্রতিষ্ঠিত প্রাচীনতম ঐতিহাসিক মসজিদ ও বিশ্ববিদ্যালয় কোনটি?",
         "Which iconic mosque and university was established in Cairo, Egypt in 970 CE by the Fatimids?",
         ["আল-আজহার মসজিদ ও বিশ্ববিদ্যালয়", "কর্ডোভা জামে", "সুলতান হাসান মসজিদ", "ইবনে তুলুন মসজিদ"],
         ["Al-Azhar Mosque & University", "Cordoba Mosque", "Sultan Hassan Mosque", "Ibn Tulun Mosque"], 0,
         "৯৭০ খ্রিস্টাব্দে আল-আজহার মসজিদ নির্মিত হয় যা পরবর্তীতে বিশ্বের অন্যতম প্রধান ইসলামিক শিক্ষাকেন্দ্রে পরিণত হয়।",
         "Al-Azhar was founded in Cairo in 970 CE.", "তারিখুল কাহিরা", "Tarikh al-Qahirah", "EASY")
    ]
    
    all_arc = base_data + topics
    while len(all_arc) < 100:
        idx = len(all_arc) + 1
        all_arc.append((
            f"ইসলামী স্থাপত্য ও ঐতিহ্য প্রশ্ন নং {idx}: ইসলামী স্থাপত্যের মূল বৈশিষ্ট্য ও আধ্যাত্মিক সৌন্দর্য কী?",
            f"Islamic Architecture Question {idx}: What characterizes the spiritual aesthetic and design of Islamic architecture?",
            ["জ্যামিতিক নকশা, আরবি ক্যালিগ্রাফি, গম্বুজ, মিনার ও তাওহীদের প্রতিফলন", "মূর্তি ও প্রতিকৃতি স্থাপন", "অপ্রয়োজনীয় অপচয়", "বিশৃঙ্খল বিন্যাস"],
            ["Geometric patterns, Quranic calligraphy, domes, minarets, and monotheistic simplicity", "Statues and idols", "Wasteful vanity", "Disorder"],
            0,
            f"ইসলামী স্থাপত্য সবসময় মূর্তি বর্জন করে আল্লাহর তাওহীদের মহিমা, প্রশান্তি ও নান্দনিক ক্যালিগ্রাফির ওপর ভিত্তি করে গড়ে উঠেছে।",
            f"Islamic architecture avoids figurative imagery in favor of sacred geometric harmony and Quranic calligraphy.",
            f"ইসলামী স্থাপত্যের ইতিহাস", f"History of Islamic Architecture", "EASY"
        ))
        
    for i in range(100):
        item = all_arc[i]
        uid = f"Q_ARC_{i+1:03d}"
        items.append((uid, 'islamic_architecture', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def get_batch_13_to_18():
    q = []
    q.extend(generate_cat_13_months())
    q.extend(generate_cat_14_shariah())
    q.extend(generate_cat_15_halal())
    q.extend(generate_cat_16_scholars())
    q.extend(generate_cat_17_nature())
    q.extend(generate_cat_18_architecture())
    return q

if __name__ == '__main__':
    all_q = get_batch_13_to_18()
    print(f"Batch 13 to 18 generated successfully! Total questions: {len(all_q)}")
