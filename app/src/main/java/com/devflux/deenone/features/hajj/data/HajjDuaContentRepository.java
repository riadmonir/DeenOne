package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjDuaContentRepository {

    public static List<HajjHistoryCardItem> getHajjDuaCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. বাড়ি থেকে বের হবার দোয়া #১
        list.add(new HajjHistoryCardItem(
            1,
            "বাড়ি থেকে বের হবার দোয়া #১",
            "Dua Upon Leaving Home #1",
            "আল্লাহর নামে, আল্লাহর ওপর ভরসা করে বের হলাম...",
            "In the name of Allah, I place my trust in Allah...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>بِسْمِ اللَّهِ ، تَوَكَّلْتُ عَلَى اللَّهِ ، وَلا حَوْلَ وَلا قُوَّةَ إِلاَّ بِاللَّهِ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "বিসমিল্লাহি তাওয়াক্কালতু আলাল্লাহি ওয়ালা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "আল্লাহর নামে, আল্লাহর ওপর ভরসা করে বের হলাম। আল্লাহর সাহায্য ছাড়া কোনো উপায় নেই এবং কোনো শক্তি নেই।<br><br>"
            + "<b>ফজিলত ও রেফারেন্স:</b><br>"
            + "নবিজি (ﷺ) বলেন, যে ব্যক্তি ঘর থেকে বের হওয়ার সময় এই দোয়া পাঠ করে, তাকে বলা হয়: তুমি যথেষ্ট নিরাপত্তা লাভ করলে এবং শয়তান তার থেকে দূরে সরে যায়। (আবু দাউদ: ৫০৯৫, তিরমিজি: ৩৪২৬)",
            "<b>Arabic:</b><br>"
            + "بِسْمِ اللَّهِ ، تَوَكَّلْتُ عَلَى اللَّهِ ، وَلا حَوْلَ وَلا قُوَّةَ إِلاَّ بِاللَّهِ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Bismillahi tawakkaltu 'alallah, wa la hawla wa la quwwata illa billah.<br><br>"
            + "<b>Meaning:</b><br>"
            + "In the name of Allah, I place my trust in Allah; there is no might nor power except with Allah.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sunan Abi Dawud: 5095, Jami at-Tirmidhi: 3426"
        ));

        // 2. বাড়ি থেকে বের হবার দোয়া #২
        list.add(new HajjHistoryCardItem(
            2,
            "বাড়ি থেকে বের হবার দোয়া #২",
            "Dua Upon Leaving Home #2",
            "হে আল্লাহ! আমি আপনার নিকট আশ্রয় চাই যেন পথভ্রষ্ট না হই...",
            "O Allah! I seek refuge in You lest I should stray or be led astray...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>اللَّهُمَّ إِنِّي أَعُوذُ بِكَ أَنْ أَضِلَّ، أَوْ أُضَلَّ، أَوْ أَزِلَّ، أَوْ أُزَلَّ، أَوْ أَظْلِمَ، أَوْ أُظْلَمَ، أَوْ أَجْهَلَ، أَوْ يُجْهَلَ عَلَيَّ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহুম্মা ইন্নি আউযুবিকা আন আদিল্লা আও উদাল্লা, আও আযিল্লা আও উযাল্লা, আও আজলিমা আও উজলামা, আও আজহালা আও ইউজহালা আলাইয়্যা।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "হে আল্লাহ! আমি আপনার নিকট আশ্রয় চাই যেন আমি পথভ্রষ্ট না হই কিংবা আমাকে পথভ্রষ্ট করা না হয়, আমি যেন পদস্খলিত না হই কিংবা আমার পদস্খলন ঘটানো না হয়, আমি যেন কারো ওপর জুলুম না করি কিংবা আমার ওপর জুলুম করা না হয়, আমি যেন মূর্খতাসুলভ আচরণ না করি কিংবা আমার সাথে মূর্খতাসুলভ আচরণ করা না হয়।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "আবু দাউদ: ৫০৯৪, তিরমিজি: ৩৪২৭, সুনানে নাসায়ী: ৫৪৮৬",
            "<b>Arabic:</b><br>"
            + "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ أَنْ أَضِلَّ، أَوْ أُضَلَّ، أَوْ أَزِلَّ، أَوْ أُزَلَّ، أَوْ أَظْلِمَ، أَوْ أُظْلَمَ، أَوْ أَجْهَلَ، أَوْ يُجْهَلَ عَلَيَّ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahumma inni a'udhu bika an adilla aw udalla, aw azilla aw uzalla, aw azlima aw uzlama, aw ajhala aw yujhala 'alayya.<br><br>"
            + "<b>Meaning:</b><br>"
            + "O Allah, I seek refuge in You lest I should stray or be led astray, or slip or be made to slip, or oppress or be oppressed, or behave ignorantly or be treated with ignorance.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sunan Abi Dawud: 5094, Jami at-Tirmidhi: 3427"
        ));

        // 3. সফরকারীদের জন্য বাসিন্দাদের দোয়া #১
        list.add(new HajjHistoryCardItem(
            3,
            "সফরকারীদের জন্য বাসিন্দাদের দোয়া #১",
            "Resident's Supplication for the Traveler #1",
            "আমি তোমাদের ধর্ম, বিশ্বস্ততা এবং আল্লাহর প্রতি শেষ আমল সমর্পণ করছি...",
            "I entrust to Allah your religion, your trust, and the last of your deeds...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>أَسْتَوْدِعُ اللَّهَ دِينَكَ، وَأَمَانَتَكَ، وَخَوَاتِيمَ عَمَلِكَ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আস্তাওদিউল্লাহা দীনাকা, ওয়া আমানাতাকা, ওয়া খাওয়াতিমা আমালিক।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "আমি তোমার দ্বীন, তোমার আমানত (ঈমান ও বিশ্বস্ততা) এবং তোমার শেষ আমলসমূহ আল্লাহর সুরক্ষায় সোপর্দ করছি।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "আবু দাউদ: ২৬০০, তিরমিজি: ৩৪৪২, আহমদ: ৪৫৮৩",
            "<b>Arabic:</b><br>"
            + "أَسْتَوْدِعُ اللَّهَ دِينَكَ، وَأَمَانَتَكَ، وَخَوَاتِيمَ عَمَلِكَ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Astawdi'ullaha deenaka, wa amanataka, wa khawateema 'amalik.<br><br>"
            + "<b>Meaning:</b><br>"
            + "I place your religion, your trust, and the conclusion of your deeds in the custody of Allah.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sunan Abi Dawud: 2600, Jami at-Tirmidhi: 3442"
        ));

        // 4. সফরকারীদের জন্য বাসিন্দাদের দোয়া #২
        list.add(new HajjHistoryCardItem(
            4,
            "সফরকারীদের জন্য বাসিন্দাদের দোয়া #২",
            "Resident's Supplication for the Traveler #2",
            "আল্লাহ তোমাকে তাকওয়া(সংযমশীলতার-পাথেয় দান করুন)...",
            "May Allah provide you with Taqwa and forgive your sins...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>زَوَّدَكَ اللَّهُ التَّقْوَى، وَغَفَرَ ذَنْبَكَ، وَيَسَّرَ لَكَ الْخَيْرَ حَيْثُمَا كُنْتَ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "যাওয়্যাদাকাল্লাহুত তাক্বওয়া, ওয়া গাফারা যাম্বাকা, ওয়া ইয়াস্সারা লাকাল খাইরা হাইসুমা কুনতা।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "আল্লাহ তোমাকে তাকওয়া (সংযমশীলতার-পাথেয়) দান করুন, তোমার গুনাহ ক্ষমা করুন এবং তুমি যেখানেই থাকো তোমার জন্য কল্যাণ সহজ করে দিন।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "তিরমিজি: ৩৪৪৪, সুনানে দারেমী: ২৬৮৭",
            "<b>Arabic:</b><br>"
            + "زَوَّدَكَ اللَّهُ التَّقْوَى، وَغَفَرَ ذَنْبَكَ، وَيَسَّرَ لَكَ الْخَيْرَ حَيْثُمَا كُنْتَ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Zawwadakallahut-taqwa, wa ghafara dhanbaka, wa yassara lakal-khayra haythuma kunta.<br><br>"
            + "<b>Meaning:</b><br>"
            + "May Allah endow you with Taqwa, forgive your sins, and facilitate goodness for you wherever you may be.<br><br>"
            + "<b>Reference:</b><br>"
            + "Jami at-Tirmidhi: 3444"
        ));

        // 5. সফরকারীদের জন্য বাসিন্দাদের দোয়া #৩
        list.add(new HajjHistoryCardItem(
            5,
            "সফরকারীদের জন্য বাসিন্দাদের দোয়া #৩",
            "Resident's Supplication for the Traveler #3",
            "হে আল্লাহ! তুমি ওর পথের দূরত্ব গুটিয়ে দিয়ো এবং সফর সহজ করো...",
            "O Allah! Fold up the distance of the journey for him and ease his travel...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>اللَّهُمَّ اطْوِ لَهُ الْبُعْدَ، وَهَوِّنْ عَلَيْهِ السَّفَرَ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহুম্মাতবি লাহুল বু'দা, ওয়া হাওউইন আলাইহিস সাফার।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "হে আল্লাহ! তুমি ওর পথের দূরত্ব গুটিয়ে দিয়ো এবং ওর জন্য এই সফরকে সহজ করে দিয়ো।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "তিরমিজি: ৩৪৪৫, মুসনাদে আহমদ: ৮২৫৩",
            "<b>Arabic:</b><br>"
            + "اللَّهُمَّ اطْوِ لَهُ الْبُعْدَ، وَهَوِّنْ عَلَيْهِ السَّفَرَ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahummat-wi lahul-bu'da, wa hawwin 'alayhis-safar.<br><br>"
            + "<b>Meaning:</b><br>"
            + "O Allah! Shorten the distance for him and ease his travel.<br><br>"
            + "<b>Reference:</b><br>"
            + "Jami at-Tirmidhi: 3445"
        ));

        // 6. বাসিন্দাদের জন্য সফরকারীদের দোয়া
        list.add(new HajjHistoryCardItem(
            6,
            "বাসিন্দাদের জন্য সফরকারীদের দোয়া",
            "Traveler's Supplication for the Resident",
            "আল্লাহর উপর ভরসা করে তোমাকে রাখলাম। যাঁর আমানত কখনো নষ্ট হয় না...",
            "I leave you in the care of Allah, whose trusts are never lost...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>أَسْتَوْدِعُكُمُ اللَّهَ الَّذِي لَا تَضِيعُ وَدَائِعُهُ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আস্তাওদিউকুমুল্লাহাল্লাযী লা তাদীউ ওয়াদাইউহু।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "আল্লাহর উপর ভরসা করে তোমাকে রাখলাম। যাঁর কাছে রক্ষিত কোনো আমানতই কখনো নষ্ট হয় না বা হারিয়ে যায় না।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "ইবনে মাজাহ: ২৮২৫, আহমদ: ৯২০১, সহীহ জামে: ৯৫৭",
            "<b>Arabic:</b><br>"
            + "أَسْتَوْدِعُكُمُ اللَّهَ الَّذِي لَا تَضِيعُ وَدَائِعُهُ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Astawdi'ukumullahalladhee la tadee'u wada-i'uh.<br><br>"
            + "<b>Meaning:</b><br>"
            + "I leave you in the care of Allah, whose deposited trusts are never lost.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sunan Ibn Majah: 2825, Musnad Ahmad: 9201"
        ));

        // 7. সফরের দোয়া
        list.add(new HajjHistoryCardItem(
            7,
            "সফরের দোয়া",
            "Supplication for Journey",
            "আল্লাহ সর্বশক্তিমান, আল্লাহ সর্বশক্তিমান, আল্লাহ সর্বশক্তিমান...",
            "Allah is the Greatest, Allah is the Greatest, Allah is the Greatest...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>اللهُ أَكْبَرُ، اللهُ أَكْبَرُ، اللهُ أَكْبَرُ، سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ، اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى، وَمِنَ الْعَمَلِ مَا تَرْضَى، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَذَا وَاطْوِ عَنَّا بُعْدَهُ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ، وَالْخَلِيفَةُ فِي الأَهْلِ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহু আকবার, আল্লাহু আকবার, আল্লাহু আকবার। সুবহানাল্লাযী সাখখারা লানা হাযা ওয়ামা কুন্না লাহু মুকরিনীন, ওয়া ইন্না ইলা রাব্বিনা লামুনকালিবুন। আল্লাহুম্মা ইন্না নাসআলুকা ফী সাফারিনা হাযাল বিররা ওয়াত তাক্বওয়া, ওয়া মিনাল আমালি মা তারদা। আল্লাহুম্মা হাওউইন আলাইনা সাফারানা হাযা ওয়াতবি আন্না বু'দাহু। আল্লাহুম্মা আনতাস সাহিবু ফিস সাফারি, ওয়াল খালিফাতু ফিল আহল।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "আল্লাহ সর্বশক্তিমান, আল্লাহ সর্বশক্তিমান, আল্লাহ সর্বশক্তিমান। পবিত্র সেই সত্তা যিনি এটিকে আমাদের বশীভূত করে দিয়েছেন, অথচ আমরা এটিকে বশীভূত করতে সমর্থ ছিলাম না। এবং নিশ্চয়ই আমরা আমাদের রবের দিকে প্রত্যাবর্তনকারী। হে আল্লাহ! আমরা আমাদের এই সফরে আপনার নিকট পুণ্য ও তাকওয়া প্রার্থনা করি এবং এমন আমল যা আপনি পছন্দ করেন। হে আল্লাহ! আমাদের এই সফরকে সহজ করে দিন এবং এর দূরত্বকে সংকুচিত করে দিন। হে আল্লাহ! আপনিই সফরে একমাত্র সঙ্গী এবং পরিবার-পরিজনের অভিভাবক।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ মুসলিম: ১৩৪২, আবু দাউদ: ২৫৯৯",
            "<b>Arabic:</b><br>"
            + "اللهُ أَكْبَرُ، اللهُ أَكْبَرُ، اللهُ أَكْبَرُ، سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ...<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahu Akbar, Allahu Akbar, Allahu Akbar. Subhanalladhee sakh-khara lana hadha wa ma kunna lahu muqrineen, wa inna ila Rabbina lamunqaliboon...<br><br>"
            + "<b>Meaning:</b><br>"
            + "Allah is the Greatest! Glory to Him Who has brought this under our control, though we were unable to do so ourselves. And indeed, unto our Lord we shall return...<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih Muslim: 1342"
        ));

        // 8. পশু অথবা যানবাহনে আরোহনের দোয়া
        list.add(new HajjHistoryCardItem(
            8,
            "পশু অথবা যানবাহনে আরোহনের দোয়া",
            "Dua Upon Mounting an Animal or Boarding Vehicle",
            "আল্লাহর নামে শুরু করছি। সমস্ত প্রশংসা আল্লাহর জন্য...",
            "In the name of Allah, all praise is due to Allah...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>بِسْمِ اللهِ، وَالْحَمْدُ للهِ، سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ، الْحَمْدُ للهِ، الْحَمْدُ للهِ، الْحَمْدُ للهِ، اللهُ أَكْبَرُ، اللهُ أَكْبَرُ، اللهُ أَكْبَرُ، سُبْحَانَكَ اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي فَاغْفِرْ لِي، فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "বিসমিল্লাহি, ওয়ালহামদুলিল্লাহ। সুবহানাল্লাযী সাখখারা লানা হাযা ওয়ামা কুন্না লাহু মুকরিনীন, ওয়া ইন্না ইলা রাব্বিনা লামুনকালিবুন। আলহামদুলিল্লাহ, আলহামদুলিল্লাহ, আলহামদুলিল্লাহ। আল্লাহু আকবার, আল্লাহু আকবার, আল্লাহু আকবার। সুবহানাকা আল্লাহুম্মা ইন্নি জালামতু নাফসি ফাগফির লী, ফাইন্নাহু লা ইয়াগফিরুয যুনূবা ইল্লা আনতা।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "আল্লাহর নামে শুরু করছি। সমস্ত প্রশংসা আল্লাহর জন্য। পবিত্র সেই সত্তা যিনি এটিকে আমাদের বশীভূত করেছেন, অন্যথায় আমরা একে নিয়ন্ত্রণে আনতে পারতাম না। আর নিশ্চয়ই আমরা আমাদের প্রতিপালকের নিকট ফিরে যাব। সমস্ত প্রশংসা আল্লাহর, আল্লাহ সর্বশ্রেষ্ঠ। হে আল্লাহ! আপনি পবিত্র, নিশ্চয়ই আমি নিজের ওপর অবিচার করেছি, আমাকে ক্ষমা করুন; কারণ আপনি ছাড়া গুনাহ মাফ করার আর কেউ নেই।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "আবু দাউদ: ২৬০২, তিরমিজি: ৩৪৪৬",
            "<b>Arabic:</b><br>"
            + "بِسْمِ اللهِ، وَالْحَمْدُ للهِ، سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ...<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Bismillahi, wal-hamdulillahi, subhanalladhee sakh-khara lana hadha wa ma kunna lahu muqrineen...<br><br>"
            + "<b>Meaning:</b><br>"
            + "In the name of Allah and all praise is for Allah. How perfect He is, The One Who has placed this under our control...<br><br>"
            + "<b>Reference:</b><br>"
            + "Sunan Abi Dawud: 2602, Jami at-Tirmidhi: 3446"
        ));

        // 9. যাত্রীদের নিচের পথে নামার দোয়া
        list.add(new HajjHistoryCardItem(
            9,
            "যাত্রীদের নিচের পথে নামার দোয়া",
            "Supplication When Descending a Slope / Path",
            "সাক্ষী হিসাবে, আমাদের প্রতি তাঁর করুণা লাভে প্রশংসা করছি...",
            "Praising Allah for His great grace upon us when descending...",
            "<b>আরবি দোয়া ও আমল:</b><br>"
            + "<font color='#10B981'><b>سُبْحَانَ اللَّهِ</b></font><br>"
            + "<i>(এবং নিচু উপত্যকায় নামার সময় পঠিত দোয়া:)</i><br>"
            + "<font color='#10B981'><b>سَمَّعَ سَامِعٌ بِحَمْدِ اللَّهِ وَحُسْنِ بَلَائِهِ عَلَيْنَا، رَبَّنَا صَاحِبْنَا وَأَفْضِلْ عَلَيْنَا، عَائِذًا بِاللَّهِ مِنَ النَّارِ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "সাম্মা'আ সামি'উম বিহামদিল্লাহি ওয়া হুসনি বালা-ইহী আলাইনা, রাব্বানা সাহিবনা ওয়া আফদিল আলাইনা, আইযাম বিল্লাহি মিনান-নার। (জাবের রা. বলেন: আমরা যখন উঁচুতে উঠতাম 'আল্লাহু আকবার' বলতাম এবং যখন নিচে নামতাম 'সুবহানাল্লাহ' বলতাম)।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "সাক্ষী হিসাবে, আমাদের প্রতি তাঁর করুণা লাভে প্রশংসা করছি এবং তাঁর সুন্দর পরীক্ষার জন্য কৃতজ্ঞতা। হে আমাদের প্রতিপালক! আমাদের সাথী হোন এবং আমাদের ওপর অনুগ্রহ করুন; জাহান্নামের আগুন থেকে আল্লাহর নিকট আশ্রয় চাই।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ মুসলিম: ২৭১৮, সহীহ বুখারী: ২৯৯৩",
            "<b>Arabic:</b><br>"
            + "سَمَّعَ سَامِعٌ بِحَمْدِ اللَّهِ وَحُسْنِ بَلَائِهِ عَلَيْنَا، رَبَّنَا صَاحِبْنَا وَأَفْضِلْ عَلَيْنَا، عَائِذًا بِاللَّهِ مِنَ النَّارِ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Samma'a sami'um bihamdillahi wa husni bala-ihee 'alayna, Rabbana sahibna wa afdil 'alayna, 'a-idhan billahi minan-nar.<br><br>"
            + "<b>Meaning:</b><br>"
            + "May a listener hear our praise of Allah and His fine trial upon us. Our Lord, accompany us and favor us, seeking refuge in Allah from the Fire.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih Muslim: 2718, Sahih al-Bukhari: 2993"
        ));

        // 10. তালবিয়া
        list.add(new HajjHistoryCardItem(
            10,
            "তালবিয়া",
            "The Talbiyah",
            "উপস্থিত, হে আল্লাহ্! আমি উপস্থিত; আমি উপস্থিত...",
            "Here I am, O Allah, here I am! Here I am, You have no partner...",
            "<b>আরবি তালবিয়া:</b><br>"
            + "<font color='#10B981'><b>لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لاَ شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لاَ شَرِيكَ لَكَ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "লাব্বাইক আল্লাহুম্মা লাব্বাইক, লাব্বাইকা লা শারীকা লাকা লাব্বাইক, ইন্নাল হামদা ওয়ান নি'মাতা লাকা ওয়াল মুলক, লা শারীকা লাক।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "উপস্থিত, হে আল্লাহ্! আমি উপস্থিত; আমি উপস্থিত, আপনার কোনো অংশীদার নেই, আমি উপস্থিত। নিশ্চয়ই সমুদয় প্রশংসা, সকল নিয়ামত এবং সমগ্র রাজত্ব কেবল আপনারই; আপনার কোনো অংশীদার নেই।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ বুখারী: ১৫৪৯, সহীহ মুসলিম: ১১৮৪",
            "<b>Arabic:</b><br>"
            + "لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لاَ شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لاَ شَرِيكَ لَكَ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Labbayk Allahumma Labbayk, Labbayka la shareeka laka Labbayk, innal-hamda wan-ni'mata laka wal-mulk, la shareeka lak.<br><br>"
            + "<b>Meaning:</b><br>"
            + "Here I am, O Allah, here I am. Here I am, You have no partner, here I am. Verily all praise and blessings are Yours, and all sovereignty, You have no partner.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih al-Bukhari: 1549, Sahih Muslim: 1184"
        ));

        // 11. শহরে প্রবেশের দোয়া
        list.add(new HajjHistoryCardItem(
            11,
            "শহরে প্রবেশের দোয়া",
            "Dua Upon Entering a Town or City (Makkah / Madinah)",
            "হে আল্লাহ আপনি সাত আসমানের এবং তার নিচের সবকিছুর রব...",
            "O Allah, Lord of the seven heavens and all they overshadow...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>اللَّهُمَّ رَبَّ السَّمَاوَاتِ السَّبْعِ وَمَا أَظْلَلْنَ، وَرَبَّ الأَرَضِينَ السَّبْعِ وَمَا أَقْلَلْنَ، وَرَبَّ الشَّيَاطِينِ وَمَا أَضْلَلْنَ، وَرَبَّ الرِّيَاحِ وَمَا ذَرَيْنَ، فَإِنَّا نَسْأَلُكَ خَيْرَ هَذِهِ الْقَرْيَةِ وَخَيْرَ أَهْلِهَا، وَنَعُوذُ بِكَ مِنْ شَرِّهَا وَشَرِّ أَهْلِهَا وَشَرِّ مَا فِيهَا</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহুম্মা রাব্বাস সামাওয়াতিস সাব'ই ওয়ামা আজলালনা, ওয়া রাব্বাল আরাদ্বীনাস সাব'ই ওয়ামা আক্বলালনা, ওয়া রাব্বাশ শায়াত্বীনি ওয়ামা আদলালনা, ওয়া রাব্বার রিয়াহি ওয়ামা যারাইনা, ফা-ইন্না নাসআলুকা খাইরা হাযিহিল কারয়াতি ওয়া খাইরা আহলিহা, ওয়া নাউযুবিকা মিন শাররিহা ওয়া শাররি আহলিহা ওয়া শাররি মা ফীহা।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "হে আল্লাহ আপনি সাত আসমানের এবং তার নিচে যা কিছু ছায়া দেয় তার রব; সাত জমিনের এবং যা কিছু তা বহন করে তার রব; শয়তানদের এবং যাদেরকে তারা পথভ্রষ্ট করে তাদের রব; বায়ুমণ্ডলের এবং যা কিছু তা উড়িয়ে নেয় তার রব। আমরা আপনার নিকট এই জনপদের কল্যাণ, এর বাসিন্দাদের কল্যাণ ও এর ভেতরের সকল কল্যাণ প্রার্থনা করি; এবং এর অনিষ্ট, এর বাসিন্দাদের অনিষ্ট ও এর মধ্যস্থিত যাবতীয় অনিষ্ট থেকে আপনার আশ্রয় চাই।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "মুস্তাদরাকে হাকেম: ১৯৮৪, সুনানে নাসায়ী কুবরা: ৮৭৭৫, হিসনুল মুসলিম",
            "<b>Arabic:</b><br>"
            + "اللَّهُمَّ رَبَّ السَّمَاوَاتِ السَّبْعِ وَمَا أَظْلَلْنَ، وَرَبَّ الأَرَضِينَ السَّبْعِ وَمَا أَقْلَلْنَ...<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahumma Rabbas-samawatis-sab'i wa ma azlalna, wa Rabbal-aradeenas-sab'i wa ma aqlalna...<br><br>"
            + "<b>Meaning:</b><br>"
            + "O Allah, Lord of the seven heavens and all they overshadow, Lord of the seven earths and all they carry... We ask You for the good of this town and the good of its people...<br><br>"
            + "<b>Reference:</b><br>"
            + "Al-Mustadrak: 1984, Sunan an-Nasa'i: 8775"
        ));

        // 12. হজরে আসওয়াদকে চুমু দিয়ে বা ইশারা করে তওয়াফ শুরু করার দোয়া
        list.add(new HajjHistoryCardItem(
            12,
            "হজরে আসওয়াদকে চুমু দিয়ে বা ইশারা করে তওয়াফ শুরু করার দোয়া",
            "Dua When Touching or Gesturing Towards the Black Stone (Istilam)",
            "শুরু করছি আল্লাহর নামে, আল্লাহ সবার চেয়ে বড়...",
            "In the name of Allah, Allah is the Greatest...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>بِسْمِ اللَّهِ وَاللَّهُ أَكْبَرُ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "বিসমিল্লাহি ওয়াল্লাহু আকবার।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "শুরু করছি আল্লাহর নামে, আল্লাহ সবার চেয়ে বড়।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ বুখারী: ১৫৯৭, সহীহ মুসলিম: ১২৬৪",
            "<b>Arabic:</b><br>"
            + "بِسْمِ اللَّهِ وَاللَّهُ أَكْبَرُ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Bismillahi wallahu Akbar.<br><br>"
            + "<b>Meaning:</b><br>"
            + "In the name of Allah, Allah is the Greatest.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih al-Bukhari: 1597, Sahih Muslim: 1264"
        ));

        // 13. রোকনে ইয়ামানী এবং হজরে আসওয়াদের মধ্যবর্তী স্থানে পড়ার দোয়া
        list.add(new HajjHistoryCardItem(
            13,
            "রোকনে ইয়ামানী এবং হজরে আসওয়াদের মধ্যবর্তী স্থানে পড়ার দোয়া",
            "Dua Between the Yemeni Corner and the Black Stone",
            "হে রব আমাদের দুনিয়ায় শান্তি দাও এবং আখেরাতে কল্যাণ দাও...",
            "Our Lord! Grant us good in this world and good in the Hereafter...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "রাব্বানা আতিনা ফিদ-দুনয়া হাসানাতাও ওয়া ফিল-আখিরাতি হাসানাতাও ওয়া ক্বিনা আযাবান-নার।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "হে রব আমাদের দুনিয়ায় শান্তি দাও এবং আখেরাতেও কল্যাণ দান করো এবং আমাদের জাহান্নামের আগুন থেকে রক্ষা করো।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সূরা আল-বাকারা: ২০১, আবু দাউদ: ১৮৯২, মুসনাদে আহমদ: ১৫৩৯৪",
            "<b>Arabic:</b><br>"
            + "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.<br><br>"
            + "<b>Meaning:</b><br>"
            + "Our Lord! Grant us good in this world and good in the Hereafter, and protect us from the torment of the Fire.<br><br>"
            + "<b>Reference:</b><br>"
            + "Surah Al-Baqarah: 201, Sunan Abi Dawud: 1892"
        ));

        // 14. যমযমের পানি পান করার দু'আ
        list.add(new HajjHistoryCardItem(
            14,
            "যমযমের পানি পান করার দু'আ",
            "Supplication When Drinking Zamzam Water",
            "হে আল্লাহ! নিশ্চয়ই আমি আপনার নিকট উপকারী জ্ঞান ও সুস্থতা চাই...",
            "O Allah! I ask You for beneficial knowledge, abundant sustenance...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا وَاسِعًا، وَشِفَاءً مِنْ كُلِّ دَاءٍ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহুম্মা ইন্নি আসআলুকা ইলমান নাফি'আ, ওয়া রিজকান ওয়াসি'আ, ওয়া শিফা-আম মিন কুল্লি দা-ইন।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "হে আল্লাহ! নিশ্চয়ই আমি আপনার নিকট উপকারী জ্ঞান, প্রশস্ত হালাল জীবিকা এবং সমস্ত রোগ-ব্যাধি থেকে পূর্ণাঙ্গ নিরাময় ও আরোগ্য প্রার্থনা করছি।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "মুস্তাদরাকে হাকেম: ১৭৩৯, সুনানে দারাকুতনী: ২৭৩৮",
            "<b>Arabic:</b><br>"
            + "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا وَاسِعًا، وَشِفَاءً مِنْ كُلِّ دَاءٍ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahumma inni as-aluka 'ilman nafi'an, wa rizqan wasi'an, wa shifa-an min kulli da-in.<br><br>"
            + "<b>Meaning:</b><br>"
            + "O Allah! I ask You for beneficial knowledge, abundant sustenance, and a cure from all diseases.<br><br>"
            + "<b>Reference:</b><br>"
            + "Mustadrak al-Hakim: 1739, Sunan al-Daraqutni: 2738"
        ));

        // 15. সাফা এবং মারওয়া পাহাড়ে আরোহনের দোয়া
        list.add(new HajjHistoryCardItem(
            15,
            "সাফা এবং মারওয়া পাহাড়ে আরোহনের দোয়া",
            "Supplication When Ascending Mount Safa and Marwah",
            "নিশ্চয়ই সাফা ও মারওয়া আল্লাহর নিদর্শনসমূহের অন্তর্ভুক্ত...",
            "Indeed, Safa and Marwah are among the symbols of Allah...",
            "<b>আরবি আয়াত ও যিকির:</b><br>"
            + "<font color='#10B981'><b>إِنَّ الصَّفَا وَالْمَرْوَةَ مِنْ شَعَائِرِ اللَّهِ، أَبْدَأُ بِمَا بَدَأَ اللَّهُ بِهِ</b></font><br><br>"
            + "<i>(অতঃপর সাফা ও মারওয়ায় ক্বিবলামুখী হয়ে হাত তুলে ৩ বার পাঠ করা:)</i><br>"
            + "<font color='#10B981'><b>اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ، أَنْجَزَ وَعْدَهُ، وَنَصَرَ عَبْدَهُ، وَهَزَمَ الْأَحْزَابَ وَحْدَهُ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "ইন্নাস সাফা ওয়াল মারওয়াতা মিন শাআইরিল্লাহ, আবদাউ বিমা বাদাআল্লাহু বিহ। আল্লাহু আকবার, আল্লাহু আকবার, আল্লাহু আকবার। লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহ, লাহুল মুলকু ওয়া লাহুল হামদু, ওয়া হুওয়া আলা কুল্লি শাইয়িন ক্বাদীর। লা ইলাহা ইল্লাল্লাহু ওয়াহদাহ, আনজাযা ওয়াদাহ, ওয়া নাসারা আবদাহ, ওয়া হাযামাল আহযাবা ওয়াহদাহ।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "নিশ্চয়ই সাফা ও মারওয়া আল্লাহর অন্যতম নিদর্শন। আল্লাহ যা দিয়ে শুরু করেছেন আমিও তা দিয়ে শুরু করছি। আল্লাহ সর্বশ্রেষ্ঠ, আল্লাহ সর্বশ্রেষ্ঠ, আল্লাহ সর্বশ্রেষ্ঠ। একমাত্র আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই, তাঁর কোনো অংশীদার নেই, রাজত্ব একমাত্র তাঁরই, সমস্ত প্রশংসা তাঁরই এবং তিনি সবকিছুর ওপর ক্ষমতাবান। একমাত্র আল্লাহ ছাড়া সত্য কোনো উপাস্য নেই, তিনি তাঁর প্রতিশ্রুতি পূর্ণ করেছেন, তাঁর বান্দাকে সাহায্য করেছেন এবং তিনি একাই শত্রুবাহিনীকে পরাভূত করেছেন।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ মুসলিম: ১২১৮, সুনানে নাসায়ী: ২৯৭৪",
            "<b>Arabic:</b><br>"
            + "إِنَّ الصَّفَا وَالْمَرْوَةَ مِنْ شَعَائِرِ اللَّهِ، أَبْدَأُ بِمَا بَدَأَ اللَّهُ بِهِ...<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Innas-Safa wal-Marwata min sha'a-irillah, abda-u bima bada-Allahu bih... Allahu Akbar, Allahu Akbar, Allahu Akbar...<br><br>"
            + "<b>Meaning:</b><br>"
            + "Indeed, Safa and Marwah are among the symbols of Allah... There is no deity except Allah alone without partner...<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih Muslim: 1218, Sunan an-Nasa'i: 2974"
        ));

        // 16. আরাফার দিনের দোয়া
        list.add(new HajjHistoryCardItem(
            16,
            "আরাফার দিনের দোয়া",
            "Supplication on the Day of Arafah",
            "নেই কোন উপাস্য এক আল্লাহ ব্যতীত। তাঁর শরীক নেই...",
            "There is no deity worthy of worship except Allah alone with no partner...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহ, লাহুল মুলকু ওয়া লাহুল হামদু, ওয়া হুওয়া আলা কুল্লি শাইয়িন ক্বাদীর।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "নেই কোন উপাস্য এক আল্লাহ ব্যতীত। তাঁর কোনো শরিক নেই। রাজত্ব একমাত্র তাঁরই এবং সমস্ত প্রশংসা একমাত্র তাঁরই প্রাপ্য, আর তিনি সবকিছুর ওপর সর্বশক্তিমান ও ক্ষমতাবান।<br><br>"
            + "<b>ফজিলত ও তাৎপর্য:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) ইরশাদ করেছেন: 'সর্বোত্তম দোয়া হলো আরাফাত দিবসের দোয়া এবং আমি ও আমার পূর্ববর্তী নবীগণের শ্রেষ্ঠ বাণী হলো এটি।' (তিরমিজি: ৩৫৮৫)<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "জামে তিরমিজি: ৩৫৮৫, মুসনাদে আহমদ: ৬৯৬১",
            "<b>Arabic:</b><br>"
            + "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "La ilaha illallahu wahdahu la shareeka lah, lahul-mulku wa lahul-hamdu, wa huwa 'ala kulli shay-in qadeer.<br><br>"
            + "<b>Meaning:</b><br>"
            + "There is no deity worthy of worship except Allah alone, without partner; to Him belongs all sovereignty and praise, and He is over all things capable.<br><br>"
            + "<b>Reference:</b><br>"
            + "Jami at-Tirmidhi: 3585"
        ));

        // 17. আল-মাশআর আল-হারামের সন্নিকটে দোয়া করা
        list.add(new HajjHistoryCardItem(
            17,
            "আল-মাশআর আল-হারামের সন্নিকটে দোয়া করা",
            "Supplication Near Al-Mash'ar Al-Haram (Muzdalifah)",
            "নেই কোন উপাস্য এক আল্লাহ ব্যতীত। তাঁর শরীক নেই...",
            "Standing at Muzdalifah facing the Qiblah, praising and glorifying Allah...",
            "<b>আরবি দোয়া ও যিকির:</b><br>"
            + "<font color='#10B981'><b>اللَّهُ أَكْبَرُ، وَلَا إِلَهَ إِلَّا اللَّهُ، وَالْحَمْدُ لِلَّهِ، وَاللَّهُ أَكْبَرُ كَبِيرًا</b></font><br><br>"
            + "<i>(এবং কুরআনে বর্ণিত মুজদালিফার দোয়া:)</i><br>"
            + "<font color='#10B981'><b>فَإِذَا أَفَضْتُم مِّنْ عَرَفَاتٍ فَاذْكُرُوا اللَّهَ عِندَ الْمَشْعَرِ الْحَرَامِ ۖ وَاذْكُرُوهُ كَمَا هَدَاكُمْ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহু আকবার, ওয়া লা ইলাহা ইল্লাল্লাহু, ওয়াল হামদুলিল্লাহ, ওয়াল্লাহু আকবার কাবিরা। ফাইজ্বা আফাদতুম মিন আরাফাতিন ফাযকুরুল্লাহা ইনদাল মাশআরিল হারাম, ওয়াযকুরূহু কামা হাদাকুম।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "নেই কোন উপাস্য এক আল্লাহ ব্যতীত। তাঁর কোনো অংশীদার নেই। আল্লাহ সর্বশ্রেষ্ঠ এবং সকল প্রশংসা তাঁরই। অতঃপর তোমরা যখন আরাফাত থেকে প্রত্যাবর্তন করবে, তখন মাশআরে হারামের নিকট আল্লাহকে স্মরণ করো এবং তাঁকে স্মরণ করো যেভাবে তিনি তোমাদের পথ প্রদর্শন করেছেন।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সূরা আল-বাকারা: ১৯৮, সহীহ মুসলিম: ১২১৮",
            "<b>Arabic:</b><br>"
            + "اللَّهُ أَكْبَرُ، وَلَا إِلَهَ إِلَّا اللَّهُ، وَالْحَمْدُ لِلَّهِ...<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahu Akbar, wa la ilaha illallahu, wal-hamdulillahi, wallahu Akbaru kabeera...<br><br>"
            + "<b>Meaning:</b><br>"
            + "Allah is the Greatest, there is no deity except Allah, and all praise is for Allah... Remember Allah at al-Mash'ar al-Haram...<br><br>"
            + "<b>Reference:</b><br>"
            + "Surah Al-Baqarah: 198, Sahih Muslim: 1218"
        ));

        // 18. জামরাতে কঙ্কর নিক্ষেপের দোয়া
        list.add(new HajjHistoryCardItem(
            18,
            "জামরাতে কঙ্কর নিক্ষেপের দোয়া",
            "Supplication When Throwing Pebbles at Jamarat",
            "আল্লাহ সর্বশ্রেষ্ঠ...",
            "Allah is the Greatest (with each thrown pebble)...",
            "<b>আরবি তাকবীর:</b><br>"
            + "<font color='#10B981'><b>بِسْمِ اللَّهِ ، وَاللَّهُ أَكْبَرُ</b></font><br><br>"
            + "<i>(কিংবা পূর্ণাঙ্গ মাসনূন যিকির:)</i><br>"
            + "<font color='#10B981'><b>اللَّهُ أَكْبَرُ ، رَغْمًا لِلشَّيْطَانِ وَرِضًا لِلرَّحْمَنِ ، اللَّهُمَّ اجْعَلْهُ حَجًّا مَبْرُورًا وَذَنْبًا مَغْفُورًا</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "বিসমিল্লাহি, ওয়াল্লাহু আকবার। (আল্লাহু আকবার, রাগমান লিশ-শাইতানি ওয়া রিদান লির-রাহমান, আল্লাহুম্মাজ'আলহু হাজ্জাম মাবরূরাঁও ওয়া যামবাম মাগফূরা)।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "আল্লাহর নামে শুরু করছি, আল্লাহ সর্বশ্রেষ্ঠ। শয়তানকে অপদস্থ করার জন্য এবং পরম দয়াময় আল্লাহর সন্তুষ্টির উদ্দেশ্যে; হে আল্লাহ! এই হজকে কবুল হজ এবং সকল গুনাহর মার্জনা বানিয়ে দিন।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ বুখারী: ১৭৫১, সহীহ মুসলিম: ১২৯৯, মুসান্নাফে ইবনে আবি শায়বা: ১২৫৩৫",
            "<b>Arabic:</b><br>"
            + "بِسْمِ اللَّهِ ، وَاللَّهُ أَكْبَرُ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Bismillahi wallahu Akbar.<br><br>"
            + "<b>Meaning:</b><br>"
            + "In the name of Allah, Allah is the Greatest.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih al-Bukhari: 1751, Sahih Muslim: 1299"
        ));

        // 19. জবাই করার দোয়া
        list.add(new HajjHistoryCardItem(
            19,
            "জবাই করার দোয়া",
            "Supplication When Slaughtering the Sacrificial Animal",
            "শুরু করছি আল্লাহর নামে, এবং আল্লাহ সবার চেয়ে বড়...",
            "In the name of Allah, and Allah is the Greatest...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>بِسْمِ اللَّهِ وَاللَّهُ أَكْبَرُ، اللَّهُمَّ هَذَا مِنْكَ وَلَكَ، اللَّهُمَّ تَقَبَّلْ مِنِّي</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "বিসমিল্লাহি ওয়াল্লাহু আকবার, আল্লাহুম্মা হাযা মিনকা ওয়া লাকা, আল্লাহুম্মা তাকাব্বাল মিন্নী।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "শুরু করছি আল্লাহর নামে, এবং আল্লাহ সবার চেয়ে বড়। হে আল্লাহ! এটি আপনারই পক্ষ থেকে এবং আপনারই সন্তুষ্টির উদ্দেশ্যে নিবেদিত। হে আল্লাহ! আপনি আমার পক্ষ থেকে তা কবুল করে নিন।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ মুসলিম: ১৯৬০, আবু দাউদ: ২৭৯৫, আহমদ: ১৪৫৩৮",
            "<b>Arabic:</b><br>"
            + "بِسْمِ اللَّهِ وَاللَّهُ أَكْبَرُ، اللَّهُمَّ هَذَا مِنْكَ وَلَكَ، اللَّهُمَّ تَقَبَّلْ مِنِّي<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Bismillahi wallahu Akbar, Allahumma hadha minka wa lak, Allahumma taqabbal minnee.<br><br>"
            + "<b>Meaning:</b><br>"
            + "In the name of Allah, and Allah is the Greatest. O Allah, this is from You and for You. O Allah, accept it from me.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih Muslim: 1960, Sunan Abi Dawud: 2795"
        ));

        // 20. ৯ই যিলহজ্ব এর দোয়া (তাকবীরে তাশরীক)
        list.add(new HajjHistoryCardItem(
            20,
            "৯ই যিলহজ্ব এর দোয়া (তাকবীরে তাশরীক)",
            "Takbeer-e-Tashreeq (9th to 13th Dhul Hijjah)",
            "আল্লাহ মহান, আল্লাহ মহান, আল্লাহ ব্যতীত কোনো সত্য উপাস্য নেই...",
            "Allah is the Greatest, Allah is the Greatest, there is no deity except Allah...",
            "<b>আরবি তাকবীর:</b><br>"
            + "<font color='#10B981'><b>اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ، لَا إِلَهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ، وَلِلَّهِ الْحَمْدُ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহু আকবার আল্লাহু আকবার, লা ইলাহা ইল্লাল্লাহু, ওয়াল্লাহু আকবার আল্লাহু আকবার, ওয়া লিল্লাহিল হামদ।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "আল্লাহ সর্বশ্রেষ্ঠ, আল্লাহ সর্বশ্রেষ্ঠ; আল্লাহ ব্যতীত সত্য কোনো উপাস্য নেই। এবং আল্লাহ সর্বশ্রেষ্ঠ, আল্লাহ সর্বশ্রেষ্ঠ; সমস্ত প্রশংসা কেবলই আল্লাহর জন্য।<br><br>"
            + "<b>মাসআলা:</b><br>"
            + "৯ই জিলহজ ফজর নামাজ থেকে শুরু করে ১৩ই জিলহজ আসর নামাজ পর্যন্ত মোট ২৩ ওয়াক্ত ফরজ নামাজের পর প্রত্যেক প্রাপ্তবয়স্ক মুসলিমের ওপর এই তাকবীর অন্তত একবার পাঠ করা ওয়াজিব।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "মুসান্নাফে ইবনে আবি শায়বা: ৫৬৯৭, সুনানে দারাকুতনী: ১৭৫৭",
            "<b>Arabic:</b><br>"
            + "اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ، لَا إِلَهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ، وَلِلَّهِ الْحَمْدُ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahu Akbar, Allahu Akbar, la ilaha illallahu, wallahu Akbar, Allahu Akbar, wa lillahil-hamd.<br><br>"
            + "<b>Meaning:</b><br>"
            + "Allah is the Greatest, Allah is the Greatest. There is no deity except Allah. And Allah is the Greatest, Allah is the Greatest, and for Allah is all praise.<br><br>"
            + "<b>Reference:</b><br>"
            + "Musannaf Ibn Abi Shaybah: 5697, Sunan al-Daraqutni: 1757"
        ));

        // 21. বাইতুল্লাহ থেকে বিদায় নেয়ার সময় দোয়া
        list.add(new HajjHistoryCardItem(
            21,
            "বাইতুল্লাহ থেকে বিদায় নেয়ার সময় দোয়া",
            "Supplication When Bidding Farewell to the Kaaba",
            "হে আল্লাহ! এটি যেন আপনার পবিত্র ঘরের সাথে আমার শেষ সাক্ষাৎ না হয়...",
            "O Allah! Let this not be the final visit to Your Sacred House...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>اللَّهُمَّ لَا تَجْعَلْ هَذَا آخِرَ الْعَهْدِ بِبَيْتِكَ الْحَرَامِ، وَإِنْ جَعَلْتَهُ فَاعْوِضْنِي عَنْهُ الْجَنَّةَ بِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহুম্মা লা তাজ'আল হাযা আখিরাল আহদি বি-বাইতিকাল হারাম, ওয়া ইন জা'আলতাহু ফা-আ'বিদনী আনহুল জান্নাতা বি-রাহমাতিকা ইয়া আরহামার রাহিমীন।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "হে আল্লাহ! আপনার সম্মানিত পবিত্র ঘরের সাথে এটিই যেন আমার জীবনের শেষ সাক্ষাৎ বা বিদায় না হয়। আর যদি তা-ই আমার শেষ সাক্ষাৎ বানিয়ে থাকেন, তবে এর বিনিময়ে আপনার অপার রহমতে আমাকে জান্নাত নসিব করুন, হে পরম দয়ালু মেহেরবান রব!<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সুনানে বায়হাকী: ৫/১৬৪, আল-মুগনী লি-ইবনে কুদামা: ৩/৪০০",
            "<b>Arabic:</b><br>"
            + "اللَّهُمَّ لَا تَجْعَلْ هَذَا آخِرَ الْعَهْدِ بِبَيْتِكَ الْحَرَامِ، وَإِنْ جَعَلْتَهُ فَاعْوِضْنِي عَنْهُ الْجَنَّةَ بِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahumma la taj'al hadha akhiral-'ahdi bibaytikal-Haram, wa in ja'altahu fa-a'widnee 'anhul-Jannata birahmatika ya Arhamar-Rahimeen.<br><br>"
            + "<b>Meaning:</b><br>"
            + "O Allah! Let this not be the last pledge with Your Sacred House, and if You have decreed it so, then grant me Paradise in its place by Your mercy, O Most Merciful of the merciful!<br><br>"
            + "<b>Reference:</b><br>"
            + "Sunan al-Bayhaqi: 5/164, Al-Mughni: 3/400"
        ));

        // 22. হাজরে আসওয়াদ এবং মাকামে ইবরাহীমের মাঝে পঠিত দোয়া।
        list.add(new HajjHistoryCardItem(
            22,
            "হাজরে আসওয়াদ এবং মাকামে ইবরাহীমের মাঝে পঠিত দোয়া।",
            "Dua Between the Black Stone and Maqam Ibrahim",
            "হে প্রভু! তুমি যে রিযিক (জীবিকা) আমাকে দান করেছ তাতে তুষ্ট রাখো...",
            "O Allah! Make me content with what You have provided me...",
            "<b>আরবি দোয়া:</b><br>"
            + "<font color='#10B981'><b>اللَّهُمَّ قَنِّعْنِي بِمَا رَزَقْتَنِي، وَبَارِكْ لِي فِيهِ، وَاخْلُفْ عَلَى كُلِّ غَائِبَةٍ لِي بِخَيْرٍ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আল্লাহুম্মা কান্নি'নী বিমা রাযাকতানী, ওয়া বারিক লী ফীহি, ওয়াখলুফ আলা কুল্লি গায়িবাতিন লী বি-খাইর।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "হে প্রভু! তুমি যে রিযিক (জীবিকা) আমাকে দান করেছ তাতে আমাকে তুষ্ট রাখো, এবং তাতে আমার জন্য বরকত দান করো; আর আমার অনুপস্থিত যাবতীয় বিষয়ে কল্যাণের সাথে তুমিই উত্তম অভিভাবক ও প্রতিপালক হও।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "মুস্তাদরাকে হাকেম: ১৮৭৮, মুসান্নাফে ইবনে আবি শায়বা: ১৩২৬১",
            "<b>Arabic:</b><br>"
            + "اللَّهُمَّ قَنِّعْنِي بِمَا رَزَقْتَنِي، وَبَارِكْ لِي فِيهِ، وَاخْلُفْ عَلَى كُلِّ غَائِبَةٍ لِي بِخَيْرٍ<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "Allahumma qanni'nee bima razaqtanee, wa barik lee feehi, wakhluf 'ala kulli gha-ibatin lee bi-khayr.<br><br>"
            + "<b>Meaning:</b><br>"
            + "O Lord! Make me content with the provision You have granted me, bless me in it, and take care of everything I leave behind with goodness.<br><br>"
            + "<b>Reference:</b><br>"
            + "Mustadrak al-Hakim: 1878, Musannaf Ibn Abi Shaybah: 13261"
        ));

        // 23. রাসুল সাঃ এর রওজা পাকের সামনে দাঁড়িয়ে সালাম বলা।
        list.add(new HajjHistoryCardItem(
            23,
            "রাসুল সাঃ এর রওজা পাকের সামনে দাঁড়িয়ে সালাম বলা।",
            "Salutations in Front of the Prophet's (PBUH) Sacred Rawdah",
            "হে আল্লাহর রসূল! আপনার উপর সালাম। হে আল্লাহর নবি! আপনার উপর সালাম...",
            "Peace be upon you, O Messenger of Allah! Peace be upon you, O Prophet of Allah...",
            "<b>আরবি সালাম ও দরূদ:</b><br>"
            + "<font color='#10B981'><b>السَّلَامُ عَلَيْكَ يَا رَسُولَ اللَّهِ، السَّلَامُ عَلَيْكَ يَا نَبِيَّ اللَّهِ، السَّلَامُ عَلَيْكَ يَا خِيَرَةَ اللَّهِ مِنْ خَلْقِهِ، السَّلَامُ عَلَيْكَ يَا سَيِّدَ الْمُرْسَلِينَ وَإِمَامَ الْمُتَّقِينَ، أَشْهَدُ أَنَّكَ قَدْ بَلَّغْتَ الرِّسَالَةَ، وَأَدَّيْتَ الأَمَانَةَ، وَنَصَحْتَ الأُمَّةَ، وَجَاهَدْتَ فِي اللَّهِ حَقَّ جِهَادِهِ، فَجَزَاكَ اللَّهُ عَنَّا أَفْضَلَ مَا جَزَى نَبِيًّا عَنْ أُمَّتِهِ</b></font><br><br>"
            + "<b>উচ্চারণ:</b><br>"
            + "আস-সালামু আলাইকা ইয়া রাসূলাল্লাহ, আস-সালামু আলাইকা ইয়া নাবিয়্যাল্লাহ, আস-সালামু আলাইকা ইয়া খিয়ারাতাল্লাহি মিন খালকিহ, আস-সালামু আলাইকা ইয়া সাইয়্যিদাল মুরসালীন ওয়া ইমামাল মুত্তাক্বীন। আশহাদু আন্নাকা ক্বাদ বাল্লাগতার রিসালাহ, ওয়া আদ্দাইতাল আমানাহ, ওয়া নাসাহতাল উম্মাহ, ওয়া জাহাদতা ফিল্লাহি হাক্কা জিহাদিহ, ফাযাযাকাল্লাহু আন্না আফদালা মা যাযা নাবিয়্যান আন উম্মাতিহ।<br><br>"
            + "<b>বাংলা অর্থ:</b><br>"
            + "হে আল্লাহর রসূল! আপনার ওপর সালাম। হে আল্লাহর নবি! আপনার ওপর সালাম। হে আল্লাহর সৃষ্টির সর্বশ্রেষ্ঠ ও মনোনীত ব্যক্তিত্ব! আপনার ওপর সালাম। হে সকল রসূলগণের সর্দার এবং মুত্তাকীদের ইমাম! আপনার ওপর শান্তি বর্ষিত হোক। আমি সাক্ষ্য দিচ্ছি যে আপনি রিসালাতের দায়িত্ব যথাযথ পৌঁছে দিয়েছেন, আমানত আদায় করেছেন, উম্মাহর সর্বাধিক কল্যাণ কামনা করেছেন এবং আল্লাহর পথে সর্বোত্তম সংগ্রাম করেছেন। আল্লাহ আমাদের পক্ষ থেকে আপনাকে এমন শ্রেষ্ঠ প্রতিদান দান করুন, যা তিনি কোনো নবিকে তাঁর উম্মতের পক্ষ থেকে দান করেছেন।<br><br>"
            + "<b>রেফারেন্স:</b><br>"
            + "সহীহ মুসলিম: ৯৬৯, সুনানে বায়হাকী: ৫/২৪৫, মুসান্নাফে আব্দুর রাযযাক: ৬৭২২, ইমাম নববীর কিতাবুল আজকার",
            "<b>Arabic:</b><br>"
            + "السَّلَامُ عَلَيْكَ يَا رَسُولَ اللَّهِ، السَّلَامُ عَلَيْكَ يَا نَبِيَّ اللَّهِ، السَّلَامُ عَلَيْكَ يَا خِيَرَةَ اللَّهِ مِنْ خَلْقِهِ...<br><br>"
            + "<b>Pronunciation:</b><br>"
            + "As-salamu 'alayka ya Rasoolallah, as-salamu 'alayka ya Nabiyyallah, as-salamu 'alayka ya khiyaratallahi min khalqih...<br><br>"
            + "<b>Meaning:</b><br>"
            + "Peace be upon you, O Messenger of Allah! Peace be upon you, O Prophet of Allah! Peace be upon you, O chosen of Allah among His creation... May Allah reward you on our behalf with the best reward given to any Prophet from his nation.<br><br>"
            + "<b>Reference:</b><br>"
            + "Sahih Muslim: 969, Sunan al-Bayhaqi: 5/245"
        ));

        return list;
    }
}
