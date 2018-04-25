package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.PFirstAd;
import com.shunlian.app.ui.activity.DayDayAct;
import com.shunlian.app.ui.core.AishangAct;
import com.shunlian.app.ui.core.GetCouponAct;
import com.shunlian.app.ui.core.KouBeiAct;
import com.shunlian.app.ui.core.PingpaiAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.MHorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFirstAd;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class FirstPageAdapter extends BaseRecyclerAdapter<GetDataEntity.MData> implements IFirstAd{
    private static final int TYPE1 = 1;//轮播
    private static final int TYPE2 = 2;//导航
    private static final int TYPE3 = 3;//会场
    private static final int TYPE4 = 4;//核心
    private static final int TYPE5 = 5;//hot1单图
    private static final int TYPE6 = 6;//hot2多图
    private static final int TYPE7 = 7;//goods
    private static final int TYPE8 = 8;//cate
    public boolean isFirst = false;
    private EightHolder mEightHolder;

    public FirstPageAdapter(Context context, boolean isShowFooter, List<GetDataEntity.MData> datas, boolean isFirst) {
        super(context, isShowFooter, datas);
        this.isFirst = isFirst;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE1:
//                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_one,parent,false);
//                View itemView = View.inflate(parent.getContext(), R.layout.block_store_first_one, null);
                return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_first_page_banner, parent, false));
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
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (lists.get(position).module) {
            case "banner":
                return TYPE1;
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
                    GetDataEntity.MData data = lists.get(position);
                    if (data.datass != null && data.datass.size() > 0) {
                        List<String> strings = new ArrayList<>();
                        for (int i = 0; i < data.datass.size(); i++) {
                            strings.add(data.datass.get(i).thumb);
                            if (i >= data.datass.size() - 1) {
                                oneHolder.kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                                oneHolder.kanner.setBanner(strings);
                                oneHolder.kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Common.goGoGo(context, data.datass.get(position).url.type, data.datass.get(position).url.item_id);
                                    }
                                });
                            }
                        }
                    }
                }
                break;
            case TYPE2:
                if (holder instanceof TwoHolder) {
                    TwoHolder twoHolder = (TwoHolder) holder;
                    GetDataEntity.MData data = lists.get(position);
                    GlideUtils.getInstance().loadImage(context, twoHolder.miv_nav1, data.datass.get(0).thumb);
                    GlideUtils.getInstance().loadImage(context, twoHolder.miv_nav2, data.datass.get(1).thumb);
                    GlideUtils.getInstance().loadImage(context, twoHolder.miv_nav3, data.datass.get(2).thumb);
                    GlideUtils.getInstance().loadImage(context, twoHolder.miv_nav4, data.datass.get(3).thumb);
                    twoHolder.mtv_nav1.setText(data.datass.get(0).title);
                    twoHolder.mtv_nav2.setText(data.datass.get(1).title);
                    twoHolder.mtv_nav3.setText(data.datass.get(2).title);
                    twoHolder.mtv_nav4.setText(data.datass.get(3).title);
//                    twoHolder.mtv_nav1.setTextColor(Color.parseColor(data.text_color));
//                    twoHolder.mtv_nav2.setTextColor(Color.parseColor(data.text_color));
//                    twoHolder.mtv_nav3.setTextColor(Color.parseColor(data.text_color));
//                    twoHolder.mtv_nav4.setTextColor(Color.parseColor(data.text_color));
//                    twoHolder.mllayout_nav.setBackgroundColor(Color.parseColor(data.bg_color));
                    twoHolder.mllayout_nav1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id);
                        }
                    });
                    twoHolder.mllayout_nav2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id);
                        }
                    });
                    twoHolder.mllayout_nav3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.goGoGo(context, data.datass.get(2).url.type, data.datass.get(2).url.item_id);
                        }
                    });
                    twoHolder.mllayout_nav4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GetCouponAct.startAct(context);
//                            Common.goGoGo(context, data.datass.get(3).url.type, data.datass.get(3).url.item_id);
                        }
                    });
                }
                break;
            case TYPE3:
                if (holder instanceof ThreeHolder) {
                    ThreeHolder threeHolder = (ThreeHolder) holder;
                    GetDataEntity.MData data = lists.get(position);
//                    threeHolder.mrlayout_root.setBackgroundColor(Color.parseColor(data.bg_color));
                    switch (data.number) {
                        case "1":
                            threeHolder.miv_one.setVisibility(View.VISIBLE);
                            threeHolder.mllayout_two.setVisibility(View.GONE);
                            threeHolder.mllayout_three.setVisibility(View.GONE);
                            threeHolder.mllayout_four.setVisibility(View.GONE);
                            threeHolder.mllayout_five.setVisibility(View.GONE);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_one, data.datass.get(0).thumb);
                            threeHolder.miv_one.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id);
                                }
                            });
                            break;
                        case "2":
                            threeHolder.miv_one.setVisibility(View.GONE);
                            threeHolder.mllayout_two.setVisibility(View.VISIBLE);
                            threeHolder.mllayout_three.setVisibility(View.GONE);
                            threeHolder.mllayout_four.setVisibility(View.GONE);
                            threeHolder.mllayout_five.setVisibility(View.GONE);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_twol, data.datass.get(0).thumb);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_twor, data.datass.get(1).thumb);
                            threeHolder.miv_twol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id);
                                }
                            });
                            threeHolder.miv_twor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id);
                                }
                            });
                            break;
                        case "3":
                            threeHolder.miv_one.setVisibility(View.GONE);
                            threeHolder.mllayout_two.setVisibility(View.GONE);
                            threeHolder.mllayout_three.setVisibility(View.VISIBLE);
                            threeHolder.mllayout_four.setVisibility(View.GONE);
                            threeHolder.mllayout_five.setVisibility(View.GONE);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_threel, data.datass.get(0).thumb);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_threem, data.datass.get(1).thumb);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_threer, data.datass.get(2).thumb);
                            threeHolder.miv_threel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id);
                                }
                            });
                            threeHolder.miv_threem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id);
                                }
                            });
                            threeHolder.miv_threer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(2).url.type, data.datass.get(2).url.item_id);
                                }
                            });
                            break;
                        case "4":
                            threeHolder.miv_one.setVisibility(View.GONE);
                            threeHolder.mllayout_two.setVisibility(View.GONE);
                            threeHolder.mllayout_three.setVisibility(View.GONE);
                            threeHolder.mllayout_four.setVisibility(View.VISIBLE);
                            threeHolder.mllayout_five.setVisibility(View.GONE);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_four1, data.datass.get(0).thumb);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_four2, data.datass.get(1).thumb);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_four3, data.datass.get(2).thumb);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_four4, data.datass.get(3).thumb);
                            threeHolder.miv_four1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id);
                                }
                            });
                            threeHolder.miv_four2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id);
                                }
                            });
                            threeHolder.miv_four3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(2).url.type, data.datass.get(2).url.item_id);
                                }
                            });
                            threeHolder.miv_four4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(3).url.type, data.datass.get(3).url.item_id);
                                }
                            });
                            break;
                        case "5":
                            threeHolder.miv_one.setVisibility(View.GONE);
                            threeHolder.mllayout_two.setVisibility(View.GONE);
                            threeHolder.mllayout_three.setVisibility(View.GONE);
                            threeHolder.mllayout_four.setVisibility(View.GONE);
                            threeHolder.mllayout_five.setVisibility(View.VISIBLE);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_fivel, data.datass.get(0).thumb);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_fivet, data.datass.get(1).thumb);
                            GlideUtils.getInstance().loadImage(context, threeHolder.miv_fiveb, data.datass.get(2).thumb);
                            threeHolder.miv_fivel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id);
                                }
                            });
                            threeHolder.miv_fivet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id);
                                }
                            });
                            threeHolder.miv_fiveb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Common.goGoGo(context, data.datass.get(2).url.type, data.datass.get(2).url.item_id);
                                }
                            });
                            break;
                    }
                }
                break;
            case TYPE4:
                if (holder instanceof FourHolder) {
                    FourHolder fourHolder = (FourHolder) holder;
                    GetDataEntity.MData data = lists.get(position);
                    if (!TextUtils.isEmpty(data.bg_color))
//                        fourHolder.mllayout_root.setBackgroundColor(Color.parseColor(data.bg_color));
                        GlideUtils.getInstance().loadImageWithView(context, fourHolder.mllayout_one,
                                data.ttth.thumb);
                    GlideUtils.getInstance().loadImageWithView(context, fourHolder.mllayout_two,
                            data.pptm.thumb);
                    GlideUtils.getInstance().loadImageWithView(context, fourHolder.mllayout_three,
                            data.asxp.thumb);
                    GlideUtils.getInstance().loadImageWithView(context, fourHolder.mllayout_four,
                            data.kbrx.thumb);
//                    fourHolder.mtv_one1.setTextColor(Color.parseColor(data.ttth.t_color));
//                    fourHolder.mtv_one2.setTextColor(Color.parseColor(data.ttth.c_color));
//                    fourHolder.mtv_two1.setTextColor(Color.parseColor(data.pptm.t_color));
//                    fourHolder.mtv_two1.setTextColor(Color.parseColor(data.pptm.c_color));
//                    fourHolder.mtv_three1.setTextColor(Color.parseColor(data.asxp.t_color));
//                    fourHolder.mtv_three2.setTextColor(Color.parseColor(data.asxp.c_color));
//                    fourHolder.mtv_four1.setTextColor(Color.parseColor(data.kbrx.t_color));
//                    fourHolder.mtv_four2.setTextColor(Color.parseColor(data.kbrx.c_color));
                    fourHolder.mtv_one1.setText(data.ttth.title);
                    fourHolder.mtv_two1.setText(data.pptm.title);
                    fourHolder.mtv_three1.setText(data.asxp.title);
                    fourHolder.mtv_four1.setText(data.kbrx.title);
                    fourHolder.mtv_one2.setText(data.ttth.content);
                    fourHolder.mtv_two2.setText(data.pptm.content);
                    fourHolder.mtv_three2.setText(data.asxp.content);
                    fourHolder.mtv_four2.setText(data.kbrx.content);
                    fourHolder.mllayout_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DayDayAct.startAct(context);
                        }
                    });
                    fourHolder.mllayout_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PingpaiAct.startAct(context);
                        }
                    });
                    fourHolder.mllayout_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AishangAct.startAct(context);
                        }
                    });
                    fourHolder.mllayout_four.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            KouBeiAct.startAct(context);
                        }
                    });
                }
                break;
            case TYPE5:
                if (holder instanceof FiveHolder) {
                    FiveHolder fiveHolder = (FiveHolder) holder;
                    GetDataEntity.MData data = lists.get(position);
                    GlideUtils.getInstance().loadImage(context, fiveHolder.miv_photo, data.pic);
                    if (isFirst) {
                        fiveHolder.view_line.setBackgroundColor(getColor(R.color.white));
                        fiveHolder.mtv_topic.setVisibility(View.GONE);
                        fiveHolder.mtv_desc.setVisibility(View.VISIBLE);
                        fiveHolder.mtv_desc.setText(data.content);
                        fiveHolder.mtv_title.setText(data.title);
                    } else {
                        fiveHolder.mtv_topic.setText(data.title);
                        fiveHolder.mtv_title.setText(data.content);
                        fiveHolder.mtv_desc.setVisibility(View.GONE);
                        fiveHolder.mtv_topic.setVisibility(View.VISIBLE);
                        fiveHolder.view_line.setBackgroundColor(getColor(R.color.value_F7F7F7));
                    }
                    SpannableStringBuilder priceBuilder = Common.changeTextSize(getString(R.string.common_yuan) + data.price,
                            getString(R.string.common_yuan), 11);
                    fiveHolder.mtv_price.setText(priceBuilder);
                    fiveHolder.mllayout_root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.goGoGo(context, data.url.type, data.url.item_id);
                        }
                    });
                }
                break;
            case TYPE6:
                if (holder instanceof SixHolder) {
                    SixHolder sixHolder = (SixHolder) holder;
                    GetDataEntity.MData data = lists.get(position);
                    GlideUtils.getInstance().loadImage(context, sixHolder.miv_photo, data.pic);
                    sixHolder.miv_photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.goGoGo(context, data.url.type, data.url.item_id);
                        }
                    });
                    if (sixHolder.firstHorizonAdapter==null){
                        sixHolder.firstHorizonAdapter=new FirstHorizonAdapter(context, false, data.datass, false);
                        sixHolder.rv_goods.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        sixHolder.rv_goods.setAdapter(new FirstHorizonAdapter(context, false, data.datass, false));
                        sixHolder.rv_goods.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
                    }
                    sixHolder.firstHorizonAdapter.notifyDataSetChanged();
                }
                break;
            case TYPE7:
                if (holder instanceof SevenHolder) {
                    SevenHolder sevenHolder = (SevenHolder) holder;
                    GetDataEntity.MData data = lists.get(position);
                    if (sevenHolder.firstHorizonAdapter==null){
                        sevenHolder.firstHorizonAdapter=new FirstHorizonAdapter(context, false, data.datass, true);
                        sevenHolder.rv_goods.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        sevenHolder.rv_goods.setAdapter(sevenHolder.firstHorizonAdapter);
                        sevenHolder.rv_goods.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
                    }
                    sevenHolder.firstHorizonAdapter.notifyDataSetChanged();
                 }
                break;
            case TYPE8:
                if (holder instanceof EightHolder) {
                    EightHolder eightHolder = (EightHolder) holder;
                    mEightHolder= (EightHolder) holder;
                    List<GetDataEntity.MData.Cate> data = lists.get(position).cates;
                    if (eightHolder.firstCategoryMenuAdapter == null) {
                        eightHolder.firstCategoryMenuAdapter = new FirstCategoryMenuAdapter(context, false, data, isFirst);
                        if (isFirst)
                            eightHolder.rv_categoryMenu.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
//                        eightHolder.cate_id=data.get(0).id;
//                        eightHolder.pFirstAd=new PFirstAd(context,this);
//                        eightHolder.pFirstAd.resetBaby(eightHolder.cate_id);
//                        eightHolder.rv_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                if (eightHolder != null) {
//                                    int lastPosition = eightHolder.gridLayoutManager.findLastVisibleItemPosition();
//                                    if (lastPosition + 1 == eightHolder.gridLayoutManager.getItemCount()) {
//                                        if (eightHolder.pFirstAd != null) {
//                                            eightHolder.pFirstAd.refreshBaby(eightHolder.cate_id);
//                                        }
//                                    }
//                                }
//                            }
//                        });
                        eightHolder.firstCategoryMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                eightHolder.firstCategoryMenuAdapter.selectedPosition = position;
                                eightHolder.firstCategoryMenuAdapter.notifyDataSetChanged();
                                eightHolder.cate_id=data.get(position).id;
                                if (eightHolder.rv_category.getScrollState() == 0) {
                                    eightHolder.pFirstAd.resetBaby(eightHolder.cate_id);
                                }
                            }
                        });
                        eightHolder.rv_categoryMenu.setAdapter(eightHolder.firstCategoryMenuAdapter);
                        eightHolder.rv_categoryMenu.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    }
                    eightHolder.firstCategoryMenuAdapter.notifyDataSetChanged();
                    if (isFirst) {
                        eightHolder.mtv_meiri.setVisibility(View.GONE);
                        eightHolder.rv_categoryMenu.setBackgroundColor(getColor(R.color.white));
                    } else {
                        eightHolder.mtv_meiri.setVisibility(View.VISIBLE);
                        eightHolder.rv_categoryMenu.setBackgroundColor(getColor(R.color.light_gray_three));
                    }
                }
                break;
        }
    }

    @Override
    public void setGoods(List<GoodsDeatilEntity.Goods> mDatas, int page, int allPage) {
        if (mEightHolder.firstMoreAdapter==null){
//            mEightHolder.firstMoreAdapter=new FirstMoreAdapter(context,mData);
            mEightHolder.gridLayoutManager=new GridLayoutManager(context,2);
            mEightHolder.rv_category.setLayoutManager(mEightHolder.gridLayoutManager);
            mEightHolder.rv_category.setAdapter(mEightHolder.firstMoreAdapter);
            GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(TransformUtil.dip2px(context, 5), false);
            mEightHolder.rv_category.addItemDecoration(gridSpacingItemDecoration);
        }else {
            mEightHolder.firstMoreAdapter.notifyDataSetChanged();
        }
        mEightHolder.firstMoreAdapter.setPageLoading(page, allPage);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    class OneHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.kanner)
        MyKanner kanner;

        OneHolder(View itemView) {
            super(itemView);
        }
    }

    class TwoHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.mllayout_nav1)
        MyLinearLayout mllayout_nav1;

        @BindView(R.id.mllayout_nav2)
        MyLinearLayout mllayout_nav2;

        @BindView(R.id.mllayout_nav3)
        MyLinearLayout mllayout_nav3;

        @BindView(R.id.mllayout_nav4)
        MyLinearLayout mllayout_nav4;

        @BindView(R.id.mllayout_nav)
        MyLinearLayout mllayout_nav;

        @BindView(R.id.miv_nav1)
        MyImageView miv_nav1;

        @BindView(R.id.miv_nav2)
        MyImageView miv_nav2;

        @BindView(R.id.miv_nav3)
        MyImageView miv_nav3;

        @BindView(R.id.miv_nav4)
        MyImageView miv_nav4;

        @BindView(R.id.mtv_nav1)
        MyTextView mtv_nav1;

        @BindView(R.id.mtv_nav2)
        MyTextView mtv_nav2;

        @BindView(R.id.mtv_nav3)
        MyTextView mtv_nav3;

        @BindView(R.id.mtv_nav4)
        MyTextView mtv_nav4;


        TwoHolder(View itemView) {
            super(itemView);
        }
    }

    class ThreeHolder extends BaseRecyclerViewHolder {
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
        }
    }

    class FourHolder extends BaseRecyclerViewHolder {
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

        FourHolder(View itemView) {
            super(itemView);
        }
    }

    class FiveHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;
        @BindView(R.id.mtv_title)
        MyTextView mtv_title;
        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;
        @BindView(R.id.mtv_price)
        MyTextView mtv_price;
        @BindView(R.id.mtv_topic)
        MyTextView mtv_topic;
        @BindView(R.id.view_line)
        View view_line;
        @BindView(R.id.mllayout_root)
        MyLinearLayout mllayout_root;

        FiveHolder(View itemView) {
            super(itemView);
        }
    }

    class SixHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;
        @BindView(R.id.rv_goods)
        RecyclerView rv_goods;
        private FirstHorizonAdapter firstHorizonAdapter;
        SixHolder(View itemView) {
            super(itemView);
        }
    }

    class SevenHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.rv_goods)
        RecyclerView rv_goods;
        private FirstHorizonAdapter firstHorizonAdapter;
        SevenHolder(View itemView) {
            super(itemView);
        }
    }

    class EightHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.mtv_meiri)
        MyTextView mtv_meiri;
        @BindView(R.id.rv_categoryMenu)
        RecyclerView rv_categoryMenu;
        @BindView(R.id.rv_category)
        RecyclerView rv_category;
        private FirstCategoryMenuAdapter firstCategoryMenuAdapter;
        private PFirstAd pFirstAd;
        private String cate_id;
        private FirstMoreAdapter firstMoreAdapter;
        private GridLayoutManager gridLayoutManager;

        EightHolder(View itemView) {
            super(itemView);
        }
    }

    class DefaultHolder extends BaseRecyclerViewHolder {

        DefaultHolder(View itemView) {
            super(itemView);
        }
    }
}
