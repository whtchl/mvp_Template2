package com.lifeng.f300.sqlite.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.utils.LogUtils;

/**
 * Created by happen on 2017/8/22.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "OperatorStore.db";
    public static final int DATABASE_VERSION = 11;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        JUtils.Log("create db");
        //新建OperatorInfo表
        db.execSQL(OperatorInfoDBTable.getInstance().create());
        db.execSQL(OperatorInfoDBTable.sql1);
        db.execSQL(OperatorInfoDBTable.sql2);
        db.execSQL(OperatorInfoDBTable.sql3);
        db.execSQL(OperatorInfoDBTable.sql4);
        db.execSQL(OperatorInfoDBTable.sql5);
        //新建TransData表
        db.execSQL(TransDataDBTable.getInstance().create());


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.d("wang","onUpgrade database");
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS"+OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO);
            db.execSQL(OperatorInfoDBTable.getInstance().create());
            db.execSQL(OperatorInfoDBTable.sql1);
            db.execSQL(OperatorInfoDBTable.sql2);
            db.execSQL(OperatorInfoDBTable.sql3);
            db.execSQL(OperatorInfoDBTable.sql4);
            db.execSQL(OperatorInfoDBTable.sql5);

            db.execSQL("DROP TABLE IF EXISTS " + TransDataDBTable.TABLE_NAME_TRANS_DATA);
            db.execSQL(TransDataDBTable.getInstance().create());
        }
    }
}
