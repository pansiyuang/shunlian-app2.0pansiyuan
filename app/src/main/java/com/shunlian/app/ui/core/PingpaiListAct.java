package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.PingListAdapter;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.presenter.PPingList;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.timer.DayRedBlackDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.view.IPingList;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class PingpaiListAct extends BaseActivity implements View.OnClickListener, IPingList {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    @BindView(R.id.view_bg)
    View view_bg;

    @BindView(R.id.miv_arrow)
    MyImageView miv_arrow;

    @BindView(R.id.miv_dot)
    MyImageView miv_dot;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.miv_avar)
    MyImageView miv_avar;

    @BindView(R.id.miv_photo)
    MyImageView miv_photo;

    @BindView(R.id.downTime_firsts)
    DayRedBlackDownTimerView downTime_firsts;

    @BindView(R.id.msv_out)
    MyScrollView msv_out;

    private PPingList pPingList;
    private PingListAdapter pingListAdapter;
    private boolean isMore=false;

    public static void startAct(Context context, String id) {
        Intent intent = new Intent(context, PingpaiListAct.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.miv_arrow:
                if (isMore){
                    miv_arrow.setImageResource(R.mipmap.icon_common_arrowdowns);
                    mtv_desc.setLines(2);
                    isMore=false;
                }else {
                    isMore=true;
                    mtv_desc.setLines(3);
                    miv_arrow.setImageResource(R.mipmap.icon_common_arrowups);
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        setHideStatus();
        return R.layout.act_ping_list;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_arrow.setOnClickListener(this);
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom && pPingList != null) {
                    pPingList.refreshBaby();
                }
                float alpha = ((float) y) / 250;
                if (y > 250) {
                    mtv_title.setAlpha(1);
                    view_bg.setAlpha(1);
                } else if (y > 150) {
                    mtv_title.setTextColor(getColorResouce(R.color.new_text));
                    view_bg.setAlpha(alpha);
                    mtv_title.setAlpha(alpha);
                    miv_close.setImageResource(R.mipmap.icon_common_back_black);
                    miv_dot.setImageResource(R.mipmap.icon_common_more_black);
                    miv_close.setAlpha(alpha);
                    miv_dot.setAlpha(alpha);
                } else if (y > 0) {
                    mtv_title.setTextColor(getColorResouce(R.color.white));
                    view_bg.setAlpha(0);
                    mtv_title.setAlpha(1);
                    miv_close.setAlpha(1 - alpha);
                    miv_dot.setAlpha(1 - alpha);
                    miv_close.setImageResource(R.mipmap.icon_common_back_white);
                    miv_dot.setImageResource(R.mipmap.icon_common_more_white);
                }
            }
        });
    }

    @Override
    protected void initData() {
//        pPingList = new PPingList(this, this, getIntent().getStringExtra("id"));
        pPingList = new PPingList(this, this, "1");
        pPingList.getApiData();
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    protected void onDestroy() {
        if (downTime_firsts!=null){
            downTime_firsts.cancelDownTimer();
            downTime_firsts=null;
        }
        super.onDestroy();
    }

    @Override
    public void setApiData(CorePingEntity corePingEntity, List<CorePingEntity.MData> mDatas) {
        if (pingListAdapter==null){
            downTime_firsts.setDownTime(Integer.parseInt(corePingEntity.brand.count_down));
            downTime_firsts.setDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onFinish() {
                    downTime_firsts.cancelDownTimer();
                }

            });
            downTime_firsts.startDownTimer();
            mtv_title.setText(corePingEntity.brand.title);
            mtv_desc.setText(corePingEntity.brand.content);
            GlideUtils.getInstance().loadImage(getBaseContext(),miv_photo,corePingEntity.brand.img);
            GlideUtils.getInstance().loadImage(getBaseContext(),miv_avar,corePingEntity.brand.logo);
            pingListAdapter=new PingListAdapter(getBaseContext(),mDatas);
            rv_list.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
            rv_list.setNestedScrollingEnabled(false);
            rv_list.setAdapter(pingListAdapter);
            pingListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GoodsDetailAct.startAct(getBaseContext(),mDatas.get(position).id);
                }
            });
        }else {
            pingListAdapter.notifyDataSetChanged();
        }
        pingListAdapter.setPageLoading(Integer.parseInt(corePingEntity.page),Integer.parseInt(corePingEntity.total_page));
    }
}
