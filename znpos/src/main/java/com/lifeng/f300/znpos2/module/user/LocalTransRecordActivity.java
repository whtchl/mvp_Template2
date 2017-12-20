package com.lifeng.f300.znpos2.module.user;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.contract.OnTransFlowTouchListener;
import com.lifeng.f300.common.entites.SettlementRequest;
import com.lifeng.f300.common.entites.SingleFlowEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.DeviceUtils;
import com.lifeng.f300.common.utils.PosDataUtils;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.adapter.TransFlowListAdapter;
import com.lifeng.f300.znpos2.contract.SwipeRefreshLayer;
import com.lifeng.f300.znpos2.utils.MerchantRegisterDataManager;
import com.lifeng.f300.znpos2.utils.MerchantSignDataManager;
import com.lifeng.f300.znpos2.view.MultiSwipeRefreshLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.lifeng.f300.config.ConnectURL.ENVIRONMENT;


/**
 * Created by happen on 2017/9/7.
 */
@RequiresPresenter(LocalTransRecordPresenter.class)
public class LocalTransRecordActivity extends BeamBaseActivity<LocalTransRecordPresenter> implements SwipeRefreshLayer {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @InjectView(R.id.rv_trans_flow)
    RecyclerView rvTransFlowRecyclerView;
    @InjectView(R.id.swipe_refresh_layout)
    MultiSwipeRefreshLayout mSwipeRefreshLayout;

    TransFlowListAdapter mTransFlowListAdapter;
    @InjectView(R.id.tv_batch)
    TextView tvBatch;
    @InjectView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @InjectView(R.id.rl_batch_totalcash)
    RelativeLayout rlBatchTotalcash;
    private List<TransResponse> mTransFlowList = new ArrayList<TransResponse>();

    private boolean mIsRequestDataRefresh = false;

    private ArrayList<TransResponse> mFlowTransResponseList = new ArrayList<TransResponse>();
    public static LocalTransRecordActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
        instance = this;
    }

    private void initEvent() {
    }

    private void initData() {

    }

    private void initView() {
        setContentView(R.layout.activity_local_trans_record);
        ButterKnife.inject(this);
        setupRecyclerView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trans_flows, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_old_trans:
                JUtils.Log("历史结算流水查询");
               /* for (int i = 0; i < mFlowTransResponseList.size(); i++) {
                    JUtils.Log("***** " + mFlowTransResponseList.get(i).REMARK + "   ***  " + mFlowTransResponseList.get(i).STATUS);
                }*/

                //历史结算流水查询

                Intent selectOldBatch = new Intent(this, WebActivity.class);
                selectOldBatch.putExtra("type",5);
                startActivity(selectOldBatch);
                return true;
            case R.id.action_old_settlement:
                JUtils.Log("历史结算查询");
                Intent selectOld = new Intent(this, WebActivity.class);
                selectOld.putExtra("type", 4);
                startActivity(selectOld);
                return true;

            case R.id.settlement:
                JUtils.Log("结算");
                if (mFlowTransResponseList != null && mFlowTransResponseList.size() == 0) {
                    JUtils.ToastLong( getString(R.string.no_transaction_flow));
                    return true;
                }
                settlement();

        }
        return super.onOptionsItemSelected(item);
    }


    //开始结算
    private void settlement() {

        PackageInfo packInfo = null;
        try {
            packInfo = instance.getPackageManager().getPackageInfo(instance.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SettlementRequest request = new SettlementRequest();
        request.DEVICEID = DeviceUtils.getDeviceSn(instance);
        request.MERCHANTCODE=  MerchantSignDataManager.getInstance().getSignData().MERCHANTCODE;
        request.POSCODE = MerchantRegisterDataManager.getInstance().getRegisterData().POSCODE;
        request.BATCHNUM = MerchantSignDataManager.getInstance().getSignData().BATCHNUM;
        request.VISION = packInfo.versionName;


        int consume_count_trade = 0;
        StringBuffer consume_bank_amount = new StringBuffer();  //消费总金额减去撤销总金额
        int cancel_count_trade=0;
        StringBuffer cancel_bank_amount = new StringBuffer();   //撤销总金额
        StringBuffer invoice_id_list = new StringBuffer();

        HashMap cancelTrans = new HashMap();   //撤销的流水：AUTH_CODE，BANK_AMOUNT
        HashMap cousumeTrans = new HashMap();   //成功流水：AUTH_CODE，BANK_AMOUNT

        BigDecimal consumeMoney = new BigDecimal("0");
        BigDecimal totalConsumeMoney = new BigDecimal("0");
        BigDecimal cancelMoney = new BigDecimal("0");
        BigDecimal totalCancelMoney = new BigDecimal("0");

        for(int i=0;i<mFlowTransResponseList.size();i++){

            //凭证号列表
            invoice_id_list.append(mFlowTransResponseList.get(i).INVOICE_ID);
            invoice_id_list.append(",");
            //撤销总笔数
            if(mFlowTransResponseList.get(i).INVOICE_TYPE != null && mFlowTransResponseList.get(i).INVOICE_TYPE.equals("20")){
                cancel_count_trade++;
                if(StringUtil.isNotEmpty(mFlowTransResponseList.get(i).AUTH_CODE) &&StringUtil.isNotEmpty(mFlowTransResponseList.get(i).BANK_AMOUNT) ){
                    cancelTrans.put(mFlowTransResponseList.get(i).AUTH_CODE,mFlowTransResponseList.get(i).BANK_AMOUNT);
                }

            }
            //计算撤销的总金额
            if(StringUtil.isNotEmpty(mFlowTransResponseList.get(i).PAID) && StringUtil.isDigit(mFlowTransResponseList.get(i).PAID)){
                BigDecimal decimal = new BigDecimal(mFlowTransResponseList.get(i).PAID);
                totalCancelMoney = totalCancelMoney.add(decimal);
            }

            //消费的总笔数
            if(mFlowTransResponseList.get(i).INVOICE_TYPE != null && mFlowTransResponseList.get(i).INVOICE_TYPE.equals("3")){
                if(StringUtil.isNotEmpty(mFlowTransResponseList.get(i).AUTH_CODE) &&StringUtil.isNotEmpty(mFlowTransResponseList.get(i).BANK_AMOUNT) ){
                    cousumeTrans.put(mFlowTransResponseList.get(i).AUTH_CODE,mFlowTransResponseList.get(i).BANK_AMOUNT);
                }
            }

            //计算消费的总金额
            if(StringUtil.isNotEmpty(mFlowTransResponseList.get(i).PAID) && StringUtil.isDigit(mFlowTransResponseList.get(i).PAID)){
                BigDecimal decimal = new BigDecimal(mFlowTransResponseList.get(i).PAID);
                totalConsumeMoney = totalConsumeMoney.add(decimal);
            }
        }

        Set consumeSet = cousumeTrans.entrySet();
        int sameTrans=0;//撤销和消费中AUTH_CODE相同的笔数
        for(Iterator consumeIter = consumeSet.iterator(); consumeIter.hasNext();)
        {
            Set cancelSet = cancelTrans.entrySet();
            for(Iterator cancelIter = consumeSet.iterator(); cancelIter.hasNext();){
                if(((Map.Entry)consumeIter.next()).getKey().equals(
                        ((Map.Entry)cancelIter.next()).getKey()  )
                        ){
                    sameTrans++;
                }
            }
        }

        //消费总金额
        if(totalConsumeMoney.add(totalCancelMoney).compareTo(BigDecimal.ZERO) == -1){  //消费总金额-撤销总金额小于0 ，那么报错
            JUtils.ToastLong(getString(R.string.jecc1));
            return ;
        }else{
            request.CONSUME_BANK_AMOUNT = StringUtil.formatterAmount( totalConsumeMoney.add(totalCancelMoney).toString());
        }

        //撤销总金额
        request.CANCEL_BANK_AMOUNT =  StringUtil.formatterAmount(totalConsumeMoney.toString());

        //消费的笔数（不包括撤销的笔数）
        if((cousumeTrans.size()-sameTrans)>=0){
            request.CANCEL_COUNT_TRADE = String.valueOf(cousumeTrans.size()-sameTrans);
        }else{
            JUtils.ToastLong(getString(R.string.jecc1));
            return ;
        }

        //撤销的笔数
        if(cancel_count_trade>=0){
            request.CANCEL_COUNT_TRADE =  String.valueOf(cancel_count_trade);
        }else{
            request.CANCEL_COUNT_TRADE="";
        }
        //凭证号列表
        if(  request.INVOICE_ID_LIST.length()>0){
            request.INVOICE_ID_LIST = invoice_id_list.substring(0,invoice_id_list.length()-1);
        }else{
            request.INVOICE_ID_LIST="";
        }


        getPresenter().pSettlement(request);

        //clearViewData();
    }

    public  void clearViewData(){
        PosDataUtils.delFile(ENVIRONMENT+ ConnectURL.PATH_SPECIAL+ConnectURL.FILE_REGISTER);
        Intent intent = new Intent(LocalTransRecordActivity.this,LoginActivity.class);
        intent.putExtra("isReset", true);
        startActivity(intent);
        finish();
    }

    public void addFlowTransResponseList(TransResponse s) {
        mFlowTransResponseList.add(s);
        JUtils.Log("list size:" + mFlowTransResponseList.size());

    }

    private void setupRecyclerView() {
        /*TransFlowEntity m = new TransFlowEntity("消费","12rmb");
        mTransFlowList= new ArrayList<>();
        mTransFlowList.add(m);*/

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTransFlowRecyclerView.setLayoutManager(layoutManager);
        mTransFlowListAdapter = new TransFlowListAdapter(this, mTransFlowList);
        rvTransFlowRecyclerView.setAdapter(mTransFlowListAdapter);

        mTransFlowListAdapter.setOnTransFlowTouchListener(new OnTransFlowTouchListener() {
            @Override
            public void onTouch(View v, View card, TransResponse mTransFlowEntity) {
                if (v == card) {
                    //查看流水详情
                    Intent intent = new Intent(LocalTransRecordActivity.this, ResultActivity.class);
                    intent.putExtra("result", mTransFlowEntity);
                    startActivity(intent);
                }
            }
        });
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        Log.e("wang", "computeVerticalScrollExtent+computeVerticalScrollOffset>= computeVerticalScrollRange  ? ||" +
                recyclerView.computeVerticalScrollExtent() + "  + " + recyclerView.computeVerticalScrollOffset() + "  ?  "
                + recyclerView.computeVerticalScrollRange());
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        trySetupSwipeRefresh();
        new Handler().postDelayed(() -> setRefresh(true), 358);
        queryFlowInfoByBatchNo();
    }

    /**
     * 从服务器获取数据后更新recycleView
     */
    public void RefreshRecycleView() {
       /* TransFlowEntity m = new TransFlowEntity("消费","12rmb");
        mTransFlowList= new ArrayList<>();
        mTransFlowList.add(m);*/
        JUtils.Log("current thread:" + Thread.currentThread().toString() + Thread.currentThread().getName());
        mFlowTransResponseList.size();
        mTransFlowList.clear();
        mTransFlowList.addAll(mFlowTransResponseList);
        mTransFlowListAdapter.notifyDataSetChanged();
        setRefresh(false);

        //如果有流水信息，那么显示批次号和累计收银；否则不显示
        if(mFlowTransResponseList.size()>0){
            rlBatchTotalcash.setVisibility(View.VISIBLE);
            tvBatch.setText(getString(R.string.batch_no)+":"+MerchantSignDataManager.getInstance().getSignData().BATCHNUM);
            tvTotalAmount.setText(calTotalMoney(mFlowTransResponseList));
        }else{
            rlBatchTotalcash.setVisibility(View.GONE);
        }
    }

    /**
     * 根据服务器返回的流水列表，计算该批次的累计收银
     * @param mFlowTransResponseList
     * @return
     */
    private String calTotalMoney(ArrayList<TransResponse> mFlowTransResponseList) {
        BigDecimal totalMoney = new BigDecimal("0");
        String totalMoneyOk;
        for(TransResponse transResponse : mFlowTransResponseList){
            String money = transResponse.PAID;
            if(StringUtil.isNotEmpty(money) && StringUtil.isDigit(money)){
                BigDecimal decimal = new BigDecimal(money);
                totalMoney = totalMoney.add(decimal);
            }
        }
        totalMoneyOk = StringUtil.formatterAmount(totalMoney.toString());
        return totalMoneyOk;
    }

    /**
     * 根据批次号查询该批次的流水信息
     *
     * @param
     */
    private void queryFlowInfoByBatchNo() {

        JUtils.Log("已经交易的最大流水号：" + PosDataUtils.getYJYMAXPosSerialNumber());
        mFlowTransResponseList.clear();
        //getPresenter().ptest();
        getPresenter().pQueryFlowInfoByBatchNo2(getListSingleFlowEntity());
        /*getPresenter().pQueryFlowInfoByBatchNo2("000159",
                PosDataUtils.getPosSerialNumberList(),
                JUtils.getSharedPreference().getString("operatorId", ""),
                MerchantRegisterDataManager.getInstance().getRegisterData().MERCHANT_CODE,
                MerchantRegisterDataManager.getInstance().getRegisterData().POSCODE,
                MerchantSignDataManager.getInstance().getSignData().SECONDARYKEY);*/

        /*getPresenter().pQueryFlowInfoByBatchNo(MerchantSignDataManager.getInstance().getSignData().BATCHNUM,
                PosDataUtils.getYJYMAXPosSerialNumber(),
                JUtils.getSharedPreference().getString("operatorId", ""),
                MerchantRegisterDataManager.getInstance().getRegisterData().MERCHANT_CODE,
                MerchantRegisterDataManager.getInstance().getRegisterData().POSCODE,
                MerchantSignDataManager.getInstance().getSignData().SECONDARYKEY);*/
    }

    private List<SingleFlowEntity> getListSingleFlowEntity() {
        ArrayList<SingleFlowEntity> singleFlowEntity = new ArrayList();
        List<String> mListSerialNum = PosDataUtils.getPosSerialNumberList();
        String operatorId = JUtils.getSharedPreference().getString("operatorId", "");
        MerchantRegisterDataManager.RegisterData merchantData = MerchantRegisterDataManager.getInstance().getRegisterData();
        MerchantSignDataManager.SignData signData = MerchantSignDataManager.getInstance().getSignData();
        JUtils.Log("size:" + mListSerialNum.size());
        for (int i = 0; i < mListSerialNum.size(); i++) {
            /*singleFlowEntity.add(new SingleFlowEntity(MerchantSignDataManager.getInstance().getSignData().BATCHNUM,mListSerialNum.get(i),operatorId,
                    merchantData.MERCHANT_CODE,merchantData.POSCODE,signData.SECONDARYKEY));*/
            singleFlowEntity.add(new SingleFlowEntity("000159", mListSerialNum.get(i), operatorId,
                    merchantData.MERCHANT_CODE, merchantData.POSCODE, signData.SECONDARYKEY));
            singleFlowEntity.get(i).toString();
        }
        return singleFlowEntity;
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
        queryFlowInfoByBatchNo();
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
    public void setCanChildScrollUpCallback(MultiSwipeRefreshLayout.CanChildScrollUpCallback canChildScrollUpCallback) {
        mSwipeRefreshLayout.setCanChildScrollUpCallback(canChildScrollUpCallback);
    }


    public boolean isRequestDataRefresh() {
        return mIsRequestDataRefresh;
    }
}
