package com.devflux.deenone.features.quran;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranJourneyManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.features.quran.adapter.QuranJourneyRevisionAdapter;
import com.devflux.deenone.features.quran.adapter.QuranJourneyWordAdapter;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class QuranJourneyPageDialog extends FullScreenPageDialog {

    private static final String TAG = "QuranJourneyPageDialog";

    private final boolean isBn;
    private final QuranJourneyManager.JourneyLesson lesson;

    private int currentStep = 1;
    private boolean quizAnswered = false;
    private boolean quizPointsAwarded = false;
    private boolean amolPointsAwarded = false;

    // Dynamic Streak & Emotional Reaction Banner
    private LinearLayout layoutStreakReactionCard;
    private FrameLayout containerReactionEmoji;
    private TextView tvReactionEmoji;
    private TextView tvReactionTitle;
    private TextView tvReactionSubtitle;
    private LinearLayout layoutReactionStreakPill;
    private TextView tvReactionStreakDays;

    // Header & Top UI
    private FrameLayout btnJourneyBack;
    private LinearLayout layoutLevelBadge;
    private TextView tvLevelBadgeText;
    private LinearLayout layoutNoorPointsBadge;
    private TextView tvNoorPointsBadgeText;
    private FrameLayout btnJourneyProfile;
    private TextView tvCommunityProofText;

    // Progress Bar Segments
    private View segStep1, segStep2, segStep3, segStep4, segStep5;
    private TextView tvStepCounterText;
    private NestedScrollView scrollJourneyContent;
    private ViewFlipper flipperJourneySteps;

    // Step 1: Audio Player & Ayah
    private TextView tvStep1SurahBadge;
    private TextView tvStep1Arabic;
    private TextView tvStep1Translation;
    private TextView tvStep1Note;
    private ProgressBar progressAudioAyah;
    private TextView tvAudioCurrentTime;
    private TextView tvAudioTotalTime;
    private LinearLayout btnAudioRepeat;
    private TextView tvRepeatBadge;
    private FrameLayout btnAudioPlayPause;
    private ImageView ivAudioPlayPauseIcon;
    private LinearLayout btnAudioSpeed;
    private TextView tvSpeedBadge;

    // Step 2: Word by word
    private RecyclerView rvJourneyWords;
    private QuranJourneyWordAdapter wordAdapter;

    // Step 3: Quiz
    private TextView tvStep3RewardBadge;
    private TextView tvQuizAyahBlank;
    private RelativeLayout cardQuizOption1, cardQuizOption2, cardQuizOption3, cardQuizOption4;
    private TextView tvQuizOption1Text, tvQuizOption2Text, tvQuizOption3Text, tvQuizOption4Text;
    private TextView tvQuizOption1Badge, tvQuizOption2Badge, tvQuizOption3Badge, tvQuizOption4Badge;
    private LinearLayout layoutQuizFeedbackBanner;
    private TextView tvQuizFeedbackTitle;
    private TextView tvQuizFeedbackSubtitle;

    // Step 4: Lessons & Revisions
    private TextView tvLessonTitle;
    private TextView tvLessonDescription;
    private LinearLayout layoutAmolActionRow;
    private CheckBox cbAmolDone;
    private TextView tvAmolText;
    private SwitchCompat switchDailyReminder;
    private RecyclerView rvRevisionList;
    private QuranJourneyRevisionAdapter revisionAdapter;
    private LinearLayout layoutRevisionEmptyState;
    private TextView tvRevisionEmptyTitle;
    private TextView tvRevisionEmptySubtitle;

    // Step 5: Completion
    private TextView tvMetricPointsValue;
    private TextView tvMetricStreakValue;
    private TextView tvMetricAmolValue;
    private TextView tvTotalPointsBanner;
    private MaterialButton btnJourneyFinalComplete;

    // Bottom Navigation
    private TextView btnStepPrevious;
    private FrameLayout btnStepNext;
    private TextView tvNextStepText;

    // Audio Playback
    private MediaPlayer ayahMediaPlayer;
    private MediaPlayer revisionMediaPlayer;
    private final Handler audioHandler = new Handler(Looper.getMainLooper());
    private int currentRepeatLoop = 0;
    private int targetRepeatCount = 3;
    private float currentSpeed = 1.0f;

    public QuranJourneyPageDialog(@NonNull Context context) {
        super(context);
        this.isBn = LocaleManager.isBengali(context);
        this.lesson = QuranJourneyManager.getTodayLesson(context);
        this.targetRepeatCount = QuranJourneyManager.getRepeatCount(context);
        this.currentSpeed = QuranJourneyManager.getPlaySpeed(context);
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_quran_journey);

        initViews();
        applyLanguageLocalization();
        setupStep1Audio();
        setupStep2Words();
        setupStep3Quiz();
        setupStep4Action();
        setupStep5Celebration();
        setupNavigation();

        updateStepUi();
        updateTopStats();
    }

    private void initViews() {
        layoutStreakReactionCard = findViewById(R.id.layoutStreakReactionCard);
        containerReactionEmoji = findViewById(R.id.containerReactionEmoji);
        tvReactionEmoji = findViewById(R.id.tvReactionEmoji);
        tvReactionTitle = findViewById(R.id.tvReactionTitle);
        tvReactionSubtitle = findViewById(R.id.tvReactionSubtitle);
        layoutReactionStreakPill = findViewById(R.id.layoutReactionStreakPill);
        tvReactionStreakDays = findViewById(R.id.tvReactionStreakDays);

        btnJourneyBack = findViewById(R.id.btnJourneyBack);
        layoutLevelBadge = findViewById(R.id.layoutLevelBadge);
        tvLevelBadgeText = findViewById(R.id.tvLevelBadgeText);
        layoutNoorPointsBadge = findViewById(R.id.layoutNoorPointsBadge);
        tvNoorPointsBadgeText = findViewById(R.id.tvNoorPointsBadgeText);
        btnJourneyProfile = findViewById(R.id.btnJourneyProfile);
        tvCommunityProofText = findViewById(R.id.tvCommunityProofText);

        segStep1 = findViewById(R.id.segStep1);
        segStep2 = findViewById(R.id.segStep2);
        segStep3 = findViewById(R.id.segStep3);
        segStep4 = findViewById(R.id.segStep4);
        segStep5 = findViewById(R.id.segStep5);
        tvStepCounterText = findViewById(R.id.tvStepCounterText);
        scrollJourneyContent = findViewById(R.id.scrollJourneyContent);
        flipperJourneySteps = findViewById(R.id.flipperJourneySteps);

        // Step 1
        tvStep1SurahBadge = findViewById(R.id.tvStep1SurahBadge);
        tvStep1Arabic = findViewById(R.id.tvStep1Arabic);
        tvStep1Translation = findViewById(R.id.tvStep1Translation);
        tvStep1Note = findViewById(R.id.tvStep1Note);
        progressAudioAyah = findViewById(R.id.progressAudioAyah);
        tvAudioCurrentTime = findViewById(R.id.tvAudioCurrentTime);
        tvAudioTotalTime = findViewById(R.id.tvAudioTotalTime);
        btnAudioRepeat = findViewById(R.id.btnAudioRepeat);
        tvRepeatBadge = findViewById(R.id.tvRepeatBadge);
        btnAudioPlayPause = findViewById(R.id.btnAudioPlayPause);
        ivAudioPlayPauseIcon = findViewById(R.id.ivAudioPlayPauseIcon);
        btnAudioSpeed = findViewById(R.id.btnAudioSpeed);
        tvSpeedBadge = findViewById(R.id.tvSpeedBadge);

        // Step 2
        rvJourneyWords = findViewById(R.id.rvJourneyWords);

        // Step 3
        tvStep3RewardBadge = findViewById(R.id.tvStep3RewardBadge);
        tvQuizAyahBlank = findViewById(R.id.tvQuizAyahBlank);
        cardQuizOption1 = findViewById(R.id.cardQuizOption1);
        cardQuizOption2 = findViewById(R.id.cardQuizOption2);
        cardQuizOption3 = findViewById(R.id.cardQuizOption3);
        cardQuizOption4 = findViewById(R.id.cardQuizOption4);
        tvQuizOption1Text = findViewById(R.id.tvQuizOption1Text);
        tvQuizOption2Text = findViewById(R.id.tvQuizOption2Text);
        tvQuizOption3Text = findViewById(R.id.tvQuizOption3Text);
        tvQuizOption4Text = findViewById(R.id.tvQuizOption4Text);
        tvQuizOption1Badge = findViewById(R.id.tvQuizOption1Badge);
        tvQuizOption2Badge = findViewById(R.id.tvQuizOption2Badge);
        tvQuizOption3Badge = findViewById(R.id.tvQuizOption3Badge);
        tvQuizOption4Badge = findViewById(R.id.tvQuizOption4Badge);
        layoutQuizFeedbackBanner = findViewById(R.id.layoutQuizFeedbackBanner);
        tvQuizFeedbackTitle = findViewById(R.id.tvQuizFeedbackTitle);
        tvQuizFeedbackSubtitle = findViewById(R.id.tvQuizFeedbackSubtitle);

        // Step 4
        tvLessonTitle = findViewById(R.id.tvLessonTitle);
        tvLessonDescription = findViewById(R.id.tvLessonDescription);
        layoutAmolActionRow = findViewById(R.id.layoutAmolActionRow);
        cbAmolDone = findViewById(R.id.cbAmolDone);
        tvAmolText = findViewById(R.id.tvAmolText);
        switchDailyReminder = findViewById(R.id.switchDailyReminder);
        rvRevisionList = findViewById(R.id.rvRevisionList);
        layoutRevisionEmptyState = findViewById(R.id.layoutRevisionEmptyState);
        tvRevisionEmptyTitle = findViewById(R.id.tvRevisionEmptyTitle);
        tvRevisionEmptySubtitle = findViewById(R.id.tvRevisionEmptySubtitle);

        // Step 5
        tvMetricPointsValue = findViewById(R.id.tvMetricPointsValue);
        tvMetricStreakValue = findViewById(R.id.tvMetricStreakValue);
        tvMetricAmolValue = findViewById(R.id.tvMetricAmolValue);
        tvTotalPointsBanner = findViewById(R.id.tvTotalPointsBanner);
        btnJourneyFinalComplete = findViewById(R.id.btnJourneyFinalComplete);

        // Bottom Nav
        btnStepPrevious = findViewById(R.id.btnStepPrevious);
        btnStepNext = findViewById(R.id.btnStepNext);
        tvNextStepText = findViewById(R.id.tvNextStepText);

        // Touch feedback
        TouchAnimationUtil.attachTouchSpring(btnJourneyBack);
        TouchAnimationUtil.attachTouchSpring(layoutLevelBadge);
        TouchAnimationUtil.attachTouchSpring(btnJourneyProfile);
        TouchAnimationUtil.attachTouchSpring(btnAudioRepeat);
        TouchAnimationUtil.attachTouchSpring(btnAudioPlayPause);
        TouchAnimationUtil.attachTouchSpring(btnAudioSpeed);
        TouchAnimationUtil.attachTouchSpring(btnStepPrevious);
        TouchAnimationUtil.attachTouchSpring(btnStepNext);
        TouchAnimationUtil.attachTouchSpring(btnJourneyFinalComplete);

        btnJourneyBack.setOnClickListener(v -> dismiss());

        // Level badge selector
        View.OnClickListener levelClickListener = v -> showLevelPickerDialog();
        layoutLevelBadge.setOnClickListener(levelClickListener);
        btnJourneyProfile.setOnClickListener(levelClickListener);

        tvLevelBadgeText.setText(isBn ? QuranJourneyManager.getCurrentLevel(getContext()) : QuranJourneyManager.getCurrentLevelEnglish(getContext()));
        setupStreakReaction();
    }

    private void applyLanguageLocalization() {
        tvLevelBadgeText.setText(isBn ? QuranJourneyManager.getCurrentLevel(getContext()) : QuranJourneyManager.getCurrentLevelEnglish(getContext()));

        // Step 1
        TextView tvStep1Title = findViewById(R.id.tvStep1Title);
        TextView tvStep1Subtitle = findViewById(R.id.tvStep1Subtitle);
        if (tvStep1Title != null) tvStep1Title.setText(isBn ? "তিলাওয়াত শুনুন ও পুনরাবৃত্তি করুন" : "Listen & Repeat Recitation");
        if (tvStep1Subtitle != null) tvStep1Subtitle.setText(isBn ? "মনোযোগ দিয়ে শুনুন এবং একই সাথে পড়ার চেষ্টা করুন" : "Listen attentively and try reciting along");
        if (tvStep1Note != null) tvStep1Note.setText(isBn ? "অর্থ ও উচ্চারণ লক্ষ্য করুন" : "Observe the meaning and pronunciation");

        // Step 2
        TextView tvStep2Title = findViewById(R.id.tvStep2Title);
        TextView tvStep2Subtitle = findViewById(R.id.tvStep2Subtitle);
        TextView tvStep2AyahBadge = findViewById(R.id.tvStep2AyahBadge);
        TextView tvStep2Hint = findViewById(R.id.tvStep2Hint);
        if (tvStep2Title != null) tvStep2Title.setText(isBn ? "শব্দে শব্দে অর্থ বুঝুন" : "Word by Word Meaning");
        if (tvStep2Subtitle != null) tvStep2Subtitle.setText(isBn ? "প্রতিটি শব্দের অর্থ শিখুন। অর্থ প্রকাশ করতে চোখে ট্যাপ করুন।" : "Learn meaning of each word. Tap eye icon to reveal meaning.");
        if (tvStep2AyahBadge != null) tvStep2AyahBadge.setText(isBn ? lesson.surahRefBengali : lesson.surahRefEnglish);
        if (tvStep2Hint != null) tvStep2Hint.setText(isBn ? "💡 শব্দগুলো ভালো করে মুখস্থ করুন, পরবর্তী ধাপে কুইজে এই শব্দগুলো আসবে!" : "💡 Memorize these words well, they will appear in the next quiz step!");

        // Step 3
        TextView tvStep3Title = findViewById(R.id.tvStep3Title);
        TextView tvStep3Subtitle = findViewById(R.id.tvStep3Subtitle);
        TextView tvQuizQuestionTitle = findViewById(R.id.tvQuizQuestionTitle);
        if (tvStep3Title != null) tvStep3Title.setText(isBn ? "কুইজ চ্যালেঞ্জ" : "Quiz Challenge");
        if (tvStep3RewardBadge != null) tvStep3RewardBadge.setText(isBn ? "✨ +১০ পয়েন্ট" : "✨ +10 Points");
        if (tvStep3Subtitle != null) tvStep3Subtitle.setText(isBn ? "শূন্যস্থান পূরণ করতে সঠিক শব্দটি নির্বাচন করুন" : "Select the correct word to fill in the blank");
        if (tvQuizQuestionTitle != null) tvQuizQuestionTitle.setText(isBn ? "শূন্যস্থানে সঠিক শব্দটি কী হবে?" : "What is the correct word for the blank?");

        // Step 4
        TextView tvStep4Title = findViewById(R.id.tvStep4Title);
        TextView tvStep4Subtitle = findViewById(R.id.tvStep4Subtitle);
        TextView tvAmolTitle = findViewById(R.id.tvAmolTitle);
        TextView tvAmolPointsTag = findViewById(R.id.tvAmolPointsTag);
        TextView tvReminderTitle = findViewById(R.id.tvReminderTitle);
        TextView tvReminderSubtitle = findViewById(R.id.tvReminderSubtitle);
        TextView tvRevisionSectionTitle = findViewById(R.id.tvRevisionSectionTitle);
        TextView tvRevisionSectionSubtitle = findViewById(R.id.tvRevisionSectionSubtitle);
        if (tvStep4Title != null) tvStep4Title.setText(isBn ? "জীবনের শিক্ষা ও আমল" : "Life Lessons & Deeds");
        if (tvStep4Subtitle != null) tvStep4Subtitle.setText(isBn ? "আজকের আয়াত থেকে প্রাপ্ত শিক্ষা নিজের জীবনে প্রয়োগ করুন" : "Apply lessons from today's verse to your life");
        if (tvAmolTitle != null) tvAmolTitle.setText(isBn ? "দৈনিক আমল চেকলিস্ট" : "Daily Action Checklist");
        if (tvAmolPointsTag != null) tvAmolPointsTag.setText(isBn ? "✨ +১০" : "✨ +10");
        if (tvReminderTitle != null) tvReminderTitle.setText(isBn ? "রাত ৯:০০ টায় দৈনিক আমল স্মরণ করিয়ে দিন" : "Remind daily deed at 9:00 PM");
        if (tvReminderSubtitle != null) tvReminderSubtitle.setText(isBn ? "প্রতিদিন একই সময়ে কুরআনের সাথে সম্পর্ক রাখুন" : "Stay connected with the Quran at the same time daily");
        if (tvRevisionSectionTitle != null) tvRevisionSectionTitle.setText(isBn ? "গত ৭ দিনের পুনরাবৃত্তি (পূর্ববর্তী পাঠ)" : "Past 7 Days Revision");
        if (tvRevisionSectionSubtitle != null) tvRevisionSectionSubtitle.setText(isBn ? "স্মৃতিতে আয়াতসমূহ সতেজ রাখতে অডিও শুনুন" : "Listen to audio to keep verses fresh in memory");

        // Step 5
        TextView tvCelebrationHeading = findViewById(R.id.tvCelebrationHeading);
        TextView tvCelebrationSubheading = findViewById(R.id.tvCelebrationSubheading);
        TextView tvMetricPointsLabel = findViewById(R.id.tvMetricPointsLabel);
        TextView tvMetricStreakLabel = findViewById(R.id.tvMetricStreakLabel);
        TextView tvMetricAmolLabel = findViewById(R.id.tvMetricAmolLabel);
        TextView tvMotivationalQuote = findViewById(R.id.tvMotivationalQuote);
        if (tvCelebrationHeading != null) tvCelebrationHeading.setText(isBn ? "মাশাআল্লাহ! আজকের পাঠ সম্পন্ন!" : "MashaAllah! Today's Lesson Complete!");
        if (tvCelebrationSubheading != null) tvCelebrationSubheading.setText(isBn ? "আপনি সাফল্যের সাথে আজকের আয়াত শিক্ষা সম্পন্ন করেছেন। আল্লাহ আপনার ইলম বৃদ্ধি করুন। আমীন।" : "You have successfully completed today's verse lesson. May Allah increase your knowledge. Ameen.");
        if (tvMetricPointsLabel != null) tvMetricPointsLabel.setText(isBn ? "নূর পয়েন্ট" : "Noor Points");
        if (tvMetricStreakLabel != null) tvMetricStreakLabel.setText(isBn ? "ধারাবাহিকতা" : "Streak");
        if (tvMetricAmolLabel != null) tvMetricAmolLabel.setText(isBn ? "আমলের বাগান" : "Deeds Garden");
        if (tvMotivationalQuote != null) tvMotivationalQuote.setText(isBn ? "“কুরআন তিলাওয়াতকারীর প্রতিটি হরফে দশটি নেকি।” — তিরমিযী" : "“Whoever recites a letter from the Book of Allah will have ten rewards.” — Tirmidhi");
        if (btnJourneyFinalComplete != null) btnJourneyFinalComplete.setText(isBn ? "সম্পন্ন করুন" : "Complete");

        // Bottom Nav
        if (btnStepPrevious != null) btnStepPrevious.setText(isBn ? "← পেছনে" : "← Back");
    }

    private void setupStreakReaction() {
        QuranJourneyManager.StreakReaction reaction = QuranJourneyManager.getStreakReaction(getContext());
        if (tvReactionEmoji != null) {
            tvReactionEmoji.setText(reaction.emoji);
        }
        if (containerReactionEmoji != null) {
            containerReactionEmoji.setBackgroundResource(reaction.bgCircleRes);
        }
        if (tvReactionTitle != null) {
            tvReactionTitle.setText(isBn ? reaction.titleBn : reaction.titleEn);
        }
        if (tvReactionSubtitle != null) {
            tvReactionSubtitle.setText(isBn ? reaction.subtitleBn : reaction.subtitleEn);
        }
        if (tvReactionStreakDays != null) {
            String streakStr = (isBn ? BengaliNumberUtil.toBengali(reaction.streakDays) : String.valueOf(reaction.streakDays))
                    + (isBn ? " দিন" : " Days");
            tvReactionStreakDays.setText(streakStr);
        }
    }

    private void updateTopStats() {
        int points = QuranJourneyManager.getNoorPoints(getContext());
        tvNoorPointsBadgeText.setText(isBn ? BengaliNumberUtil.toBengali(points) : String.valueOf(points));

        int learners = QuranJourneyManager.getLearnerCount(getContext());
        updateLearnersCountDisplay(learners);

        QuranJourneyManager.fetchRealTimeLearnersCount(getContext(), count -> {
            if (isShowing()) {
                updateLearnersCountDisplay(count);
            }
        });

        setupStreakReaction();
    }

    private void updateLearnersCountDisplay(int count) {
        if (tvCommunityProofText != null) {
            tvCommunityProofText.setText(isBn
                    ? (BengaliNumberUtil.toBengali(count) + " জন শিক্ষার্থী ইতিমধ্যেই কুরআন যাত্রা শুরু করেছেন!")
                    : (count + " learners have already begun their Quran Journey!"));
        }
    }

    private void showLevelPickerDialog() {
        String[] levelsBn = {"প্রাথমিক (৩ আয়াত/দিন)", "মাধ্যমিক (৫ আয়াত/দিন)", "উচ্চতর (১০ আয়াত/দিন)"};
        String[] levelsEn = {"Beginner (3 Verses/day)", "Intermediate (5 Verses/day)", "Advanced (10 Verses/day)"};
        String[] displayLevels = isBn ? levelsBn : levelsEn;

        new MaterialAlertDialogBuilder(getContext())
                .setTitle(isBn ? "কুরআন যাত্রার স্তর নির্বাচন করুন" : "Select Quran Journey Level")
                .setItems(displayLevels, (d, which) -> {
                    String selected = displayLevels[which];
                    QuranJourneyManager.setCurrentLevel(getContext(), levelsBn[which]);
                    tvLevelBadgeText.setText(selected);
                })
                .show();
    }

    // ================= STEP 1: AUDIO & AYAH =================
    private void setupStep1Audio() {
        tvStep1SurahBadge.setText(isBn ? lesson.surahRefBengali : lesson.surahRefEnglish);
        tvStep1Arabic.setText(lesson.arabicAyah);
        tvStep1Translation.setText(isBn ? lesson.meaningBengali : lesson.meaningEnglish);
        tvStep1Note.setText(isBn ? "অর্থ ও উচ্চারণ লক্ষ্য করুন" : "Observe the meaning and pronunciation");

        updateRepeatBadgeText();
        updateSpeedBadgeText();

        btnAudioRepeat.setOnClickListener(v -> {
            if (targetRepeatCount == 1) targetRepeatCount = 2;
            else if (targetRepeatCount == 2) targetRepeatCount = 3;
            else if (targetRepeatCount == 3) targetRepeatCount = 5;
            else if (targetRepeatCount == 5) targetRepeatCount = 10;
            else if (targetRepeatCount == 10) targetRepeatCount = -1;
            else targetRepeatCount = 1;

            QuranJourneyManager.setRepeatCount(getContext(), targetRepeatCount);
            updateRepeatBadgeText();
        });

        btnAudioSpeed.setOnClickListener(v -> {
            if (Math.abs(currentSpeed - 0.75f) < 0.05f) currentSpeed = 1.0f;
            else if (Math.abs(currentSpeed - 1.0f) < 0.05f) currentSpeed = 1.25f;
            else if (Math.abs(currentSpeed - 1.25f) < 0.05f) currentSpeed = 1.5f;
            else currentSpeed = 0.75f;

            QuranJourneyManager.setPlaySpeed(getContext(), currentSpeed);
            updateSpeedBadgeText();
            applySpeedToPlayer();
        });

        btnAudioPlayPause.setOnClickListener(v -> toggleAyahPlayback());
    }

    private void updateRepeatBadgeText() {
        if (targetRepeatCount == -1) {
            tvRepeatBadge.setText(isBn ? "লুপ" : "Loop");
        } else {
            tvRepeatBadge.setText(isBn ? (BengaliNumberUtil.toBengali(targetRepeatCount) + " বার") : (targetRepeatCount + "x"));
        }
    }

    private void updateSpeedBadgeText() {
        String speedStr = (Math.abs(currentSpeed - 1.0f) < 0.05f) ? "1.0x" :
                ((Math.abs(currentSpeed - 1.5f) < 0.05f) ? "1.5x" :
                ((Math.abs(currentSpeed - 1.25f) < 0.05f) ? "1.25x" : "0.75x"));
        tvSpeedBadge.setText(isBn ? BengaliNumberUtil.toBengali(speedStr) : speedStr);
    }

    private void applySpeedToPlayer() {
        if (ayahMediaPlayer != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                android.media.PlaybackParams params = ayahMediaPlayer.getPlaybackParams();
                if (params == null) {
                    params = new android.media.PlaybackParams();
                }
                params.setSpeed(currentSpeed);
                ayahMediaPlayer.setPlaybackParams(params);
            } catch (Exception e) {
                try {
                    android.media.PlaybackParams params = new android.media.PlaybackParams();
                    params.setSpeed(currentSpeed);
                    ayahMediaPlayer.setPlaybackParams(params);
                } catch (Exception ex) {
                    Log.w(TAG, "Failed setting speed: " + ex.getMessage());
                }
            }
        }
    }

    private void toggleAyahPlayback() {
        if (ayahMediaPlayer != null && ayahMediaPlayer.isPlaying()) {
            pauseAyahAudio();
        } else {
            if (ayahMediaPlayer != null) {
                try {
                    int pos = ayahMediaPlayer.getCurrentPosition();
                    int dur = ayahMediaPlayer.getDuration();
                    if (dur > 0 && pos >= (dur - 250)) {
                        ayahMediaPlayer.seekTo(0);
                        currentRepeatLoop = 0;
                    }
                } catch (Exception ignored) {}
            }
            playAyahAudio();
        }
    }

    private void playAyahAudio() {
        stopRevisionAudio();
        if (ayahMediaPlayer == null) {
            try {
                ayahMediaPlayer = new MediaPlayer();
                ayahMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build());
                ayahMediaPlayer.setDataSource(lesson.audioUrl);
                ayahMediaPlayer.prepareAsync();

                ayahMediaPlayer.setOnPreparedListener(mp -> {
                    mp.start();
                    applySpeedToPlayer();
                    ivAudioPlayPauseIcon.setImageResource(R.drawable.ic_pause);
                    startProgressTracker();
                });

                ayahMediaPlayer.setOnCompletionListener(mp -> {
                    currentRepeatLoop++;
                    if (targetRepeatCount == -1 || currentRepeatLoop < targetRepeatCount) {
                        try {
                            mp.seekTo(0);
                            applySpeedToPlayer();
                            mp.start();
                        } catch (Exception e) {
                            Log.w(TAG, "Replay failed: " + e.getMessage());
                        }
                    } else {
                        currentRepeatLoop = 0;
                        ivAudioPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow);
                        progressAudioAyah.setProgress(100);
                        stopProgressTracker();
                    }
                });

                ayahMediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    ivAudioPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow);
                    stopProgressTracker();
                    return true;
                });
            } catch (Exception e) {
                Log.e(TAG, "Audio error: " + e.getMessage());
                ivAudioPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow);
            }
        } else {
            ayahMediaPlayer.start();
            applySpeedToPlayer();
            ivAudioPlayPauseIcon.setImageResource(R.drawable.ic_pause);
            startProgressTracker();
        }
    }

    private void pauseAyahAudio() {
        if (ayahMediaPlayer != null && ayahMediaPlayer.isPlaying()) {
            ayahMediaPlayer.pause();
            ivAudioPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow);
            stopProgressTracker();
        }
    }

    private void stopAyahAudio() {
        if (ayahMediaPlayer != null) {
            try {
                if (ayahMediaPlayer.isPlaying()) {
                    ayahMediaPlayer.stop();
                }
                ayahMediaPlayer.reset();
                ayahMediaPlayer.release();
            } catch (Exception ignored) {}
            ayahMediaPlayer = null;
        }
        ivAudioPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow);
        stopProgressTracker();
    }

    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (ayahMediaPlayer != null && ayahMediaPlayer.isPlaying()) {
                int pos = ayahMediaPlayer.getCurrentPosition();
                int dur = ayahMediaPlayer.getDuration();
                if (dur > 0) {
                    int percent = (int) (((long) pos * 100) / dur);
                    progressAudioAyah.setProgress(percent);
                    tvAudioCurrentTime.setText(formatTime(pos));
                    tvAudioTotalTime.setText(formatTime(dur));
                }
                audioHandler.postDelayed(this, 150);
            }
        }
    };

    private void startProgressTracker() {
        audioHandler.removeCallbacks(progressRunnable);
        audioHandler.post(progressRunnable);
    }

    private void stopProgressTracker() {
        audioHandler.removeCallbacks(progressRunnable);
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return String.format(java.util.Locale.US, "%d:%02d", minutes, seconds);
    }

    // ================= STEP 2: WORD BY WORD =================
    private void setupStep2Words() {
        rvJourneyWords.setLayoutManager(new LinearLayoutManager(getContext()));
        rvJourneyWords.setHasFixedSize(true);
        wordAdapter = new QuranJourneyWordAdapter(getContext(), lesson.words);
        rvJourneyWords.setAdapter(wordAdapter);
    }

    // ================= STEP 3: QUIZ =================
    private void setupStep3Quiz() {
        tvQuizAyahBlank.setText(lesson.quizChallengeArabic);

        if (lesson.quizOptions != null && lesson.quizOptions.length >= 4) {
            tvQuizOption1Text.setText(lesson.quizOptions[0]); // لِلَّهِ (Correct)
            tvQuizOption2Text.setText(lesson.quizOptions[1]);
            tvQuizOption3Text.setText(lesson.quizOptions[2]);
            tvQuizOption4Text.setText(lesson.quizOptions[3]);
        }

        cardQuizOption1.setOnClickListener(v -> handleQuizChoice(1, true));
        cardQuizOption2.setOnClickListener(v -> handleQuizChoice(2, false));
        cardQuizOption3.setOnClickListener(v -> handleQuizChoice(3, false));
        cardQuizOption4.setOnClickListener(v -> handleQuizChoice(4, false));
    }

    private void handleQuizChoice(int optionIndex, boolean isCorrect) {
        if (quizAnswered) return;
        quizAnswered = true;

        if (isCorrect) {
            // Option 1 correct
            cardQuizOption1.setBackgroundResource(R.drawable.bg_journey_option_correct);
            tvQuizOption1Badge.setText("✔");
            tvQuizOption1Badge.setTextColor(ContextCompat.getColor(getContext(), R.color.accent_mint));
            tvQuizOption1Badge.setVisibility(View.VISIBLE);

            if (!quizPointsAwarded) {
                quizPointsAwarded = true;
                QuranJourneyManager.addNoorPoints(getContext(), 10);
                updateTopStats();
            }

            layoutQuizFeedbackBanner.setVisibility(View.VISIBLE);
            tvQuizFeedbackTitle.setText(isBn ? "মাশাআল্লাহ! সঠিক উত্তর!" : "MashaAllah! Correct Answer!");
            tvQuizFeedbackSubtitle.setText(isBn ? "আপনি সফলভাবে +১০ নূর পয়েন্ট অর্জন করেছেন!" : "You successfully earned +10 Noor Points!");
        } else {
            // Wrong selection
            RelativeLayout wrongCard = (optionIndex == 2) ? cardQuizOption2 : (optionIndex == 3 ? cardQuizOption3 : cardQuizOption4);
            TextView wrongBadge = (optionIndex == 2) ? tvQuizOption2Badge : (optionIndex == 3 ? tvQuizOption3Badge : tvQuizOption4Badge);

            wrongCard.setBackgroundResource(R.drawable.bg_journey_option_wrong);
            wrongBadge.setText("❌");
            wrongBadge.setTextColor(ContextCompat.getColor(getContext(), R.color.accent_red));
            wrongBadge.setVisibility(View.VISIBLE);

            // Highlight correct option in green
            cardQuizOption1.setBackgroundResource(R.drawable.bg_journey_option_correct);
            tvQuizOption1Badge.setText("✔");
            tvQuizOption1Badge.setTextColor(ContextCompat.getColor(getContext(), R.color.accent_mint));
            tvQuizOption1Badge.setVisibility(View.VISIBLE);

            layoutQuizFeedbackBanner.setVisibility(View.VISIBLE);
            tvQuizFeedbackTitle.setText(isBn ? "🔄 চিন্তা নেই! সঠিক উত্তর হলো: لِلَّهِ" : "No worries! Correct answer is: لِلَّهِ");
            tvQuizFeedbackSubtitle.setText(isBn ? "কারণ: আয়াতটি হলো \"আলহামদু লিল্লাহি রাব্বিল আলামিন\"" : "Reason: The verse is \"Alhamdu lillahi rabbil alamin\"");
        }
    }

    // ================= STEP 4: AMOL & REVISION =================
    private void setupStep4Action() {
        tvLessonTitle.setText(isBn ? "কৃতজ্ঞতা ও শুকরিয়া আদায়" : "Gratitude & Thankfulness");
        tvLessonDescription.setText(isBn ? lesson.lessonBengali : lesson.lessonEnglish);
        tvAmolText.setText(isBn ? lesson.amolBengali : lesson.amolEnglish);

        boolean alreadyDone = QuranJourneyManager.isAmolDoneToday(getContext());
        cbAmolDone.setChecked(alreadyDone);

        View.OnClickListener amolToggle = v -> {
            boolean nextState = !cbAmolDone.isChecked();
            cbAmolDone.setChecked(nextState);
            QuranJourneyManager.setAmolDoneToday(getContext(), nextState);

            if (nextState && !amolPointsAwarded && !alreadyDone) {
                amolPointsAwarded = true;
                QuranJourneyManager.addNoorPoints(getContext(), 10);
                updateTopStats();
            }
        };

        layoutAmolActionRow.setOnClickListener(amolToggle);
        cbAmolDone.setOnClickListener(v -> amolToggle.onClick(v));

        switchDailyReminder.setChecked(QuranJourneyManager.isReminderEnabled(getContext()));
        switchDailyReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            QuranJourneyManager.setReminderEnabled(getContext(), isChecked);
        });

        // 7 Day Revision List or Authentic Empty State
        if (lesson.revisionAyahs == null || lesson.revisionAyahs.isEmpty()) {
            rvRevisionList.setVisibility(View.GONE);
            if (layoutRevisionEmptyState != null) {
                layoutRevisionEmptyState.setVisibility(View.VISIBLE);
                if (tvRevisionEmptyTitle != null) {
                    tvRevisionEmptyTitle.setText(isBn ? "আপনি আপনার কুরআন যাত্রার প্রথম পাঠে আছেন!" : "You are on your first lesson of the Quran Journey!");
                }
                if (tvRevisionEmptySubtitle != null) {
                    tvRevisionEmptySubtitle.setText(isBn ? "পাঠ সম্পন্ন করার পর পূর্ববর্তী আয়াতগুলো এখানে পর্যালোচনার জন্য জমা হবে।" : "Completed verses will appear here for daily revision.");
                }
            }
        } else {
            if (layoutRevisionEmptyState != null) {
                layoutRevisionEmptyState.setVisibility(View.GONE);
            }
            rvRevisionList.setVisibility(View.VISIBLE);
            rvRevisionList.setLayoutManager(new LinearLayoutManager(getContext()));
            rvRevisionList.setHasFixedSize(true);
            revisionAdapter = new QuranJourneyRevisionAdapter(getContext(), lesson.revisionAyahs, (item, position) -> {
                playRevisionAudio(item.audioUrl, position);
            });
            rvRevisionList.setAdapter(revisionAdapter);
        }
    }

    private void playRevisionAudio(String audioUrl, int position) {
        pauseAyahAudio();

        if (revisionAdapter.getCurrentPlayingPosition() == position && revisionMediaPlayer != null && revisionMediaPlayer.isPlaying()) {
            stopRevisionAudio();
            return;
        }

        stopRevisionAudio();
        try {
            revisionMediaPlayer = new MediaPlayer();
            revisionMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());
            revisionMediaPlayer.setDataSource(audioUrl);
            revisionMediaPlayer.prepareAsync();

            revisionMediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                revisionAdapter.setCurrentPlayingPosition(position);
            });

            revisionMediaPlayer.setOnCompletionListener(mp -> {
                stopRevisionAudio();
            });

            revisionMediaPlayer.setOnErrorListener((mp, what, extra) -> {
                stopRevisionAudio();
                return true;
            });
        } catch (Exception e) {
            Log.w(TAG, "Revision audio error: " + e.getMessage());
            stopRevisionAudio();
        }
    }

    private void stopRevisionAudio() {
        if (revisionMediaPlayer != null) {
            try {
                if (revisionMediaPlayer.isPlaying()) revisionMediaPlayer.stop();
                revisionMediaPlayer.reset();
                revisionMediaPlayer.release();
            } catch (Exception ignored) {}
            revisionMediaPlayer = null;
        }
        if (revisionAdapter != null) {
            revisionAdapter.setCurrentPlayingPosition(-1);
        }
    }

    // ================= STEP 5: CELEBRATION =================
    private void setupStep5Celebration() {
        int earnedThisSession = (quizPointsAwarded ? 10 : 0) + (amolPointsAwarded ? 10 : 0);
        if (earnedThisSession == 0) earnedThisSession = 10; // baseline for completing ayah

        tvMetricPointsValue.setText(isBn ? ("✨ +" + BengaliNumberUtil.toBengali(earnedThisSession)) : ("✨ +" + earnedThisSession));

        int streak = QuranJourneyManager.getStreakDays(getContext());
        if (streak == 0) streak = 1;
        tvMetricStreakValue.setText(isBn ? ("🔥 " + BengaliNumberUtil.toBengali(streak) + " দিন") : ("🔥 " + streak + " day"));

        tvMetricAmolValue.setText(isBn ? "🌱 ১টি গাছ" : "🌱 1 Tree");

        int totalPoints = QuranJourneyManager.getNoorPoints(getContext());
        tvTotalPointsBanner.setText(isBn ? ("মোট নূর পয়েন্ট: ✨ " + BengaliNumberUtil.toBengali(totalPoints)) : ("Total Noor Points: ✨ " + totalPoints));

        btnJourneyFinalComplete.setOnClickListener(v -> {
            QuranJourneyManager.markCompletedToday(getContext());
            dismiss();
        });
    }

    // ================= NAVIGATION =================
    private void setupNavigation() {
        btnStepPrevious.setOnClickListener(v -> {
            if (currentStep > 1) {
                currentStep--;
                updateStepUi();
            }
        });

        btnStepNext.setOnClickListener(v -> {
            if (currentStep < 5) {
                currentStep++;
                updateStepUi();
            } else {
                // Completed
                QuranJourneyManager.markCompletedToday(getContext());
                dismiss();
            }
        });
    }

    private void updateStepUi() {
        // Pause audio when moving between steps
        pauseAyahAudio();
        stopRevisionAudio();

        // Update progress segments
        segStep1.setBackgroundResource(currentStep >= 1 ? R.drawable.bg_journey_step_active : R.drawable.bg_journey_step_inactive);
        segStep2.setBackgroundResource(currentStep >= 2 ? R.drawable.bg_journey_step_active : R.drawable.bg_journey_step_inactive);
        segStep3.setBackgroundResource(currentStep >= 3 ? R.drawable.bg_journey_step_active : R.drawable.bg_journey_step_inactive);
        segStep4.setBackgroundResource(currentStep >= 4 ? R.drawable.bg_journey_step_active : R.drawable.bg_journey_step_inactive);
        segStep5.setBackgroundResource(currentStep >= 5 ? R.drawable.bg_journey_step_active : R.drawable.bg_journey_step_inactive);

        tvStepCounterText.setText(isBn ? ("ধাপ " + BengaliNumberUtil.toBengali(currentStep) + " / ৫") : ("Step " + currentStep + " / 5"));

        flipperJourneySteps.setDisplayedChild(currentStep - 1);
        scrollJourneyContent.smoothScrollTo(0, 0);

        btnStepPrevious.setVisibility(currentStep > 1 ? View.VISIBLE : View.INVISIBLE);

        if (currentStep == 5) {
            tvNextStepText.setText(isBn ? "✔ সম্পন্ন করুন" : "✔ Complete");
            setupStep5Celebration();
        } else {
            tvNextStepText.setText(isBn ? "পরের ধাপ →" : "Next Step →");
        }
    }

    @Override
    public void dismiss() {
        stopAyahAudio();
        stopRevisionAudio();
        super.dismiss();
    }
}
