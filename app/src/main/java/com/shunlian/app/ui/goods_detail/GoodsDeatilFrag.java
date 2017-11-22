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
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.widget.FootprintDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.banner.Kanner;

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
    private int screenWidth;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_goods_details, null);
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_footprint.setOnClickListener(this);
        recy_view_root.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int[] detail = new int[2];
            int[] comment = new int[2];
            GoodsDetailAct detailAct = (GoodsDetailAct) baseActivity;
            int offset = detailAct.offset;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    View firstView = manager.findViewByPosition(firstPosition);
                    if (firstView instanceof Kanner){
                        totalDy += dy;
                        detailAct.setBgColor(firstPosition,totalDy);
                    }else {
                        detailAct.setToolbar();
                        totalDy = screenWidth;
                    }
                    System.out.println("dy=="+dy+"  totalDy==="+totalDy);
                    View viewComment = manager.findViewByPosition(4);
                    if (viewComment != null) {
                        comment[1] = 0;
                        viewComment.getLocationInWindow(comment);
                    }
                    View viewDetail = manager.findViewByPosition(6);
                    if (viewDetail != null) {
                        detail[1] = 0;
                        viewDetail.getLocationInWindow(detail);
                    }
//                    System.out.println("detail=="+detail[1]+"  comment==="+comment[1]);
                    if (iscCallScrollPosition){
                        iscCallScrollPosition = false;
                        return;
                    }else {
                        if ((comment[1] < 0 && detail[1] < offset) || firstPosition >= 6) {
                            detailAct.setTabBarStatue(GoodsDetailAct.DETAIL_ID);
                        } else if ((detail[1] > offset && comment[1] < offset) || firstPosition >= 4) {
                            detailAct.setTabBarStatue(GoodsDetailAct.COMMENT_ID);
                        } else {
                            detailAct.setTabBarStatue(GoodsDetailAct.GOODS_ID);
                        }
                    }
                }
            }
        });
    }

    private boolean iscCallScrollPosition = false;

    public void setScrollPosition(int statue,int offset){
        //statue == 0 商品
        //statue == 1 评价
        //statue == 2 详情
        iscCallScrollPosition = true;
        if (statue == 0){
            totalDy = 0;
            manager.scrollToPositionWithOffset(0,0);
            manager.scrollToPositionWithOffset(0,offset);
        }else if (statue == 1){
            totalDy = screenWidth;
            manager.scrollToPositionWithOffset(4,0);
            manager.scrollToPositionWithOffset(4,offset);
        }else{
            totalDy = screenWidth;
            manager.scrollToPositionWithOffset(6,0);
            manager.scrollToPositionWithOffset(6,offset);
        }
    }

    @Override
    protected void initData() {
        screenWidth = DeviceInfoUtil.getDeviceWidth(baseActivity);
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


}
