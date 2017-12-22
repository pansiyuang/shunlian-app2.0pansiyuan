package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ComboDetailEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/24.
 */

public class ComboDetailAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    public static final int ITEM_PIC = 2;
    public static final int ITEM_GIFT_PRICE = 3;
    public static final int ITEM_OTHER = 4;
    public static final int ITEM_OTHER_COMBO = 5;
    private final LayoutInflater mInflater;
    private ComboDetailEntity mEntity;

    public ComboDetailAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists, ComboDetailEntity entity) {
        super(context, isShowFooter, lists);
        mInflater = LayoutInflater.from(context);
        mEntity = entity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_PIC:
                View picView = mInflater.inflate(R.layout.item_detail, parent, false);
                return new PicHolder(picView);
            case ITEM_GIFT_PRICE:
                View giftPriceView = mInflater.inflate(R.layout.item_gift_price, parent, false);
                return new GiftPriceHolder(giftPriceView);
            case ITEM_OTHER:
                View otherView = mInflater.inflate(R.layout.item_gift_price, parent, false);
                return new OtherHolder(otherView);
            case ITEM_OTHER_COMBO:
                View comboView = mInflater.inflate(R.layout.item_dialog_combo, parent, false);
                return new OtherComboHolder(comboView);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public int getItemCount() {
        List<ComboDetailEntity.OthersCombo> others_combo = mEntity.others_combo;
        if (isEmpty(others_combo)){
            return super.getItemCount() + 2;
        }else {
            return super.getItemCount() + 3 + others_combo.size();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case ITEM_PIC:
                holderPic(holder,position);
                break;
            case ITEM_GIFT_PRICE:
                holderGift(holder,position);
                break;
            case ITEM_OTHER:
                holderOther(holder,position);
                break;
            case ITEM_OTHER_COMBO:
                holderOtherCombo(holder,position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    private void holderOtherCombo(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OtherComboHolder){
            OtherComboHolder mHolder = (OtherComboHolder) holder;

        }
    }

    private void holderOther(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OtherHolder){
            OtherHolder mHolder = (OtherHolder) holder;
            mHolder.mllayout_price_detail.setVisibility(View.GONE);
            mHolder.mtv_combo_title.setText("其他套餐");
        }
    }

    private void holderGift(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GiftPriceHolder){
            GiftPriceHolder mHolder = (GiftPriceHolder) holder;
            mHolder.mtv_combo_title.setText(mEntity.combo_title);
            SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.rmb)
                    .concat(mEntity.combo_price), getString(R.string.rmb), 9);
            mHolder.tv_combo_price.setText(spannableStringBuilder);
            mHolder.tv_market_price.setText("套餐原价".concat(getString(R.string.rmb)).concat(mEntity.old_combo_price));
        }
    }

    private void holderPic(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PicHolder){
            PicHolder mHolder = (PicHolder) holder;
            GlideUtils.getInstance().loadImage(context,(ImageView) mHolder.itemView,mEntity.combo_thumb);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return ITEM_PIC;
        }else if (position == 1){
            return ITEM_GIFT_PRICE;
        }else if (position < lists.size() + 2){
            return super.getItemViewType(position);
        }else if (position == lists.size() + 2){
            return ITEM_OTHER;
        }else {
            return ITEM_OTHER_COMBO;
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = mInflater.inflate(R.layout.item_combo, parent, false);
        return new ComboHolder(inflate);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ComboHolder){
            ComboHolder mHolder = (ComboHolder) holder;
            GoodsDeatilEntity.Goods goods = lists.get(position - 2);
            GlideUtils.getInstance().loadImage(context,mHolder.miv_goods_pic,goods.thumb);
            mHolder.mtv_goods_info.setText(goods.title);
            if (position == lists.size() + 1){
                mHolder.view_line.setVisibility(View.INVISIBLE);
            }else {
                mHolder.view_line.setVisibility(View.VISIBLE);
            }
        }
    }

    public class PicHolder extends BaseRecyclerViewHolder{


        public PicHolder(View itemView) {
            super(itemView);
            MyImageView miv_pic = (MyImageView) itemView;
            miv_pic.setWHProportion(720,386);
        }
    }

    public class GiftPriceHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.tv_combo_price)
        TextView tv_combo_price;

        @BindView(R.id.mtv_combo_title)
        TextView mtv_combo_title;

        @BindView(R.id.tv_market_price)
        TextView tv_market_price;

        public GiftPriceHolder(View itemView) {
            super(itemView);
        }
    }

    public class OtherHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mllayout_price_detail)
        MyLinearLayout mllayout_price_detail;

        @BindView(R.id.mtv_combo_title)
        MyTextView mtv_combo_title;

        @BindView(R.id.view_line)
        View view_line;
        public OtherHolder(View itemView) {
            super(itemView);
            view_line.setVisibility(View.VISIBLE);
        }
    }

    public class OtherComboHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.tv_combo_price)
        TextView tv_combo_price;

        @BindView(R.id.tv_market_price)
        TextView tv_market_price;

        @BindView(R.id.recycler_combo)
        RecyclerView recycler_combo;

        @BindView(R.id.view_line)
        View view_line;
        public OtherComboHolder(View itemView) {
            super(itemView);
        }
    }

    public class ComboHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mtv_goods_info)
        MyTextView mtv_goods_info;

        @BindView(R.id.mtv_select)
        MyTextView mtv_select;

        @BindView(R.id.view_line)
        View view_line;
        public ComboHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.mtv_select)
        public void tvSelect(){
            GoodsDeatilEntity.Goods goods = lists.get(getAdapterPosition() - 2);
            ParamDialog paramDialog = new ParamDialog(context,goods);
            paramDialog.show();
            paramDialog.setOnSelectCallBack(new ParamDialog.OnSelectCallBack() {
                @Override
                public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
                    String name = "";
                    if (sku != null) {
                        name = sku.name;
                    }
                    mtv_select.setText("已选择:".concat(name).concat(count+"件"));
                }
            });
        }
    }
}
