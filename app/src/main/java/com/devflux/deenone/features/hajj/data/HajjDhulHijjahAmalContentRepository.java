package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjDhulHijjahAmalCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjDhulHijjahAmalContentRepository {

    public static List<HajjDhulHijjahAmalCardItem> getDhulHijjahAmalCards() {
        List<HajjDhulHijjahAmalCardItem> list = new ArrayList<>();

        // 1. তাওবা
        list.add(new HajjDhulHijjahAmalCardItem(
            1,
            "তাওবা",
            "Tawbah (Sincere Repentance)",
            "তাওবা অর্থ ফিরে আসা বা প্রত্যাবর্তন করা। আল্লাহ ...",
            "Tawbah means turning back or returning to Allah in sincere repentance...",
            "তাওবা অর্থ ফিরে আসা বা প্রত্যাবর্তন করা। আল্লাহ তা'আলার সন্তুষ্টি ও রহমত লাভের উদ্দেশ্যে সকল পাপ ও ভুলত্রুটি থেকে খাঁটি মনে ক্ষমা চেয়ে আল্লাহর দিকে ফিরে আসা প্রতিটি মুমিনের প্রধান দায়িত্ব। জিলহজের এই বরকতময় দিনগুলোতে বেশি বেশি তাওবা ও ইস্তিগফার করা অত্যন্ত ফযিলতপূর্ণ।",
            "Tawbah literally means turning back or returning to Allah. Seeking sincere forgiveness from all sins and returning to Allah to attain His pleasure and mercy is a fundamental duty of every believer. Engaging in abundant repentance and Istighfar during these blessed days of Dhul Hijjah carries immense reward."
        ));

        // 2. ফরয ও নফল সালাতগুলো গুরুত্বের সাথে আদায় করা
        list.add(new HajjDhulHijjahAmalCardItem(
            2,
            "ফরয ও নফল সালাতগুলো গুরুত্বের সাথে আদায় করা",
            "Performing Obligatory & Voluntary Prayers with Care",
            "অর্থাৎ ফরয ও ওয়াজিবসমূহ সময়-মত সুন্দর ও পরি...",
            "That is, performing obligatory and voluntary prayers in their prescribed times with perfection...",
            "ফরয ও ওয়াজিবসমূহ সময়-মত সুন্দর ও পরিপূর্ণ খুশু-খুজুর সাথে আদায় করা এবং সাথে সাথে বেশি বেশি নফল সালাত, তাহাজ্জুদ, চাশত ও সুন্নতে মুয়াক্কাদাগুলোর প্রতি বিশেষ যত্নবান হওয়া।",
            "Performing obligatory and emphasized prayers punctually with full devotion and tranquility, alongside observing voluntary prayers like Tahajjud, Chasht (Duha), and Sunnah prayers."
        ));

        // 3. সিয়াম পালন-রোজা রাখা
        list.add(new HajjDhulHijjahAmalCardItem(
            3,
            "সিয়াম পালন-রোজা রাখা",
            "Observing Fasts during First Ten Days",
            "যিলহজ মাসের প্রথম দশ দিনের রোজা রাখা একটি ...",
            "Fasting during the first nine days of Dhul Hijjah is a great Sunnah...",
            "যিলহজ মাসের প্রথম নয় দিন নফল রোজা রাখা অত্যন্ত মর্যাদাপূর্ণ ও ফযিলতপূর্ণ ইবাদত। বিশেষ করে প্রথম ৯টি দিন রোজা পালনের ব্যাপারে হাদিসে বিশেষ উৎসাহ দেওয়া হয়েছে।",
            "Fasting during the first nine days of Dhul Hijjah is an exceptional and highly rewarding act of worship encouraged by the Sunnah."
        ));

        // 4. হজ ও ওমরা করা
        list.add(new HajjDhulHijjahAmalCardItem(
            4,
            "হজ ও ওমরা করা",
            "Performing Hajj & Umrah",
            "নবী সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম এ দুটি মর্যাদাপূর্ণ ই...",
            "The Prophet (ﷺ) described these two noble acts of worship...",
            "নবী সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম এ দুটি মর্যাদাপূর্ণ ইবাদতকে জীবনের অন্যতম শ্রেষ্ঠ আমল হিসেবে ঘোষণা করেছেন। সামর্থ্যবান ব্যক্তির জন্য জীবনে একবার হজ ফরজ এবং ওমরাহ সুন্নাত।",
            "The Prophet (ﷺ) declared these two noble rites among the greatest deeds in Islam. Hajj is obligatory once in a lifetime for capable believers, and Umrah is an established Sunnah."
        ));

        // 5. আল্লাহর যিকির করা
        list.add(new HajjDhulHijjahAmalCardItem(
            5,
            "আল্লাহর যিকির করা",
            "Abundant Remembrance (Dhikr) of Allah",
            "এ দিনসমূহে অন্যান্য আমলের মাঝে যিকিরের এক বি...",
            "Among all good deeds in these days, Dhikr holds a special place...",
            "এ দিনসমূহে অন্যান্য আমলের মাঝে যিকিরের এক বিশেষ মর্যাদা রয়েছে। সুবহানাল্লাহ, আলহামদুলিল্লাহ, লা ইলাহা ইল্লাল্লাহ ও আল্লাহু আকবার বেশি বেশি পাঠ করে জবাকে সিক্ত রাখা।",
            "Among all deeds during these blessed days, remembrance of Allah holds exceptional merit. One should constantly moisten the tongue with SubhanAllah, Alhamdulillah, La ilaha illallah, and Allahu Akbar."
        ));

        // 6. তাকবীর, তাহলীল ও তাহমীদ
        list.add(new HajjDhulHijjahAmalCardItem(
            6,
            "তাকবীর, তাহলীল ও তাহমীদ",
            "Takbeer, Tahleel & Tahmeed",
            "এ দিনগুলোতে আল্লাহ রাব্বুল আলামিনের মহত্ব ঘোষ...",
            "In these days, proclaiming the greatness and praise of Allah...",
            "এ দিনগুলোতে আল্লাহ রাব্বুল আলামিনের মহত্ব ঘোষণা করা, তাকবীর (আল্লাহু আকবার), তাহলীল (লা ইলাহা ইল্লাল্লাহ) ও তাহমীদ (আলহামদুলিল্লাহ) সর্বত্র বেশি বেশি উচ্চারণ করা সুন্নত।",
            "Proclaiming the greatness of Allah through Takbeer (Allahu Akbar), Tahleel (La ilaha illallah), and Tahmeed (Alhamdulillah) throughout these days is an established prophetic Sunnah."
        ));

        // 7. আরাফার দিন রোজা রাখা
        list.add(new HajjDhulHijjahAmalCardItem(
            7,
            "আরাফার দিন রোজা রাখা",
            "Fasting on the Day of Arafah (9th Dhul Hijjah)",
            "হজ পালনকারী ছাড়া অন্যদের জন্য আরাফার দিন ...",
            "For non-pilgrims, fasting on the Day of Arafah...",
            "হজ পালনকারী ছাড়া অন্যদের জন্য আরাফার দিন (৯ই জিলহজ) রোজা রাখা বিগত এক বছর ও আগামী এক বছরের গুনাহের কাফফারা স্বরূপ।",
            "For Muslims not performing Hajj, fasting on the Day of Arafah (9th of Dhul Hijjah) expiates the sins of the previous year and the coming year."
        ));

        // 8. কুরবানির দিন তথা দশ তারিখের আমল
        list.add(new HajjDhulHijjahAmalCardItem(
            8,
            "কুরবানির দিন তথা দশ তারিখের আমল",
            "Deeds on the 10th Day (Day of Sacrifice)",
            "এ দিনের একটি নাম হল ইয়াওমুল হজ্জিল আকবর বা...",
            "This day is named Yawm al-Hajj al-Akbar (The Greatest Day of Pilgrimage)...",
            "এ দিনের একটি নাম হল ইয়াওমুল হজ্জিল আকবর বা মহা হজের দিন। এই দিনে ঈদের সালাত আদায়, কুরবানি করা এবং হজের প্রধান রুকনসমূহ সম্পন্ন করা হয়।",
            "This momentous day is termed Yawm al-Hajj al-Akbar. It encompasses the Eid prayer, offering the sacrificial Qurbani, and major rites of Hajj."
        ));

        // 9. কুরবানি করা
        list.add(new HajjDhulHijjahAmalCardItem(
            9,
            "কুরবানি করা",
            "Offering Sacrificial Animal (Qurbani / Udhiyah)",
            "কুরবানি বলা হয় ঈদুল আজহার দিনগুলোতে নির্দিষ্ট ...",
            "Qurbani refers to sacrificing a prescribed animal during Eid al-Adha...",
            "কুরবানি বলা হয় ঈদুল আজহার দিনগুলোতে নির্দিষ্ট পশু আল্লাহর সন্তুষ্টির উদ্দেশ্যে জবেহ করা। সামর্থ্যবান ব্যক্তির জন্য কুরবানি করা অন্যতম প্রধান ওয়াজিব বিধান।",
            "Qurbani (Udhiyah) is the ritual slaughtering of a prescribed animal during the days of Eid al-Adha exclusively seeking the pleasure of Allah. It is an emphasized religious obligation for every capable Muslim."
        ));

        return list;
    }
}
