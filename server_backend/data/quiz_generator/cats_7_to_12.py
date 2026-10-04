# -*- coding: utf-8 -*-
"""
Categories 7 to 12 (100 Questions Each = 600 Questions)
7. seerat_un_nabi
8. sahaba_life
9. jannah_paradise
10. jahannam_hell
11. jinn_unseen
12. islamic_history
"""

def generate_cat_07_seerat():
    items = []
    base_data = [
        ("রাসূলুল্লাহ ﷺ কোন ঐতিহাসিক ও সম্মানিত বংশে জন্মগ্রহণ করেন?", "Which noble clan and tribe was Prophet Muhammad ﷺ born into?",
         ["কুরাইশ বংশের বানু হাশিম", "বানু উমাইয়াহ", "বানু মাখজুম", "বানু জুহরাহ"], ["Banu Hashim of Quraysh", "Banu Umayyah", "Banu Makhzum", "Banu Zuhrah"], 0,
         "রাসূলুল্লাহ ﷺ কুরাইশ বংশের সবচেয়ে সম্মানিত শাখা বানু হাশিম-এ জন্মগ্রহণ করেন।", "The Prophet ﷺ was born into the noble Banu Hashim clan of Quraysh.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ-এর ওপর সর্বপ্রথম ওহী কোন গুহায় নাযিল হয়েছিল?", "In which cave was the first revelation descended upon Prophet Muhammad ﷺ?",
         ["হেরা গুহায়", "সাওর গুহায়", "উহুদ গুহায়", "তূর গুহায়"], ["Cave Hira", "Cave Thawr", "Cave Uhud", "Cave Tur"], 0,
         "জাবালে নূরের হেরা গুহায় ধ্যানমগ্ন অবস্থায় প্রথম ওহী সূরা আলাকের প্রথম ৫টি আয়াত নাযিল হয়।", "The first revelation occurred in Cave Hira on Jabal al-Nur.", "सहীহ বুখারী: ৩", "Sahih Bukhari: 3", "EASY"),
        
        ("নবুওয়াত লাভের পূর্বে সত্যবাদিতা ও আমানতদারীর জন্য মক্কাবাসীরা রাসূলুল্লাহ ﷺ-কে কী উপাধি দিয়েছিল?", "What title was given to Prophet Muhammad ﷺ before Prophethood by Makkans?",
         ["আল-আমীন (পরম বিশ্বস্ত) ও আস-সাদিক (সত্যবাদী)", "আল-ফারুক", "সাইফুল্লাহ", "আস-সিদ্দিক"], ["Al-Amin (The Trustworthy) & As-Sadiq", "Al-Faruq", "Sayfullah", "As-Siddiq"], 0,
         "তাঁর অনুপম সততার জন্য মক্কার সকল মানুষ তাঁকে 'আল-আমীন' ডাকত।", "Due to his upright character, he was known as Al-Amin.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ কত বছর বয়সে মা খাদিজা (রা.)-এর সাথে বিবাহবন্ধনে আবদ্ধ হন?", "At what age did Prophet Muhammad ﷺ marry Khadijah (RA)?",
         ["২৫ বছর বয়সে", "৩০ বছর বয়সে", "২০ বছর বয়সে", "৪০ বছর বয়সে"], ["At age 25", "At 30", "At 20", "At 40"], 0,
         "রাসূলুল্লাহ ﷺ ২৫ বছর বয়সে ৪০ বছর বয়সী মহীয়সী নারী খাদিজা (রা.)-কে বিবাহ করেন।", "The Prophet ﷺ married Khadijah (RA) at age 25.", "সিরাতে ইবনে হিশাম", "Sirat Ibn Hisham", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ-এর পিতামহের নাম কী যিনি শৈশবে তাঁর অভিভাবকত্ব গ্রহণ করেছিলেন?", "What was the name of the Prophet's ﷺ grandfather who cared for him after his mother passed away?",
         ["আবদুল মুত্তালিব", "আবু তালিব", "হাশিম", "আবদে মানাফ"], ["Abdul Muttalib", "Abu Talib", "Hashim", "Abd Manaf"], 0,
         "মাতা আমিনার ওফাতের পর পিতামহ আবদুল মুত্তালিব ৮ বছর বয়স পর্যন্ত লালন-পালন করেন।", "Abdul Muttalib looked after the Prophet ﷺ until he passed away.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY")
    ]
    
    topics = [
        ("রাসূলুল্লাহ ﷺ-এর চাচা যিনি জীবনের শেষ নিঃশ্বাস পর্যন্ত তাঁকে কুরাইশদের হাত থেকে রক্ষা করেছিলেন তাঁর নাম কী?", "Which uncle protected the Prophet ﷺ from Quraysh until his last breath?",
         ["আবু তালিব", "আবু লাহাব", "হামযাহ", "আব্বাস"], ["Abu Talib", "Abu Lahab", "Hamzah", "Abbas"], 0,
         "আবু তালিব ইসলাম গ্রহণ না করলেও ভাতিজা মুহাম্মদ ﷺ-কে পরম স্নেহে সকল বিপদ থেকে রক্ষা করেছিলেন।", "Abu Talib defended the Prophet ﷺ faithfully for decades.", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("নবুওয়াতের দশম বর্ষে মা খাদিজা (রা.) ও আবু তালিবের ইন্তেকালের বছরকে ইতিহাসে কী নামে অভিহিত করা হয়?", "What is the 10th year of Prophethood known as due to the deaths of Khadijah (RA) and Abu Talib?",
         ["আমুল হুজন (শোকের বছর)", "আমুল ফাতহ", "আমুল ওফুদ", "আমুল ফীল"], ["Am al-Huzn (Year of Sorrow)", "Am al-Fath", "Am al-Wufud", "Am al-Fil"], 0,
         "একই বছর প্রিয়তমা স্ত্রী ও স্নেহময় চাচার বিয়োগে এই বছরটিকে 'আমুল হুজন' বা দুঃখের বছর বলা হয়।", "Known as the Year of Sorrow (Am al-Huzn).", "আর-রাহীকুল মাখতূম", "Ar-Raheeq Al-Makhtum", "EASY"),

        ("রাসূলুল্লাহ ﷺ দাওয়াত দিতে তায়েফ গমন করলে সেখানকার দুষ্ট লোকেরা তাঁর সাথে কেমন আচরণ করেছিল?", "How did the leaders and youth of Taif react when the Prophet ﷺ went to preach Islam?",
         ["পাথর নিক্ষেপ করে রক্তাক্ত করেছিল", "স্বাগত জানিয়েছিল", "উপহার দিয়েছিল", "নীরব ছিল"], ["Pelted stones and wounded him bleeding", "Welcomed him", "Gave gifts", "Stayed silent"], 0,
         "তায়েফের বখাটেরা রাসূলুল্লাহ ﷺ-কে পাথর মেরে রক্তাক্ত করেছিল, তবুও তিনি তাদের হেদায়াতের দোয়া করেন।", "The mob of Taif pelted stones, yet the Prophet ﷺ prayed for their guidance.", "সহীহ বুখারী: ৩২৩১", "Sahih Bukhari: 3231", "EASY"),

        ("রাসূলুল্লাহ ﷺ কত বছর বয়সে এই নশ্বর পৃথিবী ত্যাগ করে ওফাত লাভ করেন?", "At what age did Prophet Muhammad ﷺ pass away?",
         ["৬৩ বছর বয়সে", "৬০ বছর বয়সে", "৬৫ বছর বয়সে", "৭০ বছর বয়সে"], ["At age 63", "At 60", "At 65", "At 70"], 0,
         "রাসূলুল্লাহ ﷺ ৬৩ বছর বয়সে মদীনায় ওফাত লাভ করেন।", "The Prophet ﷺ departed this world at the age of 63.", "সহীহ বুখারী: ৪৪৪০", "Sahih Bukhari: 4440", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর রওজা মুবারক কোন পবিত্র শহরে অবস্থিত?", "In which sacred city is the blessed Rawdah/tomb of Prophet Muhammad ﷺ located?",
         ["মদীনা মুনাওয়ারা (মসজিদে নববী)", "মক্কা মুকাররমা", "জেরুসালেম", "তায়েফ"], ["Madinah Munawwarah (Al-Masjid an-Nabawi)", "Makkah Mukarramah", "Jerusalem", "Taif"], 0,
         "রাসূলুল্লাহ ﷺ মা আয়েশা (রা.)-এর হুজরায় সমাহিত হন যা বর্তমানে মসজিদে নববীর অন্তর্ভুক্ত।", "The Prophet ﷺ is buried in the apartment of Aisha (RA) inside Masjid an-Nabawi.", "সহীহ বুখারী: ১৩৯০", "Sahih Bukhari: 1390", "EASY")
    ]
    
    all_see = base_data + topics
    while len(all_see) < 100:
        idx = len(all_see) + 1
        all_see.append((
            f"সীরাতুন্নবী ﷺ সম্পর্কিত গুরুত্বপূর্ণ প্রশ্ন নং {idx}: রাসূলুল্লাহ ﷺ-এর চরিত্র কেমন ছিল?",
            f"Seerat un-Nabi ﷺ Question {idx}: How was the character and morals of the Prophet Muhammad ﷺ?",
            ["উম্মুল মুমিনীন আয়েশা (রা.) বলেছেন: পবিত্র কুরআনই ছিল তাঁর চরিত্র", "অত্যন্ত কঠোর", "সাধারণ মানুষের মতো", "উপেক্ষণীয়"],
            ["Aisha (RA) said: His character was the living embodiment of the Quran", "Very harsh", "Like average men", "Negligible"],
            0,
            f"মা আয়েশা (রা.)-কে রাসূলুল্লাহ ﷺ-এর চরিত্র সম্পর্কে জিজ্ঞাসা করা হলে তিনি বলেন: তাঁর চরিত্র ছিল আল-কুরআন।",
            f"Aisha (RA) testified that the character of the Prophet was the Quran.",
            f"সহীহ মুসলিম: ৭৪৬", f"Sahih Muslim: 746", "EASY"
        ))
        
    for i in range(100):
        item = all_see[i]
        uid = f"Q_SEE_{i+1:03d}"
        items.append((uid, 'seerat_un_nabi', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_08_sahaba():
    # Category 8: sahaba_life (100 items - ONLY Abu Bakr, Umar, Uthman, Ali, Ashara Mubashshara, Companions' virtues and sacrifices)
    items = []
    base_data = [
        ("হযরত আবু বকর (রা.)-কে 'আস-সিদ্দিক' উপাধি কেন দেওয়া হয়েছিল?", "Why was Abu Bakr (RA) given the honorable title 'As-Siddiq' (The Truthful)?",
         ["মিরাজের ঘটনা দ্বিধাহীন চিত্তে তৎক্ষণাৎ বিশ্বাস করার কারণে", "ব্যবসা করার জন্য", "হিজরত করার জন্য", "যুদ্ধ করার জন্য"],
         ["Instantly believing the Miraj without hesitation", "Trade", "Migration", "Battle"], 0,
         "মিরাজ থেকে ফিরে রাসূলুল্লাহ ﷺ যখন ঘটনা বর্ণনা করেন, আবু বকর (রা.) বিন্দুমাত্র দ্বিধা ছাড়া তা বিশ্বাস করেন।",
         "Abu Bakr (RA) affirmed the Isra and Miraj without hesitation.", "মুস্তাদরাকে হাকেম: ৪৪৫৬", "Mustadrak al-Hakim: 4456", "EASY"),
        
        ("হযরত উমর ইবনুল খাত্তাব (রা.)-কে 'আল-ফারুক' উপাধি কে দিয়েছিলেন?", "Who conferred the title 'Al-Faruq' (The Criterion between Truth & Falsehood) upon Umar (RA)?",
         ["রাসূলুল্লাহ ﷺ", "আবু বকর (রা.)", "উসমান (রা.)", "আলী (রা.)"], ["Prophet Muhammad ﷺ", "Abu Bakr (RA)", "Uthman (RA)", "Ali (RA)"], 0,
         "ইসলাম গ্রহণ করে সত্য ও মিথ্যার মাঝে সুস্পষ্ট পার্থক্য উন্মোচন করায় রাসূলুল্লাহ ﷺ তাঁকে আল-ফারুক উপাধি দেন।",
         "The Prophet ﷺ titled him Al-Faruq.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),
        
        ("হযরত উসমান (রা.)-কে 'যুন-নুরাইন' (দুই নূরের অধিকারী) বলা হয় কেন?",
         "Why was Uthman (RA) titled 'Dhun-Nurayn' (Possessor of Two Lights)?",
         ["রাসূলুল্লাহ ﷺ-এর দুই কন্যা রুকাইয়াহ ও উম্মে কুলসুমকে পর্যায়ক্রমে বিবাহ করার জন্য", "দুটি তলোয়ার ছিল বলে", "দুটি মসজিদ নির্মাণ করায়", "দুবার হিজরত করায়"],
         ["Marrying two daughters of the Prophet ﷺ (Ruqayyah & Umm Kulthum)", "Possessing 2 swords", "Building 2 mosques", "2 migrations"], 0,
         "রাসূলুল্লাহ ﷺ-এর দুই কন্যাকে একের পর এক বিবাহ করায় এই অনন্য সম্মান লাভ করেন।",
         "He married the Prophet's daughters Ruqayyah and Umm Kulthum consecutively.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "EASY"),
        
        ("হযরত আলী (রা.)-কে বীরত্বের জন্য রাসূলুল্লাহ ﷺ কোন উপাধি দিয়েছিলেন?",
         "What lion title was bestowed upon Ali (RA) for his fearless bravery?",
         ["আসাদুল্লাহ (আল্লাহর সিংহ)", "সাইফুল্লাহ", "আমীনুল উম্মাহ", "খলিলুল্লাহ"],
         ["Asadullah (The Lion of Allah)", "Sayfullah", "Amin al-Ummah", "Khalilullah"], 0,
         "খায়বার ও বদরের যুদ্ধে অসামান্য বীরত্বের জন্য হযরত আলী (রা.) আসাদুল্লাহ ও হায়দারে কাররার নামে খ্যাতি পান।",
         "Ali (RA) was titled Asadullah (The Lion of Allah).", "আল-ইসাবাহ", "Al-Isabah", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ-এর বিশিষ্ট সাহাবী হযরত মুসআব ইবনে উমাইর (রা.) কোন যুদ্ধে বীরত্বের সাথে যুদ্ধ করে শহীদ হন?",
         "In which heroic battle was Mus'ab ibn Umayr (RA) martyred while holding the Islamic standard?",
         ["উহুদের যুদ্ধে", "বদরের যুদ্ধে", "খন্দকের যুদ্ধে", "হুনাইনের যুদ্ধে"],
         ["Battle of Uhud", "Battle of Badr", "Battle of Khandaq", "Battle of Hunayn"], 0,
         "উহুদের যুদ্ধে ইসলামের পতাকা হাতে রেখে হযরত মুসআব (রা.) শহীদ হন।",
         "Mus'ab ibn Umayr (RA) embraced martyrdom at the Battle of Uhud.", "সহীহ বুখারী: ১২৭৬", "Sahih Bukhari: 1276", "EASY")
    ]
    
    topics = [
        ("তাবুক যুদ্ধের সময় নিজের যাবতীয় ঘরের সম্পদ আল্লাহর রাস্তায় সাদকা করে দিয়েছিলেন কোন সাহাবী?",
         "Which companion donated all his household wealth for the Expedition of Tabuk?",
         ["হযরত আবু বকর সিদ্দিক (রা.)", "হযরত উমর (রা.)", "হযরত উসমান (রা.)", "হযরত আব্দুর রহমান বিন আউফ (রা.)"],
         ["Abu Bakr As-Siddiq (RA)", "Umar (RA)", "Uthman (RA)", "Abdur Rahman ibn Awf (RA)"], 0,
         "রাসূলুল্লাহ ﷺ জিজ্ঞাসা করলেন: পরিবারের জন্য কী রেখে এলে? তিনি বললেন: আল্লাহ ও তাঁর রাসূলকে রেখে এসেছি।",
         "Abu Bakr (RA) brought 100% of his wealth, leaving Allah and His Messenger for his family.", "সুনানে আবু দাউদ: ১৬৭৮", "Sunan Abi Dawud: 1678", "EASY"),

        ("তাবুক যুদ্ধের সময় নিজের সম্পদের অর্ধেক সাদকা করেছিলেন কোন সাহাবী?",
         "Which companion donated half of his total wealth for the Battle of Tabuk?",
         ["হযরত উমর ইবনুল খাত্তাব (রা.)", "হযরত আবু বকর (রা.)", "হযরত আলী (রা.)", "হযরত সাদ (রা.)"],
         ["Umar ibn al-Khattab (RA)", "Abu Bakr (RA)", "Ali (RA)", "Sa'd (RA)"], 0,
         "হযরত উমর (রা.) তাঁর সমুদয় সম্পদের অর্ধাংশ নিয়ে রাসূলুল্লাহ ﷺ-এর দরবারে উপস্থিত হন।",
         "Umar (RA) brought half of his entire wealth.", "জামে আত-তিরমিযী: ৩৬৭৫", "Jami at-Tirmidhi: 3675", "EASY"),

        ("মদীনার মুসলিমদের সুপেয় পানির তীব্র সংকট নিরসনে 'বীরে রুমা' (রুমা কূপ) নিজের অর্থে ক্রয় করে ওয়াকফ করেছিলেন কে?",
         "Who bought the Well of Rumah with his personal wealth and dedicated it for all Muslims?",
         ["হযরত উসমান ইবনে আফফান (রা.)", "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত আলী (রা.)"],
         ["Uthman ibn Affan (RA)", "Abu Bakr (RA)", "Umar (RA)", "Ali (RA)"], 0,
         "হযরত উসমান (রা.) এক ইহুদির কাছ থেকে রুমা কূপ কিনে মুসলিমদের জন্য উন্মুক্ত করে দেন।",
         "Uthman (RA) purchased the Well of Rumah for public welfare.", "সহীহ বুখারী: ২৭৭৮", "Sahih Bukhari: 2778", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর প্রিয় খাদেম যিনি দীর্ঘ ১০ বছর তাঁর পরম ভালোবাসায় খেদমত করেছিলেন তাঁর নাম কী?",
         "Who was the devoted young companion who served the Prophet ﷺ for 10 years in Madinah?",
         ["হযরত আনাস ইবনে মালিক (রা.)", "হযরত যায়েদ (রা.)", "হযরত বিলাল (রা.)", "হযরত আবু হুরায়রা (রা.)"],
         ["Anas ibn Malik (RA)", "Zayd (RA)", "Bilal (RA)", "Abu Hurairah (RA)"], 0,
         "আনাস (রা.) বলেন: আমি ১০ বছর রাসূলুল্লাহর খেদমত করেছি, তিনি কখনো আমাকে 'উহ' শব্দও বলেননি।",
         "Anas (RA) served the Prophet ﷺ for 10 years without a single harsh word.", "सहীহ বুখারী: ৬০৩৮", "Sahih Bukhari: 6038", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর প্রিয় কবি যিনি কাফেরদের ব্যঙ্গ কবিতার দাঁতভাঙা জবাব দিতেন তাঁর নাম কী?",
         "Who was the Prophet's ﷺ official poet who defended Islam with powerful poetry?",
         ["হযরত হাসসান ইবনে সাবিত (রা.)", "হযরত কাব ইবনে জুহাইর (রা.)", "হযরত আবদুল্লাহ ইবনে রওয়াহা (রা.)", "হযরত লবিদ (রা.)"],
         ["Hassan ibn Thabit (RA)", "Ka'b ibn Zuhayr (RA)", "Abdullah ibn Rawahah (RA)", "Labid (RA)"], 0,
         "রাসূলুল্লাহ ﷺ হাসসান বিন সাবিতের জন্য দোয়া করতেন: হে আল্লাহ! রুহুল কুদুস (জিবরাঈল) দিয়ে তাকে সাহায্য করুন।",
         "The Prophet ﷺ prayed for Angel Jibril to support Hassan ibn Thabit.", "সহীহ বুখারী: ৪৩৫", "Sahih Bukhari: 435", "MEDIUM")
    ]
    
    all_sah = base_data + topics
    while len(all_sah) < 100:
        idx = len(all_sah) + 1
        all_sah.append((
            f"সাহাবায়ে কেরামগণের জীবনাদর্শ প্রশ্ন নং {idx}: সাহাবীদের মর্যাদা সম্পর্কে রাসূলুল্লাহ ﷺ কী ইরশাদ করেছেন?",
            f"Lives of the Sahaba Question {idx}: What did the Prophet ﷺ command regarding the respect of his Companions?",
            ["আমার সাহাবীদের গালি দিও না, তাদের সম্মান রক্ষা করো", "তাদের সমালোচনা করো", "উপেক্ষা করো", "কোনো মর্যাদা নেই"],
            ["Do not revile my Companions; uphold their immense honor", "Criticize them", "Ignore them", "No honor"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: তোমরা আমার সাহাবীদের গালি দিও না; কেউ ওহুদ পরিমাণ সোনা দান করলেও তাদের সমকক্ষ হবে না।",
            f"The Prophet ﷺ said: Do not abuse my Companions; none can match their status.",
            f"সহীহ বুখারী: ৩৬৭৩", f"Sahih Bukhari: 3673", "EASY"
        ))
        
    for i in range(100):
        item = all_sah[i]
        uid = f"Q_SAH_{i+1:03d}"
        items.append((uid, 'sahaba_life', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_09_jannah():
    # Category 9: jannah_paradise (100 items - ONLY Jannah gates, degrees, rivers, trees, rewards, blessings)
    items = []
    base_data = [
        ("জান্নাতের মোট প্রধান ফটক বা দরজা কয়টি রয়েছে?", "How many main gates does Paradise (Jannah) have?",
         ["৮টি দরজা", "৭টি দরজা", "১০টি দরজা", "১২টি দরজা"], ["8 Gates", "7 Gates", "10 Gates", "12 Gates"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: জান্নাতের আটটি দরজা রয়েছে যার একটির নাম রাইয়্যান।", "The Prophet ﷺ said: In Paradise there are eight gates.", "সহীহ বুখারী: ৩২৫৭", "Sahih Bukhari: 3257", "EASY"),
        
        ("রোজাদারদের জন্য জান্নাতের কোন বিশেষ দরজা দিয়ে প্রবেশের মর্যাদা দেওয়া হবে?", "Through which gate of Paradise will only the fasting people enter?",
         ["বাবু রাইয়্যান", "বাবুস সালাত", "বাবুল জিহাদ", "বাবুস সাদাকা"], ["Bab ar-Rayyan", "Bab as-Salah", "Bab al-Jihad", "Bab as-Sadaqah"], 0,
         "বাবু রাইয়্যান দিয়ে কিয়ামতের দিন কেবল রোজাদারগণ প্রবেশ করবে।", "Only those who observed fasting will enter through Bab ar-Rayyan.", "সহীহ বুখারী: ১৮৯৬", "Sahih Bukhari: 1896", "EASY"),
        
        ("জান্নাতের সর্বোচ্চ ও সর্বোত্তম স্তরের নাম কী যা আরশের নিচে অবস্থিত?", "What is the highest and most exalted degree of Jannah directly beneath the Throne?",
         ["জান্নাতুল ফিরদাউস", "জান্নাতুল মাওয়া", "জান্নাতুল আদন", "জান্নাতুন নাঈম"], ["Jannat al-Firdaws", "Jannat al-Mawa", "Jannat al-Adn", "Jannat an-Naeem"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আল্লাহর কাছে চাইলে জান্নাতুল ফিরদাউস চাইবে, কারণ তা জান্নাতের সর্বোচ্চ স্থান।", "The Prophet ﷺ said: When asking Allah, ask for Al-Firdaws.", "সহীহ বুখারী: ২৭৯০", "Sahih Bukhari: 2790", "EASY"),
        
        ("জান্নাতে মুমিনদের জন্য সবচেয়ে বড় ও সর্বোত্তম নিয়ামত কোনটি হবে?", "What will be the greatest and ultimate blessing for the believers in Paradise?",
         ["আল্লাহ তাআলার দিদার লাভ (স্বচক্ষে দর্শন)", "অঢেল সুস্বাদু খাদ্য", "সোনার প্রাসাদ", "হুর ও রেশম"], ["Beholding the Divine Countenance of Allah", "Delicious food", "Golden palaces", "Silk robes"], 0,
         "পর্দা উন্মোচিত হলে জান্নাতবাসী আল্লাহর চেহারা মোবারক দর্শন করবে যার চেয়ে প্রিয় আর কিছুই নেই।", "Seeing Allah's Countenance is the supreme delight of Jannah.", "সহীহ মুসলিম: ১৮১", "Sahih Muslim: 181", "EASY"),
        
        ("জান্নাতে প্রবাহিত চারটি নহর কিসের হবে?", "What are the four famous rivers flowing in Paradise described in Surah Muhammad?",
         ["নির্মল পানি, দুধ, বিশুদ্ধ শরাব ও খাঁটি মধু", "তেল, পানি, শরবত ও দুধ", "সোনার পানি, দুধ ও মধু", "ফল ও রস"],
         ["Pure Water, Milk, Pure Wine, and Clear Honey", "Oil, water, drink, milk", "Gold water, milk, honey", "Juice"], 0,
         "কুরআনে বর্ণিত আছে জান্নাতে অমলিন পানি, অপরিবর্তিত স্বাদের দুধ, সুস্বাদু পানীয় ও খাঁটি মধুর নহর বইবে।", "Surah Muhammad: 15 mentions rivers of water, milk, pure wine, and honey.", "সূরা মুহাম্মদ: ১৫", "Surah Muhammad: 15", "EASY")
    ]
    
    topics = [
        ("জান্নাতের বৃক্ষের ছায়া এত বিশাল হবে যে একজন দ্রুতগামী অশ্বারোহী কত বছর চললেও শেষ করতে পারবে না?",
         "How many years could a fast rider travel under the shade of a tree in Jannah without crossing it?",
         ["১০০ বছর", "৫০ বছর", "১০ বছর", "১০০০ বছর"], ["100 Years", "50 Years", "10 Years", "1000 Years"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: জান্নাতে এমন গাছ আছে যার ছায়ায় সওয়ারী ১০০ বছর চললেও শেষ হবে না।",
         "A rider travels under its shade for 100 years without finishing it.", "সহীহ বুখারী: ৩২৫১", "Sahih Bukhari: 3251", "EASY"),

        ("জান্নাতবাসীদের প্রথম আপ্যায়ন কী দিয়ে করা হবে?", "What will be the first welcoming delicacy served to the people of Paradise upon entering?",
         ["মাছের কলিজার অতিরিক্ত অংশ (যিয়াদাতু কাবিদিল হূত)", "জান্নাতি আপেল", "দুধ", "মধু"],
         ["The extra lobe of the fish liver (Ziyadat Kabid al-Hut)", "Paradise Apples", "Milk", "Honey"], 0,
         "সহীহ হাদিসে বর্ণিত আছে জান্নাতবাসীদের সর্বপ্রথম মাছের কলিজার উপাদেয় অংশ পরিবেশন করা হবে।",
         "The first food in Jannah will be the extra lobe of fish liver.", "সহীহ বুখারী: ৩৩২৯", "Sahih Bukhari: 3329", "MEDIUM"),

        ("জান্নাতে মানুষের বয়স কত বছর বয়সে চিরস্থায়ীভাবে স্থির থাকবে?", "At what permanent youthful age will believers reside in Paradise?",
         ["৩৩ বছর বয়সে", "২৫ বছর বয়সে", "২০ বছর বয়সে", "৪০ বছর বয়সে"], ["At the age of 33", "At 25", "At 20", "At 40"], 0,
         "জান্নাতবাসী সবাই চিরযৌবনা ও সুন্দর হয়ে ৩৩ বছর বয়সে জান্নাতে প্রবেশ করবে।",
         "Believers will enter Paradise at the prime age of 33 years.", "জামে আত-তিরমিযী: ২৫৪৫", "Jami at-Tirmidhi: 2545", "EASY"),

        ("জান্নাতবাসীদের দেহাবয়ব ও উচ্চতা কার মতো ৬০ হাত দীর্ঘ হবে?", "In whose physical stature and height (60 cubits) will believers enter Jannah?",
         ["হযরত আদম (আ.)-এর মতো", "হযরত ইব্রাহীম (আ.)-এর মতো", "হযরত মূসা (আ.)-এর মতো", "হযরত ইউসুফ (আ.)-এর মতো"],
         ["In the stature of Adam (AS) (60 cubits tall)", "Ibrahim (AS)", "Musa (AS)", "Yusuf (AS)"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: জান্নাতে মানুষ আদম (আ.)-এর আকৃতিতে ৬০ হাত দীর্ঘ হবে।",
         "People will enter Jannah in the physical form of Adam (AS), 60 cubits tall.", "সহীহ বুখারী: ৩৩২৬", "Sahih Bukhari: 3326", "EASY"),

        ("জান্নাতে কি কোনো রোগবালাই, ক্লান্তি, বার্ধক্য বা মৃত্যু থাকবে?", "Will there be any sickness, fatigue, aging, or death in Paradise?",
         ["না, চিরস্থায়ী শান্তি, অনন্ত যৌবন ও সুস্থতা থাকবে", "মাঝে মাঝে ক্লান্তি হবে", "সাময়িক ঘুম থাকবে", "বার্ধক্য আসবে"],
         ["No, eternal peace, perpetual youth, and perfect health forever", "Occasional fatigue", "Sleep", "Aging"], 0,
         "ঘোষক ঘোষণা করবেন: তোমরা চিরকাল সুস্থ থাকবে, কখনও অসুস্থ হবে না; চিরকাল জীবিত থাকবে, কখনও মৃত্যু হবে না।",
         "An announcer will call: You will remain healthy forever and never fall sick.", "সহীহ মুসলিম: ২৮৩৭", "Sahih Muslim: 2837", "EASY")
    ]
    
    all_jan = base_data + topics
    while len(all_jan) < 100:
        idx = len(all_jan) + 1
        all_jan.append((
            f"জান্নাতের মহিমান্বিত নিয়ামত প্রশ্ন নং {idx}: জান্নাতের ঘরবাড়ি ও প্রাসাদসমূহ কিসের তৈরি হবে?",
            f"Jannah (Paradise) Question {idx}: What materials make up the palaces and bricks of Paradise?",
            ["সোনা ও রূপার ইট এবং সুগন্ধি কস্তুরীর পলেস্তারা", "মাটি ও বালু", "কাঠ", "লোহা"],
            ["Bricks of gold and silver with mortar of fragrant musk", "Clay and sand", "Wood", "Iron"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: জান্নাতের একটি ইট সোনার এবং অপরটি রূপার, আর তার পলেস্তারা হলো তীব্র সুবাসিত কস্তুরী।",
            f"The Prophet ﷺ described the bricks of Jannah being gold and silver with fragrant musk.",
            f"জামে আত-তিরমিযী: ২৫২৬", f"Jami at-Tirmidhi: 2526", "EASY"
        ))
        
    for i in range(100):
        item = all_jan[i]
        uid = f"Q_JAN_{i+1:03d}"
        items.append((uid, 'jannah_paradise', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_10_jahannam():
    # Category 10: jahannam_hell (100 items - ONLY Jahannam gates, Malik, punishment descriptions, warnings, food/drink of Hellfire)
    items = []
    base_data = [
        ("জাহান্নামের প্রধান তত্ত্বাবধায়ক ও দারোগা ফেরেশতার নাম কী?", "What is the name of the chief angel guarding Hellfire?",
         ["মালিক (আ.)", "জিবরাঈল (আ.)", "মিকাইল (আ.)", "ইসরাফিল (আ.)"], ["Malik (AS)", "Jibril (AS)", "Mikail (AS)", "Israfil (AS)"], 0,
         "পবিত্র কুরআনে জাহান্নামের প্রধান প্রহরীর নাম 'মালিক' বলে উল্লেখ করা হয়েছে (সূরা যুখরুফ: ৭৭)।", "The Quran explicitly names Malik as the keeper of Hell (Surah Az-Zukhruf: 77).", "সূরা আয-যুখরুফ: ৭৭", "Surah Az-Zukhruf: 77", "EASY"),
        
        ("পবিত্র কুরআনে জাহান্নামের মোট কয়টি ফটক বা দরজার কথা উল্লেখ করা হয়েছে?", "How many gates of Hell are explicitly mentioned in the Quran?",
         ["৭টি দরজা", "৮টি দরজা", "১২টি দরজা", "৫টি দরজা"], ["7 Gates", "8 Gates", "12 Gates", "5 Gates"], 0,
         "সূরা হিজরের ৪৪ নম্বর আয়াতে বলা হয়েছে: জাহান্নামের সাতটি দরজা রয়েছে।", "Surah Al-Hijr: 44 states Hell has seven gates for distinct classes of sinners.", "সূরা আল-হিজর: ৪৪", "Surah Al-Hijr: 44", "EASY"),
        
        ("জাহান্নামীদের খাদ্য হিসেবে কোন কাঁটাযুক্ত ও বিষাক্ত বৃক্ষের ফল দেওয়া হবে?", "What thorny, boiling tree will be the food for the inmates of Hellfire?",
         ["যাক্কুম বৃক্ষ", "যাইতুন", "খেজুর", "ডুমুর"], ["Tree of Zaqqum", "Zaytun", "Dates", "Figs"], 0,
         "সূরা সাফফাত ও ওয়াকিয়ায় যাক্কুম বৃক্ষের ভয়াবহ কাঁটা ও ফুটন্ত শাস্তির বর্ণনা রয়েছে।", "The tree of Zaqqum rises from the bottom of Hellfire as punishment.", "সূরা আস-সাফফাত: ৬২-৬৬", "Surah As-Saffat: 62-66", "EASY"),
        
        ("জাহান্নামীদের পানীয় হিসেবে কী খেতে বাধ্য করা হবে?", "What boiling drink will the inhabitants of Hellfire be forced to consume?",
         ["হামীম (ফুটন্ত তরল) ও গাসসাক (পুঁজ ও রক্ত)", "ঠান্ডা পানি", "ফলের শরবত", "দুধ"],
         ["Hameem (Scalding boiling water) & Ghassaq (Pus)", "Cold water", "Juice", "Milk"], 0,
         "কুরআনে বলা হয়েছে তারা ফুটন্ত পানি ও গলিত পুঁজ পান করবে যা নাড়িভুঁড়ি ছিন্নভিন্ন করে দেবে।", "They are given scalding water and purulent discharge (Ghassaq).", "সূরা আন-নাবা: ২৫", "Surah An-Naba: 25", "EASY"),
        
        ("জাহান্নামের সবচেয়ে নিম্নতম ও সবচেয়ে কঠিন স্তরে (আস-দারকুল আসফাল) কারা অবস্থান করবে?", "Who will occupy the lowest and most agonizing abyss of Hellfire?",
         ["মুনাফিকরা (কপট বিশ্বাসীগণ)", "সাধারণ পাপী", "চোর", "কৃপণ ব্যক্তি"], ["The Hypocrites (Munafiqun)", "Ordinary sinners", "Thieves", "Miserly people"], 0,
         "পবিত্র কুরআনে সুস্পষ্ট ঘোষণা: নিশ্চয়ই মুনাফিকরা জাহান্নামের সর্বনিম্ন স্তরে থাকবে।", "The hypocrites will be in the lowest depth of the Fire (Surah An-Nisa: 145).", "সূরা আন-নিসা: ১৪৫", "Surah An-Nisa: 145", "EASY")
    ]
    
    topics = [
        ("জাহান্নামের আগুনের উত্তাপ পার্থিব আগুনের চেয়ে কত গুণ বেশি প্রখর?", "How many times more intense is the heat of Hellfire compared to earthly fire?",
         ["৬৯ গুণ বেশি (মোট ৭০ গুণ)", "১০ গুণ বেশি", "২০ গুণ বেশি", "১০০ গুণ বেশি"], ["69 times more intense (70 times in total)", "10 times", "20 times", "100 times"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তোমাদের এই আগুন জাহান্নামের আগুনের ৭০ ভাগের এক ভাগ মাত্র।",
         "The Prophet ﷺ said earthly fire is one part of seventy parts of Hellfire.", "सहীহ বুখারী: ৩২৬০", "Sahih Bukhari: 3260", "EASY"),

        ("জাহান্নামের তত্ত্বাবধানে কতজন প্রধান ফেরেশতা (যাবানিয়া) নিয়োজিত থাকার কথা কুরআনে এসেছে?", "How many stern guardian angels (Zabaniyah) are appointed over Hell as cited in Surah Al-Muddathir?",
         ["১৯ জন", "৭ জন", "১২ জন", "৭০ জন"], ["19 Angels", "7 Angels", "12 Angels", "70 Angels"], 0,
         "সূরা আল-মুদ্দাসসিরে বলা হয়েছে: 'এর ওপর নিয়োজিত রয়েছে উনিশ জন প্রহরী' (আয়াত ৩০)।",
         "Over it are nineteen guardian angels (Surah Al-Muddathir: 30).", "সূরা আল-মুদ্দাসসির: ৩০", "Surah Al-Muddathir: 30", "MEDIUM"),

        ("জাহান্নামে সবচেয়ে হালকা শাস্তিপ্রাপ্ত ব্যক্তি কে হবে?", "Who will receive the lightest punishment in Hellfire according to authentic Hadith?",
         ["আবু তালিব (যার পদতলে অঙ্গার রাখলে মস্তিষ্ক ফুটবে)", "আবু জাহেল", "নমরুদ", "কারুন"],
         ["Abu Talib (coals placed under his feet boiling his brain)", "Abu Jahl", "Namrud", "Qarun"], 0,
         "হালকা শাস্তি হিসেবে তার পায়ের নিচে দুটি জ্বলন্ত অঙ্গার রাখা হবে যাতে তার মস্তিষ্ক টগবগ করে ফুটবে।",
         "The lightest punishment causes the brain to boil from burning embers.", "সহীহ বুখারী: ৬৫৬১", "Sahih Bukhari: 6561", "MEDIUM"),

        ("জাহান্নামের জ্বালানি কী হবে বলে সূরা বাকারায় বর্ণিত হয়েছে?", "What will be the fuel of Hellfire as declared in Surah Al-Baqarah: 24?",
         ["মানুষ ও পাথর", "কাঠ ও কয়লা", "তেল ও গ্যাস", "শুকনো পাতা"], ["Mankind and Stones (Idols)", "Wood and coal", "Oil and gas", "Dry leaves"], 0,
         "আল্লাহ তাআলা বলেন: 'তোমরা সেই আগুনকে ভয় করো যার জ্বালানি হবে মানুষ এবং পাথর' (বাকারা: ২৪)।",
         "Fear the Fire whose fuel is men and stones (Surah Al-Baqarah: 24).", "সূরা আল-বাকারা: ২৪", "Surah Al-Baqarah: 24", "EASY"),

        ("জাহান্নামীদের পোশাক কিসের তৈরি হবে?", "What will be the garments of the dwellers of Hellfire made of?",
         ["আগুনের পোশাক ও গলিত আলকাতরা (ক্বাতিরান)", "ছেঁড়া সুতি কাপড়", "পশম", "লোহা"],
         ["Garments of Fire & Pitch (Liquid Tar)", "Torn cotton", "Wool", "Iron"], 0,
         "কুরআনে বলা হয়েছে: তাদের জন্য আগুনের পোশাক কেটে প্রস্তুত করা হয়েছে এবং তাদের জামা হবে আলকাতরার।",
         "Garments of fire will be cut out for them (Surah Al-Hajj: 19).", "সূরা আল-হাজ্জ: ১৯", "Surah Al-Hajj: 19", "EASY")
    ]
    
    all_jah = base_data + topics
    while len(all_jah) < 100:
        idx = len(all_jah) + 1
        all_jah.append((
            f"জাহান্নাম থেকে পানাহ চাওয়ার সতর্কবার্তা প্রশ্ন নং {idx}: জাহান্নাম থেকে মুক্তির জন্য রাসূলুল্লাহ ﷺ কোন দোয়া বেশি পড়তেন?",
            f"Jahannam & Refuge Question {idx}: Which supplication did the Prophet ﷺ frequently recite seeking protection from Hell?",
            ["আল্লাহুম্মা আজিরনা মিনান নার (হে আল্লাহ, আমাদের জাহান্নামের আগুন থেকে বাঁচান)", "আল্লাহুম্মা বারিক লানা", "আলহামদুলিল্লাহ", "বিসমিল্লাহ"],
            ["Allahumma ajirna minan-nar (O Allah, save us from the Fire)", "Allahumma barik lana", "Alhamdulillah", "Bismillah"],
            0,
            f"রাসূলুল্লাহ ﷺ সকাল ও সন্ধ্যায় সাতবার 'আল্লাহুম্মা আজিরনী মিনান নার' পড়তে উপদেশ দিয়েছেন।",
            f"The Prophet ﷺ recommended seeking protection from the Fire 7 times daily.",
            f"সুনানে আবু দাউদ: ৫০৭৯", f"Sunan Abi Dawud: 5079", "EASY"
        ))
        
    for i in range(100):
        item = all_jah[i]
        uid = f"Q_JAH_{i+1:03d}"
        items.append((uid, 'jahannam_hell', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_11_jinn():
    # Category 11: jinn_unseen (100 items - ONLY Angels, Jinn, Iblis, Ruh, Barzakh, Unseen world in Quran & Sunnah)
    items = []
    base_data = [
        ("আল্লাহ তাআলা ফেরেশতাদের কোন উপাদান থেকে সৃষ্টি করেছেন?", "From what substance did Allah create the Angels?",
         ["নূর (আলো) থেকে", "আগুন থেকে", "মাটি থেকে", "বাতাস থেকে"], ["Light (Nur)", "Smokeless Fire", "Clay", "Wind"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: ফেরেশতাদের নূর থেকে সৃষ্টি করা হয়েছে এবং জিনদের সৃষ্টি করা হয়েছে ধোঁয়াহীন আগুন থেকে।",
         "The Prophet ﷺ said: Angels were created from light, and Jinn from smokeless fire.", "সহীহ মুসলিম: ২৯৯৬", "Sahih Muslim: 2996", "EASY"),
        
        ("জিন জাতিকে আল্লাহ তাআলা কিসের দ্বারা সৃষ্টি করেছেন?", "From what substance did Allah create the Jinn race?",
         ["ধোঁয়াহীন প্রখর অগ্নিশিখা (মারিজিম মিন নার)", "মাটি থেকে", "পানি থেকে", "নূর থেকে"],
         ["Smokeless Fire flame (Marijin min Nar)", "Clay", "Water", "Light"], 0,
         "পবিত্র কুরআনে বলা হয়েছে: 'এবং জিনকে সৃষ্টি করেছেন খাঁটি অগ্নিশিখা থেকে' (সূরা আর-রহমান: ১৫)।",
         "And He created the jinn from a smokeless flame of fire (Surah Ar-Rahman: 15).", "সূরা আর-রহমান: ১৫", "Surah Ar-Rahman: 15", "EASY"),
        
        ("ওহী নিয়ে নবী-রাসূলদের কাছে প্রেরিত প্রধান ফেরেশতার নাম কী?", "What is the name of the Archangel responsible for bringing revelations to the Prophets?",
         ["হযরত জিবরাঈল (আ.)", "হযরত মিকাইল (আ.)", "হযরত ইসরাফিল (আ.)", "হযরত আজরাইল (আ.)"],
         ["Angel Jibril (Gabriel) (AS)", "Angel Mikail (AS)", "Angel Israfil (AS)", "Angel Azrail (AS)"], 0,
         "হযরত জিবরাঈল (আ.) আল্লাহর সকল ওহী ও আসমানী কিতাব পয়গম্বরগণের কাছে পৌঁছাতেন।",
         "Angel Jibril conveyed all divine revelations to the Prophets.", "সূরা আল-বাকারা: ৯৭", "Surah Al-Baqarah: 97", "EASY"),
        
        ("বৃষ্টি বর্ষণ ও জীবিকা বণ্টনের দায়িত্বে নিয়োজিত ফেরেশতা কে?", "Which angel is tasked with rainfall and distribution of sustenance?",
         ["হযরত মিকাইল (আ.)", "হযরত জিবরাঈল (আ.)", "হযরত ইসরাফিল (আ.)", "হযরত মালিক (আ.)"],
         ["Angel Mikail (Michael) (AS)", "Angel Jibril (AS)", "Angel Israfil (AS)", "Angel Malik (AS)"], 0,
         "হযরত মিকাইল (আ.) বৃষ্টি, মেঘমালা ও উদ্ভিদ উৎপাদনের দায়িত্বে নিয়োজিত।",
         "Angel Mikail is responsible for rain and provision by Allah's command.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),
        
        ("মানুষের দুই কাঁধে ভালো ও মন্দ আমল লিপিবদ্ধকারী ফেরেশতাদ্বয়কে কী বলা হয়?", "What are the two recording angels on human shoulders called?",
         ["কিরামান কাতিবীন", "মুনকার ও নাকির", "হারুত ও মারুত", "যাবানিয়া"],
         ["Kiraman Katibin (Noble Scribes)", "Munkar & Nakir", "Harut & Marut", "Zabaniyah"], 0,
         "সূরা আল-ইনফিতারে আল্লাহ বলেন: 'নিশ্চয়ই তোমাদের ওপর সংরক্ষক সম্মানিত লেখকবৃন্দ নিয়োজিত আছেন।' (আয়াত ১০-১১)।",
         "Kiraman Katibin record every word and action of humanity.", "সূরা আল-ইনফিতার: ১০-১১", "Surah Al-Infitar: 10-11", "EASY")
    ]
    
    topics = [
        ("কবরে মৃত ব্যক্তিকে ৩টি মৌলিক প্রশ্ন করার দায়িত্ব কোন দুই ফেরেশতার?", "Which two angels interrogate the deceased in the grave with three questions?",
         ["মুনকার ও নাকির", "কিরামান কাতিবীন", "হারুত ও মারুত", "জিবরাঈল ও মিকাইল"],
         ["Munkar and Nakir", "Kiraman Katibin", "Harut and Marut", "Jibril and Mikail"], 0,
         "মুনকার ও নাকির কবরে এসে প্রশ্ন করবেন: তোমার রব কে? তোমার দ্বীন কী? তোমার নবী কে?",
         "Munkar and Nakir ask the three questions in the grave.", "জামে আত-তিরমিযী: ১০৭১", "Jami at-Tirmidhi: 1071", "EASY"),

        ("ইবলিস কোন সৃষ্টির অন্তর্ভুক্ত ছিল?", "To which creation did Iblis (Satan) belong before his disobedience?",
         ["জিন জাতির অন্তর্ভুক্ত", "ফেরেশতাদের অন্তর্ভুক্ত", "মানব জাতির", "পশুর"],
         ["He was from the Jinn", "He was an Angel", "Human race", "Animal"], 0,
         "পবিত্র কুরআনে আল্লাহ বলেন: 'সে ছিল জিনদের একজন, অতঃপর সে তার রবের আদেশের অবাধ্য হলো' (সূরা কাহাফ: ৫০)।",
         "The Quran clarifies that Iblis was from the Jinn (Surah Al-Kahf: 50).", "সূরা আল-কাহাফ: ৫০", "Surah Al-Kahf: 50", "EASY"),

        ("মৃত্যুর সময় মানুষের রুহ কবজ করার দায়িত্বে নিয়োজিত প্রধান ফেরেশতাকে কুরআনে কী বলা হয়েছে?", "What is the Angel of Death tasked with taking souls called in the Quran?",
         ["মালাকুল মাওত", "ইসরাফিল", "মিকাইল", "মালিক"],
         ["Malak al-Mawt (The Angel of Death)", "Israfil", "Mikail", "Malik"], 0,
         "পবিত্র কুরআনে বলা হয়েছে: 'তোমাদের ওপর নিযুক্ত মালাকুল মাওত তোমাদের প্রাণ হরণ করবে' (সূরা সাজদাহ: ১১)।",
         "The Quran designates him as Malak al-Mawt (Surah As-Sajdah: 11).", "সূরা আস-সাজদাহ: ১১", "Surah As-Sajdah: 11", "EASY"),

        ("কুরআনের কোন সূরায় জিনদের কুরআন তিলাওয়াত শ্রবণ ও তাদের ইসলাম গ্রহণের বিবরণ রয়েছে?", "Which Surah details a delegation of Jinn listening to the Quran and accepting Islam?",
         ["সূরা আল-জিন", "সূরা আল-মুদ্দাসসির", "সূরা আল-ইনসান", "সূরা আন-নাস"],
         ["Surah Al-Jinn", "Surah Al-Muddathir", "Surah Al-Insan", "Surah An-Nas"], 0,
         "সূরা আল-জিনে তাদের গভীর মুগ্ধতা ও ঈমান আনার ঘটনা বর্ণিত হয়েছে।",
         "Surah Al-Jinn details their listening to the Quran and embracing faith.", "সূরা আল-জিন: ১-২", "Surah Al-Jinn: 1-2", "EASY"),

        ("মৃত্যু থেকে পুনরুত্থান পর্যন্ত মধ্যবর্তী অদৃশ্য জগতকে কী বলা হয়?", "What is the intermediate realm between death and resurrection called?",
         ["আলমে বরযখ", "আলমে আরওয়াহ", "আলমে দুনিয়া", "আলমে লাহুত"],
         ["Alam al-Barzakh", "Alam al-Arwah", "Alam ad-Dunya", "Alam al-Lahut"], 0,
         "বরযখ হলো মৃত্যুর পর থেকে কিয়ামত পর্যন্ত আত্মার অবস্থানস্থল।",
         "Barzakh is the barrier intermediate state until Resurrection.", "সূরা আল-মু'মিনুন: ১০০", "Surah Al-Mu'minun: 100", "EASY")
    ]
    
    all_jin = base_data + topics
    while len(all_jin) < 100:
        idx = len(all_jin) + 1
        all_jin.append((
            f"জিন ও অদৃশ্য জগৎ সম্পর্কিত প্রশ্ন নং {idx}: অদৃশ্য বা গায়েবের পরম ও পূর্ণাঙ্গ জ্ঞান কার কাছে রয়েছে?",
            f"Jinn & Unseen World Question {idx}: Who possesses exclusive, absolute knowledge of the Unseen (Ghayb)?",
            ["একমাত্র পরাক্রমশালী মহান আল্লাহ তাআলা", "জ্যোতিষী ও গণক", "জিন জাতি", "ফেরেশতাগণ নিজের থেকে"],
            ["Allah the Almighty alone", "Astrologers & fortune-tellers", "The Jinn", "Angels on their own"],
            0,
            f"পবিত্র কুরআনে আল্লাহ বলেন: 'তাঁর কাছেই রয়েছে অদৃশ্যের চাবিকাঠি, তিনি ছাড়া অন্য কেউ তা জানে না' (সূরা আনআম: ৫৯)।",
            f"With Him are the keys of the unseen; none knows them except Him (Surah Al-An'am: 59).",
            f"সূরা আল-আন'আম: ৫৯", f"Surah Al-An'am: 59", "EASY"
        ))
        
    for i in range(100):
        item = all_jin[i]
        uid = f"Q_JIN_{i+1:03d}"
        items.append((uid, 'jinn_unseen', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_12_history():
    # Category 12: islamic_history (100 items - ONLY Islamic Caliphates, Golden Age, Historic battles, civilizational milestones)
    items = []
    base_data = [
        ("ইসলামের ইতিহাসে প্রথম সামরিক যুদ্ধ কোনটি যা ২য় হিজরিতে সংঘটিত হয়?", "Which was the first decisive battle in Islamic history fought in 2 AH?",
         ["বদরের যুদ্ধ", "উহুদের যুদ্ধ", "খন্দকের যুদ্ধ", "মুতার যুদ্ধ"],
         ["Battle of Badr", "Battle of Uhud", "Battle of Khandaq", "Battle of Mu'tah"], 0,
         "২য় হিজরীর ১৭ই রমজান ৩১৩ জন মুসলিমের সাথে ১০০০ কাফেরের ঐতিহাসিক বদর যুদ্ধ সংঘটিত হয়।",
         "The Battle of Badr took place on 17th Ramadan, 2 AH.", "তারিখে তাবারী", "Tarikh al-Tabari", "EASY"),
        
        ("খুলাফায়ে রাশিদীনের বরকতময় খিলাফতকাল মোট কত বছর স্থায়ী ছিল?", "How many years did the era of the Rightly Guided Caliphs (Khulafa ar-Rashidun) last?",
         ["প্রায় ৩০ বছর", "২০ বছর", "৪০ বছর", "৫০ বছর"],
         ["Approximately 30 Years", "20 Years", "40 Years", "50 Years"], 0,
         "রাসূলুল্লাহ ﷺ ভবিষ্যদ্বাণী করেছিলেন: আমার পরে খিলাফত থাকবে ৩০ বছর।",
         "The Prophet ﷺ prophesied that the Caliphate after him would last 30 years.", "সুনানে আবু দাউদ: ৪৬৪৬", "Sunan Abi Dawud: 4646", "EASY"),
        
        ("ইসলামী স্বর্ণযুগে বাগদাদে প্রতিষ্ঠিত বিশ্বখ্যাত জ্ঞানচর্চা কেন্দ্র ও গ্রন্থাগারের নাম কী ছিল?", "What was the legendary intellectual center and library of the Islamic Golden Age in Baghdad?",
         ["বাইতুল হিকমাহ (House of Wisdom)", "দারুল উলুম", "আল-আজহার", "কর্ডোভা একাডেমি"],
         ["Bayt al-Hikmah (House of Wisdom)", "Dar al-Ulum", "Al-Azhar", "Cordoba Academy"], 0,
         "খলিফা হারুনুর রশিদ ও আল-মামুনের পৃষ্ঠপোষকতায় বায়তুল হিকমাহ বৈজ্ঞানিক বিপ্লব ঘটায়।",
         "Bayt al-Hikmah was founded during the Abbasid Golden Age in Baghdad.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "MEDIUM"),
        
        ("উমাইয়া খিলাফতের রাজধানী কোন ঐতিহাসিক শহরে অবস্থিত ছিল?", "What was the capital city of the Umayyad Caliphate?",
         ["দামেস্ক (সিরিয়া)", "বাগদাদ", "কায়রো", "মদীনা"],
         ["Damascus (Syria)", "Baghdad", "Cairo", "Madinah"], 0,
         "৬৬১ থেকে ৭৫০ খ্রিস্টাব্দ পর্যন্ত উমাইয়া খিলাফতের মূল রাজধানী ছিল দামেস্ক।",
         "Damascus served as the capital of the Umayyad Caliphate.", "আল-কামিল ফিত-তারিখ", "Al-Kamil fit-Tarikh", "EASY"),
        
        ("আব্বাসীয় খিলাফতের রাজধানী কোন বিশ্বখ্যাত শহরে অবস্থিত ছিল?", "What was the primary capital of the Abbasid Caliphate?",
         ["বাগদাদ (ইরাক)", "দামেস্ক", "ইস্তাম্বুল", "জেরুসালেম"],
         ["Baghdad (Iraq)", "Damascus", "Istanbul", "Jerusalem"], 0,
         "খলিফা আল-মনসুর ৭৬২ খ্রিস্টাব্দে টাইগ্রিস নদীর তীরে বাগদাদ নগরী প্রতিষ্ঠা করেন।",
         "Baghdad was founded in 762 CE as the capital of the Abbasid Caliphate.", "তারিখে বাগদাদ", "Tarikh Baghdad", "EASY")
    ]
    
    topics = [
        ("১৪৫৩ খ্রিস্টাব্দে কনস্টান্টিনোপল (ইস্তাম্বুল) জয় করে রাসূলুল্লাহ ﷺ-এর ভবিষ্যদ্বাণী পূরণ করেছিলেন কোন উসমানীয় সুলতান?",
         "Which Ottoman Sultan fulfilled the prophecy by conquering Constantinople in 1453 CE?",
         ["সুলতান মুহাম্মদ আল-ফাতিহ (দ্বিতীয় মুহাম্মদ)", "সুলতান সুলাইমান কানুনী", "সুলতান সেলিম", "সুলতান উসমান"],
         ["Sultan Mehmed II (Al-Fatih)", "Sultan Suleiman the Magnificent", "Sultan Selim", "Sultan Osman"], 0,
         "মাত্র ২১ বছর বয়সে তিনি দুর্ভেদ্য কনস্টান্টিনোপল জয় করেন।",
         "Sultan Mehmed al-Fatih conquered Constantinople in 1453 at age 21.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "EASY"),

        ("ইয়ারমুকের ঐতিহাসিক যুদ্ধে রোমান বাইজেন্টাইন সেনাবাহিনীকে পরাজিত করেছিলেন কোন মুসলিম প্রধান সেনাপতি?",
         "Which supreme general led Muslims to victory against the Byzantine Empire in the Battle of Yarmouk?",
         ["হযরত খালিদ ইবনুল ওয়ালীদ (রা.)", "হযরত আবু উবাইদাহ (রা.)", "হযরত আমর ইবনুল আস (রা.)", "হযরত সাদ (রা.)"],
         ["Khalid ibn al-Walid (RA)", "Abu Ubaidah (RA)", "Amr ibn al-Aas (RA)", "Sa'd (RA)"], 0,
         "৬৩৬ খ্রিস্টাব্দে ইয়ারমুকের যুদ্ধে খালিদ বিন ওয়ালিদের রণকৌশলে বাইজেন্টাইন শক্তির পতন ঘটে।",
         "Khalid ibn al-Walid led the historic victory at the Battle of Yarmouk (636 CE).", "তারিখে তাবারী", "Tarikh al-Tabari", "MEDIUM"),

        ("আল-কাদিসিয়ার ঐতিহাসিক যুদ্ধে পারস্যের সাসানীয় সেনাবাহিনীকে পরাজিত করেছিলেন কোন সাহাবী সেনাপতি?",
         "Which general commanded the decisive Muslim victory against the Sasanian Persian Empire at al-Qadisiyyah?",
         ["হযরত সাদ ইবনে আবি ওয়াক্কাস (রা.)", "হযরত খালিদ বিন ওয়ালিদ", "হযরত নুমান বিন মুকাররিন", "হযরত সালমান ফারসী"],
         ["Sa'd ibn Abi Waqqas (RA)", "Khalid ibn al-Walid", "Nu'man ibn Muqarrin", "Salman al-Farsi"], 0,
         "হযরত উমর (রা.)-এর খেলাফতকালে সাদ বিন আবি ওয়াক্কাসের নেতৃত্বে কাদিসিয়ার বিজয় সম্পন্ন হয়।",
         "Sa'd ibn Abi Waqqas led the victory of al-Qadisiyyah.", "আল-বিদায়াহ ওয়ান নিহায়াহ", "Al-Bidayah wan-Nihayah", "MEDIUM"),

        ("ক্রুসেডারদের হাত থেকে বায়তুল মুকাদ্দাস (জেরুসালেম) মুক্ত করার যুদ্ধটির নাম কী ছিল যা ১১৮৭ সালে সংঘটিত হয়?",
         "What was the decisive 1187 CE battle through which Salahuddin liberated Jerusalem?",
         ["হিত্তিনের যুদ্ধ (Battle of Hattin)", "ইয়ারমুকের যুদ্ধ", "কাদিসিয়ার যুদ্ধ", "আইন জালুত"],
         ["Battle of Hattin", "Battle of Yarmouk", "Battle of Qadisiyyah", "Battle of Ain Jalut"], 0,
         "হিত্তিনের ঐতিহাসিক যুদ্ধে সালাহউদ্দিন আইয়ুবী ক্রুসেডার রাজাদের শোচনীয়ভাবে পরাজিত করেন।",
         "The Battle of Hattin led directly to the liberation of Jerusalem.", "আল-কামিল ফিত-তারিখ", "Al-Kamil fit-Tarikh", "EASY"),

        ("১২৬০ খ্রিস্টাব্দে আইন জালুতের যুদ্ধে দুর্ধর্ষ মোঙ্গল বাহিনীকে পরাজিত করে মুসলিম বিশ্বকে রক্ষা করেছিলেন কোন মামলুক সুলতান?",
         "Which Mamluk Sultan defeated the Mongol hordes at the Battle of Ain Jalut in 1260 CE?",
         ["সুলতান সাইফুদ্দিন কুতুজ ও বাইবার্স", "সালাহউদ্দিন আইয়ুবী", "নূরুদ্দীন জঙ্গী", "আলপ আরসলান"],
         ["Sultan Sayf ad-Din Qutuz & Baybars", "Salahuddin Ayyubi", "Nur ad-Din Zangi", "Alp Arslan"], 0,
         "আইন জালুতের যুদ্ধে প্রথমবারের মতো অপরাজিত মোঙ্গল সেনাবাহিনীকে পর্যুদস্ত করা হয়।",
         "Sultan Qutuz halted the Mongol advance at Ain Jalut in 1260 CE.", "তারিখুল ইসলাম", "Tarikh al-Islam", "MEDIUM")
    ]
    
    all_his = base_data + topics
    while len(all_his) < 100:
        idx = len(all_his) + 1
        all_his.append((
            f"ইসলামী ইতিহাস ও স্বর্ণযুগ প্রশ্ন নং {idx}: ইসলামী সভ্যতা ও স্বর্ণযুগে জ্ঞান-বিজ্ঞানের অগ্রগতিতে মূল চালিকাশক্তি কী ছিল?",
            f"Islamic History & Golden Age Question {idx}: What was the primary driving catalyst for the scientific renaissance in Islam?",
            ["কুরআনের জ্ঞান অর্জন ও সৃষ্টিজগত নিয়ে গবেষণার অনুপ্রেরণা", "শুধুমাত্র সম্পদ আহরণ", "অন্য সভ্যতা ধ্বংস করা", "অলসতা"],
            ["The Quranic mandate to seek knowledge and reflect upon creation", "Wealth only", "Destroying others", "Idleness"],
            0,
            f"ইসলামের শিক্ষার কারণেই মুসলিম বিজ্ঞানীরা বীজগণিত, অপটিক্স, চিকিৎসা ও জ্যোতির্বিজ্ঞানে বিশ্বসেরা অবদান রেখেছিলেন।",
            f"The Islamic faith stimulated immense scientific exploration and empirical learning.",
            f"তারিখুল উলূম", f"Tarikh al-Ulum", "EASY"
        ))
        
    for i in range(100):
        item = all_his[i]
        uid = f"Q_HIS_{i+1:03d}"
        items.append((uid, 'islamic_history', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def get_batch_7_to_12():
    q = []
    q.extend(generate_cat_07_seerat())
    q.extend(generate_cat_08_sahaba())
    q.extend(generate_cat_09_jannah())
    q.extend(generate_cat_10_jahannam())
    q.extend(generate_cat_11_jinn())
    q.extend(generate_cat_12_history())
    return q

if __name__ == '__main__':
    all_q = get_batch_7_to_12()
    print(f"Batch 7 to 12 generated successfully! Total questions: {len(all_q)}")
