package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.SelectGoodsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/10/18.
 */

public class SelectGoodsAct extends BaseActivity implements IView{
    @BindView(R.id.ed_edit)
    EditText edEdit;

    @BindView(R.id.mtv_clear)
    MyTextView mtvClear;

    @BindView(R.id.recy_view)
    RecyclerView recyView;
    private LinearLayoutManager manager;
    private SelectGoodsPresenter presenter;

    public static void startAct(Activity activity,int code){
        activity.startActivityForResult(new Intent(activity,SelectGoodsAct.class),code);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_select_goodsv2;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recyView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null && presenter != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
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
        recyView.setLayoutManager(manager);
        int i = TransformUtil.dip2px(this, 12);
        recyView.addItemDecoration(new MVerticalItemDecoration(this,i,0,0));
        presenter = new SelectGoodsPresenter(this,this);
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
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recyView.setAdapter(adapter);
    }
}
