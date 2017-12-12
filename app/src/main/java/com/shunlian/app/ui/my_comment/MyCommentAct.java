package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MyCommentAdapter;
import com.shunlian.app.bean.MyCommentListEntity;
import com.shunlian.app.presenter.MyCommentListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IMyCommentListView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyCommentAct extends BaseActivity implements IMyCommentListView{

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_comment_all)
    MyTextView mtv_comment_all;

    @BindView(R.id.view_comment_all)
    View view_comment_all;

    @BindView(R.id.mtv_comment_append)
    MyTextView mtv_comment_append;

    @BindView(R.id.view_comment_append)
    View view_comment_append;
    private int pink_color;
    private int new_text;
    private MyCommentListPresenter presenter;
    private int currentPage = MyCommentListPresenter.ALL;


    public static void startAct(Context context) {
        context.startActivity(new Intent(context, MyCommentAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_my_comment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        pink_color = getResources().getColor(R.color.pink_color);
        new_text = getResources().getColor(R.color.new_text);

        presenter = new MyCommentListPresenter(this,this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        int space = TransformUtil.dip2px(this, 7.5f);
        recy_view.addItemDecoration(new VerticalItemDecoration(space, 0,
                0, getResources().getColor(R.color.white_ash)));

    }

    @OnClick(R.id.mllayout_all)
    public void allComment() {
        currentPage = MyCommentListPresenter.ALL;
        classState(1);
        presenter.myCommentListAll();
    }


    @OnClick(R.id.mllayout_append)
    public void appendComment() {
        currentPage = MyCommentListPresenter.APPEND;
        classState(2);
        presenter.myCommentListAppend();
    }

    private void classState(int state) {

        mtv_comment_all.setTextColor(state == 1 ? pink_color : new_text);
        view_comment_all.setVisibility(state == 1 ? View.VISIBLE : View.INVISIBLE);

        mtv_comment_append.setTextColor(state == 2 ? pink_color : new_text);
        view_comment_append.setVisibility(state == 2 ? View.VISIBLE : View.INVISIBLE);
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

    /**
     * 评价列表
     *
     * @param entity
     */
    @Override
    public void commentList(MyCommentListEntity entity) {
        if (currentPage == MyCommentListPresenter.ALL) {
            MyCommentAdapter adapter = new MyCommentAdapter(this, false,
                    DataUtil.getListString(20, "fff"));

            recy_view.setAdapter(adapter);
        }else {

        }
    }
}
