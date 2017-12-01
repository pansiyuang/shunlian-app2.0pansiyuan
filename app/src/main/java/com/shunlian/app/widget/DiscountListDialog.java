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
import com.shunlian.app.listener.OnItemClickListener;
import com.shunlian.app.utils.TransformUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private int recycleHeight;
    private int currentPosition = 0;


    public DiscountListDialog(Context context) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.discount_list_dialog, null, false);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
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

        mtv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
    }

    public void setGoodsDiscount(List<ConfirmOrderEntity.Voucher> vouchers){
        mtv_title.setText("商品优惠券");
        ConfirmOrderEntity.Voucher voucher = new ConfirmOrderEntity.Voucher();
        voucher.title = "不使用优惠券";
        voucher.voucher_id = "no_0";
        vouchers.add(voucher);
        final SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<ConfirmOrderEntity.Voucher>(mContext,
                R.layout.item_changeprefer, vouchers) {

            @Override
            public void convert(SimpleViewHolder holder, ConfirmOrderEntity.Voucher s, int position) {
                TextView tv_prefer = holder.getView(R.id.tv_prefer);
                tv_prefer.setText(s.title);
                MyImageView miv_prefer_select = holder.getView(R.id.miv_prefer_select);
                holder.addOnClickListener(R.id.miv_prefer_select);
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

        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentPosition = position;
                recyclerAdapter.notifyDataSetChanged();
            }
        });
    }
}
