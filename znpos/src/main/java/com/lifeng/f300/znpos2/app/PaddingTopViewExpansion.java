package com.lifeng.f300.znpos2.app;

import android.support.v7.widget.Toolbar;
import android.view.View;


import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.beam.expansion.overlay.DefaultViewExpansionDelegate;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.widget.WaitingDialog;
import com.lifeng.f300.znpos2.R;

/**
 * Created by zhuchenxi on 15/9/26.
 */
public class PaddingTopViewExpansion extends DefaultViewExpansionDelegate {
    public PaddingTopViewExpansion(BeamBaseActivity activity) {
        super(activity);
    }

    private WaitingDialog mProgressDialog;

    @Override
    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            this.getActivity().setSupportActionBar(toolbar);
            this.getActivity().onSetToolbar(toolbar);
        }
    }

    @Override
    public void showProgressDialog(String title) {
        LogUtils.d("wang","waiting dialog of:"+title);
        if (mProgressDialog != null) mProgressDialog.dismiss();
        mProgressDialog = new WaitingDialog(getActivity(), R.style.CustomProgressDialog, title);
        mProgressDialog.show();
/*        mProgressDialog = new MaterialDialog.Builder(getActivity())
                .progress(true,100)
                .cancelable(false)
                .content(title)
                .show();*/
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) mProgressDialog.dismiss();
    }
}
