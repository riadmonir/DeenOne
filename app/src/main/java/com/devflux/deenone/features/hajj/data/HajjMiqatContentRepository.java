package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjMiqatContentRepository {

    public static List<HajjHistoryCardItem> getMiqatCards() {
        List<HajjHistoryCardItem> items = new ArrayList<>();

        // Card 1: যুল হুলাইফা (Verbatim matching screenshot: "ইহরামের জন্য পাঁচটি মীকাত- নবী করীম (সাল্লা...")
        items.add(new HajjHistoryCardItem(
            1,
            "যুল হুলাইফা",
            "Dhul Hulaifah",
            "ইহরামের জন্য পাঁচটি মীকাত- নবী করীম (সাল্লা...",
            "Five Miqats for Ihram - The Holy Prophet (peace...",
            "ইহরামের জন্য পাঁচটি মীকাত- নবী করীম (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) মদিনাবাসী এবং মদিনা হয়ে আগমনকারীদের জন্য 'যুল হুলাইফা' মীকাত হিসেবে নির্ধারণ করেছেন।\n\n" +
            "<b>অবস্থান ও ভৌগোলিক দূরত্ব:</b>\n" +
            "এটি মদিনা মুনাওয়ারা থেকে প্রায় ১৮ কিলোমিটার দক্ষিণে এবং পবিত্র মক্কা মুকাররমা থেকে প্রায় ৪৫০ কিলোমিটার উত্তরে অবস্থিত। ভৌগোলিক দূরত্বের দিক থেকে এটি পবিত্র কাবা শরিফ থেকে সবচেয়ে দূরবর্তী মীকাত।\n\n" +
            "<b>বর্তমান পরিচিতি:</b>\n" +
            "বর্তমানে এই স্থানটি 'আবয়ার আলী' (Abyar Ali) বা 'মসজিদে শাজারাহ' (Masjid ash-Shajarah) বা 'মসজিদে যুল হুলাইফা' নামে বিশ্বজুড়ে পরিচিত ও সুসংগঠিত।\n\n" +
            "<b>শরীয়তের আবশ্যকীয় বিধান:</b>\n" +
            "মদিনা শরীফ থেকে সড়কপথে মক্কার উদ্দেশ্যে রওয়ানা হলে এই স্থান অতিক্রম করার পূর্বেই শারীরিক পরিচ্ছন্নতা অর্জন করে সেলাইবিহীন ইহরামের কাপড় পরিধান করা এবং হজ্জ্ব বা ওমরার সুস্পষ্ট নিয়ত ও তালবিয়াহ পাঠ করা ওয়াজিব। ইহরাম ছাড়া এই সীমানা অতিক্রম করা সম্পূর্ণরূপে নিষিদ্ধ।\n\n" +
            "<b>[সহীহ বুখারী: ১৫২৪, সহীহ মুসলিম: ১১৮১]</b>",
            "The Prophet Muhammad (peace and blessings be upon him) designated Dhul Hulaifah as the Miqat for the residents of Madinah and all travelers passing through it.\n\n" +
            "<b>Location & Distance:</b>\n" +
            "It is situated approximately 18 km south of Madinah and about 450 km north of Makkah. In terms of physical distance, it is the furthest Miqat from the Holy Ka'bah.\n\n" +
            "<b>Contemporary Identity:</b>\n" +
            "Today, this sacred site is widely known as Abyar Ali or Masjid ash-Shajarah, equipped with state-of-the-art facilities for pilgrims.\n\n" +
            "<b>Jurisprudential Ruling:</b>\n" +
            "Anyone journeying from Madinah to Makkah with the intention of Hajj or Umrah must enter the state of Ihram before crossing this boundary. Crossing without Ihram is strictly prohibited and incurs a compensatory sacrifice (Dam).\n\n" +
            "<b>[Sahih al-Bukhari: 1524, Sahih Muslim: 1181]</b>"
        ));

        // Card 2: জুহফা (Verbatim matching screenshot: "শাম, মরক্কো, মিশরবাসী এবং এ পথ হয়ে যারা ...")
        items.add(new HajjHistoryCardItem(
            2,
            "জুহফা",
            "Al-Juhfah",
            "শাম, মরক্কো, মিশরবাসী এবং এ পথ হয়ে যারা ...",
            "For the residents of Sham, Morocco, Egypt and those...",
            "শাম, মরক্কো, মিশরবাসী এবং এ পথ হয়ে যারা আসবেন তাদের জন্য 'জুহফা' মীকাত নির্ধারণ করেছেন রাসূলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম)।\n\n" +
            "<b>ঐতিহাসিক পটভূমি ও ভৌগোলিক অবস্থান:</b>\n" +
            "জুহফা লোহিত সাগরের উপকূলের অদূরে মক্কা মুকাররমা থেকে উত্তর-পশ্চিমে প্রায় ১৮৭ কিলোমিটার দূরে অবস্থিত। এটি প্রাচীন একটি সমৃদ্ধ নগরী ছিল। পরবর্তীতে বনু উমাইয়া আমলে মহামারী ও প্লাবনের কারণে এটি পরিত্যক্ত হয়ে যায়।\n\n" +
            "<b>বর্তমান অবস্থা ও রাবেগ মীকাত:</b>\n" +
            "জুহফার প্রাচীন জনপদটি বিরান হয়ে পড়ার পর তার সন্নিকটে অবস্থিত 'রাবেগ' (Rabigh) শহরটি এই অঞ্চলের প্রধান মীকাত হিসেবে ব্যবহৃত হতে শুরু করে। বর্তমানে জুহফার মূল স্থানে নতুন মনোরম মসজিদ ও কমপ্লেক্স পুনঃনির্মাণ করা হয়েছে।\n\n" +
            "<b>প্রযোজ্য হাজীদের বিবরণ:</b>\n" +
            "সিরিয়া, জর্ডান, ফিলিস্তিন, লেবানন, মিশর, সুদান এবং উত্তর আফ্রিকার মরক্কো, আলজেরিয়া, তিউনিসিয়া থেকে স্থল, সমুদ্র বা বিমানপথে যারা লোহিত সাগরের দিক দিয়ে মক্কায় আগমন করেন, তাদের সবাইকে জুহফা বা রাবেগ বরাবর পৌঁছানোর পূর্বেই ইহরাম গ্রহণ করতে হয়।\n\n" +
            "<b>[সহীহ বুখারী: ১৫২৪, সুনানে আবু দাউদ: ১৭৩৭]</b>",
            "The Messenger of Allah (ﷺ) appointed Al-Juhfah as the designated Miqat for the inhabitants of Sham (Greater Syria), Morocco, Egypt, and everyone traveling along their routes.\n\n" +
            "<b>Historical Context & Location:</b>\n" +
            "Al-Juhfah is located approximately 187 km northwest of Makkah near the Red Sea coast. It was once a thriving settlement before being devastated by epidemics and flooding during the Umayyad era.\n\n" +
            "<b>Modern Status & Rabigh:</b>\n" +
            "Historically, the nearby coastal town of Rabigh served as the practical Miqat. In recent years, a massive modern mosque complex has been reconstructed at the original Al-Juhfah site.\n\n" +
            "<b>Applicable Pilgrims:</b>\n" +
            "Pilgrims arriving from Syria, Jordan, Palestine, Lebanon, Egypt, Sudan, Morocco, Algeria, and Tunisia via maritime or overland routes must assume Ihram before crossing this latitude.\n\n" +
            "<b>[Sahih al-Bukhari: 1524, Sunan Abi Dawud: 1737]</b>"
        ));

        // Card 3: কারনুল মানাজিল (Verbatim matching screenshot: "নজদ বাসী এবং এ পথে যারা আসবেন তাদের আ...")
        items.add(new HajjHistoryCardItem(
            3,
            "কারনুল মানাজিল",
            "Qarn al-Manazil",
            "নজদ বাসী এবং এ পথে যারা আসবেন তাদের আ...",
            "For the residents of Najd and those who arrive...",
            "নজদ বাসী এবং এ পথে যারা আসবেন তাদের জন্য 'কারনুল মানাজিল' মীকাত নির্ধারিত।\n\n" +
            "<b>ভৌগোলিক অবস্থান ও দূরত্ব:</b>\n" +
            "এটি পবিত্র মক্কা মুকাররমা থেকে পূর্ব দিকে প্রায় ৭৮ কিলোমিটার দূরে অবস্থিত একটি পাহাড়ি উপত্যকা। ভৌগোলিক নৈকট্যের দিক থেকে এটি মক্কার অন্যতম নিকটবর্তী মীকাত।\n\n" +
            "<b>বর্তমান পরিচিতি:</b>\n" +
            "বর্তমানে এই স্থানটি 'আস-সাইলুল কাবীর' (As-Sail Al-Kabeer) নামে সুপরিচিত। এখানে রয়েছে বিশাল শীতাতপ নিয়ন্ত্রিত দৃষ্টিনন্দন মসজিদ, ওজুখানা এবং হাজার হাজার হাজীর জন্য গোসল ও ইহরামের আধুনিকতম অবকাঠামো।\n\n" +
            "<b>কারা এখান থেকে ইহরাম বাঁধবেন:</b>\n" +
            "সৌদি আরবের রিয়াদ, কাসিম, দাহরান, দাম্মামসহ সমগ্র নজদ অঞ্চল, উপসাগরীয় দেশসমূহ (সংযুক্ত আরব আমিরাত, কাতার, কুয়েত, বাহরাইন, ওমান) থেকে স্থলপথে আগত সকল যাত্রী এখান থেকে ইহরাম পরিধান ও নিয়ত করেন।\n\n" +
            "<b>[সহীহ বুখারী: ১৫২৪, সহীহ মুসলিম: ১১৮১]</b>",
            "Prophet Muhammad (ﷺ) fixed Qarn al-Manazil as the Miqat for the people of Najd and all travelers passing through their route.\n\n" +
            "<b>Geographical Location:</b>\n" +
            "Located about 78 km east of Makkah, it is one of the closest primary Miqat stations to the Holy Sanctuary.\n\n" +
            "<b>Contemporary Identity:</b>\n" +
            "Today, it is widely known as As-Sail Al-Kabeer, equipped with a grand mosque, extensive bathing quarters, and modern pilgrim amenities.\n\n" +
            "<b>Applicable Pilgrims:</b>\n" +
            "Pilgrims originating from Riyadh, Dammam, and the GCC countries (UAE, Qatar, Kuwait, Bahrain, and Oman) who travel overland cross this boundary.\n\n" +
            "<b>[Sahih al-Bukhari: 1524, Sahih Muslim: 1181]</b>"
        ));

        // Card 4: ওয়াদি মুহাররাম (Verbatim matching screenshot: "ইয়ামানবাসী এবং তাদের পথে যারা আসবেন তা...")
        items.add(new HajjHistoryCardItem(
            4,
            "ওয়াদি মুহাররাম",
            "Wadi Muharram",
            "ইয়ামানবাসী এবং তাদের পথে যারা আসবেন তা...",
            "For the residents of Yemen and those arriving via...",
            "ইয়ামানবাসী এবং তাদের পথে যারা আসবেন তাদের জন্য ওয়াদি মুহাররাম সমান্তরাল মীকাতের মর্যাদা রাখে।\n\n" +
            "<b>অবস্থান ও কৌশলগত গুরুত্ব:</b>\n" +
            "ওয়াদি মুহাররাম তায়েফ শহরের প্রবেশদ্বারে আল-হাদা (Al-Hada) পাহাড়ি হাইওয়েতে অবস্থিত। সমুদ্রপৃষ্ঠ থেকে প্রায় ২০০০ মিটার উচ্চতায় অবস্থিত এই স্থানটি কারনুল মানাজিল মীকাতেরই সমান্তরাল একটি উচ্চভূমি স্টেশন।\n\n" +
            "<b>স্থাপত্য ও সুবিধাসমূহ:</b>\n" +
            "এখানে রয়েছে সৌদি সরকারের নির্মিত সুবিশাল ঐতিহাসিক 'মিকাত আল-ওয়াদি আল-মুহাররাম মসজিদ'। এটি তায়েফ-মক্কা কেবল কার (টেলিফেরিক) ও আল-হাদা সড়কের সংযোগস্থলে অবস্থিত।\n\n" +
            "<b>কারা এখান থেকে ইহরাম গ্রহণ করবেন:</b>\n" +
            "তায়েফ নগরী হয়ে যারা পাহাড়ি পথে মক্কায় অবতরণ করেন অথবা দক্ষিণ দিক থেকে তায়েফ রুট ব্যবহার করে আসেন, তারা এই ওয়াদি মুহাররাম থেকে ইহরামের আনুষ্ঠানিকতা সম্পন্ন করেন।\n\n" +
            "<b>[ফাতাওয়া আল-লাজনাহ আদ-দাইমাহ: ১১/১৩১]</b>",
            "Wadi Muharram serves as the elevated twin Miqat aligned with Qarn al-Manazil for travelers passing through the highlands.\n\n" +
            "<b>Location & Strategic Prominence:</b>\n" +
            "Perched atop the Al-Hada mountain road near Taif at an altitude of approximately 2,000 meters above sea level, it stands parallel to Qarn al-Manazil.\n\n" +
            "<b>Modern Facilities:</b>\n" +
            "It houses the magnificent Grand Miqat Wadi Muharram Mosque complex along the scenic mountain corridor.\n\n" +
            "<b>Target Pilgrims:</b>\n" +
            "Travelers descending from Taif or traveling overland through the southern high plateau must enter Ihram at this station.\n\n" +
            "<b>[Fatawa al-Lajnah ad-Da'imah: 11/131]</b>"
        ));

        // Card 5: ইয়ালামলাম (Verbatim matching screenshot: "ইরাক বাসীদের এবং এ পথে যারা আসবেন তাদের...")
        items.add(new HajjHistoryCardItem(
            5,
            "ইয়ালামলাম",
            "Yalamlam",
            "ইরাক বাসীদের এবং এ পথে যারা আসবেন তাদের...",
            "For the residents of Iraq and those who arrive...",
            "ইরাক বাসীদের এবং এ পথে যারা আসবেন তাদের এবং দক্ষিণ অঞ্চল দিয়ে আগমনকারীদের জন্য 'ইয়ালামলাম' মীকাত নির্ধারিত।\n\n" +
            "<b>ভৌগোলিক অবস্থান:</b>\n" +
            "পবিত্র মক্কা মুকাররমা থেকে দক্ষিণ-পূর্ব দিকে প্রায় ১০০ থেকে ১৩০ কিলোমিটার দূরে অবস্থিত একটি পাহাড়ি উপত্যকা। বর্তমানে এর প্রধান কেন্দ্রটি 'আস-সাদিয়া' (As-Sadiyah) নামে পরিচিত।\n\n" +
            "<b>বাংলাদেশ ও দক্ষিণ এশীয় বিমানযাত্রীদের মীকাত:</b>\n" +
            "বাংলাদেশ, ভারত, পাকিস্তানসহ দক্ষিণ ও পূর্ব এশিয়া থেকে জেদ্দা কিং আব্দুল আজিজ আন্তর্জাতিক বিমানবন্দরে পৌঁছানোর ফ্লাইটে বিমান এই ইয়ালামলাম মীকাতের আকাশসীমা দিয়ে অতিক্রম করে। বিমান মীকাত বরাবর পৌঁছার ২০-৩০ মিনিট পূর্বে পাইলট বা কেবিন ক্রু ঘোষণা প্রদান করেন। ঘোষণার সাথে সাথে বা বিমানে ওঠার পূর্বেই ইহরামের পোশাক পরা এবং ঘোষণার মুহূর্তে মুখে স্পষ্ট নিয়ত ও তালবিয়াহ পাঠ করা ওয়াজিব।\n\n" +
            "<b>সতর্কতা:</b>\n" +
            "জেদ্দা বিমানবন্দর মীকাতের ভেতরে অবস্থিত, তাই জেদ্দায় নেমে ইহরাম বাঁধার কোনো সুযোগ নেই। মীকাত পেরিয়ে গেলে কাফফারা স্বরূপ দম (একটি কোরবানি) দেওয়া বাধ্যতামূলক।\n\n" +
            "<b>[সহীহ বুখারী: ১৫২৪, সহীহ মুসলিম: ১১৮২]</b>",
            "The Holy Prophet (ﷺ) appointed Yalamlam as the Miqat for those arriving from the southern corridors and maritime routes.\n\n" +
            "<b>Geographical Location:</b>\n" +
            "Located about 100 to 130 km southeast of Makkah in a mountainous valley, the modern center is known as As-Sadiyah.\n\n" +
            "<b>Vital Importance for Air Travelers:</b>\n" +
            "Flights from Bangladesh, India, Pakistan, and Southeast Asia heading to Jeddah King Abdulaziz International Airport cross directly over the Yalamlam airspace. Pilgrims must assume Ihram before or upon the pilot's airborne announcement.\n\n" +
            "<b>Crucial Caution:</b>\n" +
            "Jeddah lies inside the Miqat perimeter. Assuming Ihram after landing in Jeddah is a major violation requiring a compensatory sacrifice (Dam).\n\n" +
            "<b>[Sahih al-Bukhari: 1524, Sahih Muslim: 1182]</b>"
        ));

        // Card 6: যাতু ইরক (Verbatim matching screenshot: "যিনি হজ্জ্ব অথবা ওমরার নিয়ত করবেন তার জন্য ...")
        items.add(new HajjHistoryCardItem(
            6,
            "যাতু ইরক",
            "Dhat 'Irq",
            "যিনি হজ্জ্ব অথবা ওমরার নিয়ত করবেন তার জন্য ...",
            "Whoever makes intention for Hajj or Umrah, for him...",
            "যিনি হজ্জ্ব অথবা ওমরার নিয়ত করবেন তার জন্য 'যাতু ইরক' নির্ধারিত একটি ঐতিহাসিক মীকাত।\n\n" +
            "<b>অবস্থান ও দূরত্ব:</b>\n" +
            "এটি পবিত্র মক্কা মুকাররমা থেকে উত্তর-পূর্ব দিকে প্রায় ৯০ থেকে ১০০ কিলোমিটার দূরে অবস্থিত একটি মরু উপত্যকা। একে 'ইরক' নামক একটি অনুচ্চ পাহাড়ের কারণে যাতু ইরক নামকরণ করা হয়েছে। বর্তমানে স্থানটি 'আদ-দাহরা' (Ad-Dharibah) নামে পরিচিত।\n\n" +
            "<b>ঐতিহাসিক নির্ধারণ ও সুন্নাহ দলিল:</b>\n" +
            "ইরাকের কুফা ও বসরা অঞ্চলের মুসলমানরা হযরত উমর ইবনুল খাত্তাব (রা.)-এর নিকট আবেদন করলেন—'হে আমীরুল মুমিনীন! কারনুল মানাজিল আমাদের সাধারণ ভ্রমণপথ থেকে সরে পড়েছে এবং সেখানে যাওয়া আমাদের জন্য কষ্টসাধ্য।' তখন হযরত উমর (রা.) কা'বার অবস্থানের সাথে সামঞ্জস্য রেখে তাদের চলাচলের পথে 'যাতু ইরক'কে মীকাত হিসেবে চিহ্নিত করে দেন। সুনানে আবু দাউদে স্বয়ং রাসূলুল্লাহ (ﷺ) কর্তৃকও এটিকে নির্ধারণের সহীহ প্রমাণ রয়েছে।\n\n" +
            "<b>কারা এখান থেকে ইহরাম বাঁধবেন:</b>\n" +
            "ইরাক, ইরান এবং মধ্য এশিয়া ও কাস্পিয়ান অঞ্চল থেকে স্থলপথে আগত পুণ্যার্থীরা এই স্থান থেকে ইহরামের আনুষ্ঠানিকতা সম্পন্ন করেন।\n\n" +
            "<b>[সহীহ বুখারী: ১৫৩১, সুনানে আবু দাউদ: ১৭৩৯]</b>",
            "Dhat 'Irq is an established historic Miqat for all pilgrims journeying from the northeastern regions.\n\n" +
            "<b>Location & Naming:</b>\n" +
            "Situated about 90 to 100 km northeast of Makkah, named after a prominent low hillock ('Irq) in the valley. Today it is also referred to as Ad-Dharibah.\n\n" +
            "<b>Historical Designation:</b>\n" +
            "When the people of Iraq came to Umar ibn al-Khattab (RA) explaining that Qarn al-Manazil was off their travel route, Umar (RA) assessed the parallel boundary and established Dhat 'Irq, which is also confirmed by authentic Sunnah narrations.\n\n" +
            "<b>Target Pilgrims:</b>\n" +
            "Pilgrims arriving from Iraq, Iran, and Central Asia via overland routes enter Ihram here.\n\n" +
            "<b>[Sahih al-Bukhari: 1531, Sunan Abi Dawud: 1739]</b>"
        ));

        return items;
    }
}
