package com.shunlian.app.ui.more_credit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MoreCreditAdapter;
import com.shunlian.app.adapter.PhoneRecordAdapter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/7/13.
 */

public class MoreCreditAct extends BaseActivity {

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

    public final int REQUEST_CODE = 6666;


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

    @Override
    protected void initListener() {
        super.initListener();
        mtv_toolbar_right.setOnClickListener(this);
    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()){
            case R.id.mtv_toolbar_right:
                PhoneRecordAct.startAct(this);
                break;
        }
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

        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);
        int w = TransformUtil.dip2px(this, 15);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(w,false));
        recy_view.setAdapter(new MoreCreditAdapter(this, DataUtil.getListString(7,"ff")));
    }


    @OnClick(R.id.miv_select_phone)
    public void selectPhone() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
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
}
