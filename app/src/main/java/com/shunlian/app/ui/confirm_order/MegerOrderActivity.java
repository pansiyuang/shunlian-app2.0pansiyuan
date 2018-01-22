package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ViewPageFragmentAdapter;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.MegerPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.fragment.RecommendFrag;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IMegerView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PagerSlidingTabStrip;
import com.shunlian.app.widget.ParamDialog;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MegerOrderActivity extends BaseActivity implements IMegerView, ParamDialog.OnSelectCallBack, View.OnClickListener {

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.miv_more)
    MyImageView miv_more;

    @BindView(R.id.tv_number)
    TextView tv_number;

    @BindView(R.id.strip_tab)
    PagerSlidingTabStrip strip_tab;

    @BindView(R.id.vp_meger)
    ViewPager vp_meger;

    @BindView(R.id.tv_meger_total)
    TextView tv_meger_total;

    @BindView(R.id.tv_meger_min)
    TextView tv_meger_min;

    @BindView(R.id.tv_to_shopcar)
    TextView tv_to_shopcar;

    public MegerPresenter megerPresenter;
    public ViewPageFragmentAdapter viewPageFragmentAdapter;
    private String currentNeedId;
    private GoodsDeatilEntity.Goods currentGoods;
    private ParamDialog paramDialog;

    public static void startAct(Context context, String needId) {
        Intent intent = new Intent(context, MegerOrderActivity.class);
        intent.putExtra("need_more", needId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meger_order;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        currentNeedId = getIntent().getStringExtra("need_more");
        if (!TextUtils.isEmpty(currentNeedId)) {
            megerPresenter = new MegerPresenter(this, this);
            megerPresenter.getMegercCates(currentNeedId);
        }
        viewPageFragmentAdapter = new ViewPageFragmentAdapter(getSupportFragmentManager(), strip_tab, vp_meger);
    }

    @Override
    protected void initListener() {
        tv_to_shopcar.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getCateEntity(CateEntity cateEntity) {
        List<CateEntity.Cate> cateList = cateEntity.cates;
        strip_tab.removeAllTab();//清除所有

        if (isEmpty(cateList)) {
            CateEntity.Cate item = new CateEntity.Cate();
            item.cate_id = "0";
            item.cate_name = "全部";
            viewPageFragmentAdapter.addTab(item.cate_name, item.cate_id, RecommendFrag.getInstance(currentNeedId, item.cate_id));
        } else {
            for (int i = 0; i < cateList.size(); i++) {
                CateEntity.Cate cate = cateList.get(i);
                LogUtil.httpLogW("cateList:" + cateList.size() + "   cate_id:" + cate.cate_id);
                viewPageFragmentAdapter.addTab(cate.cate_name, cate.cate_id, RecommendFrag.getInstance(currentNeedId, cate.cate_id));
            }
        }
        strip_tab.needTabStrip();

        tv_meger_total.setText("小计：¥" + Common.dotAfterSmall(cateEntity.sub_amount, 11));
        tv_meger_min.setText(cateEntity.hint);
        setCateCount(cateEntity.sub_count);
    }

    @Override
    public void getGoodsInfo(GoodsDeatilEntity.GoodsInfo goodsInfo) {
        if (currentGoods != null) {
            currentGoods.goods_info = goodsInfo;

            paramDialog = new ParamDialog(this, currentGoods);
            paramDialog.setOnSelectCallBack(this);
            paramDialog.show();
        }
    }

    @Override
    public void addCart(CateEntity cateEntity) {
        tv_meger_total.setText("小计：¥" + Common.dotAfterSmall(cateEntity.sub_amount, 11));
        tv_meger_min.setText(cateEntity.hint);

        setCateCount(cateEntity.sub_count);
    }

    public void setCateCount(String cateStr) {
        int count;
        if (isEmpty(cateStr)) {
            tv_number.setVisibility(View.GONE);
        } else {
            count = Integer.valueOf(cateStr);
            if (count <= 0) {
                tv_number.setVisibility(View.GONE);
            } else if (count > 0 && count <= 99) {
                tv_number.setVisibility(View.VISIBLE);
                tv_number.setText(cateStr);
            } else {
                tv_number.setVisibility(View.VISIBLE);
                tv_number.setText("99+");
            }
        }
    }

    public void getGoodsInfo(GoodsDeatilEntity.Goods goods) {
        currentGoods = goods;
        megerPresenter.getGoodsSku(goods.goods_id);
    }

    @Override
    public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
        if (sku == null) {
            megerPresenter.addCart(currentGoods.goods_id, null, String.valueOf(count));
        } else {
            megerPresenter.addCart(currentGoods.goods_id, sku.id, String.valueOf(count));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_to_shopcar:
                MainActivity.startAct(this, "shoppingcar");
                break;
        }
        super.onClick(view);
    }
}
