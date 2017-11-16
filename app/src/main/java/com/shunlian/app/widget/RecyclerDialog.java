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
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by Administrator on 2017/11/10.
 */

public class RecyclerDialog extends Dialog {
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
    private ComboAdapter comboAdapter;
    private AttributeAdapter attributeAdapter;
    private int recycleHeight;

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
}
