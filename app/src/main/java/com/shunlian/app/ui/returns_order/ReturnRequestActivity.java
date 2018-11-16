package com.shunlian.app.ui.returns_order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.presenter.ReturnRequestPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.order.ExchangeDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IReturnRequestView;
import com.shunlian.app.widget.CustomerGoodsView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ReturnGoodsDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.shunlian.app.adapter.SingleImgAdapter.REQUEST_CAMERA_CODE;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ReturnRequestActivity extends BaseActivity implements CustomerGoodsView.IChangeCountListener, ReturnGoodsDialog.ISelectListener, View.OnClickListener, IReturnRequestView, MessageCountManager.OnGetMessageListener {

    @BindView(R.id.customer_goods)
    CustomerGoodsView customer_goods;

    @BindView(R.id.rl_return_reason)
    RelativeLayout rl_return_reason;

    @BindView(R.id.edt_return_money)
    MyEditText edt_return_money;

    @BindView(R.id.tv_freight)
    TextView tv_freight;

    @BindView(R.id.rl_return_money)
    RelativeLayout rl_return_money;

    @BindView(R.id.tv_return_reason)
    TextView tv_return_reason;

    @BindView(R.id.tv_left)
    TextView tv_left;

    @BindView(R.id.view_money)
    View view_money;

    @BindView(R.id.edt_refunds)
    EditText edt_refunds;

    @BindView(R.id.grid_imgs)
    GridView grid_imgs;

    @BindView(R.id.tv_request_complete)
    TextView tv_request_complete;

    @BindView(R.id.tv_request)
    TextView tv_request;

    @BindView(R.id.tv_return_type)
    TextView tv_return_type;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mtv_toolbar_msgCount)
    MyTextView mtv_toolbar_msgCount;

    /**
     * 输入框小数的位数
     */
    private RefundDetailEntity.RefundDetail.Edit currentInfoEntity;
    private List<ImageEntity> imageEntityList = new ArrayList<>();
    private List<ImageEntity> upLoadList = new ArrayList<>();
    private SingleImgAdapter singleImgAdapter;
    private String currentServiceType;  //1 仅退款、 3 退货退款 4 换货
    private int goodsCount;//要退的商品数
    private int maxGoodsCount;//最大可退商品数
    private float freightPrice;
    private int isLast;  //是否改订单最后一件没有退的商品  1是  0 否
    private ReturnGoodsDialog goodsDialog;
    private String currentReasonId;
    private String currentRefundId;
    private ReturnRequestPresenter presenter;
    private boolean isEdit; //是否是编辑
    private int index;
    private float totalPrice;
    private List<RefundDetailEntity.RefundDetail.Edit.Reason> currentReasonList;
    private MessageCountManager messageCountManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_request;
    }

    public static void startAct(Context context, RefundDetailEntity.RefundDetail.Edit infoEntity, boolean isEdit, String refundId) {
        Intent intent = new Intent(context, ReturnRequestActivity.class);
        intent.putExtra("infoEntity", infoEntity);
        intent.putExtra("isEdit", isEdit);
        intent.putExtra("refundId", refundId);
        context.startActivity(intent);
    }


    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);

        currentReasonList = new ArrayList<>();

        currentInfoEntity = (RefundDetailEntity.RefundDetail.Edit) getIntent().getSerializableExtra("infoEntity");
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        currentRefundId = getIntent().getStringExtra("refundId");
        //第一次退 默认退商品最大数量
        goodsCount = Integer.valueOf(currentInfoEntity.qty);
        maxGoodsCount = Integer.valueOf(currentInfoEntity.qty);
        freightPrice = Float.valueOf(currentInfoEntity.shipping_fee);
        isLast = Integer.valueOf(currentInfoEntity.is_last);

        if (!isEmpty(currentInfoEntity.serviceType)) {
            currentServiceType = currentInfoEntity.serviceType;
        }

        if (isEdit) {
            currentReasonId = currentInfoEntity.reason_id;
            currentServiceType = currentInfoEntity.refund_type;
            currentReasonId = currentInfoEntity.reason_id;
            goodsCount = Integer.valueOf(currentInfoEntity.goods_num);
        }

        initViews(currentServiceType);
        presenter = new ReturnRequestPresenter(this, this);
    }

    @Override
    protected void onResume() {
        if (messageCountManager.isLoad()) {
            String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
            if (quick_actions != null)
                quick_actions.setMessageCount(s);
        } else {
            messageCountManager.initData();
        }
        super.onResume();
    }

    @OnClick(R.id.mrlayout_toolbar_more)
    public void more() {
        visible(quick_actions);
        quick_actions.afterSale();
    }

    public void initViews(String type) {
        goodsDialog = new ReturnGoodsDialog(this);
        goodsDialog.setSelectListener(this);
        switch (type) {
            case "1": //仅退款
                mtv_toolbar_title.setText(getStringResouce(R.string.return_request));
                customer_goods.setLabelName(getStringResouce(R.string.return_money_goods), false);
                rl_return_money.setVisibility(View.VISIBLE);
                view_money.setVisibility(View.VISIBLE);

                goodsDialog.setDialogTitle(getStringResouce(R.string.return_order));
                setMaxPrice(goodsCount);
                break;
            case "3": //退货退款
                mtv_toolbar_title.setText(getStringResouce(R.string.return_goods_and_money));
                customer_goods.setLabelName(getStringResouce(R.string.return_goods), false);
                tv_left.setText(getStringResouce(R.string.return_goods_money));
                tv_return_type.setText(getStringResouce(R.string.return_goods_reason));
                tv_request.setText(getStringResouce(R.string.refunds_goods));
                rl_return_money.setVisibility(View.VISIBLE);
                view_money.setVisibility(View.VISIBLE);
                goodsDialog.setDialogTitle(getStringResouce(R.string.return_goods_reason));

                setMaxPrice(goodsCount);
                break;
            case "4": //换货
                mtv_toolbar_title.setText(getStringResouce(R.string.change_request));
                customer_goods.setLabelName(getStringResouce(R.string.change_goods), false);
                rl_return_money.setVisibility(View.GONE);
                tv_request.setText(getStringResouce(R.string.return_instruction));
                tv_return_type.setText(getStringResouce(R.string.return_reason));
                goodsDialog.setDialogTitle(getStringResouce(R.string.return_reason));
                break;
        }

        if (isEdit) {
            edt_refunds.setText(currentInfoEntity.refund_remark_seller);
            edt_refunds.setSelection(edt_refunds.getText().length());
            List<ImageEntity> list = getImageEntityList();
            if (list != null && list.size() != 0) {
                imageEntityList.addAll(list);
            }
            tv_return_reason.setText(currentInfoEntity.buyer_message);
            tv_return_reason.setTextColor(getColorResouce(R.color.new_text));
            edt_return_money.setText(currentInfoEntity.refund_amount);
            edt_return_money.setFocusable(false);
        }

        GlideUtils.getInstance().loadOverrideImage(this,
                customer_goods.getGoodsIcon(), currentInfoEntity.thumb, 160, 160);
        customer_goods.setGoodsTitle(currentInfoEntity.title).selectCount(Integer.valueOf(currentInfoEntity.qty))
                .setEdittextGoodsCount(goodsCount)
                .setGoodsParams(currentInfoEntity.sku_desc)
                .setGoodsPrice(getStringResouce(R.string.common_yuan) + currentInfoEntity.price);

        singleImgAdapter = new SingleImgAdapter(this, imageEntityList);
        grid_imgs.setAdapter(singleImgAdapter);
    }

    @Override
    protected void initListener() {
        customer_goods.setIChangeCountListener(this);
        rl_return_reason.setOnClickListener(this);
        tv_request_complete.setOnClickListener(this);

        edt_return_money.setOnTouchListener((v, event) -> {
            setEdittextFocusable(true, edt_return_money);
            return false;
        });

        edt_return_money.addTextChangedListener(new TextWatcher() {
            String rel = "(\\d*)(\\.\\d{0,2})?";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Pattern.matches(rel, s)) {
                    s = s.subSequence(0, s.toString().indexOf(".") + 3);
                    edt_return_money.setText(s);
                    edt_return_money.setSelection(s.length());
                }
                if (s.toString().startsWith(".")) {
                    s = "0" + s;
                    edt_return_money.setText(s);
                    edt_return_money.setSelection(s.length());
                }
                /*if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        edt_return_money.setText(s);
                        edt_return_money.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edt_return_money.setText(s);
                    edt_return_money.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edt_return_money.setText(s.subSequence(0, 1));
                        edt_return_money.setSelection(1);
                        return;
                    }
                }*/
                if (!isEmpty(s.toString())) {
                    LogUtil.httpLogW("totalPrice:" + totalPrice);
                    if (Float.valueOf(s.toString()) > totalPrice) {
                        edt_return_money.setText(totalPrice + "");
                        edt_return_money.setSelection((totalPrice + "").length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        super.initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imagePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            upLoadList.clear();
            index = 0;
            compressImgs(index, imagePaths);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void compressImgs(int i, final List<String> list) {
        Luban.with(this).load(list.get(i)).putGear(3).setCompressListener(new OnCompressListener() {

            @Override
            public void onStart() {
                LogUtil.httpLogW("onStart()");
            }

            @Override
            public void onSuccess(File file) {
                LogUtil.httpLogW("压缩后大小:" + file.length());
                ImageEntity imageEntity = new ImageEntity(list.get(index));
                imageEntity.file = file;
                upLoadList.add(imageEntity);
                index++;
                if (index >= list.size()) {
                    presenter.uploadPic(upLoadList, "refund");
                } else {
                    compressImgs(index, list);
                }
            }

            @Override
            public void onError(Throwable e) {
                Common.staticToast("上传图片失败");
            }
        }).launch();
    }

    @Override
    public void onChangeCount(int count) {
        goodsCount = count;
        setMaxPrice(count);
    }

    public void setMaxPrice(int count) {
        if (isLast == 1) { //是最后一件
            String freePrice = ",含邮费 ¥ ";
            if (freightPrice > 0) {
                freePrice += Common.formatFloat(freightPrice);
            } else {
                freePrice = "";
            }

            if (count == maxGoodsCount) {
                totalPrice = (Float.valueOf(currentInfoEntity.real_amount));
                tv_freight.setText("您最多能退 ¥ " + Common.formatFloat(totalPrice) + freePrice);
            } else {
                totalPrice = getTotalPrice(currentInfoEntity.return_price, count);
                tv_freight.setText("您最多能退 ¥ " + totalPrice);
            }
        } else {
            totalPrice = getTotalPrice(currentInfoEntity.return_price, count);
            tv_freight.setText("您最多能退 ¥ " + totalPrice);
        }
        edt_return_money.setText(Common.formatFloat(totalPrice));
        edt_return_money.setSelection(edt_return_money.getText().length());
    }

    public float getTotalPrice(String price, int count) {
        if (isEmpty(price) || count == 0) {
            return 0;
        }
        BigDecimal b2 = new BigDecimal(price);
        BigDecimal b1 = new BigDecimal(count);
        String str = Common.formatFloat(b2.multiply(b1).floatValue());
        return Float.parseFloat(str);
    }

    private List<ImageEntity> getImageEntityList() {
        if (currentInfoEntity.member_evidence_seller == null || currentInfoEntity.member_evidence_seller.size() == 0) {
            return null;
        }
        List<ImageEntity> list = new ArrayList<>();
        for (String str : currentInfoEntity.member_evidence_seller) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.imgUrl = str;
            list.add(imageEntity);
        }
        return list;
    }

    @Override
    public void onSelect(int position) {
        if (position < 0 || isEmpty(currentReasonList)) {
            return;
        }
        currentReasonId = currentReasonList.get(position).reason_id;
        String reason = currentReasonList.get(position).reason_info;
        tv_return_reason.setText(reason);
        tv_return_reason.setTextColor(getColorResouce(R.color.new_text));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_return_reason:
                if (isEmpty(currentReasonList)) {
                    presenter.getRefundReason(currentInfoEntity.order_id, currentServiceType);
                } else {
                    goodsDialog.setRefundReason(currentReasonList, currentReasonId);
                    if (goodsDialog != null && !goodsDialog.isShowing()) {
                        goodsDialog.show();
                    }
                }
                break;
            case R.id.tv_request_complete:
                if (isEmpty(currentReasonId)) {
                    Common.staticToast("请选择退款原因");
                    return;
                }
                if ("3".equals(currentServiceType) || "1".equals(currentServiceType)) {
                    if (isEmpty(edt_return_money.getText())) {
                        Common.staticToast("请输入退款金额");
                        return;
                    }
                }
                if (isEmpty(edt_refunds.getText().toString())) {
                    Common.staticToast("请填写退款说明");
                    return;
                }
                String imageStr = getImageString();
                presenter.applyRefund(currentRefundId, currentInfoEntity.og_Id,
                        String.valueOf(customer_goods.getCurrentCount()),
                        edt_return_money.getText().toString(), currentServiceType,
                        currentReasonId, edt_refunds.getText().toString(), imageStr, isEdit);
                break;
        }
    }

    @Override
    public void applyRefundSuccess(String refundId) {
        finish();
        ExchangeDetailAct.startAct(this, refundId);
    }

    @Override
    public void getReasonList(List<RefundDetailEntity.RefundDetail.Edit.Reason> reasonList) {
        if (isEmpty(reasonList)) {
            return;
        }
        currentReasonList.clear();
        currentReasonList.addAll(reasonList);
        goodsDialog.setRefundReason(currentReasonList, currentReasonId);

        if (goodsDialog != null && !goodsDialog.isShowing()) {
            goodsDialog.show();
        }
    }

    @Override
    public void applyRefundFail(String error) {
        Common.staticToast(error);
    }

    @Override
    public void uploadImg(UploadPicEntity entity) {
        Message message = mHandler.obtainMessage();
        message.obj = entity.relativePath;
        message.what = 1;
        mHandler.sendMessage(message);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public String getImageString() {
        StringBuffer result = new StringBuffer();
        if (imageEntityList == null || imageEntityList.size() == 0) {
            return null;
        }

        for (int i = 0; i < imageEntityList.size(); i++) {
            result.append(imageEntityList.get(i).imgUrl);
            if (i != imageEntityList.size() - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int progress = msg.arg1;
                    String tag = String.valueOf(msg.obj);
//            creatCommentAdapter.updateProgress(currentPosition, tag, progress);
                    break;
                case 1:
                    List<String> imgs = (List<String>) msg.obj;
                    for (int i = 0; i < upLoadList.size(); i++) {
                        upLoadList.get(i).imgUrl = imgs.get(i);
                    }
                    imageEntityList.addAll(upLoadList);
                    singleImgAdapter.setData(imageEntityList);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }
}
