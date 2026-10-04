package com.devflux.deenone.features.battle;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetKnowledgeBattleBinding;
import com.devflux.deenone.databinding.DialogNoInternetCardBinding;
import com.devflux.deenone.features.battle.adapter.BattleCategoryAdapter;
import com.devflux.deenone.features.battle.adapter.BattleLobbyPlayerAdapter;
import com.devflux.deenone.features.battle.data.local.BattleSessionStorage;
import com.devflux.deenone.features.battle.data.remote.BattleCloudRelayClient;
import com.devflux.deenone.features.battle.engine.BattleResultCalculator;
import com.devflux.deenone.features.battle.engine.IslamicBattleEngine;
import com.devflux.deenone.features.battle.model.BattleCategory;
import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattleFinalSummary;
import com.devflux.deenone.features.battle.model.BattleLifecycleState;
import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.battle.model.BattleRoom;
import com.devflux.deenone.features.battle.model.PlayerBattleResult;
import com.devflux.deenone.features.battle.repository.KnowledgeBattleRepository;
import com.devflux.deenone.features.battle.service.BattleRoomServerManager;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class KnowledgeBattlePageDialog {

    private enum ScreenState {
        LANDING,
        TOPIC_SELECTION,
        CONFIG,
        LOBBY,
        ARENA,
        PODIUM
    }

    private final Context context;
    private final FullScreenPageDialog dialog;
    private final BottomSheetKnowledgeBattleBinding binding;
    private final IslamicBattleEngine battleEngine;
    private final BattleRoomServerManager serverManager;
    private final BattleSessionStorage sessionStorage;
    private final String currentUserId;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private ScreenState currentScreen = ScreenState.LANDING;
    private BattleCategory selectedCategory;
    private BattleConfig.BattleMode selectedMode = BattleConfig.BattleMode.ONE_VS_ONE;
    private int selectedQuestionCount = 10;
    private int selectedTotalTimeMinutes = 5;
    private int selectedMaxPlayers = 2;

    private BattleRoom currentRoom;
    private BattleLobbyPlayerAdapter lobbyAdapter;
    private long matchStartTimeMillis = 0;

    public static void show(@NonNull Context context) {
        if (!NetworkConnectivityHelper.isOnline(context)) {
            showNoInternetDialog(context, () -> show(context));
            return;
        }
        KnowledgeBattlePageDialog page = new KnowledgeBattlePageDialog(context);
        page.init();
    }

    public static void showNoInternetDialog(@NonNull Context context, Runnable onRetry) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) return;

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogNoInternetCardBinding b = DialogNoInternetCardBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(b.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        boolean isBn = LocaleManager.isBengali(context);
        b.tvNoInternetTitle.setText(isBn ? "ইন্টারনেট সংযোগ প্রয়োজন" : "Internet Connection Required");
        b.tvNoInternetSubtitle.setText(isBn
                ? "নলেজ ব্যাটেল শুরু করার জন্য ইন্টারনেট কানেকশন বা ওয়াইফাই কানেক্ট করুন।"
                : "Please connect to the internet or Wi-Fi to start Knowledge Battle.");
        b.tvRetryBtnText.setText(isBn ? "ওয়াইফাই সেটিংস / পুনরায় চেষ্টা করুন" : "Wi-Fi Settings / Retry");
        b.btnCloseNoInternet.setText(isBn ? "বাতিল করুন" : "Cancel");

        TouchAnimationUtil.attachTouchSpring(b.btnRetryInternet);
        TouchAnimationUtil.attachTouchSpring(b.btnCloseNoInternet);

        b.btnRetryInternet.setOnClickListener(v -> {
            dialog.dismiss();
            if (NetworkConnectivityHelper.isOnline(context)) {
                if (onRetry != null) onRetry.run();
            } else {
                try {
                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                } catch (Exception e) {
                    try {
                        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        b.btnCloseNoInternet.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public KnowledgeBattlePageDialog(@NonNull Context context) {
        this.context = context;
        this.dialog = new FullScreenPageDialog(context);
        this.binding = BottomSheetKnowledgeBattleBinding.inflate(LayoutInflater.from(context));
        this.dialog.setContentView(binding.getRoot());
        this.battleEngine = new IslamicBattleEngine();
        this.serverManager = BattleRoomServerManager.getInstance();
        this.sessionStorage = BattleSessionStorage.getInstance(context);
        this.currentUserId = sessionStorage.getUserId();

        this.dialog.setOnDismissListener(d -> {
            if (battleEngine != null) {
                battleEngine.destroy();
            }
            if (currentRoom != null) {
                if (currentScreen == ScreenState.LOBBY) {
                    com.devflux.deenone.features.battle.data.remote.BattleDatabaseSyncHandler.getInstance()
                            .updateRoomStateOnServer(context, currentRoom.getRoomCode(), "CANCELLED", null, null, null, null);
                }
                serverManager.stopLobbySync(currentRoom.getRoomCode());
            }
        });
    }

    public void init() {
        TouchAnimationUtil.attachTouchSpring(binding.btnBattleNavBack);
        TouchAnimationUtil.attachTouchSpring(binding.btnTopActionHistory);
        TouchAnimationUtil.attachTouchSpring(binding.btnMenuCreateBattle);
        TouchAnimationUtil.attachTouchSpring(binding.btnMenuJoinWithCode);
        TouchAnimationUtil.attachTouchSpring(binding.btnSubmitJoinCode);
        TouchAnimationUtil.attachTouchSpring(binding.btnConfigCreateBattle);
        TouchAnimationUtil.attachTouchSpring(binding.btnCopyLobbyCode);
        TouchAnimationUtil.attachTouchSpring(binding.btnLobbyShare);
        TouchAnimationUtil.attachTouchSpring(binding.btnLobbyCancelBattle);
        TouchAnimationUtil.attachTouchSpring(binding.btnLobbyStartGame);
        TouchAnimationUtil.attachTouchSpring(binding.btnArenaOpt0);
        TouchAnimationUtil.attachTouchSpring(binding.btnArenaOpt1);
        TouchAnimationUtil.attachTouchSpring(binding.btnArenaOpt2);
        TouchAnimationUtil.attachTouchSpring(binding.btnArenaOpt3);
        TouchAnimationUtil.attachTouchSpring(binding.btnFinalRematch);
        TouchAnimationUtil.attachTouchSpring(binding.btnFinalBackHome);

        setupNavigation();
        setupScreen1Landing();
        setupScreen2Topics();
        setupScreen3Config();
        setupScreen4Lobby();
        setupScreen5Arena();
        setupScreen6Podium();

        applyLocalization();
        showScreen(ScreenState.LANDING);
        dialog.show();
    }

    private void applyLocalization() {
        boolean isBn = LocaleManager.isBengali(context);

        // Screen 1: Landing
        binding.tvBattleLandingSubtitle.setText(isBn
                ? "বন্ধুদের সাথে প্রতিযোগিতা করুন অথবা কোড দিয়ে যোগ দিন"
                : "Compete with friends or join with code");
        binding.tvActiveRoomTitle.setText(isBn
                ? "আপনার একটি সক্রিয় ব্যাটেল রুম চালু আছে"
                : "You have an active battle room");
        binding.tvCreateBattleTitle.setText(isBn
                ? "নতুন ব্যাটেল তৈরি করুন"
                : "Create New Battle");
        binding.tvCreateBattleSubtitle.setText(isBn
                ? "একটি বিষয় বেছে নিন এবং বন্ধুদের আমন্ত্রণ করুন"
                : "Pick a topic and invite friends");
        binding.tvJoinWithCodeTitle.setText(isBn
                ? "কোড দিয়ে যোগ দিন"
                : "Join with Code");
        binding.tvJoinWithCodeSubtitle.setText(isBn
                ? "বন্ধুর দেওয়া ব্যাটেল কোড লিখে সরাসরি যোগ দিন"
                : "Enter battle code to join directly");
        binding.tvJoinCodeBoxTitle.setText(isBn
                ? "ব্যাটেল কোড লিখুন"
                : "Enter Battle Code");
        binding.tvJoinCodeBoxSubtitle.setText(isBn
                ? "৬ ডিজিটের ব্যাটেল কোডটি লিখুন (যেমন: ZQ4CZ2)"
                : "Enter 6-character battle code (e.g. ZQ4CZ2)");
        binding.etJoinRoomCode.setHint(isBn ? "যেমন: ZQ4CZ2" : "e.g. ZQ4CZ2");
        binding.btnSubmitJoinCode.setText(isBn ? "যোগ দিন" : "Join");

        // Screen 2: Topics
        binding.tvTopicsHeaderTitle.setText(isBn ? "টপিক নির্বাচন করুন" : "Select Topic");

        // Screen 3: Config
        binding.tvConfigQCountTitle.setText(isBn ? "প্রশ্ন সংখ্যা" : "Questions");
        binding.cfgQ5.setText(isBn ? "৫" : "5");
        binding.cfgQ10.setText(isBn ? "১০" : "10");
        binding.cfgQ15.setText(isBn ? "১৫" : "15");
        binding.cfgQ20.setText(isBn ? "২০" : "20");
        binding.cfgQ100.setText(isBn ? "১০০" : "100");
        binding.cfgQCustom.setText(isBn ? "কাস্টম" : "Custom");

        binding.tvConfigTotalTimeTitle.setText(isBn ? "সর্বমোট সময়" : "Total Time");
        binding.cfgTime1m.setText(isBn ? "১ মি" : "1 min");
        binding.cfgTime3m.setText(isBn ? "৩ মি" : "3 min");
        binding.cfgTime5m.setText(isBn ? "৫ মি" : "5 min");
        binding.cfgTime10m.setText(isBn ? "১০ মি" : "10 min");
        binding.cfgTimeCustom.setText(isBn ? "কাস্টম" : "Custom");

        binding.tvConfigMaxPlayersTitle.setText(isBn ? "সর্বোচ্চ খেলোয়াড়" : "Total Players");
        binding.cfgPlayer2.setText(isBn ? "২" : "2");
        binding.cfgPlayer3.setText(isBn ? "৩" : "3");
        binding.cfgPlayer4.setText(isBn ? "৪" : "4");
        binding.cfgPlayer5.setText(isBn ? "৫" : "5");

        binding.tvConfigSelectedQCount.setText(isBn ? BengaliNumberUtil.toBengali(selectedQuestionCount) : String.valueOf(selectedQuestionCount));
        binding.tvConfigSelectedTotalTime.setText((isBn ? BengaliNumberUtil.toBengali(selectedTotalTimeMinutes) : String.valueOf(selectedTotalTimeMinutes)) + (isBn ? " মি" : " min"));
        binding.tvConfigSelectedPlayers.setText(isBn ? BengaliNumberUtil.toBengali(selectedMaxPlayers) : String.valueOf(selectedMaxPlayers));
        binding.btnConfigCreateBattle.setText(isBn ? "ব্যাটেল তৈরি করুন" : "Create Battle");

        // Screen 4: Lobby
        binding.tvLobbyCodeHeading.setText(isBn ? "আপনার ব্যাটেল কোড" : "Your Battle Code");
        binding.tvLobbyCodeSubheading.setText(isBn ? "অন্যদের সাথে এই কোডটি শেয়ার করুন" : "Share this code with others");
        binding.tvLobbySpecTopicLabel.setText(isBn ? "বিষয়" : "Topic");
        binding.tvLobbySpecQLabel.setText(isBn ? "প্রশ্ন" : "Questions");
        binding.tvLobbySpecTimeLabel.setText(isBn ? "সময়" : "Time");
        binding.tvLobbyRosterHeaderTitle.setText(isBn ? "খেলোয়াড় তালিকা" : "Player Roster");
        binding.btnLobbyCancelBattle.setText(isBn ? "ব্যাটেল রুম বাতিল করুন" : "Cancel Battle");

        // Screen 5: Arena
        binding.tvServerCountdownTitle.setText(isBn ? "সার্ভার সিঙ্ক্রোনাইজেশন" : "Server Synchronization");
        binding.tvServerCountdownSubtitle.setText(isBn ? "যুদ্ধক্ষেত্রে প্রবেশ করছেন..." : "Entering the arena...");
        binding.tvArenaYouLabel.setText(isBn ? "আপনি" : "You");

        // Screen 6: Podium
        binding.tvPodium1Badge.setText(isBn ? "১ম" : "1st");
        binding.tvPodium2Badge.setText(isBn ? "২য়" : "2nd");
        binding.tvPodium3Badge.setText(isBn ? "৩য়" : "3rd");
        binding.btnFinalRematch.setText(isBn ? "আবার খেলুন" : "Play Again");
        binding.btnFinalBackHome.setText(isBn ? "হোম পেজে ফিরুন" : "Back to Home");
    }

    private void setupNavigation() {
        binding.btnBattleNavBack.setOnClickListener(v -> handleBackPress());
        binding.btnTopActionHistory.setOnClickListener(v -> {
            BattleMatchHistoryDialog.show(context, context instanceof LifecycleOwner ? (LifecycleOwner) context : null);
        });
    }

    private void handleBackPress() {
        switch (currentScreen) {
            case LANDING:
                dialog.dismiss();
                break;
            case TOPIC_SELECTION:
                showScreen(ScreenState.LANDING);
                break;
            case CONFIG:
                showScreen(ScreenState.TOPIC_SELECTION);
                break;
            case LOBBY:
                // Return to landing without destroying the live room
                showScreen(ScreenState.LANDING);
                break;
            case ARENA:
                if (battleEngine != null) battleEngine.destroy();
                showScreen(ScreenState.LANDING);
                break;
            case PODIUM:
                showScreen(ScreenState.LANDING);
                break;
        }
    }

    // ==================== SCREEN 1: LANDING & JOIN VALIDATION ====================
    private void setupScreen1Landing() {
        refreshActiveRoomBanner();

        binding.btnMenuCreateBattle.setOnClickListener(v -> {
            BattleRoom activeRoom = serverManager.getActiveRoomForUser(context, currentUserId);
            if (activeRoom != null && !activeRoom.isFinished()) {
                boolean isBn = LocaleManager.isBengali(context);
                com.google.android.material.dialog.MaterialAlertDialogBuilder builder = new com.google.android.material.dialog.MaterialAlertDialogBuilder(context);
                builder.setTitle(isBn ? "সক্রিয় ব্যাটেল রুম বিদ্যমান" : "Active Battle Room Exists");
                builder.setMessage(isBn
                        ? ("আপনার ইতোমধ্যে একটি সক্রিয় ব্যাটেল রুম চালু রয়েছে (কোড: " + activeRoom.getRoomCode() + ")। আপনি নতুন ব্যাটেল তৈরি করতে পারবেন না। আগের ব্যাটেলে প্রবেশ করুন অথবা রুমটি বাতিল করুন।")
                        : ("You already have an active battle room (Code: " + activeRoom.getRoomCode() + "). You cannot create a new one until it is finished or cancelled. Please return to it or cancel it."));
                builder.setPositiveButton(isBn ? "রুমে প্রবেশ করুন" : "Enter Room", (d, w) -> {
                    this.currentRoom = activeRoom;
                    boolean isHost = activeRoom.getHostPlayer().getId().equals(currentUserId)
                            || sessionStorage.isRoomCreator(activeRoom.getRoomCode());
                    if (isHost) {
                        openLobbyAsHost(activeRoom);
                    } else {
                        openLobbyAsGuest(activeRoom);
                    }
                });
                builder.setNegativeButton(isBn ? "রুম বাতিল করুন" : "Cancel Room", (d, w) -> {
                    String codeToCancel = activeRoom.getRoomCode();
                    com.devflux.deenone.features.battle.data.remote.BattleDatabaseSyncHandler.getInstance()
                            .updateRoomStateOnServer(context, codeToCancel, "CANCELLED", null, null, null, null);
                    serverManager.deleteRoom(codeToCancel);
                    sessionStorage.clearActiveSession();
                    refreshActiveRoomBanner();
                    Toast.makeText(context, isBn ? "ব্যাটেল রুম বাতিল করা হয়েছে" : "Battle room cancelled", Toast.LENGTH_SHORT).show();
                });
                builder.setNeutralButton(isBn ? "বন্ধ করুন" : "Close", (d, w) -> d.dismiss());
                builder.show();
                return;
            }
            showScreen(ScreenState.TOPIC_SELECTION);
        });

        binding.btnMenuJoinWithCode.setOnClickListener(v -> {
            int vis = binding.cardJoinCodeInputBox.getVisibility();
            binding.cardJoinCodeInputBox.setVisibility(vis == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        binding.btnSubmitJoinCode.setOnClickListener(v -> {
            boolean isBn = LocaleManager.isBengali(context);
            String rawInput = binding.etJoinRoomCode.getText() != null ? binding.etJoinRoomCode.getText().toString().trim() : "";
            String code = BengaliNumberUtil.toEnglish(rawInput).replaceAll("[^A-Za-z0-9]", "").toUpperCase();
            if (code.length() < 4) {
                Toast.makeText(context, isBn ? "দয়া করে সঠিক ৬ ডিজিটের কোড লিখুন" : "Please enter a valid 6-character code", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.btnSubmitJoinCode.setEnabled(false);
            binding.btnSubmitJoinCode.setText(isBn ? "যাচাই করা হচ্ছে..." : "Verifying...");

            String userName = sessionStorage.getUserName();
            if (userName != null) {
                userName = userName.replace("(আপনি)", "").replace("(You)", "").trim();
            }
            if (userName == null || userName.isEmpty() || userName.equalsIgnoreCase("Anonymous")) {
                userName = isBn ? "আপনি" : "You";
            }
            BattlePlayer myPlayer = new BattlePlayer(currentUserId, userName, "ic_person", false);

            serverManager.validateAndJoinRoom(context, code, myPlayer, result -> {
                binding.btnSubmitJoinCode.setEnabled(true);
                binding.btnSubmitJoinCode.setText(isBn ? "যোগ দিন" : "Join");

                if (result.isSuccess() && result.getRoom() != null) {
                    this.currentRoom = result.getRoom();
                    boolean isHost = result.getRoom().getHostPlayer().getId().equals(currentUserId)
                            || sessionStorage.isRoomCreator(code);
                    if (isHost) {
                        openLobbyAsHost(currentRoom);
                    } else {
                        openLobbyAsGuest(currentRoom);
                    }
                } else {
                    Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void refreshActiveRoomBanner() {
        boolean isBn = LocaleManager.isBengali(context);
        BattleRoom activeRoom = serverManager.getActiveRoomForUser(context, currentUserId);
        if (activeRoom != null && !activeRoom.isFinished()) {
            binding.cardActiveRoomBanner.setVisibility(View.VISIBLE);
            binding.tvActiveRoomCodeInfo.setText(isBn
                    ? ("কোড: " + activeRoom.getRoomCode() + " • ফিরে যেতে ট্যাপ করুন")
                    : ("Code: " + activeRoom.getRoomCode() + " • Tap to return"));
            binding.cardActiveRoomBanner.setOnClickListener(v -> {
                this.currentRoom = activeRoom;
                boolean isHost = activeRoom.getHostPlayer().getId().equals(currentUserId)
                        || sessionStorage.isRoomCreator(activeRoom.getRoomCode());
                if (isHost) {
                    openLobbyAsHost(activeRoom);
                } else {
                    openLobbyAsGuest(activeRoom);
                }
            });
        } else {
            binding.cardActiveRoomBanner.setVisibility(View.GONE);
        }
    }

    // ==================== SCREEN 2: TOPICS ====================
    private void setupScreen2Topics() {
        BattleCategoryAdapter adapter = new BattleCategoryAdapter(category -> {
            this.selectedCategory = category;
            showScreen(ScreenState.CONFIG);
        });

        binding.rvTopicsList.setLayoutManager(new GridLayoutManager(context, 2));
        binding.rvTopicsList.setAdapter(adapter);
        adapter.setCategories(KnowledgeBattleRepository.getAllCategories());

        // Fetch live authentic category question counts from MySQL
        KnowledgeBattleRepository.fetchLiveCategoryQuestionCounts(context, () -> {
            if (binding != null && binding.rvTopicsList != null) {
                adapter.setCategories(KnowledgeBattleRepository.getAllCategories());
            }
        });
    }

    // ==================== SCREEN 3: CONFIG ====================
    private void setupScreen3Config() {
        // 1. Question Count
        binding.cfgQ5.setOnClickListener(v -> selectQuestionChip(5, binding.cfgQ5));
        binding.cfgQ10.setOnClickListener(v -> selectQuestionChip(10, binding.cfgQ10));
        binding.cfgQ15.setOnClickListener(v -> selectQuestionChip(15, binding.cfgQ15));
        binding.cfgQ20.setOnClickListener(v -> selectQuestionChip(20, binding.cfgQ20));
        binding.cfgQ100.setOnClickListener(v -> selectQuestionChip(100, binding.cfgQ100));
        binding.cfgQCustom.setOnClickListener(v -> selectQuestionChip(25, binding.cfgQCustom));

        // 2. Total Time (Minutes)
        binding.cfgTime1m.setOnClickListener(v -> selectTotalTimeChip(1, binding.cfgTime1m));
        binding.cfgTime3m.setOnClickListener(v -> selectTotalTimeChip(3, binding.cfgTime3m));
        binding.cfgTime5m.setOnClickListener(v -> selectTotalTimeChip(5, binding.cfgTime5m));
        binding.cfgTime10m.setOnClickListener(v -> selectTotalTimeChip(10, binding.cfgTime10m));
        binding.cfgTimeCustom.setOnClickListener(v -> selectTotalTimeChip(15, binding.cfgTimeCustom));

        // 3. Max Players
        binding.cfgPlayer2.setOnClickListener(v -> selectPlayersChip(2, binding.cfgPlayer2));
        binding.cfgPlayer3.setOnClickListener(v -> selectPlayersChip(3, binding.cfgPlayer3));
        binding.cfgPlayer4.setOnClickListener(v -> selectPlayersChip(4, binding.cfgPlayer4));
        binding.cfgPlayer5.setOnClickListener(v -> selectPlayersChip(5, binding.cfgPlayer5));

        // Create Battle Button
        binding.btnConfigCreateBattle.setOnClickListener(v -> {
            boolean isBn = LocaleManager.isBengali(context);
            String categoryTitle = selectedCategory != null
                    ? (isBn ? selectedCategory.getTitleBn() : selectedCategory.getTitleEn())
                    : (isBn ? "কুরআনের ঘটনা ও শিক্ষা" : "Quran Stories & Lessons");

            BattleConfig config = new BattleConfig(
                    selectedMaxPlayers,
                    selectedQuestionCount,
                    selectedTotalTimeMinutes,
                    selectedCategory != null ? selectedCategory.getId() : "quran_stories",
                    categoryTitle
            );

            String hostName = sessionStorage.getUserName();
            if (hostName != null) {
                hostName = hostName.replace("(আপনি)", "").replace("(You)", "").trim();
            }
            if (hostName == null || hostName.isEmpty() || hostName.equalsIgnoreCase("Anonymous")) {
                hostName = isBn ? "আপনি" : "You";
            }
            BattlePlayer host = new BattlePlayer(currentUserId, hostName, "ic_person", false);

            binding.btnConfigCreateBattle.setEnabled(false);
            binding.btnConfigCreateBattle.setText(isBn ? "ব্যাটেল রুম তৈরি হচ্ছে..." : "Creating Battle Room...");

            serverManager.createRoom(context, config, host, new BattleRoomServerManager.RoomCallback() {
                @Override
                public void onSuccess(BattleRoom room) {
                    binding.btnConfigCreateBattle.setEnabled(true);
                    binding.btnConfigCreateBattle.setText(isBn ? "ব্যাটেল তৈরি করুন" : "Create Battle");
                    currentRoom = room;
                    openLobbyAsHost(room);
                }

                @Override
                public void onError(String errorMessage) {
                    binding.btnConfigCreateBattle.setEnabled(true);
                    binding.btnConfigCreateBattle.setText(isBn ? "ব্যাটেল তৈরি করুন" : "Create Battle");
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void selectQuestionChip(int count, TextView target) {
        boolean isBn = LocaleManager.isBengali(context);
        this.selectedQuestionCount = count;
        binding.tvConfigSelectedQCount.setText(isBn ? BengaliNumberUtil.toBengali(count) : String.valueOf(count));
        resetChips(binding.cfgQ5, binding.cfgQ10, binding.cfgQ15, binding.cfgQ20, binding.cfgQ100, binding.cfgQCustom);
        highlightChip(target);
    }

    private void selectTotalTimeChip(int minutes, TextView target) {
        boolean isBn = LocaleManager.isBengali(context);
        this.selectedTotalTimeMinutes = minutes;
        binding.tvConfigSelectedTotalTime.setText((isBn ? BengaliNumberUtil.toBengali(minutes) : String.valueOf(minutes)) + (isBn ? " মি" : " min"));
        resetChips(binding.cfgTime1m, binding.cfgTime3m, binding.cfgTime5m, binding.cfgTime10m, binding.cfgTimeCustom);
        highlightChip(target);
    }

    private void selectPlayersChip(int count, TextView target) {
        boolean isBn = LocaleManager.isBengali(context);
        this.selectedMaxPlayers = count;
        binding.tvConfigSelectedPlayers.setText(isBn ? BengaliNumberUtil.toBengali(count) : String.valueOf(count));
        resetChips(binding.cfgPlayer2, binding.cfgPlayer3, binding.cfgPlayer4, binding.cfgPlayer5);
        highlightChip(target);
    }

    private void resetChips(TextView... chips) {
        int bg = ContextCompat.getColor(context, R.color.bg_card_secondary);
        int text = ContextCompat.getColor(context, R.color.text_secondary);
        for (TextView t : chips) {
            t.setBackgroundResource(R.drawable.bg_badge_pill);
            t.setBackgroundTintList(ColorStateList.valueOf(bg));
            t.setTextColor(text);
        }
    }

    private void highlightChip(TextView target) {
        int bg = ContextCompat.getColor(context, R.color.accent_gold);
        int text = ContextCompat.getColor(context, R.color.text_primary);
        target.setBackgroundResource(R.drawable.bg_badge_pill);
        target.setBackgroundTintList(ColorStateList.valueOf(bg));
        target.setTextColor(text);
    }

    // ==================== SCREEN 4: WAITING LOBBY ====================
    private void setupScreen4Lobby() {
        lobbyAdapter = new BattleLobbyPlayerAdapter();
        binding.rvLobbyPlayerRoster.setLayoutManager(new LinearLayoutManager(context));
        binding.rvLobbyPlayerRoster.setAdapter(lobbyAdapter);

        binding.btnCopyLobbyCode.setOnClickListener(v -> {
            if (currentRoom == null) return;
            boolean isBn = LocaleManager.isBengali(context);
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("Battle Room Code", currentRoom.getRoomCode()));
                Toast.makeText(context, isBn ? ("রুম কোড কপি করা হয়েছে: " + currentRoom.getRoomCode()) : ("Room code copied: " + currentRoom.getRoomCode()), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnLobbyShare.setOnClickListener(v -> shareLobbyInvitation());

        binding.btnLobbyCancelBattle.setOnClickListener(v -> {
            boolean isBn = LocaleManager.isBengali(context);
            if (currentRoom != null) {
                String codeToCancel = currentRoom.getRoomCode();
                com.devflux.deenone.features.battle.data.remote.BattleDatabaseSyncHandler.getInstance()
                        .updateRoomStateOnServer(context, codeToCancel, "CANCELLED", null, null, null, null);
                serverManager.deleteRoom(codeToCancel);
                currentRoom = null;
            }
            sessionStorage.clearActiveSession();
            refreshActiveRoomBanner();
            Toast.makeText(context, isBn ? "ব্যাটেল রুম বাতিল করা হয়েছে" : "Battle room cancelled", Toast.LENGTH_SHORT).show();
            showScreen(ScreenState.LANDING);
        });

        binding.btnLobbyStartGame.setOnClickListener(v -> {
            if (currentRoom == null) return;
            boolean isBn = LocaleManager.isBengali(context);

            if (!serverManager.canStartBattle(currentRoom.getRoomCode())) {
                Toast.makeText(context, isBn ? "প্রয়োজনীয় সংখ্যক খেলোয়াড় রেডি না হওয়া পর্যন্ত শুরু করা যাবে না!" : "Cannot start until all required players are ready!", Toast.LENGTH_LONG).show();
                return;
            }

            startServerSynchronizedMatchCountdown();
        });
    }

    private void shareLobbyInvitation() {
        if (currentRoom == null) return;
        boolean isBn = LocaleManager.isBengali(context);
        String catTitle = isBn ? currentRoom.getConfig().getCategoryTitleBn() : currentRoom.getConfig().getCategoryTitleEn();
        String msg = isBn
                ? ("আসসালামু আলাইকুম! DeenOne নলেজ ব্যাটেল-এ যোগ দিন। বিষয়: " +
                catTitle +
                "\nব্যাটেল কোড: " + currentRoom.getRoomCode())
                : ("Assalamu Alaikum! Join DeenOne Knowledge Battle. Topic: " +
                catTitle +
                "\nBattle Code: " + currentRoom.getRoomCode());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        context.startActivity(Intent.createChooser(intent, isBn ? "ব্যাটেল কোড শেয়ার করুন" : "Share Battle Code"));
    }

    private void openLobbyAsHost(BattleRoom room) {
        this.currentRoom = room;
        updateLobbyUI(room);
        showScreen(ScreenState.LOBBY);

        serverManager.startLobbySync(room.getRoomCode(), new BattleCloudRelayClient.RoomSyncListener() {
            @Override
            public void onPlayerJoined(BattlePlayer player) {
                if (currentRoom != null && !currentRoom.hasPlayer(player.getId())) {
                    currentRoom.addPlayer(player);
                    updateLobbyUI(currentRoom);
                    boolean isBn = LocaleManager.isBengali(context);
                    Toast.makeText(context, isBn ? (player.getName() + " ব্যাটেলে যোগ দিয়েছেন!") : (player.getName() + " joined the battle!"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPlayerStatusChanged(String playerId, BattlePlayer.PlayerStatus status) {
                if (currentRoom != null) {
                    currentRoom.updatePlayerStatus(playerId, status);
                    updateLobbyUI(currentRoom);
                }
            }

            @Override
            public void onMatchStarted() {
                // Host initiates match start directly
            }

            @Override
            public void onOpponentAnswer(String playerId, int score, boolean isCorrect, int pointsDelta) {
                if (currentRoom != null && !playerId.equals(currentUserId)) {
                    for (BattlePlayer p : currentRoom.getPlayers()) {
                        if (p.getId().equals(playerId)) {
                            p.setScore(score);
                            if (isCorrect) p.incrementCorrect();
                            else p.incrementWrong();
                            break;
                        }
                    }
                    updateArenaScores();
                }
            }

            @Override
            public void onRoomCancelled() {}
        });
    }

    private void openLobbyAsGuest(BattleRoom room) {
        this.currentRoom = room;
        updateLobbyUI(room);
        showScreen(ScreenState.LOBBY);

        serverManager.startLobbySync(room.getRoomCode(), new BattleCloudRelayClient.RoomSyncListener() {
            @Override
            public void onPlayerJoined(BattlePlayer player) {
                if (currentRoom != null && !currentRoom.hasPlayer(player.getId())) {
                    currentRoom.addPlayer(player);
                    updateLobbyUI(currentRoom);
                }
            }

            @Override
            public void onPlayerStatusChanged(String playerId, BattlePlayer.PlayerStatus status) {
                if (currentRoom != null) {
                    currentRoom.updatePlayerStatus(playerId, status);
                    updateLobbyUI(currentRoom);
                }
            }

            @Override
            public void onMatchStarted() {
                if (currentScreen == ScreenState.LOBBY) {
                    startServerSynchronizedMatchCountdown();
                }
            }

            @Override
            public void onOpponentAnswer(String playerId, int score, boolean isCorrect, int pointsDelta) {
                if (currentRoom != null && !playerId.equals(currentUserId)) {
                    for (BattlePlayer p : currentRoom.getPlayers()) {
                        if (p.getId().equals(playerId)) {
                            p.setScore(score);
                            if (isCorrect) p.incrementCorrect();
                            else p.incrementWrong();
                            break;
                        }
                    }
                    updateArenaScores();
                }
            }

            @Override
            public void onRoomCancelled() {
                if (currentScreen == ScreenState.LOBBY) {
                    boolean isBn = LocaleManager.isBengali(context);
                    Toast.makeText(context, isBn ? "হোস্ট ব্যাটেল রুম বাতিল করেছেন" : "Host cancelled the battle room", Toast.LENGTH_LONG).show();
                    showScreen(ScreenState.LANDING);
                }
            }
        });

        // Register real-time sync listener so guest seamlessly joins when host starts match
        serverManager.registerSyncListener(room.getRoomCode(), syncState -> {
            if (currentScreen == ScreenState.LOBBY && syncState != null) {
                if (syncState.getLifecycleState() == BattleLifecycleState.COUNTDOWN ||
                        syncState.getLifecycleState() == BattleLifecycleState.LIVE) {
                    startServerSynchronizedMatchCountdown();
                }
            }
        });
    }

    private void updateLobbyUI(BattleRoom room) {
        if (room == null) return;
        boolean isBn = LocaleManager.isBengali(context);

        binding.tvLobbyRoomCode.setText(room.getRoomCode());
        binding.tvLobbyTopicTitle.setText(isBn ? room.getConfig().getCategoryTitleBn() : room.getConfig().getCategoryTitleEn());
        binding.tvLobbyQuestionCount.setText(isBn ? BengaliNumberUtil.toBengali(room.getConfig().getTotalQuestions()) : String.valueOf(room.getConfig().getTotalQuestions()));
        binding.tvLobbyDuration.setText((isBn ? BengaliNumberUtil.toBengali(room.getConfig().getTotalDurationMinutes()) : String.valueOf(room.getConfig().getTotalDurationMinutes())) + (isBn ? " মি" : " min"));
        binding.tvLobbyPlayerCountRatio.setText((isBn ? BengaliNumberUtil.toBengali(room.getPlayerCount()) : String.valueOf(room.getPlayerCount())) + "/" + (isBn ? BengaliNumberUtil.toBengali(room.getConfig().getTotalPlayers()) : String.valueOf(room.getConfig().getTotalPlayers())));

        lobbyAdapter.setRoomData(room, player -> {
            BattlePlayer.PlayerStatus newStatus = player.isReady() ? BattlePlayer.PlayerStatus.NOT_READY : BattlePlayer.PlayerStatus.READY;
            serverManager.updatePlayerStatus(room.getRoomCode(), player.getId(), newStatus, (r, canStart) -> {
                updateLobbyUI(r);
            });
        });

        int currentCount = room.getPlayerCount();
        int totalRequired = room.getConfig().getTotalPlayers();
        boolean isAllReady = room.isAllPlayersReady();
        boolean isHost = room.getHostPlayer().getId().equals(currentUserId)
                || sessionStorage.isRoomCreator(room.getRoomCode());

        binding.btnLobbyCancelBattle.setText(isBn ? "ব্যাটেল বাতিল করুন" : "Cancel Battle");

        int primaryGreen = ContextCompat.getColor(context, R.color.primary_green);
        int white = ContextCompat.getColor(context, R.color.white);
        int bgSecondary = ContextCompat.getColor(context, R.color.bg_card_secondary);
        int textMuted = ContextCompat.getColor(context, R.color.text_muted);
        int textSecondary = ContextCompat.getColor(context, R.color.text_secondary);

        String countFraction = (isBn ? BengaliNumberUtil.toBengali(currentCount) : String.valueOf(currentCount)) + "/" + (isBn ? BengaliNumberUtil.toBengali(totalRequired) : String.valueOf(totalRequired));

        if (!isHost) {
            binding.btnLobbyStartGame.setEnabled(false);
            binding.btnLobbyStartGame.setBackgroundTintList(ColorStateList.valueOf(bgSecondary));
            binding.btnLobbyStartGame.setTextColor(textSecondary);
            binding.btnLobbyStartGame.setText(isAllReady
                    ? (isBn ? "হোস্ট খেলা শুরু করার অপেক্ষায়..." : "Waiting for host to start...")
                    : (isBn ? ("খেলোয়াড়দের জন্য অপেক্ষা করুন (" + countFraction + ")")
                            : ("Waiting for players (" + countFraction + ")")));
            return;
        }

        if (currentCount < totalRequired) {
            binding.btnLobbyStartGame.setEnabled(false);
            binding.btnLobbyStartGame.setBackgroundTintList(ColorStateList.valueOf(bgSecondary));
            binding.btnLobbyStartGame.setTextColor(textMuted);
            binding.btnLobbyStartGame.setText(isBn
                    ? ("খেলোয়াড়দের জন্য অপেক্ষা করুন (" + countFraction + ")")
                    : ("Waiting for players (" + countFraction + ")"));
        } else if (!isAllReady) {
            binding.btnLobbyStartGame.setEnabled(false);
            binding.btnLobbyStartGame.setBackgroundTintList(ColorStateList.valueOf(bgSecondary));
            binding.btnLobbyStartGame.setTextColor(textSecondary);
            binding.btnLobbyStartGame.setText(isBn ? "সব খেলোয়াড় রেডি হওয়ার অপেক্ষা..." : "Waiting for all players to be ready...");
        } else {
            binding.btnLobbyStartGame.setEnabled(true);
            binding.btnLobbyStartGame.setBackgroundTintList(ColorStateList.valueOf(primaryGreen));
            binding.btnLobbyStartGame.setTextColor(white);
            binding.btnLobbyStartGame.setText(isBn ? "ব্যাটেল শুরু করুন" : "Start Battle");
        }
    }

    // ==================== SCREEN 5: SYNCHRONIZED COUNTDOWN & ARENA ====================
    private void setupScreen5Arena() {
        binding.btnArenaOpt0.setOnClickListener(v -> submitUserOption(0));
        binding.btnArenaOpt1.setOnClickListener(v -> submitUserOption(1));
        binding.btnArenaOpt2.setOnClickListener(v -> submitUserOption(2));
        binding.btnArenaOpt3.setOnClickListener(v -> submitUserOption(3));
    }

    private void submitUserOption(int optIndex) {
        binding.btnArenaOpt0.setEnabled(false);
        binding.btnArenaOpt1.setEnabled(false);
        binding.btnArenaOpt2.setEnabled(false);
        binding.btnArenaOpt3.setEnabled(false);

        battleEngine.submitUserAnswer(context, currentUserId, optIndex);
    }

    private void startServerSynchronizedMatchCountdown() {
        if (currentRoom == null) return;
        boolean isBn = LocaleManager.isBengali(context);
        showScreen(ScreenState.ARENA);

        binding.layoutCountdownOverlay.setVisibility(View.VISIBLE);
        binding.tvServerCountdownSubtitle.setText(isBn ? "যুদ্ধক্ষেত্রে প্রবেশ করছেন..." : "Entering the arena...");
        matchStartTimeMillis = System.currentTimeMillis();

        serverManager.startSynchronizedCountdown(currentRoom.getRoomCode(), new BattleRoomServerManager.CountdownCallback() {
            @Override
            public void onTick(int secondsRemaining) {
                binding.tvServerCountdownNumber.setText(isBn ? BengaliNumberUtil.toBengali(secondsRemaining) : String.valueOf(secondsRemaining));
            }

            @Override
            public void onMatchStarted() {
                binding.layoutCountdownOverlay.setVisibility(View.GONE);
                startLiveBattle();
            }
        });
    }

    private void startLiveBattle() {
        if (currentRoom == null) return;
        boolean isBn = LocaleManager.isBengali(context);

        battleEngine.startBattle(currentRoom, currentUserId, new IslamicBattleEngine.BattleEngineListener() {
            @Override
            public void onCountdownTick(int secondsRemaining) {}

            @Override
            public void onRoundStarted(int questionIndex, BattleQuestion question, int totalQuestions) {
                if (isBn) {
                    binding.tvArenaRoundNumber.setText(BengaliNumberUtil.toBengali(questionIndex + 1) + "/" + BengaliNumberUtil.toBengali(totalQuestions));
                    binding.tvArenaTopicBadge.setText(currentRoom.getConfig().getCategoryTitleBn());
                    binding.tvArenaQuestion.setText(question.getQuestionText());

                    resetOptionButton(binding.btnArenaOpt0, "ক. " + question.getOptions().get(0));
                    resetOptionButton(binding.btnArenaOpt1, "খ. " + question.getOptions().get(1));
                    resetOptionButton(binding.btnArenaOpt2, "গ. " + question.getOptions().get(2));
                    resetOptionButton(binding.btnArenaOpt3, "ঘ. " + question.getOptions().get(3));

                    binding.tvArenaFeedbackText.setText("সঠিক উত্তর দিলে +১০ পয়েন্ট, ভুল হলে -৫ পয়েন্ট");
                } else {
                    binding.tvArenaRoundNumber.setText((questionIndex + 1) + "/" + totalQuestions);
                    binding.tvArenaTopicBadge.setText(currentRoom.getConfig().getCategoryTitleEn());
                    binding.tvArenaQuestion.setText(question.getQuestionText());

                    resetOptionButton(binding.btnArenaOpt0, "A. " + question.getOptions().get(0));
                    resetOptionButton(binding.btnArenaOpt1, "B. " + question.getOptions().get(1));
                    resetOptionButton(binding.btnArenaOpt2, "C. " + question.getOptions().get(2));
                    resetOptionButton(binding.btnArenaOpt3, "D. " + question.getOptions().get(3));

                    binding.tvArenaFeedbackText.setText("Correct answer +10 points, wrong -5 points");
                }
                binding.tvArenaFeedbackText.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));

                updateArenaScores();
            }

            @Override
            public void onRoundTimerTick(int secondsRemaining, int totalDuration) {
                binding.tvArenaTimerCount.setText(isBn ? BengaliNumberUtil.toBengali(secondsRemaining) : String.valueOf(secondsRemaining));
                if (secondsRemaining <= 3) {
                    binding.tvArenaTimerCount.setTextColor(ContextCompat.getColor(context, R.color.accent_red));
                } else {
                    binding.tvArenaTimerCount.setTextColor(ContextCompat.getColor(context, R.color.accent_gold));
                }
            }

            @Override
            public void onPlayerAnswered(BattlePlayer player, int optionSelected, boolean isCorrect, int pointsDelta) {
                if (player != null && player.getId().equals(currentUserId)) {
                    Button btn = getOptionButton(optionSelected);
                    int green = ContextCompat.getColor(context, R.color.primary_green);
                    int red = ContextCompat.getColor(context, R.color.accent_red);
                    int white = ContextCompat.getColor(context, R.color.white);
                    if (btn != null) {
                        if (isCorrect) {
                            btn.setBackgroundTintList(ColorStateList.valueOf(green));
                            btn.setTextColor(white);
                            binding.tvArenaFeedbackText.setText(isBn ? "মাশাআল্লাহ! সঠিক উত্তর (+১০ পয়েন্ট)" : "MashaAllah! Correct answer (+10 pts)");
                            binding.tvArenaFeedbackText.setTextColor(green);
                        } else {
                            btn.setBackgroundTintList(ColorStateList.valueOf(red));
                            btn.setTextColor(white);
                            binding.tvArenaFeedbackText.setText(isBn ? "ভুল উত্তর (-৫ পয়েন্ট)" : "Wrong answer (-5 pts)");
                            binding.tvArenaFeedbackText.setTextColor(red);
                        }
                    }
                }
                updateArenaScores();
            }

            @Override
            public void onRoundEnded(BattleQuestion question, int userOption, int botOption) {
                int green = ContextCompat.getColor(context, R.color.primary_green);
                int white = ContextCompat.getColor(context, R.color.white);
                Button correctBtn = getOptionButton(question.getCorrectOptionIndex());
                if (correctBtn != null) {
                    correctBtn.setBackgroundTintList(ColorStateList.valueOf(green));
                    correctBtn.setTextColor(white);
                }
                binding.tvArenaFeedbackText.setText((isBn ? "দলিল: " : "Reference: ") + question.getReference());
                binding.tvArenaFeedbackText.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
            }

            @Override
            public void onMatchFinished(List<BattlePlayer> rankedPlayers, List<BattleQuestion> questionReviewList, int netPointsEarned) {
                showScreen(ScreenState.PODIUM);

                long matchDuration = (System.currentTimeMillis() - matchStartTimeMillis) / 1000L;
                BattleFinalSummary summary = BattleResultCalculator.computeFinalSummary(currentRoom, matchDuration);

                if (summary == null || summary.getRankedResults().isEmpty()) return;

                List<PlayerBattleResult> results = summary.getRankedResults();
                PlayerBattleResult p1 = results.get(0);
                boolean isUserWinner = p1.getPlayerId().equals(currentUserId);

                if (isUserWinner) {
                    binding.tvFinalWinnerHeading.setText(isBn ? "মাশাআল্লাহ! আপনি বিজয়ী!" : "MashaAllah! You Won!");
                    binding.tvFinalWinnerHeading.setTextColor(ContextCompat.getColor(context, R.color.accent_gold));
                } else {
                    binding.tvFinalWinnerHeading.setText(isBn ? "চমৎকার প্রতিযোগিতা হয়েছে!" : "Great match played!");
                    binding.tvFinalWinnerHeading.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
                }

                binding.tvFinalXpBonus.setText("+" + (isBn ? BengaliNumberUtil.toBengali(p1.getDeenXpEarned()) : String.valueOf(p1.getDeenXpEarned())) + " Deen XP " + (isBn ? "অর্জিত হয়েছে" : "earned"));

                // 1st Place Podium
                String p1DisplayName = p1.getPlayerName();
                if (p1DisplayName != null) {
                    p1DisplayName = p1DisplayName.replace("(আপনি)", "").replace("(You)", "").trim();
                }
                if (p1DisplayName == null || p1DisplayName.isEmpty() || p1DisplayName.equalsIgnoreCase("Anonymous")) {
                    p1DisplayName = isBn ? "আপনি" : "You";
                }
                binding.tvPodium1Name.setText(p1DisplayName);
                binding.tvPodium1Score.setText((isBn ? BengaliNumberUtil.toBengali(p1.getFinalScore()) : String.valueOf(p1.getFinalScore())) + (isBn ? " পয়েন্ট" : " Points"));

                // 2nd Place Podium
                if (results.size() > 1) {
                    PlayerBattleResult p2 = results.get(1);
                    String p2DisplayName = p2.getPlayerName();
                    if (p2DisplayName != null) {
                        p2DisplayName = p2DisplayName.replace("(আপনি)", "").replace("(You)", "").trim();
                    }
                    if (p2DisplayName == null || p2DisplayName.isEmpty() || p2DisplayName.equalsIgnoreCase("Anonymous")) {
                        p2DisplayName = isBn ? "খেলোয়াড় ২" : "Player 2";
                    }
                    binding.tvPodium2Name.setText(p2DisplayName);
                    binding.tvPodium2Score.setText((isBn ? BengaliNumberUtil.toBengali(p2.getFinalScore()) : String.valueOf(p2.getFinalScore())) + (isBn ? " পয়েন্ট" : " Points"));
                }

                // 3rd Place Podium
                if (results.size() > 2) {
                    PlayerBattleResult p3 = results.get(2);
                    if (binding.layoutPodiumCol3 != null) {
                        binding.layoutPodiumCol3.setVisibility(View.VISIBLE);
                        String p3DisplayName = p3.getPlayerName();
                        if (p3DisplayName != null) {
                            p3DisplayName = p3DisplayName.replace("(আপনি)", "").replace("(You)", "").trim();
                        }
                        if (p3DisplayName == null || p3DisplayName.isEmpty() || p3DisplayName.equalsIgnoreCase("Anonymous")) {
                            p3DisplayName = isBn ? "খেলোয়াড় ৩" : "Player 3";
                        }
                        binding.tvPodium3Name.setText(p3DisplayName);
                        binding.tvPodium3Score.setText((isBn ? BengaliNumberUtil.toBengali(p3.getFinalScore()) : String.valueOf(p3.getFinalScore())) + (isBn ? " পয়েন্ট" : " Points"));
                    }
                } else {
                    if (binding.layoutPodiumCol3 != null) {
                        binding.layoutPodiumCol3.setVisibility(View.GONE);
                    }
                }

                // Persist Match Result to Local Room Database & Session Storage
                BattleSessionStorage.getInstance(context)
                        .recordCompletedBattle(summary, currentUserId);

                if (currentRoom != null) {
                    org.json.JSONArray arr = new org.json.JSONArray();
                    for (PlayerBattleResult pbr : results) {
                        org.json.JSONObject obj = new org.json.JSONObject();
                        try {
                            obj.put("playerId", pbr.getPlayerId());
                            obj.put("playerName", pbr.getPlayerName());
                            obj.put("score", pbr.getFinalScore());
                            obj.put("correctCount", pbr.getCorrectAnswers());
                            obj.put("wrongCount", pbr.getWrongAnswers());
                            arr.put(obj);
                        } catch (Exception ignored) {}
                    }
                    com.devflux.deenone.features.battle.data.remote.BattleDatabaseSyncHandler.getInstance()
                            .updateRoomStateOnServer(
                                    context,
                                    currentRoom.getRoomCode(),
                                    "COMPLETED",
                                    p1 != null ? p1.getPlayerId() : "",
                                    p1 != null ? p1.getPlayerName() : "",
                                    arr.toString(),
                                    null
                            );
                }
            }
        });
    }

    private void resetOptionButton(Button btn, String text) {
        int bg = ContextCompat.getColor(context, R.color.bg_card_secondary);
        int textColor = ContextCompat.getColor(context, R.color.text_primary);
        btn.setEnabled(true);
        btn.setText(text);
        btn.setBackgroundTintList(ColorStateList.valueOf(bg));
        btn.setTextColor(textColor);
    }

    private Button getOptionButton(int idx) {
        if (idx == 0) return binding.btnArenaOpt0;
        if (idx == 1) return binding.btnArenaOpt1;
        if (idx == 2) return binding.btnArenaOpt2;
        if (idx == 3) return binding.btnArenaOpt3;
        return null;
    }

    private void updateArenaScores() {
        if (currentRoom == null) return;
        boolean isBn = LocaleManager.isBengali(context);
        BattlePlayer p1 = null;
        BattlePlayer p2 = null;

        for (BattlePlayer p : currentRoom.getPlayers()) {
            if (p.getId().equals(currentUserId)) {
                p1 = p;
            } else if (p2 == null) {
                p2 = p;
            }
        }

        if (p1 == null) p1 = currentRoom.getHostPlayer();
        binding.tvArenaP1Score.setText((isBn ? BengaliNumberUtil.toBengali(p1.getScore()) : String.valueOf(p1.getScore())) + (isBn ? " পয়েন্ট" : " pts"));

        if (p2 != null) {
            String p2Name = p2.getName();
            if (p2Name != null) {
                p2Name = p2Name.replace("(আপনি)", "").replace("(You)", "").trim();
            }
            if (p2Name == null || p2Name.isEmpty() || p2Name.equalsIgnoreCase("Anonymous")) {
                p2Name = isBn ? "খেলোয়াড় ২" : "Player 2";
            }
            binding.tvArenaP2Name.setText(p2Name);
            binding.tvArenaP2Score.setText((isBn ? BengaliNumberUtil.toBengali(p2.getScore()) : String.valueOf(p2.getScore())) + (isBn ? " পয়েন্ট" : " pts"));
        } else if (currentRoom.getPlayers().size() > 1) {
            BattlePlayer other = currentRoom.getPlayers().get(1);
            String otherName = other.getName();
            if (otherName != null) {
                otherName = otherName.replace("(আপনি)", "").replace("(You)", "").trim();
            }
            if (otherName == null || otherName.isEmpty() || otherName.equalsIgnoreCase("Anonymous")) {
                otherName = isBn ? "খেলোয়াড় ২" : "Player 2";
            }
            binding.tvArenaP2Name.setText(otherName);
            binding.tvArenaP2Score.setText((isBn ? BengaliNumberUtil.toBengali(other.getScore()) : String.valueOf(other.getScore())) + (isBn ? " পয়েন্ট" : " pts"));
        }
    }

    // ==================== SCREEN 6: VICTORY PODIUM ====================
    private void setupScreen6Podium() {
        boolean isBn = LocaleManager.isBengali(context);
        binding.btnFinalRematch.setText(isBn ? "আবার খেলুন" : "Play Again");
        binding.btnFinalBackHome.setText(isBn ? "হোম পেজে ফিরুন" : "Back to Home");

        binding.btnFinalRematch.setOnClickListener(v -> {
            if (currentRoom != null) {
                openLobbyAsHost(currentRoom);
            }
        });

        binding.btnFinalBackHome.setOnClickListener(v -> {
            sessionStorage.clearActiveSession();
            currentRoom = null;
            showScreen(ScreenState.LANDING);
        });
    }

    // ==================== SCREEN MANAGER ====================
    private void showScreen(ScreenState state) {
        this.currentScreen = state;
        boolean isBn = LocaleManager.isBengali(context);
        applyLocalization();

        binding.screen1Landing.setVisibility(state == ScreenState.LANDING ? View.VISIBLE : View.GONE);
        binding.screen2Topics.setVisibility(state == ScreenState.TOPIC_SELECTION ? View.VISIBLE : View.GONE);
        binding.screen3Config.setVisibility(state == ScreenState.CONFIG ? View.VISIBLE : View.GONE);
        binding.screen4Lobby.setVisibility(state == ScreenState.LOBBY ? View.VISIBLE : View.GONE);
        binding.screen5Arena.setVisibility(state == ScreenState.ARENA ? View.VISIBLE : View.GONE);
        binding.screen6Podium.setVisibility(state == ScreenState.PODIUM ? View.VISIBLE : View.GONE);

        switch (state) {
            case LANDING:
                refreshActiveRoomBanner();
                binding.tvBattleTopTitle.setText(isBn ? "নলেজ ব্যাটেল" : "Knowledge Battle");
                binding.btnLobbyShare.setVisibility(View.GONE);
                binding.btnTopActionHistory.setVisibility(View.VISIBLE);
                break;
            case TOPIC_SELECTION:
                binding.tvBattleTopTitle.setText(isBn ? "টপিক নির্বাচন করুন" : "Select Topic");
                binding.btnLobbyShare.setVisibility(View.GONE);
                binding.btnTopActionHistory.setVisibility(View.GONE);
                break;
            case CONFIG:
                binding.tvBattleTopTitle.setText(isBn ? "ব্যাটেল কনফিগার" : "Battle Configuration");
                binding.btnLobbyShare.setVisibility(View.GONE);
                binding.btnTopActionHistory.setVisibility(View.GONE);
                break;
            case LOBBY:
                binding.tvBattleTopTitle.setText(isBn ? "অপেক্ষমাণ কক্ষ" : "Waiting Lobby");
                binding.btnLobbyShare.setVisibility(View.VISIBLE);
                binding.btnTopActionHistory.setVisibility(View.GONE);
                break;
            case ARENA:
                binding.tvBattleTopTitle.setText(isBn ? "লাইভ ব্যাটেল" : "Live Battle");
                binding.btnLobbyShare.setVisibility(View.GONE);
                binding.btnTopActionHistory.setVisibility(View.GONE);
                break;
            case PODIUM:
                binding.tvBattleTopTitle.setText(isBn ? "ফলাফল ও র‍্যাঙ্কিং" : "Results & Ranking");
                binding.btnLobbyShare.setVisibility(View.GONE);
                binding.btnTopActionHistory.setVisibility(View.GONE);
                break;
        }
    }
}
