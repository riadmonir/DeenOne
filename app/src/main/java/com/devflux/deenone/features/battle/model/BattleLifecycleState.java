package com.devflux.deenone.features.battle.model;

public enum BattleLifecycleState {
    CREATED("রুম তৈরি হয়েছে"),
    WAITING_FOR_PLAYERS("খেলোয়াড়দের জন্য অপেক্ষা"),
    PLAYERS_JOINED("সব খেলোয়াড় যুক্ত হয়েছে"),
    READY_CHECK("রেডি চেক যাচাই"),
    COUNTDOWN("কাউন্টডাউন চলছে"),
    LIVE("লাইভ যুদ্ধক্ষেত্র"),
    COMPLETED("ব্যাটেল সম্পন্ন"),
    RESULT("ফলাফল ও র‍্যাঙ্কিং"),
    EXPIRED("রুমের মেয়াদ শেষ");

    private final String labelBn;

    BattleLifecycleState(String labelBn) {
        this.labelBn = labelBn;
    }

    public String getLabelBn() {
        return labelBn;
    }
}
