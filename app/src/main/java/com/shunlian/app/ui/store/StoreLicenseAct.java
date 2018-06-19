package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.presenter.StoreLicensePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.StoreLicenseView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.io.Serializable;

import butterknife.BindView;

public class StoreLicenseAct extends BaseActivity implements View.OnClickListener, StoreLicenseView {

    @BindView(R.id.iv_verifi)
    MyImageView iv_verifi;

    @BindView(R.id.miv_photo)
    MyImageView miv_photo;

    @BindView(R.id.miv_star)
    MyImageView miv_star;

    @BindView(R.id.miv_shop_head)
    MyImageView miv_shop_head;

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.mtv_sure)
    MyTextView mtv_sure;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_shop_name)
    MyTextView mtv_shop_name;

    @BindView(R.id.edt_verifi)
    EditText edt_verifi;

    @BindView(R.id.mllayout_one)
    MyLinearLayout mllayout_one;

    @BindView(R.id.mrlayout_content)
    MyRelativeLayout mrlayout_content;


    private StoreLicensePresenter storeLicensePresenter;
    private String seller_id;

    public static void startAct(Context context,StoreIntroduceEntity storeIntroduceEntity) {
        Intent intent = new Intent(context, StoreLicenseAct.class);
        intent.putExtra("storeIntroduceEntity", storeIntroduceEntity);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_store_license;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        StoreIntroduceEntity storeIntroduceEntity= (StoreIntroduceEntity) getIntent().getSerializableExtra("storeIntroduceEntity");
        if (storeIntroduceEntity.isCode){
            mtv_title.setText(getStringResouce(R.string.store_dianpuerweima));
            mrlayout_content.setVisibility(View.VISIBLE);
            mtv_shop_name.setText(storeIntroduceEntity.store_name);
            GlideUtils.getInstance().loadImageZheng(this,miv_star,storeIntroduceEntity.storeScore);
            int i = TransformUtil.countRealWidth(this, 400);
            Bitmap qrImage=BitmapUtil.createQRImage(storeIntroduceEntity.store_url,null,i);
            miv_code.setImageBitmap(qrImage);
            if (TextUtils.isEmpty(storeIntroduceEntity.storeLogo)){
                miv_shop_head.setVisibility(View.GONE);
            }else {
                GlideUtils.getInstance().loadCircleAvar(this,miv_shop_head,storeIntroduceEntity.storeLogo);
            }
        }else {
            seller_id = storeIntroduceEntity.seller_id;
            storeLicensePresenter = new StoreLicensePresenter(this, this);
            mllayout_one.setVisibility(View.VISIBLE);
            mtv_title.setText(getStringResouce(R.string.store_yingyezhizhao));
        }
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
