package com.devflux.deenone.domain.usecase;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.entity.DailyContentEntity;
import com.devflux.deenone.domain.repository.IContentRepository;

public class GetDailyContentUseCase {

    private final IContentRepository repository;

    public GetDailyContentUseCase(IContentRepository repository) {
        this.repository = repository;
    }

    public LiveData<DailyContentEntity> execute() {
        return repository.getLatestContent();
    }

    public void setDuaRead(long id, boolean isRead) {
        repository.setDuaRead(id, isRead);
    }
}
