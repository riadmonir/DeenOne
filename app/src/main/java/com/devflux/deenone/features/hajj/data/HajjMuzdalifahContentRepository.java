package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjMuzdalifahContentRepository {

    public static List<HajjHistoryCardItem> getMuzdalifahCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. মুযদালিফার দিন করণীয়
        list.add(new HajjHistoryCardItem(
            1,
            "মুযদালিফার দিন করণীয়",
            "Duties on the Day & Night of Muzdalifah",
            "হজ্বীদের কাফেলাসমূহ আল্লাহর উপর ভরসা করে মাশ'আরে হারাম বা মুযদালিফার দিকে রওয়ানা দিবে। মুযদালিফা পৌঁছেই বিলম্ব না করে এক আজান ও দুই ইক্বামাতের মাধ্যমে মাগরিব ও এশার সালাত (নামায) একত্রে ও কসর করে আদায় করে নিবে। আল্লাহর যি...",
            "Pilgrim convoys proceed towards Al-Mash'ar Al-Haram or Muzdalifah relying upon Allah. Upon reaching Muzdalifah without delay, they perform Maghrib and Isha prayers combined and shortened with one Adhan and two Iqamahs...",
            "হজ্বীদের কাফেলাসমূহ আল্লাহর উপর ভরসা করে মাশ'আরে হারাম বা মুযদালিফার দিকে রওয়ানা দিবে। মুযদালিফা পৌঁছেই বিলম্ব না করে এক আজান ও দুই ইক্বামাতের মাধ্যমে মাগরিব ও এশার সালাত (নামায) একত্রে ও কসর করে আদায় করে নিবে। আল্লাহর যিকির করে এবং তিনি যে আরাফার ময়দানে হাজির হওয়ার তৌফিক দিয়েছেন ময়দানে হাজির হওয়ার তৌফীক্ব দিয়েেছন সেজন্য তাঁর শুক- রিয়া আদায় করে মুযদালিফায় রাত কাটাবে।",
            "Pilgrim convoys proceed towards Al-Mash'ar Al-Haram or Muzdalifah relying upon Allah. Upon reaching Muzdalifah without delay, they perform Maghrib and Isha prayers combined and shortened with one Adhan and two Iqamahs. They spend the night in Muzdalifah engaging in the remembrance of Allah and thanking Him for granting them the opportunity to stand on the plains of Arafah."
        ));

        // 2. মুযদালিফায় পৌঁছানোর পর কিছু ভুল
        list.add(new HajjHistoryCardItem(
            2,
            "মুযদালিফায় পৌঁছানোর পর কিছু ভুল",
            "Common Mistakes After Reaching Muzdalifah",
            "কোন কোন হাজ্বী মুযদালিফায় পৌঁছে এমন কিছু ভুল করে থাকেন, যা থেকে সতর্ক করা উচিৎ। তন্মধ্যে বিশেষভাবে উল্লেখযোগ্য হলো-...",
            "Some pilgrims make certain mistakes upon arriving at Muzdalifah which should be cautioned against. Among them notably:...",
            "কোন কোন হাজ্বী মুযদালিফায় পৌঁছে এমন কিছু ভুল করে থাকেন, যা থেকে সতর্ক করা উচিৎ। তন্মধ্যে বিশেষভাবে উল্লেখযোগ্য হলো-\n\n•মুৎসালিফার দিকে মুযদালিফা পৌঁছে মাগরিব ও এশার সালাত (নামায) একত্রে ও কসর করেআদায় করার পুর্বেই কঙ্কর সংগ্রহে ব্যস্ত হয়ে পড়েন।\n\n•তাঁরা এমনটি বিশ্বাস করেন যে, মুযদালিফা থেকেই কঙ্কর সংগ্রহ করতে হবে।\n\n•কঙ্করগুলো ধুয়ে নেন। অথচ রাসূল (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) এমনটি করেননি।\n\nপূর্বেই উল্লেখ করা হয়েছে, মুযদালিফায় দশ তারিখ ফজর পর্যন্ত থাকা সুন্নাত। তবে মহিলা, দুর্বল, শিশু এবং এদের দায়িত্বে যারা থাকবেন তাদের জন্য মধ্যরাতের পর মিনার উদ্দেশ্যে রওয়ানা হওয়ার অনুমতি আছেে।",
            "Some pilgrims make certain mistakes upon arriving at Muzdalifah which should be cautioned against. Among them notably:\n\n• Becoming busy collecting pebbles before performing the combined and shortened Maghrib and Isha prayers.\n\n• Believing that pebbles must only be collected strictly from Muzdalifah.\n\n• Washing the pebbles, whereas the Messenger of Allah (peace and blessings be upon him) did not do so.\n\nAs previously mentioned, remaining in Muzdalifah until Fajr of the 10th is Sunnah. However, women, the weak, children, and those responsible for their care are permitted to depart towards Mina after midnight."
        ));

        // 3. মুজদালিফায় ফজরের সালাত আদায়ের পর
        list.add(new HajjHistoryCardItem(
            3,
            "মুজদালিফায় ফজরের সালাত আদায়ের পর",
            "After Performing Fajr Prayer at Muzdalifah",
            "ফজরের সালাত আদায়ের পর হাজ্বী সাহেবের জন্য মুস্তাহাব হল-...",
            "After offering the Fajr prayer, it is Mustahabb (recommended) for the pilgrim:...",
            "ফজরের সালাত আদায়ের পর হাজ্বী সাহেবের জন্য মুস্তাহাব হল-\n\nমাশ'আরে হারামে (মুযদালিফার একটি পাহাড়)'র নিকট অথবা মুযদালিফার যেকোন স্থানে কিবলামুখী হয়ে দাঁড়িয়ে বেশী বেশী তাকবীর বলা, আল্লাহর যিকির-আযকার, তা- সবীহ-তাহলীল ও দোয়া করতে থাকা। এরপর সূর্যোদয়ের পূর্বেই মিনার উদ্দেশ্যে মুযদালিফা ত্যাগ করবেন। পথে বড় জামরায় নিক্ষেপ করার জন্য ছোলার চেয়ে সামান্য বড় সাতটি কঙ্কর সংগ্রহ করবেন। বাকী কঙ্কর মিনা থেকেই সংগ্রহহ করবে।\n\nঅতঃপর আল্লাহর উপর ভরসা করে আবেগসহকারে তালবিয়া পড়তে পড়তে এবং আল্লাহর যিকির করতে করতে মিনা অভিমুখে চলতে থাকবে।",
            "After offering the Fajr prayer, it is Mustahabb (recommended) for the pilgrim:\n\nTo stand facing the Qiblah near Al-Mash'ar Al-Haram (a mountain in Muzdalifah) or anywhere in Muzdalifah, abundantly proclaiming Takbir, engaging in Dhikr, Tasbeeh, Tahleel, and heartfelt supplication. Then, before sunrise, depart Muzdalifah for Mina. On the way, collect seven pebbles slightly larger than chickpeas for stoning the Great Jamarah. The remaining pebbles may be collected from Mina.\n\nThen, relying upon Allah, proceed towards Mina while passionately reciting the Talbiyah and remembering Allah."
        ));

        return list;
    }
}
