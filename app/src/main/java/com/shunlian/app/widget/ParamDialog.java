package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ProductDetailEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.flowlayout.FlowLayout;
import com.shunlian.app.widget.flowlayout.TagAdapter;
import com.shunlian.app.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by Administrator on 2017/11/10.
 */

public class ParamDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.iv_dialogPhoto)
    MyImageView iv_dialogPhoto;

    @BindView(R.id.dia_tv_price)
    TextView dia_tv_price;

    @BindView(R.id.tv_count)
    TextView tv_count;

    @BindView(R.id.recycler_param)
    RecyclerView recycler_param;

    @BindView(R.id.btn_minus)
    Button btn_minus;

    @BindView(R.id.btn_add)
    Button btn_add;

    @BindView(R.id.tv_number)
    TextView tv_number;

    @BindView(R.id.tv_param)
    TextView tv_param;

    @BindView(R.id.btn_complete)
    Button btn_complete;

    @BindView(R.id.iv_cancel)
    MyImageView iv_cancel;

    @BindView(R.id.rLayout_count)
    RelativeLayout rLayout_count;

    private List<GoodsDeatilEntity.Specs> specs;
    private List<GoodsDeatilEntity.Sku> mSku;
    private GoodsDeatilEntity goodsDeatilEntity;
    private Context mContext;
    private GoodsDeatilEntity.Goods mGoods;
    private ProductDetailEntity productDetailEntity;
    private LinearLayoutManager linearLayoutManager;
    private ParamItemAdapter paramItemAdapter;
    private List<GoodsDeatilEntity.Values> mCurrentValues;
    private OnSelectCallBack selectCallBack;
    private int recycleHeight;
    private int totalStock;
    private String hasOption = "0";//是否有参数
    private int currentCount = 1;
    private LinkedHashMap<String, GoodsDeatilEntity.Values> linkedHashMap;
    public boolean isSelectCount = true;//默认可以选择数量

    public ParamDialog(Context context, GoodsDeatilEntity goods) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        init();
        setParam(goods);
    }

    public ParamDialog(Context context, GoodsDeatilEntity.Goods goods) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        init();
        setParamGoods(goods);
    }

    public ParamDialog(Context context, ProductDetailEntity productDetailEntity) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        init();
        setParamGoods(productDetailEntity);
    }

    public ParamDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_goods_select,
                null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
        initListeners();
        setCanceledOnTouchOutside(false);

        recycleHeight = getDeviceHeight(mContext) * 1 / 3;
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, recycleHeight);
        recycler_param.setLayoutParams(params);
    }

    public void setParam(GoodsDeatilEntity goods) {
        this.goodsDeatilEntity = goods;
        this.specs = goods.specs;
        this.mSku = goods.sku;
        this.hasOption = goods.has_option;
        mCurrentValues = new ArrayList<>();
        initMap();
        initViews();
    }

    public void setParamGoods(GoodsDeatilEntity.Goods goods) {
        this.mGoods = goods;
        this.specs = goods.goods_info.specs;
        this.mSku = goods.goods_info.sku;
        this.hasOption = goods.goods_info.has_option;
        mCurrentValues = new ArrayList<>();
        initMap();
        initViews();
    }

    public void setParamGoods(ProductDetailEntity entity) {
        this.productDetailEntity = entity;
        this.specs = productDetailEntity.specs;
        this.mSku = productDetailEntity.sku;
        this.hasOption = productDetailEntity.has_option;
        mCurrentValues = new ArrayList<>();
        initMap();
        initViews();
    }

    public void initViews() {
        if (goodsDeatilEntity != null) {
            GoodsDeatilEntity.SpecailAct common_activity = goodsDeatilEntity.common_activity;
            GoodsDeatilEntity.TTAct tt_act = goodsDeatilEntity.tt_act;
            if (common_activity != null && "2".equals(common_activity.activity_status)) {
                dia_tv_price.setText("¥" + common_activity.actprice);
            } else if (tt_act != null && "1".equals(tt_act.sale)) {
                dia_tv_price.setText("¥" + tt_act.act_price);
            } else {
                dia_tv_price.setText("¥" + goodsDeatilEntity.price);
            }
            totalStock = Integer.valueOf(goodsDeatilEntity.stock);
            tv_count.setText(String.format(mContext.getResources().getString(R.string.goods_stock), totalStock));
            GlideUtils.getInstance().loadImage(mContext, iv_dialogPhoto, goodsDeatilEntity.thumb);
        }
        if (mGoods != null) {
            dia_tv_price.setText("¥" + mGoods.price);
            totalStock = Integer.valueOf(mGoods.stock);
            tv_count.setText(String.format(mContext.getResources().getString(R.string.goods_stock), totalStock));
            GlideUtils.getInstance().loadImage(mContext, iv_dialogPhoto, mGoods.thumb);
        }

        if (productDetailEntity != null) {
            dia_tv_price.setText("¥" + productDetailEntity.price);
            totalStock = Integer.valueOf(productDetailEntity.stock);
            tv_count.setText(String.format(mContext.getResources().getString(R.string.goods_stock), totalStock));
            GlideUtils.getInstance().loadImage(mContext, iv_dialogPhoto, productDetailEntity.thumb);
        }

        if (specs == null || specs.size() == 0) {
            recycler_param.setVisibility(View.INVISIBLE);
            tv_param.setVisibility(View.INVISIBLE);
        } else {
            paramItemAdapter = new ParamItemAdapter(specs);
            linearLayoutManager = new LinearLayoutManager(mContext);
            recycler_param.setLayoutManager(linearLayoutManager);
            recycler_param.setAdapter(paramItemAdapter);
            tv_param.setVisibility(View.VISIBLE);
        }
    }


    public void initListeners() {
        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
    }

    public void initMap() {
        tv_param.setText(mContext.getResources().getString(R.string.goods_pleaseselectparam));
        if (specs != null && specs.size() != 0) {
            linkedHashMap = new LinkedHashMap<>();
            for (int i = 0; i < specs.size(); i++) {
                linkedHashMap.put(specs.get(i).name, null);
                List<GoodsDeatilEntity.Values> values = specs.get(i).values;
                if (values != null && values.size() > 0) {
                    for (GoodsDeatilEntity.Values value : values) {
                        value.isSelect = false;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                currentCount++;
                if (currentCount > totalStock) {
                    currentCount = totalStock;
                    return;
                }
                tv_number.setText(String.valueOf(currentCount));
                break;
            case R.id.btn_minus:
                currentCount--;
                if (currentCount <= 0) {
                    currentCount = 0;
                    return;
                }
                tv_number.setText(String.valueOf(currentCount));
                break;
            case R.id.btn_complete:
                if (currentCount > totalStock) {
                    Common.staticToast("库存不足");
                    return;
                }

                if ("1".equals(hasOption)) {
                    GoodsDeatilEntity.Sku s = checkLinkmap(true);
                    if (selectCallBack != null && s != null) {
                        selectCallBack.onSelectComplete(s, currentCount);
                        dismiss();
                    }
                } else {
                    if (selectCallBack != null)
                        selectCallBack.onSelectComplete(null, currentCount);
                    dismiss();
                }
                break;
            case R.id.iv_cancel:
                dismiss();
                break;
        }
    }

    public class ParamItemAdapter extends RecyclerView.Adapter<ParamItemAdapter.ViewHolder> {
        List<GoodsDeatilEntity.Specs> spes;

        public ParamItemAdapter(List<GoodsDeatilEntity.Specs> s) {
            this.spes = s;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_param_select, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final GoodsDeatilEntity.Specs mSpecs = specs.get(position);
            final List<GoodsDeatilEntity.Values> mValues = mSpecs.values;

            holder.tv_param_type.setText(specs.get(position).name);
            holder.flowLayout_parm.setAdapter(new TagAdapter(mValues) {
                @Override
                public View getView(FlowLayout parent, int position, Object o) {
                    GoodsDeatilEntity.Values values = mValues.get(position);
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tag,
                            parent, false);
                    TextView tv = (TextView) view.findViewById(R.id.tv_tag);
                    tv.setText(values.name);

                    if (values.isSelect()) {
                        tv.setBackgroundResource(R.drawable.shape_tag_selected);
                        tv.setTextColor(mContext.getResources().getColor(R.color.white));
                    } else {
                        tv.setBackgroundResource(R.drawable.shape_tag_nomal);
                        tv.setTextColor(mContext.getResources().getColor(R.color.new_text));
                    }

                    view.setOnClickListener(v -> {
                        GoodsDeatilEntity.Values valuesItem = mValues.get(position);
                        if (!valuesItem.isSelect()) {
                            for (GoodsDeatilEntity.Values mValues : mValues) {
                                mValues.isSelect = false;
                            }
                            valuesItem.isSelect = true;
                            notifyDataChanged();
                            if (linkedHashMap != null) {
                                linkedHashMap.put(mSpecs.name, values);
                            }
                            if (checkLinkmap(false) != null) {
                                GoodsDeatilEntity.Sku s = checkLinkmap(false);

                                GlideUtils.getInstance().loadImage(mContext, iv_dialogPhoto, s.thumb);
                                dia_tv_price.setText("¥" + s.price);
                                totalStock = Integer.valueOf(s.stock);
                                tv_count.setText(String.format(mContext.getResources()
                                        .getString(R.string.goods_stock), s.stock));
                                tv_param.setText(s.name);
                            }
                        }
                    });
                    return view;
                }
            });
        }


        @Override
        public int getItemCount() {
            return specs.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_param_type)
            TextView tv_param_type;

            @BindView(R.id.flowLayout_parm)
            TagFlowLayout flowLayout_parm;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


    /**
     * 检查是否点击所有的item
     */
    public GoodsDeatilEntity.Sku checkLinkmap(boolean isComplete) {
        if (linkedHashMap == null) {
            return null;
        }
        mCurrentValues.clear();
        //检查某项是否点击了
        Iterator it = linkedHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getValue() == null) {
                if (isComplete) {
                    Common.staticToast("请选择" + e.getKey());
                }
                return null;
            }
            mCurrentValues.add((GoodsDeatilEntity.Values) e.getValue());
        }
        // 按升序排序
        Collections.sort(mCurrentValues, (values, t1) ->
                Integer.valueOf(values.id).compareTo(Integer.valueOf(t1.id)));

        //拼接id字符 按下划线拼接
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < mCurrentValues.size(); i++) {
            result.append(mCurrentValues.get(i).id);
            if (i != mCurrentValues.size() - 1) {
                result.append("_");
            }
        }
        //从sku中遍历获取sku
        if (mSku == null || mSku.size() == 0) {
            return null;
        }
        for (int i = 0; i < mSku.size(); i++) {
            if (result.toString().equals(mSku.get(i).specs)) {
                return mSku.get(i);
            }
        }
        return null;
    }

    public void setOnSelectCallBack(OnSelectCallBack callBack) {
        this.selectCallBack = callBack;
        if (!isSelectCount) {
            rLayout_count.setVisibility(View.GONE);
        } else {
            rLayout_count.setVisibility(View.VISIBLE);
        }
    }


    public interface OnSelectCallBack {
        void onSelectComplete(GoodsDeatilEntity.Sku sku, int count);
    }
}
