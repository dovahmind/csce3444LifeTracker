package com.example.wildkarrde;

public class DailyTask {

    /* MODIFICATION BY JACOB. ADDED reminder id, reminder type,
    reminder start time, and end time to more closely follow the schema
    on the external server
     */

    private int rid;
    private String type;
    private String Title;
    private String Date;
    private String Description;
    private String start_time;
    private String end_time;
    private int checkboxResource;


    public DailyTask(int checkbox, String text1, String text2){
        checkboxResource = checkbox;
        Title = text1;
        Date = text2;
    }

    public int getCheckboxResource(){
        return checkboxResource;
    }
    public String getTitle(){
        return Title;
    }
    public String getDate(){
        return Date;
    }
}
