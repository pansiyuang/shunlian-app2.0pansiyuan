package com.shunlian.app.ui.category;

import android.content.Context;
import android.content.Intent;
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

import java.io.Serializable;
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
    private List<GetListFilterEntity.Brand> brands;
    private ArrayList<String> letters;
    @Override
    protected int getLayoutId() {
        return R.layout.act_filtrate_category;
    }

    public static void startAct(Context context, String orderId) {
        Intent intent = new Intent(context, CategoryFiltrateAct.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_address.setOnClickListener(this);
        categoryFiltratePresenter=new CategoryFiltratePresenter(this,this,"75","");
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
                categoryFiltratePresenter.initGps();
                break;
            case R.id.mtv_cancel:

                break;
            case R.id.mtv_finish:
                if (letters!=null&&brands!=null&&letters.size()>0){
                    CategoryLetterAct.startAct(this,brands,letters);
                }
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
        rv_pingpai.setNestedScrollingEnabled(false);//防止滚动卡顿

        rv_category.setLayoutManager(new LinearLayoutManager(this));
        if (shaixuanAttrAdapter==null){
            shaixuanAttrAdapter=new ShaixuanAttrAdapter(this, false,getListFilterEntity.attr_list);
        }
        rv_category.setAdapter(shaixuanAttrAdapter);
        rv_category.setNestedScrollingEnabled(false);//防止滚动卡顿

        brands=getListFilterEntity.brand_list;
        letters=getListFilterEntity.first_letter_list;
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
