package com.shunlian.app.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.NoAddressOrderEntity;
import com.shunlian.app.bean.TaskDrawEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoldEggDialog extends Dialog {

    @BindView(R.id.btn_bottom)
    Button btn_bottom;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.ll_notice)
    LinearLayout ll_notice;

    @BindView(R.id.ll_icon)
    LinearLayout ll_icon;

    @BindView(R.id.miv_select)
    MyImageView miv_select;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.miv_draw_icon)
    MyImageView miv_draw_icon;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_notice)
    TextView tv_notice;

    @BindView(R.id.ll_voucher)
    LinearLayout ll_voucher;

    @BindView(R.id.ll_rootView)
    LinearLayout ll_rootView;

    @BindView(R.id.tv_voucher_price)
    TextView tv_voucher_price;

    @BindView(R.id.tv_voucher_name)
    TextView tv_voucher_name;

    @BindView(R.id.tv_expire_date)
    TextView tv_expire_date;

    @BindView(R.id.tv_useType)
    TextView tv_useType;

    @BindView(R.id.tv_voucher_date)
    TextView tv_voucher_date;

    @BindView(R.id.tv_voucher_desc)
    TextView tv_voucher_desc;

    private TaskDrawEntity mTaskDraw;
    private NoAddressOrderEntity mOrderEntity;
    private Unbinder bind;
    private int showType = -1; //奖品类型：-1=金蛋抽奖，0=未中奖，1=实物，2=优惠券，3=金蛋 ，4=未填写收获地址，5=金蛋数量不足
    private int goldCount;
    private boolean isAttention = false;
    private OnDialogBtnClickListener onDialogBtnClickListener;

    public GoldEggDialog(Context context) {
        this(context, R.style.popAd);
    }

    public GoldEggDialog(Context context, int themeResId) {
        super(context, themeResId);

        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_gold_egg, null, false);
        setContentView(inflate);
        bind = ButterKnife.bind(this, inflate);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);

        btn_bottom.setOnClickListener(v -> {
            dismiss();
            if (onDialogBtnClickListener == null) {
                return;
            }
            switch (showType) {
                case -1: //金蛋抽奖
                    onDialogBtnClickListener.onDraw();
                    SharedPrefUtil.saveCacheSharedPrfBoolean("isAttention", isAttention);
                    break;
                case 0: //未中奖
                    break;
                case 1://实物
                    onDialogBtnClickListener.onSetAddress();
                    break;
                case 2://优惠券
                    if (mTaskDraw == null || mTaskDraw.voucher == null || mTaskDraw.voucher.url == null) {
                        return;
                    }
                    onDialogBtnClickListener.onToVisit(mTaskDraw.voucher.url);
                    break;
                case 3://金蛋
                    break;
                case 4://未填写收获地址
                    onDialogBtnClickListener.onSetAddress();
                    break;
                case 5://金蛋数量不足
                    onDialogBtnClickListener.jumpTaskCenter();
                    break;
            }
        });
        miv_close.setOnClickListener(v -> dismiss());
        miv_select.setOnClickListener(v -> {
            if (isAttention) {
                isAttention = false;
                miv_select.setImageResource(R.mipmap.icon_nor);
            } else {
                isAttention = true;
                miv_select.setImageResource(R.mipmap.icon_sel);
            }
        });
        int i = TransformUtil.dip2px(getContext(), 20);
        TransformUtil.expandViewTouchDelegate(miv_select, i, i, i, i);
    }

    public void setShowType(int type, int consume) {
        this.showType = type;
        this.goldCount = consume;
        initPrizeView(showType);
    }

    public void setShowType(TaskDrawEntity taskDraw) {
        mTaskDraw = taskDraw;
        this.showType = mTaskDraw.type;
        initPrizeView(showType);
    }

    public void setShowType(NoAddressOrderEntity orderEntity) {
        mOrderEntity = orderEntity;
        this.showType = 4;
        initPrizeView(showType);
    }

    public int getShowType() {
        return showType;
    }

    private void initPrizeView(int type) {
        switch (type) {
            case -1:
                tv_notice.setTextSize(24);
                tv_notice.setText("确定消耗" + goldCount + "个金蛋？");
                ll_voucher.setVisibility(View.GONE);
                ll_icon.setVisibility(View.VISIBLE);
                miv_icon.setVisibility(View.VISIBLE);
                miv_draw_icon.setVisibility(View.GONE);
                miv_icon.setImageResource(R.mipmap.image_xiaohaojindan);
                ll_notice.setVisibility(View.VISIBLE);
                tv_content.setVisibility(View.GONE);
                btn_bottom.setText(getContext().getResources().getText(R.string.SelectRecommendAct_sure));
                break;
            case 0:
                tv_notice.setTextSize(24);
                tv_notice.setText("哎呀，太可惜了！");
                ll_voucher.setVisibility(View.GONE);
                ll_icon.setVisibility(View.VISIBLE);
                miv_icon.setVisibility(View.GONE);
                miv_draw_icon.setVisibility(View.VISIBLE);
                miv_draw_icon.setImageResource(R.mipmap.image_weizhongjiang);
                ll_notice.setVisibility(View.GONE);
                tv_content.setVisibility(View.VISIBLE);
                tv_content.setText("差点中奖了");
                btn_bottom.setText(getContext().getResources().getText(R.string.SelectRecommendAct_sure));
                setLinearLayoutParam(194, 147, miv_draw_icon);
                break;
            case 1:
                tv_notice.setTextSize(24);
                tv_notice.setText("中奖啦！");
                ll_voucher.setVisibility(View.GONE);
                ll_icon.setVisibility(View.VISIBLE);
                ll_notice.setVisibility(View.GONE);
                if (mTaskDraw != null) {
                    miv_icon.setVisibility(View.GONE);
                    miv_draw_icon.setVisibility(View.VISIBLE);
                    GlideUtils.getInstance().loadOverrideImage(getContext(), miv_draw_icon, mTaskDraw.image, TransformUtil.dip2px(getContext(), 150), TransformUtil.dip2px(getContext(), 150));
                    setLinearLayoutParam(150, 150, miv_draw_icon);
                    tv_content.setVisibility(View.VISIBLE);
                    tv_content.setText(mTaskDraw.desc);
                }else{
                    tv_content.setVisibility(View.GONE);
                }
                btn_bottom.setText("填写收货地址");
                break;
            case 2:
                tv_notice.setTextSize(24);
                ll_voucher.setVisibility(View.VISIBLE);
                ll_icon.setVisibility(View.GONE);
                tv_notice.setText("中奖啦！");
                ll_notice.setVisibility(View.GONE);
                if (mTaskDraw != null && mTaskDraw.voucher != null) {
                    TaskDrawEntity.Voucher voucher = mTaskDraw.voucher;
                    tv_voucher_name.setText(voucher.title);
                    String price = getContext().getResources().getString(R.string.common_yuan) + voucher.denomination;
                    tv_voucher_price.setText(Common.changeTextSize(price, getContext().getResources().getString(R.string.common_yuan), 12));
                    tv_expire_date.setText(voucher.limit_time);
                    tv_useType.setText(voucher.use_condition);
                    tv_voucher_date.setText(voucher.start_time);
                    tv_voucher_desc.setText(voucher.desc);
                    tv_content.setVisibility(View.VISIBLE);
                    tv_content.setText(mTaskDraw.desc);
                }else{
                    tv_content.setVisibility(View.GONE);
                }
                btn_bottom.setText(getContext().getResources().getText(R.string.go_to_visit));
                break;
            case 3:
                tv_notice.setTextSize(24);
                ll_icon.setVisibility(View.VISIBLE);
                tv_notice.setText("中奖啦！");
                ll_voucher.setVisibility(View.GONE);
                ll_icon.setVisibility(View.VISIBLE);
                miv_icon.setVisibility(View.VISIBLE);
                miv_draw_icon.setVisibility(View.GONE);
                miv_icon.setImageResource(R.mipmap.image_huodejindan);
                ll_notice.setVisibility(View.GONE);
                if (mTaskDraw != null) {
                    tv_content.setVisibility(View.VISIBLE);
                    tv_content.setText(mTaskDraw.desc);
                }
                btn_bottom.setText(getContext().getResources().getText(R.string.SelectRecommendAct_sure));
                break;
            case 4:
                tv_notice.setTextSize(20);
                ll_icon.setVisibility(View.VISIBLE);
                tv_notice.setText("你还有奖品未填写收货地址");
                ll_voucher.setVisibility(View.GONE);
                ll_icon.setVisibility(View.VISIBLE);
                miv_icon.setVisibility(View.GONE);
                miv_draw_icon.setVisibility(View.VISIBLE);
                ll_notice.setVisibility(View.GONE);
                if (mOrderEntity != null) {
                    tv_content.setVisibility(View.VISIBLE);
                    tv_content.setText(mOrderEntity.desc);
                }
                btn_bottom.setText("填写收货地址");
                setLinearLayoutParam(190, 133, miv_draw_icon);
                miv_draw_icon.setImageResource(R.mipmap.image_dizhi);
                break;
            case 5:
                tv_notice.setTextSize(20);
                ll_icon.setVisibility(View.VISIBLE);
                tv_notice.setText("抱歉，您的金蛋数量不足,快去做任务领金蛋吧");
                ll_voucher.setVisibility(View.GONE);
                ll_icon.setVisibility(View.VISIBLE);
                miv_icon.setVisibility(View.GONE);
                miv_draw_icon.setVisibility(View.VISIBLE);
                ll_notice.setVisibility(View.GONE);
                tv_content.setVisibility(View.GONE);
                btn_bottom.setText(getContext().getResources().getText(R.string.go_to_visit));
                setLinearLayoutParam(190, 133, miv_draw_icon);
                miv_draw_icon.setImageResource(R.mipmap.image_shuliangbuzu);
                break;
        }
        show();
    }

    public void setLinearLayoutParam(int width, int height, MyImageView myImageView) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) myImageView.getLayoutParams();
        layoutParams.width = TransformUtil.dip2px(getContext(), width);
        layoutParams.height = TransformUtil.dip2px(getContext(), height);
        myImageView.setLayoutParams(layoutParams);
    }

    public void setOnDialogBtnClickListener(OnDialogBtnClickListener listener) {
        this.onDialogBtnClickListener = listener;
    }


    public interface OnDialogBtnClickListener {
        void onDraw();

        void onSetAddress();

        void onToVisit(TaskDrawEntity.Url url);

        void jumpTaskCenter();
    }
}
