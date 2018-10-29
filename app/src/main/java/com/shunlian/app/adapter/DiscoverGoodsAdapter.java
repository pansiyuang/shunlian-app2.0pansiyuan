package com.shunlian.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SaveAlbumDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class DiscoverGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    private LayoutInflater mInflater;
    private boolean isCode;
    private QuickActions quickActions;
    private String from,froms;


    public DiscoverGoodsAdapter(Context context, List<GoodsDeatilEntity.Goods> lists,boolean isCode,QuickActions quick_actions,String from,String froms) {
        super(context, false, lists);
        mInflater = LayoutInflater.from(context);
        this.isCode=isCode;
        this.quickActions=quick_actions;
        this.from=from;
        this.froms=froms;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new SingleViewHolder(mInflater.inflate(R.layout.item_found_goods, parent, false));
    }


    void createCode(String shareLink,String title,String desc,String price,String goodsId,String thumb,
                    boolean isSuperiorProduct,String from,String froms){
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(context, "login");
        }else {
            Dialog dialog_new = new Dialog(context, R.style.popAd);
            dialog_new.setContentView(R.layout.share_goods_new);
            Window window = dialog_new.getWindow();
//        //设置边框距离
            window.getDecorView().setPadding(TransformUtil.dip2px(context,30),
                    100,
                    TransformUtil.dip2px(context,30),
                    0);
            //设置dialog位置
//            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            //设置宽高
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            MyImageView miv_close = dialog_new.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_wexin = dialog_new.findViewById(R.id.mllayout_wexin);
            MyLinearLayout mllayout_save = dialog_new.findViewById(R.id.mllayout_save);
//            CircleImageView miv_user_head = dialog_new.findViewById(R.id.miv_user_head);
//            MyTextView mtv_nickname = dialog_new.findViewById(R.id.mtv_nickname);
//            mtv_nickname.setText("来自" + mShareInfoParam.userName + "的分享");
            MyImageView miv_code = (MyImageView) dialog_new.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(context, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(shareLink, null, i);
            miv_code.setImageBitmap(qrImage);


            MyTextView mtv_title = (MyTextView) dialog_new.findViewById(R.id.mtv_title);
            mtv_title.setText(title);

            MyTextView mtv_desc = (MyTextView) dialog_new.findViewById(R.id.mtv_desc);
            if (!TextUtils.isEmpty(desc)) {
                mtv_desc.setVisibility(View.VISIBLE);
                mtv_desc.setText(desc);
            } else {
                mtv_desc.setVisibility(View.GONE);
            }

            MyTextView mtv_price = (MyTextView) dialog_new.findViewById(R.id.mtv_price);
            mtv_price.setText("￥" + price);

//            MyTextView mtv_time = (MyTextView) dialog_new.findViewById(R.id.mtv_time);
//            MyTextView mtv_act_label = (MyTextView) dialog_new.findViewById(R.id.mtv_act_label);

            MyTextView mtv_goodsID = (MyTextView) dialog_new.findViewById(R.id.mtv_goodsID);
            mtv_goodsID.setText("商品编号:" +goodsId + "(搜索可直达)");

//            LinearLayout llayout_day = (LinearLayout) dialog_new.findViewById(R.id.llayout_day);

//            if (TextUtils.isEmpty(mShareInfoParam.start_time)) {
//                llayout_day.setVisibility(View.GONE);
//            } else {
//                mtv_time.setText(mShareInfoParam.start_time);
//                mtv_act_label.setText(mShareInfoParam.act_label);
//            }

            //显示优品图标
            MyImageView miv_SuperiorProduct = (MyImageView) dialog_new.findViewById(R.id.miv_SuperiorProduct);
            if (isSuperiorProduct) {
                miv_SuperiorProduct.setVisibility(View.VISIBLE);
            } else {
                miv_SuperiorProduct.setVisibility(View.GONE);
            }

            MyImageView miv_goods_pic = (MyImageView) dialog_new.findViewById(R.id.miv_goods_pic);
            int width=Common.getScreenWidth((Activity) context)-TransformUtil.dip2px(context,120);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) miv_goods_pic.getLayoutParams();
            layoutParams.width=width;
            layoutParams.height=width;
            GlideUtils.getInstance().loadImageZheng(context,miv_goods_pic,thumb);

            miv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_new.dismiss();
                }
            });

            mllayout_wexin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quickActions.saveshareGoodsPic(shareLink,title,desc,price,goodsId,thumb,isSuperiorProduct,false,from,froms);
                }
            });
            mllayout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quickActions.saveshareGoodsPic(shareLink,title,desc,price,goodsId,thumb,isSuperiorProduct,true,from,froms);
                }
            });
            dialog_new.setCancelable(false);
            dialog_new.show();
        }
    }
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SingleViewHolder) {
                SingleViewHolder viewHolder = (SingleViewHolder) holder;
                GoodsDeatilEntity.Goods goods = lists.get(position);
                if (goods==null)
                    return;
                GlideUtils.getInstance().loadCornerImage(context, viewHolder.miv_photo, goods.thumb);
                viewHolder.ntv_title.setText(goods.title);
                viewHolder.ntv_price.setText(getString(R.string.common_yuan)+goods.price);
                viewHolder.ntv_priceM.setText(getString(R.string.common_yuan)+goods.market_price);
                viewHolder.ntv_priceM.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                if (isCode){
                    viewHolder.ntv_code.setVisibility(View.VISIBLE);
                    viewHolder.miv_share.setVisibility(View.GONE);
                    viewHolder.ntv_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean is=false;
//                            if (!isEmpty(goods.isSuperiorProduct)&&"1".equals(goods.isSuperiorProduct)){
//                                is=true;
//                            }else {
//                                is=false;
//                            }
                            createCode(goods.share_url,goods.title,goods.desc,goods.price,goods.goods_id,goods.thumb,
                                    1==goods.isSuperiorProduct,from,froms);
                        }
                    });
                }else {
                    viewHolder.miv_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.staticToast("分享...");
                        }
                    });
                }

            }
    }



    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.miv_share)
        MyImageView miv_share;

        @BindView(R.id.ntv_title)
        NewTextView ntv_title;

        @BindView(R.id.ntv_price)
        NewTextView ntv_price;

        @BindView(R.id.ntv_code)
        NewTextView ntv_code;

        @BindView(R.id.ntv_priceM)
        NewTextView ntv_priceM;

        public SingleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
