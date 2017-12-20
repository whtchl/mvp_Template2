package com.lifeng.f300.znpos2.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifeng.f300.common.contract.HintDialogInterface;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.znpos2.R;

/**
 * Created by happen on 2017/8/28.
 */

/*public class ModifyPwdDialog {
}*/
public class OperatorDialog extends Dialog {

    public OperatorDialog(Context context, int theme) {
        super(context, theme);
    }

    public static boolean isShow = true;
    private static Dialog dialog;
    private static EditText etPassword;
    private static EditText etUserName;
    private static ImageView icon;
    private static RelativeLayout closeIv;
    private static EditText etOldPassword;
    private static View line;
    private static EditText etPasswordSure;

    public static void closeDialog(){
        if (dialog!=null) {
            dialog.dismiss();
        }
    }

    /**
     * @param context
     * @param iconId  图片资源
     * @param title   标题
     * @param buttonInfo  按钮文字
     * @param dialogInterface  回调接口
     */
    public static void showDialog(boolean isGone,Context context,int iconId,String title,String buttonInfo,String userName,final OnLoginDialogInterface dialogInterface) {
        try {
            if (context != null && context instanceof HintDialogInterface) {
                HintDialogInterface activity = (HintDialogInterface) context;
                if (activity.verifyDialogFlag()) {
                    return;
                }
            }
            dialog = new Dialog(context, R.style.MoreTextDialogVip);
            View view = LayoutInflater.from(context).inflate(R.layout.operator_login, null);
            dialog.setContentView(view);
            Button btnSure = (Button) view.findViewById(R.id.btlogin);
            TextView titleView = (TextView) view.findViewById(R.id.title);
            titleView.setText(title);
            etUserName = (EditText) view.findViewById(R.id.userName);
            etUserName.setText(TextUtils.isEmpty(userName) ? "" : userName);
            if (!TextUtils.isEmpty(userName)) {
                etUserName.setEnabled(false);
            }
            etPassword = (EditText) view.findViewById(R.id.password);
            etPasswordSure = (EditText) view.findViewById(R.id.password_sure);
            etOldPassword = (EditText) view.findViewById(R.id.old_password);
            line = view.findViewById(R.id.line);
            icon = (ImageView) view.findViewById(R.id.login_icon);
            closeIv = (RelativeLayout) view.findViewById(R.id.close_iv);
            icon.setImageResource(iconId);
            btnSure.setText(buttonInfo);
            if (isGone) {
                etOldPassword.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            }
            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialogInterface != null) {
                        dialogInterface.onConfirmClick(etUserName,etOldPassword,etPassword,etPasswordSure);
                    }
                }
            });

            closeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog();
                }
            });

            dialog.setCancelable(true);
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                WindowManager m = activity.getWindowManager();
                Window dialogWindow = dialog.getWindow();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.65
                dialogWindow.setAttributes(p);
                dialog.show();
                isShow = false;
            }
        } catch (Exception e)
        {
        }
    }
}
