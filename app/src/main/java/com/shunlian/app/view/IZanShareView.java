package com.shunlian.app.view;

import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ZanShareEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

public interface IZanShareView extends IView {

    void getMsgList(List<ZanShareEntity.Msg> list, int page, int totalPage);

    void refreshFinish();
}
