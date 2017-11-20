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
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDeatilFrag extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.recy_view_root)
    RecyclerView recy_view_root;
    private LinearLayoutManager manager;

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
                    System.out.println("firstVisibleItemPosition="+firstVisibleItemPosition);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 轮播
     * @param pics
     */
    public void setBanner(ArrayList<String> pics) {

    }

    /**
     * 商品的标题和价格
     * 是否包邮
     * 销售量
     * 发货地点
     */
    public void goodsInfo(String title, String price, String market_price, String free_shipping, String sales, String address) {
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
    /**
     *
     * @param is_new 是否新品
     * @param is_explosion 是否爆款
     * @param is_hot 是否热卖
     * @param is_recommend 是否推荐
     */
    public void smallLabel(String is_new, String is_explosion, String is_hot, String is_recommend) {

    }


    @Override
    public void onClick(View v) {
    }





    /**
     * 优惠券
     * @param vouchers
     */
    public void setVoucher(ArrayList<GoodsDeatilEntity.Voucher> vouchers) {

    }

    /**
     * 店铺信息
     * @param infos
     */
    public void setShopInfo(GoodsDeatilEntity.StoreInfo infos) {
    }

    /**
     * 套餐列表
     * @param combos
     */
    public void setComboDetail(List<GoodsDeatilEntity.Combo> combos) {

    }

    /**
     * 商品参数列表
     * @param attrses
     */
    public void setGoodsParameter(List<GoodsDeatilEntity.Attrs> attrses) {

    }

    /**
     * 评价列表
     * @param commentses
     */
    public void setCommentList(List<GoodsDeatilEntity.Comments> commentses) {
    }

    /**
     * 商品详情
     * @param detail
     */
    public void setDetail(GoodsDeatilEntity.Detail detail) {
    }



}
