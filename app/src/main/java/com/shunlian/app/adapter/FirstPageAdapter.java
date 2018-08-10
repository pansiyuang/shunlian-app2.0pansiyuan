package com.shunlian.app.adapter;

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
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.ui.activity.DayDayAct;
import com.shunlian.app.ui.core.AishangAct;
import com.shunlian.app.ui.core.KouBeiAct;
import com.shunlian.app.ui.core.PingpaiAct;
import com.shunlian.app.ui.fragment.first_page.CateGoryFrag;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.MHorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.timer.HourRedDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.popmenu.PopMenu;
import com.shunlian.app.widget.popmenu.PopMenuItem;
import com.shunlian.app.widget.popmenu.PopMenuItemCallback;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class FirstPageAdapter extends BaseRecyclerAdapter<GetDataEntity.MData> {
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
    public int mergePosition = 0;
    private CateGoryFrag cateGoryFrag;
    private ShareInfoParam mShareInfoParam = new ShareInfoParam();
    private int second = (int) (System.currentTimeMillis() / 1000);
    private int hotPosition = -1;

    public FirstPageAdapter(Context context, boolean isShowFooter, List<GetDataEntity.MData> datas, boolean isFirst, CateGoryFrag cateGoryFrag, int mergePosition) {
        super(context, isShowFooter, datas);
        this.isFirst = isFirst;
        this.cateGoryFrag = cateGoryFrag;
        this.mergePosition = mergePosition;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position < mergePosition) {
                        return manager.getSpanCount();
                    } else {
                        return isBottoms(position) ? manager.getSpanCount() : 1;
                    }
                }
            });
        }
    }

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
        switch (lists.get(position).module) {
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
                        GetDataEntity.MData data = lists.get(position);
                        if (data.datass != null && data.datass.size() > 0) {
                            List<String> strings = new ArrayList<>();
                            for (int i = 0; i < data.datass.size(); i++) {
                                strings.add(data.datass.get(i).thumb);
                                if (i >= data.datass.size() - 1) {
                                    nineHolder.kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                                    nineHolder.kanner.setBanner(strings);
                                    nineHolder.kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                                        @Override
                                        public void onItemClick(int position) {
                                            Common.goGoGo(context, data.datass.get(position).url.type, data.datass.get(position).url.item_id, data.datass.get(position).url.channe_id);
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
                        GlideUtils.getInstance().loadImage(context, twoHolder.miv_nav1, data.datass.get(0).thumb, R.mipmap.img_default_home_nav);
                        GlideUtils.getInstance().loadImage(context, twoHolder.miv_nav2, data.datass.get(1).thumb, R.mipmap.img_default_home_nav);
                        GlideUtils.getInstance().loadImage(context, twoHolder.miv_nav3, data.datass.get(2).thumb, R.mipmap.img_default_home_nav);
                        GlideUtils.getInstance().loadImage(context, twoHolder.miv_nav4, data.datass.get(3).thumb, R.mipmap.img_default_home_nav);
                        twoHolder.mtv_nav1.setText(data.datass.get(0).title);
                        twoHolder.mtv_nav2.setText(data.datass.get(1).title);
                        twoHolder.mtv_nav3.setText(data.datass.get(2).title);
                        twoHolder.mtv_nav4.setText(data.datass.get(3).title);
                        if (Common.isColor(data.text_color)) {
                            twoHolder.mtv_nav1.setTextColor(Color.parseColor(data.text_color));
                            twoHolder.mtv_nav2.setTextColor(Color.parseColor(data.text_color));
                            twoHolder.mtv_nav3.setTextColor(Color.parseColor(data.text_color));
                            twoHolder.mtv_nav4.setTextColor(Color.parseColor(data.text_color));
                        }
                        if (!TextUtils.isEmpty(data.bg_pic)) {
                            GlideUtils.getInstance().loadBgImage(context, twoHolder.mllayout_nav, data.bg_pic);
                        } else if (Common.isColor(data.bg_color)) {
                            twoHolder.mllayout_nav.setBackgroundColor(Color.parseColor(data.bg_color));
                        }
                        twoHolder.mllayout_nav1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id, data.datass.get(0).url.channe_id);
                            }
                        });
                        twoHolder.mllayout_nav2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id, data.datass.get(1).url.channe_id);
                            }
                        });
                        twoHolder.mllayout_nav3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.goGoGo(context, data.datass.get(2).url.type, data.datass.get(2).url.item_id, data.datass.get(2).url.channe_id);
                            }
                        });
                        twoHolder.mllayout_nav4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                            GetCouponAct.startAct(context);
                                Common.goGoGo(context, data.datass.get(3).url.type, data.datass.get(3).url.item_id, data.datass.get(3).url.channe_id);
                            }
                        });
                    }
                    break;
                case TYPE3:
                    if (holder instanceof ThreeHolder) {
                        ThreeHolder threeHolder = (ThreeHolder) holder;
                        GetDataEntity.MData data = lists.get(position);
                        if (Common.isColor(data.bg_color))
                            threeHolder.mrlayout_root.setBackgroundColor(Color.parseColor(data.bg_color));
                        if (!isEmpty(data.number))
                            switch (data.number) {
                                case "1":
                                    threeHolder.miv_one.setVisibility(View.VISIBLE);
                                    threeHolder.mllayout_two.setVisibility(View.GONE);
                                    threeHolder.mllayout_three.setVisibility(View.GONE);
                                    threeHolder.mllayout_four.setVisibility(View.GONE);
                                    threeHolder.mllayout_five.setVisibility(View.GONE);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_one, data.datass.get(0).thumb, R.mipmap.img_default_home_retui);
                                    if (!isEmpty(data.datass.get(0).width)) {
                                        int picWidth = Common.getScreenWidth((Activity) context);
                                        int height = picWidth * Integer.parseInt(data.datass.get(0).height) / Integer.parseInt(data.datass.get(0).width);
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(picWidth, height);
                                        threeHolder.miv_one.setLayoutParams(params);
                                    }
                                    threeHolder.miv_one.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id, data.datass.get(0).url.channe_id);
                                        }
                                    });
                                    break;
                                case "2":
                                    threeHolder.miv_one.setVisibility(View.GONE);
                                    threeHolder.mllayout_two.setVisibility(View.VISIBLE);
                                    threeHolder.mllayout_three.setVisibility(View.GONE);
                                    threeHolder.mllayout_four.setVisibility(View.GONE);
                                    threeHolder.mllayout_five.setVisibility(View.GONE);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_twol, data.datass.get(0).thumb, R.mipmap.img_default_home_promotion2);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_twor, data.datass.get(1).thumb, R.mipmap.img_default_home_promotion2);
                                    threeHolder.miv_twol.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id, data.datass.get(0).url.channe_id);
                                        }
                                    });
                                    threeHolder.miv_twor.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id, data.datass.get(1).url.channe_id);
                                        }
                                    });
                                    break;
                                case "3":
                                    threeHolder.miv_one.setVisibility(View.GONE);
                                    threeHolder.mllayout_two.setVisibility(View.GONE);
                                    threeHolder.mllayout_three.setVisibility(View.VISIBLE);
                                    threeHolder.mllayout_four.setVisibility(View.GONE);
                                    threeHolder.mllayout_five.setVisibility(View.GONE);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_threel, data.datass.get(0).thumb, R.mipmap.img_default_home_promotion3);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_threem, data.datass.get(1).thumb, R.mipmap.img_default_home_promotion3);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_threer, data.datass.get(2).thumb, R.mipmap.img_default_home_promotion3);
                                    threeHolder.miv_threel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id, data.datass.get(0).url.channe_id);
                                        }
                                    });
                                    threeHolder.miv_threem.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id, data.datass.get(1).url.channe_id);
                                        }
                                    });
                                    threeHolder.miv_threer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(2).url.type, data.datass.get(2).url.item_id, data.datass.get(2).url.channe_id);
                                        }
                                    });
                                    break;
                                case "4":
                                    threeHolder.miv_one.setVisibility(View.GONE);
                                    threeHolder.mllayout_two.setVisibility(View.GONE);
                                    threeHolder.mllayout_three.setVisibility(View.GONE);
                                    threeHolder.mllayout_four.setVisibility(View.VISIBLE);
                                    threeHolder.mllayout_five.setVisibility(View.GONE);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_four1, data.datass.get(0).thumb, R.mipmap.img_default_home_promotion4);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_four2, data.datass.get(1).thumb, R.mipmap.img_default_home_promotion4);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_four3, data.datass.get(2).thumb, R.mipmap.img_default_home_promotion4);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_four4, data.datass.get(3).thumb, R.mipmap.img_default_home_promotion4);
                                    threeHolder.miv_four1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id, data.datass.get(0).url.channe_id);
                                        }
                                    });
                                    threeHolder.miv_four2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id, data.datass.get(1).url.channe_id);
                                        }
                                    });
                                    threeHolder.miv_four3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(2).url.type, data.datass.get(2).url.item_id, data.datass.get(2).url.channe_id);
                                        }
                                    });
                                    threeHolder.miv_four4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(3).url.type, data.datass.get(3).url.item_id, data.datass.get(3).url.channe_id);
                                        }
                                    });
                                    break;
                                case "5":
                                    threeHolder.miv_one.setVisibility(View.GONE);
                                    threeHolder.mllayout_two.setVisibility(View.GONE);
                                    threeHolder.mllayout_three.setVisibility(View.GONE);
                                    threeHolder.mllayout_four.setVisibility(View.GONE);
                                    threeHolder.mllayout_five.setVisibility(View.VISIBLE);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_fivel, data.datass.get(0).thumb, R.mipmap.img_default_home_promotion3_1);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_fivet, data.datass.get(1).thumb, R.mipmap.img_default_home_promotion3_2);
                                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_fiveb, data.datass.get(2).thumb, R.mipmap.img_default_home_promotion3_2);
                                    threeHolder.miv_fivel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(0).url.type, data.datass.get(0).url.item_id, data.datass.get(0).url.channe_id);
                                        }
                                    });
                                    threeHolder.miv_fivet.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(1).url.type, data.datass.get(1).url.item_id, data.datass.get(1).url.channe_id);
                                        }
                                    });
                                    threeHolder.miv_fiveb.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Common.goGoGo(context, data.datass.get(2).url.type, data.datass.get(2).url.item_id, data.datass.get(2).url.channe_id);
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
                        if (Common.isColor(data.bg_color))
                            fourHolder.mllayout_root.setBackgroundColor(Color.parseColor(data.bg_color));
                        GlideUtils.getInstance().loadBgImage(context, fourHolder.mllayout_one,
                                data.ttth.thumb, R.mipmap.img_default_home_ttth);
                        GlideUtils.getInstance().loadBgImage(context, fourHolder.mllayout_two,
                                data.pptm.thumb, R.mipmap.img_default_home_pinpai);
                        GlideUtils.getInstance().loadBgImage(context, fourHolder.mllayout_three,
                                data.asxp.thumb, R.mipmap.img_default_home_xinpin);
                        GlideUtils.getInstance().loadBgImage(context, fourHolder.mllayout_four,
                                data.kbrx.thumb, R.mipmap.img_default_home_xinpin);
                        int seconds = (int) (System.currentTimeMillis() / 1000) - second;
                        if (fourHolder.downTime_first != null) {
                            fourHolder.downTime_first.cancelDownTimer();
                            fourHolder.downTime_first.setDownTime(Integer.parseInt(data.ttth.count_down) - seconds);
//                    fourHolder.downTime_first.setDownTime(10);
                            fourHolder.downTime_first.startDownTimer();
                        }
                        if (Common.isColor(data.ttth.t_color)) {
                            fourHolder.mtv_one1.setTextColor(Color.parseColor(data.ttth.t_color));
                        }
                        if (Common.isColor(data.ttth.c_color))
                            fourHolder.mtv_one2.setTextColor(Color.parseColor(data.ttth.c_color));
                        if (Common.isColor(data.pptm.t_color))
                            fourHolder.mtv_two1.setTextColor(Color.parseColor(data.pptm.t_color));
                        if (Common.isColor(data.pptm.c_color))
                            fourHolder.mtv_two2.setTextColor(Color.parseColor(data.pptm.c_color));
                        if (Common.isColor(data.asxp.t_color))
                            fourHolder.mtv_three1.setTextColor(Color.parseColor(data.asxp.t_color));
                        if (Common.isColor(data.asxp.c_color))
                            fourHolder.mtv_three2.setTextColor(Color.parseColor(data.asxp.c_color));
                        if (Common.isColor(data.kbrx.t_color))
                            fourHolder.mtv_four1.setTextColor(Color.parseColor(data.kbrx.t_color));
                        if (Common.isColor(data.kbrx.c_color))
                            fourHolder.mtv_four2.setTextColor(Color.parseColor(data.kbrx.c_color));
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
                        int picWidth = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 20);
                        int height = picWidth * 158 / 340;
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, height);
                        params.setMargins(TransformUtil.dip2px(context, 10), 0, TransformUtil.dip2px(context, 10), 0);
                        if (fiveHolder.miv_photo != null) {
                            fiveHolder.miv_photo.setLayoutParams(params);
                            GlideUtils.getInstance().loadBgImageChang(context, fiveHolder.miv_photo, data.pic);
                        }
                        fiveHolder.mtv_topic.setVisibility(View.GONE);
//                    fiveHolder.view_line.setBackgroundColor(getColor(R.color.white));
                        fiveHolder.view_line.setVisibility(View.GONE);
                        fiveHolder.mllayout_pingzhi.setVisibility(View.GONE);
                        if (!isEmpty(data.content)) {
                            fiveHolder.mtv_desc.setVisibility(View.VISIBLE);
                            fiveHolder.mtv_desc.setText(data.content);
                        }
                        fiveHolder.mtv_title.setText(data.title);
                        if (hotPosition == -1)
                            hotPosition = position;
                        if (position == hotPosition) {
                            if (isFirst) {
                                fiveHolder.mllayout_pingzhi.setVisibility(View.VISIBLE);
                            } else {
                                fiveHolder.mtv_topic.setText(getString(R.string.first_jingxuan));
                                fiveHolder.mtv_topic.setVisibility(View.VISIBLE);
//                            fiveHolder.view_line.setBackgroundColor(getColor(R.color.value_F7F7F7));
                                fiveHolder.view_line.setVisibility(View.VISIBLE);
                            }
                        }
                        if (!TextUtils.isEmpty(data.price)) {
                            SpannableStringBuilder priceBuilder = Common.changeTextSize(getString(R.string.common_yuan) + data.price,
                                    getString(R.string.common_yuan), 11);
                            fiveHolder.mtv_price.setText(priceBuilder);
                            fiveHolder.mtv_price.setVisibility(View.VISIBLE);
                        } else {
                            fiveHolder.mtv_price.setVisibility(View.GONE);
                        }
                        fiveHolder.miv_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mShareInfoParam.title = data.share.title;
                                mShareInfoParam.desc = data.share.content;
                                mShareInfoParam.img = data.share.logo;

                                if (!Common.isAlreadyLogin()) {
                                    Common.goGoGo(context, "login");
                                    return;
                                }
                                if (!isEmpty(data.share.share_url)) {
                                    mShareInfoParam.shareLink = data.share.share_url;
                                    shareStyle2Dialog();
                                } else {

                                    cateGoryFrag.getShareInfo(data.url.type, data.url.item_id);
                                }
                            }
                        });
                        fiveHolder.mllayout_root.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.goGoGo(context, data.url.type, data.url.item_id, data.url.channe_id);
                            }
                        });
                    }
                    break;
                case TYPE6:
                    if (holder instanceof SixHolder) {
                        SixHolder sixHolder = (SixHolder) holder;
                        sixHolder.mtv_topic.setVisibility(View.GONE);
//                    fiveHolder.view_line.setBackgroundColor(getColor(R.color.white));
                        sixHolder.view_line.setVisibility(View.GONE);
                        sixHolder.mllayout_pingzhi.setVisibility(View.GONE);
                        if (hotPosition == -1)
                            hotPosition = position;
                        if (position == hotPosition) {
                            if (isFirst) {
                                sixHolder.mllayout_pingzhi.setVisibility(View.VISIBLE);
                            } else {
                                sixHolder.mtv_topic.setText(getString(R.string.first_jingxuan));
                                sixHolder.mtv_topic.setVisibility(View.VISIBLE);
                                sixHolder.view_line.setVisibility(View.VISIBLE);
                            }
                        }
                        GetDataEntity.MData data = lists.get(position);
                        int picWidth = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 20);
                        int height = picWidth * 158 / 340;
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, height);
                        params.setMargins(TransformUtil.dip2px(context, 10), 0, TransformUtil.dip2px(context, 10), 0);
                        sixHolder.miv_photo.setLayoutParams(params);
                        GlideUtils.getInstance().loadBgImageChang(context, sixHolder.miv_photo, data.pic);
                        sixHolder.miv_photo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.goGoGo(context, data.url.type, data.url.item_id, data.url.channe_id);
                            }
                        });
                        if (sixHolder.firstHorizonAdapter == null) {
                            sixHolder.firstHorizonAdapter = new FirstHorizonAdapter(context, false, data.datass, false);
                            sixHolder.rv_goods.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            sixHolder.rv_goods.setAdapter(sixHolder.firstHorizonAdapter);
                            sixHolder.rv_goods.setNestedScrollingEnabled(false);
                            sixHolder.rv_goods.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
                            sixHolder.firstHorizonAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    GoodsDetailAct.startAct(context, data.datass.get(position).url.item_id);
                                }
                            });
                        }
                        sixHolder.firstHorizonAdapter.notifyDataSetChanged();
                        sixHolder.rv_goods.setFocusable(false);
                    }
                    break;
                case TYPE7:
                    if (holder instanceof SevenHolder) {
                        SevenHolder sevenHolder = (SevenHolder) holder;
                        GetDataEntity.MData data = lists.get(position);
                        if (sevenHolder.firstHorizonAdapter == null) {
                            sevenHolder.firstHorizonAdapter = new FirstHorizonAdapter(context, false, data.datass, true);
                            sevenHolder.rv_goods.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            sevenHolder.rv_goods.setAdapter(sevenHolder.firstHorizonAdapter);
                            sevenHolder.rv_goods.setNestedScrollingEnabled(false);
                            sevenHolder.rv_goods.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
                            sevenHolder.firstHorizonAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    GoodsDetailAct.startAct(context, data.datass.get(position).url.item_id);
                                }
                            });
                        }
                        sevenHolder.firstHorizonAdapter.notifyDataSetChanged();
                        sevenHolder.rv_goods.setFocusable(false);
                    }
                    break;
                case TYPE8:
                    if (holder instanceof EightHolder) {
                        EightHolder eightHolder = (EightHolder) holder;
                        List<GetDataEntity.MData.Cate> data = lists.get(position).cates;
                        if (eightHolder.firstCategoryMenuAdapter == null) {
//                        cateGoryFrag.cate_id = data.get(0).id;
//                        cateGoryFrag.pFirstPage.resetBaby(cateGoryFrag.cate_id);
                            eightHolder.firstCategoryMenuAdapter = new FirstCategoryMenuAdapter(context, false, data, isFirst);
                            if (isFirst)
                                eightHolder.rv_categoryMenu.addItemDecoration(new MHorItemDecoration(context, 10, 10, 10));
                            eightHolder.firstCategoryMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (cateGoryFrag != null && cateGoryFrag.rv_view != null && cateGoryFrag.rv_view.getScrollState() == 0) {
                                        eightHolder.firstCategoryMenuAdapter.selectedPosition = position;
                                        eightHolder.firstCategoryMenuAdapter.notifyDataSetChanged();
                                        cateGoryFrag.cate_id = data.get(position).id;
                                        cateGoryFrag.sort_type = data.get(position).sort_type;
                                        if (cateGoryFrag.pFirstPage != null)
                                            cateGoryFrag.pFirstPage.resetBaby(data.get(position).id,data.get(position).sort_type);
                                    }
                                }
                            });
                            eightHolder.rv_categoryMenu.setAdapter(eightHolder.firstCategoryMenuAdapter);
                            eightHolder.rv_categoryMenu.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        } else {
                            eightHolder.firstCategoryMenuAdapter.notifyDataSetChanged();
                        }
                        if (isFirst) {
                            eightHolder.mtv_meiri.setVisibility(View.GONE);
                            eightHolder.rv_categoryMenu.setBackgroundColor(getColor(R.color.white));
                        } else {
                            eightHolder.mtv_meiri.setVisibility(View.VISIBLE);
                            eightHolder.rv_categoryMenu.setBackgroundColor(getColor(R.color.light_gray_three));
                        }
                    }
                    break;
                case TYPE10:
                    if (holder instanceof TenHolder) {
                        TenHolder tenHolder = (TenHolder) holder;
                        GoodsDeatilEntity.Goods goods = lists.get(position).moreGoods;
                        GlideUtils.getInstance().loadImageZheng(context, tenHolder.miv_photo, goods.thumb);
                        if (tenHolder.mtv_title != null)
                            tenHolder.mtv_title.setText(goods.title);
                        if (!isEmpty(goods.price) && tenHolder.mtv_price != null) {
                            SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan) + goods.price, getString(R.string.common_yuan), 12);
                            tenHolder.mtv_price.setText(spannableStringBuilder);
                        }
                        if (tenHolder.mllayout_tag!=null)
                        tenHolder.mllayout_tag.removeAllViews();
                        tenHolder.mllayout_root.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GoodsDetailAct.startAct(context, goods.id);
                            }
                        });
                        if ("1".equals(goods.is_new)) {
                            tenHolder.mllayout_tag.addView(creatTextTag("新品", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fbd500_2px), tenHolder));
                        }

                        if ("1".equals(goods.is_hot)) {
                            tenHolder.mllayout_tag.addView(creatTextTag("热卖", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb9f00_2px), tenHolder));
                        }

                        if ("1".equals(goods.is_explosion)) {
                            tenHolder.mllayout_tag.addView(creatTextTag("爆款", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_fb6400_2px), tenHolder));
                        }

                        if ("1".equals(goods.is_recommend)) {
                            tenHolder.mllayout_tag.addView(creatTextTag("推荐", getColor(R.color.white), getDrawable(R.drawable.rounded_corner_7898da_2px), tenHolder));
                        }

                        if ("1".equals(goods.has_coupon)) {
                            tenHolder.mllayout_tag.addView(creatTextTag("劵", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), tenHolder));
                        }

                        if ("1".equals(goods.has_discount)) {
                            tenHolder.mllayout_tag.addView(creatTextTag("折", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), tenHolder));
                        }

                        if ("1".equals(goods.has_gift)) {
                            tenHolder.mllayout_tag.addView(creatTextTag("赠", getColor(R.color.value_f46c6f), getDrawable(R.drawable.rounded_corner_f46c6f_2px), tenHolder));
                        }

                    }
                    break;
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        mShareInfoParam = baseEntity.data;
        shareStyle2Dialog();
    }

    /**
     * 分享微信和复制链接
     */
    public void shareStyle2Dialog() {
        PopMenu mPopMenu = new PopMenu.Builder().attachToActivity((Activity) context)
                .addMenuItem(new PopMenuItem("微信", getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem("复制链接", getDrawable(R.mipmap.icon_lianjie)))
                .setOnItemClickListener(new PopMenuItemCallback() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                WXEntryActivity.startAct(context,
                                        "shareFriend", mShareInfoParam);
                                break;
                            case 1:
                                Common.copyText(context, mShareInfoParam.shareLink, mShareInfoParam.desc, true);
                                break;
                        }
                    }

                    @Override
                    public void onHideCallback(Activity mActivity) {
                        ImmersionBar.with(mActivity).fitsSystemWindows(true)
                                .statusBarColor(R.color.pink_color)
                                .statusBarDarkFont(false, 0)
                                .init();
                        Constant.IS_FIRST_SHARE = false;
                    }
                }).build();
        if (!mPopMenu.isShowing()) {
            Constant.IS_FIRST_SHARE = true;
            mPopMenu.show();
        }
    }

    public TextView creatTextTag(String content, int colorRes, Drawable drawable, TenHolder tenHolder) {
        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextSize(9);
        textView.setBackgroundDrawable(drawable);
        textView.setTextColor(colorRes);
        int padding = TransformUtil.dip2px(context, 3f);
        textView.setPadding(padding, 0, padding, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (tenHolder.mllayout_tag.getChildCount() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }


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

        TenHolder(View itemView) {
            super(itemView);
            mllayout_root= (MyLinearLayout) itemView.findViewById(R.id.mllayout_root);
            miv_photo= (MyImageView) itemView.findViewById(R.id.miv_photo);
            mtv_title= (MyTextView) itemView.findViewById(R.id.mtv_title);
            mllayout_tag= (MyLinearLayout) itemView.findViewById(R.id.mllayout_tag);
            mtv_price= (MyTextView) itemView.findViewById(R.id.mtv_price);
            int picWidth = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth / 2, picWidth / 2);
            miv_photo.setLayoutParams(params);
        }
    }

    class TwoHolder extends BaseRecyclerViewHolders {
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
            int picWidth = Common.getScreenWidth((Activity) context);
            int height = picWidth * 192 / 720;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, height);
            mllayout_nav.setLayoutParams(params);
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, picWidth*226 /360);
            params.setMargins(0,TransformUtil.dip2px(context, 10),0,0);
            mllayout_root.setLayoutParams(params);
            downTime_first.setDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onFinish() {
                    if (downTime_first != null && cateGoryFrag != null) {
                        downTime_first.cancelDownTimer();
                        cateGoryFrag.refresh();
                    }
                }

            });
        }
    }

    class FiveHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;
        @BindView(R.id.miv_share)
        MyImageView miv_share;
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
        }
    }

    class SevenHolder extends BaseRecyclerViewHolders {
        @BindView(R.id.rv_goods)
        RecyclerView rv_goods;
        private FirstHorizonAdapter firstHorizonAdapter;

        SevenHolder(View itemView) {
            super(itemView);
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
        }
    }

    class DefaultHolder extends BaseRecyclerViewHolders {

        DefaultHolder(View itemView) {
            super(itemView);
        }
    }
}
