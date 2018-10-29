package com.shunlian.app.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.TransformUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/10/26.
 */

public class CommBottomDialog extends Dialog implements BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private OnItemClickCallBack mCallBack;
    private Unbinder bind;
    private LinearLayoutManager manager;
    private List<String> stringList;
    private SingTextAdapter mAdapter;

    public CommBottomDialog(Context context) {
        this(context, R.style.popAd);
        initView();
    }

    public CommBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        //设置当前dialog宽高
//        Window win = getWindow();
//        WindowManager.LayoutParams lp = win.getAttributes();
////        lp.width = DeviceInfoUtil.getDeviceWidth(getContext()) - TransformUtil.dip2px(getContext(), 32);
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
////        lp.gravity = Gravity.BOTTOM;
//        win.setAttributes(lp);
//        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_dialog, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);

        manager = new LinearLayoutManager(getContext());
        recycler_list.setLayoutManager(manager);
        stringList = new ArrayList<>();
    }

    public void setRecyclerList(List<String> list) {
        stringList.clear();
        if (list != null && list.size() != 0) {
            stringList.addAll(list);
        }
        stringList.add("取消");
        if (mAdapter == null) {
            mAdapter = new SingTextAdapter(getContext(), stringList);
            recycler_list.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setOnItemClickCallBack(OnItemClickCallBack callBack) {
        mCallBack = callBack;
    }

    public interface OnItemClickCallBack {
        void clickItem(String string);
    }

    public void destory() {
        if (isShowing()) {
            dismiss();
        }
        if (bind != null) {
            bind.unbind();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mCallBack != null) {
            String s = stringList.get(position);
            if ("取消".equals(s) && isShowing()) {
                dismiss();
                return;
            }
            mCallBack.clickItem(s);
        }
    }

    public class SingTextAdapter extends BaseRecyclerAdapter<String> {

        public SingTextAdapter(Context context, List<String> lists) {
            super(context, false, lists);
        }

        @Override
        protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
            return new SingleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_botton_dialog, parent, false));
        }

        @Override
        public void handleList(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SingleViewHolder) {
                SingleViewHolder viewHolder = (SingleViewHolder) holder;
                String string = lists.get(position);
                viewHolder.tv_item.setText(string);
            }
        }

        public class SingleViewHolder extends BaseRecyclerViewHolder {

            @BindView(R.id.tv_item)
            TextView tv_item;

            public SingleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
