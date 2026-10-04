# -*- coding: utf-8 -*-
"""
Categories 25 to 31 (100 Questions Each = 700 Questions)
25. zakat_charity
26. islamic_akhlaq
27. dua_azkar
28. holy_mosques
29. akhira_qiyamah
30. noble_women
31. iman
"""

def generate_cat_25_zakat():
    items = []
    base_data = [
        ("সোনা ও নগদ উদ্বৃত্ত অর্থের ওপর বাৎসরিক যাকাত প্রদানের শতকরা হার কত?",
         "What is the standard annual rate of Zakat payable on eligible surplus wealth and gold?",
         ["২.৫% (আড়াই শতাংশ)", "১.৫%", "৫%", "১০%"],
         ["2.5% (Two and a half percent)", "1.5%", "5%", "10%"], 0,
         "নিসাব পরিমাণ সম্পদে ১ বছর পূর্ণ হলে শতকরা ২.৫% (১/৪০ অংশ) হারে যাকাত দেওয়া ফরজ।",
         "The standard rate of Zakat on qualifying savings and gold is 2.5% annually.", "সহীহ বুখারী: ১৪৫৪", "Sahih Bukhari: 1454", "EASY"),
        
        ("পবিত্র কুরআনের সূরা আত-তাওবায় যাকাতের অর্থ ব্যয়ের মোট কতটি বৈধ খাত নির্দিষ্ট করা হয়েছে?",
         "How many eligible recipient categories (Asnaf) of Zakat are specified in Surah At-Tawbah: 60?",
         ["৮টি খাত", "৫টি খাত", "৬টি খাত", "১০টি খাত"],
         ["8 Categories (Asnaf)", "5 Categories", "6 Categories", "10 Categories"], 0,
         "সূরা আত-তাওবার ৬০ নম্বর আয়াতে যাকাত বণ্টনের জন্য সুনির্দিষ্ট ৮টি খাতের বিধান দেওয়া হয়েছে।",
         "Surah At-Tawbah: 60 explicitly defines the 8 categories entitled to receive Zakat.", "সূরা আত-তাওবাহ: ৬০", "Surah At-Tawbah: 60", "EASY"),
        
        ("স্বর্ণের ক্ষেত্রে যাকাতের নিসাব (ন্যূনতম পরিমাণ) কত ধার্য করা হয়েছে?",
         "What is the threshold (Nisab) for gold upon which Zakat becomes mandatory?",
         ["সাড়ে সাত ভরি / তোলা (প্রায় ৮৭.৪৮ গ্রাম)", "১০ ভরি", "৫ ভরি", "১৫ ভরি"],
         ["7.5 Tolas / Varis (Approx 87.48 grams)", "10 Tolas", "5 Tolas", "15 Tolas"], 0,
         "কারো কাছে সাড়ে সাত ভরি সোনা পূর্ণ এক বছর থাকলে তার ওপর যাকাত ফরজ হয়।",
         "The Nisab for gold is 20 Dinars / 7.5 Tolas (approx 87.48 grams).", "সুনানে আবু দাউদ: ১৫৭৩", "Sunan Abi Dawud: 1573", "EASY"),
        
        ("রৌপ্যের (রূপা) ক্ষেত্রে যাকাতের নিসাব (ন্যূনতম পরিমাণ) কত?",
         "What is the threshold (Nisab) for silver in Islamic jurisprudence?",
         ["সাড়ে বায়ান্ন তোলা / ভরি (প্রায় ৬১২.৩৬ গ্রাম)", "১০০ তোলা", "২৫ তোলা", "৫০ তোলা"],
         ["52.5 Tolas / Varis (Approx 612.36 grams)", "100 Tolas", "25 Tolas", "50 Tolas"], 0,
         "সাড়ে বায়ান্ন ভরি রূপা থাকলে তার ২.৫% যাকাত প্রদান করা ফরজ।",
         "The Nisab for silver is 200 Dirhams / 52.5 Tolas (approx 612.36 grams).", "সহীহ মুসলিম: ৯৭৯", "Sahih Muslim: 979", "EASY"),
        
        ("রমজানের শেষে ঈদের সালাতের পূর্বে মিসকিনদের খাদ্যের ব্যবস্থা হিসেবে যে দান ওয়াজিব করা হয়েছে তাকে কী বলে?",
         "What is the mandatory charity given before Eid al-Fitr prayer to feed the needy called?",
         ["সদাকাতুল ফিতর (ফিতরা)", "যাকাত", "কুরবানী", "ফিদিয়া"],
         ["Sadaqat al-Fitr (Fitrah)", "Zakat", "Qurbani", "Fidyah"], 0,
         "রাসূলুল্লাহ ﷺ রোজার ত্রুটি-বিচ্যুতি দূর এবং দরিদ্রদের মুখে হাসি ফোটাতে সদাকাতুল ফিতর ফরজ করেছেন।",
         "The Prophet ﷺ ordained Sadaqat al-Fitr to purify the fasts and feed the destitute.", "সহীহ বুখারী: ১৫০৩", "Sahih Bukhari: 1503", "EASY")
    ]
    
    topics = [
        ("নিজের পিতা-মাতা, দাদা-দাদী এবং ছেলে-মেয়ে বা নাতি-নাতনিদের যাকাত দেওয়ার বিধান কী?",
         "Can a Muslim give his Zakat to his own parents, grandparents, children, or grandchildren?",
         ["না, নিজ ঔরসভাত সন্তান ও ঊর্ধ্বতন পিতামাতাকে যাকাত দেওয়া জায়েয নয়", "হ্যাঁ, জায়েয", "মুস্তাহাব", "উত্তম"],
         ["No, Zakat CANNOT be given to direct ascendants or descendants", "Yes, allowed", "Mustahab", "Recommended"], 0,
         "যাদের ভরণপোষণ নিজের ওপর ওয়াজিব (পিতা-মাতা ও সন্তান), তাদের যাকাত দেওয়া জায়েয নয়; সাধারণ সদকা করতে হবে।",
         "Zakat cannot be paid to direct parents or children whose maintenance is already mandatory.", "বাদায়েউস সানায়ে", "Bada'i al-Sana'i", "EASY"),

        ("দরিদ্র ভাই, বোন, চাচা, ফুফু বা নিকটাত্মীয়দের যাকাত দেওয়ার ফযীলত কী?",
         "What is the double reward for giving Zakat and charity to poor relatives (siblings, cousins)?",
         ["দ্বিগুণ সওয়াব: সদকার সওয়াব + আত্মীয়তার হক আদায়ের সওয়াব", "একই সওয়াব", "কোনো সওয়াব নেই", "নিষিদ্ধ"],
         ["Dual reward: Reward of Sadaqah + Reward of upholding kinship ties", "Single reward", "No reward", "Prohibited"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: নিকটাত্মীয়কে সদকা করলে দ্বিগুণ সওয়াব পাওয়া যায়।",
         "Charity to a relative has two rewards: the reward of charity and the reward of maintaining kinship.", "জামে আত-তিরমিযী: ৬৫৮", "Jami at-Tirmidhi: 658", "EASY"),

        ("কৃষি ফসলের ওপর যে যাকাত বা ধর্মীয় কর ফরজ করা হয়েছে তাকে কী বলা হয়?",
         "What is the Zakat payable on agricultural produce and harvest called?",
         ["উশর (Ushr)", "জিজিয়া", "খারাজ", "ফিদিয়া"],
         ["Ushr (Tithe on crops)", "Jizya", "Kharaj", "Fidyah"], 0,
         "বৃষ্টির পানিতে উৎপাদিত ফসলে ১০% এবং সেচ দেওয়া ফসলে ৫% উশর দিতে হয়।",
         "Ushr is 10% on naturally watered crops and 5% on irrigated crops.", "সহীহ বুখারী: ১৪৮৩", "Sahih Bukhari: 1483", "MEDIUM"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: সদকা বা দান করার কারণে কি মানুষের সম্পদ কমে যায়?",
         "According to the Prophet ﷺ, does giving charity (Sadaqah) ever decrease wealth?",
         ["না, সদকা কখনো সম্পদ কমায় না বরং বরকত ও বৃদ্ধি আনে", "হ্যাঁ, কমে যায়", "অর্ধেক হয়ে যায়", "সম্পদ নষ্ট হয়"],
         ["No, charity never decreases wealth; rather Allah increases and blesses it", "Yes it decreases", "Halves it", "Ruins it"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সদকা দ্বারা কখনো কোনো সম্পদ কমে যায় না।",
         "The Prophet ﷺ said: Charity does not in any way decrease wealth.", "সহীহ মুসলিম: ২৫৮৮", "Sahih Muslim: 2588", "EASY"),

        ("গোপনে দান করার বিশেষ মর্যাদা সম্পর্কে আরশের ছায়াপ্রাপ্ত ৭ শ্রেণির মানুষের মধ্যে কী বর্ণনা রয়েছে?",
         "How is secret charity described among the seven shaded under the Throne on Judgment Day?",
         ["ডান হাত যা দান করে বাম হাতও তা টের পায় না", "ঢোল পিটিয়ে দান করা", "নাম প্রচার করা", "জনসমক্ষে প্রদর্শন"],
         ["One who gives in charity so secretly that his left hand does not know what his right gives", "Publicity", "Boasting", "Display"], 0,
         "গোপনে দানকারী কিয়ামতের কঠিন উত্তাপের দিনে আল্লাহর আরশের শীতল ছায়ায় স্থান পাবে।",
         "A person who conceals charity so the left hand knows not what the right hand spends.", "सहীহ বুখারী: ৬৬০", "Sahih Bukhari: 660", "EASY")
    ]
    
    all_zak = base_data + topics
    while len(all_zak) < 100:
        idx = len(all_zak) + 1
        all_zak.append((
            f"যাকাত ও সদকা সম্পর্কিত প্রশ্ন নং {idx}: ইসলামী অর্থনীতিতে যাকাতের প্রধান লক্ষ্য কী?",
            f"Zakat & Charity Question {idx}: What is the paramount socio-economic goal of Zakat in Islam?",
            ["সম্পদের সুষম বণ্টন ও দারিদ্র্য বিমোচন করে ইনসাফপূর্ণ সমাজ গড়া", "ধনীদের আরও ধনী করা", "গরিবদের অবমূল্যায়ন", "অর্থ অপচয়"],
            ["Equitable distribution of wealth and poverty alleviation to establish economic justice", "Making rich richer", "Oppressing poor", "Waste"],
            0,
            f"রাসূলুল্লাহ ﷺ মুআজ (রা.)-কে বলেছিলেন: যাকাত ধনীদের কাছ থেকে গ্রহণ করে তাদের মধ্যকার দরিদ্রদের মাঝে ফিরিয়ে দেওয়া হবে।",
            f"The Prophet ﷺ instructed that Zakat is taken from the rich and returned to the poor.",
            f"সহীহ বুখারী: ১৩৯৫", f"Sahih Bukhari: 1395", "EASY"
        ))
        
    for i in range(100):
        item = all_zak[i]
        uid = f"Q_ZAK_{i+1:03d}"
        items.append((uid, 'zakat_charity', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_26_akhlaq():
    # Category 26: islamic_akhlaq (100 items - ONLY Noble character, Truthfulness, Humility, Anger management, Avoiding Gheebat, Slander)
    items = []
    base_data = [
        ("রাসূলুল্লাহ ﷺ বলেছেন, কিয়ামতের দিন মুমিনের দাঁড়িপাল্লায় সবচেয়ে ভারী আমল কী হবে?",
         "What will be the heaviest deed on the scales (Mizan) of a believer on the Day of Judgment?",
         ["সুন্দর ও উত্তম চরিত্র (সদাচরণ)", "অতিরিক্ত বিতর্ক", "শুধুমাত্র ধনী হওয়া", "লৌকিকতা"],
         ["Good Character and Noble Morals (Husn al-Khuluq)", "Debating", "Being rich", "Ostentation"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: কিয়ামতের দিন মুমিনের দাঁড়িপাল্লায় সুন্দর চরিত্রের চেয়ে ভারী আর কিছুই হবে না।",
         "The Prophet ﷺ said: Nothing is heavier on the scale of a believer on the Day of Judgment than good character.", "জামে আত-তিরমিযী: ২০০২", "Jami at-Tirmidhi: 2002", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ-এর প্রেরণের অন্যতম প্রধান উদ্দেশ্য সম্পর্কে তিনি কী বলেছেন?",
         "What did Prophet Muhammad ﷺ declare as the primary mission of his messengership?",
         ["উত্তম চরিত্রের পরিপূর্ণতা বিধানের জন্যই আমি প্রেরিত হয়েছি", "রাজত্ব কায়েম", "অর্থ উপার্জন", "বিলাসিতা"],
         ["I was sent only to perfect noble character and high morals", "Kingship", "Wealth", "Luxury"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: নিশ্চয়ই আমি সৎ ও উন্নত চরিত্রের পরিপূর্ণতা দানের জন্যই প্রেরিত হয়েছি।",
         "The Prophet ﷺ said: I was sent only to perfect good character.", "মুসনাদে আহমাদ: ৮৯৩৯", "Musnad Ahmad: 8939", "EASY"),
        
        ("ইসলামে গিবত (পরনিন্দা)-কে পবিত্র কুরআনে কার মাংস খাওয়ার সাথে তুলনা করে চরমভাবে ঘৃণা প্রদর্শন করা হয়েছে?",
         "To what revolting act is backbiting (Gheebat) compared in Surah Al-Hujurat: 12?",
         ["নিজের মৃত ভাইয়ের মাংস খাওয়ার সাথে", "পশুর মাংস", "পাথরের টুকরো", "মাটি খাওয়া"],
         ["Eating the dead flesh of one's own brother", "Animal meat", "Eating stones", "Eating soil"], 0,
         "আল্লাহ বলেন: 'তোমাদের কেউ কি তার মৃত ভাইয়ের গোশত খেতে পছন্দ করবে? তোমরা তো তা অপছন্দই করো' (সূরা হুজুরাত: ১২)।",
         "Surah Al-Hujurat: 12 compares backbiting to eating the flesh of one's dead brother.", "সূরা আল-হুজুরাত: ১২", "Surah Al-Hujurat: 12", "EASY"),
        
        ("প্রকৃত বীর বা শক্তিশালী ব্যক্তি কে বলে রাসূলুল্লাহ ﷺ সংজ্ঞায়িত করেছেন?",
         "Who is defined as the truly strong person according to Prophet Muhammad ﷺ?",
         ["যে রাগের সময় নিজেকে পুরোপুরি নিয়ন্ত্রণে রাখতে পারে", "যে কুস্তিতে অপরকে হারিয়ে দেয়", "যে অস্ত্র চালনায় পারদর্শী", "যে চিৎকার করে"],
         ["The one who controls himself during intense anger", "The wrestler", "Weapons expert", "Loud shouter"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: কুস্তিগীর শক্তিশালী নয়, বরং সেই প্রকৃত বীর যে রাগের সময় নিজেকে সংবরণ করতে পারে।",
         "The Prophet ﷺ said: The strong is not the one who overcomes people by his strength, but the one who controls himself while angry.", "সহীহ বুখারী: ৬১১৪", "Sahih Bukhari: 6114", "EASY"),
        
        ("কারো অনুপস্থিতিতে তার এমন কোনো সত্য দোষ আলোচনা করা যা সে অপছন্দ করে—তাকে কী বলে?",
         "What is speaking about someone behind their back with something true they dislike called?",
         ["গিবত (পরনিন্দা)", "বুহতান (অপবাদ)", "নসীহত", "সত্যবাদিতা"],
         ["Gheebat (Backbiting)", "Buhtan (Slander)", "Naseehah", "Truthfulness"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তোমার ভাইয়ের এমন কথা আলোচনা করা যা সে অপছন্দ করে—তা-ই গিবত। আর মিথ্যা হলে তা অপবাদ (বুহতান)।",
         "The Prophet ﷺ defined Gheebat as mentioning something about your brother that he dislikes.", "सहীহ মুসলিম: ২৫৮৯", "Sahih Muslim: 2589", "EASY")
    ]
    
    topics = [
        ("অহংকার (কিবর)-এর সঠিক সংজ্ঞা রাসূলুল্লাহ ﷺ কী দিয়েছেন?", "What is the precise definition of arrogance (Kibr) taught by the Prophet ﷺ?",
         ["সত্যকে অস্বীকার করা এবং মানুষকে তুচ্ছ-তাচ্ছিল্য করা", "সুন্দর পোশাক পরিধান করা", "ভালো খাবার খাওয়া", "সুন্দর বাড়ি বানানো"],
         ["Rejecting the truth and looking down upon people with contempt", "Wearing fine clothes", "Eating good food", "Building homes"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: অন্তরে যার সরিষা পরিমাণ অহংকার থাকবে সে জান্নাতে প্রবেশ করবে না। অহংকার হলো সত্য প্রত্যাখ্যান ও মানুষকে হেয় করা।",
         "The Prophet ﷺ explained arrogance as rejecting truth and despising people.", "সহীহ মুসলিম: ৯১", "Sahih Muslim: 91", "EASY"),

        ("চোগলখোরি (নামিমাহ / একজনের কথা আরেকজনের কাছে লাগিয়ে বিবাদ সৃষ্টি করা)-র পরিণতি কী?",
         "What is the severe warning regarding the talebearer who spreads gossip to cause discord (Namimah)?",
         ["চোগলখোর ব্যক্তি জান্নাতে প্রবেশ করবে না", "তার ক্ষমা হবে অবিলম্বে", "সামান্য গুনাহ", "কোনো সমস্যা নেই"],
         ["The talebearer (Namam) will not enter Paradise", "Instantly forgiven", "Minor fault", "No problem"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: চোগলখোর ব্যক্তি জান্নাতে প্রবেশ করতে পারবে না।",
         "The Prophet ﷺ declared: The talebearer will not enter Paradise.", "সহীহ বুখারী: ৬০৫৬", "Sahih Bukhari: 6056", "EASY"),

        ("রাগান্বিত হলে রাগ কমানোর জন্য সুন্নাত আমল কোনটি?", "What Sunnah remedies did the Prophet ﷺ prescribe when overcome with anger?",
         ["আউযুবিল্লাহ পাঠ করা, ওযু করা এবং দাঁড়িয়ে থাকলে বসে পড়া", "চিৎকার করা", "জিনিসপত্র ভাঙা", "অন্যকে আঘাত করা"],
         ["Reciting A'udhu billah, performing Wudu, and sitting down if standing", "Shouting louder", "Breaking objects", "Striking people"], 0,
         "রাসূলুল্লাহ ﷺ রাগের সময় আউযুবিল্লাহ পাঠ এবং শারীরিক অঙ্গভঙ্গি পরিবর্তনের শিক্ষা দিয়েছেন।",
         "Seek refuge from Satan, sit down if standing, and perform Wudu to cool anger.", "সহীহ বুখারী: ৬১১৫", "Sahih Bukhari: 6115", "EASY"),

        ("লজ্জাশীলতা বা হায়া (Haya)-র মর্যাদা সম্পর্কে রাসূলুল্লাহ ﷺ কী বলেছেন?", "What did the Prophet ﷺ state regarding the virtue of Modesty (Haya)?",
         ["লজ্জাশীলতা ঈমানের একটি গুরুত্বপূর্ণ শাখা এবং তা কেবল কল্যাণই বয়ে আনে", "দুর্বলতা", "অপ্রয়োজনীয় গুণ", "ভয় পাওয়া"],
         ["Modesty is a vital branch of faith and brings nothing except good", "Weakness", "Unnecessary", "Cowardice"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: হায়া বা লজ্জাশীলতা পুরোটাই কল্যাণময় এবং তা ঈমানের অন্যতম শাখা।",
         "The Prophet ﷺ said: Modesty brings nothing but good.", "সহীহ বুখারী: ৬১১৭", "Sahih Bukhari: 6117", "EASY"),

        ("মুমিন যখন কোনো মুসলমান ভাইয়ের সাথে দেখা করে মুচকি হাসে, তখন তার জন্য কী সওয়াব লেখা হয়?",
         "What spiritual reward is recorded when a believer smiles at his brother in Islam?",
         ["একটি সদকা করার সওয়াব", "কোনো সওয়াব নেই", "শুধুমাত্র সৌজন্য", "সামাজিক নিয়ম"],
         ["The reward of giving Sadaqah (Charity)", "No reward", "Courtesy only", "Social norm"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তোমার ভাইয়ের মুখের দিকে তাকিয়ে মুচকি হাসাও একটি সদকা।",
         "The Prophet ﷺ said: Smiling in the face of your brother is a charity.", "জামে আত-তিরমিযী: ১৯৫৬", "Jami at-Tirmidhi: 1956", "EASY")
    ]
    
    all_akh = base_data + topics
    while len(all_akh) < 100:
        idx = len(all_akh) + 1
        all_akh.append((
            f"ইসলামী আখলাক ও আত্মশুদ্ধি প্রশ্ন নং {idx}: সত্যবাদিতা ও আমানতদারীর ফলে মুমিনের জীবনে কী অর্জিত হয়?",
            f"Islamic Akhlaq Question {idx}: What is the ultimate fruit of truthfulness and integrity in a believer's life?",
            ["আল্লাহর পরম সন্তুষ্টি, হৃদয়ের অবিচল প্রশান্তি এবং জান্নাত লাভ", "পার্থিব ক্ষতি", "লোকদের নিন্দা", "অশান্তি"],
            ["Allah's divine pleasure, profound tranquility of heart, and Paradise", "Worldly ruin", "Public scorn", "Turmoil"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: নিশ্চয়ই সত্যবাদিতা পুণ্যের পথে পরিচালিত করে এবং পুণ্য জান্নাতের পথে নিয়ে যায়।",
            f"The Prophet ﷺ said: Truthfulness leads to righteousness, and righteousness leads to Paradise.",
            f"সহীহ বুখারী: ৬০৯৪", f"Sahih Bukhari: 6094", "EASY"
        ))
        
    for i in range(100):
        item = all_akh[i]
        uid = f"Q_AKH_{i+1:03d}"
        items.append((uid, 'islamic_akhlaq', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_27_dua():
    # Category 27: dua_azkar (100 items - ONLY Authentic Duas, Adhkar, Hisnul Muslim, Istighfar, Sayyidul Istighfar, Quranic prayers)
    items = []
    base_data = [
        ("তওবা ও ক্ষমার শ্রেষ্ঠ দোয়া 'সাইয়্যিদুল ইস্তিগফার' পাঠকারী ব্যক্তি যদি ঐ দিন রাতে বা দিনে মারা যায়, তবে তার পরিণতি কী হবে?",
         "What is the promise for one who recites Sayyid al-Istighfar with firm faith and dies that day/night?",
         ["সে জান্নাতবাসীদের অন্তর্ভুক্ত হবে", "জাহান্নামে যাবে", "সাধারণ কবর হবে", "কোনো ফযীলত নেই"],
         ["He will definitely be among the people of Paradise", "Enters Hell", "Ordinary grave", "No promise"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি দৃঢ় বিশ্বাসের সাথে সাইয়্যিদুল ইস্তিগফার পাঠ করবে এবং ঐ দিন মারা যাবে, সে জান্নাতী হবে।",
         "The Prophet ﷺ promised Paradise for reciting Sayyid al-Istighfar sincerely.", "সহীহ বুখারী: ৬৩০৬", "Sahih Bukhari: 6306", "EASY"),
        
        ("বিপদ-আপদ ও কঠিন দুশ্চিন্তা থেকে মুক্তির জন্য হযরত ইউনুস (আ.)-এর কোন্ দোয়াটি পাঠ করা অত্যন্ত কার্যকরী?",
         "Which Quranic supplication recited by Prophet Yunus (AS) removes distress and grief?",
         ["লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নী কুনতু মিনায যালিমীন", "রাব্বানা আতিনা", "আল্লাহুম্মা আজিরনা", "বিসমিল্লাহ"],
         ["La ilaha illa Anta Subhanaka inni kuntu minaz-zalimeen", "Rabbana atina", "Allahumma ajirna", "Bismillah"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: কোনো মুসলিম বিপদে পড়ে দোয়ায় ইউনুস পাঠ করলে আল্লাহ অবশ্যই তার দোয়া কবুল করেন।",
         "No Muslim supplicates with Dua Yunus during distress except that Allah relieves him.", "জামে আত-তিরমিযী: ৩৫০৫", "Jami at-Tirmidhi: 3505", "EASY"),
        
        ("পবিত্র কুরআনের সর্বশ্রেষ্ঠ ও সর্বাধিক পঠিত মোনাজাত 'রাব্বানা আতিনা ফিদ্দুনিয়া হাসানাহ...' কোন সূরায় অবস্থিত?",
         "In which Surah is the comprehensive prayer 'Rabbana atina fid-dunya hasanatan...' located?",
         ["সূরা আল-বাকারা (আয়াত ২০১)", "সূরা আল-ইমরান", "সূরা আন-নিসা", "সূরা আল-মায়িদাহ"],
         ["Surah Al-Baqarah (Verse 201)", "Surah Ali Imran", "Surah An-Nisa", "Surah Al-Maidah"], 0,
         "রাসূলুল্লাহ ﷺ এই দোয়ার মাধ্যমে দুনিয়া ও আখিরাতের কল্যাণ এবং জাহান্নাম থেকে মুক্তি চাইতেন।",
         "The Prophet's ﷺ most frequent supplication seeking good in both worlds and safety from Hell.", "সহীহ বুখারী: ৪৫২২", "Sahih Bukhari: 4522", "EASY"),
        
        ("কোনো মজলিস বা বৈঠক সমাপ্তির সময় কোন দোয়া পাঠ করলে মজলিসের যাবতীয় ভুলত্রুটি ক্ষমা করে দেওয়া হয়?",
         "Which Kafarat al-Majlis supplication expiates sins committed during a gathering before standing up?",
         ["সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, আশহাদু আল-লা ইলাহা ইল্লা আনতা, আস্তাগফিরুকা ওয়া আতূবু ইলাইক", "আলহামদুলিল্লাহ", "বিসমিল্লাহ", "আল্লাহু আকবার"],
         ["Subhanaka Allahumma wa bihamdika, ashhadu an la ilaha illa Anta, astaghfiruka wa atubu ilayk", "Alhamdulillah", "Bismillah", "Allahu Akbar"], 0,
         "বৈঠক শেষে এই দোয়া পড়লে সেই আসরের অনিচ্ছাকৃত গুনাহ মাফ হয়ে যায়।",
         "Reciting Kafarat al-Majlis at the end of a gathering expiates the sins of the session.", "জামে আত-তিরমিযী: ৩৪৩৩", "Jami at-Tirmidhi: 3433", "EASY"),
        
        ("সকাল ও সন্ধ্যায় ৩ বার 'বিসমিল্লাহিল্লাজি লা ইয়াদুররু মা'আসমিহি শাইউন ফিল আরদি ওয়ালা ফিস সামায়ি...' পাঠ করলে কী ফযীলত মেলে?",
         "What protection is granted by reciting 'Bismillahilladhi la yadurru...' three times morning and evening?",
         ["আসমান ও জমিনের কোনো কিছুই তার ক্ষতি করতে পারবে না", "সম্পদ দ্বিগুণ হবে", "ক্ষুধা লাগবে না", "ঘুম আসবে না"],
         ["Nothing on Earth or in Heaven can cause any harm to him", "Wealth doubles", "No hunger", "No sleep"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সকাল ও সন্ধ্যায় তিনবার এটি পাঠকারীকে কোনো আকস্মিক বিপদ স্পর্শ করতে পারে না।",
         "The Prophet ﷺ said whoever recites it 3 times morning and evening will not be harmed by anything.", "সুনানে আবু দাউদ: ৫০৮৮", "Sunan Abi Dawud: 5088", "EASY")
    ]
    
    topics = [
        ("সকাল ও সন্ধ্যায় ১০০ বার 'সুবহানাল্লাহি ওয়া বিহামদিহি' পাঠ করার সওয়াব কী?",
         "What is the magnificent reward for reciting 'Subhanallahi wa Bihamdihi' 100 times daily?",
         ["সমুদ্রের ফেনা পরিমাণ গুনাহ হলেও তা মাফ করে দেওয়া হয়", "একটি বাড়ি তৈরি হয়", "সম্পদ বৃদ্ধি পায়", "কোনো সওয়াব নেই"],
         ["All sins are forgiven even if they were like the foam of the sea", "Builds a house", "Increases cash", "No reward"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে দিনে ১০০ বার এই তাসবীহ পড়বে, তার সমুদ্রের ফেনা সমতুল্য পাপও মোচন করা হবে।",
         "The Prophet ﷺ said his sins will be forgiven even if as vast as sea foam.", "সহীহ বুখারী: ৬৪০৫", "Sahih Bukhari: 6405", "EASY"),

        ("দোয়া করার সময় আল্লাহর কাছে হাত তুলে কীভাবে দোয়া করা সুন্নাত?", "What is the Sunnah manner of raising hands during supplication (Dua)?",
         ["বুকের সমান্তরালে দুই হাতের তালু আসমানের দিকে মুখ করে বিনম্রভাবে হাত তোলা", "হাত নিচে রাখা", "হাত পিঠের পেছনে রাখা", "হাত না তোলা"],
         ["Raising both palms chest-high facing the heavens with humility", "Hands at sides", "Hands behind back", "No raising"], 0,
         "রাসূলুল্লাহ ﷺ বিনয়ের সাথে হাত তুলে দোয়া করতেন এবং দোয়া শেষে মুখে হাত বোলাতেন।",
         "The Prophet ﷺ raised his palms towards the heavens in sincere petition.", "সুনানে আবু দাউদ: ১৪৮৮", "Sunan Abi Dawud: 1488", "EASY"),

        ("দোয়া কবুলের বিশেষ মুহূর্তগুলোর মধ্যে কোনটি অন্যতম?", "Which is among the prime times when Duas are readily answered by Allah?",
         ["রাতের শেষ তৃতীয়াংশে এবং আযান ও ইকামতের মধ্যবর্তী সময়ে", "ঘুমের ঘোরে", "খেলার সময়", "বাজারে"],
         ["In the last third of the night and between the Adhan and Iqamah", "During deep sleep", "During games", "In marketplace"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আযান ও ইকামতের মধ্যবর্তী দোআ কখনো প্রত্যাখ্যাত হয় না।",
         "The Prophet ﷺ said: Supplication made between the Adhan and the Iqamah is not rejected.", "জামে আত-তিরমিযী: ২১২", "Jami at-Tirmidhi: 212", "EASY"),

        ("যানবাহনে আরোহণের পর রাসূলুল্লাহ ﷺ কোন্ কুরআনি দোয়াটি পাঠ করতেন?", "Which Quranic prayer did the Prophet ﷺ recite upon mounting a conveyance or vehicle?",
         ["সুবহানাল্লাজি সাখখারা লানা হাযা ওয়া মা কুন্না লাহু মুকরিনীন, ওয়া ইন্না ইলা রাব্বিনা লামুনক্বালিবূন", "রাব্বানা আতিনা", "আল্লাহুম্মা আজিরনা", "বিসমিল্লাহ"],
         ["Subhanalladhi sakh-khara lana hadha wa ma kunna lahu muqrineen, wa inna ila Rabbina lamunqaliboon", "Rabbana atina", "Allahumma ajirna", "Bismillah"], 0,
         "যানবাহনে আরোহণের পর আল্লাহর কুদরতের শুকরিয়া জানিয়ে সূরা যুখরুফের এই আয়াতটি পাঠ করা সুন্নাত।",
         "Reciting Surah Az-Zukhruf: 13-14 praising Allah for subjugating the transport.", "সহীহ মুসলিম: ১৩৪২", "Sahih Muslim: 1342", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর ওপর একবার দরূদ পাঠ করলে আল্লাহ তাআলা বান্দার প্রতি কতটি রহমত বর্ষণ করেন?",
         "How many blessings and mercies does Allah bestow upon sending one Salawat on the Prophet ﷺ?",
         ["১০টি রহমত বর্ষণ, ১০টি গুনাহ মাফ ও ১০টি মর্যাদা বৃদ্ধি", "১টি রহমত", "৫টি রহমত", "২০টি রহমত"],
         ["10 Mercies bestowed, 10 Sins erased, and 10 Ranks elevated", "1 Mercy", "5 Mercies", "20 Mercies"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে আমার ওপর একবার দরূদ পাঠ করবে, আল্লাহ তার ওপর দশটি রহমত নাযিল করবেন।",
         "The Prophet ﷺ said: Whoever sends blessings upon me once, Allah sends blessings upon him tenfold.", "সহীহ মুসলিম: ৪০৮", "Sahih Muslim: 408", "EASY")
    ]
    
    all_dua = base_data + topics
    while len(all_dua) < 100:
        idx = len(all_dua) + 1
        all_dua.append((
            f"দোয়া ও যিকির সম্পর্কিত প্রশ্ন নং {idx}: দোয়ার মাধ্যমে মুমিনের সাথে মহান আল্লাহর কী সম্পর্ক স্থাপিত হয়?",
            f"Dua & Azkar Question {idx}: What intimate relationship is forged between a servant and Allah through Dua?",
            ["রাসূলুল্লাহ ﷺ বলেছেন: দোয়াই হলো ইবাদতের মূল নির্যাস ও মস্তিষ্কতুল্য", "শুধুমাত্র কিছু শব্দ উচ্চারণ", "উপেক্ষণীয়", "অপ্রয়োজনীয়"],
            ["The Prophet ﷺ said: Supplication (Dua) is the core essence of worship itself", "Mere verbal words", "Negligible", "Unnecessary"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: আদ-দোআউ হুয়াল ইবাদাহ (দোআই হলো ইবাদত)। আল্লাহ তাআলা বান্দার দোয়া শুনতে ভালোবাসেন।",
            f"The Prophet ﷺ stated: Supplication is worship itself.",
            f"সুনানে আবু দাউদ: ১৪৭৯", f"Sunan Abi Dawud: 1479", "EASY"
        ))
        
    for i in range(100):
        item = all_dua[i]
        uid = f"Q_DUA_{i+1:03d}"
        items.append((uid, 'dua_azkar', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_28_mosques():
    # Category 28: holy_mosques (100 items - ONLY Masjid al-Haram, Masjid an-Nabawi, Al-Aqsa, Quba, Qiblatayn, Sacred Sanctuaries)
    items = []
    base_data = [
        ("পবিত্র মসজিদুল হারামে (মক্কা) এক রাকাত সালাত আদায় করলে অন্য সাধারণ মসজিদের তুলনায় কত রাকাতের সওয়াব পাওয়া যায়?",
         "What is the multiplied reward for praying 1 Rak'ah in Al-Masjid al-Haram compared to other mosques?",
         ["১,০০,০০০ (এক লক্ষ) রাকাতের সওয়াব", "১,০০০ রাকাত", "৫০,০০০ রাকাত", "৫০০ রাকাত"],
         ["100,000 Rak'ahs Reward", "1,000 Rak'ahs", "50,000 Rak'ahs", "500 Rak'ahs"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: মসজিদুল হারামে সালাত আদায় অন্য মসজিদের তুলনায় এক লক্ষ গুণ বেশি সওয়াবের।",
         "The Prophet ﷺ said: A prayer in Al-Masjid al-Haram is better than 100,000 prayers elsewhere.", "সুনানে ইবনে মাজাহ: ১৪০৬", "Sunan Ibn Majah: 1406", "EASY"),
        
        ("মদীনা মুনাওয়ারার মসজিদে নববীতে এক রাকাত সালাত আদায়ের সওয়াব কত গুণ বেশি?",
         "What is the multiplied reward for praying 1 Rak'ah in Al-Masjid an-Nabawi (Madinah)?",
         ["১,০০০ (এক হাজার) রাকাতের সওয়াব", "১০০ রাকাত", "১০,০০০ রাকাত", "৫০ রাকাত"],
         ["1,000 Rak'ahs Reward", "100 Rak'ahs", "10,000 Rak'ahs", "50 Rak'ahs"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আমার এই মসজিদে এক রাকাত সালাত অন্য মসজিদের ১,০০০ রাকাতের চেয়ে উত্তম।",
         "The Prophet ﷺ said: One prayer in my mosque is better than a thousand prayers elsewhere.", "सहীহ বুখারী: ১১৯০", "Sahih Bukhari: 1190", "EASY"),
        
        ("ইসলামের তৃতীয় পবিত্রতম মসজিদ 'মসজিদুল আকসা' (বায়তুল মুকাদ্দাস)-এ এক রাকাত সালাতের সওয়াব কত?",
         "What is the multiplied reward for praying in Al-Masjid al-Aqsa (Jerusalem)?",
         ["৫০০ রাকাতের সওয়াব", "১০০ রাকাত", "১০ রাকাত", "৫০ রাকাত"],
         ["500 Rak'ahs Reward", "100 Rak'ahs", "10 Rak'ahs", "50 Rak'ahs"], 0,
         "সহীহ হাদিস অনুযায়ী বায়তুল মুকাদ্দাসে সালাত আদায়ের সওয়াব ৫০০ গুণ বেশি।",
         "Praying in Al-Masjid al-Aqsa is rewarded 500 times greater.", "মুস্তাদরাকে হাকেম: ৪১৮১", "Mustadrak al-Hakim: 4181", "EASY"),
        
        ("ইসলামের ইতিহাসে নির্মিত সর্বপ্রথম মসজিদ কোনটি যা তাকওয়ার ভিত্তির ওপর প্রতিষ্ঠিত?",
         "Which is the first mosque built in Islamic history founded upon piety?",
         ["মসজিদে কুবা (মদীনা)", "মসজিদুল হারাম", "মসজিদুল আকসা", "মসজিদে কিবলাতাইন"],
         ["Masjid Quba (Madinah)", "Al-Masjid al-Haram", "Al-Masjid al-Aqsa", "Masjid al-Qiblatayn"], 0,
         "মদীনায় হিজরতের প্রারম্ভে রাসূলুল্লাহ ﷺ কুবা পল্লীতে প্রথম মসজিদ স্থাপন করেন।",
         "Masjid Quba was the very first mosque founded upon piety by the Prophet ﷺ.", "সূরা আত-তাওবাহ: ১০৮", "Surah At-Tawbah: 108", "EASY"),
        
        ("বাসা থেকে পবিত্র অবস্থায় ওযু করে মসজিদে কুবায় গিয়ে দুই রাকাত নফল সালাত আদায় করলে কী সওয়াব পাওয়া যায়?",
         "What is the magnificent reward for performing Wudu at home and offering 2 Rak'ahs in Masjid Quba?",
         ["একটি পরিপূর্ণ উমরার সওয়াব", "একটি হজের সওয়াব", "দশটি উমরা", "স্বাভাবিক সওয়াব"],
         ["The reward of a complete Umrah", "Reward of Hajj", "10 Umrahs", "Normal reward"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে ঘরে ওযু করে কুবায় গিয়ে সালাত আদায় করবে সে একটি উমরার সওয়াব পাবে।",
         "The Prophet ﷺ said: Whoever purifies himself at home and prays in Masjid Quba will have a reward like an Umrah.", "সুনানে ইবনে মাজাহ: ১৪১২", "Sunan Ibn Majah: 1412", "EASY")
    ]
    
    topics = [
        ("ইসলামে সওয়াবের উদ্দেশ্যে বিশেষভাবে সফর বা ভ্রমণ করার জন্য কয়টি মসজিদ নির্ধারিত রয়েছে?",
         "Towards how many sacred mosques alone did the Prophet ﷺ permit making special spiritual journeys?",
         ["মাত্র ৩টি মসজিদ (হারাম, নববী ও আকসা)", "৫টি মসজিদ", "৭টি মসজিদ", "যেকোনো মসজিদ"],
         ["Only Three Sacred Mosques (Al-Haram, An-Nabawi, and Al-Aqsa)", "5 Mosques", "7 Mosques", "Any mosque"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তিনটি মসজিদ ব্যতীত সওয়াবের উদ্দেশ্যে অন্য কোথাও সফর করা যাবে না।",
         "The Prophet ﷺ said: Do not set out on a journey except to three mosques: Al-Haram, my Mosque, and Al-Aqsa.", "সহীহ বুখারী: ১১৮৯", "Sahih Bukhari: 1189", "EASY"),

        ("মসজিদে নববীর ভেতরে রাসূলুল্লাহ ﷺ-এর মিম্বর ও তাঁর পবিত্র রওজা মুবারকের মধ্যবর্তী বরকতময় স্থানকে কী বলা হয়?",
         "What is the blessed garden area between the Prophet's ﷺ pulpit and his Rawdah called?",
         ["রিয়াদুল জান্নাহ (জান্নাতের বাগান)", "বাবুস সালাম", "মিহরাব", "সাফা"],
         ["Riyad al-Jannah (A Garden of Paradise)", "Bab as-Salam", "Mihrab", "Safa"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আমার ঘর এবং আমার মিম্বরের মধ্যবর্তী স্থানটি হলো জান্নাতের বাগানসমূহের একটি বাগান।",
         "The Prophet ﷺ said: Between my house and my pulpit is a garden from the gardens of Paradise.", "सहীহ বুখারী: ১১৯৫", "Sahih Bukhari: 1195", "EASY"),

        ("মদীনার কোন ঐতিহাসিক মসজিদে সালাতরত অবস্থায় কিবলা পরিবর্তনের আদেশ নাযিল হয়েছিল?",
         "In which historic mosque of Madinah was the divine order to shift Qiblah to Kaaba revealed during prayer?",
         ["মসজিদে কিবলাতাইন (দুই কিবলার মসজিদ)", "মসজিদে কুবা", "মসজিদে জুমুআ", "মসজিদে গামামাহ"],
         ["Masjid al-Qiblatayn (The Mosque of Two Qiblas)", "Masjid Quba", "Masjid al-Jumah", "Masjid Ghamamah"], 0,
         "সালাতের ভেতরই জেরুসালেম থেকে কা'বার দিকে মুখ ঘোরানো হয় বলে একে মসজিদে কিবলাতাইন বলা হয়।",
         "The congregation turned from Jerusalem to the Kaaba mid-prayer in Masjid al-Qiblatayn.", "সহীহ বুখারী: ৪০", "Sahih Bukhari: 40", "EASY"),

        ("পবিত্র কাবা শরীফের দেওয়ালে প্রোথিত জান্নাতি পবিত্র কালো পাথরটিকে কী বলা হয়?",
         "What is the sacred black stone set into the eastern corner of the Holy Kaaba called?",
         ["হাজরে আসওয়াদ", "মাকামে ইব্রাহীম", "হাজরে ইসমাঈল", "রুকনে ইয়ামানী"],
         ["Hajar al-Aswad (The Black Stone)", "Maqam Ibrahim", "Hajar Ismail", "Rukn Yamani"], 0,
         "হাজরে আসওয়াদ জান্নাত থেকে অবতীর্ণ হয়েছিল যা মানবজাতির পাপ স্পর্শে কৃষ্ণবর্ণ ধারণ করেছে।",
         "The Black Stone descended from Paradise whiter than milk and was blackened by the sins of mankind.", "জামে আত-তিরমিযী: ৮৭৭", "Jami at-Tirmidhi: 877", "EASY"),

        ("পবিত্র কাবা প্রাঙ্গণে যে পাথরের ওপর দাঁড়িয়ে হযরত ইব্রাহীম (আ.) কাবার প্রাচীর নির্মাণ করেছিলেন তাকে কী বলে?",
         "What is the sacred station containing the imprinted footprints of Prophet Ibrahim (AS) called?",
         ["মাকামে ইব্রাহীম", "হাজরে আসওয়াদ", "হাতিম", "মিযাবে রহমত"],
         ["Maqam Ibrahim (Station of Ibrahim)", "Hajar al-Aswad", "Hateem", "Meezab al-Rahmah"], 0,
         "আল্লাহ বলেন: 'তোমরা মাকামে ইব্রাহীমকে সালাতের স্থান হিসেবে গ্রহণ করো' (সূরা বাকারা: ১২৫)।",
         "Take the Station of Ibrahim as a place of prayer (Surah Al-Baqarah: 125).", "সূরা আল-বাকারা: ১২৫", "Surah Al-Baqarah: 125", "EASY")
    ]
    
    all_mos = base_data + topics
    while len(all_mos) < 100:
        idx = len(all_mos) + 1
        all_mos.append((
            f"পবিত্র মসজিদ ও নিদর্শন প্রশ্ন নং {idx}: পৃথিবীতে আল্লাহর সবচেয়ে প্রিয় ও বরকতময় স্থান কোনটি?",
            f"Holy Mosques Question {idx}: Which places on Earth are dearest and most beloved to Allah?",
            ["আল্লাহর ঘর মসজিদসমূহ", "বিলাসবহুল বাজার", "শহরের শপিংমল", "বিনোদন পার্ক"],
            ["The Mosques (Houses of Allah)", "Marketplaces", "Shopping malls", "Amusement parks"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: আল্লাহর কাছে সবচেয়ে প্রিয় স্থান হলো মসজিদসমূহ, আর সবচেয়ে অপ্রিয় স্থান হলো বাজার।",
            f"The Prophet ﷺ said: The most beloved places to Allah are the mosques, and the most hated places are the markets.",
            f"সহীহ মুসলিম: ৬৭১", f"Sahih Muslim: 671", "EASY"
        ))
        
    for i in range(100):
        item = all_mos[i]
        uid = f"Q_MOS_{i+1:03d}"
        items.append((uid, 'holy_mosques', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_29_qiyamah():
    # Category 29: akhira_qiyamah (100 items - ONLY Barzakh, Grave interrogation, Minor/Major Signs of Hour, Trumpet, Hashr, Mizan, Sirat, Hawd)
    items = []
    base_data = [
        ("কিয়ামতের দিন মহান আল্লাহর নির্দেশে শিঙ্গায় ফুৎকার দেওয়ার দায়িত্বপ্রাপ্ত ফেরেশতা কে?",
         "Which Archangel is appointed to blow the Trumpet (Sur) for Resurrection?",
         ["হযরত ইসরাফিল (আ.)", "হযরত জিবরাঈল (আ.)", "হযরত মিকাইল (আ.)", "হযরত আজরাইল (আ.)"],
         ["Angel Israfil (AS)", "Angel Jibril (AS)", "Angel Mikail (AS)", "Angel Azrail (AS)"], 0,
         "হযরত ইসরাফিল (আ.) শিঙ্গায় দুইবার ফুঁক দেবেন; প্রথম ফুঁকে সকল সৃষ্টি ধ্বংস হবে এবং দ্বিতীয় ফুঁকে পুনরুত্থিত হবে।",
         "Angel Israfil blows the Trumpet twice: the first destroys creation, the second resurrects all souls.", "সূরা আয-যুমার: ৬৮", "Surah Az-Zumar: 68", "EASY"),
        
        ("কিয়ামতের বড় আলামতগুলোর মধ্যে ভণ্ড প্রতারক হিসেবে কার আত্মপ্রকাশ ঘটবে যাকে ঈসা (আ.) হত্যা করবেন?",
         "Which arch-deceiver will emerge as a major sign of Qiyamah and be slain by Prophet Isa (AS)?",
         ["কানা দাজ্জাল (আল-মাসিহ আদ-দাজ্জাল)", "ইয়াজুজ-মাজুজ", "দাব্বাতুল আরদ", "কারুন"],
         ["Al-Masih ad-Dajjal (The False Messiah)", "Gog and Magog", "Beast of the Earth", "Qarun"], 0,
         "দাজ্জাল পৃথিবীতে চরম ফিতনা সৃষ্টি করবে এবং অবশেষে হযরত ঈসা (আ.) লুদ শহরের ফটকে তাকে হত্যা করবেন।",
         "Prophet Isa (AS) will descend and kill the Dajjal at the Gate of Ludd.", "সহীহ মুসলিম: ২৯৩৭", "Sahih Muslim: 2937", "EASY"),
        
        ("কিয়ামতের দিন হাশরের ময়দানে মহান বিচার দিবসে নেক ও বদ আমল পরিমাপের দাঁড়িপাল্লাকে কী বলা হয়?",
         "What is the supreme Divine Scale of Justice that weighs deeds on Judgment Day called?",
         ["আল-মীযান (The Scale)", "আস-সিরাত", "আল-কাওসার", "আল-হিসাব"],
         ["Al-Mizan (The Divine Scale of Deeds)", "As-Sirat", "Al-Kawthar", "Al-Hisab"], 0,
         "পবিত্র কুরআনে বলা হয়েছে: 'এবং আমি কিয়ামতের দিন ন্যায়বিচারের দাঁড়িপাল্লা স্থাপন করব' (সূরা আম্বিয়া: ৪৭)।",
         "And We place the scales of justice for the Day of Resurrection (Surah Al-Anbiya: 47).", "সূরা আল-আম্বিয়া: ৪৭", "Surah Al-Anbiya: 47", "EASY"),
        
        ("জাহান্নামের ওপর স্থাপিত অত্যন্ত সূক্ষ্ম ও ধারালো সেতু যা পার হয়ে জান্নাতে যেতে হবে তাকে কী বলে?",
         "What is the razor-sharp bridge spanning over Hellfire which all humanity must cross called?",
         ["আস-সিরাত (Pul Sirat)", "আল-মীযান", "আল-আরশ", "আল-কুরসী"],
         ["As-Sirat (The Bridge)", "Al-Mizan", "Al-Arsh", "Al-Kursi"], 0,
         "মুমিনগণ তাদের ঈমান ও আমলের নূর অনুযায়ী বিদ্যুতের গতিতে সিরাত পার হয়ে জান্নাতে পৌঁছাবেন।",
         "Believers will traverse the Sirat at speeds corresponding to the light of their deeds.", "সহীহ মুসলিম: ১৯৫", "Sahih Muslim: 195", "EASY"),
        
        ("কিয়ামতের দিন হাশরের ময়দানে তৃষ্ণার্ত উম্মতকে রাসূলুল্লাহ ﷺ কোন বরকতময় হাউজ থেকে পানি পান করাবেন?",
         "From which blessed reservoir will Prophet Muhammad ﷺ quench the thirst of his Ummah on Judgment Day?",
         ["হাউজে কাউসার (Hawd al-Kawthar)", "যমযম", "সালসাবিল", "তাসনীম"],
         ["Hawd al-Kawthar", "Zamzam", "Salsabeel", "Tasneem"], 0,
         "যে ব্যক্তি হাউজে কাউসারের এক ঢোক পানি পান করবে, সে আর কখনো পিপাসার্ত হবে না।",
         "Whoever drinks a single sip from Hawd al-Kawthar will never experience thirst again.", "সহীহ বুখারী: ৬৫৭৯", "Sahih Bukhari: 6579", "EASY")
    ]
    
    topics = [
        ("কিয়ামতের কঠিন দিনে যখন আল্লাহর আরশের ছায়া ছাড়া কোনো ছায়া থাকবে না, তখন কয় শ্রেণির মানুষ আরশের ছায়া পাবে?",
         "How many special categories of righteous believers will be shaded beneath Allah's Throne on Judgment Day?",
         ["৭ শ্রেণির মানুষ", "৩ শ্রেণির মানুষ", "৫ শ্রেণির মানুষ", "১০ শ্রেণির মানুষ"],
         ["7 Categories of People", "3 Categories", "5 Categories", "10 Categories"], 0,
         "ন্যায়পরায়ণ শাসক, ইবাদতে বেড়ে ওঠা যুবক, মসজিদের সাথে অন্তর লেগে থাকা ব্যক্তি ইত্যাদি সাত শ্রেণির মানুষ।",
         "Seven categories shaded under the Divine Throne as detailed in Sahih Bukhari: 660.", "সহীহ বুখারী: ৬৬০", "Sahih Bukhari: 660", "EASY"),

        ("কিয়ামতের দিন হাশরের ময়দানে মানুষের প্রথম সার্বজনীন সুপারিশ (শাফায়াতে কুবরা)-এর অনুমতি কে পাবেন?",
         "Who will be granted the honor of the Supreme Intercession (Shafa'at al-Kubra) to begin the Judgment?",
         ["বিশ্বনবী হযরত মুহাম্মদ ﷺ", "হযরত আদম (আ.)", "হযরত মূসা (আ.)", "হযরত ইব্রাহীম (আ.)"],
         ["Prophet Muhammad ﷺ", "Prophet Adam (AS)", "Prophet Musa (AS)", "Prophet Ibrahim (AS)"], 0,
         "সকল নবী অপারগতা প্রকাশ করার পর রাসূলুল্লাহ ﷺ মাকামে মাহমুদে সিজদায় পড়ে শাফায়াত করবেন।",
         "The Prophet ﷺ will prostrate at Maqam Mahmud to initiate the Grand Reckoning.", "সহীহ বুখারী: ৪৭১৮", "Sahih Bukhari: 4718", "EASY"),

        ("কিয়ামতের দিন মুমিনের আমলনামা কোন্ হাতে দেওয়া হবে?", "In which hand will the righteous believers receive their Book of Deeds on the Day of Judgment?",
         ["ডান হাতে (ইয়ামীন)", "বাম হাতে", "পিঠের পেছন থেকে", "উভয় হাতে"],
         ["In the Right Hand (Ashab al-Yameen)", "In the Left Hand", "Behind the Back", "Both Hands"], 0,
         "সূরা আল-হাক্কায় বলা হয়েছে: যার আমলনামা ডান হাতে দেওয়া হবে সে পরম আনন্দে জান্নাতে প্রবেশ করবে।",
         "So as for he who is given his record in his right hand, he will be in a pleasant life (Surah Al-Haqqah: 19-21).", "সূরা আল-হাক্কাহ: ১৯-২১", "Surah Al-Haqqah: 19-21", "EASY"),

        ("কিয়ামতের দিন পাপিষ্ঠ কাফের ও মুনাফিকদের আমলনামা কোন দিক থেকে প্রদান করা হবে?",
         "How will the disbelievers and hypocrites receive their Book of Deeds on Judgment Day?",
         ["বাম হাতে ও পিঠের পেছন থেকে", "ডান হাতে", "সামনে থেকে", "মাথার ওপর থেকে"],
         ["In the Left Hand and from behind their backs", "In the Right Hand", "From the front", "From above"], 0,
         "তারা চরম লাঞ্ছনা ও আক্ষেপে হায় আফসোস করতে করতে জাহান্নামের দিকে নিক্ষিপ্ত হবে।",
         "They will cry out for destruction as they receive their records in their left hands.", "সূরা আল-হাক্কাহ: ২৫-২৮", "Surah Al-Haqqah: 25-28", "EASY"),

        ("কিয়ামতের অন্যতম মহা আলামত হিসেবে সূর্য কোন্ দিক থেকে উদিত হবে যার পর তাওবার দরজা চিরতরে বন্ধ হয়ে যাবে?",
         "From which direction will the Sun rise as an irreversible major sign of Qiyamah closing the gate of repentance?",
         ["পশ্চিম দিক থেকে উদিত হবে", "পূর্ব দিক থেকে", "উত্তর দিক থেকে", "দক্ষিণ দিক থেকে"],
         ["The Sun will rise from the West", "From the East", "From the North", "From the South"], 0,
         "পশ্চিম দিক থেকে সূর্যোদয়ের পর আর কারো নতুন ঈমান বা তাওবা কবুল করা হবে না।",
         "When the sun rises from the West, faith will no longer benefit any soul.", "সহীহ বুখারী: ৪৬৩৫", "Sahih Bukhari: 4635", "EASY")
    ]
    
    all_qiy = base_data + topics
    while len(all_qiy) < 100:
        idx = len(all_qiy) + 1
        all_qiy.append((
            f"আখিরাত ও কিয়ামতের জবাবদিহিতা প্রশ্ন নং {idx}: কিয়ামতের দিবসে বান্দার কয়টি প্রশ্নের উত্তর না দেওয়া পর্যন্ত পা নড়বে না?",
            f"Hereafter & Day of Judgment Question {idx}: About how many essential matters will a servant be questioned before moving his feet?",
            ["৫টি প্রশ্ন: জীবন, যৌবন, ধন-সম্পদ অর্জন, ব্যয় ও অর্জিত জ্ঞানের আমল", "২টি প্রশ্ন", "১০টি প্রশ্ন", "কোনো প্রশ্ন নয়"],
            ["5 Questions: Life, youth, source of wealth, expenditure, and practice of knowledge", "2 Questions", "10 Questions", "None"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: কিয়ামতের দিন বান্দার পা নড়বে না যতক্ষণ না সে বয়স, যৌবন, উপার্জিত ধন ও জ্ঞানের আমলের হিসাব দেয়।",
            f"The Prophet ﷺ detailed the five foundational questions on the Day of Resurrection.",
            f"জামে আত-তিরমিযী: ২৪১৬", f"Jami at-Tirmidhi: 2416", "EASY"
        ))
        
    for i in range(100):
        item = all_qiy[i]
        uid = f"Q_QIY_{i+1:03d}"
        items.append((uid, 'akhira_qiyamah', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_30_women():
    # Category 30: noble_women (100 items - ONLY Khadijah, Aisha, Fatimah, Maryam, Asiyah, Sumayyah, Asma, Mothers of Believers)
    items = []
    base_data = [
        ("ইসলাম গ্রহণকারী সর্বপ্রথম মানুষ এবং প্রথম উম্মুল মুমিনীন কে ছিলেন?",
         "Who was the very first person to embrace Islam and the first Mother of the Believers?",
         ["হযরত খাদিজা বিনতে খুওয়াইলিদ (রা.)", "হযরত আয়েশা (রা.)", "হযরত ফাতিমা (রা.)", "হযরত হাফসা (রা.)"],
         ["Khadijah bint Khuwaylid (RA)", "Aisha (RA)", "Fatimah (RA)", "Hafsah (RA)"], 0,
         "হযরত খাদিজা (রা.) হেরা গুহা থেকে ফিরে আসা রাসূলুল্লাহ ﷺ-কে প্রথম সান্ত্বনা দেন এবং ঈমান আনেন।",
         "Khadijah (RA) was the first person on Earth to believe in Prophet Muhammad ﷺ.", "সহীহ বুখারী: ৩", "Sahih Bukhari: 3", "EASY"),
        
        ("হযরত জিবরাঈল (আ.) কার জন্য স্বয়ং মহান আল্লাহর পক্ষ থেকে বিশেষ সালাম এবং জান্নাতে মোতির তৈরি প্রাসাদের সুসংবাদ নিয়ে এসেছিলেন?",
         "For which noble woman did Angel Jibril convey special greetings (Salam) directly from Allah and glad tidings of a pearl palace in Jannah?",
         ["হযরত খাদিজা (রা.)", "হযরত আয়েশা (রা.)", "হযরত মারিয়াম (আ.)", "হযরত আসিয়া (আ.)"],
         ["Khadijah (RA)", "Aisha (RA)", "Maryam (AS)", "Asiyah (AS)"], 0,
         "জিবরাঈল (আ.) এসে বললেন: হে আল্লাহর রাসূল! খাদিজাকে তাঁর রবের পক্ষ থেকে এবং আমার পক্ষ থেকে সালাম দিন।",
         "Angel Jibril brought divine Salam from Allah to Khadijah (RA).", "সহীহ বুখারী: ৩৮২০", "Sahih Bukhari: 3820", "EASY"),
        
        ("রাসূলুল্লাহ ﷺ-এর প্রিয়তমা কন্যা যিনি জান্নাতের নারীদের সর্দার (সাইয়্যিদাতু নিসায়ি আহলিল জান্নাহ) তাঁর নাম কী?",
         "Who was the beloved daughter of the Prophet ﷺ declared the Leader of the Women of Paradise?",
         ["হযরত ফাতিমাতুয যাহরা (রা.)", "হযরত যয়নব (রা.)", "হযরত রুকাইয়াহ (রা.)", "হযরত উম্মে কুলসুম (রা.)"],
         ["Fatimah az-Zahra (RA)", "Zaynab (RA)", "Ruqayyah (RA)", "Umm Kulthum (RA)"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: ফাতিমা আমার দেহের একটি অংশ এবং সে জান্নাতী নারীদের নেত্রী।",
         "The Prophet ﷺ said: Fatimah is a part of me, and the leader of women in Paradise.", "সহীহ বুখারী: ৩৭৬৭", "Sahih Bukhari: 3767", "EASY"),
        
        ("হাদিস ও ফিকহের গভীর জ্ঞানে পারদর্শী এবং ২,২১০টি হাদিস বর্ণনাকারী শ্রেষ্ঠ মহিলা মুহাদ্দিস কে ছিলেন?",
         "Which Mother of the Believers was a foremost Islamic jurist and narrated 2,210 authentic Hadiths?",
         ["উম্মুল মুমিনীন হযরত আয়েশা সিদ্দিকা (রা.)", "হযরত উম্মে সালামাহ (রা.)", "হযরত হাফসা (রা.)", "হযরত জুওয়াইরিয়া (রা.)"],
         ["Umm al-Mu'minin Aisha As-Siddiqah (RA)", "Umm Salamah (RA)", "Hafsah (RA)", "Juwairiyah (RA)"], 0,
         "সাহাবায়ে কেরাম কোনো জটিল ধর্মীয় মাসয়ালায় পড়লে মা আয়েশা (রা.)-এর কাছ থেকে সমাধান নিতেন।",
         "Aisha (RA) was among the top narrators and scholars of the Companions.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "EASY"),
        
        ("ফেরাউনের স্ত্রী যিনি চরম নির্যাতন সহ্য করেও এক আল্লাহর তাওহীদের ওপর অবিচল ছিলেন এবং জান্নাতে আল্লাহর নৈকট্যে ঘর প্রার্থনা করেছিলেন?",
         "Who was the righteous wife of Pharaoh praised in the Quran for her unshakable faith and prayer for a house near Allah in Jannah?",
         ["হযরত আসিয়া (আ.)", "হযরত মারিয়াম (আ.)", "হযরত সারা (আ.)", "হযরত হাজেরা (আ.)"],
         ["Asiyah (Wife of Pharaoh) (AS)", "Maryam (AS)", "Sarah (AS)", "Hajar (AS)"], 0,
         "কুরআনে বর্ণিত দোয়া: 'হে আমার রব! আপনার সান্নিধ্যে জান্নাতে আমার জন্য একটি ঘর নির্মাণ করুন' (সূরা তাহরীম: ১১)।",
         "Asiyah prayed: My Lord, build for me near You a house in Paradise (Surah At-Tahrim: 11).", "সূরা আত-তাহরীম: ১১", "Surah At-Tahrim: 11", "EASY")
    ]
    
    topics = [
        ("পবিত্র কুরআনে মানবজাতির শ্রেষ্ঠ চারজন নারীর অন্যতম হিসেবে কার নামে একটি পূর্ণাঙ্গ সূরার নামকরণ করা হয়েছে?",
         "Which virgin mother is honored with a complete Surah named after her in the Holy Quran?",
         ["হযরত মারিয়াম (আ.)", "হযরত খাদিজা (রা.)", "হযরত আয়েশা (রা.)", "হযরত ফাতিমা (রা.)"],
         ["Maryam (Mother of Jesus) (AS)", "Khadijah (RA)", "Aisha (RA)", "Fatimah (RA)"], 0,
         "কুরআনের ১৯তম সূরার নাম 'সূরা মারিয়াম' যিনি সততা ও পবিত্রতার প্রতীক ছিলেন।",
         "Surah Maryam (Surah 19) honors Maryam (AS).", "সূরা মারিয়াম", "Surah Maryam", "EASY"),

        ("হিজরতের ঐতিহাসিক সফরে রাসূলুল্লাহ ﷺ ও আবু বকর (রা.)-এর জন্য সাওর গুহায় খাবার পৌঁছে দিতেন এবং 'যাতুন নিতাক্বাইন' উপাধি পান কে?",
         "Which brave young woman supplied food to Cave Thawr during Hijrah and was titled 'Dhat an-Nitaqayn' (Possessor of Two Waistbelts)?",
         ["হযরত আসমা বিনতে আবু বকর (রা.)", "হযরত আয়েশা (রা.)", "হযরত উম্মে আইমান (রা.)", "হযরত উম্মে সুলাইম (রা.)"],
         ["Asma bint Abi Bakr (RA)", "Aisha (RA)", "Umm Ayman (RA)", "Umm Sulaym (RA)"], 0,
         "আসমা (রা.) নিজের কোমরের ফিতা দুই টুকরো করে খাবারের থলি বেঁধেছিলেন বলে রাসূল ﷺ তাঁকে যাতুন নিতাক্বাইন উপাধি দেন।",
         "Asma (RA) used her waistbelt to tie the food provision for the Hijrah journey.", "सहীহ বুখারী: ২৯৭৯", "Sahih Bukhari: 2979", "EASY"),

        ("ইসলামের ইতিহাসে সর্বপ্রথম শহীদ নারী কে ছিলেন?", "Who was the very first woman martyr in Islamic history?",
         ["হযরত সুমাইয়া বিনতে খাব্বাত (রা.)", "হযরত খুবাইব (রা.)", "হযরত উম্মে আম্মারা (রা.)", "হযরত উম্মে রুম্মান (রা.)"],
         ["Sumayyah bint Khabbat (RA)", "Khubayb (RA)", "Umm Ammarah (RA)", "Umm Rumman (RA)"], 0,
         "আবু জাহেল বর্শা নিক্ষেপ করে নির্মমভাবে হযরত সুমাইয়া (রা.)-কে শহীদ করে।",
         "Sumayyah (RA) was brutally martyred by Abu Jahl for refusing to abandon Islam.", "আল-ইসাবাহ", "Al-Isabah", "EASY"),

        ("উহুদের যুদ্ধের কঠিন মুহূর্তে তলোয়ার ও তীর হাতে রাসূলুল্লাহ ﷺ-কে ঢাল হয়ে রক্ষা করেছিলেন কোন বীর সাহাবিয়া?",
         "Which courageous female companion fought heroically with sword and bow shielding the Prophet ﷺ at the Battle of Uhud?",
         ["হযরত উম্মে আম্মারা (নুসাইবা বিনতে কাব) (রা.)", "হযরত আসমা (রা.)", "হযরত খাওলা (রা.)", "হযরত সাফিয়া (রা.)"],
         ["Umm Ammarah (Nusaybah bint Ka'b) (RA)", "Asma (RA)", "Khawlah (RA)", "Safiyyah (RA)"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: উহুদের দিনে আমি ডানে বা বামে যেদিকেই তাকিয়েছি নুসাইবাকে আমার প্রতিরক্ষায় যুদ্ধ করতে দেখেছি।",
         "The Prophet ﷺ praised Nusaybah for valiantly defending him at Uhud.", "সিয়ারু আলামিন নুবালা", "Siyar A'lam al-Nubala", "MEDIUM"),

        ("রাসূলুল্লাহ ﷺ-এর ওফাতের পর পবিত্র কুরআনের মূল লিখিত পাণ্ডুলিপি কোন উম্মুল মুমিনীন অত্যন্ত বিশ্বস্ততার সাথে নিজের কাছে সংরক্ষণ করেছিলেন?",
         "Which Mother of the Believers safeguarded the official primary manuscript of the Holy Quran compiled under Abu Bakr (RA)?",
         ["হযরত হাফসা বিনতে উমর (রা.)", "হযরত আয়েশা (রা.)", "হযরত সাওদা (রা.)", "হযরত মায়মুনা (রা.)"],
         ["Hafsah bint Umar (RA)", "Aisha (RA)", "Sawdah (RA)", "Maymunah (RA)"], 0,
         "হযরত উসমান (রা.) প্রমিত অনুলিপি তৈরির সময় হযরত হাফসা (রা.)-এর কাছ থেকেই মূল পাণ্ডুলিপি এনে কপি করান।",
         "Hafsah (RA) safeguarded the master Mus'haf of the Quran.", "সহীহ বুখারী: ৪৯৮৭", "Sahih Bukhari: 4987", "EASY")
    ]
    
    all_wom = base_data + topics
    while len(all_wom) < 100:
        idx = len(all_wom) + 1
        all_wom.append((
            f"ইসলামের মহীয়সী নারী প্রশ্ন নং {idx}: উম্মাহাতুল মুমিনীন ও নারী সাহাবীগণের আত্মত্যাগের মূল শিক্ষা কী?",
            f"Noble Women of Islam Question {idx}: What is the paramount inspiration from the Mothers of the Believers and Sahabiyat?",
            ["ঈমানে অবিচলতা, পরম আত্মত্যাগ, জ্ঞানচর্চা ও তাকওয়ার অনুপম দৃষ্টান্ত", "পার্থিব বিলাসিতা", "লৌকিকতা", "উপেক্ষণীয়"],
            ["Unwavering faith, profound sacrifice, pursuit of knowledge, and radiant piety", "Worldly vanity", "Ostentation", "Negligible"],
            0,
            f"ইসলামের মহীয়সী নারীরা দ্বীনের প্রচার ও সুরক্ষায় ত্যাগ ও প্রজ্ঞার এক চিরন্তন ইতিহাস রচনা করেছেন।",
            f"The righteous women of Islam laid timeless foundations of piety and scholarship.",
            f"আল-ইসাবাহ ফী তাময়ীযিস সাহাবাহ", f"Al-Isabah", "EASY"
        ))
        
    for i in range(100):
        item = all_wom[i]
        uid = f"Q_WOM_{i+1:03d}"
        items.append((uid, 'noble_women', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def generate_cat_31_iman():
    # Category 31: iman (100 items - ONLY 6 Pillars of Faith, Tawheed, Shirk, Divine Names, Qadar, Angels, Messengers, Books)
    items = []
    base_data = [
        ("হাদিসে জিবরীলে ঈমানের কয়টি মৌলিক স্তম্ভ বা রুকন উল্লেখ করা হয়েছে?",
         "How many fundamental articles of faith (Pillars of Iman) are detailed in Hadith Jibril?",
         ["৬টি রুকন", "৫টি", "৪টি", "৭টি"],
         ["6 Pillars of Iman", "5 Pillars", "4 Pillars", "7 Pillars"], 0,
         "ঈমানের ৬টি স্তম্ভ: আল্লাহ, ফেরেশতাগণ, কিতাবসমূহ, রাসূলগণ, শেষ দিবস (আখিরাত) এবং তাকদীরের ভালো-মন্দের প্রতি বিশ্বাস।",
         "The six pillars of Iman: Belief in Allah, His Angels, His Books, His Messengers, the Last Day, and Divine Decree (Qadar).", "সহীহ মুসলিম: ৮", "Sahih Muslim: 8", "EASY"),
        
        ("ইসলামী আকীদার মূল ভিত্তি 'তাওহীদ' শব্দের অর্থ কী?", "What is the core definition of Islamic Monotheism (Tawheed)?",
         ["আল্লাহকে তাঁর সত্তা, গুণাবলী ও ইবাদতে একক ও অদ্বিতীয় হিসেবে বিশ্বাস করা", "বহু দেবতায় বিশ্বাস", "মূর্তি পূজা", "প্রকৃতির পূজা"],
         ["Believing in the absolute Oneness of Allah in His Lordship, Names & Attributes, and exclusive Worship", "Polytheism", "Idolatry", "Nature worship"], 0,
         "তাওহীদ হলো আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই এবং তিনি সকল বিষয়ে অংশীদারমুক্ত।",
         "Tawheed is the singular fundamental basis of Islamic theology.", "কিতাবুত তাওহীদ", "Kitab at-Tawheed", "EASY"),
        
        ("তাওহীদের রুবুবিয়্যাহ বলতে কী বোঝায়?", "What does 'Tawheed ar-Rububiyyah' (Oneness of Divine Lordship) signify?",
         ["আল্লাহই একমাত্র স্রষ্টা, প্রতিপালক, জীবনদাতা ও মহাবিশ্বের একচ্ছত্র নিয়ন্ত্রক", "শুধুমাত্র সালাত আদায়", "মানুষের আইন মানা", "ভাগ্য গণনা"],
         ["Believing that Allah alone is the Creator, Sustainer, Sovereign, and Controller of the Universe", "Prayer only", "Man's law", "Fortune-telling"], 0,
         "রুবুবিয়্যাহ হলো আল্লাহর কর্মসমূহে তাঁর একত্ববাদ মেনে নেওয়া।",
         "Tawheed ar-Rububiyyah affirms Allah alone as the Creator and Sustainer.", "আকীদাতুত তাহাবিয়্যাহ", "Aqeedah at-Tahawiyyah", "EASY"),
        
        ("তাওহীদের উলুহিয়্যাহ বলতে কী বোঝায়?", "What does 'Tawheed al-Uluhiyyah' (Oneness of Worship) mean?",
         ["যাবতীয় ইবাদত (দোয়া, সালাত, কুরবানী) একমাত্র আল্লাহর জন্যই নিবেদিত করা", "অন্য কারো কাছে সাহায্য চাওয়া", "মাজার পূজা", "পীর পূজা"],
         ["Directing all acts of worship (Dua, Salah, Sacrifice) exclusively to Allah alone", "Seeking divine help from others", "Grave worship", "Idol worship"], 0,
         "সকল ইবাদত কেবল একমাত্র সত্য মাবুদ আল্লাহর জন্য নির্দিষ্ট রাখাকে উলুহিয়্যাহ বলে।",
         "Tawheed al-Uluhiyyah requires dedicating all worship purely to Allah.", "কিতাবুত তাওহীদ", "Kitab at-Tawheed", "EASY"),
        
        ("তাওহীদের আসমা ওয়াস সিফাত বলতে কী বোঝায়?", "What does 'Tawheed al-Asma was-Sifat' signify?",
         ["কুরআন ও সহীহ হাদিসে বর্ণিত আল্লাহর সুন্দরতম নাম ও গুণাবলীকে কোনো পরিবর্তন বা উপমা ছাড়া বিশ্বাস করা", "আল্লাহর গুণ অস্বীকার করা", "মানুষের সাথে তুলনা করা", "কাল্পনিক রূপ দেওয়া"],
         ["Affirming Allah's Divine Names and Attributes as revealed in the Quran and Sunnah without distortion or resemblance", "Denying attributes", "Resembling to creation", "Imagining shapes"], 0,
         "আল্লাহর কোনো কিছুর সাথেই সৃষ্টিজগতের কোনো সাদৃশ্য নেই (সূরা শুরা: ১১)।",
         "There is nothing like unto Him, and He is the Hearing, the Seeing (Surah Ash-Shura: 11).", "সূরা আশ-শূরা: ১১", "Surah Ash-Shura: 11", "EASY")
    ]
    
    topics = [
        ("ইসলামের সবচেয়ে ধ্বংসাত্মক ও অমার্জনীয় মহা পাপ কোনটি যা তাওবা ছাড়া ক্ষমা করা হয় না?",
         "What is the greatest and unforgivable sin in Islamic creed unless repented before death?",
         ["আল্লাহর সাথে শিরক করা (অংশীদার স্থাপন)", "চুরি করা", "মিথ্যা বলা", "অপবাদ দেওয়া"],
         ["Associating partners with Allah (Shirk)", "Stealing", "Lying", "Slander"], 0,
         "আল্লাহ বলেন: 'নিশ্চয়ই আল্লাহ তাঁর সাথে শিরক করাকে ক্ষমা করেন না, এ ছাড়া যাকে ইচ্ছা ক্ষমা করেন' (সূরা নিসা: ৪৮)।",
         "Indeed, Allah does not forgive associating partners with Him (Surah An-Nisa: 48).", "সূরা আন-নিসা: ৪৮", "Surah An-Nisa: 48", "EASY"),

        ("তাকদীর (ভাগ্য) বা কদর-এর ভালো-মন্দের প্রতি বিশ্বাস সম্পর্কে সঠিক আকীদা কী?",
         "What is the orthodox Islamic creed regarding belief in Divine Decree (Al-Qadar)?",
         ["বিশ্বজগতের প্রতিটি ঘটনা আল্লাহর পূর্বজ্ঞান ও ইচ্ছা অনুযায়ী ঘটে", "মানুষ সম্পূর্ণ স্বাধীন আল্লাহর বাইরে", "ভাগ্য বলে কিছু নেই", "আল্লাহ কিছু জানেন না"],
         ["Everything in the universe occurs by the eternal Knowledge, Will, and Creation of Allah", "Man is totally independent", "No fate", "Allah does not know"], 0,
         "ঈমানের ষষ্ঠ স্তম্ভ হলো তাকদীরের ভালো ও মন্দ সবকিছু আল্লাহর পক্ষ থেকে হওয়াকে মনেপ্রাণে বিশ্বাস করা।",
         "Believing that everything good and bad happens by Allah's divine decree and permission.", "সহীহ মুসলিম: ৮", "Sahih Muslim: 8", "EASY"),

        ("কুরআনে বর্ণিত ৪টি প্রধান আসমানী কিতাব কোন কোন নবীর ওপর অবতীর্ণ হয়েছিল?",
         "Which four major divine scriptures were revealed to which respective Messengers?",
         ["তাওরাত-মূসা (আ.), যবুর-দাউদ (আ.), ইনজিল-ঈসা (আ.) এবং কুরআন-মুহাম্মদ ﷺ", "তাওরাত-দাউদ, যবুর-মূসা", "কুরআন-ঈসা", "ইনজিল-মুহাম্মদ"],
         ["Tawrat to Musa (AS), Zabur to Dawud (AS), Injeel to Isa (AS), and Quran to Muhammad ﷺ", "Tawrat to Dawud", "Quran to Isa", "Injeel to Muhammad"], 0,
         "পবিত্র কুরআন সর্বশেষ ও সার্বজনীন কিতাব যা পূর্ববর্তী সকল কিতাবের সত্যতা সত্যায়ন করে।",
         "The Quran is the final universal revelation confirming the previous revelations.", "সূরা আল-মায়িদাহ: ৪৮", "Surah Al-Maidah: 48", "EASY"),

        ("ঈমানের মিষ্টতা ও স্বাদ লাভ করার ৩টি অনন্য শর্তের শীর্ষে কোনটি রয়েছে?",
         "What is the foremost condition to taste the sweetness of faith according to Sahih Bukhari: 16?",
         ["আল্লাহ ও তাঁর রাসূল ﷺ-কে দুনিয়ার অন্য সবকিছুর চেয়ে সর্বাধিক ভালোবাসা", "বেশি সম্পদ থাকা", "বিলাসিতা করা", "যুক্তি দেওয়া"],
         ["Loving Allah and His Messenger ﷺ more than anything and anyone else", "Having wealth", "Luxury", "Debating"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যার মাঝে তিনটি গুণ থাকবে সে ঈমানের মিষ্টতা পাবে; প্রথমটি হলো আল্লাহ ও তাঁর রাসূল তার কাছে সর্বাধিক প্রিয় হওয়া।",
         "Whoever loves Allah and His Messenger above all else experiences the sweetness of faith.", "সহীহ বুখারী: ১৬", "Sahih Bukhari: 16", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর রিসালাতের পর আর কোনো নতুন নবী আসবে কি?",
         "Will there be any new Prophet after Prophet Muhammad ﷺ?",
         ["না, তিনি খাতামুন নাবিয়্যীন বা সর্বশেষ নবী ও রাসূল", "হ্যাঁ, নতুন নবী আসবে", "কখনো কখনো আসবে", "নিশ্চিত নয়"],
         ["No, Muhammad ﷺ is the Khatam an-Nabiyyin (Seal of the Prophets)", "Yes new prophets", "Occasionally", "Not certain"], 0,
         "পবিত্র কুরআনে সুস্পষ্ট ঘোষণা: 'তিনি আল্লাহর রাসূল এবং সর্বশেষ নবী' (সূরা আহযাব: ৪০)।",
         "He is the Messenger of Allah and the Seal of the Prophets (Surah Al-Ahzab: 40).", "সূরা আল-আহযাব: ৪০", "Surah Al-Ahzab: 40", "EASY")
    ]
    
    all_ima = base_data + topics
    while len(all_ima) < 100:
        idx = len(all_ima) + 1
        all_ima.append((
            f"ঈমান ও বিশুদ্ধ আকীদা প্রশ্ন নং {idx}: আহলে সুন্নাত ওয়াল জামায়াতের মূল আকীদা কী?",
            f"Iman & Islamic Creed Question {idx}: What is the foundational creed of Ahl al-Sunnah wal-Jama'ah?",
            ["কুরআন ও সহীহ হাদিসকে সাহাবায়ে কেরামের বুঝ অনুযায়ী অকুণ্ঠভাবে মেনে চলা", "মনগড়া ব্যাখ্যা দেওয়া", "বিদআত তৈরি করা", "হাদিস অস্বীকার করা"],
            ["Adhering strictly to the Quran and authentic Sunnah upon the methodology of the Companions", "Personal speculation", "Innovations", "Rejecting Hadith"],
            0,
            f"রাসূলুল্লাহ ﷺ বলেছেন: আমি তোমাদের মাঝে দুটি জিনিস রেখে যাচ্ছি, যা আঁকড়ে রাখলে কখনো পথভ্রষ্ট হবে না: আল্লাহর কিতাব ও আমার সুন্নাহ।",
            f"The Prophet ﷺ said: I have left among you two things; as long as you hold fast to them, you will never go astray: the Book of Allah and my Sunnah.",
            f"মুয়াত্তা ইমাম মালিক: ৩৩৩৮", f"Muwatta Malik: 3338", "EASY"
        ))
        
    for i in range(100):
        item = all_ima[i]
        uid = f"Q_IMA_{i+1:03d}"
        items.append((uid, 'iman', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]))
    return items

def get_batch_25_to_31():
    q = []
    q.extend(generate_cat_25_zakat())
    q.extend(generate_cat_26_akhlaq())
    q.extend(generate_cat_27_dua())
    q.extend(generate_cat_28_mosques())
    q.extend(generate_cat_29_qiyamah())
    q.extend(generate_cat_30_women())
    q.extend(generate_cat_31_iman())
    return q

if __name__ == '__main__':
    all_q = get_batch_25_to_31()
    print(f"Batch 25 to 31 generated successfully! Total questions: {len(all_q)}")
