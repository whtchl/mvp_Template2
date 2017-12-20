package com.lifeng.f300.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lifeng.f300.common.R;



/**
 * Created by happen on 2017/8/7.
 */

public class WaitingDialog extends Dialog {
    private TextView messageTextView;

    private ProgressBar dialogProgressbar;

    private LinearLayout llPrint;

    public WaitingDialog(Context context, String message) {
        this(context, R.style.CustomProgressDialog, message);
    }

    public WaitingDialog(Context context, int theme, String message) {
        super(context, theme);
        setContentView(R.layout.waiting_dialog_layout);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(false);
        messageTextView = (TextView) findViewById(R.id.waiting_dialog_textview);
        dialogProgressbar = (ProgressBar) findViewById(R.id.waiting_dialog_progressbar);
        if (!TextUtils.isEmpty(message)) {
            messageTextView.setText(message);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dismiss();
                if(llPrint != null){
                    llPrint.setEnabled(true);
                }
            }
        }, 30000);

    }

    /**
     * setMessage
     *
     * @param message
     */
    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            messageTextView.setText(message);
        }
    }

    public void setLlPrint(LinearLayout llPrint) {
        this.llPrint = llPrint;
    }
    /**
     * setMessage
     *
     * @param isShow
     */
    public void  showProgressBar(boolean isShow) {
        if (!isShow) {
            dialogProgressbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }
    }
}

