package com.lifeng.f300.common.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by happen on 2017/8/7.
 */

public class MD5Utils {
    private static final String TAG = MD5Utils.class.getSimpleName();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // 这里模拟了一个为加密的字符串：加密过后为32位的字符串： fc124a3bf89db6f1c37363a704b08673
        String str = "wangkeze";
        String nowStr = md52Pass(str);
        System.out.println(nowStr);
    }

    /**
     * 使用指定Key进行MD5加密
     *
     * @param key
     * @param content
     * @return
     */
    public static String encryption(String key, String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(content.getBytes());
            byte[] bytes = md.digest(key.getBytes());
            return NumberConvertUtils.byteToHex(bytes).toLowerCase();
        } catch (Exception e) {
            Log.e(TAG, "MD5 encyption failed", e);
        }

        return null;
    }

    /**
     * 使用默认方式加密
     *
     * @param str
     * @return
     */
    public static String md52Pass(String str) {
        // 定义一个可变容器来装这个字符串
        // StringBuffer sb = new StringBuffer();
        try {
            // 得到md5的加密器对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 加密器去获得最底层的字节数组
            byte[] bs = md.digest(str.getBytes());
            return NumberConvertUtils.byteToHex(bs).toLowerCase();
            // for (byte b : bs) {
            // // 把每个字节转换为一个数字，所以&0xff
            // int num = b & 0xff; // 【加盐的方法】int num=b&0xfe; 等等或者 int
            // // num=b&0xff + 1//-2等等;
            // // 再将这个数字转化为16进制的字符
            // String singstr = Integer.toHexString(num);
            // // 转化为单个字符时，前面补0再拼接
            // if (singstr.length() == 1) {
            // sb.append("0" + singstr);
            // // 是两位的话，直接加入容器中
            // } else {
            // sb.append(singstr);
            // }
            // }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "MD5 encyption failed", e);
        }
        // 最终返回字符串就行了
        return null;
    }

}

