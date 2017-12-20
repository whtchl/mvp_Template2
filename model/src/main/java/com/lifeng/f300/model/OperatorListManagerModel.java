package com.lifeng.f300.model;

import android.app.Application;
import android.database.Cursor;

import com.jude.utils.JUtils;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.server.service.DefaultTransform;
import com.lifeng.f300.sqlite.db.DbManager;
import com.lifeng.f300.sqlite.db.OperatorInfoDBTable;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by happen on 2017/8/30.
 */

public class OperatorListManagerModel extends AbsModel {

    public static OperatorListManagerModel getInstance() {
        return getInstance(OperatorListManagerModel.class);
    }

    public long maddOperator(final OperatorInfoBrief operator) {
        return DbManager.addOperatorOTB(operator);

    }

    public Observable<List<OperatorInfoBrief>> mQueryOperatorByOperatorId(final String operatorId) {
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
    }

    /**
     * 设置中，查询操作人员
     *
     * @param name 查询操作员
     * @return
     */
    public Observable<List<OperatorInfoBrief>> mQueryOperators(final String name) {
        if ("99".equals(name)) {
            return DbManager.queryOperatorsOTB()
                    .mapToList(new Func1<Cursor, OperatorInfoBrief>() {
                                   @Override
                                   public OperatorInfoBrief call(Cursor cursor) {
                                       LogUtils.d("wang", "mQueryOperators Func1 " + Thread.currentThread().getName() +
                                               " Thread id:" + Thread.currentThread().getId());
                                       return OperatorInfoDBTable.getInstance().from(cursor);
                                   }
                               }
                    ).subscribeOn(Schedulers.io())
                    .compose(new DefaultTransform<List<OperatorInfoBrief>>());
        } else {
            return DbManager.queryOperatorByOperatorIdOTB(name)
                    .mapToList(new Func1<Cursor, OperatorInfoBrief>() {
                                   @Override
                                   public OperatorInfoBrief call(Cursor cursor) {
                                       JUtils.Log("number :" + cursor.getCount());
                                       LogUtils.d("wang", "mQueryOperators Func1 " + Thread.currentThread().getName() +
                                               " Thread id:" + Thread.currentThread().getId());
                                       return OperatorInfoDBTable.getInstance().from(cursor);
                                   }
                               }
                    ).subscribeOn(Schedulers.io())
                    .compose(new DefaultTransform<List<OperatorInfoBrief>>());
        }
    }
}
