package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.presenter.PMain;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DownloadService;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IMain;

import java.io.File;


/**
 * Created by Administrator on 2016/12/13 0013.
 */

public class CommondDialog implements IMain {
    public Dialog dialog_commond;
    private Activity activity;
    private PMain pMain;
    //按返回键不取消dialog
    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    /**
     * 解析剪切板内容
     * @param
     */
    public void parseCommond(){
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (!TextUtils.isEmpty(cm.getText()) && cm.getText().toString().contains("slAppWord")) {
            if (pMain==null)
            pMain = new PMain(activity, this);
            pMain.getCommond(cm.getText().toString());
            cm.setText("");
        }
    }


    public CommondDialog(Activity activity) {
        this.activity = activity;

    }


    public void initCommodDialog(CommondEntity data) {
        if (dialog_commond == null) {
            dialog_commond = new Dialog(activity, R.style.popAd);
            dialog_commond.setContentView(R.layout.dialog_commond);
            dialog_commond.setOnKeyListener(keylistener);
            MyImageView miv_close = (MyImageView) dialog_commond.findViewById(R.id.miv_close);
            MyImageView miv_photo = (MyImageView) dialog_commond.findViewById(R.id.miv_photo);
            NewTextView ntv_title = (NewTextView) dialog_commond.findViewById(R.id.ntv_title);
            NewTextView ntv_price = (NewTextView) dialog_commond.findViewById(R.id.ntv_price);
            NewTextView ntv_detail = (NewTextView) dialog_commond.findViewById(R.id.ntv_detail);
            NewTextView ntv_from = (NewTextView) dialog_commond.findViewById(R.id.ntv_from);
            GlideUtils.getInstance().loadCornerImage(activity, miv_photo, data.type_data.thumb,4);
            if (!TextUtils.isEmpty(data.type_data.price)){
                SpannableStringBuilder spannableStringBuilder= Common.changeTextSize(activity.getString(R.string.common_yuan)+data.type_data.price,activity.getString(R.string.common_yuan),22);
                ntv_price.setText(spannableStringBuilder);
                ntv_price.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(data.type_data.title)) {
                ntv_title.setText(data.type_data.title);
                ntv_title.setVisibility(View.VISIBLE);
            }
            ntv_from.setText("一  "+data.type_data.from_user+"  一");
            miv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_commond.dismiss();
                }
            });
            ntv_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.goGoGo(activity, data.type,data.type_data.id);
                    dialog_commond.dismiss();
                }
            });
            dialog_commond.setCancelable(false);
        }
        dialog_commond.show();
    }

    @Override
    public void setAD(AdEntity data) {

    }

    @Override
    public void setCommond(CommondEntity data) {
        initCommodDialog(data);
    }

    @Override
    public void setUpdateInfo(UpdateEntity data) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
