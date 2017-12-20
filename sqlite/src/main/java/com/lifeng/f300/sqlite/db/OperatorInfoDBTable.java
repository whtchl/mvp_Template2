package com.lifeng.f300.sqlite.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.lifeng.f300.common.entites.OperatorInfoBrief;


/**
 * Created by happen on 2017/8/22.
 */

public class OperatorInfoDBTable extends AbsDBTable<OperatorInfoBrief> {
    private static OperatorInfoDBTable instance = new OperatorInfoDBTable();

    public static OperatorInfoDBTable getInstance() {
        return instance;
    }

    public static final String TABLE_NAME_OPERATOR_INFO = "operatorInfo";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_OPERATORID = "operatorId";
    public static final String COLUMN_PASSWORD = "password";


    public static final String sql1="insert into operatorInfo ("+COLUMN_ID + ","+ COLUMN_OPERATORID +","+COLUMN_PASSWORD+") values(1, '99', '123456')";
    public static final String sql2="insert into operatorInfo ("+COLUMN_ID + ","+ COLUMN_OPERATORID +","+COLUMN_PASSWORD+") values(2, '01', '0000')";
    public static final String sql3="insert into operatorInfo ("+COLUMN_ID + ","+ COLUMN_OPERATORID +","+COLUMN_PASSWORD+") values(3, '02', '0000')";
    public static final String sql4="insert into operatorInfo ("+COLUMN_ID + ","+ COLUMN_OPERATORID +","+COLUMN_PASSWORD+") values(4, '03', '0000')";
    public static final String sql5="insert into operatorInfo ("+COLUMN_ID + ","+ COLUMN_OPERATORID +","+COLUMN_PASSWORD+") values(5, '04', '0000')";


    @Override
    public String create() {
        return "CREATE TABLE " + TABLE_NAME_OPERATOR_INFO + " (" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_OPERATORID + " text," +
                COLUMN_PASSWORD + " text" +
                ");";
    }

    @Override
    public ContentValues to(OperatorInfoBrief object) {
        ContentValues vals = new ContentValues();
        vals.put(COLUMN_OPERATORID, object.getOperatorId());
        vals.put(COLUMN_PASSWORD, object.getPassword());
        return vals;
    }

    @Override
    public OperatorInfoBrief from(Cursor cursor) {
        OperatorInfoBrief operatorInfoBrief = new OperatorInfoBrief(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPERATORID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
        );
        return operatorInfoBrief;
    }
}
