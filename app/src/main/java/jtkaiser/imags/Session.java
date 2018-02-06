package jtkaiser.imags;

/**
 * Created by amybea on 1/29/2018.
 */

public class Session {

    String SID;
    String PID;
    String URI;
    String MED;
    String DUR;


    // Empty constructor
    public Session(){

    }

    // constructor
    public Session(String sid, String pid){
        this.SID = sid;
        this.PID = pid;
    }

    // constructor
    public Session(String sid, String pid, String uri, String time){
        this.SID = sid;
        this.PID = pid;
        this.URI = uri;
        this.DUR = time;
    }

    // constructor
    public Session(String sid, String pid, String uri, String med, String time){
        this.SID = sid;
        this.PID = pid;
        this.URI = uri;
        this.MED = med;
        this.DUR = time;
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

    // setting patient ID
    public void setPID(String pid){
        this.PID = pid;
    }

    // getting URI
    public String getURI(){
        return this.URI;
    }

    // setting song URI
    public void setURI(String uri){
        this.URI = uri;
    }

    // getting med status
    public String getMED(){
        return this.MED;
    }

    // setting med status
    public void setMED(String med){
        this.MED = med;
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
