package com.example.wildkarrde;

import android.app.Application;

import androidx.lifecycle.*;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private ReminderRepository reminderRepository;

    private LiveData<List<Reminder>> AllReminders;

    public ReminderViewModel (Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
        AllReminders = reminderRepository.getAllReminders();
    }

    LiveData<List<Reminder>> getAllReminders() { return AllReminders; }

    public void insert(Reminder reminder) { reminderRepository.insert(reminder); }
}
