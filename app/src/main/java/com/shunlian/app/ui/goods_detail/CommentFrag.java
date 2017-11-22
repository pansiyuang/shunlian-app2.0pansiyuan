package com.shunlian.app.ui.goods_detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommentAdapter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/21.
 * 评价
 */

public class CommentFrag extends BaseFragment {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View frag_comment = LayoutInflater.from(baseActivity).inflate(R.layout.frag_comment, container, false);
        return frag_comment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recy_view.getLayoutParams();
        layoutParams.topMargin = ((GoodsDetailAct)baseActivity).offset - ImmersionBar.getStatusBarHeight(baseActivity);
        recy_view.setLayoutParams(layoutParams);
        List<String> iten = DataUtil.getListString(10, "iten");
        LinearLayoutManager manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
        CommentAdapter commentAdapter = new CommentAdapter(baseActivity,false,iten);
        recy_view.setAdapter(commentAdapter);
    }
}
