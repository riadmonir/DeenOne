package com.devflux.deenone.core.backend;

import android.content.Context;

import com.devflux.deenone.core.backend.model.AdControlConfig;
import com.devflux.deenone.core.backend.model.AppUpdateInfo;
import com.devflux.deenone.core.backend.model.CommunityMessage;
import com.devflux.deenone.core.backend.model.LeaderboardUser;
import com.devflux.deenone.core.backend.model.UserProfile;

import java.io.File;
import java.util.List;

public class BackendRepository implements IBackendService {

    private static BackendRepository instance;
    private final Context context;
    private final PhpMysqlBackendService phpService;
    private final FirebaseBackendBridge firebaseBridge;

    private BackendRepository(Context context) {
        this.context = context.getApplicationContext();
        this.phpService = new PhpMysqlBackendService(context);
        this.firebaseBridge = new FirebaseBackendBridge(context);
    }

    public static synchronized BackendRepository getInstance(Context context) {
        if (instance == null) {
            instance = new BackendRepository(context);
        }
        return instance;
    }

    private IBackendService getActiveService() {
        BackendProviderType type = BackendConfigManager.getActiveProvider(context);
        if (type == BackendProviderType.FIREBASE) {
            return firebaseBridge;
        } else {
            return phpService;
        }
    }

    @Override
    public void fetchAdConfig(BackendCallback<AdControlConfig> callback) {
        getActiveService().fetchAdConfig(callback);
    }

    @Override
    public void fetchAppUpdateInfo(BackendCallback<AppUpdateInfo> callback) {
        getActiveService().fetchAppUpdateInfo(callback);
    }

    @Override
    public void fetchLeaderboard(BackendCallback<List<LeaderboardUser>> callback) {
        getActiveService().fetchLeaderboard(callback);
    }

    @Override
    public void submitUserPoints(String userId, String userName, int points, BackendCallback<Boolean> callback) {
        getActiveService().submitUserPoints(userId, userName, points, callback);
    }

    @Override
    public void fetchCommunityMessages(String category, BackendCallback<List<CommunityMessage>> callback) {
        getActiveService().fetchCommunityMessages(category, callback);
    }

    @Override
    public void postCommunityMessage(CommunityMessage message, BackendCallback<Boolean> callback) {
        getActiveService().postCommunityMessage(message, callback);
    }

    @Override
    public void fetchUserProfile(String userId, BackendCallback<UserProfile> callback) {
        getActiveService().fetchUserProfile(userId, callback);
    }

    @Override
    public void updateUserProfile(UserProfile profile, BackendCallback<Boolean> callback) {
        getActiveService().updateUserProfile(profile, callback);
    }

    @Override
    public void uploadProfileImage(String userId, File imageFile, BackendCallback<String> callback) {
        getActiveService().uploadProfileImage(userId, imageFile, callback);
    }

    @Override
    public void registerBloodDonor(String userId, String name, String bloodGroup, String phone, String district, BackendCallback<Boolean> callback) {
        getActiveService().registerBloodDonor(userId, name, bloodGroup, phone, district, callback);
    }
}
