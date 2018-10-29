package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
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

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

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
    private boolean isDefault = true;
    private ObjectMapper objectMapper;
    private boolean isSelf;

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
                miv_title_right.setImageResource(R.mipmap.icon_faxian_xiaoxi);
            } else {
                miv_title_right.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
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
        viewpager.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), goodsFrags, titles));
        viewpager.setOffscreenPageLimit(goodsFrags.size());
    }

    public void initInfo(HotBlogsEntity.MemberInfo memberInfo, HotBlogsEntity.DiscoveryInfo discoveryInfo) {
        if (memberInfo != null) {
            GlideUtils.getInstance().loadCircleAvar(this, miv_icon, memberInfo.avatar);
            tv_nickname.setText(memberInfo.nickname);
            tv_signature.setText(memberInfo.signature);
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
}
