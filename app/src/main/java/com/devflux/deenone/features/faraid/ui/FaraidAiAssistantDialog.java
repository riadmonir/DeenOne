package com.devflux.deenone.features.faraid.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devflux.deenone.R;
import com.devflux.deenone.databinding.DialogFaraidAiAssistantBinding;
import com.devflux.deenone.features.faraid.engine.FaraidAiExplanationEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidDisclaimerHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * AI Shariah Assistant & Faraid Interactive Explanation Dialog.
 * Explains already-calculated deterministic results and answers classical inheritance FAQs.
 */
public class FaraidAiAssistantDialog extends BottomSheetDialogFragment {

    private DialogFaraidAiAssistantBinding binding;
    private FaraidCalculationResult calculationResult;

    public static FaraidAiAssistantDialog newInstance(FaraidCalculationResult result) {
        FaraidAiAssistantDialog dialog = new FaraidAiAssistantDialog();
        dialog.calculationResult = result;
        return dialog;
    }

    public static void show(@NonNull Context context, FaraidCalculationResult result) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_faraid_ai_assistant, null);
        builder.setView(view);

        TextView tvTopTitle = view.findViewById(R.id.tvAiTopTitle);
        TextView tvHeroTitle = view.findViewById(R.id.tvAiHeroCardTitle);
        TextView tvContextSummary = view.findViewById(R.id.tvAiContextSummary);
        TextView tvAnalysisHeader = view.findViewById(R.id.tvAiAnalysisHeader);
        TextView tvAnalysis = view.findViewById(R.id.tvAiAnalysisContent);
        TextView tvFaqHeader = view.findViewById(R.id.tvAiFaqHeader);

        TextView chipAdopted = view.findViewById(R.id.chipFaqAdoptedChild);
        TextView chipNonMuslim = view.findViewById(R.id.chipFaqNonMuslim);
        TextView chipUnborn = view.findViewById(R.id.chipFaqUnbornChild);
        TextView chipDower = view.findViewById(R.id.chipFaqDowerDebt);

        if (tvTopTitle != null) tvTopTitle.setText(isBn ? "এআই শরীয়াহ ফতোয়া ও গাইড" : "AI Shariah Analysis & Guide");
        if (tvHeroTitle != null) tvHeroTitle.setText(isBn ? "কুরআন, সহীহ হাদিস ও চার মাযহাব ভিত্তিক বিশ্লেষণ" : "Analysis based on Quran, Sunnah & 4 Madhabs");
        if (tvContextSummary != null) tvContextSummary.setText(isBn
                ? "আপনার বর্তমান উত্তরাধিকার বিন্যাস এবং চার মাযহাবের ফতোয়া ও ইসলামিক ফাউন্ডেশনের নির্দেশনা অনুসারে বিশ্লেষণ নিচে প্রদর্শিত হচ্ছে।"
                : "Deterministic inheritance analysis based on the four Sunni Madhabs and authentic Islamic sources.");
        if (tvAnalysisHeader != null) tvAnalysisHeader.setText(isBn ? "শরীয়াহ ফতোয়া ও দলিলের ব্যাখ্যা" : "Shariah Fatwa & Proof Breakdown");
        if (tvFaqHeader != null) tvFaqHeader.setText(isBn ? "ইসলামিক ফারায়েজ সংক্রান্ত সাধারণ প্রশ্নোত্তর:" : "Common Inheritance FAQs:");

        if (chipAdopted != null) chipAdopted.setText(isBn ? "পালক সন্তানের মিরাস" : "Adopted Child's Share");
        if (chipNonMuslim != null) chipNonMuslim.setText(isBn ? "অমুসলিম আত্মীয়ের অংশ" : "Non-Muslim Relative");
        if (chipUnborn != null) chipUnborn.setText(isBn ? "গর্ভস্থ সন্তানের অংশ" : "Unborn Child");
        if (chipDower != null) chipDower.setText(isBn ? "দেনমোহর ও ঋণ পরিশোধ" : "Mahr & Debt Settlement");

        if (result != null && tvAnalysis != null) {
            tvAnalysis.setText(FaraidAiExplanationEngine.explainCalculation(result, isBn));
        }

        if (chipAdopted != null) {
            chipAdopted.setOnClickListener(v -> {
                if (tvAnalysis != null) {
                    tvAnalysis.setText(isBn
                            ? "পবিত্র কুরআনের সূরা আল-আহযাবের ৪ ও ৫ নম্বর আয়াত এবং বাংলাদেশ আইন অনুযায়ী: পালক সন্তান ঔরসজাত সন্তানের সমতুল্য নয় এবং সে মিরাসের কোনো স্বয়ংক্রিয় উত্তরাধিকারী হয় না।\n\n" +
                              "শরীয়াহ ও আইনি সমাধান:\n" +
                              "মৃত ব্যক্তি জীবদ্দশায় রেজিস্ট্রিকৃত অসিয়ত (সর্বোচ্চ মোট সম্পদের ১/৩ অংশ) অথবা হেবা (উপহার/দান দলিল রেজিস্ট্রেশন আইন ১৯০৮)-এর মাধ্যমে পালক সন্তানকে সম্পত্তি প্রদান করতে পারেন।"
                            : "According to Surah Al-Ahzab (verses 4-5) and Bangladesh law: An adopted child is not a biological heir and does not inherit automatically under Faraid.\n\n" +
                              "Shariah & Legal Remedy:\n" +
                              "The deceased can allocate property during their lifetime via registered Wasiyyah (up to 1/3) or Hiba (Gift deed).");
                }
            });
        }

        if (chipNonMuslim != null) {
            chipNonMuslim.setOnClickListener(v -> {
                if (tvAnalysis != null) {
                    tvAnalysis.setText(isBn
                            ? "সহীহ বুখারী (৬৭৬৪) ও সহীহ মুসলিমের হাদিস অনুযায়ী রাসূলুল্লাহ ﷺ বলেছেন:\n" +
                              "لا يَرِثُ المُسْلِمُ الكافِرَ ولا الكافِرُ المُسْلِمَ\n" +
                              "(মুসলিম ব্যক্তি কাফিরের ওয়ারিশ হয় না এবং কাফির ব্যক্তি মুসলিমের ওয়ারিশ হয় না)।\n\n" +
                              "চার মাযহাব ও বাংলাদেশ আইন:\n" +
                              "ধর্ম ভিন্ন হলে স্বয়ংক্রিয় মিরাস বণ্টিত হবে না; তবে মুসলিম ব্যক্তি চাইলে অমুসলিম আত্মীয়ের জন্য ১/৩ অংশ পর্যন্ত বৈধ অসিয়ত রেজিস্ট্রি করে যেতে পারেন।"
                            : "According to Sahih al-Bukhari (6764) & Muslim, the Prophet ﷺ said:\n" +
                              "لا يَرِثُ المُسْلِمُ الكافِرَ ولا الكافِرُ المُسْلِمَ\n" +
                              "(A Muslim cannot inherit from a non-Muslim, nor can a non-Muslim inherit from a Muslim).\n\n" +
                              "Four Madhabs & Law:\n" +
                              "Different religions prevent automatic Faraid inheritance; however, a registered Wasiyyah up to 1/3 is permissible.");
                }
            });
        }

        if (chipUnborn != null) {
            chipUnborn.setOnClickListener(v -> {
                if (tvAnalysis != null) {
                    tvAnalysis.setText(isBn
                            ? "চার মাযহাব (হানাফি, শাফেয়ী, মালিকি, হাম্বলী) ও বাংলাদেশ পারিবারিক আদালত আইন অনুসারে:\n" +
                              "গর্ভস্থ সন্তানের নিশ্চিত অংশের জন্য সম্ভাব্য সর্বোচ্চ হিস্যা (ছেলে বা কন্যা হিসেবে বেশিটি) বণ্টন স্থগিত রেখে সংরক্ষণ করতে হবে, অথবা সন্তান ভূমিষ্ঠ হয়ে জীবিত কান্না করার পর পূর্ণ মিরাস বণ্টিত হবে (সুনান আবু দাউদ: ২৯২০)।"
                            : "According to the Four Sunni Madhabs and Bangladesh Family Courts:\n" +
                              "The potential maximum share (as male or female) must be reserved until birth, or full distribution takes place after the live birth is confirmed (Sunan Abi Dawud: 2920).");
                }
            });
        }

        if (chipDower != null) {
            chipDower.setOnClickListener(v -> {
                if (tvAnalysis != null) {
                    tvAnalysis.setText(isBn
                            ? "সূরা আন-নিসার ১১ ও ১২ নম্বর আয়াত অনুযায়ী:\n" +
                              "مِنْ بَعْدِ وَصِيَّةٍ يُوصِي بِهَا أَوْ دَيْنٍ\n" +
                              "উত্তরাধিকার বণ্টনের পূর্বেই স্ত্রীর অপরিশোধিত দেনমোহর এবং অন্যান্য সকল ঋণ মৃত ব্যক্তির সম্পত্তি থেকে অগ্রাধিকার ভিত্তিতে পরিশোধ করতে হবে।"
                            : "According to Surah An-Nisa (verses 11-12):\n" +
                              "مِنْ بَعْدِ وَصِيَّةٍ يُوصِي بِهَا أَوْ دَيْنٍ\n" +
                              "Unpaid dower (Mahr) and all outstanding liabilities must be settled prior to any estate distribution.");
                }
            });
        }

        builder.setPositiveButton(isBn ? "ঠিক আছে" : "OK", null);
        builder.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFaraidAiAssistantBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(requireContext());

        binding.btnAiBack.setOnClickListener(v -> dismiss());

        if (binding.tvAiTopTitle != null) binding.tvAiTopTitle.setText(isBn ? "এআই শরীয়াহ ফতোয়া ও গাইড" : "AI Shariah Analysis & Guide");
        if (binding.tvAiHeroCardTitle != null) binding.tvAiHeroCardTitle.setText(isBn ? "কুরআন, সহীহ হাদিস ও চার মাযহাব ভিত্তিক বিশ্লেষণ" : "Analysis based on Quran, Sunnah & 4 Madhabs");
        if (binding.tvAiContextSummary != null) binding.tvAiContextSummary.setText(isBn
                ? "আপনার বর্তমান উত্তরাধিকার বিন্যাস এবং চার মাযহাবের ফতোয়া ও ইসলামিক ফাউন্ডেশনের নির্দেশনা অনুসারে বিশ্লেষণ নিচে প্রদর্শিত হচ্ছে।"
                : "Deterministic inheritance analysis based on the four Sunni Madhabs and authentic Islamic sources.");
        if (binding.tvAiAnalysisHeader != null) binding.tvAiAnalysisHeader.setText(isBn ? "শরীয়াহ ফতোয়া ও দলিলের ব্যাখ্যা" : "Shariah Fatwa & Proof Breakdown");
        if (binding.tvAiFaqHeader != null) binding.tvAiFaqHeader.setText(isBn ? "ইসলামিক ফারায়েজ সংক্রান্ত সাধারণ প্রশ্নোত্তর:" : "Common Inheritance FAQs:");

        if (binding.chipFaqAdoptedChild != null) binding.chipFaqAdoptedChild.setText(isBn ? "পালক সন্তানের মিরাস" : "Adopted Child's Share");
        if (binding.chipFaqNonMuslim != null) binding.chipFaqNonMuslim.setText(isBn ? "অমুসলিম আত্মীয়ের অংশ" : "Non-Muslim Relative");
        if (binding.chipFaqUnbornChild != null) binding.chipFaqUnbornChild.setText(isBn ? "গর্ভস্থ সন্তানের অংশ" : "Unborn Child");
        if (binding.chipFaqDowerDebt != null) binding.chipFaqDowerDebt.setText(isBn ? "দেনমোহর ও ঋণ পরিশোধ" : "Mahr & Debt Settlement");

        if (binding.etAiCustomQuestion != null) {
            binding.etAiCustomQuestion.setHint(isBn ? "ফারায়েজ সংক্রান্ত কোনো প্রশ্ন লিখুন..." : "Ask a Faraid question...");
        }

        if (calculationResult != null) {
            binding.tvAiAnalysisContent.setText(FaraidAiExplanationEngine.explainCalculation(calculationResult, isBn));
        } else {
            binding.tvAiAnalysisContent.setText(isBn
                    ? "ক্যালকুলেটরে তথ্য দিয়ে হিসাব সম্পন্ন করুন অথবা নিচের প্রশ্নগুলোতে ট্যাপ করুন।"
                    : "Enter information in the calculator to calculate, or tap any FAQ topic below.");
        }

        binding.chipFaqAdoptedChild.setOnClickListener(v -> {
            binding.tvAiAnalysisContent.setText(isBn
                    ? "পবিত্র কুরআনের সূরা আল-আহযাবের ৪ ও ৫ নম্বর আয়াত এবং বাংলাদেশ আইন অনুযায়ী: পালক সন্তান ঔরসজাত সন্তানের সমতুল্য নয় এবং সে মিরাসের কোনো স্বয়ংক্রিয় উত্তরাধিকারী হয় না।\n\n" +
                      "শরীয়াহ ও আইনি সমাধান:\n" +
                      "মৃত ব্যক্তি জীবদ্দশায় রেজিস্ট্রিকৃত অসিয়ত (সর্বোচ্চ মোট সম্পদের ১/৩ অংশ) অথবা হেবা (উপহার/দান দলিল রেজিস্ট্রেশন আইন ১৯০৮)-এর মাধ্যমে পালক সন্তানকে সম্পত্তি প্রদান করতে পারেন।"
                    : "According to Surah Al-Ahzab (verses 4-5) and Bangladesh law: An adopted child is not a biological heir and does not inherit automatically under Faraid.\n\n" +
                      "Shariah & Legal Remedy:\n" +
                      "The deceased can allocate property during their lifetime via registered Wasiyyah (up to 1/3) or Hiba (Gift deed).");
        });

        binding.chipFaqNonMuslim.setOnClickListener(v -> {
            binding.tvAiAnalysisContent.setText(isBn
                    ? "সহীহ বুখারী (৬৭৬৪) ও সহীহ মুসলিমের হাদিস অনুযায়ী রাসূলুল্লাহ ﷺ বলেছেন:\n" +
                      "لا يَرِثُ المُسْلِمُ الكافِرَ ولا الكافِرُ المُسْلِمَ\n" +
                      "(মুসলিম ব্যক্তি কাফিরের ওয়ারিশ হয় না এবং কাফির ব্যক্তি মুসলিমের ওয়ারিশ হয় না)।\n\n" +
                      "চার মাযহাব ও বাংলাদেশ আইন:\n" +
                      "ধর্ম ভিন্ন হলে স্বয়ংক্রিয় মিরাস বণ্টিত হবে না; তবে মুসলিম ব্যক্তি চাইলে অমুসলিম আত্মীয়ের জন্য ১/৩ অংশ পর্যন্ত বৈধ অসিয়ত রেজিস্ট্রি করে যেতে পারেন।"
                    : "According to Sahih al-Bukhari (6764) & Muslim, the Prophet ﷺ said:\n" +
                      "لا يَرِثُ المُسْلِمُ الكافِرَ ولا الكافِرُ المُسْلِمَ\n" +
                      "(A Muslim cannot inherit from a non-Muslim, nor can a non-Muslim inherit from a Muslim).\n\n" +
                      "Four Madhabs & Law:\n" +
                      "Different religions prevent automatic Faraid inheritance; however, a registered Wasiyyah up to 1/3 is permissible.");
        });

        binding.chipFaqUnbornChild.setOnClickListener(v -> {
            binding.tvAiAnalysisContent.setText(isBn
                    ? "চার মাযহাব (হানাফি, শাফেয়ী, মালিকি, হাম্বলী) ও বাংলাদেশ পারিবারিক আদালত আইন অনুসারে:\n" +
                      "গর্ভস্থ সন্তানের নিশ্চিত অংশের জন্য সম্ভাব্য সর্বোচ্চ হিস্যা (ছেলে বা কন্যা হিসেবে বেশিটি) বণ্টন স্থগিত রেখে সংরক্ষণ করতে হবে, অথবা সন্তান ভূমিষ্ঠ হয়ে জীবিত কান্না করার পর পূর্ণ মিরাস বণ্টিত হবে (সুনান আবু দাউদ: ২৯২০)।"
                    : "According to the Four Sunni Madhabs and Bangladesh Family Courts:\n" +
                      "The potential maximum share (as male or female) must be reserved until birth, or full distribution takes place after the live birth is confirmed (Sunan Abi Dawud: 2920).");
        });

        binding.chipFaqDowerDebt.setOnClickListener(v -> {
            binding.tvAiAnalysisContent.setText(isBn
                    ? "সূরা আন-নিসার ১১ ও ১২ নম্বর আয়াত অনুযায়ী:\n" +
                      "مِنْ بَعْدِ وَصِيَّةٍ يُوصِي بِهَا أَوْ دَيْنٍ\n" +
                      "উত্তরাধিকার বণ্টনের পূর্বেই স্ত্রীর অপরিশোধিত দেনমোহর এবং অন্যান্য সকল ঋণ মৃত ব্যক্তির সম্পত্তি থেকে অগ্রাধিকার ভিত্তিতে পরিশোধ করতে হবে।"
                    : "According to Surah An-Nisa (verses 11-12):\n" +
                      "مِنْ بَعْدِ وَصِيَّةٍ يُوصِي بِهَا أَوْ دَيْنٍ\n" +
                      "Unpaid dower (Mahr) and all outstanding liabilities must be settled prior to any estate distribution.");
        });

        binding.btnAiSendQuestion.setOnClickListener(v -> {
            String question = binding.etAiCustomQuestion.getText().toString().trim();
            if (question.isEmpty()) return;
            binding.etAiCustomQuestion.setText("");
            binding.tvAiAnalysisContent.setText(isBn
                    ? "আপনার প্রশ্নের শরয়ী সমাধান:\n\nফারায়েজ শরীয়াহর সুনির্দিষ্ট নীতিমালার ওপর প্রতিষ্ঠিত। যেকোনো অস্পষ্ট বা জটিল পারিবারিক বিষয়ের জন্য সংশ্লিষ্ট সনদপ্রাপ্ত মুফতি বা বিজ্ঞ আলেমের সাথে পরামর্শ করুন।\n\n" + FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN
                    : "Shariah Analysis for your inquiry:\n\nFaraid rules are strictly deterministic based on Qur'an and Sunnah. For complex or disputed family circumstances, please consult a certified Mufti or Islamic scholar.\n\n" + FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}