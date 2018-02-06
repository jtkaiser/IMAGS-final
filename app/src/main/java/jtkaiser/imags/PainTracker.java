package jtkaiser.imags;

import android.content.Context;

/**
 * Created by jtkai on 1/29/2018.
 */

public class PainTracker {
    private static PainTracker sPainTracker;

    private int mLastValue;

    private static DBHelper dbH;
    //private static SQLiteDatabase db;
    private static Context context;

    public static PainTracker get(){
        if (sPainTracker == null) {
            sPainTracker = new PainTracker();
        }
        dbH = new DBHelper(context);
        PainLog value = new PainLog(sPainTracker.getLastValue()); //create painlog
        //dbH.onCreate(db);
        //dbH.createPainLogpain(value);
            ; //insert painlog with only a value in db
            //db.getAllPainLogs(); //Log.d("Pain Rating: ", String.valueOf(value.getPain()));

        //dbH.closeDB();
        return sPainTracker;
    }

    private PainTracker(){
        mLastValue = 0;

    }

    public int getLastValue(){
        return mLastValue;
    }

    public void setValue(int value){
        mLastValue = value;
    }
}
