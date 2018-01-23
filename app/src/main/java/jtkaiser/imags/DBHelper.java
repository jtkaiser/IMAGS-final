package jtkaiser.imags;

/**
 * Created by amybea on 1/23/2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "sessionBase.db";

    public DBHelper(){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
}

