package com.devflux.deenone.features.blood;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import android.graphics.Color;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.theme.ThemeManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.DialogBloodFraudPreventionBinding;
import com.devflux.deenone.databinding.ItemBloodFaqCardBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class BloodFraudPreventionDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogBloodFraudPreventionBinding binding = DialogBloodFraudPreventionBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Theme Icon
        boolean isDark = ThemeManager.getSavedThemeMode(activity) == ThemeManager.THEME_DARK;
        binding.ivFraudThemeIcon.setImageResource(isDark ? R.drawable.ic_sun : R.drawable.ic_moon);

        // Headers & Badges (Matches Screenshots Verbatim)
        binding.tvFraudBadgeText.setText(isBn ? "নিরাপত্তা ও সতর্কতা" : "Safety & Caution");
        binding.tvFraudMainTitle.setText(isBn ? "প্রতারণা রোধ ও নিরাপত্তা নির্দেশিকা" : "Fraud Prevention & Safety Guidelines");

        binding.tvSection1Title.setText(isBn ? "প্রতারণা রোধ (Fraud Prevention FAQ)" : "Fraud Prevention FAQ");
        binding.tvSection2Title.setText(isBn ? "প্রতারণা হলে করণীয় (App Misuse)" : "App Misuse & Action Guide");

        binding.tvBannerSafeTitle.setText(isBn ? "সচেতন থাকুন, নিরাপদ থাকুন" : "Stay Alert, Stay Safe");
        binding.tvBannerSafeSubtitle.setText(isBn
                ? "আপনার একটু সচেতনতা অন্যের জীবন এবং আপনার নিরাপত্তা নিশ্চিত করতে পারে।"
                : "A little vigilance can save someone's life and ensure your safety.");

        // Attach Spring Touch Animation ONLY to buttons (Strict Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseFraudPrevention);
        TouchAnimationUtil.attachTouchSpring(binding.btnFraudThemeToggle);
        TouchAnimationUtil.attachTouchSpring(binding.btnFraudNotification);

        binding.btnCloseFraudPrevention.setOnClickListener(v -> dialog.dismiss());
        binding.btnFraudThemeToggle.setOnClickListener(v -> {
            if (activity instanceof MainActivity mainActivity) {
                mainActivity.toggleAppTheme();
                dialog.dismiss();
            }
        });
        binding.btnFraudNotification.setOnClickListener(v -> {
            if (activity instanceof MainActivity mainActivity) {
                mainActivity.showNotificationHistorySheet();
            }
        });

        // 1. Populate Section 1: প্রতারণা রোধ (Fraud Prevention FAQ)
        String[][] sec1Items = isBn ? new String[][]{
                {"কিভাবে বুঝবো অনুরোধটি আসল?", "১. রোগীর ভর্তি তথ্য ও প্রেসক্রিপশন সরাসরি হাসপাতালে বা ব্লাড ব্যাংকে ফোন করে নিশ্চিত করুন।\n২. রোগীর সাথে থাকা স্বজনের সাথে কথা বলুন এবং ক্রস-ম্যাচিং স্লিপ দেখুন।\n৩. রক্তদান সবসময় সরাসরি হাসপাতাল বা ব্লাড ব্যাংকে গিয়ে সম্পন্ন করুন।"},
                {"কেউ আগে টাকা চাইলে কী করবো?", "১. কখনোই রক্তের জন্য কোনো ব্যক্তিকে অগ্রিম টাকা বা যাতায়াত ভাড়া বাবদ বিকাশ/নগদ/রকেটে টাকা পাঠাবেন না।\n২. প্রকৃত ও নিঃস্বার্থ রক্তদাতারা কখনোই অগ্রিম অর্থ দাবি করেন না।\n৩. কেউ আগে টাকা দাবি করলে তাৎক্ষণিকভাবে তাকে প্রত্যাখ্যান করুন।"},
                {"ফেক কল/মেসেজ কীভাবে চিনবো?", "১. কলকারী যদি রোগীকে অতি সংকটাপন্ন দেখিয়ে অযৌক্তিক তাড়াহুড়ো করে দ্রুত টাকা পাঠাতে চাপ দেয়।\n২. কোনো সন্দেহজনক লিংক বা ওটিপি (OTP) পাঠাতে বললে।\n৩. হাসপাতালের সঠিক তথ্য ও চিকিৎসকের প্রেসক্রিপশন দিতে অপারগতা প্রকাশ করলে।"},
                {"সোশ্যাল মিডিয়ার পোস্ট কি সব সত্যি?", "১. সোশ্যাল মিডিয়ার বহু পোস্ট অনেক পুরনো হয়ে থাকে যা বারবার কপি-পেস্ট করা হয়।\n২. অনেক ক্ষেত্রে প্রতারক চক্র পুরনো পোস্টে নিজের বিকাশ/নগদ নম্বর বসিয়ে দেয়।\n৩. তাই পোস্টে উল্লিখিত নম্বরে কথা বলে হাসপাতালের তথ্য ও রোগীর বর্তমান অবস্থা যাচাই করুন।"}
        } : new String[][]{
                {"How do I know if the request is genuine?", "1. Verify patient admission details and doctor's requisition directly with the hospital or blood bank.\n2. Speak with the patient's attendant and check the cross-matching slip.\n3. Always donate blood directly on-site at the hospital or certified blood bank."},
                {"What should I do if someone asks for advance money?", "1. Never send advance money or travel expenses via mobile financial services (bKash/Nagad/Rocket).\n2. Genuine blood donors donate voluntarily and never demand advance money.\n3. Instantly refuse and disconnect if anyone requests advance payment."},
                {"How do I identify fake calls or messages?", "1. The caller creates false urgency and pressures you to transfer money immediately.\n2. They ask you to click suspicious links or share verification OTPs.\n3. They are unable to provide verified hospital admission details or doctor prescriptions."},
                {"Are all social media blood posts real?", "1. Many social media blood requests are outdated posts circulated repeatedly.\n2. Scammers often replace genuine contact numbers with their own mobile payment numbers.\n3. Always verify with the hospital and confirm if blood is still actively required."}
        };

        populateFaqContainer(activity, binding.layoutSection1Container, sec1Items);

        // 2. Populate Section 2: প্রতারণা হলে করণীয় (App Misuse)
        String[][] sec2Items = isBn ? new String[][]{
                {"আমাদের অ্যাপ ব্যবহার করে কেউ প্রতারণা করলে কী করবো?", "১. তাৎক্ষণিকভাবে অ্যাপের সাপোর্ট সেন্টারে প্রতারকের আইডি, ফোন নম্বর ও বিস্তারিত ঘটনা উল্লেখ করে রিপোর্ট করুন।\n২. অ্যাপ টিম দ্রুততার সাথে সেই অ্যাকাউন্ট ও ডিভাইস স্থায়ীভাবে ব্লক করবে।\n৩. আইনি পদক্ষেপ গ্রহণে যাবতীয় সহায়তা প্রদান করবে।"},
                {"কোথায় রিপোর্ট করবো?", "১. দ্বীনওয়ান অ্যাপের সেটিংস থেকে 'সাপোর্ট ও মতামত' মেন্যুতে রিপোর্ট জমা দিন।\n২. আমাদের অফিশিয়াল হেল্পলাইন এবং ইমেইলে সরাসরি মেসেজ পাঠান।\n৩. জরুরি প্রয়োজনে আমাদের হটলাইনে যোগাযোগ করুন।"},
                {"রিপোর্ট করতে কী লাগবে?", "১. প্রতারকের ব্যবহৃত মোবাইল নম্বর ও চ্যাট হিস্ট্রি বা স্ক্রিনশট।\n২. কল রেকর্ডিং (যদি থাকে)।\n৩. কোনো আর্থিক লেনদেন হয়ে থাকলে ট্রানজেকশন আইডি (TrxID) ও স্টেটমেন্ট।\n৪. সংশ্লিষ্ট রক্তের আবেদনের স্ক্রিনশট।"},
                {"অ্যাপ কী ব্যবস্থা নেবে?", "১. অভিযুক্ত ব্যক্তির ডিভাইস ও অ্যাকাউন্ট তাৎক্ষণিকভাবে সম্পূর্ণ ব্যান করা হবে।\n২. প্রতারকের ফোন নম্বর কালো তালিকাভুক্ত (Blacklist) করা হবে যাতে ভবিষ্যতে কোনো সার্ভিস না পায়।\n৩. প্রয়োজনে সাইবার ক্রাইম তদন্তকারী আইন-শৃঙ্খলা বাহিনীর কাছে তথ্য হস্তান্তর করা হবে।"},
                {"টাকা হারালে কী করবো?", "১. লেনদেনের ট্রানজেকশন আইডি (TrxID) সহ নিকটস্থ থানায় একটি সাধারণ ডায়েরি (GD) করুন।\n২. মোবাইল ব্যাংকিং কাস্টমার কেয়ারে (বিকাশ ১৬২৪৭ / নগদ ১৬১৬৭) ফোন করে প্রতারকের অ্যাকাউন্ট ফ্রিজ করার আবেদন করুন।\n৩. জাতীয় সাইবার হেল্পলাইনে (১৩২১৯) অভিযোগ দায়ের করুন।"}
        } : new String[][]{
                {"What to do if someone cheats using our app?", "1. Immediately report the scammer's ID and phone number to our support desk.\n2. The administration team will swiftly ban the offending account and device.\n3. We will cooperate with law enforcement agencies if legal action is pursued."},
                {"Where can I submit a report?", "1. Submit via 'Support & Feedback' in the DeenOne app settings.\n2. Send proof and screenshots directly to our official support email.\n3. Contact our support team directly through the hotline."},
                {"What is required to report a fraud?", "1. Scammer's phone number and conversation screenshots.\n2. Call recordings (if available).\n3. Mobile banking Transaction ID (TrxID) and payment receipt (if money was transferred).\n4. Details of the relevant blood request."},
                {"What action will the app take?", "1. The culprit's device and account will be permanently banned.\n2. The phone number will be blacklisted across the entire platform.\n3. Logs and evidence will be provided to law enforcement/cyber crime units when requested."},
                {"What should I do if I lost money?", "1. File a General Diary (GD) at your nearest police station with transaction IDs.\n2. Contact your mobile financial service helpline immediately to request an account freeze.\n3. Lodge an official complaint with the national cyber helpline (13219)."}
        };

        populateFaqContainer(activity, binding.layoutSection2Container, sec2Items);

        dialog.show();
    }

    private static void populateFaqContainer(Activity activity, LinearLayout container, String[][] items) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(activity);

        int normalBorderColor = ContextCompat.getColor(activity, R.color.border_card);
        int activeBorderColor = Color.parseColor("#E53935");

        for (String[] item : items) {
            ItemBloodFaqCardBinding cardBinding = ItemBloodFaqCardBinding.inflate(inflater, container, false);

            cardBinding.tvFaqQuestion.setText(item[0]);
            cardBinding.tvFaqAnswer.setText(item[1]);

            // Default state: collapsed
            cardBinding.layoutFaqAnswer.setVisibility(View.GONE);
            cardBinding.ivFaqArrow.setRotation(0f);
            cardBinding.cardFaqRoot.setStrokeColor(normalBorderColor);

            // Click listener on header row to toggle
            // STRICT RULE 7: ZERO touch animation on the card
            cardBinding.cardFaqRoot.setOnClickListener(v -> toggleCard(cardBinding, normalBorderColor, activeBorderColor));
            cardBinding.layoutFaqHeader.setOnClickListener(v -> toggleCard(cardBinding, normalBorderColor, activeBorderColor));

            container.addView(cardBinding.getRoot());
        }
    }

    private static void toggleCard(ItemBloodFaqCardBinding cardBinding, int normalBorderColor, int activeBorderColor) {
        boolean isExpanded = cardBinding.layoutFaqAnswer.getVisibility() == View.VISIBLE;
        if (isExpanded) {
            cardBinding.layoutFaqAnswer.setVisibility(View.GONE);
            cardBinding.ivFaqArrow.animate().rotation(0f).setDuration(150).start();
            cardBinding.cardFaqRoot.setStrokeColor(normalBorderColor);
        } else {
            cardBinding.layoutFaqAnswer.setVisibility(View.VISIBLE);
            cardBinding.ivFaqArrow.animate().rotation(180f).setDuration(150).start();
            cardBinding.cardFaqRoot.setStrokeColor(activeBorderColor);
        }
    }
}
