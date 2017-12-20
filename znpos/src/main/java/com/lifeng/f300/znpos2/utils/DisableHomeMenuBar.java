package com.lifeng.f300.znpos2.utils;

/**
 * Created by happen on 2017/9/5.
 */

public class DisableHomeMenuBar {

    private static String action1="android.intent.action.DISABLE_KEYCODE";//home键，menu键
    private static String action2="android.intent.action.DISABLE_STATUSBAR";//状态栏
    /**
     * 状态
     * isStop 1 禁用
     * isStop 0 启用
     * 屏蔽所有的设备的menu，home，bar
     */
    public static void operatorAll(int isStop){
        operatorHome(isStop);
        operatorMenu(isStop);
        operatorBar(isStop);
    }

    /**
     * 屏蔽或者打开home
     */
    /**
     * 开始禁止所有或者开启所有
     * @param action  意图 android.intent.action.DISABLE_KEYCODE（按键） android.intent.action.DISABLE_STATUSBAR(下拉)
     * @param keycode 标识  KeyEvent.KEYCODE_HOME,KeyEvent.KEYCODE_MENU,KeyEvent.KEYCODE_BACK
     * @param state 状态 1,禁用；0 启用
     */
    private static void operatorHome(int isStop){
        /*if (BuildModle.isShengBen()) {
            Intent intentHome = new Intent();
            intentHome.setAction(action1);
            intentHome.putExtra("keycode", KeyEvent.KEYCODE_HOME);
            if (isStop == 1) {
                intentHome.putExtra("state", 1);
            }else{
                intentHome.putExtra("state", 0);
            }
            PayApplication.getInstance().sendBroadcast(intentHome);
        }*/
    }

    /**
     * 屏蔽或者打开Menu
     */
    private static void operatorMenu(int isStop){
        /*if (BuildModle.isShengBen()) {
            Intent intentMenu = new Intent();
            intentMenu.setAction(action1);
            intentMenu.putExtra("keycode", KeyEvent.KEYCODE_MENU);
            if (isStop == 1) {
                intentMenu.putExtra("state", 1);
            }else{
                intentMenu.putExtra("state", 0);
            }
            PayApplication.getInstance().sendBroadcast(intentMenu);
        }*/
    }

    /**
     * 屏蔽或者打开Bar
     */
    private static void operatorBar(int isStop){
        /*if (BuildModle.isShengBen()) {
            Intent intentBar = new Intent();
            intentBar.setAction(action2);
            if (isStop == 1) {
                intentBar.putExtra("state", 1);
            }else{
                intentBar.putExtra("state", 0);
            }
            PayApplication.getInstance().sendBroadcast(intentBar);
        }*/
    }
}

