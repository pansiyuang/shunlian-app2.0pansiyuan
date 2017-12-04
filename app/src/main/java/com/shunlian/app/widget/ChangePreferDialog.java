package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ChangePreferAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShoppingCarEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ChangePreferDialog extends Dialog {
    @BindView(R.id.recycler_changeprefer_list)
    RecyclerView recycler_changeprefer_list;

    @BindView(R.id.tv_close)
    TextView tv_close;

    private int recycleHeight;
    private Context mContext;
    private ChangePreferAdapter changePreferAdapter;
    private List<GoodsDeatilEntity.AllProm> proms;
    private GoodsDeatilEntity.AllProm currentProm;
    private OnPreferSelectCallBack mCallBack;

    public ChangePreferDialog(Context context, List<GoodsDeatilEntity.AllProm> promList) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        this.proms = promList;
    }

    public ChangePreferDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_changeprefer, null, false);
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
        recycler_changeprefer_list.setLayoutManager(linearLayoutManager);
        changePreferAdapter = new ChangePreferAdapter(mContext, false, proms);
        changePreferAdapter.setOnItemPreferSelectListener(new ChangePreferAdapter.OnItemPreferSelectListener() {
            @Override
            public void OnPreferSelect(GoodsDeatilEntity.AllProm prom) {
                currentProm = prom;
            }
        });
        recycler_changeprefer_list.setAdapter(changePreferAdapter);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (currentProm != null && mCallBack != null) {
                    mCallBack.onSelect(currentProm);
                }
            }
        });
    }

    public void setOnPreferSelectCallBack(OnPreferSelectCallBack callBack) {
        mCallBack = callBack;
    }

    public interface OnPreferSelectCallBack {
        void onSelect(GoodsDeatilEntity.AllProm allProm);
    }
}
