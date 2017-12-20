package com.lifeng.f300.znpos2.contract;


import com.lifeng.f300.znpos2.view.MultiSwipeRefreshLayout;

/**
 * Created by happen on 2016/5/31.
 */
public interface SwipeRefreshLayer {
    void requestDataRefresh();

    void setRefresh(boolean refresh);

    void setProgressViewOffset(boolean scale, int start, int end);

    void setCanChildScrollUpCallback(MultiSwipeRefreshLayout.CanChildScrollUpCallback callback);
}
