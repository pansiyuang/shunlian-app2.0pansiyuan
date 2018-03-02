package com.shunlian.app.ui.category;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.BrandlistAdapter;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.presenter.BrandListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.ui.myself_store.GoodsSearchAct;
import com.shunlian.app.view.IBrandListView;
import com.shunlian.app.widget.WaveSideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/28.
 */

public class BrandListAct extends BaseActivity implements IBrandListView {


    @BindView(R.id.rv_contacts)
    RecyclerView rv_contacts;

    @BindView(R.id.side_bar)
    WaveSideBar side_bar;

    private List<GetListFilterEntity.Brand.Item> mLogisticsName = new ArrayList<>();
    private LinearLayoutManager manager;

    public static void startAct(Activity activity) {
        Intent intent = new Intent(activity, BrandListAct.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_brandlist;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        BrandListPresenter presenter = new BrandListPresenter(this, this);
        manager = new LinearLayoutManager(this);
        rv_contacts.setLayoutManager(manager);

        side_bar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < mLogisticsName.size(); i++) {
                    if (mLogisticsName.get(i).first_letter.equalsIgnoreCase(index)) {
                        manager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void setBrandList(ArrayList<String> letters, List<GetListFilterEntity.Brand> brands) {
        side_bar.setIndexItems(letters);
        for (GetListFilterEntity.Brand nameItem : brands) {
            List<GetListFilterEntity.Brand.Item> item_list = nameItem.item_list;
            mLogisticsName.addAll(item_list);
        }

        BrandlistAdapter adapter = new BrandlistAdapter(this, mLogisticsName);
        rv_contacts.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GoodsSearchParam param = new GoodsSearchParam();
                param.keyword = mLogisticsName.get(position).brand_name;
                GoodsSearchAct.startAct(BrandListAct.this,param);
            }
        });
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
