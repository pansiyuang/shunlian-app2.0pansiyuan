package com.shunlian.app.ui.discover_new.comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.bean.CommentRejectedEntity;
import com.shunlian.app.presenter.CommentRejectedPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.view.ICommentRejectedView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by zhanghe on 2019/1/23.
 */

public class CommentRejectedAct extends BaseActivity implements ICommentRejectedView {


    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.mtv_toolbar_right)
    MyTextView mtv_toolbar_right;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.met_rejected_content)
    EditText met_rejected_content;
    private CommentRejectedPresenter mPresenter;
    private String comment_id;


    public static void startAct(Context context,String comment_id){
        context.startActivity(new Intent(context,CommentRejectedAct.class)
                .putExtra("comment_id",comment_id));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_comment_rejected;
    }

    @Override
    protected void initListener() {
        super.initListener();
        met_rejected_content.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (s.length() > 0){
                    met_rejected_content.setGravity(Gravity.TOP | Gravity.LEFT);
                    met_rejected_content.setSelection(s.length());
                }else {
                    met_rejected_content.setGravity(Gravity.CENTER);
                }

                if (s.length() > 300){
                    met_rejected_content.setText(s.subSequence(0,300));
                    met_rejected_content.setSelection(s.length());
                }
            }
        });


        mtv_toolbar_right.setOnClickListener(v -> {
            String remark = met_rejected_content.getText().toString();
            if (!isEmpty(remark)){
                mPresenter.commentCheck(comment_id,remark);
            }else {
                Common.staticToast("请输入驳回原因");
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setTitleBar();
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        comment_id = getIntent().getStringExtra("comment_id");

        mPresenter = new CommentRejectedPresenter(this,this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

    }

    private void setTitleBar() {
        mtv_toolbar_title.setText("驳回原因");
        mtv_toolbar_title.setTextColor(getColorResouce(R.color.value_484848));
        mtv_toolbar_title.setTextSize(19);
        gone(mrlayout_toolbar_more);
        visible(mtv_toolbar_right);
        mtv_toolbar_right.setText("提交");
        mtv_toolbar_right.setTextColor(getColorResouce(R.color.value_484848));
        mtv_toolbar_right.setTextSize(15);
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
    public void setAdapter(RecyclerView.Adapter adapter) {
        recy_view.setAdapter(adapter);
    }

    @Override
    public void selectRejectedContent(CommentRejectedEntity.RejectedList list) {
        met_rejected_content.setText(list.value);
    }
}
