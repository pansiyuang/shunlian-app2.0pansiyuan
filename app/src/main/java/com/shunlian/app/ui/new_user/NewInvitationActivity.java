package com.shunlian.app.ui.new_user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.InviteLogUserEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.UserPaySuccessEvent;
import com.shunlian.app.presenter.NewInvitationPresenter;
import com.shunlian.app.presenter.NewUserPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HighLightKeyWordUtil;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.UserBuyGoodsDialog;
import com.shunlian.app.view.INewUserPageView;
import com.shunlian.app.view.InvitationView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class NewInvitationActivity extends BaseActivity implements InvitationView,ShareGoodDialogUtil.OnShareBlogCallBack {
    private SimpleRecyclerAdapter adapter;

    @BindView(R.id.recy_view_invitaion)
    RecyclerView recy_view_invitaion;


    @BindView(R.id.tv_show_price)
    TextView tv_show_price;

    @BindView(R.id.tv_show_desc)
    TextView tv_show_desc;

    @BindView(R.id.tv_please)
    TextView tv_please;

    @BindView(R.id.tv_my_invitaion)
    TextView tv_my_invitaion;

    @BindView(R.id.tv_invitation_copy)
    TextView tv_invitation_copy;

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;

    @BindView(R.id.empty_view)
    NetAndEmptyInterface empty_view;

    @BindView(R.id.scrollview)
    NestedScrollView scrollview;

    private List<InviteLogUserEntity.UserList> invitationData;
    private String code;

    private NewInvitationPresenter newInvitationPresenter;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private ShareInfoParam shareInfoParam;

    LinearLayoutManager manager;
    @Override
    protected int getLayoutId() {
        return R.layout.act_user_invitation_layout;
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        newInvitationPresenter = new NewInvitationPresenter(this,this);
        invitationData = new ArrayList<>();
        shareInfoParam = new ShareInfoParam();
        shareGoodDialogUtil = new ShareGoodDialogUtil(this);
        manager =  new LinearLayoutManager(this);
        recy_view_invitaion.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleRecyclerAdapter<InviteLogUserEntity.UserList>(this, R.layout.item_invation_user, this.invitationData) {
            @Override
            public void convert(SimpleViewHolder holder, InviteLogUserEntity.UserList  userList, int position) {
                TextView tv_invation_date =  holder.getView(R.id.tv_invation_date);
                TextView tv_invation_name =  holder.getView(R.id.tv_invation_name);
                TextView tv_invation_state =  holder.getView(R.id.tv_invation_state);
                tv_invation_state.setText(userList.desc);
                tv_invation_name.setText(userList.nickname);
                tv_invation_date.setText(userList.date);
            }
        };
        recy_view_invitaion.setAdapter(adapter);
        scrollview.smoothScrollTo(0,0);
        recy_view_invitaion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (manager != null) {
//                    int lastPosition = manager.findLastVisibleItemPosition();
//                    if (lastPosition + 1 == manager.getItemCount()) {
//                        if (newInvitationPresenter != null) {
//                            newInvitationPresenter.onRefresh();
//                        }
//                    }
//                }
            }
        });
        tv_invitation_copy.setOnClickListener(this);
        tv_please.setOnClickListener(this);
        empty_view.setImageResource(R.mipmap.img_wuguanzhu);
        empty_view.setTextPaddingTop(10);
        empty_view.setText("您还没有邀请哦");
        empty_view.setBackgroundColor(getColorResouce(R.color.transparent));
        newInvitationPresenter.getUserList(true);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewInvitationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_invitation_copy:
                if(!TextUtils.isEmpty(code)){
                    Common.copyText(this,code);
                    Common.staticToastAct(this,"复制成功");
                }
                break;
            case R.id.tv_please:
                newInvitationPresenter.getNewUserShareInfo();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        shareInfoParam.shareLink =baseEntity.data.link;
        shareInfoParam.title =baseEntity.data.title;
        shareInfoParam.desc =baseEntity.data.content;
        shareInfoParam.img =baseEntity.data.imgdefalt;
        shareInfoParam.special_img_url =baseEntity.data.imgdefalt;
        shareGoodDialogUtil.shareGoodDialog(shareInfoParam,false,false);
    }

    @Override
    public void shareSuccess(String blogId, String goodsId) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void refreshFinish(List<InviteLogUserEntity.UserList> userLists, InviteLogUserEntity inviteLogUserEntity,int currentPage) {
        try{
            if (!TextUtils.isEmpty(inviteLogUserEntity.code)) {
                tv_my_invitaion.setText("我的邀请码：" + inviteLogUserEntity.code);
                this.code  = inviteLogUserEntity.code;
            }
            if (!TextUtils.isEmpty(inviteLogUserEntity.money)) {
                tv_show_price.setText("立赚" + inviteLogUserEntity.money + "元");
                tv_show_desc.setText(HighLightKeyWordUtil.getHighLightKeyWord(getColorResouce(R.color.pink_color),
                        "好友成为顺联动力用户，并完成首单，你就可以获得" + inviteLogUserEntity.money + "元", inviteLogUserEntity.money));
            }
            if (!TextUtils.isEmpty(inviteLogUserEntity.prize)) {
                tv_total_price.setText(getStringResouce(R.string.common_yuan) + inviteLogUserEntity.prize);
            }
            if(userLists==null||userLists.size()==0&&currentPage==1){
                recy_view_invitaion.setVisibility(View.GONE);
                empty_view.setVisibility(View.VISIBLE);
                empty_view.setButtonText("");
            }else {
                recy_view_invitaion.setVisibility(View.VISIBLE);
                empty_view.setVisibility(View.GONE);
                invitationData.addAll(userLists);
                adapter.notifyDataSetChanged();
                scrollview.smoothScrollTo(0,0);
            }
        }catch (Exception e){

        }
    }
}
