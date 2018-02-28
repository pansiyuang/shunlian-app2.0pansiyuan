package com.shunlian.app.view;

import com.shunlian.app.bean.GetListFilterEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IBrandListView extends IView {

    /**
     *设置品牌列表
     * @param letters
     * @param brands
     */
    void setBrandList(ArrayList<String> letters, List<GetListFilterEntity.Brand> brands);
}
