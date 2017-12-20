package com.lifeng.f300.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lifeng.f300.common.R;

/**
 * Created by happen on 2017/8/4.
 */

public class ToastMakeUtils {
    public static void showToast(final Activity activity,final String info){

        //判断在主线程中还是在子线程中
        if ("main".equals(Thread.currentThread().getName())) {
            showToastSingle(activity,info);
        } else {
            activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    showToastSingle(activity,info);
                }
            });
        }
    }

    protected static Toast toast   = null;

    private static int time = 3000;

    private static void showToastSingle(Activity activity, String s){
        Context context = activity;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastRoot = inflater.inflate(R.layout.toast, null);
        TextView tvMessage = (TextView) toastRoot.findViewById(R.id.message);
        tvMessage.setText(s);
        if(toast==null){
            toast = new Toast(context);
//        	toast.setGravity(Gravity.CENTER, 0,20);
            toast.setDuration(Toast.LENGTH_LONG);
        }else{
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.setView(toastRoot);
        toast.show();
    }
    public static void cancel(){
        if(toast!=null){
            toast.cancel();
            toast = null;
        }
    }
}
