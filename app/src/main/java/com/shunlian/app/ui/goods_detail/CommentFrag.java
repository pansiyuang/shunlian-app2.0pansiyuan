package com.shunlian.app.ui.goods_detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommentAdapter;
import com.shunlian.app.presenter.GoodsDetailPresenter;
import com.shunlian.app.ui.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/21.
 * 评价
 */

public class CommentFrag extends BaseFragment {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    private LinearLayoutManager manager;
    private GoodsDetailPresenter goodsDetailPresenter;
    private String goodsId;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View frag_comment = LayoutInflater.from(baseActivity).inflate(R.layout.frag_comment,
                container, false);
        return frag_comment;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()){
                        if (goodsDetailPresenter != null){
                            goodsDetailPresenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        goodsId = getArguments().getString("goodsId");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                recy_view.getLayoutParams();
        layoutParams.topMargin = ((GoodsDetailAct)baseActivity).offset;
        recy_view.setLayoutParams(layoutParams);
        manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
    }

    public void setPresenter(GoodsDetailPresenter goodsDetailPresenter, String id) {
        this.goodsDetailPresenter = goodsDetailPresenter;
        if (recy_view != null)
            recy_view.scrollToPosition(0);
    }

    public void setCommentAdapter(CommentAdapter adapter) {
        if (recy_view != null){
            recy_view.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        RecyclerView.Adapter adapter = recy_view.getAdapter();
        if (adapter != null){
            adapter.onDetachedFromRecyclerView(recy_view);
            adapter = null;
        }
        super.onDestroyView();
    }
}
