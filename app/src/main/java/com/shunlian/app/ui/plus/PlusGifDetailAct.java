package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ProductDetailAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ProductDetailEntity;
import com.shunlian.app.presenter.GifDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.confirm_order.PLUSConfirmOrderAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IGifDetailView;
import com.shunlian.app.widget.ParamDialog;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/25.
 */

public class PlusGifDetailAct extends BaseActivity implements IGifDetailView, ParamDialog.OnSelectCallBack {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_buy)
    TextView tv_buy;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private ProductDetailAdapter mAdapter;
    private GifDetailPresenter mPresenter;
    private String currentId;
    private LinearLayoutManager manager;
    private ParamDialog paramDialog;
    private ProductDetailEntity mEntity;
    private PromptDialog promptDialog;

    public static void startAct(Context context, String productId) {
        Intent intent = new Intent(context, PlusGifDetailAct.class);
        intent.putExtra("product_id", productId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_plus_gif_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(R.string.plus_product_detail);

        currentId = getIntent().getStringExtra("product_id");

        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
    }

    @Override
    protected void onResume() {
        mPresenter = new GifDetailPresenter(this, this);
        mPresenter.getProductDetail(currentId);
        super.onResume();
    }

    @Override
    protected void initListener() {
        tv_buy.setOnClickListener(v -> {
            if (paramDialog == null) {
                paramDialog = new ParamDialog(this, mEntity);
                paramDialog.isSelectCount = false;
                paramDialog.setOnSelectCallBack(this);
            } else {
                paramDialog.setParamGoods(mEntity);
            }

            if (!paramDialog.isShowing()) {
                paramDialog.show();
            }
        });
        super.initListener();
    }

    @Override
    public void getGifDetail(ProductDetailEntity productDetailEntity) {
        mEntity = productDetailEntity;
        ProductDetailEntity.Detail detail = productDetailEntity.detail;
        mAdapter = new ProductDetailAdapter(this, productDetailEntity, detail.pics);
        recycler_list.setAdapter(mAdapter);

        if (Integer.valueOf(productDetailEntity.stock) == 0) {//已售罄
            tv_buy.setBackgroundColor(getColorResouce(R.color.white_ash));
            tv_buy.setText(getStringResouce(R.string.seller_out));
            tv_buy.setTextColor(getColorResouce(R.color.my_gray_one));
            tv_buy.setEnabled(false);
        } else {
            tv_buy.setBackgroundColor(getColorResouce(R.color.pink_color));
            tv_buy.setText(getStringResouce(R.string.to_buy));
            tv_buy.setTextColor(getColorResouce(R.color.white));
            tv_buy.setEnabled(true);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(this,"login");
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
            promptDialog.setSureAndCancleListener(String.format(getStringResouce(R.string.plus_open_up), mEntity.member_code),
                    getStringResouce(R.string.SelectRecommendAct_sure), v -> {
                        String skuid = null;
                        if (sku != null) {
                            skuid = sku.id;
                        }
                        PLUSConfirmOrderAct.startAct(PlusGifDetailAct.this, currentId, skuid);
                        promptDialog.dismiss();
                    }, getStringResouce(R.string.errcode_cancel),
                    v -> promptDialog.dismiss());
        }
        promptDialog.show();
    }
}
