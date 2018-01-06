package com.shunlian.app.ui.category;

import android.location.Location;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ContactsAdapter;
import com.shunlian.app.adapter.PingpaiAdapter;
import com.shunlian.app.adapter.ShaixuanAttrAdapter;
import com.shunlian.app.bean.Contact;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.presenter.CategoryFiltratePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.CategoryFiltrateView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.WaveSideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CategoryFiltrateAct extends BaseActivity implements CategoryFiltrateView {

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_locate)
    MyTextView mtv_locate;

    @BindView(R.id.mtv_cancel)
    MyTextView mtv_cancel;

    @BindView(R.id.mtv_finish)
    MyTextView mtv_finish;

    @BindView(R.id.rv_pingpai)
    RecyclerView rv_pingpai;

    @BindView(R.id.rv_category)
    RecyclerView rv_category;


    private String district_ids;
    private CategoryFiltratePresenter categoryFiltratePresenter;
    private PingpaiAdapter pingpaiAdapter;
    private ShaixuanAttrAdapter shaixuanAttrAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.act_filtrate_category;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_address.setOnClickListener(this);
        categoryFiltratePresenter=new CategoryFiltratePresenter(this,this,"6","");
        Location location = Common.getGPS(this);
        if (location != null) {
            categoryFiltratePresenter.initGps(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
        }
        mtv_locate.setOnClickListener(this);
        mtv_cancel.setOnClickListener(this);
        mtv_finish.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.mtv_reset:

                break;
            case R.id.mtv_locate:
                Location location = Common.getGPS(this);
                if (location != null) {
                    categoryFiltratePresenter.initGps(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
                }
                break;
            case R.id.mtv_cancel:

                break;
            case R.id.mtv_finish:

                break;
        }
    }

    @Override
    public void getListFilter(GetListFilterEntity getListFilterEntity) {
        rv_pingpai.setLayoutManager(new GridLayoutManager(this,3));
        if (pingpaiAdapter==null){
            pingpaiAdapter=new PingpaiAdapter(this, false,getListFilterEntity.recommend_brand_list);
        }
        rv_pingpai.setAdapter(pingpaiAdapter);

        rv_category.setLayoutManager(new LinearLayoutManager(this));
        if (shaixuanAttrAdapter==null){
            shaixuanAttrAdapter=new ShaixuanAttrAdapter(this, false,getListFilterEntity.attr_list);
        }
        rv_category.setAdapter(shaixuanAttrAdapter);

    }

    @Override
    public void getGps(DistrictGetlocationEntity districtGetlocationEntity) {
        district_ids = "";
        for (int m = 0; m < districtGetlocationEntity.district_ids.size(); m++) {
            if (m >= districtGetlocationEntity.district_ids.size() - 1) {
                district_ids += districtGetlocationEntity.district_ids.get(m);
            } else {
                district_ids += districtGetlocationEntity.district_ids.get(m) + ",";
            }
        }
        mtv_address.setText(districtGetlocationEntity.district_names.get(1));
        mtv_address.setBackgroundResource(R.mipmap.img_xcha);
    }
    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
