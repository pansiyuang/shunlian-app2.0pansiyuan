package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SuperProductAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.SuperProductEntity;
import com.shunlian.app.presenter.SuperproductPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.JosnSensorsDataAPI;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.ISuperProductView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/24.
 */

public class SuperProductsAct extends BaseActivity implements ISuperProductView, SuperProductAdapter.OnShareClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    public List<SuperProductEntity.SuperProduct> mData;
    private SuperproductPresenter mPresenter;
    private LinearLayoutManager manager;
    private SuperProductAdapter mAdapter;
    private ShareInfoParam mInfoParam;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SuperProductsAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_superior_products;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.super_product));

        mPresenter = new SuperproductPresenter(this, this);
        mPresenter.getProductList();

        mData = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
        recycler_list.setNestedScrollingEnabled(false);

    }

    @Override
    public void getProductList(List<SuperProductEntity.SuperProduct> list) {
        if (!isEmpty(list)) {
            mData.clear();
            mData.addAll(list);
        }

        if (mAdapter == null) {
            mAdapter = new SuperProductAdapter(this, mData);
            mAdapter.setOnShareClickListener(this);
            recycler_list.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    JosnSensorsDataAPI.youpinGoodClick(mData.get(position).url.type,mData.get(position).url.item_id,mData.get(position).title,position);
                    Common.goGoGo(baseAct,mData.get(position).url.type,mData.get(position).url.item_id);
                }
            });
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onShare(ShareInfoParam infoParam,String id) {
        infoParam.goods_id = id;
        mInfoParam = infoParam;
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(this,"login");
            return;
        }
        if (isEmpty(infoParam.shareLink) && mPresenter != null){
            mPresenter.getShareInfo(mPresenter.goods,id);
            return;
        }
        share(infoParam);
    }

    private void share(ShareInfoParam infoParam) {
        if (quick_actions != null) {
            visible(quick_actions);
            quick_actions.shareInfo(infoParam);
            quick_actions.shareStyle2Dialog(true, 3);
        }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        mInfoParam.shareLink = baseEntity.data.shareLink;
        mInfoParam.userName = baseEntity.data.userName;
        mInfoParam.userAvatar = baseEntity.data.userAvatar;
        share(mInfoParam);
    }
}
