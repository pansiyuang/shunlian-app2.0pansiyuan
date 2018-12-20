package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.eventbus_bean.MemberInfoEvent;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.presenter.MemberPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.view.IMemberPageView;
import com.shunlian.app.widget.EditTextImage;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class SettingMemberActivity extends BaseActivity implements View.OnClickListener , IMemberPageView {
    MemberPagePresenter memberPagePresenter;
    @BindView(R.id.tv_add_weixin)
    TextView tv_add_weixin;

    @BindView(R.id.tv_me_weixin)
    TextView tv_me_weixin;

    @BindView(R.id.tv_weixin_title)
    TextView tv_weixin_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.line_have_weixin)
    LinearLayout line_have_weixin;

    @BindView(R.id.line_no_weixin)
    LinearLayout line_no_weixin;

    private String weixinNum;
    private CommonDialogUtil commonDialogUtil;
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
        return R.layout.act_setting_member;
    }
    @Override
    protected void initData() {
        memberPagePresenter = new MemberPagePresenter(this,this);
        weixinNum = this.getIntent().getStringExtra("weixinNum");
        commonDialogUtil = new CommonDialogUtil(this);
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setWeixinView();
    }

    /**
     * 设置微信号显示
     */
    private void setWeixinView(){
        if(TextUtils.isEmpty(weixinNum)){
            tv_add_weixin.setText("填写微信号");
            tv_add_weixin.setBackgroundResource(R.drawable.rounded_corner_solid_pink_50px);
            tv_weixin_title.setVisibility(View.GONE);
            tv_me_weixin.setVisibility(View.GONE);
            line_have_weixin.setVisibility(View.GONE);
            line_no_weixin.setVisibility(View.VISIBLE);
        }else{
            tv_add_weixin.setText("编辑");
            tv_add_weixin.setBackgroundResource(R.drawable.rounded_corner_solid_pink_50px);
            tv_weixin_title.setVisibility(View.VISIBLE);
            tv_me_weixin.setVisibility(View.VISIBLE);
            tv_me_weixin.setText(weixinNum);
            line_have_weixin.setVisibility(View.VISIBLE);
            line_no_weixin.setVisibility(View.GONE);
        }
    }
    public static void startAct(Context context,String weixinNum) {
        Intent intent = new Intent(context, SettingMemberActivity.class);
        if(weixinNum==null) {
            intent.putExtra("weixinNum", "");
        }else{
            intent.putExtra("weixinNum", weixinNum);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        tv_add_weixin.setOnClickListener(this);
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_add_weixin){
            commonDialogUtil.defaultEditDialog(new ICallBackResult<String>() {
                @Override
                public void onTagClick(String data) {
                    if(memberPagePresenter!=null){
                        memberPagePresenter.setInfo("weixin",data);
                    }
                }
            },weixinNum);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void memberListInfo(List<MemberInfoEntity.MemberList> memberLists, int currentPage) {
    }

    @Override
    public void memberDetail(MemberInfoEntity memberInfoEntity, String total_num) {
    }

    @Override
    public void setWeixin(String weixin) {
        this.weixinNum = weixin;
        setWeixinView();
        EventBus.getDefault().post(new MemberInfoEvent(weixin));
    }

    @Override
    public void showFailureView(int request_code) {
    }

    @Override
    public void showDataEmptyView(int request_code) {
    }
}
