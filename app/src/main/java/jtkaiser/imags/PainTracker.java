package jtkaiser.imags;

import android.content.Context;
import android.util.Log;

/**
 * Created by jtkai on 1/29/2018.
 */

public class PainTracker {
    private static PainTracker sPainTracker;

    private int mLastValue;

    private static Context applicationContext;

    public static PainTracker get(){
        if (sPainTracker == null) {
            sPainTracker = new PainTracker();
        }

        DBHelper dbH = new DBHelper(getApplicationContext()); //might be needed
        PainLog value = new PainLog(sPainTracker.getLastValue()); //create painlog

        dbH.createPainLogpain(value);
        Log.d("Pain Rating: ", String.valueOf(value.getPain()));
            ; //insert painlog with only a value in db
            //db.getAllPainLogs(); //

        dbH.closeDB();
        return sPainTracker;
    }

    private PainTracker(){
        mLastValue = 0;

    }

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public int getLastValue(){
        return mLastValue;
    }

    public void setValue(int value){
        mLastValue = value;
    }
}
