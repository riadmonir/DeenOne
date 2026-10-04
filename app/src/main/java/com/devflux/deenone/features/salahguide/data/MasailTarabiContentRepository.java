package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Masail: তারাবীহ (Tarabi).
 * 100% verbatim text matching screenshots and authentic fiqh sources.
 */
public class MasailTarabiContentRepository {

    public static List<HajjHistoryCardItem> getTarabiCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. মুসাফিরের জন্য তারাবিহ নামাজ পড়া কি জরুরি?
        list.add(new HajjHistoryCardItem(
            1,
            "মুসাফিরের জন্য তারাবিহ নামাজ পড়া কি জরুরি?",
            "Is it Necessary for a Traveler to Pray Tarabi Prayer?",
            "যদি শরয়ী সফর হয় এবং বেশি ব্যস্ততা থাকে তাহলে ফজরের সুন্নাত বাদে বাকী সুন্নাত গুলো ছেড়ে দেওয়ার সুযোগ আছে। কিন্তু যদি পড়ার সুযোগ পাওয়া যায় তাহলে ছেড়ে দেওয়া আদৌ উচিৎ নয়। [ফতোয়ায়ে হিন্দিয়া]",
            "If it is a valid Islamic journey (Shar'i Safar) and there is pressing preoccupation, there is leeway to omit Sunnah prayers other than Fajr Sunnah...",
            "যদি শরয়ী সফর হয় এবং বেশি ব্যস্ততা থাকে তাহলে ফজরের সুন্নাত বাদে বাকী সুন্নাত গুলো ছেড়ে দেওয়ার সুযোগ আছে। কিন্তু যদি পড়ার সুযোগ পাওয়া যায় তাহলে ছেড়ে দেওয়া আদৌ উচিৎ নয়। [ফতোয়ায়ে হিন্দিয়া]",
            "If it is a valid Islamic journey (Shar'i Safar) and there is pressing preoccupation or hardship, there is allowance to omit Sunnah prayers other than the Fajr Sunnah. However, if there is ease and opportunity to pray, it should by no means be neglected.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 2. তারাবির নামাজে সেজদায়ে তেলাওয়াতের এলান করা
        list.add(new HajjHistoryCardItem(
            2,
            "তারাবির নামাজে সেজদায়ে তেলাওয়াতের এলান করা",
            "Announcing Sajdah at-Tilawah in Tarabi Prayer",
            "সেজদায়ে তেলাওয়াতের জন্য এলান করার প্রয়োজন নেই। তবে মুক্তাদীদের সতর্কতার জন্য এলান করার অবকাশ রয়েছে। [আপকে মাসায়েল আওর উনকা হল]",
            "There is no requirement to make an announcement for Sajdah at-Tilawah. However, there is leeway to notify or alert the followers...",
            "সেজদায়ে তেলাওয়াতের জন্য এলান করার প্রয়োজন নেই। তবে মুক্তাদীদের সতর্কতার জন্য এলান করার অবকাশ রয়েছে। [আপকে মাসায়েল আওর উনকা হল]",
            "There is no requirement to make an announcement for Sajdah at-Tilawah. However, there is leeway to notify or alert the followers for clarity and awareness.\n\n" +
            "[Aapke Masail Aur Unka Hal]"
        ));

        // 3. তারাবির নামাজ জামাতে পড়া
        list.add(new HajjHistoryCardItem(
            3,
            "তারাবির নামাজ জামাতে পড়া",
            "Praying Tarabi Prayer in Congregation",
            "পুরো রমজান মাসে তারাবীহ নামাজ জামাতে পড়া সুন্নাতে মুয়াক্কাদা। [ফেকহি জাওয়াযেত]",
            "Offering Tarabi prayer in congregation throughout the entire month of Ramadan is Sunnah Mu'akkadah...",
            "পুরো রমজান মাসে তারাবীহ নামাজ জামাতে পড়া সুন্নাতে মুয়াক্কাদা। [ফেকহি জাওয়াযেত]",
            "Offering Tarabi prayer in congregation throughout the entire month of Ramadan is Sunnah Mu'akkadah (Kifayah).\n\n" +
            "[Fiqhi Jawazat]"
        ));

        // 4. টাখনুর নিচে পায়জামা বা লুঙ্গি পড়ে নামাজ পড়া
        list.add(new HajjHistoryCardItem(
            4,
            "টাখনুর নিচে পায়জামা বা লুঙ্গি পড়ে নামাজ পড়া",
            "Praying with Trousers or Lungi Below the Ankles",
            "অহংকারবশত টাখনুর নিচে পায়জামা বা লুঙ্গি পরিধান করা মাকরূহে তাহরীমী। তাই এভাবে নামাজ পড়া মাকরূহ হবে কিন্তু নামাজ হয়ে যাবে। [বুখারী শরীফ/ফাতাওয়ায়ে কাজিখান]",
            "Wearing trousers or a lungi below the ankles out of arrogance is Makruh Tahrimi. Therefore, praying in this state is disliked, though the prayer is valid...",
            "অহংকারবশত টাখনুর নিচে পায়জামা বা লুঙ্গি পরিধান করা মাকরূহে তাহরীমী। তাই এভাবে নামাজ পড়া মাকরূহ হবে কিন্তু নামাজ হয়ে যাবে। [বুখারী শরীফ/ফাতাওয়ায়ে কাজিখান]",
            "Wearing trousers or a lungi below the ankles out of arrogance or vanity is Makruh Tahrimi. Therefore, praying in this state is disliked (Makruh), though the prayer is technically fulfilled and valid.\n\n" +
            "[Sahih al-Bukhari / Fatawa Qadhikhan]"
        ));

        // 5. নামাজের মধ্যে মুখে থুথু বা কফ আসা
        list.add(new HajjHistoryCardItem(
            5,
            "নামাজের মধ্যে মুখে থুথু বা কফ আসা",
            "Saliva or Phlegm Entering the Mouth During Prayer",
            "যদি নামাজরত অবস্থায় থুথু বা কফ আসে তাহলে সম্ভব হলে তা গিলে নিবে অন্যথায় কাপড়ের কোন এক কোণ দ্বারা মুছে নিবে। [বুখারী শরীফ/ ফাতাওয়ায়ে হক্কানিয়া]",
            "If saliva or phlegm rises in the mouth while praying, one may swallow it if feasible; otherwise, one may wipe it with a corner of cloth...",
            "যদি নামাজরত অবস্থায় থুথু বা কফ আসে তাহলে সম্ভব হলে তা গিলে নিবে অন্যথায় কাপড়ের কোন এক কোণ দ্বারা মুছে নিবে। [বুখারী শরীফ/ ফাতাওয়ায়ে হক্কানিয়া]",
            "If saliva or phlegm rises in the mouth while praying, one may swallow it if feasible; otherwise, one may discreetly wipe it with a corner of a garment or handkerchief.\n\n" +
            "[Sahih al-Bukhari / Fatawa Haqqaniyyah]"
        ));

        // 6. মেহরাবে ঢুকে নামাজ পড়ানো
        list.add(new HajjHistoryCardItem(
            6,
            "মেহরাবে ঢুকে নামাজ পড়ানো",
            "Leading Prayer by Standing Completely Inside the Mihrab",
            "ইমাম সাহেব একেবারে মেহরাবের ভিতর ঢুকে নামাজ পড়ানো মাকরূহ। তবে যদি ইমাম সাহেব মেহরাবের বাহিরে দাঁড়ায় এবং রকু সেজদাহ মেহরাবের ভিতরে করে তাহলে এতে কোন সমস্যা নেই। [ফাতাওয়ায়ে শামী]",
            "It is disliked for the Imam to stand completely inside the Mihrab while leading prayer. However, if the Imam stands outside and only bows inside...",
            "ইমাম সাহেব একেবারে মেহরাবের ভিতর ঢুকে নামাজ পড়ানো মাকরূহ। তবে যদি ইমাম সাহেব মেহরাবের বাহিরে দাঁড়ায় এবং রকু সেজদাহ মেহরাবের ভিতরে করে তাহলে এতে কোন সমস্যা নেই। [ফাতাওয়ায়ে শামী]",
            "It is disliked (Makruh) for the Imam to stand completely inside the arch of the Mihrab while leading prayer. However, if the Imam stands outside the niche and only bows (Ruku) and prostrates (Sajdah) within the Mihrab, there is no issue or dislike in doing so.\n\n" +
            "[Fatawa Shami]"
        ));

        return list;
    }
}
