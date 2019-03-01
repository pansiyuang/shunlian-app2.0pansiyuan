package com.shunlian.app.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.HotsaleEntity;
import com.shunlian.app.bean.HotsaleHomeEntity;
import com.shunlian.app.bean.KoubeiSecondEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.PKoubei;
import com.shunlian.app.ui.more_credit.PhoneRecordAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.view.IKoubei;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.SaleProgressView;
import com.shunlian.app.widget.SaleProgressViews;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class KouBeiSecondAdapter extends BaseRecyclerAdapter<KoubeiSecondEntity.Content> implements IKoubei {
    private int divide = -1;
    private LottieAnimationView animation_view;
    private MyImageView miv_logo;
    private NewTextView ntv_desc, ntv_zan;
    private int position;
    private boolean isClick;
    private  PKoubei pKoubei;
    private ShareInfoParam mShareInfoParam = new ShareInfoParam();
    private ShareGoodDialogUtil shareGoodDialogUtil;

    public KouBeiSecondAdapter(Context context, boolean isShowFooter, List<KoubeiSecondEntity.Content> lists, int divide) {
        super(context, isShowFooter, lists);
        shareGoodDialogUtil = new ShareGoodDialogUtil(context);
        pKoubei = new PKoubei(context, this);
        this.divide = divide;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_koubei_second, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        try {
            KoubeiSecondEntity.Content ad = lists.get(position);
            if (ad==null)
                return;
            GlideUtils.getInstance().loadImageZheng(context, mHolder.miv_photo, ad.thumb);
            mHolder.ntv_title.setText(ad.title);
            mHolder.ntv_price.setText(getString(R.string.common_yuan) + ad.price);
            mHolder.ntv_earn.setText(ad.self_buy_earn);
            mHolder.ntv_desc.setText(ad.praise);
            mHolder.mrlayout_comment.setVisibility(View.GONE);
            mHolder.mrlayout_comments.setVisibility(View.GONE);
            mHolder.view_line.setVisibility(View.GONE);
            if (!isEmpty(ad.comments)) {
                mHolder.view_line.setVisibility(View.VISIBLE);
                if (ad.comments.get(0) != null) {
                    mHolder.mrlayout_comment.setVisibility(View.VISIBLE);
                    GlideUtils.getInstance().loadCircleAvar(context, mHolder.miv_icon, ad.comments.get(0).avatar);
                    mHolder.ntv_name.setText(ad.comments.get(0).nickname);
                    mHolder.ntv_comment.setText(ad.comments.get(0).content);
                }
                if (2 == ad.comments.size() && ad.comments.get(1) != null) {
                    mHolder.mrlayout_comments.setVisibility(View.VISIBLE);
                    GlideUtils.getInstance().loadCircleAvar(context, mHolder.miv_icons, ad.comments.get(1).avatar);
                    mHolder.ntv_names.setText(ad.comments.get(1).nickname);
                    mHolder.ntv_comments.setText(ad.comments.get(1).content);
                }
            }
            mHolder.miv_tag.setVisibility(View.VISIBLE);
            if (position <= divide) {
                mHolder.miv_tag.setImageResource(R.mipmap.img_koubei_tuijan);
            } else {
                switch (position - divide) {
                    case 1:
                        mHolder.miv_tag.setImageResource(R.mipmap.img_koubei_one);
                        break;
                    case 2:
                        mHolder.miv_tag.setImageResource(R.mipmap.img_koubei_two);
                        break;
                    case 3:
                        mHolder.miv_tag.setImageResource(R.mipmap.img_koubei_three);
                        break;
                    default:
                        mHolder.miv_tag.setVisibility(View.GONE);
                        break;
                }
            }
            if ("1".equals(ad.praise_flag)) {
                mHolder.miv_logo.setImageResource(R.mipmap.icon_yeslaughs);
            } else {
                mHolder.miv_logo.setImageResource(R.mipmap.icon_nolaugh);
            }
            mHolder.miv_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isClick)
                        zan(position,mHolder.ntv_desc,mHolder.animation_view,mHolder.miv_logo,ad.praise_flag,mHolder.ntv_zan,ad.id);
                }
            });
            mHolder.ntv_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isClick)
                        zan(position,mHolder.ntv_desc,mHolder.animation_view,mHolder.miv_logo,ad.praise_flag,mHolder.ntv_zan,ad.id);
                }
            });
            try {
                if (isEmpty(ad.star_rate)){
                    mHolder.progress_view_sale.setVisibility(View.GONE);
                }else {
                    mHolder.progress_view_sale.setVisibility(View.VISIBLE);
                    mHolder.progress_view_sale.setTotalAndCurrentCount(100,Integer.parseInt(ad.star_rate));
                }
            }catch (Exception e){
                mHolder.progress_view_sale.setVisibility(View.GONE);
            }
            mHolder.miv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mShareInfoParam.goods_id = ad.id;
                    mShareInfoParam.share_buy_earn = ad.share_buy_earn;
                    pKoubei.getShareInfo(pKoubei.goods,ad.id);
                }
            });
        }catch (Exception e){

        }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        mShareInfoParam.shareLink = baseEntity.data.shareLink;
        mShareInfoParam.img = baseEntity.data.img;
        mShareInfoParam.desc = baseEntity.data.desc;
        mShareInfoParam.price = baseEntity.data.price;
        mShareInfoParam.market_price = baseEntity.data.market_price;
        if(!TextUtils.isEmpty(baseEntity.data.share_buy_earn))
            mShareInfoParam.share_buy_earn = baseEntity.data.share_buy_earn;
        mShareInfoParam.voucher = baseEntity.data.voucher;
        mShareInfoParam.little_word = baseEntity.data.little_word;
        mShareInfoParam.time_text = baseEntity.data.time_text;
        mShareInfoParam.is_start = baseEntity.data.is_start;
        mShareInfoParam.title = baseEntity.data.title;
        shareGoodDialogUtil.shareGoodDialog(mShareInfoParam, true, false);
    }

    public void zan(int position, NewTextView ntv_desc, LottieAnimationView animation_view, MyImageView miv_logo, String praise_flag, NewTextView ntv_zan, String goods_id) {
        isClick=true;
        if ("1".equals(praise_flag)) {
            ntv_zan.setVisibility(View.VISIBLE);
            ntv_zan.setText("已赞过！");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ntv_zan.setVisibility(View.INVISIBLE);
                    isClick=false;
                }
            }, 2 * 1000);
        } else {
            this.animation_view = animation_view;
            this.miv_logo = miv_logo;
            this.ntv_desc = ntv_desc;
            this.ntv_zan = ntv_zan;
            this.position = position;
            pKoubei.getSetZan(goods_id);
        }
    }

    @Override
    public void setHomeData(HotsaleHomeEntity hotsaleHomeEntity) {

    }

    @Override
    public void setHotsaleCate(HotsaleEntity hotsaleEntity) {

    }

    @Override
    public void setHotsaleCates(KoubeiSecondEntity koubeiSecondEntity) {

    }


    @Override
    public void getZan(CommonEntity commonEntity) {
        ntv_zan.setVisibility(View.VISIBLE);
        ntv_zan.setText("  +1");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ntv_zan.setVisibility(View.INVISIBLE);
                isClick=false;
            }
        }, 2 * 1000);

        ntv_desc.setText(commonEntity.praise);

        lists.get(position).praise = commonEntity.praise;
        lists.get(position).praise_flag = "1";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                animation_view.setVisibility(View.VISIBLE);
                animation_view.setAnimation("icon_xiaoface_json.json");//在assets目录下的动画json文件名。
                animation_view.loop(false);//设置动画循环播放
                animation_view.setImageAssetsFolder("images/");//assets目录下的子目录，存放动画所需的图片
                animation_view.playAnimation();//播放动画
            } else {
                miv_logo.setVisibility(View.VISIBLE);
                animation_view.setVisibility(View.INVISIBLE);
                miv_logo.setImageResource(R.mipmap.icon_yeslaughs);
            }
        } catch (Exception e) {
            Log.w("splash", "splash----crush");
        }
    }

    @Override
    public void showFailureView(int request_code) {
        isClick=false;
    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mrlayout_bottom)
        MyRelativeLayout mrlayout_bottom;

        @BindView(R.id.progress_view_sale)
        SaleProgressView progress_view_sale;

        @BindView(R.id.ntv_zan)
        NewTextView ntv_zan;

        @BindView(R.id.animation_view)
        LottieAnimationView animation_view;

        @BindView(R.id.miv_logo)
        MyImageView miv_logo;

        @BindView(R.id.miv_tag)
        MyImageView miv_tag;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_icons)
        MyImageView miv_icons;

        @BindView(R.id.miv_share)
        MyImageView miv_share;

        @BindView(R.id.ntv_title)
        NewTextView ntv_title;

        @BindView(R.id.ntv_names)
        NewTextView ntv_names;

        @BindView(R.id.ntv_desc)
        NewTextView ntv_desc;

        @BindView(R.id.ntv_comments)
        NewTextView ntv_comments;

        @BindView(R.id.ntv_name)
        NewTextView ntv_name;

        @BindView(R.id.ntv_comment)
        NewTextView ntv_comment;

        @BindView(R.id.ntv_price)
        NewTextView ntv_price;

        @BindView(R.id.ntv_earn)
        NewTextView ntv_earn;

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.mrlayout_comment)
        MyRelativeLayout mrlayout_comment;

        @BindView(R.id.mrlayout_comments)
        MyRelativeLayout mrlayout_comments;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
            mrlayout_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }
}
