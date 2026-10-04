package com.devflux.deenone.features.about;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.theme.ThemeManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemTeamMemberCardBinding;
import com.devflux.deenone.databinding.PageAboutUsBinding;
import com.devflux.deenone.utils.AsyncImageLoader;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class AboutUsPageDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageAboutUsBinding binding = PageAboutUsBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);
        boolean isDark = ThemeManager.getSavedThemeMode(activity) == ThemeManager.THEME_DARK;

        // Theme Icon
        binding.ivAboutThemeIcon.setImageResource(isDark ? R.drawable.ic_sun : R.drawable.ic_moon);

        // Attach Spring Touch Effects
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseAboutUs);
        TouchAnimationUtil.attachTouchSpring(binding.btnAboutThemeToggle);
        TouchAnimationUtil.attachTouchSpring(binding.btnAboutNotifications);
        TouchAnimationUtil.attachTouchSpring(binding.btnVersionBadge);

        // Header Navigation Listeners
        binding.btnCloseAboutUs.setOnClickListener(v -> dialog.dismiss());
        binding.btnAboutThemeToggle.setOnClickListener(v -> {
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).toggleAppTheme();
                dialog.dismiss();
            }
        });
        binding.btnAboutNotifications.setOnClickListener(v -> {
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).showNotificationHistorySheet();
            }
        });

        binding.btnVersionBadge.setOnClickListener(v -> {
            Toast.makeText(activity, isBn ? "আপনি দ্বীনওয়ানের সর্বশেষ সংস্করণ ব্যবহার করছেন" : "You are using the latest version of DeenOne", Toast.LENGTH_SHORT).show();
        });

        // 1. Render Initial Cached Data (Instant Zero-Lag Display)
        AboutUsModel.AboutInfo cachedInfo = AboutUsManager.getCachedAboutInfo(activity);
        List<AboutUsModel.TeamMember> cachedTeam = AboutUsManager.getCachedTeamMembers(activity);

        renderAboutInfo(binding, cachedInfo, isBn);
        renderTeamMembers(activity, binding.layoutTeamMembersContainer, cachedTeam, isBn);

        // 2. Background Remote Sync via PHP REST API
        AboutUsManager.syncRemoteData(activity, (freshInfo, freshTeam) -> {
            if (activity.isFinishing() || activity.isDestroyed()) return;
            renderAboutInfo(binding, freshInfo, isBn);
            renderTeamMembers(activity, binding.layoutTeamMembersContainer, freshTeam, isBn);
        });

        dialog.show();
    }

    private static void renderAboutInfo(PageAboutUsBinding binding, AboutUsModel.AboutInfo info, boolean isBn) {
        if (info == null) return;

        // Brand tagline
        binding.tvAboutTagline.setText(isBn ? info.taglineBn : info.taglineEn);

        // Version card
        binding.tvVersionLabel.setText(isBn ? "অ্যাপ সংস্করণ" : "App Version");
        binding.tvVersionNumber.setText(info.appVersion);
        binding.btnVersionBadge.setText(isBn ? info.versionBadgeBn : info.versionBadgeEn);

        // Developers Section Heading
        binding.tvDevelopersHeading.setText(isBn ? "অ্যাপ্লিকেশন ডেভেলপার" : "Application Developer");

        // Mission Card
        binding.tvMissionHeading.setText(isBn ? info.missionTitleBn : info.missionTitleEn);
        binding.tvMissionDescription.setText(isBn ? info.missionDescBn : info.missionDescEn);
        binding.tvMissionQuote.setText(isBn ? info.quoteTextBn : info.quoteTextEn);

        // Pill Tags
        if (info.pills != null && info.pills.size() >= 4) {
            binding.pill1.setText(info.pills.get(0));
            binding.pill2.setText(info.pills.get(1));
            binding.pill3.setText(info.pills.get(2));
            binding.pill4.setText(info.pills.get(3));
        }

        // Core Values
        binding.tvValuesTitle.setText(isBn ? "দ্বীনওয়ানের মূল অঙ্গীকার" : "DeenOne Core Commitments");
        binding.tvValue1.setText(isBn ? "১০০% সহীহ ও প্রামাণিক ইসলামিক তথ্য নীতি" : "100% Authentic & Verified Islamic Principles");
        binding.tvValue2.setText(isBn ? "শতভাগ বিজ্ঞাপনমুক্ত ও নিরাপদ অভিজ্ঞতা" : "100% Ad-Free & Pure Spiritual Experience");
        binding.tvValue3.setText(isBn ? "অফলাইন ফার্স্ট ও ব্যক্তিগত তথ্য সুরক্ষামূলক আর্কিটেকচার" : "Offline-First & Privacy-Preserving Architecture");

        // Copyright Footer
        binding.tvAboutFooterCopyright.setText(isBn ? info.copyrightBn : info.copyrightEn);
    }

    private static void renderTeamMembers(Activity activity, LinearLayout container, List<AboutUsModel.TeamMember> team, boolean isBn) {
        if (container == null || team == null || team.isEmpty()) return;
        container.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(activity);

        if (team.size() == 1) {
            // Single Member (e.g. Riad Monir - Founder & Lead Developer)
            AboutUsModel.TeamMember m = team.get(0);
            View cardView = createMemberCard(activity, inflater, m, isBn);
            container.addView(cardView);
        } else {
            // Multiple Members: arrange in 2-column horizontal rows
            LinearLayout currentRow = null;
            for (int i = 0; i < team.size(); i++) {
                if (i % 2 == 0) {
                    currentRow = new LinearLayout(activity);
                    currentRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    currentRow.setOrientation(LinearLayout.HORIZONTAL);
                    currentRow.setWeightSum(2f);
                    if (i > 0) {
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) currentRow.getLayoutParams();
                        lp.topMargin = (int) (12 * activity.getResources().getDisplayMetrics().density);
                        currentRow.setLayoutParams(lp);
                    }
                    container.addView(currentRow);
                }

                AboutUsModel.TeamMember m = team.get(i);
                View cardView = createMemberCard(activity, inflater, m, isBn);
                LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                if (i % 2 == 0) {
                    cardLp.rightMargin = (int) (6 * activity.getResources().getDisplayMetrics().density);
                } else {
                    cardLp.leftMargin = (int) (6 * activity.getResources().getDisplayMetrics().density);
                }
                cardView.setLayoutParams(cardLp);
                if (currentRow != null) {
                    currentRow.addView(cardView);
                }
            }
        }
    }

    private static View createMemberCard(Activity activity, LayoutInflater inflater, AboutUsModel.TeamMember m, boolean isBn) {
        ItemTeamMemberCardBinding cardBinding = ItemTeamMemberCardBinding.inflate(inflater, null, false);

        // Name & Role
        cardBinding.tvMemberName.setText(isBn ? m.nameBn : m.nameEn);
        cardBinding.tvMemberRole.setText(isBn ? m.roleBn : m.roleEn);

        // Avatar
        if (m.avatarUrl != null && !m.avatarUrl.trim().isEmpty()) {
            String fullUrl = m.avatarUrl;
            if (!fullUrl.startsWith("http://") && !fullUrl.startsWith("https://")) {
                String baseUrl = BackendConfigManager.getPhpApiBaseUrl(activity);
                if (baseUrl.endsWith("/api/")) {
                    baseUrl = baseUrl.substring(0, baseUrl.length() - 4); // removes 'api/' to point to root
                }
                if (!baseUrl.endsWith("/")) baseUrl += "/";
                fullUrl = baseUrl + fullUrl;
            }
            AsyncImageLoader.getInstance(activity).loadImage(cardBinding.ivMemberAvatar, fullUrl, R.drawable.ic_deenone_logo);
        } else {
            cardBinding.ivMemberAvatar.setImageResource(R.drawable.ic_deenone_logo);
        }

        // Spring animation on interactive elements

        // Social Button Click Handlers
        bindSocialClick(activity, cardBinding.btnMemberLinkedIn, m.linkedinUrl);
        bindSocialClick(activity, cardBinding.btnMemberGitHub, m.githubUrl);
        bindSocialClick(activity, cardBinding.btnMemberWebsite, m.websiteUrl);

        return cardBinding.getRoot();
    }

    private static void bindSocialClick(Activity activity, View button, String url) {
        if (button == null) return;
        if (url != null && !url.trim().isEmpty()) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    activity.startActivity(intent);
                } catch (Exception ignored) {}
            });
        } else {
            button.setVisibility(View.GONE);
        }
    }
}
