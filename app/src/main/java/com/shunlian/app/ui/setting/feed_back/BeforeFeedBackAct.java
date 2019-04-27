package com.shunlian.app.ui.setting.feed_back;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.yjfk.OpinionActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class BeforeFeedBackAct extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.ntv_email)
    NewTextView ntv_email;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.rLayout_more)
    RelativeLayout rLayout_more;

    @BindView(R.id.rLayout_mores)
    RelativeLayout rLayout_mores;

    private String goodsid;

    public static void startAct(Context context, String goodsid) {
        Intent intent = new Intent(context, BeforeFeedBackAct.class);
        intent.putExtra("goodsid", goodsid);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_before_feedback;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        gone(mrlayout_toolbar_more);
        mtv_toolbar_title.setText("我要反馈");
        goodsid = getIntent().getStringExtra("goodsid");
        ntv_email.setText(getStringResouce(R.string.help_fanfuducha) + Constant.EMAIL);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rLayout_more.setOnClickListener(this);
        rLayout_mores.setOnClickListener(this);
        ntv_email.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ntv_email:
                Common.copyText(this,Constant.EMAIL);
                break;
            case R.id.rLayout_more:
                /*Intent intent = new Intent(this, NewFeedBackAct.class);
                intent.putExtra("goodsid", goodsid);
                intent.putExtra("type", "1");
                startActivity(intent);*/
                OpinionActivity.startAct(this);
                break;
            case R.id.rLayout_mores:
                Intent intents = new Intent(this, NewFeedBackAct.class);
                intents.putExtra("goodsid", "");
                intents.putExtra("type", "0");
                startActivity(intents);
                break;
        }
    }
}