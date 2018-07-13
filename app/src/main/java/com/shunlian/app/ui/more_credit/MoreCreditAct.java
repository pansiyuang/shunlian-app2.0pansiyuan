package com.shunlian.app.ui.more_credit;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.MoreCreditPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IMoreCreditView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.mylibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/7/13.
 */

public class MoreCreditAct extends BaseActivity implements IMoreCreditView {

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.met_phone)
    MyEditText met_phone;

    @BindView(R.id.miv_select_phone)
    MyImageView miv_select_phone;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_toolbar_right)
    MyTextView mtv_toolbar_right;

    @BindView(R.id.mbtn_credit)
    MyButton mbtn_credit;

    @BindView(R.id.rlayout_input)
    RelativeLayout rlayout_input;

    public final int REQUEST_CODE = 6666;
    private MoreCreditPresenter presenter;


    public static void startAct(Context context){
        context.startActivity(new Intent(context,MoreCreditAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_morecredit;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        gone(mrlayout_toolbar_more);
        visible(mtv_toolbar_right);
        mtv_toolbar_right.setText(getStringResouce(R.string.prepaid_phone_records));
        mtv_toolbar_title.setText(getStringResouce(R.string.more_creadit));
        setEdittextFocusable(true,met_phone);

        presenter = new MoreCreditPresenter(this,this);

        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);
        int w = TransformUtil.dip2px(this, 15);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(w,false));

    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @OnClick(R.id.miv_select_phone)
    public void selectPhone() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void inputPhoneAnim(){
        int[] pos = new int[2];
        rlayout_input.getLocationInWindow(pos);
        int statusBarHeight = ImmersionBar.getStatusBarHeight(this);
        LogUtil.zhLogW(String.format("=statusBarHeight=%d======pos==%d=",statusBarHeight,pos[1]));
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(pos[1],0);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            LogUtil.zhLogW("==value======"+value);
            rlayout_input.setY(value);
        });
        valueAnimator.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                //String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String phoneNumber = null;
                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                    + contactId,
                            null,
                            null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                    if (met_phone != null)
                        met_phone.setText(phoneNumber);
                }
            }
        }
    }


    @OnClick(R.id.mbtn_credit)
    public void credit(){

    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }
}
