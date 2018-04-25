package com.shunlian.app.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/23.
 */

public class NickNameAct extends BaseActivity {

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.met_nickname)
    MyEditText met_nickname;

    @BindView(R.id.mbtn_save)
    MyButton mbtn_save;

    public static final int REQUEST_CODE = 100;

    public static void startAct(Activity activity,String nickname){
        Intent intent = new Intent(activity, NickNameAct.class);
        intent.putExtra("nickname",nickname);
        activity.startActivityForResult(intent,REQUEST_CODE);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_nickname;
    }

    @Override
    protected void initListener() {
        super.initListener();
        met_nickname.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
                if (s.length() > 0){
                    background.setColor(getColorResouce(R.color.pink_color));
                }else {
                    background.setColor(getColorResouce(R.color.color_value_6c));
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("昵称");
        gone(mrlayout_toolbar_more);

        setEdittextFocusable(true,met_nickname);

        String nickname = getIntent().getStringExtra("nickname");
        met_nickname.setHint(nickname);

        GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
        background.setColor(getColorResouce(R.color.color_value_6c));
    }

    @OnClick(R.id.mbtn_save)
    public void save(){
        String s = met_nickname.getText().toString();
        if (isEmpty(s)){
            Common.staticToast("请设置昵称");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("nickname",s);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
