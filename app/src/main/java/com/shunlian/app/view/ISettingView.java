package com.shunlian.app.view;

/**
 * Created by Administrator on 2018/4/24.
 */

public interface ISettingView extends IView {
    /**
     * 推送开关
     * @param push_on
     */
    void pushSwitch(String push_on);

    /**
     * 关于app的url
     * @param about_url
     */
    void aboutUrl(String about_url);

    /**
     * 下载app二维码路径
     * @param appQRCode
     */
    void downAppQRCode(String appQRCode);
}
