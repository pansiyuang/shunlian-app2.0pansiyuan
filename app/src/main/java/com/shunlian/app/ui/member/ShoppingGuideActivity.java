package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.eventbus_bean.MemberInfoEvent;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.presenter.MemberAddPresenter;
import com.shunlian.app.presenter.MemberPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.view.IMemberCodePageView;
import com.shunlian.app.widget.EditTextImage;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 */

public class ShoppingGuideActivity extends BaseActivity implements View.OnClickListener,IMemberCodePageView {
    MemberAddPresenter memberAddPresenter;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.relt_empty_guide)
    RelativeLayout relt_empty_guide;

    @BindView(R.id.tv_add_guide)
    TextView tv_add_guide;

    private CommonDialogUtil commonDialogUtil;

    private MemberInfoEntity.Followfrom followfrom;

    private MemberCodeListEntity bean;
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
        return R.layout.act_shop_guide;
    }
    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .init();
        followfrom = (MemberInfoEntity.Followfrom)this.getIntent().getSerializableExtra("followfrom");
        if(followfrom==null){
            followfrom = new MemberInfoEntity.Followfrom();
        }
        memberAddPresenter = new MemberAddPresenter(this,this);
        commonDialogUtil = new CommonDialogUtil(this);
        nei_empty.setImageResource(R.mipmap.img_huiyuan_ji).setText("您还没有导购专员，快去绑定吧！").setButtonText(null);
        if(followfrom!=null&&followfrom.code!=null) {
            memberAddPresenter.codeDetail(true,followfrom.code);
        }else{
            relt_empty_guide.setVisibility(View.VISIBLE);
        }

    }

    public static void startAct(Context context, MemberInfoEntity.Followfrom followfrom) {
        Intent intent = new Intent(context, ShoppingGuideActivity.class);
        intent.putExtra("followfrom",followfrom);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_add_guide.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_add_guide){
            /**
             * 输入邀请码
             */
            commonDialogUtil.inputGuideCommonDialog(new ICallBackResult<String>() {
                @Override
                public void onTagClick(String data) {
                    if(memberAddPresenter!=null){
                        memberAddPresenter.codeDetail(false,data);
                    }
                }
            });
        }
    }

    /**
     * 确认绑定
     */
    private void showGuideUserInfo(MemberCodeListEntity bean){
        this.bean = bean;
        commonDialogUtil.guideInfoCommonDialog(new ICallBackResult<MemberCodeListEntity.ListBean>() {
            @Override
            public void onTagClick(MemberCodeListEntity.ListBean data) {
                if(memberAddPresenter!=null){
                    memberAddPresenter.bindShareidAfter(bean.info.code);
                }
            }
        },bean.info);
    }

    @Override
    public void codeInfo(MemberCodeListEntity bean, String error,boolean intoPage) {
        if(bean!=null&&bean.info!=null){
            if(intoPage){
                relt_empty_guide.setVisibility(View.GONE);
                //进入app
            }else {
                showGuideUserInfo(bean);
            }
        }else{
            relt_empty_guide.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void bindSuccess(String code) {
        this.followfrom.code = this.bean.info.code;
        this.followfrom.avatar = this.bean.info.avatar;
        this.followfrom.nickname = this.bean.info.nickname;
        this.followfrom.weixin = this.bean.info.weixin;
        relt_empty_guide.setVisibility(View.GONE);
        EventBus.getDefault().post(new MemberInfoEvent(followfrom.code,followfrom.avatar
                ,followfrom.nickname,followfrom.weixin));
    }

    /**
     * 绑定过导购显示内容
     */
    private void setBindGuideWeixin(){


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
