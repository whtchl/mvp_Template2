package com.lifeng.f300.common.contract;

import android.view.View;

import com.lifeng.f300.common.entites.TransFlowEntity;
import com.lifeng.f300.common.entites.TransResponse;

/**
 * Created by happen on 2017/9/8.
 */

public interface OnTransFlowTouchListener {
    void onTouch(View v, View card, TransResponse mTransFlowEntity);
}
