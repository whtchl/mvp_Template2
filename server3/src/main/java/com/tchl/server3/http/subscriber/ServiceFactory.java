package com.tchl.server3.http.subscriber;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jude.utils.JUtils;

import java.lang.reflect.Field;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private final Gson mGson;
    private OkHttpClient mOkHttpClient;


    private ServiceFactory() {
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        mOkHttpClient = OkHttpProvider.getDefaultOkHttpClient();

    }

/*    private static class SingletonHolder {
        private static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static ServiceFactory getNoCacheInstance() {
        ServiceFactory factory = SingletonHolder.INSTANCE;
        factory.mOkHttpClient = OkHttpProvider.getOkHttpClient();
        return factory;
    }*/

    public static ServiceFactory getNoCacheInstance() {
        ServiceFactory factory  = new ServiceFactory();
        factory.mOkHttpClient = OkHttpProvider.getOkHttpClient();
        return factory;
    }

    private static ServiceFactory mInstance;
   // private AudioManager(){}
    public static ServiceFactory getInstance(){
        if(mInstance == null){
            synchronized (ServiceFactory.class){
                if(mInstance == null){
                    mInstance = new ServiceFactory();
                }
            }
        }
        return mInstance;
    }


    public <S> S createService(Class<S> serviceClass) {
        String baseUrl = "";
        try {
            Field field1 = serviceClass.getField("BASE_URL");   //反射机制
            baseUrl = (String) field1.get(serviceClass);
            JUtils.Log("baseUrl:"+baseUrl);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.getMessage();
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }


}
