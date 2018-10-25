package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.NearAddressPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IView;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/10/17.
 * 附近位置
 */

public class NearAddressAct extends BaseActivity implements IView{

    @BindView(R.id.ed_edit)
    EditText edEdit;

    @BindView(R.id.recy_view)
    RecyclerView recyView;
    private NearAddressPresenter presenter;

    public static void startAct(Activity activity,int code){
        activity.startActivityForResult(new Intent(activity,NearAddressAct.class),code);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_near_address;
    }

    @Override
    protected void initListener() {
        super.initListener();
        edEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Common.hideKeyboard(edEdit);
                if (presenter != null){
                    presenter.getNearAddr(edEdit.getText().toString());
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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);

        presenter = new NearAddressPresenter(this,this);
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
