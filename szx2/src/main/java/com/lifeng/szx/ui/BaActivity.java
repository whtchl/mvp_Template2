package com.lifeng.szx.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.datetime.DateDialog;
import com.lifeng.f300.common.utils.BASE64Encoder;
import com.lifeng.f300.common.utils.DeleteDirectory;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.common.utils.ToastMakeUtils;
import com.lifeng.szx.MainActivity;
import com.lifeng.szx.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import me.iwf.photopicker.PhotoPicker;
import szx.entites.SzxTransResponse;


@RequiresPresenter(BaPresenter.class)
public class BaActivity extends BeamBaseActivity<BaPresenter> {
    private final static int MSG_FILE1 = 11;
    private final static int MSG_FILE2 = 12;
    private final static int MSG_FILE3= 13;
    private final static int MSG_BAINFO= 14;

    public String filepath1,filepath3,filepath2;
    Toolbar toolbarBa;

    EditText et_spje, et_aqms, et_barxm, et_barsj, et_yzm,et_badd;
    TextView tv_cxsj;
    Button next_button;
    ImageView im_baxc,im_sjpcd,im_sfhyz;

    final int TAKE_BAXC = 1;

    int pic = 0;

    public String file_baxc="",file_sjpcd="",file_sfhyz="";

    public String file_baxc_server="",file_sjpcd_server="",file_sfhyz_server="";

    static public String filename_baxc = "filename_baxc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ba);
        initView();
        initEvent();
    }



    private void initView() {
        toolbarBa = (Toolbar) this.findViewById(R.id.toolbar_ba);
        setSupportActionBar(toolbarBa);
        toolbarBa.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_spje = (EditText) this.findViewById(R.id.et_spje);
        tv_cxsj = (TextView) this.findViewById(R.id.tv_cxsj);
        et_aqms = (EditText) this.findViewById(R.id.et_aqms);
        et_barxm = (EditText) this.findViewById(R.id.et_barxm);
        et_barsj = (EditText) this.findViewById(R.id.et_barsj);
        et_yzm = (EditText) this.findViewById(R.id.et_yzm);

        et_badd= (EditText) this.findViewById(R.id.et_badd);
        im_baxc = (ImageView) this.findViewById(R.id.iv_baxc);
        im_sfhyz = (ImageView) this.findViewById(R.id.iv_sfhyz);
        im_sjpcd = (ImageView) this.findViewById(R.id.iv_sjpcd);
        next_button = (Button) this.findViewById(R.id.next_button);

    }

    private void initEvent() {
        tv_cxsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog=   new DateDialog(BaActivity.this,getString(R.string.gettime), DateDialog.MODE_1, new DateDialog.InterfaceDateDialog() {
                    @Override
                    public void getTime(String dateTime) {
                        //Toast.makeText(BaActivity.this,dateTime, Toast.LENGTH_LONG).show();
                        tv_cxsj.setText(dateTime);
                    }
                } );
                dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateDialog.show();
            }
        });

        im_baxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setOpenCamera(true)
                        .start(BaActivity.this);
                pic = 0;
            }
        });

        im_sfhyz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setOpenCamera(true)
                        .start(BaActivity.this);
                pic = 1;
            }
        });

        im_sjpcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setOpenCamera(true)
                        .start(BaActivity.this);
                pic = 2;
            }
        });


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadInfo();
            }
        });

    }

    private void uploadInfo() {
        if(TextUtils.isEmpty(et_spje.getText().toString().trim())){
            JUtils.ToastLong("请输入金额");
            return;
        }
        if(TextUtils.isEmpty(tv_cxsj.getText().toString().trim())){
            JUtils.ToastLong("请选择出险时间");
            return;
        }
        if(TextUtils.isEmpty(et_badd.getText().toString().trim())){
            JUtils.ToastLong("请填写报案地点");
            return;
        }


        if(TextUtils.isEmpty(et_aqms.getText().toString().trim())){
            JUtils.ToastLong("请输入案情描述");
            return;
        }
        if(TextUtils.isEmpty(et_barxm.getText().toString().trim())){
            JUtils.ToastLong("请输入报案人姓名");
            return;
        }

        if (TextUtils.isEmpty(et_barsj.getText().toString().trim())) {
            JUtils.ToastLong(getString(R.string.input_phone));
            return;
        }

        if (!StringUtil.isMobileNo(et_barsj.getText().toString().trim())) {
            JUtils.ToastLong(getString(R.string.phone_error));
            return;
        }

        if(TextUtils.isEmpty(file_baxc) || TextUtils.isEmpty(file_sfhyz) || TextUtils.isEmpty(file_sjpcd) ){
            JUtils.ToastLong("请拍照");
            return;
        }

        if(TextUtils.isEmpty(JUtils.getSharedPreference().getString("sp_third",""))
                || TextUtils.isEmpty(JUtils.getSharedPreference().getString("sp_mchcode",""))){
            JUtils.ToastLong("请设置支付公司标识或商户号");
            return;
        }
        Log.i("wang",JUtils.getSharedPreference().getString("sp_third",""));

        Log.i("wang",JUtils.getSharedPreference().getString("sp_mchcode",""));


            //api/ICommon/upload file=base64 thirdMark='reportCase' thirdMCode=time()

        getPresenter().pUploadSingleFile(file_baxc,"reportCase",System.currentTimeMillis()+"",2);


        /*getPresenter().pSubmitBaInfo(JUtils.getSharedPreference().getString("sp_third",""),
                 JUtils.getSharedPreference().getString("sp_mchcode",""),

                et_spje.getText().toString().trim(),
                tv_cxsj.getText().toString().trim(),
                et_badd.getText().toString().trim(),
                et_aqms.getText().toString().trim(),
                et_barxm.getText().toString().trim(),
                et_barsj.getText().toString().trim(),
                file_baxc,
                file_sjpcd,
                file_sfhyz);
*/

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
        return "data:image/png;base64," +encoder.encode(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  //选择返回
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            iv_crop.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }*/
        //拍照功能或者裁剪功能返回
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.CROP_CODE) {
            if(pic == 0) {
                im_baxc.setBackgroundResource(0);
                Log.i("wang", "file:" + data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH));
                file_baxc = data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH);
                Glide.with(getApplicationContext()).load(Uri.fromFile(new File(data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH)))).into(im_baxc);
            }else if(pic == 1){
                im_sfhyz.setBackgroundResource(0);
                Log.i("wang", "file:" + data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH));
                file_sfhyz = data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH);
                Glide.with(getApplicationContext()).load(Uri.fromFile(new File(data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH)))).into(im_sfhyz);
            }else if(pic == 2 ){
                im_sjpcd.setBackgroundResource(0);
                Log.i("wang", "file:" + data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH));
                file_sjpcd = data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH);
                Glide.with(getApplicationContext()).load(Uri.fromFile(new File(data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH)))).into(im_sjpcd);
            }
        }
    }

    public void gotoMainActivity(){
        boolean success =  DeleteDirectory.deleteDir(new File(getPicPath(file_baxc)));
        if(success){
            LogUtils.d("wang","delete success");}

       /* boolean success = deleteDir(new File(newDir2));*/
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private String  getPicPath(String str){
        String path = str.substring(0,str.indexOf("J")-1);
         Log.i("wang","path:"+str);
        return path;
    }

    public void  gotoSendSecondPIC(SzxTransResponse szxTransResponse){
//        file_baxc_server="",file_sjpcd_server="",file_sfhyz_server="";
        file_baxc_server = szxTransResponse.data.get(0).path;
        LogUtils.d("wang","file_baxc_server:"+file_baxc_server);
        Message msg = Message.obtain();
        msg.what = MSG_FILE2;
        handler.sendMessage(msg);
    }


    public void  gotoSendThirdPIC(SzxTransResponse szxTransResponse){
        file_sjpcd_server = szxTransResponse.data.get(0).path;
        LogUtils.d("wang","file_sjpcd_server:"+file_sjpcd_server);
        Message msg = Message.obtain();
        msg.what = MSG_FILE3;
        handler.sendMessage(msg);
    }
    public void  gotoUploadBaInfo(SzxTransResponse szxTransResponse){
        file_sfhyz_server = szxTransResponse.data.get(0).path;
        LogUtils.d("wang","file_sfhyz_server:"+file_sfhyz_server);
        Message msg = Message.obtain();
        msg.what = MSG_BAINFO;
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FILE2:
                    getPresenter().pUploadSingleFile(file_sjpcd,"reportCase",System.currentTimeMillis()+"",3);
                    break;
                case MSG_FILE3:
                    getPresenter().pUploadSingleFile(file_sfhyz,"reportCase",System.currentTimeMillis()+"",0);
                    break;
                case MSG_BAINFO:
                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("scene",file_baxc_server);
                    hashMap.put("paid",file_sjpcd_server);
                    hashMap.put("shake",file_sfhyz_server);
                    Gson gson = new Gson();
                    String str = gson.toJson(hashMap);
                    Log.i("wang",str);
                    getPresenter().pSubmitBaInfo(JUtils.getSharedPreference().getString("sp_third",""),
                            JUtils.getSharedPreference().getString("sp_mchcode",""),

                            et_spje.getText().toString().trim(),
                            tv_cxsj.getText().toString().trim(),
                            et_badd.getText().toString().trim(),
                            et_aqms.getText().toString().trim(),
                            et_barxm.getText().toString().trim(),
                            et_barsj.getText().toString().trim(),
                            str);
                    break;
                default:
                    break;
            }
        }
    };
}
