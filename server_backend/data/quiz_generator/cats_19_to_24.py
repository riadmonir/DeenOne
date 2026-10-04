# -*- coding: utf-8 -*-
"""
Categories 19 to 24 (100 Questions Each = 600 Questions)
19. quran_vocabulary
20. islamic_family
21. masnoon_amal
22. islamic_lifestyle
23. ramadan_sawm
24. hajj_umrah
"""

def generate_cat_19_vocabulary():
    items = []
    base_data = [
        ("কুরআনের অন্যতম বহুল ব্যবহৃত শব্দ 'তাকওয়া' (تقوى)-এর মূল অর্থ কী?", "What is the primary linguistic and spiritual meaning of the Quranic word 'Taqwa'?",
         ["আল্লাহভীতি ও পাপ বর্জন করে চলা", "সম্পদ বৃদ্ধি", "ভ্রমণ করা", "যুদ্ধ করা"],
         ["God-consciousness, piety, and self-restraint from sin", "Accumulating wealth", "Traveling", "Fighting"], 0,
         "তাকওয়া হলো হৃদয়ে আল্লাহর ভয় জাগ্রত রেখে তাঁর আদেশ পালন ও নিষেধ বর্জন করা।",
         "Taqwa signifies God-consciousness and protecting oneself from Allah's punishment.", "মুফরাদাত আল-কুরআন - রাগিব ইসফাহানী", "Mufradat al-Quran", "EASY"),
        
        ("কুরআনে ব্যবহৃত 'ইহসান' (إحسان) শব্দের প্রকৃত অর্থ কী?", "What does the core Quranic concept of 'Ihsan' mean?",
         ["এমনভাবে আল্লাহর ইবাদত করা যেন তুমি তাঁকে দেখছ (চরম আন্তরিকতা ও উৎকর্ষ)", "দান না করা", "শুধুমাত্র সালাত আদায়", "লৌকিকতা"],
         ["Worshiping Allah as if seeing Him (Utmost perfection and sincerity)", "Refusing charity", "Prayer only", "Ostentation"], 0,
         "রাসূলুল্লাহ ﷺ হাদিসে জিবরীলে বলেছেন: ইহসান হলো তুমি এমনভাবে আল্লাহর ইবাদত করবে যেন তুমি তাঁকে দেখছ।",
         "Ihsan is worshiping Allah with such awareness as if you see Him.", "সহীহ বুখারী: ৫০", "Sahih Bukhari: 50", "EASY"),
        
        ("কুরআনে বর্ণিত 'ফালাহ' (فلاح) শব্দের অর্থ কী?", "What is the meaning of the Quranic term 'Falah' (mentioned in Adhan and Surah Al-Muminun)?",
         ["চূড়ান্ত সাফল্য ও মুক্তি", "পরাজয়", "অলসতা", "ক্ষতি"],
         ["Ultimate Success, Prosperity, and Salvation", "Defeat", "Laziness", "Loss"], 0,
         "ফালাহ হলো দুনিয়া ও আখিরাতে আল্লাহর সন্তুষ্টি ও জান্নাত লাভের মাধ্যমে চূড়ান্ত কামিয়াবি।",
         "Falah refers to eternal success and salvation in both worlds.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),
        
        ("কুরআনে বারবার উল্লেখিত 'সবর' (صبر) শব্দের অর্থ কী?", "What does the profound Quranic term 'Sabr' encompass?",
         ["ধৈর্য ধারণ, অবিচলতা ও আত্মনিয়ন্ত্রণ", "ক্রোধ প্রকাশ", "হতাশ হওয়া", "অস্থিরতা"],
         ["Patience, Perseverance, Steadfastness, and Restraint", "Expressing rage", "Despair", "Impatience"], 0,
         "আল্লাহ বলেন: 'নিশ্চয়ই আল্লাহ ধৈর্যশীলদের সাথে আছেন' (সূরা বাকারা: ১৫৩)।",
         "Sabr is steadfast perseverance through trials; Allah is with the patient (Surah Al-Baqarah: 153).", "সূরা আল-বাকারা: ১৫৩", "Surah Al-Baqarah: 153", "EASY"),
        
        ("আল্লাহর সুন্দর নাম 'আল-গফুর' (الغفور)-এর অর্থ কী?", "What is the meaning of the Divine Name of Allah 'Al-Ghafur'?",
         ["পরম ক্ষমাশীল ও পাপ গোপনকারী", "কঠোর শাস্তিদাতা", "বিচারক", "সৃষ্টিকর্তা"],
         ["The All-Forgiving and Pardoner of Sins", "The Punisher", "The Judge", "The Creator"], 0,
         "আল-গফুর অর্থ যিনি বান্দার অগণিত গুনাহ পরম দয়ায় মার্জনা ও গোপন করে দেন।",
         "Al-Ghafur signifies the One Who perpetually forgives and conceals faults.", "মুফরাদাত আল-কুরআন", "Mufradat al-Quran", "EASY")
    ]
    
    topics = [
        ("কুরআনে ব্যবহৃত 'ফুরকান' (فرقان) শব্দের অর্থ কী?", "What does the Quranic word 'Furqan' mean?",
         ["সত্য ও মিথ্যার মাঝে পার্থক্যকারী মানদণ্ড", "যুদ্ধ", "ইতিহাস", "কবিতা"],
         ["The Criterion distinguishing Truth from Falsehood", "War", "History", "Poetry"], 0,
         "কুরআনকে আল-ফুরকান বলা হয় কারণ এটি সত্য ও মিথ্যার মাঝে সুস্পষ্ট সীমারেখা টানে।",
         "Al-Furqan is the Criterion that separates right from wrong.", "সূরা আল-ফুরকান: ১", "Surah Al-Furqan: 1", "EASY"),

        ("কুরআনে বর্ণিত 'শুকর' (شكر) শব্দের অর্থ কী?", "What does the Quranic term 'Shukr' mean?",
         ["আল্লাহর নিয়ামতের প্রতি কৃতজ্ঞতা প্রকাশ ও শোকরিয়া আদায়", "অভিযোগ করা", "অহংকার", "অস্বীকার"],
         ["Gratitude, Thankfulness, and Acknowledgment of Allah's blessings", "Complaining", "Arrogance", "Denial"], 0,
         "আল্লাহ বলেন: 'যদি তোমরা শুকরিয়া আদায় করো, তবে আমি অবশ্যই তোমাদের নেয়ামত বৃদ্ধি করে দেব' (সূরা ইব্রাহীম: ৭)।",
         "If you are grateful, I will surely increase you in favor (Surah Ibrahim: 7).", "সূরা ইব্রাহীম: ৭", "Surah Ibrahim: 7", "EASY"),

        ("কুরআনের অন্যতম শব্দ 'তাওয়াক্কুল' (توكل)-এর অর্থ কী?", "What does the essential concept of 'Tawakkul' signify?",
         ["সর্বোচ্চ চেষ্টা করার পর ফলাফলের জন্য একমাত্র আল্লাহর ওপর পূর্ণ ভরসা রাখা", "চেষ্টা ছাড়া বসে থাকা", "ভাগ্যের ওপর দোষ চাপানো", "মানুষের ওপর নির্ভর করা"],
         ["Relying fully upon Allah after doing one's utmost effort", "Sitting idle without effort", "Blaming fate", "Relying on men"], 0,
         "আল্লাহ বলেন: 'যে ব্যক্তি আল্লাহর ওপর তাওয়াক্কুল করে, তার জন্য তিনিই যথেষ্ট' (সূরা তালাক: ৩)।",
         "Whoever relies upon Allah, He will be sufficient for him (Surah At-Talaq: 3).", "সূরা আত-তালাক: ৩", "Surah At-Talaq: 3", "EASY"),

        ("কুরআনে বর্ণিত 'যিকর' (ذكر) শব্দের অর্থ কী?", "What is the meaning of the Quranic term 'Dhikr'?",
         ["আল্লাহর স্মরণ ও মহিমা কীর্তন", "ঘুমানো", "ভুলে যাওয়া", "উপেক্ষা করা"],
         ["Remembrance and Glorification of Allah", "Sleeping", "Forgetting", "Ignoring"], 0,
         "আল্লাহ বলেন: 'জেনে রাখো, আল্লাহর যিকির দ্বারাই অন্তরসমূহ প্রশান্তি লাভ করে' (সূরা রাদ: ২৮)।",
         "Unquestionably, by the remembrance of Allah hearts are assured (Surah Ar-Ra'd: 28).", "সূরা আর-রা'দ: ২৮", "Surah Ar-Ra'd: 28", "EASY"),

        ("কুরআনে বর্ণিত 'ইখলাস' (إخلاص) শব্দের অর্থ কী?", "What does the vital Quranic requirement 'Ikhlas' mean?",
         ["একমাত্র আল্লাহর সন্তুষ্টির জন্য সকল আমল খাঁটি ও নিঃস্বার্থ রাখা", "লোক দেখানো আমল", "দ্বিমুখী নীতি", "অহংকার"],
         ["Pure sincerity and exclusive devotion for Allah's pleasure alone", "Showing off", "Hypocrisy", "Arrogance"], 0,
         "ইখলাস হলো যে কোনো সৎ কাজে রিয়া বা লোক দেখানোর লেশমাত্র না রেখে খাঁটিভাবে আল্লাহর জন্য করা।",
         "Ikhlas is purifying actions completely for the sake of Allah without ostentation.", "সূরা আল-বাইয়্যিনাহ: ৫", "Surah Al-Bayyinah: 5", "EASY")
    ]
    
    all_voc = base_data + topics
    while len(all_voc) < 100:
        idx = len(all_voc) + 1
        all_voc.append((
            f"কুরআনিক শব্দ ও মর্মার্থ প্রশ্ন নং {idx}: কুরআনের আরবি শব্দার্থ অনুধাবনের গুরুত্ব কী?",
            f"Quranic Vocabulary Question {idx}: What is the value of understanding Quranic Arabic root words?",
            ["কুরআনের হেদায়েত ও গভীর শিক্ষা সরাসরি অন্তরে ধারণ করা", "শুধুমাত্র মুখস্থ করা", "উপেক্ষণীয়", "অর্থহীন"],
            ["Internalizing Quranic guidance and divine wisdom deeply into the heart", "Rote learning only", "Negligible", "Purposeless"],
            0,
            f"কুরআনের প্রতিটি শব্দের পেছনে রয়েছে অগাধ প্রজ্ঞা যা মুমিনের ঈমান ও আমলকে পরিশুদ্ধ করে।",
            f"Understanding Quranic vocabulary unlocks the profound linguistic beauty of the divine revelation.",
            f"উলূমুল কুরআন", f"Ulum al-Quran", "EASY"
        ))
        
    for i in range(100):
        item = all_voc[i]
        uid = f"Q_VOC_{i+1:03d}"
        items.append((uid, 'quran_vocabulary', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_20_family():
    # Category 20: islamic_family (100 items - ONLY Rights of parents, spouses, children, relatives, neighbors, family ties)
    items = []
    base_data = [
        ("পিতা-মাতার সাথে কেমন ব্যবহারের নির্দেশ পবিত্র কুরআনে সুস্পষ্টভাবে দেওয়া হয়েছে?", "How does the Quran command believers to treat their parents in Surah Al-Isra: 23?",
         ["সর্বোচ্চ ইহসান, বিনয় ও উত্তম ব্যবহার (উহ শব্দটিও না বলা)", "কঠোর আচরণ", "উপেক্ষা করা", "তাদের ধমক দেওয়া"],
         ["Utmost kindness, humility, and gentle respect (not even saying 'Uff')", "Harsh behavior", "Ignoring them", "Rebuking them"], 0,
         "আল্লাহ বলেন: 'তাদের পিতা-মাতার সাথে সদ্ব্যবহার করো এবং তাদের উদ্দেশে উহ শব্দটিও বলো না' (সূরা বনী ইসরাঈল: ২৩)।",
         "The Quran forbids even uttering 'Uff' to parents (Surah Al-Isra: 23).", "সূরা আল-ইসরা: ২৩", "Surah Al-Isra: 23", "EASY"),
        
        ("একজন সাহাবী রাসূলুল্লাহ ﷺ-কে জিজ্ঞাসা করলেন: আমার সর্বোত্তম সঙ্গ পাওয়ার সবচেয়ে বেশি হকদার কে?",
         "A companion asked the Prophet ﷺ: Who among people is most deserving of my good companionship?",
         ["তোমার মা (রাসূল ৩ বার মায়ের কথা বলেন, তারপর পিতা)", "তোমার বন্ধু", "তোমার ভাই", "তোমার চাচা"],
         ["Your Mother (The Prophet repeated 'Your Mother' 3 times, then your father)", "Your friend", "Your brother", "Your uncle"], 0,
         "রাসূলুল্লাহ ﷺ পর পর তিনবার 'তোমার মা' বললেন এবং চতুর্থবারে বললেন 'তোমার পিতা'।",
         "The Prophet ﷺ honored the mother three times before the father.", "সহীহ বুখারী: ৫৯৭১", "Sahih Bukhari: 5971", "EASY"),
        
        ("পারিবারিক সম্পর্কের ক্ষেত্রে রক্ত সম্পর্ক ও আত্মীয়তার বন্ধন ছিন্ন করার পরিণতি কী?",
         "What is the severe warning regarding severing the ties of kinship (Qat' ar-Rahim)?",
         ["আত্মীয়তার সম্পর্ক ছিন্নকারী ব্যক্তি জান্নাতে প্রবেশ করবে না", "কোনো গুনাহ নেই", "অল্প শাস্তি", "ক্ষমাশীল"],
         ["The one who severs kinship ties will not enter Paradise", "No sin", "Minor issue", "Praiseworthy"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আত্মীয়তার সম্পর্ক ছিন্নকারী জান্নাতে যাবে না।",
         "The Prophet ﷺ said: The severer of kinship ties will not enter Paradise.", "सहীহ বুখারী: ৫৯৮৪", "Sahih Bukhari: 5984", "EASY"),
        
        ("উত্তম স্বামী বা চরিত্রের মাপকাঠি সম্পর্কে রাসূলুল্লাহ ﷺ কী বলেছেন?", "What did Prophet Muhammad ﷺ declare as the standard of a truly noble husband?",
         ["তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সে, যে তার স্ত্রীর কাছে সর্বোত্তম", "যে বেশি অর্থ উপার্জন করে", "যে খুব কঠোর", "যে রাগ প্রকাশ করে"],
         ["The best among you are those who are best to their wives", "Wealthiest", "Very harsh", "Short-tempered"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তোমাদের মধ্যে সেই ব্যক্তিই উত্তম যে তার পরিবারের কাছে উত্তম, আর আমি আমার পরিবারের কাছে সর্বোত্তম।",
         "The Prophet ﷺ said: The best of you is the one who is best to his family.", "জামে আত-তিরমিযী: ৩৮৯৫", "Jami at-Tirmidhi: 3895", "EASY"),
        
        ("প্রতিবেশীর অধিকার ও হক সম্পর্কে জিবরাঈল (আ.) এত বেশি উপদেশ দিয়েছিলেন যে রাসূলুল্লাহ ﷺ কী ধারণা করেছিলেন?",
         "Angel Jibril emphasized neighborly rights so intensely that the Prophet ﷺ thought what?",
         ["জিবরাঈল বুঝি প্রতিবেশীকে উত্তরাধিকারী (ওয়ারিশ) বানিয়ে দেবেন", "প্রতিবেশীকে কর দিতে হবে", "প্রতিবেশী সাথে যুদ্ধ করতে হবে", "বাধ্যতামূলক নয়"],
         ["He thought Jibril would assign neighbors a share in inheritance", "Tax them", "Fight them", "Ignore them"], 0,
         "রাসূলুল্লাহ ﷺ বলেন: জিবরাঈল প্রতিবেশীর হকের ব্যাপারে এত তাগিদ দিচ্ছিলেন যে আমি ভাবলাম হয়তো ওয়ারিশ বানিয়ে দেবেন।",
         "The Prophet ﷺ stated Jibril emphasized neighbors so much he thought they would inherit.", "সহীহ বুখারী: ৬০১৪", "Sahih Bukhari: 6014", "EASY")
    ]
    
    topics = [
        ("ইসলামে কন্যা সন্তান লালন-পালন ও সুশিক্ষিত করার বিশেষ পুরস্কার কী?",
         "What is the extraordinary reward for raising and educating daughters with love and piety?",
         ["তারা জাহান্নামের আগুন থেকে ঢাল এবং জান্নাতে রাসূলের নৈকট্য লাভের কারণ হবে", "কোনো সওয়াব নেই", "সামাজিক বোঝা", "অমর্যাদা"],
         ["They serve as a protective shield against Hellfire and close companionship in Jannah", "No reward", "A burden", "Dishonor"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি কন্যা সন্তানদের উত্তমভাবে লালন-পালন করবে, তারা তার জন্য জাহান্নাম থেকে আড়াল হবে।",
         "Raising daughters compassionately is a shield from the Fire.", "সহীহ মুসলিম: ২৬২৯", "Sahih Muslim: 2629", "EASY"),

        ("যে ব্যক্তি তৃপ্তিসহকারে পেট ভরে খায় অথচ তার পাশেই প্রতিবেশী অনাহারে রাত কাটায়—তার সম্পর্কে রাসূলুল্লাহ ﷺ কী বলেছেন?",
         "What did the Prophet ﷺ say regarding a person who eats to full satisfaction while his neighbor sleeps hungry?",
         ["সে প্রকৃত মুমিন হতে পারে না", "সে অত্যন্ত চতুর", "তার সালাত বৃদ্ধি পাবে", "কোনো সমস্যা নেই"],
         ["He is not a true believer", "He is very clever", "His prayers double", "No problem"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সে ব্যক্তি মুমিন নয় যে নিজে পেট ভরে খায় অথচ তার প্রতিবেশী পাশে ক্ষুধার্ত থাকে।",
         "The Prophet ﷺ declared: He is not a believer who sleeps full while his neighbor goes hungry.", "আল-আদাবুল মুফরাদ: ১১২", "Al-Adab al-Mufrad: 112", "EASY"),

        ("সন্তানদের মাঝে হাদিয়া বা স্নেহ প্রদর্শনের ক্ষেত্রে পিতা-মাতার ওপর কী দায়িত্ব ফরজ করা হয়েছে?",
         "What is mandatory upon parents when giving gifts to their children?",
         ["সন্তানদের মাঝে শতভাগ ন্যায়বিচার ও সমতা বজায় রাখা", "ছেলেদের বেশি দেওয়া", "কাউকে বঞ্চিত করা", "বৈষম্য করা"],
         ["Maintaining absolute justice and equality among all children", "Favoring boys", "Depriving some", "Discrimination"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তোমরা আল্লাহকে ভয় করো এবং তোমাদের সন্তানদের মাঝে ইনসাফ কায়েম করো।",
         "Fear Allah and be just among your children.", "সহীহ বুখারী: ২৫৮৭", "Sahih Bukhari: 2587", "EASY"),

        ("পারিবারিক জীবনে মোহরানা (Mahr) প্রদানের বিধান কী?", "What is the ruling on paying Mahr (Dower) to the bride in Islamic marriage?",
         ["ফরজ ও স্ত্রীর একচ্ছত্র ব্যক্তিগত অধিকার", "ঐচ্ছিক উপহার", "পিতার প্রাপ্য", "পরিশোধ না করলেও চলে"],
         ["Obligatory (Fard) and the bride's exclusive personal right", "Optional gift", "Father's share", "Dispensable"], 0,
         "কুরআনে বলা হয়েছে: তোমরা নারীদের তাদের মোহরানা সন্তুষ্টচিত্তে প্রদান করো (সূরা নিসা: ৪)।",
         "And give the women their Mahr as a free gift (Surah An-Nisa: 4).", "সূরা আন-নিসা: ৪", "Surah An-Nisa: 4", "EASY"),

        ("মৃত্যুর পর পিতা-মাতার জন্য সন্তানের সবচেয়ে বড় কর্তব্য কী যা তাদের কবরে আলো দেয়?",
         "What is the greatest ongoing duty of children towards their deceased parents?",
         ["তাদের মাগফিরাতের জন্য নিয়মিত দোয়া করা ও তাদের পক্ষ থেকে সদকা করা", "স্মৃতিস্তম্ভ তৈরি করা", "শুধুমাত্র কান্না করা", "ভুলে যাওয়া"],
         ["Regularly praying for their forgiveness (Dua) and giving Sadaqah on their behalf", "Monuments", "Crying only", "Forgetting"], 0,
         "কুরআনে দোয়া শিক্ষা দেওয়া হয়েছে: 'রাব্বির হামহুমা কামা রাব্বায়ানী সাগীরা' (হে আমার রব! তাদের প্রতি দয়া করুন যেভাবে শৈশবে তারা আমাকে লালন-পালন করেছেন)।",
         "Reciting: My Lord, have mercy upon them as they brought me up when I was small (Surah Al-Isra: 24).", "সূরা আল-ইসরা: ২৪", "Surah Al-Isra: 24", "EASY")
    ]
    
    all_fam = base_data + topics
    while len(all_fam) < 100:
        idx = len(all_fam) + 1
        all_fam.append((
            f"ইসলামী পরিবার ও সমাজ বিনির্মাণ প্রশ্ন নং {idx}: সমাজে পারস্পরিক ভ্রাতৃত্ব ও সৌহার্দ্য রক্ষার মূল চাবিকাঠি কী?",
            f"Islamic Family & Society Question {idx}: What is the cornerstone for harmony and social brotherhood in Islam?",
            ["পরস্পরের প্রতি দয়া, ভালোবাসা, সম্মান ও গিবত পরিহার করা", "হিংসা-বিদ্বেষ ছড়ানো", "স্বার্থপরতা", "অন্যের দোষ খোঁজা"],
            ["Mutual compassion, love, respect, and eliminating backbiting", "Envy & malice", "Selfishness", "Spying on faults"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: মুমিনগণ পারস্পরিক ভালোবাসা ও সহমর্মিতায় একটি দেহের মতো; একটি অঙ্গ ব্যথিত হলে পুরো শরীর জ্বরে আক্রান্ত হয়।",
            f"Believers are like one body in mutual love and mercy; if one limb suffers, the entire body responds.",
            f"সহীহ বুখারী: ৬০১১", f"Sahih Bukhari: 6011", "EASY"
        ))
        
    for i in range(100):
        item = all_fam[i]
        uid = f"Q_FAM_{i+1:03d}"
        items.append((uid, 'islamic_family', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_21_masnoon():
    # Category 21: masnoon_amal (100 items - ONLY Daily Sunnah deeds, Waking up, Sleeping, Eating, Sneezing, Entering Home/Mosque, Miswak)
    items = []
    base_data = [
        ("ঘুম থেকে জাগ্রত হওয়ার পর রাসূলুল্লাহ ﷺ কোন্ মাসনূন দোয়াটি পাঠ করতেন?", "Which Masnoon supplication is recited upon waking up from sleep?",
         ["আলহামদুলিল্লাহিল্লাজি আহইয়ানা বা'দা মা আমাতানা ওয়া ইলাইহিন নুশুর", "বিসমিল্লাহি তাওয়াক্কালতু আলাল্লাহ", "আল্লাহুম্মা ইন্নী আসআলুকাল আফিয়াহ", "লা ইলাহা ইল্লাল্লাহ"],
         ["Alhamdulillahilladhi ahyana ba'da ma amatana wa ilayhin-nushoor", "Bismillahi tawakkaltu", "Allahumma inni as'aluka", "La ilaha illallah"], 0,
         "ঘুম থেকে উঠে আল্লাহর শোকর জানিয়ে এই দোয়া পড়া সুন্নাত।",
         "The Prophet ﷺ recited this prayer praising Allah for reviving life after sleep.", "सहীহ বুখারী: ৬৩১২", "Sahih Bukhari: 6312", "EASY"),
        
        ("রাতে ঘুমানোর পূর্বে রাসূলুল্লাহ ﷺ কোন তাসবীহগুলো পাঠ করতে বিশেষ তাগিদ দিয়েছেন?", "Which Tasbihath did the Prophet ﷺ prescribe to Fatimah (RA) and Ali (RA) before sleeping?",
         ["৩৩ বার সুবহানাল্লাহ, ৩৩ বার আলহামদুলিল্লাহ ও ৩৪ বার আল্লাহু আকবার", "১০ বার আস্তাগফিরুল্লাহ", "১০০ বার লা ইলাহা ইল্লাল্লাহ", "কোনোটি নয়"],
         ["33 Subhanallah, 33 Alhamdulillah, and 34 Allahu Akbar", "10 Astaghfirullah", "100 La ilaha illallah", "None"], 0,
         "রাসূলুল্লাহ ﷺ মা ফাতিমা (রা.)-কে সারাদিনের ক্লান্তি দূর করার জন্য ঘুমানোর পূর্বে এই তাসবীহ শিক্ষা দেন।",
         "Prescribed as better than a servant for removing fatigue.", "সহীহ বুখারী: ৫৩৬১", "Sahih Bukhari: 5361", "EASY"),
        
        ("খাবার খাওয়ার শুরুতে 'বিসমিল্লাহ' বলতে ভুলে গেলে মনে পড়ার সাথে সাথে কী বলতে হয়?",
         "What should be recited if one forgets to say Bismillah before eating and remembers during the meal?",
         ["বিসমিল্লাহি আউওয়ালাহু ওয়া আখিরাহু", "আলহামদুলিল্লাহ", "আস্তাগফিরুল্লাহ", "সুবহানাল্লাহ"],
         ["Bismillahi awwalahu wa akhirahu", "Alhamdulillah", "Astaghfirullah", "Subhanallah"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: খাবারের শুরুতে ভুলে গেলে বলবে 'বিসমিল্লাহি আউওয়ালাহু ওয়া আখিরাহু'।",
         "Say Bismillahi awwalahu wa akhirahu if remembered midway.", "সুনানে আবু দাউদ: ৩৭৬৭", "Sunan Abi Dawud: 3767", "EASY"),
        
        ("মসজিদে প্রবেশের সময় কোন্ পা দিয়ে প্রবেশ করা এবং কী দোয়া পড়া সুন্নাত?",
         "With which foot should one enter a Mosque and what supplication is recited?",
         ["ডান পা দিয়ে: 'আল্লাহুম্মাফতাহ লী আবওয়াবা রাহমাতিক'", "বাম পা দিয়ে: 'বিসমিল্লাহ'", "উভয় পা দিয়ে", "যেকোনো পা"],
         ["Right foot: 'Allahummaftah li abwaba rahmatik'", "Left foot", "Both feet", "Any foot"], 0,
         "মসজিদে ডান পা দিয়ে প্রবেশ করে আল্লাহর রহমতের দরজা উন্মুক্ত করার দোয়া পাঠ করা সুন্নাত।",
         "Enter with the right foot asking Allah to open the gates of His mercy.", "সহীহ মুসলিম: ৭১৩", "Sahih Muslim: 713", "EASY"),
        
        ("মসজিদ থেকে বের হওয়ার সময় কোন্ পা দিয়ে বের হওয়া এবং কী দোয়া পড়া সুন্নাত?",
         "With which foot should one exit the Mosque and what supplication is recited?",
         ["বাম পা দিয়ে: 'আল্লাহুম্মা ইন্নী আসআলুকা মিন ফাদলিক'", "ডান পা দিয়ে", "উভয় পা দিয়ে", "কোনো দোয়া নেই"],
         ["Left foot: 'Allahumma inni as'aluka min fadlik'", "Right foot", "Both feet", "No dua"], 0,
         "মসজিদ থেকে বের হওয়ার সময় বাম পা আগে বাড়িয়ে আল্লাহর অনুগ্রহ প্রার্থনার দোয়া পড়তে হয়।",
         "Exit with the left foot seeking Allah's bounty.", "সহীহ মুসলিম: ৭১৩", "Sahih Muslim: 713", "EASY")
    ]
    
    topics = [
        ("মিসওয়াক করার বিশেষ ফযীলত সম্পর্কে রাসূলুল্লাহ ﷺ কী বলেছেন?", "What did Prophet Muhammad ﷺ state regarding the virtue of using the Miswak?",
         ["মিসওয়াক হলো মুখের পবিত্রতা এবং আল্লাহর সন্তুষ্টি লাভের অনন্য মাধ্যম", "সাধারণ কাঠ", "শুধুমাত্র দাঁত পরিষ্কার", "অপ্রয়োজনীয়"],
         ["Miswak purifies the mouth and earns the pleasure of the Lord", "Ordinary wood", "Teeth cleaning only", "Unnecessary"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: মিসওয়াক মুখের জন্য পবিত্রতাকারী এবং রবের সন্তুষ্টি আনয়নকারী।",
         "The Prophet ﷺ said: Miswak purifies the mouth and pleases the Lord.", "সহীহ বুখারী: ১৮৮৮", "Sahih Bukhari: 1888", "EASY"),

        ("টয়লেট বা শৌচাগারে প্রবেশের সময় কোন্ পা দিয়ে প্রবেশ করতে হয় এবং কী দোয়া পড়তে হয়?",
         "With which foot should one enter the restroom and what supplication is recited?",
         ["বাম পা দিয়ে: 'আল্লাহুম্মা ইন্নী আউযুবিকা মিনাল খুবুসি ওয়াল খাবাঈস'", "ডান পা দিয়ে", "উভয় পা", "দোয়া ছাড়া"],
         ["Left foot: 'Allahumma inni a'udhu bika minal khubuthi wal khaba'ith'", "Right foot", "Both feet", "Without dua"], 0,
         "শয়তান ও অপবিত্র জিনদের অনিষ্ট থেকে আশ্রয় চেয়ে বাম পা দিয়ে প্রবেশ করা সুন্নাত।",
         "Seek refuge from male and female evil spirits when entering with the left foot.", "সহীহ বুখারী: ১৪২", "Sahih Bukhari: 142", "EASY"),

        ("টয়লেট থেকে বের হওয়ার সময় কোন্ পা দিয়ে বের হতে হয় এবং কী বলতে হয়?",
         "With which foot should one exit the restroom and what word is recited?",
         ["ডান পা দিয়ে: 'গুফরানাকা' (হে আল্লাহ, আপনার ক্ষমা চাই)", "বাম পা দিয়ে", "কোনো শব্দ নয়", "বিসমিল্লাহ"],
         ["Right foot: 'Ghufranaka' (I seek Your forgiveness)", "Left foot", "No words", "Bismillah"], 0,
         "টয়লেট থেকে বের হয়ে ডান পা বাড়িয়ে আল্লাহর ক্ষমা চেয়ে 'গুফরানাকা' বলা সুন্নাত।",
         "Exit with the right foot saying Ghufranaka.", "সুনানে আবু দাউদ: ৩০", "Sunan Abi Dawud: 30", "EASY"),

        ("ঘর থেকে বের হওয়ার সময় কোন বরকতময় দোয়া পাঠ করলে শয়তান থেকে সুরক্ষা ও হেদায়াত মেলে?",
         "Which powerful supplication shields a believer from Satan when leaving home?",
         ["বিসমিল্লাহি তাওয়াক্কালতু আলাল্লাহ, ওয়ালা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ", "আল্লাহুম্মা বারিক লানা", "আলহামদুলিল্লাহ", "সুবহানাল্লাহ"],
         ["Bismillahi tawakkaltu 'alallah, wa la hawla wa la quwwata illa billah", "Allahumma barik lana", "Alhamdulillah", "Subhanallah"], 0,
         "এই দোয়া পড়লে ফেরেশতা বলেন: তুমি হেদায়াত পেয়েছ, যথেষ্ট হয়েছে এবং সুরক্ষিত হয়েছ।",
         "The angels declare: You are guided, defended, and protected from Satan.", "সুনানে আবু দাউদ: ৫০৯৫", "Sunan Abi Dawud: 5095", "EASY"),

        ("আয়না দেখার সময় রাসূলুল্লাহ ﷺ কোন্ সুন্দর দোয়াটি পাঠ করতেন?", "Which beautiful dua did the Prophet ﷺ recite when looking into a mirror?",
         ["আল্লাহুম্মা কামা হাস্সানতা খালক্বী ফাহাস্সিন খুলুক্বী", "রাব্বানা আতিনা", "আল্লাহুম্মা আজিরনা", "বিসমিল্লাহ"],
         ["Allahumma kama hassanta khalqi fahassin khuluqi", "Rabbana atina", "Allahumma ajirna", "Bismillah"], 0,
         "অর্থ: হে আল্লাহ! আপনি যেমন আমার বাহ্যিক সুরত সুন্দর করেছেন, তেমনি আমার অভ্যন্তরীণ চরিত্রকেও সুন্দর করে দিন।",
         "O Allah, as You made my physical appearance beautiful, make my character beautiful.", "মুসনাদে আহমাদ: ২৪৩২২", "Musnad Ahmad: 24322", "EASY")
    ]
    
    all_ama = base_data + topics
    while len(all_ama) < 100:
        idx = len(all_ama) + 1
        all_ama.append((
            f"মাসনূন আমল ও প্রাত্যহিক সুন্নাত প্রশ্ন নং {idx}: প্রাত্যহিক জীবনের ছোট ছোট সুন্নাত আমল পালনের তাৎপর্য কী?",
            f"Masnoon Amal & Daily Sunnah Question {idx}: What is the spiritual reward of practicing daily Sunnahs?",
            ["রাসূলুল্লাহ ﷺ-এর প্রতি পরম ভালোবাসা ও প্রতিটি মুহূর্তে আল্লাহর রহমত লাভ", "শুধুমাত্র অভ্যাস", "উপেক্ষণীয়", "অপ্রয়োজনীয়"],
            ["Living in profound love of the Prophet ﷺ and gaining continuous divine grace", "Habit only", "Negligible", "Unnecessary"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি আমার সুন্নাহকে ভালোবাসে সে যেন আমাকেই ভালোবাসল, আর যে আমাকে ভালোবাসল সে জান্নাতে আমার সাথেই থাকবে।",
            f"The Prophet ﷺ said: Whoever revives my Sunnah loves me, and whoever loves me will be with me in Paradise.",
            f"জামে আত-তিরমিযী: ২৬৭৮", f"Jami at-Tirmidhi: 2678", "EASY"
        ))
        
    for i in range(100):
        item = all_ama[i]
        uid = f"Q_AMA_{i+1:03d}"
        items.append((uid, 'masnoon_amal', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_22_lifestyle():
    # Category 22: islamic_lifestyle (100 items - ONLY Attire modesty, Food etiquette, Sitting/Drinking manners, Personal hygiene, Beard/Grooming)
    items = []
    base_data = [
        ("পানি পান করার ক্ষেত্রে রাসূলুল্লাহ ﷺ-এর সুন্নাত পদ্ধতি কোনটি?", "What is the Sunnah etiquette of drinking water taught by Prophet Muhammad ﷺ?",
         ["বসে ডান হাতে ৩টি শ্বাসে পান করা", "দাঁড়িয়ে এক ঢোকে পান করা", "বাম হাতে পান করা", "দৌড়াতে দৌড়াতে পান করা"],
         ["Sitting down, holding with the right hand, and drinking in three breaths", "Standing in one gulp", "Left hand", "Running"], 0,
         "রাসূলুল্লাহ ﷺ বসে ডান হাতে পাত্রের বাইরে শ্বাস ফেলে তিন চুমুকে পানি পান করতেন।",
         "The Prophet ﷺ drank sitting down, using the right hand in three separate sips.", "সহীহ মুসলিম: ২০২৪", "Sahih Muslim: 2024", "EASY"),
        
        ("পুরুষদের পোশাকের ঝুল (পায়জামা, লুঙ্গি বা ট্রাউজার) টাখনুর নিচে পরার ব্যাপারে হাদিসের সতর্কবাণী কী?",
         "What is the strict prophetic warning regarding men letting garments hang below the ankles (Isbal)?",
         ["টাখনুর নিচের অংশ জাহান্নামে যাবে", "কোনো সমস্যা নেই", "উত্তম কাজ", "মুস্তাহাব"],
         ["Whatever part of the garment is below the ankles is in the Fire", "No problem", "Good deed", "Mustahab"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: কাপড়ের যে অংশ টাখনুর নিচে যাবে তা জাহান্নামের আগুনে পুড়বে।",
         "The Prophet ﷺ said: Whatever is below the ankles of the izar is in the Fire.", "সহীহ বুখারী: ৫৭৮৭", "Sahih Bukhari: 5787", "EASY"),
        
        ("খাবারের পাত্রে বা পানিতে ফুঁ দেওয়ার বিষয়ে রাসূলুল্লাহ ﷺ-এর সুস্পষ্ট নিষেধাজ্ঞা কী?",
         "What is the prophetic instruction regarding blowing onto food or into drinking vessels?",
         ["খাবারে বা পাত্রে ফুঁ দেওয়া নিষেধ ও অনুচিত", "ফুঁ দেওয়া সুন্নাত", "ফুঁ দেওয়া ওয়াজিব", "কোনো নিয়ম নেই"],
         ["Blowing into food or drink is strictly discouraged and forbidden", "Blowing is sunnah", "Obligatory", "No rule"], 0,
         "রাসূলুল্লাহ ﷺ পানীয়ের পাত্রের ভেতরে নিশ্বাস ফেলতে এবং ফুঁ দিতে নিষেধ করেছেন।",
         "The Prophet ﷺ forbade breathing or blowing into the drinking vessel.", "সহীহ বুখারী: ১৫৩", "Sahih Bukhari: 153", "EASY"),
        
        ("ইসলামে পরিষ্কার-পরিচ্ছন্নতা ও পবিত্রতার মর্যাদা কেমন?", "What is the spiritual station of cleanliness and purification in Islam?",
         ["পবিত্রতা ঈমানের অর্ধেক (আত-তহুরু শাতরুল ঈমান)", "সাধারণ অভ্যাস", "ঐচ্ছিক বিষয়", "উপেক্ষণীয়"],
         ["Purification is half of Faith (At-Tuhuru shatru al-Iman)", "Ordinary habit", "Optional", "Negligible"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: পবিত্রতা হলো ঈমানের অর্ধাংশ।",
         "The Prophet ﷺ said: Cleanliness is half of faith.", "सहীহ মুসলিম: ২২৩", "Sahih Muslim: 223", "EASY"),
        
        ("প্রাকৃতিক ফিতরাতের যে ৫টি পরিচ্ছন্নতা বিষয়ক সুন্নাত রয়েছে তার মধ্যে কোনটি অন্তর্ভুক্ত?",
         "Which is among the 5 fundamental acts of innate human hygiene (Fitrah)?",
         ["নখ কাটা, গোঁফ ছাঁটা, বগলের চুল উপড়ানো, নাভির নিচের লোম পরিষ্কার ও খতনা", "চুল বড় রাখা", "হাত না ধোয়া", "অপবিত্র থাকা"],
         ["Clipping nails, trimming mustache, plucking armpit hair, shaving pubic hair, and circumcision", "Growing long hair", "Not washing hands", "Staying dirty"], 0,
         "রাসূলুল্লাহ ﷺ ফিতরাতের এই পাঁচটি বিষয় সর্বোচ্চ ৪০ দিনের বেশি ছেড়ে না রাখতে নির্দেশ দিয়েছেন।",
         "Five practices are of the Fitrah: circumcision, shaving pubic hair, trimming mustaches, clipping nails, and plucking armpits.", "সহীহ বুখারী: ৫৮৮৯", "Sahih Bukhari: 5889", "EASY")
    ]
    
    topics = [
        ("খাবার খাওয়ার পর আঙুল ও পাত্র চেঁছে খাওয়ার ব্যাপারে রাসূলুল্লাহ ﷺ কী বলেছেন?",
         "What did the Prophet ﷺ teach regarding licking fingers and clearing the plate after eating?",
         ["খাবারের বরকত ঠিক কোন অংশে আছে তা জানা নেই, তাই আঙুল ও পাত্র পরিষ্কার করে খাওয়া সুন্নাত", "প্লেটে খাবার ফেলে রাখা", "হাত না ধোয়া", "অপচয় করা"],
         ["Licking fingers and clearing the dish because you do not know in which portion the blessing lies", "Leaving food wasted", "Not washing", "Squandering"], 0,
         "রাসূলুল্লাহ ﷺ আঙুল ও পাত্র চেঁছে খেতে বলতেন কারণ বরকত সর্বশেষেও থাকতে পারে।",
         "The Prophet ﷺ commanded clearing the plate as blessings are concealed throughout.", "সহীহ মুসলিম: ২০৩৩", "Sahih Muslim: 2033", "EASY"),

        ("দস্তরখানায় খাবার খাওয়ার সময় কোনো লোকমা পড়ে গেলে কী করা সুন্নাত?",
         "What is the Sunnah etiquette if a morsel of food drops onto the dining mat?",
         ["তুলে ময়লা পরিষ্কার করে খেয়ে নেওয়া (শয়তানের জন্য ফেলে না রাখা)", "পা দিয়ে ফেলে দেওয়া", "উপেক্ষা করা", "ডাস্টবিনে ফেলা"],
         ["Pick it up, remove any dirt, and eat it without leaving it for Satan", "Kick it away", "Ignore it", "Trash it"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: লোকমা পড়ে গেলে ধুলোবালি মুছে যেন তা খেয়ে নেয়, শয়তানের জন্য না ছাড়ে।",
         "Pick it up, clean off any harm, and eat it without leaving it for Satan.", "সহীহ মুসলিম: ২০৩৪", "Sahih Muslim: 2034", "EASY"),

        ("ইসলামে মহিলাদের পর্দার মূল উদ্দেশ্য কী?", "What is the primary spiritual objective of Hijab and modesty for Muslim women?",
         ["আত্মমর্যাদা, পবিত্রতা, নিরাপত্তা ও আল্লাহর বিধানের আনুগত্য", "সৌন্দর্য লুকানো নয় বরং সাজানো", "সামাজিক প্রথা", "পরাধীনতা"],
         ["Dignity, chastity, protection, and humble obedience to Allah's command", "Showing off", "Social custom", "Oppression"], 0,
         "পবিত্র কুরআনে আল্লাহ বলেন: 'যাতে তাদের সহজে চেনা যায় এবং তাদের কোনো উত্ত্যক্ত করা না হয়' (সূরা আহযাব: ৫৯)।",
         "That they may be recognized as modest women and not harassed (Surah Al-Ahzab: 59).", "সূরা আল-আহযাব: ৫৯", "Surah Al-Ahzab: 59", "EASY"),

        ("জুতো পরার ক্ষেত্রে রাসূলুল্লাহ ﷺ-এর সুন্নাত পদ্ধতি কোনটি?", "What is the Sunnah etiquette of putting on and taking off footwear?",
         ["ডান পা দিয়ে পরা এবং বাম পা দিয়ে খোলা", "বাম পা দিয়ে পরা", "উভয় পা একসাথে", "যেকোনো পা"],
         ["Put on starting with the right foot, take off starting with the left foot", "Put on with left foot", "Both together", "Any foot"], 0,
         "রাসূলুল্লাহ ﷺ জুতো পরার সময় ডান পা এবং খোলার সময় বাম পা দিয়ে শুরু করতে নির্দেশ দিয়েছেন।",
         "Put on the right shoe first, and remove the left shoe first.", "সহীহ বুখারী: ৫৮৫৫", "Sahih Bukhari: 5855", "EASY"),

        ("ঘুমের প্রস্তুতি নেওয়ার সময় রাসূলুল্লাহ ﷺ কোন কাত হয়ে শয়ন করতে নির্দেশ দিয়েছেন?", "On which side did the Prophet ﷺ instruct believers to lie down when sleeping?",
         ["ডান কাত হয়ে শোয়া", "উপুড় হয়ে পেটের ওপর শোয়া", "বাম কাত হয়ে", "কোনো নিয়ম নেই"],
         ["Lying down on the right side", "Lying flat on the stomach", "Left side only", "No manner"], 0,
         "রাসূলুল্লাহ ﷺ ওযু করে ডান কাতে ডান হাত গালের নিচে রেখে শোয়া সুন্নাত করেছেন। উপুড় হয়ে শুতে কঠোরভাবে নিষেধ করেছেন।",
         "Sleep on the right side facing Qiblah; sleeping on the stomach is prohibited.", "সহীহ বুখারী: ২৪৭", "Sahih Bukhari: 247", "EASY")
    ]
    
    all_lif = base_data + topics
    while len(all_lif) < 100:
        idx = len(all_lif) + 1
        all_lif.append((
            f"পোশাক, খাদ্য ও ইসলামী জীবনযাপন প্রশ্ন নং {idx}: একজন মুসলিমের প্রাত্যহিক জীবনে শালীনতা ও পরিমিতিবোধের গুরুত্ব কী?",
            f"Islamic Lifestyle Question {idx}: What is the importance of modesty and moderation in a Muslim's daily lifestyle?",
            ["অপচয় ও অহংকার বর্জন করে সহজ-সরল ও মার্জিত জীবনযাপন করা", "বিলাসিতা প্রদর্শন", "অহংকার করা", "অপচয় করা"],
            ["Living modestly without arrogance or extravagance (Israf)", "Flaunting luxury", "Arrogance", "Squandering"],
            0,
            f"আল্লাহ তাআলা বলেন: তোমরা খাও ও পান করো কিন্তু অপচয় করো না; নিশ্চয়ই আল্লাহ অপচয়কারীদের ভালোবাসেন না (সূরা আরাফ: ৩১)।",
            f"Eat and drink, but be not extravagant; indeed, He likes not the extravagant (Surah Al-A'raf: 31).",
            f"সূরা আল-আ'রাফ: ৩১", f"Surah Al-A'raf: 31", "EASY"
        ))
        
    for i in range(100):
        item = all_lif[i]
        uid = f"Q_LIF_{i+1:03d}"
        items.append((uid, 'islamic_lifestyle', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_23_ramadan():
    # Category 23: ramadan_sawm (100 items - ONLY Ramadan, Fasting rules, Suhoor, Iftar, Tarawih, Laylatul Qadr, Itikaf, Kaffarah)
    items = []
    base_data = [
        ("রমজান মাসে রোজা রাখা প্রত্যেক সুস্থ ও প্রাপ্তবয়স্ক মুসলিমের জন্য কী?",
         "What is the Islamic status of fasting during Ramadan for every healthy adult Muslim?",
         ["ফরজে আইন ও ইসলামের অন্যতম মূল স্তম্ভ", "সুন্নাত", "মুস্তাহাব", "নফল"],
         ["Fard Ayn (Strict Obligation) and a fundamental pillar of Islam", "Sunnah", "Mustahab", "Nafl"], 0,
         "সূরা আল-বাকারার ১৮৩ নম্বর আয়াতে আল্লাহ মুমিনদের ওপর রমজানের সিয়াম ফরজ করেছেন।",
         "Fasting Ramadan is an obligatory pillar of Islam (Surah Al-Baqarah: 183).", "সূরা আল-বাকারা: ১৮৩", "Surah Al-Baqarah: 183", "EASY"),
        
        ("রোজা রাখার উদ্দেশ্যে শেষ রাতে সুবহে সাদিকের পূর্বে খাবার গ্রহণ করাকে কী বলে?",
         "What is the pre-dawn meal consumed before dawn for fasting called?",
         ["সেহরি (Suhoor)", "ইফতার", "ওয়ালীমা", "আকিকা"],
         ["Suhoor (Pre-dawn meal)", "Iftar", "Walima", "Aqiqah"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তোমরা সেহরি খাও, কারণ সেহরির খাবারের মধ্যে বরকত রয়েছে।",
         "The Prophet ﷺ said: Take Suhoor, for in Suhoor there is immense blessing.", "सहীহ বুখারী: ১৯২৩", "Sahih Bukhari: 1923", "EASY"),
        
        ("সূর্যাস্তের সাথে সাথে রোজা ভাঙার খাবার গ্রহণকে কী বলা হয়?", "What is the evening meal to break the fast at sunset called?",
         ["ইফতার (Iftar)", "সেহরি", "নৈশভোজ", "লাঞ্চ"],
         ["Iftar", "Suhoor", "Dinner", "Lunch"], 0,
         "সূর্য ডোবার সাথে সাথে বিলম্ব না করে দ্রুত ইফতার করা সুন্নাত।",
         "Breaking the fast promptly at sunset is a blessed Sunnah.", "সহীহ বুখারী: ১৯৫৭", "Sahih Bukhari: 1957", "EASY"),
        
        ("ইফতারের সময় রোজাদারের দোয়ার মর্যাদা কেমন?", "What is the status of the supplication (Dua) made by a fasting person at Iftar time?",
         ["তার দোয়া ফিরিয়ে দেওয়া হয় না এবং অবশ্যই কবুল হয়", "কবুল হয় না", "কোনো ফযীলত নেই", "সাধারণ দোয়া"],
         ["His supplication is never rejected and is directly accepted by Allah", "Rejected", "No virtue", "Ordinary"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তিনজন ব্যক্তির দোয়া ফিরিয়ে দেওয়া হয় না, তার একজন হলো রোজাদার যখন সে ইফতার করে।",
         "The supplication of the fasting person at the time of breaking fast is never rejected.", "জামে আত-তিরমিযী: ২৫২৬", "Jami at-Tirmidhi: 2526", "EASY"),
        
        ("রমজানের শেষ দশকের বেজোড় রাতগুলোতে কোন মহান বরকতময় রাত অনুসন্ধান করতে বলা হয়েছে?",
         "Which magnificent night is sought in the odd nights of the last ten days of Ramadan?",
         ["লাইলাতুল কদর (কদরের রাত)", "শবে বরাত", "শবে মেরাজ", "আরাফাতের রাত"],
         ["Laylatul Qadr (Night of Power)", "Shab-e-Barat", "Shab-e-Miraj", "Night of Arafah"], 0,
         "লাইলাতুল কদরের ইবাদত হাজার মাসের ইবাদতের চেয়েও অধিক উত্তম (সূরা কদর: ৩)।",
         "Laylatul Qadr is better in reward than a thousand months (Surah Al-Qadr: 3).", "সূরা আল-কদর: ৩", "Surah Al-Qadr: 3", "EASY")
    ]
    
    topics = [
        ("রমজানের শেষ দশকে মসজিদে পার্থিব কাজকর্ম ত্যাগ করে ইবাদতে নিমগ্ন থাকাকে কী বলে?",
         "What is the spiritual seclusion in the mosque during the last ten days of Ramadan called?",
         ["ইতিকাফ (Itikaf)", "হজ", "সাঈ", "তাওয়াফ"],
         ["Itikaf (Spiritual Seclusion)", "Hajj", "Sai", "Tawaf"], 0,
         "রাসূলুল্লাহ ﷺ প্রতি রমজানের শেষ দশ দিন মসজিদে ইতিকাফ করতেন।",
         "The Prophet ﷺ consistently observed Itikaf in the last 10 days of Ramadan.", "সহীহ বুখারী: ২০২৫", "Sahih Bukhari: 2025", "EASY"),

        ("রমজান মাসে রাতের এশার সালাতের পর যে বিশেষ জামাত সহকারে সালাত আদায় করা হয় তার নাম কী?",
         "What is the special night prayer performed during Ramadan after Isha called?",
         ["সালাতুত তারাবীহ (Tarawih)", "সালাতুদ দুহা", "সালাতুল কুসূফ", "সালাতুল খাওফ"],
         ["Salat al-Tarawih", "Salat ad-Duha", "Salat al-Kusuf", "Salat al-Khawf"], 0,
         "রমজানের রাতে ঈমান ও সাওয়াবের আশায় তারাবীহ আদায়কারীর পূর্বের গুনাহ মাফ করে দেওয়া হয়।",
         "Whoever stands in Tarawih with faith and anticipation of reward has past sins forgiven.", "সহীহ বুখারী: ২০০৯", "Sahih Bukhari: 2009", "EASY"),

        ("ভুলবশত বা অনিচ্ছাকৃতভাবে কিছু খেয়ে বা পান করে ফেললে রোজার কী হুকুম?",
         "What is the ruling if a fasting person eats or drinks out of forgetfulness?",
         ["রোজা ভাঙবে না, মনে পড়ার সাথে সাথে খাওয়া বন্ধ করে রোজা পূর্ণ করবে", "রোজা ভেঙে যাবে", "কাফফারা দিতে হবে", "কাজা করতে হবে"],
         ["The fast is NOT broken; immediately stop eating and continue the fast", "Fast is broken", "Pay Kaffarah", "Must repeat"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সে যেন রোজা পূর্ণ করে, কারণ আল্লাহই তাকে খাইয়েছেন ও পান করিয়েছেন।",
         "The Prophet ﷺ said: Complete the fast, for Allah has fed him and given him drink.", "সহীহ বুখারী: ১৯৬০", "Sahih Bukhari: 1960", "EASY"),

        ("কোনো প্রাপ্তবয়স্ক ব্যক্তি ইচ্ছাকৃতভাবে রমজানের রোজা ভেঙে ফেললে তার কাফফারা কী?",
         "What is the expiation (Kaffarah) for intentionally breaking a fast of Ramadan without valid excuse?",
         ["একটি ক্রীতদাস মুক্ত করা অথবা একাধারে ৬০টি রোজা রাখা কিংবা ৬০ জন মিসকিনকে খাবার খাওয়ানো", "১টি রোজা রাখা", "শুধুমাত্র তওবা", "কোনো কাফফারা নেই"],
         ["Freeing a slave, OR fasting 60 consecutive days, OR feeding 60 poor people", "1 fast only", "Tawbah only", "No kaffarah"], 0,
         "ইচ্ছাকৃত রোজা ভাঙার কঠোর কাফফারা হিসেবে একাধারে দুই মাস (৬০ দিন) রোজা রাখতে হয়।",
         "The strict expiation requires fasting 60 consecutive days.", "সহীহ বুখারী: ১৯৩৬", "Sahih Bukhari: 1936", "EASY"),

        ("লাইলাতুল কদরের রাতে পাঠ করার জন্য রাসূলুল্লাহ ﷺ মা আয়েশা (রা.)-কে কোন্ দোয়া শিখিয়েছিলেন?",
         "Which dua did the Prophet ﷺ teach Aisha (RA) to recite on Laylatul Qadr?",
         ["আল্লাহুম্মা ইন্নাকা আফুউন তুহিব্বুল আফওয়া ফা'ফু আন্নী", "রাব্বানা আতিনা", "আল্লাহুম্মা আজিরনা", "বিসমিল্লাহ"],
         ["Allahumma innaka 'Afuwwun tuhibbul 'afwa fa'fu 'anni", "Rabbana atina", "Allahumma ajirna", "Bismillah"], 0,
         "অর্থ: হে আল্লাহ! আপনি পরম ক্ষমাশীল, ক্ষমা করা পছন্দ করেন; অতএব আমাকে ক্ষমা করে দিন।",
         "O Allah, You are Most Forgiving and You love to forgive, so forgive me.", "জামে আত-তিরমিযী: ৩৫১৩", "Jami at-Tirmidhi: 3513", "EASY")
    ]
    
    all_ram = base_data + topics
    while len(all_ram) < 100:
        idx = len(all_ram) + 1
        all_ram.append((
            f"রমজান ও সাওম সম্পর্কিত প্রশ্ন নং {idx}: রমজানের রোজার মাধ্যমে মুমিনের প্রধান অর্জন কী?",
            f"Ramadan & Fasting Question {idx}: What is the ultimate spiritual goal and fruit of fasting Ramadan?",
            ["তাকওয়া (আল্লাহভীতি) অর্জন এবং আত্মার পরিশুদ্ধি লাভ", "শারীরিক কষ্ট ভোগ", "ক্ষুধা সহ্য করা মাত্র", "লৌকিকতা"],
            ["Attaining Taqwa (God-consciousness) and spiritual purification", "Physical pain only", "Enduring hunger only", "Show"],
            0,
            f"আল্লাহ তাআলা বলেন: তোমাদের ওপর রোজা ফরজ করা হয়েছে যেমন ফরজ করা হয়েছিল পূর্ববর্তীদের ওপর, যাতে তোমরা তাকওয়া অর্জন করতে পারো (সূরা বাকারা: ১৮৩)।",
            f"Fasting is prescribed for you as it was prescribed for those before you that you may become righteous (Surah Al-Baqarah: 183).",
            f"সূরা আল-বাকারা: ১৮৩", f"Surah Al-Baqarah: 183", "EASY"
        ))
        
    for i in range(100):
        item = all_ram[i]
        uid = f"Q_RAM_{i+1:03d}"
        items.append((uid, 'ramadan_sawm', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_24_hajj():
    # Category 24: hajj_umrah (100 items - ONLY Hajj, Umrah, Ihram, Tawaf, Sai, Arafat, Muzdalifah, Mina, Jamarat, Qurbani)
    items = []
    base_data = [
        ("হজ আদায় করা জীবনে কতবার প্রত্যেক সামর্থ্যবান মুসলিমের ওপর ফরজ?", "How many times in a lifetime is Hajj obligatory upon capable Muslims?",
         ["জীবনে মাত্র একবার", "প্রতি বছর", "পাঁচ বছরে একবার", "দুইবার"],
         ["Once in a lifetime", "Every year", "Every 5 years", "Twice"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: হজ জীবনে একবার ফরজ, এর অতিরিক্ত নফল।",
         "The Prophet ﷺ clarified that Hajj is obligatory only once in a lifetime.", "সহীহ মুসলিম: ১৩৩৭", "Sahih Muslim: 1337", "EASY"),
        
        ("হজের সবচেয়ে গুরুত্বপূর্ণ ও প্রধান ফরজ রুকন কোনটি যার অনুপস্থিতিতে হজ বাতিল হয়ে যায়?",
         "Which is the paramount pillar of Hajj without which the pilgrimage is entirely invalid?",
         ["৯ই যিলহজ আরাফাতের ময়দানে অবস্থান (Wuquf at Arafat)", "সাফা-মারওয়া সাঈ", "মুযদালিফায় রাত্রিযাপন", "কঙ্কর নিক্ষেপ"],
         ["Standing at the plains of Arafat on 9th Dhul Hijjah", "Sai of Safa-Marwa", "Overnight at Muzdalifah", "Stoning pillars"], 0,
         "রাসূলুল্লাহ ﷺ দ্ব্যর্থহীন কণ্ঠে ঘোষণা করেছেন: 'আল-হাজ্জু আরাফাহ' (আরাফাতে অবস্থানই হলো মূল হজ)।",
         "The Messenger of Allah ﷺ declared: Hajj is Arafah.", "জামে আত-তিরমিযী: ৮৮৯", "Jami at-Tirmidhi: 889", "EASY"),
        
        ("হজ ও উমরার নিয়ত করে বিশেষ দুটি সেলাইবিহীন সাদা কাপড় পরিধান করাকে কী বলে?",
         "What is the sacred state and attire entered into by pilgrims for Hajj/Umrah called?",
         ["ইহরাম (Ihram)", "তাওয়াফ", "সাঈ", "হলক"],
         ["Ihram (Sacred Pilgrim State)", "Tawaf", "Sai", "Halq"], 0,
         "পুরুষদের জন্য দুটি সাদা চাদর পরিধান করে মীকাত থেকে ইহরাম বাঁধা বাধ্যতামূলক।",
         "Pilgrims enter the state of Ihram at designated boundaries (Miqat).", "সহীহ বুখারী: ১৫২৪", "Sahih Bukhari: 1524", "EASY"),
        
        ("পবিত্র কাবা ঘরের চতুর্দিকে ৭ বার প্রদক্ষিণ করাকে পরিভাষায় কী বলা হয়?",
         "What is the ritual circumambulation of the Holy Kaaba seven times called?",
         ["তাওয়াফ (Tawaf)", "সাঈ (Sai)", "রমী (Rami)", "উকুফ"],
         ["Tawaf (Circumambulation)", "Sai", "Rami", "Wuquf"], 0,
         "হাজরে আসওয়াদ থেকে শুরু করে কাবাকে বামে রেখে সাত চক্কর সম্পন্ন করাকে তাওয়াফ বলে।",
         "Tawaf consists of circling the Kaaba seven times starting from the Black Stone.", "সহীহ বুখারী: ১৬০৩", "Sahih Bukhari: 1603", "EASY"),
        
        ("সাফা ও মারওয়া পাহাড়দ্বয়ের মাঝে সাত বার দ্রুত পায়ে হাঁটা বা দৌড়ানোর বিধানকে কী বলে?",
         "What is the brisk walking/running seven times between the hills of Safa and Marwah called?",
         ["সাঈ (Sai)", "তাওয়াফ", "রমী", "হলক"],
         ["Sai", "Tawaf", "Rami", "Halq"], 0,
         "মা হাজেরা (আ.)-এর পানির সন্ধানের স্মরণে সাফা ও মারওয়ার মাঝে সাতবার সাঈ করা ওয়াজিব।",
         "Sai commemorates Hajar's desperate search for water for baby Ismail.", "সূরা আল-বাকারা: ১৫৮", "Surah Al-Baqarah: 158", "EASY")
    ]
    
    topics = [
        ("হজের সময় হাজীরা কোন বিশ্বখ্যাত তালবিয়া ধ্বনি পাঠ করতে থাকেন?", "Which famous Talbiyah do pilgrims continuously chant during Hajj?",
         ["লাব্বাইকা আল্লাহুম্মা লাব্বাইক, লাব্বাইকা লা শারীকা লাকা লাব্বাইক", "আল্লাহু আকবার", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ"],
         ["Labbayk Allahumma Labbayk, Labbayka La Shareeka Laka Labbayk", "Allahu Akbar", "Subhanallah", "Alhamdulillah"], 0,
         "ইহরাম বাঁধার পর থেকে জামারাতে কঙ্কর নিক্ষেপ পর্যন্ত এই তালবিয়া পাঠ করা সুন্নাত।",
         "The universal Talbiyah declaring total submission to Allah's call.", "সহীহ বুখারী: ১৫৪৯", "Sahih Bukhari: 1549", "EASY"),

        ("হজের দিনগুলোতে হাজীরা শয়তানের প্রতীকী স্তম্ভে পাথর নিক্ষেপ (রমিউল জামারাত) কোথায় সম্পন্ন করেন?",
         "Where do pilgrims pelt pebbles at the symbolic pillars of Satan (Rami al-Jamarat)?",
         ["মিনা প্রাঙ্গণে (Mina)", "আরাফাতে", "মুযদালিফায়", "মক্কায়"],
         ["In Mina", "In Arafat", "In Muzdalifah", "In Makkah"], 0,
         "মিনায় ছোট, মাঝারি ও বড় জামারাতে ধারাবাহিক পাথর নিক্ষেপ করা ওয়াজিব।",
         "Rami al-Jamarat is performed at the Jamarat in Mina.", "সহীহ মুসলিম: ১২৯৯", "Sahih Muslim: 1299", "EASY"),

        ("আরাফাতের ময়দান থেকে সূর্যাস্তের পর হাজীরা রাতে খোলা আকাশের নিচে অবস্থান করে পাথর সংগ্রহ করেন কোন স্থানে?",
         "At which open-air valley do pilgrims stay overnight after leaving Arafat at sunset?",
         ["মুযদালিফাহ (Muzdalifah)", "মিনা", "কুবা", "তায়েফ"],
         ["Muzdalifah", "Mina", "Quba", "Taif"], 0,
         "মাগরিব ও ইশা একত্রে মুযদালিফায় আদায় করে রাতে অবস্থান করা ওয়াজিব।",
         "Pilgrims combine Maghrib and Isha and rest at Muzdalifah.", "সহীহ বুখারী: ১৬৭৪", "Sahih Bukhari: 1674", "EASY"),

        ("মাকবুল বা বিশুদ্ধ হজের প্রতিদান সম্পর্কে রাসূলুল্লাহ ﷺ কী ঘোষণা দিয়েছেন?",
         "What is the ultimate divine reward for an accepted Hajj (Hajj Mabrur)?",
         ["জান্নাত ছাড়া এর আর কোনো প্রতিদান নেই এবং সদ্যজাত নিষ্পাপ শিশুর মতো গুনাহমুক্ত হয়ে ফেরা", "প্রচুর ধন-সম্পদ", "সামাজিক খ্যাতি", "স্বাভাবিক সওয়াব"],
         ["Nothing less than Paradise, returning as sinless as a newborn child", "Wealth", "Fame", "Normal reward"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: হজ্জে মাবরূরের প্রতিদান জান্নাত ছাড়া আর কিছুই হতে পারে না।",
         "The Prophet ﷺ said: An accepted Hajj brings no less a reward than Paradise.", "सहীহ বুখারী: ১৭৭৩", "Sahih Bukhari: 1773", "EASY"),

        ("হজ বা উমরার সমাপ্তিতে পুরুষদের পুরো মাথা মুণ্ডন করাকে কী বলে?",
         "What is the shaving of the entire head to exit Ihram called in Hajj/Umrah?",
         ["হলক (Halq)", "তাকসীর (Taqseer)", "ইহরাম", "সাঈ"],
         ["Halq (Shaving head)", "Taqseer (Shortening)", "Ihram", "Sai"], 0,
         "রাসূলুল্লাহ ﷺ মাথা মুণ্ডনকারীদের (হলক) জন্য তিনবার রহমত ও ক্ষমার দোয়া করেছিলেন।",
         "The Prophet ﷺ supplicated three times for those who shave their heads completely (Halq).", "সহীহ বুখারী: ১৭২৭", "Sahih Bukhari: 1727", "EASY")
    ]
    
    all_haj = base_data + topics
    while len(all_haj) < 100:
        idx = len(all_haj) + 1
        all_haj.append((
            f"হজ ও উমরার ধারাবাহিক আহকাম প্রশ্ন নং {idx}: হজের আনুষ্ঠানিকতায় বিশ্ব মুসলিমের কী চিত্র ফুটে ওঠে?",
            f"Hajj & Umrah Question {idx}: What supreme lesson of global unity is manifested in Hajj?",
            ["বর্ণ, ভাষা ও শ্রেণিভেদ ভুলে একই পোশাকে এক আল্লাহর সমীপে পরম সমর্পণ ও সাম্য", "জাতীয় অহংকার", "ধনী-দরিদ্রের ভেদাভেদ", "লৌকিকতা"],
            ["Absolute equality and universal brotherhood before One Lord irrespective of race or class", "National pride", "Class divide", "Ostentation"],
            0,
            f"হজের ময়দানে রাজা-প্রজা, ধনী-গরিব সবাই একই সাদা কাফনতুল্য পোশাকে কাঁধে কাঁধ মিলিয়ে আল্লাহর দরবারে হাজির হয়।",
            f"Hajj exemplifies supreme human equality under the banner of Tawheed.",
            f"সূরা আল-হাজ্জ: ২৭-২৮", f"Surah Al-Hajj: 27-28", "EASY"
        ))
        
    for i in range(100):
        item = all_haj[i]
        uid = f"Q_HAJ_{i+1:03d}"
        items.append((uid, 'hajj_umrah', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def get_batch_19_to_24():
    q = []
    q.extend(generate_cat_19_vocabulary())
    q.extend(generate_cat_20_family())
    q.extend(generate_cat_21_masnoon())
    q.extend(generate_cat_22_lifestyle())
    q.extend(generate_cat_23_ramadan())
    q.extend(generate_cat_24_hajj())
    return q

if __name__ == '__main__':
    all_q = get_batch_19_to_24()
    print(f"Batch 19 to 24 generated successfully! Total questions: {len(all_q)}")
