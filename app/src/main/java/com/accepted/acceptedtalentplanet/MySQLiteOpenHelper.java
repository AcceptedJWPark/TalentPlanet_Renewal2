package com.accepted.acceptedtalentplanet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private final String TAG = "MySQLiteOpenHelper";

    public MySQLiteOpenHelper(Context mContext, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(mContext, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d(TAG, "CREATE TABLE");
        String sql = "CREATE TABLE IF NOT EXISTS TB_CHAT_LOG (MESSAGE_ID INTEGER PRIMARY KEY, ROOM_ID INTEGER, MASTER_ID TEXT, USER_ID TEXT, CONTENT TEXT, CREATION_DATE TEXT, READED_FLAG TEXT, POINT_MSG_FLAG TEXT DEFAULT 0, POINT_SEND_FLAG TEXT DEFAULT 0)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
//        Log.d(TAG, "ALTER TABLE");
//        String sql = "ALTER TABLE TB_CHAT_LOG ADD POINT_SEND_FLAG TEXT DEFAULT 0";
//        db.execSQL(sql);
//
//        sql = "ALTER TABLE TB_CHAT_LOG ADD POINT_MSG_FLAG TEXT DEFAULT 0";
//        db.execSQL(sql);
    }
}
