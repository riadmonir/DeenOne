# -*- coding: utf-8 -*-
"""
Batch 3: Categories 13 to 18 (35 Genuine Authentic Unique Questions Each = 210 Questions)
13. islamic_months
14. shariah_life
15. halal_haram
16. islamic_akhlaq
17. dua_azkar
18. aqeedah_tawheed
"""

def get_cat_13_months():
    items = [
        ("পবিত্র কুরআনে আল্লাহ তাআলা হিজরি সনে মোট কতটি মাস নির্ধারণ করেছেন?", "How many months has Allah ordained in the Islamic Hijri calendar in the Quran?",
         ["১২টি মাস", "১০টি মাস", "১১টি মাস", "১৪টি মাস"], ["12 Months", "10 Months", "11 Months", "14 Months"], 0,
         "সূরা আত-তাওবার ৩৬ নম্বর আয়াতে আল্লাহ বলেন: 'নিশ্চয়ই আল্লাহর নিকট গণনা অনুসারে মাসের সংখ্যা বারোটি।' ", "Indeed, the number of months with Allah is twelve months in the register of Allah (Surah At-Tawbah: 36).", "সূরা আত-তাওবাহ: ৩৬", "Surah At-Tawbah: 36", "EASY"),

        ("ইসলামে পবিত্র ও সম্মানিত চারটি নিষিদ্ধ মাসকে (আশহুরে হুরুম) কী কী?", "Which are the four sacred forbidden months (Ashhur al-Hurum) in Islam?",
         ["যুলকাদা, যুলহিজ্জাহ, মুহররম এবং রজব", "রমজান, শাওয়াল, সফর, শাবান", "রবিউল আউয়াল, রবিউস সানী, জুমাদাল উলা, জুমাদাস সানী", "মুহররম ও রমজান"], ["Dhu al-Qi'dah, Dhu al-Hijjah, Muharram, and Rajab", "Ramadan, Shawwal, Safar, Shaban", "Rabi I, Rabi II, Jumada I, Jumada II", "Muharram & Ramadan"], 0,
         "এই চার মাসে যুদ্ধবিগ্রহ নিষিদ্ধ এবং পাপ ও পুণ্য উভয়ের ফলাফল বহুগুণ বৃদ্ধি পায়।", "Three consecutive (Dhu al-Qi'dah, Dhu al-Hijjah, Muharram) and Rajab.", "সহীহ বুখারী: ৩১৯৭", "Sahih Bukhari: 3197", "EASY"),

        ("মুহররম মাসের ১০ম তারিখকে কী বলা হয় যা ঐতিহাসিক বহু তাৎপর্যে সমৃদ্ধ?", "What is the 10th day of the sacred month of Muharram called?",
         ["আশুরা (Ashura)", "আরাফাত দিবস", "লাইলাতুল কদর", "শবে বরাত"], ["Yawm al-Ashura", "Day of Arafah", "Laylatul Qadr", "Shab-e-Barat"], 0,
         "আশুরার দিনে আল্লাহ তাআলা হযরত মূসা (আ.) ও বনী ইসরাঈলকে ফেরাউনের হাত থেকে মুক্ত করেছিলেন।", "On Ashura, Allah saved Musa (AS) and drowned Pharaoh.", "সহীহ বুখারী: ২০০৪", "Sahih Bukhari: 2004", "EASY"),

        ("আশুরার (১০ই মুহররম) রোজার সাথে কয়টি রোজা রাখা সুন্নাত হিসেবে নির্দেশিত হয়েছে?", "How should the Sunnah fast of Ashura be observed according to Hadith?",
         ["১০ তারিখের সাথে আগে বা পরে মিলিয়ে ২টি রোজা (৯ ও ১০ অথবা ১০ ও ১১ই মুহররম)", "কেবলমাত্র ১টি", "৩টি রোজা", "পুরো মাস"], ["Two days: 9th & 10th or 10th & 11th Muharram", "Only 1 day", "3 days", "Whole month"], 0,
         "রাসূল ﷺ ইহুদিদের বিপরীত করার লক্ষ্যে ৯ ও ১০ই মুহররম রোজা রাখার ইচ্ছা প্রকাশ করেন।", "Fast the 9th and 10th of Muharram to differ from the Jews.", "সহীহ মুসলিম: ১১৩৪", "Sahih Muslim: 1134", "EASY"),

        ("আশুরার দিনের একটিমাত্র নফল রোজার মাধ্যমে আল্লাহ বান্দার কতদিনের গুনাহ মাফ করে দেন?", "What is the immense reward of observing the voluntary fast of Ashura?",
         ["বিগত এক বছরের ছোট গুনাহসমূহ ক্ষমা করে দেওয়া হয়", "১ মাসের গুনাহ", "১০ বছরের গুনাহ", "জীবনের সব গুনাহ"], ["Expiation of all minor sins of the preceding year", "1 month sins", "10 years sins", "Entire life"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আমি আল্লাহর কাছে আশা রাখি আশুরার রোজা পূর্ববর্তী এক বছরের গুনাহের কাফফারা হবে।", "Fasting the day of Ashura expiates the sins of the previous year.", "সহীহ মুসলিম: ১১৬২", "Sahih Muslim: 1162", "EASY"),

        ("রমজান মাসের ঠিক পূর্ববর্তী বরকতময় মাসটির নাম কী যাতে নবীজি ﷺ সবচেয়ে বেশি নফল রোজা রাখতেন?", "Which month immediately precedes Ramadan, during which the Prophet ﷺ fasted most frequently?",
         ["শাবান মাস (Sha'ban)", "রজব মাস", "সফর মাস", "শাওয়াল মাস"], ["Sha'ban", "Rajab", "Safar", "Shawwal"], 0,
         "আয়েশা (রা.) বলেন: রাসূল ﷺ শাবান মাসের মতো এত অধিক নফল রোজা অন্য কোনো মাসে রাখতেন না।", "Aisha (RA) reported that the Prophet ﷺ fasted most in Sha'ban preparing for Ramadan.", "সহীহ বুখারী: ১৯৬৯", "Sahih Bukhari: 1969", "EASY"),

        ("শাওয়াল মাসের কয়টি নফল রোজা রাখলে সারা বছর রোজা রাখার সমান সওয়াব লাভ হয়?", "Fasting how many voluntary days in Shawwal yields the reward of fasting the whole year?",
         ["৬টি রোজা (Shitta Min Shawwal)", "৩টি রোজা", "১০টি রোজা", "৭টি রোজা"], ["6 Fasting Days", "3 Days", "10 Days", "7 Days"], 0,
         "রাসূল ﷺ বলেন: যে রমজানের পর শাওয়ালের ছয়টি রোজা রাখল সে যেন সারা বছর রোজা রাখল।", "Whoever fasts Ramadan and follows it with six days of Shawwal, it is as if he fasted perpetually.", "সহীহ মুসলিম: ১১৬৪", "Sahih Muslim: 1164", "EASY"),

        ("ইসলামে বছরের সর্বশ্রেষ্ঠ ও মোবারকতম ১০টি দিন কোন্ মাসের প্রথম দশক?", "Which are the greatest and most beloved ten days of the entire year in Islam?",
         ["যিলহজ মাসের প্রথম ১০ দিন (১-১০ই যিলহজ)", "রমজানের শেষ ১০ দিন", "মুহররমের প্রথম ১০ দিন", "রজবের ১০ দিন"], ["First ten days of Dhu al-Hijjah", "Last 10 of Ramadan", "First 10 of Muharram", "First 10 of Rajab"], 0,
         "রাসূল ﷺ বলেন: যিলহজের প্রথম ১০ দিনের নেক আমলের চেয়ে প্রিয় অন্য কোনো দিনের আমল আল্লাহর নিকট নেই।", "No good deeds done on other days are superior to those done in the first 10 days of Dhu al-Hijjah.", "সহীহ বুখারী: ৯৬৯", "Sahih Bukhari: 969", "EASY"),

        ("৯ই যিলহজ আরাফাত দিবসের নফল রোজার ফযীলত সম্পর্কে হাদিসে কী এসেছে?", "What is the virtue of fasting on the Day of Arafah (9th Dhu al-Hijjah) for non-pilgrims?",
         ["বিগত এক বছর এবং আগামী এক বছরের (মোট ২ বছরের) গুনাহ মাফ হয়", "১ বছরের গুনাহ", "১০ দিনের গুনাহ", "১ মাসের গুনাহ"], ["Expiates the minor sins of the past year and the coming year (2 Years)", "1 year", "10 days", "1 month"], 0,
         "রাসূল ﷺ বলেন: আরাফাতের দিনের রোজা বিগত বছর ও পরবর্তী বছরের গুনাহের কাফফারা হবে বলে আমি আশা করি।", "Fasting on Arafah expiates sins of the previous year and the coming year.", "সহীহ মুসলিম: ১১৬২", "Sahih Muslim: 1162", "EASY"),

        ("হিজরি সনের কোন্ মাসে পবিত্র হজের আনুষ্ঠানিকতা ও কুরবানি সম্পন্ন হয়?", "In which Islamic month are the rites of Hajj and animal sacrifice (Qurbani) performed?",
         ["যিলহজ মাস (Dhu al-Hijjah)", "যুলকাদা মাস", "রমজান মাস", "মুহররম মাস"], ["Dhu al-Hijjah", "Dhu al-Qi'dah", "Ramadan", "Muharram"], 0,
         "৮ থেকে ১২ই যিলহজ হজের মূল কার্যক্রম এবং ১০ই যিলহজ ঈদুল আজহা ও কুরবানি অনুষ্ঠিত হয়।", "Hajj rites occur from 8th to 12th Dhu al-Hijjah.", "সূরা আল-হাজ্জ", "Surah Al-Hajj", "EASY"),

        ("প্রতি চান্দ্র মাসের ১৩, ১৪ ও ১৫ তারিখের নফল রোজাকে ইসলামী পরিভাষায় কী বলা হয়?", "What are the voluntary fasts of the 13th, 14th, and 15th of each lunar month called?",
         ["আইয়ামে বীজ-এর রোজা (Ayyam al-Beed)", "আশুরার রোজা", "শাওয়ালের রোজা", "কাফফারার রোজা"], ["Ayyam al-Beed (The White Days)", "Ashura", "Shawwal", "Kaffarah"], 0,
         "রাসূল ﷺ সাহাবীদের প্রতি মাসের এই তিন দিন রোজা রাখার নিয়মিত উপদেশ দিতেন।", "The Prophet ﷺ advised fasting the three White Days (13, 14, 15) of every lunar month.", "সহীহ বুখারী: ১৯৮১", "Sahih Bukhari: 1981", "EASY"),

        ("সপ্তাহের কোন্ দুটি বারে রোজা রাখা সুন্নাত কারণ এ দিনগুলোতে আল্লাহর দরবারে বান্দার আমলনামা পেশ করা হয়?", "On which two days of the week is fasting Sunnah as deeds are presented to Allah?",
         ["সোমবার ও বৃহস্পতিবার", "শুক্রবার ও শনিবার", "রবিবার ও মঙ্গলবার", "বুধবার ও বৃহস্পতিবার"], ["Monday and Thursday", "Friday & Saturday", "Sunday & Tuesday", "Wednesday & Thursday"], 0,
         "রাসূল ﷺ বলেন: সোম ও বৃহস্পতিবার আমলনামা পেশ করা হয়, তাই আমি রোযাদার অবস্থায় আমল পেশ পছন্দ করি।", "Deeds are presented on Monday and Thursday, and I love that my deeds be presented while fasting.", "জামে আত-তিরমিযী: ৭৪৭", "Jami at-Tirmidhi: 747", "EASY"),

        ("আইয়ামে তাশরিক (১১, ১২ ও ১৩ই যিলহজ) কোন্ তাকবীর পাঠ করা ওয়াজিব?", "Which Takbir is mandatory to recite after every Fard prayer during the Days of Tashreeq?",
         ["তাকবীরে তাশরিক ('আল্লাহু আকবার আল্লাহু আকবার লা ইলাহা ইল্লাল্লাহু...')", "তাকবীরে তাহরীমা", "তালবিয়া", "দুরুদ"], ["Takbir Tashreeq ('Allahu Akbar, Allahu Akbar, La ilaha illallah...')", "Takbir Tahrimah", "Talbiyah", "Durood"], 0,
         "৯ই যিলহজ ফজর থেকে ১৩ই যিলহজ আসর পর্যন্ত প্রত্যেক ফরজ সালাতের পর তাকবীরে তাশরিক বলা ওয়াজিব।", "Takbir Tashreeq is recited after all 23 Fard prayers from 9th Fajr to 13th Asr.", "সুনানে দারা কুতনী", "Sunan ad-Daraqutni", "EASY"),

        ("বছরের কোন্ ৫টি দিনে রোজা রাখা শরীয়তে সম্পূর্ণ হারাম ও নিষিদ্ধ?", "On which five specific days of the year is fasting strictly prohibited (Haram)?",
         ["ঈদুল ফিতর, ঈদুল আজহা এবং আইয়ামে তাশরিকের ৩ দিন (১১, ১২, ১৩ই যিলহজ)", "জুমার দিন", "আশুরার দিন", "আরাফাতের দিন"], ["Eid al-Fitr, Eid al-Adha, and the 3 Days of Tashreeq (11, 12, 13 Dhu al-Hijjah)", "Friday", "Ashura", "Arafah"], 0,
         "ঈদের দুই দিন এবং তাশরিকের দিনগুলো হলো খাওয়া-দাওয়া ও আনন্দের দিন, তাই রোজা নিষিদ্ধ।", "Fasting is forbidden on the two Eids and the days of Tashreeq.", "সহীহ মুসলিম: ১১৪১", "Sahih Muslim: 1141", "EASY"),

        ("হিজরি বর্ষপঞ্জির দ্বিতীয় মাসের নাম কী যা নিয়ে অজ্ঞতার যুগে অমঙ্গলের কুসংস্কার ছিল?", "What is the 2nd month of the Hijri calendar regarding which pre-Islamic superstitions existed?",
         ["সফর মাস (Safar)", "মুহররম", "রজব", "শাবান"], ["Safar", "Muharram", "Rajab", "Shaban"], 0,
         "রাসূলুল্লাহ ﷺ সাফ বা অমঙ্গলের কুসংস্কার বাতিল ঘোষণা করে বলেন: 'সফর মাসে কোনো অশুভ নেই।' ", "The Prophet ﷺ rejected superstition stating: 'There is no evil omen in Safar.'", "সহীহ বুখারী: ৫৭৭৬", "Sahih Bukhari: 5776", "EASY"),

        ("রজব মাস আগমন করলে রাসূলুল্লাহ ﷺ বরকত ও রমজান প্রাপ্তির জন্য কোন্ দোয়া পাঠ করতেন?", "Which dua did the Prophet ﷺ recite welcoming the month of Rajab?",
         ["'আল্লাহুম্মা বারিক লানা ফী রাজাবা ওয়া শা'বান, ওয়া বাল্লিগনা রামাদান'", "রাব্বানা আতিনা", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ"], ["'Allahumma barik lana fee Rajaba wa Sha'ban, wa ballighna Ramadan'", "Rabbana Atina", "Subhanallah", "Alhamdulillah"], 0,
         "হে আল্লাহ! আমাদের রজব ও শাবান মাসে বরকত দিন এবং আমাদের রমজান পর্যন্ত পৌঁছে দিন।", "O Allah, bless us in Rajab and Sha'ban, and allow us to reach Ramadan.", "মুসনাদে আহমাদ: ২৩৪৬", "Musnad Ahmad: 2346", "EASY"),

        ("রমজান মাসের শেষ দশকের বিজোড় রাতগুলোতে (২১, ২৩, ২৫, ২৭, ২৯) কোন্ মহা বরকতময় রাত অনুসন্ধান করতে হয়?", "Which glorious night is sought in the odd nights of the last 10 days of Ramadan?",
         ["লাইলাতুল কদর (কদরের রাত)", "শবে বরাত", "শবে মেরাজ", "আরাফাতের রাত"], ["Laylatul Qadr (The Night of Decree)", "Shab-e-Barat", "Shab-e-Miraj", "Night of Arafah"], 0,
         "রাসূল ﷺ বলেছেন: তোমরা রমজানের শেষ দশকের বিজোড় রাতগুলোতে লাইলাতুল কদর তালাশ করো।", "Search for Laylatul Qadr in the odd nights of the last ten nights of Ramadan.", "সহীহ বুখারী: ২০২০", "Sahih Bukhari: 2020", "EASY"),

        ("লাইলাতুল কদরের রাতে পাঠ করার জন্য হযরত আয়েশা (রা.)-কে রাসূল ﷺ কোন্ বিশেষ শ্রেষ্ঠ দোয়া শিখিয়েছিলেন?", "Which supplication did the Prophet ﷺ teach Aisha (RA) for Laylatul Qadr?",
         ["'আল্লাহুম্মা ইন্নাকা আফুউন তুহিব্বুল আফওয়া ফা'ফু আন্নী'", "রাব্বানা যালামনা", "সুবহানাল্লাহি ওয়া বিহামদিহি", "লা হাওলা ওয়ালা কুওয়াতা"], ["'Allahumma innaka 'Afuwwun tuhibbul 'afwa fa'fu 'annee'", "Rabbana Zalamna", "Subhanallahi", "La hawla"], 0,
         "হে আল্লাহ! নিশ্চয়ই আপনি পরম ক্ষমাশীল, ক্ষমা করা পছন্দ করেন; অতএব আমাকে ক্ষমা করে দিন।", "O Allah, You are Most Forgiving and You love forgiveness, so forgive me.", "জামে আত-তিরমিযী: ৩৫১৩", "Jami at-Tirmidhi: 3513", "EASY"),

        ("ঈদুল ফিতরের সালাত আদায় করতে যাওয়ার পূর্বে কোন্ আর্থিক ওয়াজিব দান আদায় করা আবশ্যক?", "Which mandatory financial purification must be paid before the Eid al-Fitr prayer?",
         ["সাদাকাতুল ফিতর (ফিতরা)", "বার্ষিক যাকাত", "কুরবানি", "নফল সদকা"], ["Sadaqat al-Fitr (Fitrah)", "Annual Zakat", "Qurbani", "Nafl Charity"], 0,
         "ঈদের নামাজের পূর্বে ফিতরা আদায় করা গরিবদের মুখে হাসি ফোটানোর জন্য ওয়াজিব করা হয়েছে।", "Sadaqat al-Fitr must be discharged before the Eid prayer to feed the poor.", "সহীহ বুখারী: ১৫০৩", "Sahih Bukhari: 1503", "EASY"),

        ("ঈদুল আজহায় কুরবানি করার নির্ধারিত সময়সীমা কতদিন পর্যন্ত স্থায়ী থাকে?", "What is the permissible timeframe for slaughtering the Qurbani animals?",
         ["১০ই যিলহজ ঈদের জামাত থেকে শুরু করে ১২ই যিলহজের সূর্যাস্ত পর্যন্ত (৩ দিন)", "কেবল ঈদের দিন", "পুরো মাস", "৭ দিন"], ["From 10th Dhu al-Hijjah until sunset of 12th Dhu al-Hijjah (3 Days)", "Eid day only", "Whole month", "7 days"], 0,
         "১০, ১১ ও ১২ই যিলহজের সূর্যাস্তের পূর্ব পর্যন্ত পশু কুরবানি করার শরয়ী সময়।", "Sacrifice is valid on the 10th, 11th, and 12th of Dhu al-Hijjah.", "ফিকহুস সুন্নাহ", "Fiqh us-Sunnah", "EASY"),

        ("জিলকদ মাসে যুদ্ধবিগ্রহ নিষিদ্ধ করার অন্যতম মূল প্রাচীন প্রজ্ঞাময় কারণ কী ছিল?", "What was the ancient wisdom behind declaring Dhu al-Qi'dah a sacred forbidden month?",
         ["হজের উদ্দেশ্যে দূর-দূরান্ত থেকে কাফেলা যেন নিরাপদে মক্কায় পৌঁছাতে পারে", "ব্যবসা বন্ধ রাখা", "ঘুমিয়ে থাকা", "উৎসব করা"], ["To allow pilgrims across Arabia to travel safely to Makkah for Hajj without ambush", "Stop trade", "Sleep", "Festivals"], 0,
         "সম্মানিত মাসগুলোতে শান্তির পরিবেশ থাকায় দূরবর্তী দেশের হাজীরা নিরাপদে সফর করতে পারতেন।", "Prohibition of fighting guaranteed peaceful travel for distant Hajj caravans.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "MEDIUM"),

        ("রমজান মাসে দিনে রোজা রাখা ছাড়া রাতে কোন বিশেষ দীর্ঘ জামাত সালাত আদায় করা সুন্নাতে মুয়াক্কাদাহ?", "Which prolonged congregational prayer is Sunnah Mu'akkadah throughout the nights of Ramadan?",
         ["সালাতুত তারাবীহ (Tarawih)", "সালাতুল কুসূফ", "সালাতুল জানাজা", "সালাতুদ দুহা"], ["Salat al-Tarawih", "Salat al-Kusuf", "Salat al-Janazah", "Salat ad-Duha"], 0,
         "রাসূল ﷺ বলেন: যে ব্যক্তি ঈমান ও সওয়াবের আশায় রমজানের রাতে কিয়াম (তারাবীহ) করবে তার পূর্বের সব গুনাহ মাফ হবে।", "Whoever prays during the nights of Ramadan with faith will have his past sins forgiven.", "সহীহ বুখারী: ৩৭", "Sahih Bukhari: 37", "EASY"),

        ("রমজানের শেষ দশকে আল্লাহর সান্নিধ্য লাভের উদ্দেশ্যে মসজিদে একাকী অবস্থানের সুন্নাত আমলকে কী বলে?", "What is the Sunnah practice of secluding oneself in the mosque during the last 10 days of Ramadan called?",
         ["ই'তিকাফ (I'tikaf)", "ইহরাম", "তওয়াফ", "সালাতুল খাওফ"], ["I'tikaf", "Ihram", "Tawaf", "Salat al-Khawf"], 0,
         "রাসূলুল্লাহ ﷺ প্রতি রমজানের শেষ দশকে লাইলাতুল কদর ও নৈকট্য অন্বেষণে মসজিদে ইতিকাফ করতেন।", "The Prophet ﷺ observed I'tikaf in the mosque during the last ten days of Ramadan.", "সহীহ বুখারী: ২০২৫", "Sahih Bukhari: 2025", "EASY"),

        ("চাঁদ দেখে রোজা শুরু এবং চাঁদ দেখে ঈদ উদযাপনের ব্যাপারে রাসূলুল্লাহ ﷺ-এর মূল মূলনীতি কী?", "What is the fundamental principle established by the Prophet ﷺ for starting Ramadan and celebrating Eid?",
         ["'তোমরা চাঁদ দেখে রোজা রাখো এবং চাঁদ দেখে রোজা ভঙ্গ (ঈদ) করো'", "ক্যালেন্ডার দেখে রোজা রাখো", "অনুমান করো", "অন্য দেশের খবর শুনে"], ["'Start fasting when you sight the crescent, and celebrate Eid when you sight it'", "By calendar only", "Guessing", "Foreign news"], 0,
         "মেঘলা থাকলে শাবান বা রমজান ৩০ দিন পূর্ণ করার স্পষ্ট নির্দেশ রয়েছে।", "Fast when you see it (the crescent) and break your fast when you see it.", "সহীহ বুখারী: ১৯০৯", "Sahih Bukhari: 1909", "EASY"),

        ("ইসলামী হিজরি বর্ষপঞ্জির ভিত্তি কিসের গতির ওপর নির্ভরশীল?", "Upon what celestial cycle is the Islamic Hijri calendar based?",
         ["চাঁদের নিখুঁত আবর্তন ও গতির ওপর (চন্দ্র বর্ষপঞ্জি)", "সূর্যের গতির ওপর", "তারার গতির ওপর", "বাতাসের গতির ওপর"], ["The lunar cycle (Lunar Calendar)", "Solar cycle", "Stars", "Wind"], 0,
         "কুরআনে বলা হয়েছে: 'তারা আপনাকে নতুন চাঁদ সম্পর্কে জিজ্ঞেস করে, বলুন এটি মানুষের সময় ও হজের হিসাব।' ", "They ask you about the new moons; say: They are measurements of time for people and Hajj.", "সূরা আল-বাক্বারাহ: ১৮৯", "Surah Al-Baqarah: 189", "EASY"),

        ("ইসলামে সাপ্তাহিক উৎসবের দিন হিসেবে কোন দিনটিকে গরিবদের হজের দিন সমতুল্য বরকত বলা হয়?", "Which day is the weekly festival of Muslims regarded as having immense blessings?",
         ["পবিত্র জুমাবার (শুক্রবার)", "সোমবার", "বৃহস্পতিবার", "শনিবার"], ["Blessed Friday (Jummah)", "Monday", "Thursday", "Saturday"], 0,
         "জুমার দিনে এমন একটি মুহূর্ত রয়েছে যাতে বান্দা আল্লাহর কাছে যা প্রার্থনা করে আল্লাহ তা কবুল করেন।", "Friday contains an hour in which no Muslim asks Allah for good except that He gives it.", "সহীহ মুসলিম: ৮৫২", "Sahih Muslim: 852", "EASY"),

        ("যিলহজ মাসের ১০ তারিখে হাজীগণ মিনায় যে স্থানে পাথর নিক্ষেপ করেন সেই পাথর মারার স্থানসমূহকে কী বলে?", "What are the stone pillars at Mina where pilgrims perform the symbolic Ramy al-Jamarat called?",
         ["জামারাত (Jamarat - উলা, উসতা ও আকাবা)", "সাফা ও মারওয়া", "মাকামে ইব্রাহিম", "হাতেমে কাবা"], ["Jamarat (Jamrah Sughra, Wusta, Aqabah)", "Safa & Marwah", "Maqam Ibrahim", "Hateem"], 0,
         "হযরত ইব্রাহিম (আ.) শয়তানকে যেখানে পাথর মেরে বিতাড়িত করেছিলেন সেখানে প্রতীকী পাথর নিক্ষেপ করা হয়।", "Ramy al-Jamarat commemorates Prophet Ibrahim casting stones at Satan.", "সহীহ মুসলিম: ১২৯৯", "Sahih Muslim: 1299", "EASY"),

        ("ইসলামিক বর্ষপঞ্জির ষষ্ঠ মাসের নাম কী?", "What is the 6th month of the Islamic Hijri calendar?",
         ["জুমাদাস সানী (Jumada ath-Thaniyah)", "জুমাদাল উলা", "রবিউস সানী", "রজব"], ["Jumada ath-Thaniyah", "Jumada al-Ula", "Rabi ath-Thani", "Rajab"], 0,
         "হিজরি মাসের ক্রম: মুহররম, সফর, রবিউল আউয়াল, রবিউস সানী, জুমাদাল উলা, জুমাদাস সানী।", "Jumada ath-Thaniyah is the 6th month of the Hijri year.", "আল-মুহাল্লা", "Al-Muhalla", "EASY"),

        ("ইসলামে শাওয়াল মাসের প্রথম দিন রোজা রাখার ব্যাপারে বিধান কী?", "What is the ruling on fasting on the 1st day of Shawwal (Eid al-Fitr)?",
         ["হারাম ও সম্পূর্ণরূপে নিষিদ্ধ", "ফরজ", "মুস্তাহাব", "মাকরুহ তানযীহী"], ["Haram and strictly prohibited", "Fard", "Mustahab", "Makruh Tanzeehi"], 0,
         "ঈদুল ফিতরের দিনে মুসলিমদের জন্য আল্লাহর পক্ষ থেকে জিয়াফত বা মেহমানদারী থাকে, তাই রোজা নিষিদ্ধ।", "Fasting on Eid al-Fitr (1st Shawwal) is categorically forbidden.", "সহীহ বুখারী: ১৯৯০", "Sahih Bukhari: 1990", "EASY"),

        ("রমজান মাসের পূর্ণ রোজার পর আল্লাহ আমাদের কী আদেশ দিয়েছেন সূরা বাকারার ১৮৫ আয়াতে?", "What does Allah command upon completing the fasts of Ramadan in Surah Al-Baqarah: 185?",
         ["'যেন তোমরা গণনা পূর্ণ করো এবং আল্লাহ তোমাদের যে পথ দেখিয়েছেন সেজন্য তাঁর শ্রেষ্ঠত্ব ঘোষণা করো'", "ঘুমাও", "বাণিজ্য করো", "যুদ্ধ করো"], ["'That you may complete the count and glorify Allah for guiding you' (Surah Al-Baqarah: 185)", "Sleep", "Trade", "Fight"], 0,
         "রমজানের সফল সমাপ্তিতে তাকবীর ধ্বনি দিয়ে ঈদের আনন্দে আল্লাহর শুকরিয়া আদায় করা হয়।", "Completing Ramadan is crowned with Takbirat and sincere thanksgiving to Allah.", "সূরা আল-বাক্বারাহ: ১৮৫", "Surah Al-Baqarah: 185", "EASY"),

        ("ইসলামের ইতিহাসে হিজরি ক্যালেন্ডার প্রবর্তনের সময় বছরের সূচনা কোন্ মাস দিয়ে করার সর্বসম্মত সিদ্ধান্ত হয়?", "Which month was unanimously agreed upon to begin the Hijri year under Caliph Umar?",
         ["পবিত্র মুহররম মাস", "রমজান মাস", "রবিউল আউয়াল মাস", "যিলহজ মাস"], ["Sacred Month of Muharram", "Ramadan", "Rabiul Awwal", "Dhu al-Hijjah"], 0,
         "হজ সম্পন্নের পরপরই মানুষ ঘরে ফেরে এবং নতুন বছরের পবিত্র সূচনা হিসেবে মুহররম নির্ধারিত হয়।", "Muharram follows the conclusion of Hajj and was selected as year opener.", "তারিখুল খুলাফা", "Tarikh al-Khulafa", "EASY"),

        ("কুরআনে বর্ণিত 'লাইলাতুল বারাআত' বা শাবান মাসের মধ্যরজনী সম্পর্কে গ্রহণযোগ্য মত কী?", "What is the balanced authentic view regarding the middle night of Sha'ban (Nisf Sha'ban)?",
         ["আল্লাহ এ রাতে সৃষ্টিজগতের প্রতি রহমতের দৃষ্টি দেন এবং শিরককারী ও হিংসুক ব্যতীত সকলকে ক্ষমা করেন", "কোনো ফযীলত নেই", "সবাই জাহান্নামে যাবে", "হজ করতে হবে"], ["Allah forgives vast numbers of people on this night except idolaters and those harboring malice", "No virtue", "All to Hell", "Must make Hajj"], 0,
         "হাদিসে এসেছে: আল্লাহ শাবানের মধ্যরাতে মুশরিক ও বিদ্বেষপোষণকারী ছাড়া সকল বান্দাকে ক্ষমা করেন।", "Allah looks upon His creation on the middle night of Sha'ban and forgives everyone except mushrik and spiteful.", "সহীহ ইবনে মাজাহ: ১১৩৮", "Sahih Ibn Majah: 1138", "MEDIUM"),

        ("জিলহজ মাসের ৯ তারিখ আরাফাতের ময়দানে অবস্থান না করলে হজের হুকুম কী হয়?", "What happens to the Hajj if a pilgrim fails to stand at Mount Arafat on 9th Dhu al-Hijjah?",
         ["হজ সম্পূর্ণ বাতিল হয়ে যায়, কারণ আরাফাতে অবস্থানই হজের মূল স্তম্ভ", "হজ হবে কিন্তু জরিমানা দিতে হবে", "দাম দিলেই হবে", "ক্ষতি নেই"], ["Hajj is completely invalid as standing at Arafat is the core supreme pillar", "Valid with fine", "Pay money", "No harm"], 0,
         "রাসূলুল্লাহ ﷺ স্পষ্টভাবে বলেছেন: 'আল-হাজ্জু আরাফাহ' (আরাফাতে অবস্থানই হলো হজ)।", "The Prophet ﷺ declared: 'Hajj is Arafah.' Missing it invalidates the pilgrimage.", "জামে আত-তিরমিযী: ৮৮৯", "Jami at-Tirmidhi: 889", "EASY"),

        ("শাওয়াল মাসের ছয়টি রোজা কি একাধারে রাখা বাধ্যতামূলক নাকি বিচ্ছিন্নভাবে রাখা যায়?", "Must the six fasts of Shawwal be kept consecutively or can they be scattered?",
         ["একটানা অথবা মাসের যেকোনো দিন বিচ্ছিন্নভাবে ভাগ ভাগ করে উভয়ভাবেই রাখা বৈধ", "কেবল একটানা রাখতে হবে", "কেবল শেষে রাখতে হবে", "কেবল শুরুতে"], ["Both consecutively and intermittently throughout the month are fully valid", "Consecutively only", "At end only", "At start only"], 0,
         "শাওয়াল মাসের ভেতরে সুবিধাজনক যেকোনো ছয় দিন রোজা সম্পন্ন করলেই পূর্ণ প্রতিদান পাওয়া যাবে।", "Fasting the 6 days of Shawwal consecutively or separately yields the promised reward.", "ফিকহুস সুন্নাহ", "Fiqh us-Sunnah", "EASY"),

        ("পবিত্র রমজান মাস মানবজাতির জন্য কোন্ সর্বশ্রেষ্ঠ উপহার নাযিলের মাস হিসেবে মহাসম্মানিত?", "For what supreme divine revelation is the blessed month of Ramadan exalted above all months?",
         ["মানবজাতির হেদায়াত গ্রন্থ আল-কুরআন নাযিলের মাস", "তাওরাত নাযিলের মাস", "যবুর নাযিলের মাস", "ইনজিল নাযিলের মাস"], ["The revelation of the Holy Quran for human guidance", "Tawrat", "Zabur", "Injeel"], 0,
         "সূরা আল-বাকারায় আল্লাহ বলেন: 'রমজান মাস হলো সেই মাস যাতে কুরআন নাযিল করা হয়েছে মানবজাতির হেদায়াতরূপে।' ", "The month of Ramadan in which was revealed the Quran as guidance for humanity (Surah Al-Baqarah: 185).", "সূরা আল-বাক্বারাহ: ১৮৫", "Surah Al-Baqarah: 185", "EASY")
    ]
    return [('Q_MON_' + str(i+1).zfill(3), 'islamic_months', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_14_shariah():
    items = [
        ("ইসলামী শরীয়তের প্রধান ও মৌলিক চারটি উৎসের (উসূলে ফিকহ) সঠিক ক্রম কোনটি?", "What is the correct sequential order of the 4 primary sources of Islamic Shariah (Usul al-Fiqh)?",
         ["আল-কুরআন, সুন্নাহ (হাদিস), ইজমা (ঐকমত্য) ও কিয়াস (সাদৃশ্যমূলক গবেষণা)", "কিয়াস, ইজমা, হাদিস, কুরআন", "মানুষের রায়, প্রথা, আইন, ফতোয়া", "ইতিহাস, দর্শন, বিজ্ঞান, যুক্তি"], ["The Quran, The Sunnah, Ijma (Consensus), and Qiyas (Analogical Deduction)", "Qiyas, Ijma, Hadith, Quran", "Opinion, Custom, Law, Fatwa", "History, Philosophy, Science, Logic"], 0,
         "সকল ফকিহ ও মুজতাহিদগণের ঐকমত্যে শরীয়তের সিদ্ধান্ত এই চার স্তরীয় উৎসের ভিত্তিতে গৃহীত হয়।", "The four foundational pillars of Islamic jurisprudence are Quran, Sunnah, Ijma, and Qiyas.", "আল-মুসতাসফা", "Al-Mustasfa", "EASY"),

        ("মাক্বাসিদুশ শারীয়াহ (শরীয়তের সর্বোচ্চ ৫টি মৌলিক উদ্দেশ্য) মানুষের কোন্ অধিকারগুলো রক্ষা করে?", "What are the 5 Essential Objectives of Islamic Shariah (Maqasid al-Shariah)?",
         ["দ্বীন/ধর্ম, জীবন/প্রাণ, বুদ্ধি/জ্ঞান, বংশ/সম্মান এবং ধন-সম্পদের সংরক্ষণ", "কেবল ক্ষমতা অর্জন", "যুদ্ধ জয়", "পার্থিব সম্পদ বৃদ্ধি"], ["Preservation of Religion, Life, Intellect, Lineage/Honor, and Property", "Political power", "Warfare", "Wealth accumulation only"], 0,
         "ইসলামী শরীয়তের প্রতিটি আদেশ ও নিষেধ এই পাঁচটি মৌলিক মানব অধিকার সংরক্ষণের ওপর প্রতিষ্ঠিত।", "Every Shariah law serves to protect faith, life, intellect, lineage, and wealth.", "আল-মুওয়াফাকাত", "Al-Muwafaqat", "EASY"),

        ("শরীয়তের পঞ্চবিধ বিধানের (আল-আহকামুল খামসাহ) সঠিক শ্রেণীবিভাগ কোনটি?", "What are the five categories of Islamic legal rulings (Al-Ahkam al-Khamsah)?",
         ["ফরজ/ওয়াজিব, মুস্তাহাব/মানদুব, মুবাহ (বৈধ), মাকরুহ এবং হারাম (নিষিদ্ধ)", "ভালো, মন্দ, মধ্যম", "অনুমোদিত ও শাস্তিযোগ্য", "শাস্তি ও পুরস্কার"], ["Fard/Wajib, Mustahab/Mandub, Mubah (Permissible), Makruh, and Haram (Forbidden)", "Good, Bad, Average", "Allowed & Punished", "Reward & Penalty"], 0,
         "মানবজীবনের প্রতিটি কাজ এই পাঁচটি শরয়ী মূল্যায়নের কোনো না কোনোটির আওতাভুক্ত।", "All human actions fall under Obligatory, Recommended, Permissible, Disliked, or Forbidden.", "রওদাতুন নাযির", "Rawdat an-Nazir", "EASY"),

        ("যে কাজ করলে নিশ্চিত সওয়াব পাওয়া যায় কিন্তু না করলে কোনো গুনাহ হয় না তাকে শরীয়তে কী বলে?", "What is an action termed whose performance is rewarded but omission carries no sin?",
         ["মুস্তাহাব / সুন্নাত / মানদুব (Recommended)", "ফরজ", "ওয়াজিব", "হারাম"], ["Mustahab / Sunnah / Mandub", "Fard", "Wajib", "Haram"], 0,
         "যেমন মেসওয়াক করা, দান করা বা নফল নামাজ আদায় করা মুস্তাহাব ও প্রশংসনীয়।", "Mustahab actions earn reward when done, but carry no sin or punishment if omitted.", "উসূলে ফিকহ", "Usul al-Fiqh", "EASY"),

        ("যে কাজ শরীয়তে সম্পূর্ণ নিষিদ্ধ এবং করলে শাস্তিযোগ্য কবিরা গুনাহ হয় তাকে কী বলে?", "What is a strictly forbidden action in Shariah whose violation incurs severe punishment?",
         ["হারাম (Haram / Prohibited)", "মাকরুহ", "মুবাহ", "মুস্তাহাব"], ["Haram (Prohibited)", "Makruh", "Mubah", "Mustahab"], 0,
         "যেমন সুদ খাওয়া, মিথ্যা বলা, যেনা-ব্যভিচার ও অন্যায় রক্তপাত হারাম।", "Haram refers to explicitly forbidden acts carrying divine punishment.", "আত-তা'রিফাত", "At-Ta'rifat", "EASY"),

        ("যে কাজ করলে কোনো সওয়াব বা গুনাহ নেই, যা স্বাভাবিক ও নিরপেক্ষ তাকে কী বলে?", "What is an action termed that is fundamentally neutral with neither reward nor sin?",
         ["মুবাহ বা হালাল (Mubah / Permissible)", "ফরজ", "মাকরুহ", "হারাম"], ["Mubah / Halal (Neutral / Permissible)", "Fard", "Makruh", "Haram"], 0,
         "যেমন ক্ষুধা পেলে স্বাভাবিক খাবার খাওয়া, পানি পান করা বা হাঁটাচলা করা মুবাহ।", "Mubah actions are intrinsically neutral in Islamic law.", "আল-মুসতাসফা", "Al-Mustasfa", "EASY"),

        ("শরীয়তের কোনো বিষয়ে নতুন সমস্যার সমাধানে কুরআন-সুন্নাহ থেকে গভীর গবেষণামূলক সিদ্ধান্ত বের করার যোগ্যতাকে কী বলে?", "What is the scholarly endeavor of extracting legal rulings from primary texts called?",
         ["ইজতিহাদ (Ijtihad)", "তাকলীদ", "ফতোয়া", "ইস্তিসহাব"], ["Ijtihad", "Taqleed", "Fatwa", "Istishab"], 0,
         "মুজতাহিদ আলেমগণ কুরআন-সুন্নাহর আলোকে আধুনিক যুগোপযোগী সমাধান নির্ণয়ে ইজতিহাদ করেন।", "Ijtihad is the qualified scholarly effort to derive legal solutions for new circumstances.", "রওদাতুন নাযির", "Rawdat an-Nazir", "MEDIUM"),

        ("ইজতিহাদ করার সময় একজন মুজতাহিদ সঠিক সিদ্ধান্তে পৌঁছলে কয়টি সওয়াব লাভ করেন?", "How many rewards does a qualified Mujtahid earn when arriving at a correct ruling?",
         ["২টি সওয়াব (একটি গবেষণার এবং অপরটি সঠিক সিদ্ধান্তের)", "১টি সওয়াব", "১০টি সওয়াব", "কোনো সওয়াব নেই"], ["2 Rewards (One for effort and one for correctness)", "1 Reward", "10 Rewards", "No reward"], 0,
         "রাসূল ﷺ বলেন: বিচারক ইজতিহাদ করে সঠিক হলে দুটি সওয়াব এবং ভুল হলেও একটি সওয়াব পান।", "If a judge makes a ruling through Ijtihad and is right, he gets two rewards; if wrong, one reward.", "সহীহ বুখারী: ৭৩৫২", "Sahih Bukhari: 7352", "EASY"),

        ("ইসলামী শরীয়তের স্বর্ণালী মূলনীতি: 'জরুরি অবস্থা বা জীবন বাঁচানোর বাধ্যবাধকতায় নিষিদ্ধ জিনিস...'?", "What does the vital legal maxim 'Necessity renders prohibited things permissible' mean?",
         ["জীবন রক্ষার সমপরিমাণ সীমিত পরিসরে সাময়িক বৈধ হয় (আদ-দারুরাতু তুবিহুল মাহযূরাত)", "চিরতরে হালাল হয়ে যায়", "কোনো ছাড় নেই", "শাস্তি হবে"], ["Permissible only to the strictly necessary extent to save life", "Permanently halal", "No concession", "Punished"], 0,
         "মরুভূমিতে চরম তৃষ্ণায় জীবন বাঁচানোর বিকল্প না থাকলে হারাম বস্তু জীবন রক্ষার মাপে গ্রহণ বৈধ।", "Severe life-threatening necessity temporarily permits forbidden things strictly to preserve life.", "সূরা আল-বাক্বারাহ: ১৭৩", "Surah Al-Baqarah: 173", "EASY"),

        ("শরীয়তের অন্যতম বিখ্যাত ফিকহী মূলনীতি: 'সকল কাজের ফলাফল নির্ভর করে...'?", "What is the foundational legal maxim derived from Bukhari 1 regarding actions?",
         ["কাজের পেছনের আন্তরিক নিয়ত ও উদ্দেশ্যের ওপর (আল-উমুরু বি মাক্বাসিদিহা)", "বাহ্যিক প্রদর্শনের ওপর", "মানুষের প্রশংসার ওপর", "ফলাফলের ওপর নয়"], ["The underlying intention and purpose (Al-Umooru bi-Maqasidiha)", "Outward show", "Praise", "Not on intent"], 0,
         "ইসলামে উদ্দেশ্য ও নিয়তের বিশুদ্ধতার ওপর আমলের গ্রহণযোগ্যতা ও প্রতিদান নির্ধারিত হয়।", "Matters are judged by their intentions and underlying motivations.", "সহীহ বুখারী: ১", "Sahih Bukhari: 1", "EASY"),

        ("ইসলামী আইনে কোনো ব্যক্তির ওপর ততক্ষণ অপরাধের দায় চাপানো যায় না যতক্ষণ না সে...?", "Under Islamic jurisprudence, an individual is legally unaccountable until they reach...?",
         ["সুস্থ মস্তিষ্কের অধিকারী ও বালেগ (প্রাপ্তবয়স্ক) হয়", "ধনী হয়", "বৃদ্ধ হয়", "শিক্ষিত হয়"], ["Sane, conscious, and reaches the age of puberty (Baligh)", "Becomes rich", "Old", "Educated"], 0,
         "রাসূল ﷺ বলেন: তিন ব্যক্তি থেকে কলম উঠিয়ে রাখা হয়েছে: ঘুমন্ত ব্যক্তি, অপ্রাপ্তবয়স্ক শিশু এবং উন্মাদ ব্যক্তি।", "The pen is lifted from three: the sleeper until he awakes, the child until puberty, and the insane until sanity.", "সুনানে আবু দাউদ: ৪৪০৩", "Sunan Abi Dawud: 4403", "EASY"),

        ("ইসলামে বিচারক বা বিচারকের পদে আসীন ব্যক্তির জন্য রাগান্বিত বা ক্ষুব্ধ অবস্থায় বিচার করার বিধান কী?", "What is the strict ruling on a judge passing a verdict in a state of anger?",
         ["হারাম ও নিষিদ্ধ; রাগের সময় বিচারিক রায় দেওয়া বৈধ নয়", "মুস্তাহাব", "ওয়াজিব", "উত্তম"], ["Forbidden (Haram); a judge must never issue verdicts while angry", "Mustahab", "Wajib", "Good"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: কোনো বিচারক যেন ক্রোধাম্বিত বা রাগান্বিত অবস্থায় দুই পক্ষের মাঝে বিচার না করে।", "No judge should pass a judgment between two people while he is angry.", "সহীহ বুখারী: ৭১৫৮", "Sahih Bukhari: 7158", "EASY"),

        ("ইসলামী আদালতে কারো বিরুদ্ধে অভিযোগ আনলে প্রমাণের মূল দায়িত্ব কার ওপর বর্তায়?", "In Islamic judicial procedure, upon whom does the primary burden of proof rest?",
         ["অভিযোগকারী বা বাদীর ওপর প্রমাণ আনা আবশ্যক এবং অস্বীকারকারী শপথ করবে", "বিবাদীর ওপর", "বিচারকের ওপর", "দর্শকদের ওপর"], ["The claimant must provide proof (Bayyinah), and the defendant takes an oath", "On defendant", "On judge", "On spectators"], 0,
         "রাসূল ﷺ বলেন: 'প্রমাণ উপস্থাপন করা বাদীর দায়িত্ব এবং শপথ করা অস্বীকারকারীর দায়িত্ব।' ", "The burden of proof is on the claimant, and the oath is upon the one who denies.", "বায়হাকী: সুনানুল কুবরা", "Sunan al-Bayhaqi", "EASY"),

        ("ইসলামী শরীয়তে মিথ্যা সাক্ষ্য (শাহাদাতুয যূর) দেওয়ার অপরাধের ভয়াবহতা কত মারাত্মক?", "How severe is giving false witness/testimony (Shahadat az-Zoor) in Islamic law?",
         ["সবচেয়ে মারাত্মক কবিরা গুনাহসমূহের অন্যতম এবং শিরকের সমতুল্য পাপ", "ছোট গুনাহ", "সাধারণ ভুল", "ক্ষমাযোগ্য"], ["One of the most heinous major sins, equated directly to idolatry (Shirk)", "Minor sin", "Ordinary mistake", "Pardonable"], 0,
         "রাসূল ﷺ হেলান দেওয়া থেকে সোজা হয়ে বসে বারবার সতর্ক করেন: 'সাবধান! মিথ্যা সাক্ষ্য দেওয়া মারাত্মক ধ্বংসাত্মক।' ", "The Prophet ﷺ sat upright warning repeatedly against false testimony.", "সহীহ বুখারী: ২৬৫৪", "Sahih Bukhari: 2654", "EASY"),

        ("ইসলামী শরীয়তে চুক্তিবদ্ধ অমুসলিম নাগরিকদের (জিম্মি) নিরাপত্তা ও অধিকার রক্ষার ব্যাপারে রাসূল ﷺ কী বলেছেন?", "What did Prophet Muhammad ﷺ declare regarding the rights of non-Muslim citizens (Dhimmi)?",
         ["'যে ব্যক্তি কোনো চুক্তিবদ্ধ অমুসলিমকে হত্যা করবে সে জান্নাতের সুঘ্রাণও পাবে না'", "তাদের কোনো অধিকার নেই", "তারা বহিষ্কৃত", "তারা দাস"], ["'Whoever kills an allied non-Muslim citizen will not even smell the fragrance of Paradise'", "No rights", "Expelled", "Slaves"], 0,
         "ইসলাম অমুসলিম নাগরিকদের জান, মাল ও ধর্মীয় স্বাধীনতার পূর্ণ নিশ্চয়তা প্রদান করে।", "Whoever harms a peaceful non-Muslim citizen, I will be his opponent on Judgment Day.", "সহীহ বুখারী: ৩১৬৬", "Sahih Bukhari: 3166", "EASY"),

        ("ইসলামী পারিবারিক আইনে বিবাহের অন্যতম মৌলিক শর্ত হিসেবে মোহরানা (Mahr) কার একক ব্যক্তিগত অধিকার?", "In Islamic marital law, whose exclusive and untouchable personal right is the Mahr (Dower)?",
         ["একান্তভাবেই স্ত্রীর নিজস্ব ব্যক্তিগত সম্পত্তি ও অধিকার", "পিতার সম্পত্তি", "স্বামীর ফেরত নেওয়ার অধিকার", "শ্বশুরের"], ["Exclusively the wife's sole personal property and entitlement", "Father's property", "Husband's return", "In-laws"], 0,
         "কুরআনে আল্লাহ বলেন: 'তোমরা নারীদের তাদের মোহরানা সন্তুষ্টচিত্তে উপহার হিসেবে প্রদান করো।' ", "And give the women their dower with a good heart as a free gift (Surah An-Nisa: 4).", "সূরা আন-নিসা: ৪", "Surah An-Nisa: 4", "EASY"),

        ("ইসলামে শ্রমিকের অধিকার রক্ষায় তার পারিশ্রমিক পরিশোধের ব্যাপারে রাসূল ﷺ-এর ঐতিহাসিক নির্দেশনা কী?", "What is the famous prophetic mandate regarding paying a laborer his wages?",
         ["'শ্রমিকের গায়ের ঘাম শুকানোর পূর্বেই তার পারিশ্রমিক পরিশোধ করে দাও'", "এক মাস পর", "কাজ শেষে কাল দেবে", "কমিয়ে দাও"], ["'Pay the laborer his wages before his sweat dries'", "After 1 month", "Tomorrow", "Reduce it"], 0,
         "শ্রমিকের হক নষ্টকারী ব্যক্তির বিরুদ্ধে কিয়ামতের দিন আল্লাহ স্বয়ং বাদী হিসেবে দাঁড়াবেন।", "Give the worker his wages before his sweat dries.", "সুনানে ইবনে মাজাহ: ২৪৪৩", "Sunan Ibn Majah: 2443", "EASY"),

        ("ইসলামী শরীয়তে আমানতদারিতা ও চুক্তি পালনের ব্যাপারে কুরআনের স্পষ্ট নির্দেশ কী?", "What is the strict Quranic obligation regarding fulfilling contracts and covenants?",
         ["'হে মুমিনগণ! তোমরা অঙ্গীকারসমূহ ও চুক্তি সম্পূর্ণরূপে পূর্ণ করো'", "ইচ্ছা হলে ভাঙো", "মুনাফা না হলে বাদ দাও", "অপ্রয়োজনীয়"], ["'O you who have believed, fulfill all contracts and commitments' (Surah Al-Ma'idah: 1)", "Break if wished", "Discard if no profit", "Unnecessary"], 0,
         "চুক্তি ভঙ্গ করা ও ওয়াদা খেলাফ করা মুনাফিকের অন্যতম প্রধান লক্ষণ।", "Fulfilling covenants is a core obligation of true faith.", "সূরা আল-মায়িদাহ: ১", "Surah Al-Ma'idah: 1", "EASY"),

        ("ইসলামী শরীয়তের দৃষ্টিতে কোনো প্রাণী বা প্রাণীর চেহারায় আঘাত করা বা আগুনে পুড়িয়ে মারা কেমন?", "What is the Shariah ruling on striking animals on the face or torturing them with fire?",
         ["হারাম ও সম্পূর্ণরূপে নিষিদ্ধ", "জায়েজ", "মাকরুহ", "মুস্তাহাব"], ["Strictly Haram and severely cursed in Hadith", "Permissible", "Makruh", "Mustahab"], 0,
         "রাসূল ﷺ প্রাণীর মুখে আঘাত করতে নিষেধ করেছেন এবং বলেন: আগুন দিয়ে শাস্তি দেওয়ার অধিকার একমাত্র আগুনের রবের।", "The Prophet ﷺ forbade branding or striking the face of animals.", "সহীহ মুসলিম: ২১১৭", "Sahih Muslim: 2117", "EASY"),

        ("ইসলামী আইনে 'সাদাকাতুল জারিয়াহ' (চলমান সদকা) বলতে কোন্ ধরণের পরোপকারী দানকে বোঝায়?", "What does 'Sadaqah Jariyah' (Continuous / Perpetual Charity) signify in Islamic law?",
         ["যে দানের কল্যাণ মানুষের মৃত্যুর পরও দীর্ঘকাল চলমান থাকে (যেমন: মসজিদ, কূপ, উপকারী জ্ঞান ও নেক সন্তান)", "এককালীন খাদ্য", "পকেট খরচ", "কাপড় দেওয়া"], ["Charitable endowments that continuously benefit humanity after one's death (Mosque, water well, beneficial knowledge)", "One-time food", "Pocket money", "Clothing"], 0,
         "মানুষের মৃত্যুর সাথে সাথে সকল আমল বন্ধ হয় তিনটি ব্যতীত: সদকায়ে জারিয়া, উপকারী জ্ঞান ও দোয়াকারী নেক সন্তান।", "When a human dies, his deeds cease except three: ongoing charity, beneficial knowledge, or a righteous child who prays.", "সহীহ মুসলিম: ১৬৩১", "Sahih Muslim: 1631", "EASY"),

        ("ইসলামে রাষ্ট্রীয় বা যৌথ মালিকানাধীন সম্পদ আত্মসাৎ বা অপচয় করার বিধান কী?", "What is the ruling on embezzling or misappropriating public funds and state property (Ghulool)?",
         ["চরম কবিরা গুনাহ ও কিয়ামতের দিন সেই সম্পদ কাঁধে তুলে লাঞ্ছিত হওয়ার কারণ", "জায়েজ", "ছোট ভুল", "ক্ষতি নেই"], ["A catastrophic major sin (Ghulool) resulting in public disgrace on Judgment Day", "Permissible", "Minor error", "No harm"], 0,
         "কুরআনে বলা হয়েছে: যে আত্মসাৎ করবে সে কিয়ামতের দিন আত্মসাৎকৃত বস্তু নিয়ে হাজির হবে।", "Whoever embezzles public property will bring forth what he embezzled on the Day of Resurrection.", "সূরা আলে ইমরান: ১৬১", "Surah Ali Imran: 161", "EASY"),

        ("ইসলামে চিকিৎসাসেবা গ্রহণ ও ঔষধ ব্যবহারের ব্যাপারে রাসূলুল্লাহ ﷺ-এর সুস্পষ্ট বিধান কী?", "What is the prophetic guidance on seeking medical treatment and utilizing medicine?",
         ["'তোমরা চিকিৎসা গ্রহণ করো, কারণ আল্লাহ এমন কোনো রোগ দেননি যার নিরাময় সৃষ্টি করেননি'", "ঔষধ খাওয়া নিষেধ", "কেবল হাত দেখা", "ঝাড়ফুঁক যথেষ্ট"], ["'Seek medical treatment, for Allah has not created a disease without creating its cure'", "Medicine forbidden", "Palmistry", "Exorcism only"], 0,
         "চিকিৎসা গ্রহণ করা তাওয়াক্কুলের পরিপন্থী নয়, বরং এটি সুন্নাহ ও শরীয়তের অন্যতম নির্দেশ।", "Allah sent down the disease and the cure; therefore treat sickness with lawful medicine.", "সুনানে আবু দাউদ: ৩৮৫৫", "Sunan Abi Dawud: 3855", "EASY"),

        ("পরিবেশ সংরক্ষণ ও বৃক্ষরোপণের অসীম সওয়াব বর্ণনা করে রাসূলুল্লাহ ﷺ কী বলেছেন?", "What did the Prophet ﷺ state regarding the perpetual reward of planting trees and agriculture?",
         ["যে কোনো মুসলিম গাছ লাগায়, অতঃপর তা থেকে মানুষ বা পশু যা খায় তা তার জন্য চলমান সদকা হয়", "সাধারণ কাজ", "সময় নষ্ট", "পাপ"], ["Whatever is eaten from it by human, bird, or animal is counted as continuous Sadaqah", "Ordinary", "Waste of time", "Sin"], 0,
         "এমনকি কিয়ামত সংঘটিত হওয়ার মুহূর্তেও হাতে চারাগাছ থাকলে তা রোপণ করার নির্দেশ দিয়েছেন নবীজি ﷺ।", "Even if the Final Hour strikes while holding a sapling, plant it.", "সহীহ বুখারী: ২৩২০", "Sahih Bukhari: 2320", "EASY"),

        ("ইসলামে গোপন দোষ খোঁজা (তাজাসসুস) এবং অন্যের ঘরে অনুমতি ছাড়া উঁকি মারার বিধান কী?", "What is the Shariah ruling on spying (Tajassus) and intruding into others' private lives?",
         ["হারাম ও অন্যের ব্যক্তিগত গোপনীয়তা রক্ষা করা বাধ্যতামূলক", "জায়েজ", "মুস্তাহাব", "ভালো কাজ"], ["Strictly Haram; upholding personal privacy is an inviolable Islamic right", "Permissible", "Mustahab", "Good deed"], 0,
         "সূরা হুজুরাতে আল্লাহ বলেন: 'তোমরা একে অপরের গোপনীয় বিষয় অনুসন্ধান করো না।' ", "And do not spy or backbite each other (Surah Al-Hujurat: 12).", "সূরা আল-হুজুরাত: ১২", "Surah Al-Hujurat: 12", "EASY"),

        ("ইসলামী শরীয়তে ঋণ (Debt) পরিশোধের ব্যাপারে অবহেলা করার ভয়াবহতা কতটা কঠোর?", "How severely did the Prophet ﷺ warn regarding neglecting to repay debts?",
         ["আল্লাহর রাস্তায় শহীদ ব্যক্তিরও সকল গুনাহ মাফ হলেও অপরিশোধিত ঋণের দায় মাফ হয় না", "মাফ হয়ে যায়", "কোনো সমস্যা নেই", "ক্ষমাযোগ্য"], ["Even a martyr in battle has all sins forgiven except unpaid debt", "Forgiven", "No problem", "Pardonable"], 0,
         "রাসূল ﷺ ঋণগ্রস্ত মৃত ব্যক্তির জানাজা আদায় করতেন না যতক্ষণ না কেউ তার ঋণের দায়িত্ব নিত।", "All sins of a martyr are forgiven except debt.", "সহীহ মুসলিম: ১৮৮৬", "Sahih Muslim: 1886", "EASY"),

        ("ধনী ও সক্ষম ব্যক্তির জন্য পাওনাদারের পাওনা পরিশোধে অহেতুক টালবাহানা বা বিলম্ব করাকে কী বলা হয়েছে?", "What did the Prophet ﷺ term a rich debtor delaying payment to creditors unjustly?",
         ["জুলুম (অবিচার ও পাপ)", "ব্যবসা কৌশল", "জায়েজ", "বুদ্ধিমানি"], ["Zulm (Oppression and Sin)", "Trade tactic", "Permissible", "Smartness"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: সক্ষম ধনী ব্যক্তির ঋণ পরিশোধে কালক্ষেপণ করা সুস্পষ্ট জুলুম।", "Procrastination in paying debts by a wealthy person is injustice.", "সহীহ বুখারী: ২৪০০", "Sahih Bukhari: 2400", "EASY"),

        ("ইসলামী শরীয়তে উত্তরাধিকার সম্পত্তি (উত্তরাধিকার আইন) বণ্টনের সুস্পষ্ট অনুপাত কে নির্ধারণ করেছেন?", "Who designated the precise mathematical shares of inheritance in Islamic Shariah?",
         ["স্বয়ং আল্লাহ তাআলা পবিত্র কুরআনের সূরা নিসায়", "মানুষের ইচ্ছা", "বিচারকের পছন্দ", "রাষ্ট্রনেতা"], ["Allah Almighty Himself directly in Surah An-Nisa", "Human desire", "Judge choice", "Ruler"], 0,
         "উত্তরাধিকার আইনের অংশসমূহ আল্লাহ সুনির্দিষ্ট করে দিয়েছেন, এতে কোনো মানুষের পরিবর্তন করার অধিকার নেই।", "Allah ordained the exact shares of inheritance as divine limits (Surah An-Nisa: 11-14).", "সূরা আন-নিসা: ১১", "Surah An-Nisa: 11", "EASY"),

        ("কোনো ব্যক্তির জন্য তার জীবদ্দশায় মোট সম্পত্তির কত অংশের বেশি ওসিয়ত (উইল) করা নিষিদ্ধ?", "What is the maximum limit of estate a person is permitted to bequeath in a will (Wasiyyah)?",
         ["সর্বোচ্চ এক তৃতীয়াংশ (১/৩ অংশ) এবং তা কোনো উত্তরাধিকারীর জন্য হতে পারবে না", "সম্পূর্ণ সম্পদ", "অর্ধেক", "দুই তৃতীয়াংশ"], ["At most one-third (1/3), and not for an existing legal heir", "All estate", "Half", "Two-thirds"], 0,
         "রাসূল ﷺ সা'দ (রা.)-কে বলেন: এক তৃতীয়াংশ ওসিয়ত করো, আর এক তৃতীয়াংশই অনেক বেশি।", "The third, and a third is much; better to leave your heirs wealthy than poor.", "সহীহ বুখারী: ২৭৪৪", "Sahih Bukhari: 2744", "EASY"),

        ("ইসলামী আইনে কোনো উত্তরাধিকারীর নামে অতিরিক্ত ওসিয়ত করার ব্যাপারে রাসূল ﷺ-এর সুস্পষ্ট নিষেধাজ্ঞা কী?", "What did the Prophet ﷺ state regarding making an extra will for an existing legal heir?",
         ["'নিশ্চয়ই কোনো উত্তরাধিকারীর জন্য কোনো অতিরিক্ত ওসিয়ত নেই (লা ওয়াসিয়্যাতা লি ওয়ারিস)'", "করা যাবে", "জায়েজ", "উত্তম"], ["'There is no bequest (will) for an heir' (La Wasiyyata li-Waarith)", "Allowed", "Permissible", "Good"], 0,
         "আল্লাহ প্রত্যেক উত্তরাধিকারীর অংশ নির্ধারণ করে দিয়েছেন, তাই তাদের জন্য অতিরিক্ত ওসিয়ত বাতিল।", "Allah has given every rightful heir their due share, so no bequest is allowed for an heir.", "জামে আত-তিরমিযী: ২১২০", "Jami at-Tirmidhi: 2120", "EASY"),

        ("ইসলামে মিথ্যা শপথ (ইয়ামীনুল গামূস) করে অন্যের হক আত্মসাৎ করার পরিণতি কী?", "What is the consequence of taking a false oath (Yameen Ghamoos) to seize another's property?",
         ["জাহান্নামের আগুনে নিমজ্জিত হওয়ার কারণ এবং ক্ষমার অযোগ্য কবিরা গুনাহ", "জায়েজ", "ছোট ভুল", "উপকারী"], ["Plunges the perjurer directly into Hellfire and is an unforgivable perjury", "Permissible", "Minor error", "Beneficial"], 0,
         "মিথ্যা শপথ গ্রহণকারী মূলত নিজেকে জেনেশুনে জাহান্নামের অতল গহ্বরে নিক্ষেপ করে।", "Whoever takes an intentional false oath to seize Muslim wealth will meet Allah in wrath.", "সহীহ বুখারী: ৬৬৫৯", "Sahih Bukhari: 6659", "EASY"),

        ("ইসলামে কোনো অপরাধ সংঘটিত হতে দেখলে প্রতিটি সক্ষম মুসলিমের সামাজিক দায়িত্ব কী?", "What is the proactive duty of every Muslim upon witnessing an evil act in society?",
         ["হাত দিয়ে প্রতিহত করবে, সম্ভব না হলে মুখে প্রতিবাদ করবে, তাও না পারলে অন্তরে ঘৃণা করবে", "উপভোগ করবে", "নীরব থাকবে", "ছবি তুলবে"], ["Change it with hand, or with tongue, or at minimum hate it in heart (weakest faith)", "Enjoy it", "Stay silent", "Take photos"], 0,
         "অসৎকাজে বাধা প্রদান ও সৎকাজে আদেশ দেওয়া ইসলামী সমাজ গঠনের অপরিহার্য বৈশিষ্ট্য।", "Whoever sees an evil, let him change it with his hand, tongue, or heart.", "সহীহ মুসলিম: ৪৯", "Sahih Muslim: 49", "EASY"),

        ("ইসলামী শরীয়তের দৃষ্টিতে অপব্যয়ী ও অপচয়কারীকে কুরআনে কার ভাই বলে অভিহিত করা হয়েছে?", "Whose brothers are wasteful spenders (Mubadhdhireen) described as in Surah Al-Isra?",
         ["শয়তানের ভাই (ইখওয়ানুশ শায়াতীন)", "ফেরেশতার ভাই", "মানুষের ভাই", "পশুর ভাই"], ["Brothers of the Devils (Ikhwan ash-Shayateen)", "Brothers of angels", "Brothers of humans", "Brothers of beasts"], 0,
         "সূরা বনী ইসরাঈলে আল্লাহ বলেন: নিশ্চয়ই অপচয়কারীরা শয়তানের ভাই এবং শয়তান তার রবের প্রতি চরম অকৃতজ্ঞ।", "Indeed, the wasteful are brothers of the devils, and Satan is ungrateful to his Lord.", "সূরা আল-ইসরা: ২৭", "Surah Al-Isra: 27", "EASY"),

        ("ইসলামে হালাল খাদ্য ও হালাল রুজি উপার্জনের গুরুত্ব সালাত ও ইবাদতের কবুলিয়তের ক্ষেত্রে কেমন?", "How vital is Halal sustenance to the acceptance of prayers and worship in Islam?",
         ["হারাম খাদ্য ভক্ষণকারীর সালাত, রোজা ও কোনো দোয়াই আল্লাহর দরবারে কবুল হয় না", "সাধারণ বিষয়", "প্রভাব নেই", "ঐচ্ছিক"], ["Consuming Haram invalidates the acceptance of all supplications and worship", "Normal", "No effect", "Optional"], 0,
         "রাসূল ﷺ বলেন: তার খাদ্য হারাম, পানীয় হারাম, পোশাক হারাম; তবে তার দোয়া কীভাবে কবুল হতে পারে?", "His food is haram, his drink is haram, so how could his prayer be answered?", "সহীহ মুসলিম: ১০১৫", "Sahih Muslim: 1015", "EASY"),

        ("ইসলামী শরীয়তে রাস্তাঘাট থেকে কষ্টদায়ক বস্তু (যেমন: পাথর, কাঁটা বা ময়লা) অপসারণ করার মর্যাদা কী?", "What is the spiritual reward of removing harmful objects (thorns, stones) from pathways?",
         ["ঈমানের অন্যতম শাখা এবং সদকার সমতুল্য মহা পুণ্যময় আমল", "সাধারণ কাজ", "অপমানজনক", "ক্ষতি"], ["A branch of true faith and counted as a meritorious act of Sadaqah", "Ordinary", "Humiliating", "Harm"], 0,
         "রাসূল ﷺ বলেন: ঈমানের সত্তরের অধিক শাখা রয়েছে, যার সর্বোচ্চ হলো লা ইলাহা ইল্লাল্লাহ এবং সর্বনিম্ন রাস্তা থেকে কষ্টদায়ক বস্তু সরানো।", "Faith has over seventy branches; the highest is Tawheed and the lowest is removing harm from the road.", "সহীহ মুসলিম: ৩৫", "Sahih Muslim: 35", "EASY"),

        ("ইসলামী শরীয়তের সার্বিক উদ্দেশ্য বিশ্বমানবতার জন্য কী প্রতিষ্ঠা করা?", "What is the overarching universal mission of Islamic Shariah for humanity?",
         ["ন্যায়বিচার, সাম্য, শান্তি, নৈতিক উৎকর্ষ ও উভয় জাহানের চিরন্তন কল্যাণ", "কেবল আনুষ্ঠানিকতা", "শাসন বিস্তার", "যুদ্ধ"], ["Justice, equality, peace, moral perfection, and eternal success in both worlds", "Only rituals", "Expansion", "War"], 0,
         "ইসলাম মানুষকে অন্ধকার থেকে আলোতে এবং অন্যায় ও জুলুম থেকে ইনসাফের শীতল ছায়ায় পরিচালিত করে।", "Islamic Shariah establishes universal justice, compassion, and divine mercy across creation.", "সূরা আল-আম্বিয়া: ১০৭", "Surah Al-Anbiya: 107", "EASY")
    ]
    return [('Q_SHR_' + str(i+1).zfill(3), 'shariah_life', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_15_halal():
    items = [
        ("খাদ্যদ্রব্যের ক্ষেত্রে ইসলামী শরীয়তের মৌলিক মূলনীতি (আসল) কী?", "What is the fundamental default legal principle regarding foods in Islamic law?",
         ["সকল পবিত্র ও উপকারী খাবার মূলত হালাল, যতক্ষণ না স্পষ্ট দলিলের মাধ্যমে তা হারাম প্রমাণিত হয়", "সবকিছুই হারাম", "কিছু খাওয়া যাবে না", "পশুর মাংস নিষিদ্ধ"], ["All pure and wholesome foods are inherently Halal unless explicitly prohibited", "Everything is haram", "Nothing allowed", "Meat forbidden"], 0,
         "আল্লাহ তাআলা পবিত্র জিনিসসমূহকে মানবজাতির জন্য হালাল এবং অপবিত্র ক্ষতিকর জিনিসকে হারাম করেছেন।", "He makes lawful for them the good things and prohibits for them the evil (Surah Al-A'raf: 157).", "সূরা আল-আ'রাফ: ১৫৭", "Surah Al-A'raf: 157", "EASY"),

        ("সূরা আল-মায়িদাহর ৩ নম্বর আয়াতে সুস্পষ্টভাবে ঘোষিত হারাম খাবারসমূহ কী কী?", "Which forbidden meats are explicitly enumerated in Surah Al-Ma'idah verse 3?",
         ["মৃত প্রাণী, প্রবাহিত রক্ত, শূকরের গোশত এবং আল্লাহ ছাড়া অন্যের নামে উৎসর্গকৃত পশু", "মাছ ও হাঁস", "গরু ও ছাগল", "উটের মাংস"], ["Dead animals (carrion), blood, swine flesh, and that slaughtered in other than Allah's name", "Fish & duck", "Cow & goat", "Camel meat"], 0,
         "গলা চিপে মারা, আঘাতপ্রাপ্ত হয়ে মরা বা হিংস্র প্রাণীর খাওয়া অংশ খাওয়াও হারাম।", "Prohibited to you are dead animals, blood, the flesh of swine, and dedicated to other than Allah.", "সূরা আল-মায়িদাহ: ৩", "Surah Al-Ma'idah: 3", "EASY"),

        ("পশু জবাই করার সময় কোন্ শর্তটি পূরণ না করলে পশুর গোশত খাওয়া হালাল হয় না?", "Which condition is mandatory during animal slaughtering for the meat to be Halal?",
         ["ধারালো অস্ত্র দিয়ে আল্লাহর নাম (বিসমিল্লাহি আল্লাহু আকবার) বলে কণ্ঠনালী ও রক্তনালী কেটে রক্ত প্রবাহিত করা", "লাঠি দিয়ে মারা", "বিদ্যুৎ দেওয়া", "মাথায় আঘাত"], ["Severing the jugular veins with a sharp blade invoking Allah's name (Bismillah)", "Sticking", "Electrocution", "Blow to head"], 0,
         "বিসমিল্লাহ বলে রক্ত প্রবাহিত না করলে সেই পশু মৃত প্রাণীর অন্তর্ভুক্ত এবং খাওয়া হারাম।", "Eat of that upon which the name of Allah has been mentioned (Surah Al-An'am: 118).", "সূরা আল-আন'আম: ১১৮", "Surah Al-An'am: 118", "EASY"),

        ("সামুদ্রিক ও জলজ প্রাণীদের ব্যাপারে রাসূলুল্লাহ ﷺ-এর সুস্পষ্ট শরয়ী ফায়সালা কী?", "What did Prophet Muhammad ﷺ declare regarding the purity of sea water and marine life?",
         ["'সমুদ্রের পানি সম্পূর্ণ পবিত্র এবং এর মৃত প্রাণীও হালাল ও ভক্ষণযোগ্য'", "সামুদ্রিক মাছ হারাম", "সমুদ্র অপবিত্র", "পানি খাওয়া নিষেধ"], ["'Its water is pure and purifying, and its dead creatures (seafood) are Halal'", "Seafood haram", "Impure", "Forbidden"], 0,
         "সমুদ্রের সকল মাছ ও জলজ প্রাণী জবাই ছাড়াই স্বাভাবিকভাবে হালাল ও পবিত্র।", "Regarding the sea: Its water is purifying and its dead are lawful to eat.", "সুনানে আবু দাউদ: ৮৩", "Sunan Abi Dawud: 83", "EASY"),

        ("স্থলভাগের কোন্ দুটি মৃত প্রাণী জবাই ছাড়াই খাওয়াকে ইসলামে বিশেষভাবে হালাল করা হয়েছে?", "Which two dead creatures are made uniquely lawful to consume without slaughter in Hadith?",
         ["মৃত মাছ এবং পঙ্গপাল (টিড্ডি)", "মৃত মুরগি ও হাঁস", "মৃত হরিণ", "মৃত ভেড়া"], ["Dead fish and locusts", "Dead chicken", "Dead deer", "Dead sheep"], 0,
         "রাসূল ﷺ বলেন: আমাদের জন্য দুটি মৃত প্রাণী হালাল করা হয়েছে: মাছ ও পঙ্গপাল; এবং দুটি রক্ত: কলিজা ও প্লীহা।", "Two dead animals are made lawful for us: fish and locusts; and two bloods: liver and spleen.", "সুনানে ইবনে মাজাহ: ৩৩১৪", "Sunan Ibn Majah: 3314", "EASY"),

        ("স্থলভাগের হিংস্র প্রাণী যাদের ধারালো শিকারী দাঁত (Caniens) আছে তাদের গোশত খাওয়ার বিধান কী?", "What is the ruling on eating the meat of terrestrial beasts of prey with fangs (Canines)?",
         ["হারাম ও সম্পূর্ণরূপে নিষিদ্ধ (যেমন: বাঘ, সিংহ, নেকড়ে, কুকুর, বিড়াল)", "হালাল", "মুস্তাহাব", "মাকরুহ তানযীহী"], ["Haram and strictly forbidden (e.g., tigers, lions, wolves, dogs, cats)", "Halal", "Mustahab", "Makruh"], 0,
         "রাসূলুল্লাহ ﷺ শিকারী দাঁতওয়ালা হিংস্র চতুষ্পদ জন্তু এবং শিকারী নখরযুক্ত পাখি খাওয়া নিষিদ্ধ করেছেন।", "The Prophet ﷺ forbade eating any fanged beast of prey and taloned bird.", "সহীহ মুসলিম: ১৯৩৪", "Sahih Muslim: 1934", "EASY"),

        ("পাখিদের মধ্যে যেসব পাখির শিকার ধরার ধারালো নখ (Talons) আছে তাদের খাওয়ার বিধান কী?", "What is the ruling on eating predatory birds with talons (hunting claws)?",
         ["হারাম ও নিষিদ্ধ (যেমন: ঈগল, বাজপাখি, চিল, শকুন, পেঁচা)", "হালাল", "জায়েজ", "মুস্তাহাব"], ["Haram and forbidden (e.g., eagles, hawks, falcons, vultures, owls)", "Halal", "Permissible", "Mustahab"], 0,
         "শিকারী নখর দিয়ে শিকারকারী সকল পাখি ভক্ষণ করা হাদিসে দ্ব্যর্থহীনভাবে নিষিদ্ধ।", "All birds of prey that hunt with talons are strictly prohibited.", "সহীহ মুসলিম: ১৯৩৪", "Sahih Muslim: 1934", "EASY"),

        ("মাদকদ্রব্য ও সকল নেশাজাতীয় পানীয়ের (Khamr) ব্যাপারে ইসলামের চূড়ান্ত বিধান কী?", "What is the absolute prohibition regarding alcohol and intoxicants (Khamr) in Islam?",
         ["হারাম এবং সকল অপকর্ম ও পাপাচারের মূল জননী (উম্মুল খাবায়িস)", "সামান্য খাওয়া বৈধ", "জায়েজ", "মাকরুহ"], ["Haram and the mother of all evils and abominations (Umm al-Khaba'ith)", "Allowed in small amount", "Permissible", "Makruh"], 0,
         "রাসূল ﷺ বলেন: 'যা অধিক পরিমাণে গ্রহণ করলে নেশা হয়, তার সামান্যতম অংশও হারাম।' ", "Whatever intoxicates in large quantities, a small quantity of it is also Haram.", "সুনানে আবু দাউদ: ৩৬৮১", "Sunan Abi Dawud: 3681", "EASY"),

        ("মাদকদ্রব্যের সংশ্লিষ্টতায় রাসূলুল্লাহ ﷺ কত শ্রেণির ব্যক্তির ওপর আল্লাহর অভিশাপ (লানত) ঘোষণা করেছেন?", "Upon how many categories of people associated with intoxicants did the Prophet ﷺ pronounce curse?",
         ["১০ শ্রেণির ব্যক্তি (প্রস্তুতকারক, পরিবেশনকারী, বিক্রেতা, বহনকারী, ক্রেতা ইত্যাদি)", "১ জন", "৩ জন", "৫ জন"], ["10 categories (Producer, server, seller, transporter, buyer, consumer, etc.)", "1", "3", "5"], 0,
         "মদ তৈরি, পান, পরিবেশন, ক্রয়-বিক্রয় ও পরিবহনকারী সকলের ওপর আল্লাহর লানত বর্ষিত হয়।", "Allah has cursed wine, its presser, its server, its buyer, its seller, and its drinker.", "সুনানে আবু দাউদ: ৩৬৭৪", "Sunan Abi Dawud: 3674", "EASY"),

        ("ইসলামে সুদ (Riba / Usury) গ্রহণ, প্রদান ও সুদী ব্যাংকিংয়ে অংশ নেওয়ার ভয়াবহতা সম্পর্কে কুরআনের ঘোষণা কী?", "What did Allah proclaim against those who deal in Riba (Interest/Usury) in Surah Al-Baqarah?",
         ["আল্লাহ ও তাঁর রাসূলের বিরুদ্ধে সরাসরি যুদ্ধ ঘোষণার শামিল", "সাধারণ ব্যবসা", "ছোট গুনাহ", "লাভজনক লেনদেন"], ["A direct declaration of war against Allah and His Messenger (Surah Al-Baqarah: 279)", "Normal trade", "Minor sin", "Profitable deal"], 0,
         "আল্লাহ ব্যবসাকে হালাল করেছেন এবং সুদকে কঠোরভাবে হারাম ঘোষণা করেছেন।", "Allah has permitted trade and has forbidden interest (Surah Al-Baqarah: 275).", "সূরা আল-বাক্বারাহ: ২৭৫", "Surah Al-Baqarah: 275", "EASY"),

        ("সুদের সাথে জড়িত চার ব্যক্তির (গ্রহীতা, দাতা, লেখক ও সাক্ষীদ্বয়) ব্যাপারে হাদিসের ফয়সালা কী?", "What is the ruling on the four parties involved in an interest transaction?",
         ["তারা সকলেই সমান অপরাধী ও অভিশপ্ত", "কেবল ব্যাংক দোষী", "কোনো দোষ নেই", "লেখক নির্দোষ"], ["They are all equally sinful and cursed by the Prophet ﷺ", "Only bank guilty", "No fault", "Scribe innocent"], 0,
         "রাসূল ﷺ সুদখোর, সুদদাতা, সুদের হিসাব লেখক এবং সুদের দুই সাক্ষীকে সমান অপরাধী হিসেবে লানত দিয়েছেন।", "The Prophet ﷺ cursed the receiver of interest, the payer, the scribe, and the two witnesses.", "সহীহ মুসলিম: ১৫৯৮", "Sahih Muslim: 1598", "EASY"),

        ("জুয়া (Maysir / Gambling) এবং লটারি বা বাজি ধরার ব্যাপারে কুরআনের সুস্পষ্ট হুকুম কী?", "What is the unequivocal Quranic decree on gambling (Maysir), betting, and lotteries?",
         ["হারাম এবং শয়তানের অপবিত্র ঘৃণ্য কাজ যা থেকে বিরত থাকা ফরজ", "জায়েজ খেলা", "ভাগ্যের পরীক্ষা", "উপকারী"], ["Haram and an abomination of Satan's handiwork to be strictly avoided", "Permissible game", "Luck test", "Beneficial"], 0,
         "সূরা মায়িদায় মদ ও জুয়াকে পারস্পরিক শত্রুতা ও আল্লাহর যিকির থেকে বাধা প্রদানকারী শয়তানি ফাঁদ বলা হয়েছে।", "Intoxicants and gambling are defilement from the work of Satan, so avoid it.", "সূরা আল-মায়িদাহ: ৯০", "Surah Al-Ma'idah: 90", "EASY"),

        ("ঘুষ (Rishwah / Bribery) আদান-প্রদান করার অপরাধে জড়িতদের ব্যাপারে রাসূল ﷺ কী ঘোষণা করেছেন?", "What did Prophet Muhammad ﷺ declare regarding the giver and taker of bribes (Rishwah)?",
         ["'ঘুষদাতা এবং ঘুষগ্রহীতা উভয়ই জাহান্নামের আগুনে প্রবেশ করবে'", "ঘুষদাতা নির্দোষ", "সামাজিক প্রথা", "জায়েজ"], ["'The one who gives the bribe and the one who accepts it are both in Hellfire'", "Giver innocent", "Custom", "Permissible"], 0,
         "অন্যায় সুবিধা গ্রহণ বা অধিকার হরণের জন্য ঘুষ দেওয়া ও নেওয়া উভয়ই মারাত্মক কবিরা গুনাহ।", "The curse of Allah is upon the briber and the bribe-taker.", "জামে আত-তিরমিযী: ১৩৩৭", "Jami at-Tirmidhi: 1337", "EASY"),

        ("ব্যবসা-বাণিজ্যে পণ্য মজুতদারি করে কৃত্রিম সংকট সৃষ্টি করে অতিরিক্ত মূল্যে বিক্রি করাকে কী বলে?", "What is the prohibited monopolistic hoarding of essential food goods to inflate prices called?",
         ["ইহতিকার (Ihtikar / Hoarding)", "মুদারাবা", "মুরাবাহা", "ইজারা"], ["Ihtikar (Hoarding)", "Mudarabah", "Murabaha", "Ijarah"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: পাপিষ্ঠ ও অপরাধী ব্যক্তি ছাড়া কেউই নিত্যপ্রয়োজনীয় খাদ্যদ্রব্য মজুতদারি করে না।", "No one hoards goods to artificially raise prices except a sinner.", "সহীহ মুসলিম: ১৬০৫", "Sahih Muslim: 1605", "EASY"),

        ("পণ্য বিক্রির সময় পণ্যের কোনো ত্রুটি বা খুঁত গোপন করে বিক্রি করার বিধান কী?", "What is the ruling on concealing defects of merchandise during trade?",
         ["হারাম ও প্রতারণা; রাসূল ﷺ বলেন: যে প্রতারণা করে সে আমার উম্মতের অন্তর্ভুক্ত নয়", "জায়েজ কৌশল", "মুনাফা বৃদ্ধি", "মাকরুহ"], ["Haram and fraud; the Prophet ﷺ said: Whoever deceives is not of us", "Smart tactic", "Profit gain", "Makruh"], 0,
         "বিক্রেতার দায়িত্ব পণ্যের সকল দোষ স্পষ্টভাবে ক্রেতার সামনে প্রকাশ করা, অন্যথায় বরকত ধ্বংস হয়।", "He who cheats us is not one of us.", "সহীহ মুসলিম: ১০২", "Sahih Muslim: 102", "EASY"),

        ("পুরুষদের জন্য খাঁটি রেশমি পোশাক পরিধান করা এবং স্বর্ণালংকার ব্যবহার করার বিধান কী?", "What is the ruling for Muslim men regarding wearing pure silk and gold jewelry?",
         ["পুরুষদের জন্য হারাম ও নিষিদ্ধ, তবে নারীদের জন্য সম্পূর্ণ হালাল ও বৈধ", "পুরুষদের জন্য হালাল", "উভয়ের জন্য হারাম", "উভয়ের জন্য হালাল"], ["Haram for men, but fully Halal and permissible for women", "Halal for men", "Haram for both", "Halal for both"], 0,
         "রাসূল ﷺ স্বর্ণ ও রেশম হাতে নিয়ে বলেন: এ দুটি আমার উম্মতের পুরুষদের জন্য হারাম এবং নারীদের জন্য হালাল।", "Gold and silk are forbidden for the males of my Ummah and lawful for their females.", "জামে আত-তিরমিযী: ১৭২০", "Jami at-Tirmidhi: 1720", "EASY"),

        ("শরীরে স্থায়ী উল্কি বা ট্যাটু (Tattoo) আঁকা এবং ভ্রু প্লাক করার ব্যাপারে হাদিসের নির্দেশনা কী?", "What is the strict ruling regarding permanent tattoos and plucking eyebrows?",
         ["হারাম ও আল্লাহর সৃষ্টির প্রাকৃতিক রূপ পরিবর্তনের কারণে অভিশপ্ত কাজ", "সৌন্দর্যচর্চা", "জায়েজ", "মুস্তাহাব"], ["Haram and cursed for altering the natural physical creation of Allah", "Beauty care", "Permissible", "Mustahab"], 0,
         "রাসূল ﷺ উল্কি অঙ্কনকারী, উল্কি গ্রহণকারী ও কৃত্রিমভাবে দাঁতের মাঝে ফাঁক সৃষ্টিকারীদের ওপর লানত দিয়েছেন।", "Allah has cursed women who tattoo themselves and alter Allah's creation.", "সহীহ বুখারী: ৫৯৩১", "Sahih Bukhari: 5931", "EASY"),

        ("গৃহে জীবজন্তু বা মানুষের ছবি ও মূর্তি ঝুলিয়ে রাখার ব্যাপারে রাসূল ﷺ-এর নিষেধাজ্ঞা কী?", "What happens to a home containing hung images/portraits of animate beings or statues?",
         ["রহমতের ফেরেশতা সেই ঘরে প্রবেশ করে না", "ফেরেশতা বৃদ্ধি পায়", "বরকত আসে", "কোনো প্রভাব নেই"], ["Angels of mercy do not enter a house containing statues or pictures of living beings", "Angels increase", "Barakah comes", "No effect"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: ফেরেশতাগণ এমন ঘরে প্রবেশ করেন না যেখানে কুকুর বা জীবন্ত প্রাণীর ছবি থাকে।", "Angels do not enter a house in which there is a dog or a picture of living creatures.", "সহীহ বুখারী: ৩২২৫", "Sahih Bukhari: 3225", "EASY"),

        ("গৃহপালিত গাধা (Domestic Donkey)-এর গোশত খাওয়ার বিধান কী যা খায়বারের যুদ্ধে ঘোষিত হয়?", "What is the ruling on consuming the meat of domestic donkeys (Al-Humur al-Ahliyyah)?",
         ["হারাম ও নিষিদ্ধ", "হালাল", "মুস্তাহাব", "মাকরুহ তানযীহী"], ["Haram and strictly prohibited", "Halal", "Mustahab", "Makruh"], 0,
         "খায়বার যুদ্ধের দিন রাসূলুল্লাহ ﷺ গৃহপালিত গাধার গোশত খাওয়া চিরতরে নিষিদ্ধ ঘোষণা করেন।", "The Prophet ﷺ prohibited the meat of domestic donkeys on the day of Khaybar.", "সহীহ বুখারী: ৫৫৫৮", "Sahih Bukhari: 5558", "EASY"),

        ("বন্য জেব্রা বা বনগাধার (Wild Ass) গোশত খাওয়ার ব্যাপারে শরীয়তের বিধান কী?", "What is the ruling on consuming the meat of wild asses / zebras in Islamic law?",
         ["হালাল ও ভক্ষণযোগ্য", "হারাম", "মাকরুহ তাহরীমী", "নিষিদ্ধ"], ["Halal and permissible to consume", "Haram", "Makruh", "Forbidden"], 0,
         "হযরত আবু কাতাদা (রা.) একটি বুনো গাধা শিকার করলে রাসূল ﷺ নিজে তার গোশত ভক্ষণ করেছিলেন।", "The Prophet ﷺ ate from the meat of the wild ass hunted by Abu Qatadah.", "সহীহ বুখারী: ৫৪৯১", "Sahih Bukhari: 5491", "MEDIUM"),

        ("সোনা ও রূপার তৈরি পাত্রে খাবার খাওয়া ও পানীয় পান করার ব্যাপারে হাদিসের নিষেধাজ্ঞা কী?", "What is the ruling on eating and drinking from gold and silver utensils?",
         ["হারাম ও অহংকারের প্রতীক; যে এতে পান করে সে পেটে জাহান্নামের আগুন ঢালছে", "জায়েজ", "মুস্তাহাব", "মাকরুহ"], ["Haram; whoever drinks from silver/gold vessels swallows Hellfire into his belly", "Permissible", "Mustahab", "Makruh"], 0,
         "রাসূল ﷺ বলেন: সোনা ও রূপার পাত্রে পানাহার করো না, এটা দুনিয়াতে কাফেরদের জন্য এবং পরকালে তোমাদের জন্য।", "Do not drink in silver or gold vessels; they are for them in this world and for you in the Hereafter.", "সহীহ বুখারী: ৫৪২৬", "Sahih Bukhari: 5426", "EASY"),

        ("জ্যোতিষশাস্ত্র ও রাশিফল (Horoscope) দেখে নিজের ভবিষ্যৎ ভাগ্য নির্ধারণ বা বিশ্বাস করার বিধান কী?", "What is the ruling on reading horoscopes and believing astrology for future predictions?",
         ["হারাম ও তাওহীদবিরোধী কাজ যা ঈমানকে ধ্বংস করে দেয়", "জায়েজ বিনোদন", "বিজ্ঞানচর্চা", "মুস্তাহাব"], ["Haram and contradictory to Tawheed, nullifying Islamic faith", "Entertainment", "Science", "Mustahab"], 0,
         "তারকা বা রাশির কোনো ক্ষমতা নেই মানুষের ভালো-মন্দ নির্ধারণ করার; ভাগ্য একমাত্র আল্লাহর হাতে।", "Whoever acquires a branch of astrology has acquired a branch of magic.", "সুনানে আবু দাউদ: ৩৯০৫", "Sunan Abi Dawud: 3905", "EASY"),

        ("যেনা-ব্যভিচার (Zina / Fornication) এবং সমকামিতার ব্যাপারে ইসলামের শরয়ী বিধান কী?", "What is the status of Zina (adultery/fornication) and sodomy in Islamic law?",
         ["চরম ঘৃণ্যতম কবিরা গুনাহ এবং শরীয়তে কঠোরতম হদ্দের শাস্তিযোগ্য অপরাধ", "ক্ষমাযোগ্য ভুল", "ছোট পাপ", "ব্যক্তিগত স্বাধীনতা"], ["Heinous destructive major sins subject to severe Hadd corporal punishments", "Minor mistake", "Small sin", "Personal choice"], 0,
         "আল্লাহ বলেন: 'তোমরা ব্যভিচারের কাছেও যেও না, নিশ্চয়ই এটি অশ্লীল ও নিকৃষ্ট পথ।' ", "And do not approach unlawful sexual intercourse; indeed, it is an immorality and an evil way.", "সূরা আল-ইসরা: ৩২", "Surah Al-Isra: 32", "EASY"),

        ("কোনো সতী-সাধ্বী নির্দোষ নারীর ওপর মিথ্যা অপবাদ আরোপ করাকে (ক্বযফ) কুরআনে কী বলা হয়েছে?", "What is falsely accusing chaste innocent women of immorality (Qadhf) termed in the Quran?",
         ["মারাত্মক কবিরা গুনাহ যার শাস্তি ৮০টি বেত্রাঘাত এবং তার সাক্ষ্য আজীবন বাতিল", "ছোট গুনাহ", "সাধারণ কথা", "ক্ষতি নেই"], ["A heinous major sin punishable by 80 lashes and permanent disqualification of testimony", "Minor sin", "Normal talk", "No harm"], 0,
         "সূরা নূরে মিথ্যা অপবাদকারীদের ওপর দুনিয়া ও আখেরাতে লানত এবং ৮০ বেত্রাঘাতের বিধান ঘোষিত হয়েছে।", "Those who accuse chaste women and bring not four witnesses, flog them with eighty stripes.", "সূরা আন-নূর: ৪", "Surah An-Nur: 4", "EASY"),

        ("ইসলামে মিথ্যা কসম বা আল্লাহ ছাড়া অন্য কারও নামে শপথ (যেমন: পিতার নামে, মাথায় হাত দিয়ে) করার বিধান কী?", "What is the ruling on swearing oaths by other than Allah (parents, life, creations)?",
         ["হারাম এবং ছোট শিরকের অন্তর্ভুক্ত অপরাধ", "জায়েজ", "মুস্তাহাব", "ওয়াজিব"], ["Haram and a form of Minor Shirk", "Permissible", "Mustahab", "Wajib"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি শপথ করতে চায় সে যেন কেবল আল্লাহর নামেই শপথ করে অথবা চুপ থাকে।", "Whoever swears an oath, let him swear by Allah alone or remain silent.", "সহীহ বুখারী: ২৬৭৯", "Sahih Bukhari: 2679", "EASY"),

        ("অন্যায়ভাবে কোনো মুসলিম বা নির্দোষ অমুসলিম নাগরিককে হত্যা করার শাস্তি কুরআনে কী ঘোষিত হয়েছে?", "What is the severe consequence of unjustly killing a believer or innocent person in the Quran?",
         ["চিরস্থায়ী জাহান্নাম, আল্লাহর চরম ক্রোধ, লানত এবং মহাশাস্তি", "ক্ষমাযোগ্য", "ছোট গুনাহ", "সাধারণ জরিমানা"], ["Eternal Hellfire, the Wrath and Curse of Allah, and a tremendous torment (Surah An-Nisa: 93)", "Pardonable", "Minor sin", "Small fine"], 0,
         "কুরআনে বলা হয়েছে: যে অন্যায়ভাবে একজন মানুষকে হত্যা করল সে যেন সমগ্র মানবজাতিকে হত্যা করল।", "Whoever kills a soul unjustly, it is as if he had slain all mankind.", "সূরা আল-মায়িদাহ: ৩২", "Surah Al-Ma'idah: 32", "EASY"),

        ("ইসলামে জাদুবিদ্যা ও তন্ত্রমন্ত্রের মাধ্যমে মানুষের ক্ষতি করার চেষ্টার অপরাধের হুকুম কী?", "What is the grave ruling on practicing black magic and sorcery in Islamic law?",
         ["কুফরি ও সর্বোচ্চ পর্যায়ের শাস্তিযোগ্য কবিরা গুনাহ", "জায়েজ খেলা", "চিকিৎসা", "বিনোদন"], ["Major Kufr (Disbelief) and a capital offence in Islamic jurisprudence", "Game", "Medicine", "Entertainment"], 0,
         "যাদু হলো মানুষের ঈমান ও পারিবারিক সম্পর্ক ধ্বংসকারী শয়তানি তৎপরতা যা কঠোরভাবে নিষিদ্ধ।", "Sorcery is one of the seven destructive sins nullifying faith.", "সহীহ বুখারী: ২৭৬৬", "Sahih Bukhari: 2766", "EASY"),

        ("হালাল পশুর রক্তের ব্যাপারে কুরআনে কী বিধান এসেছে?", "What is the ruling regarding the flowing blood (Damman Masfoohan) of slaughtered Halal animals?",
         ["হারাম ও অপবিত্র; কেবল পশুর গোশতে লেগে থাকা অবশিষ্টাংশ এবং কলিজা-প্লীহা হালাল", "রক্ত খাওয়া হালাল", "মুস্তাহাব", "জায়েজ"], ["Haram and impure; only residual blood in veins/meat and liver/spleen are permissible", "Blood halal", "Mustahab", "Permissible"], 0,
         "প্রবাহিত রক্ত জমাট বেঁধে রান্না করে খাওয়া কুরআনে স্পষ্টভাবে হারাম ঘোষণা করা হয়েছে।", "Say: I find not in that revealed to me anything forbidden except carrion, or blood poured forth.", "সূরা আল-আন'আম: ১৪৫", "Surah Al-An'am: 145", "EASY"),

        ("ইসলামে শূকর (Pork / Swine) কেন সম্পূর্ণরূপে অপবিত্র ও হারাম ঘোষণা করা হয়েছে?", "Why is pork categorically prohibited and deemed impure (Rijs) in the Quran?",
         ["আল্লাহ তাআলার দ্ব্যর্থহীন নিষেধাজ্ঞায় এটি অপবিত্র (রিজস) ও স্বাস্থ্যের জন্য চরম ক্ষতিকর", "অর্থনৈতিক কারণে", "আরবে ছিল না বলে", "ঐচ্ছিক"], ["By explicit divine command; it is intrinsically impure (Rijs) and spiritually/physically destructive", "Economic", "Not in Arabia", "Optional"], 0,
         "কুরআনের চারটি পৃথক সূরায় শূকরের গোশতকে 'রিজস' (চরম অপবিত্র) হিসেবে হারাম ঘোষণা করা হয়েছে।", "He has only forbidden you dead animals, blood, and the flesh of swine (Surah Al-Baqarah: 173).", "সূরা আল-বাক্বারাহ: ১৭৩", "Surah Al-Baqarah: 173", "EASY"),

        ("দাড়ি ছাঁটা বা সম্পূর্ণ মুণ্ডন করার ব্যাপারে সুন্নাহর সাধারণ সুসংহত অবস্থান কী?", "What is the established position of the Prophetic Sunnah regarding the beard for men?",
         ["দাড়ি লম্বা রাখা ওয়াজিব/সুন্নাতে মুয়াক্কাদাহ এবং গোঁফ খাটো করা সুন্নাত", "দাড়ি মুণ্ডন করা ফরজ", "কোনো নিয়ম নেই", "ঐচ্ছিক সাজসজ্জা"], ["Growing the beard and trimming the mustache is an authentic Sunnah mandate", "Shaving fard", "No rule", "Optional"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: গোঁফ খাটো করো এবং দাড়ি বড় করো; অগ্নিপূজকদের বিপরীত চলো।", "Trim closely the mustache and grow the beard.", "সহীহ মুসলিম: ২৫৯", "Sahih Muslim: 259", "EASY"),

        ("ইসলামে মিথ্যা শপথ করে পণ্য বিক্রি করার ব্যাপারে রাসূল ﷺ-এর বিশেষ হুঁশিয়ারি কী?", "What did the Prophet ﷺ warn regarding selling goods through false swearing?",
         ["পণ্য সাময়িক বিক্রি হলেও ব্যবসার সকল বরকত চিরতরে ধ্বংস হয়ে যায়", "মুনাফা বাড়ে", "জায়েজ", "উত্তম কৌশল"], ["The merchandise might sell quickly, but all divine Barakah is wiped out", "Profit increases", "Permissible", "Good trick"], 0,
         "রাসূল ﷺ বলেন: মিথ্যা কসম পণ্য দ্রুত কাটায় কিন্তু ব্যবসায়ের বরকত নিঃশেষ করে দেয়।", "Swearing produces a quick sale for the goods but wipes out the blessing.", "সহীহ বুখারী: ২০৮৭", "Sahih Bukhari: 2087", "EASY"),

        ("ইসলামে এতিমের সম্পদ অন্যায়ভাবে আত্মসাৎ করার ব্যাপারে কুরআনের ভয়াবহ হুঁশিয়ারি কী?", "What does the Quran declare regarding those who unjustly consume the property of orphans?",
         ["'তারা মূলত নিজেদের পেটে জাহান্নামের দাউদাউ আগুনই ভক্ষণ করছে'", "ক্ষমা পাবে", "ছোট ভুল", "সম্পদ বৃদ্ধি"], ["'They are only consuming fire into their bellies and will burn in a Blaze' (Surah An-Nisa: 10)", "Forgiven", "Minor error", "Wealth gain"], 0,
         "এতিমের সম্পদ রক্ষা করা অভিভাবকের দায়িত্ব; তাতে কোনো রূপ খেয়ানত করা মহাপাপ।", "Indeed, those who devour the property of orphans unjustly consume only fire into their bellies.", "সূরা আন-নিসা: ১০", "Surah An-Nisa: 10", "EASY"),

        ("হারাম উপার্জিত অর্থে দান-সদকা করলে আল্লাহ তাআলা কি তা কবুল করেন?", "Does Allah accept charity (Sadaqah) given from unlawfully acquired (Haram) wealth?",
         ["না, আল্লাহ পবিত্র এবং তিনি পবিত্র ও হালাল উৎস ছাড়া কোনো দান কবুল করেন না", "হ্যাঁ, অর্ধেক কবুল হয়", "পুরো সওয়াব মেলে", "ইচ্ছাধীন"], ["No; Allah is Pure and accepts only that which is lawful and pure", "Half accepted", "Full reward", "Optional"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: নিশ্চয়ই আল্লাহ তাআলা পবিত্র, তিনি কেবল পবিত্র বস্তুই কবুল করেন।", "Allah the Almighty is Pure and accepts only that which is pure.", "সহীহ মুসলিম: ১০১৫", "Sahih Muslim: 1015", "EASY"),

        ("হালাল ও হারামের সুস্পষ্ট সীমানার মাঝে যেসব অস্পষ্ট বা সন্দেহজনক বিষয় (শুভাহাত) থাকে সেগুলোর ব্যাপারে মুমিনের করণীয় কী?", "What is the believer's duty regarding ambiguous or doubtful matters (Shubuhat)?",
         ["সন্দেহজনক বিষয় থেকে পুরোপুরি বেঁচে থাকা যাতে দ্বীন ও সম্মান নিরাপদ থাকে", "সন্দেহ থাকলেও করা", "উপেক্ষা করা", "হারাম মনে না করা"], ["Avoid doubtful matters completely to protect one's faith and honor", "Engage anyway", "Ignore", "Not haram"], 0,
         "রাসূল ﷺ বলেন: যে সন্দেহজনক বিষয় থেকে বেঁচে থাকে সে নিজের দ্বীন ও আত্মমর্যাদাকে নিষ্কলুষ রাখে।", "He who guards against doubtful matters saves his religion and his innocence.", "সহীহ বুখারী: ৫২", "Sahih Bukhari: 52", "EASY"),

        ("হালাল রুজি অন্বেষণ করাকে ফরজ ইবাদতের পর কী মর্যাদা দেওয়া হয়েছে?", "What is the noble status of seeking Halal livelihood after mandatory obligations?",
         ["ফরজ ইবাদতের পর অন্যতম ফরজ দায়িত্ব", "মুস্তাহাব", "ঐচ্ছিক", "সাধারণ পার্থিব কাজ"], ["An obligatory duty second only to the primary Fard acts of worship", "Mustahab", "Optional", "Secular chore"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: ফরজ ইবাদতের পর হালাল রুজি অন্বেষণ করা অন্যতম একটি ফরজ।", "Seeking Halal livelihood is an obligatory duty after the compulsory duties.", "শুআবুল ঈমান: বায়হাকী", "Shu'ab al-Iman", "EASY")
    ]
    return [('Q_HLH_' + str(i+1).zfill(3), 'halal_haram', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_16_akhlaq():
    items = [
        ("বিশ্বনবী হযরত মুহাম্মদ ﷺ-এর প্রেরণের মূল লক্ষ্য সম্পর্কে তিনি স্বয়ং কী ঘোষণা করেছেন?", "What did Prophet Muhammad ﷺ declare as the primary mission of his prophethood?",
         ["'আমি তো মহান উত্তম চরিত্রের পরিপূর্ণতা সাধনের জন্যই প্রেরিত হয়েছি'", "কেবল সাম্রাজ্য বিস্তার", "পার্থিব শাসন", "বাণিজ্য সম্প্রসারণ"], ["'I was sent only to perfect noble moral character (Makarim al-Akhlaq)'", "Empire", "Rule", "Trade"], 0,
         "রাসূল ﷺ বলেন: নিশ্চয়ই আল্লাহ আমাকে সর্বোত্তম চরিত্রের বিকাশ ও পূর্ণতার জন্যই প্রেরণ করেছেন।", "I have only been sent to perfect good moral character.", "মুয়াত্তা মালিক: ১৬১৪", "Muwatta Malik: 1614", "EASY"),

        ("কিয়ামতের দিন মুমিনের আমলের পাল্লায় (মীযান) সবচেয়ে ভারী ও ওজনদার কোন্ জিনিসটি হবে?", "What will be the heaviest single virtue on the Scale (Mizan) on the Day of Judgment?",
         ["উত্তম চরিত্র ও সুন্দর শিষ্টাচার (হুসনুল খুলুক্ব)", "কেবল শারীরিক শক্তি", "ধনদৌলত", "বংশের নাম"], ["Good moral character and noble conduct (Husn al-Khuluq)", "Physical power", "Wealth", "Lineage"], 0,
         "রাসূল ﷺ বলেছেন: কিয়ামতের দিন মুমিনের দাড়িপাল্লায় উত্তম চরিত্রের চেয়ে ভারী আর কিছুই থাকবে না।", "Nothing is heavier on the scale of a believer on the Day of Resurrection than good character.", "জামে আত-তিরমিযী: ২০০২", "Jami at-Tirmidhi: 2002", "EASY"),

        ("রাসূলুল্লাহ ﷺ-এর চরিত্র কেমন ছিল তা জানতে চাইলে উম্মুল মুমিনীন হযরত আয়েশা (রা.) কী উত্তর দেন?", "How did Aisha (RA) describe the character and personality of the Prophet ﷺ?",
         ["'তাঁর চরিত্র ছিল অবিকল জীবন্ত আল-কুরআন'", "তিনি সাধারণ মানুষ ছিলেন", "তিনি গম্ভীর ছিলেন", "তিনি কবি ছিলেন"], ["'His character was a living embodiment of the Holy Quran'", "Average", "Solemn", "Poet"], 0,
         "কুরআনে যেসব গুণের আদেশ দেওয়া হয়েছে নবীজি ﷺ ছিলেন তার বাস্তব প্রতিচ্ছবি।", "His character was the Quran in practical action.", "সহীহ মুসলিম: ৭৪৬", "Sahih Muslim: 746", "EASY"),

        ("মুমিনদের মধ্যে ঈমানে সবচেয়ে পূর্ণাঙ্গ ও সেরা ব্যক্তি কে যা হাদিসে বর্ণিত হয়েছে?", "Who is the most complete in faith among the believers according to Hadith?",
         ["যার চরিত্র ও ব্যবহার সবার চেয়ে সবচেয়ে বেশি সুন্দর", "যে সবচেয়ে ধনী", "যে সবচেয়ে শক্তিশালী", "যে সবচেয়ে বেশি কথা বলে"], ["The one who possesses the best moral character and gentlest conduct", "Richest", "Strongest", "Talks most"], 0,
         "রাসূল ﷺ বলেন: মুমিনদের মাঝে পূর্ণাঙ্গতম ঈমানদার সে, যার চরিত্র সর্বাধিক সুন্দর ও উত্তম।", "The most complete of believers in faith are those with the best character.", "জামে আত-তিরমিযী: ১১৬২", "Jami at-Tirmidhi: 1162", "EASY"),

        ("ইসলামে সত্যবাদিতা (Sidq) মানুষকে কোন্ পথে পরিচালিত করে এবং মিথ্যা কোথায় নিয়ে যায়?", "To what does truthfulness lead in life, and where does falsehood take a person?",
         ["সত্যবাদিতা পুণ্যের পথে ও জান্নাতে নিয়ে যায়; আর মিথ্যা পাপাচারের পথে ও জাহান্নামে নিয়ে যায়", "উভয়ই সমান", "মিথ্যা ধনী করে", "সত্য ক্ষতি করে"], ["Truthfulness leads to righteousness and Jannah; falsehood leads to wickedness and Hell", "Both equal", "Lies make rich", "Truth harms"], 0,
         "সত্যবাদী ব্যক্তি সর্বদা সত্য বলতে বলতে আল্লাহর দরবারে 'সিদ্দিক' বা পরম সত্যবাদী হিসেবে লিখিত হয়।", "Truthfulness leads to righteousness, and righteousness leads to Paradise.", "সহীহ বুখারী: ৬০৯৪", "Sahih Bukhari: 6094", "EASY"),

        ("ইসলামে পিতা-মাতার সন্তুষ্টি ও অসন্তুষ্টির সাথে আল্লাহর সম্পর্কের ব্যাপারে রাসূল ﷺ কী বলেছেন?", "What did the Prophet ﷺ state regarding the pleasure of parents and the Pleasure of Allah?",
         ["'পিতার সন্তুষ্টিতে আল্লাহর সন্তুষ্টি এবং পিতার অসন্তুষ্টিতে আল্লাহর অসন্তুষ্টি নিহিত'", "কোনো সম্পর্ক নেই", "পিতা-মাতার গুরুত্ব কম", "পার্থিব বিষয়"], ["'The pleasure of the Lord is in the pleasure of the parent, and His displeasure in their displeasure'", "No relation", "Low importance", "Worldly matter"], 0,
         "পিতা-মাতার সেবা জান্নাতের প্রধানতম দরজা; তাদের অবাধ্যতা মারাত্মক কবিরা গুনাহ।", "The Lord's pleasure is in the parent's pleasure, and His wrath is in the parent's wrath.", "জামে আত-তিরমিযী: ১৮৯৯", "Jami at-Tirmidhi: 1899", "EASY"),

        ("সন্তানের ওপর মাতার অধিকারের প্রাধান্য প্রকাশে এক সাহাবীর প্রশ্নের জবাবে রাসূল ﷺ কতবার 'তোমার মা' বলেছেন?", "How many times did the Prophet ﷺ emphasize 'Your Mother' before mentioning the father?",
         ["টানা তিনবার 'তোমার মা' বলার পর চতুর্থবারে 'তোমার পিতা' বলেছেন", "একবার", "দুইবার", "সমান"], ["Three consecutive times: 'Your mother', then fourthly 'Your father'", "Once", "Twice", "Equal"], 0,
         "মা গর্ভধারণ, প্রসব ও লালন-পালনের অতুলনীয় কষ্টের কারণে সন্তানের নিকট তিনগুণ বেশি সেবার অধিকারী।", "Treat your mother with highest companionship, then your mother, then your mother, then your father.", "সহীহ বুখারী: ৫৯৭১", "Sahih Bukhari: 5971", "EASY"),

        ("রক্তের আত্মীয়তার সম্পর্ক বজায় রাখা ও আত্মীয়দের সাথে সুসম্পর্ক রক্ষা করাকে কী বলে?", "What is the vital Islamic obligation of maintaining ties of kinship termed?",
         ["সিলাতুর রাহিম (Silat ar-Rahim)", "মুদারাবা", "মুআমালাত", "ক্বিরাস"], ["Silat ar-Rahim (Upholding Kinship Ties)", "Mudarabah", "Muamalat", "Qiras"], 0,
         "সিলাতুর রাহিম রক্ষা করলে আল্লাহ রিযিক বৃদ্ধি করেন এবং মানুষের আয়ু ও স্মৃতিতে বরকত দেন।", "Whoever wishes to have his provision expanded and his lifespan prolonged, let him uphold kinship.", "সহীহ বুখারী: ৫৯৮৬", "Sahih Bukhari: 5986", "EASY"),

        ("আত্মীয়তার সম্পর্ক ছিন্নকারী ব্যক্তির ব্যাপারে রাসূলুল্লাহ ﷺ-এর সুস্পষ্ট সতর্কবার্তা কী?", "What did the Prophet ﷺ strictly warn regarding one who severs family ties (Qati' ar-Rahim)?",
         ["'আত্মীয়তার সম্পর্ক ছিন্নকারী ব্যক্তি জান্নাতে প্রবেশ করতে পারবে না'", "ক্ষমা পাবে", "ধনী হবে", "কোনো সমস্যা নেই"], ["'The one who severs family ties of kinship will not enter Paradise'", "Forgiven", "Becomes rich", "No issue"], 0,
         "আল্লাহ বলেন: যে আত্মীয়তার সম্পর্ক বজায় রাখে আমি তার সাথে সম্পর্ক রাখি, আর যে ছিন্ন করে আমি তার সাথে ছিন্ন করি।", "The severer of kinship ties will not enter Paradise.", "সহীহ বুখারী: ৫৯৮৪", "Sahih Bukhari: 5984", "EASY"),

        ("প্রতিবেশীর অধিকার রক্ষার ওপর জিবরাইল (আ.) এত বেশি জোর দিচ্ছিলেন যে রাসূল ﷺ কী ভেবেছিলেন?", "Why did the Prophet ﷺ think the neighbor might be designated as a legal heir?",
         ["জিবরাইল প্রতিবেশীর হক সম্পর্কে এতো অসিয়ত করছিলেন যে মনে হচ্ছিল প্রতিবেশীকে ওয়ারিশ বানিয়ে দেওয়া হবে", "প্রতিবেশী শত্রু", "কোনো হক নেই", "যুদ্ধ হবে"], ["Jibreel kept advising regarding the neighbor until the Prophet ﷺ thought he would make him an heir", "Neighbor enemy", "No rights", "War"], 0,
         "প্রতিবেশী মুসলিম হোক বা অমুসলিম, তার ক্ষুধা নিবারণ ও নিরাপত্তা নিশ্চিত করা অপরিহার্য দায়িত্ব।", "Jibreel kept enjoining good treatment of neighbors until I thought he would give them inheritance.", "সহীহ বুখারী: ৬০১৪", "Sahih Bukhari: 6014", "EASY"),

        ("মুমিনের সাথে কথা বলার সময় তার চেহারার সামনে মুচকি হাসি দেওয়ার সওয়াব হাদিসে কীসের সমতুল্য?", "What spiritual reward is attained by meeting your Muslim brother with a smiling cheerful face?",
         ["একটি সদকা করার সমতুল্য পুণ্য (সাদাকাহ)", "সাধারণ অঙ্গভঙ্গি", "সওয়াব নেই", "সময় কাটানো"], ["Equivalent to giving a voluntary charity (Sadaqah)", "Ordinary gesture", "No reward", "Passing time"], 0,
         "রাসূল ﷺ বলেন: তোমার ভাইয়ের মুখের দিকে তাকিয়ে তোমার মুচকি হাসি দেওয়াও একটি সদকা।", "Your smiling in the face of your brother is an act of charity.", "জামে আত-তিরমিযী: ১৯৫৬", "Jami at-Tirmidhi: 1956", "EASY"),

        ("ইসলামে সালামের ব্যাপক প্রচার-প্রসার ঘটানোর ব্যাপারে রাসূলুল্লাহ ﷺ-এর নির্দেশনা কী?", "What did the Prophet ﷺ instruct regarding spreading the greeting of Salam?",
         ["'পরিচিত ও অপরিচিত সকল মুসলিমকে সালাম দাও, এতে পারস্পরিক ভালোবাসা সৃষ্টি হবে'", "কেবল পরিচিতকে দাও", "বড়দের দিও না", "দরকার নেই"], ["'Spread Salam among those you know and those you do not know to foster mutual love'", "Only acquaintances", "Not to elders", "Unnecessary"], 0,
         "সালাম হলো শান্তির বার্তা; সালামের ব্যাপক প্রচলন ঈমান ও পারস্পরিক ভ্রাতৃত্বের পূর্ণতা আনে।", "Spread Salam, feed the hungry, and you will enter Paradise in peace.", "সহীহ বুখারী: ১২", "Sahih Bukhari: 12", "EASY"),

        ("গীবত (Gheebah / পরনিন্দা) করার পাপকে পবিত্র কুরআনে কোন্ চরম ঘৃণ্য কাজের সাথে তুলনা করা হয়েছে?", "To what repulsive act is backbiting (Gheebah) compared in Surah Al-Hujurat?",
         ["নিজের মৃত ভাইয়ের গোশত খাওয়ার সাথে তুলনা করা হয়েছে", "চুরি করার সাথে", "বিষপানের সাথে", "মারামারি করার সাথে"], ["Eating the raw flesh of one's own dead brother", "Stealing", "Drinking poison", "Fighting"], 0,
         "কুরআনে আল্লাহ বলেন: 'তোমাদের কেউ কি তার মৃত ভাইয়ের গোশত খেতে পছন্দ করবে? বস্তুত তোমরা তা ঘৃণা করো।' ", "Would any of you like to eat the flesh of his dead brother? You would despise it (Surah Al-Hujurat: 12).", "সূরা আল-হুজুরাত: ১২", "Surah Al-Hujurat: 12", "EASY"),

        ("কারো পেছনে তার এমন কোনো সত্য দোষের কথা বলা যা সে শুনলে কষ্ট পাবে তাকে কী বলে?", "What is stating a true but disliked flaw about a brother in his absence called?",
         ["গীবত (Gheebah / Backbiting)", "বুহতান (অপবাদ)", "নাসীহাত", "ইস্তিগফার"], ["Gheebah (Backbiting)", "Buhtan (Slander)", "Naseehah", "Istighfar"], 0,
         "রাসূল ﷺ বলেন: গীবত হলো ভাইয়ের পেছনে এমন কিছু বলা যা সে অপছন্দ করে; আর যদি তা মিথ্যা হয় তবে তা অপবাদ (বুহতান)।", "Gheebah is saying something about your brother that he would dislike.", "সহীহ মুসলিম: ২৫৮৯", "Sahih Muslim: 2589", "EASY"),

        ("মানুষের মাঝে চোগলখোরি (Namimah / একের কথা অন্যকে লাগিয়ে বিবাদ সৃষ্টি) করার পরিণতি কী?", "What did the Prophet ﷺ warn regarding the talebearer / slanderer (Qattat / Nameem)?",
         ["'চোগলখোর ব্যক্তি কখনো জান্নাতে প্রবেশ করতে পারবে না'", "মাফ পাবে", "ধনী হবে", "ক্ষতি নেই"], ["'The talebearer (Nameem) who stirs discord will never enter Paradise'", "Forgiven", "Becomes rich", "No harm"], 0,
         "চোগলখোরি পারস্পরিক বিদ্বেষ সৃষ্টি করে এবং এটি কবরের ভয়াবহ আযাবের অন্যতম প্রধান কারণ।", "A talebearer stirring discord between people will not enter Paradise.", "সহীহ বুখারী: ৬০৫৬", "Sahih Bukhari: 6056", "EASY"),

        ("হিংসা-বিদ্বেষ (Hasad) মানুষের নেক আমলের সাথে কেমন আচরণ করে যা হাদিসে বর্ণিত হয়েছে?", "How does malicious envy/jealousy (Hasad) destroy a person's good deeds?",
         ["আগুন যেমন শুকনো কাঠ পুড়িয়ে ছাই করে দেয়, হিংসা তেমনই মানুষের নেক আমল ধ্বংস করে", "আমল বৃদ্ধি করে", "কোনো ক্ষতি করে না", "ধনী বানায়"], ["Envy consumes good deeds just as blazing fire consumes dry firewood", "Increases deeds", "No harm", "Makes rich"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: তোমরা হিংসা থেকে বেঁচে থাকো, কেননা হিংসা নেক আমলকে ভস্মীভূত করে দেয়।", "Beware of envy, for envy devours good deeds just as fire devours wood.", "সুনানে আবু দাউদ: ৪৯০৩", "Sunan Abi Dawud: 4903", "EASY"),

        ("ইসলামে অতিথি বা মেহমানদারী (Iqram ad-Dayf) করার নির্ধারিত সুন্নাত মেয়াদের সময়সীমা কতদিন?", "What is the recommended period of hospitality (hosting a guest) in Islamic etiquette?",
         ["তিন দিন মেহমানদারী করা সুন্নাত ও দায়িত্ব; এর অতিরিক্ত সদকা হিসেবে গণ্য", "১ দিন", "৭ দিন", "এক মাস"], ["Three days is the prescribed hospitality; anything beyond that is Sadaqah", "1 day", "7 days", "1 month"], 0,
         "প্রথম দিন বিশেষ সমাদর এবং তিন দিন পর্যন্ত স্বাভাবিক মেহমানদারী করা মুমিনের বৈশিষ্ট্য।", "Hospitality is for three days, and his special gift is for a day and a night.", "সহীহ বুখারী: ৬১৩৫", "Sahih Bukhari: 6135", "EASY"),

        ("হাঁচি দেওয়ার পর হাঁচিদাতার কর্তব্য কী এবং তা শুনে শ্রোতার জবাব কী হওয়া আবশ্যক?", "What is the Sunnah response when someone sneezes and praises Allah?",
         ["হাঁচিদাতা বলবে 'আলহামদুলিল্লাহ' এবং শ্রোতা বলবে 'ইয়ারহামুকাল্লাহ'", "চুপ থাকবে", "কিছু না বলা", "হাসবে"], ["Sneezer says 'Alhamdulillah', listener replies 'Yarhamukallah'", "Stay silent", "Say nothing", "Laugh"], 0,
         "উত্তরে হাঁচিদাতা পুনরায় বলবে: 'ইয়াহদিকুমুল্লাহু ওয়া ইউসলিহু বালাকুম' (আল্লাহ তোমাদের হেদায়েত ও কল্যাণ দিন)।", "The sneezer says Alhamdulillah, the hearer responds Yarhamukallah.", "সহীহ বুখারী: ৬২২৪", "Sahih Bukhari: 6224", "EASY"),

        ("কাউকে উপদেশ দেওয়ার ক্ষেত্রে ইসলামী শিষ্টাচারের মূল পদ্ধতি কেমন হওয়া উচিত?", "What is the correct Islamic etiquette when advising or correcting a brother?",
         ["নির্জনে পরম নম্রতা, ভালোবাসা ও গোপনীয়তার সাথে বোঝানো", "জনসমক্ষে অপমান করা", "চিৎকার করা", "তিরস্কার করা"], ["Privately with gentleness, compassion, and preserving their dignity", "Public shaming", "Shouting", "Scolding"], 0,
         "ইমাম শাফেয়ী বলেন: যে জনসমক্ষে উপদেশ দেয় সে মূলত তাকে অপমান ও কলঙ্কিত করে; আর যে একাকী বলে সে সংশোধন করে।", "Advising your brother in private elevates him; doing so publicly humiliates him.", "হিলয়াতুল আউলিয়া", "Hilyat al-Awliya", "EASY"),

        ("ইসলামে অহংকার বা দাম্ভিকতা (Kibr)-এর প্রকৃত সংজ্ঞা রাসূলুল্লাহ ﷺ কীভাবে দিয়েছেন?", "How did Prophet Muhammad ﷺ define true arrogance (Kibr)?",
         ["'সত্যকে প্রত্যাখ্যান করা এবং অন্য মানুষকে তুচ্ছ-তাচ্ছিল্য করা'", "সুন্দর পোশাক পরা", "জুতো পরা", "সুগন্ধি লাগানো"], ["'Rejecting the absolute truth and looking down upon people with contempt'", "Wearing nice clothes", "Shoes", "Perfume"], 0,
         "সুন্দর পোশাক পরা অহংকার নয়; অহংকার হলো সত্যের সামনে মাথা নত না করা এবং মানুষকে হেয় ভাবা।", "Arrogance is rejecting the truth and despising people.", "সহীহ মুসলিম: ৯১", "Sahih Muslim: 91", "EASY"),

        ("যার অন্তরে একটি সরিষার দানা বা অনু পরিমাণ অহংকার থাকবে তার পরিণতি সম্পর্কে হাদিসে কী এসেছে?", "What did the Prophet ﷺ declare regarding one with an atom's weight of arrogance in his heart?",
         ["সে ব্যক্তি জান্নাতে প্রবেশ করতে পারবে না", "মাফ পাবে", "ধনী হবে", "কিছু হবে না"], ["He will not enter Paradise so long as arrogance resides in his heart", "Forgiven", "Becomes rich", "Nothing"], 0,
         "অহংকার আল্লাহর চাদর; যে এতে অংশ নিতে চায় আল্লাহ তাকে লাঞ্ছিত করে জাহান্নামে নিক্ষেপ করবেন।", "He who has an atom's weight of arrogance in his heart will not enter Paradise.", "সহীহ মুসলিম: ৯১", "Sahih Muslim: 91", "EASY"),

        ("ধৈর্য বা সবর (Sabr)-এর শ্রেষ্ঠত্ব কখন প্রকাশ পায় যা রাসূল ﷺ শিক্ষা দিয়েছেন?", "When is true patience (Sabr) most praiseworthy according to the Prophet ﷺ?",
         ["বিপদের প্রথম ধাক্কার মুহূর্তে অটল ধৈর্যধারণে (ইন-নামাস সাবরু ইনদাস সাদমাতিল উলা)", "এক মাস পর", "সবকিছু শেষ হলে", "মানুষ দেখলে"], ["At the very first initial blow of a calamity or tragedy", "After 1 month", "When all ends", "When people watch"], 0,
         "বিপদ আসতেই ভেঙে না পড়ে 'ইন্না লিল্লাহি ওয়া ইন্না ইলাইহি রাজিউন' পাঠ করাই প্রকৃত সবরের পরিচয়।", "Verily, true patience is shown at the first impact of a calamity.", "সহীহ বুখারী: ১২৮৩", "Sahih Bukhari: 1283", "EASY"),

        ("কারো অনুপস্থিতিতে কোনো ব্যক্তি যদি তার মুসলিম ভাইয়ের সম্মান রক্ষা করে তার জন্য কী সুসংবাদ রয়েছে?", "What reward is promised to one who defends the honor of his absent Muslim brother?",
         ["কিয়ামতের দিন আল্লাহ তাআলা তার চেহারা থেকে জাহান্নামের আগুন হটিয়ে দেবেন", "টাকা পাবে", "মানুষ সম্মান দেবে", "ক্ষতি হবে"], ["Allah will protect his face from the fire of Hell on the Day of Judgment", "Gets money", "Honored by men", "Harm"], 0,
         "ভাইয়ের ইজ্জত রক্ষা করা মুমিনের কর্তব্য; এর বিনিময়ে আল্লাহ জাহান্নাম থেকে মুক্তি দেন।", "Whoever defends his brother's honor in his absence, Allah shields his face from Hell.", "জামে আত-তিরমিযী: ১৯৩১", "Jami at-Tirmidhi: 1931", "EASY"),

        ("লজ্জাশীলতা ও শালীনতা (Haya) সম্পর্কে রাসূলুল্লাহ ﷺ কী ঘোষণা করেছেন?", "What did the Prophet ﷺ state regarding the virtue of modesty (Haya)?",
         ["'লজ্জাশীলতা ঈমানেরই একটি বিশেষ অপরিহার্য শাখা এবং তা কেবল কল্যাণই আনে'", "লজ্জা দুর্বলতা", "লজ্জা ক্ষতি করে", "অপ্রয়োজনীয়"], ["'Modesty is an essential branch of faith, and modesty brings nothing but good'", "Modesty is weakness", "Harmful", "Unnecessary"], 0,
         "লজ্জাশীলতা ও ঈমান একে অপরের সাথে ওতপ্রোতভাবে জড়িত; একটি উঠে গেলে অপরটিও চলে যায়।", "Modesty and faith are companions together; when one is lifted, the other follows.", "মুসতাদরাকে হাকিম", "Mustadrak al-Hakim", "EASY"),

        ("কারো সাথে তিন দিনের বেশি কথা বন্ধ রাখা বা সম্পর্ক ত্যাগ করার ব্যাপারে শরীয়তের বিধান কী?", "What is the ruling on a Muslim boycotting or abandoning speaking to his brother over 3 days?",
         ["হারাম ও নিষিদ্ধ; তিন দিনের বেশি মুখ ফিরিয়ে থাকা মুমিনের জন্য বৈধ নয়", "জায়েজ", "উত্তম", "মুস্তাহাব"], ["Haram; it is unlawful for a Muslim to abandon his brother for more than 3 days", "Permissible", "Good", "Mustahab"], 0,
         "রাসূল ﷺ বলেন: এদের মধ্যে সর্বোত্তম ব্যক্তি সে যে প্রথমে এগিয়ে এসে সালাম বিনিময় করে।", "It is not lawful for a Muslim to boycott his brother for more than three days.", "সহীহ বুখারী: ৬০৭৭", "Sahih Bukhari: 6077", "EASY"),

        ("মানুষের গোপন ত্রুটি-বিচ্যুতি জনসমক্ষে প্রকাশ না করে গোপন রাখার সওয়াব কী?", "What is the divine reward for covering and concealing the faults of a fellow Muslim?",
         ["আল্লাহ তাআলা কিয়ামতের কঠিন দিনে দুনিয়া ও আখেরাতে তার দোষত্রুটি গোপন রাখবেন", "কোনো সওয়াব নেই", "সে দোষী হবে", "ক্ষতি হবে"], ["Allah will conceal and cover his faults in this world and on the Day of Resurrection", "No reward", "He is guilty", "Harm"], 0,
         "রাসূল ﷺ বলেন: যে ব্যক্তি কোনো মুসলিমের গোপনীয়তা ঢেকে রাখে, আল্লাহ কিয়ামতে তার গোপনীয়তা ঢেকে রাখবেন।", "Whoever conceals the faults of a Muslim, Allah will conceal his faults in both worlds.", "সহীহ মুসলিম: ২৬৯৯", "Sahih Muslim: 2699", "EASY"),

        ("ইসলামে কৃতজ্ঞতা প্রকাশে কেউ উপকার করলে তার জন্য কোন্ সর্বশ্রেষ্ঠ দোয়া করতে হয়?", "What is the highest expression of Islamic gratitude when someone does a favor?",
         ["'জাযাকাল্লাহু খাইরান' (আল্লাহ আপনাকে সর্বোত্তম প্রতিদান দান করুন)", "থ্যাংক ইউ", "ধন্যবাদ", "ভালো থাকুন"], ["'Jazakallahu Khayran' (May Allah reward you with the best)", "Thank you", "Thanks", "Stay well"], 0,
         "রাসূল ﷺ বলেন: যে ব্যক্তি উপকারের জবাবে 'জাযাকাল্লাহু খাইরান' বলে সে প্রশংসার সর্বোচ্চ পরাকাষ্ঠা প্রদর্শন করল।", "Whoever says 'Jazakallahu Khayran' has expressed the ultimate gratitude.", "জামে আত-তিরমিযী: ২০৩৫", "Jami at-Tirmidhi: 2035", "EASY"),

        ("রাগ সংবরণ করার সময় যখন চরম ক্ষোভ তৈরি হয় তখন সাথে সাথে কী পড়ার নির্দেশ রয়েছে?", "What should a person recite immediately when overwhelmed with intense anger?",
         ["'আউযুবিল্লাহি মিনাশ শায়তানির রাজীম' (বিতাড়িত শয়তান থেকে আল্লাহর আশ্রয় চাই)", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "আল্লাহু আকবার"], ["'A'udhu billahi minash-shaytanir rajeem' (I seek refuge in Allah from Satan)", "Subhanallah", "Alhamdulillah", "Allahu Akbar"], 0,
         "রাসূল ﷺ বলেন: আমি এমন একটি বাক্য জানি যা বললে রাগ দূর হয়ে যায়, তা হলো আউযুবিল্লাহ পাঠ করা।", "I know a word which, if he were to say it, his anger would go away: A'udhu billah.", "সহীহ বুখারী: ৩২৮২", "Sahih Bukhari: 3282", "EASY"),

        ("দাঁড়ানো অবস্থায় হঠাৎ প্রচণ্ড রাগ উপস্থিত হলে সুন্নাহ সম্মত শারীরিক কৌশল কী?", "What physical action did the Prophet ﷺ advise if anger overtakes a standing person?",
         ["বসে পড়া, তাতেও রাগ না কমলে মাটিতে শুয়ে পড়া অথবা ওযু করে নেওয়া", "চিৎকার করা", "দৌড় দেওয়া", "লাফালাফি করা"], ["Sit down immediately; if anger persists, lie down or perform Wudu", "Shout", "Run", "Jump"], 0,
         "শারীরিক অবস্থান পরিবর্তনের ফলে রক্তের উত্তেজনা প্রশমিত হয় এবং শয়তানের প্রভাব নষ্ট হয়।", "When one of you becomes angry while standing, let him sit down; if it goes away, good, otherwise lie down.", "সুনানে আবু দাউদ: ৪৭৮২", "Sunan Abi Dawud: 4782", "EASY"),

        ("প্রতিশ্রুতি ও ওয়াদা রক্ষা করা (Wafa al-Ahd) সম্পর্কে পবিত্র কুরআনের দ্ব্যর্থহীন ঘোষণা কী?", "What is the unequivocal command in Surah Al-Isra regarding fulfilling promises?",
         ["'তোমরা প্রতিশ্রুতি পূর্ণ করো, নিশ্চয়ই প্রতিশ্রুতি সম্পর্কে কিয়ামতে জবাবদিহি করতে হবে'", "ওয়াদা ভঙ্গ করা যায়", "ঐচ্ছিক বিষয়", "দরকার নেই"], ["'And fulfill every commitment; indeed, commitments will be questioned about' (Surah Al-Isra: 34)", "Can break", "Optional", "Not needed"], 0,
         "ওয়াদা ভঙ্গ করা মুনাফিকির চরিত্র; মুমিন জীবনের প্রতিটি অঙ্গীকার অক্ষরে অক্ষরে পালন করে।", "Fulfilling pledges is an accountability before Allah on the Day of Judgment.", "সূরা আল-ইসরা: ৩৪", "Surah Al-Isra: 34", "EASY"),

        ("ইসলামে নম্রতা ও কোমলতা (Rifq) অবলম্বন করার গুরুত্ব সম্পর্কে রাসূল ﷺ কী বলেছেন?", "What did Prophet Muhammad ﷺ say regarding the beauty of gentleness (Rifq)?",
         ["'নম্রতা যে জিনিসেই থাকে তাকে সৌন্দর্যমণ্ডিত করে, আর যা থেকে ছিনিয়ে নেওয়া হয় তাকে কলঙ্কিত করে'", "নম্রতা দুর্বলতা", "কঠোরতা ভালো", "নম্রতা ক্ষতিকর"], ["'Gentleness is not in anything except that it beautifies it, and not removed except that it ruins it'", "Gentleness is weakness", "Harshness good", "Harmful"], 0,
         "আল্লাহ নিজে নম্র এবং তিনি কঠোরতার বদলে নম্রতার মাধ্যমেই মহাকল্যাণ দান করেন।", "Verily, Allah is gentle and loves gentleness in all matters.", "সহীহ মুসলিম: ২৫৯৪", "Sahih Muslim: 2594", "EASY"),

        ("মানুষের সাথে বিতর্কে বা তর্কে জড়িয়ে নিজেকে সঠিক প্রমাণিত করার চেয়ে বিতর্ক পরিহারকারীর জন্য কী সুসংবাদ রয়েছে?", "What did the Prophet ﷺ guarantee for a person who abandons arguing even when in the right?",
         ["জান্নাতের উপকণ্ঠে একটি আলিশান প্রাসাদের নিশ্চয়তা", "টাকা দেওয়া", "যুদ্ধজয়ী", "ক্ষমতা লাভ"], ["A palace on the outskirts of Paradise for abandoning arguments even when right", "Given money", "War hero", "Power"], 0,
         "অপ্রয়োজনীয় বিতর্ক পরিহার করে অন্তরের শান্তি ও ভ্রাতৃত্ব রক্ষা করা অত্যন্ত মর্যাদাবান গুণ।", "I guarantee a house in the surroundings of Paradise for one who leaves arguing even if right.", "সুনানে আবু দাউদ: ৪৮০০", "Sunan Abi Dawud: 4800", "EASY"),

        ("পরিহাসচ্ছলে বা কৌতুক করেও মিথ্যা না বলা ব্যক্তির জন্য জান্নাতের কোন্ সুসংবাদ রয়েছে?", "What did the Prophet ﷺ guarantee for one who abandons lying even in jest/jokes?",
         ["জান্নাতের মধ্যভাগে একটি মহিমান্বিত প্রাসাদের গ্যারান্টি", "উপহার", "ধনদৌলত", "প্রশংসা"], ["A palace in the middle of Paradise for one who abandons lying even while joking", "Gifts", "Wealth", "Praise"], 0,
         "কৌতুক করেও মিথ্যা বলা ইসলামে নিষিদ্ধ; সত্যনিষ্ঠার জন্য জান্নাতের কেন্দ্রস্থলে ঘর নির্ধারিত।", "I guarantee a house in the middle of Paradise for one who leaves lying even when joking.", "সুনানে আবু দাউদ: ৪৮০০", "Sunan Abi Dawud: 4800", "EASY"),

        ("মানুষের মধ্যে উত্তম চরিত্রবান ব্যক্তির জান্নাতে অবস্থান সম্পর্কে রাসূল ﷺ কী ওয়াদা করেছেন?", "What did the Prophet ﷺ promise for the one who perfects his moral character?",
         ["জান্নাতের সর্বোচ্চ শিখরে একটি অনন্য প্রাসাদের নিশ্চয়তা", "দরজার কাছে ঘর", "মাঝারি ঘর", "ছোট ঘর"], ["A palace in the highest loftiest heights of Paradise", "House near gate", "Average", "Small"], 0,
         "সর্বোত্তম চরিত্রের অধিকারী ব্যক্তি জান্নাতে রাসূলুল্লাহ ﷺ-এর সবচেয়ে প্রিয় ও নিকটতম সঙ্গী হবে।", "I guarantee a house in the highest part of Paradise for one who makes his character good.", "সুনানে আবু দাউদ: ৪৮০০", "Sunan Abi Dawud: 4800", "EASY"),

        ("রাসূলুল্লাহ ﷺ বলেছেন: 'কিয়ামতের দিন আমার নিকট তোমাদের মধ্যে সবচেয়ে প্রিয় এবং সবচেয়ে নিকটবর্তী হবে সে ব্যক্তি যে...'", "The Prophet ﷺ said: 'The dearest and closest of you to me on the Day of Resurrection will be those who...'",
         ["তোমাদের মধ্যে সর্বোত্তম চরিত্রের অধিকারী", "সর্বাধিক ধনী", "সবচেয়ে লম্বা", "সবচেয়ে বেশি ভাষী"], ["Have the best moral character and gentlest nature among you", "Richest", "Tallest", "Most talkative"], 0,
         "উত্তম চরিত্র ও সুন্দর আচরণই রাসূল ﷺ-এর নৈকট্য ও জান্নাতের স্থায়ী আসন লাভের সর্বপ্রধান চাবিকাঠি।", "The dearest of you to me on Judgment Day are those with the best manners.", "জামে আত-তিরমিযী: ২০১Retrying after error 8", "Jami at-Tirmidhi: 2018", "EASY")
    ]
    return [('Q_AKH_' + str(i+1).zfill(3), 'islamic_akhlaq', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_17_dua():
    items = [
        ("ইসলামে দোয়াকে (Dua / Supplication) ইবাদতের ক্ষেত্রে কী মর্যাদা দেওয়া হয়েছে?", "What is the sublime status of Dua (Supplication) in Islamic worship as stated in Hadith?",
         ["'দোয়াই হলো ইবাদতের মূল মগজ ও সারনির্যাস (আদ-দোয়াউ হুয়াল ইবাদাহ)'", "কেবল আনুষ্ঠানিকতা", "ঐচ্ছিক বিষয়", "সাধারণ কথা"], ["'Supplication is the very essence and core of worship (Ad-Dua'u Huwal Ibadah)'", "Ritual", "Optional", "Normal talk"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: দোয়াই হলো আসল ইবাদত, অতঃপর তিনি কুরআন থেকে তিলাওয়াত করেন: 'তোমরা আমাকে ডাকো, আমি সাড়া দেব।' ", "The Prophet ﷺ said: Supplication is worship itself (Sunan Abi Dawud: 1479).", "সুনানে আবু দাউদ: ১৪৭৯", "Sunan Abi Dawud: 1479", "EASY"),

        ("কুরআনে কারীমে বর্ণিত সবচেয়ে সর্বজনীন, বরকতময় ও সর্বাধিক পঠিত সার্বিক কল্যাণের দোয়া কোনটি?", "Which is the most comprehensive and frequently recited Quranic supplication for both worlds?",
         ["'রাব্বানা আতিনা ফিদ-দুনিয়া হাসানাতাও ওয়া ফিল আখিরাতি হাসানাতাও ওয়া কিনা আযাবান নার'", "রাব্বানা যালামনা", "রাব্বির হামহুমা", "রাব্বি জিদনি ইলমা"], ["'Rabbana Atina fid-Dunya Hasanatan wa fil-Akhirati Hasanatan wa Qina Adhaban-Nar'", "Rabbana Zalamna", "Rabbir Hamhuma", "Rabbi Zidni Ilma"], 0,
         "দুনিয়ার সকল কল্যাণ, আখেরাতের মুক্তি এবং জাহান্নাম থেকে পরিত্রাণের এই দোয়া রাসূল ﷺ সর্বাধিক পাঠ করতেন।", "Our Lord, give us in this world that which is good and in the Hereafter good and protect us from Fire.", "সূরা আল-বাক্বারাহ: ২০১", "Surah Al-Baqarah: 201", "EASY"),

        ("সকালে ও সন্ধ্যায় পাঠ করার জন্য সাইয়্যিদুল ইস্তিগফার (সর্বশ্রেষ্ঠ ক্ষমা প্রার্থনা)-এর অতুলনীয় ফযীলত কী?", "What is the supreme virtue of reciting Sayyidul Istighfar with firm conviction?",
         ["যে দিনে বা রাতে বিশ্বাসের সাথে পাঠ করবে এবং মারা যাবে সে সরাসরি জান্নাতবাসী হবে", "ধনশালী হবে", "রোগমুক্ত হবে", "পাহাড়সম সোনা পাবে"], ["Whoever recites it with conviction and dies during that day or night will enter Paradise", "Rich", "Healed", "Gold"], 0,
         "সাইয়্যিদুল ইস্তিগফারে তাওহীদের স্বীকৃতি, রবের নেয়ামতের শুকরিয়া ও নিজের পাপের নিঃশর্ত ক্ষমা চাওয়া হয়েছে।", "Sayyidul Istighfar is the chief master of repentance guaranteeing Paradise for true believers.", "সহীহ বুখারী: ৬৩০৬", "Sahih Bukhari: 6306", "EASY"),

        ("দুনিয়া ও আখেরাতের সর্বপ্রকার বিপদ-আপদ, অলসতা ও ঋণগ্রস্ততা থেকে মুক্তির বিখ্যাত মাসনূন দোয়া কোনটি?", "Which comprehensive prophetic dua protects against anxiety, grief, helplessness, and debt?",
         ["'আল্লাহুম্মা ইন্নি আউযুবিকা মিনাল হাম্মি ওয়াল হাযান, ওয়াল আজযি ওয়াল কাসাল, ওয়াল জুবনি ওয়াল বুখল...'", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "লা ইলাহা ইল্লাল্লাহ"], ["'Allahumma inni a'udhu bika minal-hammi wal-hazan, wal-'ajzi wal-kasal, wal-jubni wal-bukhl...'", "Subhanallah", "Alhamdulillah", "La ilaha"], 0,
         "হযরত আনাস (রা.) বলেন: রাসূল ﷺ সর্বদা এই দোয়ার মাধ্যমে দুশ্চিন্তা ও ঋণের চাপ থেকে পানাহ চাইতেন।", "The Prophet ﷺ regularly supplicated for protection against worry, grief, incapacity, and overwhelming debt.", "সহীহ বুখারী: ২৮৯৩", "Sahih Bukhari: 2893", "EASY"),

        ("ঘুম থেকে জাগ্রত হওয়ার সাথে সাথে কোন্ শুকরিয়ার দোয়া পাঠ করা সুন্নাত?", "Which gratitude supplication is Sunnah to recite immediately upon waking up?",
         ["'আলহামদুলিল্লাহিল্লাযী আহইয়ানা বা'দা মা আমাতানা ওয়া ইলাইহিন নুশূর'", "বিসমিল্লাহ", "সুবহানাল্লাহ", "আল্লাহু আকবার"], ["'Alhamdulillahilladhee Ahyana ba'da ma amatana wa ilayhin-nushoor'", "Bismillah", "Subhanallah", "Allahu Akbar"], 0,
         "সমস্ত প্রশংসা আল্লাহর যিনি আমাদের মৃত্যুর (ঘুমের) পর পুনরুজ্জীবিত করলেন এবং তাঁরই কাছে সবার পুনরুত্থান।", "All praise is for Allah Who gave us life after taking it, and unto Him is the resurrection.", "সহীহ বুখারী: ৬৩১২", "Sahih Bukhari: 6312", "EASY"),

        ("বাড়ি থেকে বের হওয়ার সময় শয়তান ও বিপদের হাত থেকে রক্ষাকবচ হিসেবে কোন্ দোয়া পাঠ করতে হয়?", "Which protective supplication guarantees guidance and safety when stepping out of home?",
         ["'বিসমিল্লাহি তাওয়াক্কালতু আলাল্লাহ, লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ'", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "আল্লাহু আকবার"], ["'Bismillahi tawakkaltu 'alallah, la hawla wa la quwwata illa billah'", "Subhanallah", "Alhamdulillah", "Allahu Akbar"], 0,
         "এই দোয়া পড়লে ফেরেশতারা ঘোষণা দেন: তুমি সঠিক পথ পেলে, নিরাপদ হলে; এবং শয়তান দূরে সরে যায়।", "Whoever says this upon exiting, angels say: You are guided, defended, and protected.", "সুনানে আবু দাউদ: ৫০৯৫", "Sunan Abi Dawud: 5095", "EASY"),

        ("টয়লেট বা বাথরুমে প্রবেশের পূর্বে অপবিত্র দুষ্ট জিন ও শয়তান থেকে বাঁচতে কোন্ দোয়া পড়তে হয়?", "Which supplication protects against male and female devils (Khubuth & Khaba'ith) before entering the toilet?",
         ["'আল্লাহুম্মা ইন্নি আউযুবিকা মিনাল খুবুসি ওয়াল খাবায়িস'", "বিসমিল্লাহ", "আলহামদুলিল্লাহ", "সুবহানাল্লাহ"], ["'Allahumma inni a'udhu bika minal-khubuthi wal-khaba'ith'", "Bismillah", "Alhamdulillah", "Subhanallah"], 0,
         "টয়লেটে প্রবেশের পূর্বে বাম পা দিয়ে এবং এই দোয়ার মাধ্যমে শয়তানের কুদৃষ্টি থেকে পর্দা স্থাপিত হয়।", "Enter with left foot reciting refuge from male and female wicked spirits.", "সহীহ বুখারী: ১৪২", "Sahih Bukhari: 142", "EASY"),

        ("টয়লেট বা শৌচাগার থেকে বের হওয়ার পর আল্লাহর শুকরিয়ায় কোন্ সংক্ষিপ্ত দোয়া পাঠ করতে হয়?", "Which supplication is recited upon exiting the restroom with the right foot?",
         ["'গুফরানাকা' (হে আল্লাহ! আপনার ক্ষমা প্রার্থনা করছি)", "আলহামদুলিল্লাহ", "সুবহানাল্লাহ", "বিসমিল্লাহ"], ["'Ghufranaka' (I seek Your forgiveness, O Allah)", "Alhamdulillah", "Subhanallah", "Bismillah"], 0,
         "বের হয়ে ডান পা রেখে 'গুফরানাকা' বলা সুন্নাত।", "Step out with the right foot saying: Ghufranaka.", "সুনানে আবু দাউদ: ৩০", "Sunan Abi Dawud: 30", "EASY"),

        ("খাবার গ্রহণের শুরুতে 'বিসমিল্লাহ' বলতে ভুলে গেলে খাওয়ার মাঝে মনে পড়লে কী পড়তে হয়?", "What should one recite if forgetting Bismillah before meals and remembering mid-meal?",
         ["'বিসমিল্লাহি আউওয়ালাহু ওয়া আখিরাহু'", "আলহামদুলিল্লাহ", "সুবহানাল্লাহ", "আল্লাহু আকবার"], ["'Bismillahi Awwalahu wa Akhirahu'", "Alhamdulillah", "Subhanallah", "Allahu Akbar"], 0,
         "এই দোয়া পড়ার সাথে সাথে শয়তান যা খাচ্ছিল তা বমি করে ফেলে দিতে বাধ্য হয়।", "In the name of Allah at its beginning and its end.", "সুনানে আবু দাউদ: ৩৭৬৭", "Sunan Abi Dawud: 3767", "EASY"),

        ("খাবার শেষ করার পর আল্লাহর অফুরন্ত নিয়ামতের কৃতজ্ঞতায় কোন্ দোয়া পাঠ করা সুন্নাত?", "Which supplication of gratitude is Sunnah to recite upon finishing a meal?",
         ["'আলহামদুলিল্লাহিল্লাযী আত'আমানা ওয়া সাক্বানা ওয়া জা'আলানা মিনাল মুসলিমীন'", "বিসমিল্লাহ", "সুবহানাল্লাহ", "আল্লাহু আকবার"], ["'Alhamdulillahilladhee At'amana wa Saqana wa Ja'alana minal Muslimeen'", "Bismillah", "Subhanallah", "Allahu Akbar"], 0,
         "সমস্ত প্রশংসা আল্লাহর যিনি আমাদের আহার করালেন, পান করালেন এবং মুসলিম বানালেন।", "Praise be to Allah Who fed us, gave us drink, and made us Muslims.", "জামে আত-তিরমিযী: ৩৪৫৭", "Jami at-Tirmidhi: 3457", "EASY"),

        ("মসজিদে প্রবেশের সময় ডান পা রেখে কোন্ রহমত প্রার্থনার দোয়া পাঠ করতে হয়?", "Which supplication is recited entering the mosque with the right foot?",
         ["'আল্লাহুম্মাফ তাহলী আবওয়াবা রাহমাতিকা' (হে আল্লাহ! আমার জন্য আপনার রহমতের দ্বার খুলে দিন)", "বিসমিল্লাহ", "আলহামদুলিল্লাহ", "সুবহানাল্লাহ"], ["'Allahummaf-tah lee abwaba rahmatika' (O Allah, open for me the gates of Your mercy)", "Bismillah", "Alhamdulillah", "Subhanallah"], 0,
         "মসজিদে প্রবেশের সময় দরুদ ও সালামসহ এই দোয়া পাঠ করা সুন্নাত।", "Recite Salawat and ask Allah to open the gates of His mercy.", "সহীহ মুসলিম: ৭১৩", "Sahih Muslim: 713", "EASY"),

        ("মসজিদ থেকে বের হওয়ার সময় বাম পা রেখে কোন্ অনুগ্রহ প্রার্থনার দোয়া পাঠ করতে হয়?", "Which supplication is recited exiting the mosque with the left foot?",
         ["'আল্লাহুম্মা ইন্নি আসআলুকা মিন ফাদলিকা' (হে আল্লাহ! আমি আপনার অনুগ্রহ প্রার্থনা করছি)", "আলহামদুলিল্লাহ", "বিসমিল্লাহ", "সুবহানাল্লাহ"], ["'Allahumma inni as-aluka min fadlika' (O Allah, I ask of You from Your bounty)", "Alhamdulillah", "Bismillah", "Subhanallah"], 0,
         "মসজিদ থেকে বের হওয়ার সময় আল্লাহর পার্থিব ও আধ্যাত্মিক অনুগ্রহ (ফজল) চাওয়া সুন্নাত।", "Step out asking for Allah's abundant bounty and grace.", "সহীহ মুসলিম: ৭১৩", "Sahih Muslim: 713", "EASY"),

        ("যানবাহনে আরোহণের পর কোন্ কুরআনিক তাসবীহ ও দোয়া পাঠ করা সুন্নাত?", "Which Quranic supplication is Sunnah to recite when boarding a vehicle or riding?",
         ["'সুবহানাল্লাযী সাখখারা লানা হাযা ওয়া মা কুন্না লাহু মুকরিনীনা, ওয়া ইন্না ইলা রাব্বিনা লামুনক্বালিবূন'", "বিসমিল্লাহ", "আলহামদুলিল্লাহ", "আল্লাহু আকবার"], ["'Subhanalladhee sakhkhara lana hadha wa ma kunna lahu muqrineen, wa inna ila Rabbina lamunqaliboon'", "Bismillah", "Alhamdulillah", "Allahu Akbar"], 0,
         "পবিত্র সেই মহান সত্তা যিনি এই বাহনকে আমাদের বশীভূত করেছেন অথচ আমরা একে নিয়ন্ত্রণ করতে সক্ষম ছিলাম না।", "Glory to Him Who subjected this to us, and we were not capable of it alone (Surah Az-Zukhruf: 13-14).", "সূরা আয-যুখরুফ: ১৩-১৪", "Surah Az-Zukhruf: 13-14", "EASY"),

        ("আয়নায় নিজের চেহারা দেখার সময় উত্তম চরিত্র কামনায় কোন্ চমৎকার দোয়া পড়তে হয়?", "Which dua is recited when looking into a mirror seeking beauty of character?",
         ["'আল্লাহুম্মা কামা হাস্সানতা খালক্বী ফাহাস্সিন খুলুক্বী' (হে আল্লাহ! যেমন রূপ সুন্দর করেছেন চরিত্রও সুন্দর করুন)", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "মাশাআল্লাহ"], ["'Allahumma kama hassanta khalqee fa-hassin khuluqee' (O Allah, as You made my creation good, beautify my character)", "Subhanallah", "Alhamdulillah", "MashaAllah"], 0,
         "রাসূল ﷺ আয়নায় দৃষ্টিপাতের সময় অভ্যন্তরীণ চরিত্রের সৌন্দর্যের জন্য এই দোয়া করতেন।", "O Allah, as You have perfected my physical form, perfect my moral character.", "মুসনাদে আহমাদ: ২৪৩৯২", "Musnad Ahmad: 24392", "EASY"),

        ("নতুন পোশাক পরিধান করার সময় আল্লাহর শুকরিয়ায় কোন্ দোয়া পাঠ করতে হয়?", "Which supplication is recited upon putting on new clothes?",
         ["'আলহামদুলিল্লাহিল্লাযী কাসানী হাযাস সাওবা ওয়া রাযাক্বানীহি মিন গাইরি হাওলিম মিন্নী ওয়ালা কুওয়াহ'", "বিসমিল্লাহ", "সুবহানাল্লাহ", "আল্লাহু আকবার"], ["'Alhamdulillahilladhee kasanee hadhath-thawba wa razaqaneehi min ghayri hawlim-minnee wa la quwwah'", "Bismillah", "Subhanallah", "Allahu Akbar"], 0,
         "যে ব্যক্তি এই দোয়া পড়ে তার অতীত ও ভবিষ্যতের সকল ছোট গুনাহ মাফ হয়ে যায়।", "Whoever recites this when wearing clothes has his past sins expiated.", "সুনানে আবু দাউদ: ৪০২৩", "Sunan Abi Dawud: 4023", "EASY"),

        ("রোগী দেখতে গেলে রোগীর দ্রুত সুস্থতা ও কাফফারার কামনায় কোন্ সান্ত্বনাসূচক বাক্য ও দোয়া পড়তে হয়?", "What comforting supplication did the Prophet ﷺ recite when visiting a sick patient?",
         ["'লা বা'সা তুহূরুন ইনশাআল্লাহ' এবং ৭ বার 'আসআলুল্লাহাল আযীম রাব্বাল আরশিল আযীম আইঁ ইয়াশফিয়াকা'", "আল্লাহ মাফ করুক", "বিপদ কেটে যাবে", "ভালো থাকুন"], ["'La ba'sa tahoorun InshaAllah' & 7 times: 'As-alullahal 'Azeem Rabbal 'Arshil 'Azeem an yashfiyaka'", "Allah forgive", "Danger gone", "Stay well"], 0,
         "রোগীর সামনে এই দোয়া সাতবার পাঠ করলে আল্লাহ তাকে অবধারিত রোগমুক্তি দান করেন যদি মৃত্যুর সময় না এসে থাকে।", "Seven times recitation brings cure by Allah's permission unless death has arrived.", "সুনানে আবু দাউদ: ৩১০৬", "Sunan Abi Dawud: 3106", "EASY"),

        ("কবর জিয়ারত করার সময় কবরবাসীদের উদ্দেশ্যে সালাম ও দোয়ার মাসনূন বাক্য কোনটি?", "What is the Sunnah greeting and supplication recited when visiting the graveyard (Qabr)?",
         ["'আসসালামু আলাইকুম আহলাদ দিয়ারী মিনাল মু'মিনীন... ওয়া ইন্না ইনশাআল্লাহু বিকুম লাহিকূন'", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "আল্লাহু আকবার"], ["'Assalamu alaykum ahlad-diyari minal mu'mineen... wa inna InshaAllahu bikum lahiqoon'", "Subhanallah", "Alhamdulillah", "Allahu Akbar"], 0,
         "হে কবরবাসী মুমিনগণ! তোমাদের ওপর শান্তি বর্ষিত হোক; নিশ্চয়ই আমরাও অচিরেই তোমাদের সাথে মিলিত হবো।", "Peace be upon you, O dwellers of the graves, believers and Muslims; we shall soon follow you.", "সহীহ মুসলিম: ৯৭৫", "Sahih Muslim: 975", "EASY"),

        ("জ্ঞান ও স্মৃতিশক্তি বৃদ্ধির জন্য পবিত্র কুরআনে হযরত মূসা (আ.)-এর কোন্ বিখ্যাত দোয়া শেখানো হয়েছে?", "Which famous Quranic prayer of Prophet Musa (AS) is recited to expand knowledge and speech?",
         ["'রাব্বিশ রাহলী সাদরী, ওয়া ইয়াসসির লী আমরী, ওয়াহলুল উক্বদাতাম মিল লিসানী, ইয়াফক্বাহু ক্বাওলী'", "রাব্বানা আতিনা", "রাব্বি জিদনি ইলমা", "সুবহানাল্লাহ"], ["'Rabbish-rah lee sadree, wa yassir lee amree, wahlul 'uqdatam-mil-lisanee, yafqahoo qawlee'", "Rabbana Atina", "Rabbi Zidni Ilma", "Subhanallah"], 0,
         "হে আমার রব! আমার বক্ষ প্রশস্ত করে দিন, আমার কাজ সহজ করুন এবং আমার জিহ্বার জড়তা দূর করে দিন যাতে তারা আমার কথা বুঝতে পারে।", "My Lord, expand for me my breast and ease for me my task and untie the knot from my tongue (Surah Ta-Ha: 25-28).", "সূরা ত্বা-হা: ২৫-২৮", "Surah Ta-Ha: 25-28", "EASY"),

        ("কুরআনে অতিরিক্ত জ্ঞান ও প্রজ্ঞা বৃদ্ধির জন্য সরাসরি আল্লাহর কাছে প্রার্থনার দোয়া কোনটি?", "Which concise Quranic prayer asks Allah for direct increase in knowledge?",
         ["'রাব্বি যিদনী ইলমা' (হে আমার রব! আমার জ্ঞান বৃদ্ধি করে দিন)", "রাব্বানা যালামনা", "রাব্বির হামহুমা", "রাব্বানা তাকাব্বাল"], ["'Rabbi Zidnee 'Ilma' (My Lord, increase me in knowledge)", "Rabbana Zalamna", "Rabbir Hamhuma", "Rabbana Taqabbal"], 0,
         "আল্লাহ তাঁর রাসূলকে নির্দেশ দিয়েছেন: বলুন, হে আমার রব! আমার জ্ঞান বৃদ্ধি করে দিন।", "And say: My Lord, increase me in knowledge (Surah Ta-Ha: 114).", "সূরা ত্বা-হা: ১১৪", "Surah Ta-Ha: 114", "EASY"),

        ("পিতা-মাতার জন্য রহমত ও মাগফিরাত কামনায় কুরআনে শেখানো অবিস্মরণীয় দোয়া কোনটি?", "Which immortal Quranic supplication did Allah teach us to pray for parents' mercy?",
         ["'রাব্বির হামহুমা কামা রাব্বায়ানী সাগীরা' (হে আমার রব! তাদের প্রতি দয়া করুন যেমন তারা শৈশবে আমাকে লালন করেছিলেন)", "রাব্বানা আতিনা", "রাব্বানা যালামনা", "রাব্বি জিদনি ইলমা"], ["'Rabbir-hamhuma kama rabbayanee sagheera' (My Lord, have mercy upon them as they raised me when small)", "Rabbana Atina", "Rabbana Zalamna", "Rabbi Zidni Ilma"], 0,
         "সন্তানের জন্য সর্বদা মাতা-পিতার সুস্থতা ও পরকালীন রহমতের এই দোয়া করা অন্যতম প্রধান ইবাদত।", "My Lord, have mercy upon them as they brought me up when I was small (Surah Al-Isra: 24).", "সূরা আল-ইসরা: ২৪", "Surah Al-Isra: 24", "EASY"),

        ("নেক সন্তান এবং স্ত্রী-পরিবারের মাধ্যমে চোখের শীতলতা লাভের কুরআনিক দোয়া কোনটি?", "Which Quranic supplication is recited for righteous spouses and comforting offspring?",
         ["'রাব্বানা হাবলানা মিন আযওয়াজিনা ওয়া যুররিয়্যাতিনা কুররাতা আ'ইউনিঁও ওয়াজ'আলনা লিল মুত্তাক্বীনা ইমামা'", "রাব্বানা আতিনা", "রাব্বানা যালামনা", "রাব্বির হামহুমা"], ["'Rabbana hab lana min azwajina wa dhurriyyatina qurrata a'yunin waj'alna lil-muttaqeena imama'", "Rabbana Atina", "Rabbana Zalamna", "Rabbir Hamhuma"], 0,
         "হে আমাদের রব! আমাদের স্ত্রীদের ও সন্তানদের আমাদের জন্য চোখের শীতলতা স্বরূপ বানিয়ে দিন এবং মুত্তাকীদের নেতা করুন।", "Grant us from among our wives and offspring comfort to our eyes and make us a leader for the righteous.", "সূরা আল-ফুরকান: ৭৪", "Surah Al-Furqan: 74", "EASY"),

        ("বিপদ-আপদে বা কারও মৃত্যুর সংবাদ শুনলে ধৈর্যের সাথে পাঠ করার মূল কুরআনিক বাক্য কোনটি?", "What is the core Istirja sentence recited with patient acceptance upon any tragedy?",
         ["'ইন্না লিল্লাহি ওয়া ইন্না ইলাইহি রাজিউন' (নিশ্চয়ই আমরা আল্লাহর এবং তাঁরই কাছে ফিরে যাব)", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "আল্লাহু আকবার"], ["'Inna Lillahi wa Inna Ilayhi Raji'oon' (Indeed we belong to Allah and to Him we return)", "Subhanallah", "Alhamdulillah", "Allahu Akbar"], 0,
         "বিপদে এই বাক্য পাঠকারীকে আল্লাহ অফুরন্ত রহমত, মাগফিরাত ও উত্তম বিকল্প দান করেন।", "Who, when disaster strikes them, say: Indeed we belong to Allah, and indeed to Him we will return.", "সূরা আল-বাক্বারাহ: ১৫৬", "Surah Al-Baqarah: 156", "EASY"),

        ("হযরত ইউনুস (আ.)-এর দোয়া 'দোয়ায়ে ইউনুস' পাঠ করলে যে কোনো সংকট থেকে মুক্তির নিশ্চয়তায় রাসূল ﷺ কী বলেছেন?", "What did the Prophet ﷺ guarantee regarding the supplication of Yunus (AS) in difficulty?",
         ["যে কোনো মুসলিম কোনো সংকটে এই দোয়া দিয়ে প্রার্থনা করলে আল্লাহ অবশ্যই তার দোয়া কবুল করেন", "সন্দেহ আছে", "কখনো হয় কখনো হয় না", "কেবল নবীদের জন্য"], ["Any Muslim who supplicates with it in distress, Allah will surely answer his prayer", "Doubtful", "Sometimes", "Only for Prophets"], 0,
         "লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নি কুনতু মিনাজ জোয়ালিমীন - সংকট মোচনের মহৌষধ।", "No Muslim supplicates with the prayer of Dhun-Nun except that Allah responds to him.", "জামে আত-তিরমিযী: ৩৫০৫", "Jami at-Tirmidhi: 3505", "EASY"),

        ("আল্লাহর কাছে সবচেয়ে প্রিয় ও সম্মানিত চারটি জিকিরের বাক্য কোন্গুলো?", "Which are the four most beloved phrases of remembrance (Dhikr) to Allah?",
         ["সুবহানাল্লাহ, আলহামদুলিল্লাহ, লা ইলাহা ইল্লাল্লাহ এবং আল্লাহু আকবার", "বিসমিল্লাহ ও ইনশাআল্লাহ", "মাশাআল্লাহ ও লা হাওলা", "আস্তাগফিরুল্লাহ"], ["Subhanallah, Alhamdulillah, La ilaha illallah, and Allahu Akbar", "Bismillah & InshaAllah", "MashaAllah & La hawla", "Astaghfirullah"], 0,
         "রাসূল ﷺ বলেন: আল্লাহর কাছে সর্বাধিক প্রিয় বাক্য চারটি; এর যেকোনো একটি দিয়ে শুরু করতে কোনো ক্ষতি নেই।", "The most beloved words to Allah are four: Subhanallah, Alhamdulillah, La ilaha illallah, Allahu Akbar.", "সহীহ মুসলিম: ২১৩৭", "Sahih Muslim: 2137", "EASY"),

        ("জিহ্বার জন্য অতি সহজ কিন্তু মীজানের পাল্লায় অত্যন্ত ভারী এবং রহমানের অতি প্রিয় দুটি বাক্য কী?", "Which two phrases are light on the tongue, heavy on the Scales, and beloved to the Most Merciful?",
         ["'সুবহানাল্লাহি ওয়া বিহামদিহি, সুবহানাল্লাহিল আযীম'", "লা হাওলা ওয়ালা কুওয়াতা", "আলহামদুলিল্লাহি রাব্বিল আলামীন", "আল্লাহু আকবার কাবিরা"], ["'Subhanallahi wa bihamdihi, Subhanallahil-'Azeem'", "La hawla wa la quwwata", "Alhamdulillahi Rabbil Alameen", "Allahu Akbar Kabeera"], 0,
         "সহীহ আল-বুখারী গ্রন্থের সর্বশেষ হাদিস হিসেবে এই মহামূল্যবান জিকিরটি ইমাম বুখারী সংকলন করেছেন।", "Two words light on the tongue, heavy on the scale, beloved to Ar-Rahman (Sahih Bukhari: 6682).", "সহীহ বুখারী: ৬৬৮২", "Sahih Bukhari: 6682", "EASY"),

        ("জান্নাতের রত্নভাণ্ডারসমূহের (Treasures of Paradise) অন্যতম ভাণ্ডার বলা হয়েছে কোন্ শক্তিশালী জিকিরকে?", "Which powerful remembrance is described as a treasure from the treasures of Jannah?",
         ["'লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ' (আল্লাহর সাহায্য ছাড়া পাপ থেকে ফেরার ও নেক কাজ করার কোনো শক্তি নেই)", "সুবহানাল্লাহ", "আলহামদুলিল্লাহ", "আল্লাহু আকবার"], ["'La hawla wa la quwwata illa billah' (There is no power and no strength except with Allah)", "Subhanallah", "Alhamdulillah", "Allahu Akbar"], 0,
         "মানুষের সকল সীমাবদ্ধতা দূর করে সম্পূর্ণ রবের কুদরতের ওপর সমর্পণ করার এই বাক্য জান্নাতের মহা রত্ন।", "Recite 'La hawla wa la quwwata illa billah' for it is a treasure of Paradise.", "সহীহ বুখারী: ৪২০৫", "Sahih Bukhari: 4205", "EASY"),

        ("যে ব্যক্তি সকালে ও সন্ধ্যায় ১০০ বার 'সুবহানাল্লাহি ওয়া বিহামদিহি' পাঠ করে তার সওয়াব কেমন?", "What is the immense reward of reciting 'Subhanallahi wa bihamdihi' 100 times daily?",
         ["তার সমুদ্রের ফেনা পরিমাণ গুনাহ থাকলেও আল্লাহ তা সম্পূর্ণরূপে মুছে ক্ষমা করে দেন", "১টি সওয়াব", "ধনী হবে", "পাহাড় পরিমাণ স্বর্ণ পাবে"], ["All his minor sins are wiped out even if they were like the foam of the ocean", "1 reward", "Rich", "Gold mountain"], 0,
         "রাসূল ﷺ বলেন: কিয়ামতের দিন তার চেয়ে শ্রেষ্ঠ আমল নিয়ে আর কেউ আসতে পারবে না যে এর সমান বা বেশি পড়েছে।", "Whoever says it 100 times a day, his sins are forgiven even if like foam of sea.", "সহীহ বুখারী: ৬৪০৫", "Sahih Bukhari: 6405", "EASY"),

        ("দৈনিক ১০০ বার 'লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু...' পাঠ করার বিশেষ ফযীলত কী?", "What is the fourfold reward of reciting the comprehensive Tahlil 100 times daily?",
         ["১০টি গোলাম মুক্ত করার সওয়াব, ১০০ নেকি লাভ, ১০০ গুনাহ মাফ এবং সারাদিন শয়তান থেকে পূর্ণ নিরাপত্তা", "১টি রোজা", "১টি হজ", "১টি ওমরাহ"], ["Reward of freeing 10 slaves, 100 good deeds, 100 sins erased, and complete shield from Satan", "1 Fast", "1 Hajj", "1 Umrah"], 0,
         "সকাল থেকে সন্ধ্যা পর্যন্ত শয়তানের কোনো প্ররোচনা তার ক্ষতি করতে পারে না।", "Equivalent to freeing ten slaves, 100 rewards written, 100 sins wiped, and shielded from Satan all day.", "সহীহ বুখারী: ৩২৯৭", "Sahih Bukhari: 3297", "EASY"),

        ("ঘুমানোর পূর্বে সূরা ইখলাস, ফালাক ও নাস ৩ বার পড়ে দুই হাতের তালুতে ফুঁ দিয়ে সারা শরীরে হাত বুলানোকে কী বলে?", "What is the Sunnah practice of reciting the 3 Quls, blowing into palms, and wiping over body before sleep?",
         ["রাসূল ﷺ-এর নিয়মিত শয়নকালীন সুন্নাত রুকইয়াহ ও নিরাপত্তা আমল", "চিকিৎসা পদ্ধতি", "সাধারণ অভ্যাস", "ঐচ্ছিক সাজসজ্জা"], ["The Prophet's regular nightly Sunnah of self-Ruqyah and divine protection", "Medical care", "Habit", "Optional"], 0,
         "মাথা ও মুখমণ্ডল থেকে শুরু করে শরীরের সম্মুখভাগে যতদূর সম্ভব হাত বুলানো সুন্নাত।", "The Prophet ﷺ did this 3 times every night before sleeping.", "সহীহ বুখারী: ৫০১৭", "Sahih Bukhari: 5017", "EASY"),

        ("সালাত সমাপ্তির পর ফরজের শেষে ৩৩ বার সুবহানাল্লাহ, ৩৩ বার আলহামদুলিল্লাহ ও ৩৩ বার আল্লাহু আকবার পাঠ করাকে কী বলে?", "What is the essential post-prayer Tasbih Fatimi completed with the 100th Tahlil?",
         ["তাসবীহে ফাতেমী", "তাসবীহে ইউনুস", "তাসবীহে তারাবীহ", "তাসবীহে তাহাজ্জুদ"], ["Tasbih Fatimi", "Tasbih Yunus", "Tasbih Tarawih", "Tasbih Tahajjud"], 0,
         "৯৯ বার পড়ে শততম বারে 'লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু...' বললে সমুদ্রের ফেনা সমপরিমাণ গুনাহও মাফ হয়।", "Reciting 33 Subhanallah, 33 Alhamdulillah, 33 Allahu Akbar, completing 100 with Tahlil.", "সহীহ মুসলিম: ৫৯৭", "Sahih Muslim: 597", "EASY"),

        ("দোয়া কবুল হওয়ার সবচেয়ে বরকতময় ও নিশ্চিত সময়সমূহের মধ্যে কোনটি অন্যতম?", "Which is among the most auspicious times when supplications (Dua) are assuredly answered?",
         ["রাতের শেষ তৃতীয়াংশে (তাহাজ্জুদের সময়) এবং সিজদারত অবস্থায়", "দুপুরে", "খাবার সময়", "হাটার সময়"], ["In the last third of the night and during Sujood (Prostration)", "Midday", "While eating", "While walking"], 0,
         "আল্লাহ রাতের শেষ প্রহরে ডাকেন: কে আছ দোয়া করার, আমি তার দোয়া কবুল করব।", "Our Lord descends in the last third of the night asking: Who is calling upon Me that I may answer?", "সহীহ বুখারী: ১১৪৫", "Sahih Bukhari: 1145", "EASY"),

        ("আযান ও ইকামতের মধ্যবর্তী সময়ের দোয়ার মর্যাদা সম্পর্কে হাদিসে কী বলা হয়েছে?", "What did the Prophet ﷺ state regarding supplication between the Adhan and Iqamah?",
         ["'আযান ও ইকামতের মধ্যবর্তী দোয়া কখনোই ফিরিয়ে দেওয়া হয় না'", "মাঝে মাঝে হয়", "কবুল হয় না", "দেরি হয়"], ["'Supplication made between the Adhan and Iqamah is never rejected'", "Sometimes", "Not answered", "Delayed"], 0,
         "সালাতের পূর্বের এই বরকতময় সংক্ষিপ্ত সময়কে দোয়ায় কাজে লাগানো পরম লাভজনক।", "Supplication between Adhan and Iqamah is not rejected, so supplicate.", "জামে আত-তিরমিযী: ২১২", "Jami at-Tirmidhi: 212", "EASY"),

        ("বৃষ্টি বর্ষণের সময় দোয়ার মর্যাদা কেমন যা হাদিসে বর্ণিত হয়েছে?", "What is the status of making Dua during the falling of rain?",
         ["দোয়া কবুলের অন্যতম বিশেষ বরকতময় রহমতের সময়", "দোয়া করা নিষেধ", "সাধারণ সময়", "মাকরুহ সময়"], ["A special blessed time of divine mercy when supplications are accepted", "Forbidden", "Ordinary time", "Disliked"], 0,
         "বৃষ্টির সময় আল্লাহ রহমতের দুয়ার উন্মুক্ত করে দেন, তাই সে সময় দোয়া কবুল হয়।", "Seek acceptance of supplication when armies clash, when rain falls, and when prayer is established.", "আল-মুস্তাদরাক", "Al-Mustadrak", "EASY"),

        ("জুমার দিনের কোন্ বিশেষ সময়ে বান্দার সকল নেক দোয়া নিশ্চিতরূপে কবুল হওয়ার মুহূর্ত (সাআতুল ইজাবাহ) রয়েছে?", "During which specific window on Friday is the golden Hour of Acceptance (Sa'at al-Ijabah)?",
         ["আসর থেকে মাগরিবের মধ্যবর্তী শেষ সময়ে (অথবা দুই খুতবার মাঝে)", "সকালে", "মাঝরাতে", "ফজরের পূর্বে"], ["The final hour between Asr and Maghrib (or during the Khutbah sitting)", "Morning", "Midnight", "Before Fajr"], 0,
         "রাসূল ﷺ বলেন: জুমার দিন আসরের শেষ প্রহরে সেই শুভ মুহূর্তটি অনুসন্ধান করো।", "Seek the hour of acceptance on Friday in the last hour after Asr.", "সুনানে আবু দাউদ: ১০৪৮", "Sunan Abi Dawud: 1048", "EASY"),

        ("দোয়া করার সময় অন্তরের একাগ্রতা ও তাওয়াক্কুলের গুরুত্ব সম্পর্কে রাসূল ﷺ কী বলেছেন?", "What did the Prophet ﷺ command regarding certainty of faith while supplicating to Allah?",
         ["'তোমরা নিশ্চিত কবুলিয়তের পূর্ণ আস্থা ও বিশ্বাস নিয়ে আল্লাহর কাছে দোয়া করো'", "সন্দেহ রেখে দোয়া করো", "পরীক্ষা করো", "আমলে নির্ভর করো"], ["'Call upon Allah while you are absolutely certain of His response'", "Doubtful", "Testing Allah", "Rely on deeds"], 0,
         "আল্লাহ কোনো অমনোযোগী, উদাসীন ও বেপরোয়া অন্তরের দোয়া কবুল করেন না।", "Know that Allah does not answer a supplication from an inattentive, heedless heart.", "জামে আত-তিরমিযী: ৩৪৭৯", "Jami at-Tirmidhi: 3479", "EASY")
    ]
    return [('Q_DUA_' + str(i+1).zfill(3), 'dua_azkar', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_cat_18_aqeedah():
    items = [
        ("ইসলামের মৌলিক আকিদার মূল ভিত্তি তাওহীদকে প্রধানত কয়টি শাখায় বিভক্ত করে আলোচনা করা হয়?", "Into how many primary categories is Islamic Monotheism (Tawheed) categorized by scholars?",
         ["৩টি শাখা (তাওহীদুল রুবূবিয়্যাহ, তাওহীদুল উলূহিয়্যাহ ও তাওহীদুল আসমা ওয়াস সিফাত)", "২টি", "৫টি", "৪টি"], ["3 Categories (Rububiyyah, Uluhiyyah, and Asma wa-Sifat)", "2", "5", "4"], 0,
         "আল্লাহকে একমাত্র সৃষ্টিকর্তা, একমাত্র উপাস্য এবং তাঁর অনুপম নাম ও গুণের একত্বে বিশ্বাস করা।", "Tawheed consists of Lordship (Rububiyyah), Worship (Uluhiyyah), and Names & Attributes (Asma wa Sifat).", "কিতাবুত তাওহীদ", "Kitab at-Tawheed", "EASY"),

        ("তাওহীদুল রুবূবিয়্যাহ (Tawheed ar-Rububiyyah) বলতে কী বোঝায়?", "What does Tawheed ar-Rububiyyah (Oneness of Lordship) signify?",
         ["আল্লাহকে সমগ্র মহাবিশ্বের একমাত্র স্রষ্টা, মালিক, পালনকর্তা ও নিয়ন্ত্রক হিসেবে বিশ্বাস করা", "কেবল নামাজ পড়া", "মূর্তি ভাঙা", "দান করা"], ["Believing Allah is the Sole Creator, Sovereign, Sustainer, and Controller of the Universe", "Praying only", "Breaking idols", "Charity"], 0,
         "মহাবিশ্বের সৃষ্টি, জীবন-মৃত্যু ও সকল ব্যবস্থাপনা একমাত্র আল্লাহর হাতে পরিচালিত।", "Rububiyyah affirms Allah alone creates, sustains, and controls the cosmos.", "সূরা আল-ফাতিহা: ২", "Surah Al-Fatihah: 2", "EASY"),

        ("তাওহীদুল উলূহিয়্যাহ বা ইবাদাত (Tawheed al-Uluhiyyah) বলতে কী বোঝায়?", "What does Tawheed al-Uluhiyyah (Oneness in Worship) mean in Islamic theology?",
         ["সকল প্রকার ইবাদত (সালাত, দোয়া, কোরবানি, মান্নত) একমাত্র আল্লাহর জন্যই নির্দিষ্ট করা", "আল্লাহকে স্রষ্টা মানা", "ফেরেশতাদের মানা", "আকাশ দেখা"], ["Directing all acts of worship (Salah, Dua, sacrifice, vows) exclusively to Allah alone", "Believing Creator", "Believing Angels", "Looking at sky"], 0,
         "মক্কার কাফেররা আল্লাহকে স্রষ্টা মানলেও মূর্তির মাধ্যমে মধ্যস্থতা করায় উলূহিয়্যাতে শিরক করেছিল।", "Uluhiyyah is dedicating every act of servitude and prayer to Allah with zero partners.", "সূরা আল-ইখলাস", "Surah Al-Ikhlas", "EASY"),

        ("তাওহীদুল আসমা ওয়াস সিফাত (Tawheed al-Asma wa's-Sifat) বলতে কী বোঝায়?", "What does Tawheed al-Asma wa's-Sifat (Oneness of Divine Names and Attributes) affirm?",
         ["কুরআন ও সহীহ হাদিসে বর্ণিত আল্লাহর সকল সুন্দর নাম ও গুণাবলীকে কোনো রূপান্তর, সাদৃশ্য বা অস্বীকার ছাড়াই হুবহু বিশ্বাস করা", "নাম পরিবর্তন করা", "সাদৃশ্য তৈরি করা", "গুণ অস্বীকার করা"], ["Affirming all divine Names and Attributes from Quran/Sunnah without distortion, denial, or resemblance to creation", "Changing names", "Resembling", "Denying"], 0,
         "কুরআনে বলা হয়েছে: 'লাইসা কামিসলিহী শাইউন ওয়া হুয়াস সামীউল বাসীর' (তাঁর সদৃশ কোনো কিছুই নেই এবং তিনি সর্বশ্রোতা ও সর্বদ্রষ্টা)।", "There is nothing like unto Him, and He is the Hearing, the Seeing (Surah Ash-Shura: 11).", "সূরা আশ-শূরা: ১১", "Surah Ash-Shura: 11", "EASY"),

        ("ঈমানের ছয়টি মৌলিক স্তম্ভের (আরকানুল ঈমান) সঠিক পূর্ণ তালিকা কোনটি যা হাদিসে জিবরাইলে বর্ণিত?", "What are the six essential pillars of Islamic faith (Arkan al-Iman) from Hadith Jibreel?",
         ["আল্লাহ, ফেরেশতাগণ, আসমানী কিতাবসমূহ, নবী-রাসূলগণ, পরকাল এবং তাকদীরের ভালো-মন্দের ওপর বিশ্বাস", "নামাজ, রোজা, হজ, যাকাত, কালেমা", "দান, সততা, দয়া", "ইতিহাস ও সমাজ"], ["Belief in Allah, His Angels, His Books, His Messengers, the Last Day, and Divine Destiny (Qadar)", "Salah, Fast, Hajj, Zakat", "Charity, honesty", "History"], 0,
         "হযরত জিবরাইল (আ.) মানুষের বেশে এসে নবীজি ﷺ-কে ঈমানের এই ছয়টি রুকন সম্পর্কে জিজ্ঞাসা করেছিলেন।", "Iman is to believe in Allah, His angels, His books, His messengers, the Last Day, and Qadar.", "সহীহ মুসলিম: ৮", "Sahih Muslim: 8", "EASY"),

        ("ইসলামের দৃষ্টিতে ক্ষমার অযোগ্য ও সবচেয়ে মারাত্মক ধ্বংসাত্মক মহাপাপ কোনটি?", "Which is the absolute greatest, most unforgivable sin in Islamic theology?",
         ["শিরক (Shirk - আল্লাহর সাথে কোনো কিছুকে অংশীদার সাব্যস্ত করা)", "চুরি করা", "মিথ্যা বলা", "রাগ করা"], ["Shirk (Associating partners with Allah Almighty)", "Stealing", "Lying", "Anger"], 0,
         "কুরআনে স্পষ্ট ঘোষণা: 'নিশ্চয়ই আল্লাহ তাঁর সাথে অংশীদার করার অপরাধ ক্ষমা করেন না; এ ছাড়া অন্য পাপ যাকে ইচ্ছা ক্ষমা করেন।' ", "Indeed, Allah does not forgive association with Him, but He forgives what is less than that (Surah An-Nisa: 48).", "সূরা আন-নিসা: ৪৮", "Surah An-Nisa: 48", "EASY"),

        ("রিয়া (Riya / লোক দেখানো ইবাদত বা মানুষের প্রশংসা পাওয়ার নিয়তে আমল করা)-কে হাদিসে কী বলা হয়েছে?", "What is Riya (showing off / performing worship for human praise) termed in Hadith?",
         ["শিরকে আসগর বা ছোট শিরক (Ash-Shirk al-Asghar)", "বড় শিরক", "মুবাহ", "মুস্তাহাব"], ["Ash-Shirk al-Asghar (Minor Shirk)", "Major Shirk", "Mubah", "Mustahab"], 0,
         "রাসূল ﷺ বলেন: আমি তোমাদের ব্যাপারে যে জিনিসটিকে সবচেয়ে বেশি ভয় করি তা হলো ছোট শিরক বা রিয়া।", "The thing I fear most for you is minor shirk: showing off in worship.", "মুসনাদে আহমাদ: ২৩৬৮০", "Musnad Ahmad: 23680", "EASY"),

        ("তাকদীর (Al-Qadar / ঐশ্বরিক ভাগ্যলিপি)-এর ওপর ঈমান আনার অর্থ কী?", "What does authentic belief in Al-Qadar (Divine Destiny) entail in Islamic creed?",
         ["মহাবিশ্বে যা কিছু ঘটে সবকিছুই আল্লাহর পূর্বজ্ঞান, লিপিবদ্ধ সিদ্ধান্ত, ইচ্ছা ও সৃষ্টির অধীনে সংঘটিত হয়", "মানুষের কোনো ইচ্ছা নেই", "সবকিছু অন্ধ ভাগ্য", "কর্মের প্রয়োজন নেই"], ["Believing all events occur by Allah's timeless Foreknowledge, Record, Divine Will, and Creation", "No human will", "Blind fate", "No effort"], 0,
         "বান্দাকে কাজের ইচ্ছা ও স্বাধীনতা দেওয়া হয়েছে এবং সেই চেষ্টার ভিত্তিতেই পরকালে পুরস্কৃত বা তিরস্কৃত করা হবে।", "Qadar encompasses Allah's Knowledge, the Preserved Tablet (Lawh Mahfuz), His Will, and Creation.", "সূরা আল-কামার: ৪৯", "Surah Al-Qamar: 49", "EASY"),

        ("আল্লাহ তাআলা সমস্ত সৃষ্টিজগতের ভাগ্য কিয়ামত পর্যন্ত যা ঘটবে তা আসমান-জমিন সৃষ্টির কত বছর পূর্বে লিখে রেখেছেন?", "How long before the creation of the heavens and earth were the destinies recorded?",
         ["৫০,০০০ বছর পূর্বে সংরক্ষিত ফলকে (লাওহে মাহফুজে)", "১,০০০ বছর পূর্বে", "১০,০০০ বছর পূর্বে", "আদম সৃষ্টির সময়"], ["50,000 years before in the Preserved Tablet (Lawh al-Mahfuz)", "1,000 years", "10,000 years", "At Adam's creation"], 0,
         "রাসূল ﷺ বলেন: আল্লাহ আসমান ও জমিন সৃষ্টির পঞ্চাশ হাজার বছর পূর্বে সকল সৃষ্টির ভাগ্য লিখে রেখেছেন।", "Allah ordained the measures of creation fifty thousand years before creating heavens and earth.", "সহীহ মুসলিম: ২৬৫৩", "Sahih Muslim: 2653", "EASY"),

        ("তাকদীরের ওপর বিশ্বাস রাখার ফলে মুমিনের মানসিক ও আত্মিক জীবনে কোন্ মহা উপকার অর্জিত হয়?", "What immense psychological and spiritual benefit does belief in Qadar grant a believer?",
         ["বিপদে ধৈর্য ও প্রশান্তি লাভ হয় এবং সাফল্যে অহংকারমুক্ত পরম কৃতজ্ঞতা সৃষ্টি হয়", "হতাশা আসে", "কর্মে অলসতা আসে", "ভয় তৈরি হয়"], ["Steadfast peace in trials and humble gratitude without arrogance in success", "Despair", "Laziness", "Fear"], 0,
         "কুরআনে বলা হয়েছে: যাতে তোমরা যা হারিয়েছ তার জন্য হতাশ না হও এবং যা পেয়েছ তাতে অহংকার না করো।", "So that you not despair over what has eluded you and not exult in pride over what He has given you.", "সূরা আল-হাদীদ: ২৩", "Surah Al-Hadid: 23", "EASY"),

        ("আল্লাহর সুন্দরতম নামসমূহের (আসমাউল হুসনা) মোট সংখ্যা হাদিসে কতটি বিশিষ্ট সুসংবাদের সাথে বর্ণিত হয়েছে?", "How many blessed Divine Names (Asma al-Husna) are highlighted with the promise of Jannah?",
         ["৯৯টি নাম (এক কম একশত)", "১০০টি নাম", "৭৭টি নাম", "১২০টি নাম"], ["99 Names (One hundred less one)", "100", "77", "120"], 0,
         "রাসূলুল্লাহ ﷺ বলেছেন: আল্লাহর নিরানব্বইটি নাম রয়েছে; যে ব্যক্তি এগুলো মুখস্থ, অনুধাবন ও আমল করবে সে জান্নাতে প্রবেশ করবে।", "Allah has ninety-nine names; whoever memorizes and lives by them will enter Paradise.", "সহীহ বুখারী: ২৭৩৬", "Sahih Bukhari: 2736", "EASY"),

        ("কুরআনে কারীমে আল্লাহ তাআলা আরশের ওপর অধিষ্ঠিত হওয়ার ব্যাপারে কী ঘোষণা এসেছে?", "What is the Quranic statement regarding Allah ascending above the Mighty Throne (Al-Arsh)?",
         ["'আর-রহমানু আলাল আরশিস তাওয়া' (পরম করুণাময় আল্লাহ আরশের ওপর সমুন্নত হয়েছেন)", "তিনি সর্বত্র বিলীন", "তিনি মাটিতে আছেন", "তিনি আরশের নিচে"], ["'Ar-Rahmanu 'alal-'Arshis-tawa' (The Most Merciful ascended over the Throne in a manner befitting His Majesty)", "Diffused everywhere", "In earth", "Below Throne"], 0,
         "আহলে সুন্নাত ওয়াল জামাআতের আকিদা হলো আল্লাহ স্বীয় সত্তায় সৃষ্টিজগতের ঊর্ধ্বে আরশের ওপর সমুন্নত এবং তাঁর ইলম সর্বত্র পরিবেষ্টিত।", "Allah ascended above His Throne befitting His Majesty without resembling creation.", "সূরা ত্বা-হা: ৫", "Surah Ta-Ha: 5", "EASY"),

        ("মৃত্যুর পর কবরে প্রতিটি মানুষকে যে ৩টি মৌলিক প্রশ্ন করা হবে সেগুলো কী কী?", "What are the three fundamental questions asked by angels in the grave?",
         ["১. তোমার রব কে? ২. তোমার দ্বীন কী? ৩. তোমার নবী কে?", "১. তোমার নাম কী? ২. তোমার দেশ কোথায়? ৩. তোমার টাকা কত?", "১. তোমার পিতা কে? ২. ভাষা কী? ৩. বর্ণ কী?", "১. কত পড়েছ? ২. পেশা কী? ৩. সন্তান কয়টি?"], ["1. Who is your Lord? 2. What is your Deen? 3. Who is your Prophet?", "Name, Country, Wealth", "Father, Language, Race", "Education, Job, Children"], 0,
         "মুমিন আত্মা দৃঢ়তার সাথে উত্তর দেবে: আমার রব আল্লাহ, আমার দ্বীন ইসলাম এবং আমার নবী মুহাম্মদ ﷺ।", "The believer answers: My Lord is Allah, my religion is Islam, and my Prophet is Muhammad ﷺ.", "সুনানে আবু দাউদ: ৪৭৫৩", "Sunan Abi Dawud: 4753", "EASY"),

        ("কিয়ামতের দিন বান্দার আমল ও কর্মফল ওজন করার জন্য স্থাপিত মহাসূক্ষ্ম দাঁড়িপাল্লাকে কী বলে?", "What is the divine balance erected on Judgment Day to weigh deeds called in the Quran?",
         ["মীযান (Al-Mizan)", "সিরাত", "কাউসার", "বারযাখ"], ["Al-Mizan (The Scale of Deeds)", "Sirat", "Kawthar", "Barzakh"], 0,
         "আল্লাহ বলেন: 'আমি কিয়ামতের দিন ন্যায়বিচারের তুলাদণ্ড স্থাপন করব, সুতরাং কারও ওপর বিন্দুমাত্র জুলুম হবে না।' ", "And We place the scales of justice for the Day of Resurrection, so no soul will be treated unjustly (Surah Al-Anbiya: 47).", "সূরা আল-আম্বিয়া: ৪৭", "Surah Al-Anbiya: 47", "EASY"),

        ("কিয়ামতের দিন সমগ্র মানবজাতির হিসাবের পূর্বে বিশ্বনবী ﷺ-এর মহান মধ্যস্থতাকে কী বলা হয়?", "What is the Supreme Intercession of Prophet Muhammad ﷺ to inaugurate the Judgment called?",
         ["আশ-শাফাআতুল কুবরা (The Supreme Intercession)", "শাফাআতে সুগরা", "ইসতিসকা", "ইসতিখারা"], ["Ash-Shafa'at al-Kubra (The Supreme Intercession)", "Shafa'at Sughra", "Istisqa", "Istikharah"], 0,
         "হাশরের তীব্র উত্তাপে মানুষ সকল নবীর কাছে গিয়ে ব্যর্থ হলে নবীজি ﷺ সিজদায় লুটিয়ে পড়ে এই শাফাআত করবেন।", "The Prophet ﷺ will fall in prostration beneath the Throne to intercede for all mankind.", "সহীহ বুখারী: ৪৭১৮", "Sahih Bukhari: 4718", "EASY"),

        ("আল্লাহ তাআলার ওপর পরিপূর্ণ তাওয়াক্কুল (Trust in Allah)-এর সঠিক ইসলামী রূপরেখা কোনটি?", "What is the authentic Islamic definition of Tawakkul (Reliance on Allah)?",
         ["উপযুক্ত বৈধ মাধ্যম ও সর্বোচ্চ প্রচেষ্টা গ্রহণ করে চূড়ান্ত ফলাফলের জন্য আল্লাহর ওপর নির্ভর করা", "চেষ্টা না করে বসে থাকা", "কেবল ভাগ্যের ওপর ছেড়ে দেওয়া", "উপায় বর্জন করা"], ["Tying the camel / exerting lawful effort while relying entirely on Allah for the outcome", "Sitting idle", "Blind fatalism", "Ignoring means"], 0,
         "এক ব্যক্তি জিজ্ঞেস করল: উট বেঁধে তাওয়াক্কুল করব নাকি ছেড়ে দিয়ে? নবীজি ﷺ বললেন: 'আগে উট বাঁধো, তারপর তাওয়াক্কুল করো।' ", "Tie your camel first, then put your trust in Allah.", "জামে আত-তিরমিযী: ২৫১৭", "Jami at-Tirmidhi: 2517", "EASY"),

        ("ইসলামী আকিদায় কালেমায়ে শাহাদাত 'লা ইলাহা ইল্লাল্লাহ'-এর সঠিক ও নিখুঁত অর্থ কী?", "What is the precise theological meaning of the testimony 'La ilaha illallah'?",
         ["'একমাত্র আল্লাহ ব্যতীত সত্য কোনো উপাস্য ও মাবুদ নেই (লা মা'বূদা বিহাক্কিন ইল্লাল্লাহ)'", "আল্লাহ ছাড়া কোনো স্রষ্টা নেই", "আল্লাহ ছাড়া কোনো রাজা নেই", "সবকিছুই আল্লাহ"], ["'There is no true deity worthy of worship in truth except Allah alone'", "No creator except Allah", "No king except Allah", "Everything is Allah"], 0,
         "কালেমাটি সকল মিথ্যা উপাস্যকে বাতিল (নাফি) করে একমাত্র সত্য আল্লাহর জন্য উলূহিয়াতকে সাব্যস্ত (ইসবাত) করে।", "La ilaha illallah negates all false gods and affirms worship solely for Allah.", "তাফসীরে ইবনে কাসীর", "Tafsir Ibn Kathir", "EASY"),

        ("ইসলামে গায়েব (অদৃশ্য জগত)-এর ওপর ঈমান আনার গুরুত্ব সম্পর্কে সূরা বাকারার শুরুতে কী বলা হয়েছে?", "How does Surah Al-Baqarah define the foremost trait of the righteous (Muttaqun)?",
         ["'আল্লাযীনা ইউ'মিনূনা বিল গাইব' (যারা অদৃশ্যের বিষয়ে দৃঢ় বিশ্বাস স্থাপন করে)", "যারা দেখে বিশ্বাস করে", "যারা ধনী", "যারা শক্তিশালী"], ["'Those who believe in the Unseen (Ghayb)' (Surah Al-Baqarah: 3)", "Believe what they see", "The wealthy", "The mighty"], 0,
         "আল্লাহ, ফেরেশতা, আখিরাত, জান্নাত-জাহান্নাম ইত্যাদি চাক্ষুষ না দেখেও ওহীর সত্যতায় বিশ্বাস করাই ঈমানের প্রাণ।", "Belief in the Unseen based on divine revelation is the cornerstone of Taqwa.", "সূরা আল-বাক্বারাহ: ৩", "Surah Al-Baqarah: 3", "EASY"),

        ("কুরআনে কারীমে কোন সুরত বা সৃষ্টিকে আল্লাহর সাথে তুলনা করার ব্যাপারে সুস্পষ্ট নিষেধাজ্ঞা কী?", "What is the categorical Quranic declaration regarding comparing anything to Allah?",
         ["'ওয়া লাম ইয়াকুল লাহু কুফুওয়ান আহাদ' (এবং তাঁর সমকক্ষ বা সমতুল্য কেউই নেই)", "তিনি মানুষের মতো", "তিনি প্রতিমার মতো", "তিনি প্রকৃতির মতো"], ["'And there is none comparable or equal unto Him' (Surah Al-Ikhlas: 4)", "Like humans", "Like idols", "Like nature"], 0,
         "আল্লাহ তাআলা স্থান, কাল, আকৃতি ও সৃষ্টির সকল অপূর্ণতা ও সাদৃশ্য থেকে চিরপবিত্র।", "Allah is uniquely One, Self-Sufficient, and absolutely incomparable to any creation.", "সূরা আল-ইখলাস: ৪", "Surah Al-Ikhlas: 4", "EASY"),

        ("ইসলামী আকিদায় কবর, মাজার বা কোনো মৃত ব্যক্তির কাছে সন্তান চাওয়া বা সাহায্য প্রার্থনার বিধান কী?", "What is the ruling on praying directly to graves, shrines, or deceased saints for children/help?",
         ["শিরকে আকবর (বড় শিরক) যা ঈমানকে সম্পূর্ণরূপে বাতিল করে দেয়", "জায়েজ উসিলা", "মুস্তাহাব", "ছোট গুনাহ"], ["Major Shirk (Shirk Akbar) which completely invalidates Islamic faith", "Permissible wasilah", "Mustahab", "Minor sin"], 0,
         "দোয়া হলো খাঁটি ইবাদত; আল্লাহ ছাড়া কোনো পীর, ওলী বা মৃত ব্যক্তির কাছে বিপদমুক্তি বা সন্তান প্রার্থনা করা প্রকাশ্য শিরক।", "Directing supplication or seeking supernatural aid from the dead is blatant Shirk.", "সূরা আল-ফাতিহা: ৫", "Surah Al-Fatihah: 5", "EASY"),

        ("ইসলামে তাবিজ, সুতা, কড়ি বা পাথরকে রোগমুক্তি বা সৌভাগ্যের স্বয়ংসক্রিয় মাধ্যম মনে করে ঝুলানোর বিধান কী?", "What did the Prophet ﷺ declare regarding wearing amulets (Tama'im) for protection/good luck?",
         ["'যে ব্যক্তি কোনো তাবিজ বা কবচ ঝুলাল সে মূলত শিরক করল (মান আল্লাকা তামিমাতান ফাকাদ আশরাক)'", "জায়েজ সাজসজ্জা", "মুস্তাহাব", "ওয়াজিব"], ["'Whoever hangs an amulet has committed Shirk' (Man 'allaqa tameematan faqad ashrak)", "Decoration", "Mustahab", "Wajib"], 0,
         "রোগমুক্তি ও কল্যাণ একমাত্র আল্লাহর হাতে; বস্তুর ওপর অলৌকিক ক্ষমতার বিশ্বাস শিরক।", "Amulets and charms worn for superstitious protection are condemned as Shirk.", "মুসনাদে আহমাদ: ১৬৯৬৯", "Musnad Ahmad: 16969", "EASY"),

        ("ইসলামী আকিদায় আল্লাহ তাআলার 'আল-হাইইউ আল-ক্বাইয়্যূম' নামের অর্থ কী?", "What is the profound meaning of the Divine Names 'Al-Hayy, Al-Qayyum' in Ayat al-Kursi?",
         ["চিরঞ্জীব ও সমগ্র মহাবিশ্বকে ধারণকারী ও পরিচালনাকারী চিরস্থায়ী সত্তা", "প্রথম সত্তা", "শেষ সত্তা", "ক্ষমতাশালী"], ["The Ever-Living, the Sustainer and Sovereign Maintainer of all existence", "First", "Last", "Mighty"], 0,
         "আল্লাহর কোনো মৃত্যু, তন্দ্রা বা ক্লান্তি নেই; তিনি চিরকাল জীবিত এবং সকল সৃষ্টি তাঁর ওপর নির্ভরশীল।", "Allah is the Ever-Living, the Self-Subsisting Sustainer of all creation.", "সূরা আল-বাক্বারাহ: ২৫৫", "Surah Al-Baqarah: 255", "EASY"),

        ("কুরআনে কারীমে আল্লাহ তাআলার প্রথম ও শেষ হওয়ার চিরন্তন রূপ কীভাবে ব্যক্ত হয়েছে?", "How does Surah Al-Hadid describe the Primordial and Eternal Transcendence of Allah?",
         ["'হুয়াল আওয়ালু ওয়াল আখিরু ওয়ায যাহিরু ওয়াল বাতিন' (তিনিই আদি, তিনিই অন্ত, তিনিই প্রকাশ্য, তিনিই অপ্রকাশ্য)", "তিনি সীমাবদ্ধ", "তিনি কেবল বর্তমান", "তিনি কেবল ভবিষ্যতে"], ["'He is the First and the Last, the Ascendant and the Intimate' (Surah Al-Hadid: 3)", "Limited", "Present only", "Future only"], 0,
         "সৃষ্টির পূর্বেও তিনি ছিলেন এবং সকল সৃষ্টির ধ্বংসের পরও তিনি চিরকাল বিদ্যমান থাকবেন।", "He is the First before Whom there is nothing, and the Last after Whom there is nothing.", "সূরা আল-হাদীদ: ৩", "Surah Al-Hadid: 3", "EASY"),

        ("কিয়ামতের দিন মুমিনগণের আমলনামা ডান হাতে দেওয়া হবে এবং অপরাধীদের আমলনামা কীভাবে দেওয়া হবে?", "How will the records of deeds be handed to the righteous versus the wicked on Judgment Day?",
         ["মুমিনদের ডান হাতে এবং অপরাধীদের বাম হাতে বা পিঠের পেছন দিক থেকে দেওয়া হবে", "উভয়ের ডান হাতে", "উভয়ের মাথায়", "মাটিতে রাখা হবে"], ["Righteous in their right hand; wicked in their left hand or from behind their backs", "Both right hand", "Both head", "On earth"], 0,
         "যার আমলনামা ডান হাতে দেওয়া হবে সে পরম আনন্দে চিৎকার করে বলবে: 'আমার আমলনামা পড়ে দেখো!' ", "He who is given his record in his right hand will have an easy accounting.", "সূরা আল-ইনশিক্বাক্ব: ৭-১০", "Surah Al-Inshiqaq: 7-10", "EASY"),

        ("কিয়ামতের দিন সমগ্র মানবজাতির চূড়ান্ত পরিণতি নির্ধারণের পর মৃত্যুকে কী করা হবে?", "What will happen to Death itself after all people enter Jannah and Jahannam?",
         ["মৃত্যুকে একটি সাদা-কালো মেষের আকৃতিতে এনে জান্নাত ও জাহান্নামের মাঝে জবাই করা হবে", "ছেড়ে দেওয়া হবে", "স্বর্গে যাবে", "পৃথিবীতে থাকবে"], ["Death will be brought in the form of a ram and slaughtered between Jannah and Hell", "Released", "Go to heaven", "Stay on earth"], 0,
         "অতঃপর ঘোষণা দেওয়া হবে: 'হে জান্নাতবাসী! অনন্ত জীবন, আর কোনো মৃত্যু নেই; হে জাহান্নামবাসী! অনন্ত আযাব, আর কোনো মৃত্যু নেই।' ", "Death will be slaughtered, inaugurating absolute immortality for both abodes.", "সহীহ বুখারী: ৪৭৩০", "Sahih Bukhari: 4730", "EASY"),

        ("ইসলামে মুনাফিকদের (Hypocrites) মৌলিক ৪টি চরিত্রগত লক্ষণ কী যা হাদিসে বর্ণিত হয়েছে?", "What are the four defining traits of a hypocrite (Munafiq) according to authentic Hadith?",
         ["১. কথা বললে মিথ্যা বলে, ২. ওয়াদা করলে ভঙ্গ করে, ৩. আমানত রাখলে খেয়ানত করে, ৪. বিবাদে জড়ালে অশ্লীল গালাগাল করে", "রোজা রাখে না", "হজ করে না", "মসজিদে যায় না"], ["1. Lies when speaking, 2. Breaks promises, 3. Betrays trusts, 4. Uses foul vulgarity in disputes", "Doesn't fast", "Doesn't do Hajj", "Doesn't go to mosque"], 0,
         "যার মধ্যে এই চারটি স্বভাব থাকে সে খাঁটি মুনাফিক যতক্ষণ না তা পরিহার করে।", "Four traits whoever has them is a pure hypocrite until he abandons them.", "সহীহ বুখারী: ৩৪", "Sahih Bukhari: 34", "EASY"),

        ("ইসলামী আকিদায় কিয়ামতের পূর্বে প্রকাশ্য বড় আলামত হিসেবে প্রতিশ্রুত মহামানব ইমাম মাহদীর আগমন সম্পর্কে বিশ্বাস কী?", "What is the authentic Ahlus Sunnah creed regarding the advent of Imam Al-Mahdi before Qiyamah?",
         ["তিনি রাসূল ﷺ-এর বংশে জন্ম নেবেন এবং সমগ্র পৃথিবীকে জুলুমের বদলে ন্যায়বিচারে পূর্ণ করবেন", "তিনি কল্পকাহিনী", "তিনি নতুন নবী হবেন", "তিনি মারা গেছেন"], ["He will descend from the Prophet's lineage and fill the earth with justice and equity", "Fable", "New Prophet", "Already died"], 0,
         "ইমাম মাহদী মুসলিম উম্মাহকে নেতৃত্ব দেবেন এবং তাঁর যুগেই হযরত ঈসা (আ.) আসমান থেকে অবতরণ করবেন।", "Al-Mahdi will arise from my family and fill the earth with justice as it was filled with oppression.", "সুনানে আবু দাউদ: ৪২৮২", "Sunan Abi Dawud: 4282", "EASY"),

        ("কিয়ামতের পূর্বে দাজ্জালের ভয়াবহ ফেতনা থেকে সুরক্ষা পেতে প্রতি জুমায় সূরা কাহফের কোন্ অংশ মুখস্থ করার নির্দেশ রয়েছে?", "Which verses of Surah Al-Kahf protect the believer from the severe trial of Dajjal?",
         ["সূরা আল-কাহফের প্রথম ১০টি আয়াত (অথবা শেষ ১০টি আয়াত)", "সূরা ফাতিহা", "সূরা ইয়াসীন", "সূরা বাকারা"], ["The first ten verses (or last ten verses) of Surah Al-Kahf", "Surah Fatihah", "Surah Yasin", "Surah Baqarah"], 0,
         "রাসূল ﷺ বলেন: যে ব্যক্তি সূরা কাহফের প্রথম দশটি আয়াত মুখস্থ করবে সে দাজ্জালের ফেতনা থেকে সুরক্ষিত থাকবে।", "Whoever memorizes ten verses from the beginning of Surah Al-Kahf will be protected from Dajjal.", "সহীহ মুসলিম: ৮০৯", "Sahih Muslim: 809", "EASY"),

        ("হযরত ঈসা (আ.) আসমান থেকে দামেস্কের সাদা মিনারে অবতরণ করে কোন্ স্থানে দাজ্জালকে হত্যা করবেন?", "At which location will Prophet Isa (AS) slay the False Messiah (Dajjal)?",
         ["ফিলিস্তিনের বাবে লুদ্দ (Gate of Ludd) নামক স্থানে", "জেরুজালেমে", "মক্কায়", "দামেস্কে"], ["At the Gate of Ludd (Bāb Ludd) in Palestine", "Jerusalem", "Makkah", "Damascus"], 0,
         "ঈসা (আ.) বাবে লুদ্দের নিকটে দাজ্জালকে খুঁজে পেয়ে নিজের বর্শা দিয়ে নিকেশ করবেন।", "Prophet Isa (AS) will pursue Dajjal and kill him at the Gate of Ludd.", "সহীহ মুসলিম: ২৯৩৭", "Sahih Muslim: 2937", "EASY"),

        ("কিয়ামতের পূর্বে ইয়াজুজ ও মাজুজ (Gog and Magog) জাতির বিশাল মানব স্রোত কোন্ স্থান ভেঙে বের হবে?", "From behind what barrier will the hordes of Gog and Magog (Yajuj wa Majuj) break out?",
         ["যুলকারনাইন কর্তৃক নির্মিত দুর্ভেদ্য লৌহ ও তামার প্রাচীর ভেঙে", "সমুদ্রের নিচ থেকে", "আকাশ থেকে", "মরুভূমি থেকে"], ["Breaking through the iron and copper barrier constructed by Dhul-Qarnayn", "From ocean", "From sky", "From desert"], 0,
         "তারা প্রতিটি উঁচু ভূমি থেকে দ্রুতগতিতে নেমে এসে সমস্ত পানি পান করে ফেলবে এবং উপদ্রব সৃষ্টি করবে।", "Until when Gog and Magog are opened and they descend from every mound (Surah Al-Anbiya: 96).", "সূরা আল-আম্বিয়া: ৯৬", "Surah Al-Anbiya: 96", "EASY"),

        ("কিয়ামতের অন্যতম প্রধান মহাজাগতিক আলামত হিসেবে সূর্য কোন দিক থেকে উদিত হবে যার পর তওবার দরজা চিরতরে বন্ধ হবে?", "From which direction will the sun rise as a major sign after which repentance is closed?",
         ["পশ্চিম দিক থেকে সূর্য উদয় হবে", "পূর্ব দিক থেকে", "উত্তর দিক থেকে", "দক্ষিণ দিক থেকে"], ["The sun will rise from the West (Maghrib)", "From East", "From North", "From South"], 0,
         "পশ্চিমাকাশে সূর্য উদিত হওয়ার পর আর কোনো অবিশ্বাসী ঈমান আনলে বা তওবা করলে তা কবুল হবে না।", "The Hour will not be established until the sun rises from the west; then repentance is sealed.", "সহীহ বুখারী: ৪৬৩৫", "Sahih Bukhari: 4635", "EASY"),

        ("আহলে সুন্নাত ওয়াল জামাআতের আকিদায় সালাফে সালেহীন ও চার ইমামগণের প্রতি দৃষ্টিভঙ্গি কেমন?", "What is the authentic Ahlus Sunnah creed regarding the Salaf as-Salih and the 4 Imams?",
         ["তাঁদের প্রতি গভীর শ্রদ্ধা, আনুগত্য ও কুরআন-সুন্নাহর সঠিক অনুসারী হিসেবে তাঁদের ফিকহী অবদানকে মূল্যায়ন করা", "তাঁদের ভুল ধরা", "তাঁদের অস্বীকার করা", "তাঁদের নবী মানা"], ["Deep veneration, respect, and utilizing their monumental scholarship rooted in Quran and Sunnah", "Condemning them", "Rejecting them", "Regarding them as Prophets"], 0,
         "সাহাবা, তাবিঈন এবং চার ইমাম হলেন উম্মতের পথপ্রদর্শক সোনালী আলোকবর্তিকা।", "The pious early generations (Salaf as-Salih) are the gold standard of Islamic understanding.", "আল-আকিদাহ আত-তাহাবিয়্যাহ", "Al-Aqeedah at-Tahawiyyah", "EASY"),

        ("ইসলামে বিদআত (Bid'ah / দ্বীনের মধ্যে নতুন মনগড়া ইবাদত আবিষ্কার) করার ব্যাপারে রাসূল ﷺ-এর হুঁশিয়ারি কী?", "What did Prophet Muhammad ﷺ strictly warn regarding religious innovations (Bid'ah)?",
         ["'দ্বীনের মধ্যে প্রতিটি নতুন উদ্ভাবিত ইবাদতই বিদআত এবং প্রতিটি বিদআতই পথভ্রষ্টতা ও জাহান্নামী'", "বিদআত ভালো", "সওয়াবের কাজ", "জায়েজ"], ["'Every innovation in religious worship is Bid'ah, every Bid'ah is misguidance, and every misguidance is in Hell'", "Bid'ah is good", "Rewarding", "Permissible"], 0,
         "যে ব্যক্তি আমাদের দ্বীনে এমন কোনো নতুন কাজ সৃষ্টি করে যা এতে নেই তা নিশ্চিত প্রত্যাখ্যাত ও বাতেল।", "He who innovates something in this matter of ours that is not of it will have it rejected.", "সহীহ বুখারী: ২৬৯৭", "Sahih Bukhari: 2697", "EASY"),

        ("মানুষের ঈমান কি নেক আমলের দ্বারা বৃদ্ধি পায় এবং পাপের দ্বারা হ্রাস পায়?", "Does faith (Iman) increase with righteous deeds and decrease with sins according to Ahlus Sunnah creed?",
         ["হ্যাঁ, খাঁটি আকিদা অনুযায়ী ঈমান আনুগত্য ও নেক আমলে বাড়ে এবং পাপাচারে কমে যায়", "ঈমান বাড়ে কমে না", "ঈমান স্থির থাকে", "আমলের সাথে সম্পর্ক নেই"], ["Yes, true faith increases with obedience/good deeds and decreases with sins", "Never changes", "Static", "No relation to deeds"], 0,
         "কুরআনে স্পষ্ট ঘোষণা: 'যাতে তাদের ঈমানের সাথে আরও ঈমান বৃদ্ধি পায়।' (সূরা ফাতহ: ৪)।", "That they may increase in faith along with their faith (Surah Al-Fath: 4).", "সূরা আল-ফাতহ: ৪", "Surah Al-Fath: 4", "EASY"),

        ("ইসলামী আকিদায় কিয়ামতের দিনে মুমিনদের জন্য মহান রবের সবচেয়ে বড় উপহার কোনটি হবে?", "What will be the ultimate crowning reward and joy for the believers in the Hereafter?",
         ["জান্নাতে কোনো আবরণ ছাড়া স্বচক্ষে আল্লাহ রব্বুল আলামীনের পবিত্র দীদার দর্শন লাভ", "সোনার মহল", "হূরদের সঙ্গ", "হাওযে কাউসার"], ["Beholding with their physical eyes the Radiant Countenance of Allah without barrier in Jannah", "Gold mansion", "Hoor", "Hawd Kawthar"], 0,
         "কুরআনে আল্লাহ বলেন: 'সেদিন কতক মুখমণ্ডল উজ্জ্বল প্রফুল্ল হবে, তারা তাদের রবের দিকে তাকিয়ে থাকবে।' ", "Some faces, that Day, will be radiant, looking at their Lord (Surah Al-Qiyamah: 22-23).", "সূরা আল-ক্বিয়ামাহ: ২২-২৩", "Surah Al-Qiyamah: 22-23", "EASY")
    ]
    return [('Q_AQD_' + str(i+1).zfill(3), 'aqeedah_tawheed', item[0], item[1], item[2][0], item[3][0], item[2][1], item[3][1], item[2][2], item[3][2], item[2][3], item[3][3], item[4], item[5], item[6], item[7], item[8], item[9]) for i, item in enumerate(items)]

def get_batch_13_to_18():
    q = []
    q.extend(get_cat_13_months())
    q.extend(get_cat_14_shariah())
    q.extend(get_cat_15_halal())
    q.extend(get_cat_16_akhlaq())
    q.extend(get_cat_17_dua())
    q.extend(get_cat_18_aqeedah())
    return q
