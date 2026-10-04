package com.devflux.deenone.domain.repository;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.entity.DailyContentEntity;

public interface IContentRepository {
    LiveData<DailyContentEntity> getLatestContent();
    void setDuaRead(long id, boolean isRead);
}
