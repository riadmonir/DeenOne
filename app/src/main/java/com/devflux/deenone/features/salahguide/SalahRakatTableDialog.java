package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageSalahRakatTableBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SalahRakatTableDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahRakatTableBinding binding = PageSalahRakatTableBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);
        binding.tvHeaderTitle.setText(isBn ? "৫ ওয়াক্ত নামাজের রাকাত সমূহ" : "Rakat Breakdown of 5 Prayers");

        // Back button
        binding.btnBackRakatTable.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackRakatTable);

        // Share button
        binding.btnShareRakatTable.setOnClickListener(v -> shareRakatSchedule(activity));
        TouchAnimationUtil.attachTouchSpring(binding.btnShareRakatTable);

        // Spring feedback on legend cards

        // Interactive hints on Waqt Rows
        binding.rowFajrMatrix.setOnClickListener(v -> 
                showInfo(activity, isBn ? "ফজর" : "Fajr", isBn ? "মোট ৪ রাকাত:\n• ২ রাকাত সুন্নতে মুয়াক্কাদা\n• ২ রাকাত ফরজ" : "Total 4 Rakats:\n• 2 Sunnah Muakkadah\n• 2 Fardh", isBn));
        binding.rowDhuhrMatrix.setOnClickListener(v -> 
                showInfo(activity, isBn ? "যোহর" : "Dhuhr", isBn ? "মোট ১২ রাকাত:\n• ৪ রাকাত সুন্নতে মুয়াক্কাদা\n• ৪ রাকাত ফরজ\n• ২ রাকাত সুন্নতে মুয়াক্কাদা\n• ২ রাকাত নফল" : "Total 12 Rakats:\n• 4 Sunnah Muakkadah\n• 4 Fardh\n• 2 Sunnah Muakkadah\n• 2 Nafl", isBn));
        binding.rowJumuahMatrix.setOnClickListener(v -> 
                showInfo(activity, isBn ? "জুমুআ" : "Jumu'ah", isBn ? "মোট ১৪ রাকাত:\n• ৪ রাকাত কাবলাল জুমা\n• ২ রাকাত ফরজ\n• ৪ রাকাত বা'দাল জুমা\n• ২ রাকাত সুন্নত\n• ২ রাকাত নফল" : "Total 14 Rakats:\n• 4 Qablal Jumu'ah\n• 2 Fardh\n• 4 Ba'dal Jumu'ah\n• 2 Sunnah\n• 2 Nafl", isBn));
        binding.rowAsrMatrix.setOnClickListener(v -> 
                showInfo(activity, isBn ? "আসর" : "Asr", isBn ? "মোট ৮ রাকাত:\n• ৪ রাকাত সুন্নতে গায়রে মুয়াক্কাদা\n• ৪ রাকাত ফরজ" : "Total 8 Rakats:\n• 4 Sunnah Ghayr Muakkadah\n• 4 Fardh", isBn));
        binding.rowMaghribMatrix.setOnClickListener(v -> 
                showInfo(activity, isBn ? "মাগরিব" : "Maghrib", isBn ? "মোট ৭ রাকাত:\n• ৩ রাকাত ফরজ\n• ২ রাকাত সুন্নতে মুয়াক্কাদা\n• ২ রাকাত নফল" : "Total 7 Rakats:\n• 3 Fardh\n• 2 Sunnah Muakkadah\n• 2 Nafl", isBn));
        binding.rowIshaMatrix.setOnClickListener(v -> 
                showInfo(activity, isBn ? "এশা" : "Isha", isBn ? "মোট ১৭ রাকাত:\n• ৪ রাকাত সুন্নতে গায়রে মুয়াক্কাদা\n• ৪ রাকাত ফরজ\n• ২ রাকাত সুন্নতে মুয়াক্কাদা\n• ২ রাকাত নফল\n• ৩ রাকাত বিতর ওয়াজিব\n• ২ রাকাত নফল" : "Total 17 Rakats:\n• 4 Sunnah Ghayr Muakkadah\n• 4 Fardh\n• 2 Sunnah Muakkadah\n• 2 Nafl\n• 3 Witr Wajib\n• 2 Nafl", isBn));

        TouchAnimationUtil.attachTouchSpring(binding.rowFajrMatrix);
        TouchAnimationUtil.attachTouchSpring(binding.rowDhuhrMatrix);
        TouchAnimationUtil.attachTouchSpring(binding.rowJumuahMatrix);
        TouchAnimationUtil.attachTouchSpring(binding.rowAsrMatrix);
        TouchAnimationUtil.attachTouchSpring(binding.rowMaghribMatrix);
        TouchAnimationUtil.attachTouchSpring(binding.rowIshaMatrix);

        dialog.show();
    }

    private static void showInfo(Activity activity, String title, String message, boolean isBn) {
        if (activity == null || activity.isFinishing()) return;
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                .show();
    }

    private static void shareRakatSchedule(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        String shareText = isBn
                ? "🕌 ৫ ওয়াক্ত নামাজের রাকাত সমূহ:\n\n"
                + "🌅 ফজর: মোট ৪ রাকাত\n"
                + "• ২ রাকাত সুন্নতে মুয়াক্কাদা + ২ রাকাত ফরজ\n\n"
                + "☀️ যোহর: মোট ১২ রাকাত\n"
                + "• ৪ রাকাত সুন্নতে মুয়াক্কাদা + ৪ রাকাত ফরজ + ২ রাকাত সুন্নতে মুয়াক্কাদা + ২ রাকাত নফল\n\n"
                + "🕌 জুমুআ: মোট ১৪ রাকাত\n"
                + "• ৪ রাকাত কাবলাল জুমা + ২ রাকাত ফরজ + ৪ রাকাত বা'দাল জুমা + ২ রাকাত সুন্নত + ২ রাকাত নফল\n\n"
                + "🌤️ আসর: মোট ৮ রাকাত\n"
                + "• ৪ রাকাত সুন্নতে গায়রে মুয়াক্কাদা + ৪ রাকাত ফরজ\n\n"
                + "🌇 মাগরিব: মোট ৭ রাকাত\n"
                + "• ৩ রাকাত ফরজ + ২ রাকাত সুন্নতে মুয়াক্কাদা + ২ রাকাত নফল\n\n"
                + "🌌 এশা: মোট ১৭ রাকাত\n"
                + "• ৪ রাকাত সুন্নতে গায়রে মুয়াক্কাদা + ৪ রাকাত ফরজ + ২ রাকাত সুন্নতে মুয়াক্কাদা + ২ রাকাত নফল + ৩ রাকাত বিতর ওয়াজিব + ২ রাকাত নফল\n\n"
                + "— দ্বীনওয়ান"
                : "🕌 Rakat Breakdown of 5 Daily Prayers:\n\n"
                + "🌅 Fajr: Total 4 Rakats\n"
                + "• 2 Sunnah Muakkadah + 2 Fardh\n\n"
                + "☀️ Dhuhr: Total 12 Rakats\n"
                + "• 4 Sunnah Muakkadah + 4 Fardh + 2 Sunnah Muakkadah + 2 Nafl\n\n"
                + "🕌 Jumu'ah: Total 14 Rakats\n"
                + "• 4 Qablal Jumu'ah + 2 Fardh + 4 Ba'dal Jumu'ah + 2 Sunnah + 2 Nafl\n\n"
                + "🌤️ Asr: Total 8 Rakats\n"
                + "• 4 Sunnah Ghayr Muakkadah + 4 Fardh\n\n"
                + "🌇 Maghrib: Total 7 Rakats\n"
                + "• 3 Fardh + 2 Sunnah Muakkadah + 2 Nafl\n\n"
                + "🌌 Isha: Total 17 Rakats\n"
                + "• 4 Sunnah Ghayr Muakkadah + 4 Fardh + 2 Sunnah Muakkadah + 2 Nafl + 3 Witr Wajib + 2 Nafl\n\n"
                + "— DeenOne";

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, isBn ? "৫ ওয়াক্ত নামাজের রাকাত শেয়ার করুন" : "Share Salah Rakat Schedule");
        activity.startActivity(shareIntent);
    }
}
