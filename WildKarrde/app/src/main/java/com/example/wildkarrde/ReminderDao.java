package com.example.wildkarrde;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface ReminderDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Reminder word);

    /* Occurs upon a successful connection made to the server,
    so that new insertions can be made with up-to-date info
     */
    @Query("DELETE FROM reminders")
    void deleteAll();


    //(do livedata from Android Room with A View tutorial if needed)
    /* This will be called whenever a user selects
    view reminders. Can either occur after a successful
    server connection, or it will simply display what was last
    retrieved if the user does not have internet access.
     */
    @Query("SELECT * from reminders")
    LiveData<List<Reminder>> getReminders();
}
