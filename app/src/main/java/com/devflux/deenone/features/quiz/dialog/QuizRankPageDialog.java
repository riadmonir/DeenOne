package com.devflux.deenone.features.quiz.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.features.quiz.adapter.QuizRankAdapter;
import com.devflux.deenone.features.quiz.model.QuizRankUser;
import com.devflux.deenone.features.quiz.repository.QuizRankRepository;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class QuizRankPageDialog {

    public static void show(@NonNull Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_quiz_rank_list, null);
        dialog.setContentView(view);

        boolean isBn = LocaleManager.isBengali(activity);

        // Bind Views
        TextView tvQuizRankTitle = view.findViewById(R.id.tvQuizRankTitle);
        TextView tvQuizRankSubtitle = view.findViewById(R.id.tvQuizRankSubtitle);
        TextView tvAllCompetitorsHeader = view.findViewById(R.id.tvAllCompetitorsHeader);
        FrameLayout btnRefreshQuizRank = view.findViewById(R.id.btnRefreshQuizRank);
        FrameLayout btnCloseQuizRank = view.findViewById(R.id.btnCloseQuizRank);
        RecyclerView rvQuizRankList = view.findViewById(R.id.rvQuizRankList);
        ProgressBar pbQuizRankLoading = view.findViewById(R.id.pbQuizRankLoading);
        TextView tvQuizRankEmpty = view.findViewById(R.id.tvQuizRankEmpty);
        LinearLayout cardCurrentUserBottomBar = view.findViewById(R.id.cardCurrentUserBottomBar);
        TextView tvCurrentUserAvatar = view.findViewById(R.id.tvCurrentUserAvatar);
        TextView tvMyRankLabel = view.findViewById(R.id.tvMyRankLabel);
        TextView tvMyRankValue = view.findViewById(R.id.tvMyRankValue);
        LinearLayout btnMyPointsPill = view.findViewById(R.id.btnMyPointsPill);
        TextView tvMyPointsValue = view.findViewById(R.id.tvMyPointsValue);

        // Language-based text setup
        tvQuizRankTitle.setText(isBn ? "কুইজ র‍্যাংক তালিকা" : "Quiz Rank List");
        tvQuizRankSubtitle.setText(isBn ? "কুইজের অর্জিত পয়েন্ট অনুযায়ী" : "Based on earned quiz points");
        tvAllCompetitorsHeader.setText(isBn ? "সকল প্রতিযোগীগণ" : "All Competitors");
        tvMyRankLabel.setText(isBn ? "আপনার বর্তমান র‍্যাংক" : "Your Current Rank");
        tvQuizRankEmpty.setText(isBn ? "কোনো প্রতিযোগী পাওয়া যায়নি" : "No competitors found");

        // Touch Spring Animations
        TouchAnimationUtil.attachTouchSpring(btnRefreshQuizRank);
        TouchAnimationUtil.attachTouchSpring(btnCloseQuizRank);
        TouchAnimationUtil.attachTouchSpring(btnMyPointsPill);

        // Setup RecyclerView
        QuizRankAdapter adapter = new QuizRankAdapter(activity);
        rvQuizRankList.setLayoutManager(new LinearLayoutManager(activity));
        rvQuizRankList.setAdapter(adapter);

        // Setup User Avatar Initial
        AppDatabase.databaseWriteExecutor.execute(() -> {
            String initial = "r";
            String userId = "usr_current";
            try {
                UserProfileEntity profile = AppDatabase.getInstance(activity).userProfileDao().getActiveProfileSync();
                if (profile != null) {
                    if (profile.getFullName() != null && !profile.getFullName().trim().isEmpty()) {
                        initial = profile.getFullName().trim().substring(0, 1).toLowerCase();
                    }
                    if (profile.getUserId() != null) {
                        userId = profile.getUserId();
                    }
                }
            } catch (Exception ignored) {}

            final String finalInitial = initial;
            final String finalUserId = userId;
            activity.runOnUiThread(() -> {
                tvCurrentUserAvatar.setText(finalInitial);
                adapter.setCurrentUserId(finalUserId);
            });
        });

        // Close action
        btnCloseQuizRank.setOnClickListener(v -> dialog.dismiss());

        // Refresh action
        Runnable loadDataRunnable = () -> {
            pbQuizRankLoading.setVisibility(View.VISIBLE);
            QuizRankRepository.getInstance().getQuizRankings(activity, (users, myRank, myQuizPoints) -> {
                pbQuizRankLoading.setVisibility(View.GONE);

                if (users != null && !users.isEmpty()) {
                    adapter.setUsers(users);
                    tvQuizRankEmpty.setVisibility(View.GONE);
                } else {
                    tvQuizRankEmpty.setVisibility(View.VISIBLE);
                }

                // Update bottom bar
                String rankStr = isBn ? BengaliNumberUtil.toBengali(String.valueOf(myRank)) : String.valueOf(myRank);
                tvMyRankValue.setText((isBn ? "অবস্থান: #" : "Rank: #") + rankStr);

                String ptsStr = isBn ? BengaliNumberUtil.toBengali(String.valueOf(myQuizPoints)) : String.valueOf(myQuizPoints);
                tvMyPointsValue.setText(ptsStr + (isBn ? " পয়েন্ট" : " Points"));
            });
        };

        btnRefreshQuizRank.setOnClickListener(v -> loadDataRunnable.run());

        // Initial Load
        loadDataRunnable.run();

        dialog.show();
    }
}
