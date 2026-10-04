package com.devflux.deenone.features.ramadan.ui;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageRozaTaraweehMasailBinding;
import com.devflux.deenone.features.ramadan.adapter.RozaTaraweehMasailAdapter;
import com.devflux.deenone.features.ramadan.model.RozaTaraweehMasalaItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Production-ready Page Dialog for: "তারাবীহ নিয়ে মাসআলা-মাসায়েল" (Topic 7 in Taraweeh).
 * 100% accurately matching the user screenshot with expandable Masail cards,
 * verbatim line-by-line Bengali texts, clean dual-language mode, and ultra-smooth 60 FPS scrolling.
 */
public class RozaTaraweehMasailPageDialog {

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageRozaTaraweehMasailBinding binding = PageRozaTaraweehMasailBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Header Title matching screenshot verbatim: "তারাবীহ নিয়ে মাসআলা-মাসায়েল"
        String title = isBn ? "তারাবীহ নিয়ে মাসআলা-মাসায়েল" : "Rulings and Masail of Taraweeh";
        binding.tvTaraweehMasailHeaderTitle.setText(title);

        // Spring touch on back button
        TouchAnimationUtil.attachTouchSpring(binding.btnBackTaraweehMasail);
        binding.btnBackTaraweehMasail.setOnClickListener(v -> dialog.dismiss());

        // Setup RecyclerView
        binding.rvTaraweehMasailList.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvTaraweehMasailList.setHasFixedSize(true);
        binding.rvTaraweehMasailList.setItemViewCacheSize(10);

        List<RozaTaraweehMasalaItem> items = getMasailList();
        RozaTaraweehMasailAdapter adapter = new RozaTaraweehMasailAdapter(items, isBn);
        binding.rvTaraweehMasailList.setAdapter(adapter);

        dialog.show();
    }

    public static List<RozaTaraweehMasalaItem> getMasailList() {
        List<RozaTaraweehMasalaItem> list = new ArrayList<>();

        // 1. অসুস্থ ও রুগীর জন্য তারাবি প্রসঙ্গে
        list.add(new RozaTaraweehMasalaItem(
                1,
                "অসুস্থ ও রুগীর জন্য তারাবি প্রসঙ্গে",
                "Taraweeh for the Sick and Ill",
                "তারাবির নামাজ প্রাপ্ত বয়স্ক পুরষ-মহিলা সবার ওপর সুন্নতে মোয়াক্কাদা। অসুস্থ ও রুগীর ওপর তারাবি জরুরি নয়, তবে কোনো কষ্ট না হলে তাদেরও পড়া মুস্তাহাব। [রদ্দুল মুহতার: ১/৭৪২] তারাবির নামাজ জামাতে আদায় করা মুস্তাহাব, একাকি আদায় করলেও আদায় হবে। [বাদায়েউস সানায়ে: ১/২৯০]",
                "Taraweeh prayer is Sunnah Mu'akkadah upon all adult men and women. For the sick and infirm, Taraweeh is not obligatory; however, if there is no undue hardship, it is recommended (Mustahabb) for them to perform it. [Radd al-Muhtar: 1/742] Offering Taraweeh prayer in congregation is Mustahabb; praying individually also fulfills the prayer. [Bada'i al-Sana'i: 1/290]",
                "তারাবির নামাজ প্রাপ্ত বয়স্ক পুরষ-মহিলা সবার ওপর সুন্নতে মোয়াক্কাদা। অসুস্থ ও রুগীর ওপর তারাবি জরুরি নয়, তবে কোনো কষ্ট না হলে তাদেরও পড়া মুস্তাহাব।\n\n" +
                        "[রদ্দুল মুহতার: ১/৭৪২]\n\n" +
                        "তারাবির নামাজ জামাতে আদায় করা মুস্তাহাব, একাকি আদায় করলেও আদায় হবে।\n\n" +
                        "[বাদায়েউস সানায়ে: ১/২৯০]",
                "Taraweeh prayer is Sunnah Mu'akkadah upon all adult men and women. For the sick and infirm, Taraweeh is not obligatory; however, if there is no undue hardship, it is recommended (Mustahabb) for them to perform it.\n\n" +
                        "[Radd al-Muhtar: 1/742]\n\n" +
                        "Offering Taraweeh prayer in congregation is Mustahabb; praying individually also fulfills the prayer.\n\n" +
                        "[Bada'i al-Sana'i: 1/290]",
                "[রদ্দুল মুহতার: ১/৭৪২, বাদায়েউস সানায়ে: ১/২৯০]",
                "[Radd al-Muhtar: 1/742, Bada'i al-Sana'i: 1/290]"
        ));

        // 2. তারাবির জামাত থেকে কিছু রাকাত ছুটে গেলে
        list.add(new RozaTaraweehMasalaItem(
                2,
                "তারাবির জামাত থেকে কিছু রাকাত ছুটে গেলে",
                "When Missing Some Rak'ahs of Congregational Taraweeh",
                "কারও যদি তারাবির জামাত থেকে কিছু রাকাত ছুটে যায় তাহলে বেতরের নামাজের পর তা আদায় করে নেবে। [রদ্দুল মুহতার: ২/৪৪]",
                "If someone misses some rak'ahs of the Taraweeh congregation, they should perform those missed rak'ahs after the Witr prayer. [Radd al-Muhtar: 2/44]",
                "কারও যদি তারাবির জামাত থেকে কিছু রাকাত ছুটে যায় তাহলে বেতরের নামাজের পর তা আদায় করে নেবে।\n\n" +
                        "[রদ্দুল মুহতার: ২/৪৪]",
                "If someone misses some rak'ahs of the Taraweeh congregation, they should perform those missed rak'ahs after the Witr prayer.\n\n" +
                        "[Radd al-Muhtar: 2/44]",
                "[রদ্দুল মুহতার: ২/৪৪]",
                "[Radd al-Muhtar: 2/44]"
        ));

        // 3. নাবালেগ হাফেজের পেছনে নামাজ প্রসঙ্গে
        list.add(new RozaTaraweehMasalaItem(
                3,
                "নাবালেগ হাফেজের পেছনে নামাজ প্রসঙ্গে",
                "Praying Behind a Minor Hafiz",
                "নাবালেগ হাফেজের পেছনে বালেগ পুরুষ-মহিলা কারও জন্যই ইক্তিদা করা বৈধ নয়। [আল বাহরুর রায়েক: ১/৩৫৯]",
                "Following a minor (non-adult) Hafiz in prayer is not valid for any adult male or female. [Al-Bahr ar-Ra'iq: 1/359]",
                "নাবালেগ হাফেজের পেছনে বালেগ পুরুষ-মহিলা কারও জন্যই ইক্তিদা করা বৈধ নয়।\n\n" +
                        "[আল বাহরুর রায়েক: ১/৩৫৯]",
                "Following a minor (non-adult) Hafiz in prayer is not valid for any adult male or female.\n\n" +
                        "[Al-Bahr ar-Ra'iq: 1/359]",
                "[আল বাহরুর রায়েক: ১/৩৫৯]",
                "[Al-Bahr ar-Ra'iq: 1/359]"
        ));

        // 4. নামাজে সূরার শুরুতে বিসমিল্লাহ পড়া
        list.add(new RozaTaraweehMasalaItem(
                4,
                "নামাজে সূরার শুরুতে বিসমিল্লাহ পড়া",
                "Reciting Bismillah at the Beginning of Surahs in Prayer",
                "ফরজ, নফল বা তারাবি যে নামাজেই প্রত্যেক সূরার শুরুতে বিসমিল্লাহ নিঃসন্দেহে পড়া সুন্নত। তবে বিসমিল্লাহ নিঃশব্দে পড়া সুন্নত। তাই তারাবির নামাজেও খতমে কোরআনের সময় প্রত্যেক সূরার শুরুতে নিঃশব্দে পড়া সুন্নত। তবে যেহেতু বিসমিল্লাহির রাহমানির রাহিমও কোরআনের একটি আয়াত; তাই মুসল্লিদের খতম পূর্ণ হওয়ার জন্য যেকোনো সূরার শুরু...",
                "Reciting Bismillah at the start of every Surah in any prayer, whether Fard, Nafl, or Taraweeh, is undoubtedly Sunnah. However, reciting Bismillah silently is Sunnah. Therefore, reciting it silently at the beginning of each Surah during Quran completion in Taraweeh is Sunnah...",
                "ফরজ, নফল বা তারাবি যে নামাজেই প্রত্যেক সূরার শুরুতে বিসমিল্লাহ নিঃসন্দেহে পড়া সুন্নত। তবে বিসমিল্লাহ নিঃশব্দে পড়া সুন্নত। তাই তারাবির নামাজেও খতমে কোরআনের সময় প্রত্যেক সূরার শুরুতে নিঃশব্দে পড়া সুন্নত। তবে যেহেতু বিসমিল্লাহির রাহমানির রাহিমও কোরআনের একটি আয়াত; তাই মুসল্লিদের খতম পূর্ণ হওয়ার জন্য যেকোনো সূরার শুরুতে বিসমিল্লাহ স্বশব্দে পড়ে নিলে সবার খতম পূর্ণ হয়ে যাবে। প্রতি সূরার শুরুতে বিসমিল্লাহ স্বশব্দে পড়লেও কোনো সমস্যা নেই। উভয়ের ওপর আমল করার অবকাশ আছে।\n\n" +
                        "[রদ্দুল মুহতার: ১/৪৯০]",
                "Reciting Bismillah at the beginning of every Surah in any prayer—whether Fard, Nafl, or Taraweeh—is undoubtedly Sunnah. However, reciting Bismillah silently is Sunnah. Therefore, in Taraweeh prayer during the completion of the Quran (Khatm al-Quran), reciting it silently at the start of each Surah is Sunnah. However, since 'Bismillahir Rahmanir Rahim' is also an Ayah of the Quran, reciting Bismillah audibly at the beginning of any one Surah suffices for all worshippers to complete their Khatm. Reciting Bismillah audibly at the beginning of each Surah is also unobjectionable. There is scope to act upon both practices.\n\n" +
                        "[Radd al-Muhtar: 1/490]",
                "[রদ্দুল মুহতার: ১/৪৯০]",
                "[Radd al-Muhtar: 1/490]"
        ));

        // 5. ইসলামি শরিয়তের দৃষ্টিতে নামাজের ভেতর লোকমা
        list.add(new RozaTaraweehMasalaItem(
                5,
                "ইসলামি শরিয়তের দৃষ্টিতে নামাজের ভেতর লোকমা",
                "Luqmah (Correction Prompt) During Prayer in Islamic Shariah",
                "ইসলামি শরিয়তের দৃষ্টিতে নামাজের ভেতর লোকমা (নামাজের কেরাতে কোথাও ইমামের সন্দেহ হলে এবং সামনে অগ্রসর হতে না পারলে মুক্তাদির তাকে সহযোগিতা করা উত্তম। সহযোগিতার পদ্ধতি হলো, মুক্তাদি উচ্চস্বরে শুদ্ধভাবে পাঠ করবেন। এটাকে পরিভাষায় ‘লোকমা দেয়া’ বলে। অনেক সময় কেরাত ছাড়াও উঠা-বসার ক্ষেত্রে কোথাও ইমামের ভুল হ...",
                "In the perspective of Islamic Shariah, giving Luqmah during prayer (when the Imam has doubt in recitation and cannot proceed, it is commendable for a follower to assist him by reciting loudly and accurately; this is terminologically termed 'giving Luqmah'...",
                "ইসলামি শরিয়তের দৃষ্টিতে নামাজের ভেতর লোকমা (নামাজের কেরাতে কোথাও ইমামের সন্দেহ হলে এবং সামনে অগ্রসর হতে না পারলে মুক্তাদির তাকে সহযোগিতা করা উত্তম। সহযোগিতার পদ্ধতি হলো, মুক্তাদি উচ্চস্বরে শুদ্ধভাবে পাঠ করবেন। এটাকে পরিভাষায় ‘লোকমা দেয়া’ বলে। অনেক সময় কেরাত ছাড়াও উঠা-বসার ক্ষেত্রে কোথাও ইমামের ভুল হলে তাকে সতর্ক করাকেও লোকমা দেয়া বলে। ইসলামে লোকমা দেয়া ও নেয়ার বিধান রয়েছে। যেগুলো জানা ও মেনে চলা অপরিহার্য) দেয়ার ব্যাপারে তাড়াহুড়ো না করা উচিত এবং ইমাম সাহেবের জন্য লোকমার অপেক্ষা না করে অন্য আয়াত পড়ে নামাজ শেষ করা উচিত। লোকমা দেয়ার সঠিক পদ্ধতি হলো- প্রথমে ইমাম সাহেবকে আয়াত পুনরাবৃত্তির সুযোগ দেয়া। এতদসত্ত্বেও ইমাম সাহেব শুধরে নিতে না পারলে সেক্ষেত্রে মুক্তাদি লোকমা দিলে কোনো ক্ষতি হবে না। তারাবি নামাজে খতমে কোরআনে যদি হাফেজ সাহেব লোকমার অপেক্ষা না করে, তাহলে লোকমা না দিলে কোনো অসুবিধা হবে না। তবে ভুলে যাওয়া আয়াত পরবর্তীতে সূরা ফাতেহার পর পড়ে নিতে হবে।\n\n" +
                        "[রদ্দুল মুহতার: ১/৬২৩]",
                "In the perspective of Islamic Shariah, one should not rush in giving Luqmah during prayer (when the Imam falters in recitation and cannot proceed, it is commendable for a follower to assist him by reciting correctly aloud; this is known as 'giving Luqmah'. Alerting the Imam when he errs in postures is also termed Luqmah. Shariah has established guidelines for giving and receiving Luqmah that are essential to observe), and the Imam should not wait for a Luqmah but rather transition to another verse to conclude prayer. The correct method of giving Luqmah is first to allow the Imam opportunity to repeat the verse. If he is still unable to correct himself, the follower may prompt him without detriment. In Taraweeh Khatm al-Quran, if the Hafiz does not wait for a Luqmah, omitting it causes no harm; however, the omitted verse should be recited subsequently following Surah Al-Fatihah.\n\n" +
                        "[Radd al-Muhtar: 1/623]",
                "[রদ্দুল মুহতার: ১/৬২৩]",
                "[Radd al-Muhtar: 1/623]"
        ));

        // 6. তারাবির নামাজে ভুল করলে
        list.add(new RozaTaraweehMasalaItem(
                6,
                "তারাবির নামাজে ভুল করলে",
                "Mistakes in Taraweeh Prayer",
                "তারাবির নামাজে দ্বিতীয় রাকাতে না বসে দাঁড়িয়ে গেলে তৃতীয় রাকাতে সিজদা করার পূর্বে স্মরণ হলে বসে তাশাহহুদ ও সেজদায়ে সাহু আদায় করলে তেলাওয়াত ও নামাজ শুদ্ধ হয়ে যাবে। যদি তৃতীয় রাকাতে সিজদা করে ফেলে, তবে চতুর্থ রাকাত মিলিয়ে নেবে। এতে শেষের দুই রাকাত তারাবির নামাজ হিসেবে ধর্তব্য হবে এবং শেষ বৈঠক না করার কারণে প্র...",
                "If in Taraweeh prayer one stands up instead of sitting for the second rak'ah, and remembers before prostrating in the third rak'ah, sitting down and performing Tashahhud and Sahw Sajdah renders both recitation and prayer valid. If one has already prostrated in the third rak'ah, one should complete a fourth rak'ah...",
                "তারাবির নামাজে দ্বিতীয় রাকাতে না বসে দাঁড়িয়ে গেলে তৃতীয় রাকাতে সিজদা করার পূর্বে স্মরণ হলে বসে তাশাহহুদ ও সেজদায়ে সাহু আদায় করলে তেলাওয়াত ও নামাজ শুদ্ধ হয়ে যাবে। যদি তৃতীয় রাকাতে সিজদা করে ফেলে, তবে চতুর্থ রাকাত মিলিয়ে নেবে। এতে শেষের দুই রাকাত তারাবির নামাজ হিসেবে ধর্তব্য হবে এবং শেষ বৈঠক না করার কারণে প্রথম দুই রাকাত তারাবি হিসেবে গণ্য না হওয়ায় তেলাওয়াতসহ পুনরায় পড়তে হবে।\n\n" +
                        "[বাদায়েউস সানায়ে: ১/২৮৯]\n\n" +
                        "যদি কোনো ব্যক্তি তারাবির নামাজ চার রাকাতের নিয়ত করে শুরু করে এবং ভুলে দুই রাকাতের পর বৈঠক না করে চার রাকাত শেষ করেই বৈঠক করে, তাহলে সে যদি নামাজ শেষে সেজদায়ে সাহু করে থাকে, তবে শুধু শেষের দুই রাকাত তারাবি হিসেবে গণ্য হবে।\n\n" +
                        "[আল বাহরুর রায়েক: ২/১১৭]",
                "If in Taraweeh prayer one mistakenly stands up instead of sitting in the second rak'ah, and remembers before performing the prostration (Sajdah) of the third rak'ah, sitting down immediately and performing Tashahhud followed by Sajdah as-Sahw renders both the recitation and prayer valid. If one has already prostrated in the third rak'ah, one should add a fourth rak'ah. In that case, the final two rak'ahs will count as Taraweeh prayer, and because the sitting after the second rak'ah was missed, the first two rak'ahs do not count as Taraweeh and must be repeated along with the recitation.\n\n" +
                        "[Bada'i al-Sana'i: 1/289]\n\n" +
                        "If someone initiates Taraweeh with the intention of praying four rak'ahs and mistakenly sits only after finishing four rak'ahs without sitting after two rak'ahs, then if they perform Sajdah as-Sahw at the conclusion, only the final two rak'ahs are credited as Taraweeh.\n\n" +
                        "[Al-Bahr ar-Ra'iq: 2/117]",
                "[বাদায়েউস সানায়ে: ১/২৮৯, আল বাহরুর রায়েক: ২/১১৭]",
                "[Bada'i al-Sana'i: 1/289, Al-Bahr ar-Ra'iq: 2/117]"
        ));

        // 7. বসে তারাবির নামাজ
        list.add(new RozaTaraweehMasalaItem(
                7,
                "বসে তারাবির নামাজ",
                "Performing Taraweeh Sitting Down",
                "মাটিতে বসে রুকু-সেজদার মাধ্যমে তারাবি নামাজ বৈধ। অনুরূপ তারাবির কেরাতের সময় চেয়ারে বসে রুকু-সেজদা নামাজের নিয়মমাফিক আদায় করলে তাও বৈধ। কিন্তু বিনা ওজরে এরূপ করলে নামাজের পূর্ণ সওয়াব পাবে না, বরং অর্ধেক সওয়াব পাবে। তবে হ্যাঁ, নিয়মমাফিক রুকু সেজদায় সক্ষম ব্যক্তি চেয়ারে বসে ইশারায় রুকু সেজদার মাধ্যমে না...",
                "Offering Taraweeh prayer sitting on the floor with full bowing and prostration is permissible. Similarly, sitting on a chair during recitation but bowing and prostrating on the floor according to the prescribed manner is also valid. However, doing so without a valid excuse yields only half the reward...",
                "মাটিতে বসে রুকু-সেজদার মাধ্যমে তারাবি নামাজ বৈধ। অনুরূপ তারাবির কেরাতের সময় চেয়ারে বসে রুকু-সেজদা নামাজের নিয়মমাফিক আদায় করলে তাও বৈধ। কিন্তু বিনা ওজরে এরূপ করলে নামাজের পূর্ণ সওয়াব পাবে না, বরং অর্ধেক সওয়াব পাবে। তবে হ্যাঁ, নিয়মমাফিক রুকু সেজদায় সক্ষম ব্যক্তি চেয়ারে বসে ইশারায় রুকু সেজদার মাধ্যমে নামাজ পড়লে নামাজ শুদ্ধ হবে না।\n\n" +
                        "[ফাতাওয়ায়ে হিন্দিয়া: ১/১১৮]",
                "Offering Taraweeh prayer sitting on the floor with regular bowing (Ruku) and prostration (Sajdah) is permissible. Similarly, sitting on a chair during recitation but performing bowing and prostration on the floor in the prescribed manner is also valid. However, doing so without a legitimate excuse forfeits full reward, yielding only half reward. However, someone capable of regular bowing and prostration who sits on a chair and performs Ruku and Sajdah merely through gestures will not have their prayer validated.\n\n" +
                        "[Fatawa al-Hindiyyah: 1/118]",
                "[ফাতাওয়ায়ে হিন্দিয়া: ১/১১৮]",
                "[Fatawa al-Hindiyyah: 1/118]"
        ));

        return list;
    }
}
