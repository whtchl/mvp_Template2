package com.lifeng.szx.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.contract.OnTransFlowTouchListener;
import com.lifeng.f300.common.entites.LpResponse;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.szx.MainActivity;
import com.lifeng.szx.R;
import com.lifeng.szx.SettingsActivity;
import com.lifeng.szx.adapter.LpListAdapter;
import com.lifeng.szx.interfaces.OnLpTouchListener;
import com.lifeng.szx.view.MultiSwipeRefreshLayout;
import com.lifeng.szx.view.SwipeRefreshLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/12/4.
 */
@RequiresPresenter(LpcxPresenter.class)
public class LpcxActivity extends BeamBaseActivity<LpcxPresenter> implements SwipeRefreshLayer {
    private static final int MSG_GOTO_UPDATE_RECYCLEVIEW = 8;        //结算
    Toolbar toolbarLpcx;
    RecyclerView lpcxList;
    MultiSwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rvTransFlowRecyclerView;

    private Boolean mIsRequestDataRefresh = false;
    private ArrayList<LpResponse> mFlowLpResponseList = new ArrayList<LpResponse>();
    private List<LpResponse> mLpList = new ArrayList<LpResponse>();
    LpListAdapter mLpListAdapter;

    String third="",mchcode="";

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Log.i("wang", "000");
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.ab_search:
                    JUtils.ToastLong("理赔查询");
                    break;
                default:
                    break;
            }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_lpcx);
        toolbarLpcx = (Toolbar) this.findViewById(R.id.toolbar_lpcx);
        lpcxList = (RecyclerView) this.findViewById(R.id.lpcx_list);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout)this.findViewById(R.id.swipe_refresh_layout);
        rvTransFlowRecyclerView =(RecyclerView)this.findViewById(R.id.lpcx_list);
        setSupportActionBar(toolbarLpcx);
        toolbarLpcx.setOnMenuItemClickListener(onMenuItemClick);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupRecyclerView();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /*trySetupSwipeRefresh();
        new Handler().postDelayed(() -> setRefresh(true), 358);
        queryLplb();//查询理赔列表*/

        chaxun();



    }

    private void chaxun(){

        third=JUtils.getSharedPreference().getString("sp_third","");
        mchcode=JUtils.getSharedPreference().getString("sp_mchcode","");

        if(TextUtils.isEmpty(third)
                || TextUtils.isEmpty(mchcode)){
            JUtils.ToastLong("请设置支付公司标识或商户号");
            return ;
        }else{
            //查询列表
            gotoQueryList();
        }
    }

    //查询理赔列表
    private void queryLplb() {
        mFlowLpResponseList.clear();
        getPresenter().pQueryLpList(third,mchcode);
    }

    void trySetupSwipeRefresh() {
        JUtils.Log("tchl trySetupSwipeRefresh ");
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_3,
                    R.color.refresh_progress_2, R.color.refresh_progress_1);
            // Do not use lambda here!
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                }
            });
        }
    }


    @Override
    public void requestDataRefresh() {
        JUtils.Log("tchl  requestDataRefresh set to true");
        mIsRequestDataRefresh = true;
        queryLplb();//查询理赔列表
        //queryFlowInfoByBatchNo();
    }

    public void setRefresh(boolean requestDataRefresh) {
        if (mSwipeRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            // 防止刷新消失太快，让子弹飞一会儿.
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 1000);
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void setProgressViewOffset(boolean scale, int start, int end) {
        mSwipeRefreshLayout.setProgressViewOffset(scale, start, end);
    }

    @Override
    public void setCanChildScrollUpCallback(MultiSwipeRefreshLayout.CanChildScrollUpCallback callback) {
        mSwipeRefreshLayout.setCanChildScrollUpCallback(callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lpcx, menu);
        MenuItem menuItem = menu.findItem(R.id.ab_search);//在菜单中找到对应控件的item
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        Log.d("wang", "menu create");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String queryText) {
                Log.d("wang", "onQueryTextChange = " + queryText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {

                Log.d("wang", "onQueryTextSubmit = " + queryText);

                if (searchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    searchView.clearFocus(); // 不获取焦点

                    if (TextUtils.isEmpty(queryText.trim())) {
                        JUtils.ToastLong(getString(R.string.input_phone));
                        return true;
                    }

                    if (!StringUtil.isMobileNo(queryText.trim())) {
                        JUtils.ToastLong(getString(R.string.phone_error));
                        return true;
                    }
                    third=JUtils.getSharedPreference().getString("sp_third","");
                    mchcode=JUtils.getSharedPreference().getString("sp_mchcode","");

                    if(TextUtils.isEmpty(third)
                            || TextUtils.isEmpty(mchcode)){
                        JUtils.ToastLong("请设置支付公司标识或商户号");
                        return true;
                    }else{
                        //查询列表
                        gotoQueryList();
                    }
                }
                return true;
            }

        });
        return true;
    }

    private void gotoQueryList() {
        trySetupSwipeRefresh();
        /*new Handler().postDelayed(() -> setRefresh(true), 358);*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefresh(true);
            }
        },358);
        queryLplb();//查询理赔列表
    }
        /*trySetupSwipeRefresh();
        new Handler().postDelayed(() -> setRefresh(true), 358);
        queryLplb();//查询理赔列表*/



    private void setupRecyclerView() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTransFlowRecyclerView.setLayoutManager(layoutManager);
        mLpListAdapter = new LpListAdapter(this, mLpList);
        rvTransFlowRecyclerView.setAdapter(mLpListAdapter);

        mLpListAdapter.setOnTransFlowTouchListener(new OnLpTouchListener() {
            @Override
            public void onTouch(View v, View card, LpResponse mTransFlowEntity) {
                if (v == card) {
                    //查看流水详情
                    Intent intent = new Intent(LpcxActivity.this, LpDetailActivity.class);
                    intent.putExtra("id", mTransFlowEntity.id);
                    startActivity(intent);
                }
            }
        });

    }

    public void  addLpResponseList(List<LpResponse> s){
         mFlowLpResponseList.addAll(s);
        JUtils.Log("list size:" + mFlowLpResponseList.size());
    }

    /**
     * 从服务器获取数据后更新recycleView
     */
    public void RefreshRecycleView() {
       /* TransFlowEntity m = new TransFlowEntity("消费","12rmb");
        mTransFlowList= new ArrayList<>();
        mTransFlowList.add(m);*/
        JUtils.Log("current thread:" + Thread.currentThread().toString() + Thread.currentThread().getName());
        mFlowLpResponseList.size();
        mLpList.clear();
        mLpList.addAll(mFlowLpResponseList);
        mLpListAdapter.notifyDataSetChanged();
        setRefresh(false);

        //如果有流水信息，那么显示批次号和累计收银；否则不显示
        /*if(mFlowLpResponseList.size()>0){
            rlBatchTotalcash.setVisibility(View.VISIBLE);
            tvBatch.setText(getString(R.string.batch_no)+":"+MerchantSignDataManager.getInstance().getSignData().BATCHNUM);
            tvTotalAmount.setText(calTotalMoney(mFlowTransResponseList));
        }else{
            rlBatchTotalcash.setVisibility(View.GONE);
        }*/
    }

    public void gotoMsgUpdateList(List<LpResponse> l){
        addLpResponseList(l);
        Message msg = Message.obtain();
        msg.what = MSG_GOTO_UPDATE_RECYCLEVIEW;

        handler.sendMessage(msg);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GOTO_UPDATE_RECYCLEVIEW:
                    RefreshRecycleView();
                    break;
                default:
                    break;
            }
        }
    };
}
