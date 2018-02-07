package com.shunlian.app.ui.fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.shunlian.app.R;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.presenter.PersonalcenterPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.collection.MyCollectionAct;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.returns_order.RefundAfterSaleAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IPersonalView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 个人中心页面
 */

public class PersonalCenterFrag extends BaseFragment implements IPersonalView, View.OnClickListener {
    @BindView(R.id.miv_qiandao)
    MyImageView miv_qiandao;

    @BindView(R.id.miv_before)
    MyImageView miv_before;

    @BindView(R.id.miv_equal)
    MyImageView miv_equal;

    @BindView(R.id.miv_mid)
    MyImageView miv_mid;

    @BindView(R.id.miv_after)
    MyImageView miv_after;

    @BindView(R.id.mtv_before)
    MyTextView mtv_before;

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

    @BindView(R.id.miv_avar)
    MyImageView miv_avar;

    @BindView(R.id.mtv_yaoqingma)
    MyTextView mtv_yaoqingma;

    @BindView(R.id.miv_call)
    MyImageView miv_call;

    @BindView(R.id.mtv_yue)
    MyTextView mtv_yue;

    @BindView(R.id.mtv_youhuiquan)
    MyTextView mtv_youhuiquan;

    @BindView(R.id.mtv_donglizhishu)
    MyTextView mtv_donglizhishu;

    @BindView(R.id.mtv_xiaoshou)
    MyTextView mtv_xiaoshou;

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

    @BindView(R.id.mtv_xiaodianhuiyuan)
    MyTextView mtv_xiaodianhuiyuan;

    @BindView(R.id.mtv_xiaodiandingdan)
    MyTextView mtv_xiaodiandingdan;

    @BindView(R.id.mllayout_quanbu)
    MyLinearLayout mllayout_quanbu;

    @BindView(R.id.mllayout_daifukuan)
    MyLinearLayout mllayout_daifukuan;

    @BindView(R.id.mllayout_daishouhuo)
    MyLinearLayout mllayout_daishouhuo;

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

    @BindView(R.id.mrlayout_xiaodianhuiyuan)
    MyRelativeLayout mrlayout_xiaodianhuiyuan;

    @BindView(R.id.mrlayout_xiaodiandingdan)
    MyRelativeLayout mrlayout_xiaodiandingdan;

    @BindView(R.id.miv_levels)
    MyImageView miv_levels;

    @BindView(R.id.mtv_persent)
    MyTextView mtv_persent;

    @BindView(R.id.mtv_all)
    MyTextView mtv_all;

    @BindView(R.id.seekbar_grow)
    SeekBar seekbar_grow;

    private PersonalcenterPresenter personalcenterPresenter;
    private Timer outTimer;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_mine, container, false);
    }


    @Override
    protected void initData() {
        personalcenterPresenter = new PersonalcenterPresenter(baseContext, this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_qiandao.setOnClickListener(this);
        mtv_qiandao.setOnClickListener(this);
        mllayout_quanbu.setOnClickListener(this);
        mllayout_daifukuan.setOnClickListener(this);
        mllayout_daishouhuo.setOnClickListener(this);
        mllayout_daifahuo.setOnClickListener(this);
        mllayout_daipingjia.setOnClickListener(this);
        mllayout_shangping.setOnClickListener(this);
        mllayout_dianpu.setOnClickListener(this);
        mllayout_neirong.setOnClickListener(this);
        mllayout_zuji.setOnClickListener(this);
        mrlayout_xiaodianhuiyuan.setOnClickListener(this);
        mrlayout_xiaodiandingdan.setOnClickListener(this);
    }

    @Override
    public void getApiData(PersonalcenterEntity personalcenterEntity) {
        mtv_name.setText(personalcenterEntity.nickname);
        mtv_equal.setText(personalcenterEntity.my_rank_info);
        startToast(80);
        mtv_all.setText("升级总积分null");
        seekbar_grow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        switch (personalcenterEntity.level) {
            case "up":
                miv_equal.setImageResource(R.mipmap.icon_personalcenter_shangjiantou);
                break;
            case "down":
                miv_equal.setImageResource(R.mipmap.icon_personalcenter_xiajiantou);
                break;
            default:
                miv_equal.setImageResource(R.mipmap.icon_personalcenter_chiping);
                break;
        }
        switch (personalcenterEntity.level) {
            default:
                miv_level.setImageResource(R.mipmap.v0);
                miv_levels.setImageResource(R.mipmap.img_personalcenter_v1);
                break;
            case "0":
                miv_level.setImageResource(R.mipmap.v0);
                miv_levels.setImageResource(R.mipmap.img_personalcenter_v1);
                break;
            case "1":
                miv_level.setImageResource(R.mipmap.v1);
                miv_levels.setImageResource(R.mipmap.img_personalcenter_v1);
                break;
            case "2":
                miv_level.setImageResource(R.mipmap.v2);
                miv_levels.setImageResource(R.mipmap.img_personalcenter_v2);
                break;
            case "3":
                miv_level.setImageResource(R.mipmap.v3);
                miv_levels.setImageResource(R.mipmap.img_personalcenter_v3);
                break;
            case "4":
                miv_level.setImageResource(R.mipmap.v4);
                miv_levels.setImageResource(R.mipmap.img_personalcenter_v4);
                break;
            case "5":
                miv_level.setImageResource(R.mipmap.v5);
                miv_levels.setImageResource(R.mipmap.img_personalcenter_v5);
                break;
            case "6":
                miv_level.setImageResource(R.mipmap.v6);
                miv_levels.setImageResource(R.mipmap.img_personalcenter_v6);
                break;
        }
        mtv_yaoqingma.setText("邀请码:" + personalcenterEntity.invite_code);
        GlideUtils.getInstance().loadImage(baseContext, miv_call, personalcenterEntity.member_role);
        GlideUtils.getInstance().loadCircleImage(baseContext, miv_avar, personalcenterEntity.avatar);
        mtv_yue.setText(personalcenterEntity.balance);
        mtv_youhuiquan.setText(personalcenterEntity.coupon_num);
        mtv_donglizhishu.setText(personalcenterEntity.all_sl_income);
        mtv_xiaoshou.setText(personalcenterEntity.team_sales);
        mtv_shangping.setText(personalcenterEntity.goods_fav_num);
        mtv_dianpu.setText(personalcenterEntity.store_fav_num);
        mtv_neirong.setText(personalcenterEntity.article_fav_num);
        mtv_zuji.setText(personalcenterEntity.footermark_fav_num);
        mtv_xiaodianhuiyuan.setText(personalcenterEntity.team_member_num);
        mtv_xiaodiandingdan.setText(personalcenterEntity.team_order_num);
        if (personalcenterEntity.sl_user_ranks.get(0) != null) {
            mtv_before.setText(personalcenterEntity.sl_user_ranks.get(0).nickname);
            mtv_befores.setText(personalcenterEntity.sl_user_ranks.get(0).number);
            GlideUtils.getInstance().loadCircleImage(baseContext, miv_before, personalcenterEntity.sl_user_ranks.get(0).avatar);
        }
        if (personalcenterEntity.sl_user_ranks.get(1) != null) {
            mtv_mid.setText(personalcenterEntity.sl_user_ranks.get(1).nickname);
            mtv_mids.setText(personalcenterEntity.sl_user_ranks.get(1).number);
            GlideUtils.getInstance().loadCircleImage(baseContext, miv_mid, personalcenterEntity.sl_user_ranks.get(1).avatar);
        }
        if (personalcenterEntity.sl_user_ranks.get(2) != null) {
            mtv_after.setText(personalcenterEntity.sl_user_ranks.get(2).nickname);
            mtv_afters.setText(personalcenterEntity.sl_user_ranks.get(2).number);
            GlideUtils.getInstance().loadCircleImage(baseContext, miv_after, personalcenterEntity.sl_user_ranks.get(2).avatar);
        }
    }
    public void startToast(final int percent) {
        final int[] now = {0};
        if (outTimer != null) {
            outTimer.cancel();
        }
        outTimer = new Timer();
        outTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (now[0] <percent){
                    now[0]++;
                    seekbar_grow.setProgress(now[0]);
                }else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mtv_persent.setText("当前积分null");
                        }
                    });
                    outTimer.cancel();
                }
            }
        }, 0,percent/10);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mtv_qiandao:
            case R.id.miv_qiandao:
                // TODO: 2018/2/2
                //签到页面
                break;
            case R.id.mllayout_quanbu:
                MyOrderAct.startAct(baseContext, 1);
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
                MyCollectionAct.startAct(baseContext);
                break;
            case R.id.mllayout_dianpu:
                MyCollectionAct.startAct(baseContext);
                break;
            case R.id.mllayout_neirong:
                MyCollectionAct.startAct(baseContext);
                break;
            case R.id.mllayout_zuji:
                MyCollectionAct.startAct(baseContext);
                break;
            case R.id.mrlayout_xiaodianhuiyuan:
                // TODO: 2018/2/2
//                小店会员
                break;
            case R.id.mrlayout_xiaodiandingdan:
                // TODO: 2018/2/2
//                小店订单
                break;
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


}
