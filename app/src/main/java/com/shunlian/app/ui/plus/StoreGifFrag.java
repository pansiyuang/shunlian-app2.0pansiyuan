package com.shunlian.app.ui.plus;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.SuperProductEntity;
import com.shunlian.app.presenter.SuperproductPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISuperProductView;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public class StoreGifFrag extends BaseFragment implements ISuperProductView, View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView tv_more_product;

    private SuperproductPresenter mPresenter;

    public static StoreGifFrag getInstance() {
        StoreGifFrag storeGifFrag = new StoreGifFrag();
        return storeGifFrag;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_store_gif, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);
        tv_more_product = (TextView) view.findViewById(R.id.tv_more_product);
        return view;
    }

    @Override
    protected void initData() {
        mPresenter = new SuperproductPresenter(getActivity(), this);
        mPresenter.getProductList();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(getActivity(), 5), false));
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initListener() {
        tv_more_product.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void getProductList(List<SuperProductEntity.SuperProduct> list) {
        recyclerView.setAdapter(new SimpleRecyclerAdapter<SuperProductEntity.SuperProduct>(getActivity(), R.layout.item_product_img, list) {
            @Override
            public void convert(SimpleViewHolder holder, SuperProductEntity.SuperProduct superProduct, int position) {
                MyImageView myImageView = holder.getView(R.id.miv_img);
                GlideUtils.getInstance().loadCornerImage(getActivity(), myImageView, superProduct.thumb, 5);
            }

            @Override
            public int getItemCount() {
                if (list.size() >= 4) {
                    return 4;
                }
                return super.getItemCount();
            }
        });
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View v) {
        SuperProductsAct.startAct(getActivity());
    }
}
