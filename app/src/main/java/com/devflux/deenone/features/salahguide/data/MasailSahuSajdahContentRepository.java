package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Masail: সাহু সেজদাহ (Sahu Sajdah).
 * 100% verbatim text matching screenshots and authentic fiqh sources.
 */
public class MasailSahuSajdahContentRepository {

    public static List<HajjHistoryCardItem> getSahuSajdahCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. যদি কারো ওয়াজিব ছুটে যাওয়ার কারণে সন্দেহ হয় তাহলে তার সেজদায়ে সাহ আদায় করতে হবে কি?
        list.add(new HajjHistoryCardItem(
            1,
            "যদি কারো ওয়াজিব ছুটে যাওয়ার কারণে সন্দেহ হয় তাহলে তার সেজদায়ে সাহ আদায় করতে হবে কি?",
            "Does a Doubt Regarding Missing a Wajib Obligate Sahu Sajdah?",
            "শুধুমাত্র সন্দেহের কারণে সেজদায়ে সাহ্ আদায় করতে হবে না এবং না করা উচিত। যদি কেউ করে ফেলে তাহলে তার নামাজ হয়ে যাবে। দ্বিতীয় বার পড়তে হবে না। [ফতোয়ায়ে শামী]",
            "Sahu Sajdah is not required and should not be performed merely based on doubt. If someone inadvertently performs it out of doubt, their prayer remains valid...",
            "শুধুমাত্র সন্দেহের কারণে সেজদায়ে সাহ্ আদায় করতে হবে না এবং না করা উচিত। যদি কেউ করে ফেলে তাহলে তার নামাজ হয়ে যাবে। দ্বিতীয় বার পড়তে হবে না। [ফতোয়ায়ে শামী]",
            "Sahu Sajdah is not required and should not be performed merely based on doubt. If someone inadvertently performs it out of doubt, their prayer remains valid and does not need to be repeated.\n\n" +
            "[Fatawa Shami]"
        ));

        // 2. ইমাম সেজদায়ে সাহ করতে ভুলে গেলে
        list.add(new HajjHistoryCardItem(
            2,
            "ইমাম সেজদায়ে সাহ করতে ভুলে গেলে",
            "If the Imam Forgets to Perform Sahu Sajdah",
            "যদি ইমাম সাহেবের ওপর ওয়াজিব হয় আর তা তিনি আদায় করতে ভুলে যান তাহলে মুক্তাদীদের উচিত তাকে স্মরণ করিয়ে দেওয়া। মুক্তাদীদের উপর সেজদায়ে সাহ্ করা ওয়াজিব নয়। তবে ঐ নামাজকে পুনরায় পড়া ওয়াজিব। [ফতোয়ায়ে হিন্দিয়া]",
            "If Sahu Sajdah was obligatory upon the Imam and he forgets to perform it, the followers (Muqtadis) should remind him...",
            "যদি ইমাম সাহেবের ওপর ওয়াজিব হয় আর তা তিনি আদায় করতে ভুলে যান তাহলে মুক্তাদীদের উচিত তাকে স্মরণ করিয়ে দেওয়া। মুক্তাদীদের উপর সেজদায়ে সাহ্ করা ওয়াজিব নয়। তবে ঐ নামাজকে পুনরায় পড়া ওয়াজিব। [ফতোয়ায়ে হিন্দিয়া]",
            "If Sahu Sajdah was obligatory upon the Imam and he forgets to perform it, the followers (Muqtadis) should remind him. The followers are not obliged to perform Sahu Sajdah on their own, but repeating that prayer is Wajib.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 3. সেজদায়ে সাহ-এর পর নামাজে শরীক হওয়া
        list.add(new HajjHistoryCardItem(
            3,
            "সেজদায়ে সাহর পর নামাজে শরীক হওয়া",
            "Joining the Prayer After the Imam Performs Sahu Sajdah",
            "হ্যাঁ, ইমাম সেজদায়ে সাহ্ করার পর কেউ জামাতে শরীক হলে তার ইকতেদা সহীহ হবে। সে বসে তাশাহহুদ পড়বে এবং ইমাম সালাম ফিরানোর পর দাঁড়িয়ে বাকী নামাজ পূর্ণ করবে। [ফতোয়ায়ে হিন্দিয়া]",
            "Yes, if someone joins the congregation after the Imam performs Sahu Sajdah, following the Imam is valid...",
            "হ্যাঁ, ইমাম সেজদায়ে সাহ্ করার পর কেউ জামাতে শরীক হলে তার ইকতেদা সহীহ হবে। সে বসে তাশাহহুদ পড়বে এবং ইমাম সালাম ফিরানোর পর দাঁড়িয়ে বাকী নামাজ পূর্ণ করবে। [ফতোয়ায়ে হিন্দিয়া]",
            "Yes, if someone joins the congregation after the Imam performs Sahu Sajdah, following the Imam is valid. The person sits to recite Tashahhud and then stands up after the Imam's final Salam to complete the remaining Rak'ahs.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 4. সেজদায়ে সাহ ওয়াজিব কিনা তা জানা না থাকলে
        list.add(new HajjHistoryCardItem(
            4,
            "সেজদায়ে সাহ ওয়াজিব কিনা তা জানা না থাকলে",
            "When in Doubt Whether Sahu Sajdah is Wajib",
            "এমন ব্যক্তির জন্য সতর্কতামূলক সেজদায়ে সাহ্ করে নেয়া উত্তম। [ফাতাওয়ায়ে দারুল উলুম দেওবন্দ]",
            "For such a person, it is recommended and preferable as a precaution to perform Sahu Sajdah...",
            "এমন ব্যক্তির জন্য সতর্কতামূলক সেজদায়ে সাহ্ করে নেয়া উত্তম। [ফাতাওয়ায়ে দারুল উলুম দেওবন্দ]",
            "For such a person, it is recommended and preferable as a precaution to perform Sahu Sajdah.\n\n" +
            "[Fatawa Darul Uloom Deoband]"
        ));

        // 5. চার রাকাত বিশিষ্ট নামাজের শেষ দুই রাকাতে সুরা মিলালে
        list.add(new HajjHistoryCardItem(
            5,
            "চার রাকাত বিশিষ্ট নামাজের শেষ দুই রাকাতে সুরা মিলালে",
            "Reciting an Additional Surah in the Last Two Rak'ahs of a 4-Rak'ah Fardh Prayer",
            "চার রাকাত বিশিষ্ট নামাজের শেষ দুই রাকাতে ভুলবশত সুরা মিলালে সেজদায়ে সাহ্ করতে হবে না। [ফাতাওয়ায়ে তাতারখানিয়া]",
            "If someone mistakenly recites an additional Surah after Surah al-Fatihah in the last two Rak'ahs of a four-Rak'ah obligatory prayer, Sahu Sajdah is not required...",
            "চার রাকাত বিশিষ্ট নামাজের শেষ দুই রাকাতে ভুলবশত সুরা মিলালে সেজদায়ে সাহ্ করতে হবে না। [ফাতাওয়ায়ে তাতারখানিয়া]",
            "If someone mistakenly recites an additional Surah after Surah al-Fatihah in the last two Rak'ahs of a four-Rak'ah obligatory prayer, Sahu Sajdah is not required.\n\n" +
            "[Fatawa Tatarikhaniyyah]"
        ));

        // 6. সুরা ফাতেহা একাধিক বার পড়া
        list.add(new HajjHistoryCardItem(
            6,
            "সুরা ফাতেহা একাধিক বার পড়া",
            "Reciting Surah al-Fatihah More Than Once",
            "হ্যাঁ, একই রাকাতে একাধিক বার সুরা ফাতেহা পড়লে সেজদায়ে সাহ্ করা আবশ্যক। [ফতোয়ায়ে শামী]",
            "Yes, if Surah al-Fatihah is recited more than once in the same Rak'ah, performing Sahu Sajdah becomes obligatory...",
            "হ্যাঁ, একই রাকাতে একাধিক বার সুরা ফাতেহা পড়লে সেজদায়ে সাহ্ করা আবশ্যক। [ফতোয়ায়ে শামী]",
            "Yes, if Surah al-Fatihah is recited more than once in the same Rak'ah (before the Surah in Fardh), performing Sahu Sajdah becomes obligatory.\n\n" +
            "[Fatawa Shami]"
        ));

        // 7. জুমা ও ঈদের নামাজে সেজদায়ে সাহ
        list.add(new HajjHistoryCardItem(
            7,
            "জুমা ও ঈদের নামাজে সেজদায়ে সাহ",
            "Sahu Sajdah in Jumu'ah and Eid Prayers",
            "যদি জামাত এতো ছোট হয় যে, ইমাম সেজদায়ে সাহ্ করলে মুসল্লিরা বুঝতে পারবে। তাহলে সেজদায়ে সাহ্ করবে। আর যদি জামাত বড় হয় এবং মুসল্লিগণ বিভ্রান্তিতে পড়ার আশংকা হয় তাহলে সেজদায়ে সাহ্ করবে না। [ফতোয়ায়ে শামী]",
            "If the congregation is small enough that the worshippers will easily understand the Imam's Sahu Sajdah, he should perform it...",
            "যদি জামাত এতো ছোট হয় যে, ইমাম সেজদায়ে সাহ্ করলে মুসল্লিরা বুঝতে পারবে। তাহলে সেজদায়ে সাহ্ করবে। আর যদি জামাত বড় হয় এবং মুসল্লিগণ বিভ্রান্তিতে পড়ার আশংকা হয় তাহলে সেজদায়ে সাহ্ করবে না। [ফতোয়ায়ে শামী]",
            "If the congregation is small enough that the worshippers will easily understand the Imam's Sahu Sajdah, he should perform it. But if the congregation is large and there is fear of causing confusion among the worshippers, the Imam should omit Sahu Sajdah.\n\n" +
            "[Fatawa Shami]"
        ));

        // 8. প্রথম বৈঠক না করলে
        list.add(new HajjHistoryCardItem(
            8,
            "প্রথম বৈঠক না করলে",
            "Omitting the First Sitting (Tashahhud)",
            "নামাজে প্রথম বৈঠক ওয়াজিব। ভুলবশত প্রথম বৈঠক না করে দাঁড়িয়ে গেলে, যদি সোজা হয়ে দাঁড়িয়ে যায় তবে আর বসবে না; নামাজ শেষে সেজদায়ে সাহ্ করে নিবে। আর যদি পুরোপুরি সোজা হয়ে দাঁড়ানোর আগেই মনে পড়ে, তবে বসে তাশাহহুদ পড়বে এবং সেজদায়ে সাহ্ করতে হবে না। [ফতোয়ায়ে শামী, আলমগীরী]",
            "The first sitting (Qa'dah Ula) is Wajib. If one mistakenly stands up without sitting, if they have stood fully erect, they should not return to sit...",
            "নামাজে প্রথম বৈঠক ওয়াজিব। ভুলবশত প্রথম বৈঠক না করে দাঁড়িয়ে গেলে, যদি সোজা হয়ে দাঁড়িয়ে যায় তবে আর বসবে না; নামাজ শেষে সেজদায়ে সাহ্ করে নিবে। আর যদি পুরোপুরি সোজা হয়ে দাঁড়ানোর আগেই মনে পড়ে, তবে বসে তাশাহহুদ পড়বে এবং সেজদায়ে সাহ্ করতে হবে না। [ফতোয়ায়ে শামী, আলমগীরী]",
            "The first sitting (Qa'dah Ula) is Wajib. If one mistakenly stands up without sitting, if they have stood fully erect, they should not return to sit, but instead complete the prayer and perform Sahu Sajdah at the end. If they remember before standing fully erect, they should return to sitting and Sahu Sajdah is not required.\n\n" +
            "[Fatawa Shami, Alamgiri]"
        ));

        // 9. কিসের কারণে সেজদায়ে সাহ ওয়াজিব হয়
        list.add(new HajjHistoryCardItem(
            9,
            "কিসের কারণে সেজদায়ে সাহ ওয়াজিব হয়",
            "Reasons That Make Sahu Sajdah Obligatory",
            "সেজদায়ে সাহ্ ওয়াজিব হওয়ার মূল নীতি হল এই যে, অর্থাৎ ফরজ ও ওয়াজিব বিলম্ব করার দ্বারা এবং ওয়াজিব ছেড়ে দেওয়ার দ্বারা। এসমস্ত কারণে সেজদায়ে সাহ্ ওয়াজিব হয়। [আলমগীরী]",
            "The foundational principle for Sahu Sajdah becoming Wajib is: delaying a Fardh or Wajib act, or inadvertently omitting a Wajib act...",
            "সেজদায়ে সাহ্ ওয়াজিব হওয়ার মূল নীতি হল এই যে, অর্থাৎ ফরজ ও ওয়াজিব বিলম্ব করার দ্বারা এবং ওয়াজিব ছেড়ে দেওয়ার দ্বারা। এসমস্ত কারণে সেজদায়ে সাহ্ ওয়াজিব হয়। [আলমগীরী]",
            "The foundational principle for Sahu Sajdah becoming Wajib is: delaying a Fardh or Wajib act, or inadvertently omitting a Wajib act of prayer. Sahu Sajdah becomes obligatory due to these reasons.\n\n" +
            "[Fatawa al-Alamgiri]"
        ));

        // 10. সেজদায়ে সাহ আদায় করার পদ্ধতি কি
        list.add(new HajjHistoryCardItem(
            10,
            "সেজদায়ে সাহ আদায় করার পদ্ধতি কি",
            "Method of Performing Sahu Sajdah",
            "সেজদায়ে সাহ্ আদায় করার পদ্ধতি হলো, শেষ বৈঠকে শুধু তাশাহহুদ পড়ে ডান দিকে সালাম ফিরিয়ে দুইটি সেজদা করবে এবং পুনরায় তাশাহহুদ পড়বে, দরুদ শরীফ ও দোয়ায়ে মাছুরা পড়বে। এরপর সালাম ফিরাবে। [ফতোয়ায়ে হিন্দিয়া]",
            "The method of performing Sahu Sajdah is: in the final sitting, recite only Tashahhud, turn the head to the right with one Salam, perform two prostrations...",
            "সেজদায়ে সাহ্ আদায় করার পদ্ধতি হলো, শেষ বৈঠকে শুধু তাশাহহুদ পড়ে ডান দিকে সালাম ফিরিয়ে দুইটি সেজদা করবে এবং পুনরায় তাশাহহুদ পড়বে, দরুদ শরীফ ও দোয়ায়ে মাছুরা পড়বে। এরপর সালাম ফিরাবে। [ফতোয়ায়ে হিন্দিয়া]",
            "The method of performing Sahu Sajdah is: in the final sitting, recite only Tashahhud, turn the head to the right with one Salam, perform two prostrations (Sajdah), then sit back to recite Tashahhud again along with Durood and Du'a Mathurah, and conclude with Salam to both sides.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 11. নামাজে একটি সেজদা করলে
        list.add(new HajjHistoryCardItem(
            11,
            "নামাজে একটি সেজদা করলে",
            "Performing Only One Prostration in a Rak'ah",
            "যখন স্মরণ হবে তখন সেজদাটি করে নিবে এবং নিয়ম অনুযায়ী সেজদায়ে সাহ্ করে নিবে। [ফতোয়ায়ে হিন্দিয়া]",
            "Whenever one remembers the missed prostration during the prayer, they should perform that prostration immediately and then perform Sahu Sajdah...",
            "যখন স্মরণ হবে তখন সেজদাটি করে নিবে এবং নিয়ম অনুযায়ী সেজদায়ে সাহ্ করে নিবে। [ফতোয়ায়ে হিন্দিয়া]",
            "Whenever one remembers the missed prostration during the prayer, they should perform that prostration immediately and then perform Sahu Sajdah at the end of the prayer according to the prescribed rule.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 12. বিতিরের শেষ রাকাতে দোয়ায়ে কুনুত না পড়া
        list.add(new HajjHistoryCardItem(
            12,
            "বিতিরের শেষ রাকাতে দোয়ায়ে কুনুত না পড়া",
            "Omitting Du'a al-Qunut in the Final Rak'ah of Witr",
            "হ্যাঁ, সেজদায়ে সাহ্ ওয়াজিব হবে। কারণ দোয়ায়ে কুনুত পড়া ওয়াজিব। [ফতোয়ায়ে হিন্দিয়া]",
            "Yes, Sahu Sajdah becomes obligatory because reciting Du'a al-Qunut in Witr prayer is Wajib...",
            "হ্যাঁ, সেজদায়ে সাহ্ ওয়াজিব হবে। কারণ দোয়ায়ে কুনুত পড়া ওয়াজিব। [ফতোয়ায়ে হিন্দিয়া]",
            "Yes, Sahu Sajdah becomes obligatory because reciting Du'a al-Qunut in Witr prayer is Wajib.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 13. সেজদায়ে সাহু করতে ভুলে গেলে
        list.add(new HajjHistoryCardItem(
            13,
            "সেজদায়ে সাহু করতে ভুলে গেলে",
            "If One Forgets to Perform Sahu Sajdah",
            "নামাজের মধ্যে সেজদায়ে সাহু ওয়াজিব হলে ভুলে তা আদায় করতে না পারলে নামাজ পুনরায় পড়া ওয়াজিব। [ফতোয়ায়ে শামী]",
            "If Sahu Sajdah becomes obligatory in prayer and one forgets to perform it before concluding the prayer, repeating the prayer is obligatory...",
            "নামাজের মধ্যে সেজদায়ে সাহু ওয়াজিব হলে ভুলে তা আদায় করতে না পারলে নামাজ পুনরায় পড়া ওয়াজিব। [ফতোয়ায়ে শামী]",
            "If Sahu Sajdah becomes obligatory in prayer and one forgets to perform it before concluding the prayer, repeating the prayer is obligatory (Wajib).\n\n" +
            "[Fatawa Shami]"
        ));

        return list;
    }
}
