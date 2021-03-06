package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.CustomViewPager;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MessageActivity extends BaseActivity implements ViewPager.OnPageChangeListener, MessageCountManager.OnGetMessageListener {

    public static final String[] tabTitle = new String[]{"系统消息", "小店消息"};

    @BindView(R.id.view_pager)
    CustomViewPager view_pager;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.miv_title_right)
    MyImageView miv_title_right;

    @BindView(R.id.rl_sys)
    RelativeLayout rl_sys;

    @BindView(R.id.tv_sys_title)
    TextView tv_sys_title;

    @BindView(R.id.tv_sys_count)
    TextView tv_sys_count;

    @BindView(R.id.line_sys)
    View line_sys;

    @BindView(R.id.rl_store)
    RelativeLayout rl_store;

    @BindView(R.id.tv_store_title)
    TextView tv_store_title;

    @BindView(R.id.tv_store_count)
    TextView tv_store_count;

    @BindView(R.id.line_store)
    View line_store;

    @BindView(R.id.line_title)
    View line_title;

    private List<BaseFragment> mFrags = new ArrayList<>();
    private CommonLazyPagerAdapter mPagerAdapter;
    private int sysCount, storeCount;
    private MessageCountManager messageCountManager;
    private MessageListFragment messageListFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.act_message;
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.message));
        miv_title_right.setVisibility(View.VISIBLE);
        miv_title_right.setImageResource(R.mipmap.icon_found_sousuo);
        line_title.setVisibility(View.GONE);

        messageListFragment = MessageListFragment.getInstance();
        mFrags.add(messageListFragment);
        mFrags.add(LittleStoreFragment.getInstance());

        mPagerAdapter = new CommonLazyPagerAdapter(getSupportFragmentManager(), mFrags, tabTitle);
        view_pager.setAdapter(mPagerAdapter);
        view_pager.setOffscreenPageLimit(2);

        SysClick();
    }

    @Override
    protected void initListener() {
        rl_sys.setOnClickListener(this);
        rl_store.setOnClickListener(this);
        view_pager.addOnPageChangeListener(this);
        miv_title_right.setOnClickListener(this);
        super.initListener();
    }

    @Override
    protected void onResume() {
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.initData();
        messageCountManager.setOnGetMessageListener(this);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        messageListFragment.resetData();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        messageCountManager.cancleRequest();
        super.onDestroy();
    }

    private void SysClick() {
        tv_sys_title.setSelected(true);
        tv_store_title.setSelected(false);

        isShowSysCount();

        if (isEmpty(Common.formatBadgeNumber(storeCount))) {
            tv_store_count.setVisibility(View.GONE);
        } else {
            tv_store_count.setText(Common.formatBadgeNumber(storeCount));
            tv_store_count.setVisibility(View.VISIBLE);
        }

        line_sys.setBackgroundColor(getColorResouce(R.color.pink_color));
        line_store.setBackgroundColor(getColorResouce(R.color.light_gray_two));
    }

    /**
     * 是否显示系统消息
     */
    private void isShowSysCount() {
        if (isEmpty(Common.formatBadgeNumber(sysCount))) {
            tv_sys_count.setVisibility(View.GONE);
        } else {
            tv_sys_count.setText(Common.formatBadgeNumber(sysCount));
            tv_sys_count.setVisibility(View.VISIBLE);
        }
    }

    private void StoreClick() {
        tv_sys_title.setSelected(false);
        tv_store_title.setSelected(true);

        if (isEmpty(Common.formatBadgeNumber(storeCount))) {
            tv_store_count.setVisibility(View.GONE);
        } else {
            tv_store_count.setText(Common.formatBadgeNumber(storeCount));
            tv_store_count.setVisibility(View.VISIBLE);
        }

        isShowSysCount();

        line_sys.setBackgroundColor(getColorResouce(R.color.light_gray_two));
        line_store.setBackgroundColor(getColorResouce(R.color.pink_color));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sys:
                SysClick();
                view_pager.setCurrentItem(0);
                break;
            case R.id.rl_store:
                StoreClick();
                view_pager.setCurrentItem(1);
                break;
            case R.id.miv_title_right:
                SearchCustomerActivity.startAct(this);
                break;
        }
        super.onClick(view);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            SysClick();
        } else {
            StoreClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        try {
            sysCount = messageCountEntity.sys_msg;
            storeCount = messageCountEntity.store_msg;
            isShowSysCount();

            if (isEmpty(Common.formatBadgeNumber(storeCount))) {
                tv_store_count.setVisibility(View.GONE);
            } else {
                tv_store_count.setText(Common.formatBadgeNumber(storeCount));
                tv_store_count.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnLoadFail() {

    }

    public void updateMsgCount() {
        sysCount++;
        isShowSysCount();
    }
}
