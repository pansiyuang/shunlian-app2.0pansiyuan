package com.shunlian.app.ui.goods_detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.GoodsDetailAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDeatilFrag extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.recy_view_root)
    RecyclerView recy_view_root;
    private LinearLayoutManager manager;
    private int dt;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_goods_details, null);
    }

    @Override
    protected void initListener() {
        super.initListener();

        recy_view_root.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                    GoodsDetailAct detailAct = (GoodsDetailAct) baseActivity;
                    dt += dy;
                    detailAct.setBgColor(firstVisibleItemPosition,dt);

                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 商品详情数据
     */
    public void setGoodsDetailData(GoodsDeatilEntity goodsDeatilEntity) {


        manager = new LinearLayoutManager(baseActivity);
        recy_view_root.setLayoutManager(manager);
        recy_view_root.setNestedScrollingEnabled(false);
        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        recy_view_root.setRecycledViewPool(pool);
        pool.setMaxRecycledViews(0,5);

        ArrayList<String> pics = goodsDeatilEntity.detail.pics;
        recy_view_root.setAdapter(new GoodsDetailAdapter(baseActivity, false,goodsDeatilEntity,pics));

    }


    @Override
    public void onClick(View v) {
    }


    private void setState(int state){
    }
}
