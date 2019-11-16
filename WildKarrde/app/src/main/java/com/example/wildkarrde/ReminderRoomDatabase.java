package com.example.wildkarrde;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.*;

@Database(entities = {Reminder.class}, version = 1, exportSchema = false)
public abstract class ReminderRoomDatabase extends RoomDatabase{
    public abstract ReminderDao reminderDao();

    private static volatile ReminderRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ReminderRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ReminderRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ReminderRoomDatabase.class, "reminder_database").
                            addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
