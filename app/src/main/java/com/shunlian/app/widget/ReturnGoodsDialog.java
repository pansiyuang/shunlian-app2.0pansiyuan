package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.RefundInfoEntity;
import com.shunlian.app.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by Administrator on 2017/12/28.
 */

public class ReturnGoodsDialog extends Dialog {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.tv_close)
    TextView tv_close;

    private Context mContext;
    private int recycleHeight;
    private ISelectListener listener;
    private int currentPosition;
    private List<RefundInfoEntity.Reason> mData;

    public ReturnGoodsDialog(Context context, List<RefundInfoEntity.Reason> reasons) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        this.mData = reasons;
    }

    public ReturnGoodsDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_return_dialog, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
        setCanceledOnTouchOutside(false);

        //设置当前dialog宽高
        recycleHeight = getDeviceHeight(mContext) * 3 / 5;
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = recycleHeight;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recycler_list.setLayoutManager(linearLayoutManager);

        final SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<RefundInfoEntity.Reason>(mContext, R.layout.item_changeprefer, mData) {
            @Override
            public void convert(SimpleViewHolder holder, RefundInfoEntity.Reason reason, int position) {
                TextView tv_str = holder.getView(R.id.tv_prefer);
                tv_str.setText(reason.reason_info);
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
        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentPosition = position;
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelect(currentPosition);
                }
                dismiss();
            }
        });
    }

    public void setSelectListener(ISelectListener listener) {
        this.listener = listener;
    }


    public interface ISelectListener {
        void onSelect(int position);
    }
}
