package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ShaixuanAttrAdapter extends BaseRecyclerAdapter<GetListFilterEntity.Attr> {

    public ShaixuanAttrAdapter(Context context, boolean isShowFooter, List<GetListFilterEntity.Attr> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shaixuan_attr, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        GetListFilterEntity.Attr attr = lists.get(position);
        viewHolder.mtv_name.setText(attr.name);
        viewHolder.rv_attr.setLayoutManager(new GridLayoutManager(context, 3));
        viewHolder.strings=new ArrayList<>();
        Constant.BRAND_ATTRNAME.add(attr.name);
        Constant.BRAND_ATTRS.put(attr.name,viewHolder.strings);
        viewHolder.shaixuanAttrsAdapter=new ShaixuanAttrsAdapter(context, false, attr.val_list,attr.name);
        viewHolder.rv_attr.setAdapter(viewHolder.shaixuanAttrsAdapter);
        viewHolder.rv_attr.setNestedScrollingEnabled(false);
    }


    public class ViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.mtv_more)
        MyTextView mtv_more;

        @BindView(R.id.miv_arrow)
        MyImageView miv_arrow;

        @BindView(R.id.rv_attr)
        RecyclerView rv_attr;

        private ShaixuanAttrsAdapter shaixuanAttrsAdapter;
        private boolean isMore;
        private List<String> strings;

        public ViewHolder(View itemView) {
            super(itemView);
            mtv_more.setOnClickListener(this);
            miv_arrow.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mtv_more:
                case R.id.miv_arrow:
                    if (isMore) {
                        miv_arrow.setImageResource(R.mipmap.icon_saixuan_gd);
                        mtv_more.setText(R.string.category_gengduo);
                        shaixuanAttrsAdapter.isAll=false;
                        isMore=false;
                    } else {
                        miv_arrow.setImageResource(R.mipmap.icon_saixuan_sq);
                        mtv_more.setText(R.string.category_shouqi);
                        shaixuanAttrsAdapter.isAll=true;
                        isMore=true;
                    }
                    shaixuanAttrsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
