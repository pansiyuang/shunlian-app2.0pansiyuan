package com.shunlian.app.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/10/17.
 */

public class PhoneTextWatcher implements TextWatcher {

    private EditText numberEditText;
    int beforeLen = 0;
    int afterLen = 0;

    public PhoneTextWatcher(EditText numberEditText) {
        this.numberEditText = numberEditText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeLen = s.length();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String txt = numberEditText.getText().toString();
        afterLen = txt.length();
        if (afterLen > beforeLen) { //输入文字
            if (txt.length() == 4 || txt.length() == 9) {
                numberEditText.setText(new StringBuffer(txt).insert(txt.length() - 1, " ").toString());
                numberEditText.setSelection(numberEditText.getText().length());
            }
        } else {//删除文字
            if (txt.length() == 4 || txt.length() == 9) {
                numberEditText.setText(new StringBuffer(txt).delete(txt.length() - 1, txt.length()).toString());
                numberEditText.setSelection(numberEditText.getText().length());
            }
        }
    }
}
