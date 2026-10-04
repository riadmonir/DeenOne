package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjHolyPlacesContentRepository {

    public static List<HajjHistoryCardItem> getHolyPlaceCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. পবিত্র কাবা
        list.add(new HajjHistoryCardItem(
            1,
            "পবিত্র কাবা",
            "The Holy Kaaba",
            "১৪১৭ হিজরীতে বাদশাহ ফাহাদ ইবনে আব্দুল আ...",
            "In 1417 AH, King Fahd bin Abdulaziz completed the comprehensive renovation...",
            "<b>ঐতিহাসিক পটভূমি ও পরিচয়:</b><br>"
            + "পবিত্র কাবা হলো বায়তুল্লাহ বা আল্লাহর ঘর, যা সমগ্র বিশ্বের মুসলমানদের কিবলা এবং পৃথিবীর সর্বপ্রথম ইবাদতখানা।<br><br>"
            + "<b>সংস্কার ও নির্মাণ:</b><br>"
            + "১৪১৭ হিজরীতে বাদশাহ ফাহাদ ইবনে আব্দুল আজিজের আমলে পবিত্র কাবার সামগ্রিক আধুনিক সংস্কার ও পুনর্নির্মাণ সম্পন্ন হয়। কাবার বর্তমান উচ্চতা প্রায় ১৩.১০ মিটার (৩৯ ফুট ৯ ইঞ্চি), দৈর্ঘ্য প্রায় ১২.৮৬ মিটার এবং প্রস্থ প্রায় ১১.০৩ মিটার।<br><br>"
            + "<b>চারটি কোণ (রুকন):</b><br>"
            + "১. রুকনে আসওয়াদ (পূর্ব-দক্ষিণ কোণ)<br>"
            + "২. রুকনে শামী (উত্তর-পশ্চিম কোণ)<br>"
            + "৩. রুকনে ইরাকী (উত্তর-পূর্ব কোণ)<br>"
            + "৪. রুকনে ইয়ামানী (দক্ষিণ-পশ্চিম কোণ)।<br><br>"
            + "<b>কুরআন রেফারেন্স:</b><br>"
            + "إِنَّ أَوَّلَ بَيْتٍ وُضِعَ لِلنَّاسِ لَلَّذِي بِبَكَّةَ مُبَارَكًا وَهُدًى لِّلْعَالَمِينَ<br>"
            + "<i>\"নিশ্চয় মানবজাতির জন্য সর্বপ্রথম যে ঘরটি প্রতিষ্ঠিত হয়েছিল, তা তো বাক্কায় (মক্কায়), যা বরকতময় ও সমগ্র বিশ্ববাসীর জন্য দিশারি।\"</i> (সূরা আলে ইমরান: ৯৬)",
            "<b>Overview:</b><br>"
            + "The Holy Kaaba (Baytullah) is the supreme Qiblah for all Muslims and the first sanctuary built on earth.<br><br>"
            + "<b>Renovation:</b><br>"
            + "In 1417 AH, King Fahd bin Abdulaziz oversaw a massive historical renovation. The Kaaba stands approximately 13.10 meters high, with base dimensions of 12.86m x 11.03m.<br><br>"
            + "<b>The 4 Corners:</b><br>"
            + "1. Rukn al-Aswad (South-East)<br>"
            + "2. Rukn al-Shami (North-West)<br>"
            + "3. Rukn al-Iraqi (North-East)<br>"
            + "4. Rukn al-Yamani (South-West).<br><br>"
            + "<b>Quran Reference:</b><br>"
            + "Surah Ali 'Imran: 96"
        ));

        // 2. হাজরে আসওয়াদ বা কালো পাথর
        list.add(new HajjHistoryCardItem(
            2,
            "হাজরে আসওয়াদ বা কালো পাথর",
            "The Black Stone (Hajr al-Aswad)",
            "পবিত্র কাবার দক্ষিণ কোণে, জমিন থেকে ১.১০ ...",
            "Situated in the south-eastern corner of the Kaaba, 1.10 meters above ground...",
            "<b>অবস্থান ও পরিচিতি:</b><br>"
            + "পবিত্র কাবার দক্ষিণ-পূর্ব কোণে, জমিন থেকে প্রায় ১.১০ মিটার উঁচুতে খাঁটি রূপার ফ্রেমে বাঁধানো বেহেশতি পাথর হলো হাজরে আসওয়াদ।<br><br>"
            + "<b>মহাত্ম্য ও হাদিস:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) ইরশাদ করেছেন:<br>"
            + "<font color='#10B981'><b>نَزَلَ الحَجَرُ الأَسْوَدُ مِنَ الجَنَّةِ، وَهُوَ أَشَدُّ بَيَاضًا مِنَ اللَّبَنِ، فَسَوَّدَتْهُ خَطَايَا بَنِي آدَمَ</b></font><br>"
            + "<i>\"হাজরে আসওয়াদ জান্নাত থেকে অবতীর্ণ হয়েছিল, তখন তা দুধের চেয়েও বেশি সাদা ছিল; কিন্তু আদম সন্তানের পাপরাশি একে কালো বানিয়ে দিয়েছে।\"</i> (জামে তিরমিজি: ৮৭৭)<br><br>"
            + "<b>তাওয়াফের বিধান:</b><br>"
            + "তাওয়াফ এই হাজরে আসওয়াদ থেকেই শুরু এবং এখানেই শেষ হয়। একে স্পর্শ করা বা চুম্বন করা সম্ভব না হলে হাত দিয়ে ইশারা (ইসতিলাম) করা সুন্নত।",
            "<b>Location & Significance:</b><br>"
            + "Located in the southeastern corner of the Kaaba, set in a pure silver casing approximately 1.10 meters above the floor.<br><br>"
            + "<b>Prophetic Hadith:</b><br>"
            + "The Prophet (ﷺ) said: 'The Black Stone came down from Paradise whiter than milk, but the sins of the children of Adam made it black.' (Jami at-Tirmidhi: 877)<br><br>"
            + "<b>Rites:</b><br>"
            + "Tawaf starts and concludes at the Black Stone. Touching or gesturing towards it (Istilam) is an established Sunnah."
        ));

        // 3. রুকনে য়ামানি
        list.add(new HajjHistoryCardItem(
            3,
            "রুকনে য়ামানি",
            "Rukn al-Yamani (Yemeni Corner)",
            "কাবা শরীফের দক্ষিণ-পশ্চিম কোণ। তাওয়াফের ...",
            "The southwestern corner of the Holy Kaaba facing towards Yemen...",
            "<b>অবস্থান ও পরিচিতি:</b><br>"
            + "কাবা শরীফের দক্ষিণ-পশ্চিম কোণ। এটি ইয়েমেন দেশের অভিমুখে অবস্থিত হওয়ায় একে রুকনে ইয়ামানি বলা হয়।<br><br>"
            + "<b>সুন্নাত আমল ও বিধান:</b><br>"
            + "তাওয়াফের সময় এই কোণ অতিক্রমকালে একে উভয় হাত বা ডান হাত দিয়ে স্পর্শ করা (ইসতিলাম) সুন্নত। তবে একে চুম্বন করা বা সেজদা করা যাবে না। ভিড়ের কারণে স্পর্শ করা সম্ভব না হলে ইশারা না করে সাধারণ হেঁটে যাওয়া সুন্নাত।<br><br>"
            + "<b>বিশেষ দোয়া:</b><br>"
            + "রুকনে ইয়ামানি থেকে হাজরে আসওয়াদ পর্যন্ত এই বরকতময় স্থানে মহানবী (ﷺ) পাঠ করতেন:<br>"
            + "<font color='#10B981'><b>رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ</b></font><br>"
            + "<i>\"হে আমাদের রব! আমাদের দুনিয়ায় শান্তি ও কল্যাণ দিন এবং আখেরাতেও শান্তি ও কল্যাণ দান করুন এবং আমাদের জাহান্নামের আগুন থেকে রক্ষা করুন।\"</i> (আবু দাউদ: ১৮৯২)",
            "<b>Overview:</b><br>"
            + "The southwestern corner of the Holy Kaaba facing towards Yemen.<br><br>"
            + "<b>Rites:</b><br>"
            + "Touching it with the right hand or both hands without kissing during Tawaf is a Sunnah.<br><br>"
            + "<b>Dua:</b><br>"
            + "Recite 'Rabbana atina fid-dunya hasanatan...' between Rukn Yamani and the Black Stone (Sunan Abi Dawud: 1892)."
        ));

        // 4. মুলতাযাম
        list.add(new HajjHistoryCardItem(
            4,
            "মুলতাযাম",
            "Al-Multazam",
            "হাজরে আসওয়াদ থেকে কাবা শরীফের দরজা প...",
            "The section of the Kaaba wall between the Black Stone and the door...",
            "<b>অবস্থান ও অর্থ:</b><br>"
            + "হাজরে আসওয়াদ থেকে কাবা শরীফের দরজা পর্যন্ত প্রায় দুই মিটার মধ্যবর্তী দেয়ালের অংশকে 'মুলতাযাম' বলা হয়। 'মুলতাযাম' শব্দের অর্থ আঁকড়ে ধরার বা জড়িয়ে ধরার স্থান।<br><br>"
            + "<b>ফজিলত ও আমল:</b><br>"
            + "সাহাবায়ে কেরাম ও তাবেয়ীগণ এই দেয়ালের সাথে বুক, গাল ও দুই হাত প্রসারিত করে পরম কান্নাকাটি সহকারে দোয়া করতেন।<br><br>"
            + "<b>দোয়া কবুলের নিশ্চয়তা:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) ইরশাদ করেছেন:<br>"
            + "<font color='#10B981'><b>المُلْتَزَمُ مَوْضِعٌ يُسْتَجَابُ فِيهِ الدُّعَاءُ، مَا دَعَا اللَّهَ فِيهِ عَبْدٌ بِدَعْوَةٍ إِلَّا اسْتَجَابَهَا</b></font><br>"
            + "<i>\"মুলতাযাম এমন একটি স্থান যেখানে দোয়া কবুল হয়; কোনো বান্দা সেখানে আল্লাহর কাছে যে দোয়াই করুক না কেন, আল্লাহ তা কবুল করেন।\"</i> (মুসনাদে বাযযার, বাইহাকী: ৫/১৬৪)",
            "<b>Location & Meaning:</b><br>"
            + "The space between the Black Stone and the door of the Kaaba, roughly two meters wide. 'Multazam' means the place of clinging.<br><br>"
            + "<b>Virtue:</b><br>"
            + "It is one of the premier stations for answered supplication where the Prophet (ﷺ) and Sahabah placed their chest, face, and forearms in weeping prayer."
        ));

        // 5. মাকামে ইব্রাহীম
        list.add(new HajjHistoryCardItem(
            5,
            "মাকামে ইব্রাহীম",
            "Maqam Ibrahim (Station of Abraham)",
            "মাকাম শব্দের আভিধানিক অর্থ, দণ্ডায়মান ব্যক্তি...",
            "Linguistically means the standing place of a person; the miraculous stone...",
            "<b>আভিধানিক অর্থ ও পরিচয়:</b><br>"
            + "মাকাম শব্দের আভিধানিক অর্থ, দণ্ডায়মান ব্যক্তির পা রাখার স্থান। এটি একটি অলৌকিক জান্নাতী পাথর যার ওপর দাঁড়িয়ে হযরত ইব্রাহিম (আলাইহিস সালাম) কাবা শরীফ পুনর্নির্মাণ করেছিলেন।<br><br>"
            + "<b>অলৌকিক বৈশিষ্ট্য:</b><br>"
            + "কাবার দেয়াল উঁচু করার সময় আল্লাহর কুদরতে এই পাথরটি ইব্রাহিম (আ.)-কে নিয়ে স্বয়ংক্রিয়ভাবে উপরে উঠত এবং নিচে নামত। এতে হযরত ইব্রাহিম (আ.)-এর পবিত্র দুই পদচিহ্ন চিরস্থায়ীভাবে মুদ্রিত হয়ে আছে।<br><br>"
            + "<b>বর্তমান রূপ ও বিধান:</b><br>"
            + "বর্তমানে এটি কাবার পূর্ব পাশে একটি সুরভিত সোনালী কাঁচের আধারে সংরক্ষিত রয়েছে। তাওয়াফের ৭ চক্কর শেষ করে এর পেছনে দুই রাকাত নামাজ আদায় করা ওয়াজিব/সুন্নাত।<br><br>"
            + "<b>কুরআন রেফারেন্স:</b><br>"
            + "وَاتَّخِذُوا مِن مَّقَامِ إِبْرَاهِيمَ مُصَلًّى<br>"
            + "<i>\"এবং তোমরা মাকামে ইব্রাহিমকে নামাজের স্থান হিসেবে গ্রহণ করো।\"</i> (সূরা আল-বাকারা: ১২৫)",
            "<b>Meaning & Miraculous Stone:</b><br>"
            + "The stone upon which Prophet Ibrahim (AS) stood while building the upper portions of the Kaaba.<br><br>"
            + "<b>Miracle:</b><br>"
            + "The stone softened and elevated under his feet by Allah's command, permanently preserving his footprints.<br><br>"
            + "<b>Quran Reference:</b><br>"
            + "Surah Al-Baqarah: 125 ('And take the Station of Abraham as a place of prayer.')"
        ));

        // 6. মাতাফ
        list.add(new HajjHistoryCardItem(
            6,
            "মাতাফ",
            "The Mataf (Tawaf Area)",
            "কাবা শরীফের চার পাশে উন্মুক্ত জায়গাকে মাতা...",
            "The open courtyard surrounding the Holy Kaaba dedicated to Tawaf...",
            "<b>পরিচয় ও সীমানা:</b><br>"
            + "কাবা শরীফের চার পাশে উন্মুক্ত জায়গাকে মাতাফ বলা হয়, যেখানে হাজী ও ওমরাহকারীগণ পরম ভক্তিভরে সাত চক্কর তাওয়াফ সম্পন্ন করেন।<br><br>"
            + "<b>থার্মাল মার্বেল মেঝের বৈশিষ্ট্য:</b><br>"
            + "মাতাফের মেঝেতে গ্রিস থেকে সংগৃহীত বিশেষ 'থাচোস' শুভ্র মার্বেল পাথর বসানো রয়েছে। এই পাথর রাতের শীতলতাকে ধরে রাখে এবং গ্রীষ্মের প্রচণ্ড রৌদ্রেও প্রাকৃতিকভাবে মেঝেকে সম্পূর্ণ ঠাণ্ডা ও আরামদায়ক রাখে।<br><br>"
            + "<b>আধুনিক সম্প্রসারণ:</b><br>"
            + "বর্তমানে মাতাফে গ্রাউন্ড ফ্লোর ছাড়াও শীতাতপ নিয়ন্ত্রিত ১ম তলা, ২য় তলা এবং উন্মুক্ত ছাদসহ বহুতল তাওয়াফ ব্রিজ বিদ্যমান, যা একসাথে লক্ষ লক্ষ হাজীকে নিরাপদে তাওয়াফের সুযোগ করে দেয়।",
            "<b>Overview:</b><br>"
            + "The open courtyard directly encircling the Kaaba where pilgrims perform the seven circuits of Tawaf.<br><br>"
            + "<b>Thassos Marble Flooring:</b><br>"
            + "Paved with rare Greek Thassos white marble that naturally absorbs moisture and stays cool under extreme desert sun.<br><br>"
            + "<b>Expansion:</b><br>"
            + "Features multi-tiered indoor and outdoor circumambulation levels accommodating millions simultaneously."
        ));

        // 7. সাফা
        list.add(new HajjHistoryCardItem(
            7,
            "সাফা",
            "Mount Safa",
            "কাবা শরীফ থেকে দক্ষিণ-পূর্ব দিকে, ১৩০ মিটার ...",
            "Located approximately 130 meters southeast of the Holy Kaaba...",
            "<b>অবস্থান ও ঐতিহাসিক গুরুত্ব:</b><br>"
            + "কাবা শরীফ থেকে দক্ষিণ-পূর্ব দিকে, প্রায় ১৩০ মিটার দূরে অবস্থিত একটি ঐতিহাসিক বরকতময় পাহাড়।<br><br>"
            + "<b>সাঈর সূচনা বিন্দু:</b><br>"
            + "হজ ও ওমরাহর সাঈ সর্বদা সাফা পাহাড় থেকেই শুরু করতে হয়। হযরত হাজেরা (আলাইহাস সালাম) তাঁর শিশুপুত্র হযরত ইসমাঈল (আ.)-এর জন্য পানির খোঁজে ব্যাকুল হয়ে সর্বপ্রথম এই পাহাড়ে উঠেছিলেন।<br><br>"
            + "<b>মাসনূন দোয়া ও কিবলামুখী মোনাজাত:</b><br>"
            + "সাফায় উঠে কিবলামুখী হয়ে কাবা দেখে হাত তুলে তাকবীর, তাহলীল ও ৩ বার দোয়া পাঠ করা সুন্নত:<br>"
            + "<font color='#10B981'><b>إِنَّ الصَّفَا وَالْمَرْوَةَ مِنْ شَعَائِرِ اللَّهِ، أَبْدَأُ بِمَا بَدَأَ اللَّهُ بِهِ</b></font><br><br>"
            + "<b>কুরআন রেফারেন্স:</b><br>"
            + "সূরা আল-বাকারা: ১৫৮",
            "<b>Location & Significance:</b><br>"
            + "Situated roughly 130 meters southeast of the Kaaba. The starting mountain of Sa'i.<br><br>"
            + "<b>History:</b><br>"
            + "Hajar (AS) climbed Safa searching for water for infant Ismail (AS).<br><br>"
            + "<b>Quran Reference:</b><br>"
            + "Surah Al-Baqarah: 158 ('Indeed, Safa and Marwah are among the symbols of Allah.')"
        ));

        // 8. মারওয়া
        list.add(new HajjHistoryCardItem(
            8,
            "মারওয়া",
            "Mount Marwah",
            "শক্ত সাদা পাথরের ছোট্ট একটি পাহাড়। পবিত্র কা...",
            "A small hill of smooth white stone located 300 meters northeast of Kaaba...",
            "<b>অবস্থান ও পরিচিতি:</b><br>"
            + "শক্ত সাদা পাথরের ছোট্ট একটি পাহাড়। পবিত্র কাবা শরীফ থেকে উত্তর-পূর্ব দিকে প্রায় ৩০০ মিটার দূরে এটি অবস্থিত।<br><br>"
            + "<b>সাঈ সমাপ্তির স্থান:</b><br>"
            + "সাফা থেকে শুরু করে মারওয়ায় এসে ১ম চক্কর এবং এভাবে ৭ম চক্করটি মারওয়া পাহাড়ে এসেই সমাপ্ত হয়।<br><br>"
            + "<b>ইহরাম সমাপ্তি (হালাল হওয়া):</b><br>"
            + "সাঈ সমাপ্ত হওয়ার পর মারওয়া পাহাড়ের সন্নিকটেই পুরুষদের পুরো মাথা মুণ্ডন (হলক) অথবা সব চুল সমানভাবে ছোট (কসর) করতে হয় এবং মহিলাদের চুলের অগ্রভাগ থেকে এক ইঞ্চি পরিমাণ চুল কেটে ইহরাম শেষ করতে হয়।",
            "<b>Overview:</b><br>"
            + "Located approximately 300 meters northeast of the Kaaba, composed of smooth light-colored stone.<br><br>"
            + "<b>Completion of Sa'i:</b><br>"
            + "The 7th and final lap of Sa'i finishes at Mount Marwah.<br><br>"
            + "<b>Exiting Ihram (Halal):</b><br>"
            + "Pilgrims shave (Halq) or trim (Taqseer) their hair at Marwah to exit the state of Ihram."
        ));

        // 9. মাস'আ
        list.add(new HajjHistoryCardItem(
            9,
            "মাস'আ",
            "The Mas'a (Sa'i Gallery)",
            "সাফা ও মারওয়ার মধ্যবর্তী স্থানকে মাস'আ বলা হ...",
            "The long covered corridor between Mount Safa and Mount Marwah...",
            "<b>সংজ্ঞা ও আয়তন:</b><br>"
            + "সাফা ও মারওয়ার মধ্যবর্তী স্থানকে 'মাস'আ' বলা হয়, যেখানে সাঈ (দ্রুত পদচারণা) আদায় করা হয়।<br><br>"
            + "<b>দূরত্ব ও চক্কর:</b><br>"
            + "সাফা থেকে মারওয়ার একমুখী দূরত্ব প্রায় ৪৫০ মিটার (বা দুই প্রান্তের মধ্যবর্তী নিট দূরত্ব ৩৫০ মিটার)। ৭ চক্করে সর্বমোট দূরত্ব দাঁড়ায় প্রায় ৩.১৫ কিলোমিটার।<br><br>"
            + "<b>সবুজ বাতি (মাইলানি আখদারাইন):</b><br>"
            + "মাস'আর মাঝে সিলিংয়ে সবুজ বাতি দিয়ে চিহ্নিত একটি স্থান রয়েছে। হযরত হাজেরা (আ.) যখন গভীর উপত্যকায় নেমেছিলেন তখন তিনি দ্রুত দৌড়েছিলেন। তাই এই সবুজ বাতি অংশের মধ্যে পুরুষদের জন্য দ্রুত কদমে দৌড়ে অতিক্রম করা সুন্নাত।",
            "<b>Definition:</b><br>"
            + "The vast enclosed air-conditioned gallery spanning between Mount Safa and Mount Marwah.<br><br>"
            + "<b>Distance:</b><br>"
            + "Each lap is approximately 450 meters; 7 laps total approximately 3.15 kilometers.<br><br>"
            + "<b>Green Lights (Maylayn Akhdarayn):</b><br>"
            + "Illuminated zone where men briskly jog in emulation of mother Hajar's hurried search."
        ));

        // 10. মসজিদুল হারাম
        list.add(new HajjHistoryCardItem(
            10,
            "মসজিদুল হারাম",
            "Al-Masjid Al-Haram (The Sacred Mosque)",
            "কাবা শরীফ, ও তার চার পাশের মাতাফ, মাতাফে...",
            "The grand sanctuary enclosing the Kaaba, Mataf, and Mas'a...",
            "<b>মর্যাদা ও পরিচিতি:</b><br>"
            + "পবিত্র কাবা শরীফ, ও তার চার পাশের মাতাফ, মাতাফ সংলগ্ন সাফা-মারওয়া এবং সমগ্র পরিবেষ্টিত মসজিদ মিলেই তৈরি হয়েছে পৃথিবীর সর্বশ্রেষ্ঠ পবিত্র স্থান মসজিদুল হারাম।<br><br>"
            + "<b>এক লক্ষ গুণ সওয়াব:</b><br>"
            + "রাসূলুল্লাহ (ﷺ) ইরশাদ করেছেন:<br>"
            + "<font color='#10B981'><b>صَلَاةٌ فِي الْمَسْجِدِ الْحَرَامِ أَفْضَلُ مِنْ مِائَةِ أَلْفِ صَلَاةٍ فِيمَا سِوَاهُ</b></font><br>"
            + "<i>\"মসজিদুল হারামে এক রাকাত নামাজ আদায় অন্য যেকোনো মসজিদে এক লক্ষ রাকাত নামাজ পড়ার চেয়েও বেশি সওয়াব ও মর্যাদাপূর্ণ।\"</i> (সুনানে ইবনে মাজাহ: ১৪০৬, মুসনাদে আহমদ: ১৪৬৯৪)<br><br>"
            + "<b>আশীর্বাদ ও বরকত:</b><br>"
            + "এখানে রয়েছে বরকতময় জমজম কূপের পানি পানের সুব্যবস্থা এবং দিন-রাত ২৪ ঘণ্টা সার্বক্ষণিক তাওহীদের আলোকবর্তিকা ও তাওয়াফের মহাসুযোগ।",
            "<b>Overview:</b><br>"
            + "The grand sanctuary enclosing the Holy Kaaba, Mataf, and Mas'a; the holiest mosque in Islam.<br><br>"
            + "<b>100,000x Prayer Reward:</b><br>"
            + "The Prophet (ﷺ) said: 'One prayer in Al-Masjid Al-Haram is better than 100,000 prayers elsewhere.' (Sunan Ibn Majah: 1406)<br><br>"
            + "<b>Blessings:</b><br>"
            + "Supplies pure Zamzam water and hosts continuous worship and Tawaf around the clock."
        ));

        return list;
    }
}
