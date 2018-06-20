package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoveryCommentListEntity;
import com.shunlian.app.presenter.PADiscoverSucaiku;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IADiscoverSucaiku;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/21.
 */

public class TieziCommentAdapter extends BaseRecyclerAdapter<DiscoveryCommentListEntity.Mdata.Commentlist> implements IADiscoverSucaiku{
    private PADiscoverSucaiku paDiscoverSucaiku;
    private String circle_id,inv_id;

    public TieziCommentAdapter(Context context,String circle_id,String inv_id, boolean isShowFooter, List<DiscoveryCommentListEntity.Mdata.Commentlist> list) {
        super(context, isShowFooter, list);
        this.circle_id=circle_id;
        this.inv_id=inv_id;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new CommentHolder(LayoutInflater.from(context).inflate(R.layout.use_comment_item, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        final CommentHolder viewHolder = (CommentHolder) holder;
        final DiscoveryCommentListEntity.Mdata.Commentlist content = lists.get(position);
        viewHolder.zan=Integer.parseInt(content.likes);
        if ("1".equals(content.is_likes)){
            viewHolder.isZan=true;
            viewHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_h);
            viewHolder.mtv_zan_count.setTextColor(getColor(R.color.pink_color));
        }else {
            viewHolder.isZan=false;
            viewHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_n);
            viewHolder.mtv_zan_count.setTextColor(getColor(R.color.share_text));
        }
        viewHolder.miv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paDiscoverSucaiku==null)
                    paDiscoverSucaiku = new PADiscoverSucaiku(context, TieziCommentAdapter.this);
                if (viewHolder.isZan) {
                    paDiscoverSucaiku.dianZanss(circle_id,content.id,inv_id, "2", viewHolder);
                } else {
                    paDiscoverSucaiku.dianZanss(circle_id,content.id,inv_id, "1", viewHolder);
                }
            }
        });
        Bitmap bitmap = TransformUtil.convertVIP(context, content.level);
        viewHolder.miv_vip.setImageBitmap(bitmap);
        viewHolder.mtv_name.setText(content.nickname);
        viewHolder.mtv_zan_count.setText(content.likes);
        viewHolder.mtv_time.setText(content.add_time);
        viewHolder.mtv_content.setText(content.content);
        GlideUtils.getInstance().loadCircleHeadImage(context,viewHolder.civ_head,content.avatar);
    }

    @Override
    public void dianZan(DiscoverSucaikuAdapter.SucaikuHolder holder) {

    }

    @Override
    public void dianZans(DiscoverNewAdapter.NewHolder holder) {

    }

    @Override
    public void dianZanss(CommentHolder viewHolder) {
        if (viewHolder.isZan){
            viewHolder.isZan=false;
            viewHolder.zan= viewHolder.zan-1;
            viewHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_n);
            viewHolder.mtv_zan_count.setTextColor(getColor(R.color.share_text));
        } else {
            viewHolder.isZan=true;
            viewHolder.zan= viewHolder.zan+1;
            viewHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_h);
            viewHolder.mtv_zan_count.setTextColor(getColor(R.color.pink_color));
        }
        viewHolder.mtv_zan_count.setText(String.valueOf(viewHolder.zan));
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public class CommentHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.mtv_zan_count)
        MyTextView mtv_zan_count;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.civ_head)
        MyImageView civ_head;

        @BindView(R.id.miv_vip)
        MyImageView miv_vip;

        @BindView(R.id.miv_zan)
        MyImageView miv_zan;

        private boolean isZan=false;
        private int zan=0;

        public CommentHolder(View itemView) {
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
