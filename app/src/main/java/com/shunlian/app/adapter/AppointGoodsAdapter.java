package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.eventbus_bean.ModifyNumEvent;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.ShapeUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AppointGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {


    private String mFrom;

    public AppointGoodsAdapter(Context context, boolean isShowFooter,
                               List<GoodsDeatilEntity.Goods> lists, String from) {
        super(context, isShowFooter, lists);
        mFrom = from;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View item_appoint_goods = LayoutInflater.from(context)
                .inflate(R.layout.item_appoint_goods, parent, false);
        return new AppointGoodsHolder(item_appoint_goods);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        AppointGoodsHolder mHolder = (AppointGoodsHolder) holder;
        GoodsDeatilEntity.Goods goods = lists.get(position);
        GlideUtils.getInstance().loadImage(context,mHolder.miv_goods,goods.thumb);
        mHolder.mtv_count.setText("x"+goods.qty);
        if (ConfirmOrderAct.TYPE_PLUSFREE.equals(mFrom)){
            mHolder.mtv_price.setText(goods.price+"元购");
            mHolder.mtv_price.setTextColor(getColor(R.color.pink_color));
        }else {
            mHolder.mtv_price.setText(Common.dotAfterSmall(getString(R.string.rmb) + goods.price, 11));
            mHolder.mtv_price.setTextColor(getColor(R.color.new_text));
        }
        mHolder.mtv_attribute.setText(goods.sku);

        String label = goods.big_label;
        if (!isEmpty(label)){
            gone(mHolder.mtv_label);
            //mHolder.mtv_label.setText(label);
        }else {
            gone(mHolder.mtv_label);
        }
        mHolder.mtv_title.setText(Common.getPlaceholder(context,label,goods.title));

        if (ConfirmOrderAct.TYPE_GOODS_DETAIL.equals(mFrom)){
            gone(mHolder.mtv_count);
            visible(mHolder.mllayout_count);
            mHolder.mllayout_count.setBackgroundDrawable(ShapeUtils.commonShape(context,
                    Color.WHITE,2,1,getColor(R.color.e4_color)));
            mHolder.mtv_edit_count.setText(goods.qty);
        }else {
            gone(mHolder.mllayout_count);
            visible(mHolder.mtv_count);
        }

        mHolder.setModifyNum(position);
    }

    public class AppointGoodsHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.miv_goods)
        MyImageView miv_goods;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_attribute)
        MyTextView mtv_attribute;

        @BindView(R.id.mtv_count)
        MyTextView mtv_count;

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        @BindView(R.id.mllayout_count)
        MyLinearLayout mllayout_count;

        @BindView(R.id.mtv_edit_count)
        EditText mtv_edit_count;

        @BindView(R.id.mtv_count_reduce)
        MyImageView mtv_count_reduce;

        @BindView(R.id.mtv_count_add)
        MyImageView mtv_count_add;

        int min_count = 0;//最少购买数量
        int max_count = 0;//最大购买数量

        public AppointGoodsHolder(View itemView) {
            super(itemView);
            int i = TransformUtil.dip2px(context, 10);
            TransformUtil.expandViewTouchDelegate(mtv_count_add,i,i,0,i);
            TransformUtil.expandViewTouchDelegate(mtv_count_reduce,i,i,i,0);
        }

        public void setModifyNum(int postion){
            if (ConfirmOrderAct.TYPE_GOODS_DETAIL.equals(mFrom)){
                try{
                    GoodsDeatilEntity.Goods goods = lists.get(postion);
                    String min_buy_limit = goods.min_buy_limit;
                    String stock = goods.stock;

                    if (!isEmpty(min_buy_limit) && !isEmpty(stock)){
                        min_count = Integer.parseInt(min_buy_limit);
                        max_count = Integer.parseInt(stock);
                    }
                }catch (Exception e){}

                mtv_count_reduce.setOnClickListener(v -> {
                     try {
                        String content = mtv_edit_count.getText().toString();
                        int i = Integer.parseInt(content);
                        i -= 1;
                        if (i < min_count){
                            i = min_count;
                        }
                        mtv_edit_count.setText(String.valueOf(i));
                        ModifyNumEvent event = new ModifyNumEvent(String.valueOf(i));
                        EventBus.getDefault().post(event);
                     }catch (Exception e){}
                });

                mtv_count_add.setOnClickListener(v -> {
                     try {
                        String content = mtv_edit_count.getText().toString();
                        int i = Integer.parseInt(content);
                        i += 1;
                        if (i > max_count){
                            i = max_count;
                        }

                        mtv_edit_count.setText(String.valueOf(i));
                        ModifyNumEvent event = new ModifyNumEvent(String.valueOf(i));
                        EventBus.getDefault().post(event);
                     }catch (Exception e){}
                });

                mtv_edit_count.setOnEditorActionListener((v, actionId, event) -> {
                    try {
                        if (actionId == EditorInfo.IME_ACTION_DONE){
                            String s = mtv_edit_count.getText().toString();
                            int i = Integer.parseInt(s);
                            if (i < min_count){
                                i = min_count;
                            }

                            mtv_edit_count.setText(String.valueOf(i));
                            ModifyNumEvent event1 = new ModifyNumEvent(String.valueOf(i));
                            EventBus.getDefault().post(event1);
                        }
                     }catch (Exception e){}
                    return false;
                });

            }
        }
    }
}
