package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/2.
 */

public class QrcodeStoreAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    public static final int HEAD_VIEW = 10002;
    public static final int FOOT_VIEW = 10003;
    private String mName;
    private String mAvatar;
    private String mLevel;
    private String mRole;
    private String qrcodeUrl;

    public QrcodeStoreAdapter(Context context, List<GoodsDeatilEntity.Goods> lists, String nickName, String avatar, String level, String memberRole, String url) {
        super(context, false, lists);
        this.mName = nickName;
        this.mAvatar = avatar;
        this.mLevel = level;
        this.mRole = memberRole;
        this.qrcodeUrl = url;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_VIEW) {
            return new HeadHolderView(LayoutInflater.from(context).inflate(R.layout.head_qrcode_store, parent, false));
        } else if (viewType == FOOT_VIEW) {
            return new FootHolderView(LayoutInflater.from(context).inflate(R.layout.bottom_qrcode_store, parent, false));
        } else {
            return new GoodsHolderView(LayoutInflater.from(context).inflate(R.layout.item_qrcode_goods, parent, false));
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemType = getItemViewType(position);
                    if (itemType == FOOT_VIEW || itemType == HEAD_VIEW) {
                        return ((GridLayoutManager) manager).getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_VIEW;
        } else if (position == getItemCount() - 1) {
            return FOOT_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 2;
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == FOOT_VIEW) {
            handFoot(holder);
        } else if (getItemViewType(position) == HEAD_VIEW) {
            handHead(holder);
        } else {
            handItem(holder, position);
        }
    }

    public void handHead(RecyclerView.ViewHolder holder) {
        if (holder instanceof HeadHolderView) {
            HeadHolderView headHolderView = (HeadHolderView) holder;
            headHolderView.tv_store_name.setText(mName);
            GlideUtils.getInstance().loadCircleImage(context, headHolderView.miv_circle_icon, mAvatar);
            if (!isEmpty(mLevel)) {
                Bitmap bitmap = TransformUtil.convertNewVIP(context, mLevel);
                headHolderView.miv_level.setImageBitmap(bitmap);
                headHolderView.miv_level.setVisibility(View.VISIBLE);
            } else {
                headHolderView.miv_level.setVisibility(View.GONE);
            }

            setMivHonour(mRole, headHolderView.miv_honour);
        }
    }

    public void handFoot(RecyclerView.ViewHolder holder) {
        if (holder instanceof FootHolderView) {
            FootHolderView footHolderView = (FootHolderView) holder;
            if (!isEmpty(qrcodeUrl)) {
                GlideUtils.getInstance().loadImage(context, footHolderView.miv_qrcode, qrcodeUrl);
            }
        }
    }

    public void handItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsHolderView) {
            GoodsHolderView goodsHolderView = (GoodsHolderView) holder;
            GoodsDeatilEntity.Goods goods = lists.get(position - 1);

            int width = TransformUtil.dip2px(context, (DeviceInfoUtil.getDeviceWidth(context) - 72) / 2);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, TransformUtil.dip2px(context, 168.5f));
            goodsHolderView.miv_icon.setLayoutParams(layoutParams);

            int space = TransformUtil.dip2px(context, 12);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (position % 2 == 1) {
                params.setMargins(space, space, space, 0);
            } else {
                params.setMargins(0, space, space, 0);
            }
            goodsHolderView.rl_rootView.setLayoutParams(params);

            GlideUtils.getInstance().loadImage(context, goodsHolderView.miv_icon, goods.thumb);
            goodsHolderView.tv_goods_title.setText(goods.title);
            if (!isEmpty(goods.price)) {
                goodsHolderView.tv_price.setText(getString(R.string.common_yuan) + " " + goods.price);
            }
        }
    }

    public class GoodsHolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;

        @BindView(R.id.rl_rootView)
        RelativeLayout rl_rootView;

        @BindView(R.id.tv_price)
        TextView tv_price;

        public GoodsHolderView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition() - 1);
            }
        }
    }

    public class HeadHolderView extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_circle_icon)
        MyImageView miv_circle_icon;

        @BindView(R.id.miv_level)
        MyImageView miv_level;

        @BindView(R.id.miv_honour)
        MyImageView miv_honour;

        @BindView(R.id.tv_store_name)
        TextView tv_store_name;

        public HeadHolderView(View itemView) {
            super(itemView);
        }
    }

    public class FootHolderView extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_qrcode)
        MyImageView miv_qrcode;

        public FootHolderView(View itemView) {
            super(itemView);
        }
    }

    public void setMivHonour(String type, MyImageView miv_honour) {
        switch (type) {
            case "1":
                miv_honour.setImageResource(R.mipmap.img_chuangkejingying);
                miv_honour.setVisibility(View.VISIBLE);
                break;
            case "2":
                miv_honour.setImageResource(R.mipmap.img_jingyingdaoshi);
                miv_honour.setVisibility(View.VISIBLE);
                break;
        }
    }
}
