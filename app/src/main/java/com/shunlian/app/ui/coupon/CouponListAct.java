package com.shunlian.app.ui.coupon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.CouponListEntity;
import com.shunlian.app.presenter.CouponListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.core.GetCouponAct;
import com.shunlian.app.ui.help.HelpSolutionAct;
import com.shunlian.app.view.ICouponListView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/19.
 * 优惠券列表
 */

public class CouponListAct extends BaseActivity implements ICouponListView{

    @BindView(R.id.mtv_rule)
    MyTextView mtv_rule;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.llayout_no_used)
    MyLinearLayout llayout_no_used;

    @BindView(R.id.llayout_used)
    MyLinearLayout llayout_used;

    @BindView(R.id.llayout_already_used)
    MyLinearLayout llayout_already_used;

    @BindView(R.id.mtv_no_used)
    MyTextView mtv_no_used;

    @BindView(R.id.view_no_used)
    View view_no_used;

    @BindView(R.id.mtv_used)
    MyTextView mtv_used;

    @BindView(R.id.view_used)
    View view_used;

    @BindView(R.id.mtv_already_used)
    MyTextView mtv_already_used;

    @BindView(R.id.view_already_used)
    View view_already_used;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;


    private CouponListPresenter presenter;
    private LinearLayoutManager manager;
    private String helpId;


    public static void startAct(Context context){
        Intent intent=new Intent(context,CouponListAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_coupon_list;
    }

    @Override
    protected void initListener() {
        super.initListener();
        llayout_no_used.setOnClickListener(this);
        llayout_used.setOnClickListener(this);
        llayout_already_used.setOnClickListener(this);
        mtv_rule.setOnClickListener(this);

        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null && presenter != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()){
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

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        presenter = new CouponListPresenter(this,this);

    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        if (request_code == 0) {
            gone(recy_view);
            visible(nei_empty);
            nei_empty.setNetExecption().setOnClickListener(v -> {
                if (presenter != null) {
                    presenter.initApi();
                }
            });
        }
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

            nei_empty.setImageResource(R.mipmap.img_empty_youhuiquan)
                    .setText("您没有已过期的优惠券")
                    .setButtonText("去领券中心逛逛")
                    .setOnClickListener((view)-> GetCouponAct.startAct(this));
        }else {
            gone(nei_empty);
            visible(recy_view);
        }
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        gone(nei_empty);
        visible(recy_view);
        recy_view.setAdapter(adapter);
    }

    /**
     * 设置优惠券数量
     *
     * @param numInfo
     */
    @Override
    public void setCouponNum(CouponListEntity.NumInfo numInfo) {

        mtv_no_used.setText("未使用("+numInfo.not_used+")");
        mtv_used.setText("已使用("+numInfo.used+")");
        mtv_already_used.setText("已过期("+numInfo.expired+")");
        helpId = numInfo.help_id;
    }

    private void state(int state){
        int pink_color = getColorResouce(R.color.pink_color);
        int new_text = getColorResouce(R.color.new_text);

        mtv_no_used.setTextColor(state == 1?pink_color:new_text);
        view_no_used.setVisibility(state == 1 ? View.VISIBLE:View.INVISIBLE);

        mtv_used.setTextColor(state == 2?pink_color:new_text);
        view_used.setVisibility(state == 2 ? View.VISIBLE:View.INVISIBLE);

        mtv_already_used.setTextColor(state == 3?pink_color:new_text);
        view_already_used.setVisibility(state == 3 ? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.llayout_no_used:
                state(1);
                if (presenter != null){
                    presenter.current_state = presenter.no_used;
                    presenter.initApi();
                }
                break;
            case R.id.llayout_used:
                state(2);
                if (presenter != null){
                    presenter.current_state = presenter.used;
                    presenter.initApi();
                }
                break;
            case R.id.llayout_already_used:
                state(3);
                if (presenter != null){
                    presenter.current_state = presenter.already_used;
                    presenter.initApi();
                }
                break;
            case R.id.mtv_rule:
                HelpSolutionAct.startAct(this,helpId);
                break;
        }
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
