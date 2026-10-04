package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.BloodDonorEntity;

import java.util.List;

@Dao
public interface BloodDonorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateDonor(BloodDonorEntity donor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BloodDonorEntity> donors);

    @Query("SELECT * FROM blood_donors ORDER BY registrationTimestamp DESC")
    LiveData<List<BloodDonorEntity>> getAllDonorsLiveData();

    @Query("SELECT * FROM blood_donors ORDER BY registrationTimestamp DESC")
    List<BloodDonorEntity> getAllDonorsSync();

    @Query("SELECT * FROM blood_donors WHERE bloodGroup = :group ORDER BY registrationTimestamp DESC")
    LiveData<List<BloodDonorEntity>> getDonorsByGroupLiveData(String group);

    @Query("SELECT * FROM blood_donors WHERE bloodGroup = :group ORDER BY registrationTimestamp DESC")
    List<BloodDonorEntity> getDonorsByGroupSync(String group);

    @Query("SELECT * FROM blood_donors WHERE userId = :userId LIMIT 1")
    BloodDonorEntity getDonorByUserIdSync(String userId);

    @Query("SELECT COUNT(*) FROM blood_donors")
    int getDonorCount();

    @Query("DELETE FROM blood_donors WHERE donorId = :donorId")
    void deleteDonor(String donorId);

    @Query("DELETE FROM blood_donors WHERE donorId IN ('donor_1', 'donor_2', 'donor_3') OR userId IN ('usr_abdullah', 'usr_salman', 'usr_tariq', 'usr_fatima', 'usr_rakib', 'usr_sumon', 'usr_nusrat', 'usr_tanvir', 'usr_arif', 'usr_farhana', 'usr_zahid') OR phone IN ('01711111111', '01822222222', '01933333333', '01812345678', '01987654321', '01555667788', '01711000001', '01811000002', '01911000003', '01611000004', '01511000005', '01711000006', '01811000007', '01911000008')")
    void deleteDummyDonors();

    @Query("DELETE FROM blood_donors WHERE userId = :userId")
    void deleteDonorByUserId(String userId);

    @Query("DELETE FROM blood_donors")
    void deleteAllDonors();

    @Query("DELETE FROM blood_donors WHERE userId != :userId AND (:userId IS NOT NULL AND :userId != '')")
    void deleteDonorsExceptUser(String userId);
}
