package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.GifProductAdapter;
import com.shunlian.app.bean.GifProductEntity;
import com.shunlian.app.presenter.GifBagPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IGifBagView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/23.
 */

public class GifBagListAct extends BaseActivity implements IGifBagView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private GifProductAdapter mAdapter;
    private GifBagPresenter mPresenter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, GifBagListAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_gifbaglist;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.gif_bag_list));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
        recycler_list.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 6.5f), 0, 0, getColorResouce(R.color.white_ash)));

        mPresenter = new GifBagPresenter(this, this);
        mPresenter.getGifList();
    }

    @Override
    public void getGifList(List<GifProductEntity.Product> productList) {
        if (!isEmpty(productList)) {
            mAdapter = new GifProductAdapter(this, productList);
            mAdapter.setOnItemClickListener((view, position) -> {
                GifProductEntity.Product product = productList.get(position);
                PlusGifDetailAct.startAct(GifBagListAct.this, product.product_id);
            });
            recycler_list.setAdapter(mAdapter);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
