package jtkaiser.imags;

import java.util.UUID;

/**
 * Created by amybea on 1/29/2018.
 */

public class SessionData {

    private static SessionData sSessionData;

    private String SID;
    private String PID;
    private String startTime;
    private String DUR;


    private SessionData(){

    }

    public static SessionData get(){
        if(sSessionData == null){
            sSessionData = new SessionData();
        }
        return sSessionData;
    }

    // getting SID
    public String getSID(){
        return this.SID;
    }

    // setting session id
    public void setID(String sid){
        this.SID = sid;
    }

    // getting PID
    public String getPID(){
        return this.PID;
    }

    // setting participant ID
    public void setPID(String pid){
        this.PID = pid;
    }

    public String getStartTime(){
        return startTime;
    }

    public void setStartTime(String time){
        startTime = time;
    }

    // getting duration of session
    public String getDuration(){
        return this.DUR;
    }

    // setting duration of session
    public void setDuration(String dur){
        this.DUR = dur;
    }
}
