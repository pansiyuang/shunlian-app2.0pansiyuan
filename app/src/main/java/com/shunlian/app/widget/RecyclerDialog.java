package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.shunlian.app.adapter.ActivityMoreAdapter;
import com.shunlian.app.adapter.AttributeAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.ComboAdapter;
import com.shunlian.app.adapter.PromotionAdapter;
import com.shunlian.app.adapter.VoucherAdapter;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.goods_detail.ComboDetailAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceHeight;

/**
 * Created by Administrator on 2017/11/10.
 */

public class RecyclerDialog extends Dialog{
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
    private ActivityMoreAdapter moreAdapter;
    private int fullCut = 0;
    private int fullDiscount = 0;
    private int buyGift = 0;

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

    public void setCombos(List<GoodsDeatilEntity.Combo> combos, final String goods_id) {
        this.mCombos = combos;
        if (comboAdapter == null) {
            comboAdapter = new ComboAdapter(mContext, false, mCombos);
        } else {
            comboAdapter.setData(mCombos);
        }
        dialog_title.setText(mContext.getResources().getText(R.string.select_combo));
        recycler_list.setAdapter(comboAdapter);

        layout_title.setBackgroundColor(mContext.getResources().getColor(R.color.white_ash));
        comboAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GoodsDeatilEntity.Combo combo = mCombos.get(position);
                ComboDetailAct.startAct(mContext, combo.combo_id, goods_id);
                if (isShowing()) {
                    dismiss();
                }
            }
        });
    }

    public void setAttributes(List<GoodsDeatilEntity.Attrs> attributes) {
        attributeAdapter = new AttributeAdapter(mContext, false, mAttributes);
        this.mAttributes = attributes;
        if (attributeAdapter == null) {
        } else {
            attributeAdapter.setData(mAttributes);
        }
        dialog_title.setText(mContext.getResources().getText(R.string.goods_attribute));
        recycler_list.setAdapter(attributeAdapter);

        layout_title.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }

    public void setVoucheres(final List<GoodsDeatilEntity.Voucher> voucheres) {
        this.mVourcheres = voucheres;
        if (voucherAdapter == null) {
            voucherAdapter = new VoucherAdapter(mContext, false, mVourcheres);
        } else {
            voucherAdapter.setData(mVourcheres);
        }
        dialog_title.setText(mContext.getResources().getText(R.string.select_voucher));
        recycler_list.setAdapter(voucherAdapter);
        layout_title.setBackgroundColor(mContext.getResources().getColor(R.color.white_ash));
        voucherAdapter.setOnItemClickListener((v, p) -> {
            GoodsDeatilEntity.Voucher voucher = voucheres.get(p);
            if (mCallBack != null) {
                LogUtil.zhLogW("=setVoucheres============="+voucher.voucher_id);
                mCallBack.onVoucherSelect(voucher);
                mCallBack.itemVoucher(voucher,p);
            }
        });
    }

    public void setActivity(List<GoodsDeatilEntity.ActivityDetail> full_cut,
                            List<GoodsDeatilEntity.ActivityDetail> full_discount,
                            List<GoodsDeatilEntity.ActivityDetail> buy_gift, String store_id) {
        layout_title.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        if (full_cut != null && full_cut.size() > 0) {
            fullCut = full_cut.size();
        }
        if (full_discount != null && full_discount.size() > 0) {
            fullDiscount = full_discount.size();
        }
        if (buy_gift != null && buy_gift.size() > 0) {
            buyGift = buy_gift.size();
        }
        List<GoodsDeatilEntity.ActivityDetail> allActs = new ArrayList<>();
        allActs.addAll(full_cut);
        allActs.addAll(full_discount);
        allActs.addAll(buy_gift);
        if (moreAdapter == null) {
            moreAdapter = new ActivityMoreAdapter(mContext, false, allActs, fullCut, fullDiscount, buyGift);
            int px = TransformUtil.dip2px(mContext, 20);
            recycler_list.addItemDecoration(new VerticalItemDecoration(px, 0, 0));
        } else {

        }
        dialog_title.setText(mContext.getResources().getText(R.string.shop_promotion_activity));
        recycler_list.setAdapter(moreAdapter);
        moreAdapter.setOnItemClickListener((view,position)-> {
            /*if (position < fullCut) {
                //满减
            } else if (position < fullCut + fullDiscount) {
                //打折
            } else {
                //买赠
            }*/
            Intent intent = new Intent(mContext, StoreAct.class);
            intent.putExtra("storeId",store_id);
            intent.putExtra("discount",true);
            mContext.startActivity(intent);
        });
    }

    /**
     * 促销详情
     *
     * @param infos
     */
    public void setPromotionDetail(List<ConfirmOrderEntity.PromotionInfo> infos) {
        layout_title.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        dialog_title.setText(mContext.getResources().getString(R.string.promotional_details));
        PromotionAdapter adapter = new PromotionAdapter(mContext, false, infos);
        recycler_list.setAdapter(adapter);
    }

    public void setOnVoucherCallBack(OnVoucherCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnVoucherCallBack {
        default void onVoucherSelect(GoodsDeatilEntity.Voucher voucher){}
        default void itemVoucher(GoodsDeatilEntity.Voucher voucher,int position){}
    }

    public void getVoucherSuccess(String voucherId) {
        if (voucherAdapter != null) {
            voucherAdapter.getItemSuccess(voucherId);
        }
    }

    public void getVoucherSuccess(GoodsDeatilEntity.Voucher voucher,int position) {
        if (voucherAdapter != null) {
            voucherAdapter.getItemSuccess(voucher,position);
        }
    }
}
