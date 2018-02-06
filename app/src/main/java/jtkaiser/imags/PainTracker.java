package jtkaiser.imags;

/**
 * Created by jtkai on 1/29/2018.
 */

public class PainTracker {
    private static PainTracker sPainTracker;

    private int mLastValue;

    private static DBHelper db;

    public static PainTracker get(){
        if (sPainTracker == null) {
            sPainTracker = new PainTracker();
        }
        PainLog value = new PainLog(sPainTracker.getLastValue()); //create painlog
        //db.createPainLog(value); //insert painlog with only a value in db
        //Log.d("Pain Rating: ", String.valueOf(value.getPain()));
        //db.closeDB();
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
