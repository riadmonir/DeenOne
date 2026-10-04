package com.devflux.deenone.features.ramadan.data;

import com.devflux.deenone.features.ramadan.model.RozaInfoItem;

import java.util.ArrayList;
import java.util.List;

public class RozaInfoRepository {

    public static List<RozaInfoItem> getRozaInfoList() {
        List<RozaInfoItem> list = new ArrayList<>();

        // 1. করণীয়-বর্জনীয়
        list.add(new RozaInfoItem(
                1,
                "করণীয়-বর্জনীয়",
                "Do's and Don'ts",
                "করণীয়: আল্লাহ তা'আলার ওপর আস্থা রাখুন। সালাত সুন্দর করে ধীরস্থিরভাবে আদায় করুন। কুরআন পড়ুন। যথাসম্ভব বুঝে পড়ার চেষ্টা করুন। যারা কুরআন পড়তে পারেন না, তারা সম্পূর্ণ মনোযোগের সাথে তিলাওয়াত শ্রবণ করুন...",
                "Do's: Have trust in Allah. Perform prayers calmly with serenity. Recite the Quran and try to understand its meaning. Those who cannot read Quran should listen attentively...",
                "করণীয়:\n\n" +
                        "আল্লাহ তা'আলার ওপর আস্থা রাখুন।\n" +
                        "সালাত সুন্দর করে ধীরস্থিরভাবে আদায় করুন।\n" +
                        "কুরআন পড়ুন। যথাসম্ভব বুঝে পড়ার চেষ্টা করুন।\n" +
                        "যারা কুরআন পড়তে পারেন না, তারা সম্পূর্ণ মনোযোগের সাথে তিলাওয়াত শ্রবণ করুন।\n" +
                        "প্রত্যেক ভালো কাজ আল্লাহর নামে আরম্ভ করুন।\n" +
                        "আপনার সম্পদের হিসাব করে যাকাত প্রদান করুন।\n" +
                        "গোপনে দান করুন।\n" +
                        "নিকটাত্মীয়দের জন্য ব্যয় করুন।\n" +
                        "অভাবীদের জন্য ব্যয় করুন।\n" +
                        "আপনার সিয়ামকে অর্থবহ করার চেষ্টা করুন।\n" +
                        "চোখের পর্দা করুন।\n" +
                        "অন্যকে সৎকর্মের দিকে উৎসাহিত এবং পরিচালিত করুন।\n" +
                        "দিনমজুর ও সাধারণ শ্রেণীর মানুষদের সালাম দিন।\n" +
                        "অধীনদের প্রতি সদয় থাকুন। তাদের কাজের বোঝা হালকা করে দিন।\n" +
                        "স্বাস্থ্যসম্মত ইফতার করুন এবং অতিভোজন পরিহার করুন।\n" +
                        "সাহরীতে একটি হলেও খেজুর খান।\n" +
                        "ভোর রাতে আল্লাহর নিকট দু'আ করুন।\n" +
                        "পথে কষ্টদায়ক কিছু দেখলে তা পরিষ্কার করুন।\n" +
                        "রমাদানে তাহজ্জুদ আদায়ের অভ্যাস করুন।\n\n\n" +
                        "বর্জনীয়:\n\n" +
                        "অর্থহীন ও অহেতুক কাজ পরিহার করুন\n" +
                        "টিভি সিরিয়াল, মুভি, নাটক ও সিনেমা দেখা পরিহার করুন।\n" +
                        "মিউজিক শুনবেন না।\n" +
                        "গেমস খেলে সময় নষ্ট করবেন না।\n" +
                        "আড্ডা ও দলবেঁধে গল্পগুজব পরিহার করুন।\n" +
                        "ইউটিউব ও সামাজিক যোগাযোগ-মাধ্যমে সময় কম দিন।\n" +
                        "হারাম উপার্জন বন্ধ করুন।\n" +
                        "আপনি সঠিক হওয়া সত্ত্বেও পরিবার কিংবা পরিবারের বাইরে কারো সাথে তর্ক পরিহার করুন।\n" +
                        "ঝগড়া পরিহার করুন।\n" +
                        "মিথ্যা কথা পরিহার করুন।\n" +
                        "যে কোনো পাপকাজ পরিহার করুন।\n" +
                        "কারো ব্যাপারে সন্দেহ বা কুধারণা পোষণ করবেন না।\n" +
                        "কারো পেছনে সমালোচনা করবেন না।\n" +
                        "কারো প্রতি রূঢ় আচরণ করবেন না।\n" +
                        "কারো প্রতি হিংসা বা শত্রুতা লালন করবেন না।\n" +
                        "কারো কাছ থেকে অবৈধ কোনো সুবিধা নেবেন না বা কাউকে অবৈধ সুবিধা দেবেন না।\n" +
                        "কারো মনে কষ্ট হয়, এমন কোনো আচরণ করবেন না।\n" +
                        "সাহরী, ইফতার ও ঈদের কেনাকাটায় অপচয় করবেন না",
                "Do's:\n\n" +
                        "Have firm trust in Allah.\n" +
                        "Perform prayers with serenity and focus.\n" +
                        "Recite the Quran and try to understand its meaning.\n" +
                        "Those who cannot recite Quran should listen attentively to recitation.\n" +
                        "Begin every good deed with the name of Allah.\n" +
                        "Calculate your wealth and pay Zakat.\n" +
                        "Give charity in secret.\n" +
                        "Spend on close relatives.\n" +
                        "Spend on the needy and underprivileged.\n" +
                        "Strive to make your fasting meaningful.\n" +
                        "Lower your gaze and guard your eyes.\n" +
                        "Encourage and guide others towards good deeds.\n" +
                        "Greet laborers and common people with Salam.\n" +
                        "Be kind to subordinates and lighten their burdens.\n" +
                        "Have healthy Iftar and avoid overeating.\n" +
                        "Eat at least a date during Suhur.\n" +
                        "Supplicate to Allah during the last part of the night.\n" +
                        "Remove harmful objects from roads and paths.\n" +
                        "Cultivate the habit of praying Tahajjud in Ramadan.\n\n\n" +
                        "Don'ts:\n\n" +
                        "Avoid useless and idle activities.\n" +
                        "Refrain from watching TV serials, movies, dramas, and films.\n" +
                        "Do not listen to music.\n" +
                        "Do not waste time playing games.\n" +
                        "Avoid unproductive gossiping and gatherings.\n" +
                        "Reduce time spent on YouTube and social media.\n" +
                        "Stop any unlawful (haram) earnings.\n" +
                        "Avoid arguing even if you are right.\n" +
                        "Refrain from fighting and quarelling.\n" +
                        "Avoid lying and dishonesty.\n" +
                        "Refrain from all forms of sins.\n" +
                        "Do not harbor suspicious or negative thoughts about others.\n" +
                        "Do not backbite or criticize people behind their backs.\n" +
                        "Do not be harsh or rude to anyone.\n" +
                        "Do not harbor malice, jealousy, or enmity towards anyone.\n" +
                        "Do not take or give any illegal favors.\n" +
                        "Do not hurt anyone's feelings or cause distress.\n" +
                        "Avoid extravagance and waste in Suhur, Iftar, and Eid shopping.",
                "কুরআন ও সুন্নাহর নির্দেশনা",
                "Guidance of Quran and Sunnah"
        ));

        // 2. গুরুত্বপূর্ণ আমল
        list.add(new RozaInfoItem(
                2,
                "গুরুত্বপূর্ণ আমল",
                "Important Deeds",
                "ইসলামের পাঁচটি রুকনের একটি রুকন হল সিয়াম। আর রমাদান মাসে সিয়াম পালন করা ফরজ। সেজন্য রমাদান মাসের প্রধান আমল হলো সুন্নাহ মোতাবেক সিয়াম পালন করনিম্নে কিছু গুরুত্বপূর্ণ আমল তুলে ধরা হলোা: সিয়াম পালন করা, সময় মত সালাত আদায় করা, সহীহভাবে কুরআন শেখা...",
                "Fasting is one of the five pillars of Islam, and fasting during the month of Ramadan is obligatory. Therefore, the primary deed of Ramadan is to observe fasting according to the Sunnah...",
                "ইসলামের পাঁচটি রুকনের একটি রুকন হল সিয়াম। আর রমাদান মাসে সিয়াম পালন করা ফরজ। সেজন্য রমাদান মাসের প্রধান আমল হলো সুন্নাহ মোতাবেক সিয়াম পালন করনিম্নে কিছু গুরুত্বপূর্ণ আমল তুলে ধরা হলোা:\n\n" +
                        "সিয়াম পালন করা\n" +
                        "সময় মত সালাত আদায় করা\n" +
                        "সহীহভাবে কুরআন শেখা\n" +
                        "সাহরী খাওয়া\n" +
                        "সালাতুত তারাবীহ পড়া\n" +
                        "বেশি বেশি কুরআন তিলাওয়াত করা\n" +
                        "শুকরিয়া আদায় করা\n" +
                        "কল্যাণকর কাজ বেশি বেশি করা\n" +
                        "সালাতুত তাহাজ্জুদ পড়া\n" +
                        "বেশি বেশি দান-সদাকাহ করা\n" +
                        "উত্তম চরিত্র গঠনের অনুশীলন করা\n" +
                        "ই‘তিকাফ করা\n" +
                        "দাওয়াতে দ্বীনের কাজ করা\n" +
                        "সামর্থ্য থাকলে উমরা পালন করা\n" +
                        "লাইলাতুল কদর তালাশ করা\n" +
                        "লাইলাতুল কদরের দো‘আ\n" +
                        "বেশি বেশি দো‘আ ও কান্নাকাটি করা\n" +
                        "ইফতার করা\n" +
                        "ইফতার করানো\n" +
                        "তাওবাহ ও ইস্তেগফার করা\n" +
                        "তাকওয়া অর্জন করা\n" +
                        "ফজরের পর সূর্যোদয় পর্যন্ত মাসজিদে অবস্থান করা\n" +
                        "ফিতরাহ দেয়া\n" +
                        "অপরকে খাদ্য খাওয়ানো\n" +
                        "আত্মীয়তার সম্পর্ক উন্নীত করা\n" +
                        "কুরআন মুখস্থ বা হিফয করা\n" +
                        "আল্লাহর যিকর করা\n" +
                        "মিসওয়াক করা\n" +
                        "একজন অপরজনকে কুরআন শুনানো\n" +
                        "কুরআন বুঝা ও আমল করা",
                "Fasting is one of the five pillars of Islam, and fasting during the month of Ramadan is obligatory. Therefore, the primary deed of Ramadan is to observe fasting according to the Sunnah. Below are some important deeds highlighted:\n\n" +
                        "Observing the fast\n" +
                        "Performing prayers on time\n" +
                        "Learning the Quran correctly\n" +
                        "Eating Suhur\n" +
                        "Praying Salat al-Taraweeh\n" +
                        "Abundant recitation of the Holy Quran\n" +
                        "Expressing gratitude to Allah\n" +
                        "Engaging in good and beneficial deeds\n" +
                        "Praying Salat al-Tahajjud\n" +
                        "Giving abundant charity and Sadaqah\n" +
                        "Practicing and refining noble character\n" +
                        "Observing Itikaf\n" +
                        "Engaging in Dawah activities\n" +
                        "Performing Umrah if able\n" +
                        "Seeking Laylatul Qadr (Night of Decree)\n" +
                        "Supplicating with the Dua of Laylatul Qadr\n" +
                        "Abundant dua and supplication with tears\n" +
                        "Breaking fast (Iftar) on time\n" +
                        "Providing Iftar to other fasting people\n" +
                        "Repentance and seeking forgiveness (Istighfar)\n" +
                        "Attaining Taqwa (God-consciousness)\n" +
                        "Staying in the mosque from Fajr until sunrise\n" +
                        "Paying Sadaqat al-Fitr\n" +
                        "Feeding the hungry and needy\n" +
                        "Enhancing ties of kinship\n" +
                        "Memorizing or revising the Quran\n" +
                        "Engaging in the remembrance of Allah (Dhikr)\n" +
                        "Using the Miswak\n" +
                        "Reciting and listening to Quran with one another\n" +
                        "Understanding and acting upon the Quran",
                "কুরআন ও সুন্নাহর আলোকে",
                "In Light of Quran and Sunnah"
        ));

        // 3. রমজানের প্রস্তুতি
        list.add(new RozaInfoItem(
                3,
                "রমজানের প্রস্তুতি",
                "Preparation for Ramadan",
                "রমজান হলো আত্মশুদ্ধি, ইবাদত ও তাকওয়া অর্জনের মাস। এই পবিত্র মাসকে সঠিকভাবে কাটানোর জন্য আমাদের কিছু প্রস্তুতি নেওয়া উচিত। নিচে কয়েকটি গুরুত্বপূর্ণ প্রস্তুতির দিক আলোচনা করা হলো...",
                "Ramadan is the month of self-purification, worship, and gaining Taqwa. To spend this holy month properly, we should take preparatory steps in advance...",
                "রমজান হলো আত্মশুদ্ধি, ইবাদত ও তাকওয়া অর্জনের মাস। এই পবিত্র মাসকে সঠিকভাবে কাটানোর জন্য আমাদের কিছু প্রস্তুতি নেওয়া উচিত। নিচে কয়েকটি গুরুত্বপূর্ণ প্রস্তুতির দিক আলোচনা করা হলো,\n\n\n" +
                        "আত্মশুদ্ধির নিয়ত ও পরিকল্পনা করা:\n\n" +
                        "রমজানের মূল উদ্দেশ্য আল্লাহর নৈকট্য লাভ করা এবং তাকওয়া অর্জন করা। আল্লাহ তাআলা বলেন,\n\n" +
                        "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِنْ قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ\n\n" +
                        "অর্থ:\n" +
                        "হে ঈমানদারগণ! তোমাদের ওপর রোজা ফরজ করা হয়েছে, যেমন ফরজ করা হয়েছিল তোমাদের পূর্ববর্তীদের ওপর, যেন তোমরা তাকওয়া অর্জন করতে পারো।\n" +
                        "[সূরা আল-বাকারা: ১৮৩]\n\n" +
                        "তাই রমজানের জন্য মানসিকভাবে প্রস্তুতি নেওয়া জরুরি, যেন আমরা এটিকে একটি আত্মগঠনের সুযোগ হিসেবে গ্রহণ করতে পারি।\n\n\n" +
                        "রোযার মাসায়েল জানা ও আমল ঠিক করা:\n\n" +
                        "রমজানের রোযা শুধু না খাওয়া বা পান না করার নাম নয়; বরং এটি সংযম ও আত্মনিয়ন্ত্রণের প্রশিক্ষণ। তাই রমজানের রোযার ফরজ, ওয়াজিব, সুন্নাহ ও মাকরূহ বিষয়গুলো জানা জরুরি।\n\n" +
                        "কিছু গুরুত্বপূর্ণ মাসায়েল:\n" +
                        "সাহরী ও ইফতারের সময় ও দোয়া মুখস্থ করা\n" +
                        "কি কি কারণে রোযা ভঙ্গ হয়, তা জানা\n" +
                        "রমজানের শেষ দশকে ইতিকাফের নিয়ম জানা\n" +
                        "জাকাত দেওয়ার পরিমাণ ও হিসাব ঠিক করা\n\n\n" +
                        "নামাজ ও কুরআনের সাথে সম্পর্ক বাড়ানো:\n\n" +
                        "রমজান হলো কুরআনের মাস। আল্লাহ বলেন,\n\n" +
                        "شَهْرُ رَمَضَانَ ٱلَّذِىٓ أُنزِلَ فِيهِ ٱلْقُرْءَانُ\n\n" +
                        "অর্থ:\n" +
                        "রমজান মাস, যাতে কুরআন অবতীর্ণ করা হয়েছে।\n" +
                        "[সূরা আল-বাকারা: ১৮৫]\n\n" +
                        "তাই রমজানের আগে থেকেই কুরআন তিলাওয়াতের অভ্যাস তৈরি করা উচিত, যেন এ মাসে বেশি বেশি পড়া ও বুঝার সুযোগ হয়। এছাড়াও পাঁচ ওয়াক্ত নামাজ ও তাহাজ্জুদ নামাজের অভ্যাস গড়ে তোলা দরকার।\n\n\n" +
                        "দোয়া ও ইস্তেগফার বাড়ানো:\n\n" +
                        "রমজান হলো ক্ষমা লাভের মাস। রাসূল ﷺ বলেছেন,\n\n" +
                        "مَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n" +
                        "অর্থ:\n" +
                        "যে ব্যক্তি ঈমান ও সওয়াবের নিয়তে রমজানের রোযা রাখবে, তার পূর্ববর্তী গুনাহসমূহ মাফ করে দেওয়া হবে।\n" +
                        "[বুখারি: ২০১৪, মুসলিম: ৭৬০]\n\n" +
                        "তাই আমাদের উচিত বেশি বেশি ইস্তেগফার করা এবং আল্লাহর কাছে হেদায়াত ও গুনাহ মাফ চাওয়া।\n\n\n" +
                        "স্বাস্থ্যগত প্রস্তুতি নেওয়া:\n\n" +
                        "রমজানে অনেকেই প্রথম দিকে শারীরিক দুর্বলতা অনুভব করেন। তাই আগে থেকেই,\n" +
                        "খাদ্যাভ্যাস নিয়ন্ত্রণ করা\n" +
                        "অতিরিক্ত খাওয়া ও পান করা কমানো\n" +
                        "রাত জাগার বদভ্যাস দূর করা\n" +
                        "শরীরের জন্য উপকারী খাবার গ্রহণ করা\n\n\n" +
                        "জাকাত ও সদকা দেওয়ার পরিকল্পনা করা:\n\n" +
                        "যাদের ওপর জাকাত ফরজ, তারা যেন রমজানের মধ্যে তা পরিশোধ করতে পারেন, সে জন্য হিসাব করে প্রস্তুতি নেওয়া জরুরি। এছাড়াও, রাসূল ﷺ রমজানে অনেক বেশি দান করতেন,\n\n" +
                        "كَانَ رَسُولُ اللَّهِ ﷺ أَجْوَدَ النَّاسِ، وَكَانَ أَجْوَدَ مَا يَكُونُ فِي رَمَضَانَ\n\n" +
                        "অর্থ:\n" +
                        "রাসূলুল্লাহ ﷺ ছিলেন সবচেয়ে দানশীল, আর তিনি রমজানে সবচেয়ে বেশি দান করতেন।\n" +
                        "[বুখারি: ৬]\n\n" +
                        "তাই আমাদের উচিত গরীব-দুঃখীদের সহায়তা করা ও দান-সদকা করা।\n\n\n" +
                        "খারাপ অভ্যাস ত্যাগ করা:\n\n" +
                        "রমজান হলো আত্মশুদ্ধির মাস। তাই আমাদের উচিত,\n" +
                        "গীবত (পরনিন্দা) ও মিথ্যা কথা বলা বন্ধ করা\n" +
                        "অপ্রয়োজনীয় সময় নষ্ট করা পরিহার করা\n" +
                        "গান, সিনেমা ও অশ্লীল কনটেন্ট দেখা থেকে বিরত থাকা",
                "Ramadan is the month of self-purification, worship, and gaining Taqwa. To spend this sacred month properly, we should take some preparatory steps. Below are some important aspects of preparation:\n\n" +
                        "1. Intention and Planning for Self-Purification:\n" +
                        "The core purpose of Ramadan is drawing close to Allah and attaining Taqwa. Allah the Almighty says:\n" +
                        "\"O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous.\" [Surah Al-Baqarah: 183]\n" +
                        "Therefore, mental preparation is essential to embrace this month as a personal reformation opportunity.\n\n" +
                        "2. Learning Rulings of Fasting:\n" +
                        "Fasting is not merely refraining from food and drink, but a training of restraint and discipline. It is essential to learn the obligatory (Fard), essential (Wajib), Sunnah, and disliked (Makruh) matters.\n" +
                        "Key rulings include: memorizing Suhur and Iftar times and prayers, understanding causes of breaking fast, rules of Itikaf in the last ten days, and calculating Zakat.\n\n" +
                        "3. Strengthening Relationship with Salah and Quran:\n" +
                        "Ramadan is the month of the Quran: \"The month of Ramadan in which was revealed the Quran...\" [Surah Al-Baqarah: 185]. Cultivating Quran recitation habit and establishing five daily prayers along with Tahajjud.\n\n" +
                        "4. Increasing Dua and Istighfar:\n" +
                        "Ramadan is the month of forgiveness. The Prophet ﷺ said: \"Whoever fasts Ramadan out of faith and hope of reward, his previous sins will be forgiven.\" [Bukhari: 2014, Muslim: 760].\n\n" +
                        "5. Health and Physical Preparation:\n" +
                        "Regulating diet, moderating food and water intake, eliminating late-night sleep habits, and adopting wholesome nutrition.\n\n" +
                        "6. Planning Zakat and Charity:\n" +
                        "Calculating and preparing Zakat if obligatory, following the Sunnah of the Prophet ﷺ who was the most generous of all, especially during Ramadan [Bukhari: 6].\n\n" +
                        "7. Renouncing Bad Habits:\n" +
                        "Refraining from backbiting and falsehood, avoiding time-wasting, and staying away from inappropriate media and contents.",
                "সূরা আল-বাকারা: ১৮৩, ১৮৫ | সহীহ বুখারী ও মুসলিম",
                "Surah Al-Baqarah: 183, 185 | Sahih Bukhari & Muslim"
        ));

        // 4. রোজার নিয়ত
        list.add(new RozaInfoItem(
                4,
                "রোজার নিয়ত",
                "Intention for Fasting",
                "نَوَيْتُ أَنْ أَصُومَ غَدًا مِنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللَّهُ فَتَقَبَّلْ مِنِّي إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ উচ্চারণ: নাওয়াইতু আন আসূমা গাদান মিন শাহরি রামাদানাল মুবারাক ফারদাল্লাকা ইয়া আল্লাহু ফাতাকাব্বাল মিন্নি ইন্নিকা আনতাস সামীউল আলীম...",
                "Nawaytu an asooma ghadan min shahri Ramadan al-mubarak fardan laka ya Allahu fataqabbal minnee innaka antas-Samee'ul-'Aleem...",
                "نَوَيْتُ أَنْ أَصُومَ غَدًا مِنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللَّهُ فَتَقَبَّلْ مِنِّي إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ\n\n\n" +
                        "উচ্চারণ:\n\n" +
                        "নাওয়াইতু আন আসূমা গাদান মিন শাহরি রামাদানাল মুবারাক ফারদাল্লাকা ইয়া আল্লাহু ফাতাকাব্বাল মিন্নি ইন্নিকা আনতাস সামীউল আলীম।\n\n\n" +
                        "অর্থ:\n\n" +
                        "আমি নিয়ত করলাম আগামীকাল রমজান মাসের ফরজ রোজা আপনার জন্য রাখবো, হে আল্লাহ। আপনি তা কবুল করুন। নিশ্চয়ই আপনি সর্বশ্রোতা, সর্বজ্ঞ।",
                "نَوَيْتُ أَنْ أَصُومَ غَدًا مِنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللَّهُ فَتَقَبَّلْ مِنِّي إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ\n\n\n" +
                        "Transliteration:\n\n" +
                        "Nawaytu an asooma ghadan min shahri Ramadanal mubaraki fardan laka ya Allahu fataqabbal minnee innaka antas-Samee'ul-'Aleem.\n\n\n" +
                        "Translation:\n\n" +
                        "I intend to observe tomorrow's obligatory fast of the blessed month of Ramadan for You, O Allah. Please accept it from me. Indeed, You are the All-Hearing, the All-Knowing.",
                "ফিকহ শাস্ত্রের বিশুদ্ধ সংকলন",
                "Principles of Islamic Jurisprudence"
        ));

        // 5. ইফতারের দোয়া
        list.add(new RozaInfoItem(
                5,
                "ইফতারের দোয়া",
                "Dua for Iftar",
                "اللَّهُمَّ لَكَ صُمْتُ وَعَلَىٰ رِزْقِكَ أَفْطَرْتُ وَبِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ উচ্চারণ:আল্লাহুম্মা লাকা ছুমতু ওয়া আলা রিযক্বিকা আফতারতু ওয়া বিরাহমাতিকা ইয়া আরহামার রাহিমীন অর্থ:হে আল্লাহ! আমি আপনার জন্য রোজা রেখেছি এবং আপনারই দেওয়া রিজিক দ্বারা ইফতার করেছি...",
                "Allahumma laka sumtu wa 'ala rizqika aftartu wa bi-rahmatika ya Arhamar-Rahimeen. Transliteration and English translation...",
                "اللَّهُمَّ لَكَ صُمْتُ وَعَلَىٰ رِزْقِكَ أَفْطَرْتُ وَبِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ\n\n\n" +
                        "উচ্চারণ:\n\n" +
                        "আল্লাহুম্মা লাকা ছুমতু ওয়া আলা রিযক্বিকা আফতারতু ওয়া বিরাহমাতিকা ইয়া আরহামার রাহিমীন।\n\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! আমি আপনার জন্য রোজা রেখেছি এবং আপনারই দেওয়া রিজিক দ্বারা ইফতার করেছি। হে পরম দয়ালু, আপনার রহমতের আশায়।\n\n" +
                        "[আবু দাউদ - ২৩৫৮]",
                "اللَّهُمَّ لَكَ صُمْتُ وَعَلَىٰ رِزْقِكَ أَفْطَرْتُ وَبِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ\n\n\n" +
                        "Transliteration:\n\n" +
                        "Allahumma laka sumtu wa 'ala rizqika aftartu wa bi-rahmatika ya Arhamar-Rahimeen.\n\n\n" +
                        "Translation:\n\n" +
                        "O Allah! I fasted for You and I break my fast with Your provision, and by Your mercy, O Most Merciful of the merciful.\n\n" +
                        "[Abu Dawud - 2358]",
                "আবু দাউদ - ২৩৫৮",
                "Abu Dawud - 2358"
        ));

        // 6. সেহরির দোয়া
        list.add(new RozaInfoItem(
                6,
                "সেহরির দোয়া",
                "Dua for Suhur",
                "نَوَيْتُ اَنْ اُصُوْمَ غَدًا مِّنْ شَهْرِ رَمْضَانَ الْمُبَارَكِ فَرْضَا لَكَ يَا اللهُ فَتَقَبَّل مِنِّى اِنَّكَ اَنْتَ السَّمِيْعُ الْعَلِيْم উচ্চারণ: নাওয়াইতু আন আছুম্মা গাদাম মিন্ শাহরি রমাজানাল মুবারাকি ফারদাল্লাকা, ইয়া আল্লাহু ফাতাকাব্বাল মিন্নি ইন্নিকা আনতাস্ সামিউল আলিম। অর্থ: হে আল্লাহ! আমি আগামীকাল পবিত্র মাহে রমজানের নির্ধারিত ফরজ রোজা রাখার নিয়ত করলাম। অতএব তুমি আমার ...",
                "Nawaytu an asooma ghadan min shahri Ramadan al-mubarak fardan laka ya Allahu fataqabbal minnee innaka antas-Samee'ul-'Aleem. Transliteration and English translation...",
                "نَوَيْتُ اَنْ اُصُوْمَ غَدًا مِّنْ شَهْرِ رَمْضَانَ الْمُبَارَكِ فَرْضَا لَكَ يَا اللهُ فَتَقَبَّل مِنِّى اِنَّكَ اَنْتَ السَّمِيْعُ الْعَلِيْم\n\n\n" +
                        "উচ্চারণ:\n\n" +
                        "নাওয়াইতু আন আছুম্মা গাদাম মিন্ শাহরি রমাজানাল মুবারাকি ফারদাল্লাকা, ইয়া আল্লাহু ফাতাকাব্বাল মিন্নি ইন্নিকা আনতাস্ সামিউল আলিম।\n\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! আমি আগামীকাল পবিত্র মাহে রমজানের নির্ধারিত ফরজ রোজা রাখার নিয়ত করলাম। অতএব তুমি আমার রোযা তথা পানাহার থেকে বিরত থাকাকে কবুল কর, নিশ্চয়ই তুমি সর্বশ্রোতা ও সর্বজ্ঞানী।",
                "نَوَيْتُ اَنْ اُصُوْمَ غَدًا مِّنْ شَهْرِ رَمْضَانَ الْمُبَارَكِ فَرْضَا لَكَ يَا اللهُ فَتَقَبَّل مِنِّى اِنَّكَ اَنْتَ السَّمِيْعُ الْعَلِيْم\n\n\n" +
                        "Transliteration:\n\n" +
                        "Nawaytu an asooma ghadan min shahri Ramadan al-mubarak fardan laka ya Allahu fataqabbal minnee innaka antas-Samee'ul-'Aleem.\n\n\n" +
                        "Translation:\n\n" +
                        "O Allah! I intend to observe tomorrow's obligatory fast of the blessed month of Ramadan for You. Therefore, accept from me my fast (abstaining from food and drink); indeed, You are the All-Hearing, the All-Knowing.",
                "ফিকহ শাস্ত্রের বিশুদ্ধ সংকলন",
                "Principles of Islamic Jurisprudence"
        ));

        // 7. ইস্তেগফার
        list.add(new RozaInfoItem(
                7,
                "ইস্তেগফার",
                "Istighfar",
                "ক্ষমা প্রার্থনায় তাওবাহ বা ইসতেগফারের বিকল্প নেই। আল্লাহর কাছে ক্ষমা প্রার্থনায় কোরআন-সুন্নাহতে অনেক দোয়া ও ইসতেগফার রয়েছে। তবে পড়তে সহজ ও ব্যাপক প্রচলিত দোয়ার মাধ্যমেও ক্ষমা প্রার্থনা করা যায়। এমন ই ৫টি দোয়া তুলে ধরা হলো যার মাধ্যমে মুমিন মুসলমান আল্লাহর কাছে ক্ষমা প্রার্থনা করবেন। আর তা হলো-أَسْتَ...",
                "There is no substitute for repentance and seeking forgiveness (Istighfar). While the Quran and Sunnah contain numerous supplications, five easily practiced and widely authentic prayers for forgiveness are presented here...",
                "ক্ষমা প্রার্থনায় তাওবাহ বা ইসতেগফারের বিকল্প নেই। আল্লাহর কাছে ক্ষমা প্রার্থনায় কোরআন-সুন্নাহতে অনেক দোয়া ও ইসতেগফার রয়েছে। তবে পড়তে সহজ ও ব্যাপক প্রচলিত দোয়ার মাধ্যমেও ক্ষমা প্রার্থনা করা যায়। এমন ই ৫টি দোয়া তুলে ধরা হলো যার মাধ্যমে মুমিন মুসলমান আল্লাহর কাছে ক্ষমা প্রার্থনা করবেন। আর তা হলো-\n\n\n" +
                        "أَستَغْفِرُ اللهَ\n\n\n" +
                        "উচ্চারণ: \n\n" +
                        "আস্তাগফিরুল্লাহ।\n\n" +
                        "অর্থ: \n\n" +
                        "আমি আল্লাহর ক্ষমা প্রার্থনা করছি।\n\n" +
                        "নিয়ম: \n\n" +
                        "প্রতি ওয়াক্ত ফরজ নামাজের সালাম ফেরানোর পর রাসুলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম এ ইসতেগফারটি ৩ বার পড়তেন।\n\n" +
                        "[মিশকাত]\n\n\n" +
                        "أَسْتَغْفِرُ اللهَ وَأَتُوْبُ إِلَيْهِ\n\n\n" +
                        "উচ্চারণ: \n\n" +
                        "আস্তাগফিরুল্লাহা ওয়া আতুবু ইলাইহি।\n\n" +
                        "অর্থ:\n\n" +
                        "আমি আল্লাহর ক্ষমা প্রার্থনা করছি এবং তাঁর দিকেই ফিরে আসছি।\n\n" +
                        "নিয়ম: \n\n" +
                        "এ ইসতেগফারটি প্রতিদিন ৭০/১০০ বার পড়া। রাসুলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম প্রতিদিন ৭০ বারের অধিক তাওবাহ ও ইসতেগফার করতেন। \n\n" +
                        "[বুখারি]\n\n\n" +
                        "رَبِّ اغْفِرْ لِيْ وَتُبْ عَلَيَّ إِنَّكَ (أنْتَ) التَّوَّابُ الرَّحِيْمُ\n\n\n" +
                        "উচ্চারণ: \n\n" +
                        "রাব্বিগ্ ফিরলি ওয়া তুব আলাইয়্যা ইন্নাকা (আংতাত) তাওয়্যাবুর রাহিম।\n\n" +
                        "অর্থ: \n\n" +
                        "হে আমার প্রভু! আপনি আমাকে ক্ষমা করুন এবং আমার তাওবাহ কবুল করুন। নিশ্চয়ই আপনি মহান তাওবা কবুলকারী করুণাময়।\n\n" +
                        "নিয়ম: \n\n" +
                        "রাসুলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম মসজিদে বসে এক বৈঠকেই এই দোয়া ১০০ বার পড়েছেন। (আবু দাউদ, ইবনে মাজাহ, তিরমিজি, মিশকাত)\n\n\n" +
                        "أَسْتَغْفِرُ اللَّهَ الَّذِي لاَ إِلَهَ إِلاَّ هُوَ الْحَىُّ الْقَيُّومُ وَأَتُوبُ إِلَيْهِ\n\n\n" +
                        "উচ্চারণ: \n\n" +
                        "আস্তাগফিরুল্লা হাল্লাজি লা ইলাহা ইল্লা হুওয়াল হাইয়্যুল কইয়্যুমু ওয়া আতুবু ইলায়হি।\n\n" +
                        "অর্থ: \n\n" +
                        "আমি ওই আল্লাহর কাছে ক্ষমা চাই, যিনি ছাড়া প্রকৃতপক্ষে কোনো মাবুদ নেই, তিনি চিরঞ্জীব, চিরস্থায়ী এবং তাঁর কাছেই (তাওবাহ করে) ফিরে আসি।\n\n" +
                        "নিয়ম:\n\n" +
                        "দিনের যে কোনো ইবাদত-বন্দেগি তথা ক্ষমা প্রার্থনার সময় এভাবে তাওবাহ-ইসতেগফার করা। হাদিসে এসেছে- এভাবে তাওবাহ-ইসতেগফার করলে আল্লাহতাআলা তাকে ক্ষমা করে দেবেন, যদিও সে যুদ্ধক্ষেত্র থেকে পলায়নকারী হয়।\n\n" +
                        "[আবু দাউদ, তিরমিজি, মিশকাত]\n\n\n" +
                        "সাইয়েদুল ইসতেগফার পড়া:\n\n\n" +
                        "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ خَلَقْتَنِي وَأَنَا عَبْدُكَ وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ\n\n\n" +
                        "উচ্চারণ: \n\n" +
                        "আল্লাহুম্মা আংতা রাব্বি লা ইলাহা ইল্লা আংতা খালাক্কতানি ওয়া আনা আবদুকা ওয়া আনা আলা আহ্দিকা ওয়া ওয়াদিকা মাসতাতাতু আউজুবিকা মিন শাররি মা সানাতু আবুউলাকা বিনিমাতিকা আলাইয়্যা ওয়া আবুউলাকা বিজাম্বি ফাগ্ফিরলি ফা-ইন্নাহু লা ইয়াগফিরুজ জুনুবা ইল্লা আংতা।\n\n" +
                        "অর্থ: \n\n" +
                        "হে আল্লাহ! তুমিই আমার প্রতিপালক। তুমি ছাড়া কোনো ইলাহ নেই। তুমিই আমাকে সৃষ্টি করেছ। আমি তোমারই বান্দা আমি যথাসাধ্য তোমার সঙ্গে প্রতিজ্ঞা ও অঙ্গীকারের ওপর আছি।\n\n" +
                        "আমি আমার সব কৃতকর্মের কুফল থেকে তোমার কাছে আশ্রয় চাই। তুমি আমার প্রতি তোমার যে নেয়ামত দিয়েছ তা স্বীকার করছি। আর আমার কৃত গোনাহের কথাও স্বীকার করছি। তুমি আমাকে ক্ষমা করে দাও। কারণ তুমি ছাড়া কেউ গোনাহ ক্ষমা করতে পারবে না।\n\n" +
                        "নিয়ম:\n\n" +
                        "সকালে ও সন্ধ্যায় এ ইসতেগফার করা। ফজর ও মাগরিবের নামাজের পর এ ইসতেগফার পড়তে ভুল না করা।",
                "There is no substitute for repentance and seeking forgiveness (Istighfar). Five widely authentic supplications for seeking forgiveness:\n\n" +
                        "1. Astaghfirullah (I seek forgiveness from Allah).\n" +
                        "Recited 3 times after each obligatory prayer [Mishkat].\n\n" +
                        "2. Astaghfirullaha wa atoobu ilayh (I seek forgiveness from Allah and repent to Him).\n" +
                        "Recited 70 to 100 times daily [Bukhari].\n\n" +
                        "3. Rabbighfir lee wa tub 'alayya innaka Antat-Tawwabur-Raheem (My Lord, forgive me and accept my repentance, indeed You are the Accepter of repentance, the Merciful) [Abu Dawud, Tirmidhi].\n\n" +
                        "4. Astaghfirullahal-ladhee la ilaha illa Huwal-Hayyul-Qayyoomu wa atoobu ilayh (I seek forgiveness from Allah, besides Whom there is no deity, the Ever-Living, the Sustainer of all, and I repent to Him) [Abu Dawud, Tirmidhi].\n\n" +
                        "5. Sayyidul Istighfar (The Master Supplication for Forgiveness):\n" +
                        "Allahumma Anta Rabbee la ilaha illa Anta, khalaqtanee wa ana 'abduka wa ana 'ala 'ahdika wa wa'dika mastata'tu, a'oodhu bika min sharri ma sana'tu, aboo'u laka bi-ni'matika 'alayya wa aboo'u laka bi-dhanbee faghfir lee, fa-innahu la yaghfirudh-dhunooba illa Ant.",
                "বুখারি, তিরমিজি, মিশকাত, আবু দাউদ",
                "Bukhari, Tirmidhi, Mishkat, Abu Dawud"
        ));

        // 8. তারাবির নিয়ত
        list.add(new RozaInfoItem(
                8,
                "তারাবির নিয়ত",
                "Intention for Taraweeh",
                "نَوَيْتُ أَنْ أُصَلِّيَ لِلَّهِ تَعَالَى رَكْعَتَيْ صَلَاةِ التَّرَاوِيحِ سُنَّةَ رَسُولِ اللَّهِ تَعَالَى مُتَوَجِّهًا إِلَى جِهَةِ الْكَعْبَةِ الشَّرِيفَةِ، اللَّهُ أَكْبَرُ উচ্চারণ: নাওয়াইতু আন উসাল্লিয়া লিল্লাহি তা‘আলা রাকআতাই সালাতিত তারাবীহি সুন্নাতা রাসূলিল্লাহি তা‘আলা মুতাওয়াজ্জিহান ইলা জিহাতিল কা‘বাতিশ শরীফা, আল্লাহু আকবার। অর্থ: আমি আল্লাহ তা‘আলার জন্য দুই রাকাত তারাবির নামাজ (রাসূল ﷺ-এর সুন্নাহ) ...",
                "Nawaytu an usalliya lillahi ta'ala rak'atay salatit-taraweehi sunnata Rasoolillahi ta'ala mutawajjihan ila jihatil-Ka'batish-shareefati, Allahu Akbar. Transliteration and English translation...",
                "نَوَيْتُ أَنْ أُصَلِّيَ لِلَّهِ تَعَالَى رَكْعَتَيْ صَلَاةِ التَّرَاوِيحِ سُنَّةَ رَسُولِ اللَّهِ تَعَالَى مُتَوَجِّهًا إِلَى جِهَةِ الْكَعْبَةِ الشَّرِيفَةِ، اللَّهُ أَكْبَرُ\n\n\n" +
                        "উচ্চারণ:\n\n" +
                        "নাওয়াইতু আন উসাল্লিয়া লিল্লাহি তা‘আলা রাকআতাই সালাতিত তারাবীহি সুন্নাতা রাসূলিল্লাহি তা‘আলা মুতাওয়াজ্জিহান ইলা জিহাতিল কা‘বাতিশ শরীফা, আল্লাহু আকবার।\n\n\n" +
                        "অর্থ:\n\n" +
                        "আমি আল্লাহ তা‘আলার জন্য দুই রাকাত তারাবির নামাজ (রাসূল ﷺ-এর সুন্নাহ) কাবা শরীফের দিকে মুখ করে আদায় করার নিয়ত করলাম। আল্লাহ মহার।\n\n \n" +
                        "তারাবির বাংলা নিয়ত:\n\n" +
                        "নিয়ত আরবিতেই করতে হবে এমন কোনো বাধ্যবাধকতা নেই। বাংলাতেও করা যাবে। তাহলো- তারাবিহর দুই রাকাত নামাজ কেবলামুখী হয়ে আল্লাহর জন্য (জামাত হলে- এ ইমামের পেছনে) পড়ছি (اَللهُ اَكْبَر) আল্লাহু আকবার।",
                "نَوَيْتُ أَنْ أُصَلِّيَ لِلَّهِ تَعَالَى رَكْعَتَيْ صَلَاةِ التَّرَاوِيحِ سُنَّةَ رَسُولِ اللَّهِ تَعَالَى مُتَوَجِّهًا إِلَى جِهَةِ الْكَعْبَةِ الشَّرِيفَةِ، اللَّهُ أَكْبَرُ\n\n\n" +
                        "Transliteration:\n\n" +
                        "Nawaytu an usalliya lillahi ta'ala rak'atay salatit-taraweehi sunnata Rasoolillahi ta'ala mutawajjihan ila jihatil-Ka'batish-shareefati, Allahu Akbar.\n\n\n" +
                        "Translation:\n\n" +
                        "I intend to pray two Rak'ahs of the Taraweeh prayer (Sunnah of the Messenger of Allah ﷺ) for the sake of Allah the Almighty, facing towards the Holy Ka'bah; Allah is the Greatest.\n\n\n" +
                        "Intention in Any Language:\n\n" +
                        "There is no strict requirement to make the intention in Arabic. It can be made in any language: I intend to pray two Rak'ahs of Taraweeh prayer facing the Qiblah for Allah (behind this Imam in congregation); Allahu Akbar.",
                "ফিকহ শাস্ত্রের বিশুদ্ধ সংকলন",
                "Principles of Islamic Jurisprudence"
        ));

        // 9. যে ৫ দিন রোযা রাখা হারাম
        list.add(new RozaInfoItem(
                9,
                "যে ৫ দিন রোযা রাখা হারাম",
                "5 Days When Fasting is Prohibited",
                "ইসলামে পাঁচটি দিন রোযা রাখা হারাম হিসেবে নির্ধারিত হয়েছে। এই দিনগুলোতে রোযা রাখা নিষিদ্ধ, কারণ রাসূলুল্লাহ ﷺ সরাসরি তা নিষেধ করেছেন। নিচে সেই পাঁচটি দিন উল্লেখ করা হলো,ঈদুল ফিতরের দিন:রমজানের পরে ১ শাওয়াল ঈদুল ফিতরের দিন। এই দিনে রোযা রাখা সম্পূর্ণ হারাম। রাসূল ﷺ বলেন,ঈদের দিনে রোযা রাখার অনুমতি নেই, ঈদুল ফি...",
                "Five days are designated in Islam on which fasting is strictly prohibited (haram) by the Prophet Muhammad ﷺ. Day of Eid-ul-Fitr, Day of Eid-ul-Adha, and the 3 days of Tashreeq...",
                "ইসলামে পাঁচটি দিন রোযা রাখা হারাম হিসেবে নির্ধারিত হয়েছে। এই দিনগুলোতে রোযা রাখা নিষিদ্ধ, কারণ রাসূলুল্লাহ ﷺ সরাসরি তা নিষেধ করেছেন। নিচে সেই পাঁচটি দিন উল্লেখ করা হলো,\n\n\n" +
                        "ঈদুল ফিতরের দিন:\n\n" +
                        "রমজানের পরে ১ শাওয়াল ঈদুল ফিতরের দিন। এই দিনে রোযা রাখা সম্পূর্ণ হারাম। রাসূল ﷺ বলেন,\n\n" +
                        "ঈদের দিনে রোযা রাখার অনুমতি নেই, ঈদুল ফিতর এবং ঈদুল আজহা।\n\n" +
                        "[সহিহ মুসলিম: ১১৩৮, বুখারি: ১৯৯০]\n\n\n" +
                        "ঈদুল আজহার দিন:\n\n" +
                        "কুরবানির ঈদের দিন মুসলমানদের জন্য আনন্দ ও খাওয়া-দাওয়ার দিন। তাই এদিন রোযা রাখা হারাম। ৩, ৪ ও ৫. তাশরিকের তিন দিন (১১, ১২ ও ১৩ জিলহজ) কুরবানির ঈদের পরের তিন দিনকে \"আইয়্যামুত তাশরিক\" (أيام التشريق) বলা হয়। এই দিনগুলোতে রোযা রাখা নিষিদ্ধ, কারণ এগুলো খাবার ও আল্লাহর জিকির করার দিন। রাসূল ﷺ বলেছেন, তাশরিকের দিনগুলো হলো খাবার, পানীয় ও আল্লাহকে স্মরণ করার দিন।\n\n" +
                        "[সহিহ মুসলিম: ১১৪১]",
                "In Islam, five days are designated where fasting is strictly prohibited (haram). Fasting on these days is forbidden because the Messenger of Allah ﷺ directly prohibited it. The five days are mentioned below:\n\n\n" +
                        "1. Day of Eid-ul-Fitr:\n\n" +
                        "1st of Shawwal immediately following Ramadan. Fasting on this day is completely forbidden. The Prophet ﷺ said:\n\n" +
                        "\"Fasting is not permitted on the two days of Eid: Eid-ul-Fitr and Eid-ul-Adha.\"\n\n" +
                        "[Sahih Muslim: 1138, Bukhari: 1990]\n\n\n" +
                        "2. Day of Eid-ul-Adha:\n\n" +
                        "The day of sacrifice is a day of joy and eating for Muslims. Therefore, fasting on this day is prohibited.\n\n\n" +
                        "3, 4 & 5. The Three Days of Tashreeq (11th, 12th & 13th Dhul Hijjah):\n\n" +
                        "The three days after Eid-ul-Adha are known as \"Ayyam al-Tashreeq\" (أيام التشريق). Fasting during these days is prohibited because they are days of eating, drinking, and remembering Allah. The Prophet ﷺ said: \"The days of Tashreeq are days of eating, drinking, and remembering Allah.\"\n\n" +
                        "[Sahih Muslim: 1141]",
                "সহিহ মুসলিম: ১১৩৮, ১১৪১, বুখারি: ১৯৯০",
                "Sahih Muslim: 1138, 1141, Bukhari: 1990"
        ));

        // 10. রোযা ভঙ্গের কারণ
        list.add(new RozaInfoItem(
                10,
                "রোযা ভঙ্গের কারণ",
                "Causes of Breaking Fast",
                "রমজানের রোযা ইসলামের অন্যতম স্তম্ভ। এটি একটি গুরুত্বপূর্ণ ইবাদত, তাই আমাদের জানা দরকার কোন কোন কারণে রোযা ভেঙে যায়। নিচে রোযা ভঙ্গের কারণসমূহ দেওয়া হলো।ইচ্ছা করে বমি করাবমির বেশির ভাগ মুখে আসার পর তা গিলে ফেলামেয়েদের মাসিক ও সন্তান প্রসবের পর ঋতুস্রাবইসলাম ত্যাগ করলেগ্লুকোজ বা শক্তিবর্ধক ইনজেকশন...",
                "Fasting during Ramadan is an essential pillar of Islam. Below are the verified causes that invalidate the fast according to Islamic jurisprudence...",
                "রমজানের রোযা ইসলামের অন্যতম স্তম্ভ। এটি একটি গুরুত্বপূর্ণ ইবাদত, তাই আমাদের জানা দরকার কোন কোন কারণে রোযা ভেঙে যায়। নিচে রোযা ভঙ্গের কারণসমূহ দেওয়া হলো।\n\n" +
                        "ইচ্ছা করে বমি করা\n" +
                        "বমির বেশির ভাগ মুখে আসার পর তা গিলে ফেলা\n" +
                        "মেয়েদের মাসিক ও সন্তান প্রসবের পর ঋতুস্রাব\n" +
                        "ইসলাম ত্যাগ করলে\n" +
                        "গ্লুকোজ বা শক্তিবর্ধক ইনজেকশন বা সেলাইন দিলে\n" +
                        "প্রস্রাব-পায়খানার রাস্তা দিয়ে ওষুধ বা অন্য কিছু শরীরে প্রবেশ করালে\n" +
                        "রোজাদারকে জোর করে কেউ কিছু খাওয়ালে\n" +
                        "ইফতারের সময় হয়েছে ভেবে সূর্যাস্তের আগে ইফতার করলে\n" +
                        "মুখ ভরে বমি করলে\n" +
                        "ভুলবশত কোনো কিছু খেয়ে, রোজা ভেঙে গেছে ভেবে ইচ্ছা করে আরও কিছু খেলে\n" +
                        "বৃষ্টির পানি মুখে পড়ার পর তা খেয়ে ফেললে\n" +
                        "কান বা নাক দিয়ে ওষুধ প্রবেশ করালে \n" +
                        "জিহ্বা দিয়ে দাঁতের ফাঁক থেকে ছোলা পরিমাণ কোনো কিছু বের করে খেয়ে ফেললে\n" +
                        "অল্প বমি মুখে আসার পর ইচ্ছাকৃতভাবে তা গিলে ফেললে\n" +
                        "রোজা স্মরণ থাকা অবস্থায় অজুতে কুলি বা নাকে পানি দেয়ার সময় ভেতরে পানি চলে গেলে\n\n" +
                        "[ফাতাওয়ায়ে শামি ও ফাতাওয়ায়ে আলমগিরি]",
                "Fasting during Ramadan is one of the pillars of Islam. It is a vital act of worship; hence, it is essential to know what causes invalidate the fast. Below are the causes:\n\n" +
                        "1. Vomiting intentionally.\n" +
                        "2. Swallowing vomit back after most of it filled the mouth.\n" +
                        "3. Menstruation (Haiz) and post-natal bleeding (Nifas).\n" +
                        "4. Apostasy (leaving Islam).\n" +
                        "5. Receiving glucose, energizing injections, or IV saline.\n" +
                        "6. Introducing medicine or substances through urinary or rectal passages.\n" +
                        "7. Being forcibly fed by someone while fasting.\n" +
                        "8. Breaking the fast before sunset under the false impression of sunset.\n" +
                        "9. Vomiting a mouthful.\n" +
                        "10. Ingesting food mistakenly, then intentionally eating more assuming the fast broke.\n" +
                        "11. Swallowing rainwater after it enters the mouth.\n" +
                        "12. Administering medicine through the ear or nose.\n" +
                        "13. Extracting food debris chickpea-sized from teeth and swallowing it.\n" +
                        "14. Deliberately swallowing even a little vomit after it reaches the mouth.\n" +
                        "15. Water entering internally during wudu mouth-rinsing while conscious of fasting.\n\n" +
                        "[Fatawa Shami & Fatawa Alamgiri]",
                "ফাতাওয়ায়ে শামি ও ফাতাওয়ায়ে আলমগিরি",
                "Fatawa Shami & Fatawa Alamgiri"
        ));

        // 11. ইফতারের পর আল্লাহর শুকরিয়া আদায় করে দোয়া
        list.add(new RozaInfoItem(
                11,
                "ইফতারের পর আল্লাহর শুকরিয়া আদায় করে দোয়া",
                "Dua of Gratitude to Allah After Iftar",
                "ইফতারের পর আল্লাহর শুকরিয়া আদায় করে পড়ার দোয়া রাসূল ﷺ এর ইফতারের দোয়া,ذَهَبَ الظَّمَأُ وَابْتَلَّتِ العُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ উচ্চারণ:যাহাবাজ্ জমা'উ, ওয়াবতাল্লাতিল উরূকু, ওয়াসাবাতাল অজরু ইন্ শা'আল্লাহ অর্থ:তৃষ্ণা দূর হলো, শিরাগুলো সিক্ত হলো এবং ইনশাআল্লাহ প্রতিদান প্রতিষ্ঠিত হলো [আবু দাউদ: ২৩৫৭, ইবনে মাজাহ: ১৭৫৩]আল্লা...",
                "Supplications of gratitude to Allah after Iftar: The Prophet's ﷺ dua 'Dhahabadh-dhama'u wabtallatil-'urooqu wa thabatal-ajru in sha Allah' and the supplication of thankfulness...",
                "ইফতারের পর আল্লাহর শুকরিয়া আদায় করে পড়ার দোয়া রাসূল ﷺ এর ইফতারের দোয়া,\n\n\n" +
                        "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ العُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ\n\n\n" +
                        "উচ্চারণ:\n\n" +
                        "যাহাবাজ্ জমা'উ, ওয়াবতাল্লাতিল উরূকু, ওয়াসাবাতাল অজরু ইন্ শা'আল্লাহ।\n\n\n" +
                        "অর্থ:\n\n" +
                        "তৃষ্ণা দূর হলো, শিরাগুলো সিক্ত হলো এবং ইনশাআল্লাহ প্রতিদান প্রতিষ্ঠিত হলো।\n\n" +
                        "[আবু দাউদ: ২৩৫৭, ইবনে মাজাহ: ১৭৫৩]\n\n\n" +
                        "আল্লাহর শুকরিয়া আদায়ের দোয়া:\n\n\n" +
                        "اللَّهُمَّ لَكَ الحَمْدُ، أَنْتَ أَطْعَمْتَ وَسَقَيْتَ وَأَغْنَيْتَ وَأَقْنَيْتَ وَهَدَيْتَ وَأَحْيَيْتَ، فَلَكَ الحَمْدُ عَلَى مَا أَعْطَيْتَ\n\n\n" +
                        "উচ্চারণ:\n\n" +
                        "আল্লাহুম্মা লাকা আল-হামদু, আনতা আত'আমতা ওয়া সাকাইতা, ওয়া আগনাইতা ওয়া আকনাইতা, ওয়া হাদাইতা ওয়া আহ্যাইতা, ফালাকা আল-হামদু 'আলা মা আ'তাইতা।\n\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! সমস্ত প্রশংসা তোমার জন্য, তুমি খাওয়ালে, পানি পান করালে, অভাবমুক্ত করালে, সন্তুষ্ট করালে, হেদায়াত দিলে এবং জীবন দান করালে। তুমি যা দান করেছ তার জন্য সমস্ত প্রশংসা তোমারই।\n\n" +
                        "[মুসনাদ আহমাদ: ১৮৮৯৬]",
                "Dua of the Messenger of Allah ﷺ when breaking fast and supplication expressing gratitude to Allah after Iftar:\n\n\n" +
                        "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ العُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ\n\n\n" +
                        "Transliteration:\n\n" +
                        "Dhahabadh-dhama'u, wabtallatil-'urooqu, wa thabatal-ajru in sha Allah.\n\n\n" +
                        "Translation:\n\n" +
                        "The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.\n\n" +
                        "[Abu Dawud: 2357, Ibn Majah: 1753]\n\n\n" +
                        "Dua of Gratitude to Allah:\n\n\n" +
                        "اللَّهُمَّ لَكَ الحَمْدُ، أَنْتَ أَطْعَمْتَ وَسَقَيْتَ وَأَغْنَيْتَ وَأَقْنَيْتَ وَهَدَيْتَ وَأَحْيَيْتَ، فَلَكَ الحَمْدُ عَلَى مَا أَعْطَيْتَ\n\n\n" +
                        "Transliteration:\n\n" +
                        "Allahumma laka al-hamdu, Anta at'amta wa saqayta, wa aghnayta wa aqnayta, wa hadayta wa ahyayta, falakal-hamdu 'ala ma a'tayta.\n\n\n" +
                        "Translation:\n\n" +
                        "O Allah! All praise is due to You. You provided food and drink, You enriched and satisfied, You guided and gave life; so all praise is for You for what You have bestowed.\n\n" +
                        "[Musnad Ahmad: 18896]",
                "আবু দাউদ: ২৩৫৭, ইবনে মাজাহ: ১৭৫৩, মুসনাদ আহমাদ: ১৮৮৯৬",
                "Abu Dawud: 2357, Ibn Majah: 1753, Musnad Ahmad: 18896"
        ));

        // 12. যে সমস্ত কারণে রোযা শুরু করার পর ভেঙ্গে ফেলার অনুমতি রয়েছে
        list.add(new RozaInfoItem(
                12,
                "যে সমস্ত কারণে রোযা শুরু করার পর ভেঙ্গে ফেলার অনুমতি রয়েছে",
                "Permissible Reasons to Break Fast After Starting",
                "অসুস্থতা (মারাত্মক অসুস্থ হলে বা অসুস্থতা বেড়ে গেলে):যদি কেউ রোযা রাখার পর মারাত্মক অসুস্থ হয়ে পড়ে, অথবা রোযার কারণে তার অসুস্থতা বেড়ে যাওয়ার আশঙ্কা থাকে, তাহলে সে রোযা ভেঙে ফেলতে পারে। আল্লাহ তাআলা বলেন,وَمَن كَانَ مَرِيضًا أَوْ عَلَىٰ سَفَرٍ فَعِدَّةٌ مِّنْ أَيَّامٍ أُخَرَঅর্থ:আর তোমাদের কেউ যদি অসুস্থ হয় অথবা সফরে থাকে, তাহলে সে ...",
                "Legitimate Islamic exemptions permitting one to break an ongoing fast: severe illness, pregnancy and nursing, traveling, extreme weakness, and life-saving necessity...",
                "অসুস্থতা (মারাত্মক অসুস্থ হলে বা অসুস্থতা বেড়ে গেলে):\n\n" +
                        "যদি কেউ রোযা রাখার পর মারাত্মক অসুস্থ হয়ে পড়ে, অথবা রোযার কারণে তার অসুস্থতা বেড়ে যাওয়ার আশঙ্কা থাকে, তাহলে সে রোযা ভেঙে ফেলতে পারে। আল্লাহ তাআলা বলেন,\n\n\n" +
                        "وَمَن كَانَ مَرِيضًا أَوْ عَلَىٰ سَفَرٍ فَعِدَّةٌ مِّنْ أَيَّامٍ أُخَرَ\n\n\n" +
                        "অর্থ:\n\n" +
                        "আর তোমাদের কেউ যদি অসুস্থ হয় অথবা সফরে থাকে, তাহলে সে অন্য দিনে সে সংখ্যা পূরণ করবে।\n\n" +
                        "[সূরা আল-বাকারা: ১৮৫]\n\n\n" +
                        "গর্ভবতী ও দুগ্ধদানকারী মায়ের জন্য:\n\n" +
                        "গর্ভবতী বা দুগ্ধদানকারী মা যদি মনে করেন যে রোযা রাখলে তার নিজের বা শিশুর ক্ষতি হতে পারে, তবে তিনি রোযা ভেঙে দিতে পারেন। তবে পরে কাজা করতে হবে।\n\n" +
                        "[আবু দাউদ: ২৪০৮, নাসাঈ: ২২৭৪]\n\n\n" +
                        "ভ্রমণ (সফর করা হলে):\n\n" +
                        "যদি কেউ সফর অবস্থায় (কমপক্ষে ৭৮ কিমি দূরের) থাকে এবং রোযা রাখার পর কষ্ট অনুভব করে, তবে সে রোযা ভেঙে দিতে পারে। রাসূল ﷺ বলেছেন,\n\n\n" +
                        "إِنَّ اللَّهَ عَزَّ وَجَلَّ وَضَعَ عَنِ الْمُسَافِرِ الصَّوْمَ وَشَطْرَ الصَّلَاةِ\n\n\n" +
                        "অর্থ:\n\n" +
                        "নিশ্চয়ই আল্লাহ সফরকারীর জন্য রোযা এবং নামাজের কিছু অংশ হালকা করেছেন।\n\n" +
                        "[নাসাঈ: ২২৭৫, ইবনে মাজাহ: ১৬৬৭]\n\n\n" +
                        "চরম দুর্বলতা ও জীবন রক্ষার প্রয়োজন:\n\n" +
                        "যদি কেউ চরম দুর্বল হয়ে পড়ে এবং রোযার কারণে মৃত্যুর আশঙ্কা থাকে, তবে সে রোযা ভেঙে দিতে পারে। ইসলাম জীবনকে প্রাধান্য দেয়, তাই জীবন বাঁচানো জরুরি।\n\n" +
                        "[সূরা আল-বাকারা: ১৯৫]\n\n\n" +
                        "অনিচ্ছাকৃতভাবে কিছু প্রবেশ করা:\n\n" +
                        "যদি ভুলবশত ধোঁয়া, পানি বা খাবার গিলে ফেলে এবং এতে দমবন্ধ বা অসুবিধা হয়, তবে সে রোযা ভেঙে দিতে পারে এবং পরে কাজা করবে।\n\n\n" +
                        "মাসিক (হায়েজ) বা প্রসব শুরু হলে:\n\n" +
                        "কোনো মহিলা যদি দিনের বেলায় হায়েজ বা নিফাস শুরু করে, তবে তার রোযা ভেঙে যাবে এবং পরে কাজা করতে হবে। \n\n" +
                        "[বুখারি: ১৯৫০, মুসলিম: ৩৩৫]\n\n\n" +
                        "যেসব কারণে রোযা ভেঙে ফেলার অনুমতি নেই:\n\n" +
                        "হালকা মাথাব্যথা বা সাধারণ দুর্বলতা\n" +
                        "কষ্ট অনুভব করলেও সহ্য করা সম্ভব হলে\n" +
                        "অলসতা বা খাওয়ার ইচ্ছা হলে\n\n\n" +
                        "রোযা ভেঙে ফেলার পর করণীয়:\n\n" +
                        "পরে একটি কাজা রোযা রাখতে হবে। যদি ইচ্ছাকৃতভাবে ভেঙে ফেলে (বিশেষত খাওয়া বা সহবাস করে), তবে ৬০ দিন কাফফারার রোযা বা ৬০ জন গরিবকে খাওয়াতে হবে। \n\n" +
                        "[বুখারি: ১৯৩৬, মুসলিম: ১১১১]",
                "Illness (Severe sickness or exacerbation of condition):\n\n" +
                        "If someone falls severely ill after beginning the fast, or there is fear that fasting will worsen the illness, they may break the fast. Allah Almighty says:\n\n" +
                        "وَمَن كَانَ مَرِيضًا أَوْ عَلَىٰ سَفَرٍ فَعِدَّةٌ مِّنْ أَيَّامٍ أُخَرَ\n\n" +
                        "Meaning:\n" +
                        "\"And whoever is ill or on a journey, then an equal number of other days.\"\n" +
                        "[Surah Al-Baqarah: 185]\n\n\n" +
                        "For Pregnant and Nursing Mothers:\n\n" +
                        "If a pregnant or nursing mother fears harm for herself or infant, she may break the fast and make it up (Qaza) later.\n" +
                        "[Abu Dawud: 2408, An-Nasa'i: 2274]\n\n\n" +
                        "Traveling (Safir):\n\n" +
                        "If traveling (at least 78 km) and experiencing severe hardship, one may break the fast. The Messenger of Allah ﷺ said:\n" +
                        "\"Indeed, Allah has relieved the traveler of the obligation of fasting and halved the prayer.\"\n" +
                        "[An-Nasa'i: 2275, Ibn Majah: 1667]\n\n\n" +
                        "Extreme Weakness and Life-saving Necessity:\n\n" +
                        "If someone becomes extremely frail and there is apprehension of death due to fasting, they may break the fast. Preserving life is paramount.\n" +
                        "[Surah Al-Baqarah: 195]\n\n\n" +
                        "Unintentional Ingestion:\n\n" +
                        "Accidentally inhaling dense smoke, water, or choking substances permits breaking the fast and making it up later.\n\n\n" +
                        "Onset of Menstruation (Haiz) or Post-Natal Bleeding (Nifas):\n\n" +
                        "If menses or post-natal bleeding begins during daytime, the fast is invalidated and made up later.\n" +
                        "[Bukhari: 1950, Muslim: 335]\n\n\n" +
                        "Reasons for Which Breaking Fast is NOT Permitted:\n\n" +
                        "- Mild headache or regular tiredness\n" +
                        "- Experiencing hardships that are bearable\n" +
                        "- Laziness or mere appetite for food\n\n\n" +
                        "What to do After Breaking the Fast:\n\n" +
                        "One must make up (Qaza) one day of fast. Breaking without valid justification intentionally entails 60 consecutive days of Kaffarah fasts or feeding 60 poor individuals.\n" +
                        "[Bukhari: 1936, Muslim: 1111]",
                "কুরআন ২:১৮৫, বুখারি: ১৯৩৬, মুসলিম: ৩৩৫",
                "Quran 2:185, Bukhari: 1936, Muslim: 335"
        ));

        return list;
    }
}
