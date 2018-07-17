package com.shunlian.app.ui.more_credit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.MoreCreditPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IMoreCreditView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

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

    @BindView(R.id.mtv_phone)
    MyTextView mtv_phone;

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

    @BindView(R.id.mtv_BelongingTo)
    MyTextView mtv_BelongingTo;

    @BindView(R.id.view_line)
    View view_line;

    @BindView(R.id.mtv_error_tip)
    MyTextView mtv_error_tip;

    @BindView(R.id.frame_mask)
    FrameLayout frame_mask;

    @BindView(R.id.frame_mask1)
    FrameLayout frame_mask1;

    @BindView(R.id.llayout_input)
    LinearLayout llayout_input;

    @BindView(R.id.miv_clear)
    MyImageView miv_clear;

    public final int REQUEST_CODE = 6666;
    private MoreCreditPresenter presenter;
    //是否来自通讯录
    private boolean is_from_the_address_book = false;


    public static void startAct(Context context){
        context.startActivity(new Intent(context,MoreCreditAct.class));
    }

    @OnClick(R.id.mtv_toolbar_right)
    public void prepaidPhoneRecords(){
        PhoneRecordAct.startAct(this);
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

        presenter = new MoreCreditPresenter(this,this);

        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);
        int w = TransformUtil.dip2px(this, 15);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(w,false));

        isPhoneCorrectState(true);
    }

    @Override
    protected void initListener() {
        super.initListener();

        met_phone.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (isEmpty(s))return;
                if (!is_from_the_address_book){
                    if (s.length() > 11){
                        CharSequence charSequence = s.subSequence(0, 11);
                        met_phone.setText(charSequence);
                    }
                }else {
                    met_phone.setSelection(s.length());
                }
                if (s.length() == 11){
                    mtv_phone.setText(met_phone.getText());
                    hideInput();
                }
            }
        });
    }

    private void checkPhoneCorrect(){
        if (mtv_phone.getText().length()==11 && presenter != null && mtv_phone.
                getText().toString().startsWith("1")){
            isPhoneCorrectState(true);
            presenter.phoneNumber = mtv_phone.getText().toString();
            presenter.initApi();
        }else {
            is_from_the_address_book = false;
            if (!mtv_phone.getText().toString().startsWith("1") || mtv_phone.getText().length() > 11){
                isPhoneCorrectState(false);
            }else {
                isPhoneCorrectState(true);
            }
            mtv_BelongingTo.setText("");
        }
    }

    @OnClick({R.id.rlayout_input,R.id.mtv_phone})
    public void showInput(){
        visible(frame_mask);
        inputPhoneAnim(-1,0);
    }

    @OnClick(R.id.frame_mask)
    public void hideInput(){
        Common.hideKeyboard(met_phone);
        inputPhoneAnim(0,-1);
    }

    @OnClick(R.id.miv_select_phone)
    public void selectPhone() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.mtv_toolbar_right)
    public void prepaidPhoneRecords(){
        PhoneRecordAct.startAct(this);
    }
    public void inputPhoneAnim(int fromYValue,int toYValue){
        visible(llayout_input);
        llayout_input.clearAnimation();
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0
                ,Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,fromYValue,Animation.RELATIVE_TO_SELF,toYValue);
        ta.setDuration(250);
        ta.setInterpolator(new LinearInterpolator());
        ta.setFillAfter(true);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (toYValue == 0){
                    visible(llayout_input);
                    setEdittextFocusable(true,met_phone);
                    Common.showKeyboard(met_phone);
                }else {
                    gone(llayout_input,frame_mask);
                    setEdittextFocusable(false,met_phone);
                    checkPhoneCorrect();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        llayout_input.setAnimation(ta);
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
                    is_from_the_address_book = true;
                    if (mtv_phone != null) {
                        mtv_phone.setText(phoneNumber.replaceAll(" ", ""));
                        checkPhoneCorrect();
                    }
                }
            }
        }
    }


    @OnClick(R.id.mbtn_credit)
    public void credit(){
        if (isEmpty(mtv_phone.getText())){
            Common.staticToast("手机号不能为空");
            return;
        }
        if (mtv_phone.getText().length() < 11 || !mtv_phone.getText().toString().startsWith("1")){
            isPhoneCorrectState(false);
            return;
        }
        isPhoneCorrectState(true);
        String phone = mtv_phone.getText().toString().replaceAll(" ", "");
        if (presenter != null){
            presenter.topUp(phone);
        }
    }

    private void isPhoneCorrectState(boolean correct){
        GradientDrawable inputGB = (GradientDrawable) rlayout_input.getBackground();
        if (!correct){
            inputGB.setColor(Color.WHITE);
            inputGB.setStroke(TransformUtil.dip2px(this,1)
                    ,getColorResouce(R.color.pink_color));
            gone(view_line);
            visible(mtv_error_tip,frame_mask1);
        }else {
            visible(view_line);
            gone(frame_mask1);
            mtv_error_tip.setVisibility(View.INVISIBLE);
            inputGB.setColor(Color.WHITE);
            inputGB.setStroke(TransformUtil.dip2px(this,1),Color.WHITE);
        }
    }

    @OnClick(R.id.miv_clear)
    public void clearPhone(){
        met_phone.setText("");
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

    /**
     * 设置号码归属地
     *
     * @param phone
     * @param card_address
     */
    @Override
    public void setBelongingTo(String phone, String card_address) {
        mtv_BelongingTo.setText(String.format("(%s)",card_address));
    }

    /**
     * 手机号码错误
     */
    @Override
    public void phoneError() {
        isPhoneCorrectState(false);
    }
}
