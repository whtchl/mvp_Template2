package com.lifeng.f300.server.config;
import com.lifeng.f300.common.entites.BaDetail;
import com.lifeng.f300.common.entites.CancelTransResponBean;
import com.lifeng.f300.common.entites.LpResponse;
import com.lifeng.f300.common.entites.SettlementResponse;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.Tools;

import java.util.List;
import java.util.Map;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import rx.Observable;
import szx.config.config;
import szx.entites.SzxTransResponse;

/**
 * Created by happen on 2017/8/7.
 */

public interface Service {
    String BASE_URL = "http://111.1.41.104:8080/trade.htm";
    String BASE_URL_DEBUG = "http://111.1.41.104:8080/trade.htm";




    @FormUrlEncoded
    @POST("/")
    Observable<TransResponse>activate(@Field("json") String requestBody);

    @FormUrlEncoded
    @POST("/")
    Observable<TransResponse>logon(@Field("json") String requestBody);

    @FormUrlEncoded
    @POST("/")
    Observable<TransResponse>consume(@Field("json") String requestBody);

    @FormUrlEncoded
    @POST("/")
    Observable<TransResponse>checkUpdate(@Field("json") String requestBody);

    /*撤销交易*/
    @FormUrlEncoded
    @POST("/")
    Observable<CancelTransResponBean>cancel(@Field("json") String requestBody);

    /*查询单笔流水信息*/
    @FormUrlEncoded
    @POST("/")
    Observable<TransResponse>queryTrade(@Field("json") String requestBody);


    /*查询单笔流水信息*/
    @FormUrlEncoded
    @POST("/")
    TransResponse queryTrade2(@Field("json") String requestBody);


    /*结算*/
    @FormUrlEncoded
    @POST("/")
    Observable<SettlementResponse> logout(@Field("json") String requestBody);

    /*食责险发送短信*/
    @FormUrlEncoded
    @POST("/IThird/smsPolicy2")
    Observable<SzxTransResponse> smsPolicy(@FieldMap Map<String, String> requestBody);
   /* @GET("/smsPolicy")
    Observable<SzxTransResponse>smsPolicy(@Query("mchcode") String mchcode,
                                          @Query("mobile") String mobile,
                                          @Query("third") String third,
                                          @Query("paytime") String paytime,
                                          @Query("mchname") String mchname,
                                          @Query("username") String username,
                                          @Query("money") String money,
                                          @Query("sign") String sign
                                          );*/

    /*食责险添加报案信息*/
    @FormUrlEncoded
    @POST("/IThird/reportCase")
    Observable<SzxTransResponse> SubmitBaInfo(@FieldMap Map<String, Object> requestBody);

    /*食责险添加报案信息*/
    @FormUrlEncoded
    @POST("/IThird/reportCase")
    Observable<SzxTransResponse> SubmitBaInfo3(@FieldMap Map<String, Object> requestBody);//, @Field("picBase64") String picBase64

    /*食责险添加报案信息*/
    @FormUrlEncoded
    @POST("/IThird/reportCase")
    Observable<SzxTransResponse> SubmitBaInfo5(@FieldMap Map<String, String> requestBody);

    @FormUrlEncoded
    @POST("/IThird/reportCase")
    Observable<SzxTransResponse> SubmitBaInfo2(@Field("address") String address,
                                               @Field("happen_time") String happen_time,
                                               @Field("informant") String informant,
                                               @Field("informant_phone") String informant_phone,
                                               @Field("report_money") String report_money,
                                               @Field("picName[]") List<String> picName,
                                               @Field("cause") String cause,
                                               @Field("mchcode") String mchcode,
                                               @Field("third") String third,

                                               @Field("sign") String sign,
                                               @Field("picBase64[]") List<String> picBase6);//@Field("picBase64[]") List<String> picBase64

    @Multipart
    @POST("/IThird/reportCase")
    Observable<SzxTransResponse> SubmitIMGANDINFO(@Part("address") String address,
                                                  @Part("happen_time") String happen_time,
                                                  @Part("informant") String informant,
                                                  @Part("informant_phone") String informant_phone,
                                                  @Part("report_money") String report_money,
                                                  @Part("picName[]") List<String> picName,
                                                  @Part("cause") String cause,
                                                  @Part("mchcode") String mchcode,
                                                  @Part("third") String third,

                                                  @Part("sign") String sign);
    /*食责险报案列表*/
    @FormUrlEncoded
    @POST("/IThird/getCaseList")
    Observable<SzxTransResponse> queryLpList(@FieldMap Map<String, Object> requestBody);

    /*食责险报案详情*/
    @FormUrlEncoded
    @POST("/IThird/getCaseDetail")
    Observable<SzxTransResponse> queryLpDetail(@FieldMap Map<String, Object> requestBody);
    //Observable<SzxTransResponse> queryLpDetail(@Body BaDetail requestBody);


    @FormUrlEncoded
    @POST("/IThird/upload")
    Observable<SzxTransResponse>  uploadSingleFile(@FieldMap Map<String, Object> requestBody);

}
