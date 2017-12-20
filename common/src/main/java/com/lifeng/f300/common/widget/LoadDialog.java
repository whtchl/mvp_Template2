package com.lifeng.f300.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lifeng.f300.common.R;

/**
 * Created by happen on 2017/9/11.
 */

public class LoadDialog extends Dialog {

    private TextView messageTextView;

    private ProgressBar dialogProgressbar;

    public LoadDialog(Context context, String message) {
        this(context, R.style.CustomProgressDialog, message);
    }

    public LoadDialog(Context context, int theme, String message) {
        super(context, theme);
        setContentView(R.layout.load_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(false);
        messageTextView = (TextView) findViewById(R.id.waiting_dialog_textview);
        dialogProgressbar = (ProgressBar) findViewById(R.id.progressBar);
        if (!TextUtils.isEmpty(message)) {
            messageTextView.setText(message);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dismiss();
            }
        }, 65000);

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
