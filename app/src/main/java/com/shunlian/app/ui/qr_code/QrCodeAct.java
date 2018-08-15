package com.shunlian.app.ui.qr_code;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.GetQrCardEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.PQrCode;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IQrCode;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.wxapi.WXEntryActivity;

import butterknife.BindView;

public class QrCodeAct extends BaseActivity implements View.OnClickListener, IQrCode {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_yiyaoqing)
    MyTextView mtv_yiyaoqing;

    @BindView(R.id.mtv_chakanxiaodian)
    MyTextView mtv_chakanxiaodian;

    @BindView(R.id.rl_more)
    MyRelativeLayout rl_more;

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.mllayout_yaoqingweixin)
    MyLinearLayout mllayout_yaoqingweixin;

    @BindView(R.id.mllayout_fenxiangerweima)
    MyLinearLayout mllayout_fenxiangerweima;

    private PQrCode pQrCode;
    private String codeUrl;

    public static void startAct(Context context,String managerUrl) {
        Intent intent = new Intent(context, QrCodeAct.class);
        intent.putExtra("managerUrl", managerUrl);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_qr_code;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_chakanxiaodian:
                H5Act.startAct(this, getIntent().getStringExtra("managerUrl"), H5Act.MODE_SONIC);
                break;
            case R.id.mllayout_yaoqingweixin:
                ShareInfoParam shareInfoParam = new ShareInfoParam();
                shareInfoParam.photo = codeUrl;
                WXEntryActivity.startAct(this, "shareFriend", shareInfoParam);
                break;
            case R.id.mllayout_fenxiangerweima:
                GlideUtils.getInstance().savePicture(getBaseContext(), codeUrl);
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_chakanxiaodian.setOnClickListener(this);
        mllayout_yaoqingweixin.setOnClickListener(this);
        mllayout_fenxiangerweima.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
//        storeId = getIntent().getStringExtra("storeId");
        pQrCode = new PQrCode(this, this);
        mtv_title.setText(getStringResouce(R.string.personal_yaoqingpengyou));
        rl_more.setVisibility(View.GONE);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setApiData(GetQrCardEntity data) {
        codeUrl = data.card_path;
        GlideUtils.getInstance().loadImageZheng(this, miv_code, data.qrcode_path);
        String invitedNum = String.format(getString(R.string.qr_yiyaoqing), data.invited);
        SpannableStringBuilder invitedNumBuilder = Common.changeColorAndSize(invitedNum, data.invited, 16, getColorResouce(R.color.pink_color));
        if (mtv_chakanxiaodian!=null)
        mtv_yiyaoqing.setText(invitedNumBuilder);
    }
}
