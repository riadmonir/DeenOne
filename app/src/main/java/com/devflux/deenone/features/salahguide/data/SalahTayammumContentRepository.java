package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for: তায়াম্মুম (Tayammum - Dry Ablution).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahTayammumContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // Card 1: তায়াম্মুম (Verbatim)
        list.add(new HajjHistoryCardItem(
            1,
            "তায়াম্মুম",
            "Tayammum (Introduction & Concept)",
            "তায়াম্মুম হলো এমন একটি পবিত্রতার পদ্ধতি, যা তখন করা হয় যখন ওজু বা গোসলের জন্য পানি পাওয়া যায় না, অথবা পানি ব্যবহার করা ক্ষতিকর বা অসম্ভব হয়ে যায়। এই অবস্থায় পরিষ্কার মাটি বা ধূলা দিয়ে নির্দিষ্ট নিয়মে মুখ ও হাত মাসেহ করার মাধ্যমে পবি...",
            "Tayammum is a method of ritual purification performed when water is unavailable for Wudu or Ghusl, or when using water is harmful or impossible. In this state, ritual purity is attained by wiping the face and hands with clean earth or dust in a prescribed manner...",
            "তায়াম্মুম হলো এমন একটি পবিত্রতার পদ্ধতি, যা তখন করা হয় যখন ওজু বা গোসলের জন্য পানি পাওয়া যায় না, অথবা পানি ব্যবহার করা ক্ষতিকর বা অসম্ভব হয়ে যায়। এই অবস্থায় পরিষ্কার মাটি বা ধূলা দিয়ে নির্দিষ্ট নিয়মে মুখ ও হাত মাসেহ করার মাধ্যমে পবিত্রতা অর্জন করা হয়। ইসলাম সহজ ধর্ম তাই আল্লাহ তাআলা বান্দাদের কষ্টে ফেলেন না। পানি না থাকলেও যেন নামাজ বন্ধ না হয়, সে জন্য তায়াম্মুমের ব্যবস্থা রাখা হয়েছে। এটি ওজু বা গোসলের বিকল্প হিসেবে কাজ করে, তবে এটি সাময়িক; পানি পাওয়া গেলে আবার স্বাভাবিকভাবে ওজু বা গোসল করতে হয়।\n\n" +
            "فَلَمْ تَجِدُوا مَاءً فَتَيَمَّمُوا صَعِيدًا طَيِّبًا\n\n" +
            "অর্থ:\n" +
            "যদি তোমরা পানি না পাও, তবে পবিত্র মাটি দ্বারা তায়াম্মুম করো।\n\n" +
            "[সূরা আল-মায়িদা ৬]",
            "Tayammum is a method of ritual purification performed when water is unavailable for Wudu or Ghusl, or when using water is harmful or impossible. In this state, ritual purity is attained by wiping the face and hands with clean earth or dust in a prescribed manner. Islam is an easy religion, so Allah the Almighty does not put His servants into hardship. Tayammum has been prescribed so that prayer does not cease even in the absence of water. It serves as a substitute for Wudu or Ghusl, but it is temporary; once water becomes available, one must perform normal Wudu or Ghusl.\n\n" +
            "فَلَمْ تَجِدُوا مَاءً فَتَيَمَّمُوا صَعِيدًا طَيِّبًا\n\n" +
            "Translation:\n" +
            "If you find no water, then perform Tayammum with clean earth.\n\n" +
            "[Surah Al-Ma'idah 5:6]"
        ));

        // Card 2: কার জন্য তায়াম্মুম করা বৈধ (Verbatim)
        list.add(new HajjHistoryCardItem(
            2,
            "কার জন্য তায়াম্মুম করা বৈধ",
            "Who is Permitted to Perform Tayammum",
            "পানি না পাওয়া গেলে ওযূ বা গোসলের পরিবর্তে পবিত্র মাটি দ্বারা পবিত্রতা অর্জনের ইসলামী পদ্ধতিকে ‘তায়াম্মুম’ বলে’। যখন তায়াম্মুম করা যাবে: • যদি পবিত্র পানি না পাওয়া যায়। • পানি পেতে গেলে যদি সালাত ক্বাযা হওয়ার ভয় থাকে...",
            "The Islamic method of attaining ritual purity using clean earth instead of Wudu or Ghusl when water cannot be found is called 'Tayammum'. When Tayammum is permissible: • If pure water is not found. • If seeking water carries fear of missing the prayer time...",
            "পানি না পাওয়া গেলে ওযূ বা গোসলের পরিবর্তে পবিত্র মাটি দ্বারা পবিত্রতা অর্জনের ইসলামী পদ্ধতিকে ‘তায়াম্মুম’ বলে’।\n\n" +
            "যখন তায়াম্মুম করা যাবে:\n" +
            "• যদি পবিত্র পানি না পাওয়া যায়।\n" +
            "• পানি পেতে গেলে যদি সালাত ক্বাযা হওয়ার ভয় থাকে।\n" +
            "• পানি ব্যবহারে যদি রোগ বৃদ্ধির আশংকা থাকে।\n" +
            "• যদি কোন বিপদ বা জীবনের ঝুঁকি থাকে ইত্যাদি।\n\n" +
            "উপরোক্ত কারণ সমূহের প্রেক্ষিতে ওযূ বা ফরয গোসলের পরিবর্তে প্রয়োজনে দীর্ঘদিন যাবৎ একটানা ‘তায়াম্মুম’ করা যাবে। কেননা রাসূল (সাঃ) বলেন,\n\n" +
            "إِنَّ الصَّعِيدَ الطَّيِّبَ وَضُوءُ الْمُسلم وَإِن لم يجد لاماء عشر سِنِين\n\n" +
            "অর্থ:\n" +
            "নিশ্চয়ই পবিত্র মাটি মুসলমানদের জন্য ওযূর মাধ্যম স্বরূপ। যদিও সে ১০ বছর পর্যন্ত পানি না পায়’।\n\n" +
            "[আহমাদ, তিরমিযী, আবুদাঊদ, নাসাঈ, মিশকাত হা/৫৩০, ‘তায়াম্মুম’ অনুচ্ছেদ-১০]",
            "The Islamic method of attaining ritual purity using clean earth instead of Wudu or Ghusl when water cannot be found is called 'Tayammum'.\n\n" +
            "When Tayammum is permissible:\n" +
            "• If pure water is not found.\n" +
            "• If seeking water carries fear of missing the prayer time (Qada).\n" +
            "• If using water causes fear of worsening illness.\n" +
            "• If there is danger, peril, or risk to life and safety, etc.\n\n" +
            "In light of the above reasons, one may continuously perform 'Tayammum' for long periods when necessary in place of Wudu or obligatory Ghusl. For the Messenger of Allah (peace be upon him) said:\n\n" +
            "إِنَّ الصَّعِيدَ الطَّيِّبَ وَضُوءُ الْمُسلم وَإِن لم يجد لاماء عشر سِنِين\n\n" +
            "Translation:\n" +
            "\"Indeed, clean earth is the ablution for a Muslim, even if he does not find water for ten years.\"\n\n" +
            "[Ahmad, Tirmidhi, Abu Dawud, Nasa'i, Mishkat Hadith 530, 'Tayammum' Section-10]"
        ));

        // Card 3: তায়াম্মুম ফরজ হওয়ার শর্তাবলী
        list.add(new HajjHistoryCardItem(
            3,
            "তায়াম্মুম ফরজ হওয়ার শর্তাবলী",
            "Conditions that Make Tayammum Obligatory",
            "* বালেগ বা প্রাপ্ত বয়স্ক হওয়া। * মাটি ব্যবহারে সক্ষম হওয়া। * অপবিত্রতা নষ্টকারী কোনো কিছু ঘটা।",
            "* Being of the age of puberty (Baligh). * Being capable of using clean earth. * Occurrence of anything that breaks ritual purity.",
            "* বালেগ বা প্রাপ্ত বয়স্ক হওয়া।\n" +
            "* মাটি ব্যবহারে সক্ষম হওয়া।\n" +
            "* অপবিত্রতা নষ্টকারী কোনো কিছু ঘটা (যেমন ওজু বা গোসল ভেঙে যাওয়া)।\n" +
            "* পানি ব্যবহারে অপারগ বা অক্ষম হওয়া।",
            "* Being an adult who has reached puberty (Baligh).\n" +
            "* Being physically capable of using clean earth or dust.\n" +
            "* Occurrence of a state requiring ritual ablution or bath (breaking Wudu or Ghusl).\n" +
            "* Inability to use water due to scarcity, distance, or legitimate excuse."
        ));

        // Card 4: তায়াম্মুম শুদ্ধ হওয়ার শর্তাবলী
        list.add(new HajjHistoryCardItem(
            4,
            "তায়াম্মুম শুদ্ধ হওয়ার শর্তাবলী",
            "Conditions for the Validity of Tayammum",
            "* ইসলাম। * হায়েয বা নিফাসের রক্ত শেষ হওয়া। * আকল বা বুদ্ধিসম্পন্ন হওয়া। * পবিত্র মাটি পাওয়া।",
            "* Islam (being a Muslim). * Cessation of menstruation (Hayd) or post-natal bleeding (Nifas). * Having sound intellect (Aql). * Availability of clean earth.",
            "* ইসলাম (মুসলিম হওয়া)।\n" +
            "* হায়েয বা নিফাসের রক্ত শেষ হওয়া।\n" +
            "* আকল বা বুদ্ধিসম্পন্ন হওয়া।\n" +
            "* পবিত্র মাটি বা মাটিজাতীয় বস্তু পাওয়া।\n" +
            "* নিয়ত বা তায়াম্মুমের বিশুদ্ধ ইচ্ছা পোষণ করা।",
            "* Islam (being a Muslim).\n" +
            "* Cessation of menstruation (Hayd) or post-natal bleeding (Nifas).\n" +
            "* Having sound intellect and sanity.\n" +
            "* Availability of clean, pure earth or earth-based substance.\n" +
            "* Making a sincere intention (Niyyah) for purification."
        ));

        // Card 5: তায়াম্মুমের ফরজসমূহ
        list.add(new HajjHistoryCardItem(
            5,
            "তায়াম্মুমের ফরজসমূহ",
            "Fard (Obligatory Acts) of Tayammum",
            "* নিয়ত। * পবিত্র মাটি। * একবার মাটিতে হাত মারা। * মুখমণ্ডল ও হাতের তালু মাসাহ করা।",
            "* Intention (Niyyah). * Pure earth. * Striking the earth with hands. * Wiping the face and hands.",
            "* নিয়ত করা (পবিত্রতা অর্জনের উদ্দেশ্যে অন্তরে সংকল্প করা)।\n" +
            "* পবিত্র মাটি বা মাটিজাতীয় বস্তু ব্যবহার করা।\n" +
            "* উভয় হাত পবিত্র মাটিতে স্পর্শ করা বা মারা।\n" +
            "* সম্পূর্ণ মুখমণ্ডল ও হাত সুন্দরভাবে মাসাহ করা।",
            "* Making the intention (Niyyah) to purify oneself for worship.\n" +
            "* Using clean, pure earth or dust.\n" +
            "* Striking or touching the hands upon the clean earth.\n" +
            "* Completely wiping the entire face and hands/arms."
        ));

        // Card 6: তায়াম্মুমের সুন্নাতসমূহ
        list.add(new HajjHistoryCardItem(
            6,
            "তায়াম্মুমের সুন্নাতসমূহ",
            "Sunnahs of Tayammum",
            "* বিসমিল্লাহ বলা। * কিবলামুখী হওয়া। * সালাতের আদায়ের ইচ্ছা করার আগে করা * দ্বিতীয়বার মাটিতে হাত রাখা। * ধারাবাহিকতা বজায় রাখা।",
            "* Saying Bismillah. * Facing the Qiblah. * Performing before prayer time expires. * Striking the earth a second time. * Maintaining proper sequence.",
            "* বিসমিল্লাহ বলা।\n" +
            "* কিবলামুখী হওয়া।\n" +
            "* সালাতের আদায়ের ইচ্ছা করার আগে করা।\n" +
            "* দ্বিতীয়বার মাটিতে হাত রাখা।\n" +
            "* ধারাবাহিকতা বজায় রাখা (প্রথমে মুখমণ্ডল, এরপর হাতদ্বয় মাসেহ করা)।\n" +
            "* মাটিতে হাত রেখে সামান্য সামনে-পেছনে নেওয়া এবং অতিরিক্ত ধুলা ঝেড়ে ফেলা।",
            "* Saying 'Bismillah' at the beginning.\n" +
            "* Facing toward the Qiblah.\n" +
            "* Performing before attempting to offer Salah.\n" +
            "* Striking the earth a second time for wiping the arms.\n" +
            "* Maintaining proper sequence (face first, then arms).\n" +
            "* Lightly moving hands forward/backward on dust and shaking off excess."
        ));

        // Card 7: তায়াম্মুমের পদ্ধতি (Verbatim)
        list.add(new HajjHistoryCardItem(
            7,
            "তায়াম্মুমের পদ্ধতি",
            "Step-by-Step Method of Tayammum",
            "তায়াম্মুম অর্থ ‘সংকল্প করা’। পারিভাষিক অর্থে : ‘পানি না পাওয়া গেলে ওযূ বা গোসলের পরিবর্তে পাক মাটি দ্বারা পবিত্রতা অর্জনের ইসলামী পদ্ধতিকে ‘তায়াম্মুম’ বলে’। এটি মুসলিম উম্মাহর জন্য আল্লাহর অন্যতম বিশেষ অনুগ্রহ...",
            "Linguistically, Tayammum means 'to intend'. Terminologically: 'The Islamic method of attaining purification using clean earth instead of Wudu or Ghusl when water is not found'. This is one of Allah's special favors upon the Muslim Ummah...",
            "তায়াম্মুম অর্থ ‘সংকল্প করা’। পারিভাষিক অর্থে : ‘পানি না পাওয়া গেলে ওযূ বা গোসলের পরিবর্তে পাক মাটি দ্বারা পবিত্রতা অর্জনের ইসলামী পদ্ধতিকে ‘তায়াম্মুম’ বলে’। এটি মুসলিম উম্মাহর জন্য আল্লাহর অন্যতম বিশেষ অনুগ্রহ। যা ইতিপূর্বে কোন উম্মতকে দেওয়া হয়নি।\n\n" +
            "[ফিক্বহুস সুন্নাহ ১/৫৯]\n\n" +
            "আল্লাহ বলেন,\n\n" +
            "وَ اِنۡ كُنۡتُمۡ مَّرۡضٰۤی اَوۡ عَلٰی سَفَرٍ اَوۡ جَآءَ اَحَدٌ مِّنۡكُمۡ مِّنَ الۡغَآئِطِ اَوۡ لٰمَسۡتُمُ النِّسَآءَ فَلَمۡ تَجِدُوۡا مَآءً فَتَیَمَّمُوۡا صَعِیۡدًا طَیِّبًا فَامۡسَحُوۡا بِوُجُوۡهِكُمۡ وَ اَیۡدِیۡكُمۡ مِّنۡهُ\n\n" +
            "অর্থ:\n" +
            "আর যদি তোমরা পীড়িত হও কিংবা সফরে থাক অথবা পায়খানা থেকে আস কিংবা স্ত্রী স্পর্শ করে থাক, অতঃপর পানি না পাও, তাহলে তোমরা পবিত্র মাটি দ্বারা ‘তায়াম্মুম’ কর ও তা দ্বারা তোমাদের মুখমণ্ডল ও হস্তদ্বয় মাসাহ কর’।\n\n" +
            "[মায়েদা ৫/৬]\n\n" +
            "পদ্ধতি:\n" +
            "• পবিত্রতা অর্জনের নিয়তে ‘বিসমিল্লাহ’ বলতে হবে।\n" +
            "• মাটির উপর দু’হাত একবার মেরে তাতে ফুঁক দিয়ে ঝেড়ে ফেলতে হবে।\n" +
            "• এরপর মুখমণ্ডল ও দু’হাতের কব্জি পর্যন্ত একবার বুলাতে হবে।\n\n" +
            "[তিরমিযী, ইবনু মাজাহ প্রভৃতি মিশকাত হা/৪০২ ‘পবিত্রতা’ অধ্যায়-৩, অনুচ্ছেদ-৪; আবুদাঊদ হা/১০১-০২; মুত্তাফাক্ব ‘আলাইহ, মিশকাতহা/৫২৮ ‘তায়াম্মুম’ অনুচ্ছেদ-১০]",
            "Linguistically, Tayammum means 'to intend'. In Islamic terminology: 'The Islamic method of attaining ritual purity using clean earth instead of Wudu or Ghusl when water is not found'. This is one of Allah's special favors upon the Muslim Ummah, which was not granted to any previous nation.\n\n" +
            "[Fiqhus Sunnah 1/59]\n\n" +
            "Allah says:\n\n" +
            "وَ اِنۡ كُنۡتُمۡ مَّرۡضٰۤی اَوۡ عَلٰی سَفَرٍ اَوۡ جَآءَ اَحَدٌ مِّنۡكُمۡ مِّنَ الۡغَآئِطِ اَوۡ لٰمَسۡتُمُ النِّسَآءَ فَلَمۡ تَجِدُوۡا مَآءً فَتَیَمَّمُوۡا صَعِیۡدًا طَیِّبًا فَامۡسَحُوۡا بِوُجُوۡهِكُمۡ وَ اَیۡدِیۡكُمۡ مِّنۡهُ\n\n" +
            "Translation:\n" +
            "\"And if you are ill or on a journey or one of you comes from the place of relieving himself or you have contacted women and find no water, then seek clean earth and wipe over your faces and hands with it.\"\n\n" +
            "[Surah Al-Ma'idah 5:6]\n\n" +
            "Method:\n" +
            "• Say 'Bismillah' with the intention of attaining ritual purity.\n" +
            "• Strike the clean earth once with both hands and blow into them to shake off excess dust.\n" +
            "• Then wipe over the entire face and both hands up to the wrists once.\n\n" +
            "[Tirmidhi, Ibn Majah, Mishkat Hadith 402, 'Purity' Chapter-3, Section-4; Abu Dawud Hadith 101-102; Muttafaqun Alayh, Mishkat Hadith 528, 'Tayammum' Section-10]"
        ));

        return list;
    }
}
