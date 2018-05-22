package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.utils.TransformUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by Administrator on 2017/11/27.
 */

public class DiscountListDialog extends Dialog {

    private Context mContext;

    @BindView(R.id.recy_view)
    RecyclerView recycler_list;

    @BindView(R.id.mtv_close)
    MyTextView mtv_close;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private int recycleHeight;
    private int currentPosition = 0;
    private ISelectListener listener;
    private List<ConfirmOrderEntity.Voucher> mVouchers;
    private Unbinder bind;


    public DiscountListDialog(Context context) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.discount_list_dialog, null, false);
        setContentView(rootView);
        bind = ButterKnife.bind(this, rootView);
        initViews();
    }

    public DiscountListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    private void initViews() {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        recycleHeight = getDeviceHeight(mContext) * 1 / 2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recycleHeight);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recycler_list.setLayoutManager(linearLayoutManager);
        recycler_list.setLayoutParams(params);

        mtv_close.setOnClickListener(view -> {
            if (listener != null){
                listener.onSelect(currentPosition);
            }
            dismiss();
            if (bind != null){
                bind.unbind();
            }
        });

        miv_close.setOnClickListener(v -> {
            if (isShowing()){
                dismiss();
                if (bind != null){
                    bind.unbind();
                }
            }
        });
    }

    /**
     * 优惠券
     * @param enabled
     */
    public void setGoodsDiscount(ConfirmOrderEntity.Enabled  enabled){
        mVouchers = enabled.voucher;
        mtv_title.setText(mContext.getResources().getString(R.string.goods_voucher));
        currentPosition = enabled.selectVoucherId;
        ConfirmOrderEntity.Voucher voucher1 = mVouchers.get(mVouchers.size() - 1);
        if (!"".equals(voucher1.voucher_id)){
            ConfirmOrderEntity.Voucher voucher = new ConfirmOrderEntity.Voucher();
            voucher.title = mContext.getResources().getString(R.string.not_use_voucher);
            voucher.voucher_hint = mContext.getResources().getString(R.string.not_use_voucher);
            voucher.voucher_id = "";
            voucher.denomination = "0";
            mVouchers.add(voucher);
        }
        final SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<ConfirmOrderEntity.Voucher>(mContext,
                R.layout.item_changeprefer, mVouchers) {

            @Override
            public void convert(SimpleViewHolder holder, ConfirmOrderEntity.Voucher s, int position) {
                TextView tv_prefer = holder.getView(R.id.tv_prefer);
                tv_prefer.setText(s.voucher_hint);
                MyImageView miv_prefer_select = holder.getView(R.id.miv_prefer_select);
                holder.addOnClickListener(R.id.rl_item);
                if (currentPosition == position) {
                    miv_prefer_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
                } else {
                    miv_prefer_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
                }
            }
        };
        int space1 = TransformUtil.dip2px(mContext, 10);
        recycler_list.setPadding(space1,0,space1,0);
        recycler_list.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener((view, position) -> {
            currentPosition = position;
            recyclerAdapter.notifyDataSetChanged();
        });
    }

    /**
     * 促销
     * @param enabled
     */
    public void setPromotion(ConfirmOrderEntity.Enabled  enabled){
        mtv_title.setText(mContext.getResources().getString(R.string.store_discount));
        currentPosition = enabled.selectPromotionId;
        final SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<ConfirmOrderEntity.PromotionInfo>(mContext,
                R.layout.item_changeprefer, enabled.promotion_info) {

            @Override
            public void convert(SimpleViewHolder holder, ConfirmOrderEntity.PromotionInfo s, int position) {
                TextView tv_prefer = holder.getView(R.id.tv_prefer);
                tv_prefer.setText(s.prom_title);
                MyImageView miv_prefer_select = holder.getView(R.id.miv_prefer_select);
                holder.addOnClickListener(R.id.rl_item);
                if (currentPosition == position) {
                    miv_prefer_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
                } else {
                    miv_prefer_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
                }
            }
        };
        int space1 = TransformUtil.dip2px(mContext, 10);
        recycler_list.setPadding(space1,0,space1,0);
        recycler_list.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener((view, position) -> {
            currentPosition = position;
            recyclerAdapter.notifyDataSetChanged();
        });
    }

    /**
     * 选择原因
     */
    public void setSelectReason(){
        miv_close.setVisibility(View.VISIBLE);
        mtv_title.setGravity(Gravity.CENTER);
        mtv_title.setText("请选择原因");
        mtv_close.setTextColor(mContext.getResources().getColor(R.color.white));
        mtv_close.setBackgroundColor(mContext.getResources().getColor(R.color.pink_color));
        mtv_close.setText("提交");

        String[] select_reason = mContext.getResources().getStringArray(R.array.select_reason);
        List<String> list = Arrays.asList(select_reason);
        final SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<String>(mContext,
                R.layout.item_changeprefer, list) {

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {
                TextView tv_prefer = holder.getView(R.id.tv_prefer);
                tv_prefer.setText(s);
                MyImageView miv_prefer_select = holder.getView(R.id.miv_prefer_select);
                holder.addOnClickListener(R.id.rl_item);
                if (currentPosition == position) {
                    miv_prefer_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
                } else {
                    miv_prefer_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
                }
            }
        };

        recycler_list.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener((view, position) -> {
            currentPosition = position;
            recyclerAdapter.notifyDataSetChanged();
        });
    }

    public void setSelectListener(ISelectListener listener){
        this.listener = listener;
    }

    public interface ISelectListener{
        void onSelect(int position);
    }
}
