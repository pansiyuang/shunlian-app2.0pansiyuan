package com.shunlian.app.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FuWuAdapter;
import com.shunlian.app.adapter.ShangAdapter;
import com.shunlian.app.adapter.ZiChanAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.ui.MessageActivity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PersonalcenterPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.qr_code.QrCodeAct;
import com.shunlian.app.ui.returns_order.RefundAfterSaleAct;
import com.shunlian.app.ui.setting.SettingAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPersonalView;
import com.shunlian.app.widget.CompileScrollView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 个人中心页面
 */

public class NewPersonalCenterFrag extends BaseFragment implements IPersonalView, View.OnClickListener, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.view_bg)
    View view_bg;
    @BindView(R.id.csv_out)
    CompileScrollView csv_out;
    @BindView(R.id.rLayout_title)
    MyRelativeLayout rLayout_title;
    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;
    @BindView(R.id.ntv_grow)
    NewTextView ntv_grow;
    @BindView(R.id.ntv_name)
    NewTextView ntv_name;
    @BindView(R.id.ntv_yue)
    NewTextView ntv_yue;
    @BindView(R.id.ntv_title)
    NewTextView ntv_title;
    @BindView(R.id.ntv_left)
    NewTextView ntv_left;
    @BindView(R.id.ntv_zhifu)
    NewTextView ntv_zhifu;
    @BindView(R.id.ntv_keti)
    NewTextView ntv_keti;
    @BindView(R.id.ntv_dai)
    NewTextView ntv_dai;
    @BindView(R.id.ntv_notice)
    NewTextView ntv_notice;
    @BindView(R.id.ntv_copy)
    NewTextView ntv_copy;
    @BindView(R.id.ntv_yaoqing)
    NewTextView ntv_yaoqing;
    @BindView(R.id.ntv_desc)
    NewTextView ntv_desc;
    @BindView(R.id.miv_avar)
    MyImageView miv_avar;
    @BindView(R.id.miv_level)
    MyImageView miv_level;
    @BindView(R.id.miv_pluss)
    MyImageView miv_pluss;
    @BindView(R.id.miv_kefu)
    MyImageView miv_kefu;
    @BindView(R.id.miv_levels)
    MyImageView miv_levels;
    @BindView(R.id.miv_shezhi)
    MyImageView miv_shezhi;
    @BindView(R.id.miv_huiyuan)
    MyImageView miv_huiyuan;
    @BindView(R.id.rl_more)
    MyRelativeLayout rl_more;
    @BindView(R.id.rv_zichan)
    RecyclerView rv_zichan;
    @BindView(R.id.rv_fuwu)
    RecyclerView rv_fuwu;
    @BindView(R.id.rv_shang)
    RecyclerView rv_shang;
    @BindView(R.id.kanner)
    MyKanner kanner;
    @BindView(R.id.miv_isShow_data)
    MyImageView miv_isShow_data;
    @BindView(R.id.miv_yaoqing)
    MyImageView miv_yaoqing;
    @BindView(R.id.ntv_quanbu)
    NewTextView ntv_quanbu;
    @BindView(R.id.mllayout_daifukuan)
    MyLinearLayout mllayout_daifukuan;
    @BindView(R.id.mllayout_daishouhuo)
    MyLinearLayout mllayout_daishouhuo;
    @BindView(R.id.mllayout_daifahuo)
    MyLinearLayout mllayout_daifahuo;
    @BindView(R.id.mllayout_daipingjia)
    MyLinearLayout mllayout_daipingjia;
    @BindView(R.id.mllayout_shouhuo)
    MyLinearLayout mllayout_shouhuo;
    public PersonalcenterPresenter personalcenterPresenter;
    private PersonalcenterEntity personalcenterEntity;
    private String invite_code;
    private MessageCountManager messageCountManager;
    private PersonalcenterEntity.MyAssets myAssets;
    private boolean isShowData = true;
    public final static String ASTERISK = "****";
    public final static String KEY = "person_isShow";
    private String managerUrl;

    //    private Timer outTimer;
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_mine_new, container, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(tv_msg_count);

    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(tv_msg_count);

    }


    @Override
    public void onResume() {
        if (!isHidden()) {
            getPersonalcenterData();
        }
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(baseContext);
            if (messageCountManager.isLoad()) {
                messageCountManager.setTextCount(tv_msg_count);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            ImmersionBar.with(this).fitsSystemWindows(true)
//                    .statusBarColor(R.color.white)
//                    .statusBarDarkFont(true, 0.2f)
//                    .init();
            ImmersionBar.with(this).titleBar(rLayout_title, false).init();
        }
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).titleBar(rLayout_title, false).init();
        view_bg.setAlpha(0);
        personalcenterPresenter = new PersonalcenterPresenter(baseContext, this);
        int picWidth = Common.getScreenWidth((Activity) baseActivity) - TransformUtil.dip2px(baseActivity, 26);
        int height = ((picWidth/2) * 80)/ 165;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) miv_yaoqing.getLayoutParams();
        params.height=height;
        params.width=picWidth/2;
        RelativeLayout.LayoutParams paramss = (RelativeLayout.LayoutParams) miv_huiyuan.getLayoutParams();
        paramss.height=height;
        paramss.width=picWidth/2;
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initListener() {
        super.initListener();
        ntv_yaoqing.setOnClickListener(this);
        ntv_copy.setOnClickListener(this);
        miv_yaoqing.setOnClickListener(this);
        miv_shezhi.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        ntv_quanbu.setOnClickListener(this);
        mllayout_daifukuan.setOnClickListener(this);
        mllayout_daishouhuo.setOnClickListener(this);
        mllayout_daifahuo.setOnClickListener(this);
        mllayout_daipingjia.setOnClickListener(this);
        mllayout_shouhuo.setOnClickListener(this);
        csv_out.setOnScrollListener(new CompileScrollView.OnScrollListener() {
            @Override
//            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
            public void scrollCallBack(int y, int oldy) {
                if (y > 188) {
                    view_bg.setAlpha(1);
                } else if (y > 0) {
                    float alpha = ((float) y) / 188;
                    view_bg.setAlpha(alpha);
                } else {
                    view_bg.setAlpha(0);
                    csv_out.setFocusable(false);
                }
            }
        });
    }


    public void getPersonalcenterData() {
        if (personalcenterPresenter != null && !MyOnClickListener.isFastRequest()) {
            personalcenterPresenter.getApiData();
        }
    }

    private boolean equals(String avatar, String avatar1) {
        if (!isEmpty(avatar) && avatar.equals(avatar1)) {
            return true;
        }
        return false;
    }

    @Override
    public void getApiData(PersonalcenterEntity personalcenterEntity) {
        SharedPrefUtil.saveSharedUserString("plus_role", personalcenterEntity.plus_role);
        this.personalcenterEntity = personalcenterEntity;
        String avatar = SharedPrefUtil.getSharedUserString("personal_avatar", "null");
        if (!equals(avatar, personalcenterEntity.avatar) || miv_avar.getDrawable() == null) {
            SharedPrefUtil.saveSharedUserString("personal_avatar", personalcenterEntity.avatar);
            GlideUtils.getInstance().loadCircleAvar(baseContext, miv_avar, personalcenterEntity.avatar);
        }
        if (!isEmpty(personalcenterEntity.plus_expire_time))
            GlideUtils.getInstance().loadCircleAvar(baseContext, miv_pluss, personalcenterEntity.plus_expire_time);
        if (!isEmpty(personalcenterEntity.level)) {
            Bitmap bitmap = TransformUtil.convertNewVIP(getActivity(), personalcenterEntity.level);
            miv_level.setImageBitmap(bitmap);
            miv_level.setVisibility(View.VISIBLE);
        } else {
            miv_level.setVisibility(View.GONE);
        }
        ntv_grow.setText("成长值\n"+personalcenterEntity.grow_num);
        miv_levels.setVisibility(View.GONE);
        if (!isEmpty(personalcenterEntity.plus_role)) {
            int level = Integer.parseInt(personalcenterEntity.plus_role);
            if (level > 0)
                miv_levels.setVisibility(View.VISIBLE);
            if (level == 1) {
                miv_levels.setImageResource(R.mipmap.img_plus_phb_dianzhu);
            } else if (level == 2) {
                miv_levels.setImageResource(R.mipmap.img_plus_phb_zhuguan);
            } else {
                miv_levels.setImageResource(R.mipmap.img_plus_phb_jingli);
            }
        }
        ntv_name.setText(personalcenterEntity.nickname);
        invite_code = personalcenterEntity.invite_code;
        ntv_yaoqing.setText("邀请码：" + invite_code);
        ntv_desc.setText(personalcenterEntity.plus_meg);
        ntv_title.setText(personalcenterEntity.team_sales);
        if (!isEmpty(personalcenterEntity.diff)&&Float.parseFloat(personalcenterEntity.diff)>0){
            ntv_left.setText("销售额还差"+personalcenterEntity.diff+"元即可获得奖励>");
        }else {
            ntv_left.setText("查看本月销售业绩>");
        }
        ntv_zhifu.setText(personalcenterEntity.zhifu);
        ntv_keti.setText(personalcenterEntity.all_sl_income);
        ntv_dai.setText(personalcenterEntity.estimateProfit);
        ntv_notice.setText(personalcenterEntity.notice);
         myAssets=personalcenterEntity.myAssets;
         if (myAssets==null)
             return;
        if (myAssets.balance!=null&&!isEmpty(myAssets.balance.title)){
            ntv_yue.setText(myAssets.balance.title+">");
            ntv_yue.setVisibility(View.VISIBLE);
        }else {
            ntv_yue.setVisibility(View.GONE);
        }
        isShowData = SharedPrefUtil.getCacheSharedPrfBoolean(KEY, true);
        changeState();
        managerUrl = personalcenterEntity.son_manage_url;
        if (isEmpty(myAssets.items)){
            rv_zichan.setVisibility(View.GONE);
        }else {
            rv_zichan.setVisibility(View.VISIBLE);
            ZiChanAdapter ziChanAdapter=new ZiChanAdapter(baseActivity,false,myAssets.items);
            rv_zichan.setLayoutManager(new GridLayoutManager(baseActivity,3));
            rv_zichan.setAdapter(ziChanAdapter);
            ziChanAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Common.goGoGo(baseActivity,myAssets.items.get(position).url.type,myAssets.items.get(position).url.item_id);
                }
            });
        }
        PersonalcenterEntity.MyService myService=personalcenterEntity.myService;
        if (isEmpty(myService.items)){
            rv_fuwu.setVisibility(View.GONE);
        }else {
            rv_fuwu.setVisibility(View.VISIBLE);
            FuWuAdapter ziChanAdapter=new FuWuAdapter(baseActivity,false,myService.items);
            rv_fuwu.setLayoutManager(new GridLayoutManager(baseActivity,4));
            rv_fuwu.setAdapter(ziChanAdapter);
            ziChanAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Common.goGoGo(baseActivity,myService.items.get(position).url.type,myService.items.get(position).url.item_id);
                }
            });
        }
        PersonalcenterEntity.OursSchool oursSchool=personalcenterEntity.oursSchool;
        if (isEmpty(oursSchool.items)){
            rv_shang.setVisibility(View.GONE);
        }else {
            rv_shang.setVisibility(View.VISIBLE);
            ShangAdapter ziChanAdapter=new ShangAdapter(baseActivity,false,oursSchool.items);
            rv_shang.setLayoutManager(new GridLayoutManager(baseActivity,3));
            rv_shang.setAdapter(ziChanAdapter);
            ziChanAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Common.goGoGo(baseActivity,oursSchool.items.get(position).url.type,oursSchool.items.get(position).url.item_id);
                }
            });
        }
        if (myService.banner_list != null && myService.banner_list.size() > 0) {
            kanner.setVisibility(View.VISIBLE);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < myService.banner_list.size(); i++) {
                strings.add(myService.banner_list.get(i).img);
                if (i >= myService.banner_list.size() - 1) {
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                        @Override
                        public void onItemClick(int position) {
                            Common.goGoGo(baseActivity, myService.banner_list.get(position).link.type, myService.banner_list.get(position).link.item_id);
                        }
                    });
                }
            }
        } else {
            kanner.setVisibility(View.GONE);
        }
    }



    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.ntv_yaoqing:
            case R.id.ntv_copy:
                Common.copyText(getActivity(), invite_code);
                //点击查看特权
                break;
            case R.id.miv_shezhi:
                SettingAct.startAct(baseContext);
                break;
            case R.id.ntv_quanbu:
                MyOrderAct.startAct(baseContext, 1);
                break;
            case R.id.rl_more:
                MessageActivity.startAct(baseActivity);
                break;
            case R.id.miv_kefu:
                MessageActivity.startAct(baseActivity);
                break;
            case R.id.ntv_yue:
                Common.goGoGo(baseActivity,myAssets.balance.url.type,myAssets.balance.url.item_id);
                break;
            case R.id.miv_yaoqing:
                QrCodeAct.startAct(baseContext, managerUrl);
                break;
            case R.id.mllayout_daifukuan:
                MyOrderAct.startAct(baseContext, 2);
                break;
            case R.id.mllayout_daishouhuo:
                MyOrderAct.startAct(baseContext, 4);
                break;
            case R.id.mllayout_daifahuo:
                MyOrderAct.startAct(baseContext, 3);
                break;
            case R.id.mllayout_daipingjia:
                MyOrderAct.startAct(baseContext, 5);
                break;
            case R.id.mllayout_shouhuo:
                RefundAfterSaleAct.startAct(baseContext);
                break;
        }
    }
    public SpannableStringBuilder formatNumber(String number) {
        if (isEmpty(number)) return new SpannableStringBuilder("");
        char[] chars = number.toCharArray();
        return Common.changeTextSize(number, String.valueOf(chars[chars.length - 1]), 11);
    }

    private void changeState() {
        miv_isShow_data.setImageResource(!isShowData ? R.mipmap.icon_eyes_opens : R.mipmap.icon_eyes_closes);
        if (personalcenterEntity != null) {
            ntv_title.setText(!isShowData ? ASTERISK : personalcenterEntity.team_sales);
            ntv_zhifu.setText(!isShowData ? ASTERISK : personalcenterEntity.zhifu);
            ntv_keti.setText(!isShowData ? ASTERISK : personalcenterEntity.all_sl_income);
            ntv_dai.setText(!isShowData ? ASTERISK : personalcenterEntity.estimateProfit);
        }
    }

    @OnClick(R.id.miv_isShow_data)
    public void isShowData() {
        isShowData = !isShowData;
        changeState();
        SharedPrefUtil.saveCacheSharedPrfBoolean(KEY, isShowData);
//        TaskCenterAct.startAct(baseActivity);
    }

    @Override
    public void showFailureView(int request_code) {
    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    @Override
    public void OnLoadFail() {

    }
}
