package com.shunlian.app.newchat.util;


import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shunlian.app.R;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/24.
 */

public class TransferDialog extends Dialog {

    @BindView(R.id.edt_reason)
    EditText edt_reason;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.tv_count)
    TextView tv_count;

    private String currentReason;
    private OnReasonPutListener mListener;

    public TransferDialog(Context context) {
        super(context, R.style.Mydialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_transfer_dialog, null, false);
        setContentView(view);
        ButterKnife.bind(this, view);
        setCanceledOnTouchOutside(false);

        btn_submit.setOnClickListener(v -> {
            currentReason = edt_reason.getText().toString();
            if (currentReason.length() < 5) {
                Toast.makeText(context, "请至少输入5个字符", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mListener != null) {
                mListener.OnSubmit(currentReason);
            }
            dismiss();
        });

        edt_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = edt_reason.getText().toString().trim();
                tv_count.setText(str.length() + "/50");
            }
        });
    }

    public void setOnReasonPutListener(OnReasonPutListener listener) {
        this.mListener = listener;
    }

    public interface OnReasonPutListener {

        void OnSubmit(String reason);
    }
}
