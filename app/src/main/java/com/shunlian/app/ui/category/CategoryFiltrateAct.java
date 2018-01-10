package com.shunlian.app.ui.category;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.adapter.PingpaiAdapter;
import com.shunlian.app.adapter.ShaixuanAttrAdapter;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.presenter.CategoryFiltratePresenter;
import com.shunlian.app.presenter.CategoryPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.CategoryFiltrateView;
import com.shunlian.app.view.ICategoryView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

public class CategoryFiltrateAct extends BaseActivity implements CategoryFiltrateView, ICategoryView {

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_locate)
    MyTextView mtv_locate;

    @BindView(R.id.mtv_cancel)
    MyTextView mtv_cancel;

    @BindView(R.id.mtv_baoyou)
    MyTextView mtv_baoyou;

    @BindView(R.id.mtv_zhusan)
    MyTextView mtv_zhusan;

    @BindView(R.id.mtv_jiangzhe)
    MyTextView mtv_jiangzhe;

    @BindView(R.id.mtv_finish)
    MyTextView mtv_finish;

    @BindView(R.id.miv_arrow)
    MyImageView miv_arrow;

    @BindView(R.id.mtv_more)
    MyTextView mtv_more;

    @BindView(R.id.edt_max)
    EditText edt_max;

    @BindView(R.id.edt_min)
    EditText edt_min;

    @BindView(R.id.view_pingpai)
    View view_pingpai;

    @BindView(R.id.mrlayout_pingpai)
    MyRelativeLayout mrlayout_pingpai;

    @BindView(R.id.rv_pingpai)
    RecyclerView rv_pingpai;

    @BindView(R.id.rv_category)
    RecyclerView rv_category;


    private CategoryFiltratePresenter categoryFiltratePresenter;
    private CategoryPresenter categoryPresenter;
    private PingpaiAdapter pingpaiAdapter;
    private ShaixuanAttrAdapter shaixuanAttrAdapter;
    private List<GetListFilterEntity.Brand> brands;
    private ArrayList<String> letters;
    private Boolean isZhu = false, isZhe = false, isDing = false, isBao = false, isMore = false;
    private String keyword, cid = "75", sort_type;

    public static void startAct(Context context, String keyword, String cid, String sort_type) {
        Intent intent = new Intent(context, CategoryFiltrateAct.class);
        intent.putExtra("keyword", keyword);
        intent.putExtra("cid", cid);
        intent.putExtra("sort_type", sort_type);
        context.startActivity(intent);
    }

    public void reset() {
        categoryFiltratePresenter.isSecond = false;

        mtv_baoyou.setBackgroundColor(getColorResouce(R.color.value_f5));
        isBao = false;

        edt_min.setText("");
        edt_max.setText("");

        if (pingpaiAdapter != null) {
            miv_arrow.setImageResource(R.mipmap.icon_saixuan_gd);
            mtv_more.setText(R.string.category_gengduo);
            pingpaiAdapter.isAll = false;
            isMore = false;
            if (Constant.BRAND_IDS != null) {
                Constant.BRAND_IDS.clear();
            }
            pingpaiAdapter.notifyDataSetChanged();
        }

        if (shaixuanAttrAdapter != null) {
            if (Constant.BRAND_IDSBEFORE == null) {
                Constant.BRAND_IDSBEFORE.clear();
            }
            if (Constant.BRAND_IDS == null) {
                Constant.BRAND_IDS.clear();
            }
            shaixuanAttrAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_filtrate_category;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pingpaiAdapter != null) {
            pingpaiAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        keyword = getIntent().getStringExtra("keyword");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("cid"))) {
            cid = getIntent().getStringExtra("cid");
        }
        sort_type = getIntent().getStringExtra("sort_type");
        mtv_address.setOnClickListener(this);
        categoryFiltratePresenter = new CategoryFiltratePresenter(this, this, cid, keyword);
        mtv_locate.setOnClickListener(this);
        mtv_cancel.setOnClickListener(this);
        mtv_finish.setOnClickListener(this);
        mtv_baoyou.setOnClickListener(this);
        mtv_zhusan.setOnClickListener(this);
        mtv_jiangzhe.setOnClickListener(this);
        miv_arrow.setOnClickListener(this);
        mtv_more.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.mtv_baoyou:
                if (isBao) {
                    mtv_baoyou.setBackgroundColor(getColorResouce(R.color.value_f5));
                    isBao = false;
                } else {
                    mtv_baoyou.setBackgroundResource(R.mipmap.img_xcha);
                    isBao = true;
                }
                break;
            case R.id.mtv_zhusan:
                if (isZhu) {
                    mtv_zhusan.setBackgroundColor(getColorResouce(R.color.value_f5));
                    isZhu = false;
                } else {
                    mtv_zhusan.setBackgroundResource(R.mipmap.img_xcha);
                    isZhu = true;
                    mtv_jiangzhe.setBackgroundColor(getColorResouce(R.color.value_f5));
                    mtv_address.setBackgroundColor(getColorResouce(R.color.value_f5));
                    isDing = false;
                    isZhe = false;
                }
                break;
            case R.id.mtv_jiangzhe:
                if (isZhe) {
                    mtv_jiangzhe.setBackgroundColor(getColorResouce(R.color.value_f5));
                    isZhe = false;
                } else {
                    mtv_jiangzhe.setBackgroundResource(R.mipmap.img_xcha);
                    isZhe = true;
                    mtv_zhusan.setBackgroundColor(getColorResouce(R.color.value_f5));
                    mtv_address.setBackgroundColor(getColorResouce(R.color.value_f5));
                    isDing = false;
                    isZhu = false;
                }
                break;
            case R.id.mtv_address:
                if (isDing) {
                    mtv_address.setBackgroundColor(getColorResouce(R.color.value_f5));
                    isDing = false;
                } else {
                    mtv_address.setBackgroundResource(R.mipmap.img_xcha);
                    isDing = true;
                    mtv_zhusan.setBackgroundColor(getColorResouce(R.color.value_f5));
                    mtv_jiangzhe.setBackgroundColor(getColorResouce(R.color.value_f5));
                    isZhe = false;
                    isZhu = false;
                }
                break;
            case R.id.mtv_reset:
                reset();
                break;
            case R.id.mtv_more:
            case R.id.miv_arrow:
                if (isMore) {
                    miv_arrow.setImageResource(R.mipmap.icon_saixuan_gd);
                    mtv_more.setText(R.string.category_gengduo);
                    pingpaiAdapter.isAll = false;
                    isMore = false;
                } else {
                    miv_arrow.setImageResource(R.mipmap.icon_saixuan_sq);
                    mtv_more.setText(R.string.category_shouqi);
                    pingpaiAdapter.isAll = true;
                    isMore = true;
                }
                pingpaiAdapter.notifyDataSetChanged();
                break;
            case R.id.mtv_locate:
                categoryFiltratePresenter.initGps();
                break;
            case R.id.mtv_cancel:
                reset();
                finish();
                break;
            case R.id.mtv_finish:
                GoodsSearchParam goodsSearchParam = new GoodsSearchParam();
                goodsSearchParam.keyword = keyword;
                goodsSearchParam.min_price = edt_min.getText().toString();
                goodsSearchParam.max_price = edt_max.getText().toString();
                if (isBao) {
                    goodsSearchParam.is_free_ship = "Y";
                }
                goodsSearchParam.cid = cid;
                if (isDing) {
                    goodsSearchParam.send_area = mtv_address.getText().toString();
                } else if (isZhe) {
                    goodsSearchParam.send_area = "江浙沪";
                } else if (isZhu) {
                    goodsSearchParam.send_area = "珠三角";
                }
                goodsSearchParam.sort_type = sort_type;
                String brand_ids="";
                for (int m = 0; m < Constant.BRAND_IDS.size(); m++) {
                    if (m >= Constant.BRAND_IDS.size() - 1) {
                        brand_ids += Constant.BRAND_IDS.get(m);
                        goodsSearchParam.brand_ids = brand_ids;

                        goodsSearchParam.attr_data=new ArrayList<>();

//                        for (int n = 0; n < Constant.BRAND_ATTRS.size(); n++) {
//                            GoodsSearchParam.Attr attr=new GoodsSearchParam.Attr();
//                            Set set = Constant.BRAND_ATTRS.keySet();
//                            HashMap hashmp = ne HashMap();
//                            hashmp.put("aa", "111");
//                            Set set = hashmp.keySet();
//                            Iterator iter = set.iterator();
//                            while (iter.hasNext()) {
//                                String key = (String) iter.next();
//                            }
//                            for (String key : list.get(pos).keySet() ) {
//                                myKey = key;
//                            }
//                            attr.attr_name=set[n];
//                        }
                        categoryPresenter = new CategoryPresenter(this, this);
                    } else {
                        brand_ids += Constant.BRAND_IDS.get(m) + ",";
                    }
                }

                break;
        }
    }

    @Override
    public void getListFilter(GetListFilterEntity getListFilterEntity) {
        brands = getListFilterEntity.brand_list;
        letters = getListFilterEntity.first_letter_list;
        if (pingpaiAdapter == null && getListFilterEntity.recommend_brand_list != null && getListFilterEntity.recommend_brand_list.size() > 0) {
            mrlayout_pingpai.setVisibility(View.VISIBLE);
            view_pingpai.setVisibility(View.VISIBLE);
            Constant.BRAND_IDS = new ArrayList<>();
            List<GetListFilterEntity.Recommend> recommends = new ArrayList<>();
            for (int i = 0; i < 11 || i < getListFilterEntity.recommend_brand_list.size(); i++) {
                GetListFilterEntity.Recommend recommend = getListFilterEntity.recommend_brand_list.get(i);
                recommends.add(recommend);
                if (i >= getListFilterEntity.recommend_brand_list.size() - 1 || i >= 10) {
                    if (getListFilterEntity.recommend_brand_list.size() > 10) {
                        recommends.add(getListFilterEntity.recommend_brand_list.get(0));
                    }
                    pingpaiAdapter = new PingpaiAdapter(this, false, recommends, brands, letters);
                    rv_pingpai.setLayoutManager(new GridLayoutManager(this, 3));
                    rv_pingpai.setAdapter(pingpaiAdapter);
                    rv_pingpai.setNestedScrollingEnabled(false);//防止滚动卡顿
                }
            }
        } else {
            mrlayout_pingpai.setVisibility(View.GONE);
            view_pingpai.setVisibility(View.GONE);
        }
        if (shaixuanAttrAdapter == null && getListFilterEntity.attr_list != null && getListFilterEntity.attr_list.size() > 0) {
            Constant.BRAND_ATTRS = new HashMap<>();
            rv_category.setLayoutManager(new LinearLayoutManager(this));
            shaixuanAttrAdapter = new ShaixuanAttrAdapter(this, false, getListFilterEntity.attr_list);
            rv_category.setAdapter(shaixuanAttrAdapter);
            rv_category.setNestedScrollingEnabled(false);//防止滚动卡顿
        }
    }

    @Override
    public void getGps(DistrictGetlocationEntity districtGetlocationEntity) {
        mtv_address.setText(districtGetlocationEntity.district_names.get(1));
        mtv_address.setBackgroundResource(R.mipmap.img_xcha);
        isDing = true;
        isZhe = false;
        isZhu = false;
        mtv_address.setBackgroundResource(R.mipmap.img_xcha);
//        mtv_zhusan.setBackgroundResource(0);
        mtv_zhusan.setBackgroundColor(getColorResouce(R.color.value_f5));
        mtv_jiangzhe.setBackgroundColor(getColorResouce(R.color.value_f5));
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getSearchGoods(SearchGoodsEntity goodsEntity) {

    }
}
