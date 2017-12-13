package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CreatCommentAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CreatCommentActivity extends BaseActivity {
    @BindView(R.id.recycler_creat_comment)
    RecyclerView recycler_creat_comment;

    @BindView(R.id.tv_title_left)
    TextView tv_title_left;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    private List<GoodsDeatilEntity.Goods> goodsList;
    private CreatCommentAdapter creatCommentAdapter;


    public static void startAct(Context context) {
        Intent intent = new Intent(context, CreatCommentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_creat_comment;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title_left.setText("评价晒单");
        tv_title_left.setVisibility(View.VISIBLE);

        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText(R.string.comment_release);
        tv_title_right.setTextColor(getColorResouce(R.color.pink_color));

        goodsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GoodsDeatilEntity.Goods goods = new GoodsDeatilEntity.Goods();
            goods.goods_title = "商品" + (i + 1);
            goods.price = String.valueOf(i + 5);
            goodsList.add(goods);
        }

        creatCommentAdapter = new CreatCommentAdapter(this, false, goodsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_creat_comment.setNestedScrollingEnabled(false);
        recycler_creat_comment.setLayoutManager(linearLayoutManager);
        recycler_creat_comment.setAdapter(creatCommentAdapter);
        recycler_creat_comment.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 7), 0, 0));
    }
}
