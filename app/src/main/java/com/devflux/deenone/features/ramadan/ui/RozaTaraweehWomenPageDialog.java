package com.devflux.deenone.features.ramadan.ui;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageRozaTaraweehWomenBinding;
import com.devflux.deenone.features.ramadan.adapter.RozaTaraweehMasailAdapter;
import com.devflux.deenone.features.ramadan.model.RozaTaraweehMasalaItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Production-ready full-screen Page Dialog for "নারীদের জন্য তারাবীহ" (Topic 9).
 * Exactly matches the user screenshot with individual expandable cards:
 * 1. নারীদের তারাবীর নামায প্রসঙ্গে
 * 2. নারীদের তারাবি কোথায় পড়া উত্তম
 * 100% verbatim lines, pure dual-language mode, 60 FPS smooth spring touches.
 */
public class RozaTaraweehWomenPageDialog {

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageRozaTaraweehWomenBinding binding = PageRozaTaraweehWomenBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Header Title matching screenshot verbatim: "নারীদের জন্য তারাবীহ"
        String title = isBn ? "নারীদের জন্য তারাবীহ" : "Taraweeh for Women";
        binding.tvTaraweehWomenHeaderTitle.setText(title);

        // Spring touch on back button
        TouchAnimationUtil.attachTouchSpring(binding.btnBackTaraweehWomen);
        binding.btnBackTaraweehWomen.setOnClickListener(v -> dialog.dismiss());

        // Setup RecyclerView
        binding.rvTaraweehWomenList.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvTaraweehWomenList.setHasFixedSize(true);
        binding.rvTaraweehWomenList.setItemViewCacheSize(5);

        List<RozaTaraweehMasalaItem> items = getWomenTaraweehList();
        RozaTaraweehMasailAdapter adapter = new RozaTaraweehMasailAdapter(items, isBn);
        binding.rvTaraweehWomenList.setAdapter(adapter);

        dialog.show();
    }

    public static List<RozaTaraweehMasalaItem> getWomenTaraweehList() {
        List<RozaTaraweehMasalaItem> list = new ArrayList<>();

        // Card 1: নারীদের তারাবীর নামায প্রসঙ্গে
        list.add(new RozaTaraweehMasalaItem(
                1,
                "নারীদের তারাবীর নামায প্রসঙ্গে",
                "Rulings on Women's Taraweeh Prayer",
                "তারাবীর নামায সুন্নতে মুয়াক্কাদা। নারীদের জন্যে কিয়ামুল লাইল (রাতের নামায) ঘরে পড়া উত্তম। যেহেতু নবী সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম বলেন: “নারীদেরকে মসজিদে যেতে বাধা দিও না। তবে, তাদের জন্য ঘরই উত্তম [সহিহুল ৭৪৫৮]",
                "Taraweeh prayer is Sunnah Mu'akkadah. For women, performing Qiyam al-Layl (the night prayer) at home is more virtuous. As the Prophet ﷺ stated: 'Do not prevent women from going to mosques; however, their homes are better for them.' [Sahihul 7458]",
                "তারাবীর নামায সুন্নতে মুয়াক্কাদা। নারীদের জন্যে কিয়ামুল লাইল (রাতের নামায) ঘরে পড়া উত্তম। যেহেতু নবী সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম বলেন: “নারীদেরকে মসজিদে যেতে বাধা দিও না। তবে, তাদের জন্য ঘরই উত্তম।\n\n" +
                        "[সহিহুল ৭৪৫৮]",
                "Taraweeh prayer is Sunnah Mu'akkadah. For women, performing Qiyam al-Layl (the night prayer) at home is more virtuous. As the Prophet ﷺ stated: 'Do not prevent women from going to mosques; however, their homes are better for them.'\n\n" +
                        "[Sahihul 7458]",
                "[সহিহুল ৭৪৫৮]",
                "[Sahihul 7458]"
        ));

        // Card 2: নারীদের তারাবি কোথায় পড়া উত্তম
        list.add(new RozaTaraweehMasalaItem(
                2,
                "নারীদের তারাবি কোথায় পড়া উত্তম",
                "Where It Is Most Virtuous for Women to Pray Taraweeh",
                "নারীর নামাযের স্থান যতবেশী নির্জনে হবে, যতবেশি ব্যক্তিগত হবে সেটাই উত্তম। যেহেতু নবী সাল্লল্লাহু আলাইহি ওয়া সাল্লাম বলেছেন, “মহিলাদের জন্য শোয়ার ঘরে নামায আদায় করা বৈঠকখানায় নামায আদায় করার চেয়ে উত্তম। তাদের জন্য গোপন প্রকোষ্ঠে নামায করা শোয়ার ঘরে নামায আদায় করার চেয়ে উত্তম। [সহিহুল ৩৮৩৩] আবু হুমাইদ আল-সা...",
                "The more secluded and private a woman's place of prayer is, the more virtuous it is. The Prophet ﷺ said: 'A woman's prayer in her inner bedroom is better than her prayer in her living room, and her prayer in her innermost chamber is better than her prayer in her bedroom.' [Sahihul 3833] Umm Humaid, the wife of Abu Humaid as-Sa'idi...",
                "নারীর নামাযের স্থান যতবেশী নির্জনে হবে, যতবেশি ব্যক্তিগত হবে সেটাই উত্তম। যেহেতু নবী সাল্লল্লাহু আলাইহি ওয়া সাল্লাম বলেছেন, “মহিলাদের জন্য শোয়ার ঘরে নামায আদায় করা বৈঠকখানায় নামায আদায় করার চেয়ে উত্তম। তাদের জন্য গোপন প্রকোষ্ঠে নামায করা শোয়ার ঘরে নামায আদায় করার চেয়ে উত্তম।\n\n" +
                        "[সহিহুল ৩৮৩৩]\n\n" +
                        "আবু হুমাইদ আল-সায়েদি এর স্ত্রী উম্মে হুমাইদ থেকে বর্ণিত তিনি একবার নবী সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের কাছে এসে বললেন: ইয়া রাসূলুল্লাহ্! আমি আপনার সাথে নামায আদায় করতে পছন্দ করি। তখন তিনি বললেন: আমি জেনেছি আপনি আমার সাথে নামায পড়া পছন্দ করেন। কিন্তু, আপনি আপনার শোয়ার ঘরে নামায আদায় করা বৈঠক ঘরে নামায আদায় করার চেয়ে উত্তম। আপনি আপনার বৈঠক ঘরে নামায আদায় করা বাড়ীর উঠোনে নামায আদায় করার চেয়ে উত্তম। আপনি আপনার বাড়ীর উঠোনে নামায আদায় করা গোত্রীয় মসজিদে নামায আদায় করার চেয়ে উত্তম। আপনি আপনার গোত্রীয় মসজিদে নামায আদায় করা আমার মসজিদে নামায আদায় করার চেয়ে উত্তম। বর্ণনাকারী বলেন: ফলে তিনি তার ঘরের একেবারে ভিতরে অন্ধকার স্থানে তার জন্য নামাযের জায়গা বানানোর নির্দেশ দিলেন। তিনি মৃত্যু পর্যন্ত সে জায়গায় নামায আদায় করেছেন।\n\n" +
                        "[মুসনাদে আহমাদ, হাদিসটির বর্ণনাকারীগণ নির্ভরযোগ্য]\n\n" +
                        "তবে উল্লেখিত ফযিলত নারীদেরকে মসজিদে যাওয়ার অনুমতি দেয়ার ক্ষেত্রে প্রতিবন্ধক নয়। যেমনটি আব্দুল্লাহ্ বিন উমর (রাঃ) কর্তৃক হাদিসে এসেছে, তিনি বলেন: আমি রাসূলুল্লাহ্ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামকে বলতে শুনেছি তিনি বলেন: যদি নারীরা তোমাদের কাছে মসজিদে যেতে অনুমতি চায় তাহলে তোমরা তাদেরকে মসজিদে যেতে বাধা দিও না। বর্ণনাকারী বলেন, তখন বিলাল বিন আব্দুল্লাহ্ (বিন উমর) বলল: আল্লাহ্র শপথ, অবশ্যই আমরা তাদেরকে বাধা দিব। বর্ণনাকারী বলেন: তখন আব্দুল্লাহ্ তার দিকে এগিয়ে এসে তাকে তীব্র গালমন্দ করলেন; আমি তাঁর কাছ থেকে এমন কথা আর কখনও শুনিনি। এবং তিনি বললেন: আমি তোমাকে রাসূলুল্লাহ্ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম থেকে হাদিস জানাচ্ছি। আর তুমি বল: আল্লাহ্র শপথ, অবশ্যই আমরা তাদেরকে বাধা দিব।\n\n" +
                        "[সহিহ মুসলিম: ৬৬৭]\n\n" +
                        "কিন্তু, কোন নারী মসজিদে যাওয়ার ক্ষেত্রে নিম্নোক্ত শর্ত রয়েছে:\n\n" +
                        "পরিপূর্ণ হিজাব থাকতে হবে।\n" +
                        "সুগন্ধি লাগিয়ে যাবে না।\n" +
                        "স্বামীর অনুমতি লাগবে।\n\n" +
                        "এবং এ বের হওয়ার ক্ষেত্রে অন্য আরেকটি হারাম যেন সংঘটিত না হয়; যেমন একাকী ড্রাইভারের সাথে বের হওয়া। যদি কোন নারী উল্লেখিত শর্তগুলোর কোনটি ভঙ্গ করে সেক্ষেত্রে নারীর স্বামী কিংবা অভিভাবক তাকে মসজিদে যেতে বাধা দিতে পারবেন; বরং বাধা দেওয়া আবশ্যক হবে।",
                "The more secluded and private a woman's place of prayer is, the more virtuous it is. The Prophet ﷺ said: 'A woman's prayer in her inner bedroom is better than her prayer in her living room, and her prayer in her innermost chamber is better than her prayer in her bedroom.'\n\n" +
                        "[Sahihul 3833]\n\n" +
                        "Narrated from Umm Humaid, the wife of Abu Humaid as-Sa'idi, that she came to the Prophet ﷺ and said: 'O Messenger of Allah! I love to pray with you.' He ﷺ replied: 'I know that you love to pray with me, but your prayer in your inner room is better for you than your prayer in your living chamber; your prayer in your living chamber is better than your prayer in your household courtyard; your prayer in your household courtyard is better than your prayer in the mosque of your tribe; and your prayer in the mosque of your tribe is better than your prayer in my mosque.' The narrator said: She then ordered a place of prayer to be built for her in the deepest, darkest corner of her home, and she prayed there until her death.\n\n" +
                        "[Musnad Ahmad, authentic chain of narrators]\n\n" +
                        "However, the mentioned virtues do not preclude granting women permission to go to the mosque. As narrated by Abdullah ibn Umar (RA): 'I heard the Messenger of Allah ﷺ say: \"If your women ask permission to go to the mosque, do not prevent them.\"' The narrator stated that Bilal ibn Abdullah (ibn Umar) said: 'By Allah, we will certainly prevent them.' The narrator said: Abdullah turned to him and rebuked him severely in words I had never heard him speak before, saying: 'I convey to you a Hadith from the Messenger of Allah ﷺ and you say: \"By Allah, we will certainly prevent them\"!'\n\n" +
                        "[Sahih Muslim: 667]\n\n" +
                        "However, for a woman going to the mosque, the following conditions must strictly be observed:\n\n" +
                        "Full and proper Hijab must be maintained.\n" +
                        "No perfume or fragrance may be worn.\n" +
                        "Permission of the husband must be obtained.\n\n" +
                        "And her going out must not involve any prohibited act, such as being in seclusion alone with a driver. If any woman breaches these conditions, her husband or guardian has the right to prevent her from going to the mosque; rather, preventing her becomes an obligation.",
                "[সহিহুল ৩৮৩৩, মুসনাদে আহমাদ, সহিহ মুসলিম: ৬৬৭]",
                "[Sahihul 3833, Musnad Ahmad, Sahih Muslim: 667]"
        ));

        return list;
    }
}
