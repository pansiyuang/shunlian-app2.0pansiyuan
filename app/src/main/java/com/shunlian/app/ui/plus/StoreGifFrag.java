package com.shunlian.app.ui.plus;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.GifProductEntity;
import com.shunlian.app.bean.SuperProductEntity;
import com.shunlian.app.listener.OnItemClickListener;
import com.shunlian.app.presenter.GifBagPresenter;
import com.shunlian.app.presenter.SuperproductPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IGifBagView;
import com.shunlian.app.view.ISuperProductView;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public class StoreGifFrag extends BaseFragment implements View.OnClickListener, IGifBagView {

    private RecyclerView recyclerView;
    private TextView tv_more_product;

    private GifBagPresenter mPresenter;
    private int recyclerViewWidth, imgWidth;
    private boolean isLoad = false;

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
        mPresenter = new GifBagPresenter(getActivity(), this);
        mPresenter.getGifList();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(getActivity(), 5), false));
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.post(() -> {
            recyclerViewWidth = recyclerView.getWidth();
            imgWidth = (recyclerViewWidth - TransformUtil.dip2px(getActivity(), 5)) / 2;

            if (isLoad && recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initListener() {
        tv_more_product.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View v) {
        GifBagListAct.startAct(getActivity());
    }

    @Override
    public void getGifList(List<GifProductEntity.Product> productList) {
        isLoad = true;
        recyclerView.setAdapter(new SimpleRecyclerAdapter<GifProductEntity.Product>(getActivity(), R.layout.item_product_img, productList) {
            @Override
            public void convert(SimpleViewHolder holder, GifProductEntity.Product product, int position) {
                MyImageView myImageView = holder.getView(R.id.miv_img);

                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(imgWidth, imgWidth);
                myImageView.setLayoutParams(layoutParams);

                GlideUtils.getInstance().loadCornerImage(getActivity(), myImageView, product.pic, 5);
                myImageView.setOnClickListener(v -> {
                    if(isEmpty(product.product_id)){
                        return;
                    }
                    PlusGifDetailAct.startAct(baseActivity,product.product_id);
                });
            }

            @Override
            public int getItemCount() {
                if (productList.size() > 4) {
                    return 4;
                }
                return super.getItemCount();
            }
        });
    }
}
