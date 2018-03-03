package jtkaiser.imags;

import android.content.Context;

import jtkaiser.imags.database.DataManager;

/**
 * Created by jtkai on 1/29/2018.
 */

public class PainTracker {
    private static PainTracker sPainTracker;
    private Context mContext;
    private int mLastValue;

    public static PainTracker get(Context context){
        if (sPainTracker == null) {
            sPainTracker = new PainTracker(context);
        }

        return sPainTracker;
    }

    private PainTracker(Context context){
        mContext = context;
        mLastValue = 0;
    }

    public int getLastValue(){
        return mLastValue;
    }

    public void setValue(int value){
        mLastValue = value;
        DataManager.get(mContext).createPainLogEntry();
    }
}
