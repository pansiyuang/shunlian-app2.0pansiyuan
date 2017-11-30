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
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;

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

    public void setGoodsDiscount(){
        mtv_title.setText("商品优惠券");
        int space = TransformUtil.dip2px(mContext, 0.5f);
        recycler_list.addItemDecoration(new VerticalItemDecoration(space,0,0,
                mContext.getResources().getColor(R.color.bg_gray_two)));
        SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, DataUtil.getListString(5,"ff")) {

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {
                TextView view = holder.getView(android.R.id.text1);
                view.setText(s);
            }
        };
        int space1 = TransformUtil.dip2px(mContext, 10);
        recycler_list.setPadding(space1,0,space1,0);
        recycler_list.setAdapter(recyclerAdapter);
    }
}
