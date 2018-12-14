package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.shunlian.app.utils.GlideUtils;
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
    @BindView(R.id.title_bar)
    RelativeLayout title_bar;
    @BindView(R.id.tv_head)
    TextView tv_head;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;
    @BindView(R.id.relt_empty_guide)
    RelativeLayout relt_empty_guide;
    @BindView(R.id.tv_add_guide)
    TextView tv_add_guide;

    @BindView(R.id.line_no_weixin)
    LinearLayout line_no_weixin;
    @BindView(R.id.img__member_head)
    ImageView img__member_head;
    @BindView(R.id.tv_member_name)
    TextView tv_member_name;
    @BindView(R.id.tv_member_number)
    TextView tv_member_number;

    @BindView(R.id.line_have_weixin)
    LinearLayout line_have_weixin;
    @BindView(R.id.img__weixin_member_head)
    ImageView img__weixin_member_head;
    @BindView(R.id.tv_weixin_member_name)
    TextView tv_weixin_member_name;
    @BindView(R.id.tv_weixin_member_number)
    TextView tv_weixin_member_number;
    @BindView(R.id.tv_weixin_number)
    TextView tv_weixin_number;
    @BindView(R.id.tv_weixin_copy)
    TextView tv_weixin_copy;

    @BindView(R.id.line_bg)
    LinearLayout line_bg;

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
                .statusBarColor(R.color.transparent)
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
        }else if(view.getId()==R.id.tv_weixin_copy){
            if(!TextUtils.isEmpty(followfrom.code)){
                Common.copyText(this,followfrom.weixin);
                Common.staticToast("复制成功");
            }
        }
    }

    /**
     * 确认绑定
     */
    private void showGuideUserInfo(MemberCodeListEntity bean){
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
        this.bean = bean;
        if(bean!=null&&bean.info!=null){
            if(intoPage){
                bindSuccess(bean.info.code);
                //直接进入app
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
        setBindGuideWeixin();
        EventBus.getDefault().post(new MemberInfoEvent(followfrom.code,followfrom.avatar
                ,followfrom.nickname,followfrom.weixin));
    }

    /**
     * 绑定过导购显示内容
     */
    private void setBindGuideWeixin(){
        line_bg.setBackgroundResource(R.mipmap.img_daogou_beijing);
        title_bar.setBackgroundColor(getColorResouce(R.color.transparent));
        miv_close.setImageResource(R.mipmap.icon_common_back_white);
        tv_head.setTextColor(getColorResouce(R.color.white));
        immersionBar.statusBarDarkFont(false).init();
        if(!TextUtils.isEmpty(followfrom.weixin)){
            line_have_weixin.setVisibility(View.VISIBLE);
            line_no_weixin.setVisibility(View.GONE);
            GlideUtils.getInstance().loadCircleAvar(this, img__weixin_member_head, followfrom.avatar);
            tv_weixin_member_name.setText(followfrom.nickname);
            tv_weixin_member_number.setText("Ta的邀请码:"+followfrom.code);
            tv_weixin_number.setText(followfrom.weixin);
            tv_weixin_copy.setOnClickListener(this);
            //没有微信显示
         }else{
            line_have_weixin.setVisibility(View.GONE);
            line_no_weixin.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadCircleAvar(this, img__member_head, followfrom.avatar);
            tv_member_name.setText(followfrom.nickname);
            tv_member_number.setText("Ta的邀请码:"+followfrom.code);
            //有微信显示
        }

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
