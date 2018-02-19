package jtkaiser.imags;

import java.util.UUID;

/**
 * Created by amybea on 1/29/2018.
 */

public class SessionData {

    private static SessionData sSessionData;

    private String SID;
    private String PID;
    private String URI;
    private String MED;
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

    public final String generateUUSID(String email) {
        UUID sessionID = UUID.fromString(email);
        String sid = sessionID.toString();
        return sid;
    }
}
