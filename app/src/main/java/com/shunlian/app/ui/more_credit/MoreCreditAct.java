package com.shunlian.app.ui.more_credit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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
import com.shunlian.app.utils.Code;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
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

    @BindView(R.id.view_line)
    View view_line;

    @BindView(R.id.mtv_tip)
    MyTextView mtv_tip;

    @BindView(R.id.frame_mask)
    FrameLayout frame_mask;

    @BindView(R.id.llayout_input)
    LinearLayout llayout_input;

    @BindView(R.id.miv_clear)
    MyImageView miv_clear;

    @BindView(R.id.recy_view_history)
    RecyclerView recy_view_history;

    @BindView(R.id.miv_clear1)
    MyImageView miv_clear1;

    @BindView(R.id.frame_mask1)
    FrameLayout frame_mask1;

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
        mtv_toolbar_right.setTextColor(Color.parseColor("#FF007AFF"));
        mtv_toolbar_right.setText(getStringResouce(R.string.prepaid_phone_records));
        mtv_toolbar_title.setText(getStringResouce(R.string.more_creadit));

        presenter = new MoreCreditPresenter(this,this);

        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);
        int w = TransformUtil.dip2px(this, 15);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(w,false));

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        recy_view_history.setLayoutManager(manager1);
        int space = TransformUtil.dip2px(this, 0.5f);
        recy_view_history.addItemDecoration(new VerticalItemDecoration(space,0,
                0,getColorResouce(R.color.color_value_6c)));

        mtv_tip.setVisibility(View.INVISIBLE);
        TransformUtil.expandViewTouchDelegate(miv_select_phone,w*2,w*2,w*2,w*2);
        TransformUtil.expandViewTouchDelegate(miv_clear1,w*2,w*2,w*2,w*2);
        TransformUtil.expandViewTouchDelegate(miv_clear,w*2,w*2,w*2,w*2);
    }

    @Override
    protected void initListener() {
        super.initListener();

        met_phone.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (isEmpty(s))return;
                if (s.length() > 11){
                    CharSequence charSequence = s.subSequence(0, 11);
                    met_phone.setText(charSequence);
                }
                if (s.length() == 11){
                    mtv_phone.setText(met_phone.getText());
                    hideInput();
                }
            }
        });

        miv_clear1.setOnClickListener(v -> {
            mtv_phone.setText("");
            mtv_tip.setText("");
            met_phone.setText("");
            setEdittextFocusable(true,met_phone);
            gone(miv_clear1);
            visible(miv_select_phone);
        });
    }

    private void checkPhoneCorrect(){
        //LogUtil.zhLogW("checkPhoneCorrect========="+mtv_phone.getText());
        if (mtv_phone.getText().length()==11 && presenter != null){
            mtv_tip.setVisibility(View.INVISIBLE);
            presenter.phoneNumber = mtv_phone.getText().toString();
            presenter.initApi();
        }else {
            if (mtv_phone.getText().length() != 11){
                handleTip(Code.CREDIT_PHONE_ERROR,"请输入正确手机号");
            }else {
                mtv_tip.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick({R.id.rlayout_input,R.id.mtv_phone})
    public void showInput(){
        visible(frame_mask);
        met_phone.setSelection(met_phone.getText().length());
        inputPhoneAnim(-1,0);
    }

    @OnClick({R.id.frame_mask,R.id.miv_back})
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
        if (requestCode == REQUEST_CODE) {
            Uri contactData = data.getData();
            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c != null && c.moveToFirst()) {
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
                    //LogUtil.zhLogW("phoneNumber========="+phoneNumber);
                    if (!isEmpty(phoneNumber)){
                        if (phoneNumber.contains("+86")){
                            phoneNumber = phoneNumber.replace("+86","");
                        }
                        phoneNumber = phoneNumber.replaceAll("-", "");
                        if (mtv_phone != null) {
                            mtv_phone.setText(phoneNumber.replaceAll(" ", ""));
                            met_phone.setText(mtv_phone.getText());
                            checkPhoneCorrect();
                        }
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
        if (mtv_phone.getText().length() != 11){
            handleTip(Code.CREDIT_PHONE_ERROR,"请输入正确手机号");
            return;
        }
        //mtv_tip.setVisibility(View.INVISIBLE);
        String phone = mtv_phone.getText().toString().replaceAll(" ", "");
        if (presenter != null){
            presenter.topUp(phone);
        }
    }

    @OnClick(R.id.miv_clear)
    public void clearPhone(){
        met_phone.setText("");
        setEdittextFocusable(true,met_phone);
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
        handleTip(1,String.format("号码归属地(%s)",card_address));
    }


    /**
     * 1 提示归属地
     * 2 提示手机号错误
     * 3 提示暂不支持港澳台
     * @param code
     */
    private void handleTip(int code,String tip){
        visible(mtv_tip);
        switch (code){
            case 1:
                gone(miv_clear1,frame_mask1);
                visible(miv_select_phone);
                mtv_tip.setTextColor(getColorResouce(R.color.text_gray2));
                mtv_tip.setText(tip);
                break;
            case Code.CREDIT_PHONE_ERROR:
            case Code.CREDIT_NO_SUPPORT:
            case Code.CREDIT_NO_BELONGING:
                visible(miv_clear1,frame_mask1);
                gone(miv_select_phone);
                mtv_tip.setTextColor(getColorResouce(R.color.pink_color));
                mtv_tip.setText(tip);
                break;
            default:
                mtv_tip.setTextColor(getColorResouce(R.color.pink_color));
                mtv_tip.setText(tip);
                visible(frame_mask1);
                break;
        }
    }

    /**
     * 手机号码错误
     */
    @Override
    public void phoneError(int code,String msg) {
        handleTip(code,msg);
    }

    @Override
    public void setTopUpHistoryAdapter(BaseRecyclerAdapter adapter) {
        if (adapter == null){
            gone(recy_view_history);
        }else {
            visible(recy_view_history);
            if (adapter.getItemCount() <= 2){
                ViewGroup.LayoutParams layoutParams = recy_view_history.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                recy_view_history.setLayoutParams(layoutParams);
            }
            recy_view_history.setAdapter(adapter);
        }
    }

    public void setPhone(String num){
        if (met_phone != null)
            met_phone.setText(num);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null){
            presenter.detachView();
            presenter = null;
        }
        super.onDestroy();
    }
}
