package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ComboAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/10.
 */

public class RecyclerDialog extends Dialog {
    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.dialog_title)
    TextView dialog_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private Context mContext;
    private List<GoodsDeatilEntity.Combo> mCombos;
    private ComboAdapter comboAdapter;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recycler_list.setLayoutManager(linearLayoutManager);
    }

    public void setCombos(List<GoodsDeatilEntity.Combo> combos) {
        this.mCombos = combos;
        if (comboAdapter == null) {
            comboAdapter = new ComboAdapter(mContext, false, mCombos);
        } else {
            comboAdapter.setData(mCombos);
        }
        recycler_list.setAdapter(comboAdapter);
    }
}
