package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.ChangePreferDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.ParamDialog;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/20.
 */

public class EnableGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {
    private List<GoodsDeatilEntity.Goods> mGoods;
    private ShoppingCarEntity.Enabled.Promotion mPromotion;
    private String promotionTitle;
    private Context mContext;
    private boolean isEdit;
    private boolean isEditAll;
    private ChangePreferDialog preferDialog;
    private ParamDialog paramDialog;
    private OnGoodsChangeListener onGoodsChangeListener;

    public EnableGoodsAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists, ShoppingCarEntity.Enabled.Promotion promotion) {
        super(context, isShowFooter, lists);
        this.mGoods = lists;
        this.mPromotion = promotion;
        this.mContext = context;
        this.promotionTitle = mPromotion.prom_title;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        EnableViewHolder enableViewHolder = new EnableViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shoppingcar_goods, parent, false));
        return enableViewHolder;
    }

    public void setEdit(boolean edit) {
        this.isEdit = edit;
        notifyDataSetChanged();
    }

    public void setEditAll(boolean editAll) {
        this.isEditAll = editAll;
        notifyDataSetChanged();
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        final int stock;//库存量
        final EnableViewHolder enableViewHolder = (EnableViewHolder) holder;
        final GoodsDeatilEntity.Goods goods = mGoods.get(position);

        GlideUtils.getInstance().loadImage(mContext, enableViewHolder.miv_goods, goods.thumb);
        enableViewHolder.tv_goods_title.setText(goods.title);

        if (TextUtils.isEmpty(goods.left) || "null".equals(goods.left)) {
            enableViewHolder.tv_goods_notice.setVisibility(View.GONE);
        } else {
            enableViewHolder.tv_goods_notice.setText(String.format(mContext.getResources().getString(R.string.count_notice), goods.left));
            enableViewHolder.tv_goods_notice.setVisibility(View.VISIBLE);
        }
        stock = Integer.valueOf(goods.stock);
        enableViewHolder.tv_goods_param.setText(goods.sku);
        enableViewHolder.tv_edit_param.setText(goods.sku);
        enableViewHolder.tv_discount.setText(promotionTitle);
        enableViewHolder.tv_goods_attribute.setText(goods.sku);
        enableViewHolder.tv_goods_num.setText("x" + goods.qty);
        enableViewHolder.tv_goods_count.setText(goods.qty);
        enableViewHolder.tv_goods_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(goods.qty) + 1;
                if (count > stock) {
                    return;
                }
                enableViewHolder.tv_goods_count.setText(String.valueOf(count));
                onGoodsChangeListener.OnChangeCount(goods.cart_id, count);
            }
        });

        enableViewHolder.tv_goods_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(goods.qty) - 1;
                if (count <= 0) {
                    return;
                }
                enableViewHolder.tv_goods_count.setText(String.valueOf(count));
                if (onGoodsChangeListener != null) {
                    onGoodsChangeListener.OnChangeCount(goods.cart_id, count);
                }
            }
        });

        enableViewHolder.miv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(goods.is_check)) {
                    goods.is_check = "0";
                } else {
                    goods.is_check = "1";
                }
                if (onGoodsChangeListener != null) {
                    onGoodsChangeListener.OnChangeCheck(goods.cart_id, goods.is_check);
                }
            }
        });

        if ("1".equals(goods.is_check)) {
            enableViewHolder.miv_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
        } else {
            enableViewHolder.miv_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
        }

        if (!isEdit) { //编辑状态
            enableViewHolder.rl_goods_param.setVisibility(View.VISIBLE);
            enableViewHolder.tv_goods_num.setVisibility(View.VISIBLE);
            enableViewHolder.ll_goods_edit.setVisibility(View.GONE);
            enableViewHolder.tv_edit_del.setVisibility(View.GONE);
        } else {
            enableViewHolder.tv_goods_num.setVisibility(View.GONE);
            enableViewHolder.ll_goods_edit.setVisibility(View.VISIBLE);
            enableViewHolder.tv_edit_param.setVisibility(View.GONE);
            enableViewHolder.tv_edit_del.setVisibility(View.VISIBLE);
            if (!isEditAll) {  //编辑所有商品
                enableViewHolder.tv_edit_param.setVisibility(View.GONE);
                enableViewHolder.rl_goods_parm.setVisibility(View.VISIBLE);
            } else {
                enableViewHolder.tv_edit_param.setVisibility(View.VISIBLE);
                enableViewHolder.rl_goods_parm.setVisibility(View.GONE);
            }
        }

        enableViewHolder.rl_goods_parm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paramDialog == null) {
                    paramDialog = new ParamDialog(mContext, goods);
                }
                paramDialog.setOnSelectCallBack(new ParamDialog.OnSelectCallBack() {
                    @Override
                    public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
                        if (onGoodsChangeListener != null) {
                            onGoodsChangeListener.OnChangeSku(goods.cart_id, sku.id);
                        }
                    }
                });
                paramDialog.show();
            }
        });

        if (goods.all_prom == null || goods.all_prom.size() == 0) {
            enableViewHolder.rl_prefer.setEnabled(false);
            enableViewHolder.tv_edit_promo.setVisibility(View.INVISIBLE);
        } else {
            enableViewHolder.rl_prefer.setEnabled(true);
            enableViewHolder.tv_edit_promo.setVisibility(View.VISIBLE);
        }

        enableViewHolder.rl_prefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferDialog == null) {
                    preferDialog = new ChangePreferDialog(mContext, goods.all_prom);
                }
                preferDialog.setOnPreferSelectCallBack(new ChangePreferDialog.OnPreferSelectCallBack() {
                    @Override
                    public void onSelect(GoodsDeatilEntity.AllProm allProm) {
                        if (onGoodsChangeListener != null) {
                            onGoodsChangeListener.OnChangePromotion(goods.cart_id, allProm.prom_id);
                        }
                    }
                });
                preferDialog.show();
            }
        });
    }

    public class EnableViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.miv_goods)
        MyImageView miv_goods;

        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;

        @BindView(R.id.tv_goods_param)
        TextView tv_goods_param;

        @BindView(R.id.tv_goods_notice)
        TextView tv_goods_notice;

        @BindView(R.id.tv_goods_price)
        TextView tv_goods_price;

        @BindView(R.id.tv_goods_num)
        TextView tv_goods_num;

        @BindView(R.id.tv_goods_count)
        TextView tv_goods_count;

        @BindView(R.id.rl_goods_param)
        RelativeLayout rl_goods_param;

        @BindView(R.id.ll_goods_edit)
        LinearLayout ll_goods_edit;

        @BindView(R.id.tv_edit_del)
        TextView tv_edit_del;

        @BindView(R.id.tv_goods_attribute)
        TextView tv_goods_attribute;

        @BindView(R.id.rl_goods_parm)
        RelativeLayout rl_goods_parm;

        @BindView(R.id.tv_edit_param)
        TextView tv_edit_param;

        @BindView(R.id.tv_goods_add)
        TextView tv_goods_add;

        @BindView(R.id.tv_goods_min)
        TextView tv_goods_min;

        @BindView(R.id.rl_prefer)
        RelativeLayout rl_prefer;

        @BindView(R.id.tv_prefer)
        TextView tv_prefer;

        @BindView(R.id.tv_discount)
        TextView tv_discount;

        @BindView(R.id.tv_edit_promo)
        TextView tv_edit_promo;

        public EnableViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnGoodsChangeListener(OnGoodsChangeListener listener) {
        this.onGoodsChangeListener = listener;
    }

    public interface OnGoodsChangeListener {
        void OnChangeCount(String goodsId, int count);

        void OnChangeSku(String goodsId, String skuId);

        void OnChangeCheck(String goodsId, String isCheck);

        void OnChangePromotion(String goods, String promoId);
    }
}
