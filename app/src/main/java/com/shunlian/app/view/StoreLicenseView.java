package com.shunlian.app.view;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface StoreLicenseView extends IView {
    void setCode(byte[] bytes);
    void setLicense(String url);
}
