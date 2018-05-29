package com.shunlian.app.ui.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.PunishEntity;
import com.shunlian.app.presenter.PunishPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IPunishView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 */

public class PunishAct extends BaseActivity implements IPunishView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.llayout_card)
    LinearLayout llayout_card;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.mtv_id)
    MyTextView mtv_id;

    @BindView(R.id.mtv_tip)
    MyTextView mtv_tip;

    @BindView(R.id.mtv_time)
    MyTextView mtv_time;

    @BindView(R.id.mtv_content)
    MyTextView mtv_content;

    @BindView(R.id.miv_pic)
    MyImageView miv_pic;

    @BindView(R.id.mtv_complaints)
    MyTextView mtv_complaints;





    private PunishPresenter presenter;
    private String mOPT;

    public static void startAct(Context context, String id, String opt){
        Intent intent = new Intent(context,PunishAct.class);
        intent.putExtra("id",id);
        intent.putExtra("opt",opt);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_punish;
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        mtv_toolbar_title.setText(getStringResouce(R.string.punish));
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        String id = getIntent().getStringExtra("id");
        mOPT = getIntent().getStringExtra("opt");

        GradientDrawable background = (GradientDrawable) llayout_card.getBackground();
        background.setColor(getColorResouce(R.color.white));

        presenter = new PunishPresenter(this,this,id);
    }

    @OnClick(R.id.mrlayout_toolbar_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.message();
    }

    @Override
    public void setData(PunishEntity entity) {
        mtv_id.setText(entity.nickname+"("+entity.member_id+")");
        if ("1072".equals(mOPT)){
            mtv_tip.setTextColor(getColorResouce(R.color.pink_color));
        }else {
            mtv_tip.setTextColor(getColorResouce(R.color.value_03C780));
        }
        mtv_tip.setText(entity.title);
        mtv_time.setText(entity.desc);
        mtv_complaints.setText(entity.complaints);
        mtv_content.setText(entity.remark);

        if (!isEmpty(entity.pic_url)){
            visible(miv_pic);
            GlideUtils.getInstance().loadImage(this,miv_pic,entity.pic_url);
        }else {
            gone(miv_pic);
        }
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
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }
}
