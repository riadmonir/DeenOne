package com.devflux.deenone.features.ramadan.ui;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageRozaTaraweehBinding;
import com.devflux.deenone.features.ramadan.adapter.RozaTaraweehAdapter;
import com.devflux.deenone.features.ramadan.data.RozaTaraweehRepository;
import com.devflux.deenone.features.ramadan.model.RozaTaraweehItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

/**
 * Production-ready Page Dialog for: রোজা:- তারাবীহ (Taraweeh Section).
 * 100% matches user screenshot with clean list, chevron arrows, dual language,
 * and ultra-fast 60 FPS scrolling.
 */
public class RozaTaraweehPageDialog {

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageRozaTaraweehBinding binding = PageRozaTaraweehBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "তারাবীহ" / "Taraweeh")
        String title = isBn ? "তারাবীহ" : "Taraweeh";
        binding.tvTaraweehHeaderTitle.setText(title);

        // Spring touch on back button
        TouchAnimationUtil.attachTouchSpring(binding.btnBackTaraweeh);
        binding.btnBackTaraweeh.setOnClickListener(v -> dialog.dismiss());

        // Setup RecyclerView
        binding.rvTaraweehList.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvTaraweehList.setHasFixedSize(true);
        binding.rvTaraweehList.setItemViewCacheSize(12);

        List<RozaTaraweehItem> initialItems = RozaTaraweehRepository.getTopics(activity, updatedItems -> {
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    RozaTaraweehAdapter adapter = (RozaTaraweehAdapter) binding.rvTaraweehList.getAdapter();
                    if (adapter != null) {
                        adapter.updateData(updatedItems);
                    }
                }
            });
        });

        RozaTaraweehAdapter adapter = new RozaTaraweehAdapter(activity, initialItems, isBn);
        binding.rvTaraweehList.setAdapter(adapter);

        dialog.show();
    }
}
