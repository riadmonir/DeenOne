package com.devflux.deenone.features.notifications.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.data.local.entity.NotificationMessageEntity;
import com.devflux.deenone.databinding.ItemNotificationHistoryCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationHistoryAdapter extends RecyclerView.Adapter<NotificationHistoryAdapter.NotificationViewHolder> {

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationMessageEntity item);
        void onDeleteClick(NotificationMessageEntity item);
    }

    private final List<NotificationMessageEntity> items = new ArrayList<>();
    private final OnNotificationClickListener listener;

    public NotificationHistoryAdapter(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<NotificationMessageEntity> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationHistoryCardBinding binding = ItemNotificationHistoryCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationHistoryCardBinding binding;

        NotificationViewHolder(ItemNotificationHistoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NotificationMessageEntity item, OnNotificationClickListener listener) {
            Context context = binding.getRoot().getContext();
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

            binding.tvNotificationTitle.setText(item.getTitle());
            binding.tvNotificationMessage.setText(item.getMessage());

            // Dual-language relative time
            binding.tvNotificationTime.setText(isBn
                ? BengaliNumberUtil.getBengaliRelativeTime(item.getTimestamp())
                : getEnglishRelativeTime(item.getTimestamp()));

            // Themed Icon Box, Icon and Category by Type
            String type = item.getType() != null ? item.getType().toLowerCase() : "";
            if (type.contains("sunnah")) {
                binding.flNotificationIconBox.getBackground().setTint(androidx.core.content.ContextCompat.getColor(context, R.color.feat_bg_audio));
                binding.ivNotificationIcon.setImageResource(R.drawable.ic_check_circle);
                binding.ivNotificationIcon.setColorFilter(androidx.core.content.ContextCompat.getColor(context, R.color.accent_mint));
                binding.tvNotificationTypeBadge.setText(isBn ? "সুন্নাহ আপডেট" : "Sunnah Update");
            } else if (type.contains("amal")) {
                binding.flNotificationIconBox.getBackground().setTint(androidx.core.content.ContextCompat.getColor(context, R.color.feat_bg_amal));
                binding.ivNotificationIcon.setImageResource(R.drawable.ic_sparkle);
                binding.ivNotificationIcon.setColorFilter(androidx.core.content.ContextCompat.getColor(context, R.color.accent_cyan));
                binding.tvNotificationTypeBadge.setText(isBn ? "নতুন আমল" : "Daily Deed");
            } else if (type.contains("adhan") || type.contains("prayer")) {
                binding.flNotificationIconBox.getBackground().setTint(androidx.core.content.ContextCompat.getColor(context, R.color.feat_bg_audio));
                binding.ivNotificationIcon.setImageResource(R.drawable.ic_nav_salat);
                binding.ivNotificationIcon.setColorFilter(androidx.core.content.ContextCompat.getColor(context, R.color.accent_mint));
                binding.tvNotificationTypeBadge.setText(isBn ? "সালাত অনুস্মারক" : "Prayer Reminder");
            } else if (type.contains("hadith")) {
                binding.flNotificationIconBox.getBackground().setTint(androidx.core.content.ContextCompat.getColor(context, R.color.feat_bg_hadith));
                binding.ivNotificationIcon.setImageResource(R.drawable.ic_menu_book);
                binding.ivNotificationIcon.setColorFilter(androidx.core.content.ContextCompat.getColor(context, R.color.accent_gold));
                binding.tvNotificationTypeBadge.setText(isBn ? "সহীহ হাদিস" : "Sahih Hadith");
            } else if (type.contains("dua")) {
                binding.flNotificationIconBox.getBackground().setTint(androidx.core.content.ContextCompat.getColor(context, R.color.feat_bg_dua));
                binding.ivNotificationIcon.setImageResource(R.drawable.ic_feat_dua);
                binding.ivNotificationIcon.setColorFilter(androidx.core.content.ContextCompat.getColor(context, R.color.accent_gold));
                binding.tvNotificationTypeBadge.setText(isBn ? "মাসনূন দোয়া" : "Masnoon Dua");
            } else if (type.contains("quiz")) {
                binding.flNotificationIconBox.getBackground().setTint(androidx.core.content.ContextCompat.getColor(context, R.color.feat_bg_quiz));
                binding.ivNotificationIcon.setImageResource(R.drawable.ic_feat_quiz);
                binding.ivNotificationIcon.setColorFilter(androidx.core.content.ContextCompat.getColor(context, R.color.accent_purple));
                binding.tvNotificationTypeBadge.setText(isBn ? "ইসলামিক কুইজ" : "Islamic Quiz");
            } else if (type.contains("blood")) {
                binding.flNotificationIconBox.getBackground().setTint(Color.parseColor("#261214"));
                binding.ivNotificationIcon.setImageResource(R.drawable.ic_feat_blood);
                binding.ivNotificationIcon.setColorFilter(Color.parseColor("#EF4444"));
                binding.tvNotificationTypeBadge.setText(isBn ? "রক্তের আবেদন" : "Blood Request");
            } else {
                binding.flNotificationIconBox.getBackground().setTint(androidx.core.content.ContextCompat.getColor(context, R.color.bg_badge_pill));
                binding.ivNotificationIcon.setImageResource(R.drawable.ic_notification);
                binding.ivNotificationIcon.setColorFilter(androidx.core.content.ContextCompat.getColor(context, R.color.accent_mint));
                binding.tvNotificationTypeBadge.setText(isBn ? "আপডেট ও নোটিশ" : "Updates & Notices");
            }

            binding.tvNotificationActionLink.setText(isBn ? "বিস্তারিত দেখুন →" : "View Details →");

            // Dynamic Unread Highlight Visual compatible with Dark & Light theme
            int unreadBg = androidx.core.content.ContextCompat.getColor(context, R.color.bg_card_active);
            int readBg = androidx.core.content.ContextCompat.getColor(context, R.color.bg_card);
            int unreadStroke = androidx.core.content.ContextCompat.getColor(context, R.color.accent_mint);
            int readStroke = androidx.core.content.ContextCompat.getColor(context, R.color.border_card);

            binding.cardNotificationHistoryItem.setCardBackgroundColor(!item.isRead() ? unreadBg : readBg);
            binding.cardNotificationHistoryItem.setStrokeColor(!item.isRead() ? unreadStroke : readStroke);
            binding.cardNotificationHistoryItem.setAlpha(!item.isRead() ? 1.0f : 0.95f);

            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tvNotificationActionLink);

            binding.cardNotificationHistoryItem.setOnClickListener(v -> {
                if (listener != null) listener.onNotificationClick(item);
            });

            binding.tvNotificationActionLink.setOnClickListener(v -> {
                if (listener != null) listener.onNotificationClick(item);
            });
        }

        private static String getEnglishRelativeTime(long timestamp) {
            long diff = System.currentTimeMillis() - timestamp;
            if (diff < 60 * 1000L) {
                return "Just now";
            } else if (diff < 60 * 60 * 1000L) {
                long mins = diff / (60 * 1000L);
                return mins + "m ago";
            } else if (diff < 24 * 60 * 60 * 1000L) {
                long hours = diff / (60 * 60 * 1000L);
                return hours + "h ago";
            } else {
                long days = diff / (24 * 60 * 60 * 1000L);
                return days + "d ago";
            }
        }
    }
}
