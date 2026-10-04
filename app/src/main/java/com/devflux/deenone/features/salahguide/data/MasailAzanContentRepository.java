package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Masail: আজান (Adhan).
 * Contains 100% verbatim text matching screenshots and authentic fiqh sources.
 */
public class MasailAzanContentRepository {

    public static List<HajjHistoryCardItem> getAzanCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. আজানের শুরুতে দরুদ শরিফ পড়া
        list.add(new HajjHistoryCardItem(
            1,
            "আজানের শুরুতে দরুদ শরিফ পড়া",
            "Reciting Durood Sharif Before the Adhan",
            "সন্দেহ নেই নফল ইবাদতসমূহের মধ্যে দরুদ শরিফের মর্যাদা অনন্য। বেশি বেশি দরুদ শরিফ পড়লে রাসুল (সা.) এর সুপারিশ ভাগ্যে জুটবে। না পড়লে রাসুলের সুপারিশ থেকে বঞ্চিত হতে হবে। তাই বলে কি যখন তখন যেখানে সেখানে দরুদ শরিফ পড়তে হ...",
            "Undoubtedly, among voluntary acts of worship, sending blessings (Durood Sharif) holds a distinct and unique status. Reciting abundant Durood earns the intercession of the Messenger of Allah (ﷺ)...",
            "সন্দেহ নেই নফল ইবাদতসমূহের মধ্যে দরুদ শরিফের মর্যাদা অনন্য। বেশি বেশি দরুদ শরিফ পড়লে রাসুল (সা.) এর সুপারিশ ভাগ্যে জুটবে। না পড়লে রাসুলের সুপারিশ থেকে বঞ্চিত হতে হবে। তাই বলে কি যখন তখন যেখানে সেখানে দরুদ শরিফ পড়তে হবে? ইসলামে আবেগেরে স্থান নেই。\n\n" +
            "শরিয়ত যেখানে দরুদ শরিফ পড়তে বলেনি সেখানে দরুদ শরিফ পড়া সুন্নত নয় বরং খেলাফে সুন্নাত। উপমহাদেশের অনেক মসজিদেই আজানের আগে দরুদ শরিফ পড়ার প্রচলন দেখা যায়। এ বিষয়ে আলেমদের ফতোয়া হলো, রাসুল (সা.), সাহাবি ও তাবেয়িদের যুগে আজানের আগে দরুদ শরিফ পড়ার প্রচলন ছিল না, তাই এটি একটি সুন্নাহবহির্ভূত আমল। সুন্নাহপ্রেমিক কোনো উম্মত এমনটি করতে পারে না。",
            "Undoubtedly, among voluntary acts of worship, sending blessings (Durood Sharif) holds a distinct and unique status. Reciting abundant Durood earns the intercession of the Messenger of Allah (ﷺ). Neglecting it deprives one of his intercession. But does that mean Durood Sharif can be recited anywhere and at any time without restriction? There is no place for mere emotion in Islam.\n\n" +
            "Where the Shariah has not prescribed reciting Durood Sharif, reciting it there is not a Sunnah, but rather contrary to the Sunnah. In many mosques across the subcontinent, the custom of reciting Durood Sharif before the Adhan is observed. In this regard, the ruling of the scholars is that during the era of the Prophet (ﷺ), the Companions, and the Tabi'un, reciting Durood before Adhan was nonexistent; hence, it is an action outside the Sunnah. No true lover of the Sunnah among the Ummah can engage in such practice."
        ));

        // 2. আজানের ভেতর অতিরিক্ত শব্দ বা বাক্য সংযোজন করাও সুন্নাতের খেলাফ
        list.add(new HajjHistoryCardItem(
            2,
            "আজানের ভেতর অতিরিক্ত শব্দ বা বাক্য সংযোজন করাও সুন্নাতের খেলাফ",
            "Adding Extra Words or Phrases in Adhan is Against the Sunnah",
            "একইভাবে আজানের ভেতর নতুন কোনো শব্দ যোগ করা কিংবা কোন শব্দ ছেড়ে দেওয়াও সুন্নাহ সম্মত নয়। রাসুল (সা.) এর সময় থেকে যেভাবে আজান প্রচার হয়ে আসছে ঠিক সেভাবেই কেয়ামত পর্যন্ত আজান দিয়ে যেতে হবে। এর ব্যতিক্রম করা সুন্নতের খেলা...",
            "Similarly, adding any new word into the Adhan or omitting any word is not compliant with the Sunnah. The Adhan must be called until the Day of Judgment in the exact manner it has been proclaimed from the time of the Prophet (ﷺ)...",
            "একইভাবে আজানের ভেতর নতুন কোনো শব্দ যোগ করা কিংবা কোন শব্দ ছেড়ে দেওয়াও সুন্নাহ সম্মত নয়। রাসুল (সা.) এর সময় থেকে যেভাবে আজান প্রচার হয়ে আসছে ঠিক সেভাবেই কেয়ামত পর্যন্ত আজান দিয়ে যেতে হবে। এর ব্যতিক্রম করা সুন্নতের খেলাফ。\n\n" +
            "গত শতকের শেষ দিকে পাকিস্তানের কোনো কোনো মসজিদে আজানের শেষ বাক্য ‘লা ইলাহা ইল্লাল্লাহ’ এর সাথে ‘মুহাম্মাদুর রাসুলুল্লাহ অতিরিক্ত যোগ করা হতো। পাকিস্তানের মুফতিরা এর বিরুদ্ধে কঠোর অবস্থান নিয়ে বলেন, এসব সুস্পষ্ট সুন্নাহবিরোধী আমল。",
            "Similarly, adding any new word into the Adhan or omitting any word is not compliant with the Sunnah. The Adhan must be called until the Day of Judgment in the exact manner it has been proclaimed from the time of the Prophet (ﷺ). Acting contrary to this is against the Sunnah.\n\n" +
            "Towards the end of the last century, in some mosques in Pakistan, 'Muhammadur Rasulullah' was additionally added after the final phrase of the Adhan, 'La ilaha illallah'. The Muftis of Pakistan took a strict stance against this, declaring that such practices are blatantly contrary to the Sunnah."
        ));

        // 3. ভুলে কোন শব্দ বা বাক্য বাদ পড়ে গেলে কী করবেন
        list.add(new HajjHistoryCardItem(
            3,
            "ভুলে কোন শব্দ বা বাক্য বাদ পড়ে গেলে কী করবেন",
            "What to Do if a Word or Sentence is Omitted by Mistake",
            "আজান দেওয়ার সময় ভুলবশত কোনো শব্দ বা বাক্য বলা না হলে দ্বিতীয়বার শুদ্ধভাবে আজান দিতে হবে। তবে ফজরের আজানে ‘আসসালাতু খাইরুমমিনান নাওম’ বাক্যটি বাদ পড়ে গেলে দ্বিতীয়বার আজান দেওয়ার প্রয়োজন নেই। ভুল আজানে জামাত করলে জামা...",
            "If a word or sentence is inadvertently omitted while calling the Adhan, the Adhan should be called correctly a second time. However, if 'As-Salatu Khairum Minan Nawm' is missed in Fajr, repeating the Adhan is not necessary...",
            "আজান দেওয়ার সময় ভুলবশত কোনো শব্দ বা বাক্য বলা না হলে দ্বিতীয়বার শুদ্ধভাবে আজান দিতে হবে। তবে ফজরের আজানে ‘আসসালাতু খাইরুমমিনান নাওম’ বাক্যটি বাদ পড়ে গেলে দ্বিতীয়বার আজান দেওয়ার প্রয়োজন নেই। ভুল আজানে জামাত করলে জামাতের কোনো অসুবিধা হবে না。",
            "If a word or phrase is inadvertently omitted while calling the Adhan, the Adhan should be called correctly a second time. However, if the phrase 'As-Salatu Khairum Minan Nawm' is omitted during the Fajr Adhan, repeating the Adhan is not necessary. Holding the congregational prayer with such an Adhan will cause no invalidity or defect in the congregation."
        ));

        // 4. বিপদ-মহামারির সময় আজান দেওয়া জায়েজ
        list.add(new HajjHistoryCardItem(
            4,
            "বিপদ-মহামারির সময় আজান দেওয়া জায়েজ",
            "Permissibility of Calling Adhan During Calamities and Epidemics",
            "বিখ্যাত ফতোয়ার গ্রন্থ বাহরুর রায়েকের টিকায় আল্লামা খাইরুদ্দিন রহমতুল্লাহি আলাইহি লিখেন, আমি শাফেয়ি মাজহাবের গ্রন্থে দেখেছি, নামাজ ছাড়াও আরো কিছু অনুষ্ঠানে আজান দেওয়া সুন্নাত। যেমন নবজাতকের কানে, দুশ্চিন্তার সময়, মহামারি দেখা দিলে, দুষ্টু ...",
            "In the marginalia of the renowned Fiqh text Al-Bahr ar-Raiq, Allamah Khayruddin (RA) writes: 'I found in Shafi'i texts that Adhan is Sunnah on occasions outside Salah—such as in a newborn's ear, in times of grief, during epidemics...",
            "বিখ্যাত ফতোয়ার গ্রন্থ বাহরুর রায়েকের টিকায় আল্লামা খাইরুদ্দিন রহমতুল্লাহি আলাইহি লিখেন, আমি শাফেয়ি মাজহাবের গ্রন্থে দেখেছি, নামাজ ছাড়াও আরো কিছু অনুষ্ঠানে আজান দেওয়া সুন্নাত। যেমন নবজাতকের কানে, দুশ্চিন্তার সময়, মহামারি দেখা দিলে, দুষ্টু লোক কিংবা হিংস্র জানোয়ারের সামনে পড়লে, সৈন্যদের আক্রমণের সময় এবং কোথাও আগুন লাগলে。\n\n" +
            "বিপদের সময় আজান দেওয়ার বিষয়ে হানাফি মাজহাবের গ্রন্থগুলোতে তেমন কোন তথ্য পাওয়া যায় না। আধুনিক হানাফি আলেমরা এটিকে নিষেধও মনে করেন না, আবার কাউকে উৎসাহও দেন না。",
            "In the marginalia of the renowned Fiqh text Al-Bahr ar-Raiq, Allamah Khayruddin (RA) writes: 'I found in the books of the Shafi'i Madhhab that Adhan is Sunnah on occasions other than Salah—such as in the ear of a newborn, in times of deep grief, when epidemics break out, when encountering mischievous persons or ferocious beasts, during enemy military attacks, and when a fire breaks out.'\n\n" +
            "Regarding calling the Adhan during times of calamity, little specific information is found in classical Hanafi texts. Contemporary Hanafi scholars neither deem it prohibited nor actively encourage it."
        ));

        // 5. জামাতের জন্য আজান শর্ত
        list.add(new HajjHistoryCardItem(
            5,
            "জামাতের জন্য আজান শর্ত",
            "Adhan as a Prerequisite for Congregational Prayer",
            "পাঁচ ওয়াক্ত ফরজ নামাজ এবং জুমার নামাজের জামাতের জন্য আজান শর্ত। তবে কোথাও যদি আজান ছাড়াই জামাত হয়ে যায় তবে দ্বিতীয়বার আর জামাত করার প্রয়োজন নেই...",
            "Calling the Adhan is a prerequisite for the congregation of the five daily obligatory prayers and Jumu'ah. However, if a congregation is held without Adhan, repeating...",
            "পাঁচ ওয়াক্ত ফরজ নামাজ এবং জুমার নামাজের জামাতের জন্য আজান শর্ত। তবে কোথাও যদি আজান ছাড়াই জামাত হয়ে যায় তবে দ্বিতীয়বার আর জামাত করার প্রয়োজন নেই। হানাফি মাজহাবে কোনো নফল নামাজের জন্য আজান দেওয়া জায়েজ নেই। তবে অন্যান্য মাজহাবে তাহাজ্জুদসহ বিভিন্ন নফল নামাজের জন্য আজান দেওয়ার কথা বলা আছে。",
            "Calling the Adhan is a prerequisite for the congregation of the five daily obligatory prayers and Jumu'ah. However, if a congregation has already been performed without the Adhan, repeating the congregation is not required. In the Hanafi Madhhab, calling the Adhan for any voluntary (Nafl) prayer is not permissible. However, in other Madhhabs, calling the Adhan for various voluntary prayers, including Tahajjud, is mentioned."
        ));

        return list;
    }
}
