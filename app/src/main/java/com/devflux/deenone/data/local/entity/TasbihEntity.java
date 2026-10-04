package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * TasbihEntity — Represents a dhikr preset (SubhanAllah, Alhamdulillah, etc.)
 * with its current counter state. Data is persisted locally between sessions.
 */
@Entity(tableName = "tasbih_logs")
public class TasbihEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    /** Arabic text: سُبْحَانَ اللَّهِ */
    private String dhikrArabic;

    /** Transliteration: SubhanAllah */
    private String dhikrTransliteration;

    /** Bengali label: সুবহানাল্লাহ */
    private String dhikrBengali;

    /** English label: SubhanAllah */
    private String dhikrEnglish;

    /** Current session count */
    private int currentCount;

    /** Target count: 33, 99, 100, 1000, or custom */
    private int targetCount;

    /** Lifetime accumulated count across all sessions */
    private long totalLifetimeCount;

    /** Number of completed cycles (currentCount reached targetCount) */
    private int completedCycles;

    /** Whether vibration is enabled for this dhikr */
    private boolean vibrationEnabled;

    /** Whether click sound is enabled */
    private boolean soundEnabled;

    /** Sort order / position in list */
    private int sortOrder;

    public TasbihEntity() {}

    @Ignore
    public TasbihEntity(String dhikrArabic, String dhikrTransliteration,
                        String dhikrBengali, String dhikrEnglish,
                        int currentCount, int targetCount,
                        long totalLifetimeCount, int completedCycles,
                        boolean vibrationEnabled, boolean soundEnabled,
                        int sortOrder) {
        this.dhikrArabic = dhikrArabic;
        this.dhikrTransliteration = dhikrTransliteration;
        this.dhikrBengali = dhikrBengali;
        this.dhikrEnglish = dhikrEnglish;
        this.currentCount = currentCount;
        this.targetCount = targetCount;
        this.totalLifetimeCount = totalLifetimeCount;
        this.completedCycles = completedCycles;
        this.vibrationEnabled = vibrationEnabled;
        this.soundEnabled = soundEnabled;
        this.sortOrder = sortOrder;
    }

    // --- Getters & Setters ---

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDhikrArabic() { return dhikrArabic; }
    public void setDhikrArabic(String dhikrArabic) { this.dhikrArabic = dhikrArabic; }

    public String getDhikrTransliteration() { return dhikrTransliteration; }
    public void setDhikrTransliteration(String dhikrTransliteration) { this.dhikrTransliteration = dhikrTransliteration; }

    public String getDhikrBengali() { return dhikrBengali; }
    public void setDhikrBengali(String dhikrBengali) { this.dhikrBengali = dhikrBengali; }

    public String getDhikrEnglish() { return dhikrEnglish; }
    public void setDhikrEnglish(String dhikrEnglish) { this.dhikrEnglish = dhikrEnglish; }

    public int getCurrentCount() { return currentCount; }
    public void setCurrentCount(int currentCount) { this.currentCount = currentCount; }

    public int getTargetCount() { return targetCount; }
    public void setTargetCount(int targetCount) { this.targetCount = targetCount; }

    public long getTotalLifetimeCount() { return totalLifetimeCount; }
    public void setTotalLifetimeCount(long totalLifetimeCount) { this.totalLifetimeCount = totalLifetimeCount; }

    public int getCompletedCycles() { return completedCycles; }
    public void setCompletedCycles(int completedCycles) { this.completedCycles = completedCycles; }

    public boolean isVibrationEnabled() { return vibrationEnabled; }
    public void setVibrationEnabled(boolean vibrationEnabled) { this.vibrationEnabled = vibrationEnabled; }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
