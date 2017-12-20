package com.lifeng.szx.view;

/**
 * Created by happen on 2017/12/6.
 */

public interface SwipeRefreshLayer {
    void requestDataRefresh();

    void setRefresh(boolean refresh);

    void setProgressViewOffset(boolean scale, int start, int end);

    void setCanChildScrollUpCallback(MultiSwipeRefreshLayout.CanChildScrollUpCallback callback);
}
