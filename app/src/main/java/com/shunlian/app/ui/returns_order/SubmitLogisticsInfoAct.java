package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.GridView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.zxing_code.ZXingDemoAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/28.
 */

public class SubmitLogisticsInfoAct extends BaseActivity {

    @BindView(R.id.met_logistics)
    MyEditText met_logistics;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.gv_proof)
    GridView gv_proof;

    @BindView(R.id.met_explain)
    MyEditText met_explain;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, SubmitLogisticsInfoAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_submit_logisticsinfo;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(met_logistics);
                finish();
            }
        });


        met_explain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                met_explain.setSelection(s.length());

                if (!s.toString().startsWith(Common.getPlaceholder(3))){
                    String concat = Common.getPlaceholder(1).concat(s.toString());
                    met_explain.setText(concat);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        met_explain.setText(Common.getPlaceholder(3));
//        SingleImgAdapter singleImgAdapter = new SingleImgAdapter(this, list, position);
//        gv_proof.setAdapter(singleImgAdapter);
    }

    @OnClick(R.id.miv_code)
    public void scanCode() {
        ZXingDemoAct.startAct(this,true, 100);
    }

    @OnClick(R.id.met_logistics)
    public void editNumber(){
        setEdittextFocusable(true,met_logistics);
        Common.showKeyboard(met_logistics);
    }

    @OnClick(R.id.met_explain)
    public void explainText(){
        setEdittextFocusable(true,met_explain);
        Common.showKeyboard(met_explain);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == ZXingDemoAct.RESULT_CODE) {
            String result = data.getStringExtra("result");
            met_logistics.setText(result);
        }
    }

    @Override
    protected void onDestroy() {
        Common.hideKeyboard(met_logistics);
        super.onDestroy();
    }
}
