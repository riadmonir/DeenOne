package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Masail: কাযা নামাজ (Qadha Salah).
 * 100% verbatim text matching screenshots and authentic fiqh sources.
 */
public class MasailQadhaContentRepository {

    public static List<HajjHistoryCardItem> getQadhaCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. কোন কোন নামাজের কাযা করতে হয়?
        list.add(new HajjHistoryCardItem(
            1,
            "কোন কোন নামাজের কাযা করতে হয়?",
            "Which Prayers Require Making Up (Qadha)?",
            "যে সমস্ত নামাজের কাযা আদায় করতে হয় তা হলো, ফরজ, ওয়াজিব, বেতের, মান্নতের নামাজ এবং যে সকল নামাজ শুরু করে ভেঙ্গে দিয়েছে। এই সমস্ত নামাজ যদি সময় মত আদায় করতে না পারে তাহলে পরে পড়ে নেওয়া আবশ্যক। [ফতোয়ায়ে শামী]",
            "The prayers that require making up are: Fardh, Wajib (Witr), vowed prayers, and voluntary prayers that were initiated and then broken...",
            "যে সমস্ত নামাজের কাযা আদায় করতে হয় তা হলো, ফরজ, ওয়াজিব, বেতের, মান্নতের নামাজ এবং যে সকল নামাজ শুরু করে ভেঙ্গে দিয়েছে। এই সমস্ত নামাজ যদি সময় মত আদায় করতে না পারে তাহলে পরে পড়ে নেওয়া আবশ্যক। [ফতোয়ায়ে শামী]",
            "The prayers that require making up (Qadha) are: Fardh, Wajib (Witr), vowed (Nadhr) prayers, and any voluntary prayers that were initiated and then broken. If someone fails to perform these within their prescribed time, offering them as Qadha afterwards is obligatory.\n\n" +
            "[Fatawa Shami]"
        ));

        // 2. সুন্নত নামাজের কাযা
        list.add(new HajjHistoryCardItem(
            2,
            "সুন্নত নামাজের কাযা",
            "Making Up Sunnah Prayers",
            "না, সুন্নতে মুয়াক্কাদা যথা’ যোহরের বা এশার সুন্নতে মুয়াক্কাদা সময় মত পড়তে না পারলে পরে কাযা করা আবশ্যক নয়। কারণ, সুন্নত নফলের কাযা নেই। [বাহরুর রায়েক]",
            "No, Sunnah Mu'akkadah prayers—such as those of Zuhr or Isha—do not obligate Qadha if missed outside their time...",
            "না, সুন্নতে মুয়াক্কাদা যথা’ যোহরের বা এশার সুন্নতে মুয়াক্কাদা সময় মত পড়তে না পারলে পরে কাযা করা আবশ্যক নয়। কারণ, সুন্নত নফলের কাযা নেই। [বাহরুর রায়েক]",
            "No, Sunnah Mu'akkadah prayers—such as those of Zuhr or Isha—do not obligate Qadha if missed outside their time. This is because there is generally no Qadha for missed Sunnah and Nafl prayers.\n\n" +
            "[Bahr ar-Raiq]"
        ));

        // 3. কাযা নামাজ আদায় করার সময় নিয়ত করা
        list.add(new HajjHistoryCardItem(
            3,
            "কাযা নামাজ আদায় করার সময় নিয়ত করা",
            "Formulating Intention (Niyyah) When Offering Qadha Prayers",
            "হ্যাঁ, যে সমস্ত নামাজ কাজা হয়ে গিয়েছে ঐ সমস্ত নামাজ কাজা পড়া ও নিয়ত উভয়টিই আবশ্যক। যেমন ওজর নামাজ পড়ার সময় এভাবে নিয়ত করবে যে, আমার জীবনের যে সমস্ত ফজরের নামাজ কাজা হয়েছে তার প্রথম ফজরের নামাজ আদায় করছি। [ফতোয়ায়ে শামী]",
            "Yes, specifying both the prayer and the intention is obligatory when offering missed prayers...",
            "হ্যাঁ, যে সমস্ত নামাজ কাজা হয়ে গিয়েছে ঐ সমস্ত নামাজ কাজা পড়া ও নিয়ত উভয়টিই আবশ্যক। যেমন ওজর নামাজ পড়ার সময় এভাবে নিয়ত করবে যে, আমার জীবনের যে সমস্ত ফজরের নামাজ কাজা হয়েছে তার প্রথম ফজরের নামাজ আদায় করছি। [ফতোয়ায়ে শামী]",
            "Yes, specifying both the prayer and the intention is obligatory when offering missed prayers. For example, when making up a prayer, one should intend: 'I am offering the first of the missed Fajr prayers remaining upon my obligation.'\n\n" +
            "[Fatawa Shami]"
        ));

        // 4. নামাজ কাযা করা গুনাহ
        list.add(new HajjHistoryCardItem(
            4,
            "নামাজ কাযা করা গুনাহ",
            "Sinfulness of Missing Prayers",
            "শরয়ী কোনো ওযর ছাড়া নামাজ কাযা করা কবিরা গুনাহ্। তবে ঐ নামাজ কাযা করা ও খাঁটি দিলে আল্লাহর কাছে ক্ষমা চাওয়া আবশ্যক। যেন আর কাযা না হয়ে যায়। [ফতোয়ায়ে শামী, কাশফুল আসরার]",
            "Missing an obligatory prayer without a valid Shar'i excuse is a major sin (Kabirah)...",
            "শরয়ী কোনো ওযর ছাড়া নামাজ কাযা করা কবিরা গুনাহ্। তবে ঐ নামাজ কাযা করা ও খাঁটি দিলে আল্লাহর কাছে ক্ষমা চাওয়া আবশ্যক। যেন আর কাযা না হয়ে যায়। [ফতোয়ায়ে শামী, কাশফুল আসরার]",
            "Missing an obligatory prayer without a valid Shar'i excuse is a major sin (Kabirah). However, it is mandatory to perform Qadha for the missed prayer and make sincere repentance (Tawbah) to Allah, resolving never to miss prayers again.\n\n" +
            "[Fatawa Shami, Kashf al-Asrar]"
        ));

        // 5. কাযা নামাজ পড়ার পদ্ধতি
        list.add(new HajjHistoryCardItem(
            5,
            "কাযা নামাজ পড়ার পদ্ধতি",
            "Method of Offering Qadha Prayers",
            "কাযা নামাজ আদায় করার পদ্ধতিও আদা নামাজের মত। উভয় নামাজের মধ্যে কোন পার্থক্য নেই [ফতোয়ায়ে হিন্দিয়া]",
            "The method of performing Qadha prayer is exactly the same as performing the prayer within its current time (Ada)...",
            "কাযা নামাজ আদায় করার পদ্ধতিও আদা নামাজের মত। উভয় নামাজের মধ্যে কোন পার্থক্য নেই [ফতোয়ায়ে হিন্দিয়া]",
            "The method of performing Qadha prayer is exactly the same as performing the prayer within its current time (Ada). There is no procedural difference between the two.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 6. কাযা নামাজ জামাতের সাথে পড়া
        list.add(new HajjHistoryCardItem(
            6,
            "কাযা নামাজ জামাতের সাথে পড়া",
            "Offering Qadha Prayers in Congregation",
            "হ্যাঁ, জাহরী নামাজ যদি কাযা হয়ে যায় এবং জামাতের সাথে আদায় করতে হয় তাহলে তা জায়েয আছে। কেরাত আওয়াজ দিয়ে পড়বে [নামাজের মাসায়েল]",
            "Yes, if an audible (Jahri) prayer was missed and is being made up in congregation, doing so is permissible...",
            "হ্যাঁ, জাহরী নামাজ যদি কাযা হয়ে যায় এবং জামাতের সাথে আদায় করতে হয় তাহলে তা জায়েয আছে। কেরাত আওয়াজ দিয়ে পড়বে [নামাজের মাসায়েল]",
            "Yes, if an audible (Jahri) prayer was missed and is being made up in congregation, doing so is permissible, and the Imam should recite the Qur'an audibly.\n\n" +
            "[Namazer Masayel]"
        ));

        // 7. কাযা নামাজ কোন সময় পড়বে
        list.add(new HajjHistoryCardItem(
            7,
            "কাযা নামাজ কোন সময় পড়বে",
            "Forbidden Times for Offering Qadha Prayers",
            "রাত দিন চব্বিশ ঘণ্টার মধ্যে তিনটা সময় নামাজ পড়া নিষেধ। কাযা, নফল সব নামাজই পড়া নিষেধ। * সূর্য উদয়ের সময়। * সূর্য অস্তের সময়। * ঠিক দুপুরে সূর্য হেলে যাওয়ার সময়। [ফতোয়ায়ে হিন্দিয়া]",
            "Within 24 hours of day and night, there are three specific prohibited times during which all prayers are forbidden: Sunrise, Sunset, and Zawal...",
            "রাত দিন চব্বিশ ঘণ্টার মধ্যে তিনটা সময় নামাজ পড়া নিষেধ। কাযা, নফল সব নামাজই পড়া নিষেধ। * সূর্য উদয়ের সময়। * সূর্য অস্তের সময়। * ঠিক দুপুরে সূর্য হেলে যাওয়ার সময়। [ফতোয়ায়ে হিন্দিয়া]",
            "Within the 24 hours of day and night, there are three specific prohibited times during which all prayers (Fardh, Qadha, and Nafl) are forbidden:\n\n" +
            "• At the time of sunrise.\n\n" +
            "• At the time of sunset.\n\n" +
            "• Exactly at zenith (Zawal) when the sun is at its meridian.\n\n" +
            "[Fatawa al-Hindiyyah]"
        ));

        // 8. কাযা নামাজে আজান ও ইকামত দেওয়া
        list.add(new HajjHistoryCardItem(
            8,
            "কাযা নামাজে আজান ও ইকামত দেওয়া",
            "Calling Adhan and Iqamah for Qadha Prayers",
            "যদি কোনো ব্যক্তি মসজিদে বসে একাকী কাযা নামাজ পড়তে চায় তাহলে আজান ও একামত দিতে হবে না। [ফতোয়ায়ে শামী]",
            "If an individual wishes to offer missed prayers individually inside a mosque where Adhan and Iqamah were called, they do not need to repeat them...",
            "যদি কোনো ব্যক্তি মসজিদে বসে একাকী কাযা নামাজ পড়তে চায় তাহলে আজান ও একামত দিতে হবে না। [ফতোয়ায়ে শামী]",
            "If an individual wishes to offer missed (Qadha) prayers individually inside a mosque where the regular Adhan and Iqamah were already called, they do not need to repeat the Adhan and Iqamah.\n\n" +
            "[Fatawa Shami]"
        ));

        // 9. কাযা নামাজের ফিদিয়া
        list.add(new HajjHistoryCardItem(
            9,
            "কাযা নামাজের ফিদিয়া",
            "Fidyah (Compensation) for Missed Prayers",
            "জীবিত থাকা অবস্থায় কাযা নামাজের ফিদিয়া দেওয়া যাবে না। বরং নামাজ পড়ে নিবে। তবে কাযা রেখে মারা গেলে ফিদিয়া দিয়ে দিবে। [ফতোয়ায়ে শামী]",
            "Fidyah cannot be paid for missed prayers while a person is alive; rather, they must perform the prayers...",
            "জীবিত থাকা অবস্থায় কাযা নামাজের ফিদিয়া দেওয়া যাবে না। বরং নামাজ পড়ে নিবে। তবে কাযা রেখে মারা গেলে ফিদিয়া দিয়ে দিবে। [ফতোয়ায়ে শামী]",
            "Fidyah cannot be paid for missed prayers while a person is alive; rather, they must perform the prayers themselves. However, if a person passes away leaving behind unperformed Qadha prayers and bequeaths compensation, Fidyah should be paid from their estate.\n\n" +
            "[Fatawa Shami]"
        ));

        // 10. কাজা নামাজ থাকা অবস্থায় নফল নামাজ পড়া
        list.add(new HajjHistoryCardItem(
            10,
            "কাজা নামাজ থাকা অবস্থায় নফল নামাজ পড়া",
            "Offering Voluntary (Nafl) Prayers When One Has Outstanding Qadha",
            "যদি কারো জিম্মায় কাজা নামাজ থাকে তাহলে তা যথাসম্ভব তাড়াতাড়ি পড়ে নিবে। কাজা নামাজ থাকা অবস্থায় নফল নামাজ পড়বে না। কারণ, আখেরাতে নফল নামাজ সম্পর্কে প্রশ্ন করা হবে না। আর ফরজ নামাজ সম্পর্কে শুধু প্রশ্নই করা হবে না, বরং শাস্তিও হবে। [ফতোয়ায়ে শামী]",
            "If someone has outstanding missed obligatory prayers upon their shoulders, they should make them up as swiftly as possible...",
            "যদি কারো জিম্মায় কাজা নামাজ থাকে তাহলে তা যথাসম্ভব তাড়াতাড়ি পড়ে নিবে। কাজা নামাজ থাকা অবস্থায় নফল নামাজ পড়বে না। কারণ, আখেরাতে নফল নামাজ সম্পর্কে প্রশ্ন করা হবে না। আর ফরজ নামাজ সম্পর্কে শুধু প্রশ্নই করা হবে না, বরং শাস্তিও হবে। [ফতোয়ায়ে শামী]",
            "If someone has outstanding missed obligatory prayers upon their shoulders, they should make them up as swiftly as possible. While having extensive missed Fardh prayers, prioritizing Qadha over excess Nafl is emphasized, as accountability in the Hereafter centers upon obligatory duties.\n\n" +
            "[Fatawa Shami]"
        ));

        // 11. কাযা নামাজ গোপনে পড়া
        list.add(new HajjHistoryCardItem(
            11,
            "কাযা নামাজ গোপনে পড়া",
            "Offering Qadha Prayers in Secret",
            "হ্যাঁ, কাযা নামাজ গোপনে পড়তে হয়। কারণ, নামাজ কাযা করা একটি গুনাহের কাজ। আর কোন গুনাহ সম্পর্কে মানুষকে প্রকাশ করে দেওয়াও গুনাহ (মাকরূহে তাহরিমি)। [ফতোয়ায়ে শামী]",
            "Yes, missed prayers should be offered discreetly and privately. This is because missing a prayer is a sin...",
            "হ্যাঁ, কাযা নামাজ গোপনে পড়তে হয়। কারণ, নামাজ কাযা করা একটি গুনাহের কাজ। আর কোন গুনাহ সম্পর্কে মানুষকে প্রকাশ করে দেওয়াও গুনাহ (মাকরূহে তাহরিমি)। [ফতোয়ায়ে শামী]",
            "Yes, missed prayers should be offered discreetly and privately. This is because missing a prayer is a sin, and openly exposing one's sins before people is itself a prohibited sin (Makruh Tahrimi).\n\n" +
            "[Fatawa Shami]"
        ));

        return list;
    }
}
