package com.devflux.deenone.features.quiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.features.battle.model.BattleCategory;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Vertical Category Adapter for Islamic Quiz Hub.
 * 100% Verbatim UI matching user specification & screenshot with pure dual-language mode.
 */
public class QuizCategoryVerticalAdapter extends RecyclerView.Adapter<QuizCategoryVerticalAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategorySelected(BattleCategory category);
    }

    private final List<BattleCategory> categories = new ArrayList<>();
    private final OnCategoryClickListener listener;

    public QuizCategoryVerticalAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<BattleCategory> newCategories) {
        this.categories.clear();
        if (newCategories != null) {
            this.categories.addAll(newCategories);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_category_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoryTag;
        private final TextView tvCategoryTitle;
        private final TextView tvCategoryDescription;
        private final TextView tvCategoryQuestionCount;
        private final TextView tvCategoryDuration;
        private final TextView tvCategoryPoints;
        private final LinearLayout btnStartCategoryQuiz;
        private final TextView tvBtnStartText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryTag = itemView.findViewById(R.id.tvCategoryTag);
            tvCategoryTitle = itemView.findViewById(R.id.tvCategoryTitle);
            tvCategoryDescription = itemView.findViewById(R.id.tvCategoryDescription);
            tvCategoryQuestionCount = itemView.findViewById(R.id.tvCategoryQuestionCount);
            tvCategoryDuration = itemView.findViewById(R.id.tvCategoryDuration);
            tvCategoryPoints = itemView.findViewById(R.id.tvCategoryPoints);
            btnStartCategoryQuiz = itemView.findViewById(R.id.btnStartCategoryQuiz);
            tvBtnStartText = itemView.findViewById(R.id.tvBtnStartText);

            // Rule 7: Touch animation strictly ONLY on button (zero animation on card)
            TouchAnimationUtil.attachTouchSpring(btnStartCategoryQuiz);
        }

        void bind(BattleCategory category) {
            Context ctx = itemView.getContext();
            boolean isBn = LocaleManager.isBengali(ctx);

            tvCategoryTag.setText(isBn ? category.getTagBn() : category.getTagEn());
            tvCategoryTitle.setText(isBn ? category.getTitleBn() : category.getTitleEn());
            tvCategoryDescription.setText(isBn ? category.getDescriptionBn() : category.getDescriptionEn());

            tvCategoryQuestionCount.setText(isBn
                    ? (BengaliNumberUtil.toBengali(category.getTotalQuestions()) + " টি প্রশ্ন")
                    : (category.getTotalQuestions() + " Questions"));

            tvCategoryDuration.setText(isBn
                    ? (BengaliNumberUtil.toBengali(category.getDurationMinutes()) + " মিনিট")
                    : (category.getDurationMinutes() + " Mins"));

            tvCategoryPoints.setText(isBn
                    ? (BengaliNumberUtil.toBengali(category.getPointsPerQuestion()) + " পয়েন্ট প্রতি প্রশ্ন")
                    : (category.getPointsPerQuestion() + " Points per question"));

            boolean isCompletedToday = com.devflux.deenone.features.quiz.repository.QuizResultStorage.isCategoryCompletedToday(ctx, category.getId());
            if (isCompletedToday) {
                tvBtnStartText.setText(isBn ? "ফলাফল দেখুন  →" : "View Results  →");
            } else {
                tvBtnStartText.setText(isBn ? "এখনই শুরু করুন  →" : "Start Now  →");
            }

            btnStartCategoryQuiz.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategorySelected(category);
                }
            });

            // Card click also triggers action smoothly without spring bounce
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategorySelected(category);
                }
            });
        }
    }
}
