package com.lifeng.f300.znpos2.module.user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.common.widget.PasswordInputView;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.adapter.MultiAdapter;
import com.lifeng.f300.znpos2.adapter.item.ColorItem;
import com.lifeng.f300.znpos2.adapter.item.ContentItem;
import com.lifeng.f300.znpos2.adapter.item.LineItem;
import com.lifeng.f300.znpos2.adapter.item.SubTitleItem;
import com.lifeng.f300.znpos2.adapter.item.TitleItem;
import com.lifeng.f300.znpos2.utils.MerchantRegisterDataManager;
import com.lifeng.f300.znpos2.utils.MerchantSignDataManager;
import com.lifeng.f300.znpos2.utils.Utils;
import com.lifeng.f300.znpos2.view.RecycleViewItemData;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/9/6.
 */
@RequiresPresenter(ResultPresenter.class)
public class ResultActivity extends BeamBaseActivity<ResultPresenter> {
    private static final int MSG_CANCEL_TRANS = 1;//撤销交易Message
    private static final int REQUSET_RESULT_TRANS = 45; // 撤销的请求

    private ArrayList<RecycleViewItemData> dataList;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.creditcard)
    RecyclerView creditcard;


    private Dialog dialogPwd;
    private PasswordInputView passwordText;
    private View keyboardView;
    private Editable editable;

    private TransResponse transResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        setContentView(R.layout.result_layout);
        ButterKnife.inject(this, getWindow().getDecorView());
        toolbar.setOnMenuItemClickListener(onMenuItemClick);


    }

    private void initData() {
        if (getIntent().getExtras().get("result") != null) {
            transResponse = (TransResponse) getIntent().getExtras().get("result");
        }
        initRecycleViewData(transResponse);

    }

    private void initRecycleViewData(TransResponse transResponse) {
        creditcard.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dataList=new ArrayList<RecycleViewItemData>();
        dataList.add(new RecycleViewItemData(new TitleItem(getString(R.string.pos_title)),1));
        dataList.add(new RecycleViewItemData(new SubTitleItem(getString(R.string.shcg)),2));
        if(StringUtil.isNotEmpty(transResponse.PAID)){
            if(transResponse.INVOICE_TYPE != null && transResponse.INVOICE_TYPE.equals("20")){ //20是消费撤销
                dataList.add(new RecycleViewItemData(new ColorItem(getString(R.string.xf), getString(R.string.consum_trans_revoke)+"￥"+transResponse.PAID),5));
            }else{
                dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.xf), "￥"+transResponse.PAID),4));
            }
        }


        recycleViewAddItemData(getString(R.string.merchant_name),MerchantSignDataManager.getInstance().getSignData().MERCHANTNAME,4);
        recycleViewAddItemData(getString(R.string.merchant_no),MerchantSignDataManager.getInstance().getSignData().MERCHANTCODE,4);
        recycleViewAddItemData(getString(R.string.terminal_number),MerchantRegisterDataManager.getInstance().getRegisterData().POSCODE ,4);
        recycleViewAddItemData(getString(R.string.pay_type),payType(transResponse.BANK_TYPE),4);
        recycleViewAddItemData(getString(R.string.operator),JUtils.getSharedPreference().getString("operatorId","") ,4);

        recycleViewAddItemData(getString(R.string.serial_number), transResponse.TRACE_NO ,4);
        recycleViewAddItemData(getString(R.string.batch_no),transResponse.BATCHNO ,4);
        recycleViewAddItemData(getString(R.string.voucher),transResponse.INVOICE_ID,4);
        recycleViewAddItemData(getString(R.string.create_time),transResponse.RESPONSE_TIME ,4);

        recycleViewAddItemData(getString(R.string.sdh),transResponse.BM_NAME ,4);
        recycleViewAddItemData(getString(R.string.fkh),transResponse.CARD_BANK_NAME ,4);
        recycleViewAddItemData(getString(R.string.str_expdate),transResponse.CARD_EXPIRE ,4);
        dataList.add(new RecycleViewItemData(new LineItem(getString(R.string.line)),0));

        recycleViewAddItemData(getString(R.string.yhkh),transResponse.CARD_EXPIRE ,4);
        if(StringUtil.isNotEmpty(transResponse.BANK_CARD_TYPE)) {
            recycleViewAddItemData(getString(R.string.str_expdate), bankReadType(transResponse.BANK_STARASN, transResponse.BANK_CARD_TYPE), 4);
        }
        recycleViewAddItemData(getString(R.string.shbh),transResponse.BM_MERID ,4);
        recycleViewAddItemData(getString(R.string.zdbh),transResponse.BM_POSID ,4);
        recycleViewAddItemData(getString(R.string.lsbh),transResponse.TRADE_SN ,4);
        recycleViewAddItemData(getString(R.string.authorization_code),transResponse.REFER_NUM ,4);
        dataList.add(new RecycleViewItemData(new LineItem(getString(R.string.line)),0));
       /* dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.merchant_name), MerchantSignDataManager.getInstance().getSignData().MERCHANTNAME ),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.merchant_no), MerchantSignDataManager.getInstance().getSignData().MERCHANTCODE ),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.terminal_number), MerchantRegisterDataManager.getInstance().getRegisterData().POSCODE ),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.pay_type), payType(transResponse.BANK_TYPE) ),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.operator), JUtils.getSharedPreference().getString("operatorId","") ),4));

        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.serial_number), transResponse.TRACE_NO ),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.batch_no), transResponse.BATCHNO ),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.voucher), transResponse.INVOICE_ID ),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.create_time),transResponse.RESPONSE_TIME ),4));*/

       /* if(StringUtil.isNotEmpty(transResponse.BM_NAME)){
            dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.sdh), transResponse.BM_NAME),4));
        }
        if(StringUtil.isNotEmpty(transResponse.CARD_BANK_NAME)){
            dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.fkh), transResponse.CARD_BANK_NAME),4));
        }
        if(transResponse.CARD_EXPIRE != null){
            dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.str_expdate),transResponse.CARD_EXPIRE ),4));
        }
        dataList.add(new RecycleViewItemData(new LineItem(getString(R.string.line)),0));

        if(transResponse.BANK_CARD_TYPE !=null){
            dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.yhkh),bankReadType(transResponse.BANK_STARASN,transResponse.BANK_CARD_TYPE)),4));
        }
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.shbh),transResponse.BM_MERID),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.zdbh),transResponse.BM_POSID),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.lsbh),transResponse.TRADE_SN),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.authorization_code),transResponse.AUTH_CODE),4));
        dataList.add(new RecycleViewItemData(new ContentItem(getString(R.string.reference_number),transResponse.REFER_NUM),4));
        dataList.add(new RecycleViewItemData(new LineItem(getString(R.string.line)),0));
*/
        JUtils.Log("array list size:"+dataList.size());
        creditcard.setAdapter(new MultiAdapter(dataList));
    }

    /**
     *先判断text2是否为空，然后recycleView 添加item数据
     * @param text1  每个item的名字
     * @param text2  每个item的内容,
     * @param type   item的显示格式
     */
    private void recycleViewAddItemData(String text1, String text2, int type){
        if(StringUtil.isNotEmpty(text2)){
            dataList.add(new RecycleViewItemData(new ContentItem(text1, text2 ),type));
        }
    }
    /**
     *
     * @param bank_starasn
     * @param bank_card_type   银行卡类型：0：磁条卡；1：IC卡 2：非接卡
     * @return
     */
    private String bankReadType(String bank_starasn, String bank_card_type) {
        if(bank_card_type.equals("0")){
            return bank_starasn+"  S";
        }else if (bank_card_type.equals("1")){
            return bank_starasn+"  I";
        }else if(bank_starasn.equals("2")){
            return bank_starasn+"   C";
        }else{
            return "";
        }
    }

    /**
     *
     * @param bank_type
     * @return 第三方支付类型 0没有、1银行卡、2微信、3支付宝
     */
    private String payType(String bank_type) {
        if(bank_type.equals("1")){
            return getString(R.string.yhk);
        }else if(bank_type.equals("2")){
            return getString(R.string.wx);
        }else if(bank_type.equals("3")){
            return getString(R.string.zfb);
        }else {
            return "";
        }
    }

    private void initEvent() {
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_cancel_trans:
                    inputCancelTransPwd();
                    JUtils.ToastLong("cancel trans");
                    break;
            }
            return true;
        }
    };

    /**
     * 输入主管密码
     */
    private void inputCancelTransPwd() {
        if (null != dialogPwd) {
            dialogPwd.dismiss();
            dialogPwd = null;
        }
        dialogPwd = Utils.setDialog(this, R.layout.cashier_details3, 1, 0);
        dialogPwd.getWindow().setGravity(Gravity.CENTER);
        TextView detailName = (TextView) dialogPwd.findViewById(R.id.detail_name);// 标题
        TextView tvKouKuan = (TextView) dialogPwd.findViewById(R.id.tv_kouKuan);// 消费
        TextView biaoJi = (TextView) dialogPwd.findViewById(R.id.biaoji);// 标题 ¥
        tvKouKuan.setText("");
        detailName.setText(getString(R.string.cancel_trans_over));
        biaoJi.setText(getString(R.string.input_pwds));
        passwordText = (PasswordInputView) dialogPwd.findViewById(R.id.passwordText);
        passwordText.setPasswordColor(getResources().getColor(R.color.black_color));
        passwordText.setBorderColor(getResources().getColor(R.color.pwd_border_color));
        keyboardView = dialogPwd.findViewById(R.id.keyboardview);
        passwordText.setTag(200);// 校验管理员密码
        passwordText.addTextChangedListener(watcherPwd);// 监听密码
        dialogPwd.findViewById(R.id.re_pwd).setVisibility(View.VISIBLE);
        dialogPwd.findViewById(R.id.tv_pwd_tp).setVisibility(View.GONE);
        dialogPwd.findViewById(R.id.re_detail_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPwd.cancel();
            }
        });
        displayView();
        dialogPwd.show();
    }

    // 密码键盘的监听
    TextWatcher watcherPwd = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String pwd = passwordText.getText().toString();
            JUtils.Log("用户的密码：" + pwd);
            if (pwd.length() == 6) {
                loadValidatePwd(pwd);
            }
        }
    };

    /**
     * 开始检验管理员的密码
     *
     * @param pwd
     */
    private void loadValidatePwd(String pwd) {
        int codeNumber = Integer.parseInt(passwordText.getTag().toString());
        if (codeNumber == 200) {// 校验密码
            getPresenter().pCheckPwd(pwd);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_result, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void toastWrongPwd() {
        JUtils.ToastLong(getString(R.string.password_wrong));
        clearCharge();
    }

    /**
     * 清空主管密码的方法
     */
    private void clearCharge() {
        if (null != passwordText) {
            editable = passwordText.getText();
            editable.clear();
        }
    }

    /***
     * 弹出密码输入框
     */
    private void displayView() {
        passwordText.setInputType(InputType.TYPE_NULL);
        passwordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (keyboardView.getVisibility() == View.GONE) {
                    keyboardView.setVisibility(View.VISIBLE);
                    dialogPwd.getWindow().setGravity(Gravity.BOTTOM);
                    dialogPwd.show();
                }
                return false;
            }
        });
        if (keyboardView.getVisibility() == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
            dialogPwd.getWindow().setGravity(Gravity.BOTTOM);
            dialogPwd.show();
        }
    }

    /***
     * 自定义数字键盘点击
     *
     * @param v
     */
    public void keyNumClick(View v) {
        int code = Integer.parseInt(v.getTag().toString());
        int index = 0;
        if (passwordText != null) {
            index = passwordText.getSelectionStart();
            editable = passwordText.getText();
        }
        if (code == -2) { // 删除
            try {
                editable.delete(index - 1, index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (code == -1) {// 隐藏
            dialogPwd.getWindow().setGravity(Gravity.CENTER);
            dialogPwd.show();
            keyboardView.setVisibility(View.GONE);
        } else if (code == -3) {// 确认
            dialogPwd.getWindow().setGravity(Gravity.BOTTOM);// 设置密码整个布局在
            dialogPwd.show();
            String pwd = passwordText.getText().toString();
            loadValidatePwd(pwd);
        } else {
            editable.insert(index, code + "");
        }
    }

    public void sendMsgCancelTrans() {
        handler.sendEmptyMessage(MSG_CANCEL_TRANS);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CANCEL_TRANS:
                    keyboardView.setVisibility(View.GONE);
                    dialogPwd.cancel();
                    sendToCancelTrans();
                    break;
                default:
                    break;
            }
        }
    };

    private void sendToCancelTrans() {
        JUtils.Log("sendToCancelTrans");
        TransResponse transResponse = null;
        Intent intent = new Intent(ResultActivity.this, CancelTransActivity.class);
        intent.putExtra("result", transResponse);
        startActivityForResult(intent, REQUSET_RESULT_TRANS);
       /* Intent intent = new Intent(ResultActivity.this, CancelTransActivity.class);
        startActivityForResult(intent, REQUSET_RESULT_TRANS);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUSET_RESULT_TRANS: // 更新撤销的交易信息
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
