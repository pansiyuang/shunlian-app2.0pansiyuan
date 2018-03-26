package com.shunlian.app.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.ExperienceDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.order.ExchangeDetailAct;
import com.shunlian.app.view.IExperienceDetailView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ExperienceDetailAct extends BaseActivity implements IExperienceDetailView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    private ExperienceDetailPresenter presenter;


    public static void startAct(Context context,String experience_id){
        Intent intent = new Intent(context, ExchangeDetailAct.class);
        intent.putExtra("experience_id",experience_id);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_experiencedetail;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        immersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .keyboardEnable(true)
                .init();

        String experience_id = getIntent().getStringExtra("experience_id");
        presenter = new ExperienceDetailPresenter(this,this,experience_id);

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
}
