package com.tingchung.life.electric;
/**
 * Created by Administrator on 2017/1/12.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ElectricSQLiteHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "electric.db";  //資料庫名稱
    private final static int DATABASE_VERSION = 1;  //資料庫版本
    private static SQLiteDatabase database;

    public ElectricSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new ElectricSQLiteHelper(context, DATABASE_NAME,
                    null, DATABASE_VERSION).getWritableDatabase();
        }
        return database;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        getWritableDatabase()
        db.execSQL(ElectricDbCtrl.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
