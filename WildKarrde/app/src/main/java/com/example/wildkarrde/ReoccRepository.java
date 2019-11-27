package com.example.wildkarrde;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReoccRepository {
    private ReoccDao reoccDao;
    private LiveData<List<Reocc>> AllReocc_tasks;

    // Note that in order to unit test the ReminderRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    ReoccRepository(Application application) {
        ReoccRoomDatabase db = ReoccRoomDatabase.getDatabase(application);
        reoccDao = db.reoccDao();
        AllReocc_tasks = reoccDao.getReocc_tasks();

    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Reocc>> getAllReocc_tasks() {
        return AllReocc_tasks;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Reocc reocc) {
        ReoccRoomDatabase.databaseWriteExecutor.execute(() -> {
            reoccDao.insert(reocc);
        });
    }
}
