package com.lifeng.f300.common.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifeng.f300.common.R;
import com.lifeng.f300.common.contract.HintDialogInterface;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;

/**
 * Created by happen on 2017/8/11.
 */

public class AllDialog extends Dialog {

    public AllDialog(Context context, int theme) {
        super(context, theme);
    }

    public static Dialog dialog;   //弹出框
    private static RelativeLayout closeRl;   //叉叉
    private static Button cancelBt;   //取消按钮
    private static Button sureBt;     //确定按钮
    private static TextView titleTishi;   //提示标题
    private static TextView contentInfo;   //内容
    private static ImageView contentImage;

    public static void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog=null;
        }
    }

    /**
     * @param context 上下文
     * @param theme   样式：R.style.CustomProgressDialog
     * @param title   标题
     * @param content 提示的内容
     * @param layout  弹框的布局
     * @param dialogInterface 回调接口
     */
    public static void showDialog(Context context, int theme, String title,String content,int imageId,int layout,
                                  final OnLoginDialogInterface dialogInterface) {
        try {
            if (context != null && context instanceof HintDialogInterface) {
                HintDialogInterface activity = (HintDialogInterface) context;
                if (activity.verifyDialogFlag()) {
                    return;
                }
            }
            if (dialog != null) {
                dialog.dismiss();
                dialog=null;
            }
            dialog = new Dialog(context, theme);
            View view = LayoutInflater.from(context).inflate(layout, null);
            dialog.setContentView(view);
            titleTishi = (TextView) view.findViewById(R.id.title_tishi);
            contentInfo = (TextView) view.findViewById(R.id.content_info);
            contentImage = (ImageView) view.findViewById(R.id.content_image);
            closeRl = (RelativeLayout) view.findViewById(R.id.cancels);
            sureBt = (Button) view.findViewById(R.id.sure_bt);
            cancelBt = (Button) view.findViewById(R.id.cancel_bt);

            if(null != titleTishi){
                titleTishi.setText(title);
            }

            if(null != contentInfo){
                contentInfo.setText(content);
            }

            if (null != contentImage) {
                if (imageId!=0) {
                    contentImage.setBackgroundResource(imageId);
                }
            }

            //确定
            if(null != sureBt){
                sureBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogInterface != null) {
                            dialogInterface.onSureClick();
                        }
                    }
                });
            }

            //取消按钮
            if(null != cancelBt){
                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogInterface != null) {
                            dialogInterface.onCancelClick();
                        }
                    }
                });
            }

            //叉叉
            if(null != closeRl){
                closeRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogInterface != null) {
                            dialogInterface.onCancelClick();
                        }
                    }
                });
            }

            dialog.setCancelable(false);
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                WindowManager m = activity.getWindowManager();
                Window dialogWindow = dialog.getWindow();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.65
                dialogWindow.setAttributes(p);
                dialog.show();
            }
        } catch (Exception e) {
        }
    }
}

