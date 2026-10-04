package com.devflux.deenone.core.goals;

import org.json.JSONException;
import org.json.JSONObject;

public class IslamicGoalItem {

  public enum Category {
    QURAN("কুরআন তিলাওয়াত", "#10B981"),
    SALAH("নামাজ ও জামাত", "#34D399"),
    DHIKR("জিকির ও তাসবিহ", "#F59E0B"),
    DUA("মাসনূন দোয়া", "#60A5FA"),
    AMAL("সুন্নাত ও আমল", "#A78BFA"),
    HADITH("সহীহ হাদিস", "#F472B6"),
    FASTING("রোজা ও সিয়াম", "#38BDF8"),
    KHATM("কুরআন খতম", "#34D399");

    public final String displayName;
    public final String colorHex;

    Category(String displayName, String colorHex) {
      this.displayName = displayName;
      this.colorHex = colorHex;
    }
  }

  public String id;
  public Category category;
  public String title;
  public String targetUnit;
  public int dailyTarget;
  public int weeklyTarget;
  public int currentProgress;
  public int streakDays;
  public boolean isCompletedToday;
  public boolean isReminderEnabled;
  public String reminderTime;
  public String lastCompletedDate;
  public long createdTimestamp;

  public IslamicGoalItem(String id, Category category, String title, String targetUnit,
              int dailyTarget, int weeklyTarget, int currentProgress, int streakDays,
              boolean isCompletedToday, boolean isReminderEnabled, String reminderTime,
              String lastCompletedDate, long createdTimestamp) {
    this.id = id;
    this.category = category;
    this.title = title;
    this.targetUnit = targetUnit;
    this.dailyTarget = dailyTarget;
    this.weeklyTarget = weeklyTarget;
    this.currentProgress = currentProgress;
    this.streakDays = streakDays;
    this.isCompletedToday = isCompletedToday;
    this.isReminderEnabled = isReminderEnabled;
    this.reminderTime = reminderTime;
    this.lastCompletedDate = lastCompletedDate;
    this.createdTimestamp = createdTimestamp;
  }

  public int getProgressPercentage() {
    if (dailyTarget <= 0) return 0;
    return Math.min(100, (int) Math.round(((double) currentProgress / dailyTarget) * 100));
  }

  public JSONObject toJson() {
    JSONObject obj = new JSONObject();
    try {
      obj.put("id", id);
      obj.put("category", category.name());
      obj.put("title", title);
      obj.put("targetUnit", targetUnit);
      obj.put("dailyTarget", dailyTarget);
      obj.put("weeklyTarget", weeklyTarget);
      obj.put("currentProgress", currentProgress);
      obj.put("streakDays", streakDays);
      obj.put("isCompletedToday", isCompletedToday);
      obj.put("isReminderEnabled", isReminderEnabled);
      obj.put("reminderTime", reminderTime);
      obj.put("lastCompletedDate", lastCompletedDate);
      obj.put("createdTimestamp", createdTimestamp);
    } catch (JSONException ignored) {}
    return obj;
  }

  public static IslamicGoalItem fromJson(JSONObject obj) {
    try {
      String id = obj.getString("id");
      Category cat = Category.valueOf(obj.getString("category"));
      String title = obj.getString("title");
      String unit = obj.optString("targetUnit", "বার");
      int daily = obj.getInt("dailyTarget");
      int weekly = obj.optInt("weeklyTarget", daily * 7);
      int prog = obj.optInt("currentProgress", 0);
      int streak = obj.optInt("streakDays", 0);
      boolean completed = obj.optBoolean("isCompletedToday", false);
      boolean reminder = obj.optBoolean("isReminderEnabled", false);
      String reminderTime = obj.optString("reminderTime", "06:00 AM");
      String lastDate = obj.optString("lastCompletedDate", "");
      long ts = obj.optLong("createdTimestamp", System.currentTimeMillis());

      return new IslamicGoalItem(id, cat, title, unit, daily, weekly, prog, streak, completed, reminder, reminderTime, lastDate, ts);
    } catch (Exception e) {
      return null;
    }
  }
}