package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreFirstAdapter extends BaseRecyclerAdapter<StoreIndexEntity.Body> {
    private static final int TYPE1 = 1;//商品组件双列
    private static final int TYPE2 = 2;//宝组单列
    private static final int TYPE3 = 3;//标题就传
    private static final int TYPE4 = 4;//图组文本就传
    private static final int TYPE5 = 5;//图组单列
    private static final int TYPE6 = 6;//图组双列
    private static final int TYPE7 = 7;//图组轮播
    private static final int TYPE8 = 8;//图组三列
    private Context context;
    private List<StoreIndexEntity.Body> datas;

    public StoreFirstAdapter(Context context, boolean isShowFooter,List<StoreIndexEntity.Body> datas) {
        super(context, isShowFooter, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE1:
//                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_one,parent,false);
//                View itemView = View.inflate(parent.getContext(), R.layout.block_store_first_one, null);
                return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_one, parent, false));
            case TYPE2:
                return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_two, parent, false));
            case TYPE3:
                return new ThreeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_three, parent, false));
            case TYPE4:
                return new FourHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_four, parent, false));
            case TYPE5:
                return new FiveHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_five, parent, false));
            case TYPE6:
                return new SixHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_six, parent, false));
            case TYPE7:
                return new SevenHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_seven, parent, false));
            case TYPE8:
                return new EightHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_eight, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (datas.get(position).block_module_type) {
            case "1":
                return TYPE1;
            case "2":
                return TYPE2;
            case "3":
                return TYPE3;
            case "4":
                return TYPE4;
            case "5":
                return TYPE5;
            case "6":
                return TYPE6;
            case "7":
                return TYPE7;
            case "8":
                return TYPE8;
            default:
                return super.getItemViewType(position);
        }
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new DefaultHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_default, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE1:
                if (holder instanceof OneHolder) {
                    OneHolder oneHolder = (OneHolder) holder;
                    StoreIndexEntity.Body data=datas.get(position);
                    if (TextUtils.isEmpty(data.title)){
                        oneHolder.view_lineOne.setVisibility(View.GONE);
                        oneHolder.view_lineTwo.setVisibility(View.GONE);
                        oneHolder.mtv_one.setVisibility(View.GONE);
                    }else {
                        if (0==position){
                            oneHolder.view_lineOne.setVisibility(View.VISIBLE);
                            oneHolder.view_lineTwo.setVisibility(View.GONE);
                        }else {
                            oneHolder.view_lineOne.setVisibility(View.GONE);
                            oneHolder.view_lineTwo.setVisibility(View.VISIBLE);
                        }
                        oneHolder.mtv_one.setVisibility(View.VISIBLE);
                        oneHolder.mtv_one.setText(data.title);
                    }
                    oneHolder.mtv_descl.setText(data.ldata.title);
                    oneHolder.mtv_numberl.setText("已售null");
                    oneHolder.mtv_pricel.setText(data.ldata.price);
                    GlideUtils.getInstance().loadImage(context,oneHolder.miv_onel,data.ldata.whole_thumb);

                    if (TextUtils.isEmpty(data.rdata.title)&&TextUtils.isEmpty(data.rdata.whole_thumb)){
                        oneHolder.mllayout_oner.setVisibility(View.INVISIBLE);
                    }else {
                        oneHolder.mllayout_oner.setVisibility(View.VISIBLE);
                        oneHolder.mtv_descr.setText(data.rdata.title);
                        oneHolder.mtv_numberr.setText("已售null");
                        oneHolder.mtv_pricer.setText(data.rdata.price);
                        GlideUtils.getInstance().loadImage(context,oneHolder.miv_oner,data.rdata.whole_thumb);
                    }
//                    BlockStoreFirstOneAdapter blockStoreFirstOneAdapter = new BlockStoreFirstOneAdapter(context, false,data.data);
//                    oneHolder.rv_one.setAdapter(blockStoreFirstOneAdapter);
                }
                break;
            case TYPE2:
                if (holder instanceof TwoHolder) {
                    TwoHolder twoHolder = (TwoHolder) holder;
                    StoreIndexEntity.Body data=datas.get(position);
                    if (TextUtils.isEmpty(data.title)){
                        twoHolder.view_lineOne.setVisibility(View.GONE);
                        twoHolder.view_lineTwo.setVisibility(View.GONE);
                        twoHolder.mtv_two.setVisibility(View.GONE);
                    }else {
                        if (0==position){
                            twoHolder.view_lineOne.setVisibility(View.VISIBLE);
                            twoHolder.view_lineTwo.setVisibility(View.GONE);
                        }else {
                            twoHolder.view_lineOne.setVisibility(View.GONE);
                            twoHolder.view_lineTwo.setVisibility(View.VISIBLE);
                        }
                        twoHolder.mtv_two.setVisibility(View.VISIBLE);
                        twoHolder.mtv_two.setText(data.title);
                    }
                    twoHolder.mtv_desc.setText(data.ldata.title);
                    twoHolder.mtv_number.setText(data.ldata.item_id);
                    twoHolder.mtv_price.setText(data.ldata.price);
                    GlideUtils.getInstance().loadImage(context,twoHolder.miv_two,data.ldata.whole_thumb);
//                    BlockStoreFirstTwoAdapter blockStoreFirstTwoAdapter = new BlockStoreFirstTwoAdapter(context, false,data.data);
//                    twoHolder.rv_two.setAdapter(blockStoreFirstTwoAdapter);
                }
                break;
            case TYPE3:
                if (holder instanceof ThreeHolder) {
                    ThreeHolder threeHolder = (ThreeHolder) holder;
                    StoreIndexEntity.Body data=datas.get(position);
                    if (0==position){
                        threeHolder.view_lineOne.setVisibility(View.VISIBLE);
                        threeHolder.view_lineTwo.setVisibility(View.GONE);
                    }else {
                        threeHolder.view_lineOne.setVisibility(View.GONE);
                        threeHolder.view_lineTwo.setVisibility(View.VISIBLE);
                    }
                    threeHolder.mtv_three.setText(data.title);
                }
                break;
            case TYPE4:
                if (holder instanceof FourHolder) {
                    FourHolder fourHolder = (FourHolder) holder;
                    StoreIndexEntity.Body data=datas.get(position);
                    if (0==position){
                        fourHolder.view_lineOne.setVisibility(View.VISIBLE);
                        fourHolder.view_lineTwo.setVisibility(View.GONE);
                    }else {
                        fourHolder.view_lineOne.setVisibility(View.GONE);
                        fourHolder.view_lineTwo.setVisibility(View.VISIBLE);
                    }
                    fourHolder.mtv_four.setText(data.textare);
                }
                break;
            case TYPE5:
                if (holder instanceof FiveHolder) {
                    FiveHolder fiveHolder = (FiveHolder) holder;
                    StoreIndexEntity.Body data=datas.get(position);
                    if (0==position){
                        fiveHolder.view_lineOne.setVisibility(View.VISIBLE);
                        fiveHolder.view_lineTwo.setVisibility(View.GONE);
                    }else {
                        fiveHolder.view_lineOne.setVisibility(View.GONE);
                        fiveHolder.view_lineTwo.setVisibility(View.VISIBLE);
                    }
                    if (data.data!=null&&data.data.size()>0){
                        fiveHolder.mtv_five.setText(data.data.get(0).description);
                        GlideUtils.getInstance().loadImage(context,fiveHolder.miv_five,data.data.get(0).whole_thumb);
                    }
                }
                break;
            case TYPE6:
                if (holder instanceof SixHolder) {
                    SixHolder sixHolder = (SixHolder) holder;
                    StoreIndexEntity.Body data=datas.get(position);
                    if (0==position){
                        sixHolder.view_lineOnel.setVisibility(View.VISIBLE);
                        sixHolder.view_lineTwol.setVisibility(View.GONE);
                        sixHolder.view_lineOner.setVisibility(View.VISIBLE);
                        sixHolder.view_lineTwor.setVisibility(View.GONE);
                    }else {
                        sixHolder.view_lineOnel.setVisibility(View.GONE);
                        sixHolder.view_lineTwol.setVisibility(View.VISIBLE);
                        sixHolder.view_lineOner.setVisibility(View.GONE);
                        sixHolder.view_lineTwor.setVisibility(View.VISIBLE);
                    }
                    if (data.data!=null&&data.data.size()>1){
                        sixHolder.mtv_sixl.setText(data.data.get(0).description);
                        GlideUtils.getInstance().loadImage(context,sixHolder.miv_sixl,data.data.get(0).whole_thumb);
                        sixHolder.mtv_sixr.setText(data.data.get(1).description);
                        GlideUtils.getInstance().loadImage(context,sixHolder.miv_sixr,data.data.get(1).whole_thumb);
                    }
                }
                break;
            case TYPE7:
                if (holder instanceof SevenHolder) {
                    SevenHolder sevenHolder = (SevenHolder) holder;

                }
                break;
            case TYPE8:
                if (holder instanceof EightHolder) {
                    EightHolder eightHolder = (EightHolder) holder;
                    StoreIndexEntity.Body data=datas.get(position);
                    if (0==position){
                        eightHolder.view_lineOnel.setVisibility(View.VISIBLE);
                        eightHolder.view_lineTwol.setVisibility(View.GONE);
                        eightHolder.view_lineOner.setVisibility(View.VISIBLE);
                        eightHolder.view_lineTwor.setVisibility(View.GONE);
                        eightHolder.view_lineOnem.setVisibility(View.VISIBLE);
                        eightHolder.view_lineTwom.setVisibility(View.GONE);
                    }else {
                        eightHolder.view_lineOnel.setVisibility(View.GONE);
                        eightHolder.view_lineTwol.setVisibility(View.VISIBLE);
                        eightHolder.view_lineOner.setVisibility(View.GONE);
                        eightHolder.view_lineTwor.setVisibility(View.VISIBLE);
                        eightHolder.view_lineOnem.setVisibility(View.GONE);
                        eightHolder.view_lineTwom.setVisibility(View.VISIBLE);
                    }
                    if (data.data!=null&&data.data.size()>2){
                        eightHolder.mtv_eightl.setText(data.data.get(0).description);
                        GlideUtils.getInstance().loadImage(context,eightHolder.miv_eightl,data.data.get(0).whole_thumb);
                        eightHolder.mtv_eightm.setText(data.data.get(1).description);
                        GlideUtils.getInstance().loadImage(context,eightHolder.miv_eightm,data.data.get(1).whole_thumb);
                        eightHolder.mtv_eightr.setText(data.data.get(2).description);
                        GlideUtils.getInstance().loadImage(context,eightHolder.miv_eightr,data.data.get(2).whole_thumb);
                    }
                }
                break;
        }
    }

    class OneHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_one,mtv_descl,mtv_pricel,mtv_numberl,mtv_descr,mtv_pricer,mtv_numberr;
        private View view_lineOne,view_lineTwo;
        private MyImageView miv_onel,miv_oner;
        private MyLinearLayout mllayout_oner;
//        private RecyclerView rv_one;

        OneHolder(View itemView) {
            super(itemView);
            mtv_one = (MyTextView) itemView.findViewById(R.id.mtv_one);
            view_lineOne = itemView.findViewById(R.id.view_lineOne);
            view_lineTwo = itemView.findViewById(R.id.view_lineTwo);
            miv_onel = (MyImageView) itemView.findViewById(R.id.miv_onel);
            miv_oner = (MyImageView) itemView.findViewById(R.id.miv_oner);
            mtv_descl = (MyTextView) itemView.findViewById(R.id.mtv_descl);
            mtv_pricel = (MyTextView) itemView.findViewById(R.id.mtv_pricel);
            mtv_numberl = (MyTextView) itemView.findViewById(R.id.mtv_numberl);
            mtv_descr = (MyTextView) itemView.findViewById(R.id.mtv_descr);
            mtv_pricer = (MyTextView) itemView.findViewById(R.id.mtv_pricer);
            mtv_numberr = (MyTextView) itemView.findViewById(R.id.mtv_numberr);
            mllayout_oner = (MyLinearLayout) itemView.findViewById(R.id.mllayout_oner);
//            rv_one = (RecyclerView) itemView.findViewById(R.id.rv_one);
//            GridLayoutManager manager = new GridLayoutManager(itemView.getContext(), 2);
//            rv_one.setLayoutManager(manager);

        }
    }

    class TwoHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_two,mtv_desc,mtv_price,mtv_number;
        private View view_lineOne,view_lineTwo;
        private MyImageView miv_two;
//        private RecyclerView rv_two;

        TwoHolder(View itemView) {
            super(itemView);
            mtv_two = (MyTextView) itemView.findViewById(R.id.mtv_two);
            mtv_desc = (MyTextView) itemView.findViewById(R.id.mtv_desc);
            mtv_price = (MyTextView) itemView.findViewById(R.id.mtv_price);
            mtv_number = (MyTextView) itemView.findViewById(R.id.mtv_number);
            view_lineOne = itemView.findViewById(R.id.view_lineOne);
            view_lineTwo = itemView.findViewById(R.id.view_lineTwo);
            miv_two = (MyImageView) itemView.findViewById(R.id.miv_two);
//            rv_two = (RecyclerView) itemView.findViewById(R.id.rv_two);
//            LinearLayoutManager manager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
//            rv_two.setLayoutManager(manager);
        }
    }

    class ThreeHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_three;
        private View view_lineOne,view_lineTwo;

        ThreeHolder(View itemView) {
            super(itemView);
            mtv_three = (MyTextView) itemView.findViewById(R.id.mtv_three);
            view_lineOne = itemView.findViewById(R.id.view_lineOne);
            view_lineTwo = itemView.findViewById(R.id.view_lineTwo);
        }
    }

    class FourHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_four;
        private View view_lineOne,view_lineTwo;

        FourHolder(View itemView) {
            super(itemView);
            mtv_four = (MyTextView) itemView.findViewById(R.id.mtv_four);
            view_lineOne = itemView.findViewById(R.id.view_lineOne);
            view_lineTwo = itemView.findViewById(R.id.view_lineTwo);
        }
    }

    class FiveHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_five;
        private MyImageView miv_five;
        private View view_lineOne,view_lineTwo;

        FiveHolder(View itemView) {
            super(itemView);
            mtv_five = (MyTextView) itemView.findViewById(R.id.mtv_five);
            miv_five = (MyImageView) itemView.findViewById(R.id.miv_five);
            view_lineOne = itemView.findViewById(R.id.view_lineOne);
            view_lineTwo = itemView.findViewById(R.id.view_lineTwo);
        }
    }

    class SixHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_sixl, mtv_sixr;
        private MyImageView miv_sixl,miv_sixr;
        private View view_lineOnel,view_lineTwol,view_lineOner,view_lineTwor;
        SixHolder(View itemView) {
            super(itemView);
            mtv_sixl = (MyTextView) itemView.findViewById(R.id.mtv_sixl);
            mtv_sixr = (MyTextView) itemView.findViewById(R.id.mtv_sixr);
            miv_sixl = (MyImageView) itemView.findViewById(R.id.miv_sixl);
            miv_sixr = (MyImageView) itemView.findViewById(R.id.miv_sixr);
            view_lineOnel = itemView.findViewById(R.id.view_lineOnel);
            view_lineTwol = itemView.findViewById(R.id.view_lineTwol);
            view_lineOner = itemView.findViewById(R.id.view_lineOner);
            view_lineTwor = itemView.findViewById(R.id.view_lineTwor);
        }
    }

    class SevenHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_seven;

        SevenHolder(View itemView) {
            super(itemView);
            mtv_seven = (MyTextView) itemView.findViewById(R.id.mtv_seven);
        }
    }

    class EightHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_eightl, mtv_eightm, mtv_eightr;
        private MyImageView miv_eightl, miv_eightm, miv_eightr;
        private View view_lineOnel,view_lineTwol,view_lineOner,view_lineTwor,view_lineOnem,view_lineTwom;

        EightHolder(View itemView) {
            super(itemView);
            mtv_eightl = (MyTextView) itemView.findViewById(R.id.mtv_eightl);
            mtv_eightm = (MyTextView) itemView.findViewById(R.id.mtv_eightm);
            mtv_eightr = (MyTextView) itemView.findViewById(R.id.mtv_eightr);
            miv_eightl = (MyImageView) itemView.findViewById(R.id.miv_eightl);
            miv_eightm = (MyImageView) itemView.findViewById(R.id.miv_eightm);
            miv_eightr = (MyImageView) itemView.findViewById(R.id.miv_eightr);
            view_lineOnel = itemView.findViewById(R.id.view_lineOnel);
            view_lineTwol = itemView.findViewById(R.id.view_lineTwol);
            view_lineOner = itemView.findViewById(R.id.view_lineOner);
            view_lineTwor = itemView.findViewById(R.id.view_lineTwor);
            view_lineOnem = itemView.findViewById(R.id.view_lineOnem);
            view_lineTwom = itemView.findViewById(R.id.view_lineTwom);
        }
    }

    class DefaultHolder extends RecyclerView.ViewHolder {

        DefaultHolder(View itemView) {
            super(itemView);
        }
    }
}
