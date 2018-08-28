package com.shunlian.app.ui.collection;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/22.
 */

public class MyCollectionAct extends BaseActivity {

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_manage)
    MyTextView mtv_manage;
    //商品
    @BindView(R.id.mtv_goods)
    MyTextView mtv_goods;

    @BindView(R.id.view_goods)
    View view_goods;
    //店铺
    @BindView(R.id.mtv_store)
    MyTextView mtv_store;

    @BindView(R.id.view_store)
    View view_store;
    //足迹
    @BindView(R.id.mtv_footprint)
    MyTextView mtv_footprint;

    @BindView(R.id.view_footprint)
    View view_footprint;
    //内容
    @BindView(R.id.mtv_content)
    MyTextView mtv_content;

    @BindView(R.id.view_content)
    View view_content;

    @BindView(R.id.mrlayout_manage)
    MyRelativeLayout mrlayout_manage;

    @BindView(R.id.miv_all_select)
    MyImageView miv_all_select;

    @BindView(R.id.mtv_all_select)
    MyTextView mtv_all_select;

    @BindView(R.id.mtv_delete)
    MyTextView mtv_delete;

    @BindView(R.id.miv_search)
    MyImageView miv_search;

    private int new_text;
    private int pink_color;
    private Map<String,CollectionFrag> fragments;
    public static final String GOODS_FLAG = "goods_flag";
    public static final String STORE_FLAG = "store_flag";
    public static final String FOOTPRINT_FLAG = "footprint_flag";
    public static final String CONTENT_FLAG = "content_flag";
    private CollectionGoodsFrag collectionGoodsFrag;
    private CollectionStoreFrag collectionStoreFrag;
    private FootprintFrag footprintFrag;
    private CollectionContentFrag collectionContentFrag;
    private String currentFrag = GOODS_FLAG;//当前frag
    private boolean isSelectAll = false;//选择全部
    private int color_value_6c;

    public static void startAct(Context context,String flag){
        Intent intent = new Intent(context, MyCollectionAct.class);
        intent.putExtra("flag",flag);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_my_collection;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        int i = TransformUtil.dip2px(this, 20);
        TransformUtil.expandViewTouchDelegate(miv_all_select,i,i,i,i);
        pink_color = getColorResouce(R.color.pink_color);
        new_text = getColorResouce(R.color.new_text);
        color_value_6c = getColorResouce(R.color.color_value_6c);
        fragments = new HashMap<>();
        String flag = getIntent().getStringExtra("flag");
        switch (flag == null ? "" : flag){
            case GOODS_FLAG:
                collectionGoods();
                break;
            case STORE_FLAG:
                collectionStore();
                break;
            case FOOTPRINT_FLAG:
                collectionFootprint();
                break;
            case CONTENT_FLAG:
                collectionContent();
                break;
            default:
                collectionGoods();
                break;
        }
    }

    /**
     * 商品
     */
    public void goodsFrag(){
        if (collectionGoodsFrag == null) {
            collectionGoodsFrag = new CollectionGoodsFrag();
            fragments.put(GOODS_FLAG, collectionGoodsFrag);
        }else {
            collectionGoodsFrag = (CollectionGoodsFrag) fragments.get(GOODS_FLAG);
        }
        visible(miv_search);
        switchContent(collectionGoodsFrag);
    }


    /**
     * 店铺
     */
    public void storeFrag(){
        if (collectionStoreFrag == null) {
            collectionStoreFrag = new CollectionStoreFrag();
            fragments.put(STORE_FLAG, collectionStoreFrag);
        }else {
            collectionStoreFrag = (CollectionStoreFrag) fragments.get(STORE_FLAG);
        }
        visible(miv_search);
        switchContent(collectionStoreFrag);
    }

    /**
     * 足迹
     */
    public void footprintFrag(){
        if (footprintFrag == null) {
            footprintFrag = new FootprintFrag();
            fragments.put(FOOTPRINT_FLAG, footprintFrag);
        }else {
            footprintFrag = (FootprintFrag) fragments.get(FOOTPRINT_FLAG);
        }
        gone(miv_search);
        switchContent(footprintFrag);
    }

    /**
     * 内容
     */
    public void contentFrag(){
        if (collectionContentFrag == null) {
            collectionContentFrag = new CollectionContentFrag();
            fragments.put(CONTENT_FLAG, collectionContentFrag);
        }else {
            collectionContentFrag = (CollectionContentFrag) fragments.get(CONTENT_FLAG);
        }
        gone(miv_search);
        switchContent(collectionContentFrag);
    }

    /*
    替换fragment内容
     */
    public void switchContent(Fragment show) {
        if (show != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!show.isAdded()) {
                ft.add(R.id.flayout_content, show);
            } else {
                ft.show(show);
            }

            if (fragments != null && fragments.size() > 0) {
                Iterator<String> keys = fragments.keySet().iterator();
                while (keys.hasNext()){
                    String next = keys.next();
                    CollectionFrag collectionFrag = fragments.get(next);
                    if (show != collectionFrag) {
                        if (collectionFrag != null && collectionFrag.isVisible()) {
                            ft.hide(collectionFrag);
                            recoveryManage(collectionFrag);
                            setDeleteBackgroundColor(false);
                            isSelectAll = false;
                            collectionFrag.operationMange();
                        }
                    }
                }
            }
//            ft.commit();
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * 删除
     */
    @OnClick(R.id.mtv_delete)
    public void deleteManage(){
        CollectionFrag collectionFrag = fragments.get(currentFrag);
        collectionFrag.delete();
    }

    @OnClick({R.id.miv_all_select,R.id.mtv_all_select})
    public void allSelect(){
        CollectionFrag collectionFrag = fragments.get(currentFrag);
        collectionFrag.selectAll();
        if (!isSelectAll) {
            miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
            mtv_delete.setBackgroundColor(pink_color);
            mtv_delete.setClickable(true);
        }else {
            miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
            mtv_delete.setBackgroundColor(color_value_6c);
            mtv_delete.setClickable(false);
        }
        isSelectAll = !isSelectAll;
    }

    @OnClick(R.id.miv_search)
    public void clickSearch(){
        String flag = null;
        if (currentFrag == GOODS_FLAG) {
            flag = "goods";
        }else {
            flag = "shop";
        }
        SearchGoodsActivity.startActivityForResult(this, false, flag);
    }

    /**
     * 设置删除背景
     * @param isLight 部分选择
     */
    public void setDeleteBackgroundColor(boolean isLight){
        if (isLight){
            mtv_delete.setBackgroundColor(pink_color);
            mtv_delete.setClickable(true);
        }else {
            mtv_delete.setBackgroundColor(color_value_6c);
            mtv_delete.setClickable(false);
            miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
        }
    }

    /**
     * 设置管理选择状态
     * @param state 0 全选  1 部分选择 2 全不选
     */
    public void setManageState(int state){
        LogUtil.httpLogW("setManageState:" + state);
        mtv_delete.setClickable(true);
        if (state == 0){
            mtv_delete.setBackgroundColor(pink_color);
            miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
            isSelectAll = true;
        }else if (1 == state){
            mtv_delete.setBackgroundColor(pink_color);
            miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
            isSelectAll = false;
        }else {
            mtv_delete.setClickable(false);
            mtv_delete.setBackgroundColor(color_value_6c);
            miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
            isSelectAll = false;
        }
    }

    @OnClick(R.id.mtv_manage)
    public void manage(){
        String s = mtv_manage.getText().toString();
        CollectionFrag collectionFrag = fragments.get(currentFrag);
        if (collectionFrag.isClickManage()) {
            if (s.equals(getStringResouce(R.string.manage))) {
                mtv_manage.setText(getStringResouce(R.string.RegisterTwoAct_finish));
                mrlayout_manage.setVisibility(View.VISIBLE);
            } else {
                recoveryManage(collectionFrag);
            }
        }
    }

    /**
     *将操作状态恢复为正常状态
     * @param collectionFrag
     */
    public void recoveryManage(CollectionFrag collectionFrag) {
        collectionFrag.finishManage();
        mtv_manage.setText(getStringResouce(R.string.manage));
        mrlayout_manage.setVisibility(View.GONE);
        setManageState(2);
    }

    /**
     * 收藏商品
     */
    @OnClick(R.id.mllayout_goods)
    public void collectionGoods(){
        mtv_title.setText(getStringResouce(R.string.goods_collection));
        currentFrag = GOODS_FLAG;
        showSataus(0);
        goodsFrag();
    }

    /**
     * 收藏店铺
     */
    @OnClick(R.id.mrlayout_store)
    public void collectionStore(){
        mtv_title.setText(getStringResouce(R.string.store_collection));
        currentFrag = STORE_FLAG;
        showSataus(1);
        storeFrag();
    }

    /**
     * 足迹
     */
    @OnClick(R.id.mrlayout_footprint)
    public void collectionFootprint(){
        mtv_title.setText(getStringResouce(R.string.my_footprint));
        currentFrag = FOOTPRINT_FLAG;
        showSataus(2);
        footprintFrag();
    }

    /**
     * 内容
     */
    @OnClick(R.id.mrlayout_content)
    public void collectionContent(){
        mtv_title.setText(getStringResouce(R.string.content_collection));
        currentFrag = CONTENT_FLAG;
        showSataus(3);
        contentFrag();
    }

    private void showSataus(int status){
        //商品
        mtv_goods.setTextColor(status == 0 ? pink_color : new_text);
        view_goods.setVisibility(status == 0 ? View.VISIBLE : View.INVISIBLE);
        //店铺
        mtv_store.setTextColor(status == 1 ? pink_color : new_text);
        view_store.setVisibility(status == 1 ? View.VISIBLE : View.INVISIBLE);
        //足迹
        mtv_footprint.setTextColor(status == 2 ? pink_color : new_text);
        view_footprint.setVisibility(status == 2 ? View.VISIBLE : View.INVISIBLE);
        //内容
        mtv_content.setTextColor(status == 3 ? pink_color : new_text);
        view_content.setVisibility(status == 3 ? View.VISIBLE : View.INVISIBLE);
    }
}
