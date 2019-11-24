package com.example.wildkarrde;

public class DailyTask {
    private int checkboxResource;
    private String Title;
    private String Date;

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
