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
import com.shunlian.app.widget.FootprintDialog;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDeatilFrag extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.recy_view_root)
    RecyclerView recy_view_root;

    @BindView(R.id.miv_footprint)
    MyImageView miv_footprint;
    private LinearLayoutManager manager;
    private int totalDy;
    private FootprintDialog footprintDialog;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_goods_details, null);
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_footprint.setOnClickListener(this);
        recy_view_root.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
                if (manager != null){
                    int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                    GoodsDetailAct detailAct = (GoodsDetailAct) baseActivity;
                    detailAct.setBgColor(firstVisibleItemPosition,totalDy);
                    if (firstVisibleItemPosition < 3){
                        detailAct.setTabBarStatue(GoodsDetailAct.GOODS_ID);
                    }else if (firstVisibleItemPosition < 6){
                        detailAct.setTabBarStatue(GoodsDetailAct.COMMENT_ID);
                    }else {
                        detailAct.setTabBarStatue(GoodsDetailAct.DETAIL_ID);
                    }
                }
            }
        });
    }

    public void setScrollPosition(int statue,int offset){
        //statue == 0 商品
        //statue == 1 评价
        //statue == 2 详情
        System.out.println(">>>>>>>>>>>>>>"+offset);
        if (statue == 0){
            totalDy = 0;
            manager.scrollToPositionWithOffset(0,0);
            manager.scrollToPositionWithOffset(0,offset);
        }else if (statue == 1){
            manager.scrollToPositionWithOffset(4,0);
            manager.scrollToPositionWithOffset(4,offset);
        }else{
            manager.scrollToPositionWithOffset(6,0);
            manager.scrollToPositionWithOffset(6,offset);
        }
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
        switch (v.getId()){
            case R.id.miv_footprint:
                if (footprintDialog == null) {
                    footprintDialog = new FootprintDialog(baseActivity);
                }
                footprintDialog.show();
                break;
        }
    }


    private void setState(int state){
    }
}
