package com.devflux.deenone.core.backend;

import com.devflux.deenone.core.backend.model.AdControlConfig;
import com.devflux.deenone.core.backend.model.AppUpdateInfo;
import com.devflux.deenone.core.backend.model.CommunityMessage;
import com.devflux.deenone.core.backend.model.LeaderboardUser;
import com.devflux.deenone.core.backend.model.UserProfile;

import java.io.File;
import java.util.List;

public interface IBackendService {

    interface BackendCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    void fetchAdConfig(BackendCallback<AdControlConfig> callback);

    void fetchAppUpdateInfo(BackendCallback<AppUpdateInfo> callback);

    void fetchLeaderboard(BackendCallback<List<LeaderboardUser>> callback);

    void submitUserPoints(String userId, String userName, int points, BackendCallback<Boolean> callback);

    void fetchCommunityMessages(String category, BackendCallback<List<CommunityMessage>> callback);

    void postCommunityMessage(CommunityMessage message, BackendCallback<Boolean> callback);

    void fetchUserProfile(String userId, BackendCallback<UserProfile> callback);

    void updateUserProfile(UserProfile profile, BackendCallback<Boolean> callback);

    void uploadProfileImage(String userId, File imageFile, BackendCallback<String> callback);

    void registerBloodDonor(String userId, String name, String bloodGroup, String phone, String district, BackendCallback<Boolean> callback);
}
