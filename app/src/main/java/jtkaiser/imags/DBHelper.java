package jtkaiser.imags;

/**
 * Created by amybea on 1/23/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    //logcat tag
    private static final String LOG = "DatabaseHelper";
    //database version
    private static final int VERSION = 1;
    //database name
    private static final String DATABASE_NAME = "IMAGS.db"; //might not need the .db?

    //table names
    public static final String SESSION_TABLE_NAME = "sessions";
    public static final String PAIN_TABLE_NAME = "pain logs";
    public static final String PATIENT_TABLE_NAME = "patients";

    //column names that are recyled in sessions, painlog, and patients
    public static final String SID = "session ID";
    public static final String PID = "patient ID";
    public static final String URI = "song URI";

    //sessions table info
    public static final String MED = "medication status";
    public static final String DUR = "session duration";
    // sid, pid, and uri obvi

    //pain log table info
    public static final String timeStamp = "time";
    public static final String painLVL = "pain level";
    public static final String INIT = "initial start pain?";
    //sid obvi

    //patients table info
    public static final String firstN = "first name";
    public static final String lastN = "last name";
    //pid obvi

//table create statements (might need to remove cascade constraints)
//public static final String CREATE_SESSIONS_TABLE = "cascade constraints; create table " + SESSION_TABLE_NAME + "("
    // session table create sql query
    private static final String CREATE_SESSIONS_TABLE = "cascade constraints; create table "
        + SESSION_TABLE_NAME + "(" + SID + " varchar2(10) primary key, "
        + PID + " varchar2(50), " + URI + " varchar2(50), "
        + MED + " varchar2(20), " + DUR + " datetime, "
        + "constraint Session_un unique (" + SID + "));";//"constraint Patient_pk primary key (" + SessionPID + ") constraint Patient_un unique (" + SessionPID + ") constraint Session_fk foreign key (" + SessionSID + ") references " + PAIN_TABLE_NAME + " (" + SessionSID + "));";
    //pain log table create sql query
    public static final String CREATE_PAINS_TABLE = "cascade constraints; create table "
            + PAIN_TABLE_NAME + "(" + SID + " varchar2(10), "
            + timeStamp + " datetime, " + painLVL + " number(2), "
            + INIT + "varchar2(3), constraint Pain_ch check (" + painLVL + " between 0 and 10));";
    //patients table create sql query
    public static final String CREATE_PATIENTS_TABLE = "cascade constraints; create table "
            + PATIENT_TABLE_NAME + "(" + PID + " varchar2(50) primary key, "
            + firstN + " varchar2(30), " + lastN + " varchar2(30), constraint Patient_un unique ("
            + PID + "));";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) { db.close(); }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//constructor
        //creating the tables
        db.execSQL(CREATE_SESSIONS_TABLE);
        db.execSQL(CREATE_PAINS_TABLE);
        db.execSQL(CREATE_PATIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop the older tables
        db.execSQL("drop table if exists " + SESSION_TABLE_NAME);
        db.execSQL("drop table if exists " + PAIN_TABLE_NAME);
        db.execSQL("drop table if exists " + PATIENT_TABLE_NAME);

        //create new tables
        onCreate(db); //however when updating one it will recreate all which is not wanted TODOL8r
    }

    // creating a new patient in the patients table
    public String createPatient(Patient patient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PID, patient.getID()); // common/recycled variable name
        values.put(firstN, patient.getFName());
        values.put(lastN, patient.getLName());

        // Inserting Row
        db.insert(PATIENT_TABLE_NAME, null, values);

       return PID;// db.close(); // Closing database connection
    }

    // Getting a patient
    public Patient getPatient(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + PATIENT_TABLE_NAME + " where "
                + PID + " = " + id;

        Log.e(LOG, query); //record purposes

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        Patient patient = new Patient();
        patient.setID(cursor.getString(cursor.getColumnIndex(PID)));
        patient.setFName(cursor.getString(cursor.getColumnIndex(firstN)));
        patient.setLName(cursor.getString(cursor.getColumnIndex(lastN)));

        // return patient info
        return patient;
    }

    // Getting All Contacts
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<Patient>();

        String query = "SELECT  * FROM " + PATIENT_TABLE_NAME;

        Log.e(LOG, query); //record purposes

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setID(cursor.getString(cursor.getColumnIndex(PID)));
                patient.setFName(cursor.getString(cursor.getColumnIndex(firstN)));
                patient.setLName(cursor.getString(cursor.getColumnIndex(lastN)));
                // Adding contact to list
                patients.add(patient);
            } while (cursor.moveToNext());
        }
        // return patient list
        return patients;
    }

    // Updating single patient (names only)
    public int updatePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(firstN, patient.getFName());
        values.put(lastN, patient.getLName());

        // updating row
        return db.update(PATIENT_TABLE_NAME, values, PID + " = ?",
                new String[] { String.valueOf(patient.getID()) });
    }

    // Deleting a patient
    public void deletePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PATIENT_TABLE_NAME, PID + " = ?",
                new String[] { String.valueOf(patient.getID()) });
        //db.close();
    }

    // Adding new painlog row into the table
    void createPainLogpain(PainLog painLog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(timeStamp, painLog.getStart()); // time
        values.put(painLVL, painLog.getPain()); // pain

        // Inserting Row
        db.insert(PAIN_TABLE_NAME,  null, values);
        //return true;
        //db.close(); // Closing database connection
    }

    // Getting All painlogspain
    public List<PainLog> getAllPain() {
        List<PainLog> painLogsList = new ArrayList<PainLog>();
        // Select All Query
        String query = "select * from " + PAIN_TABLE_NAME;

        Log.e(LOG, query);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PainLog pl = new PainLog();
                pl.setPain(cursor.getColumnIndex(painLVL));
          //      pl.setStart(cursor.getString(cursor.getColumnIndex(timeStamp)));

                // Adding painlog pl to list painlog list
                painLogsList.add(pl);
            } while (cursor.moveToNext());
        }
        // return painlog list
        return painLogsList;
    }

    // Adding new painlog row into the table
    void createPainLog(PainLog painLog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SID, painLog.getSID()); //associated
        values.put(timeStamp, painLog.getStart()); // time
        values.put(painLVL, painLog.getPain()); // pain

        // Inserting Row
        db.insert(PAIN_TABLE_NAME, null, values);
        //db.close(); // Closing database connection
    }

    // Getting all pain logs associated with a certain session ID
    List<PainLog> getAllSessionPain(String sid) {
        List<PainLog> painLogSID = new ArrayList<PainLog>();

        String query = "select * from " + PATIENT_TABLE_NAME + " where "
                + SID + " = " + sid;

        Log.e(LOG, query); //record purposes

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                PainLog pl = new PainLog();
                pl.setSID(cursor.getString(cursor.getColumnIndex(SID)));
                pl.setStart(cursor.getString(cursor.getColumnIndex(timeStamp)));
                pl.setPain(cursor.getColumnIndex(painLVL));
                pl.setInit(cursor.getString(cursor.getColumnIndex(INIT)));
                painLogSID.add(pl);
            } while (cursor.moveToNext());
        }
        return painLogSID;
    }

    // Getting All painlogs
    public List<PainLog> getAllPainLogs() {
        List<PainLog> painLogsList = new ArrayList<PainLog>();
        // Select All Query
        String query = "select * from " + PAIN_TABLE_NAME;

        Log.e(LOG, query);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PainLog pl = new PainLog();
                pl.setSID(cursor.getString(cursor.getColumnIndex(SID)));
                pl.setPain(cursor.getColumnIndex(painLVL));
                pl.setStart(cursor.getString(cursor.getColumnIndex(timeStamp)));
                pl.setInit(cursor.getString(cursor.getColumnIndex(INIT)));

                // Adding painlog pl to list painlog list
                painLogsList.add(pl);
            } while (cursor.moveToNext());
        }
        // return painlog list
        return painLogsList;
    }

    // Updating single painlog
    public int updatePainLog(PainLog pl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(timeStamp, pl.getStart());
        values.put(painLVL, pl.getPain());
        values.put(INIT, pl.getInit());

        // updating row
        return db.update(PAIN_TABLE_NAME, values, SID + " = ?",
                new String[] { String.valueOf(pl.getSID()) });
    }

    // Deleting single painlog
    public void deletePainLog(PainLog pl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PAIN_TABLE_NAME, SID + " = ?",
                new String[] { String.valueOf(pl.getSID()) });
        //db.close();
    }

    // Adding new sessionData
    String addSession(SessionData sessionData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SID, sessionData.getSID()); // sessionData ID
        values.put(PID, sessionData.getPID()); // patient ID
        values.put(URI, sessionData.getURI()); // song URI
        values.put(MED, sessionData.getMED()); // med status
        values.put(DUR, sessionData.getDuration()); // sessionData duration (time)
        // Inserting Row
        db.insert(SESSION_TABLE_NAME, null, values);
        return sessionData.getSID(); //db.close(); // Closing database connection
    }

    // Getting single session
    SessionData getSession(String sid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + SESSION_TABLE_NAME
                + " where " + SID + " = " + sid;

        Log.e(LOG, query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        SessionData s = SessionData.get();
        s.setPID(cursor.getString(cursor.getColumnIndex(PID)));
        s.setDuration(cursor.getString(cursor.getColumnIndex(INIT)));
        s.setURI(cursor.getString(cursor.getColumnIndex(URI)));
        s.setMED(cursor.getString(cursor.getColumnIndex(MED)));

        // return session
        return s;
    }

    // Getting all sessions associated with a certain patient ID
    List<SessionData> getAllSessionPatient(String pid) {
        List<SessionData> sessionsPID = new ArrayList<SessionData>();

        String query = "select * from " + PATIENT_TABLE_NAME + " where "
                + PID + " = " + pid;

        Log.e(LOG, query); //record purposes

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SessionData s = SessionData.get();
                s.setID(cursor.getString(cursor.getColumnIndex(SID)));
                s.setPID(cursor.getString(cursor.getColumnIndex(PID)));
                s.setDuration(cursor.getString(cursor.getColumnIndex(INIT)));
                s.setURI(cursor.getString(cursor.getColumnIndex(URI)));
                s.setMED(cursor.getString(cursor.getColumnIndex(MED)));
                sessionsPID.add(s);
            } while (cursor.moveToNext());
        }
        return sessionsPID;
    }

    // Getting All session
    public List<SessionData> getAllSessions() {
        List<SessionData> sessionDataList = new ArrayList<SessionData>();
        // Select All Query
        String query = "select * from " + SESSION_TABLE_NAME;

        Log.e(LOG, query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SessionData s = SessionData.get();
                s.setID(cursor.getString(cursor.getColumnIndex(SID)));
                s.setPID(cursor.getString(cursor.getColumnIndex(PID)));
                s.setDuration(cursor.getString(cursor.getColumnIndex(INIT)));
                s.setURI(cursor.getString(cursor.getColumnIndex(URI)));
                s.setMED(cursor.getString(cursor.getColumnIndex(MED)));
                // Adding session to list
                sessionDataList.add(s);
            } while (cursor.moveToNext());
        }
        // return session list
        return sessionDataList;
    }

    // Updating single session
    public int updateSession(SessionData s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PID, s.getPID());
        values.put(URI, s.getURI());
        values.put(MED, s.getMED());
        values.put(DUR, s.getDuration());
        // updating row
        return db.update(SESSION_TABLE_NAME, values, SID + " = ?",
                new String[] { String.valueOf(s.getSID()) });
    }

    // Deleting single session
    public void deleteSession(SessionData s) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SESSION_TABLE_NAME, SID + " = ?",
                new String[] { String.valueOf(s.getSID()) });
        //db.close();
    }

    // Getting Count of all rows in a given table
    public int getCount(String tablename) {
        String countQuery = "SELECT  * FROM " +tablename;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

}



