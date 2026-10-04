package com.devflux.deenone.features.ramadan.ui;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageRozaHadithBinding;
import com.devflux.deenone.features.ramadan.adapter.RozaHadithAdapter;
import com.devflux.deenone.features.ramadan.data.RozaHadithRepository;
import com.devflux.deenone.features.ramadan.model.RozaHadithItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

/**
 * Production-ready Page Dialog for: রোজার হাদিস (Hadiths of Fasting).
 * Built with 100% fidelity to the screenshot, supporting Dark/Light mode,
 * dual-language clean policy, and ultra-fast 60 FPS scrolling.
 */
public class RozaHadithPageDialog {

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageRozaHadithBinding binding = PageRozaHadithBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Header Localization (Verbatim: "রোজার হাদিস" / "Hadiths of Fasting")
        String title = isBn ? "রোজার হাদিস" : "Hadiths of Fasting";
        binding.tvRozaHadithTitle.setText(title);

        // Spring Touch on Back Button
        TouchAnimationUtil.attachTouchSpring(binding.btnBackRozaHadith);
        binding.btnBackRozaHadith.setOnClickListener(v -> dialog.dismiss());

        // RecyclerView & Adapter
        binding.rvRozaHadiths.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvRozaHadiths.setHasFixedSize(true);
        binding.rvRozaHadiths.setItemViewCacheSize(20);

        List<RozaHadithItem> initialItems = RozaHadithRepository.getHadiths(activity, updatedItems -> {
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    RozaHadithAdapter adapter = (RozaHadithAdapter) binding.rvRozaHadiths.getAdapter();
                    if (adapter != null) {
                        adapter.updateData(updatedItems);
                    }
                }
            });
        });

        RozaHadithAdapter adapter = new RozaHadithAdapter(initialItems, isBn);
        binding.rvRozaHadiths.setAdapter(adapter);

        dialog.show();
    }
}
