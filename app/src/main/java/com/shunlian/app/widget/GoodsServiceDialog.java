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
import com.shunlian.app.adapter.GoodsServiceAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by zhanghe on 2018/12/11.
 */

public class GoodsServiceDialog extends Dialog {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.layout_title)
    RelativeLayout layout_title;

    @BindView(R.id.dialog_title)
    TextView dialog_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private final Context mContext;
    private int recycleHeight;
    private GoodsServiceAdapter serviceAdapter;


    public GoodsServiceDialog(@NonNull Context context) {
        this(context, R.style.MyDialogStyleBottom);
    }

    public GoodsServiceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_list, null, false);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        initViews();
    }

    private void initViews() {
        //设置当前dialog宽高
        recycleHeight = getDeviceHeight(mContext) * 3 / 5;
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = recycleHeight;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setLayoutManager(linearLayoutManager);

        miv_close.setOnClickListener(view -> {
            if (isShowing()) {
                dismiss();
            }
        });
    }


    public void setServiceContent(ArrayList<GoodsDeatilEntity.SimpTitle> titles){
        dialog_title.setText("服务");
        if (serviceAdapter == null)
            serviceAdapter = new GoodsServiceAdapter(mContext,titles);
        recycler_list.setAdapter(serviceAdapter);
    }

}
