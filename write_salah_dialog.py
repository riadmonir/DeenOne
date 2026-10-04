
# -*- coding: utf-8 -*-
content = """package com.devlabs.deanone.features.salahguide;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.devlabs.deanone.R;
import com.devlabs.deanone.core.ui.FullScreenPageDialog;
import com.devlabs.deanone.databinding.PageSalahVisualGuideBinding;
import com.devlabs.deanone.utils.BengaliNumberUtil;
import com.devlabs.deanone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class SalahVisualGuideDialog {

    public static class VisualStepItem {
        final int imageRes;
        final String mainText;
        final String duaText;
        final String notes;

        public VisualStepItem(int imageRes, String mainText, String duaText, String notes) {
            this.imageRes = imageRes;
            this.mainText = mainText;
            this.duaText = duaText;
            this.notes = notes;
        }
    }

    private static boolean isMaleSelected = true;
    private static int currentStepIndex = 0;

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahVisualGuideBinding binding = PageSalahVisualGuideBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        currentStepIndex = 0;
        isMaleSelected = true;

        binding.btnBackVisualGuide.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackVisualGuide);

        binding.tabGenderMale.setOnClickListener(v -> {
            if (!isMaleSelected) {
                isMaleSelected = true;
                currentStepIndex = 0;
                updateGenderTabs(binding, activity);
                renderStep(binding, activity);
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.tabGenderMale);

        binding.tabGenderFemale.setOnClickListener(v -> {
            if (isMaleSelected) {
                isMaleSelected = false;
                currentStepIndex = 0;
                updateGenderTabs(binding, activity);
                renderStep(binding, activity);
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.tabGenderFemale);

        binding.btnPrevStep.setOnClickListener(v -> {
            if (currentStepIndex > 0) {
                currentStepIndex--;
                renderStep(binding, activity);
                binding.scrollVisualContent.smoothScrollTo(0, 0);
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnPrevStep);

        binding.btnNextStep.setOnClickListener(v -> {
            boolean isBn = com.devlabs.deanone.core.localization.LocaleManager.isBengali(activity);
            List<VisualStepItem> steps = isMaleSelected ? getMaleSteps(isBn) : getFemaleSteps(isBn);
            if (currentStepIndex < steps.size() - 1) {
                currentStepIndex++;
                renderStep(binding, activity);
                binding.scrollVisualContent.smoothScrollTo(0, 0);
            } else {
                dialog.dismiss();
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnNextStep);

        updateGenderTabs(binding, activity);
        renderStep(binding, activity);

        binding.ivStepPhoto.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (right - left != oldRight - oldLeft || bottom - top != oldBottom - oldTop) {
                boolean isBn = com.devlabs.deanone.core.localization.LocaleManager.isBengali(activity);
                List<VisualStepItem> steps = isMaleSelected ? getMaleSteps(isBn) : getFemaleSteps(isBn);
                if (!steps.isEmpty() && currentStepIndex >= 0 && currentStepIndex < steps.size()) {
                    applySmartImageCrop(binding.ivStepPhoto, activity, steps.get(currentStepIndex).imageRes);
                }
            }
        });

        dialog.show();
    }

    private static void updateGenderTabs(PageSalahVisualGuideBinding binding, Activity activity) {
        boolean isBn = com.devlabs.deanone.core.localization.LocaleManager.isBengali(activity);
        binding.tabGenderMale.setText(isBn ? "পুরুষের নামাজ" : "Men's Prayer");
        binding.tabGenderFemale.setText(isBn ? "মহিলাদের নামাজ" : "Women's Prayer");

        if (isMaleSelected) {
            binding.tabGenderMale.setBackgroundResource(R.drawable.bg_tab_visual_active);
            binding.tabGenderMale.setTextColor(Color.WHITE);
            binding.tabGenderMale.setTypeface(null, android.graphics.Typeface.BOLD);

            binding.tabGenderFemale.setBackgroundResource(R.drawable.bg_tab_visual_inactive);
            binding.tabGenderFemale.setTextColor(ContextCompat.getColor(activity, R.color.text_secondary));
            binding.tabGenderFemale.setTypeface(null, android.graphics.Typeface.NORMAL);
        } else {
            binding.tabGenderFemale.setBackgroundResource(R.drawable.bg_tab_visual_active);
            binding.tabGenderFemale.setTextColor(Color.WHITE);
            binding.tabGenderFemale.setTypeface(null, android.graphics.Typeface.BOLD);

            binding.tabGenderMale.setBackgroundResource(R.drawable.bg_tab_visual_inactive);
            binding.tabGenderMale.setTextColor(ContextCompat.getColor(activity, R.color.text_secondary));
            binding.tabGenderMale.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
    }

    private static void renderStep(PageSalahVisualGuideBinding binding, Activity activity) {
        boolean isBn = com.devlabs.deanone.core.localization.LocaleManager.isBengali(activity);
        List<VisualStepItem> steps = isMaleSelected ? getMaleSteps(isBn) : getFemaleSteps(isBn);
        if (currentStepIndex < 0) currentStepIndex = 0;
        if (currentStepIndex >= steps.size()) currentStepIndex = steps.size() - 1;

        VisualStepItem step = steps.get(currentStepIndex);

        String genderTitle = isMaleSelected
                ? (isBn ? "পুরুষের নামাজ" : "Men's Prayer")
                : (isBn ? "মহিলাদের নামাজ" : "Women's Prayer");
        String stepText = isBn
                ? ("ধাপ " + BengaliNumberUtil.toBengali(currentStepIndex + 1))
                : ("Step " + (currentStepIndex + 1));
        binding.tvHeaderTitle.setText(genderTitle + " - " + stepText);

        applySmartImageCrop(binding.ivStepPhoto, activity, step.imageRes);

        binding.tvStepBadge.setText(isBn
                ? (stepText + " / " + BengaliNumberUtil.toBengali(steps.size()))
                : (stepText + " / " + steps.size()));

        binding.tvStepDescription.setText(step.mainText);

        if (step.duaText != null && !step.duaText.trim().isEmpty()) {
            binding.cardDua.setVisibility(View.VISIBLE);
            binding.tvStepDua.setText(step.duaText);
        } else {
            binding.cardDua.setVisibility(View.GONE);
        }

        if (step.notes != null && !step.notes.trim().isEmpty()) {
            binding.cardHadith.setVisibility(View.VISIBLE);
            binding.tvStepHadith.setText(step.notes);
        } else {
            binding.cardHadith.setVisibility(View.GONE);
        }

        if (currentStepIndex == 0) {
            binding.btnPrevStep.setVisibility(View.INVISIBLE);
        } else {
            binding.btnPrevStep.setVisibility(View.VISIBLE);
            binding.tvPrevText.setText(isBn ? "পূর্ববর্তী ধাপ" : "Previous Step");
        }

        if (currentStepIndex == steps.size() - 1) {
            binding.tvNextText.setText(isBn ? "সমাপ্ত" : "Finish");
            binding.ivNextIcon.setImageResource(R.drawable.ic_check_circle);
            binding.tvNextText.setTextColor(ContextCompat.getColor(activity, R.color.accent_teal));
            binding.ivNextIcon.setColorFilter(ContextCompat.getColor(activity, R.color.accent_teal));
        } else {
            binding.tvNextText.setText(isBn ? "পরবর্তী ধাপ" : "Next Step");
            binding.ivNextIcon.setImageResource(R.drawable.ic_chevron_right);
            int teal = ContextCompat.getColor(activity, R.color.accent_teal);
            binding.tvNextText.setTextColor(teal);
            binding.ivNextIcon.setColorFilter(teal);
        }
    }

    private static List<VisualStepItem> getMaleSteps(boolean isBn) {
        List<VisualStepItem> list = new ArrayList<>();

        // ধাপ ১: কিয়াম ও নিয়ত
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_1,
                isBn ? "নামাজ পড়ার আগে মন দিয়ে আল্লাহর কাছে নামাজের উদ্দেশ্য ঠিক করতে হবে। মনোযোগ সহকারে নামাজের জন্য নিয়ত করতে হবে।"
                     : "Before starting prayer, sincerely set your intention in your heart purely for Allah.",
                null,
                isBn ? "উদাহরণ: 'আমি দুই রাকাত ফরজ নামাজ আদায় করছি আল্লাহর জন্য।' প্রাথমিক নিয়ত করে নামাজ শুরু করতে হয়। (হাদিস: সুনানে আবু দাউদ: ৭৫৮)"
                     : "Example: 'I intend to offer two Rak'ahs of Fard prayer for Allah.' The intention is made in the heart. (Hadith: Sunan Abi Dawud: 758)"
        ));

        // ধাপ ২: তাকবীরে তাহরীমা
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_2,
                isBn ? "কিবলামুখী হয়ে আপনার উভয় হাত কান বরাবর ওপরে তুলুন এবং বলুন:"
                     : "Facing the Qiblah, raise both hands up to ear level and recite:",
                isBn ? "আরবি: اللَّهُ أَكْبَرُ\n\nউচ্চারণ: আল্লাহু আকবার।\nঅর্থ: আল্লাহ সর্বশ্রেষ্ঠ।"
                     : "Arabic: اللَّهُ أَكْبَرُ\n\nTransliteration: Allahu Akbar.\nMeaning: Allah is the Greatest.",
                isBn ? "আপনার নামাজ এখান থেকে শুরু হলো।"
                     : "Your prayer begins here."
        ));

        // ধাপ ৩: হাত বাঁধা ও ছানা পাঠ
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_3,
                isBn ? "ডান হাত বাম হাতের ওপর রেখে নাভির ওপর বাঁধুন এবং দৃষ্টি সিজদার স্থানে রাখুন। তাকবীরে তাহরীমার পরেই পড়ুন ছানা:"
                     : "Place your right hand over the left over your navel and keep your gaze at the place of prostration. Recite Sana:",
                isBn ? "আরবি: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nউচ্চারণ: সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়া লা-ইলাহা গাইরুক।"
                     : "Arabic: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nTransliteration: Subhanak Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruk.",
                isBn ? "তা’য়াউজঃ আউ’যুবিল্লাহি মিনাশশাইত্বানির রাজীম\n\nতাসমিয়াহ্ঃ বিসমিল্লাহির রাহমানির রাহীম\n\nসুরা ফাতিহা পড়া ফরজ। (কুরআন: ১: ১-৭), প্রত্যেক নামাজে সুরা ফাতিহা পড়ুন এবং অন্য যেকোনো একটি সূরা তেলাওয়াত করুন।"
                     : "Ta'awwudh: A'udhu billahi minash-shaytanir-rajim\n\nTasmiyah: Bismillahir-Rahmanir-Rahim\n\nReciting Surah Al-Fatiha is obligatory (Quran 1:1-7). Recite Surah Al-Fatiha in every Rak'ah followed by any other Surah."
        ));

        // ধাপ ৪: রুকু
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_4,
                isBn ? "এবার আল্লাহু আকবার বলে রুকুতে যান। রুকুর মুহূর্তে আপনার হাত আপনার হাঁটুতে এবং আপনার চোখের দৃষ্টি সিজদার স্থানে হওয়া উচিত। আপনার শরীরটি মাটির সাথে সমান্তরাল রাখুন।"
                     : "Say 'Allahu Akbar' and bow into Ruku. Place your hands on your knees and look at the place of Sajdah. Keep your back parallel to the ground.",
                isBn ? "রুকুর দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الْعَظِيمِ\nউচ্চারণ: সুবহানা রব্বিয়াল আযীম। (৩ বার)\nঅর্থ: আমি আমার মহান প্রভুর পবিত্রতা বর্ণনা করছি।"
                     : "Ruku Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الْعَظِيمِ\nTransliteration: Subhana Rabbiyal Azeem (3 times)\nMeaning: Glory be to my Lord, the Almighty.",
                isBn ? "রুকুতে এই দোয়া ৩, ৫, ৭ বা বিজোড় সংখ্যক বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)"
                     : "Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)"
        ));

        // ধাপ ৫: কওমা - রুকু থেকে উঠে দাঁড়ানো
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_5,
                isBn ? "রুকু থেকে উঠে দাঁড়াতে দাঁড়াতে পড়ুন:"
                     : "Rise straight from Ruku into standing position (Qawmah) and recite:",
                isBn ? "আরবিঃ سَمِعَ اللهُ لِمَنْ حَمِدَهُ\nউচ্চারণ: সামিয়াল্লাহু লিমান হামিদাহ\nঅর্থ: যে ব্যক্তি আল্লাহর প্রশংসা করে, আল্লাহ তা শুনে থাকেন।"
                     : "Arabic: سَمِعَ اللهُ لِمَنْ حَمِدَهُ\nTransliteration: Sami' Allahu liman hamidah\nMeaning: Allah hears whoever praises Him.",
                isBn ? "এরপর সোজা হয়ে দাঁড়িয়ে এই দোয়াটি পড়ুন:\nউচ্চারণ: রাব্বানা লাকাল হামদু হামদান কাসীরান তাইয়িবান মুবারাকান ফীহ্\n\nঅর্থ: হে আমাদের পরওয়ারদিগার! তোমারই জন্যে বহু পবিত্র প্রশংসা রয়েছে, যার মধ্যে বরকতও নিহিত আছে।"
                     : "Then standing upright, recite:\nTransliteration: Rabbana lakal-hamd, hamdan katheeran tayyiban mubarakan feeh\n\nMeaning: Our Lord, to You belongs all praise, abundant, pure and blessed praise."
        ));

        // ধাপ ৬: সিজদা
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_6,
                isBn ? "এবার 'আল্লাহু আকবার' বলে ধীরে ধীরে মাটিতে সিজদায় যান এবং কপাল, নাক, হাত, হাঁটু ও পায়ের পাতা মাটিতে স্পর্শ করুন।"
                     : "Say 'Allahu Akbar' and prostrate into Sajdah, placing your forehead, nose, palms, knees and toes on the ground.",
                isBn ? "সিজদার দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الأَعْلَى\nউচ্চারণ: সুবহানা রাব্বিয়াল আ'লা (৩ বার)\nঅর্থ: আমি আমার সর্বোচ্চ প্রভুর পবিত্রতা বর্ণনা করছি।"
                     : "Sajdah Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الأَعْلَى\nTransliteration: Subhana Rabbiyal A'la (3 times)\nMeaning: Glory be to my Lord, the Most High.",
                isBn ? "সিজদায় এই দোয়া ৩, ৫ বা ৭ বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)"
                     : "Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)"
        ));

        // ধাপ ৭: দুই সিজদার মধ্যবর্তী বৈঠক (জালসা)
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_7,
                isBn ? "সিজদা থেকে উঠুন এবং কিছুক্ষণের জন্য শান্তভাবে বসুন ও এই দোয়াটি পড়ুন:"
                     : "Rise from Sajdah and sit calmly between the two prostrations (Jalsah), then recite:",
                isBn ? "দুই সিজদার মধ্যবর্তী দোয়াঃ\n\nআরবিঃ اَللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي\nউচ্চারণ: আল্লাহুম্মাগফির লী ওয়ারহামনী ওয়াজবুরনী ওয়াহদিনী ওয়া আফিনী ওয়ারযুক্বনী।\nঅর্থ: হে আল্লাহ! আপনি আমাকে ক্ষমা করুন, রহম করুন, আমার অবস্থা সংশোধন করুন, সৎপথ দেখান, সুস্থতা দিন ও জীবিকা দান করুন।"
                     : "Jalsah Dua:\n\nArabic: اَللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي\nTransliteration: Allahummagh-fir lee, warhamnee, wajburnee, wahdinee, wa 'aafinee, warzuqnee.\nMeaning: O Allah! Forgive me, have mercy on me, guide me, grant me well-being and provide for me.",
                isBn ? "এরপর দ্বিতীয় সিজদা করে দ্বিতীয় রাকাতের জন্য দাঁড়ান।"
                     : "Perform the second Sajdah and stand up for the second Rak'ah."
        ));

        // ধাপ ৮: তাশাহহুদ বা বৈঠক
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_8,
                isBn ? "প্রতি দুই রাকাতের শেষে এবং শেষ রাকাতে শান্তভাবে বসুন ও তাশাহহুদ পড়ুন:"
                     : "Sit comfortably after every two Rak'ahs and in the final sitting to recite Tashahhud:",
                isBn ? "তাশাহ্হুদ (আত্তাহিয়্যাতু):\n\nاَلتَّحِيَّاتُ لِلّٰهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nউচ্চারণ: আত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু ওয়াত-তৈয়্যিবাতু আসসালামু আলাইকা আইয়্যুহান নাবিয়্যু ওয়া রহমাতুল্লাহি ওয়া বারাকাতুহু, আসসালামু আলাইনা ওয়া আলা ইবাদিল্লাহিস সালিহীন, আশহাদু আল্লা ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসুলুহু।"
                     : "Tashahhud:\n\nاَلتَّحِيَّاتُ لِلّٰهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nTransliteration: At-tahiyyatu lillahi was-salawatu wat-tayyibat. As-salamu 'alayka ayyuhan-Nabiyyu wa rahmatullahi wa barakatuh. As-salamu 'alayna wa 'ala 'ibadillahis-saliheen. Ash-hadu alla ilaha illallahu wa ash-hadu anna Muhammadan 'abduhu wa rasooluh.",
                isBn ? "অর্থ: যাবতীয় সম্মান, উপাসনা ও পবিত্র বিষয় আল্লাহর জন্য। হে নবী! আপনার উপর শান্তি, আল্লাহর রহমত ও বরকত বর্ষিত হোক। শান্তি বর্ষিত হোক আমাদের উপর ও আল্লাহর সৎকর্মশীল বান্দাদের উপর। আমি সাক্ষ্য দিচ্ছি যে আল্লাহ ছাড়া কোনো উপাস্য নেই এবং মুহাম্মদ (সা.) তাঁর বান্দা ও রাসুল।"
                     : "Meaning: All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and Allah's mercy and blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no deity except Allah, and Muhammad is His slave and Messenger."
        ));

        // ধাপ ৯: শেষ বৈঠক - দরুদ ও দোয়া মাসূরা
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_8,
                isBn ? "শেষ রাকাতে তাশাহহুদের পর দরুদে ইবরাহিম ও দোয়ায়ে মাসূরা পড়ুন:"
                     : "In the final sitting, recite Durood-e-Ibrahim followed by Dua Masura:",
                isBn ? "দরুদঃ\n\nاَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ- اَللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ\n\nদোআয়ে মাছুরাহঃ\n\nاَللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّনُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ"
                     : "Durood Ibrahim:\n\nاَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ- اَللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ\n\nDua Masura:\n\nاَللّٰهُمَّ إِنِّই ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّনُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
                isBn ? "দরুদ ও মাসূরা পড়া শেষে সালামের মাধ্যমে নামাজ সম্পন্ন করুন।"
                     : "After completing Durood and Dua Masura, finish your prayer with Tasleem (Salam)."
        ));

        // ধাপ ১০: সালাম ফিরানো
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_9,
                isBn ? "আপনার ডান দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে সালাম দিন:"
                     : "Turn your face to the right shoulder and say:",
                isBn ? "সালাম ফিরাবার দোয়াঃ\n\nআরবিঃ اَلسَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্"
                     : "Arabic: اَلسَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
                null
        ));

        // ধাপ ১১: বাম দিকে সালাম ফিরানো
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_10,
                isBn ? "এবং তারপরে বাম দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে আবার বলুন:"
                     : "Then turn your face to the left shoulder and say:",
                isBn ? "সালাম ফিরাবার দোয়াঃ\n\nআরবিঃ اَلسَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্"
                     : "Arabic: اَلسَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
                isBn ? "এখানে আপনার নামাজ শেষ হলো।"
                     : "Your Salah is now completed."
        ));

        // ধাপ ১২: সমাপ্তি ও দোয়া
        list.add(new VisualStepItem(
                R.drawable.img_salah_male_step_11,
                isBn ? "সালাম ফেরানোর মাধ্যমে নামাজ পূর্ণ হলো। এরপর ৩ বার আস্তাগফিরুল্লাহ পাঠ ও মোনাজাত করা উত্তম।"
                     : "After finishing Salah, it is Sunnah to seek forgiveness (Astaghfirullah 3 times) and supplicate.",
                isBn ? "আরবি: أَسْتَغْفِرُ اللَّهَ ، اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ\n\nউচ্চারণ: আস্তাগফিরুল্লাহ। আল্লাহুম্মা আনতাস সালামু ওয়া মিনকাস সালাম, তাবারাকতা ইয়া যাল জালালি ওয়াল ইকরাম।"
                     : "Arabic: أَسْتَغْفِرُ اللَّهَ ، اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ\n\nTransliteration: Astaghfirullah. Allahumma Antas-Salam wa minkas-Salam, tabarakta ya Dhal-Jalali wal-Ikram.",
                isBn ? "নামাজ সমাপ্ত হলো।"
                     : "Prayer finished."
        ));

        return list;
    }

    private static List<VisualStepItem> getFemaleSteps(boolean isBn) {
        List<VisualStepItem> list = new ArrayList<>();

        // মহিলা ধাপ ১: কিয়াম ও নিয়ত
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_1,
                isBn ? "নামাজ পড়ার আগে মন দিয়ে আল্লাহর কাছে নামাজের উদ্দেশ্য ঠিক করতে হবে। মনোযোগ সহকারে নামাজের জন্য নিয়ত করতে হবে।"
                     : "Before starting prayer, sincerely set your intention in your heart for the sake of Allah.",
                null,
                isBn ? "উদাহরণ: 'আমি দুই রাকাত ফরজ নামাজ আদায় করছি আল্লাহর জন্য।' প্রাথমিক নিয়ত করে নামাজ শুরু করতে হয়। (হাদিস: সুনানে আবু দাউদ: ৭৫৮)"
                     : "Example: 'I intend to pray two Rak'ahs of Fard prayer for Allah.' The intention is made in the heart. (Hadith: Sunan Abi Dawud: 758)"
        ));

        // মহিলা ধাপ ২: তাকবীরে তাহরীমা
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_2,
                isBn ? "কিবলামুখী হয়ে তাকবীরে তাহরীমা বলার সময় উভয় হাত কাঁধ পর্যন্ত উঠান এবং বলুন:"
                     : "Facing the Qiblah, raise both hands up to shoulder level and recite:",
                isBn ? "আরবিঃ اَللهُ أَكْبَرُ\nউচ্চারণঃ আল্লাহু আকবর।"
                     : "Arabic: اَللهُ أَكْبَرُ\nTransliteration: Allahu Akbar.",
                isBn ? "আপনার নামাজ এখান থেকে শুরু হলো।"
                     : "Your prayer begins here."
        ));

        // মহিলা ধাপ ৩: হাত বাঁধা ও ছানা পাঠ
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_3,
                isBn ? "বুকের ওপর হাত বাঁধবেন। ডান হাত দিয়ে বাম হাত জড়িয়ে ধরবেন না বরং বাম হাতের পিঠের ওপর ডান হাত রেখে দিন এবং দৃষ্টি সিজদার স্থানে রাখুন।"
                     : "Fold your hands on the chest. Place the right hand over the back of the left hand without gripping it and keep your gaze at the place of prostration.",
                isBn ? "তাকবীরে তাহরীমার পরেই পড়ুন ছানা:\n\nআরবি: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nউচ্চারণ: সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়া লা-ইলাহা গাইরুক।"
                     : "Recite Sana after Takbeer:\n\nArabic: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nTransliteration: Subhanak Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruk.",
                isBn ? "তা’য়াউজঃ আউ’যুবিল্লাহি মিনাশশাইত্বানির রাজীম\n\nতাসমিয়াহ্ঃ বিসমিল্লাহির রাহমানির রাহীম\n\nসূরা ফাতিহা পড়া ফরজ। (কুরআন: ১: ১-৭), প্রত্যেক নামাজে সূরা ফাতিহা পড়ুন এবং অন্য যেকোনো একটি সূরা তেলাওয়াত করুন।"
                     : "Ta'awwudh: A'udhu billahi minash-shaytanir-rajim\n\nTasmiyah: Bismillahir-Rahmanir-Rahim\n\nReciting Surah Al-Fatiha is obligatory (Quran 1:1-7). Recite Surah Al-Fatiha in every Rak'ah followed by any other Surah."
        ));

        // মহিলা ধাপ ৪: রুকু
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_4,
                isBn ? "রুকুতে সামান্য নত হয়ে হাত হাঁটু পর্যন্ত রাখুন এবং আঙ্গুলগুলো মিলিয়ে হাঁটুর ওপর রাখুন।"
                     : "Bow gently into Ruku with hands placed lightly on knees with fingers kept together.",
                isBn ? "রুকুর দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الْعَظِيمِ\nউচ্চারণ: সুবহানা রব্বিয়াল আযীম। (৩ বার)\nঅর্থ: আমি আমার মহান প্রভুর পবিত্রতা বর্ণনা করছি।"
                     : "Ruku Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الْعَظِيمِ\nTransliteration: Subhana Rabbiyal Azeem (3 times)\nMeaning: Glory be to my Lord, the Almighty.",
                isBn ? "রুকুতে এই দোয়া ৩, ৫ বা ৭ বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)"
                     : "Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)"
        ));

        // মহিলা ধাপ ৫: কওমা
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_5,
                isBn ? "রুকু থেকে উঠে সোজা হয়ে দাঁড়াতে দাঁড়াতে পড়ুন:"
                     : "Rise straight from Ruku into standing position (Qawmah) and recite:",
                isBn ? "আরবিঃ سَمِعَ اللهُ لِمَنْ حَمِدَهُ رَبَّنَا وَلَكَ الْحَمْدُ\nউচ্চারণ: সামিয়াল্লাহু লিমান হামিদাহ, রব্বানা লাকাল হামদ।\nঅর্থ: যে ব্যক্তি আল্লাহর প্রশংসা করে, আল্লাহ তা শুনে থাকেন।"
                     : "Arabic: سَمِعَ اللهُ لِمَنْ حَمِدَهُ رَبَّنَا وَلَكَ الْحَمْدُ\nTransliteration: Sami' Allahu liman hamidah, Rabbana wa lakal-hamd.\nMeaning: Allah hears whoever praises Him. Our Lord, to You belongs all praise.",
                isBn ? "দাঁড়িয়ে দোয়া পড়ুন:\nউচ্চারণ: রাব্বানা লাকাল হামদু হামদান কাসীরান তাইয়িবান মুবারাকান ফীহ্\n\nঅর্থ: হে আমাদের প্রতিপালক! তোমারই জন্যে বহু পবিত্র প্রশংসা রয়েছে, যার মধ্যে বরকতও নিহিত আছে।"
                     : "Standing upright, recite:\nTransliteration: Rabbana lakal-hamd, hamdan katheeran tayyiban mubarakan feeh\n\nMeaning: Our Lord, to You belongs all praise, abundant, pure and blessed praise."
        ));

        // মহিলা ধাপ ৬: সিজদা
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_6,
                isBn ? "জড়োসড়ো হয়ে সিজদা করুন। সিজদাতে পেট উরুর সঙ্গে লাগিয়ে রাখুন এবং দুই পা ডান দিকে রাখুন। বলুন 'আল্লাহু আকবার' এবং সিজদায় যান।"
                     : "Prostrate closely in Sajdah with arms close to the body and both feet placed to the right side. Say 'Allahu Akbar' and prostrate.",
                isBn ? "সিজদার দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الأَعْلَى\nউচ্চারণ: সুবহানা রাব্বিয়াল আ'লা (৩ বার)\nঅর্থ: আমি আমার সর্বোচ্চ প্রভুর পবিত্রতা বর্ণনা করছি।"
                     : "Sajdah Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الأَعْلَى\nTransliteration: Subhana Rabbiyal A'la (3 times)\nMeaning: Glory be to my Lord, the Most High.",
                isBn ? "সিজদায় এই দোয়া ৩, ৫ বা ৭ বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)"
                     : "Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)"
        ));

        // মহিলা ধাপ ৭: দুই সিজদার মধ্যবর্তী বৈঠক
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_7,
                isBn ? "সিজদা থেকে উঠুন এবং কিছুক্ষণের জন্য শান্তভাবে বসুন ও এই দোয়াটি পড়ুন:"
                     : "Rise from Sajdah and sit calmly between the two prostrations, then recite:",
                isBn ? "দুই সিজদার মধ্যবর্তী দোয়াঃ\n\nআরবিঃ اَللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي\nউচ্চারণ: আল্লাহুম্মাগফির লী ওয়ারহামনী ওয়াজবুরনী ওয়াহদিনী ওয়া আফিনী ওয়ারযুক্বনী।\nঅর্থ: হে আল্লাহ! আপনি আমাকে ক্ষমা করুন, রহম করুন, আমার অবস্থা সংশোধন করুন, সৎপথ দেখান, সুস্থতা দিন ও জীবিকা দান করুন।"
                     : "Jalsah Dua:\n\nArabic: اَللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي\nTransliteration: Allahummagh-fir lee, warhamnee, wajburnee, wahdinee, wa 'aafinee, warzuqnee.\nMeaning: O Allah! Forgive me, have mercy on me, guide me, grant me well-being and provide for me.",
                isBn ? "এরপর দ্বিতীয় সিজদা আদায় করে পরবর্তী রাকাতের জন্য দাঁড়ান।"
                     : "Perform the second Sajdah and rise for the next Rak'ah."
        ));

        // মহিলা ধাপ ৮: তাশাহহুদ বা বৈঠক
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_8,
                isBn ? "বৈঠকের সময় শান্তভাবে বসে তাশাহহুদ, দরুদ ও দোয়া মাসূরা পাঠ করুন:"
                     : "Sit calmly in Tashahhud and recite At-Tahiyyat, Durood Ibrahim and Dua Masura:",
                isBn ? "তাশাহ্হুদ:\n\nاَلتَّحِيَّاتُ لِلهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nদরুদঃ\n\nاَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ- اَللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّদٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ"
                     : "Tashahhud:\n\nاَلتَّحِيَّاتُ لِلهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nDurood:\n\nاَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ- اَللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّদٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ",
                isBn ? "দো‘আয়ে মাছুরাহঃ\n\nاَللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّনُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ"
                     : "Dua Masura:\n\nاَللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّনُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ"
        ));

        // মহিলা ধাপ ৯: ডান দিকে সালাম ফিরানো
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_9,
                isBn ? "আপনার ডান দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে সালাম দিন:"
                     : "Turn your face to the right shoulder and say:",
                isBn ? "সালাম ফিরাবার দোয়াঃ\n\nআরবিঃ السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্"
                     : "Arabic: السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
                null
        ));

        // মহিলা ধাপ ১০: বাম দিকে সালাম ফিরানো ও সমাপ্তি
        list.add(new VisualStepItem(
                R.drawable.img_salah_female_step_10,
                isBn ? "এবং তারপরে বাম দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে আবার বলুন:"
                     : "Then turn your face to the left shoulder and say:",
                isBn ? "সালাম ফিরাবার দোয়াঃ\n\nআরবিঃ السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্"
                     : "Arabic: السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
                isBn ? "এখানে আপনার নামাজ সম্পন্ন হলো।"
                     : "Your Salah is now completed."
        ));
        return list;
    }

    private static void applySmartImageCrop(ImageView imageView, Activity activity, @DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(activity, resId);
        if (drawable == null) {
            imageView.setImageResource(resId);
            return;
        }
        imageView.setImageDrawable(drawable);

        Runnable updateMatrix = () -> {
            int vWidth = imageView.getWidth();
            int vHeight = imageView.getHeight();
            int dWidth = drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight();

            if (vWidth <= 0 || vHeight <= 0 || dWidth <= 0 || dHeight <= 0) {
                return;
            }

            float scale;
            float dx = 0f;
            float dy;

            if (dWidth * vHeight > vWidth * dHeight) {
                scale = (float) vHeight / (float) dHeight;
                dx = (vWidth - dWidth * scale) * 0.5f;
                dy = 0f;
            } else {
                scale = (float) vWidth / (float) dWidth;
                float diffY = vHeight - dHeight * scale;
                float verticalBias = getVerticalBiasForStep(resId);
                dy = diffY * verticalBias;
            }

            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
            imageView.setImageMatrix(matrix);
        };

        if (imageView.getWidth() > 0 && imageView.getHeight() > 0) {
            updateMatrix.run();
        } else {
            imageView.post(updateMatrix);
        }
    }

    private static float getVerticalBiasForStep(@DrawableRes int resId) {
        if (resId == R.drawable.img_salah_male_step_2) {
            return 0.02f; // Takbir: Head is near top (Y=33), 2% bias provides natural headroom
        } else if (resId == R.drawable.img_salah_male_step_1) {
            return 0.08f; // Qiyam: Head at Y=56, 8% bias
        } else if (resId == R.drawable.img_salah_male_step_5) {
            return 0.12f; // Qawma: Head at Y=64, 12% bias
        } else if (resId == R.drawable.img_salah_male_step_4) {
            return 0.30f; // Ruku: Bowed back at Y=35, 30% bias for natural breathing room
        } else if (resId == R.drawable.img_salah_female_step_2) {
            return 0.08f; // Takbir: Top of hijab at Y=67, 8% bias
        } else if (resId == R.drawable.img_salah_female_step_3) {
            return 0.08f; // Qiyam: Top of hijab at Y=56, 8% bias
        } else if (resId == R.drawable.img_salah_female_step_5) {
            return 0.10f; // Qawma: Top of hijab at Y=60, 10% bias
        } else if (resId == R.drawable.img_salah_male_step_3 || resId == R.drawable.img_salah_female_step_1) {
            return 0.35f; // Standing reference poses: 35% bias ensures full headroom and mat visibility
        } else {
            return 0.50f; // Sitting, Sajdah, Jalsa, Tashahhud, Salam, Dua: balanced center-crop
        }
    }
}
"""

with open(r"F:\Deanone\app\src\main\java\com\devlabs\deanone\features\salahguide\SalahVisualGuideDialog.java", "w", encoding="utf-8") as f:
    f.write(content.strip() + "\n")
print("SUCCESS!")
