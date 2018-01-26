package com.shunlian.app.ui.category;

import android.app.Activity;
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
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @BindView(R.id.mtv_reset)
    MyTextView mtv_reset;

    @BindView(R.id.mtv_resets)
    MyTextView mtv_resets;

    @BindView(R.id.mtv_finishs)
    MyTextView mtv_finishs;

    @BindView(R.id.edt_max)
    EditText edt_max;

    @BindView(R.id.edt_min)
    EditText edt_min;

    @BindView(R.id.view_pingpai)
    View view_pingpai;

    @BindView(R.id.mrlayout_pingpai)
    MyRelativeLayout mrlayout_pingpai;

    @BindView(R.id.mllayout_bottom)
    MyLinearLayout mllayout_bottom;

    @BindView(R.id.mllayout_bottoms)
    MyLinearLayout mllayout_bottoms;

    @BindView(R.id.rv_pingpai)
    RecyclerView rv_pingpai;

    @BindView(R.id.rv_category)
    RecyclerView rv_category;


    private CategoryFiltratePresenter categoryFiltratePresenter;
    private PingpaiAdapter pingpaiAdapter;
    private ShaixuanAttrAdapter shaixuanAttrAdapter;
    private List<GetListFilterEntity.Brand> brands;
    private ArrayList<String> letters;
    private Boolean isZhu = false, isZhe = false, isDing = false, isBao = false, isMore = false, isopt = false;
    private String keyword, cid = "", sort_type, locate;

    public static void startAct(Activity context, String keyword, String cid, String sort_type) {
        Intent intent = new Intent(context, CategoryFiltrateAct.class);
        intent.putExtra("keyword", keyword);
        intent.putExtra("cid", cid);
        intent.putExtra("sort_type", sort_type);
        context.startActivityForResult(intent, CategoryFiltratePresenter.FILTRATE_REQUEST_CODE);
    }

    public void reset() {
        if (Constant.SEARCHPARAM != null) {
            Constant.SEARCHPARAM.send_area = "";
            Constant.SEARCHPARAM.brand_ids = "";
            if (Constant.SEARCHPARAM.attr_data != null) {
                Constant.SEARCHPARAM.attr_data.clear();
            }
            Constant.SEARCHPARAM.is_free_ship = "";
            Constant.SEARCHPARAM.max_price = "";
            Constant.SEARCHPARAM.min_price = "";
        }
        categoryFiltratePresenter.isSecond = false;
        isopt = false;
        initLocate();
        categoryFiltratePresenter.initGps();

        mtv_baoyou.setBackgroundColor(getColorResouce(R.color.value_f5));
        isBao = false;

        edt_min.setText("");
        edt_max.setText("");

        if (pingpaiAdapter != null) {
//            miv_arrow.setImageResource(R.mipmap.icon_saixuan_gd);
//            mtv_more.setText(R.string.category_gengduo);
//            pingpaiAdapter.isAll = false;
//            isMore = false;
            if (Constant.BRAND_IDS != null) {
                Constant.BRAND_IDS.clear();
            }
            if (Constant.BRAND_IDSBEFORE != null) {
                Constant.BRAND_IDSBEFORE.clear();
            }
            pingpaiAdapter.notifyDataSetChanged();
        }

        if (shaixuanAttrAdapter != null) {
            if (Constant.BRAND_ATTRS != null) {
                Constant.BRAND_ATTRS.clear();
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

    public void reBuildData() {
        if (!TextUtils.isEmpty(Constant.DINGWEI)) {
            locate = Constant.DINGWEI;
            mtv_address.setBackgroundResource(R.mipmap.icon_dizhi);
            mtv_address.setText("      " + locate);
        }
        initList(Constant.LISTFILTER);
        if (Constant.SEARCHPARAM != null) {
            if (Constant.SEARCHPARAM.isMore) {
                miv_arrow.setImageResource(R.mipmap.icon_saixuan_sq);
                mtv_more.setText(R.string.category_shouqi);
                isMore = true;
            } else {
                miv_arrow.setImageResource(R.mipmap.icon_saixuan_gd);
                mtv_more.setText(R.string.category_gengduo);
                isMore = false;
            }
            if (Constant.BRAND_IDS == null) {
                Constant.BRAND_IDS = new ArrayList<>();
            } else {
                Constant.BRAND_IDS.clear();
            }
            if (Constant.REBRAND_IDS != null)
                Constant.BRAND_IDS.addAll(Constant.REBRAND_IDS);
            if (Constant.BRAND_ATTRS == null) {
                Constant.BRAND_ATTRS = new HashMap<>();
            } else {
                Constant.BRAND_ATTRS.clear();
            }
            if (Constant.REBRAND_ATTRS != null)
                Constant.BRAND_ATTRS.putAll(Constant.REBRAND_ATTRS);

            if ("Y".equals(Constant.SEARCHPARAM.is_free_ship)) {
                isBao = true;
                mtv_baoyou.setBackgroundResource(R.mipmap.img_xcha);
            } else {
                isBao = false;
                mtv_baoyou.setBackgroundColor(getColorResouce(R.color.value_f5));
            }
            if (!TextUtils.isEmpty(Constant.SEARCHPARAM.min_price)) {
                edt_min.setText(Constant.SEARCHPARAM.min_price);
            }
            if (!TextUtils.isEmpty(Constant.SEARCHPARAM.max_price)) {
                edt_max.setText(Constant.SEARCHPARAM.max_price);
            }
            if (!TextUtils.isEmpty(Constant.SEARCHPARAM.send_area)) {
                switch (Constant.SEARCHPARAM.send_area) {
                    case "珠三角":
                        mtv_zhusan.setBackgroundResource(R.mipmap.img_xcha);
                        isZhu = true;
                        isZhe = false;
                        isDing = false;
                        break;
                    case "江浙沪":
                        mtv_jiangzhe.setBackgroundResource(R.mipmap.img_xcha);
                        isZhu = false;
                        isZhe = true;
                        isDing = false;
                        break;
                    default:
                        mtv_address.setBackgroundResource(R.mipmap.img_xcha);
                        mtv_address.setText(locate);
                        isZhu = false;
                        isZhe = false;
                        isDing = true;
                        break;
                }
            }
        }
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        locate = getStringResouce(R.string.category_dingwei);
        categoryFiltratePresenter = new CategoryFiltratePresenter(this, this, cid, keyword);
        keyword = getIntent().getStringExtra("keyword");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("cid"))) {
            cid = getIntent().getStringExtra("cid");
        }
        sort_type = getIntent().getStringExtra("sort_type");

        if (Constant.LISTFILTER != null) {
            reBuildData();
        } else {
            categoryFiltratePresenter.initApiData();
        }

        mtv_address.setOnClickListener(this);
        mtv_locate.setOnClickListener(this);
        mtv_cancel.setOnClickListener(this);
        mtv_reset.setOnClickListener(this);
        mtv_finish.setOnClickListener(this);
        mtv_resets.setOnClickListener(this);
        mtv_finishs.setOnClickListener(this);
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
                    mtv_address.setBackgroundResource(R.mipmap.icon_dizhi);
                    mtv_address.setText("      " + locate);
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
                    mtv_address.setBackgroundResource(R.mipmap.icon_dizhi);
                    mtv_address.setText("      " + locate);
                    isDing = false;
                    isZhu = false;
                }
                break;
            case R.id.mtv_address:
                if (isDing) {
                    mtv_address.setBackgroundResource(R.mipmap.icon_dizhi);
                    mtv_address.setText("      " + locate);
                    isDing = false;
                } else {
                    mtv_address.setBackgroundResource(R.mipmap.img_xcha);
                    mtv_address.setText(locate);
                    isDing = true;
                    mtv_zhusan.setBackgroundColor(getColorResouce(R.color.value_f5));
                    mtv_jiangzhe.setBackgroundColor(getColorResouce(R.color.value_f5));
                    isZhe = false;
                    isZhu = false;
                }
                break;
            case R.id.mtv_reset:
            case R.id.mtv_resets:
                reset();
                break;
            case R.id.mtv_more:
            case R.id.miv_arrow:
                if (isMore) {
                    miv_arrow.setImageResource(R.mipmap.icon_saixuan_gd);
                    mtv_more.setText(R.string.category_gengduo);
                    isMore = false;
                    pingpaiAdapter.isAll = false;
                } else {
                    miv_arrow.setImageResource(R.mipmap.icon_saixuan_sq);
                    mtv_more.setText(R.string.category_shouqi);
                    isMore = true;
                    pingpaiAdapter.isAll = true;
                }
                pingpaiAdapter.notifyDataSetChanged();
                break;
            case R.id.mtv_locate:
                isopt = true;
                categoryFiltratePresenter.initGps();
                break;
            case R.id.mtv_cancel:
                finish();
                break;
            case R.id.mtv_finish:
            case R.id.mtv_finishs:
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
                goodsSearchParam.isMore = isMore;
                categoryFiltratePresenter.dealBrandIds(goodsSearchParam);
                break;
        }
    }

    public void initList(GetListFilterEntity getListFilterEntity) {
        brands = getListFilterEntity.brand_list;
        letters = getListFilterEntity.first_letter_list;

        if (pingpaiAdapter == null && getListFilterEntity.recommend_brand_list != null && getListFilterEntity.recommend_brand_list.size() > 0) {
            mrlayout_pingpai.setVisibility(View.VISIBLE);
            view_pingpai.setVisibility(View.VISIBLE);
            categoryFiltratePresenter.dealRecommendBrand(getListFilterEntity);
        } else {
            mrlayout_pingpai.setVisibility(View.GONE);
            view_pingpai.setVisibility(View.GONE);
        }
        if (shaixuanAttrAdapter == null && getListFilterEntity.attr_list != null && getListFilterEntity.attr_list.size() > 0) {
            if (Constant.BRAND_ATTRS == null) {
                Constant.BRAND_ATTRS = new HashMap<>();
            }
            if (Constant.BRAND_ATTRNAME == null) {
                Constant.BRAND_ATTRNAME = new ArrayList<>();
            }
            rv_category.setLayoutManager(new LinearLayoutManager(this));
            shaixuanAttrAdapter = new ShaixuanAttrAdapter(this, false, getListFilterEntity.attr_list);
            rv_category.setAdapter(shaixuanAttrAdapter);
            rv_category.setNestedScrollingEnabled(false);//防止滚动卡顿
        }
        if (pingpaiAdapter == null && shaixuanAttrAdapter == null) {
            mllayout_bottom.setVisibility(View.GONE);
            mllayout_bottoms.setVisibility(View.VISIBLE);
            rv_category.setVisibility(View.GONE);
        } else {
            mllayout_bottom.setVisibility(View.VISIBLE);
            mllayout_bottoms.setVisibility(View.GONE);
        }
    }

    @Override
    public void getListFilter(GetListFilterEntity getListFilterEntity) {
        Constant.LISTFILTER = getListFilterEntity;
        initList(getListFilterEntity);
    }

    public void initLocate() {
        if (isopt) {
            mtv_address.setText(locate);
            mtv_address.setBackgroundResource(R.mipmap.img_xcha);
            isDing = true;
        } else {
            mtv_address.setText("      " + locate);
            mtv_address.setBackgroundResource(R.mipmap.icon_dizhi);
            isDing = false;
        }
        isZhe = false;
        isZhu = false;
//        mtv_zhusan.setBackgroundResource(0);
        mtv_zhusan.setBackgroundColor(getColorResouce(R.color.value_f5));
        mtv_jiangzhe.setBackgroundColor(getColorResouce(R.color.value_f5));
    }

    @Override
    public void getGps(DistrictGetlocationEntity districtGetlocationEntity) {
        locate = districtGetlocationEntity.district_names.get(1);
        Constant.DINGWEI = locate;
        initLocate();
    }

    @Override
    public void initPingpai(List<GetListFilterEntity.Recommend> recommends) {
        pingpaiAdapter = new PingpaiAdapter(this, false, recommends, brands, letters, isMore);
        rv_pingpai.setLayoutManager(new GridLayoutManager(this, 3));
        rv_pingpai.setAdapter(pingpaiAdapter);
        rv_pingpai.setNestedScrollingEnabled(false);//防止滚动卡顿
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getSearchGoods(SearchGoodsEntity goodsEntity, int page, int allPage) {

    }
}
