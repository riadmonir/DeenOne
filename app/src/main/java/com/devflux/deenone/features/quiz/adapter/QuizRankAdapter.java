package com.devflux.deenone.features.quiz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.features.quiz.model.QuizRankUser;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class QuizRankAdapter extends RecyclerView.Adapter<QuizRankAdapter.QuizRankViewHolder> {

    private final Context context;
    private final List<QuizRankUser> users = new ArrayList<>();
    private final boolean isBn;
    private String currentUserId = "";

    public QuizRankAdapter(Context context) {
        this.context = context;
        this.isBn = LocaleManager.isBengali(context);
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId != null ? currentUserId : "";
    }

    public void setUsers(List<QuizRankUser> newUsers) {
        users.clear();
        if (newUsers != null) {
            users.addAll(newUsers);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizRankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_rank_row, parent, false);
        return new QuizRankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizRankViewHolder holder, int position) {
        QuizRankUser user = users.get(position);
        holder.bind(user, position + 1, isBn, currentUserId);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class QuizRankViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivRankMedal;
        private final TextView tvRankNumber;
        private final FrameLayout layoutAvatarContainer;
        private final TextView tvAvatarInitials;
        private final TextView tvUserName;
        private final TextView tvUserTag;
        private final TextView tvUserPoints;
        private final TextView tvPointsLabel;

        public QuizRankViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRankMedal = itemView.findViewById(R.id.ivRankMedal);
            tvRankNumber = itemView.findViewById(R.id.tvRankNumber);
            layoutAvatarContainer = itemView.findViewById(R.id.layoutAvatarContainer);
            tvAvatarInitials = itemView.findViewById(R.id.tvAvatarInitials);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserTag = itemView.findViewById(R.id.tvUserTag);
            tvUserPoints = itemView.findViewById(R.id.tvUserPoints);
            tvPointsLabel = itemView.findViewById(R.id.tvPointsLabel);

        }

        public void bind(QuizRankUser user, int fallbackRank, boolean isBn, String currentUserId) {
            int rank = user.getRank() > 0 ? user.getRank() : fallbackRank;

            // 1. Rank presentation
            if (rank == 1) {
                ivRankMedal.setVisibility(View.VISIBLE);
                ivRankMedal.setImageResource(R.drawable.ic_rank_medal_1);
                tvRankNumber.setVisibility(View.GONE);
            } else if (rank == 2) {
                ivRankMedal.setVisibility(View.VISIBLE);
                ivRankMedal.setImageResource(R.drawable.ic_rank_medal_2);
                tvRankNumber.setVisibility(View.GONE);
            } else if (rank == 3) {
                ivRankMedal.setVisibility(View.VISIBLE);
                ivRankMedal.setImageResource(R.drawable.ic_rank_medal_3);
                tvRankNumber.setVisibility(View.GONE);
            } else {
                ivRankMedal.setVisibility(View.GONE);
                tvRankNumber.setVisibility(View.VISIBLE);
                tvRankNumber.setText(isBn ? BengaliNumberUtil.toBengali(String.valueOf(rank)) : String.valueOf(rank));
            }

            // 2. Avatar container & Initials
            String initials = user.getInitials();
            tvAvatarInitials.setText(initials);

            // Special highlighted avatar color styling (Matching screenshot rank 3 with dark teal)
            if (rank == 3 || (currentUserId != null && !currentUserId.isEmpty() && currentUserId.equalsIgnoreCase(user.getUserId()))) {
                layoutAvatarContainer.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#0F766E")));
                tvAvatarInitials.setTextColor(Color.WHITE);
            } else {
                layoutAvatarContainer.setBackgroundTintList(null);
                tvAvatarInitials.setTextColor(itemView.getContext().getResources().getColor(R.color.text_primary));
            }

            // 3. User Name & District / Tag
            tvUserName.setText(user.getName());
            tvUserTag.setText(user.getDistrict());

            // 4. Points & Label
            int pts = user.getQuizPoints();
            tvUserPoints.setText(isBn ? BengaliNumberUtil.toBengali(String.valueOf(pts)) : String.valueOf(pts));
            tvPointsLabel.setText(isBn ? "পয়েন্ট" : "Points");
        }
    }
}
