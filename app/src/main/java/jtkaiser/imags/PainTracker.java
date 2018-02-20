package jtkaiser.imags;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jtkai on 1/29/2018.
 */

public class PainTracker {
    private static PainTracker sPainTracker;
    private UUID sid;
    private int mLastValue;
    private List<PainLog> pList;

    public static PainTracker get(Context context, UUID sid){
        if (sPainTracker == null) {
            sPainTracker = new PainTracker(context, sid);
        }

        return sPainTracker;
    }

    private PainTracker(Context context, UUID sid){
        sid = sid;
        mLastValue = 0;
        pList = new ArrayList<PainLog>();

//        pList = mDBHelper.getAllPain();
//        mDBHelper.closeDatabase();
    }

    public int getLastValue(){
        return mLastValue;
    }

    public void setValue(int value){
        mLastValue = value;
        pList.add(PainLog.newLog(sid, value));
    }
}
