package com.devflux.deenone.features.battle;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.local.entity.BattleHistoryEntity;
import com.devflux.deenone.features.battle.adapter.BattleHistoryAdapter;
import com.devflux.deenone.features.battle.data.local.BattleSessionStorage;

import java.util.List;

import com.devflux.deenone.core.ui.FullScreenPageDialog;

public class BattleMatchHistoryDialog extends FullScreenPageDialog {

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private BattleHistoryAdapter adapter;
    private RecyclerView rvHistoryList;
    private LinearLayout layoutEmpty;
    private TextView tvStatTotalMatches;
    private TextView tvStatTotalWins;
    private TextView tvStatTotalXp;

    public static void show(Context context, LifecycleOwner lifecycleOwner) {
        if (context == null) return;
        BattleMatchHistoryDialog dialog = new BattleMatchHistoryDialog(context, lifecycleOwner);
        dialog.show();
    }

    public BattleMatchHistoryDialog(@NonNull Context context, LifecycleOwner lifecycleOwner) {
        super(context);
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_battle_match_history);

        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        initViews();
        loadHistoryData();
    }

    private void initViews() {
        boolean isBn = LocaleManager.isBengali(context);

        FrameLayout btnClose = findViewById(R.id.btnCloseBattleHistory);
        if (btnClose != null) {
            TouchAnimationUtil.attachTouchSpring(btnClose);
            btnClose.setOnClickListener(v -> dismiss());
        }

        TextView tvTitle = findViewById(R.id.tvBattleHistoryTitle);
        TextView tvSubtitle = findViewById(R.id.tvBattleHistorySubtitle);
        TextView tvMatchesLabel = findViewById(R.id.tvStatTotalMatchesLabel);
        TextView tvWinsLabel = findViewById(R.id.tvStatTotalWinsLabel);
        TextView tvXpLabel = findViewById(R.id.tvStatTotalXpLabel);
        TextView tvEmptyTitle = findViewById(R.id.tvEmptyBattleHistoryTitle);
        TextView tvEmptySubtitle = findViewById(R.id.tvEmptyBattleHistorySubtitle);

        if (tvTitle != null) tvTitle.setText(isBn ? "ব্যাটেল ম্যাচ হিস্ট্রি" : "Battle Match History");
        if (tvSubtitle != null) tvSubtitle.setText(isBn ? "নলেজ ব্যাটেল সমাপ্ত ম্যাচ ও ফলাফল" : "Knowledge Battle completed matches & results");
        if (tvMatchesLabel != null) tvMatchesLabel.setText(isBn ? "মোট ম্যাচ" : "Total Matches");
        if (tvWinsLabel != null) tvWinsLabel.setText(isBn ? "বিজয়" : "Victories");
        if (tvXpLabel != null) tvXpLabel.setText(isBn ? "অর্জিত XP" : "Earned XP");
        if (tvEmptyTitle != null) tvEmptyTitle.setText(isBn ? "এখনও কোনো ম্যাচ খেলা হয়নি" : "No matches played yet");
        if (tvEmptySubtitle != null) tvEmptySubtitle.setText(isBn ? "নতুন ব্যাটেল শুরু করুন এবং বন্ধুদের সাথে খেলুন" : "Start a new battle and play with friends");

        tvStatTotalMatches = findViewById(R.id.tvStatTotalMatches);
        tvStatTotalWins = findViewById(R.id.tvStatTotalWins);
        tvStatTotalXp = findViewById(R.id.tvStatTotalXp);
        layoutEmpty = findViewById(R.id.layoutBattleHistoryEmpty);
        rvHistoryList = findViewById(R.id.rvBattleHistoryList);

        adapter = new BattleHistoryAdapter(context);
        if (rvHistoryList != null) {
            rvHistoryList.setLayoutManager(new LinearLayoutManager(context));
            rvHistoryList.setAdapter(adapter);
        }
    }

    private void loadHistoryData() {
        boolean isBn = LocaleManager.isBengali(context);
        BattleSessionStorage storage = BattleSessionStorage.getInstance(context);

        if (lifecycleOwner != null) {
            storage.getAllBattleHistoryLive().observe(lifecycleOwner, historyList -> {
                updateUI(historyList, isBn);
            });
        } else {
            // Fallback background sync read
            new Thread(() -> {
                List<BattleHistoryEntity> historyList = com.devflux.deenone.data.local.AppDatabase.getInstance(context)
                        .battleHistoryDao().getRecentBattleHistorySync();
                if (rvHistoryList != null) {
                    rvHistoryList.post(() -> updateUI(historyList, isBn));
                }
            }).start();
        }
    }

    private void updateUI(List<BattleHistoryEntity> list, boolean isBn) {
        if (list == null || list.isEmpty()) {
            if (layoutEmpty != null) layoutEmpty.setVisibility(View.VISIBLE);
            if (rvHistoryList != null) rvHistoryList.setVisibility(View.GONE);
            if (tvStatTotalMatches != null) tvStatTotalMatches.setText(isBn ? "০" : "0");
            if (tvStatTotalWins != null) tvStatTotalWins.setText(isBn ? "০" : "0");
            if (tvStatTotalXp != null) tvStatTotalXp.setText(isBn ? "০" : "0");
            return;
        }

        if (layoutEmpty != null) layoutEmpty.setVisibility(View.GONE);
        if (rvHistoryList != null) rvHistoryList.setVisibility(View.VISIBLE);

        int totalMatches = list.size();
        int totalWins = 0;
        int totalXp = 0;

        for (BattleHistoryEntity item : list) {
            if (item.isWinner() || item.getRank() == 1) {
                totalWins++;
            }
            totalXp += item.getDeenXpEarned();
        }

        if (tvStatTotalMatches != null) {
            tvStatTotalMatches.setText(isBn ? BengaliNumberUtil.toBengali(totalMatches) : String.valueOf(totalMatches));
        }
        if (tvStatTotalWins != null) {
            tvStatTotalWins.setText(isBn ? BengaliNumberUtil.toBengali(totalWins) : String.valueOf(totalWins));
        }
        if (tvStatTotalXp != null) {
            tvStatTotalXp.setText(isBn ? ("+" + BengaliNumberUtil.toBengali(totalXp)) : ("+" + totalXp));
        }

        if (adapter != null) {
            adapter.submitList(list);
        }
    }
}
