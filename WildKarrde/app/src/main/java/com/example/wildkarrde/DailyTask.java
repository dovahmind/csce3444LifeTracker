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
    public String getDate(){ return Date; }
    public String getDescription(){ return Description; }
    public String getStart_time(){ return start_time; }
    public String getEnd_time(){ return end_time; }

    public void setRid(int Srid){ rid = Srid; }
    public void setType(String SType){ type = SType; }
    public void setTitle(String STitle){ Title = STitle; }
    public void setDate(String SDate){ Date = SDate; }
    public void setDescription(String SDescription){ Description = SDescription; }
    public void setStart_time(String SStart_time){ start_time = SStart_time; }
    public void setEnd_time(String SEnd_time){ end_time = SEnd_time; }
    public void setCheckboxResource(int r){ checkboxResource = r; }
}
