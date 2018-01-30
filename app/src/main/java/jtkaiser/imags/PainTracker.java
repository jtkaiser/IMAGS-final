package jtkaiser.imags;

/**
 * Created by jtkai on 1/29/2018.
 */

public class PainTracker {
    private static PainTracker sPainTracker;

    private int mLastValue;

    public static PainTracker get(){
        if (sPainTracker == null) {
            sPainTracker = new PainTracker();
        }
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
