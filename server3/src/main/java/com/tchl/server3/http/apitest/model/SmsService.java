package com.tchl.server3.http.apitest.model;

import com.tchl.server3.http.apitest.bean.SzxTransResponse;
import com.tchl.server3.http.subscriber.HttpResult;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by happen on 2017/12/19.
 */

public interface SmsService {

    String BASE_URL = "http://www.shizexian.com/api/";
    /*食责险发送短信*/
    @FormUrlEncoded
    @POST("IThird/smsPolicy2")
    //Observable<SzxTransResponse> smsPolicy(@FieldMap Map<String, String> requestBody);
    Single<HttpResult<SzxTransResponse>> smsPolicy(@FieldMap Map<String, String> requestBody);
}
