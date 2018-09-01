package com.shunlian.app.view;

import com.shunlian.app.bean.EggDetailEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IEggDetail extends IView {
    void getApiData(int allPage, int page, List<EggDetailEntity.Out> list);

}
