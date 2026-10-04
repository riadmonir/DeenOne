package com.devflux.deenone.features.quiz.dialog;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quiz.QuizItem;
import com.devflux.deenone.core.quiz.QuizManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.features.quiz.model.QuizPlayQuestion;
import com.devflux.deenone.features.quiz.repository.QuizPlayRepository;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

/**
 * Islamic Quiz Play Page Dialog.
 * 100% Verbatim UI matching user specifications and screenshots.
 * Pure dual-language mode (Bengali / English), 60 FPS smooth execution,
 * 10-minute countdown timer, step counter, option selection, and real-time backend sync.
 */
public class QuizPlayPageDialog {

    private static final long TOTAL_QUIZ_DURATION_MS = 10 * 60 * 1000L; // 10 minutes

    private final Activity activity;
    private final FullScreenPageDialog dialog;
    private final View rootView;

    private final TextView tvQuizTimer;
    private final ProgressBar pbQuizProgress;
    private final TextView tvQuizCounter;

    private final TextView tvQuizQuestionText;

    private final LinearLayout[] layoutOptions = new LinearLayout[4];
    private final FrameLayout[] flOptionNumbers = new FrameLayout[4];
    private final TextView[] tvOptionNumbers = new TextView[4];
    private final TextView[] tvOptionTexts = new TextView[4];
    private final ImageView[] ivOptionChecks = new ImageView[4];

    private final LinearLayout btnPrevQuestion;
    private final TextView tvPrevBtnText;
    private final LinearLayout btnNextQuestion;
    private final TextView tvNextBtnText;

    private final List<QuizPlayQuestion> questions = new ArrayList<>();
    private final int[] selectedOptions = new int[]{-1, -1, -1, -1, -1};
    private int currentIndex = 0;

    private CountDownTimer countDownTimer;
    private boolean isFinished = false;

    private final String categoryId;
    private final String categoryTitle;

    public static void show(@NonNull Activity activity) {
        show(activity, "iman", "ঈমান");
    }

    public static void show(@NonNull Activity activity, @Nullable String categoryId, @Nullable String categoryTitle) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        new QuizPlayPageDialog(activity, categoryId, categoryTitle).show();
    }

    private QuizPlayPageDialog(@NonNull Activity activity, @Nullable String categoryId, @Nullable String categoryTitle) {
        this.activity = activity;
        this.categoryId = categoryId != null ? categoryId : "iman";
        this.categoryTitle = categoryTitle != null ? categoryTitle : "ঈমান";
        this.dialog = new FullScreenPageDialog(activity);
        this.rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_quiz_play, null);
        this.dialog.setContentView(rootView);

        // Bind Top Header Views
        tvQuizTimer = rootView.findViewById(R.id.tvQuizTimer);
        pbQuizProgress = rootView.findViewById(R.id.pbQuizProgress);
        tvQuizCounter = rootView.findViewById(R.id.tvQuizCounter);

        // Bind Question Card Views
        tvQuizQuestionText = rootView.findViewById(R.id.tvQuizQuestionText);

        // Bind 4 Option Rows
        layoutOptions[0] = rootView.findViewById(R.id.layoutQuizOption0);
        layoutOptions[1] = rootView.findViewById(R.id.layoutQuizOption1);
        layoutOptions[2] = rootView.findViewById(R.id.layoutQuizOption2);
        layoutOptions[3] = rootView.findViewById(R.id.layoutQuizOption3);

        flOptionNumbers[0] = rootView.findViewById(R.id.flQuizOptionNumber0);
        flOptionNumbers[1] = rootView.findViewById(R.id.flQuizOptionNumber1);
        flOptionNumbers[2] = rootView.findViewById(R.id.flQuizOptionNumber2);
        flOptionNumbers[3] = rootView.findViewById(R.id.flQuizOptionNumber3);

        tvOptionNumbers[0] = rootView.findViewById(R.id.tvQuizOptionNumber0);
        tvOptionNumbers[1] = rootView.findViewById(R.id.tvQuizOptionNumber1);
        tvOptionNumbers[2] = rootView.findViewById(R.id.tvQuizOptionNumber2);
        tvOptionNumbers[3] = rootView.findViewById(R.id.tvQuizOptionNumber3);

        tvOptionTexts[0] = rootView.findViewById(R.id.tvQuizOptionText0);
        tvOptionTexts[1] = rootView.findViewById(R.id.tvQuizOptionText1);
        tvOptionTexts[2] = rootView.findViewById(R.id.tvQuizOptionText2);
        tvOptionTexts[3] = rootView.findViewById(R.id.tvQuizOptionText3);

        ivOptionChecks[0] = rootView.findViewById(R.id.ivQuizOptionCheck0);
        ivOptionChecks[1] = rootView.findViewById(R.id.ivQuizOptionCheck1);
        ivOptionChecks[2] = rootView.findViewById(R.id.ivQuizOptionCheck2);
        ivOptionChecks[3] = rootView.findViewById(R.id.ivQuizOptionCheck3);

        // Bind Bottom Navigation Buttons
        btnPrevQuestion = rootView.findViewById(R.id.btnPrevQuizQuestion);
        tvPrevBtnText = rootView.findViewById(R.id.tvPrevQuizBtnText);
        btnNextQuestion = rootView.findViewById(R.id.btnNextQuizQuestion);
        tvNextBtnText = rootView.findViewById(R.id.tvNextQuizBtnText);

        // Rule 7: Touch animation strictly and exclusively on Action Buttons
        TouchAnimationUtil.attachTouchSpring(btnPrevQuestion);
        TouchAnimationUtil.attachTouchSpring(btnNextQuestion);

        setupOptionClickListeners();
        setupNavigationButtons();

        dialog.setOnDismissListener(d -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        });
    }

    private void show() {
        dialog.show();
        updateTimerDisplay(TOTAL_QUIZ_DURATION_MS);
        loadQuestions();
    }

    private void start10MinuteTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(TOTAL_QUIZ_DURATION_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerDisplay(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                updateTimerDisplay(0);
                if (!isFinished) {
                    finishQuiz();
                }
            }
        };
        countDownTimer.start();
    }

    private void updateTimerDisplay(long millis) {
        boolean isBn = LocaleManager.isBengali(activity);
        long totalSeconds = millis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (isBn) {
            String minStr = BengaliNumberUtil.toBengali((int) minutes);
            String secStr = (seconds < 10 ? "০" : "") + BengaliNumberUtil.toBengali((int) seconds);
            tvQuizTimer.setText(minStr + ":" + secStr);
        } else {
            String timeStr = String.format(Locale.US, "%d:%02d", minutes, seconds);
            tvQuizTimer.setText(timeStr);
        }
    }

    private void loadQuestions() {
        boolean isBn = LocaleManager.isBengali(activity);
        tvQuizQuestionText.setText(isBn ? "প্রশ্নাবলী প্রস্তুত হচ্ছে..." : "Preparing questions...");
        for (int i = 0; i < 4; i++) {
            layoutOptions[i].setVisibility(View.GONE);
        }

        QuizPlayRepository.getQuestions(activity, categoryId, 5, loadedQuestions -> {
            if (activity.isFinishing() || activity.isDestroyed()) return;
            questions.clear();
            if (loadedQuestions != null && !loadedQuestions.isEmpty()) {
                questions.addAll(loadedQuestions);
                start10MinuteTimer();
            } else {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
            renderCurrentQuestion();
        });
    }

    private void setupOptionClickListeners() {
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            // Rule 7: Strict zero touch bounce on Option CardViews (direct state change)
            layoutOptions[i].setOnClickListener(v -> {
                selectedOptions[currentIndex] = optionIndex;
                updateOptionsUI();
            });
        }
    }

    private void setupNavigationButtons() {
        btnPrevQuestion.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                renderCurrentQuestion();
            }
        });

        btnNextQuestion.setOnClickListener(v -> {
            if (currentIndex < questions.size() - 1 && currentIndex < 4) {
                currentIndex++;
                renderCurrentQuestion();
            } else {
                // Last question (৫/৫): Finish Quiz
                finishQuiz();
            }
        });
    }

    private void renderCurrentQuestion() {
        boolean isBn = LocaleManager.isBengali(activity);

        if (questions.isEmpty()) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            pbQuizProgress.setMax(1);
            pbQuizProgress.setProgress(1);
            tvQuizCounter.setText(isBn ? "০/০" : "0/0");
            tvQuizQuestionText.setText(isBn 
                    ? "মাশাআল্লাহ! আপনি এই বিষয়ের সকল প্রশ্ন ইতিমধ্যে সম্পন্ন করেছেন। শীঘ্রই নতুন প্রশ্ন যোগ করা হবে।" 
                    : "MashaAllah! You have already completed all available questions in this category. New questions will be added soon.");
            for (int i = 0; i < 4; i++) {
                layoutOptions[i].setVisibility(View.GONE);
            }
            btnPrevQuestion.setVisibility(View.GONE);
            btnNextQuestion.setBackgroundResource(R.drawable.bg_quiz_btn_finish);
            tvNextBtnText.setText(isBn ? "ফিরে যান" : "Go Back");
            btnNextQuestion.setOnClickListener(v -> dialog.dismiss());
            return;
        }

        if (currentIndex < 0 || currentIndex >= questions.size()) {
            return;
        }

        QuizPlayQuestion currentQ = questions.get(currentIndex);

        // Update Top Step Counter & Progress Bar
        int currentStep = currentIndex + 1;
        int totalSteps = Math.min(5, Math.max(1, questions.size()));

        pbQuizProgress.setMax(totalSteps);
        pbQuizProgress.setProgress(currentStep);

        if (isBn) {
            tvQuizCounter.setText(BengaliNumberUtil.toBengali(currentStep) + "/" + BengaliNumberUtil.toBengali(totalSteps));
        } else {
            tvQuizCounter.setText(currentStep + "/" + totalSteps);
        }

        // Update Question Text
        tvQuizQuestionText.setText(isBn ? currentQ.getQuestionBn() : currentQ.getQuestionEn());

        // Update Option Texts
        List<String> options = isBn ? currentQ.getOptionsBn() : currentQ.getOptionsEn();
        for (int i = 0; i < 4; i++) {
            if (i < options.size()) {
                layoutOptions[i].setVisibility(View.VISIBLE);
                tvOptionTexts[i].setText(options.get(i));
                if (isBn) {
                    tvOptionNumbers[i].setText(BengaliNumberUtil.toBengali(i + 1));
                } else {
                    tvOptionNumbers[i].setText(String.valueOf(i + 1));
                }
            } else {
                layoutOptions[i].setVisibility(View.GONE);
            }
        }

        updateOptionsUI();
        updateNavigationButtonsUI(isBn);
    }

    private void updateOptionsUI() {
        int selectedIndex = selectedOptions[currentIndex];

        for (int i = 0; i < 4; i++) {
            boolean isSelected = (selectedIndex == i);

            if (isSelected) {
                // Option Card: Bright mint green border
                layoutOptions[i].setBackgroundResource(R.drawable.bg_quiz_option_selected);
                // Number Pill: Solid mint green fill with dark text
                flOptionNumbers[i].setBackgroundResource(R.drawable.bg_quiz_number_selected);
                tvOptionNumbers[i].setTextColor(ContextCompat.getColor(activity, R.color.bg_main));
                // Right Checkmark Icon: Visible
                ivOptionChecks[i].setVisibility(View.VISIBLE);
            } else {
                // Option Card: Default dark background
                layoutOptions[i].setBackgroundResource(R.drawable.bg_quiz_option_normal);
                // Number Pill: Dark background with mint green text
                flOptionNumbers[i].setBackgroundResource(R.drawable.bg_quiz_number_normal);
                tvOptionNumbers[i].setTextColor(ContextCompat.getColor(activity, R.color.accent_mint));
                // Right Checkmark Icon: Gone
                ivOptionChecks[i].setVisibility(View.GONE);
            }
        }
    }

    private void updateNavigationButtonsUI(boolean isBn) {
        // Previous Button setup
        if (currentIndex == 0) {
            btnPrevQuestion.setAlpha(0.35f);
            btnPrevQuestion.setEnabled(false);
        } else {
            btnPrevQuestion.setAlpha(1.0f);
            btnPrevQuestion.setEnabled(true);
        }
        tvPrevBtnText.setText(isBn ? "আগেরটি" : "Previous");

        // Next / Finish Quiz Button setup
        if (currentIndex == 4 || currentIndex == questions.size() - 1) {
            // 5th Question: Prominent Vibrant Orange "কুইজ শেষ করুন" (Finish Quiz)
            btnNextQuestion.setBackgroundResource(R.drawable.bg_quiz_btn_finish);
            tvNextBtnText.setText(isBn ? "কুইজ শেষ করুন" : "Finish Quiz");
            tvNextBtnText.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        } else {
            // Questions 1 to 4: "পরেরটি" (Next)
            btnNextQuestion.setBackgroundResource(R.drawable.bg_quiz_btn_next);
            tvNextBtnText.setText(isBn ? "পরেরটি" : "Next");
            tvNextBtnText.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        }
    }

    private void finishQuiz() {
        if (isFinished) return;
        isFinished = true;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Calculate score: +10 points per correct answer, 0 for wrong
        int totalQuestions = questions.size();
        int correctCount = 0;

        List<com.devflux.deenone.features.quiz.model.QuizSessionResult.QuestionReviewItem> reviewItems = new ArrayList<>();
        for (int i = 0; i < totalQuestions; i++) {
            QuizPlayQuestion q = questions.get(i);
            int chosenOption = selectedOptions[i];
            boolean isCorrect = (chosenOption >= 0 && chosenOption == q.getCorrectOptionIndex());
            if (isCorrect) {
                correctCount++;
            }

            String correctBn = q.getOptionsBn().size() > q.getCorrectOptionIndex() ? q.getOptionsBn().get(q.getCorrectOptionIndex()) : "";
            String correctEn = q.getOptionsEn().size() > q.getCorrectOptionIndex() ? q.getOptionsEn().get(q.getCorrectOptionIndex()) : "";
            String userBn = (chosenOption >= 0 && chosenOption < q.getOptionsBn().size()) ? q.getOptionsBn().get(chosenOption) : "";
            String userEn = (chosenOption >= 0 && chosenOption < q.getOptionsEn().size()) ? q.getOptionsEn().get(chosenOption) : "";

            reviewItems.add(new com.devflux.deenone.features.quiz.model.QuizSessionResult.QuestionReviewItem(
                    q.getQuestionBn(),
                    q.getQuestionEn(),
                    correctBn,
                    correctEn,
                    userBn,
                    userEn,
                    isCorrect,
                    q.getExplanationBn(),
                    q.getExplanationEn()
            ));

            // Record each quiz item answer locally
            QuizItem legacyItem = new QuizItem(
                    q.getId(),
                    q.getQuestionBn(),
                    q.getOptionsBn().toArray(new String[0]),
                    q.getCorrectOptionIndex(),
                    categoryTitle,
                    "general",
                    q.getReferenceBn(),
                    q.getExplanationBn()
            );
            QuizManager.getInstance().recordQuizAnswer(activity, legacyItem, isCorrect);
        }

        // Extract question IDs and permanently mark as seen (Anti-Repetition Rule)
        List<String> playedQuestionIds = new ArrayList<>();
        for (QuizPlayQuestion q : questions) {
            if (q.getId() != null && !q.getId().trim().isEmpty()) {
                playedQuestionIds.add(q.getId().trim());
            }
        }
        QuizPlayRepository.markQuestionsAsSeen(activity, categoryId, playedQuestionIds);

        int pointsEarned = correctCount * 10;
        if (pointsEarned > 0) {
            QuizManager.getInstance().addDeenPoints(activity, pointsEarned);
        }

        // Save today's session result locally for this category
        com.devflux.deenone.features.quiz.model.QuizSessionResult sessionResult =
                new com.devflux.deenone.features.quiz.model.QuizSessionResult(
                        categoryId,
                        categoryTitle,
                        categoryTitle,
                        correctCount,
                        totalQuestions,
                        pointsEarned,
                        System.currentTimeMillis(),
                        reviewItems
                );
        com.devflux.deenone.features.quiz.repository.QuizResultStorage.saveTodaySessionResult(activity, sessionResult);

        // Sync with PHP Backend & MySQL database asynchronously
        syncQuizResultToBackend(correctCount, totalQuestions, pointsEarned);

        // Dismiss the play dialog and show the Result screen
        dialog.dismiss();
        QuizResultPageDialog.show(activity, sessionResult);
    }

    private void syncQuizResultToBackend(int correctCount, int totalQuestions, int pointsEarned) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String userId = "usr_current";
                try {
                    UserProfileEntity profile = AppDatabase.getInstance(activity).userProfileDao().getActiveProfileSync();
                    if (profile != null && profile.getUserId() != null && !profile.getUserId().trim().isEmpty()) {
                        userId = profile.getUserId();
                    }
                } catch (Exception ignored) {}

                String baseUrl = BackendConfigManager.getPhpApiBaseUrl(activity);
                String fullUrl = baseUrl + "user_sync.php";

                JSONObject payload = new JSONObject();
                payload.put("user_id", userId);
                payload.put("action", "record_quiz_result");
                payload.put("category_id", categoryId);
                payload.put("correct_count", correctCount);
                payload.put("total_questions", totalQuestions);
                payload.put("points_earned", pointsEarned);
                payload.put("completed_at", System.currentTimeMillis());

                byte[] postBytes = payload.toString().getBytes(StandardCharsets.UTF_8);

                URL url = new URL(fullUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setDoOutput(true);
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);

                OutputStream os = conn.getOutputStream();
                os.write(postBytes);
                os.flush();
                os.close();

                conn.getResponseCode();
                conn.disconnect();
            } catch (Exception ignored) {}
        });
    }
}
