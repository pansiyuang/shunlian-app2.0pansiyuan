package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.ShowSignEntity;
import com.shunlian.app.bean.ShowVoucherSuspension;
import com.shunlian.app.bean.TeamCodeInfoEntity;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.presenter.PMain;
import com.shunlian.app.ui.integral_team.TeamIntegralActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IMain;


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
        try {
            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (!TextUtils.isEmpty(cm.getText()) &&!Constant.SHARE_LINK.equals(cm.getText().toString())&&cm.getText().toString().contains("slAppWord")||cm.getText().toString().contains("天天组队最高瓜分")) {
                if (pMain==null)
                    pMain = new PMain(activity, this);
                pMain.getCommond(cm.getText().toString());
                cm.setText("");
            }
//            else if(!TextUtils.isEmpty(cm.getText()) && !Constant.SHARE_LINK.equals(cm.getText().toString())&&cm.getText().toString().contains("天天组队最高瓜分")){
//                if (pMain==null)
//                    pMain = new PMain(activity, this);
//                pMain.readPassword(cm.getText().toString());
//                cm.setText("");
//            }
        }catch (Exception e){

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
    public void setADs(ShowSignEntity data) {

    }

    @Override
    public void setContent(GetDataEntity data) {

    }

    @Override
    public void setTab(GetMenuEntity data) {

    }

    @Override
    public void setCommond(CommondEntity data) {
        SharedPrefUtil.saveSharedUserString("share_code", data.share_code);
        if (data.type_data != null)
            initCommodDialog(data);
    }

    @Override
    public void setReadPassword(CommondEntity data) {
        if(data.type_data!=null&&data.type_data.is_pop.equals("1")) {
            CommonDialogUtil commondDialog = new CommonDialogUtil(activity);
            commondDialog.teamPasteCommonDialog(data.type_data, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Common.isAlreadyLogin()) {
                        if (commondDialog.dialog_team_paste != null && commondDialog.dialog_team_paste.isShowing()) {
                            commondDialog.dialog_team_paste.dismiss();
                        }
                        TeamIntegralActivity.startAct(activity, data.type_data.password);
                    }else{
                        Common.goGoGo(activity,"login");
                    }
                }
            });
        }
    }

    @Override
    public void setUpdateInfo(UpdateEntity data) {

    }

    @Override
    public void entryInfo(CommonEntity data) {

    }

    @Override
    public void isShowNew(CommonEntity data) {

    }

    @Override
    public void getPrize(CommonEntity data) {

    }

    @Override
    public void setDiscoveryUnreadCount(CommonEntity data) {

    }

    @Override
    public void showVoucherSuspension(ShowVoucherSuspension voucherSuspension) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
