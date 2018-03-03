package jtkaiser.imags.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import jtkaiser.imags.PainLog;
import jtkaiser.imags.database.IMAGSDbSchema.PainLogTable;

/**
 * Created by jtkai on 3/2/2018.
 */

public class PainLogCursorWrapper extends CursorWrapper{
    public PainLogCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public PainLog getPainLog(){
        PainLog log = new PainLog();
        log.timeStamp = getString(getColumnIndex(PainLogTable.Cols.timeStamp));
        log.level = getInt(getColumnIndex(PainLogTable.Cols.painLVL));

        return log;
    }
}
