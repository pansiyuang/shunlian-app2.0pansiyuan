package com.shunlian.app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.TransformUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/1/10.
 */

public class CategorySortPopWindow extends PopupWindow {
    private Context mContext;
    private View contentview;
    private View grayView;
    private int currentPosition;
    private RecyclerView recyclerView;
    private OnSortSelectListener listener;
    private List<String> arrayList = new ArrayList<>();

    public CategorySortPopWindow(Context context) {
        this.mContext = context;
        contentview = LayoutInflater.from(mContext).inflate(R.layout.layout_recycler, null);
        setContentView(contentview);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setWidth(DeviceInfoUtil.getDeviceWidth(mContext));
        int height = DeviceInfoUtil.getDeviceHeight(mContext);
        setHeight(height - TransformUtil.dip2px(mContext, 84.5f));

        recyclerView = (RecyclerView) contentview.findViewById(R.id.recycler_list);
        grayView = contentview.findViewById(R.id.view_gray);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        grayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void initData(int index) {
        arrayList.clear();
        arrayList.add("综合排序");
        arrayList.add("价格(从高到低)");
        arrayList.add("价格(从低到高)");
        arrayList.add("人气");

        currentPosition = index;

        final SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<String>(mContext, R.layout.item_category_sort, arrayList) {
            @Override
            public void convert(SimpleViewHolder holder, String string, final int position) {
                TextView tv_str = holder.getView(R.id.tv_content);
                tv_str.setText(string);
                MyImageView miv_select = holder.getView(R.id.miv_select);
                RelativeLayout rl_item = holder.getView(R.id.rl_item);
                rl_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.OnItemSelect(position, arrayList.get(position));
                            dismiss();
                        }
                    }
                });
                if (currentPosition == position) {
                    tv_str.setTextColor(mContext.getResources().getColor(R.color.pink_color));
                    miv_select.setVisibility(View.VISIBLE);
                } else {
                    tv_str.setTextColor(mContext.getResources().getColor(R.color.new_text));
                    miv_select.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void setOnSortSelectListener(OnSortSelectListener l) {
        this.listener = l;
    }

    public interface OnSortSelectListener {
        void OnItemSelect(int position, String str);
    }

}
