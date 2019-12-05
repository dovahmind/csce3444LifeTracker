package com.example.wildkarrde;

public class RecurringTask {

    /*
    Need to modify this so that it contains the data that the recurringtask has instead of daily task
     */

    private int rid;
    private String type;
    private String Title;
    private String upcomDate;
    private String Description;
    private int checkboxResource;
    private String displayDate;


    public RecurringTask(int inprid, String inptype, String inptitle, String inpDate,
                         String inpdescription, int checkbox){
        rid = inprid;
        type = inptype;
        Title = inptitle;
        upcomDate = inpDate;
        Description = inpdescription;
        checkboxResource = checkbox;

        displayDate = "Deadline: "  + upcomDate;
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
    public String getDate(){ return upcomDate; }
    public String getDescription(){ return Description; }
    public String getDisplayDate(){ return displayDate; }

    public void setRid(int Srid){ rid = Srid; }
    public void setType(String SType){ type = SType; }
    public void setTitle(String STitle){ Title = STitle; }
    public void setDate(String SDate){ upcomDate = SDate; }
    public void setDescription(String SDescription){ Description = SDescription; }
    public void setCheckboxResource(int r){ checkboxResource = r; }
    public void setDisplayDate(String SDisplay){ displayDate = SDisplay; }
}

