package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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

    @BindView(R.id.btn_complete)
    Button btn_complete;

    @BindView(R.id.iv_cancel)
    MyImageView iv_cancel;

    private List<GoodsDeatilEntity.Specs> specs;
    private List<GoodsDeatilEntity.Sku> mSku;
    private GoodsDeatilEntity goodsDeatilEntity;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private ParamItemAdapter paramItemAdapter;
    private OnSelectCallBack selectCallBack;
    private int recycleHeight;
    private int totalStock;
    private int currentCount = 1;
    private List<GoodsDeatilEntity.Specs.Values> currentValues;
    private Map<String, GoodsDeatilEntity.Specs.Values> valuesMap;

    public ParamDialog(Context context, GoodsDeatilEntity goods) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        this.goodsDeatilEntity = goods;
        this.specs = goods.specs;
        this.mSku = goods.sku;
        currentValues = new ArrayList<>();
        initMap(goods);
    }

    public ParamDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_goods_select, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
        initViews();
        initListeners();
        setCanceledOnTouchOutside(false);

        recycleHeight = getDeviceHeight(mContext) * 1 / 3;
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        paramItemAdapter = new ParamItemAdapter(specs);
        linearLayoutManager = new LinearLayoutManager(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recycleHeight);
        recycler_param.setLayoutManager(linearLayoutManager);
        recycler_param.setLayoutParams(params);
        recycler_param.setAdapter(paramItemAdapter);
    }

    public void initViews() {
        dia_tv_price.setText("¥" + goodsDeatilEntity.price);
        totalStock = Integer.valueOf(goodsDeatilEntity.stock);
        tv_count.setText(String.format(mContext.getResources().getString(R.string.goods_stock), goodsDeatilEntity.stock));
        GlideUtils.getInstance().loadImage(mContext, iv_dialogPhoto, goodsDeatilEntity.thumb);
    }


    public void initListeners() {
        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
    }

    public void initMap(GoodsDeatilEntity goods) {
        valuesMap = new HashMap<>();
        for (GoodsDeatilEntity.Specs specs : goods.specs) {
            valuesMap.put(specs.name, null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                currentCount++;
                if (currentCount >= totalStock) {
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
                sortValuesList(valuesMap, true);
                break;
            case R.id.iv_cancel:
                dismiss();
                break;
        }
    }

    public class ParamItemAdapter extends RecyclerView.Adapter<ParamItemAdapter.ViewHolder> {
        GridLayoutManager gridLayoutManager;
        List<GoodsDeatilEntity.Specs> spes;

        public ParamItemAdapter(List<GoodsDeatilEntity.Specs> s) {
            this.spes = s;
        }

        public void setSpecs(List<GoodsDeatilEntity.Specs> spesList) {
            spes = spesList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_param_select, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final List<GoodsDeatilEntity.Specs.Values> mValues = specs.get(position).values;

            holder.tv_param_type.setText(specs.get(position).name);
            gridLayoutManager = new GridLayoutManager(mContext, 4);
            holder.recycler_param.setLayoutManager(gridLayoutManager);
            final ParamTagAdapter paramTagAdapter = new ParamTagAdapter(mValues, specs.get(position).show_type);
            holder.recycler_param.setAdapter(paramTagAdapter);

            paramTagAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int p) {
                    String spesName = specs.get(position).name;
                    GoodsDeatilEntity.Specs.Values values = mValues.get(p);
                    if (!values.isSelect()) {
                        paramTagAdapter.itemSelected(p);
                        if (!TextUtils.isEmpty(spesName)) {
                            valuesMap.put(spesName, values);
                            sortValuesList(valuesMap, false);
                        }
                    }
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

            @BindView(R.id.recycler_param)
            RecyclerView recycler_param;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    public class ParamTagAdapter extends RecyclerView.Adapter<ParamTagAdapter.ViewHolder> implements View.OnClickListener {

        private List<GoodsDeatilEntity.Specs.Values> valuesList;
        private String showType;
        private OnItemClickListener mOnItemClickListener = null;

        public ParamTagAdapter(List<GoodsDeatilEntity.Specs.Values> values, String type) {
            this.showType = type;
            this.valuesList = values;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag, parent, false);
            ViewHolder vh = new ViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv_tag.setText(valuesList.get(position).name);
            holder.itemView.setTag(position);
            if (valuesList.get(position).isSelect()) {
                holder.tv_tag.setBackgroundResource(R.drawable.shape_tag_selected);
                holder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                holder.tv_tag.setBackgroundResource(R.drawable.shape_tag_nomal);
                holder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.new_text));
            }
        }

        @Override
        public int getItemCount() {
            return valuesList.size();
        }

        public void itemSelected(int position) {
            for (int i = 0; i < valuesList.size(); i++) {
                valuesList.get(i).setSelect(false);
            }
            GoodsDeatilEntity.Specs.Values values = valuesList.get(position);
            values.setSelect(true);
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(view, (int) view.getTag());
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_tag)
            TextView tv_tag;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    /**
     * @param map        根据id大小排序
     * @param isComplete
     */

    public void sortValuesList(Map<String, GoodsDeatilEntity.Specs.Values> map, boolean isComplete) {
        StringBuffer ids = new StringBuffer();
        currentValues.clear();

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (entry.getValue() == null) {
                if (isComplete) {
                    Common.staticToast("请选择" + entry.getKey());
                }
            } else {
                currentValues.add((GoodsDeatilEntity.Specs.Values) entry.getValue());
            }
        }

        if (currentValues == null && currentValues.size() == 0) {
            return;
        }

        if (mSku == null && mSku.size() == 0) {
            return;
        }
        Collections.sort(currentValues, new Comparator<GoodsDeatilEntity.Specs.Values>() {
            @Override
            public int compare(GoodsDeatilEntity.Specs.Values values, GoodsDeatilEntity.Specs.Values t1) {
                return Integer.valueOf(values.id).compareTo(Integer.valueOf(t1.id));
            }
        });

        //拼接ids
        for (int i = 0; i < currentValues.size(); i++) {
            ids.append(currentValues.get(i).id);
            if (i != currentValues.size() - 1) {
                ids.append("_");
            }
        }

        for (GoodsDeatilEntity.Sku s : mSku) {
            if (ids.toString().equals(s.specs) && selectCallBack != null) {
                if (isComplete) {
                    selectCallBack.onSelectComplete(s, currentCount);
                } else {
                    dia_tv_price.setText(s.market_price);
                    tv_count.setText(String.format(mContext.getResources().getString(R.string.goods_stock), s.stock));
                }
                break;
            }
        }
    }

    public void setOnSelectCallBack(OnSelectCallBack callBack) {
        this.selectCallBack = callBack;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnSelectCallBack {
        void onSelectComplete(GoodsDeatilEntity.Sku sku, int count);
    }
}
