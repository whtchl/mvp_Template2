package com.lifeng.beam.expansion.list;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.lifeng.beam.R;
import com.lifeng.beam.Utils;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Mr.Jude on 2015/8/17.
 */
public abstract class BeamListActivity<T extends BeamListActivityPresenter, M> extends BeamBaseActivity<T> {
    Context mCtx;
    private ListConfig mListConfig;
    private EasyRecyclerView mListView;
    private BeamListActivityPresenter.DataAdapter mAdapter;
    public EasyRecyclerView getListView() {
        return mListView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸状态栏和home，back，setting
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }*/
        mCtx = this;
        mListConfig = getConfig();
        createRecycler();
        findRecycler();
        initList();
        if (mListConfig.mStartWithProgress&&!getPresenter().inited) mListView.setAdapterWithProgress(mAdapter = getPresenter().getAdapter());
        else mListView.setAdapter(mAdapter = getPresenter().getAdapter());
        initAdapter();
    }

    public void stopRefresh(){
        if(mListView!=null)
            mListView.getSwipeToRefresh().setRefreshing(false);
    }
    public void showError(Throwable e){
        if (mListView!=null)
            mListView.showError();
    }

    public int getLayout(){
        return 0;
    }

    private void findRecycler(){
        mListView = (EasyRecyclerView) findViewById(R.id.recycler);
        if (mListView==null)throw new RuntimeException("No found recycler with id \"recycler\"");
        mListView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createRecycler(){
        if (getLayout()!=0){
            setContentView(getLayout());
        }else if (mListConfig.mContainerLayoutRes!=0){
            setContentView(mListConfig.mContainerLayoutRes);
        }else if (mListConfig.mContainerLayoutView!=null){
            setContentView(mListConfig.mContainerLayoutView);
        }else{
            EasyRecyclerView mListView = new EasyRecyclerView(this);
            mListView.setId(R.id.recycler);
            mListView.setLayoutManager(new LinearLayoutManager(this));
            mListView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            setContentView(mListView);
        }
    }

    private void initList(){
        if (mListConfig.mRefreshAble)mListView.setRefreshListener(getPresenter());
        if (mListConfig.mContainerProgressAble){
            if (mListConfig.mContainerProgressView != null)mListView.setProgressView(mListConfig.mContainerProgressView);
            else mListView.setProgressView(mListConfig.mContainerProgressRes);
        }
        if (mListConfig.mContainerErrorAble){
            if (mListConfig.mContainerErrorView != null)mListView.setErrorView(mListConfig.mContainerErrorView);
            else mListView.setErrorView(mListConfig.mContainerErrorRes);
        }
        if (mListConfig.mContainerEmptyAble){
            if (mListConfig.mContainerEmptyView != null)mListView.setEmptyView(mListConfig.mContainerEmptyView);
            else mListView.setEmptyView(mListConfig.mContainerEmptyRes);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mListConfig.mPaddingNavigationBarAble && Utils.hasSoftKeys(this)){
            mListView.setRecyclerPadding(0,0,0,Utils.getNavigationBarHeight(this));
        }
    }

    private void initAdapter(){
        if (mListConfig.mNoMoreAble){
            if (mListConfig.mNoMoreView != null)mAdapter.setNoMore(mListConfig.mNoMoreView);
            else mAdapter.setNoMore(mListConfig.mNoMoreRes);
        }
        if (mListConfig.mLoadmoreAble){
            if (mListConfig.mLoadMoreView != null)mAdapter.setMore(mListConfig.mLoadMoreView, getPresenter());
            else mAdapter.setMore(mListConfig.mLoadMoreRes, getPresenter());
        }
        if (mListConfig.mErrorAble){
            View errorView;
            if (mListConfig.mErrorView != null)errorView = mAdapter.setMore(mListConfig.mErrorView,getPresenter());
            else errorView = mAdapter.setError(mListConfig.mErrorRes);
            if (mListConfig.mErrorTouchToResumeAble)errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.resumeMore();
                }
            });
        }
    }

    protected ListConfig getConfig(){
        return ListConfig.Default.clone();
    }

    public int getViewType(int position){
        return 0;
    }

    public abstract BaseViewHolder<M> getViewHolder(ViewGroup parent,int viewType);


}
