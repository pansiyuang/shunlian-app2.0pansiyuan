package com.shunlian.app.view;

import com.shunlian.app.bean.LuckDrawEntity;
import com.shunlian.app.bean.TurnTableEntity;
import com.shunlian.app.bean.TurnTablePopEntity;

/**
 * Created by Administrator on 2018/8/31.
 */

public interface ITurnTableView extends IView {

    void getTurnData(TurnTableEntity turnTableEntity);

    void getLuckDraw(LuckDrawEntity luckDrawEntity);

    void getTurnPop(TurnTablePopEntity turnTablePopEntity);

    void getShareImg(String shareImg);
}
