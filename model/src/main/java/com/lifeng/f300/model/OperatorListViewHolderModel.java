package com.lifeng.f300.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;

import com.jude.utils.JUtils;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.sqlite.db.DBHelper;
import com.lifeng.f300.sqlite.db.DbManager;
import com.lifeng.f300.sqlite.db.OperatorInfoDBTable;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by happen on 2017/8/29.
 */

public class OperatorListViewHolderModel extends AbsModel {

    public static OperatorListViewHolderModel getInstance() {
        return getInstance(OperatorListViewHolderModel.class);
    }


    @Override
    protected void onAppCreate(Context ctx) {
               SqlBrite mSqlBrite = SqlBrite.create();
    }

    /**
     * 修改密码
     * @param id
     * @param operatorId
     * @param newPwd
     * @return
     */
    public int  mUpdateOperatorPwd(final String id, final String operatorId, final String newPwd) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(OperatorInfoDBTable.COLUMN_PASSWORD,newPwd);
            JUtils.Log("pwd:"+newPwd+" operatorid:"+operatorId );
            //update 返回>0成功，返回0失败
            return DbManager.updatePwdByOperatorIdOTB(contentValues,operatorId);
        }catch(Exception e1) {
            JUtils.Log("DB", "updated ERROR:" + e1.getLocalizedMessage());
            return 0;
        }
    }

    /**
     * 删除操作员
     * @param operatorId
     * @return
     */
    public int mDeleteOperator(final String operatorId){
        return DbManager.deleteOperatorByOpeartorIdOTB(operatorId);
    }

}
