package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    @BindView(R.id.btn_complete)
    Button btn_complete;

    @BindView(R.id.iv_cancel)
    MyImageView iv_cancel;

    private List<GoodsDeatilEntity.Specs> specs;
    private List<GoodsDeatilEntity.Sku> mSku;
    private GoodsDeatilEntity goodsDeatilEntity;
    private Context mContext;
    private GoodsDeatilEntity.Goods mGoods;
    private LinearLayoutManager linearLayoutManager;
    private ParamItemAdapter paramItemAdapter;
    private List<GoodsDeatilEntity.Values> mCurrentValues;
    private OnSelectCallBack selectCallBack;
    private int recycleHeight;
    private int totalStock;
    private int currentCount = 1;
    private LinkedHashMap<String, GoodsDeatilEntity.Values> linkedHashMap;

    public ParamDialog(Context context, GoodsDeatilEntity goods) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        this.goodsDeatilEntity = goods;
        this.specs = goods.specs;
        this.mSku = goods.sku;
        mCurrentValues = new ArrayList<>();
        initMap();
    }

    public ParamDialog(Context context, GoodsDeatilEntity.Goods goods) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        this.mGoods = goods;
        this.specs = goods.goods_info.specs;
        this.mSku = goods.goods_info.sku;
        mCurrentValues = new ArrayList<>();
        initMap();
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
        if (goodsDeatilEntity != null) {
            dia_tv_price.setText("¥" + goodsDeatilEntity.price);
            totalStock = Integer.valueOf(goodsDeatilEntity.stock);
            tv_count.setText(String.format(mContext.getResources().getString(R.string.goods_stock), goodsDeatilEntity.stock));
            GlideUtils.getInstance().loadImage(mContext, iv_dialogPhoto, goodsDeatilEntity.thumb);
        }
        if (mGoods != null) {
            dia_tv_price.setText("¥" + mGoods.price);
            totalStock = Integer.valueOf(mGoods.stock);
            tv_count.setText(String.format(mContext.getResources().getString(R.string.goods_stock), mGoods.stock));
            GlideUtils.getInstance().loadImage(mContext, iv_dialogPhoto, mGoods.thumb);
        }
    }


    public void initListeners() {
        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
    }

    public void initMap() {
        if (specs != null && specs.size() != 0) {
            linkedHashMap = new LinkedHashMap<>();
            for (int i = 0; i < specs.size(); i++) {
                linkedHashMap.put(specs.get(i).name, null);
            }
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
                GoodsDeatilEntity.Sku s = checkLinkmap(true);

                if (s != null && selectCallBack != null) {
                    selectCallBack.onSelectComplete(s, currentCount);
                    dismiss();
                }
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


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_param_select, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final GoodsDeatilEntity.Specs mSpecs = specs.get(position);
            final List<GoodsDeatilEntity.Values> mValues = mSpecs.values;

            holder.tv_param_type.setText(specs.get(position).name);
            gridLayoutManager = new GridLayoutManager(mContext, 4);
            holder.recycler_param.setLayoutManager(gridLayoutManager);
            final ParamTagAdapter paramTagAdapter = new ParamTagAdapter(mContext, false, mValues);
            holder.recycler_param.setAdapter(paramTagAdapter);
            holder.recycler_param.addItemDecoration(new GridSpacingItemDecoration(4, 26, false));
            paramTagAdapter.setOnItemTagClickListener(new OnItemTagClickListener() {
                @Override
                public void onItemTagClick(View view, int position) {
                    GoodsDeatilEntity.Values values = mValues.get(position);
                    if (!values.isSelect()) {
                        paramTagAdapter.itemSelected(position, true);
                        if (linkedHashMap != null) {
                            linkedHashMap.put(mSpecs.name, values);
                        }
                        if (checkLinkmap(false) != null) {
                            GoodsDeatilEntity.Sku s = checkLinkmap(false);

                            GlideUtils.getInstance().loadImage(mContext, iv_dialogPhoto, s.thumb);
                            dia_tv_price.setText("¥" + s.price);
                            totalStock = Integer.valueOf(s.stock);
                            tv_count.setText(String.format(mContext.getResources().getString(R.string.goods_stock), s.stock));
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

    public class ParamTagAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Values> implements View.OnClickListener {

        private List<GoodsDeatilEntity.Values> valuesList;
        private String showType;
        private OnItemTagClickListener mOnItemClickListener = null;

        public ParamTagAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Values> lists) {
            super(context, isShowFooter, lists);
            this.valuesList = lists;
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
        protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag, parent, false);
            ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(this);
            return vh;
        }

        @Override
        public void handleList(RecyclerView.ViewHolder viewHolder, int position) {
            ViewHolder holder = (ViewHolder) viewHolder;
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

        public void itemSelected(int position, boolean isSelect) {
            for (int i = 0; i < valuesList.size(); i++) {
                valuesList.get(i).setSelect(false);
            }

            GoodsDeatilEntity.Values values = valuesList.get(position);
            values.setSelect(isSelect);
            notifyDataSetChanged();
        }

        public void setOnItemTagClickListener(OnItemTagClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemTagClick(view, (int) view.getTag());
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
        Collections.sort(mCurrentValues, new Comparator<GoodsDeatilEntity.Values>() {
            @Override
            public int compare(GoodsDeatilEntity.Values values, GoodsDeatilEntity.Values t1) {
                return Integer.valueOf(values.id).compareTo(Integer.valueOf(t1.id));
            }
        });

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
    }

    public interface OnItemTagClickListener {
        void onItemTagClick(View view, int position);
    }

    public interface OnSelectCallBack {
        void onSelectComplete(GoodsDeatilEntity.Sku sku, int count);
    }
}
