package com.lifeng.beam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Mr.Jude on 2015/12/24.
 */
public class Utils {
    /**
     * 取导航栏高度
     * @return
     */
    public static int getNavigationBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean hasSoftKeys(Context ctx){
        boolean hasSoftwareKeys;
        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            Display d = manager.getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys =  (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        }else{
            boolean hasMenuKey = ViewConfiguration.get(ctx).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }
        return hasSoftwareKeys;
    }

    public static Dialog setDialog(Activity activity, int layout, double width,
                                   double height) {
        View view = LayoutInflater.from(activity).inflate(layout, null);
        Dialog dialog = new Dialog(activity, R.style.CustomProgressDialog);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        WindowManager m = activity.getWindowManager();
        Window dialogWindow = dialog.getWindow();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        if (width > 0) {
            p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.65
        }
        if (height > 0) {
            p.height = (int) (d.getHeight() * height); // 宽度设置为屏幕的0.65
        }
        dialogWindow.setAttributes(p);
        return dialog;
    }
}
