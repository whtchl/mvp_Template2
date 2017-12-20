package com.tchl.server3.http.apitest.model;


import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lifeng.f300.common.utils.EncryptUtils;
import com.lifeng.f300.common.utils.Tools;
import com.tchl.server3.http.apitest.GankService;
import com.tchl.server3.http.apitest.bean.DailyList;
import com.tchl.server3.http.apitest.bean.GanHuoData;
import com.tchl.server3.http.apitest.bean.SzxTransResponse;
import com.tchl.server3.http.subscriber.HttpResultSubscriber;
import com.tchl.server3.http.subscriber.ServiceFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by happen on 2017/12/18.
 */

public class GankModel {

    public static void loadData(final int pageIndex) {
       //http://www.gank.io/api/data/%E7%9E%8E%E6%8E%A8%E8%8D%90/20/1
        String mType = "瞎推荐";
        ServiceFactory.getInstance().createService(GankService.class)
                .getGanHuo(mType, pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               /* .compose(this.<HttpResult<List<GanHuoData>>>bindToLifecycle())
                .compose(RxUtils.<HttpResult<List<GanHuoData>>>defaultSchedulers_single())*/

                .subscribe(new HttpResultSubscriber<List<GanHuoData>>() {
                    @Override
                    public void _onSuccess(List<GanHuoData> list) {
                        //onDataSuccessReceived(pageIndex, list);
                        JUtils.Log("loadData onSuccess");
                        JUtils.Toast("loadData onSuccess");
                    }

                    @Override
                    public void _onError(Throwable e) {
                        //showError(new Exception(e));
                        //ToastUtils.getInstance().showToast(e.getMessage());
                        JUtils.Log(e.getMessage());
                        JUtils.Toast(e.getMessage());
                    }

                });

    }

    public static void mgetRecentlyGanHuo(){
        //http://www.gank.io/api/day/2017%2F12%2F15
        ServiceFactory.getInstance().createService(GankService.class)
                .getRecentlyGanHuo("2017/12/15")
                /*.compose(this.<HttpResult<DailyList>>bindToLifecycle())
                .compose(RxUtils.<HttpResult<DailyList>>defaultSchedulers_single())*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultSubscriber<DailyList>() {

                    @Override
                    public void _onError(Throwable e) {
                        /*showError(new Exception(e));*/
                        JUtils.Log(e.getMessage());
                        JUtils.Toast(e.getMessage());
                    }

                    @Override
                    public void _onSuccess(DailyList recentlyBean) {
                        List list = new ArrayList<>();
                        StringBuilder str = new StringBuilder();

                        if (recentlyBean != null) {
                            if (recentlyBean.get休息视频() != null) {
                               /* list.add(new DailyTitle("休息视频"));
                                list.addAll(recentlyBean.get休息视频());*/
                                str.append("休息视频");
                            }
                            if (recentlyBean.getAndroid() != null) {
                                /*list.add(new DailyTitle("Android"));
                                list.addAll(recentlyBean.getAndroid());*/
                                str.append("Android");
                            }
                            if (recentlyBean.getIOS() != null) {
                                /*list.add(new DailyTitle("iOS"));
                                list.addAll(recentlyBean.getIOS());*/
                                str.append("iOS");
                            }
                            if (recentlyBean.get前端() != null) {
                                /*list.add(new DailyTitle("前端"));
                                list.addAll(recentlyBean.get前端());*/
                                str.append("前端");
                            }
                            if (recentlyBean.getApp() != null) {
                               /* list.add(new DailyTitle("App"));
                                list.addAll(recentlyBean.getApp());*/
                                str.append("App");
                            }
                            if (recentlyBean.get瞎推荐() != null) {
                                /*list.add(new DailyTitle("瞎推荐"));
                                list.addAll(recentlyBean.get瞎推荐());*/
                                str.append("瞎推荐");
                            }


                        }
                        JUtils.Toast(str.toString());
                        //onDataSuccessReceived(pageIndex, list);
                    }
                });


    }

    public static void mSms(){

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date=sdf.format(new java.util.Date());
        HashMap<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("third", "xdjk");
        hashMap.put("mchcode", "666666");
        hashMap.put("mchname", "");
        hashMap.put("username", "tchl");
        hashMap.put("mobile", "15957471109");
        hashMap.put("money", "123");
        hashMap.put("paytime", date);
        JUtils.Log(date+"==========================");


        HashMap<String,String> hashMap2 = new HashMap<String, String>();
        //long time = System.currentTimeMillis();
        hashMap.put("third", "xdjk");
        hashMap.put("mchcode", "666666");
        hashMap2.put("mchname", "");
        hashMap2.put("username", "tchl");
        hashMap2.put("mobile", "15957471109");
        hashMap2.put("money", "123");
        hashMap2.put("paytime",hashMap.get("paytime"));
        hashMap2.put("sign", Tools.getInstance().strCalcSHA1(hashMap, "9A9B9C9D9E9F9G9H9" + "api/ithird/smspolicy2"));

        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap2);
        JUtils.Log("wang","sendSms:"+strEntity);

        ServiceFactory.getInstance().createService(SmsService.class)
                .smsPolicy(hashMap2)
                /*.compose(this.<HttpResult<DailyList>>bindToLifecycle())
                .compose(RxUtils.<HttpResult<DailyList>>defaultSchedulers_single())*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultSubscriber<SzxTransResponse>() {

                    @Override
                    public void _onError(Throwable e) {
                        if(e.getCause().toString().contains("TimeoutException")){
                            JUtils.ToastLong("服务器连接超时");
                        }else if (e.getCause().toString().contains("ConnectException")){
                            JUtils.ToastLong("服务器连接失败");
                        }else{
                            JUtils.ToastLong(e.getCause().toString());
                        }
                    }

                    @Override
                    public void _onSuccess(SzxTransResponse mSzxTransResponse) {
                        JUtils.Toast("Send sms success");
                    }
                });

    }
}
