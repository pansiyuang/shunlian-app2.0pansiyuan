package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/10/12.
 */

public class FindSendPictureTextAct extends BaseActivity {


    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtvToolbarTitle;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayoutToolbarMore;

    @BindView(R.id.mtv_toolbar_right)
    MyTextView mtvToolbarRight;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, FindSendPictureTextAct.class);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_picture_text;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtvToolbarTitle.setText("发布图文");
        gone(mrlayoutToolbarMore);
        visible(mtvToolbarRight);
        mtvToolbarRight.setText("发布");
        mtvToolbarRight.setTextColor(getColorResouce(R.color.pink_color));

        mtvToolbarTitle.setOnClickListener(v -> SelectPicVideoAct.startAct(FindSendPictureTextAct.this));
    }

    /**
     * 添加位置
     */
    @OnClick(R.id.rlayout_address)
    public void appendAddress(){

    }
}
