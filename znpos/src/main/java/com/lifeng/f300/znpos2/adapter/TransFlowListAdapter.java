package com.lifeng.f300.znpos2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.contract.OnTransFlowTouchListener;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.Constants;
import com.lifeng.f300.common.utils.DateUtil;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.znpos2.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/9/7.
 */

public class TransFlowListAdapter extends RecyclerView.Adapter<TransFlowListAdapter.ViewHolder> {



    private List<TransResponse> mList;
    private Context mContext;
    private OnTransFlowTouchListener mOnTransFlowTouchListener;

    public TransFlowListAdapter(Context context, List<TransResponse> mTransFlowList) {
        mList = mTransFlowList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.trans_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // LayoutInflater.from(mContext).inflate(R.layout.trans_items,parent);
        TransResponse mTransFlowEntity = mList.get(position);
        viewHolder.mTransFlowEntity = mTransFlowEntity;


        viewHolder.transDay.setText(DateUtil.getDateDetail(mTransFlowEntity.TRANS_TIME));
        if (Constants.TODAY.equals(viewHolder.transDay.getText().toString()) || Constants.YESTERDAY.equals(viewHolder.transDay.getText().toString())) {
            viewHolder.localTransTimeTextview.setText(formatTime(mTransFlowEntity.TRANS_TIME, "HH:mm"));
        } else {
            viewHolder.localTransTimeTextview.setText(formatTime(mTransFlowEntity.TRANS_TIME, "MM-dd"));
        }
        viewHolder.ivTransType.setBackgroundResource(R.drawable.icon_xiaofei);
        //20表示类型是撤销
        if (mTransFlowEntity.INVOICE_TYPE.equals("20")) {


            //消费显示. 如果是撤销那么字体是黄色。
            viewHolder.tvRemarkType.setTextColor(mContext.getResources().getColor(R.color.money_red));
            String[] remarkList1 = mTransFlowEntity.REMARK.split("：");
            try{
                if(remarkList1.length == 2){
                    viewHolder.tvRemarkType.setText(remarkList1[0]);
                    /*if(remarkList1[1].length()>1){
                        viewHolder.tvRemarkMoney.setText(  "  ¥ "+remarkList1[1].substring(0,remarkList1[1].length()-1));
                    }*/
                    viewHolder.tvRemarkMoney.setText("¥ "+mTransFlowEntity.PAID);

                }else{
                    viewHolder.tvRemarkType.setText(mTransFlowEntity.REMARK);
                }

            }catch (Exception e){
                 JUtils.Log(e.getMessage());
                viewHolder.tvRemarkType.setText(mTransFlowEntity.REMARK);
            }
        }else  if(mTransFlowEntity.INVOICE_TYPE.equals("3")){   //3 是消费
            String[] remarkList2 = mTransFlowEntity.REMARK.split("：");
            try{
                if(remarkList2.length == 2){
                    viewHolder.tvRemarkType.setText(remarkList2[0]);
                    /*if(remarkList2[1].length()>1) {
                        viewHolder.tvRemarkMoney.setText("  ¥ " + remarkList2[1].substring(0, remarkList2[1].length() - 1));
                    }*/
                    viewHolder.tvRemarkMoney.setText("¥ "+mTransFlowEntity.PAID);
                }else{
                    viewHolder.tvRemarkType.setText(mTransFlowEntity.REMARK);
                }

            }catch (Exception e){
                JUtils.Log(e.getMessage());
                viewHolder.tvRemarkType.setText(mTransFlowEntity.REMARK);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setOnTransFlowTouchListener(OnTransFlowTouchListener onTransFlowTouchListener) {
        this.mOnTransFlowTouchListener = onTransFlowTouchListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View card;
        TransResponse mTransFlowEntity;
        @InjectView(R.id.trans_day)
        TextView transDay;
        @InjectView(R.id.local_trans_time_textview)
        TextView localTransTimeTextview;
        @InjectView(R.id.ll_time)
        LinearLayout llTime;
        @InjectView(R.id.iv_trans_type)
        ImageView ivTransType;
        @InjectView(R.id.tv_remark_type)
        TextView tvRemarkType;
        @InjectView(R.id.tv_remark_money)
        TextView tvRemarkMoney;
        @InjectView(R.id.ll_amount)
        LinearLayout llAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            ButterKnife.inject(this, itemView);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnTransFlowTouchListener.onTouch(v, card, mTransFlowEntity);
        }
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    private String formatTime(String time, String mformat) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = format.parse(time);
            return new SimpleDateFormat(mformat).format(date);
        } catch (Exception e) {
            JUtils.Log(e.getLocalizedMessage());
        }

        return null;
    }

    //1是正常，2是已撤销
    private String getTRANS_STATUS(String status) {
        if (status != null) {
            if (status.equals("2")) {
                return mContext.getString(R.string.trans_revoke);
            }
        }
        return "";
    }

    private String getBANK_AMOUNT(String amount) {
        return "¥ " + StringUtil.formatterAmount(amount);
    }

    // private String getINVOICE_TYPE(String )
}


