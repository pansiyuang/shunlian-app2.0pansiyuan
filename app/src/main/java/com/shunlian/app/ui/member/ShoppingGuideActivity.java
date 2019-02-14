package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.MemberTeacherEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.eventbus_bean.MemberInfoEvent;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.presenter.MemberAddPresenter;
import com.shunlian.app.presenter.MemberPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IMemberCodePageView;
import com.shunlian.app.widget.EditTextImage;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.mylibrary.ImmersionBar;
import com.zh.chartlibrary.common.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @BindView(R.id.line_empty_guide)
    LinearLayout line_empty_guide;
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

    @BindView(R.id.img_sys_code)
    ImageView img_sys_code;
    @BindView(R.id.tv_weixin_sys)
    TextView tv_weixin_sys;
    @BindView(R.id.tv_weixin_sys_copy)
    TextView tv_weixin_sys_copy;
    @BindView(R.id.tv_add_save)
    TextView tv_add_save;
    @BindView(R.id.tv_add_meweixin)
    TextView tv_add_meweixin;

    private CommonDialogUtil commonDialogUtil;

    private MemberTeacherEntity memberTeacherEntity;
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
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .init();
        EventBus.getDefault().register(this);
        tv_add_meweixin.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);
        memberAddPresenter = new MemberAddPresenter(this,this);
        commonDialogUtil = new CommonDialogUtil(this);
        memberAddPresenter.codeTeacherDetail();
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
        tv_weixin_sys_copy.setOnClickListener(this);
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
            if(memberTeacherEntity!=null&&memberTeacherEntity.follow_from!=null
                    &&!TextUtils.isEmpty(memberTeacherEntity.follow_from.weixin)){
                Common.staticToastAct(this,"复制成功");
                Common.copyText(this,memberTeacherEntity.follow_from.weixin);
            }
        }else if(view.getId() == R.id.tv_add_meweixin){
            if(memberTeacherEntity!=null&&memberTeacherEntity.my_weinxin!=null) {
                commonDialogUtil.defaultEditDialog(new ICallBackResult<String>() {
                    @Override
                    public void onTagClick(String data) {
                        if(memberAddPresenter!=null){
                            memberAddPresenter.setInfo("weixin",data);
                        }
                    }
                },memberTeacherEntity.my_weinxin!=null?memberTeacherEntity.my_weinxin:"");
            }
        }else if(view.getId()==R.id.tv_weixin_sys_copy){
            if(memberTeacherEntity!=null&&memberTeacherEntity.system_weixin!=null
                    &&!TextUtils.isEmpty(memberTeacherEntity.system_weixin.weixin)){
                Common.staticToastAct(this,"复制成功");
                Common.copyText(this,memberTeacherEntity.system_weixin.weixin);
            }
        }else if(view.getId()==R.id.tv_add_save){
            if(memberTeacherEntity!=null&&memberTeacherEntity.system_weixin!=null
                    &&!TextUtils.isEmpty(memberTeacherEntity.system_weixin.weixin_qrcode)){
                new DownLoadImageThread(this, memberTeacherEntity.system_weixin.weixin_qrcode, new DownLoadImageThread.MyCallBack() {
                    @Override
                    public void successBack() {
                        downLoadHand.sendEmptyMessage(0);
                    }
                    @Override
                    public void errorBack() {
                    }
                }).start();
            }
        }
    }

    /**
     * 发送下载消息
     */
    public Handler downLoadHand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Common.staticToastAct(ShoppingGuideActivity.this,"已存入手机相册");
        }
    };

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
        if(bean!=null&&bean.info!=null){
            showGuideUserInfo(bean);
        }
    }

    @Override
    public void bindSuccess(String code) {
        memberAddPresenter.codeTeacherDetail();
    }

    @Override
    public void teacherCodeInfo(MemberTeacherEntity memberTeacherEntity) {
        if(memberTeacherEntity!=null){
            this.memberTeacherEntity =memberTeacherEntity;
            if(memberTeacherEntity.follow_from==null||memberTeacherEntity.follow_from.code==null){
                line_empty_guide.setVisibility(View.VISIBLE);
            }else {
                line_empty_guide.setVisibility(View.GONE);
                setBindGuideWeixin();
            }

            if(memberTeacherEntity.system_weixin!=null){
                tv_weixin_sys.setText("微信号："+memberTeacherEntity.system_weixin.weixin);
                GlideUtils.getInstance().loadBgImageZheng(this,img_sys_code,memberTeacherEntity.system_weixin.weixin_qrcode);
            }

            if(!TextUtils.isEmpty(memberTeacherEntity.my_weinxin)){
                tv_add_meweixin.setText("编辑我的微信");
            }else{
                tv_add_meweixin.setText("添加我的微信");
            }
        }
    }

    @Override
    public void setWeixin(String weixin) {
        if (weixin!=null&&memberTeacherEntity!=null&&memberTeacherEntity.my_weinxin!=null) {
            memberTeacherEntity.my_weinxin = weixin;
            tv_add_meweixin.setText("编辑我的微信");
        }
    }

    /**
     * 绑定过导购显示内容
     */
    private void setBindGuideWeixin(){
        if(!TextUtils.isEmpty(memberTeacherEntity.follow_from.weixin)){
            line_have_weixin.setVisibility(View.VISIBLE);
            line_no_weixin.setVisibility(View.GONE);
            line_empty_guide.setVisibility(View.GONE);
            GlideUtils.getInstance().loadCircleAvar(this, img__weixin_member_head, memberTeacherEntity.follow_from.avatar);
            tv_weixin_member_name.setText(memberTeacherEntity.follow_from.nickname);
            tv_weixin_member_number.setText("Ta的邀请码:"+memberTeacherEntity.follow_from.code);
            tv_weixin_number.setText(memberTeacherEntity.follow_from.weixin);
            tv_weixin_copy.setOnClickListener(this);
            //没有微信显示
         }else{
            line_have_weixin.setVisibility(View.GONE);
            line_no_weixin.setVisibility(View.VISIBLE);
            line_empty_guide.setVisibility(View.GONE);
            GlideUtils.getInstance().loadCircleAvar(this, img__member_head, memberTeacherEntity.follow_from.avatar);
            tv_member_name.setText(memberTeacherEntity.follow_from.nickname);
            tv_member_number.setText("Ta的邀请码:"+memberTeacherEntity.follow_from.code);
            //有微信显示
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWeixin(MemberInfoEvent memberInfoEvent) {
        if (memberInfoEvent!=null&&memberTeacherEntity!=null&&memberTeacherEntity.my_weinxin!=null) {
            memberTeacherEntity.my_weinxin = memberInfoEvent.weixinNum;
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
