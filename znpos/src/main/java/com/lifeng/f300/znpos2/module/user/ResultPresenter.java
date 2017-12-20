package com.lifeng.f300.znpos2.module.user;

import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.model.ResultModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by happen on 2017/9/6.
 */

public class ResultPresenter   extends Presenter<ResultActivity> {

    /**
     * 校验密码
     * @param pwd
     */
    public void  pCheckPwd(final String pwd ){
        ResultModel.getInstance().mCheckPwd(pwd).subscribe(new Subscriber<List<OperatorInfoBrief>>() {
            @Override
            public void onCompleted() {
                LogUtils.d("wang","pQueryDB onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("wang","pQueryDB onError");
            }

            @Override
            public void onNext(List<OperatorInfoBrief> operatorInfoBriefs) {
                LogUtils.d("wang", "pQueryDB call " + Thread.currentThread().getName() +
                        " Thread id:" + Thread.currentThread().getId());
                if(operatorInfoBriefs.size()==1){
                    for(int i=0; i<operatorInfoBriefs.size();i++){
                        LogUtils.d("wang",operatorInfoBriefs.get(i).getOperatorId()+ "  "+operatorInfoBriefs.get(i).getPassword());
                    }
                    getView().sendMsgCancelTrans();
                }else{
                    getView().toastWrongPwd();
                }
            }
        });


       /* //先查询该操作员是否已经存在。如果存在，这不添加，否则添加
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
pwd
                    @Override
                    public void onNext(List<OperatorInfoBrief> operatorInfoBriefs) {
                        if(operatorInfoBriefs.size() > 0){
                            JUtils.Toast(getView().getString(R.string.operator_number)+":"+operator.getOperatorId()+getView().getString(R.string.ycz));
                        }else{
                            getView().sendMsgAddOperator(operator);
                        }
                    }
                });*/
    }
}
