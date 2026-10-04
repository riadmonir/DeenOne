package com.devflux.deenone.features.ramadan.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemRozaItikafTopicRowBinding;
import com.devflux.deenone.databinding.PageRozaItikafBinding;
import com.devflux.deenone.features.ramadan.data.RozaItikafRepository;
import com.devflux.deenone.features.ramadan.model.RozaItikafTopicItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Production-ready Page Dialog for: section: ই'তিকাফ (Itikaf Section).
 * 100% matches screenshot:
 * - Top Header: Back button (←) and title "ই'তিকাফ" / "Itikaf"
 * - Vertical list of 6 topics:
 *   1. ই'তিকাফের পরিচয়
 *   2. ই'তিকাফের ফজিলত ও গুরুত্ব
 *   3. ই'তিকাফের শর্তাবলী
 *   4. ই'তিকাফের প্রকারভেদ
 *   5. ই'তিকাফের সুন্নাত ও আদব
 *   6. ই'তিকাফ ভঙ্গ হওয়ার কারণ
 * - Clicking any card opens RozaItikafTopicDetailDialog showing verbatim content.
 * - STRICT Rule 7: ZERO touch animation on CardViews; touch spring ONLY on back button.
 * - 60 FPS ultra-smooth scrolling, lag-free performance.
 */
public class RozaItikafPageDialog {

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageRozaItikafBinding binding = PageRozaItikafBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "ই'তিকাফ" / "Itikaf")
        binding.tvRozaItikafTitle.setText(isBn ? "ই'তিকাফ" : "Itikaf");

        // Back Button with Spring Touch Effect (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnBackRozaItikaf);
        binding.btnBackRozaItikaf.setOnClickListener(v -> dialog.dismiss());

        // Setup RecyclerView with 6 Topics
        binding.rvItikafTopicsList.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvItikafTopicsList.setHasFixedSize(true);
        binding.rvItikafTopicsList.setItemViewCacheSize(6);

        List<RozaItikafTopicItem> initialItems = RozaItikafRepository.getTopics(activity, updatedItems -> {
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing() && !activity.isDestroyed() && binding.rvItikafTopicsList.getAdapter() instanceof RozaItikafTopicAdapter) {
                    ((RozaItikafTopicAdapter) binding.rvItikafTopicsList.getAdapter()).updateData(updatedItems);
                }
            });
        });

        RozaItikafTopicAdapter adapter = new RozaItikafTopicAdapter(activity, initialItems, isBn);
        binding.rvItikafTopicsList.setAdapter(adapter);

        dialog.show();
    }

    public static class RozaItikafTopicAdapter extends RecyclerView.Adapter<RozaItikafTopicAdapter.ViewHolder> {
        private final Activity activity;
        private final List<RozaItikafTopicItem> items = new ArrayList<>();
        private final boolean isBn;

        public RozaItikafTopicAdapter(Activity activity, List<RozaItikafTopicItem> initialItems, boolean isBn) {
            this.activity = activity;
            if (initialItems != null) {
                this.items.addAll(initialItems);
            }
            this.isBn = isBn;
        }

        public void updateData(List<RozaItikafTopicItem> newItems) {
            this.items.clear();
            if (newItems != null) {
                this.items.addAll(newItems);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemRozaItikafTopicRowBinding binding = ItemRozaItikafTopicRowBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RozaItikafTopicItem item = items.get(position);
            ItemRozaItikafTopicRowBinding b = holder.binding;

            b.tvItikafTopicTitle.setText(item.getTitle(isBn));

            // Card click opens the topic's detailed card view
            // STRICT Rule 7: ZERO touch animation on the card!
            b.cardItikafTopicItem.setOnClickListener(v -> RozaItikafTopicDetailDialog.show(activity, item));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemRozaItikafTopicRowBinding binding;

            public ViewHolder(ItemRozaItikafTopicRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
