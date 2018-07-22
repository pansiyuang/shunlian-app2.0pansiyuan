package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoveryCircleEntity;
import com.shunlian.app.presenter.PADiscoverSucaiku;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IADiscoverSucaiku;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/21.
 */

public class DiscoverNewAdapter extends BaseRecyclerAdapter<DiscoveryCircleEntity.Mdata.Content> implements IADiscoverSucaiku{
    private PADiscoverSucaiku paDiscoverSucaiku;
    public DiscoverNewAdapter(Context context, boolean isShowFooter, List<DiscoveryCircleEntity.Mdata.Content> list) {
        super(context, isShowFooter, list);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new NewHolder(LayoutInflater.from(context).inflate(R.layout.item_discover_new, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        final NewHolder viewHolder = (NewHolder) holder;
        final DiscoveryCircleEntity.Mdata.Content content = lists.get(position);
        viewHolder.zan=Integer.parseInt(content.likes);
        if ("1".equals(content.is_likes)){
            viewHolder.isZan=true;
            viewHolder.miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_h);
        }else {
            viewHolder.isZan=false;
            viewHolder.miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_ns);
        }
        viewHolder.miv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paDiscoverSucaiku==null)
                    paDiscoverSucaiku = new PADiscoverSucaiku(context, DiscoverNewAdapter.this);
                if (viewHolder.isZan) {
                    paDiscoverSucaiku.dianZans(content.id, "2", viewHolder);
                } else {
                    paDiscoverSucaiku.dianZans(content.id, "1", viewHolder);
                }
            }
        });
        viewHolder.mtv_desc.setText(String.format(getString(R.string.discover_yiyourencanyu),content.comments));
        viewHolder.mtv_like.setText(String.format(getString(R.string.discover_renxihuan),content.likes));
        viewHolder.mtv_title.setText(content.title);
        GlideUtils.getInstance().communityTopPic(context,viewHolder.miv_photo,content.img,4,false);
    }

    @Override
    public void dianZan(DiscoverSucaikuAdapter.SucaikuHolder holder) {

    }

    @Override
    public void dianZans(NewHolder viewHolder) {
        if (viewHolder.isZan){
            viewHolder.isZan=false;
            viewHolder.zan= viewHolder.zan-1;
            viewHolder.miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_ns);
        } else {
            viewHolder.isZan=true;
            viewHolder.zan= viewHolder.zan+1;
            viewHolder.miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_h);
        }
        viewHolder.mtv_like.setText(String.format(getString(R.string.discover_renxihuan),String.valueOf(viewHolder.zan)));
    }

    @Override
    public void dianZanss(TieziCommentAdapter.CommentHolder holder) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public class NewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_like)
        MyTextView mtv_like;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.miv_like)
        MyImageView miv_like;

        private boolean isZan=false;
        private int zan=0;
        public NewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.onItemClick(view,getAdapterPosition());
            }
        }
    }

}
