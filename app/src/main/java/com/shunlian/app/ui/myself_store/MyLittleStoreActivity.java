package com.shunlian.app.ui.myself_store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.LittleStoreAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.PersonShopEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.PersonStorePresent;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPersonStoreView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.popmenu.PopMenu;
import com.shunlian.app.widget.popmenu.PopMenuItem;
import com.shunlian.app.widget.popmenu.PopMenuItemCallback;
import com.shunlian.app.wxapi.WXEntryActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class MyLittleStoreActivity extends BaseActivity implements IPersonStoreView, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener {
    public PersonStorePresent mPresenter;
    @BindView(R.id.recycler_goods)
    RecyclerView recycler_goods;
    @BindView(R.id.miv_add)
    MyImageView miv_add;
    @BindView(R.id.tv_add_count)
    TextView tv_add_count;
    @BindView(R.id.rl_share)
    RelativeLayout rl_share;
    @BindView(R.id.tv_del)
    TextView tv_del;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.ll_manager)
    LinearLayout ll_manager;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;
    private LittleStoreAdapter mAdapter;
    private List<GoodsDeatilEntity.Goods> goodsList;
    private List<String> selectList;
    private StringBuffer stringBuffer = new StringBuffer();
    private boolean isEdit;
    private PromptDialog promptDialog;
    private PopMenu mPopMenu;
    private String shareTitle, shareDesc, shareImg, shareQrImg, shareLink;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MyLittleStoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_my_little_store;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.decorate_my_store));

        goodsList = new ArrayList<>();
        selectList = new ArrayList<>();
        mAdapter = new LittleStoreAdapter(this, goodsList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recycler_goods.setLayoutManager(manager);
        recycler_goods.setAdapter(mAdapter);
        recycler_goods.setNestedScrollingEnabled(false);
        ((DefaultItemAnimator) recycler_goods.getItemAnimator()).setSupportsChangeAnimations(false);
        recycler_goods.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(this, 12), true));

        mAdapter.setOnItemClickListener(this);
        mPopMenu = new PopMenu.Builder().attachToActivity(this)
                .addMenuItem(new PopMenuItem("微信", getResources().getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem("复制链接", getResources().getDrawable(R.mipmap.icon_lianjie)))
                .addMenuItem(new PopMenuItem("保存二维码", getResources().getDrawable(R.mipmap.icon_erweima)))
                .setOnItemClickListener(new PopMenuItemCallback() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                ShareInfoParam shareInfoParam = new ShareInfoParam();
                                shareInfoParam.title = shareTitle;
                                shareInfoParam.shareLink = shareLink;
                                shareInfoParam.desc = shareDesc;
                                shareInfoParam.img = shareImg;
                                WXEntryActivity.startAct(MyLittleStoreActivity.this, "shareFriend", shareInfoParam,0);
                                break;
                            case 1:
                                Common.copyText(MyLittleStoreActivity.this, shareLink, shareDesc, true);
                                break;
                            case 2:
                                GlideUtils.getInstance().savePicture(MyLittleStoreActivity.this, shareQrImg);
                                break;
                        }
                    }
                })
                .build();
    }


    @Override
    protected void onResume() {
        mPresenter = new PersonStorePresent(this, this);
        mPresenter.getPersonDetail();
        mPresenter.getFairishNums();
        super.onResume();
    }

    @Override
    protected void initListener() {
        miv_add.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.miv_add:
                AddStoreGoodsAct.startAct(this);
                break;
            case R.id.rl_share:
                if (!mPopMenu.isShowing()) {
                    mPopMenu.show();
                }
                break;
            case R.id.tv_title_right:
                isEdit = true;
                showManager(true);
                mAdapter.setEditMode(true);
                break;
            case R.id.tv_del:
                if (selectList.size() == 0) {
                    Common.staticToast("请选择需要删除的商品");
                    return;
                }
                if (promptDialog == null) {
                    checkSelectDialog();
                }
                promptDialog.show();
                break;
            case R.id.tv_sure:
                isEdit = false;
                mAdapter.setEditMode(false);
                showManager(false);
                resetSelectData();
                break;
        }
        super.onClick(view);
    }

    public void showManager(boolean isShow) {
        if (isShow) {
            tv_title_right.setVisibility(View.GONE);
            rl_share.setVisibility(View.GONE);
            miv_add.setVisibility(View.GONE);
            tv_add_count.setVisibility(View.GONE);
            ll_manager.setVisibility(View.VISIBLE);
        } else {
            tv_title_right.setVisibility(View.VISIBLE);
            rl_share.setVisibility(View.VISIBLE);
            miv_add.setVisibility(View.VISIBLE);
            tv_add_count.setVisibility(View.VISIBLE);
            ll_manager.setVisibility(View.GONE);
        }
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void showEmptyView(boolean isShowEmpty) {
        if (isShowEmpty) {
            nei_empty.setImageResource(R.mipmap.img_xiaodian_kongyemian).setText(getStringResouce(R.string.store_empty));
            nei_empty.setButtonText(getStringResouce(R.string.add_goods)).setOnClickListener(v -> AddStoreGoodsAct.startAct(MyLittleStoreActivity.this));
            nei_empty.setVisibility(View.VISIBLE);
            recycler_goods.setVisibility(View.GONE);

            rl_share.setVisibility(View.GONE);
            ll_manager.setVisibility(View.GONE);
            tv_title_right.setVisibility(View.GONE);
            miv_add.setVisibility(View.GONE);
            tv_add_count.setVisibility(View.GONE);
        } else {
            nei_empty.setVisibility(View.GONE);
            recycler_goods.setVisibility(View.VISIBLE);

            tv_title_right.setVisibility(View.VISIBLE);
            tv_title_right.setText(getStringResouce(R.string.manage));
            tv_title_right.setTextColor(getColorResouce(R.color.pink_color));

            if (isEdit) {
                rl_share.setVisibility(View.GONE);
                ll_manager.setVisibility(View.VISIBLE);
                tv_add_count.setVisibility(View.GONE);
                miv_add.setVisibility(View.GONE);
            } else {
                rl_share.setVisibility(View.VISIBLE);
                ll_manager.setVisibility(View.GONE);
                tv_add_count.setVisibility(View.VISIBLE);
                miv_add.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getShopDetail(PersonShopEntity personShopEntity) {
        goodsList.clear();
        if (personShopEntity.shareInfo != null) {
            shareDesc = personShopEntity.shareInfo.desc;
            shareImg = personShopEntity.shareInfo.img;
            shareLink = personShopEntity.shareInfo.wx_link;
            shareQrImg = personShopEntity.shareInfo.qrcode_img;
            shareTitle = personShopEntity.shareInfo.title;
        }
        if (!isEmpty(personShopEntity.goods_list)) {
            goodsList.addAll(personShopEntity.goods_list);
            mAdapter.notifyDataSetChanged();

            showEmptyView(false);
        } else {
            showEmptyView(true);
        }
    }

    @Override
    public void getFairishNums(String count, boolean isDel) {
        if (!isEmpty(count)) {
            tv_add_count.setText(String.format(getStringResouce(R.string.add_some_goods), count));
        }
        if (isDel) {
            isEdit = false;
            if (promptDialog != null) {
                promptDialog.dismiss();
            }
            Common.staticToast("删除成功");

            Iterator<GoodsDeatilEntity.Goods> iterator = goodsList.iterator();
            while (iterator.hasNext()) {
                GoodsDeatilEntity.Goods goods = iterator.next();
                if (goods.isSelect) {
                    iterator.remove();
                }
            }
            mAdapter.notifyDataSetChanged();
            selectList.clear();

            if (goodsList.size() == 0) {
                showEmptyView(true);
                mAdapter.setEditMode(false);
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        GoodsDeatilEntity.Goods goods = goodsList.get(position);
        if (!isEdit) {
            GoodsDetailAct.startAct(this, goods.goods_id);
            return;
        }
        int currentIndex = goods.index;
        if (goods.isSelect) {
            if (selectList.contains(goods.goods_id)) {
                selectList.remove(goods.goods_id);
            }
            for (int i = 0; i < goodsList.size(); i++) {
                if (goodsList.get(i).isSelect && currentIndex < goodsList.get(i).index) {
                    goodsList.get(i).index -= 1;
                }
            }
            goods.isSelect = false;
            goods.index = -1;
        } else {
            goods.isSelect = true;
            selectList.add(goods.goods_id);
            goods.index = selectList.size();
        }
        mAdapter.notifyItemRangeChanged(0, goodsList.size(), goodsList);
    }

    public void delGoods() {
        stringBuffer.setLength(0);
        for (int i = 0; i < selectList.size(); i++) {
            stringBuffer.append(selectList.get(i));
            if (i != selectList.size() - 1) {
                stringBuffer.append(",");
            }
        }
        mPresenter.delStorGoods(stringBuffer.toString());
    }

    public void checkSelectDialog() {
        promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener("", "确定要删除小店中的这些商品吗",
                getStringResouce(R.string.SelectRecommendAct_sure), v -> delGoods(),
                getStringResouce(R.string.errcode_cancel), v -> promptDialog.dismiss());
    }

    public void resetSelectData() {
        for (GoodsDeatilEntity.Goods goods : goodsList) {
            goods.isSelect = false;
            goods.index = -1;
        }
        mAdapter.notifyDataSetChanged();
        selectList.clear();
    }

    @Override
    protected void onDestroy() {
        mPresenter.cancelRequest();
        super.onDestroy();
    }
}
