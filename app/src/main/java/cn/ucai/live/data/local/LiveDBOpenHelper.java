package cn.ucai.live.data.local;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.LiveHelper;

/**
 * Created by Administrator on 2017/6/9.
 */
public class LiveDBOpenHelper extends SQLiteOpenHelper{
    private static final String TAG = "LiveDBOpenHelper";

    private static final int versionNumber = 1;
    private static LiveDBOpenHelper instance;

    private static final String GIFT_TABLE_CREATE = "CREATE TABLE "
            + LiveDao.GIFT_TABLE_NAME + " ("
            + LiveDao.GIFT_COLUMN_NAME + " TEXT, "
            + LiveDao.GIFT_COLUMN_URL + " TEXT, "
            + LiveDao.GIFT_COLUMN_PRICE + " INTEGER, "
            + LiveDao.GIFT_COLUMN_ID + " INTEGER PRIMARY KEY);";

    public LiveDBOpenHelper(Context context) {
        super(context, getDatabaseNames(context), null, versionNumber);
    }

    private static String getDatabaseNames(Context context) {
        return context.getPackageName()+".db";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(GIFT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public static LiveDBOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LiveDBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
