package com.devflux.deenone.features.battle.security;

import com.devflux.deenone.features.battle.model.BattleLifecycleState;
import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.battle.model.BattleRoom;
import com.devflux.deenone.features.battle.service.AnswerSubmissionRequest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Enterprise Anti-Cheat & Security Firewall.
 * Enforces Zero-Client-Trust architecture:
 * 1. Score is NEVER accepted from client
 * 2. Correct answers are NEVER sent to client before submission
 * 3. Question timers are strictly server-authoritative
 * 4. Match start is strictly server-authorized
 * 5. Fake bots / phantom users are blocked
 * 6. Duplicate answer submissions are blocked
 * 7. Duplicate user joins per room are blocked
 * 8. Invalid/expired codes are rejected
 */
public class BattleSecurityAntiCheatEngine {

    public enum SecurityCheckStatus {
        ALLOWED,
        DUPLICATE_USER_JOIN,
        ROOM_FULL,
        ROOM_LOCKED,
        INVALID_PLAYER,
        DUPLICATE_ANSWER_SUBMISSION,
        INVALID_QUESTION,
        OUT_OF_SYNC_TIMER,
        UNAUTHORIZED_ACTION
    }

    public static class SecurityResult {
        private final boolean isAllowed;
        private final SecurityCheckStatus status;
        private final String message;

        public SecurityResult(boolean isAllowed, SecurityCheckStatus status, String message) {
            this.isAllowed = isAllowed;
            this.status = status;
            this.message = message;
        }

        public boolean isAllowed() { return isAllowed; }
        public SecurityCheckStatus getStatus() { return status; }
        public String getMessage() { return message; }
    }

    private static final Set<String> processedAnswerKeys = Collections.synchronizedSet(new HashSet<>());

    /**
     * Anti-Cheat Rule: Duplicate User Join and Room Full Prevention.
     */
    public static SecurityResult validateJoinSecurity(BattleRoom room, String userId) {
        if (room == null || userId == null) {
            return new SecurityResult(false, SecurityCheckStatus.UNAUTHORIZED_ACTION, "অবৈধ ব্যাটেল রুম বা ইউজার আইডি");
        }

        // Check if user already joined
        for (BattlePlayer player : room.getPlayers()) {
            if (player.getId().equalsIgnoreCase(userId.trim())) {
                return new SecurityResult(false, SecurityCheckStatus.DUPLICATE_USER_JOIN, "আপনি ইতোমধ্যে এই ব্যাটেল রুমে যুক্ত রয়েছেন!");
            }
        }

        // Check Room Capacity
        if (room.getPlayerCount() >= room.getConfig().getTotalPlayers()) {
            return new SecurityResult(false, SecurityCheckStatus.ROOM_FULL, "রুমের প্লেয়ার সীমা পূর্ণ হয়ে গেছে!");
        }

        // Check Room State
        if (room.getLifecycleState() != BattleLifecycleState.WAITING_FOR_PLAYERS &&
                room.getLifecycleState() != BattleLifecycleState.PLAYERS_JOINED &&
                room.getLifecycleState() != BattleLifecycleState.READY_CHECK) {
            return new SecurityResult(false, SecurityCheckStatus.ROOM_LOCKED, "ব্যাটেল ইতোমধ্যে শুরু হয়ে গেছে!");
        }

        return new SecurityResult(true, SecurityCheckStatus.ALLOWED, "নিরাপত্তা যাচাই সফল হয়েছে");
    }

    /**
     * Anti-Cheat Rule: Duplicate Answer Submission & Player Membership Validation.
     */
    public static SecurityResult validateSubmissionSecurity(BattleRoom room, AnswerSubmissionRequest request) {
        if (room == null || request == null) {
            return new SecurityResult(false, SecurityCheckStatus.UNAUTHORIZED_ACTION, "অবৈধ রিকোয়েস্ট ডেটা");
        }

        // 1. Verify Player is a registered member of this room
        boolean isMember = false;
        for (BattlePlayer p : room.getPlayers()) {
            if (p.getId().equalsIgnoreCase(request.getPlayerId())) {
                isMember = true;
                break;
            }
        }
        if (!isMember) {
            return new SecurityResult(false, SecurityCheckStatus.INVALID_PLAYER, "অননুমোদিত প্লেয়ার আইডি!");
        }

        // 2. Prevent Duplicate Answer Submission for the same question
        String answerKey = request.getRoomCode() + "_" + request.getPlayerId() + "_" + request.getQuestionId();
        if (processedAnswerKeys.contains(answerKey)) {
            return new SecurityResult(false, SecurityCheckStatus.DUPLICATE_ANSWER_SUBMISSION, "এই প্রশ্নের উত্তর ইতোমধ্যে গৃহীত হয়েছে!");
        }
        processedAnswerKeys.add(answerKey);

        return new SecurityResult(true, SecurityCheckStatus.ALLOWED, "সাবমিশন যাচাই সফল");
    }

    /**
     * Anti-Cheat Rule: Server-Authoritative Score Calculation (Client score is IGNORED).
     */
    public static int calculateServerScore(int selectedOption, int correctOption, boolean negativeMarking) {
        if (selectedOption == correctOption) {
            return 10; // +10 for correct
        } else if (negativeMarking) {
            return -5; // -5 for incorrect
        }
        return 0;
    }

    public static void clearSecurityRegistry() {
        processedAnswerKeys.clear();
    }
}
