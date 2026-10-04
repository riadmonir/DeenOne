package com.devflux.deenone.features.quiz.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.features.quiz.model.QuizSessionResult;
import com.devflux.deenone.features.quiz.repository.QuizResultStorage;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class QuizResultPageDialog {

    public static void show(@NonNull Activity activity, @NonNull QuizSessionResult result) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        new QuizResultPageDialog(activity, result).show();
    }

    public static void showForCategoryIfCompleted(@NonNull Activity activity, @NonNull String categoryId) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        QuizSessionResult cached = QuizResultStorage.getTodaySessionResult(activity, categoryId);
        if (cached != null) {
            new QuizResultPageDialog(activity, cached).show();
        }
    }

    private final Activity activity;
    private final FullScreenPageDialog dialog;
    private final View rootView;
    private final QuizSessionResult result;

    private QuizResultPageDialog(@NonNull Activity activity, @NonNull QuizSessionResult result) {
        this.activity = activity;
        this.result = result;
        this.dialog = new FullScreenPageDialog(activity);
        this.rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_quiz_result, null);
        this.dialog.setContentView(rootView);

        bindViewsAndData();
    }

    private void show() {
        dialog.show();
    }

    private void bindViewsAndData() {
        boolean isBn = LocaleManager.isBengali(activity);

        TextView tvResultMainTitle = rootView.findViewById(R.id.tvResultMainTitle);
        TextView tvResultSubtitle = rootView.findViewById(R.id.tvResultSubtitle);
        TextView tvEarnedPointsLabel = rootView.findViewById(R.id.tvEarnedPointsLabel);
        TextView tvEarnedPointsValue = rootView.findViewById(R.id.tvEarnedPointsValue);
        TextView tvCorrectRatioLabel = rootView.findViewById(R.id.tvCorrectRatioLabel);
        TextView tvCorrectRatioValue = rootView.findViewById(R.id.tvCorrectRatioValue);
        TextView tvAccuracyLabel = rootView.findViewById(R.id.tvAccuracyLabel);
        TextView tvAccuracyPercent = rootView.findViewById(R.id.tvAccuracyPercent);
        ProgressBar pbResultAccuracy = rootView.findViewById(R.id.pbResultAccuracy);
        LinearLayout layoutQuestionReviewsContainer = rootView.findViewById(R.id.layoutQuestionReviewsContainer);
        LinearLayout btnReturnToQuizList = rootView.findViewById(R.id.btnReturnToQuizList);
        TextView tvReturnBtnText = rootView.findViewById(R.id.tvReturnBtnText);

        // Language text bindings
        tvResultMainTitle.setText(isBn ? "মাশাআল্লাহ! সম্পন্ন হয়েছে" : "MashaAllah! Completed");
        tvResultSubtitle.setText(isBn ? "আপনি সফলভাবে দ্বীনি কুইজটি সম্পন্ন করেছেন।" : "You have successfully completed the Islamic quiz.");
        tvEarnedPointsLabel.setText(isBn ? "অর্জিত পয়েন্ট" : "Earned Points");
        tvCorrectRatioLabel.setText(isBn ? "সরাসরি সঠিক" : "Directly Correct");
        tvAccuracyLabel.setText(isBn ? "নির্ভুলতা" : "Accuracy");
        tvReturnBtnText.setText(isBn ? "কুইজ তালিকায় ফিরে যান" : "Return to Quiz List");

        // Values
        if (isBn) {
            tvEarnedPointsValue.setText("+" + BengaliNumberUtil.toBengali(result.getPointsEarned()));
            tvCorrectRatioValue.setText(BengaliNumberUtil.toBengali(result.getCorrectCount()) + "/" + BengaliNumberUtil.toBengali(result.getTotalQuestions()));
            tvAccuracyPercent.setText(BengaliNumberUtil.toBengali(result.getAccuracyPercentage()) + "%");
        } else {
            tvEarnedPointsValue.setText("+" + result.getPointsEarned());
            tvCorrectRatioValue.setText(result.getCorrectCount() + "/" + result.getTotalQuestions());
            tvAccuracyPercent.setText(result.getAccuracyPercentage() + "%");
        }
        pbResultAccuracy.setProgress(result.getAccuracyPercentage());

        // Populate question reviews
        layoutQuestionReviewsContainer.removeAllViews();
        List<QuizSessionResult.QuestionReviewItem> reviews = result.getReviewItems();
        LayoutInflater inflater = LayoutInflater.from(activity);

        for (int i = 0; i < reviews.size(); i++) {
            QuizSessionResult.QuestionReviewItem item = reviews.get(i);
            View itemView = inflater.inflate(R.layout.item_quiz_result_review, layoutQuestionReviewsContainer, false);

            TextView tvReviewNumber = itemView.findViewById(R.id.tvReviewNumber);
            TextView tvReviewQuestion = itemView.findViewById(R.id.tvReviewQuestion);
            TextView tvReviewCorrectAnswer = itemView.findViewById(R.id.tvReviewCorrectAnswer);
            TextView tvReviewUserAnswer = itemView.findViewById(R.id.tvReviewUserAnswer);
            TextView tvReviewExplanation = itemView.findViewById(R.id.tvReviewExplanation);

            tvReviewNumber.setText(isBn ? BengaliNumberUtil.toBengali(i + 1) : String.valueOf(i + 1));
            tvReviewQuestion.setText(isBn ? item.getQuestionBn() : item.getQuestionEn());

            String correctText = isBn ? item.getCorrectAnswerBn() : item.getCorrectAnswerEn();
            tvReviewCorrectAnswer.setText(isBn ? ("সঠিক উত্তর: " + correctText) : ("Correct Answer: " + correctText));

            if (!item.isCorrect()) {
                String userText = isBn ? item.getUserAnswerBn() : item.getUserAnswerEn();
                if (userText == null || userText.trim().isEmpty()) {
                    userText = isBn ? "কোনো উত্তর দেওয়া হয়নি" : "No answer given";
                }
                tvReviewUserAnswer.setText(isBn ? ("আপনার উত্তর: " + userText) : ("Your Answer: " + userText));
                tvReviewUserAnswer.setVisibility(View.VISIBLE);
            } else {
                tvReviewUserAnswer.setVisibility(View.GONE);
            }

            String expText = isBn ? item.getExplanationBn() : item.getExplanationEn();
            if (expText != null && !expText.trim().isEmpty()) {
                tvReviewExplanation.setText(expText);
                itemView.findViewById(R.id.layoutReviewExplanation).setVisibility(View.VISIBLE);
            } else {
                itemView.findViewById(R.id.layoutReviewExplanation).setVisibility(View.GONE);
            }

            layoutQuestionReviewsContainer.addView(itemView);
        }

        // Action button
        TouchAnimationUtil.attachTouchSpring(btnReturnToQuizList);
        btnReturnToQuizList.setOnClickListener(v -> dialog.dismiss());
    }
}
