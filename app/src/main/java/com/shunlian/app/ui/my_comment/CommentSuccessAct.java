package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommentSuccessAdapter;
import com.shunlian.app.bean.CommentSuccessEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.presenter.CommentSuccessPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ICommentSuccessView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class CommentSuccessAct extends BaseActivity implements ICommentSuccessView{

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    private CommentSuccessPresenter presenter;
    private List<CommentSuccessEntity.Comment> otherComments = new ArrayList<>();
    private int commentSize = 0;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,CommentSuccessAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_comment_success;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        presenter = new CommentSuccessPresenter(this,this);
    }

    /**
     * 其他评价列表
     *
     * @param comment
     * @param append
     */
    @Override
    public void otherCommentList(List<CommentSuccessEntity.Comment> comment, List<CommentSuccessEntity.Comment> append) {

        if (comment != null){
            otherComments.addAll(comment);
            commentSize = comment.size();
        }

        if (append != null){
            otherComments.addAll(append);
        }

        CommentSuccessAdapter adapter = new
                CommentSuccessAdapter(this,false,otherComments,commentSize);
        recy_view.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
            CommentSuccessEntity.Comment comment1 = otherComments.get(position - 1);
            if (position < commentSize + 1){
                ReleaseCommentEntity entity = new ReleaseCommentEntity(comment1.order_sn,comment1.thumb,
                        comment1.title,comment1.price,comment1.goods_id);

                CreatCommentActivity.startAct(CommentSuccessAct.this,
                        entity,CreatCommentActivity.CREAT_COMMENT);
            }else {
                ReleaseCommentEntity entity = new ReleaseCommentEntity(comment1.thumb,
                        comment1.title,comment1.price,comment1.comment_id);

                CreatCommentActivity.startAct(CommentSuccessAct.this,
                        entity,CreatCommentActivity.APPEND_COMMENT);
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

    }
}
