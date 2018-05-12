package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
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
import com.shunlian.app.ui.goods_detail.ComboDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private ISelectParamsListener mParamsListener;
    private Map<String,String> priceMap = new HashMap<>();//价格
    private Map<String,String> marketPriceMap = new HashMap<>();//原价

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
        List<GoodsDeatilEntity.Combo> others_combo = mEntity.others_combo;
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
            final GoodsDeatilEntity.Combo othersCombo = mEntity.others_combo.get(position - lists.size() - 3);
            SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.rmb)
                    .concat(othersCombo.combo_price).concat("-").concat(othersCombo.max_combo_price), getString(R.string.rmb), 9);
            mHolder.tv_combo_price.setText(spannableStringBuilder);
            mHolder.tv_combo_price.setTextSize(19);
            mHolder.tv_market_price.setText("套餐原价".concat(getString(R.string.rmb))
                    .concat(othersCombo.old_combo_price).concat("-")
                    .concat(othersCombo.max_old_combo_price));

            ComboAdapter.ComboPicAdapter adapter = new ComboAdapter.
                    ComboPicAdapter(context,false,othersCombo.goods);
            mHolder.recycler_combo.setAdapter(adapter);
            if (position + 1 == getItemCount()){
                mHolder.view_line.setVisibility(View.INVISIBLE);
            }else {
                mHolder.view_line.setVisibility(View.VISIBLE);
            }

            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GoodsDeatilEntity.Goods goods = othersCombo.goods.get(position);
                    ComboDetailAct.startAct(context,othersCombo.combo_id,goods.goods_id);
                }
            });
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
            GoodsDeatilEntity.Combo current_combo = mEntity.current_combo;
            mHolder.mtv_combo_title.setText(current_combo.combo_title);
            if (priceMap != null && priceMap.size() > 0){
                String mapValue = getMapValue(priceMap);
                SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.rmb)
                        .concat(mapValue), getString(R.string.rmb), 9);
                mHolder.tv_combo_price.setText(spannableStringBuilder);
            }else {
                SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.rmb)
                        .concat(current_combo.combo_price).concat("-")
                        .concat(current_combo.max_combo_price), getString(R.string.rmb), 9);
                mHolder.tv_combo_price.setText(spannableStringBuilder);
            }

            if (marketPriceMap != null && marketPriceMap.size() > 0){
                String mapValue = getMapValue(marketPriceMap);
                mHolder.tv_market_price.setText("套餐原价".concat(getString(R.string.rmb))
                        .concat(mapValue));
            }else {
                mHolder.tv_market_price.setText("套餐原价".concat(getString(R.string.rmb))
                        .concat(current_combo.old_combo_price).concat("-")
                        .concat(current_combo.max_old_combo_price));
            }
        }
    }

    /**
     * 获取map中的值，相加得到返回
     * @param priceMap
     */
    private String getMapValue(Map<String, String> priceMap) {
        float totalPrice = 0;
        Set<String> keySet = priceMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            String value = priceMap.get(next);
            totalPrice += Float.parseFloat(value);
        }
        return Common.formatFloat(totalPrice);
    }

    private void holderPic(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PicHolder){
            PicHolder mHolder = (PicHolder) holder;
            GoodsDeatilEntity.Combo current_combo = mEntity.current_combo;
            GlideUtils.getInstance().loadImage(context,(ImageView) mHolder.itemView,current_combo.combo_thumb);
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
            LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            recycler_combo.setLayoutManager(manager);

            ViewGroup.LayoutParams layoutParams = view_line.getLayoutParams();
            layoutParams.height = TransformUtil.dip2px(context,0.5f);
            view_line.setLayoutParams(layoutParams);
            view_line.setBackgroundColor(getColor(R.color.light_gray_three));
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
            miv_goods_pic.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        @OnClick(R.id.mtv_select)
        public void tvSelect(){
            final GoodsDeatilEntity.Goods goods = lists.get(getAdapterPosition() - 2);
            ParamDialog paramDialog = new ParamDialog(context,goods);
            paramDialog.show();
            paramDialog.setOnSelectCallBack(new ParamDialog.OnSelectCallBack() {
                @Override
                public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
                    String name = "";
                    if (sku != null && sku.name != null) {
                        name = sku.name ;
                        priceMap.put(String.valueOf(getAdapterPosition() -2),sku.price);
                        marketPriceMap.put(String.valueOf(getAdapterPosition() -2),sku.market_price);
                        notifyItemChanged(1);
                    }
                    mtv_select.setText("已选择:".concat(name).concat("  ")
                            .concat(String.valueOf(count)).concat("件"));
                    if (mParamsListener != null){
                        mParamsListener.selectParam(goods.goods_id,sku==null?"":sku.id);
                    }
                }
            });
        }
    }

    /**
     * 选择商品属性
     * @param paramsListener
     */
    public void setSelectParamsListener(ISelectParamsListener paramsListener){

        mParamsListener = paramsListener;
    }

    public interface ISelectParamsListener{
        void selectParam(String goods_id,String sku);
    }
}
