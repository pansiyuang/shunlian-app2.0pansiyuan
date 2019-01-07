package com.shunlian.app.ui.new_user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
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
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.UserPaySuccessEvent;
import com.shunlian.app.presenter.NewUserPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.UserBuyGoodsDialog;
import com.shunlian.app.view.INewUserPageView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.MyKanner;
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

public class NewInvitationActivity extends BaseActivity {
    private SimpleRecyclerAdapter adapter;

    @BindView(R.id.recy_view_invitaion)
    RecyclerView recy_view_invitaion;

    private List<String> invitationData;
    @Override
    protected int getLayoutId() {
        return R.layout.act_user_invitation_layout;
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        invitationData = new ArrayList<>();
        for (int i=0;i<40;i++){
            invitationData.add(i+"");
        }
        recy_view_invitaion.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleRecyclerAdapter<String>(this, R.layout.item_tag_layout, this.invitationData) {
            @Override
            public void convert(SimpleViewHolder holder, String  goods, int position) {
              TextView textView =  holder.getView(R.id.tv_history_tag);
                textView.setText(goods+"信息");
            }
        };
        recy_view_invitaion.setAdapter(adapter);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewInvitationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }


}
