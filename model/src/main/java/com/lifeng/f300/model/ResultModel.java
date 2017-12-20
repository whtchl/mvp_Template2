package com.lifeng.f300.model;

import android.database.Cursor;

import com.jude.utils.JUtils;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.sqlite.db.DbManager;
import com.lifeng.f300.sqlite.db.OperatorInfoDBTable;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by happen on 2017/9/6.
 */

public class ResultModel extends AbsModel {
    public static ResultModel getInstance() {
        return getInstance(ResultModel.class);
    }

    public static Observable<List<OperatorInfoBrief>> mCheckPwd(final String pwd){
            return DbManager.checkOperatorAndPwdOTB("99", pwd).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /*public Observable<List<OperatorInfoBrief>> mQueryOperatorByOperatorId(final String operatorId) {
        JUtils.Log("call mQueryPersonByOperatorId ");
        return DbManager.queryOperatorByOperatorIdOTB(operatorId)
                .map(new Func1<SqlBrite.Query, List<OperatorInfoBrief>>() {
                    @Override
                    public List<OperatorInfoBrief> call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        List<OperatorInfoBrief> operatorInfoBriefs = new ArrayList<OperatorInfoBrief>();

                        for (int i = 0; i < cursor.getCount(); i++) {
                            operatorInfoBriefs.add(new OperatorInfoBrief(cursor.getString(cursor.getColumnIndex(OperatorInfoDBTable.COLUMN_OPERATORID))
                                    , cursor.getString(cursor.getColumnIndex(OperatorInfoDBTable.COLUMN_PASSWORD))));
                        }
                        return operatorInfoBriefs;
                    }
                });
    }*/
}
