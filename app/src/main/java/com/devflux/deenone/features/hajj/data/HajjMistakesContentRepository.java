package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjMistakesCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjMistakesContentRepository {

    public static List<HajjMistakesCardItem> getMistakesCards() {
        List<HajjMistakesCardItem> list = new ArrayList<>();

        // 1. মীকাত ও ইহরাম বিষয়ক ভুল
        list.add(new HajjMistakesCardItem(
            1,
            "মীকাত ও ইহরাম বিষয়ক ভুল",
            "Mistakes Regarding Miqat & Ihram",
            "হজ্জ কিংবা ওমরার নিয়ত থাকা সত্ত্বেও এহরাম না...",
            "Passing the Miqat without assuming Ihram despite having the intention for Hajj or Umrah...",
            "হজ্জ কিংবা ওমরার নিয়ত থাকা সত্ত্বেও মীকাত অতিক্রম করার সময় ইহরাম না বেঁধে পরবর্তী স্থানে গিয়ে ইহরাম বাঁধা একটি মারাত্মক ভুল। মীকাত অতিক্রমের পূর্বেই ইহরাম বাঁধা ওয়াজিব। এছাড়া ইহরাম অবস্থায় নিষিদ্ধ কাজ যেমন সুগন্ধি ব্যবহার, সেলাইযুক্ত কাপড় (পুরুষদের জন্য) পরিধান ইত্যাদি এড়িয়ে চলা আবশ্যক।",
            "Crossing the designated Miqat without entering into the state of Ihram despite intending Hajj or Umrah is a major error. Assuming Ihram before or at the Miqat is mandatory (Wajib). In addition, one must strictly refrain from all Ihram prohibitions such as applying perfume, wearing stitched garments (for men), and cutting nails or hair."
        ));

        // 2. তালবিয়া পাঠের ক্ষেত্রে ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            2,
            "তালবিয়া পাঠের ক্ষেত্রে ভুলত্রুটি",
            "Mistakes Regarding Talbiyah Recitation",
            "অনেকে দলবদ্ধভাবে একই স্বরে তালবিয়া পাঠ ক...",
            "Chanting Talbiyah collectively in unison under a chorus leader...",
            "অনেকে দলবদ্ধভাবে একজনের নেতৃত্বে কোরাস বা সমস্বরে তালবিয়া পাঠ করে থাকেন, যা সুন্নাহ সম্মত নয়। নিয়ম হলো প্রত্যেকে নিজ নিজ কণ্ঠে স্বতঃস্ফূর্তভাবে আল্লাহর দরবারে তালবিয়া পাঠ করবেন। এছাড়া প্রথম কঙ্কর নিক্ষেপের পর তালবিয়া বন্ধ করা সুন্নাত।",
            "Many pilgrims recite Talbiyah collectively in unison behind a guide, which is contrary to the established Sunnah. The prescribed practice is for each individual pilgrim to proclaim Talbiyah individually and earnestly in their own voice. Furthermore, Talbiyah should cease upon casting the first pebble at Jamrat al-Aqabah."
        ));

        // 3. অন্যান্য ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            3,
            "অন্যান্য ভুলত্রুটি",
            "Miscellaneous Common Mistakes",
            "হারাম সীমানার বাইরে ‘হাদী’ জবেহ করা। কুরবা...",
            "Slaughtering the Hady sacrificial animal outside the sacred Haram boundaries...",
            "হারাম সীমানার বাইরে ‘হাদী’ বা দমের পশু জবেহ করা বৈধ নয়; হজের ওয়াজিব কুরবানি ও ফিদয়া অবশ্যই হারামের সীমানার ভেতরেই সম্পন্ন করতে হবে। এছাড়া অপর হাজীদের সাথে অসদাচরণ, ঝগড়া-বিবাদ বা অনর্থক কথাবার্তা পরিহার করা জরুরি।",
            "Slaughtering the Hady sacrificial animal outside the sacred Haram territory is invalid; all mandatory sacrifices and Dam must take place strictly within the Haram sanctuary limits. In addition, arguing, quarreling, or engaging in frivolous behavior harms the reward of Hajj."
        ));

        // 4. হেরেমে শরীফে প্রবেশের সময় ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            4,
            "হেরেমে শরীফে প্রবেশের সময় ভুলত্রুটি",
            "Mistakes Upon Entering the Sacred Mosque (Haram)",
            "হেরেমে শরীফে প্রবেশের সময় অনেক হাজি এমন...",
            "Entering Masjid al-Haram with incorrect beliefs regarding specific gates...",
            "হেরেমে শরীফে প্রবেশের সময় অনেকে মনে করেন নির্দিষ্ট কোনো দরজা (যেমন বাবুল উমরাহ বা বাবে সালাম) দিয়েই কেবল প্রবেশ করতে হবে—এটা ঠিক নয়, যেকোনো সুবিধাজনক দরজা দিয়ে প্রবেশ করা যায়। প্রবেশের সময় ডান পা দিয়ে মাসনূন দোয়া পাঠ করে প্রবেশ করা সুন্নাত।",
            "Some pilgrims erroneously believe one must enter Masjid al-Haram through a specific gate (such as Bab as-Salam). In reality, entering through any accessible gate is completely valid. One should enter with the right foot while reciting the prophetic supplication for entering the mosque."
        ));

        // 5. তাওয়াফের সময় ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            5,
            "তাওয়াফের সময় ভুলত্রুটি",
            "Mistakes During Circumambulation (Tawaf)",
            "তাওয়াফের প্রত্যেক চক্করের জন্য বিশেষ কোনো ...",
            "Believing there are specific prescribed Duas for each individual circuit...",
            "তাওয়াফের প্রত্যেক চক্করের জন্য মনগড়া বিশেষ কোনো দোয়ার বই নির্দিষ্ট মনে করা ঠিক নয়। তাওয়াফের সময় কুরআন তিলাওয়াত, জিকির ও নিজের ভাষায় যেকোনো নেক দোয়া করা যায়। এছাড়া হাতিমের (হিজরে ইসমাইল) ভেতরের অংশ দিয়ে তাওয়াফ করলে সে চক্কর আদায় হবে না, কারণ হাতিম কাবার অন্তর্ভুক্ত।",
            "Believing that there are rigid, obligatory supplications designated for each round of Tawaf is a common error. One may supplicate with any righteous Dua, Quranic verse, or Dhikr. Furthermore, passing inside the Hatim (Hijr Ismail) invalidates that round, as the Hatim is physically part of the Kaaba."
        ));

        // 6. সাঈ করার সময় ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            6,
            "সাঈ করার সময় ভুলত্রুটি",
            "Mistakes During Sa'i between Safa & Marwah",
            "সাঈর নিয়ত মুখে উচ্চারণ করে পড়া। মারওয়া পা...",
            "Verbally pronouncing formulated intentions or starting Sa'i from Marwah...",
            "সাঈর নিয়ত মুখে ঘটা করে উচ্চারণ করা, মারওয়া থেকে সাঈ শুরু করা, কিংবা তাকবীরের সময় হাত তোলার মতো ভুল করা যাবে না। সাঈ অবশ্যই সাফা থেকে শুরু করে মারওয়ায় শেষ করতে হবে এবং মোট ৭টি চক্কর সম্পন্ন করতে হবে।",
            "Common mistakes during Sa'i include making verbal formulated intentions, mistakenly starting from Marwah instead of Safa, or raising hands as if in prayer while uttering Takbir. Sa'i must commence at Safa and conclude at Marwah, completing seven distinct traverses."
        ));

        // 7. ৮ জিলহজ্জ হাজিদের ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            7,
            "৮ জিলহজ্জ হাজিদের ভুলত্রুটি",
            "Mistakes by Pilgrims on 8th Dhul Hijjah (Yawm at-Tarwiyah)",
            "৮ তারিখে মিনাতে না এসে সরাসরি আরাফায় চ...",
            "Bypassing Mina on the 8th of Dhul Hijjah and heading straight to Arafat...",
            "৮ তারিখে মিনাতে না এসে কোনো যৌক্তিক কারণ ছাড়াই সরাসরি আরাফায় চলে যাওয়া সুন্নাহ পরিপন্থী। ৮ই জিলহজ জোহর থেকে ৯ই জিলহজ ফজর পর্যন্ত মিনায় অবস্থান করা এবং পাঁচ ওয়াক্ত সালাত কসর করে স্ব স্ব সময়ে আদায় করা সুন্নাত।",
            "Skipping Mina on the 8th of Dhul Hijjah and heading straight to Arafat without a valid necessity misses an important Sunnah. Staying in Mina from Dhuhr of the 8th until Fajr of the 9th and praying the five prayers shortened at their proper times is the established prophetic practice."
        ));

        // 8. আরাফা দিবসের ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            8,
            "আরাফা দিবসের ভুলত্রুটি",
            "Mistakes on the Day of Arafah (9th Dhul Hijjah)",
            "আরাফার সীমানায় প্রবেশ না করেই উকুফ করা এ...",
            "Staying outside the demarcated boundaries of Arafat during Wuquf...",
            "আরাফার মূল সীমানায় প্রবেশ না করে নামিরা উপত্যকা বা আরাফার বাইরে অবস্থান করলে হজের প্রধান ফরজ (উকুফে আরাফা) বাতিল হয়ে যাবে। এছাড়া জাবালে রহমতে ওঠা আবশ্যক মনে করা কিংবা সূর্যাস্তের পূর্বেই তাড়াহুড়ো করে আরাফা ত্যাগ করা মারাত্মক ভুল।",
            "Remaining outside the designated boundaries of Arafat (such as in Wadi Namirah) invalidates the foremost pillar of Hajj (Wuquf). Additionally, believing that climbing Jabal ar-Rahmah is obligatory or departing Arafat before the sun completely sets are serious errors."
        ));

        // 9. উকুফে মুযদালেফার ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            9,
            "উকুফে মুযদালেফার ভুলত্রুটি",
            "Mistakes at Muzdalifah",
            "ধীর-স্থির ও শান্ত ভাব বজায় না রেখে হুলস্থুল ক...",
            "Rushing chaotically without tranquility and delaying Maghrib & Isha prayers...",
            "ধীর-স্থির ও শান্ত ভাব বজায় না রেখে হুলস্থুল করা, মুযদালিফায় পৌঁছার পূর্বেই মাগরিবের সালাত আদায় করা, কিংবা ফজরের পূর্বে কোনো ওজর ছাড়াই মুযদালিফা ত্যাগ করা ভুল। মুযদালিফায় মাগরিব ও এশা একত্রে এক আজান ও দুই ইকামতে আদায় করতে হয়।",
            "Rushing chaotically, praying Maghrib on the road before reaching Muzdalifah, or leaving Muzdalifah before midnight without a valid excuse are major errors. In Muzdalifah, Maghrib and Isha are combined together with one Adhan and two Iqamahs."
        ));

        // 10. কঙ্কর নিক্ষেপের ভুল-ত্রুটি
        list.add(new HajjMistakesCardItem(
            10,
            "কঙ্কর নিক্ষেপের ভুল-ত্রুটি",
            "Mistakes in Stoning the Jamarat (Ramy)",
            "মুযদালেফা থেকে কঙ্কর কুড়িয়ে না নিলে কঙ্কর নি...",
            "Believing pebbles must strictly be collected only from Muzdalifah...",
            "মুযদালিফা থেকেই কঙ্কর কুড়াতে হবে এমন কোনো বাধ্যবাধকতা নেই; মিনা থেকেও নেওয়া যায়। এছাড়া শয়তানের উপর ক্রোধ প্রকাশ করে জুতা, লাঠি বা বড় পাথর নিক্ষেপ করা, সব কঙ্কর একসাথে ছুঁড়ে মারা, কিংবা ভিড়ের মধ্যে মারামারি করা বড় ভুল। প্রতিটি কঙ্কর এক এক করে তাকবীরসহ হাউজের মধ্যে ফেলতে হবে।",
            "There is no obligation that pebbles must only be gathered from Muzdalifah; they may be collected from Mina as well. Casting large stones, shoes, or sticks in anger, throwing all seven pebbles at once, or violent jostling are grave mistakes. Pebbles must be cast one by one with Takbir into the basin."
        ));

        // 11. মদিনা মুনাওয়ারা যিয়ারতকালে ভুলত্রুটি
        list.add(new HajjMistakesCardItem(
            11,
            "মদিনা মুনাওয়ারা যিয়ারতকালে ভুলত্রুটি",
            "Mistakes When Visiting Madinah Munawwarah",
            "মদিনা যিয়ারত হজ্জের অংশ বলে মনে করা। রাসু...",
            "Believing visiting Madinah is an obligatory component of Hajj rites...",
            "মদিনা যিয়ারতকে হজের আনুষ্ঠানিক অংশ মনে করা ঠিক নয়; এটি আলাদা একটি বরকতময় সুন্নাত যিয়ারত। মসজিদে নববীতে সালাত আদায় ও রাসুলুল্লাহ (ﷺ)-এর রওজা মুবারকে আদবের সাথে সালাম পেশ করতে হবে। রওজা স্পর্শ করা, সিজদা করা বা বরকতের জন্য কাপড় ঘষা শিরক ও বিদআত।",
            "Visiting Madinah is not a ritual pillar or obligation of Hajj, but a blessed, highly recommended Sunnah visit. One should pray in Masjid an-Nabawi and convey salam upon the Prophet (ﷺ) with utmost decorum, avoiding un-Islamic practices like kissing or rubbing the grilles."
        ));

        return list;
    }
}
