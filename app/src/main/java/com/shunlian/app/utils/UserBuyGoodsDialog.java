package com.shunlian.app.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.widget.CBProgressBar;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.circle.DrawHookView;
import com.shunlian.app.widget.dialog.CommonDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class UserBuyGoodsDialog {
    private  CommonDialog nomalBuildl;
    private Context context;
    private SimpleRecyclerAdapter adapter;
    private List<NewUserGoodsEntity.Goods> goodsList;
    public UserBuyGoodsDialog(Context context){
        this.context = context;
        goodsList = new ArrayList<>();
    }


    public void updateItemData(List<NewUserGoodsEntity.Goods> goodsList){
        this.goodsList.clear();
        this.goodsList.addAll(goodsList);
        if(adapter!=null){
            adapter.notifyDataSetChanged();
            if(goodsList.size()==0&&nomalBuildl!=null){
                nomalBuildl.dismiss();
            }
        }
    }
    public void showGoodsInfo(List<NewUserGoodsEntity.Goods> goodsList){
        this.goodsList.clear();
        this.goodsList.addAll(goodsList);
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context).fullWidth().fromBottom()
                .setView(R.layout.dialog_user_goods);
        nomalBuildl = nomalBuild.create();
        nomalBuildl.setCancelable(false);
        nomalBuildl.show();
        MyImageView miv_close = nomalBuildl.findViewById(R.id.miv_close);
        RecyclerView rv_goods = nomalBuildl.findViewById(R.id.rv_goods);
        TextView tv_go_buy= nomalBuildl.findViewById(R.id.tv_go_buy);
        miv_close.setOnClickListener(view -> nomalBuildl.dismiss());
        rv_goods.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SimpleRecyclerAdapter<NewUserGoodsEntity.Goods>(context, R.layout.item_goods_new_user, this.goodsList) {
            @Override
            public void convert(SimpleViewHolder holder, NewUserGoodsEntity.Goods  goods, int position) {
                MyImageView miv_goods_pic = holder.getView(R.id.miv_goods_pic);
                MyTextView mtv_title = holder.getView(R.id.mtv_title);
                MyTextView mtv_price = holder.getView(R.id.mtv_price);
                MyTextView mtv_discount_price = holder.getView(R.id.mtv_discount_price);
                MyTextView tv_shopping_car = holder.getView(R.id.tv_shopping_car);
                holder.getView(R.id.tv_usew_desc).setVisibility(View.GONE);

                GlideUtils.getInstance().loadOverrideImage(context,
                        miv_goods_pic, goods.thumb,220,220);
                mtv_title.setText(goods.title);
                String source = context.getString(R.string.rmb).concat(goods.price);
                mtv_price.setText(Common.changeTextSize(source, context.getString(R.string.rmb), 12));
                tv_shopping_car.setText("删除");
                mtv_discount_price.setText(goods.marker_price);
                tv_shopping_car.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cartDelGoodListenl!=null){
                            cartDelGoodListenl.delGood(goods);
                        }
                    }
                });
            }
        };
        rv_goods.setAdapter(adapter);
        tv_go_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private CartDelGoodListen cartDelGoodListenl;

    public  void setCartDelGoodListen(CartDelGoodListen cartDelGoodListen){
        this.cartDelGoodListenl = cartDelGoodListen;
    }
    public interface CartDelGoodListen{
        void delGood(NewUserGoodsEntity.Goods goods);
    }

}
