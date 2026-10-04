package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আর নামায.
 * 100% verbatim text matching screenshots and authentic sources.
 */
public class JumuahNamazContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
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
            "يَا أَيُّهَا الَّذِيْنَ آمَنُوْا إِذَا نُوْدِيَ لِلصَّلاَةِ مِنْ يَّوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَى ذِكْرِ اللهِ وَذَرُوا الْبَيْعَ، ذَلِكُمْ خَيْرٌ لَّكُمْ إِنْ كُنْتُمْ تَعْلَمُوْنَ\n\n" +
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
            "হযরত জাবের বিন আব্দুল্লাহ্ (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, একদা নবী (ﷺ) জুমআর দিন খাড়া হয়ে খুতবা দানকালে বললেন, “সম্ভবত: এমনও লোক আছে, যার নিকট জুমুআহ উপস্থিত হয়; অথচ সে মদ্বীনা থেকে মাত্র এক মাইল দূরে থাকে এবং জুমআয় হাযির হয় না।” দ্বিতীয় বারে তিনি বললেন, “সম্ভবত: এমন লোকও আছে যার নিকট জুমুআহ উপস্থিত হয়; অথচ সে মদ্বীনা থেকে মাত্র দুই মাইল দূরে থাকে এবং জুমআয় হাজির হয় না।” অতঃপর তৃতীয়বারে তিনি বললেন, “সম্ভবত: এমন লোকও আছে যে মদ্বীনা থেকে মাত্র তিন মাইল দূরে থাকে এবং জুমআয় হাজির হয় না তার হৃদয়ে আল্লাহ মোহ্র মেরে দেন।\n\n" +
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

        // 2. জুম'আর দিন দুই আযান কেন হয়
        list.add(new HajjHistoryCardItem(
            2,
            "জুম'আর দিন দুই আযান কেন হয়",
            "Why Two Adhans are Called on Friday",
            "প্রথম দিকে রাসূলুল্লাহ ﷺ এবং হযরত আবু বকর ও উমর (রাযিয়াল্লাহু ‘আনহুমা)–এর সময় শুধু এক আযান দেওয়া হতো, যা খুতবার ঠিক আগে মিম্বরের সামনে দেওয়া হতো। পরে হযরত উসমান (রাযি.)-এর খিলাফতের সময় মদীনায় মানুষের সংখ্যা বৃদ্ধি পায়...",
            "In the early period during the era of the Messenger of Allah (ﷺ), Abu Bakr, and Umar (RA), only one Adhan was called right in front of the Minbar before the Khutbah. Later, during the caliphate of Uthman (RA), as the population of Madinah expanded...",
            "প্রথম দিকে রাসূলুল্লাহ ﷺ এবং হযরত আবু বকর ও উমর (রাযিয়াল্লাহু ‘আনহুমা)–এর সময় শুধু এক আযান দেওয়া হতো, যা খুতবার ঠিক আগে মিম্বরের সামনে দেওয়া হতো।\n\n" +
            "পরে হযরত উসমান (রাযি.)-এর খিলাফতের সময় মদীনায় মানুষের সংখ্যা বৃদ্ধি পায়। ফলে বাজার ও ঘরে থাকা মানুষদের মসজিদের দিকে ডাকার জন্য তিনি একটি অতিরিক্ত আযান চালু করেন, যা মসজিদে নববীর বাইরে “যাউরা” নামে একটি স্থানে দেওয়া হতো। এটা ছিল প্রশাসনিক ও দাওয়াতী প্রয়োজন।\n\n" +
            "عَنْ السَّائِبِ بْنِ يَزِيدَ، قَالَ:\n" +
            "\"كَانَ النِّدَاءُ يَوْمَ الْجُمُعَةِ، أَوَّلَ مَا يَبْدَأُ رَسُولُ اللَّهِ ﷺ بِالْخُطْبَةِ، فَلَمْ يَكُنْ إِلَّا نِدَاءٌ وَاحِدٌ، فَزَادَهُ عُثْمَانُ الثَّالِثَ.\"\n\n" +
            "অর্থ:\n" +
            "সায়িব ইবন ইয়াযীদ (রহ.) বলেন, “রাসূলুল্লাহ ﷺ- এর যুগে এবং আবু বকর ও উমর (রাযি.)-এর সময় জুমার দিন একটি মাত্র আযান হতো, যখন ইমাম খুতবার জন্য মিম্বরে উঠতেন। এরপর হযরত উসমান (রাযি.) তৃতীয় আযান (অর্থাৎ অতিরিক্ত এক আযান) যুক্ত করেন।”\n\n" +
            "হানাফি মাযহাবের অবস্থান:\n" +
            "হানাফি ও অধিকাংশ ফুকাহা হযরত উসমান (রাযি.)–এর এই আযানকে শরঈ দৃষ্টিতে জায়েয ও মুস্তাহাব মনে করেন, কেননা এটা ইজমা দ্বারা অনুমোদিত সাহাবী আমল। পরবর্তীতে তা মুসলিম বিশ্বে সর্বত্র প্রচলিত হয়।\n\n" +
            "বর্তমানে দুই আযান কেন দেওয়া হয়:\n" +
            "প্রথম আযান: খুতবার আগেই দেওয়া হয়, যেন লোকেরা প্রস্তুত হতে পারে।\n" +
            "দ্বিতীয় আযান: খুতবার শুরুতে মিম্বরের সামনে দেওয়া হয়, যেটা মূল আযান।",
            "In the early era during the lifetime of the Messenger of Allah (ﷺ) and the caliphates of Abu Bakr and Umar (RA), only a single Adhan was proclaimed immediately before the Khutbah in front of the Minbar.\n\n" +
            "Later, during the Caliphate of Uthman (RA), the population of Madinah expanded substantially. Consequently, to alert and call the people in the markets and homes toward the mosque, he instituted an additional Adhan which was called from a prominent location outside the Prophet's Mosque known as \"Az-Zawra\". This was an administrative and Da'wah necessity.\n\n" +
            "Narrated by As-Sa'ib ibn Yazid (RA):\n" +
            "\"On Fridays, the Adhan used to be called initially when the Messenger of Allah (ﷺ) sat upon the pulpit (Minbar). There was only one Adhan, but Uthman (RA) added the third call (an additional Adhan).\"\n\n" +
            "Meaning:\n" +
            "As-Sa'ib ibn Yazid (RA) reported: \"During the era of the Messenger of Allah (ﷺ), and the caliphates of Abu Bakr and Umar (RA), there was only a single call to prayer on Friday when the Imam ascended the Minbar for the Khutbah. Later, Uthman (RA) added the third call (meaning an additional Adhan).\"\n\n" +
            "Position of the Hanafi Madhhab:\n" +
            "The Hanafi school and the vast majority of Islamic Jurists consider this Adhan instituted by Uthman (RA) as legally valid (Ja'iz) and highly recommended (Mustahabb), as it is an established Sunnah of the Rightly-Guided Caliphs endorsed by consensus (Ijma) of the Sahabah. Subsequently, it became universally practiced across the Muslim world.\n\n" +
            "Why Two Adhans are Called Today:\n" +
            "First Adhan: Called well before the Khutbah so that people can prepare themselves, perform ablution, and proceed to the mosque.\n" +
            "Second Adhan: Called directly in front of the Minbar at the commencement of the Khutbah, which is the original fundamental Adhan."
        ));

        return list;
    }
}
