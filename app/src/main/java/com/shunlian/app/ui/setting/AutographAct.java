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

public class AutographAct extends BaseActivity {

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.met_signature)
    MyEditText met_signature;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.mbtn_save)
    MyButton mbtn_save;

    private int maxTextLength = 50;
    private String format = "%d/%d";

    public static final int REQUEST_CODE = 200;


    public static void startAct(Activity activity, String signature){
        Intent intent = new Intent(activity, AutographAct.class);
        intent.putExtra("signature",signature);
        activity.startActivityForResult(intent,REQUEST_CODE);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_autograph;
    }

    @Override
    protected void initListener() {
        super.initListener();
        met_signature.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
                if (s.length() > 0){
                    background.setColor(getColorResouce(R.color.pink_color));
                }else {
                    background.setColor(getColorResouce(R.color.color_value_6c));
                }
                mtv_count.setText(String.format(format,s.length(),maxTextLength));
                if (s.length() > maxTextLength){
                    met_signature.setText(s.subSequence(0,maxTextLength));
                    met_signature.setSelection(maxTextLength);
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
        mtv_toolbar_title.setText("个性签名");
        gone(mrlayout_toolbar_more);

        String signature = getIntent().getStringExtra("signature");
        met_signature.setText(signature);
        setEdittextFocusable(true,met_signature);

//        GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
//        background.setColor(getColorResouce(R.color.color_value_6c));

        mtv_count.setText(String.format(format,signature.length(),maxTextLength));
    }

    @OnClick(R.id.mbtn_save)
    public void save(){
        String s = met_signature.getText().toString();
        if (isEmpty(s)){
            Common.staticToast("请设置个性签名");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("signature",s);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
