package com.shunlian.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shunlian.app.R;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/6.
 */

public class SortFragAdapter extends BaseAdapter {

    private Context mContext;
    private List<SortFragEntity.Toplist> toplists;
    public int currentPosition;//当前位置

    public SortFragAdapter(Context context, List<SortFragEntity.Toplist> toplists) {
        this.mContext = context;
        this.toplists = toplists;
    }

    @Override
    public int getCount() {
        return toplists.size();
    }


    @Override
    public SortFragEntity.Toplist getItem(int position) {
        return toplists.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SortFragHolder mHolder = null;
        if (convertView == null) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_sort_left, parent, false);
            mHolder = new SortFragHolder(view);
        } else {
            mHolder = (SortFragHolder) convertView.getTag();
        }

        SortFragEntity.Toplist item = getItem(position);
        mHolder.mtv_name.setText(item.name);
        if (position == currentPosition) {
            mHolder.view_line.setVisibility(View.VISIBLE);
            mHolder.mtv_name.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            mHolder.mtv_name.setTextColor(mContext.getResources().getColor(R.color.pink_color));
        } else {
            mHolder.view_line.setVisibility(View.INVISIBLE);
            mHolder.mtv_name.setTextColor(mContext.getResources().getColor(R.color.my_dark_grey));
            mHolder.mtv_name.setBackgroundColor(mContext.getResources().getColor(R.color.white_ash));
        }

        return mHolder.getItemView();
    }

    public class SortFragHolder {

        private View itemView;

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        public SortFragHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }

        public View getItemView() {
            return itemView;
        }
    }
}
