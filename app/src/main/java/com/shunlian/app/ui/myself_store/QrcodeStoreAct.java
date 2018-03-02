package com.shunlian.app.ui.myself_store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.QrcodeStoreAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.PersonShopEntity;
import com.shunlian.app.presenter.PersonShopPresent;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPersonShopView;
import com.shunlian.app.view.IPersonStoreView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/2.
 */

public class QrcodeStoreAct extends BaseActivity implements IPersonShopView, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.miv_icon)
    CircleImageView miv_icon;

    @BindView(R.id.miv_level)
    MyImageView miv_level;

    @BindView(R.id.miv_honour)
    MyImageView miv_honour;

    @BindView(R.id.tv_store_name)
    TextView tv_store_name;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private QrcodeStoreAdapter mAdapter;
    private PersonShopPresent mPresenter;
    private List<GoodsDeatilEntity.Goods> goodsList;
    private String currentMemberId;

    public static void startAct(Context context, String memberId) {
        Intent intent = new Intent(context, QrcodeStoreAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_qrcodestore;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        currentMemberId = getIntent().getStringExtra("memberId");

        recycler_list.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_list.setNestedScrollingEnabled(false);

        mPresenter = new PersonShopPresent(this, this);
        mPresenter.getPersonShopData(currentMemberId);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getPersonShop(PersonShopEntity personShopEntity) {
        tv_title.setText(personShopEntity.nickname + "的小店");
        tv_store_name.setText(personShopEntity.nickname + "的小店");
        setMiv_level(personShopEntity.level);
        setMivHonour(personShopEntity.member_role);

        GlideUtils.getInstance().loadImage(this, miv_icon, personShopEntity.avatarl);

        goodsList = personShopEntity.goods_list;
        mAdapter = new QrcodeStoreAdapter(this, personShopEntity.goods_list, personShopEntity.qrcode);
        mAdapter.setOnItemClickListener(this);
        recycler_list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void setMivHonour(String type) {
        switch (type) {
            case "1":
                miv_honour.setImageResource(R.mipmap.img_chuangkejingying);
                miv_honour.setVisibility(View.VISIBLE);
                break;
            case "2":
                miv_honour.setImageResource(R.mipmap.img_jingyingdaoshi);
                miv_honour.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setMiv_level(String level) {
        switch (level) {
            default:
                miv_level.setImageResource(R.mipmap.v0);
                break;
            case "1":
                miv_level.setImageResource(R.mipmap.v1);
                break;
            case "2":
                miv_level.setImageResource(R.mipmap.v2);
                break;
            case "3":
                miv_level.setImageResource(R.mipmap.v3);
                break;
            case "4":
                miv_level.setImageResource(R.mipmap.v4);
                break;
            case "5":
                miv_level.setImageResource(R.mipmap.v5);
                break;
            case "6":
                miv_level.setImageResource(R.mipmap.v6);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        GoodsDeatilEntity.Goods goods = goodsList.get(position);
        GoodsDetailAct.startAct(this, goods.goods_id);
    }
}
