package com.example.wildkarrde;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class ReminderRepository {

    private ReminderDao reminderDao;
    private LiveData<List<Reminder>> AllReminders;

    // Note that in order to unit test the ReminderRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    ReminderRepository(Application application) {
        ReminderRoomDatabase db = ReminderRoomDatabase.getDatabase(application);
        reminderDao = db.reminderDao();
        AllReminders = reminderDao.getReminders();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Reminder>> getAllReminders() {
        return AllReminders;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Reminder reminder) {
        ReminderRoomDatabase.databaseWriteExecutor.execute(() -> {
            reminderDao.insert(reminder);
        });
    }
}
