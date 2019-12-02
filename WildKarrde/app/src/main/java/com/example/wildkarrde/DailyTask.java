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
    private String displayTime;


    public DailyTask(int rid, String type, String title, String inpDate, String description,
                      String Sstart_time, String Send_time, int checkbox){
        rid = rid;
        type = type;
        Title = title;
        Date = Date;
        Description = description;
        start_time = Sstart_time;
        end_time = Send_time;
        checkboxResource = checkbox;

        if(start_time.endsWith(":00"))
            start_time = start_time.substring(0, start_time.length() - 3);

        if(end_time.endsWith(":00"))
            end_time = end_time.substring(0, end_time.length() - 3);

        displayTime = start_time + "-" + end_time;
    }


        //if end up making a place to display all the info about the event
    //public void displayAll(String Text);
    public int getrid() { return rid; }
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
    public String getDisplayTime(){ return displayTime; }

    public void setRid(int Srid){ rid = Srid; }
    public void setType(String SType){ type = SType; }
    public void setTitle(String STitle){ Title = STitle; }
    public void setDate(String SDate){ Date = SDate; }
    public void setDescription(String SDescription){ Description = SDescription; }
    public void setStart_time(String SStart_time){ start_time = SStart_time; }
    public void setEnd_time(String SEnd_time){ end_time = SEnd_time; }
    public void setCheckboxResource(int r){ checkboxResource = r; }
    public void setDisplayTime(String SDisplay){ displayTime = SDisplay; }
}
