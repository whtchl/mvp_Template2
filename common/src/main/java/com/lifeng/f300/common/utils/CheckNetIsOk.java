package com.lifeng.f300.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;

import com.lifeng.f300.common.R;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.common.widget.AllDialog;

/**
 * 检查网络
 * Created by happen on 2017/8/11.
 */

public class CheckNetIsOk {
    public static boolean isNetOk(final Context context){
        if (!networkIsAvailable(context)) {
            AllDialog.showDialog(context, R.style.CustomProgressDialog,context.getString(R.string.kind_remind),
                    context.getString(R.string.network_err),R.drawable.error_net, R.layout.network_error_vip, new OnLoginDialogInterface(){
                @Override
                public void onConfirmClick(EditText etUserName, EditText etOldPassword, EditText etPassword, EditText etPasswordSure) {
                }

                @Override
                public void onCancelClick() {
                    AllDialog.closeDialog();
                }

                @Override
                public void onSureClick() {
                    AllDialog.closeDialog();
                    context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
            return false;
        }else{
            return true;
        }
    }


    // 判断网络连接
    public static boolean networkIsAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //新版本调用方法获取网络状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            //否则调用旧版本方法
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
