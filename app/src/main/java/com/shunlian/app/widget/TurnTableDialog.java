package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.LuckDrawEntity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.receive_adress.AddressListActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;

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

    public TurnTableDialog(Context context, int showType, String content, String thumb) {
        this(context, R.style.popAd);
        this.mContext = context;

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_turntable, null, false);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        initDialog(showType, content, thumb);
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

    public void initDialog(int type, String content, String thumb) {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = getDeviceWidth(mContext) - TransformUtil.dip2px(mContext, 90);
        win.setAttributes(lp);
        setShowDialog(type, content, thumb);
        initListeners();
    }

    public void initListeners() {
        miv_close.setOnClickListener(view -> {
            if (isShowing()) {
                dismiss();
            }
        });
        miv_button.setOnClickListener(v -> {
            if ("去购物".equals(miv_button.getTag())) {
                MainActivity.startAct(mContext, "mainPage");
            }
            dismiss();
        });
        miv_address.setOnClickListener(v -> {
            AddressListActivity.startAct(mContext, null);
            dismiss();
        });
        miv_share.setOnClickListener(v -> {

        });
    }

    public void setShowDialog(int type, String content, String thumb) {
        tv_content.setText(content);
        switch (type) {
            case 1:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_gouwu);
                miv_type.setVisibility(View.GONE);
                miv_button.setVisibility(View.VISIBLE);
                miv_button.setImageResource(R.mipmap.img_choujiang_anniu_gouwu);
                miv_button.setTag("去购物");
                miv_share.setVisibility(View.GONE);
                miv_address.setVisibility(View.GONE);
                break;
            case 2:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_jiangpin);
                miv_type.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(mContext, miv_type, thumb);
                miv_button.setVisibility(View.GONE);
                miv_share.setVisibility(View.VISIBLE);
                miv_address.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setDialogData(LuckDrawEntity luckDrawEntity) {
        //'奖品类型  1商品  2 优惠券  3金蛋',
        tv_content.setText(luckDrawEntity.meg);
        switch (luckDrawEntity.trophy_type) {
            case 1:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_jiangpin);
                miv_type.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(mContext, miv_type, luckDrawEntity.thumb);
                miv_button.setVisibility(View.GONE);
                miv_share.setVisibility(View.VISIBLE);
                miv_address.setVisibility(View.VISIBLE);
                break;
            case 2:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_youhuiquan);
                miv_type.setVisibility(View.GONE);
                miv_button.setVisibility(View.VISIBLE);
                miv_button.setTag("确定");
                miv_share.setVisibility(View.GONE);
                miv_address.setVisibility(View.GONE);
                break;
            case 3:
                miv_bg.setImageResource(R.mipmap.img_choujiang_tanchuang_jindan);
                miv_type.setVisibility(View.GONE);
                miv_button.setVisibility(View.VISIBLE);
                miv_button.setTag("确定");
                miv_share.setVisibility(View.GONE);
                miv_address.setVisibility(View.GONE);
                break;
        }
    }

    public void setLuckDrawEntity(LuckDrawEntity luckDrawEntity) {
        this.myLuckDraw = luckDrawEntity;
        setDialogData(luckDrawEntity);
    }

    public void setPopupData(int showType, String content, String thumb) {
        setShowDialog(showType, content, thumb);
    }
}
