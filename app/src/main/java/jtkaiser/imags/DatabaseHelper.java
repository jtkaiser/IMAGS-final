/**
 * Created by amybea on 2/12/2018.
 */
package jtkaiser.imags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {
    //for log(cat?) ; tag
    private static final String TAG = "DatabaseHelper";

    //database version
    private static final int DATABASE_VERSION = 1;

    //database name
    private static final String DATABASE_NAME = "imags";

    //table names
    public static final String SESSION_TABLE_NAME = "sessions";
    public static final String PAIN_TABLE_NAME = "painlogs";
    public static final String PATIENT_TABLE_NAME = "patients";
    public static final String SONG_TABLE_NAME = "songs";

    //sessions table info
    public static final String MED = "MEDstatus";
    public static final String DUR = "duration";
    public static final String SID = "sessionSID";
    public static final String PIDs = "sessionPID";
    public static final String URIs = "songURI";


    //pain log table info
    public static final String timeStamp = "time";
    public static final String painLVL = "painLvl";
    public static final String INIT = "isInitialStart";
    public static final String SIDp = "painSID";


    //patients table info
    public static final String firstN = "firstName";
    public static final String lastN = "lastName";
    public static final String PID = "patientSID";
    //pid obvi

    //patients song info
    public static final String URI = "URI";
    public static final String attrb1 = "abc";
    public static final String attb2 = "def";
    public static final String attbN = "etc";
    //pid obvi

    //table create statements (might need to remove cascade constraints)
//public static final String CREATE_SESSIONS_TABLE = "cascade constraints; create table " + SESSION_TABLE_NAME + "("

    // session table create sql query
    private static final String CREATE_SESSIONS_TABLE = "create table "
            + SESSION_TABLE_NAME + "(" + SID + " varchar2(10) primary key, "
            + PIDs + " varchar2(50), " + URIs + " varchar2(50), "
            + MED + " varchar2(20), " + DUR + " datetime, foreign key("
            + PIDs + ") references " + PATIENT_TABLE_NAME + "("
            + PID + "), foreign key(" + URIs + ") references "
            + SONG_TABLE_NAME + "(" + URI +"));";
    //+ "constraint Session_un unique (" + SID + "));";//"constraint Patient_pk primary key (" + SessionPID + ") constraint Patient_un unique (" + SessionPID + ") constraint Session_fk foreign key (" + SessionSID + ") references " + PAIN_TABLE_NAME + " (" + SessionSID + "));";

    //pain log table create sql query
    public static final String CREATE_PAINS_TABLE = "create table "
            + PAIN_TABLE_NAME + "(" + SIDp + " varchar2(10), "
            + timeStamp + " datetime, " + painLVL + " number(2), "
            + INIT + " varchar2(3), foreign key(" + SIDp + ") references "
            + SESSION_TABLE_NAME + "(" + SID + "));";
    //, constraint Pain_ch check (" + painLVL + " between 0 and 10));";

    //patients table create sql query
    public static final String CREATE_PATIENTS_TABLE = "create table "
            + PATIENT_TABLE_NAME + "(" + PID + " varchar2(50) primary key, "
            + firstN + " varchar2(30), " + lastN + " varchar2(30));";//, constraint Patient_un unique ("
    //+ PID + "));";

    //patients table create sql query
    public static final String CREATE_SONGS_TABLE = "create table "
            + SONG_TABLE_NAME + "(" + URI + " varchar2(50) primary key, "
            + attrb1 + " varchar2(30), " + attb2+ " varchar2(30), "
            + attbN + " varchar2(30));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

      @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
          //creating required tables
          sqLiteDatabase.execSQL(CREATE_PATIENTS_TABLE);
          sqLiteDatabase.execSQL(CREATE_SESSIONS_TABLE);
          sqLiteDatabase.execSQL(CREATE_PAINS_TABLE);
          sqLiteDatabase.execSQL(CREATE_SONGS_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase database, int oldV, int newV) {
        //drop older tables when onUpgrade called
        database.execSQL("drop table if exists " + PATIENT_TABLE_NAME);
        database.execSQL("drop table if exists " + SESSION_TABLE_NAME);
        database.execSQL("drop table if exists " + PAIN_TABLE_NAME);
        database.execSQL("drop table if exists " + SONG_TABLE_NAME);

        //create new tables
        onCreate(database);
    }

    //closing database
    public void closeDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) { db.close(); }
    }

    //get datetime??
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
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

    //patient table methods
    // creating a new patient in the patients table
    public String createPatient(Patient patient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PID, patient.getID()); //pk
        values.put(firstN, patient.getFName());
        values.put(lastN, patient.getLName());

        // Inserting Row
        db.insert(PATIENT_TABLE_NAME, null, values);
        return PID;
    }

    // Getting a patient
    public Patient getPatient(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + PATIENT_TABLE_NAME + " where "
                + PID + " = " + id;

        Log.e(TAG, query); //record purposes

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

    // Getting All Patients
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<Patient>();

        String query = "SELECT  * FROM " + PATIENT_TABLE_NAME;

        Log.e(TAG, query); //record purposes

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
    public void deletePatient(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PATIENT_TABLE_NAME, PID + " = ?",
                new String[] { id });
        //db.close();
    }

    //session table methods
    // Adding new session
    public void createSession(SessionData session) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SID, session.getSID().toString()); // session ID
        values.put(PIDs, session.getPID()); // patient ID
        values.put(URIs, session.getURI()); // song URI
        values.put(MED, session.getMED()); // med status
        values.put(DUR, session.getDuration()); // session duration (time)
        // Inserting Row
        db.insert(SESSION_TABLE_NAME, null, values);
        //return session.getSID();
    }

    // Getting single session
//    SessionData getSession(String sid) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "select * from " + SESSION_TABLE_NAME
//                + " where " + SID + " = " + sid;
//
//        Log.e(TAG, query);
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        SessionData s = SessionData.get();
//        s.setPID(cursor.getString(cursor.getColumnIndex(PIDs)));
//        s.setDuration(cursor.getString(cursor.getColumnIndex(INIT)));
//        s.setURI(cursor.getString(cursor.getColumnIndex(URIs)));
//        s.setMED(cursor.getString(cursor.getColumnIndex(MED)));
//        s.setID(cursor.getString(cursor.getColumnIndex(SID)));
//
//        // return session
//        return s;
//    }

//    // Getting all sessions associated with a certain patient ID
//    List<Session> getAllSessionPatient(Patient p) {
//        List<Session> sessionsPID = new ArrayList<Session>();
//
//        String query = "select * from " + SESSION_TABLE_NAME + " where "
//                + PIDs + " = " + p.getID();
//
//        Log.e(TAG, query); //record purposes
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                Session s = new Session();
//                s.setID(cursor.getString(cursor.getColumnIndex(SID)));
//                s.setPID(cursor.getString(cursor.getColumnIndex(PIDs)));
//                s.setDuration(cursor.getString(cursor.getColumnIndex(INIT)));
//                s.setURI(cursor.getString(cursor.getColumnIndex(URIs)));
//                s.setMED(cursor.getString(cursor.getColumnIndex(MED)));
//                sessionsPID.add(s);
//            } while (cursor.moveToNext());
//        }
//        return sessionsPID;
//    }

//    // Getting All session
//    public List<Session> getAllSessions() {
//        List<Session> sessionList = new ArrayList<Session>();
//        // Select All Query
//        String query = "select * from " + SESSION_TABLE_NAME;
//
//        Log.e(LOG, query);
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Session s = new Session();
//                s.setID(cursor.getString(cursor.getColumnIndex(SID)));
//                s.setPID(cursor.getString(cursor.getColumnIndex(PIDs)));
//                s.setDuration(cursor.getString(cursor.getColumnIndex(INIT)));
//                s.setURI(cursor.getString(cursor.getColumnIndex(URIs)));
//                s.setMED(cursor.getString(cursor.getColumnIndex(MED)));
//                // Adding session to list
//                sessionList.add(s);
//            } while (cursor.moveToNext());
//        }
//        // return session list
//        return sessionList;
//    }

    // Updating single session (must have sid attached
    public int updateSession() {
        SQLiteDatabase db = this.getWritableDatabase();

        SessionData s = SessionData.get();

        ContentValues values = new ContentValues();
        values.put(PIDs, s.getPID());
        values.put(URIs, s.getURI());
        values.put(MED, s.getMED());
        values.put(DUR, s.getDuration());

        // updating row
        return db.update(SESSION_TABLE_NAME, values, SID + " = ?",
                new String[] { String.valueOf(s.getSID()) });
    }

//    // Deleting single session
//    public void deleteSession(Session s) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(SESSION_TABLE_NAME, SID + " = ?",
//                new String[] { String.valueOf(s.getSID()) });
//        //db.close();
//    }

    //painlog table methods
    // Adding new painlog row into the table
    void createPainLog(PainLog painLog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SIDp, painLog.getSID()); //associated
        values.put(timeStamp, painLog.getStart()); // time
        values.put(painLVL, painLog.getPain()); // pain
        values.put(INIT, painLog.getInit()); //is it initial?

        // Inserting Row
        db.insert(PAIN_TABLE_NAME, null, values);
    }

//    // Getting all pain logs associated with a certain session ID
//    List<PainLog> getAllSessionPain(String sid) {
//        List<PainLog> painLogSID = new ArrayList<PainLog>();
//
//        String query = "select * from " + PATIENT_TABLE_NAME + " where "
//                + SIDp + " = " + sid;
//
//        Log.e(LOG, query); //record purposes
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                PainLog pl = new PainLog();
//                pl.setSID(cursor.getString(cursor.getColumnIndex(SIDp)));
//                pl.setStart(cursor.getString(cursor.getColumnIndex(timeStamp)));
//                pl.setPain(cursor.getColumnIndex(painLVL));
//                pl.setInit(cursor.getString(cursor.getColumnIndex(INIT)));
//                painLogSID.add(pl);
//            } while (cursor.moveToNext());
//        }
//        return painLogSID;
//    }

    // Getting All painlogs
    public List<PainLog> getAllPainLogs() {
        List<PainLog> painLogsList = new ArrayList<PainLog>();
        // Select All Query
        String query = "select * from " + PAIN_TABLE_NAME;

        Log.e(TAG, query);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PainLog pl = new PainLog();
                pl.setSID(cursor.getString(cursor.getColumnIndex(SIDp)));
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

    // Updating single painlog (need sid
    public int updatePainLog(PainLog pl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(timeStamp, pl.getStart());
        values.put(painLVL, pl.getPain());
        values.put(INIT, pl.getInit());

        // updating row
        return db.update(PAIN_TABLE_NAME, values, SIDp + " = ?",
                new String[] { String.valueOf(pl.getSID()) });
    }

//    // Deleting single painlog (need sid
//    public void deletePainLog(PainLog pl) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(PAIN_TABLE_NAME, SIDp + " = ?",
//                new String[] { String.valueOf(pl.getSID()) });
//        //db.close();
//    }


    //songs table methods
    // Adding new song
    //String createSong(Song song) {
    // Getting single song (uri)
    // Getting All song
    // Updating single song (song)
    // Deleting single song (song)

    //test incase
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

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PainLog pl = new PainLog();
                pl.setPain(cursor.getColumnIndex(painLVL));
                Log.e(TAG, String.valueOf(pl.getPain()));

                // Adding painlog pl to list painlog list
                painLogsList.add(pl);
            } while (cursor.moveToNext());
        }
        // return painlog list
        Log.e(TAG, query);
        return painLogsList;
    }
}
