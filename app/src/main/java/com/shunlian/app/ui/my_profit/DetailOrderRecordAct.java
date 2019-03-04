package com.shunlian.app.ui.my_profit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.DetailOrderRecordPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IDetailOrderRecordView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/17.
 */

public class DetailOrderRecordAct extends BaseActivity implements IDetailOrderRecordView{

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.miv_title_right)
    MyImageView miv_title_right;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private DetailOrderRecordPresenter presenter;
    private LinearLayoutManager manager;
    private boolean isShow = true;

    public static void startAct(Context context){
        Intent intent = new Intent(context, DetailOrderRecordAct.class);
        if (!(context instanceof Activity)){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_detail_order_record;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null && presenter != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition +1 == manager.getItemCount()){
                        presenter.onRefresh();
                    }
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
        tv_title.setText("详细订单记录");
        miv_title_right.setImageResource(R.mipmap.icon_eyes_open);
        visible(miv_title_right);

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int i = TransformUtil.dip2px(this, 10);
        TransformUtil.expandViewTouchDelegate(miv_title_right, i, i, i, i);
        recy_view.addItemDecoration(new VerticalItemDecoration(i,0,0));
//        recy_view.addItemDecoration(new MVerticalItemDecoration(this,10,0,0));

        presenter = new DetailOrderRecordPresenter(this,this);

        miv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    isShow = false;
                    miv_title_right.setImageResource(R.mipmap.icon_eyes_closes);
                } else {
                    isShow = true;
                    miv_title_right.setImageResource(R.mipmap.icon_eyes_opens);
                }
                presenter.setIsShow(isShow);
            }
        });
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
        if (request_code == 100){
            gone(recy_view);
            visible(nei_empty);
            nei_empty.setText("您还没有详细订单记录")
                    .setImageResource(R.mipmap.img_empty_dingdan)
                    .setButtonText(null);
        }else {
            gone(nei_empty);
            visible(recy_view);
        }
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
            presenter = null;
        }
    }
}
