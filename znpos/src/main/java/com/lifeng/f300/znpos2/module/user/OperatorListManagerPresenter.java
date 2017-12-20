package com.lifeng.f300.znpos2.module.user;

import android.content.Intent;
import android.os.Bundle;

import com.jude.utils.JUtils;
import com.lifeng.beam.expansion.list.BeamListActivityPresenter;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.config.SharePreferenceKey;
import com.lifeng.f300.model.OperatorListManagerModel;
import com.lifeng.f300.model.SignInModel;
import com.lifeng.f300.znpos2.R;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by happen on 2017/8/28.
 */

public class OperatorListManagerPresenter extends BeamListActivityPresenter<OperatorListManagerActivity,OperatorInfoBrief> {
    @Override
    protected void onCreate(OperatorListManagerActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        //SignInModel.getInstance().().subscribe(getRefreshSubscriber());
        JUtils.Log("OperatorListManagerPresenter  call onCreate");
        SignInModel.getInstance().mQueryOperators(JUtils.getSharedPreference().getString("operatorId","")).unsafeSubscribe(getRefreshSubscriber());
    }

    @Override
    public void onRefresh(){
        //getAdapter().clear();
        OperatorListManagerModel.getInstance().mQueryOperators(JUtils.getSharedPreference().getString(SharePreferenceKey.OPETATOR_NUMBER,"")).unsafeSubscribe(getRefreshSubscriber());
        JUtils.Log("onRresh");
    }

    /**
     * 查询操作人员
     * @param operator
     */
    public void  pQueryOperatorByOperator(final OperatorInfoBrief operator){
        //先查询该操作员是否已经存在。如果存在，这不添加，否则添加
        OperatorListManagerModel.getInstance().mQueryOperatorByOperatorId(operator.getOperatorId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OperatorInfoBrief>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<OperatorInfoBrief> operatorInfoBriefs) {
                        if(operatorInfoBriefs.size() > 0){
                            JUtils.Toast(getView().getString(R.string.operator_number)+":"+operator.getOperatorId()+getView().getString(R.string.ycz));
                        }else{
                            getView().sendMsgAddOperator(operator);
                        }
                    }
                });
    }

    public void pAddOperator(final OperatorInfoBrief operatorInfoBrief){
        if(OperatorListManagerModel.getInstance().maddOperator(operatorInfoBrief)>0){
            JUtils.Toast(getView().getString(R.string.add_operator_ok));
            Intent intent = new Intent();
            intent.setAction("com.refresh.operators");
            getView().sendBroadcast(intent);

            //onRefresh();
        }else {
            JUtils.Toast(getView().getString(R.string.add_operator_no));
        }
    }


}
