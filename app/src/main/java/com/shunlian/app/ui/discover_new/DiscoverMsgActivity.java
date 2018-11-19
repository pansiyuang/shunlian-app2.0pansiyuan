package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.eventbus_bean.DiscoveryCountEvent;
import com.shunlian.app.presenter.DiscoverMsgPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.discover_new.discoverMsg.AttentionMsgFrag;
import com.shunlian.app.ui.discover_new.discoverMsg.DownloadMsgFrag;
import com.shunlian.app.ui.discover_new.discoverMsg.NoticeMsgFrag;
import com.shunlian.app.ui.discover_new.discoverMsg.ZanAndShareMsgFrag;
import com.shunlian.app.view.IDiscoverMsgView;
import com.shunlian.app.widget.MyImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class DiscoverMsgActivity extends BaseActivity implements IDiscoverMsgView {

    @BindView(R.id.rl_zan)
    RelativeLayout rl_zan;

    @BindView(R.id.tv_zan)
    TextView tv_zan;

    @BindView(R.id.view_zan)
    View view_zan;

    @BindView(R.id.miv_zan_point)
    MyImageView miv_zan_point;

    @BindView(R.id.rl_attention)
    RelativeLayout rl_attention;

    @BindView(R.id.tv_attention)
    TextView tv_attention;

    @BindView(R.id.view_attention)
    View view_attention;

    @BindView(R.id.miv_attention_point)
    MyImageView miv_attention_point;

    @BindView(R.id.rl_download)
    RelativeLayout rl_download;

    @BindView(R.id.tv_download)
    TextView tv_download;

    @BindView(R.id.view_download)
    View view_download;

    @BindView(R.id.miv_download_point)
    MyImageView miv_download_point;

    @BindView(R.id.rl_notice)
    RelativeLayout rl_notice;

    @BindView(R.id.tv_notice)
    TextView tv_notice;

    @BindView(R.id.view_notice)
    View view_notice;

    @BindView(R.id.miv_notice_point)
    MyImageView miv_notice_point;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.line_title)
    View line_title;

    private List<BaseFragment> baseFragmentList;
    private ZanAndShareMsgFrag zanFrag;
    private AttentionMsgFrag attentionMsgFrag;
    private DownloadMsgFrag downloadMsgFrag;
    private NoticeMsgFrag noticeMsgFrag;
    private DiscoverMsgPresenter mPresenter;
    private String[] titles = {"点赞/分享", "关注", "下载", "通知"};
    private int showType;//0 分享点赞 ,1 关注 2,下载 3,通知
    private int currentPraiseCount, currentAttentionCount, currentNoticeCount, currentDownloadCount, totalMsgCount;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DiscoverMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_discover_msg;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText("发现消息");
        line_title.setVisibility(View.GONE);
        mPresenter = new DiscoverMsgPresenter(this, this);
        mPresenter.getDiscoverMsg();
        initFrags();

        showTab(showType);
        viewpager.setCurrentItem(showType);
    }

    @Override
    protected void initListener() {
        rl_zan.setOnClickListener(v -> {
            showType = 0;
            showTab(showType);
            viewpager.setCurrentItem(showType);
        });
        rl_attention.setOnClickListener(v -> {
            showType = 1;
            showTab(showType);
            viewpager.setCurrentItem(showType);
        });
        rl_download.setOnClickListener(v -> {
            showType = 2;
            showTab(showType);
            viewpager.setCurrentItem(showType);
        });
        rl_notice.setOnClickListener(v -> {
            showType = 3;
            showTab(showType);
            viewpager.setCurrentItem(showType);
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showType = position;
                showTab(showType);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        super.initListener();
    }

    private void initFrags() {

        baseFragmentList = new ArrayList<>();

        zanFrag = new ZanAndShareMsgFrag();
        attentionMsgFrag = new AttentionMsgFrag();
        downloadMsgFrag = new DownloadMsgFrag();
        noticeMsgFrag = new NoticeMsgFrag();

        baseFragmentList.add(zanFrag);
        baseFragmentList.add(attentionMsgFrag);
        baseFragmentList.add(downloadMsgFrag);
        baseFragmentList.add(noticeMsgFrag);

        viewpager.setOffscreenPageLimit(baseFragmentList.size());
        viewpager.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), baseFragmentList, titles));
    }

    public void showTab(int tab) {
        tv_zan.setTextColor(getColorResouce(R.color.value_484848));
        tv_attention.setTextColor(getColorResouce(R.color.value_484848));
        tv_download.setTextColor(getColorResouce(R.color.value_484848));
        tv_notice.setTextColor(getColorResouce(R.color.value_484848));

        view_zan.setVisibility(View.INVISIBLE);
        view_attention.setVisibility(View.INVISIBLE);
        view_download.setVisibility(View.INVISIBLE);
        view_notice.setVisibility(View.INVISIBLE);

        switch (tab) {
            case 0:
                tv_zan.setTextColor(getColorResouce(R.color.pink_color));
                view_zan.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_attention.setTextColor(getColorResouce(R.color.pink_color));
                view_attention.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_download.setTextColor(getColorResouce(R.color.pink_color));
                view_download.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_notice.setTextColor(getColorResouce(R.color.pink_color));
                view_notice.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void showPraisePage() {
        totalMsgCount = totalMsgCount - currentPraiseCount;
        currentPraiseCount = 0;
        miv_zan_point.setVisibility(View.GONE);

        EventBus.getDefault().post(new DiscoveryCountEvent(totalMsgCount > 0 ? true : false));
    }

    public void showAttentionPage() {
        totalMsgCount = totalMsgCount - currentAttentionCount;
        currentAttentionCount = 0;
        miv_attention_point.setVisibility(View.GONE);

        EventBus.getDefault().post(new DiscoveryCountEvent(totalMsgCount > 0 ? true : false));
    }

    public void showDownloadPage() {
        totalMsgCount = totalMsgCount - currentDownloadCount;
        currentDownloadCount = 0;
        miv_download_point.setVisibility(View.GONE);

        EventBus.getDefault().post(new DiscoveryCountEvent(totalMsgCount > 0 ? true : false));
    }

    public void showNoticePage() {
        totalMsgCount = totalMsgCount - currentNoticeCount;
        currentNoticeCount = 0;
        miv_notice_point.setVisibility(View.GONE);

        EventBus.getDefault().post(new DiscoveryCountEvent(totalMsgCount > 0 ? true : false));
    }

    @Override
    public void getDiscoverMsg(CommonEntity commonEntity) {
        currentPraiseCount = commonEntity.praise_share;
        currentAttentionCount = commonEntity.attention;
        currentDownloadCount = commonEntity.download;
        currentNoticeCount = commonEntity.notice;
        totalMsgCount = commonEntity.total;

        miv_zan_point.setVisibility(currentPraiseCount > 0 ? View.VISIBLE : View.GONE);
        miv_attention_point.setVisibility(currentAttentionCount > 0 ? View.VISIBLE : View.GONE);
        miv_download_point.setVisibility(currentDownloadCount > 0 ? View.VISIBLE : View.GONE);
        miv_notice_point.setVisibility(currentNoticeCount > 0 ? View.VISIBLE : View.GONE);
    }
}
