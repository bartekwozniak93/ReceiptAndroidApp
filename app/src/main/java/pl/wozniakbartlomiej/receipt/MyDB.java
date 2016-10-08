package pl.wozniakbartlomiej.receipt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yoonsz25 on 10/8/2016.
 */

public class MyDB {
    MyDBHelper DBHelper;
    SQLiteDatabase db;
    final Context context;

    public MyDB(Context ctx){
        this.context = ctx;
        DBHelper = new MyDBHelper(this.context);
    }
    public MyDB open(){
        db = DBHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        DBHelper.close();
    }
    public long insertMethod(String name1_str, String name2_str){
        ContentValues initialValues =	new ContentValues();

        initialValues.put(MyDBHelper.columnName1,	name1_str);
        initialValues.put(MyDBHelper.columnName2,	name2_str);

        return db.insert(MyDBHelper.tableName,	null,	initialValues);
    }
    public int deleteMethod(String usernameToBeDeleted){
        return db.delete(MyDBHelper.tableName, MyDBHelper.columnName1+"="+usernameToBeDeleted, null);
    }
    public int updateMethod(String originalUsername, String name1_str, String name2_str){
        ContentValues initialValues = new ContentValues();
        initialValues.put(MyDBHelper.columnName1, name1_str);
        initialValues.put(MyDBHelper.columnName2, name2_str);
        return db.update(MyDBHelper.tableName, initialValues, MyDBHelper.columnName1+"="+originalUsername, null);
    }
    public Cursor getAllRecords(){
        return db.query(MyDBHelper.tableName,
                new String[]{MyDBHelper.columnName1,
                        MyDBHelper.columnName2,},
                null, null, null, null, null);
    }
    public Cursor getAllRecords(long id){
        Cursor c = db.query(MyDBHelper.tableName,
                new String[]{MyDBHelper.columnName1,
                        MyDBHelper.columnName2,},
                MyDBHelper.columnName1+"="+id,
                null, null, null, null, null);
        if(c!=null){
            c.moveToFirst();
        }
        return c;
    }
}
