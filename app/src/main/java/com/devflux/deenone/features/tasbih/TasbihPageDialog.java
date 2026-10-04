package com.devflux.deenone.features.tasbih;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.devflux.deenone.R;
import com.devflux.deenone.core.gamification.GamificationManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.tasbih.CircularTasbihProgressView;
import com.devflux.deenone.core.tasbih.TasbihFeedbackHelper;
import com.devflux.deenone.core.tasbih.TasbihTapProtectionManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetTasbihBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 100% Dual-Language & Theme Compatible Digital Tasbih Dialog.
 */
public class TasbihPageDialog {

    public static class DhikrPreset {
        public long id;
        public String titleBn;
        public String titleEn;
        public String arabic;
        public String transliterationBn;
        public String transliterationEn;
        public String meaningBn;
        public String meaningEn;
        public String fazilatBn;
        public String fazilatEn;
        public int defaultTarget;

        public DhikrPreset(long id, String titleBn, String titleEn, String arabic,
                           String transliterationBn, String transliterationEn,
                           String meaningBn, String meaningEn,
                           String fazilatBn, String fazilatEn,
                           int defaultTarget) {
            this.id = id;
            this.titleBn = titleBn;
            this.titleEn = titleEn;
            this.arabic = arabic;
            this.transliterationBn = transliterationBn;
            this.transliterationEn = transliterationEn;
            this.meaningBn = meaningBn;
            this.meaningEn = meaningEn;
            this.fazilatBn = fazilatBn;
            this.fazilatEn = fazilatEn;
            this.defaultTarget = defaultTarget;
        }

        public String getTitle(boolean isBn) {
            return isBn ? titleBn : titleEn;
        }

        public String getTransliteration(boolean isBn) {
            return isBn ? transliterationBn : transliterationEn;
        }

        public String getMeaning(boolean isBn) {
            return isBn ? meaningBn : meaningEn;
        }

        public String getFazilat(boolean isBn) {
            return isBn ? fazilatBn : fazilatEn;
        }
    }

    private static final List<DhikrPreset> BUILTIN_PRESETS = new ArrayList<>();
    static {
        BUILTIN_PRESETS.add(new DhikrPreset(
                1,
                "সুবহানাল্লাহ",
                "Subhanallah",
                "سُبْحَانَ اللَّهِ",
                "সুবহানাল্লাহ",
                "SubhanAllah",
                "আল্লাহ অতি পবিত্র ও নিষ্কলুষ",
                "Glory be to Allah, free from all imperfections",
                "মিজানের পাল্লা অর্ধেক পূর্ণ করে ও জান্নাতে বৃক্ষ রোপিত হয়।",
                "Fills half the Scale of deeds and plants a tree in Jannah.",
                33
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                2,
                "আলহামদুলিল্লাহ",
                "Alhamdulillah",
                "الْحَمْدُ لِلَّهِ",
                "আলহামদুলিল্লাহ",
                "Al-Hamdulillah",
                "সমস্ত প্রশংসা একমাত্র আল্লাহর",
                "All praise and gratitude belongs solely to Allah",
                "মিজানের পাল্লা সম্পূর্ণ পূর্ণ করে দেয়।",
                "Fills the Scale of good deeds completely.",
                33
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                3,
                "আল্লাহু আকবার",
                "Allahu Akbar",
                "اللَّهُ أَكْبَرُ",
                "আল্লাহু আকবার",
                "Allahu Akbar",
                "আল্লাহ সর্বশ্রেষ্ঠ ও সবচেয়ে মহান",
                "Allah is the Greatest",
                "আসমান ও জমিনের মধ্যবর্তী স্থান নেকিতে পূর্ণ করে।",
                "Fills what is between the heavens and earth with reward.",
                34
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                4,
                "লা ইলাহা ইল্লাল্লাহ",
                "La Ilaha Illallah",
                "لَا إِلٰهَ إِلَّا اللهُ",
                "লা ইলাহা ইল্লাল্লাহ",
                "La ilaha illallah",
                "আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই",
                "None has the right to be worshipped except Allah",
                "সর্বশ্রেষ্ঠ জিকির ও জান্নাতের চাবিকাঠি।",
                "The most virtuous remembrance and the key to Paradise.",
                100
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                5,
                "আস্তাগফিরুল্লাহ",
                "Astaghfirullah",
                "أَسْتَغْفِرُ اللَّهَ",
                "আস্তাগফিরুল্লাহ",
                "Astaghfirullah",
                "আমি আল্লাহর নিকট ক্ষমা প্রার্থনা করছি",
                "I seek forgiveness from Allah",
                "চিন্তা ও সংকট দূর করে এবং রিজিকে বরকত আনে।",
                "Relieves anxiety, distress, and brings abundant sustenance.",
                100
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                6,
                "সুবহানাল্লাহি ওয়া বিহামদিহি",
                "Subhanallahi Wa Bihamdihi",
                "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
                "সুবহানাল্লাহি ওয়া বিহামদিহি",
                "Subhanallahi wa bihamdihi",
                "আল্লাহর প্রশংসাসহ তাঁর পবিত্রতা ঘোষণা করছি",
                "Glory and praise be to Allah",
                "জিহ্বায় সহজ কিন্তু মিজানের পাল্লায় অতি ভারী।",
                "Light on the tongue, heavy on the Scale of good deeds.",
                100
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                7,
                "লা হাওলা ওয়ালা কুওয়াতা",
                "La Hawla Wa La Quwwata",
                "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
                "লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ",
                "La hawla wa la quwwata illa billah",
                "আল্লাহর সাহায্য ব্যতীত কারও কোনো শক্তি নেই",
                "There is no power and no strength except with Allah",
                "জান্নাতের অমূল্য রত্নভাণ্ডারের একটি রত্ন।",
                "One of the priceless treasures of Paradise.",
                100
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                8,
                "দরুদ শরীফ",
                "Durood Sharif",
                "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ",
                "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদ",
                "Allahumma salli 'ala Muhammad",
                "হে আল্লাহ! মুহাম্মদ (সা.)-এর ওপর রহমত বর্ষণ করুন",
                "O Allah, bestow peace and blessings upon Muhammad",
                "একবার পাঠে ১০টি রহমত, ১০টি গুনাহ মাফ ও ১০ মর্যাদা বৃদ্ধি।",
                "Reciting once grants 10 mercies, erases 10 sins, and raises 10 ranks.",
                100
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                9,
                "হাসবুনাল্লাহু ওয়া নি'মাল ওয়াকিল",
                "Hasbunallahu Wa Ni'mal Wakil",
                "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
                "হাসবুনাল্লাহু ওয়া নিমাল ওয়াকিল",
                "Hasbunallahu wa ni'mal wakeel",
                "আল্লাহই আমাদের জন্য যথেষ্ট এবং তিনিই উত্তম কর্মবিধায়ক",
                "Allah is sufficient for us, and He is the best Disposer of affairs",
                "যেকোনো ভয় ও সংকট থেকে মুক্তির অমোঘ আশ্রয়।",
                "The ultimate refuge from fear, calamity, and sorrow.",
                100
        ));
        BUILTIN_PRESETS.add(new DhikrPreset(
                10,
                "দোয়া ইউনুস",
                "Dua Yunus",
                "لَا إِلٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
                "লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নি কুনতু মিনায যালিমিন",
                "La ilaha illa anta subhanaka inni kuntu minaz-zalimin",
                "আপনি ছাড়া কোনো উপাস্য নেই, আপনি পবিত্র! নিশ্চয়ই আমি অপরাধী",
                "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers",
                "কঠিন বিপদে এই দোয়ায় আল্লাহ দুশ্চিন্তা দূর করে দেন।",
                "Allah removes distress and grants relief when invoked with this Dua.",
                100
        ));
    }

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        BottomSheetTasbihBinding binding = BottomSheetTasbihBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        TasbihViewModel viewModel = null;
        if (activity instanceof ViewModelStoreOwner) {
            viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(TasbihViewModel.class);
        }

        // Apply dynamic translations
        binding.tvTasbihHeaderTitle.setText(isBn ? "তাসবিহ" : "Digital Tasbih");
        binding.btnTasbihUndo.setContentDescription(isBn ? "পূর্বের সংখ্যায় ফিরুন" : "Undo count");
        binding.btnTarget33.setText(isBn ? "৩৩" : "33");
        binding.btnTarget100.setText(isBn ? "১০০" : "100");
        binding.btnTarget1000.setText(isBn ? "১০০০" : "1000");
        binding.btnTargetCustom.setText(isBn ? "কাস্টম" : "Custom");

        // Active State
        final int[] activePresetIndex = {0};
        final int[] currentCount = {0};
        final int[] targetCount = {33};
        final int[] completedCycles = {0};
        final long[] lifetimeTotal = {0};

        final TasbihViewModel finalViewModel = viewModel;

        // Top Actions Touch Feedback
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseTasbih);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTasbihSoundToggle);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTasbihVibrationToggle);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTasbihReset);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTasbihUndo);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTarget33);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTarget100);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTarget1000);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTargetCustom);

        binding.btnCloseTasbih.setOnClickListener(v -> dialog.dismiss());

        // Sound Toggle Button
        updateSoundIcon(activity, binding.btnTasbihSoundToggle, binding.ivTasbihSoundIcon);
        binding.btnTasbihSoundToggle.setOnClickListener(v -> {
            boolean newState = TasbihFeedbackHelper.toggleSound(activity);
            updateSoundIcon(activity, binding.btnTasbihSoundToggle, binding.ivTasbihSoundIcon);
            if (newState) {
                TasbihFeedbackHelper.playTapFeedback(activity, v);
            }
        });

        // Vibration Toggle Button
        updateVibrationIcon(activity, binding.btnTasbihVibrationToggle, binding.ivTasbihVibrationIcon);
        binding.btnTasbihVibrationToggle.setOnClickListener(v -> {
            boolean newState = TasbihFeedbackHelper.toggleVibration(activity);
            updateVibrationIcon(activity, binding.btnTasbihVibrationToggle, binding.ivTasbihVibrationIcon);
            if (newState) {
                TasbihFeedbackHelper.playTapFeedback(activity, v);
            }
        });

        // Reset Button
        binding.btnTasbihReset.setOnClickListener(v -> {
            boolean currentBn = LocaleManager.isBengali(activity);
            new AlertDialog.Builder(activity)
                    .setTitle(currentBn ? "কাউন্টার রিসেট" : "Reset Counter")
                    .setMessage(currentBn ? "আপনি কি বর্তমান জিকিরের গণনা শূন্য করতে চান?" : "Do you want to reset the current count to zero?")
                    .setPositiveButton(currentBn ? "হ্যাঁ, রিসেট" : "Yes, Reset", (d, w) -> {
                        currentCount[0] = 0;
                        updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], true);
                        if (finalViewModel != null) finalViewModel.reset();
                        TasbihFeedbackHelper.playTapFeedback(activity, v);
                    })
                    .setNegativeButton(currentBn ? "বাতিল" : "Cancel", null)
                    .show();
        });

        // Populate Dhikr Category Pills
        Runnable populatePills = () -> {
            boolean currentBn = LocaleManager.isBengali(activity);
            binding.layoutDhikrPillContainer.removeAllViews();
            for (int i = 0; i < BUILTIN_PRESETS.size(); i++) {
                final int index = i;
                DhikrPreset preset = BUILTIN_PRESETS.get(i);
                boolean isSelected = (i == activePresetIndex[0]);

                LinearLayout pill = createDhikrPillView(activity, preset.getTitle(currentBn), isSelected);
                com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(pill);
                pill.setOnClickListener(v -> {
                    activePresetIndex[0] = index;
                    currentCount[0] = 0;
                    targetCount[0] = preset.defaultTarget;

                    // Update Text & UI
                    boolean bn = LocaleManager.isBengali(activity);
                    binding.tvTasbihArabic.setText(preset.arabic);
                    binding.tvTasbihMeaning.setText(preset.getMeaning(bn));
                    binding.tvTasbihFazilat.setText(preset.getTransliteration(bn) + " • " + preset.getFazilat(bn));

                    highlightTargetButton(binding, activity, targetCount[0]);
                    updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], false);

                    // Re-render pills to update active state
                    reRenderPills(binding, activity, activePresetIndex[0]);

                    // Immediate tactile feedback on dhikr switch
                    TasbihFeedbackHelper.playTapFeedback(activity, v);
                });
                binding.layoutDhikrPillContainer.addView(pill);
            }

            // Custom Dhikr Button (+ কাস্টম / + Custom)
            LinearLayout addCustomPill = createCustomAddPillView(activity);
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(addCustomPill);
            addCustomPill.setOnClickListener(v -> showAddCustomDhikrDialog(activity, (customTitle, customArabic, customTarget) -> {
                boolean bn = LocaleManager.isBengali(activity);
                long newId = BUILTIN_PRESETS.size() + 1;
                DhikrPreset customPreset = new DhikrPreset(
                        newId,
                        customTitle,
                        customTitle,
                        customArabic.isEmpty() ? customTitle : customArabic,
                        customTitle,
                        customTitle,
                        bn ? "কাস্টম নির্ধারিত জিকির" : "Custom Dhikr",
                        "Custom Dhikr",
                        bn ? "দৈনিক নেক আমল" : "Daily Good Deed",
                        "Daily Good Deed",
                        customTarget
                );
                BUILTIN_PRESETS.add(customPreset);
                activePresetIndex[0] = BUILTIN_PRESETS.size() - 1;
                currentCount[0] = 0;
                targetCount[0] = customTarget;

                binding.tvTasbihArabic.setText(customPreset.arabic);
                binding.tvTasbihMeaning.setText(customPreset.getMeaning(bn));
                binding.tvTasbihFazilat.setText(customPreset.getFazilat(bn));

                highlightTargetButton(binding, activity, targetCount[0]);
                updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], false);
                reRenderPills(binding, activity, activePresetIndex[0]);
            }));
            binding.layoutDhikrPillContainer.addView(addCustomPill);
        };

        populatePills.run();

        // Initial Dhikr Display
        DhikrPreset firstPreset = BUILTIN_PRESETS.get(0);
        binding.tvTasbihArabic.setText(firstPreset.arabic);
        binding.tvTasbihMeaning.setText(firstPreset.getMeaning(isBn));
        binding.tvTasbihFazilat.setText(firstPreset.getTransliteration(isBn) + " • " + firstPreset.getFazilat(isBn));
        targetCount[0] = firstPreset.defaultTarget;
        highlightTargetButton(binding, activity, targetCount[0]);
        updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], false);

        // Tap Counter Action (Increment with Guaranteed Vibration Feedback on Every Single Tap)
        View.OnClickListener tapCountListener = v -> {
            boolean currentBn = LocaleManager.isBengali(activity);
            TasbihTapProtectionManager.ValidationResult vResult =
                    TasbihTapProtectionManager.validateTap(activity);
            if (vResult.status == TasbihTapProtectionManager.TapStatus.COOLDOWN_ACTIVE) {
                String timeStr = TasbihTapProtectionManager.formatDuration(vResult.remainingCooldownMs);
                String msg = currentBn
                        ? ("দ্রুত ট্যাপের কারণে তাসবিহ সাময়িকভাবে বন্ধ (" + BengaliNumberUtil.toBengali(timeStr) + " বাকি)")
                        : ("Tasbih temporarily paused due to rapid tapping (" + timeStr + " remaining)");
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                return;
            }
            if (vResult.status == TasbihTapProtectionManager.TapStatus.RAPID_TAP_WARNING) {
                String warnMsg = currentBn ? vResult.warningMessage : "Please tap gently and consciously during dhikr.";
                Toast.makeText(activity, warnMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            // Animate spring scale bounce
            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(50)
                    .withEndAction(() -> v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(120).setInterpolator(new OvershootInterpolator()).start())
                    .start();

            currentCount[0]++;
            lifetimeTotal[0]++;

            // Guaranteed Tactile Vibration & Sound Feedback on every tap
            TasbihFeedbackHelper.playTapFeedback(activity, v);

            // Cycle Completion Check
            if (targetCount[0] > 0 && currentCount[0] >= targetCount[0]) {
                completedCycles[0]++;
                currentCount[0] = 0;

                // Milestone celebratory vibration & Gamification reward
                TasbihFeedbackHelper.playCycleMilestoneFeedback(activity);
                GamificationManager.addXP(activity, 20);
                String completionMsg = currentBn
                        ? ("মাশাআল্লাহ! " + BengaliNumberUtil.toBengali(targetCount[0]) + " বার সম্পন্ন হয়েছে (+২০ XP)")
                        : ("MashaAllah! Completed " + targetCount[0] + " times (+20 XP)");
                Toast.makeText(activity, completionMsg, Toast.LENGTH_SHORT).show();

                if (finalViewModel != null) {
                    finalViewModel.increment();
                }
            } else {
                if (finalViewModel != null) {
                    finalViewModel.increment();
                }
            }

            updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], true);
        };

        binding.btnCircularCounterContainer.setOnClickListener(tapCountListener);

        // Undo Action (Decrement with Tactile Feedback)
        binding.btnTasbihUndo.setOnClickListener(v -> {
            if (currentCount[0] > 0) {
                currentCount[0]--;
                if (lifetimeTotal[0] > 0) lifetimeTotal[0]--;
                TasbihFeedbackHelper.playTapFeedback(activity, v);
                updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], true);
                if (finalViewModel != null) finalViewModel.decrement();
            }
        });

        // Target Preset Buttons (33, 100, 1000, Custom)
        binding.btnTarget33.setOnClickListener(v -> {
            targetCount[0] = 33;
            currentCount[0] = 0;
            highlightTargetButton(binding, activity, 33);
            updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], true);
            TasbihFeedbackHelper.playTapFeedback(activity, v);
            if (finalViewModel != null) finalViewModel.setTarget(33);
        });

        binding.btnTarget100.setOnClickListener(v -> {
            targetCount[0] = 100;
            currentCount[0] = 0;
            highlightTargetButton(binding, activity, 100);
            updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], true);
            TasbihFeedbackHelper.playTapFeedback(activity, v);
            if (finalViewModel != null) finalViewModel.setTarget(100);
        });

        binding.btnTarget1000.setOnClickListener(v -> {
            targetCount[0] = 1000;
            currentCount[0] = 0;
            highlightTargetButton(binding, activity, 1000);
            updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], true);
            TasbihFeedbackHelper.playTapFeedback(activity, v);
            if (finalViewModel != null) finalViewModel.setTarget(1000);
        });

        binding.btnTargetCustom.setOnClickListener(v -> {
            boolean currentBn = LocaleManager.isBengali(activity);
            EditText input = new EditText(activity);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint(currentBn ? "টার্গেট সংখ্যা লিখুন (যেমন: ৫০০, ৫০০০)" : "Enter target count (e.g. 500, 5000)");
            input.setPadding(40, 30, 40, 30);

            new AlertDialog.Builder(activity)
                    .setTitle(currentBn ? "কাস্টম টার্গেট নির্ধারণ" : "Set Custom Target")
                    .setView(input)
                    .setPositiveButton(currentBn ? "সেট করুন" : "Set", (d, w) -> {
                        String txt = input.getText().toString().trim();
                        if (!txt.isEmpty()) {
                            try {
                                int val = Integer.parseInt(txt);
                                if (val > 0) {
                                    targetCount[0] = val;
                                    currentCount[0] = 0;
                                    highlightTargetButton(binding, activity, val);
                                    updateCounterUI(binding, currentCount[0], targetCount[0], completedCycles[0], lifetimeTotal[0], true);
                                    TasbihFeedbackHelper.playTapFeedback(activity, null);
                                    if (finalViewModel != null) finalViewModel.setTarget(val);
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    })
                    .setNegativeButton(currentBn ? "বাতিল" : "Cancel", null)
                    .show();
        });

        dialog.show();
    }

    private static void updateCounterUI(BottomSheetTasbihBinding binding, int current, int target, int cycles, long total, boolean animate) {
        Context context = binding.getRoot().getContext();
        boolean isBn = LocaleManager.isBengali(context);
        binding.tvCounterNumber.setText(isBn ? BengaliNumberUtil.toBengali(current) : String.valueOf(current));

        String targetStr = target > 0
                ? ((isBn ? "টার্গেট: " : "Target: ") + (isBn ? BengaliNumberUtil.toBengali(target) : String.valueOf(target)))
                : (isBn ? "টার্গেট: অসীম" : "Target: Infinite");
        binding.tvCounterTargetSubtitle.setText(targetStr);

        binding.tvCounterCycleSubtitle.setText(
                isBn ? ("চক্র: " + BengaliNumberUtil.toBengali(cycles) + " • মোট: " + BengaliNumberUtil.toBengali(total))
                     : ("Cycles: " + cycles + " • Total: " + total)
        );

        binding.circularProgressView.setProgress(current, target, animate);
    }

    private static LinearLayout createDhikrPillView(Context context, String label, boolean isSelected) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(32, 18, 32, 18);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMarginEnd(16);
        layout.setLayoutParams(lp);

        if (isSelected) {
            layout.setBackgroundResource(R.drawable.bg_badge_pill_active);

            ImageView checkIcon = new ImageView(context);
            checkIcon.setImageResource(R.drawable.ic_check);
            checkIcon.setImageTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_mint)));
            LinearLayout.LayoutParams icLp = new LinearLayout.LayoutParams(36, 36);
            icLp.setMarginEnd(8);
            checkIcon.setLayoutParams(icLp);
            layout.addView(checkIcon);

            TextView tv = new TextView(context);
            tv.setText(label);
            tv.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
            tv.setTextSize(13.5f);
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            layout.addView(tv);
        } else {
            layout.setBackgroundResource(R.drawable.bg_badge_pill);

            TextView tv = new TextView(context);
            tv.setText(label);
            tv.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            tv.setTextSize(13.5f);
            layout.addView(tv);
        }

        return layout;
    }

    private static LinearLayout createCustomAddPillView(Context context) {
        boolean isBn = LocaleManager.isBengali(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(30, 18, 30, 18);
        layout.setBackgroundResource(R.drawable.bg_badge_pill);

        TextView tv = new TextView(context);
        tv.setText(isBn ? "+ কাস্টম জিকির" : "+ Custom Dhikr");
        tv.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
        tv.setTextSize(13f);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        layout.addView(tv);

        return layout;
    }

    private static void reRenderPills(BottomSheetTasbihBinding binding, Context context, int activeIndex) {
        boolean isBn = LocaleManager.isBengali(context);
        int count = binding.layoutDhikrPillContainer.getChildCount();
        for (int i = 0; i < count - 1; i++) { // exclude last custom button
            View child = binding.layoutDhikrPillContainer.getChildAt(i);
            boolean isSelected = (i == activeIndex);
            if (child instanceof LinearLayout) {
                LinearLayout pillLayout = (LinearLayout) child;
                pillLayout.removeAllViews();
                String label = BUILTIN_PRESETS.get(i).getTitle(isBn);

                if (isSelected) {
                    pillLayout.setBackgroundResource(R.drawable.bg_badge_pill_active);

                    ImageView checkIcon = new ImageView(context);
                    checkIcon.setImageResource(R.drawable.ic_check);
                    checkIcon.setImageTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_mint)));
                    LinearLayout.LayoutParams icLp = new LinearLayout.LayoutParams(36, 36);
                    icLp.setMarginEnd(8);
                    checkIcon.setLayoutParams(icLp);
                    pillLayout.addView(checkIcon);

                    TextView tv = new TextView(context);
                    tv.setText(label);
                    tv.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
                    tv.setTextSize(13.5f);
                    tv.setTypeface(null, android.graphics.Typeface.BOLD);
                    pillLayout.addView(tv);
                } else {
                    pillLayout.setBackgroundResource(R.drawable.bg_badge_pill);

                    TextView tv = new TextView(context);
                    tv.setText(label);
                    tv.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
                    tv.setTextSize(13.5f);
                    pillLayout.addView(tv);
                }
            }
        }
    }

    private static void highlightTargetButton(BottomSheetTasbihBinding binding, Context context, int target) {
        int activeBg = R.drawable.bg_badge_pill_active;
        int inactiveBg = R.drawable.bg_badge_pill;
        int activeColor = ContextCompat.getColor(context, R.color.accent_mint);
        int inactiveColor = ContextCompat.getColor(context, R.color.text_primary);
        boolean isBn = LocaleManager.isBengali(context);

        binding.btnTarget33.setBackgroundResource(target == 33 ? activeBg : inactiveBg);
        binding.btnTarget33.setTextColor(target == 33 ? activeColor : inactiveColor);

        binding.btnTarget100.setBackgroundResource(target == 100 ? activeBg : inactiveBg);
        binding.btnTarget100.setTextColor(target == 100 ? activeColor : inactiveColor);

        binding.btnTarget1000.setBackgroundResource(target == 1000 ? activeBg : inactiveBg);
        binding.btnTarget1000.setTextColor(target == 1000 ? activeColor : inactiveColor);

        boolean isCustom = (target != 33 && target != 100 && target != 1000);
        binding.btnTargetCustom.setBackgroundResource(isCustom ? activeBg : inactiveBg);
        binding.btnTargetCustom.setTextColor(isCustom ? activeColor : inactiveColor);
        if (isCustom) {
            binding.btnTargetCustom.setText(isBn ? BengaliNumberUtil.toBengali(target) : String.valueOf(target));
        } else {
            binding.btnTargetCustom.setText(isBn ? "কাস্টম" : "Custom");
        }
    }

    private static void updateSoundIcon(Context context, FrameLayout btn, ImageView icon) {
        boolean soundOn = TasbihFeedbackHelper.isSoundEnabled(context);
        btn.setBackgroundResource(R.drawable.bg_circular_counter);
        btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(context, soundOn ? R.color.bg_badge_pill : R.color.bg_card_secondary)
        ));
        icon.setImageResource(soundOn ? R.drawable.ic_volume_up : R.drawable.ic_volume_off);
        icon.setImageTintList(android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(context, soundOn ? R.color.accent_mint : R.color.text_secondary)
        ));
    }

    private static void updateVibrationIcon(Context context, FrameLayout btn, ImageView icon) {
        boolean vibOn = TasbihFeedbackHelper.isVibrationEnabled(context);
        btn.setBackgroundResource(R.drawable.bg_circular_counter);
        btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(context, vibOn ? R.color.bg_badge_pill : R.color.bg_card_secondary)
        ));
        icon.setImageTintList(android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(context, vibOn ? R.color.accent_mint : R.color.text_secondary)
        ));
    }

    public interface OnCustomDhikrAdded {
        void onAdded(String title, String arabic, int target);
    }

    private static void showAddCustomDhikrDialog(Context context, OnCustomDhikrAdded callback) {
        boolean isBn = LocaleManager.isBengali(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 16);

        EditText etTitle = new EditText(context);
        etTitle.setHint(isBn ? "জিকিরের নাম (যেমন: সুবহানাল্লাহিল আযীম)" : "Dhikr name (e.g. Subhanallahil Azeem)");
        layout.addView(etTitle);

        EditText etArabic = new EditText(context);
        etArabic.setHint(isBn ? "আরবি পাঠ / উচ্চারণ (ঐচ্ছিক)" : "Arabic text (optional)");
        layout.addView(etArabic);

        EditText etTarget = new EditText(context);
        etTarget.setHint(isBn ? "টার্গেট সংখ্যা (ডিফল্ট: ১০০)" : "Target count (default: 100)");
        etTarget.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(etTarget);

        new AlertDialog.Builder(context)
                .setTitle(isBn ? "নতুন কাস্টম জিকির যুক্ত করুন" : "Add Custom Dhikr")
                .setView(layout)
                .setPositiveButton(isBn ? "যুক্ত করুন" : "Add", (d, w) -> {
                    String title = etTitle.getText().toString().trim();
                    String arabic = etArabic.getText().toString().trim();
                    String targetStr = etTarget.getText().toString().trim();
                    if (title.isEmpty()) {
                        return;
                    }
                    int target = 100;
                    if (!targetStr.isEmpty()) {
                        try {
                            target = Integer.parseInt(targetStr);
                        } catch (NumberFormatException ignored) {}
                    }
                    callback.onAdded(title, arabic, target);
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }
}
