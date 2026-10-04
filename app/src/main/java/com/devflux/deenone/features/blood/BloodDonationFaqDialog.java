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
import com.devflux.deenone.databinding.DialogBloodDonationFaqBinding;
import com.devflux.deenone.databinding.ItemBloodFaqCardBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class BloodDonationFaqDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogBloodDonationFaqBinding binding = DialogBloodDonationFaqBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Theme Icon
        boolean isDark = ThemeManager.getSavedThemeMode(activity) == ThemeManager.THEME_DARK;
        binding.ivFaqThemeIcon.setImageResource(isDark ? R.drawable.ic_sun : R.drawable.ic_moon);

        // Header Texts (Matches Screenshots 7, 8, 9)
        binding.tvFaqBadgeText.setText(isBn ? "স্বাস্থ্য ও রক্তদান" : "Health & Blood Donation");
        binding.tvFaqMainTitle.setText(isBn ? "রক্তদান সাধারণ প্রশ্নোত্তর (FAQ)" : "Blood Donation FAQ");

        binding.tvCtaSaveLifeTitle.setText(isBn ? "রক্তদান করুন, জীবন বাঁচান" : "Donate Blood, Save Lives");
        binding.tvCtaSaveLifeHadith.setText(isBn
                ? "\"যে ব্যক্তি একজনের জীবন বাঁচালো, সে যেন গোটা মানবজাতিকে বাঁচালো।\""
                : "\"Whoever saves a life is as though he had saved the whole of humanity.\"");
        binding.tvFindDonorsBtnText.setText(isBn ? "ডোনার খুঁজুন" : "Find Donors");

        // Attach Spring Touch Animation ONLY to buttons (Strict Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseBloodFaq);
        TouchAnimationUtil.attachTouchSpring(binding.btnFaqThemeToggle);
        TouchAnimationUtil.attachTouchSpring(binding.btnFaqNotification);
        TouchAnimationUtil.attachTouchSpring(binding.btnFindDonorsFromFaq);

        binding.btnCloseBloodFaq.setOnClickListener(v -> dialog.dismiss());
        binding.btnFaqThemeToggle.setOnClickListener(v -> {
            if (activity instanceof MainActivity mainActivity) {
                mainActivity.toggleAppTheme();
                dialog.dismiss();
            }
        });
        binding.btnFaqNotification.setOnClickListener(v -> {
            if (activity instanceof MainActivity mainActivity) {
                mainActivity.showNotificationHistorySheet();
            }
        });

        // "ডোনার খুঁজুন" Action Button -> Opens Blood Donor Search Dialog directly
        binding.btnFindDonorsFromFaq.setOnClickListener(v -> {
            dialog.dismiss();
            BloodDonorSearchDialog.show(activity);
        });

        // Verbatim 12 Questions Matching Screenshots Exactly (Bengali & English)
        String[][] faqItems = isBn ? new String[][]{
                {"১. কে রক্ত দিতে পারবেন?", "১৮-৬০ বছর বয়সী এবং কমপক্ষে ৫০ কেজি ওজনের সুস্থ ব্যক্তি রক্ত দিতে পারবেন।"},
                {"২. রক্ত দেওয়ার আগে কী প্রস্তুতি নিতে হবে?", "রক্ত দেওয়ার আগের রাতে ভালো ঘুম হওয়া জরুরি। রক্তদানের আগে প্রচুর পানি বা তরল পান করুন এবং পুষ্টিকর খাবার খান। খালি পেটে কখনো রক্ত দেওয়া উচিত নয়।"},
                {"৩. রক্ত দেওয়ার সময় কি ব্যথা লাগে?", "না, সুই প্রবেশের সময় সামান্য একটু পিঁপড়ার কামড়ের মতো অনুভূতি হতে পারে। পুরো প্রক্রিয়াটি সম্পূর্ণ নিরাপদ ও ব্যথাহীন।"},
                {"৪. রক্ত দেওয়ার পর কী করতে হবে?", "রক্তদানের পর ১০-১৫ মিনিট বিশ্রাম নিন, স্যালাইন বা জুস পান করুন এবং প্রচুর পানি গ্রহণ করুন। ঐ দিন ভারী কাজ বা অতিরিক্ত শারীরিক পরিশ্রম থেকে বিরত থাকুন।"},
                {"৫. কতদিন পর পর রক্ত দেওয়া যায়?", "সুস্থ পুরুষ প্রতি ৩ মাস পর পর এবং সুস্থ নারী প্রতি ৪ মাস পর পর নিয়মিত রক্ত দিতে পারবেন।"},
                {"৬. কোন অবস্থায় রক্ত দেওয়া যাবে না?", "জ্বর, সংক্রামক রোগ, অ্যান্টিবায়োটিক সেবনরত অবস্থা, হেপাটাইটিস, এইডস, ক্যান্সার, হৃদরোগ কিংবা গর্ভাবস্থায় রক্ত দেওয়া যাবে না।"},
                {"৭. রক্ত নেওয়ার আগে কোন কোন পরীক্ষা করা জরুরি?", "ব্লাড গ্রুপ ও হিমোগ্লোবিন ছাড়াও রোগীর শরীরে সঞ্চালনের আগে ৫টি বাধ্যতামূলক স্ক্রিনিং টেস্ট (HIV, HBV, HCV, Malaria, Syphilis) করা হয়।"},
                {"৮. ক্রস-ম্যাচিং কী এবং কেন প্রয়োজন?", "ক্রস-ম্যাচিং হলো রোগীর রক্তের সাথে রক্তদাতার রক্তের চূড়ান্ত সামঞ্জস্যতা পরীক্ষা, যাতে শরীরে কোনো পার্শ্বপ্রতিক্রিয়া বা রক্ত জমাট না বাঁধে।"},
                {"৯. রক্ত দিলে কি শরীরে কোনো ক্ষতি হয়?", "না, কোনো ক্ষতি হয় না। বরং নিয়মিত রক্তদানে শরীরে নতুন রক্তকণিকা তৈরি হয়, হৃদরোগ ও স্ট্রোকের ঝুঁকি কমে এবং কোলেস্টেরল নিয়ন্ত্রণে থাকে।"},
                {"১০. রক্ত দেওয়ার পর কি দুর্বল লাগে?", "স্বাভাবিকভাবে তেমন কোনো দুর্বলতা লাগে না। প্রচুর তরল ও পুষ্টিকর খাবার গ্রহণ করলে ২৪ থেকে ৪৮ ঘণ্টার মধ্যে রক্তের তরল অংশ পূরণ হয়ে যায়।"},
                {"১১. জরুরি রক্তের জন্য কীভাবে অনুরোধ জানাবো?", "দ্বীনওয়ান অ্যাপের 'জরুরী রক্ত প্রয়োজন' অপশনে ক্লিক করে রোগীর নাম, রক্তের গ্রুপ, হাসপাতালের ঠিকানা ও ফোন নম্বর দিয়ে আবেদন পোস্ট করুন।"},
                {"১২. রক্তদাতাকে কি কোনো খরচ দিতে হয়?", "না, স্বেচ্ছায় রক্তদান সম্পূর্ণ বিনামূল্যে ও নিঃস্বার্থ মানবসেবা। রক্তদাতাকে কোনো অর্থ বা সম্মানী দিতে হয় না।"}
        } : new String[][]{
                {"1. Who can donate blood?", "Healthy individuals aged 18-60 years weighing at least 50 kg can donate blood."},
                {"2. What preparation is needed before donating blood?", "Ensure adequate sleep the night before. Drink plenty of fluids and eat a nutritious meal. Never donate on an empty stomach."},
                {"3. Does blood donation hurt?", "No, there is only a momentary mild sensation like an ant pinch. The entire process is sterile, safe, and painless."},
                {"4. What should be done after blood donation?", "Rest for 10-15 minutes, drink saline or fruit juice, and stay hydrated. Avoid heavy lifting or strenuous exercise for the day."},
                {"5. How frequently can one donate blood?", "Healthy adult males can donate every 3 months, and healthy females every 4 months."},
                {"6. When should blood donation be avoided?", "Do not donate during active fever, infectious diseases, antibiotic treatment, hepatitis, HIV, cancer, heart disease, or pregnancy."},
                {"7. Which tests are mandatory before transfusion?", "Blood group, hemoglobin, and 5 mandatory screening tests: HIV, Hepatitis B, Hepatitis C, Malaria, and Syphilis."},
                {"8. What is cross-matching and why is it essential?", "Cross-matching confirms biological compatibility between patient and donor blood to prevent adverse reactions or clotting."},
                {"9. Does donating blood harm the body?", "Not at all. Regular donation stimulates new red blood cell creation, lowers cardiovascular risks, and balances iron."},
                {"10. Does one feel weak after donating blood?", "Usually not. Fluid balance is fully restored within 24 to 48 hours with proper hydration and nutrition."},
                {"11. How to request emergency blood?", "Tap 'Emergency Blood Request' in the DeenOne app to submit patient name, required group, hospital, and phone number."},
                {"12. Does a donor have to pay any fee?", "No, voluntary blood donation is completely free and noble. No monetary payment or honorarium is required."}
        };

        populateFaqContainer(activity, binding.layoutBloodFaqContainer, faqItems);

        dialog.show();
    }

    private static void populateFaqContainer(Activity activity, LinearLayout container, String[][] items) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(activity);

        int normalBorderColor = androidx.core.content.ContextCompat.getColor(activity, R.color.border_card);
        int activeBorderColor = android.graphics.Color.parseColor("#E53935");

        for (int i = 0; i < items.length; i++) {
            String[] item = items[i];
            ItemBloodFaqCardBinding cardBinding = ItemBloodFaqCardBinding.inflate(inflater, container, false);

            cardBinding.tvFaqQuestion.setText(item[0]);
            cardBinding.tvFaqAnswer.setText(item[1]);

            // Expand first item by default to match screenshot 3, others collapsed
            if (i == 0) {
                cardBinding.layoutFaqAnswer.setVisibility(View.VISIBLE);
                cardBinding.ivFaqArrow.setRotation(180f);
                cardBinding.cardFaqRoot.setStrokeColor(activeBorderColor);
            } else {
                cardBinding.layoutFaqAnswer.setVisibility(View.GONE);
                cardBinding.ivFaqArrow.setRotation(0f);
                cardBinding.cardFaqRoot.setStrokeColor(normalBorderColor);
            }

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
