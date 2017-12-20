package com.lifeng.f300.sqlite.db;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.common.entites.TransDataBrief;
import com.lifeng.f300.common.utils.LogUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by happen on 2017/8/30.
 */
public class DbManager {
    private static BriteDatabase briteDatabase;
    private static Context mApplicationContent;

    public static void initialize(Application app){
        LogUtils.d("wang","Dbmanager initialize");
        mApplicationContent = app.getApplicationContext();
        SqlBrite sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                //JUtils.Log(message);
                LogUtils.d("wang",message);
            }
        });
        // 执行slqbirte 构造数据库的语句
        briteDatabase = sqlBrite.wrapDatabaseHelper(new DBHelper(mApplicationContent), Schedulers.io());
        briteDatabase.setLoggingEnabled(true);
        LogUtils.d("wang","Dbmanager initialize2");
    }
   // public static  BriteDatabase getBriteDatabase(){ return briteDatabase; }


    /*public static Observable<List<OperatorInfoBrief>> queryPersonByOperatorIdOTB(final String operatorId) {
        LogUtils.d("wang","operatorId :"+operatorId);
        return briteDatabase.createQuery(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO, "SELECT * FROM "
                        + OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO
                        + " WHERE "
                        + OperatorInfoDBTable.COLUMN_OPERATORID
                        + " = ?"
                , operatorId)
                .map(new Func1<SqlBrite.Query, List<OperatorInfoBrief>>() {
                    @Override
                    public List<OperatorInfoBrief> call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        List<OperatorInfoBrief> operatorInfoBriefs = new ArrayList<OperatorInfoBrief>();

                        for(int i=0; i<cursor.getCount();i++){
                            operatorInfoBriefs.add(new OperatorInfoBrief(cursor.getString(cursor.getColumnIndex(OperatorInfoDBTable.COLUMN_OPERATORID))
                            ,cursor.getString(cursor.getColumnIndex(OperatorInfoDBTable.COLUMN_PASSWORD))));
                        }
                        return operatorInfoBriefs;
                    }
                });
    }*/
    public static QueryObservable queryOperatorByOperatorIdOTB(final String operatorId) {
        LogUtils.d("wang","operatorId :"+operatorId);
        return briteDatabase.createQuery(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO, "SELECT * FROM "
                        + OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO+ " WHERE "
                        + OperatorInfoDBTable.COLUMN_OPERATORID
                        + " = ?" , operatorId);


    }
    public static  QueryObservable queryOperatorsOTB(){
        return briteDatabase.createQuery(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO, "SELECT * FROM "
                        + OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO );
    }

    public static long addOperatorOTB(OperatorInfoBrief operator) {
        return briteDatabase.insert(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO, OperatorInfoDBTable.getInstance().to(operator));
    }

    public static int updatePwdByOperatorIdOTB(final ContentValues contentValues,final  String operatorId){
        return  briteDatabase.update(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO, contentValues,OperatorInfoDBTable.COLUMN_OPERATORID+"=?",operatorId);
    }
    public static int deleteOperatorByOpeartorIdOTB(final String operatorId){
        return  briteDatabase.delete(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO,OperatorInfoDBTable.COLUMN_OPERATORID+" =? ",operatorId);
    }

    public static Observable<List<OperatorInfoBrief>> checkOperatorAndPwdOTB(final String name,final String pwd){
        JUtils.Log("name:"+name+"  pwd:"+pwd);
        if(briteDatabase == null) JUtils.Log("briteDatabase == null");
       return briteDatabase.createQuery(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO,
                "select * from " + OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO +
                        " where " + OperatorInfoDBTable.COLUMN_OPERATORID + " = ?  and "
                        + OperatorInfoDBTable.COLUMN_PASSWORD + " =? ", name, pwd)
                .mapToList(new Func1<Cursor, OperatorInfoBrief>() {
                               @Override
                               public OperatorInfoBrief call(Cursor cursor) {
                                   LogUtils.d("wang","cursor"+ cursor.getCount());
                                   return OperatorInfoDBTable.getInstance().from(cursor);
                               }
                           }
                );
    }

    public static  QueryObservable queryTransDataTTB(){
        return briteDatabase.createQuery(TransDataDBTable.TABLE_NAME_TRANS_DATA, "SELECT * FROM "
                + TransDataDBTable.TABLE_NAME_TRANS_DATA );
    }

    public static long addTransDataTTB(TransDataBrief transDataBrief){
        return briteDatabase.insert(TransDataDBTable.TABLE_NAME_TRANS_DATA,TransDataDBTable.getInstance().to(transDataBrief));
    }
    public static int deleteTransDataTTB(final String serialNum,final String batchNum){
        return  briteDatabase.delete(TransDataDBTable.TABLE_NAME_TRANS_DATA,TransDataDBTable.Column.BATCH_NO+" =?  and "+TransDataDBTable.Column.SERIAL_NUMBER+" =? ",batchNum,serialNum);
    }

}

