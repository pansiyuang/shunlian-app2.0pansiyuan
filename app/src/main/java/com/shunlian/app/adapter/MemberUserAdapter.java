package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.ui.new_user.NewUserPageActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22.
 */

public class MemberUserAdapter extends BaseRecyclerAdapter<NewUserGoodsEntity.Goods> {

    public MemberUserAdapter(Context context, List<NewUserGoodsEntity.Goods> lists) {
        super(context, true, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member_user,
                parent, false);
        return new MemberHolder(view);
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.i_have_a_bottom_line));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    /**
     * 处理列表
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MemberHolder) {
            MemberHolder mHolder = (MemberHolder) holder;
            GlideUtils.getInstance().loadCircleAvar(context,mHolder.miv_member_pic,
                    "https://static.veer.com/veer/static/resources/FourPack/2018-12-03/d9738f6321324d51a78e567fdfeabc63.jpg");
        }
    }


    public class MemberHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_member_pic)
        MyImageView miv_member_pic;

        @BindView(R.id.tv_member_name)
        TextView tv_member_name;
        @BindView(R.id.image_level)
        ImageView image_level;
        @BindView(R.id.image_shop)
        ImageView image_shop;

        @BindView(R.id.tv_member_number)
        TextView tv_member_number;

        @BindView(R.id.tv_member_time_num)
        TextView tv_member_time_num;

        @BindView(R.id.tv_member_profit)
        MyTextView tv_member_profit;

        public MemberHolder(View itemView) {
            super(itemView);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
            }
        }
    }

}
