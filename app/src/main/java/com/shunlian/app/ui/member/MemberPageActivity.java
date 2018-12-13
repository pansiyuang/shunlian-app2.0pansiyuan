package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.MemberInfoEvent;
import com.shunlian.app.eventbus_bean.UserPaySuccessEvent;
import com.shunlian.app.presenter.MemberPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IMemberPageView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class MemberPageActivity extends BaseActivity implements IMemberPageView {
    private List<MemberInfoEntity.MemberList> lists;
    private MemberUserAdapter memberUserAdapter;
    private MemberInfoEntity memberInfoEntity;
    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title_bar)
    RelativeLayout title_bar;

    @BindView(R.id.miv_close)
    MyImageView miv_close;
    @BindView(R.id.tv_head)
    TextView tv_head;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.img_user_head)
    ImageView img_user_head;
    @BindView(R.id.tv_member_name)
    TextView tv_member_name;
    @BindView(R.id.tv_member_num)
    TextView tv_member_num;
    @BindView(R.id.tv_copy_num)
    TextView tv_copy_num;

    @BindView(R.id.tv_sett_state)
    TextView tv_sett_state;
    @BindView(R.id.img_sett_point)
    ImageView img_sett_point;

    @BindView(R.id.tv_me_member_number)
    TextView tv_me_member_number;
    @BindView(R.id.tv_me_member_profit)
    TextView tv_me_member_profit;

    @BindView(R.id.line_search)
    LinearLayout line_search;

    @BindView(R.id.appbar)
     AppBarLayout appbar;

    @BindView(R.id.img_user_level)
    ImageView img_user_level;
    @BindView(R.id.img_user_shop)
    ImageView img_user_shop;

    LinearLayoutManager  manager;
    ImmersionBar immersionBar;

    MemberPagePresenter memberPagePresenter;
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_member_page;
    }
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        memberPagePresenter = new MemberPagePresenter(this, this);
        lists = new ArrayList<>();
        immersionBar = ImmersionBar.with(this);
        immersionBar.fitsSystemWindows(false)
                .statusBarColor(R.color.transparent)
                .statusBarDarkFont(false)
                .init();
        memberUserAdapter = new MemberUserAdapter(this,lists);
        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        recy_view.setAdapter(memberUserAdapter);

        memberPagePresenter.memberListInfo(true);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MemberPageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (memberPagePresenter != null) {
                            memberPagePresenter.onRefresh();
                        }
                    }
                }
            }
        });
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
                if (title_bar != null && miv_close != null && tv_head != null && tv_title_right != null) {
                    toolbar.setAlpha(percent);
                    if(percent>0.5){
                        tv_head.setTextColor(getColorResouce(R.color.black));
                        tv_title_right.setTextColor(getColorResouce(R.color.black));
                        miv_close.setImageResource(R.mipmap.icon_common_back_black);
                        tv_head.setAlpha(2*percent-1);
                        tv_title_right.setAlpha(2*percent-1);
                        miv_close.setAlpha(2*percent-1);
                        immersionBar.statusBarDarkFont(true).init();
                    }else{
                        tv_head.setTextColor(getColorResouce(R.color.white));
                        tv_title_right.setTextColor(getColorResouce(R.color.white));
                        miv_close.setImageResource(R.mipmap.icon_common_back_white);
                        tv_head.setAlpha(1-2*percent);
                        tv_title_right.setAlpha(1-2*percent);
                        miv_close.setAlpha(1-2*percent);
                        if (percent > 0) {
                            immersionBar.statusBarDarkFont(false).init();
                        }
                    }
                }
            }
        });
        line_search.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_sett_state.setOnClickListener(this);
        tv_copy_num.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
       if(view.getId()==R.id.line_search){
           SearchMemberActivity.startAct(this);
       }else if(view.getId()==R.id.tv_title_right){
           if(memberInfoEntity!=null)
            ShoppingGuideActivity.startAct(this,memberInfoEntity.follow_from);
        }else if(view.getId()==R.id.tv_sett_state){
           if(memberInfoEntity!=null) {
               SettingMemberActivity.startAct(this,memberInfoEntity.weixin);
           }
       }else if(view.getId()==R.id.tv_copy_num){
           if(memberInfoEntity!=null&&memberInfoEntity.invite_code!=null) {
               Common.copyText(this, memberInfoEntity.invite_code);
               Common.staticToast("复制成功");
           }
       }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void memberListInfo(List<MemberInfoEntity.MemberList> memberLists,int currentPage) {
           if(memberLists.size()>0){
               this.lists.addAll(memberLists);
               memberUserAdapter.notifyDataSetChanged();
           }
    }

    @Override
    public void memberDetail(MemberInfoEntity memberInfoEntity,String personNum) {
         this.memberInfoEntity = memberInfoEntity;
         GlideUtils.getInstance().loadCircleAvar(this, img_user_head, memberInfoEntity.avatar);
         if(!TextUtils.isEmpty(memberInfoEntity.nickname)) {
             tv_member_name.setText(memberInfoEntity.nickname);
         }
        if(!TextUtils.isEmpty(memberInfoEntity.total_income)) {
            tv_me_member_profit.setText(memberInfoEntity.total_income);
        }
        if(!TextUtils.isEmpty(memberInfoEntity.invite_code)) {
            tv_member_num.setText("我的邀请码:"+memberInfoEntity.invite_code);
        }else{
            tv_member_num.setText("我的邀请码:");
        }
        tv_me_member_number.setText(memberInfoEntity.total_person_count);

        int plus_role_code = Integer.parseInt(memberInfoEntity.role);
        if (plus_role_code == 1) {//店主 1=plus店主，2=销售主管，>=3 销售经理
            img_user_shop.setVisibility(View.VISIBLE);
            img_user_shop.setImageResource(R.mipmap.img_plus_phb_dianzhu);
        } else if (plus_role_code >= 3) {//经理
            img_user_shop.setVisibility(View.VISIBLE);
            img_user_shop.setImageResource(R.mipmap.img_plus_phb_jingli);
        } else if (plus_role_code == 2) {//主管
            img_user_shop.setVisibility(View.VISIBLE);
            img_user_shop.setImageResource(R.mipmap.img_plus_phb_zhuguan);
        } else {//没有级别
            img_user_shop.setVisibility(View.GONE);
        }
        Bitmap levelBitmap =TransformUtil.convertNewVIP(this,memberInfoEntity.level);
        if(levelBitmap!=null){
            img_user_level.setVisibility(View.VISIBLE);
            img_user_level.setImageBitmap(levelBitmap);
        }else{
            img_user_level.setVisibility(View.GONE);
        }
        weixinCodeInfo();
    }

    @Override
    public void setWeixin(String weixin) {

    }

    private void weixinCodeInfo(){
        if(TextUtils.isEmpty(memberInfoEntity.weixin)){
            tv_sett_state.setText("未设置");
            img_sett_point.setVisibility(View.VISIBLE);
        }else{
            tv_sett_state.setText("已设置");
            img_sett_point.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWeixin(MemberInfoEvent memberInfoEvent) {
        if (memberInfoEvent!=null&&memberInfoEntity!=null&&memberInfoEvent.weixinNum!=null) {
            memberInfoEntity.weixin = memberInfoEvent.weixinNum;
            weixinCodeInfo();
        }else if(memberInfoEvent!=null&&memberInfoEntity!=null&&memberInfoEvent.code!=null){
            memberInfoEntity.follow_from.code = memberInfoEvent.code;
            memberInfoEntity.follow_from.avatar = memberInfoEvent.avatar;
            memberInfoEntity.follow_from.nickname = memberInfoEvent.nickname;
            memberInfoEntity.follow_from.weixin = memberInfoEvent.weixin;
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {
    }

}
