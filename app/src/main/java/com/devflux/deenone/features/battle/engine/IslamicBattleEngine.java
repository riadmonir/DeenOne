package com.devflux.deenone.features.battle.engine;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.battle.model.BattleRoom;
import com.devflux.deenone.features.battle.repository.KnowledgeBattleRepository;
import com.devflux.deenone.features.battle.service.AnswerSubmissionRequest;
import com.devflux.deenone.features.battle.service.BattleRoomServerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 100% Logical and Robust Islamic Battle Engine.
 * Supports dynamic player identification (Host and Guests), server-authoritative score calculations,
 * authentic question streaming, and seamless round transitions.
 */
public class IslamicBattleEngine {

    public interface BattleEngineListener {
        void onCountdownTick(int secondsRemaining);
        void onRoundStarted(int questionIndex, BattleQuestion question, int totalQuestions);
        void onRoundTimerTick(int secondsRemaining, int totalDuration);
        void onPlayerAnswered(BattlePlayer player, int optionSelected, boolean isCorrect, int pointsDelta);
        void onRoundEnded(BattleQuestion question, int userOption, int botOption);
        void onMatchFinished(List<BattlePlayer> rankedPlayers, List<BattleQuestion> questionReviewList, int netPointsEarned);
    }

    private static final long ROUND_TRANSITION_DELAY_MS = 1800L;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();

    private BattleRoom currentRoom;
    private String localPlayerId;
    private BattleEngineListener listener;

    private int currentRoundTimerSec = 15;
    private Runnable roundTimerRunnable;
    private Runnable nextQuestionTransitionRunnable;
    private final List<Runnable> opponentRunnables = new ArrayList<>();

    private int userSelectedOption = -1;
    private boolean isRoundActive = false;

    public void startBattle(BattleRoom room, String localPlayerId, BattleEngineListener listener) {
        this.currentRoom = room;
        this.localPlayerId = localPlayerId;
        this.listener = listener;

        if (room.getQuestions().isEmpty()) {
            List<BattleQuestion> qList = KnowledgeBattleRepository.getQuestionsForMatch(
                    room.getConfig().getCategoryId(),
                    room.getConfig().getTotalQuestions(),
                    room.getUsedQuestionIds());
            room.setQuestions(qList);
        }

        room.setState(BattleRoom.RoomState.IN_BATTLE);
        room.setCurrentQuestionIndex(0);
        startRound(0);
    }

    public void startBattle(BattleRoom room, BattleEngineListener listener) {
        String defaultUserId = (room != null && room.getHostPlayer() != null) ? room.getHostPlayer().getId() : "";
        startBattle(room, defaultUserId, listener);
    }

    private void startRound(int index) {
        if (currentRoom == null || index >= currentRoom.getQuestions().size()) {
            finishMatch();
            return;
        }

        clearOpponentRunnables();
        userSelectedOption = -1;
        isRoundActive = true;

        BattleQuestion currentQ = currentRoom.getQuestions().get(index);
        currentRoundTimerSec = Math.max(10, currentRoom.getConfig().getTimePerQuestionSec());

        if (listener != null) {
            listener.onRoundStarted(index, currentQ, currentRoom.getConfig().getTotalQuestions());
            listener.onRoundTimerTick(currentRoundTimerSec, currentRoundTimerSec);
        }

        scheduleOpponentSimulatedAnswers(currentQ);
        startRoundTimer();
    }

    private void startRoundTimer() {
        if (roundTimerRunnable != null) handler.removeCallbacks(roundTimerRunnable);

        roundTimerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isRoundActive) return;

                if (currentRoundTimerSec > 0) {
                    currentRoundTimerSec--;
                    if (listener != null && currentRoom != null) {
                        listener.onRoundTimerTick(currentRoundTimerSec, currentRoom.getConfig().getTimePerQuestionSec());
                    }
                    handler.postDelayed(this, 1000);
                } else {
                    endCurrentRoundInstant();
                }
            }
        };
        handler.postDelayed(roundTimerRunnable, 1000);
    }

    private void scheduleOpponentSimulatedAnswers(BattleQuestion q) {
        if (currentRoom == null || currentRoom.getPlayers().size() <= 1) return;

        for (BattlePlayer player : currentRoom.getPlayers()) {
            if (player.getId().equals(localPlayerId)) continue; // Skip local user
            if (!player.isBot()) continue; // Real human opponent on another phone! Wait for real answer via cloud sync.

            long delay = 2000L + random.nextInt(Math.max(1000, Math.min(6000, (currentRoundTimerSec - 2) * 1000)));
            Runnable task = () -> {
                if (!isRoundActive || currentRoom == null) return;
                boolean isCorrect = random.nextDouble() < 0.70;
                int chosenOpt = isCorrect ? q.getCorrectOptionIndex() : (q.getCorrectOptionIndex() + 1 + random.nextInt(3)) % 4;
                int points = isCorrect ? 10 : (currentRoom.getConfig().isNegativeMarkingEnabled() ? -5 : 0);

                player.addScore(points);
                if (isCorrect) player.incrementCorrect();
                else player.incrementWrong();

                if (listener != null) {
                    listener.onPlayerAnswered(player, chosenOpt, isCorrect, points);
                }
            };
            opponentRunnables.add(task);
            handler.postDelayed(task, delay);
        }
    }

    private void clearOpponentRunnables() {
        for (Runnable r : opponentRunnables) {
            handler.removeCallbacks(r);
        }
        opponentRunnables.clear();
    }

    /**
     * Real Player Answer Submission & Server-Side Validation.
     */
    public synchronized void submitUserAnswer(Context context, String playerId, int optionIndex) {
        if (!isRoundActive || userSelectedOption != -1 || currentRoom == null) return;
        userSelectedOption = optionIndex;

        if (roundTimerRunnable != null) handler.removeCallbacks(roundTimerRunnable);

        BattleQuestion q = currentRoom.getQuestions().get(currentRoom.getCurrentQuestionIndex());

        BattlePlayer user = null;
        for (BattlePlayer p : currentRoom.getPlayers()) {
            if (p.getId().equals(playerId)) {
                user = p;
                break;
            }
        }
        if (user == null) {
            user = currentRoom.getHostPlayer();
        }

        AnswerSubmissionRequest req = new AnswerSubmissionRequest(
                currentRoom.getRoomCode(),
                user.getId(),
                q.getId(),
                optionIndex
        );

        final BattlePlayer finalUser = user;
        BattleRoomServerManager.getInstance().validateAnswer(context, req, response -> {
            if (response.isSuccess()) {
                if (listener != null) {
                    listener.onPlayerAnswered(finalUser, optionIndex, response.isCorrect(), response.getPointsDelta());
                }
            }
            endCurrentRoundInstant();
        });
    }

    public void submitUserAnswer(Context context, int optionIndex) {
        submitUserAnswer(context, localPlayerId, optionIndex);
    }

    private synchronized void endCurrentRoundInstant() {
        if (!isRoundActive) return;
        isRoundActive = false;

        if (roundTimerRunnable != null) handler.removeCallbacks(roundTimerRunnable);
        clearOpponentRunnables();

        if (currentRoom != null && currentRoom.getCurrentQuestionIndex() < currentRoom.getQuestions().size()) {
            BattleQuestion q = currentRoom.getQuestions().get(currentRoom.getCurrentQuestionIndex());
            if (listener != null) {
                listener.onRoundEnded(q, userSelectedOption, -1);
            }
        }

        if (nextQuestionTransitionRunnable != null) handler.removeCallbacks(nextQuestionTransitionRunnable);
        nextQuestionTransitionRunnable = () -> {
            if (currentRoom != null) {
                int nextIndex = currentRoom.getCurrentQuestionIndex() + 1;
                currentRoom.setCurrentQuestionIndex(nextIndex);
                startRound(nextIndex);
            }
        };
        handler.postDelayed(nextQuestionTransitionRunnable, ROUND_TRANSITION_DELAY_MS);
    }

    private void finishMatch() {
        if (currentRoom == null) return;
        currentRoom.setState(BattleRoom.RoomState.PODIUM_FINISHED);

        List<BattlePlayer> ranked = new ArrayList<>(currentRoom.getPlayers());
        Collections.sort(ranked, (p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        int netXp = 25;
        for (BattlePlayer p : currentRoom.getPlayers()) {
            if (p.getId().equals(localPlayerId)) {
                netXp = Math.max(10, p.getScore());
                break;
            }
        }

        if (listener != null) {
            listener.onMatchFinished(ranked, currentRoom.getQuestions(), netXp);
        }
    }

    public void destroy() {
        isRoundActive = false;
        if (handler != null) {
            if (roundTimerRunnable != null) handler.removeCallbacks(roundTimerRunnable);
            if (nextQuestionTransitionRunnable != null) handler.removeCallbacks(nextQuestionTransitionRunnable);
            clearOpponentRunnables();
        }
    }
}
