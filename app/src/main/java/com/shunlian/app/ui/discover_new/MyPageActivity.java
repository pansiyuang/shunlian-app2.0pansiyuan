package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.eventbus_bean.DiscoveryCountEvent;
import com.shunlian.app.presenter.MyPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.setting.AutographAct;
import com.shunlian.app.ui.setting.PersonalDataAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IMyPageView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class MyPageActivity extends BaseActivity implements IMyPageView {

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

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

    @BindView(R.id.ll_info)
    LinearLayout ll_info;

    @BindView(R.id.miv_title_right)
    MyImageView miv_title_right;

    @BindView(R.id.miv_msg_point)
    MyImageView miv_msg_point;

    private String[] titles = {"我的", "收藏"};
    private List<BaseFragment> goodsFrags;
    private String currentMemberId;
    private HotBlogsEntity.MemberInfo currentMember;
    private boolean isDefault = true;
    private ObjectMapper objectMapper;
    private boolean isSelf;
    private BaseFragment commonBlogFrag;
    private int totalDistance;
    private MyPagePresenter mPresenter;
    private int currentUnreadCount;
    private String currentSelectType;
    private int appBarScrollDistance;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int distance = (int) msg.obj;
                    float alph = Float.valueOf(distance) / totalDistance;
                    miv_title_icon.setAlpha(alph);
                    tv_title_nickname.setAlpha(alph);

                    if (distance == 0) {
                        lay_refresh.setRefreshEnabled(true);
                    } else {
                        lay_refresh.setRefreshEnabled(false);
                    }
                    break;
                case 2:
                    lay_refresh.setRefreshEnabled((boolean) msg.obj);
                    break;
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
        EventBus.getDefault().register(this);

        NestedSlHeader header = new NestedSlHeader(this);
        lay_refresh.setRefreshHeaderView(header);

        currentMemberId = getIntent().getStringExtra("member_id");
        mPresenter = new MyPagePresenter(this, this);
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

        setTabMode(isDefault);
        initFrags();
    }

    @Override
    protected void initListener() {

        tv_signature.setOnClickListener(v -> {
            if (isSelf) {
                AutographAct.startAct(this, tv_signature.getText().toString());
            }
        });

        tv_attention.setOnClickListener(v -> {
            if (currentMember != null) {
                ((CommonBlogFrag) commonBlogFrag).toFocusUser(currentMember.is_focus, currentMemberId);
            }
        });

        mAppbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            totalDistance = TransformUtil.dip2px(MyPageActivity.this, 81);
            appBarScrollDistance = 0;
            if (verticalOffset < 0) {
                appBarScrollDistance = verticalOffset * -1;
            }
            if (appBarScrollDistance > totalDistance) {
                appBarScrollDistance = totalDistance;
            }
            Message message = mHandler.obtainMessage(1);
            message.obj = appBarScrollDistance;
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

        miv_icon.setOnClickListener(v -> {
            if (isSelf) {
                PersonalDataAct.startAct(MyPageActivity.this);
            }
        });

        lay_refresh.setOnRefreshListener(() -> mPresenter.getBlogList(currentMemberId, "2"));
        super.initListener();
    }

    public void initFrags() {
        goodsFrags = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            goodsFrags.add(CommonBlogFrag.getInstance(titles[i], currentMemberId, isSelf));
        }
        commonBlogFrag = goodsFrags.get(0);
        viewpager.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), goodsFrags, titles));
        viewpager.setOffscreenPageLimit(goodsFrags.size());
    }

    public void initInfo(HotBlogsEntity.MemberInfo memberInfo, HotBlogsEntity.DiscoveryInfo discoveryInfo, int UnreadCount) {
        currentUnreadCount = UnreadCount;
        if (memberInfo != null) {
            currentMember = memberInfo;
            GlideUtils.getInstance().loadCircleAvar(this, miv_icon, memberInfo.avatar);
            GlideUtils.getInstance().loadCircleAvar(this, miv_title_icon, memberInfo.avatar);

            if (memberInfo.add_v == 0) {
                miv_v.setVisibility(View.GONE);
            } else {
                GlideUtils.getInstance().loadImage(this, miv_v, memberInfo.v_icon);
                miv_v.setVisibility(View.VISIBLE);
            }

            if (memberInfo.expert == 0) {
                miv_expert.setVisibility(View.GONE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_info.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                ll_info.setLayoutParams(layoutParams);
            } else {
                GlideUtils.getInstance().loadImage(this, miv_expert, memberInfo.expert_icon);
                miv_expert.setVisibility(View.VISIBLE);
            }
            tv_nickname.setText(memberInfo.nickname);
            tv_title_nickname.setText(memberInfo.nickname);
            if (isEmpty(memberInfo.signature)) {
                if (isSelf) {
                    tv_signature.setText("还没有个人介绍哦，赶紧去编辑吧");
                    tv_signature.setEnabled(true);
                } else {
                    tv_signature.setText("TA有点高冷，还没有介绍~");
                    tv_signature.setEnabled(false);
                }
            } else {
                tv_signature.setText(memberInfo.signature);
                tv_signature.setEnabled(false);
            }

            if (isSelf && currentUnreadCount > 0) {
                miv_msg_point.setVisibility(View.VISIBLE);
            } else {
                miv_msg_point.setVisibility(View.GONE);
            }

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
            currentSelectType = "2";
        } else {
            tv_right.setTextColor(getColorResouce(R.color.pink_color));
            tv_left.setTextColor(getColorResouce(R.color.value_484848));
            line_right.setVisibility(View.VISIBLE);
            line_left.setVisibility(View.GONE);
            viewpager.setCurrentItem(1);
            currentSelectType = "3";
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

    public void setScrollDistance(boolean canRefresh, String type) {
        if (currentSelectType == type) {
            Message message = mHandler.obtainMessage(2);
            message.obj = canRefresh;
            mHandler.sendMessage(message);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode//个性签名
                && requestCode == AutographAct.REQUEST_CODE) {
            String signature = data.getStringExtra("signature");
            if (!isEmpty(signature) && mPresenter != null) {
                mPresenter.setInfo("signature", signature);
            }
        }
    }

    @Override
    public void setSignature(String signature) {
        tv_signature.setText(signature);
        tv_signature.setEnabled(false);
    }

    @Override
    public void getFocusblogs(HotBlogsEntity hotBlogsEntity) {
        if (hotBlogsEntity != null) {
            if (isDefault && goodsFrags.get(0) != null) {
                ((CommonBlogFrag) goodsFrags.get(0)).initPage();
            }
        }
        currentUnreadCount = hotBlogsEntity.unread;
        if (hotBlogsEntity.member_info != null) {
            currentMember = hotBlogsEntity.member_info;
//            GlideUtils.getInstance().loadCircleAvar(this, miv_icon, currentMember.avatar);
//            GlideUtils.getInstance().loadCircleAvar(this, miv_title_icon, currentMember.avatar);

            if (currentMember.add_v == 0) {
                miv_v.setVisibility(View.GONE);
            } else {
                GlideUtils.getInstance().loadImage(this, miv_v, currentMember.v_icon);
                miv_v.setVisibility(View.VISIBLE);
            }

            if (currentMember.expert == 0) {
                miv_expert.setVisibility(View.GONE);
            } else {
                GlideUtils.getInstance().loadImage(this, miv_expert, currentMember.expert_icon);
                miv_expert.setVisibility(View.VISIBLE);
            }
            tv_nickname.setText(currentMember.nickname);
            tv_title_nickname.setText(currentMember.nickname);
            if (isEmpty(currentMember.signature)) {
                if (isSelf) {
                    tv_signature.setText("还没有个人介绍哦，赶紧去编辑吧");
                    tv_signature.setEnabled(true);
                } else {
                    tv_signature.setText("TA有点高冷，还没有介绍~");
                    tv_signature.setEnabled(false);
                }
            } else {
                tv_signature.setText(currentMember.signature);
                tv_signature.setEnabled(false);
            }

            if (isSelf && currentUnreadCount > 0) {
                miv_msg_point.setVisibility(View.VISIBLE);
            } else {
                miv_msg_point.setVisibility(View.GONE);
            }

            setAttentStatus(currentMember.is_focus, currentMember.member_id);
        }
        if (hotBlogsEntity.discovery_info != null) {
            tv_attention_count.setText(hotBlogsEntity.discovery_info.focus_num);
            tv_fans_count.setText(hotBlogsEntity.discovery_info.fans_num);
            tv_download_count.setText(hotBlogsEntity.discovery_info.down_num);
            tv_praise_count.setText(hotBlogsEntity.discovery_info.praise_num);
        }
    }

    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(DiscoveryCountEvent event) {
        if (isSelf && event.isShow) {
            miv_msg_point.setVisibility(View.VISIBLE);
        } else {
            miv_msg_point.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
