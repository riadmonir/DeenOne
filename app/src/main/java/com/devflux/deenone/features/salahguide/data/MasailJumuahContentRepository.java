package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Masail: জুম'আ (Jumu'ah Masail).
 * 100% verbatim text matching screenshots and authentic Islamic sources.
 */
public class MasailJumuahContentRepository {

    public static List<HajjHistoryCardItem> getJumuahCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আর নামায
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আর নামায",
            "Friday Jumu'ah Prayer",
            "জুমআর নামায প্রত্যেক সাবালক জ্ঞান-সম্পন্ন পুরুষের জন্য জামাআত সহকারে ফরয। মহান আল্লাহ বলেন- يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا نُودِيَ لِلصَّلَاةِ مِنْ يَوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَىٰ ذِكْرِ اللَّهِ وَذَرُوا الْبَيْعَ ۚ ذَٰلِكُمْ خَيْرٌ لَكُمْ إِنْ كُنْتُمْ تَعْلَمُونَ অর্থ: অর্থাৎ, হে ঈমানদারগণ! যখন জুমআর দিন নামা...",
            "Friday prayer is an individual obligation with congregation upon every adult, sane Muslim male. Allah says in the Qur'an: O you who believe! When the call is proclaimed for the prayer on Friday...",
            "জুমআর নামায প্রত্যেক সাবালক জ্ঞান-সম্পন্ন পুরুষের জন্য জামাআত সহকারে ফরয।\n\n" +
            "মহান আল্লাহ বলেন-\n\n" +
            "يَا أَيُّهَا الَّذِيْنَ آمَنُوْا إِذَا نُوْدِيَ لِلصَّلاَةِ مِنْ يَّوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَى ذِكْرِ اللهِ وَذَرُوا الْبَيْعَ، ذَلِكُمْ خَيْرٌ لَّكُمْ إِنْ كُنْتُمْ تَعْلَمُوْনَ\n\n" +
            "অর্থ:\n" +
            "অর্থাৎ, হে ঈমানদারগণ! যখন জুমআর দিন নামাযের জন্য আহবান করা হবে, তখন তোমরা সত্বর আল্লাহর স্মরণের জন্য উপস্থিত হও এবং ক্রয়-বিক্রয় বর্জন কর। এটিই তোমাদের জন্য কল্যাণকর, যদি তোমরা উপলব্ধি কর।\n\n" +
            "[কুরআন মাজীদ ৬২/৯]\n\n" +
            "মহানবী (ﷺ) বলেন, “দুনিয়াতে আমাদের আসার সময় সকল জাতির পরে। কিন্তু কিয়ামতের দিন আমরা সকলের অগ্রবর্তী। (সকলের আগে আমাদের হিসাব-নিকাশ হবে।) অবশ্য আমাদের পূর্বে ওদেরকে (ইয়াহুদী ও নাসারাকে) কিতাব দেওয়া হয়েছে। আমরা কিতাব পেয়েছি ওদের পরে। এই (জুমআর) দিনের তা’যীম ওদের উপর ফরয করা হয়েছিল। কিন্তু ওরা তাতে মতভেদ করে বসল। পক্ষান্তরে আল্লাহ আমাদেরকে তাতে একমত হওয়ার তওফীক দান করেছেন। সুতরাং সকল মানুষ আমাদের থেকে পশ্চাতে। ইয়াহুদী আগামী দিন (শনিবার)কে তাযীম করে (জুমআর দিন বলে মানে) এবং নাসারা করে তার পরের দিন (রবিবার)কে।”\n\n" +
            "[বুখারী, মুসলিম, মিশকাত]\n\n" +
            "মহানবী (ﷺ) বলেন, “প্রত্যেক সাবালক পুরুষের জন্য জুমআয় উপস্থিত হওয়া ওয়াজেব।”\n\n" +
            "[নাসাঈ, সুনান -১৩৭১]\n\n" +
            "হযরত ইবনে মসউদ (রাঃ) কর্তৃক বর্ণিত, নবী (ﷺ) বলেন, “আমি ইচ্ছা করেছি যে, এক ব্যক্তিকে লোকেদের ইমামতি করতে আদেশ করে ঐ শ্রেণীর লোকেদের ঘর-বাড়ি পুড়িয়ে দিই, যারা জুমআতে অনুপস্থিত থাকে।” \n\n" +
            "[মুসলিম - ৬৫২]\n\n" +
            "হযরত আবূ হুরাইরা (রাঃ) ও ইবনে উমার (রাঃ) কর্তৃক বর্ণিত, তাঁরা শুনেছেন, আল্লাহর রসূল (ﷺ) তাঁর মিম্বরের কাঠের উপর বলেছেন যে, “কতক সম্প্রদায় তাদের জুমুআহ ত্যাগ করা হতে অতি অবশ্যই বিরত হোক, নতুবা আল্লাহ তাদের অন্তরে অবশ্যই মোহ্র মেরে দেবেন। অতঃপর তারা অবশ্যই অবহেলাকারীদের অন্তর্ভুক্ত হয়ে যাবে।” \n\n" +
            "[মুসলিম, সহীহ - ৮৬৫, ইবনে মাজাহ্, সুনান]\n\n" +
            "হযরত আবুল জা’দ যামরী (রাঃ) হতে বর্ণিত, নবী (ﷺ) বলেন, “যে ব্যক্তি বিনা ওজরে তিনটি জুমুআহ ত্যাগ করবে সে ব্যক্তি মুনাফিক।” \n\n" +
            "[ইবনে খুযাইমাহ্, সহীহ, ইবনে হিব্বান, সহীহ, সহিহ তারগিব - ৭২৬]\n\n" +
            "হযরত জাবের বিন আব্দুল্লাহ্ (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, একদা নবী (ﷺ) জুমআর দিন খাড়া হয়ে খুতবা দানকালে বললেন, “সম্ভবত: এমনও লোক আছে, যার নিকট জুমুআহ উপস্থিত হয়; অথচ সে মদ্বীনা থেকে মাত্র এক মাইল দূরে থাকে এবং জুমআয় হাযির হয় না।” দ্বিতীয় বারে তিনি বললেন, “সম্ভবত: এমন লোকও আছে যার নিকট জুমুআহ উপস্থিত হয়; অথচ সে মদ্বীনা থেকে মাত্র দুই মাইল দূরে থাকে এবং জুমআয় হাজির হয় না।” অতঃপর তৃতীয়বারে তিনি বললেন, “সম্ভবত: এমন লোকও আছে যে মদ্বীনা থেকে মাত্র তিন মাইল দূরে থাকে এবং জুমআয় হাজির হয় না তার হৃদয়ে আল্লাহ মোহ্র মেরে দেন。\n\n" +
            "[সহিহ তারগিব - ৭৩১]\n\n" +
            "হযরত ইবনে আব্বাস (রাঃ) বলেন, “যে ব্যক্তি পরপর ৩ টি জুমুআহ ত্যাগ করল, সে অবশ্যই ইসলামকে নিজের পিছনে ফেলে দিল।” \n\n" +
            "[সহিহ তারগিব - ৭৩২]",
            "Friday (Jumu'ah) prayer is an individual obligation (Fardh 'Ayn) with congregation upon every sane, adult Muslim male.\n\n" +
            "Allah the Exalted proclaims:\n" +
            "\"O you who have believed, when the call is made for prayer on the day of Jumu'ah, proceed to the remembrance of Allah and leave off trade. That is better for you, if you only knew.\"\n\n" +
            "[Quran Majeed 62:9]\n\n" +
            "The Holy Prophet (ﷺ) said: \"We are the last (to come in this world) but we will be the foremost on the Day of Resurrection, even though they were given the Book before us and we were given the Book after them. This day (Friday) was made obligatory upon them, but they differed about it, whereas Allah guided us to it. Therefore, all other people follow us: the Jews celebrate tomorrow (Saturday) and the Christians the day after (Sunday).\"\n\n" +
            "[Bukhari, Muslim, Mishkat]\n\n" +
            "The Holy Prophet (ﷺ) said: \"Attending Jumu'ah is obligatory upon every adult male.\"\n\n" +
            "[Nasa'i, Sunan #1371]\n\n" +
            "Narrated by Ibn Mas'ud (RA), the Prophet (ﷺ) said: \"I intended to command a man to lead people in prayer, and then burn down the houses over those men who abandon the Jumu'ah prayer.\"\n\n" +
            "[Muslim #652]\n\n" +
            "Narrated by Abu Hurairah (RA) and Ibn Umar (RA), they heard the Messenger of Allah (ﷺ) say upon the wooden steps of his pulpit: \"People must cease neglecting the Friday prayers, or Allah will surely seal their hearts and they will become among the unmindful.\"\n\n" +
            "[Muslim, Sahih #865, Ibn Majah]\n\n" +
            "Narrated by Abul Ja'd ad-Damri (RA), the Prophet (ﷺ) said: \"Whoever misses three Friday prayers without a valid excuse is a hypocrite (Munafiq).\"\n\n" +
            "[Ibn Khuzaymah, Ibn Hibban, Sahih at-Targhib #726]\n\n" +
            "Narrated by Jabir ibn Abdullah (RA), the Prophet (ﷺ) said while delivering the Friday Khutbah: \"There might be a person upon whom Jumu'ah becomes obligatory while living just one mile away from Madinah and he does not attend.\" Then he said: \"There might be a person living two miles away who does not attend.\" Then he said: \"There might be a person living three miles away and does not attend—Allah sets a seal upon his heart.\"\n\n" +
            "[Sahih at-Targhib #731]\n\n" +
            "Ibn Abbas (RA) stated: \"Whoever abandons three consecutive Friday prayers has indeed cast Islam behind his back.\"\n\n" +
            "[Sahih at-Targhib #732]"
        ));

        // 2. জুম'আর নামাজের ইতিহাস
        list.add(new HajjHistoryCardItem(
            2,
            "জুম'আর নামাজের ইতিহাস",
            "History of Jumu'ah Prayer",
            "জুমার ব্যাখ্যা ও শাব্দিক অর্থ: আরবি শব্দ ‘জুমআহ/জুমুআহ’র ব্যাপারে একাধিক মতামত পাওয়া যায়। জমহুর উলামায়ে কেরামের মতে শব্দটি হবে ‘জুমুআহ’। আর ইমাম আ’মাশ (রহ.)-এর মতে, শব্দটি হবে ‘জুমআহ’। [রুহুল মাআনি: ১৪/৯৯ দরসে তিরমিজি:২/১৯২- এর সূত্রে]...",
            "Explanation and Linguistic Meaning of Jumu'ah: Multiple opinions exist regarding the Arabic term 'Jum'ah / Jumu'ah'. According to the majority of scholars (Jamhur), the term is 'Jumu'ah', while according to Imam al-A'mash (RA), it is 'Jum'ah'. [Ruh al-Ma'ani: 14/99; via Dars-e-Tirmidhi: 2/192]...",
            "জুমার ব্যাখ্যা ও শাব্দিক অর্থ:\n\n" +
            "আরবি শব্দ ‘জুমআহ/জুমুআহ’র ব্যাপারে একাধিক মতামত পাওয়া যায়। জমহুর উলামায়ে কেরামের মতে শব্দটি হবে ‘জুমুআহ’। আর ইমাম আ’মাশ (রহ.)-এর মতে, শব্দটি হবে ‘জুমআহ’। \n\n" +
            "[রুহুল মাআনি: ১৪/৯৯ দরসে তিরমিজি:২/১৯২- এর সূত্রে]\n\n" +
            "আমাদের বাংলাদেশের রীতি অনুযায়ী একে ‘জুমা’ বলা হয়। জুমার অর্থ হলো একত্র হওয়া, সংঘবদ্ধ হওয়া। যেহেতু এই দিনে মানুষ একত্র হয়, তাই এ দিনকে ইয়াওমুল জুমা বা জুমার দিনও বলা হয়।\n\n" +
            "জুমার দিনের নামকরণ:\n\n" +
            "মূর্খতাযুগে শুক্রবারকে ‘ইয়াওমে আরুবা’ বলা হতো। আরবে কাআব ইবনে লুয়াই সর্বপ্রথম এর নাম ‘ইয়াওমুল জুমুআ’ বা জুমার দিন রাখেন। এই দিনে কোরাইশদের সমাবেশ হতো এবং কাআব ইবনে লুয়াই ভাষণ দিতেন। এটা রাসুলুল্লাহ (সা.)-এর আবির্ভাবের পাঁচশ ষাট বছর পূর্বের ঘটনা। কাআব ইবনে লুয়াই রাসুলুল্লাহ (সা.)-এর পূর্বপুরুষদের অন্যতম।\n\n" +
            "[তাফসিরে মারেফুল কোরআন বাংলা - ১৩৭১]\n\n" +
            "ইসলাম-পরবর্তী সময়ে এ দিনে মুসলিম উম্মাহ আল্লাহর নির্দেশ পালনের উদ্দেশ্যে মসজিদে একত্র হয় বলে দিনটিকে ইয়াওমুল জুমুআ বা জুমার দিন বলা হয়। কোরআনেও সে কথাটি ওঠে এসেছে।ইরশাদ হয়েছে,\n\n" +
            "হে মুমিনগণ, জুমার দিনে যখন নামাজের আজান দেওয়া হয়, তখন তোমরা আল্লাহর স্মরণে দ্রুত মসজিদের দিকে ধাবিত হও। আর বেচাকেনা বন্ধ করো। এটা তোমাদের জন্য উত্তম, যদি তোমরা বুঝতে পারো।\n\n" +
            "[সুরা জুমা - ৯]\n\n" +
            "যেভাবে জুমার সূচনা:\n\n" +
            "প্রথম হিজরি থেকে। প্রিয় নবী (সা.) পবিত্র নগরী মক্কা থেকে হিজরত করে মদিনার কুবায় গিয়ে অবস্থান করেন। সেখান থেকে জুমার দিন মদিনায় যাওয়ার পথে বনি সালেম গোত্রের উপত্যকায় পৌঁছলে জোহরের সময় হয়ে যায়। তিনি সেখানেই জুমা আদায় করেন। ইসলামের ইতিহাসে এটাই ছিল প্রথম জুমা। তবে হিজরতের পর জুমার নামাজ ফরজ হওয়ার আগে বিশ্বনবী মুহাম্মদ (সা.)-এর নবুয়তের ১২তম বর্ষে মদিনায় নাকিউল খাজিমাতে দুই রাকাত জুমা পড়ার প্রমাণ পাওয়া যায়। সেখানে হজরত আসআদ বিন জুরারাহ (রা.)-এর ইমামতিতে শুক্রবার সে নামাজ অনুষ্ঠিত হয়। আর সেটি ছিল নফল নামাজ। এ প্রসঙ্গে মুহাম্মদ ইবনে সিরিন থেকে বর্ণিত আছে, রাসুল (সা.) মদিনায় আগমনের পর এবং জুমার নামাজ ফরজ হওয়ার আগে একবার মদিনার আনসারগণ একত্র হয়ে আলোচনা করলেন যে ইহুদিদের জন্য সপ্তাহে একটা দিন নির্দিষ্ট আছে, যে দিন তারা সবাই একত্র হয়। আর নাসারাদেরও সপ্তাহে সবার একত্র হওয়ার জন্য এক দিন নির্ধারিত আছে। সুতরাং আমাদের জন্য সপ্তাহে একটা দিন নির্দিষ্ট হওয়া প্রয়োজন, যে দিন আমরা সবাই সমবেত হয়ে আল্লাহকে স্মরণ করব, নামাজ আদায় করব। অতঃপর তারা আলোচনাকালে বললেন, শনিবার ইহুদিদের আর রবিবার নাসারাদের জন্য নির্ধারিত। অবশেষে তাঁরা ‘ইয়াওমুল আরুবা’ (শুক্রবার)-কে গ্রহণ করলেন এবং তাঁরাই এদিনকে ‘জুমার দিন’ নামকরণ করলেন। \n\n" +
            "[দরসে তিরমিজি - ২/১৯২-এর সূত্রে মুসান্নাফে আবদুর রাজ্জাক - ৩/১৫৯, হাদিস - ৫১৪৪]\n\n" +
            "তবে বিশ্বনবী (সা.) কুবা নামক স্থান থেকে মদিনায় পৌঁছার পথে বনি সালেম গোত্রের উপত্যকায় সবাইকে নিয়ে যে জুমা আদায় করেন, এটিকে ইসলামের প্রথম জুমা ধরা হয়। আর সেদিন থেকেই মুমিন মুসলমানদের জুমা আদায় শুরু হয়। সুতরাং জুমা হলো, মুমিন মুসলমানের জন্য বিশেষ ফজিলতপূর্ণ ইবাদত। যা প্রিয় নবী (সা.) মদিনায় হিজরত করে আনুষ্ঠানিকভাবে বনি সালেম গোত্রের উপত্যকায় আদায়ের মাধ্যমে শুরু করেছিলেন। আজও মুমিন-মুসলমানের জন্য তা বিশেষ ইবাদত হিসেবে জারি আছে।",
            "Explanation and Linguistic Meaning of Jumu'ah:\n\n" +
            "Multiple opinions exist regarding the Arabic term 'Jum'ah / Jumu'ah'. According to the majority of scholars (Jamhur), the term is pronounced 'Jumu'ah', whereas according to Imam al-A'mash (RA), it is vocalized as 'Jum'ah'.\n\n" +
            "[Ruh al-Ma'ani: 14/99; via Dars-e-Tirmidhi: 2/192]\n\n" +
            "In customary convention, it is called Jumu'ah. The linguistic meaning of Jumu'ah is gathering, uniting, and assembling together. Since people congregate on this day, it is termed 'Yawm al-Jumu'ah' or the Day of Friday.\n\n" +
            "Naming of the Day of Jumu'ah:\n\n" +
            "During the Pre-Islamic Era of Ignorance (Jahiliyyah), Friday was called 'Yawm al-'Arubah'. In Arabia, Ka'b ibn Lu'ayy was the first to name it 'Yawm al-Jumu'ah' (the Day of Assembly). On this day, the Quraysh used to assemble and Ka'b ibn Lu'ayy would deliver an address to them. This took place five hundred and sixty years before the advent of the Prophet (ﷺ). Ka'b ibn Lu'ayy was one of the noble ancestors of the Prophet (ﷺ).\n\n" +
            "[Tafsir Ma'ariful Quran Bangla - 1371]\n\n" +
            "In the Islamic era, this day is called Yawm al-Jumu'ah because the Muslim Ummah gathers in mosques to fulfill the divine command of Allah. This is proclaimed in the Holy Quran, where Allah says:\n\n" +
            "\"O you who have believed, when the call is proclaimed for the prayer on the day of Jumu'ah, proceed swiftly to the remembrance of Allah and cease all trade. That is better for you, if you only knew.\"\n\n" +
            "[Surah Al-Jumu'ah: 9]\n\n" +
            "How Jumu'ah Began:\n\n" +
            "From the first year of Hijrah. The Holy Prophet (ﷺ) migrated from the sacred city of Makkah and stayed at Quba in Madinah. From there, while journeying to Madinah on Friday, upon reaching the valley of the Banu Salim tribe, the time of Dhuhr arrived. He offered the Jumu'ah prayer there. In Islamic history, this was the very first congregational Jumu'ah. However, prior to the obligation of Jumu'ah following Hijrah, historical evidence shows that in the 12th year of Prophethood in Madinah, two Rak'ahs of Jumu'ah were offered at Naqi' al-Khadhimat under the leadership of As'ad ibn Zurarah (RA) on a Friday, which was a voluntary prayer. Regarding this, it is narrated from Muhammad ibn Sirin that before the arrival of the Prophet (ﷺ) in Madinah and prior to Jumu'ah being made obligatory, the Ansar of Madinah gathered and discussed: \"The Jews have a specific day in the week on which they assemble, and the Christians likewise have a designated day in the week for gathering. Therefore, we should establish a day in the week on which we all gather to remember Allah and pray.\" They noted that Saturday belonged to the Jews and Sunday to the Christians. Consequently, they adopted 'Yawm al-'Arubah' (Friday) and named it the Day of Jumu'ah.\n\n" +
            "[Dars-e-Tirmidhi: 2/192; via Musannaf Abdur Razzaq: 3/159, Hadith #5144]\n\n" +
            "However, the Jumu'ah prayer that the Prophet (ﷺ) led for everyone in the valley of Banu Salim on his way from Quba to Madinah is recognized as the first official Jumu'ah in Islam. From that day, the continuous practice of Jumu'ah for Muslims commenced. Thus, Jumu'ah is an exceptionally virtuous act of worship for believers, officially established by the Prophet (ﷺ) upon his migration in the valley of Banu Salim, and it remains a cornerstone of Islamic devotion to this day."
        ));

        // 3. স্থানীয় ভাষায় জুম'আর খুতবা
        list.add(new HajjHistoryCardItem(
            3,
            "স্থানীয় ভাষায় জুম'আর খুতবা",
            "Jumu'ah Khutbah in Local Language",
            "জুমআর জমায়েত মুসলিমদের একটি প্রশিক্ষণ ক্ষেত্র। সাপ্তাহিক এই প্রশিক্ষণে মুসলিমের বিস্মৃত কথা স্মরণ হয়, চলার পথে অন্ধকারে আলোর দিশা পায়, সুন্দর চরিত্র ও ব্যবহার গড়তে সহায়তা পায়, ঈমান নবায়ন হয়, হৃদয় নরম হয়, মৃত্যু ও পরকালের স্মরণ হয়, ত...",
            "The Friday Jumu'ah gathering is a training ground for Muslims. Through this weekly assembly, believers are reminded of forgotten truths, find guidance in darkness, gain support in cultivating noble character...",
            "জুমআর জমায়েত মুসলিমদের একটি প্রশিক্ষণ ক্ষেত্র। সাপ্তাহিক এই প্রশিক্ষণে মুসলিমের বিস্মৃত কথা স্মরণ হয়, চলার পথে অন্ধকারে আলোর দিশা পায়, সুন্দর চরিত্র ও ব্যবহার গড়তে সহায়তা পায়, ঈমান নবায়ন হয়, হৃদয় নরম হয়, মৃত্যু ও পরকালের স্মরণ হয়, তওবা করতে অনুপ্রাণিত হয়, ভালো কাজ করতে এবং খারাপ কাজ বর্জন করতে উৎসাহ্ পায়, ইত্যাদি।\n\n" +
            "তাই খুতবার ভূমিকা আরবীতে হওয়ার পর স্থানীয় ভাষায় বাকী খুতবা পাঠ বৈধ। যেহেতু খুতবার আসল উদ্দেশ্য হল জনসাধারণকে শরীয়তের শিক্ষা ও উপদেশ দান করা। আর তা আরবীতে হলে উদ্দেশ্য বিফল হয়। সুতরাং যে খুতবা আরবীতে হত তারই ভাবার্থ স্থানীয় ভাষায় হলে মুসলিমদেরকে সপ্তাহান্তে একবার উপদেশ ও পথ নির্দেশনা দান করার মত মহান উদ্দেশ্য সাধিত হয়। পক্ষান্তরে খুতবা নামাযের মত নয়। নামাযে অন্য ভাষা বললে নামায বাতিল। কিন্তু খুতবা তা নয়। যেমন খুতবা ছেড়ে অন্য কথা বলা যায়, নামাযে তা যায় না। ইত্যাদি।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪২২-৪২৩, মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৫/৮৪]\n\n" +
            "পক্ষান্তরে খুতবার আগে স্থানীয় ভাষায় খুতবা দেওয়া বিধেয় নয়। কারণ, উপায় থাকতেও ডবল খুতবা হয়ে যায় তাতে। ডিষ্টার্ব হয় নামায, তেলাওয়াত ও যিক্ররত মুসল্লীদের।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৭/৭১-৭২]\n\n" +
            "উল্লেখ্য যে, কোন স্থানের জামাআতে খুতবা দেওয়ার মত কোন লোক না থাকার ফলে যদি খুতবা দেওয়া না হয়, তাহলে সেই জামাআতের লোক জুমুআহ না পড়ে যোহ্র পড়বে।\n\n" +
            "[ইবনে আবী শাইবা ৫২৬৯-৫২৭৬]\n\n" +
            "জ্ঞাতব্য যে, যিনি খুতবা দেবেন তাঁরই নামায পড়া জরুরী নয়। যদিও সুন্নত হল খতীবেরই ইমামতি করা।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪১০, ৪১৩]",
            "The Friday Jumu'ah gathering is a training ground for Muslims. Through this weekly assembly, believers are reminded of forgotten truths, find light in the darkness of life's path, receive assistance in cultivating noble character and conduct, have their faith renewed, hearts softened, are reminded of death and the Hereafter, inspired to repent, and encouraged to do good deeds and shun evil, and so forth.\n\n" +
            "Therefore, delivering the introduction of the Khutbah in Arabic and reciting the remainder of the sermon in the local language is permissible. This is because the fundamental purpose of the Khutbah is to provide Islamic education and admonition to the general public. If it is delivered entirely in Arabic to a non-Arabic congregation, this core purpose is lost. Hence, conveying the essence and meaning in the native local language accomplishes the great objective of providing weekly guidance and advice to Muslims. On the other hand, the Khutbah is not like the ritual prayer (Salah). In prayer, speaking in any language other than Arabic invalidates the prayer; however, this is not the case for the Khutbah. For instance, speaking outside the sermon is permitted when necessary, whereas it is strictly forbidden during Salah, and so on.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/422-423; Majallatul Buhuthil Islamiyyah 15/84]\n\n" +
            "Conversely, delivering a sermon in the local language prior to the official Khutbah is not prescribed. This is because doing so effectively results in a double Khutbah despite having viable alternatives, and it disturbs the worshippers engaged in voluntary prayers, Qur'anic recitation, and remembrance of Allah.\n\n" +
            "[Majallatul Buhuthil Islamiyyah 17/71-72]\n\n" +
            "It is noteworthy that if a congregation lacks anyone capable of delivering a Khutbah and consequently no Khutbah is given, the worshippers of that congregation must offer Dhuhr prayer instead of Jumu'ah.\n\n" +
            "[Ibn Abi Shaybah 5269-5276]\n\n" +
            "It is important to know that it is not obligatory for the one who delivers the Khutbah to also lead the prayer, although the established Sunnah is for the Khatib himself to lead the congregation in prayer.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/410, 413]"
        ));

        // 4. জুম'আর দিনে করণীয়
        list.add(new HajjHistoryCardItem(
            4,
            "জুম'আর দিনে করণীয়",
            "Sunnah Duties on Friday",
            "জুমআর ফজরের প্রথম রাকআতে সূরা সাজদাহ এবং দ্বিতীয় রাকআতে সূরা দাহ্র (ইনসান) পাঠ করা। উভয় সূরা প্রথম থেকে শেষ পর্যন্ত পাঠ করাই সুন্নত। প্রত্যেক সূরার কিছু করে অংশ পড়া সুন্নত নয়। অবশ্য অন্য সূরা পড়া দোষাবহ্ নয়। বরং কখনো কখনো ঐ দুই ...",
            "Reciting Surah As-Sajdah in the first Rak'ah and Surah Ad-Dahr (Al-Insan) in the second Rak'ah of Fajr prayer on Friday. It is Sunnah to recite both surahs completely from beginning to end...",
            "জুমআর ফজরের প্রথম রাকআতে সূরা সাজদাহ এবং দ্বিতীয় রাকআতে সূরা দাহ্র (ইনসান) পাঠ করা। উভয় সূরা প্রথম থেকে শেষ পর্যন্ত পাঠ করাই সুন্নত। প্রত্যেক সূরার কিছু করে অংশ পড়া সুন্নত নয়। অবশ্য অন্য সূরা পড়া দোষাবহ্ নয়। বরং কখনো কখনো ঐ দুই সূরা না পড়াই উচিৎ। যাতে সাধারণ মানুষ তা পড়া জরুরী মনে না করে বসে। বরং তা জরুরী মনে করে পড়া এবং কখনো কখনো না ছাড়া বা কেউতা না পড়লে আপত্তি করা বিদআত।\n\n" +
            "[মু’জামুল বিদা’ ২৮১পৃ:]",
            "Reciting Surah As-Sajdah in the first Rak'ah and Surah Ad-Dahr (Al-Insan) in the second Rak'ah of Fajr prayer on Friday. It is the established Sunnah to recite both surahs completely from beginning to end. Reciting only portions of each surah is not the Sunnah. However, reciting other surahs is not sinful. In fact, it is recommended to occasionally omit reciting these two surahs so that the general public does not consider reciting them as strictly obligatory. Rather, regarding it as mandatory, never omitting it, or objecting when someone recites other surahs is an innovation (Bid'ah).\n\n" +
            "[Mu'jam al-Bida' p. 281]"
        ));

        // 5. জুম'আর আগে ও পরে সুন্নত
        list.add(new HajjHistoryCardItem(
            5,
            "জুম'আর আগে ও পরে সুন্নত",
            "Sunnah Before & After Jumu'ah",
            "জুমআর খুতবার পূর্বে ‘কাবলাল জুমুআহ’ বলে কোন নির্দিষ্ট রাকআত সুন্নত নেই। অতএব নামাযী মসজিদে এলে ‘তাহিয়্যাতুল মাসজিদ’ ২ রাকআত সুন্নত পড়ে বসে যেতে পারে এবং দুআ, দরুদ তাসবীহ- যিকর বা তেলাওয়াত করতে পারে। আবার ইচ্ছা হলে নামাযও পড়তে পারে। তবে এ নামায হবে নফল এবং অনির্দিষ্ট সংখ্যায়...",
            "There is no fixed, designated number of Rak'ahs prescribed as 'Qablal Jumu'ah' Sunnah prior to the Friday sermon. Therefore, upon entering the mosque, the worshipper may offer 2 Rak'ahs of Tahiyyatul Masjid, sit down, and engage in supplication, sending Durood, Dhikr, or reciting the Holy Qur'an...",
            "জুমআর খুতবার পূর্বে ‘কাবলাল জুমুআহ’ বলে কোন নির্দিষ্ট রাকআত সুন্নত নেই। অতএব নামাযী মসজিদে এলে ‘তাহিয়্যাতুল মাসজিদ’ ২ রাকআত সুন্নত পড়ে বসে যেতে পারে এবং দুআ, দরুদ তাসবীহ- যিকর বা তেলাওয়াত করতে পারে। আবার ইচ্ছা হলে নামাযও পড়তে পারে। তবে এ নামায হবে নফল এবং অনির্দিষ্ট সংখ্যায়।\n\n" +
            "মহানবী (ﷺ) বলেন,\n" +
            "যে ব্যক্তি জুমআর দিন যথা নিয়মে গোসল করে, দাঁত পরিষ্কার করে, খোশবূ থাকলে তা ব্যবহার করে, তার সবচেয়ে সুন্দর পোশাক পরে, অতঃপর (মসজিদে) যায়, নামাযীদের ঘাড় ডিঙিয়ে (কাতার চিরে) আগে যায় না, অতঃপর আল্লাহর ইচ্ছানুযায়ী নামায পড়ে। তারপর ইমাম উপস্থিত হলে নীরব ও নিশ্চুপ থাকে এবং নামায শেষ না হওয়া পর্যন্ত কোন কথা বলে না, সে ব্যক্তির এ কাজ এই জুমুআহ থেকে অপর জুমআর মধ্যবর্তীকালে কৃত পাপের কাফফারা হয়ে যায়।\n\n" +
            "[আহমাদ, মুসনাদ, ইবনে মাজাহ্, সুনান,হাকেম, মুস্তাদরাক, জামে ৬০৬৬নং]\n\n" +
            "প্রকাশ থাকে যে, প্রত্যেক আযান ও ইকামতের মাঝে নামায আছে।\n\n" +
            "[বুখারী, মুসলিম, মিশকাত ৬৬২নং]\n\n" +
            "এই হাদীস দ্বারা কাবলাল জুমআর সুন্নত প্রমাণ হয় না। কারণ, বিদিত যে, জুমআর আযান ও ইকামতের মাঝে থাকে খুতবা। আর মহানবী (ﷺ)-এর যুগে পূর্বের আর একটি আযান ছিল না। আর সুন্নত প্রমাণ হলেও মুআক্কাদাহ ও নির্দিষ্ট সংখ্যক নয়। তদনুরুপ “এমন কোন ফরয নামায নেই, যার পূর্বে ২ রাকআত নামায নেই।\n\n" +
            "[ইবনে হিব্বান, সহীহ, ত্বাবারানীরানী, মু’জাম, সিলসিলাহ সহীহাহ, আলবানী ২৩২, জামে ৫৭৩০নং]\n\n" +
            "এ হাদীস দ্বারাও জুমআর পূর্বে ২ রাকআত সুন্নত প্রমাণ হয় না। কারণ, জুমআর ফরয নামাযের পূর্বে খুতবা হয়। আর খুতবার পূর্বে ২ রাকআত নামায এ দ্বারা প্রমাণিত হয় না।\n\n" +
            "[সিলসিলাহ সহীহাহ, আলবানী ২৩২নং]",
            "There is no fixed, designated number of Rak'ahs prescribed as 'Qablal Jumu'ah' Sunnah prior to the Friday sermon. Therefore, upon entering the mosque, the worshipper may offer 2 Rak'ahs of Tahiyyatul Masjid, sit down, and engage in supplication, sending Durood, Dhikr, or reciting the Holy Qur'an. Alternatively, one may offer voluntary (Nafl) prayers as desired, though these voluntary prayers are unrestricted in number.\n\n" +
            "The Holy Prophet (ﷺ) said:\n" +
            "\"Whoever takes a bath on Friday according to the prescribed rules, cleans their teeth, applies perfume if available, wears their best clothes, then proceeds to the mosque without stepping over the shoulders of the worshippers (or breaking rows), then prays whatever Allah wills for them to pray; and when the Imam appears, remains completely silent and listens attentively until the prayer finishes—this act will serve as an expiation for the sins committed between that Friday and the next.\"\n\n" +
            "[Ahmad, Musnad; Ibn Majah, Sunan; Al-Hakim, Mustadrak; Jami #6066]\n\n" +
            "It is worth noting that: \"Between every two calls (Adhan and Iqamah) there is a prayer.\"\n\n" +
            "[Bukhari, Muslim, Mishkat #662]\n\n" +
            "This Hadith does not establish a specific Sunnah prayer before Jumu'ah (Qablal Jumu'ah). Because, as is well known, the Khutbah takes place between the Friday Adhan and Iqamah. Furthermore, during the era of the Holy Prophet (ﷺ), there was no earlier additional Adhan. Even if any Sunnah were inferred, it is not an emphasized Sunnah (Mu'akkadah) nor is it a fixed number of Rak'ahs. Similarly, the narration: \"There is no obligatory prayer except that before it there are two Rak'ahs of prayer.\"\n\n" +
            "[Ibn Hibban, Sahih; Tabarani, Mu'jam; Silsilah Sahihah, Albani #232; Jami #5730]\n\n" +
            "This Hadith also does not establish a two-Rak'ah Sunnah before Jumu'ah. Because the Khutbah precedes the obligatory Jumu'ah prayer, and offering a two-Rak'ah prayer prior to the Khutbah is not proven by this narration.\n\n" +
            "[Silsilah Sahihah, Albani #232]"
        ));

        // 6. জুম'আর সময়
        list.add(new HajjHistoryCardItem(
            6,
            "জুম'আর সময়",
            "Time of Jumu'ah Prayer",
            "অধিকাংশ সাহাবা, তাবেঈন ও ইমামগণের নিকট জুমআর সময় যোহরের সময় একই। অর্থাৎ, সূর্য ঢলার পর থেকে নিয়ে প্রত্যেক বস্তুর ছায়া তার সমপরিমাণ হওয়া (আসরের আগে) পর্যন্ত। হযরত আনাস (রাঃ) বলেন, ‘নবী (ﷺ) জুমুআহ তখন পড়তেন, যখন সূর্য প...",
            "According to the majority of the Sahabah, Tabi'un, and Imams, the time for Friday prayer is identical to the time of Dhuhr prayer: starting from the moment the sun declines past the zenith...",
            "অধিকাংশ সাহাবা, তাবেঈন ও ইমামগণের নিকট জুমআর সময় যোহরের সময় একই। অর্থাৎ, সূর্য ঢলার পর থেকে নিয়ে প্রত্যেক বস্তুর ছায়া তার সমপরিমাণ হওয়া (আসরের আগে) পর্যন্ত।\n\n" +
            "হযরত আনাস (রাঃ) বলেন, ‘নবী (ﷺ) জুমুআহ তখন পড়তেন, যখন সূর্য পশ্চিম আকাশেঢলে যেত।’ [আহমাদ, মুসনাদ, বুখারী, আবূদাঊদ, সুনান, তিরমিযী, সুনান, বায়হাকী]\n\n" +
            "ইমাম বুখারী বলেন, ‘জুমআর সময় সূর্য ঢলার পরই শুরু হয়। হযরত উমার, আলী, নু’মান বিন বাশীর এবং আম্র বিন হুয়াইরিষ কর্তৃক এ ব্যাপারে বর্ণনা পাওয়া যায়।’\n\n" +
            "[বুখারী]\n\n" +
            "হযরত সালামাহ্ বিন আকওয়া’ (রাঃ) বলেন, ‘আমরা যখন নবী (ﷺ)-এর সাথে জুমআর নামায পড়ে ঘরে ফিরতাম, তখন দেওয়ালের কোন ছায়া থাকত না।’\n\n" +
            "[বুখারী, মুসলিম, আবূদাঊদ, সুনান]\n\n" +
            "হযরত আনাস (রাঃ) বলেন, ‘ঠান্ডা খুব বেশী হলে নবী (ﷺ) জুমআর নামায সকাল সকাল পড়তেন এবং গরম খুব বেশী হলে দেরী করে পড়তেন।’\n\n" +
            "[বুখারী]",
            "According to the majority of the Sahabah (Companions), Tabi'un (Successors), and Imams, the time for Friday (Jumu'ah) prayer is identical to the time of Dhuhr prayer: starting from the moment the sun passes its zenith (meridian) until the shadow of an object equals its length (prior to Asr).\n\n" +
            "Narrated by Anas (RA): 'The Prophet (ﷺ) used to offer the Friday prayer when the sun declined past the zenith.' [Ahmad, Musnad; Bukhari; Abu Dawud, Sunan; Tirmidhi, Sunan; Bayhaqi]\n\n" +
            "Imam Bukhari stated: 'The time of Friday prayer begins immediately after the decline of the sun past its zenith. Narrations to this effect are recorded from Umar, Ali, Nu'man ibn Bashir, and Amr ibn Hurayrith.'\n\n" +
            "[Bukhari]\n\n" +
            "Narrated by Salamah ibn al-Akwa' (RA): 'When we returned home after offering the Friday prayer with the Prophet (ﷺ), there was hardly enough shadow from the walls for us to seek shade.'\n\n" +
            "[Bukhari, Muslim, Abu Dawud, Sunan]\n\n" +
            "Narrated by Anas (RA): 'When it was extremely cold, the Prophet (ﷺ) would offer the Friday prayer early, and when it was intensely hot, he would delay it until it became cooler.'\n\n" +
            "[Bukhari]"
        ));

        // 7. জুম'আর স্থান
        list.add(new HajjHistoryCardItem(
            7,
            "জুম'আর স্থান",
            "Location for Jumu'ah Prayer",
            "জুমুআহ যেমন শহরবাসীর জন্য ফরয, তেমনি ফরয গ্রামবাসীর জন্যও। এর জন্য খলীফা হওয়া, শহর হওয়া, জামে মসজিদ হওয়া বা ৪০ জন নামাযী হওয়া শর্ত নয়। বরং যেখানেই স্থানীয় স্থায়ী বসবাসকারী জামাআত পাওয়া যাবে, সেখানেই জুমুআহ ফরয। [মাজাল্লা...",
            "Friday prayer is obligatory upon villagers just as it is obligatory upon city dwellers. Having a Caliph, being a major city, having a grand Jame Mosque, or requiring 40 worshippers is not an essential prerequisite...",
            "জুমুআহ যেমন শহরবাসীর জন্য ফরয, তেমনি ফরয গ্রামবাসীর জন্যও। এর জন্য খলীফা হওয়া, শহর হওয়া, জামে মসজিদ হওয়া বা ৪০ জন নামাযী হওয়া শর্ত নয়। বরং যেখানেই স্থানীয় স্থায়ী বসবাসকারী জামাআত পাওয়া যাবে, সেখানেই জুমুআহ ফরয।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ২২/৭৫, ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪২৪]\n\n" +
            "হযরত ইবনে আব্বাস (রাঃ) বলেন, ‘নবী (ﷺ)-এর মসজিদে জুমুআহ প্রতিষ্ঠিত হওয়ার পর ইসলামে সর্বপ্রথম যে জুমুআহ প্রতিষ্ঠিত হয়, তা হল বাহ্রাইনের জুয়াষা নামক এক গ্রামে।’\n\n" +
            "[বুখারী ৮৯২, ৪৩৭১, আবূদাঊদ, সুনান ১০৬৮নং]\n\n" +
            "হযরত ইবনে উমার (রাঃ) মক্কা মুকার্রামা ও মদ্বীনা নববিয়ার মধ্যবর্তী পথে অবস্থিত ছোট ছোট জনপদে জুমুআহ প্রতিষ্ঠিত হতে লক্ষ্য করতেন। তিনি তাতে কোন আপত্তি জানাতেন না। (আব্দুর রাযযাক, মুসান্নাফ)\n\n" +
            "হযরত উমার (রাঃ) বলেন, ‘তোমরা যেখানেই থাক, সেখানেই জুমুআহ পড়।’\n\n" +
            "[আব্দুর রাযযাক, মুসান্নাফ, তামামুল মিন্নাহ্, আলবানী ৩৩২পৃ:]\n\n" +
            "পক্ষান্তরে পাড়া-গ্রামে জুমুআহ হবে না বলে হযরত আলী কর্তৃক যে হাদীস বর্ণনা করা হয়, তা সহীহ নয়।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৬/৩৫২-৩৫৪, ২২/৭৫]\n\n" +
            "কোন অমুসলিম দেশে পড়াশোনা বা চাকরী করতে গিয়ে সেখানে মসজিদ না থাকলে বা যথেষ্ট সংখ্যক মুসলিম না থাকলেও ৩ জনেই যে কোন রুমে জুমুআহ কায়েম হবে।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৫/৮৫]\n\n" +
            "একই বড় গ্রাম বা শহরে বিচ্ছন্নতা সৃষ্টির উদ্দেশ্যে নয়, বরং মসজিদ সংকীর্ণ হওয়ার কারণে, অথবা দূর হওয়ার কারণে, অথবা ফিতনা সৃষ্টি হওয়ার আশঙ্কায় প্রয়োজনে একাধিক মসজিদে জুমুআহ কায়েম করা যায়।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৮/১১১, ১৯/১৬৫-১৬৬]\n\n" +
            "কোন মসজিদে জুমুআহ পড়ার জন্য নির্মাণের সময় ঐ নিয়ত শর্ত নয়। অক্তিযারুপে নির্মাণের পর প্রয়োজনে সেখানে জুমুআহ পড়া যায়।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৮/১১০]",
            "Friday (Jumu'ah) prayer is obligatory upon villagers just as it is obligatory upon city dwellers. Having a Caliph, being a major city, having a grand Jame Mosque, or requiring 40 worshippers is not an essential prerequisite. Rather, wherever a permanent resident Muslim community is present, Jumu'ah is obligatory upon them.\n\n" +
            "[Majallatul Buhuthil Islamiyyah 22/75; Fatawa Islamiyyah, Saudi Scholars Committee 1/424]\n\n" +
            "Narrated by Ibn Abbas (RA): 'The first Friday prayer offered in Islam after the Friday prayer in the Prophet's Mosque (in Madinah) was held at the village of Juwatha in Bahrain.'\n\n" +
            "[Bukhari #892, 4371; Abu Dawud, Sunan #1068]\n\n" +
            "Ibn Umar (RA) used to observe Friday prayers established in small habitations situated along the route between Makkah and Madinah, and he did not raise any objection against it. (Abdur Razzaq, Musannaf)\n\n" +
            "Umar (RA) stated: 'Wherever you are, establish the Friday prayer.'\n\n" +
            "[Abdur Razzaq, Musannaf; Tamam al-Minnah, Albani p. 332]\n\n" +
            "Conversely, the narration attributed to Ali (RA) claiming that Friday prayer cannot be held in villages and rural outskirts is not authentic (Da'if / Ghayr Sahih).\n\n" +
            "[Majallatul Buhuthil Islamiyyah 16/352-354, 22/75]\n\n" +
            "If one travels to a non-Muslim country for study or employment and there is no mosque or large Muslim gathering available, holding Jumu'ah with even three individuals in any room is valid and established.\n\n" +
            "[Majallatul Buhuthil Islamiyyah 15/85]\n\n" +
            "In a large village or city, establishing Friday prayer in multiple mosques is permissible out of necessity—not for causing division, but due to mosque congestion, excessive distance, or fear of civil discord (Fitnah).\n\n" +
            "[Majallatul Buhuthil Islamiyyah 18/111, 19/165-166]\n\n" +
            "Having the intention to establish Jumu'ah at the initial construction of a mosque is not a condition. A regular prayer area built for daily prayers may subsequently be used for holding Jumu'ah whenever the need arises.\n\n" +
            "[Majallatul Buhuthil Islamiyyah 18/110]"
        ));

        // 8. জুম'আ যাদের উপর ফরয নয়
        list.add(new HajjHistoryCardItem(
            8,
            "জুম'আ যাদের উপর ফরয নয়",
            "Those Exempt from Obligatory Jumu'ah",
            "মহিলা, শিশু ও অসুস্থ ব্যক্তি: নবী মুবাশ্শির (ﷺ) বলেন, প্রত্যেক মুসলিমের জন্য জামাআত সহকারে জুমুআহ ফরয। অবশ্য ৪ ব্যক্তির জন্য ফরয নয়; ক্রীতদাস, মহিলা, শিশু ও অসুস্থ। [আবূদাঊদ, সুনান ১০৬৭নং] যে ব্যক্তি (শত্রু, সম্পদ বিনষ্ট, সফরের সঙ্গী ছু...",
            "Women, Children, and the Sick: The Prophet (ﷺ) said: The Friday prayer in congregation is an obligatory duty upon every Muslim, with the exception of four individuals: an enslaved person, a woman, a child, and a sick person...",
            "মহিলা, শিশু ও অসুস্থ ব্যক্তি:\n\n" +
            "নবী মুবাশ্শির (ﷺ) বলেন,\n" +
            "প্রত্যেক মুসলিমের জন্য জামাআত সহকারে জুমুআহ ফরয। অবশ্য ৪ ব্যক্তির জন্য ফরয নয়; ক্রীতদাস, মহিলা, শিশু ও অসুস্থ।\n\n" +
            "[আবূদাঊদ, সুনান ১০৬৭নং]\n\n" +
            "যে ব্যক্তি (শত্রু, সম্পদ বিনষ্ট, সফরের সঙ্গী ছুটে যাওয়ার) ভয়ে, অথবা বৃষ্টি, কাদা বা অত্যন্ত শীত বা গ্রীষ্মের কারণে মসজিদে উপস্থিত হতে অক্ষম। মহানবী (ﷺ) বলেন,\n" +
            "যে ব্যক্তি মুআযযিনের (আযান) শোনে এবং কোন ওজর (ভয় অথবা অসুখ) তাকে জামাআতে উপস্থিত হতে বাধা না দেয়, তাহলে যে নামায সে পড়ে, তার সে নামায কবুল হয় না।\n\n" +
            "[আবূদাঊদ, সুনান ৫৫১নং]\n\n" +
            "একদা ইবনে আব্বাস (রাঃ) এক বৃষ্টিময় জুমআর দিনে তাঁর মুআযযিনকে বললেন, ‘তুমি যখন আশহাদু আন্না মুহাম্মাদুর রাসূলুল্লাহ্’ বলবে তখন বল, ‘তোমরা তোমাদের ঘরে নামায পড়ে নাও।’ এ কথা শুনে লোকেরা যেন আপত্তিকর মনোভাব ব্যক্ত করল। কিন্তু তিনি বললেন, ‘এরুপ তিনি করেছেন যিনি আমার থেকে শ্রেষ্ঠ। অর্থাৎ, আল্লাহর রসূল (ﷺ) এরুপ করেছেন। আর আমিও তোমাদেরকে এই কাদা ও পিছল জায়গার মাঝে বের হওয়াকে অপছন্দ করলাম।’\n\n" +
            "[বুখারী ৯০১, মুসলিম, সহীহ ৬৯৯নং]\n\n" +
            "প্রকাশ থাকে যে, যারা দূরের মাঠে অথবা জঙ্গলে অথবা সমুদ্রে কাজ করে এবং আযান শুনতে পায় না, তাছাড়া কাজ ছেড়ে শহর বা গ্রামে আসাও সম্ভব নয়, তাদের জন্য জুমুআহ ফরয নয়।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪১৪, ফাতাওয়া ইবনে উষাইমীন ১/৩৯৯]\n\n" +
            "মুসাফির:\n\n" +
            "মহানবী (ﷺ) জুমআর দিন সফরে থাকলে, জুমআর নামায না পড়ে যোহরের নামায পড়তেন। বিদায়ী হজ্জের সময় তিনি আরাফাতে অবস্থানকালে জুমআর নামায পড়েননি। বরং যোহ্র ও আসরের নামাযকে অগ্রিম জমা করে পড়েছিলেন। অনুরুপ আমল ছিল খুলাফায়ে রাশেদ্বীন (রাঃ) দেরও।উপর্যুক্ত ব্যক্তিবর্গের জন্য জুমআর নামায ফরয নয়। কিন্তু যোহরের নামায অবশ্যই ফরয। পরন্তু যদি তারাও জুমআর মসজিদে উপস্থিত হয়ে জামাআতে জুমুআহ পড়ে নেয়, তাহলে তা বৈধ। এ ক্ষেত্রে তাদের জুমুআহ হয়ে যাবে এবং যোহরের নামায মাফ হয়ে যাবে। একাধিক হাদীস এ কথা প্রমাণ করে যে, মহানবী (ﷺ) এবং খুলাফায়ে রাশেদ্বীন (রাঃ) দের যুগে মহিলারা জুমুআহ ও জামাআতে উপস্থিত হয়ে নামায আদায় করত। উল্লেখ্য যে, কোন মুসাফির জুমুআহ খুতবা দিলে ও ইমামতি করলে তা শুদ্ধ হয়ে যাবে।\n\n" +
            "[আলমুমতে শারহে ফিক্হ, ইবনে উষাইমীন ৫/২৩]\n\n" +
            "জুমআর জামাআতে কোন মুসাফির যোহরের কসর আদায় করার নিয়ত করতে পারে না। কারণ, যোহ্র অপেক্ষা জুমআর ফযীলত অনেক বেশী।\n\n" +
            "[আলমুমতে শারহে ফিক্হ, ইবনে উষাইমীন ৪/৫৭৪]",
            "Women, Children, and the Sick:\n\n" +
            "The Prophet of glad tidings (ﷺ) said:\n" +
            "\"The Friday (Jumu'ah) prayer in congregation is an obligatory duty upon every Muslim, with the exception of four individuals: an enslaved person, a woman, a child, and a sick person.\"\n\n" +
            "[Abu Dawud, Sunan #1067]\n\n" +
            "A person who is unable to attend the mosque due to fear (of enemies, loss of property, or missing their travel companions), or due to heavy rain, mud, or extreme cold or heat. The Holy Prophet (ﷺ) said:\n" +
            "\"Whoever hears the caller to prayer (Adhan) and has no valid excuse (fear or illness) that prevents him from attending the congregation, then the prayer he offers alone is not accepted.\"\n\n" +
            "[Abu Dawud, Sunan #551]\n\n" +
            "Once, on a rainy Friday, Ibn Abbas (RA) told his Mu'adhdhin: \"When you pronounce 'Ashhadu anna Muhammadan Rasulullah', then announce: 'Pray in your dwellings (homes).'\" Upon hearing this, the people expressed surprise and hesitation. Thereupon he said: \"This was done by one who was far superior to me—meaning the Messenger of Allah (ﷺ). And I disliked making you walk out in this slippery mud and dirt.\"\n\n" +
            "[Bukhari #901, Muslim, Sahih #699]\n\n" +
            "It is worth noting that for those who work in distant fields, forests, or at sea, where they cannot hear the Adhan, and it is unfeasible for them to leave their work and travel to a town or village, Jumu'ah is not obligatory upon them.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/414; Fatawa Ibn Uthaymeen 1/399]\n\n" +
            "Travelers (Musafir):\n\n" +
            "When the Holy Prophet (ﷺ) was traveling on a Friday, he would not offer the Jumu'ah prayer, but rather prayed Dhuhr. During the Farewell Pilgrimage (Hajjat al-Wada'), while staying at Arafah on a Friday, he did not offer Jumu'ah prayer; instead, he combined Dhuhr and Asr in advance. The Rightly Guided Caliphs (RA) followed the same practice. For the aforementioned categories of people, Jumu'ah prayer is not obligatory, but Dhuhr prayer remains mandatory. However, if they attend the mosque and pray Jumu'ah in congregation, it is fully valid for them; their Jumu'ah is accomplished, and the obligation of Dhuhr is lifted. Numerous Hadiths prove that during the era of the Prophet (ﷺ) and the Rightly Guided Caliphs, women used to attend the mosque and participate in Jumu'ah and congregational prayers. Furthermore, if a traveler delivers the Khutbah and leads the Jumu'ah prayer, it is entirely valid and sound.\n\n" +
            "[Al-Mumti' Sharh al-Fiqh, Ibn Uthaymeen 5/23]\n\n" +
            "A traveler joining the Jumu'ah congregation cannot make the intention for shortening (Qasr) Dhuhr, because the virtue and rank of Jumu'ah are far greater than Dhuhr.\n\n" +
            "[Al-Mumti' Sharh al-Fiqh, Ibn Uthaymeen 4/574]"
        ));

        // 9. জুম'আর রাকআত ছুটে গেলে
        list.add(new HajjHistoryCardItem(
            9,
            "জুম'আর রাকআত ছুটে গেলে",
            "What to Do When Missing a Rak'ah of Jumu'ah",
            "কারো জুমআর এক রাকআত ছুটে গেলে বাকী আর এক রাকআত ইমামের সালাম ফিরার পর উঠে পড়ে নিলে তার জুমুআহ হয়ে যাবে। অনুরুপ কেউ দ্বিতীয় রাকআতের রুকূ পেলেও ঐ রাকআত এবং তার সাথে আর এক রাকআত পড়লে তারও জুমুআহ হয়ে যাবে। ...",
            "If a worshipper misses one Rak'ah of Jumu'ah, they can stand up after the Imam finishes with Salam and complete the remaining one Rak'ah, and their Jumu'ah prayer will be valid...",
            "কারো জুমআর এক রাকআত ছুটে গেলে বাকী আর এক রাকআত ইমামের সালাম ফিরার পর উঠে পড়ে নিলে তার জুমুআহ হয়ে যাবে। অনুরুপ কেউ দ্বিতীয় রাকআতের রুকূ পেলেও ঐ রাকআত এবং তার সাথে আর এক রাকআত পড়লে তারও জুমুআহ হয়ে যাবে। কিন্তু যদি কেউ দ্বিতীয় রাকআতের রুকূ থেকে ইমামের মাথা তোলার পর জামাআতে শামিল হয়, তাহলে সে জুমআর নামায পাবে না। এই অবস্থায় তাকে যোহরের ৪ রাকআত আদায়ের নিয়তে জামাআতে শামিল হয়ে ইমামের সালাম ফিরার পর ৪ রাকআত ফরয পড়তে হবে।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪১৮, ৪২১]\n\n" +
            "যেমন জামাআত ছুটে গেলে জুমআও ছুটে যাবে। এ ক্ষেত্রেও একাকী যোহ্র পড়তে হবে। কারণ জামাআত ছাড়া জুমুআহ হয় না।\n\n" +
            "মহানবী (ﷺ) বলেন, “যে ব্যক্তি জুমআর এক রাকআত নামায পায়, সে যেন অপর এক রাকআত পড়ে নেয়।”\n\n" +
            "[ইবনে মাজাহ্, সুনান,হাকেম, মুস্তাদরাক, ইরওয়াউল গালীল, আলবানী ৬২২, জামে ৫৯৯১নং]\n\n" +
            "মহানবী (ﷺ) বলেন, “যে ব্যক্তি এক রাকআত নামায পায়, সে নামায পেয়ে যায়।”\n\n" +
            "[বুখারী ৫৭৯, মুসলিম, সহীহ ৬০৭, তিরমিযী, সুনান ৫২৪নং]\n\n" +
            "এর বিপরীত অর্থ এই দাঁড়ায় যে, “যে ব্যক্তি এক রাকআত নামায পায় না, সে নামায পায় না।” এ জন্যই ইমাম তিরমিযী উক্ত হাদীসের টীকায় বলেছেন, ‘এ হাদীসটি হাসান সহীহ। নবী (ﷺ)-এর অধিকাংশ সাহাবা ও অন্যান্য আহলে ইলমগণ এহাদীসের উপর আমল করেছেন। তাঁরা বলেছেন, “যে ব্যক্তি জুমআর এক রাকআত পেয়ে যায়, সে ব্যক্তি যেন আর এক রাকআত পড়ে নেয়। কিন্তু যে (দ্বিতীয় রাকআতের তাশাহহুদের) বৈঠক অবস্থায় জামাআত পায়, সে যেন যোহরের ৪ রাকআত পড়ে নেয়।” এ মত গ্রহণ করেছেন সুফয়্যান সওরী, ইবনুল মুবারক, শাফেয়ী, আহমাদ এবং ইসহাক (রহঃ)।\n\n" +
            "ইবনে মাসঊদ (রাঃ) বলেন, “যে ব্যক্তি জুমআর এক রাকআত পেয়ে যায়, সে ব্যক্তি যেন আর এক রাকআত পড়ে নেয়। কিন্তু যে (দ্বিতীয় রাকআতের) রুকূ না পায়, সে যেন যোহরের ৪ রাকআত পড়ে নেয়।”\n\n" +
            "[ইবনে আবী শাইবা, ত্বাবারানীরানী, মু’জাম, বায়হাকী, ইরওয়াউল গালীল, আলবানী ৬২১নং]\n\n" +
            "ইবনে উমার (রাঃ) বলেন, “যে ব্যক্তি জুমআর এক রাকআত পেয়ে যায়, সে ব্যক্তি যেন আর এক রাকআত পড়ে নেয়। কিন্তু যে (দ্বিতীয় রাকআতের) তাশাহহুদ পায়, সে যেন যোহরের ৪ রাকআত পড়ে নেয়।”\n\n" +
            "[বায়হাকী, ইরওয়াউল গালীল, আলবানী ৬২১]\n\n" +
            "কোন কোন বর্ণনায় তাশাহহুদ পেলে নামায পেয়ে যাওয়ার কথা বলা হয়েছে। আর তার মানে হল জামাআতের সওয়াব পেয়ে যাওয়া।\n\n" +
            "[ইরওয়াউল গালীল, আলবানী ৩/৮২]\n\n" +
            "প্রকাশ থাকে যে, জুমআর খুতবা না শুনলেও; বরং ১ রাকআত নামায না পেলেও জুমুআহ হয়ে যাবে। যেমন, জুমআর খুতবা দিলে অথবা শুনলেও যদি নামাযের ১ রাকআতও না পায়, তাহলে তাকে যোহ্রই পড়তে হবে।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪১০]\n\n" +
            "উল্লেখ্য যে, জুমআর নামায পড়তে পড়তে যদি কারো ওযূ নষ্ট হয়ে যায় এবং ওযূ করে ফিরে এসে যদি দ্বিতীয় রাকআতের রুকূ পেয়ে যায়, তাহলে সে আর এক রাকআত পড়ে নেবে। নচেৎ সিজদা বা তাশাহহুদ পেলে যোহরের নিয়তে শামিল হয়ে ৪ রাকআত পড়বে। তদনুরুপ জামাআত ছুটে গেলেও যোহ্র পড়বে।\n\n" +
            "অনুরুপ কোন ইমাম সাহেব যদি বিনা ওযূতে জুমুআহ পড়িয়ে নামাযের শেষে মনে হয়, তাহলে মুক্তাদীদের নামায সহীহ হয়ে যাবে। আর ইমাম ঐ নামায কাযা করতে ৪ রাকআত যোহ্র পড়বেন।\n\n" +
            "[আল-মুন্তাকা মিন ফাতাওয়াল ফাওযান ৩/৬৮]",
            "If a worshipper misses one Rak'ah of Jumu'ah, they can stand up after the Imam finishes with Salam and complete the remaining one Rak'ah, and their Jumu'ah prayer will be valid. Similarly, if someone catches the Ruku of the second Rak'ah, they have caught that Rak'ah, and upon offering one more Rak'ah after the Imam's Salam, their Jumu'ah is valid. However, if one joins the congregation after the Imam has risen from the Ruku of the second Rak'ah, they have not caught the Jumu'ah prayer. In this case, they should join the congregation with the intention of offering four Rak'ahs of Dhuhr, and after the Imam's Salam, stand up and complete four Rak'ahs of obligatory Dhuhr prayer.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/418, 421]\n\n" +
            "Just as missing the congregation entirely means missing Jumu'ah, in such a case one must pray Dhuhr individually, because Jumu'ah is not valid without congregation.\n\n" +
            "The Holy Prophet (ﷺ) said: \"Whoever catches one Rak'ah of the Friday prayer, let him add another Rak'ah to it.\"\n\n" +
            "[Ibn Majah, Sunan; Al-Hakim, Mustadrak; Irwa al-Ghalil, Albani #622; Jami #5991]\n\n" +
            "The Holy Prophet (ﷺ) said: \"Whoever catches one Rak'ah of the prayer has indeed caught the prayer.\"\n\n" +
            "[Bukhari #579, Muslim, Sahih #607, Tirmidhi, Sunan #524]\n\n" +
            "The converse implication of this is: \"Whoever does not catch at least one Rak'ah has not caught the prayer.\" For this reason, Imam Tirmidhi noted in his commentary on this Hadith: 'This Hadith is Hasan Sahih. The majority of the Prophet's (ﷺ) Companions and other scholars of knowledge acted upon this Hadith. They held: \"Whoever catches one Rak'ah of Jumu'ah should pray one more Rak'ah. But whoever catches the congregation during the sitting (Tashahhud) of the second Rak'ah must pray four Rak'ahs of Dhuhr.\" This view was adopted by Sufyan ath-Thawri, Ibn al-Mubarak, ash-Shafi'i, Ahmad, and Ishaq (RA).'\n\n" +
            "Ibn Mas'ud (RA) stated: \"Whoever catches one Rak'ah of Jumu'ah should pray one more Rak'ah. But whoever does not catch the Ruku (of the second Rak'ah) must pray four Rak'ahs of Dhuhr.\"\n\n" +
            "[Ibn Abi Shaybah; Tabarani, Mu'jam; Bayhaqi; Irwa al-Ghalil, Albani #621]\n\n" +
            "Ibn Umar (RA) stated: \"Whoever catches one Rak'ah of Jumu'ah should pray one more Rak'ah. But whoever catches only the Tashahhud (of the second Rak'ah) must pray four Rak'ahs of Dhuhr.\"\n\n" +
            "[Bayhaqi; Irwa al-Ghalil, Albani #621]\n\n" +
            "In some narrations where catching the Tashahhud is mentioned as 'catching the prayer', it specifically refers to attaining the reward of the congregation.\n\n" +
            "[Irwa al-Ghalil, Albani 3/82]\n\n" +
            "It is worth noting that even if someone did not hear the Friday Khutbah, as long as they catch at least one Rak'ah of the prayer, their Jumu'ah is valid. Conversely, if someone delivered or listened to the Khutbah but failed to catch even a single Rak'ah of the congregational prayer, they must offer Dhuhr instead.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/410]\n\n" +
            "Additionally, if one's Wudu breaks during the Jumu'ah prayer and after renewing Wudu they return and catch the Ruku of the second Rak'ah, they should complete one more Rak'ah. Otherwise, if they catch the Sujud or Tashahhud, they must join with the intention of Dhuhr and complete four Rak'ahs. Similarly, if one misses the congregation entirely, they must offer Dhuhr.\n\n" +
            "Likewise, if an Imam leads the Jumu'ah prayer without Wudu and only remembers after finishing the prayer, the prayer of the followers (Muqtadis) remains valid, while the Imam must make up for it by offering four Rak'ahs of Dhuhr.\n\n" +
            "[Al-Muntaqa min Fatawa al-Fawzan 3/68]"
        ));

        // 10. জুম'আর দিনের ফযীলত ও বৈশিষ্ট্য
        list.add(new HajjHistoryCardItem(
            10,
            "জুম'আর দিনের ফযীলত ও বৈশিষ্ট্য",
            "Virtues and Distinctions of Friday",
            "জুমুআহ অর্থাৎ জমায়েত বা সমাবেশ ও সম্মেলনের দিন। এটি মুসলিমদের সাপ্তাহিক ঈদ ও বিশেষ ইবাদতের দিন। মহানবী (ﷺ) বলেন, “এই দিন হল ঈদের দিন। আল্লাহ মুসলিমদের জন্য তা নির্বাচিত করেছেন। অতএব যে জুমআয় আসে, সে যেন গোসল করে এ...",
            "Jumu'ah means the day of gathering, congregation, and assembly. It is the weekly Eid and a special day of worship for Muslims. The Holy Prophet (ﷺ) said: \"This day is a day of Eid that Allah has designated for Muslims...",
            "জুমুআহ অর্থাৎ জমায়েত বা সমাবেশ ও সম্মেলনের দিন। এটি মুসলিমদের সাপ্তাহিক ঈদ ও বিশেষ ইবাদতের দিন। মহানবী (ﷺ) বলেন, “এই দিন হল ঈদের দিন। আল্লাহ মুসলিমদের জন্য তা নির্বাচিত করেছেন। অতএব যে জুমআয় আসে, সে যেন গোসল করে এবং খোশবূ থাকলে তা ব্যবহার করে। আর তোমরা দাঁতন করায় অভ্যাসী হও।\n\n" +
            "[ইবনে মাজাহ্, সুনান ১০৯৮নং]\n\n" +
            "জুমআর দিন সবার চাইতে শ্রেষ্ঠ দিন। এমনকি ঈদুল ফিতর ও আযহা থেকেও শ্রেষ্ঠ। এই দিনে আদমকে সৃষ্টি করা হয়েছে এবং বেহেশ্ত দান করা হয়েছে। মহানবী (ﷺ) বলেন,\n" +
            "যার উপর সূর্য উদিত হয়েছে তার মধ্যে সর্বশ্রেষ্ঠ দিন হল জুমআর দিন। এই দিনে আদমকে সৃষ্টি করা হয়েছে, এই দিনে তাঁকে বেহেশ্তে স্থান দেওয়া হয়েছে এবং এই দিনেই তাঁকে বের করে দেওয়া হয়েছে বেহেশ্ত থেকে। (এই দিনেই তাঁর তওবা কবুল করা হয়েছে, তাঁর মৃত্যু হয়েছে এই দিনেই। আর কিয়ামত সংঘটিত হবে এই দিনেই।”\n\n" +
            "[মুসলিম, মালেক, মুঅত্তা, আহমাদ, সহীহ, জামে ৩৩৩৪নং]\n\n" +
            "তিনি বলেন, “জুমআর দিন সকল দিনের সর্দার এবং আল্লাহর নিকট সবার চেয়ে মহান দিন। এমনকি এ দিনটি আল্লাহর নিকট আযহা ও ফিতরের দিন থেকেও শ্রেষ্ঠ। এই দিনে রয়েছে ৫টি বিশেষ বৈশিষ্ট্য; এই দিনে আল্লাহ আদমকে সৃষ্টি করেছেন, এই দিনে তাঁকে পৃথিবীতে অবতারণ করেছেন, এই দিনে তাঁর ইন্তিকাল হয়েছে, এই দিনে এমন একটি মুহূর্ত আছে, যদি কোন মুসলিম বান্দা সে মুহূর্তে আল্লাহর নিকট কোন কিছু বৈধ জিনিস প্রার্থনা করে, তাহলে আল্লাহ তাকে তা দিয়ে থাকেন। এই দিনে কিয়ামত সংঘটিত হবে। আর প্রত্যেক নৈকট্যপ্রাপ্ত ফিরিশ্তা, আকাশ, পৃথিবী, বাতাস, পর্বত, সমুদ্র এই দিনকে ভয় করে।\n\n" +
            "[ইবনে মাজাহ্, সুনান ১০৮৪নং]\n\n" +
            "এই দিনে মহান আল্লাহ জান্নাতে প্রত্যেক সপ্তাহে বেহেশতী বান্দাগণকে দর্শন দেবেন। হযরত আনাস (রাঃ) (ولدينا مزيد) এর ব্যাখ্যায় বলেন, ‘মহান আল্লাহ বেহেশতীদের জন্য প্রত্যেক জুমআর দিন জ্যোতিষ্মান হবেন। এই দিনের আসমানী ফিরিশ্তাবর্গের নিকট নাম হল, ‘য়্যাউমুল মাযীদ। এই দিনে গুনাহ মাফ হয়। হযরত আবু হুরাইরা (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল (ﷺ) বলেছেন, যে ব্যক্তি সুন্দরভাবে ওযু করে জুমআর উদ্দেশ্যে (মসজিদে) উপস্থিত হয়। অতঃপর মনোযোগ সহকারে (খুতবাহ্) শ্রবণ করে ও নীরব থাকে সেই ব্যক্তির ঐ জুমুআহ থেকে দ্বিতীয় জুমুআহর মধ্যবর্তীকালে সংঘটিত এবং অতিরিক্ত তিন দিনের পাপ মাফ করে দেওয়া হয়। আর যে ব্যক্তি (খুতবা চলাকালে) কাঁকর স্পর্শ করে সে অসার (ভুল) কাজ করে।\n\n" +
            "[মুসলিম, সহীহ ৮৫৭ নং আবূদাঊদ, সুনান, তিরমিযী, সুনান, ইবনে মাজাহ্, সুনান]\n\n" +
            "এই দিনে এমন একটি সময় আছে, যাতে দুআ কবুল হয়। মহানবী (ﷺ) বলেন,\n" +
            "জুমআর দিনে এমন একটি (সামান্য) মুহূর্ত আছে, যদি কোন মুসলিম বান্দা নামায পড়া অবস্থায় তা পায় এবং আল্লাহর নিকট কোন মঙ্গল প্রার্থনা করে, তাহলে আল্লাহ তাকে তা দিয়ে থাকেন।\n\n" +
            "[বুখারী ৯৩৫নং, মুসলিম, মিশকাত ১৩৫৭নং]\n\n" +
            "এই মুহূর্তের ব্যাপারে বলা হয়েছে যে, তা হল ইমামের মিম্বরে বসা থেকে নিয়ে নামায শেষ হওয়া পর্যন্ত সময়।\n\n" +
            "[মুসলিম, মিশকাত ১৩৫৮নং]\n\n" +
            "অথবা তা হল আসরের পর যে কোন একটি সময়। অবশ্য এ প্রসঙ্গে আরো অন্য সময়ের কথাও অনেকে বলেছেন।\n\n" +
            "[যাদুল মাআদ, ইবনুল কাইয়েম ১/৩৮৯-৩৯০]\n\n" +
            "এই দিনে দান-খয়রাত করার সওয়াব বেশী। হযরত কা’ব (রাঃ) বলেন, ‘অন্যান্য সকল দিন অপেক্ষা এই দিনে সদকাহ্ করার সওয়াব অধিক।’\n\n" +
            "[যাদুল মাআদ, ইবনুল কাইয়েম]\n\n" +
            "জুমআর ফজরের নামায জামাআত সহকারে পড়ার পৃথক বৈশিষ্ট্য আছে। মহানবী (ﷺ) বলেন, আল্লাহর নিকটে সব চাইতে শ্রেষ্ঠ নামায হল, জুমআর দিন জামাআত সহকারে ফজরের নামায।\n\n" +
            "[সিলসিলাহ সহীহাহ, আলবানী ১৫৬৬নং]\n\n" +
            "এই দিনে বা তার রাতে কেউ মারা গেলে কবরের আযাব থেকে রেহাই পাবে। মহানবী (ﷺ) বলেন, “যে মুসলিম জুমআর দিন অথবা রাতে মারা যায়, আল্লাহ তাকে কবরের ফিতনা থেকে বাচান।\n\n" +
            "[আহমাদ, মুসনাদ, তিরমিযী, সুনান, জামে ৫৭৭৩]",
            "Jumu'ah means the day of gathering, congregation, and assembly. It is the weekly Eid and a special day of worship for Muslims. The Holy Prophet (ﷺ) said: \"This day is a day of Eid that Allah has designated for Muslims. Therefore, whoever comes for Jumu'ah should take a bath and apply perfume if available. And make a regular habit of using the tooth-stick (Miswak).\"\n\n" +
            "[Ibn Majah, Sunan #1098]\n\n" +
            "The day of Friday is the best of all days—superior even to Eid al-Fitr and Eid al-Adha. On this day Adam was created and granted Paradise. The Holy Prophet (ﷺ) said:\n" +
            "\"The best day upon which the sun has risen is Friday; on it Adam was created, on it he was admitted into Paradise, and on it he was expelled from it. (On it his repentance was accepted, on it he passed away, and on it the Hour will be established).\"\n\n" +
            "[Muslim; Malik, Muwatta; Ahmad, Sahih; Jami #3334]\n\n" +
            "He (ﷺ) said: \"Friday is the master of all days and the greatest day in the sight of Allah. It is even greater before Allah than the days of Eid al-Adha and Eid al-Fitr. It has five distinct virtues: on this day Allah created Adam, on this day He sent him down to the earth, on this day Adam passed away, on this day there is a special hour during which no Muslim servant asks Allah for anything permissible except that He grants it to him, and on this day the Hour will be established. Every proximate angel, heaven, earth, wind, mountain, and sea stands in awe of this day.\"\n\n" +
            "[Ibn Majah, Sunan #1084]\n\n" +
            "On this day, Allah Almighty will grant His blessed vision to the dwellers of Paradise each week. Narrated by Anas (RA) in the commentary of the verse (وَلَدَيْنَا مَزِيدٌ): 'Allah Almighty will manifest His glorious divine light to the people of Paradise every Friday. The angels in the heavens name this day 'Yawm al-Majeed' (the Day of Abundance).' Sins are forgiven on this day. Narrated by Abu Hurairah (RA), the Messenger of Allah (ﷺ) said: 'Whoever performs Wudu thoroughly and proceeds to the mosque for Jumu'ah, listens attentively to the Khutbah and remains silent, his sins between that Friday and the following Friday—plus an additional three days—will be forgiven. And whoever touches pebbles (distracts himself) during the Khutbah has engaged in an idle, invalidating act.'\n\n" +
            "[Muslim, Sahih #857; Abu Dawud; Tirmidhi; Ibn Majah]\n\n" +
            "There is an hour on this day in which supplications are answered. The Holy Prophet (ﷺ) said:\n" +
            "\"On Friday there is a particular hour in which no Muslim servant asks Allah for good while standing in prayer, except that Allah grants it to him.\"\n\n" +
            "[Bukhari #935, Muslim, Mishkat #1357]\n\n" +
            "Regarding this hour, it is said that it is the duration from when the Imam sits on the pulpit until the prayer concludes.\n\n" +
            "[Muslim, Mishkat #1358]\n\n" +
            "Alternatively, it is an hour after Asr prayer until sunset. Other times have also been discussed by scholars.\n\n" +
            "[Zad al-Ma'ad, Ibn al-Qayyim 1/389-390]\n\n" +
            "Giving charity on this day carries immense reward. Ka'b (RA) stated: 'Giving charity on Friday is greater in virtue than on all other days.'\n\n" +
            "[Zad al-Ma'ad, Ibn al-Qayyim]\n\n" +
            "Offering the Fajr prayer in congregation on Friday has a unique distinction. The Holy Prophet (ﷺ) said: \"The best prayer in the sight of Allah is the Fajr prayer in congregation on Friday.\"\n\n" +
            "[Silsilah Sahihah, Albani #1566]\n\n" +
            "Whoever passes away on this day or its preceding night will be saved from the trial of the grave. The Prophet (ﷺ) said: \"No Muslim dies on the day or night of Friday except that Allah protects him from the trial and torment of the grave.\"\n\n" +
            "[Ahmad, Musnad; Tirmidhi, Sunan; Jami #5773]"
        ));

        // 11. ঈদের দিন জুম'আ
        list.add(new HajjHistoryCardItem(
            11,
            "ঈদের দিন জুম'আ",
            "When Eid Falls on Friday",
            "ঈদের দিন জুমুআহ পড়লে ইমাম জুমুআহ পড়বেন। সাধারণ মুসলিমদের জন্য এখতিয়ার থাকবে; তারা জুমুআহ পড়তেও পারে, নচেৎ যোহ্র পড়াও বৈধ। আর পূর্বে এ কথা উল্লেখ করা হয়েছে যে, বৃষ্টি-বন্যার কারণে মসজিদে উপস্থিত হতে না পারলে ঘরে যোহ্র প...",
            "When Eid falls on Friday, the Imam will lead the Jumu'ah prayer. The general Muslims have an option; they may attend Jumu'ah, or otherwise offering Dhuhr is permissible. And as mentioned previously, if unable to attend due to rain or floods...",
            "ঈদের দিন জুমুআহ পড়লে ইমাম জুমুআহ পড়বেন। সাধারণ মুসলিমদের জন্য এখতিয়ার থাকবে; তারা জুমুআহ পড়তেও পারে, নচেৎ যোহ্র পড়াও বৈধ।\n\n" +
            "আর পূর্বে এ কথা উল্লেখ করা হয়েছে যে, বৃষ্টি-বন্যার কারণে মসজিদে উপস্থিত হতে না পারলে ঘরে যোহ্র পড়ে নিতে হবে।",
            "When Eid falls on Friday, the Imam will lead the Jumu'ah prayer. The general Muslims have an option (allowance); they may attend and offer the Jumu'ah prayer, or otherwise it is also permissible for them to offer Dhuhr prayer instead.\n\n" +
            "And as mentioned previously, if one is unable to attend the mosque due to heavy rain or floods, they should offer Dhuhr prayer at home."
        ));

        return list;
    }
}
