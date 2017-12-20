package com.lifeng.szx.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifeng.f300.common.entites.LpResponse;
import com.lifeng.f300.common.utils.DateUtil;
import com.lifeng.szx.R;
import com.lifeng.szx.interfaces.OnLpTouchListener;

import java.util.List;


/**
 * Created by happen on 2017/12/6.
 */

public class LpListAdapter extends RecyclerView.Adapter<LpListAdapter.ViewHolder> {

    private List<LpResponse> mList;
    private Context mContext;
    private OnLpTouchListener mOnLpTouchListener;

    public LpListAdapter(Context context, List<LpResponse> mLpList) {
        mList = mLpList;
        mContext = context;
    }

    @Override
    public LpListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.lp_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LpListAdapter.ViewHolder viewHolder, int position) {

        LpResponse mLpEntity = mList.get(position);
        viewHolder.mLpEntity = mLpEntity;

        viewHolder.tv_id.setText("编号:"+mLpEntity.id);
        viewHolder.tv_mchcode.setText("报案时间:"+mLpEntity.happen_time);
        viewHolder.tv_third.setText("理赔金额:"+mLpEntity.report_money+" 元");

    }

    @Override
    public int getItemCount() {
        return  mList.size();

    }
    public void setOnTransFlowTouchListener(OnLpTouchListener mOnLpTouchListener) {
        this.mOnLpTouchListener = mOnLpTouchListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View card;
        LpResponse mLpEntity;
        LinearLayout ll_all;
        TextView tv_id,tv_third,tv_mchcode;
        public ViewHolder(View itemView) {
            super(itemView);
            ll_all =(LinearLayout) itemView.findViewById(R.id.ll_all);
            tv_id =(TextView) itemView.findViewById(R.id.tv_id);
            tv_third =(TextView) itemView.findViewById(R.id.tv_third);
            tv_mchcode =(TextView) itemView.findViewById(R.id.tv_mchcode);
            card = itemView;
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnLpTouchListener.onTouch(view, card, mLpEntity);
        }
    }
}
