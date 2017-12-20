package com.lifeng.f300.common.utils;

import android.util.Log;

import com.lifeng.f300.common.entites.picEntity;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by happen on 2017/8/7.
 */

public class Tools {
    private static Tools mInstance;

    private Tools() {
    }

    public static Tools getInstance() {
        if (mInstance == null) {
            synchronized (Tools.class) {
                if (mInstance == null) {
                    mInstance = new Tools();
                }
            }
        }
        return mInstance;
    }

    /**
     * 统一的加密方式
     * @param map  map集合
     * @param key  最后追加的字符串
     * @return
     */
    public  boolean calcMD5(Map<String, String> map, String key){
        String MD5_NAME = "MD5";
        Set<String> key_set = map.keySet();
        TreeSet<String> tree_set = new TreeSet<String>(new Comparator<String>(){
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        tree_set.addAll(key_set);
        StringBuffer sb = new StringBuffer("");
        for(String s : tree_set){
            if(!MD5_NAME.equals(s.trim())){
                Object val = map.get(s);
                String v = val == null ? "" : val.toString().trim();
                sb.append(v);
            }
        }
        sb.append(key);
        String md5_str = sb.toString();
        LogUtils.d("wang", "MD5之前:"+md5_str);
        String md5 = MD5Utils.md52Pass(md5_str);
        map.put(MD5_NAME, md5);
        return true;
    }

    public static String strCalcSHA1(Map<String, String> map, String key) {
        String MD5_NAME = "MD5";
        // 开始排序
        Set<String> key_set = map.keySet();
        TreeSet<String> tree_set = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        tree_set.addAll(key_set);
        StringBuffer sb = new StringBuffer("");
        for (String s : tree_set) {
            if (!MD5_NAME.equals(s.trim())) {
                Object val = map.get(s);
                String v = val == null ? "" : val.toString().trim();
                LogUtils.d("wang","key:"+s+"  value:"+v);

                sb.append(v);
            }
        }
        sb.append(key);
        String sha1_str = sb.toString();
        LogUtils.d("wang", "SHA1之前" + sha1_str);
		/*String md5_str = sb.toString();
		LogUtils.d("wang", "MD5之前"+md5_str);*/
        return EncryptUtils.encryptToSHA(sb.toString());

    }


    public static String _strCalcSHA1(Map<String, Object> map, String key) {
        String MD5_NAME = "MD5";
        // 开始排序
        Set<String> key_set = map.keySet();
        TreeSet<String> tree_set = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        tree_set.addAll(key_set);
        StringBuffer sb = new StringBuffer("");
        for (String s : tree_set) {
            if (!MD5_NAME.equals(s.trim())) {
                Object val = map.get(s);
                String v = val == null ? "" : val.toString().trim();
                LogUtils.d("wang","key:"+s+"  value:"+v);

                sb.append(v);
            }
        }
        sb.append(key);
        String sha1_str = sb.toString();
        LogUtils.d("wang", "SHA1之前" + sha1_str);
		/*String md5_str = sb.toString();
		LogUtils.d("wang", "MD5之前"+md5_str);*/
        return EncryptUtils.encryptToSHA(sb.toString());

    }

}
