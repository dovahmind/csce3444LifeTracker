package com.example.wildkarrde;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.*;


@Database(entities = {Reocc.class}, version = 1, exportSchema = false)
public abstract class ReoccRoomDatabase extends RoomDatabase {

    public abstract ReoccDao reoccDao();

    private static volatile ReoccRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ReoccRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ReoccRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ReoccRoomDatabase.class, "reocc_database").
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
