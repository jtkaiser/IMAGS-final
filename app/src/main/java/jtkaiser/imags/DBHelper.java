package jtkaiser.imags;

/**
 * Created by amybea on 1/23/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    //database name
    private static final String DATABASE_NAME = "sessionBase.db";
    //sessions table
    public static final String SESSION_TABLE_NAME = "sessions";
    public static final String SessionSID = "session ID";
    public static final String SessionPID = "patient ID";
    public static final String SessionURI = "song URI";
    public static final String SessionMED = "medication";
    public static final String SessionDUR = "session duration";
    //pain table
    public static final String PAIN_TABLE_NAME = "pain logs";
    public static final String PainSID = "session ID";
    public static final String PainSTART = "time";
    public static final String PainLVL = "pain level";
    //patients table
    public static final String PATIENT_TABLE_NAME = "patients";
    public static final String PatientPID = "patient ID";
    public static final String firstN = "first name";
    public static final String lastN = "last name";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//constructor
        String CREATE_SESSIONS_TABLE = "drop table " + SESSION_TABLE_NAME + " cascade constraints; create table " + SESSION_TABLE_NAME + "("
                + SessionSID + " varchar2(10), " + SessionPID + " varchar2(50), " + SessionURI + " varchar2(50), " + SessionMED + " varchar2(20), " + SessionDUR + " number(10), constraint Patient_pk primary key (" + SessionPID +
                ") constraint Patient_un unique (" + SessionPID +
                ") constraint Song_un unique (" + SessionURI +
                ") constraint Session_un unique (" + SessionSID +
                ") constraint Session_fk foreign key (" + SessionSID +
                ") references " + PAIN_TABLE_NAME + " (" + SessionSID + "));";
        db.execSQL(CREATE_SESSIONS_TABLE);

        String CREATE_PAINS_TABLE = "drop table " + PAIN_TABLE_NAME + " cascade constraints; create table " + PAIN_TABLE_NAME + "("
                + PainSID + " varchar2(10), " + PainSTART + " number(10), " + PainLVL + " number(2), constraint Session_pk primary key (" + PainSID +
                ") constraint Session_un unique (" + PainSID +
                ") PainVal check (" + PainLVL + " between 0 and 10));";
        db.execSQL(CREATE_PAINS_TABLE);

        String CREATE_PATIENTS_TABLE = "drop table " + PATIENT_TABLE_NAME + " cascade constraints; create table " + PATIENT_TABLE_NAME + "("
                + PatientPID + " varchar2(50), " + firstN + " varchar2(30), " + lastN + " varchar2(30), constraint Patient_pk primary key (" + PatientPID +
                ") constraint Patient_un unique (" + PatientPID +
                ") constraint Patient_fk foreign key (" + PatientPID +
                ") references " + SESSION_TABLE_NAME + " (" + PatientPID + "));";
        db.execSQL(CREATE_PATIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db); //however when updating one it will recreate all which is not wanted TODOL8r
    }

    // Adding new contact
    public void addPatient(Patient patient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PatientPID, patient.getID()); // patient ID
        values.put(firstN, patient.getFName());
        values.put(lastN, patient.getLName());

        // Inserting Row
        db.insert(PATIENT_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Patient getPatient(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(PATIENT_TABLE_NAME, new String[] { firstN, lastN },  PatientPID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Patient patient = new Patient((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return patient;
    }

    // Getting All Contacts
    public List<Patient> getAllPatients() {
        List<Patient> patientList = new ArrayList<Patient>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PATIENT_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setID(cursor.getString(0));
                patient.setFName(cursor.getString(1));
                patient.setLName(cursor.getString(2));
                // Adding contact to list
                patientList.add(patient);
            } while (cursor.moveToNext());
        }
        // return patient list
        return patientList;
    }

    // Updating single patient
    public int updatePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(firstN, patient.getFName());
        values.put(lastN, patient.getLName());
        // updating row
        return db.update(PATIENT_TABLE_NAME, values, PatientPID + " = ?",
                new String[] { String.valueOf(patient.getID()) });
    }

    // Deleting single patient
    public void deletePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PATIENT_TABLE_NAME, PatientPID + " = ?",
                new String[] { String.valueOf(patient.getID()) });
        db.close();
    }

    // Adding new painlog
    void addPainLog(PainLog painLog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PainSID, painLog.getSID()); //session ID
        values.put(PainSTART, painLog.getStart()); // time
        values.put(PainLVL, painLog.getPain()); // pain

        // Inserting Row
        db.insert(PAIN_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting all pain logs associated with a certain session ID
    List<PainLog> getAllSessionPain(String sid) {
        List<PainLog> painLogSessionList = new ArrayList<PainLog>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(PAIN_TABLE_NAME, new String[] { PainSID,
                        PainSTART, PainLVL }, PainSID + "=?",
                new String[] { String.valueOf(sid) }, null, null, null, null);
        if (cursor != null) {
            do {
                cursor.moveToFirst();
                PainLog pl = new PainLog();
                PainLog session = new PainLog((cursor.getString(0)),
                        (Integer.parseInt(cursor.getString(1))), Integer.parseInt(cursor.getString(2)));
                    // Adding painlog to list
                    painLogSessionList.add(pl);
                } while (cursor.moveToNext());
            }
        return painLogSessionList;
    }

    // Getting All painlogs
    public List<PainLog> getAllPainLogs() {
        List<PainLog> painLogListlList = new ArrayList<PainLog>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PAIN_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PainLog pl = new PainLog();
                pl.setSID(cursor.getString(0));
                pl.setStart(Integer.parseInt(cursor.getString(1)));
                pl.setPain(Integer.parseInt(cursor.getString(2)));
                // Adding painlog to list
                painLogListlList.add(pl);
            } while (cursor.moveToNext());
        }

        // return painlog list
        return painLogListlList;
    }

    // Updating single painlog
    public int updatePainLog(PainLog pl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PainSTART, pl.getStart());
        values.put(PainLVL, pl.getPain());
        // updating row
        return db.update(PAIN_TABLE_NAME, values, PainSID + " = ?",
                new String[] { String.valueOf(pl.getSID()) });
    }

    // Deleting single painlog
    public void deletePainLog(PainLog pl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PAIN_TABLE_NAME, PainSID + " = ?",
                new String[] { String.valueOf(pl.getSID()) });
        db.close();
    }

    // Adding new session
    void addSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SessionSID, session.getSID()); // session ID
        values.put(SessionPID, session.getPID()); // patient ID
        values.put(SessionURI, session.getURI()); // song URI
        values.put(SessionMED, session.getMED()); // med status
        values.put(SessionDUR, session.getDuration()); // session duration (time)
        // Inserting Row
        db.insert(SESSION_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single session
    Session getSession(String sid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SESSION_TABLE_NAME, new String[] { SessionSID,
                        SessionPID, SessionURI, SessionMED, SessionDUR }, SessionSID + "=?",
                new String[] { String.valueOf(sid) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Session s = new Session((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),  cursor.getString(3), (Integer.parseInt(cursor.getString(0))));
        // return session
        return s;
    }

    // Getting All session
    public List<Session> getAllSessions() {
        List<Session> sessionList = new ArrayList<Session>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SESSION_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Session s = new Session();
                s.setID(cursor.getString(0));
                s.setPID(cursor.getString(1));
                s.setURI(cursor.getString(2));
                s.setMED(cursor.getString(3));
                s.setDuration(Integer.parseInt(cursor.getString(4)));
                // Adding session to list
                sessionList.add(s);
            } while (cursor.moveToNext());
        }
        // return session list
        return sessionList;
    }

    // Updating single session
    public int updateSession(Session s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SessionPID, s.getPID());
        values.put(SessionURI, s.getURI());
        values.put(SessionMED, s.getMED());
        values.put(SessionDUR, s.getDuration());
        // updating row
        return db.update(SESSION_TABLE_NAME, values, SessionSID + " = ?",
                new String[] { String.valueOf(s.getSID()) });
    }

    // Deleting single session
    public void deleteSession(Session s) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SESSION_TABLE_NAME, SessionSID + " = ?",
                new String[] { String.valueOf(s.getSID()) });
        db.close();
    }

    // Getting contacts Count
    public int getCount(String tablename) {
        String countQuery = "SELECT  * FROM " +tablename;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

}



