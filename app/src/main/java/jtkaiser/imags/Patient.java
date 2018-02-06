package jtkaiser.imags;

/**
 * Created by amybea on 1/29/2018.
 */

public class Patient {
    //variables
    String patientID;
    String firstName;
    String lastName;

    // Empty constructor
    public Patient(){
    }
    // constructor
    public Patient(String id, String fname, String lname){
        this.patientID = id;
        this.firstName = fname;
        this.lastName = lname;
    }

    // getting ID
    public String getID(){
        return this.patientID;
    }

    // setting id
    public void setID(String id){
        this.patientID = id;
    }

    // getting first name
    public String getFName(){
        return this.firstName;
    }

    // setting first name
    public void setFName(String f){
        this.firstName = f;
    }

    // getting last name
    public String getLName(){
        return this.lastName;
    }

    // setting last name
    public void setLName(String l){
        this.lastName = l;
    }
}
