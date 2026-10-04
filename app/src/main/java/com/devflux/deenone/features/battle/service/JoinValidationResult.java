package com.devflux.deenone.features.battle.service;

import com.devflux.deenone.features.battle.model.BattleRoom;

public class JoinValidationResult {

    public enum Status {
        SUCCESS("সফলভাবে যোগ দিয়েছেন"),
        INVALID_CODE("ভুল কোড! কোনো সক্রিয় ব্যাটেল রুম পাওয়া যায়নি।"),
        ROOM_FULL("এই ব্যাটেল রুমটি ইতিমধ্যে পূর্ণ হয়ে গেছে।"),
        ALREADY_STARTED("এই রুমের খেলা ইতিমধ্যে শুরু হয়ে গেছে।"),
        ROOM_EXPIRED("এই ব্যাটেল কোডের মেয়াদ উত্তীর্ণ হয়ে গেছে।"),
        NETWORK_ERROR("সার্ভারের সাথে সংযোগ স্থাপন করা সম্ভব হয়নি।");

        private final String messageBn;

        Status(String messageBn) {
            this.messageBn = messageBn;
        }

        public String getMessageBn() {
            return messageBn;
        }
    }

    private final Status status;
    private final BattleRoom room;
    private final String message;

    public JoinValidationResult(Status status, BattleRoom room, String message) {
        this.status = status;
        this.room = room;
        this.message = message != null ? message : status.getMessageBn();
    }

    public static JoinValidationResult success(BattleRoom room) {
        return new JoinValidationResult(Status.SUCCESS, room, Status.SUCCESS.getMessageBn());
    }

    public static JoinValidationResult failure(Status status) {
        return new JoinValidationResult(status, null, status.getMessageBn());
    }

    public static JoinValidationResult failure(Status status, String customMessage) {
        return new JoinValidationResult(status, null, customMessage);
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public Status getStatus() {
        return status;
    }

    public BattleRoom getRoom() {
        return room;
    }

    public String getMessage() {
        return message;
    }
}
