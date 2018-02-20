package jtkaiser.imags;

import java.util.UUID;

/**
 * Created by amybea on 1/29/2018.
 */

public class PainLog {
    UUID SID;
    String timeStsmp;
//    String inital;
    int PAIN;

//    // Empty constructor
//    public PainLog(){
//    }

    // constructor
//    public PainLog(int pain){
//        this.PAIN = pain;
//    }

    // constructor
//    public PainLog(String inital){
//        this.inital = inital;
//    }


    // constructor
    private PainLog(UUID sid, String time, int pain){
        this.SID = sid;
        this.timeStsmp = time;
        this.PAIN = pain;
//        this.inital = init;
    }

    public static PainLog newLog(UUID sid, int level){
        return new PainLog(sid, DatabaseHelper.getDateTime(), level);
    }

    // getting SID
    public UUID getSID(){
        return this.SID;
    }

    // setting SID
    public void setSID(UUID sid){
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
//    public String getInit(){
//        return this.inital;
//    }

    // setting initial status
//    public void setInit(String YN){
//        this.inital = YN;
//    }
}
