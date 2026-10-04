package com.devflux.deenone.features.quran;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.BanglaQuranManager;
import com.devflux.deenone.core.quran.BanglaQuranTranslator;
import com.devflux.deenone.databinding.DialogSelectBanglaTranslatorBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class BanglaQuranTranslatorDialog {

    public interface OnTranslatorSelectedListener {
        void onTranslatorSelected(BanglaQuranTranslator translator);
    }

    public static void show(Context context, OnTranslatorSelectedListener listener) {
        if (context == null) return;

        Dialog dialog = new AppCompatDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DialogSelectBanglaTranslatorBinding binding = DialogSelectBanglaTranslatorBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        boolean isBn = LocaleManager.isBengali(context);
        BanglaQuranManager manager = BanglaQuranManager.getInstance(context);
        BanglaQuranTranslator current = manager.getActiveTranslator();

        // Dual-language titles
        binding.tvTranslatorDialogTitle.setText(isBn ? "অনুবাদ সংস্করণ নির্বাচন করুন" : "Select Bangla Translation Edition");
        binding.tvTranslatorDialogSubtitle.setText(isBn
                ? "তানজিল প্রজেক্ট (Tanzil.net) অনুমোদিত বিশুদ্ধ বাংলা অনুবাদ"
                : "Authentic Bengali Translations from Tanzil Project");

        binding.tvMuhiuddinTitle.setText(isBn ? BanglaQuranTranslator.MUHIUDDIN_KHAN.fullNameBn : BanglaQuranTranslator.MUHIUDDIN_KHAN.fullNameEn);
        binding.tvMuhiuddinDesc.setText(isBn ? BanglaQuranTranslator.MUHIUDDIN_KHAN.descriptionBn : BanglaQuranTranslator.MUHIUDDIN_KHAN.descriptionEn);

        binding.tvZohurulTitle.setText(isBn ? BanglaQuranTranslator.ZOHURUL_HOQUE.fullNameBn : BanglaQuranTranslator.ZOHURUL_HOQUE.fullNameEn);
        binding.tvZohurulDesc.setText(isBn ? BanglaQuranTranslator.ZOHURUL_HOQUE.descriptionBn : BanglaQuranTranslator.ZOHURUL_HOQUE.descriptionEn);

        // Update UI selection state
        Runnable updateUi = () -> {
            BanglaQuranTranslator active = manager.getActiveTranslator();
            boolean isMuhiuddin = (active == BanglaQuranTranslator.MUHIUDDIN_KHAN);

            // Muhiuddin Card
            binding.ivRadioMuhiuddin.setImageResource(isMuhiuddin ? R.drawable.bg_radio_selected : R.drawable.bg_radio_unselected);
            binding.cardTranslatorMuhiuddin.setStrokeColor(ContextCompat.getColor(context, isMuhiuddin ? R.color.accent_mint : R.color.border_card));
            binding.badgeMuhiuddinActive.setVisibility(isMuhiuddin ? View.VISIBLE : View.GONE);

            // Zohurul Card
            binding.ivRadioZohurul.setImageResource(!isMuhiuddin ? R.drawable.bg_radio_selected : R.drawable.bg_radio_unselected);
            binding.cardTranslatorZohurul.setStrokeColor(ContextCompat.getColor(context, !isMuhiuddin ? R.color.accent_mint : R.color.border_card));
            binding.badgeZohurulActive.setVisibility(!isMuhiuddin ? View.VISIBLE : View.GONE);
        };

        updateUi.run();

        // Touch spring ONLY on buttons
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseTranslatorDialog);
        binding.btnCloseTranslatorDialog.setOnClickListener(v -> dialog.dismiss());

        binding.cardTranslatorMuhiuddin.setOnClickListener(v -> {
            manager.setActiveTranslator(BanglaQuranTranslator.MUHIUDDIN_KHAN);
            updateUi.run();
            if (listener != null) {
                listener.onTranslatorSelected(BanglaQuranTranslator.MUHIUDDIN_KHAN);
            }
            dialog.dismiss();
        });

        binding.cardTranslatorZohurul.setOnClickListener(v -> {
            manager.setActiveTranslator(BanglaQuranTranslator.ZOHURUL_HOQUE);
            updateUi.run();
            if (listener != null) {
                listener.onTranslatorSelected(BanglaQuranTranslator.ZOHURUL_HOQUE);
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}
