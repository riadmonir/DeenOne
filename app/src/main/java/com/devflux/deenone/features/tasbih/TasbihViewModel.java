package com.devflux.deenone.features.tasbih;

import android.app.Application;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.TasbihDao;
import com.devflux.deenone.data.local.entity.TasbihEntity;

import java.util.List;

/**
 * TasbihViewModel — Manages the complete Tasbih counter lifecycle.
 *
 * Features:
 *  - Load all dhikr presets from Room
 *  - Counter: increment, undo (decrement), reset
 *  - Cycle tracking (when count reaches target, auto-reset + increment cycle count)
 *  - Vibration feedback on each tap
 *  - Target change (33 / 99 / 100 / 1000 / custom)
 *  - Lifetime total tracking
 *  - Custom dhikr add / delete
 */
public class TasbihViewModel extends AndroidViewModel {

    private final TasbihDao tasbihDao;
    private final LiveData<List<TasbihEntity>> allDhikrList;
    private final LiveData<Long> totalLifetimeCount;

    // Currently selected dhikr
    private final MutableLiveData<TasbihEntity> selectedDhikr = new MutableLiveData<>();

    // Cycle complete event (one-shot)
    private final MutableLiveData<Boolean> cycleCompletedEvent = new MutableLiveData<>(false);

    private Vibrator vibrator;

    public TasbihViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        tasbihDao = db.tasbihDao();
        allDhikrList = tasbihDao.getAllDhikr();
        totalLifetimeCount = tasbihDao.getTotalLifetimeDhikrCount();

        vibrator = (Vibrator) application.getSystemService(Application.VIBRATOR_SERVICE);
    }

    // --- Observables ---

    public LiveData<List<TasbihEntity>> getAllDhikr() {
        return allDhikrList;
    }

    public LiveData<TasbihEntity> getSelectedDhikr() {
        return selectedDhikr;
    }

    public LiveData<Long> getTotalLifetimeCount() {
        return totalLifetimeCount;
    }

    public LiveData<Boolean> getCycleCompletedEvent() {
        return cycleCompletedEvent;
    }

    // --- Selection ---

    public void selectDhikr(TasbihEntity entity) {
        selectedDhikr.setValue(entity);
    }

    // --- Counter Actions ---

    public void increment() {
        TasbihEntity current = selectedDhikr.getValue();
        if (current == null) return;

        // Vibrate if enabled
        if (current.isVibrationEnabled()) {
            vibrate(30);
        }

        int newCount = current.getCurrentCount() + 1;

        if (newCount >= current.getTargetCount()) {
            // Cycle complete: reset count, increment cycles, add to lifetime
            current.setCurrentCount(0);
            current.setCompletedCycles(current.getCompletedCycles() + 1);
            current.setTotalLifetimeCount(current.getTotalLifetimeCount() + current.getTargetCount());
            cycleCompletedEvent.setValue(true);
            vibrate(200); // Strong vibration on completion
        } else {
            current.setCurrentCount(newCount);
        }

        selectedDhikr.setValue(current);
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.update(current));
    }

    public void decrement() {
        TasbihEntity current = selectedDhikr.getValue();
        if (current == null || current.getCurrentCount() <= 0) return;

        current.setCurrentCount(current.getCurrentCount() - 1);
        selectedDhikr.setValue(current);
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.update(current));
    }

    public void reset() {
        TasbihEntity current = selectedDhikr.getValue();
        if (current == null) return;

        current.setCurrentCount(0);
        selectedDhikr.setValue(current);
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.updateCount(current.getId(), 0));
    }

    public void resetCycles() {
        TasbihEntity current = selectedDhikr.getValue();
        if (current == null) return;

        current.setCurrentCount(0);
        current.setCompletedCycles(0);
        selectedDhikr.setValue(current);
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.update(current));
    }

    // --- Target ---

    public void setTarget(int target) {
        TasbihEntity current = selectedDhikr.getValue();
        if (current == null) return;

        current.setTargetCount(target);
        current.setCurrentCount(0);
        selectedDhikr.setValue(current);
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.update(current));
    }

    // --- Settings ---

    public void setVibration(boolean enabled) {
        TasbihEntity current = selectedDhikr.getValue();
        if (current == null) return;
        current.setVibrationEnabled(enabled);
        selectedDhikr.setValue(current);
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.setVibration(current.getId(), enabled));
    }

    public void setSound(boolean enabled) {
        TasbihEntity current = selectedDhikr.getValue();
        if (current == null) return;
        current.setSoundEnabled(enabled);
        selectedDhikr.setValue(current);
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.setSound(current.getId(), enabled));
    }

    // --- Custom Dhikr ---

    public void addCustomDhikr(String arabic, String bengali, int target) {
        TasbihEntity custom = new TasbihEntity(
                arabic, "", bengali, "",
                0, target, 0, 0, true, false, 99
        );
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.insert(custom));
    }

    public void deleteDhikr(TasbihEntity entity) {
        AppDatabase.databaseWriteExecutor.execute(() -> tasbihDao.deleteById(entity.getId()));
    }

    // --- Vibration helper ---

    private void vibrate(long ms) {
        if (vibrator == null || !vibrator.hasVibrator()) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(ms);
        }
    }

    public void acknowledgeCycleComplete() {
        cycleCompletedEvent.setValue(false);
    }
}
