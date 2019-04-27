package test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FirstCategoryMenuAdapter;
import com.shunlian.app.adapter.FirstHorizonAdapter;
import com.shunlian.app.adapter.FirstNavyAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.EmptyPresenter;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.fragment.first_page.CateGoryFrag;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.JosnSensorsDataAPI;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MHorItemDecoration;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.timer.HourRedDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.view.IView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class NewFirstPageAdapter extends BaseRecyclerAdapter<String> {
    private static final int TYPE9 = 9;//轮播
    private static final int TYPE2 = 2;//导航
    private static final int TYPE3 = 3;//会场
    private static final int TYPE4 = 4;//核心
    private static final int TYPE5 = 5;//hot1单图
    private static final int TYPE6 = 6;//hot2多图
    private static final int TYPE7 = 7;//goods
    private static final int TYPE8 = 8;//cate
    private static final int TYPE10 = 10;//moreGoods
    public boolean isFirst = false;


    public NewFirstPageAdapter(Context context, boolean isShowFooter, List<String> strings) {
        super(context, isShowFooter, strings);


    }

//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
//            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    if (position < mergePosition) {
//                        return manager.getSpanCount();
//                    } else {
//                        return isBottoms(position) ? manager.getSpanCount() : 1;
//                    }
//                }
//            });
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE9:
//                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_one,parent,false);
//                View itemView = View.inflate(parent.getContext(), R.layout.block_store_first_one, null);
                return new NineHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_page_banner, parent, false));
            case TYPE2:
                return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_page_nav, parent, false));
            case TYPE3:
                return new ThreeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_place, parent, false));
            case TYPE4:
                return new FourHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_core, parent, false));
            case TYPE5:
                return new FiveHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_hotone, parent, false));
            case TYPE6:
                return new SixHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_hotmore, parent, false));
            case TYPE7:
                return new SevenHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_goods, parent, false));
            case TYPE8:
                return new EightHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_category, parent, false));
            case TYPE10:
                return new TenHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_more, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position > lists.size() - 1)
            return super.getItemViewType(position);
        switch (lists.get(position)) {
            case "banner":
                return TYPE9;
            case "nav":
                return TYPE2;
            case "place":
                return TYPE3;
            case "core":
                return TYPE4;
            case "hot1":
                return TYPE5;
            case "hot2":
                return TYPE6;
            case "goods":
                return TYPE7;
            case "cate":
                return TYPE8;
            case "moreGoods":
                return TYPE10;
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
//        try {
        switch (itemViewType) {
            case TYPE9:
                if (holder instanceof NineHolder) {
                    NineHolder nineHolder = (NineHolder) holder;

                }
                break;
            case TYPE2:
                if (holder instanceof TwoHolder) {
                    TwoHolder twoHolder = (TwoHolder) holder;
                }
                break;
            case TYPE3:
                if (holder instanceof ThreeHolder) {
                    ThreeHolder threeHolder = (ThreeHolder) holder;

                }
                break;
            case TYPE4:
                if (holder instanceof FourHolder) {
                    FourHolder fourHolder = (FourHolder) holder;
                }
                break;
            case TYPE5:
                if (holder instanceof FiveHolder) {
                    FiveHolder fiveHolder = (FiveHolder) holder;

                }
                break;
            case TYPE6:
                if (holder instanceof SixHolder) {
                    SixHolder sixHolder = (SixHolder) holder;

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

                }
                break;
            case TYPE10:
                if (holder instanceof TenHolder) {
                    TenHolder tenHolder = (TenHolder) holder;
                }
                break;
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void bannerInfoClick(String type,GetDataEntity.MData ndata){

    }


//    @Override
//    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
//        mShareInfoParam.shareLink = baseEntity.data.shareLink;
//        mShareInfoParam.img = baseEntity.data.img;
//        mShareInfoParam.desc = baseEntity.data.desc;
//        mShareInfoParam.price = baseEntity.data.price;
//        mShareInfoParam.market_price = baseEntity.data.market_price;
//        if(!TextUtils.isEmpty(baseEntity.data.share_buy_earn))
//        mShareInfoParam.share_buy_earn = baseEntity.data.share_buy_earn;
//        mShareInfoParam.voucher = baseEntity.data.voucher;
//        mShareInfoParam.little_word = baseEntity.data.little_word;
//        mShareInfoParam.time_text = baseEntity.data.time_text;
//        mShareInfoParam.is_start = baseEntity.data.is_start;
//        mShareInfoParam.title = baseEntity.data.title;
//        shareGoodDialogUtil.shareGoodDialog(mShareInfoParam, true, false);
//    }
//
//    public TextView creatTextTag(String content, int colorRes, Drawable drawable, TenHolder tenHolder) {
//        TextView textView = new TextView(context);
//        textView.setText(content);
//        textView.setTextSize(9);
//        textView.setBackgroundDrawable(drawable);
//        textView.setTextColor(colorRes);
//        int padding = TransformUtil.dip2px(context, 3f);
//        textView.setPadding(padding, 0, padding, 0);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        if (tenHolder.mllayout_tag.getChildCount() == 0) {
//            params.setMargins(0, 0, 0, 0);
//        } else {
//            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
//        }
//        textView.setLayoutParams(params);
//        return textView;
//    }
//
//    private String goodId;
//    @Override
//    public void showGoodsSku(GoodsDeatilEntity.Goods goods) {
//        this.goodId = goods.goods_id;
//        ParamDialog paramDialog  = new ParamDialog(context, goods);
//        paramDialog.setOnGoodsBuyCallBack(NewFirstPageAdapter.this);
//        if (paramDialog != null){
//            paramDialog.show();
//        }
//    }
//
//    @Override
//    public void onAddCar(GoodsDeatilEntity.Sku sku, int count) {
//        onSelectComplete(sku,count,true);
//    }
//
//    @Override
//    public void onBuyNow(GoodsDeatilEntity.Sku sku, int count) {
//        onSelectComplete(sku,count,false);
//    }
//    //@Override
//
//    public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count,boolean isAddcart) {
//        if (context instanceof MainActivity) {
//            MainActivity goodsDetailAct = (MainActivity) context;
//            goodsDetailAct.selectGoodsInfo(sku, count,isAddcart,this.goodId);
//        }
//    }


    class NineHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.kanner)
        MyKanner kanner;

        NineHolder(View itemView) {
            super(itemView);
        }
    }

    class TenHolder extends BaseRecyclerViewHolders {
        //        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        //        @BindView(R.id.mllayout_root)
        MyLinearLayout mllayout_root;

        //        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        //        @BindView(R.id.mllayout_tag)
        MyLinearLayout mllayout_tag;

        //        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        MyTextView mtv_earn;
        TenHolder(View itemView) {
            super(itemView);
            mllayout_root = (MyLinearLayout) itemView.findViewById(R.id.mllayout_root);
            miv_photo = (MyImageView) itemView.findViewById(R.id.miv_photo);
            mtv_title = (MyTextView) itemView.findViewById(R.id.mtv_title);
            mtv_earn = (MyTextView) itemView.findViewById(R.id.mtv_earn);
            mllayout_tag = (MyLinearLayout) itemView.findViewById(R.id.mllayout_tag);
            mtv_price = (MyTextView) itemView.findViewById(R.id.mtv_price);
            int picWidth = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth / 2, picWidth / 2);
            miv_photo.setLayoutParams(params);
        }
    }

    class TwoHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.rv_nav)
        RecyclerView rv_nav;

        private FirstNavyAdapter firstNavyAdapter;

//        @BindView(R.id.mllayout_nav1)
//        MyLinearLayout mllayout_nav1;
//
//        @BindView(R.id.mllayout_nav2)
//        MyLinearLayout mllayout_nav2;
//
//        @BindView(R.id.mllayout_nav3)
//        MyLinearLayout mllayout_nav3;
//
//        @BindView(R.id.mllayout_nav4)
//        MyLinearLayout mllayout_nav4;
//
//        @BindView(R.id.mllayout_nav)
//        MyLinearLayout mllayout_nav;
//
//        @BindView(R.id.miv_nav1)
//        MyImageView miv_nav1;
//
//        @BindView(R.id.miv_nav2)
//        MyImageView miv_nav2;
//
//        @BindView(R.id.miv_nav3)
//        MyImageView miv_nav3;
//
//        @BindView(R.id.miv_nav4)
//        MyImageView miv_nav4;
//
//        @BindView(R.id.mtv_nav1)
//        MyTextView mtv_nav1;
//
//        @BindView(R.id.mtv_nav2)
//        MyTextView mtv_nav2;
//
//        @BindView(R.id.mtv_nav3)
//        MyTextView mtv_nav3;
//
//        @BindView(R.id.mtv_nav4)
//        MyTextView mtv_nav4;


        TwoHolder(View itemView) {
            super(itemView);
//            int picWidth = Common.getScreenWidth((Activity) context);
//            int height = picWidth * 192 / 720;
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, height);
//            mllayout_nav.setLayoutParams(params);
        }
    }

    class ThreeHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.miv_one)
        MyImageView miv_one;
        @BindView(R.id.miv_twol)
        MyImageView miv_twol;
        @BindView(R.id.miv_twor)
        MyImageView miv_twor;
        @BindView(R.id.miv_threel)
        MyImageView miv_threel;
        @BindView(R.id.miv_threem)
        MyImageView miv_threem;
        @BindView(R.id.miv_threer)
        MyImageView miv_threer;
        @BindView(R.id.miv_four1)
        MyImageView miv_four1;
        @BindView(R.id.miv_four2)
        MyImageView miv_four2;
        @BindView(R.id.miv_four3)
        MyImageView miv_four3;
        @BindView(R.id.miv_four4)
        MyImageView miv_four4;
        @BindView(R.id.miv_fivel)
        MyImageView miv_fivel;
        @BindView(R.id.miv_fivet)
        MyImageView miv_fivet;
        @BindView(R.id.miv_fiveb)
        MyImageView miv_fiveb;
        @BindView(R.id.mllayout_two)
        MyLinearLayout mllayout_two;
        @BindView(R.id.mllayout_three)
        MyLinearLayout mllayout_three;
        @BindView(R.id.mllayout_four)
        MyLinearLayout mllayout_four;
        @BindView(R.id.mllayout_five)
        MyLinearLayout mllayout_five;
        @BindView(R.id.mrlayout_root)
        MyRelativeLayout mrlayout_root;

        ThreeHolder(View itemView) {
            super(itemView);
//            int picWidth = (Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context,18) - TransformUtil.dip2px(context,38)) / 3;
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, picWidth);
//            miv_pic.setLayoutParams(params);
        }
    }

    class FourHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.mllayout_root)
        MyLinearLayout mllayout_root;
        @BindView(R.id.mllayout_one)
        MyLinearLayout mllayout_one;
        @BindView(R.id.mllayout_two)
        MyLinearLayout mllayout_two;
        @BindView(R.id.mllayout_three)
        MyLinearLayout mllayout_three;
        @BindView(R.id.mllayout_four)
        MyLinearLayout mllayout_four;
        @BindView(R.id.mtv_one1)
        MyTextView mtv_one1;
        @BindView(R.id.mtv_one2)
        MyTextView mtv_one2;
        @BindView(R.id.mtv_two1)
        MyTextView mtv_two1;
        @BindView(R.id.mtv_two2)
        MyTextView mtv_two2;
        @BindView(R.id.mtv_three1)
        MyTextView mtv_three1;
        @BindView(R.id.mtv_three2)
        MyTextView mtv_three2;
        @BindView(R.id.mtv_four1)
        MyTextView mtv_four1;
        @BindView(R.id.mtv_four2)
        MyTextView mtv_four2;
        @BindView(R.id.downTime_first)
        HourRedDownTimerView downTime_first;

        FourHolder(View itemView) {
            super(itemView);
            int picWidth = Common.getScreenWidth((Activity) context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, picWidth * 226 / 360);
            params.setMargins(0, TransformUtil.dip2px(context, 10), 0, 0);
            mllayout_root.setLayoutParams(params);

        }
    }

    class FiveHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_share)
        MyTextView mtv_share;

        @BindView(R.id.mtv_buy_num)
        MyTextView mtv_buy_num;

        @BindView(R.id.mtv_buy)
        MyTextView mtv_buy;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_store_title)
        MyTextView mtv_store_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_market_price)
        MyTextView mtv_market_price;

        @BindView(R.id.mtv_topic)
        MyTextView mtv_topic;

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.mllayout_root)
        MyLinearLayout mllayout_root;

        @BindView(R.id.mllayout_pingzhi)
        MyLinearLayout mllayout_pingzhi;

        FiveHolder(View itemView) {
            super(itemView);
        }
    }

    class SixHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;
        @BindView(R.id.rv_goods)
        RecyclerView rv_goods;
        @BindView(R.id.mtv_topic)
        MyTextView mtv_topic;
        @BindView(R.id.view_line)
        View view_line;
        @BindView(R.id.mllayout_pingzhi)
        MyLinearLayout mllayout_pingzhi;
        private FirstHorizonAdapter firstHorizonAdapter;

        SixHolder(View itemView) {
            super(itemView);
            rv_goods.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
        }
    }

    class SevenHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.rv_goods)
        RecyclerView rv_goods;

        @BindView(R.id.mtv_xianshi)
        MyTextView mtv_xianshi;
        private FirstHorizonAdapter firstHorizonAdapter;

        SevenHolder(View itemView) {
            super(itemView);
            rv_goods.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
        }
    }

    class EightHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.mtv_meiri)
        MyTextView mtv_meiri;
        @BindView(R.id.rv_categoryMenu)
        RecyclerView rv_categoryMenu;
        private FirstCategoryMenuAdapter firstCategoryMenuAdapter;

        EightHolder(View itemView) {
            super(itemView);
            if (isFirst)
                rv_categoryMenu.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
        }
    }

    class DefaultHolder extends BaseRecyclerViewHolders {

        DefaultHolder(View itemView) {
            super(itemView);
        }
    }
}
