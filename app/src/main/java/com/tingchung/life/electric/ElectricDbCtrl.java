package com.tingchung.life.electric;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.BaseColumns._ID;

public class ElectricDbCtrl {

    public static final String TABLE_NAME = "electric";  //表格名稱

    public static final String DATE_COLUMN = "date";
    public static final String TIME_COLUMN = "time";
    public static final String NUMBER_COLUMN = "number";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE_COLUMN + " TEXT  NOT NULL, "
            + TIME_COLUMN + " TEXT  NOT NULL, "
            + NUMBER_COLUMN + " REAL);";

    private SQLiteDatabase db;

    private static volatile ElectricDbCtrl instance = null;

    private ElectricDbCtrl() {
    }

    public static ElectricDbCtrl getInstance() {
//        if (instance == null) {
//            synchronized (ElectricDbCtrl.class) {
//                if (instance == null) {
//                    instance = new ElectricDbCtrl();
//                }
//            }
//        }
        return instance;
    }

    public static ElectricDbCtrl getInstance(Context context) {
        if (instance == null) {
            synchronized (ElectricDbCtrl.class) {
                if (instance == null) {
                    instance = new ElectricDbCtrl(context);
                }
            }
        }
        return instance;
    }

    public ElectricDbCtrl(Context context) {
        this.db = ElectricSQLiteHelper.getDatabase(context);
    }

    public ElectricObject insert(ElectricObject item) {
        ContentValues cv = new ContentValues();

        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DATE_COLUMN, item.getDate());
        cv.put(TIME_COLUMN, item.getTime());
        cv.put(NUMBER_COLUMN, item.getNumber());

        long id = db.insert(TABLE_NAME, null, cv);
        item.setId(id);
        return item;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = _ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public List<ElectricObject> getAll() {
        List<ElectricObject> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public ElectricObject getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        ElectricObject result = new ElectricObject();

        result.setId(cursor.getLong(0));
        result.setDate(cursor.getString(1));
        result.setTime(cursor.getString(2));
        result.setNumber(cursor.getDouble(3));

        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    public void sample() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date current = new Date();
        ElectricObject item1 = new ElectricObject(0, dateFormat.format(current),  timeFormat.format(current) , 123);
        ElectricObject item2 = new ElectricObject(0, dateFormat.format(current),  timeFormat.format(current) , 234.3);
        ElectricObject item3 = new ElectricObject(0, dateFormat.format(current),  timeFormat.format(current) , 453.7);
        ElectricObject item4 = new ElectricObject(0, dateFormat.format(current),  timeFormat.format(current) , 654.4);

        insert(item1);
        insert(item2);
        insert(item3);
        insert(item4);
    }

}
