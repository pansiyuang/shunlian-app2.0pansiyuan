package com.shunlian.app.ui.my_profit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.KeyEvent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/19.
 */

public class SexSelectAct extends BaseActivity {

    /*@BindView(R.id.mrlayout_private)
    MyRelativeLayout mrlayout_private;*/

    @BindView(R.id.mrlayout_woman)
    MyRelativeLayout mrlayout_woman;

    @BindView(R.id.mrlayout_man)
    MyRelativeLayout mrlayout_man;

    @BindView(R.id.miv_man)
    MyImageView miv_man;

    @BindView(R.id.miv_woman)
    MyImageView miv_woman;

    /*@BindView(R.id.miv_private)
    MyImageView miv_private;*/

    @BindView(R.id.mbt_next)
    MyButton mbt_next;

    private int sexSelect = -1;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,SexSelectAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_sexselect;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        closeSideslip();
        GradientDrawable background = (GradientDrawable) mbt_next.getBackground();
        background.setColor(getColorResouce(R.color.pink_color));
    }

    @OnClick(R.id.mrlayout_man)
    public void clickMan(){
        change(1);
        sexSelect = 1;
    }

    @OnClick(R.id.mrlayout_woman)
    public void clickWoman(){
        change(2);
        sexSelect = 2;

    }

    /*@OnClick(R.id.mrlayout_private)
    public void clickPrivate(){
        change(3);
        sexSelect = 3;

    }*/

    private void change(int state){
        int tixian = R.mipmap.img_balance_tixian;
        int img_kong = R.mipmap.img_kong;
        miv_man.setImageResource(state == 1?tixian:img_kong);
        miv_woman.setImageResource(state == 2?tixian:img_kong);
        //miv_private.setImageResource(state == 3?tixian:img_kong);
    }

    @OnClick(R.id.mbt_next)
    public void next(){
        if (sexSelect < 0){
            Common.staticToast("请选择性别");
            return;
        }
        SelectLabelAct.startAct(this,sexSelect);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            finish();
            MainActivity.startAct(this,"personCenter");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)return true;
        return super.onKeyDown(keyCode, event);
    }
}
