package com.shunlian.app.ui.goods_detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.GoodsDetailAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.SmallVideoPlayer;
import com.shunlian.app.widget.VideoBannerWrapper;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import org.greenrobot.eventbus.EventBus;

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

    @BindView(R.id.miv_top)
    MyImageView miv_top;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private LinearLayoutManager manager;
    private int totalDy;
    private int screenWidth;
    private GoodsDetailAdapter goodsDetailAdapter;
    public int currentFirstItem;//当前第一个条目
    public static boolean isShowNetTip;//是否提示网络

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_goods_details, null);
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_top.setOnClickListener(this);
        miv_footprint.setOnClickListener(this);
        recy_view_root.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private DefMessageEvent event;
            int[] detail = new int[2];
            int[] comment = new int[2];
            GoodsDetailAct detailAct = (GoodsDetailAct) baseActivity;
            int offset = detailAct.toolbarHeight;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    currentFirstItem = firstPosition;
                    if (event == null)
                        event = new DefMessageEvent();
                    event.itemPosition = firstPosition;
                    EventBus.getDefault().post(event);
                    if (firstPosition > 2){
                        miv_top.setVisibility(View.VISIBLE);
                    }else {
                        miv_top.setVisibility(View.INVISIBLE);
                    }
                    View firstView = manager.findViewByPosition(firstPosition);
                    if (firstView instanceof VideoBannerWrapper){
                        totalDy += dy;
                        detailAct.setBgColor(firstPosition,totalDy);
                    }else {
                        detailAct.setToolbar();
                        totalDy = screenWidth;
                    }
//                    System.out.println("dy=="+dy+"  totalDy==="+totalDy);
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


        recy_view_root.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                SmallVideoPlayer.onChildViewAttachedToWindow(view, R.id.customVideoPlayer);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                SmallVideoPlayer.onChildViewDetachedFromWindow(view);
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
        int netWorkStatus = NetworkUtils.getNetWorkStatus(baseActivity);
        if (netWorkStatus != NetworkUtils.NETWORK_WIFI &&
                netWorkStatus != NetworkUtils.NETWORK_CLASS_UNKNOWN){
            isShowNetTip = true;
        }
    }

    /**
     * 商品详情数据
     */
    public void setGoodsDetailData(GoodsDeatilEntity goodsDeatilEntity) {
        gone(nei_empty);
        manager = new LinearLayoutManager(baseActivity);
        recy_view_root.setLayoutManager(manager);
        recy_view_root.setNestedScrollingEnabled(false);

        GoodsDeatilEntity.Detail detail = goodsDeatilEntity.detail;
        ArrayList<String> pics = null;
        if (detail != null && detail.pics != null){
            pics = detail.pics;
        }else {
            pics = new ArrayList<>();
        }

        goodsDetailAdapter = new GoodsDetailAdapter(baseActivity,goodsDeatilEntity,pics);
        recy_view_root.setAdapter(goodsDetailAdapter);
        recy_view_root.setNestedScrollingEnabled(false);

    }

    /**
     * 显示属性选择框
     */
    public void showParamDialog(){
        if (goodsDetailAdapter != null){
            ParamDialog paramDialog = goodsDetailAdapter.paramDialog;
            if (paramDialog != null){
                paramDialog.show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        GoodsDetailAct goodsDetailAct = (GoodsDetailAct) baseActivity;
        switch (v.getId()){
            case R.id.miv_footprint:
                goodsDetailAct.showFootprintList();
                break;
            case R.id.miv_top:
                if (miv_top.getVisibility() == View.INVISIBLE){
                    return;
                }
                if (recy_view_root != null){
                    goodsDetailAct.listTop();
                }
                break;
        }
    }


    public void refreshVoucherState(GoodsDeatilEntity.Voucher voucher) {
        if (goodsDetailAdapter != null){
            goodsDetailAdapter.refreshVoucherState(voucher);
        }
    }

    @Override
    public void onDestroyView() {

        DefMessageEvent event = new DefMessageEvent();
        event.isrelease = true;
        EventBus.getDefault().post(event);
        if (goodsDetailAdapter != null){
            goodsDetailAdapter.onDetachedFromRecyclerView(recy_view_root);
            goodsDetailAdapter.unbind();
            goodsDetailAdapter = null;
        }
        super.onDestroyView();
    }

    public void onFailure(){
        if (nei_empty != null){
            visible(nei_empty);
            nei_empty.setNetExecption().setOnClickListener(v ->{
                if (MyOnClickListener.isFastClick()) {
                    return;
                }
                ((GoodsDetailAct)baseActivity).refreshDetail();
            });
        }
    }
}
