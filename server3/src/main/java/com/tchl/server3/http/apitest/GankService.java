package com.tchl.server3.http.apitest;

import com.tchl.server3.http.apitest.bean.DailyList;
import com.tchl.server3.http.apitest.bean.GanHuoData;
import com.tchl.server3.http.apitest.bean.SzxTransResponse;
import com.tchl.server3.http.subscriber.HttpResult;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by happen on 2017/12/18.
 */

public interface GankService {
    String BASE_URL = "http://www.gank.io/api/";
    /***
     * 根据类别查询干货
     *
     * @param category
     * @param pageIndex
     * @return
     */
    @GET("data/{category}/20/{pageIndex}")
    Single<HttpResult<List<GanHuoData>>> getGanHuo(@Path("category") String category
            , @Path("pageIndex") int pageIndex);

    /**
     * 获取某天的干货
     *
     * @param date
     * @return
     */
    @GET("day/{date}")
    Single<HttpResult<DailyList>> getRecentlyGanHuo(@Path("date") String date);


}
