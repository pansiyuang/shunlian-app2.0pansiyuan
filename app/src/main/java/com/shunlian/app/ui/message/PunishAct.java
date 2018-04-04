package com.shunlian.app.ui.message;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.PunishPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IPunishView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/3.
 */

public class PunishAct extends BaseActivity implements IPunishView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    private PunishPresenter presenter;
    private LinearLayoutManager manager;

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
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    //分页
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        mtv_toolbar_title.setText(getStringResouce(R.string.punish));
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        presenter = new PunishPresenter(this,this);
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
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
