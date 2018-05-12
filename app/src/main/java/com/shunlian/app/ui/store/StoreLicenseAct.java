package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.presenter.StoreLicensePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.StoreLicenseView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class StoreLicenseAct extends BaseActivity implements View.OnClickListener, StoreLicenseView {

    @BindView(R.id.iv_verifi)
    MyImageView iv_verifi;

    @BindView(R.id.miv_photo)
    MyImageView miv_photo;

    @BindView(R.id.mtv_sure)
    MyTextView mtv_sure;

    @BindView(R.id.edt_verifi)
    EditText edt_verifi;

    @BindView(R.id.mllayout_one)
    MyLinearLayout mllayout_one;


    private StoreLicensePresenter storeLicensePresenter;
    private String seller_id;

    public static void startAct(Context context, String seller_id) {
        Intent intent = new Intent(context, StoreLicenseAct.class);
        intent.putExtra("seller_id", seller_id);//店铺id
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_store_license;
    }

    @Override
    protected void initData() {
//        seller_id = getIntent().getStringExtra("seller_id");
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        seller_id = "206";
        storeLicensePresenter = new StoreLicensePresenter(this, this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_verifi.setOnClickListener(this);
        mtv_sure.setOnClickListener(this);
    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null) {
            iv_verifi.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    }

    @Override
    public void setLicense(String url) {
        mllayout_one.setVisibility(View.GONE);
        GlideUtils.getInstance().loadImage(this, miv_photo, url);
        miv_photo.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View view) {
        if (MyOnClickListener.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_verifi:
                storeLicensePresenter.getCode();
                break;
            case R.id.mtv_sure:
                if (!TextUtils.isEmpty(edt_verifi.getText())){
                    storeLicensePresenter.checkLicense(edt_verifi.getText().toString(),seller_id);
                }else {
                    Common.staticToast("请先输入验证码");
                }
                break;
        }
    }
}
