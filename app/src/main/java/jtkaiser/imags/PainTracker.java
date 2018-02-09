package jtkaiser.imags;

import android.content.Context;
import android.util.Log;

/**
 * Created by jtkai on 1/29/2018.
 */

public class PainTracker {
    private static PainTracker sPainTracker;

    private int mLastValue;

    private DBHelper mDBHelper;

    public static PainTracker get(Context context){
        if (sPainTracker == null) {
            sPainTracker = new PainTracker(context);
        }

        return sPainTracker;
    }

    private PainTracker(Context context){
        mDBHelper = new DBHelper(context);
        mLastValue = 0;

        PainLog value;
        if(sPainTracker == null){
            value = new PainLog(0);
        }
        else {
            value = new PainLog(sPainTracker.getLastValue()); //create painlog
        }
        mDBHelper.createPainLogpain(value);
        Log.d("Pain Rating: ", String.valueOf(value.getPain()));
        ; //insert painlog with only a value in db
        //db.getAllPainLogs(); //

        mDBHelper.closeDB();

    }

    public int getLastValue(){
        return mLastValue;
    }

    public void setValue(int value){
        mLastValue = value;
    }
}
