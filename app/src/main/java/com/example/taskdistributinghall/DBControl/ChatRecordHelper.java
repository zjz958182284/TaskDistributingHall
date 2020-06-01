package com.example.taskdistributinghall.DBControl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ChatRecordHelper extends SQLiteOpenHelper {
    private static final int version=1;
    private static String db_name="chat";
    public ChatRecordHelper( Context context) {
        super(context,db_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql=" create table chatRecordTemp(\n" +
                " senderphone varchar(15),\n" +
                " receiverphone varchar(15),\n" +
                "date TIMESTAMP default CURRENT_TIMESTAMP,\n" +
                "record varchar(100),\n" +
                "issend  enum(1,0),\n"+
                "primary key(senderphone,receiverphone,date)\n" +
                ");";
        String sql2="create table chatTimeStamp(\n" +
                "senderphone varchar(15),\n" +
                "  receiverphone varchar(15),\n" +
                " date datetime default CURRENT_TIMESTAMP,\n" +
                " primary key(senderphone,receiverphone,date)\n" +
                " );";
        db.execSQL(sql);
        db.execSQL(sql2);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
