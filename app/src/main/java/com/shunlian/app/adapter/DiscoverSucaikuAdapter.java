package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DiscoveryMaterialEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.PADiscoverSucaiku;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IADiscoverSucaiku;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/8/21.
 */

public class DiscoverSucaikuAdapter extends BaseRecyclerAdapter<DiscoveryMaterialEntity.Content> implements IADiscoverSucaiku {

    public DiscoverSucaikuAdapter(Context context, boolean isShowFooter, List<DiscoveryMaterialEntity.Content> list) {
        super(context, isShowFooter, list);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new SucaikuHolder(LayoutInflater.from(context).inflate(R.layout.item_discover_sucaiku, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        SucaikuHolder viewHolder = (SucaikuHolder) holder;
        final DiscoveryMaterialEntity.Content content=lists.get(position);
        SpannableStringBuilder titleBuilder=Common.changeColor(content.add_time+content.content,content.add_time,getColor(R.color.value_299FFA));
        viewHolder.mtv_find_title.setText(titleBuilder);
        viewHolder.mtv_zan.setText(content.praise_num);
        if ("1".equals(content.praise)){
            viewHolder.miv_zan.setImageResource(R.mipmap.icon_found_zan_h);
            viewHolder.mtv_zan.setTextColor(getColor(R.color.pink_color));
        }else {
            viewHolder.miv_zan.setImageResource(R.mipmap.icon_found_zan_n);
            viewHolder.mtv_zan.setTextColor(getColor(R.color.value_BDBDBD));
        }
        viewHolder.miv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PADiscoverSucaiku paDiscoverSucaiku=new PADiscoverSucaiku(context,DiscoverSucaikuAdapter.this);
                if ("1".equals(content.praise)){
                    paDiscoverSucaiku.dianZan(content.id,"2",position);
                }else {
                    paDiscoverSucaiku.dianZan(content.id,"1",position);
                }
            }
        });


        if (content.image==null||content.image.size() == 0) {
            viewHolder.rv_pics.setVisibility(View.GONE);
        } else {
            GridLayoutManager flashManagers;
            if (content.image.size() == 1) {
                flashManagers = new GridLayoutManager(context, 1);
            } else {
                flashManagers = new GridLayoutManager(context, 3);
            }
            viewHolder.rv_pics.setVisibility(View.VISIBLE);
            SinglePicAdapter picAdapter = new SinglePicAdapter(context,false,content.image);
            viewHolder.rv_pics.setLayoutManager(flashManagers);
            viewHolder.rv_pics.addItemDecoration(new GrideItemDecoration(0, 0, TransformUtil.dip2px(context, 9), TransformUtil.dip2px(context, 9),false));
            viewHolder.rv_pics.setAdapter(picAdapter);
            picAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //点击查看大图
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList<String>) content.image;
                    bigImgEntity.index = position;
                    LookBigImgAct.startAct(context, bigImgEntity);
                }
            });
        }
    }

    @Override
    public void dianZan(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    public class SucaikuHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_find_title)
        MyTextView mtv_find_title;

        @BindView(R.id.mtv_zan)
        MyTextView mtv_zan;

        @BindView(R.id.miv_zan)
        MyImageView miv_zan;

        @BindView(R.id.rv_pics)
        RecyclerView rv_pics;

        public SucaikuHolder(View itemView) {
            super(itemView);
        }

    }

}
