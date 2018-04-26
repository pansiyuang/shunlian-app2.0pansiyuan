package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.newchat.adapter.ChatOrderAdapter;
import com.shunlian.app.presenter.ChatOrderPresenter;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IChatOrderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by Administrator on 2018/4/14.
 */

public class ChatOrderDialog extends Dialog implements BaseRecyclerAdapter.OnItemClickListener, IChatOrderView {
    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.layout_title)
    RelativeLayout layout_title;

    @BindView(R.id.dialog_title)
    TextView dialog_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.view_line)
    View view_line;

    private int recycleHeight;
    private OnOrderSendListener mOrderListener;
    private ChatOrderAdapter chatOrderAdapter;
    private List<MyOrderEntity.Orders> mOrders;
    private ChatOrderPresenter mPresenter;
    private LinearLayoutManager linearLayoutManager;

    public ChatOrderDialog(Context context) {
        this(context, R.style.MyDialogStyleBottom);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_list, null, false);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        initViews();
    }

    public ChatOrderDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void initViews() {
        //设置当前dialog宽高
        recycleHeight = getDeviceHeight(getContext()) * 3 / 5;
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = recycleHeight;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        layout_title.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        dialog_title.setText(getContext().getResources().getString(R.string.order_select));
        view_line.setVisibility(View.VISIBLE);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setLayoutManager(linearLayoutManager);
        recycler_list.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(getContext(), 0.5f), 0, 0, getContext().getResources().getColor(R.color.bg_gray_two)));

        miv_close.setOnClickListener(v -> {
            if (isShowing()) {
                dismiss();
            }
        });

        mOrders = new ArrayList<>();
        mPresenter = new ChatOrderPresenter(getContext(), this);
        mPresenter.getOrderList(true);

        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    public void setOnOrderListener(OnOrderSendListener listener) {
        this.mOrderListener = listener;
    }

    @Override
    public void onItemClick(View view, int position) {
        MyOrderEntity.Orders orders = mOrders.get(position);
        if (mOrderListener != null) {
            mOrderListener.OnOrderSelect(orders);
        }
    }

    @Override
    public void getOrderList(List<MyOrderEntity.Orders> ordersList, int currentPage, int totalPage) {
        if (currentPage == 1) {
            mOrders.clear();
        }
        if (ordersList == null || ordersList.size() == 0) {
            return;
        }
        mOrders.addAll(ordersList);

        if (chatOrderAdapter == null) {
            chatOrderAdapter = new ChatOrderAdapter(getContext(), mOrders);
            chatOrderAdapter.setOnItemClickListener(this);
            recycler_list.setAdapter(chatOrderAdapter);
        } else {
            chatOrderAdapter.setData(mOrders);
        }
        chatOrderAdapter.notifyItemChanged(0, mOrders.size());
    }

    @Override
    public void refreshFinish() {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public interface OnOrderSendListener {
        void OnOrderSelect(MyOrderEntity.Orders orders);
    }
}
