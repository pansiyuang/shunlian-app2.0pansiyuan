package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class MyPageActivity extends BaseActivity {

    @BindView(R.id.mAppbar)
    AppBarLayout mAppbar;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.miv_v)
    MyImageView miv_v;

    @BindView(R.id.miv_expert)
    MyImageView miv_expert;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.miv_title_icon)
    MyImageView miv_title_icon;

    @BindView(R.id.tv_title_nickname)
    TextView tv_title_nickname;

    @BindView(R.id.tv_attention)
    TextView tv_attention;

    @BindView(R.id.tv_signature)
    TextView tv_signature;

    @BindView(R.id.tv_attention_count)
    TextView tv_attention_count;

    @BindView(R.id.tv_fans_count)
    TextView tv_fans_count;

    @BindView(R.id.tv_download_count)
    TextView tv_download_count;

    @BindView(R.id.tv_praise_count)
    TextView tv_praise_count;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.tv_left)
    TextView tv_left;

    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.line_left)
    View line_left;

    @BindView(R.id.line_right)
    View line_right;

    @BindView(R.id.ll_left)
    RelativeLayout ll_left;

    @BindView(R.id.ll_right)
    RelativeLayout ll_right;

    @BindView(R.id.ll_attention)
    LinearLayout ll_attention;

    @BindView(R.id.ll_fans)
    LinearLayout ll_fans;

    @BindView(R.id.ll_download)
    LinearLayout ll_download;

    @BindView(R.id.ll_zan)
    LinearLayout ll_zan;

    @BindView(R.id.miv_title_right)
    MyImageView miv_title_right;

    private String[] titles = {"我的", "收藏"};
    private List<BaseFragment> goodsFrags;
    private String currentMemberId;
    private HotBlogsEntity.MemberInfo currentMember;
    private boolean isDefault = true;
    private ObjectMapper objectMapper;
    private boolean isSelf;
    private BaseFragment commonBlogFrag;
    private int totalDistance;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int distance = (int) msg.obj;
                float alph = Float.valueOf(distance) / totalDistance;
                miv_title_icon.setAlpha(alph);
                tv_title_nickname.setAlpha(alph);
            }
        }
    };

    public static void startAct(Context context, String memberId) {
        Intent intent = new Intent(context, MyPageActivity.class);
        intent.putExtra("member_id", memberId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_my_page;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        currentMemberId = getIntent().getStringExtra("member_id");

        setTabMode(isDefault);
        initFrags();

        objectMapper = new ObjectMapper();
        try {
            String baseInfoStr = SharedPrefUtil.getSharedUserString("base_info", "");
            HotBlogsEntity.BaseInfo baseInfo = objectMapper.readValue(baseInfoStr, HotBlogsEntity.BaseInfo.class);
            if (currentMemberId.equals(baseInfo.member_id)) {
                isSelf = true;
                miv_title_right.setVisibility(View.VISIBLE);
                tv_attention.setVisibility(View.GONE);
            } else {
                miv_title_right.setVisibility(View.GONE);
                tv_attention.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
        tv_attention.setOnClickListener(v -> {
            if (currentMember != null) {
                ((CommonBlogFrag) commonBlogFrag).toFocusUser(currentMember.is_focus, currentMemberId);
            }
        });

        mAppbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            totalDistance = TransformUtil.dip2px(MyPageActivity.this, 81);
            int y = 0;
            if (verticalOffset < 0) {
                y = verticalOffset * -1;
            }
            if (y > totalDistance) {
                y = totalDistance;
            }
            Message message = mHandler.obtainMessage(1);
            message.obj = y;
            mHandler.sendMessage(message);
        });

        ll_left.setOnClickListener(v -> {
            isDefault = true;
            setTabMode(isDefault);
        });
        ll_right.setOnClickListener(v -> {
            isDefault = false;
            setTabMode(isDefault);
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    isDefault = true;
                    setTabMode(isDefault);
                } else {
                    isDefault = false;
                    setTabMode(isDefault);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        ll_attention.setOnClickListener(v -> {
            if (isSelf) {
                AttentionMemberActivity.startAct(this);
            } else {
                AttentionMemberActivity.startAct(this, currentMemberId);
            }
        });

        ll_fans.setOnClickListener(v -> {
            if (isSelf) {
                FansListActivity.startAct(this);
            } else {
                FansListActivity.startAct(this, currentMemberId);
            }
        });

        miv_title_right.setOnClickListener(v -> {
            DiscoverMsgActivity.startActivity(this);
        });

        ll_download.setOnClickListener(v -> {
            //暂时不需要添加点击事件
        });

        ll_zan.setOnClickListener(v -> {
            //暂时不需要添加点击事件
        });
        super.initListener();
    }

    public void initFrags() {
        goodsFrags = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            goodsFrags.add(CommonBlogFrag.getInstance(titles[i], currentMemberId));
        }
        commonBlogFrag = goodsFrags.get(0);
        viewpager.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), goodsFrags, titles));
        viewpager.setOffscreenPageLimit(goodsFrags.size());
    }

    public void initInfo(HotBlogsEntity.MemberInfo memberInfo, HotBlogsEntity.DiscoveryInfo discoveryInfo) {
        if (memberInfo != null) {
            currentMember = memberInfo;
            GlideUtils.getInstance().loadCircleAvar(this, miv_icon, memberInfo.avatar);
            GlideUtils.getInstance().loadCircleAvar(this, miv_title_icon, memberInfo.avatar);
            tv_nickname.setText(memberInfo.nickname);
            tv_title_nickname.setText(memberInfo.nickname);
            tv_signature.setText(memberInfo.signature);

            setAttentStatus(currentMember.is_focus, memberInfo.member_id);
        }
        if (discoveryInfo != null) {
            tv_attention_count.setText(discoveryInfo.focus_num);
            tv_fans_count.setText(discoveryInfo.fans_num);
            tv_download_count.setText(discoveryInfo.down_num);
            tv_praise_count.setText(discoveryInfo.praise_num);
        }
    }

    public void setTabMode(boolean isDefault) {
        if (isDefault) {
            tv_left.setTextColor(getColorResouce(R.color.pink_color));
            tv_right.setTextColor(getColorResouce(R.color.value_484848));
            line_left.setVisibility(View.VISIBLE);
            line_right.setVisibility(View.GONE);
            viewpager.setCurrentItem(0);
        } else {
            tv_right.setTextColor(getColorResouce(R.color.pink_color));
            tv_left.setTextColor(getColorResouce(R.color.value_484848));
            line_right.setVisibility(View.VISIBLE);
            line_left.setVisibility(View.GONE);
            viewpager.setCurrentItem(1);
        }
    }

    public void setAttentStatus(int isFocus, String memberId) {
        if (!currentMemberId.equals(memberId)) {
            return;
        }
        currentMember.is_focus = isFocus;
        if (isFocus == 1) {//已经关注
            tv_attention.setBackgroundDrawable(null);
            tv_attention.setText("已关注");
            tv_attention.setTextColor(getResources().getColor(R.color.text_gray2));
        } else {
            tv_attention.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
            tv_attention.setText("关注");
            tv_attention.setTextColor(getResources().getColor(R.color.pink_color));
        }
    }
}
