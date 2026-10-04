package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Masail: নামাজ (Salah).
 * 100% verbatim text matching screenshots and authentic fiqh sources.
 */
public class MasailSalahContentRepository {

    public static List<HajjHistoryCardItem> getSalahCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. নামাজে যা করলে নামাজ ও ওযু উভয়টি ভেঙ্গে যায়
        list.add(new HajjHistoryCardItem(
            1,
            "নামাজে যা করলে নামাজ ও ওযু উভয়টি ভেঙ্গে যায়",
            "Actions that Invalidate Both Salah and Wudu",
            "নামাজে ৫টি কাজ করলে নামাজ ও ওযু উভয়টিই ভেঙ্গে যায় • নামাজের মধ্যে অট্ট হাসি দিলে • হেলান দিয়ে ঘুমালে • ইচ্ছাকৃতভাবে হদস করা (ওযু ভেঙ্গে ফেলা) • জ্ঞান লোপ পাওয়া • নামাজে স্বপ্ন দোষ হওয়া (বীর্য বের হওয়া) [আননুতাফ ফিল ফাতাতাওয়া]",
            "Performing 5 actions during prayer invalidates both Salah and Wudu: • Laughing aloud • Sleeping while leaning against support • Deliberately breaking Wudu (Hadath) • Loss of consciousness • Experiencing a wet dream...",
            "নামাজে ৫টি কাজ করলে নামাজ ও ওযু উভয়টিই ভেঙ্গে যায়\n\n" +
            "• নামাজের মধ্যে অট্ট হাসি দিলে\n\n" +
            "• হেলান দিয়ে ঘুমালে\n\n" +
            "• ইচ্ছাকৃতভাবে হদস করা (ওযু ভেঙ্গে ফেলা)\n\n" +
            "• জ্ঞান লোপ পাওয়া\n\n" +
            "• নামাজে স্বপ্ন দোষ হওয়া (বীর্য বের হওয়া)\n\n" +
            "[আননুতাফ ফিল ফাতাতাওয়া]",
            "Performing 5 actions during prayer invalidates both Salah and Wudu:\n\n" +
            "• Laughing aloud during prayer.\n\n" +
            "• Sleeping while leaning against a support.\n\n" +
            "• Deliberately breaking Wudu (Hadath).\n\n" +
            "• Loss of consciousness.\n\n" +
            "• Experiencing a wet dream (emission of semen) during prayer.\n\n" +
            "[An-Nutaf fil-Fatawa]"
        ));

        // 2. নামাজে যা করলে শুধু নামাজ ভেঙ্গে যায়
        list.add(new HajjHistoryCardItem(
            2,
            "নামাজে যা করলে শুধু নামাজ ভেঙ্গে যায়",
            "Actions that Invalidate Only Salah",
            "নামাজে ১০টি কাজ করলে শুধু নামাজ ভেঙ্গে যায় • নামাজে কথা বলা চাই কম হোক বা বেশি। ইচ্ছায় বা অনিচ্ছায় • খাওয়া, চাই কম হোক বা বেশি- ইচ্ছায় বা অনিচ্ছায় • আমলে কাসির করা • ইচ্ছাকৃতভাবে কেবলার দিক থেকে সিনা ঘুরিয়ে ফেলা • পুরুষ মহিলা...",
            "Performing 10 actions during prayer invalidates only Salah: • Speaking during prayer, whether little or much • Eating or drinking • Committing Amal-e-Katheer • Turning chest away from Qiblah • Physical contact between man and woman...",
            "নামাজে ১০টি কাজ করলে শুধু নামাজ ভেঙ্গে যায়\n\n" +
            "• নামাজে কথা বলা চাই কম হোক বা বেশি। ইচ্ছায় বা অনিচ্ছায়\n\n" +
            "• খাওয়া, চাই কম হোক বা বেশি- ইচ্ছায় বা অনিচ্ছায়\n\n" +
            "• আমলে কাসির করা\n\n" +
            "• ইচ্ছাকৃতভাবে কেবলার দিক থেকে সিনা ঘুরিয়ে ফেলা\n\n" +
            "• পুরুষ মহিলাকে স্পর্শ করা কিংবা মহিলা পুরুষকে স্পর্শ করা\n\n" +
            "• ইচ্ছাকৃতভাবে অন্যের লজ্জাস্থানের দিকে তাকানো\n\n" +
            "• ইচ্ছাকৃতভাবে আপন লজ্জাস্থান প্রকাশ করা\n\n" +
            "• এক দিরহামের বেশি পরিমাণ মল শরীরে লেগে থাকা অবস্থায় রুকু কিংবা সেজদা করা\n\n" +
            "• ইচ্ছাকৃতভাবে অতিরিক্ত রুকু বা সেজদা করা\n\n" +
            "• মুসল্লি যদি ইমামের হদস হওয়া সম্পর্কে জানে আর ইমাম ওযু না করে রুকু সেজদা করে ফেলে তাহলে মুসল্লির নামাজ ভেঙ্গে যাবে।\n\n" +
            "[আননুতাফ ফিল ফাতাওয়া]",
            "Performing 10 actions during prayer invalidates only Salah (Wudu remains intact):\n\n" +
            "• Speaking during prayer, whether little or much, intentionally or unintentionally.\n\n" +
            "• Eating or drinking, whether little or much, intentionally or unintentionally.\n\n" +
            "• Committing 'Amal-e-Katheer' (excessive bodily movements).\n\n" +
            "• Deliberately turning the chest away from the Qiblah.\n\n" +
            "• Physical contact between a man and a woman.\n\n" +
            "• Deliberately looking towards another person's Awrah (private parts).\n\n" +
            "• Deliberately exposing one's own Awrah.\n\n" +
            "• Performing Ruku or Sujood with impurity exceeding the amount of one dirham on the body/clothing.\n\n" +
            "• Deliberately performing an extra Ruku or Sujood.\n\n" +
            "• If a follower knows the Imam has broken Wudu (Hadath) and the Imam performs Ruku or Sujood without Wudu, the follower's prayer is invalidated.\n\n" +
            "[An-Nutaf fil-Fatawa]"
        ));

        // 3. নামাজের মধ্যে নিয়ত মুখে বলা জরুরি কি?
        list.add(new HajjHistoryCardItem(
            3,
            "নামাজের মধ্যে নিয়ত মুখে বলা জরুরি কি?",
            "Is it Obligatory to Utter the Niyyah (Intention) Verbally in Prayer?",
            "নিয়ত হলো অন্তরের একটি কাজ। অর্থাৎ মানুষ অন্তরে এই ফিকির করবে যে, আমি অমুক নামাজ পড়ছি। মুখে বলা জরুরি নয়। তবে উলামায়ে মুতাআখিরিন মুখে বলাকে মনে মনে বলার চেয়ে উত্তম বলেছেন। [ফাতাওয়ায়ে আলমগীরী]",
            "Intention (Niyyah) is an act of the heart. That is, a person should consciously intend in their heart that they are offering a specific prayer. Uttering it verbally is not obligatory. [Fatawa al-Alamgiri]",
            "নিয়ত হলো অন্তরের একটি কাজ। অর্থাৎ মানুষ অন্তরে এই ফিকির করবে যে, আমি অমুক নামাজ পড়ছি। মুখে বলা জরুরি নয়। তবে উলামায়ে মুতাআখিরিন মুখে বলাকে মনে মনে বলার চেয়ে উত্তম বলেছেন। [ফাতাওয়ায়ে আলমগীরী]",
            "Intention (Niyyah) is an act of the heart. That is, a person should consciously intend in their heart that they are offering a specific prayer. Uttering it verbally is not obligatory. However, the later scholars (Muta'akhkhireen) have mentioned that verbalizing it along with the heart's intention is better.\n\n" +
            "[Fatawa al-Alamgiri]"
        ));

        // 4. নফল এর জায়গায় ভুলে সুন্নতের কথা বলা
        list.add(new HajjHistoryCardItem(
            4,
            "নফল এর জায়গায় ভুলে সুন্নতের কথা বলা",
            "Mistakenly Mentioning Sunnah Instead of Nafl",
            "নিয়ত হলো অন্তরের একটি কাজ, সুতরাং অন্তরে যে নামাজের ইচ্ছা ছিল ঐ নামাজই হবে। [ফাতাওয়ায়ে আলমগীরী]",
            "Intention is essentially an act of the heart; therefore, whichever prayer was intended in the heart, that prayer will be counted. [Fatawa al-Alamgiri]",
            "নিয়ত হলো অন্তরের একটি কাজ, সুতরাং অন্তরে যে নামাজের ইচ্ছা ছিল ঐ নামাজই হবে। [ফাতাওয়ায়ে আলমগীরী]",
            "Intention is essentially an act of the heart; therefore, whichever prayer was intended in the heart, that prayer will be counted.\n\n" +
            "[Fatawa al-Alamgiri]"
        ));

        // 5. নামাজে ডানে বামে তাকানো
        list.add(new HajjHistoryCardItem(
            5,
            "নামাজে ডানে বামে তাকানো",
            "Looking Left or Right During Prayer",
            "নামাজে ডানে বামে দেখা মাকরূহে তানযিহি। আর চেহারা ঘুরানো মাকরূহে তাহরিমি। [ফাতাওয়ায়ে আলমগীরী]",
            "Glancing sideways (with the eyes) during prayer is Makruh Tanzihi (mildly disliked), whereas turning the face away is Makruh Tahrimi (severely disliked). [Fatawa al-Alamgiri]",
            "নামাজে ডানে বামে দেখা মাকরূহে তানযিহি। আর চেহারা ঘুরানো মাকরূহে তাহরিমি। [ফাতাওয়ায়ে আলমগীরী]",
            "Glancing sideways (with the eyes) during prayer is Makruh Tanzihi (mildly disliked), whereas turning the entire face away is Makruh Tahrimi (severely disliked).\n\n" +
            "[Fatawa al-Alamgiri]"
        ));

        // 6. তাকবিরে তাহরিমার সময় হাত উঠানো
        list.add(new HajjHistoryCardItem(
            6,
            "তাকবিরে তাহরিমার সময় হাত উঠানো",
            "Raising Hands During Takbir at-Tahrimah",
            "তাকবিরে তাহরিমা হলো ফরজ আর বাকি তাকবির সুন্নত। সুতরাং তাকবিরে তাহরিমা ছাড়া কোন একটা ছেড়ে দিলে নামাজ নষ্ট হবে না। [ফাতাওয়ায়ে শামী]",
            "Takbir at-Tahrimah is obligatory while the other Takbirs are Sunnah. Therefore, omitting any Takbir other than Takbir at-Tahrimah will not invalidate the prayer. [Fatawa Shami]",
            "তাকবিরে তাহরিমা হলো ফরজ আর বাকি তাকবির সুন্নত। সুতরাং তাকবিরে তাহরিমা ছাড়া কোন একটা ছেড়ে দিলে নামাজ নষ্ট হবে না। [ফাতাওয়ায়ে শামী]",
            "Takbir at-Tahrimah is obligatory (Fardh/Shart) while the other Takbirs are Sunnah. Therefore, omitting any Takbir other than Takbir at-Tahrimah will not invalidate the prayer.\n\n" +
            "[Fatawa Shami]"
        ));

        // 7. ইমাম তাকবির কখন বলবে
        list.add(new HajjHistoryCardItem(
            7,
            "ইমাম তাকবির কখন বলবে",
            "When Should the Imam Pronounce the Takbir?",
            "উত্তম হলো একামত শেষ হলেই ইমাম সাহেবের তাকবির বলা যেন একামত বলা লোকও সাথে শরীক হতে পারে। [বাহরুর রায়েক]",
            "It is commendable for the Imam to pronounce the Takbir as soon as the Iqamah finishes, so that the person who proclaimed the Iqamah can also join from the start. [Bahr ar-Raiq]",
            "উত্তম হলো একামত শেষ হলেই ইমাম সাহেবের তাকবির বলা যেন একামত বলা লোকও সাথে শরীক হতে পারে। [বাহরুর রায়েক]",
            "It is commendable for the Imam to pronounce the Takbir as soon as the Iqamah finishes, so that the person who proclaimed the Iqamah can also join from the start.\n\n" +
            "[Bahr ar-Raiq]"
        ));

        // 8. কিছু লোক নিয়ত বাধার পর হাত বাধে না, তাদের নামাজ হবে কি?
        list.add(new HajjHistoryCardItem(
            8,
            "কিছু লোক নিয়ত বাধার পর হাত বাধে না, তাদের নামাজ হবে কি?",
            "Do Those Who Do Not Fold Hands After Niyyah Have Their Prayer Accepted?",
            "নামাজে হাত বাধা সুন্নত সুতরাং কেউ যদি নিয়ত বেধে হাত ছেড়ে দিলে ও তার নামাজ হয়ে যাবে। [বাদায়েউস সানায়ে]",
            "Folding the hands in prayer is Sunnah; therefore, if someone makes the intention and leaves their hands down by their sides, their prayer remains valid. [Bada'i as-Sana'i]",
            "নামাজে হাত বাধা সুন্নত সুতরাং কেউ যদি নিয়ত বেধে হাত ছেড়ে দিলে ও তার নামাজ হয়ে যাবে। [বাদায়েউস সানায়ে]",
            "Folding the hands in prayer is Sunnah; therefore, if someone makes the intention and leaves their hands down by their sides, their prayer remains valid.\n\n" +
            "[Bada'i as-Sana'i]"
        ));

        // 9. রফে ইয়াদাইন করলে নামাজ সহীহ হবে কি?
        list.add(new HajjHistoryCardItem(
            9,
            "রফে ইয়াদাইন করলে নামাজ সহীহ হবে কি?",
            "Is the Prayer Valid if One Performs Raf' al-Yadayn?",
            "শুধু তাকবিরে তাহরিমার সময় হাত উঠানো সুন্নত এছাড়া বাকি জায়গায় হাত উঠানো অনুত্তম। [বাদায়েউস সানায়ে]",
            "Raising the hands is Sunnah only during Takbir at-Tahrimah; raising hands at other transitions is non-recommended. [Bada'i as-Sana'i]",
            "শুধু তাকবিরে তাহরিমার সময় হাত উঠানো সুন্নত এছাড়া বাকি জায়গায় হাত উঠানো অনুত্তম। [বাদায়েউস সানায়ে]",
            "Raising the hands is Sunnah only during Takbir at-Tahrimah; raising hands at other transitions is non-recommended (Ghayr Mustahsan according to the Hanafi school).\n\n" +
            "[Bada'i as-Sana'i]"
        ));

        // 10. বসে নামাজ আদায়কারী রুকুতে কি পরিমাণ ঝুকবে?
        list.add(new HajjHistoryCardItem(
            10,
            "বসে নামাজ আদায়কারী রুকুতে কি পরিমাণ ঝুকবে?",
            "How Much Should a Person Sitting for Prayer Bow in Ruku?",
            "এই পরিমাণ ঝুকবে যে মাথা হাঁটু বরাবর হয়ে যায়। [ফাতাওয়ায়ে শামী]",
            "One should bow to such an extent that the head aligns roughly parallel with the knees. [Fatawa Shami]",
            "এই পরিমাণ ঝুকবে যে মাথা হাঁটু বরাবর হয়ে যায়। [ফাতাওয়ায়ে শামী]",
            "One should bow to such an extent that the head aligns roughly parallel with the knees.\n\n" +
            "[Fatawa Shami]"
        ));

        // 11. সেজদায় জমিনে নাক রাখার হুকুম
        list.add(new HajjHistoryCardItem(
            11,
            "সেজদায় জমিনে নাক রাখার হুকুম",
            "Ruling on Placing the Nose on the Ground in Sujood",
            "সেজদায় জমিনে নাক রাখা ওয়াজিব। যদি কেউ ভুলে একবার রেখে আবার উঠিয়ে ফেলে তাহলে তার নামাজ আদায় হয়ে যাবে। কারণ একবার রাখার দ্বারাই ওয়াজিব আদায় হয়ে গেছে। কিন্তু এটা খেলাফে সুন্নত। [ফাতাওয়ায়ে আলমগীরী]",
            "Placing the nose on the ground during Sujood is Wajib. If someone mistakenly places it once and then lifts it up, the prayer is valid because the Wajib was fulfilled. [Fatawa al-Alamgiri]",
            "সেজদায় জমিনে নাক রাখা ওয়াজিব। যদি কেউ ভুলে একবার রেখে আবার উঠিয়ে ফেলে তাহলে তার নামাজ আদায় হয়ে যাবে। কারণ একবার রাখার দ্বারাই ওয়াজিব আদায় হয়ে গেছে। কিন্তু এটা খেলাফে সুন্নত। [ফাতাওয়ায়ে আলমগীরী]",
            "Placing the nose on the ground during Sujood is Wajib. If someone mistakenly places it once and then lifts it up, the prayer is valid because the Wajib was fulfilled with the single placement, though doing so contradicts the Sunnah.\n\n" +
            "[Fatawa al-Alamgiri]"
        ));

        // 12. সেজদা করার সময় উভয় পা জমিন থেকে উঠে যাওয়া
        list.add(new HajjHistoryCardItem(
            12,
            "সেজদা করার সময় উভয় পা জমিন থেকে উঠে যাওয়া",
            "Both Feet Lifting from the Ground During Sujood",
            "যদি তিনবার সুবহানাল্লাহ বলা পরিমাণ পা উঠে থাকে তাহলে তার নামাজ ভঙ্গ হয়ে যাবে। [ফাতাওয়ায়ে ইন্দিয়া]",
            "If both feet remain completely lifted off the ground for a duration equivalent to reciting SubhanAllah three times, the prayer is invalidated. [Fatawa al-Hindiyyah]",
            "যদি তিনবার সুবহানাল্লাহ বলা পরিমাণ পা উঠে থাকে তাহলে তার নামাজ ভঙ্গ হয়ে যাবে। [ফাতাওয়ায়ে ইন্দিয়া]",
            "If both feet remain completely lifted off the ground for a duration equivalent to reciting 'SubhanAllah' three times, the prayer is invalidated.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 13. জামাতের সাথে নামাজ পড়া
        list.add(new HajjHistoryCardItem(
            13,
            "জামাতের সাথে নামাজ পড়া",
            "Praying in Congregation (Jama'at)",
            "পাঁচ ওয়াক্ত নামাজের জন্য জামাত ওয়াজিব, চাই ঘরে হোক বা মসজিদে। তবে যদি কোন ওজর থাকে তাহলে ওয়াজিব নয়। [ফেকহি জাওয়াযেত]",
            "Praying in congregation is Wajib for the five daily prayers, whether at home or in the mosque, unless there is a valid excuse. [Fiqhi Jawazat]",
            "পাঁচ ওয়াক্ত নামাজের জন্য জামাত ওয়াজিব, চাই ঘরে হোক বা মসজিদে। তবে যদি কোন ওজর থাকে তাহলে ওয়াজিব নয়। [ফেকহি জাওয়াযেত]",
            "Praying in congregation is Wajib for the five daily prayers, whether at home or in the mosque. However, if there is a valid excuse (Udhr), it ceases to be obligatory.\n\n" +
            "[Fiqhi Jawazat]"
        ));

        return list;
    }
}
