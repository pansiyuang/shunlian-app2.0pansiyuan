package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.ui.category.CategoryLetterAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class PingpaiAdapter extends BaseRecyclerAdapter<GetListFilterEntity.Recommend> {
    private List<GetListFilterEntity.Brand> brands;
    private ArrayList<String> letters;
    public boolean isAll;
    private List<GetListFilterEntity.Recommend> lists;

    public PingpaiAdapter(Context context, boolean isShowFooter, List<GetListFilterEntity.Recommend> lists, List<GetListFilterEntity.Brand> brands, ArrayList<String> letters, boolean isAll) {
        super(context, isShowFooter, lists);
        this.brands = brands;
        this.letters = letters;
        this.lists=lists;
        this.isAll=isAll;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pingpai, parent, false));
    }

    @Override
    public int getItemCount() {
        if (!isAll&&lists.size()>5){
            return 6;
        }
        return super.getItemCount();
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final GetListFilterEntity.Recommend recommend = lists.get(position);
        if (position == lists.size() - 1 && position > 10) {
            viewHolder.mtv_name.setText(R.string.category_quanbu);
            viewHolder.mtv_name.setTextColor(getColor(R.color.pink_color));
            viewHolder.mtv_name.setBackgroundColor(getColor(R.color.value_f5));
        } else {
            viewHolder.mtv_name.setText(recommend.brand_name);
            viewHolder.mtv_name.setTextColor(getColor(R.color.new_text));
            viewHolder.mtv_name.setBackgroundColor(getColor(R.color.value_f5));
        }
        if (Constant.BRAND_IDS.size() > 0) {
            for (int i = 0; i < Constant.BRAND_IDS.size(); i++) {
                if (recommend.id.equals(Constant.BRAND_IDS.get(i))&&!viewHolder.mtv_name.getText().toString().equals(getString(R.string.category_quanbu))) {
                    viewHolder.mtv_name.setBackgroundResource(R.mipmap.img_dcha);
                    break;
                } else if (i >= Constant.BRAND_IDS.size() - 1) {
                    viewHolder.mtv_name.setBackgroundColor(getColor(R.color.value_f5));
                    break;
                }
            }
        } else {
            viewHolder.mtv_name.setBackgroundColor(getColor(R.color.value_f5));
        }
        viewHolder.mtv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyOnClickListener.isFastClick()) {
                    return;
                }
                if (viewHolder.mtv_name.getText().toString().equals(getString(R.string.category_quanbu)) && letters != null && brands != null && letters.size() > 0) {
                    if (Constant.BRAND_IDSBEFORE==null){
                        Constant.BRAND_IDSBEFORE=new ArrayList<>();
                    }else {
                        Constant.BRAND_IDSBEFORE.clear();
                    }
                    Constant.BRAND_IDSBEFORE.addAll(Constant.BRAND_IDS);
                    CategoryLetterAct.startAct(context, brands, letters);
                } else if (Constant.BRAND_IDS.size() > 0) {
                    for (int i = 0; i < Constant.BRAND_IDS.size(); i++) {
                        if (recommend.id.equals(Constant.BRAND_IDS.get(i))) {
                            viewHolder.mtv_name.setBackgroundColor(getColor(R.color.value_f5));
                            Constant.BRAND_IDS.remove(i);
                            break;
                        } else if (i >= Constant.BRAND_IDS.size() - 1) {
                            if (Constant.BRAND_IDS.size() < 8) {
                                viewHolder.mtv_name.setBackgroundResource(R.mipmap.img_dcha);
                                Constant.BRAND_IDS.add(recommend.id);
                            } else {
                                Common.staticToast("已达上限8个");
                            }
                            break;
                        }
                    }
                } else {
                    viewHolder.mtv_name.setBackgroundResource(R.mipmap.img_dcha);
                    Constant.BRAND_IDS.add(recommend.id);
                }

            }
        });
    }


    public class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
