package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.SelectGoodsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.view.IView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/10/18.
 */

public class SelectGoodsAct extends BaseActivity implements IView{

    @BindView(R.id.ed_edit)
    EditText edEdit;

    @BindView(R.id.recy_view)
    RecyclerView recyView;
    private LinearLayoutManager manager;
    private SelectGoodsPresenter presenter;

    public static void startAct(Activity activity, String goodsid, int code){
        activity.startActivityForResult(new Intent(activity,SelectGoodsAct.class)
                .putExtra("goods",goodsid),code);
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

        edEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Common.hideKeyboard(edEdit);
                if (presenter != null){
                    presenter.getSearchGoods(edEdit.getText().toString(),true);
                    if (recyView != null){
                        recyView.scrollToPosition(0);
                    }
                }
            }
            return false;
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        String goods = getIntent().getStringExtra("goods");
        List<String> mSelectList = null;
        if (!isEmpty(goods)){
            String[] split = goods.split(",");
            mSelectList = Arrays.asList(split);
        }
        manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        recyView.addItemDecoration(new MVerticalItemDecoration(this,12,0,0));
        presenter = new SelectGoodsPresenter(this,this,mSelectList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
            presenter = null;
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
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recyView.setAdapter(adapter);
    }
}
