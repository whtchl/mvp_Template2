package com.lifeng.f300.config;

/**
 * Created by happen on 2017/10/19.
 */

public class Permission  {

    private static Permission instance;

    /**
     * 获取权限实列对象
     * @return
     */
    public static Permission getInstance() {
        instance = new Permission();
        return instance;
    }

    //test
    public boolean testParam(){
        return true;
    }

    /**
     * 控制上电下电
     */
    public boolean controlPowOnOff(){
        return false;
    }

    /**
     * 是否写固定秘钥
     */
    public boolean isLoadSameKey(){ return true; }

    /**
     * 是否弹出ReadCardActivity
     */
    public boolean isPopupReadCardActivity() {  return true; }
}
