package com.example.wildkarrde;

import android.app.Application;

import androidx.lifecycle.*;

import java.util.List;

public class ReoccViewModel extends AndroidViewModel {
    private ReoccRepository reoccRepository;

    private LiveData<List<Reocc>> AllReocc_tasks;

    public ReoccViewModel (Application application) {
        super(application);
        reoccRepository = new ReoccRepository(application);
        AllReocc_tasks = reoccRepository.getAllReocc_tasks();
    }

    LiveData<List<Reocc>> getAllReocc_tasks() { return AllReocc_tasks; }

    public void insert(Reocc reocc) { reoccRepository.insert(reocc); }
}
