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
    private static final int DATABASE_VERSION = 1;
    private static LiveDBOpenHelper instance ;
    public LiveDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public LiveDBOpenHelper(Context context) {
        super(context,getUserDatabaseName(),null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    private static String getUserDatabaseName() {
        return  LiveHelper.getInstance().getCurrentUsernName() + "_demo.db";
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static LiveDBOpenHelper getInstance(Context context) {
        if(instance==null){
            instance = new LiveDBOpenHelper(context.getApplicationContext());
        }
        return null;
    }
    public void closeDB(){
        if(instance!=null){
            try{
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();

            }catch (Exception e){
                e.printStackTrace();
            }
            instance =null;
        }
    }
}
