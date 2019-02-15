package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by zhanghe on 2019/2/15.
 */

public class ModifyCountDialog {

    private Activity mActivity;
    private Dialog logoutDialog;
    private MyImageView miv_reduce;
    private MyImageView miv_add;
    private EditText mtv_edit_count;
    private long min_count;
    private long max_count;
    private TextView tv_cancel;
    private TextView tv_sure;
    private IModifyCountListener mListener;

    public ModifyCountDialog(Activity activity){
        mActivity = activity;
        logoutDialog = new Dialog(mActivity, R.style.Mydialog);
        logoutDialog.setContentView(R.layout.dialog_modify_count);
        WindowManager.LayoutParams attributes = logoutDialog.getWindow().getAttributes();
        attributes.width = TransformUtil.countRealWidth(mActivity,540);
        logoutDialog.getWindow().setAttributes(attributes);
        initView(logoutDialog);
        setCancelable(false);

        initListener();
    }



    private void initView(Dialog logoutDialog) {
        miv_reduce = logoutDialog.findViewById(R.id.miv_reduce);
        miv_add = logoutDialog.findViewById(R.id.miv_add);
        mtv_edit_count = logoutDialog.findViewById(R.id.mtv_edit_count);
        tv_cancel = logoutDialog.findViewById(R.id.tv_cancel);
        tv_sure = logoutDialog.findViewById(R.id.tv_sure);
    }


    public void setMin_MaxCount(long min_count,long max_count,long current_count){
        this.min_count = min_count;
        this.max_count = max_count;
        mtv_edit_count.setText(String.valueOf(current_count));
        mtv_edit_count.setSelection(String.valueOf(current_count).length());
    }

    private void initListener() {
        //减少数量
        miv_reduce.setOnClickListener(v -> {
            try {
                String content = mtv_edit_count.getText().toString();
                long i = Long.parseLong(content);
                i -= 1;
                if (i < min_count){
                    i = min_count;
                    Common.staticToast("宝贝不能再减少了哦");
                }

                mtv_edit_count.setText(String.valueOf(i));
                mtv_edit_count.setSelection(String.valueOf(i).length());
            }catch (Exception e){}
        });
        //增加数量
        miv_add.setOnClickListener(v -> {
            String content = mtv_edit_count.getText().toString();
            addCount(content);
        });

        mtv_edit_count.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                try {
                    if (TextUtils.isEmpty(s))return;
                    long i = Long.parseLong(s.toString());
                    if (i > max_count) {
                        i = max_count;
                        Common.staticToast("您选择的数量超出库存");
                        mtv_edit_count.setText(String.valueOf(i));
                        mtv_edit_count.setSelection(mtv_edit_count.getText().length());
                    }
                } catch (Exception e) {}
            }
        });

        //编辑数量
        mtv_edit_count.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                finishModify();
            }
            return false;
        });


        tv_sure.setOnClickListener(v -> {
            finishModify();
        });

        tv_cancel.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void finishModify() {
        if (mListener != null && mtv_edit_count != null) {
            String s = mtv_edit_count.getText().toString();
            if (!TextUtils.isEmpty(s) && !"0".equals(s))
                mListener.modifyCount(s);
        }
        dismiss();
    }

    private void addCount(String content) {
        try {
            long i = Long.parseLong(content);
            i += 1;
            if (i > max_count){
                i = max_count;
                Common.staticToast("您选择的数量超出库存");
            }

            mtv_edit_count.setText(String.valueOf(i));
            mtv_edit_count.setSelection(String.valueOf(i).length());
        }catch (Exception e){}
    }

    public void setCancelable(boolean flag) {
        if (logoutDialog != null)
            logoutDialog.setCancelable(flag);
    }

    protected void setEdittextFocusable(boolean focusable,EditText... editText){
        for (int i = 0; i < editText.length; i++) {
            editText[i].setFocusable(focusable);
            editText[i].setFocusableInTouchMode(focusable);
            if (focusable){
                editText[i].requestFocus();
            }
        }
    }


    public void show() {
        if (!mActivity.isFinishing()&&logoutDialog!=null&&!logoutDialog.isShowing()) {
            logoutDialog.show();
        }
        if (mtv_edit_count != null){
            showPopInput(mActivity,mtv_edit_count);
        }
    }

    public void dismiss() {
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
        if (mtv_edit_count != null){
            hintPopInput(mActivity,mtv_edit_count);
        }
    }

    /**
     * 释放dialog 防止内存泄漏
     */
    public void release(){
        dismiss();
        logoutDialog=null;
        tv_cancel = null;
        tv_sure = null;
        miv_add = null;
        miv_reduce = null;
        mtv_edit_count = null;
    }


    public void showPopInput(final Context context, final View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    public void hintPopInput(final Context context, final View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 2);
        }
        if (context instanceof Activity)
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void setOnModifyCountListener(IModifyCountListener listener){
        mListener = listener;
    }

    public interface IModifyCountListener{
        void modifyCount(String count);
    }
}
