package com.shunlian.app.ui.collection;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.presenter.SearchResultPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ICollectionSearchResultView;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/25.
 * 收藏搜索结果
 */

public class SearchResultAct extends BaseActivity implements ICollectionSearchResultView{


    private String keyword;
    private String type;
    private SearchResultPresenter mPresenter;

    public static void startAct(Context context, String keyword, String type){
        Intent intent = new Intent(context,SearchResultAct.class);
        intent.putExtra("keyword",keyword);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    /**
     * 布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_search_result;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        type = intent.getStringExtra("type");

        mPresenter = new SearchResultPresenter(this,this,keyword,type);
    }

    @OnClick(R.id.tv_search_cancel)
    public void cancel(){
        finish();
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
