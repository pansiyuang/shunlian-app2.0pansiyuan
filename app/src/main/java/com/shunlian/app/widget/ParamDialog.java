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
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private List<GoodsDeatilEntity.Specs> specs;
    private List<GoodsDeatilEntity.Sku> mSku;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private ParamItemAdapter paramItemAdapter;
    private List<GoodsDeatilEntity.Specs.Values> mCurrentValues;
    private OnSelectCallBack selectCallBack;
    private int currentCount = 1;

    public ParamDialog(Context context) {
        super(context);
    }

    public ParamDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        mCurrentValues = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_goods_select, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
        initListeners();
        setCanceledOnTouchOutside(false);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        paramItemAdapter = new ParamItemAdapter(specs);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recycler_param.setLayoutManager(linearLayoutManager);
        recycler_param.setAdapter(paramItemAdapter);
    }

    public void initData(List<GoodsDeatilEntity.Specs> sps, List<GoodsDeatilEntity.Sku> sku) {
        this.specs = sps;
        this.mSku = sku;
    }

    public void initListeners() {
        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                currentCount++;
                tv_number.setText(String.valueOf(currentCount));
                break;
            case R.id.btn_minus:
                currentCount--;
                if (currentCount <= 0) {
                    currentCount = 0;
                }
                tv_number.setText(String.valueOf(currentCount));
                break;
            case R.id.btn_complete:
                sortValues();
                StringBuffer ids = new StringBuffer();

                for (int i = 0; i < mCurrentValues.size(); i++) {
                    ids.append(mCurrentValues.get(i).id);
                    if (i != mCurrentValues.size() - 1) {
                        ids.append("_");
                    }
                }
                if (getSkuId(ids.toString()) != null && selectCallBack != null) {
                    selectCallBack.onSelectComplete(ids.toString(), currentCount);
                }
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
                    GoodsDeatilEntity.Specs.Values values = mValues.get(p);

                    if (values.isSelect()) {
                        paramTagAdapter.itemSelected(p, false);
                    } else {
                        paramTagAdapter.itemSelected(p, true);
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

        public void itemSelected(int position, boolean isSelect) {
            for (int i = 0; i < valuesList.size(); i++) {
                valuesList.get(i).setSelect(false);
                if (isIncludeValue(valuesList.get(i))) {
                    mCurrentValues.remove(valuesList.get(i));
                }
            }

            GoodsDeatilEntity.Specs.Values values = valuesList.get(position);
            values.setSelect(isSelect);
            notifyDataSetChanged();

            if (isSelect && !isIncludeValue(values)) {
                //选中且当前item不存在
                mCurrentValues.add(values);
            }

            if (!isSelect && isIncludeValue(values)) {
                //反选且当前item存在
                mCurrentValues.remove(values);
            }
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


    private boolean isIncludeValue(GoodsDeatilEntity.Specs.Values values) {
        boolean isInclude = false;
        if (mCurrentValues == null || mCurrentValues.size() == 0) {
            isInclude = false;
        }
        for (GoodsDeatilEntity.Specs.Values v : mCurrentValues) {
            if (values.id.equals(v.id)) {
                isInclude = true;
            }
        }
        return isInclude;
    }


    private List<GoodsDeatilEntity.Specs.Values> sortValues() {
        if (mCurrentValues == null || mCurrentValues.size() <= 1) {
            return null;
        }
        Collections.sort(mCurrentValues, new Comparator<GoodsDeatilEntity.Specs.Values>() {
            public int compare(GoodsDeatilEntity.Specs.Values arg0, GoodsDeatilEntity.Specs.Values arg1) {
                return Integer.valueOf(arg0.id).compareTo(Integer.valueOf(arg1.id));
            }
        });
        return mCurrentValues;
    }

    private GoodsDeatilEntity.Sku getSkuId(String ids) {
        GoodsDeatilEntity.Sku s = null;
        if (mSku == null || mSku.size() == 0) {
            return null;
        }
        for (int i = 0; i < mSku.size(); i++) {
            if (ids.equals(mSku.get(i).specs)) {
                s = mSku.get(i);
            }
        }
        return s;
    }


    public void setOnSelectCallBack(OnSelectCallBack callBack) {
        this.selectCallBack = callBack;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnSelectCallBack {
        void onSelectComplete(String skuId, int count);
    }
}
