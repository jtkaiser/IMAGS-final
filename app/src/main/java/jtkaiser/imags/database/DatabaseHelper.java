/**
 * Created by amybea on 2/12/2018.
 */
package jtkaiser.imags.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import jtkaiser.imags.database.DbSchema.SessionTable;
import jtkaiser.imags.database.DbSchema.PainLogTable;
import jtkaiser.imags.database.DbSchema.ParticipantTable;
import jtkaiser.imags.database.DbSchema.SongTable;
import jtkaiser.imags.database.DbSchema.MedicationTable;


public class DatabaseHelper extends SQLiteOpenHelper {
    //for log(cat?) ; tag
    private static final String TAG = "DatabaseHelper";

    //database version
    private static final int DATABASE_VERSION = 1;

    //database name
    private static final String DATABASE_NAME = "imags.db";

    //table create statements (might need to remove cascade constraints)
//public static final String CREATE_SESSIONS_TABLE = "cascade constraints; create table " + SESSION_TABLE_NAME + "("

    // session table create sql query
    private static final String CREATE_SESSIONS_TABLE = "create table "
            + SessionTable.NAME + "(" + SessionTable.Cols.SID + " char(36) primary key, "
            + SessionTable.Cols.PID + " varchar2(50), "
            + SessionTable.Cols.START + " datetime, "
            + SessionTable.Cols.DUR + " int(5), foreign key("
            + SessionTable.Cols.PID + ") references " + ParticipantTable.NAME + "("
            + ParticipantTable.Cols.PID + "));";

//    private static final String CREATE_SESSIONS_TABLE = "create table sessions (sessionID char(36) primary key, participantID varchar2(50), startTime datetime, duration int(5), foreign key(participantID) references participants(participantID));";
    //+ "constraint Session_un unique (" + SID + "));";//"constraint Participant_pk primary key (" + SessionPID + ") constraint Participant_un unique (" + SessionPID + ") constraint Session_fk foreign key (" + SessionSID + ") references " + PAIN_TABLE_NAME + " (" + SessionSID + "));";

    //pain log table create sql query
    public static final String CREATE_PAINS_TABLE = "create table "
            + PainLogTable.NAME + "(" + PainLogTable.Cols.pSID + " varchar2(10), "
            + PainLogTable.Cols.timeStamp + " datetime, " + PainLogTable.Cols.painLVL +
            " number(2), foreign key(" + PainLogTable.Cols.pSID + ") references "
            + SessionTable.NAME + "(" + SessionTable.Cols.SID + "));";
    //, constraint Pain_ch check (" + painLVL + " between 0 and 10));";

    //participants table create sql query
    public static final String CREATE_PARTICIPANTS_TABLE = "create table "
            + ParticipantTable.NAME + "(" + ParticipantTable.Cols.PID + " varchar2(50) primary key);";
    //, constraint Participant_un unique ("
    //+ PID + "));";

    //songs table create sql query
    public static final String CREATE_SONGS_TABLE = "create table "
            + SongTable.NAME + "(" + SongTable.Cols.URI + " varchar2(50), "
            + SongTable.Cols.SID + " char(36), " + SongTable.Cols.ACOUS + " float(5), "
            + SongTable.Cols.AURL + " varchar2(100), " + SongTable.Cols.DANC + " float(5), "
            + SongTable.Cols.DUR + " int(10), " + SongTable.Cols.NRG + " float(5), "
            + SongTable.Cols.SONGID + " varchar2(50), " + SongTable.Cols.INS + " float(5), "
            + SongTable.Cols.KEY + " int(1), " + SongTable.Cols.LIVE + " float(5), "
            + SongTable.Cols.LOUD + " float(5), " + SongTable.Cols.MODE + " int(1), "
            + SongTable.Cols.SPCH + " float(5), " + SongTable.Cols.TMPO + " float(5), "
            + SongTable.Cols.TIME + " int(5), " + SongTable.Cols.HREF + " varchar2(100), "
            + SongTable.Cols.VLNC + " float(5), foreign key(" + SongTable.Cols.SID + ") references "
            + SessionTable.NAME + "(" + SessionTable.Cols.SID + "));";

    public static final String CREATE_MEDICATION_TABLE = "create table "
            + MedicationTable.NAME + "(" + MedicationTable.Cols.SID + " char(36), "
            + MedicationTable.Cols.TOOK + " int(1), " + MedicationTable.Cols.MED + " varchar(50), "
            + MedicationTable.Cols.DOSE + " varchar(100), foreign key(" + MedicationTable.Cols.SID + ") references "
            + SessionTable.NAME + "(" + SessionTable.Cols.SID + "));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

      @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
          //creating required tables
          Log.d(TAG, "creating tables...");
          sqLiteDatabase.execSQL(CREATE_PARTICIPANTS_TABLE);
          sqLiteDatabase.execSQL(CREATE_SESSIONS_TABLE);
          sqLiteDatabase.execSQL(CREATE_PAINS_TABLE);
          sqLiteDatabase.execSQL(CREATE_SONGS_TABLE);
          sqLiteDatabase.execSQL(CREATE_MEDICATION_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase database, int oldV, int newV) {
        //drop older tables when onUpgrade called
        database.execSQL("drop table if exists " + ParticipantTable.NAME);
        database.execSQL("drop table if exists " + SessionTable.NAME);
        database.execSQL("drop table if exists " + PainLogTable.NAME);
        database.execSQL("drop table if exists " + SongTable.NAME);
        database.execSQL("drop table if exists " + MedicationTable.NAME);

        //create new tables
        onCreate(database);
    }

    //closing database
    public void closeDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) { db.close(); }
    }
}
