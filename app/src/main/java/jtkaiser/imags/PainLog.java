package jtkaiser.imags;

/**
 * Created by amybea on 1/29/2018.
 */

public class PainLog {
    String SID;
    int START;
    int PAIN;

    // Empty constructor
    public PainLog(){
    }
    // constructor
    public PainLog(String sid, int start, int pain){
        this.SID = sid;
        this.START = start;
        this.PAIN = pain;
    }

    // getting SID
    public String getSID(){
        return this.SID;
    }

    // setting SID
    public void setSID(String sid){
        this.SID = sid;
    }

    // getting med status
    public int getStart(){
        return this.START;
    }

    // setting start time
    public void setStart(int time){
        this.START = time;
    }

    // getting pain lvl
    public int getPain(){
        return this.PAIN;
    }

    // setting pain lvl
    public void setPain(int pain){
        this.PAIN = pain;
    }
}
