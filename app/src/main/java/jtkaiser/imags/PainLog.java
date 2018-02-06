package jtkaiser.imags;

/**
 * Created by amybea on 1/29/2018.
 */

public class PainLog {
    String SID;
    String timeStsmp;
    String inital;
    int PAIN;

    // Empty constructor
    public PainLog(){
    }

    // constructor
    public PainLog(String inital){
        this.inital = inital;
    }
    // constructor
    public PainLog(String sid, String start, int pain, String init){
        this.SID = sid;
        this.timeStsmp = start;
        this.PAIN = pain;
        this.inital = init;
    }

    // getting SID
    public String getSID(){
        return this.SID;
    }

    // setting SID
    public void setSID(String sid){
        this.SID = sid;
    }

    // getting timeStamp (start of the pain record)
    public String getStart(){
        return this.timeStsmp;
    }

    // setting timeStamp
    public void setStart(String time){
        this.timeStsmp = time;
    }

    // getting pain lvl
    public int getPain(){
        return this.PAIN;
    }

    // setting pain lvl
    public void setPain(int pain){
        this.PAIN = pain;
    }

    // getting initial status info
    public String getInit(){
        return this.inital;
    }

    // setting initial status
    public void setInit(String YN){
        this.inital = YN;
    }
}
