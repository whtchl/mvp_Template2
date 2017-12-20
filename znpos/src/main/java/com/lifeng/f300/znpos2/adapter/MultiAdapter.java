package com.lifeng.f300.znpos2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.f300.znpos2.R;

import com.lifeng.f300.znpos2.view.RecycleViewItemData;

import java.util.ArrayList;
import com.lifeng.f300.znpos2.adapter.item.*;

/**
 * Created by happen on 2017/9/18.
 */

public class MultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_LINE = 0;              //分割线
    private static final int TYPE_TITLE = 1;             //主标题
    private static final int TYPE_SUBTITLE = 2;          //子标题
    private static final int TYPE_IMG = 3;               //签名突破
    private static final int TYPE_CONTENT = 4;           //内容
    private static final int TYPE_COLOR_TYPE = 5;        //字体颜色


    private ArrayList<RecycleViewItemData> dataList;     //数据集合

    public MultiAdapter(ArrayList<RecycleViewItemData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果viewType是分割线，创建LineViewHolder型的viewholder
        if (viewType == TYPE_LINE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line, parent, false);
            LineViewHolder viewHolder = new LineViewHolder(view);
            return viewHolder;
        }
        //如果viewType是主标题，创建TitleViewHolder型的viewholder
        if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            TitleViewHolder viewHolder = new TitleViewHolder(view);
            return viewHolder;
        }

        //如果viewType是分割线，创建SubTitleViewHolder型的viewholder
        if (viewType == TYPE_SUBTITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtitle, parent, false);
            SubTitleViewHolder viewHolder = new SubTitleViewHolder(view);
            return viewHolder;
        }

        //如果viewType是分割线，创建ContentViewHolder型的viewholder
        if (viewType == TYPE_CONTENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent, false);
            ContentViewHolder viewHolder = new ContentViewHolder(view);
            return viewHolder;
        }

        //如果viewType是分割线，创建ColorTypeViewHolder型的viewholder
        if (viewType == TYPE_COLOR_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
            ColorTypeViewHolder viewHolder = new ColorTypeViewHolder(view);
            return viewHolder;
        }



        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //如果是holder是LineViewHolder的实例
        if (holder instanceof LineViewHolder) {
            JUtils.Log("this is LineViewHolder");
            LineItem mLineItem = (LineItem) dataList.get(position).getT();
            ((LineViewHolder) holder).mTextView1.setText(mLineItem.getText1());
        }
        //如果是holder是TitleViewHolder的实例
        if (holder instanceof TitleViewHolder) {
            TitleItem mTitleItem = (TitleItem) dataList.get(position).getT();
            JUtils.Log("this is TitleViewHolder");
            ((TitleViewHolder) holder).mTextView1.setText(mTitleItem.getText1());
        }
        //如果是holder是SubTitleViewHolder的实例
        if (holder instanceof SubTitleViewHolder) {
            JUtils.Log("this is SubTitleViewHolder");
            SubTitleItem mSubTitleItem = (SubTitleItem) dataList.get(position).getT();
            ((SubTitleViewHolder) holder).mTextView1.setText(mSubTitleItem.getText1());
        }
        //如果是holder是ContentViewHolder的实例
        if (holder instanceof ContentViewHolder) {
            JUtils.Log("this is ContentViewHolder");
            ContentItem mContentItem = (ContentItem) dataList.get(position).getT();
            ((ContentViewHolder) holder).mTextView1.setText(mContentItem.getText1());
            ((ContentViewHolder) holder).mTextView2.setText(mContentItem.getText2());
        }
        //如果是holder是ColorTypeViewHolder的实例
        if (holder instanceof ColorTypeViewHolder) {
            JUtils.Log("this is ColorTypeViewHolder");
            ColorItem mColorItem = (ColorItem) dataList.get(position).getT();
            ((ColorTypeViewHolder) holder).mTextView1.setText(mColorItem.getText1());
            ((ColorTypeViewHolder) holder).mTextView2.setText(mColorItem.getText2());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == dataList.get(position).getDataType()) {
            return TYPE_LINE;
        } else if (1 == dataList.get(position).getDataType()) {
            return TYPE_TITLE;
        } else if (2 == dataList.get(position).getDataType()) {
            return TYPE_SUBTITLE;
        } else if (3 == dataList.get(position).getDataType()) {
            return TYPE_IMG;
        } else if (4 == dataList.get(position).getDataType()) {
            return TYPE_CONTENT;
        }else if (5 == dataList.get(position).getDataType()) {
            return TYPE_COLOR_TYPE;
        } else {
            return 0;
        }
    }

    public class LineViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;

        public LineViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.line_text1);
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;

        public TitleViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.title_text1);
        }
    }

    public class SubTitleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;

        public SubTitleViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.subtitle_text1);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public ContentViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.content_text1);
            mTextView2 = (TextView) itemView.findViewById(R.id.content_text2);
        }
    }


    public class ColorTypeViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public ColorTypeViewHolder(View itemView) {
            super(itemView);
            mTextView1 = (TextView) itemView.findViewById(R.id.content_text1);
            mTextView2 = (TextView) itemView.findViewById(R.id.content_text2);
        }
    }
}
