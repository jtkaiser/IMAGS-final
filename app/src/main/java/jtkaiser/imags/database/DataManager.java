package jtkaiser.imags.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import jtkaiser.imags.PainLog;
import jtkaiser.imags.PainTracker;
import jtkaiser.imags.SessionData;
import jtkaiser.imags.SongData;
import jtkaiser.imags.TrackDataManager;
import jtkaiser.imags.VolleyHelper;
import jtkaiser.imags.database.IMAGSDbSchema.ParticipantTable;
import jtkaiser.imags.database.IMAGSDbSchema.SessionTable;
import jtkaiser.imags.database.IMAGSDbSchema.SongTable;
import jtkaiser.imags.database.IMAGSDbSchema.PainLogTable;
import jtkaiser.imags.database.IMAGSDbSchema.MedicationTable;

import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by jtkai on 3/1/2018.
 */

public class DataManager {
    private static final String TAG = "DataManager";
    private static DataManager sDataManager;
    private SQLiteDatabase mDatabase;
    private Context mContext;
    

    public static DataManager get(Context context){
        if(sDataManager == null){
            sDataManager = new DataManager(context);
        }
        return sDataManager;
    }

    private DataManager(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public void createSession(String participantID) {

        SessionData sd = SessionData.get();
        sd.setID(UUID.randomUUID().toString());
        sd.setPID(participantID);
        sd.setStartTime(getDateTime());
        sd.setDuration("0");
        Log.d(TAG, "SID: " + sd.getSID() + ", PID: " + sd.getPID() + ", START: " + sd.getStartTime() + ", DUR: " + sd.getDuration());

        Log.d("Session Started at: ", sd.getStartTime());

        createParticipantListEntry();

        ContentValues values = new ContentValues();
        values.put(SessionTable.Cols.SID, sd.getSID()); // session ID
        values.put(SessionTable.Cols.PID, sd.getPID()); // participant ID
        values.put(SessionTable.Cols.START, sd.getStartTime());
        values.put(SessionTable.Cols.DUR, sd.getDuration()); // session duration (time)
        // Inserting Row
        mDatabase.insert(SessionTable.NAME, null, values);
    }

    public void createSongEntry(Context context, SpotifyService service){

        TrackDataManager td = TrackDataManager.get(context, service);

        ContentValues values = new ContentValues();
        values.put(SongTable.Cols.URI, td.getUri());
        values.put(SongTable.Cols.SID, SessionData.get().getSID());
        values.put(SongTable.Cols.ACOUS, td.getAcousticness());
        values.put(SongTable.Cols.AURL, td.getAnalysisURL());
        values.put(SongTable.Cols.DANC, td.getDanceability());
        values.put(SongTable.Cols.DUR, td.getDuration());
        values.put(SongTable.Cols.NRG, td.getEnergy());
        values.put(SongTable.Cols.SONGID, td.getSongID());
        values.put(SongTable.Cols.INS, td.getInstrumentalness());
        values.put(SongTable.Cols.KEY, td.getSongKey());
        values.put(SongTable.Cols.LIVE, td.getLiveness());
        values.put(SongTable.Cols.LOUD, td.getLoudness());
        values.put(SongTable.Cols.MODE, td.getSongMode());
        values.put(SongTable.Cols.SPCH, td.getSpeechiness());
        values.put(SongTable.Cols.TMPO, td.getTempo());
        values.put(SongTable.Cols.TIME, td.getTimeSignature());
        values.put(SongTable.Cols.HREF, td.gethref());
        values.put(SongTable.Cols.VLNC, td.getValence());

        mDatabase.insert(SongTable.NAME, null, values);
    }

    private SongCursorWrapper querySongs(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                SongTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new SongCursorWrapper(cursor);
    }

    public void createPainLogEntry(){
        ContentValues values = new ContentValues();
        values.put(PainLogTable.Cols.pSID, SessionData.get().getSID());
        values.put(PainLogTable.Cols.painLVL, PainTracker.get(mContext).getLastValue());
        values.put(PainLogTable.Cols.timeStamp, getDateTime());

        mDatabase.insert(PainLogTable.NAME, null, values);
    }

    private PainLogCursorWrapper queryLogs(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                PainLogTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new PainLogCursorWrapper(cursor);
    }

    private void createParticipantListEntry(){
        ContentValues values = new ContentValues();
        values.put(ParticipantTable.Cols.PID, SessionData.get().getPID());

        mDatabase.replace(ParticipantTable.NAME, null, values);
    }

    public void createMedicationEntry(final Boolean tookMed, final String name, final String dosage){
        ContentValues values = new ContentValues();
        values.put(MedicationTable.Cols.SID, SessionData.get().getSID());
        values.put(MedicationTable.Cols.TOOK, tookMed);
        values.put(MedicationTable.Cols.MED, name);
        values.put(MedicationTable.Cols.DOSE, dosage);

        mDatabase.insert(MedicationTable.NAME, null, values);

        Log.d(TAG, "exporting medication info");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            if(Response.equals("OK")){
                                Log.d(TAG, "Saved Medication Info");

                            }
                            else{
                                Log.d(TAG, "Medication: " + Response);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sessionID", SessionData.get().getSID());
                params.put("tookMed", String.valueOf(tookMed ? 1 : 0));
                params.put("name", name);
                params.put("dosage", dosage);
                params.put("updateType", "med_data");


                return params;
            }
        }
                ;

        VolleyHelper.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void endSession(){
        Log.d(TAG, "saveToServer");



        if(checkNetworkConnection())
        {
            exportParticipantListEntry();
            exportSession();
            exportSongs(SessionData.get().getSID());
            exportPainLogEntries();
        }
    }

    private void exportSession(){
        Log.d(TAG, "exporting session");

        final SessionData s = SessionData.get();
        Log.d(TAG, "connection okay");
        Log.d(TAG, "SID: " + s.getSID() + ", PID: " + s.getPID() + ", START: " + s.getStartTime() + ", DUR: " + s.getDuration());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            if(Response.equals("OK")){
                                Log.d(TAG, "Session Saved");

                            }
                            else{
                                Log.d(TAG, "Session: " + Response);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sessionID", s.getSID());
                params.put("participantID", s.getPID());
                params.put("startTime", s.getStartTime());
                //CHANGE THIS!!!
                params.put("sessionDuration", "100");
                params.put("updateType", "session");
                return params;
            }
        }
                ;

        VolleyHelper.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void exportSongs(String SID){

        SongCursorWrapper cursor = querySongs(SongTable.Cols.SID + " = ?",
                new String[] { SID });

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                SongData s = cursor.getSong();
                exportSong(s);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    private void exportSong(SongData song){
        Log.d(TAG, "exporting a song");
        final SongData s = song;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            if(Response.equals("OK")){
                                Log.d(TAG, "Song Saved");

                            }
                            else{
                                Log.d(TAG, "Song: " + Response);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("URI", s.URI);
                params.put("sessionID", s.SessionID);
                params.put("acousticness", String.valueOf(s.Acousticness));
                params.put("analysisURL", s.AnalysisURL);
                params.put("danceability", String.valueOf(s.Danceability));
                params.put("durationMS", String.valueOf(s.Duration));
                params.put("energy", String.valueOf(s.Energy));
                params.put("songID", s.SongID);
                params.put("instrumentalness", String.valueOf(s.Instrumentalness));
                params.put("songKey", String.valueOf(s.SongKey));
                params.put("liveness", String.valueOf(s.Liveness));
                params.put("loudness", String.valueOf(s.Loudness));
                params.put("songMode", String.valueOf(s.SongMode));
                params.put("speechiness", String.valueOf(s.Speechiness));
                params.put("tempo", String.valueOf(s.Tempo));
                params.put("timeSignature", String.valueOf(s.TimeSignature));
                params.put("trackhref", s.Trackhref);
                params.put("valence", String.valueOf(s.Valence));
                params.put("updateType", "song");

                return params;
            }
        }
                ;

        VolleyHelper.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void exportParticipantListEntry(){
        Log.d(TAG, "exporting participant");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            if(Response.equals("OK")){
                                Log.d(TAG, "Saved Participant");

                            }
                            else{
                                Log.d(TAG, "Participant: " + Response);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("participantID", SessionData.get().getPID());
                params.put("updateType", "participant");


                return params;
            }
        }
                ;

        VolleyHelper.getInstance(mContext).addToRequestQueue(stringRequest);

    }

    private void exportPainLogEntries(){

        PainLogCursorWrapper cursor = queryLogs(PainLogTable.Cols.pSID + " = ?",
                new String[] { SessionData.get().getSID() });

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                PainLog log = cursor.getPainLog();
                exportPainLogEntry(log);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    private void exportPainLogEntry(PainLog l){
        Log.d(TAG, "exporting a painLog");

        final PainLog log = l;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            if(Response.equals("OK")){
                                Log.d(TAG, "Saved PainLog");

                            }
                            else{
                                Log.d(TAG, "PainLog: " + Response);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sessionID", SessionData.get().getSID());
                params.put("timeRecorded", log.timeStamp);
                params.put("painLVL", String.valueOf(log.level));
                params.put("updateType", "painlog");


                return params;
            }
        }
                ;

        VolleyHelper.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!= null && networkInfo.isConnected());
    }
}
