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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AttributeAdapter;
import com.shunlian.app.adapter.ComboAdapter;
import com.shunlian.app.adapter.VoucherAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by Administrator on 2017/11/10.
 */

public class RecyclerDialog extends Dialog implements VoucherAdapter.OnVoucherSelectCallBack {
    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.layout_title)
    RelativeLayout layout_title;

    @BindView(R.id.dialog_title)
    TextView dialog_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private Context mContext;
    private List<GoodsDeatilEntity.Combo> mCombos;
    private List<GoodsDeatilEntity.Attrs> mAttributes;
    private List<GoodsDeatilEntity.Voucher> mVourcheres;
    private ComboAdapter comboAdapter;
    private AttributeAdapter attributeAdapter;
    private VoucherAdapter voucherAdapter;
    private int recycleHeight;
    private OnVoucherCallBack mCallBack;

    public RecyclerDialog(Context context) {
        this(context, R.style.MyDialogStyleBottom);
        this.mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_list, null, false);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        initViews();
    }

    public RecyclerDialog(Context context, int themeResId) {
        super(context, themeResId);
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

        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
    }

    public void setCombos(List<GoodsDeatilEntity.Combo> combos) {
        this.mCombos = combos;
        if (comboAdapter == null) {
            comboAdapter = new ComboAdapter(mContext, false, mCombos);
        } else {
            comboAdapter.setData(mCombos);
        }
        dialog_title.setText(mContext.getResources().getText(R.string.select_combo));
        recycler_list.setAdapter(comboAdapter);

        layout_title.setBackgroundColor(mContext.getResources().getColor(R.color.white_ash));
    }

    public void setAttributes(List<GoodsDeatilEntity.Attrs> attributes) {
        this.mAttributes = attributes;
        if (attributeAdapter == null) {
            attributeAdapter = new AttributeAdapter(mContext, false, mAttributes);
        } else {
            attributeAdapter.setData(mAttributes);
        }
        dialog_title.setText(mContext.getResources().getText(R.string.goods_attribute));
        recycler_list.setAdapter(attributeAdapter);

        layout_title.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }

    public void setVoucheres(List<GoodsDeatilEntity.Voucher> voucheres) {
        this.mVourcheres = voucheres;
        if (voucherAdapter == null) {
            voucherAdapter = new VoucherAdapter(mContext, false, mVourcheres);
        } else {
            voucherAdapter.setData(mVourcheres);
        }
        dialog_title.setText(mContext.getResources().getText(R.string.select_voucher));
        recycler_list.setAdapter(voucherAdapter);
        voucherAdapter.setOnVoucherSelectCallBack(this);
        layout_title.setBackgroundColor(mContext.getResources().getColor(R.color.white_ash));
    }

    @Override
    public void OnVoucherSelect(GoodsDeatilEntity.Voucher voucher) {
        if (mCallBack != null) {
            mCallBack.OnVoucherSelect(voucher);
        }
    }

    public void setOnVoucherCallBack(OnVoucherCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnVoucherCallBack {
        void OnVoucherSelect(GoodsDeatilEntity.Voucher voucher);
    }
}
