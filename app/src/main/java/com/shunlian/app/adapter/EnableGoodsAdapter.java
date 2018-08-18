package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.ui.fragment.ShoppingCarFrag;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.IconTextSpan;
import com.shunlian.app.widget.ChangePreferDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.ParamDialog;

import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.MyOnClickListener.isFastClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class EnableGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {
    private List<GoodsDeatilEntity.Goods> mGoods;
    private ShoppingCarEntity.Enabled.Promotion mPromotion;
    private Context mContext;
    private boolean isEdit;
    private boolean isEditAll;
    private ChangePreferDialog preferDialog;
    private ParamDialog paramDialog;
    private OnGoodsChangeListener onGoodsChangeListener;
    private ShoppingCarFrag mFrag;
    private StringBuffer stringBuffer;

    public EnableGoodsAdapter(Context context, ShoppingCarFrag frag, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists, ShoppingCarEntity.Enabled.Promotion promotion) {
        super(context, isShowFooter, lists);
        this.mGoods = lists;
        this.mFrag = frag;
        this.mPromotion = promotion;
        this.mContext = context;
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
        final GoodsDeatilEntity.EveryDay everyDay = goods.every_day;

        GlideUtils.getInstance().loadImage(mContext, enableViewHolder.miv_goods, goods.thumb);
        setLabelTitle(enableViewHolder.tv_goods_title, goods.big_label, goods.title);

        if (isEmpty(goods.left) || "null".equals(goods.left)) {
            enableViewHolder.tv_goods_notice.setVisibility(View.GONE);
        } else {
            enableViewHolder.tv_goods_notice.setText(goods.left);
            enableViewHolder.tv_goods_notice.setVisibility(View.VISIBLE);
        }

        if (isEmpty(goods.reduced) || "null".equals(goods.reduced)) {
            enableViewHolder.tv_markdown.setVisibility(View.GONE);
        } else {
            enableViewHolder.tv_markdown.setVisibility(View.VISIBLE);
            enableViewHolder.tv_markdown.setText(goods.reduced);
        }
        stock = Integer.valueOf(goods.stock);
        enableViewHolder.tv_goods_param.setText(goods.sku);
        enableViewHolder.tv_edit_param.setText(goods.sku);
        enableViewHolder.tv_goods_attribute.setText(goods.sku);
        enableViewHolder.tv_goods_num.setText("x" + goods.qty);
        enableViewHolder.edt_goods_count.setText(goods.qty);
        enableViewHolder.tv_goods_price.setText("¥" + goods.price);
        enableViewHolder.tv_edit_price.setText("¥" + goods.price);

        enableViewHolder.tv_prefer.setText(mPromotion.title_label);
        enableViewHolder.tv_discount.setText(mPromotion.prom_title);

        if (everyDay != null) {
            enableViewHolder.tv_active.setText(everyDay.remind + " " + everyDay.left_time);
            enableViewHolder.tv_active.setVisibility(View.VISIBLE);
            enableViewHolder.rl_prefer.setVisibility(View.VISIBLE);
            enableViewHolder.rl_prefer.setEnabled(false);
        } else {
            enableViewHolder.tv_active.setVisibility(View.GONE);
            if ("0".equals(goods.prom_id)) {
                enableViewHolder.rl_prefer.setVisibility(View.GONE);
                enableViewHolder.rl_prefer.setEnabled(false);
            } else {
                enableViewHolder.rl_prefer.setVisibility(View.VISIBLE);
                enableViewHolder.rl_prefer.setEnabled(true);
            }
        }

        if (isEmpty(mPromotion.title_label) && isEmpty(mPromotion.prom_title)) {
            enableViewHolder.rl_prefer.setVisibility(View.GONE);
        } else {
            enableViewHolder.rl_prefer.setVisibility(View.VISIBLE);
        }

        if (isEmpty(goods.all_prom)) {
            enableViewHolder.tv_edit_promo.setVisibility(View.GONE);
        } else {
            enableViewHolder.tv_edit_promo.setVisibility(View.VISIBLE);
        }

        enableViewHolder.tv_goods_add.setOnClickListener(v -> {
            if (isFastClick()) {
                return;
            }
            int count = Integer.valueOf(goods.qty) + 1;
            if (count > stock) {
                Common.staticToast("不能超出库存数量");
                return;
            }
            enableViewHolder.edt_goods_count.setText(String.valueOf(count));
            onGoodsChangeListener.OnChangeCount(goods.cart_id, count);
        });

        enableViewHolder.tv_goods_min.setOnClickListener(v -> {
            if (isFastClick()) {
                return;
            }
            int count = Integer.valueOf(goods.qty) - 1;
            if (count <= 0) {
                return;
            }
            if (onGoodsChangeListener != null) {
                onGoodsChangeListener.OnChangeCount(goods.cart_id, count);
            }
        });

        enableViewHolder.edt_goods_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isEmpty(s.toString())) {
                    int goodsCount = Integer.valueOf(s.toString());
                    if (goodsCount > stock) {
                        Common.staticToast("最多只能添加" + stock + "件商品哦");
                        enableViewHolder.edt_goods_count.setText(stock + "");
                        enableViewHolder.edt_goods_count.setSelection(String.valueOf(stock).length());
                    }
                }
            }
        });

        enableViewHolder.edt_goods_count.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (onGoodsChangeListener != null) {
                    String s = enableViewHolder.edt_goods_count.getText().toString();
                    if (isEmpty(s)) {
                        s = goods.qty;
                    }
                    onGoodsChangeListener.OnChangeCount(goods.cart_id, Integer.valueOf(s));
                }
                return true;
            }
            return false;
        });

        enableViewHolder.miv_select.setOnClickListener(v -> {
            if ("1".equals(goods.is_check)) {
                goods.is_check = "0";
            } else {
                goods.is_check = "1";
            }
            if (onGoodsChangeListener != null) {
                onGoodsChangeListener.OnChangeCheck(goods.cart_id, goods.is_check);
            }
        });

        if ("1".equals(goods.is_check)) {
            enableViewHolder.miv_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
        } else {
            enableViewHolder.miv_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
        }

        if (!isEdit) { //编辑状态
            enableViewHolder.rl_goods_param.setVisibility(View.VISIBLE);
            enableViewHolder.rl_goods_edit.setVisibility(View.GONE);
        } else {
            enableViewHolder.rl_goods_edit.setVisibility(View.VISIBLE);
            enableViewHolder.rl_goods_param.setVisibility(View.GONE);
            if (!isEditAll) {  //编辑所有商品
                enableViewHolder.tv_edit_param.setVisibility(View.GONE);
                if (!isEmpty(goods.sku)) {
                    enableViewHolder.rl_goods_attribute.setVisibility(View.VISIBLE);
                } else {
                    enableViewHolder.rl_goods_attribute.setVisibility(View.GONE);
                }
            } else {
                enableViewHolder.tv_edit_param.setVisibility(View.VISIBLE);
                enableViewHolder.rl_goods_attribute.setVisibility(View.GONE);
            }
        }

        enableViewHolder.rl_goods_attribute.setOnClickListener(view -> {
            if (paramDialog == null) {
                paramDialog = new ParamDialog(mContext, goods);
                paramDialog.isSelectCount = false;
            }
            paramDialog.setOnSelectCallBack((sku, count) -> {
                if (onGoodsChangeListener != null) {
                    onGoodsChangeListener.OnChangeSku(goods.cart_id, sku.id);
                }
            });
            paramDialog.show();
        });

        enableViewHolder.rl_prefer.setOnClickListener(v -> {
            if (isEmpty(goods.all_prom)) {
                return;
            }
            if (preferDialog == null) {
                preferDialog = new ChangePreferDialog(mContext, goods.all_prom);
            }
            preferDialog.selectItem(goods.prom_id);
            preferDialog.setOnPreferSelectCallBack(allProm -> {
                if (onGoodsChangeListener != null) {
                    onGoodsChangeListener.OnChangePromotion(goods.cart_id, allProm.prom_id);
                }
            });
            preferDialog.show();
        });
        enableViewHolder.tv_edit_del.setOnClickListener(v -> {
            if (onGoodsChangeListener != null) {
                onGoodsChangeListener.OnGoodsDel(goods.cart_id);
            }
        });
    }

    public class EnableViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
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

        @BindView(R.id.edt_goods_count)
        EditText edt_goods_count;

        @BindView(R.id.rl_goods_attribute)
        RelativeLayout rl_goods_attribute;

        @BindView(R.id.rl_goods_param)
        RelativeLayout rl_goods_param;

        @BindView(R.id.rl_goods_edit)
        RelativeLayout rl_goods_edit;

        @BindView(R.id.tv_edit_del)
        TextView tv_edit_del;

        @BindView(R.id.tv_goods_attribute)
        TextView tv_goods_attribute;

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

        @BindView(R.id.tv_edit_price)
        TextView tv_edit_price;

        @BindView(R.id.tv_active)
        TextView tv_active;

        @BindView(R.id.tv_markdown)
        TextView tv_markdown;


        public EnableViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            GoodsDetailAct.startAct(context, lists.get(getAdapterPosition()).goods_id);
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

        void OnGoodsDel(String goodsId);
    }

    public void setLabelTitle(TextView textView, String label, String title) {
        if (!isEmpty(label)) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            } else {
                stringBuffer.setLength(0);
            }
            IconTextSpan iconTextSpan = new IconTextSpan(context, R.color.pink_color, label);
            stringBuffer.append("");
            stringBuffer.append(title);
            SpannableString spannableString = new SpannableString(stringBuffer.toString());
            spannableString.setSpan(iconTextSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        } else {
            textView.setText(title);
        }
    }
}
