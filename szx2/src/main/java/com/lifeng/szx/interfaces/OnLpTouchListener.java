package com.lifeng.szx.interfaces;

import android.view.View;

import com.lifeng.f300.common.entites.LpResponse;

/**
 * Created by happen on 2017/12/6.
 */

public interface OnLpTouchListener {
    void onTouch(View v, View card, LpResponse mLpResponse);
}
