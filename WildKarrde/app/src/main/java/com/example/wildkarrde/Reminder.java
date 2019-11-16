package com.example.wildkarrde;

import androidx.annotation.NonNull;
import androidx.room.*;

/* A Reminder entity part of the reminders table that will store
info that is retrieved from the server
 */

/* Note that this will only contain the reminders that are gathered from view_daily_reminders.
When a user presses this and a connection is successfully made to the server,
then all stuff originally in the database is deleted and replaced with current information.
 */

@Entity(tableName = "reminders")
public class Reminder {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "rid")
    private int rid;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "start_time")
    private String start_time;

    @ColumnInfo(name = "end_time")
    private String end_time;

    @ColumnInfo(name = "completed")
    private boolean completed;

    public Reminder(int rid, String type, String date, String description,
                    String start_time, String end_time, boolean completed)
    {
        this.rid = rid;
        this.type = type;
        this.date = date;
        this.description = description;
        this.start_time = start_time;
        this.end_time = end_time;
        this.completed = completed;
    }

    public int getRid(){
        return this.rid;
    }

    public String getType(){
        return this.type;
    }

    public String getDate(){
        return this.date;
    }

    public String getDescription(){
        return this.description;
    }

    public String getStart_time(){
        return this.start_time;
    }

    public String getEnd_time(){
        return this.end_time;
    }

    public boolean getCompleted(){
        return this.completed;
    }
}
