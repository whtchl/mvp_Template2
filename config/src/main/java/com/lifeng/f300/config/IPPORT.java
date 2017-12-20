package com.lifeng.f300.config;

import android.util.Log;

/**
 * Created by happen on 2017/8/16.
 */

public class IPPORT {
    String BASE_REGISTE = "http://";
    String METHOD = "/trade.htm";
    private String ipPort="";
    private static IPPORT mInstance;

    private IPPORT() {
    }

    public static IPPORT getInstance() {
        if (mInstance == null) {
            synchronized (IPPORT.class) {
                if (mInstance == null) {
                    mInstance = new IPPORT();
                }
            }
        }
        return mInstance;
    }

    public void  setIpPort(String ipport){
        ipPort= BASE_REGISTE + ipport+METHOD;
        Log.d("wang","setIpPort:"+ipPort);
    }

    public String getIpPort(){
        Log.d("wang","getIpPort:"+ipPort);
        return ipPort;
    }
}
