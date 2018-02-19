package jtkaiser.imags;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jtkai on 1/29/2018.
 */

public class PainTracker {
    private static PainTracker sPainTracker;

    private int mLastValue;

    private DatabaseHelper mDBHelper;
    private List<PainLog> pList;
    //private static SQLiteDatabase db;

    public static PainTracker get(Context context){
        if (sPainTracker == null) {
            sPainTracker = new PainTracker(context);
        }

        return sPainTracker;
    }

    private PainTracker(Context context){
        mDBHelper = new DatabaseHelper(context);
        mLastValue = 0;
        pList = new ArrayList<PainLog>();

        PainLog value, test;
        if(sPainTracker == null){
            value = new PainLog(0);
        }
        else {
            value = new PainLog(sPainTracker.getLastValue()); //create painlog
        }
        //test = new PainLog(null);
        //mDBHelper.createPainLog(test);
        value.setStart(mDBHelper.getDateTime());
        //value.setSID();
        mDBHelper.createPainLogpain(value);
        Log.d("Pain Rating: ", String.valueOf(value.getPain()));
        Log.d("Time: ", value.getStart());
        ; //insert painlog with only a value in db
        pList = mDBHelper.getAllPain();
        // //
        mDBHelper.closeDatabase();
    }

    public int getLastValue(){
        return mLastValue;
    }

    public void setValue(int value){
        mLastValue = value;
    }
}
