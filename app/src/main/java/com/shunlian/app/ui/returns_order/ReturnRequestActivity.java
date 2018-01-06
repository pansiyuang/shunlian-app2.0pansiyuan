package com.shunlian.app.ui.returns_order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.RefundInfoEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.presenter.ReturnRequestPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IReturnRequestView;
import com.shunlian.app.widget.CustomerGoodsView;
import com.shunlian.app.widget.ReturnGoodsDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.adapter.SingleImgAdapter.REQUEST_CAMERA_CODE;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ReturnRequestActivity extends BaseActivity implements CustomerGoodsView.IChangeCountListener, ReturnGoodsDialog.ISelectListener, View.OnClickListener, IReturnRequestView {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.customer_goods)
    CustomerGoodsView customer_goods;

    @BindView(R.id.rl_return_reason)
    RelativeLayout rl_return_reason;

    @BindView(R.id.edt_return_money)
    EditText edt_return_money;

    @BindView(R.id.tv_freight)
    TextView tv_freight;

    @BindView(R.id.rl_return_money)
    RelativeLayout rl_return_money;

    @BindView(R.id.tv_return_reason)
    TextView tv_return_reason;

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

    /**
     * 输入框小数的位数
     */
    private static final int DECIMAL_DIGITS = 1;
    private RefundInfoEntity currentInfoEntity;
    private List<ImageEntity> imageEntityList;
    private List<ImageEntity> upLoadList = new ArrayList<>();
    private SingleImgAdapter singleImgAdapter;
    private String currentServiceType;  //1 仅退款、 3 退货换货 4 换货
    private int goodsCount;
    private double maxPrice;
    private double freightPrice;
    private double returnPrice;
    private int isLast;  //是否改订单最后一件没有退的商品  1是  0 否
    private ReturnGoodsDialog goodsDialog;
    private String currentReasonId;
    private ReturnRequestPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_request;
    }

    public static void startAct(Context context, RefundInfoEntity infoEntity) {
        Intent intent = new Intent(context, ReturnRequestActivity.class);
        intent.putExtra("infoEntity", infoEntity);
        context.startActivity(intent);
    }


    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        currentInfoEntity = (RefundInfoEntity) getIntent().getSerializableExtra("infoEntity");
        goodsCount = Integer.valueOf(currentInfoEntity.qty);
        freightPrice = Double.valueOf(currentInfoEntity.shipping_fee);
        returnPrice = Double.valueOf(currentInfoEntity.return_price);
        isLast = Integer.valueOf(currentInfoEntity.is_last);

        if (!isEmpty(currentInfoEntity.serviceType)) {
            currentServiceType = currentInfoEntity.serviceType;
            initViews(currentServiceType);
        }
        presenter = new ReturnRequestPresenter(this, this);
    }

    public void initViews(String type) {
        goodsDialog = new ReturnGoodsDialog(this, currentInfoEntity.reason);
        goodsDialog.setSelectListener(this);
        switch (type) {
            case "1": //仅退款
                tv_title.setText(getStringResouce(R.string.return_request));
                customer_goods.setLabelName(getStringResouce(R.string.return_goods), false);
                rl_return_money.setVisibility(View.VISIBLE);
                view_money.setVisibility(View.VISIBLE);

                edt_return_money.setFilters(new InputFilter[]{lengthFilter});
                setMaxPrice(customer_goods.getCurrentCount());
                break;
            case "3": //退货换货
                tv_title.setText(getStringResouce(R.string.return_request));
                customer_goods.setLabelName(getStringResouce(R.string.return_goods), false);
                rl_return_money.setVisibility(View.VISIBLE);
                view_money.setVisibility(View.VISIBLE);
                edt_return_money.setFilters(new InputFilter[]{lengthFilter});

                setMaxPrice(customer_goods.getCurrentCount());
                break;
            case "4": //换货
                tv_title.setText(getStringResouce(R.string.change_request));
                customer_goods.setLabelName(getStringResouce(R.string.change_goods), false);
                rl_return_money.setVisibility(View.GONE);
                tv_request.setText(getStringResouce(R.string.return_instruction));
                tv_return_type.setText(getStringResouce(R.string.return_reason));
                goodsDialog.setDialogTitle(getStringResouce(R.string.return_reason));
                break;
        }

        GlideUtils.getInstance().loadImage(this, customer_goods.getGoodsIcon(), currentInfoEntity.thumb);
        customer_goods.setGoodsTitle(currentInfoEntity.title)
                .setGoodsCount("x" + currentInfoEntity.qty)
                .setGoodsParams(currentInfoEntity.sku_desc)
                .setGoodsPrice(getStringResouce(R.string.common_yuan) + currentInfoEntity.price)
                .selectCount(Integer.valueOf(currentInfoEntity.qty));

        imageEntityList = new ArrayList<>();
        singleImgAdapter = new SingleImgAdapter(this, imageEntityList);
        grid_imgs.setAdapter(singleImgAdapter);
    }

    @Override
    protected void initListener() {
        customer_goods.setIChangeCountListener(this);
        rl_return_reason.setOnClickListener(this);
        tv_request_complete.setOnClickListener(this);
        super.initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imagePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            upLoadList.clear();
            for (String s : imagePaths) {
                ImageEntity imageEntity = new ImageEntity(s);
                upLoadList.add(imageEntity);
            }
            presenter.uploadPic(upLoadList, "refund");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onChangeCount(int count) {
        setMaxPrice(count);
    }

    public void setMaxPrice(int count) {
        if (isLast == 1) { //是最后一件
            if (freightPrice == 0) {
                maxPrice = returnPrice * count;
                tv_freight.setText("您最多能退¥" + maxPrice);
            } else {
                if (count == goodsCount) {
                    maxPrice = count * returnPrice + freightPrice;
                    tv_freight.setText("您最多能退¥" + maxPrice + ",含邮费¥" + freightPrice);
                } else {
                    tv_freight.setText("您最多能退¥" + maxPrice);
                }
            }
        } else {
            maxPrice = count * returnPrice;
            tv_freight.setText("您最多能退¥" + maxPrice);
        }
    }

    private InputFilter lengthFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (dest.length() == 0 && source.equals(".")) {
                return "0.";
            }
            if (dest.length() == 0 && source.equals("0")) {
                return "0.";
            }

            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                if (dotValue.length() == DECIMAL_DIGITS) {
                    return "";
                }
            }
            return null;
        }
    };

    @Override
    public void onSelect(int position) {
        currentReasonId = currentInfoEntity.reason.get(position).reason_id;
        String reason = currentInfoEntity.reason.get(position).reason_info;
        tv_return_reason.setText(reason);
        tv_return_reason.setTextColor(getColorResouce(R.color.new_text));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_return_reason:
                if (goodsDialog != null && !goodsDialog.isShowing()) {
                    goodsDialog.show();
                }
                break;
            case R.id.tv_request_complete:
                if (isEmpty(currentReasonId)) {
                    Common.staticToast("请选择原因");
                    return;
                }
                if ("3".equals(currentServiceType) || "1".equals(currentServiceType)) {
                    if (isEmpty(edt_return_money.getText())) {
                        Common.staticToast("请输入退款金额");
                        return;
                    }
                }
                if (isEmpty(edt_refunds.getText())) {
                    Common.staticToast("请输入说明");
                    return;
                }

                String imageStr = getImageString();
                presenter.applyRefund(currentInfoEntity.og_Id, String.valueOf(customer_goods.getCurrentCount()), edt_return_money.getText().toString(), currentServiceType, currentReasonId, edt_refunds.getText().toString(), imageStr);
                break;
        }
    }

    @Override
    public void applyRefundSuccess(String str) {
        Common.staticToast(str);
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
}
