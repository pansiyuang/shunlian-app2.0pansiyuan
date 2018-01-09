package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.RefundDetailEntity;
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

    public ReturnGoodsDialog(Context context) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        initViews();
    }

    public ReturnGoodsDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void initViews() {
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

    public void setRefundReason(List<RefundDetailEntity.RefundDetail.Edit.Reason> reasonList, String chooseId) {
        if (!TextUtils.isEmpty(chooseId)) {
            for (int i = 0; i < reasonList.size(); i++) {
                if (chooseId.equals(reasonList.get(i).reason_id)) {
                    currentPosition = i;
                    break;
                }
            }
        }

        final SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<RefundDetailEntity.RefundDetail.Edit.Reason>(mContext, R.layout.item_changeprefer, reasonList) {
            @Override
            public void convert(SimpleViewHolder holder, RefundDetailEntity.RefundDetail.Edit.Reason reason, int position) {
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
    }

    public void setGoodsStatus(List<RefundDetailEntity.RefundDetail.Edit.Reason> userStatusList, String statusId) {
        if (!TextUtils.isEmpty(statusId)) {
            for (int i = 0; i < userStatusList.size(); i++) {
                if (statusId.equals(userStatusList.get(i).reason_id)) {
                    currentPosition = i;
                    break;
                }
            }
        }

        final SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter<RefundDetailEntity.RefundDetail.Edit.Reason>(mContext, R.layout.item_changeprefer, userStatusList) {
            @Override
            public void convert(SimpleViewHolder holder, RefundDetailEntity.RefundDetail.Edit.Reason reason, int position) {
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
    }

    public void setDialogTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
    }


    public void setSelectListener(ISelectListener listener) {
        this.listener = listener;
    }


    public interface ISelectListener {
        void onSelect(int position);
    }
}
