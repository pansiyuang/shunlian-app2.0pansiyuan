package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.ShapeUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22.
 */

public class MemberUserAdapter extends BaseRecyclerAdapter<MemberInfoEntity.MemberList> {

    private boolean isSettingMobile;

    public MemberUserAdapter(Context context, List<MemberInfoEntity.MemberList> lists,
                             boolean isSettingMobile) {
        super(context, true, lists);
        this.isSettingMobile = isSettingMobile;
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
            mHolder.tv_member_profit.setText(getString(R.string.common_yuan)+" "+lists.get(position).income);
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

            String mobile = lists.get(position).mobile;
            if (!isEmpty(mobile)){
                mHolder.mtv_phonenum.setText("手机号："+mobile);
            }else {
                mHolder.mtv_phonenum.setText("");
            }

            if(!isEmpty(mobile)&&lists.get(position).isShow){
                mHolder.mtv_phonenum.setVisibility(View.VISIBLE);
                mHolder.tv_copy.setVisibility(View.VISIBLE);
            }else{
                mHolder.mtv_phonenum.setVisibility(View.INVISIBLE);
                mHolder.tv_copy.setVisibility(View.INVISIBLE);
            }
//            String member_manager = SharedPrefUtil.getSharedUserString("member_manager", "1");
//            if ("0".equals(member_manager) && isSettingMobile){//9437105286
//                visible(mHolder.tv_copy,mHolder.mtv_phonenum);
//            }else {
//                mHolder.tv_copy.setVisibility(View.INVISIBLE);
//                mHolder.mtv_phonenum.setVisibility(View.INVISIBLE);
//            }
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

        @BindView(R.id.mtv_phonenum)
        MyTextView mtv_phonenum;

        @BindView(R.id.tv_copy)
        MyTextView tv_copy;

        public MemberHolder(View itemView) {
            super(itemView);
            int i = TransformUtil.dip2px(context, 2);
            GradientDrawable gradientDrawable = ShapeUtils.commonShape(context,
                    getColor(R.color.white), i, i / 4,
                    Color.parseColor("#EAB044"));
            tv_copy.setBackgroundDrawable(gradientDrawable);

            tv_copy.setOnClickListener(v -> {
                if (getAdapterPosition()>=0){
                    MemberInfoEntity.MemberList memberList = lists.get(getAdapterPosition());
                    Common.copyText(context,memberList.mobile);
                }
            });
//            mtv_phonenum.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (getAdapterPosition()>=0) {
//                        MemberInfoEntity.MemberList memberList = lists.get(getAdapterPosition());
//                        Common.copyText(context, memberList.mobile);
//                    }
//                    return false;
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition()>=0) {
                        MemberInfoEntity.MemberList memberList = lists.get(getAdapterPosition());
                        if (mtv_phonenum.getVisibility() == View.VISIBLE) {
                            mtv_phonenum.setVisibility(View.INVISIBLE);
                            tv_copy.setVisibility(View.INVISIBLE);
                            memberList.isShow = false;
                            notifyDataSetChanged();
                        } else {
                            mtv_phonenum.setVisibility(View.VISIBLE);
                            tv_copy.setVisibility(View.VISIBLE);
                            setAllUserMemberPhone();
                            memberList.isShow = true;
                            notifyDataSetChanged();
                        }
                    }
                }
            });

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


    /**
     * 设置所有的手机号隐藏状态
     */
    private void setAllUserMemberPhone(){
        if(lists!=null&&lists.size()>0){
            for (int i=0;i<lists.size();i++){
                lists.get(i).isShow = false;
            }
        }
    }
}
