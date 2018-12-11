package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class MemberPageActivity extends BaseActivity{
    private List<NewUserGoodsEntity.Goods> lists;
    private MemberUserAdapter memberUserAdapter;

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
        lists = new ArrayList<>();
        for (int i =0;i<20;i++){
            lists.add(new NewUserGoodsEntity.Goods());
        }
        ImmersionBar.with(this).fitsSystemWindows(false)
                .statusBarColor(R.color.transparent)
                .statusBarDarkFont(true)
                .init();
//        setStatusBarColor(R.color.value_3e3e3e);
//        setStatusBarFontDark();

        memberUserAdapter = new MemberUserAdapter(this,lists);
        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        recy_view.setAdapter(memberUserAdapter);
        memberUserAdapter.notifyDataSetChanged();
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MemberPageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
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
//                        setStatusBarColor(R.color.white);
                        tv_head.setAlpha(2*percent-1);
                        tv_title_right.setAlpha(2*percent-1);
                        miv_close.setAlpha(2*percent-1);
                    }else{
                        tv_head.setTextColor(getColorResouce(R.color.white));
                        tv_title_right.setTextColor(getColorResouce(R.color.white));
                        miv_close.setImageResource(R.mipmap.icon_common_back_white);
//                        setStatusBarColor(R.color.value_3e3e3e);
                        tv_head.setAlpha(1-2*percent);
                        tv_title_right.setAlpha(1-2*percent);
                        miv_close.setAlpha(1-2*percent);
                    }
                }
            }
        });
        line_search.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
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
            ShoppingGuideActivity.startAct(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
