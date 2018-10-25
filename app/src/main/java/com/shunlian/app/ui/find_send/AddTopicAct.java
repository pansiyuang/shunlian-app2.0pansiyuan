package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.AddTopicPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IView;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/10/18.
 */

public class AddTopicAct extends BaseActivity implements IView{


    @BindView(R.id.ed_edit)
    EditText edEdit;

    @BindView(R.id.miv_not)
    MyImageView mivNot;

    @BindView(R.id.llayout_not)
    RelativeLayout llayoutNot;

    @BindView(R.id.recy_view)
    RecyclerView recyView;
    private AddTopicPresenter presenter;
    private LinearLayoutManager manager;

    public static void startAct(Activity activity, int code) {
        activity.startActivityForResult(new Intent(activity, AddTopicAct.class), code);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_add_topic;
    }

    @Override
    protected void initListener() {
        super.initListener();
        edEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Common.hideKeyboard(edEdit);
                if (presenter != null){
                    presenter.key_word = edEdit.getText().toString();
                    presenter.initApi();
                }
            }
            return false;
        });

        recyView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        recyView.setLayoutManager(manager);
        presenter = new AddTopicPresenter(this,this);
    }


    @OnClick(R.id.llayout_not)
    public void not_select(){
        visible(mivNot);
        mivNot.postDelayed(()->{
            Intent intent = new Intent();
            intent.putExtra("title","");
            setResult(Activity.RESULT_OK,intent);
            finish();
        },400);
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
