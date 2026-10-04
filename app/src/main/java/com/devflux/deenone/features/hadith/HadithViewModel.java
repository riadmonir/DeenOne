package com.devflux.deenone.features.hadith;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.data.local.entity.HadithEntity;
import com.devflux.deenone.data.repository.HadithRepository;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.List;

public class HadithViewModel extends AndroidViewModel {

    private final HadithRepository repository;
    private final MutableLiveData<String> selectedCollection = new MutableLiveData<>("all");
    private final MutableLiveData<String> selectedTopic = new MutableLiveData<>("all");
    private final MutableLiveData<String> selectedLanguage = new MutableLiveData<>("bn"); // "bn", "en", "ur", "ar"
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    private final MutableLiveData<Boolean> isSyncing = new MutableLiveData<>(false);
    private final MutableLiveData<String> syncStatusMessage = new MutableLiveData<>("");

    private final MediatorLiveData<List<HadithEntity>> hadithListLiveData = new MediatorLiveData<>();
    private LiveData<List<HadithEntity>> currentSource = null;

    public HadithViewModel(@NonNull Application application) {
        super(application);
        this.repository = new HadithRepository(application);

        hadithListLiveData.addSource(selectedCollection, col -> updateHadithSource());
        hadithListLiveData.addSource(selectedTopic, top -> updateHadithSource());
        updateHadithSource();
    }

    private void updateHadithSource() {
        String col = selectedCollection.getValue();
        String top = selectedTopic.getValue();

        if (currentSource != null) {
            hadithListLiveData.removeSource(currentSource);
        }

        currentSource = repository.getHadithByCollectionAndTopic(col, top);
        hadithListLiveData.addSource(currentSource, hadithListLiveData::setValue);
    }

    public LiveData<List<HadithEntity>> getHadithList() {
        return hadithListLiveData;
    }

    public LiveData<String> getSelectedLanguage() {
        return selectedLanguage;
    }

    public LiveData<String> getSelectedCollection() {
        return selectedCollection;
    }

    public LiveData<String> getSelectedTopic() {
        return selectedTopic;
    }

    public LiveData<Boolean> getIsSyncing() {
        return isSyncing;
    }

    public LiveData<String> getSyncStatusMessage() {
        return syncStatusMessage;
    }

    public LiveData<Integer> getTotalHadithCount() {
        return repository.getHadithCountLive();
    }

    public void setCollection(String collectionId) {
        selectedCollection.setValue(collectionId);
    }

    public void setTopic(String topic) {
        selectedTopic.setValue(topic);
    }

    public void setLanguage(String langCode) {
        selectedLanguage.setValue(langCode);
    }

    public LiveData<List<HadithEntity>> searchHadith(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getHadithList();
        }
        String col = selectedCollection.getValue();
        return repository.searchHadith(col, query.trim());
    }

    public void toggleBookmark(HadithEntity item) {
        if (item != null) {
            repository.setBookmark(item.getId(), !item.isBookmarked());
        }
    }

    /**
     * Trigger real-time online Hadith sync for next genuine un-synced section.
     * Only inserts genuinely new hadiths into Room, updating the SQLite row count.
     */
    public void triggerOnlineSync(String collectionId) {
        if (Boolean.TRUE.equals(isSyncing.getValue())) return;

        isSyncing.setValue(true);
        syncStatusMessage.setValue("অনলাইন থেকে হাদিস সিঙ্ক হচ্ছে...");

        repository.syncNextHadithsFromOnline(collectionId, new HadithRepository.SyncCallback() {
            @Override
            public void onSyncSuccess(int count) {
                isSyncing.postValue(false);
                if (count > 0) {
                    syncStatusMessage.postValue("অনলাইন থেকে " + BengaliNumberUtil.toBengali(count) + "টি নতুন সহীহ হাদিস সংরক্ষিত হয়েছে");
                } else {
                    syncStatusMessage.postValue("এই কিতাবের সকল হাদিস সংরক্ষিত ও আপ-টু-ডেট আছে");
                }
            }

            @Override
            public void onSyncError(String error) {
                isSyncing.postValue(false);
                syncStatusMessage.postValue("ইন্টারনেট সংযোগ নেই: অফলাইন ডাটাবেজের হাদিস প্রদর্শিত হচ্ছে");
            }
        });
    }

    public void triggerOnlineSync(String collectionId, int sectionId) {
        triggerOnlineSync(collectionId);
    }
}
