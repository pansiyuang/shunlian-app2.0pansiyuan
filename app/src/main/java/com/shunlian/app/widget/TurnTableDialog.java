package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.LuckDrawEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.TurnTablePopEntity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.receive_adress.AddressListActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.luckWheel.RotateListener;
import com.shunlian.app.wxapi.WXEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceWidth;

/**
 * Created by Administrator on 2018/9/3.
 */

public class TurnTableDialog extends Dialog {
    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.miv_bg)
    MyImageView miv_bg;

    @BindView(R.id.miv_type)
    MyImageView miv_type;

    @BindView(R.id.miv_button)
    MyImageView miv_button;

    @BindView(R.id.miv_address)
    MyImageView miv_address;

    @BindView(R.id.miv_share)
    MyImageView miv_share;

    @BindView(R.id.tv_content)
    TextView tv_content;

    private Context mContext;
    private LuckDrawEntity myLuckDraw;
    private TurnTablePopEntity myPopEntity;
    private OnShareCallBack callBack;
    private boolean isAttention;

    public TurnTableDialog(Context context, LuckDrawEntity luckDrawEntity) {
        this(context, R.style.popAd);
        this.mContext = context;
        this.myLuckDraw = luckDrawEntity;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_turntable, null, false);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        initViews();
        setCanceledOnTouchOutside(false);
    }

    public TurnTableDialog(Context context, TurnTablePopEntity turnTablePopEntity) {
        this(context, R.style.popAd);
        this.mContext = context;
        this.myPopEntity = turnTablePopEntity;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_turntable, null, false);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        initDialog(turnTablePopEntity);
        setCanceledOnTouchOutside(false);
    }

    public TurnTableDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void initViews() {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = getDeviceWidth(mContext) - TransformUtil.dip2px(mContext, 90);
        win.setAttributes(lp);
        setDialogData(myLuckDraw);
        initListeners();
    }

    public void initDialog(TurnTablePopEntity turnTablePopEntity) {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = getDeviceWidth(mContext) - TransformUtil.dip2px(mContext, 90);
        win.setAttributes(lp);
        setShowDialog(turnTablePopEntity);
        initListeners();
    }

    public void initListeners() {
        miv_close.setOnClickListener(view -> {
            dismiss();
            if (myPopEntity != null && myPopEntity.show == 2) {
                if (callBack != null) {
                    callBack.cancelDraw();
                }
            } else if (myLuckDraw != null && myLuckDraw.trophy_type == 1) {
                if (callBack != null) {
                    callBack.cancelDraw();
                }
            }
        });
        miv_button.setOnClickListener(v -> {
            if ("去购物".equals(miv_button.getTag())) {
                MainActivity.startAct(mContext, "mainPage");
            } else if ("炫耀一下".equals(miv_button.getTag())) {
                if (callBack != null) {
                    callBack.onShare();
                }
            }
            dismiss();
        });
        miv_address.setOnClickListener(v -> {
            AddressListActivity.startAct(mContext, null);
            dismiss();
        });
        miv_share.setOnClickListener(v -> {
            if (callBack != null) {
                if (!isAttention) {
                    callBack.onShare();
                }
            }
            dismiss();
        });
    }

    public void setShowDialog(TurnTablePopEntity turnTablePopEntity) {
        this.myPopEntity = turnTablePopEntity;
        tv_content.setText(turnTablePopEntity.list.meg);
        miv_button.setTag(null);
        miv_button.setImageResource(R.mipmap.img_choujiang_anniu_xuanyao);
        miv_close.setVisibility(View.VISIBLE);
        isAttention = false;
        switch (turnTablePopEntity.show) {
            case 1:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_gouwu);
                miv_type.setVisibility(View.GONE);
                miv_button.setImageResource(R.mipmap.img_choujiang_anniu_gouwu);
                miv_button.setVisibility(View.VISIBLE);
                miv_button.setTag("去购物");
                miv_share.setVisibility(View.GONE);
                miv_address.setVisibility(View.GONE);
                break;
            case 2:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_jiangpin);
                miv_type.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(mContext, miv_type, turnTablePopEntity.list.thumb);
                miv_button.setVisibility(View.GONE);
                miv_share.setVisibility(View.VISIBLE);
                miv_address.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setDialogData(LuckDrawEntity luckDrawEntity) {
        this.myLuckDraw = luckDrawEntity;
        //'奖品类型  1商品  2 优惠券  3金蛋',
        tv_content.setText(luckDrawEntity.meg);
        miv_button.setTag(null);
        miv_button.setImageResource(R.mipmap.img_choujiang_anniu_xuanyao);
        miv_close.setVisibility(View.VISIBLE);
        isAttention = false;
        switch (luckDrawEntity.trophy_type) {
            case 1:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_jiangpin);
                miv_type.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(mContext, miv_type, luckDrawEntity.thumb);
                miv_button.setVisibility(View.GONE);
                miv_share.setVisibility(View.VISIBLE);
                miv_share.setImageResource(R.mipmap.img_choujiang_anniu_fenxiang);
                miv_address.setImageResource(R.mipmap.img_choujiang_anniu_dizhi);
                miv_address.setVisibility(View.VISIBLE);
                break;
            case 2:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_youhuiquan);
                miv_type.setVisibility(View.GONE);
                miv_button.setImageResource(R.mipmap.img_choujiang_anniu_gouwu);
                miv_button.setVisibility(View.VISIBLE);
                miv_button.setTag("去购物");
                miv_share.setVisibility(View.GONE);
                miv_address.setVisibility(View.GONE);
                break;
            case 3:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_jindan);
                miv_type.setVisibility(View.GONE);
                miv_button.setVisibility(View.VISIBLE);
                miv_button.setTag("炫耀一下");
                miv_share.setVisibility(View.GONE);
                miv_address.setVisibility(View.GONE);
                break;
        }
    }

    public void setLuckDrawEntity(LuckDrawEntity luckDrawEntity) {
        this.myLuckDraw = luckDrawEntity;
        this.myPopEntity = null;
        setDialogData(luckDrawEntity);
    }

    public void setPopupData(TurnTablePopEntity turnTablePopEntity) {
        this.myPopEntity = turnTablePopEntity;
        this.myLuckDraw = null;
        setShowDialog(turnTablePopEntity);
    }

    public void showAttentionData() {
        isAttention = true;
        if (myPopEntity != null) {
            tv_content.setText(myPopEntity.text);
        } else if (myLuckDraw != null) {
            tv_content.setText(myLuckDraw.text);
        }
        miv_button.setTag(null);
        miv_button.setImageResource(R.mipmap.img_choujiang_anniu_xuanyao);
        miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_jiangpin);
        miv_type.setVisibility(View.VISIBLE);
        miv_type.setImageResource(R.mipmap.img_xiaoji);
        miv_button.setVisibility(View.GONE);
        miv_share.setVisibility(View.VISIBLE);
        miv_share.setImageResource(R.mipmap.img_guanbi);
        miv_address.setVisibility(View.VISIBLE);
        miv_address.setImageResource(R.mipmap.img_choujiang_anniu_dizhi);
        miv_close.setVisibility(View.GONE);
    }

    public void setCallBack(OnShareCallBack shareCallBack) {
        callBack = shareCallBack;
    }

    public interface OnShareCallBack {

        void onShare();

        void cancelDraw();
    }
}
