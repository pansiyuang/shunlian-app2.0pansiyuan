package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.ui.new_user.NewUserPageActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22.
 */

public class MemberUserAdapter extends BaseRecyclerAdapter<MemberInfoEntity.MemberList> {

    public MemberUserAdapter(Context context, List<MemberInfoEntity.MemberList> lists) {
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
            mHolder.tv_member_name.setText(lists.get(position).nickname);
            mHolder.tv_member_time_num.setText(lists.get(position).reg_time+"     "+"已购买 "+lists.get(position).order_count+" 单");
            mHolder.tv_member_number.setText("邀请码："+lists.get(position).code);
            mHolder.tv_member_profit.setText(getString(R.string.common_yuan)+lists.get(position).income);
            GlideUtils.getInstance().loadCircleAvar(context,mHolder.miv_member_pic,
                    lists.get(position).avatar);
            if(!TextUtils.isEmpty(lists.get(position).role)) {
                int plus_role_code = Integer.parseInt(lists.get(position).role);
                if (plus_role_code == 1) {//店主 1=plus店主，2=销售主管，>=3 销售经理
                    mHolder.image_level.setVisibility(View.VISIBLE);
                    mHolder.image_level.setImageResource(R.mipmap.img_plus_phb_dianzhu);
                } else if (plus_role_code >= 3) {//经理
                    mHolder.image_level.setVisibility(View.VISIBLE);
                    mHolder.image_level.setImageResource(R.mipmap.img_plus_phb_jingli);
                } else if (plus_role_code == 2) {//主管
                    mHolder.image_level.setVisibility(View.VISIBLE);
                    mHolder.image_level.setImageResource(R.mipmap.img_plus_phb_zhuguan);
                } else {//没有级别
                    mHolder.image_level.setVisibility(View.GONE);
                }
            }
            if(!TextUtils.isEmpty(lists.get(position).level)) {
                Bitmap levelBitmap = TransformUtil.convertNewVIP(context, lists.get(position).level);
                if (levelBitmap != null) {
                    mHolder.image_shop.setVisibility(View.VISIBLE);
                    mHolder.image_shop.setImageBitmap(levelBitmap);
                } else {
                    mHolder.image_shop.setVisibility(View.GONE);
                }
            }
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
