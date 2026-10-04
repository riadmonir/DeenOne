package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjUmrahContentRepository {

    public static List<HajjHistoryCardItem> getUmrahCards() {
        List<HajjHistoryCardItem> items = new ArrayList<>();

        // Card 1: ওমরাহ এর গুরুত্ব ও তাৎপর্য
        items.add(new HajjHistoryCardItem(
            1,
            "ওমরাহ এর গুরুত্ব ও তাৎপর্য",
            "Significance & Importance of Umrah",
            "ওমরাহ খুবই গুরুত্বপূর্ণ একটি ইবাদাত; যার অর্থ ...",
            "Umrah is an immensely significant worship; literally meaning...",
            "ওমরাহ খুবই গুরুত্বপূর্ণ একটি ইবাদাত; যার অর্থ দর্শন করা, সাক্ষাৎ করা বা পরিদর্শন করা। শরীয়তের পরিভাষায় নির্দিষ্ট নিয়ম অনুযায়ী ইহরাম পরিধান করে আল্লাহর সন্তুষ্টির উদ্দেশ্যে পবিত্র কাবা শরীফ তাওয়াফ, সাফা ও মারওয়া পাহাড়ে সাঈ এবং মাথা মুণ্ডন বা চুল ছোট করার মাধ্যমে সম্পন্ন করা ইবাদতকে ওমরাহ বলা হয়।\n\n" +
            "রাসূলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) বলেছেন:\n" +
            "\"এক ওমরাহ হতে অন্য ওমরাহর মধ্যবর্তী সময়ের সমস্ত গুনাহের কাফফারা হয়ে যায়, আর মাবরুর হজের প্রতিদান একমাত্র জান্নাত।\"\n\n" +
            "<b>[সহীহ বুখারী: ১৭৭৩, সহীহ মুসলিম: ১৩৪৯]</b>\n\n" +
            "হাদিসে আরও এসেছে, রমজান মাসে একটি ওমরাহ পালন করা রাসূলুল্লাহ (ﷺ)-এর সাথে হজ আদায়ের সমতুল্য সওয়াব লাভ করায়। [সহীহ বুখারী: ১৭৮২]。",
            "Umrah is an immensely significant worship; literally meaning to visit, inspect, or frequent. In Islamic terminology, it entails assuming the state of Ihram, circumambulating the Holy Ka'bah (Tawaf), performing Sa'i between Safa and Marwah, and shaving or trimming the hair solely for the pleasure of Allah.\n\n" +
            "The Messenger of Allah (peace and blessings be upon him) said:\n" +
            "\"An Umrah to another Umrah is an expiation for whatever sins occur between them, and the reward of an accepted Hajj is nothing less than Paradise.\"\n\n" +
            "<b>[Sahih al-Bukhari: 1773, Sahih Muslim: 1349]</b>\n\n" +
            "Furthermore, performing Umrah during the blessed month of Ramadan equates in spiritual reward to accompanying the Prophet (ﷺ) on Hajj. [Sahih al-Bukhari: 1782]."
        ));

        // Card 2: ওমরাহ হজের বিবরণ
        items.add(new HajjHistoryCardItem(
            2,
            "ওমরাহ হজের বিবরণ",
            "Details of Umrah Pilgrimage",
            "পবিত্র কুরআনে, হজ্জ ও ওমরাহ উভয়টির কথা এ...",
            "In the Holy Quran, both Hajj and Umrah are mentioned...",
            "পবিত্র কুরআনে, হজ্জ ও ওমরাহ উভয়টির কথা এসেছে। আল্লাহ সুবহানাহু ওয়া তা'আলা এরশাদ করেছেন:\n\n" +
            "وَأَتِمُّوا الْحَجَّ وَالْعُمْرَةَ لِلَّهِ\n\n" +
            "\"আর তোমরা আল্লাহর উদ্দেশ্যে হজ ও ওমরাহ পূর্ণ করো।\"\n" +
            "<b>[সূরা আল-বাকারা: ১৯৬]</b>\n\n" +
            "হজ বছরের একটি সুনির্দিষ্ট সময়ে (যিলহজ মাসে) আদায় করতে হয়, কিন্তু ওমরাহ বছরের যেকোনো দিন যেকোনো সময়ে পালন করা যায়। তবে হজের মূল দিনগুলোতে (৯ থেকে ১৩ই জিলহজ) ওমরাহর ইহরাম বাঁধা অনুত্তম। ওমরাহ পালনে বান্দার আত্মিক পরিশুদ্ধি ঘটে এবং দারিদ্র্য ও গুনাহ মোচন হয়। রাসূলুল্লাহ (ﷺ) বলেছেন: \"তোমরা ধারাবাহিক হজ ও ওমরাহ পালন করো; কেননা তা দারিদ্র্য ও গুনাহ দূর করে যেমন হাঁপর লোহার মরিচা দূর করে।\" [তিরমিযী: ৮১০]。",
            "In the Holy Quran, both Hajj and Umrah are explicitly commanded together. Allah Almighty declares:\n\n" +
            "وَأَتِمُّوا الْحَجَّ وَالْعُمْرَةَ لِلَّهِ\n\n" +
            "\"And complete the Hajj and Umrah for Allah.\"\n" +
            "<b>[Surah Al-Baqarah: 196]</b>\n\n" +
            "While Hajj is strictly restricted to designated days of Dhul Hijjah, Umrah can be performed at any time throughout the year. Continuously performing Umrah purifies the believer from sins and eliminates poverty, just as the bellows remove impurities from iron. [Jami` at-Tirmidhi: 810]."
        ));

        // Card 3: ওমরাহ করা সুন্নত না ওয়াজিব
        items.add(new HajjHistoryCardItem(
            3,
            "ওমরাহ করা সুন্নত না ওয়াজিব",
            "Is Umrah Sunnah or Wajib?",
            "ওমরাহ শুরু করলে তা পূর্ণ করা ওয়াজিব। তবে ...",
            "Once commenced, completing Umrah is Wajib; however...",
            "ওমরাহ শুরু করলে তা পূর্ণ করা ওয়াজিব। তবে সাধারণভাবে ওমরাহ করা সম্পর্কে ফুকাহায়ে কেরামের মাঝে দুটি মত রয়েছে:\n\n" +
            "১. <b>ইমাম শাফেয়ী ও ইমাম আহমদ (রহ.)-এর মতে:</b> সামর্থ্যবান ব্যক্তির জন্য জীবনে একবার ওমরাহ পালন করা ফরয/ওয়াজিব। দলীল সূরা বাকারার ১৯৬ নম্বর আয়াত: \"আর তোমরা আল্লাহর উদ্দেশ্যে হজ ও ওমরাহ পূর্ণ করো।\"\n\n" +
            "২. <b>ইমাম আবু হানীফা ও ইমাম মালিক (রহ.)-এর মতে:</b> ওমরাহ করা সুন্নাতে মুয়াক্কাদাহ। হযরত জাবের (রা.) হতে বর্ণিত, এক ব্যক্তি রাসূলুল্লাহ (ﷺ)-কে জিজ্ঞাসা করলেন: 'ওমরাহ কি ওয়াজিব?' তিনি বললেন: \"না, তবে তোমার ওমরাহ করা উত্তম।\" [তিরমিযী: ৯৩১]।\n\n" +
            "উভয় মতেই সামর্থ্যবান ব্যক্তির জন্য জীবনে অন্তত একবার ওমরাহ পালন করা অত্যন্ত সওয়াবের ও গুরুত্বপূর্ণ ইবাদত।",
            "Once commenced, completing Umrah is strictly Wajib. Regarding whether Umrah is inherently obligatory, classical jurists hold two primary scholarly views:\n\n" +
            "1. <b>Imam Ash-Shafi'i & Imam Ahmad (RA):</b> Performing Umrah once in a lifetime is obligatory (Fard/Wajib) for anyone possessing physical and financial means, citing Surah Al-Baqarah 196.\n\n" +
            "2. <b>Imam Abu Hanifah & Imam Malik (RA):</b> Umrah is an emphatic Sunnah (Sunnah Mu'akkadah). Jabir (RA) narrated that a man asked the Prophet (ﷺ): \"Is Umrah obligatory?\" He replied: \"No, but performing Umrah is virtuous for you.\" [Tirmidhi: 931].\n\n" +
            "Under both schools of jurisprudence, undertaking Umrah once in a lifetime carries immense spiritual reward."
        ));

        // Card 4: ওমরাহ এর ফরয-ওয়াজিব সমূহ
        items.add(new HajjHistoryCardItem(
            4,
            "ওমরাহ এর ফরয-ওয়াজিব সমূহ",
            "Fard & Wajib of Umrah",
            "ওমরাহর ফরয - ইহরাম বাঁধার নিয়ত করা। যে ব্য...",
            "Fard of Umrah - Intention of entering Ihram. Whoever...",
            "ওমরাহর ফরয - ইহরাম বাঁধার নিয়ত করা। যে ব্যক্তি ওমরাহ করতে চায় তাকে অবশ্যই এর ফরয ও ওয়াজিবসমূহ যথাযথভাবে পালন করতে হবে:\n\n" +
            "<b>ওমরাহর ফরয ২টি:</b>\n" +
            "১. <b>ইহরাম গ্রহণ করা:</b> মীকাত অতিক্রম করার পূর্বে নিয়ত ও তালবিয়াহ পাঠ করা।\n" +
            "২. <b>কাবা শরীফ তাওয়াফ করা:</b> পবিত্র কাবা ঘরের চতুর্দিকে ৭ বার প্রদক্ষিণ করা।\n\n" +
            "<b>ওমরাহর ওয়াজিব ২টি:</b>\n" +
            "১. <b>সাফা ও মারওয়ার মাঝে সাঈ করা:</b> সাফা থেকে শুরু করে মারওয়া পর্যন্ত ৭ চক্কর সাঈ সম্পন্ন করা।\n" +
            "২. <b>মাথার চুল মুণ্ডন করা (হলক) বা ছোট করা (কসর)।</b>",
            "Fard of Umrah - Intention of entering Ihram. Whoever intends to perform Umrah must meticulously fulfill its obligatory pillars and mandates:\n\n" +
            "<b>Obligatory Pillars (Fard - 2 Rites):</b>\n" +
            "1. <b>Entering Ihram:</b> Making genuine Niyyah and reciting Talbiyah before crossing the Miqat boundary.\n" +
            "2. <b>Tawaf of the Ka'bah:</b> Circumambulating the Sacred House 7 complete circuits.\n\n" +
            "<b>Obligatory Requisites (Wajib - 2 Rites):</b>\n" +
            "1. <b>Sa'i between Safa and Marwah:</b> Traversing the 7 laps starting at Safa and ending at Marwah.\n" +
            "2. <b>Halq or Taqseer:</b> Shaving the head entirely or trimming hair uniformly."
        ));

        // Card 5: ওমরাহ ওয়াজিব হওয়ার শর্তসমূহ
        items.add(new HajjHistoryCardItem(
            5,
            "ওমরাহ ওয়াজিব হওয়ার শর্তসমূহ",
            "Conditions for Umrah Becoming Obligatory",
            "হজ-ওমরাহ সহীহ হওয়ার জন্য ব্যক্তিকে অবশ্যই ...",
            "For Hajj and Umrah to be valid, a person must be...",
            "হজ-ওমরাহ সহীহ হওয়ার জন্য ব্যক্তিকে অবশ্যই প্রয়োজনীয় শর্তসমূহ পূরণ করতে হবে:\n\n" +
            "• <b>মুসলমান হওয়া:</b> অমুসলিমের কোনো ইবাদত গ্রহণযোগ্য নয়।\n" +
            "• <b>জ্ঞানসম্পন্ন ও সুস্থ মস্তিষ্ক হওয়া:</b> অপ্রকৃতিস্থ বা পাগলের ওপর ইবাদত আবশ্যক নয়।\n" +
            "• <b>প্রাপ্তবয়স্ক বা বালেগ হওয়া:</b> নাবালেগ শিশুর ওপর আবশ্যক নয়।\n" +
            "• <b>স্বাধীন হওয়া:</b> দাসত্বের অধীন না থাকা।\n" +
            "• <b>আর্থিক ও শারীরিক সামর্থ্য থাকা:</b> নিজের এবং পরিবার-পরিজনের প্রয়োজনীয় ভরণপোষণ বজায় রেখে যাতায়াত খরচের সামর্থ্য থাকা।\n" +
            "• <b>পথ নিরাপদ হওয়া:</b> সফরের রাস্তা জান-মালের দিক থেকে ঝুঁকিমুক্ত হওয়া।\n" +
            "• <b>মহিলাদের জন্য মাহরাম থাকা ও ইদ্দতকালীন অবস্থায় না থাকা।</b>",
            "For Hajj and Umrah to be valid, a person must fulfill the required criteria of accountability:\n\n" +
            "• <b>Being a Muslim:</b> Faith is a prerequisite for accepted worship.\n" +
            "• <b>Sanity:</b> Being of sound mind and intellectual capacity.\n" +
            "• <b>Adulthood (Bulugh):</b> Having attained puberty.\n" +
            "• <b>Freedom:</b> Not being enslaved or bounded.\n" +
            "• <b>Capability (Istita'ah):</b> Financial adequacy for travel, lodging, and family provision, alongside physical health.\n" +
            "• <b>Safe Travel Routes:</b> Security of life and honor during travel.\n" +
            "• <b>For Women:</b> Having a qualifying Mahram and not undergoing marital waiting period (Iddah)."
        ));

        // Card 6: হজ ও ওমরাহ আবশ্যক হওয়ার শর্ত
        items.add(new HajjHistoryCardItem(
            6,
            "হজ ও ওমরাহ আবশ্যক হওয়ার শর্ত",
            "Prerequisites for Hajj & Umrah Obligation",
            "প্রাপ্ত বয়স্ক হওয়া - অপ্রাপ্ত বয়স্ক বাচ্চা-যদিও সে ...",
            "Attaining adulthood - A minor child, even if performing...",
            "প্রাপ্ত বয়স্ক হওয়া - অপ্রাপ্ত বয়স্ক বাচ্চা-যদিও সে ওমরাহ বা হজ পালন করে তবে তা নফল হিসেবে গণ্য হবে এবং বালেগ হওয়ার পর সামর্থ্যবান হলে পুনরায় মূল দায়িত্ব আদায় করতে হবে।\n\n" +
            "<b>মূল আবশ্যকীয় শর্তসমূহ:</b>\n" +
            "• <b>আকল বা জ্ঞানসম্পন্ন হওয়া:</b> বিবেকবুদ্ধিসম্পন্ন হওয়া আবশ্যক।\n" +
            "• <b>সামর্থ্য (ইস্তিতাত):</b> পবিত্র মক্কায় যাওয়ার মতো শারীরিক সুস্থতা ও প্রয়োজনীয় পাথেয় থাকা।\n" +
            "• <b>ঋণমুক্ত হওয়া:</b> নিজের যাবতীয় জরুরি ঋণ পরিশোধের পর ওমরাহ সফরের সামর্থ্য থাকা উত্তম।\n" +
            "• <b>পরিবারের খরচ নিশ্চিত করা:</b> সফরকালীন সময়ে পরিবারের সদস্যদের স্বাভাবিক খরচ নিশ্চিত রাখা ওয়াজিব।",
            "Attaining adulthood - A minor child, even if performing Umrah or Hajj, earns the status of voluntary worship (Nafl). Upon reaching maturity, the obligation must be fulfilled anew if capability exists.\n\n" +
            "<b>Core Mandates:</b>\n" +
            "• <b>Intellect & Sanity:</b> Sound mental discernment is mandatory.\n" +
            "• <b>Capability (Istita'ah):</b> Physical resilience and financial independence.\n" +
            "• <b>Freedom from Debt:</b> Settling active debts before commencing international pilgrimage.\n" +
            "• <b>Provision for Dependents:</b> Securing household maintenance during absence."
        ));

        // Card 7: হজের সফরে মহিলার সাথে মাহরাম পুরুষ থাকা
        items.add(new HajjHistoryCardItem(
            7,
            "হজের সফরে মহিলার সাথে মাহরাম পুরুষ থাকা",
            "Mahram Companion for Women During Pilgrimage",
            "ইমাম আবু হানীফা ও ইমাম আহমদ রহ.-এর মতে ...",
            "According to Imam Abu Hanifah and Imam Ahmad (RA)...",
            "ইমাম আবু হানীফা ও ইমাম আহমদ রহ.-এর মতে কোনো মহিলার ওপর হজ বা ওমরাহ আবশ্যক হওয়ার জন্য এবং সফরের জন্য স্বামী অথবা কোনো প্রাপ্তবয়স্ক বিশ্বস্ত মাহরাম পুরুষ (যেমন: পিতা, ভাই, ছেলে, চাচা, মামা) সাথে থাকা আবশ্যক।\n\n" +
            "রাসূলুল্লাহ (ﷺ) এরশাদ করেছেন:\n" +
            "\"কোনো নারী যেন মাহরাম ছাড়া তিন দিনের বা দূরবর্তী সফরে বের না হয়।\"\n\n" +
            "<b>[সহীহ বুখারী: ১০৮৬, সহীহ মুসলিম: ১৩৩৯]</b>\n\n" +
            "বর্তমান সৌদি হজ মন্ত্রণালয়ের নিয়মে বয়স্ক মহিলাদের দলবদ্ধভাবে সফরের আইনি ছাড়পত্র থাকলেও, শরীয়তের মূল ও নিরাপদ বিধান অনুযায়ী মাহরাম সাথে রাখাই সুন্নাতসম্মত ও উত্তম।",
            "According to Imam Abu Hanifah and Imam Ahmad (RA), a woman must be accompanied by her husband or a mature, trustworthy Mahram (such as father, brother, son, uncle) for Hajj and Umrah travel.\n\n" +
            "The Prophet (ﷺ) explicitly declared:\n" +
            "\"A woman must not travel for three days except with a Mahram.\"\n\n" +
            "<b>[Sahih al-Bukhari: 1086, Sahih Muslim: 1339]</b>\n\n" +
            "While regulatory frameworks now permit women above a certain age to travel in authorized groups, having a Mahram remains the cautious, optimal Sunnah standard."
        ));

        // Card 8: ওমরাহ পালনের ধারাবাহিক নিয়ম ও দোয়া
        items.add(new HajjHistoryCardItem(
            8,
            "ওমরাহ পালনের ধারাবাহিক নিয়ম ও দোয়া",
            "Step-by-Step Umrah Rites & Supplications",
            "যাদের অনেকেই মক্কা গিয়েই আদায় করবেন ওম...",
            "Many of those who arrive in Makkah to perform Umrah...",
            "যাদের অনেকেই মক্কা গিয়েই আদায় করবেন ওমরাহ, তাদের জন্য ধারাবাহিক নিয়ম জানা আবশ্যক:\n\n" +
            "১. মীকাত পৌঁছার পূর্বে গোসল বা ওজু করে পরিচ্ছন্ন হওয়া।\n" +
            "২. পুরুষরা দুটি সেলাইবিহীন সাদা চাদর পরিধান করবেন। মহিলারা শালীন ঢিলেঢালা পোশাক পরবেন।\n" +
            "৩. দুই রাকাত ইহরামের নফল নামাজ আদায় করা (মাকরূহ ওয়াক্ত না হলে)।\n" +
            "৪. মনে ও মুখে ওমরাহর স্পষ্ট নিয়ত করা:\n\n" +
            "اللَّهُمَّ إِنِّي أُرِيدُ الْعُمْرَةَ فَيَسِّرْهَا لِي وَتَقَبَّلْهَا مِنِّي\n\n" +
            "<b>উচ্চারণ:</b> 'আল্লাহুম্মা ইন্নী উরীদুল উমরাতা ফায়াসসিরহা লী ওয়া তাক্বাব্বালহা মিন্নী।'\n" +
            "<b>অর্থ:</b> হে আল্লাহ! আমি ওমরাহর নিয়ত করছি, আপনি তা আমার জন্য সহজ করে দিন এবং কবুল করে নিন।\n" +
            "৫. নিয়তের সাথে সাথে তালবিয়াহ পাঠ শুরু করা।",
            "Many of those who arrive in Makkah to perform Umrah must observe the systematic chronological sequence:\n\n" +
            "1. Perform complete purification (Ghusl or Wudu) before reaching the Miqat boundary.\n" +
            "2. Men don two white unstitched cloths (Izar & Rida). Women wear modest regular attire.\n" +
            "3. Pray two Rak'ahs Sunnah of Ihram (unless during forbidden prayer times).\n" +
            "4. Formulate the explicit verbal and heartfelt intention (Niyyah):\n\n" +
            "اللَّهُمَّ إِنِّي أُرِيدُ الْعُمْرَةَ فَيَسِّرْهَا لِي وَتَقَبَّلْهَا مِنِّي\n\n" +
            "<b>Pronunciation:</b> 'Allahumma inni ureedul-'umrata fayassirha li wa taqabbalha minni.'\n" +
            "<b>Meaning:</b> O Allah, I intend to perform Umrah, so make it easy for me and accept it from me.\n" +
            "5. Immediately commence the continuous recitation of Talbiyah."
        ));

        // Card 9: নিয়তের পর অধিক হারে তালবিয়া পড়তে থাকুন
        items.add(new HajjHistoryCardItem(
            9,
            "নিয়তের পর অধিক হারে তালবিয়া পড়তে থাকুন",
            "Recite Talbiyah Abundantly After Niyyah",
            "لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ لَبَّيْكَ لَا شَرِيكَ لَكَ لَبَّইكَ إِنَّ الْحَمْدَ...",
            "Labbayk Allahumma Labbayk, Labbayka la shareeka laka...",
            "নিয়তের পর থেকে তাওয়াফ শুরুর পূর্ব পর্যন্ত পুরুষদের উচ্চৈঃস্বরে এবং মহিলাদের অনুচ্চ কণ্ঠে তালবিয়াহ পাঠ অব্যাহত রাখা সুন্নাত:\n\n" +
            "<b>لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لَا شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لَا شَرِيكَ لَكَ</b>\n\n" +
            "<b>উচ্চারণ:</b>\n" +
            "লাব্বাইক আল্লাহুম্মা লাব্বাইক, লাব্বাইকা লা শারীকা লাকা লাব্বাইক, ইন্নাল হামদা ওয়ান নি'মাতা লাকা ওয়াল মুলক, লা শারীকা লাক।\n\n" +
            "<b>অর্থ:</b>\n" +
            "আমি আপনার দরবারে হাজির হে আল্লাহ! আমি হাজির! আপনার কোনো শরিক নেই, আমি হাজির! নিশ্চয় সমস্ত প্রশংসা, সমস্ত নেয়ামত এবং সার্বভৌমত্ব একমাত্র আপনারই, আপনার কোনো শরিক নেই।",
            "From the moment Niyyah is articulated until entering Tawaf, reciting Talbiyah persistently is Sunnah (men audibly, women softly):\n\n" +
            "<b>لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لَا شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لَا شَرِيكَ لَكَ</b>\n\n" +
            "<b>Pronunciation:</b>\n" +
            "Labbayk Allahumma Labbayk, Labbayka la shareeka laka Labbayk, Innal-hamda wan-ni'mata laka wal-mulk, la shareeka lak.\n\n" +
            "<b>Meaning:</b>\n" +
            "Here I am at Your service, O Allah, here I am! Here I am, You have no partner, here I am! Verily, all praise, grace, and sovereignty belong to You alone, You have no partner."
        ));

        // Card 10: বিমানে ওঠার সময় উপরোল্লিখিত সফরের দোয়া
        items.add(new HajjHistoryCardItem(
            10,
            "বিমানে ওঠার সময় উপরোল্লিখিত সফরের দোয়া",
            "Supplication for Travel When Boarding the Flight",
            "বিমান থেকে জেদ্দা বিমানবন্দর নজরে এলে এই ...",
            "When Jeddah airport comes into sight from the plane...",
            "বিমানে ওঠার সময় উপরোল্লিখিত সফরের দোয়া পড়া সুন্নাত:\n\n" +
            "<b>سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَى رَبِّنَا لَمُنقَلِبُونَ</b>\n\n" +
            "<b>উচ্চারণ:</b>\n" +
            "সুবহানাল্লাযী সাখখারা লানা হাযা ওয়া মা কুন্না লাহু মুক্বরিনীন, ওয়া ইন্না ইলা রব্বিনা লামুনক্বালিবূন।\n\n" +
            "<b>অর্থ:</b>\n" +
            "পবিত্র ও মহান সেই সত্তা, যিনি এটিকে আমাদের বশীভূত করে দিয়েছেন, অথচ আমরা একে বশীভূত করতে সমর্থ ছিলাম না। আর নিশ্চয় আমরা আমাদের রবের নিকটই প্রত্যাবর্তনকারী। [সূরা যুখরুফ: ১৩-১৪]\n\n" +
            "বিমান থেকে জেদ্দা বিমানবন্দর নজরে এলে এই সময় বেশি বেশি জিকির, ইস্তিগফার ও তালবিয়াহ পাঠ করুন।",
            "Upon boarding the aircraft, it is Sunnah to invoke the comprehensive travel supplication:\n\n" +
            "<b>سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَى رَبِّنَا لَمُنقَلِبُونَ</b>\n\n" +
            "<b>Pronunciation:</b>\n" +
            "Subhanalladhee sakh-khara lana hadha wa ma kunna lahu muqrineen, wa inna ila Rabbina lamunqaliboon.\n\n" +
            "<b>Meaning:</b>\n" +
            "Glory be to Him Who has subjected this to us, and we could never have achieved it by ourselves. And verily, unto our Lord we shall return. [Surah Az-Zukhruf: 13-14]\n\n" +
            "When Jeddah airport comes into view, amplify your prayers, seeking forgiveness and proclaiming Talbiyah."
        ));

        // Card 11: জেদ্দায় অবতরণের সময় দোয়া
        items.add(new HajjHistoryCardItem(
            11,
            "জেদ্দায় অবতরণের সময় দোয়া",
            "Supplication Upon Landing in Jeddah",
            "رَبِّ أَدْخِلْنِي مُدْخَلَ صِدْقٍ وَأَخْرِجْنِي مُخْرَجَ صِدْقٍ ...",
            "Rabbi adkhilnee mudkhala sidqin wa akhrijnee...",
            "বিমান জেদ্দা আন্তর্জাতিক বিমানবন্দরে অবতরণের সময় এই দোয়া পাঠ করা উত্তম:\n\n" +
            "<b>رَّبِّ أَدْخِلْنِي مُدْخَلَ صِدْقٍ وَأَخْرِجْنِي مُخْرَجَ صِدْقٍ وَاجْعَل لِّي مِن لَّدُنكَ سُلْطَانًا نَّصِيرًا</b>\n\n" +
            "<b>উচ্চারণ:</b>\n" +
            "রব্বি আদখিলনী মুদখালা সিদক্বিওঁ ওয়া আখরিজনী মুখরাজা সিদক্বিওঁ ওয়াজ'আল লী মিল্লাদুনকা সুলত্বানান নাসীরা।\n\n" +
            "<b>অর্থ:</b>\n" +
            "হে আমার পালনকর্তা! আমাকে প্রবেশ করান সত্যতার সাথে এবং আমাকে নিষ্ক্রান্ত করুন সত্যতার সাথে এবং আমাকে আপনার নিকট থেকে দান করুন সাহায্যকারী শক্তি।\n\n" +
            "<b>[সূরা আল-ইসরা: ৮০]</b>",
            "Upon touching down at Jeddah King Abdulaziz International Airport, recite this Quranic supplication:\n\n" +
            "<b>رَّبِّ أَدْخِلْنِي مُدْخَلَ صِدْقٍ وَأَخْرِجْنِي مُخْرَجَ صِدْقٍ وَاجْعَل لِّي مِن لَّدُنكَ سُلْطَانًا نَّصِيرًا</b>\n\n" +
            "<b>Pronunciation:</b>\n" +
            "Rabbi adkhilnee mudkhala sidqin wa akhrijnee mukhraja sidqin waj'al lee min ladunka sultanan naseera.\n\n" +
            "<b>Meaning:</b>\n" +
            "My Lord, cause me to enter an entry of truth, and cause me to exit an exit of truth, and grant me from Yourself a supporting authority.\n\n" +
            "<b>[Surah Al-Isra: 80]</b>"
        ));

        // Card 12: ওমরাহর সংক্ষিপ্ত কার্যাবলী
        items.add(new HajjHistoryCardItem(
            12,
            "ওমরাহর সংক্ষিপ্ত কার্যাবলী",
            "Summary of Umrah Rites",
            "ফরয গোসলের মতো করে ভালভাবে গোসল করা...",
            "Taking a thorough bath like obligatory Ghusl...",
            "ওমরাহর সংক্ষিপ্ত কার্যাবলী এক নজরে:\n\n" +
            "• ফরয গোসলের মতো করে ভালভাবে গোসল করা অথবা সুন্দরভাবে ওজু সম্পন্ন করা।\n" +
            "• পুরুষরা দুটি সেলাইবিহীন সাদা কাপড় পরা এবং মহিলারা স্বাভাবিক শালীন পোশাক পরিধান করা।\n" +
            "• মীকাত অতিক্রমের পূর্বে ওমরাহর নিয়ত করা ও তালবিয়াহ পাঠ শুরু করা।\n" +
            "• পবিত্র কাবা শরীফ ৭ চক্কর তাওয়াফ করা (হাজরে আসওয়াদ থেকে শুরু ও শেষ)।\n" +
            "• মাকামে ইবরাহিমের পেছনে ২ রাকাত নামাজ আদায় করা ও জমজমের পানি পান করা।\n" +
            "• সাফা ও মারওয়ার মাঝে ৭ চক্কর সাঈ সম্পন্ন করা।\n" +
            "• পুরুষদের মাথা মুণ্ডন (হলক) বা চুল ছোট করা (কসর) এবং মহিলাদের আঙুলের এক কর পরিমাণ চুল কাটা।",
            "A concise summary of Umrah rites at a glance:\n\n" +
            "• Taking a thorough bath like obligatory Ghusl or performing thorough ablution.\n" +
            "• Men donning two unstitched white sheets; women wearing modest Islamic dress.\n" +
            "• Making intention for Umrah and reciting Talbiyah before crossing Miqat.\n" +
            "• Circumambulating the Ka'bah 7 laps starting from the Black Stone.\n" +
            "• Praying two Rak'ahs behind Maqam Ibrahim and drinking Zamzam water.\n" +
            "• Performing Sa'i between Safa and Marwah across 7 laps.\n" +
            "• Shaving or trimming the hair to conclude Ihram and achieve full deconsecration."
        ));

        // Card 13: ইহরামের কাপড় পরিধানের পূর্বে করণীয়
        items.add(new HajjHistoryCardItem(
            13,
            "ইহরামের কাপড় পরিধানের পূর্বে করণীয়",
            "Acts Prior to Donning Ihram Garments",
            "ইহরামের কাপড় পরিধানের পূর্বে শরীরের গুপ্ত ...",
            "Before putting on Ihram garments, shaving unwanted hair...",
            "ইহরামের কাপড় পরিধানের পূর্বে করণীয় পরিচ্ছন্নতা কার্যক্রম:\n\n" +
            "• ইহরামের কাপড় পরিধানের পূর্বে শরীরের গুপ্ত ও অপ্রয়োজনীয় লোম পরিষ্কার করা।\n" +
            "• হাতের ও পায়ের নখ সুন্দরভাবে কেটে ফেলা।\n" +
            "• গোঁফ ছেঁটে নেওয়া ও দাড়ি বিন্যস্ত করা।\n" +
            "• সাবান দিয়ে উত্তমরূপে গোসল করা; গোসলের সুযোগ না থাকলে সুন্দরভাবে ওজু করা।\n" +
            "• শরীরে বা দাড়িতে সুগন্ধি লাগানো সুন্নাত; তবে ইহরামের কাপড়ে সুগন্ধি না লাগানোই নিরাপদ, এবং ইহরাম বাঁধার পর আর সুগন্ধি স্পর্শ করা সম্পূর্ণ নিষিদ্ধ।",
            "Hygiene and personal grooming protocol prior to donning Ihram:\n\n" +
            "• Shaving and removing armpit and pubic hair.\n" +
            "• Trimming finger and toe nails.\n" +
            "• Clipping the moustache and combing the beard.\n" +
            "• Performing a thorough ritual bath (Ghusl) with soap, or performing full Wudu.\n" +
            "• Applying perfume to the body/beard (Sunnah before Niyyah; applying directly on clothing should be avoided, and no perfume may be touched once Ihram is activated)."
        ));

        // Card 14: পুরুষদের জন্য ইহরামের কাপড়
        items.add(new HajjHistoryCardItem(
            14,
            "পুরুষদের জন্য ইহরামের কাপড়",
            "Ihram Garments for Men",
            "পুরুষরা ইহরামের কাপড় হিসেবে সেলাই বিহীন দু...",
            "Men wear two unstitched pieces of cloth as Ihram...",
            "পুরুষরা ইহরামের কাপড় হিসেবে সেলাই বিহীন দুটি সাদা সুতি বা তোয়ালে জাতীয় কাপড় পরিধান করবেন:\n\n" +
            "• <b>ইযার (নিচের অংশ):</b> নাভির ওপর থেকে টাখনুর উপর পর্যন্ত লুঙ্গির মতো করে পরা হয়।\n" +
            "• <b>রিদা (উপরের অংশ):</b> শরীরের উপরিভাগ চাদরের মতো জড়িয়ে রাখা হয়।\n" +
            "• কোনো প্রকার সেলাই করা পোশাক (যেমন: আন্ডারওয়্যার, গেঞ্জি, শার্ট, প্যান্ট ইত্যাদি) পরা যাবে না।\n" +
            "• মাথা ও মুখমণ্ডল সম্পূর্ণ খোলা রাখতে হবে; টুপি বা পাগড়ি পরা যাবে না।\n" +
            "• এমন স্যান্ডেল বা চটি পরতে হবে যাতে পায়ের পাতার ওপরের উঁচু অংশ ও গোড়ালি উন্মুক্ত থাকে।",
            "Men wear two unstitched pieces of cloth as Ihram:\n\n" +
            "• <b>Izar (Lower Sheet):</b> Wrapped around the waist from above the navel down to above the ankles.\n" +
            "• <b>Rida (Upper Sheet):</b> Draped over the upper torso.\n" +
            "• All tailored or stitched garments (underwear, shirts, trousers, socks) are strictly forbidden.\n" +
            "• The head and face must remain completely uncovered; caps or turbans are prohibited.\n" +
            "• Footwear must leave the ankle bones and the upper arch of the feet exposed."
        ));

        // Card 15: মহিলাদের জন্য ইহরামের কাপড়
        items.add(new HajjHistoryCardItem(
            15,
            "মহিলাদের জন্য ইহরামের কাপড়",
            "Ihram Attire for Women",
            "মহিলারা ইহরামের জন্য সেলাই যুক্ত যে কোন পো...",
            "Women wear any regular stitched, modest clothing for Ihram...",
            "মহিলারা ইহরামের জন্য সেলাই যুক্ত যে কোন পোশাক পরিধান করবেন:\n\n" +
            "• মহিলাদের জন্য কোনো বিশেষ রঙের বা সেলাইবিহীন কাপড়ের বাধ্যবাধকতা নেই। তারা নিজেদের পছন্দমতো যেকোনো স্বাভাবিক, ঢিলেঢালা ও শালীন পোশাক পরিধান করবেন।\n" +
            "• পুরো শরীর ও চুল আবৃত রাখতে হবে।\n" +
            "• ইহরাম অবস্থায় মহিলারা চেহারার সাথে সরাসরি লেগে থাকে এমন কোনো নেকাব ব্যবহার করবেন না এবং হাতে গ্লাভস (দস্তানা) পরবেন না। তবে পরপুরুষের সামনে মাথার চাদর সামনে ঝুলিয়ে চেহারা আবৃত রাখবেন।\n" +
            "• স্বাভাবিক জুতো ও মোজা পরা তাদের জন্য বৈধ।",
            "Women wear any regular stitched, modest clothing for Ihram:\n\n" +
            "• There is no designated color or unstitched requirement for women. Any modest, loose-fitting attire is permissible.\n" +
            "• Hair and the entire body must remain fully covered.\n" +
            "• Women must not wear form-fitting facial veils (Niqab) or gloves while in Ihram. However, they may drape their headscarf in front of non-Mahram men without it directly fastening to the skin.\n" +
            "• Regular shoes and socks are fully permissible for women."
        ));

        // Card 16: ইহরামের কাপড় কোথায় পড়বেন
        items.add(new HajjHistoryCardItem(
            16,
            "ইহরামের কাপড় কোথায় পড়বেন",
            "Where to Put on Ihram Attire",
            "যেহেতু মহিলাদের জন্য আলাদা কোন ইহরামের ...",
            "Since women have no separate Ihram clothing requirement...",
            "যেহেতু মহিলাদের জন্য আলাদা কোন ইহরামের কাপড় নেই, তাই তারা বাড়ি থেকেই শালীন পোশাক পরে রওয়ানা হতে পারেন। পুরুষদের জন্য কাপড় পরার সুবিধাজনক স্থান:\n\n" +
            "• <b>বিমানে ভ্রমণের ক্ষেত্রে:</b> নিজ দেশের বিমানবন্দর বা বিমানে ওঠার পূর্বেই ইহরামের চাদর পরিধান করে নেওয়া সবচেয়ে উত্তম ও ঝামেলামুক্ত।\n" +
            "• বিমানে মীকাত ঘোষণার প্রায় ২০-৩০ মিনিট পূর্বে মুখে ওমরাহর নিয়ত করবেন ও তালবিয়াহ পাঠ শুরু করবেন।\n" +
            "• <b>মদিনা থেকে মক্কায় আগমনের ক্ষেত্রে:</b> 'যুল হুলাইফা' (আবয়ার আলী) মীকাত মসজিদে গোসল শেষে ইহরামের কাপড় পরিধান করে নিয়ত ও তালবিয়াহ পড়া সুন্নাত।",
            "Since women have no separate Ihram clothing requirement, they can depart wearing their normal modest dress from home. For men, ideal locations to don Ihram:\n\n" +
            "• <b>Air Travel:</b> Wearing the Ihram sheets at the departure airport before boarding is easiest and stress-free.\n" +
            "• 20-30 minutes before reaching the airborne Miqat boundary, articulate the verbal Niyyah and begin Talbiyah.\n" +
            "• <b>Overland from Madinah:</b> At the Dhul Hulaifah (Abyar Ali) Miqat mosque, bathe, put on Ihram, and proclaim the intention."
        ));

        // Card 17: ইহরামের নিয়ত করা
        items.add(new HajjHistoryCardItem(
            17,
            "ইহরামের নিয়ত করা",
            "Making the Intention (Niyyah) for Ihram",
            "ইহরামের কাপড় পরিধানের পর ইহরামের নিয়ত ...",
            "After wearing Ihram attire, making intention for Ihram...",
            "ইহরামের কাপড় পরিধানের পর ইহরামের নিয়ত করা ওয়াজিব:\n\n" +
            "কাপড় পরার পর মাকরূহ ওয়াক্ত না হলে দুই রাকাত নফল সালাত আদায় করুন। সালাত শেষে মীকাত অতিক্রমের পূর্বে মনে ও মুখে বলুন:\n\n" +
            "<b>اللَّهُمَّ إِنِّي أُرِيدُ الْعُمْرَةَ فَيَسِّرْهَا لِي وَتَقَبَّلْهَا مِنِّي</b>\n\n" +
            "<b>উচ্চারণ:</b> 'আল্লাহুম্মা ইন্নী উরীদুল উমরাতা ফায়াসসিরহা লী ওয়া তাক্বাব্বালহা মিন্নী।'\n" +
            "<b>অর্থ:</b> হে আল্লাহ! আমি ওমরাহর নিয়ত করছি, আপনি তা আমার জন্য সহজ করে দিন এবং আমার পক্ষ থেকে কবুল করে নিন।\n\n" +
            "এরপর উচ্চৈঃস্বরে তালবিয়াহ পাঠ করুন। এর মাধ্যমে আপনি ইহরামের মর্যাদাপূর্ণ অবস্থায় প্রবেশ করলেন এবং ইহরামের যাবতীয় বিধিনিষেধ কার্যকর হলো।",
            "After wearing Ihram attire, making intention for Ihram is an indispensable pillar:\n\n" +
            "Offer two Rak'ahs of Sunnah prayer (if not a disliked time). Conclude the prayer and recite before the Miqat boundary:\n\n" +
            "<b>اللَّهُمَّ إِنِّي أُرِيدُ الْعُمْرَةَ فَيَسِّرْهَا لِي وَتَقَبَّلْهَا مِنِّي</b>\n\n" +
            "<b>Pronunciation:</b> 'Allahumma inni ureedul-'umrata fayassirha li wa taqabbalha minni.'\n" +
            "<b>Meaning:</b> O Allah, I intend to perform Umrah, so make it easy for me and accept it from me.\n\n" +
            "Immediately recite Talbiyah audibly. You have now formally entered Ihram, and all its prohibitions take effect."
        ));

        // Card 18: হারাম শরীফে প্রবেশের সময় দোয়া
        items.add(new HajjHistoryCardItem(
            18,
            "হারাম শরীফে প্রবেশের সময় দোয়া",
            "Supplication Upon Entering the Sacred Sanctuary",
            "হারাম শরীফে প্রবেশের সময় এ দোয়া পড়ুন- ...اللَّهُ",
            "Upon entering the Haram, recite this Dua - ...Allahumma",
            "হারাম শরীফে প্রবেশের সময় এ দোয়া পড়ুন- ডান পা দিয়ে অত্যন্ত বিনীতভাবে প্রবেশ করার সময় পাঠ করুন:\n\n" +
            "<b>بِسْمِ اللَّهِ، وَالصَّلَاةُ وَالسَّلَامُ عَلَى رَسُولِ اللَّهِ، اللَّهُمَّ اغْفِرْ لِي ذُنُوبِي وَافْتَحْ لِي أَبْوَابَ رَحْمَتِكَ، أَعُوذُ بِاللَّهِ الْعَظِيمِ وَبِوَجْهِهِ الْكَرِيمِ وَسُلْطَانِهِ الْقَدِيمِ مِنَ الشَّيْطَانِ الرَّجِيمِ</b>\n\n" +
            "<b>উচ্চারণ:</b>\n" +
            "বিসমিল্লাহি ওয়াস সালাতু ওয়াস সালামু আলা রাসূলিল্লাহ, আল্লাহুম্মাগফির লী যুনূবী ওয়াফতাহ্ লী আবওয়াবা রহমাতিক, আউযু বিল্লাহিল আযীম ওয়া বি-ওয়াজহিহিল কারীম ওয়া সুলত্বানিহিল ক্বাদীম মিনাশ শাইত্বানির রাজীম।\n\n" +
            "<b>কাবা ঘরের প্রথম দর্শনে দোয়া:</b>\n" +
            "পবিত্র কাবা ঘরে প্রথম দৃষ্টি পড়ার সাথে সাথে হাত তুলে দোয়া করুন; এই সময় দোয়া নিশ্চিতভাবে কবুল হয়। বলুন: 'আল্লাহুম্মা যিদ হাযাল বাইতা তাশরীফাঁও ওয়া তা'যীমা...'।",
            "Upon entering the Haram, recite this Dua - Enter humbly stepping with the right foot:\n\n" +
            "<b>بِسْمِ اللَّهِ، وَالصَّلَاةُ وَالسَّلَامُ عَلَى رَسُولِ اللَّهِ، اللَّهُمَّ اغْفِرْ لِي ذُنُوبِي وَافْتَحْ لِي أَبْوَابَ رَحْمَتِكَ، أَعُوذُ بِاللَّهِ الْعَظِيمِ وَبِوَجْهِهِ الْكَرِيمِ وَسُلْطَانِهِ الْقَدِيمِ مِنَ الشَّيْطَانِ الرَّجِيمِ</b>\n\n" +
            "<b>Pronunciation:</b>\n" +
            "Bismillahi was-salatu was-salamu 'ala Rasulillah, Allahummagh-fir li dhunoobi waftah li abwaba rahmatik, A'oodhu billahil-'Azeem wa bi-wajhihil-Kareem wa sultanihel-Qadeem minash-shaytanir-rajeem.\n\n" +
            "<b>First Sight of Ka'bah:</b>\n" +
            "Raise your hands and make heartfelt Dua at the precious moment of first seeing the Holy Ka'bah; prayers are answered then."
        ));

        // Card 19: তালবিয়া
        items.add(new HajjHistoryCardItem(
            19,
            "তালবিয়া",
            "Talbiyah Protocol & Ceasing Time",
            "ওমরাহহুর নিয়ত করার পরেই বেশি বেশি তালবি...",
            "Recite Talbiyah continuously after making Umrah intention...",
            "ওমরাহহুর নিয়ত করার পরেই বেশি বেশি তালবিয়া পাঠ করতে থাকুন:\n\n" +
            "লব্বাইক আল্লাহুম্মা লব্বাইক, লব্বাইকা লা শারীকা লাকা লব্বাইক, ইন্নাল হামদা ওয়ান নি'মাতা লাকা ওয়াল মুলক, লা শারীকা লাক।\n\n" +
            "<b>তালবিয়া কখন বন্ধ করবেন?</b>\n" +
            "পবিত্র কাবা শরীফে প্রবেশ করে তাওয়াফ শুরু করার জন্য যখন হাজরে আসওয়াদের সামনে দাঁড়িয়ে ইস্তিলাম (ইশারা বা চুম্বন) করবেন, ঠিক তখনই তালবিয়া পাঠ বন্ধ করে দিতে হবে। এরপর তাওয়াফের দোয়া ও তাসবীহ শুরু হবে।",
            "Recite Talbiyah continuously after making Umrah intention:\n\n" +
            "Labbayk Allahumma Labbayk, Labbayka la shareeka laka Labbayk, Innal-hamda wan-ni'mata laka wal-mulk, la shareeka lak.\n\n" +
            "<b>When to Cease Talbiyah?</b>\n" +
            "Upon reaching the Holy Ka'bah and facing the Black Stone (Hajar al-Aswad) to commence Tawaf, cease reciting Talbiyah immediately. From that point, Tawaf supplications and Tasbih begin."
        ));

        // Card 20: হজ ও ওমরাহ কালীন দোয়াসমূহ
        items.add(new HajjHistoryCardItem(
            20,
            "হজ ও ওমরাহ কালীন দোয়াসমূহ",
            "Supplications During Hajj & Umrah",
            "তাওয়াফ শুরু করার আগে হাজরে আসওয়াদের ...",
            "Before commencing Tawaf facing the Black Stone...",
            "তাওয়াফ শুরু করার আগে হাজরে আসওয়াদের মুখোমুখি হয়ে বলুন: 'বিসমিল্লাহি আল্লাহু আকবার'।\n\n" +
            "তাওয়াফের সময় কোনো নির্দিষ্ট দোয়া আবশ্যক নয়; আপনি কোরআন তেলাওয়াত, জিকির ও নিজের ভাষায় প্রার্থনা করতে পারেন। তবে রুকনে ইয়ামানী ও হাজরে আসওয়াদের মধ্যবর্তী স্থানে এই দোয়া পড়া সুন্নাত:\n\n" +
            "<b>رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ</b>\n\n" +
            "<b>উচ্চারণ:</b> 'রব্বানা আতিনা ফিদ-দুনয়া হাসানাতাঁও ওয়া ফিল আখিরাতি হাসানাতাঁও ওয়া ক্বিনা আযাবান নার।'\n" +
            "<b>অর্থ:</b> হে আমাদের রব! আমাদের দুনিয়াতে কল্যাণ দিন এবং আখেরাতেও কল্যাণ দিন এবং আমাদের জাহান্নামের আযাব থেকে রক্ষা করুন। [সূরা বাকারা: ২০১]",
            "Before commencing Tawaf facing the Black Stone, proclaim: 'Bismillahi Allahu Akbar'.\n\n" +
            "No specific set of words is strictly mandated during Tawaf; personal supplications, Quranic recitation, and Dhikr are all virtuous. Between Rukn al-Yamani and the Black Stone, reciting this Sunnah verse is recommended:\n\n" +
            "<b>رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ</b>\n\n" +
            "<b>Pronunciation:</b> 'Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.'\n" +
            "<b>Meaning:</b> Our Lord! Grant us good in this world and good in the Hereafter, and save us from the torment of the Fire. [Surah Al-Baqarah: 201]"
        ));

        // Card 21: ওমরাহ হজের রুকন ও ওয়াজিব সমূহ
        items.add(new HajjHistoryCardItem(
            21,
            "ওমরাহ হজের রুকন ও ওয়াজিব সমূহ",
            "Pillars (Rukn) & Obligations (Wajib) of Umrah",
            "ওমরাহহ এর রুকুন (ফরয) হল তিনটি - • অন্তরে ...",
            "The core pillars (Fard) of Umrah are based upon - • In the heart...",
            "ওমরাহহ এর রুকুন (ফরয) হল তিনটি - • অন্তরে ওমরাহর নিয়ত করা • মীকাত থেকে ইহরাম বাঁধা • এবং কাবা শরীফ তাওয়াফ করা।\n\n" +
            "<b>ওমরাহর রুকনসমূহ (ফরয):</b>\n" +
            "• মীকাত থেকে ইহরাম পরিধান ও নিয়ত করা।\n" +
            "• পবিত্র কাবা ঘরের তাওয়াফ করা।\n\n" +
            "<b>ওমরাহর ওয়াজিবসমূহ:</b>\n" +
            "• সাফা ও মারওয়া পাহাড়ের মাঝে সাঈ করা।\n" +
            "• মাথার চুল মুণ্ডন করা (হলক) বা ছোট করা (কসর)।\n\n" +
            "কোনো ফরয ছুটে গেলে ওমরাহ বাতিল হয়ে যায়; আর কোনো ওয়াজিব ছুটে গেলে কাফফারা স্বরূপ 'দম' (একটি পশু কোরবানি) দেওয়া আবশ্যক হয়।",
            "The core pillars (Fard) of Umrah are based upon - • In the heart forming pure intention • Assuming Ihram from Miqat • And circumambulating the Ka'bah.\n\n" +
            "<b>Essential Pillars (Fard):</b>\n" +
            "• Entering Ihram with intention before crossing Miqat.\n" +
            "• Tawaf around the Holy Ka'bah.\n\n" +
            "<b>Obligatory Requisites (Wajib):</b>\n" +
            "• Sa'i between Safa and Marwah.\n" +
            "• Halq (shaving) or Taqseer (trimming hair).\n\n" +
            "Missing a Fard invalidates Umrah, whereas omitting a Wajib requires a compensatory sacrifice (Dam)."
        ));

        // Card 22: নারী-পুরুষের ওমরাহ ধারাবাহিক বিস্তারিত নিয়ম ও দোয়া
        items.add(new HajjHistoryCardItem(
            22,
            "নারী-পুরুষের ওমরাহ ধারাবাহিক বিস্তারিত নিয়ম ও দোয়া",
            "Step-by-Step Umrah Rules & Duas for Men and Women",
            "প্রথম কাজ : ইহরাম বাঁধা (ফরজ) নির্ধারিত মিকা...",
            "First deed: Entering Ihram (Fard) before designated Miqat...",
            "নারী ও পুরুষের জন্য ওমরাহর ধারাবাহিক ৪টি মূল কাজ:\n\n" +
            "১. <b>প্রথম কাজ : ইহরাম বাঁধা (ফরজ)</b> নির্ধারিত মীকাত অতিক্রমের পূর্বে গোসল/ওজু করে ইহরামের কাপড় পরে নিয়ত করা ও তালবিয়াহ পড়া।\n" +
            "২. <b>দ্বিতীয় কাজ : তাওয়াফ করা (ফরজ)</b> কাবা শরীফের চারপাশে হাজরে আসওয়াদ থেকে শুরু করে ৭ চক্কর তাওয়াফ করা।\n" +
            "৩. <b>তৃতীয় কাজ : সাফা-মারওয়ায় সাঈ করা (ওয়াজিব)</b> সাফা পাহাড় থেকে শুরু করে মারওয়ায় মোট ৭টি চক্কর সম্পন্ন করা।\n" +
            "৪. <b>চতুর্থ কাজ : মাথা মুণ্ডন বা চুল ছোট করা (ওয়াজিব)</b> পুরুষদের পুরো মাথা কামানো বা ছোট করা, মহিলাদের আঙুলের এক কর পরিমাণ চুল কাটা।",
            "The 4 chronological key deeds of Umrah for men and women:\n\n" +
            "1. <b>First Deed : Entering Ihram (Fard):</b> Purifying, donning Ihram garments, making Niyyah and reciting Talbiyah before Miqat.\n" +
            "2. <b>Second Deed : Performing Tawaf (Fard):</b> Completing 7 circuits around Ka'bah starting from the Black Stone.\n" +
            "3. <b>Third Deed : Sa'i between Safa and Marwah (Wajib):</b> Walking 7 laps starting at Safa and ending at Marwah.\n" +
            "4. <b>Fourth Deed : Shaving or Trimming Hair (Wajib):</b> Halq/Taqseer for men, trimming one fingertip length for women."
        ));

        // Card 23: দ্বিতীয় কাজ - তাওয়াফ করা (ফরজ)
        items.add(new HajjHistoryCardItem(
            23,
            "দ্বিতীয় কাজ - তাওয়াফ করা (ফরজ)",
            "Second Deed - Performing Tawaf (Fard)",
            "ওমরাহহ দ্বিতীয় ফরজ কাজ হলো কাবা শরিফ তা...",
            "The second Fard duty of Umrah is circumambulating the Kaaba...",
            "ওমরাহহ দ্বিতীয় ফরজ কাজ হলো কাবা শরিফ তাওয়াফ করা:\n\n" +
            "• তাওয়াফের জন্য ওজু থাকা আবশ্যক ও শর্ত।\n" +
            "• পুরুষদের জন্য তাওয়াফের প্রথম ৩ চক্করে ডান কাঁধ খোলা রেখে চাদর পরাকে 'ইযতিবা' এবং বীরদর্পে দ্রুত হাঁটাকে 'রমল' বলে, যা সুন্নাত।\n" +
            "• হাজরে আসওয়াদের বরাবর গ্রিন লাইটের সামনে দাঁড়িয়ে দুই হাত তুলে 'বিসমিল্লাহি আল্লাহু আকবার' বলে তাওয়াফ শুরু করুন।\n" +
            "• কাবা শরীফকে বামে রেখে ঘড়ির কাঁটার বিপরীত দিকে ৭ চক্কর সম্পন্ন করুন।",
            "The second Fard duty of Umrah is circumambulating the Kaaba (Tawaf):\n\n" +
            "• Purity with valid Wudu is an indispensable condition for Tawaf.\n" +
            "• Men practice 'Idtiba' (uncovering the right shoulder) and 'Raml' (brisk, forceful pace) during the first 3 laps.\n" +
            "• Face the Black Stone in line with the green marker light, raise hands, proclaim 'Bismillahi Allahu Akbar', and begin.\n" +
            "• Complete 7 laps counter-clockwise keeping the Ka'bah to your left."
        ));

        // Card 24: তাওয়াফ করার নিয়ম
        items.add(new HajjHistoryCardItem(
            24,
            "তাওয়াফ করার নিয়ম",
            "Method and Etiquette of Tawaf",
            "তাওয়াফ একটি অত্যন্ত বরকতময় এবাদত এবং ...",
            "Tawaf is an immensely blessed act of devotion and...",
            "তাওয়াফ একটি অত্যন্ত বরকতময় এবাদত এবং এর আদবসমূহ:\n\n" +
            "• তাওয়াফ মোট ৭টি চক্করে সম্পন্ন হয়। প্রতি চক্কর হাজরে আসওয়াদ থেকে শুরু হয়ে হাজরে আসওয়াদে এসেই শেষ হয়।\n" +
            "• তাওয়াফের সময় বিনম্রতা ও একাগ্রতা বজায় রাখুন; ধাক্কাধাক্কি থেকে সম্পূর্ণ বিরত থাকুন।\n" +
            "• যেকোনো সহীহ দোয়া, তাসবীহ, দরূদ ও তওবা পাঠ করতে পারেন।\n" +
            "• রুকনে ইয়ামানী অতিক্রম করার সময় সম্ভব হলে তা ডান হাত দিয়ে স্পর্শ করুন; সম্ভব না হলে কোনো ইশারা করবেন না।\n" +
            "• ৭ম চক্কর শেষ হলে পুরুষরা চাদর দিয়ে উভয় কাঁধ ঢেকে ফেলুন।",
            "Tawaf is an immensely blessed act of devotion and its key protocols:\n\n" +
            "• Tawaf consists of 7 circuits; each begins and ends at the Black Stone.\n" +
            "• Maintain reverence, humility, and avoid crowding or shoving other pilgrims.\n" +
            "• Supplicate freely with personal prayers, Quranic verses, Salawat, and Dhikr.\n" +
            "• At the Yemeni Corner (Rukn al-Yamani), touch it with the right hand if possible without kissing; do not gesture from afar.\n" +
            "• Upon completing the 7th circuit, cover both shoulders with the upper Ihram garment."
        ));

        // Card 25: মাকামে ইবরাহিমে নামাজ
        items.add(new HajjHistoryCardItem(
            25,
            "মাকামে ইবরাহিমে নামাজ",
            "Prayer at Maqam Ibrahim",
            "তাওয়াফ শেষে সম্ভব হলে মাকামে ইবরাহিমে কিং...",
            "Following Tawaf, pray behind Maqam Ibrahim if feasible or...",
            "তাওয়াফ শেষে সম্ভব হলে মাকামে ইবরাহিমে কিংবা মসজিদুল হারামের যেকোনো স্থানে দুই রাকাত ওয়াজিবুত তাওয়াফ সালাত আদায় করুন:\n\n" +
            "• সূরা বাকারার ১২৫ নম্বর আয়াতে বলা হয়েছে: 'তোমরা মাকামে ইবরাহিমকে নামাজের জায়গা বানিয়ে নাও।'\n" +
            "• প্রথম রাকাতে সূরা ফাতিহার পর সূরা আল-কাফিরূন এবং দ্বিতীয় রাকাতে সূরা আল-ইখলাস পড়া সুন্নাত।\n" +
            "• সালাত শেষে কিবলামুখী হয়ে আল্লাহর দরবারে বিনীতভাবে রোনাজারি ও দোয়া করুন।",
            "Following Tawaf, pray behind Maqam Ibrahim if feasible or anywhere within the Sacred Sanctuary:\n\n" +
            "• Surah Al-Baqarah verse 125 instructs: 'And take the Station of Ibrahim as a place of prayer.'\n" +
            "• Recite Surah Al-Kafirun in the first Rak'ah after Al-Fatihah, and Surah Al-Ikhlas in the second Rak'ah.\n" +
            "• Conclude the prayer facing Qiblah and offer heartfelt supplications to Allah."
        ));

        // Card 26: অতঃপর জমজমের পানি পান
        items.add(new HajjHistoryCardItem(
            26,
            "অতঃপর জমজমের পানি পান",
            "Drinking Zamzam Water with Supplication",
            "মাকামে ইবরাহিমে নামাজ আদায় করে জমজমের...",
            "After praying at Maqam Ibrahim, drinking Zamzam water...",
            "মাকামে ইবরাহিমে নামাজ আদায় করে জমজমের পানি তৃপ্তিসহকারে পান করা সুন্নাত:\n\n" +
            "• কিবলামুখী হয়ে বিসমিল্লাহ বলে দাঁড়িয়ে বা বসে জমজমের পানি পান করুন।\n" +
            "• মাথায় ও মুখে সামান্য পানি দেওয়া বরকতময়।\n" +
            "• পানি পানের সময় এই দোয়াটি পাঠ করুন:\n\n" +
            "<b>اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا وَاسِعًا، وَشِفَاءً مِنْ كُلِّ دَاءٍ</b>\n\n" +
            "<b>উচ্চারণ:</b> 'আল্লাহুম্মা ইন্নী আসআলুকা ইলমান নাফি'আ, ওয়া রিযক্বান ওয়াসি'আ, ওয়া শিফা-আম মিন কুল্লি দা-ই।'\n" +
            "<b>অর্থ:</b> হে আল্লাহ! আমি আপনার নিকট উপকারী জ্ঞান, প্রশস্ত রিজিক এবং সকল রোগব্যাধি থেকে আরোগ্য প্রার্থনা করছি।",
            "After praying at Maqam Ibrahim, drinking Zamzam water abundantly is a cherished Sunnah:\n\n" +
            "• Drink facing Qiblah with Bismillah in three sips.\n" +
            "• Sprinkling a little water over your head and face carries blessing.\n" +
            "• Recite this beloved supplication while drinking:\n\n" +
            "<b>اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا وَاسِعًا، وَشِفَاءً مِنْ كُلِّ دَاءٍ</b>\n\n" +
            "<b>Pronunciation:</b> 'Allahumma inni as'aluka 'ilman nafi'a, wa rizqan wasi'a, wa shifa'an min kulli da'.'\n" +
            "<b>Meaning:</b> O Allah, I ask You for beneficial knowledge, abundant provision, and healing from every illness."
        ));

        // Card 27: তৃতীয় কাজ : সাফা-মারওয়ায় সাঈ করা (ওয়াজিব)
        items.add(new HajjHistoryCardItem(
            27,
            "তৃতীয় কাজ : সাফা-মারওয়ায় সাঈ করা (ওয়াজিব)",
            "Third Deed: Sa'i Between Safa & Marwah (Wajib)",
            "জমজমের পানি পান করে ধীরে ধীরে সাফা পাহা...",
            "After drinking Zamzam water, proceed slowly towards Safa hill...",
            "জমজমের পানি পান করে ধীরে ধীরে সাফা পাহাড়ে গমন করুন সাঈ সম্পন্ন করার জন্য। এটি ওমরাহর তৃতীয় আবশ্যকীয় (ওয়াজিব) কাজ:\n\n" +
            "• সাফা পাহাড়ে ওঠার সময় এই কুরআনিক আয়াত পাঠ করুন:\n" +
            "\"إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ\"\n" +
            "<b>[সূরা আল-বাকারা: ১৫৮]</b>\n" +
            "এবং বলুন: 'আবদাউ বিমা বাদাআল্লাহু বিহী' (আল্লাহ যা দিয়ে শুরু করেছেন, আমিও তা দিয়ে শুরু করছি)।\n\n" +
            "• সাফা পাহাড়ে কাবামুখী হয়ে হাত তুলে তিনবার 'আল্লাহু আকবার, লা ইলাহা ইল্লাল্লাহ' বলে দীর্ঘ সময় মোনাজাত করুন।",
            "After drinking Zamzam water, proceed slowly towards Safa hill to perform Sa'i, the 3rd Wajib rite of Umrah:\n\n" +
            "• While ascending Safa, recite the Quranic verse:\n" +
            "\"إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ\"\n" +
            "<b>[Surah Al-Baqarah: 158]</b>\n" +
            "And say: 'Abda'u bima bada'Allahu bihi' (I begin with that which Allah began with).\n\n" +
            "• Stand facing the Ka'bah with raised hands, proclaim Takbir three times, and supplicate fervently."
        ));

        // Card 28: সাঈতে করণীয়
        items.add(new HajjHistoryCardItem(
            28,
            "সাঈতে করণীয়",
            "Prescribed Actions During Sa'i",
            "সাঈ করার নিয়ত বা প্রতিজ্ঞা করা। • হাজরে আস...",
            "Making the intention for Sa'i. • Facing the direction of...",
            "সাঈতে করণীয় গুরুত্বপূর্ণ বিষয়সমূহ:\n\n" +
            "• সাঈ করার নিয়ত বা প্রতিজ্ঞা করা।\n" +
            "• সাঈ মোট ৭টি চক্করে সম্পন্ন হবে। সাফা থেকে মারওয়া পর্যন্ত ১টি চক্কর, মারওয়া থেকে সাফা ২য় চক্কর—এভাবে ৭ম চক্কর মারওয়ায় সমাপ্ত হবে।\n" +
            "• দুই সবুজ বাতির মধ্যবর্তী স্থানে পুরুষরা সামান্য দ্রুত বা দৌড়ের গতিতে চলবেন। মহিলারা স্বাভাবিকভাবে হাঁটবেন।\n" +
            "• সাঈ চলাকালে অবিরত তাসবীহ, ইস্তিগফার ও দোয়া পাঠ করুন।",
            "Prescribed Actions During Sa'i:\n\n" +
            "• Formulate the sincere intention for Sa'i.\n" +
            "• Sa'i comprises 7 laps: Safa to Marwah is 1 lap; Marwah to Safa is lap 2, ending the 7th lap at Marwah.\n" +
            "• Men jog briskly between the two green fluorescent marker pillars; women walk at a regular pace.\n" +
            "• Maintain continuous remembrance, Istighfar, and heartfelt prayers throughout."
        ));

        // Card 29: মারওয়া পাহাড়ে আরোহণ
        items.add(new HajjHistoryCardItem(
            29,
            "মারওয়া পাহাড়ে আরোহণ",
            "Ascending Mount Marwah",
            "সবুজ চিহ্নিত স্থান অতিক্রম করে নারী-পুরুষ সবা...",
            "Crossing the green marker zone, all men and women...",
            "সবুজ চিহ্নিত স্থান অতিক্রম করে নারী-পুরুষ সবাই স্বাভাবিক গতিতে মারওয়া পাহাড়ে আরোহণ করবেন:\n\n" +
            "• মারওয়ায় কিবলামুখী হয়ে হাত তুলে আল্লাহর প্রশংসা করুন এবং দোয়া করুন।\n" +
            "• মারওয়া থেকে পুনরায় সাফার দিকে রওয়ানা হবেন।\n" +
            "• ৭ম চক্কর মারওয়া পাহাড়ে পৌঁছালে সাঈর আনুষ্ঠানিকতা সমাপ্ত হবে।\n" +
            "• সাঈ শেষে মারওয়া সংলগ্ন চত্বরে দুই রাকাত নফল সালাত আদায় করা উত্তম।",
            "Crossing the green marker zone, all men and women resume regular pace ascending Mount Marwah:\n\n" +
            "• On Marwah, face the Qiblah, praise Allah, proclaim His greatness, and make Dua.\n" +
            "• Depart Marwah returning back towards Safa.\n" +
            "• Upon completing the 7th lap atop Marwah, Sa'i is officially concluded.\n" +
            "• It is virtuous to offer two voluntary Rak'ahs of prayer near the Marwah exit area."
        ));

        // Card 30: ওমরাহ শেষ কাজ : মাথা মুণ্ডন করা (ওয়াজিব)
        items.add(new HajjHistoryCardItem(
            30,
            "ওমরাহ শেষ কাজ : মাথা মুণ্ডন করা (ওয়াজিব)",
            "Final Deed: Shaving or Trimming Hair (Wajib)",
            "সাফা এবং মারওয়া পাহাড়দ্বয় সাঈ করার পর মা...",
            "After concluding Sa'i between Safa and Marwah, head shaving...",
            "সাফা এবং মারওয়া পাহাড়দ্বয় সাঈ করার পর মাথা মুণ্ডন করা বা চুল ছোট করা ওমরাহর শেষ ওয়াজিব কাজ:\n\n" +
            "• <b>পুরুষদের জন্য:</b> পুরো মাথা ক্ষুর বা ব্লেড দিয়ে কামানো (হলক) সর্বোত্তম ও ৩ গুণ অধিক সওয়াবপূর্ণ। কামাতে না চাইলে পুরো মাথার চুল সমানভাবে ছোট (কসর) করবেন।\n" +
            "• <b>মহিলাদের জন্য:</b> মাথা কামানো সম্পূর্ণ হারাম ও নিষিদ্ধ। মহিলারা তাদের চুলের শেষ প্রান্ত থেকে আঙুলের এক কর (প্রায় ১ ইঞ্চি) পরিমাণ চুল কেটে নেবেন।\n\n" +
            "চুল কাটার সাথে সাথে ওমরাহর ইহরাম সমাপ্ত হলো এবং ইহরামের সমস্ত নিষেধাজ্ঞা উঠে গিয়ে পূর্ণ হালাল সম্পন্ন হলো। আলহামদুলিল্লাহ!",
            "After concluding Sa'i between Safa and Marwah, head shaving or hair trimming is the culminating Wajib rite of Umrah:\n\n" +
            "• <b>For Men:</b> Shaving the entire head with a razor (Halq) is superior and rewarded threefold over trimming. Alternatively, trim hair uniformly across the entire head (Taqseer).\n" +
            "• <b>For Women:</b> Shaving the head is strictly prohibited. Women simply trim a fingertip's length (approx. 1 inch) from the ends of their hair braids.\n\n" +
            "Upon cutting the hair, the state of Ihram ends, all prohibitions are lifted, and full deconsecration is attained. Alhamdulillah!"
        ));

        return items;
    }
}
