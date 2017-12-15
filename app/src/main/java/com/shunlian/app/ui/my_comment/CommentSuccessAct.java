package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommentSuccessAdapter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.DataUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class CommentSuccessAct extends BaseActivity {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;


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

        CommentSuccessAdapter adapter = new
                CommentSuccessAdapter(this,false, DataUtil.getListString(10,""));
        recy_view.setAdapter(adapter);
    }
}
