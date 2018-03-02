package com.shunlian.app.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.listener.OnItemClickListener;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.category.BrandListAct;
import com.shunlian.app.ui.my_comment.MyCommentAct;
import com.shunlian.app.ui.myself_store.GoodsSearchAct;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 发现页面
 */

public class DiscoverFrag extends BaseFragment {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_discover, null, false);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ImmersionBar.with(this).fitsSystemWindows(true)
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        List<String> strings = new ArrayList<>();
        strings.add("我的评价");
        strings.add("品牌列表");
        strings.add("搜索列表");

        LinearLayoutManager manager = new LinearLayoutManager(baseContext);
        recy_view.setLayoutManager(manager);

        SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter<String>
                (baseActivity, android.R.layout.simple_list_item_1, strings) {

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {
                holder.addOnClickListener(android.R.id.text1);
                TextView textView = holder.getView(android.R.id.text1);
                textView.setText(s);
            }
        };

        recy_view.setAdapter(simpleRecyclerAdapter);

        simpleRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onClick(view, position);
            }
        });
    }

    private void onClick(View view, int position) {
        switch (position) {
            case 0:
                MyCommentAct.startAct(baseActivity);
                break;
            case 1:
                BrandListAct.startAct(baseActivity,666);
                break;
            case 2:
                GoodsSearchParam param = new GoodsSearchParam();
                param.keyword="衣服";
                GoodsSearchAct.startAct(baseActivity,param);
                break;
        }
    }
}
