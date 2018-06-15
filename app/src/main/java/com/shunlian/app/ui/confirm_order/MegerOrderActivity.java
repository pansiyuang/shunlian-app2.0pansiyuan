package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.MegerPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.fragment.RecommendFrag;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IMegerView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.ParamDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shunlian.app.App.getContext;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MegerOrderActivity extends BaseActivity implements IMegerView, ParamDialog.OnSelectCallBack, View.OnClickListener, MessageCountManager.OnGetMessageListener {

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.tv_title_number)
    TextView tv_title_number;

    @BindView(R.id.tv_title)
    TextView tv_title;

//    @BindView(R.id.tv_number)
//    TextView tv_number;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.vp_meger)
    ViewPager vp_meger;

    @BindView(R.id.tv_meger_total)
    TextView tv_meger_total;

    @BindView(R.id.tv_meger_min)
    TextView tv_meger_min;

    @BindView(R.id.tv_to_shopcar)
    TextView tv_to_shopcar;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    public MegerPresenter megerPresenter;
    private MessageCountManager messageCountManager;
    private String currentNeedId;
    private GoodsDeatilEntity.Goods currentGoods;
    private ParamDialog paramDialog;
    private String[] titles;
    private static List<BaseFragment> mFrags;
    private String currentPromId;

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
        rl_title_more.setVisibility(View.VISIBLE);
        tv_title.setText("凑单");

        EventBus.getDefault().register(this);
        currentNeedId = getIntent().getStringExtra("need_more");
        if (!TextUtils.isEmpty(currentNeedId)) {
            megerPresenter = new MegerPresenter(this, this);
            megerPresenter.getMegercCates(currentNeedId);
        }
    }

    @Override
    protected void onResume() {
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
        if (messageCountManager.isLoad()) {
            String s = messageCountManager.setTextCount(tv_title_number);
            if (quick_actions != null)
                quick_actions.setMessageCount(s);
        } else {
            messageCountManager.initData();
        }
        super.onResume();
    }


    @Override
    protected void initListener() {
        tv_to_shopcar.setOnClickListener(this);
        super.initListener();
    }

    @OnClick(R.id.rl_title_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.afterSale();
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
        mFrags = new ArrayList<>();
        if (isEmpty(cateList)) {
            CateEntity.Cate item = new CateEntity.Cate();
            item.cate_id = "0";
            item.cate_name = "全部";

            titles = new String[1];
            titles[0] = item.cate_name;
            mFrags.add(RecommendFrag.getInstance(currentNeedId, item.cate_id));
            tab_layout.addTab(tab_layout.newTab().setText(item.cate_name));
        } else {
            titles = new String[cateList.size()];
            for (int i = 0; i < cateList.size(); i++) {
                CateEntity.Cate cate = cateList.get(i);
                mFrags.add(RecommendFrag.getInstance(currentNeedId, cate.cate_id));
                tab_layout.addTab(tab_layout.newTab().setText(cate.cate_name));
                titles[i] = cate.cate_name;
            }
        }
        vp_meger.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), mFrags, titles));
        tab_layout.setupWithViewPager(vp_meger);
        vp_meger.setOffscreenPageLimit(mFrags.size());
        tab_layout.post(() -> {
            setTabPadding();
            tab_layout.setBackgroundColor(getColorResouce(R.color.white));
        });

        tv_meger_total.setText("小计：¥" + Common.dotAfterSmall(cateEntity.sub_amount, 11));
        tv_meger_min.setText(cateEntity.hint);
//        setCateCount(cateEntity.sub_count);
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

//        setCateCount(cateEntity.sub_count);
    }
//
//    public void setCateCount(String cateStr) {
//        int count;
//        if (isEmpty(cateStr)) {
//            tv_number.setVisibility(View.GONE);
//        } else {
//            count = Integer.valueOf(cateStr);
//            if (count <= 0) {
//                tv_number.setVisibility(View.GONE);
//            } else if (count > 0 && count <= 99) {
//                tv_number.setVisibility(View.VISIBLE);
//                tv_number.setText(cateStr);
//            } else {
//                tv_number.setVisibility(View.VISIBLE);
//                tv_number.setText("99+");
//            }
//        }
//    }

    public void getGoodsInfo(GoodsDeatilEntity.Goods goods, String promId) {
        currentGoods = goods;
        megerPresenter.getGoodsSku(goods.goods_id);
        currentPromId = promId;
    }

    @Override
    public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
        if (isEmpty(currentPromId)) {
            return;
        }
        if (sku == null) {
            megerPresenter.addCart(currentGoods.goods_id, null, String.valueOf(count), currentPromId);
        } else {
            megerPresenter.addCart(currentGoods.goods_id, sku.id, String.valueOf(count), currentPromId);
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

    public void setTabPadding() {
        try {
            //拿到tabLayout的mTabStrip属性
            Field mTabStripField = tab_layout.getClass().getDeclaredField("mTabStrip");
            mTabStripField.setAccessible(true);
            LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tab_layout);
            int margin = TransformUtil.dip2px(getContext(), 12.5f);
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                View tabView = mTabStrip.getChildAt(i);
                //拿到tabView的mTextView属性
                Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                mTextViewField.setAccessible(true);
                TextView mTextView = (TextView) mTextViewField.get(tabView);
                tabView.setPadding(0, 0, 0, 0);
                //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                int width = 0;
                width = mTextView.getWidth();
                if (width == 0) {
                    mTextView.measure(0, 0);
                    width = mTextView.getMeasuredWidth();
                }
                //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                params.width = width;
                params.leftMargin = margin;
                params.rightMargin = margin;
                tabView.setLayoutParams(params);

                tabView.invalidate();
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_title_number);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_title_number);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }
}
