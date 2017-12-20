package szx.model;

import android.util.Log;

import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.BaDetail;
import com.lifeng.f300.common.entites.LpResponse;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.entites.picEntity;
import com.lifeng.f300.common.entites.picName;
import com.lifeng.f300.common.utils.BASE64Encoder;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.Tools;
import com.lifeng.f300.model.SignInModel;
import com.lifeng.f300.server.config.Service;
import com.lifeng.f300.server.service.DefaultTransform;
import com.lifeng.f300.server.service.DefaultTransformSzx;
import com.lifeng.f300.server.service.ServiceClient;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit.http.Field;
import retrofit.http.Query;
import rx.Observable;
import rx.functions.Action1;
import szx.config.config;
import szx.entites.SzxTransResponse;

import static com.lifeng.f300.common.utils.Tools.strCalcSHA1;

/**
 * Created by happen on 2017/11/20.
 */

public class SzxModel extends AbsModel {
    public static SzxModel getInstance() {
        return getInstance(SzxModel.class);
    }
    public Observable<SzxTransResponse>  UploadSingleFile(String file,String thirdMark,String thirdMCode){
        ServiceClient.setServiceNull();
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("thirdMark", thirdMark);
        hashMap.put("thirdMCode",thirdMCode);
        //hashMap.put("file", thirdMCode);
        JUtils.Log("==========================");

        HashMap<String,Object> hashMap2 = new HashMap<String, Object>();

        hashMap2.put("file", getImageStr(file));
        hashMap2.put("thirdMark", thirdMark);
        hashMap2.put("thirdMCode",thirdMCode);
        hashMap2.put("sign",Tools.getInstance()._strCalcSHA1(hashMap, config.key + config.sha1_bauploadsinglefile));
        return ServiceClient.getServiceSzx().uploadSingleFile(hashMap2)
                .compose(new DefaultTransformSzx<SzxTransResponse>())
                .doOnNext(new Action1<SzxTransResponse>() {
                    @Override
                    public void call(SzxTransResponse lpResponse) {
                        LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });


    }

    public Observable<SzxTransResponse>  queryLpDetail(String id,String third, String mchcode){
        ServiceClient.setServiceNull();
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("id", id);
        hashMap.put("third", third);
        hashMap.put("mchcode",mchcode);
        JUtils.Log("==========================");
        HashMap<String,Object> hashMap2 = new HashMap<String, Object>();
        hashMap2.put("id", id);
        hashMap2.put("third", third);
        hashMap2.put("mchcode",mchcode);
        hashMap2.put("sign",Tools.getInstance()._strCalcSHA1(hashMap, config.key + config.sha1_badetail));

        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap2);
        LogUtils.d("wang","queryLpDetail:"+strEntity);

        return ServiceClient.getServiceSzx().queryLpDetail(hashMap2)
                .compose(new DefaultTransformSzx<SzxTransResponse>())
                .doOnNext(new Action1<SzxTransResponse>() {
                    @Override
                    public void call(SzxTransResponse lpResponse) {
                        LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }


    public Observable<SzxTransResponse>  queryLpList(String third, String mchcode){
        ServiceClient.setServiceNull();
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("third", third);
        hashMap.put("mchcode",mchcode);
        JUtils.Log("==========================");
        HashMap<String,Object> hashMap2 = new HashMap<String, Object>();
        hashMap2.put("third", third);
        hashMap2.put("mchcode",mchcode);
        hashMap2.put("sign",Tools.getInstance()._strCalcSHA1(hashMap, config.key + config.sha1_balist));

        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap2);
        LogUtils.d("wang","queryLpList:"+strEntity);

        return ServiceClient.getServiceSzx().queryLpList(hashMap2)
                .compose(new DefaultTransformSzx<SzxTransResponse>())
                .doOnNext(new Action1<SzxTransResponse>() {
                    @Override
                    public void call(SzxTransResponse lpResponse) {
                        LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }

    public Observable<SzxTransResponse> sendSms(String mobile,String name,String money){


        ServiceClient.setServiceNull();

        HashMap<String,String> hashMap = new HashMap<String, String>();
        long time = System.currentTimeMillis();


        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date=sdf.format(new java.util.Date());

        hashMap.put("third", JUtils.getSharedPreference().getString("sp_third",""));
        hashMap.put("mchcode", JUtils.getSharedPreference().getString("sp_mchcode",""));
        hashMap.put("mchname", "");
        hashMap.put("username", "");
        hashMap.put("mobile", mobile);
        JUtils.Log(date+"==========================");


        HashMap<String,String> hashMap2 = new HashMap<String, String>();
        hashMap2.put("third", JUtils.getSharedPreference().getString("sp_third",""));
        hashMap2.put("mchcode", JUtils.getSharedPreference().getString("sp_mchcode",""));
        hashMap2.put("mchname", "");
        hashMap2.put("username", "");
        hashMap2.put("mobile", mobile);
        hashMap2.put("sign",Tools.getInstance().strCalcSHA1(hashMap, config.key + config.sha1_SMS2));

        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap2);
        LogUtils.d("wang","sendSms:"+strEntity);

        return ServiceClient.getServiceSzx().smsPolicy(hashMap2)
                .compose(new DefaultTransformSzx<SzxTransResponse>())
                .doOnNext(new Action1<SzxTransResponse>() {
                    @Override
                    public void call(SzxTransResponse szxTransResponse) {
                        LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }
    private static CountDownLatch latch = new CountDownLatch(3);

    public Observable<SzxTransResponse> SubmitBaInfo5(String third, String mchcode,String report_money,String happen_time,String address,String cause,
                                                      String informant, String informant_phone,String report_imgs){
        ServiceClient.setServiceNull();
        HashMap<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("third", third);
        hashMap.put("mchcode", mchcode);
        hashMap.put("report_money", report_money);
        hashMap.put("happen_time",happen_time);
        hashMap.put("address", address);
        hashMap.put("cause", cause);
        hashMap.put("informant", informant);
        hashMap.put("informant_phone", informant_phone);
        hashMap.put("report_imgs", report_imgs);

        JUtils.Log("==========================");



        HashMap<String,String> hashMap2 = new HashMap<String, String>();
        hashMap2.put("third", third);
        hashMap2.put("mchcode", mchcode);
        hashMap2.put("report_money", report_money);
        hashMap2.put("happen_time",happen_time);
        hashMap2.put("address", address);
        hashMap2.put("cause", cause);
        hashMap2.put("informant", informant);
        hashMap2.put("informant_phone", informant_phone);
        hashMap2.put("report_imgs", report_imgs);
        hashMap2.put("sign",Tools.getInstance().strCalcSHA1(hashMap, config.key + config.sha1_ba));
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap2);
        LogUtils.d("wang","SubmitBaInfo5:"+strEntity);

        return ServiceClient.getServiceSzx().SubmitBaInfo5(hashMap2)
                .compose(new DefaultTransformSzx<SzxTransResponse>())
                .doOnNext(new Action1<SzxTransResponse>() {
                    @Override
                    public void call(SzxTransResponse szxTransResponse) {
                        LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }

    public Observable<SzxTransResponse> SubmitBaInfo(String third, String mchcode,String report_money,String happen_time,String address,String cause,
                                                     String informant, String informant_phone,
                                                    final String scene,String paid,String shakes){
        String base64 = "data:image/png;base64,";
        Gson gson = new Gson();

        List<String> picnames = new ArrayList<String>();
        picnames.add("scene");
        picnames.add("paid");
        picnames.add("shakes");
        String str_picnames = gson.toJson(picnames);
        ServiceClient.setServiceNull();

       /* String strBase64_scene = getImageStr(scene);
        String strBase64_paid = getImageStr(paid);
        String strBase64_shakes = getImageStr(shakes);*/

        //多线程处理 begin
        ExecutorService exec = Executors.newFixedThreadPool(3);
        List<Callable<String>> tasks = new ArrayList<Callable<String>>();
        Callable<String> task = null;
        for (int i = 0; i < 3; i++)
        {
            task = new Callable<String>()
            {
                @Override
                public String call() throws Exception
                {
                    String  ran = getImageStr(scene);
                    //Thread.sleep(ran);
                    System.out.println(Thread.currentThread().getName()+" 休息了 scene"  );
                    return ran;
                }
            };

            tasks.add(task);
        }
        long s = System.currentTimeMillis();

        List<Future<String>> results = null;
        try {
            results = exec.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("执行任务消耗了 ：" + (System.currentTimeMillis() - s) +"毫秒");

        /*for (int i = 0; i < results.size(); i++)
        {
            try
            {
                System.out.println(results.get(i).get());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }*/
/*
        String strBase64_scene = getImageStr(scene);
        String strBase64_paid = getImageStr(paid);
        String strBase64_shakes = getImageStr(shakes);*/
        List<String> ListBase64 = new ArrayList<String>();
        try {
            ListBase64.add(results.get(0).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            ListBase64.add(results.get(1).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            ListBase64.add(results.get(2).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        exec.shutdown();

        //多线程处理 end
        /*Log.i("wang","strBase64_scene"+strBase64_scene);
        Log.i("wang","strBase64_paid"+strBase64_paid);
        Log.i("wang","strBase64_shakes"+strBase64_shakes);*/
       /* List<String> ListBase64 = new ArrayList<String>();
        ListBase64.add(base64+strBase64_scene);
        ListBase64.add(base64+strBase64_paid);
        ListBase64.add(base64+strBase64_shakes);*/
        /*String strListBase64 = gson.toJson(ListBase64);*/

 //file:/storage/emulated/0/Pictures/JPEG_20171205_191458.jpg
        String  name_scene = getImageName(scene);
        String  name_paid = getImageName(paid);
        String name_shakes = getImageName(shakes);
        picName mpicName = new picName(name_scene,name_paid,name_shakes);
        List<picName> listName =new ArrayList<picName>();
        listName.add(mpicName);

        Log.i("wang","name_scene:"+name_scene);

        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        long time = System.currentTimeMillis();

       /* List<String> picbase64 = new ArrayList<String>();
        picbase64.add(base64+strBase64_scene);
        picbase64.add("2");
        picbase64.add("3");
        String str_picbase64 = gson.toJson(picbase64);*/



        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date=sdf.format(new java.util.Date());

        hashMap.put("third", third);
        hashMap.put("mchcode", mchcode);
        hashMap.put("report_money", report_money);
        hashMap.put("happen_time",happen_time);
        hashMap.put("address", address);
        hashMap.put("cause", cause);
        hashMap.put("informant", informant);
        hashMap.put("informant_phone", informant_phone);
        //hashMap.put("picName", str_picnames );
        //hashMap.put("picBase64",picbase64);
        JUtils.Log(date+"==========================");

        HashMap<String,Object> hashMap2 = new HashMap<String, Object>();
        //long time = System.currentTimeMillis();
        hashMap2.put("third", JUtils.getSharedPreference().getString("sp_third",""));
        hashMap2.put("mchcode", JUtils.getSharedPreference().getString("sp_mchcode",""));
        hashMap2.put("report_money", report_money);
        hashMap2.put("happen_time",happen_time);
        hashMap2.put("address", address);
        hashMap2.put("cause", cause);
        hashMap2.put("informant", informant);
        hashMap2.put("informant_phone", informant_phone);


       // hashMap2.put("picBase64","" base64+strBase64_scene+,base64+strBase64_paid,base64+strBase64_shakes");
       /* List<String> list3=new ArrayList<String>();
        list3.add(name_scene);
        list3.add(name_scene);
        list3.add(name_scene);*/

        hashMap2.put("picName[]", picnames );


        /*picbase64.add(base64+strBase64_scene);
        picbase64.add(base64+strBase64_paid);*/

       /* picbase64.add(base64+strBase64_shakes);*/
  /*      Log.i("wang","sence:"+picbase64.get(0));
        Log.i("wang","strBase64_paid:"+picbase64.get(1));
        Log.i("wang","strBase64_shakes:"+picbase64.get(2));*/
        //hashMap2.put("picBase64",picbase64);

        hashMap2.put("sign",Tools.getInstance()._strCalcSHA1(hashMap, config.key + config.sha1_ba));

        String strEntity = gson.toJson(hashMap2);
        LogUtils.d("wang","SubmitBaInfo:"+strEntity);


        LogUtils.d("wang","str_picnames:"+str_picnames);

        return ServiceClient.getServiceSzx().SubmitBaInfo2(address,happen_time,informant,informant_phone,report_money,picnames,
                                                          cause,mchcode,third,(String)hashMap2.get("sign"),ListBase64)
                .compose(new DefaultTransformSzx<SzxTransResponse>())
                .doOnNext(new Action1<SzxTransResponse>() {
                    @Override
                    public void call(SzxTransResponse szxTransResponse) {
                        LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }




    public Observable<SzxTransResponse> SubmitBaInfo2(String third, String mchcode,String report_money,String happen_time,String address,String cause,
                                                     String informant, String informant_phone,
                                                     String scene,String paid,String shakes){
        Gson gson = new Gson();

        List<String> picnames = new ArrayList<String>();
        picnames.add("scene");
        picnames.add("paid");
        picnames.add("shakes");
        String str_picnames = gson.toJson(picnames);
        ServiceClient.setServiceNull();

        String strBase64_scene = getImageStr(scene);
        String strBase64_paid = getImageStr(paid);
        String strBase64_shakes = getImageStr(shakes);
        Log.i("wang","strBase64_scene"+strBase64_scene);
        Log.i("wang","strBase64_paid"+strBase64_paid);
        Log.i("wang","strBase64_shakes"+strBase64_shakes);

        String base64 = "data:image/png;base64,";
        HashMap<String,String> hashMap3 = new HashMap<String, String>();
        hashMap3.put("strBase64_scene",base64+strBase64_scene);
        hashMap3.put("strBase64_paid",base64+strBase64_paid);
        hashMap3.put("strBase64_shakes",base64+strBase64_shakes);
        if(hashMap3.get("strBase64_scene").equals(base64+strBase64_scene))     {
            LogUtils.d("wang","strBase64_scene is same");
        }else{
            LogUtils.d("wang","strBase64_scene is not same");
        }

        if(hashMap3.get("strBase64_paid").equals(base64+strBase64_paid))     {
            LogUtils.d("wang","strBase64_paid is same");
        }else{
            LogUtils.d("wang","strBase64_paid is not same");
        }

        if(hashMap3.get("strBase64_shakes").equals(base64+strBase64_shakes))     {
            LogUtils.d("wang","strBase64_shakes is same");
        }else{
            LogUtils.d("wang","strBase64_shakes is not same");
        }

        //file:/storage/emulated/0/Pictures/JPEG_20171205_191458.jpg
        String  name_scene = getImageName(scene);
        String  name_paid = getImageName(paid);
        String name_shakes = getImageName(shakes);
        picName mpicName = new picName(name_scene,name_paid,name_shakes);
        List<picName> listName =new ArrayList<picName>();
        listName.add(mpicName);

        Log.i("wang","name_scene:"+name_scene);

        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        long time = System.currentTimeMillis();

        ArrayList<String> picbase64 = new ArrayList<String>();
        picbase64.add(base64+strBase64_scene);
        picbase64.add("2");
        picbase64.add("3");
        /*picbase64.add(base64+strBase64_scene);
        picbase64.add(base64+strBase64_paid);
        picbase64.add(base64+strBase64_shakes);*/


        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date=sdf.format(new java.util.Date());

        hashMap.put("third", third);
        hashMap.put("mchcode", mchcode);
        hashMap.put("report_money", report_money);
        hashMap.put("happen_time",happen_time);
        hashMap.put("address", address);
        hashMap.put("cause", cause);
        hashMap.put("informant", informant);
        hashMap.put("informant_phone", informant_phone);
        //hashMap.put("picName", picnames );
        //hashMap.put("picBase64",picbase64);
        JUtils.Log(date+"==========================");

        HashMap<String,Object> hashMap2 = new HashMap<String, Object>();
        //long time = System.currentTimeMillis();
        hashMap2.put("third", JUtils.getSharedPreference().getString("sp_third",""));
        hashMap2.put("mchcode", JUtils.getSharedPreference().getString("sp_mchcode",""));
        hashMap2.put("report_money", report_money);
        hashMap2.put("happen_time",happen_time);
        hashMap2.put("address", address);
        hashMap2.put("cause", cause);
        hashMap2.put("informant", informant);
        hashMap2.put("informant_phone", informant_phone);
        hashMap2.put("picName", picnames );
        Log.i("wang","sence:"+picbase64.get(0));
        Log.i("wang","strBase64_paid:"+picbase64.get(1));
        Log.i("wang","strBase64_shakes:"+picbase64.get(2));
        //hashMap2.put("picBase64",picbase64);

        hashMap2.put("sign",Tools.getInstance()._strCalcSHA1(hashMap, config.key + config.sha1_ba));

        String strEntity = gson.toJson(hashMap2);
        LogUtils.d("wang","SubmitBaInfo:"+strEntity);


        return ServiceClient.getServiceSzx().SubmitBaInfo3(hashMap2)
                .compose(new DefaultTransformSzx<SzxTransResponse>())
                .doOnNext(new Action1<SzxTransResponse>() {
                    @Override
                    public void call(SzxTransResponse szxTransResponse) {
                        LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });

    }

/*    public Observable<SzxTransResponse> SubmitIMGANDINFO(String third, String mchcode,String report_money,String happen_time,String address,String cause,
                                                      String informant, String informant_phone,
                                                      String scene,String paid,String shakes) {

    }*/

    private String getImageName(String shakes) {
        return shakes.substring(shakes.indexOf("J"),shakes.length());
    }

    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return "data:image/png;base64,"+encoder.encode(data);
    }

}

/**
 *   下面这个是食责险get提交内容
 *    @GET("/smsPolicy")
 *     Observable<SzxTransResponse>smsPolicy(@Query("mchcode") String mchcode,
 *     @Query("mobile") String mobile,
 *     @Query("third") String third,
 *     @Query("paytime") String paytime,
 *     @Query("mchname") String mchname,
 *     @Query("username") String username,
 *     @Query("money") String money,
 *     @Query("sign") String sign
 *     );
 *
 *
 *
 *
 *
 *
 *
   *   public Observable<SzxTransResponse> sendSms(String mobile){
   *
   *
   *   ServiceClient.setServiceNull();
   *
   *   HashMap<String,String> hashMap = new HashMap<String, String>();
   *   long time = System.currentTimeMillis();
   *   hashMap.put("third", JUtils.getSharedPreference().getString("sp_third",""));
   *   hashMap.put("mchcode", JUtils.getSharedPreference().getString("sp_mchcode",""));
   *   hashMap.put("mchname", null);
   *   hashMap.put("username", "");
   *   hashMap.put("mobile", mobile);
   *   hashMap.put("money", "");
   *   hashMap.put("paytime", time+"");
   *
   *
   *
   *   HashMap<String,String> hashMap2 = new HashMap<String, String>();
   *   hashMap2.put("third", JUtils.getSharedPreference().getString("sp_third",""));
   *   hashMap2.put("mchcode", JUtils.getSharedPreference().getString("sp_mchcode",""));
   *   hashMap2.put("mchname", null);
   *   hashMap2.put("username", "");
   *   hashMap2.put("mobile", mobile);
   *   hashMap2.put("money", "");
   *   hashMap2.put("paytime",hashMap.get("paytime"));
   *   hashMap2.put("sign",Tools.getInstance().strCalcSHA1(hashMap, config.key + config.sha1_SMS));
   *
   *   Gson gson = new Gson();
   *   String strEntity = gson.toJson(hashMap2);
   *   LogUtils.d("wang","sendSms:"+strEntity);
   *
   *   return ServiceClient.getServiceSzx().smsPolicy(hashMap2.get("mchcode"),
   *   hashMap2.get("mobile"),
   *   hashMap2.get("third"),
   *   hashMap2.get("paytime"),
   *   hashMap2.get("mchname"),
   *   hashMap2.get("username"),
   *   hashMap2.get("money"),
   *   hashMap2.get("sign"))
   *   .compose(new DefaultTransformSzx<SzxTransResponse>())
   *   .doOnNext(new Action1<SzxTransResponse>() {
  *    @Override
  *    public void call(SzxTransResponse szxTransResponse) {
  *    LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
  *    " \n Thread id:" + Thread.currentThread().getId());
  *    }
  *   });
  * }
 *
 *
 */
