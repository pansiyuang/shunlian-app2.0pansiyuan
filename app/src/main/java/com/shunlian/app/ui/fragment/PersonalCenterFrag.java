package com.shunlian.app.ui.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.HelpArticleAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.HelpcenterIndexEntity;
import com.shunlian.app.bean.MemberTeacherEntity;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.ui.MessageActivity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PersonalcenterPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.balance.BalanceMainAct;
import com.shunlian.app.ui.collection.MyCollectionAct;
import com.shunlian.app.ui.coupon.CouponListAct;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.help.HelpClassAct;
import com.shunlian.app.ui.help.HelpOneAct;
import com.shunlian.app.ui.my_profit.MyProfitAct;
import com.shunlian.app.ui.myself_store.MyLittleStoreActivity;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.qr_code.QrCodeAct;
import com.shunlian.app.ui.returns_order.RefundAfterSaleAct;
import com.shunlian.app.ui.sale_data.SaleDataAct;
import com.shunlian.app.ui.sale_rank.SaleRankAct;
import com.shunlian.app.ui.setting.SettingAct;
import com.shunlian.app.ui.sign.SignInAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.MHorItemDecoration;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPersonalView;
import com.shunlian.app.widget.CompileScrollView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedRingHeader;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 个人中心页面
 */

public class PersonalCenterFrag extends BaseFragment implements IPersonalView, View.OnClickListener, MessageCountManager.OnGetMessageListener {

    public final static String ASTERISK = "****";
    public final static String KEY = "person_isShow";
    public PersonalcenterPresenter personalcenterPresenter;
    @BindView(R.id.miv_before)
    MyImageView miv_before;
    @BindView(R.id.miv_equal)
    MyImageView miv_equal;
    @BindView(R.id.miv_equals)
    MyImageView miv_equals;
    @BindView(R.id.miv_mid)
    MyImageView miv_mid;
    @BindView(R.id.miv_after)
    MyImageView miv_after;
    @BindView(R.id.mtv_before)
    MyTextView mtv_before;
    @BindView(R.id.mtv_chakanpaihang)
    MyTextView mtv_chakanpaihang;
    @BindView(R.id.mtv_befores)
    MyTextView mtv_befores;
    @BindView(R.id.mtv_mid)
    MyTextView mtv_mid;
    @BindView(R.id.mtv_mids)
    MyTextView mtv_mids;
    @BindView(R.id.mtv_after)
    MyTextView mtv_after;
    @BindView(R.id.mtv_afters)
    MyTextView mtv_afters;
    @BindView(R.id.mtv_qiandao)
    MyTextView mtv_qiandao;
    @BindView(R.id.mtv_name)
    MyTextView mtv_name;
    @BindView(R.id.miv_level)
    MyImageView miv_level;
    @BindView(R.id.miv_entryL)
    MyImageView miv_entryL;
    @BindView(R.id.miv_entryR)
    MyImageView miv_entryR;
    @BindView(R.id.miv_avar)
    MyImageView miv_avar;
    @BindView(R.id.mtv_yaoqingma)
    MyTextView mtv_yaoqingma;
    @BindView(R.id.mtv_yue)
    MyTextView mtv_yue;
    @BindView(R.id.mtv_youhuiquan)
    MyTextView mtv_youhuiquan;
    @BindView(R.id.mtv_donglizhishu)
    MyTextView mtv_donglizhishu;
    @BindView(R.id.mtv_xiaoshou)
    MyTextView mtv_xiaoshou;
    @BindView(R.id.mtv_chakan)
    MyTextView mtv_chakan;
    @BindView(R.id.mtv_shangping)
    MyTextView mtv_shangping;
    @BindView(R.id.mtv_dianpu)
    MyTextView mtv_dianpu;
    @BindView(R.id.mtv_neirong)
    MyTextView mtv_neirong;
    @BindView(R.id.mtv_zuji)
    MyTextView mtv_zuji;
    @BindView(R.id.mtv_equal)
    MyTextView mtv_equal;
    @BindView(R.id.mtv_equals)
    MyTextView mtv_equals;
    @BindView(R.id.mllayout_quanbu)
    MyLinearLayout mllayout_quanbu;
    @BindView(R.id.mllayout_huiyuanguanli)
    MyLinearLayout mllayout_huiyuanguanli;
    @BindView(R.id.mllayout_huiyuandingdan)
    MyLinearLayout mllayout_huiyuandingdan;
    @BindView(R.id.mllayout_guanfangkefu)
    MyLinearLayout mllayout_guanfangkefu;
    @BindView(R.id.mllayout_daifukuan)
    MyLinearLayout mllayout_daifukuan;
    @BindView(R.id.mlLayout_member)
    MyLinearLayout mlLayout_member;
    @BindView(R.id.mllayout_daishouhuo)
    MyLinearLayout mllayout_daishouhuo;
    @BindView(R.id.mllayout_paihang)
    MyLinearLayout mllayout_paihang;
    @BindView(R.id.mllayout_daifahuo)
    MyLinearLayout mllayout_daifahuo;
    @BindView(R.id.mllayout_daipingjia)
    MyLinearLayout mllayout_daipingjia;
    @BindView(R.id.mllayout_shangping)
    MyLinearLayout mllayout_shangping;
    @BindView(R.id.mllayout_dianpu)
    MyLinearLayout mllayout_dianpu;
    @BindView(R.id.mllayout_neirong)
    MyLinearLayout mllayout_neirong;
    @BindView(R.id.mllayout_zuji)
    MyLinearLayout mllayout_zuji;
    @BindView(R.id.miv_levels)
    MyImageView miv_levels;
    @BindView(R.id.miv_jingli)
    MyImageView miv_jingli;
    @BindView(R.id.miv_isShow_data)
    MyImageView miv_isShow_data;
    @BindView(R.id.miv_shezhi)
    MyImageView miv_shezhi;
    @BindView(R.id.rl_more)
    MyRelativeLayout rl_more;
    @BindView(R.id.mrlayout_zidingyi)
    MyRelativeLayout mrlayout_zidingyi;
    @BindView(R.id.mllayout_shouhuo)
    MyLinearLayout mllayout_shouhuo;
    @BindView(R.id.mrlayout_yaoqing)
    MyRelativeLayout mrlayout_yaoqing;
    @BindView(R.id.mllayout_yue)
    MyLinearLayout mllayout_yue;
    @BindView(R.id.mllayout_mid)
    MyLinearLayout mllayout_mid;
    @BindView(R.id.mllayout_youhuiquan)
    MyLinearLayout mllayout_youhuiquan;
    @BindView(R.id.mllayout_dongli)
    MyLinearLayout mllayout_dongli;
    @BindView(R.id.mllayout_xiaoshou)
    MyLinearLayout mllayout_xiaoshou;
    @BindView(R.id.mtv_refundNum)
    MyTextView mtv_refundNum;
    @BindView(R.id.mtv_remarkNum)
    MyTextView mtv_remarkNum;
    @BindView(R.id.mtv_sendNum)
    MyTextView mtv_sendNum;
    @BindView(R.id.mtv_receiveNum)
    MyTextView mtv_receiveNum;
    @BindView(R.id.mtv_hint)
    MyTextView mtv_hint;
    //    @BindView(R.id.seekbar_grow)
//    SeekBar seekbar_grow;
    @BindView(R.id.mtv_payNum)
    MyTextView mtv_payNum;
    //    @BindView(R.id.refreshview)
//    RingRefreshView refreshview;
    @BindView(R.id.view_bg)
    View view_bg;
    @BindView(R.id.csv_out)
    CompileScrollView csv_out;
    @BindView(R.id.rv_article)
    RecyclerView rv_article;
    @BindView(R.id.rLayout_title)
    MyRelativeLayout rLayout_title;
    @BindView(R.id.mrlayout_paihang)
    MyRelativeLayout mrlayout_paihang;
    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;
    @BindView(R.id.mrlayout_plus)
    MyRelativeLayout mrlayout_plus;
    @BindView(R.id.mllayout_entry)
    MyLinearLayout mllayout_entry;
    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;
    private MessageCountManager messageCountManager;
    private HelpArticleAdapter helpArticleAdapter;
    private String managerUrl, orderUrl;
    private boolean isShowData = true;
    private PersonalcenterEntity personalcenterEntity;
    private PromptDialog promptDialog;
    //    private MainActivity mainActivity;
    private String one, two, three, four;
    private int flag = 0;
    private String invite_code;

    //    private Timer outTimer;
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_mine, container, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(tv_msg_count);

    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(tv_msg_count);

    }

    @OnClick(R.id.miv_isShow_data)
    public void isShowData() {
        isShowData = !isShowData;
        changeState();
        SharedPrefUtil.saveCacheSharedPrfBoolean(KEY, isShowData);
//        TaskCenterAct.startAct(baseActivity);
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
//        ImmersionBar.with(this).fitsSystemWindows(true)
//                .statusBarColor(R.color.white)
//                .statusBarDarkFont(true, 0.2f)
//                .init();
        //新增下拉刷新
//        mainActivity = (MainActivity) getActivity();
        NestedRingHeader header = new NestedRingHeader(getContext());
        lay_refresh.setRefreshHeaderView(header);

        EventBus.getDefault().register(this);
        ImmersionBar.with(this).titleBar(rLayout_title, false).init();
//        refreshview.setCanRefresh(true);
//        refreshview.setCanLoad(false);
        view_bg.setAlpha(0);
        personalcenterPresenter = new PersonalcenterPresenter(baseContext, this);
        if ("1".equals(SharedPrefUtil.getCacheSharedPrf("is_open", ""))) {
            mrlayout_plus.setVisibility(View.VISIBLE);
        } else {
            mrlayout_plus.setVisibility(View.GONE);
        }
    }

    private void changeState() {
        miv_isShow_data.setImageResource(!isShowData ? R.mipmap.img_plus_guanbi_n : R.mipmap.img_guanbi_h);
        if (personalcenterEntity != null) {
            mtv_yue.setText(!isShowData ? ASTERISK : formatNumber(personalcenterEntity.balance));
            mtv_youhuiquan.setText(!isShowData ? ASTERISK : personalcenterEntity.coupon_num);
            mtv_donglizhishu.setText(!isShowData ? ASTERISK : formatNumber(personalcenterEntity.all_sl_income));
            mtv_xiaoshou.setText(!isShowData ? ASTERISK : formatNumber(personalcenterEntity.team_sales));

//            mtv_yue.setText(!isShowData ? ASTERISK : formatNumber(one));
//            mtv_youhuiquan.setText(!isShowData ? ASTERISK : two);
//            mtv_donglizhishu.setText(!isShowData ? ASTERISK : formatNumber(three));
//            mtv_xiaoshou.setText(!isShowData ? ASTERISK : formatNumber(four));
        }
    }

    public SpannableStringBuilder formatNumber(String number) {
        /*if (isEmpty(number)) {
            return Common.changeTextSize(
                    "数据异常", "数据", 11);
        }
        try {
            double sale = Double.parseDouble(number);
            if (sale < 10000) {
                if (number.contains(".")) {
                    if (sale < 100 && number.length() - number.indexOf(".") > 2) {
                        number = number.substring(0, number.indexOf(".") + 3);
                    } else if (sale < 1000 && number.length() - number.indexOf(".") > 1) {
                        number = number.substring(0, number.indexOf(".") + 2);
                    } else {
                        number = number.substring(0, number.indexOf("."));
                    }
                }
                return Common.changeTextSize(
                        number + getStringResouce(R.string.common_yuans), getStringResouce(R.string.common_yuans), 11);
            } else {
                double sales = sale * 0.0001;
                String numbers = String.valueOf(sales);
                if (numbers.contains(".")) {
                    if (sales < 100 && numbers.length() - numbers.indexOf(".") > 2) {
                        number = numbers.substring(0, numbers.indexOf(".") + 3);
                    } else if (sales < 1000 && numbers.length() - numbers.indexOf(".") > 1) {
                        number = numbers.substring(0, numbers.indexOf(".") + 2);
                    } else {
                        number = numbers.substring(0, numbers.indexOf("."));
                    }
                }
                return Common.changeTextSize(
                        number + getStringResouce(R.string.common_wan), getStringResouce(R.string.common_wan), 11);
            }
        } catch (Exception e) {
            return Common.changeTextSize(
                    "数据异常", "数据", 11);
        }*/
        if (isEmpty(number)) return new SpannableStringBuilder("");
        char[] chars = number.toCharArray();
        return Common.changeTextSize(number, String.valueOf(chars[chars.length - 1]), 11);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_qiandao.setOnClickListener(this);
        mtv_yaoqingma.setOnClickListener(this);
        mllayout_quanbu.setOnClickListener(this);
        miv_entryR.setOnClickListener(this);
        miv_entryL.setOnClickListener(this);
        mllayout_guanfangkefu.setOnClickListener(this);
        mllayout_huiyuanguanli.setOnClickListener(this);
        mllayout_huiyuandingdan.setOnClickListener(this);
        mllayout_daifukuan.setOnClickListener(this);
        mllayout_daishouhuo.setOnClickListener(this);
        mllayout_daifahuo.setOnClickListener(this);
        mllayout_daipingjia.setOnClickListener(this);
        mllayout_shangping.setOnClickListener(this);
        mllayout_dianpu.setOnClickListener(this);
        mllayout_neirong.setOnClickListener(this);
        mllayout_zuji.setOnClickListener(this);
        miv_shezhi.setOnClickListener(this);
        mtv_chakanpaihang.setOnClickListener(this);
        mrlayout_yaoqing.setOnClickListener(this);
//        mtv_chakan.setOnClickListener(this);
        mrlayout_plus.setOnClickListener(this);
        mrlayout_zidingyi.setOnClickListener(this);
        mrlayout_yaoqing.setOnClickListener(this);
        mllayout_yue.setOnClickListener(this);
        mllayout_youhuiquan.setOnClickListener(this);
        mllayout_dongli.setOnClickListener(this);
        mllayout_xiaoshou.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        mllayout_shouhuo.setOnClickListener(this);
        view_bg.setOnClickListener(this);
        csv_out.setOnScrollListener(new CompileScrollView.OnScrollListener() {
            @Override
//            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
            public void scrollCallBack(int y, int oldy) {
                if (y > 30) {
                    view_bg.setAlpha(1);
                } else if (y > 0) {
                    float alpha = ((float) y) / 30;
                    view_bg.setAlpha(alpha);
                } else {
                    view_bg.setAlpha(0);
                    csv_out.setFocusable(false);
                }
            }
        });
//        refreshview.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                personalcenterPresenter.getApiData();
//            }
//
//            @Override
//            public void onLoadMore() {
//            }
//        });
        lay_refresh.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh() {
                /*switch (flag) {
                    case 0:
                        one = "0.99";
                        two = "1";
                        three = "99.99";
                        four = "199.";
                        break;
                    case 1:
                        one = "999.9";
                        two = "999.999";
                        three = "9999.55";
                        four = "99999.999";
                        break;
                    case 2:
                        one = "10000";
                        two = "100";
                        three = "1000";
                        four = "99999999";
                        break;
                    case 3:
                        one = "999999.999";
                        two = "9999999.555";
                        three = "99999999";
                        four = "9999999";
                        four = "9999999";
                        break;
                    case 4:
                        one = "999999.";
                        two = "9999999.";
                        three = "99999999.";
                        four = "9999999..";
                        break;
                    default:
                        flag = -1;
                        break;
                }
                flag++;*/
                personalcenterPresenter.getApiData();
            }
        });
    }

    public void initHintDialog() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(baseActivity);
        }
        promptDialog.setSureAndCancleListener(getStringResouce(R.string.personal_ninhaibushi), getStringResouce(R.string.personal_goumai), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
                H5X5Act.startAct(baseContext, SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD), H5X5Act.MODE_SONIC);
            }
        }, getStringResouce(R.string.errcode_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        }).show();
    }

    public void getPersonalcenterData() {
        if (personalcenterPresenter != null && !MyOnClickListener.isFastRequest()) {
            personalcenterPresenter.getApiData();
        }
    }

    @Override
    public void getApiData(PersonalcenterEntity personalcenterEntity) {
        SharedPrefUtil.saveSharedUserString("plus_role", personalcenterEntity.plus_role);
        this.personalcenterEntity = personalcenterEntity;
        if (!isEmpty(personalcenterEntity.invite_code))
            SharedPrefUtil.saveSharedUserString("invite_code", personalcenterEntity.invite_code);
        if (!isEmpty(personalcenterEntity.game_door)) {
            int picWidth = Common.getScreenWidth(baseActivity) - TransformUtil.dip2px(baseActivity, 20);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWidth, picWidth * 100 / 360);
            layoutParams.setMargins(0, TransformUtil.dip2px(baseActivity, 10), 0, 0);
            mllayout_entry.setLayoutParams(layoutParams);
            if (personalcenterEntity.game_door.size() == 2) {
                GlideUtils.getInstance().loadImageChang(baseActivity, miv_entryL, personalcenterEntity.game_door.get(0).thumb);
                GlideUtils.getInstance().loadImageChang(baseActivity, miv_entryR, personalcenterEntity.game_door.get(1).thumb);
                miv_entryR.setVisibility(View.VISIBLE);
            } else {
                miv_entryR.setVisibility(View.GONE);
                GlideUtils.getInstance().loadImageChang(baseActivity, miv_entryL, personalcenterEntity.game_door.get(0).thumb);
            }
            mllayout_entry.setVisibility(View.VISIBLE);
        } else {
            mllayout_entry.setVisibility(View.GONE);
        }
        if (!isEmpty(personalcenterEntity.bcm_role) && "1".equals(personalcenterEntity.bcm_role)) {
            miv_jingli.setVisibility(View.VISIBLE);
        } else {
            miv_jingli.setVisibility(View.GONE);
        }
        if (isEmpty(personalcenterEntity.note)) {
            gone(mtv_hint);
        } else {
            visible(mtv_hint);
            mtv_hint.setText(personalcenterEntity.note);
        }
        if (!isEmpty(personalcenterEntity.balance)) {
            mllayout_yue.setVisibility(View.VISIBLE);
        } else {
            mllayout_yue.setVisibility(View.GONE);
        }
        isShowData = SharedPrefUtil.getCacheSharedPrfBoolean(KEY, true);
        changeState();
        managerUrl = personalcenterEntity.son_manage_url;
        orderUrl = personalcenterEntity.son_order_url;
//        refreshview.stopRefresh(true);
        lay_refresh.setRefreshing(false);

//        refreshview.stopLoadMore(true);
        mtv_name.setText(personalcenterEntity.nickname);
//        int percent = Integer.parseInt(personalcenterEntity.next_level_percent);
        //percent为10的倍数
//        showLevel(10, personalcenterEntity.next_level_info);
        mtv_refundNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.new_refund_num) <= 0) {
            mtv_refundNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.new_refund_num) > 9) {
            mtv_refundNum.setText("9+");
        } else {
            mtv_refundNum.setText(personalcenterEntity.new_refund_num);
        }
        mtv_remarkNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.un_comment_num) <= 0) {
            mtv_remarkNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.un_comment_num) > 9) {
            mtv_remarkNum.setText("9+");
        } else {
            mtv_remarkNum.setText(personalcenterEntity.un_comment_num);
        }
        mtv_sendNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.order_un_send_num) <= 0) {
            mtv_sendNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.order_un_send_num) > 9) {
            mtv_sendNum.setText("9+");
        } else {
            mtv_sendNum.setText(personalcenterEntity.order_un_send_num);
        }
        mtv_receiveNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.order_un_receive_num) <= 0) {
            mtv_receiveNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.order_un_receive_num) > 9) {
            mtv_receiveNum.setText("9+");
        } else {
            mtv_receiveNum.setText(personalcenterEntity.order_un_receive_num);
        }
        mtv_payNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.order_un_pay_num) <= 0) {
            mtv_payNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.order_un_pay_num) > 9) {
            mtv_payNum.setText("9+");
        } else {
            mtv_payNum.setText(personalcenterEntity.order_un_pay_num);
        }
//        seekbar_grow.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return true;
//            }
//        });
        switch (personalcenterEntity.my_rank_code) {
            case "up":
                miv_equal.setImageResource(R.mipmap.icon_personalcenter_shangjiantou);
                SpannableStringBuilder upBuilder = Common.changeColor(personalcenterEntity.my_rank_label + personalcenterEntity.my_rank_info, personalcenterEntity.my_rank_info, getColorResouce(R.color.value_01C269));
                mtv_equal.setText(upBuilder);
                miv_equals.setImageResource(R.mipmap.icon_personalcenter_shangjiantou);
                mtv_equals.setText(upBuilder);
                break;
            case "down":
                SpannableStringBuilder downBuilder = Common.changeColor(personalcenterEntity.my_rank_label + personalcenterEntity.my_rank_info, personalcenterEntity.my_rank_info, getColorResouce(R.color.pink_color));
                mtv_equal.setText(downBuilder);
                miv_equal.setImageResource(R.mipmap.icon_personalcenter_xiajiantou);
                miv_equals.setImageResource(R.mipmap.icon_personalcenter_xiajiantou);
                mtv_equals.setText(downBuilder);
                break;
            default:
                miv_equal.setImageResource(R.mipmap.icon_personalcenter_chiping);
                SpannableStringBuilder defaultBuilder = Common.changeColor(personalcenterEntity.my_rank_label + personalcenterEntity.my_rank_info, personalcenterEntity.my_rank_info, getColorResouce(R.color.value_1C8FE0));
                mtv_equal.setText(defaultBuilder);
                miv_equals.setImageResource(R.mipmap.icon_personalcenter_chiping);
                mtv_equals.setText(defaultBuilder);
                break;
        }
        if (!isEmpty(personalcenterEntity.level)) {
            Bitmap bitmap = TransformUtil.convertNewVIP(getActivity(), personalcenterEntity.level);
            miv_level.setImageBitmap(bitmap);
            miv_level.setVisibility(View.VISIBLE);
        } else {
            miv_level.setVisibility(View.GONE);
        }
        invite_code = personalcenterEntity.invite_code;
        mtv_yaoqingma.setText("邀请码:" + invite_code);

//        switch (personalcenterEntity.role) {
//            case "1":
//                miv_levels.setImageResource(R.mipmap.img_chuangkejingying);
//                break;
//            case "2":
//                miv_levels.setImageResource(R.mipmap.img_jingyingdaoshi);
//                break;
//            default:
//                miv_levels.setVisibility(View.INVISIBLE);
//                break;
//        }
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
        String avatar = SharedPrefUtil.getSharedUserString("personal_avatar", "null");
        if (!equals(avatar, personalcenterEntity.avatar) || miv_avar.getDrawable() == null) {
            SharedPrefUtil.saveSharedUserString("personal_avatar", personalcenterEntity.avatar);
            GlideUtils.getInstance().loadCircleAvar(baseContext, miv_avar, personalcenterEntity.avatar);
        }
        mtv_shangping.setText(personalcenterEntity.goods_fav_num);
        mtv_dianpu.setText(personalcenterEntity.store_fav_num);
        mtv_neirong.setText(personalcenterEntity.article_fav_num);
        mtv_zuji.setText(personalcenterEntity.footermark_fav_num);

        miv_equal.setVisibility(View.INVISIBLE);
        mtv_equal.setVisibility(View.INVISIBLE);
        mtv_befores.setTextColor(getColorResouce(R.color.new_text));
        miv_equals.setVisibility(View.INVISIBLE);
        mtv_equals.setVisibility(View.INVISIBLE);
        mtv_mids.setTextColor(getColorResouce(R.color.new_text));


//        mrlayout_paihang.setVisibility(View.VISIBLE);
//        mllayout_paihang.setVisibility(View.VISIBLE);
        mlLayout_member.setVisibility(View.GONE);
        mllayout_mid.setVisibility(View.GONE);

        if (personalcenterEntity.sl_user_ranks != null) {
            switch (personalcenterEntity.sl_user_ranks.size()) {
                case 1:
                    if (personalcenterEntity.sl_user_ranks.get(0) != null) {
                        if ("1".equals(personalcenterEntity.sl_user_ranks.get(0).is_mine)) {
                            miv_equal.setVisibility(View.VISIBLE);
                            mtv_equal.setVisibility(View.VISIBLE);
                            mtv_befores.setTextColor(getColorResouce(R.color.pink_color));
                        }
                        mtv_before.setText(personalcenterEntity.sl_user_ranks.get(0).nickname);
                        mtv_befores.setText(personalcenterEntity.sl_user_ranks.get(0).sale);
                        GlideUtils.getInstance().loadCircleAvar(baseContext, miv_before, personalcenterEntity.sl_user_ranks.get(0).avatar);
                    }
                    break;
                case 2:
                    mllayout_mid.setVisibility(View.VISIBLE);
                    if (personalcenterEntity.sl_user_ranks.get(0) != null) {
                        if ("1".equals(personalcenterEntity.sl_user_ranks.get(0).is_mine)) {
                            miv_equal.setVisibility(View.VISIBLE);
                            mtv_equal.setVisibility(View.VISIBLE);
                            mtv_befores.setTextColor(getColorResouce(R.color.pink_color));
                        }
                        mtv_before.setText(personalcenterEntity.sl_user_ranks.get(0).nickname);
                        mtv_befores.setText(personalcenterEntity.sl_user_ranks.get(0).sale);
                        GlideUtils.getInstance().loadCircleAvar(baseContext, miv_before, personalcenterEntity.sl_user_ranks.get(0).avatar);
                    }
                    if (personalcenterEntity.sl_user_ranks.get(1) != null) {
                        if ("1".equals(personalcenterEntity.sl_user_ranks.get(1).is_mine)) {
                            miv_equals.setVisibility(View.VISIBLE);
                            mtv_equals.setVisibility(View.VISIBLE);
                            mtv_mids.setTextColor(getColorResouce(R.color.pink_color));
                        }
                        mtv_mid.setText(personalcenterEntity.sl_user_ranks.get(1).nickname);
                        mtv_mids.setText(personalcenterEntity.sl_user_ranks.get(1).sale);
                        GlideUtils.getInstance().loadCircleAvar(baseContext, miv_mid, personalcenterEntity.sl_user_ranks.get(1).avatar);
                    }
                    break;
                case 3:
                    mllayout_mid.setVisibility(View.VISIBLE);
                    mlLayout_member.setVisibility(View.VISIBLE);
                    if (personalcenterEntity.sl_user_ranks.get(0) != null) {
                        mtv_before.setText(personalcenterEntity.sl_user_ranks.get(0).nickname);
                        mtv_befores.setText(personalcenterEntity.sl_user_ranks.get(0).sale);
                        GlideUtils.getInstance().loadCircleAvar(baseContext, miv_before, personalcenterEntity.sl_user_ranks.get(0).avatar);
                    }
                    if (personalcenterEntity.sl_user_ranks.get(1) != null) {
                        miv_equals.setVisibility(View.VISIBLE);
                        mtv_equals.setVisibility(View.VISIBLE);
                        mtv_mids.setTextColor(getColorResouce(R.color.pink_color));
                        mtv_mid.setText(personalcenterEntity.sl_user_ranks.get(1).nickname);
                        mtv_mids.setText(personalcenterEntity.sl_user_ranks.get(1).sale);
                        GlideUtils.getInstance().loadCircleAvar(baseContext, miv_mid, personalcenterEntity.sl_user_ranks.get(1).avatar);
                    }
                    if (personalcenterEntity.sl_user_ranks.get(2) != null) {
                        mtv_after.setText(personalcenterEntity.sl_user_ranks.get(2).nickname);
                        mtv_afters.setText(personalcenterEntity.sl_user_ranks.get(2).sale);
                        GlideUtils.getInstance().loadCircleAvar(baseContext, miv_after, personalcenterEntity.sl_user_ranks.get(2).avatar);
                    }
                    break;
                default:
                    mrlayout_paihang.setVisibility(View.GONE);
                    mllayout_paihang.setVisibility(View.GONE);
                    break;
            }
        }
        if (helpArticleAdapter == null) {
            helpArticleAdapter = new HelpArticleAdapter(getContext(), false, personalcenterEntity.article);
            rv_article.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rv_article.addItemDecoration(new MHorItemDecoration(getContext(), 5, 10, 10));
            rv_article.setAdapter(helpArticleAdapter);
            rv_article.setNestedScrollingEnabled(false);
            helpArticleAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position < 0)
                        return;
                    HelpcenterIndexEntity.ArticleCategory questionCategory = personalcenterEntity.article.get(position);
                    HelpClassAct.startAct(getContext(), questionCategory.id);
                }
            });
        } else {
            helpArticleAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getUserId(String userId) {

    }

    @Override
    public void teacherCodeInfo(MemberTeacherEntity memberTeacherEntity) {

    }


    private boolean equals(String avatar, String avatar1) {
        if (!isEmpty(avatar) && avatar.equals(avatar1)) {
            return true;
        }
        return false;
    }


//    public void showLevel(final int percent, final String next_level_info) {
//        seekbar_grow.setProgress(0);
//        mtv_persent.setText("");
//        final int[] now = {0};
//        if (outTimer != null) {
//            outTimer.cancel();
//        }
//        outTimer = new Timer();
//        outTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (now[0] < percent) {
//                    now[0]++;
//                    seekbar_grow.setProgress(now[0]);
//                } else {
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mtv_persent.setText(next_level_info);
//                        }
//                    });
//                    outTimer.cancel();
//                }
//            }
//        }, 500, percent / 10);
//    }

    public void initDialog(PersonalcenterEntity.Game.Url url) {
        Dialog dialog_hint = new Dialog(baseContext, R.style.Mydialog);
        dialog_hint.setContentView(R.layout.dialog_hint_single);
        MyTextView mtv_sure = (MyTextView) dialog_hint.findViewById(R.id.mtv_sure);
        MyTextView mtv_hint = (MyTextView) dialog_hint.findViewById(R.id.mtv_hint);
        MyLinearLayout mllayout_root = (MyLinearLayout) dialog_hint.findViewById(R.id.mllayout_root);
        mtv_hint.setText(url.msg);
        mtv_sure.setText(url.button);

        int width=Common.getScreenWidth(baseActivity)*300/360;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        mllayout_root.setLayoutParams(params);
        mtv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.goGoGo(baseActivity, url.type,url.item_id);
                dialog_hint.dismiss();
            }
        });
        dialog_hint.show();
    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.mllayout_quanbu:
                MyOrderAct.startAct(baseContext, 1);
                break;
            case R.id.miv_entryL:
                if (!isEmpty(personalcenterEntity.game_door.get(0).url.msg)){
                    initDialog(personalcenterEntity.game_door.get(0).url);
                }else {
                    Common.goGoGo(baseActivity, personalcenterEntity.game_door.get(0).url.type, personalcenterEntity.game_door.get(0).url.item_id);
                }
                break;
            case R.id.miv_entryR:
                if (!isEmpty(personalcenterEntity.game_door.get(1).url.msg)){
                    initDialog(personalcenterEntity.game_door.get(1).url);
                }else {
                    Common.goGoGo(baseActivity, personalcenterEntity.game_door.get(1).url.type, personalcenterEntity.game_door.get(1).url.item_id);
                }
                break;
            case R.id.mtv_chakanpaihang:
//                initDialogs("https://wx.shunliandongli.com/agreement/1",true);
                SaleRankAct.startAct(baseContext);
                break;
            case R.id.mllayout_guanfangkefu:
                HelpOneAct.startAct(baseContext);
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
            case R.id.mllayout_shangping:
                MyCollectionAct.startAct(baseContext, MyCollectionAct.GOODS_FLAG);
                break;
            case R.id.mllayout_dianpu:
                MyCollectionAct.startAct(baseContext, MyCollectionAct.STORE_FLAG);
                break;
            case R.id.mllayout_neirong:
                MyCollectionAct.startAct(baseContext, MyCollectionAct.CONTENT_FLAG);
                break;
            case R.id.mllayout_zuji:
                MyCollectionAct.startAct(baseContext, MyCollectionAct.FOOTPRINT_FLAG);
                break;
            case R.id.miv_shezhi:
                SettingAct.startAct(baseContext);
                break;
            case R.id.mrlayout_yaoqing:
                QrCodeAct.startAct(baseContext, managerUrl);
                break;
            case R.id.mrlayout_zidingyi:
                if ("1".equals(SharedPrefUtil.getCacheSharedPrf("is_open", ""))) {
                    if (Common.isPlus()) {
                        MyLittleStoreActivity.startAct(getActivity());
                    } else {
                        initHintDialog();
                    }
                } else {
                    MyLittleStoreActivity.startAct(getActivity());
                }
                break;
            case R.id.rl_more:
                MessageActivity.startAct(getActivity());
                break;
            case R.id.mllayout_yue:
                Constant.ISBALANCE = true;
                BalanceMainAct.startAct(baseContext, false);
                break;
            case R.id.mllayout_youhuiquan:
                CouponListAct.startAct(baseActivity);
                break;
            case R.id.mllayout_dongli:
                MainActivity.startAct(baseActivity, "personCenter");
//                MyProfitAct.startAct(baseActivity, false);
                break;
            case R.id.mllayout_xiaoshou:
                SaleDataAct.startAct(baseActivity);
                break;
            case R.id.mtv_qiandao:
                SignInAct.startAct(baseContext);
                break;
            case R.id.mllayout_huiyuanguanli:
                H5X5Act.startAct(getContext(), managerUrl, H5X5Act.MODE_SONIC);
                //会员管理
                break;
            case R.id.mllayout_huiyuandingdan:
                H5X5Act.startAct(getContext(), orderUrl, H5X5Act.MODE_SONIC);
                //会员订单
                break;
//            case R.id.mtv_chakan:
            case R.id.mrlayout_plus:
//                EggDetailAct.startAct(getContext());
//                mainActivity.myPlusClick();//old
                if (Common.isPlus()) {
//                    LogUtil.augusLogW("uiui-"+SharedPrefUtil.getCacheSharedPrf("plus_index", Constant.PLUS_ADD));
                    H5X5Act.startAct(baseContext, SharedPrefUtil.getCacheSharedPrf("plus_index", Constant.PLUS_ADD), H5X5Act.MODE_SONIC);
//                    H5X5Act.startAct(baseContext, "https://www.baidu.com", H5Act.MODE_SONIC);
                } else {
//                    LogUtil.augusLogW("uiui-"+SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD));
                    H5X5Act.startAct(baseContext, SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD), H5X5Act.MODE_SONIC);
//                    H5X5Act.startAct(baseContext, "https://www.baidu.com", H5Act.MODE_SONIC);
                }
                //点击查看特权
                break;
            case R.id.mtv_yaoqingma:
                Common.copyText(getActivity(), invite_code);
                //点击查看特权
                break;
        }
    }


    @Override
    public void showFailureView(int request_code) {
//        refreshview.stopRefresh(true);
        lay_refresh.setRefreshing(false);
    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    @Override
    public void OnLoadFail() {

    }
}
